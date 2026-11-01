package androidx.media3.exoplayer.upstream;

/* JADX INFO: loaded from: classes.dex */
public interface Allocator {

    public interface AllocationNode {
        Allocation getAllocation();

        AllocationNode next();
    }

    Allocation allocate();

    int getIndividualAllocationLength();

    int getTotalBytesAllocated();

    void release(Allocation r1);

    void release(AllocationNode r1);

    void trim();
}
