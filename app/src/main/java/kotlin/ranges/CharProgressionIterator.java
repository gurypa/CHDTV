package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CharIterator;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ProgressionIterators.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\t\u001a\u00020\nH\u0096\u0002J\b\u0010\u000e\u001a\u00020\u0003H\u0016R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000f"}, m421d2 = {"Lkotlin/ranges/CharProgressionIterator;", "Lkotlin/collections/CharIterator;", "first", "", "last", "step", "", "(CCI)V", "finalElement", "hasNext", "", "next", "getStep", "()I", "nextChar", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class CharProgressionIterator extends CharIterator {
    private final int finalElement;
    private boolean hasNext;
    private int next;
    private final int step;

    public CharProgressionIterator(char r4, char r5, int r6) {
        this.step = r6;
        this.finalElement = r5;
        boolean r1 = true;
        if (this.step <= 0) goto L8;
        if (Intrinsics.compare(r4, r5) <= 0) goto L11;
    L10:
        r1 = false;
    L11:
        this.hasNext = r1;
        if (this.hasNext == false) goto L14;
        int r0 = r4;
    L15:
        this.next = r0;
        return;
    L14:
        r0 = this.finalElement;
        goto L15
    L8:
        if (Intrinsics.compare(r4, r5) < 0) goto L10;
        goto L10
    }

    public final int getStep() {
        return this.step;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override // kotlin.collections.CharIterator
    public char nextChar() {
        int r0 = this.next;
        if (r0 == this.finalElement) goto L5;
        this.next += this.step;
    L11:
        return (char) r0;
    L5:
        if (this.hasNext == false) goto L8;
        this.hasNext = false;
        goto L11
    L8:
        throw new NoSuchElementException();
    }
}
