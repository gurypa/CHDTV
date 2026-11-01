package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;

/* JADX INFO: loaded from: classes.dex */
public abstract class PropertyReference extends CallableReference implements KProperty {
    private final boolean syntheticJavaProperty;

    public PropertyReference() {
        this.syntheticJavaProperty = false;
    }

    public PropertyReference(Object r2) {
        super(r2);
        this.syntheticJavaProperty = false;
    }

    public PropertyReference(Object r10, Class r11, String r12, String r13, int r14) {
        boolean r1 = false;
        if ((r14 & 1) != 1) goto L5;
        boolean r8 = true;
    L6:
        super(r10, r11, r12, r13, r8);
        if ((r14 & 2) != 2) goto L9;
        r1 = true;
    L9:
        this.syntheticJavaProperty = r1;
        return;
    L5:
        r8 = false;
        goto L6
    }

    @Override // kotlin.jvm.internal.CallableReference
    public KCallable compute() {
        if (this.syntheticJavaProperty == false) goto L6;
        return this;
    L6:
        return super.compute();
    }

    public boolean equals(Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof PropertyReference) == false) goto L19;
        PropertyReference r1 = (PropertyReference) r6;
        if (getOwner().equals(r1.getOwner()) == false) goto L17;
        if (getName().equals(r1.getName()) == false) goto L17;
        if (getSignature().equals(r1.getSignature()) == false) goto L17;
        if (Intrinsics.areEqual(getBoundReceiver(), r1.getBoundReceiver()) == false) goto L17;
        return true;
    L17:
        return false;
    L19:
        if ((r6 instanceof KProperty) == true) goto L21;
        return false;
    L21:
        return r6.equals(compute());
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected /* bridge */ /* synthetic */ KCallable getReflected() {
        return getReflected();
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected KProperty getReflected() {
        if (this.syntheticJavaProperty == true) goto L7;
        return (KProperty) super.getReflected();
    L7:
        throw new UnsupportedOperationException("Kotlin reflection is not yet supported for synthetic Java properties");
    }

    public int hashCode() {
        return (((getOwner().hashCode() * 31) + getName().hashCode()) * 31) + getSignature().hashCode();
    }

    @Override // kotlin.reflect.KProperty
    public boolean isConst() {
        return getReflected().isConst();
    }

    @Override // kotlin.reflect.KProperty
    public boolean isLateinit() {
        return getReflected().isLateinit();
    }

    public String toString() {
        KCallable r0 = compute();
        if (r0 == this) goto L7;
        return r0.toString();
    L7:
        return "property " + getName() + " (Kotlin reflection is not available)";
    }
}
