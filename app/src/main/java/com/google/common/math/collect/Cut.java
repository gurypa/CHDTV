package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.Comparable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
    private static final long serialVersionUID = 0;
    final C endpoint;

    /* JADX INFO: renamed from: com.google.common.collect.Cut$1 */
    static /* synthetic */ class C06131 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType = null;

        static {
            $SwitchMap$com$google$common$collect$BoundType = new int[BoundType.values().length];
            $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            $SwitchMap$com$google$common$collect$BoundType[BoundType.OPEN.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    private static final class AboveAll extends Cut<Comparable<?>> {
        private static final AboveAll INSTANCE = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new AboveAll();
        }

        private AboveAll() {
            super("");
        }

        static /* synthetic */ AboveAll access$100() {
            return INSTANCE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.Cut
        public int compareTo(Cut<Comparable<?>> r2) {
            if (r2 != this) goto L4;
            return 0;
        L4:
            return 1;
        }

        @Override // com.google.common.collect.Cut, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object r1) {
            return compareTo((Cut) r1);
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder r2) {
            r2.append("+∞)");
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> r2) {
            return r2.maxValue();
        }

        @Override // com.google.common.collect.Cut
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(Comparable<?> r2) {
            return false;
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> r2) {
            throw new AssertionError();
        }

        public String toString() {
            return "+∞";
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withLowerBoundType(BoundType r3, DiscreteDomain<Comparable<?>> r4) {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withUpperBoundType(BoundType r2, DiscreteDomain<Comparable<?>> r3) {
            throw new IllegalStateException();
        }
    }

    private static final class AboveValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        AboveValue(C r2) {
            super((Comparable) Preconditions.checkNotNull(r2));
        }

        @Override // com.google.common.collect.Cut
        Cut<C> canonical(DiscreteDomain<C> r3) {
            Comparable r0 = leastValueAbove(r3);
            if (r0 == null) goto L6;
            return belowValue(r0);
        L6:
            return Cut.aboveAll();
        }

        @Override // com.google.common.collect.Cut, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object r1) {
            return super.compareTo((Cut) r1);
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder r3) {
            r3.append('(').append(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder r3) {
            r3.append(this.endpoint).append(']');
        }

        @Override // com.google.common.collect.Cut
        C greatestValueBelow(DiscreteDomain<C> r2) {
            return this.endpoint;
        }

        @Override // com.google.common.collect.Cut
        public int hashCode() {
            return ~this.endpoint.hashCode();
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(C r2) {
            if (Range.compareOrThrow(this.endpoint, r2) >= 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.Cut
        @CheckForNull
        C leastValueAbove(DiscreteDomain<C> r2) {
            return (C) r2.next(this.endpoint);
        }

        public String toString() {
            return "/" + this.endpoint + "\\";
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            return BoundType.OPEN;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            return BoundType.CLOSED;
        }

        @Override // com.google.common.collect.Cut
        Cut<C> withLowerBoundType(BoundType r3, DiscreteDomain<C> r4) {
            switch(C06131.$SwitchMap$com$google$common$collect$BoundType[r3.ordinal()]) {
                case 1: goto L7;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            return this;
        L7:
            Comparable r0 = r4.next(this.endpoint);
            if (r0 != null) goto L11;
            return Cut.belowAll();
        L11:
            return belowValue(r0);
        L5:
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        Cut<C> withUpperBoundType(BoundType r3, DiscreteDomain<C> r4) {
            switch(C06131.$SwitchMap$com$google$common$collect$BoundType[r3.ordinal()]) {
                case 1: goto L11;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            Comparable r0 = r4.next(this.endpoint);
            if (r0 != null) goto L10;
            return Cut.aboveAll();
        L10:
            return belowValue(r0);
        L11:
            return this;
        L5:
            throw new AssertionError();
        }
    }

    private static final class BelowAll extends Cut<Comparable<?>> {
        private static final BelowAll INSTANCE = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new BelowAll();
        }

        private BelowAll() {
            super("");
        }

        static /* synthetic */ BelowAll access$000() {
            return INSTANCE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> r2) {
            return Cut.belowValue(r2.minValue());
        L5:
            return this;
        }

        @Override // com.google.common.collect.Cut
        public int compareTo(Cut<Comparable<?>> r2) {
            if (r2 != this) goto L4;
            return 0;
        L4:
            return -1;
        }

        @Override // com.google.common.collect.Cut, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object r1) {
            return compareTo((Cut) r1);
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder r2) {
            r2.append("(-∞");
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(Comparable<?> r2) {
            return true;
        }

        @Override // com.google.common.collect.Cut
        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> r2) {
            return r2.minValue();
        }

        public String toString() {
            return "-∞";
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withLowerBoundType(BoundType r2, DiscreteDomain<Comparable<?>> r3) {
            throw new IllegalStateException();
        }

        @Override // com.google.common.collect.Cut
        Cut<Comparable<?>> withUpperBoundType(BoundType r3, DiscreteDomain<Comparable<?>> r4) {
            throw new AssertionError("this statement should be unreachable");
        }
    }

    private static final class BelowValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        BelowValue(C r2) {
            super((Comparable) Preconditions.checkNotNull(r2));
        }

        @Override // com.google.common.collect.Cut, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object r1) {
            return super.compareTo((Cut) r1);
        }

        @Override // com.google.common.collect.Cut
        void describeAsLowerBound(StringBuilder r3) {
            r3.append('[').append(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        void describeAsUpperBound(StringBuilder r3) {
            r3.append(this.endpoint).append(')');
        }

        @Override // com.google.common.collect.Cut
        @CheckForNull
        C greatestValueBelow(DiscreteDomain<C> r2) {
            return (C) r2.previous(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        public int hashCode() {
            return this.endpoint.hashCode();
        }

        @Override // com.google.common.collect.Cut
        boolean isLessThan(C r2) {
            if (Range.compareOrThrow(this.endpoint, r2) > 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.Cut
        C leastValueAbove(DiscreteDomain<C> r2) {
            return this.endpoint;
        }

        public String toString() {
            return "\\" + this.endpoint + "/";
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsLowerBound() {
            return BoundType.CLOSED;
        }

        @Override // com.google.common.collect.Cut
        BoundType typeAsUpperBound() {
            return BoundType.OPEN;
        }

        @Override // com.google.common.collect.Cut
        Cut<C> withLowerBoundType(BoundType r3, DiscreteDomain<C> r4) {
            switch(C06131.$SwitchMap$com$google$common$collect$BoundType[r3.ordinal()]) {
                case 1: goto L11;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            Comparable r0 = r4.previous(this.endpoint);
            if (r0 != null) goto L10;
            return Cut.belowAll();
        L10:
            return new AboveValue(r0);
        L11:
            return this;
        L5:
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        Cut<C> withUpperBoundType(BoundType r3, DiscreteDomain<C> r4) {
            switch(C06131.$SwitchMap$com$google$common$collect$BoundType[r3.ordinal()]) {
                case 1: goto L7;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            return this;
        L7:
            Comparable r0 = r4.previous(this.endpoint);
            if (r0 != null) goto L11;
            return Cut.aboveAll();
        L11:
            return new AboveValue(r0);
        L5:
            throw new AssertionError();
        }
    }

    Cut(C r1) {
        this.endpoint = r1;
    }

    static <C extends Comparable> Cut<C> aboveAll() {
        return AboveAll.access$100();
    }

    static <C extends Comparable> Cut<C> aboveValue(C r1) {
        return new AboveValue(r1);
    }

    static <C extends Comparable> Cut<C> belowAll() {
        return BelowAll.access$000();
    }

    static <C extends Comparable> Cut<C> belowValue(C r1) {
        return new BelowValue(r1);
    }

    Cut<C> canonical(DiscreteDomain<C> r1) {
        return this;
    }

    public int compareTo(Cut<C> r4) {
        if (r4 != belowAll()) goto L7;
        return 1;
    L7:
        if (r4 != aboveAll()) goto L10;
        return -1;
    L10:
        int r0 = Range.compareOrThrow(this.endpoint, r4.endpoint);
        if (r0 == 0) goto L14;
        return r0;
    L14:
        return Boolean.compare(this instanceof AboveValue, r4 instanceof AboveValue);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object r1) {
        return compareTo((Cut) r1);
    }

    abstract void describeAsLowerBound(StringBuilder r1);

    abstract void describeAsUpperBound(StringBuilder r1);

    C endpoint() {
        return this.endpoint;
    }

    public boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof Cut) == true) goto L12;
        return false;
    L12:
        if (compareTo((Cut) r4) != 0) goto L14;
        return true;
    L14:
        return false;
    L10:
        return false;
    }

    @CheckForNull
    abstract C greatestValueBelow(DiscreteDomain<C> r1);

    public abstract int hashCode();

    abstract boolean isLessThan(C r1);

    @CheckForNull
    abstract C leastValueAbove(DiscreteDomain<C> r1);

    abstract BoundType typeAsLowerBound();

    abstract BoundType typeAsUpperBound();

    abstract Cut<C> withLowerBoundType(BoundType r1, DiscreteDomain<C> r2);

    abstract Cut<C> withUpperBoundType(BoundType r1, DiscreteDomain<C> r2);
}
