package androidx.media3.common.util;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: loaded from: classes.dex */
public class SystemClock implements Clock {
    protected SystemClock() {
    }

    @Override // androidx.media3.common.util.Clock
    public HandlerWrapper createHandler(Looper r3, Handler.Callback r4) {
        return new SystemHandlerWrapper(new Handler(r3, r4));
    }

    @Override // androidx.media3.common.util.Clock
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override // androidx.media3.common.util.Clock
    public long elapsedRealtime() {
        return android.os.SystemClock.elapsedRealtime();
    }

    @Override // androidx.media3.common.util.Clock
    public long nanoTime() {
        return System.nanoTime();
    }

    @Override // androidx.media3.common.util.Clock
    public void onThreadBlocked() {
    }

    @Override // androidx.media3.common.util.Clock
    public long uptimeMillis() {
        return android.os.SystemClock.uptimeMillis();
    }
}
