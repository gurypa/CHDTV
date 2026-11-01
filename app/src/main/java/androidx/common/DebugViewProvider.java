package androidx.media3.common;

import android.view.SurfaceView;

/* JADX INFO: loaded from: classes.dex */
public interface DebugViewProvider {
    public static final DebugViewProvider NONE = null;

    /* JADX INFO: renamed from: androidx.media3.common.DebugViewProvider$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        static {
            DebugViewProvider r0 = DebugViewProvider.NONE;
        }

        public static /* synthetic */ SurfaceView lambda$static$0(int r1, int r2) {
            return null;
        }
    }

    static {
        NONE = new DebugViewProvider$$ExternalSyntheticLambda0();
    }

    SurfaceView getDebugPreviewSurfaceView(int r1, int r2);
}
