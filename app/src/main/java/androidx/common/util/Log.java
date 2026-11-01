package androidx.media3.common.util;

import android.text.TextUtils;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.UnknownHostException;
import org.checkerframework.dataflow.qual.Pure;

/* JADX INFO: loaded from: classes.dex */
public final class Log {
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_ERROR = 3;
    public static final int LOG_LEVEL_INFO = 1;
    public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
    public static final int LOG_LEVEL_WARNING = 2;
    private static final Object lock = null;
    private static int logLevel;
    private static boolean logStackTraces;
    private static Logger logger;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevel {
    }

    public interface Logger {
        public static final Logger DEFAULT = null;

        static {
            DEFAULT = new C01991();
        }

        /* JADX INFO: renamed from: d */
        void mo50d(String r1, String r2, Throwable r3);

        /* JADX INFO: renamed from: e */
        void mo51e(String r1, String r2, Throwable r3);

        /* JADX INFO: renamed from: i */
        void mo52i(String r1, String r2, Throwable r3);

        /* JADX INFO: renamed from: w */
        void mo53w(String r1, String r2, Throwable r3);
    }

    static {
        lock = new Object();
        logLevel = 0;
        logStackTraces = true;
        logger = Logger.DEFAULT;
    }

    private Log() {
    }

    @Pure
    public static String appendThrowableString(String r4, Throwable r5) {
        String r0 = getThrowableString(r5);
        if (TextUtils.isEmpty(r0) == false) goto L5;
        return r4;
    L5:
        return r4 + "\n  " + r0.replace("\n", "\n  ") + '\n';
    }

    @Pure
    /* JADX INFO: renamed from: d */
    public static void m42d(String r3, String r4) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel != 0) goto L7;
        logger.mo50d(r3, r4, null);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    /* JADX INFO: renamed from: d */
    public static void m43d(String r2, String r3, Throwable r4) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel != 0) goto L7;
        logger.mo50d(r2, r3, r4);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    /* JADX INFO: renamed from: e */
    public static void m44e(String r3, String r4) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel > 3) goto L7;
        logger.mo51e(r3, r4, null);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    /* JADX INFO: renamed from: e */
    public static void m45e(String r3, String r4, Throwable r5) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel > 3) goto L7;
        logger.mo51e(r3, r4, r5);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    public static int getLogLevel() {
        Object r0 = lock;
        monitor-enter(r0);
        int r1 = logLevel;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Pure
    public static String getThrowableString(Throwable r4) {
        if (r4 != null) goto L5;
        return null;
    L5:
        Object r0 = lock;
        monitor-enter(r0);
    L20:
        th = move-exception;
        throw th;
    L8:
        if (isCausedByUnknownHostException(r4) == false) goto L13;
        monitor-exit(r0);     // Catch: Throwable -> L20
        return "UnknownHostException (no network)";
    L13:
        if (logStackTraces == true) goto L17;
        String r1 = r4.getMessage();     // Catch: Throwable -> L20
        monitor-exit(r0);     // Catch: Throwable -> L20
        return r1;
    L17:
        String r2 = android.util.Log.getStackTraceString(r4).trim().replace("\t", "    ");     // Catch: Throwable -> L20
        monitor-exit(r0);     // Catch: Throwable -> L20
        return r2;
    }

    @Pure
    /* JADX INFO: renamed from: i */
    public static void m46i(String r3, String r4) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel > 1) goto L7;
        logger.mo52i(r3, r4, null);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    /* JADX INFO: renamed from: i */
    public static void m47i(String r3, String r4, Throwable r5) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel > 1) goto L7;
        logger.mo52i(r3, r4, r5);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    private static boolean isCausedByUnknownHostException(Throwable r1) {
    L2:
        if (r1 == null) goto L8;
        if ((r1 instanceof UnknownHostException) == true) goto L5;
        r1 = r1.getCause();
        goto L2
    L5:
        return true;
    L8:
        return false;
    }

    public static void setLogLevel(int r2) {
        Object r0 = lock;
        monitor-enter(r0);
        logLevel = r2;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public static void setLogStackTraces(boolean r2) {
        Object r0 = lock;
        monitor-enter(r0);
        logStackTraces = r2;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public static void setLogger(Logger r2) {
        Object r0 = lock;
        monitor-enter(r0);
        logger = r2;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    @Pure
    /* JADX INFO: renamed from: w */
    public static void m48w(String r3, String r4) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel > 2) goto L7;
        logger.mo53w(r3, r4, null);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Pure
    /* JADX INFO: renamed from: w */
    public static void m49w(String r3, String r4, Throwable r5) {
        Object r0 = lock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (logLevel > 2) goto L7;
        logger.mo53w(r3, r4, r5);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }
}
