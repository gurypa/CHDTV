package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class ActivityRecreator {
    private static final String LOG_TAG = "ActivityRecreator";
    protected static final Class<?> activityThreadClass = null;
    private static final Handler mainHandler = null;
    protected static final Field mainThreadField = null;
    protected static final Method performStopActivity2ParamsMethod = null;
    protected static final Method performStopActivity3ParamsMethod = null;
    protected static final Method requestRelaunchActivityMethod = null;
    protected static final Field tokenField = null;

    private static final class LifecycleCheckCallbacks implements Application.ActivityLifecycleCallbacks {
        Object currentlyRecreatingToken;
        private Activity mActivity;
        private boolean mDestroyed;
        private final int mRecreatingHashCode;
        private boolean mStarted;
        private boolean mStopQueued;

        LifecycleCheckCallbacks(Activity r2) {
            this.mStarted = false;
            this.mDestroyed = false;
            this.mStopQueued = false;
            this.mActivity = r2;
            this.mRecreatingHashCode = this.mActivity.hashCode();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity r1, Bundle r2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity r2) {
            if (this.mActivity != r2) goto L6;
            this.mActivity = null;
            this.mDestroyed = true;
            return;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity r3) {
            if (this.mDestroyed == true) goto L5;
            return;
        L5:
            if (this.mStopQueued == false) goto L7;
            return;
        L7:
            if (this.mStarted == false) goto L9;
            return;
        L9:
            if (ActivityRecreator.queueOnStopIfNecessary(this.currentlyRecreatingToken, this.mRecreatingHashCode, r3) == false) goto L15;
            this.mStopQueued = true;
            this.currentlyRecreatingToken = null;
            return;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity r1) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity r1, Bundle r2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity r2) {
            if (this.mActivity != r2) goto L6;
            this.mStarted = true;
            return;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity r1) {
        }
    }

    static {
        mainHandler = new Handler(Looper.getMainLooper());
        activityThreadClass = getActivityThreadClass();
        mainThreadField = getMainThreadField();
        tokenField = getTokenField();
        performStopActivity3ParamsMethod = getPerformStopActivity3Params(activityThreadClass);
        performStopActivity2ParamsMethod = getPerformStopActivity2Params(activityThreadClass);
        requestRelaunchActivityMethod = getRequestRelaunchActivityMethod(activityThreadClass);
    }

    private ActivityRecreator() {
    }

    private static Class<?> getActivityThreadClass() {
        return Class.forName("android.app.ActivityThread");
    L5:
        return null;
    }

    private static Field getMainThreadField() {
        Field r0 = Activity.class.getDeclaredField("mMainThread");     // Catch: Throwable -> L4
        r0.setAccessible(true);     // Catch: Throwable -> L4
        return r0;
    L5:
        return null;
    }

    private static Method getPerformStopActivity2Params(Class<?> r5) {
        if (r5 != null) goto L9;
        return null;
    L9:
        Method r1 = r5.getDeclaredMethod("performStopActivity", new Class[]{IBinder.class, Boolean.TYPE});     // Catch: Throwable -> L7
        r1.setAccessible(true);     // Catch: Throwable -> L7
        return r1;
    L8:
        return null;
    }

    private static Method getPerformStopActivity3Params(Class<?> r6) {
        if (r6 != null) goto L9;
        return null;
    L9:
        Method r1 = r6.getDeclaredMethod("performStopActivity", new Class[]{IBinder.class, Boolean.TYPE, String.class});     // Catch: Throwable -> L7
        r1.setAccessible(true);     // Catch: Throwable -> L7
        return r1;
    L8:
        return null;
    }

    private static Method getRequestRelaunchActivityMethod(Class<?> r7) {
        if (needsRelaunchCall() == false) goto L10;
        if (r7 == null) goto L10;
        Class<?> r3 = Boolean.TYPE;     // Catch: Throwable -> L8
        Method r0 = r7.getDeclaredMethod("requestRelaunchActivity", new Class[]{IBinder.class, List.class, List.class, Integer.TYPE, r3, Configuration.class, Configuration.class, r3, r3});     // Catch: Throwable -> L8
        r0.setAccessible(true);     // Catch: Throwable -> L8
        return r0;
    L9:
        return null;
    L10:
        return null;
    }

    private static Field getTokenField() {
        Field r0 = Activity.class.getDeclaredField("mToken");     // Catch: Throwable -> L4
        r0.setAccessible(true);     // Catch: Throwable -> L4
        return r0;
    L5:
        return null;
    }

    private static boolean needsRelaunchCall() {
        if (Build.VERSION.SDK_INT != 26) goto L5;
    L8:
        return true;
    L5:
        if (Build.VERSION.SDK_INT == 27) goto L8;
        return false;
    }

    protected static boolean queueOnStopIfNecessary(Object r5, int r6, Activity r7) {
        final Object r1 = tokenField.get(r7);     // Catch: Throwable -> L12
        if (r1 == r5) goto L6;
    L11:
        return false;
    L6:
        if (r7.hashCode() != r6) goto L11;
        final Object r2 = mainThreadField.get(r7);     // Catch: Throwable -> L12
        mainHandler.postAtFrontOfQueue(new RunnableC00503(r2, r1));     // Catch: Throwable -> L12
        return true;
    L12:
        th = move-exception;
        Log.e(LOG_TAG, "Exception while fetching field values", th);
        return false;
    }

    static boolean recreate(Activity r14) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        r14.recreate();
        return true;
    L7:
        if (needsRelaunchCall() == false) goto L12;
        if (requestRelaunchActivityMethod != null) goto L12;
        return false;
    L12:
        if (performStopActivity2ParamsMethod == null) goto L14;
    L36:
        final Object r0 = tokenField.get(r14);     // Catch: Throwable -> L33
        if (r0 != null) goto L19;
        return false;
    L19:
        Object r3 = mainThreadField.get(r14);     // Catch: Throwable -> L33
        if (r3 != null) goto L22;
        return false;
    L22:
        final Application r4 = r14.getApplication();     // Catch: Throwable -> L33
        final LifecycleCheckCallbacks r5 = new LifecycleCheckCallbacks(r14);     // Catch: Throwable -> L33
        r4.registerActivityLifecycleCallbacks(r5);     // Catch: Throwable -> L33
        mainHandler.post(new RunnableC00481(r5, r0));     // Catch: Throwable -> L33
    L30:
        th = move-exception;
        mainHandler.post(new RunnableC00492(r4, r5));     // Catch: Throwable -> L33
        throw th;     // Catch: Throwable -> L33
    L24:
        if (needsRelaunchCall() == false) goto L26;
        requestRelaunchActivityMethod.invoke(r3, new Object[]{r0, null, null, 0, false, null, null, false, false});     // Catch: Throwable -> L30
    L28:
        mainHandler.post(new RunnableC00492(r4, r5));     // Catch: Throwable -> L33
        return true;
    L26:
        r14.recreate();     // Catch: Throwable -> L30
    L34:
        return false;
    L14:
        if (performStopActivity3ParamsMethod != null) goto L36;
        return false;
    }
}
