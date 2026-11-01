package androidx.arch.core.executor;

import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class ArchTaskExecutor extends TaskExecutor {
    private static final Executor sIOThreadExecutor = null;
    private static volatile ArchTaskExecutor sInstance;
    private static final Executor sMainThreadExecutor = null;
    private TaskExecutor mDefaultTaskExecutor;
    private TaskExecutor mDelegate;

    static {
        sMainThreadExecutor = new ExecutorC00321();
        sIOThreadExecutor = new ExecutorC00332();
    }

    private ArchTaskExecutor() {
        this.mDefaultTaskExecutor = new DefaultTaskExecutor();
        this.mDelegate = this.mDefaultTaskExecutor;
    }

    public static Executor getIOThreadExecutor() {
        return sIOThreadExecutor;
    }

    public static ArchTaskExecutor getInstance() {
        if (sInstance == null) goto L7;
        return sInstance;
    L7:
        monitor-enter(ArchTaskExecutor.class);
    L14:
        th = move-exception;
        throw th;
    L9:
        if (sInstance != null) goto L11;
        sInstance = new ArchTaskExecutor();     // Catch: Throwable -> L14
    L11:
        monitor-exit(ArchTaskExecutor.class);     // Catch: Throwable -> L14
        return sInstance;
    }

    public static Executor getMainThreadExecutor() {
        return sMainThreadExecutor;
    }

    @Override // androidx.arch.core.executor.TaskExecutor
    public void executeOnDiskIO(Runnable r2) {
        this.mDelegate.executeOnDiskIO(r2);
    }

    @Override // androidx.arch.core.executor.TaskExecutor
    public boolean isMainThread() {
        return this.mDelegate.isMainThread();
    }

    @Override // androidx.arch.core.executor.TaskExecutor
    public void postToMainThread(Runnable r2) {
        this.mDelegate.postToMainThread(r2);
    }

    public void setDelegate(TaskExecutor r2) {
        if (r2 != null) goto L4;
        TaskExecutor r0 = this.mDefaultTaskExecutor;
    L5:
        this.mDelegate = r0;
        return;
    L4:
        r0 = r2;
        goto L5
    }
}
