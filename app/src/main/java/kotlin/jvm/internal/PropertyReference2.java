package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;
import kotlin.reflect.KProperty2;

/* JADX INFO: loaded from: classes.dex */
public abstract class PropertyReference2 extends PropertyReference implements KProperty2 {
    public PropertyReference2() {
    }

    public PropertyReference2(Class r7, String r8, String r9, int r10) {
        super(NO_RECEIVER, r7, r8, r9, r10);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected KCallable computeReflected() {
        return Reflection.property2(this);
    }

    @Override // kotlin.reflect.KProperty2
    public Object getDelegate(Object r2, Object r3) {
        return ((KProperty2) getReflected()).getDelegate(r2, r3);
    }

    @Override // kotlin.reflect.KProperty
    public /* bridge */ /* synthetic */ KProperty.Getter getGetter() {
        return getGetter();
    }

    @Override // kotlin.reflect.KProperty
    public KProperty2.Getter getGetter() {
        return ((KProperty2) getReflected()).getGetter();
    }

    @Override // kotlin.jvm.functions.Function2
    public Object invoke(Object r2, Object r3) {
        return get(r2, r3);
    }
}
