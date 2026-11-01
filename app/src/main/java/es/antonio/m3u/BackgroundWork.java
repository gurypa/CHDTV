package es.antonio.m3u;

import android.app.Activity;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;

/* JADX INFO: loaded from: classes7.dex */
final class BackgroundWork {
    private static volatile long quietUntil;

    BackgroundWork() {
    }

    static boolean busy() {
        return busy(SystemClock.elapsedRealtime());
    }

    static boolean busy(long r2) {
        if (r2 >= quietUntil) goto L5;
        return true;
    L5:
        return false;
    }

    static EpgData guide(EpgRepository r2) {
        if (Looper.myLooper() == Looper.getMainLooper()) goto L5;
        int r0 = Process.getThreadPriority(Process.myTid());     // Catch: Exception -> L8
    L10:
        lowPriority();
        EpgData r3 = r2.refreshBackgroundOriginal();     // Catch: Throwable -> L16
        Process.setThreadPriority(r0);     // Catch: Exception -> L14
    L15:
        return r3;
    L16:
        th = move-exception;
        Process.setThreadPriority(r0);     // Catch: Exception -> L19
    L20:
        throw th;
    L9:
        r0 = 0;
        goto L10
    L5:
        return r2.cached();
    }

    static void lowPriority() {
        Process.setThreadPriority(10);     // Catch: Exception -> L5
        return;
    }

    static void playback() {
        reserve(SystemClock.elapsedRealtime(), 4000);
    }

    static EpgData refresh(EpgRepository r3) {
        if (Looper.myLooper() == Looper.getMainLooper()) goto L5;
        int r0 = Process.getThreadPriority(Process.myTid());     // Catch: Exception -> L8
    L10:
        lowPriority();
    L32:
    L27:
        th = move-exception;
        Process.setThreadPriority(r0);     // Catch: Exception -> L30
    L31:
        throw th;
    L12:
        if (busy() == false) goto L22;
        Thread.sleep(250);     // Catch: InterruptedException -> L16 Throwable -> L27
    L17:
        Thread.currentThread().interrupt();     // Catch: Throwable -> L27
        EpgData r4 = r3.cached();     // Catch: Throwable -> L27
        Process.setThreadPriority(r0);     // Catch: Exception -> L20
    L21:
        return r4;
    L22:
        EpgData r5 = r3.refreshBackgroundOriginal();     // Catch: Throwable -> L27
        Process.setThreadPriority(r0);     // Catch: Exception -> L25
    L26:
        return r5;
    L9:
        r0 = 0;
        goto L10
    L5:
        return r3.cached();
    }

    static synchronized void reserve(long r3, long r5) {
        monitor-enter(BackgroundWork.class);
        quietUntil = Math.max(quietUntil, r3 + r5);     // Catch: Throwable -> L7
        monitor-exit(BackgroundWork.class);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static void resumed(Activity r4) {
        if ((r4 instanceof MainActivity) == false) goto L5;
    L6:
        reserve(SystemClock.elapsedRealtime(), 8000);
        return;
    L5:
        if ((r4 instanceof PlayerActivity) == true) goto L6;
    }
}
