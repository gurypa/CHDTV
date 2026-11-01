package androidx.core.app;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.graphics.drawable.Icon;
import android.os.Build;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import androidx.versionedparcelable.VersionedParcelable;

/* JADX INFO: loaded from: classes.dex */
public final class RemoteActionCompat implements VersionedParcelable {
    public PendingIntent mActionIntent;
    public CharSequence mContentDescription;
    public boolean mEnabled;
    public IconCompat mIcon;
    public boolean mShouldShowIcon;
    public CharSequence mTitle;

    static class Api26Impl {
        private Api26Impl() {
        }

        static RemoteAction createRemoteAction(Icon r1, CharSequence r2, CharSequence r3, PendingIntent r4) {
            return new RemoteAction(r1, r2, r3, r4);
        }

        static PendingIntent getActionIntent(RemoteAction r1) {
            return r1.getActionIntent();
        }

        static CharSequence getContentDescription(RemoteAction r1) {
            return r1.getContentDescription();
        }

        static Icon getIcon(RemoteAction r1) {
            return r1.getIcon();
        }

        static CharSequence getTitle(RemoteAction r1) {
            return r1.getTitle();
        }

        static boolean isEnabled(RemoteAction r1) {
            return r1.isEnabled();
        }

        static void setEnabled(RemoteAction r0, boolean r1) {
            r0.setEnabled(r1);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static void setShouldShowIcon(RemoteAction r0, boolean r1) {
            r0.setShouldShowIcon(r1);
        }

        static boolean shouldShowIcon(RemoteAction r1) {
            return r1.shouldShowIcon();
        }
    }

    public RemoteActionCompat() {
    }

    public RemoteActionCompat(RemoteActionCompat r2) {
        Preconditions.checkNotNull(r2);
        this.mIcon = r2.mIcon;
        this.mTitle = r2.mTitle;
        this.mContentDescription = r2.mContentDescription;
        this.mActionIntent = r2.mActionIntent;
        this.mEnabled = r2.mEnabled;
        this.mShouldShowIcon = r2.mShouldShowIcon;
    }

    public RemoteActionCompat(IconCompat r2, CharSequence r3, CharSequence r4, PendingIntent r5) {
        this.mIcon = (IconCompat) Preconditions.checkNotNull(r2);
        this.mTitle = (CharSequence) Preconditions.checkNotNull(r3);
        this.mContentDescription = (CharSequence) Preconditions.checkNotNull(r4);
        this.mActionIntent = (PendingIntent) Preconditions.checkNotNull(r5);
        this.mEnabled = true;
        this.mShouldShowIcon = true;
    }

    public static RemoteActionCompat createFromRemoteAction(RemoteAction r5) {
        Preconditions.checkNotNull(r5);
        RemoteActionCompat r0 = new RemoteActionCompat(IconCompat.createFromIcon(Api26Impl.getIcon(r5)), Api26Impl.getTitle(r5), Api26Impl.getContentDescription(r5), Api26Impl.getActionIntent(r5));
        r0.setEnabled(Api26Impl.isEnabled(r5));
        if (Build.VERSION.SDK_INT < 28) goto L5;
        r0.setShouldShowIcon(Api28Impl.shouldShowIcon(r5));
    L5:
        return r0;
    }

    public PendingIntent getActionIntent() {
        return this.mActionIntent;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean r1) {
        this.mEnabled = r1;
    }

    public void setShouldShowIcon(boolean r1) {
        this.mShouldShowIcon = r1;
    }

    public boolean shouldShowIcon() {
        return this.mShouldShowIcon;
    }

    public RemoteAction toRemoteAction() {
        RemoteAction r0 = Api26Impl.createRemoteAction(this.mIcon.toIcon(), this.mTitle, this.mContentDescription, this.mActionIntent);
        Api26Impl.setEnabled(r0, isEnabled());
        if (Build.VERSION.SDK_INT < 28) goto L5;
        Api28Impl.setShouldShowIcon(r0, shouldShowIcon());
    L5:
        return r0;
    }
}
