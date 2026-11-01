package androidx.media3.exoplayer.util;

import androidx.media3.common.util.Consumer;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public interface ReleasableExecutor extends Executor {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.util.ReleasableExecutor$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static <T extends Executor> ReleasableExecutor from(final T r1, final Consumer<T> r2) {
            return new C03791(r1, r2);
        }
    }

    void release();
}
