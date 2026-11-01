package androidx.media3.exoplayer.upstream;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultAllocator implements Allocator {
    private static final int AVAILABLE_EXTRA_CAPACITY = 100;
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;
    private int targetBufferSize;
    private final boolean trimOnReset;

    public DefaultAllocator(boolean r2, int r3) {
        this(r2, r3, 0);
    }

    public DefaultAllocator(boolean r6, int r7, int r8) {
        boolean r0 = false;
        if (r7 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (r8 < 0) goto L9;
        r0 = true;
    L9:
        Assertions.checkArgument(r0);
        this.trimOnReset = r6;
        this.individualAllocationSize = r7;
        this.availableCount = r8;
        this.availableAllocations = new Allocation[r8 + 100];
        if (r8 <= 0) goto L15;
        this.initialAllocationBlock = new byte[r8 * r7];
        int r1 = 0;
    L12:
        if (r1 >= r8) goto L18;
        this.availableAllocations[r1] = new Allocation(this.initialAllocationBlock, r1 * r7);
        r1 = r1 + 1;
        goto L12
    L18:
        return;
    L15:
        this.initialAllocationBlock = null;
        return;
    L5:
        r2 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.upstream.Allocator
    public synchronized Allocation allocate() {
        monitor-enter(this);
        this.allocatedCount++;     // Catch: Throwable -> L11
        if (this.availableCount <= 0) goto L6;
        Allocation[] r0 = this.availableAllocations;     // Catch: Throwable -> L11
        int r1 = this.availableCount - 1;     // Catch: Throwable -> L11
        this.availableCount = r1;     // Catch: Throwable -> L11
        Allocation r2 = (Allocation) Assertions.checkNotNull(r0[r1]);     // Catch: Throwable -> L11
        this.availableAllocations[this.availableCount] = null;     // Catch: Throwable -> L11
    L9:
        monitor-exit(this);
        return r2;
    L6:
        r2 = new Allocation(new byte[this.individualAllocationSize], 0);     // Catch: Throwable -> L11
        if (this.allocatedCount <= this.availableAllocations.length) goto L9;
        this.availableAllocations = (Allocation[]) Arrays.copyOf(this.availableAllocations, this.availableAllocations.length * 2);     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.upstream.Allocator
    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }

    @Override // androidx.media3.exoplayer.upstream.Allocator
    public synchronized int getTotalBytesAllocated() {
        monitor-enter(this);
        int r0 = this.allocatedCount * this.individualAllocationSize;
        monitor-exit(this);
        return r0;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.upstream.Allocator
    public synchronized void release(Allocation r4) {
        monitor-enter(this);
        Allocation[] r0 = this.availableAllocations;     // Catch: Throwable -> L6
        int r1 = this.availableCount;     // Catch: Throwable -> L6
        this.availableCount = r1 + 1;     // Catch: Throwable -> L6
        r0[r1] = r4;     // Catch: Throwable -> L6
        this.allocatedCount--;     // Catch: Throwable -> L6
        notifyAll();     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.upstream.Allocator
    public synchronized void release(Allocator.AllocationNode r4) {
        monitor-enter(this);
    L3:
        if (r4 == null) goto L5;
        Allocation[] r0 = this.availableAllocations;     // Catch: Throwable -> L8
        int r1 = this.availableCount;     // Catch: Throwable -> L8
        this.availableCount = r1 + 1;     // Catch: Throwable -> L8
        r0[r1] = r4.getAllocation();     // Catch: Throwable -> L8
        this.allocatedCount--;     // Catch: Throwable -> L8
        r4 = r4.next();     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        throw th;
    L5:
        notifyAll();     // Catch: Throwable -> L8
        monitor-exit(this);
    }

    public synchronized void reset() {
        monitor-enter(this);
    L8:
        th = move-exception;
        throw th;
    L4:
        if (this.trimOnReset == false) goto L6;
        setTargetBufferSize(0);     // Catch: Throwable -> L8
    L6:
        monitor-exit(this);
    }

    public synchronized void setTargetBufferSize(int r2) {
        monitor-enter(this);
    L12:
        th = move-exception;
        throw th;
    L4:
        if (r2 >= this.targetBufferSize) goto L6;
        boolean r0 = true;
    L7:
        this.targetBufferSize = r2;     // Catch: Throwable -> L12
        if (r0 == false) goto L10;
        trim();     // Catch: Throwable -> L12
    L10:
        monitor-exit(this);
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.exoplayer.upstream.Allocator
    public synchronized void trim() {
        monitor-enter(this);
        int r1 = Math.max(0, Util.ceilDivide(this.targetBufferSize, this.individualAllocationSize) - this.allocatedCount);     // Catch: Throwable -> L26
        if (r1 < this.availableCount) goto L8;
        monitor-exit(this);
        return;
    L8:
        if (this.initialAllocationBlock == null) goto L23;
        int r2 = 0;
        int r3 = this.availableCount - 1;     // Catch: Throwable -> L26
    L10:
        if (r2 > r3) goto L19;
        Allocation r4 = (Allocation) Assertions.checkNotNull(this.availableAllocations[r2]);     // Catch: Throwable -> L26
        if (r4.data != this.initialAllocationBlock) goto L14;
        r2 = r2 + 1;     // Catch: Throwable -> L26
        goto L10
    L14:
        Allocation r5 = (Allocation) Assertions.checkNotNull(this.availableAllocations[r3]);     // Catch: Throwable -> L26
        if (r5.data == this.initialAllocationBlock) goto L17;
        r3 = r3 - 1;     // Catch: Throwable -> L26
        goto L10
    L17:
        this.availableAllocations[r2] = r5;     // Catch: Throwable -> L26
        this.availableAllocations[r3] = r4;     // Catch: Throwable -> L26
        r3 = r3 - 1;     // Catch: Throwable -> L26
        r2 = r2 + 1;     // Catch: Throwable -> L26
        goto L10
    L19:
        r1 = Math.max(r1, r2);     // Catch: Throwable -> L26
        if (r1 < this.availableCount) goto L23;
        monitor-exit(this);
        return;
    L23:
        Arrays.fill(this.availableAllocations, r1, this.availableCount, null);     // Catch: Throwable -> L26
        this.availableCount = r1;     // Catch: Throwable -> L26
        monitor-exit(this);
        return;
    L26:
        th = move-exception;
        throw th;
    }
}
