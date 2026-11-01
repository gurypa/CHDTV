package es.antonio.m3u;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.FFprobeKit;
import com.arthenica.ffmpegkit.Log;
import com.arthenica.ffmpegkit.MediaInformationSession;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.SessionState;
import com.arthenica.ffmpegkit.Statistics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/* JADX INFO: loaded from: classes7.dex */
public final class RecordingService extends Service {
    private static final String CHANNEL = "chdtv-recording";
    private static final int NOTIFICATION = 48142;
    private volatile boolean completionStarted;
    private final Set<String> engineWarnings;
    private volatile String finishNote;
    private final Handler main;
    private final Runnable monitor;
    private volatile FFmpegSession nativeSession;
    private volatile RecordingProxy proxy;
    private String queuedId;
    private volatile long session;
    private volatile boolean stopFallbackScheduled;
    private volatile String stopReason;
    private volatile boolean stopped;
    private volatile RecordingTask task;
    private PowerManager.WakeLock wake;
    private final ExecutorService worker;

    public RecordingService() {
        this.worker = Executors.newSingleThreadExecutor();
        this.main = new Handler(Looper.getMainLooper());
        this.session = -1;
        this.engineWarnings = Collections.synchronizedSet(new HashSet());
        this.stopReason = "";
        this.finishNote = "Grabación finalizada";
        this.monitor = new RunnableC10301(this);
    }

    static /* synthetic */ RecordingTask access$0(RecordingService r0) {
        return r0.task;
    }

    static /* synthetic */ boolean access$1(RecordingService r0) {
        return r0.completionStarted;
    }

    static /* synthetic */ void access$2(RecordingService r0, RecordingTask r1) {
        r0.pollCompletion(r1);
    }

    static /* synthetic */ boolean access$3(RecordingService r0) {
        return r0.stopped;
    }

    static /* synthetic */ FFmpegSession access$4(RecordingService r0) {
        return r0.nativeSession;
    }

    static /* synthetic */ String access$5(FFmpegSession r0) {
        return engineState(r0);
    }

    static /* synthetic */ RecordingProxy access$6(RecordingService r0) {
        return r0.proxy;
    }

    static /* synthetic */ void access$7(RecordingService r0, String r1) {
        r0.stop(r1);
    }

    static /* synthetic */ void access$8(RecordingService r0, String r1) {
        r0.notifyState(r1);
    }

    static /* synthetic */ Handler access$9(RecordingService r0) {
        return r0.main;
    }

