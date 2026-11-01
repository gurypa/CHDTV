package es.antonio.m3u;

import android.content.Context;
import android.os.SystemClock;
import androidx.media3.exoplayer.ExoPlayer;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes7.dex */
final class EpgChannelFlow {
    private static Context app;
    private static final List<WeakReference<MainActivity>> clients = null;
    private static boolean complete;
    private static final ExecutorService disk = null;
    private static long epoch;
    private static long lastCheck;
    private static EpgData latest;
    private static long latestAt;
    private static boolean loading;
    private static final ExecutorService network = null;
    private static final ThreadLocal<Progress> progress = null;
    private static boolean refreshing;

    static final class Progress {
        final Context context;
        long last;
        final long now;
        final long revision;
        final EpgData small;
        final long token;

        Progress(Context r3, long r4) {
            this.now = System.currentTimeMillis();
            this.small = new EpgData();
            this.context = r3;
            this.token = r4;
            this.revision = r3.getSharedPreferences("epg_settings", 0).getLong("revision", 0);
            EpgData r5 = this.small;
            r5.loadedAt = this.now;
        }

        void aliases(String r3, List<String> r4) {
            this.small.names.put(r3, new ArrayList(r4));
            this.small.epg217ResetNames();
        }

        void flush() {
            if (EpgChannelFlow.access$000(this.context, this.token, this.revision) == true) goto L5;
            return;
        L5:
            if (this.small.count() <= 0) goto L9;
            EpgChannelFlow.access$100(EpgChannelStore.compact(this.small, this.now), this.token, "download-progress");
            this.last = SystemClock.elapsedRealtime();
            return;
        }

        void programme(EpgData.Programme r7) {
            if (r7.stop <= r7.start) goto L5;
            long r0 = r7.stop;
        L7:
            if (r0 > this.now) goto L9;
            return;
        L9:
            if (r7.start >= (this.now + 86400000)) goto L20;
            List<EpgData.Programme> r1 = this.small.programmes.get(r7.channel);
            if (r1 != null) goto L14;
            r1 = new ArrayList();
            this.small.programmes.put(r7.channel, r1);
        L14:
            EpgData.Programme r2 = new EpgData.Programme();
            r2.channel = r7.channel;
            r2.title = r7.title;
            r2.description = r7.description;
            r2.start = r7.start;
            r2.stop = r7.stop;
            r1.add(r2);
            long r3 = SystemClock.elapsedRealtime();
            if ((r3 - this.last) < ExoPlayer.DEFAULT_DETACH_SURFACE_TIMEOUT_MS) goto L19;
            this.last = r3;
            flush();
            return;
        L19:
            return;
        L20:
            return;
        L5:
            r0 = r7.start + 7200000;
            goto L7
        }
    }

    static {
        disk = Executors.newSingleThreadExecutor();
        network = Executors.newSingleThreadExecutor();
        clients = new ArrayList();
        progress = new ThreadLocal();
    }

    EpgChannelFlow() {
    }

    static /* synthetic */ boolean access$000(Context r0, long r1, long r3) {
        return current(r0, r1, r3);
    }

    static /* synthetic */ void access$100(EpgData r0, long r1, String r3) {
        publish(r0, r1, r3);
    }

    private static synchronized boolean current(Context r5, long r6, long r8) {
        monitor-enter(EpgChannelFlow.class);
        boolean r7 = false;
        if (r6 == epoch) goto L7;
    L12:
        monitor-exit(EpgChannelFlow.class);
        return r7;
    L7:
        if (r5.getSharedPreferences("epg_settings", 0).getBoolean("enabled", true) == false) goto L12;
        if (r5.getSharedPreferences("epg_settings", 0).getLong("revision", 0) != r8) goto L12;
        r7 = true;
    L14:
        th = move-exception;
        throw th;
    }

    private static void deliver(final MainActivity r1, final EpgData r2) {
        r1.runOnUiThread(new EpgChannelFlow$$ExternalSyntheticLambda2(r1, r2));
    }

    static /* synthetic */ void lambda$deliver$2(MainActivity r1, EpgData r2) {
        if (r1.isDestroyed() == false) goto L5;
        return;
    L5:
        if (r1.isFinishing() == true) goto L9;
        r1.epg216Apply(r2);
        return;
    }

    static /* synthetic */ void lambda$queueRefresh$1(Context r0, long r1) {
        refresh(r0, r1);
    }

    static /* synthetic */ void lambda$request$0(Context r0, long r1) {
        load(r0, r1);
    }

