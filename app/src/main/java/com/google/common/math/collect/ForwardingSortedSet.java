package com.google.common.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E> {
    protected ForwardingSortedSet() {
    }

    @Override // java.util.SortedSet
    @CheckForNull
    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Set delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract SortedSet<E> delegate();

    @Override // java.util.SortedSet
    @ParametricNullness
    public E first() {
        return delegate().first();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> headSet(@ParametricNullness E r2) {
        return delegate().headSet(r2);
    }

    @Override // java.util.SortedSet
    @ParametricNullness
    public E last() {
        return delegate().last();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardContains(@CheckForNull Object r5) {
        E r2 = tailSet(r5).first();     // Catch: NullPointerException -> L7 Throwable -> L9 ClassCastException -> L11
        if (ForwardingSortedMap.unsafeCompare(comparator(), r2, r5) != 0) goto L15;
        return true;
    L15:
        return false;
    L12:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingCollection
    protected boolean standardRemove(@CheckForNull Object r6) {
        Iterator<E> r2 = tailSet(r6).iterator();     // Catch: NullPointerException -> L12 Throwable -> L14
        if (r2.hasNext() == false) goto L11;
        E r3 = r2.next();     // Catch: NullPointerException -> L12 Throwable -> L14
        if (ForwardingSortedMap.unsafeCompare(comparator(), r3, r6) != 0) goto L11;
        r2.remove();     // Catch: NullPointerException -> L12 Throwable -> L14
        return true;
    L11:
        return false;
    L15:
        return false;
    }

    protected SortedSet<E> standardSubSet(@ParametricNullness E r2, @ParametricNullness E r3) {
        return tailSet(r2).headSet(r3);
    }

    @Override // java.util.SortedSet
    public SortedSet<E> subSet(@ParametricNullness E r2, @ParametricNullness E r3) {
        return delegate().subSet(r2, r3);
    }

    @Override // java.util.SortedSet
    public SortedSet<E> tailSet(@ParametricNullness E r2) {
        return delegate().tailSet(r2);
    }
}
