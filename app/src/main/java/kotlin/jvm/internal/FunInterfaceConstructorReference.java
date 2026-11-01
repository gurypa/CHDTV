package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

/* JADX INFO: loaded from: classes.dex */
public class FunInterfaceConstructorReference extends FunctionReference implements Serializable {
    private final Class funInterface;

    public FunInterfaceConstructorReference(Class r2) {
        super(1);
        this.funInterface = r2;
    }

    @Override // kotlin.jvm.internal.FunctionReference
    public boolean equals(Object r4) {
        if (this != r4) goto L6;
        return true;
    L6:
        if ((r4 instanceof FunInterfaceConstructorReference) == true) goto L10;
        return false;
    L10:
        return this.funInterface.equals(((FunInterfaceConstructorReference) r4).funInterface);
    }

    @Override // kotlin.jvm.internal.FunctionReference, kotlin.jvm.internal.CallableReference
    protected /* bridge */ /* synthetic */ KCallable getReflected() {
        return getReflected();
    }

    @Override // kotlin.jvm.internal.FunctionReference, kotlin.jvm.internal.CallableReference
    protected KFunction getReflected() {
        throw new UnsupportedOperationException("Functional interface constructor does not support reflection");
    }

    @Override // kotlin.jvm.internal.FunctionReference
    public int hashCode() {
        return this.funInterface.hashCode();
    }

    @Override // kotlin.jvm.internal.FunctionReference
    public String toString() {
        return "fun interface " + this.funInterface.getName();
    }
}
