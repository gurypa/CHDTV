package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class AggregateFuture<InputT, OutputT> extends AggregateFutureState<OutputT> {
    private static final LazyLogger logger = null;
    private final boolean allMustSucceed;
    private final boolean collectsValues;

    @CheckForNull
    @LazyInit
    private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;

    static final enum ReleaseResourcesReason extends Enum<ReleaseResourcesReason> {
        private static final /* synthetic */ ReleaseResourcesReason[] $VALUES = null;
        public static final ReleaseResourcesReason ALL_INPUT_FUTURES_PROCESSED = null;
        public static final ReleaseResourcesReason OUTPUT_FUTURE_DONE = null;

        private static /* synthetic */ ReleaseResourcesReason[] $values() {
            return new ReleaseResourcesReason[]{OUTPUT_FUTURE_DONE, ALL_INPUT_FUTURES_PROCESSED};
        }

        static {
            OUTPUT_FUTURE_DONE = new ReleaseResourcesReason("OUTPUT_FUTURE_DONE", 0);
            ALL_INPUT_FUTURES_PROCESSED = new ReleaseResourcesReason("ALL_INPUT_FUTURES_PROCESSED", 1);
            $VALUES = $values();
        }

        private ReleaseResourcesReason(String r1, int r2) {
            super(r1, r2);
        }

        public static ReleaseResourcesReason valueOf(String r1) {
            return (ReleaseResourcesReason) Enum.valueOf(ReleaseResourcesReason.class, r1);
        }

        public static ReleaseResourcesReason[] values() {
            return (ReleaseResourcesReason[]) $VALUES.clone();
        }
    }

    static {
        logger = new LazyLogger(AggregateFuture.class);
    }

    AggregateFuture(ImmutableCollection<? extends ListenableFuture<? extends InputT>> r2, boolean r3, boolean r4) {
        super(r2.size());
        this.futures = (ImmutableCollection) Preconditions.checkNotNull(r2);
        this.allMustSucceed = r3;
        this.collectsValues = r4;
    }

    private static boolean addCausalChain(Set<Throwable> r3, Throwable r4) {
        Throwable r0 = r4;
    L3:
        if (r0 == null) goto L9;
        if (r3.add(r0) == false) goto L6;
        r0 = r0.getCause();
        goto L3
    L6:
        return false;
    L9:
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void collectValueFromNonCancelledFuture(int r3, Future<? extends InputT> r4) {
        collectOneValue(r3, Uninterruptibles.getUninterruptibly(r4));     // Catch: Throwable -> L4 ExecutionException -> L6
    L13:
        return;
    L6:
        e = move-exception;
        handleException(e.getCause());
        return;
    L4:
        th = move-exception;
        handleException(th);
    }

    private void decrementCountAndMaybeComplete(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> r4) {
        int r0 = decrementRemainingAndGet();
        if (r0 < 0) goto L5;
        boolean r1 = true;
    L6:
        Preconditions.checkState(r1, "Less than 0 remaining futures");
        if (r0 != 0) goto L10;
        processCompleted(r4);
        return;
    L10:
        return;
    L5:
        r1 = false;
        goto L6
    }

    private void handleException(Throwable r3) {
        Preconditions.checkNotNull(r3);
        if (this.allMustSucceed == false) goto L11;
        if (setException(r3) == true) goto L11;
        if (addCausalChain(getOrInitSeenExceptions(), r3) == false) goto L11;
        log(r3);
        return;
    L11:
        if ((r3 instanceof Error) == false) goto L14;
        log(r3);
        return;
    }

    private static void log(Throwable r3) {
        if ((r3 instanceof Error) == false) goto L5;
        String r0 = "Input Future failed with Error";
    L6:
        logger.get().log(Level.SEVERE, r0, r3);
        return;
    L5:
        r0 = "Got more than one input Future failure. Logging failures after the first";
        goto L6
    }

    private void processAllMustSucceedDoneFuture(int r3, ListenableFuture<? extends InputT> r4) {
    L9:
        th = move-exception;
        decrementCountAndMaybeComplete(null);
        throw th;
    L4:
        if (r4.isCancelled() == false) goto L6;
        this.futures = null;     // Catch: Throwable -> L9
        cancel(false);     // Catch: Throwable -> L9
    L7:
        decrementCountAndMaybeComplete(null);
        return;
    L6:
        collectValueFromNonCancelledFuture(r3, r4);     // Catch: Throwable -> L9
        goto L7
    }

    private void processCompleted(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> r5) {
        if (r5 == null) goto L10;
        int r0 = 0;
        UnmodifiableIterator<? extends Future<? extends InputT>> r1 = r5.iterator();
    L5:
        if (r1.hasNext() == false) goto L10;
        Future<? extends InputT> r2 = r1.next();
        if (r2.isCancelled() == true) goto L9;
        collectValueFromNonCancelledFuture(r0, r2);
    L9:
        r0 = r0 + 1;
    L10:
        clearSeenExceptions();
        handleAllCompleted();
        releaseResources(ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
    }

    @Override // com.google.common.util.concurrent.AggregateFutureState
    final void addInitialException(Set<Throwable> r2) {
        Preconditions.checkNotNull(r2);
        if (isCancelled() == true) goto L6;
        addCausalChain(r2, (Throwable) Objects.requireNonNull(tryInternalFastPathGetFailure()));
        return;
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected final void afterDone() {
        super.afterDone();
        ImmutableCollection<? extends ListenableFuture<? extends InputT>> r0 = this.futures;
        releaseResources(ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
        boolean r1 = isCancelled();
        if (r0 == null) goto L5;
        boolean r2 = true;
    L7:
        if ((r1 & r2) == false) goto L12;
        boolean r3 = wasInterrupted();
        UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> r4 = r0.iterator();
    L10:
        if (r4.hasNext() == false) goto L14;
        r4.next().cancel(r3);
        goto L10
    L14:
        return;
    L12:
        return;
    L5:
        r2 = false;
        goto L7
    }

    abstract void collectOneValue(int r1, @ParametricNullness InputT r2);

    abstract void handleAllCompleted();

    final void init() {
        Objects.requireNonNull(this.futures);
        if (this.futures.isEmpty() == false) goto L7;
        handleAllCompleted();
        return;
    L7:
        if (this.allMustSucceed == false) goto L18;
        final int r0 = 0;
        UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> r1 = this.futures.iterator();
    L10:
        if (r1.hasNext() == false) goto L29;
        final ListenableFuture<? extends InputT> r2 = r1.next();
        int r3 = r0 + 1;
        if (r2.isDone() == false) goto L14;
        processAllMustSucceedDoneFuture(r0, r2);
    L15:
        r0 = r3;
        goto L10
    L14:
        r2.addListener(new AggregateFuture$$ExternalSyntheticLambda0(this, r0, r2), MoreExecutors.directExecutor());
        goto L15
    L29:
        return;
    L18:
        if (this.collectsValues == false) goto L20;
        final ImmutableCollection<? extends ListenableFuture<? extends InputT>> r4 = this.futures;
    L21:
        Runnable r5 = new AggregateFuture$$ExternalSyntheticLambda1(this, r4);
        UnmodifiableIterator<? extends ListenableFuture<? extends InputT>> r6 = this.futures.iterator();
    L23:
        if (r6.hasNext() == false) goto L39;
        ListenableFuture<? extends InputT> r7 = r6.next();
        if (r7.isDone() == true) goto L26;
        r7.addListener(r5, MoreExecutors.directExecutor());
        goto L23
    L26:
        decrementCountAndMaybeComplete(r4);
        goto L23
    L39:
        return;
    L20:
        r4 = null;
        goto L21
    }

    /* JADX INFO: renamed from: lambda$init$0$com-google-common-util-concurrent-AggregateFuture, reason: not valid java name */
    /* synthetic */ void m507lambda$init$0$comgooglecommonutilconcurrentAggregateFuture(int r1, ListenableFuture r2) {
        processAllMustSucceedDoneFuture(r1, r2);
    }

    /* JADX INFO: renamed from: lambda$init$1$com-google-common-util-concurrent-AggregateFuture, reason: not valid java name */
    /* synthetic */ void m508lambda$init$1$comgooglecommonutilconcurrentAggregateFuture(ImmutableCollection r1) {
        decrementCountAndMaybeComplete(r1);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    @CheckForNull
    protected final String pendingToString() {
        ImmutableCollection<? extends ListenableFuture<? extends InputT>> r0 = this.futures;
        if (r0 == null) goto L7;
        return "futures=" + r0;
    L7:
        return super.pendingToString();
    }

    void releaseResources(ReleaseResourcesReason r2) {
        Preconditions.checkNotNull(r2);
        this.futures = null;
    }
}
