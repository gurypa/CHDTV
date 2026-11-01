package androidx.core.os;

import android.os.Build;
import android.os.Trace;
import android.util.Log;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class TraceCompat {
    private static final String TAG = "TraceCompat";
    private static Method sAsyncTraceBeginMethod;
    private static Method sAsyncTraceEndMethod;
    private static Method sIsTagEnabledMethod;
    private static Method sTraceCounterMethod;
    private static long sTraceTagApp;

    static class Api18Impl {
        private Api18Impl() {
        }

        static void beginSection(String r0) {
            Trace.beginSection(r0);
        }

        static void endSection() {
            Trace.endSection();
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static void beginAsyncSection(String r0, int r1) {
            Trace.beginAsyncSection(r0, r1);
        }

        static void endAsyncSection(String r0, int r1) {
            Trace.endAsyncSection(r0, r1);
        }

        static boolean isEnabled() {
            return Trace.isEnabled();
        }

        static void setCounter(String r0, long r1) {
            Trace.setCounter(r0, r1);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 29) goto L11;
        sTraceTagApp = Trace.class.getField("TRACE_TAG_APP").getLong(null);     // Catch: Exception -> L6
        sIsTagEnabledMethod = Trace.class.getMethod("isTagEnabled", new Class[]{Long.TYPE});     // Catch: Exception -> L6
        sAsyncTraceBeginMethod = Trace.class.getMethod("asyncTraceBegin", new Class[]{Long.TYPE, String.class, Integer.TYPE});     // Catch: Exception -> L6
        sAsyncTraceEndMethod = Trace.class.getMethod("asyncTraceEnd", new Class[]{Long.TYPE, String.class, Integer.TYPE});     // Catch: Exception -> L6
        sTraceCounterMethod = Trace.class.getMethod("traceCounter", new Class[]{Long.TYPE, String.class, Integer.TYPE});     // Catch: Exception -> L6
        return;
    L6:
        e = move-exception;
        Log.i(TAG, "Unable to initialize via reflection.", e);
        return;
    }

    private TraceCompat() {
    }

    public static void beginAsyncSection(String r5, int r6) {
        if (Build.VERSION.SDK_INT < 29) goto L11;
        Api29Impl.beginAsyncSection(r5, r6);
        return;
    L11:
        sAsyncTraceBeginMethod.invoke(null, new Object[]{Long.valueOf(sTraceTagApp), r5, Integer.valueOf(r6)});     // Catch: Exception -> L8
        return;
    L9:
        Log.v(TAG, "Unable to invoke asyncTraceBegin() via reflection.");
    }

    public static void beginSection(String r0) {
        Api18Impl.beginSection(r0);
    }

    public static void endAsyncSection(String r5, int r6) {
        if (Build.VERSION.SDK_INT < 29) goto L11;
        Api29Impl.endAsyncSection(r5, r6);
        return;
    L11:
        sAsyncTraceEndMethod.invoke(null, new Object[]{Long.valueOf(sTraceTagApp), r5, Integer.valueOf(r6)});     // Catch: Exception -> L8
        return;
    L9:
        Log.v(TAG, "Unable to invoke endAsyncSection() via reflection.");
    }

    public static void endSection() {
        Api18Impl.endSection();
    }

    public static boolean isEnabled() {
        if (Build.VERSION.SDK_INT < 29) goto L12;
        return Api29Impl.isEnabled();
    L12:
        return ((Boolean) sIsTagEnabledMethod.invoke(null, new Object[]{Long.valueOf(sTraceTagApp)})).booleanValue();
    L10:
        Log.v(TAG, "Unable to invoke isTagEnabled() via reflection.");
        return false;
    }

    public static void setCounter(String r5, int r6) {
        if (Build.VERSION.SDK_INT < 29) goto L11;
        Api29Impl.setCounter(r5, r6);
        return;
    L11:
        sTraceCounterMethod.invoke(null, new Object[]{Long.valueOf(sTraceTagApp), r5, Integer.valueOf(r6)});     // Catch: Exception -> L8
        return;
    L9:
        Log.v(TAG, "Unable to invoke traceCounter() via reflection.");
    }
}
