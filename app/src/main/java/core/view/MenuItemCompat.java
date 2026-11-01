package androidx.core.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.core.internal.view.SupportMenuItem;

/* JADX INFO: loaded from: classes.dex */
public final class MenuItemCompat {

    @Deprecated
    public static final int SHOW_AS_ACTION_ALWAYS = 2;

    @Deprecated
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;

    @Deprecated
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;

    @Deprecated
    public static final int SHOW_AS_ACTION_NEVER = 0;

    @Deprecated
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    static class Api26Impl {
        private Api26Impl() {
        }

        static int getAlphabeticModifiers(MenuItem r1) {
            return r1.getAlphabeticModifiers();
        }

        static CharSequence getContentDescription(MenuItem r1) {
            return r1.getContentDescription();
        }

        static ColorStateList getIconTintList(MenuItem r1) {
            return r1.getIconTintList();
        }

        static PorterDuff.Mode getIconTintMode(MenuItem r1) {
            return r1.getIconTintMode();
        }

        static int getNumericModifiers(MenuItem r1) {
            return r1.getNumericModifiers();
        }

        static CharSequence getTooltipText(MenuItem r1) {
            return r1.getTooltipText();
        }

        static MenuItem setAlphabeticShortcut(MenuItem r1, char r2, int r3) {
            return r1.setAlphabeticShortcut(r2, r3);
        }

        static MenuItem setContentDescription(MenuItem r1, CharSequence r2) {
            return r1.setContentDescription(r2);
        }

        static MenuItem setIconTintList(MenuItem r1, ColorStateList r2) {
            return r1.setIconTintList(r2);
        }

        static MenuItem setIconTintMode(MenuItem r1, PorterDuff.Mode r2) {
            return r1.setIconTintMode(r2);
        }

        static MenuItem setNumericShortcut(MenuItem r1, char r2, int r3) {
            return r1.setNumericShortcut(r2, r3);
        }

        static MenuItem setShortcut(MenuItem r1, char r2, char r3, int r4, int r5) {
            return r1.setShortcut(r2, r3, r4, r5);
        }

        static MenuItem setTooltipText(MenuItem r1, CharSequence r2) {
            return r1.setTooltipText(r2);
        }
    }

    @Deprecated
    public interface OnActionExpandListener {
        boolean onMenuItemActionCollapse(MenuItem r1);

        boolean onMenuItemActionExpand(MenuItem r1);
    }

    private MenuItemCompat() {
    }

    @Deprecated
    public static boolean collapseActionView(MenuItem r1) {
        return r1.collapseActionView();
    }

    @Deprecated
    public static boolean expandActionView(MenuItem r1) {
        return r1.expandActionView();
    }

    public static ActionProvider getActionProvider(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == true) goto L5;
        Log.w(TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    L5:
        return ((SupportMenuItem) r2).getSupportActionProvider();
    }

    @Deprecated
    public static View getActionView(MenuItem r1) {
        return r1.getActionView();
    }

    public static int getAlphabeticModifiers(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == false) goto L7;
        return ((SupportMenuItem) r2).getAlphabeticModifiers();
    L7:
        if (Build.VERSION.SDK_INT >= 26) goto L9;
        return 0;
    L9:
        return Api26Impl.getAlphabeticModifiers(r2);
    }

    public static CharSequence getContentDescription(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == false) goto L7;
        return ((SupportMenuItem) r2).getContentDescription();
    L7:
        if (Build.VERSION.SDK_INT >= 26) goto L9;
        return null;
    L9:
        return Api26Impl.getContentDescription(r2);
    }

    public static ColorStateList getIconTintList(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == false) goto L7;
        return ((SupportMenuItem) r2).getIconTintList();
    L7:
        if (Build.VERSION.SDK_INT >= 26) goto L9;
        return null;
    L9:
        return Api26Impl.getIconTintList(r2);
    }

    public static PorterDuff.Mode getIconTintMode(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == false) goto L7;
        return ((SupportMenuItem) r2).getIconTintMode();
    L7:
        if (Build.VERSION.SDK_INT >= 26) goto L9;
        return null;
    L9:
        return Api26Impl.getIconTintMode(r2);
    }

    public static int getNumericModifiers(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == false) goto L7;
        return ((SupportMenuItem) r2).getNumericModifiers();
    L7:
        if (Build.VERSION.SDK_INT >= 26) goto L9;
        return 0;
    L9:
        return Api26Impl.getNumericModifiers(r2);
    }

    public static CharSequence getTooltipText(MenuItem r2) {
        if ((r2 instanceof SupportMenuItem) == false) goto L7;
        return ((SupportMenuItem) r2).getTooltipText();
    L7:
        if (Build.VERSION.SDK_INT >= 26) goto L9;
        return null;
    L9:
        return Api26Impl.getTooltipText(r2);
    }

    @Deprecated
    public static boolean isActionViewExpanded(MenuItem r1) {
        return r1.isActionViewExpanded();
    }

    public static MenuItem setActionProvider(MenuItem r2, ActionProvider r3) {
        if ((r2 instanceof SupportMenuItem) == true) goto L5;
        Log.w(TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return r2;
    L5:
        return ((SupportMenuItem) r2).setSupportActionProvider(r3);
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem r1, int r2) {
        return r1.setActionView(r2);
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem r1, View r2) {
        return r1.setActionView(r2);
    }

    public static void setAlphabeticShortcut(MenuItem r2, char r3, int r4) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setAlphabeticShortcut(r3, r4);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setAlphabeticShortcut(r2, r3, r4);
        return;
    }

    public static void setContentDescription(MenuItem r2, CharSequence r3) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setContentDescription(r3);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setContentDescription(r2, r3);
        return;
    }

    public static void setIconTintList(MenuItem r2, ColorStateList r3) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setIconTintList(r3);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setIconTintList(r2, r3);
        return;
    }

    public static void setIconTintMode(MenuItem r2, PorterDuff.Mode r3) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setIconTintMode(r3);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setIconTintMode(r2, r3);
        return;
    }

    public static void setNumericShortcut(MenuItem r2, char r3, int r4) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setNumericShortcut(r3, r4);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setNumericShortcut(r2, r3, r4);
        return;
    }

    @Deprecated
    public static MenuItem setOnActionExpandListener(MenuItem r1, final OnActionExpandListener r2) {
        return r1.setOnActionExpandListener(new MenuItemOnActionExpandListenerC01121(r2));
    }

    public static void setShortcut(MenuItem r2, char r3, char r4, int r5, int r6) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setShortcut(r3, r4, r5, r6);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setShortcut(r2, r3, r4, r5, r6);
        return;
    }

    @Deprecated
    public static void setShowAsAction(MenuItem r0, int r1) {
        r0.setShowAsAction(r1);
    }

    public static void setTooltipText(MenuItem r2, CharSequence r3) {
        if ((r2 instanceof SupportMenuItem) == false) goto L6;
        ((SupportMenuItem) r2).setTooltipText(r3);
        return;
    L6:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        Api26Impl.setTooltipText(r2, r3);
        return;
    }
}
