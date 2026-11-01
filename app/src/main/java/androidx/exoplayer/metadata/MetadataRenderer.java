package androidx.media3.exoplayer.metadata;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.BaseRenderer;
import androidx.media3.exoplayer.FormatHolder;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.extractor.metadata.MetadataDecoder;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.dataflow.qual.SideEffectFree;

/* JADX INFO: loaded from: classes.dex */
public final class MetadataRenderer extends BaseRenderer implements Handler.Callback {
    private static final int MSG_INVOKE_RENDERER = 1;
    private static final String TAG = "MetadataRenderer";
    private final MetadataInputBuffer buffer;
    private MetadataDecoder decoder;
    private final MetadataDecoderFactory decoderFactory;
    private boolean inputStreamEnded;
    private final MetadataOutput output;
    private final Handler outputHandler;
    private final boolean outputMetadataEarly;
    private boolean outputStreamEnded;
    private long outputStreamOffsetUs;
    private Metadata pendingMetadata;
    private long subsampleOffsetUs;

    public MetadataRenderer(MetadataOutput r2, Looper r3) {
        this(r2, r3, MetadataDecoderFactory.DEFAULT);
    }

    public MetadataRenderer(MetadataOutput r2, Looper r3, MetadataDecoderFactory r4) {
        this(r2, r3, r4, false);
    }

    public MetadataRenderer(MetadataOutput r3, Looper r4, MetadataDecoderFactory r5, boolean r6) {
        super(5);
        this.output = (MetadataOutput) Assertions.checkNotNull(r3);
        if (r4 != null) goto L5;
        Handler r0 = null;
    L6:
        this.outputHandler = r0;
        this.decoderFactory = (MetadataDecoderFactory) Assertions.checkNotNull(r5);
        this.outputMetadataEarly = r6;
        this.buffer = new MetadataInputBuffer();
        this.outputStreamOffsetUs = C0180C.TIME_UNSET;
        return;
    L5:
        r0 = Util.createHandler(r4, this);
        goto L6
    }

    private void decodeWrappedMetadata(Metadata r7, List<Metadata.Entry> r8) {
        int r0 = 0;
    L4:
        if (r0 >= r7.length()) goto L15;
        Format r1 = r7.get(r0).getWrappedMetadataFormat();
        if (r1 != null) goto L8;
    L13:
        r8.add(r7.get(r0));
    L14:
        r0 = r0 + 1;
        goto L4
    L8:
        if (this.decoderFactory.supportsFormat(r1) == false) goto L13;
        MetadataDecoder r2 = this.decoderFactory.createDecoder(r1);
        byte[] r3 = (byte[]) Assertions.checkNotNull(r7.get(r0).getWrappedMetadataBytes());
        this.buffer.clear();
        this.buffer.ensureSpaceForWrite(r3.length);
        ((ByteBuffer) Util.castNonNull(this.buffer.data)).put(r3);
        this.buffer.flip();
        Metadata r4 = r2.decode(this.buffer);
        if (r4 == null) goto L14;
        decodeWrappedMetadata(r4, r8);
        goto L14
    }

    @SideEffectFree
    private long getPresentationTimeUs(long r8) {
        boolean r3 = true;
        if (r8 == C0180C.TIME_UNSET) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkState(r2);
        if (this.outputStreamOffsetUs != C0180C.TIME_UNSET) goto L10;
        r3 = false;
    L10:
        Assertions.checkState(r3);
        return r8 - this.outputStreamOffsetUs;
    L5:
        r2 = false;
        goto L6
    }

    private void invokeRenderer(Metadata r3) {
        if (this.outputHandler == null) goto L5;
        this.outputHandler.obtainMessage(1, r3).sendToTarget();
        return;
    L5:
        invokeRendererInternal(r3);
    }

    private void invokeRendererInternal(Metadata r2) {
        this.output.onMetadata(r2);
    }

    private boolean outputMetadata(long r6) {
        boolean r0 = false;
        if (this.pendingMetadata == null) goto L10;
        if (this.outputMetadataEarly == false) goto L7;
    L8:
        invokeRenderer(this.pendingMetadata);
        this.pendingMetadata = null;
        r0 = true;
        goto L10
    L7:
        if (this.pendingMetadata.presentationTimeUs <= getPresentationTimeUs(r6)) goto L8;
    L10:
        if (this.inputStreamEnded == true) goto L12;
    L14:
        return r0;
    L12:
        if (this.pendingMetadata != null) goto L14;
        this.outputStreamEnded = true;
        goto L14
    }

    private void readMetadata() {
        if (this.inputStreamEnded == false) goto L5;
        return;
    L5:
        if (this.pendingMetadata != null) goto L24;
        this.buffer.clear();
        FormatHolder r0 = getFormatHolder();
        int r1 = readSource(r0, this.buffer, 0);
        if (r1 != (-4)) goto L20;
        if (this.buffer.isEndOfStream() == false) goto L12;
        this.inputStreamEnded = true;
        return;
    L12:
        if (this.buffer.timeUs < getLastResetPositionUs()) goto L26;
        MetadataInputBuffer r2 = this.buffer;
        r2.subsampleOffsetUs = this.subsampleOffsetUs;
        this.buffer.flip();
        Metadata r3 = ((MetadataDecoder) Util.castNonNull(this.decoder)).decode(this.buffer);
        if (r3 == null) goto L29;
        ArrayList r4 = new ArrayList(r3.length());
        decodeWrappedMetadata(r3, r4);
        if (r4.isEmpty() == true) goto L30;
        this.pendingMetadata = new Metadata(getPresentationTimeUs(this.buffer.timeUs), r4);
        return;
    L30:
        return;
    L29:
        return;
    L26:
        return;
    L20:
        if (r1 != (-5)) goto L27;
        this.subsampleOffsetUs = ((Format) Assertions.checkNotNull(r0.format)).subsampleOffsetUs;
        return;
    L27:
        return;
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
        invokeRendererInternal((Metadata) r2.obj);
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
        return true;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onDisabled() {
        this.pendingMetadata = null;
        this.decoder = null;
        this.outputStreamOffsetUs = C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onPositionReset(long r2, boolean r4) {
        this.pendingMetadata = null;
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
    }

    @Override // androidx.media3.exoplayer.BaseRenderer
    protected void onStreamChanged(Format[] r6, long r7, long r9, MediaSource.MediaPeriodId r11) {
        this.decoder = this.decoderFactory.createDecoder(r6[0]);
        if (this.pendingMetadata == null) goto L5;
        this.pendingMetadata = this.pendingMetadata.copyWithPresentationTimeUs((this.pendingMetadata.presentationTimeUs + this.outputStreamOffsetUs) - r9);
    L5:
        this.outputStreamOffsetUs = r9;
    }

    @Override // androidx.media3.exoplayer.Renderer
    public void render(long r2, long r4) {
        boolean r0 = true;
    L3:
        if (r0 == false) goto L5;
        readMetadata();
        r0 = outputMetadata(r2);
        goto L3
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities
    public int supportsFormat(Format r2) {
        if (this.decoderFactory.supportsFormat(r2) == false) goto L11;
        if (r2.cryptoType != 0) goto L7;
        int r0 = 4;
    L9:
        return RendererCapabilities.CC.create(r0);
    L7:
        r0 = 2;
        goto L9
    L11:
        return RendererCapabilities.CC.create(0);
    }
}
