package androidx.media3.exoplayer.text;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.extractor.text.CueDecoder;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleDecoder;
import androidx.media3.extractor.text.SubtitleDecoderException;
import androidx.media3.extractor.text.SubtitleInputBuffer;
import androidx.media3.extractor.text.SubtitleOutputBuffer;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.checkerframework.dataflow.qual.SideEffectFree;

/* JADX INFO: loaded from: classes.dex */
public final class TextRenderer extends BaseRenderer implements Handler.Callback {
    private static final int MSG_UPDATE_OUTPUT = 1;
    private static final int REPLACEMENT_STATE_NONE = 0;
    private static final int REPLACEMENT_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REPLACEMENT_STATE_WAIT_END_OF_STREAM = 2;
    private static final String TAG = "TextRenderer";
    private final CueDecoder cueDecoder;
    private final DecoderInputBuffer cueDecoderInputBuffer;
    private CuesResolver cuesResolver;
    private int decoderReplacementState;
    private long finalStreamEndPositionUs;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private long lastRendererPositionUs;
    private boolean legacyDecodingEnabled;
    private SubtitleOutputBuffer nextSubtitle;
    private int nextSubtitleEventIndex;
    private SubtitleInputBuffer nextSubtitleInputBuffer;
    private final TextOutput output;
    private final Handler outputHandler;
    private boolean outputStreamEnded;
    private IOException streamError;
    private Format streamFormat;
    private SubtitleOutputBuffer subtitle;
    private SubtitleDecoder subtitleDecoder;
    private final SubtitleDecoderFactory subtitleDecoderFactory;
    private boolean waitingForKeyFrame;

    public TextRenderer(TextOutput r2, Looper r3) {
        this(r2, r3, SubtitleDecoderFactory.DEFAULT);
    }

    public TextRenderer(TextOutput r3, Looper r4, SubtitleDecoderFactory r5) {
        super(3);
        this.output = (TextOutput) Assertions.checkNotNull(r3);
        if (r4 != null) goto L5;
        Handler r0 = null;
    L6:
        this.outputHandler = r0;
        this.subtitleDecoderFactory = r5;
        this.cueDecoder = new CueDecoder();
        this.cueDecoderInputBuffer = new DecoderInputBuffer(1);
        this.formatHolder = new FormatHolder();
        this.finalStreamEndPositionUs = C0180C.TIME_UNSET;
        this.lastRendererPositionUs = C0180C.TIME_UNSET;
        this.legacyDecodingEnabled = false;
        return;
    L5:
        r0 = Util.createHandler(r4, this);
        goto L6
    }

    @RequiresNonNull({"streamFormat"})
    private void assertLegacyDecodingEnabledIfRequired() {
        if (this.legacyDecodingEnabled == false) goto L5;
    L12:
        boolean r0 = true;
    L13:
        Assertions.checkState(r0, "Legacy decoding is disabled, can't handle " + this.streamFormat.sampleMimeType + " samples (expected " + MimeTypes.APPLICATION_MEDIA3_CUES + ").");
        return;
    L5:
        if (Objects.equals(this.streamFormat.sampleMimeType, MimeTypes.APPLICATION_CEA608) == true) goto L12;
        if (Objects.equals(this.streamFormat.sampleMimeType, MimeTypes.APPLICATION_MP4CEA608) == true) goto L12;
        if (Objects.equals(this.streamFormat.sampleMimeType, MimeTypes.APPLICATION_CEA708) == true) goto L12;
        r0 = false;
        goto L13
    }

    private void clearOutput() {
        updateOutput(new CueGroup(ImmutableList.m212of(), getPresentationTimeUs(this.lastRendererPositionUs)));
    }

    @RequiresNonNull({"subtitle"})
    @SideEffectFree
    private long getCurrentEventTimeUs(long r4) {
        int r0 = this.subtitle.getNextEventTimeIndex(r4);
        if (r0 == 0) goto L13;
        if (this.subtitle.getEventTimeCount() == 0) goto L13;
        if (r0 != (-1)) goto L11;
        return this.subtitle.getEventTime(this.subtitle.getEventTimeCount() - 1);
    L11:
        return this.subtitle.getEventTime(r0 - 1);
    L13:
        return this.subtitle.timeUs;
    }

    private long getNextEventTime() {
        if (this.nextSubtitleEventIndex != (-1)) goto L5;
        return Long.MAX_VALUE;
    L5:
        Assertions.checkNotNull(this.subtitle);
        if (this.nextSubtitleEventIndex < this.subtitle.getEventTimeCount()) goto L9;
        return Long.MAX_VALUE;
    L9:
        return this.subtitle.getEventTime(this.nextSubtitleEventIndex);
    }

