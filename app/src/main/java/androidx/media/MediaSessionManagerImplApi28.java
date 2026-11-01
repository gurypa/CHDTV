package androidx.media;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
class MediaSessionManagerImplApi28 extends MediaSessionManagerImplApi21 {
    android.media.session.MediaSessionManager mObject;

    static final class RemoteUserInfoImplApi28 extends MediaSessionManagerImplBase.RemoteUserInfoImplBase {
        final android.media.session.MediaSessionManager.RemoteUserInfo mObject;

        RemoteUserInfoImplApi28(android.media.session.MediaSessionManager.RemoteUserInfo r4) {
            super(r4.getPackageName(), r4.getPid(), r4.getUid());
            this.mObject = r4;
        }

        RemoteUserInfoImplApi28(String r2, int r3, int r4) {
            super(r2, r3, r4);
            this.mObject = new android.media.session.MediaSessionManager.RemoteUserInfo(r2, r3, r4);
        }

        static String getPackageName(android.media.session.MediaSessionManager.RemoteUserInfo r1) {
            return r1.getPackageName();
        }
    }

    MediaSessionManagerImplApi28(Context r2) {
        super(r2);
        this.mObject = (android.media.session.MediaSessionManager) r2.getSystemService("media_session");
    }

    @Override // androidx.media.MediaSessionManagerImplApi21, androidx.media.MediaSessionManagerImplBase, androidx.media.MediaSessionManager.MediaSessionManagerImpl
    public boolean isTrustedForMediaControl(MediaSessionManager.RemoteUserInfoImpl r2) {
        return super.isTrustedForMediaControl(r2);
    }
}
