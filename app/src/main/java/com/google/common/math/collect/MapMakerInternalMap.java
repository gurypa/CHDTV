package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMakerInternalMap.InternalEntry;
import com.google.common.collect.MapMakerInternalMap.Segment;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = null;
    private static final long serialVersionUID = 5;
    final int concurrencyLevel;
    final transient InternalEntryHelper<K, V, E, S> entryHelper;

    @CheckForNull
    @LazyInit
    transient Set<Map.Entry<K, V>> entrySet;
    final Equivalence<Object> keyEquivalence;

    @CheckForNull
    @LazyInit
    transient Set<K> keySet;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V, E, S>[] segments;

    @CheckForNull
    @LazyInit
    transient Collection<V> values;

    static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
        private static final long serialVersionUID = 3;
        final int concurrencyLevel;
        transient ConcurrentMap<K, V> delegate;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;

        AbstractSerializationProxy(Strength r1, Strength r2, Equivalence<Object> r3, Equivalence<Object> r4, int r5, ConcurrentMap<K, V> r6) {
            this.keyStrength = r1;
            this.valueStrength = r2;
            this.keyEquivalence = r3;
            this.valueEquivalence = r4;
            this.concurrencyLevel = r5;
            this.delegate = r6;
        }

        @Override // com.google.common.collect.ForwardingConcurrentMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingConcurrentMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Map delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingConcurrentMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }

        void readEntries(ObjectInputStream r4) throws IOException, ClassNotFoundException {
        L2:
            Object r0 = r4.readObject();
            if (r0 == null) goto L4;
            Object r1 = r4.readObject();
            this.delegate.put((K) r0, (V) r1);
            goto L2
        }

        MapMaker readMapMaker(ObjectInputStream r4) throws IOException {
            int r0 = r4.readInt();
            return new MapMaker().initialCapacity(r0).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
        }

        void writeMapTo(ObjectOutputStream r4) throws IOException {
            r4.writeInt(this.delegate.size());
            Iterator<Map.Entry<K, V>> r0 = this.delegate.entrySet().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            Map.Entry<K, V> r1 = r0.next();
            r4.writeObject(r1.getKey());
            r4.writeObject(r1.getValue());
            goto L4
        L6:
            r4.writeObject(null);
        }
    }

    static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>> implements InternalEntry<K, V, E> {
        final int hash;
        final K key;

        AbstractStrongKeyEntry(K r1, int r2) {
            this.key = r1;
            this.hash = r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @CheckForNull
        public E getNext() {
            return null;
        }
    }

    static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> {
        final int hash;

        AbstractWeakKeyEntry(ReferenceQueue<K> r1, K r2, int r3) {
            super(r2, r1);
            this.hash = r3;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final K getKey() {
            return (K) get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @CheckForNull
        public E getNext() {
            return null;
        }
    }

    static final class CleanupMapTask implements Runnable {
        final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;

        public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> r2) {
            this.mapReference = new WeakReference(r2);
        }

        @Override // java.lang.Runnable
        public void run() {
            MapMakerInternalMap<?, ?, ?, ?> r0 = this.mapReference.get();
            if (r0 == null) goto L9;
            Segment<?, ?, E, S>[] r1 = r0.segments;
            int r2 = r1.length;
            int r3 = 0;
        L5:
            if (r3 >= r2) goto L7;
            r1[r3].runCleanup();
            r3 = r3 + 1;
            goto L5
        L7:
            return;
        L9:
            throw new CancellationException();
        }
    }

    static final class DummyInternalEntry implements InternalEntry<Object, Object, DummyInternalEntry> {
        private DummyInternalEntry() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public int getHash() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public Object getKey() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public DummyInternalEntry getNext() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public /* bridge */ /* synthetic */ InternalEntry getNext() {
            return getNext();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public Object getValue() {
            throw new AssertionError();
        }
    }

    final class EntryIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<Map.Entry<K, V>> {
        final /* synthetic */ MapMakerInternalMap this$0;

        EntryIterator(MapMakerInternalMap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        final /* synthetic */ MapMakerInternalMap this$0;

        EntrySet(MapMakerInternalMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object r7) {
            if ((r7 instanceof Map.Entry) == true) goto L5;
            return false;
        L5:
            Map.Entry r0 = (Map.Entry) r7;
            Object r2 = r0.getKey();
            if (r2 != null) goto L8;
            return false;
        L8:
            Object r3 = this.this$0.get(r2);
            if (r3 != null) goto L11;
            return false;
        L11:
            if (this.this$0.valueEquivalence().equivalent(r0.getValue(), r3) == false) goto L15;
            return true;
        L15:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.this$0.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator(this.this$0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object r6) {
            if ((r6 instanceof Map.Entry) == true) goto L5;
            return false;
        L5:
            Map.Entry r0 = (Map.Entry) r6;
            Object r2 = r0.getKey();
            if (r2 != null) goto L8;
            return false;
        L8:
            if (this.this$0.remove(r2, r0.getValue()) == false) goto L12;
            return true;
        L12:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }
    }

    abstract class HashIterator<T> implements Iterator<T> {

        @CheckForNull
        Segment<K, V, E, S> currentSegment;

        @CheckForNull
        AtomicReferenceArray<E> currentTable;

        @CheckForNull
        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;

        @CheckForNull
        E nextEntry;

        @CheckForNull
        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex;
        final /* synthetic */ MapMakerInternalMap this$0;

        HashIterator(MapMakerInternalMap r2) {
            this.this$0 = r2;
            this.nextSegmentIndex = r2.segments.length - 1;
            this.nextTableIndex = -1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (nextInChain() == false) goto L6;
            return;
        L6:
            if (nextInTable() == false) goto L9;
            return;
        L9:
            if (this.nextSegmentIndex < 0) goto L15;
            Segment<K, V, E, S>[] r0 = this.this$0.segments;
            int r1 = this.nextSegmentIndex;
            this.nextSegmentIndex = r1 - 1;
            this.currentSegment = r0[r1];
            if (this.currentSegment.count == 0) goto L9;
            this.currentTable = this.currentSegment.table;
            this.nextTableIndex = this.currentTable.length() - 1;
            if (nextInTable() == false) goto L9;
            return;
        }

        boolean advanceTo(E r5) {
            Object r0 = r5.getKey();     // Catch: Throwable -> L9
            Object r1 = this.this$0.getLiveValue(r5);     // Catch: Throwable -> L9
            if (r1 == null) goto L7;
            this.nextExternal = new WriteThroughEntry(this.this$0, r0, r1);     // Catch: Throwable -> L9
            this.currentSegment.postReadCleanup();
            return true;
        L7:
            this.currentSegment.postReadCleanup();
            return false;
        L9:
            th = move-exception;
            this.currentSegment.postReadCleanup();
            throw th;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextExternal == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.Iterator
        public abstract T next();

        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) goto L7;
            this.lastReturned = this.nextExternal;
            advance();
            return this.lastReturned;
        L7:
            throw new NoSuchElementException();
        }

        boolean nextInChain() {
            if (this.nextEntry == null) goto L10;
        L4:
            this.nextEntry = (E) this.nextEntry.getNext();
            if (this.nextEntry == null) goto L15;
            if (advanceTo(this.nextEntry) == false) goto L4;
            return true;
        L15:
            return false;
        L10:
            return false;
        }

        boolean nextInTable() {
        L3:
            if (this.nextTableIndex < 0) goto L12;
            AtomicReferenceArray<E> r0 = this.currentTable;
            int r1 = this.nextTableIndex;
            this.nextTableIndex = r1 - 1;
            E r2 = r0.get(r1);
            this.nextEntry = r2;
            if (r2 == null) goto L3;
            if (advanceTo(this.nextEntry) == true) goto L10;
            if (nextInChain() == false) goto L3;
            return true;
        L10:
            return true;
        L12:
            return false;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastReturned == null) goto L5;
            boolean r0 = true;
        L6:
            CollectPreconditions.checkRemove(r0);
            this.this$0.remove(this.lastReturned.getKey());
            this.lastReturned = null;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
        int getHash();

        K getKey();

        E getNext();

        V getValue();
    }

    interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
        E copy(S r1, E r2, @CheckForNull E r3);

        Strength keyStrength();

        E newEntry(S r1, K r2, int r3, @CheckForNull E r4);

        S newSegment(MapMakerInternalMap<K, V, E, S> r1, int r2);

        void setValue(S r1, E r2, V r3);

        Strength valueStrength();
    }

    final class KeyIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<K> {
        final /* synthetic */ MapMakerInternalMap this$0;

        KeyIterator(MapMakerInternalMap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    final class KeySet extends AbstractSet<K> {
        final /* synthetic */ MapMakerInternalMap this$0;

        KeySet(MapMakerInternalMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object r2) {
            return this.this$0.containsKey(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.this$0.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new KeyIterator(this.this$0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object r2) {
            if (this.this$0.remove(r2) == null) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }
    }

    static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends ReentrantLock {
        volatile int count;
        final MapMakerInternalMap<K, V, E, S> map;
        int modCount;
        final AtomicInteger readCount;

        @CheckForNull
        volatile AtomicReferenceArray<E> table;
        int threshold;

        Segment(MapMakerInternalMap<K, V, E, S> r2, int r3) {
            this.readCount = new AtomicInteger();
            this.map = r2;
            initTable(newEntryArray(r3));
        }

        static <K, V, E extends InternalEntry<K, V, E>> boolean isCollected(E r1) {
            if (r1.getValue() != null) goto L5;
            return true;
        L5:
            return false;
        }

        abstract E castForTesting(InternalEntry<K, V, ?> r1);

        void clear() {
            if (this.count == 0) goto L14;
            lock();
            AtomicReferenceArray<E> r0 = this.table;     // Catch: Throwable -> L11
            int r1 = 0;
        L7:
            if (r1 >= r0.length()) goto L9;
            r0.set(r1, null);     // Catch: Throwable -> L11
            r1 = r1 + 1;     // Catch: Throwable -> L11
            goto L7
        L9:
            maybeClearReferenceQueues();     // Catch: Throwable -> L11
            this.readCount.set(0);     // Catch: Throwable -> L11
            this.modCount++;     // Catch: Throwable -> L11
            this.count = 0;     // Catch: Throwable -> L11
            unlock();
            return;
        L11:
            th = move-exception;
            unlock();
            throw th;
        }

        <T> void clearReferenceQueue(ReferenceQueue<T> r2) {
        L3:
            if (r2.poll() != null) goto L3;
        }

        boolean clearValueForTesting(K r9, int r10, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> r11) {
            lock();
            AtomicReferenceArray<E> r0 = this.table;     // Catch: Throwable -> L22
            int r1 = (r0.length() - 1) & r10;     // Catch: Throwable -> L22
            E r3 = r0.get(r1);     // Catch: Throwable -> L22
            InternalEntry r4 = r3;
        L5:
            if (r4 == null) goto L20;
            Object r6 = r4.getKey();     // Catch: Throwable -> L22
            if (r4.getHash() != r10) goto L19;
            if (r6 == null) goto L19;
            if (this.map.keyEquivalence.equivalent(r9, r6) == false) goto L19;
            if (((WeakValueEntry) r4).getValueReference() != r11) goto L16;
            r0.set(r1, (E) removeFromChain(r3, r4));     // Catch: Throwable -> L22
            unlock();
            return true;
        L16:
            unlock();
            return false;
        L19:
            r4 = r4.getNext();     // Catch: Throwable -> L22
            goto L5
        L20:
            unlock();
            return false;
        L22:
            th = move-exception;
            unlock();
            throw th;
        }

        boolean containsKey(Object r4, int r5) {
            boolean r1 = false;
            if (this.count == 0) goto L11;
            InternalEntry r0 = getLiveEntry(r4, r5);     // Catch: Throwable -> L13
            if (r0 == null) goto L9;
            if (r0.getValue() == null) goto L9;
            r1 = true;
        L9:
            postReadCleanup();
            return r1;
        L11:
            postReadCleanup();
            return false;
        L13:
            th = move-exception;
            postReadCleanup();
            throw th;
        }

        boolean containsValue(Object r7) {
        L20:
            th = move-exception;
            postReadCleanup();
            throw th;
        L3:
            if (this.count == 0) goto L18;
            AtomicReferenceArray<E> r0 = this.table;     // Catch: Throwable -> L20
            int r1 = r0.length();     // Catch: Throwable -> L20
            int r2 = 0;
        L5:
            if (r2 >= r1) goto L18;
            E r3 = r0.get(r2);     // Catch: Throwable -> L20
        L7:
            if (r3 == null) goto L17;
            V r4 = getLiveValue(r3);     // Catch: Throwable -> L20
            if (r4 != null) goto L12;
        L16:
            r3 = r3.getNext();     // Catch: Throwable -> L20
            goto L7
        L12:
            if (this.map.valueEquivalence().equivalent(r7, r4) == false) goto L16;
            postReadCleanup();
            return true;
        L17:
            r2 = r2 + 1;
        L18:
            postReadCleanup();
            return false;
        }

        @CheckForNull
        E copyEntry(E r3, E r4) {
            return (E) this.map.entryHelper.copy(self(), r3, r4);
        }

        E copyForTesting(InternalEntry<K, V, ?> r5, @CheckForNull InternalEntry<K, V, ?> r6) {
            return (E) this.map.entryHelper.copy(self(), castForTesting(r5), castForTesting(r6));
        }

        void drainKeyReferenceQueue(ReferenceQueue<K> r5) {
            int r0 = 0;
        L3:
            Reference<? extends K> r1 = r5.poll();
            if (r1 == null) goto L9;
            MapMakerInternalMap<K, V, E, S> r3 = this.map;
            r3.reclaimKey((InternalEntry) r1);
            r0 = r0 + 1;
            if (r0 != 16) goto L3;
            return;
        }

        void drainValueReferenceQueue(ReferenceQueue<V> r5) {
            int r0 = 0;
        L3:
            Reference<? extends V> r1 = r5.poll();
            if (r1 == null) goto L9;
            MapMakerInternalMap<K, V, E, S> r3 = this.map;
            r3.reclaimValue((WeakValueReference) r1);
            r0 = r0 + 1;
            if (r0 != 16) goto L3;
            return;
        }

        /* JADX WARN: Multi-variable type inference failed */
        void expand() {
            AtomicReferenceArray<E> r0 = this.table;
            int r1 = r0.length();
            if (r1 < 1073741824) goto L5;
            return;
        L5:
            int r2 = this.count;
            AtomicReferenceArray<E> r3 = newEntryArray(r1 << 1);
            this.threshold = (r3.length() * 3) / 4;
            int r4 = r3.length() - 1;
            int r5 = 0;
        L6:
            if (r5 >= r1) goto L26;
            E r6 = r0.get(r5);
            if (r6 == null) goto L25;
            InternalEntry r7 = r6.getNext();
            int r8 = r6.getHash() & r4;
            if (r7 != null) goto L12;
            r3.set(r8, r6);
            goto L25
        L12:
            InternalEntry r9 = r6;
            int r10 = r8;
            InternalEntry r11 = r7;
        L13:
            if (r11 == null) goto L18;
            int r12 = r11.getHash() & r4;
            if (r12 == r10) goto L17;
            r10 = r12;
            r9 = r11;
        L17:
            r11 = r11.getNext();
            goto L13
        L18:
            r3.set(r10, r9);
            InternalEntry r13 = r6;
        L19:
            if (r13 == r9) goto L25;
            int r14 = r13.getHash() & r4;
            InternalEntry r15 = copyEntry(r13, (InternalEntry) r3.get(r14));
            if (r15 == null) goto L23;
            r3.set(r14, r15);
        L24:
            r13 = r13.getNext();
            goto L19
        L23:
            r2 = r2 - 1;
        L25:
            r5 = r5 + 1;
            goto L6
        L26:
            this.table = r3;
            this.count = r2;
        }

        @CheckForNull
        V get(Object r3, int r4) {
            InternalEntry r0 = getLiveEntry(r3, r4);     // Catch: Throwable -> L11
            if (r0 != null) goto L6;
            postReadCleanup();
            return null;
        L6:
            V r1 = (V) r0.getValue();
            if (r1 != null) goto L9;
            tryDrainReferenceQueues();     // Catch: Throwable -> L11
        L9:
            postReadCleanup();
            return r1;
        L11:
            th = move-exception;
            postReadCleanup();
            throw th;
        }

        @CheckForNull
        E getEntry(Object r4, int r5) {
            if (this.count == 0) goto L16;
            E r0 = (E) getFirst(r5);
        L5:
            if (r0 == null) goto L23;
            if (r0.getHash() != r5) goto L15;
            Object r1 = r0.getKey();
            if (r1 != null) goto L13;
            tryDrainReferenceQueues();
            goto L15
        L13:
            if (this.map.keyEquivalence.equivalent(r4, r1) == false) goto L15;
            return r0;
        L15:
            r0 = (E) r0.getNext();
            goto L5
        L23:
            return null;
        L16:
            return null;
        }

        @CheckForNull
        E getFirst(int r3) {
            AtomicReferenceArray<E> r0 = this.table;
            return r0.get((r0.length() - 1) & r3);
        }

        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            throw new AssertionError();
        }

        @CheckForNull
        E getLiveEntry(Object r2, int r3) {
            return (E) getEntry(r2, r3);
        }

        @CheckForNull
        V getLiveValue(E r3) {
            if (r3.getKey() != null) goto L6;
            tryDrainReferenceQueues();
            return null;
        L6:
            V r0 = (V) r3.getValue();
            if (r0 != null) goto L10;
            tryDrainReferenceQueues();
            return null;
        L10:
            return r0;
        }

        @CheckForNull
        V getLiveValueForTesting(InternalEntry<K, V, ?> r2) {
            return getLiveValue(castForTesting(r2));
        }

        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            throw new AssertionError();
        }

        WeakValueReference<K, V, E> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> r2) {
            throw new AssertionError();
        }

        void initTable(AtomicReferenceArray<E> r2) {
            this.threshold = (r2.length() * 3) / 4;
            this.table = r2;
        }

        void maybeClearReferenceQueues() {
        }

        void maybeDrainReferenceQueues() {
        }

        AtomicReferenceArray<E> newEntryArray(int r2) {
            return new AtomicReferenceArray(r2);
        }

        E newEntryForTesting(K r4, int r5, @CheckForNull InternalEntry<K, V, ?> r6) {
            return (E) this.map.entryHelper.newEntry(self(), r4, r5, castForTesting(r6));
        }

        WeakValueReference<K, V, E> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> r2, V r3) {
            throw new AssertionError();
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) != 0) goto L6;
            runCleanup();
            return;
        }

        void preWriteCleanup() {
            runLockedCleanup();
        }

        @CheckForNull
        V put(K r10, int r11, V r12, boolean r13) {
            lock();
            preWriteCleanup();     // Catch: Throwable -> L29
            int r0 = this.count + 1;     // Catch: Throwable -> L29
            if (r0 <= this.threshold) goto L6;
            expand();     // Catch: Throwable -> L29
            r0 = this.count + 1;     // Catch: Throwable -> L29
        L6:
            AtomicReferenceArray<E> r1 = this.table;     // Catch: Throwable -> L29
            int r2 = (r1.length() - 1) & r11;     // Catch: Throwable -> L29
            E r3 = r1.get(r2);     // Catch: Throwable -> L29
            InternalEntry r4 = r3;
        L8:
            if (r4 == null) goto L26;
            Object r6 = r4.getKey();     // Catch: Throwable -> L29
            if (r4.getHash() != r11) goto L25;
            if (r6 == null) goto L25;
            if (this.map.keyEquivalence.equivalent(r10, r6) == false) goto L25;
            V r7 = (V) r4.getValue();
            if (r7 != null) goto L19;
            this.modCount++;     // Catch: Throwable -> L29
            setValue(r4, r12);     // Catch: Throwable -> L29
            this.count = this.count;     // Catch: Throwable -> L29
            unlock();
            return null;
        L19:
            if (r13 == false) goto L22;
            unlock();
            return r7;
        L22:
            this.modCount++;     // Catch: Throwable -> L29
            setValue(r4, r12);     // Catch: Throwable -> L29
            unlock();
            return r7;
        L25:
            r4 = r4.getNext();     // Catch: Throwable -> L29
            goto L8
        L26:
            this.modCount++;     // Catch: Throwable -> L29
            InternalEntry r5 = this.map.entryHelper.newEntry(self(), r10, r11, r3);     // Catch: Throwable -> L29
            setValue(r5, r12);     // Catch: Throwable -> L29
            r1.set(r2, (E) r5);     // Catch: Throwable -> L29
            this.count = r0;     // Catch: Throwable -> L29
            unlock();
            return null;
        L29:
            th = move-exception;
            unlock();
            throw th;
        }

        boolean reclaimKey(E r9, int r10) {
            lock();
            int r0 = this.count - 1;     // Catch: Throwable -> L13
            AtomicReferenceArray<E> r2 = this.table;     // Catch: Throwable -> L13
            int r3 = (r2.length() - 1) & r10;     // Catch: Throwable -> L13
            E r4 = r2.get(r3);     // Catch: Throwable -> L13
            InternalEntry r5 = r4;
        L4:
            if (r5 == null) goto L11;
            if (r5 == r9) goto L6;
            r5 = r5.getNext();     // Catch: Throwable -> L13
            goto L4
        L6:
            this.modCount++;     // Catch: Throwable -> L13
            InternalEntry r6 = removeFromChain(r4, r5);     // Catch: Throwable -> L13
            int r7 = this.count - 1;     // Catch: Throwable -> L13
            r2.set(r3, (E) r6);     // Catch: Throwable -> L13
            this.count = r7;     // Catch: Throwable -> L13
            unlock();
            return true;
        L11:
            unlock();
            return false;
        L13:
            th = move-exception;
            unlock();
            throw th;
        }

        boolean reclaimValue(K r11, int r12, WeakValueReference<K, V, E> r13) {
            lock();
            int r0 = this.count - 1;     // Catch: Throwable -> L22
            AtomicReferenceArray<E> r2 = this.table;     // Catch: Throwable -> L22
            int r3 = (r2.length() - 1) & r12;     // Catch: Throwable -> L22
            E r4 = r2.get(r3);     // Catch: Throwable -> L22
            InternalEntry r5 = r4;
        L5:
            if (r5 == null) goto L20;
            Object r7 = r5.getKey();     // Catch: Throwable -> L22
            if (r5.getHash() != r12) goto L19;
            if (r7 == null) goto L19;
            if (this.map.keyEquivalence.equivalent(r11, r7) == false) goto L19;
            if (((WeakValueEntry) r5).getValueReference() != r13) goto L16;
            this.modCount++;     // Catch: Throwable -> L22
            InternalEntry r6 = removeFromChain(r4, r5);     // Catch: Throwable -> L22
            int r9 = this.count - 1;     // Catch: Throwable -> L22
            r2.set(r3, (E) r6);     // Catch: Throwable -> L22
            this.count = r9;     // Catch: Throwable -> L22
            unlock();
            return true;
        L16:
            unlock();
            return false;
        L19:
            r5 = r5.getNext();     // Catch: Throwable -> L22
            goto L5
        L20:
            unlock();
            return false;
        L22:
            th = move-exception;
            unlock();
            throw th;
        }

        @CheckForNull
        V remove(Object r10, int r11) {
            lock();
            preWriteCleanup();     // Catch: Throwable -> L25
            int r0 = this.count - 1;     // Catch: Throwable -> L25
            AtomicReferenceArray<E> r1 = this.table;     // Catch: Throwable -> L25
            int r2 = (r1.length() - 1) & r11;     // Catch: Throwable -> L25
            E r3 = r1.get(r2);     // Catch: Throwable -> L25
            InternalEntry r4 = r3;
        L5:
            if (r4 == null) goto L23;
            Object r6 = r4.getKey();     // Catch: Throwable -> L25
            if (r4.getHash() != r11) goto L22;
            if (r6 == null) goto L22;
            if (this.map.keyEquivalence.equivalent(r10, r6) == false) goto L22;
            V r7 = (V) r4.getValue();
            if (r7 == null) goto L15;
        L16:
            this.modCount++;     // Catch: Throwable -> L25
            InternalEntry r5 = removeFromChain(r3, r4);     // Catch: Throwable -> L25
            int r8 = this.count - 1;     // Catch: Throwable -> L25
            r1.set(r2, (E) r5);     // Catch: Throwable -> L25
            this.count = r8;     // Catch: Throwable -> L25
            unlock();
            return r7;
        L15:
            if (isCollected(r4) == true) goto L16;
            unlock();
            return null;
        L22:
            r4 = r4.getNext();     // Catch: Throwable -> L25
            goto L5
        L23:
            unlock();
            return null;
        L25:
            th = move-exception;
            unlock();
            throw th;
        }

        boolean remove(Object r11, int r12, Object r13) {
            lock();
            preWriteCleanup();     // Catch: Throwable -> L25
            int r0 = this.count - 1;     // Catch: Throwable -> L25
            AtomicReferenceArray<E> r1 = this.table;     // Catch: Throwable -> L25
            int r2 = (r1.length() - 1) & r12;     // Catch: Throwable -> L25
            E r3 = r1.get(r2);     // Catch: Throwable -> L25
            InternalEntry r4 = r3;
        L5:
            if (r4 == null) goto L23;
            Object r6 = r4.getKey();     // Catch: Throwable -> L25
            if (r4.getHash() != r12) goto L22;
            if (r6 == null) goto L22;
            if (this.map.keyEquivalence.equivalent(r11, r6) == false) goto L22;
            boolean r8 = false;
            if (this.map.valueEquivalence().equivalent(r13, r4.getValue()) == false) goto L15;
            r8 = true;
        L16:
            this.modCount++;     // Catch: Throwable -> L25
            InternalEntry r5 = removeFromChain(r3, r4);     // Catch: Throwable -> L25
            int r9 = this.count - 1;     // Catch: Throwable -> L25
            r1.set(r2, (E) r5);     // Catch: Throwable -> L25
            this.count = r9;     // Catch: Throwable -> L25
            unlock();
            return r8;
        L15:
            if (isCollected(r4) == true) goto L16;
            unlock();
            return false;
        L22:
            r4 = r4.getNext();     // Catch: Throwable -> L25
            goto L5
        L23:
            unlock();
            return false;
        L25:
            th = move-exception;
            unlock();
            throw th;
        }

        boolean removeEntryForTesting(E r10) {
            int r0 = r10.getHash();
            int r1 = this.count - 1;
            AtomicReferenceArray<E> r3 = this.table;
            int r4 = (r3.length() - 1) & r0;
            E r5 = r3.get(r4);
            InternalEntry r6 = r5;
        L3:
            if (r6 == null) goto L8;
            if (r6 == r10) goto L5;
            r6 = r6.getNext();
            goto L3
        L5:
            this.modCount++;
            InternalEntry r7 = removeFromChain(r5, r6);
            int r8 = this.count - 1;
            r3.set(r4, (E) r7);
            this.count = r8;
            return true;
        L8:
            return false;
        }

        @CheckForNull
        E removeFromChain(E r5, E r6) {
            int r0 = this.count;
            E r1 = (E) r6.getNext();
            InternalEntry r2 = r5;
        L3:
            if (r2 == r6) goto L9;
            InternalEntry r3 = copyEntry(r2, r1);
            if (r3 == null) goto L7;
            r1 = (E) r3;
        L8:
            r2 = r2.getNext();
            goto L3
        L7:
            r0 = r0 - 1;
            goto L8
        L9:
            this.count = r0;
            return r1;
        }

        @CheckForNull
        E removeFromChainForTesting(InternalEntry<K, V, ?> r3, InternalEntry<K, V, ?> r4) {
            return (E) removeFromChain(castForTesting(r3), castForTesting(r4));
        }

        boolean removeTableEntryForTesting(InternalEntry<K, V, ?> r2) {
            return removeEntryForTesting(castForTesting(r2));
        }

        @CheckForNull
        V replace(K r11, int r12, V r13) {
            lock();
            preWriteCleanup();     // Catch: Throwable -> L25
            AtomicReferenceArray<E> r0 = this.table;     // Catch: Throwable -> L25
            int r1 = (r0.length() - 1) & r12;     // Catch: Throwable -> L25
            E r2 = r0.get(r1);     // Catch: Throwable -> L25
            InternalEntry r3 = r2;
        L5:
            if (r3 == null) goto L23;
            Object r5 = r3.getKey();     // Catch: Throwable -> L25
            if (r3.getHash() != r12) goto L22;
            if (r5 == null) goto L22;
            if (this.map.keyEquivalence.equivalent(r11, r5) == false) goto L22;
            V r6 = (V) r3.getValue();
            if (r6 == null) goto L14;
            this.modCount++;     // Catch: Throwable -> L25
            setValue(r3, r13);     // Catch: Throwable -> L25
            unlock();
            return r6;
        L14:
            if (isCollected(r3) == false) goto L16;
            int r7 = this.count - 1;     // Catch: Throwable -> L25
            this.modCount++;     // Catch: Throwable -> L25
            InternalEntry r8 = removeFromChain(r2, r3);     // Catch: Throwable -> L25
            int r9 = this.count - 1;     // Catch: Throwable -> L25
            r0.set(r1, (E) r8);     // Catch: Throwable -> L25
            this.count = r9;     // Catch: Throwable -> L25
        L16:
            unlock();
            return null;
        L22:
            r3 = r3.getNext();     // Catch: Throwable -> L25
            goto L5
        L23:
            unlock();
            return null;
        L25:
            th = move-exception;
            unlock();
            throw th;
        }

        boolean replace(K r12, int r13, V r14, V r15) {
            lock();
            preWriteCleanup();     // Catch: Throwable -> L29
            AtomicReferenceArray<E> r0 = this.table;     // Catch: Throwable -> L29
            int r1 = (r0.length() - 1) & r13;     // Catch: Throwable -> L29
            E r3 = r0.get(r1);     // Catch: Throwable -> L29
            InternalEntry r4 = r3;
        L5:
            if (r4 == null) goto L27;
            Object r6 = r4.getKey();     // Catch: Throwable -> L29
            if (r4.getHash() != r13) goto L26;
            if (r6 == null) goto L26;
            if (this.map.keyEquivalence.equivalent(r12, r6) == false) goto L26;
            Object r7 = r4.getValue();     // Catch: Throwable -> L29
            if (r7 != null) goto L19;
            if (isCollected(r4) == false) goto L16;
            int r8 = this.count - 1;     // Catch: Throwable -> L29
            this.modCount++;     // Catch: Throwable -> L29
            InternalEntry r9 = removeFromChain(r3, r4);     // Catch: Throwable -> L29
            int r10 = this.count - 1;     // Catch: Throwable -> L29
            r0.set(r1, (E) r9);     // Catch: Throwable -> L29
            this.count = r10;     // Catch: Throwable -> L29
        L16:
            unlock();
            return false;
        L19:
            if (this.map.valueEquivalence().equivalent(r14, r7) == false) goto L23;
            this.modCount++;     // Catch: Throwable -> L29
            setValue(r4, r15);     // Catch: Throwable -> L29
            unlock();
            return true;
        L23:
            unlock();
            return false;
        L26:
            r4 = r4.getNext();     // Catch: Throwable -> L29
            goto L5
        L27:
            unlock();
            return false;
        L29:
            th = move-exception;
            unlock();
            throw th;
        }

        void runCleanup() {
            runLockedCleanup();
        }

        void runLockedCleanup() {
            if (tryLock() == true) goto L10;
            return;
        L10:
            maybeDrainReferenceQueues();     // Catch: Throwable -> L6
            this.readCount.set(0);     // Catch: Throwable -> L6
            unlock();
            return;
        L6:
            th = move-exception;
            unlock();
            throw th;
        }

        abstract S self();

        void setTableEntryForTesting(int r3, InternalEntry<K, V, ?> r4) {
            this.table.set(r3, (E) castForTesting(r4));
        }

        void setValue(E r3, V r4) {
            this.map.entryHelper.setValue(self(), r3, r4);
        }

        void setValueForTesting(InternalEntry<K, V, ?> r4, V r5) {
            this.map.entryHelper.setValue(self(), castForTesting(r4), r5);
        }

        void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> r2, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> r3) {
            throw new AssertionError();
        }

        void tryDrainReferenceQueues() {
            if (tryLock() == true) goto L10;
            return;
        L10:
            maybeDrainReferenceQueues();     // Catch: Throwable -> L6
            unlock();
            return;
        L6:
            th = move-exception;
            unlock();
            throw th;
        }
    }

    private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3;

        SerializationProxy(Strength r1, Strength r2, Equivalence<Object> r3, Equivalence<Object> r4, int r5, ConcurrentMap<K, V> r6) {
            super(r1, r2, r3, r4, r5, r6);
        }

        private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
            r3.defaultReadObject();
            this.delegate = readMapMaker(r3).makeMap();
            readEntries(r3);
        }

        private Object readResolve() {
            return this.delegate;
        }

        private void writeObject(ObjectOutputStream r1) throws IOException {
            r1.defaultWriteObject();
            writeMapTo(r1);
        }
    }

    static abstract enum Strength extends Enum<Strength> {
        private static final /* synthetic */ Strength[] $VALUES = null;
        public static final Strength STRONG = null;
        public static final Strength WEAK = null;

        private static /* synthetic */ Strength[] $values() {
            return new Strength[]{STRONG, WEAK};
        }

        static {
            final String r1 = "STRONG";
            final int r2 = 0;
            STRONG = new C06741(r1, r2);
            final String r3 = "WEAK";
            final int r4 = 1;
            WEAK = new C06752(r3, r4);
            $VALUES = $values();
        }

        private Strength(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ Strength(String r1, int r2, C06731 r3) {
            this(r1, r2);
        }

        public static Strength valueOf(String r1) {
            return (Strength) Enum.valueOf(Strength.class, r1);
        }

        public static Strength[] values() {
            return (Strength[]) $VALUES.clone();
        }

        abstract Equivalence<Object> defaultEquivalence();
    }

    static class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> {

        static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> {
            private static final Helper<?> INSTANCE = null;

            static {
                INSTANCE = new Helper();
            }

            Helper() {
            }

            static <K> Helper<K> instance() {
                return (Helper<K>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment r1, InternalEntry r2, @CheckForNull InternalEntry r3) {
                return copy((StrongKeyDummyValueSegment) r1, (StrongKeyDummyValueEntry) r2, (StrongKeyDummyValueEntry) r3);
            }

            public StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueSegment<K> r3, StrongKeyDummyValueEntry<K> r4, @CheckForNull StrongKeyDummyValueEntry<K> r5) {
                return newEntry(r3, r4.key, r4.hash, r5);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment r1, Object r2, int r3, @CheckForNull InternalEntry r4) {
                return newEntry((StrongKeyDummyValueSegment) r1, r2, r3, (StrongKeyDummyValueEntry) r4);
            }

            public StrongKeyDummyValueEntry<K> newEntry(StrongKeyDummyValueSegment<K> r3, K r4, int r5, @CheckForNull StrongKeyDummyValueEntry<K> r6) {
                if (r6 != null) goto L5;
                return new StrongKeyDummyValueEntry(r4, r5, null);
            L5:
                return new LinkedStrongKeyDummyValueEntry(r4, r5, r6);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ Segment newSegment(MapMakerInternalMap r1, int r2) {
                return newSegment(r1, r2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> r2, int r3) {
                return new StrongKeyDummyValueSegment(r2, r3);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment r1, InternalEntry r2, MapMaker.Dummy r3) {
                setValue((StrongKeyDummyValueSegment) r1, (StrongKeyDummyValueEntry) r2, r3);
            }

            public void setValue(StrongKeyDummyValueSegment<K> r1, StrongKeyDummyValueEntry<K> r2, MapMaker.Dummy r3) {
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }
        }

        private static final class LinkedStrongKeyDummyValueEntry<K> extends StrongKeyDummyValueEntry<K> {
            private final StrongKeyDummyValueEntry<K> next;

            LinkedStrongKeyDummyValueEntry(K r2, int r3, StrongKeyDummyValueEntry<K> r4) {
                super(r2, r3, null);
                this.next = r4;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ InternalEntry getNext() {
                return getNext();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public StrongKeyDummyValueEntry<K> getNext() {
                return this.next;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.StrongKeyDummyValueEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ Object getValue() {
                return super.getValue();
            }
        }

        private StrongKeyDummyValueEntry(K r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ StrongKeyDummyValueEntry(Object r1, int r2, C06731 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public /* bridge */ /* synthetic */ Object getValue() {
            return getValue();
        }
    }

    static final class StrongKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> {
        StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> r1, int r2) {
            super(r1, r2);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public /* bridge */ /* synthetic */ InternalEntry castForTesting(InternalEntry r1) {
            return castForTesting(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyDummyValueEntry<K> castForTesting(InternalEntry<K, MapMaker.Dummy, ?> r2) {
            return (StrongKeyDummyValueEntry) r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        /* bridge */ /* synthetic */ Segment self() {
            return self();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        StrongKeyDummyValueSegment<K> self() {
            return this;
        }
    }

    static class StrongKeyStrongValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> {

        @CheckForNull
        private volatile V value;

        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = null;

            static {
                INSTANCE = new Helper();
            }

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment r1, InternalEntry r2, @CheckForNull InternalEntry r3) {
                return copy((StrongKeyStrongValueSegment) r1, (StrongKeyStrongValueEntry) r2, (StrongKeyStrongValueEntry) r3);
            }

            public StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueSegment<K, V> r3, StrongKeyStrongValueEntry<K, V> r4, @CheckForNull StrongKeyStrongValueEntry<K, V> r5) {
                StrongKeyStrongValueEntry<K, V> r0 = newEntry(r3, r4.key, r4.hash, r5);
                StrongKeyStrongValueEntry.access$202(r0, StrongKeyStrongValueEntry.access$200(r4));
                return r0;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment r1, Object r2, int r3, @CheckForNull InternalEntry r4) {
                return newEntry((StrongKeyStrongValueSegment) r1, r2, r3, (StrongKeyStrongValueEntry) r4);
            }

            public StrongKeyStrongValueEntry<K, V> newEntry(StrongKeyStrongValueSegment<K, V> r3, K r4, int r5, @CheckForNull StrongKeyStrongValueEntry<K, V> r6) {
                if (r6 != null) goto L5;
                return new StrongKeyStrongValueEntry(r4, r5, null);
            L5:
                return new LinkedStrongKeyStrongValueEntry(r4, r5, r6);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ Segment newSegment(MapMakerInternalMap r1, int r2) {
                return newSegment(r1, r2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> r2, int r3) {
                return new StrongKeyStrongValueSegment(r2, r3);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment r1, InternalEntry r2, Object r3) {
                setValue((StrongKeyStrongValueSegment) r1, (StrongKeyStrongValueEntry) r2, r3);
            }

            public void setValue(StrongKeyStrongValueSegment<K, V> r1, StrongKeyStrongValueEntry<K, V> r2, V r3) {
                StrongKeyStrongValueEntry.access$202(r2, r3);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }
        }

        private static final class LinkedStrongKeyStrongValueEntry<K, V> extends StrongKeyStrongValueEntry<K, V> {
            private final StrongKeyStrongValueEntry<K, V> next;

            LinkedStrongKeyStrongValueEntry(K r2, int r3, StrongKeyStrongValueEntry<K, V> r4) {
                super(r2, r3, null);
                this.next = r4;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ InternalEntry getNext() {
                return getNext();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public StrongKeyStrongValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        private StrongKeyStrongValueEntry(K r2, int r3) {
            super(r2, r3);
            this.value = null;
        }

        /* synthetic */ StrongKeyStrongValueEntry(Object r1, int r2, C06731 r3) {
            this(r1, r2);
        }

        static /* synthetic */ Object access$200(StrongKeyStrongValueEntry r1) {
            return r1.value;
        }

        /* JADX WARN: Multi-variable type inference failed */
        static /* synthetic */ Object access$202(StrongKeyStrongValueEntry r0, Object r1) {
            r0.value = r1;
            return r1;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @CheckForNull
        public final V getValue() {
            return this.value;
        }
    }

    static final class StrongKeyStrongValueSegment<K, V> extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
        StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> r1, int r2) {
            super(r1, r2);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        @CheckForNull
        public /* bridge */ /* synthetic */ InternalEntry castForTesting(@CheckForNull InternalEntry r1) {
            return castForTesting(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        @CheckForNull
        public StrongKeyStrongValueEntry<K, V> castForTesting(@CheckForNull InternalEntry<K, V, ?> r2) {
            return (StrongKeyStrongValueEntry) r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        /* bridge */ /* synthetic */ Segment self() {
            return self();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        StrongKeyStrongValueSegment<K, V> self() {
            return this;
        }
    }

    static class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference;

        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = null;

            static {
                INSTANCE = new Helper();
            }

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            @CheckForNull
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment r1, InternalEntry r2, @CheckForNull InternalEntry r3) {
                return copy((StrongKeyWeakValueSegment) r1, (StrongKeyWeakValueEntry) r2, (StrongKeyWeakValueEntry) r3);
            }

            @CheckForNull
            public StrongKeyWeakValueEntry<K, V> copy(StrongKeyWeakValueSegment<K, V> r4, StrongKeyWeakValueEntry<K, V> r5, @CheckForNull StrongKeyWeakValueEntry<K, V> r6) {
                if (Segment.isCollected(r5) == false) goto L6;
                return null;
            L6:
                StrongKeyWeakValueEntry<K, V> r0 = newEntry(r4, r5.key, r5.hash, r6);
                StrongKeyWeakValueEntry.access$402(r0, StrongKeyWeakValueEntry.access$400(r5).copyFor(StrongKeyWeakValueSegment.access$500(r4), r0));
                return r0;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment r1, Object r2, int r3, @CheckForNull InternalEntry r4) {
                return newEntry((StrongKeyWeakValueSegment) r1, r2, r3, (StrongKeyWeakValueEntry) r4);
            }

            public StrongKeyWeakValueEntry<K, V> newEntry(StrongKeyWeakValueSegment<K, V> r3, K r4, int r5, @CheckForNull StrongKeyWeakValueEntry<K, V> r6) {
                if (r6 != null) goto L5;
                return new StrongKeyWeakValueEntry(r4, r5, null);
            L5:
                return new LinkedStrongKeyWeakValueEntry(r4, r5, r6);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ Segment newSegment(MapMakerInternalMap r1, int r2) {
                return newSegment(r1, r2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> r2, int r3) {
                return new StrongKeyWeakValueSegment(r2, r3);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment r1, InternalEntry r2, Object r3) {
                setValue((StrongKeyWeakValueSegment) r1, (StrongKeyWeakValueEntry) r2, r3);
            }

            public void setValue(StrongKeyWeakValueSegment<K, V> r4, StrongKeyWeakValueEntry<K, V> r5, V r6) {
                WeakValueReference r0 = StrongKeyWeakValueEntry.access$400(r5);
                StrongKeyWeakValueEntry.access$402(r5, new WeakValueReferenceImpl(StrongKeyWeakValueSegment.access$500(r4), r6, r5));
                r0.clear();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.WEAK;
            }
        }

        private static final class LinkedStrongKeyWeakValueEntry<K, V> extends StrongKeyWeakValueEntry<K, V> {
            private final StrongKeyWeakValueEntry<K, V> next;

            LinkedStrongKeyWeakValueEntry(K r2, int r3, StrongKeyWeakValueEntry<K, V> r4) {
                super(r2, r3, null);
                this.next = r4;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ InternalEntry getNext() {
                return getNext();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public StrongKeyWeakValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        private StrongKeyWeakValueEntry(K r2, int r3) {
            super(r2, r3);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }

        /* synthetic */ StrongKeyWeakValueEntry(Object r1, int r2, C06731 r3) {
            this(r1, r2);
        }

        static /* synthetic */ WeakValueReference access$400(StrongKeyWeakValueEntry r1) {
            return r1.valueReference;
        }

        static /* synthetic */ WeakValueReference access$402(StrongKeyWeakValueEntry r0, WeakValueReference r1) {
            r0.valueReference = r1;
            return r1;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @CheckForNull
        public final V getValue() {
            return this.valueReference.get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public final WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    static final class StrongKeyWeakValueSegment<K, V> extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<V> queueForValues;

        StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> r2, int r3) {
            super(r2, r3);
            this.queueForValues = new ReferenceQueue();
        }

        static /* synthetic */ ReferenceQueue access$500(StrongKeyWeakValueSegment r1) {
            return r1.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        @CheckForNull
        public /* bridge */ /* synthetic */ InternalEntry castForTesting(@CheckForNull InternalEntry r1) {
            return castForTesting(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        @CheckForNull
        public StrongKeyWeakValueEntry<K, V> castForTesting(@CheckForNull InternalEntry<K, V, ?> r2) {
            return (StrongKeyWeakValueEntry) r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> r2) {
            return castForTesting(r2).getValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainValueReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> r4, V r5) {
            return new WeakValueReferenceImpl(this.queueForValues, r5, castForTesting(r4));
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        /* bridge */ /* synthetic */ Segment self() {
            return self();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        StrongKeyWeakValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> r4, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> r5) {
            StrongKeyWeakValueEntry<K, V> r0 = castForTesting(r4);
            WeakValueReference r2 = StrongKeyWeakValueEntry.access$400(r0);
            StrongKeyWeakValueEntry.access$402(r0, r5);
            r2.clear();
        }
    }

    interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
    }

    final class ValueIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<V> {
        final /* synthetic */ MapMakerInternalMap this$0;

        ValueIterator(MapMakerInternalMap r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    final class Values extends AbstractCollection<V> {
        final /* synthetic */ MapMakerInternalMap this$0;

        Values(MapMakerInternalMap r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object r2) {
            return this.this$0.containsValue(r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.this$0.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator(this.this$0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.this$0.size();
        }
    }

    static class WeakKeyDummyValueEntry<K> extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> {

        static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> {
            private static final Helper<?> INSTANCE = null;

            static {
                INSTANCE = new Helper();
            }

            Helper() {
            }

            static <K> Helper<K> instance() {
                return (Helper<K>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            @CheckForNull
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment r1, InternalEntry r2, @CheckForNull InternalEntry r3) {
                return copy((WeakKeyDummyValueSegment) r1, (WeakKeyDummyValueEntry) r2, (WeakKeyDummyValueEntry) r3);
            }

            @CheckForNull
            public WeakKeyDummyValueEntry<K> copy(WeakKeyDummyValueSegment<K> r3, WeakKeyDummyValueEntry<K> r4, @CheckForNull WeakKeyDummyValueEntry<K> r5) {
                K r0 = r4.getKey();
                if (r0 != null) goto L7;
                return null;
            L7:
                return newEntry(r3, r0, r4.hash, r5);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment r1, Object r2, int r3, @CheckForNull InternalEntry r4) {
                return newEntry((WeakKeyDummyValueSegment) r1, r2, r3, (WeakKeyDummyValueEntry) r4);
            }

            public WeakKeyDummyValueEntry<K> newEntry(WeakKeyDummyValueSegment<K> r10, K r11, int r12, @CheckForNull WeakKeyDummyValueEntry<K> r13) {
                if (r13 != null) goto L5;
                return new WeakKeyDummyValueEntry(WeakKeyDummyValueSegment.access$800(r10), r11, r12, null);
            L5:
                return new LinkedWeakKeyDummyValueEntry(WeakKeyDummyValueSegment.access$800(r10), r11, r12, r13, null);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ Segment newSegment(MapMakerInternalMap r1, int r2) {
                return newSegment(r1, r2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> r2, int r3) {
                return new WeakKeyDummyValueSegment(r2, r3);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment r1, InternalEntry r2, MapMaker.Dummy r3) {
                setValue((WeakKeyDummyValueSegment) r1, (WeakKeyDummyValueEntry) r2, r3);
            }

            public void setValue(WeakKeyDummyValueSegment<K> r1, WeakKeyDummyValueEntry<K> r2, MapMaker.Dummy r3) {
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }
        }

        private static final class LinkedWeakKeyDummyValueEntry<K> extends WeakKeyDummyValueEntry<K> {
            private final WeakKeyDummyValueEntry<K> next;

            private LinkedWeakKeyDummyValueEntry(ReferenceQueue<K> r2, K r3, int r4, WeakKeyDummyValueEntry<K> r5) {
                super(r2, r3, r4, null);
                this.next = r5;
            }

            /* synthetic */ LinkedWeakKeyDummyValueEntry(ReferenceQueue r1, Object r2, int r3, WeakKeyDummyValueEntry r4, C06731 r5) {
                this(r1, r2, r3, r4);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ InternalEntry getNext() {
                return getNext();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public WeakKeyDummyValueEntry<K> getNext() {
                return this.next;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.WeakKeyDummyValueEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ Object getValue() {
                return super.getValue();
            }
        }

        private WeakKeyDummyValueEntry(ReferenceQueue<K> r1, K r2, int r3) {
            super(r1, r2, r3);
        }

        /* synthetic */ WeakKeyDummyValueEntry(ReferenceQueue r1, Object r2, int r3, C06731 r4) {
            this(r1, r2, r3);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public /* bridge */ /* synthetic */ Object getValue() {
            return getValue();
        }
    }

    static final class WeakKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> {
        private final ReferenceQueue<K> queueForKeys;

        WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> r2, int r3) {
            super(r2, r3);
            this.queueForKeys = new ReferenceQueue();
        }

        static /* synthetic */ ReferenceQueue access$800(WeakKeyDummyValueSegment r1) {
            return r1.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public /* bridge */ /* synthetic */ InternalEntry castForTesting(InternalEntry r1) {
            return castForTesting(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyDummyValueEntry<K> castForTesting(InternalEntry<K, MapMaker.Dummy, ?> r2) {
            return (WeakKeyDummyValueEntry) r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        /* bridge */ /* synthetic */ Segment self() {
            return self();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        WeakKeyDummyValueSegment<K> self() {
            return this;
        }
    }

    static class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> {

        @CheckForNull
        private volatile V value;

        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = null;

            static {
                INSTANCE = new Helper();
            }

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            @CheckForNull
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment r1, InternalEntry r2, @CheckForNull InternalEntry r3) {
                return copy((WeakKeyStrongValueSegment) r1, (WeakKeyStrongValueEntry) r2, (WeakKeyStrongValueEntry) r3);
            }

            @CheckForNull
            public WeakKeyStrongValueEntry<K, V> copy(WeakKeyStrongValueSegment<K, V> r4, WeakKeyStrongValueEntry<K, V> r5, @CheckForNull WeakKeyStrongValueEntry<K, V> r6) {
                K r0 = r5.getKey();
                if (r0 != null) goto L6;
                return null;
            L6:
                WeakKeyStrongValueEntry<K, V> r1 = newEntry(r4, r0, r5.hash, r6);
                WeakKeyStrongValueEntry.access$1102(r1, WeakKeyStrongValueEntry.access$1100(r5));
                return r1;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment r1, Object r2, int r3, @CheckForNull InternalEntry r4) {
                return newEntry((WeakKeyStrongValueSegment) r1, r2, r3, (WeakKeyStrongValueEntry) r4);
            }

            public WeakKeyStrongValueEntry<K, V> newEntry(WeakKeyStrongValueSegment<K, V> r10, K r11, int r12, @CheckForNull WeakKeyStrongValueEntry<K, V> r13) {
                if (r13 != null) goto L5;
                return new WeakKeyStrongValueEntry(WeakKeyStrongValueSegment.access$1200(r10), r11, r12, null);
            L5:
                return new LinkedWeakKeyStrongValueEntry(WeakKeyStrongValueSegment.access$1200(r10), r11, r12, r13, null);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ Segment newSegment(MapMakerInternalMap r1, int r2) {
                return newSegment(r1, r2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> r2, int r3) {
                return new WeakKeyStrongValueSegment(r2, r3);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment r1, InternalEntry r2, Object r3) {
                setValue((WeakKeyStrongValueSegment) r1, (WeakKeyStrongValueEntry) r2, r3);
            }

            public void setValue(WeakKeyStrongValueSegment<K, V> r1, WeakKeyStrongValueEntry<K, V> r2, V r3) {
                WeakKeyStrongValueEntry.access$1102(r2, r3);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }
        }

        private static final class LinkedWeakKeyStrongValueEntry<K, V> extends WeakKeyStrongValueEntry<K, V> {
            private final WeakKeyStrongValueEntry<K, V> next;

            private LinkedWeakKeyStrongValueEntry(ReferenceQueue<K> r2, K r3, int r4, WeakKeyStrongValueEntry<K, V> r5) {
                super(r2, r3, r4, null);
                this.next = r5;
            }

            /* synthetic */ LinkedWeakKeyStrongValueEntry(ReferenceQueue r1, Object r2, int r3, WeakKeyStrongValueEntry r4, C06731 r5) {
                this(r1, r2, r3, r4);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ InternalEntry getNext() {
                return getNext();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public WeakKeyStrongValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        private WeakKeyStrongValueEntry(ReferenceQueue<K> r2, K r3, int r4) {
            super(r2, r3, r4);
            this.value = null;
        }

        /* synthetic */ WeakKeyStrongValueEntry(ReferenceQueue r1, Object r2, int r3, C06731 r4) {
            this(r1, r2, r3);
        }

        static /* synthetic */ Object access$1100(WeakKeyStrongValueEntry r1) {
            return r1.value;
        }

        /* JADX WARN: Multi-variable type inference failed */
        static /* synthetic */ Object access$1102(WeakKeyStrongValueEntry r0, Object r1) {
            r0.value = r1;
            return r1;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        @CheckForNull
        public final V getValue() {
            return this.value;
        }
    }

    static final class WeakKeyStrongValueSegment<K, V> extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys;

        WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> r2, int r3) {
            super(r2, r3);
            this.queueForKeys = new ReferenceQueue();
        }

        static /* synthetic */ ReferenceQueue access$1200(WeakKeyStrongValueSegment r1) {
            return r1.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public /* bridge */ /* synthetic */ InternalEntry castForTesting(InternalEntry r1) {
            return castForTesting(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> r2) {
            return (WeakKeyStrongValueEntry) r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        /* bridge */ /* synthetic */ Segment self() {
            return self();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        WeakKeyStrongValueSegment<K, V> self() {
            return this;
        }
    }

    static class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference;

        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = null;

            static {
                INSTANCE = new Helper();
            }

            Helper() {
            }

            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            @CheckForNull
            public /* bridge */ /* synthetic */ InternalEntry copy(Segment r1, InternalEntry r2, @CheckForNull InternalEntry r3) {
                return copy((WeakKeyWeakValueSegment) r1, (WeakKeyWeakValueEntry) r2, (WeakKeyWeakValueEntry) r3);
            }

            @CheckForNull
            public WeakKeyWeakValueEntry<K, V> copy(WeakKeyWeakValueSegment<K, V> r5, WeakKeyWeakValueEntry<K, V> r6, @CheckForNull WeakKeyWeakValueEntry<K, V> r7) {
                K r0 = r6.getKey();
                if (r0 != null) goto L6;
                return null;
            L6:
                if (Segment.isCollected(r6) == false) goto L8;
                return null;
            L8:
                WeakKeyWeakValueEntry<K, V> r1 = newEntry(r5, r0, r6.hash, r7);
                WeakKeyWeakValueEntry.access$1402(r1, WeakKeyWeakValueEntry.access$1400(r6).copyFor(WeakKeyWeakValueSegment.access$1500(r5), r1));
                return r1;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ InternalEntry newEntry(Segment r1, Object r2, int r3, @CheckForNull InternalEntry r4) {
                return newEntry((WeakKeyWeakValueSegment) r1, r2, r3, (WeakKeyWeakValueEntry) r4);
            }

            public WeakKeyWeakValueEntry<K, V> newEntry(WeakKeyWeakValueSegment<K, V> r3, K r4, int r5, @CheckForNull WeakKeyWeakValueEntry<K, V> r6) {
                if (r6 != null) goto L5;
                return new WeakKeyWeakValueEntry(WeakKeyWeakValueSegment.access$1600(r3), r4, r5);
            L5:
                return new LinkedWeakKeyWeakValueEntry(WeakKeyWeakValueSegment.access$1600(r3), r4, r5, r6);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ Segment newSegment(MapMakerInternalMap r1, int r2) {
                return newSegment(r1, r2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> r2, int r3) {
                return new WeakKeyWeakValueSegment(r2, r3);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public /* bridge */ /* synthetic */ void setValue(Segment r1, InternalEntry r2, Object r3) {
                setValue((WeakKeyWeakValueSegment) r1, (WeakKeyWeakValueEntry) r2, r3);
            }

            public void setValue(WeakKeyWeakValueSegment<K, V> r4, WeakKeyWeakValueEntry<K, V> r5, V r6) {
                WeakValueReference r0 = WeakKeyWeakValueEntry.access$1400(r5);
                WeakKeyWeakValueEntry.access$1402(r5, new WeakValueReferenceImpl(WeakKeyWeakValueSegment.access$1500(r4), r6, r5));
                r0.clear();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.WEAK;
            }
        }

        private static final class LinkedWeakKeyWeakValueEntry<K, V> extends WeakKeyWeakValueEntry<K, V> {
            private final WeakKeyWeakValueEntry<K, V> next;

            LinkedWeakKeyWeakValueEntry(ReferenceQueue<K> r1, K r2, int r3, WeakKeyWeakValueEntry<K, V> r4) {
                super(r1, r2, r3);
                this.next = r4;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ InternalEntry getNext() {
                return getNext();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public WeakKeyWeakValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        WeakKeyWeakValueEntry(ReferenceQueue<K> r2, K r3, int r4) {
            super(r2, r3, r4);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }

        static /* synthetic */ WeakValueReference access$1400(WeakKeyWeakValueEntry r1) {
            return r1.valueReference;
        }

        static /* synthetic */ WeakValueReference access$1402(WeakKeyWeakValueEntry r0, WeakValueReference r1) {
            r0.valueReference = r1;
            return r1;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.valueReference.get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public final WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    static final class WeakKeyWeakValueSegment<K, V> extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys;
        private final ReferenceQueue<V> queueForValues;

        WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> r2, int r3) {
            super(r2, r3);
            this.queueForKeys = new ReferenceQueue();
            this.queueForValues = new ReferenceQueue();
        }

        static /* synthetic */ ReferenceQueue access$1500(WeakKeyWeakValueSegment r1) {
            return r1.queueForValues;
        }

        static /* synthetic */ ReferenceQueue access$1600(WeakKeyWeakValueSegment r1) {
            return r1.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        @CheckForNull
        public /* bridge */ /* synthetic */ InternalEntry castForTesting(@CheckForNull InternalEntry r1) {
            return castForTesting(r1);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        @CheckForNull
        public WeakKeyWeakValueEntry<K, V> castForTesting(@CheckForNull InternalEntry<K, V, ?> r2) {
            return (WeakKeyWeakValueEntry) r2;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> r2) {
            return castForTesting(r2).getValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
            drainValueReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> r4, V r5) {
            return new WeakValueReferenceImpl(this.queueForValues, r5, castForTesting(r4));
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        /* bridge */ /* synthetic */ Segment self() {
            return self();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        WeakKeyWeakValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> r4, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> r5) {
            WeakKeyWeakValueEntry<K, V> r0 = castForTesting(r4);
            WeakValueReference r2 = WeakKeyWeakValueEntry.access$1400(r0);
            WeakKeyWeakValueEntry.access$1402(r0, r5);
            r2.clear();
        }
    }

    interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
        WeakValueReference<K, V, E> getValueReference();
    }

    interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
        void clear();

        WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> r1, E r2);

        @CheckForNull
        V get();

        E getEntry();
    }

    static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> {
        final E entry;

        WeakValueReferenceImpl(ReferenceQueue<V> r1, V r2, E r3) {
            super(r2, r1);
            this.entry = r3;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> r3, E r4) {
            return new WeakValueReferenceImpl(r3, get(), r4);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public E getEntry() {
            return this.entry;
        }
    }

    final class WriteThroughEntry extends AbstractMapEntry<K, V> {
        final K key;
        final /* synthetic */ MapMakerInternalMap this$0;
        V value;

        WriteThroughEntry(MapMakerInternalMap r1, K r2, V r3) {
            this.this$0 = r1;
            this.key = r2;
            this.value = r3;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof Map.Entry) == false) goto L10;
            Map.Entry r0 = (Map.Entry) r5;
            if (this.key.equals(r0.getKey()) == true) goto L7;
            return false;
        L7:
            if (this.value.equals(r0.getValue()) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V setValue(V r3) {
            V r0 = (V) this.this$0.put(this.key, r3);
            this.value = r3;
            return r0;
        }
    }

    static {
        UNSET_WEAK_VALUE_REFERENCE = new C06731();
    }

    private MapMakerInternalMap(MapMaker r9, InternalEntryHelper<K, V, E, S> r10) {
        this.concurrencyLevel = Math.min(r9.getConcurrencyLevel(), 65536);
        this.keyEquivalence = r9.getKeyEquivalence();
        this.entryHelper = r10;
        int r0 = Math.min(r9.getInitialCapacity(), 1073741824);
        int r1 = 0;
        int r2 = 1;
    L4:
        if (r2 >= this.concurrencyLevel) goto L6;
        r1 = r1 + 1;
        r2 = r2 << 1;
        goto L4
    L6:
        this.segmentShift = 32 - r1;
        this.segmentMask = r2 - 1;
        this.segments = newSegmentArray(r2);
        int r3 = r0 / r2;
        if ((r3 * r2) >= r0) goto L9;
        r3 = r3 + 1;
    L9:
        int r4 = 1;
    L10:
        if (r4 >= r3) goto L12;
        r4 = r4 << 1;
        goto L10
    L12:
        int r5 = 0;
    L14:
        if (r5 >= this.segments.length) goto L16;
        this.segments[r5] = createSegment(r4);
        r5 = r5 + 1;
        goto L14
    }

    static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker r2) {
        if (r2.getKeyStrength() != Strength.STRONG) goto L9;
        if (r2.getValueStrength() != Strength.STRONG) goto L9;
        return new MapMakerInternalMap(r2, StrongKeyStrongValueEntry.Helper.instance());
    L9:
        if (r2.getKeyStrength() != Strength.STRONG) goto L15;
        if (r2.getValueStrength() != Strength.WEAK) goto L15;
        return new MapMakerInternalMap(r2, StrongKeyWeakValueEntry.Helper.instance());
    L15:
        if (r2.getKeyStrength() != Strength.WEAK) goto L21;
        if (r2.getValueStrength() != Strength.STRONG) goto L21;
        return new MapMakerInternalMap(r2, WeakKeyStrongValueEntry.Helper.instance());
    L21:
        if (r2.getKeyStrength() != Strength.WEAK) goto L27;
        if (r2.getValueStrength() != Strength.WEAK) goto L27;
        return new MapMakerInternalMap(r2, WeakKeyWeakValueEntry.Helper.instance());
    L27:
        throw new AssertionError();
    }

    static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker r2) {
        if (r2.getKeyStrength() != Strength.STRONG) goto L9;
        if (r2.getValueStrength() != Strength.STRONG) goto L9;
        return new MapMakerInternalMap(r2, StrongKeyDummyValueEntry.Helper.instance());
    L9:
        if (r2.getKeyStrength() != Strength.WEAK) goto L15;
        if (r2.getValueStrength() != Strength.STRONG) goto L15;
        return new MapMakerInternalMap(r2, WeakKeyDummyValueEntry.Helper.instance());
    L15:
        if (r2.getValueStrength() != Strength.WEAK) goto L19;
        throw new IllegalArgumentException("Map cannot have both weak and dummy values");
    L19:
        throw new AssertionError();
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializationProxy");
    }

    static int rehash(int r2) {
        int r3 = r2 + ((r2 << 15) ^ (-12931));
        int r4 = r3 ^ (r3 >>> 10);
        int r5 = r4 + (r4 << 3);
        int r6 = r5 ^ (r5 >>> 6);
        int r7 = r6 + ((r6 << 2) + (r6 << 14));
        return (r7 >>> 16) ^ r7;
    }

    static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
        return (WeakValueReference<K, V, E>) UNSET_WEAK_VALUE_REFERENCE;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Segment<K, V, E, S>[] r0 = this.segments;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].clear();
        r2 = r2 + 1;
        goto L3
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@CheckForNull Object r3) {
        if (r3 != null) goto L5;
        return false;
    L5:
        int r0 = hash(r3);
        return segmentFor(r0).containsKey(r3, r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1, types: [int] */
    /* JADX WARN: Type inference failed for: r10v3 */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@CheckForNull Object r18) {
        boolean r1 = false;
        if (r18 != null) goto L5;
        return false;
    L5:
        Segment<K, V, E, S>[] r3 = this.segments;
        long r4 = -1;
        int r6 = 0;
    L7:
        if (r6 >= 3) goto L29;
        long r7 = 0;
        int r9 = r3.length;
        ?? r10 = r1;
    L9:
        if (r10 >= r9) goto L24;
        Segment<K, V, E, S> r11 = r3[r10];
        int r12 = r11.count;
        AtomicReferenceArray<E> r13 = r11.table;
        int r14 = 0;
    L12:
        if (r14 >= r13.length()) goto L23;
        E r15 = r13.get(r14);
    L14:
        if (r15 == null) goto L22;
        boolean r16 = r1;
        V r2 = r11.getLiveValue(r15);
        if (r2 == null) goto L21;
        if (valueEquivalence().equivalent(r18, r2) == false) goto L21;
        return true;
    L21:
        r15 = r15.getNext();
        r1 = r16;
        goto L14
    L22:
        r14 = r14 + 1;
        goto L12
    L23:
        boolean r17 = r1;
        r7 = r7 + ((long) r11.modCount);
        r1 = r17;
        r10 = r10 + 1;
        goto L9
    L24:
        boolean r19 = r1;
        if (r7 == r4) goto L38;
        r4 = r7;
        r6 = r6 + 1;
        r1 = r19;
        goto L7
    L38:
        return r19;
    L29:
        return r1;
    }

    E copyEntry(E r3, E r4) {
        return (E) segmentFor(r3.getHash()).copyEntry(r3, r4);
    }

    Segment<K, V, E, S> createSegment(int r2) {
        return this.entryHelper.newSegment(this, r2);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> r0 = this.entrySet;
        if (r0 != null) goto L7;
        EntrySet r1 = new EntrySet(this);
        this.entrySet = r1;
        return r1;
    L7:
        return r0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V get(@CheckForNull Object r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        int r0 = hash(r3);
        return segmentFor(r0).get(r3, r0);
    }

    @CheckForNull
    E getEntry(@CheckForNull Object r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        int r0 = hash(r3);
        return (E) segmentFor(r0).getEntry(r3, r0);
    }

    @CheckForNull
    V getLiveValue(E r2) {
        if (r2.getKey() != null) goto L7;
        return null;
    L7:
        return (V) r2.getValue();
    }

    int hash(Object r3) {
        return rehash(this.keyEquivalence.hash(r3));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        long r0 = 0;
        Segment<K, V, E, S>[] r2 = this.segments;
        int r3 = 0;
    L4:
        if (r3 >= r2.length) goto L10;
        if (r2[r3].count != 0) goto L7;
        r0 = r0 + ((long) r2[r3].modCount);
        r3 = r3 + 1;
        goto L4
    L7:
        return false;
    L10:
        if (r0 == 0) goto L22;
        int r6 = 0;
    L13:
        if (r6 >= r2.length) goto L19;
        if (r2[r6].count != 0) goto L16;
        r0 = r0 - ((long) r2[r6].modCount);
        r6 = r6 + 1;
        goto L13
    L16:
        return false;
    L19:
        if (r0 == 0) goto L21;
        return false;
    L21:
        return true;
    L22:
        return true;
    }

    boolean isLiveForTesting(InternalEntry<K, V, ?> r2) {
        if (segmentFor(r2.getHash()).getLiveValueForTesting(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> r0 = this.keySet;
        if (r0 != null) goto L7;
        KeySet r1 = new KeySet(this);
        this.keySet = r1;
        return r1;
    L7:
        return r0;
    }

    Strength keyStrength() {
        return this.entryHelper.keyStrength();
    }

    final Segment<K, V, E, S>[] newSegmentArray(int r2) {
        return new Segment[r2];
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V put(K r4, V r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        int r0 = hash(r4);
        return segmentFor(r0).put(r4, r0, r5, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> r5) {
        Iterator<Map.Entry<? extends K, ? extends V>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<? extends K, ? extends V> r1 = r0.next();
        put(r1.getKey(), r1.getValue());
        goto L4
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CheckForNull
    public V putIfAbsent(K r4, V r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        int r0 = hash(r4);
        return segmentFor(r0).put(r4, r0, r5, true);
    }

    void reclaimKey(E r3) {
        int r0 = r3.getHash();
        segmentFor(r0).reclaimKey(r3, r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void reclaimValue(WeakValueReference<K, V, E> r5) {
        InternalEntry r0 = r5.getEntry();
        int r1 = r0.getHash();
        segmentFor(r1).reclaimValue(r0.getKey(), r1, r5);
    }

    @Override // java.util.AbstractMap, java.util.Map
    @CheckForNull
    public V remove(@CheckForNull Object r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        int r0 = hash(r3);
        return segmentFor(r0).remove(r3, r0);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(@CheckForNull Object r3, @CheckForNull Object r4) {
        if (r3 == null) goto L7;
        if (r4 == null) goto L9;
        int r0 = hash(r3);
        return segmentFor(r0).remove(r3, r0, r4);
    L9:
        return false;
    L7:
        return false;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CheckForNull
    public V replace(K r3, V r4) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r4);
        int r0 = hash(r3);
        return segmentFor(r0).replace(r3, r0, r4);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K r3, @CheckForNull V r4, V r5) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkNotNull(r5);
        if (r4 != null) goto L6;
        return false;
    L6:
        int r0 = hash(r3);
        return segmentFor(r0).replace(r3, r0, r4, r5);
    }

    Segment<K, V, E, S> segmentFor(int r4) {
        return this.segments[(r4 >>> this.segmentShift) & this.segmentMask];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        Segment<K, V, E, S>[] r0 = this.segments;
        long r1 = 0;
        int r3 = 0;
    L4:
        if (r3 >= r0.length) goto L7;
        r1 = r1 + ((long) r0[r3].count);
        r3 = r3 + 1;
        goto L4
    L7:
        return Ints.saturatedCast(r1);
    }

    Equivalence<Object> valueEquivalence() {
        return this.entryHelper.valueStrength().defaultEquivalence();
    }

    Strength valueStrength() {
        return this.entryHelper.valueStrength();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> r0 = this.values;
        if (r0 != null) goto L7;
        Values r1 = new Values(this);
        this.values = r1;
        return r1;
    L7:
        return r0;
    }

    Object writeReplace() {
        return new SerializationProxy(this.entryHelper.keyStrength(), this.entryHelper.valueStrength(), this.keyEquivalence, this.entryHelper.valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
    }
}
