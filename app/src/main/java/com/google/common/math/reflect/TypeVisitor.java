package com.google.common.reflect;

import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class TypeVisitor {
    private final Set<Type> visited;

    TypeVisitor() {
        this.visited = Sets.newHashSet();
    }

    public final void visit(Type... r6) {
        int r0 = r6.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L35;
        Type r2 = r6[r1];
        if (r2 == null) goto L34;
        if (this.visited.add(r2) == false) goto L34;
    L30:
        th = move-exception;
        if (0 != 0) goto L33;
        this.visited.remove(r2);
    L33:
        throw th;
    L11:
        if ((r2 instanceof TypeVariable) == false) goto L14;
        visitTypeVariable((TypeVariable) r2);     // Catch: Throwable -> L30
    L26:
        if (1 != 0) goto L34;
        this.visited.remove(r2);
        goto L34
    L14:
        if ((r2 instanceof WildcardType) == false) goto L17;
        visitWildcardType((WildcardType) r2);     // Catch: Throwable -> L30
        goto L26
    L17:
        if ((r2 instanceof ParameterizedType) == false) goto L20;
        visitParameterizedType((ParameterizedType) r2);     // Catch: Throwable -> L30
        goto L26
    L20:
        if ((r2 instanceof Class) == false) goto L23;
        visitClass((Class) r2);     // Catch: Throwable -> L30
        goto L26
    L23:
        if ((r2 instanceof GenericArrayType) == false) goto L29;
        visitGenericArrayType((GenericArrayType) r2);     // Catch: Throwable -> L30
        goto L26
    L29:
        throw new AssertionError("Unknown type: " + r2);     // Catch: Throwable -> L30
    L34:
        r1 = r1 + 1;
        goto L3
    }

    void visitClass(Class<?> r1) {
    }

    void visitGenericArrayType(GenericArrayType r1) {
    }

    void visitParameterizedType(ParameterizedType r1) {
    }

    void visitTypeVariable(TypeVariable<?> r1) {
    }

    void visitWildcardType(WildcardType r1) {
    }
}
