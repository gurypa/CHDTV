package com.google.common.collect;

import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Enum;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
    private final transient EnumSet<E> delegate;

    @LazyInit
    private transient int hashCode;

    /* JADX INFO: renamed from: com.google.common.collect.ImmutableEnumSet$1 */
    static /* synthetic */ class C06291 {
    }

    private static class EnumSerializedForm<E extends Enum<E>> implements Serializable {
        private static final long serialVersionUID = 0;
        final EnumSet<E> delegate;

        EnumSerializedForm(EnumSet<E> r1) {
            this.delegate = r1;
        }

        Object readResolve() {
            return new ImmutableEnumSet(this.delegate.clone(), null);
        }
    }

    private ImmutableEnumSet(EnumSet<E> r1) {
        this.delegate = r1;
    }

    /* synthetic */ ImmutableEnumSet(EnumSet r1, C06291 r2) {
        this(r1);
    }

    static <E extends Enum<E>> ImmutableSet<E> asImmutable(EnumSet<E> r1) {
        switch(r1.size()) {
            case 0: goto L9;
            case 1: goto L7;
            default: goto L5;
        };
    L5:
        return new ImmutableEnumSet(r1);
    L7:
        return ImmutableSet.m260of((Enum) Iterables.getOnlyElement(r1));
    L9:
        return ImmutableSet.m259of();
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r2) {
        return this.delegate.contains(r2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> r2) {
        if ((r2 instanceof ImmutableEnumSet) == false) goto L6;
        r2 = ((ImmutableEnumSet) r2).delegate;
    L6:
        return this.delegate.containsAll(r2);
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L6;
        return true;
    L6:
        if ((r2 instanceof ImmutableEnumSet) == false) goto L9;
        r2 = ((ImmutableEnumSet) r2).delegate;
    L9:
        return this.delegate.equals(r2);
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        int r0 = this.hashCode;
        if (r0 != 0) goto L6;
        int r1 = this.delegate.hashCode();
        this.hashCode = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return true;
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.delegate.size();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return this.delegate.toString();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new EnumSerializedForm(this.delegate);
    }
}
