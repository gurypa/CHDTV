package es.antonio.m3u;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
final class RecordingScheduler {
    RecordingScheduler() {
    }

    static PendingIntent alarm(Context r3, String r4) {
        return PendingIntent.getBroadcast(r3, 0, new Intent(r3, RecordingReceiver.class).setAction("es.antonio.m3u.RECORD").setData(Uri.parse("chdtv-record:" + r4)).putExtra("id", r4), 201326592);
    }

    static void cancel(Context r2, RecordingTask r3) throws IOException {
        ((AlarmManager) r2.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(alarm(r2, r3.f188id));
        r3.state = "Cancelada";
        r3.message = "Cancelada por el usuario";
        RecordingStore.put(r2, r3);
    }

    static boolean permitted(Context r2) {
        if (Build.VERSION.SDK_INT >= 31) goto L5;
    L8:
        return true;
    L5:
        if (((AlarmManager) r2.getSystemService(NotificationCompat.CATEGORY_ALARM)).canScheduleExactAlarms() == true) goto L8;
        return false;
    }

    static void schedule(Context r5, RecordingTask r6) throws IOException {
        if (permitted(r5) == false) goto L15;
        if (r6.start <= System.currentTimeMillis()) goto L13;
        RecordingSchedule.validate(Collections.singletonList(r6), RecordingStore.all(r5), System.currentTimeMillis());
        RecordingStore.put(r5, r6);
        ((AlarmManager) r5.getSystemService(NotificationCompat.CATEGORY_ALARM)).setExactAndAllowWhileIdle(0, r6.start, alarm(r5, r6.f188id));     // Catch: RuntimeException -> L9
        return;
    L9:
        e = move-exception;
        r6.state = "Error";
        r6.message = "Android no permitió programar la alarma";
        RecordingStore.put(r5, r6);
        throw e;
    L13:
        throw new IOException("La fecha de inicio ya ha pasado");
    L15:
        throw new IOException("Activa Alarmas y recordatorios para programar la grabación");
    }

    static synchronized void scheduleAll(Context r6, List<RecordingTask> r7) throws IOException {
        monitor-enter(RecordingScheduler.class);
        RecordingSchedule.validate(r7, RecordingStore.all(r6), System.currentTimeMillis());     // Catch: Throwable -> L28
        if (permitted(r6) == false) goto L27;
        ArrayList r1 = new ArrayList();     // Catch: Throwable -> L28
        Iterator<RecordingTask> r2 = r7.iterator();     // Catch: Throwable -> L14 IOException -> L16 Throwable -> L28
    L9:
        if (r2.hasNext() == false) goto L12;
        RecordingTask r3 = r2.next();     // Catch: Throwable -> L14 IOException -> L16 Throwable -> L28
        schedule(r6, r3);     // Catch: Throwable -> L14 IOException -> L16 Throwable -> L28
        r1.add(r3);     // Catch: Throwable -> L14 IOException -> L16 Throwable -> L28
        goto L9
    L12:
        monitor-exit(RecordingScheduler.class);
        return;
    L14:
        e = move-exception;
        Iterator r4 = r1.iterator();     // Catch: Throwable -> L28
    L19:
        if (r4.hasNext() == false) goto L25;
        cancel(r6, (RecordingTask) r4.next());     // Catch: Exception -> L23 Throwable -> L28
        goto L19
    L25:
        throw e;     // Catch: Throwable -> L28
    L27:
        throw new IOException("Activa Alarmas y recordatorios para programar la grabación");     // Catch: Throwable -> L28
    L28:
        th = move-exception;
        throw th;
    }

    static void start(Context r3, String r4) {
        Intent r0 = new Intent(r3, RecordingService.class).setAction("start").putExtra("id", r4);
        if (Build.VERSION.SDK_INT < 26) goto L5;
        r3.startForegroundService(r0);
        return;
    L5:
        r3.startService(r0);
    }
}
