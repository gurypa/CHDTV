package androidx.media3.exoplayer.scheduler;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class Requirements implements Parcelable {
    public static final Parcelable.Creator<Requirements> CREATOR = null;
    public static final int DEVICE_CHARGING = 8;
    public static final int DEVICE_IDLE = 4;
    public static final int DEVICE_STORAGE_NOT_LOW = 16;
    public static final int NETWORK = 1;
    public static final int NETWORK_UNMETERED = 2;
    private final int requirements;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequirementFlags {
    }

    static {
        CREATOR = new C03221();
    }

    public Requirements(int r2) {
        if ((r2 & 2) == 0) goto L5;
        r2 = r2 | 1;
    L5:
        this.requirements = r2;
    }

    private int getNotMetNetworkRequirements(Context r5) {
        if (isNetworkRequired() == true) goto L5;
        return 0;
    L5:
        ConnectivityManager r0 = (ConnectivityManager) Assertions.checkNotNull(r5.getSystemService("connectivity"));
        NetworkInfo r2 = r0.getActiveNetworkInfo();
        if (r2 == null) goto L20;
        if (r2.isConnected() == false) goto L20;
        if (isInternetConnectivityValidated(r0) == false) goto L20;
        if (isUnmeteredNetworkRequired() == true) goto L15;
    L18:
        return 0;
    L15:
        if (r0.isActiveNetworkMetered() == false) goto L18;
        return 2;
    L20:
        return this.requirements & 3;
    }

    private boolean isDeviceCharging(Context r5) {
        Intent r0 = r5.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (r0 != null) goto L5;
        return false;
    L5:
        int r2 = r0.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
        if (r2 != 2) goto L8;
    L9:
        return true;
    L8:
        if (r2 == 5) goto L9;
        return false;
    }

    private boolean isDeviceIdle(Context r4) {
        PowerManager r0 = (PowerManager) Assertions.checkNotNull(r4.getSystemService("power"));
        if (Util.SDK_INT < 23) goto L6;
        return r0.isDeviceIdleMode();
    L6:
        if (r0.isInteractive() == true) goto L8;
        return true;
    L8:
        return false;
    }

    private static boolean isInternetConnectivityValidated(ConnectivityManager r5) {
        if (Util.SDK_INT >= 24) goto L5;
        return true;
    L5:
        Network r0 = r5.getActiveNetwork();
        if (r0 != null) goto L18;
        return false;
    L18:
        NetworkCapabilities r3 = r5.getNetworkCapabilities(r0);     // Catch: SecurityException -> L16
        if (r3 == null) goto L15;
        if (r3.hasCapability(16) == false) goto L15;
        return true;
    L15:
        return false;
    L17:
        return true;
    }

    private boolean isStorageNotLow(Context r3) {
        if (r3.registerReceiver(null, new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW")) != null) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean checkRequirements(Context r2) {
        if (getNotMetRequirements(r2) != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r5) {
        if (this != r5) goto L6;
        return true;
    L6:
        if (r5 != null) goto L8;
    L15:
        return false;
    L8:
        if (getClass() != r5.getClass()) goto L15;
        if (this.requirements != ((Requirements) r5).requirements) goto L14;
        return true;
    L14:
        return false;
    }

    public Requirements filterRequirements(int r3) {
        int r0 = this.requirements & r3;
        if (r0 != this.requirements) goto L6;
        return this;
    L6:
        return new Requirements(r0);
    }

    public int getNotMetRequirements(Context r3) {
        int r0 = getNotMetNetworkRequirements(r3);
        if (isChargingRequired() == false) goto L8;
        if (isDeviceCharging(r3) == true) goto L8;
        r0 = r0 | 8;
    L8:
        if (isIdleRequired() == false) goto L13;
        if (isDeviceIdle(r3) == true) goto L13;
        r0 = r0 | 4;
    L13:
        if (isStorageNotLowRequired() == true) goto L15;
        return r0;
    L15:
        if (isStorageNotLow(r3) == false) goto L17;
        return r0;
    L17:
        return r0 | 16;
    }

    public int getRequirements() {
        return this.requirements;
    }

    public int hashCode() {
        return this.requirements;
    }

    public boolean isChargingRequired() {
        if ((this.requirements & 8) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isIdleRequired() {
        if ((this.requirements & 4) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isNetworkRequired() {
        if ((this.requirements & 1) != 0) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean isStorageNotLowRequired() {
        if ((this.requirements & 16) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isUnmeteredNetworkRequired() {
        if ((this.requirements & 2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.requirements);
    }
}
