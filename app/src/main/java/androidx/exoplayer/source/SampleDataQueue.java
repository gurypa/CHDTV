package androidx.media3.exoplayer.source;

import androidx.media3.common.DataReader;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoInfo;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.upstream.Allocation;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.extractor.TrackOutput;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
class SampleDataQueue {
    private static final int INITIAL_SCRATCH_SIZE = 32;
    private final int allocationLength;
    private final Allocator allocator;
    private AllocationNode firstAllocationNode;
    private AllocationNode readAllocationNode;
    private final ParsableByteArray scratch;
    private long totalBytesWritten;
    private AllocationNode writeAllocationNode;

    private static final class AllocationNode implements Allocator.AllocationNode {
        public Allocation allocation;
        public long endPosition;
        public AllocationNode next;
        public long startPosition;

        public AllocationNode(long r1, int r3) {
            reset(r1, r3);
        }

        public AllocationNode clear() {
            this.allocation = null;
            AllocationNode r1 = this.next;
            this.next = null;
            return r1;
        }

        @Override // androidx.media3.exoplayer.upstream.Allocator.AllocationNode
        public Allocation getAllocation() {
            return (Allocation) Assertions.checkNotNull(this.allocation);
        }

        public void initialize(Allocation r1, AllocationNode r2) {
            this.allocation = r1;
            this.next = r2;
        }

        @Override // androidx.media3.exoplayer.upstream.Allocator.AllocationNode
        public Allocator.AllocationNode next() {
            if (this.next != null) goto L5;
            return null;
        L5:
            if (this.next.allocation != null) goto L8;
            return null;
        L8:
            return this.next;
        }

        public void reset(long r3, int r5) {
            if (this.allocation != null) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            this.startPosition = r3;
            this.endPosition = ((long) r5) + r3;
            return;
        L5:
            r0 = false;
            goto L6
        }

        public int translateOffset(long r3) {
            return ((int) (r3 - this.startPosition)) + this.allocation.offset;
        }
    }

    public SampleDataQueue(Allocator r5) {
        this.allocator = r5;
        this.allocationLength = r5.getIndividualAllocationLength();
        this.scratch = new ParsableByteArray(32);
        this.firstAllocationNode = new AllocationNode(0, this.allocationLength);
        this.readAllocationNode = this.firstAllocationNode;
        this.writeAllocationNode = this.firstAllocationNode;
    }

    private void clearAllocationNodes(AllocationNode r2) {
        if (r2.allocation != null) goto L5;
        return;
    L5:
        this.allocator.release(r2);
        r2.clear();
    }

    private static AllocationNode getNodeContainingPosition(AllocationNode r2, long r3) {
    L3:
        if (r3 < r2.endPosition) goto L5;
        r2 = r2.next;
        goto L3
    L5:
        return r2;
    }

    private void postAppend(int r5) {
        this.totalBytesWritten += (long) r5;
        if (this.totalBytesWritten != this.writeAllocationNode.endPosition) goto L6;
        this.writeAllocationNode = this.writeAllocationNode.next;
        return;
    }

    private int preAppend(int r7) {
        if (this.writeAllocationNode.allocation != null) goto L6;
        this.writeAllocationNode.initialize(this.allocator.allocate(), new AllocationNode(this.writeAllocationNode.endPosition, this.allocationLength));
    L6:
        return Math.min(r7, (int) (this.writeAllocationNode.endPosition - this.totalBytesWritten));
    }

    private static AllocationNode readData(AllocationNode r5, long r6, ByteBuffer r8, int r9) {
        AllocationNode r7 = getNodeContainingPosition(r5, r6);
        int r0 = r9;
    L3:
        if (r0 <= 0) goto L8;
        int r1 = Math.min(r0, (int) (r7.endPosition - r6));
        r8.put(r7.allocation.data, r7.translateOffset(r6), r1);
        r0 = r0 - r1;
        r6 = r6 + ((long) r1);
        if (r6 != r7.endPosition) goto L3;
        r7 = r7.next;
        goto L3
    L8:
        return r7;
    }

