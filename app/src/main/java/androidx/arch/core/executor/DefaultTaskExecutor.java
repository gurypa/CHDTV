package androidx.arch.core.executor;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class DefaultTaskExecutor extends TaskExecutor {
    private final ExecutorService mDiskIO;
    private final Object mLock;
    private volatile Handler mMainHandler;

    public DefaultTaskExecutor() {
        this.mLock = new Object();
        this.mDiskIO = Executors.newFixedThreadPool(4, new ThreadFactoryC00341(this));
    }

    private static Handler createAsync(Looper r7) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return (Handler) Handler.class.getDeclaredConstructor(new Class[]{Looper.class, Handler.Callback.class, Boolean.TYPE}).newInstance(new Object[]{r7, null, true});
    L18:
        return new Handler(r7);
    L11:
        return new Handler(r7);
    L5:
        return Handler.createAsync(r7);
    }

    @Override // androidx.arch.core.executor.TaskExecutor
    public void executeOnDiskIO(Runnable r2) {
        this.mDiskIO.execute(r2);
    }

    @Override // androidx.arch.core.executor.TaskExecutor
    public boolean isMainThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.arch.core.executor.TaskExecutor
    public void postToMainThread(Runnable r3) {
        if (this.mMainHandler != null) goto L14;
        Object r0 = this.mLock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L7:
        if (this.mMainHandler != null) goto L9;
        this.mMainHandler = createAsync(Looper.getMainLooper());     // Catch: Throwable -> L11
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L11
    L14:
        this.mMainHandler.post(r3);
    }
}
