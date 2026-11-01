package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface Multiset<E> extends Collection<E> {

    public interface Entry<E> {
        boolean equals(@CheckForNull Object r1);

        int getCount();

        @ParametricNullness
        E getElement();

        int hashCode();

        String toString();
    }

    int add(@ParametricNullness E r1, int r2);

    boolean add(@ParametricNullness E r1);

    boolean contains(@CheckForNull Object r1);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> r1);

    int count(@CheckForNull Object r1);

    Set<E> elementSet();

    Set<Entry<E>> entrySet();

    boolean equals(@CheckForNull Object r1);

    int hashCode();

    Iterator<E> iterator();

    int remove(@CheckForNull Object r1, int r2);

    boolean remove(@CheckForNull Object r1);

    boolean removeAll(Collection<?> r1);

    boolean retainAll(Collection<?> r1);

    int setCount(@ParametricNullness E r1, int r2);

    boolean setCount(@ParametricNullness E r1, int r2, int r3);

    int size();

    String toString();
}
