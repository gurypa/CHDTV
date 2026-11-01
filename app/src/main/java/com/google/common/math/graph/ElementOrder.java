package com.google.common.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.errorprone.annotations.Immutable;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
public final class ElementOrder<T> {

    @CheckForNull
    private final Comparator<T> comparator;
    private final Type type;

    public static final enum Type extends Enum<Type> {
        private static final /* synthetic */ Type[] $VALUES = null;
        public static final Type INSERTION = null;
        public static final Type SORTED = null;
        public static final Type STABLE = null;
        public static final Type UNORDERED = null;

        private static /* synthetic */ Type[] $values() {
            return new Type[]{UNORDERED, STABLE, INSERTION, SORTED};
        }

        static {
            UNORDERED = new Type("UNORDERED", 0);
            STABLE = new Type("STABLE", 1);
            INSERTION = new Type("INSERTION", 2);
            SORTED = new Type("SORTED", 3);
            $VALUES = $values();
        }

        private Type(String r1, int r2) {
            super(r1, r2);
        }

        public static Type valueOf(String r1) {
            return (Type) Enum.valueOf(Type.class, r1);
        }

        public static Type[] values() {
            return (Type[]) $VALUES.clone();
        }
    }

    private ElementOrder(Type r5, @CheckForNull Comparator<T> r6) {
        this.type = (Type) Preconditions.checkNotNull(r5);
        this.comparator = r6;
        boolean r1 = true;
        if (r5 != Type.SORTED) goto L5;
        boolean r0 = true;
    L6:
        if (r6 == null) goto L8;
        boolean r3 = true;
    L9:
        if (r0 == r3) goto L12;
        r1 = false;
    L12:
        Preconditions.checkState(r1);
        return;
    L8:
        r3 = false;
        goto L9
    L5:
        r0 = false;
        goto L6
    }

    public static <S> ElementOrder<S> insertion() {
        return new ElementOrder(Type.INSERTION, null);
    }

    public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
        return new ElementOrder(Type.SORTED, Ordering.natural());
    }

    public static <S> ElementOrder<S> sorted(Comparator<S> r3) {
        return new ElementOrder(Type.SORTED, (Comparator) Preconditions.checkNotNull(r3));
    }

    public static <S> ElementOrder<S> stable() {
        return new ElementOrder(Type.STABLE, null);
    }

    public static <S> ElementOrder<S> unordered() {
        return new ElementOrder(Type.UNORDERED, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T1 extends T> ElementOrder<T1> cast() {
        return this;
    }

    public Comparator<T> comparator() {
        if (this.comparator == null) goto L7;
        return this.comparator;
    L7:
        throw new UnsupportedOperationException("This ordering does not define a comparator.");
    }

    <K extends T, V> Map<K, V> createMap(int r2) {
        switch(this.type.ordinal()) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L9;
            case 3: goto L7;
            default: goto L5;
        };
    L5:
        throw new AssertionError();
    L7:
        return Maps.newTreeMap(comparator());
    L9:
        return Maps.newLinkedHashMapWithExpectedSize(r2);
    L11:
        return Maps.newHashMapWithExpectedSize(r2);
    }

    public boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof ElementOrder) == true) goto L8;
        return false;
    L8:
        ElementOrder r1 = (ElementOrder) r6;
        if (this.type != r1.type) goto L14;
        if (Objects.equal(this.comparator, r1.comparator) == false) goto L14;
        return true;
    L14:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.type, this.comparator});
    }

    public String toString() {
        MoreObjects.ToStringHelper r0 = MoreObjects.toStringHelper(this).add("type", this.type);
        if (this.comparator == null) goto L6;
        r0.add("comparator", this.comparator);
    L6:
        return r0.toString();
    }

    public Type type() {
        return this.type;
    }
}
