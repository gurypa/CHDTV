package androidx.core.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;

/* JADX INFO: loaded from: classes.dex */
public final class AccessibilityServiceInfoCompat {
    public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int FEEDBACK_BRAILLE = 32;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REPORT_VIEW_IDS = 16;
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;

    private AccessibilityServiceInfoCompat() {
    }

    public static String capabilityToString(int r1) {
        switch(r1) {
            case 1: goto L11;
            case 2: goto L9;
            case 4: goto L7;
            case 8: goto L5;
            default: goto L3;
        };
    L3:
        return "UNKNOWN";
    L5:
        return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
    L7:
        return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
    L9:
        return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
    L11:
        return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
    }

    public static String feedbackTypeToString(int r4) {
        StringBuilder r0 = new StringBuilder();
        r0.append("[");
    L3:
        if (r4 <= 0) goto L15;
        int r1 = 1 << Integer.numberOfTrailingZeros(r4);
        r4 = r4 & (~r1);
        if (r0.length() <= 1) goto L7;
        r0.append(", ");
    L7:
        switch(r1) {
            case 1: goto L13;
            case 2: goto L12;
            case 4: goto L11;
            case 8: goto L10;
            case 16: goto L9;
            default: goto L3;
        };
    L9:
        r0.append("FEEDBACK_GENERIC");
        goto L3
    L10:
        r0.append("FEEDBACK_VISUAL");
        goto L3
    L11:
        r0.append("FEEDBACK_AUDIBLE");
        goto L3
    L12:
        r0.append("FEEDBACK_HAPTIC");
        goto L3
    L13:
        r0.append("FEEDBACK_SPOKEN");
        goto L3
    L15:
        r0.append("]");
        return r0.toString();
    }

    public static String flagToString(int r1) {
        switch(r1) {
            case 1: goto L15;
            case 2: goto L13;
            case 4: goto L11;
            case 8: goto L9;
            case 16: goto L7;
            case 32: goto L5;
            default: goto L3;
        };
    L3:
        return null;
    L5:
        return "FLAG_REQUEST_FILTER_KEY_EVENTS";
    L7:
        return "FLAG_REPORT_VIEW_IDS";
    L9:
        return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
    L11:
        return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
    L13:
        return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
    L15:
        return "DEFAULT";
    }

    public static int getCapabilities(AccessibilityServiceInfo r1) {
        return r1.getCapabilities();
    }

    public static String loadDescription(AccessibilityServiceInfo r1, PackageManager r2) {
        return r1.loadDescription(r2);
    }
}
