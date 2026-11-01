package es.antonio.m3u;

import java.io.Serializable;
import java.util.UUID;

/* JADX INFO: loaded from: classes3.dex */
final class RecordingTask implements Serializable {
    static final String CANCELLED = "Cancelada";
    static final String COMPLETE = "Terminada";
    static final String FAILED = "Error";
    static final String PREPARING = "Preparando";
    static final String RUNNING = "Grabando";
    static final String SCHEDULED = "Programada";
    static final String STOPPED = "Detenida";
    private static final long serialVersionUID = 1;
    volatile long bytes;
    long end;
    Entry entry;
    String format;

    /* JADX INFO: renamed from: id */
    String f188id;
    volatile String message;
    volatile long recordedMillis;
    long start;
    volatile String state;
    String title;

    RecordingTask() {
        this.f188id = UUID.randomUUID().toString();
        this.title = "";
        this.format = "mp4";
        this.state = SCHEDULED;
        this.message = "";
    }

    boolean active() {
        if (PREPARING.equals(this.state) == false) goto L5;
    L8:
        return true;
    L5:
        if (RUNNING.equals(this.state) == true) goto L8;
        return false;
    }

    boolean playable() {
        if (COMPLETE.equals(this.state) == false) goto L5;
    L8:
        return true;
    L5:
        if (STOPPED.equals(this.state) == true) goto L8;
        return false;
    }
}
