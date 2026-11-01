package androidx.core.provider;

import android.os.Handler;
import android.os.Process;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
class RequestExecutor {

    private static class DefaultThreadFactory implements ThreadFactory {
        private int mPriority;
        private String mThreadName;

        private static class ProcessPriorityThread extends Thread {
            private final int mPriority;

            ProcessPriorityThread(Runnable r1, String r2, int r3) {
                super(r1, r2);
                this.mPriority = r3;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Process.setThreadPriority(this.mPriority);
                super.run();
            }
        }

        DefaultThreadFactory(String r1, int r2) {
            this.mThreadName = r1;
            this.mPriority = r2;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r4) {
            return new ProcessPriorityThread(r4, this.mThreadName, this.mPriority);
        }
    }

    private static class HandlerExecutor implements Executor {
        private final Handler mHandler;

        HandlerExecutor(Handler r2) {
            this.mHandler = (Handler) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable r4) {
            if (this.mHandler.post((Runnable) Preconditions.checkNotNull(r4)) == false) goto L6;
            return;
        L6:
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }

    private static class ReplyRunnable<T> implements Runnable {
        private Callable<T> mCallable;
        private Consumer<T> mConsumer;
        private Handler mHandler;

        ReplyRunnable(Handler r1, Callable<T> r2, Consumer<T> r3) {
            this.mCallable = r2;
            this.mConsumer = r3;
            this.mHandler = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            T r0 = this.mCallable.call();     // Catch: Exception -> L4
        L6:
            final T r1 = r0;
            final Consumer<T> r2 = this.mConsumer;
            this.mHandler.post(new RunnableC00881(this, r2, r1));
            return;
        L5:
            r0 = null;
            goto L6
        }
    }

    private RequestExecutor() {
    }

    static ThreadPoolExecutor createDefaultExecutor(String r9, int r10, int r11) {
        ThreadPoolExecutor r1 = new ThreadPoolExecutor(0, 1, r11, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(), new DefaultThreadFactory(r9, r10));
        r1.allowCoreThreadTimeOut(true);
        return r1;
    }

    static Executor createHandlerExecutor(Handler r1) {
        return new HandlerExecutor(r1);
    }

    static <T> void execute(Executor r2, Callable<T> r3, Consumer<T> r4) {
        r2.execute(new ReplyRunnable(CalleeHandler.create(), r3, r4));
    }

    static <T> T submit(ExecutorService r4, Callable<T> r5, int r6) throws InterruptedException {
        return r4.submit(r5).get(r6, TimeUnit.MILLISECONDS);
    L8:
        e = move-exception;
        throw e;
    L10:
        e = move-exception;
        throw new RuntimeException(e);
    L7:
        throw new InterruptedException("timeout");
    }
}
