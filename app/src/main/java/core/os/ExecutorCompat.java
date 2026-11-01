package androidx.core.os;

import android.os.Handler;
import androidx.core.util.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/* JADX INFO: loaded from: classes.dex */
public final class ExecutorCompat {

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

    private ExecutorCompat() {
    }

    public static Executor create(Handler r1) {
        return new HandlerExecutor(r1);
    }
}
