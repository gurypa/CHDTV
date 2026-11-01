package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Primitives;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
    private static final long serialVersionUID = 3637540370352322684L;

    @CheckForNull
    @LazyInit
    private transient TypeResolver covariantTypeResolver;

    @CheckForNull
    @LazyInit
    private transient TypeResolver invariantTypeResolver;
    private final Type runtimeType;

    private static class Bounds {
        private final Type[] bounds;
        private final boolean target;

        Bounds(Type[] r1, boolean r2) {
            this.bounds = r1;
            this.target = r2;
        }

        boolean isSubtypeOf(Type r7) {
            Type[] r0 = this.bounds;
            int r1 = r0.length;
            int r2 = 0;
        L3:
            if (r2 >= r1) goto L10;
            if (TypeToken.m376of(r0[r2]).isSubtypeOf(r7) == this.target) goto L7;
            r2 = r2 + 1;
            goto L3
        L7:
            return this.target;
        L10:
            return !this.target;
        }

        boolean isSupertypeOf(Type r8) {
            TypeToken<?> r0 = TypeToken.m376of(r8);
            Type[] r1 = this.bounds;
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L10;
            if (r0.isSubtypeOf(r1[r3]) == this.target) goto L7;
            r3 = r3 + 1;
            goto L3
        L7:
            return this.target;
        L10:
            return !this.target;
        }
    }

    private final class ClassSet extends TypeToken<T>.TypeSet {
        private static final long serialVersionUID = 0;

        @CheckForNull
        private transient ImmutableSet<TypeToken<? super T>> classes;
        final /* synthetic */ TypeToken this$0;

        private ClassSet(TypeToken r1) {
            this.this$0 = r1;
            super(r1);
        }

        /* synthetic */ ClassSet(TypeToken r1, C08791 r2) {
            this(r1);
        }

        private Object readResolve() {
            return this.this$0.getTypes().classes();
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet
        public TypeToken<T>.TypeSet classes() {
            return this;
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet<TypeToken<? super T>> r0 = this.classes;
            if (r0 != null) goto L6;
            ImmutableSet<TypeToken<? super T>> r2 = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(this.this$0)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            this.classes = r2;
            return r2;
        L6:
            return r0;
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet
        public TypeToken<T>.TypeSet interfaces() {
            throw new UnsupportedOperationException("classes().interfaces() not supported.");
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet
        public Set<Class<? super T>> rawTypes() {
            return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.access$300(this.this$0)));
        }
    }

    private final class InterfaceSet extends TypeToken<T>.TypeSet {
        private static final long serialVersionUID = 0;
        private final transient TypeToken<T>.TypeSet allTypes;

        @CheckForNull
        private transient ImmutableSet<TypeToken<? super T>> interfaces;
        final /* synthetic */ TypeToken this$0;

        InterfaceSet(TypeToken r1, TypeToken<T>.TypeSet r2) {
            this.this$0 = r1;
            super(r1);
            this.allTypes = r2;
        }

        private Object readResolve() {
            return this.this$0.getTypes().interfaces();
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet
        public TypeToken<T>.TypeSet classes() {
            throw new UnsupportedOperationException("interfaces().classes() not supported.");
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet<TypeToken<? super T>> r0 = this.interfaces;
            if (r0 != null) goto L6;
            ImmutableSet<TypeToken<? super T>> r1 = FluentIterable.from(this.allTypes).filter(TypeFilter.INTERFACE_ONLY).toSet();
            this.interfaces = r1;
            return r1;
        L6:
            return r0;
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet
        public TypeToken<T>.TypeSet interfaces() {
            return this;
        }

        @Override // com.google.common.reflect.TypeToken.TypeSet
        public Set<Class<? super T>> rawTypes() {
            return FluentIterable.from(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.access$300(this.this$0))).filter(new TypeToken$InterfaceSet$$ExternalSyntheticLambda0()).toSet();
        }
    }

    private static final class SimpleTypeToken<T> extends TypeToken<T> {
        private static final long serialVersionUID = 0;

        SimpleTypeToken(Type r2) {
            super(r2, null);
        }
    }

    private static abstract class TypeCollector<K> {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = null;
        static final TypeCollector<Class<?>> FOR_RAW_TYPE = null;

        private static class ForwardingTypeCollector<K> extends TypeCollector<K> {
            private final TypeCollector<K> delegate;

            ForwardingTypeCollector(TypeCollector<K> r2) {
                super(null);
                this.delegate = r2;
            }

            @Override // com.google.common.reflect.TypeToken.TypeCollector
            Iterable<? extends K> getInterfaces(K r2) {
                return this.delegate.getInterfaces(r2);
            }

            @Override // com.google.common.reflect.TypeToken.TypeCollector
            Class<?> getRawType(K r2) {
                return this.delegate.getRawType(r2);
            }

            @Override // com.google.common.reflect.TypeToken.TypeCollector
            @CheckForNull
            K getSuperclass(K r2) {
                return this.delegate.getSuperclass(r2);
            }
        }

        static {
            FOR_GENERIC_TYPE = new C08831();
            FOR_RAW_TYPE = new C08842();
        }

        private TypeCollector() {
        }

        /* synthetic */ TypeCollector(C08791 r1) {
            this();
        }

        /* JADX WARN: Multi-variable type inference failed */
        private int collectTypes(K r6, Map<? super K, Integer> r7) {
            Integer r0 = r7.get(r6);
            if (r0 != null) goto L5;
            boolean r1 = getRawType(r6).isInterface();
            Iterator<? extends K> r2 = getInterfaces(r6).iterator();
            int r3 = r1;
        L8:
            if (r2.hasNext() == false) goto L10;
            r3 = Math.max(r3, collectTypes(r2.next(), r7));
            goto L8
        L10:
            K r4 = getSuperclass(r6);
            int r5 = r3;
            if (r4 == null) goto L13;
            r5 = Math.max(r3, collectTypes(r4, r7));
        L13:
            r7.put(r6, Integer.valueOf(r5 + 1));
            return r5 + 1;
        L5:
            return r0.intValue();
        }

        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> r2, final Comparator<? super V> r3) {
            return (ImmutableList<K>) new C08864(r3, r2).immutableSortedCopy(r2.keySet());
        }

        final TypeCollector<K> classesOnly() {
            return new C08853(this, this);
        }

        ImmutableList<K> collectTypes(Iterable<? extends K> r4) {
            HashMap r0 = Maps.newHashMap();
            Iterator<? extends K> r1 = r4.iterator();
        L4:
            if (r1.hasNext() == false) goto L7;
            collectTypes(r1.next(), r0);
            goto L4
        L7:
            return sortKeysByValue(r0, Ordering.natural().reverse());
        }

        final ImmutableList<K> collectTypes(K r2) {
            return collectTypes(ImmutableList.m213of(r2));
        }

        abstract Iterable<? extends K> getInterfaces(K r1);

        abstract Class<?> getRawType(K r1);

        @CheckForNull
        abstract K getSuperclass(K r1);
    }

    private static abstract enum TypeFilter extends Enum<TypeFilter> implements Predicate<TypeToken<?>> {
        private static final /* synthetic */ TypeFilter[] $VALUES = null;
        public static final TypeFilter IGNORE_TYPE_VARIABLE_OR_WILDCARD = null;
        public static final TypeFilter INTERFACE_ONLY = null;

        private static /* synthetic */ TypeFilter[] $values() {
            return new TypeFilter[]{IGNORE_TYPE_VARIABLE_OR_WILDCARD, INTERFACE_ONLY};
        }

        static {
            final String r1 = "IGNORE_TYPE_VARIABLE_OR_WILDCARD";
            final int r2 = 0;
            IGNORE_TYPE_VARIABLE_OR_WILDCARD = new C08871(r1, r2);
            final String r3 = "INTERFACE_ONLY";
            final int r4 = 1;
            INTERFACE_ONLY = new C08882(r3, r4);
            $VALUES = $values();
        }

        private TypeFilter(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ TypeFilter(String r1, int r2, C08791 r3) {
            this(r1, r2);
        }

        public static TypeFilter valueOf(String r1) {
            return (TypeFilter) Enum.valueOf(TypeFilter.class, r1);
        }

        public static TypeFilter[] values() {
            return (TypeFilter[]) $VALUES.clone();
        }
    }

    public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable {
        private static final long serialVersionUID = 0;
        final /* synthetic */ TypeToken this$0;

        @CheckForNull
        private transient ImmutableSet<TypeToken<? super T>> types;

        TypeSet(TypeToken r1) {
            this.this$0 = r1;
        }

        public TypeToken<T>.TypeSet classes() {
            return new ClassSet(this.this$0, null);
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Collection delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet<TypeToken<? super T>> r0 = this.types;
            if (r0 != null) goto L6;
            ImmutableSet<TypeToken<? super T>> r2 = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.collectTypes(this.this$0)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            this.types = r2;
            return r2;
        L6:
            return r0;
        }

        public TypeToken<T>.TypeSet interfaces() {
            return new InterfaceSet(this.this$0, this);
        }

        public Set<Class<? super T>> rawTypes() {
            return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.access$300(this.this$0)));
        }
    }

    protected TypeToken() {
        this.runtimeType = capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }

    protected TypeToken(Class<?> r3) {
        Type r0 = super.capture();
        if ((r0 instanceof Class) == false) goto L5;
        this.runtimeType = r0;
        return;
    L5:
        this.runtimeType = TypeResolver.covariantly(r3).resolveType(r0);
    }

    private TypeToken(Type r2) {
        this.runtimeType = (Type) Preconditions.checkNotNull(r2);
    }

    /* synthetic */ TypeToken(Type r1, C08791 r2) {
        this(r1);
    }

    static /* synthetic */ TypeResolver access$000(TypeToken r1) {
        return r1.getCovariantTypeResolver();
    }

    static /* synthetic */ TypeResolver access$100(TypeToken r1) {
        return r1.getInvariantTypeResolver();
    }

    static /* synthetic */ ImmutableSet access$300(TypeToken r1) {
        return r1.getRawTypes();
    }

    static /* synthetic */ Type access$500(TypeToken r1) {
        return r1.runtimeType;
    }

    private static Bounds any(Type[] r2) {
        return new Bounds(r2, true);
    }

    @CheckForNull
    private TypeToken<? super T> boundAsSuperclass(Type r3) {
        TypeToken<? super T> r0 = (TypeToken<? super T>) m376of(r3);
        if (r0.getRawType().isInterface() == false) goto L7;
        return null;
    L7:
        return r0;
    }

    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] r7) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        int r1 = r7.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L9;
        TypeToken<?> r4 = m376of(r7[r2]);
        if (r4.getRawType().isInterface() == false) goto L7;
        r0.add(r4);
    L7:
        r2 = r2 + 1;
        goto L3
    L9:
        return r0.build();
    }

    private static Type canonicalizeTypeArg(TypeVariable<?> r1, Type r2) {
        if ((r2 instanceof WildcardType) == false) goto L6;
        return canonicalizeWildcardType(r1, (WildcardType) r2);
    L6:
        return canonicalizeWildcardsInType(r2);
    }

    private static WildcardType canonicalizeWildcardType(TypeVariable<?> r8, WildcardType r9) {
        Type[] r0 = r8.getBounds();
        ArrayList r1 = new ArrayList();
        Type[] r2 = r9.getUpperBounds();
        int r3 = r2.length;
        int r5 = 0;
    L3:
        if (r5 >= r3) goto L9;
        Type r6 = r2[r5];
        if (any(r0).isSubtypeOf(r6) == true) goto L7;
        r1.add(canonicalizeWildcardsInType(r6));
    L7:
        r5 = r5 + 1;
        goto L3
    L9:
        return new Types.WildcardTypeImpl(r9.getLowerBounds(), (Type[]) r1.toArray(new Type[0]));
    }

    private static ParameterizedType canonicalizeWildcardsInParameterizedType(ParameterizedType r6) {
        Class r0 = (Class) r6.getRawType();
        TypeVariable<Class<T>>[] r1 = r0.getTypeParameters();
        Type[] r2 = r6.getActualTypeArguments();
        int r3 = 0;
    L4:
        if (r3 >= r2.length) goto L7;
        r2[r3] = canonicalizeTypeArg(r1[r3], r2[r3]);
        r3 = r3 + 1;
        goto L4
    L7:
        return Types.newParameterizedTypeWithOwner(r6.getOwnerType(), r0, r2);
    }

    private static Type canonicalizeWildcardsInType(Type r1) {
        if ((r1 instanceof ParameterizedType) == false) goto L7;
        return canonicalizeWildcardsInParameterizedType((ParameterizedType) r1);
    L7:
        if ((r1 instanceof GenericArrayType) == true) goto L9;
        return r1;
    L9:
        return Types.newArrayType(canonicalizeWildcardsInType(((GenericArrayType) r1).getGenericComponentType()));
    }

    private static Bounds every(Type[] r2) {
        return new Bounds(r2, false);
    }

    private TypeToken<? extends T> getArraySubtype(Class<?> r5) {
        Class<?> r0 = r5.getComponentType();
        if (r0 == null) goto L7;
        return (TypeToken<? extends T>) m376of(newArrayClassOrGenericArrayType(((TypeToken) Objects.requireNonNull(getComponentType())).getSubtype(r0).runtimeType));
    L7:
        throw new IllegalArgumentException(r5 + " does not appear to be a subtype of " + this);
    }

    private TypeToken<? super T> getArraySupertype(Class<? super T> r5) {
        TypeToken<?> r0 = getComponentType();
        if (r0 == null) goto L7;
        return (TypeToken<? super T>) m376of(newArrayClassOrGenericArrayType(r0.getSupertype((Class) Objects.requireNonNull(r5.getComponentType())).runtimeType));
    L7:
        throw new IllegalArgumentException(r5 + " isn't a super type of " + this);
    }

    private TypeResolver getCovariantTypeResolver() {
        TypeResolver r0 = this.covariantTypeResolver;
        if (r0 != null) goto L6;
        TypeResolver r1 = TypeResolver.covariantly(this.runtimeType);
        this.covariantTypeResolver = r1;
        return r1;
    L6:
        return r0;
    }

    private TypeResolver getInvariantTypeResolver() {
        TypeResolver r0 = this.invariantTypeResolver;
        if (r0 != null) goto L6;
        TypeResolver r1 = TypeResolver.invariantly(this.runtimeType);
        this.invariantTypeResolver = r1;
        return r1;
    L6:
        return r0;
    }

    @CheckForNull
    private Type getOwnerTypeIfPresent() {
        if ((this.runtimeType instanceof ParameterizedType) == false) goto L7;
        return ((ParameterizedType) this.runtimeType).getOwnerType();
    L7:
        if ((this.runtimeType instanceof Class) == true) goto L9;
        return null;
    L9:
        return ((Class) this.runtimeType).getEnclosingClass();
    }

    private ImmutableSet<Class<? super T>> getRawTypes() {
        final ImmutableSet.Builder r0 = ImmutableSet.builder();
        new C08824(this, r0).visit(new Type[]{this.runtimeType});
        return r0.build();
    }

    private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> r4, Type[] r5) {
        if (r5.length <= 0) goto L7;
        return (TypeToken<? extends T>) m376of(r5[0]).getSubtype(r4);
    L7:
        throw new IllegalArgumentException(r4 + " isn't a subclass of " + this);
    }

    private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> r6, Type[] r7) {
        int r0 = r7.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L10;
        TypeToken<?> r3 = m376of(r7[r1]);
        if (r3.isSubtypeOf(r6) == true) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return (TypeToken<? super T>) r3.getSupertype(r6);
    L10:
        throw new IllegalArgumentException(r6 + " isn't a super type of " + this);
    }

    /* JADX INFO: renamed from: is */
    private boolean m374is(Type r5, TypeVariable<?> r6) {
        if (this.runtimeType.equals(r5) == false) goto L6;
        return true;
    L6:
        if ((r5 instanceof WildcardType) == false) goto L15;
        WildcardType r0 = canonicalizeWildcardType(r6, (WildcardType) r5);
        if (every(r0.getUpperBounds()).isSupertypeOf(this.runtimeType) == true) goto L10;
    L12:
        return false;
    L10:
        if (every(r0.getLowerBounds()).isSubtypeOf(this.runtimeType) == false) goto L12;
        return true;
    L15:
        return canonicalizeWildcardsInType(this.runtimeType).equals(canonicalizeWildcardsInType(r5));
    }

    private boolean isOwnedBySubtypeOf(Type r5) {
        Iterator<TypeToken<? super T>> r0 = getTypes().iterator();
    L4:
        if (r0.hasNext() == false) goto L12;
        Type r2 = r0.next().getOwnerTypeIfPresent();
        if (r2 == null) goto L4;
        if (m376of(r2).isSubtypeOf(r5) == false) goto L4;
        return true;
    L12:
        return false;
    }

    private boolean isSubtypeOfArrayType(GenericArrayType r4) {
        if ((this.runtimeType instanceof Class) == false) goto L10;
        Class r0 = (Class) this.runtimeType;
        if (r0.isArray() == true) goto L8;
        return false;
    L8:
        return m375of(r0.getComponentType()).isSubtypeOf(r4.getGenericComponentType());
    L10:
        if ((this.runtimeType instanceof GenericArrayType) == true) goto L12;
        return false;
    L12:
        return m376of(((GenericArrayType) this.runtimeType).getGenericComponentType()).isSubtypeOf(r4.getGenericComponentType());
    }

    private boolean isSubtypeOfParameterizedType(ParameterizedType r10) {
        Class<? super Object> r0 = m376of(r10).getRawType();
        if (someRawTypeIsSubclassOf(r0) == true) goto L5;
        return false;
    L5:
        TypeVariable<Class<? super Object>>[] r1 = r0.getTypeParameters();
        Type[] r3 = r10.getActualTypeArguments();
        int r4 = 0;
    L7:
        if (r4 >= r1.length) goto L13;
        if (m376of(getCovariantTypeResolver().resolveType(r1[r4])).m374is(r3[r4], r1[r4]) == false) goto L10;
        r4 = r4 + 1;
        goto L7
    L10:
        return false;
    L13:
        if (Modifier.isStatic(((Class) r10.getRawType()).getModifiers()) == false) goto L15;
    L18:
        return true;
    L15:
        if (r10.getOwnerType() == null) goto L18;
        if (isOwnedBySubtypeOf(r10.getOwnerType()) == true) goto L18;
        return false;
    }

    private boolean isSupertypeOfArray(GenericArrayType r4) {
        if ((this.runtimeType instanceof Class) == false) goto L11;
        Class r0 = (Class) this.runtimeType;
        if (r0.isArray() == true) goto L9;
        return r0.isAssignableFrom(Object[].class);
    L9:
        return m376of(r4.getGenericComponentType()).isSubtypeOf(r0.getComponentType());
    L11:
        if ((this.runtimeType instanceof GenericArrayType) == true) goto L13;
        return false;
    L13:
        return m376of(r4.getGenericComponentType()).isSubtypeOf(((GenericArrayType) this.runtimeType).getGenericComponentType());
    }

    private boolean isWrapper() {
        return Primitives.allWrapperTypes().contains(this.runtimeType);
    }

    private static Type newArrayClassOrGenericArrayType(Type r1) {
        return Types.JavaVersion.JAVA7.newArrayType(r1);
    }

    /* JADX INFO: renamed from: of */
    public static <T> TypeToken<T> m375of(Class<T> r1) {
        return new SimpleTypeToken(r1);
    }

    /* JADX INFO: renamed from: of */
    public static TypeToken<?> m376of(Type r1) {
        return new SimpleTypeToken(r1);
    }

    private TypeToken<?> resolveSupertype(Type r3) {
        TypeToken<?> r0 = m376of(getCovariantTypeResolver().resolveType(r3));
        r0.covariantTypeResolver = this.covariantTypeResolver;
        r0.invariantTypeResolver = this.invariantTypeResolver;
        return r0;
    }

    private Type resolveTypeArgsForSubclass(Class<?> r5) {
        if ((this.runtimeType instanceof Class) == true) goto L5;
    L9:
        TypeToken r0 = toGenericType(r5);
        Type r1 = r0.getSupertype(getRawType()).runtimeType;
        return new TypeResolver().where(r1, this.runtimeType).resolveType(r0.runtimeType);
    L5:
        if (r5.getTypeParameters().length != 0) goto L7;
    L8:
        return r5;
    L7:
        if (getRawType().getTypeParameters().length == 0) goto L9;
        goto L8
    }

    private boolean someRawTypeIsSubclassOf(Class<?> r4) {
        UnmodifiableIterator<Class<? super T>> r0 = getRawTypes().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r4.isAssignableFrom(r0.next()) == false) goto L4;
        return true;
    L10:
        return false;
    }

    static <T> TypeToken<? extends T> toGenericType(Class<T> r3) {
        if (r3.isArray() == true) goto L5;
        TypeVariable<Class<T>>[] r0 = r3.getTypeParameters();
        if (r3.isMemberClass() == true) goto L9;
    L11:
        Type r1 = null;
    L13:
        if (r0.length > 0) goto L21;
        if (r1 == null) goto L19;
        if (r1 != r3.getEnclosingClass()) goto L21;
    L19:
        return m375of(r3);
    L21:
        return (TypeToken<? extends T>) m376of(Types.newParameterizedTypeWithOwner(r1, r3, r0));
    L9:
        if (Modifier.isStatic(r3.getModifiers()) == true) goto L11;
        r1 = toGenericType(r3.getEnclosingClass()).runtimeType;
        goto L13
    L5:
        return (TypeToken<? extends T>) m376of(Types.newArrayType(toGenericType(r3.getComponentType()).runtimeType));
    }

    public final Invokable<T, T> constructor(final Constructor<?> r4) {
        if (r4.getDeclaringClass() != getRawType()) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "%s not declared by %s", r4, getRawType());
        return new C08802(this, r4);
    L5:
        r0 = false;
        goto L6
    }

    public boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof TypeToken) == true) goto L5;
        return false;
    L5:
        return this.runtimeType.equals(((TypeToken) r4).runtimeType);
    }

    @CheckForNull
    public final TypeToken<?> getComponentType() {
        Type r0 = Types.getComponentType(this.runtimeType);
        if (r0 != null) goto L7;
        return null;
    L7:
        return m376of(r0);
    }

    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if ((this.runtimeType instanceof TypeVariable) == false) goto L7;
        return boundsAsInterfaces(((TypeVariable) this.runtimeType).getBounds());
    L7:
        if ((this.runtimeType instanceof WildcardType) == true) goto L9;
        ImmutableList.Builder r0 = ImmutableList.builder();
        Type[] r1 = getRawType().getGenericInterfaces();
        int r2 = r1.length;
        int r3 = 0;
    L11:
        if (r3 >= r2) goto L14;
        r0.add(resolveSupertype(r1[r3]));
        r3 = r3 + 1;
        goto L11
    L14:
        return r0.build();
    L9:
        return boundsAsInterfaces(((WildcardType) this.runtimeType).getUpperBounds());
    }

    @CheckForNull
    final TypeToken<? super T> getGenericSuperclass() {
        if ((this.runtimeType instanceof TypeVariable) == false) goto L7;
        return boundAsSuperclass(((TypeVariable) this.runtimeType).getBounds()[0]);
    L7:
        if ((this.runtimeType instanceof WildcardType) == true) goto L9;
        Type r0 = getRawType().getGenericSuperclass();
        if (r0 != null) goto L15;
        return null;
    L15:
        return (TypeToken<? super T>) resolveSupertype(r0);
    L9:
        return boundAsSuperclass(((WildcardType) this.runtimeType).getUpperBounds()[0]);
    }

    public final Class<? super T> getRawType() {
        return getRawTypes().iterator().next();
    }

    public final TypeToken<? extends T> getSubtype(Class<?> r5) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if ((this.runtimeType instanceof WildcardType) == false) goto L7;
        return getSubtypeFromLowerBounds(r5, ((WildcardType) this.runtimeType).getLowerBounds());
    L7:
        if (isArray() == true) goto L9;
        Preconditions.checkArgument(getRawType().isAssignableFrom(r5), "%s isn't a subclass of %s", r5, this);
        TypeToken<? extends T> r1 = (TypeToken<? extends T>) m376of(resolveTypeArgsForSubclass(r5));
        Preconditions.checkArgument(r1.isSubtypeOf(this), "%s does not appear to be a subtype of %s", r1, this);
        return r1;
    L9:
        return getArraySubtype(r5);
    }

    public final TypeToken<? super T> getSupertype(Class<? super T> r3) {
        Preconditions.checkArgument(someRawTypeIsSubclassOf(r3), "%s is not a super class of %s", r3, this);
        if ((this.runtimeType instanceof TypeVariable) == false) goto L7;
        return getSupertypeFromUpperBounds(r3, ((TypeVariable) this.runtimeType).getBounds());
    L7:
        if ((this.runtimeType instanceof WildcardType) == false) goto L11;
        return getSupertypeFromUpperBounds(r3, ((WildcardType) this.runtimeType).getUpperBounds());
    L11:
        if (r3.isArray() == false) goto L15;
        return getArraySupertype(r3);
    L15:
        return (TypeToken<? super T>) resolveSupertype(toGenericType(r3).runtimeType);
    }

    public final Type getType() {
        return this.runtimeType;
    }

    public final TypeToken<T>.TypeSet getTypes() {
        return new TypeSet(this);
    }

    public int hashCode() {
        return this.runtimeType.hashCode();
    }

    public final boolean isArray() {
        if (getComponentType() == null) goto L5;
        return true;
    L5:
        return false;
    }

    public final boolean isPrimitive() {
        if ((this.runtimeType instanceof Class) == true) goto L5;
    L7:
        return false;
    L5:
        if (((Class) this.runtimeType).isPrimitive() == false) goto L7;
        return true;
    }

    public final boolean isSubtypeOf(TypeToken<?> r2) {
        return isSubtypeOf(r2.getType());
    }

    public final boolean isSubtypeOf(Type r3) {
        Preconditions.checkNotNull(r3);
        if ((r3 instanceof WildcardType) == false) goto L7;
        return any(((WildcardType) r3).getLowerBounds()).isSupertypeOf(this.runtimeType);
    L7:
        if ((this.runtimeType instanceof WildcardType) == false) goto L11;
        return any(((WildcardType) this.runtimeType).getUpperBounds()).isSubtypeOf(r3);
    L11:
        if ((this.runtimeType instanceof TypeVariable) == false) goto L19;
        if (this.runtimeType.equals(r3) == false) goto L15;
    L16:
        return true;
    L15:
        if (any(((TypeVariable) this.runtimeType).getBounds()).isSubtypeOf(r3) == true) goto L16;
        return false;
    L19:
        if ((this.runtimeType instanceof GenericArrayType) == false) goto L23;
        return m376of(r3).isSupertypeOfArray((GenericArrayType) this.runtimeType);
    L23:
        if ((r3 instanceof Class) == false) goto L27;
        return someRawTypeIsSubclassOf((Class) r3);
    L27:
        if ((r3 instanceof ParameterizedType) == false) goto L31;
        return isSubtypeOfParameterizedType((ParameterizedType) r3);
    L31:
        if ((r3 instanceof GenericArrayType) == true) goto L33;
        return false;
    L33:
        return isSubtypeOfArrayType((GenericArrayType) r3);
    }

    public final boolean isSupertypeOf(TypeToken<?> r2) {
        return r2.isSubtypeOf(getType());
    }

    public final boolean isSupertypeOf(Type r3) {
        return m376of(r3).isSubtypeOf(getType());
    }

    public final Invokable<T, Object> method(final Method r3) {
        Preconditions.checkArgument(someRawTypeIsSubclassOf(r3.getDeclaringClass()), "%s not declared by %s", r3, this);
        return new C08791(this, r3);
    }

    final TypeToken<T> rejectTypeVariables() {
        new C08813(this).visit(new Type[]{this.runtimeType});
        return this;
    }

    public final TypeToken<?> resolveType(Type r2) {
        Preconditions.checkNotNull(r2);
        return m376of(getInvariantTypeResolver().resolveType(r2));
    }

    public String toString() {
        return Types.toString(this.runtimeType);
    }

    public final TypeToken<T> unwrap() {
        if (isWrapper() == true) goto L5;
        return this;
    L5:
        return m375of(Primitives.unwrap((Class) this.runtimeType));
    }

    public final <X> TypeToken<T> where(TypeParameter<X> r4, TypeToken<X> r5) {
        return new SimpleTypeToken(new TypeResolver().where(ImmutableMap.m232of(new TypeResolver.TypeVariableKey(r4.typeVariable), r5.runtimeType)).resolveType(this.runtimeType));
    }

    public final <X> TypeToken<T> where(TypeParameter<X> r2, Class<X> r3) {
        return where(r2, m375of(r3));
    }

    public final TypeToken<T> wrap() {
        if (isPrimitive() == true) goto L5;
        return this;
    L5:
        return m375of(Primitives.wrap((Class) this.runtimeType));
    }

    protected Object writeReplace() {
        return m376of(new TypeResolver().resolveType(this.runtimeType));
    }
}
