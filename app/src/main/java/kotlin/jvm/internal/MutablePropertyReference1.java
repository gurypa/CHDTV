package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty;
import kotlin.reflect.KProperty1;

/* JADX INFO: loaded from: classes.dex */
public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1 {
    public MutablePropertyReference1() {
    }

    public MutablePropertyReference1(Object r1) {
        super(r1);
    }

    public MutablePropertyReference1(Object r1, Class r2, String r3, String r4, int r5) {
        super(r1, r2, r3, r4, r5);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected KCallable computeReflected() {
        return Reflection.mutableProperty1(this);
    }

    @Override // kotlin.reflect.KProperty1
    public Object getDelegate(Object r2) {
        return ((KMutableProperty1) getReflected()).getDelegate(r2);
    }

    @Override // kotlin.reflect.KProperty
    public /* bridge */ /* synthetic */ KProperty.Getter getGetter() {
        return getGetter();
    }

    @Override // kotlin.reflect.KProperty
    public KProperty1.Getter getGetter() {
        return ((KMutableProperty1) getReflected()).getGetter();
    }

    @Override // kotlin.reflect.KMutableProperty
    public /* bridge */ /* synthetic */ KMutableProperty.Setter getSetter() {
        return getSetter();
    }

    @Override // kotlin.reflect.KMutableProperty
    public KMutableProperty1.Setter getSetter() {
        return ((KMutableProperty1) getReflected()).getSetter();
    }

    @Override // kotlin.jvm.functions.Function1
    public Object invoke(Object r2) {
        return get(r2);
    }
}
