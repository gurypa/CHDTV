package androidx.media;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public final class MediaSessionManager {
    static final boolean DEBUG = false;
    static final String TAG = "MediaSessionManager";
    private static final Object sLock = null;
    private static volatile MediaSessionManager sSessionManager;
    MediaSessionManagerImpl mImpl;

    interface MediaSessionManagerImpl {
        Context getContext();

        boolean isTrustedForMediaControl(RemoteUserInfoImpl r1);
    }

    public static final class RemoteUserInfo {
        public static final String LEGACY_CONTROLLER = "android.media.session.MediaController";
        public static final int UNKNOWN_PID = -1;
        public static final int UNKNOWN_UID = -1;
        RemoteUserInfoImpl mImpl;

        public RemoteUserInfo(android.media.session.MediaSessionManager.RemoteUserInfo r4) {
            String r0 = MediaSessionManagerImplApi28.RemoteUserInfoImplApi28.getPackageName(r4);
            if (r0 == null) goto L11;
            if (TextUtils.isEmpty(r0) == true) goto L9;
            this.mImpl = new MediaSessionManagerImplApi28.RemoteUserInfoImplApi28(r4);
            return;
        L9:
            throw new IllegalArgumentException("packageName should be nonempty");
        L11:
            throw new NullPointerException("package shouldn't be null");
        }

        public RemoteUserInfo(String r3, int r4, int r5) {
            if (r3 == null) goto L14;
            if (TextUtils.isEmpty(r3) == true) goto L12;
            if (Build.VERSION.SDK_INT < 28) goto L9;
            this.mImpl = new MediaSessionManagerImplApi28.RemoteUserInfoImplApi28(r3, r4, r5);
            return;
        L9:
            this.mImpl = new MediaSessionManagerImplBase.RemoteUserInfoImplBase(r3, r4, r5);
            return;
        L12:
            throw new IllegalArgumentException("packageName should be nonempty");
        L14:
            throw new NullPointerException("package shouldn't be null");
        }

        public boolean equals(Object r3) {
            if (this != r3) goto L6;
            return true;
        L6:
            if ((r3 instanceof RemoteUserInfo) == true) goto L10;
            return false;
        L10:
            return this.mImpl.equals(((RemoteUserInfo) r3).mImpl);
        }

        public String getPackageName() {
            return this.mImpl.getPackageName();
        }

        public int getPid() {
            return this.mImpl.getPid();
        }

        public int getUid() {
            return this.mImpl.getUid();
        }

        public int hashCode() {
            return this.mImpl.hashCode();
        }
    }

    interface RemoteUserInfoImpl {
        String getPackageName();

        int getPid();

        int getUid();
    }

    static {
        DEBUG = Log.isLoggable(TAG, 3);
        sLock = new Object();
    }

    private MediaSessionManager(Context r3) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        this.mImpl = new MediaSessionManagerImplApi28(r3);
        return;
    L5:
        this.mImpl = new MediaSessionManagerImplApi21(r3);
    }

    public static MediaSessionManager getSessionManager(Context r3) {
        if (r3 == null) goto L15;
        Object r0 = sLock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L6:
        if (sSessionManager != null) goto L8;
        sSessionManager = new MediaSessionManager(r3.getApplicationContext());     // Catch: Throwable -> L11
    L8:
        MediaSessionManager r1 = sSessionManager;     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
        return r1;
    L15:
        throw new IllegalArgumentException("context cannot be null");
    }

    Context getContext() {
        return this.mImpl.getContext();
    }

    public boolean isTrustedForMediaControl(RemoteUserInfo r3) {
        if (r3 == null) goto L6;
        return this.mImpl.isTrustedForMediaControl(r3.mImpl);
    L6:
        throw new IllegalArgumentException("userInfo should not be null");
    }
}
