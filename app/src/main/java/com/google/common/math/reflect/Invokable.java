package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class Invokable<T, R> implements AnnotatedElement, Member {
    private static final boolean ANNOTATED_TYPE_EXISTS = false;
    private final AccessibleObject accessibleObject;
    private final Member member;

    static class ConstructorInvokable<T> extends Invokable<T, T> {
        final Constructor<?> constructor;

        ConstructorInvokable(Constructor<?> r1) {
            super(r1);
            this.constructor = r1;
        }

        private boolean mayNeedHiddenThis() {
            Class<?> r0 = this.constructor.getDeclaringClass();
            if (r0.getEnclosingConstructor() == null) goto L5;
            return true;
        L5:
            Method r1 = r0.getEnclosingMethod();
            if (r1 == null) goto L10;
            return true ^ Modifier.isStatic(r1.getModifiers());
        L10:
            if (r0.getEnclosingClass() != null) goto L12;
        L14:
            return false;
        L12:
            if (Modifier.isStatic(r0.getModifiers()) == true) goto L14;
            return true;
        }

        @Override // com.google.common.reflect.Invokable
        Type[] getGenericExceptionTypes() {
            return this.constructor.getGenericExceptionTypes();
        }

        @Override // com.google.common.reflect.Invokable
        Type[] getGenericParameterTypes() {
            Type[] r0 = this.constructor.getGenericParameterTypes();
            if (r0.length > 0) goto L5;
        L12:
            return r0;
        L5:
            if (mayNeedHiddenThis() == false) goto L12;
            Class<?>[] r1 = this.constructor.getParameterTypes();
            if (r0.length != r1.length) goto L12;
            if (r1[0] != getDeclaringClass().getEnclosingClass()) goto L12;
            return (Type[]) Arrays.copyOfRange(r0, 1, r0.length);
        }

        @Override // com.google.common.reflect.Invokable
        Type getGenericReturnType() {
            Class<? super T> r0 = getDeclaringClass();
            TypeVariable<Class<? super T>>[] r1 = r0.getTypeParameters();
            if (r1.length > 0) goto L5;
            return r0;
        L5:
            return Types.newParameterizedType(r0, r1);
        }

        @Override // com.google.common.reflect.Invokable
        final Annotation[][] getParameterAnnotations() {
            return this.constructor.getParameterAnnotations();
        }

        @Override // com.google.common.reflect.Invokable
        public final TypeVariable<?>[] getTypeParameters() {
            TypeVariable<Class<? super T>>[] r0 = getDeclaringClass().getTypeParameters();
            TypeVariable<Constructor<?>>[] r1 = this.constructor.getTypeParameters();
            TypeVariable<?>[] r2 = new TypeVariable[r0.length + r1.length];
            System.arraycopy(r0, 0, r2, 0, r0.length);
            System.arraycopy(r1, 0, r2, r0.length, r1.length);
            return r2;
        }

        @Override // com.google.common.reflect.Invokable
        final Object invokeInternal(@CheckForNull Object r5, Object[] r6) throws InvocationTargetException, IllegalAccessException {
            return this.constructor.newInstance(r6);
        L4:
            e = move-exception;
            throw new RuntimeException(this.constructor + " failed.", e);
        }

        @Override // com.google.common.reflect.Invokable
        public final boolean isOverridable() {
            return false;
        }

        @Override // com.google.common.reflect.Invokable
        public final boolean isVarArgs() {
            return this.constructor.isVarArgs();
        }
    }

    static class MethodInvokable<T> extends Invokable<T, Object> {
        final Method method;

        MethodInvokable(Method r1) {
            super(r1);
            this.method = r1;
        }

        @Override // com.google.common.reflect.Invokable
        Type[] getGenericExceptionTypes() {
            return this.method.getGenericExceptionTypes();
        }

        @Override // com.google.common.reflect.Invokable
        Type[] getGenericParameterTypes() {
            return this.method.getGenericParameterTypes();
        }

        @Override // com.google.common.reflect.Invokable
        Type getGenericReturnType() {
            return this.method.getGenericReturnType();
        }

        @Override // com.google.common.reflect.Invokable
        final Annotation[][] getParameterAnnotations() {
            return this.method.getParameterAnnotations();
        }

        @Override // com.google.common.reflect.Invokable
        public final TypeVariable<?>[] getTypeParameters() {
            return this.method.getTypeParameters();
        }

        @Override // com.google.common.reflect.Invokable
        @CheckForNull
        final Object invokeInternal(@CheckForNull Object r2, Object[] r3) throws InvocationTargetException, IllegalAccessException {
            return this.method.invoke(r2, r3);
        }

        @Override // com.google.common.reflect.Invokable
        public final boolean isOverridable() {
            if (isFinal() == false) goto L5;
        L11:
            return false;
        L5:
            if (isPrivate() == true) goto L11;
            if (isStatic() == true) goto L11;
            if (Modifier.isFinal(getDeclaringClass().getModifiers()) == true) goto L11;
            return true;
        }

        @Override // com.google.common.reflect.Invokable
        public final boolean isVarArgs() {
            return this.method.isVarArgs();
        }
    }

    static {
        ANNOTATED_TYPE_EXISTS = initAnnotatedTypeExists();
    }

    <M extends AccessibleObject & Member> Invokable(M r2) {
        Preconditions.checkNotNull(r2);
        this.accessibleObject = r2;
        this.member = r2;
    }

    public static <T> Invokable<T, T> from(Constructor<T> r1) {
        return new ConstructorInvokable(r1);
    }

    public static Invokable<?, Object> from(Method r1) {
        return new MethodInvokable(r1);
    }

    private static boolean initAnnotatedTypeExists() {
        Class.forName("java.lang.reflect.AnnotatedType");     // Catch: ClassNotFoundException -> L5
        return true;
    L6:
        return false;
    }

    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Invokable) == false) goto L10;
        Invokable r0 = (Invokable) r5;
        if (getOwnerType().equals(r0.getOwnerType()) == true) goto L7;
        return false;
    L7:
        if (this.member.equals(r0.member) == false) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    @Override // java.lang.reflect.AnnotatedElement
    @CheckForNull
    public final <A extends Annotation> A getAnnotation(Class<A> r2) {
        return (A) this.accessibleObject.getAnnotation(r2);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public final Annotation[] getAnnotations() {
        return this.accessibleObject.getAnnotations();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public final Annotation[] getDeclaredAnnotations() {
        return this.accessibleObject.getDeclaredAnnotations();
    }

    @Override // java.lang.reflect.Member
    public final Class<? super T> getDeclaringClass() {
        return (Class<? super T>) this.member.getDeclaringClass();
    }

    public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
        ImmutableList.Builder r0 = ImmutableList.builder();
        Type[] r1 = getGenericExceptionTypes();
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L6;
        r0.add(TypeToken.m376of(r1[r3]));
        r3 = r3 + 1;
        goto L3
    L6:
        return r0.build();
    }

    abstract Type[] getGenericExceptionTypes();

    abstract Type[] getGenericParameterTypes();

    abstract Type getGenericReturnType();

    @Override // java.lang.reflect.Member
    public final int getModifiers() {
        return this.member.getModifiers();
    }

    @Override // java.lang.reflect.Member
    public final String getName() {
        return this.member.getName();
    }

    public TypeToken<T> getOwnerType() {
        return TypeToken.m375of(getDeclaringClass());
    }

    abstract Annotation[][] getParameterAnnotations();

    public final ImmutableList<Parameter> getParameters() {
        Type[] r0 = getGenericParameterTypes();
        Annotation[][] r1 = getParameterAnnotations();
        Object[] r2 = new Object[r0.length];
        ImmutableList.Builder r3 = ImmutableList.builder();
        int r7 = 0;
    L4:
        if (r7 >= r0.length) goto L7;
        r3.add(new Parameter(this, r7, TypeToken.m376of(r0[r7]), r1[r7], r2[r7]));
        r7 = r7 + 1;
        goto L4
    L7:
        return r3.build();
    }

    public final TypeToken<? extends R> getReturnType() {
        return (TypeToken<? extends R>) TypeToken.m376of(getGenericReturnType());
    }

    public abstract TypeVariable<?>[] getTypeParameters();

    public int hashCode() {
        return this.member.hashCode();
    }

    @CheckForNull
    public final R invoke(@CheckForNull T r2, Object... r3) throws InvocationTargetException, IllegalAccessException {
        return (R) invokeInternal(r2, (Object[]) Preconditions.checkNotNull(r3));
    }

    @CheckForNull
    abstract Object invokeInternal(@CheckForNull Object r1, Object[] r2) throws InvocationTargetException, IllegalAccessException;

    public final boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public final boolean isAccessible() {
        return this.accessibleObject.isAccessible();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public final boolean isAnnotationPresent(Class<? extends Annotation> r2) {
        return this.accessibleObject.isAnnotationPresent(r2);
    }

    public final boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    public final boolean isNative() {
        return Modifier.isNative(getModifiers());
    }

    public abstract boolean isOverridable();

    public final boolean isPackagePrivate() {
        if (isPrivate() == false) goto L5;
    L9:
        return false;
    L5:
        if (isPublic() == true) goto L9;
        if (isProtected() == true) goto L9;
        return true;
    }

    public final boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    public final boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    public final boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    public final boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public final boolean isSynchronized() {
        return Modifier.isSynchronized(getModifiers());
    }

    @Override // java.lang.reflect.Member
    public final boolean isSynthetic() {
        return this.member.isSynthetic();
    }

    final boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    public abstract boolean isVarArgs();

    final boolean isVolatile() {
        return Modifier.isVolatile(getModifiers());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> r4) {
        if (r4.isSupertypeOf(getReturnType()) == false) goto L7;
        return this;
    L7:
        throw new IllegalArgumentException("Invokable is known to return " + getReturnType() + ", not " + r4);
    }

    public final <R1 extends R> Invokable<T, R1> returning(Class<R1> r2) {
        return returning(TypeToken.m375of(r2));
    }

    public final void setAccessible(boolean r2) {
        this.accessibleObject.setAccessible(r2);
    }

    public String toString() {
        return this.member.toString();
    }

    public final boolean trySetAccessible() {
        this.accessibleObject.setAccessible(true);     // Catch: Exception -> L4
        return true;
    L5:
        return false;
    }
}
