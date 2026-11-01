package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class TopKSelector<T> {
    private final T[] buffer;
    private int bufferSize;
    private final Comparator<? super T> comparator;

    /* JADX INFO: renamed from: k */
    private final int f120k;

    @CheckForNull
    private T threshold;

    private TopKSelector(Comparator<? super T> r5, int r6) {
        this.comparator = (Comparator) Preconditions.checkNotNull(r5, "comparator");
        this.f120k = r6;
        boolean r0 = true;
        if (r6 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "k (%s) must be >= 0", r6);
        if (r6 <= 1073741823) goto L10;
        r0 = false;
    L10:
        Preconditions.checkArgument(r0, "k (%s) must be <= Integer.MAX_VALUE / 2", r6);
        this.buffer = (T[]) new Object[IntMath.checkedMultiply(r6, 2)];
        this.bufferSize = 0;
        this.threshold = null;
        return;
    L5:
        r2 = false;
        goto L6
    }

    public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int r1) {
        return greatest(r1, Ordering.natural());
    }

    public static <T> TopKSelector<T> greatest(int r2, Comparator<? super T> r3) {
        return new TopKSelector(Ordering.from(r3).reverse(), r2);
    }

    public static <T extends Comparable<? super T>> TopKSelector<T> least(int r1) {
        return least(r1, Ordering.natural());
    }

    public static <T> TopKSelector<T> least(int r1, Comparator<? super T> r2) {
        return new TopKSelector(r2, r1);
    }

    private int partition(int r6, int r7, int r8) {
        Object r0 = NullnessCasts.uncheckedCastNullableTToT(this.buffer[r8]);
        this.buffer[r8] = this.buffer[r7];
        int r1 = r6;
        int r2 = r6;
    L3:
        if (r2 >= r7) goto L8;
        if (this.comparator.compare((Object) NullnessCasts.uncheckedCastNullableTToT(this.buffer[r2]), r0) >= 0) goto L7;
        swap(r1, r2);
        r1 = r1 + 1;
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        this.buffer[r7] = this.buffer[r1];
        ((T[]) this.buffer)[r1] = r0;
        return r1;
    }

    private void swap(int r4, int r5) {
        T r0 = this.buffer[r4];
        this.buffer[r4] = this.buffer[r5];
        this.buffer[r5] = r0;
    }

    private void trim() {
        int r0 = 0;
        int r1 = (this.f120k * 2) - 1;
        int r2 = 0;
        int r3 = 0;
        int r4 = IntMath.log2(r1 - 0, RoundingMode.CEILING) * 3;
    L3:
        if (r0 >= r1) goto L14;
        int r6 = partition(r0, r1, ((r0 + r1) + 1) >>> 1);
        if (r6 <= this.f120k) goto L8;
        r1 = r6 - 1;
    L10:
        r3 = r3 + 1;
        if (r3 < r4) goto L3;
        Arrays.sort(this.buffer, r0, r1 + 1, this.comparator);
        goto L14
    L8:
        if (r6 >= this.f120k) goto L14;
        r0 = Math.max(r6, r0 + 1);
        r2 = r6;
    L14:
        this.bufferSize = this.f120k;
        this.threshold = (T) NullnessCasts.uncheckedCastNullableTToT(this.buffer[r2]);
        int r5 = r2 + 1;
    L16:
        if (r5 >= this.f120k) goto L21;
        if (this.comparator.compare((Object) NullnessCasts.uncheckedCastNullableTToT(this.buffer[r5]), (Object) NullnessCasts.uncheckedCastNullableTToT(this.threshold)) <= 0) goto L20;
        this.threshold = this.buffer[r5];
    L20:
        r5 = r5 + 1;
        goto L16
    }

    /* JADX WARN: Multi-variable type inference failed */
    TopKSelector<T> combine(TopKSelector<T> r3) {
        int r0 = 0;
    L4:
        if (r0 >= r3.bufferSize) goto L6;
        offer(NullnessCasts.uncheckedCastNullableTToT(r3.buffer[r0]));
        r0 = r0 + 1;
        goto L4
    L6:
        return this;
    }

    public void offer(@ParametricNullness T r4) {
        if (this.f120k != 0) goto L6;
        return;
    L6:
        if (this.bufferSize != 0) goto L9;
        this.buffer[0] = r4;
        this.threshold = r4;
        this.bufferSize = 1;
        return;
    L9:
        if (this.bufferSize >= this.f120k) goto L14;
        T[] r0 = this.buffer;
        int r1 = this.bufferSize;
        this.bufferSize = r1 + 1;
        r0[r1] = r4;
        if (this.comparator.compare(r4, (Object) NullnessCasts.uncheckedCastNullableTToT(this.threshold)) <= 0) goto L20;
        this.threshold = r4;
        return;
    L20:
        return;
    L14:
        if (this.comparator.compare(r4, (Object) NullnessCasts.uncheckedCastNullableTToT(this.threshold)) >= 0) goto L22;
        T[] r2 = this.buffer;
        int r3 = this.bufferSize;
        this.bufferSize = r3 + 1;
        r2[r3] = r4;
        if (this.bufferSize != (this.f120k * 2)) goto L23;
        trim();
        return;
    L23:
        return;
    }

    public void offerAll(Iterable<? extends T> r2) {
        offerAll(r2.iterator());
    }

    public void offerAll(Iterator<? extends T> r2) {
    L3:
        if (r2.hasNext() == false) goto L5;
        offer(r2.next());
        goto L3
    }

    public List<T> topK() {
        T[] r0 = this.buffer;
        Arrays.sort(r0, 0, this.bufferSize, this.comparator);
        if (this.bufferSize <= this.f120k) goto L6;
        Arrays.fill(this.buffer, this.f120k, this.buffer.length, null);
        this.bufferSize = this.f120k;
        this.threshold = this.buffer[this.f120k - 1];
    L6:
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(r0, this.bufferSize)));
    }
}
