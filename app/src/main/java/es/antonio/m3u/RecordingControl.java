package es.antonio.m3u;

import android.content.Context;

/* JADX INFO: loaded from: classes7.dex */
final class RecordingControl {
    RecordingControl() {
    }

    static void deletionFailed(Context r2, String r3) {
        r2.getSharedPreferences("recording-control-v187", 0).edit().putInt(r3, 1).apply();
    }

    static int request(Context r2, String r3) {
        if (r3 != null) goto L6;
        return 0;
    L6:
        return r2.getSharedPreferences("recording-control-v187", 0).getInt(r3, 0);
    }

    static synchronized void request(Context r3, String r4, boolean r5) {
        monitor-enter(RecordingControl.class);
        if (r4 != null) goto L7;
        monitor-exit(RecordingControl.class);
        return;
    L7:
        if (r5 == false) goto L9;
        int r6 = 2;
    L19:
    L16:
        th = move-exception;
        throw th;
    L11:
        if (request(r3, r4) <= r6) goto L14;
    L12:
        monitor-exit(RecordingControl.class);
        return;
    L14:
        r3.getSharedPreferences("recording-control-v187", 0).edit().putInt(r4, r6).apply();     // Catch: Throwable -> L16
        goto L12
    L9:
        r6 = 1;
        goto L19
    }
}
