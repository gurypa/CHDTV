package es.antonio.m3u;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
final class RecordingSchedule {
    RecordingSchedule() {
    }

    static void validate(List<RecordingTask> r9, List<RecordingTask> r10, long r11) throws IOException {
        if (r9.isEmpty() == true) goto L38;
        ArrayList r0 = new ArrayList();
        Iterator<RecordingTask> r1 = r10.iterator();
    L6:
        if (r1.hasNext() == false) goto L12;
        RecordingTask r2 = r1.next();
        if (r2.active() == true) goto L11;
        if ("Programada".equals(r2.state) == false) goto L6;
    L11:
        r0.add(r2);
        goto L6
    L12:
        Iterator<RecordingTask> r3 = r9.iterator();
    L14:
        if (r3.hasNext() == false) goto L36;
        RecordingTask r4 = r3.next();
        if (r4.start <= r11) goto L35;
        if (r4.end <= r4.start) goto L35;
        if ((r4.end - r4.start) > 21600000) goto L35;
        Iterator r5 = r0.iterator();
    L23:
        if (r5.hasNext() == false) goto L33;
        RecordingTask r6 = (RecordingTask) r5.next();
        if (r6.f188id.equals(r4.f188id) == true) goto L23;
        if (r4.start >= r6.end) goto L23;
        if (r6.start >= r4.end) goto L23;
        throw new IOException("Se solapa con «" + r6.title + "». Sólo hay una grabación simultánea");
    L33:
        r0.add(r4);
    L35:
        throw new IOException("El programa debe ser futuro y durar entre 1 y 360 minutos");
    L36:
        return;
    L38:
        throw new IOException("Selecciona al menos un programa");
    }
}
