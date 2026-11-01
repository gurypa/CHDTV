package es.antonio.m3u;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.LruCache;
import android.widget.ImageView;
import com.google.common.base.Ascii;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes3.dex */
final class LogoLoader {
    private final LruCache<String, Bitmap> cache;
    private volatile boolean closed;
    private final File directory;
    private final LruCache<String, Long> failed;
    private final Map<ImageView, List<String>> fallbacks;
    private final Fetcher fetcher;
    private final Handler main;
    private volatile boolean paused;
    private final Map<String, List<WeakReference<ImageView>>> pending;
    private final ThreadPoolExecutor pool;

    interface Fetcher {
        byte[] get(String r1) throws Exception;
    }

    private final class LogoJob implements Runnable {
        final /* synthetic */ LogoLoader this$0;
        final String url;

        LogoJob(LogoLoader r1, String r2) {
            this.this$0 = r1;
            this.url = r2;
        }

        @Override // java.lang.Runnable
        public void run() {
            LogoLoader.m583$$Nest$mload(this.this$0, this.url);
        }
    }

    public static /* synthetic */ void $r8$lambda$I31lIJi7AC2GQoA0bTgZQULyF3c(LogoLoader r0, String r1, Bitmap r2) {
        r0.lambda$load$2(r1, r2);
    }

    public static /* synthetic */ byte[] $r8$lambda$baWh2GYaGjADymkIV1m9pcKRmvE(String r0) {
        return download(r0);
    }

    /* JADX INFO: renamed from: -$$Nest$mload, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m583$$Nest$mload(LogoLoader r0, String r1) {
        r0.load(r1);
    }

    LogoLoader(Context r2) {
        this(r2, new LogoLoader$$ExternalSyntheticLambda5());
    }

    LogoLoader(Context r12, Fetcher r13) {
        this.main = new Handler(Looper.getMainLooper());
        final int r1 = 12582912;
        this.cache = new C09991(this, r1);
        this.pool = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(32), new LogoLoader$$ExternalSyntheticLambda3(), new ThreadPoolExecutor.AbortPolicy());
        this.failed = new LruCache(256);
        this.pending = new HashMap();
        this.fallbacks = new WeakHashMap();
        this.fetcher = r13;
        this.directory = new File(DemandArtwork.root(r12), "artwork-v1");
        this.directory.mkdirs();
    }

    private static Bitmap decode(byte[] r5) throws IOException {
        BitmapFactory.Options r0 = new BitmapFactory.Options();
        int r1 = 1;
        r0.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(r5, 0, r5.length, r0);
        if (r0.outWidth <= 0) goto L15;
        if (r0.outHeight <= 0) goto L15;
    L6:
        r0.inSampleSize = r1;
        if ((r0.outWidth / r0.inSampleSize) > 384) goto L13;
        if ((r0.outHeight / r0.inSampleSize) > 384) goto L13;
        r0.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(r5, 0, r5.length, r0);
    L13:
        r1 = r0.inSampleSize * 2;
    L15:
        throw new IOException();
    }

    private static byte[] download(String r4) throws Exception {
        HttpURLConnection r0 = (HttpURLConnection) new URL(r4).openConnection();
        r0.setConnectTimeout(8000);
        r0.setReadTimeout(8000);
    L20:
        th = move-exception;
        r0.disconnect();
        throw th;
    L4:
        if (r0.getResponseCode() != 200) goto L19;
        InputStream r1 = r0.getInputStream();     // Catch: Throwable -> L20
        byte[] r2 = read(r1);     // Catch: Throwable -> L11
        if (r1 == null) goto L9;
        r1.close();     // Catch: Throwable -> L20
    L9:
        r0.disconnect();
        return r2;
    L11:
        th = move-exception;
        if (r1 != null) goto L23;
    L17:
        throw th;     // Catch: Throwable -> L20
    L23:
        r1.close();     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L20
        goto L17
    L19:
        throw new IOException();     // Catch: Throwable -> L20
    }

    private static String hash(String r7) throws Exception {
        byte[] r0 = MessageDigest.getInstance("SHA-256").digest(r7.getBytes("UTF-8"));
        StringBuilder r1 = new StringBuilder();
        int r2 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L6;
        byte r4 = r0[r3];
        r1.append(Character.forDigit((r4 >> 4) & 15, 16));
        r1.append(Character.forDigit(r4 & Ascii.f88SI, 16));
        r3 = r3 + 1;
        goto L3
    L6:
        return r1.toString();
    }

    private /* synthetic */ void lambda$load$2(String r7, Bitmap r8) {
        Map<String, List<WeakReference<ImageView>>> r0 = this.pending;
        monitor-enter(r0);
        List<WeakReference<ImageView>> r1 = this.pending.remove(r7);     // Catch: Throwable -> L27
        monitor-exit(r0);     // Catch: Throwable -> L27
        if (this.closed == false) goto L9;
        return;
    L9:
        if (this.paused == true) goto L46;
        if (r1 == null) goto L47;
        Iterator<WeakReference<ImageView>> r2 = r1.iterator();
    L14:
        if (r2.hasNext() == false) goto L48;
        ImageView r3 = r2.next().get();
        if (r3 == null) goto L14;
        if (r7.equals(r3.getTag()) == false) goto L14;
        if (r8 != null) goto L20;
        List<String> r4 = this.fallbacks.get(r3);
        if (r4 == null) goto L14;
        if (r4.isEmpty() == true) goto L14;
        show(r3, r4);
        goto L14
    L20:
        setBitmap(r3, r8);
        goto L14
    L48:
        return;
    L47:
        return;
    L46:
        return;
    L27:
        th = move-exception;
        throw th;
    }

