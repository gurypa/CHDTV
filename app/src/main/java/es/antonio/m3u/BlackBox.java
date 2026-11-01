package es.antonio.m3u;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import androidx.core.os.EnvironmentCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes3.dex */
public final class BlackBox {
    static final long LIMIT = 1048576;
    private static Context app;
    private static final Object disk = null;
    private static final AtomicInteger dropped = null;
    private static volatile boolean enabled;
    private static boolean handlerInstalled;

    /* JADX INFO: renamed from: io */
    private static final ThreadPoolExecutor f176io = null;
    private static long lastStack;
    private static Handler main;
    private static final ScheduledExecutorService monitor = null;
    private static volatile long pendingAt;
    private static final Map<Activity, Boolean> screens = null;
    private static volatile boolean visible;
    private static ScheduledFuture<?> watch;

    interface Export {
        void done(File r1, Exception r2);
    }

    private static final class InputRecorder implements InvocationHandler {
        final Window.Callback original;
        final String screen;

        InputRecorder(Window.Callback r1, String r2) {
            this.original = r1;
            this.screen = r2;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object r5, Method r6, Object[] r7) throws Throwable {
            if (BlackBox.m525$$Nest$sfgetenabled() == false) goto L44;
            if (r7 == null) goto L44;
            if (r7.length <= 0) goto L44;
            if (r6.getName().equals("dispatchTouchEvent") == false) goto L15;
            if ((r7[0] instanceof MotionEvent) == false) goto L15;
            if (((MotionEvent) r7[0]).getActionMasked() != 1) goto L15;
            BlackBox.event("INPUT", this.screen + " touch");
        L15:
            if (r6.getName().equals("dispatchKeyEvent") == false) goto L44;
            if ((r7[0] instanceof KeyEvent) == false) goto L44;
            KeyEvent r0 = (KeyEvent) r7[0];
            int r1 = r0.getKeyCode();
            if (r0.getAction() != 0) goto L44;
            if (r0.getRepeatCount() != 0) goto L44;
            if (r1 < 19) goto L27;
            if (r1 > 23) goto L27;
        L38:
            BlackBox.event("NAV_KEY", this.screen + " code=" + r1);
        L27:
            if (r1 == 4) goto L38;
            if (r1 == 66) goto L38;
            if (r1 == 82) goto L38;
            if (r1 == 24) goto L38;
            if (r1 == 25) goto L38;
            if (r1 == 85) goto L38;
        L44:
            return r6.invoke(this.original, r7);
        L41:
            e = move-exception;
            throw e.getCause();
        }
    }

    public static /* synthetic */ void $r8$lambda$iXQOsXH8WCyIvo2atSlHN9qlNOI() {
        sample();
    }

