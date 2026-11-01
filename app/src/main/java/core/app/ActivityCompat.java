package androidx.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.LocusId;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.content.LocusIdCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ActivityCompat extends ContextCompat {
    private static PermissionCompatDelegate sDelegate;

    /* JADX INFO: renamed from: androidx.core.app.ActivityCompat$1 */
    class RunnableC00461 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ String[] val$permissionsArray;
        final /* synthetic */ int val$requestCode;

        RunnableC00461(String[] r1, Activity r2, int r3) {
            this.val$permissionsArray = r1;
            this.val$activity = r2;
            this.val$requestCode = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            int[] r0 = new int[this.val$permissionsArray.length];
            PackageManager r1 = this.val$activity.getPackageManager();
            String r2 = this.val$activity.getPackageName();
            int r3 = this.val$permissionsArray.length;
            int r4 = 0;
        L3:
            if (r4 >= r3) goto L5;
            r0[r4] = r1.checkPermission(this.val$permissionsArray[r4], r2);
            r4 = r4 + 1;
            goto L3
        L5:
            ((OnRequestPermissionsResultCallback) this.val$activity).onRequestPermissionsResult(this.val$requestCode, this.val$permissionsArray, r0);
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static void finishAffinity(Activity r0) {
            r0.finishAffinity();
        }

        static void startActivityForResult(Activity r0, Intent r1, int r2, Bundle r3) {
            r0.startActivityForResult(r1, r2, r3);
        }

        static void startIntentSenderForResult(Activity r0, IntentSender r1, int r2, Intent r3, int r4, int r5, int r6, Bundle r7) throws IntentSender.SendIntentException {
            r0.startIntentSenderForResult(r1, r2, r3, r4, r5, r6, r7);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static void finishAfterTransition(Activity r0) {
            r0.finishAfterTransition();
        }

        static void postponeEnterTransition(Activity r0) {
            r0.postponeEnterTransition();
        }

        static void setEnterSharedElementCallback(Activity r0, android.app.SharedElementCallback r1) {
            r0.setEnterSharedElementCallback(r1);
        }

        static void setExitSharedElementCallback(Activity r0, android.app.SharedElementCallback r1) {
            r0.setExitSharedElementCallback(r1);
        }

        static void startPostponedEnterTransition(Activity r0) {
            r0.startPostponedEnterTransition();
        }
    }

    static class Api22Impl {
        private Api22Impl() {
        }

        static Uri getReferrer(Activity r1) {
            return r1.getReferrer();
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static void onSharedElementsReady(Object r1) {
            ((android.app.SharedElementCallback.OnSharedElementsReadyListener) r1).onSharedElementsReady();
        }

        static void requestPermissions(Activity r0, String[] r1, int r2) {
            r0.requestPermissions(r1, r2);
        }

        static boolean shouldShowRequestPermissionRationale(Activity r1, String r2) {
            return r1.shouldShowRequestPermissionRationale(r2);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static <T> T requireViewById(Activity r1, int r2) {
            return (T) r1.requireViewById(r2);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static Display getDisplay(ContextWrapper r1) {
            return r1.getDisplay();
        }

        static void setLocusContext(Activity r1, LocusIdCompat r2, Bundle r3) {
            if (r2 != null) goto L4;
            LocusId r0 = null;
        L5:
            r1.setLocusContext(r0, r3);
            return;
        L4:
            r0 = r2.toLocusId();
            goto L5
        }
    }

    static class Api31Impl {
        private Api31Impl() {
        }

        static boolean isLaunchedFromBubble(Activity r1) {
            return r1.isLaunchedFromBubble();
        }
    }

    public interface OnRequestPermissionsResultCallback {
        void onRequestPermissionsResult(int r1, String[] r2, int[] r3);
    }

    public interface PermissionCompatDelegate {
        boolean onActivityResult(Activity r1, int r2, int r3, Intent r4);

        boolean requestPermissions(Activity r1, String[] r2, int r3);
    }

    public interface RequestPermissionsRequestCodeValidator {
        void validateRequestPermissionsRequestCode(int r1);
    }

    static class SharedElementCallback21Impl extends android.app.SharedElementCallback {
        private final SharedElementCallback mCallback;

        SharedElementCallback21Impl(SharedElementCallback r1) {
            this.mCallback = r1;
        }

        static /* synthetic */ void lambda$onSharedElementsArrived$0(android.app.SharedElementCallback.OnSharedElementsReadyListener r0) {
            Api23Impl.onSharedElementsReady(r0);
        }

        @Override // android.app.SharedElementCallback
        public Parcelable onCaptureSharedElementSnapshot(View r2, Matrix r3, RectF r4) {
            return this.mCallback.onCaptureSharedElementSnapshot(r2, r3, r4);
        }

        @Override // android.app.SharedElementCallback
        public View onCreateSnapshotView(Context r2, Parcelable r3) {
            return this.mCallback.onCreateSnapshotView(r2, r3);
        }

        @Override // android.app.SharedElementCallback
        public void onMapSharedElements(List<String> r2, Map<String, View> r3) {
            this.mCallback.onMapSharedElements(r2, r3);
        }

        @Override // android.app.SharedElementCallback
        public void onRejectSharedElements(List<View> r2) {
            this.mCallback.onRejectSharedElements(r2);
        }

        @Override // android.app.SharedElementCallback
        public void onSharedElementEnd(List<String> r2, List<View> r3, List<View> r4) {
            this.mCallback.onSharedElementEnd(r2, r3, r4);
        }

        @Override // android.app.SharedElementCallback
        public void onSharedElementStart(List<String> r2, List<View> r3, List<View> r4) {
            this.mCallback.onSharedElementStart(r2, r3, r4);
        }

        @Override // android.app.SharedElementCallback
        public void onSharedElementsArrived(List<String> r3, List<View> r4, final android.app.SharedElementCallback.OnSharedElementsReadyListener r5) {
            this.mCallback.onSharedElementsArrived(r3, r4, new C0047xa7ca8857(r5));
        }
    }

    protected ActivityCompat() {
    }

    public static void finishAffinity(Activity r0) {
        Api16Impl.finishAffinity(r0);
    }

    public static void finishAfterTransition(Activity r0) {
        Api21Impl.finishAfterTransition(r0);
    }

    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return sDelegate;
    }

    public static Uri getReferrer(Activity r1) {
        return Api22Impl.getReferrer(r1);
    }

    @Deprecated
    public static boolean invalidateOptionsMenu(Activity r1) {
        r1.invalidateOptionsMenu();
        return true;
    }

    public static boolean isLaunchedFromBubble(Activity r4) {
        if (Build.VERSION.SDK_INT < 31) goto L7;
        return Api31Impl.isLaunchedFromBubble(r4);
    L7:
        if (Build.VERSION.SDK_INT != 30) goto L16;
        if (Api30Impl.getDisplay(r4) == null) goto L14;
        if (Api30Impl.getDisplay(r4).getDisplayId() == 0) goto L14;
        return true;
    L14:
        return false;
    L16:
        if (Build.VERSION.SDK_INT == 29) goto L18;
        return false;
    L18:
        if (r4.getWindowManager().getDefaultDisplay() == null) goto L23;
        if (r4.getWindowManager().getDefaultDisplay().getDisplayId() == 0) goto L23;
        return true;
    L23:
        return false;
    }

    static /* synthetic */ void lambda$recreate$0(Activity r1) {
        if (r1.isFinishing() == false) goto L5;
        return;
    L5:
        if (ActivityRecreator.recreate(r1) == true) goto L9;
        r1.recreate();
        return;
    }

    public static void postponeEnterTransition(Activity r0) {
        Api21Impl.postponeEnterTransition(r0);
    }

    public static void recreate(final Activity r2) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        r2.recreate();
        return;
    L5:
        new Handler(r2.getMainLooper()).post(new ActivityCompat$$ExternalSyntheticLambda0(r2));
    }

    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(Activity r1, DragEvent r2) {
        return DragAndDropPermissionsCompat.request(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void requestPermissions(Activity r7, String[] r8, int r9) {
        if (sDelegate != null) goto L5;
    L7:
        HashSet r0 = new HashSet();
        int r1 = 0;
    L9:
        if (r1 >= r8.length) goto L20;
        if (TextUtils.isEmpty(r8[r1]) == true) goto L19;
        if (BuildCompat.isAtLeastT() == true) goto L17;
        if (TextUtils.equals(r8[r1], "android.permission.POST_NOTIFICATIONS") == false) goto L17;
        r0.add(Integer.valueOf(r1));
    L17:
        r1 = r1 + 1;
        goto L9
    L19:
        throw new IllegalArgumentException("Permission request for permissions " + Arrays.toString(r8) + " must not contain null or empty values");
    L20:
        int r2 = r0.size();
        if (r2 <= 0) goto L23;
        String[] r3 = new String[r8.length - r2];
    L24:
        if (r2 <= 0) goto L36;
        if (r2 != r8.length) goto L28;
        return;
    L28:
        int r4 = 0;
        int r5 = 0;
    L30:
        if (r4 >= r8.length) goto L36;
        if (r0.contains(Integer.valueOf(r4)) == true) goto L34;
        r3[r5] = r8[r4];
        r5 = r5 + 1;
    L34:
        r4 = r4 + 1;
    L36:
        if ((r7 instanceof RequestPermissionsRequestCodeValidator) == false) goto L38;
        ((RequestPermissionsRequestCodeValidator) r7).validateRequestPermissionsRequestCode(r9);
    L38:
        Api23Impl.requestPermissions(r7, r8, r9);
        return;
    L23:
        r3 = r8;
        goto L24
    L5:
        if (sDelegate.requestPermissions(r7, r8, r9) == false) goto L7;
    }

    public static <T extends View> T requireViewById(Activity r3, int r4) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        T r0 = (T) r3.findViewById(r4);
        if (r0 == null) goto L10;
        return r0;
    L10:
        throw new IllegalArgumentException("ID does not reference a View inside this Activity");
    L5:
        return (T) Api28Impl.requireViewById(r3, r4);
    }

    public static void setEnterSharedElementCallback(Activity r1, SharedElementCallback r2) {
        if (r2 == null) goto L4;
        SharedElementCallback21Impl r0 = new SharedElementCallback21Impl(r2);
    L5:
        Api21Impl.setEnterSharedElementCallback(r1, r0);
        return;
    L4:
        r0 = null;
        goto L5
    }

    public static void setExitSharedElementCallback(Activity r1, SharedElementCallback r2) {
        if (r2 == null) goto L4;
        SharedElementCallback21Impl r0 = new SharedElementCallback21Impl(r2);
    L5:
        Api21Impl.setExitSharedElementCallback(r1, r0);
        return;
    L4:
        r0 = null;
        goto L5
    }

    public static void setLocusContext(Activity r2, LocusIdCompat r3, Bundle r4) {
        if (Build.VERSION.SDK_INT < 30) goto L6;
        Api30Impl.setLocusContext(r2, r3, r4);
        return;
    }

    public static void setPermissionCompatDelegate(PermissionCompatDelegate r0) {
        sDelegate = r0;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity r1, String r2) {
        if (BuildCompat.isAtLeastT() == true) goto L9;
        if (TextUtils.equals("android.permission.POST_NOTIFICATIONS", r2) == false) goto L9;
        return false;
    L9:
        return Api23Impl.shouldShowRequestPermissionRationale(r1, r2);
    }

    public static void startActivityForResult(Activity r0, Intent r1, int r2, Bundle r3) {
        Api16Impl.startActivityForResult(r0, r1, r2, r3);
    }

    public static void startIntentSenderForResult(Activity r0, IntentSender r1, int r2, Intent r3, int r4, int r5, int r6, Bundle r7) throws IntentSender.SendIntentException {
        Api16Impl.startIntentSenderForResult(r0, r1, r2, r3, r4, r5, r6, r7);
    }

    public static void startPostponedEnterTransition(Activity r0) {
        Api21Impl.startPostponedEnterTransition(r0);
    }
}