    @SideEffectFree
    private long getPresentationTimeUs(long r3) {
        if (r3 == C0180C.TIME_UNSET) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        return r3 - getStreamOffsetUs();
    L5:
        r0 = false;
        goto L6
    }

    private void handleDecoderError(SubtitleDecoderException r3) {
        Log.m45e(TAG, "Subtitle decoding failed. streamFormat=" + this.streamFormat, r3);
        clearOutput();
        replaceSubtitleDecoder();
    }

    private static boolean hasNoEventsAfter(Subtitle r3, long r4) {
        if (r3 != null) goto L5;
        return true;
    L5:
        if (r3.getEventTime(r3.getEventTimeCount() - 1) <= r4) goto L10;
        return false;
    L10:
        return true;
    }

    private void initSubtitleDecoder() {
        this.waitingForKeyFrame = true;
        this.subtitleDecoder = this.subtitleDecoderFactory.createDecoder((Format) Assertions.checkNotNull(this.streamFormat));
        this.subtitleDecoder.setOutputStartTimeUs(getLastResetPositionUs());
    }

    private void invokeUpdateOutputInternal(CueGroup r3) {
        this.output.onCues(r3.cues);
        this.output.onCues(r3);
    }

    @SideEffectFree
    private static boolean isCuesWithTiming(Format r2) {
        return Objects.equals(r2.sampleMimeType, MimeTypes.APPLICATION_MEDIA3_CUES);
    }

    @RequiresNonNull({"this.cuesResolver"})
    private boolean readAndDecodeCuesWithTiming(long r9) {
        if (this.inputStreamEnded == false) goto L6;
        return false;
    L6:
        switch(readSource(this.formatHolder, this.cueDecoderInputBuffer, 0)) {
            case -4: goto L9;
            default: goto L7;
        };
    L7:
        return false;
    L9:
        if (this.cueDecoderInputBuffer.isEndOfStream() == false) goto L12;
        this.inputStreamEnded = true;
        return false;
    L12:
        this.cueDecoderInputBuffer.flip();
        ByteBuffer r1 = (ByteBuffer) Assertions.checkNotNull(this.cueDecoderInputBuffer.data);
        CuesWithTiming r2 = this.cueDecoder.decode(this.cueDecoderInputBuffer.timeUs, r1.array(), r1.arrayOffset(), r1.limit());
        this.cueDecoderInputBuffer.clear();
        return this.cuesResolver.addCues(r2, r9);
    }

    private void releaseSubtitleBuffers() {
        this.nextSubtitleInputBuffer = null;
        this.nextSubtitleEventIndex = -1;
        if (this.subtitle == null) goto L6;
        this.subtitle.release();
        this.subtitle = null;
    L6:
        if (this.nextSubtitle == null) goto L9;
        this.nextSubtitle.release();
        this.nextSubtitle = null;
        return;
    }

    private void releaseSubtitleDecoder() {
        releaseSubtitleBuffers();
        ((SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder)).release();
        this.subtitleDecoder = null;
        this.decoderReplacementState = 0;
    }

    @RequiresNonNull({"this.cuesResolver"})
    private void renderFromCuesWithTiming(long r10) {
        boolean r0 = readAndDecodeCuesWithTiming(r10);
        long r1 = this.cuesResolver.getNextCueChangeTimeUs(this.lastRendererPositionUs);
        if (r1 != Long.MIN_VALUE) goto L9;
        if (this.inputStreamEnded == false) goto L9;
        if (r0 == true) goto L9;
        this.outputStreamEnded = true;
    L9:
        if (r1 != Long.MIN_VALUE) goto L11;
    L13:
        if (r0 == false) goto L15;
        ImmutableList<Cue> r3 = this.cuesResolver.getCuesAtTimeUs(r10);
        long r4 = this.cuesResolver.getPreviousCueChangeTimeUs(r10);
        updateOutput(new CueGroup(r3, getPresentationTimeUs(r4)));
        this.cuesResolver.discardCuesBeforeTimeUs(r4);
    L15:
        this.lastRendererPositionUs = r10;
        return;
    L11:
        if (r1 > r10) goto L13;
        r0 = true;
        goto L13
    }

