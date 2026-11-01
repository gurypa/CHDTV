package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Parameter implements AnnotatedElement {
    private final Object annotatedType;
    private final ImmutableList<Annotation> annotations;
    private final Invokable<?, ?> declaration;
    private final int position;
    private final TypeToken<?> type;

    Parameter(Invokable<?, ?> r2, int r3, TypeToken<?> r4, Annotation[] r5, Object r6) {
        this.declaration = r2;
        this.position = r3;
        this.type = r4;
        this.annotations = ImmutableList.copyOf(r5);
        this.annotatedType = r6;
    }

    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Parameter) == false) goto L10;
        Parameter r0 = (Parameter) r5;
        if (this.position == r0.position) goto L7;
        return false;
    L7:
        if (this.declaration.equals(r0.declaration) == false) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    @Override // java.lang.reflect.AnnotatedElement
    @CheckForNull
    public <A extends Annotation> A getAnnotation(Class<A> r4) {
        Preconditions.checkNotNull(r4);
        UnmodifiableIterator<Annotation> r0 = this.annotations.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        Annotation r1 = r0.next();
        if (r4.isInstance(r1) == false) goto L4;
        return r4.cast(r1);
    L10:
        return null;
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> r2) {
        return (A[]) getDeclaredAnnotationsByType(r2);
    }

    @Override // java.lang.reflect.AnnotatedElement
    @CheckForNull
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> r2) {
        Preconditions.checkNotNull(r2);
        return (A) FluentIterable.from(this.annotations).filter(r2).first().orNull();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return (Annotation[]) this.annotations.toArray(new Annotation[0]);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> r3) {
        return (A[]) ((Annotation[]) FluentIterable.from(this.annotations).filter(r3).toArray(r3));
    }

    public Invokable<?, ?> getDeclaringInvokable() {
        return this.declaration;
    }

    public TypeToken<?> getType() {
        return this.type;
    }

    public int hashCode() {
        return this.position;
    }

    @Override // java.lang.reflect.AnnotatedElement
    public boolean isAnnotationPresent(Class<? extends Annotation> r2) {
        if (getAnnotation(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public String toString() {
        return this.type + " arg" + this.position;
    }
}