    static /* synthetic */ void lambda$new$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$new$1(final Runnable r3) {
        return new Thread(new LogoLoader$$ExternalSyntheticLambda0(r3), "CHDTVArtwork");
    }

    static /* synthetic */ boolean lambda$save$3(File r1, String r2) {
        return r2.endsWith(".png");
    }

    static /* synthetic */ int lambda$save$4(File r4, File r5) {
        return Long.compare(r4.lastModified(), r5.lastModified());
    }

    private void load(String r12) {
        if (this.closed == false) goto L5;
        return;
    L5:
        Bitmap r1 = null;
        Throwable r2 = null;
        r2 = null;
        r1 = null;
        r1 = null;
        r1 = null;
        if (this.paused == false) goto L9;
        this.main.post(new DemandArtwork.Ready(this, r12, null));
        return;
    L9:
        long r3 = SystemClock.elapsedRealtime();
        String r0 = "network";
        File r4 = new File(this.directory, String.valueOf(hash(r12)) + ".png");     // Catch: Exception -> L60
        File r5 = DemandArtwork.previous(this.directory, r4);     // Catch: Exception -> L60
        if (r4.isFile() == false) goto L13;
        r5 = r4;
    L13:
        if (r5 != null) goto L15;
    L48:
        if (r1 != null) goto L57;
        r1 = decode(this.fetcher.get(r12));     // Catch: Exception -> L60
        if (r1 == null) goto L55;
        if (this.closed == true) goto L57;
        save(r4, r1);     // Catch: Exception -> L60
        goto L57
    L55:
        throw new IOException();     // Catch: Exception -> L60
    L57:
        if (this.closed == true) goto L62;
        this.cache.put(r12, r1);     // Catch: Exception -> L60
    L62:
        StringBuilder r6 = new StringBuilder("duration_ms=").append(SystemClock.elapsedRealtime() - r3).append(" success=");
        if (r1 == null) goto L65;
        boolean r7 = true;
    L66:
        BlackBox.event("ARTWORK_LOAD", r6.append(r7).append(" source=").append(r0).toString());
        if (this.closed == true) goto L85;
        this.main.post(new DemandArtwork.Ready(this, r12, r1));
        return;
    L85:
        return;
    L65:
        r7 = false;
        goto L66
    L15:
        if (r5.isFile() == false) goto L48;
        if (r5.length() > PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE) goto L48;
        FileInputStream r8 = new FileInputStream(r5);     // Catch: Throwable -> L30
        Bitmap r9 = decode(read(r8));     // Catch: Throwable -> L25
        r8.close();     // Catch: Throwable -> L23
    L22:
        r1 = r9;
        if (r1 == null) goto L48;
        r0 = "disk";
        r5.setLastModified(System.currentTimeMillis());     // Catch: Exception -> L60
        if (r5.equals(r4) == true) goto L48;
        if (this.closed == true) goto L48;
        save(r4, r1);     // Catch: Exception -> L60
    L23:
        th = th;
    L32:
        if (r2 == null) goto L36;
        if (r2 == th) goto L83;
        r2.addSuppressed(th);     // Catch: Exception -> L37
        throw r2;     // Catch: Exception -> L37
    L81:
        r5.delete();     // Catch: Exception -> L46
    L46:
        e = e;
        r1 = r9;
    L61:
        BlackBox.failure("ARTWORK_ERROR", e);
        this.failed.put(r12, Long.valueOf(System.currentTimeMillis()));
        goto L62
    L83:
        throw r2;     // Catch: Exception -> L37
    L36:
        throw th;     // Catch: Exception -> L37
    L25:
        th = move-exception;
        r8.close();     // Catch: Throwable -> L28
        throw th;     // Catch: Throwable -> L28
    L28:
        th = th;
        r9 = null;
        r2 = th;
    L30:
        th = th;
        r9 = null;
    L60:
        e = e;
        goto L61
    }

