package androidx.core.view;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.C0039R;
import androidx.core.view.accessibility.AccessibilityClickableSpanCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityDelegateCompat {
    private static final View.AccessibilityDelegate DEFAULT_DELEGATE = null;
    private final View.AccessibilityDelegate mBridge;
    private final View.AccessibilityDelegate mOriginalDelegate;

    static final class AccessibilityDelegateAdapter extends View.AccessibilityDelegate {
        final AccessibilityDelegateCompat mCompat;

        AccessibilityDelegateAdapter(AccessibilityDelegateCompat r1) {
            this.mCompat = r1;
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean dispatchPopulateAccessibilityEvent(View r2, AccessibilityEvent r3) {
            return this.mCompat.dispatchPopulateAccessibilityEvent(r2, r3);
        }

        @Override // android.view.View.AccessibilityDelegate
        public AccessibilityNodeProvider getAccessibilityNodeProvider(View r3) {
            AccessibilityNodeProviderCompat r0 = this.mCompat.getAccessibilityNodeProvider(r3);
            if (r0 != null) goto L7;
            return null;
        L7:
            return (AccessibilityNodeProvider) r0.getProvider();
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityEvent(View r2, AccessibilityEvent r3) {
            this.mCompat.onInitializeAccessibilityEvent(r2, r3);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View r5, AccessibilityNodeInfo r6) {
            AccessibilityNodeInfoCompat r0 = AccessibilityNodeInfoCompat.wrap(r6);
            r0.setScreenReaderFocusable(ViewCompat.isScreenReaderFocusable(r5));
            r0.setHeading(ViewCompat.isAccessibilityHeading(r5));
            r0.setPaneTitle(ViewCompat.getAccessibilityPaneTitle(r5));
            r0.setStateDescription(ViewCompat.getStateDescription(r5));
            this.mCompat.onInitializeAccessibilityNodeInfo(r5, r0);
            r0.addSpansToExtras(r6.getText(), r5);
            List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> r1 = AccessibilityDelegateCompat.getActionList(r5);
            int r2 = 0;
        L4:
            if (r2 >= r1.size()) goto L6;
            r0.addAction(r1.get(r2));
            r2 = r2 + 1;
            goto L4
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onPopulateAccessibilityEvent(View r2, AccessibilityEvent r3) {
            this.mCompat.onPopulateAccessibilityEvent(r2, r3);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean onRequestSendAccessibilityEvent(ViewGroup r2, View r3, AccessibilityEvent r4) {
            return this.mCompat.onRequestSendAccessibilityEvent(r2, r3, r4);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View r2, int r3, Bundle r4) {
            return this.mCompat.performAccessibilityAction(r2, r3, r4);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEvent(View r2, int r3) {
            this.mCompat.sendAccessibilityEvent(r2, r3);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEventUnchecked(View r2, AccessibilityEvent r3) {
            this.mCompat.sendAccessibilityEventUnchecked(r2, r3);
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static AccessibilityNodeProvider getAccessibilityNodeProvider(View.AccessibilityDelegate r1, View r2) {
            return r1.getAccessibilityNodeProvider(r2);
        }

        static boolean performAccessibilityAction(View.AccessibilityDelegate r1, View r2, int r3, Bundle r4) {
            return r1.performAccessibilityAction(r2, r3, r4);
        }
    }

    static {
        DEFAULT_DELEGATE = new View.AccessibilityDelegate();
    }

    public AccessibilityDelegateCompat() {
        this(DEFAULT_DELEGATE);
    }

    public AccessibilityDelegateCompat(View.AccessibilityDelegate r2) {
        this.mOriginalDelegate = r2;
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }

    static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View r2) {
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> r0 = (List) r2.getTag(C0039R.id.tag_accessibility_actions);
        if (r0 != null) goto L6;
        return Collections.emptyList();
    L6:
        return r0;
    }

    private boolean isSpanStillValid(ClickableSpan r5, View r6) {
        if (r5 == null) goto L12;
        ClickableSpan[] r1 = AccessibilityNodeInfoCompat.getClickableSpans(r6.createAccessibilityNodeInfo().getText());
        int r2 = 0;
    L4:
        if (r1 == null) goto L17;
        if (r2 >= r1.length) goto L18;
        if (r5.equals(r1[r2]) == true) goto L9;
        r2 = r2 + 1;
        goto L4
    L9:
        return true;
    L18:
        return false;
    L17:
        return false;
    L12:
        return false;
    }

    private boolean performClickableSpanAction(int r5, View r6) {
        SparseArray r0 = (SparseArray) r6.getTag(C0039R.id.tag_accessibility_clickable_spans);
        if (r0 == null) goto L10;
        WeakReference r1 = (WeakReference) r0.get(r5);
        if (r1 == null) goto L12;
        ClickableSpan r2 = (ClickableSpan) r1.get();
        if (isSpanStillValid(r2, r6) == false) goto L13;
        r2.onClick(r6);
        return true;
    L13:
        return false;
    L12:
        return false;
    L10:
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(View r2, AccessibilityEvent r3) {
        return this.mOriginalDelegate.dispatchPopulateAccessibilityEvent(r2, r3);
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View r3) {
        AccessibilityNodeProvider r0 = Api16Impl.getAccessibilityNodeProvider(this.mOriginalDelegate, r3);
        if (r0 != null) goto L5;
        return null;
    L5:
        return new AccessibilityNodeProviderCompat(r0);
    }

    View.AccessibilityDelegate getBridge() {
        return this.mBridge;
    }

    public void onInitializeAccessibilityEvent(View r2, AccessibilityEvent r3) {
        this.mOriginalDelegate.onInitializeAccessibilityEvent(r2, r3);
    }

    public void onInitializeAccessibilityNodeInfo(View r3, AccessibilityNodeInfoCompat r4) {
        this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(r3, r4.unwrap());
    }

    public void onPopulateAccessibilityEvent(View r2, AccessibilityEvent r3) {
        this.mOriginalDelegate.onPopulateAccessibilityEvent(r2, r3);
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup r2, View r3, AccessibilityEvent r4) {
        return this.mOriginalDelegate.onRequestSendAccessibilityEvent(r2, r3, r4);
    }

    public boolean performAccessibilityAction(View r6, int r7, Bundle r8) {
        boolean r0 = false;
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> r1 = getActionList(r6);
        int r2 = 0;
    L4:
        if (r2 >= r1.size()) goto L9;
        AccessibilityNodeInfoCompat.AccessibilityActionCompat r3 = r1.get(r2);
        if (r3.getId() == r7) goto L7;
        r2 = r2 + 1;
        goto L4
    L7:
        r0 = r3.perform(r6, r8);
    L9:
        if (r0 == true) goto L11;
        r0 = Api16Impl.performAccessibilityAction(this.mOriginalDelegate, r6, r7, r8);
    L11:
        if (r0 == false) goto L13;
        return r0;
    L13:
        if (r7 != C0039R.id.accessibility_action_clickable_span) goto L20;
        if (r8 != null) goto L16;
        return r0;
    L16:
        return performClickableSpanAction(r8.getInt(AccessibilityClickableSpanCompat.SPAN_ID, -1), r6);
    L20:
        return r0;
    }

    public void sendAccessibilityEvent(View r2, int r3) {
        this.mOriginalDelegate.sendAccessibilityEvent(r2, r3);
    }

    public void sendAccessibilityEventUnchecked(View r2, AccessibilityEvent r3) {
        this.mOriginalDelegate.sendAccessibilityEventUnchecked(r2, r3);
    }
}
