package kotlin.jvm.internal;

import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

/* JADX INFO: loaded from: classes.dex */
public class MutablePropertyReference2Impl extends MutablePropertyReference2 {
    public MutablePropertyReference2Impl(Class r1, String r2, String r3, int r4) {
        super(r1, r2, r3, r4);
    }

    public MutablePropertyReference2Impl(KDeclarationContainer r3, String r4, String r5) {
        super(((ClassBasedDeclarationContainer) r3).getJClass(), r4, r5, !(r3 instanceof KClass) ? 1 : 0);
    }

    @Override // kotlin.reflect.KProperty2
    public Object get(Object r4, Object r5) {
        return getGetter().call(new Object[]{r4, r5});
    }

    @Override // kotlin.reflect.KMutableProperty2
    public void set(Object r4, Object r5, Object r6) {
        getSetter().call(new Object[]{r4, r5, r6});
    }
}
