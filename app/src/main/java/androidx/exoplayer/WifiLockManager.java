package androidx.media3.exoplayer;

import android.content.Context;
import android.net.wifi.WifiManager;
import androidx.media3.common.util.Log;

/* JADX INFO: loaded from: classes.dex */
final class WifiLockManager {
    private static final String TAG = "WifiLockManager";
    private static final String WIFI_LOCK_TAG = "ExoPlayer:WifiLockManager";
    private final Context applicationContext;
    private boolean enabled;
    private boolean stayAwake;
    private WifiManager.WifiLock wifiLock;

    public WifiLockManager(Context r2) {
        this.applicationContext = r2.getApplicationContext();
    }

    private void updateWifiLock() {
        if (this.wifiLock != null) goto L6;
        return;
    L6:
        if (this.enabled == true) goto L8;
    L10:
        this.wifiLock.release();
        return;
    L8:
        if (this.stayAwake == false) goto L10;
        this.wifiLock.acquire();
    }

    public void setEnabled(boolean r4) {
        if (r4 == true) goto L4;
    L10:
        this.enabled = r4;
        updateWifiLock();
        return;
    L4:
        if (this.wifiLock != null) goto L10;
        WifiManager r0 = (WifiManager) this.applicationContext.getApplicationContext().getSystemService("wifi");
        if (r0 != null) goto L9;
        Log.m48w(TAG, "WifiManager is null, therefore not creating the WifiLock.");
        return;
    L9:
        this.wifiLock = r0.createWifiLock(3, WIFI_LOCK_TAG);
        this.wifiLock.setReferenceCounted(false);
        goto L10
    }

    public void setStayAwake(boolean r1) {
        this.stayAwake = r1;
        updateWifiLock();
    }
}
