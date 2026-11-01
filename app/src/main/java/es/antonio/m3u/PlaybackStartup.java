package es.antonio.m3u;

import android.os.Build;

/* JADX INFO: loaded from: classes3.dex */
final class PlaybackStartup {
    private String decoder;
    private String error;
    private long firstFrame;
    private long firstManifestDuration;
    private long lastManifestAt;
    private long lastManifestDuration;
    private long manifestBytes;
    private int manifests;
    private String phase;
    private long requested;
    private int segments;
    private long started;
    private long totalFirstFrame;

    PlaybackStartup() {
        this.firstManifestDuration = -1;
        this.firstFrame = -1;
        this.totalFirstFrame = -1;
        this.phase = "Sin iniciar";
        this.decoder = "Sin inicializar";
        this.error = "";
    }

    synchronized void completed(int r7, long r8, long r10, long r12) {
        monitor-enter(this);
        if (r7 != 4) goto L14;
        this.manifestBytes += r8;     // Catch: Throwable -> L12
        this.manifests++;     // Catch: Throwable -> L12
        if (this.firstManifestDuration >= 0) goto L8;
        this.firstManifestDuration = r10;     // Catch: Throwable -> L12
    L8:
        this.lastManifestDuration = r10;     // Catch: Throwable -> L12
        this.lastManifestAt = r12 - this.started;     // Catch: Throwable -> L12
        if (this.firstFrame >= 0) goto L20;
        String r0 = "Manifiesto recibido";
    L11:
        this.phase = r0;     // Catch: Throwable -> L12
    L20:
        monitor-exit(this);
        return;
    L14:
        if (r7 != 1) goto L20;
        this.segments++;     // Catch: Throwable -> L12
        if (this.firstFrame >= 0) goto L20;
        r0 = "Contenido recibido";
    L12:
        th = move-exception;
        throw th;
    }

    synchronized void decoder(String r5) {
        monitor-enter(this);
        this.decoder = r5;     // Catch: Throwable -> L8
        if (this.firstFrame >= 0) goto L6;
        this.phase = "Decodificador inicializado";     // Catch: Throwable -> L8
    L6:
        monitor-exit(this);
        return;
    L8:
        th = move-exception;
        throw th;
    }

    synchronized void failed(Exception r2) {
        monitor-enter(this);
        this.error = r2.getClass().getSimpleName();     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    synchronized void frame(long r5) {
        monitor-enter(this);
    L9:
        th = move-exception;
        throw th;
    L4:
        if (this.firstFrame >= 0) goto L6;
        this.firstFrame = r5 - this.started;     // Catch: Throwable -> L9
        this.totalFirstFrame = r5 - this.requested;     // Catch: Throwable -> L9
    L6:
        this.phase = "Reproduciendo";     // Catch: Throwable -> L9
        monitor-exit(this);
    }

    synchronized void loading(int r5) {
        monitor-enter(this);
    L12:
        th = move-exception;
        throw th;
    L4:
        if (this.firstFrame < 0) goto L6;
    L10:
        monitor-exit(this);
        return;
    L6:
        if (r5 != 4) goto L8;
        String r0 = "Descargando manifiesto";
    L9:
        this.phase = r0;     // Catch: Throwable -> L12
        goto L10
    L8:
        r0 = "Descargando contenido";
        goto L9
    }

    synchronized String report(long r8) {
        monitor-enter(this);
        StringBuilder r0 = new StringBuilder().append("CHDTV 2.27-sampleaes-tv-mobile-test\nAndroid ").append(Build.VERSION.RELEASE).append(" · ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\nEstado: ").append(this.phase).append("\nTiempo desde solicitud: ").append(Math.max(0, r8 - this.requested)).append(" ms\nAntes de preparar: ").append(Math.max(0, this.started - this.requested)).append(" ms\nPrimera imagen desde solicitud: ");     // Catch: Throwable -> L22
        if (this.totalFirstFrame >= 0) goto L6;
        String r1 = "pendiente";
    L7:
        StringBuilder r2 = r0.append(r1).append("\nPrimera imagen desde preparación: ");     // Catch: Throwable -> L22
        if (this.firstFrame >= 0) goto L10;
        String r3 = "pendiente";
    L11:
        StringBuilder r4 = r2.append(r3).append("\nPrimera descarga de manifiesto: ");     // Catch: Throwable -> L22
        if (this.firstManifestDuration >= 0) goto L14;
        String r5 = "pendiente";
    L15:
        StringBuilder r6 = r4.append(r5).append("\nÚltima descarga de manifiesto: ").append(this.lastManifestDuration).append(" ms · recibida a ").append(this.lastManifestAt).append(" ms\nManifiestos: ").append(this.manifests).append(" · ").append(this.manifestBytes).append(" bytes\nSegmentos recibidos: ").append(this.segments).append("\nVídeo: ").append(this.decoder);     // Catch: Throwable -> L22
        if (this.error.isEmpty() == false) goto L18;
        String r7 = "";
    L19:
        String r9 = r6.append(r7).toString();     // Catch: Throwable -> L22
        monitor-exit(this);
        return r9;
    L18:
        r7 = "\nÚltimo error de carga: " + this.error;     // Catch: Throwable -> L22
        goto L19
    L14:
        r5 = this.firstManifestDuration + " ms";     // Catch: Throwable -> L22
        goto L15
    L10:
        r3 = this.firstFrame + " ms";     // Catch: Throwable -> L22
        goto L11
    L6:
        r1 = this.totalFirstFrame + " ms";     // Catch: Throwable -> L22
    L22:
        th = move-exception;
        throw th;
    }

    synchronized void requested(long r1) {
        monitor-enter(this);
        this.requested = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    synchronized void reset(long r5) {
        monitor-enter(this);
        this.started = r5;     // Catch: Throwable -> L11
        if (this.requested > 0) goto L6;
    L7:
        this.requested = r5;     // Catch: Throwable -> L11
    L8:
        this.manifests = 0;     // Catch: Throwable -> L11
        this.segments = 0;     // Catch: Throwable -> L11
        long r0 = 0;
        this.lastManifestAt = r0;     // Catch: Throwable -> L11
        this.lastManifestDuration = r0;     // Catch: Throwable -> L11
        this.manifestBytes = r0;     // Catch: Throwable -> L11
        this.totalFirstFrame = -1;     // Catch: Throwable -> L11
        this.firstFrame = -1;     // Catch: Throwable -> L11
        this.firstManifestDuration = -1;     // Catch: Throwable -> L11
        this.phase = "Preparando fuente";     // Catch: Throwable -> L11
        this.decoder = "Sin inicializar";     // Catch: Throwable -> L11
        this.error = "";     // Catch: Throwable -> L11
        monitor-exit(this);
        return;
    L6:
        if (this.requested <= r5) goto L8;
    L11:
        th = move-exception;
        throw th;
    }

    synchronized void state(int r2) {
        monitor-enter(this);
        if (r2 != 2) goto L9;
        this.phase = "Almacenando vídeo en búfer";     // Catch: Throwable -> L6
    L19:
    L17:
        monitor-exit(this);
        return;
    L9:
        if (r2 != 3) goto L13;
        String r0 = "Listo para reproducir";
    L11:
        this.phase = r0;     // Catch: Throwable -> L6
        goto L19
    L13:
        if (r2 != 4) goto L17;
        r0 = "Finalizado";
    L6:
        th = move-exception;
        throw th;
    }
}
