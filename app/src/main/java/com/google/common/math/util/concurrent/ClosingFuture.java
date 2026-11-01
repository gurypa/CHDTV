package com.google.common.util.concurrent;

import androidx.media3.exoplayer.mediacodec.C0305xa830b2f;
import com.google.common.base.Functions;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.errorprone.annotations.DoNotMock;
import java.io.Closeable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ClosingFuture.from(Futures.immediate*Future)")
@ElementTypesAreNonnullByDefault
public final class ClosingFuture<V> {
    private static final LazyLogger logger = null;
    private final CloseableList closeables;
    private final FluentFuture<V> future;
    private final AtomicReference<State> state;

    public interface AsyncClosingCallable<V> {
        ClosingFuture<V> call(DeferredCloser r1) throws Exception;
    }

    public interface AsyncClosingFunction<T, U> {
        ClosingFuture<U> apply(DeferredCloser r1, @ParametricNullness T r2) throws Exception;
    }

    private static final class CloseableList extends IdentityHashMap<AutoCloseable, Executor> implements Closeable {
        private volatile boolean closed;
        private final DeferredCloser closer;

        @CheckForNull
        private volatile CountDownLatch whenClosed;

        private CloseableList() {
            this.closer = new DeferredCloser(this);
        }

        /* synthetic */ CloseableList(CallableC09191 r1) {
            this();
        }

        static /* synthetic */ DeferredCloser access$300(CloseableList r1) {
            return r1.closer;
        }

        void add(@CheckForNull AutoCloseable r2, Executor r3) {
            Preconditions.checkNotNull(r3);
            if (r2 != null) goto L5;
            return;
        L5:
            monitor-enter(this);
        L14:
            th = move-exception;
            throw th;
        L7:
            if (this.closed == true) goto L11;
            put(r2, r3);     // Catch: Throwable -> L14
            monitor-exit(this);     // Catch: Throwable -> L14
            return;
        L11:
            monitor-exit(this);     // Catch: Throwable -> L14
            ClosingFuture.access$3200(r2, r3);
        }

        <V, U> FluentFuture<U> applyAsyncClosingFunction(AsyncClosingFunction<V, U> r5, @ParametricNullness V r6) throws Exception {
            CloseableList r0 = new CloseableList();
            ClosingFuture<U> r1 = r5.apply(r0.closer, r6);     // Catch: Throwable -> L6
            ClosingFuture.access$400(r1, r0);     // Catch: Throwable -> L6
            FluentFuture<U> r2 = ClosingFuture.access$000(r1);     // Catch: Throwable -> L6
            add(r0, MoreExecutors.directExecutor());
            return r2;
        L6:
            th = move-exception;
            add(r0, MoreExecutors.directExecutor());
            throw th;
        }

        /* JADX WARN: Multi-variable type inference failed */
        <V, U> ListenableFuture<U> applyClosingFunction(ClosingFunction<? super V, U> r4, @ParametricNullness V r5) throws Exception {
            CloseableList r0 = new CloseableList();
            ListenableFuture<U> r1 = Futures.immediateFuture(r4.apply(r0.closer, r5));     // Catch: Throwable -> L6
            add(r0, MoreExecutors.directExecutor());
            return r1;
        L6:
            th = move-exception;
            add(r0, MoreExecutors.directExecutor());
            throw th;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.closed == false) goto L5;
            return;
        L5:
            monitor-enter(this);
        L20:
            th = move-exception;
            throw th;
        L7:
            if (this.closed == false) goto L10;
            monitor-exit(this);     // Catch: Throwable -> L20
            return;
        L10:
            this.closed = true;     // Catch: Throwable -> L20
            monitor-exit(this);     // Catch: Throwable -> L20
            Iterator<Map.Entry<AutoCloseable, Executor>> r0 = entrySet().iterator();
        L14:
            if (r0.hasNext() == false) goto L16;
            Map.Entry<AutoCloseable, Executor> r1 = r0.next();
            ClosingFuture.access$3200(r1.getKey(), r1.getValue());
            goto L14
        L16:
            clear();
            if (this.whenClosed == null) goto L25;
            this.whenClosed.countDown();
            return;
        }

