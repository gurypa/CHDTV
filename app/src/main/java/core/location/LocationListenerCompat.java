package androidx.core.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface LocationListenerCompat extends LocationListener {

    /* JADX INFO: renamed from: androidx.core.location.LocationListenerCompat$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onFlushComplete(LocationListenerCompat r0, int r1) {
        }

        public static void $default$onLocationChanged(LocationListenerCompat r3, List r4) {
            int r0 = r4.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            r3.onLocationChanged((Location) r4.get(r1));
            r1 = r1 + 1;
            goto L3
        }

        public static void $default$onProviderDisabled(LocationListenerCompat r0, String r1) {
        }

        public static void $default$onProviderEnabled(LocationListenerCompat r0, String r1) {
        }

        public static void $default$onStatusChanged(LocationListenerCompat r0, String r1, int r2, Bundle r3) {
        }
    }

    @Override // android.location.LocationListener
    void onFlushComplete(int r1);

    @Override // android.location.LocationListener
    void onLocationChanged(List<Location> r1);

    @Override // android.location.LocationListener
    void onProviderDisabled(String r1);

    @Override // android.location.LocationListener
    void onProviderEnabled(String r1);

    @Override // android.location.LocationListener
    void onStatusChanged(String r1, int r2, Bundle r3);
}
