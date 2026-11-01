package androidx.core.view;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public final class DisplayCompat {
    private static final int DISPLAY_SIZE_4K_HEIGHT = 2160;
    private static final int DISPLAY_SIZE_4K_WIDTH = 3840;

    static class Api17Impl {
        private Api17Impl() {
        }

        static void getRealSize(Display r0, Point r1) {
            r0.getRealSize(r1);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static ModeCompat getMode(Context r4, Display r5) {
            Display.Mode r0 = r5.getMode();
            Point r1 = DisplayCompat.getCurrentDisplaySizeFromWorkarounds(r4, r5);
            if (r1 == null) goto L9;
            if (physicalSizeEquals(r0, r1) == true) goto L9;
            return new ModeCompat(r0, r1);
        L9:
            return new ModeCompat(r0, true);
        }

        public static ModeCompat[] getSupportedModes(Context r8, Display r9) {
            Display.Mode[] r0 = r9.getSupportedModes();
            ModeCompat[] r1 = new ModeCompat[r0.length];
            Display.Mode r2 = r9.getMode();
            Point r3 = DisplayCompat.getCurrentDisplaySizeFromWorkarounds(r8, r9);
            if (r3 != null) goto L5;
        L15:
            int r4 = 0;
        L17:
            if (r4 >= r0.length) goto L19;
            boolean r5 = physicalSizeEquals(r0[r4], r2);
            r1[r4] = new ModeCompat(r0[r4], r5);
            r4 = r4 + 1;
        L19:
            return r1;
        L5:
            if (physicalSizeEquals(r2, r3) == true) goto L15;
            int r6 = 0;
        L9:
            if (r6 >= r0.length) goto L19;
            if (physicalSizeEquals(r0[r6], r2) == false) goto L13;
            ModeCompat r7 = new ModeCompat(r0[r6], r3);
        L14:
            r1[r6] = r7;
            r6 = r6 + 1;
            goto L9
        L13:
            r7 = new ModeCompat(r0[r6], false);
            goto L14
        }

        static boolean isCurrentModeTheLargestMode(Display r8) {
            Display.Mode r0 = r8.getMode();
            Display.Mode[] r1 = r8.getSupportedModes();
            int r2 = r1.length;
            int r4 = 0;
        L3:
            if (r4 >= r2) goto L11;
            Display.Mode r5 = r1[r4];
            if (r0.getPhysicalHeight() < r5.getPhysicalHeight()) goto L10;
            if (r0.getPhysicalWidth() < r5.getPhysicalWidth()) goto L10;
            r4 = r4 + 1;
        L10:
            return false;
        L11:
            return true;
        }

        static boolean physicalSizeEquals(Display.Mode r2, Point r3) {
            if (r2.getPhysicalWidth() != r3.x) goto L7;
            if (r2.getPhysicalHeight() != r3.y) goto L7;
        L10:
            return true;
        L7:
            if (r2.getPhysicalWidth() == r3.y) goto L9;
        L11:
            return false;
        L9:
            if (r2.getPhysicalHeight() != r3.x) goto L11;
            goto L10
        }

        static boolean physicalSizeEquals(Display.Mode r2, Display.Mode r3) {
            if (r2.getPhysicalWidth() == r3.getPhysicalWidth()) goto L5;
        L7:
            return false;
        L5:
            if (r2.getPhysicalHeight() != r3.getPhysicalHeight()) goto L7;
            return true;
        }
    }

    public static final class ModeCompat {
        private final boolean mIsNative;
        private final Display.Mode mMode;
        private final Point mPhysicalSize;

        static class Api23Impl {
            private Api23Impl() {
            }

            static int getPhysicalHeight(Display.Mode r1) {
                return r1.getPhysicalHeight();
            }

            static int getPhysicalWidth(Display.Mode r1) {
                return r1.getPhysicalWidth();
            }
        }

        ModeCompat(Point r2) {
            Preconditions.checkNotNull(r2, "physicalSize == null");
            this.mPhysicalSize = r2;
            this.mMode = null;
            this.mIsNative = true;
        }

        ModeCompat(Display.Mode r2, Point r3) {
            Preconditions.checkNotNull(r2, "mode == null, can't wrap a null reference");
            Preconditions.checkNotNull(r3, "physicalSize == null");
            this.mPhysicalSize = r3;
            this.mMode = r2;
            this.mIsNative = true;
        }

        ModeCompat(Display.Mode r4, boolean r5) {
            Preconditions.checkNotNull(r4, "mode == null, can't wrap a null reference");
            this.mPhysicalSize = new Point(Api23Impl.getPhysicalWidth(r4), Api23Impl.getPhysicalHeight(r4));
            this.mMode = r4;
            this.mIsNative = r5;
        }

        public int getPhysicalHeight() {
            return this.mPhysicalSize.y;
        }

        public int getPhysicalWidth() {
            return this.mPhysicalSize.x;
        }

        @Deprecated
        public boolean isNative() {
            return this.mIsNative;
        }

        public Display.Mode toMode() {
            return this.mMode;
        }
    }

    private DisplayCompat() {
    }

    static Point getCurrentDisplaySizeFromWorkarounds(Context r4, Display r5) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        Point r0 = parsePhysicalDisplaySizeFromSystemProperties("sys.display-size", r5);
    L7:
        if (r0 == null) goto L10;
        return r0;
    L10:
        if (isSonyBravia4kTv(r4) == true) goto L12;
        return null;
    L12:
        if (isCurrentModeTheLargestMode(r5) == true) goto L15;
        return null;
    L15:
        return new Point(DISPLAY_SIZE_4K_WIDTH, DISPLAY_SIZE_4K_HEIGHT);
    L5:
        r0 = parsePhysicalDisplaySizeFromSystemProperties("vendor.display-size", r5);
        goto L7
    }

    private static Point getDisplaySize(Context r2, Display r3) {
        Point r0 = getCurrentDisplaySizeFromWorkarounds(r2, r3);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Point r1 = new Point();
        Api17Impl.getRealSize(r3, r1);
        return r1;
    }

    public static ModeCompat getMode(Context r1, Display r2) {
        return Api23Impl.getMode(r1, r2);
    }

    public static ModeCompat[] getSupportedModes(Context r1, Display r2) {
        return Api23Impl.getSupportedModes(r1, r2);
    }

    private static String getSystemProperty(String r6) {
        Class<?> r0 = Class.forName("android.os.SystemProperties");     // Catch: Exception -> L4
        return (String) r0.getMethod("get", new Class[]{String.class}).invoke(r0, new Object[]{r6});
    L5:
        return null;
    }

    static boolean isCurrentModeTheLargestMode(Display r1) {
        return Api23Impl.isCurrentModeTheLargestMode(r1);
    }

    private static boolean isSonyBravia4kTv(Context r2) {
        if (isTv(r2) == true) goto L5;
    L11:
        return false;
    L5:
        if ("Sony".equals(Build.MANUFACTURER) == false) goto L11;
        if (Build.MODEL.startsWith("BRAVIA") == false) goto L11;
        if (r2.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd") == false) goto L11;
        return true;
    }

    private static boolean isTv(Context r3) {
        UiModeManager r0 = (UiModeManager) r3.getSystemService("uimode");
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.getCurrentModeType() != 4) goto L7;
        return true;
    }

    private static Point parseDisplaySize(String r4) throws NumberFormatException {
        String[] r0 = r4.trim().split("x", -1);
        if (r0.length != 2) goto L10;
        int r1 = Integer.parseInt(r0[0]);
        int r2 = Integer.parseInt(r0[1]);
        if (r1 <= 0) goto L10;
        if (r2 <= 0) goto L10;
        return new Point(r1, r2);
    L10:
        throw new NumberFormatException();
    }

    private static Point parsePhysicalDisplaySizeFromSystemProperties(String r3, Display r4) {
        if (r4.getDisplayId() == 0) goto L5;
        return null;
    L5:
        String r0 = getSystemProperty(r3);
        if (TextUtils.isEmpty(r0) == true) goto L13;
        if (r0 == null) goto L13;
        return parseDisplaySize(r0);
    L12:
        return null;
    L13:
        return null;
    }
}
