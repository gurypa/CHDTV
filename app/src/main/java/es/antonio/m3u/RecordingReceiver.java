package es.antonio.m3u;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public final class RecordingReceiver extends BroadcastReceiver {
    public RecordingReceiver() {
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context r9, Intent r10) {
        if ("android.intent.action.BOOT_COMPLETED".equals(r10.getAction()) == false) goto L5;
    L50:
        Iterator<RecordingTask> r0 = RecordingStore.all(r9).iterator();     // Catch: Exception -> L45
    L30:
        if (r0.hasNext() == false) goto L66;
        RecordingTask r3 = r0.next();     // Catch: Exception -> L45
        if (r3.active() == true) goto L34;
        if ("Programada".equals(r3.state) == false) goto L30;
        if (r3.start <= System.currentTimeMillis()) goto L43;
        if (RecordingScheduler.permitted(r9) == false) goto L43;
        RecordingScheduler.schedule(r9, r3);     // Catch: Exception -> L45
    L43:
        r3.state = "Error";     // Catch: Exception -> L45
        r3.message = "No se pudo recuperar la programación; revisa la hora y el permiso de alarmas";     // Catch: Exception -> L45
    L35:
        RecordingStore.put(r9, r3);     // Catch: Exception -> L45
        goto L30
    L34:
        r3.state = "Error";     // Catch: Exception -> L45
        r3.message = "Grabación interrumpida al reiniciar el dispositivo";     // Catch: Exception -> L45
        goto L35
    L66:
        return;
    L46:
        return;
    L5:
        if ("android.intent.action.MY_PACKAGE_REPLACED".equals(r10.getAction()) == true) goto L50;
        String r1 = r10.getStringExtra("id");
        if (r1 != null) goto L49;
        return;
    L49:
        RecordingTask r4 = RecordingStore.get(r9, r1);     // Catch: Exception -> L22
        if (r4 != null) goto L13;
        return;
    L13:
        if ("Programada".equals(r4.state) == true) goto L16;
        return;
    L16:
        if (r4.end > System.currentTimeMillis()) goto L19;
        r4.state = "Error";     // Catch: Exception -> L22
        r4.message = "El intervalo de grabación ya terminó";     // Catch: Exception -> L22
        RecordingStore.put(r9, r4);     // Catch: Exception -> L22
        return;
    L19:
        RecordingScheduler.start(r9, r1);     // Catch: Exception -> L22
        return;
    L47:
        RecordingTask r5 = RecordingStore.get(r9, r1);     // Catch: Exception -> L27
        if (r5 == null) goto L63;
        r5.state = "Error";     // Catch: Exception -> L27
        r5.message = "Android no permitió iniciar la grabación en segundo plano";     // Catch: Exception -> L27
        RecordingStore.put(r9, r5);     // Catch: Exception -> L27
        return;
    L63:
        return;
    }
}
