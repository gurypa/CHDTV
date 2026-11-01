package androidx.core.os;

import android.os.Build;
import androidx.exifinterface.media.ExifInterface;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class BuildCompat {

    public @interface PrereleaseSdkCheck {
    }

    private BuildCompat() {
    }

    @Deprecated
    public static boolean isAtLeastN() {
        if (Build.VERSION.SDK_INT < 24) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastNMR1() {
        if (Build.VERSION.SDK_INT < 25) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastO() {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastOMR1() {
        if (Build.VERSION.SDK_INT < 27) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastP() {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        return true;
    L5:
        return false;
    }

    protected static boolean isAtLeastPreReleaseCodename(String r4, String r5) {
        if ("REL".equals(r5) == false) goto L6;
        return false;
    L6:
        if (r5.toUpperCase(Locale.ROOT).compareTo(r4.toUpperCase(Locale.ROOT)) < 0) goto L9;
        return true;
    L9:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastQ() {
        if (Build.VERSION.SDK_INT < 29) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastR() {
        if (Build.VERSION.SDK_INT < 30) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public static boolean isAtLeastS() {
        if (Build.VERSION.SDK_INT < 31) goto L5;
    L10:
        return true;
    L5:
        if (Build.VERSION.SDK_INT >= 30) goto L7;
    L9:
        return false;
    L7:
        if (isAtLeastPreReleaseCodename(ExifInterface.LATITUDE_SOUTH, Build.VERSION.CODENAME) == false) goto L9;
        goto L9
    }

    @Deprecated
    public static boolean isAtLeastSv2() {
        if (Build.VERSION.SDK_INT < 32) goto L5;
    L10:
        return true;
    L5:
        if (Build.VERSION.SDK_INT >= 31) goto L7;
    L9:
        return false;
    L7:
        if (isAtLeastPreReleaseCodename("Sv2", Build.VERSION.CODENAME) == false) goto L9;
        goto L9
    }

    public static boolean isAtLeastT() {
        if (Build.VERSION.SDK_INT < 33) goto L5;
    L10:
        return true;
    L5:
        if (Build.VERSION.SDK_INT >= 32) goto L7;
    L9:
        return false;
    L7:
        if (isAtLeastPreReleaseCodename("Tiramisu", Build.VERSION.CODENAME) == false) goto L9;
        goto L9
    }

    public static boolean isAtLeastU() {
        if (Build.VERSION.SDK_INT >= 33) goto L5;
    L7:
        return false;
    L5:
        if (isAtLeastPreReleaseCodename("UpsideDownCake", Build.VERSION.CODENAME) == false) goto L7;
        return true;
    }
}
