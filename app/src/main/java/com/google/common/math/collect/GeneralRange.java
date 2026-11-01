package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class GeneralRange<T> implements Serializable {
    private final Comparator<? super T> comparator;
    private final boolean hasLowerBound;
    private final boolean hasUpperBound;
    private final BoundType lowerBoundType;

    @CheckForNull
    private final T lowerEndpoint;

    @CheckForNull
    @LazyInit
    private transient GeneralRange<T> reverse;
    private final BoundType upperBoundType;

    @CheckForNull
    private final T upperEndpoint;

    private GeneralRange(Comparator<? super T> r6, boolean r7, @CheckForNull T r8, BoundType r9, boolean r10, @CheckForNull T r11, BoundType r12) {
        this.comparator = (Comparator) Preconditions.checkNotNull(r6);
        this.hasLowerBound = r7;
        this.hasUpperBound = r10;
        this.lowerEndpoint = r8;
        this.lowerBoundType = (BoundType) Preconditions.checkNotNull(r9);
        this.upperEndpoint = r11;
        this.upperBoundType = (BoundType) Preconditions.checkNotNull(r12);
        if (r7 == false) goto L5;
        r6.compare((Object) NullnessCasts.uncheckedCastNullableTToT(r8), (Object) NullnessCasts.uncheckedCastNullableTToT(r8));
    L5:
        if (r10 == false) goto L7;
        r6.compare((Object) NullnessCasts.uncheckedCastNullableTToT(r11), (Object) NullnessCasts.uncheckedCastNullableTToT(r11));
    L7:
        if (r7 == false) goto L23;
        if (r10 == false) goto L24;
        int r0 = r6.compare((Object) NullnessCasts.uncheckedCastNullableTToT(r8), (Object) NullnessCasts.uncheckedCastNullableTToT(r11));
        boolean r1 = true;
        if (r0 > 0) goto L12;
        boolean r3 = true;
    L13:
        Preconditions.checkArgument(r3, "lowerEndpoint (%s) > upperEndpoint (%s)", r8, r11);
        if (r0 == 0) goto L16;
        return;
    L16:
        if (r9 == BoundType.OPEN) goto L18;
    L21:
        Preconditions.checkArgument(r1);
        return;
    L18:
        if (r12 != BoundType.OPEN) goto L21;
        r1 = false;
        goto L21
    L12:
        r3 = false;
        goto L13
    L24:
        return;
    }

    static <T> GeneralRange<T> all(Comparator<? super T> r8) {
        return new GeneralRange(r8, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
    }

    static <T> GeneralRange<T> downTo(Comparator<? super T> r8, @ParametricNullness T r9, BoundType r10) {
        return new GeneralRange(r8, true, r9, r10, false, null, BoundType.OPEN);
    }

    static <T extends Comparable> GeneralRange<T> from(Range<T> r10) {
        Comparable r1 = null;
        if (r10.hasLowerBound() == false) goto L5;
        Comparable r0 = r10.lowerEndpoint();
    L6:
        Comparable r5 = r0;
        if (r10.hasLowerBound() == false) goto L9;
        BoundType r2 = r10.lowerBoundType();
    L10:
        BoundType r6 = r2;
        if (r10.hasUpperBound() == false) goto L13;
        r1 = r10.upperEndpoint();
    L13:
        Comparable r8 = r1;
        if (r10.hasUpperBound() == false) goto L16;
        BoundType r3 = r10.upperBoundType();
    L18:
        return new GeneralRange(Ordering.natural(), r10.hasLowerBound(), r5, r6, r10.hasUpperBound(), r8, r3);
    L16:
        r3 = BoundType.OPEN;
        goto L18
    L9:
        r2 = BoundType.OPEN;
        goto L10
    L5:
        r0 = null;
        goto L6
    }

    static <T> GeneralRange<T> range(Comparator<? super T> r8, @ParametricNullness T r9, BoundType r10, @ParametricNullness T r11, BoundType r12) {
        return new GeneralRange(r8, true, r9, r10, true, r11, r12);
    }

    static <T> GeneralRange<T> upTo(Comparator<? super T> r8, @ParametricNullness T r9, BoundType r10) {
        return new GeneralRange(r8, false, null, BoundType.OPEN, true, r9, r10);
    }

    Comparator<? super T> comparator() {
        return this.comparator;
    }

    boolean contains(@ParametricNullness T r2) {
        if (tooLow(r2) == false) goto L5;
    L7:
        return false;
    L5:
        if (tooHigh(r2) == true) goto L7;
        return true;
    }

    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof GeneralRange) == false) goto L21;
        GeneralRange r0 = (GeneralRange) r5;
        if (this.comparator.equals(r0.comparator) == true) goto L7;
        return false;
    L7:
        if (this.hasLowerBound == r0.hasLowerBound) goto L9;
        return false;
    L9:
        if (this.hasUpperBound == r0.hasUpperBound) goto L11;
        return false;
    L11:
        if (getLowerBoundType().equals(r0.getLowerBoundType()) == true) goto L13;
        return false;
    L13:
        if (getUpperBoundType().equals(r0.getUpperBoundType()) == true) goto L15;
        return false;
    L15:
        if (Objects.equal(getLowerEndpoint(), r0.getLowerEndpoint()) == true) goto L17;
        return false;
    L17:
        if (Objects.equal(getUpperEndpoint(), r0.getUpperEndpoint()) == false) goto L28;
        return true;
    L28:
        return false;
    L21:
        return false;
    }

    BoundType getLowerBoundType() {
        return this.lowerBoundType;
    }

    @CheckForNull
    T getLowerEndpoint() {
        return this.lowerEndpoint;
    }

    BoundType getUpperBoundType() {
        return this.upperBoundType;
    }

    @CheckForNull
    T getUpperEndpoint() {
        return this.upperEndpoint;
    }

    boolean hasLowerBound() {
        return this.hasLowerBound;
    }

    boolean hasUpperBound() {
        return this.hasUpperBound;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType()});
    }

    GeneralRange<T> intersect(GeneralRange<T> r13) {
        Preconditions.checkNotNull(r13);
        Preconditions.checkArgument(this.comparator.equals(r13.comparator));
        boolean r0 = this.hasLowerBound;
        T r1 = getLowerEndpoint();
        BoundType r2 = getLowerBoundType();
        if (hasLowerBound() == true) goto L6;
        boolean r3 = r13.hasLowerBound;
        r1 = r13.getLowerEndpoint();
        r2 = r13.getLowerBoundType();
        boolean r6 = r3;
    L14:
        boolean r4 = this.hasUpperBound;
        T r5 = getUpperEndpoint();
        BoundType r7 = getUpperBoundType();
        if (hasUpperBound() == true) goto L18;
        boolean r8 = r13.hasUpperBound;
        T r9 = r13.getUpperEndpoint();
        r7 = r13.getUpperBoundType();
        boolean r10 = r8;
        T r11 = r9;
    L26:
        if (r6 == false) goto L36;
        if (r10 == false) goto L36;
        int r12 = this.comparator.compare(r1, r11);
        if (r12 > 0) goto L35;
        if (r12 != 0) goto L36;
        if (r2 != BoundType.OPEN) goto L36;
        if (r7 != BoundType.OPEN) goto L36;
    L35:
        BoundType r14 = BoundType.OPEN;
        T r15 = r11;
        BoundType r16 = r14;
        BoundType r17 = BoundType.CLOSED;
    L38:
        return new GeneralRange(this.comparator, r6, r15, r16, r10, r11, r17);
    L36:
        r15 = r1;
        r16 = r2;
        r17 = r7;
        goto L38
    L18:
        if (r13.hasUpperBound() == false) goto L25;
        int r18 = this.comparator.compare(getUpperEndpoint(), r13.getUpperEndpoint());
        if (r18 > 0) goto L24;
        if (r18 != 0) goto L25;
        if (r13.getUpperBoundType() != BoundType.OPEN) goto L25;
    L24:
        T r19 = r13.getUpperEndpoint();
        r7 = r13.getUpperBoundType();
        r10 = r4;
        r11 = r19;
    L25:
        r10 = r4;
        r11 = r5;
        goto L26
    L6:
        if (r13.hasLowerBound() == false) goto L13;
        int r20 = this.comparator.compare(getLowerEndpoint(), r13.getLowerEndpoint());
        if (r20 < 0) goto L12;
        if (r20 != 0) goto L13;
        if (r13.getLowerBoundType() != BoundType.OPEN) goto L13;
    L12:
        r1 = r13.getLowerEndpoint();
        r2 = r13.getLowerBoundType();
        r6 = r0;
    L13:
        r6 = r0;
        goto L14
    }

    /* JADX WARN: Multi-variable type inference failed */
    boolean isEmpty() {
        if (hasUpperBound() == false) goto L7;
        if (tooLow(NullnessCasts.uncheckedCastNullableTToT(getUpperEndpoint())) == false) goto L7;
    L10:
        return true;
    L7:
        if (hasLowerBound() == true) goto L9;
    L11:
        return false;
    L9:
        if (tooHigh(NullnessCasts.uncheckedCastNullableTToT(getLowerEndpoint())) == false) goto L11;
        goto L10
    }

    GeneralRange<T> reverse() {
        GeneralRange<T> r0 = this.reverse;
        if (r0 != null) goto L6;
        GeneralRange<T> r1 = new GeneralRange(Ordering.from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
        r1.reverse = this;
        this.reverse = r1;
        return r1;
    L6:
        return r0;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append(this.comparator).append(":");
        if (this.lowerBoundType != BoundType.CLOSED) goto L5;
        char r1 = '[';
    L6:
        StringBuilder r2 = r0.append(r1);
        if (this.hasLowerBound == false) goto L9;
        Object r3 = this.lowerEndpoint;
    L10:
        StringBuilder r4 = r2.append(r3).append(',');
        if (this.hasUpperBound == false) goto L13;
        Object r5 = this.upperEndpoint;
    L14:
        StringBuilder r6 = r4.append(r5);
        if (this.upperBoundType != BoundType.CLOSED) goto L17;
        char r7 = ']';
    L19:
        return r6.append(r7).toString();
    L17:
        r7 = ')';
        goto L19
    L13:
        r5 = "∞";
        goto L14
    L9:
        r3 = "-∞";
        goto L10
    L5:
        r1 = '(';
        goto L6
    }

    boolean tooHigh(@ParametricNullness T r9) {
        boolean r1 = false;
        if (hasUpperBound() == true) goto L5;
        return false;
    L5:
        int r2 = this.comparator.compare(r9, NullnessCasts.uncheckedCastNullableTToT(getUpperEndpoint()));
        if (r2 <= 0) goto L8;
        boolean r4 = true;
    L9:
        if (r2 != 0) goto L11;
        boolean r5 = true;
    L13:
        if (getUpperBoundType() != BoundType.OPEN) goto L16;
        r1 = true;
    L16:
        return (r1 & r5) | r4;
    L11:
        r5 = false;
        goto L13
    L8:
        r4 = false;
        goto L9
    }

    boolean tooLow(@ParametricNullness T r9) {
        boolean r1 = false;
        if (hasLowerBound() == true) goto L5;
        return false;
    L5:
        int r2 = this.comparator.compare(r9, NullnessCasts.uncheckedCastNullableTToT(getLowerEndpoint()));
        if (r2 >= 0) goto L8;
        boolean r4 = true;
    L9:
        if (r2 != 0) goto L11;
        boolean r5 = true;
    L13:
        if (getLowerBoundType() != BoundType.OPEN) goto L16;
        r1 = true;
    L16:
        return (r1 & r5) | r4;
    L11:
        r5 = false;
        goto L13
    L8:
        r4 = false;
        goto L9
    }
}
