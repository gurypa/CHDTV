package kotlin.jvm.internal;

import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

/* JADX INFO: loaded from: classes.dex */
public class MutablePropertyReference1Impl extends MutablePropertyReference1 {
    public MutablePropertyReference1Impl(Class r7, String r8, String r9, int r10) {
        super(NO_RECEIVER, r7, r8, r9, r10);
    }

    public MutablePropertyReference1Impl(Object r1, Class r2, String r3, String r4, int r5) {
        super(r1, r2, r3, r4, r5);
    }

    public MutablePropertyReference1Impl(KDeclarationContainer r7, String r8, String r9) {
        super(NO_RECEIVER, ((ClassBasedDeclarationContainer) r7).getJClass(), r8, r9, !(r7 instanceof KClass) ? 1 : 0);
    }

    @Override // kotlin.reflect.KProperty1
    public Object get(Object r4) {
        return getGetter().call(new Object[]{r4});
    }

    @Override // kotlin.reflect.KMutableProperty1
    public void set(Object r4, Object r5) {
        getSetter().call(new Object[]{r4, r5});
    }
}