    private void renderFromSubtitles(long r11) {
        this.lastRendererPositionUs = r11;
        if (this.nextSubtitle != null) goto L11;
        ((SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder)).setPositionUs(r11);
        this.nextSubtitle = ((SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder)).dequeueOutputBuffer();     // Catch: SubtitleDecoderException -> L7
    L7:
        e = move-exception;
        handleDecoderError(e);
        return;
    L11:
        if (getState() == 2) goto L13;
        return;
    L13:
        boolean r0 = false;
        if (this.subtitle == null) goto L20;
        long r4 = getNextEventTime();
    L17:
        if (r4 > r11) goto L20;
        this.nextSubtitleEventIndex++;
        r4 = getNextEventTime();
        r0 = true;
    L20:
        if (this.nextSubtitle == null) goto L36;
        SubtitleOutputBuffer r2 = this.nextSubtitle;
        if (r2.isEndOfStream() == false) goto L31;
        if (r0 == true) goto L36;
        if (getNextEventTime() != Long.MAX_VALUE) goto L36;
        if (this.decoderReplacementState != 2) goto L29;
        replaceSubtitleDecoder();
        goto L36
    L29:
        releaseSubtitleBuffers();
        this.outputStreamEnded = true;
        goto L36
    L31:
        if (r2.timeUs > r11) goto L36;
        if (this.subtitle == null) goto L35;
        this.subtitle.release();
    L35:
        this.nextSubtitleEventIndex = r2.getNextEventTimeIndex(r11);
        this.subtitle = r2;
        this.nextSubtitle = null;
        r0 = true;
    L36:
        if (r0 == false) goto L39;
        Assertions.checkNotNull(this.subtitle);
        updateOutput(new CueGroup(this.subtitle.getCues(r11), getPresentationTimeUs(getCurrentEventTimeUs(r11))));
    L39:
        if (this.decoderReplacementState != 2) goto L79;
        return;
    L79:
    L74:
        e = move-exception;
        handleDecoderError(e);
        return;
    L42:
        if (this.inputStreamEnded == true) goto L94;
        SubtitleInputBuffer r3 = this.nextSubtitleInputBuffer;     // Catch: SubtitleDecoderException -> L74
        if (r3 != null) goto L50;
        r3 = ((SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder)).dequeueInputBuffer();     // Catch: SubtitleDecoderException -> L74
        if (r3 == null) goto L47;
        this.nextSubtitleInputBuffer = r3;     // Catch: SubtitleDecoderException -> L74
        goto L50
    L47:
        return;
    L50:
        if (this.decoderReplacementState == 1) goto L51;
        boolean r6 = false;
        int r5 = readSource(this.formatHolder, r3, 0);     // Catch: SubtitleDecoderException -> L74
        if (r5 == (-4)) goto L56;
        if (r5 != (-3)) goto L79;
        return;
    L56:
        if (r3.isEndOfStream() == false) goto L58;
        this.inputStreamEnded = true;     // Catch: SubtitleDecoderException -> L74
        this.waitingForKeyFrame = false;     // Catch: SubtitleDecoderException -> L74
    L66:
        if (this.waitingForKeyFrame == true) goto L79;
        ((SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder)).queueInputBuffer(r3);     // Catch: SubtitleDecoderException -> L74
        this.nextSubtitleInputBuffer = null;     // Catch: SubtitleDecoderException -> L74
        goto L79
    L58:
        Format r7 = this.formatHolder.format;     // Catch: SubtitleDecoderException -> L74
        if (r7 == null) goto L60;
        r3.subsampleOffsetUs = r7.subsampleOffsetUs;     // Catch: SubtitleDecoderException -> L74
        r3.flip();     // Catch: SubtitleDecoderException -> L74
        boolean r8 = this.waitingForKeyFrame;     // Catch: SubtitleDecoderException -> L74
        if (r3.isKeyFrame() == true) goto L64;
        r6 = true;
    L64:
        this.waitingForKeyFrame = r6 & r8;     // Catch: SubtitleDecoderException -> L74
        goto L66
    L60:
        return;
    L51:
        r3.setFlags(4);     // Catch: SubtitleDecoderException -> L74
        ((SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder)).queueInputBuffer(r3);     // Catch: SubtitleDecoderException -> L74
        this.nextSubtitleInputBuffer = null;     // Catch: SubtitleDecoderException -> L74
        this.decoderReplacementState = 2;     // Catch: SubtitleDecoderException -> L74
        return;
    }

    private void replaceSubtitleDecoder() {
        releaseSubtitleDecoder();
        initSubtitleDecoder();
    }

    private void updateOutput(CueGroup r3) {
        if (this.outputHandler == null) goto L5;
        this.outputHandler.obtainMessage(1, r3).sendToTarget();
        return;
    L5:
        invokeUpdateOutputInternal(r3);
    }