    private static void load(Context r7, long r8) {
        long r0 = SystemClock.elapsedRealtime();
        EpgData r3 = AppStorage.peekEpg(r7);     // Catch: Throwable -> L43 Exception -> L45
        if (r3 == null) goto L75;
        publish(EpgChannelStore.compact(r3, System.currentTimeMillis()), r8, "memory");     // Catch: Throwable -> L43 Exception -> L45
        monitor-enter(EpgChannelFlow.class);
    L14:
        th = move-exception;
        throw th;
    L9:
        if (r8 != epoch) goto L11;
        loading = false;     // Catch: Throwable -> L14
    L11:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L14
        BlackBox.event("EPG_CHANNEL_CACHE_READ", "duration_ms=" + (SystemClock.elapsedRealtime() - r0));
        queueRefresh(r7, r8);
        return;
    L75:
        publish(EpgChannelStore.read(r7, System.currentTimeMillis()), r8, "projection");     // Catch: Exception -> L29 Throwable -> L43
        monitor-enter(EpgChannelFlow.class);
    L26:
        th = move-exception;
        throw th;
    L21:
        if (r8 != epoch) goto L23;
        loading = false;     // Catch: Throwable -> L26
    L23:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L26
        BlackBox.event("EPG_CHANNEL_CACHE_READ", "duration_ms=" + (SystemClock.elapsedRealtime() - r0));
        queueRefresh(r7, r8);
        return;
    L30:
        Object r4 = new AppStorage(r7).read("epg.dat");     // Catch: Throwable -> L43 Exception -> L45
        if ((r4 instanceof EpgData) == false) goto L34;
        EpgData r5 = (EpgData) r4;     // Catch: Throwable -> L43 Exception -> L45
        r5.ensureIndexed();     // Catch: Throwable -> L43 Exception -> L45
        publish(EpgChannelStore.compact(r5, System.currentTimeMillis()), r8, "migration");     // Catch: Throwable -> L43 Exception -> L45
        EpgChannelStore.save(r7, new File(r7.getFilesDir(), "epg.dat"), r5, System.currentTimeMillis());     // Catch: Throwable -> L43 Exception -> L45
    L34:
        monitor-enter(EpgChannelFlow.class);
    L40:
        th = move-exception;
        throw th;
    L36:
        if (r8 != epoch) goto L38;
        loading = false;     // Catch: Throwable -> L40
    L38:
        String r2 = "EPG_CHANNEL_CACHE_READ";
        StringBuilder r6 = new StringBuilder();
    L54:
        BlackBox.event(r2, r6.append("duration_ms=").append(SystemClock.elapsedRealtime() - r0).toString());
        queueRefresh(r7, r8);
        return;
    L43:
        th = move-exception;
        monitor-enter(EpgChannelFlow.class);
    L67:
        th = move-exception;
        throw th;
    L62:
        if (r8 != epoch) goto L64;
        loading = false;     // Catch: Throwable -> L67
    L64:
        BlackBox.event("EPG_CHANNEL_CACHE_READ", "duration_ms=" + (SystemClock.elapsedRealtime() - r0));
        queueRefresh(r7, r8);
        throw th;
    L45:
        e = move-exception;
        BlackBox.event("EPG_CHANNEL_CACHE_ERROR", e.getClass().getSimpleName());     // Catch: Throwable -> L43
        monitor-enter(EpgChannelFlow.class);
    L56:
        th = move-exception;
        throw th;
    L50:
        if (r8 != epoch) goto L52;
        loading = false;     // Catch: Throwable -> L56
    L52:
        r2 = "EPG_CHANNEL_CACHE_READ";
        r6 = new StringBuilder();
        goto L54
    }

    static Progress progress() {
        return progress.get();
    }

