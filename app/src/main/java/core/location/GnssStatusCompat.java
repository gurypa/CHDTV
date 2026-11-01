package androidx.core.location;

import android.location.GnssStatus;
import android.location.GpsStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public abstract class GnssStatusCompat {
    public static final int CONSTELLATION_BEIDOU = 5;
    public static final int CONSTELLATION_GALILEO = 6;
    public static final int CONSTELLATION_GLONASS = 3;
    public static final int CONSTELLATION_GPS = 1;
    public static final int CONSTELLATION_IRNSS = 7;
    public static final int CONSTELLATION_QZSS = 4;
    public static final int CONSTELLATION_SBAS = 2;
    public static final int CONSTELLATION_UNKNOWN = 0;

    public static abstract class Callback {
        public Callback() {
        }

        public void onFirstFix(int r1) {
        }

        public void onSatelliteStatusChanged(GnssStatusCompat r1) {
        }

        public void onStarted() {
        }

        public void onStopped() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConstellationType {
    }

    GnssStatusCompat() {
    }

    public static GnssStatusCompat wrap(GnssStatus r1) {
        return new GnssStatusWrapper(r1);
    }

    public static GnssStatusCompat wrap(GpsStatus r1) {
        return new GpsStatusWrapper(r1);
    }

    public abstract float getAzimuthDegrees(int r1);

    public abstract float getBasebandCn0DbHz(int r1);

    public abstract float getCarrierFrequencyHz(int r1);

    public abstract float getCn0DbHz(int r1);

    public abstract int getConstellationType(int r1);

    public abstract float getElevationDegrees(int r1);

    public abstract int getSatelliteCount();

    public abstract int getSvid(int r1);

    public abstract boolean hasAlmanacData(int r1);

    public abstract boolean hasBasebandCn0DbHz(int r1);

    public abstract boolean hasCarrierFrequencyHz(int r1);

    public abstract boolean hasEphemerisData(int r1);

    public abstract boolean usedInFix(int r1);
}
