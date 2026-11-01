package com.google.common.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Predicates {

    /* JADX INFO: renamed from: com.google.common.base.Predicates$1 */
    static /* synthetic */ class C05451 {
    }

    private static class AndPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final List<? extends Predicate<? super T>> components;

        private AndPredicate(List<? extends Predicate<? super T>> r1) {
            this.components = r1;
        }

        /* synthetic */ AndPredicate(List r1, C05451 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T r3) {
            int r0 = 0;
        L4:
            if (r0 >= this.components.size()) goto L10;
            if (this.components.get(r0).apply(r3) == false) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return false;
        L10:
            return true;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof AndPredicate) == true) goto L5;
            return false;
        L5:
            return this.components.equals(((AndPredicate) r4).components);
        }

        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }

        public String toString() {
            return Predicates.access$800("and", this.components);
        }
    }

    private static class CompositionPredicate<A, B> implements Predicate<A>, Serializable {
        private static final long serialVersionUID = 0;

        /* JADX INFO: renamed from: f */
        final Function<A, ? extends B> f95f;

        /* JADX INFO: renamed from: p */
        final Predicate<B> f96p;

        private CompositionPredicate(Predicate<B> r2, Function<A, ? extends B> r3) {
            this.f96p = (Predicate) Preconditions.checkNotNull(r2);
            this.f95f = (Function) Preconditions.checkNotNull(r3);
        }

        /* synthetic */ CompositionPredicate(Predicate r1, Function r2, C05451 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness A r3) {
            return this.f96p.apply(this.f95f.apply(r3));
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof CompositionPredicate) == false) goto L10;
            CompositionPredicate r0 = (CompositionPredicate) r5;
            if (this.f95f.equals(r0.f95f) == true) goto L7;
            return false;
        L7:
            if (this.f96p.equals(r0.f96p) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            return this.f95f.hashCode() ^ this.f96p.hashCode();
        }

        public String toString() {
            return this.f96p + "(" + this.f95f + ")";
        }
    }

    private static class ContainsPatternFromStringPredicate extends ContainsPatternPredicate {
        private static final long serialVersionUID = 0;

        ContainsPatternFromStringPredicate(String r2) {
            super(Platform.compilePattern(r2));
        }

        @Override // com.google.common.base.Predicates.ContainsPatternPredicate
        public String toString() {
            return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
        }
    }

    private static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable {
        private static final long serialVersionUID = 0;
        final CommonPattern pattern;

        ContainsPatternPredicate(CommonPattern r2) {
            this.pattern = (CommonPattern) Preconditions.checkNotNull(r2);
        }

        /* JADX INFO: renamed from: apply, reason: avoid collision after fix types in other method */
        public boolean apply2(CharSequence r2) {
            return this.pattern.matcher(r2).find();
        }

        @Override // com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(CharSequence r1) {
            return apply2(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof ContainsPatternPredicate) == false) goto L11;
            ContainsPatternPredicate r0 = (ContainsPatternPredicate) r5;
            if (Objects.equal(this.pattern.pattern(), r0.pattern.pattern()) == true) goto L7;
            return false;
        L7:
            if (this.pattern.flags() != r0.pattern.flags()) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.pattern.pattern(), Integer.valueOf(this.pattern.flags())});
        }

        public String toString() {
            return "Predicates.contains(" + MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString() + ")";
        }
    }

    private static class InPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Collection<?> target;

        private InPredicate(Collection<?> r2) {
            this.target = (Collection) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ InPredicate(Collection r1, C05451 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T r3) {
            return this.target.contains(r3);
        L7:
            return false;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof InPredicate) == true) goto L5;
            return false;
        L5:
            return this.target.equals(((InPredicate) r4).target);
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public String toString() {
            return "Predicates.in(" + this.target + ")";
        }
    }

    private static class InstanceOfPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Class<?> clazz;

        private InstanceOfPredicate(Class<?> r2) {
            this.clazz = (Class) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ InstanceOfPredicate(Class r1, C05451 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T r2) {
            return this.clazz.isInstance(r2);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof InstanceOfPredicate) == true) goto L5;
            return false;
        L5:
            if (this.clazz != ((InstanceOfPredicate) r5).clazz) goto L9;
            return true;
        L9:
            return false;
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        public String toString() {
            return "Predicates.instanceOf(" + this.clazz.getName() + ")";
        }
    }

    private static class IsEqualToPredicate implements Predicate<Object>, Serializable {
        private static final long serialVersionUID = 0;
        private final Object target;

        private IsEqualToPredicate(Object r1) {
            this.target = r1;
        }

        /* synthetic */ IsEqualToPredicate(Object r1, C05451 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@CheckForNull Object r2) {
            return this.target.equals(r2);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof IsEqualToPredicate) == true) goto L5;
            return false;
        L5:
            return this.target.equals(((IsEqualToPredicate) r4).target);
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public String toString() {
            return "Predicates.equalTo(" + this.target + ")";
        }

        <T> Predicate<T> withNarrowedType() {
            return this;
        }
    }

    private static class NotPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Predicate<T> predicate;

        NotPredicate(Predicate<T> r2) {
            this.predicate = (Predicate) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T r2) {
            return !this.predicate.apply(r2);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof NotPredicate) == true) goto L5;
            return false;
        L5:
            return this.predicate.equals(((NotPredicate) r4).predicate);
        }

        public int hashCode() {
            return ~this.predicate.hashCode();
        }

        public String toString() {
            return "Predicates.not(" + this.predicate + ")";
        }
    }

    static abstract enum ObjectPredicate extends Enum<ObjectPredicate> implements Predicate<Object> {
        private static final /* synthetic */ ObjectPredicate[] $VALUES = null;
        public static final ObjectPredicate ALWAYS_FALSE = null;
        public static final ObjectPredicate ALWAYS_TRUE = null;
        public static final ObjectPredicate IS_NULL = null;
        public static final ObjectPredicate NOT_NULL = null;

        private static /* synthetic */ ObjectPredicate[] $values() {
            return new ObjectPredicate[]{ALWAYS_TRUE, ALWAYS_FALSE, IS_NULL, NOT_NULL};
        }

        static {
            final String r1 = "ALWAYS_TRUE";
            final int r2 = 0;
            ALWAYS_TRUE = new C05461(r1, r2);
            final String r3 = "ALWAYS_FALSE";
            final int r4 = 1;
            ALWAYS_FALSE = new C05472(r3, r4);
            final String r5 = "IS_NULL";
            final int r6 = 2;
            IS_NULL = new C05483(r5, r6);
            final String r7 = "NOT_NULL";
            final int r8 = 3;
            NOT_NULL = new C05494(r7, r8);
            $VALUES = $values();
        }

        private ObjectPredicate(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ ObjectPredicate(String r1, int r2, C05451 r3) {
            this(r1, r2);
        }

        public static ObjectPredicate valueOf(String r1) {
            return (ObjectPredicate) Enum.valueOf(ObjectPredicate.class, r1);
        }

        public static ObjectPredicate[] values() {
            return (ObjectPredicate[]) $VALUES.clone();
        }

        <T> Predicate<T> withNarrowedType() {
            return this;
        }
    }

    private static class OrPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final List<? extends Predicate<? super T>> components;

        private OrPredicate(List<? extends Predicate<? super T>> r1) {
            this.components = r1;
        }

        /* synthetic */ OrPredicate(List r1, C05451 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@ParametricNullness T r3) {
            int r0 = 0;
        L4:
            if (r0 >= this.components.size()) goto L10;
            if (this.components.get(r0).apply(r3) == true) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return true;
        L10:
            return false;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof OrPredicate) == true) goto L5;
            return false;
        L5:
            return this.components.equals(((OrPredicate) r4).components);
        }

        public int hashCode() {
            return this.components.hashCode() + 87855567;
        }

        public String toString() {
            return Predicates.access$800("or", this.components);
        }
    }

    private static class SubtypeOfPredicate implements Predicate<Class<?>>, Serializable {
        private static final long serialVersionUID = 0;
        private final Class<?> clazz;

        private SubtypeOfPredicate(Class<?> r2) {
            this.clazz = (Class) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ SubtypeOfPredicate(Class r1, C05451 r2) {
            this(r1);
        }

        /* JADX INFO: renamed from: apply, reason: avoid collision after fix types in other method */
        public boolean apply2(Class<?> r2) {
            return this.clazz.isAssignableFrom(r2);
        }

        @Override // com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Class<?> r1) {
            return apply2(r1);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof SubtypeOfPredicate) == true) goto L5;
            return false;
        L5:
            if (this.clazz != ((SubtypeOfPredicate) r5).clazz) goto L9;
            return true;
        L9:
            return false;
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        public String toString() {
            return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
        }
    }

    private Predicates() {
    }

    static /* synthetic */ String access$800(String r1, Iterable r2) {
        return toStringHelper(r1, r2);
    }

    public static <T> Predicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
    }

    public static <T> Predicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
    }

    public static <T> Predicate<T> and(Predicate<? super T> r3, Predicate<? super T> r4) {
        return new AndPredicate(asList((Predicate) Preconditions.checkNotNull(r3), (Predicate) Preconditions.checkNotNull(r4)), null);
    }

    public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> r3) {
        return new AndPredicate(defensiveCopy(r3), null);
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<? super T>... r3) {
        return new AndPredicate(defensiveCopy(r3), null);
    }

    private static <T> List<Predicate<? super T>> asList(Predicate<? super T> r2, Predicate<? super T> r3) {
        return Arrays.asList(new Predicate[]{r2, r3});
    }

    public static <A, B> Predicate<A> compose(Predicate<B> r2, Function<A, ? extends B> r3) {
        return new CompositionPredicate(r2, r3, null);
    }

    public static Predicate<CharSequence> contains(Pattern r2) {
        return new ContainsPatternPredicate(new JdkPattern(r2));
    }

    public static Predicate<CharSequence> containsPattern(String r1) {
        return new ContainsPatternFromStringPredicate(r1);
    }

    static <T> List<T> defensiveCopy(Iterable<T> r4) {
        ArrayList r0 = new ArrayList();
        Iterator<T> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(Preconditions.checkNotNull(r1.next()));
        goto L4
    L6:
        return r0;
    }

    private static <T> List<T> defensiveCopy(T... r1) {
        return defensiveCopy(Arrays.asList(r1));
    }

    public static <T> Predicate<T> equalTo(@ParametricNullness T r2) {
        if (r2 != null) goto L5;
        return isNull();
    L5:
        return new IsEqualToPredicate(r2, null).withNarrowedType();
    }

    /* JADX INFO: renamed from: in */
    public static <T> Predicate<T> m186in(Collection<? extends T> r2) {
        return new InPredicate(r2, null);
    }

    public static <T> Predicate<T> instanceOf(Class<?> r2) {
        return new InstanceOfPredicate(r2, null);
    }

    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> not(Predicate<T> r1) {
        return new NotPredicate(r1);
    }

    public static <T> Predicate<T> notNull() {
        return ObjectPredicate.NOT_NULL.withNarrowedType();
    }

    /* JADX INFO: renamed from: or */
    public static <T> Predicate<T> m187or(Predicate<? super T> r3, Predicate<? super T> r4) {
        return new OrPredicate(asList((Predicate) Preconditions.checkNotNull(r3), (Predicate) Preconditions.checkNotNull(r4)), null);
    }

    /* JADX INFO: renamed from: or */
    public static <T> Predicate<T> m188or(Iterable<? extends Predicate<? super T>> r3) {
        return new OrPredicate(defensiveCopy(r3), null);
    }

    @SafeVarargs
    /* JADX INFO: renamed from: or */
    public static <T> Predicate<T> m189or(Predicate<? super T>... r3) {
        return new OrPredicate(defensiveCopy(r3), null);
    }

    public static Predicate<Class<?>> subtypeOf(Class<?> r2) {
        return new SubtypeOfPredicate(r2, null);
    }

    private static String toStringHelper(String r5, Iterable<?> r6) {
        StringBuilder r0 = new StringBuilder("Predicates.").append(r5).append('(');
        boolean r1 = true;
        Iterator<?> r2 = r6.iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        Object r3 = r2.next();
        if (r1 == true) goto L8;
        r0.append(',');
    L8:
        r0.append(r3);
        r1 = false;
        goto L4
    L10:
        return r0.append(')').toString();
    }
}
