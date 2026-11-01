package androidx.core.location;

import android.location.GnssStatus;
import android.os.Build;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
class GnssStatusWrapper extends GnssStatusCompat {
    private final GnssStatus mWrapped;

    static class Api26Impl {
        private Api26Impl() {
        }

        static float getCarrierFrequencyHz(GnssStatus r1, int r2) {
            return r1.getCarrierFrequencyHz(r2);
        }

        static boolean hasCarrierFrequencyHz(GnssStatus r1, int r2) {
            return r1.hasCarrierFrequencyHz(r2);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static float getBasebandCn0DbHz(GnssStatus r1, int r2) {
            return r1.getBasebandCn0DbHz(r2);
        }

        static boolean hasBasebandCn0DbHz(GnssStatus r1, int r2) {
            return r1.hasBasebandCn0DbHz(r2);
        }
    }

    GnssStatusWrapper(Object r2) {
        this.mWrapped = (GnssStatus) Preconditions.checkNotNull((GnssStatus) r2);
    }

    public boolean equals(Object r4) {
        if (this != r4) goto L6;
        return true;
    L6:
        if ((r4 instanceof GnssStatusWrapper) == true) goto L10;
        return false;
    L10:
        return this.mWrapped.equals(((GnssStatusWrapper) r4).mWrapped);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public float getAzimuthDegrees(int r2) {
        return this.mWrapped.getAzimuthDegrees(r2);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public float getBasebandCn0DbHz(int r3) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return Api30Impl.getBasebandCn0DbHz(this.mWrapped, r3);
    L7:
        throw new UnsupportedOperationException();
    }

    @Override // androidx.core.location.GnssStatusCompat
    public float getCarrierFrequencyHz(int r3) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.getCarrierFrequencyHz(this.mWrapped, r3);
    L7:
        throw new UnsupportedOperationException();
    }

    @Override // androidx.core.location.GnssStatusCompat
    public float getCn0DbHz(int r2) {
        return this.mWrapped.getCn0DbHz(r2);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public int getConstellationType(int r2) {
        return this.mWrapped.getConstellationType(r2);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public float getElevationDegrees(int r2) {
        return this.mWrapped.getElevationDegrees(r2);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public int getSatelliteCount() {
        return this.mWrapped.getSatelliteCount();
    }

    @Override // androidx.core.location.GnssStatusCompat
    public int getSvid(int r2) {
        return this.mWrapped.getSvid(r2);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public boolean hasAlmanacData(int r2) {
        return this.mWrapped.hasAlmanacData(r2);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public boolean hasBasebandCn0DbHz(int r3) {
        if (Build.VERSION.SDK_INT >= 30) goto L5;
        return false;
    L5:
        return Api30Impl.hasBasebandCn0DbHz(this.mWrapped, r3);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public boolean hasCarrierFrequencyHz(int r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return false;
    L5:
        return Api26Impl.hasCarrierFrequencyHz(this.mWrapped, r3);
    }

    @Override // androidx.core.location.GnssStatusCompat
    public boolean hasEphemerisData(int r2) {
        return this.mWrapped.hasEphemerisData(r2);
    }

    public int hashCode() {
        return this.mWrapped.hashCode();
    }

    @Override // androidx.core.location.GnssStatusCompat
    public boolean usedInFix(int r2) {
        return this.mWrapped.usedInFix(r2);
    }
}
