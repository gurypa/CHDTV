package kotlin.ranges;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UInt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: UIntRange.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0013\u0010\f\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eR\u0013\u0010\b\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\f\u001a\u00020\u0002X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b!¨\u0006\u000f"}, m421d2 = {"Lkotlin/ranges/UIntProgressionIterator;", "", "Lkotlin/UInt;", "first", "last", "step", "", "(IIILkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "I", "hasNext", "", "next", "next-pVg5ArA", "()I", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class UIntProgressionIterator implements Iterator<UInt>, KMappedMarker {
    private final int finalElement;
    private boolean hasNext;
    private int next;
    private final int step;

    private UIntProgressionIterator(int r4, int r5, int r6) {
        this.finalElement = r5;
        boolean r0 = true;
        if (r6 <= 0) goto L8;
        if (UByte$$ExternalSyntheticBackport4.m433m(r4, r5) <= 0) goto L11;
    L10:
        r0 = false;
    L11:
        this.hasNext = r0;
        this.step = UInt.m914constructorimpl(r6);
        if (this.hasNext == false) goto L14;
        int r1 = r4;
    L15:
        this.next = r1;
        return;
    L14:
        r1 = this.finalElement;
        goto L15
    L8:
        if (UByte$$ExternalSyntheticBackport4.m433m(r4, r5) < 0) goto L10;
        goto L10
    }

    public /* synthetic */ UIntProgressionIterator(int r1, int r2, int r3, DefaultConstructorMarker r4) {
        this(r1, r2, r3);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ UInt next() {
        return UInt.m908boximpl(m2045nextpVg5ArA());
    }

    /* JADX INFO: renamed from: next-pVg5ArA, reason: not valid java name */
    public int m2045nextpVg5ArA() {
        int r0 = this.next;
        if (r0 == this.finalElement) goto L5;
        this.next = UInt.m914constructorimpl(this.next + this.step);
    L10:
        return r0;
    L5:
        if (this.hasNext == false) goto L8;
        this.hasNext = false;
        goto L10
    L8:
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