    private static AllocationNode readData(AllocationNode r6, long r7, byte[] r9, int r10) {
        AllocationNode r8 = getNodeContainingPosition(r6, r7);
        int r0 = r10;
    L3:
        if (r0 <= 0) goto L8;
        int r1 = Math.min(r0, (int) (r8.endPosition - r7));
        System.arraycopy(r8.allocation.data, r8.translateOffset(r7), r9, r10 - r0, r1);
        r0 = r0 - r1;
        r7 = r7 + ((long) r1);
        if (r7 != r8.endPosition) goto L3;
        r8 = r8.next;
        goto L3
    L8:
        return r8;
    }

    private static AllocationNode readEncryptionData(AllocationNode r20, DecoderInputBuffer r21, SampleQueue.SampleExtrasHolder r22, ParsableByteArray r23) {
        long r2 = r22.offset;
        boolean r4 = true;
        r23.reset(1);
        AllocationNode r5 = readData(r20, r2, r23.getData(), 1);
        long r3 = r2 + 1;
        byte r6 = r23.getData()[0];
        if ((r6 & 128) != 0) goto L6;
        r4 = false;
    L6:
        int r8 = r6 & 127;
        CryptoInfo r10 = r21.cryptoInfo;
        if (r10.f34iv != null) goto L9;
        r10.f34iv = new byte[16];
    L10:
        AllocationNode r7 = readData(r5, r3, r10.f34iv, r8);
        long r9 = r3 + ((long) r8);
        if (r4 == false) goto L13;
        r23.reset(2);
        r7 = readData(r7, r9, r23.getData(), 2);
        r9 = r9 + 2;
        int r11 = r23.readUnsignedShort();
    L14:
        int[] r12 = r10.numBytesOfClearData;
        if (r12 != null) goto L17;
    L18:
        r12 = new int[r11];
    L19:
        int[] r13 = r10.numBytesOfEncryptedData;
        if (r13 != null) goto L22;
    L23:
        r13 = new int[r11];
    L24:
        if (r4 == false) goto L29;
        int r14 = r11 * 6;
        r23.reset(r14);
        r7 = readData(r7, r9, r23.getData(), r14);
        r9 = r9 + ((long) r14);
        r23.setPosition(0);
        int r15 = 0;
    L26:
        if (r15 >= r11) goto L30;
        r12[r15] = r23.readUnsignedShort();
        r13[r15] = r23.readUnsignedIntToInt();
        r15 = r15 + 1;
    L30:
        TrackOutput.CryptoData r16 = (TrackOutput.CryptoData) Util.castNonNull(r22.cryptoData);
        r10.set(r11, r12, r13, r16.encryptionKey, r10.f34iv, r16.cryptoMode, r16.encryptedBlocks, r16.clearBlocks);
        int r1 = (int) (r9 - r22.offset);
        r22.offset += (long) r1;
        r22.size -= r1;
        return r7;
    L29:
        r12[0] = 0;
        r13[0] = r22.size - ((int) (r9 - r22.offset));
        goto L30
    L22:
        if (r13.length >= r11) goto L24;
    L17:
        if (r12.length >= r11) goto L19;
    L13:
        r11 = 1;
        goto L14
    L9:
        Arrays.fill(r10.f34iv, (byte) 0);
        goto L10
    }

    private static AllocationNode readSampleData(AllocationNode r6, DecoderInputBuffer r7, SampleQueue.SampleExtrasHolder r8, ParsableByteArray r9) {
        if (r7.isEncrypted() == false) goto L6;
        r6 = readEncryptionData(r6, r7, r8, r9);
    L6:
        if (r7.hasSupplementalData() == false) goto L8;
        r9.reset(4);
        AllocationNode r10 = readData(r6, r8.offset, r9.getData(), 4);
        int r1 = r9.readUnsignedIntToInt();
        r8.offset += 4;
        r8.size -= 4;
        r7.ensureSpaceForWrite(r1);
        AllocationNode r11 = readData(r10, r8.offset, r7.data, r1);
        r8.offset += (long) r1;
        r8.size -= r1;
        r7.resetSupplementalData(r8.size);
        return readData(r11, r8.offset, r7.supplementalData, r8.size);
    L8:
        r7.ensureSpaceForWrite(r8.size);
        return readData(r6, r8.offset, r7.data, r8.size);
    }

