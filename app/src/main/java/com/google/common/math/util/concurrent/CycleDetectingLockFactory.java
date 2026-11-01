package com.google.common.util.concurrent;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class CycleDetectingLockFactory {
    private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = null;
    private static final ConcurrentMap<Class<? extends Enum<?>>, Map<? extends Enum<?>, LockGraphNode>> lockGraphNodesPerType = null;
    private static final LazyLogger logger = null;
    final Policy policy;

    private interface CycleDetectingLock {
        LockGraphNode getLockGraphNode();

        boolean isAcquiredByCurrentThread();
    }

    final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLock {
        private final LockGraphNode lockGraphNode;
        final /* synthetic */ CycleDetectingLockFactory this$0;

        private CycleDetectingReentrantLock(CycleDetectingLockFactory r2, LockGraphNode r3, boolean r4) {
            this.this$0 = r2;
            super(r4);
            this.lockGraphNode = (LockGraphNode) Preconditions.checkNotNull(r3);
        }

        /* synthetic */ CycleDetectingReentrantLock(CycleDetectingLockFactory r1, LockGraphNode r2, boolean r3, C09391 r4) {
            this(r1, r2, r3);
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public boolean isAcquiredByCurrentThread() {
            return isHeldByCurrentThread();
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void lock() {
            CycleDetectingLockFactory.access$600(this.this$0, this);
            super.lock();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this);
            return;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.access$600(this.this$0, this);
            super.lockInterruptibly();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this);
            return;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public boolean tryLock() {
            CycleDetectingLockFactory.access$600(this.this$0, this);
            boolean r0 = super.tryLock();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this);
            return r0;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public boolean tryLock(long r2, TimeUnit r4) throws InterruptedException {
            CycleDetectingLockFactory.access$600(this.this$0, this);
            boolean r0 = super.tryLock(r2, r4);     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this);
            return r0;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void unlock() {
            super.unlock();     // Catch: Throwable -> L5
            CycleDetectingLockFactory.access$700(this);
            return;
        L5:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this);
            throw th;
        }
    }

    private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;
        final /* synthetic */ CycleDetectingLockFactory this$0;

        CycleDetectingReentrantReadLock(CycleDetectingLockFactory r1, CycleDetectingReentrantReadWriteLock r2) {
            this.this$0 = r1;
            super(r2);
            this.readWriteLock = r2;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public void lock() {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            super.lock();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            super.lockInterruptibly();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public boolean tryLock() {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            boolean r0 = super.tryLock();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return r0;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public boolean tryLock(long r3, TimeUnit r5) throws InterruptedException {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            boolean r0 = super.tryLock(r3, r5);     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return r0;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock, java.util.concurrent.locks.Lock
        public void unlock() {
            super.unlock();     // Catch: Throwable -> L5
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return;
        L5:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }
    }

    final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLock {
        private final LockGraphNode lockGraphNode;
        private final CycleDetectingReentrantReadLock readLock;
        final /* synthetic */ CycleDetectingLockFactory this$0;
        private final CycleDetectingReentrantWriteLock writeLock;

        private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory r2, LockGraphNode r3, boolean r4) {
            this.this$0 = r2;
            super(r4);
            this.readLock = new CycleDetectingReentrantReadLock(r2, this);
            this.writeLock = new CycleDetectingReentrantWriteLock(r2, this);
            this.lockGraphNode = (LockGraphNode) Preconditions.checkNotNull(r3);
        }

        /* synthetic */ CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory r1, LockGraphNode r2, boolean r3, C09391 r4) {
            this(r1, r2, r3);
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        @Override // com.google.common.util.concurrent.CycleDetectingLockFactory.CycleDetectingLock
        public boolean isAcquiredByCurrentThread() {
            if (isWriteLockedByCurrentThread() == false) goto L5;
        L8:
            return true;
        L5:
            if (getReadHoldCount() > 0) goto L8;
            return false;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock, java.util.concurrent.locks.ReadWriteLock
        public /* bridge */ /* synthetic */ Lock readLock() {
            return readLock();
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock, java.util.concurrent.locks.ReadWriteLock
        public ReentrantReadWriteLock.ReadLock readLock() {
            return this.readLock;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock, java.util.concurrent.locks.ReadWriteLock
        public /* bridge */ /* synthetic */ Lock writeLock() {
            return writeLock();
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock, java.util.concurrent.locks.ReadWriteLock
        public ReentrantReadWriteLock.WriteLock writeLock() {
            return this.writeLock;
        }
    }

    private class CycleDetectingReentrantWriteLock extends ReentrantReadWriteLock.WriteLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;
        final /* synthetic */ CycleDetectingLockFactory this$0;

        CycleDetectingReentrantWriteLock(CycleDetectingLockFactory r1, CycleDetectingReentrantReadWriteLock r2) {
            this.this$0 = r1;
            super(r2);
            this.readWriteLock = r2;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public void lock() {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            super.lock();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            super.lockInterruptibly();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public boolean tryLock() {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            boolean r0 = super.tryLock();     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return r0;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public boolean tryLock(long r3, TimeUnit r5) throws InterruptedException {
            CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
            boolean r0 = super.tryLock(r3, r5);     // Catch: Throwable -> L6
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return r0;
        L6:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }

        @Override // java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock, java.util.concurrent.locks.Lock
        public void unlock() {
            super.unlock();     // Catch: Throwable -> L5
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            return;
        L5:
            th = move-exception;
            CycleDetectingLockFactory.access$700(this.readWriteLock);
            throw th;
        }
    }

    private static class ExampleStackTrace extends IllegalStateException {
        static final StackTraceElement[] EMPTY_STACK_TRACE = null;
        static final ImmutableSet<String> EXCLUDED_CLASS_NAMES = null;

        static {
            EMPTY_STACK_TRACE = new StackTraceElement[0];
            EXCLUDED_CLASS_NAMES = ImmutableSet.m262of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), LockGraphNode.class.getName());
        }

        ExampleStackTrace(LockGraphNode r6, LockGraphNode r7) {
            super(r6.getLockName() + " -> " + r7.getLockName());
            StackTraceElement[] r0 = getStackTrace();
            int r1 = 0;
            int r2 = r0.length;
        L3:
            if (r1 >= r2) goto L16;
            if (WithExplicitOrdering.class.getName().equals(r0[r1].getClassName()) == true) goto L6;
            if (EXCLUDED_CLASS_NAMES.contains(r0[r1].getClassName()) == false) goto L9;
            r1 = r1 + 1;
            goto L3
        L9:
            setStackTrace((StackTraceElement[]) Arrays.copyOfRange(r0, r1, r2));
            return;
        L6:
            setStackTrace(EMPTY_STACK_TRACE);
            return;
        }
    }

    private static class LockGraphNode {
        final Map<LockGraphNode, ExampleStackTrace> allowedPriorLocks;
        final Map<LockGraphNode, PotentialDeadlockException> disallowedPriorLocks;
        final String lockName;

        LockGraphNode(String r2) {
            this.allowedPriorLocks = new MapMaker().weakKeys().makeMap();
            this.disallowedPriorLocks = new MapMaker().weakKeys().makeMap();
            this.lockName = (String) Preconditions.checkNotNull(r2);
        }

        @CheckForNull
        private ExampleStackTrace findPathTo(LockGraphNode r6, Set<LockGraphNode> r7) {
            if (r7.add(this) == true) goto L5;
            return null;
        L5:
            ExampleStackTrace r0 = this.allowedPriorLocks.get(r6);
            if (r0 == null) goto L8;
            return r0;
        L8:
            Iterator<Map.Entry<LockGraphNode, ExampleStackTrace>> r2 = this.allowedPriorLocks.entrySet().iterator();
        L10:
            if (r2.hasNext() == false) goto L16;
            Map.Entry<LockGraphNode, ExampleStackTrace> r3 = r2.next();
            LockGraphNode r4 = r3.getKey();
            ExampleStackTrace r1 = r4.findPathTo(r6, r7);
            if (r1 == null) goto L10;
            ExampleStackTrace r5 = new ExampleStackTrace(r4, this);
            r5.setStackTrace(r3.getValue().getStackTrace());
            r5.initCause(r1);
            return r5;
        L16:
            return null;
        }

        void checkAcquiredLock(Policy r6, LockGraphNode r7) {
            if (this == r7) goto L4;
            boolean r0 = true;
        L5:
            Preconditions.checkState(r0, "Attempted to acquire multiple locks with the same rank %s", r7.getLockName());
            if (this.allowedPriorLocks.containsKey(r7) == false) goto L8;
            return;
        L8:
            PotentialDeadlockException r1 = this.disallowedPriorLocks.get(r7);
            C09391 r2 = null;
            if (r1 == null) goto L12;
            r6.handlePotentialDeadlock(new PotentialDeadlockException(r7, this, r1.getConflictingStackTrace(), r2));
            return;
        L12:
            ExampleStackTrace r3 = r7.findPathTo(this, Sets.newIdentityHashSet());
            if (r3 != null) goto L15;
            this.allowedPriorLocks.put(r7, new ExampleStackTrace(r7, this));
            return;
        L15:
            PotentialDeadlockException r4 = new PotentialDeadlockException(r7, this, r3, r2);
            this.disallowedPriorLocks.put(r7, r4);
            r6.handlePotentialDeadlock(r4);
            return;
        L4:
            r0 = false;
            goto L5
        }

        void checkAcquiredLocks(Policy r3, List<LockGraphNode> r4) {
            Iterator<LockGraphNode> r0 = r4.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            checkAcquiredLock(r3, r0.next());
            goto L4
        }

        String getLockName() {
            return this.lockName;
        }
    }

    public static abstract enum Policies extends Enum<Policies> implements Policy {
        private static final /* synthetic */ Policies[] $VALUES = null;
        public static final Policies DISABLED = null;
        public static final Policies THROW = null;
        public static final Policies WARN = null;

        private static /* synthetic */ Policies[] $values() {
            return new Policies[]{THROW, WARN, DISABLED};
        }

        static {
            final String r1 = "THROW";
            final int r2 = 0;
            THROW = new C09401(r1, r2);
            final String r3 = "WARN";
            final int r4 = 1;
            WARN = new C09412(r3, r4);
            final String r5 = "DISABLED";
            final int r6 = 2;
            DISABLED = new C09423(r5, r6);
            $VALUES = $values();
        }

        private Policies(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ Policies(String r1, int r2, C09391 r3) {
            this(r1, r2);
        }

        public static Policies valueOf(String r1) {
            return (Policies) Enum.valueOf(Policies.class, r1);
        }

        public static Policies[] values() {
            return (Policies[]) $VALUES.clone();
        }
    }

    public interface Policy {
        void handlePotentialDeadlock(PotentialDeadlockException r1);
    }

    public static final class PotentialDeadlockException extends ExampleStackTrace {
        private final ExampleStackTrace conflictingStackTrace;

        private PotentialDeadlockException(LockGraphNode r1, LockGraphNode r2, ExampleStackTrace r3) {
            super(r1, r2);
            this.conflictingStackTrace = r3;
            initCause(r3);
        }

        /* synthetic */ PotentialDeadlockException(LockGraphNode r1, LockGraphNode r2, ExampleStackTrace r3, C09391 r4) {
            this(r1, r2, r3);
        }

        public ExampleStackTrace getConflictingStackTrace() {
            return this.conflictingStackTrace;
        }

        @Override // java.lang.Throwable
        public String getMessage() {
            StringBuilder r0 = new StringBuilder((String) Objects.requireNonNull(super.getMessage()));
            Throwable r1 = this.conflictingStackTrace;
        L3:
            if (r1 == null) goto L6;
            r0.append(", ").append(r1.getMessage());
            r1 = r1.getCause();
            goto L3
        L6:
            return r0.toString();
        }
    }

    public static final class WithExplicitOrdering<E extends Enum<E>> extends CycleDetectingLockFactory {
        private final Map<E, LockGraphNode> lockGraphNodes;

        WithExplicitOrdering(Policy r2, Map<E, LockGraphNode> r3) {
            super(r2, null);
            this.lockGraphNodes = r3;
        }

        public ReentrantLock newReentrantLock(E r2) {
            return newReentrantLock(r2, false);
        }

        public ReentrantLock newReentrantLock(E r4, boolean r5) {
            if (this.policy != Policies.DISABLED) goto L6;
            return new ReentrantLock(r5);
        L6:
            return new CycleDetectingReentrantLock(this, (LockGraphNode) Objects.requireNonNull(this.lockGraphNodes.get(r4)), r5, null);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E r2) {
            return newReentrantReadWriteLock(r2, false);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E r4, boolean r5) {
            if (this.policy != Policies.DISABLED) goto L6;
            return new ReentrantReadWriteLock(r5);
        L6:
            return new CycleDetectingReentrantReadWriteLock(this, (LockGraphNode) Objects.requireNonNull(this.lockGraphNodes.get(r4)), r5, null);
        }
    }

    static {
        lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
        logger = new LazyLogger(CycleDetectingLockFactory.class);
        acquiredLocks = new C09391();
    }

    private CycleDetectingLockFactory(Policy r2) {
        this.policy = (Policy) Preconditions.checkNotNull(r2);
    }

    /* synthetic */ CycleDetectingLockFactory(Policy r1, C09391 r2) {
        this(r1);
    }

    private void aboutToAcquire(CycleDetectingLock r4) {
        if (r4.isAcquiredByCurrentThread() == true) goto L6;
        ArrayList r0 = (ArrayList) Objects.requireNonNull(acquiredLocks.get());
        LockGraphNode r1 = r4.getLockGraphNode();
        r1.checkAcquiredLocks(this.policy, r0);
        r0.add(r1);
        return;
    }

    static /* synthetic */ LazyLogger access$100() {
        return logger;
    }

    static /* synthetic */ void access$600(CycleDetectingLockFactory r0, CycleDetectingLock r1) {
        r0.aboutToAcquire(r1);
    }

    static /* synthetic */ void access$700(CycleDetectingLock r0) {
        lockStateChanged(r0);
    }

    static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> r10) {
        EnumMap r0 = Maps.newEnumMap(r10);
        E[] r1 = r10.getEnumConstants();
        int r2 = r1.length;
        ArrayList r3 = Lists.newArrayListWithCapacity(r2);
        int r4 = r1.length;
        int r6 = 0;
    L3:
        if (r6 >= r4) goto L5;
        E r7 = r1[r6];
        LockGraphNode r8 = new LockGraphNode(getLockName(r7));
        r3.add(r8);
        r0.put(r7, r8);
        r6 = r6 + 1;
        goto L3
    L5:
        int r5 = 1;
    L6:
        if (r5 >= r2) goto L8;
        ((LockGraphNode) r3.get(r5)).checkAcquiredLocks(Policies.THROW, r3.subList(0, r5));
        r5 = r5 + 1;
        goto L6
    L8:
        int r9 = 0;
    L10:
        if (r9 >= (r2 - 1)) goto L13;
        ((LockGraphNode) r3.get(r9)).checkAcquiredLocks(Policies.DISABLED, r3.subList(r9 + 1, r2));
        r9 = r9 + 1;
        goto L10
    L13:
        return Collections.unmodifiableMap(r0);
    }

    private static String getLockName(Enum<?> r2) {
        return r2.getDeclaringClass().getSimpleName() + "." + r2.name();
    }

    private static <E extends Enum<E>> Map<? extends E, LockGraphNode> getOrCreateNodes(Class<E> r3) {
        Map<? extends E, LockGraphNode> r0 = (Map) lockGraphNodesPerType.get(r3);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Map<? extends Enum<?>, LockGraphNode> r1 = createNodes(r3);
        return (Map) MoreObjects.firstNonNull(lockGraphNodesPerType.putIfAbsent(r3, r1), r1);
    }

    private static void lockStateChanged(CycleDetectingLock r4) {
        if (r4.isAcquiredByCurrentThread() == true) goto L10;
        ArrayList r0 = (ArrayList) Objects.requireNonNull(acquiredLocks.get());
        LockGraphNode r1 = r4.getLockGraphNode();
        int r2 = r0.size() - 1;
    L5:
        if (r2 < 0) goto L14;
        if (r0.get(r2) == r1) goto L8;
        r2 = r2 - 1;
        goto L5
    L8:
        r0.remove(r2);
        return;
    L14:
        return;
    }

    public static CycleDetectingLockFactory newInstance(Policy r1) {
        return new CycleDetectingLockFactory(r1);
    }

    public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> r2, Policy r3) {
        Preconditions.checkNotNull(r2);
        Preconditions.checkNotNull(r3);
        return new WithExplicitOrdering(r3, getOrCreateNodes(r2));
    }

    public ReentrantLock newReentrantLock(String r2) {
        return newReentrantLock(r2, false);
    }

    public ReentrantLock newReentrantLock(String r4, boolean r5) {
        if (this.policy != Policies.DISABLED) goto L6;
        return new ReentrantLock(r5);
    L6:
        return new CycleDetectingReentrantLock(this, new LockGraphNode(r4), r5, null);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String r2) {
        return newReentrantReadWriteLock(r2, false);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String r4, boolean r5) {
        if (this.policy != Policies.DISABLED) goto L6;
        return new ReentrantReadWriteLock(r5);
    L6:
        return new CycleDetectingReentrantReadWriteLock(this, new LockGraphNode(r4), r5, null);
    }
}
