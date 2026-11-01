package es.antonio.m3u;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.ByteArrayDataSource;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.TransferListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes7.dex */
public final class TvManifestWarm {
    static final String PLAIN = "plain-hls-v199";
    private static final ExecutorService POOL = null;
    private static final Map<PlayerActivity, State> STATES = null;

    static final class Cache {
        final Map<String, Payload> items;

        Cache() {
            this.items = new LinkedHashMap();
        }

        synchronized void clear() {
            monitor-enter(this);
            this.items.clear();     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }

        synchronized void put(String r2, Payload r3) {
            monitor-enter(this);
            this.items.put(r2, r3);     // Catch: Throwable -> L11
        L6:
            if (this.items.size() <= 2) goto L7;
            this.items.remove(this.items.keySet().iterator().next());     // Catch: Throwable -> L11
            goto L6
        L7:
            monitor-exit(this);
            return;
        L11:
            th = move-exception;
            throw th;
        }

        synchronized Payload take(String r2, String r3, Map<String, String> r4) {
            monitor-enter(this);
            Payload r5 = this.items.remove(r2);     // Catch: Throwable -> L15
            if (r5 != null) goto L6;
        L12:
            r5 = null;
        L13:
            monitor-exit(this);
            return r5;
        L6:
            if (r5.auth.equals(r3) == false) goto L12;
            if (r5.requestHeaders.equals(r4) == false) goto L12;
            if (r5.fresh(System.nanoTime()) == false) goto L12;
        L15:
            th = move-exception;
            throw th;
        }
    }

    static final class Payload {
        final String auth;
        final byte[] bytes;
        final Map<String, List<String>> headers;
        final long ready;
        final Map<String, String> requestHeaders;
        final long ttl;
        final Uri uri;

        Payload(byte[] r2, Uri r3, Map<String, List<String>> r4, String r5, Map<String, String> r6) {
            this.bytes = r2;
            this.uri = r3;
            this.headers = r4;
            this.auth = r5;
            this.requestHeaders = r6;
            this.ready = System.nanoTime();
            String r7 = new String(r2, 0, Math.min(r2.length, 16384), StandardCharsets.UTF_8);
            TimeUnit r8 = TimeUnit.MILLISECONDS;
            if (r7.contains("#EXT-X-STREAM-INF:") == true) goto L5;
            int r9 = 1000;
        L9:
            this.ttl = r8.toNanos(r9);
            return;
        L5:
            if (TvManifestWarm.PLAIN.equals(r5) == false) goto L7;
            r9 = 3000;
            goto L9
        L7:
            r9 = 30000;
            goto L9
        }

        boolean fresh(long r3) {
            if ((r3 - this.ready) > this.ttl) goto L6;
            return true;
        L6:
            return false;
        }
    }

    private static final class Source implements DataSource {
        final String auth;
        final Entry entry;
        final int generation;
        final List<TransferListener> listeners;
        Payload payload;
        DataSource selected;
        final State state;
        final DataSource upstream;

        Source(State r2, DataSource r3, String r4, Entry r5, int r6) {
            this.listeners = new ArrayList();
            this.state = r2;
            this.upstream = r3;
            this.auth = r4;
            this.entry = r5;
            this.generation = r6;
        }

        @Override // androidx.media3.datasource.DataSource
        public void addTransferListener(TransferListener r2) {
            this.listeners.add(r2);
            this.upstream.addTransferListener(r2);
        }

        @Override // androidx.media3.datasource.DataSource
        public void close() throws IOException {
            if (this.selected == null) goto L5;
            this.selected.close();
        L5:
            this.selected = null;
            this.payload = null;
        }

        @Override // androidx.media3.datasource.DataSource
        public Map<String, List<String>> getResponseHeaders() {
            if (this.payload == null) goto L6;
            return this.payload.headers;
        L6:
            if (this.selected != null) goto L9;
            return Collections.emptyMap();
        L9:
            return this.selected.getResponseHeaders();
        }

        @Override // androidx.media3.datasource.DataSource
        public Uri getUri() {
            if (this.payload == null) goto L8;
            if (this.payload.uri == null) goto L8;
            return this.payload.uri;
        L8:
            if (this.selected != null) goto L11;
            return null;
        L11:
            return this.selected.getUri();
        }

