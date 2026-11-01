package com.google.common.util.concurrent;

import androidx.concurrent.futures.C0037xc40028dd;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import javax.annotation.CheckForNull;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    private static final AtomicHelper ATOMIC_HELPER = null;
    static final boolean GENERATE_CANCELLATION_CAUSES = false;
    private static final Object NULL = null;
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    static final LazyLogger log = null;

    @CheckForNull
    private volatile Listener listeners;

    @CheckForNull
    private volatile Object value;

    @CheckForNull
    private volatile Waiter waiters;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.AbstractFuture$1 */
    static /* synthetic */ class C09001 {
    }

    private static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        /* synthetic */ AtomicHelper(C09001 r1) {
            this();
        }

        abstract boolean casListeners(AbstractFuture<?> r1, @CheckForNull Listener r2, Listener r3);

        abstract boolean casValue(AbstractFuture<?> r1, @CheckForNull Object r2, Object r3);

        abstract boolean casWaiters(AbstractFuture<?> r1, @CheckForNull Waiter r2, @CheckForNull Waiter r3);

        abstract Listener gasListeners(AbstractFuture<?> r1, Listener r2);

        abstract Waiter gasWaiters(AbstractFuture<?> r1, Waiter r2);

        abstract void putNext(Waiter r1, @CheckForNull Waiter r2);

        abstract void putThread(Waiter r1, Thread r2);
    }

    private static final class Cancellation {

        @CheckForNull
        static final Cancellation CAUSELESS_CANCELLED = null;

        @CheckForNull
        static final Cancellation CAUSELESS_INTERRUPTED = null;

        @CheckForNull
        final Throwable cause;
        final boolean wasInterrupted;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES == false) goto L5;
            CAUSELESS_CANCELLED = null;
            CAUSELESS_INTERRUPTED = null;
            return;
        L5:
            CAUSELESS_CANCELLED = new Cancellation(false, null);
            CAUSELESS_INTERRUPTED = new Cancellation(true, null);
        }

        Cancellation(boolean r1, @CheckForNull Throwable r2) {
            this.wasInterrupted = r1;
            this.cause = r2;
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = null;
        final Throwable exception;

        static {
            final String r2 = "Failure occurred while trying to finish a future.";
            FALLBACK_INSTANCE = new Failure(new C09011(r2));
        }

        Failure(Throwable r2) {
            this.exception = (Throwable) Preconditions.checkNotNull(r2);
        }
    }

    private static final class Listener {
        static final Listener TOMBSTONE = null;

        @CheckForNull
        final Executor executor;

        @CheckForNull
        Listener next;

        @CheckForNull
        final Runnable task;

        static {
            TOMBSTONE = new Listener();
        }

        Listener() {
            this.task = null;
            this.executor = null;
        }

        Listener(Runnable r1, Executor r2) {
            this.task = r1;
            this.executor = r2;
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<? super AbstractFuture<?>, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<? super AbstractFuture<?>, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<? super AbstractFuture<?>, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> r2, AtomicReferenceFieldUpdater<Waiter, Waiter> r3, AtomicReferenceFieldUpdater<? super AbstractFuture<?>, Waiter> r4, AtomicReferenceFieldUpdater<? super AbstractFuture<?>, Listener> r5, AtomicReferenceFieldUpdater<? super AbstractFuture<?>, Object> r6) {
            super(null);
            this.waiterThreadUpdater = r2;
            this.waiterNextUpdater = r3;
            this.waitersUpdater = r4;
            this.listenersUpdater = r5;
            this.valueUpdater = r6;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> r2, @CheckForNull Listener r3, Listener r4) {
            return C0037xc40028dd.m4m(this.listenersUpdater, r2, r3, r4);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> r2, @CheckForNull Object r3, Object r4) {
            return C0037xc40028dd.m4m(this.valueUpdater, r2, r3, r4);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> r2, @CheckForNull Waiter r3, @CheckForNull Waiter r4) {
            return C0037xc40028dd.m4m(this.waitersUpdater, r2, r3, r4);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Listener gasListeners(AbstractFuture<?> r2, Listener r3) {
            return this.listenersUpdater.getAndSet(r2, r3);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Waiter gasWaiters(AbstractFuture<?> r2, Waiter r3) {
            return this.waitersUpdater.getAndSet(r2, r3);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter r2, @CheckForNull Waiter r3) {
            this.waiterNextUpdater.lazySet(r2, r3);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter r2, Thread r3) {
            this.waiterThreadUpdater.lazySet(r2, r3);
        }
    }

    private static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        SetFuture(AbstractFuture<V> r1, ListenableFuture<? extends V> r2) {
            this.owner = r1;
            this.future = r2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AbstractFuture.access$300(this.owner) == this) goto L5;
            return;
        L5:
            Object r0 = AbstractFuture.access$400(this.future);
            if (AbstractFuture.access$200().casValue(this.owner, this, r0) == false) goto L9;
            AbstractFuture.access$500(this.owner, false);
            return;
        }
    }

    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super(null);
        }

        /* synthetic */ SynchronizedHelper(C09001 r1) {
            this();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> r2, @CheckForNull Listener r3, Listener r4) {
            monitor-enter(r2);
        L12:
            th = move-exception;
            throw th;
        L4:
            if (AbstractFuture.access$700(r2) != r3) goto L9;
            AbstractFuture.access$702(r2, r4);     // Catch: Throwable -> L12
            monitor-exit(r2);     // Catch: Throwable -> L12
            return true;
        L9:
            monitor-exit(r2);     // Catch: Throwable -> L12
            return false;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> r2, @CheckForNull Object r3, Object r4) {
            monitor-enter(r2);
        L12:
            th = move-exception;
            throw th;
        L4:
            if (AbstractFuture.access$300(r2) != r3) goto L9;
            AbstractFuture.access$302(r2, r4);     // Catch: Throwable -> L12
            monitor-exit(r2);     // Catch: Throwable -> L12
            return true;
        L9:
            monitor-exit(r2);     // Catch: Throwable -> L12
            return false;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> r2, @CheckForNull Waiter r3, @CheckForNull Waiter r4) {
            monitor-enter(r2);
        L12:
            th = move-exception;
            throw th;
        L4:
            if (AbstractFuture.access$800(r2) != r3) goto L9;
            AbstractFuture.access$802(r2, r4);     // Catch: Throwable -> L12
            monitor-exit(r2);     // Catch: Throwable -> L12
            return true;
        L9:
            monitor-exit(r2);     // Catch: Throwable -> L12
            return false;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Listener gasListeners(AbstractFuture<?> r2, Listener r3) {
            monitor-enter(r2);
            Listener r0 = AbstractFuture.access$700(r2);     // Catch: Throwable -> L8
            if (r0 == r3) goto L6;
            AbstractFuture.access$702(r2, r3);     // Catch: Throwable -> L8
        L6:
            monitor-exit(r2);     // Catch: Throwable -> L8
            return r0;
        L8:
            th = move-exception;
            throw th;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Waiter gasWaiters(AbstractFuture<?> r2, Waiter r3) {
            monitor-enter(r2);
            Waiter r0 = AbstractFuture.access$800(r2);     // Catch: Throwable -> L8
            if (r0 == r3) goto L6;
            AbstractFuture.access$802(r2, r3);     // Catch: Throwable -> L8
        L6:
            monitor-exit(r2);     // Catch: Throwable -> L8
            return r0;
        L8:
            th = move-exception;
            throw th;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter r1, @CheckForNull Waiter r2) {
            r1.next = r2;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter r1, Thread r2) {
            r1.thread = r2;
        }
    }

    interface Trusted<V> extends ListenableFuture<V> {
    }

    static abstract class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V> {
        TrustedFuture() {
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable r1, Executor r2) {
            super.addListener(r1, r2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean cancel(boolean r2) {
            return super.cancel(r2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get() throws InterruptedException, ExecutionException {
            return (V) super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get(long r2, TimeUnit r4) throws InterruptedException, ExecutionException, TimeoutException {
            return (V) super.get(r2, r4);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return super.isDone();
        }
    }

    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET = 0;
        static final Unsafe UNSAFE = null;
        static final long VALUE_OFFSET = 0;
        static final long WAITERS_OFFSET = 0;
        static final long WAITER_NEXT_OFFSET = 0;
        static final long WAITER_THREAD_OFFSET = 0;

        static {
            Unsafe r1 = Unsafe.getUnsafe();     // Catch: SecurityException -> L5
        L18:
            WAITERS_OFFSET = r1.objectFieldOffset(AbstractFuture.class.getDeclaredField("waiters"));     // Catch: NoSuchFieldException -> L10
            LISTENERS_OFFSET = r1.objectFieldOffset(AbstractFuture.class.getDeclaredField("listeners"));     // Catch: NoSuchFieldException -> L10
            VALUE_OFFSET = r1.objectFieldOffset(AbstractFuture.class.getDeclaredField("value"));     // Catch: NoSuchFieldException -> L10
            WAITER_THREAD_OFFSET = r1.objectFieldOffset(Waiter.class.getDeclaredField("thread"));     // Catch: NoSuchFieldException -> L10
            WAITER_NEXT_OFFSET = r1.objectFieldOffset(Waiter.class.getDeclaredField("next"));     // Catch: NoSuchFieldException -> L10
            UNSAFE = r1;     // Catch: NoSuchFieldException -> L10
            return;
        L10:
            e = move-exception;
            throw new RuntimeException(e);
        L20:
            r1 = (Unsafe) AccessController.doPrivileged(new C09031());     // Catch: PrivilegedActionException -> L13
        L13:
            e = move-exception;
            throw new RuntimeException("Could not initialize intrinsics", e.getCause());
        }

        private UnsafeAtomicHelper() {
            super(null);
        }

        /* synthetic */ UnsafeAtomicHelper(C09001 r1) {
            this();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casListeners(AbstractFuture<?> r7, @CheckForNull Listener r8, Listener r9) {
            return C0902x75145c29.m380m(UNSAFE, r7, LISTENERS_OFFSET, r8, r9);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casValue(AbstractFuture<?> r7, @CheckForNull Object r8, Object r9) {
            return C0902x75145c29.m380m(UNSAFE, r7, VALUE_OFFSET, r8, r9);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        boolean casWaiters(AbstractFuture<?> r7, @CheckForNull Waiter r8, @CheckForNull Waiter r9) {
            return C0902x75145c29.m380m(UNSAFE, r7, WAITERS_OFFSET, r8, r9);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Listener gasListeners(AbstractFuture<?> r3, Listener r4) {
        L2:
            Listener r0 = AbstractFuture.access$700(r3);
            if (r4 == r0) goto L4;
            if (casListeners(r3, r0, r4) == false) goto L2;
            return r0;
        L4:
            return r0;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        Waiter gasWaiters(AbstractFuture<?> r3, Waiter r4) {
        L2:
            Waiter r0 = AbstractFuture.access$800(r3);
            if (r4 == r0) goto L4;
            if (casWaiters(r3, r0, r4) == false) goto L2;
            return r0;
        L4:
            return r0;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putNext(Waiter r4, @CheckForNull Waiter r5) {
            UNSAFE.putObject(r4, WAITER_NEXT_OFFSET, r5);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        void putThread(Waiter r4, Thread r5) {
            UNSAFE.putObject(r4, WAITER_THREAD_OFFSET, r5);
        }
    }

    private static final class Waiter {
        static final Waiter TOMBSTONE = null;

        @CheckForNull
        volatile Waiter next;

        @CheckForNull
        volatile Thread thread;

        static {
            TOMBSTONE = new Waiter(false);
        }

        Waiter() {
            AbstractFuture.access$200().putThread(this, Thread.currentThread());
        }

        Waiter(boolean r1) {
        }

        void setNext(@CheckForNull Waiter r2) {
            AbstractFuture.access$200().putNext(this, r2);
        }

        void unpark() {
            Thread r0 = this.thread;
            if (r0 == null) goto L6;
            this.thread = null;
            LockSupport.unpark(r0);
            return;
        }
    }

    static {
        boolean r0 = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));     // Catch: SecurityException -> L4
    L6:
        GENERATE_CANCELLATION_CAUSES = r0;
        log = new LazyLogger(AbstractFuture.class);
        Throwable r2 = null;
        Throwable r3 = null;
        C09001 r4 = null;
        AtomicHelper r1 = new UnsafeAtomicHelper(r4);     // Catch: Error -> L9 Throwable -> L11
    L19:
        ATOMIC_HELPER = r1;
        if (r3 == null) goto L22;
        log.get().log(Level.SEVERE, "UnsafeAtomicHelper is broken!", r2);
        log.get().log(Level.SEVERE, "SafeAtomicHelper is broken!", r3);
    L22:
        NULL = new Object();
        return;
    L11:
        e = move-exception;
        r2 = e;
        r1 = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));     // Catch: Throwable -> L15 Exception -> L17
    L15:
        e = move-exception;
        r3 = e;
        r1 = new SynchronizedHelper(r4);
    L5:
        r0 = false;
        goto L6
    }

    protected AbstractFuture() {
    }

    static /* synthetic */ AtomicHelper access$200() {
        return ATOMIC_HELPER;
    }

    static /* synthetic */ Object access$300(AbstractFuture r1) {
        return r1.value;
    }

    static /* synthetic */ Object access$302(AbstractFuture r0, Object r1) {
        r0.value = r1;
        return r1;
    }

    static /* synthetic */ Object access$400(ListenableFuture r1) {
        return getFutureValue(r1);
    }

    static /* synthetic */ void access$500(AbstractFuture r0, boolean r1) {
        complete(r0, r1);
    }

    static /* synthetic */ Listener access$700(AbstractFuture r1) {
        return r1.listeners;
    }

    static /* synthetic */ Listener access$702(AbstractFuture r0, Listener r1) {
        r0.listeners = r1;
        return r1;
    }

    static /* synthetic */ Waiter access$800(AbstractFuture r1) {
        return r1.waiters;
    }

    static /* synthetic */ Waiter access$802(AbstractFuture r0, Waiter r1) {
        r0.waiters = r1;
        return r1;
    }

    private void addDoneString(StringBuilder r5) {
        Object r1 = getUninterruptibly(this);     // Catch: Exception -> L5 CancellationException -> L7 ExecutionException -> L9
        r5.append("SUCCESS, result=[");     // Catch: Exception -> L5 CancellationException -> L7 ExecutionException -> L9
        appendResultObject(r5, r1);     // Catch: Exception -> L5 CancellationException -> L7 ExecutionException -> L9
        r5.append("]");     // Catch: Exception -> L5 CancellationException -> L7 ExecutionException -> L9
        goto L8
    L15:
        return;
    L9:
        e = move-exception;
        r5.append("FAILURE, cause=[").append(e.getCause()).append("]");
        return;
    L5:
        e = move-exception;
        r5.append("UNKNOWN, cause=[").append(e.getClass()).append(" thrown from get()]");
        return;
    L8:
        r5.append("CANCELLED");
    }

    private void addPendingString(StringBuilder r7) {
        int r0 = r7.length();
        r7.append("PENDING");
        Object r1 = this.value;
        if ((r1 instanceof SetFuture) == false) goto L17;
        r7.append(", setFuture=[");
        appendUserObject(r7, ((SetFuture) r1).future);
        r7.append("]");
    L14:
        if (isDone() == false) goto L19;
        r7.delete(r0, r7.length());
        addDoneString(r7);
        return;
    L19:
        return;
    L17:
        String r2 = Strings.emptyToNull(pendingToString());     // Catch: StackOverflowError -> L7 Throwable -> L9
    L11:
        if (r2 == null) goto L14;
        r7.append(", info=[").append(r2).append("]");
    L9:
        e = move-exception;
        r2 = "Exception thrown from implementation: " + e.getClass();
        goto L11
    }

    private void appendResultObject(StringBuilder r3, @CheckForNull Object r4) {
        if (r4 != null) goto L4;
        r3.append("null");
        return;
    L4:
        if (r4 != this) goto L6;
        r3.append("this future");
        return;
    L6:
        r3.append(r4.getClass().getName()).append("@").append(Integer.toHexString(System.identityHashCode(r4)));
    }

    private void appendUserObject(StringBuilder r4, @CheckForNull Object r5) {
        if (r5 != this) goto L4;
        r4.append("this future");     // Catch: StackOverflowError -> L6 Throwable -> L8
    L11:
        return;
    L4:
        r4.append(r5);     // Catch: StackOverflowError -> L6 Throwable -> L8
    L8:
        e = move-exception;
        r4.append("Exception thrown from implementation: ").append(e.getClass());
    }

    private static CancellationException cancellationExceptionWithCause(String r1, @CheckForNull Throwable r2) {
        CancellationException r0 = new CancellationException(r1);
        r0.initCause(r2);
        return r0;
    }

    @CheckForNull
    private Listener clearListeners(@CheckForNull Listener r4) {
        Listener r0 = ATOMIC_HELPER.gasListeners(this, Listener.TOMBSTONE);
        Listener r1 = r4;
    L3:
        if (r0 == null) goto L5;
        Listener r2 = r0;
        r0 = r0.next;
        r2.next = r1;
        r1 = r2;
        goto L3
    L5:
        return r1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void complete(AbstractFuture<?> r7, boolean r8) {
        AbstractFuture r0 = r7;
        Listener r1 = null;
    L3:
        r0.releaseWaiters();
        if (r8 == false) goto L6;
        r0.interruptTask();
        r8 = false;
    L6:
        r0.afterDone();
        r1 = r0.clearListeners(r1);
    L7:
        if (r1 == null) goto L18;
        Listener r2 = r1;
        r1 = r1.next;
        Runnable r3 = (Runnable) Objects.requireNonNull(r2.task);
        if ((r3 instanceof SetFuture) == true) goto L10;
        executeListener(r3, (Executor) Objects.requireNonNull(r2.executor));
        goto L7
    L10:
        SetFuture r4 = (SetFuture) r3;
        r0 = r4.owner;
        if (r0.value != r4) goto L7;
        Object r5 = getFutureValue(r4.future);
        if (ATOMIC_HELPER.casValue(r0, r4, r5) == false) goto L7;
    }

    private static void executeListener(Runnable r5, Executor r6) {
        r6.execute(r5);     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        log.get().log(Level.SEVERE, "RuntimeException while executing runnable " + r5 + " with executor " + r6, e);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @ParametricNullness
    private V getDoneValue(Object r3) throws ExecutionException {
        if ((r3 instanceof Cancellation) == true) goto L15;
        if ((r3 instanceof Failure) == true) goto L13;
        if (r3 != NULL) goto L11;
        return (V) NullnessCasts.uncheckedNull();
    L11:
        return r3;
    L13:
        throw new ExecutionException(((Failure) r3).exception);
    L15:
        throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) r3).cause);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Object getFutureValue(ListenableFuture<?> r7) {
        if ((r7 instanceof Trusted) == false) goto L16;
        Object r0 = ((AbstractFuture) r7).value;
        if ((r0 instanceof Cancellation) == false) goto L14;
        Cancellation r1 = (Cancellation) r0;
        if (r1.wasInterrupted == false) goto L14;
        if (r1.cause == null) goto L11;
        Cancellation r3 = new Cancellation(false, r1.cause);
    L12:
        r0 = r3;
        goto L14
    L11:
        r3 = Cancellation.CAUSELESS_CANCELLED;
    L14:
        return Objects.requireNonNull(r0);
    L16:
        if ((r7 instanceof InternalFutureFailureAccess) == false) goto L21;
        Throwable r2 = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) r7);
        if (r2 == null) goto L21;
        return new Failure(r2);
    L21:
        boolean r4 = r7.isCancelled();
        if (((!GENERATE_CANCELLATION_CAUSES) & r4) == true) goto L24;
        Object r5 = getUninterruptibly(r7);     // Catch: Error -> L34 Exception -> L36 CancellationException -> L39 ExecutionException -> L45
        if (r4 == true) goto L28;
        if (r5 != null) goto L33;
        return NULL;
    L33:
        return r5;
    L28:
        return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + r7));
    L34:
        e = e;
    L38:
        return new Failure(e);
    L39:
        e = move-exception;
        if (r4 == true) goto L44;
        return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + r7, e));
    L44:
        return new Cancellation(false, e);
    L45:
        e = move-exception;
        if (r4 == false) goto L50;
        return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + r7, e));
    L50:
        return new Failure(e.getCause());
    L36:
        e = e;
        goto L38
    L24:
        return Objects.requireNonNull(Cancellation.CAUSELESS_CANCELLED);
    }

    @ParametricNullness
    private static <V> V getUninterruptibly(Future<V> r3) throws ExecutionException {
        boolean r0 = false;
    L13:
        V r1 = r3.get();     // Catch: Throwable -> L7 InterruptedException -> L11
        goto L12
    L4:
        if (r0 == false) goto L6;
        Thread.currentThread().interrupt();
    L6:
        return r1;
    L7:
        th = move-exception;
        if (r0 == false) goto L10;
        Thread.currentThread().interrupt();
    L10:
        throw th;
    L12:
        r0 = true;
        goto L13
    }

    private void releaseWaiters() {
        Waiter r1 = ATOMIC_HELPER.gasWaiters(this, Waiter.TOMBSTONE);
    L3:
        if (r1 == null) goto L5;
        r1.unpark();
        r1 = r1.next;
        goto L3
    }

    private void removeWaiter(Waiter r5) {
        r5.thread = null;
    L3:
        Waiter r0 = null;
        Waiter r1 = this.waiters;
        if (r1 == Waiter.TOMBSTONE) goto L5;
    L6:
        if (r1 == null) goto L18;
        Waiter r2 = r1.next;
        if (r1.thread == null) goto L10;
        r0 = r1;
    L17:
        r1 = r2;
        goto L6
    L10:
        if (r0 == null) goto L15;
        r0.next = r2;
        if (r0.thread != null) goto L17;
    L15:
        if (ATOMIC_HELPER.casWaiters(this, r1, r2) == true) goto L17;
    L18:
        return;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable r4, Executor r5) {
        Preconditions.checkNotNull(r4, "Runnable was null.");
        Preconditions.checkNotNull(r5, "Executor was null.");
        if (isDone() == true) goto L12;
        Listener r0 = this.listeners;
        if (r0 == Listener.TOMBSTONE) goto L12;
        Listener r1 = new Listener(r4, r5);
    L7:
        r1.next = r0;
        if (ATOMIC_HELPER.casListeners(this, r0, r1) == true) goto L9;
        r0 = this.listeners;
        if (r0 != Listener.TOMBSTONE) goto L7;
    L9:
        return;
    L12:
        executeListener(r4, r5);
    }

    protected void afterDone() {
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean r11) {
        Object r0 = this.value;
        boolean r1 = false;
        if (r0 != null) goto L5;
        boolean r4 = true;
    L7:
        if ((r4 | (r0 instanceof SetFuture)) == true) goto L9;
    L34:
        return r1;
    L9:
        if (GENERATE_CANCELLATION_CAUSES == false) goto L11;
        Object r5 = new Cancellation(r11, new CancellationException("Future.cancel() was called."));
    L15:
        AbstractFuture<V> r6 = this;
    L17:
        if (ATOMIC_HELPER.casValue(r6, r0, r5) == true) goto L18;
        r0 = r6.value;
        if ((r0 instanceof SetFuture) == true) goto L17;
    L18:
        r1 = true;
        complete(r6, r11);
        if ((r0 instanceof SetFuture) == false) goto L34;
        ListenableFuture<? extends V> r7 = ((SetFuture) r0).future;
        if ((r7 instanceof Trusted) == false) goto L30;
        AbstractFuture<V> r8 = (AbstractFuture) r7;
        r0 = r8.value;
        if (r0 != null) goto L25;
        boolean r9 = true;
    L27:
        if ((r9 | (r0 instanceof SetFuture)) == false) goto L34;
        r6 = r8;
        goto L17
    L25:
        r9 = false;
        goto L27
    L30:
        r7.cancel(r11);
        goto L34
    L11:
        if (r11 == false) goto L13;
        Cancellation r10 = Cancellation.CAUSELESS_INTERRUPTED;
    L14:
        r5 = Objects.requireNonNull(r10);
        goto L15
    L13:
        r10 = Cancellation.CAUSELESS_CANCELLED;
        goto L14
    L5:
        r4 = false;
        goto L7
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get() throws InterruptedException, ExecutionException {
        if (Thread.interrupted() == true) goto L34;
        Object r0 = this.value;
        if (r0 == null) goto L7;
        boolean r3 = true;
    L9:
        if ((r3 & (!(r0 instanceof SetFuture))) == true) goto L11;
        Waiter r4 = this.waiters;
        if (r4 == Waiter.TOMBSTONE) goto L32;
        Waiter r5 = new Waiter();
    L15:
        r5.setNext(r4);
        if (ATOMIC_HELPER.casWaiters(this, r4, r5) == true) goto L17;
        r4 = this.waiters;
        if (r4 != Waiter.TOMBSTONE) goto L15;
    L17:
        LockSupport.park(this);
        if (Thread.interrupted() == true) goto L27;
        Object r1 = this.value;
        if (r1 == null) goto L22;
        boolean r6 = true;
    L24:
        if ((r6 & (!(r1 instanceof SetFuture))) == false) goto L17;
        return getDoneValue(r1);
    L22:
        r6 = false;
        goto L24
    L27:
        removeWaiter(r5);
        throw new InterruptedException();
    L32:
        return getDoneValue(Objects.requireNonNull(this.value));
    L11:
        return getDoneValue(r0);
    L7:
        r3 = false;
        goto L9
    L34:
        throw new InterruptedException();
    }

    @Override // java.util.concurrent.Future
    @ParametricNullness
    public V get(long r28, TimeUnit r30) throws InterruptedException, TimeoutException, ExecutionException {
        long r6 = r30.toNanos(r28);
        if (Thread.interrupted() == true) goto L87;
        Object r8 = this.value;
        if (r8 == null) goto L7;
        boolean r11 = true;
    L9:
        if ((r11 & (!(r8 instanceof SetFuture))) == true) goto L11;
        long r12 = 0;
        if (r6 <= 0) goto L15;
        long r13 = System.nanoTime() + r6;
    L17:
        if (r6 < 1000) goto L44;
        Waiter r9 = this.waiters;
        boolean r18 = true;
        if (r9 == Waiter.TOMBSTONE) goto L43;
        Waiter r10 = new Waiter();
    L21:
        r10.setNext(r9);
        long r19 = r12;
        if (ATOMIC_HELPER.casWaiters(this, r9, r10) == true) goto L23;
        r9 = this.waiters;
        if (r9 == Waiter.TOMBSTONE) goto L43;
        r12 = r19;
    L23:
        OverflowAvoidingLockSupport.parkNanos(this, r6);
        if (Thread.interrupted() == true) goto L36;
        Object r14 = this.value;
        if (r14 == null) goto L28;
        boolean r15 = true;
    L30:
        if ((r15 & (!(r14 instanceof SetFuture))) == true) goto L32;
        r6 = r13 - System.nanoTime();
        if (r6 >= 1000) goto L23;
        removeWaiter(r10);
    L46:
        if (r6 <= r19) goto L60;
        Object r16 = this.value;
        if (r16 == null) goto L50;
        boolean r17 = r18;
    L52:
        if ((r17 & (!(r16 instanceof SetFuture))) == true) goto L54;
        if (Thread.interrupted() == true) goto L59;
        r6 = r13 - System.nanoTime();
        goto L46
    L59:
        throw new InterruptedException();
    L54:
        return getDoneValue(r16);
    L50:
        r17 = false;
        goto L52
    L60:
        String r20 = toString();
        String r110 = r30.toString().toLowerCase(Locale.ROOT);
        String r0 = "Waited " + r28 + " " + r30.toString().toLowerCase(Locale.ROOT);
        if ((r6 + 1000) >= r19) goto L81;
        String r1 = r0 + " (plus ";
        long r2 = -r6;
        long r4 = r30.convert(r2, TimeUnit.NANOSECONDS);
        long r3 = r2 - r30.toNanos(r4);
        if (r4 != r19) goto L65;
    L68:
        boolean r111 = r18;
    L70:
        if (r4 <= r19) goto L76;
        String r5 = r1 + r4 + " " + r110;
        if (r111 == false) goto L75;
        r5 = r5 + ",";
    L75:
        r1 = r5 + " ";
    L76:
        if (r111 == false) goto L78;
        r1 = r1 + r3 + " nanoseconds ";
    L78:
        r0 = r1 + "delay)";
        goto L81
    L65:
        if (r3 > 1000) goto L68;
        r111 = false;
    L81:
        if (isDone() == false) goto L85;
        throw new TimeoutException(r0 + " but future completed as timeout expired");
    L85:
        throw new TimeoutException(r0 + " for " + r20);
    L32:
        return getDoneValue(r14);
    L28:
        r15 = false;
        goto L30
    L36:
        removeWaiter(r10);
        throw new InterruptedException();
    L43:
        return getDoneValue(Objects.requireNonNull(this.value));
    L44:
        r18 = true;
        r19 = 0;
        goto L46
    L15:
        r13 = 0;
        goto L17
    L11:
        return getDoneValue(r8);
    L7:
        r11 = false;
        goto L9
    L87:
        throw new InterruptedException();
    }

    protected void interruptTask() {
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        Object r0 = this.value;
        if (r0 == null) goto L5;
        boolean r2 = true;
    L7:
        return (true ^ (r0 instanceof SetFuture)) & r2;
    L5:
        r2 = false;
        goto L7
    }

    final void maybePropagateCancellationTo(@CheckForNull Future<?> r3) {
        if (r3 == null) goto L4;
        boolean r0 = true;
    L6:
        if ((r0 & isCancelled()) == false) goto L9;
        r3.cancel(wasInterrupted());
        return;
    L9:
        return;
    L4:
        r0 = false;
        goto L6
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    protected String pendingToString() {
        if ((this instanceof ScheduledFuture) == true) goto L5;
        return null;
    L5:
        return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
    }

    protected boolean set(@ParametricNullness V r4) {
        if (r4 != null) goto L4;
        Object r0 = NULL;
    L6:
        if (ATOMIC_HELPER.casValue(this, null, r0) == false) goto L9;
        complete(this, false);
        return true;
    L9:
        return false;
    L4:
        r0 = r4;
        goto L6
    }

    protected boolean setException(Throwable r4) {
        Failure r0 = new Failure((Throwable) Preconditions.checkNotNull(r4));
        if (ATOMIC_HELPER.casValue(this, null, r0) == false) goto L6;
        complete(this, false);
        return true;
    L6:
        return false;
    }

    protected boolean setFuture(ListenableFuture<? extends V> r7) {
        Preconditions.checkNotNull(r7);
        Object r0 = this.value;
        if (r0 != null) goto L26;
        if (r7.isDone() == false) goto L11;
        Object r2 = getFutureValue(r7);
        if (ATOMIC_HELPER.casValue(this, null, r2) == false) goto L10;
        complete(this, false);
        return true;
    L10:
        return false;
    L11:
        SetFuture r3 = new SetFuture(this, r7);
        if (ATOMIC_HELPER.casValue(this, null, r3) == true) goto L29;
        r0 = this.value;
        goto L26
    L29:
        r7.addListener(r3, DirectExecutor.INSTANCE);     // Catch: Throwable -> L15
    L23:
        return true;
    L15:
        th = move-exception;
        Failure r4 = new Failure(th);     // Catch: Error -> L18 Throwable -> L20
    L22:
        ATOMIC_HELPER.casValue(this, r3, r4);
    L21:
        r4 = Failure.FALLBACK_INSTANCE;
    L26:
        if ((r0 instanceof Cancellation) == false) goto L28;
        r7.cancel(((Cancellation) r0).wasInterrupted);
    L28:
        return false;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        if (getClass().getName().startsWith("com.google.common.util.concurrent.") == false) goto L5;
        r0.append(getClass().getSimpleName());
    L6:
        r0.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
        if (isCancelled() == false) goto L10;
        r0.append("CANCELLED");
    L14:
        return r0.append("]").toString();
    L10:
        if (isDone() == false) goto L12;
        addDoneString(r0);
        goto L14
    L12:
        addPendingString(r0);
        goto L14
    L5:
        r0.append(getClass().getName());
        goto L6
    }

    @Override // com.google.common.util.concurrent.internal.InternalFutureFailureAccess
    @CheckForNull
    protected final Throwable tryInternalFastPathGetFailure() {
        if ((this instanceof Trusted) == false) goto L8;
        Object r0 = this.value;
        if ((r0 instanceof Failure) == true) goto L7;
        return null;
    L7:
        return ((Failure) r0).exception;
    L8:
        return null;
    }

    protected final boolean wasInterrupted() {
        Object r0 = this.value;
        if ((r0 instanceof Cancellation) == true) goto L5;
    L7:
        return false;
    L5:
        if (((Cancellation) r0).wasInterrupted == false) goto L7;
        return true;
    }
}
