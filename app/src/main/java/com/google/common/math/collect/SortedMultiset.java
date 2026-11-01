package com.google.common.collect;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface SortedMultiset<E> extends SortedMultisetBridge<E>, SortedIterable<E> {

    /* JADX INFO: renamed from: com.google.common.collect.SortedMultiset$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ Set $default$elementSet(SortedMultiset r1) {
            return r1.elementSet();
        }

        /* JADX INFO: renamed from: $default$elementSet, reason: collision with other method in class */
        public static /* bridge */ /* synthetic */ SortedSet m497$default$elementSet(SortedMultiset r1) {
            return r1.elementSet();
        }
    }

    Comparator<? super E> comparator();

    SortedMultiset<E> descendingMultiset();

    @Override // com.google.common.collect.SortedMultisetBridge, com.google.common.collect.Multiset
    NavigableSet<E> elementSet();

    @Override // com.google.common.collect.Multiset
    Set<Multiset.Entry<E>> entrySet();

    @CheckForNull
    Multiset.Entry<E> firstEntry();

    SortedMultiset<E> headMultiset(@ParametricNullness E r1, BoundType r2);

    @Override // com.google.common.collect.Multiset, java.util.Collection, java.lang.Iterable
    Iterator<E> iterator();

    @CheckForNull
    Multiset.Entry<E> lastEntry();

    @CheckForNull
    Multiset.Entry<E> pollFirstEntry();

    @CheckForNull
    Multiset.Entry<E> pollLastEntry();

    SortedMultiset<E> subMultiset(@ParametricNullness E r1, BoundType r2, @ParametricNullness E r3, BoundType r4);

    SortedMultiset<E> tailMultiset(@ParametricNullness E r1, BoundType r2);
}
