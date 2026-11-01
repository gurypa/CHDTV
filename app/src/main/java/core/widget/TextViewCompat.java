package androidx.core.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.text.Editable;
import android.text.PrecomputedText;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class TextViewCompat {
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompat";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;

    static class Api16Impl {
        private Api16Impl() {
        }

        static boolean getIncludeFontPadding(TextView r1) {
            return r1.getIncludeFontPadding();
        }

        static int getMaxLines(TextView r1) {
            return r1.getMaxLines();
        }

        static int getMinLines(TextView r1) {
            return r1.getMinLines();
        }
    }

    static class Api17Impl {
        private Api17Impl() {
        }

        static Drawable[] getCompoundDrawablesRelative(TextView r1) {
            return r1.getCompoundDrawablesRelative();
        }

        static int getLayoutDirection(View r1) {
            return r1.getLayoutDirection();
        }

        static int getTextDirection(View r1) {
            return r1.getTextDirection();
        }

        static Locale getTextLocale(TextView r1) {
            return r1.getTextLocale();
        }

        static void setCompoundDrawablesRelative(TextView r0, Drawable r1, Drawable r2, Drawable r3, Drawable r4) {
            r0.setCompoundDrawablesRelative(r1, r2, r3, r4);
        }

        static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView r0, int r1, int r2, int r3, int r4) {
            r0.setCompoundDrawablesRelativeWithIntrinsicBounds(r1, r2, r3, r4);
        }

        static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView r0, Drawable r1, Drawable r2, Drawable r3, Drawable r4) {
            r0.setCompoundDrawablesRelativeWithIntrinsicBounds(r1, r2, r3, r4);
        }

        static void setTextDirection(View r0, int r1) {
            r0.setTextDirection(r1);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static int getBreakStrategy(TextView r1) {
            return r1.getBreakStrategy();
        }

        static ColorStateList getCompoundDrawableTintList(TextView r1) {
            return r1.getCompoundDrawableTintList();
        }

        static PorterDuff.Mode getCompoundDrawableTintMode(TextView r1) {
            return r1.getCompoundDrawableTintMode();
        }

        static int getHyphenationFrequency(TextView r1) {
            return r1.getHyphenationFrequency();
        }

        static void setBreakStrategy(TextView r0, int r1) {
            r0.setBreakStrategy(r1);
        }

        static void setCompoundDrawableTintList(TextView r0, ColorStateList r1) {
            r0.setCompoundDrawableTintList(r1);
        }

        static void setCompoundDrawableTintMode(TextView r0, PorterDuff.Mode r1) {
            r0.setCompoundDrawableTintMode(r1);
        }

        static void setHyphenationFrequency(TextView r0, int r1) {
            r0.setHyphenationFrequency(r1);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static DecimalFormatSymbols getInstance(Locale r1) {
            return DecimalFormatSymbols.getInstance(r1);
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static int getAutoSizeMaxTextSize(TextView r1) {
            return r1.getAutoSizeMaxTextSize();
        }

        static int getAutoSizeMinTextSize(TextView r1) {
            return r1.getAutoSizeMinTextSize();
        }

        static int getAutoSizeStepGranularity(TextView r1) {
            return r1.getAutoSizeStepGranularity();
        }

        static int[] getAutoSizeTextAvailableSizes(TextView r1) {
            return r1.getAutoSizeTextAvailableSizes();
        }

        static int getAutoSizeTextType(TextView r1) {
            return r1.getAutoSizeTextType();
        }

        static void setAutoSizeTextTypeUniformWithConfiguration(TextView r0, int r1, int r2, int r3, int r4) {
            r0.setAutoSizeTextTypeUniformWithConfiguration(r1, r2, r3, r4);
        }

        static void setAutoSizeTextTypeUniformWithPresetSizes(TextView r0, int[] r1, int r2) {
            r0.setAutoSizeTextTypeUniformWithPresetSizes(r1, r2);
        }

        static void setAutoSizeTextTypeWithDefaults(TextView r0, int r1) {
            r0.setAutoSizeTextTypeWithDefaults(r1);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static String[] getDigitStrings(DecimalFormatSymbols r1) {
            return r1.getDigitStrings();
        }

        static PrecomputedText.Params getTextMetricsParams(TextView r1) {
            return r1.getTextMetricsParams();
        }

        static void setFirstBaselineToTopHeight(TextView r0, int r1) {
            r0.setFirstBaselineToTopHeight(r1);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSizeTextType {
    }

    private static class OreoCallback implements ActionMode.Callback {
        private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
        private final ActionMode.Callback mCallback;
        private boolean mCanUseMenuBuilderReferences;
        private boolean mInitializedMenuBuilderReferences;
        private Class<?> mMenuBuilderClass;
        private Method mMenuBuilderRemoveItemAtMethod;
        private final TextView mTextView;

        OreoCallback(ActionMode.Callback r2, TextView r3) {
            this.mCallback = r2;
            this.mTextView = r3;
            this.mInitializedMenuBuilderReferences = false;
        }

        private Intent createProcessTextIntent() {
            return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
        }

        private Intent createProcessTextIntentForResolveInfo(ResolveInfo r4, TextView r5) {
            return createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", !isEditable(r5)).setClassName(r4.activityInfo.packageName, r4.activityInfo.name);
        }

        private List<ResolveInfo> getSupportedActivities(Context r7, PackageManager r8) {
            ArrayList r0 = new ArrayList();
            if ((r7 instanceof Activity) == true) goto L5;
            return r0;
        L5:
            Iterator<ResolveInfo> r3 = r8.queryIntentActivities(createProcessTextIntent(), 0).iterator();
        L7:
            if (r3.hasNext() == false) goto L12;
            ResolveInfo r4 = r3.next();
            if (isSupportedActivity(r4, r7) == false) goto L7;
            r0.add(r4);
            goto L7
        L12:
            return r0;
        }

        private boolean isEditable(TextView r2) {
            if ((r2 instanceof Editable) == true) goto L5;
        L9:
            return false;
        L5:
            if (r2.onCheckIsTextEditor() == false) goto L9;
            if (r2.isEnabled() == false) goto L9;
            return true;
        }

        private boolean isSupportedActivity(ResolveInfo r4, Context r5) {
            if (r5.getPackageName().equals(r4.activityInfo.packageName) == false) goto L6;
            return true;
        L6:
            if (r4.activityInfo.exported == true) goto L9;
            return false;
        L9:
            if (r4.activityInfo.permission != null) goto L11;
            return true;
        L11:
            if (r5.checkSelfPermission(r4.activityInfo.permission) != 0) goto L15;
            return true;
        L15:
            return false;
        }

        private void recomputeProcessTextMenuItems(Menu r10) {
            Context r0 = this.mTextView.getContext();
            PackageManager r1 = r0.getPackageManager();
            if (this.mInitializedMenuBuilderReferences == true) goto L38;
            this.mInitializedMenuBuilderReferences = true;
            this.mMenuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");     // Catch: Throwable -> L7 ClassNotFoundException -> L9
            this.mMenuBuilderRemoveItemAtMethod = this.mMenuBuilderClass.getDeclaredMethod("removeItemAt", new Class[]{Integer.TYPE});     // Catch: Throwable -> L7 ClassNotFoundException -> L9
            this.mCanUseMenuBuilderReferences = true;     // Catch: Throwable -> L7 ClassNotFoundException -> L9
        L10:
            this.mMenuBuilderClass = null;
            this.mMenuBuilderRemoveItemAtMethod = null;
            this.mCanUseMenuBuilderReferences = false;
        L38:
            if (this.mCanUseMenuBuilderReferences == true) goto L14;
        L16:
            Method r2 = r10.getClass().getDeclaredMethod("removeItemAt", new Class[]{Integer.TYPE});     // Catch: InvocationTargetException -> L30 IllegalAccessException -> L32 Throwable -> L34
        L17:
            int r3 = r10.size() - 1;     // Catch: InvocationTargetException -> L30 IllegalAccessException -> L32 Throwable -> L34
        L18:
            if (r3 < 0) goto L25;
            MenuItem r6 = r10.getItem(r3);     // Catch: InvocationTargetException -> L30 IllegalAccessException -> L32 Throwable -> L34
            if (r6.getIntent() == null) goto L24;
            if ("android.intent.action.PROCESS_TEXT".equals(r6.getIntent().getAction()) == false) goto L24;
            r2.invoke(r10, new Object[]{Integer.valueOf(r3)});     // Catch: InvocationTargetException -> L30 IllegalAccessException -> L32 Throwable -> L34
        L24:
            r3 = r3 - 1;
            goto L18
        L25:
            List<ResolveInfo> r4 = getSupportedActivities(r0, r1);
            int r5 = 0;
        L27:
            if (r5 >= r4.size()) goto L29;
            ResolveInfo r7 = r4.get(r5);
            r10.add(0, 0, r5 + 100, r7.loadLabel(r1)).setIntent(createProcessTextIntentForResolveInfo(r7, this.mTextView)).setShowAsAction(1);
            r5 = r5 + 1;
            goto L27
        L29:
            return;
        L14:
            if (this.mMenuBuilderClass.isInstance(r10) == false) goto L16;
            r2 = this.mMenuBuilderRemoveItemAtMethod;     // Catch: InvocationTargetException -> L30 IllegalAccessException -> L32 Throwable -> L34
        }

        ActionMode.Callback getWrappedCallback() {
            return this.mCallback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode r2, MenuItem r3) {
            return this.mCallback.onActionItemClicked(r2, r3);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode r2, Menu r3) {
            return this.mCallback.onCreateActionMode(r2, r3);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode r2) {
            this.mCallback.onDestroyActionMode(r2);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode r2, Menu r3) {
            recomputeProcessTextMenuItems(r3);
            return this.mCallback.onPrepareActionMode(r2, r3);
        }
    }

    private TextViewCompat() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getAutoSizeMaxTextSize(TextView r2) {
        if (Build.VERSION.SDK_INT < 27) goto L7;
        return Api26Impl.getAutoSizeMaxTextSize(r2);
    L7:
        if ((r2 instanceof AutoSizeableTextView) == true) goto L9;
        return -1;
    L9:
        return ((AutoSizeableTextView) r2).getAutoSizeMaxTextSize();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getAutoSizeMinTextSize(TextView r2) {
        if (Build.VERSION.SDK_INT < 27) goto L7;
        return Api26Impl.getAutoSizeMinTextSize(r2);
    L7:
        if ((r2 instanceof AutoSizeableTextView) == true) goto L9;
        return -1;
    L9:
        return ((AutoSizeableTextView) r2).getAutoSizeMinTextSize();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getAutoSizeStepGranularity(TextView r2) {
        if (Build.VERSION.SDK_INT < 27) goto L7;
        return Api26Impl.getAutoSizeStepGranularity(r2);
    L7:
        if ((r2 instanceof AutoSizeableTextView) == true) goto L9;
        return -1;
    L9:
        return ((AutoSizeableTextView) r2).getAutoSizeStepGranularity();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int[] getAutoSizeTextAvailableSizes(TextView r2) {
        if (Build.VERSION.SDK_INT < 27) goto L7;
        return Api26Impl.getAutoSizeTextAvailableSizes(r2);
    L7:
        if ((r2 instanceof AutoSizeableTextView) == false) goto L11;
        return ((AutoSizeableTextView) r2).getAutoSizeTextAvailableSizes();
    L11:
        return new int[0];
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getAutoSizeTextType(TextView r2) {
        if (Build.VERSION.SDK_INT < 27) goto L7;
        return Api26Impl.getAutoSizeTextType(r2);
    L7:
        if ((r2 instanceof AutoSizeableTextView) == true) goto L9;
        return 0;
    L9:
        return ((AutoSizeableTextView) r2).getAutoSizeTextType();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ColorStateList getCompoundDrawableTintList(TextView r2) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api23Impl.getCompoundDrawableTintList(r2);
    L7:
        if ((r2 instanceof TintableCompoundDrawablesView) == true) goto L9;
        return null;
    L9:
        return ((TintableCompoundDrawablesView) r2).getSupportCompoundDrawablesTintList();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static PorterDuff.Mode getCompoundDrawableTintMode(TextView r2) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return Api23Impl.getCompoundDrawableTintMode(r2);
    L7:
        if ((r2 instanceof TintableCompoundDrawablesView) == true) goto L9;
        return null;
    L9:
        return ((TintableCompoundDrawablesView) r2).getSupportCompoundDrawablesTintMode();
    }

    public static Drawable[] getCompoundDrawablesRelative(TextView r1) {
        return Api17Impl.getCompoundDrawablesRelative(r1);
    }

    public static int getFirstBaselineToTopHeight(TextView r2) {
        return r2.getPaddingTop() - r2.getPaint().getFontMetricsInt().top;
    }

    public static int getLastBaselineToBottomHeight(TextView r2) {
        return r2.getPaddingBottom() + r2.getPaint().getFontMetricsInt().bottom;
    }

    public static int getMaxLines(TextView r1) {
        return Api16Impl.getMaxLines(r1);
    }

    public static int getMinLines(TextView r1) {
        return Api16Impl.getMinLines(r1);
    }

    private static int getTextDirection(TextDirectionHeuristic r2) {
        if (r2 != TextDirectionHeuristics.FIRSTSTRONG_RTL) goto L6;
        return 1;
    L6:
        if (r2 != TextDirectionHeuristics.FIRSTSTRONG_LTR) goto L9;
        return 1;
    L9:
        if (r2 != TextDirectionHeuristics.ANYRTL_LTR) goto L13;
        return 2;
    L13:
        if (r2 != TextDirectionHeuristics.LTR) goto L17;
        return 3;
    L17:
        if (r2 != TextDirectionHeuristics.RTL) goto L21;
        return 4;
    L21:
        if (r2 != TextDirectionHeuristics.LOCALE) goto L25;
        return 5;
    L25:
        if (r2 != TextDirectionHeuristics.FIRSTSTRONG_LTR) goto L29;
        return 6;
    L29:
        if (r2 != TextDirectionHeuristics.FIRSTSTRONG_RTL) goto L32;
        return 7;
    L32:
        return 1;
    }

    private static TextDirectionHeuristic getTextDirectionHeuristic(TextView r5) {
        if ((r5.getTransformationMethod() instanceof PasswordTransformationMethod) == true) goto L5;
        boolean r2 = false;
        if (Build.VERSION.SDK_INT < 28) goto L20;
        if ((r5.getInputType() & 15) != 3) goto L20;
        byte r4 = Character.getDirectionality(Api28Impl.getDigitStrings(Api24Impl.getInstance(Api17Impl.getTextLocale(r5)))[0].codePointAt(0));
        if (r4 == 1) goto L18;
        if (r4 == 2) goto L18;
        return TextDirectionHeuristics.LTR;
    L18:
        return TextDirectionHeuristics.RTL;
    L20:
        if (Api17Impl.getLayoutDirection(r5) != 1) goto L23;
        r2 = true;
    L23:
        switch(Api17Impl.getTextDirection(r5)) {
            case 2: goto L37;
            case 3: goto L35;
            case 4: goto L33;
            case 5: goto L31;
            case 6: goto L29;
            case 7: goto L27;
            default: goto L24;
        };
    L24:
        if (r2 == false) goto L39;
        return TextDirectionHeuristics.FIRSTSTRONG_RTL;
    L39:
        return TextDirectionHeuristics.FIRSTSTRONG_LTR;
    L27:
        return TextDirectionHeuristics.FIRSTSTRONG_RTL;
    L29:
        return TextDirectionHeuristics.FIRSTSTRONG_LTR;
    L31:
        return TextDirectionHeuristics.LOCALE;
    L33:
        return TextDirectionHeuristics.RTL;
    L35:
        return TextDirectionHeuristics.LTR;
    L37:
        return TextDirectionHeuristics.ANYRTL_LTR;
    L5:
        return TextDirectionHeuristics.LTR;
    }

    public static PrecomputedTextCompat.Params getTextMetricsParams(TextView r3) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        PrecomputedTextCompat.Params.Builder r0 = new PrecomputedTextCompat.Params.Builder(new TextPaint(r3.getPaint()));
        r0.setBreakStrategy(Api23Impl.getBreakStrategy(r3));
        r0.setHyphenationFrequency(Api23Impl.getHyphenationFrequency(r3));
        r0.setTextDirection(getTextDirectionHeuristic(r3));
        return r0.build();
    L5:
        return new PrecomputedTextCompat.Params(Api28Impl.getTextMetricsParams(r3));
    }

    private static Field retrieveField(String r4) {
        Field r0 = null;
        r0 = TextView.class.getDeclaredField(r4);     // Catch: NoSuchFieldException -> L5
        r0.setAccessible(true);     // Catch: NoSuchFieldException -> L5
        return r0;
    L6:
        Log.e(LOG_TAG, "Could not retrieve " + r4 + " field.");
        return r0;
    }

    private static int retrieveIntFromField(Field r3, TextView r4) {
        return r3.getInt(r4);
    L5:
        Log.d(LOG_TAG, "Could not retrieve value of " + r3.getName() + " field.");
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setAutoSizeTextTypeUniformWithConfiguration(TextView r2, int r3, int r4, int r5, int r6) throws IllegalArgumentException {
        if (Build.VERSION.SDK_INT < 27) goto L6;
        Api26Impl.setAutoSizeTextTypeUniformWithConfiguration(r2, r3, r4, r5, r6);
        return;
    L6:
        if ((r2 instanceof AutoSizeableTextView) == false) goto L10;
        ((AutoSizeableTextView) r2).setAutoSizeTextTypeUniformWithConfiguration(r3, r4, r5, r6);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setAutoSizeTextTypeUniformWithPresetSizes(TextView r2, int[] r3, int r4) throws IllegalArgumentException {
        if (Build.VERSION.SDK_INT < 27) goto L6;
        Api26Impl.setAutoSizeTextTypeUniformWithPresetSizes(r2, r3, r4);
        return;
    L6:
        if ((r2 instanceof AutoSizeableTextView) == false) goto L10;
        ((AutoSizeableTextView) r2).setAutoSizeTextTypeUniformWithPresetSizes(r3, r4);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setAutoSizeTextTypeWithDefaults(TextView r2, int r3) {
        if (Build.VERSION.SDK_INT < 27) goto L6;
        Api26Impl.setAutoSizeTextTypeWithDefaults(r2, r3);
        return;
    L6:
        if ((r2 instanceof AutoSizeableTextView) == false) goto L10;
        ((AutoSizeableTextView) r2).setAutoSizeTextTypeWithDefaults(r3);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setCompoundDrawableTintList(TextView r2, ColorStateList r3) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api23Impl.setCompoundDrawableTintList(r2, r3);
        return;
    L6:
        if ((r2 instanceof TintableCompoundDrawablesView) == false) goto L10;
        ((TintableCompoundDrawablesView) r2).setSupportCompoundDrawablesTintList(r3);
        return;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setCompoundDrawableTintMode(TextView r2, PorterDuff.Mode r3) {
        Preconditions.checkNotNull(r2);
        if (Build.VERSION.SDK_INT < 24) goto L6;
        Api23Impl.setCompoundDrawableTintMode(r2, r3);
        return;
    L6:
        if ((r2 instanceof TintableCompoundDrawablesView) == false) goto L10;
        ((TintableCompoundDrawablesView) r2).setSupportCompoundDrawablesTintMode(r3);
        return;
    }

    public static void setCompoundDrawablesRelative(TextView r0, Drawable r1, Drawable r2, Drawable r3, Drawable r4) {
        Api17Impl.setCompoundDrawablesRelative(r0, r1, r2, r3, r4);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView r0, int r1, int r2, int r3, int r4) {
        Api17Impl.setCompoundDrawablesRelativeWithIntrinsicBounds(r0, r1, r2, r3, r4);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView r0, Drawable r1, Drawable r2, Drawable r3, Drawable r4) {
        Api17Impl.setCompoundDrawablesRelativeWithIntrinsicBounds(r0, r1, r2, r3, r4);
    }

    public static void setCustomSelectionActionModeCallback(TextView r1, ActionMode.Callback r2) {
        r1.setCustomSelectionActionModeCallback(wrapCustomSelectionActionModeCallback(r1, r2));
    }

    public static void setFirstBaselineToTopHeight(TextView r6, int r7) {
        Preconditions.checkArgumentNonnegative(r7);
        if (Build.VERSION.SDK_INT < 28) goto L6;
        Api28Impl.setFirstBaselineToTopHeight(r6, r7);
        return;
    L6:
        Paint.FontMetricsInt r0 = r6.getPaint().getFontMetricsInt();
        if (Api16Impl.getIncludeFontPadding(r6) == false) goto L9;
        int r1 = r0.top;
    L11:
        if (r7 <= Math.abs(r1)) goto L14;
        int r3 = r6.getPaddingLeft();
        r6.setPadding(r3, r7 + r1, r6.getPaddingRight(), r6.getPaddingBottom());
        return;
    L14:
        return;
    L9:
        r1 = r0.ascent;
        goto L11
    }

    public static void setLastBaselineToBottomHeight(TextView r6, int r7) {
        Preconditions.checkArgumentNonnegative(r7);
        Paint.FontMetricsInt r0 = r6.getPaint().getFontMetricsInt();
        if (Api16Impl.getIncludeFontPadding(r6) == false) goto L5;
        int r1 = r0.bottom;
    L7:
        if (r7 <= Math.abs(r1)) goto L10;
        int r3 = r6.getPaddingLeft();
        int r4 = r6.getPaddingTop();
        int r5 = r6.getPaddingRight();
        r6.setPadding(r3, r4, r5, r7 - r1);
        return;
    L10:
        return;
    L5:
        r1 = r0.descent;
        goto L7
    }

    public static void setLineHeight(TextView r3, int r4) {
        Preconditions.checkArgumentNonnegative(r4);
        int r0 = r3.getPaint().getFontMetricsInt(null);
        if (r4 == r0) goto L6;
        r3.setLineSpacing(r4 - r0, 1.0f);
        return;
    }

    public static void setPrecomputedText(TextView r3, PrecomputedTextCompat r4) {
        if (Build.VERSION.SDK_INT < 29) goto L6;
        r3.setText(r4.getPrecomputedText());
        return;
    L6:
        if (getTextMetricsParams(r3).equalsWithoutTextDirection(r4.getParams()) == false) goto L10;
        r3.setText(r4);
        return;
    L10:
        throw new IllegalArgumentException("Given text can not be applied to TextView.");
    }

    public static void setTextAppearance(TextView r0, int r1) {
        r0.setTextAppearance(r1);
    }

    public static void setTextMetricsParams(TextView r2, PrecomputedTextCompat.Params r3) {
        Api17Impl.setTextDirection(r2, getTextDirection(r3.getTextDirection()));
        r2.getPaint().set(r3.getTextPaint());
        Api23Impl.setBreakStrategy(r2, r3.getBreakStrategy());
        Api23Impl.setHyphenationFrequency(r2, r3.getHyphenationFrequency());
    }

    public static ActionMode.Callback unwrapCustomSelectionActionModeCallback(ActionMode.Callback r2) {
        if ((r2 instanceof OreoCallback) == true) goto L5;
    L8:
        return r2;
    L5:
        if (Build.VERSION.SDK_INT < 26) goto L8;
        return ((OreoCallback) r2).getWrappedCallback();
    }

    public static ActionMode.Callback wrapCustomSelectionActionModeCallback(TextView r2, ActionMode.Callback r3) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
    L12:
        return r3;
    L5:
        if (Build.VERSION.SDK_INT > 27) goto L12;
        if ((r3 instanceof OreoCallback) == true) goto L12;
        if (r3 == null) goto L12;
        return new OreoCallback(r3, r2);
    }
}
