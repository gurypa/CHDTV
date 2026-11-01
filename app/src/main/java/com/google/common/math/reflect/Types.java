package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.CheckForNull;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Types {
    private static final Joiner COMMA_JOINER = null;

    private static abstract enum ClassOwnership extends Enum<ClassOwnership> {
        private static final /* synthetic */ ClassOwnership[] $VALUES = null;
        static final ClassOwnership JVM_BEHAVIOR = null;
        public static final ClassOwnership LOCAL_CLASS_HAS_NO_OWNER = null;
        public static final ClassOwnership OWNED_BY_ENCLOSING_CLASS = null;

        private static /* synthetic */ ClassOwnership[] $values() {
            return new ClassOwnership[]{OWNED_BY_ENCLOSING_CLASS, LOCAL_CLASS_HAS_NO_OWNER};
        }

        static {
            final String r1 = "OWNED_BY_ENCLOSING_CLASS";
            final int r2 = 0;
            OWNED_BY_ENCLOSING_CLASS = new C08901(r1, r2);
            final String r3 = "LOCAL_CLASS_HAS_NO_OWNER";
            final int r4 = 1;
            LOCAL_CLASS_HAS_NO_OWNER = new C08912(r3, r4);
            $VALUES = $values();
            JVM_BEHAVIOR = detectJvmBehavior();
        }

        private ClassOwnership(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ ClassOwnership(String r1, int r2, C08891 r3) {
            this(r1, r2);
        }

        private static ClassOwnership detectJvmBehavior() {
            ParameterizedType r1 = (ParameterizedType) Objects.requireNonNull((ParameterizedType) new C08923().getClass().getGenericSuperclass());
            ClassOwnership[] r2 = values();
            int r3 = r2.length;
            int r4 = 0;
        L3:
            if (r4 >= r3) goto L9;
            ClassOwnership r5 = r2[r4];
            if (r5.getOwnerType(C1LocalClass.class) == r1.getOwnerType()) goto L6;
            r4 = r4 + 1;
            goto L3
        L6:
            return r5;
        L9:
            throw new AssertionError();
        }

        public static ClassOwnership valueOf(String r1) {
            return (ClassOwnership) Enum.valueOf(ClassOwnership.class, r1);
        }

        public static ClassOwnership[] values() {
            return (ClassOwnership[]) $VALUES.clone();
        }

        @CheckForNull
        abstract Class<?> getOwnerType(Class<?> r1);
    }

    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        GenericArrayTypeImpl(Type r2) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(r2);
        }

        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof GenericArrayType) == true) goto L5;
            return false;
        L5:
            return com.google.common.base.Objects.equal(getGenericComponentType(), ((GenericArrayType) r4).getGenericComponentType());
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return Types.toString(this.componentType) + "[]";
        }
    }

    static abstract enum JavaVersion extends Enum<JavaVersion> {
        private static final /* synthetic */ JavaVersion[] $VALUES = null;
        static final JavaVersion CURRENT = null;
        public static final JavaVersion JAVA6 = null;
        public static final JavaVersion JAVA7 = null;
        public static final JavaVersion JAVA8 = null;
        public static final JavaVersion JAVA9 = null;

        private static /* synthetic */ JavaVersion[] $values() {
            return new JavaVersion[]{JAVA6, JAVA7, JAVA8, JAVA9};
        }

        static {
            final String r1 = "JAVA6";
            final int r2 = 0;
            JAVA6 = new C08931(r1, r2);
            final String r3 = "JAVA7";
            final int r4 = 1;
            JAVA7 = new C08942(r3, r4);
            final String r5 = "JAVA8";
            final int r6 = 2;
            JAVA8 = new C08953(r5, r6);
            final String r7 = "JAVA9";
            final int r8 = 3;
            JAVA9 = new C08964(r7, r8);
            $VALUES = $values();
            if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class) == false) goto L9;
            if (new C08975().capture().toString().contains("java.util.Map.java.util.Map") == false) goto L7;
            CURRENT = JAVA8;
            return;
        L7:
            CURRENT = JAVA9;
            return;
        L9:
            if ((new C08986().capture() instanceof Class) == false) goto L11;
            CURRENT = JAVA7;
            return;
        L11:
            CURRENT = JAVA6;
        }

        private JavaVersion(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ JavaVersion(String r1, int r2, C08891 r3) {
            this(r1, r2);
        }

        public static JavaVersion valueOf(String r1) {
            return (JavaVersion) Enum.valueOf(JavaVersion.class, r1);
        }

        public static JavaVersion[] values() {
            return (JavaVersion[]) $VALUES.clone();
        }

        boolean jdkTypeDuplicatesOwnerName() {
            return true;
        }

        abstract Type newArrayType(Type r1);

        String typeName(Type r2) {
            return Types.toString(r2);
        }

        final ImmutableList<Type> usedInGenericType(Type[] r6) {
            ImmutableList.Builder r0 = ImmutableList.builder();
            int r1 = r6.length;
            int r2 = 0;
        L3:
            if (r2 >= r1) goto L6;
            r0.add(usedInGenericType(r6[r2]));
            r2 = r2 + 1;
            goto L3
        L6:
            return r0.build();
        }

        abstract Type usedInGenericType(Type r1);
    }

    static final class NativeTypeVariableEquals<X> {
        static final boolean NATIVE_TYPE_VARIABLE_ONLY = false;

        static {
            NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
        }

        NativeTypeVariableEquals() {
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> argumentsList;

        @CheckForNull
        private final Type ownerType;
        private final Class<?> rawType;

        ParameterizedTypeImpl(@CheckForNull Type r3, Class<?> r4, Type[] r5) {
            Preconditions.checkNotNull(r4);
            if (r5.length != r4.getTypeParameters().length) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0);
            Types.access$200(r5, "type parameter");
            this.ownerType = r3;
            this.rawType = r4;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(r5);
            return;
        L5:
            r0 = false;
            goto L6
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof ParameterizedType) == true) goto L5;
            return false;
        L5:
            ParameterizedType r0 = (ParameterizedType) r5;
            if (getRawType().equals(r0.getRawType()) == true) goto L8;
            return false;
        L8:
            if (com.google.common.base.Objects.equal(getOwnerType(), r0.getOwnerType()) == true) goto L10;
            return false;
        L10:
            if (Arrays.equals(getActualTypeArguments(), r0.getActualTypeArguments()) == false) goto L16;
            return true;
        L16:
            return false;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return Types.access$300(this.argumentsList);
        }

        @Override // java.lang.reflect.ParameterizedType
        @CheckForNull
        public Type getOwnerType() {
            return this.ownerType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        public int hashCode() {
            if (this.ownerType != null) goto L5;
            int r0 = 0;
        L7:
            return (r0 ^ this.argumentsList.hashCode()) ^ this.rawType.hashCode();
        L5:
            r0 = this.ownerType.hashCode();
            goto L7
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder();
            if (this.ownerType != null) goto L5;
        L7:
            StringBuilder r1 = r0.append(this.rawType.getName()).append(Typography.less);
            Joiner r2 = Types.access$400();
            ImmutableList<Type> r3 = this.argumentsList;
            final JavaVersion r4 = JavaVersion.CURRENT;
            Objects.requireNonNull(r4);
            return r1.append(r2.join(Iterables.transform(r3, new Types$ParameterizedTypeImpl$$ExternalSyntheticLambda0(r4)))).append(Typography.greater).toString();
        L5:
            if (JavaVersion.CURRENT.jdkTypeDuplicatesOwnerName() == false) goto L7;
            r0.append(JavaVersion.CURRENT.typeName(this.ownerType)).append('.');
            goto L7
        }
    }

    private static final class TypeVariableImpl<D extends GenericDeclaration> {
        private final ImmutableList<Type> bounds;
        private final D genericDeclaration;
        private final String name;

        TypeVariableImpl(D r2, String r3, Type[] r4) {
            Types.access$200(r4, "bound for type variable");
            this.genericDeclaration = (D) Preconditions.checkNotNull(r2);
            this.name = (String) Preconditions.checkNotNull(r3);
            this.bounds = ImmutableList.copyOf(r4);
        }

        public boolean equals(@CheckForNull Object r7) {
            if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY == false) goto L20;
            if (r7 != null) goto L6;
        L18:
            return false;
        L6:
            if (Proxy.isProxyClass(r7.getClass()) == false) goto L18;
            if ((Proxy.getInvocationHandler(r7) instanceof TypeVariableInvocationHandler) == false) goto L18;
            TypeVariableImpl r3 = TypeVariableInvocationHandler.access$500((TypeVariableInvocationHandler) Proxy.getInvocationHandler(r7));
            if (this.name.equals(r3.getName()) == false) goto L17;
            if (this.genericDeclaration.equals(r3.getGenericDeclaration()) == false) goto L17;
            if (this.bounds.equals(r3.bounds) == false) goto L17;
            return true;
        L17:
            return false;
        L20:
            if ((r7 instanceof TypeVariable) == false) goto L28;
            TypeVariable r0 = (TypeVariable) r7;
            if (this.name.equals(r0.getName()) == false) goto L27;
            if (this.genericDeclaration.equals(r0.getGenericDeclaration()) == false) goto L27;
            return true;
        L27:
            return false;
        L28:
            return false;
        }

        public Type[] getBounds() {
            return Types.access$300(this.bounds);
        }

        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }

        public String getName() {
            return this.name;
        }

        public String getTypeName() {
            return this.name;
        }

        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }

        public String toString() {
            return this.name;
        }
    }

    private static final class TypeVariableInvocationHandler implements InvocationHandler {
        private static final ImmutableMap<String, Method> typeVariableMethods = null;
        private final TypeVariableImpl<?> typeVariableImpl;

        static {
            ImmutableMap.Builder r0 = ImmutableMap.builder();
            Method[] r1 = TypeVariableImpl.class.getMethods();
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L12;
            Method r4 = r1[r3];
            if (r4.getDeclaringClass().equals(TypeVariableImpl.class) == false) goto L11;
            r4.setAccessible(true);     // Catch: AccessControlException -> L9
        L10:
            r0.put(r4.getName(), r4);
        L11:
            r3 = r3 + 1;
            goto L3
        L12:
            typeVariableMethods = r0.buildKeepingLast();
        }

        TypeVariableInvocationHandler(TypeVariableImpl<?> r1) {
            this.typeVariableImpl = r1;
        }

        static /* synthetic */ TypeVariableImpl access$500(TypeVariableInvocationHandler r1) {
            return r1.typeVariableImpl;
        }

        @Override // java.lang.reflect.InvocationHandler
        @CheckForNull
        public Object invoke(Object r5, Method r6, @CheckForNull Object[] r7) throws Throwable {
            String r0 = r6.getName();
            Method r1 = typeVariableMethods.get(r0);
            if (r1 == null) goto L10;
            return r1.invoke(this.typeVariableImpl, r7);
        L6:
            e = move-exception;
            throw e.getCause();
        L10:
            throw new UnsupportedOperationException(r0);
        }
    }

    static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;

        WildcardTypeImpl(Type[] r2, Type[] r3) {
            Types.access$200(r2, "lower bound for wildcard");
            Types.access$200(r3, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(r2);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(r3);
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof WildcardType) == false) goto L11;
            WildcardType r0 = (WildcardType) r5;
            if (this.lowerBounds.equals(Arrays.asList(r0.getLowerBounds())) == true) goto L7;
            return false;
        L7:
            if (this.upperBounds.equals(Arrays.asList(r0.getUpperBounds())) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return Types.access$300(this.lowerBounds);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return Types.access$300(this.upperBounds);
        }

        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder("?");
            UnmodifiableIterator<Type> r1 = this.lowerBounds.iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            Type r2 = r1.next();
            r0.append(" super ").append(JavaVersion.CURRENT.typeName(r2));
            goto L4
        L6:
            Iterator r3 = Types.access$600(this.upperBounds).iterator();
        L8:
            if (r3.hasNext() == false) goto L11;
            Type r4 = (Type) r3.next();
            r0.append(" extends ").append(JavaVersion.CURRENT.typeName(r4));
            goto L8
        L11:
            return r0.toString();
        }
    }

    static {
        COMMA_JOINER = Joiner.m184on(", ").useForNull("null");
    }

    private Types() {
    }

    static /* synthetic */ Type access$100(Type[] r1) {
        return subtypeOfComponentType(r1);
    }

    static /* synthetic */ void access$200(Type[] r0, String r1) {
        disallowPrimitiveType(r0, r1);
    }

    static /* synthetic */ Type[] access$300(Collection r1) {
        return toArray(r1);
    }

    static /* synthetic */ Joiner access$400() {
        return COMMA_JOINER;
    }

    static /* synthetic */ Iterable access$600(Iterable r1) {
        return filterUpperBounds(r1);
    }

    private static void disallowPrimitiveType(Type[] r6, String r7) {
        int r0 = r6.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        Type r2 = r6[r1];
        if ((r2 instanceof Class) == false) goto L7;
        Class r3 = (Class) r2;
        Preconditions.checkArgument(!r3.isPrimitive(), "Primitive type '%s' used as %s", r3, r7);
    L7:
        r1 = r1 + 1;
        goto L3
    }

    private static Iterable<Type> filterUpperBounds(Iterable<Type> r1) {
        return Iterables.filter(r1, Predicates.not(Predicates.equalTo(Object.class)));
    }

    static Class<?> getArrayClass(Class<?> r1) {
        return Array.newInstance(r1, 0).getClass();
    }

    @CheckForNull
    static Type getComponentType(Type r4) {
        Preconditions.checkNotNull(r4);
        final AtomicReference r0 = new AtomicReference();
        new C08891(r0).visit(new Type[]{r4});
        return (Type) r0.get();
    }

    static Type newArrayType(Type r6) {
        if ((r6 instanceof WildcardType) == false) goto L19;
        WildcardType r0 = (WildcardType) r6;
        Type[] r1 = r0.getLowerBounds();
        boolean r4 = true;
        if (r1.length > 1) goto L7;
        boolean r2 = true;
    L8:
        Preconditions.checkArgument(r2, "Wildcard cannot have more than one lower bounds.");
        if (r1.length == 1) goto L11;
        Type[] r3 = r0.getUpperBounds();
        if (r3.length == 1) goto L16;
        r4 = false;
    L16:
        Preconditions.checkArgument(r4, "Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(r3[0]));
    L11:
        return supertypeOf(newArrayType(r1[0]));
    L7:
        r2 = false;
        goto L8
    L19:
        return JavaVersion.CURRENT.newArrayType(r6);
    }

    static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D r3, String r4, Type... r5) {
        if (r5.length != 0) goto L5;
        Type[] r0 = {Object.class};
    L7:
        return newTypeVariableImpl(r3, r4, r0);
    L5:
        r0 = r5;
        goto L7
    }

    static ParameterizedType newParameterizedType(Class<?> r2, Type... r3) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(r2), r2, r3);
    }

    static ParameterizedType newParameterizedTypeWithOwner(@CheckForNull Type r2, Class<?> r3, Type... r4) {
        if (r2 == null) goto L4;
        Preconditions.checkNotNull(r4);
        if (r3.getEnclosingClass() == null) goto L8;
        boolean r0 = true;
    L9:
        Preconditions.checkArgument(r0, "Owner type for unenclosed %s", r3);
        return new ParameterizedTypeImpl(r2, r3, r4);
    L8:
        r0 = false;
        goto L9
    L4:
        return newParameterizedType(r3, r4);
    }

    private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D r3, String r4, Type[] r5) {
        return (TypeVariable) Reflection.newProxy(TypeVariable.class, new TypeVariableInvocationHandler(new TypeVariableImpl(r3, r4, r5)));
    }

    static WildcardType subtypeOf(Type r4) {
        return new WildcardTypeImpl(new Type[0], new Type[]{r4});
    }

    @CheckForNull
    private static Type subtypeOfComponentType(Type[] r4) {
        int r0 = r4.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L14;
        Type r3 = getComponentType(r4[r1]);
        if (r3 != null) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        if ((r3 instanceof Class) == false) goto L12;
        Class r2 = (Class) r3;
        if (r2.isPrimitive() == false) goto L12;
        return r2;
    L12:
        return subtypeOf(r3);
    L14:
        return null;
    }

    static WildcardType supertypeOf(Type r5) {
        return new WildcardTypeImpl(new Type[]{r5}, new Type[]{Object.class});
    }

    private static Type[] toArray(Collection<Type> r1) {
        return (Type[]) r1.toArray(new Type[0]);
    }

    static String toString(Type r1) {
        if ((r1 instanceof Class) == false) goto L6;
        return ((Class) r1).getName();
    L6:
        return r1.toString();
    }
}
