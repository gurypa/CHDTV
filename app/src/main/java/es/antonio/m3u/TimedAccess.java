package es.antonio.m3u;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes3.dex */
final class TimedAccess {
    static final String PREF = "timed-access-local";
    static volatile boolean ready;

    TimedAccess() {
    }

    static void activate(Context r2, long r3) throws IOException {
        if (r2.getSharedPreferences(PREF, 0).edit().putLong("expires", r3).commit() == false) goto L10;
        if (r3 <= now(r2)) goto L8;
        return;
    L8:
        throw new IOException("El código ha caducado");
    L10:
        throw new IOException("No se pudo guardar la caducidad");
    }

    static boolean allowed(Context r7) {
        if (limited(r7) == true) goto L5;
    L6:
        return true;
    L5:
        if (now(r7) < r7.getSharedPreferences(PREF, 0).getLong("expires", 0)) goto L6;
        return false;
    }

    static String description(Context r7) {
        return "Acceso hasta " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(r7.getSharedPreferences(PREF, 0).getLong("expires", 0)));
    }

    static /* synthetic */ void lambda$warm$0(Context r2) {
        r2.getSharedPreferences(PREF, 0).getAll();
        ready = true;
    }

    static boolean limited(Context r2) {
        return r2.getSharedPreferences(PREF, 0).contains("expires");
    }

    static synchronized long now(Context r18) {
        monitor-enter(TimedAccess.class);
        SharedPreferences r0 = r18.getSharedPreferences(PREF, 0);     // Catch: Throwable -> L17
        long r4 = System.currentTimeMillis();     // Catch: Throwable -> L17
        long r6 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L17
        long r8 = r0.getLong("elapsed", r6);     // Catch: Throwable -> L17
        long r10 = r0.getLong("floor", r4);     // Catch: Throwable -> L17
        if (r6 < r8) goto L7;
        long r12 = r6 - r8;     // Catch: Throwable -> L17
    L8:
        long r13 = Math.max(r4, r12 + r10);     // Catch: Throwable -> L17
        if (r0.contains("floor") == true) goto L11;
    L14:
        r0.edit().putLong("floor", r13).putLong("elapsed", r6).apply();     // Catch: Throwable -> L17
    L15:
        monitor-exit(TimedAccess.class);
        return r13;
    L11:
        if ((r13 - r10) >= 60000) goto L14;
        if (r6 >= r8) goto L15;
    L7:
        r12 = 0;
    L17:
        th = move-exception;
        throw th;
    }

    static void warm(Context r4) {
        final Context r0 = r4.getApplicationContext();
        new Thread(new TimedAccess$$ExternalSyntheticLambda0(r0), "CHDTVAccessCache").start();
    }
}
