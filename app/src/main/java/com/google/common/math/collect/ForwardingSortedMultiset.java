package com.google.common.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingSortedMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {

    protected abstract class StandardDescendingMultiset extends DescendingMultiset<E> {
        final /* synthetic */ ForwardingSortedMultiset this$0;

        public StandardDescendingMultiset(ForwardingSortedMultiset r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.collect.DescendingMultiset
        SortedMultiset<E> forwardMultiset() {
            return this.this$0;
        }
    }

    protected class StandardElementSet extends SortedMultisets.NavigableElementSet<E> {
        final /* synthetic */ ForwardingSortedMultiset this$0;

        public StandardElementSet(ForwardingSortedMultiset r1) {
            this.this$0 = r1;
            super(r1);
        }
    }

    protected ForwardingSortedMultiset() {
    }

    @Override // com.google.common.collect.SortedMultiset, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Multiset delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract SortedMultiset<E> delegate();

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> descendingMultiset() {
        return delegate().descendingMultiset();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public NavigableSet<E> elementSet() {
        return delegate().elementSet();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ SortedSet elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> firstEntry() {
        return delegate().firstEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> headMultiset(@ParametricNullness E r2, BoundType r3) {
        return delegate().headMultiset(r2, r3);
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> lastEntry() {
        return delegate().lastEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> pollFirstEntry() {
        return delegate().pollFirstEntry();
    }

    @Override // com.google.common.collect.SortedMultiset
    @CheckForNull
    public Multiset.Entry<E> pollLastEntry() {
        return delegate().pollLastEntry();
    }

    @CheckForNull
    protected Multiset.Entry<E> standardFirstEntry() {
        Iterator<Multiset.Entry<E>> r0 = entrySet().iterator();
        if (r0.hasNext() == true) goto L6;
        return null;
    L6:
        Multiset.Entry<E> r1 = r0.next();
        return Multisets.immutableEntry(r1.getElement(), r1.getCount());
    }

    @CheckForNull
    protected Multiset.Entry<E> standardLastEntry() {
        Iterator<Multiset.Entry<E>> r0 = descendingMultiset().entrySet().iterator();
        if (r0.hasNext() == true) goto L6;
        return null;
    L6:
        Multiset.Entry<E> r1 = r0.next();
        return Multisets.immutableEntry(r1.getElement(), r1.getCount());
    }

    @CheckForNull
    protected Multiset.Entry<E> standardPollFirstEntry() {
        Iterator<Multiset.Entry<E>> r0 = entrySet().iterator();
        if (r0.hasNext() == true) goto L6;
        return null;
    L6:
        Multiset.Entry<E> r1 = r0.next();
        Multiset.Entry<E> r2 = Multisets.immutableEntry(r1.getElement(), r1.getCount());
        r0.remove();
        return r2;
    }

    @CheckForNull
    protected Multiset.Entry<E> standardPollLastEntry() {
        Iterator<Multiset.Entry<E>> r0 = descendingMultiset().entrySet().iterator();
        if (r0.hasNext() == true) goto L6;
        return null;
    L6:
        Multiset.Entry<E> r1 = r0.next();
        Multiset.Entry<E> r2 = Multisets.immutableEntry(r1.getElement(), r1.getCount());
        r0.remove();
        return r2;
    }

    protected SortedMultiset<E> standardSubMultiset(@ParametricNullness E r2, BoundType r3, @ParametricNullness E r4, BoundType r5) {
        return tailMultiset(r2, r3).headMultiset(r4, r5);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> subMultiset(@ParametricNullness E r2, BoundType r3, @ParametricNullness E r4, BoundType r5) {
        return delegate().subMultiset(r2, r3, r4, r5);
    }

    @Override // com.google.common.collect.SortedMultiset
    public SortedMultiset<E> tailMultiset(@ParametricNullness E r2, BoundType r3) {
        return delegate().tailMultiset(r2, r3);
    }
}
