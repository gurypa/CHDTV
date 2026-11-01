package com.google.common.util.concurrent;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class Striped<L> {
    private static final int ALL_SET = -1;
    private static final int LARGE_LAZY_CUTOFF = 1024;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.Striped$1 */
    static /* synthetic */ class C09641 {
    }

    private static class CompactStriped<L> extends PowerOfTwoStriped<L> {
        private final Object[] array;

        private CompactStriped(int r4, Supplier<L> r5) {
            super(r4);
            if (r4 > 1073741824) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "Stripes must be <= 2^30)");
            this.array = new Object[this.mask + 1];
            int r1 = 0;
        L8:
            if (r1 >= this.array.length) goto L10;
            this.array[r1] = r5.get();
            r1 = r1 + 1;
            goto L8
        L10:
            return;
        L5:
            r0 = false;
            goto L6
        }

        /* synthetic */ CompactStriped(int r1, Supplier r2, C09641 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int r2) {
            return (L) this.array[r2];
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.array.length;
        }
    }

    static class LargeLazyStriped<L> extends PowerOfTwoStriped<L> {
        final ConcurrentMap<Integer, L> locks;
        final int size;
        final Supplier<L> supplier;

        LargeLazyStriped(int r3, Supplier<L> r4) {
            super(r3);
            if (this.mask != (-1)) goto L5;
            int r0 = Integer.MAX_VALUE;
        L6:
            this.size = r0;
            this.supplier = r4;
            this.locks = new MapMaker().weakValues().makeMap();
            return;
        L5:
            r0 = this.mask + 1;
            goto L6
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int r5) {
            if (this.size == Integer.MAX_VALUE) goto L5;
            Preconditions.checkElementIndex(r5, size());
        L5:
            L r0 = this.locks.get(Integer.valueOf(r5));
            if (r0 == null) goto L8;
            return r0;
        L8:
            L r1 = this.supplier.get();
            return (L) MoreObjects.firstNonNull(this.locks.putIfAbsent(Integer.valueOf(r5), r1), r1);
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.size;
        }
    }

    private static class PaddedLock extends ReentrantLock {
        long unused1;
        long unused2;
        long unused3;

        PaddedLock() {
            super(false);
        }
    }

    private static class PaddedSemaphore extends Semaphore {
        long unused1;
        long unused2;
        long unused3;

        PaddedSemaphore(int r2) {
            super(r2, false);
        }
    }

    private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
        final int mask;

        PowerOfTwoStriped(int r4) {
            super(null);
            if (r4 <= 0) goto L5;
            boolean r1 = true;
        L6:
            Preconditions.checkArgument(r1, "Stripes must be positive");
            if (r4 <= 1073741824) goto L9;
            int r0 = -1;
        L10:
            this.mask = r0;
            return;
        L9:
            r0 = Striped.access$200(r4) - 1;
            goto L10
        L5:
            r1 = false;
            goto L6
        }

        @Override // com.google.common.util.concurrent.Striped
        public final L get(Object r2) {
            return getAt(indexFor(r2));
        }

        @Override // com.google.common.util.concurrent.Striped
        final int indexFor(Object r3) {
            int r0 = Striped.access$300(r3.hashCode());
            return this.mask & r0;
        }
    }

    static class SmallLazyStriped<L> extends PowerOfTwoStriped<L> {
        final AtomicReferenceArray<ArrayReference<? extends L>> locks;
        final ReferenceQueue<L> queue;
        final int size;
        final Supplier<L> supplier;

        private static final class ArrayReference<L> extends WeakReference<L> {
            final int index;

            ArrayReference(L r1, int r2, ReferenceQueue<L> r3) {
                super(r1, r3);
                this.index = r2;
            }
        }

        SmallLazyStriped(int r3, Supplier<L> r4) {
            super(r3);
            this.queue = new ReferenceQueue();
            if (this.mask != (-1)) goto L5;
            int r0 = Integer.MAX_VALUE;
        L6:
            this.size = r0;
            this.locks = new AtomicReferenceArray(this.size);
            this.supplier = r4;
            return;
        L5:
            r0 = this.mask + 1;
            goto L6
        }

        private void drainQueue() {
        L2:
            Reference<? extends L> r0 = this.queue.poll();
            if (r0 == null) goto L5;
            ArrayReference r1 = (ArrayReference) r0;
            C0965xa7a47114.m388m(this.locks, r1.index, r1, null);
            goto L2
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int r7) {
            if (this.size == Integer.MAX_VALUE) goto L5;
            Preconditions.checkElementIndex(r7, size());
        L5:
            ArrayReference<? extends L> r0 = this.locks.get(r7);
            if (r0 != null) goto L8;
            L r2 = null;
        L9:
            if (r2 == null) goto L11;
            return r2;
        L11:
            L r3 = this.supplier.get();
            ArrayReference r4 = new ArrayReference(r3, r7, this.queue);
        L13:
            if (C0965xa7a47114.m388m(this.locks, r7, r0, r4) == true) goto L21;
            r0 = this.locks.get(r7);
            if (r0 != null) goto L17;
            Object r5 = null;
        L18:
            L r6 = (L) r5;
            if (r6 == null) goto L13;
            return r6;
        L17:
            r5 = r0.get();
            goto L18
        L21:
            drainQueue();
            return r3;
        L8:
            r2 = (L) r0.get();
            goto L9
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.size;
        }
    }

    private static final class WeakSafeCondition extends ForwardingCondition {
        private final Condition delegate;
        private final WeakSafeReadWriteLock strongReference;

        WeakSafeCondition(Condition r1, WeakSafeReadWriteLock r2) {
            this.delegate = r1;
            this.strongReference = r2;
        }

        @Override // com.google.common.util.concurrent.ForwardingCondition
        Condition delegate() {
            return this.delegate;
        }
    }

    private static final class WeakSafeLock extends ForwardingLock {
        private final Lock delegate;
        private final WeakSafeReadWriteLock strongReference;

        WeakSafeLock(Lock r1, WeakSafeReadWriteLock r2) {
            this.delegate = r1;
            this.strongReference = r2;
        }

        @Override // com.google.common.util.concurrent.ForwardingLock
        Lock delegate() {
            return this.delegate;
        }

        @Override // com.google.common.util.concurrent.ForwardingLock, java.util.concurrent.locks.Lock
        public Condition newCondition() {
            return new WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
        }
    }

    private static final class WeakSafeReadWriteLock implements ReadWriteLock {
        private final ReadWriteLock delegate;

        WeakSafeReadWriteLock() {
            this.delegate = new ReentrantReadWriteLock();
        }

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock readLock() {
            return new WeakSafeLock(this.delegate.readLock(), this);
        }

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock writeLock() {
            return new WeakSafeLock(this.delegate.writeLock(), this);
        }
    }

    private Striped() {
    }

    /* synthetic */ Striped(C09641 r1) {
        this();
    }

    static /* synthetic */ int access$200(int r1) {
        return ceilToPowerOfTwo(r1);
    }

    static /* synthetic */ int access$300(int r1) {
        return smear(r1);
    }

    private static int ceilToPowerOfTwo(int r2) {
        return 1 << IntMath.log2(r2, RoundingMode.CEILING);
    }

    static <L> Striped<L> custom(int r2, Supplier<L> r3) {
        return new CompactStriped(r2, r3, null);
    }

    static /* synthetic */ Lock lambda$lazyWeakLock$0() {
        return new ReentrantLock(false);
    }

    static /* synthetic */ Semaphore lambda$lazyWeakSemaphore$2(int r2) {
        return new Semaphore(r2, false);
    }

    static /* synthetic */ Semaphore lambda$semaphore$1(int r1) {
        return new PaddedSemaphore(r1);
    }

    static <L> Striped<L> lazyWeakCustom(int r1, Supplier<L> r2) {
        if (r1 >= 1024) goto L6;
        return new SmallLazyStriped(r1, r2);
    L6:
        return new LargeLazyStriped(r1, r2);
    }

    public static Striped<Lock> lazyWeakLock(int r1) {
        return lazyWeakCustom(r1, new Striped$$ExternalSyntheticLambda4());
    }

    public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int r1) {
        return lazyWeakCustom(r1, new Striped$$ExternalSyntheticLambda0());
    }

    public static Striped<Semaphore> lazyWeakSemaphore(int r1, final int r2) {
        return lazyWeakCustom(r1, new Striped$$ExternalSyntheticLambda3(r2));
    }

    public static Striped<Lock> lock(int r1) {
        return custom(r1, new Striped$$ExternalSyntheticLambda1());
    }

    public static Striped<ReadWriteLock> readWriteLock(int r1) {
        return custom(r1, new Striped$$ExternalSyntheticLambda5());
    }

    public static Striped<Semaphore> semaphore(int r1, final int r2) {
        return custom(r1, new Striped$$ExternalSyntheticLambda2(r2));
    }

    private static int smear(int r2) {
        int r3 = r2 ^ ((r2 >>> 20) ^ (r2 >>> 12));
        return ((r3 >>> 7) ^ r3) ^ (r3 >>> 4);
    }

    public Iterable<L> bulkGet(Iterable<? extends Object> r7) {
        ArrayList r0 = Lists.newArrayList(r7);
        if (r0.isEmpty() == true) goto L5;
        int[] r1 = new int[r0.size()];
        int r2 = 0;
    L8:
        if (r2 >= r0.size()) goto L10;
        r1[r2] = indexFor(r0.get(r2));
        r2 = r2 + 1;
        goto L8
    L10:
        Arrays.sort(r1);
        int r3 = r1[0];
        r0.set(0, getAt(r3));
        int r4 = 1;
    L12:
        if (r4 >= r0.size()) goto L19;
        int r5 = r1[r4];
        if (r5 != r3) goto L16;
        r0.set(r4, r0.get(r4 - 1));
    L17:
        r4 = r4 + 1;
        goto L12
    L16:
        r0.set(r4, getAt(r5));
        r3 = r5;
        goto L17
    L19:
        return Collections.unmodifiableList(r0);
    L5:
        return ImmutableList.m212of();
    }

    public abstract L get(Object r1);

    public abstract L getAt(int r1);

    abstract int indexFor(Object r1);

    public abstract int size();
}