    private void begin(String r12) {
        String r0 = "Detenida por el usuario";
        String r1 = "Error";
        final RecordingTask r4 = RecordingStore.get(this, r12);     // Catch: Throwable -> L55
    L53:
        th = th;
    L58:
        if (RecordingNative.fatal(th) == true) goto L80;
        RecordingNative.failure("engine", th);
        if (r4 != null) goto L81;
    L74:
        if (r4 != null) goto L76;
        String r13 = "No se pudo iniciar la grabación";
    L77:
        this.finishNote = r13;
        this.task = null;
        this.session = -1;
        this.main.removeCallbacks(this.monitor);
        closeProxy();
        finishService();
        return;
    L76:
        r13 = r4.message;
        goto L77
    L81:
        if (RecordingControl.request(this, r4.f188id) != 1) goto L64;
        r1 = "Detenida";
    L64:
        r4.state = r1;     // Catch: Exception -> L73
        if (RecordingControl.request(this, r4.f188id) != 1) goto L68;
    L71:
        r4.message = r0;     // Catch: Exception -> L73
        RecordingStore.put(this, r4);     // Catch: Exception -> L73
        goto L74
    L68:
        if ((th instanceof IOException) == false) goto L70;
        r0 = th.getMessage();     // Catch: Exception -> L73
        goto L71
    L70:
        r0 = "No se pudo iniciar el motor de grabación";
        goto L71
    L80:
        throw th;
    L5:
        if (RecordingControl.request(this, r12) != 0) goto L7;
        if (r4 == null) goto L50;
        if ("Programada".equals(r4.state) == false) goto L50;
        if (this.task == null) goto L21;
        if (System.currentTimeMillis() < this.task.end) goto L21;
        this.queuedId = r12;     // Catch: Throwable -> L53
        stop("Duración cumplida");     // Catch: Throwable -> L53
        return;
    L21:
        if (this.task == null) goto L24;
        r4.state = "Error";     // Catch: Throwable -> L53
        r4.message = "Ya hay otra grabación en curso";     // Catch: Throwable -> L53
        RecordingStore.put(this, r4);     // Catch: Throwable -> L53
        return;
    L24:
        Iterator<RecordingTask> r5 = RecordingStore.all(this).iterator();     // Catch: Throwable -> L53
    L26:
        if (r5.hasNext() == false) goto L28;
        RecordingTask r6 = r5.next();     // Catch: Throwable -> L53
        if (r6.active() == false) goto L26;
        r6.state = "Error";     // Catch: Throwable -> L53
        r6.message = "Grabación interrumpida; el servicio se cerró";     // Catch: Throwable -> L53
        RecordingStore.put(this, r6);     // Catch: Throwable -> L53
        goto L26
    L28:
        if (TimedAccess.allowed(this) == false) goto L94;
        String r7 = RecordingCommand.limitation(r4.entry);     // Catch: Throwable -> L53
        if (r7.isEmpty() == false) goto L93;
        if (r4.end <= System.currentTimeMillis()) goto L92;
        if (RecordingStore.folder(this).getUsableSpace() < 134217728) goto L42;
        this.wake = ((PowerManager) getSystemService("power")).newWakeLock(1, "CHDTV:recording");     // Catch: Throwable -> L53
        this.wake.acquire(Math.min(21660000, Math.max(60000, (r4.end - System.currentTimeMillis()) + 60000)));     // Catch: Throwable -> L53
        this.task = r4;     // Catch: Throwable -> L53
        this.stopped = false;     // Catch: Throwable -> L53
        this.stopReason = "";     // Catch: Throwable -> L53
        this.completionStarted = false;     // Catch: Throwable -> L53
        this.stopFallbackScheduled = false;     // Catch: Throwable -> L53
        this.nativeSession = null;     // Catch: Throwable -> L53
        this.engineWarnings.clear();     // Catch: Throwable -> L53
        r4.state = "Preparando";     // Catch: Throwable -> L53
        RecordingStore.put(this, r4);     // Catch: Throwable -> L53
        this.proxy = new RecordingProxy(this, r4.entry);     // Catch: Throwable -> L53
        File r8 = RecordingStore.file(this, r4);     // Catch: Throwable -> L53
        RecordingNative.configure(this);     // Catch: Throwable -> L53
        BlackBox.event("RECORDING_START", "format=" + r4.format);     // Catch: Throwable -> L53
        FFmpegSession r9 = FFmpegKit.executeWithArgumentsAsync(RecordingCommand.arguments(r4, this.proxy, r8, r4.end - System.currentTimeMillis()), new RecordingService$$ExternalSyntheticLambda2(this, r4), new RecordingService$$ExternalSyntheticLambda3(this, r4), new RecordingService$$ExternalSyntheticLambda4(this, r4));     // Catch: Throwable -> L53
        this.nativeSession = r9;     // Catch: Throwable -> L53
        this.session = r9.getSessionId();     // Catch: Throwable -> L53
        BlackBox.event("RECORDING_SESSION_ASSIGNED", "engine_state=" + engineState(r9));     // Catch: Throwable -> L53
        if (this.stopped == false) goto L38;
    L39:
        stop("Detenida por el usuario");     // Catch: Throwable -> L53
    L40:
        this.main.post(new RecordingService$$ExternalSyntheticLambda5(this, r4));     // Catch: Throwable -> L53
        return;
    L38:
        if (RecordingControl.request(this, r12) == 0) goto L40;
    L42:
        throw new IOException("Necesitas al menos 128 MB libres para iniciar");     // Catch: Throwable -> L53
    L92:
        throw new IOException("El intervalo de grabación ya terminó");     // Catch: Throwable -> L53
    L93:
        throw new IOException(r7);     // Catch: Throwable -> L53
    L94:
        throw new IOException("El acceso temporal ya terminó");     // Catch: Throwable -> L53
    L50:
        if (this.task != null) goto L95;
        finishService();     // Catch: Throwable -> L53
        return;
    L95:
        return;
    L7:
        if (this.task != null) goto L91;
        finishService();     // Catch: Throwable -> L53
        return;
    L91:
        return;
    L55:
        th = th;
        r4 = null;
        goto L58
    }

