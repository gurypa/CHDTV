package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = "android.view.View";
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = null;
    private static final FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = null;
    private static final FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = null;
    int mAccessibilityFocusedVirtualViewId;
    private final View mHost;
    private int mHoveredVirtualViewId;
    int mKeyboardFocusedVirtualViewId;
    private final AccessibilityManager mManager;
    private MyNodeProvider mNodeProvider;
    private final int[] mTempGlobalRect;
    private final Rect mTempParentRect;
    private final Rect mTempScreenRect;
    private final Rect mTempVisibleRect;

    private class MyNodeProvider extends AccessibilityNodeProviderCompat {
        final /* synthetic */ ExploreByTouchHelper this$0;

        MyNodeProvider(ExploreByTouchHelper r1) {
            this.this$0 = r1;
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int r3) {
            return AccessibilityNodeInfoCompat.obtain(this.this$0.obtainAccessibilityNodeInfo(r3));
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public AccessibilityNodeInfoCompat findFocus(int r3) {
            if (r3 != 2) goto L5;
            int r0 = this.this$0.mAccessibilityFocusedVirtualViewId;
        L7:
            if (r0 != Integer.MIN_VALUE) goto L11;
            return null;
        L11:
            return createAccessibilityNodeInfo(r0);
        L5:
            r0 = this.this$0.mKeyboardFocusedVirtualViewId;
            goto L7
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public boolean performAction(int r2, int r3, Bundle r4) {
            return this.this$0.performAction(r2, r3, r4);
        }
    }

    static {
        INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        NODE_ADAPTER = new C01421();
        SPARSE_VALUES_ADAPTER = new C01432();
    }

    public ExploreByTouchHelper(View r4) {
        this.mTempScreenRect = new Rect();
        this.mTempParentRect = new Rect();
        this.mTempVisibleRect = new Rect();
        this.mTempGlobalRect = new int[2];
        this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mHoveredVirtualViewId = Integer.MIN_VALUE;
        if (r4 == null) goto L9;
        this.mHost = r4;
        this.mManager = (AccessibilityManager) r4.getContext().getSystemService("accessibility");
        r4.setFocusable(true);
        if (ViewCompat.getImportantForAccessibility(r4) != 0) goto L10;
        ViewCompat.setImportantForAccessibility(r4, 1);
        return;
    L10:
        return;
    L9:
        throw new IllegalArgumentException("View may not be null");
    }

    private boolean clearAccessibilityFocus(int r2) {
        if (this.mAccessibilityFocusedVirtualViewId != r2) goto L6;
        this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mHost.invalidate();
        sendEventForVirtualView(r2, 65536);
        return true;
    L6:
        return false;
    }

    private boolean clickKeyboardFocusedVirtualView() {
        if (this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) goto L5;
    L7:
        return false;
    L5:
        if (onPerformActionForVirtualView(this.mKeyboardFocusedVirtualViewId, 16, null) == false) goto L7;
        return true;
    }

    private AccessibilityEvent createEvent(int r2, int r3) {
        switch(r2) {
            case -1: goto L6;
            default: goto L4;
        };
    L4:
        return createEventForChild(r2, r3);
    L6:
        return createEventForHost(r3);
    }

    private AccessibilityEvent createEventForChild(int r5, int r6) {
        AccessibilityEvent r0 = AccessibilityEvent.obtain(r6);
        AccessibilityNodeInfoCompat r1 = obtainAccessibilityNodeInfo(r5);
        r0.getText().add(r1.getText());
        r0.setContentDescription(r1.getContentDescription());
        r0.setScrollable(r1.isScrollable());
        r0.setPassword(r1.isPassword());
        r0.setEnabled(r1.isEnabled());
        r0.setChecked(r1.isChecked());
        onPopulateEventForVirtualView(r5, r0);
        if (r0.getText().isEmpty() == true) goto L5;
    L9:
        r0.setClassName(r1.getClassName());
        AccessibilityRecordCompat.setSource(r0, this.mHost, r5);
        r0.setPackageName(this.mHost.getContext().getPackageName());
        return r0;
    L5:
        if (r0.getContentDescription() != null) goto L9;
        throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
    }

    private AccessibilityEvent createEventForHost(int r3) {
        AccessibilityEvent r0 = AccessibilityEvent.obtain(r3);
        this.mHost.onInitializeAccessibilityEvent(r0);
        return r0;
    }

    private AccessibilityNodeInfoCompat createNodeForChild(int r12) {
        AccessibilityNodeInfoCompat r0 = AccessibilityNodeInfoCompat.obtain();
        r0.setEnabled(true);
        r0.setFocusable(true);
        r0.setClassName(DEFAULT_CLASS_NAME);
        r0.setBoundsInParent(INVALID_PARENT_BOUNDS);
        r0.setBoundsInScreen(INVALID_PARENT_BOUNDS);
        r0.setParent(this.mHost);
        onPopulateNodeForVirtualView(r12, r0);
        if (r0.getText() == null) goto L5;
    L9:
        r0.getBoundsInParent(this.mTempParentRect);
        if (this.mTempParentRect.equals(INVALID_PARENT_BOUNDS) == true) goto L50;
        int r2 = r0.getActions();
        if ((r2 & 64) != 0) goto L48;
        if ((r2 & 128) != 0) goto L46;
        r0.setPackageName(this.mHost.getContext().getPackageName());
        r0.setSource(this.mHost, r12);
        if (this.mAccessibilityFocusedVirtualViewId != r12) goto L18;
        r0.setAccessibilityFocused(true);
        r0.addAction(128);
    L20:
        if (this.mKeyboardFocusedVirtualViewId != r12) goto L22;
        boolean r3 = true;
    L23:
        if (r3 == false) goto L26;
        r0.addAction(2);
    L28:
        r0.setFocused(r3);
        this.mHost.getLocationOnScreen(this.mTempGlobalRect);
        r0.getBoundsInScreen(this.mTempScreenRect);
        if (this.mTempScreenRect.equals(INVALID_PARENT_BOUNDS) == false) goto L38;
        r0.getBoundsInParent(this.mTempScreenRect);
        if (r0.mParentVirtualDescendantId == (-1)) goto L36;
        AccessibilityNodeInfoCompat r5 = AccessibilityNodeInfoCompat.obtain();
        int r7 = r0.mParentVirtualDescendantId;
    L33:
        if (r7 == (-1)) goto L35;
        r5.setParent(this.mHost, -1);
        r5.setBoundsInParent(INVALID_PARENT_BOUNDS);
        onPopulateNodeForVirtualView(r7, r5);
        r5.getBoundsInParent(this.mTempParentRect);
        this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
        r7 = r5.mParentVirtualDescendantId;
        goto L33
    L35:
        r5.recycle();
    L36:
        this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
    L38:
        if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect) == false) goto L44;
        this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
        if (this.mTempScreenRect.intersect(this.mTempVisibleRect) == false) goto L44;
        r0.setBoundsInScreen(this.mTempScreenRect);
        if (isVisibleToUser(this.mTempScreenRect) == false) goto L44;
        r0.setVisibleToUser(true);
    L44:
        return r0;
    L26:
        if (r0.isFocusable() == false) goto L28;
        r0.addAction(1);
        goto L28
    L22:
        r3 = false;
        goto L23
    L18:
        r0.setAccessibilityFocused(false);
        r0.addAction(64);
        goto L20
    L46:
        throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
    L48:
        throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
    L50:
        throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
    L5:
        if (r0.getContentDescription() != null) goto L9;
        throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
    }

    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat r0 = AccessibilityNodeInfoCompat.obtain(this.mHost);
        ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, r0);
        ArrayList r1 = new ArrayList();
        getVisibleVirtualViews(r1);
        if (r0.getChildCount() > 0) goto L5;
    L9:
        int r3 = 0;
        int r4 = r1.size();
    L10:
        if (r3 >= r4) goto L12;
        r0.addChild(this.mHost, ((Integer) r1.get(r3)).intValue());
        r3 = r3 + 1;
        goto L10
    L12:
        return r0;
    L5:
        if (r1.size() <= 0) goto L9;
        throw new RuntimeException("Views cannot have both real and virtual children");
    }

    private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
        ArrayList r0 = new ArrayList();
        getVisibleVirtualViews(r0);
        SparseArrayCompat<AccessibilityNodeInfoCompat> r1 = new SparseArrayCompat();
        int r2 = 0;
    L4:
        if (r2 >= r0.size()) goto L6;
        r1.put(r2, createNodeForChild(r2));
        r2 = r2 + 1;
        goto L4
    L6:
        return r1;
    }

    private void getBoundsInParent(int r2, Rect r3) {
        obtainAccessibilityNodeInfo(r2).getBoundsInParent(r3);
    }

    private static Rect guessPreviouslyFocusedRect(View r4, int r5, Rect r6) {
        int r0 = r4.getWidth();
        int r1 = r4.getHeight();
        switch(r5) {
            case 17: goto L9;
            case 33: goto L8;
            case 66: goto L7;
            case 130: goto L6;
            default: goto L5;
        };
    L6:
        r6.set(0, -1, r0, -1);
    L10:
        return r6;
    L7:
        r6.set(-1, 0, -1, r1);
        goto L10
    L8:
        r6.set(0, r1, r0, r1);
        goto L10
    L9:
        r6.set(r0, 0, r0, r1);
        goto L10
    L5:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    private boolean isVisibleToUser(Rect r6) {
        if (r6 != null) goto L5;
    L23:
        return false;
    L5:
        if (r6.isEmpty() == true) goto L23;
        if (this.mHost.getWindowVisibility() == 0) goto L10;
        return false;
    L10:
        ViewParent r1 = this.mHost.getParent();
    L12:
        if ((r1 instanceof View) == false) goto L20;
        View r2 = (View) r1;
        if (r2.getAlpha() <= 0.0f) goto L19;
        if (r2.getVisibility() != 0) goto L19;
        r1 = r2.getParent();
    L19:
        return false;
    L20:
        if (r1 == null) goto L27;
        return true;
    L27:
        return false;
    }

    private static int keyToDirection(int r1) {
        switch(r1) {
            case 19: goto L9;
            case 20: goto L3;
            case 21: goto L7;
            case 22: goto L5;
            default: goto L3;
        };
    L3:
        return TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
    L5:
        return 66;
    L7:
        return 17;
    L9:
        return 33;
    }

    private boolean moveFocus(int r9, Rect r10) {
        SparseArrayCompat<AccessibilityNodeInfoCompat> r0 = getAllNodes();
        int r7 = this.mKeyboardFocusedVirtualViewId;
        if (r7 != Integer.MIN_VALUE) goto L5;
        AccessibilityNodeInfoCompat r3 = null;
    L7:
        switch(r9) {
            case 1: goto L17;
            case 2: goto L17;
            case 17: goto L10;
            case 33: goto L10;
            case 66: goto L10;
            case 130: goto L10;
            default: goto L9;
        };
    L10:
        Rect r4 = new Rect();
        if (this.mKeyboardFocusedVirtualViewId == Integer.MIN_VALUE) goto L13;
        getBoundsInParent(this.mKeyboardFocusedVirtualViewId, r4);
    L16:
        AccessibilityNodeInfoCompat r11 = (AccessibilityNodeInfoCompat) FocusStrategy.findNextFocusInAbsoluteDirection(r0, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, r3, r4, r9);
    L22:
        if (r11 != null) goto L24;
        int r1 = Integer.MIN_VALUE;
    L26:
        return requestKeyboardFocusForVirtualView(r1);
    L24:
        r1 = r0.keyAt(r0.indexOfValue(r11));
        goto L26
    L13:
        if (r10 == null) goto L15;
        r4.set(r10);
        goto L16
    L15:
        guessPreviouslyFocusedRect(this.mHost, r9, r4);
        goto L16
    L17:
        boolean r2 = true;
        if (ViewCompat.getLayoutDirection(this.mHost) == 1) goto L21;
        r2 = false;
    L21:
        r11 = (AccessibilityNodeInfoCompat) FocusStrategy.findNextFocusInRelativeDirection(r0, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, r3, r9, r2, false);
        goto L22
    L9:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L5:
        r3 = r0.get(r7);
        goto L7
    }

    private boolean performActionForChild(int r2, int r3, Bundle r4) {
        switch(r3) {
            case 1: goto L12;
            case 2: goto L10;
            case 64: goto L8;
            case 128: goto L6;
            default: goto L4;
        };
    L4:
        return onPerformActionForVirtualView(r2, r3, r4);
    L6:
        return clearAccessibilityFocus(r2);
    L8:
        return requestAccessibilityFocus(r2);
    L10:
        return clearKeyboardFocusForVirtualView(r2);
    L12:
        return requestKeyboardFocusForVirtualView(r2);
    }

    private boolean performActionForHost(int r2, Bundle r3) {
        return ViewCompat.performAccessibilityAction(this.mHost, r2, r3);
    }

    private boolean requestAccessibilityFocus(int r3) {
        if (this.mManager.isEnabled() == true) goto L5;
    L15:
        return false;
    L5:
        if (this.mManager.isTouchExplorationEnabled() == false) goto L15;
        if (this.mAccessibilityFocusedVirtualViewId != r3) goto L10;
        return false;
    L10:
        if (this.mAccessibilityFocusedVirtualViewId == Integer.MIN_VALUE) goto L12;
        clearAccessibilityFocus(this.mAccessibilityFocusedVirtualViewId);
    L12:
        this.mAccessibilityFocusedVirtualViewId = r3;
        this.mHost.invalidate();
        sendEventForVirtualView(r3, 32768);
        return true;
    }

    private void updateHoveredVirtualView(int r3) {
        if (this.mHoveredVirtualViewId != r3) goto L5;
        return;
    L5:
        int r0 = this.mHoveredVirtualViewId;
        this.mHoveredVirtualViewId = r3;
        sendEventForVirtualView(r3, 128);
        sendEventForVirtualView(r0, 256);
    }

    public final boolean clearKeyboardFocusForVirtualView(int r3) {
        if (this.mKeyboardFocusedVirtualViewId == r3) goto L5;
        return false;
    L5:
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        onVirtualViewKeyboardFocusChanged(r3, false);
        sendEventForVirtualView(r3, 8);
        return true;
    }

    public final boolean dispatchHoverEvent(MotionEvent r6) {
        if (this.mManager.isEnabled() == true) goto L5;
    L19:
        return false;
    L5:
        if (this.mManager.isTouchExplorationEnabled() == false) goto L19;
        switch(r6.getAction()) {
            case 7: goto L15;
            case 8: goto L9;
            case 9: goto L15;
            case 10: goto L11;
            default: goto L9;
        };
    L9:
        return false;
    L15:
        int r0 = getVirtualViewAt(r6.getX(), r6.getY());
        updateHoveredVirtualView(r0);
        if (r0 != Integer.MIN_VALUE) goto L18;
        return false;
    L18:
        return true;
    L11:
        if (this.mHoveredVirtualViewId == Integer.MIN_VALUE) goto L14;
        updateHoveredVirtualView(Integer.MIN_VALUE);
        return true;
    L14:
        return false;
    }

    public final boolean dispatchKeyEvent(KeyEvent r9) {
        boolean r0 = false;
        if (r9.getAction() == 1) goto L26;
        int r3 = r9.getKeyCode();
        switch(r3) {
            case 19: goto L19;
            case 20: goto L19;
            case 21: goto L19;
            case 22: goto L19;
            case 23: goto L14;
            case 61: goto L8;
            case 66: goto L14;
            default: goto L36;
        };
    L36:
        return false;
    L8:
        if (r9.hasNoModifiers() == false) goto L11;
        return moveFocus(2, null);
    L11:
        if (r9.hasModifiers(1) == true) goto L31;
        return false;
    L31:
        return moveFocus(1, null);
    L14:
        if (r9.hasNoModifiers() == true) goto L16;
        return false;
    L16:
        if (r9.getRepeatCount() != 0) goto L33;
        clickKeyboardFocusedVirtualView();
        return true;
    L33:
        return false;
    L19:
        if (r9.hasNoModifiers() == false) goto L35;
        int r5 = keyToDirection(r3);
        int r6 = r9.getRepeatCount() + 1;
        int r2 = 0;
    L21:
        if (r2 >= r6) goto L37;
        if (moveFocus(r5, null) == false) goto L38;
        r0 = true;
        r2 = r2 + 1;
        goto L21
    L38:
        return r0;
    L37:
        return r0;
    L35:
        return false;
    L26:
        return false;
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        return this.mAccessibilityFocusedVirtualViewId;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View r2) {
        if (this.mNodeProvider != null) goto L6;
        this.mNodeProvider = new MyNodeProvider(this);
    L6:
        return this.mNodeProvider;
    }

    @Deprecated
    public int getFocusedVirtualView() {
        return getAccessibilityFocusedVirtualViewId();
    }

    public final int getKeyboardFocusedVirtualViewId() {
        return this.mKeyboardFocusedVirtualViewId;
    }

    protected abstract int getVirtualViewAt(float r1, float r2);

    protected abstract void getVisibleVirtualViews(List<Integer> r1);

    public final void invalidateRoot() {
        invalidateVirtualView(-1, 1);
    }

    public final void invalidateVirtualView(int r2) {
        invalidateVirtualView(r2, 0);
    }

    public final void invalidateVirtualView(int r4, int r5) {
        if (r4 != Integer.MIN_VALUE) goto L5;
        return;
    L5:
        if (this.mManager.isEnabled() == false) goto L11;
        ViewParent r0 = this.mHost.getParent();
        if (r0 == null) goto L12;
        AccessibilityEvent r1 = createEvent(r4, 2048);
        AccessibilityEventCompat.setContentChangeTypes(r1, r5);
        ViewParentCompat.requestSendAccessibilityEvent(r0, this.mHost, r1);
        return;
    L12:
        return;
    }

    AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int r2) {
        if (r2 != (-1)) goto L7;
        return createNodeForHost();
    L7:
        return createNodeForChild(r2);
    }

    public final void onFocusChanged(boolean r3, int r4, Rect r5) {
        if (this.mKeyboardFocusedVirtualViewId == Integer.MIN_VALUE) goto L5;
        clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
    L5:
        if (r3 == false) goto L8;
        moveFocus(r4, r5);
        return;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityEvent(View r1, AccessibilityEvent r2) {
        super.onInitializeAccessibilityEvent(r1, r2);
        onPopulateEventForHost(r2);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View r1, AccessibilityNodeInfoCompat r2) {
        super.onInitializeAccessibilityNodeInfo(r1, r2);
        onPopulateNodeForHost(r2);
    }

    protected abstract boolean onPerformActionForVirtualView(int r1, int r2, Bundle r3);

    protected void onPopulateEventForHost(AccessibilityEvent r1) {
    }

    protected void onPopulateEventForVirtualView(int r1, AccessibilityEvent r2) {
    }

    protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat r1) {
    }

    protected abstract void onPopulateNodeForVirtualView(int r1, AccessibilityNodeInfoCompat r2);

    protected void onVirtualViewKeyboardFocusChanged(int r1, boolean r2) {
    }

    boolean performAction(int r2, int r3, Bundle r4) {
        switch(r2) {
            case -1: goto L6;
            default: goto L4;
        };
    L4:
        return performActionForChild(r2, r3, r4);
    L6:
        return performActionForHost(r3, r4);
    }

    public final boolean requestKeyboardFocusForVirtualView(int r3) {
        if (this.mHost.isFocused() == true) goto L8;
        if (this.mHost.requestFocus() == true) goto L8;
        return false;
    L8:
        if (this.mKeyboardFocusedVirtualViewId != r3) goto L11;
        return false;
    L11:
        if (this.mKeyboardFocusedVirtualViewId == Integer.MIN_VALUE) goto L13;
        clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
    L13:
        this.mKeyboardFocusedVirtualViewId = r3;
        onVirtualViewKeyboardFocusChanged(r3, true);
        sendEventForVirtualView(r3, 8);
        return true;
    }

    public final boolean sendEventForVirtualView(int r4, int r5) {
        if (r4 != Integer.MIN_VALUE) goto L5;
    L12:
        return false;
    L5:
        if (this.mManager.isEnabled() == false) goto L12;
        ViewParent r0 = this.mHost.getParent();
        if (r0 != null) goto L10;
        return false;
    L10:
        AccessibilityEvent r1 = createEvent(r4, r5);
        return ViewParentCompat.requestSendAccessibilityEvent(r0, this.mHost, r1);
    }
}
