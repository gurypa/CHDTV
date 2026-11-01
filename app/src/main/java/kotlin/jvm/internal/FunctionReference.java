package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

/* JADX INFO: loaded from: classes.dex */
public class FunctionReference extends CallableReference implements FunctionBase, KFunction {
    private final int arity;
    private final int flags;

    public FunctionReference(int r8) {
        this(r8, NO_RECEIVER, null, null, null, 0);
    }

    public FunctionReference(int r8, Object r9) {
        this(r8, r9, null, null, null, 0);
    }

    public FunctionReference(int r9, Object r10, Class r11, String r12, String r13, int r14) {
        boolean r1 = true;
        if ((r14 & 1) == 1) goto L6;
        r1 = false;
    L6:
        super(r10, r11, r12, r13, r1);
        this.arity = r9;
        this.flags = r14 >> 1;
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected KCallable computeReflected() {
        return Reflection.function(this);
    }

    public boolean equals(Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof FunctionReference) == false) goto L23;
        FunctionReference r1 = (FunctionReference) r6;
        if (getName().equals(r1.getName()) == false) goto L21;
        if (getSignature().equals(r1.getSignature()) == false) goto L21;
        if (this.flags != r1.flags) goto L21;
        if (this.arity != r1.arity) goto L21;
        if (Intrinsics.areEqual(getBoundReceiver(), r1.getBoundReceiver()) == false) goto L21;
        if (Intrinsics.areEqual(getOwner(), r1.getOwner()) == false) goto L21;
        return true;
    L21:
        return false;
    L23:
        if ((r6 instanceof KFunction) == true) goto L25;
        return false;
    L25:
        return r6.equals(compute());
    }

    @Override // kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected /* bridge */ /* synthetic */ KCallable getReflected() {
        return getReflected();
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected KFunction getReflected() {
        return (KFunction) super.getReflected();
    }

    public int hashCode() {
        if (getOwner() != null) goto L5;
        int r0 = 0;
    L7:
        return ((r0 + getName().hashCode()) * 31) + getSignature().hashCode();
    L5:
        r0 = getOwner().hashCode() * 31;
        goto L7
    }

    @Override // kotlin.reflect.KFunction
    public boolean isExternal() {
        return getReflected().isExternal();
    }

    @Override // kotlin.reflect.KFunction
    public boolean isInfix() {
        return getReflected().isInfix();
    }

    @Override // kotlin.reflect.KFunction
    public boolean isInline() {
        return getReflected().isInline();
    }

    @Override // kotlin.reflect.KFunction
    public boolean isOperator() {
        return getReflected().isOperator();
    }

    @Override // kotlin.jvm.internal.CallableReference, kotlin.reflect.KCallable
    public boolean isSuspend() {
        return getReflected().isSuspend();
    }

    public String toString() {
        KCallable r0 = compute();
        if (r0 == this) goto L7;
        return r0.toString();
    L7:
        if ("<init>".equals(getName()) == false) goto L10;
        return "constructor (Kotlin reflection is not available)";
    L10:
        return "function " + getName() + " (Kotlin reflection is not available)";
    }
}
