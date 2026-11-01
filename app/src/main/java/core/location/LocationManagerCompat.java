package androidx.core.location;

import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.collection.SimpleArrayMap;
import androidx.core.os.CancellationSignal;
import androidx.core.os.ExecutorCompat;
import androidx.core.util.Consumer;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
public final class LocationManagerCompat {
    private static final long GET_CURRENT_LOCATION_TIMEOUT_MS = 30000;
    private static final long MAX_CURRENT_LOCATION_AGE_MS = 10000;
    private static final long PRE_N_LOOPER_TIMEOUT_S = 5;
    private static Field sContextField;
    private static Method sGnssRequestBuilderBuildMethod;
    private static Class<?> sGnssRequestBuilderClass;
    static final WeakHashMap<LocationListenerKey, WeakReference<LocationListenerTransport>> sLocationListeners = null;
    private static Method sRegisterGnssMeasurementsCallbackMethod;

    static class Api19Impl {
        private static Class<?> sLocationRequestClass;
        private static Method sRequestLocationUpdatesLooperMethod;

        private Api19Impl() {
        }

        static boolean tryRequestLocationUpdates(LocationManager r8, String r9, LocationRequestCompat r10, LocationListenerCompat r11, Looper r12) {
            if (sLocationRequestClass != null) goto L7;
            sLocationRequestClass = Class.forName("android.location.LocationRequest");     // Catch: UnsupportedOperationException -> L14 ClassNotFoundException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20 NoSuchMethodException -> L22
        L7:
            if (sRequestLocationUpdatesLooperMethod != null) goto L9;
            sRequestLocationUpdatesLooperMethod = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[]{sLocationRequestClass, LocationListener.class, Looper.class});     // Catch: UnsupportedOperationException -> L14 ClassNotFoundException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20 NoSuchMethodException -> L22
            sRequestLocationUpdatesLooperMethod.setAccessible(true);     // Catch: UnsupportedOperationException -> L14 ClassNotFoundException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20 NoSuchMethodException -> L22
        L9:
            LocationRequest r1 = r10.toLocationRequest(r9);     // Catch: UnsupportedOperationException -> L14 ClassNotFoundException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20 NoSuchMethodException -> L22
            if (r1 == null) goto L23;
            sRequestLocationUpdatesLooperMethod.invoke(r8, new Object[]{r1, r11, r12});     // Catch: UnsupportedOperationException -> L14 ClassNotFoundException -> L16 IllegalAccessException -> L18 InvocationTargetException -> L20 NoSuchMethodException -> L22
            return true;
        L23:
            return false;
        }

        static boolean tryRequestLocationUpdates(LocationManager r8, String r9, LocationRequestCompat r10, LocationListenerTransport r11) {
            if (sLocationRequestClass != null) goto L7;
            sLocationRequestClass = Class.forName("android.location.LocationRequest");     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
        L7:
            if (sRequestLocationUpdatesLooperMethod != null) goto L9;
            sRequestLocationUpdatesLooperMethod = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[]{sLocationRequestClass, LocationListener.class, Looper.class});     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
            sRequestLocationUpdatesLooperMethod.setAccessible(true);     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
        L9:
            LocationRequest r1 = r10.toLocationRequest(r9);     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
            if (r1 == null) goto L29;
            WeakHashMap<LocationListenerKey, WeakReference<LocationListenerTransport>> r5 = LocationManagerCompat.sLocationListeners;     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
            monitor-enter(r5);     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
            sRequestLocationUpdatesLooperMethod.invoke(r8, new Object[]{r1, r11, Looper.getMainLooper()});     // Catch: Throwable -> L16
            LocationManagerCompat.registerLocationListenerTransport(r8, r11);     // Catch: Throwable -> L16
            monitor-exit(r5);     // Catch: Throwable -> L16
            return true;
        L16:
            th = move-exception;
            throw th;     // Catch: UnsupportedOperationException -> L20 ClassNotFoundException -> L22 IllegalAccessException -> L24 InvocationTargetException -> L26 NoSuchMethodException -> L28
        L29:
            return false;
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static boolean registerGnssMeasurementsCallback(LocationManager r1, GnssMeasurementsEvent.Callback r2, Handler r3) {
            return r1.registerGnssMeasurementsCallback(r2, r3);
        }