    public void discardDownstreamTo(long r5) {
        if (r5 != (-1)) goto L6;
        return;
    L6:
        if (r5 < this.firstAllocationNode.endPosition) goto L9;
        this.allocator.release(this.firstAllocationNode.allocation);
        this.firstAllocationNode = this.firstAllocationNode.clear();
        goto L6
    L9:
        if (this.readAllocationNode.startPosition >= this.firstAllocationNode.startPosition) goto L13;
        this.readAllocationNode = this.firstAllocationNode;
        return;
    }

    public void discardUpstreamSampleBytes(long r7) {
        if (r7 > this.totalBytesWritten) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.totalBytesWritten = r7;
        if (this.totalBytesWritten != 0) goto L9;
    L22:
        clearAllocationNodes(this.firstAllocationNode);
        this.firstAllocationNode = new AllocationNode(this.totalBytesWritten, this.allocationLength);
        this.readAllocationNode = this.firstAllocationNode;
        this.writeAllocationNode = this.firstAllocationNode;
        return;
    L9:
        if (this.totalBytesWritten == this.firstAllocationNode.startPosition) goto L22;
        AllocationNode r1 = this.firstAllocationNode;
    L13:
        if (this.totalBytesWritten <= r1.endPosition) goto L15;
        r1 = r1.next;
        goto L13
    L15:
        AllocationNode r2 = (AllocationNode) Assertions.checkNotNull(r1.next);
        clearAllocationNodes(r2);
        r1.next = new AllocationNode(r1.endPosition, this.allocationLength);
        if (this.totalBytesWritten != r1.endPosition) goto L18;
        AllocationNode r3 = r1.next;
    L19:
        this.writeAllocationNode = r3;
        if (this.readAllocationNode != r2) goto L25;
        this.readAllocationNode = r1.next;
        return;
    L25:
        return;
    L18:
        r3 = r1;
        goto L19
    L5:
        r0 = false;
        goto L6
    }

    public long getTotalBytesWritten() {
        return this.totalBytesWritten;
    }

    public void peekToBuffer(DecoderInputBuffer r3, SampleQueue.SampleExtrasHolder r4) {
        readSampleData(this.readAllocationNode, r3, r4, this.scratch);
    }

    public void readToBuffer(DecoderInputBuffer r3, SampleQueue.SampleExtrasHolder r4) {
        this.readAllocationNode = readSampleData(this.readAllocationNode, r3, r4, this.scratch);
    }

    public void reset() {
        clearAllocationNodes(this.firstAllocationNode);
        this.firstAllocationNode.reset(0, this.allocationLength);
        this.readAllocationNode = this.firstAllocationNode;
        this.writeAllocationNode = this.firstAllocationNode;
        this.totalBytesWritten = 0;
        this.allocator.trim();
    }

    public void rewind() {
        this.readAllocationNode = this.firstAllocationNode;
    }

    public int sampleData(DataReader r5, int r6, boolean r7) throws IOException {
        int r8 = preAppend(r6);
        int r0 = r5.read(this.writeAllocationNode.allocation.data, this.writeAllocationNode.translateOffset(this.totalBytesWritten), r8);
        if (r0 != (-1)) goto L8;
        if (r7 == false) goto L7;
        return -1;
    L7:
        throw new EOFException();
    L8:
        postAppend(r0);
        return r0;
    }

    public void sampleData(ParsableByteArray r6, int r7) {
    L2:
        if (r7 <= 0) goto L4;
        int r0 = preAppend(r7);
        r6.readBytes(this.writeAllocationNode.allocation.data, this.writeAllocationNode.translateOffset(this.totalBytesWritten), r0);
        r7 = r7 - r0;
        postAppend(r0);
        goto L2
    }
}