        @Override // androidx.media3.datasource.DataSource
        public long open(DataSpec r6) throws IOException {
            PlayerActivity r0 = this.state.owner.get();
            if (r0 == null) goto L25;
            if (this.generation != this.state.generation) goto L25;
            if (r6.position != 0) goto L25;
            if (r6.length != (-1)) goto L25;
            if (r6.httpMethod != 1) goto L25;
            if (r6.httpBody != null) goto L25;
            if (r6.httpRequestHeaders.isEmpty() == false) goto L25;
            if (TvManifestWarm.PLAIN.equals(this.auth) == false) goto L22;
            if (r0.tvWarmPlainHls() == false) goto L25;
        L23:
            this.payload = TvManifestWarm.take(this.state, r6.uri.toString(), this.auth, this.entry.headers);
            goto L25
        L22:
            if (this.auth.equals(CdnTokens.current(r0, this.entry)) == true) goto L23;
        L25:
            if (this.payload == null) goto L33;
            this.selected = new ByteArrayDataSource(this.payload.bytes);
            Iterator<TransferListener> r1 = this.listeners.iterator();
        L28:
            if (r1.hasNext() == false) goto L29;
            this.selected.addTransferListener(r1.next());
            goto L28
        L29:
            BlackBox.event("TV_MANIFEST_WARM_USED", "bytes=" + this.payload.bytes.length);
        L31:
            return this.selected.open(r6);
        L33:
            this.selected = this.upstream;
            goto L31
        }

        @Override // androidx.media3.common.DataReader
        public int read(byte[] r2, int r3, int r4) throws IOException {
            return this.selected.read(r2, r3, r4);
        }
    }

    static final class State {
        int attempts;
        String auth;
        final Cache cache;
        Entry current;
        volatile int generation;
        final List<Future<?>> jobs;
        final Handler main;
        DataSource.Factory network;
        final WeakReference<PlayerActivity> owner;
        boolean plain;
        final Runnable poll;

        State(PlayerActivity r3) {
            this.main = new Handler(Looper.getMainLooper());
            this.cache = new Cache();
            this.jobs = new ArrayList();
            this.poll = new TvManifestWarm$State$$ExternalSyntheticLambda0(this);
            this.owner = new WeakReference(r3);
        }

        /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-TvManifestWarm$State, reason: not valid java name */
        /* synthetic */ void m814lambda$0$esantoniom3uTvManifestWarm$State() {
            TvManifestWarm.access$0(this);
        }
    }

    static {
        STATES = new WeakHashMap();
        POOL = new ThreadPoolExecutor(0, 2, 20, TimeUnit.SECONDS, new SynchronousQueue(), new TvManifestWarm$$ExternalSyntheticLambda1());
    }

    public TvManifestWarm() {
    }

    static /* synthetic */ void access$0(State r0) {
        poll(r0);
    }

