package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class ImmutableMultiset<E> extends ImmutableMultisetGwtSerializationDependencies<E> implements Multiset<E> {
    private static final long serialVersionUID = 912559;

    @CheckForNull
    @LazyInit
    private transient ImmutableList<E> asList;

    @CheckForNull
    @LazyInit
    private transient ImmutableSet<Multiset.Entry<E>> entrySet;

    public static class Builder<E> extends ImmutableCollection.Builder<E> {
        boolean buildInvoked;

        @CheckForNull
        ObjectCountHashMap<E> contents;
        boolean isLinkedHash;

        public Builder() {
            this(4);
        }

        Builder(int r2) {
            this.buildInvoked = false;
            this.isLinkedHash = false;
            this.contents = ObjectCountHashMap.createWithExpectedSize(r2);
        }

        Builder(boolean r2) {
            this.buildInvoked = false;
            this.isLinkedHash = false;
            this.contents = null;
        }

        @CheckForNull
        static <T> ObjectCountHashMap<T> tryGetMap(Iterable<T> r1) {
            if ((r1 instanceof RegularImmutableMultiset) == false) goto L7;
            return ((RegularImmutableMultiset) r1).contents;
        L7:
            if ((r1 instanceof AbstractMapBasedMultiset) == true) goto L9;
            return null;
        L9:
            return ((AbstractMapBasedMultiset) r1).backingMap;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object r1) {
            return add(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object[] r1) {
            return add(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E r2) {
            return addCopies(r2, 1);
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... r1) {
            super.add(r1);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterable r1) {
            return addAll(r1);
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder addAll(Iterator r1) {
            return addAll(r1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> r8) {
            Objects.requireNonNull(this.contents);
            if ((r8 instanceof Multiset) == false) goto L15;
            Multiset r0 = Multisets.cast(r8);
            ObjectCountHashMap r1 = tryGetMap(r0);
            if (r1 == null) goto L10;
            this.contents.ensureCapacity(Math.max(this.contents.size(), r1.size()));
            int r2 = r1.firstIndex();
        L7:
            if (r2 < 0) goto L16;
            addCopies(r1.getKey(r2), r1.getValue(r2));
            r2 = r1.nextIndex(r2);
        L16:
            return this;
        L10:
            Set<Multiset.Entry<E>> r3 = r0.entrySet();
            this.contents.ensureCapacity(Math.max(this.contents.size(), r3.size()));
            Iterator<Multiset.Entry<E>> r4 = r0.entrySet().iterator();
        L12:
            if (r4.hasNext() == false) goto L16;
            Multiset.Entry<E> r5 = r4.next();
            addCopies(r5.getElement(), r5.getCount());
            goto L12
        L15:
            super.addAll(r8);
            goto L16
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> r1) {
            super.addAll(r1);
            return this;
        }

        public Builder<E> addCopies(E r4, int r5) {
            Objects.requireNonNull(this.contents);
            if (r5 != 0) goto L6;
            return this;
        L6:
            if (this.buildInvoked == false) goto L8;
            this.contents = new ObjectCountHashMap(this.contents);
            this.isLinkedHash = false;
        L8:
            this.buildInvoked = false;
            Preconditions.checkNotNull(r4);
            this.contents.put(r4, this.contents.get(r4) + r5);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection build() {
            return build();
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableMultiset<E> build() {
            Objects.requireNonNull(this.contents);
            if (this.contents.size() != 0) goto L7;
            return ImmutableMultiset.m248of();
        L7:
            if (this.isLinkedHash == false) goto L9;
            this.contents = new ObjectCountHashMap(this.contents);
            this.isLinkedHash = false;
        L9:
            this.buildInvoked = true;
            return new RegularImmutableMultiset(this.contents);
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        public Builder<E> setCount(E r4, int r5) {
            Objects.requireNonNull(this.contents);
            if (r5 != 0) goto L8;
            if (this.isLinkedHash == true) goto L8;
            this.contents = new ObjectCountLinkedHashMap(this.contents);
            this.isLinkedHash = true;
        L10:
            this.buildInvoked = false;
            Preconditions.checkNotNull(r4);
            if (r5 != 0) goto L13;
            this.contents.remove(r4);
        L14:
            return this;
        L13:
            this.contents.put((E) Preconditions.checkNotNull(r4), r5);
        L8:
            if (this.buildInvoked == false) goto L10;
            this.contents = new ObjectCountHashMap(this.contents);
            this.isLinkedHash = false;
            goto L10
        }
    }

    private final class EntrySet extends IndexedImmutableSet<Multiset.Entry<E>> {
        private static final long serialVersionUID = 0;
        final /* synthetic */ ImmutableMultiset this$0;

        private EntrySet(ImmutableMultiset r1) {
            this.this$0 = r1;
        }

        /* synthetic */ EntrySet(ImmutableMultiset r1, C06361 r2) {
            this(r1);
        }

        private void readObject(ObjectInputStream r3) throws InvalidObjectException {
            throw new InvalidObjectException("Use EntrySetSerializedForm");
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Multiset.Entry) == false) goto L11;
            Multiset.Entry r0 = (Multiset.Entry) r5;
            if (r0.getCount() > 0) goto L8;
            return false;
        L8:
            if (this.this$0.count(r0.getElement()) != r0.getCount()) goto L12;
            return true;
        L12:
            return false;
        L11:
            return false;
        }

        @Override // com.google.common.collect.IndexedImmutableSet
        Multiset.Entry<E> get(int r2) {
            return this.this$0.getEntry(r2);
        }

        @Override // com.google.common.collect.IndexedImmutableSet
        /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return this.this$0.hashCode();
        }

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return this.this$0.isPartialView();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.elementSet().size();
        }

        @Override // com.google.common.collect.IndexedImmutableSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new EntrySetSerializedForm(this.this$0);
        }
    }

    static class EntrySetSerializedForm<E> implements Serializable {
        final ImmutableMultiset<E> multiset;

        EntrySetSerializedForm(ImmutableMultiset<E> r1) {
            this.multiset = r1;
        }

        Object readResolve() {
            return this.multiset.entrySet();
        }
    }

    ImmutableMultiset() {
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    private static <E> ImmutableMultiset<E> copyFromElements(E... r1) {
        return new Builder().add(r1).build();
    }

    static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> r5) {
        Builder r0 = new Builder(r5.size());
        Iterator<? extends Multiset.Entry<? extends E>> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        Multiset.Entry<? extends E> r2 = r1.next();
        r0.addCopies(r2.getElement(), r2.getCount());
        goto L4
    L7:
        return r0.build();
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> r2) {
        if ((r2 instanceof ImmutableMultiset) == false) goto L7;
        ImmutableMultiset<E> r0 = (ImmutableMultiset) r2;
        if (r0.isPartialView() == true) goto L7;
        return r0;
    L7:
        Builder r1 = new Builder(Multisets.inferDistinctElements(r2));
        r1.addAll(r2);
        return r1.build();
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> r1) {
        return new Builder().addAll(r1).build();
    }

    public static <E> ImmutableMultiset<E> copyOf(E[] r1) {
        return copyFromElements(r1);
    }

    private ImmutableSet<Multiset.Entry<E>> createEntrySet() {
        if (isEmpty() == false) goto L6;
        return ImmutableSet.m259of();
    L6:
        return new EntrySet(this, null);
    }

    static /* synthetic */ int lambda$toImmutableMultiset$0(Object r1) {
        return 1;
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m248of() {
        return RegularImmutableMultiset.EMPTY;
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m249of(E r2) {
        return copyFromElements(new Object[]{r2});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m250of(E r2, E r3) {
        return copyFromElements(new Object[]{r2, r3});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m251of(E r2, E r3, E r4) {
        return copyFromElements(new Object[]{r2, r3, r4});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m252of(E r2, E r3, E r4, E r5) {
        return copyFromElements(new Object[]{r2, r3, r4, r5});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m253of(E r2, E r3, E r4, E r5, E r6) {
        return copyFromElements(new Object[]{r2, r3, r4, r5, r6});
    }

    /* JADX INFO: renamed from: of */
    public static <E> ImmutableMultiset<E> m254of(E r1, E r2, E r3, E r4, E r5, E r6, E... r7) {
        return new Builder().add(r1).add(r2).add(r3).add(r4).add(r5).add(r6).add(r7).build();
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
        return CollectCollectors.toImmutableMultiset(Function.identity(), new ImmutableMultiset$$ExternalSyntheticLambda0());
    }

    public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> r1, ToIntFunction<? super T> r2) {
        return CollectCollectors.toImmutableMultiset(r1, r2);
    }

    @Override // com.google.common.collect.Multiset
    @Deprecated
    public final int add(E r2, int r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<E> asList() {
        ImmutableList<E> r0 = this.asList;
        if (r0 != null) goto L6;
        ImmutableList<E> r1 = super.asList();
        this.asList = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@CheckForNull Object r2) {
        if (count(r2) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    int copyIntoArray(Object[] r5, int r6) {
        UnmodifiableIterator<Multiset.Entry<E>> r0 = entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Multiset.Entry<E> r1 = r0.next();
        Arrays.fill(r5, r6, r1.getCount() + r6, r1.getElement());
        r6 = r6 + r1.getCount();
        goto L4
    L6:
        return r6;
    }

    public abstract ImmutableSet<E> elementSet();

    public /* bridge */ /* synthetic */ Set elementSet() {
        return elementSet();
    }

    @Override // com.google.common.collect.Multiset
    public ImmutableSet<Multiset.Entry<E>> entrySet() {
        ImmutableSet<Multiset.Entry<E>> r0 = this.entrySet;
        if (r0 != null) goto L6;
        ImmutableSet<Multiset.Entry<E>> r1 = createEntrySet();
        this.entrySet = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.Multiset
    public /* bridge */ /* synthetic */ Set entrySet() {
        return entrySet();
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public boolean equals(@CheckForNull Object r2) {
        return Multisets.equalsImpl(this, r2);
    }

    abstract Multiset.Entry<E> getEntry(int r1);

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<E> iterator() {
        final UnmodifiableIterator<Multiset.Entry<E>> r0 = entrySet().iterator();
        return new C06361(this, r0);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return iterator();
    }

    @Override // com.google.common.collect.Multiset
    @Deprecated
    public final int remove(@CheckForNull Object r2, int r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multiset
    @Deprecated
    public final int setCount(E r2, int r3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multiset
    @Deprecated
    public final boolean setCount(E r2, int r3, int r4) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, com.google.common.collect.Multiset
    public String toString() {
        return entrySet().toString();
    }

    @Override // com.google.common.collect.ImmutableCollection
    abstract Object writeReplace();
}