    private void closeProxy() {
        RecordingProxy r0 = this.proxy;
        this.proxy = null;
        if (r0 == null) goto L11;
        r0.close();     // Catch: Exception -> L6
        return;
    L6:
        e = move-exception;
        BlackBox.event("RECORDING_CLOSE_ERROR", e.getClass().getSimpleName());
        return;
    }

    private void complete(RecordingTask r18, FFmpegSession r19) {
        if (this.task == r18) goto L5;
        return;
    L5:
        this.main.removeCallbacks(this.monitor);
        BlackBox.event("RECORDING_ENGINE_DONE", "success=" + ReturnCode.isSuccess(r19.getReturnCode()) + " stopped=" + this.stopped);
        this.session = -1;
        this.nativeSession = null;
        if (RecordingControl.request(this, r18.f188id) == 2) goto L113;
        boolean r7 = true;
        if (this.stopped == false) goto L27;
    L118:
        File r3 = RecordingStore.file(this, r18);     // Catch: Throwable -> L89
        r18.bytes = r3.length();     // Catch: Throwable -> L89
        if (this.stopped == false) goto L35;
        r18.message = "Preparando el archivo grabado…";     // Catch: Throwable -> L89
        RecordingStore.put(this, r18);     // Catch: Throwable -> L89
    L35:
        if (r18.bytes > 0) goto L37;
    L39:
        BlackBox.event("RECORDING_VALIDATE_START", "bytes=" + r18.bytes);     // Catch: Throwable -> L89
        if (r18.bytes <= 0) goto L42;
        MediaInformationSession r8 = FFprobeKit.getMediaInformation(r3.getAbsolutePath());     // Catch: Throwable -> L89
    L43:
        int r12 = 0;
        if (r8 != null) goto L46;
    L54:
        boolean r13 = false;
    L55:
        BlackBox.event("RECORDING_PROBE_DONE", "valid=" + r13);     // Catch: Throwable -> L89
        if (r13 == true) goto L58;
    L64:
        BlackBox.event("RECORDING_DECODE_DONE", "valid=" + r13);     // Catch: Throwable -> L89
        boolean r0 = ReturnCode.isSuccess(r19.getReturnCode());     // Catch: Throwable -> L89
        if (r13 == false) goto L79;
        if (r0 == true) goto L70;
        if (this.stopped == false) goto L79;
    L70:
        if (this.stopped == true) goto L72;
    L74:
        String r1 = "Terminada";
    L75:
        r18.state = r1;     // Catch: Throwable -> L89
        if (this.stopped == false) goto L78;
        String r2 = this.stopReason;     // Catch: Throwable -> L89
    L87:
        r18.message = r2;     // Catch: Throwable -> L89
        RecordingStore.put(this, r18);     // Catch: Throwable -> L89
    L101:
        BlackBox.event("RECORDING_FINALIZED", "state=" + r18.state + " bytes=" + r18.bytes);
        this.finishNote = String.valueOf(r18.title) + " · " + r18.state;
        this.task = null;
        this.session = -1;
        closeProxy();
        if (this.queuedId == null) goto L109;
        String r4 = this.queuedId;
        this.queuedId = null;
        this.main.removeCallbacks(this.monitor);
        if (this.wake != null) goto L106;
    L108:
        begin(r4);
        return;
    L106:
        if (this.wake.isHeld() == false) goto L108;
        this.wake.release();
        goto L108
    L109:
        finishService();
        return;
    L78:
        r2 = "Grabación guardada";
        goto L87
    L72:
        if ("Duración cumplida".equals(this.stopReason) == true) goto L74;
        r1 = "Detenida";
    L79:
        r18.state = "Error";     // Catch: Throwable -> L89
        if (this.proxy == null) goto L84;
        r12 = this.proxy.lastStatus;     // Catch: Throwable -> L89
    L84:
        if (r12 < 400) goto L86;
        r2 = "El proveedor respondió HTTP " + r12;     // Catch: Throwable -> L89
        goto L87
    L86:
        r2 = "El motor no pudo generar un vídeo reproducible. Prueba MKV o revisa el cifrado de la fuente.";
        goto L87
    L58:
        if (decodes(r3, 0.0d) == true) goto L60;
    L62:
        r7 = false;
    L63:
        r13 = r7;
        goto L64
    L60:
        if (decodes(r3, Math.max(0.0d, duration(r8.getMediaInformation().getDuration()) - 2.0d)) == false) goto L62;
    L46:
        if (ReturnCode.isSuccess(r8.getReturnCode()) == false) goto L54;
        if (r8.getMediaInformation() == null) goto L54;
        if (r8.getMediaInformation().getStreams().isEmpty() == true) goto L54;
        if (duration(r8.getMediaInformation().getDuration()) <= 0.0d) goto L54;
        r13 = true;
        goto L55
    L42:
        r8 = null;
        goto L43
    L37:
        if ("mp4".equals(r18.format) == false) goto L39;
        RecordingMedia.ensureSeekable(r3);     // Catch: Throwable -> L89
        r18.bytes = r3.length();     // Catch: Throwable -> L89
    L89:
        th = move-exception;
        if (RecordingNative.fatal(th) == true) goto L112;
        RecordingNative.failure("engine", th);
        r18.state = "Error";
        if ((th instanceof IOException) == false) goto L95;
        String r5 = th.getMessage();
    L96:
        r18.message = r5;
        RecordingStore.put(this, r18);     // Catch: Exception -> L99
        goto L101
    L95:
        r5 = "No se pudo validar el archivo grabado";
        goto L96
    L112:
        throw th;
    L27:
        if (this.proxy == null) goto L118;
        if (this.proxy.manifestFailure.isEmpty() == true) goto L118;
        this.stopped = true;
        this.stopReason = this.proxy.manifestFailure;
        goto L118
    L113:
        File r6 = RecordingStore.file(this, r18);     // Catch: Exception -> L16
        if (r6.exists() == true) goto L10;
    L14:
        RecordingStore.remove(this, r18.f188id);     // Catch: Exception -> L16
        this.finishNote = "Grabación eliminada";     // Catch: Exception -> L16
    L22:
        this.task = null;
        this.session = -1;
        closeProxy();
        finishService();
        return;
    L10:
        if (r6.delete() == true) goto L14;
        throw new IOException("No se pudo borrar el archivo");     // Catch: Exception -> L16
    L16:
        e = move-exception;
        r18.state = "Error";
        r18.message = "No se pudo borrar el archivo";
        RecordingControl.deletionFailed(this, r18.f188id);
        RecordingStore.put(this, r18);     // Catch: Exception -> L20
    L21:
        BlackBox.event("RECORDING_DELETE_ERROR", e.getClass().getSimpleName());
        goto L22
    }