        static boolean registerGnssStatusCallback(LocationManager r5, Handler r6, Executor r7, GnssStatusCompat.Callback r8) {
            if (r6 == null) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2);
            SimpleArrayMap<Object, Object> r3 = GnssListenersHolder.sGnssStatusListeners;
            monitor-enter(r3);
            PreRGnssStatusTransport r4 = (PreRGnssStatusTransport) GnssListenersHolder.sGnssStatusListeners.get(r8);     // Catch: Throwable -> L19
            if (r4 != null) goto L11;
            r4 = new PreRGnssStatusTransport(r8);     // Catch: Throwable -> L19
        L12:
            r4.register(r7);     // Catch: Throwable -> L19
            if (r5.registerGnssStatusCallback(r4, r6) == false) goto L17;
            GnssListenersHolder.sGnssStatusListeners.put(r8, r4);     // Catch: Throwable -> L19
            monitor-exit(r3);     // Catch: Throwable -> L19
            return true;
        L17:
            monitor-exit(r3);     // Catch: Throwable -> L19
            return false;
        L11:
            r4.unregister();     // Catch: Throwable -> L19
        L19:
            th = move-exception;
            throw th;
        L5:
            r2 = false;
            goto L6
        }

        static void unregisterGnssMeasurementsCallback(LocationManager r0, GnssMeasurementsEvent.Callback r1) {
            r0.unregisterGnssMeasurementsCallback(r1);
        }

        static void unregisterGnssStatusCallback(LocationManager r1, Object r2) {
            if ((r2 instanceof PreRGnssStatusTransport) == false) goto L5;
            ((PreRGnssStatusTransport) r2).unregister();
        L5:
            r1.unregisterGnssStatusCallback((GnssStatus.Callback) r2);
        }
    }

    private static class Api28Impl {
        private Api28Impl() {
        }

        static String getGnssHardwareModelName(LocationManager r1) {
            return r1.getGnssHardwareModelName();
        }

        static int getGnssYearOfHardware(LocationManager r1) {
            return r1.getGnssYearOfHardware();
        }

        static boolean isLocationEnabled(LocationManager r1) {
            return r1.isLocationEnabled();
        }
    }

    private static class Api30Impl {
        private static Class<?> sLocationRequestClass;
        private static Method sRequestLocationUpdatesExecutorMethod;

        private Api30Impl() {
        }

        static void getCurrentLocation(LocationManager r2, String r3, CancellationSignal r4, Executor r5, final Consumer<Location> r6) {
            if (r4 == null) goto L4;
            android.os.CancellationSignal r0 = (android.os.CancellationSignal) r4.getCancellationSignalObject();
        L5:
            Objects.requireNonNull(r6);
            r2.getCurrentLocation(r3, r0, r5, new LocationManagerCompat$Api30Impl$$ExternalSyntheticLambda0(r6));
            return;
        L4:
            r0 = null;
            goto L5
        }

        public static boolean registerGnssStatusCallback(LocationManager r3, Handler r4, Executor r5, GnssStatusCompat.Callback r6) {
            SimpleArrayMap<Object, Object> r0 = GnssListenersHolder.sGnssStatusListeners;
            monitor-enter(r0);
            GnssStatusTransport r1 = (GnssStatusTransport) GnssListenersHolder.sGnssStatusListeners.get(r6);     // Catch: Throwable -> L16
            if (r1 != null) goto L8;
            r1 = new GnssStatusTransport(r6);     // Catch: Throwable -> L16
        L8:
            if (r3.registerGnssStatusCallback(r5, r1) == false) goto L13;
            GnssListenersHolder.sGnssStatusListeners.put(r6, r1);     // Catch: Throwable -> L16
            monitor-exit(r0);     // Catch: Throwable -> L16
            return true;
        L13:
            monitor-exit(r0);     // Catch: Throwable -> L16
            return false;
        L16:
            th = move-exception;
            throw th;
        }

        public static boolean tryRequestLocationUpdates(LocationManager r8, String r9, LocationRequestCompat r10, Executor r11, LocationListenerCompat r12) {
            if (Build.VERSION.SDK_INT >= 30) goto L25;
        L24:
            return false;
        L25:
            if (sLocationRequestClass != null) goto L8;
            sLocationRequestClass = Class.forName("android.location.LocationRequest");     // Catch: UnsupportedOperationException -> L15 ClassNotFoundException -> L17 IllegalAccessException -> L19 InvocationTargetException -> L21 NoSuchMethodException -> L23
        L8:
            if (sRequestLocationUpdatesExecutorMethod != null) goto L10;
            sRequestLocationUpdatesExecutorMethod = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[]{sLocationRequestClass, Executor.class, LocationListener.class});     // Catch: UnsupportedOperationException -> L15 ClassNotFoundException -> L17 IllegalAccessException -> L19 InvocationTargetException -> L21 NoSuchMethodException -> L23
            sRequestLocationUpdatesExecutorMethod.setAccessible(true);     // Catch: UnsupportedOperationException -> L15 ClassNotFoundException -> L17 IllegalAccessException -> L19 InvocationTargetException -> L21 NoSuchMethodException -> L23
        L10:
            LocationRequest r0 = r10.toLocationRequest(r9);     // Catch: UnsupportedOperationException -> L15 ClassNotFoundException -> L17 IllegalAccessException -> L19 InvocationTargetException -> L21 NoSuchMethodException -> L23
            if (r0 == null) goto L24;
            sRequestLocationUpdatesExecutorMethod.invoke(r8, new Object[]{r0, r11, r12});     // Catch: UnsupportedOperationException -> L15 ClassNotFoundException -> L17 IllegalAccessException -> L19 InvocationTargetException -> L21 NoSuchMethodException -> L23
            return true;
            goto L24
        }
    }

    private static class Api31Impl {
        private Api31Impl() {
        }

        static boolean hasProvider(LocationManager r1, String r2) {
            return r1.hasProvider(r2);
        }

        static boolean registerGnssMeasurementsCallback(LocationManager r1, Executor r2, GnssMeasurementsEvent.Callback r3) {
            return r1.registerGnssMeasurementsCallback(r2, r3);
        }

        static void requestLocationUpdates(LocationManager r0, String r1, LocationRequest r2, Executor r3, LocationListener r4) {
            r0.requestLocationUpdates(r1, r2, r3, r4);
        }
    }

    private static final class CancellableLocationListener implements LocationListener {
        private Consumer<Location> mConsumer;
        private final Executor mExecutor;
        private final LocationManager mLocationManager;
        private final Handler mTimeoutHandler;
        Runnable mTimeoutRunnable;
        private boolean mTriggered;

        CancellableLocationListener(LocationManager r3, Executor r4, Consumer<Location> r5) {
            this.mLocationManager = r3;
            this.mExecutor = r4;
            this.mTimeoutHandler = new Handler(Looper.getMainLooper());
            this.mConsumer = r5;
        }

        private void cleanup() {
            this.mConsumer = null;
            this.mLocationManager.removeUpdates(this);
            if (this.mTimeoutRunnable == null) goto L6;
            this.mTimeoutHandler.removeCallbacks(this.mTimeoutRunnable);
            this.mTimeoutRunnable = null;
            return;
        }

        static /* synthetic */ void lambda$onLocationChanged$1(Consumer r0, Location r1) {
            r0.accept(r1);
        }

        public void cancel() {
            monitor-enter(this);
        L11:
            th = move-exception;
            throw th;
        L4:
            if (this.mTriggered == false) goto L7;
            monitor-exit(this);     // Catch: Throwable -> L11
            return;
        L7:
            this.mTriggered = true;     // Catch: Throwable -> L11
            monitor-exit(this);     // Catch: Throwable -> L11
            cleanup();
        }

        /* JADX INFO: renamed from: lambda$startTimeout$0$androidx-core-location-LocationManagerCompat$CancellableLocationListener */
        /* synthetic */ void m16x40ccd759() {
            this.mTimeoutRunnable = null;
            Location r1 = (Location) null;
            onLocationChanged(null);
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(final Location r4) {
            monitor-enter(this);
        L11:
            th = move-exception;
            throw th;
        L4:
            if (this.mTriggered == false) goto L7;
            monitor-exit(this);     // Catch: Throwable -> L11
            return;
        L7:
            this.mTriggered = true;     // Catch: Throwable -> L11
            monitor-exit(this);     // Catch: Throwable -> L11
            final Consumer<Location> r0 = this.mConsumer;
            this.mExecutor.execute(new RunnableC0067x27d5f43b(r0, r4));
            cleanup();
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String r3) {
            Location r1 = (Location) null;
            onLocationChanged(null);
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String r1) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String r1, int r2, Bundle r3) {
        }

        public void startTimeout(long r3) {
            monitor-enter(this);
        L10:
            th = move-exception;
            throw th;
        L4:
            if (this.mTriggered == false) goto L7;
            monitor-exit(this);     // Catch: Throwable -> L10
            return;
        L7:
            this.mTimeoutRunnable = new RunnableC0066x27d5f43a(this);     // Catch: Throwable -> L10
            this.mTimeoutHandler.postDelayed(this.mTimeoutRunnable, r3);     // Catch: Throwable -> L10
            monitor-exit(this);     // Catch: Throwable -> L10
        }
    }

    private static class GnssListenersHolder {
        static final SimpleArrayMap<Object, Object> sGnssStatusListeners = null;

        static {
            sGnssStatusListeners = new SimpleArrayMap();
        }

        private GnssListenersHolder() {
        }
    }

    private static class GnssStatusTransport extends GnssStatus.Callback {
        final GnssStatusCompat.Callback mCallback;

        GnssStatusTransport(GnssStatusCompat.Callback r3) {
            if (r3 == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "invalid null callback");
            this.mCallback = r3;
            return;
        L5:
            r0 = false;
            goto L6
        }

        @Override // android.location.GnssStatus.Callback
        public void onFirstFix(int r2) {
            this.mCallback.onFirstFix(r2);
        }

        @Override // android.location.GnssStatus.Callback
        public void onSatelliteStatusChanged(GnssStatus r3) {
            this.mCallback.onSatelliteStatusChanged(GnssStatusCompat.wrap(r3));
        }

        @Override // android.location.GnssStatus.Callback
        public void onStarted() {
            this.mCallback.onStarted();
        }

        @Override // android.location.GnssStatus.Callback
        public void onStopped() {
            this.mCallback.onStopped();
        }
    }

    private static class GpsStatusTransport implements GpsStatus.Listener {
        final GnssStatusCompat.Callback mCallback;
        volatile Executor mExecutor;
        private final LocationManager mLocationManager;

        GpsStatusTransport(LocationManager r3, GnssStatusCompat.Callback r4) {
            if (r4 == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "invalid null callback");
            this.mLocationManager = r3;
            this.mCallback = r4;
            return;
        L5:
            r0 = false;
            goto L6
        }

        /* JADX INFO: renamed from: lambda$onGpsStatusChanged$0$androidx-core-location-LocationManagerCompat$GpsStatusTransport */
        /* synthetic */ void m17x75e92221(Executor r2) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onStarted();
        }

        /* JADX INFO: renamed from: lambda$onGpsStatusChanged$1$androidx-core-location-LocationManagerCompat$GpsStatusTransport */
        /* synthetic */ void m18xc3a89a22(Executor r2) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onStopped();
        }

        /* JADX INFO: renamed from: lambda$onGpsStatusChanged$2$androidx-core-location-LocationManagerCompat$GpsStatusTransport */
        /* synthetic */ void m19x11681223(Executor r2, int r3) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onFirstFix(r3);
        }

        /* JADX INFO: renamed from: lambda$onGpsStatusChanged$3$androidx-core-location-LocationManagerCompat$GpsStatusTransport */
        /* synthetic */ void m20x5f278a24(Executor r2, GnssStatusCompat r3) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onSatelliteStatusChanged(r3);
        }

        @Override // android.location.GpsStatus.Listener
        public void onGpsStatusChanged(int r5) {
            final Executor r0 = this.mExecutor;
            if (r0 != null) goto L6;
            return;
        L6:
            switch(r5) {
                case 1: goto L15;
                case 2: goto L14;
                case 3: goto L11;
                case 4: goto L8;
                default: goto L22;
            };
        L8:
            GpsStatus r1 = this.mLocationManager.getGpsStatus(null);
            if (r1 == null) goto L17;
            final GnssStatusCompat r2 = GnssStatusCompat.wrap(r1);
            r0.execute(new RunnableC0071x7b1274a9(this, r0, r2));
            return;
        L17:
            return;
        L11:
            GpsStatus r3 = this.mLocationManager.getGpsStatus(null);
            if (r3 == null) goto L19;
            final int r4 = r3.getTimeToFirstFix();
            r0.execute(new RunnableC0070x7b1274a8(this, r0, r4));
            return;
        L19:
            return;
        L14:
            r0.execute(new RunnableC0069x7b1274a7(this, r0));
            return;
        L15:
            r0.execute(new RunnableC0068x7b1274a6(this, r0));
            return;
        }

        public void register(Executor r2) {
            if (this.mExecutor != null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0);
            this.mExecutor = r2;
            return;
        L5:
            r0 = false;
            goto L6
        }

        public void unregister() {
            this.mExecutor = null;
        }
    }

    private static final class InlineHandlerExecutor implements Executor {
        private final Handler mHandler;

        InlineHandlerExecutor(Handler r2) {
            this.mHandler = (Handler) Preconditions.checkNotNull(r2);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable r4) {
            if (Looper.myLooper() != this.mHandler.getLooper()) goto L6;
            r4.run();
            return;
        L6:
            if (this.mHandler.post((Runnable) Preconditions.checkNotNull(r4)) == false) goto L9;
            return;
        L9:
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }

    private static class LocationListenerKey {
        final LocationListenerCompat mListener;
        final String mProvider;

        LocationListenerKey(String r2, LocationListenerCompat r3) {
            this.mProvider = (String) ObjectsCompat.requireNonNull(r2, "invalid null provider");
            this.mListener = (LocationListenerCompat) ObjectsCompat.requireNonNull(r3, "invalid null listener");
        }

        public boolean equals(Object r5) {
            if ((r5 instanceof LocationListenerKey) == true) goto L5;
            return false;
        L5:
            LocationListenerKey r0 = (LocationListenerKey) r5;
            if (this.mProvider.equals(r0.mProvider) == true) goto L8;
            return false;
        L8:
            if (this.mListener.equals(r0.mListener) == false) goto L12;
            return true;
        L12:
            return false;
        }

        public int hashCode() {
            return ObjectsCompat.hash(new Object[]{this.mProvider, this.mListener});
        }
    }

    private static class LocationListenerTransport implements LocationListener {
        final Executor mExecutor;
        volatile LocationListenerKey mKey;

        LocationListenerTransport(LocationListenerKey r1, Executor r2) {
            this.mKey = r1;
            this.mExecutor = r2;
        }

        public LocationListenerKey getKey() {
            return (LocationListenerKey) ObjectsCompat.requireNonNull(this.mKey);
        }

        /* JADX INFO: renamed from: lambda$onFlushComplete$2$androidx-core-location-LocationManagerCompat$LocationListenerTransport */
        /* synthetic */ void m21xf04cfe9d(int r3) {
            LocationListenerKey r0 = this.mKey;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.mListener.onFlushComplete(r3);
        }

        /* JADX INFO: renamed from: lambda$onLocationChanged$0$androidx-core-location-LocationManagerCompat$LocationListenerTransport */
        /* synthetic */ void m22xa8d50b3d(Location r3) {
            LocationListenerKey r0 = this.mKey;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.mListener.onLocationChanged(r3);
        }

        /* JADX INFO: renamed from: lambda$onLocationChanged$1$androidx-core-location-LocationManagerCompat$LocationListenerTransport */
        /* synthetic */ void m23x2b1fc01c(List r3) {
            LocationListenerKey r0 = this.mKey;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.mListener.onLocationChanged(r3);
        }

        /* JADX INFO: renamed from: lambda$onProviderDisabled$5$androidx-core-location-LocationManagerCompat$LocationListenerTransport */
        /* synthetic */ void m24x442abc92(String r3) {
            LocationListenerKey r0 = this.mKey;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.mListener.onProviderDisabled(r3);
        }

        /* JADX INFO: renamed from: lambda$onProviderEnabled$4$androidx-core-location-LocationManagerCompat$LocationListenerTransport */
        /* synthetic */ void m25x5a2a7b08(String r3) {
            LocationListenerKey r0 = this.mKey;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.mListener.onProviderEnabled(r3);
        }

        /* JADX INFO: renamed from: lambda$onStatusChanged$3$androidx-core-location-LocationManagerCompat$LocationListenerTransport */
        /* synthetic */ void m26xdbe6a717(String r3, int r4, Bundle r5) {
            LocationListenerKey r0 = this.mKey;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.mListener.onStatusChanged(r3, r4, r5);
        }

        @Override // android.location.LocationListener
        public void onFlushComplete(final int r3) {
            if (this.mKey != null) goto L5;
            return;
        L5:
            this.mExecutor.execute(new RunnableC0076xa0af9a69(this, r3));
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(final Location r3) {
            if (this.mKey != null) goto L5;
            return;
        L5:
            this.mExecutor.execute(new RunnableC0075xa0af9a68(this, r3));
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(final List<Location> r3) {
            if (this.mKey != null) goto L5;
            return;
        L5:
            this.mExecutor.execute(new RunnableC0074xa0af9a67(this, r3));
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(final String r3) {
            if (this.mKey != null) goto L5;
            return;
        L5:
            this.mExecutor.execute(new RunnableC0073xa0af9a66(this, r3));
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(final String r3) {
            if (this.mKey != null) goto L5;
            return;
        L5:
            this.mExecutor.execute(new RunnableC0072xa0af9a65(this, r3));
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(final String r3, final int r4, final Bundle r5) {
            if (this.mKey != null) goto L5;
            return;
        L5:
            this.mExecutor.execute(new RunnableC0077xa0af9a6a(this, r3, r4, r5));
        }

        public void unregister() {
            this.mKey = null;
        }
    }

    private static class PreRGnssStatusTransport extends GnssStatus.Callback {
        final GnssStatusCompat.Callback mCallback;
        volatile Executor mExecutor;

        PreRGnssStatusTransport(GnssStatusCompat.Callback r3) {
            if (r3 == null) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkArgument(r0, "invalid null callback");
            this.mCallback = r3;
            return;
        L5:
            r0 = false;
            goto L6
        }

        /* JADX INFO: renamed from: lambda$onFirstFix$2$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport */
        /* synthetic */ void m27x4191f1e(Executor r2, int r3) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onFirstFix(r3);
        }

        /* JADX INFO: renamed from: lambda$onSatelliteStatusChanged$3$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport */
        /* synthetic */ void m28xdecf6cdb(Executor r3, GnssStatus r4) {
            if (this.mExecutor == r3) goto L5;
            return;
        L5:
            this.mCallback.onSatelliteStatusChanged(GnssStatusCompat.wrap(r4));
        }

        /* JADX INFO: renamed from: lambda$onStarted$0$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport */
        /* synthetic */ void m29x7ba12b9c(Executor r2) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onStarted();
        }

        /* JADX INFO: renamed from: lambda$onStopped$1$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport */
        /* synthetic */ void m30x80a5cd6f(Executor r2) {
            if (this.mExecutor == r2) goto L5;
            return;
        L5:
            this.mCallback.onStopped();
        }

        @Override // android.location.GnssStatus.Callback
        public void onFirstFix(final int r3) {
            final Executor r0 = this.mExecutor;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.execute(new RunnableC0078xcc169346(this, r0, r3));
        }

        @Override // android.location.GnssStatus.Callback
        public void onSatelliteStatusChanged(final GnssStatus r3) {
            final Executor r0 = this.mExecutor;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.execute(new RunnableC0079xcc169347(this, r0, r3));
        }

        @Override // android.location.GnssStatus.Callback
        public void onStarted() {
            final Executor r0 = this.mExecutor;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.execute(new RunnableC0081xcc169349(this, r0));
        }

        @Override // android.location.GnssStatus.Callback
        public void onStopped() {
            final Executor r0 = this.mExecutor;
            if (r0 != null) goto L5;
            return;
        L5:
            r0.execute(new RunnableC0080xcc169348(this, r0));
        }

        public void register(Executor r5) {
            boolean r0 = true;
            if (r5 == null) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2, "invalid null executor");
            if (this.mExecutor == null) goto L10;
            r0 = false;
        L10:
            Preconditions.checkState(r0);
            this.mExecutor = r5;
            return;
        L5:
            r2 = false;
            goto L6
        }

        public void unregister() {
            this.mExecutor = null;
        }
    }

    static {
        sLocationListeners = new WeakHashMap();
    }

    private LocationManagerCompat() {
    }

    public static void getCurrentLocation(LocationManager r9, String r10, CancellationSignal r11, Executor r12, final Consumer<Location> r13) {
        if (Build.VERSION.SDK_INT < 30) goto L6;
        Api30Impl.getCurrentLocation(r9, r10, r11, r12, r13);
        return;
    L6:
        if (r11 == null) goto L8;
        r11.throwIfCanceled();
    L8:
        final Location r0 = r9.getLastKnownLocation(r10);
        if (r0 != null) goto L11;
    L14:
        final CancellableLocationListener r1 = new CancellableLocationListener(r9, r12, r13);
        r9.requestLocationUpdates(r10, 0, 0.0f, r1, Looper.getMainLooper());
        if (r11 == null) goto L17;
        Objects.requireNonNull(r1);
        r11.setOnCancelListener(new LocationManagerCompat$$ExternalSyntheticLambda1(r1));
    L17:
        r1.startTimeout(30000);
        return;
    L11:
        if ((SystemClock.elapsedRealtime() - LocationCompat.getElapsedRealtimeMillis(r0)) >= 10000) goto L14;
        r12.execute(new LocationManagerCompat$$ExternalSyntheticLambda0(r13, r0));
    }

    public static String getGnssHardwareModelName(LocationManager r2) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return null;
    L5:
        return Api28Impl.getGnssHardwareModelName(r2);
    }

    public static int getGnssYearOfHardware(LocationManager r2) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        return 0;
    L5:
        return Api28Impl.getGnssYearOfHardware(r2);
    }

    public static boolean hasProvider(LocationManager r3, String r4) {
        if (Build.VERSION.SDK_INT < 31) goto L7;
        return Api31Impl.hasProvider(r3, r4);
    L7:
        if (r3.getAllProviders().contains(r4) == false) goto L17;
        return true;
    L17:
        if (r3.getProvider(r4) == null) goto L14;
        return true;
    L14:
        return false;
    L16:
        return false;
    }

    public static boolean isLocationEnabled(LocationManager r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.isLocationEnabled(r2);
    L7:
        if (r2.isProviderEnabled("network") == false) goto L9;
    L12:
        return true;
    L9:
        if (r2.isProviderEnabled("gps") == true) goto L12;
        return false;
    }

    static /* synthetic */ void lambda$getCurrentLocation$0(Consumer r0, Location r1) {
        r0.accept(r1);
    }

    static /* synthetic */ Boolean lambda$registerGnssStatusCallback$1(LocationManager r1, GpsStatusTransport r2) throws Exception {
        return Boolean.valueOf(r1.addGpsStatusListener(r2));
    }

    public static boolean registerGnssMeasurementsCallback(LocationManager r2, GnssMeasurementsEvent.Callback r3, Handler r4) {
        if (Build.VERSION.SDK_INT == 30) goto L7;
        return Api24Impl.registerGnssMeasurementsCallback(r2, r3, r4);
    L7:
        return registerGnssMeasurementsCallbackOnR(r2, ExecutorCompat.create(r4), r3);
    }

    public static boolean registerGnssMeasurementsCallback(LocationManager r2, Executor r3, GnssMeasurementsEvent.Callback r4) {
        if (Build.VERSION.SDK_INT <= 30) goto L7;
        return Api31Impl.registerGnssMeasurementsCallback(r2, r3, r4);
    L7:
        return registerGnssMeasurementsCallbackOnR(r2, r3, r4);
    }

    private static boolean registerGnssMeasurementsCallbackOnR(LocationManager r8, Executor r9, GnssMeasurementsEvent.Callback r10) {
        if (Build.VERSION.SDK_INT != 30) goto L31;
        if (sGnssRequestBuilderClass != null) goto L9;
        sGnssRequestBuilderClass = Class.forName("android.location.GnssRequest$Builder");     // Catch: Throwable -> L20 IllegalAccessException -> L22 NoSuchMethodException -> L24 InvocationTargetException -> L26 ClassNotFoundException -> L28
    L9:
        if (sGnssRequestBuilderBuildMethod != null) goto L12;
        sGnssRequestBuilderBuildMethod = sGnssRequestBuilderClass.getDeclaredMethod("build", new Class[0]);     // Catch: Throwable -> L20 IllegalAccessException -> L22 NoSuchMethodException -> L24 InvocationTargetException -> L26 ClassNotFoundException -> L28
        sGnssRequestBuilderBuildMethod.setAccessible(true);     // Catch: Throwable -> L20 IllegalAccessException -> L22 NoSuchMethodException -> L24 InvocationTargetException -> L26 ClassNotFoundException -> L28
    L12:
        if (sRegisterGnssMeasurementsCallbackMethod != null) goto L14;
        sRegisterGnssMeasurementsCallbackMethod = LocationManager.class.getDeclaredMethod("registerGnssMeasurementsCallback", new Class[]{Class.forName("android.location.GnssRequest"), Executor.class, GnssMeasurementsEvent.Callback.class});     // Catch: Throwable -> L20 IllegalAccessException -> L22 NoSuchMethodException -> L24 InvocationTargetException -> L26 ClassNotFoundException -> L28
        sRegisterGnssMeasurementsCallbackMethod.setAccessible(true);     // Catch: Throwable -> L20 IllegalAccessException -> L22 NoSuchMethodException -> L24 InvocationTargetException -> L26 ClassNotFoundException -> L28
    L14:
        Object r1 = sRegisterGnssMeasurementsCallbackMethod.invoke(r8, new Object[]{sGnssRequestBuilderBuildMethod.invoke(sGnssRequestBuilderClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[0]), r9, r10});     // Catch: Throwable -> L20 IllegalAccessException -> L22 NoSuchMethodException -> L24 InvocationTargetException -> L26 ClassNotFoundException -> L28
        if (r1 == null) goto L34;
        if (((Boolean) r1).booleanValue() == true) goto L19;
        return false;
    L19:
        return true;
    L34:
        return false;
    L29:
        return false;
    L31:
        throw new IllegalStateException();
    }

    private static boolean registerGnssStatusCallback(final LocationManager r18, Handler r19, Executor r20, GnssStatusCompat.Callback r21) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return Api30Impl.registerGnssStatusCallback(r18, r19, r20, r21);
    L7:
        if (Build.VERSION.SDK_INT < 24) goto L11;
        return Api24Impl.registerGnssStatusCallback(r18, r19, r20, r21);
    L11:
        if (r19 == null) goto L13;
        boolean r0 = true;
    L14:
        Preconditions.checkArgument(r0);
        SimpleArrayMap<Object, Object> r6 = GnssListenersHolder.sGnssStatusListeners;
        monitor-enter(r6);
        GpsStatusTransport r1 = (GpsStatusTransport) GnssListenersHolder.sGnssStatusListeners.get(r21);     // Catch: Throwable -> L68
        if (r1 != null) goto L19;
        GpsStatusTransport r7 = new GpsStatusTransport(r18, r21);     // Catch: Throwable -> L68
    L78:
        r7.register(r20);     // Catch: Throwable -> L72
        final GpsStatusTransport r9 = r7;
        FutureTask r2 = new FutureTask(new LocationManagerCompat$$ExternalSyntheticLambda2(r18, r9));     // Catch: Throwable -> L72
        if (Looper.myLooper() != r19.getLooper()) goto L26;
        r2.run();     // Catch: Throwable -> L72
    L27:
        boolean r11 = false;
        long r12 = TimeUnit.SECONDS.toNanos(5);     // Catch: Throwable -> L46 TimeoutException -> L48 ExecutionException -> L51
        long r14 = System.nanoTime() + r12;
    L75:
        if (((Boolean) r2.get(r12, TimeUnit.NANOSECONDS)).booleanValue() == false) goto L38;
        GnssListenersHolder.sGnssStatusListeners.put(r21, r9);     // Catch: InterruptedException -> L42 Throwable -> L46 TimeoutException -> L48 ExecutionException -> L51
        if (r11 == false) goto L35;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L72
    L35:
        monitor-exit(r6);     // Catch: Throwable -> L72
        return true;
    L38:
        if (r11 == false) goto L40;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L72
    L40:
        monitor-exit(r6);     // Catch: Throwable -> L72
        return false;
    L43:
        r11 = true;
        r12 = r14 - System.nanoTime();
    L46:
        th = move-exception;
        if (r11 == false) goto L65;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L72
    L65:
        throw th;     // Catch: Throwable -> L72
    L51:
        e = move-exception;
        if ((e.getCause() instanceof RuntimeException) == true) goto L61;
        if ((e.getCause() instanceof Error) == false) goto L59;
        throw ((Error) e.getCause());     // Catch: Throwable -> L46
    L59:
        throw new IllegalStateException(e);     // Catch: Throwable -> L46
    L61:
        throw ((RuntimeException) e.getCause());     // Catch: Throwable -> L46
    L48:
        e = move-exception;
        throw new IllegalStateException(r19 + " appears to be blocked, please run registerGnssStatusCallback() directly on a Looper thread or ensure the main Looper is not blocked by this thread", e);     // Catch: Throwable -> L46
    L26:
        if (r19.post(r2) == true) goto L27;
        throw new IllegalStateException(r19 + " is shutting down");     // Catch: Throwable -> L72
    L72:
        th = th;
    L70:
        monitor-exit(r6);     // Catch: Throwable -> L72
        throw th;
    L19:
        r1.unregister();     // Catch: Throwable -> L68
        r7 = r1;
    L68:
        th = th;
        goto L70
    L13:
        r0 = false;
        goto L14
    }

    public static boolean registerGnssStatusCallback(LocationManager r2, GnssStatusCompat.Callback r3, Handler r4) {
        if (Build.VERSION.SDK_INT < 30) goto L7;
        return registerGnssStatusCallback(r2, ExecutorCompat.create(r4), r3);
    L7:
        return registerGnssStatusCallback(r2, new InlineHandlerExecutor(r4), r3);
    }

    public static boolean registerGnssStatusCallback(LocationManager r2, Executor r3, GnssStatusCompat.Callback r4) {
        if (Build.VERSION.SDK_INT >= 30) goto L5;
        Looper r0 = Looper.myLooper();
        if (r0 != null) goto L10;
        r0 = Looper.getMainLooper();
    L10:
        return registerGnssStatusCallback(r2, new Handler(r0), r3, r4);
    L5:
        return registerGnssStatusCallback(r2, null, r3, r4);
    }

    static void registerLocationListenerTransport(LocationManager r3, LocationListenerTransport r4) {
        WeakReference<LocationListenerTransport> r0 = sLocationListeners.put(r4.getKey(), new WeakReference(r4));
        if (r0 == null) goto L5;
        LocationListenerTransport r1 = r0.get();
    L6:
        if (r1 == null) goto L9;
        r1.unregister();
        r3.removeUpdates(r1);
        return;
    L9:
        return;
    L5:
        r1 = null;
        goto L6
    }

    public static void removeUpdates(LocationManager r7, LocationListenerCompat r8) {
        WeakHashMap<LocationListenerKey, WeakReference<LocationListenerTransport>> r0 = sLocationListeners;
        monitor-enter(r0);
        ArrayList r1 = null;
        Iterator<WeakReference<LocationListenerTransport>> r2 = sLocationListeners.values().iterator();     // Catch: Throwable -> L25
    L7:
        if (r2.hasNext() == false) goto L17;
        LocationListenerTransport r4 = r2.next().get();     // Catch: Throwable -> L25
        if (r4 == null) goto L7;
        LocationListenerKey r5 = r4.getKey();     // Catch: Throwable -> L25
        if (r5.mListener != r8) goto L7;
        if (r1 != null) goto L15;
        r1 = new ArrayList();     // Catch: Throwable -> L25
    L15:
        r1.add(r5);     // Catch: Throwable -> L25
        r4.unregister();     // Catch: Throwable -> L25
        r7.removeUpdates(r4);     // Catch: Throwable -> L25
        goto L7
    L17:
        if (r1 == null) goto L22;
        Iterator r3 = r1.iterator();     // Catch: Throwable -> L25
    L20:
        if (r3.hasNext() == false) goto L22;
        LocationListenerKey r6 = (LocationListenerKey) r3.next();     // Catch: Throwable -> L25
        sLocationListeners.remove(r6);     // Catch: Throwable -> L25
    L22:
        monitor-exit(r0);     // Catch: Throwable -> L25
        r7.removeUpdates(r8);
        return;
    L25:
        th = move-exception;
        throw th;
    }

    public static void requestLocationUpdates(LocationManager r8, String r9, LocationRequestCompat r10, LocationListenerCompat r11, Looper r12) {
        if (Build.VERSION.SDK_INT < 31) goto L7;
        Api31Impl.requestLocationUpdates(r8, r9, r10.toLocationRequest(), ExecutorCompat.create(new Handler(r12)), r11);
        return;
    L7:
        if (Api19Impl.tryRequestLocationUpdates(r8, r9, r10, r11, r12) == false) goto L9;
        return;
    L9:
        r8.requestLocationUpdates(r9, r10.getIntervalMillis(), r10.getMinUpdateDistanceMeters(), r11, r12);
    }

    public static void requestLocationUpdates(LocationManager r9, String r10, LocationRequestCompat r11, Executor r12, LocationListenerCompat r13) {
        if (Build.VERSION.SDK_INT < 31) goto L7;
        Api31Impl.requestLocationUpdates(r9, r10, r11.toLocationRequest(), r12, r13);
        return;
    L7:
        if (Build.VERSION.SDK_INT >= 30) goto L9;
    L11:
        LocationListenerTransport r0 = new LocationListenerTransport(new LocationListenerKey(r10, r13), r12);
        if (Api19Impl.tryRequestLocationUpdates(r9, r10, r11, r0) == false) goto L14;
        return;
    L14:
        WeakHashMap<LocationListenerKey, WeakReference<LocationListenerTransport>> r1 = sLocationListeners;
        monitor-enter(r1);
        r9.requestLocationUpdates(r10, r11.getIntervalMillis(), r11.getMinUpdateDistanceMeters(), r0, Looper.getMainLooper());     // Catch: Throwable -> L25
        registerLocationListenerTransport(r9, r0);     // Catch: Throwable -> L25
        monitor-exit(r1);     // Catch: Throwable -> L25
        return;
    L21:
        th = move-exception;
        Throwable r14 = th;
    L23:
        monitor-exit(r1);     // Catch: Throwable -> L25
        throw r14;
    L25:
        th = move-exception;
        r14 = th;
        goto L23
    L9:
        if (Api30Impl.tryRequestLocationUpdates(r9, r10, r11, r12, r13) == false) goto L11;
    }

    public static void unregisterGnssMeasurementsCallback(LocationManager r0, GnssMeasurementsEvent.Callback r1) {
        Api24Impl.unregisterGnssMeasurementsCallback(r0, r1);
    }

    public static void unregisterGnssStatusCallback(LocationManager r2, GnssStatusCompat.Callback r3) {
        if (Build.VERSION.SDK_INT < 24) goto L14;
        SimpleArrayMap<Object, Object> r0 = GnssListenersHolder.sGnssStatusListeners;
        monitor-enter(r0);
        Object r1 = GnssListenersHolder.sGnssStatusListeners.remove(r3);     // Catch: Throwable -> L11
        if (r1 == null) goto L9;
        Api24Impl.unregisterGnssStatusCallback(r2, r1);     // Catch: Throwable -> L11
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L11:
        th = move-exception;
        throw th;
    L14:
        SimpleArrayMap<Object, Object> r4 = GnssListenersHolder.sGnssStatusListeners;
        monitor-enter(r4);
        GpsStatusTransport r5 = (GpsStatusTransport) GnssListenersHolder.sGnssStatusListeners.remove(r3);     // Catch: Throwable -> L21
        if (r5 == null) goto L19;
        r5.unregister();     // Catch: Throwable -> L21
        r2.removeGpsStatusListener(r5);     // Catch: Throwable -> L21
    L19:
        monitor-exit(r4);     // Catch: Throwable -> L21
        return;
    L21:
        th = move-exception;
        throw th;
    }
}
