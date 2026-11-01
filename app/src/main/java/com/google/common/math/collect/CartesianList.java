package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess {
    private final transient ImmutableList<List<E>> axes;
    private final transient int[] axesSizeProduct;

    CartesianList(ImmutableList<List<E>> r5) {
        this.axes = r5;
        int[] r0 = new int[r5.size() + 1];
        r0[r5.size()] = 1;
        int r2 = r5.size() - 1;     // Catch: ArithmeticException -> L9
    L4:
        if (r2 < 0) goto L7;
        r0[r2] = IntMath.checkedMultiply(r0[r2 + 1], r5.get(r2).size());     // Catch: ArithmeticException -> L9
        r2 = r2 - 1;
        goto L4
    L7:
        this.axesSizeProduct = r0;
        return;
    L11:
        throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
    }

    static /* synthetic */ ImmutableList access$000(CartesianList r1) {
        return r1.axes;
    }

    static /* synthetic */ int access$100(CartesianList r1, int r2, int r3) {
        return r1.getAxisIndexForProductIndex(r2, r3);
    }

    static <E> List<List<E>> create(List<? extends List<? extends E>> r5) {
        ImmutableList.Builder r0 = new ImmutableList.Builder(r5.size());
        Iterator<? extends List<? extends E>> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L11;
        ImmutableList r3 = ImmutableList.copyOf(r1.next());
        if (r3.isEmpty() == true) goto L8;
        r0.add(r3);
        goto L4
    L8:
        return ImmutableList.m212of();
    L11:
        return new CartesianList(r0.build());
    }

    private int getAxisIndexForProductIndex(int r3, int r4) {
        return (r3 / this.axesSizeProduct[r4 + 1]) % this.axes.get(r4).size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(@CheckForNull Object r7) {
        if ((r7 instanceof List) == true) goto L5;
        return false;
    L5:
        List r0 = (List) r7;
        if (r0.size() == this.axes.size()) goto L8;
        return false;
    L8:
        int r2 = 0;
        Iterator<E> r3 = r0.iterator();
    L10:
        if (r3.hasNext() == false) goto L15;
        E r4 = r3.next();
        if (this.axes.get(r2).contains(r4) == false) goto L13;
        r2 = r2 + 1;
        goto L10
    L13:
        return false;
    L15:
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public ImmutableList<E> get(final int r2) {
        Preconditions.checkElementIndex(r2, size());
        return new C06031(this, r2);
    }

    @Override // java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ Object get(int r1) {
        return get(r1);
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(@CheckForNull Object r9) {
        if ((r9 instanceof List) == true) goto L5;
        return -1;
    L5:
        List r0 = (List) r9;
        if (r0.size() == this.axes.size()) goto L8;
        return -1;
    L8:
        ListIterator<E> r2 = r0.listIterator();
        int r3 = 0;
    L10:
        if (r2.hasNext() == false) goto L15;
        int r4 = r2.nextIndex();
        int r5 = this.axes.get(r4).indexOf(r2.next());
        if (r5 == (-1)) goto L13;
        r3 = r3 + (this.axesSizeProduct[r4 + 1] * r5);
        goto L10
    L13:
        return -1;
    L15:
        return r3;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(@CheckForNull Object r9) {
        if ((r9 instanceof List) == true) goto L5;
        return -1;
    L5:
        List r0 = (List) r9;
        if (r0.size() == this.axes.size()) goto L8;
        return -1;
    L8:
        ListIterator<E> r2 = r0.listIterator();
        int r3 = 0;
    L10:
        if (r2.hasNext() == false) goto L15;
        int r4 = r2.nextIndex();
        int r5 = this.axes.get(r4).lastIndexOf(r2.next());
        if (r5 == (-1)) goto L13;
        r3 = r3 + (this.axesSizeProduct[r4 + 1] * r5);
        goto L10
    L13:
        return -1;
    L15:
        return r3;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.axesSizeProduct[0];
    }
}
