package es.antonio.m3u;

import android.os.Looper;
import android.os.Process;

/* JADX INFO: loaded from: classes7.dex */
final class EpgChannelWork {
    private EpgChannelWork() {
    }

    static EpgData refresh(EpgRepository r3) {
        if (Looper.myLooper() != Looper.getMainLooper()) goto L27;
        return new EpgData();
    L27:
        int r1 = Process.getThreadPriority(Process.myTid());     // Catch: Exception -> L9
    L25:
        Process.setThreadPriority(0);     // Catch: Throwable -> L13 Exception -> L15
    L13:
        th = move-exception;
        Process.setThreadPriority(r1);     // Catch: Exception -> L23
    L24:
        throw th;
    L16:
        BlackBox.event("EPG_CHANNEL_WORK", "priority=normal worker=true");     // Catch: Throwable -> L13
        EpgData r4 = r3.refreshBackgroundOriginal();     // Catch: Throwable -> L13
        Process.setThreadPriority(r1);     // Catch: Exception -> L19
    L20:
        return r4;
    L10:
        r1 = 0;
        goto L25
    }
}
