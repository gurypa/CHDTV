package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractExecutionThreadService implements Service {
    private static final LazyLogger logger = null;
    private final Service delegate;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$1 */
    class C08991 extends AbstractService {
        final /* synthetic */ AbstractExecutionThreadService this$0;

        C08991(AbstractExecutionThreadService r1) {
            this.this$0 = r1;
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            MoreExecutors.renamingDecorator(this.this$0.executor(), new AbstractExecutionThreadService$1$$ExternalSyntheticLambda0(this)).execute(new AbstractExecutionThreadService$1$$ExternalSyntheticLambda1(this));
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected void doStop() {
            this.this$0.triggerShutdown();
        }

        /* JADX INFO: renamed from: lambda$doStart$0$com-google-common-util-concurrent-AbstractExecutionThreadService$1 */
        /* synthetic */ String m378xa0f821c5() {
            return this.this$0.serviceName();
        }

        /* JADX INFO: renamed from: lambda$doStart$1$com-google-common-util-concurrent-AbstractExecutionThreadService$1 */
        /* synthetic */ void m379x3d661e24() {
            this.this$0.startUp();     // Catch: Throwable -> L16
            notifyStarted();     // Catch: Throwable -> L16
            if (isRunning() == true) goto L21;
        L14:
            this.this$0.shutDown();     // Catch: Throwable -> L16
            notifyStopped();     // Catch: Throwable -> L16
            return;
        L21:
            this.this$0.run();     // Catch: Throwable -> L6
        L6:
            th = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(th);     // Catch: Throwable -> L16
            this.this$0.shutDown();     // Catch: Exception -> L10 Throwable -> L16
        L12:
            notifyFailed(th);     // Catch: Throwable -> L16
            return;
        L10:
            e = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(e);     // Catch: Throwable -> L16
            AbstractExecutionThreadService.access$000().get().log(Level.WARNING, "Error while attempting to shut down the service after failure.", e);     // Catch: Throwable -> L16
        L16:
            th = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(th);
            notifyFailed(th);
        }

        @Override // com.google.common.util.concurrent.AbstractService
        public String toString() {
            return this.this$0.toString();
        }
    }

    static {
        logger = new LazyLogger(AbstractExecutionThreadService.class);
    }

    protected AbstractExecutionThreadService() {
        this.delegate = new C08991(this);
    }

    static /* synthetic */ LazyLogger access$000() {
        return logger;
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener r2, Executor r3) {
        this.delegate.addListener(r2, r3);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long r2, TimeUnit r4) throws TimeoutException {
        this.delegate.awaitRunning(r2, r4);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long r2, TimeUnit r4) throws TimeoutException {
        this.delegate.awaitTerminated(r2, r4);
    }

    protected Executor executor() {
        return new AbstractExecutionThreadService$$ExternalSyntheticLambda0(this);
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    /* JADX INFO: renamed from: lambda$executor$0$com-google-common-util-concurrent-AbstractExecutionThreadService */
    /* synthetic */ void m377xafeb5522(Runnable r2) {
        MoreExecutors.newThread(serviceName(), r2).start();
    }

    protected abstract void run() throws Exception;

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    protected void shutDown() throws Exception {
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    protected void startUp() throws Exception {
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    public String toString() {
        return serviceName() + " [" + state() + "]";
    }

    protected void triggerShutdown() {
    }
}