    /* JADX INFO: renamed from: -$$Nest$sfgetenabled, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m525$$Nest$sfgetenabled() {
        return enabled;
    }

    static {
        disk = new Object();
        dropped = new AtomicInteger();
        f176io = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue(128), new BlackBox$$ExternalSyntheticLambda7(), new ThreadPoolExecutor.AbortPolicy());
        monitor = Executors.newSingleThreadScheduledExecutor(new BlackBox$$ExternalSyntheticLambda8());
        screens = Collections.synchronizedMap(new WeakHashMap());
    }

    public BlackBox() {
    }

    static boolean active() {
        return enabled;
    }

    static void attach(Activity r6) {
        screens.put(r6, true);
        if (enabled == true) goto L5;
        return;
    L5:
        Window.Callback r0 = r6.getWindow().getCallback();
        if (r0 != null) goto L9;
        return;
    L9:
        if (Proxy.isProxyClass(r0.getClass()) == true) goto L11;
    L13:
        r6.getWindow().setCallback((Window.Callback) Proxy.newProxyInstance(Window.Callback.class.getClassLoader(), new Class[]{Window.Callback.class}, new InputRecorder(r0, r6.getClass().getSimpleName())));
        return;
    L11:
        if ((Proxy.getInvocationHandler(r0) instanceof InputRecorder) == false) goto L13;
    }

    static String clean(String r4) {
        if (r4 != null) goto L5;
        return "";
    L5:
        String r0 = r4.replaceAll("(?i)https?://[^\\s]+", "[URL]").replaceAll("(?i)(?:bearer\\s+|basic\\s+|(?:token|password|secret|authorization|cookie|license|key)\\s*[:=]\\s*(?:Bearer\\s+|Basic\\s+)?)[^\\s,;]+", "[SECRETO]").replaceAll("[A-Za-z0-9_-]{40,}", "[SECRETO]");
        return r0.replace('\n', ' ').replace('\r', ' ').substring(0, Math.min(r0.length(), 1200));
    }

    static void clear() {
        submit(new BlackBox$$ExternalSyntheticLambda16());
    }

    private static File current() {
        return new File(app.getFilesDir(), "black-box.txt");
    }

    static void destroyed(Activity r1) {
        detach(r1);
        screens.remove(r1);
    }

    static void detach(Activity r4) {
        Window.Callback r0 = r4.getWindow().getCallback();
        if (r0 != null) goto L5;
        return;
    L5:
        if (Proxy.isProxyClass(r0.getClass()) == false) goto L11;
        InvocationHandler r1 = Proxy.getInvocationHandler(r0);
        if ((r1 instanceof InputRecorder) == false) goto L12;
        r4.getWindow().setCallback(((InputRecorder) r1).original);
        return;
    L12:
        return;
    }

    static String device() {
        return "version=2.27-sampleaes-tv-mobile-test sdk=" + Build.VERSION.SDK_INT + " device=" + clean(Build.MANUFACTURER + " " + Build.MODEL);
    }

    public static void event(String r7, String r8) {
        if (enabled == true) goto L5;
        return;
    L5:
        if (app == null) goto L10;
        final String r2 = clean(r7) + " " + clean(r8) + " thread=" + clean(Thread.currentThread().getName());
        final long r3 = System.currentTimeMillis();
        final long r5 = SystemClock.elapsedRealtime();
        submit(new BlackBox$$ExternalSyntheticLambda1(r2, r3, r5));
        return;
    }

    static void failure(String r1, Throwable r2) {
        PlaybackReason222.record(r1, r2);
        if (TvHlsOverlap.expectedCancellation(r1, r2) == false) goto L6;
        return;
    L6:
        if (enabled == false) goto L9;
        event(r1, trace(r2));
        return;
    }

    static void foreground(boolean r2) {
        visible = r2;
        if (r2 == true) goto L6;
        pendingAt = 0;
        return;
    }

    static void init(Context r3) {
        HlsDiagnostics225.init(r3);
        LocalPanel.init(r3);
        app = r3.getApplicationContext();
        main = new Handler(Looper.getMainLooper());
        enabled = app.getSharedPreferences("black-box", 0).getBoolean("enabled", false);
        if (handlerInstalled == true) goto L6;
        handlerInstalled = true;
        final Thread.UncaughtExceptionHandler r0 = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new BlackBox$$ExternalSyntheticLambda2(r0));
    L6:
        if (enabled == false) goto L9;
        event("SESSION_RESTART", device());
        watch();
        LocalPanel.ready();
        return;
    }

    static /* synthetic */ boolean lambda$clear$14(File r2) {
        if (r2.getName().startsWith("CHDTV_caja_negra_") == true) goto L5;
    L7:
        return false;
    L5:
        if (r2.getName().endsWith(".txt") == false) goto L7;
        return true;
    }

    static /* synthetic */ void lambda$clear$15() {
        Object r0 = disk;
        monitor-enter(r0);
        current().delete();     // Catch: Throwable -> L14
        previous().delete();     // Catch: Throwable -> L14
        File[] r2 = new File(app.getCacheDir(), "shared-backups").listFiles(new BlackBox$$ExternalSyntheticLambda10());     // Catch: Throwable -> L14
        if (r2 == null) goto L9;
        int r3 = r2.length;     // Catch: Throwable -> L14
        int r4 = 0;
    L7:
        if (r4 >= r3) goto L9;
        r2[r4].delete();     // Catch: Throwable -> L14
        r4 = r4 + 1;     // Catch: Throwable -> L14
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L14
        if (enabled == false) goto L20;
        write("REGISTRO_VACIADO");
        return;
    L20:
        return;
    L14:
        th = move-exception;
        throw th;
    }

    static /* synthetic */ void lambda$event$6(String r0, long r1, long r3) {
        writeAt(r0, r1, r3);
    }

    static /* synthetic */ void lambda$init$3(Thread.UncaughtExceptionHandler r2, Thread r3, Throwable r4) {
        recordCrash(r3, r4);     // Catch: Throwable -> L7
        if (r2 == null) goto L6;
    L5:
        r2.uncaughtException(r3, r4);
        return;
    L6:
        Process.killProcess(Process.myPid());
        System.exit(10);
        return;
    L8:
        if (r2 == null) goto L6;
        goto L6
    }

