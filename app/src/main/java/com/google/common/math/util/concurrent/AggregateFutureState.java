package com.google.common.util.concurrent;

import androidx.concurrent.futures.C0037xc40028dd;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AggregateFutureState<OutputT> extends AbstractFuture.TrustedFuture<OutputT> {
    private static final AtomicHelper ATOMIC_HELPER = null;
    private static final LazyLogger log = null;
    private volatile int remaining;

    @CheckForNull
    private volatile Set<Throwable> seenExceptions;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.AggregateFutureState$1 */
    static /* synthetic */ class C09171 {
    }

    private static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        /* synthetic */ AtomicHelper(C09171 r1) {
            this();
        }

        abstract void compareAndSetSeenExceptions(AggregateFutureState<?> r1, @CheckForNull Set<Throwable> r2, Set<Throwable> r3);

        abstract int decrementAndGetRemainingCount(AggregateFutureState<?> r1);
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicIntegerFieldUpdater<? super AggregateFutureState<?>> remainingCountUpdater;
        final AtomicReferenceFieldUpdater<? super AggregateFutureState<?>, ? super Set<Throwable>> seenExceptionsUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<? super AggregateFutureState<?>, ? super Set<Throwable>> r2, AtomicIntegerFieldUpdater<? super AggregateFutureState<?>> r3) {
            super(null);
            this.seenExceptionsUpdater = r2;
            this.remainingCountUpdater = r3;
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        void compareAndSetSeenExceptions(AggregateFutureState<?> r2, @CheckForNull Set<Throwable> r3, Set<Throwable> r4) {
            C0037xc40028dd.m4m(this.seenExceptionsUpdater, r2, r3, r4);
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        int decrementAndGetRemainingCount(AggregateFutureState<?> r2) {
            return this.remainingCountUpdater.decrementAndGet(r2);
        }
    }

    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super(null);
        }

        /* synthetic */ SynchronizedAtomicHelper(C09171 r1) {
            this();
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        void compareAndSetSeenExceptions(AggregateFutureState<?> r2, @CheckForNull Set<Throwable> r3, Set<Throwable> r4) {
            monitor-enter(r2);
        L8:
            th = move-exception;
            throw th;
        L4:
            if (AggregateFutureState.access$200(r2) != r3) goto L6;
            AggregateFutureState.access$202(r2, r4);     // Catch: Throwable -> L8
        L6:
            monitor-exit(r2);     // Catch: Throwable -> L8
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        int decrementAndGetRemainingCount(AggregateFutureState<?> r2) {
            monitor-enter(r2);
            int r0 = AggregateFutureState.access$306(r2);     // Catch: Throwable -> L6
            monitor-exit(r2);     // Catch: Throwable -> L6
            return r0;
        L6:
            th = move-exception;
            throw th;
        }
    }

    static {
        log = new LazyLogger(AggregateFutureState.class);
        Throwable r0 = null;
        AtomicHelper r2 = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));     // Catch: Throwable -> L5
    L7:
        ATOMIC_HELPER = r2;
        if (r0 == null) goto L13;
        log.get().log(Level.SEVERE, "SafeAtomicHelper is broken!", r0);
        return;
    L13:
        return;
    L5:
        th = move-exception;
        r0 = th;
        r2 = new SynchronizedAtomicHelper(null);
        goto L7
    }

    AggregateFutureState(int r2) {
        this.seenExceptions = null;
        this.remaining = r2;
    }

    static /* synthetic */ Set access$200(AggregateFutureState r1) {
        return r1.seenExceptions;
    }

    static /* synthetic */ Set access$202(AggregateFutureState r0, Set r1) {
        r0.seenExceptions = r1;
        return r1;
    }

    static /* synthetic */ int access$306(AggregateFutureState r1) {
        int r0 = r1.remaining - 1;
        r1.remaining = r0;
        return r0;
    }

    abstract void addInitialException(Set<Throwable> r1);

    final void clearSeenExceptions() {
        this.seenExceptions = null;
    }

    final int decrementRemainingAndGet() {
        return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }

    final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> r0 = this.seenExceptions;
        if (r0 != null) goto L6;
        Set<Throwable> r1 = Sets.newConcurrentHashSet();
        addInitialException(r1);
        ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, r1);
        return (Set) Objects.requireNonNull(this.seenExceptions);
    L6:
        return r0;
    }
}
