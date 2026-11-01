package androidx.core.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public final class ConnectivityManagerCompat {
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

    static class Api16Impl {
        private Api16Impl() {
        }

        static boolean isActiveNetworkMetered(ConnectivityManager r1) {
            return r1.isActiveNetworkMetered();
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static int getRestrictBackgroundStatus(ConnectivityManager r1) {
            return r1.getRestrictBackgroundStatus();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RestrictBackgroundStatus {
    }

    private ConnectivityManagerCompat() {
    }

    public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager r2, Intent r3) {
        NetworkInfo r0 = (NetworkInfo) r3.getParcelableExtra("networkInfo");
        if (r0 != null) goto L5;
        return null;
    L5:
        return r2.getNetworkInfo(r0.getType());
    }

    public static int getRestrictBackgroundStatus(ConnectivityManager r2) {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return 3;
    L5:
        return Api24Impl.getRestrictBackgroundStatus(r2);
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager r1) {
        return Api16Impl.isActiveNetworkMetered(r1);
    }
}