    @Deprecated
    public void experimentalSetLegacyDecodingEnabled(boolean r1) {
        this.legacyDecodingEnabled = r1;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public String getName() {
        return TAG;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message r2) {
        switch(r2.what) {
            case 1: goto L6;
            default: goto L5;
        };
    L6:
        invokeUpdateOutputInternal((CueGroup) r2.obj);
        return true;
    L5:
        throw new IllegalStateException();
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public boolean isReady() {
        if (this.streamFormat != null) goto L6;
        return true;
    L6:
        if (this.streamError != null) goto L12;
        maybeThrowStreamError();     // Catch: IOException -> L9
    L9:
        e = move-exception;
        this.streamError = e;
    L12:
        if (this.streamError != null) goto L14;
    L31:
        return true;
    L14:
        if (isCuesWithTiming((Format) Assertions.checkNotNull(this.streamFormat)) == false) goto L21;
        if (((CuesResolver) Assertions.checkNotNull(this.cuesResolver)).getNextCueChangeTimeUs(this.lastRendererPositionUs) == Long.MIN_VALUE) goto L19;
        return true;
    L19:
        return false;
    L21:
        if (this.outputStreamEnded == false) goto L23;
    L30:
        return false;
    L23:
        if (this.inputStreamEnded == false) goto L31;
        if (hasNoEventsAfter(this.subtitle, this.lastRendererPositionUs) == false) goto L31;
        if (hasNoEventsAfter(this.nextSubtitle, this.lastRendererPositionUs) == false) goto L31;
        if (this.nextSubtitleInputBuffer == null) goto L31;
        goto L30
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.streamFormat = null;
        this.finalStreamEndPositionUs = C0180C.TIME_UNSET;
        clearOutput();
        this.lastRendererPositionUs = C0180C.TIME_UNSET;
        if (this.subtitleDecoder == null) goto L6;
        releaseSubtitleDecoder();
        return;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r4, boolean r6) {
        this.lastRendererPositionUs = r4;
        if (this.cuesResolver == null) goto L5;
        this.cuesResolver.clear();
    L5:
        clearOutput();
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        this.finalStreamEndPositionUs = C0180C.TIME_UNSET;
        if (this.streamFormat != null) goto L8;
        return;
    L8:
        if (isCuesWithTiming(this.streamFormat) == false) goto L10;
        return;
    L10:
        if (this.decoderReplacementState == 0) goto L12;
        replaceSubtitleDecoder();
        return;
    L12:
        releaseSubtitleBuffers();
        SubtitleDecoder r0 = (SubtitleDecoder) Assertions.checkNotNull(this.subtitleDecoder);
        r0.flush();
        r0.setOutputStartTimeUs(getLastResetPositionUs());
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r3, long r4, long r6, MediaSource.MediaPeriodId r8) {
        this.streamFormat = r3[0];
        if (isCuesWithTiming(this.streamFormat) == true) goto L9;
        assertLegacyDecodingEnabledIfRequired();
        if (this.subtitleDecoder == null) goto L7;
        this.decoderReplacementState = 1;
        return;
    L7:
        initSubtitleDecoder();
        return;
    L9:
        if (this.streamFormat.cueReplacementBehavior != 1) goto L11;
        CuesResolver r0 = new MergingCuesResolver();
    L12:
        this.cuesResolver = r0;
        return;
    L11:
        r0 = new ReplacingCuesResolver();
        goto L12
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r5, long r7) {
        if (isCurrentStreamFinal() == false) goto L10;
        if (this.finalStreamEndPositionUs == C0180C.TIME_UNSET) goto L10;
        if (r5 < this.finalStreamEndPositionUs) goto L10;
        releaseSubtitleBuffers();
        this.outputStreamEnded = true;
    L10:
        if (this.outputStreamEnded == false) goto L13;
        return;
    L13:
        if (isCuesWithTiming((Format) Assertions.checkNotNull(this.streamFormat)) == false) goto L15;
        Assertions.checkNotNull(this.cuesResolver);
        renderFromCuesWithTiming(r5);
        return;
    L15:
        assertLegacyDecodingEnabledIfRequired();
        renderFromSubtitles(r5);
    }

    public void setFinalStreamEndPositionUs(long r2) {
        Assertions.checkState(isCurrentStreamFinal());
        this.finalStreamEndPositionUs = r2;
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public int supportsFormat(Format r2) {
        if (isCuesWithTiming(r2) == true) goto L14;
        if (this.subtitleDecoderFactory.supportsFormat(r2) == true) goto L14;
        if (MimeTypes.isText(r2.sampleMimeType) == false) goto L12;
        return RendererCapabilities.CC.create(1);
    L12:
        return RendererCapabilities.CC.create(0);
    L14:
        if (r2.cryptoType != 0) goto L16;
        int r0 = 4;
    L18:
        return RendererCapabilities.CC.create(r0);
    L16:
        r0 = 2;
        goto L18
    }
}
