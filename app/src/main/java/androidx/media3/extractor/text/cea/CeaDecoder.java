package androidx.media3.extractor.text.cea;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderException;
import androidx.media3.decoder.DecoderOutputBuffer;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleDecoder;
import androidx.media3.extractor.text.SubtitleDecoderException;
import androidx.media3.extractor.text.SubtitleInputBuffer;
import androidx.media3.extractor.text.SubtitleOutputBuffer;
import java.util.ArrayDeque;

/* JADX INFO: loaded from: classes.dex */
abstract class CeaDecoder implements SubtitleDecoder {
    private static final int NUM_INPUT_BUFFERS = 10;
    private static final int NUM_OUTPUT_BUFFERS = 2;
    private final ArrayDeque<CeaInputBuffer> availableInputBuffers;
    private final ArrayDeque<SubtitleOutputBuffer> availableOutputBuffers;
    private CeaInputBuffer dequeuedInputBuffer;
    private long outputStartTimeUs;
    private long playbackPositionUs;
    private long queuedInputBufferCount;
    private final ArrayDeque<CeaInputBuffer> queuedInputBuffers;

    /* JADX INFO: renamed from: androidx.media3.extractor.text.cea.CeaDecoder$1 */
    static /* synthetic */ class C04451 {
    }

    private static final class CeaInputBuffer extends SubtitleInputBuffer implements Comparable<CeaInputBuffer> {
        private long queuedInputBufferCount;

        private CeaInputBuffer() {
        }

        /* synthetic */ CeaInputBuffer(C04451 r1) {
            this();
        }