    private static boolean decodes(File r13, double r14) {
        ArrayList r1 = new ArrayList(Arrays.asList(new String[]{"-hide_banner", "-loglevel", "error", "-nostdin", "-xerror"}));     // Catch: Throwable -> L8
        if (r14 <= 0.0d) goto L6;
        r1.addAll(Arrays.asList(new String[]{"-ss", String.format(Locale.ROOT, "%.3f", new Object[]{Double.valueOf(r14)})}));     // Catch: Throwable -> L8
    L6:
        r1.addAll(Arrays.asList(new String[]{"-i", r13.getAbsolutePath(), "-map", "0:v:0?", "-map", "0:a:0?", "-t", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, "-f", "null", "-"}));     // Catch: Throwable -> L8
        return ReturnCode.isSuccess(FFmpegKit.executeWithArguments((String[]) r1.toArray(new String[0])).getReturnCode());
    L8:
        th = move-exception;
        if (RecordingNative.fatal(th) == true) goto L14;
        RecordingNative.failure("decode_check", th);
        return false;
    L14:
        throw th;
    }

    private static double duration(String r2) {
        return Double.parseDouble(r2);
    L5:
        return 0.0d;
    }

    private static String engineState(FFmpegSession r0) {
        if (r0 != null) goto L5;
        return "none";
    L5:
        return String.valueOf(r0.getState());
    }

