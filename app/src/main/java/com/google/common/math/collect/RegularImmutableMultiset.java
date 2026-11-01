package com.google.common.collect;

import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
    static final RegularImmutableMultiset<Object> EMPTY = null;
    final transient ObjectCountHashMap<E> contents;

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<E> elementSet;
    private final transient int size;

    /* JADX INFO: renamed from: com.google.common.collect.RegularImmutableMultiset$1 */
    static /* synthetic */ class C07211 {
    }

    private final class ElementSet extends IndexedImmutableSet<E> {
        final /* synthetic */ RegularImmutableMultiset this$0;

        private ElementSet(RegularImmutableMultiset r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ElementSet(RegularImmutableMultiset r1, C07211 r2) {
            this(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r2) {
            return this.this$0.contains(r2);
        }

        @Override // com.google.common.collect.IndexedImmutableSet
        E get(int r2) {
            return this.this$0.contents.getKey(r2);
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.contents.size();
        }

        @Override // com.google.common.collect.IndexedImmutableSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final int[] counts;
        final Object[] elements;

        SerializedForm(Multiset<? extends Object> r7) {
            int r0 = r7.entrySet().size();
            this.elements = new Object[r0];
            this.counts = new int[r0];
            int r1 = 0;
            Iterator<Multiset.Entry<? extends Object>> r2 = r7.entrySet().iterator();
        L4:
            if (r2.hasNext() == false) goto L6;
            Multiset.Entry<? extends Object> r3 = r2.next();
            this.elements[r1] = r3.getElement();
            this.counts[r1] = r3.getCount();
            r1 = r1 + 1;
            goto L4
        }

        /* JADX WARN: Multi-variable type inference failed */
        Object readResolve() {
            ImmutableMultiset.Builder r0 = new ImmutableMultiset.Builder(this.elements.length);
            int r1 = 0;
        L4:
            if (r1 >= this.elements.length) goto L7;
            r0.addCopies(this.elements[r1], this.counts[r1]);
            r1 = r1 + 1;
            goto L4
        L7:
            return r0.build();
        }
    }

    static {
        EMPTY = new RegularImmutableMultiset(ObjectCountHashMap.create());
    }

    RegularImmutableMultiset(ObjectCountHashMap<E> r6) {
        this.contents = r6;
        long r0 = 0;
        int r2 = 0;
    L4:
        if (r2 >= r6.size()) goto L6;
        r0 = r0 + ((long) r6.getValue(r2));
        r2 = r2 + 1;
        goto L4
    L6:
        this.size = Ints.saturatedCast(r0);
    }

    @Override // com.google.common.collect.Multiset
    public int count(@CheckForNull Object r2) {
        return this.contents.get(r2);
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public ImmutableSet<E> elementSet() {
        ImmutableSet<E> r0 = this.elementSet;
        if (r0 != null) goto L6;
        ElementSet r1 = new ElementSet(this, null);
        this.elementSet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.ImmutableMultiset
    Multiset.Entry<E> getEntry(int r2) {
        return this.contents.getEntry(r2);
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
