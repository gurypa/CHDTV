package androidx.core.view.accessibility;

import android.R;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.C0039R;
import androidx.core.os.BuildCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityNodeInfoCompat {
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";
    public static final String ACTION_ARGUMENT_PRESS_AND_HOLD_DURATION_MILLIS_INT = "android.view.accessibility.action.ARGUMENT_PRESS_AND_HOLD_DURATION_MILLIS_INT";
    public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
    public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COLLAPSE = 524288;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_DISMISS = 1048576;
    public static final int ACTION_EXPAND = 262144;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 2097152;
    private static final int BOOLEAN_PROPERTY_IS_HEADING = 2;
    private static final int BOOLEAN_PROPERTY_IS_SHOWING_HINT = 4;
    private static final int BOOLEAN_PROPERTY_IS_TEXT_ENTRY_KEY = 8;
    private static final String BOOLEAN_PROPERTY_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY";
    private static final int BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE = 1;
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH = "android.core.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH";
    public static final int EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH = 20000;
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX = "android.core.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX";
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY = "android.core.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_KEY";
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    private static final String HINT_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY";
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private static final String PANE_TITLE_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY";
    private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";
    private static final String SPANS_ACTION_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY";
    private static final String SPANS_END_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY";
    private static final String SPANS_FLAGS_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY";
    private static final String SPANS_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY";
    private static final String SPANS_START_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY";
    private static final String STATE_DESCRIPTION_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.STATE_DESCRIPTION_KEY";
    private static final String TOOLTIP_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY";
    private static final String UNIQUE_ID_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.UNIQUE_ID_KEY";
    private static int sClickableSpanId;
    private final AccessibilityNodeInfo mInfo;
    public int mParentVirtualDescendantId;
    private int mVirtualDescendantId;

    public static class AccessibilityActionCompat {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS = null;
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS = null;
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS = null;
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION = null;
        public static final AccessibilityActionCompat ACTION_CLICK = null;
        public static final AccessibilityActionCompat ACTION_COLLAPSE = null;
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK = null;
        public static final AccessibilityActionCompat ACTION_COPY = null;
        public static final AccessibilityActionCompat ACTION_CUT = null;
        public static final AccessibilityActionCompat ACTION_DISMISS = null;
        public static final AccessibilityActionCompat ACTION_DRAG_CANCEL = null;
        public static final AccessibilityActionCompat ACTION_DRAG_DROP = null;
        public static final AccessibilityActionCompat ACTION_DRAG_START = null;
        public static final AccessibilityActionCompat ACTION_EXPAND = null;
        public static final AccessibilityActionCompat ACTION_FOCUS = null;
        public static final AccessibilityActionCompat ACTION_HIDE_TOOLTIP = null;
        public static final AccessibilityActionCompat ACTION_IME_ENTER = null;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK = null;
        public static final AccessibilityActionCompat ACTION_MOVE_WINDOW = null;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY = null;
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT = null;
        public static final AccessibilityActionCompat ACTION_PAGE_DOWN = null;
        public static final AccessibilityActionCompat ACTION_PAGE_LEFT = null;
        public static final AccessibilityActionCompat ACTION_PAGE_RIGHT = null;
        public static final AccessibilityActionCompat ACTION_PAGE_UP = null;
        public static final AccessibilityActionCompat ACTION_PASTE = null;
        public static final AccessibilityActionCompat ACTION_PRESS_AND_HOLD = null;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = null;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION = null;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP = null;
        public static final AccessibilityActionCompat ACTION_SELECT = null;
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS = null;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION = null;
        public static final AccessibilityActionCompat ACTION_SET_TEXT = null;
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN = null;
        public static final AccessibilityActionCompat ACTION_SHOW_TEXT_SUGGESTIONS = null;
        public static final AccessibilityActionCompat ACTION_SHOW_TOOLTIP = null;
        private static final String TAG = "A11yActionCompat";
        final Object mAction;
        protected final AccessibilityViewCommand mCommand;
        private final int mId;
        private final Class<? extends AccessibilityViewCommand.CommandArguments> mViewCommandArgumentClass;

        static {
            ACTION_FOCUS = new AccessibilityActionCompat(1, null);
            ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
            ACTION_SELECT = new AccessibilityActionCompat(4, null);
            ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
            ACTION_CLICK = new AccessibilityActionCompat(16, null);
            ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
            ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
            ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
            ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, null, AccessibilityViewCommand.MoveAtGranularityArguments.class);
            ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, null, AccessibilityViewCommand.MoveAtGranularityArguments.class);
            ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, null, AccessibilityViewCommand.MoveHtmlArguments.class);
            ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, null, AccessibilityViewCommand.MoveHtmlArguments.class);
            ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
            ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
            ACTION_COPY = new AccessibilityActionCompat(16384, null);
            ACTION_PASTE = new AccessibilityActionCompat(32768, null);
            ACTION_CUT = new AccessibilityActionCompat(65536, null);
            ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, null, AccessibilityViewCommand.SetSelectionArguments.class);
            ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
            ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
            ACTION_DISMISS = new AccessibilityActionCompat(1048576, null);
            ACTION_SET_TEXT = new AccessibilityActionCompat(2097152, null, AccessibilityViewCommand.SetTextArguments.class);
            ACTION_SHOW_ON_SCREEN = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN, R.id.accessibilityActionShowOnScreen, null, null, null);
            ACTION_SCROLL_TO_POSITION = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION, R.id.accessibilityActionScrollToPosition, null, null, AccessibilityViewCommand.ScrollToPositionArguments.class);
            ACTION_SCROLL_UP = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP, R.id.accessibilityActionScrollUp, null, null, null);
            ACTION_SCROLL_LEFT = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT, R.id.accessibilityActionScrollLeft, null, null, null);
            ACTION_SCROLL_DOWN = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN, R.id.accessibilityActionScrollDown, null, null, null);
            ACTION_SCROLL_RIGHT = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT, R.id.accessibilityActionScrollRight, null, null, null);
            if (Build.VERSION.SDK_INT < 29) goto L5;
            AccessibilityNodeInfo.AccessibilityAction r5 = AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_UP;
        L6:
            ACTION_PAGE_UP = new AccessibilityActionCompat(r5, R.id.accessibilityActionPageUp, null, null, null);
            if (Build.VERSION.SDK_INT < 29) goto L9;
            AccessibilityNodeInfo.AccessibilityAction r6 = AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_DOWN;
        L10:
            ACTION_PAGE_DOWN = new AccessibilityActionCompat(r6, R.id.accessibilityActionPageDown, null, null, null);
            if (Build.VERSION.SDK_INT < 29) goto L13;
            AccessibilityNodeInfo.AccessibilityAction r7 = AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT;
        L14:
            ACTION_PAGE_LEFT = new AccessibilityActionCompat(r7, R.id.accessibilityActionPageLeft, null, null, null);
            if (Build.VERSION.SDK_INT < 29) goto L17;
            AccessibilityNodeInfo.AccessibilityAction r8 = AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT;
        L18:
            ACTION_PAGE_RIGHT = new AccessibilityActionCompat(r8, R.id.accessibilityActionPageRight, null, null, null);
            ACTION_CONTEXT_CLICK = new AccessibilityActionCompat(AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK, R.id.accessibilityActionContextClick, null, null, null);
            if (Build.VERSION.SDK_INT < 24) goto L21;
            AccessibilityNodeInfo.AccessibilityAction r10 = AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS;
        L22:
            ACTION_SET_PROGRESS = new AccessibilityActionCompat(r10, R.id.accessibilityActionSetProgress, null, null, AccessibilityViewCommand.SetProgressArguments.class);
            if (Build.VERSION.SDK_INT < 26) goto L25;
            AccessibilityNodeInfo.AccessibilityAction r4 = AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW;
        L26:
            ACTION_MOVE_WINDOW = new AccessibilityActionCompat(r4, R.id.accessibilityActionMoveWindow, null, null, AccessibilityViewCommand.MoveWindowArguments.class);
            if (Build.VERSION.SDK_INT < 28) goto L29;
            AccessibilityNodeInfo.AccessibilityAction r9 = AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TOOLTIP;
        L30:
            ACTION_SHOW_TOOLTIP = new AccessibilityActionCompat(r9, R.id.accessibilityActionShowTooltip, null, null, null);
            if (Build.VERSION.SDK_INT < 28) goto L33;
            AccessibilityNodeInfo.AccessibilityAction r11 = AccessibilityNodeInfo.AccessibilityAction.ACTION_HIDE_TOOLTIP;
        L34:
            ACTION_HIDE_TOOLTIP = new AccessibilityActionCompat(r11, R.id.accessibilityActionHideTooltip, null, null, null);
            if (Build.VERSION.SDK_INT < 30) goto L37;
            AccessibilityNodeInfo.AccessibilityAction r12 = AccessibilityNodeInfo.AccessibilityAction.ACTION_PRESS_AND_HOLD;
        L38:
            ACTION_PRESS_AND_HOLD = new AccessibilityActionCompat(r12, R.id.accessibilityActionPressAndHold, null, null, null);
            if (Build.VERSION.SDK_INT < 30) goto L41;
            AccessibilityNodeInfo.AccessibilityAction r13 = AccessibilityNodeInfo.AccessibilityAction.ACTION_IME_ENTER;
        L42:
            ACTION_IME_ENTER = new AccessibilityActionCompat(r13, R.id.accessibilityActionImeEnter, null, null, null);
            if (Build.VERSION.SDK_INT < 32) goto L45;
            AccessibilityNodeInfo.AccessibilityAction r14 = AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_START;
        L46:
            ACTION_DRAG_START = new AccessibilityActionCompat(r14, R.id.accessibilityActionDragStart, null, null, null);
            if (Build.VERSION.SDK_INT < 32) goto L49;
            AccessibilityNodeInfo.AccessibilityAction r15 = AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_DROP;
        L50:
            ACTION_DRAG_DROP = new AccessibilityActionCompat(r15, R.id.accessibilityActionDragDrop, null, null, null);
            if (Build.VERSION.SDK_INT < 32) goto L53;
            AccessibilityNodeInfo.AccessibilityAction r16 = AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_CANCEL;
        L54:
            ACTION_DRAG_CANCEL = new AccessibilityActionCompat(r16, R.id.accessibilityActionDragCancel, null, null, null);
            if (Build.VERSION.SDK_INT < 33) goto L57;
            AccessibilityNodeInfo.AccessibilityAction r17 = AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TEXT_SUGGESTIONS;
        L58:
            ACTION_SHOW_TEXT_SUGGESTIONS = new AccessibilityActionCompat(r17, R.id.accessibilityActionShowTextSuggestions, null, null, null);
            return;
        L57:
            r17 = null;
            goto L58
        L53:
            r16 = null;
            goto L54
        L49:
            r15 = null;
            goto L50
        L45:
            r14 = null;
            goto L46
        L41:
            r13 = null;
            goto L42
        L37:
            r12 = null;
            goto L38
        L33:
            r11 = null;
            goto L34
        L29:
            r9 = null;
            goto L30
        L25:
            r4 = null;
            goto L26
        L21:
            r10 = null;
            goto L22
        L17:
            r8 = null;
            goto L18
        L13:
            r7 = null;
            goto L14
        L9:
            r6 = null;
            goto L10
        L5:
            r5 = null;
            goto L6
        }

        public AccessibilityActionCompat(int r7, CharSequence r8) {
            this(null, r7, r8, null, null);
        }

        public AccessibilityActionCompat(int r7, CharSequence r8, AccessibilityViewCommand r9) {
            this(null, r7, r8, r9, null);
        }

        private AccessibilityActionCompat(int r7, CharSequence r8, Class<? extends AccessibilityViewCommand.CommandArguments> r9) {
            this(null, r7, r8, null, r9);
        }

        AccessibilityActionCompat(Object r7) {
            this(r7, 0, null, null, null);
        }

        AccessibilityActionCompat(Object r2, int r3, CharSequence r4, AccessibilityViewCommand r5, Class<? extends AccessibilityViewCommand.CommandArguments> r6) {
            this.mId = r3;
            this.mCommand = r5;
            if (r2 != null) goto L5;
            this.mAction = new AccessibilityNodeInfo.AccessibilityAction(r3, r4);
        L6:
            this.mViewCommandArgumentClass = r6;
            return;
        L5:
            this.mAction = r2;
            goto L6
        }

        public AccessibilityActionCompat createReplacementAction(CharSequence r7, AccessibilityViewCommand r8) {
            return new AccessibilityActionCompat(null, this.mId, r7, r8, this.mViewCommandArgumentClass);
        }

        public boolean equals(Object r5) {
            if (r5 != null) goto L6;
            return false;
        L6:
            if ((r5 instanceof AccessibilityActionCompat) == true) goto L8;
            return false;
        L8:
            AccessibilityActionCompat r1 = (AccessibilityActionCompat) r5;
            if (this.mAction != null) goto L14;
            if (r1.mAction == null) goto L16;
            return false;
        L16:
            return true;
        L14:
            if (this.mAction.equals(r1.mAction) == true) goto L18;
            return false;
        L18:
            return true;
        }

        public int getId() {
            return ((AccessibilityNodeInfo.AccessibilityAction) this.mAction).getId();
        }

        public CharSequence getLabel() {
            return ((AccessibilityNodeInfo.AccessibilityAction) this.mAction).getLabel();
        }

        public int hashCode() {
            if (this.mAction != null) goto L7;
            return 0;
        L7:
            return this.mAction.hashCode();
        }

        public boolean perform(View r6, Bundle r7) {
            if (this.mCommand == null) goto L16;
            AccessibilityViewCommand.CommandArguments r0 = null;
            if (this.mViewCommandArgumentClass == null) goto L15;
            r0 = this.mViewCommandArgumentClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);     // Catch: Exception -> L8
            r0.setBundle(r7);     // Catch: Exception -> L8
        L8:
            e = move-exception;
            if (this.mViewCommandArgumentClass != null) goto L12;
            String r2 = "null";
        L13:
            Log.e(TAG, "Failed to execute command with argument class ViewCommandArgument: " + r2, e);
            goto L15
        L12:
            r2 = this.mViewCommandArgumentClass.getName();
        L15:
            return this.mCommand.perform(r6, r0);
        L16:
            return false;
        }
    }

    private static class Api33Impl {
        private Api33Impl() {
        }

        public static AccessibilityNodeInfo.ExtraRenderingInfo getExtraRenderingInfo(AccessibilityNodeInfo r1) {
            return r1.getExtraRenderingInfo();
        }

        public static boolean isTextSelectable(AccessibilityNodeInfo r1) {
            return r1.isTextSelectable();
        }

        public static void setTextSelectable(AccessibilityNodeInfo r0, boolean r1) {
            r0.setTextSelectable(r1);
        }
    }

    public static class CollectionInfoCompat {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;

        CollectionInfoCompat(Object r1) {
            this.mInfo = r1;
        }

        public static CollectionInfoCompat obtain(int r2, int r3, boolean r4) {
            return new CollectionInfoCompat(AccessibilityNodeInfo.CollectionInfo.obtain(r2, r3, r4));
        }

        public static CollectionInfoCompat obtain(int r2, int r3, boolean r4, int r5) {
            return new CollectionInfoCompat(AccessibilityNodeInfo.CollectionInfo.obtain(r2, r3, r4, r5));
        }

        public int getColumnCount() {
            return ((AccessibilityNodeInfo.CollectionInfo) this.mInfo).getColumnCount();
        }

        public int getRowCount() {
            return ((AccessibilityNodeInfo.CollectionInfo) this.mInfo).getRowCount();
        }

        public int getSelectionMode() {
            return ((AccessibilityNodeInfo.CollectionInfo) this.mInfo).getSelectionMode();
        }

        public boolean isHierarchical() {
            return ((AccessibilityNodeInfo.CollectionInfo) this.mInfo).isHierarchical();
        }
    }

    public static class CollectionItemInfoCompat {
        final Object mInfo;

        CollectionItemInfoCompat(Object r1) {
            this.mInfo = r1;
        }

        public static CollectionItemInfoCompat obtain(int r2, int r3, int r4, int r5, boolean r6) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfo.CollectionItemInfo.obtain(r2, r3, r4, r5, r6));
        }

        public static CollectionItemInfoCompat obtain(int r2, int r3, int r4, int r5, boolean r6, boolean r7) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfo.CollectionItemInfo.obtain(r2, r3, r4, r5, r6, r7));
        }

        public int getColumnIndex() {
            return ((AccessibilityNodeInfo.CollectionItemInfo) this.mInfo).getColumnIndex();
        }

        public int getColumnSpan() {
            return ((AccessibilityNodeInfo.CollectionItemInfo) this.mInfo).getColumnSpan();
        }

        public int getRowIndex() {
            return ((AccessibilityNodeInfo.CollectionItemInfo) this.mInfo).getRowIndex();
        }

        public int getRowSpan() {
            return ((AccessibilityNodeInfo.CollectionItemInfo) this.mInfo).getRowSpan();
        }

        @Deprecated
        public boolean isHeading() {
            return ((AccessibilityNodeInfo.CollectionItemInfo) this.mInfo).isHeading();
        }

        public boolean isSelected() {
            return ((AccessibilityNodeInfo.CollectionItemInfo) this.mInfo).isSelected();
        }
    }

    public static class RangeInfoCompat {
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;
        final Object mInfo;

        RangeInfoCompat(Object r1) {
            this.mInfo = r1;
        }

        public static RangeInfoCompat obtain(int r2, float r3, float r4, float r5) {
            return new RangeInfoCompat(AccessibilityNodeInfo.RangeInfo.obtain(r2, r3, r4, r5));
        }

        public float getCurrent() {
            return ((AccessibilityNodeInfo.RangeInfo) this.mInfo).getCurrent();
        }

        public float getMax() {
            return ((AccessibilityNodeInfo.RangeInfo) this.mInfo).getMax();
        }

        public float getMin() {
            return ((AccessibilityNodeInfo.RangeInfo) this.mInfo).getMin();
        }

        public int getType() {
            return ((AccessibilityNodeInfo.RangeInfo) this.mInfo).getType();
        }
    }

    public static final class TouchDelegateInfoCompat {
        final AccessibilityNodeInfo.TouchDelegateInfo mInfo;

        TouchDelegateInfoCompat(AccessibilityNodeInfo.TouchDelegateInfo r1) {
            this.mInfo = r1;
        }

        public TouchDelegateInfoCompat(Map<Region, View> r3) {
            if (Build.VERSION.SDK_INT < 29) goto L5;
            this.mInfo = new AccessibilityNodeInfo.TouchDelegateInfo(r3);
            return;
        L5:
            this.mInfo = null;
        }

        public Region getRegionAt(int r3) {
            if (Build.VERSION.SDK_INT >= 29) goto L5;
            return null;
        L5:
            return this.mInfo.getRegionAt(r3);
        }

        public int getRegionCount() {
            if (Build.VERSION.SDK_INT >= 29) goto L5;
            return 0;
        L5:
            return this.mInfo.getRegionCount();
        }

        public AccessibilityNodeInfoCompat getTargetForRegion(Region r3) {
            if (Build.VERSION.SDK_INT < 29) goto L8;
            AccessibilityNodeInfo r0 = this.mInfo.getTargetForRegion(r3);
            if (r0 != null) goto L7;
            return null;
        L7:
            return AccessibilityNodeInfoCompat.wrap(r0);
        L8:
            return null;
        }
    }

    static {
        sClickableSpanId = 0;
    }

    private AccessibilityNodeInfoCompat(AccessibilityNodeInfo r2) {
        this.mParentVirtualDescendantId = -1;
        this.mVirtualDescendantId = -1;
        this.mInfo = r2;
    }

    @Deprecated
    public AccessibilityNodeInfoCompat(Object r2) {
        this.mParentVirtualDescendantId = -1;
        this.mVirtualDescendantId = -1;
        this.mInfo = (AccessibilityNodeInfo) r2;
    }

    private void addSpanLocationToExtras(ClickableSpan r3, Spanned r4, int r5) {
        extrasIntList(SPANS_START_KEY).add(Integer.valueOf(r4.getSpanStart(r3)));
        extrasIntList(SPANS_END_KEY).add(Integer.valueOf(r4.getSpanEnd(r3)));
        extrasIntList(SPANS_FLAGS_KEY).add(Integer.valueOf(r4.getSpanFlags(r3)));
        extrasIntList(SPANS_ID_KEY).add(Integer.valueOf(r5));
    }

    private void clearExtrasSpans() {
        this.mInfo.getExtras().remove(SPANS_START_KEY);
        this.mInfo.getExtras().remove(SPANS_END_KEY);
        this.mInfo.getExtras().remove(SPANS_FLAGS_KEY);
        this.mInfo.getExtras().remove(SPANS_ID_KEY);
    }

    private List<Integer> extrasIntList(String r3) {
        ArrayList<Integer> r0 = this.mInfo.getExtras().getIntegerArrayList(r3);
        if (r0 != null) goto L6;
        ArrayList<Integer> r1 = new ArrayList();
        this.mInfo.getExtras().putIntegerArrayList(r3, r1);
        return r1;
    L6:
        return r0;
    }

    private static String getActionSymbolicName(int r1) {
        switch(r1) {
            case 1: goto L85;
            case 2: goto L83;
            case 4: goto L81;
            case 8: goto L79;
            case 16: goto L77;
            case 32: goto L75;
            case 64: goto L73;
            case 128: goto L71;
            case 256: goto L69;
            case 512: goto L67;
            case 1024: goto L65;
            case 2048: goto L63;
            case 4096: goto L61;
            case 8192: goto L59;
            case 16384: goto L57;
            case 32768: goto L55;
            case 65536: goto L53;
            case 131072: goto L51;
            case 262144: goto L49;
            case 524288: goto L47;
            case 2097152: goto L45;
            case 16908342: goto L43;
            case 16908343: goto L41;
            case 16908344: goto L39;
            case 16908345: goto L37;
            case 16908346: goto L35;
            case 16908347: goto L33;
            case 16908348: goto L31;
            case 16908349: goto L29;
            case 16908354: goto L27;
            case 16908356: goto L25;
            case 16908357: goto L23;
            case 16908358: goto L21;
            case 16908359: goto L19;
            case 16908360: goto L17;
            case 16908361: goto L15;
            case 16908362: goto L13;
            case 16908372: goto L11;
            case 16908373: goto L9;
            case 16908374: goto L7;
            case 16908375: goto L5;
            default: goto L3;
        };
    L3:
        return "ACTION_UNKNOWN";
    L5:
        return "ACTION_DRAG_CANCEL";
    L7:
        return "ACTION_DRAG_DROP";
    L9:
        return "ACTION_DRAG_START";
    L11:
        return "ACTION_IME_ENTER";
    L13:
        return "ACTION_PRESS_AND_HOLD";
    L15:
        return "ACTION_PAGE_RIGHT";
    L17:
        return "ACTION_PAGE_LEFT";
    L19:
        return "ACTION_PAGE_DOWN";
    L21:
        return "ACTION_PAGE_UP";
    L23:
        return "ACTION_HIDE_TOOLTIP";
    L25:
        return "ACTION_SHOW_TOOLTIP";
    L27:
        return "ACTION_MOVE_WINDOW";
    L29:
        return "ACTION_SET_PROGRESS";
    L31:
        return "ACTION_CONTEXT_CLICK";
    L33:
        return "ACTION_SCROLL_RIGHT";
    L35:
        return "ACTION_SCROLL_DOWN";
    L37:
        return "ACTION_SCROLL_LEFT";
    L39:
        return "ACTION_SCROLL_UP";
    L41:
        return "ACTION_SCROLL_TO_POSITION";
    L43:
        return "ACTION_SHOW_ON_SCREEN";
    L45:
        return "ACTION_SET_TEXT";
    L47:
        return "ACTION_COLLAPSE";
    L49:
        return "ACTION_EXPAND";
    L51:
        return "ACTION_SET_SELECTION";
    L53:
        return "ACTION_CUT";
    L55:
        return "ACTION_PASTE";
    L57:
        return "ACTION_COPY";
    L59:
        return "ACTION_SCROLL_BACKWARD";
    L61:
        return "ACTION_SCROLL_FORWARD";
    L63:
        return "ACTION_PREVIOUS_HTML_ELEMENT";
    L65:
        return "ACTION_NEXT_HTML_ELEMENT";
    L67:
        return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
    L69:
        return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
    L71:
        return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
    L73:
        return "ACTION_ACCESSIBILITY_FOCUS";
    L75:
        return "ACTION_LONG_CLICK";
    L77:
        return "ACTION_CLICK";
    L79:
        return "ACTION_CLEAR_SELECTION";
    L81:
        return "ACTION_SELECT";
    L83:
        return "ACTION_CLEAR_FOCUS";
    L85:
        return "ACTION_FOCUS";
    }

    private boolean getBooleanProperty(int r4) {
        Bundle r0 = getExtras();
        if (r0 != null) goto L6;
        return false;
    L6:
        if ((r0.getInt(BOOLEAN_PROPERTY_KEY, 0) & r4) != r4) goto L9;
        return true;
    L9:
        return false;
    }

    public static ClickableSpan[] getClickableSpans(CharSequence r4) {
        if ((r4 instanceof Spanned) == true) goto L5;
        return null;
    L5:
        return (ClickableSpan[]) ((Spanned) r4).getSpans(0, r4.length(), ClickableSpan.class);
    }

    private SparseArray<WeakReference<ClickableSpan>> getOrCreateSpansFromViewTags(View r3) {
        SparseArray<WeakReference<ClickableSpan>> r0 = getSpansFromViewTags(r3);
        if (r0 != null) goto L6;
        SparseArray<WeakReference<ClickableSpan>> r1 = new SparseArray();
        r3.setTag(C0039R.id.tag_accessibility_clickable_spans, r1);
        return r1;
    L6:
        return r0;
    }

    private SparseArray<WeakReference<ClickableSpan>> getSpansFromViewTags(View r2) {
        return (SparseArray) r2.getTag(C0039R.id.tag_accessibility_clickable_spans);
    }

    private boolean hasSpans() {
        return !extrasIntList(SPANS_START_KEY).isEmpty();
    }

    private int idForClickableSpan(ClickableSpan r4, SparseArray<WeakReference<ClickableSpan>> r5) {
        if (r5 == null) goto L11;
        int r0 = 0;
    L5:
        if (r0 >= r5.size()) goto L11;
        if (r4.equals(r5.valueAt(r0).get()) == true) goto L9;
        r0 = r0 + 1;
        goto L5
    L9:
        return r5.keyAt(r0);
    L11:
        int r1 = sClickableSpanId;
        sClickableSpanId = r1 + 1;
        return r1;
    }

    public static AccessibilityNodeInfoCompat obtain() {
        return wrap(AccessibilityNodeInfo.obtain());
    }

    public static AccessibilityNodeInfoCompat obtain(View r1) {
        return wrap(AccessibilityNodeInfo.obtain(r1));
    }

    public static AccessibilityNodeInfoCompat obtain(View r1, int r2) {
        return wrapNonNullInstance(AccessibilityNodeInfo.obtain(r1, r2));
    }

    public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat r1) {
        return wrap(AccessibilityNodeInfo.obtain(r1.mInfo));
    }

    private void removeCollectedSpans(View r5) {
        SparseArray<WeakReference<ClickableSpan>> r0 = getSpansFromViewTags(r5);
        if (r0 == null) goto L15;
        ArrayList r1 = new ArrayList();
        int r2 = 0;
    L6:
        if (r2 >= r0.size()) goto L11;
        if (r0.valueAt(r2).get() != null) goto L10;
        r1.add(Integer.valueOf(r2));
    L10:
        r2 = r2 + 1;
        goto L6
    L11:
        int r3 = 0;
    L13:
        if (r3 >= r1.size()) goto L20;
        r0.remove(((Integer) r1.get(r3)).intValue());
        r3 = r3 + 1;
        goto L13
    L20:
        return;
    }

    private void setBooleanProperty(int r6, boolean r7) {
        Bundle r0 = getExtras();
        if (r0 == null) goto L9;
        int r2 = 0;
        int r3 = r0.getInt(BOOLEAN_PROPERTY_KEY, 0) & (~r6);
        if (r7 == false) goto L7;
        r2 = r6;
    L7:
        r0.putInt(BOOLEAN_PROPERTY_KEY, r2 | r3);
        return;
    }

    public static AccessibilityNodeInfoCompat wrap(AccessibilityNodeInfo r1) {
        return new AccessibilityNodeInfoCompat(r1);
    }

    static AccessibilityNodeInfoCompat wrapNonNullInstance(Object r1) {
        if (r1 != null) goto L4;
        return null;
    L4:
        return new AccessibilityNodeInfoCompat(r1);
    }

    public void addAction(int r2) {
        this.mInfo.addAction(r2);
    }

    public void addAction(AccessibilityActionCompat r3) {
        this.mInfo.addAction((AccessibilityNodeInfo.AccessibilityAction) r3.mAction);
    }

    public void addChild(View r2) {
        this.mInfo.addChild(r2);
    }

    public void addChild(View r2, int r3) {
        this.mInfo.addChild(r2, r3);
    }

    public void addSpansToExtras(CharSequence r7, View r8) {
        if (Build.VERSION.SDK_INT >= 26) goto L13;
        clearExtrasSpans();
        removeCollectedSpans(r8);
        ClickableSpan[] r0 = getClickableSpans(r7);
        if (r0 != null) goto L7;
        return;
    L7:
        if (r0.length <= 0) goto L17;
        getExtras().putInt(SPANS_ACTION_ID_KEY, C0039R.id.accessibility_action_clickable_span);
        SparseArray<WeakReference<ClickableSpan>> r1 = getOrCreateSpansFromViewTags(r8);
        int r2 = 0;
    L9:
        if (r0 == null) goto L18;
        if (r2 >= r0.length) goto L19;
        int r3 = idForClickableSpan(r0[r2], r1);
        r1.put(r3, new WeakReference(r0[r2]));
        addSpanLocationToExtras(r0[r2], (Spanned) r7, r3);
        r2 = r2 + 1;
        goto L9
    L19:
        return;
    L18:
        return;
    L17:
        return;
    }

    public boolean canOpenPopup() {
        return this.mInfo.canOpenPopup();
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L9;
        return false;
    L9:
        if ((r6 instanceof AccessibilityNodeInfoCompat) == true) goto L11;
        return false;
    L11:
        AccessibilityNodeInfoCompat r2 = (AccessibilityNodeInfoCompat) r6;
        if (this.mInfo != null) goto L17;
        if (r2.mInfo == null) goto L20;
        return false;
    L20:
        if (this.mVirtualDescendantId == r2.mVirtualDescendantId) goto L23;
        return false;
    L23:
        if (this.mParentVirtualDescendantId == r2.mParentVirtualDescendantId) goto L25;
        return false;
    L25:
        return true;
    L17:
        if (this.mInfo.equals(r2.mInfo) == true) goto L20;
        return false;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String r7) {
        ArrayList r0 = new ArrayList();
        List<AccessibilityNodeInfo> r1 = this.mInfo.findAccessibilityNodeInfosByText(r7);
        int r2 = r1.size();
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0.add(wrap(r1.get(r3)));
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId(String r6) {
        List<AccessibilityNodeInfo> r0 = this.mInfo.findAccessibilityNodeInfosByViewId(r6);
        ArrayList r1 = new ArrayList();
        Iterator<AccessibilityNodeInfo> r2 = r0.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r1.add(wrap(r2.next()));
        goto L4
    L6:
        return r1;
    }

    public AccessibilityNodeInfoCompat findFocus(int r2) {
        return wrapNonNullInstance(this.mInfo.findFocus(r2));
    }

    public AccessibilityNodeInfoCompat focusSearch(int r2) {
        return wrapNonNullInstance(this.mInfo.focusSearch(r2));
    }

    public List<AccessibilityActionCompat> getActionList() {
        List<AccessibilityNodeInfo.AccessibilityAction> r0 = this.mInfo.getActionList();
        if (r0 == null) goto L9;
        ArrayList r1 = new ArrayList();
        int r2 = r0.size();
        int r3 = 0;
    L5:
        if (r3 >= r2) goto L7;
        r1.add(new AccessibilityActionCompat(r0.get(r3)));
        r3 = r3 + 1;
        goto L5
    L7:
        return r1;
    L9:
        return Collections.emptyList();
    }

    @Deprecated
    public int getActions() {
        return this.mInfo.getActions();
    }

    public List<String> getAvailableExtraData() {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return this.mInfo.getAvailableExtraData();
    L7:
        return Collections.emptyList();
    }

    @Deprecated
    public void getBoundsInParent(Rect r2) {
        this.mInfo.getBoundsInParent(r2);
    }

    public void getBoundsInScreen(Rect r2) {
        this.mInfo.getBoundsInScreen(r2);
    }

    public AccessibilityNodeInfoCompat getChild(int r2) {
        return wrapNonNullInstance(this.mInfo.getChild(r2));
    }

    public int getChildCount() {
        return this.mInfo.getChildCount();
    }

    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }

    public CollectionInfoCompat getCollectionInfo() {
        AccessibilityNodeInfo.CollectionInfo r0 = this.mInfo.getCollectionInfo();
        if (r0 != null) goto L5;
        return null;
    L5:
        return new CollectionInfoCompat(r0);
    }

    public CollectionItemInfoCompat getCollectionItemInfo() {
        AccessibilityNodeInfo.CollectionItemInfo r0 = this.mInfo.getCollectionItemInfo();
        if (r0 != null) goto L5;
        return null;
    L5:
        return new CollectionItemInfoCompat(r0);
    }

    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }

    public int getDrawingOrder() {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return 0;
    L5:
        return this.mInfo.getDrawingOrder();
    }

    public CharSequence getError() {
        return this.mInfo.getError();
    }

    public AccessibilityNodeInfo.ExtraRenderingInfo getExtraRenderingInfo() {
        if (Build.VERSION.SDK_INT >= 33) goto L5;
        return null;
    L5:
        return Api33Impl.getExtraRenderingInfo(this.mInfo);
    }

    public Bundle getExtras() {
        return this.mInfo.getExtras();
    }

    public CharSequence getHintText() {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return this.mInfo.getHintText();
    L7:
        return this.mInfo.getExtras().getCharSequence(HINT_TEXT_KEY);
    }

    @Deprecated
    public Object getInfo() {
        return this.mInfo;
    }

    public int getInputType() {
        return this.mInfo.getInputType();
    }

    public AccessibilityNodeInfoCompat getLabelFor() {
        return wrapNonNullInstance(this.mInfo.getLabelFor());
    }

    public AccessibilityNodeInfoCompat getLabeledBy() {
        return wrapNonNullInstance(this.mInfo.getLabeledBy());
    }

    public int getLiveRegion() {
        return this.mInfo.getLiveRegion();
    }

    public int getMaxTextLength() {
        return this.mInfo.getMaxTextLength();
    }

    public int getMovementGranularities() {
        return this.mInfo.getMovementGranularities();
    }

    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }

    public CharSequence getPaneTitle() {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return this.mInfo.getPaneTitle();
    L7:
        return this.mInfo.getExtras().getCharSequence(PANE_TITLE_KEY);
    }

    public AccessibilityNodeInfoCompat getParent() {
        return wrapNonNullInstance(this.mInfo.getParent());
    }

    public RangeInfoCompat getRangeInfo() {
        AccessibilityNodeInfo.RangeInfo r0 = this.mInfo.getRangeInfo();
        if (r0 != null) goto L5;
        return null;
    L5:
        return new RangeInfoCompat(r0);
    }

    public CharSequence getRoleDescription() {
        return this.mInfo.getExtras().getCharSequence(ROLE_DESCRIPTION_KEY);
    }

    public CharSequence getStateDescription() {
        if (BuildCompat.isAtLeastR() == false) goto L7;
        return this.mInfo.getStateDescription();
    L7:
        return this.mInfo.getExtras().getCharSequence(STATE_DESCRIPTION_KEY);
    }

    public CharSequence getText() {
        if (hasSpans() == false) goto L10;
        List<Integer> r0 = extrasIntList(SPANS_START_KEY);
        List<Integer> r1 = extrasIntList(SPANS_END_KEY);
        List<Integer> r2 = extrasIntList(SPANS_FLAGS_KEY);
        List<Integer> r3 = extrasIntList(SPANS_ID_KEY);
        SpannableString r4 = new SpannableString(TextUtils.substring(this.mInfo.getText(), 0, this.mInfo.getText().length()));
        int r5 = 0;
    L6:
        if (r5 >= r0.size()) goto L8;
        r4.setSpan(new AccessibilityClickableSpanCompat(r3.get(r5).intValue(), this, getExtras().getInt(SPANS_ACTION_ID_KEY)), r0.get(r5).intValue(), r1.get(r5).intValue(), r2.get(r5).intValue());
        r5 = r5 + 1;
        goto L6
    L8:
        return r4;
    L10:
        return this.mInfo.getText();
    }

    public int getTextSelectionEnd() {
        return this.mInfo.getTextSelectionEnd();
    }

    public int getTextSelectionStart() {
        return this.mInfo.getTextSelectionStart();
    }

    public CharSequence getTooltipText() {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return this.mInfo.getTooltipText();
    L7:
        return this.mInfo.getExtras().getCharSequence(TOOLTIP_TEXT_KEY);
    }

    public TouchDelegateInfoCompat getTouchDelegateInfo() {
        if (Build.VERSION.SDK_INT < 29) goto L8;
        AccessibilityNodeInfo.TouchDelegateInfo r0 = this.mInfo.getTouchDelegateInfo();
        if (r0 != null) goto L7;
        return null;
    L7:
        return new TouchDelegateInfoCompat(r0);
    L8:
        return null;
    }

    public AccessibilityNodeInfoCompat getTraversalAfter() {
        return wrapNonNullInstance(this.mInfo.getTraversalAfter());
    }

    public AccessibilityNodeInfoCompat getTraversalBefore() {
        return wrapNonNullInstance(this.mInfo.getTraversalBefore());
    }

    public String getUniqueId() {
        if (BuildCompat.isAtLeastT() == false) goto L7;
        return this.mInfo.getUniqueId();
    L7:
        return this.mInfo.getExtras().getString(UNIQUE_ID_KEY);
    }

    public String getViewIdResourceName() {
        return this.mInfo.getViewIdResourceName();
    }

    public AccessibilityWindowInfoCompat getWindow() {
        return AccessibilityWindowInfoCompat.wrapNonNullInstance(this.mInfo.getWindow());
    }

    public int getWindowId() {
        return this.mInfo.getWindowId();
    }

    public int hashCode() {
        if (this.mInfo != null) goto L6;
        return 0;
    L6:
        return this.mInfo.hashCode();
    }

    public boolean isAccessibilityFocused() {
        return this.mInfo.isAccessibilityFocused();
    }

    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }

    public boolean isChecked() {
        return this.mInfo.isChecked();
    }

    public boolean isClickable() {
        return this.mInfo.isClickable();
    }

    public boolean isContentInvalid() {
        return this.mInfo.isContentInvalid();
    }

    public boolean isContextClickable() {
        return this.mInfo.isContextClickable();
    }

    public boolean isDismissable() {
        return this.mInfo.isDismissable();
    }

    public boolean isEditable() {
        return this.mInfo.isEditable();
    }

    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }

    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }

    public boolean isFocused() {
        return this.mInfo.isFocused();
    }

    public boolean isHeading() {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return this.mInfo.isHeading();
    L7:
        if (getBooleanProperty(2) == false) goto L9;
        return true;
    L9:
        CollectionItemInfoCompat r0 = getCollectionItemInfo();
        if (r0 != null) goto L12;
    L14:
        return false;
    L12:
        if (r0.isHeading() == false) goto L14;
        return true;
    }

    public boolean isImportantForAccessibility() {
        if (Build.VERSION.SDK_INT >= 24) goto L5;
        return true;
    L5:
        return this.mInfo.isImportantForAccessibility();
    }

    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }

    public boolean isMultiLine() {
        return this.mInfo.isMultiLine();
    }

    public boolean isPassword() {
        return this.mInfo.isPassword();
    }

    public boolean isScreenReaderFocusable() {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return this.mInfo.isScreenReaderFocusable();
    L7:
        return getBooleanProperty(1);
    }

    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }

    public boolean isSelected() {
        return this.mInfo.isSelected();
    }

    public boolean isShowingHintText() {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return this.mInfo.isShowingHintText();
    L7:
        return getBooleanProperty(4);
    }

    public boolean isTextEntryKey() {
        if (Build.VERSION.SDK_INT < 29) goto L7;
        return this.mInfo.isTextEntryKey();
    L7:
        return getBooleanProperty(8);
    }

    public boolean isTextSelectable() {
        if (Build.VERSION.SDK_INT >= 33) goto L5;
        return false;
    L5:
        return Api33Impl.isTextSelectable(this.mInfo);
    }

    public boolean isVisibleToUser() {
        return this.mInfo.isVisibleToUser();
    }

    public boolean performAction(int r2) {
        return this.mInfo.performAction(r2);
    }

    public boolean performAction(int r2, Bundle r3) {
        return this.mInfo.performAction(r2, r3);
    }

    public void recycle() {
        this.mInfo.recycle();
    }

    public boolean refresh() {
        return this.mInfo.refresh();
    }

    public boolean removeAction(AccessibilityActionCompat r3) {
        return this.mInfo.removeAction((AccessibilityNodeInfo.AccessibilityAction) r3.mAction);
    }

    public boolean removeChild(View r2) {
        return this.mInfo.removeChild(r2);
    }

    public boolean removeChild(View r2, int r3) {
        return this.mInfo.removeChild(r2, r3);
    }

    public void setAccessibilityFocused(boolean r2) {
        this.mInfo.setAccessibilityFocused(r2);
    }

    public void setAvailableExtraData(List<String> r3) {
        if (Build.VERSION.SDK_INT < 26) goto L6;
        this.mInfo.setAvailableExtraData(r3);
        return;
    }

    @Deprecated
    public void setBoundsInParent(Rect r2) {
        this.mInfo.setBoundsInParent(r2);
    }

    public void setBoundsInScreen(Rect r2) {
        this.mInfo.setBoundsInScreen(r2);
    }

    public void setCanOpenPopup(boolean r2) {
        this.mInfo.setCanOpenPopup(r2);
    }

    public void setCheckable(boolean r2) {
        this.mInfo.setCheckable(r2);
    }

    public void setChecked(boolean r2) {
        this.mInfo.setChecked(r2);
    }

    public void setClassName(CharSequence r2) {
        this.mInfo.setClassName(r2);
    }

    public void setClickable(boolean r2) {
        this.mInfo.setClickable(r2);
    }

    public void setCollectionInfo(Object r3) {
        AccessibilityNodeInfo r0 = this.mInfo;
        if (r3 != null) goto L5;
        AccessibilityNodeInfo.CollectionInfo r1 = null;
    L6:
        r0.setCollectionInfo(r1);
        return;
    L5:
        r1 = (AccessibilityNodeInfo.CollectionInfo) ((CollectionInfoCompat) r3).mInfo;
        goto L6
    }

    public void setCollectionItemInfo(Object r3) {
        AccessibilityNodeInfo r0 = this.mInfo;
        if (r3 != null) goto L5;
        AccessibilityNodeInfo.CollectionItemInfo r1 = null;
    L6:
        r0.setCollectionItemInfo(r1);
        return;
    L5:
        r1 = (AccessibilityNodeInfo.CollectionItemInfo) ((CollectionItemInfoCompat) r3).mInfo;
        goto L6
    }

    public void setContentDescription(CharSequence r2) {
        this.mInfo.setContentDescription(r2);
    }

    public void setContentInvalid(boolean r2) {
        this.mInfo.setContentInvalid(r2);
    }

    public void setContextClickable(boolean r2) {
        this.mInfo.setContextClickable(r2);
    }

    public void setDismissable(boolean r2) {
        this.mInfo.setDismissable(r2);
    }

    public void setDrawingOrder(int r3) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        this.mInfo.setDrawingOrder(r3);
        return;
    }

    public void setEditable(boolean r2) {
        this.mInfo.setEditable(r2);
    }

    public void setEnabled(boolean r2) {
        this.mInfo.setEnabled(r2);
    }

    public void setError(CharSequence r2) {
        this.mInfo.setError(r2);
    }

    public void setFocusable(boolean r2) {
        this.mInfo.setFocusable(r2);
    }

    public void setFocused(boolean r2) {
        this.mInfo.setFocused(r2);
    }

    public void setHeading(boolean r3) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        this.mInfo.setHeading(r3);
        return;
    L5:
        setBooleanProperty(2, r3);
    }

    public void setHintText(CharSequence r3) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mInfo.setHintText(r3);
        return;
    L5:
        this.mInfo.getExtras().putCharSequence(HINT_TEXT_KEY, r3);
    }

    public void setImportantForAccessibility(boolean r3) {
        if (Build.VERSION.SDK_INT < 24) goto L6;
        this.mInfo.setImportantForAccessibility(r3);
        return;
    }

    public void setInputType(int r2) {
        this.mInfo.setInputType(r2);
    }

    public void setLabelFor(View r2) {
        this.mInfo.setLabelFor(r2);
    }

    public void setLabelFor(View r2, int r3) {
        this.mInfo.setLabelFor(r2, r3);
    }

    public void setLabeledBy(View r2) {
        this.mInfo.setLabeledBy(r2);
    }

    public void setLabeledBy(View r2, int r3) {
        this.mInfo.setLabeledBy(r2, r3);
    }

    public void setLiveRegion(int r2) {
        this.mInfo.setLiveRegion(r2);
    }

    public void setLongClickable(boolean r2) {
        this.mInfo.setLongClickable(r2);
    }

    public void setMaxTextLength(int r2) {
        this.mInfo.setMaxTextLength(r2);
    }

    public void setMovementGranularities(int r2) {
        this.mInfo.setMovementGranularities(r2);
    }

    public void setMultiLine(boolean r2) {
        this.mInfo.setMultiLine(r2);
    }

    public void setPackageName(CharSequence r2) {
        this.mInfo.setPackageName(r2);
    }

    public void setPaneTitle(CharSequence r3) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        this.mInfo.setPaneTitle(r3);
        return;
    L5:
        this.mInfo.getExtras().putCharSequence(PANE_TITLE_KEY, r3);
    }

    public void setParent(View r2) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo.setParent(r2);
    }

    public void setParent(View r2, int r3) {
        this.mParentVirtualDescendantId = r3;
        this.mInfo.setParent(r2, r3);
    }

    public void setPassword(boolean r2) {
        this.mInfo.setPassword(r2);
    }

    public void setRangeInfo(RangeInfoCompat r3) {
        this.mInfo.setRangeInfo((AccessibilityNodeInfo.RangeInfo) r3.mInfo);
    }

    public void setRoleDescription(CharSequence r3) {
        this.mInfo.getExtras().putCharSequence(ROLE_DESCRIPTION_KEY, r3);
    }

    public void setScreenReaderFocusable(boolean r3) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        this.mInfo.setScreenReaderFocusable(r3);
        return;
    L5:
        setBooleanProperty(1, r3);
    }

    public void setScrollable(boolean r2) {
        this.mInfo.setScrollable(r2);
    }

    public void setSelected(boolean r2) {
        this.mInfo.setSelected(r2);
    }

    public void setShowingHintText(boolean r3) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mInfo.setShowingHintText(r3);
        return;
    L5:
        setBooleanProperty(4, r3);
    }

    public void setSource(View r2) {
        this.mVirtualDescendantId = -1;
        this.mInfo.setSource(r2);
    }

    public void setSource(View r2, int r3) {
        this.mVirtualDescendantId = r3;
        this.mInfo.setSource(r2, r3);
    }

    public void setStateDescription(CharSequence r3) {
        if (BuildCompat.isAtLeastR() == false) goto L5;
        this.mInfo.setStateDescription(r3);
        return;
    L5:
        this.mInfo.getExtras().putCharSequence(STATE_DESCRIPTION_KEY, r3);
    }

    public void setText(CharSequence r2) {
        this.mInfo.setText(r2);
    }

    public void setTextEntryKey(boolean r3) {
        if (Build.VERSION.SDK_INT < 29) goto L5;
        this.mInfo.setTextEntryKey(r3);
        return;
    L5:
        setBooleanProperty(8, r3);
    }

    public void setTextSelectable(boolean r3) {
        if (Build.VERSION.SDK_INT < 33) goto L6;
        Api33Impl.setTextSelectable(this.mInfo, r3);
        return;
    }

    public void setTextSelection(int r2, int r3) {
        this.mInfo.setTextSelection(r2, r3);
    }

    public void setTooltipText(CharSequence r3) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        this.mInfo.setTooltipText(r3);
        return;
    L5:
        this.mInfo.getExtras().putCharSequence(TOOLTIP_TEXT_KEY, r3);
    }

    public void setTouchDelegateInfo(TouchDelegateInfoCompat r3) {
        if (Build.VERSION.SDK_INT < 29) goto L6;
        this.mInfo.setTouchDelegateInfo(r3.mInfo);
        return;
    }

    public void setTraversalAfter(View r2) {
        this.mInfo.setTraversalAfter(r2);
    }

    public void setTraversalAfter(View r2, int r3) {
        this.mInfo.setTraversalAfter(r2, r3);
    }

    public void setTraversalBefore(View r2) {
        this.mInfo.setTraversalBefore(r2);
    }

    public void setTraversalBefore(View r2, int r3) {
        this.mInfo.setTraversalBefore(r2, r3);
    }

    public void setUniqueId(String r3) {
        if (BuildCompat.isAtLeastT() == false) goto L5;
        this.mInfo.setUniqueId(r3);
        return;
    L5:
        this.mInfo.getExtras().putString(UNIQUE_ID_KEY, r3);
    }

    public void setViewIdResourceName(String r2) {
        this.mInfo.setViewIdResourceName(r2);
    }

    public void setVisibleToUser(boolean r2) {
        this.mInfo.setVisibleToUser(r2);
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        r0.append(super.toString());
        Rect r1 = new Rect();
        getBoundsInParent(r1);
        r0.append("; boundsInParent: " + r1);
        getBoundsInScreen(r1);
        r0.append("; boundsInScreen: " + r1);
        r0.append("; packageName: ").append(getPackageName());
        r0.append("; className: ").append(getClassName());
        r0.append("; text: ").append(getText());
        r0.append("; contentDescription: ").append(getContentDescription());
        r0.append("; viewId: ").append(getViewIdResourceName());
        r0.append("; uniqueId: ").append(getUniqueId());
        r0.append("; checkable: ").append(isCheckable());
        r0.append("; checked: ").append(isChecked());
        r0.append("; focusable: ").append(isFocusable());
        r0.append("; focused: ").append(isFocused());
        r0.append("; selected: ").append(isSelected());
        r0.append("; clickable: ").append(isClickable());
        r0.append("; longClickable: ").append(isLongClickable());
        r0.append("; enabled: ").append(isEnabled());
        r0.append("; password: ").append(isPassword());
        r0.append("; scrollable: " + isScrollable());
        r0.append("; [");
        List<AccessibilityActionCompat> r2 = getActionList();
        int r3 = 0;
    L4:
        if (r3 >= r2.size()) goto L14;
        AccessibilityActionCompat r4 = r2.get(r3);
        String r5 = getActionSymbolicName(r4.getId());
        if (r5.equals("ACTION_UNKNOWN") == true) goto L8;
    L10:
        r0.append(r5);
        if (r3 == (r2.size() - 1)) goto L13;
        r0.append(", ");
    L13:
        r3 = r3 + 1;
        goto L4
    L8:
        if (r4.getLabel() == null) goto L10;
        r5 = r4.getLabel().toString();
        goto L10
    L14:
        r0.append("]");
        return r0.toString();
    }

    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }
}
