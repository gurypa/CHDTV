package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
    private final Map<Class<? extends B>, B> delegate;

    private static final class SerializedForm<B> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Map<Class<? extends B>, B> backingMap;

        SerializedForm(Map<Class<? extends B>, B> r1) {
            this.backingMap = r1;
        }

        Object readResolve() {
            return MutableClassToInstanceMap.create(this.backingMap);
        }
    }

    private MutableClassToInstanceMap(Map<Class<? extends B>, B> r2) {
        this.delegate = (Map) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ Object access$000(Class r1, Object r2) {
        return cast(r1, r2);
    }

    static /* synthetic */ Map.Entry access$100(Map.Entry r1) {
        return checkedEntry(r1);
    }

    @CheckForNull
    private static <T> T cast(Class<T> r1, @CheckForNull Object r2) {
        return (T) Primitives.wrap(r1).cast(r2);
    }

    private static <B> Map.Entry<Class<? extends B>, B> checkedEntry(final Map.Entry<Class<? extends B>, B> r1) {
        return new C07141(r1);
    }

    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap(new HashMap());
    }

    public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> r1) {
        return new MutableClassToInstanceMap(r1);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    private Object writeReplace() {
        return new SerializedForm(delegate());
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
    protected Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map
    public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
        return new C07152(this);
    }

    @Override // com.google.common.collect.ClassToInstanceMap
    @CheckForNull
    public <T extends B> T getInstance(Class<T> r2) {
        return (T) cast(r2, get(r2));
    }

    @CheckForNull
    public B put(Class<? extends B> r2, @ParametricNullness B r3) {
        cast(r2, r3);
        return (B) super.put(r2, r3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    public /* bridge */ /* synthetic */ Object put(Object r1, @ParametricNullness Object r2) {
        return put((Class) r1, r2);
    }

    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public void putAll(Map<? extends Class<? extends B>, ? extends B> r6) {
        LinkedHashMap r0 = new LinkedHashMap(r6);
        Iterator r1 = r0.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry r2 = (Map.Entry) r1.next();
        cast((Class) r2.getKey(), r2.getValue());
        goto L4
    L6:
        super.putAll(r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ClassToInstanceMap
    @CheckForNull
    public <T extends B> T putInstance(Class<T> r2, @ParametricNullness T r3) {
        return (T) cast(r2, put(r2, r3));
    }
}