    private static byte[] read(InputStream r1) throws IOException {
        return DemandImageInput.read(r1, 8388608);
    }

    private synchronized void save(File r2, Bitmap r3) {
        monitor-enter(this);
        DemandArtwork.save(this.directory, r2, r3);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    private static void setBitmap(ImageView r1, Bitmap r2) {
        if ((r1.getDrawable() instanceof BitmapDrawable) == true) goto L5;
    L7:
        r1.setImageBitmap(r2);
        return;
    L5:
        if (((BitmapDrawable) r1.getDrawable()).getBitmap() != r2) goto L7;
    }

    static String thumbnail(String r3) {
        if (r3 != null) goto L6;
        return "";
    L6:
        return r3.replace("https://image.tmdb.org/t/p/original/", "https://image.tmdb.org/t/p/w342/").replace("http://image.tmdb.org/t/p/original/", "https://image.tmdb.org/t/p/w342/");
    }

    public boolean artworkCached(String r5) {
        File r1 = new File(this.directory, String.valueOf(hash(r5)) + ".png");     // Catch: Exception -> L11
        File r6 = DemandArtwork.previous(this.directory, r1);     // Catch: Exception -> L11
        if (r1.isFile() == true) goto L9;
        if (r6 == null) goto L8;
        if (r6.isFile() == false) goto L8;
        return true;
    L8:
        return false;
    L9:
        return true;
    L12:
        return false;
    }

    public void artworkDiscard(String r3) {
        Map<String, List<WeakReference<ImageView>>> r0 = this.pending;
        monitor-enter(r0);
        this.pending.remove(r3);     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public void artworkReady(String r1, Bitmap r2) {
        lambda$load$2(r1, r2);
    }

    public boolean artworkWanted(String r5) {
        if (this.closed == false) goto L5;
    L28:
        return false;
    L5:
        if (this.paused == true) goto L28;
        Map<String, List<WeakReference<ImageView>>> r0 = this.pending;
        monitor-enter(r0);
        List<WeakReference<ImageView>> r2 = this.pending.get(r5);     // Catch: Throwable -> L25
        if (r2 != null) goto L13;
        monitor-exit(r0);     // Catch: Throwable -> L25
        return false;
    L13:
        Iterator<WeakReference<ImageView>> r3 = r2.iterator();     // Catch: Throwable -> L25
    L15:
        if (r3.hasNext() == false) goto L16;
        ImageView r4 = r3.next().get();     // Catch: Throwable -> L25
        if (r4 == null) goto L15;
        if (r5.equals(r4.getTag()) == false) goto L15;
        monitor-exit(r0);     // Catch: Throwable -> L25
        return true;
    L16:
        monitor-exit(r0);     // Catch: Throwable -> L25
        return false;
    L25:
        th = move-exception;
        throw th;
    }

    void close() {
        this.closed = true;
        this.pool.shutdownNow();
        this.main.removeCallbacksAndMessages(null);
        Map<String, List<WeakReference<ImageView>>> r0 = this.pending;
        monitor-enter(r0);
        this.pending.clear();     // Catch: Throwable -> L8
        monitor-exit(r0);     // Catch: Throwable -> L8
        this.cache.evictAll();
        return;
    L8:
        th = move-exception;
        throw th;
    }

    void pause() {
        this.paused = true;
        DemandArtwork.cancel(this, this.pool);
    }

    void resume() {
        this.paused = false;
    }

    void show(ImageView r2, String r3) {
        show(r2, r3, "");
    }

    void show(ImageView r2, String r3, String r4) {
        show(r2, Arrays.asList(new String[]{r3, r4}));
    }

    void show(ImageView r13, List<String> r14) {
        List<String> r0 = new ArrayList();
        Iterator<String> r1 = r14.iterator();
    L4:
        if (r1.hasNext() == false) goto L13;
        String r3 = thumbnail(r1.next());
        if (r3.startsWith("https://") == true) goto L10;
        if (r3.startsWith("http://") == false) goto L4;
    L10:
        if (r0.contains(r3) == true) goto L4;
        r0.add(r3);
    L13:
        if (r0.size() <= 1) goto L21;
        Long r2 = this.failed.get(r0.get(0));
        if (r2 == null) goto L21;
        if ((System.currentTimeMillis() - r2.longValue()) >= 300000) goto L21;
        r0.remove(0);
    L21:
        if (r0.isEmpty() == false) goto L23;
        String r4 = "";
    L24:
        this.fallbacks.put(r13, r0);
        String r5 = thumbnail(r4);
        boolean r6 = r5.equals(r13.getTag());
        r13.setTag(r5);
        if (this.closed == false) goto L27;
        return;
    L27:
        if (this.paused == true) goto L114;
        Bitmap r7 = this.cache.get(r5);
        if (r7 == null) goto L33;
        setBitmap(r13, r7);
        return;
    L33:
        if (r6 == true) goto L39;
        if (AppTheme.cine(r13.getContext()) == false) goto L37;
        r13.setImageDrawable(new CinePlaceholder(r13.getContext()));
        goto L39
    L37:
        r13.setImageResource(C1028R.drawable.artwork_placeholder);
    L39:
        if (r5.startsWith("https://") == false) goto L41;
    L43:
        Long r8 = this.failed.get(r5);
        if (r8 != null) goto L46;
    L53:
        Map<String, List<WeakReference<ImageView>>> r9 = this.pending;
        monitor-enter(r9);
        List<WeakReference<ImageView>> r10 = this.pending.get(r5);     // Catch: Throwable -> L84
        if (r10 == null) goto L71;
        Iterator<WeakReference<ImageView>> r11 = r10.iterator();     // Catch: Throwable -> L84
    L59:
        if (r11.hasNext() == false) goto L67;
        ImageView r12 = r11.next().get();     // Catch: Throwable -> L84
        if (r12 == null) goto L62;
        if (r12 != r13) goto L59;
        monitor-exit(r9);     // Catch: Throwable -> L84
        return;
    L62:
        r11.remove();     // Catch: Throwable -> L84
        goto L59
    L67:
        if (r10.size() >= 64) goto L69;
        r10.add(new WeakReference(r13));     // Catch: Throwable -> L84
    L69:
        monitor-exit(r9);     // Catch: Throwable -> L84
        return;
    L71:
        ArrayList r15 = new ArrayList();     // Catch: Throwable -> L84
        r15.add(new WeakReference(r13));     // Catch: Throwable -> L84
        this.pending.put(r5, r15);     // Catch: Throwable -> L84
        monitor-exit(r9);     // Catch: Throwable -> L84
        DemandArtwork.dispatch(this, this.pool, new LogoJob(this, r5), r5);     // Catch: RejectedExecutionException -> L75
        return;
    L77:
        monitor-enter(this.pending);
        this.pending.remove(r5);     // Catch: Throwable -> L81
        return;
    L81:
        th = move-exception;
        throw th;
    L84:
        th = move-exception;
        throw th;
    L46:
        if ((System.currentTimeMillis() - r8.longValue()) >= 300000) goto L53;
        List<String> r16 = this.fallbacks.get(r13);
        if (r16 != null) goto L50;
        return;
    L50:
        if (r16.isEmpty() == true) goto L112;
        show(r13, r16);
        return;
    L112:
        return;
    L41:
        if (r5.startsWith("http://") == true) goto L43;
        return;
    L114:
        return;
    L23:
        r4 = r0.remove(0);
        goto L24
    }
}
