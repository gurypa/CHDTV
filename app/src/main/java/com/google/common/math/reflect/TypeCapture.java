package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class TypeCapture<T> {
    TypeCapture() {
    }

    final Type capture() {
        Type r0 = getClass().getGenericSuperclass();
        Preconditions.checkArgument(r0 instanceof ParameterizedType, "%s isn't parameterized", r0);
        return ((ParameterizedType) r0).getActualTypeArguments()[0];
    }
}
