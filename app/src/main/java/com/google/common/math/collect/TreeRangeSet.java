package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class TreeRangeSet<C extends Comparable<?>> extends AbstractRangeSet<C> implements Serializable {

    @CheckForNull
    @LazyInit
    private transient Set<Range<C>> asDescendingSetOfRanges;

    @CheckForNull
    @LazyInit
    private transient Set<Range<C>> asRanges;

    @CheckForNull
    @LazyInit
    private transient RangeSet<C> complement;
    final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;

    /* JADX INFO: renamed from: com.google.common.collect.TreeRangeSet$1 */
    static /* synthetic */ class C07701 {
    }

    final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
        final Collection<Range<C>> delegate;
        final /* synthetic */ TreeRangeSet this$0;

        AsRanges(TreeRangeSet r1, Collection<Range<C>> r2) {
            this.this$0 = r1;
            this.delegate = r2;
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        protected Collection<Range<C>> delegate() {
            return this.delegate;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(@CheckForNull Object r2) {
            return Sets.equalsImpl(this, r2);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private final class Complement extends TreeRangeSet<C> {
        final /* synthetic */ TreeRangeSet this$0;

        Complement(TreeRangeSet r2) {
            this.this$0 = r2;
            super(new ComplementRangesByLowerBound(r2.rangesByLowerBound), null);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void add(Range<C> r2) {
            this.this$0.remove(r2);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.RangeSet
        public RangeSet<C> complement() {
            return this.this$0;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public boolean contains(C r2) {
            return !this.this$0.contains(r2);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void remove(Range<C> r2) {
            this.this$0.add(r2);
        }
    }

    private static final class ComplementRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final Range<Cut<C>> complementLowerBoundWindow;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;

        ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> r2) {
            this(r2, Range.all());
        }

        private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> r2, Range<Cut<C>> r3) {
            this.positiveRangesByLowerBound = r2;
            this.positiveRangesByUpperBound = new RangesByUpperBound(r2);
            this.complementLowerBoundWindow = r3;
        }

        static /* synthetic */ Range access$100(ComplementRangesByLowerBound r1) {
            return r1.complementLowerBoundWindow;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> r3) {
            if (this.complementLowerBoundWindow.isConnected(r3) == false) goto L5;
            Range<C> r4 = r3.intersection(this.complementLowerBoundWindow);
            return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, r4);
        L5:
            return ImmutableSortedMap.m272of();
        }

        @Override // java.util.SortedMap
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (get(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            if (this.complementLowerBoundWindow.hasUpperBound() == false) goto L5;
            Cut<C> r0 = (Cut) this.complementLowerBoundWindow.upperEndpoint();
        L7:
            if (this.complementLowerBoundWindow.hasUpperBound() == true) goto L9;
        L11:
            boolean r1 = false;
        L12:
            final PeekingIterator r2 = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(r0, r1).descendingMap().values().iterator());
            if (r2.hasNext() == false) goto L20;
            if (((Range) r2.peek()).upperBound != Cut.aboveAll()) goto L17;
            Cut<C> r3 = ((Range) r2.next()).lowerBound;
        L25:
            final Cut r4 = (Cut) MoreObjects.firstNonNull(r3, Cut.aboveAll());
            return new C07722(this, r4, r2);
        L17:
            r3 = this.positiveRangesByLowerBound.higherKey(((Range) r2.peek()).upperBound);
            goto L25
        L20:
            if (this.complementLowerBoundWindow.contains(Cut.belowAll()) == false) goto L28;
            if (this.positiveRangesByLowerBound.containsKey(Cut.belowAll()) == true) goto L28;
            r3 = this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
        L28:
            return Iterators.emptyIterator();
        L9:
            if (this.complementLowerBoundWindow.upperBoundType() != BoundType.CLOSED) goto L11;
            r1 = true;
            goto L12
        L5:
            r0 = Cut.aboveAll();
            goto L7
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            if (this.complementLowerBoundWindow.hasLowerBound() == false) goto L9;
            NavigableMap<Cut<C>, Range<C>> r0 = this.positiveRangesByUpperBound;
            Cut<C> r1 = (Cut) this.complementLowerBoundWindow.lowerEndpoint();
            if (this.complementLowerBoundWindow.lowerBoundType() != BoundType.CLOSED) goto L7;
            boolean r2 = true;
        L8:
            Collection<Range<C>> r3 = r0.tailMap(r1, r2).values();
        L10:
            final PeekingIterator r4 = Iterators.peekingIterator(r3.iterator());
            if (this.complementLowerBoundWindow.contains(Cut.belowAll()) == false) goto L18;
            if (r4.hasNext() == true) goto L15;
        L16:
            final Cut r5 = Cut.belowAll();
        L21:
            return new C07711(this, r5, r4);
        L15:
            if (((Range) r4.peek()).lowerBound != Cut.belowAll()) goto L16;
        L18:
            if (r4.hasNext() == false) goto L23;
            r5 = ((Range) r4.next()).upperBound;
            goto L21
        L23:
            return Iterators.emptyIterator();
        L7:
            r2 = false;
            goto L8
        L9:
            r3 = this.positiveRangesByUpperBound.values();
            goto L10
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Range<C> get(@CheckForNull Object r5) {
            if ((r5 instanceof Cut) == true) goto L14;
        L13:
            return null;
        L14:
            Cut<C> r0 = (Cut) r5;     // Catch: ClassCastException -> L11
            Map.Entry<Cut<C>, Range<C>> r2 = tailMap(r0, true).firstEntry();     // Catch: ClassCastException -> L11
            if (r2 == null) goto L13;
            if (r2.getKey().equals(r0) == false) goto L13;
            return r2.getValue();
        L12:
            return null;
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> r2, boolean r3) {
            return subMap(Range.upTo(r2, BoundType.forBoolean(r3)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object r1, boolean r2) {
            return headMap((Cut) r1, r2);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return Iterators.size(entryIterator());
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> r3, boolean r4, Cut<C> r5, boolean r6) {
            return subMap(Range.range(r3, BoundType.forBoolean(r4), r5, BoundType.forBoolean(r6)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object r1, boolean r2, Object r3, boolean r4) {
            return subMap((Cut) r1, r2, (Cut) r3, r4);
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> r2, boolean r3) {
            return subMap(Range.downTo(r2, BoundType.forBoolean(r3)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object r1, boolean r2) {
            return tailMap((Cut) r1, r2);
        }
    }

    static final class RangesByUpperBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final Range<Cut<C>> upperBoundWindow;

        RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> r2) {
            this.rangesByLowerBound = r2;
            this.upperBoundWindow = Range.all();
        }

        private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> r1, Range<Cut<C>> r2) {
            this.rangesByLowerBound = r1;
            this.upperBoundWindow = r2;
        }

        static /* synthetic */ Range access$000(RangesByUpperBound r1) {
            return r1.upperBoundWindow;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> r4) {
            if (r4.isConnected(this.upperBoundWindow) == false) goto L7;
            return new RangesByUpperBound(this.rangesByLowerBound, r4.intersection(this.upperBoundWindow));
        L7:
            return ImmutableSortedMap.m272of();
        }

        @Override // java.util.SortedMap
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (get(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            if (this.upperBoundWindow.hasUpperBound() == false) goto L5;
            Collection<Range<C>> r0 = this.rangesByLowerBound.headMap((Cut) this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
        L6:
            final PeekingIterator r1 = Iterators.peekingIterator(r0.iterator());
            if (r1.hasNext() == false) goto L12;
            if (this.upperBoundWindow.upperBound.isLessThan(((Range) r1.peek()).upperBound) == false) goto L12;
            r1.next();
        L12:
            return new C07742(this, r1);
        L5:
            r0 = this.rangesByLowerBound.descendingMap().values();
            goto L6
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            if (this.upperBoundWindow.hasLowerBound() == true) goto L5;
            final Iterator<Range<C>> r0 = this.rangesByLowerBound.values().iterator();
        L13:
            return new C07731(this, r0);
        L5:
            Map.Entry<Cut<C>, Range<C>> r1 = this.rangesByLowerBound.lowerEntry((Cut) this.upperBoundWindow.lowerEndpoint());
            if (r1 != null) goto L9;
            r0 = this.rangesByLowerBound.values().iterator();
            goto L13
        L9:
            if (this.upperBoundWindow.lowerBound.isLessThan(r1.getValue().upperBound) == false) goto L11;
            r0 = this.rangesByLowerBound.tailMap(r1.getKey(), true).values().iterator();
            goto L13
        L11:
            r0 = this.rangesByLowerBound.tailMap((Cut) this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
            goto L13
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Range<C> get(@CheckForNull Object r5) {
            if ((r5 instanceof Cut) == true) goto L17;
        L16:
            return null;
        L17:
            Cut<C> r0 = (Cut) r5;     // Catch: ClassCastException -> L14
            if (this.upperBoundWindow.contains(r0) == true) goto L7;
            return null;
        L7:
            Map.Entry<Cut<C>, Range<C>> r2 = this.rangesByLowerBound.lowerEntry(r0);     // Catch: ClassCastException -> L14
            if (r2 == null) goto L16;
            if (r2.getValue().upperBound.equals(r0) == false) goto L16;
            return r2.getValue();
        L15:
            return null;
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> r2, boolean r3) {
            return subMap(Range.upTo(r2, BoundType.forBoolean(r3)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object r1, boolean r2) {
            return headMap((Cut) r1, r2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            if (this.upperBoundWindow.equals(Range.all()) == false) goto L6;
            return this.rangesByLowerBound.isEmpty();
        L6:
            if (entryIterator().hasNext() == true) goto L8;
            return true;
        L8:
            return false;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            if (this.upperBoundWindow.equals(Range.all()) == false) goto L7;
            return this.rangesByLowerBound.size();
        L7:
            return Iterators.size(entryIterator());
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> r3, boolean r4, Cut<C> r5, boolean r6) {
            return subMap(Range.range(r3, BoundType.forBoolean(r4), r5, BoundType.forBoolean(r6)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object r1, boolean r2, Object r3, boolean r4) {
            return subMap((Cut) r1, r2, (Cut) r3, r4);
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> r2, boolean r3) {
            return subMap(Range.downTo(r2, BoundType.forBoolean(r3)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object r1, boolean r2) {
            return tailMap((Cut) r1, r2);
        }
    }

    private final class SubRangeSet extends TreeRangeSet<C> {
        private final Range<C> restriction;
        final /* synthetic */ TreeRangeSet this$0;

        SubRangeSet(TreeRangeSet r4, Range<C> r5) {
            this.this$0 = r4;
            C07701 r2 = null;
            super(new SubRangeSetRangesByLowerBound(Range.all(), r5, r4.rangesByLowerBound, r2), r2);
            this.restriction = r5;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void add(Range<C> r4) {
            Preconditions.checkArgument(this.restriction.encloses(r4), "Cannot add range %s to subRangeSet(%s)", r4, this.restriction);
            this.this$0.add(r4);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void clear() {
            this.this$0.remove(this.restriction);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public boolean contains(C r2) {
            if (this.restriction.contains(r2) == true) goto L5;
        L7:
            return false;
        L5:
            if (this.this$0.contains(r2) == false) goto L7;
            return true;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public boolean encloses(Range<C> r4) {
            if (this.restriction.isEmpty() == false) goto L5;
        L12:
            return false;
        L5:
            if (this.restriction.encloses(r4) == false) goto L12;
            Range r0 = TreeRangeSet.access$600(this.this$0, r4);
            if (r0 != null) goto L9;
            return false;
        L9:
            if (r0.intersection(this.restriction).isEmpty() == true) goto L14;
            return true;
        L14:
            return false;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        @CheckForNull
        public Range<C> rangeContaining(C r3) {
            if (this.restriction.contains(r3) == true) goto L5;
            return null;
        L5:
            Range<C> r0 = this.this$0.rangeContaining(r3);
            if (r0 != null) goto L9;
            return null;
        L9:
            return r0.intersection(this.restriction);
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
        public void remove(Range<C> r3) {
            if (r3.isConnected(this.restriction) == false) goto L6;
            this.this$0.remove(r3.intersection(this.restriction));
            return;
        }

        @Override // com.google.common.collect.TreeRangeSet, com.google.common.collect.RangeSet
        public RangeSet<C> subRangeSet(Range<C> r3) {
            if (r3.encloses(this.restriction) == false) goto L6;
            return this;
        L6:
            if (r3.isConnected(this.restriction) == false) goto L10;
            return new SubRangeSet(this, this.restriction.intersection(r3));
        L10:
            return ImmutableRangeSet.m257of();
        }
    }

    private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final Range<Cut<C>> lowerBoundWindow;
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
        private final Range<C> restriction;

        private SubRangeSetRangesByLowerBound(Range<Cut<C>> r2, Range<C> r3, NavigableMap<Cut<C>, Range<C>> r4) {
            this.lowerBoundWindow = (Range) Preconditions.checkNotNull(r2);
            this.restriction = (Range) Preconditions.checkNotNull(r3);
            this.rangesByLowerBound = (NavigableMap) Preconditions.checkNotNull(r4);
            this.rangesByUpperBound = new RangesByUpperBound(r4);
        }

        /* synthetic */ SubRangeSetRangesByLowerBound(Range r1, Range r2, NavigableMap r3, C07701 r4) {
            this(r1, r2, r3);
        }

        static /* synthetic */ Range access$300(SubRangeSetRangesByLowerBound r1) {
            return r1.restriction;
        }

        static /* synthetic */ Range access$400(SubRangeSetRangesByLowerBound r1) {
            return r1.lowerBoundWindow;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> r5) {
            if (r5.isConnected(this.lowerBoundWindow) == true) goto L7;
            return ImmutableSortedMap.m272of();
        L7:
            return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(r5), this.restriction, this.rangesByLowerBound);
        }

        @Override // java.util.SortedMap
        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (get(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.AbstractNavigableMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            if (this.restriction.isEmpty() == true) goto L5;
            Cut r0 = (Cut) Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            NavigableMap<Cut<C>, Range<C>> r1 = this.rangesByLowerBound;
            Cut<C> r2 = (Cut) r0.endpoint();
            if (r0.typeAsUpperBound() != BoundType.CLOSED) goto L9;
            boolean r3 = true;
        L10:
            final Iterator<Range<C>> r4 = r1.headMap(r2, r3).descendingMap().values().iterator();
            return new C07762(this, r4);
        L9:
            r3 = false;
            goto L10
        L5:
            return Iterators.emptyIterator();
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
            if (this.restriction.isEmpty() == false) goto L7;
            return Iterators.emptyIterator();
        L7:
            if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound) == true) goto L9;
            boolean r1 = false;
            if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound) == false) goto L13;
            final Iterator<Range<C>> r0 = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
        L17:
            final Cut r2 = (Cut) Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            return new C07751(this, r0, r2);
        L13:
            NavigableMap<Cut<C>, Range<C>> r3 = this.rangesByLowerBound;
            Cut<C> r4 = (Cut) this.lowerBoundWindow.lowerBound.endpoint();
            if (this.lowerBoundWindow.lowerBoundType() != BoundType.CLOSED) goto L16;
            r1 = true;
        L16:
            r0 = r3.tailMap(r4, r1).values().iterator();
            goto L17
        L9:
            return Iterators.emptyIterator();
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public Range<C> get(@CheckForNull Object r6) {
            if ((r6 instanceof Cut) == true) goto L29;
        L28:
            return null;
        L29:
            Cut<C> r0 = (Cut) r6;     // Catch: ClassCastException -> L26
            if (this.lowerBoundWindow.contains(r0) == true) goto L7;
        L25:
            return null;
        L7:
            if (r0.compareTo(this.restriction.lowerBound) < 0) goto L25;
            if (r0.compareTo(this.restriction.upperBound) >= 0) goto L25;
            if (r0.equals(this.restriction.lowerBound) == false) goto L20;
            Range r2 = (Range) Maps.valueOrNull(this.rangesByLowerBound.floorEntry(r0));     // Catch: ClassCastException -> L26
            if (r2 == null) goto L28;
            if (r2.upperBound.compareTo(this.restriction.lowerBound) <= 0) goto L28;
            return r2.intersection(this.restriction);
        L20:
            Range r3 = (Range) this.rangesByLowerBound.get(r0);     // Catch: ClassCastException -> L26
            if (r3 == null) goto L28;
            return r3.intersection(this.restriction);
        L27:
            return null;
        }

        @Override // com.google.common.collect.AbstractNavigableMap, java.util.AbstractMap, java.util.Map
        @CheckForNull
        public /* bridge */ /* synthetic */ Object get(@CheckForNull Object r1) {
            return get(r1);
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> r2, boolean r3) {
            return subMap(Range.upTo(r2, BoundType.forBoolean(r3)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap headMap(Object r1, boolean r2) {
            return headMap((Cut) r1, r2);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return Iterators.size(entryIterator());
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> r3, boolean r4, Cut<C> r5, boolean r6) {
            return subMap(Range.range(r3, BoundType.forBoolean(r4), r5, BoundType.forBoolean(r6)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap subMap(Object r1, boolean r2, Object r3, boolean r4) {
            return subMap((Cut) r1, r2, (Cut) r3, r4);
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> r2, boolean r3) {
            return subMap(Range.downTo(r2, BoundType.forBoolean(r3)));
        }

        @Override // java.util.NavigableMap
        public /* bridge */ /* synthetic */ NavigableMap tailMap(Object r1, boolean r2) {
            return tailMap((Cut) r1, r2);
        }
    }

    private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> r1) {
        this.rangesByLowerBound = r1;
    }

    /* synthetic */ TreeRangeSet(NavigableMap r1, C07701 r2) {
        this(r1);
    }

    static /* synthetic */ Range access$600(TreeRangeSet r1, Range r2) {
        return r1.rangeEnclosing(r2);
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create() {
        return new TreeRangeSet(new TreeMap());
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> r1) {
        TreeRangeSet<C> r0 = create();
        r0.addAll(r1);
        return r0;
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> r1) {
        TreeRangeSet<C> r0 = create();
        r0.addAll(r1);
        return r0;
    }

    @CheckForNull
    private Range<C> rangeEnclosing(Range<C> r3) {
        Preconditions.checkNotNull(r3);
        Map.Entry<Cut<C>, Range<C>> r0 = this.rangesByLowerBound.floorEntry(r3.lowerBound);
        if (r0 != null) goto L5;
    L7:
        return null;
    L5:
        if (r0.getValue().encloses(r3) == false) goto L7;
        return r0.getValue();
    }

    private void replaceRangeWithSameLowerBound(Range<C> r3) {
        if (r3.isEmpty() == false) goto L5;
        this.rangesByLowerBound.remove(r3.lowerBound);
        return;
    L5:
        this.rangesByLowerBound.put(r3.lowerBound, r3);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public void add(Range<C> r7) {
        Preconditions.checkNotNull(r7);
        if (r7.isEmpty() == false) goto L5;
        return;
    L5:
        Cut<C> r0 = r7.lowerBound;
        Cut<C> r1 = r7.upperBound;
        Map.Entry<Cut<C>, Range<C>> r2 = this.rangesByLowerBound.lowerEntry(r0);
        if (r2 == null) goto L13;
        Range<C> r3 = r2.getValue();
        if (r3.upperBound.compareTo(r0) < 0) goto L13;
        if (r3.upperBound.compareTo(r1) < 0) goto L12;
        r1 = r3.upperBound;
    L12:
        r0 = r3.lowerBound;
    L13:
        Map.Entry<Cut<C>, Range<C>> r4 = this.rangesByLowerBound.floorEntry(r1);
        if (r4 == null) goto L18;
        Range<C> r5 = r4.getValue();
        if (r5.upperBound.compareTo(r1) < 0) goto L18;
        r1 = r5.upperBound;
    L18:
        this.rangesByLowerBound.subMap(r0, r1).clear();
        replaceRangeWithSameLowerBound(Range.create(r0, r1));
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void addAll(RangeSet r1) {
        super.addAll(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void addAll(Iterable r1) {
        super.addAll(r1);
    }

    @Override // com.google.common.collect.RangeSet
    public Set<Range<C>> asDescendingSetOfRanges() {
        Set<Range<C>> r0 = this.asDescendingSetOfRanges;
        if (r0 != null) goto L6;
        AsRanges r1 = new AsRanges(this, this.rangesByLowerBound.descendingMap().values());
        this.asDescendingSetOfRanges = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.RangeSet
    public Set<Range<C>> asRanges() {
        Set<Range<C>> r0 = this.asRanges;
        if (r0 != null) goto L6;
        AsRanges r1 = new AsRanges(this, this.rangesByLowerBound.values());
        this.asRanges = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.RangeSet
    public RangeSet<C> complement() {
        RangeSet<C> r0 = this.complement;
        if (r0 != null) goto L6;
        Complement r1 = new Complement(this);
        this.complement = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean contains(Comparable r1) {
        return super.contains(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean encloses(Range<C> r3) {
        Preconditions.checkNotNull(r3);
        Map.Entry<Cut<C>, Range<C>> r0 = this.rangesByLowerBound.floorEntry(r3.lowerBound);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.getValue().encloses(r3) == false) goto L7;
        return true;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean enclosesAll(RangeSet r1) {
        return super.enclosesAll(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean enclosesAll(Iterable r1) {
        return super.enclosesAll(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean equals(@CheckForNull Object r1) {
        return super.equals(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public boolean intersects(Range<C> r5) {
        Preconditions.checkNotNull(r5);
        Map.Entry<Cut<C>, Range<C>> r0 = this.rangesByLowerBound.ceilingEntry(r5.lowerBound);
        if (r0 != null) goto L5;
    L9:
        Map.Entry<Cut<C>, Range<C>> r2 = this.rangesByLowerBound.lowerEntry(r5.lowerBound);
        if (r2 != null) goto L12;
    L16:
        return false;
    L12:
        if (r2.getValue().isConnected(r5) == false) goto L16;
        if (r2.getValue().intersection(r5).isEmpty() == true) goto L16;
        return true;
    L5:
        if (r0.getValue().isConnected(r5) == false) goto L9;
        if (r0.getValue().intersection(r5).isEmpty() == true) goto L9;
        return true;
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    @CheckForNull
    public Range<C> rangeContaining(C r3) {
        Preconditions.checkNotNull(r3);
        Map.Entry<Cut<C>, Range<C>> r0 = this.rangesByLowerBound.floorEntry(Cut.belowValue(r3));
        if (r0 != null) goto L5;
        return null;
    L5:
        if (r0.getValue().contains(r3) == true) goto L7;
        return null;
    L7:
        return r0.getValue();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public void remove(Range<C> r6) {
        Preconditions.checkNotNull(r6);
        if (r6.isEmpty() == false) goto L5;
        return;
    L5:
        Map.Entry<Cut<C>, Range<C>> r0 = this.rangesByLowerBound.lowerEntry(r6.lowerBound);
        if (r0 == null) goto L15;
        Range<C> r1 = r0.getValue();
        if (r1.upperBound.compareTo(r6.lowerBound) < 0) goto L15;
        if (r6.hasUpperBound() == true) goto L12;
    L14:
        replaceRangeWithSameLowerBound(Range.create(r1.lowerBound, r6.lowerBound));
        goto L15
    L12:
        if (r1.upperBound.compareTo(r6.upperBound) < 0) goto L14;
        replaceRangeWithSameLowerBound(Range.create(r6.upperBound, r1.upperBound));
    L15:
        Map.Entry<Cut<C>, Range<C>> r2 = this.rangesByLowerBound.floorEntry(r6.upperBound);
        if (r2 == null) goto L22;
        Range<C> r3 = r2.getValue();
        if (r6.hasUpperBound() == false) goto L22;
        if (r3.upperBound.compareTo(r6.upperBound) < 0) goto L22;
        replaceRangeWithSameLowerBound(Range.create(r6.upperBound, r3.upperBound));
    L22:
        this.rangesByLowerBound.subMap(r6.lowerBound, r6.upperBound).clear();
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void removeAll(RangeSet r1) {
        super.removeAll(r1);
    }

    @Override // com.google.common.collect.AbstractRangeSet, com.google.common.collect.RangeSet
    public /* bridge */ /* synthetic */ void removeAll(Iterable r1) {
        super.removeAll(r1);
    }

    @Override // com.google.common.collect.RangeSet
    public Range<C> span() {
        Map.Entry<Cut<C>, Range<C>> r0 = this.rangesByLowerBound.firstEntry();
        Map.Entry<Cut<C>, Range<C>> r1 = this.rangesByLowerBound.lastEntry();
        if (r0 == null) goto L8;
        if (r1 == null) goto L8;
        return Range.create(r0.getValue().lowerBound, r1.getValue().upperBound);
    L8:
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.RangeSet
    public RangeSet<C> subRangeSet(Range<C> r2) {
        if (r2.equals(Range.all()) == false) goto L6;
        return this;
    L6:
        return new SubRangeSet(this, r2);
    }
}
