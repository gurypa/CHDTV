package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.reflect.KDeclarationContainer;

/* JADX INFO: loaded from: classes.dex */
public class AdaptedFunctionReference implements FunctionBase, Serializable {
    private final int arity;
    private final int flags;
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    protected final Object receiver;
    private final String signature;

    public AdaptedFunctionReference(int r8, Class r9, String r10, String r11, int r12) {
        this(r8, CallableReference.NO_RECEIVER, r9, r10, r11, r12);
    }

    public AdaptedFunctionReference(int r3, Object r4, Class r5, String r6, String r7, int r8) {
        this.receiver = r4;
        this.owner = r5;
        this.name = r6;
        this.signature = r7;
        boolean r1 = true;
        if ((r8 & 1) == 1) goto L6;
        r1 = false;
    L6:
        this.isTopLevel = r1;
        this.arity = r3;
        this.flags = r8 >> 1;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof AdaptedFunctionReference) == true) goto L8;
        return false;
    L8:
        AdaptedFunctionReference r1 = (AdaptedFunctionReference) r6;
        if (this.isTopLevel != r1.isTopLevel) goto L24;
        if (this.arity != r1.arity) goto L24;
        if (this.flags != r1.flags) goto L24;
        if (Intrinsics.areEqual(this.receiver, r1.receiver) == false) goto L24;
        if (Intrinsics.areEqual(this.owner, r1.owner) == false) goto L24;
        if (this.name.equals(r1.name) == false) goto L24;
        if (this.signature.equals(r1.signature) == false) goto L24;
        return true;
    L24:
        return false;
    }

    @Override // kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    public KDeclarationContainer getOwner() {
        if (this.owner != null) goto L6;
        return null;
    L6:
        if (this.isTopLevel == false) goto L9;
        return Reflection.getOrCreateKotlinPackage(this.owner);
    L9:
        return Reflection.getOrCreateKotlinClass(this.owner);
    }

    public int hashCode() {
        int r1 = 0;
        if (this.receiver == null) goto L5;
        int r0 = this.receiver.hashCode();
    L6:
        int r2 = r0 * 31;
        if (this.owner == null) goto L9;
        r1 = this.owner.hashCode();
    L9:
        int r3 = (((((r2 + r1) * 31) + this.name.hashCode()) * 31) + this.signature.hashCode()) * 31;
        if (this.isTopLevel == false) goto L12;
        int r4 = 1231;
    L14:
        return ((((r3 + r4) * 31) + this.arity) * 31) + this.flags;
    L12:
        r4 = 1237;
        goto L14
    L5:
        r0 = 0;
        goto L6
    }

    public String toString() {
        return Reflection.renderLambdaToString(this);
    }
}
