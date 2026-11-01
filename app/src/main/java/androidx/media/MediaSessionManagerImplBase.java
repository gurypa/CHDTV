package androidx.media;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.util.ObjectsCompat;

/* JADX INFO: loaded from: classes.dex */
class MediaSessionManagerImplBase implements MediaSessionManager.MediaSessionManagerImpl {
    private static final boolean DEBUG = false;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String PERMISSION_MEDIA_CONTENT_CONTROL = "android.permission.MEDIA_CONTENT_CONTROL";
    private static final String PERMISSION_STATUS_BAR_SERVICE = "android.permission.STATUS_BAR_SERVICE";
    private static final String TAG = "MediaSessionManager";
    ContentResolver mContentResolver;
    Context mContext;

    static class RemoteUserInfoImplBase implements MediaSessionManager.RemoteUserInfoImpl {
        private String mPackageName;
        private int mPid;
        private int mUid;

        RemoteUserInfoImplBase(String r1, int r2, int r3) {
            this.mPackageName = r1;
            this.mPid = r2;
            this.mUid = r3;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof RemoteUserInfoImplBase) == true) goto L8;
            return false;
        L8:
            RemoteUserInfoImplBase r1 = (RemoteUserInfoImplBase) r6;
            if (this.mPid < 0) goto L23;
            if (r1.mPid < 0) goto L23;
            if (TextUtils.equals(this.mPackageName, r1.mPackageName) == false) goto L21;
            if (this.mPid != r1.mPid) goto L21;
            if (this.mUid != r1.mUid) goto L21;
            return true;
        L21:
            return false;
        L23:
            if (TextUtils.equals(this.mPackageName, r1.mPackageName) == false) goto L28;
            if (this.mUid != r1.mUid) goto L28;
            return true;
        L28:
            return false;
        }

        @Override // androidx.media.MediaSessionManager.RemoteUserInfoImpl
        public String getPackageName() {
            return this.mPackageName;
        }

        @Override // androidx.media.MediaSessionManager.RemoteUserInfoImpl
        public int getPid() {
            return this.mPid;
        }

        @Override // androidx.media.MediaSessionManager.RemoteUserInfoImpl
        public int getUid() {
            return this.mUid;
        }

        public int hashCode() {
            return ObjectsCompat.hash(new Object[]{this.mPackageName, Integer.valueOf(this.mUid)});
        }
    }

    static {
        DEBUG = MediaSessionManager.DEBUG;
    }

    MediaSessionManagerImplBase(Context r2) {
        this.mContext = r2;
        this.mContentResolver = this.mContext.getContentResolver();
    }

    private boolean isPermissionGranted(MediaSessionManager.RemoteUserInfoImpl r6, String r7) {
        if (r6.getPid() >= 0) goto L10;
        if (this.mContext.getPackageManager().checkPermission(r7, r6.getPackageName()) != 0) goto L8;
        return true;
    L8:
        return false;
    L10:
        if (this.mContext.checkPermission(r7, r6.getPid(), r6.getUid()) != 0) goto L13;
        return true;
    L13:
        return false;
    }

    @Override // androidx.media.MediaSessionManager.MediaSessionManagerImpl
    public Context getContext() {
        return this.mContext;
    }

    boolean isEnabledNotificationListener(MediaSessionManager.RemoteUserInfoImpl r7) {
        String r0 = Settings.Secure.getString(this.mContentResolver, ENABLED_NOTIFICATION_LISTENERS);
        if (r0 == null) goto L14;
        String[] r1 = r0.split(":");
        int r2 = 0;
    L6:
        if (r2 >= r1.length) goto L20;
        ComponentName r3 = ComponentName.unflattenFromString(r1[r2]);
        if (r3 == null) goto L13;
        if (r3.getPackageName().equals(r7.getPackageName()) == false) goto L13;
        return true;
    L13:
        r2 = r2 + 1;
        goto L6
    L20:
        return false;
    L14:
        return false;
    }

    @Override // androidx.media.MediaSessionManager.MediaSessionManagerImpl
    public boolean isTrustedForMediaControl(MediaSessionManager.RemoteUserInfoImpl r5) {
        if (this.mContext.getPackageManager().getApplicationInfo(r5.getPackageName(), 0) != null) goto L7;
        return false;
    L7:
        if (isPermissionGranted(r5, PERMISSION_STATUS_BAR_SERVICE) == false) goto L9;
    L14:
        return true;
    L9:
        if (isPermissionGranted(r5, PERMISSION_MEDIA_CONTENT_CONTROL) == true) goto L14;
        if (r5.getUid() == 1000) goto L14;
        if (isEnabledNotificationListener(r5) == true) goto L14;
        return false;
    L18:
        if (DEBUG == false) goto L20;
        Log.d(TAG, "Package " + r5.getPackageName() + " doesn't exist");
    L20:
        return false;
    }
}