    private static void publish(EpgData r7, long r8, String r10) {
        if (r7 != null) goto L4;
        return;
    L4:
        if (r7.count() != 0) goto L7;
        return;
    L7:
        monitor-enter(EpgChannelFlow.class);
    L78:
        th = move-exception;
        throw th;
    L9:
        if (r8 == epoch) goto L12;
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L78
        return;
    L12:
        EpgData r1 = latest;     // Catch: Throwable -> L78
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L78
        if (r1 != null) goto L16;
    L24:
        ArrayList r0 = new ArrayList();
        monitor-enter(EpgChannelFlow.class);
    L75:
        th = move-exception;
        throw th;
    L27:
        if (r8 == epoch) goto L29;
    L73:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L75
        return;
    L29:
        if (latest == null) goto L34;
        if (r7.loadedAt < latest.loadedAt) goto L73;
    L34:
        if (r10.equals("download-progress") == false) goto L40;
        if (latest == r1) goto L40;
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L75
        return;
    L40:
        if (latest != null) goto L42;
    L53:
        latest = r7;     // Catch: Throwable -> L75
        latestAt = System.currentTimeMillis();     // Catch: Throwable -> L75
        if (r10.equals("download-progress") == true) goto L56;
        boolean r9 = true;
    L57:
        complete = r9;     // Catch: Throwable -> L75
        Iterator<WeakReference<MainActivity>> r11 = clients.iterator();     // Catch: Throwable -> L75
    L59:
        if (r11.hasNext() == false) goto L67;
        MainActivity r12 = r11.next().get();     // Catch: Throwable -> L75
        if (r12 == null) goto L66;
        if (r12.isDestroyed() == true) goto L66;
        r0.add(r12);     // Catch: Throwable -> L75
    L66:
        r11.remove();     // Catch: Throwable -> L75
        goto L59
    L67:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L75
        BlackBox.event("EPG_CHANNEL_READY", "source=" + r10 + " programmes=" + r7.count());
        Iterator r13 = r0.iterator();
    L70:
        if (r13.hasNext() == false) goto L72;
        deliver((MainActivity) r13.next(), r7);
        goto L70
    L72:
        return;
    L56:
        r9 = false;
        goto L57
    L42:
        if (System.currentTimeMillis() >= (latestAt + 43200000)) goto L53;
        if (r7.loadedAt != latest.loadedAt) goto L53;
        if (r10.equals("projection") == false) goto L48;
    L51:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L75
        return;
    L48:
        if (r10.equals("migration") == true) goto L51;
        if (r10.equals("memory") == false) goto L53;
    L16:
        if (r10.equals("download-progress") == false) goto L24;
        EpgData r2 = new EpgData();
        r2.loadedAt = r7.loadedAt;
        r2.names.putAll(r1.names);
        r2.names.putAll(r7.names);
        r2.programmes.putAll(r1.programmes);
        r2.programmes.putAll(r7.programmes);
        if (r1.names.entrySet().containsAll(r7.names.entrySet()) == false) goto L21;
        r1.epg217CopyNames(r2);
    L23:
        r7 = EpgChannelStore.compact(r2, System.currentTimeMillis());
        goto L24
    L21:
        if (r7.names.entrySet().containsAll(r1.names.entrySet()) == false) goto L23;
        r7.epg217CopyNames(r2);
        goto L23
    }

    private static synchronized void queueRefresh(final Context r12, final long r13) {
        monitor-enter(EpgChannelFlow.class);
    L48:
        th = move-exception;
        throw th;
    L6:
        if (r13 == epoch) goto L9;
        monitor-exit(EpgChannelFlow.class);
        return;
    L9:
        long r1 = System.currentTimeMillis();     // Catch: Throwable -> L48
        boolean r4 = false;
        if (complete == true) goto L12;
    L23:
        boolean r3 = true;
    L24:
        if (r3 == true) goto L35;
        Iterator<List<EpgData.Programme>> r5 = latest.programmes.values().iterator();     // Catch: Throwable -> L48
    L27:
        if (r5.hasNext() == false) goto L34;
        Iterator<EpgData.Programme> r8 = r5.next().iterator();     // Catch: Throwable -> L48
    L30:
        if (r8.hasNext() == false) goto L27;
        if (r8.next().stop <= r1) goto L30;
    L36:
        if (r4 == true) goto L38;
    L46:
        monitor-exit(EpgChannelFlow.class);
        return;
    L38:
        if (refreshing == true) goto L46;
        if (lastCheck != 0) goto L42;
    L45:
        refreshing = true;     // Catch: Throwable -> L48
        lastCheck = r1;     // Catch: Throwable -> L48
        network.execute(new EpgChannelFlow$$ExternalSyntheticLambda0(r12, r13));     // Catch: Throwable -> L48
        goto L46
    L42:
        if (r1 < lastCheck) goto L45;
        if ((r1 - lastCheck) <= 300000) goto L46;
    L34:
        r4 = true;
        goto L36
    L35:
        r4 = r3;
        goto L36
    L12:
        if (latest == null) goto L23;
        if (latest.count() == 0) goto L23;
        if (latest.loadedAt <= 0) goto L23;
        if ((r1 - latest.loadedAt) >= 21600000) goto L23;
        if (latest.loadedAt > r1) goto L23;
        r3 = false;
        goto L24
    }

