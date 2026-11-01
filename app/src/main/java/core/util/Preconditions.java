package androidx.core.util;

import android.text.TextUtils;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean r1) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException();
    }

    public static void checkArgument(boolean r2, Object r3) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(String.valueOf(r3));
    }

    public static void checkArgument(boolean r2, String r3, Object... r4) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(String.format(r3, r4));
    }

    public static float checkArgumentFinite(float r3, String r4) {
        if (Float.isNaN(r3) == true) goto L10;
        if (Float.isInfinite(r3) == true) goto L8;
        return r3;
    L8:
        throw new IllegalArgumentException(r4 + " must not be infinite");
    L10:
        throw new IllegalArgumentException(r4 + " must not be NaN");
    }

    public static double checkArgumentInRange(double r8, double r10, double r12, String r14) {
        if (r8 < r10) goto L10;
        if (r8 > r12) goto L8;
        return r8;
    L8:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[]{r14, Double.valueOf(r10), Double.valueOf(r12)}));
    L10:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[]{r14, Double.valueOf(r10), Double.valueOf(r12)}));
    }

    public static float checkArgumentInRange(float r8, float r9, float r10, String r11) {
        if (r8 < r9) goto L10;
        if (r8 > r10) goto L8;
        return r8;
    L8:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[]{r11, Float.valueOf(r9), Float.valueOf(r10)}));
    L10:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[]{r11, Float.valueOf(r9), Float.valueOf(r10)}));
    }

    public static int checkArgumentInRange(int r8, int r9, int r10, String r11) {
        if (r8 < r9) goto L9;
        if (r8 > r10) goto L7;
        return r8;
    L7:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{r11, Integer.valueOf(r9), Integer.valueOf(r10)}));
    L9:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{r11, Integer.valueOf(r9), Integer.valueOf(r10)}));
    }

    public static long checkArgumentInRange(long r8, long r10, long r12, String r14) {
        if (r8 < r10) goto L10;
        if (r8 > r12) goto L8;
        return r8;
    L8:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{r14, Long.valueOf(r10), Long.valueOf(r12)}));
    L10:
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{r14, Long.valueOf(r10), Long.valueOf(r12)}));
    }

    public static int checkArgumentNonnegative(int r1) {
        if (r1 < 0) goto L5;
        return r1;
    L5:
        throw new IllegalArgumentException();
    }

    public static int checkArgumentNonnegative(int r1, String r2) {
        if (r1 < 0) goto L5;
        return r1;
    L5:
        throw new IllegalArgumentException(r2);
    }

    public static int checkFlagsArgument(int r3, int r4) {
        if ((r3 & r4) != r3) goto L6;
        return r3;
    L6:
        throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(r3) + ", but only 0x" + Integer.toHexString(r4) + " are allowed");
    }

    public static <T> T checkNotNull(T r1) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(T r2, Object r3) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new NullPointerException(String.valueOf(r3));
    }

    public static void checkState(boolean r1) {
        checkState(r1, null);
    }

    public static void checkState(boolean r1, String r2) {
        if (r1 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException(r2);
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T r1) {
        if (TextUtils.isEmpty(r1) == true) goto L6;
        return r1;
    L6:
        throw new IllegalArgumentException();
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T r2, Object r3) {
        if (TextUtils.isEmpty(r2) == true) goto L6;
        return r2;
    L6:
        throw new IllegalArgumentException(String.valueOf(r3));
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T r2, String r3, Object... r4) {
        if (TextUtils.isEmpty(r2) == true) goto L6;
        return r2;
    L6:
        throw new IllegalArgumentException(String.format(r3, r4));
    }
}