        CountDownLatch whenClosedCountDown() {
            boolean r1 = false;
            if (this.closed == true) goto L5;
            monitor-enter(this);
        L18:
            th = move-exception;
            throw th;
        L8:
            if (this.closed == false) goto L13;
            CountDownLatch r0 = new CountDownLatch(0);     // Catch: Throwable -> L18
            monitor-exit(this);     // Catch: Throwable -> L18
            return r0;
        L13:
            if (this.whenClosed != null) goto L15;
            r1 = true;
        L15:
            Preconditions.checkState(r1);     // Catch: Throwable -> L18
            CountDownLatch r2 = new CountDownLatch(1);     // Catch: Throwable -> L18
            this.whenClosed = r2;     // Catch: Throwable -> L18
            monitor-exit(this);     // Catch: Throwable -> L18
            return r2;
        L5:
            return new CountDownLatch(0);
        }
    }

    public interface ClosingCallable<V> {
        @ParametricNullness
        V call(DeferredCloser r1) throws Exception;
    }

    public interface ClosingFunction<T, U> {
        @ParametricNullness
        U apply(DeferredCloser r1, @ParametricNullness T r2) throws Exception;
    }

    @DoNotMock("Use ClosingFuture.whenAllSucceed() or .whenAllComplete() instead.")
    public static class Combiner {
        private final boolean allMustSucceed;
        private final CloseableList closeables;
        protected final ImmutableList<ClosingFuture<?>> inputs;

        public interface AsyncCombiningCallable<V> {
            ClosingFuture<V> call(DeferredCloser r1, Peeker r2) throws Exception;
        }

        public interface CombiningCallable<V> {
            @ParametricNullness
            V call(DeferredCloser r1, Peeker r2) throws Exception;
        }

        private Combiner(boolean r4, Iterable<? extends ClosingFuture<?>> r5) {
            this.closeables = new CloseableList(null);
            this.allMustSucceed = r4;
            this.inputs = ImmutableList.copyOf(r5);
            Iterator<? extends ClosingFuture<?>> r0 = r5.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ClosingFuture.access$400(r0.next(), this.closeables);
            goto L4
        }

        /* synthetic */ Combiner(boolean r1, Iterable r2, CallableC09191 r3) {
            this(r1, r2);
        }

        static /* synthetic */ CloseableList access$1300(Combiner r1) {
            return r1.closeables;
        }

        private Futures.FutureCombiner<Object> futureCombiner() {
            if (this.allMustSucceed == false) goto L6;
            return Futures.whenAllSucceed(inputFutures());
        L6:
            return Futures.whenAllComplete(inputFutures());
        }

        private ImmutableList<FluentFuture<?>> inputFutures() {
            return FluentIterable.from(this.inputs).transform(new ClosingFuture$Combiner$$ExternalSyntheticLambda0()).toList();
        }

        static /* synthetic */ FluentFuture lambda$inputFutures$0(ClosingFuture r1) {
            return ClosingFuture.access$000(r1);
        }

        public <V> ClosingFuture<V> call(final CombiningCallable<V> r6, Executor r7) {
            Callable<V> r0 = new CallableC09291(this, r6);
            ClosingFuture<V> r1 = new ClosingFuture(futureCombiner().call(r0, r7), null);
            ClosingFuture.access$500(r1).add(this.closeables, MoreExecutors.directExecutor());
            return r1;
        }

        public <V> ClosingFuture<V> callAsync(final AsyncCombiningCallable<V> r6, Executor r7) {
            AsyncCallable<V> r0 = new C09302(this, r6);
            ClosingFuture<V> r1 = new ClosingFuture(futureCombiner().callAsync(r0, r7), null);
            ClosingFuture.access$500(r1).add(this.closeables, MoreExecutors.directExecutor());
            return r1;
        }
    }

    public static final class Combiner2<V1, V2> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;

        public interface AsyncClosingFunction2<V1, V2, U> {
            ClosingFuture<U> apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3) throws Exception;
        }

        public interface ClosingFunction2<V1, V2, U> {
            @ParametricNullness
            U apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3) throws Exception;
        }

        private Combiner2(ClosingFuture<V1> r4, ClosingFuture<V2> r5) {
            boolean r2 = true;
            super(r2, ImmutableList.m214of(r4, r5), null);
            this.future1 = r4;
            this.future2 = r5;
        }

        /* synthetic */ Combiner2(ClosingFuture r1, ClosingFuture r2, CallableC09191 r3) {
            this(r1, r2);
        }

        static /* synthetic */ ClosingFuture access$1800(Combiner2 r1) {
            return r1.future1;
        }

        static /* synthetic */ ClosingFuture access$1900(Combiner2 r1) {
            return r1.future2;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction2<V1, V2, U> r2, Executor r3) {
            return call(new C09311(this, r2), r3);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction2<V1, V2, U> r2, Executor r3) {
            return callAsync(new C09322(this, r2), r3);
        }
    }

    public static final class Combiner3<V1, V2, V3> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;

        public interface AsyncClosingFunction3<V1, V2, V3, U> {
            ClosingFuture<U> apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3, @ParametricNullness V3 r4) throws Exception;
        }

        public interface ClosingFunction3<V1, V2, V3, U> {
            @ParametricNullness
            U apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3, @ParametricNullness V3 r4) throws Exception;
        }

        private Combiner3(ClosingFuture<V1> r4, ClosingFuture<V2> r5, ClosingFuture<V3> r6) {
            boolean r2 = true;
            super(r2, ImmutableList.m215of(r4, r5, r6), null);
            this.future1 = r4;
            this.future2 = r5;
            this.future3 = r6;
        }

        /* synthetic */ Combiner3(ClosingFuture r1, ClosingFuture r2, ClosingFuture r3, CallableC09191 r4) {
            this(r1, r2, r3);
        }

        static /* synthetic */ ClosingFuture access$2000(Combiner3 r1) {
            return r1.future1;
        }

        static /* synthetic */ ClosingFuture access$2100(Combiner3 r1) {
            return r1.future2;
        }

        static /* synthetic */ ClosingFuture access$2200(Combiner3 r1) {
            return r1.future3;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction3<V1, V2, V3, U> r2, Executor r3) {
            return call(new C09331(this, r2), r3);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction3<V1, V2, V3, U> r2, Executor r3) {
            return callAsync(new C09342(this, r2), r3);
        }
    }

    public static final class Combiner4<V1, V2, V3, V4> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        private final ClosingFuture<V4> future4;

        public interface AsyncClosingFunction4<V1, V2, V3, V4, U> {
            ClosingFuture<U> apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3, @ParametricNullness V3 r4, @ParametricNullness V4 r5) throws Exception;
        }

        public interface ClosingFunction4<V1, V2, V3, V4, U> {
            @ParametricNullness
            U apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3, @ParametricNullness V3 r4, @ParametricNullness V4 r5) throws Exception;
        }

        private Combiner4(ClosingFuture<V1> r4, ClosingFuture<V2> r5, ClosingFuture<V3> r6, ClosingFuture<V4> r7) {
            boolean r2 = true;
            super(r2, ImmutableList.m216of(r4, r5, r6, r7), null);
            this.future1 = r4;
            this.future2 = r5;
            this.future3 = r6;
            this.future4 = r7;
        }

        /* synthetic */ Combiner4(ClosingFuture r1, ClosingFuture r2, ClosingFuture r3, ClosingFuture r4, CallableC09191 r5) {
            this(r1, r2, r3, r4);
        }

        static /* synthetic */ ClosingFuture access$2300(Combiner4 r1) {
            return r1.future1;
        }

        static /* synthetic */ ClosingFuture access$2400(Combiner4 r1) {
            return r1.future2;
        }

        static /* synthetic */ ClosingFuture access$2500(Combiner4 r1) {
            return r1.future3;
        }

        static /* synthetic */ ClosingFuture access$2600(Combiner4 r1) {
            return r1.future4;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction4<V1, V2, V3, V4, U> r2, Executor r3) {
            return call(new C09351(this, r2), r3);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction4<V1, V2, V3, V4, U> r2, Executor r3) {
            return callAsync(new C09362(this, r2), r3);
        }
    }

    public static final class Combiner5<V1, V2, V3, V4, V5> extends Combiner {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        private final ClosingFuture<V4> future4;
        private final ClosingFuture<V5> future5;

        public interface AsyncClosingFunction5<V1, V2, V3, V4, V5, U> {
            ClosingFuture<U> apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3, @ParametricNullness V3 r4, @ParametricNullness V4 r5, @ParametricNullness V5 r6) throws Exception;
        }

        public interface ClosingFunction5<V1, V2, V3, V4, V5, U> {
            @ParametricNullness
            U apply(DeferredCloser r1, @ParametricNullness V1 r2, @ParametricNullness V2 r3, @ParametricNullness V3 r4, @ParametricNullness V4 r5, @ParametricNullness V5 r6) throws Exception;
        }

        private Combiner5(ClosingFuture<V1> r4, ClosingFuture<V2> r5, ClosingFuture<V3> r6, ClosingFuture<V4> r7, ClosingFuture<V5> r8) {
            boolean r2 = true;
            super(r2, ImmutableList.m217of(r4, r5, r6, r7, r8), null);
            this.future1 = r4;
            this.future2 = r5;
            this.future3 = r6;
            this.future4 = r7;
            this.future5 = r8;
        }

        /* synthetic */ Combiner5(ClosingFuture r1, ClosingFuture r2, ClosingFuture r3, ClosingFuture r4, ClosingFuture r5, CallableC09191 r6) {
            this(r1, r2, r3, r4, r5);
        }

        static /* synthetic */ ClosingFuture access$2700(Combiner5 r1) {
            return r1.future1;
        }

        static /* synthetic */ ClosingFuture access$2800(Combiner5 r1) {
            return r1.future2;
        }

        static /* synthetic */ ClosingFuture access$2900(Combiner5 r1) {
            return r1.future3;
        }

        static /* synthetic */ ClosingFuture access$3000(Combiner5 r1) {
            return r1.future4;
        }

        static /* synthetic */ ClosingFuture access$3100(Combiner5 r1) {
            return r1.future5;
        }

        public <U> ClosingFuture<U> call(final ClosingFunction5<V1, V2, V3, V4, V5, U> r2, Executor r3) {
            return call(new C09371(this, r2), r3);
        }

        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction5<V1, V2, V3, V4, V5, U> r2, Executor r3) {
            return callAsync(new C09382(this, r2), r3);
        }
    }

    public static final class DeferredCloser {
        private final CloseableList list;

        DeferredCloser(CloseableList r1) {
            this.list = r1;
        }

        @ParametricNullness
        public <C extends AutoCloseable> C eventuallyClose(@ParametricNullness C r3, Executor r4) {
            Preconditions.checkNotNull(r4);
            if (r3 == null) goto L5;
            this.list.add(r3, r4);
        L5:
            return r3;
        }
    }

    public static final class Peeker {
        private volatile boolean beingCalled;
        private final ImmutableList<ClosingFuture<?>> futures;

        private Peeker(ImmutableList<ClosingFuture<?>> r2) {
            this.futures = (ImmutableList) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ Peeker(ImmutableList r1, CallableC09191 r2) {
            this(r1);
        }

        static /* synthetic */ Object access$1500(Peeker r1, Combiner.CombiningCallable r2, CloseableList r3) throws Exception {
            return r1.call(r2, r3);
        }

        static /* synthetic */ FluentFuture access$1700(Peeker r1, Combiner.AsyncCombiningCallable r2, CloseableList r3) throws Exception {
            return r1.callAsync(r2, r3);
        }

        @ParametricNullness
        private <V> V call(Combiner.CombiningCallable<V> r5, CloseableList r6) throws Exception {
            this.beingCalled = true;
            CloseableList r0 = new CloseableList(null);
            V r2 = r5.call(CloseableList.access$300(r0), this);     // Catch: Throwable -> L6
            r6.add(r0, MoreExecutors.directExecutor());
            this.beingCalled = false;
            return r2;
        L6:
            th = move-exception;
            r6.add(r0, MoreExecutors.directExecutor());
            this.beingCalled = false;
            throw th;
        }

        private <V> FluentFuture<V> callAsync(Combiner.AsyncCombiningCallable<V> r6, CloseableList r7) throws Exception {
            this.beingCalled = true;
            CloseableList r0 = new CloseableList(null);
            ClosingFuture<V> r2 = r6.call(CloseableList.access$300(r0), this);     // Catch: Throwable -> L6
            ClosingFuture.access$400(r2, r7);     // Catch: Throwable -> L6
            FluentFuture<V> r3 = ClosingFuture.access$000(r2);     // Catch: Throwable -> L6
            r7.add(r0, MoreExecutors.directExecutor());
            this.beingCalled = false;
            return r3;
        L6:
            th = move-exception;
            r7.add(r0, MoreExecutors.directExecutor());
            this.beingCalled = false;
            throw th;
        }

        @ParametricNullness
        public final <D> D getDone(ClosingFuture<D> r2) throws ExecutionException {
            Preconditions.checkState(this.beingCalled);
            Preconditions.checkArgument(this.futures.contains(r2));
            return (D) Futures.getDone(ClosingFuture.access$000(r2));
        }
    }

    static final enum State extends Enum<State> {
        private static final /* synthetic */ State[] $VALUES = null;
        public static final State CLOSED = null;
        public static final State CLOSING = null;
        public static final State OPEN = null;
        public static final State SUBSUMED = null;
        public static final State WILL_CLOSE = null;
        public static final State WILL_CREATE_VALUE_AND_CLOSER = null;

        private static /* synthetic */ State[] $values() {
            return new State[]{OPEN, SUBSUMED, WILL_CLOSE, CLOSING, CLOSED, WILL_CREATE_VALUE_AND_CLOSER};
        }

        static {
            OPEN = new State("OPEN", 0);
            SUBSUMED = new State("SUBSUMED", 1);
            WILL_CLOSE = new State("WILL_CLOSE", 2);
            CLOSING = new State("CLOSING", 3);
            CLOSED = new State("CLOSED", 4);
            WILL_CREATE_VALUE_AND_CLOSER = new State("WILL_CREATE_VALUE_AND_CLOSER", 5);
            $VALUES = $values();
        }

        private State(String r1, int r2) {
            super(r1, r2);
        }

        public static State valueOf(String r1) {
            return (State) Enum.valueOf(State.class, r1);
        }

        public static State[] values() {
            return (State[]) $VALUES.clone();
        }
    }

    public static final class ValueAndCloser<V> {
        private final ClosingFuture<? extends V> closingFuture;

        ValueAndCloser(ClosingFuture<? extends V> r2) {
            this.closingFuture = (ClosingFuture) Preconditions.checkNotNull(r2);
        }

        public void closeAsync() {
            ClosingFuture.access$100(this.closingFuture);
        }

        @ParametricNullness
        public V get() throws ExecutionException {
            return (V) Futures.getDone(ClosingFuture.access$000(this.closingFuture));
        }
    }

    public interface ValueAndCloserConsumer<V> {
        void accept(ValueAndCloser<V> r1);
    }

    static {
        logger = new LazyLogger(ClosingFuture.class);
    }

    private ClosingFuture(ListenableFuture<V> r3) {
        this(r3, new CloseableList(null));
    }

    /* synthetic */ ClosingFuture(ListenableFuture r1, CallableC09191 r2) {
        this(r1);
    }

    private ClosingFuture(ListenableFuture<V> r3, CloseableList r4) {
        this.state = new AtomicReference(State.OPEN);
        this.future = FluentFuture.from(r3);
        this.closeables = r4;
    }

    static /* synthetic */ FluentFuture access$000(ClosingFuture r1) {
        return r1.future;
    }

    static /* synthetic */ void access$100(ClosingFuture r0) {
        r0.close();
    }

    static /* synthetic */ void access$1100(ClosingFuture r0, State r1, State r2) {
        r0.checkAndUpdateState(r1, r2);
    }

    static /* synthetic */ void access$1200(ValueAndCloserConsumer r0, ClosingFuture r1) {
        provideValueAndCloser(r0, r1);
    }

    static /* synthetic */ void access$3200(AutoCloseable r0, Executor r1) {
        closeQuietly(r0, r1);
    }

    static /* synthetic */ void access$400(ClosingFuture r0, CloseableList r1) {
        r0.becomeSubsumedInto(r1);
    }

    static /* synthetic */ CloseableList access$500(ClosingFuture r1) {
        return r1.closeables;
    }

    private void becomeSubsumedInto(CloseableList r3) {
        checkAndUpdateState(State.OPEN, State.SUBSUMED);
        r3.add(this.closeables, MoreExecutors.directExecutor());
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
    private <X extends Throwable, W extends V> ClosingFuture<V> catchingAsyncMoreGeneric(Class<X> r3, final AsyncClosingFunction<? super X, W> r4, Executor r5) {
        Preconditions.checkNotNull(r4);
        Object r0 = new C09278(this, r4);
        return (ClosingFuture<V>) derive(this.future.catchingAsync(r3, r0, r5));
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
    private <X extends Throwable, W extends V> ClosingFuture<V> catchingMoreGeneric(Class<X> r3, final ClosingFunction<? super X, W> r4, Executor r5) {
        Preconditions.checkNotNull(r4);
        Object r0 = new C09267(this, r4);
        return (ClosingFuture<V>) derive(this.future.catchingAsync(r3, r0, r5));
    }

    private void checkAndUpdateState(State r3, State r4) {
        Preconditions.checkState(compareAndUpdateState(r3, r4), "Expected state to be %s, but it was %s", r3, r4);
    }

    private void close() {
        logger.get().log(Level.FINER, "closing {0}", this);
        this.closeables.close();
    }

    private static void closeQuietly(@CheckForNull final AutoCloseable r5, Executor r6) {
        if (r5 != null) goto L12;
        return;
    L12:
        r6.execute(new ClosingFuture$$ExternalSyntheticLambda0(r5));     // Catch: RejectedExecutionException -> L6
        return;
    L6:
        e = move-exception;
        if (logger.get().isLoggable(Level.WARNING) == false) goto L10;
        logger.get().log(Level.WARNING, String.format("while submitting close to %s; will close inline", new Object[]{r6}), e);
    L10:
        closeQuietly(r5, MoreExecutors.directExecutor());
    }

    private boolean compareAndUpdateState(State r2, State r3) {
        return C0305xa830b2f.m111m(this.state, r2, r3);
    }

    private <U> ClosingFuture<U> derive(FluentFuture<U> r3) {
        ClosingFuture<U> r0 = new ClosingFuture(r3);
        becomeSubsumedInto(r0.closeables);
        return r0;
    }

    @Deprecated
    public static <C extends AutoCloseable> ClosingFuture<C> eventuallyClosing(ListenableFuture<C> r3, final Executor r4) {
        Preconditions.checkNotNull(r4);
        final ClosingFuture<C> r0 = new ClosingFuture(Futures.nonCancellationPropagating(r3));
        Futures.addCallback(r3, new C09223(r0, r4), MoreExecutors.directExecutor());
        return r0;
    }

    public static <V> ClosingFuture<V> from(ListenableFuture<V> r1) {
        return new ClosingFuture(r1);
    }

    static /* synthetic */ void lambda$closeQuietly$0(AutoCloseable r4) {
        r4.close();     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        Platform.restoreInterruptIfIsInterruptedException(e);
        logger.get().log(Level.WARNING, "thrown by close()", e);
    }

    private static <C, V extends C> void provideValueAndCloser(ValueAndCloserConsumer<C> r1, ClosingFuture<V> r2) {
        r1.accept(new ValueAndCloser(r2));
    }

    public static <V> ClosingFuture<V> submit(final ClosingCallable<V> r3, Executor r4) {
        Preconditions.checkNotNull(r3);
        final CloseableList r0 = new CloseableList(null);
        TrustedListenableFutureTask r1 = TrustedListenableFutureTask.create(new CallableC09191(r3, r0));
        r4.execute(r1);
        return new ClosingFuture(r1, r0);
    }

    public static <V> ClosingFuture<V> submitAsync(final AsyncClosingCallable<V> r3, Executor r4) {
        Preconditions.checkNotNull(r3);
        final CloseableList r0 = new CloseableList(null);
        TrustedListenableFutureTask r1 = TrustedListenableFutureTask.create(new C09212(r3, r0));
        r4.execute(r1);
        return new ClosingFuture(r1, r0);
    }

    public static Combiner whenAllComplete(ClosingFuture<?> r1, ClosingFuture<?>... r2) {
        return whenAllComplete(Lists.asList(r1, r2));
    }

    public static Combiner whenAllComplete(Iterable<? extends ClosingFuture<?>> r3) {
        return new Combiner(false, r3, null);
    }

    public static <V1, V2> Combiner2<V1, V2> whenAllSucceed(ClosingFuture<V1> r2, ClosingFuture<V2> r3) {
        return new Combiner2(r2, r3, null);
    }

    public static <V1, V2, V3> Combiner3<V1, V2, V3> whenAllSucceed(ClosingFuture<V1> r2, ClosingFuture<V2> r3, ClosingFuture<V3> r4) {
        return new Combiner3(r2, r3, r4, null);
    }

    public static <V1, V2, V3, V4> Combiner4<V1, V2, V3, V4> whenAllSucceed(ClosingFuture<V1> r6, ClosingFuture<V2> r7, ClosingFuture<V3> r8, ClosingFuture<V4> r9) {
        return new Combiner4(r6, r7, r8, r9, null);
    }

    public static <V1, V2, V3, V4, V5> Combiner5<V1, V2, V3, V4, V5> whenAllSucceed(ClosingFuture<V1> r7, ClosingFuture<V2> r8, ClosingFuture<V3> r9, ClosingFuture<V4> r10, ClosingFuture<V5> r11) {
        return new Combiner5(r7, r8, r9, r10, r11, null);
    }

    public static Combiner whenAllSucceed(ClosingFuture<?> r2, ClosingFuture<?> r3, ClosingFuture<?> r4, ClosingFuture<?> r5, ClosingFuture<?> r6, ClosingFuture<?> r7, ClosingFuture<?>... r8) {
        return whenAllSucceed(FluentIterable.m198of(r2, new ClosingFuture[]{r3, r4, r5, r6, r7}).append(r8));
    }

    public static Combiner whenAllSucceed(Iterable<? extends ClosingFuture<?>> r3) {
        return new Combiner(true, r3, null);
    }

    public static <V, U> AsyncClosingFunction<V, U> withoutCloser(final AsyncFunction<V, U> r1) {
        Preconditions.checkNotNull(r1);
        return new C09256(r1);
    }

    public boolean cancel(boolean r4) {
        logger.get().log(Level.FINER, "cancelling {0}", this);
        boolean r0 = this.future.cancel(r4);
        if (r0 == false) goto L5;
        close();
    L5:
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <X extends Throwable> ClosingFuture<V> catching(Class<X> r2, ClosingFunction<? super X, ? extends V> r3, Executor r4) {
        return catchingMoreGeneric(r2, r3, r4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <X extends Throwable> ClosingFuture<V> catchingAsync(Class<X> r2, AsyncClosingFunction<? super X, ? extends V> r3, Executor r4) {
        return catchingAsyncMoreGeneric(r2, r3, r4);
    }

    protected void finalize() {
        if (this.state.get().equals(State.OPEN) == false) goto L6;
        logger.get().log(Level.SEVERE, "Uh oh! An open ClosingFuture has leaked and will close: {0}", this);
        finishToFuture();
        return;
    }

    public FluentFuture<V> finishToFuture() {
        if (compareAndUpdateState(State.OPEN, State.WILL_CLOSE) == false) goto L6;
        logger.get().log(Level.FINER, "will close {0}", this);
        this.future.addListener(new RunnableC09289(this), MoreExecutors.directExecutor());
    L17:
        return this.future;
    L6:
        switch(this.state.get().ordinal()) {
            case 0: goto L15;
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L11;
            case 4: goto L11;
            case 5: goto L9;
            default: goto L17;
        };
    L9:
        throw new IllegalStateException("Cannot call finishToFuture() after calling finishToValueAndCloser()");
    L11:
        throw new IllegalStateException("Cannot call finishToFuture() twice");
    L13:
        throw new IllegalStateException("Cannot call finishToFuture() after deriving another step");
    L15:
        throw new AssertionError();
    }

    public void finishToValueAndCloser(final ValueAndCloserConsumer<? super V> r3, Executor r4) {
        Preconditions.checkNotNull(r3);
        if (compareAndUpdateState(State.OPEN, State.WILL_CREATE_VALUE_AND_CLOSER) == false) goto L5;
        this.future.addListener(new RunnableC092010(this, r3), r4);
        return;
    L5:
        switch(this.state.get().ordinal()) {
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L11;
            case 4: goto L11;
            case 5: goto L9;
            default: goto L7;
        };
    L7:
        throw new AssertionError(this.state);
    L9:
        throw new IllegalStateException("Cannot call finishToValueAndCloser() twice");
    L11:
        throw new IllegalStateException("Cannot call finishToValueAndCloser() after calling finishToFuture()");
    L13:
        throw new IllegalStateException("Cannot call finishToValueAndCloser() after deriving another step");
    }

    public ListenableFuture<?> statusFuture() {
        return Futures.nonCancellationPropagating(this.future.transform(Functions.constant(null), MoreExecutors.directExecutor()));
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("state", this.state.get()).addValue(this.future).toString();
    }

    public <U> ClosingFuture<U> transform(final ClosingFunction<? super V, U> r3, Executor r4) {
        Preconditions.checkNotNull(r3);
        AsyncFunction<V, U> r0 = new C09234(this, r3);
        return derive(this.future.transformAsync(r0, r4));
    }

    public <U> ClosingFuture<U> transformAsync(final AsyncClosingFunction<? super V, U> r3, Executor r4) {
        Preconditions.checkNotNull(r3);
        AsyncFunction<V, U> r0 = new C09245(this, r3);
        return derive(this.future.transformAsync(r0, r4));
    }

    CountDownLatch whenClosedCountDown() {
        return this.closeables.whenClosedCountDown();
    }
}
