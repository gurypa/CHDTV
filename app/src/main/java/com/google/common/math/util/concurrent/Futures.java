package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Futures extends GwtFuturesCatchingSpecialization {

    private static final class CallbackListener<V> implements Runnable {
        final FutureCallback<? super V> callback;
        final Future<V> future;

        CallbackListener(Future<V> r1, FutureCallback<? super V> r2) {
            this.future = r1;
            this.callback = r2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if ((this.future instanceof InternalFutureFailureAccess) == false) goto L17;
            Throwable r0 = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) this.future);
            if (r0 == null) goto L17;
            this.callback.onFailure(r0);
            return;
        L17:
            Object r1 = Futures.getDone(this.future);     // Catch: Throwable -> L11 ExecutionException -> L14
            this.callback.onSuccess(r1);
            return;
        L14:
            e = move-exception;
            this.callback.onFailure(e.getCause());
            return;
        L11:
            th = move-exception;
            this.callback.onFailure(th);
        }

        public String toString() {
            return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
        }
    }

    public static final class FutureCombiner<V> {
        private final boolean allMustSucceed;
        private final ImmutableList<ListenableFuture<? extends V>> futures;

        private FutureCombiner(boolean r1, ImmutableList<ListenableFuture<? extends V>> r2) {
            this.allMustSucceed = r1;
            this.futures = r2;
        }

        /* synthetic */ FutureCombiner(boolean r1, ImmutableList r2, FutureC09451 r3) {
            this(r1, r2);
        }

        public <C> ListenableFuture<C> call(Callable<C> r4, Executor r5) {
            return new CombinedFuture(this.futures, this.allMustSucceed, r5, r4);
        }

        public <C> ListenableFuture<C> callAsync(AsyncCallable<C> r4, Executor r5) {
            return new CombinedFuture(this.futures, this.allMustSucceed, r5, r4);
        }

        public ListenableFuture<?> run(final Runnable r2, Executor r3) {
            return call(new CallableC09461(this, r2), r3);
        }
    }

    private static final class InCompletionOrderFuture<T> extends AbstractFuture<T> {

        @CheckForNull
        private InCompletionOrderState<T> state;

        private InCompletionOrderFuture(InCompletionOrderState<T> r1) {
            this.state = r1;
        }

        /* synthetic */ InCompletionOrderFuture(InCompletionOrderState r1, FutureC09451 r2) {
            this(r1);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        protected void afterDone() {
            this.state = null;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public boolean cancel(boolean r3) {
            InCompletionOrderState<T> r0 = this.state;
            if (super.cancel(r3) == false) goto L6;
            InCompletionOrderState.access$300((InCompletionOrderState) Objects.requireNonNull(r0), r3);
            return true;
        L6:
            return false;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        @CheckForNull
        protected String pendingToString() {
            InCompletionOrderState<T> r0 = this.state;
            if (r0 != null) goto L5;
            return null;
        L5:
            return "inputCount=[" + InCompletionOrderState.access$400(r0).length + "], remaining=[" + InCompletionOrderState.access$500(r0).get() + "]";
        }
    }

    private static final class InCompletionOrderState<T> {
        private volatile int delegateIndex;
        private final AtomicInteger incompleteOutputCount;
        private final ListenableFuture<? extends T>[] inputFutures;
        private boolean shouldInterrupt;
        private boolean wasCancelled;

        private InCompletionOrderState(ListenableFuture<? extends T>[] r3) {
            this.wasCancelled = false;
            this.shouldInterrupt = true;
            this.delegateIndex = 0;
            this.inputFutures = r3;
            this.incompleteOutputCount = new AtomicInteger(r3.length);
        }

        /* synthetic */ InCompletionOrderState(ListenableFuture[] r1, FutureC09451 r2) {
            this(r1);
        }

        static /* synthetic */ void access$300(InCompletionOrderState r0, boolean r1) {
            r0.recordOutputCancellation(r1);
        }

        static /* synthetic */ ListenableFuture[] access$400(InCompletionOrderState r1) {
            return r1.inputFutures;
        }

        static /* synthetic */ AtomicInteger access$500(InCompletionOrderState r1) {
            return r1.incompleteOutputCount;
        }

        static /* synthetic */ void access$600(InCompletionOrderState r0, ImmutableList r1, int r2) {
            r0.recordInputCompletion(r1, r2);
        }

        private void recordCompletion() {
            if (this.incompleteOutputCount.decrementAndGet() == 0) goto L5;
            return;
        L5:
            if (this.wasCancelled == false) goto L16;
            ListenableFuture<? extends T>[] r0 = this.inputFutures;
            int r1 = r0.length;
            int r2 = 0;
        L7:
            if (r2 >= r1) goto L17;
            ListenableFuture<? extends T> r3 = r0[r2];
            if (r3 == null) goto L11;
            r3.cancel(this.shouldInterrupt);
        L11:
            r2 = r2 + 1;
            goto L7
        L17:
            return;
        }

        private void recordInputCompletion(ImmutableList<AbstractFuture<T>> r4, int r5) {
            ListenableFuture<? extends T> r0 = (ListenableFuture) Objects.requireNonNull(this.inputFutures[r5]);
            this.inputFutures[r5] = null;
            int r1 = this.delegateIndex;
        L4:
            if (r1 >= r4.size()) goto L10;
            if (r4.get(r1).setFuture(r0) == true) goto L7;
            r1 = r1 + 1;
            goto L4
        L7:
            recordCompletion();
            this.delegateIndex = r1 + 1;
            return;
        L10:
            this.delegateIndex = r4.size();
        }

        private void recordOutputCancellation(boolean r2) {
            this.wasCancelled = true;
            if (r2 == true) goto L5;
            this.shouldInterrupt = false;
        L5:
            recordCompletion();
        }
    }

    private static final class NonCancellationPropagatingFuture<V> extends AbstractFuture.TrustedFuture<V> implements Runnable {

        @CheckForNull
        @LazyInit
        private ListenableFuture<V> delegate;

        NonCancellationPropagatingFuture(ListenableFuture<V> r1) {
            this.delegate = r1;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        protected void afterDone() {
            this.delegate = null;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture
        @CheckForNull
        protected String pendingToString() {
            ListenableFuture<V> r0 = this.delegate;
            if (r0 != null) goto L5;
            return null;
        L5:
            return "delegate=[" + r0 + "]";
        }

        @Override // java.lang.Runnable
        public void run() {
            ListenableFuture<V> r0 = this.delegate;
            if (r0 == null) goto L6;
            setFuture(r0);
            return;
        }
    }

    private Futures() {
    }

    public static <V> void addCallback(ListenableFuture<V> r1, FutureCallback<? super V> r2, Executor r3) {
        Preconditions.checkNotNull(r2);
        r1.addListener(new CallbackListener(r1, r2), r3);
    }

    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> r3) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(r3), true);
    }

    @SafeVarargs
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... r3) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(r3), true);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> r1, Class<X> r2, Function<? super X, ? extends V> r3, Executor r4) {
        return AbstractCatchingFuture.create(r1, r2, r3, r4);
    }

    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> r1, Class<X> r2, AsyncFunction<? super X, ? extends V> r3, Executor r4) {
        return AbstractCatchingFuture.createAsync(r1, r2, r3, r4);
    }

    @ParametricNullness
    public static <V, X extends Exception> V getChecked(Future<V> r1, Class<X> r2) throws Exception {
        return (V) FuturesGetChecked.getChecked(r1, r2);
    }

    @ParametricNullness
    public static <V, X extends Exception> V getChecked(Future<V> r1, Class<X> r2, long r3, TimeUnit r5) throws Exception {
        return (V) FuturesGetChecked.getChecked(r1, r2, r3, r5);
    }

    @ParametricNullness
    public static <V> V getDone(Future<V> r2) throws ExecutionException {
        Preconditions.checkState(r2.isDone(), "Future was expected to be done: %s", r2);
        return (V) Uninterruptibles.getUninterruptibly(r2);
    }

    @ParametricNullness
    public static <V> V getUnchecked(Future<V> r2) {
        Preconditions.checkNotNull(r2);
        return (V) Uninterruptibles.getUninterruptibly(r2);
    L5:
        e = move-exception;
        wrapAndThrowUnchecked(e.getCause());
        throw new AssertionError();
    }

    private static <T> ListenableFuture<? extends T>[] gwtCompatibleToArray(Iterable<? extends ListenableFuture<? extends T>> r2) {
        if ((r2 instanceof Collection) == false) goto L5;
        Collection r0 = (Collection) r2;
    L7:
        return (ListenableFuture[]) r0.toArray(new ListenableFuture[0]);
    L5:
        r0 = ImmutableList.copyOf(r2);
        goto L7
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        ImmediateFuture.ImmediateCancelledFuture<Object> r0 = ImmediateFuture.ImmediateCancelledFuture.INSTANCE;
        if (r0 == null) goto L6;
        return r0;
    L6:
        return new ImmediateFuture.ImmediateCancelledFuture();
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable r1) {
        Preconditions.checkNotNull(r1);
        return new ImmediateFuture.ImmediateFailedFuture(r1);
    }

    public static <V> ListenableFuture<V> immediateFuture(@ParametricNullness V r1) {
        if (r1 != null) goto L6;
        return (ListenableFuture<V>) ImmediateFuture.NULL;
    L6:
        return new ImmediateFuture(r1);
    }

    public static ListenableFuture<Void> immediateVoidFuture() {
        return ImmediateFuture.NULL;
    }

    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> r9) {
        ListenableFuture[] r0 = gwtCompatibleToArray(r9);
        FutureC09451 r2 = null;
        final InCompletionOrderState r1 = new InCompletionOrderState(r0, r2);
        ImmutableList.Builder r3 = ImmutableList.builderWithExpectedSize(r0.length);
        int r4 = 0;
    L4:
        if (r4 >= r0.length) goto L6;
        r3.add(new InCompletionOrderFuture(r1, r2));
        r4 = r4 + 1;
        goto L4
    L6:
        final ImmutableList<ListenableFuture<T>> r5 = r3.build();
        int r6 = 0;
    L8:
        if (r6 >= r0.length) goto L11;
        final int r7 = r6;
        r0[r6].addListener(new Futures$$ExternalSyntheticLambda0(r1, r5, r7), MoreExecutors.directExecutor());
        r6 = r6 + 1;
        goto L8
    L11:
        return r5;
    }

    static /* synthetic */ void lambda$inCompletionOrder$1(InCompletionOrderState r0, ImmutableList r1, int r2) {
        InCompletionOrderState.access$600(r0, r1, r2);
    }

    static /* synthetic */ void lambda$scheduleAsync$0(Future r1) {
        r1.cancel(false);
    }

    public static <I, O> Future<O> lazyTransform(final Future<I> r1, final Function<? super I, ? extends O> r2) {
        Preconditions.checkNotNull(r1);
        Preconditions.checkNotNull(r2);
        return new FutureC09451(r1, r2);
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> r2) {
        if (r2.isDone() == false) goto L5;
        return r2;
    L5:
        NonCancellationPropagatingFuture r0 = new NonCancellationPropagatingFuture(r2);
        r2.addListener(r0, MoreExecutors.directExecutor());
        return r0;
    }

    public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> r4, long r5, TimeUnit r7, ScheduledExecutorService r8) {
        TrustedListenableFutureTask r0 = TrustedListenableFutureTask.create(r4);
        final ScheduledFuture<?> r1 = r8.schedule(r0, r5, r7);
        r0.addListener(new Futures$$ExternalSyntheticLambda1(r1), MoreExecutors.directExecutor());
        return r0;
    }

    public static ListenableFuture<Void> submit(Runnable r1, Executor r2) {
        TrustedListenableFutureTask r0 = TrustedListenableFutureTask.create(r1, null);
        r2.execute(r0);
        return r0;
    }

    public static <O> ListenableFuture<O> submit(Callable<O> r1, Executor r2) {
        TrustedListenableFutureTask r0 = TrustedListenableFutureTask.create(r1);
        r2.execute(r0);
        return r0;
    }

    public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> r1, Executor r2) {
        TrustedListenableFutureTask r0 = TrustedListenableFutureTask.create(r1);
        r2.execute(r0);
        return r0;
    }

    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> r3) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(r3), false);
    }

    @SafeVarargs
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... r3) {
        return new CollectionFuture.ListFuture(ImmutableList.copyOf(r3), false);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> r1, Function<? super I, ? extends O> r2, Executor r3) {
        return AbstractTransformFuture.create(r1, r2, r3);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> r1, AsyncFunction<? super I, ? extends O> r2, Executor r3) {
        return AbstractTransformFuture.createAsync(r1, r2, r3);
    }

    public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> r4) {
        boolean r3 = false;
        return new FutureCombiner(r3, ImmutableList.copyOf(r4), null);
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... r4) {
        boolean r3 = false;
        return new FutureCombiner(r3, ImmutableList.copyOf(r4), null);
    }

    public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> r4) {
        boolean r3 = true;
        return new FutureCombiner(r3, ImmutableList.copyOf(r4), null);
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... r4) {
        boolean r3 = true;
        return new FutureCombiner(r3, ImmutableList.copyOf(r4), null);
    }

    public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> r1, long r2, TimeUnit r4, ScheduledExecutorService r5) {
        if (r1.isDone() == false) goto L6;
        return r1;
    L6:
        return TimeoutFuture.create(r1, r2, r4, r5);
    }

    private static void wrapAndThrowUnchecked(Throwable r2) {
        if ((r2 instanceof Error) == false) goto L7;
        throw new ExecutionError((Error) r2);
    L7:
        throw new UncheckedExecutionException(r2);
    }
}
