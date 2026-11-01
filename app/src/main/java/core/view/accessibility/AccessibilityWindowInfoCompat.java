package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityWindowInfoCompat {
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private final Object mInfo;

    private static class Api21Impl {
        private Api21Impl() {
        }

        static void getBoundsInScreen(AccessibilityWindowInfo r0, Rect r1) {
            r0.getBoundsInScreen(r1);
        }

        static AccessibilityWindowInfo getChild(AccessibilityWindowInfo r1, int r2) {
            return r1.getChild(r2);
        }

        static int getChildCount(AccessibilityWindowInfo r1) {
            return r1.getChildCount();
        }

        static int getId(AccessibilityWindowInfo r1) {
            return r1.getId();
        }

        static int getLayer(AccessibilityWindowInfo r1) {
            return r1.getLayer();
        }

        static AccessibilityWindowInfo getParent(AccessibilityWindowInfo r1) {
            return r1.getParent();
        }

        static AccessibilityNodeInfo getRoot(AccessibilityWindowInfo r1) {
            return r1.getRoot();
        }

        static int getType(AccessibilityWindowInfo r1) {
            return r1.getType();
        }

        static boolean isAccessibilityFocused(AccessibilityWindowInfo r1) {
            return r1.isAccessibilityFocused();
        }

        static boolean isActive(AccessibilityWindowInfo r1) {
            return r1.isActive();
        }

        static boolean isFocused(AccessibilityWindowInfo r1) {
            return r1.isFocused();
        }

        static AccessibilityWindowInfo obtain() {
            return AccessibilityWindowInfo.obtain();
        }

        static AccessibilityWindowInfo obtain(AccessibilityWindowInfo r1) {
            return AccessibilityWindowInfo.obtain(r1);
        }

        static void recycle(AccessibilityWindowInfo r0) {
            r0.recycle();
        }
    }

    private static class Api24Impl {
        private Api24Impl() {
        }

        static AccessibilityNodeInfo getAnchor(AccessibilityWindowInfo r1) {
            return r1.getAnchor();
        }

        static CharSequence getTitle(AccessibilityWindowInfo r1) {
            return r1.getTitle();
        }
    }

    private static class Api33Impl {
        private Api33Impl() {
        }

        static int getDisplayId(AccessibilityWindowInfo r1) {
            return r1.getDisplayId();
        }

        static void getRegionInScreen(AccessibilityWindowInfo r0, Region r1) {
            r0.getRegionInScreen(r1);
        }

        static boolean isInPictureInPictureMode(AccessibilityWindowInfo r1) {
            return r1.isInPictureInPictureMode();
        }
    }

    private AccessibilityWindowInfoCompat(Object r1) {
        this.mInfo = r1;
    }

    public static AccessibilityWindowInfoCompat obtain() {
        return wrapNonNullInstance(Api21Impl.obtain());
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat r1) {
        if (r1 != null) goto L5;
        return null;
    L5:
        return wrapNonNullInstance(Api21Impl.obtain((AccessibilityWindowInfo) r1.mInfo));
    }

    private static String typeToString(int r1) {
        switch(r1) {
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            case 4: goto L5;
            default: goto L3;
        };
    L3:
        return "<UNKNOWN>";
    L5:
        return "TYPE_ACCESSIBILITY_OVERLAY";
    L7:
        return "TYPE_SYSTEM";
    L9:
        return "TYPE_INPUT_METHOD";
    L11:
        return "TYPE_APPLICATION";
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object r1) {
        if (r1 != null) goto L4;
        return null;
    L4:
        return new AccessibilityWindowInfoCompat(r1);
    }

    public boolean equals(Object r5) {
        if (this != r5) goto L6;
        return true;
    L6:
        if (r5 != null) goto L9;
        return false;
    L9:
        if ((r5 instanceof AccessibilityWindowInfoCompat) == true) goto L11;
        return false;
    L11:
        AccessibilityWindowInfoCompat r2 = (AccessibilityWindowInfoCompat) r5;
        if (this.mInfo != null) goto L19;
        if (r2.mInfo != null) goto L17;
        return true;
    L17:
        return false;
    L19:
        return this.mInfo.equals(r2.mInfo);
    }

    public AccessibilityNodeInfoCompat getAnchor() {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return null;
    L5:
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(Api24Impl.getAnchor((AccessibilityWindowInfo) this.mInfo));
    }

    public void getBoundsInScreen(Rect r2) {
        Api21Impl.getBoundsInScreen((AccessibilityWindowInfo) this.mInfo, r2);
    }

    public AccessibilityWindowInfoCompat getChild(int r2) {
        return wrapNonNullInstance(Api21Impl.getChild((AccessibilityWindowInfo) this.mInfo, r2));
    }

    public int getChildCount() {
        return Api21Impl.getChildCount((AccessibilityWindowInfo) this.mInfo);
    }

    public int getDisplayId() {
        if (Build.VERSION.SDK_INT >= 33) goto L5;
        return 0;
    L5:
        return Api33Impl.getDisplayId((AccessibilityWindowInfo) this.mInfo);
    }

    public int getId() {
        return Api21Impl.getId((AccessibilityWindowInfo) this.mInfo);
    }

    public int getLayer() {
        return Api21Impl.getLayer((AccessibilityWindowInfo) this.mInfo);
    }

    public AccessibilityWindowInfoCompat getParent() {
        return wrapNonNullInstance(Api21Impl.getParent((AccessibilityWindowInfo) this.mInfo));
    }

    public void getRegionInScreen(Region r3) {
        if (Build.VERSION.SDK_INT < 33) goto L5;
        Api33Impl.getRegionInScreen((AccessibilityWindowInfo) this.mInfo, r3);
        return;
    L5:
        Rect r0 = new Rect();
        Api21Impl.getBoundsInScreen((AccessibilityWindowInfo) this.mInfo, r0);
        r3.set(r0);
    }

    public AccessibilityNodeInfoCompat getRoot() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(Api21Impl.getRoot((AccessibilityWindowInfo) this.mInfo));
    }

    public CharSequence getTitle() {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return null;
    L5:
        return Api24Impl.getTitle((AccessibilityWindowInfo) this.mInfo);
    }

    public int getType() {
        return Api21Impl.getType((AccessibilityWindowInfo) this.mInfo);
    }

    public int hashCode() {
        if (this.mInfo != null) goto L6;
        return 0;
    L6:
        return this.mInfo.hashCode();
    }

    public boolean isAccessibilityFocused() {
        return Api21Impl.isAccessibilityFocused((AccessibilityWindowInfo) this.mInfo);
    }

    public boolean isActive() {
        return Api21Impl.isActive((AccessibilityWindowInfo) this.mInfo);
    }

    public boolean isFocused() {
        return Api21Impl.isFocused((AccessibilityWindowInfo) this.mInfo);
    }

    public boolean isInPictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= 33) goto L5;
        return false;
    L5:
        return Api33Impl.isInPictureInPictureMode((AccessibilityWindowInfo) this.mInfo);
    }

    public void recycle() {
        Api21Impl.recycle((AccessibilityWindowInfo) this.mInfo);
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        Rect r1 = new Rect();
        getBoundsInScreen(r1);
        r0.append("AccessibilityWindowInfo[");
        r0.append("id=").append(getId());
        r0.append(", type=").append(typeToString(getType()));
        r0.append(", layer=").append(getLayer());
        r0.append(", bounds=").append(r1);
        r0.append(", focused=").append(isFocused());
        r0.append(", active=").append(isActive());
        StringBuilder r2 = r0.append(", hasParent=");
        boolean r4 = true;
        if (getParent() == null) goto L5;
        boolean r3 = true;
    L6:
        r2.append(r3);
        StringBuilder r5 = r0.append(", hasChildren=");
        if (getChildCount() > 0) goto L10;
        r4 = false;
    L10:
        r5.append(r4);
        r0.append(']');
        return r0.toString();
    L5:
        r3 = false;
        goto L6
    }

    public AccessibilityWindowInfo unwrap() {
        return (AccessibilityWindowInfo) this.mInfo;
    }
}
