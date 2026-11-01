package androidx.core.location;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.core.util.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class LocationCompat {
    public static final String EXTRA_BEARING_ACCURACY = "bearingAccuracy";
    public static final String EXTRA_IS_MOCK = "mockLocation";
    public static final String EXTRA_MSL_ALTITUDE = "androidx.core.location.extra.MSL_ALTITUDE";
    public static final String EXTRA_MSL_ALTITUDE_ACCURACY = "androidx.core.location.extra.MSL_ALTITUDE_ACCURACY";
    public static final String EXTRA_SPEED_ACCURACY = "speedAccuracy";
    public static final String EXTRA_VERTICAL_ACCURACY = "verticalAccuracy";
    private static Method sSetIsFromMockProviderMethod;

    private static class Api17Impl {
        private Api17Impl() {
        }

        static long getElapsedRealtimeNanos(Location r2) {
            return r2.getElapsedRealtimeNanos();
        }
    }

    private static class Api18Impl {
        private Api18Impl() {
        }

        static boolean isMock(Location r1) {
            return r1.isFromMockProvider();
        }
    }

    private static class Api26Impl {
        private Api26Impl() {
        }

        static float getBearingAccuracyDegrees(Location r1) {
            return r1.getBearingAccuracyDegrees();
        }

        static float getSpeedAccuracyMetersPerSecond(Location r1) {
            return r1.getSpeedAccuracyMetersPerSecond();
        }

        static float getVerticalAccuracyMeters(Location r1) {
            return r1.getVerticalAccuracyMeters();
        }

        static boolean hasBearingAccuracy(Location r1) {
            return r1.hasBearingAccuracy();
        }

        static boolean hasSpeedAccuracy(Location r1) {
            return r1.hasSpeedAccuracy();
        }

        static boolean hasVerticalAccuracy(Location r1) {
            return r1.hasVerticalAccuracy();
        }

        static void setBearingAccuracyDegrees(Location r0, float r1) {
            r0.setBearingAccuracyDegrees(r1);
        }

        static void setSpeedAccuracyMetersPerSecond(Location r0, float r1) {
            r0.setSpeedAccuracyMetersPerSecond(r1);
        }

        static void setVerticalAccuracyMeters(Location r0, float r1) {
            r0.setVerticalAccuracyMeters(r1);
        }
    }

    private LocationCompat() {
    }

    private static boolean containsExtra(Location r2, String r3) {
        Bundle r0 = r2.getExtras();
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.containsKey(r3) == false) goto L7;
        return true;
    }

    public static float getBearingAccuracyDegrees(Location r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        Bundle r0 = r3.getExtras();
        if (r0 != null) goto L10;
        return 0.0f;
    L10:
        return r0.getFloat(EXTRA_BEARING_ACCURACY, 0.0f);
    L5:
        return Api26Impl.getBearingAccuracyDegrees(r3);
    }

    public static long getElapsedRealtimeMillis(Location r3) {
        return TimeUnit.NANOSECONDS.toMillis(Api17Impl.getElapsedRealtimeNanos(r3));
    }

    public static long getElapsedRealtimeNanos(Location r2) {
        return Api17Impl.getElapsedRealtimeNanos(r2);
    }

    public static float getMslAltitudeAccuracyMeters(Location r2) {
        Preconditions.checkState(hasMslAltitudeAccuracy(r2), "The Mean Sea Level altitude accuracy of the location is not set.");
        return getOrCreateExtras(r2).getFloat(EXTRA_MSL_ALTITUDE_ACCURACY);
    }

    public static double getMslAltitudeMeters(Location r2) {
        Preconditions.checkState(hasMslAltitude(r2), "The Mean Sea Level altitude of the location is not set.");
        return getOrCreateExtras(r2).getDouble(EXTRA_MSL_ALTITUDE);
    }

    private static Bundle getOrCreateExtras(Location r2) {
        Bundle r0 = r2.getExtras();
        if (r0 != null) goto L6;
        r2.setExtras(new Bundle());
        return r2.getExtras();
    L6:
        return r0;
    }

    private static Method getSetIsFromMockProviderMethod() throws NoSuchMethodException {
        if (sSetIsFromMockProviderMethod != null) goto L6;
        sSetIsFromMockProviderMethod = Location.class.getDeclaredMethod("setIsFromMockProvider", new Class[]{Boolean.TYPE});
        sSetIsFromMockProviderMethod.setAccessible(true);
    L6:
        return sSetIsFromMockProviderMethod;
    }

    public static float getSpeedAccuracyMetersPerSecond(Location r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        Bundle r0 = r3.getExtras();
        if (r0 != null) goto L10;
        return 0.0f;
    L10:
        return r0.getFloat(EXTRA_SPEED_ACCURACY, 0.0f);
    L5:
        return Api26Impl.getSpeedAccuracyMetersPerSecond(r3);
    }

    public static float getVerticalAccuracyMeters(Location r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        Bundle r0 = r3.getExtras();
        if (r0 != null) goto L10;
        return 0.0f;
    L10:
        return r0.getFloat(EXTRA_VERTICAL_ACCURACY, 0.0f);
    L5:
        return Api26Impl.getVerticalAccuracyMeters(r3);
    }

    public static boolean hasBearingAccuracy(Location r2) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.hasBearingAccuracy(r2);
    L7:
        return containsExtra(r2, EXTRA_BEARING_ACCURACY);
    }

    public static boolean hasMslAltitude(Location r1) {
        return containsExtra(r1, EXTRA_MSL_ALTITUDE);
    }

    public static boolean hasMslAltitudeAccuracy(Location r1) {
        return containsExtra(r1, EXTRA_MSL_ALTITUDE_ACCURACY);
    }

    public static boolean hasSpeedAccuracy(Location r2) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.hasSpeedAccuracy(r2);
    L7:
        return containsExtra(r2, EXTRA_SPEED_ACCURACY);
    }

    public static boolean hasVerticalAccuracy(Location r2) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.hasVerticalAccuracy(r2);
    L7:
        return containsExtra(r2, EXTRA_VERTICAL_ACCURACY);
    }

    public static boolean isMock(Location r1) {
        return Api18Impl.isMock(r1);
    }

    private static void removeExtra(Location r2, String r3) {
        Bundle r0 = r2.getExtras();
        if (r0 == null) goto L8;
        r0.remove(r3);
        if (r0.isEmpty() == false) goto L9;
        r2.setExtras(null);
        return;
    L9:
        return;
    }

    public static void removeMslAltitude(Location r1) {
        removeExtra(r1, EXTRA_MSL_ALTITUDE);
    }

    public static void removeMslAltitudeAccuracy(Location r1) {
        removeExtra(r1, EXTRA_MSL_ALTITUDE_ACCURACY);
    }

    public static void setBearingAccuracyDegrees(Location r2, float r3) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Api26Impl.setBearingAccuracyDegrees(r2, r3);
        return;
    L5:
        getOrCreateExtras(r2).putFloat(EXTRA_BEARING_ACCURACY, r3);
    }

    public static void setMock(Location r4, boolean r5) {
        getSetIsFromMockProviderMethod().invoke(r4, new Object[]{Boolean.valueOf(r5)});     // Catch: InvocationTargetException -> L4 IllegalAccessException -> L7 NoSuchMethodException -> L10
        return;
    L7:
        e = move-exception;
        IllegalAccessError r1 = new IllegalAccessError();
        r1.initCause(e);
        throw r1;
    L10:
        e = move-exception;
        NoSuchMethodError r2 = new NoSuchMethodError();
        r2.initCause(e);
        throw r2;
    L4:
        e = move-exception;
        throw new RuntimeException(e);
    }

    public static void setMslAltitudeAccuracyMeters(Location r2, float r3) {
        getOrCreateExtras(r2).putFloat(EXTRA_MSL_ALTITUDE_ACCURACY, r3);
    }

    public static void setMslAltitudeMeters(Location r2, double r3) {
        getOrCreateExtras(r2).putDouble(EXTRA_MSL_ALTITUDE, r3);
    }

    public static void setSpeedAccuracyMetersPerSecond(Location r2, float r3) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Api26Impl.setSpeedAccuracyMetersPerSecond(r2, r3);
        return;
    L5:
        getOrCreateExtras(r2).putFloat(EXTRA_SPEED_ACCURACY, r3);
    }

    public static void setVerticalAccuracyMeters(Location r2, float r3) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Api26Impl.setVerticalAccuracyMeters(r2, r3);
        return;
    L5:
        getOrCreateExtras(r2).putFloat(EXTRA_VERTICAL_ACCURACY, r3);
    }
}