    public static DefaultDataSource.Factory create(Context r9, final DataSource.Factory r10) {
        if ((r9 instanceof PlayerActivity) == false) goto L5;
        PlayerActivity r0 = (PlayerActivity) r9;
        String r1 = StreamFormat.mime(r0.tvCurrent());
        if (r0.tvWarmAllowed() == false) goto L57;
        if (MimeTypes.APPLICATION_MPD.equals(r1) == false) goto L11;
    L13:
        boolean r2 = r0.tvWarmPlainHls();
        if (r2 == false) goto L16;
        String r3 = PLAIN;
    L17:
        final String r6 = r3;
        if (r6.isEmpty() == true) goto L20;
        Map<PlayerActivity, State> r4 = STATES;
        monitor-enter(r4);
        State r5 = STATES.get(r0);     // Catch: Throwable -> L53
        if (r5 != null) goto L26;
        r5 = new State(r0);     // Catch: Throwable -> L53
        STATES.put(r0, r5);     // Catch: Throwable -> L53
    L26:
        final State r7 = r5;
        monitor-exit(r4);     // Catch: Throwable -> L53
        monitor-enter(r7);
        Iterator<Future<?>> r8 = r7.jobs.iterator();     // Catch: Throwable -> L50
    L31:
        if (r8.hasNext() == false) goto L32;
        r8.next().cancel(true);     // Catch: Throwable -> L50
        goto L31
    L32:
        r7.jobs.clear();     // Catch: Throwable -> L50
        r7.generation++;     // Catch: Throwable -> L50
        r7.network = r10;     // Catch: Throwable -> L50
        r7.current = r0.tvCurrent();     // Catch: Throwable -> L50
        if (r7.auth != null) goto L35;
    L37:
        r7.auth = r6;     // Catch: Throwable -> L50
        r7.plain = r2;     // Catch: Throwable -> L50
        r7.attempts = 0;     // Catch: Throwable -> L50
        r7.main.removeCallbacks(r7.poll);     // Catch: Throwable -> L50
        Handler r11 = r7.main;     // Catch: Throwable -> L50
        Runnable r12 = r7.poll;     // Catch: Throwable -> L50
        if (r2 == false) goto L40;
        int r13 = 700;
    L41:
        r11.postDelayed(r12, r13);     // Catch: Throwable -> L50
        monitor-exit(r7);     // Catch: Throwable -> L50
        StringBuilder r14 = new StringBuilder("mode=");
        if (r2 == false) goto L46;
        String r15 = "plain_hls";
    L47:
        BlackBox.event("TV_MANIFEST_WARM_SCOPE", r14.append(r15).append(" max_neighbors=2").toString());
        final Entry r16 = r0.tvCurrent();
        final int r17 = r7.generation;
        return new DefaultDataSource.Factory(r9.getApplicationContext(), new TvManifestWarm$$ExternalSyntheticLambda0(r7, r10, r6, r16, r17));
    L46:
        r15 = "account_token";
        goto L47
    L40:
        r13 = 800;
        goto L41
    L35:
        if (r7.auth.equals(r6) == true) goto L37;
        r7.cache.clear();     // Catch: Throwable -> L50
    L50:
        th = move-exception;
        throw th;
    L53:
        th = move-exception;
        throw th;
    L20:
        return new DefaultDataSource.Factory(r9.getApplicationContext(), r10);
    L16:
        r3 = CdnTokens.current(r0, r0.tvCurrent());
        goto L17
    L11:
        if (MimeTypes.APPLICATION_M3U8.equals(r1) == true) goto L13;
    L57:
        return new DefaultDataSource.Factory(r9.getApplicationContext(), r10);
    L5:
        return new DefaultDataSource.Factory(r9.getApplicationContext(), r10);
    }

    private static void fetch(State r17, int r18, DataSource.Factory r19, String r20, String r21, Map<String, String> r22) {
        long r2 = System.nanoTime();
        DataSource r4 = r19.createDataSource();
        r4.open(new DataSpec.Builder().setUri(r20).setFlags(1).build());     // Catch: Throwable -> L44 IOException -> L46
        ByteArrayOutputStream r5 = new ByteArrayOutputStream();     // Catch: Throwable -> L44 IOException -> L46
        byte[] r7 = new byte[8192];     // Catch: Throwable -> L44 IOException -> L46
    L4:
        int r9 = r4.read(r7, 0, 8192);     // Catch: Throwable -> L44 IOException -> L46
        if (r9 == (-1)) goto L6;
        if (Thread.currentThread().isInterrupted() == true) goto L68;
        if (r9 == 0) goto L41;
        if ((r5.size() + r9) > 2097152) goto L41;
        r5.write(r7, 0, r9);     // Catch: Throwable -> L44 IOException -> L46
    L41:
        throw new IOException("Manifest size/progress");     // Catch: Throwable -> L44 IOException -> L46
    L68:
        throw new InterruptedIOException();     // Catch: Throwable -> L44 IOException -> L46
    L6:
        byte[] r6 = r5.toByteArray();     // Catch: Throwable -> L44 IOException -> L46
        String r8 = new String(r6, 0, Math.min(r6.length, 256), StandardCharsets.UTF_8).trim();     // Catch: Throwable -> L44 IOException -> L46
        if (r8.startsWith("#EXTM3U") == false) goto L9;
    L15:
        Payload r10 = new Payload(r6, r4.getUri(), r4.getResponseHeaders(), r21, r22);     // Catch: Throwable -> L44 IOException -> L46
        monitor-enter(r17);     // Catch: Throwable -> L44 IOException -> L46
    L31:
        th = move-exception;
        throw th;     // Catch: Throwable -> L44 IOException -> L46
    L18:
        if (r18 == r17.generation) goto L24;
        monitor-exit(r17);     // Catch: Throwable -> L31
        r4.close();     // Catch: IOException -> L22
        return;
    L23:
        return;
    L24:
        r17.cache.put(r20, r10);     // Catch: Throwable -> L31
        monitor-exit(r17);     // Catch: Throwable -> L31
        BlackBox.event("TV_MANIFEST_WARM_READY", "bytes=" + r6.length + " duration_ms=" + ((System.nanoTime() - r2) / 1000000));     // Catch: Throwable -> L44 IOException -> L46
        r4.close();     // Catch: IOException -> L29
    L60:
        return;
    L9:
        if (r8.startsWith("<?xml") == true) goto L15;
        if (r8.startsWith("<MPD") == true) goto L15;
        throw new IOException("Not a manifest");     // Catch: Throwable -> L44 IOException -> L46
    L44:
        th = move-exception;
        r4.close();     // Catch: IOException -> L52
        throw th;
    L53:
        throw th;
    L46:
        e = move-exception;
        BlackBox.event("TV_MANIFEST_WARM_FAILED", "type=" + e.getClass().getSimpleName());     // Catch: Throwable -> L44
        r4.close();     // Catch: IOException -> L29
    }