    private static void refresh(Context r4, long r5) {
        progress.set(new Progress(r4, r5));     // Catch: Throwable -> L17 Exception -> L19
        EpgData r1 = EpgChannelWork.refresh(new EpgRepository(r4));     // Catch: Throwable -> L17 Exception -> L19
        if (current(r4, r5, progress.get().revision) == false) goto L7;
        if (r1 == null) goto L7;
        publish(EpgChannelStore.compact(r1, System.currentTimeMillis()), r5, "refresh");     // Catch: Throwable -> L17 Exception -> L19
    L7:
        progress.remove();
        monitor-enter(EpgChannelFlow.class);
    L14:
        th = move-exception;
        throw th;
    L10:
        if (r5 != epoch) goto L12;
        refreshing = false;     // Catch: Throwable -> L14
    L12:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L14
        return;
    L19:
        e = move-exception;
        BlackBox.event("EPG_CHANNEL_REFRESH_ERROR", e.getClass().getSimpleName());     // Catch: Throwable -> L17
        progress.remove();
        monitor-enter(EpgChannelFlow.class);
    L28:
        th = move-exception;
        throw th;
    L24:
        if (r5 != epoch) goto L26;
        refreshing = false;     // Catch: Throwable -> L28
    L26:
        return;
    L17:
        th = move-exception;
        progress.remove();
        monitor-enter(EpgChannelFlow.class);
    L38:
        th = move-exception;
        throw th;
    L34:
        if (r5 != epoch) goto L36;
        refreshing = false;     // Catch: Throwable -> L38
    L36:
        throw th;
    }

    static void removed(Context r6) {
        ArrayList r0 = new ArrayList();
        monitor-enter(EpgChannelFlow.class);
        epoch++;     // Catch: Throwable -> L18
        latest = null;     // Catch: Throwable -> L18
        lastCheck = 0;     // Catch: Throwable -> L18
        latestAt = 0;     // Catch: Throwable -> L18
        complete = false;     // Catch: Throwable -> L18
        refreshing = false;     // Catch: Throwable -> L18
        loading = false;     // Catch: Throwable -> L18
        Iterator<WeakReference<MainActivity>> r2 = clients.iterator();     // Catch: Throwable -> L18
    L6:
        if (r2.hasNext() == false) goto L12;
        MainActivity r3 = r2.next().get();     // Catch: Throwable -> L18
        if (r3 == null) goto L6;
        if (r3.isDestroyed() == true) goto L6;
        r0.add(r3);     // Catch: Throwable -> L18
        goto L6
    L12:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L18
        EpgChannelStore.remove(r6);
        EpgData r7 = new EpgData();
        Iterator r1 = r0.iterator();
    L15:
        if (r1.hasNext() == false) goto L17;
        deliver((MainActivity) r1.next(), r7);
        goto L15
    L17:
        return;
    L18:
        th = move-exception;
        throw th;
    }

    static void request(MainActivity r13) {
        if (r13.isDestroyed() == false) goto L5;
        return;
    L5:
        if (r13.isFinishing() == true) goto L58;
        final Context r0 = r13.getApplicationContext();
        monitor-enter(EpgChannelFlow.class);
        app = r0;     // Catch: Throwable -> L41
        Iterator<WeakReference<MainActivity>> r2 = clients.iterator();     // Catch: Throwable -> L41
        boolean r3 = false;
        boolean r4 = false;
    L11:
        if (r2.hasNext() == false) goto L20;
        MainActivity r5 = r2.next().get();     // Catch: Throwable -> L41
        if (r5 == null) goto L19;
        if (r5.isDestroyed() == true) goto L19;
        if (r5 != r13) goto L11;
        r4 = true;
    L19:
        r2.remove();     // Catch: Throwable -> L41
        goto L11
    L20:
        if (r4 == true) goto L22;
        clients.add(new WeakReference(r13));     // Catch: Throwable -> L41
    L22:
        long r6 = System.currentTimeMillis();     // Catch: Throwable -> L41
        final long r7 = epoch;     // Catch: Throwable -> L41
        if (latest == null) goto L30;
        if (r6 < (latestAt - 3600000)) goto L30;
        if (r6 >= (latestAt + 43200000)) goto L30;
        EpgData r8 = latest;     // Catch: Throwable -> L41
    L33:
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L41
        if (r8 == null) goto L36;
        deliver(r13, r8);
    L36:
        if (r3 == false) goto L38;
        disk.execute(new EpgChannelFlow$$ExternalSyntheticLambda1(r0, r7));
        return;
    L38:
        if (r8 == null) goto L57;
        queueRefresh(r0, r7);
        return;
    L57:
        return;
    L30:
        if (loading == true) goto L32;
        loading = true;     // Catch: Throwable -> L41
        r8 = null;
        r3 = true;
        goto L33
    L32:
        r8 = null;
    L41:
        th = move-exception;
        throw th;
    }

    static void stored(Context r2, EpgData r3) {
        storedReady(r2, EpgChannelStore.compact(r3, System.currentTimeMillis()));
    }

    static void storedReady(Context r2, EpgData r3) {
        monitor-enter(EpgChannelFlow.class);
        long r0 = epoch;     // Catch: Throwable -> L8
        monitor-exit(EpgChannelFlow.class);     // Catch: Throwable -> L8
        publish(r3, r0, "saved");
        return;
    L8:
        th = move-exception;
        throw th;
    }
}
