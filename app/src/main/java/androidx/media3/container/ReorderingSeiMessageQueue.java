package androidx.media3.container;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/* JADX INFO: loaded from: classes.dex */
public final class ReorderingSeiMessageQueue {
    private SampleSeiMessages lastQueuedMessage;
    private final PriorityQueue<SampleSeiMessages> pendingSeiMessages;
    private int reorderingQueueSize;
    private final SeiConsumer seiConsumer;
    private final ArrayDeque<ParsableByteArray> unusedParsableByteArrays;
    private final ArrayDeque<SampleSeiMessages> unusedSampleSeiMessages;

    private static final class SampleSeiMessages implements Comparable<SampleSeiMessages> {
        public final List<ParsableByteArray> nalBuffers;
        public long presentationTimeUs;

        public SampleSeiMessages() {
            this.presentationTimeUs = C0180C.TIME_UNSET;
            this.nalBuffers = new ArrayList();
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(SampleSeiMessages r5) {
            return Long.compare(this.presentationTimeUs, r5.presentationTimeUs);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(SampleSeiMessages r1) {
            return compareTo2(r1);
        }

        public void init(long r3, ParsableByteArray r5) {
            if (r3 == C0180C.TIME_UNSET) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            Assertions.checkState(this.nalBuffers.isEmpty());
            this.presentationTimeUs = r3;
            this.nalBuffers.add(r5);
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    public interface SeiConsumer {
        void consume(long r1, ParsableByteArray r3);
    }

    public ReorderingSeiMessageQueue(SeiConsumer r2) {
        this.seiConsumer = r2;
        this.unusedParsableByteArrays = new ArrayDeque();
        this.unusedSampleSeiMessages = new ArrayDeque();
        this.pendingSeiMessages = new PriorityQueue();
        this.reorderingQueueSize = -1;
    }

    private ParsableByteArray copy(ParsableByteArray r7) {
        if (this.unusedParsableByteArrays.isEmpty() == false) goto L5;
        ParsableByteArray r0 = new ParsableByteArray();
    L6:
        r0.reset(r7.bytesLeft());
        System.arraycopy(r7.getData(), r7.getPosition(), r0.getData(), 0, r0.bytesLeft());
        return r0;
    L5:
        r0 = this.unusedParsableByteArrays.pop();
        goto L6
    }

    private void flushQueueDownToSize(int r7) {
    L3:
        if (this.pendingSeiMessages.size() <= r7) goto L14;
        SampleSeiMessages r0 = (SampleSeiMessages) Util.castNonNull(this.pendingSeiMessages.poll());
        int r1 = 0;
    L6:
        if (r1 >= r0.nalBuffers.size()) goto L8;
        this.seiConsumer.consume(r0.presentationTimeUs, r0.nalBuffers.get(r1));
        this.unusedParsableByteArrays.push(r0.nalBuffers.get(r1));
        r1 = r1 + 1;
        goto L6
    L8:
        r0.nalBuffers.clear();
        if (this.lastQueuedMessage == null) goto L13;
        if (this.lastQueuedMessage.presentationTimeUs != r0.presentationTimeUs) goto L13;
        this.lastQueuedMessage = null;
    L13:
        this.unusedSampleSeiMessages.push(r0);
        goto L3
    }

    public void add(long r5, ParsableByteArray r7) {
        if (this.reorderingQueueSize != 0) goto L5;
    L25:
        this.seiConsumer.consume(r5, r7);
        return;
    L5:
        if (this.reorderingQueueSize != (-1)) goto L7;
    L11:
        ParsableByteArray r0 = copy(r7);
        if (this.lastQueuedMessage == null) goto L18;
        if (r5 != this.lastQueuedMessage.presentationTimeUs) goto L18;
        this.lastQueuedMessage.nalBuffers.add(r0);
        return;
    L18:
        if (this.unusedSampleSeiMessages.isEmpty() == false) goto L20;
        SampleSeiMessages r2 = new SampleSeiMessages();
    L21:
        r2.init(r5, r0);
        this.pendingSeiMessages.add(r2);
        this.lastQueuedMessage = r2;
        if (this.reorderingQueueSize == (-1)) goto L27;
        flushQueueDownToSize(this.reorderingQueueSize);
        return;
    L27:
        return;
    L20:
        r2 = this.unusedSampleSeiMessages.pop();
        goto L21
    L7:
        if (this.pendingSeiMessages.size() < this.reorderingQueueSize) goto L11;
        if (r5 >= ((SampleSeiMessages) Util.castNonNull(this.pendingSeiMessages.peek())).presentationTimeUs) goto L11;
        goto L11
    }

    public void flush() {
        flushQueueDownToSize(0);
    }

    public int getMaxSize() {
        return this.reorderingQueueSize;
    }

    public void setMaxSize(int r2) {
        if (r2 < 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkState(r0);
        this.reorderingQueueSize = r2;
        flushQueueDownToSize(r2);
        return;
    L4:
        r0 = false;
        goto L5
    }
}
