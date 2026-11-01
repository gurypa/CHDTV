package kotlin.ranges;

import com.google.common.primitives.C0871x7f2744bd;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.ULong;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0013\u0010\f\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eR\u0013\u0010\b\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\f\u001a\u00020\u0002X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b!¨\u0006\u000f"}, m421d2 = {"Lkotlin/ranges/ULongProgressionIterator;", "", "Lkotlin/ULong;", "first", "last", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "J", "hasNext", "", "next", "next-s-VKNKU", "()J", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class ULongProgressionIterator implements Iterator<ULong>, KMappedMarker {
    private final long finalElement;
    private boolean hasNext;
    private long next;
    private final long step;

    private ULongProgressionIterator(long r4, long r6, long r8) {
        this.finalElement = r6;
        boolean r1 = true;
        if (r8 <= 0) goto L8;
        if (C0871x7f2744bd.m371m(r4, r6) <= 0) goto L11;
    L10:
        r1 = false;
    L11:
        this.hasNext = r1;
        this.step = ULong.m993constructorimpl(r8);
        if (this.hasNext == false) goto L14;
        long r0 = r4;
    L15:
        this.next = r0;
        return;
    L14:
        r0 = this.finalElement;
        goto L15
    L8:
        if (C0871x7f2744bd.m371m(r4, r6) < 0) goto L10;
        goto L10
    }

    public /* synthetic */ ULongProgressionIterator(long r1, long r3, long r5, DefaultConstructorMarker r7) {
        this(r1, r3, r5);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ ULong next() {
        return ULong.m987boximpl(m2054nextsVKNKU());
    }

    /* JADX INFO: renamed from: next-s-VKNKU, reason: not valid java name */
    public long m2054nextsVKNKU() {
        long r0 = this.next;
        if (r0 == this.finalElement) goto L5;
        this.next = ULong.m993constructorimpl(this.next + this.step);
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
