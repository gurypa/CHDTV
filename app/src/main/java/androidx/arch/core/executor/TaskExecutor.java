package androidx.arch.core.executor;

/* JADX INFO: loaded from: classes.dex */
public abstract class TaskExecutor {
    public TaskExecutor() {
    }

    public abstract void executeOnDiskIO(Runnable r1);

    public void executeOnMainThread(Runnable r2) {
        if (isMainThread() == false) goto L5;
        r2.run();
        return;
    L5:
        postToMainThread(r2);
    }

    public abstract boolean isMainThread();

    public abstract void postToMainThread(Runnable r1);
}
