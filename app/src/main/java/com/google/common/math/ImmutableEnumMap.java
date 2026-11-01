package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Enum;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;

    /* JADX INFO: renamed from: com.google.common.collect.ImmutableEnumMap$1 */
    static /* synthetic */ class C06281 {
    }

    private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumMap<K, V> delegate;

        EnumSerializedForm(EnumMap<K, V> r1) {
            this.delegate = r1;
        }

        Object readResolve() {
            return new ImmutableEnumMap(this.delegate, null);
        }
    }

    private ImmutableEnumMap(EnumMap<K, V> r2) {
        this.delegate = r2;
        Preconditions.checkArgument(!r2.isEmpty());
    }

    /* synthetic */ ImmutableEnumMap(EnumMap r1, C06281 r2) {
        this(r1);
    }

    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> r3) {
        switch(r3.size()) {
            case 0: goto L9;
            case 1: goto L6;
            default: goto L5;
        };
    L6:
        Map.Entry r0 = (Map.Entry) Iterables.getOnlyElement(r3.entrySet());
        return ImmutableMap.m232of((Enum) r0.getKey(), r0.getValue());
    L5:
        return new ImmutableEnumMap(r3);
    L9:
        return ImmutableMap.m231of();
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use EnumSerializedForm");
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean containsKey(@CheckForNull Object r2) {
        return this.delegate.containsKey(r2);
    }

    @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
        return Maps.unmodifiableEntryIterator(this.delegate.entrySet().iterator());
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L6;
        return true;
    L6:
        if ((r2 instanceof ImmutableEnumMap) == false) goto L9;
        r2 = ((ImmutableEnumMap) r2).delegate;
    L9:
        return this.delegate.equals(r2);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r2) {
        return this.delegate.get(r2);
    }

    @Override // com.google.common.collect.ImmutableMap
    boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableMap
    UnmodifiableIterator<K> keyIterator() {
        return Iterators.unmodifiableIterator(this.delegate.keySet().iterator());
    }

    @Override // java.util.Map
    public int size() {
        return this.delegate.size();
    }

    @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
