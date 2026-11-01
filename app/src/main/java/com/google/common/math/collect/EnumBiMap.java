package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Enum;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends AbstractBiMap<K, V> {
    private static final long serialVersionUID = 0;
    transient Class<K> keyTypeOrObjectUnderJ2cl;
    transient Class<V> valueTypeOrObjectUnderJ2cl;

    private EnumBiMap(Class<K> r3, Class<V> r4) {
        super(new EnumMap(r3), new EnumMap(r4));
        this.keyTypeOrObjectUnderJ2cl = r3;
        this.valueTypeOrObjectUnderJ2cl = r4;
    }

    public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> r1, Class<V> r2) {
        return new EnumBiMap(r1, r2);
    }

    public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> r2) {
        EnumBiMap<K, V> r0 = create(inferKeyTypeOrObjectUnderJ2cl(r2), inferValueTypeOrObjectUnderJ2cl(r2));
        r0.putAll(r2);
        return r0;
    }

    static <K extends Enum<K>> Class<K> inferKeyTypeOrObjectUnderJ2cl(Map<K, ?> r1) {
        if ((r1 instanceof EnumBiMap) == false) goto L7;
        return ((EnumBiMap) r1).keyTypeOrObjectUnderJ2cl;
    L7:
        if ((r1 instanceof EnumHashBiMap) == true) goto L9;
        Preconditions.checkArgument(!r1.isEmpty());
        return Platform.getDeclaringClassOrObjectForJ2cl(r1.keySet().iterator().next());
    L9:
        return ((EnumHashBiMap) r1).keyTypeOrObjectUnderJ2cl;
    }

    private static <V extends Enum<V>> Class<V> inferValueTypeOrObjectUnderJ2cl(Map<?, V> r1) {
        if ((r1 instanceof EnumBiMap) == true) goto L5;
        Preconditions.checkArgument(!r1.isEmpty());
        return Platform.getDeclaringClassOrObjectForJ2cl(r1.values().iterator().next());
    L5:
        return ((EnumBiMap) r1).valueTypeOrObjectUnderJ2cl;
    }

    private void readObject(ObjectInputStream r4) throws IOException, ClassNotFoundException {
        r4.defaultReadObject();
        this.keyTypeOrObjectUnderJ2cl = (Class) Objects.requireNonNull(r4.readObject());
        this.valueTypeOrObjectUnderJ2cl = (Class) Objects.requireNonNull(r4.readObject());
        setDelegates(new EnumMap(this.keyTypeOrObjectUnderJ2cl), new EnumMap(this.valueTypeOrObjectUnderJ2cl));
        Serialization.populateMap(this, r4);
    }

    private void writeObject(ObjectOutputStream r2) throws IOException {
        r2.defaultWriteObject();
        r2.writeObject(this.keyTypeOrObjectUnderJ2cl);
        r2.writeObject(this.valueTypeOrObjectUnderJ2cl);
        Serialization.writeMap(this, r2);
    }

    K checkKey(K r2) {
        return (K) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.collect.AbstractBiMap
    /* bridge */ /* synthetic */ Object checkKey(Object r1) {
        return checkKey((Enum) r1);
    }

    V checkValue(V r2) {
        return (V) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.collect.AbstractBiMap
    /* bridge */ /* synthetic */ Object checkValue(Object r1) {
        return checkValue((Enum) r1);
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

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.BiMap
    @CheckForNull
    public /* bridge */ /* synthetic */ Object forcePut(@ParametricNullness Object r1, @ParametricNullness Object r2) {
        return super.forcePut(r1, r2);
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

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    @CheckForNull
    public /* bridge */ /* synthetic */ Object put(@ParametricNullness Object r1, @ParametricNullness Object r2) {
        return super.put(r1, r2);
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

    public Class<V> valueType() {
        return this.valueTypeOrObjectUnderJ2cl;
    }

    @Override // com.google.common.collect.AbstractBiMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ Set values() {
        return super.values();
    }
}
