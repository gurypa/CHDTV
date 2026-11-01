package androidx.media3.exoplayer.drm;

import android.os.Looper;
import androidx.media3.common.Format;
import androidx.media3.exoplayer.analytics.PlayerId;

/* JADX INFO: loaded from: classes.dex */
public interface DrmSessionManager {
    public static final DrmSessionManager DRM_UNSUPPORTED = null;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.drm.DrmSessionManager$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static DrmSessionReference $default$preacquireSession(DrmSessionManager r1, DrmSessionEventListener.EventDispatcher r2, Format r3) {
            return DrmSessionReference.EMPTY;
        }

        public static void $default$prepare(DrmSessionManager r0) {
        }

        public static void $default$release(DrmSessionManager r0) {
        }
    }

    public interface DrmSessionReference {
        public static final DrmSessionReference EMPTY = null;

        /* JADX INFO: renamed from: androidx.media3.exoplayer.drm.DrmSessionManager$DrmSessionReference$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            static {
                DrmSessionReference r0 = DrmSessionReference.EMPTY;
            }

            public static /* synthetic */ void lambda$static$0() {
            }
        }

        static {
            EMPTY = new DrmSessionManager$DrmSessionReference$$ExternalSyntheticLambda0();
        }

        void release();
    }

    static {
        DRM_UNSUPPORTED = new C02881();
    }

    DrmSession acquireSession(DrmSessionEventListener.EventDispatcher r1, Format r2);

    int getCryptoType(Format r1);

    DrmSessionReference preacquireSession(DrmSessionEventListener.EventDispatcher r1, Format r2);

    void prepare();

    void release();

    void setPlayer(Looper r1, PlayerId r2);
}
