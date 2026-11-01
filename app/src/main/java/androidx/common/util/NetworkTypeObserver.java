package androidx.media3.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class NetworkTypeObserver {
    private static NetworkTypeObserver staticInstance;
    private final CopyOnWriteArrayList<WeakReference<Listener>> listeners;
    private final Handler mainHandler;
    private int networkType;
    private final Object networkTypeLock;

    /* JADX INFO: renamed from: androidx.media3.common.util.NetworkTypeObserver$1 */
    static /* synthetic */ class C02001 {
    }

    private static final class Api31 {

        private static final class DisplayInfoCallback extends TelephonyCallback implements TelephonyCallback.DisplayInfoListener {
            private final NetworkTypeObserver instance;

            public DisplayInfoCallback(NetworkTypeObserver r1) {
                this.instance = r1;
            }

            @Override // android.telephony.TelephonyCallback.DisplayInfoListener
            public void onDisplayInfoChanged(TelephonyDisplayInfo r5) {
                int r0 = r5.getOverrideNetworkType();
                int r2 = 5;
                if (r0 != 3) goto L5;
            L9:
                boolean r1 = true;
            L10:
                NetworkTypeObserver r3 = this.instance;
                if (r1 == false) goto L13;
                r2 = 10;
            L13:
                NetworkTypeObserver.access$200(r3, r2);
                return;
            L5:
                if (r0 == 4) goto L9;
                if (r0 == 5) goto L9;
                r1 = false;
                goto L10
            }
        }

        private Api31() {
        }

        public static void disambiguate4gAnd5gNsa(Context r3, NetworkTypeObserver r4) {
            TelephonyManager r0 = (TelephonyManager) Assertions.checkNotNull((TelephonyManager) r3.getSystemService("phone"));     // Catch: RuntimeException -> L4
            DisplayInfoCallback r1 = new DisplayInfoCallback(r4);     // Catch: RuntimeException -> L4
            r0.registerTelephonyCallback(r3.getMainExecutor(), r1);     // Catch: RuntimeException -> L4
            r0.unregisterTelephonyCallback(r1);     // Catch: RuntimeException -> L4
            return;
        L5:
            NetworkTypeObserver.access$200(r4, 5);
        }
    }

    public interface Listener {
        void onNetworkTypeChanged(int r1);
    }

    private final class Receiver extends BroadcastReceiver {
        final /* synthetic */ NetworkTypeObserver this$0;

        private Receiver(NetworkTypeObserver r1) {
            this.this$0 = r1;
        }

        /* synthetic */ Receiver(NetworkTypeObserver r1, C02001 r2) {
            this(r1);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context r4, Intent r5) {
            int r0 = NetworkTypeObserver.access$100(r4);
            if (Util.SDK_INT >= 31) goto L5;
        L7:
            NetworkTypeObserver.access$200(this.this$0, r0);
            return;
        L5:
            if (r0 != 5) goto L7;
            Api31.disambiguate4gAnd5gNsa(r4, this.this$0);
        }
    }

    private NetworkTypeObserver(Context r4) {
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.listeners = new CopyOnWriteArrayList();
        this.networkTypeLock = new Object();
        this.networkType = 0;
        IntentFilter r0 = new IntentFilter();
        r0.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        r4.registerReceiver(new Receiver(this, null), r0);
    }

    static /* synthetic */ int access$100(Context r1) {
        return getNetworkTypeFromConnectivityManager(r1);
    }

    static /* synthetic */ void access$200(NetworkTypeObserver r0, int r1) {
        r0.updateNetworkType(r1);
    }

    public static synchronized NetworkTypeObserver getInstance(Context r2) {
        monitor-enter(NetworkTypeObserver.class);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (staticInstance != null) goto L7;
        staticInstance = new NetworkTypeObserver(r2);     // Catch: Throwable -> L10
    L7:
        NetworkTypeObserver r1 = staticInstance;     // Catch: Throwable -> L10
        monitor-exit(NetworkTypeObserver.class);
        return r1;
    }

    private static int getMobileNetworkType(NetworkInfo r2) {
        switch(r2.getSubtype()) {
            case 1: goto L17;
            case 2: goto L17;
            case 3: goto L15;
            case 4: goto L15;
            case 5: goto L15;
            case 6: goto L15;
            case 7: goto L15;
            case 8: goto L15;
            case 9: goto L15;
            case 10: goto L15;
            case 11: goto L15;
            case 12: goto L15;
            case 13: goto L13;
            case 14: goto L15;
            case 15: goto L15;
            case 16: goto L4;
            case 17: goto L15;
            case 18: goto L11;
            case 19: goto L4;
            case 20: goto L7;
            default: goto L4;
        };
    L4:
        return 6;
    L11:
        return 2;
    L13:
        return 5;
    L15:
        return 4;
    L17:
        return 3;
    L7:
        if (Util.SDK_INT < 29) goto L9;
        return 9;
    L9:
        return 0;
    }

    private static int getNetworkTypeFromConnectivityManager(Context r3) {
        ConnectivityManager r0 = (ConnectivityManager) r3.getSystemService("connectivity");
        if (r0 != null) goto L26;
        return 0;
    L26:
        NetworkInfo r1 = r0.getActiveNetworkInfo();     // Catch: SecurityException -> L24
        if (r1 != null) goto L8;
        return 1;
    L8:
        if (r1.isConnected() == true) goto L11;
        return 1;
    L11:
        switch(r1.getType()) {
            case 0: goto L21;
            case 1: goto L18;
            case 2: goto L12;
            case 3: goto L12;
            case 4: goto L21;
            case 5: goto L21;
            case 6: goto L16;
            case 7: goto L12;
            case 8: goto L12;
            case 9: goto L14;
            default: goto L12;
        };
    L12:
        return 8;
    L14:
        return 7;
    L16:
        return 5;
    L18:
        return 2;
    L21:
        return getMobileNetworkType(r1);
    L25:
        return 0;
    }

    private void removeClearedReferences() {
        Iterator<WeakReference<Listener>> r0 = this.listeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        WeakReference<Listener> r1 = r0.next();
        if (r1.get() != null) goto L4;
        this.listeners.remove(r1);
        goto L4
    }

    public static synchronized void resetForTests() {
        monitor-enter(NetworkTypeObserver.class);
        staticInstance = null;     // Catch: Throwable -> L8
        monitor-exit(NetworkTypeObserver.class);
        return;
    L8:
        th = move-exception;
        throw th;
    }

    private void updateNetworkType(int r5) {
        Object r0 = this.networkTypeLock;
        monitor-enter(r0);
    L19:
        th = move-exception;
        throw th;
    L5:
        if (this.networkType != r5) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L19
        return;
    L8:
        this.networkType = r5;     // Catch: Throwable -> L19
        monitor-exit(r0);     // Catch: Throwable -> L19
        Iterator<WeakReference<Listener>> r1 = this.listeners.iterator();
    L12:
        if (r1.hasNext() == false) goto L18;
        WeakReference<Listener> r2 = r1.next();
        Listener r3 = r2.get();
        if (r3 != null) goto L15;
        this.listeners.remove(r2);
        goto L12
    L15:
        r3.onNetworkTypeChanged(r5);
        goto L12
    }

    public int getNetworkType() {
        Object r0 = this.networkTypeLock;
        monitor-enter(r0);
        int r1 = this.networkType;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: lambda$register$0$androidx-media3-common-util-NetworkTypeObserver */
    /* synthetic */ void m54xc4ab8e3(Listener r2) {
        r2.onNetworkTypeChanged(getNetworkType());
    }

    public void register(final Listener r3) {
        removeClearedReferences();
        this.listeners.add(new WeakReference(r3));
        this.mainHandler.post(new NetworkTypeObserver$$ExternalSyntheticLambda0(this, r3));
    }
}
