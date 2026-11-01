package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class TreeRangeMap<K extends Comparable, V> implements RangeMap<K, V> {
    private static final RangeMap<Comparable<?>, Object> EMPTY_SUB_RANGE_MAP = null;
    private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;

    private final class AsMapOfRanges extends Maps.IteratorBasedAbstractMap<Range<K>, V> {
        final Iterable<Map.Entry<Range<K>, V>> entryIterable;
        final /* synthetic */ TreeRangeMap this$0;

        AsMapOfRanges(TreeRangeMap r1, Iterable<RangeMapEntry<K, V>> r2) {
            this.this$0 = r1;
            this.entryIterable = r2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@CheckForNull Object r2) {
            if (get(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        Iterator<Map.Entry<Range<K>, V>> entryIterator() {
            return this.entryIterable.iterator();
        }

        @Override // java.util.AbstractMap, java.util.Map
        @CheckForNull
        public V get(@CheckForNull Object r4) {
            if ((r4 instanceof Range) == false) goto L10;
            Range r0 = (Range) r4;
            RangeMapEntry r1 = (RangeMapEntry) TreeRangeMap.access$000(this.this$0).get(r0.lowerBound);
            if (r1 != null) goto L7;
            return null;
        L7:
            if (r1.getKey().equals(r0) == true) goto L9;
            return null;
        L9:
            return (V) r1.getValue();
        L10:
            return null;
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return TreeRangeMap.access$000(this.this$0).size();
        }
    }

    private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V> {
        private final Range<K> range;
        private final V value;

        RangeMapEntry(Cut<K> r2, Cut<K> r3, V r4) {
            this(Range.create(r2, r3), r4);
        }

        RangeMapEntry(Range<K> r1, V r2) {
            this.range = r1;
            this.value = r2;
        }

        public boolean contains(K r2) {
            return this.range.contains(r2);
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public Range<K> getKey() {
            return this.range;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public /* bridge */ /* synthetic */ Object getKey() {
            return getKey();
        }

        Cut<K> getLowerBound() {
            return (Cut<K>) this.range.lowerBound;
        }

        Cut<K> getUpperBound() {
            return (Cut<K>) this.range.upperBound;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V getValue() {
            return this.value;
        }
    }

    private class SubRangeMap implements RangeMap<K, V> {
        private final Range<K> subRange;
        final /* synthetic */ TreeRangeMap this$0;

        class SubRangeMapAsMap extends AbstractMap<Range<K>, V> {
            final /* synthetic */ SubRangeMap this$1;

            SubRangeMapAsMap(SubRangeMap r1) {
                this.this$1 = r1;
            }

            static /* synthetic */ boolean access$400(SubRangeMapAsMap r1, Predicate r2) {
                return r1.removeEntryIf(r2);
            }

            private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> r5) {
                ArrayList r0 = Lists.newArrayList();
                Iterator<Map.Entry<Range<K>, V>> r1 = entrySet().iterator();
            L4:
                if (r1.hasNext() == false) goto L9;
                Map.Entry<Range<K>, V> r2 = r1.next();
                if (r5.apply(r2) == false) goto L4;
                r0.add(r2.getKey());
                goto L4
            L9:
                Iterator r3 = r0.iterator();
            L11:
                if (r3.hasNext() == false) goto L14;
                this.this$1.this$0.remove((Range) r3.next());
                goto L11
            L14:
                return !r0.isEmpty();
            }

            @Override // java.util.AbstractMap, java.util.Map
            public void clear() {
                this.this$1.clear();
            }

            @Override // java.util.AbstractMap, java.util.Map
            public boolean containsKey(@CheckForNull Object r2) {
                if (get(r2) == null) goto L5;
                return true;
            L5:
                return false;
            }

            Iterator<Map.Entry<Range<K>, V>> entryIterator() {
                if (SubRangeMap.access$300(this.this$1).isEmpty() == true) goto L5;
                Cut r0 = (Cut) MoreObjects.firstNonNull((Cut) TreeRangeMap.access$000(this.this$1.this$0).floorKey(SubRangeMap.access$300(this.this$1).lowerBound), SubRangeMap.access$300(this.this$1).lowerBound);
                final Iterator<V> r1 = TreeRangeMap.access$000(this.this$1.this$0).tailMap(r0, true).values().iterator();
                return new C07683(this, r1);
            L5:
                return Iterators.emptyIterator();
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Set<Map.Entry<Range<K>, V>> entrySet() {
                return new C07672(this);
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
            @Override // java.util.AbstractMap, java.util.Map
            @CheckForNull
            public V get(@CheckForNull Object r6) {
                if ((r6 instanceof Range) == false) goto L26;
                Range r1 = (Range) r6;     // Catch: ClassCastException -> L27
                if (SubRangeMap.access$300(this.this$1).encloses(r1) == true) goto L8;
            L24:
                return null;
            L8:
                if (r1.isEmpty() == true) goto L24;
                RangeMapEntry r2 = null;
                if (r1.lowerBound.compareTo(SubRangeMap.access$300(this.this$1).lowerBound) != 0) goto L16;
                Map.Entry r3 = TreeRangeMap.access$000(this.this$1.this$0).floorEntry(r1.lowerBound);     // Catch: ClassCastException -> L27
                if (r3 == null) goto L17;
                r2 = (RangeMapEntry) r3.getValue();     // Catch: ClassCastException -> L27
            L17:
                if (r2 == null) goto L26;
                if (r2.getKey().isConnected(SubRangeMap.access$300(this.this$1)) == false) goto L26;
                if (r2.getKey().intersection(SubRangeMap.access$300(this.this$1)).equals(r1) == false) goto L26;
                return (V) r2.getValue();
            L16:
                r2 = (RangeMapEntry) TreeRangeMap.access$000(this.this$1.this$0).get(r1.lowerBound);     // Catch: ClassCastException -> L27
            L26:
                return null;
            L28:
                return null;
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Set<Range<K>> keySet() {
                return new C07661(this, this);
            }

            @Override // java.util.AbstractMap, java.util.Map
            @CheckForNull
            public V remove(@CheckForNull Object r4) {
                V r0 = (V) get(r4);
                if (r0 == null) goto L6;
                Range<K> r1 = (Range) Objects.requireNonNull(r4);
                this.this$1.this$0.remove(r1);
                return r0;
            L6:
                return null;
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Collection<V> values() {
                return new C07694(this, this);
            }
        }

        SubRangeMap(TreeRangeMap r1, Range<K> r2) {
            this.this$0 = r1;
            this.subRange = r2;
        }

        static /* synthetic */ Range access$300(SubRangeMap r1) {
            return r1.subRange;
        }

        @Override // com.google.common.collect.RangeMap
        public Map<Range<K>, V> asDescendingMapOfRanges() {
            return new C07651(this);
        }

        @Override // com.google.common.collect.RangeMap
        public Map<Range<K>, V> asMapOfRanges() {
            return new SubRangeMapAsMap(this);
        }

        @Override // com.google.common.collect.RangeMap
        public void clear() {
            this.this$0.remove(this.subRange);
        }

        @Override // com.google.common.collect.RangeMap
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof RangeMap) == true) goto L5;
            return false;
        L5:
            return asMapOfRanges().equals(((RangeMap) r4).asMapOfRanges());
        }

        @Override // com.google.common.collect.RangeMap
        @CheckForNull
        public V get(K r2) {
            if (this.subRange.contains(r2) == true) goto L7;
            return null;
        L7:
            return (V) this.this$0.get(r2);
        }

        @Override // com.google.common.collect.RangeMap
        @CheckForNull
        public Map.Entry<Range<K>, V> getEntry(K r4) {
            if (this.subRange.contains(r4) == false) goto L8;
            Map.Entry<Range<K>, V> r0 = this.this$0.getEntry(r4);
            if (r0 != null) goto L7;
            return null;
        L7:
            return Maps.immutableEntry(r0.getKey().intersection(this.subRange), r0.getValue());
        L8:
            return null;
        }

        @Override // com.google.common.collect.RangeMap
        public int hashCode() {
            return asMapOfRanges().hashCode();
        }

        @Override // com.google.common.collect.RangeMap
        public void put(Range<K> r4, V r5) {
            Preconditions.checkArgument(this.subRange.encloses(r4), "Cannot put range %s into a subRangeMap(%s)", r4, this.subRange);
            this.this$0.put(r4, r5);
        }

        @Override // com.google.common.collect.RangeMap
        public void putAll(RangeMap<K, ? extends V> r5) {
            if (r5.asMapOfRanges().isEmpty() == false) goto L5;
            return;
        L5:
            Range<K> r0 = r5.span();
            Preconditions.checkArgument(this.subRange.encloses(r0), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", r0, this.subRange);
            this.this$0.putAll(r5);
        }

        @Override // com.google.common.collect.RangeMap
        public void putCoalescing(Range<K> r3, V r4) {
            if (TreeRangeMap.access$000(this.this$0).isEmpty() == false) goto L5;
        L9:
            put(r3, r4);
            return;
        L5:
            if (this.subRange.encloses(r3) == false) goto L9;
            put(TreeRangeMap.access$100(this.this$0, r3, Preconditions.checkNotNull(r4)).intersection(this.subRange), r4);
        }

        /* JADX WARN: Multi-variable type inference failed */
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
        @Override // com.google.common.collect.RangeMap
        public void remove(Range<K> r3) {
            if (r3.isConnected(this.subRange) == false) goto L6;
            this.this$0.remove(r3.intersection(this.subRange));
            return;
        }

        @Override // com.google.common.collect.RangeMap
        public Range<K> span() {
            Map.Entry r0 = TreeRangeMap.access$000(this.this$0).floorEntry(this.subRange.lowerBound);
            if (r0 != null) goto L5;
        L7:
            Cut r1 = (Cut) TreeRangeMap.access$000(this.this$0).ceilingKey(this.subRange.lowerBound);
            if (r1 == null) goto L22;
            if (r1.compareTo(this.subRange.upperBound) >= 0) goto L22;
        L11:
            Map.Entry r2 = TreeRangeMap.access$000(this.this$0).lowerEntry(this.subRange.upperBound);
            if (r2 == null) goto L20;
            if (((RangeMapEntry) r2.getValue()).getUpperBound().compareTo(this.subRange.upperBound) < 0) goto L16;
            Cut r3 = this.subRange.upperBound;
        L18:
            return Range.create(r1, r3);
        L16:
            r3 = ((RangeMapEntry) r2.getValue()).getUpperBound();
            goto L18
        L20:
            throw new NoSuchElementException();
        L22:
            throw new NoSuchElementException();
        L5:
            if (((RangeMapEntry) r0.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) <= 0) goto L7;
            r1 = this.subRange.lowerBound;
            goto L11
        }

        /* JADX WARN: Multi-variable type inference failed */
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
        @Override // com.google.common.collect.RangeMap
        public RangeMap<K, V> subRangeMap(Range<K> r3) {
            if (r3.isConnected(this.subRange) == true) goto L7;
            return TreeRangeMap.access$200(this.this$0);
        L7:
            return this.this$0.subRangeMap(r3.intersection(this.subRange));
        }

        @Override // com.google.common.collect.RangeMap
        public String toString() {
            return asMapOfRanges().toString();
        }
    }

    static {
        EMPTY_SUB_RANGE_MAP = new C07641();
    }

    private TreeRangeMap() {
        this.entriesByLowerBound = Maps.newTreeMap();
    }

    static /* synthetic */ NavigableMap access$000(TreeRangeMap r1) {
        return r1.entriesByLowerBound;
    }

    static /* synthetic */ Range access$100(TreeRangeMap r1, Range r2, Object r3) {
        return r1.coalescedRange(r2, r3);
    }

    static /* synthetic */ RangeMap access$200(TreeRangeMap r1) {
        return r1.emptySubRangeMap();
    }

    private static <K extends Comparable, V> Range<K> coalesce(Range<K> r1, V r2, @CheckForNull Map.Entry<Cut<K>, RangeMapEntry<K, V>> r3) {
        if (r3 != null) goto L4;
    L9:
        return r1;
    L4:
        if (r3.getValue().getKey().isConnected(r1) == false) goto L9;
        if (r3.getValue().getValue().equals(r2) == false) goto L9;
        return (Range<K>) r1.span(r3.getValue().getKey());
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    private Range<K> coalescedRange(Range<K> r5, V r6) {
        return coalesce(coalesce(r5, r6, this.entriesByLowerBound.lowerEntry((Cut<K>) r5.lowerBound)), r6, this.entriesByLowerBound.floorEntry((Cut<K>) r5.upperBound));
    }

    public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
        return new TreeRangeMap();
    }

    private RangeMap<K, V> emptySubRangeMap() {
        return EMPTY_SUB_RANGE_MAP;
    }

    private void putRangeMapEntry(Cut<K> r3, Cut<K> r4, V r5) {
        this.entriesByLowerBound.put(r3, new RangeMapEntry(r3, r4, r5));
    }

    @Override // com.google.common.collect.RangeMap
    public Map<Range<K>, V> asDescendingMapOfRanges() {
        return new AsMapOfRanges(this, this.entriesByLowerBound.descendingMap().values());
    }

    @Override // com.google.common.collect.RangeMap
    public Map<Range<K>, V> asMapOfRanges() {
        return new AsMapOfRanges(this, this.entriesByLowerBound.values());
    }

    @Override // com.google.common.collect.RangeMap
    public void clear() {
        this.entriesByLowerBound.clear();
    }

    @Override // com.google.common.collect.RangeMap
    public boolean equals(@CheckForNull Object r4) {
        if ((r4 instanceof RangeMap) == true) goto L5;
        return false;
    L5:
        return asMapOfRanges().equals(((RangeMap) r4).asMapOfRanges());
    }

    @Override // com.google.common.collect.RangeMap
    @CheckForNull
    public V get(K r3) {
        Map.Entry<Range<K>, V> r0 = getEntry(r3);
        if (r0 != null) goto L6;
        return null;
    L6:
        return r0.getValue();
    }

    @Override // com.google.common.collect.RangeMap
    @CheckForNull
    public Map.Entry<Range<K>, V> getEntry(K r3) {
        Map.Entry<Cut<K>, RangeMapEntry<K, V>> r0 = this.entriesByLowerBound.floorEntry(Cut.belowValue(r3));
        if (r0 != null) goto L5;
        return null;
    L5:
        if (r0.getValue().contains(r3) == true) goto L7;
        return null;
    L7:
        return r0.getValue();
    }

    @Override // com.google.common.collect.RangeMap
    public int hashCode() {
        return asMapOfRanges().hashCode();
    }

    @Override // com.google.common.collect.RangeMap
    public void put(Range<K> r4, V r5) {
        if (r4.isEmpty() == true) goto L6;
        Preconditions.checkNotNull(r5);
        remove(r4);
        this.entriesByLowerBound.put(r4.lowerBound, new RangeMapEntry(r4, r5));
        return;
    }

    @Override // com.google.common.collect.RangeMap
    public void putAll(RangeMap<K, ? extends V> r5) {
        Iterator<Map.Entry<Range<K>, ? extends V>> r0 = r5.asMapOfRanges().entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<Range<K>, ? extends V> r1 = r0.next();
        put(r1.getKey(), r1.getValue());
        goto L4
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.RangeMap
    public void putCoalescing(Range<K> r2, V r3) {
        if (this.entriesByLowerBound.isEmpty() == false) goto L6;
        put(r2, r3);
        return;
    L6:
        put(coalescedRange(r2, Preconditions.checkNotNull(r3)), r3);
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.google.common.collect.RangeMap
    public void remove(Range<K> r7) {
        if (r7.isEmpty() == false) goto L5;
        return;
    L5:
        Map.Entry<Cut<K>, RangeMapEntry<K, V>> r0 = this.entriesByLowerBound.lowerEntry((Cut<K>) r7.lowerBound);
        if (r0 == null) goto L13;
        RangeMapEntry<K, V> r1 = r0.getValue();
        if (r1.getUpperBound().compareTo(r7.lowerBound) <= 0) goto L13;
        if (r1.getUpperBound().compareTo(r7.upperBound) <= 0) goto L12;
        putRangeMapEntry(r7.upperBound, r1.getUpperBound(), r0.getValue().getValue());
    L12:
        putRangeMapEntry(r1.getLowerBound(), r7.lowerBound, r0.getValue().getValue());
    L13:
        Map.Entry<Cut<K>, RangeMapEntry<K, V>> r2 = this.entriesByLowerBound.lowerEntry((Cut<K>) r7.upperBound);
        if (r2 == null) goto L18;
        RangeMapEntry<K, V> r3 = r2.getValue();
        if (r3.getUpperBound().compareTo(r7.upperBound) <= 0) goto L18;
        putRangeMapEntry(r7.upperBound, r3.getUpperBound(), r2.getValue().getValue());
    L18:
        NavigableMap<Cut<K>, RangeMapEntry<K, V>> r4 = this.entriesByLowerBound;
        Cut<C> r5 = r7.lowerBound;
        r4.subMap((Cut<K>) r5, (Cut<K>) r7.upperBound).clear();
    }

    @Override // com.google.common.collect.RangeMap
    public Range<K> span() {
        Map.Entry<Cut<K>, RangeMapEntry<K, V>> r0 = this.entriesByLowerBound.firstEntry();
        Map.Entry<Cut<K>, RangeMapEntry<K, V>> r1 = this.entriesByLowerBound.lastEntry();
        if (r0 == null) goto L8;
        if (r1 == null) goto L8;
        return Range.create(r0.getValue().getKey().lowerBound, r1.getValue().getKey().upperBound);
    L8:
        throw new NoSuchElementException();
    }

    @Override // com.google.common.collect.RangeMap
    public RangeMap<K, V> subRangeMap(Range<K> r2) {
        if (r2.equals(Range.all()) == false) goto L6;
        return this;
    L6:
        return new SubRangeMap(this, r2);
    }

    @Override // com.google.common.collect.RangeMap
    public String toString() {
        return this.entriesByLowerBound.values().toString();
    }
}
