package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Enum;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class EnumHashBiMap<K extends Enum<K>, V> extends AbstractBiMap<K, V> {
    private static final long serialVersionUID = 0;
    transient Class<K> keyTypeOrObjectUnderJ2cl;

    private EnumHashBiMap(Class<K> r3) {
        super(new EnumMap(r3), new HashMap());
        this.keyTypeOrObjectUnderJ2cl = r3;
    }

    public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> r1) {
        return new EnumHashBiMap(r1);
    }

    public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> r1) {
        EnumHashBiMap<K, V> r0 = create(EnumBiMap.inferKeyTypeOrObjectUnderJ2cl(r1));
        r0.putAll(r1);
        return r0;
    }

    private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
        r3.defaultReadObject();
        this.keyTypeOrObjectUnderJ2cl = (Class) Objects.requireNonNull(r3.readObject());
        setDelegates(new EnumMap(this.keyTypeOrObjectUnderJ2cl), new HashMap());
        Serialization.populateMap(this, r3);
    }

    private void writeObject(ObjectOutputStream r2) throws IOException {
        r2.defaultWriteObject();
        r2.writeObject(this.keyTypeOrObjectUnderJ2cl);
        Serialization.writeMap(this, r2);
    }

    K checkKey(K r2) {
        return (K) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.collect.AbstractBiMap
    /* bridge */ /* synthetic */ Object checkKey(Object r1) {
        return checkKey((Enum) r1);
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ boolean containsValue(@CheckForNull Object r1) {
        return super.containsValue(r1);
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @CheckForNull
    public V forcePut(K r2, @ParametricNullness V r3) {
        return (V) super.forcePut(r2, r3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.BiMap
    @CheckForNull
    public /* bridge */ /* synthetic */ Object forcePut(Object r1, @ParametricNullness Object r2) {
        return forcePut((Enum) r1, r2);
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.BiMap
    public /* bridge */ /* synthetic */ BiMap inverse() {
        return super.inverse();
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    public Class<K> keyType() {
        return this.keyTypeOrObjectUnderJ2cl;
    }

    @CheckForNull
    public V put(K r2, @ParametricNullness V r3) {
        return (V) super.put(r2, r3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    public /* bridge */ /* synthetic */ Object put(Object r1, @ParametricNullness Object r2) {
        return put((Enum) r1, r2);
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public /* bridge */ /* synthetic */ void putAll(Map r1) {
        super.putAll(r1);
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    @CheckForNull
    public /* bridge */ /* synthetic */ Object remove(@CheckForNull Object r1) {
        return super.remove(r1);
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ Set values() {
        return super.values();
    }
}