    static /* synthetic */ void lambda$recordCrash$4(Thread r2, String r3, CountDownLatch r4) {
        write("CRASH thread=" + clean(r2.getName()) + " " + r3);
        r4.countDown();
    }

    static /* synthetic */ void lambda$sample$8(long r4) {
        if (pendingAt == r4) goto L5;
        return;
    L5:
        pendingAt = 0;
        if (enabled == true) goto L8;
        return;
    L8:
        if (r4 > 0) goto L10;
        return;
    L10:
        if ((SystemClock.elapsedRealtime() - r4) <= 250) goto L15;
        event("UI_DELAY", "duration_ms=" + (SystemClock.elapsedRealtime() - r4));
        return;
    }

    static /* synthetic */ void lambda$snapshot$10(Export r1, File r2) {
        r1.done(r2, null);
    }

    static /* synthetic */ void lambda$snapshot$11(Export r1, Exception r2) {
        r1.done(null, r2);
    }

    static /* synthetic */ void lambda$snapshot$12(final Export r14) {
        File r0 = new File(app.getCacheDir(), "shared-backups");     // Catch: Exception -> L54
        if (r0.isDirectory() == false) goto L5;
    L9:
        final File r1 = new File(r0, "CHDTV_caja_negra_" + new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.ROOT).format(new Date()) + ".txt");     // Catch: Exception -> L54
        Object r2 = disk;     // Catch: Exception -> L54
        monitor-enter(r2);     // Catch: Exception -> L54
        FileOutputStream r3 = new FileOutputStream(r1);     // Catch: Throwable -> L51
        r3.write(("CHDTV · Caja negra\n" + device() + "\nNo contiene URLs, contraseñas, tokens ni claves.\n").getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L45
        File[] r7 = {previous(), current()};     // Catch: Throwable -> L45
        int r4 = 0;
    L13:
        if (r4 >= 2) goto L32;
        File r5 = r7[r4];     // Catch: Throwable -> L45
        if (r5.isFile() == false) goto L31;
        FileInputStream r9 = new FileInputStream(r5);     // Catch: Throwable -> L45
        byte[] r10 = new byte[8192];     // Catch: Throwable -> L25
    L19:
        int r11 = r9.read(r10);     // Catch: Throwable -> L25
        if (r11 == (-1)) goto L23;
        r3.write(r10, 0, r11);     // Catch: Throwable -> L25
    L23:
        r9.close();     // Catch: Throwable -> L45
    L25:
        th = move-exception;
        r9.close();     // Catch: Throwable -> L28
    L30:
        throw th;     // Catch: Throwable -> L45
    L28:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L45
    L31:
        r4 = r4 + 1;
        goto L13
    L32:
        r3.close();     // Catch: Throwable -> L51
        monitor-exit(r2);     // Catch: Throwable -> L51
        File[] r6 = r0.listFiles(new BlackBox$$ExternalSyntheticLambda12());     // Catch: Exception -> L54
        if (r6 == null) goto L43;
        Arrays.sort(r6, Comparator.comparingLong(new BlackBox$$ExternalSyntheticLambda13()).reversed());     // Catch: Exception -> L54
        int r8 = 6;
    L38:
        if (r8 >= r6.length) goto L43;
        if (r6[r8].equals(r1) == true) goto L42;
        r6[r8].delete();     // Catch: Exception -> L54
    L42:
        r8 = r8 + 1;     // Catch: Exception -> L54
    L43:
        main.post(new BlackBox$$ExternalSyntheticLambda14(r14, r1));     // Catch: Exception -> L54
        return;
    L45:
        th = move-exception;
        r3.close();     // Catch: Throwable -> L48
    L50:
        throw th;     // Catch: Throwable -> L51
    L48:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L51
    L51:
        th = move-exception;
        throw th;     // Catch: Exception -> L54
    L5:
        if (r0.mkdirs() == true) goto L9;
        throw new IOException();     // Catch: Exception -> L54
    L54:
        e = move-exception;
        main.post(new BlackBox$$ExternalSyntheticLambda15(r14, e));
    }

    static /* synthetic */ void lambda$snapshot$13(Export r2) {
        r2.done(null, new IOException("Registro ocupado. Inténtalo de nuevo."));
    }

    static /* synthetic */ boolean lambda$snapshot$9(File r2) {
        if (r2.getName().startsWith("CHDTV_caja_negra_") == true) goto L5;
    L7:
        return false;
    L5:
        if (r2.getName().endsWith(".txt") == false) goto L7;
        return true;
    }

