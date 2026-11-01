package androidx.core.internal.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.MenuItem;
import android.view.View;
import androidx.core.view.ActionProvider;

/* JADX INFO: loaded from: classes.dex */
public interface SupportMenuItem extends MenuItem {
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;

    /* JADX INFO: renamed from: androidx.core.internal.view.SupportMenuItem$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ MenuItem $default$setContentDescription(SupportMenuItem r0, CharSequence r1) {
            return r0.setContentDescription(r1);
        }

        public static /* bridge */ /* synthetic */ MenuItem $default$setTooltipText(SupportMenuItem r0, CharSequence r1) {
            return r0.setTooltipText(r1);
        }
    }

    @Override // android.view.MenuItem
    boolean collapseActionView();

    @Override // android.view.MenuItem
    boolean expandActionView();

    @Override // android.view.MenuItem
    View getActionView();

    @Override // android.view.MenuItem
    int getAlphabeticModifiers();

    @Override // android.view.MenuItem
    CharSequence getContentDescription();

    @Override // android.view.MenuItem
    ColorStateList getIconTintList();

    @Override // android.view.MenuItem
    PorterDuff.Mode getIconTintMode();

    @Override // android.view.MenuItem
    int getNumericModifiers();

    ActionProvider getSupportActionProvider();

    @Override // android.view.MenuItem
    CharSequence getTooltipText();

    @Override // android.view.MenuItem
    boolean isActionViewExpanded();

    boolean requiresActionButton();

    boolean requiresOverflow();

    @Override // android.view.MenuItem
    MenuItem setActionView(int r1);

    @Override // android.view.MenuItem
    MenuItem setActionView(View r1);

    @Override // android.view.MenuItem
    MenuItem setAlphabeticShortcut(char r1, int r2);

    @Override // android.view.MenuItem
    SupportMenuItem setContentDescription(CharSequence r1);

    @Override // android.view.MenuItem
    MenuItem setIconTintList(ColorStateList r1);

    @Override // android.view.MenuItem
    MenuItem setIconTintMode(PorterDuff.Mode r1);

    @Override // android.view.MenuItem
    MenuItem setNumericShortcut(char r1, int r2);

    @Override // android.view.MenuItem
    MenuItem setShortcut(char r1, char r2, int r3, int r4);

    @Override // android.view.MenuItem
    void setShowAsAction(int r1);

    @Override // android.view.MenuItem
    MenuItem setShowAsActionFlags(int r1);

    SupportMenuItem setSupportActionProvider(ActionProvider r1);

    @Override // android.view.MenuItem
    SupportMenuItem setTooltipText(CharSequence r1);
}
