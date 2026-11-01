package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractIdleService implements Service {
    private final Service delegate;
    private final Supplier<String> threadNameSupplier;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.AbstractIdleService$1 */
    static /* synthetic */ class C09041 {
    }

    private final class DelegateService extends AbstractService {
        final /* synthetic */ AbstractIdleService this$0;

        private DelegateService(AbstractIdleService r1) {
            this.this$0 = r1;
        }

        /* synthetic */ DelegateService(AbstractIdleService r1, C09041 r2) {
            this(r1);
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            MoreExecutors.renamingDecorator(this.this$0.executor(), AbstractIdleService.access$200(this.this$0)).execute(new AbstractIdleService$DelegateService$$ExternalSyntheticLambda1(this));
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStop() {
            MoreExecutors.renamingDecorator(this.this$0.executor(), AbstractIdleService.access$200(this.this$0)).execute(new AbstractIdleService$DelegateService$$ExternalSyntheticLambda0(this));
        }

        /* JADX INFO: renamed from: lambda$doStart$0$com-google-common-util-concurrent-AbstractIdleService$DelegateService */
        /* synthetic */ void m382x2ed323e8() {
            this.this$0.startUp();     // Catch: Throwable -> L4
            notifyStarted();     // Catch: Throwable -> L4
            return;
        L4:
            th = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(th);
            notifyFailed(th);
        }

        /* JADX INFO: renamed from: lambda$doStop$1$com-google-common-util-concurrent-AbstractIdleService$DelegateService */
        /* synthetic */ void m383xb13e6319() {
            this.this$0.shutDown();     // Catch: Throwable -> L4
            notifyStopped();     // Catch: Throwable -> L4
            return;
        L4:
            th = move-exception;
            Platform.restoreInterruptIfIsInterruptedException(th);
            notifyFailed(th);
        }

        @Override // com.google.common.util.concurrent.AbstractService
        public String toString() {
            return this.this$0.toString();
        }
    }

    private final class ThreadNameSupplier implements Supplier<String> {
        final /* synthetic */ AbstractIdleService this$0;

        private ThreadNameSupplier(AbstractIdleService r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ThreadNameSupplier(AbstractIdleService r1, C09041 r2) {
            this(r1);
        }

        @Override // com.google.common.base.Supplier
        public /* bridge */ /* synthetic */ String get() {
            return get2();
        }

        @Override // com.google.common.base.Supplier
        /* JADX INFO: renamed from: get, reason: avoid collision after fix types in other method */
        public String get2() {
            return this.this$0.serviceName() + " " + this.this$0.state();
        }
    }

    protected AbstractIdleService() {
        C09041 r1 = null;
        this.threadNameSupplier = new ThreadNameSupplier(this, r1);
        this.delegate = new DelegateService(this, r1);
    }

    static /* synthetic */ Supplier access$200(AbstractIdleService r1) {
        return r1.threadNameSupplier;
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
        return new AbstractIdleService$$ExternalSyntheticLambda0(this);
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    /* JADX INFO: renamed from: lambda$executor$0$com-google-common-util-concurrent-AbstractIdleService */
    /* synthetic */ void m381xc998c392(Runnable r2) {
        MoreExecutors.newThread(this.threadNameSupplier.get(), r2).start();
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    protected abstract void shutDown() throws Exception;

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    protected abstract void startUp() throws Exception;

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
}