    static /* synthetic */ void lambda$static$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$static$1(final Runnable r3) {
        return new Thread(new BlackBox$$ExternalSyntheticLambda11(r3), "CHDTVBlackBox");
    }

    static /* synthetic */ Thread lambda$static$2(Runnable r2) {
        return new Thread(r2, "CHDTVBlackBoxMonitor");
    }

    static /* synthetic */ void lambda$stop$5() {
        write("SESSION_STOP");
    }

    static /* synthetic */ void lambda$submit$7(Runnable r1) {
        r1.run();     // Catch: Throwable -> L4
        return;
    }

    private static List<Activity> live() {
        Map<Activity, Boolean> r0 = screens;
        monitor-enter(r0);
        ArrayList r1 = new ArrayList(screens.keySet());     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    private static File previous() {
        return new File(app.getFilesDir(), "black-box-previous.txt");
    }

    static void recordCrash(final Thread r6, Throwable r7) throws InterruptedException {
        if (enabled == true) goto L5;
        return;
    L5:
        final String r0 = trace(r7);
        final CountDownLatch r1 = new CountDownLatch(1);
        if (submit(new BlackBox$$ExternalSyntheticLambda9(r6, r0, r1)) == true) goto L10;
        StringBuilder r2 = new StringBuilder();
    L8:
        write(r2.append("CRASH ").append(r0).toString());
        return;
    L10:
        if (r1.await(1500, TimeUnit.MILLISECONDS) == true) goto L13;
        r2 = new StringBuilder();
        goto L8
    }

    private static void sample() {
        if (enabled == true) goto L5;
        return;
    L5:
        if (visible == false) goto L27;
        final long r0 = SystemClock.elapsedRealtime();
        if (pendingAt != 0) goto L11;
        pendingAt = r0;
        main.post(new BlackBox$$ExternalSyntheticLambda6(r0));
    L21:
        Runtime r2 = Runtime.getRuntime();
        event("PROCESS", "heap_used_bytes=" + (r2.totalMemory() - r2.freeMemory()) + " native_heap_bytes=" + Debug.getNativeHeapAllocatedSize() + " cpu_ms=" + Process.getElapsedCpuTime());
        return;
    L11:
        if ((r0 - pendingAt) < 1000) goto L21;
        if ((r0 - lastStack) < 5000) goto L21;
        lastStack = r0;
        StringBuilder r3 = new StringBuilder("blocked_ms=" + (r0 - pendingAt));
        StackTraceElement[] r4 = Looper.getMainLooper().getThread().getStackTrace();
        int r5 = r4.length;
        int r6 = 0;
    L15:
        if (r6 >= r5) goto L20;
        StackTraceElement r7 = r4[r6];
        if (r3.length() >= 1000) goto L19;
        r3.append(" | ").append(r7.toString());
    L19:
        r6 = r6 + 1;
        goto L15
    L20:
        event("UI_BLOCKED", r3.toString());
        goto L21
    }

    static void snapshot(final Export r2) {
        if (app != null) goto L7;
        r2.done(null, new IOException("Registro no inicializado"));
        return;
    L7:
        if (submit(new BlackBox$$ExternalSyntheticLambda3(r2)) == true) goto L10;
        main.post(new BlackBox$$ExternalSyntheticLambda4(r2));
        return;
    }

    static void start() {
        enabled = true;
        app.getSharedPreferences("black-box", 0).edit().putBoolean("enabled", true).apply();
        event("SESSION_START", device());
        Iterator<Activity> r0 = live().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        attach(r0.next());
        goto L4
    L6:
        watch();
    }

    static void stop() {
        if (enabled == false) goto L5;
        submit(new BlackBox$$ExternalSyntheticLambda0());
    L5:
        enabled = false;
        app.getSharedPreferences("black-box", 0).edit().putBoolean("enabled", false).apply();
        monitor-enter(BlackBox.class);
    L17:
        th = move-exception;
        throw th;
    L8:
        if (watch == null) goto L10;
        watch.cancel(false);     // Catch: Throwable -> L17
    L10:
        watch = null;     // Catch: Throwable -> L17
        monitor-exit(BlackBox.class);     // Catch: Throwable -> L17
        pendingAt = 0;
        Iterator<Activity> r0 = live().iterator();
    L14:
        if (r0.hasNext() == false) goto L16;
        detach(r0.next());
        goto L14
    }

    static String stream(Entry r9) {
        if (enabled == true) goto L6;
        return "";
    L6:
        String r0 = EnvironmentCompat.MEDIA_UNKNOWN;
        byte[] r1 = MessageDigest.getInstance("SHA-256").digest((r9.identity() + "\n" + StreamFormat.label(r9)).getBytes(StandardCharsets.UTF_8));     // Catch: Exception -> L13
        StringBuilder r2 = new StringBuilder();     // Catch: Exception -> L13
        int r3 = 0;
    L9:
        if (r3 >= 6) goto L11;
        r2.append(String.format(Locale.ROOT, "%02x", new Object[]{Integer.valueOf(r1[r3] & 255)}));     // Catch: Exception -> L13
        r3 = r3 + 1;     // Catch: Exception -> L13
        goto L9
    L11:
        r0 = r2.toString();     // Catch: Exception -> L13
    L15:
        return "entry_id=" + r0 + " kind=" + r9.kind + " format=" + StreamFormat.label(r9);
    }

    private static boolean submit(final Runnable r2) {
        f176io.execute(new BlackBox$$ExternalSyntheticLambda5(r2));     // Catch: RejectedExecutionException -> L5
        return true;
    L6:
        dropped.incrementAndGet();
        return false;
    }

    static String trace(Throwable r7) {
        StringBuilder r0 = new StringBuilder();
        Set r1 = Collections.newSetFromMap(new IdentityHashMap());
        Throwable r2 = r7;
    L3:
        if (r2 == null) goto L12;
        if (r1.add(r2) == false) goto L12;
        r0.append(r2.getClass().getSimpleName());
        StackTraceElement[] r3 = r2.getStackTrace();
        int r4 = 0;
    L8:
        if (r4 >= Math.min(20, r3.length)) goto L10;
        r0.append(" | ").append(clean(r3[r4].toString()));
        r4 = r4 + 1;
        goto L8
    L10:
        r0.append(" ; ");
        r2 = r2.getCause();
    L12:
        return r0.toString();
    }

    private static synchronized void watch() {
        monitor-enter(BlackBox.class);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (watch != null) goto L7;
    L10:
        watch = monitor.scheduleWithFixedDelay(new BlackBox$$ExternalSyntheticLambda17(), 1, 1, TimeUnit.SECONDS);     // Catch: Throwable -> L11
    L8:
        monitor-exit(BlackBox.class);
        return;
    L7:
        if (watch.isDone() == true) goto L10;
        goto L8
    }

    private static void write(String r4) {
        writeAt(r4, System.currentTimeMillis(), SystemClock.elapsedRealtime());
    }

    private static void writeAt(String r8, long r9, long r11) {
        LocalPanel.offer(r8, r9, r11);
        if (app != null) goto L5;
        return;
    L5:
        Object r0 = disk;
        monitor-enter(r0);
        File r1 = current();     // Catch: Throwable -> L33 IOException -> L35
        if (r1.length() <= 1048576) goto L19;
        File r2 = previous();     // Catch: Throwable -> L33 IOException -> L35
        if (r2.exists() == false) goto L16;
        if (r2.delete() == true) goto L16;
        monitor-exit(r0);     // Catch: Throwable -> L33
        return;
    L16:
        if (r1.renameTo(r2) == true) goto L19;
        monitor-exit(r0);     // Catch: Throwable -> L33
        return;
    L19:
        String r3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT).format(new Date(r9));     // Catch: Throwable -> L33 IOException -> L35
        int r4 = dropped.getAndSet(0);     // Catch: Throwable -> L33 IOException -> L35
        FileOutputStream r5 = new FileOutputStream(r1, true);     // Catch: Throwable -> L33 IOException -> L35
        StringBuilder r6 = new StringBuilder().append(r3).append(" uptime_ms=").append(r11).append(" ").append(r8);     // Catch: Throwable -> L27
        if (r4 <= 0) goto L23;
        String r7 = " dropped_events=" + r4;     // Catch: Throwable -> L27
    L24:
        r5.write(r6.append(r7).append("\n").toString().getBytes(StandardCharsets.UTF_8));     // Catch: Throwable -> L27
        r5.close();     // Catch: Throwable -> L33 IOException -> L35
        goto L36
    L23:
        r7 = "";
    L27:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L30
    L32:
        throw th;     // Catch: Throwable -> L33 IOException -> L35
    L30:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L33 IOException -> L35
    L33:
        th = move-exception;
        throw th;
    L36:
        monitor-exit(r0);     // Catch: Throwable -> L33
    }
}
