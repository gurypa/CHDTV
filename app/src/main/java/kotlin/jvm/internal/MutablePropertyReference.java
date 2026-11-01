package kotlin.jvm.internal;

import kotlin.reflect.KMutableProperty;

/* JADX INFO: loaded from: classes.dex */
public abstract class MutablePropertyReference extends PropertyReference implements KMutableProperty {
    public MutablePropertyReference() {
    }

    public MutablePropertyReference(Object r1) {
        super(r1);
    }

    public MutablePropertyReference(Object r1, Class r2, String r3, String r4, int r5) {
        super(r1, r2, r3, r4, r5);
    }
}
