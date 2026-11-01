package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Interners {

    /* JADX INFO: renamed from: com.google.common.collect.Interners$1 */
    static /* synthetic */ class C06441 {
    }

    public static class InternerBuilder {
        private final MapMaker mapMaker;
        private boolean strong;

        private InternerBuilder() {
            this.mapMaker = new MapMaker();
            this.strong = true;
        }

        /* synthetic */ InternerBuilder(C06441 r1) {
            this();
        }

        public <E> Interner<E> build() {
            if (this.strong == true) goto L6;
            this.mapMaker.weakKeys();
        L6:
            return new InternerImpl(this.mapMaker, null);
        }

        public InternerBuilder concurrencyLevel(int r2) {
            this.mapMaker.concurrencyLevel(r2);
            return this;
        }

        public InternerBuilder strong() {
            this.strong = true;
            return this;
        }

        public InternerBuilder weak() {
            this.strong = false;
            return this;
        }
    }

    private static class InternerFunction<E> implements Function<E, E> {
        private final Interner<E> interner;

        public InternerFunction(Interner<E> r1) {
            this.interner = r1;
        }

        @Override // com.google.common.base.Function
        public E apply(E r2) {
            return this.interner.intern(r2);
        }

        @Override // com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof InternerFunction) == true) goto L5;
            return false;
        L5:
            return this.interner.equals(((InternerFunction) r4).interner);
        }

        public int hashCode() {
            return this.interner.hashCode();
        }
    }

    static final class InternerImpl<E> implements Interner<E> {
        final MapMakerInternalMap<E, MapMaker.Dummy, ?, ?> map;

        private InternerImpl(MapMaker r2) {
            this.map = MapMakerInternalMap.createWithDummyValues(r2.keyEquivalence(Equivalence.equals()));
        }

        /* synthetic */ InternerImpl(MapMaker r1, C06441 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.Interner
        public E intern(E r4) {
        L2:
            MapMakerInternalMap.InternalEntry r0 = this.map.getEntry(r4);
            if (r0 == null) goto L9;
            E r1 = (E) r0.getKey();
            if (r1 == null) goto L9;
            return r1;
        L9:
            if (this.map.putIfAbsent(r4, MapMaker.Dummy.VALUE) != null) goto L2;
            return r4;
        }
    }

    private Interners() {
    }

    public static <E> Function<E, E> asFunction(Interner<E> r2) {
        return new InternerFunction((Interner) Preconditions.checkNotNull(r2));
    }

    public static InternerBuilder newBuilder() {
        return new InternerBuilder(null);
    }

    public static <E> Interner<E> newStrongInterner() {
        return newBuilder().strong().build();
    }

    public static <E> Interner<E> newWeakInterner() {
        return newBuilder().weak().build();
    }
}