    private void engineWarning(RecordingTask r2, String r3) {
        if (this.task != r2) goto L35;
        if (r3 == null) goto L37;
        String r4 = r3.toLowerCase(Locale.ROOT);
        if (r4.contains("unrecognized option") == false) goto L9;
    L30:
        String r5 = "unsupported_option";
    L32:
        if (this.engineWarnings.add(r5) == false) goto L36;
        BlackBox.event("RECORDING_ENGINE_WARNING", "kind=" + r5);
        return;
    L36:
        return;
    L9:
        if (r4.contains("option not found") == true) goto L30;
        if (r4.contains("failed to open fragment") == false) goto L15;
        r5 = "fragment_open";
        goto L32
    L15:
        if (r4.contains("connection refused") == false) goto L18;
        r5 = "connection_refused";
        goto L32
    L18:
        if (r4.contains("timed out") == false) goto L21;
        r5 = "network_timeout";
        goto L32
    L21:
        if (r4.contains("invalid data") == false) goto L24;
        r5 = "invalid_media";
        goto L32
    L24:
        if (r4.contains("decrypt") == false) goto L27;
        r5 = "decryption";
        goto L32
    L27:
        if (r4.contains("server returned") == false) goto L29;
        r5 = "http_error";
        goto L32
    L29:
        r5 = "other";
        goto L32
    L37:
        return;
    }

    private void finishService() {
        this.main.post(new RecordingService$$ExternalSyntheticLambda0(this));
    }

    private Notification notification(String r6) {
        boolean r1 = false;
        PendingIntent r0 = PendingIntent.getActivity(this, 0, new Intent(this, RecordingsActivity.class), 201326592);
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Notification.Builder r3 = new Notification.Builder(this, CHANNEL);
    L6:
        Notification.Builder r7 = r3.setContentTitle("CHDTV · Grabación").setContentText(r6).setSmallIcon(android.R.drawable.stat_sys_download).setContentIntent(r0);
        if (this.task == null) goto L9;
        r1 = true;
    L9:
        r7.setOngoing(r1).setOnlyAlertOnce(true);
        if (this.task == null) goto L13;
        r3.addAction(new Notification.Action.Builder(android.R.drawable.ic_media_pause, "Detener", PendingIntent.getService(this, 1, new Intent(this, RecordingService.class).setAction("stop").putExtra("id", this.task.f188id), 201326592)).build());
    L13:
        return r3.build();
    L5:
        r3 = new Notification.Builder(this);
        goto L6
    }

    private void notifyState(String r3) {
        ((NotificationManager) getSystemService("notification")).notify(NOTIFICATION, notification(r3));
    }

    private void pollCompletion(RecordingTask r3) {
        FFmpegSession r0 = this.nativeSession;
        if (r0 != null) goto L5;
        return;
    L5:
        if (terminal(r0) == false) goto L9;
        queueComplete(r3, r0);
        return;
    }

    private synchronized void queueComplete(final RecordingTask r4, final FFmpegSession r5) {
        monitor-enter(this);
    L15:
        th = move-exception;
        throw th;
    L4:
        if (this.task == r4) goto L6;
    L13:
        monitor-exit(this);
        return;
    L6:
        if (this.completionStarted == true) goto L13;
        this.completionStarted = true;     // Catch: Throwable -> L15
        BlackBox.event("RECORDING_COMPLETION_RECEIVED", "engine_state=" + engineState(r5));     // Catch: Throwable -> L15
        this.worker.execute(new RecordingService$$ExternalSyntheticLambda7(this, r4, r5));     // Catch: RejectedExecutionException -> L11 Throwable -> L15
    L12:
        BlackBox.event("RECORDING_COMPLETION_REJECTED", "service_closed=true");     // Catch: Throwable -> L15
        goto L13
    }