        static /* synthetic */ long access$102(CeaInputBuffer r0, long r1) {
            r0.queuedInputBufferCount = r1;
            return r1;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(CeaInputBuffer r11) {
            if (isEndOfStream() != r11.isEndOfStream()) goto L5;
            long r0 = this.timeUs - r11.timeUs;
            if (r0 != 0) goto L16;
            r0 = this.queuedInputBufferCount - r11.queuedInputBufferCount;
            if (r0 != 0) goto L16;
            return 0;
        L16:
            if (r0 <= 0) goto L19;
            return 1;
        L19:
            return -1;
        L5:
            if (isEndOfStream() == false) goto L8;
            return 1;
        L8:
            return -1;
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(CeaInputBuffer r1) {
            return compareTo2(r1);
        }
    }

    private static final class CeaOutputBuffer extends SubtitleOutputBuffer {
        private DecoderOutputBuffer.Owner<CeaOutputBuffer> owner;

        public CeaOutputBuffer(DecoderOutputBuffer.Owner<CeaOutputBuffer> r1) {
            this.owner = r1;
        }

        @Override // androidx.media3.decoder.DecoderOutputBuffer
        public final void release() {
            this.owner.releaseOutputBuffer(this);
        }
    }

    public CeaDecoder() {
        this.availableInputBuffers = new ArrayDeque();
        int r0 = 0;
    L4:
        if (r0 >= 10) goto L6;
        this.availableInputBuffers.add(new CeaInputBuffer(null));
        r0 = r0 + 1;
        goto L4
    L6:
        this.availableOutputBuffers = new ArrayDeque();
        int r1 = 0;
    L8:
        if (r1 >= 2) goto L10;
        this.availableOutputBuffers.add(new CeaOutputBuffer(new CeaDecoder$$ExternalSyntheticLambda0(this)));
        r1 = r1 + 1;
        goto L8
    L10:
        this.queuedInputBuffers = new ArrayDeque();
        this.outputStartTimeUs = C0180C.TIME_UNSET;
    }

    private void releaseInputBuffer(CeaInputBuffer r2) {
        r2.clear();
        this.availableInputBuffers.add(r2);
    }

    protected abstract Subtitle createSubtitle();

    protected abstract void decode(SubtitleInputBuffer r1);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.decoder.Decoder
    public SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        if (this.dequeuedInputBuffer != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        if (this.availableInputBuffers.isEmpty() == false) goto L10;
        return null;
    L10:
        this.dequeuedInputBuffer = this.availableInputBuffers.pollFirst();
        return this.dequeuedInputBuffer;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() throws DecoderException {
        return dequeueInputBuffer();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.decoder.Decoder
    public SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        if (this.availableOutputBuffers.isEmpty() == false) goto L6;
        return null;
    L6:
        if (this.queuedInputBuffers.isEmpty() == true) goto L18;
        if (((CeaInputBuffer) Util.castNonNull(this.queuedInputBuffers.peek())).timeUs > this.playbackPositionUs) goto L18;
        CeaInputBuffer r0 = (CeaInputBuffer) Util.castNonNull(this.queuedInputBuffers.poll());
        if (r0.isEndOfStream() == true) goto L11;
        decode(r0);
        if (isNewSubtitleDataAvailable() == true) goto L15;
        releaseInputBuffer(r0);
        goto L6
    L15:
        Subtitle r6 = createSubtitle();
        SubtitleOutputBuffer r3 = (SubtitleOutputBuffer) Util.castNonNull(this.availableOutputBuffers.pollFirst());
        r3.setContent(r0.timeUs, r6, Long.MAX_VALUE);
        releaseInputBuffer(r0);
        return r3;
    L11:
        SubtitleOutputBuffer r1 = (SubtitleOutputBuffer) Util.castNonNull(this.availableOutputBuffers.pollFirst());
        r1.addFlag(4);
        releaseInputBuffer(r0);
        return r1;
    L18:
        return null;
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() throws DecoderException {
        return dequeueOutputBuffer();
    }

    @Override // androidx.media3.decoder.Decoder
    public void flush() {
        this.queuedInputBufferCount = 0;
        this.playbackPositionUs = 0;
    L4:
        if (this.queuedInputBuffers.isEmpty() == true) goto L7;
        releaseInputBuffer((CeaInputBuffer) Util.castNonNull(this.queuedInputBuffers.poll()));
        goto L4
    L7:
        if (this.dequeuedInputBuffer == null) goto L11;
        releaseInputBuffer(this.dequeuedInputBuffer);
        this.dequeuedInputBuffer = null;
        return;
    }

    protected final SubtitleOutputBuffer getAvailableOutputBuffer() {
        return this.availableOutputBuffers.pollFirst();
    }

    @Override // androidx.media3.decoder.Decoder
    public abstract String getName();

    protected final long getPositionUs() {
        return this.playbackPositionUs;
    }

    protected abstract boolean isNewSubtitleDataAvailable();

    /* JADX WARN: Can't rename method to resolve collision */
    public void queueInputBuffer(SubtitleInputBuffer r6) throws SubtitleDecoderException {
        if (r6 != this.dequeuedInputBuffer) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        CeaInputBuffer r1 = (CeaInputBuffer) r6;
        if (r1.timeUs != Long.MIN_VALUE) goto L9;
    L13:
        long r2 = this.queuedInputBufferCount;
        this.queuedInputBufferCount = 1 + r2;
        CeaInputBuffer.access$102(r1, r2);
        this.queuedInputBuffers.add(r1);
    L14:
        this.dequeuedInputBuffer = null;
        return;
    L9:
        if (this.outputStartTimeUs == C0180C.TIME_UNSET) goto L13;
        if (r1.timeUs >= this.outputStartTimeUs) goto L13;
        releaseInputBuffer(r1);
        goto L14
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ void queueInputBuffer(SubtitleInputBuffer r1) throws DecoderException {
        queueInputBuffer(r1);
    }

    @Override // androidx.media3.decoder.Decoder
    public void release() {
    }

    protected void releaseOutputBuffer(SubtitleOutputBuffer r2) {
        r2.clear();
        this.availableOutputBuffers.add(r2);
    }

    @Override // androidx.media3.decoder.Decoder
    public final void setOutputStartTimeUs(long r1) {
        this.outputStartTimeUs = r1;
    }

    @Override // androidx.media3.extractor.text.SubtitleDecoder
    public void setPositionUs(long r1) {
        this.playbackPositionUs = r1;
    }
}
