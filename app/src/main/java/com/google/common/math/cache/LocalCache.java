package com.google.common.cache;

import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final Queue<?> DISCARDING_QUEUE = null;
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final ValueReference<Object, Object> UNSET = null;
    static final Logger logger = null;
    final int concurrencyLevel;

    @CheckForNull
    final CacheLoader<? super K, V> defaultLoader;
    final EntryFactory entryFactory;

    @CheckForNull
    @LazyInit
    Set<Map.Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final AbstractCache.StatsCounter globalStatsCounter;
    final Equivalence<Object> keyEquivalence;

    @CheckForNull
    @LazyInit
    Set<K> keySet;
    final Strength keyStrength;
    final long maxWeight;
    final long refreshNanos;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;

    @CheckForNull
    @LazyInit
    Collection<V> values;
    final Weigher<K, V> weigher;

    abstract class AbstractCacheSet<T> extends AbstractSet<T> {
        final /* synthetic */ LocalCache this$0;

        AbstractCacheSet(LocalCache r1) {
            this.this$0 = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.this$0.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }
    }

    static abstract class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
        AbstractReferenceEntry() {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public K getKey() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long r2) {
            throw new UnsupportedOperationException();
        }
    }

    static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;

        AccessQueue() {
            this.head = new C05661(this);
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            ReferenceEntry<K, V> r0 = this.head.getNextInAccessQueue();
        L4:
            if (r0 == this.head) goto L6;
            ReferenceEntry<K, V> r1 = r0.getNextInAccessQueue();
            LocalCache.nullifyAccessOrder(r0);
            r0 = r1;
            goto L4
        L6:
            this.head.setNextInAccessQueue(this.head);
            this.head.setPreviousInAccessQueue(this.head);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object r4) {
            if (((ReferenceEntry) r4).getNextInAccessQueue() == NullEntry.INSTANCE) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            if (this.head.getNextInAccessQueue() != this.head) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<ReferenceEntry<K, V>> iterator() {
            final ReferenceEntry<K, V> r1 = peek();
            return new C05672(this, r1);
        }

        public boolean offer(ReferenceEntry<K, V> r3) {
            LocalCache.connectAccessOrder(r3.getPreviousInAccessQueue(), r3.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), r3);
            LocalCache.connectAccessOrder(r3, this.head);
            return true;
        }

        @Override // java.util.Queue
        public /* bridge */ /* synthetic */ boolean offer(Object r1) {
            return offer((ReferenceEntry) r1);
        }

        @Override // java.util.Queue
        @CheckForNull
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> r0 = this.head.getNextInAccessQueue();
            if (r0 != this.head) goto L6;
            return null;
        L6:
            return r0;
        }

        @Override // java.util.Queue
        @CheckForNull
        public /* bridge */ /* synthetic */ Object peek() {
            return peek();
        }

        @Override // java.util.Queue
        @CheckForNull
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> r0 = this.head.getNextInAccessQueue();
            if (r0 != this.head) goto L6;
            return null;
        L6:
            remove(r0);
            return r0;
        }

        @Override // java.util.Queue
        @CheckForNull
        public /* bridge */ /* synthetic */ Object poll() {
            return poll();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object r5) {
            ReferenceEntry r0 = (ReferenceEntry) r5;
            ReferenceEntry<K, V> r1 = r0.getPreviousInAccessQueue();
            ReferenceEntry<K, V> r2 = r0.getNextInAccessQueue();
            LocalCache.connectAccessOrder(r1, r2);
            LocalCache.nullifyAccessOrder(r0);
            if (r2 == NullEntry.INSTANCE) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int r0 = 0;
            ReferenceEntry<K, V> r1 = this.head.getNextInAccessQueue();
        L4:
            if (r1 == this.head) goto L6;
            r0 = r0 + 1;
            r1 = r1.getNextInAccessQueue();
            goto L4
        L6:
            return r0;
        }
    }

    static abstract enum EntryFactory extends Enum<EntryFactory> {
        private static final /* synthetic */ EntryFactory[] $VALUES = null;
        static final int ACCESS_MASK = 1;
        public static final EntryFactory STRONG = null;
        public static final EntryFactory STRONG_ACCESS = null;
        public static final EntryFactory STRONG_ACCESS_WRITE = null;
        public static final EntryFactory STRONG_WRITE = null;
        public static final EntryFactory WEAK = null;
        public static final EntryFactory WEAK_ACCESS = null;
        public static final EntryFactory WEAK_ACCESS_WRITE = null;
        static final int WEAK_MASK = 4;
        public static final EntryFactory WEAK_WRITE = null;
        static final int WRITE_MASK = 2;
        static final EntryFactory[] factories = null;

        private static /* synthetic */ EntryFactory[] $values() {
            return new EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
        }

        static {
            final String r1 = "STRONG";
            final int r2 = 0;
            STRONG = new C05681(r1, r2);
            final String r3 = "STRONG_ACCESS";
            final int r4 = 1;
            STRONG_ACCESS = new C05692(r3, r4);
            final String r5 = "STRONG_WRITE";
            final int r6 = 2;
            STRONG_WRITE = new C05703(r5, r6);
            final String r7 = "STRONG_ACCESS_WRITE";
            final int r8 = 3;
            STRONG_ACCESS_WRITE = new C05714(r7, r8);
            final String r9 = "WEAK";
            final int r10 = 4;
            WEAK = new C05725(r9, r10);
            final String r11 = "WEAK_ACCESS";
            final int r12 = 5;
            WEAK_ACCESS = new C05736(r11, r12);
            final String r13 = "WEAK_WRITE";
            final int r14 = 6;
            WEAK_WRITE = new C05747(r13, r14);
            final String r15 = "WEAK_ACCESS_WRITE";
            final int r16 = 7;
            WEAK_ACCESS_WRITE = new C05758(r15, r16);
            $VALUES = $values();
            factories = new EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
        }

        private EntryFactory(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ EntryFactory(String r1, int r2, C05641 r3) {
            this(r1, r2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        static EntryFactory getFactory(Strength r2, boolean r3, boolean r4) {
            int r1 = 0;
            if (r2 != Strength.WEAK) goto L5;
            char r0 = 4;
        L6:
            boolean r5 = r0 | (r3 ? 1 : 0);
            if (r4 == false) goto L10;
            r1 = 2;
        L10:
            return factories[(r5 ? 1 : 0) | r1];
        L5:
            r0 = 0;
            goto L6
        }

        public static EntryFactory valueOf(String r1) {
            return (EntryFactory) Enum.valueOf(EntryFactory.class, r1);
        }

        public static EntryFactory[] values() {
            return (EntryFactory[]) $VALUES.clone();
        }

        <K, V> void copyAccessEntry(ReferenceEntry<K, V> r3, ReferenceEntry<K, V> r4) {
            r4.setAccessTime(r3.getAccessTime());
            LocalCache.connectAccessOrder(r3.getPreviousInAccessQueue(), r4);
            LocalCache.connectAccessOrder(r4, r3.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(r3);
        }

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> r2, ReferenceEntry<K, V> r3, ReferenceEntry<K, V> r4, K r5) {
            return newEntry(r2, r5, r3.getHash(), r4);
        }

        <K, V> void copyWriteEntry(ReferenceEntry<K, V> r3, ReferenceEntry<K, V> r4) {
            r4.setWriteTime(r3.getWriteTime());
            LocalCache.connectWriteOrder(r3.getPreviousInWriteQueue(), r4);
            LocalCache.connectWriteOrder(r4, r3.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(r3);
        }

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> r1, K r2, int r3, @CheckForNull ReferenceEntry<K, V> r4);
    }

    final class EntryIterator extends LocalCache<K, V>.HashIterator<Map.Entry<K, V>> {
        final /* synthetic */ LocalCache this$0;

        EntryIterator(LocalCache r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public /* bridge */ /* synthetic */ Object next() {
            return next();
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    final class EntrySet extends LocalCache<K, V>.AbstractCacheSet<Map.Entry<K, V>> {
        final /* synthetic */ LocalCache this$0;

        EntrySet(LocalCache r1) {
            this.this$0 = r1;
            super(r1);
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
            if (this.this$0.valueEquivalence.equivalent(r0.getValue(), r3) == false) goto L15;
            return true;
        L15:
            return false;
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
    }

    abstract class HashIterator<T> implements Iterator<T> {

        @CheckForNull
        Segment<K, V> currentSegment;

        @CheckForNull
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;

        @CheckForNull
        LocalCache<K, V>.WriteThroughEntry lastReturned;

        @CheckForNull
        ReferenceEntry<K, V> nextEntry;

        @CheckForNull
        LocalCache<K, V>.WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex;
        final /* synthetic */ LocalCache this$0;

        HashIterator(LocalCache r2) {
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
            Segment<K, V>[] r0 = this.this$0.segments;
            int r1 = this.nextSegmentIndex;
            this.nextSegmentIndex = r1 - 1;
            this.currentSegment = r0[r1];
            if (this.currentSegment.count == 0) goto L9;
            this.currentTable = this.currentSegment.table;
            this.nextTableIndex = this.currentTable.length() - 1;
            if (nextInTable() == false) goto L9;
            return;
        }

        boolean advanceTo(ReferenceEntry<K, V> r7) {
            long r0 = this.this$0.ticker.read();     // Catch: Throwable -> L9
            K r2 = r7.getKey();     // Catch: Throwable -> L9
            Object r3 = this.this$0.getLiveValue(r7, r0);     // Catch: Throwable -> L9
            if (r3 == null) goto L7;
            this.nextExternal = new WriteThroughEntry(this.this$0, r2, r3);     // Catch: Throwable -> L9
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

        LocalCache<K, V>.WriteThroughEntry nextEntry() {
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
            this.nextEntry = this.nextEntry.getNext();
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
            AtomicReferenceArray<ReferenceEntry<K, V>> r0 = this.currentTable;
            int r1 = this.nextTableIndex;
            this.nextTableIndex = r1 - 1;
            ReferenceEntry<K, V> r2 = r0.get(r1);
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
            Preconditions.checkState(r0);
            this.this$0.remove(this.lastReturned.getKey());
            this.lastReturned = null;
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    final class KeyIterator extends LocalCache<K, V>.HashIterator<K> {
        final /* synthetic */ LocalCache this$0;

        KeyIterator(LocalCache r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    final class KeySet extends LocalCache<K, V>.AbstractCacheSet<K> {
        final /* synthetic */ LocalCache this$0;

        KeySet(LocalCache r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object r2) {
            return this.this$0.containsKey(r2);
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
    }

    static final class LoadingSerializationProxy<K, V> extends ManualSerializationProxy<K, V> implements LoadingCache<K, V>, Serializable {
        private static final long serialVersionUID = 1;

        @CheckForNull
        transient LoadingCache<K, V> autoDelegate;

        LoadingSerializationProxy(LocalCache<K, V> r1) {
            super(r1);
        }

        private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
            r3.defaultReadObject();
            this.autoDelegate = (LoadingCache<K, V>) recreateCacheBuilder().build(this.loader);
        }

        private Object readResolve() {
            return this.autoDelegate;
        }

        @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
        public V apply(K r2) {
            return this.autoDelegate.apply(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public V get(K r2) throws ExecutionException {
            return this.autoDelegate.get(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public ImmutableMap<K, V> getAll(Iterable<? extends K> r2) throws ExecutionException {
            return this.autoDelegate.getAll(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public V getUnchecked(K r2) {
            return this.autoDelegate.getUnchecked(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public void refresh(K r2) {
            this.autoDelegate.refresh(r2);
        }
    }

    static class LoadingValueReference<K, V> implements ValueReference<K, V> {
        final SettableFuture<V> futureValue;
        volatile ValueReference<K, V> oldValue;
        final Stopwatch stopwatch;

        public LoadingValueReference() {
            this(LocalCache.unset());
        }

        public LoadingValueReference(ValueReference<K, V> r2) {
            this.futureValue = SettableFuture.create();
            this.stopwatch = Stopwatch.createUnstarted();
            this.oldValue = r2;
        }

        private ListenableFuture<V> fullyFailedFuture(Throwable r2) {
            return Futures.immediateFailedFuture(r2);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> r1, @CheckForNull V r2, ReferenceEntry<K, V> r3) {
            return this;
        }

        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V get() {
            return this.oldValue.get();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.oldValue.getWeight();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return this.oldValue.isActive();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX INFO: renamed from: lambda$loadFuture$0$com-google-common-cache-LocalCache$LoadingValueReference */
        /* synthetic */ Object m194x59597480(Object r1) {
            set(r1);
            return r1;
        }

        public ListenableFuture<V> loadFuture(K r5, CacheLoader<? super K, V> r6) {
            this.stopwatch.start();     // Catch: Throwable -> L15
            V r0 = this.oldValue.get();     // Catch: Throwable -> L15
            if (r0 != null) goto L9;
            V r1 = r6.load(r5);     // Catch: Throwable -> L15
            if (set(r1) == false) goto L8;
            return this.futureValue;
        L8:
            return Futures.immediateFuture(r1);
        L9:
            ListenableFuture<V> r2 = r6.reload(r5, r0);     // Catch: Throwable -> L15
            if (r2 != null) goto L14;
            return Futures.immediateFuture(null);
        L14:
            return Futures.transform(r2, new LocalCache$LoadingValueReference$$ExternalSyntheticLambda0(this), MoreExecutors.directExecutor());
        L15:
            th = move-exception;
            if (setException(th) == false) goto L19;
            ListenableFuture<V> r3 = this.futureValue;
        L21:
            if ((th instanceof InterruptedException) == false) goto L23;
            Thread.currentThread().interrupt();
        L23:
            return r3;
        L19:
            r3 = fullyFailedFuture(th);
            goto L21
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(@CheckForNull V r2) {
            if (r2 == null) goto L4;
            set(r2);
            return;
        L4:
            this.oldValue = LocalCache.unset();
        }

        public boolean set(@CheckForNull V r2) {
            return this.futureValue.set(r2);
        }

        public boolean setException(Throwable r2) {
            return this.futureValue.setException(r2);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() throws ExecutionException {
            return (V) Uninterruptibles.getUninterruptibly(this.futureValue);
        }
    }

    static class LocalLoadingCache<K, V> extends LocalManualCache<K, V> implements LoadingCache<K, V> {
        private static final long serialVersionUID = 1;

        LocalLoadingCache(CacheBuilder<? super K, ? super V> r3, CacheLoader<? super K, V> r4) {
            super(new LocalCache(r3, (CacheLoader) Preconditions.checkNotNull(r4)), null);
        }

        private void readObject(ObjectInputStream r3) throws InvalidObjectException {
            throw new InvalidObjectException("Use LoadingSerializationProxy");
        }

        @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
        public final V apply(K r2) {
            return getUnchecked(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public V get(K r2) throws ExecutionException {
            return this.localCache.getOrLoad(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public ImmutableMap<K, V> getAll(Iterable<? extends K> r2) throws ExecutionException {
            return this.localCache.getAll(r2);
        }

        @Override // com.google.common.cache.LoadingCache
        public V getUnchecked(K r4) {
            return get(r4);
        L4:
            e = move-exception;
            throw new UncheckedExecutionException(e.getCause());
        }

        @Override // com.google.common.cache.LoadingCache
        public void refresh(K r2) {
            this.localCache.refresh(r2);
        }

        @Override // com.google.common.cache.LocalCache.LocalManualCache
        Object writeReplace() {
            return new LoadingSerializationProxy(this.localCache);
        }
    }

    static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
        private static final long serialVersionUID = 1;
        final LocalCache<K, V> localCache;

        LocalManualCache(CacheBuilder<? super K, ? super V> r3) {
            this(new LocalCache(r3, null));
        }

        private LocalManualCache(LocalCache<K, V> r1) {
            this.localCache = r1;
        }

        /* synthetic */ LocalManualCache(LocalCache r1, C05641 r2) {
            this(r1);
        }

        private void readObject(ObjectInputStream r3) throws InvalidObjectException {
            throw new InvalidObjectException("Use ManualSerializationProxy");
        }

        @Override // com.google.common.cache.Cache
        public ConcurrentMap<K, V> asMap() {
            return this.localCache;
        }

        @Override // com.google.common.cache.Cache
        public void cleanUp() {
            this.localCache.cleanUp();
        }

        @Override // com.google.common.cache.Cache
        public V get(K r3, final Callable<? extends V> r4) throws ExecutionException {
            Preconditions.checkNotNull(r4);
            return this.localCache.get(r3, new C05761(this, r4));
        }

        @Override // com.google.common.cache.Cache
        public ImmutableMap<K, V> getAllPresent(Iterable<?> r2) {
            return this.localCache.getAllPresent(r2);
        }

        @Override // com.google.common.cache.Cache
        @CheckForNull
        public V getIfPresent(Object r2) {
            return this.localCache.getIfPresent(r2);
        }

        @Override // com.google.common.cache.Cache
        public void invalidate(Object r2) {
            Preconditions.checkNotNull(r2);
            this.localCache.remove(r2);
        }

        @Override // com.google.common.cache.Cache
        public void invalidateAll() {
            this.localCache.clear();
        }

        @Override // com.google.common.cache.Cache
        public void invalidateAll(Iterable<?> r2) {
            this.localCache.invalidateAll(r2);
        }

        @Override // com.google.common.cache.Cache
        public void put(K r2, V r3) {
            this.localCache.put(r2, r3);
        }

        @Override // com.google.common.cache.Cache
        public void putAll(Map<? extends K, ? extends V> r2) {
            this.localCache.putAll(r2);
        }

        @Override // com.google.common.cache.Cache
        public long size() {
            return this.localCache.longSize();
        }

        @Override // com.google.common.cache.Cache
        public CacheStats stats() {
            AbstractCache.SimpleStatsCounter r0 = new AbstractCache.SimpleStatsCounter();
            r0.incrementBy(this.localCache.globalStatsCounter);
            Segment<K, V>[] r1 = this.localCache.segments;
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            r0.incrementBy(r1[r3].statsCounter);
            r3 = r3 + 1;
            goto L3
        L6:
            return r0.snapshot();
        }

        Object writeReplace() {
            return new ManualSerializationProxy(this.localCache);
        }
    }

    static class ManualSerializationProxy<K, V> extends ForwardingCache<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        final int concurrencyLevel;

        @CheckForNull
        transient Cache<K, V> delegate;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final CacheLoader<? super K, V> loader;
        final long maxWeight;
        final RemovalListener<? super K, ? super V> removalListener;

        @CheckForNull
        final Ticker ticker;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;
        final Weigher<K, V> weigher;

        private ManualSerializationProxy(Strength r17, Strength r18, Equivalence<Object> r19, Equivalence<Object> r20, long r21, long r23, long r25, Weigher<K, V> r27, int r28, RemovalListener<? super K, ? super V> r29, Ticker r30, CacheLoader<? super K, V> r31) {
            this.keyStrength = r17;
            this.valueStrength = r18;
            this.keyEquivalence = r19;
            this.valueEquivalence = r20;
            this.expireAfterWriteNanos = r21;
            this.expireAfterAccessNanos = r23;
            this.maxWeight = r25;
            this.weigher = r27;
            this.concurrencyLevel = r28;
            this.removalListener = r29;
            if (r30 != Ticker.systemTicker()) goto L5;
        L8:
            Ticker r15 = null;
        L9:
            this.ticker = r15;
            this.loader = r31;
            return;
        L5:
            if (r30 == CacheBuilder.NULL_TICKER) goto L8;
            r15 = r30;
            goto L9
        }

        ManualSerializationProxy(LocalCache<K, V> r18) {
            this(r18.keyStrength, r18.valueStrength, r18.keyEquivalence, r18.valueEquivalence, r18.expireAfterWriteNanos, r18.expireAfterAccessNanos, r18.maxWeight, r18.weigher, r18.concurrencyLevel, r18.removalListener, r18.ticker, r18.defaultLoader);
        }

        private void readObject(ObjectInputStream r3) throws IOException, ClassNotFoundException {
            r3.defaultReadObject();
            this.delegate = (Cache<K, V>) recreateCacheBuilder().build();
        }

        private Object readResolve() {
            return this.delegate;
        }

        @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected Cache<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        CacheBuilder<K, V> recreateCacheBuilder() {
            CacheBuilder<K, V> r0 = (CacheBuilder<K, V>) CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
            r0.strictParsing = false;
            if (this.expireAfterWriteNanos <= 0) goto L6;
            r0.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
        L6:
            if (this.expireAfterAccessNanos <= 0) goto L9;
            r0.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
        L9:
            if (this.weigher == CacheBuilder.OneWeigher.INSTANCE) goto L15;
            r0.weigher(this.weigher);
            if (this.maxWeight == (-1)) goto L18;
            r0.maximumWeight(this.maxWeight);
        L18:
            if (this.ticker == null) goto L20;
            r0.ticker(this.ticker);
        L20:
            return r0;
        L15:
            if (this.maxWeight == (-1)) goto L18;
            r0.maximumSize(this.maxWeight);
            goto L18
        }
    }

    private static final enum NullEntry extends Enum<NullEntry> implements ReferenceEntry<Object, Object> {
        private static final /* synthetic */ NullEntry[] $VALUES = null;
        public static final NullEntry INSTANCE = null;

        private static /* synthetic */ NullEntry[] $values() {
            return new NullEntry[]{INSTANCE};
        }

        static {
            INSTANCE = new NullEntry("INSTANCE", 0);
            $VALUES = $values();
        }

        private NullEntry(String r1, int r2) {
            super(r1, r2);
        }

        public static NullEntry valueOf(String r1) {
            return (NullEntry) Enum.valueOf(NullEntry.class, r1);
        }

        public static NullEntry[] values() {
            return (NullEntry[]) $VALUES.clone();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        @CheckForNull
        public Object getKey() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        @CheckForNull
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        @CheckForNull
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long r1) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<Object, Object> r1) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<Object, Object> r1) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> r1) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> r1) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<Object, Object> r1) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long r1) {
        }
    }

    static class Segment<K, V> extends ReentrantLock {
        final Queue<ReferenceEntry<K, V>> accessQueue;
        volatile int count;

        @CheckForNull
        final ReferenceQueue<K> keyReferenceQueue;
        final LocalCache<K, V> map;
        final long maxSegmentWeight;
        int modCount;
        final AtomicInteger readCount;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AbstractCache.StatsCounter statsCounter;

        @CheckForNull
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        long totalWeight;

        @CheckForNull
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> writeQueue;

        Segment(LocalCache<K, V> r3, int r4, long r5, AbstractCache.StatsCounter r7) {
            this.readCount = new AtomicInteger();
            this.map = r3;
            this.maxSegmentWeight = r5;
            this.statsCounter = (AbstractCache.StatsCounter) Preconditions.checkNotNull(r7);
            initTable(newEntryArray(r4));
            ReferenceQueue<V> r1 = null;
            if (r3.usesKeyReferences() == false) goto L5;
            ReferenceQueue<K> r0 = new ReferenceQueue();
        L6:
            this.keyReferenceQueue = r0;
            if (r3.usesValueReferences() == false) goto L9;
            r1 = new ReferenceQueue();
        L9:
            this.valueReferenceQueue = r1;
            if (r3.usesAccessQueue() == false) goto L12;
            Queue<ReferenceEntry<K, V>> r2 = new ConcurrentLinkedQueue();
        L13:
            this.recencyQueue = r2;
            if (r3.usesWriteQueue() == false) goto L16;
            Queue<ReferenceEntry<K, V>> r6 = new WriteQueue();
        L17:
            this.writeQueue = r6;
            if (r3.usesAccessQueue() == false) goto L20;
            Queue<ReferenceEntry<K, V>> r8 = new AccessQueue();
        L21:
            this.accessQueue = r8;
            return;
        L20:
            r8 = LocalCache.discardingQueue();
            goto L21
        L16:
            r6 = LocalCache.discardingQueue();
            goto L17
        L12:
            r2 = LocalCache.discardingQueue();
            goto L13
        L5:
            r0 = null;
            goto L6
        }

        void cleanUp() {
            runLockedCleanup(this.map.ticker.read());
            runUnlockedCleanup();
        }

        void clear() {
            if (this.count == 0) goto L37;
            lock();
            preWriteCleanup(this.map.ticker.read());     // Catch: Throwable -> L32
            AtomicReferenceArray<ReferenceEntry<K, V>> r2 = this.table;     // Catch: Throwable -> L32
            int r3 = 0;
        L6:
            if (r3 >= r2.length()) goto L24;
            ReferenceEntry<K, V> r4 = r2.get(r3);     // Catch: Throwable -> L32
        L9:
            if (r4 == null) goto L23;
            if (r4.getValueReference().isActive() == false) goto L22;
            K r5 = r4.getKey();     // Catch: Throwable -> L32
            V r9 = r4.getValueReference().get();     // Catch: Throwable -> L32
            if (r5 == null) goto L17;
            if (r9 == null) goto L17;
            RemovalCause r6 = RemovalCause.EXPLICIT;     // Catch: Throwable -> L32
        L38:
            enqueueNotification(r5, r4.getHash(), r9, r4.getValueReference().getWeight(), r6);     // Catch: Throwable -> L30
        L30:
            th = th;
        L34:
            unlock();
            postWriteCleanup();
            throw th;
        L17:
            r6 = RemovalCause.COLLECTED;     // Catch: Throwable -> L32
        L22:
            r4 = r4.getNext();     // Catch: Throwable -> L30
            goto L9
        L23:
            r3 = r3 + 1;     // Catch: Throwable -> L30
            goto L6
        L24:
            int r7 = 0;
        L26:
            if (r7 >= r2.length()) goto L28;
            r2.set(r7, null);     // Catch: Throwable -> L30
            r7 = r7 + 1;     // Catch: Throwable -> L30
            goto L26
        L28:
            clearReferenceQueues();     // Catch: Throwable -> L30
            this.writeQueue.clear();     // Catch: Throwable -> L30
            this.accessQueue.clear();     // Catch: Throwable -> L30
            this.readCount.set(0);     // Catch: Throwable -> L30
            this.modCount++;     // Catch: Throwable -> L30
            this.count = 0;     // Catch: Throwable -> L30
            unlock();
            postWriteCleanup();
            return;
        L32:
            th = th;
            goto L34
        }

        void clearKeyReferenceQueue() {
        L3:
            if (this.keyReferenceQueue.poll() != null) goto L3;
        }

        void clearReferenceQueues() {
            if (this.map.usesKeyReferences() == false) goto L6;
            clearKeyReferenceQueue();
        L6:
            if (this.map.usesValueReferences() == false) goto L9;
            clearValueReferenceQueue();
            return;
        }

        void clearValueReferenceQueue() {
        L3:
            if (this.valueReferenceQueue.poll() != null) goto L3;
        }

        boolean containsKey(Object r6, int r7) {
            boolean r1 = false;
            if (this.count == 0) goto L13;
            ReferenceEntry<K, V> r0 = getLiveEntry(r6, r7, this.map.ticker.read());     // Catch: Throwable -> L15
            if (r0 != null) goto L8;
            postReadCleanup();
            return false;
        L8:
            if (r0.getValueReference().get() == null) goto L11;
            r1 = true;
        L11:
            postReadCleanup();
            return r1;
        L13:
            postReadCleanup();
            return false;
        L15:
            th = move-exception;
            postReadCleanup();
            throw th;
        }

        boolean containsValue(Object r9) {
        L20:
            th = move-exception;
            postReadCleanup();
            throw th;
        L3:
            if (this.count == 0) goto L18;
            long r0 = this.map.ticker.read();     // Catch: Throwable -> L20
            AtomicReferenceArray<ReferenceEntry<K, V>> r2 = this.table;     // Catch: Throwable -> L20
            int r3 = r2.length();     // Catch: Throwable -> L20
            int r4 = 0;
        L5:
            if (r4 >= r3) goto L18;
            ReferenceEntry<K, V> r5 = r2.get(r4);     // Catch: Throwable -> L20
        L7:
            if (r5 == null) goto L17;
            V r6 = getLiveValue(r5, r0);     // Catch: Throwable -> L20
            if (r6 != null) goto L12;
        L16:
            r5 = r5.getNext();     // Catch: Throwable -> L20
            goto L7
        L12:
            if (this.map.valueEquivalence.equivalent(r9, r6) == false) goto L16;
            postReadCleanup();
            return true;
        L17:
            r4 = r4 + 1;
        L18:
            postReadCleanup();
            return false;
        }

        @CheckForNull
        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> r6, ReferenceEntry<K, V> r7) {
            K r0 = r6.getKey();
            if (r0 != null) goto L5;
            return null;
        L5:
            ValueReference<K, V> r2 = r6.getValueReference();
            V r3 = r2.get();
            if (r3 == null) goto L8;
        L10:
            ReferenceEntry<K, V> r1 = this.map.entryFactory.copyEntry(this, r6, r7, r0);
            r1.setValueReference(r2.copyFor(this.valueReferenceQueue, r3, r1));
            return r1;
        L8:
            if (r2.isActive() == false) goto L10;
            return null;
        }

        void drainKeyReferenceQueue() {
            int r0 = 0;
        L3:
            Reference<? extends K> r1 = this.keyReferenceQueue.poll();
            if (r1 == null) goto L9;
            LocalCache<K, V> r3 = this.map;
            r3.reclaimKey((ReferenceEntry) r1);
            r0 = r0 + 1;
            if (r0 != 16) goto L3;
            return;
        }

        void drainRecencyQueue() {
        L2:
            ReferenceEntry<K, V> r0 = this.recencyQueue.poll();
            if (r0 == null) goto L7;
            if (this.accessQueue.contains(r0) == false) goto L2;
            this.accessQueue.add(r0);
            goto L2
        }

        void drainReferenceQueues() {
            if (this.map.usesKeyReferences() == false) goto L6;
            drainKeyReferenceQueue();
        L6:
            if (this.map.usesValueReferences() == false) goto L9;
            drainValueReferenceQueue();
            return;
        }

        void drainValueReferenceQueue() {
            int r0 = 0;
        L3:
            Reference<? extends V> r1 = this.valueReferenceQueue.poll();
            if (r1 == null) goto L9;
            LocalCache<K, V> r3 = this.map;
            r3.reclaimValue((ValueReference) r1);
            r0 = r0 + 1;
            if (r0 != 16) goto L3;
            return;
        }

        void enqueueNotification(@CheckForNull K r5, int r6, @CheckForNull V r7, int r8, RemovalCause r9) {
            this.totalWeight -= (long) r8;
            if (r9.wasEvicted() == false) goto L6;
            this.statsCounter.recordEviction();
        L6:
            if (this.map.removalNotificationQueue == LocalCache.DISCARDING_QUEUE) goto L9;
            this.map.removalNotificationQueue.offer(RemovalNotification.create(r5, r7, r9));
            return;
        }

        void evictEntries(ReferenceEntry<K, V> r5) {
            if (this.map.evictsBySize() == true) goto L5;
            return;
        L5:
            drainRecencyQueue();
            if (r5.getValueReference().getWeight() <= this.maxSegmentWeight) goto L13;
            if (removeEntry(r5, r5.getHash(), RemovalCause.SIZE) == true) goto L13;
            throw new AssertionError();
        L13:
            if (this.totalWeight <= this.maxSegmentWeight) goto L19;
            ReferenceEntry<K, V> r0 = getNextEvictable();
            if (removeEntry(r0, r0.getHash(), RemovalCause.SIZE) == true) goto L13;
            throw new AssertionError();
        }

        void expand() {
            AtomicReferenceArray<ReferenceEntry<K, V>> r0 = this.table;
            int r1 = r0.length();
            if (r1 < 1073741824) goto L5;
            return;
        L5:
            int r2 = this.count;
            AtomicReferenceArray<ReferenceEntry<K, V>> r3 = newEntryArray(r1 << 1);
            this.threshold = (r3.length() * 3) / 4;
            int r4 = r3.length() - 1;
            int r5 = 0;
        L6:
            if (r5 >= r1) goto L26;
            ReferenceEntry<K, V> r6 = r0.get(r5);
            if (r6 == null) goto L25;
            ReferenceEntry<K, V> r7 = r6.getNext();
            int r8 = r6.getHash() & r4;
            if (r7 != null) goto L12;
            r3.set(r8, r6);
            goto L25
        L12:
            ReferenceEntry<K, V> r9 = r6;
            int r10 = r8;
            ReferenceEntry<K, V> r11 = r7;
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
            ReferenceEntry<K, V> r13 = r6;
        L19:
            if (r13 == r9) goto L25;
            int r14 = r13.getHash() & r4;
            ReferenceEntry<K, V> r15 = copyEntry(r13, r3.get(r14));
            if (r15 == null) goto L23;
            r3.set(r14, r15);
        L24:
            r13 = r13.getNext();
            goto L19
        L23:
            removeCollectedEntry(r13);
            r2 = r2 - 1;
        L25:
            r5 = r5 + 1;
            goto L6
        L26:
            this.table = r3;
            this.count = r2;
        }

        void expireEntries(long r4) {
            drainRecencyQueue();
        L3:
            ReferenceEntry<K, V> r0 = this.writeQueue.peek();
            if (r0 == null) goto L12;
            if (this.map.isExpired(r0, r4) == false) goto L12;
            if (removeEntry(r0, r0.getHash(), RemovalCause.EXPIRED) == true) goto L3;
            throw new AssertionError();
        L12:
            ReferenceEntry<K, V> r1 = this.accessQueue.peek();
            if (r1 == null) goto L21;
            if (this.map.isExpired(r1, r4) == false) goto L31;
            if (removeEntry(r1, r1.getHash(), RemovalCause.EXPIRED) == true) goto L12;
            throw new AssertionError();
        L31:
            return;
        }

        @CheckForNull
        V get(Object r13, int r14) {
        L23:
            th = move-exception;
            Throwable r15 = th;
        L25:
            postReadCleanup();
            throw r15;
        L3:
            if (this.count == 0) goto L21;
            long r2 = this.map.ticker.read();     // Catch: Throwable -> L23
            ReferenceEntry<K, V> r5 = getLiveEntry(r13, r14, r2);     // Catch: Throwable -> L23
            if (r5 != null) goto L8;
            postReadCleanup();
            return null;
        L8:
            V r8 = r5.getValueReference().get();     // Catch: Throwable -> L23
            if (r8 == null) goto L16;
            recordRead(r5, r2);     // Catch: Throwable -> L23
            V r16 = scheduleRefresh(r5, r5.getKey(), r14, r8, r2, this.map.defaultLoader);     // Catch: Throwable -> L18
            postReadCleanup();
            return r16;
        L16:
            tryDrainReferenceQueues();     // Catch: Throwable -> L18
        L18:
            th = move-exception;
            r15 = th;
        L21:
            postReadCleanup();
            return null;
        }

        V get(K r10, int r11, CacheLoader<? super K, V> r12) throws ExecutionException {
            Preconditions.checkNotNull(r10);
            Preconditions.checkNotNull(r12);
        L41:
            th = move-exception;
            Throwable r13 = th;
        L43:
            postReadCleanup();
            throw r13;
        L29:
            e = move-exception;
            ExecutionException r14 = e;
        L31:
            Throwable r15 = r14.getCause();     // Catch: Throwable -> L41
            if ((r15 instanceof Error) == true) goto L40;
            if ((r15 instanceof RuntimeException) == true) goto L36;
            throw r14;     // Catch: Throwable -> L41
        L36:
            throw new UncheckedExecutionException(r15);     // Catch: Throwable -> L41
        L40:
            throw new ExecutionError((Error) r15);     // Catch: Throwable -> L41
        L27:
            th = move-exception;
            r13 = th;
            goto L43
        L45:
            if (this.count == 0) goto L21;
            ReferenceEntry<K, V> r0 = getEntry(r10, r11);     // Catch: Throwable -> L27 ExecutionException -> L29
            if (r0 == null) goto L20;
            long r6 = this.map.ticker.read();     // Catch: Throwable -> L27 ExecutionException -> L29
            V r5 = getLiveValue(r0, r6);     // Catch: Throwable -> L27 ExecutionException -> L29
            if (r5 == null) goto L14;
            recordRead(r0, r6);     // Catch: Throwable -> L27 ExecutionException -> L29
            this.statsCounter.recordHits(1);     // Catch: Throwable -> L27 ExecutionException -> L29
        L11:
            V r16 = scheduleRefresh(r0, r10, r11, r5, r6, r12);     // Catch: ExecutionException -> L25 Throwable -> L41
            postReadCleanup();
            return r16;
        L14:
            K r3 = r10;
            ValueReference<K, V> r17 = r0.getValueReference();     // Catch: ExecutionException -> L25 Throwable -> L41
            if (r17.isLoading() == false) goto L22;
            V r18 = waitForLoadingValue(r0, r3, r17);     // Catch: ExecutionException -> L25 Throwable -> L41
            postReadCleanup();
            return r18;
        L22:
            V r19 = lockedGetOrLoad(r3, r11, r12);     // Catch: ExecutionException -> L25 Throwable -> L41
            postReadCleanup();
            return r19;
        L20:
            r3 = r10;
            goto L22
        L21:
            r3 = r10;
        L25:
            e = move-exception;
            r14 = e;
            goto L31
        }

        V getAndRecordStats(K r6, int r7, LoadingValueReference<K, V> r8, ListenableFuture<V> r9) throws ExecutionException {
            V r1 = (V) Uninterruptibles.getUninterruptibly(r9);
            if (r1 == null) goto L10;
            this.statsCounter.recordLoadSuccess(r8.elapsedNanos());     // Catch: Throwable -> L11
            storeLoadedValue(r6, r7, r8, r1);     // Catch: Throwable -> L11
            if (r1 != null) goto L8;
            this.statsCounter.recordLoadException(r8.elapsedNanos());
            removeLoadingValue(r6, r7, r8);
        L8:
            return r1;
        L10:
            throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + r6 + ".");     // Catch: Throwable -> L11
        L11:
            th = move-exception;
            if (0 != 0) goto L14;
            this.statsCounter.recordLoadException(r8.elapsedNanos());
            removeLoadingValue(r6, r7, r8);
        L14:
            throw th;
        }

        @CheckForNull
        ReferenceEntry<K, V> getEntry(Object r4, int r5) {
            ReferenceEntry<K, V> r0 = getFirst(r5);
        L3:
            if (r0 == null) goto L14;
            if (r0.getHash() != r5) goto L13;
            K r1 = r0.getKey();
            if (r1 != null) goto L11;
            tryDrainReferenceQueues();
            goto L13
        L11:
            if (this.map.keyEquivalence.equivalent(r4, r1) == false) goto L13;
            return r0;
        L13:
            r0 = r0.getNext();
            goto L3
        L14:
            return null;
        }

        ReferenceEntry<K, V> getFirst(int r3) {
            AtomicReferenceArray<ReferenceEntry<K, V>> r0 = this.table;
            return r0.get((r0.length() - 1) & r3);
        }

        @CheckForNull
        ReferenceEntry<K, V> getLiveEntry(Object r4, int r5, long r6) {
            ReferenceEntry<K, V> r0 = getEntry(r4, r5);
            if (r0 != null) goto L6;
            return null;
        L6:
            if (this.map.isExpired(r0, r6) == false) goto L9;
            tryExpireEntries(r6);
            return null;
        L9:
            return r0;
        }

        V getLiveValue(ReferenceEntry<K, V> r4, long r5) {
            if (r4.getKey() != null) goto L6;
            tryDrainReferenceQueues();
            return null;
        L6:
            V r0 = r4.getValueReference().get();
            if (r0 != null) goto L11;
            tryDrainReferenceQueues();
            return null;
        L11:
            if (this.map.isExpired(r4, r5) == false) goto L14;
            tryExpireEntries(r5);
            return null;
        L14:
            return r0;
        }

        ReferenceEntry<K, V> getNextEvictable() {
            Iterator<ReferenceEntry<K, V>> r0 = this.accessQueue.iterator();
        L4:
            if (r0.hasNext() == false) goto L10;
            ReferenceEntry<K, V> r1 = r0.next();
            if (r1.getValueReference().getWeight() <= 0) goto L4;
            return r1;
        L10:
            throw new AssertionError();
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> r5) {
            this.threshold = (r5.length() * 3) / 4;
            if (this.map.customWeigher() == false) goto L5;
        L7:
            this.table = r5;
            return;
        L5:
            if (this.threshold != this.maxSegmentWeight) goto L7;
            this.threshold++;
            goto L7
        }

        @CheckForNull
        LoadingValueReference<K, V> insertLoadingValueReference(K r13, int r14, boolean r15) {
            lock();
            long r1 = this.map.ticker.read();     // Catch: Throwable -> L25
            preWriteCleanup(r1);     // Catch: Throwable -> L25
            AtomicReferenceArray<ReferenceEntry<K, V>> r3 = this.table;     // Catch: Throwable -> L25
            int r4 = (r3.length() - 1) & r14;     // Catch: Throwable -> L25
            ReferenceEntry<K, V> r5 = (ReferenceEntry) r3.get(r4);     // Catch: Throwable -> L25
            ReferenceEntry r0 = r5;
        L4:
            if (r0 == null) goto L22;
            Object r6 = r0.getKey();     // Catch: Throwable -> L25
            if (r0.getHash() != r14) goto L21;
            if (r6 == null) goto L21;
            if (this.map.keyEquivalence.equivalent(r13, r6) == false) goto L21;
            ValueReference<K, V> r7 = r0.getValueReference();     // Catch: Throwable -> L25
            if (r7.isLoading() == true) goto L19;
            if (r15 == true) goto L14;
        L16:
            this.modCount++;     // Catch: Throwable -> L25
            LoadingValueReference<K, V> r8 = new LoadingValueReference(r7);     // Catch: Throwable -> L25
            r0.setValueReference(r8);     // Catch: Throwable -> L25
            unlock();
            postWriteCleanup();
            return r8;
        L14:
            if ((r1 - r0.getWriteTime()) >= this.map.refreshNanos) goto L16;
        L19:
            unlock();
            postWriteCleanup();
            return null;
        L21:
            r0 = r0.getNext();     // Catch: Throwable -> L25
            goto L4
        L22:
            this.modCount++;     // Catch: Throwable -> L25
            LoadingValueReference<K, V> r9 = new LoadingValueReference();     // Catch: Throwable -> L25
            ReferenceEntry<K, V> r10 = newEntry(r13, r14, r5);     // Catch: Throwable -> L25
            r10.setValueReference(r9);     // Catch: Throwable -> L25
            r3.set(r4, r10);     // Catch: Throwable -> L25
            unlock();
            postWriteCleanup();
            return r9;
        L25:
            th = move-exception;
            unlock();
            postWriteCleanup();
            throw th;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX INFO: renamed from: lambda$loadAsync$0$com-google-common-cache-LocalCache$Segment, reason: not valid java name */
        /* synthetic */ void m496lambda$loadAsync$0$comgooglecommoncacheLocalCache$Segment(Object r5, int r6, LoadingValueReference r7, ListenableFuture r8) {
            getAndRecordStats(r5, r6, r7, r8);     // Catch: Throwable -> L4
            return;
        L4:
            th = move-exception;
            LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", th);
            r7.setException(th);
        }

        ListenableFuture<V> loadAsync(final K r7, final int r8, final LoadingValueReference<K, V> r9, CacheLoader<? super K, V> r10) {
            final ListenableFuture<V> r5 = r9.loadFuture(r7, r10);
            r5.addListener(new LocalCache$Segment$$ExternalSyntheticLambda0(this, r7, r8, r9, r5), MoreExecutors.directExecutor());
            return r5;
        }

        V loadSync(K r3, int r4, LoadingValueReference<K, V> r5, CacheLoader<? super K, V> r6) throws ExecutionException {
            return getAndRecordStats(r3, r4, r5, r5.loadFuture(r3, r6));
        }

        V lockedGetOrLoad(K r19, int r20, CacheLoader<? super K, V> r21) throws ExecutionException {
            int r3 = r20;
            ValueReference<K, V> r2 = null;
            LoadingValueReference<K, V> r8 = null;
            boolean r9 = true;
            lock();
            long r4 = this.map.ticker.read();     // Catch: Throwable -> L62
            preWriteCleanup(r4);     // Catch: Throwable -> L62
            int r0 = this.count - 1;     // Catch: Throwable -> L62
            AtomicReferenceArray<ReferenceEntry<K, V>> r5 = this.table;     // Catch: Throwable -> L62
            int r14 = r3 & (r5.length() - 1);     // Catch: Throwable -> L62
            ReferenceEntry<K, V> r6 = r5.get(r14);     // Catch: Throwable -> L62
        L4:
            if (r6 == null) goto L35;
            K r7 = r6.getKey();     // Catch: Throwable -> L62
            if (r6.getHash() != r3) goto L34;
            if (r7 == null) goto L34;
            if (this.map.keyEquivalence.equivalent(r19, r7) == false) goto L34;
            ValueReference<K, V> r10 = r6.getValueReference();     // Catch: Throwable -> L62
        L30:
            th = th;
        L64:
            unlock();
            postWriteCleanup();
            throw th;
        L13:
            if (r10.isLoading() == false) goto L15;
            r9 = false;
            ReferenceEntry<K, V> r12 = r6;
            r2 = r10;
        L36:
            if (r9 == false) goto L42;
            r8 = new LoadingValueReference();     // Catch: Throwable -> L62
            if (r12 != null) goto L40;
            ReferenceEntry<K, V> r11 = newEntry(r19, r3, r6);     // Catch: Throwable -> L62
            r11.setValueReference(r8);     // Catch: Throwable -> L62
            r5.set(r14, r11);     // Catch: Throwable -> L62
        L43:
            unlock();
            postWriteCleanup();
            if (r9 == false) goto L61;
            monitor-enter(r11);     // Catch: Throwable -> L56
            V r1 = loadSync(r19, r3, r8, r21);     // Catch: Throwable -> L51
            monitor-exit(r11);     // Catch: Throwable -> L51
            this.statsCounter.recordMisses(1);
            return r1;
        L51:
            th = move-exception;
            throw th;     // Catch: Throwable -> L54
        L54:
            th = th;
        L58:
            this.statsCounter.recordMisses(1);
            throw th;
        L56:
            th = th;
            goto L58
        L61:
            return waitForLoadingValue(r11, r19, r2);
        L40:
            r12.setValueReference(r8);     // Catch: Throwable -> L62
            r11 = r12;
            goto L43
        L42:
            r11 = r12;
            goto L43
        L15:
            V r13 = r10.get();     // Catch: Throwable -> L30
            if (r13 == null) goto L18;
            r12 = r6;
            if (this.map.isExpired(r12, r4) == true) goto L22;
            recordLockedRead(r12, r4);     // Catch: Throwable -> L30
            this.statsCounter.recordHits(1);     // Catch: Throwable -> L30
            unlock();
            postWriteCleanup();
            return r13;
        L22:
            r3 = r20;
            enqueueNotification(r7, r3, r13, r10.getWeight(), RemovalCause.EXPIRED);     // Catch: Throwable -> L30
        L24:
            this.writeQueue.remove(r12);     // Catch: Throwable -> L30
            this.accessQueue.remove(r12);     // Catch: Throwable -> L30
            this.count = r0;     // Catch: Throwable -> L30
            r2 = r10;
            goto L36
        L18:
            r12 = r6;
            enqueueNotification(r7, r3, r13, r10.getWeight(), RemovalCause.COLLECTED);     // Catch: Throwable -> L28
            r3 = r20;
        L28:
            th = th;
        L34:
            r6 = r6.getNext();     // Catch: Throwable -> L62
            goto L4
        L35:
            r12 = r6;
        L62:
            th = th;
            goto L64
        }

        /* JADX WARN: Multi-variable type inference failed */
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
        ReferenceEntry<K, V> newEntry(K r3, int r4, @CheckForNull ReferenceEntry<K, V> r5) {
            return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(r3), r4, r5);
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int r2) {
            return new AtomicReferenceArray(r2);
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) != 0) goto L6;
            cleanUp();
            return;
        }

        void postWriteCleanup() {
            runUnlockedCleanup();
        }

        void preWriteCleanup(long r1) {
            runLockedCleanup(r1);
        }

        @CheckForNull
        V put(K r19, int r20, V r21, boolean r22) {
            Segment<K, V> r1 = this;
            int r2 = r20;
            r1.lock();
            long r7 = r1.map.ticker.read();     // Catch: Throwable -> L52
            r1.preWriteCleanup(r7);     // Catch: Throwable -> L52
            if ((r1.count + 1) <= r1.threshold) goto L6;
            r1.expand();     // Catch: Throwable -> L52
            int r0 = r1.count + 1;     // Catch: Throwable -> L52
        L6:
            AtomicReferenceArray<ReferenceEntry<K, V>> r4 = r1.table;     // Catch: Throwable -> L52
            int r10 = r2 & (r4.length() - 1);     // Catch: Throwable -> L52
            ReferenceEntry<K, V> r5 = r4.get(r10);     // Catch: Throwable -> L52
            ReferenceEntry<K, V> r12 = r5;
        L9:
            if (r12 == null) goto L46;
            K r6 = r12.getKey();     // Catch: Throwable -> L44
            if (r12.getHash() != r2) goto L43;
            if (r6 == null) goto L43;
            if (r1.map.keyEquivalence.equivalent(r19, r6) == false) goto L43;
            ValueReference<K, V> r8 = r12.getValueReference();     // Catch: Throwable -> L44
            V r9 = r8.get();     // Catch: Throwable -> L44
            if (r9 != null) goto L25;
            r1.modCount++;     // Catch: Throwable -> L44
            if (r8.isActive() == false) goto L20;
            r1.enqueueNotification(r19, r2, r9, r8.getWeight(), RemovalCause.COLLECTED);     // Catch: Throwable -> L44
            r1 = this;
            ReferenceEntry<K, V> r3 = r12;
            r1.setValue(r3, r19, r21, r7);     // Catch: Throwable -> L44
            int r11 = r1.count;     // Catch: Throwable -> L44
            r12 = r3;
        L21:
            r1.count = r11;     // Catch: Throwable -> L44
            r1.evictEntries(r12);     // Catch: Throwable -> L44
            r1.unlock();
            r1.postWriteCleanup();
            return null;
        L20:
            r1.setValue(r12, r19, r21, r7);     // Catch: Throwable -> L44
            r11 = r1.count + 1;     // Catch: Throwable -> L44
            goto L21
        L25:
            if (r22 == false) goto L62;
            r1.recordLockedRead(r12, r7);     // Catch: Throwable -> L44
            r1.unlock();
            r1.postWriteCleanup();
            return r9;
        L62:
            r1.modCount++;     // Catch: Throwable -> L38
            r1.enqueueNotification(r19, r20, r9, r8.getWeight(), RemovalCause.REPLACED);     // Catch: Throwable -> L36
            ReferenceEntry<K, V> r13 = r12;
            setValue(r13, r19, r21, r7);     // Catch: Throwable -> L44
            evictEntries(r13);     // Catch: Throwable -> L44
            unlock();
            postWriteCleanup();
            return r9;
        L36:
            th = th;
        L54:
            r1.unlock();
            r1.postWriteCleanup();
            throw th;
        L38:
            th = th;
        L43:
            r12 = r12.getNext();     // Catch: Throwable -> L44
            r2 = r20;
            r7 = r7;
        L44:
            th = th;
            goto L54
        L46:
            long r14 = r7;
            int r15 = r2;
            r1.modCount++;     // Catch: Throwable -> L50
            ReferenceEntry<K, V> r16 = r1.newEntry(r19, r15, r5);     // Catch: Throwable -> L50
            r1.setValue(r16, r19, r21, r14);     // Catch: Throwable -> L50
            r4.set(r10, r16);     // Catch: Throwable -> L50
            r1.count++;     // Catch: Throwable -> L50
            r1.evictEntries(r16);     // Catch: Throwable -> L50
            r1.unlock();
            r1.postWriteCleanup();
            return null;
        L50:
            th = th;
        L52:
            th = th;
            goto L54
        }

        boolean reclaimKey(ReferenceEntry<K, V> r14, int r15) {
            lock();
            int r0 = this.count - 1;     // Catch: Throwable -> L18
            AtomicReferenceArray<ReferenceEntry<K, V>> r2 = this.table;     // Catch: Throwable -> L18
            int r3 = (r2.length() - 1) & r15;     // Catch: Throwable -> L18
            ReferenceEntry<K, V> r4 = r2.get(r3);     // Catch: Throwable -> L18
            ReferenceEntry<K, V> r7 = r4;
        L4:
            if (r7 == null) goto L16;
            if (r7 == r14) goto L6;
            int r9 = r15;
        L14:
            th = move-exception;
            Throwable r16 = th;
        L20:
            unlock();
            postWriteCleanup();
            throw r16;
        L13:
            r7 = r7.getNext();     // Catch: Throwable -> L14
            r15 = r9;
            goto L4
        L6:
            this.modCount++;     // Catch: Throwable -> L18
            ReferenceEntry<K, V> r17 = removeValueFromChain(r4, r7, r7.getKey(), r15, r7.getValueReference().get(), r7.getValueReference(), RemovalCause.COLLECTED);     // Catch: Throwable -> L14
            int r5 = this.count - 1;     // Catch: Throwable -> L14
            r2.set(r3, r17);     // Catch: Throwable -> L14
            this.count = r5;     // Catch: Throwable -> L14
            unlock();
            postWriteCleanup();
            return true;
        L16:
            unlock();
            postWriteCleanup();
            return false;
        L18:
            th = move-exception;
            r16 = th;
            goto L20
        }

        boolean reclaimValue(K r14, int r15, ValueReference<K, V> r16) {
            int r5 = r15;
            lock();
            int r0 = this.count - 1;     // Catch: Throwable -> L30
            AtomicReferenceArray<ReferenceEntry<K, V>> r2 = this.table;     // Catch: Throwable -> L30
            int r11 = r5 & (r2.length() - 1);     // Catch: Throwable -> L30
            ReferenceEntry<K, V> r3 = r2.get(r11);     // Catch: Throwable -> L30
            ReferenceEntry<K, V> r4 = r3;
        L5:
            if (r4 == null) goto L26;
            K r6 = r4.getKey();     // Catch: Throwable -> L30
            if (r4.getHash() != r5) goto L25;
            if (r6 == null) goto L25;
            if (this.map.keyEquivalence.equivalent(r14, r6) == false) goto L25;
            if (r4.getValueReference() != r16) goto L18;
            this.modCount++;     // Catch: Throwable -> L30
            ReferenceEntry<K, V> r7 = removeValueFromChain(r3, r4, r6, r5, r16.get(), r16, RemovalCause.COLLECTED);     // Catch: Throwable -> L30
            int r8 = this.count - 1;     // Catch: Throwable -> L30
            r2.set(r11, r7);     // Catch: Throwable -> L30
            this.count = r8;     // Catch: Throwable -> L30
            unlock();
            if (isHeldByCurrentThread() == true) goto L17;
            postWriteCleanup();
        L17:
            return true;
        L18:
            unlock();
            if (isHeldByCurrentThread() == true) goto L21;
            postWriteCleanup();
        L21:
            return false;
        L25:
            r4 = r4.getNext();     // Catch: Throwable -> L30
            r5 = r15;
            goto L5
        L26:
            unlock();
            if (isHeldByCurrentThread() == true) goto L29;
            postWriteCleanup();
        L29:
            return false;
        L30:
            th = move-exception;
            unlock();
            if (isHeldByCurrentThread() == true) goto L34;
            postWriteCleanup();
        L34:
            throw th;
        }

        void recordLockedRead(ReferenceEntry<K, V> r2, long r3) {
            if (this.map.recordsAccess() == false) goto L5;
            r2.setAccessTime(r3);
        L5:
            this.accessQueue.add(r2);
        }

        void recordRead(ReferenceEntry<K, V> r2, long r3) {
            if (this.map.recordsAccess() == false) goto L5;
            r2.setAccessTime(r3);
        L5:
            this.recencyQueue.add(r2);
        }

        void recordWrite(ReferenceEntry<K, V> r5, int r6, long r7) {
            drainRecencyQueue();
            this.totalWeight += (long) r6;
            if (this.map.recordsAccess() == false) goto L6;
            r5.setAccessTime(r7);
        L6:
            if (this.map.recordsWrite() == false) goto L8;
            r5.setWriteTime(r7);
        L8:
            this.accessQueue.add(r5);
            this.writeQueue.add(r5);
        }

        @CheckForNull
        V refresh(K r5, int r6, CacheLoader<? super K, V> r7, boolean r8) {
            LoadingValueReference<K, V> r0 = insertLoadingValueReference(r5, r6, r8);
            if (r0 != null) goto L5;
            return null;
        L5:
            ListenableFuture<V> r2 = loadAsync(r5, r6, r0, r7);
            if (r2.isDone() == true) goto L11;
        L10:
            return null;
        L11:
            return (V) Uninterruptibles.getUninterruptibly(r2);
        }

        @CheckForNull
        V remove(Object r17, int r18) {
            int r5 = r18;
            lock();
            preWriteCleanup(this.map.ticker.read());     // Catch: Throwable -> L31
            int r0 = this.count - 1;     // Catch: Throwable -> L31
            AtomicReferenceArray<ReferenceEntry<K, V>> r2 = this.table;     // Catch: Throwable -> L31
            int r12 = r5 & (r2.length() - 1);     // Catch: Throwable -> L31
            ReferenceEntry<K, V> r3 = r2.get(r12);     // Catch: Throwable -> L31
            ReferenceEntry<K, V> r4 = r3;
        L5:
            if (r4 == null) goto L29;
            K r6 = r4.getKey();     // Catch: Throwable -> L31
            if (r4.getHash() != r5) goto L26;
            if (r6 == null) goto L26;
        L27:
            th = th;
        L33:
            unlock();
            postWriteCleanup();
            throw th;
        L12:
            if (this.map.keyEquivalence.equivalent(r17, r6) == false) goto L26;
            ValueReference<K, V> r7 = r4.getValueReference();     // Catch: Throwable -> L27
            V r8 = r7.get();     // Catch: Throwable -> L27
            if (r8 == null) goto L17;
            RemovalCause r9 = RemovalCause.EXPLICIT;     // Catch: Throwable -> L27
        L19:
            this.modCount++;     // Catch: Throwable -> L27
            ReferenceEntry<K, V> r14 = removeValueFromChain(r3, r4, r6, r5, r8, r7, r9);     // Catch: Throwable -> L27
            int r10 = this.count - 1;     // Catch: Throwable -> L27
            r2.set(r12, r14);     // Catch: Throwable -> L27
            this.count = r10;     // Catch: Throwable -> L27
            unlock();
            postWriteCleanup();
            return r8;
        L17:
            if (r7.isActive() == false) goto L22;
            r9 = RemovalCause.COLLECTED;     // Catch: Throwable -> L27
            goto L19
        L22:
            unlock();
            postWriteCleanup();
            return null;
        L26:
            r4 = r4.getNext();     // Catch: Throwable -> L27
            r5 = r18;
            goto L5
        L29:
            unlock();
            postWriteCleanup();
            return null;
        L31:
            th = th;
            goto L33
        }

        boolean remove(Object r19, int r20, Object r21) {
            int r5 = r20;
            lock();
            preWriteCleanup(this.map.ticker.read());     // Catch: Throwable -> L39
            boolean r11 = true;
            int r0 = this.count - 1;     // Catch: Throwable -> L39
            AtomicReferenceArray<ReferenceEntry<K, V>> r2 = this.table;     // Catch: Throwable -> L39
            int r13 = r5 & (r2.length() - 1);     // Catch: Throwable -> L39
            ReferenceEntry<K, V> r3 = r2.get(r13);     // Catch: Throwable -> L39
            ReferenceEntry<K, V> r4 = r3;
        L4:
            if (r4 == null) goto L37;
            K r6 = r4.getKey();     // Catch: Throwable -> L39
            if (r4.getHash() != r5) goto L46;
            if (r6 == null) goto L46;
        L30:
            th = th;
        L42:
            unlock();
            postWriteCleanup();
            throw th;
        L11:
            if (this.map.keyEquivalence.equivalent(r19, r6) == false) goto L46;
            ValueReference<K, V> r7 = r4.getValueReference();     // Catch: Throwable -> L30
            V r8 = r7.get();     // Catch: Throwable -> L30
            boolean r16 = r11;
            if (this.map.valueEquivalence.equivalent(r21, r8) == false) goto L17;
            RemovalCause r9 = RemovalCause.EXPLICIT;     // Catch: Throwable -> L35
        L21:
            this.modCount++;     // Catch: Throwable -> L35
            ReferenceEntry<K, V> r14 = removeValueFromChain(r3, r4, r6, r5, r8, r7, r9);     // Catch: Throwable -> L35
            int r10 = this.count - 1;     // Catch: Throwable -> L35
            r2.set(r13, r14);     // Catch: Throwable -> L35
            this.count = r10;     // Catch: Throwable -> L35
            if (r9 == RemovalCause.EXPLICIT) goto L25;
            r16 = false;
        L25:
            unlock();
            postWriteCleanup();
            return r16;
        L35:
            th = th;
            goto L42
        L17:
            if (r8 == null) goto L19;
        L27:
            unlock();
            postWriteCleanup();
            return false;
        L19:
            if (r7.isActive() == false) goto L27;
            r9 = RemovalCause.COLLECTED;     // Catch: Throwable -> L35
        L46:
            r4 = r4.getNext();     // Catch: Throwable -> L35
            r5 = r20;
            r11 = r11;
            goto L4
        L37:
            unlock();
            postWriteCleanup();
            return false;
        L39:
            th = th;
            goto L42
        }

        void removeCollectedEntry(ReferenceEntry<K, V> r7) {
            enqueueNotification(r7.getKey(), r7.getHash(), r7.getValueReference().get(), r7.getValueReference().getWeight(), RemovalCause.COLLECTED);
            this.writeQueue.remove(r7);
            this.accessQueue.remove(r7);
        }

        boolean removeEntry(ReferenceEntry<K, V> r13, int r14, RemovalCause r15) {
            int r9 = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> r10 = this.table;
            int r11 = r14 & (r10.length() - 1);
            ReferenceEntry<K, V> r1 = r10.get(r11);
            ReferenceEntry<K, V> r2 = r1;
        L3:
            if (r2 == null) goto L8;
            if (r2 == r13) goto L5;
            r2 = r2.getNext();
            goto L3
        L5:
            this.modCount++;
            ReferenceEntry<K, V> r3 = removeValueFromChain(r1, r2, r2.getKey(), r14, r2.getValueReference().get(), r2.getValueReference(), r15);
            int r4 = this.count - 1;
            r10.set(r11, r3);
            this.count = r4;
            return true;
        L8:
            return false;
        }

        @CheckForNull
        ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> r5, ReferenceEntry<K, V> r6) {
            int r0 = this.count;
            ReferenceEntry<K, V> r1 = r6.getNext();
            ReferenceEntry<K, V> r2 = r5;
        L3:
            if (r2 == r6) goto L9;
            ReferenceEntry<K, V> r3 = copyEntry(r2, r1);
            if (r3 == null) goto L7;
            r1 = r3;
        L8:
            r2 = r2.getNext();
            goto L3
        L7:
            removeCollectedEntry(r2);
            r0 = r0 - 1;
            goto L8
        L9:
            this.count = r0;
            return r1;
        }

        boolean removeLoadingValue(K r9, int r10, LoadingValueReference<K, V> r11) {
            lock();
            AtomicReferenceArray<ReferenceEntry<K, V>> r0 = this.table;     // Catch: Throwable -> L25
            int r1 = (r0.length() - 1) & r10;     // Catch: Throwable -> L25
            ReferenceEntry<K, V> r3 = r0.get(r1);     // Catch: Throwable -> L25
            ReferenceEntry<K, V> r4 = r3;
        L5:
            if (r4 == null) goto L23;
            K r6 = r4.getKey();     // Catch: Throwable -> L25
            if (r4.getHash() != r10) goto L22;
            if (r6 == null) goto L22;
            if (this.map.keyEquivalence.equivalent(r9, r6) == false) goto L22;
            if (r4.getValueReference() == r11) goto L14;
            unlock();
            postWriteCleanup();
            return false;
        L14:
            if (r11.isActive() == false) goto L16;
            r4.setValueReference(r11.getOldValue());     // Catch: Throwable -> L25
        L17:
            unlock();
            postWriteCleanup();
            return true;
        L16:
            r0.set(r1, removeEntryFromChain(r3, r4));     // Catch: Throwable -> L25
        L22:
            r4 = r4.getNext();     // Catch: Throwable -> L25
            goto L5
        L23:
            unlock();
            postWriteCleanup();
            return false;
        L25:
            th = move-exception;
            unlock();
            postWriteCleanup();
            throw th;
        }

        @CheckForNull
        ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> r7, ReferenceEntry<K, V> r8, @CheckForNull K r9, int r10, V r11, ValueReference<K, V> r12, RemovalCause r13) {
            enqueueNotification(r9, r10, r11, r12.getWeight(), r13);
            this.writeQueue.remove(r8);
            this.accessQueue.remove(r8);
            if (r12.isLoading() == false) goto L7;
            r12.notifyNewValue(null);
            return r7;
        L7:
            return removeEntryFromChain(r7, r8);
        }

        @CheckForNull
        V replace(K r20, int r21, V r22) {
            int r3 = r21;
            lock();
            long r9 = this.map.ticker.read();     // Catch: Throwable -> L30
            preWriteCleanup(r9);     // Catch: Throwable -> L30
            AtomicReferenceArray<ReferenceEntry<K, V>> r0 = this.table;     // Catch: Throwable -> L30
            int r11 = r3 & (r0.length() - 1);     // Catch: Throwable -> L30
            ReferenceEntry<K, V> r2 = r0.get(r11);     // Catch: Throwable -> L30
            ReferenceEntry<K, V> r4 = r2;
        L5:
            if (r4 == null) goto L28;
            K r5 = r4.getKey();     // Catch: Throwable -> L30
            if (r4.getHash() != r3) goto L27;
            if (r5 == null) goto L27;
            if (this.map.keyEquivalence.equivalent(r20, r5) == false) goto L27;
            ValueReference<K, V> r7 = r4.getValueReference();     // Catch: Throwable -> L30
            V r6 = r7.get();     // Catch: Throwable -> L30
            if (r6 == null) goto L14;
            ReferenceEntry<K, V> r15 = r4;
            this.modCount++;     // Catch: Throwable -> L30
            enqueueNotification(r20, r21, r6, r7.getWeight(), RemovalCause.REPLACED);     // Catch: Throwable -> L30
            setValue(r15, r20, r22, r9);     // Catch: Throwable -> L30
            evictEntries(r15);     // Catch: Throwable -> L30
            unlock();
            postWriteCleanup();
            return r6;
        L14:
            if (r7.isActive() == false) goto L18;
            int r14 = this.count - 1;     // Catch: Throwable -> L30
            this.modCount++;     // Catch: Throwable -> L30
            int r8 = r3;
            ReferenceEntry<K, V> r10 = removeValueFromChain(r2, r4, r5, r8, r6, r7, RemovalCause.COLLECTED);     // Catch: Throwable -> L30
            int r12 = this.count - 1;     // Catch: Throwable -> L30
            r0.set(r11, r10);     // Catch: Throwable -> L30
            this.count = r12;     // Catch: Throwable -> L30
        L18:
            unlock();
            postWriteCleanup();
            return null;
        L27:
            r4 = r4.getNext();     // Catch: Throwable -> L30
            r3 = r21;
            r9 = r9;
            r2 = r2;
            goto L5
        L28:
            unlock();
            postWriteCleanup();
            return null;
        L30:
            th = move-exception;
            unlock();
            postWriteCleanup();
            throw th;
        }

        boolean replace(K r21, int r22, V r23, V r24) {
            int r3 = r22;
            lock();
            long r9 = this.map.ticker.read();     // Catch: Throwable -> L40
            preWriteCleanup(r9);     // Catch: Throwable -> L40
            AtomicReferenceArray<ReferenceEntry<K, V>> r0 = this.table;     // Catch: Throwable -> L40
            int r12 = r3 & (r0.length() - 1);     // Catch: Throwable -> L40
            ReferenceEntry<K, V> r2 = r0.get(r12);     // Catch: Throwable -> L40
            ReferenceEntry<K, V> r4 = r2;
        L5:
            if (r4 == null) goto L38;
            K r5 = r4.getKey();     // Catch: Throwable -> L40
            if (r4.getHash() != r3) goto L45;
            if (r5 == null) goto L45;
            if (this.map.keyEquivalence.equivalent(r21, r5) == false) goto L45;
            ValueReference<K, V> r7 = r4.getValueReference();     // Catch: Throwable -> L40
            V r6 = r7.get();     // Catch: Throwable -> L40
            if (r6 == null) goto L14;
            ReferenceEntry<K, V> r16 = r4;
            if (this.map.valueEquivalence.equivalent(r23, r6) == false) goto L29;
            this.modCount++;     // Catch: Throwable -> L36
            enqueueNotification(r21, r22, r6, r7.getWeight(), RemovalCause.REPLACED);     // Catch: Throwable -> L36
            setValue(r16, r21, r24, r9);     // Catch: Throwable -> L36
            evictEntries(r16);     // Catch: Throwable -> L36
            unlock();
            postWriteCleanup();
            return true;
        L29:
            recordLockedRead(r16, r9);     // Catch: Throwable -> L36
            unlock();
            postWriteCleanup();
            return false;
        L14:
            if (r7.isActive() == false) goto L18;
            int r15 = this.count - 1;     // Catch: Throwable -> L40
            this.modCount++;     // Catch: Throwable -> L40
            int r8 = r3;
            ReferenceEntry<K, V> r10 = removeValueFromChain(r2, r4, r5, r8, r6, r7, RemovalCause.COLLECTED);     // Catch: Throwable -> L40
            int r11 = this.count - 1;     // Catch: Throwable -> L40
            r0.set(r12, r10);     // Catch: Throwable -> L40
            this.count = r11;     // Catch: Throwable -> L40
        L18:
            unlock();
            postWriteCleanup();
            return false;
        L45:
        L36:
            th = th;
        L42:
            unlock();
            postWriteCleanup();
            throw th;
        L35:
            r4 = r4.getNext();     // Catch: Throwable -> L36
            r3 = r22;
            r9 = r9;
            r2 = r2;
            goto L5
        L38:
            unlock();
            postWriteCleanup();
            return false;
        L40:
            th = th;
            goto L42
        }

        void runLockedCleanup(long r3) {
            if (tryLock() == true) goto L10;
            return;
        L10:
            drainReferenceQueues();     // Catch: Throwable -> L6
            expireEntries(r3);     // Catch: Throwable -> L6
            this.readCount.set(0);     // Catch: Throwable -> L6
            unlock();
            return;
        L6:
            th = move-exception;
            unlock();
            throw th;
        }

        void runUnlockedCleanup() {
            if (isHeldByCurrentThread() == true) goto L6;
            this.map.processPendingNotifications();
            return;
        }

        V scheduleRefresh(ReferenceEntry<K, V> r5, K r6, int r7, V r8, long r9, CacheLoader<? super K, V> r11) {
            if (this.map.refreshes() == true) goto L5;
        L11:
            return r8;
        L5:
            if ((r9 - r5.getWriteTime()) <= this.map.refreshNanos) goto L11;
            if (r5.getValueReference().isLoading() == true) goto L11;
            V r0 = refresh(r6, r7, r11, true);
            if (r0 == null) goto L11;
            return r0;
        }

        void setValue(ReferenceEntry<K, V> r5, K r6, V r7, long r8) {
            ValueReference<K, V> r0 = r5.getValueReference();
            int r1 = this.map.weigher.weigh(r6, r7);
            if (r1 < 0) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkState(r2, "Weights must be non-negative");
            r5.setValueReference(this.map.valueStrength.referenceValue(this, r5, r7, r1));
            recordWrite(r5, r1, r8);
            r0.notifyNewValue(r7);
            return;
        L5:
            r2 = false;
            goto L6
        }

        boolean storeLoadedValue(K r18, int r19, LoadingValueReference<K, V> r20, V r21) {
            Segment<K, V> r1 = this;
            int r3 = r19;
            r1.lock();
            long r7 = r1.map.ticker.read();     // Catch: Throwable -> L53
            r1.preWriteCleanup(r7);     // Catch: Throwable -> L53
            int r0 = r1.count + 1;     // Catch: Throwable -> L53
            if (r0 <= r1.threshold) goto L6;
            r1.expand();     // Catch: Throwable -> L53
            r0 = r1.count + 1;     // Catch: Throwable -> L53
        L6:
            AtomicReferenceArray<ReferenceEntry<K, V>> r4 = r1.table;     // Catch: Throwable -> L53
            int r11 = r3 & (r4.length() - 1);     // Catch: Throwable -> L53
            ReferenceEntry<K, V> r5 = r4.get(r11);     // Catch: Throwable -> L53
            ReferenceEntry<K, V> r13 = r5;
        L7:
            if (r13 == null) goto L48;
            K r6 = r13.getKey();     // Catch: Throwable -> L53
            if (r13.getHash() != r3) goto L58;
            if (r6 == null) goto L58;
            if (r1.map.keyEquivalence.equivalent(r18, r6) == false) goto L58;
            ValueReference<K, V> r8 = r13.getValueReference();     // Catch: Throwable -> L53
            V r9 = r8.get();     // Catch: Throwable -> L53
            if (r20 == r8) goto L65;
            if (r9 != null) goto L19;
            if (r8 != LocalCache.UNSET) goto L65;
        L19:
            r1.enqueueNotification(r18, r3, r21, 0, RemovalCause.REPLACED);     // Catch: Throwable -> L22
            r1.unlock();
            r1.postWriteCleanup();
            return false;
        L65:
            r1.modCount++;     // Catch: Throwable -> L42
            if (r20.isActive() == false) goto L34;
            if (r9 != null) goto L63;
            RemovalCause r2 = RemovalCause.COLLECTED;     // Catch: Throwable -> L22
        L30:
            r1.enqueueNotification(r18, r19, r9, r20.getWeight(), r2);     // Catch: Throwable -> L32
            r0 = r0 - 1;
        L32:
            th = th;
            r1 = this;
        L55:
            r1.unlock();
            r1.postWriteCleanup();
            throw th;
        L63:
            r2 = RemovalCause.REPLACED;     // Catch: Throwable -> L32
        L34:
            r1 = this;
            ReferenceEntry<K, V> r10 = r13;
            r1.setValue(r10, r18, r21, r7);     // Catch: Throwable -> L40
            r1.count = r0;     // Catch: Throwable -> L51
            r1.evictEntries(r10);     // Catch: Throwable -> L51
            r1.unlock();
            r1.postWriteCleanup();
            return true;
        L40:
            th = th;
        L42:
            th = th;
        L22:
            th = th;
        L58:
            long r12 = r7;
            r13 = r13.getNext();     // Catch: Throwable -> L51
            r3 = r3;
            r7 = r12;
        L51:
            th = th;
            goto L55
        L48:
            long r14 = r7;
            r1.modCount++;     // Catch: Throwable -> L51
            ReferenceEntry<K, V> r15 = r1.newEntry(r18, r3, r5);     // Catch: Throwable -> L51
            r1.setValue(r15, r18, r21, r14);     // Catch: Throwable -> L51
            r4.set(r11, r15);     // Catch: Throwable -> L51
            r1.count = r0;     // Catch: Throwable -> L51
            r1.evictEntries(r15);     // Catch: Throwable -> L51
            r1.unlock();
            r1.postWriteCleanup();
            return true;
        L53:
            th = th;
            goto L55
        }

        void tryDrainReferenceQueues() {
            if (tryLock() == true) goto L10;
            return;
        L10:
            drainReferenceQueues();     // Catch: Throwable -> L6
            unlock();
            return;
        L6:
            th = move-exception;
            unlock();
            throw th;
        }

        void tryExpireEntries(long r2) {
            if (tryLock() == true) goto L10;
            return;
        L10:
            expireEntries(r2);     // Catch: Throwable -> L6
            unlock();
            return;
        L6:
            th = move-exception;
            unlock();
            throw th;
        }

        V waitForLoadingValue(ReferenceEntry<K, V> r6, K r7, ValueReference<K, V> r8) throws ExecutionException {
            if (r8.isLoading() == false) goto L16;
            Preconditions.checkState(!Thread.holdsLock(r6), "Recursive load of: %s", r7);
            V r0 = r8.waitForValue();     // Catch: Throwable -> L12
            if (r0 == null) goto L11;
            recordRead(r6, this.map.ticker.read());     // Catch: Throwable -> L12
            this.statsCounter.recordMisses(1);
            return r0;
        L11:
            throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + r7 + ".");     // Catch: Throwable -> L12
        L12:
            th = move-exception;
            this.statsCounter.recordMisses(1);
            throw th;
        L16:
            throw new AssertionError();
        }
    }

    static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(ReferenceQueue<V> r1, V r2, ReferenceEntry<K, V> r3) {
            super(r2, r1);
            this.entry = r3;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> r2, V r3, ReferenceEntry<K, V> r4) {
            return new SoftValueReference(r2, r3, r4);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V r1) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    static abstract enum Strength extends Enum<Strength> {
        private static final /* synthetic */ Strength[] $VALUES = null;
        public static final Strength SOFT = null;
        public static final Strength STRONG = null;
        public static final Strength WEAK = null;

        private static /* synthetic */ Strength[] $values() {
            return new Strength[]{STRONG, SOFT, WEAK};
        }

        static {
            final String r1 = "STRONG";
            final int r2 = 0;
            STRONG = new C05771(r1, r2);
            final String r3 = "SOFT";
            final int r4 = 1;
            SOFT = new C05782(r3, r4);
            final String r5 = "WEAK";
            final int r6 = 2;
            WEAK = new C05793(r5, r6);
            $VALUES = $values();
        }

        private Strength(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ Strength(String r1, int r2, C05641 r3) {
            this(r1, r2);
        }

        public static Strength valueOf(String r1) {
            return (Strength) Enum.valueOf(Strength.class, r1);
        }

        public static Strength[] values() {
            return (Strength[]) $VALUES.clone();
        }

        abstract Equivalence<Object> defaultEquivalence();

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> r1, ReferenceEntry<K, V> r2, V r3, int r4);
    }

    static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;

        StrongAccessEntry(K r3, int r4, @CheckForNull ReferenceEntry<K, V> r5) {
            super(r3, r4, r5);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long r1) {
            this.accessTime = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> r1) {
            this.nextAccess = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> r1) {
            this.previousAccess = r1;
        }
    }

    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousAccess;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        StrongAccessWriteEntry(K r4, int r5, @CheckForNull ReferenceEntry<K, V> r6) {
            super(r4, r5, r6);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long r1) {
            this.accessTime = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> r1) {
            this.nextAccess = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> r1) {
            this.nextWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> r1) {
            this.previousAccess = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> r1) {
            this.previousWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long r1) {
            this.writeTime = r1;
        }
    }

    static class StrongEntry<K, V> extends AbstractReferenceEntry<K, V> {
        final int hash;
        final K key;

        @CheckForNull
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;

        StrongEntry(K r2, int r3, @CheckForNull ReferenceEntry<K, V> r4) {
            this.valueReference = LocalCache.unset();
            this.key = r2;
            this.hash = r3;
            this.next = r4;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> r1) {
            this.valueReference = r1;
        }
    }

    static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        StrongValueReference(V r1) {
            this.referent = r1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> r1, V r2, ReferenceEntry<K, V> r3) {
            return this;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V get() {
            return this.referent;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V r1) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> {
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        StrongWriteEntry(K r3, int r4, @CheckForNull ReferenceEntry<K, V> r5) {
            super(r3, r4, r5);
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> r1) {
            this.nextWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> r1) {
            this.previousWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long r1) {
            this.writeTime = r1;
        }
    }

    final class ValueIterator extends LocalCache<K, V>.HashIterator<V> {
        final /* synthetic */ LocalCache this$0;

        ValueIterator(LocalCache r1) {
            this.this$0 = r1;
            super(r1);
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceQueue<V> r1, @CheckForNull V r2, ReferenceEntry<K, V> r3);

        @CheckForNull
        V get();

        @CheckForNull
        ReferenceEntry<K, V> getEntry();

        int getWeight();

        boolean isActive();

        boolean isLoading();

        void notifyNewValue(@CheckForNull V r1);

        V waitForValue() throws ExecutionException;
    }

    final class Values extends AbstractCollection<V> {
        final /* synthetic */ LocalCache this$0;

        Values(LocalCache r1) {
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

    static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;

        WeakAccessEntry(ReferenceQueue<K> r3, K r4, int r5, @CheckForNull ReferenceEntry<K, V> r6) {
            super(r3, r4, r5, r6);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long r1) {
            this.accessTime = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> r1) {
            this.nextAccess = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> r1) {
            this.previousAccess = r1;
        }
    }

    static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousAccess;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        WeakAccessWriteEntry(ReferenceQueue<K> r4, K r5, int r6, @CheckForNull ReferenceEntry<K, V> r7) {
            super(r4, r5, r6, r7);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long r1) {
            this.accessTime = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> r1) {
            this.nextAccess = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> r1) {
            this.nextWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> r1) {
            this.previousAccess = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> r1) {
            this.previousWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long r1) {
            this.writeTime = r1;
        }
    }

    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;

        @CheckForNull
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;

        WeakEntry(ReferenceQueue<K> r2, K r3, int r4, @CheckForNull ReferenceEntry<K, V> r5) {
            super(r3, r2);
            this.valueReference = LocalCache.unset();
            this.hash = r4;
            this.next = r5;
        }

        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        public void setAccessTime(long r2) {
            throw new UnsupportedOperationException();
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> r2) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> r1) {
            this.valueReference = r1;
        }

        public void setWriteTime(long r2) {
            throw new UnsupportedOperationException();
        }
    }

    static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(ReferenceQueue<V> r1, V r2, ReferenceEntry<K, V> r3) {
            super(r2, r1);
            this.entry = r3;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> r2, V r3, ReferenceEntry<K, V> r4) {
            return new WeakValueReference(r2, r3, r4);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V r1) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> {
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        WeakWriteEntry(ReferenceQueue<K> r3, K r4, int r5, @CheckForNull ReferenceEntry<K, V> r6) {
            super(r3, r4, r5, r6);
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> r1) {
            this.nextWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> r1) {
            this.previousWrite = r1;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long r1) {
            this.writeTime = r1;
        }
    }

    static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V> {
        final int weight;

        WeightedSoftValueReference(ReferenceQueue<V> r1, V r2, ReferenceEntry<K, V> r3, int r4) {
            super(r1, r2, r3);
            this.weight = r4;
        }

        @Override // com.google.common.cache.LocalCache.SoftValueReference, com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> r3, V r4, ReferenceEntry<K, V> r5) {
            return new WeightedSoftValueReference(r3, r4, r5, this.weight);
        }

        @Override // com.google.common.cache.LocalCache.SoftValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V> {
        final int weight;

        WeightedStrongValueReference(V r1, int r2) {
            super(r1);
            this.weight = r2;
        }

        @Override // com.google.common.cache.LocalCache.StrongValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V> {
        final int weight;

        WeightedWeakValueReference(ReferenceQueue<V> r1, V r2, ReferenceEntry<K, V> r3, int r4) {
            super(r1, r2, r3);
            this.weight = r4;
        }

        @Override // com.google.common.cache.LocalCache.WeakValueReference, com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> r3, V r4, ReferenceEntry<K, V> r5) {
            return new WeightedWeakValueReference(r3, r4, r5, this.weight);
        }

        @Override // com.google.common.cache.LocalCache.WeakValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;

        WriteQueue() {
            this.head = new C05801(this);
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            ReferenceEntry<K, V> r0 = this.head.getNextInWriteQueue();
        L4:
            if (r0 == this.head) goto L6;
            ReferenceEntry<K, V> r1 = r0.getNextInWriteQueue();
            LocalCache.nullifyWriteOrder(r0);
            r0 = r1;
            goto L4
        L6:
            this.head.setNextInWriteQueue(this.head);
            this.head.setPreviousInWriteQueue(this.head);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object r4) {
            if (((ReferenceEntry) r4).getNextInWriteQueue() == NullEntry.INSTANCE) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            if (this.head.getNextInWriteQueue() != this.head) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<ReferenceEntry<K, V>> iterator() {
            final ReferenceEntry<K, V> r1 = peek();
            return new C05812(this, r1);
        }

        public boolean offer(ReferenceEntry<K, V> r3) {
            LocalCache.connectWriteOrder(r3.getPreviousInWriteQueue(), r3.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), r3);
            LocalCache.connectWriteOrder(r3, this.head);
            return true;
        }

        @Override // java.util.Queue
        public /* bridge */ /* synthetic */ boolean offer(Object r1) {
            return offer((ReferenceEntry) r1);
        }

        @Override // java.util.Queue
        @CheckForNull
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> r0 = this.head.getNextInWriteQueue();
            if (r0 != this.head) goto L6;
            return null;
        L6:
            return r0;
        }

        @Override // java.util.Queue
        @CheckForNull
        public /* bridge */ /* synthetic */ Object peek() {
            return peek();
        }

        @Override // java.util.Queue
        @CheckForNull
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> r0 = this.head.getNextInWriteQueue();
            if (r0 != this.head) goto L6;
            return null;
        L6:
            remove(r0);
            return r0;
        }

        @Override // java.util.Queue
        @CheckForNull
        public /* bridge */ /* synthetic */ Object poll() {
            return poll();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object r5) {
            ReferenceEntry r0 = (ReferenceEntry) r5;
            ReferenceEntry<K, V> r1 = r0.getPreviousInWriteQueue();
            ReferenceEntry<K, V> r2 = r0.getNextInWriteQueue();
            LocalCache.connectWriteOrder(r1, r2);
            LocalCache.nullifyWriteOrder(r0);
            if (r2 == NullEntry.INSTANCE) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int r0 = 0;
            ReferenceEntry<K, V> r1 = this.head.getNextInWriteQueue();
        L4:
            if (r1 == this.head) goto L6;
            r0 = r0 + 1;
            r1 = r1.getNextInWriteQueue();
            goto L4
        L6:
            return r0;
        }
    }

    final class WriteThroughEntry implements Map.Entry<K, V> {
        final K key;
        final /* synthetic */ LocalCache this$0;
        V value;

        WriteThroughEntry(LocalCache r1, K r2, V r3) {
            this.this$0 = r1;
            this.key = r2;
            this.value = r3;
        }

        @Override // java.util.Map.Entry
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

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V r3) {
            V r0 = (V) this.this$0.put(this.key, r3);
            this.value = r3;
            return r0;
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    static {
        logger = Logger.getLogger(LocalCache.class.getName());
        UNSET = new C05641();
        DISCARDING_QUEUE = new C05652();
    }

    LocalCache(CacheBuilder<? super K, ? super V> r18, @CheckForNull CacheLoader<? super K, V> r19) {
        this.concurrencyLevel = Math.min(r18.getConcurrencyLevel(), 65536);
        this.keyStrength = r18.getKeyStrength();
        this.valueStrength = r18.getValueStrength();
        this.keyEquivalence = r18.getKeyEquivalence();
        this.valueEquivalence = r18.getValueEquivalence();
        this.maxWeight = r18.getMaximumWeight();
        this.weigher = (Weigher<K, V>) r18.getWeigher();
        this.expireAfterAccessNanos = r18.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = r18.getExpireAfterWriteNanos();
        this.refreshNanos = r18.getRefreshNanos();
        this.removalListener = (RemovalListener<K, V>) r18.getRemovalListener();
        if (this.removalListener != CacheBuilder.NullListener.INSTANCE) goto L5;
        Queue<RemovalNotification<K, V>> r1 = discardingQueue();
    L6:
        this.removalNotificationQueue = r1;
        this.ticker = r18.getTicker(recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
        this.globalStatsCounter = r18.getStatsCounterSupplier().get();
        this.defaultLoader = r19;
        int r3 = Math.min(r18.getInitialCapacity(), 1073741824);
        if (evictsBySize() == true) goto L9;
    L11:
        int r4 = 0;
        int r5 = 1;
    L13:
        if (r5 >= this.concurrencyLevel) goto L19;
        if (evictsBySize() == false) goto L18;
        if ((((long) r5) * 20) > this.maxWeight) goto L19;
    L18:
        r4 = r4 + 1;
        r5 = r5 << 1;
    L19:
        this.segmentShift = 32 - r4;
        this.segmentMask = r5 - 1;
        this.segments = newSegmentArray(r5);
        int r6 = r3 / r5;
        if ((r6 * r5) >= r3) goto L22;
        r6 = r6 + 1;
    L22:
        int r7 = 1;
    L23:
        if (r7 >= r6) goto L26;
        r7 = r7 << 1;
        goto L23
    L26:
        if (evictsBySize() == false) goto L35;
        long r10 = 1;
        long r8 = (this.maxWeight / ((long) r5)) + 1;
        long r12 = this.maxWeight % ((long) r5);
        int r14 = 0;
    L29:
        if (r14 >= this.segments.length) goto L39;
        long r15 = r10;
        if (r14 != r12) goto L33;
        r8 = r8 - r15;
    L33:
        this.segments[r14] = createSegment(r7, r8, r18.getStatsCounterSupplier().get());
        r14 = r14 + 1;
        r10 = r15;
        goto L29
    L39:
        return;
    L35:
        int r9 = 0;
    L37:
        if (r9 >= this.segments.length) goto L49;
        this.segments[r9] = createSegment(r7, -1, r18.getStatsCounterSupplier().get());
        r9 = r9 + 1;
        goto L37
    L49:
        return;
    L9:
        if (customWeigher() == true) goto L11;
        r3 = (int) Math.min(r3, this.maxWeight);
        goto L11
    L5:
        r1 = new ConcurrentLinkedQueue();
        goto L6
    }

    static <K, V> void connectAccessOrder(ReferenceEntry<K, V> r0, ReferenceEntry<K, V> r1) {
        r0.setNextInAccessQueue(r1);
        r1.setPreviousInAccessQueue(r0);
    }

    static <K, V> void connectWriteOrder(ReferenceEntry<K, V> r0, ReferenceEntry<K, V> r1) {
        r0.setNextInWriteQueue(r1);
        r1.setPreviousInWriteQueue(r0);
    }

    static <E> Queue<E> discardingQueue() {
        return (Queue<E>) DISCARDING_QUEUE;
    }

    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> r1) {
        ReferenceEntry<K, V> r0 = nullEntry();
        r1.setNextInAccessQueue(r0);
        r1.setPreviousInAccessQueue(r0);
    }

    static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> r1) {
        ReferenceEntry<K, V> r0 = nullEntry();
        r1.setNextInWriteQueue(r0);
        r1.setPreviousInWriteQueue(r0);
    }

    static int rehash(int r2) {
        int r3 = r2 + ((r2 << 15) ^ (-12931));
        int r4 = r3 ^ (r3 >>> 10);
        int r5 = r4 + (r4 << 3);
        int r6 = r5 ^ (r5 >>> 6);
        int r7 = r6 + ((r6 << 2) + (r6 << 14));
        return (r7 >>> 16) ^ r7;
    }

    static <K, V> ValueReference<K, V> unset() {
        return (ValueReference<K, V>) UNSET;
    }

    public void cleanUp() {
        Segment<K, V>[] r0 = this.segments;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].cleanUp();
        r2 = r2 + 1;
        goto L3
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Segment<K, V>[] r0 = this.segments;
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
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [int] */
    /* JADX WARN: Type inference failed for: r12v3 */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@CheckForNull Object r22) {
        boolean r2 = false;
        if (r22 != null) goto L5;
        return false;
    L5:
        long r3 = this.ticker.read();
        Segment<K, V>[] r5 = this.segments;
        long r6 = -1;
        int r8 = 0;
    L7:
        if (r8 >= 3) goto L30;
        long r9 = 0;
        int r11 = r5.length;
        ?? r12 = r2;
    L9:
        if (r12 >= r11) goto L25;
        Segment<K, V> r13 = r5[r12];
        int r14 = r13.count;
        AtomicReferenceArray<ReferenceEntry<K, V>> r15 = r13.table;
        boolean r17 = r2;
        int r4 = 0;
    L11:
        Segment<K, V>[] r16 = r5;
        if (r4 >= r15.length()) goto L24;
        ReferenceEntry<K, V> r7 = r15.get(r4);
    L14:
        if (r7 == null) goto L23;
        int r18 = r4;
        V r10 = r13.getLiveValue(r7, r3);
        if (r10 == null) goto L22;
        if (this.valueEquivalence.equivalent(r22, r10) == false) goto L22;
        return true;
    L22:
        r7 = r7.getNext();
        r4 = r18;
        r3 = r3;
        goto L14
    L23:
        r4 = r4 + 1;
        r5 = r16;
        goto L11
    L24:
        r9 = r9 + ((long) r13.modCount);
        r5 = r16;
        r2 = r17;
        r3 = r3;
        r12 = r12 + 1;
        goto L9
    L25:
        boolean r19 = r2;
        long r110 = r3;
        Segment<K, V>[] r111 = r5;
        if (r9 == r6) goto L39;
        r6 = r9;
        r8 = r8 + 1;
        r5 = r111;
        r2 = r19;
        r3 = r110;
        goto L7
    L39:
        return r19;
    L30:
        return r2;
    }

    ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> r3, ReferenceEntry<K, V> r4) {
        return segmentFor(r3.getHash()).copyEntry(r3, r4);
    }

    Segment<K, V> createSegment(int r7, long r8, AbstractCache.StatsCounter r10) {
        return new Segment(this, r7, r8, r10);
    }

    boolean customWeigher() {
        if (this.weigher == CacheBuilder.OneWeigher.INSTANCE) goto L5;
        return true;
    L5:
        return false;
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

    boolean evictsBySize() {
        if (this.maxWeight < 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean expires() {
        if (expiresAfterWrite() == false) goto L5;
    L8:
        return true;
    L5:
        if (expiresAfterAccess() == true) goto L8;
        return false;
    }

    boolean expiresAfterAccess() {
        if (this.expireAfterAccessNanos <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    boolean expiresAfterWrite() {
        if (this.expireAfterWriteNanos <= 0) goto L5;
        return true;
    L5:
        return false;
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

    V get(K r3, CacheLoader<? super K, V> r4) throws ExecutionException {
        int r0 = hash(Preconditions.checkNotNull(r3));
        return segmentFor(r0).get(r3, r0, r4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ImmutableMap<K, V> getAll(Iterable<? extends K> r11) throws ExecutionException {
        int r0 = 0;
        int r1 = 0;
        LinkedHashMap r2 = Maps.newLinkedHashMap();
        LinkedHashSet r3 = Sets.newLinkedHashSet();
        Iterator<? extends K> r4 = r11.iterator();
    L4:
        if (r4.hasNext() == false) goto L36;
        K r5 = r4.next();
        Object r6 = get(r5);
        if (r2.containsKey(r5) == true) goto L4;
        r2.put(r5, r6);
        if (r6 == null) goto L9;
        r0 = r0 + 1;
        goto L4
    L9:
        r1 = r1 + 1;
        r3.add(r5);
        goto L4
    L36:
    L31:
        th = move-exception;
        this.globalStatsCounter.recordHits(r0);
        this.globalStatsCounter.recordMisses(r1);
        throw th;
    L13:
        if (r3.isEmpty() == false) goto L34;
    L28:
        ImmutableMap<K, V> r7 = ImmutableMap.copyOf(r2);     // Catch: Throwable -> L31
        this.globalStatsCounter.recordHits(r0);
        this.globalStatsCounter.recordMisses(r1);
        return r7;
    L34:
        Map r8 = loadAll(Collections.unmodifiableSet(r3), this.defaultLoader);     // Catch: CacheLoader.UnsupportedLoadingOperationException -> L23 Throwable -> L31
        Iterator r9 = r3.iterator();     // Catch: CacheLoader.UnsupportedLoadingOperationException -> L23 Throwable -> L31
    L16:
        if (r9.hasNext() == false) goto L28;
        Object r10 = r9.next();     // Catch: CacheLoader.UnsupportedLoadingOperationException -> L23 Throwable -> L31
        Object r12 = r8.get(r10);     // Catch: CacheLoader.UnsupportedLoadingOperationException -> L23 Throwable -> L31
        if (r12 == null) goto L21;
        r2.put(r10, r12);     // Catch: CacheLoader.UnsupportedLoadingOperationException -> L23 Throwable -> L31
        goto L16
    L21:
        throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + r10);     // Catch: CacheLoader.UnsupportedLoadingOperationException -> L23 Throwable -> L31
    L24:
        Iterator r13 = r3.iterator();     // Catch: Throwable -> L31
    L26:
        if (r13.hasNext() == false) goto L28;
        Object r14 = r13.next();     // Catch: Throwable -> L31
        r1 = r1 - 1;     // Catch: Throwable -> L31
        r2.put(r14, get(r14, this.defaultLoader));     // Catch: Throwable -> L31
        goto L26
    }

    /* JADX WARN: Multi-variable type inference failed */
    ImmutableMap<K, V> getAllPresent(Iterable<?> r8) {
        int r0 = 0;
        int r1 = 0;
        ImmutableMap.Builder r2 = ImmutableMap.builder();
        Iterator<?> r3 = r8.iterator();
    L4:
        if (r3.hasNext() == false) goto L10;
        Object r4 = r3.next();
        V r5 = get(r4);
        if (r5 == null) goto L7;
        r2.put(r4, r5);
        r0 = r0 + 1;
        goto L4
    L7:
        r1 = r1 + 1;
        goto L4
    L10:
        this.globalStatsCounter.recordHits(r0);
        this.globalStatsCounter.recordMisses(r1);
        return r2.buildKeepingLast();
    }

    @CheckForNull
    ReferenceEntry<K, V> getEntry(@CheckForNull Object r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        int r0 = hash(r3);
        return segmentFor(r0).getEntry(r3, r0);
    }

    @CheckForNull
    public V getIfPresent(Object r5) {
        int r0 = hash(Preconditions.checkNotNull(r5));
        V r1 = segmentFor(r0).get(r5, r0);
        if (r1 != null) goto L5;
        this.globalStatsCounter.recordMisses(1);
    L6:
        return r1;
    L5:
        this.globalStatsCounter.recordHits(1);
        goto L6
    }

    @CheckForNull
    V getLiveValue(ReferenceEntry<K, V> r4, long r5) {
        if (r4.getKey() != null) goto L5;
        return null;
    L5:
        V r0 = r4.getValueReference().get();
        if (r0 != null) goto L9;
        return null;
    L9:
        if (isExpired(r4, r5) == false) goto L11;
        return null;
    L11:
        return r0;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    @CheckForNull
    public V getOrDefault(@CheckForNull Object r3, @CheckForNull V r4) {
        V r0 = get(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        return r4;
    }

    V getOrLoad(K r2) throws ExecutionException {
        return get(r2, this.defaultLoader);
    }

    int hash(@CheckForNull Object r3) {
        return rehash(this.keyEquivalence.hash(r3));
    }

    void invalidateAll(Iterable<?> r3) {
        Iterator<?> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        remove(r0.next());
        goto L4
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        long r0 = 0;
        Segment<K, V>[] r2 = this.segments;
        int r3 = r2.length;
        int r5 = 0;
    L3:
        if (r5 >= r3) goto L9;
        Segment<K, V> r6 = r2[r5];
        if (r6.count != 0) goto L6;
        r0 = r0 + ((long) r6.modCount);
        r5 = r5 + 1;
        goto L3
    L6:
        return false;
    L9:
        if (r0 == 0) goto L20;
        int r4 = r2.length;
        int r8 = 0;
    L11:
        if (r8 >= r4) goto L17;
        Segment<K, V> r9 = r2[r8];
        if (r9.count != 0) goto L14;
        r0 = r0 - ((long) r9.modCount);
        r8 = r8 + 1;
        goto L11
    L14:
        return false;
    L17:
        if (r0 == 0) goto L19;
        return false;
    L19:
        return true;
    L20:
        return true;
    }

    boolean isExpired(ReferenceEntry<K, V> r7, long r8) {
        Preconditions.checkNotNull(r7);
        if (expiresAfterAccess() == false) goto L8;
        if ((r8 - r7.getAccessTime()) < this.expireAfterAccessNanos) goto L8;
        return true;
    L8:
        if (expiresAfterWrite() == true) goto L10;
        return false;
    L10:
        if ((r8 - r7.getWriteTime()) < this.expireAfterWriteNanos) goto L14;
        return true;
    L14:
        return false;
    }

    boolean isLive(ReferenceEntry<K, V> r2, long r3) {
        if (segmentFor(r2.getHash()).getLiveValue(r2, r3) == null) goto L5;
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

    @CheckForNull
    Map<K, V> loadAll(Set<? extends K> r9, CacheLoader<? super K, V> r10) throws ExecutionException {
        Preconditions.checkNotNull(r10);
        Preconditions.checkNotNull(r9);
        Stopwatch r0 = Stopwatch.createStarted();
        Map<? super K, V> r2 = r10.loadAll(r9);     // Catch: Throwable -> L25 Error -> L27 Exception -> L30 RuntimeException -> L33 InterruptedException -> L36 CacheLoader.UnsupportedLoadingOperationException -> L39
    L5:
        if (1 != 0) goto L7;
        this.globalStatsCounter.recordLoadException(r0.elapsed(TimeUnit.NANOSECONDS));
    L7:
        if (r2 == null) goto L23;
        r0.stop();
        boolean r3 = false;
        Iterator<Map.Entry<K, V>> r4 = r2.entrySet().iterator();
    L10:
        if (r4.hasNext() == false) goto L18;
        Map.Entry<K, V> r5 = r4.next();
        K r6 = r5.getKey();
        V r7 = r5.getValue();
        if (r6 == null) goto L16;
        if (r7 == null) goto L16;
        put(r6, r7);
    L16:
        r3 = true;
        goto L10
    L18:
        if (r3 == true) goto L21;
        this.globalStatsCounter.recordLoadSuccess(r0.elapsed(TimeUnit.NANOSECONDS));
        return r2;
    L21:
        this.globalStatsCounter.recordLoadException(r0.elapsed(TimeUnit.NANOSECONDS));
        throw new CacheLoader.InvalidCacheLoadException(r10 + " returned null keys or values from loadAll");
    L23:
        this.globalStatsCounter.recordLoadException(r0.elapsed(TimeUnit.NANOSECONDS));
        throw new CacheLoader.InvalidCacheLoadException(r10 + " returned null map from loadAll");
    L25:
        th = move-exception;
        if (0 != 0) goto L44;
        this.globalStatsCounter.recordLoadException(r0.elapsed(TimeUnit.NANOSECONDS));
    L44:
        throw th;
    L33:
        e = move-exception;
        throw new UncheckedExecutionException(e);     // Catch: Throwable -> L25
    L30:
        e = move-exception;
        throw new ExecutionException(e);     // Catch: Throwable -> L25
    L39:
        e = move-exception;
        throw e;     // Catch: Throwable -> L25
    L36:
        e = move-exception;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L25
        throw new ExecutionException(e);     // Catch: Throwable -> L25
    L27:
        e = move-exception;
        throw new ExecutionError(e);     // Catch: Throwable -> L25
    }

    long longSize() {
        Segment<K, V>[] r0 = this.segments;
        long r1 = 0;
        int r3 = r0.length;
        int r5 = 0;
    L3:
        if (r5 >= r3) goto L5;
        r1 = r1 + ((long) Math.max(0, r0[r5].count));
        r5 = r5 + 1;
        goto L3
    L5:
        return r1;
    }

    ReferenceEntry<K, V> newEntry(K r3, int r4, @CheckForNull ReferenceEntry<K, V> r5) {
        Segment<K, V> r0 = segmentFor(r4);
        r0.lock();
        ReferenceEntry<K, V> r1 = r0.newEntry(r3, r4, r5);     // Catch: Throwable -> L6
        r0.unlock();
        return r1;
    L6:
        th = move-exception;
        r0.unlock();
        throw th;
    }

    final Segment<K, V>[] newSegmentArray(int r2) {
        return new Segment[r2];
    }

    /* JADX WARN: Multi-variable type inference failed */
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
    ValueReference<K, V> newValueReference(ReferenceEntry<K, V> r5, V r6, int r7) {
        int r0 = r5.getHash();
        return this.valueStrength.referenceValue(segmentFor(r0), r5, Preconditions.checkNotNull(r6), r7);
    }

    void processPendingNotifications() {
    L2:
        RemovalNotification<K, V> r0 = this.removalNotificationQueue.poll();
        if (r0 == null) goto L9;
        this.removalListener.onRemoval(r0);     // Catch: Throwable -> L6
    L6:
        th = move-exception;
        logger.log(Level.WARNING, "Exception thrown by removal listener", th);
        goto L2
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

    void reclaimKey(ReferenceEntry<K, V> r3) {
        int r0 = r3.getHash();
        segmentFor(r0).reclaimKey(r3, r0);
    }

    void reclaimValue(ValueReference<K, V> r5) {
        ReferenceEntry<K, V> r0 = r5.getEntry();
        int r1 = r0.getHash();
        segmentFor(r1).reclaimValue(r0.getKey(), r1, r5);
    }

    boolean recordsAccess() {
        return expiresAfterAccess();
    }

    boolean recordsTime() {
        if (recordsWrite() == false) goto L5;
    L8:
        return true;
    L5:
        if (recordsAccess() == true) goto L8;
        return false;
    }

    boolean recordsWrite() {
        if (expiresAfterWrite() == false) goto L5;
    L8:
        return true;
    L5:
        if (refreshes() == true) goto L8;
        return false;
    }

    void refresh(K r5) {
        int r0 = hash(Preconditions.checkNotNull(r5));
        segmentFor(r0).refresh(r5, r0, this.defaultLoader, false);
    }

    boolean refreshes() {
        if (this.refreshNanos <= 0) goto L5;
        return true;
    L5:
        return false;
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

    Segment<K, V> segmentFor(int r4) {
        return this.segments[(r4 >>> this.segmentShift) & this.segmentMask];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return Ints.saturatedCast(longSize());
    }

    boolean usesAccessEntries() {
        if (usesAccessQueue() == false) goto L5;
    L8:
        return true;
    L5:
        if (recordsAccess() == true) goto L8;
        return false;
    }

    boolean usesAccessQueue() {
        if (expiresAfterAccess() == false) goto L5;
    L8:
        return true;
    L5:
        if (evictsBySize() == true) goto L8;
        return false;
    }

    boolean usesKeyReferences() {
        if (this.keyStrength == Strength.STRONG) goto L5;
        return true;
    L5:
        return false;
    }

    boolean usesValueReferences() {
        if (this.valueStrength == Strength.STRONG) goto L5;
        return true;
    L5:
        return false;
    }

    boolean usesWriteEntries() {
        if (usesWriteQueue() == false) goto L5;
    L8:
        return true;
    L5:
        if (recordsWrite() == true) goto L8;
        return false;
    }

    boolean usesWriteQueue() {
        return expiresAfterWrite();
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
}