    private synchronized void stop(String r8) {
        monitor-enter(this);
    L48:
        th = move-exception;
        throw th;
    L4:
        if (this.stopped == true) goto L6;
    L7:
        this.stopReason = r8;     // Catch: Throwable -> L48
    L8:
        this.stopped = true;     // Catch: Throwable -> L48
        final RecordingTask r0 = this.task;     // Catch: Throwable -> L48
        if (r0 == null) goto L52;
        if (this.completionStarted == false) goto L13;
        String r1 = "Validando grabación…";
    L14:
        r0.message = r1;     // Catch: Throwable -> L48
        goto L52
    L13:
        r1 = "Deteniendo grabación…";
    L52:
    L23:
        th = move-exception;
    L45:
        th = move-exception;
        closeProxy();     // Catch: Throwable -> L48
        throw th;     // Catch: Throwable -> L48
    L25:
        if (RecordingNative.fatal(th) == true) goto L44;
        RecordingNative.failure("cancel", th);     // Catch: Throwable -> L45
    L21:
        closeProxy();     // Catch: Throwable -> L48
        StringBuilder r4 = new StringBuilder("session_assigned=");     // Catch: Throwable -> L48
        if (this.session < 0) goto L31;
        boolean r2 = true;
    L32:
        BlackBox.event("RECORDING_STOP_REQUEST", r4.append(r2).append(" completing=").append(this.completionStarted).toString());     // Catch: Throwable -> L48
        if (r0 != null) goto L35;
    L41:
        monitor-exit(this);
        return;
    L35:
        if (this.nativeSession == null) goto L41;
        if (this.completionStarted == true) goto L41;
        if (this.stopFallbackScheduled == true) goto L41;
        this.stopFallbackScheduled = true;     // Catch: Throwable -> L48
        final FFmpegSession r9 = this.nativeSession;     // Catch: Throwable -> L48
        this.main.postDelayed(new RecordingService$$ExternalSyntheticLambda6(this, r0, r9), 1500);     // Catch: Throwable -> L48
        goto L41
    L31:
        r2 = false;
        goto L32
    L44:
        throw th;     // Catch: Throwable -> L45
    L17:
        if (this.session < 0) goto L21;
        if (this.completionStarted == true) goto L21;
        FFmpegKit.cancel(this.session);     // Catch: Throwable -> L23
    L6:
        if (this.stopReason.isEmpty() == false) goto L8;
        goto L7
    }

    private synchronized void stopFallback(final RecordingTask r7, final FFmpegSession r8, final int r9) {
        monitor-enter(this);
    L44:
        th = move-exception;
        throw th;
    L4:
        if (this.task == r7) goto L6;
    L15:
        monitor-exit(this);
        return;
    L6:
        if (this.nativeSession != r8) goto L15;
        if (this.completionStarted == true) goto L15;
        if (this.stopped == false) goto L15;
        if (terminal(r8) == false) goto L18;
        queueComplete(r7, r8);     // Catch: Throwable -> L44
        goto L15
    L18:
        if (r9 < 2) goto L48;
        BlackBox.event("RECORDING_STOP_PENDING", "engine_state=" + engineState(r8));     // Catch: Throwable -> L44
        goto L15
    L48:
        Iterator<FFmpegSession> r0 = FFmpegKit.listSessions().iterator();     // Catch: Throwable -> L37
    L23:
        if (r0.hasNext() == false) goto L24;
        FFmpegSession r1 = r0.next();     // Catch: Throwable -> L37
        if (r1.getSessionId() == r8.getSessionId()) goto L23;
        if (r1.getState() == SessionState.RUNNING) goto L31;
        if (r1.getState() != SessionState.CREATED) goto L23;
    L31:
        boolean r2 = false;
    L32:
        if (r2 == false) goto L35;
        FFmpegKit.cancel(0);     // Catch: Throwable -> L37
        String r3 = "RECORDING_STOP_IO_INTERRUPT";
        String r4 = "attempt=" + (r9 + 1) + " engine_state=" + engineState(r8);     // Catch: Throwable -> L37
    L34:
        BlackBox.event(r3, r4);     // Catch: Throwable -> L37
    L41:
        this.main.postDelayed(new RecordingService$$ExternalSyntheticLambda1(this, r7, r8, r9), 1500);     // Catch: Throwable -> L44
        goto L15
    L35:
        r3 = "RECORDING_STOP_IO_SKIPPED";
        r4 = "another_session=true";
        goto L34
    L24:
        r2 = true;
    L37:
        th = move-exception;
        if (RecordingNative.fatal(th) == true) goto L43;
        RecordingNative.failure("cancel_io", th);     // Catch: Throwable -> L44
        goto L41
    L43:
        throw th;     // Catch: Throwable -> L44
    }

