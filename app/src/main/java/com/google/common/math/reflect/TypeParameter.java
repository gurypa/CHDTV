package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class TypeParameter<T> extends TypeCapture<T> {
    final TypeVariable<?> typeVariable;

    protected TypeParameter() {
        Type r0 = capture();
        Preconditions.checkArgument(r0 instanceof TypeVariable, "%s should be a type variable.", r0);
        this.typeVariable = (TypeVariable) r0;
    }

    public final boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof TypeParameter) == true) goto L5;
        return false;
    L5:
        return this.typeVariable.equals(((TypeParameter) r4).typeVariable);
    }

    public final int hashCode() {
        return this.typeVariable.hashCode();
    }

    public String toString() {
        return this.typeVariable.toString();
    }
}