    static /* synthetic */ Thread lambda$0(Runnable r2) {
        Thread r0 = new Thread(r2, "CHDTVManifestWarm");
        r0.setDaemon(true);
        return r0;
    }

    static /* synthetic */ DataSource lambda$1(State r7, DataSource.Factory r8, String r9, Entry r10, int r11) {
        return new Source(r7, r8.createDataSource(), r9, r10, r11);
    }

    static /* synthetic */ void lambda$2(State r0, int r1, DataSource.Factory r2, String r3, String r4, Map r5) {
        fetch(r0, r1, r2, r3, r4, r5);
    }

    static boolean plainNeighbor(String r6, String r7) {
        URI r1 = new URI(r6);     // Catch: Exception -> L39
        URI r8 = new URI(r7);     // Catch: Exception -> L39
        String r2 = r1.getScheme();     // Catch: Exception -> L39
        if ("http".equalsIgnoreCase(r2) == true) goto L9;
        if ("https".equalsIgnoreCase(r2) == true) goto L9;
    L38:
        return false;
    L9:
        if (r2.equalsIgnoreCase(r8.getScheme()) == false) goto L38;
        if (r1.getHost() == null) goto L38;
        if (r8.getHost() == null) goto L38;
        if (r1.getHost().equalsIgnoreCase(r8.getHost()) == false) goto L38;
        if (r1.getUserInfo() != null) goto L38;
        if (r8.getUserInfo() != null) goto L38;
        if (r7.contains("_HLS_") == true) goto L38;
        if ("https".equalsIgnoreCase(r2) == false) goto L26;
        int r9 = 443;
    L28:
        if (r1.getPort() >= 0) goto L30;
        int r3 = r9;
    L32:
        if (r8.getPort() < 0) goto L35;
        r9 = r8.getPort();     // Catch: Exception -> L39
    L35:
        if (r3 != r9) goto L38;
        return true;
    L30:
        r3 = r1.getPort();     // Catch: Exception -> L39
        goto L32
    L26:
        r9 = 80;
    L40:
        return false;
    }