    private static boolean terminal(FFmpegSession r2) {
        if (r2 != null) goto L4;
        return false;
    L4:
        if (r2.getReturnCode() == null) goto L6;
        return true;
    L6:
        if (r2.getState() != SessionState.FAILED) goto L8;
        return true;
    L8:
        if (r2.getState() != SessionState.COMPLETED) goto L15;
        return true;
    L15:
        return false;
    }

    /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m737lambda$0$esantoniom3uRecordingService(String r6) {
        RecordingTask r1 = this.task;
        if (r1 != null) goto L5;
    L35:
        RecordingTask r2 = RecordingStore.get(this, r6);     // Catch: Exception -> L29
        if (r2 == null) goto L32;
        if ("Programada".equals(r2.state) == false) goto L14;
        RecordingScheduler.cancel(this, r2);     // Catch: Exception -> L29
    L14:
        if (RecordingControl.request(this, r6) != 2) goto L24;
        File r0 = RecordingStore.file(this, r2);     // Catch: Exception -> L29
        if (r0.exists() == true) goto L18;
    L22:
        RecordingStore.remove(this, r6);     // Catch: Exception -> L29
        goto L32
    L18:
        if (r0.delete() == true) goto L22;
        throw new IOException("No se pudo borrar el archivo");     // Catch: Exception -> L29
    L24:
        if (r2.active() == false) goto L26;
    L27:
        r2.state = "Detenida";     // Catch: Exception -> L29
        r2.message = "Detenida por el usuario";     // Catch: Exception -> L29
        RecordingStore.put(this, r2);     // Catch: Exception -> L29
        goto L32
    L26:
        if ("Programada".equals(r2.state) == true) goto L27;
    L32:
        if (this.task != null) goto L36;
        finishService();
        return;
    L36:
        return;
    L29:
        e = move-exception;
        BlackBox.event("RECORDING_STOP_ERROR", e.getClass().getSimpleName());
        goto L32
    L5:
        if (r1.f188id.equals(r6) == false) goto L35;
        stop("Detenida por el usuario");
    }

    /* JADX INFO: renamed from: lambda$1$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m738lambda$1$esantoniom3uRecordingService(String r1) {
        begin(r1);
    }

    /* JADX INFO: renamed from: lambda$2$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m739lambda$2$esantoniom3uRecordingService(RecordingTask r1, FFmpegSession r2) {
        queueComplete(r1, r2);
    }

    /* JADX INFO: renamed from: lambda$3$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m740lambda$3$esantoniom3uRecordingService(RecordingTask r1, Log r2) {
        engineWarning(r1, r2.getMessage());
    }

    /* JADX INFO: renamed from: lambda$4$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m741lambda$4$esantoniom3uRecordingService(RecordingTask r4, Statistics r5) {
        RecordingTask r0 = this.task;
        if (r0 != r4) goto L10;
        r0.recordedMillis = (long) r5.getTime();
        if (r0.recordedMillis > 0) goto L7;
        return;
    L7:
        if (this.stopped == true) goto L12;
        r0.state = "Grabando";
        return;
    L12:
        return;
    }

    /* JADX INFO: renamed from: lambda$5$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m742lambda$5$esantoniom3uRecordingService(RecordingTask r2) {
        if (this.task == r2) goto L5;
        return;
    L5:
        if (this.completionStarted == true) goto L10;
        notifyState(r2.title);
        this.main.removeCallbacks(this.monitor);
        this.main.post(this.monitor);
        return;
    }

    /* JADX INFO: renamed from: lambda$6$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m743lambda$6$esantoniom3uRecordingService(RecordingTask r2, FFmpegSession r3) {
        stopFallback(r2, r3, 0);
    }

    /* JADX INFO: renamed from: lambda$7$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m744lambda$7$esantoniom3uRecordingService(RecordingTask r1, FFmpegSession r2) {
        complete(r1, r2);
    }

    /* JADX INFO: renamed from: lambda$8$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m745lambda$8$esantoniom3uRecordingService(RecordingTask r1, FFmpegSession r2, int r3) {
        stopFallback(r1, r2, r3 + 1);
    }

    /* JADX INFO: renamed from: lambda$9$es-antonio-m3u-RecordingService, reason: not valid java name */
    /* synthetic */ void m746lambda$9$esantoniom3uRecordingService() {
        if (this.task == null) goto L5;
        return;
    L5:
        this.main.removeCallbacks(this.monitor);
        if (this.wake != null) goto L8;
    L10:
        stopForeground(true);
        notifyState(this.finishNote);
        stopSelf();
        return;
    L8:
        if (this.wake.isHeld() == false) goto L10;
        this.wake.release();
        goto L10
    }

