package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
    AbstractMapEntry() {
    }

    @Override // java.util.Map.Entry
    public boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Map.Entry) == false) goto L11;
        Map.Entry r0 = (Map.Entry) r5;
        if (Objects.equal(getKey(), r0.getKey()) == true) goto L7;
        return false;
    L7:
        if (Objects.equal(getValue(), r0.getValue()) == false) goto L13;
        return true;
    L13:
        return false;
    L11:
        return false;
    }

    @Override // java.util.Map.Entry
    @ParametricNullness
    public abstract K getKey();

    @Override // java.util.Map.Entry
    @ParametricNullness
    public abstract V getValue();

    @Override // java.util.Map.Entry
    public int hashCode() {
        K r0 = getKey();
        V r1 = getValue();
        int r2 = 0;
        if (r0 != null) goto L5;
        int r3 = 0;
    L6:
        if (r1 == null) goto L10;
        r2 = r1.hashCode();
    L10:
        return r2 ^ r3;
    L5:
        r3 = r0.hashCode();
        goto L6
    }

    @Override // java.util.Map.Entry
    @ParametricNullness
    public V setValue(@ParametricNullness V r2) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return getKey() + "=" + getValue();
    }
}