    private static void poll(final State r18) {
        PlayerActivity r0 = r18.owner.get();
        if (r0 != null) goto L5;
    L71:
        if (r0 == null) goto L98;
        release(r0);
        return;
    L98:
        return;
    L5:
        if (r0.isFinishing() == true) goto L71;
        if (r0.isDestroyed() == true) goto L71;
        if (r0.tvWarmCanPrime() == true) goto L25;
        monitor-enter(r18);
        int r1 = r18.attempts + 1;     // Catch: Throwable -> L22
        r18.attempts = r1;     // Catch: Throwable -> L22
        if (r1 != 1) goto L15;
    L16:
        BlackBox.event("TV_MANIFEST_WARM_WAIT", "attempt=" + r18.attempts + " active=" + r0.tvWarmActive() + " ready=" + r0.tvWarmReady());     // Catch: Throwable -> L22
    L18:
        if (r18.attempts >= 30) goto L20;
        r18.main.postDelayed(r18.poll, 300);     // Catch: Throwable -> L22
    L20:
        monitor-exit(r18);     // Catch: Throwable -> L22
        return;
    L15:
        if (r18.attempts != 30) goto L18;
    L22:
        th = move-exception;
        throw th;
    L25:
        List<Entry> r9 = r0.tvNeighbors();
        Entry r10 = Entry.details(r18.current);
        Iterator<Entry> r11 = r9.iterator();
        int r2 = 0;
        int r12 = 0;
        int r13 = 0;
        int r14 = 0;
    L27:
        if (r11.hasNext() == false) goto L28;
        Entry r3 = Entry.details(r11.next());
        String r4 = StreamFormat.mime(r3);
        if (r18.plain == false) goto L35;
        if (MimeTypes.APPLICATION_M3U8.equals(r4) == true) goto L35;
    L38:
        r12 = r12 + 1;
    L35:
        if (MimeTypes.APPLICATION_MPD.equals(r4) == true) goto L40;
        if (MimeTypes.APPLICATION_M3U8.equals(r4) == false) goto L38;
    L40:
        if (r3.headers.equals(r10.headers) == false) goto L41;
        if (r18.plain == false) goto L48;
        if (plainNeighbor(r10.url, r3.url) == true) goto L50;
    L49:
        r14 = r14 + 1;
    L50:
        int r15 = r2 + 1;
        monitor-enter(r18);
        final int r5 = r18.generation;     // Catch: Throwable -> L68
        final String r6 = r18.auth;     // Catch: Throwable -> L68
        final DataSource.Factory r7 = r18.network;     // Catch: Throwable -> L68
        if (r7 != null) goto L56;
        monitor-exit(r18);     // Catch: Throwable -> L68
        List<Entry> r17 = r9;
    L67:
        r2 = r15;
        r9 = r17;
        goto L27
    L56:
        final LinkedHashMap r8 = new LinkedHashMap(r3.headers);     // Catch: Throwable -> L68
        final String r16 = Entry.details(r3).url;     // Catch: Throwable -> L68
        r17 = r9;
        r18.jobs.add(POOL.submit(new TvManifestWarm$$ExternalSyntheticLambda2(r18, r5, r7, r16, r6, r8)));     // Catch: RejectedExecutionException -> L61 Throwable -> L68
    L66:
        monitor-exit(r18);     // Catch: Throwable -> L68
    L65:
        BlackBox.event("TV_MANIFEST_WARM_SKIPPED", "workers_busy=true");     // Catch: Throwable -> L68
    L64:
        r17 = r9;
    L68:
        th = move-exception;
        throw th;
    L48:
        if (CdnTokens.allowedHost(r3.url) == true) goto L50;
    L41:
        r13 = r13 + 1;
        goto L27
    L28:
        BlackBox.event("TV_MANIFEST_WARM_PLAN", "neighbors=" + r9.size() + " accepted=" + r2 + " format=" + r12 + " headers=" + r13 + " origin=" + r14);
    }

    public static void release(Object r4) {
        if ((r4 instanceof PlayerActivity) == true) goto L5;
        return;
    L5:
        PlayerActivity r5 = (PlayerActivity) r4;
        Map<PlayerActivity, State> r0 = STATES;
        monitor-enter(r0);
        State r1 = STATES.get(r5);     // Catch: Throwable -> L36
        if (r1 != null) goto L12;
        monitor-exit(r0);     // Catch: Throwable -> L36
        return;
    L12:
        if (r5.tvWarmActive() == true) goto L14;
    L17:
        STATES.remove(r5);     // Catch: Throwable -> L36
    L18:
        monitor-exit(r0);     // Catch: Throwable -> L36
        monitor-enter(r1);
        r1.generation++;     // Catch: Throwable -> L33
        r1.main.removeCallbacks(r1.poll);     // Catch: Throwable -> L33
        Iterator<Future<?>> r2 = r1.jobs.iterator();     // Catch: Throwable -> L33
    L22:
        if (r2.hasNext() == false) goto L23;
        r2.next().cancel(true);     // Catch: Throwable -> L33
        goto L22
    L23:
        r1.jobs.clear();     // Catch: Throwable -> L33
        if (r5.tvWarmActive() == true) goto L26;
    L29:
        r1.cache.clear();     // Catch: Throwable -> L33
        r1.network = null;     // Catch: Throwable -> L33
    L30:
        monitor-exit(r1);     // Catch: Throwable -> L33
        return;
    L26:
        if (r5.isFinishing() == true) goto L29;
        if (r5.isDestroyed() == false) goto L30;
    L33:
        th = move-exception;
        throw th;
    L14:
        if (r5.isFinishing() == true) goto L17;
        if (r5.isDestroyed() == false) goto L18;
    L36:
        th = move-exception;
        throw th;
    }

    static Payload take(State r0, String r1, String r2, Map<String, String> r3) {
        return r0.cache.take(r1, r2, r3);
    }
}
