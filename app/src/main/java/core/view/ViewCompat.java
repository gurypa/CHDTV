package androidx.core.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContentInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.collection.SimpleArrayMap;
import androidx.core.C0039R;
import androidx.core.util.Preconditions;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ViewCompat {
    private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS = null;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;

    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;

    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;

    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;

    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;

    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;

    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;

    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    private static final OnReceiveContentViewBehavior NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR = null;

    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;

    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;

    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    public static final int TYPE_NON_TOUCH = 1;
    public static final int TYPE_TOUCH = 0;
    private static boolean sAccessibilityDelegateCheckFailed;
    private static Field sAccessibilityDelegateField;
    private static final AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager = null;
    private static Method sChildrenDrawingOrderMethod;
    private static Method sDispatchFinishTemporaryDetach;
    private static Method sDispatchStartTemporaryDetach;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId = null;
    private static boolean sTempDetachBound;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap;

    static class AccessibilityPaneVisibilityManager implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {
        private final WeakHashMap<View, Boolean> mPanesToVisible;

        AccessibilityPaneVisibilityManager() {
            this.mPanesToVisible = new WeakHashMap();
        }

        private void checkPaneVisibility(View r5, boolean r6) {
            if (r5.isShown() == true) goto L5;
        L7:
            boolean r0 = false;
        L8:
            if (r6 == r0) goto L14;
            if (r0 == false) goto L11;
            int r1 = 16;
        L12:
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(r5, r1);
            this.mPanesToVisible.put(r5, Boolean.valueOf(r0));
            return;
        L11:
            r1 = 32;
            goto L12
        L14:
            return;
        L5:
            if (r5.getWindowVisibility() != 0) goto L7;
            r0 = true;
            goto L8
        }

        private void registerForLayoutCallback(View r2) {
            r2.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        private void unregisterForLayoutCallback(View r2) {
            Api16Impl.removeOnGlobalLayoutListener(r2.getViewTreeObserver(), this);
        }

        void addAccessibilityPane(View r3) {
            WeakHashMap<View, Boolean> r0 = this.mPanesToVisible;
            if (r3.isShown() == true) goto L5;
        L7:
            boolean r1 = false;
        L8:
            r0.put(r3, Boolean.valueOf(r1));
            r3.addOnAttachStateChangeListener(this);
            if (Api19Impl.isAttachedToWindow(r3) == false) goto L12;
            registerForLayoutCallback(r3);
            return;
        L12:
            return;
        L5:
            if (r3.getWindowVisibility() != 0) goto L7;
            r1 = true;
            goto L8
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= 28) goto L8;
            Iterator<Map.Entry<View, Boolean>> r0 = this.mPanesToVisible.entrySet().iterator();
        L6:
            if (r0.hasNext() == false) goto L10;
            Map.Entry<View, Boolean> r1 = r0.next();
            checkPaneVisibility(r1.getKey(), r1.getValue().booleanValue());
            goto L6
        L10:
            return;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View r1) {
            registerForLayoutCallback(r1);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View r1) {
        }

        void removeAccessibilityPane(View r2) {
            this.mPanesToVisible.remove(r2);
            r2.removeOnAttachStateChangeListener(this);
            unregisterForLayoutCallback(r2);
        }
    }

    static abstract class AccessibilityViewProperty<T> {
        private final int mContentChangeType;
        private final int mFrameworkMinimumSdk;
        private final int mTagKey;
        private final Class<T> mType;

        AccessibilityViewProperty(int r2, Class<T> r3, int r4) {
            this(r2, r3, 0, r4);
        }

        AccessibilityViewProperty(int r1, Class<T> r2, int r3, int r4) {
            this.mTagKey = r1;
            this.mType = r2;
            this.mContentChangeType = r3;
            this.mFrameworkMinimumSdk = r4;
        }

        private boolean extrasAvailable() {
            return true;
        }

        private boolean frameworkAvailable() {
            if (Build.VERSION.SDK_INT < this.mFrameworkMinimumSdk) goto L5;
            return true;
        L5:
            return false;
        }

        boolean booleanNullToFalseEquals(Boolean r5, Boolean r6) {
            if (r5 != null) goto L5;
        L7:
            boolean r2 = false;
        L8:
            if (r6 != null) goto L10;
        L12:
            boolean r3 = false;
        L13:
            if (r2 != r3) goto L16;
            return true;
        L16:
            return false;
        L10:
            if (r6.booleanValue() == false) goto L12;
            r3 = true;
            goto L13
        L5:
            if (r5.booleanValue() == false) goto L7;
            r2 = true;
            goto L8
        }

        abstract T frameworkGet(View r1);

        abstract void frameworkSet(View r1, T r2);

        T get(View r3) {
            if (frameworkAvailable() == false) goto L7;
            return frameworkGet(r3);
        L7:
            if (extrasAvailable() == false) goto L11;
            T r0 = (T) r3.getTag(this.mTagKey);
            if (this.mType.isInstance(r0) == false) goto L13;
            return r0;
        L13:
            return null;
        L11:
            return null;
        }

        void set(View r2, T r3) {
            if (frameworkAvailable() == false) goto L6;
            frameworkSet(r2, r3);
            return;
        L6:
            if (extrasAvailable() == true) goto L8;
            return;
        L8:
            if (shouldUpdate(get(r2), r3) == false) goto L13;
            ViewCompat.ensureAccessibilityDelegateCompat(r2);
            r2.setTag(this.mTagKey, r3);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(r2, this.mContentChangeType);
            return;
        }

        boolean shouldUpdate(T r2, T r3) {
            return !r3.equals(r2);
        }
    }

    static class Api15Impl {
        private Api15Impl() {
        }

        static boolean hasOnClickListeners(View r1) {
            return r1.hasOnClickListeners();
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static AccessibilityNodeProvider getAccessibilityNodeProvider(View r1) {
            return r1.getAccessibilityNodeProvider();
        }

        static boolean getFitsSystemWindows(View r1) {
            return r1.getFitsSystemWindows();
        }

        static int getImportantForAccessibility(View r1) {
            return r1.getImportantForAccessibility();
        }

        static int getMinimumHeight(View r1) {
            return r1.getMinimumHeight();
        }

        static int getMinimumWidth(View r1) {
            return r1.getMinimumWidth();
        }

        static ViewParent getParentForAccessibility(View r1) {
            return r1.getParentForAccessibility();
        }

        static int getWindowSystemUiVisibility(View r1) {
            return r1.getWindowSystemUiVisibility();
        }

        static boolean hasOverlappingRendering(View r1) {
            return r1.hasOverlappingRendering();
        }

        static boolean hasTransientState(View r1) {
            return r1.hasTransientState();
        }

        static boolean performAccessibilityAction(View r1, int r2, Bundle r3) {
            return r1.performAccessibilityAction(r2, r3);
        }

        static void postInvalidateOnAnimation(View r0) {
            r0.postInvalidateOnAnimation();
        }

        static void postInvalidateOnAnimation(View r0, int r1, int r2, int r3, int r4) {
            r0.postInvalidateOnAnimation(r1, r2, r3, r4);
        }

        static void postOnAnimation(View r0, Runnable r1) {
            r0.postOnAnimation(r1);
        }

        static void postOnAnimationDelayed(View r0, Runnable r1, long r2) {
            r0.postOnAnimationDelayed(r1, r2);
        }

        static void removeOnGlobalLayoutListener(ViewTreeObserver r0, ViewTreeObserver.OnGlobalLayoutListener r1) {
            r0.removeOnGlobalLayoutListener(r1);
        }

        static void requestFitSystemWindows(View r0) {
            r0.requestFitSystemWindows();
        }

        static void setBackground(View r0, Drawable r1) {
            r0.setBackground(r1);
        }

        static void setHasTransientState(View r0, boolean r1) {
            r0.setHasTransientState(r1);
        }

        static void setImportantForAccessibility(View r0, int r1) {
            r0.setImportantForAccessibility(r1);
        }
    }

    static class Api17Impl {
        private Api17Impl() {
        }

        static int generateViewId() {
            return View.generateViewId();
        }

        static Display getDisplay(View r1) {
            return r1.getDisplay();
        }

        static int getLabelFor(View r1) {
            return r1.getLabelFor();
        }

        static int getLayoutDirection(View r1) {
            return r1.getLayoutDirection();
        }

        static int getPaddingEnd(View r1) {
            return r1.getPaddingEnd();
        }

        static int getPaddingStart(View r1) {
            return r1.getPaddingStart();
        }

        static boolean isPaddingRelative(View r1) {
            return r1.isPaddingRelative();
        }

        static void setLabelFor(View r0, int r1) {
            r0.setLabelFor(r1);
        }

        static void setLayerPaint(View r0, Paint r1) {
            r0.setLayerPaint(r1);
        }

        static void setLayoutDirection(View r0, int r1) {
            r0.setLayoutDirection(r1);
        }

        static void setPaddingRelative(View r0, int r1, int r2, int r3, int r4) {
            r0.setPaddingRelative(r1, r2, r3, r4);
        }
    }

    static class Api18Impl {
        private Api18Impl() {
        }

        static Rect getClipBounds(View r1) {
            return r1.getClipBounds();
        }

        static boolean isInLayout(View r1) {
            return r1.isInLayout();
        }

        static void setClipBounds(View r0, Rect r1) {
            r0.setClipBounds(r1);
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static int getAccessibilityLiveRegion(View r1) {
            return r1.getAccessibilityLiveRegion();
        }

        static boolean isAttachedToWindow(View r1) {
            return r1.isAttachedToWindow();
        }

        static boolean isLaidOut(View r1) {
            return r1.isLaidOut();
        }

        static boolean isLayoutDirectionResolved(View r1) {
            return r1.isLayoutDirectionResolved();
        }

        static void notifySubtreeAccessibilityStateChanged(ViewParent r0, View r1, View r2, int r3) {
            r0.notifySubtreeAccessibilityStateChanged(r1, r2, r3);
        }

        static void setAccessibilityLiveRegion(View r0, int r1) {
            r0.setAccessibilityLiveRegion(r1);
        }

        static void setContentChangeTypes(AccessibilityEvent r0, int r1) {
            r0.setContentChangeTypes(r1);
        }
    }

    static class Api20Impl {
        private Api20Impl() {
        }

        static WindowInsets dispatchApplyWindowInsets(View r1, WindowInsets r2) {
            return r1.dispatchApplyWindowInsets(r2);
        }

        static WindowInsets onApplyWindowInsets(View r1, WindowInsets r2) {
            return r1.onApplyWindowInsets(r2);
        }

        static void requestApplyInsets(View r0) {
            r0.requestApplyInsets();
        }
    }

    private static class Api21Impl {
        private Api21Impl() {
        }

        static void callCompatInsetAnimationCallback(WindowInsets r1, View r2) {
            View.OnApplyWindowInsetsListener r0 = (View.OnApplyWindowInsetsListener) r2.getTag(C0039R.id.tag_window_insets_animation_callback);
            if (r0 == null) goto L6;
            r0.onApplyWindowInsets(r2, r1);
            return;
        }

        static WindowInsetsCompat computeSystemWindowInsets(View r2, WindowInsetsCompat r3, Rect r4) {
            WindowInsets r0 = r3.toWindowInsets();
            if (r0 != null) goto L5;
            r4.setEmpty();
            return r3;
        L5:
            return WindowInsetsCompat.toWindowInsetsCompat(r2.computeSystemWindowInsets(r0, r4), r2);
        }

        static boolean dispatchNestedFling(View r1, float r2, float r3, boolean r4) {
            return r1.dispatchNestedFling(r2, r3, r4);
        }

        static boolean dispatchNestedPreFling(View r1, float r2, float r3) {
            return r1.dispatchNestedPreFling(r2, r3);
        }

        static boolean dispatchNestedPreScroll(View r1, int r2, int r3, int[] r4, int[] r5) {
            return r1.dispatchNestedPreScroll(r2, r3, r4, r5);
        }

        static boolean dispatchNestedScroll(View r1, int r2, int r3, int r4, int r5, int[] r6) {
            return r1.dispatchNestedScroll(r2, r3, r4, r5, r6);
        }

        static ColorStateList getBackgroundTintList(View r1) {
            return r1.getBackgroundTintList();
        }

        static PorterDuff.Mode getBackgroundTintMode(View r1) {
            return r1.getBackgroundTintMode();
        }

        static float getElevation(View r1) {
            return r1.getElevation();
        }

        public static WindowInsetsCompat getRootWindowInsets(View r1) {
            return WindowInsetsCompat.Api21ReflectionHolder.getRootWindowInsets(r1);
        }

        static String getTransitionName(View r1) {
            return r1.getTransitionName();
        }

        static float getTranslationZ(View r1) {
            return r1.getTranslationZ();
        }

        static float getZ(View r1) {
            return r1.getZ();
        }

        static boolean hasNestedScrollingParent(View r1) {
            return r1.hasNestedScrollingParent();
        }

        static boolean isImportantForAccessibility(View r1) {
            return r1.isImportantForAccessibility();
        }

        static boolean isNestedScrollingEnabled(View r1) {
            return r1.isNestedScrollingEnabled();
        }

        static void setBackgroundTintList(View r0, ColorStateList r1) {
            r0.setBackgroundTintList(r1);
        }

        static void setBackgroundTintMode(View r0, PorterDuff.Mode r1) {
            r0.setBackgroundTintMode(r1);
        }

        static void setElevation(View r0, float r1) {
            r0.setElevation(r1);
        }

        static void setNestedScrollingEnabled(View r0, boolean r1) {
            r0.setNestedScrollingEnabled(r1);
        }

        static void setOnApplyWindowInsetsListener(final View r2, final OnApplyWindowInsetsListener r3) {
            if (Build.VERSION.SDK_INT >= 30) goto L5;
            r2.setTag(C0039R.id.tag_on_apply_window_listener, r3);
        L5:
            if (r3 != null) goto L8;
            r2.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) r2.getTag(C0039R.id.tag_window_insets_animation_callback));
            return;
        L8:
            r2.setOnApplyWindowInsetsListener(new ViewOnApplyWindowInsetsListenerC01181(r2, r3));
        }

        static void setTransitionName(View r0, String r1) {
            r0.setTransitionName(r1);
        }

        static void setTranslationZ(View r0, float r1) {
            r0.setTranslationZ(r1);
        }

        static void setZ(View r0, float r1) {
            r0.setZ(r1);
        }

        static boolean startNestedScroll(View r1, int r2) {
            return r1.startNestedScroll(r2);
        }

        static void stopNestedScroll(View r0) {
            r0.stopNestedScroll();
        }
    }

    private static class Api23Impl {
        private Api23Impl() {
        }

        public static WindowInsetsCompat getRootWindowInsets(View r3) {
            WindowInsets r0 = r3.getRootWindowInsets();
            if (r0 != null) goto L6;
            return null;
        L6:
            WindowInsetsCompat r1 = WindowInsetsCompat.toWindowInsetsCompat(r0);
            r1.setRootWindowInsets(r1);
            r1.copyRootViewBounds(r3.getRootView());
            return r1;
        }

        static int getScrollIndicators(View r1) {
            return r1.getScrollIndicators();
        }

        static void setScrollIndicators(View r0, int r1) {
            r0.setScrollIndicators(r1);
        }

        static void setScrollIndicators(View r0, int r1, int r2) {
            r0.setScrollIndicators(r1, r2);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static void cancelDragAndDrop(View r0) {
            r0.cancelDragAndDrop();
        }

        static void dispatchFinishTemporaryDetach(View r0) {
            r0.dispatchFinishTemporaryDetach();
        }

        static void dispatchStartTemporaryDetach(View r0) {
            r0.dispatchStartTemporaryDetach();
        }

        static void setPointerIcon(View r0, PointerIcon r1) {
            r0.setPointerIcon(r1);
        }

        static boolean startDragAndDrop(View r1, ClipData r2, View.DragShadowBuilder r3, Object r4, int r5) {
            return r1.startDragAndDrop(r2, r3, r4, r5);
        }

        static void updateDragShadow(View r0, View.DragShadowBuilder r1) {
            r0.updateDragShadow(r1);
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static void addKeyboardNavigationClusters(View r0, Collection<View> r1, int r2) {
            r0.addKeyboardNavigationClusters(r1, r2);
        }

        static int getImportantForAutofill(View r1) {
            return r1.getImportantForAutofill();
        }

        static int getNextClusterForwardId(View r1) {
            return r1.getNextClusterForwardId();
        }

        static boolean hasExplicitFocusable(View r1) {
            return r1.hasExplicitFocusable();
        }

        static boolean isFocusedByDefault(View r1) {
            return r1.isFocusedByDefault();
        }

        static boolean isImportantForAutofill(View r1) {
            return r1.isImportantForAutofill();
        }

        static boolean isKeyboardNavigationCluster(View r1) {
            return r1.isKeyboardNavigationCluster();
        }

        static View keyboardNavigationClusterSearch(View r1, View r2, int r3) {
            return r1.keyboardNavigationClusterSearch(r2, r3);
        }

        static boolean restoreDefaultFocus(View r1) {
            return r1.restoreDefaultFocus();
        }

        static void setAutofillHints(View r0, String... r1) {
            r0.setAutofillHints(r1);
        }

        static void setFocusedByDefault(View r0, boolean r1) {
            r0.setFocusedByDefault(r1);
        }

        static void setImportantForAutofill(View r0, int r1) {
            r0.setImportantForAutofill(r1);
        }

        static void setKeyboardNavigationCluster(View r0, boolean r1) {
            r0.setKeyboardNavigationCluster(r1);
        }

        static void setNextClusterForwardId(View r0, int r1) {
            r0.setNextClusterForwardId(r1);
        }

        static void setTooltipText(View r0, CharSequence r1) {
            r0.setTooltipText(r1);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static void addOnUnhandledKeyEventListener(View r2, final OnUnhandledKeyEventListenerCompat r3) {
            SimpleArrayMap r0 = (SimpleArrayMap) r2.getTag(C0039R.id.tag_unhandled_key_listeners);
            if (r0 != null) goto L5;
            r0 = new SimpleArrayMap();
            r2.setTag(C0039R.id.tag_unhandled_key_listeners, r0);
        L5:
            Objects.requireNonNull(r3);
            View.OnUnhandledKeyEventListener r1 = new ViewCompat$Api28Impl$$ExternalSyntheticLambda0(r3);
            r0.put(r3, r1);
            r2.addOnUnhandledKeyEventListener(r1);
        }

        static CharSequence getAccessibilityPaneTitle(View r1) {
            return r1.getAccessibilityPaneTitle();
        }

        static boolean isAccessibilityHeading(View r1) {
            return r1.isAccessibilityHeading();
        }

        static boolean isScreenReaderFocusable(View r1) {
            return r1.isScreenReaderFocusable();
        }

        static void removeOnUnhandledKeyEventListener(View r2, OnUnhandledKeyEventListenerCompat r3) {
            SimpleArrayMap r0 = (SimpleArrayMap) r2.getTag(C0039R.id.tag_unhandled_key_listeners);
            if (r0 != null) goto L5;
            return;
        L5:
            View.OnUnhandledKeyEventListener r1 = (View.OnUnhandledKeyEventListener) r0.get(r3);
            if (r1 == null) goto L9;
            r2.removeOnUnhandledKeyEventListener(r1);
            return;
        }

        static <T> T requireViewById(View r1, int r2) {
            return (T) r1.requireViewById(r2);
        }

        static void setAccessibilityHeading(View r0, boolean r1) {
            r0.setAccessibilityHeading(r1);
        }

        static void setAccessibilityPaneTitle(View r0, CharSequence r1) {
            r0.setAccessibilityPaneTitle(r1);
        }

        static void setScreenReaderFocusable(View r0, boolean r1) {
            r0.setScreenReaderFocusable(r1);
        }
    }

    private static class Api29Impl {
        private Api29Impl() {
        }

        static View.AccessibilityDelegate getAccessibilityDelegate(View r1) {
            return r1.getAccessibilityDelegate();
        }

        static List<Rect> getSystemGestureExclusionRects(View r1) {
            return r1.getSystemGestureExclusionRects();
        }

        static void saveAttributeDataForStyleable(View r0, Context r1, int[] r2, AttributeSet r3, TypedArray r4, int r5, int r6) {
            r0.saveAttributeDataForStyleable(r1, r2, r3, r4, r5, r6);
        }

        static void setSystemGestureExclusionRects(View r0, List<Rect> r1) {
            r0.setSystemGestureExclusionRects(r1);
        }
    }

    private static class Api30Impl {
        private Api30Impl() {
        }

        static CharSequence getStateDescription(View r1) {
            return r1.getStateDescription();
        }

        public static WindowInsetsControllerCompat getWindowInsetsController(View r2) {
            WindowInsetsController r0 = r2.getWindowInsetsController();
            if (r0 != null) goto L7;
            return null;
        L7:
            return WindowInsetsControllerCompat.toWindowInsetsControllerCompat(r0);
        }

        static void setStateDescription(View r0, CharSequence r1) {
            r0.setStateDescription(r1);
        }
    }

    private static final class Api31Impl {
        private Api31Impl() {
        }

        public static String[] getReceiveContentMimeTypes(View r1) {
            return r1.getReceiveContentMimeTypes();
        }

        public static ContentInfoCompat performReceiveContent(View r3, ContentInfoCompat r4) {
            ContentInfo r0 = r4.toContentInfo();
            ContentInfo r1 = r3.performReceiveContent(r0);
            if (r1 != null) goto L6;
            return null;
        L6:
            if (r1 != r0) goto L9;
            return r4;
        L9:
            return ContentInfoCompat.toContentInfoCompat(r1);
        }

        public static void setOnReceiveContentListener(View r1, String[] r2, OnReceiveContentListener r3) {
            if (r3 != null) goto L4;
            r1.setOnReceiveContentListener(r2, null);
            return;
        L4:
            r1.setOnReceiveContentListener(r2, new OnReceiveContentListenerAdapter(r3));
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRelativeDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NestedScrollType {
    }

    private static final class OnReceiveContentListenerAdapter implements android.view.OnReceiveContentListener {
        private final OnReceiveContentListener mJetpackListener;

        OnReceiveContentListenerAdapter(OnReceiveContentListener r1) {
            this.mJetpackListener = r1;
        }

        @Override // android.view.OnReceiveContentListener
        public ContentInfo onReceiveContent(View r4, ContentInfo r5) {
            ContentInfoCompat r0 = ContentInfoCompat.toContentInfoCompat(r5);
            ContentInfoCompat r1 = this.mJetpackListener.onReceiveContent(r4, r0);
            if (r1 != null) goto L6;
            return null;
        L6:
            if (r1 != r0) goto L9;
            return r5;
        L9:
            return r1.toContentInfo();
        }
    }

    public interface OnUnhandledKeyEventListenerCompat {
        boolean onUnhandledKeyEvent(View r1, KeyEvent r2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollAxis {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }

    static class UnhandledKeyEventManager {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners = null;
        private SparseArray<WeakReference<View>> mCapturedKeys;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent;
        private WeakHashMap<View, Boolean> mViewsContainingListeners;

        static {
            sViewsWithListeners = new ArrayList();
        }

        UnhandledKeyEventManager() {
            this.mViewsContainingListeners = null;
            this.mCapturedKeys = null;
            this.mLastDispatchedPreViewKeyEvent = null;
        }

        /* JADX INFO: renamed from: at */
        static UnhandledKeyEventManager m32at(View r2) {
            UnhandledKeyEventManager r0 = (UnhandledKeyEventManager) r2.getTag(C0039R.id.tag_unhandled_key_event_manager);
            if (r0 != null) goto L6;
            UnhandledKeyEventManager r1 = new UnhandledKeyEventManager();
            r2.setTag(C0039R.id.tag_unhandled_key_event_manager, r1);
            return r1;
        L6:
            return r0;
        }

        private View dispatchInOrder(View r6, KeyEvent r7) {
            if (this.mViewsContainingListeners != null) goto L5;
        L19:
            return null;
        L5:
            if (this.mViewsContainingListeners.containsKey(r6) == false) goto L19;
            if ((r6 instanceof ViewGroup) == false) goto L16;
            ViewGroup r0 = (ViewGroup) r6;
            int r2 = r0.getChildCount() - 1;
        L10:
            if (r2 < 0) goto L16;
            View r4 = dispatchInOrder(r0.getChildAt(r2), r7);
            if (r4 != null) goto L13;
            r2 = r2 - 1;
            goto L10
        L13:
            return r4;
        L16:
            if (onUnhandledKeyEvent(r6, r7) == false) goto L18;
            return r6;
        L18:
            return null;
        }

        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys != null) goto L6;
            this.mCapturedKeys = new SparseArray();
        L6:
            return this.mCapturedKeys;
        }

        private boolean onUnhandledKeyEvent(View r5, KeyEvent r6) {
            ArrayList r0 = (ArrayList) r5.getTag(C0039R.id.tag_unhandled_key_listeners);
            if (r0 == null) goto L10;
            int r1 = r0.size() - 1;
        L5:
            if (r1 < 0) goto L14;
            if (((OnUnhandledKeyEventListenerCompat) r0.get(r1)).onUnhandledKeyEvent(r5, r6) == true) goto L8;
            r1 = r1 - 1;
            goto L5
        L8:
            return true;
        L14:
            return false;
        L10:
            return false;
        }

        private void recalcViewsWithUnhandled() {
            if (this.mViewsContainingListeners == null) goto L6;
            this.mViewsContainingListeners.clear();
        L6:
            if (sViewsWithListeners.isEmpty() == false) goto L8;
            return;
        L8:
            ArrayList<WeakReference<View>> r0 = sViewsWithListeners;
            monitor-enter(r0);
        L25:
            th = move-exception;
            throw th;
        L11:
            if (this.mViewsContainingListeners != null) goto L13;
            this.mViewsContainingListeners = new WeakHashMap();     // Catch: Throwable -> L25
        L13:
            int r1 = sViewsWithListeners.size() - 1;     // Catch: Throwable -> L25
        L14:
            if (r1 < 0) goto L23;
            View r3 = sViewsWithListeners.get(r1).get();     // Catch: Throwable -> L25
            if (r3 != null) goto L18;
            sViewsWithListeners.remove(r1);     // Catch: Throwable -> L25
        L22:
            r1 = r1 - 1;     // Catch: Throwable -> L25
            goto L14
        L18:
            this.mViewsContainingListeners.put(r3, Boolean.TRUE);     // Catch: Throwable -> L25
            ViewParent r4 = r3.getParent();     // Catch: Throwable -> L25
        L20:
            if ((r4 instanceof View) == false) goto L22;
            this.mViewsContainingListeners.put((View) r4, Boolean.TRUE);     // Catch: Throwable -> L25
            r4 = r4.getParent();     // Catch: Throwable -> L25
            goto L20
        L23:
            monitor-exit(r0);     // Catch: Throwable -> L25
        }

        static void registerListeningView(View r4) {
            ArrayList<WeakReference<View>> r0 = sViewsWithListeners;
            monitor-enter(r0);
            Iterator<WeakReference<View>> r1 = sViewsWithListeners.iterator();     // Catch: Throwable -> L15
        L6:
            if (r1.hasNext() == false) goto L12;
            if (r1.next().get() != r4) goto L6;
            monitor-exit(r0);     // Catch: Throwable -> L15
            return;
        L12:
            sViewsWithListeners.add(new WeakReference(r4));     // Catch: Throwable -> L15
            monitor-exit(r0);     // Catch: Throwable -> L15
            return;
        L15:
            th = move-exception;
            throw th;
        }

        static void unregisterListeningView(View r3) {
            ArrayList<WeakReference<View>> r0 = sViewsWithListeners;
            monitor-enter(r0);
            int r1 = 0;
        L18:
        L15:
            th = move-exception;
            throw th;
        L6:
            if (r1 >= sViewsWithListeners.size()) goto L13;
            if (sViewsWithListeners.get(r1).get() == r3) goto L9;
            r1 = r1 + 1;     // Catch: Throwable -> L15
            goto L18
        L9:
            sViewsWithListeners.remove(r1);     // Catch: Throwable -> L15
            monitor-exit(r0);     // Catch: Throwable -> L15
            return;
        L13:
            monitor-exit(r0);     // Catch: Throwable -> L15
        }

        boolean dispatch(View r5, KeyEvent r6) {
            if (r6.getAction() != 0) goto L5;
            recalcViewsWithUnhandled();
        L5:
            View r0 = dispatchInOrder(r5, r6);
            if (r6.getAction() != 0) goto L12;
            int r1 = r6.getKeyCode();
            if (r0 == null) goto L12;
            if (KeyEvent.isModifierKey(r1) == true) goto L12;
            getCapturedKeys().put(r1, new WeakReference(r0));
        L12:
            if (r0 == null) goto L14;
            return true;
        L14:
            return false;
        }

        boolean preDispatch(KeyEvent r7) {
            if (this.mLastDispatchedPreViewKeyEvent != null) goto L5;
        L7:
            this.mLastDispatchedPreViewKeyEvent = new WeakReference(r7);
            WeakReference<View> r0 = null;
            SparseArray<WeakReference<View>> r2 = getCapturedKeys();
            if (r7.getAction() != 1) goto L12;
            int r3 = r2.indexOfKey(r7.getKeyCode());
            if (r3 < 0) goto L12;
            r0 = r2.valueAt(r3);
            r2.removeAt(r3);
        L12:
            if (r0 != null) goto L14;
            r0 = r2.get(r7.getKeyCode());
        L14:
            if (r0 == null) goto L21;
            View r1 = r0.get();
            if (r1 != null) goto L18;
        L20:
            return true;
        L18:
            if (ViewCompat.isAttachedToWindow(r1) == false) goto L20;
            onUnhandledKeyEvent(r1, r7);
            goto L20
        L21:
            return false;
        L5:
            if (this.mLastDispatchedPreViewKeyEvent.get() != r7) goto L7;
            return false;
        }
    }

    static {
        sNextGeneratedId = new AtomicInteger(1);
        sViewPropertyAnimatorMap = null;
        sAccessibilityDelegateCheckFailed = false;
        ACCESSIBILITY_ACTIONS_RESOURCE_IDS = new int[]{C0039R.id.accessibility_custom_action_0, C0039R.id.accessibility_custom_action_1, C0039R.id.accessibility_custom_action_2, C0039R.id.accessibility_custom_action_3, C0039R.id.accessibility_custom_action_4, C0039R.id.accessibility_custom_action_5, C0039R.id.accessibility_custom_action_6, C0039R.id.accessibility_custom_action_7, C0039R.id.accessibility_custom_action_8, C0039R.id.accessibility_custom_action_9, C0039R.id.accessibility_custom_action_10, C0039R.id.accessibility_custom_action_11, C0039R.id.accessibility_custom_action_12, C0039R.id.accessibility_custom_action_13, C0039R.id.accessibility_custom_action_14, C0039R.id.accessibility_custom_action_15, C0039R.id.accessibility_custom_action_16, C0039R.id.accessibility_custom_action_17, C0039R.id.accessibility_custom_action_18, C0039R.id.accessibility_custom_action_19, C0039R.id.accessibility_custom_action_20, C0039R.id.accessibility_custom_action_21, C0039R.id.accessibility_custom_action_22, C0039R.id.accessibility_custom_action_23, C0039R.id.accessibility_custom_action_24, C0039R.id.accessibility_custom_action_25, C0039R.id.accessibility_custom_action_26, C0039R.id.accessibility_custom_action_27, C0039R.id.accessibility_custom_action_28, C0039R.id.accessibility_custom_action_29, C0039R.id.accessibility_custom_action_30, C0039R.id.accessibility_custom_action_31};
        NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR = new ViewCompat$$ExternalSyntheticLambda0();
        sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
    }

    @Deprecated
    protected ViewCompat() {
    }

    private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        final int r1 = C0039R.id.tag_accessibility_heading;
        final Class<Boolean> r2 = Boolean.class;
        final int r3 = 28;
        return new C01174(r1, r2, r3);
    }

    public static int addAccessibilityAction(View r2, CharSequence r3, AccessibilityViewCommand r4) {
        int r0 = getAvailableActionIdFromResources(r2, r3);
        if (r0 == (-1)) goto L5;
        addAccessibilityAction(r2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(r0, r3, r4));
    L5:
        return r0;
    }

    private static void addAccessibilityAction(View r1, AccessibilityNodeInfoCompat.AccessibilityActionCompat r2) {
        ensureAccessibilityDelegateCompat(r1);
        removeActionWithId(r2.getId(), r1);
        getActionList(r1).add(r2);
        notifyViewAccessibilityStateChangedIfNeeded(r1, 0);
    }

    public static void addKeyboardNavigationClusters(View r2, Collection<View> r3, int r4) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.addKeyboardNavigationClusters(r2, r3, r4);
        return;
    }

    public static void addOnUnhandledKeyEventListener(View r3, OnUnhandledKeyEventListenerCompat r4) {
        if (Build.VERSION.SDK_INT < 28) goto L6;
        Api28Impl.addOnUnhandledKeyEventListener(r3, r4);
        return;
    L6:
        ArrayList r0 = (ArrayList) r3.getTag(C0039R.id.tag_unhandled_key_listeners);
        if (r0 != null) goto L9;
        r0 = new ArrayList();
        r3.setTag(C0039R.id.tag_unhandled_key_listeners, r0);
    L9:
        r0.add(r4);
        if (r0.size() != 1) goto L13;
        UnhandledKeyEventManager.registerListeningView(r3);
        return;
    }

    public static ViewPropertyAnimatorCompat animate(View r2) {
        if (sViewPropertyAnimatorMap != null) goto L5;
        sViewPropertyAnimatorMap = new WeakHashMap();
    L5:
        ViewPropertyAnimatorCompat r0 = sViewPropertyAnimatorMap.get(r2);
        if (r0 != null) goto L9;
        ViewPropertyAnimatorCompat r1 = new ViewPropertyAnimatorCompat(r2);
        sViewPropertyAnimatorMap.put(r2, r1);
        return r1;
    L9:
        return r0;
    }

    private static void bindTempDetach() {
        sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);     // Catch: NoSuchMethodException -> L4
        sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);     // Catch: NoSuchMethodException -> L4
    L6:
        sTempDetachBound = true;
        return;
    L4:
        e = move-exception;
        Log.e(TAG, "Couldn't find method", e);
        goto L6
    }

    @Deprecated
    public static boolean canScrollHorizontally(View r1, int r2) {
        return r1.canScrollHorizontally(r2);
    }

    @Deprecated
    public static boolean canScrollVertically(View r1, int r2) {
        return r1.canScrollVertically(r2);
    }

    public static void cancelDragAndDrop(View r2) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api24Impl.cancelDragAndDrop(r2);
        return;
    }

    @Deprecated
    public static int combineMeasuredStates(int r1, int r2) {
        return View.combineMeasuredStates(r1, r2);
    }

    private static void compatOffsetLeftAndRight(View r2, int r3) {
        r2.offsetLeftAndRight(r3);
        if (r2.getVisibility() != 0) goto L8;
        tickleInvalidationFlag(r2);
        Object r0 = r2.getParent();
        if ((r0 instanceof View) == false) goto L9;
        tickleInvalidationFlag((View) r0);
        return;
    L9:
        return;
    }

    private static void compatOffsetTopAndBottom(View r2, int r3) {
        r2.offsetTopAndBottom(r3);
        if (r2.getVisibility() != 0) goto L8;
        tickleInvalidationFlag(r2);
        Object r0 = r2.getParent();
        if ((r0 instanceof View) == false) goto L9;
        tickleInvalidationFlag((View) r0);
        return;
    L9:
        return;
    }

    public static WindowInsetsCompat computeSystemWindowInsets(View r1, WindowInsetsCompat r2, Rect r3) {
        return Api21Impl.computeSystemWindowInsets(r1, r2, r3);
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View r3, WindowInsetsCompat r4) {
        WindowInsets r0 = r4.toWindowInsets();
        if (r0 == null) goto L8;
        WindowInsets r1 = Api20Impl.dispatchApplyWindowInsets(r3, r0);
        if (r1.equals(r0) == true) goto L8;
        return WindowInsetsCompat.toWindowInsetsCompat(r1, r3);
    L8:
        return r4;
    }

    public static void dispatchFinishTemporaryDetach(View r3) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api24Impl.dispatchFinishTemporaryDetach(r3);
        return;
    L6:
        if (sTempDetachBound == true) goto L9;
        bindTempDetach();
    L9:
        if (sDispatchFinishTemporaryDetach != null) goto L17;
        r3.onFinishTemporaryDetach();
        return;
    L17:
        sDispatchFinishTemporaryDetach.invoke(r3, new Object[0]);     // Catch: Exception -> L12
        return;
    L12:
        e = move-exception;
        Log.d(TAG, "Error calling dispatchFinishTemporaryDetach", e);
    }

    public static boolean dispatchNestedFling(View r1, float r2, float r3, boolean r4) {
        return Api21Impl.dispatchNestedFling(r1, r2, r3, r4);
    }

    public static boolean dispatchNestedPreFling(View r1, float r2, float r3) {
        return Api21Impl.dispatchNestedPreFling(r1, r2, r3);
    }

    public static boolean dispatchNestedPreScroll(View r1, int r2, int r3, int[] r4, int[] r5) {
        return Api21Impl.dispatchNestedPreScroll(r1, r2, r3, r4, r5);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean dispatchNestedPreScroll(View r7, int r8, int r9, int[] r10, int[] r11, int r12) {
        if ((r7 instanceof NestedScrollingChild2) == false) goto L7;
        return ((NestedScrollingChild2) r7).dispatchNestedPreScroll(r8, r9, r10, r11, r12);
    L7:
        if (r12 == 0) goto L9;
        return false;
    L9:
        return dispatchNestedPreScroll(r7, r8, r9, r10, r11);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void dispatchNestedScroll(View r9, int r10, int r11, int r12, int r13, int[] r14, int r15, int[] r16) {
        if ((r9 instanceof NestedScrollingChild3) == false) goto L5;
        ((NestedScrollingChild3) r9).dispatchNestedScroll(r10, r11, r12, r13, r14, r15, r16);
        return;
    L5:
        dispatchNestedScroll(r9, r10, r11, r12, r13, r14, r15);
    }

    public static boolean dispatchNestedScroll(View r1, int r2, int r3, int r4, int r5, int[] r6) {
        return Api21Impl.dispatchNestedScroll(r1, r2, r3, r4, r5, r6);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean dispatchNestedScroll(View r8, int r9, int r10, int r11, int r12, int[] r13, int r14) {
        if ((r8 instanceof NestedScrollingChild2) == false) goto L7;
        return ((NestedScrollingChild2) r8).dispatchNestedScroll(r9, r10, r11, r12, r13, r14);
    L7:
        if (r14 != 0) goto L11;
        return dispatchNestedScroll(r8, r9, r10, r11, r12, r13);
    L11:
        return false;
    }

    public static void dispatchStartTemporaryDetach(View r3) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api24Impl.dispatchStartTemporaryDetach(r3);
        return;
    L6:
        if (sTempDetachBound == true) goto L9;
        bindTempDetach();
    L9:
        if (sDispatchStartTemporaryDetach != null) goto L17;
        r3.onStartTemporaryDetach();
        return;
    L17:
        sDispatchStartTemporaryDetach.invoke(r3, new Object[0]);     // Catch: Exception -> L12
        return;
    L12:
        e = move-exception;
        Log.d(TAG, "Error calling dispatchStartTemporaryDetach", e);
    }

    static boolean dispatchUnhandledKeyEventBeforeCallback(View r2, KeyEvent r3) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return false;
    L7:
        return UnhandledKeyEventManager.m32at(r2).dispatch(r2, r3);
    }

    static boolean dispatchUnhandledKeyEventBeforeHierarchy(View r2, KeyEvent r3) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return false;
    L7:
        return UnhandledKeyEventManager.m32at(r2).preDispatch(r3);
    }

    public static void enableAccessibleClickableSpanSupport(View r0) {
        ensureAccessibilityDelegateCompat(r0);
    }

    static void ensureAccessibilityDelegateCompat(View r2) {
        AccessibilityDelegateCompat r0 = getAccessibilityDelegate(r2);
        if (r0 != null) goto L5;
        r0 = new AccessibilityDelegateCompat();
    L5:
        setAccessibilityDelegate(r2, r0);
    }

    public static int generateViewId() {
        return Api17Impl.generateViewId();
    }

    public static AccessibilityDelegateCompat getAccessibilityDelegate(View r2) {
        View.AccessibilityDelegate r0 = getAccessibilityDelegateInternal(r2);
        if (r0 != null) goto L7;
        return null;
    L7:
        if ((r0 instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) == false) goto L11;
        return ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter) r0).mCompat;
    L11:
        return new AccessibilityDelegateCompat(r0);
    }

    private static View.AccessibilityDelegate getAccessibilityDelegateInternal(View r2) {
        if (Build.VERSION.SDK_INT < 29) goto L7;
        return Api29Impl.getAccessibilityDelegate(r2);
    L7:
        return getAccessibilityDelegateThroughReflection(r2);
    }

    private static View.AccessibilityDelegate getAccessibilityDelegateThroughReflection(View r4) {
        if (sAccessibilityDelegateCheckFailed == false) goto L6;
        return null;
    L6:
        if (sAccessibilityDelegateField != null) goto L22;
        sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");     // Catch: Throwable -> L9
        sAccessibilityDelegateField.setAccessible(true);     // Catch: Throwable -> L9
    L10:
        sAccessibilityDelegateCheckFailed = true;
        return null;
    L22:
        Object r0 = sAccessibilityDelegateField.get(r4);     // Catch: Throwable -> L17
        if ((r0 instanceof View.AccessibilityDelegate) == false) goto L16;
        return (View.AccessibilityDelegate) r0;
    L16:
        return null;
    L18:
        sAccessibilityDelegateCheckFailed = true;
        return null;
    }

    public static int getAccessibilityLiveRegion(View r1) {
        return Api19Impl.getAccessibilityLiveRegion(r1);
    }

    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View r2) {
        AccessibilityNodeProvider r0 = Api16Impl.getAccessibilityNodeProvider(r2);
        if (r0 != null) goto L5;
        return null;
    L5:
        return new AccessibilityNodeProviderCompat(r0);
    }

    public static CharSequence getAccessibilityPaneTitle(View r1) {
        return paneTitleProperty().get(r1);
    }

    private static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View r2) {
        ArrayList r0 = (ArrayList) r2.getTag(C0039R.id.tag_accessibility_actions);
        if (r0 != null) goto L6;
        ArrayList r1 = new ArrayList();
        r2.setTag(C0039R.id.tag_accessibility_actions, r1);
        return r1;
    L6:
        return r0;
    }

    @Deprecated
    public static float getAlpha(View r1) {
        return r1.getAlpha();
    }

    private static int getAvailableActionIdFromResources(View r7, CharSequence r8) {
        int r0 = -1;
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> r1 = getActionList(r7);
        int r2 = 0;
    L4:
        if (r2 >= r1.size()) goto L10;
        if (TextUtils.equals(r8, r1.get(r2).getLabel()) == true) goto L8;
        r2 = r2 + 1;
        goto L4
    L8:
        return r1.get(r2).getId();
    L10:
        int r3 = 0;
    L12:
        if (r3 >= ACCESSIBILITY_ACTIONS_RESOURCE_IDS.length) goto L26;
        if (r0 != (-1)) goto L26;
        int r4 = ACCESSIBILITY_ACTIONS_RESOURCE_IDS[r3];
        boolean r5 = true;
        int r6 = 0;
    L17:
        if (r6 >= r1.size()) goto L23;
        if (r1.get(r6).getId() == r4) goto L21;
        boolean r9 = true;
    L22:
        r5 = r5 & r9;
        r6 = r6 + 1;
        goto L17
    L21:
        r9 = false;
        goto L22
    L23:
        if (r5 == false) goto L25;
        r0 = r4;
    L25:
        r3 = r3 + 1;
    L26:
        return r0;
    }

    public static ColorStateList getBackgroundTintList(View r1) {
        return Api21Impl.getBackgroundTintList(r1);
    }

    public static PorterDuff.Mode getBackgroundTintMode(View r1) {
        return Api21Impl.getBackgroundTintMode(r1);
    }

    public static Rect getClipBounds(View r1) {
        return Api18Impl.getClipBounds(r1);
    }

    public static Display getDisplay(View r1) {
        return Api17Impl.getDisplay(r1);
    }

    public static float getElevation(View r1) {
        return Api21Impl.getElevation(r1);
    }

    private static Rect getEmptyTempRect() {
        if (sThreadLocalRect != null) goto L5;
        sThreadLocalRect = new ThreadLocal();
    L5:
        Rect r0 = sThreadLocalRect.get();
        if (r0 != null) goto L8;
        r0 = new Rect();
        sThreadLocalRect.set(r0);
    L8:
        r0.setEmpty();
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static OnReceiveContentViewBehavior getFallback(View r1) {
        if ((r1 instanceof OnReceiveContentViewBehavior) == false) goto L7;
        return (OnReceiveContentViewBehavior) r1;
    L7:
        return NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR;
    }

    public static boolean getFitsSystemWindows(View r1) {
        return Api16Impl.getFitsSystemWindows(r1);
    }

    public static int getImportantForAccessibility(View r1) {
        return Api16Impl.getImportantForAccessibility(r1);
    }

    public static int getImportantForAutofill(View r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return 0;
    L5:
        return Api26Impl.getImportantForAutofill(r2);
    }

    public static int getLabelFor(View r1) {
        return Api17Impl.getLabelFor(r1);
    }

    @Deprecated
    public static int getLayerType(View r1) {
        return r1.getLayerType();
    }

    public static int getLayoutDirection(View r1) {
        return Api17Impl.getLayoutDirection(r1);
    }

    @Deprecated
    public static Matrix getMatrix(View r1) {
        return r1.getMatrix();
    }

    @Deprecated
    public static int getMeasuredHeightAndState(View r1) {
        return r1.getMeasuredHeightAndState();
    }

    @Deprecated
    public static int getMeasuredState(View r1) {
        return r1.getMeasuredState();
    }

    @Deprecated
    public static int getMeasuredWidthAndState(View r1) {
        return r1.getMeasuredWidthAndState();
    }

    public static int getMinimumHeight(View r1) {
        return Api16Impl.getMinimumHeight(r1);
    }

    public static int getMinimumWidth(View r1) {
        return Api16Impl.getMinimumWidth(r1);
    }

    public static int getNextClusterForwardId(View r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return -1;
    L5:
        return Api26Impl.getNextClusterForwardId(r2);
    }

    public static String[] getOnReceiveContentMimeTypes(View r2) {
        if (Build.VERSION.SDK_INT < 31) goto L7;
        return Api31Impl.getReceiveContentMimeTypes(r2);
    L7:
        return (String[]) r2.getTag(C0039R.id.tag_on_receive_content_mime_types);
    }

    @Deprecated
    public static int getOverScrollMode(View r1) {
        return r1.getOverScrollMode();
    }

    public static int getPaddingEnd(View r1) {
        return Api17Impl.getPaddingEnd(r1);
    }

    public static int getPaddingStart(View r1) {
        return Api17Impl.getPaddingStart(r1);
    }

    public static ViewParent getParentForAccessibility(View r1) {
        return Api16Impl.getParentForAccessibility(r1);
    }

    @Deprecated
    public static float getPivotX(View r1) {
        return r1.getPivotX();
    }

    @Deprecated
    public static float getPivotY(View r1) {
        return r1.getPivotY();
    }

    public static WindowInsetsCompat getRootWindowInsets(View r1) {
        return Api23Impl.getRootWindowInsets(r1);
    }

    @Deprecated
    public static float getRotation(View r1) {
        return r1.getRotation();
    }

    @Deprecated
    public static float getRotationX(View r1) {
        return r1.getRotationX();
    }

    @Deprecated
    public static float getRotationY(View r1) {
        return r1.getRotationY();
    }

    @Deprecated
    public static float getScaleX(View r1) {
        return r1.getScaleX();
    }

    @Deprecated
    public static float getScaleY(View r1) {
        return r1.getScaleY();
    }

    public static int getScrollIndicators(View r1) {
        return Api23Impl.getScrollIndicators(r1);
    }

    public static CharSequence getStateDescription(View r1) {
        return stateDescriptionProperty().get(r1);
    }

    public static List<Rect> getSystemGestureExclusionRects(View r2) {
        if (Build.VERSION.SDK_INT < 29) goto L7;
        return Api29Impl.getSystemGestureExclusionRects(r2);
    L7:
        return Collections.emptyList();
    }

    public static String getTransitionName(View r1) {
        return Api21Impl.getTransitionName(r1);
    }

    @Deprecated
    public static float getTranslationX(View r1) {
        return r1.getTranslationX();
    }

    @Deprecated
    public static float getTranslationY(View r1) {
        return r1.getTranslationY();
    }

    public static float getTranslationZ(View r1) {
        return Api21Impl.getTranslationZ(r1);
    }

    @Deprecated
    public static WindowInsetsControllerCompat getWindowInsetsController(View r3) {
        if (Build.VERSION.SDK_INT >= 30) goto L5;
        Context r0 = r3.getContext();
    L8:
        if ((r0 instanceof ContextWrapper) == false) goto L16;
        if ((r0 instanceof Activity) == true) goto L11;
        r0 = ((ContextWrapper) r0).getBaseContext();
        goto L8
    L11:
        Window r1 = ((Activity) r0).getWindow();
        if (r1 != null) goto L14;
        return null;
    L14:
        return WindowCompat.getInsetsController(r1, r3);
    L16:
        return null;
    L5:
        return Api30Impl.getWindowInsetsController(r3);
    }

    @Deprecated
    public static int getWindowSystemUiVisibility(View r1) {
        return Api16Impl.getWindowSystemUiVisibility(r1);
    }

    @Deprecated
    public static float getX(View r1) {
        return r1.getX();
    }

    @Deprecated
    public static float getY(View r1) {
        return r1.getY();
    }

    public static float getZ(View r1) {
        return Api21Impl.getZ(r1);
    }

    public static boolean hasAccessibilityDelegate(View r1) {
        if (getAccessibilityDelegateInternal(r1) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean hasExplicitFocusable(View r2) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.hasExplicitFocusable(r2);
    L7:
        return r2.hasFocusable();
    }

    public static boolean hasNestedScrollingParent(View r1) {
        return Api21Impl.hasNestedScrollingParent(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean hasNestedScrollingParent(View r1, int r2) {
        if ((r1 instanceof NestedScrollingChild2) == false) goto L5;
        ((NestedScrollingChild2) r1).hasNestedScrollingParent(r2);
        return false;
    L5:
        if (r2 == 0) goto L7;
        return false;
    L7:
        return hasNestedScrollingParent(r1);
    }

    public static boolean hasOnClickListeners(View r1) {
        return Api15Impl.hasOnClickListeners(r1);
    }

    public static boolean hasOverlappingRendering(View r1) {
        return Api16Impl.hasOverlappingRendering(r1);
    }

    public static boolean hasTransientState(View r1) {
        return Api16Impl.hasTransientState(r1);
    }

    public static boolean isAccessibilityHeading(View r2) {
        Boolean r0 = accessibilityHeadingProperty().get(r2);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.booleanValue() == false) goto L7;
        return true;
    }

    public static boolean isAttachedToWindow(View r1) {
        return Api19Impl.isAttachedToWindow(r1);
    }

    public static boolean isFocusedByDefault(View r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return false;
    L5:
        return Api26Impl.isFocusedByDefault(r2);
    }

    public static boolean isImportantForAccessibility(View r1) {
        return Api21Impl.isImportantForAccessibility(r1);
    }

    public static boolean isImportantForAutofill(View r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return true;
    L5:
        return Api26Impl.isImportantForAutofill(r2);
    }

    public static boolean isInLayout(View r1) {
        return Api18Impl.isInLayout(r1);
    }

    public static boolean isKeyboardNavigationCluster(View r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return false;
    L5:
        return Api26Impl.isKeyboardNavigationCluster(r2);
    }

    public static boolean isLaidOut(View r1) {
        return Api19Impl.isLaidOut(r1);
    }

    public static boolean isLayoutDirectionResolved(View r1) {
        return Api19Impl.isLayoutDirectionResolved(r1);
    }

    public static boolean isNestedScrollingEnabled(View r1) {
        return Api21Impl.isNestedScrollingEnabled(r1);
    }

    @Deprecated
    public static boolean isOpaque(View r1) {
        return r1.isOpaque();
    }

    public static boolean isPaddingRelative(View r1) {
        return Api17Impl.isPaddingRelative(r1);
    }

    public static boolean isScreenReaderFocusable(View r2) {
        Boolean r0 = screenReaderFocusableProperty().get(r2);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.booleanValue() == false) goto L7;
        return true;
    }

    @Deprecated
    public static void jumpDrawablesToCurrentState(View r0) {
        r0.jumpDrawablesToCurrentState();
    }

    public static View keyboardNavigationClusterSearch(View r2, View r3, int r4) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return null;
    L5:
        return Api26Impl.keyboardNavigationClusterSearch(r2, r3, r4);
    }

    static /* synthetic */ ContentInfoCompat lambda$static$0(ContentInfoCompat r0) {
        return r0;
    }

    static void notifyViewAccessibilityStateChangedIfNeeded(View r6, int r7) {
        AccessibilityManager r0 = (AccessibilityManager) r6.getContext().getSystemService("accessibility");
        if (r0.isEnabled() == true) goto L6;
        return;
    L6:
        if (getAccessibilityPaneTitle(r6) != null) goto L8;
    L12:
        boolean r1 = false;
    L13:
        int r3 = 32;
        if (getAccessibilityLiveRegion(r6) != 0) goto L27;
        if (r1 == true) goto L27;
        if (r7 != 32) goto L20;
        AccessibilityEvent r2 = AccessibilityEvent.obtain();
        r6.onInitializeAccessibilityEvent(r2);
        r2.setEventType(32);
        Api19Impl.setContentChangeTypes(r2, r7);
        r2.setSource(r6);
        r6.onPopulateAccessibilityEvent(r2);
        r2.getText().add(getAccessibilityPaneTitle(r6));
        r0.sendAccessibilityEvent(r2);
        return;
    L20:
        if (r6.getParent() != null) goto L36;
        return;
    L36:
        Api19Impl.notifySubtreeAccessibilityStateChanged(r6.getParent(), r6, r6, r7);     // Catch: AbstractMethodError -> L24
        return;
    L24:
        e = move-exception;
        Log.e(TAG, r6.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", e);
        return;
    L27:
        AccessibilityEvent r4 = AccessibilityEvent.obtain();
        if (r1 == true) goto L31;
        r3 = 2048;
    L31:
        r4.setEventType(r3);
        Api19Impl.setContentChangeTypes(r4, r7);
        if (r1 == false) goto L34;
        r4.getText().add(getAccessibilityPaneTitle(r6));
        setViewImportanceForAccessibilityIfNeeded(r6);
    L34:
        r6.sendAccessibilityEventUnchecked(r4);
        return;
    L8:
        if (r6.isShown() == false) goto L12;
        if (r6.getWindowVisibility() != 0) goto L12;
        r1 = true;
        goto L13
    }

    public static void offsetLeftAndRight(View r0, int r1) {
        r0.offsetLeftAndRight(r1);
    }

    public static void offsetTopAndBottom(View r0, int r1) {
        r0.offsetTopAndBottom(r1);
    }

    public static WindowInsetsCompat onApplyWindowInsets(View r3, WindowInsetsCompat r4) {
        WindowInsets r0 = r4.toWindowInsets();
        if (r0 == null) goto L8;
        WindowInsets r1 = Api20Impl.onApplyWindowInsets(r3, r0);
        if (r1.equals(r0) == true) goto L8;
        return WindowInsetsCompat.toWindowInsetsCompat(r1, r3);
    L8:
        return r4;
    }

    @Deprecated
    public static void onInitializeAccessibilityEvent(View r0, AccessibilityEvent r1) {
        r0.onInitializeAccessibilityEvent(r1);
    }

    public static void onInitializeAccessibilityNodeInfo(View r1, AccessibilityNodeInfoCompat r2) {
        r1.onInitializeAccessibilityNodeInfo(r2.unwrap());
    }

    @Deprecated
    public static void onPopulateAccessibilityEvent(View r0, AccessibilityEvent r1) {
        r0.onPopulateAccessibilityEvent(r1);
    }

    private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        final int r1 = C0039R.id.tag_accessibility_pane_title;
        final Class<CharSequence> r2 = CharSequence.class;
        final int r3 = 8;
        final int r4 = 28;
        return new C01152(r1, r2, r3, r4);
    }

    public static boolean performAccessibilityAction(View r1, int r2, Bundle r3) {
        return Api16Impl.performAccessibilityAction(r1, r2, r3);
    }

    public static ContentInfoCompat performReceiveContent(View r3, ContentInfoCompat r4) {
        if (Log.isLoggable(TAG, 3) == false) goto L6;
        Log.d(TAG, "performReceiveContent: " + r4 + ", view=" + r3.getClass().getSimpleName() + "[" + r3.getId() + "]");
    L6:
        if (Build.VERSION.SDK_INT >= 31) goto L8;
        OnReceiveContentListener r0 = (OnReceiveContentListener) r3.getTag(C0039R.id.tag_on_receive_content_listener);
        if (r0 == null) goto L17;
        ContentInfoCompat r1 = r0.onReceiveContent(r3, r4);
        if (r1 != null) goto L15;
        return null;
    L15:
        return getFallback(r3).onReceiveContent(r1);
    L17:
        return getFallback(r3).onReceiveContent(r4);
    L8:
        return Api31Impl.performReceiveContent(r3, r4);
    }

    public static void postInvalidateOnAnimation(View r0) {
        Api16Impl.postInvalidateOnAnimation(r0);
    }

    public static void postInvalidateOnAnimation(View r0, int r1, int r2, int r3, int r4) {
        Api16Impl.postInvalidateOnAnimation(r0, r1, r2, r3, r4);
    }

    public static void postOnAnimation(View r0, Runnable r1) {
        Api16Impl.postOnAnimation(r0, r1);
    }

    public static void postOnAnimationDelayed(View r0, Runnable r1, long r2) {
        Api16Impl.postOnAnimationDelayed(r0, r1, r2);
    }

    public static void removeAccessibilityAction(View r1, int r2) {
        removeActionWithId(r2, r1);
        notifyViewAccessibilityStateChangedIfNeeded(r1, 0);
    }

    private static void removeActionWithId(int r3, View r4) {
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> r0 = getActionList(r4);
        int r1 = 0;
    L4:
        if (r1 >= r0.size()) goto L12;
        if (r0.get(r1).getId() == r3) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        r0.remove(r1);
        return;
    }

    public static void removeOnUnhandledKeyEventListener(View r2, OnUnhandledKeyEventListenerCompat r3) {
        if (Build.VERSION.SDK_INT < 28) goto L6;
        Api28Impl.removeOnUnhandledKeyEventListener(r2, r3);
        return;
    L6:
        ArrayList r0 = (ArrayList) r2.getTag(C0039R.id.tag_unhandled_key_listeners);
        if (r0 == null) goto L12;
        r0.remove(r3);
        if (r0.size() != 0) goto L13;
        UnhandledKeyEventManager.unregisterListeningView(r2);
        return;
    L13:
        return;
    }

    public static void replaceAccessibilityAction(View r1, AccessibilityNodeInfoCompat.AccessibilityActionCompat r2, CharSequence r3, AccessibilityViewCommand r4) {
        if (r4 != null) goto L5;
        if (r3 != null) goto L5;
        removeAccessibilityAction(r1, r2.getId());
        return;
    L5:
        addAccessibilityAction(r1, r2.createReplacementAction(r3, r4));
    }

    public static void requestApplyInsets(View r0) {
        Api20Impl.requestApplyInsets(r0);
    }

    public static <T extends View> T requireViewById(View r3, int r4) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        T r0 = (T) r3.findViewById(r4);
        if (r0 == null) goto L10;
        return r0;
    L10:
        throw new IllegalArgumentException("ID does not reference a View inside this View");
    L5:
        return (T) Api28Impl.requireViewById(r3, r4);
    }

    @Deprecated
    public static int resolveSizeAndState(int r1, int r2, int r3) {
        return View.resolveSizeAndState(r1, r2, r3);
    }

    public static boolean restoreDefaultFocus(View r2) {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return Api26Impl.restoreDefaultFocus(r2);
    L7:
        return r2.requestFocus();
    }

    public static void saveAttributeDataForStyleable(View r2, Context r3, int[] r4, AttributeSet r5, TypedArray r6, int r7, int r8) {
        if (Build.VERSION.SDK_INT < 29) goto L6;
        Api29Impl.saveAttributeDataForStyleable(r2, r3, r4, r5, r6, r7, r8);
        return;
    }

    private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        final int r1 = C0039R.id.tag_screen_reader_focusable;
        final Class<Boolean> r2 = Boolean.class;
        final int r3 = 28;
        return new C01141(r1, r2, r3);
    }

    public static void setAccessibilityDelegate(View r1, AccessibilityDelegateCompat r2) {
        if (r2 == null) goto L4;
    L6:
        if (r2 != null) goto L8;
        View.AccessibilityDelegate r0 = null;
    L9:
        r1.setAccessibilityDelegate(r0);
        return;
    L8:
        r0 = r2.getBridge();
        goto L9
    L4:
        if ((getAccessibilityDelegateInternal(r1) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) == false) goto L6;
        r2 = new AccessibilityDelegateCompat();
        goto L6
    }

    public static void setAccessibilityHeading(View r2, boolean r3) {
        accessibilityHeadingProperty().set(r2, Boolean.valueOf(r3));
    }

    public static void setAccessibilityLiveRegion(View r0, int r1) {
        Api19Impl.setAccessibilityLiveRegion(r0, r1);
    }

    public static void setAccessibilityPaneTitle(View r1, CharSequence r2) {
        paneTitleProperty().set(r1, r2);
        if (r2 == null) goto L5;
        sAccessibilityPaneVisibilityManager.addAccessibilityPane(r1);
        return;
    L5:
        sAccessibilityPaneVisibilityManager.removeAccessibilityPane(r1);
    }

    @Deprecated
    public static void setActivated(View r0, boolean r1) {
        r0.setActivated(r1);
    }

    @Deprecated
    public static void setAlpha(View r0, float r1) {
        r0.setAlpha(r1);
    }

    public static void setAutofillHints(View r2, String... r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.setAutofillHints(r2, r3);
        return;
    }

    public static void setBackground(View r0, Drawable r1) {
        Api16Impl.setBackground(r0, r1);
    }

    public static void setBackgroundTintList(View r0, ColorStateList r1) {
        Api21Impl.setBackgroundTintList(r0, r1);
    }

    public static void setBackgroundTintMode(View r0, PorterDuff.Mode r1) {
        Api21Impl.setBackgroundTintMode(r0, r1);
    }

    @Deprecated
    public static void setChildrenDrawingOrderEnabled(ViewGroup r8, boolean r9) {
        if (sChildrenDrawingOrderMethod != null) goto L21;
        sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});     // Catch: NoSuchMethodException -> L6
    L8:
        sChildrenDrawingOrderMethod.setAccessible(true);
    L6:
        e = move-exception;
        Log.e(TAG, "Unable to find childrenDrawingOrderEnabled", e);
    L21:
        sChildrenDrawingOrderMethod.invoke(r8, new Object[]{Boolean.valueOf(r9)});     // Catch: InvocationTargetException -> L11 IllegalArgumentException -> L13 IllegalAccessException -> L15
    L25:
        return;
    L15:
        e = move-exception;
        Log.e(TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
        return;
    L13:
        e = move-exception;
        Log.e(TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
        return;
    L11:
        e = move-exception;
        Log.e(TAG, "Unable to invoke childrenDrawingOrderEnabled", e);
    }

    public static void setClipBounds(View r0, Rect r1) {
        Api18Impl.setClipBounds(r0, r1);
    }

    public static void setElevation(View r0, float r1) {
        Api21Impl.setElevation(r0, r1);
    }

    @Deprecated
    public static void setFitsSystemWindows(View r0, boolean r1) {
        r0.setFitsSystemWindows(r1);
    }

    public static void setFocusedByDefault(View r2, boolean r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.setFocusedByDefault(r2, r3);
        return;
    }

    public static void setHasTransientState(View r0, boolean r1) {
        Api16Impl.setHasTransientState(r0, r1);
    }

    public static void setImportantForAccessibility(View r0, int r1) {
        Api16Impl.setImportantForAccessibility(r0, r1);
    }

    public static void setImportantForAutofill(View r2, int r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.setImportantForAutofill(r2, r3);
        return;
    }

    public static void setKeyboardNavigationCluster(View r2, boolean r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.setKeyboardNavigationCluster(r2, r3);
        return;
    }

    public static void setLabelFor(View r0, int r1) {
        Api17Impl.setLabelFor(r0, r1);
    }

    public static void setLayerPaint(View r0, Paint r1) {
        Api17Impl.setLayerPaint(r0, r1);
    }

    @Deprecated
    public static void setLayerType(View r0, int r1, Paint r2) {
        r0.setLayerType(r1, r2);
    }

    public static void setLayoutDirection(View r0, int r1) {
        Api17Impl.setLayoutDirection(r0, r1);
    }

    public static void setNestedScrollingEnabled(View r0, boolean r1) {
        Api21Impl.setNestedScrollingEnabled(r0, r1);
    }

    public static void setNextClusterForwardId(View r2, int r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.setNextClusterForwardId(r2, r3);
        return;
    }

    public static void setOnApplyWindowInsetsListener(View r0, OnApplyWindowInsetsListener r1) {
        Api21Impl.setOnApplyWindowInsetsListener(r0, r1);
    }

    public static void setOnReceiveContentListener(View r5, String[] r6, OnReceiveContentListener r7) {
        if (Build.VERSION.SDK_INT < 31) goto L6;
        Api31Impl.setOnReceiveContentListener(r5, r6, r7);
        return;
    L6:
        if (r6 != null) goto L8;
    L11:
        String[] r0 = null;
    L12:
        int r8 = 0;
        if (r7 == null) goto L18;
        if (r0 == null) goto L16;
        boolean r1 = true;
    L17:
        Preconditions.checkArgument(r1, "When the listener is set, MIME types must also be set");
        goto L18
    L16:
        r1 = false;
    L18:
        if (r0 == null) goto L26;
        boolean r2 = false;
        int r3 = r0.length;
    L20:
        if (r8 >= r3) goto L25;
        if (r0[r8].startsWith("*") == true) goto L23;
        r8 = r8 + 1;
        goto L20
    L23:
        r2 = true;
    L25:
        Preconditions.checkArgument(!r2, "A MIME type set here must not start with *: " + Arrays.toString(r0));
    L26:
        r5.setTag(C0039R.id.tag_on_receive_content_mime_types, r0);
        r5.setTag(C0039R.id.tag_on_receive_content_listener, r7);
        return;
    L8:
        if (r6.length == 0) goto L11;
        r0 = r6;
        goto L12
    }

    @Deprecated
    public static void setOverScrollMode(View r0, int r1) {
        r0.setOverScrollMode(r1);
    }

    public static void setPaddingRelative(View r0, int r1, int r2, int r3, int r4) {
        Api17Impl.setPaddingRelative(r0, r1, r2, r3, r4);
    }

    @Deprecated
    public static void setPivotX(View r0, float r1) {
        r0.setPivotX(r1);
    }

    @Deprecated
    public static void setPivotY(View r0, float r1) {
        r0.setPivotY(r1);
    }

    public static void setPointerIcon(View r2, PointerIconCompat r3) {
        if (Build.VERSION.SDK_INT < 24) goto L9;
        if (r3 == null) goto L6;
        Object r0 = r3.getPointerIcon();
    L7:
        Api24Impl.setPointerIcon(r2, (PointerIcon) r0);
        return;
    L6:
        r0 = null;
        goto L7
    }

    @Deprecated
    public static void setRotation(View r0, float r1) {
        r0.setRotation(r1);
    }

    @Deprecated
    public static void setRotationX(View r0, float r1) {
        r0.setRotationX(r1);
    }

    @Deprecated
    public static void setRotationY(View r0, float r1) {
        r0.setRotationY(r1);
    }

    @Deprecated
    public static void setSaveFromParentEnabled(View r0, boolean r1) {
        r0.setSaveFromParentEnabled(r1);
    }

    @Deprecated
    public static void setScaleX(View r0, float r1) {
        r0.setScaleX(r1);
    }

    @Deprecated
    public static void setScaleY(View r0, float r1) {
        r0.setScaleY(r1);
    }

    public static void setScreenReaderFocusable(View r2, boolean r3) {
        screenReaderFocusableProperty().set(r2, Boolean.valueOf(r3));
    }

    public static void setScrollIndicators(View r0, int r1) {
        Api23Impl.setScrollIndicators(r0, r1);
    }

    public static void setScrollIndicators(View r0, int r1, int r2) {
        Api23Impl.setScrollIndicators(r0, r1, r2);
    }

    public static void setStateDescription(View r1, CharSequence r2) {
        stateDescriptionProperty().set(r1, r2);
    }

    public static void setSystemGestureExclusionRects(View r2, List<Rect> r3) {
        if (Build.VERSION.SDK_INT < 29) goto L6;
        Api29Impl.setSystemGestureExclusionRects(r2, r3);
        return;
    }

    public static void setTooltipText(View r2, CharSequence r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        Api26Impl.setTooltipText(r2, r3);
        return;
    }

    public static void setTransitionName(View r0, String r1) {
        Api21Impl.setTransitionName(r0, r1);
    }

    @Deprecated
    public static void setTranslationX(View r0, float r1) {
        r0.setTranslationX(r1);
    }

    @Deprecated
    public static void setTranslationY(View r0, float r1) {
        r0.setTranslationY(r1);
    }

    public static void setTranslationZ(View r0, float r1) {
        Api21Impl.setTranslationZ(r0, r1);
    }

    private static void setViewImportanceForAccessibilityIfNeeded(View r3) {
        if (getImportantForAccessibility(r3) != 0) goto L5;
        setImportantForAccessibility(r3, 1);
    L5:
        ViewParent r0 = r3.getParent();
    L7:
        if ((r0 instanceof View) == false) goto L15;
        if (getImportantForAccessibility((View) r0) == 4) goto L10;
        r0 = r0.getParent();
        goto L7
    L10:
        setImportantForAccessibility(r3, 2);
        return;
    }

    public static void setWindowInsetsAnimationCallback(View r0, WindowInsetsAnimationCompat.Callback r1) {
        WindowInsetsAnimationCompat.setCallback(r0, r1);
    }

    @Deprecated
    public static void setX(View r0, float r1) {
        r0.setX(r1);
    }

    @Deprecated
    public static void setY(View r0, float r1) {
        r0.setY(r1);
    }

    public static void setZ(View r0, float r1) {
        Api21Impl.setZ(r0, r1);
    }

    public static boolean startDragAndDrop(View r2, ClipData r3, View.DragShadowBuilder r4, Object r5, int r6) {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api24Impl.startDragAndDrop(r2, r3, r4, r5, r6);
    L7:
        return r2.startDrag(r3, r4, r5, r6);
    }

    public static boolean startNestedScroll(View r1, int r2) {
        return Api21Impl.startNestedScroll(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean startNestedScroll(View r1, int r2, int r3) {
        if ((r1 instanceof NestedScrollingChild2) == true) goto L5;
        if (r3 == 0) goto L8;
        return false;
    L8:
        return startNestedScroll(r1, r2);
    L5:
        return ((NestedScrollingChild2) r1).startNestedScroll(r2, r3);
    }

    private static AccessibilityViewProperty<CharSequence> stateDescriptionProperty() {
        final int r1 = C0039R.id.tag_state_description;
        final Class<CharSequence> r2 = CharSequence.class;
        final int r3 = 64;
        final int r4 = 30;
        return new C01163(r1, r2, r3, r4);
    }

    public static void stopNestedScroll(View r0) {
        Api21Impl.stopNestedScroll(r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void stopNestedScroll(View r1, int r2) {
        if ((r1 instanceof NestedScrollingChild2) == false) goto L5;
        ((NestedScrollingChild2) r1).stopNestedScroll(r2);
        return;
    L5:
        if (r2 != 0) goto L9;
        stopNestedScroll(r1);
        return;
    }

    private static void tickleInvalidationFlag(View r2) {
        float r0 = r2.getTranslationY();
        r2.setTranslationY(1.0f + r0);
        r2.setTranslationY(r0);
    }

    public static void updateDragShadow(View r2, View.DragShadowBuilder r3) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api24Impl.updateDragShadow(r2, r3);
        return;
    }
}
