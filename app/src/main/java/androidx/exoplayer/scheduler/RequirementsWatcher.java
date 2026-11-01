package androidx.media3.exoplayer.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class RequirementsWatcher {
    private final Context context;
    private final Handler handler;
    private final Listener listener;
    private NetworkCallback networkCallback;
    private int notMetRequirements;
    private DeviceStatusChangeReceiver receiver;
    private final Requirements requirements;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.scheduler.RequirementsWatcher$1 */
    static /* synthetic */ class C03231 {
    }

    private class DeviceStatusChangeReceiver extends BroadcastReceiver {
        final /* synthetic */ RequirementsWatcher this$0;

        private DeviceStatusChangeReceiver(RequirementsWatcher r1) {
            this.this$0 = r1;
        }

        /* synthetic */ DeviceStatusChangeReceiver(RequirementsWatcher r1, C03231 r2) {
            this(r1);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context r2, Intent r3) {
            if (isInitialStickyBroadcast() == true) goto L6;
            RequirementsWatcher.access$200(this.this$0);
            return;
        }
    }

    public interface Listener {
        void onRequirementsStateChanged(RequirementsWatcher r1, int r2);
    }

    private final class NetworkCallback extends ConnectivityManager.NetworkCallback {
        private boolean networkValidated;
        private boolean receivedCapabilitiesChange;
        final /* synthetic */ RequirementsWatcher this$0;

        private NetworkCallback(RequirementsWatcher r1) {
            this.this$0 = r1;
        }

        /* synthetic */ NetworkCallback(RequirementsWatcher r1, C03231 r2) {
            this(r1);
        }

        private void postCheckRequirements() {
            RequirementsWatcher.access$300(this.this$0).post(new RequirementsWatcher$NetworkCallback$$ExternalSyntheticLambda0(this));
        }

        private void postRecheckNotMetNetworkRequirements() {
            RequirementsWatcher.access$300(this.this$0).post(new RequirementsWatcher$NetworkCallback$$ExternalSyntheticLambda1(this));
        }

        /* JADX INFO: renamed from: lambda$postCheckRequirements$0$androidx-media3-exoplayer-scheduler-RequirementsWatcher$NetworkCallback */
        /* synthetic */ void m117xcc18be42() {
            if (RequirementsWatcher.access$400(this.this$0) == null) goto L6;
            RequirementsWatcher.access$200(this.this$0);
            return;
        }

        /* JADX INFO: renamed from: lambda$postRecheckNotMetNetworkRequirements$1$androidx-media3-exoplayer-scheduler-RequirementsWatcher$NetworkCallback */
        /* synthetic */ void m118xfb2bca45() {
            if (RequirementsWatcher.access$400(this.this$0) == null) goto L6;
            RequirementsWatcher.access$500(this.this$0);
            return;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network r1) {
            postCheckRequirements();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onBlockedStatusChanged(Network r1, boolean r2) {
            if (r2 == true) goto L5;
            postRecheckNotMetNetworkRequirements();
            return;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network r3, NetworkCapabilities r4) {
            boolean r0 = r4.hasCapability(16);
            if (this.receivedCapabilitiesChange == true) goto L5;
        L9:
            this.receivedCapabilitiesChange = true;
            this.networkValidated = r0;
            postCheckRequirements();
            return;
        L5:
            if (this.networkValidated != r0) goto L9;
            if (r0 == false) goto L11;
            postRecheckNotMetNetworkRequirements();
            return;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network r1) {
            postCheckRequirements();
        }
    }

    public RequirementsWatcher(Context r2, Listener r3, Requirements r4) {
        this.context = r2.getApplicationContext();
        this.listener = r3;
        this.requirements = r4;
        this.handler = Util.createHandlerForCurrentOrMainLooper();
    }

    static /* synthetic */ void access$200(RequirementsWatcher r0) {
        r0.checkRequirements();
    }

    static /* synthetic */ Handler access$300(RequirementsWatcher r1) {
        return r1.handler;
    }

    static /* synthetic */ NetworkCallback access$400(RequirementsWatcher r1) {
        return r1.networkCallback;
    }

    static /* synthetic */ void access$500(RequirementsWatcher r0) {
        r0.recheckNotMetNetworkRequirements();
    }

    private void checkRequirements() {
        int r0 = this.requirements.getNotMetRequirements(this.context);
        if (this.notMetRequirements == r0) goto L6;
        this.notMetRequirements = r0;
        this.listener.onRequirementsStateChanged(this, r0);
        return;
    }

    private void recheckNotMetNetworkRequirements() {
        if ((this.notMetRequirements & 3) != 0) goto L5;
        return;
    L5:
        checkRequirements();
    }

    private void registerNetworkCallbackV24() {
        ConnectivityManager r0 = (ConnectivityManager) Assertions.checkNotNull((ConnectivityManager) this.context.getSystemService("connectivity"));
        this.networkCallback = new NetworkCallback(this, null);
        r0.registerDefaultNetworkCallback(this.networkCallback);
    }

    private void unregisterNetworkCallbackV24() {
        ((ConnectivityManager) Assertions.checkNotNull((ConnectivityManager) this.context.getSystemService("connectivity"))).unregisterNetworkCallback((ConnectivityManager.NetworkCallback) Assertions.checkNotNull(this.networkCallback));
        this.networkCallback = null;
    }

    public Requirements getRequirements() {
        return this.requirements;
    }

    public int start() {
        this.notMetRequirements = this.requirements.getNotMetRequirements(this.context);
        IntentFilter r0 = new IntentFilter();
        if (this.requirements.isNetworkRequired() == false) goto L9;
        if (Util.SDK_INT < 24) goto L7;
        registerNetworkCallbackV24();
        goto L9
    L7:
        r0.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    L9:
        if (this.requirements.isChargingRequired() == false) goto L12;
        r0.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        r0.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
    L12:
        if (this.requirements.isIdleRequired() == false) goto L18;
        if (Util.SDK_INT < 23) goto L16;
        r0.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        goto L18
    L16:
        r0.addAction("android.intent.action.SCREEN_ON");
        r0.addAction("android.intent.action.SCREEN_OFF");
    L18:
        if (this.requirements.isStorageNotLowRequired() == false) goto L20;
        r0.addAction("android.intent.action.DEVICE_STORAGE_LOW");
        r0.addAction("android.intent.action.DEVICE_STORAGE_OK");
    L20:
        this.receiver = new DeviceStatusChangeReceiver(this, null);
        this.context.registerReceiver(this.receiver, r0, null, this.handler);
        return this.notMetRequirements;
    }

    public void stop() {
        this.context.unregisterReceiver((BroadcastReceiver) Assertions.checkNotNull(this.receiver));
        this.receiver = null;
        if (Util.SDK_INT >= 24) goto L5;
        return;
    L5:
        if (this.networkCallback == null) goto L9;
        unregisterNetworkCallbackV24();
        return;
    }
}
