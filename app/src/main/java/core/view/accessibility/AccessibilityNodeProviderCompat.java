package androidx.core.view.accessibility;

import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityNodeProviderCompat {
    public static final int HOST_VIEW_ID = -1;
    private final Object mProvider;

    static class AccessibilityNodeProviderApi16 extends AccessibilityNodeProvider {
        final AccessibilityNodeProviderCompat mCompat;

        AccessibilityNodeProviderApi16(AccessibilityNodeProviderCompat r1) {
            this.mCompat = r1;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo createAccessibilityNodeInfo(int r3) {
            AccessibilityNodeInfoCompat r0 = this.mCompat.createAccessibilityNodeInfo(r3);
            if (r0 != null) goto L7;
            return null;
        L7:
            return r0.unwrap();
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String r7, int r8) {
            List<AccessibilityNodeInfoCompat> r0 = this.mCompat.findAccessibilityNodeInfosByText(r7, r8);
            if (r0 != null) goto L6;
            return null;
        L6:
            ArrayList r1 = new ArrayList();
            int r2 = r0.size();
            int r3 = 0;
        L7:
            if (r3 >= r2) goto L9;
            r1.add(r0.get(r3).unwrap());
            r3 = r3 + 1;
            goto L7
        L9:
            return r1;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public boolean performAction(int r2, int r3, Bundle r4) {
            return this.mCompat.performAction(r2, r3, r4);
        }
    }

    static class AccessibilityNodeProviderApi19 extends AccessibilityNodeProviderApi16 {
        AccessibilityNodeProviderApi19(AccessibilityNodeProviderCompat r1) {
            super(r1);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo findFocus(int r3) {
            AccessibilityNodeInfoCompat r0 = this.mCompat.findFocus(r3);
            if (r0 != null) goto L7;
            return null;
        L7:
            return r0.unwrap();
        }
    }

    static class AccessibilityNodeProviderApi26 extends AccessibilityNodeProviderApi19 {
        AccessibilityNodeProviderApi26(AccessibilityNodeProviderCompat r1) {
            super(r1);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public void addExtraDataToAccessibilityNodeInfo(int r3, AccessibilityNodeInfo r4, String r5, Bundle r6) {
            this.mCompat.addExtraDataToAccessibilityNodeInfo(r3, AccessibilityNodeInfoCompat.wrap(r4), r5, r6);
        }
    }

    public AccessibilityNodeProviderCompat() {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mProvider = new AccessibilityNodeProviderApi26(this);
        return;
    L5:
        this.mProvider = new AccessibilityNodeProviderApi19(this);
    }

    public AccessibilityNodeProviderCompat(Object r1) {
        this.mProvider = r1;
    }

    public void addExtraDataToAccessibilityNodeInfo(int r1, AccessibilityNodeInfoCompat r2, String r3, Bundle r4) {
    }

    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int r2) {
        return null;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String r2, int r3) {
        return null;
    }

    public AccessibilityNodeInfoCompat findFocus(int r2) {
        return null;
    }

    public Object getProvider() {
        return this.mProvider;
    }

    public boolean performAction(int r2, int r3, Bundle r4) {
        return false;
    }
}
