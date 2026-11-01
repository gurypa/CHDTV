package com.google.common.collect;

import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class LinkedHashMultiset<E> extends AbstractMapBasedMultiset<E> {
    LinkedHashMultiset(int r1) {
        super(r1);
    }

    public static <E> LinkedHashMultiset<E> create() {
        return create(3);
    }

    public static <E> LinkedHashMultiset<E> create(int r1) {
        return new LinkedHashMultiset(r1);
    }

    public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> r1) {
        LinkedHashMultiset<E> r0 = create(Multisets.inferDistinctElements(r1));
        Iterables.addAll(r0, r1);
        return r0;
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ boolean contains(@CheckForNull Object r1) {
        return super.contains(r1);
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultiset
    ObjectCountHashMap<E> newBackingMap(int r2) {
        return new ObjectCountLinkedHashMap(r2);
    }
}
