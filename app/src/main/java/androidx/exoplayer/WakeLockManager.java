package androidx.media3.exoplayer;

import android.content.Context;
import android.os.PowerManager;
import androidx.media3.common.util.Log;

/* JADX INFO: loaded from: classes.dex */
final class WakeLockManager {
    private static final String TAG = "WakeLockManager";
    private static final String WAKE_LOCK_TAG = "ExoPlayer:WakeLockManager";
    private final Context applicationContext;
    private boolean enabled;
    private boolean stayAwake;
    private PowerManager.WakeLock wakeLock;

    public WakeLockManager(Context r2) {
        this.applicationContext = r2.getApplicationContext();
    }

    private void updateWakeLock() {
        if (this.wakeLock != null) goto L6;
        return;
    L6:
        if (this.enabled == true) goto L8;
    L10:
        this.wakeLock.release();
        return;
    L8:
        if (this.stayAwake == false) goto L10;
        this.wakeLock.acquire();
    }

    public void setEnabled(boolean r4) {
        if (r4 == true) goto L4;
    L10:
        this.enabled = r4;
        updateWakeLock();
        return;
    L4:
        if (this.wakeLock != null) goto L10;
        PowerManager r0 = (PowerManager) this.applicationContext.getSystemService("power");
        if (r0 != null) goto L9;
        Log.m48w(TAG, "PowerManager is null, therefore not creating the WakeLock.");
        return;
    L9:
        this.wakeLock = r0.newWakeLock(1, WAKE_LOCK_TAG);
        this.wakeLock.setReferenceCounted(false);
        goto L10
    }

    public void setStayAwake(boolean r1) {
        this.stayAwake = r1;
        updateWakeLock();
    }
}
