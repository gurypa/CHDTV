package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;
import kotlin.reflect.KProperty0;

/* JADX INFO: loaded from: classes.dex */
public abstract class PropertyReference0 extends PropertyReference implements KProperty0 {
    public PropertyReference0() {
    }

    public PropertyReference0(Object r1) {
        super(r1);
    }

    public PropertyReference0(Object r1, Class r2, String r3, String r4, int r5) {
        super(r1, r2, r3, r4, r5);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected KCallable computeReflected() {
        return Reflection.property0(this);
    }

    @Override // kotlin.reflect.KProperty0
    public Object getDelegate() {
        return ((KProperty0) getReflected()).getDelegate();
    }

    @Override // kotlin.reflect.KProperty
    public /* bridge */ /* synthetic */ KProperty.Getter getGetter() {
        return getGetter();
    }

    @Override // kotlin.reflect.KProperty
    public KProperty0.Getter getGetter() {
        return ((KProperty0) getReflected()).getGetter();
    }

    @Override // kotlin.jvm.functions.Function0
    public Object invoke() {
        return get();
    }
}
