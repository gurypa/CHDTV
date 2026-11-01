package androidx.recyclerview.widget;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public final class AsyncDifferConfig<T> {
    private final Executor mBackgroundThreadExecutor;
    private final DiffUtil.ItemCallback<T> mDiffCallback;
    private final Executor mMainThreadExecutor;

    public static final class Builder<T> {
        private static Executor sDiffExecutor;
        private static final Object sExecutorLock = null;
        private Executor mBackgroundThreadExecutor;
        private final DiffUtil.ItemCallback<T> mDiffCallback;
        private Executor mMainThreadExecutor;

        static {
            sExecutorLock = new Object();
            sDiffExecutor = null;
        }

        public Builder(DiffUtil.ItemCallback<T> r1) {
            this.mDiffCallback = r1;
        }

        public AsyncDifferConfig<T> build() {
            if (this.mBackgroundThreadExecutor != null) goto L15;
            Object r0 = sExecutorLock;
            monitor-enter(r0);
        L11:
            th = move-exception;
            throw th;
        L7:
            if (sDiffExecutor != null) goto L9;
            sDiffExecutor = Executors.newFixedThreadPool(2);     // Catch: Throwable -> L11
        L9:
            monitor-exit(r0);     // Catch: Throwable -> L11
            this.mBackgroundThreadExecutor = sDiffExecutor;
        L15:
            return new AsyncDifferConfig(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
        }

        public Builder<T> setBackgroundThreadExecutor(Executor r1) {
            this.mBackgroundThreadExecutor = r1;
            return this;
        }

        public Builder<T> setMainThreadExecutor(Executor r1) {
            this.mMainThreadExecutor = r1;
            return this;
        }
    }

    AsyncDifferConfig(Executor r1, Executor r2, DiffUtil.ItemCallback<T> r3) {
        this.mMainThreadExecutor = r1;
        this.mBackgroundThreadExecutor = r2;
        this.mDiffCallback = r3;
    }

    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }

    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return this.mDiffCallback;
    }

    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }
}