    @Override // android.app.Service
    public IBinder onBind(Intent r1) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < 26) goto L6;
        NotificationChannel r0 = new NotificationChannel(CHANNEL, "Grabaciones CHDTV", 2);
        ((NotificationManager) getSystemService("notification")).createNotificationChannel(r0);
        return;
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.main.removeCallbacks(this.monitor);
        stop("Grabación interrumpida al cerrar el servicio");
        RecordingTask r0 = this.task;
        if (r0 == null) goto L8;
        r0.state = "Error";
        r0.message = this.stopReason;
        RecordingStore.put(this, r0);     // Catch: Exception -> L7
    L8:
        this.task = null;
        closeProxy();
        if (this.wake != null) goto L11;
    L13:
        this.worker.shutdown();
        super.onDestroy();
        return;
    L11:
        if (this.wake.isHeld() == false) goto L13;
        this.wake.release();
        goto L13
    }

    @Override // android.app.Service
    public int onStartCommand(Intent r4, int r5, int r6) {
        if (r4 != null) goto L7;
        stopSelf();
        return 2;
    L7:
        if ("stop".equals(r4.getAction()) == false) goto L9;
    L26:
        final String r7 = r4.getStringExtra("id");
        if (r7 != null) goto L29;
        return 2;
    L29:
        RecordingControl.request(this, r7, "delete".equals(r4.getAction()));
        RecordingTask r8 = this.task;
        if (r8 != null) goto L32;
    L34:
        this.worker.execute(new RecordingService$$ExternalSyntheticLambda8(this, r7));
        return 2;
    L32:
        if (r8.f188id.equals(r7) == false) goto L34;
        stop("Detenida por el usuario");
        goto L34
    L9:
        if ("delete".equals(r4.getAction()) == true) goto L26;
        Notification r9 = notification("Preparando grabación…");
        final String r10 = r4.getStringExtra("id");
        if (Build.VERSION.SDK_INT < 29) goto L15;
        startForeground(NOTIFICATION, r9, 1);     // Catch: RuntimeException -> L18
    L16:
        this.worker.execute(new RecordingService$$ExternalSyntheticLambda9(this, r10));
        return 2;
    L15:
        startForeground(NOTIFICATION, r9);     // Catch: RuntimeException -> L18
        goto L16
    L37:
        RecordingTask r11 = RecordingStore.get(this, r10);     // Catch: Exception -> L23
        if (r11 == null) goto L24;
        r11.state = "Error";     // Catch: Exception -> L23
        r11.message = "Android no permitió iniciar el servicio de grabación; revisa permisos y límite de segundo plano";     // Catch: Exception -> L23
        RecordingStore.put(this, r11);     // Catch: Exception -> L23
    L24:
        stopSelf();
        return 2;
    }

    public void onTimeout(int r1, int r2) {
        stop("Android alcanzó el límite de trabajo en segundo plano");
        RecordingTask r3 = this.task;
        if (r3 == null) goto L8;
        r3.state = "Error";
        r3.message = this.stopReason;
        RecordingStore.put(this, r3);     // Catch: Exception -> L7
    L8:
        closeProxy();
        stopForeground(true);
        stopSelf();
    }
}
