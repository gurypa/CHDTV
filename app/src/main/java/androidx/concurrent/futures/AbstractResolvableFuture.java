package androidx.concurrent.futures;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.Locale;
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
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractResolvableFuture<V> implements ListenableFuture<V> {
    static final AtomicHelper ATOMIC_HELPER = null;
    static final boolean GENERATE_CANCELLATION_CAUSES = false;
    private static final Object NULL = null;
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    private static final Logger log = null;
    volatile Listener listeners;
    volatile Object value;
    volatile Waiter waiters;

    /* JADX INFO: renamed from: androidx.concurrent.futures.AbstractResolvableFuture$1 */
    static /* synthetic */ class C00351 {
    }

    private static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        /* synthetic */ AtomicHelper(C00351 r1) {
            this();
        }

        abstract boolean casListeners(AbstractResolvableFuture<?> r1, Listener r2, Listener r3);

        abstract boolean casValue(AbstractResolvableFuture<?> r1, Object r2, Object r3);

        abstract boolean casWaiters(AbstractResolvableFuture<?> r1, Waiter r2, Waiter r3);

        abstract void putNext(Waiter r1, Waiter r2);

        abstract void putThread(Waiter r1, Thread r2);
    }

    private static final class Cancellation {
        static final Cancellation CAUSELESS_CANCELLED = null;
        static final Cancellation CAUSELESS_INTERRUPTED = null;
        final Throwable cause;
        final boolean wasInterrupted;

        static {
            if (AbstractResolvableFuture.GENERATE_CANCELLATION_CAUSES == false) goto L5;
            CAUSELESS_CANCELLED = null;
            CAUSELESS_INTERRUPTED = null;
            return;
        L5:
            CAUSELESS_CANCELLED = new Cancellation(false, null);
            CAUSELESS_INTERRUPTED = new Cancellation(true, null);
        }

        Cancellation(boolean r1, Throwable r2) {
            this.wasInterrupted = r1;
            this.cause = r2;
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = null;
        final Throwable exception;

        static {
            final String r2 = "Failure occurred while trying to finish a future.";
            FALLBACK_INSTANCE = new Failure(new C00361(r2));
        }

        Failure(Throwable r2) {
            this.exception = (Throwable) AbstractResolvableFuture.checkNotNull(r2);
        }
    }

    private static final class Listener {
        static final Listener TOMBSTONE = null;
        final Executor executor;
        Listener next;
        final Runnable task;

        static {
            TOMBSTONE = new Listener(null, null);
        }

        Listener(Runnable r1, Executor r2) {
            this.task = r1;
            this.executor = r2;
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AbstractResolvableFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractResolvableFuture, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<AbstractResolvableFuture, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> r2, AtomicReferenceFieldUpdater<Waiter, Waiter> r3, AtomicReferenceFieldUpdater<AbstractResolvableFuture, Waiter> r4, AtomicReferenceFieldUpdater<AbstractResolvableFuture, Listener> r5, AtomicReferenceFieldUpdater<AbstractResolvableFuture, Object> r6) {
            super(null);
            this.waiterThreadUpdater = r2;
            this.waiterNextUpdater = r3;
            this.waitersUpdater = r4;
            this.listenersUpdater = r5;
            this.valueUpdater = r6;
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        boolean casListeners(AbstractResolvableFuture<?> r2, Listener r3, Listener r4) {
            return C0037xc40028dd.m4m(this.listenersUpdater, r2, r3, r4);
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        boolean casValue(AbstractResolvableFuture<?> r2, Object r3, Object r4) {
            return C0037xc40028dd.m4m(this.valueUpdater, r2, r3, r4);
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        boolean casWaiters(AbstractResolvableFuture<?> r2, Waiter r3, Waiter r4) {
            return C0037xc40028dd.m4m(this.waitersUpdater, r2, r3, r4);
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        void putNext(Waiter r2, Waiter r3) {
            this.waiterNextUpdater.lazySet(r2, r3);
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        void putThread(Waiter r2, Thread r3) {
            this.waiterThreadUpdater.lazySet(r2, r3);
        }
    }

    private static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractResolvableFuture<V> owner;

        SetFuture(AbstractResolvableFuture<V> r1, ListenableFuture<? extends V> r2) {
            this.owner = r1;
            this.future = r2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.owner.value == this) goto L5;
            return;
        L5:
            Object r0 = AbstractResolvableFuture.getFutureValue(this.future);
            if (AbstractResolvableFuture.ATOMIC_HELPER.casValue(this.owner, this, r0) == false) goto L9;
            AbstractResolvableFuture.complete(this.owner);
            return;
        }
    }

    private static final class SynchronizedHelper extends AtomicHelper {
        SynchronizedHelper() {
            super(null);
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        boolean casListeners(AbstractResolvableFuture<?> r2, Listener r3, Listener r4) {
            monitor-enter(r2);
        L12:
            th = move-exception;
            throw th;
        L4:
            if (r2.listeners != r3) goto L9;
            r2.listeners = r4;     // Catch: Throwable -> L12
            monitor-exit(r2);     // Catch: Throwable -> L12
            return true;
        L9:
            monitor-exit(r2);     // Catch: Throwable -> L12
            return false;
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        boolean casValue(AbstractResolvableFuture<?> r2, Object r3, Object r4) {
            monitor-enter(r2);
        L12:
            th = move-exception;
            throw th;
        L4:
            if (r2.value != r3) goto L9;
            r2.value = r4;     // Catch: Throwable -> L12
            monitor-exit(r2);     // Catch: Throwable -> L12
            return true;
        L9:
            monitor-exit(r2);     // Catch: Throwable -> L12
            return false;
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        boolean casWaiters(AbstractResolvableFuture<?> r2, Waiter r3, Waiter r4) {
            monitor-enter(r2);
        L12:
            th = move-exception;
            throw th;
        L4:
            if (r2.waiters != r3) goto L9;
            r2.waiters = r4;     // Catch: Throwable -> L12
            monitor-exit(r2);     // Catch: Throwable -> L12
            return true;
        L9:
            monitor-exit(r2);     // Catch: Throwable -> L12
            return false;
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        void putNext(Waiter r1, Waiter r2) {
            r1.next = r2;
        }

        @Override // androidx.concurrent.futures.AbstractResolvableFuture.AtomicHelper
        void putThread(Waiter r1, Thread r2) {
            r1.thread = r2;
        }
    }

    private static final class Waiter {
        static final Waiter TOMBSTONE = null;
        volatile Waiter next;
        volatile Thread thread;

        static {
            TOMBSTONE = new Waiter(false);
        }

        Waiter() {
            AbstractResolvableFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        Waiter(boolean r1) {
        }

        void setNext(Waiter r2) {
            AbstractResolvableFuture.ATOMIC_HELPER.putNext(this, r2);
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
        GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        log = Logger.getLogger(AbstractResolvableFuture.class.getName());
        Throwable r1 = null;
        AtomicHelper r2 = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractResolvableFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractResolvableFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractResolvableFuture.class, Object.class, "value"));     // Catch: Throwable -> L5
    L7:
        ATOMIC_HELPER = r2;
        if (r1 == null) goto L10;
        log.log(Level.SEVERE, "SafeAtomicHelper is broken!", r1);
    L10:
        NULL = new Object();
        return;
    L5:
        th = move-exception;
        r1 = th;
        r2 = new SynchronizedHelper();
        goto L7
    }

    protected AbstractResolvableFuture() {
    }

    private void addDoneString(StringBuilder r5) {
        Object r1 = getUninterruptibly(this);     // Catch: RuntimeException -> L5 CancellationException -> L7 ExecutionException -> L9
        r5.append("SUCCESS, result=[").append(userObjectToString(r1)).append("]");     // Catch: RuntimeException -> L5 CancellationException -> L7 ExecutionException -> L9
        goto L8
    L15:
        return;
    L5:
        e = move-exception;
        r5.append("UNKNOWN, cause=[").append(e.getClass()).append(" thrown from get()]");
        return;
    L9:
        e = move-exception;
        r5.append("FAILURE, cause=[").append(e.getCause()).append("]");
        return;
    L8:
        r5.append("CANCELLED");
    }

    private static CancellationException cancellationExceptionWithCause(String r1, Throwable r2) {
        CancellationException r0 = new CancellationException(r1);
        r0.initCause(r2);
        return r0;
    }

    static <T> T checkNotNull(T r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new NullPointerException();
    }

    private Listener clearListeners(Listener r4) {
    L2:
        Listener r0 = this.listeners;
        if (ATOMIC_HELPER.casListeners(this, r0, Listener.TOMBSTONE) == false) goto L2;
        Listener r1 = r4;
    L5:
        if (r0 == null) goto L7;
        Listener r2 = r0;
        r0 = r0.next;
        r2.next = r1;
        r1 = r2;
        goto L5
    L7:
        return r1;
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
    static void complete(AbstractResolvableFuture<V> r6) {
        Listener r0 = null;
    L3:
        r6.releaseWaiters();
        r6.afterDone();
        r0 = r6.clearListeners(r0);
    L4:
        if (r0 == null) goto L15;
        Listener r1 = r0;
        r0 = r0.next;
        Runnable r2 = r1.task;
        if ((r2 instanceof SetFuture) == true) goto L7;
        executeListener(r2, r1.executor);
        goto L4
    L7:
        SetFuture r3 = (SetFuture) r2;
        r6 = r3.owner;
        if (r6.value != r3) goto L4;
        Object r4 = getFutureValue(r3.future);
        if (ATOMIC_HELPER.casValue(r6, r3, r4) == false) goto L4;
    }

    private static void executeListener(Runnable r5, Executor r6) {
        r6.execute(r5);     // Catch: RuntimeException -> L4
        return;
    L4:
        e = move-exception;
        log.log(Level.SEVERE, "RuntimeException while executing runnable " + r5 + " with executor " + r6, e);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private V getDoneValue(Object r3) throws ExecutionException {
        if ((r3 instanceof Cancellation) == true) goto L15;
        if ((r3 instanceof Failure) == true) goto L13;
        if (r3 != NULL) goto L11;
        return null;
    L11:
        return r3;
    L13:
        throw new ExecutionException(((Failure) r3).exception);
    L15:
        throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) r3).cause);
    }

    static Object getFutureValue(ListenableFuture<?> r6) {
        if ((r6 instanceof AbstractResolvableFuture) == false) goto L14;
        Object r0 = ((AbstractResolvableFuture) r6).value;
        if ((r0 instanceof Cancellation) == false) goto L38;
        Cancellation r2 = (Cancellation) r0;
        if (r2.wasInterrupted == true) goto L9;
        return r0;
    L9:
        if (r2.cause == null) goto L11;
        Cancellation r3 = new Cancellation(false, r2.cause);
    L13:
        return r3;
    L11:
        r3 = Cancellation.CAUSELESS_CANCELLED;
        goto L13
    L38:
        return r0;
    L14:
        boolean r1 = r6.isCancelled();
        if (((!GENERATE_CANCELLATION_CAUSES) & r1) == true) goto L17;
        Object r4 = getUninterruptibly(r6);     // Catch: Throwable -> L24 CancellationException -> L27 ExecutionException -> L33
        if (r4 != null) goto L23;
    L40:
        return NULL;
    L23:
        return r4;
    L27:
        e = move-exception;
        if (r1 == true) goto L32;
        return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + r6, e));
    L32:
        return new Cancellation(false, e);
    L33:
        e = move-exception;
        return new Failure(e.getCause());
    L24:
        th = move-exception;
        return new Failure(th);
    L17:
        return Cancellation.CAUSELESS_CANCELLED;
    }

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
    L2:
        Waiter r0 = this.waiters;
        if (ATOMIC_HELPER.casWaiters(this, r0, Waiter.TOMBSTONE) == false) goto L2;
        Waiter r1 = r0;
    L5:
        if (r1 == null) goto L7;
        r1.unpark();
        r1 = r1.next;
        goto L5
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

    private String userObjectToString(Object r2) {
        if (r2 != this) goto L6;
        return "this future";
    L6:
        return String.valueOf(r2);
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public final void addListener(Runnable r4, Executor r5) {
        checkNotNull(r4);
        checkNotNull(r5);
        Listener r0 = this.listeners;
        if (r0 == Listener.TOMBSTONE) goto L10;
        Listener r1 = new Listener(r4, r5);
    L5:
        r1.next = r0;
        if (ATOMIC_HELPER.casListeners(this, r0, r1) == true) goto L7;
        r0 = this.listeners;
        if (r0 != Listener.TOMBSTONE) goto L5;
    L7:
        return;
    L10:
        executeListener(r4, r5);
    }

    protected void afterDone() {
    }

    @Override // java.util.concurrent.Future
    public final boolean cancel(boolean r11) {
        Object r0 = this.value;
        boolean r1 = false;
        if (r0 != null) goto L5;
        boolean r4 = true;
    L7:
        if ((r4 | (r0 instanceof SetFuture)) == true) goto L9;
    L36:
        return r1;
    L9:
        if (GENERATE_CANCELLATION_CAUSES == false) goto L11;
        Cancellation r5 = new Cancellation(r11, new CancellationException("Future.cancel() was called."));
    L14:
        AbstractResolvableFuture<V> r6 = this;
    L16:
        if (ATOMIC_HELPER.casValue(r6, r0, r5) == true) goto L17;
        r0 = r6.value;
        if ((r0 instanceof SetFuture) == true) goto L16;
    L17:
        r1 = true;
        if (r11 == false) goto L20;
        r6.interruptTask();
    L20:
        complete(r6);
        if ((r0 instanceof SetFuture) == false) goto L36;
        ListenableFuture<? extends V> r7 = ((SetFuture) r0).future;
        if ((r7 instanceof AbstractResolvableFuture) == false) goto L32;
        AbstractResolvableFuture<V> r8 = (AbstractResolvableFuture) r7;
        r0 = r8.value;
        if (r0 != null) goto L27;
        boolean r9 = true;
    L29:
        if ((r9 | (r0 instanceof SetFuture)) == false) goto L36;
        r6 = r8;
        goto L16
    L27:
        r9 = false;
        goto L29
    L32:
        r7.cancel(r11);
        goto L36
    L11:
        if (r11 == false) goto L13;
        r5 = Cancellation.CAUSELESS_INTERRUPTED;
        goto L14
    L13:
        r5 = Cancellation.CAUSELESS_CANCELLED;
        goto L14
    L5:
        r4 = false;
        goto L7
    }

    @Override // java.util.concurrent.Future
    public final V get() throws InterruptedException, ExecutionException {
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
        return getDoneValue(this.value);
    L11:
        return getDoneValue(r0);
    L7:
        r3 = false;
        goto L9
    L34:
        throw new InterruptedException();
    }

    @Override // java.util.concurrent.Future
    public final V get(long r28, TimeUnit r30) throws InterruptedException, TimeoutException, ExecutionException {
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
        LockSupport.parkNanos(this, r6);
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
        return getDoneValue(this.value);
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
    public final boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    public final boolean isDone() {
        Object r0 = this.value;
        if (r0 == null) goto L5;
        boolean r2 = true;
    L7:
        return (true ^ (r0 instanceof SetFuture)) & r2;
    L5:
        r2 = false;
        goto L7
    }

    final void maybePropagateCancellationTo(Future<?> r3) {
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
    protected String pendingToString() {
        Object r0 = this.value;
        if ((r0 instanceof SetFuture) == false) goto L7;
        return "setFuture=[" + userObjectToString(((SetFuture) r0).future) + "]";
    L7:
        if ((this instanceof ScheduledFuture) == true) goto L9;
        return null;
    L9:
        return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
    }

    protected boolean set(V r4) {
        if (r4 != null) goto L4;
        Object r0 = NULL;
    L6:
        if (ATOMIC_HELPER.casValue(this, null, r0) == false) goto L9;
        complete(this);
        return true;
    L9:
        return false;
    L4:
        r0 = r4;
        goto L6
    }

    protected boolean setException(Throwable r4) {
        Failure r0 = new Failure((Throwable) checkNotNull(r4));
        if (ATOMIC_HELPER.casValue(this, null, r0) == false) goto L6;
        complete(this);
        return true;
    L6:
        return false;
    }

    protected boolean setFuture(ListenableFuture<? extends V> r7) {
        checkNotNull(r7);
        Object r0 = this.value;
        if (r0 != null) goto L24;
        if (r7.isDone() == false) goto L11;
        Object r2 = getFutureValue(r7);
        if (ATOMIC_HELPER.casValue(this, null, r2) == false) goto L10;
        complete(this);
        return true;
    L10:
        return false;
    L11:
        SetFuture r3 = new SetFuture(this, r7);
        if (ATOMIC_HELPER.casValue(this, null, r3) == true) goto L27;
        r0 = this.value;
        goto L24
    L27:
        r7.addListener(r3, DirectExecutor.INSTANCE);     // Catch: Throwable -> L15
    L21:
        return true;
    L15:
        th = move-exception;
        Failure r4 = new Failure(th);     // Catch: Throwable -> L18
    L20:
        ATOMIC_HELPER.casValue(this, r3, r4);
    L19:
        r4 = Failure.FALLBACK_INSTANCE;
    L24:
        if ((r0 instanceof Cancellation) == false) goto L26;
        r7.cancel(((Cancellation) r0).wasInterrupted);
    L26:
        return false;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append(super.toString()).append("[status=");
        if (isCancelled() == false) goto L6;
        r0.append("CANCELLED");
    L21:
        return r0.append("]").toString();
    L6:
        if (isDone() == false) goto L22;
        addDoneString(r0);
        goto L21
    L22:
        String r1 = pendingToString();     // Catch: RuntimeException -> L10
    L12:
        if (r1 == null) goto L17;
        if (r1.isEmpty() == true) goto L17;
        r0.append("PENDING, info=[").append(r1).append("]");
    L17:
        if (isDone() == false) goto L19;
        addDoneString(r0);
        goto L21
    L19:
        r0.append("PENDING");
    L10:
        e = move-exception;
        r1 = "Exception thrown from implementation: " + e.getClass();
        goto L12
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
