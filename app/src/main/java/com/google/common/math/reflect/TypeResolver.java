package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.CheckForNull;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class TypeResolver {
    private final TypeTable typeTable;

    private static final class TypeMappingIntrospector extends TypeVisitor {
        private final Map<TypeVariableKey, Type> mappings;

        private TypeMappingIntrospector() {
            this.mappings = Maps.newHashMap();
        }

        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(Type r3) {
            Preconditions.checkNotNull(r3);
            TypeMappingIntrospector r0 = new TypeMappingIntrospector();
            r0.visit(new Type[]{r3});
            return ImmutableMap.copyOf(r0.mappings);
        }

        private void map(TypeVariableKey r5, Type r6) {
            if (this.mappings.containsKey(r5) == false) goto L5;
            return;
        L5:
            Type r0 = r6;
        L6:
            if (r0 == null) goto L14;
            if (r5.equalsType(r0) == true) goto L9;
            r0 = this.mappings.get(TypeVariableKey.forLookup(r0));
            goto L6
        L9:
            Type r1 = r6;
        L10:
            if (r1 == null) goto L12;
            r1 = this.mappings.remove(TypeVariableKey.forLookup(r1));
            goto L10
        L12:
            return;
        L14:
            this.mappings.put(r5, r6);
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitClass(Class<?> r4) {
            visit(new Type[]{r4.getGenericSuperclass()});
            visit(r4.getGenericInterfaces());
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitParameterizedType(ParameterizedType r9) {
            Class r0 = (Class) r9.getRawType();
            TypeVariable[] r1 = r0.getTypeParameters();
            Type[] r2 = r9.getActualTypeArguments();
            if (r1.length != r2.length) goto L5;
            boolean r3 = true;
        L6:
            Preconditions.checkState(r3);
            int r4 = 0;
        L8:
            if (r4 >= r1.length) goto L10;
            map(new TypeVariableKey(r1[r4]), r2[r4]);
            r4 = r4 + 1;
            goto L8
        L10:
            visit(new Type[]{r0});
            visit(new Type[]{r9.getOwnerType()});
            return;
        L5:
            r3 = false;
            goto L6
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitTypeVariable(TypeVariable<?> r2) {
            visit(r2.getBounds());
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitWildcardType(WildcardType r2) {
            visit(r2.getUpperBounds());
        }
    }

    private static class TypeTable {
        private final ImmutableMap<TypeVariableKey, Type> map;

        TypeTable() {
            this.map = ImmutableMap.m231of();
        }

        private TypeTable(ImmutableMap<TypeVariableKey, Type> r1) {
            this.map = r1;
        }

        final Type resolve(final TypeVariable<?> r4) {
            return resolveInternal(r4, new C08771(this, r4, this));
        }

        Type resolveInternal(TypeVariable<?> r6, TypeTable r7) {
            Type r0 = this.map.get(new TypeVariableKey(r6));
            C08761 r1 = null;
            if (r0 != null) goto L15;
            Type[] r2 = r6.getBounds();
            if (r2.length != 0) goto L7;
            return r6;
        L7:
            Type[] r3 = TypeResolver.access$300(new TypeResolver(r7, r1), r2);
            if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY == false) goto L13;
            if (Arrays.equals(r2, r3) == false) goto L13;
            return r6;
        L13:
            return Types.newArtificialTypeVariable(r6.getGenericDeclaration(), r6.getName(), r3);
        L15:
            return new TypeResolver(r7, r1).resolveType(r0);
        }

        final TypeTable where(Map<TypeVariableKey, ? extends Type> r8) {
            ImmutableMap.Builder r0 = ImmutableMap.builder();
            r0.putAll(this.map);
            Iterator<Map.Entry<TypeVariableKey, ? extends Type>> r1 = r8.entrySet().iterator();
        L4:
            if (r1.hasNext() == false) goto L7;
            Map.Entry<TypeVariableKey, ? extends Type> r2 = r1.next();
            TypeVariableKey r3 = r2.getKey();
            Type r4 = r2.getValue();
            Preconditions.checkArgument(!r3.equalsType(r4), "Type variable %s bound to itself", r3);
            r0.put(r3, r4);
            goto L4
        L7:
            return new TypeTable(r0.buildOrThrow());
        }
    }

    static final class TypeVariableKey {
        private final TypeVariable<?> var;

        TypeVariableKey(TypeVariable<?> r2) {
            this.var = (TypeVariable) Preconditions.checkNotNull(r2);
        }

        private boolean equalsTypeVariable(TypeVariable<?> r3) {
            if (this.var.getGenericDeclaration().equals(r3.getGenericDeclaration()) == true) goto L5;
        L7:
            return false;
        L5:
            if (this.var.getName().equals(r3.getName()) == false) goto L7;
            return true;
        }

        @CheckForNull
        static TypeVariableKey forLookup(Type r2) {
            if ((r2 instanceof TypeVariable) == true) goto L5;
            return null;
        L5:
            return new TypeVariableKey((TypeVariable) r2);
        }

        public boolean equals(@CheckForNull Object r3) {
            if ((r3 instanceof TypeVariableKey) == true) goto L5;
            return false;
        L5:
            return equalsTypeVariable(((TypeVariableKey) r3).var);
        }

        boolean equalsType(Type r2) {
            if ((r2 instanceof TypeVariable) == true) goto L5;
            return false;
        L5:
            return equalsTypeVariable((TypeVariable) r2);
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.var.getGenericDeclaration(), this.var.getName()});
        }

        public String toString() {
            return this.var.toString();
        }
    }

    private static class WildcardCapturer {
        static final WildcardCapturer INSTANCE = null;

        /* JADX INFO: renamed from: id */
        private final AtomicInteger f167id;

        static {
            INSTANCE = new WildcardCapturer();
        }

        private WildcardCapturer() {
            this(new AtomicInteger());
        }

        private WildcardCapturer(AtomicInteger r1) {
            this.f167id = r1;
        }

        /* synthetic */ WildcardCapturer(AtomicInteger r1, C08761 r2) {
            this(r1);
        }

        @CheckForNull
        private Type captureNullable(@CheckForNull Type r2) {
            if (r2 != null) goto L6;
            return null;
        L6:
            return capture(r2);
        }

        private WildcardCapturer forTypeVariable(final TypeVariable<?> r3) {
            final AtomicInteger r1 = this.f167id;
            return new C08781(this, r1, r3);
        }

        private WildcardCapturer notForTypeVariable() {
            return new WildcardCapturer(this.f167id);
        }

        final Type capture(Type r8) {
            Preconditions.checkNotNull(r8);
            if ((r8 instanceof Class) == false) goto L6;
            return r8;
        L6:
            if ((r8 instanceof TypeVariable) == false) goto L9;
            return r8;
        L9:
            if ((r8 instanceof GenericArrayType) == false) goto L13;
            return Types.newArrayType(notForTypeVariable().capture(((GenericArrayType) r8).getGenericComponentType()));
        L13:
            if ((r8 instanceof ParameterizedType) == false) goto L21;
            ParameterizedType r0 = (ParameterizedType) r8;
            Class r1 = (Class) r0.getRawType();
            TypeVariable<?>[] r2 = r1.getTypeParameters();
            Type[] r3 = r0.getActualTypeArguments();
            int r4 = 0;
        L16:
            if (r4 >= r3.length) goto L19;
            r3[r4] = forTypeVariable(r2[r4]).capture(r3[r4]);
            r4 = r4 + 1;
            goto L16
        L19:
            return Types.newParameterizedTypeWithOwner(notForTypeVariable().captureNullable(r0.getOwnerType()), r1, r3);
        L21:
            if ((r8 instanceof WildcardType) == false) goto L28;
            WildcardType r5 = (WildcardType) r8;
            if (r5.getLowerBounds().length == 0) goto L25;
            return r8;
        L25:
            return captureAsTypeVariable(r5.getUpperBounds());
        L28:
            throw new AssertionError("must have been one of the known types");
        }

        TypeVariable<?> captureAsTypeVariable(Type[] r3) {
            return Types.newArtificialTypeVariable(WildcardCapturer.class, "capture#" + this.f167id.incrementAndGet() + "-of ? extends " + Joiner.m183on(Typography.amp).join(r3), r3);
        }
    }

    public TypeResolver() {
        this.typeTable = new TypeTable();
    }

    private TypeResolver(TypeTable r1) {
        this.typeTable = r1;
    }

    /* synthetic */ TypeResolver(TypeTable r1, C08761 r2) {
        this(r1);
    }

    static /* synthetic */ void access$000(Map r0, Type r1, Type r2) {
        populateTypeMappings(r0, r1, r2);
    }

    static /* synthetic */ Object access$100(Class r1, Object r2) {
        return expectArgument(r1, r2);
    }

    static /* synthetic */ Type[] access$300(TypeResolver r1, Type[] r2) {
        return r1.resolveTypes(r2);
    }

    static TypeResolver covariantly(Type r2) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(r2));
    }

    private static <T> T expectArgument(Class<T> r4, Object r5) {
        return r4.cast(r5);
    L6:
        throw new IllegalArgumentException(r5 + " is not a " + r4.getSimpleName());
    }

    static TypeResolver invariantly(Type r3) {
        Type r0 = WildcardCapturer.INSTANCE.capture(r3);
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(r0));
    }

    private static void populateTypeMappings(final Map<TypeVariableKey, Type> r3, Type r4, final Type r5) {
        if (r4.equals(r5) == false) goto L5;
        return;
    L5:
        new C08761(r3, r5).visit(new Type[]{r4});
    }

    private Type resolveGenericArrayType(GenericArrayType r4) {
        return Types.newArrayType(resolveType(r4.getGenericComponentType()));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType r7) {
        Type r0 = r7.getOwnerType();
        if (r0 != null) goto L5;
        Type r1 = null;
    L6:
        Type r2 = resolveType(r7.getRawType());
        Class r5 = (Class) r2;
        return Types.newParameterizedTypeWithOwner(r1, r5, resolveTypes(r7.getActualTypeArguments()));
    L5:
        r1 = resolveType(r0);
        goto L6
    }

    private Type[] resolveTypes(Type[] r4) {
        Type[] r0 = new Type[r4.length];
        int r1 = 0;
    L4:
        if (r1 >= r4.length) goto L6;
        r0[r1] = resolveType(r4[r1]);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private WildcardType resolveWildcardType(WildcardType r6) {
        Type[] r0 = r6.getLowerBounds();
        Type[] r1 = r6.getUpperBounds();
        return new Types.WildcardTypeImpl(resolveTypes(r0), resolveTypes(r1));
    }

    public Type resolveType(Type r3) {
        Preconditions.checkNotNull(r3);
        if ((r3 instanceof TypeVariable) == false) goto L7;
        return this.typeTable.resolve((TypeVariable) r3);
    L7:
        if ((r3 instanceof ParameterizedType) == false) goto L11;
        return resolveParameterizedType((ParameterizedType) r3);
    L11:
        if ((r3 instanceof GenericArrayType) == false) goto L15;
        return resolveGenericArrayType((GenericArrayType) r3);
    L15:
        if ((r3 instanceof WildcardType) == true) goto L17;
        return r3;
    L17:
        return resolveWildcardType((WildcardType) r3);
    }

    Type[] resolveTypesInPlace(Type[] r3) {
        int r0 = 0;
    L4:
        if (r0 >= r3.length) goto L6;
        r3[r0] = resolveType(r3[r0]);
        r0 = r0 + 1;
        goto L4
    L6:
        return r3;
    }

    public TypeResolver where(Type r4, Type r5) {
        HashMap r0 = Maps.newHashMap();
        populateTypeMappings(r0, (Type) Preconditions.checkNotNull(r4), (Type) Preconditions.checkNotNull(r5));
        return where(r0);
    }

    TypeResolver where(Map<TypeVariableKey, ? extends Type> r3) {
        return new TypeResolver(this.typeTable.where(r3));
    }
}
