package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import androidx.core.C0039R;
import androidx.core.content.LocusIdCompat;
import androidx.core.content.p000pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.text.BidiFormatter;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NotificationCompat {
    public static final int BADGE_ICON_LARGE = 2;
    public static final int BADGE_ICON_NONE = 0;
    public static final int BADGE_ICON_SMALL = 1;
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_LOCATION_SHARING = "location_sharing";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_MISSED_CALL = "missed_call";
    public static final String CATEGORY_NAVIGATION = "navigation";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_STOPWATCH = "stopwatch";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    public static final String CATEGORY_WORKOUT = "workout";
    public static final int COLOR_DEFAULT = 0;
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_CHANNEL_GROUP_ID = "android.intent.extra.CHANNEL_GROUP_ID";
    public static final String EXTRA_CHANNEL_ID = "android.intent.extra.CHANNEL_ID";
    public static final String EXTRA_CHRONOMETER_COUNT_DOWN = "android.chronometerCountDown";
    public static final String EXTRA_COLORIZED = "android.colorized";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_COMPAT_TEMPLATE = "androidx.core.app.extra.COMPAT_TEMPLATE";
    public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
    public static final String EXTRA_HIDDEN_CONVERSATION_TITLE = "android.hiddenConversationTitle";
    public static final String EXTRA_HISTORIC_MESSAGES = "android.messages.historic";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_MESSAGING_STYLE_USER = "android.messagingStyleUser";
    public static final String EXTRA_NOTIFICATION_ID = "android.intent.extra.NOTIFICATION_ID";
    public static final String EXTRA_NOTIFICATION_TAG = "android.intent.extra.NOTIFICATION_TAG";

    @Deprecated
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PEOPLE_LIST = "android.people.list";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PICTURE_CONTENT_DESCRIPTION = "android.pictureContentDescription";
    public static final String EXTRA_PICTURE_ICON = "android.pictureIcon";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
    public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
    public static final String EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED = "android.showBigPictureWhenCollapsed";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_BUBBLE = 4096;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;

    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int FOREGROUND_SERVICE_DEFAULT = 0;
    public static final int FOREGROUND_SERVICE_DEFERRED = 2;
    public static final int FOREGROUND_SERVICE_IMMEDIATE = 1;
    public static final int GROUP_ALERT_ALL = 0;
    public static final int GROUP_ALERT_CHILDREN = 2;
    public static final int GROUP_ALERT_SUMMARY = 1;
    public static final String GROUP_KEY_SILENT = "silent";
    public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES";
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int STREAM_DEFAULT = -1;
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;

    /* JADX INFO: renamed from: androidx.core.app.NotificationCompat$1 */
    static /* synthetic */ class C00521 {
    }

    public static class Action {
        static final String EXTRA_SEMANTIC_ACTION = "android.support.action.semanticAction";
        static final String EXTRA_SHOWS_USER_INTERFACE = "android.support.action.showsUserInterface";
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;
        public static final int SEMANTIC_ACTION_CALL = 10;
        public static final int SEMANTIC_ACTION_DELETE = 4;
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
        public static final int SEMANTIC_ACTION_MUTE = 6;
        public static final int SEMANTIC_ACTION_NONE = 0;
        public static final int SEMANTIC_ACTION_REPLY = 1;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
        public static final int SEMANTIC_ACTION_UNMUTE = 7;
        public PendingIntent actionIntent;

        @Deprecated
        public int icon;
        private boolean mAllowGeneratedReplies;
        private boolean mAuthenticationRequired;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private IconCompat mIcon;
        private final boolean mIsContextual;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;

        public static final class Builder {
            private boolean mAllowGeneratedReplies;
            private boolean mAuthenticationRequired;
            private final Bundle mExtras;
            private final IconCompat mIcon;
            private final PendingIntent mIntent;
            private boolean mIsContextual;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private boolean mShowsUserInterface;
            private final CharSequence mTitle;

            public Builder(int r13, CharSequence r14, PendingIntent r15) {
                IconCompat r0 = null;
                if (r13 == 0) goto L6;
                r0 = IconCompat.createWithResource(null, "", r13);
            L6:
                this(r0, r14, r15, new Bundle(), null, true, 0, true, false, false);
            }

            public Builder(Action r12) {
                this(r12.getIconCompat(), r12.title, r12.actionIntent, new Bundle(r12.mExtras), r12.getRemoteInputs(), r12.getAllowGeneratedReplies(), r12.getSemanticAction(), r12.mShowsUserInterface, r12.isContextual(), r12.isAuthenticationRequired());
            }

            public Builder(IconCompat r12, CharSequence r13, PendingIntent r14) {
                this(r12, r13, r14, new Bundle(), null, true, 0, true, false, false);
            }

            private Builder(IconCompat r3, CharSequence r4, PendingIntent r5, Bundle r6, RemoteInput[] r7, boolean r8, int r9, boolean r10, boolean r11, boolean r12) {
                this.mAllowGeneratedReplies = true;
                this.mShowsUserInterface = true;
                this.mIcon = r3;
                this.mTitle = Builder.limitCharSequenceLength(r4);
                this.mIntent = r5;
                this.mExtras = r6;
                if (r7 != null) goto L5;
                ArrayList<RemoteInput> r0 = null;
            L6:
                this.mRemoteInputs = r0;
                this.mAllowGeneratedReplies = r8;
                this.mSemanticAction = r9;
                this.mShowsUserInterface = r10;
                this.mIsContextual = r11;
                this.mAuthenticationRequired = r12;
                return;
            L5:
                r0 = new ArrayList(Arrays.asList(r7));
                goto L6
            }

            private void checkContextualActionNullFields() {
                if (this.mIsContextual == true) goto L6;
                return;
            L6:
                if (this.mIntent == null) goto L9;
                return;
            L9:
                throw new NullPointerException("Contextual Actions must contain a valid PendingIntent");
            }

            public static Builder fromAndroidAction(Notification.Action r6) {
                if (r6.getIcon() == null) goto L5;
                Builder r1 = new Builder(IconCompat.createFromIcon(r6.getIcon()), r6.title, r6.actionIntent);
            L6:
                android.app.RemoteInput[] r0 = r6.getRemoteInputs();
                if (r0 == null) goto L14;
                if (r0.length == 0) goto L14;
                int r2 = r0.length;
                int r3 = 0;
            L11:
                if (r3 >= r2) goto L14;
                r1.addRemoteInput(RemoteInput.fromPlatform(r0[r3]));
                r3 = r3 + 1;
            L14:
                if (Build.VERSION.SDK_INT < 24) goto L17;
                r1.mAllowGeneratedReplies = r6.getAllowGeneratedReplies();
            L17:
                if (Build.VERSION.SDK_INT < 28) goto L20;
                r1.setSemanticAction(r6.getSemanticAction());
            L20:
                if (Build.VERSION.SDK_INT < 29) goto L23;
                r1.setContextual(r6.isContextual());
            L23:
                if (Build.VERSION.SDK_INT < 31) goto L25;
                r1.setAuthenticationRequired(r6.isAuthenticationRequired());
            L25:
                return r1;
            L5:
                r1 = new Builder(r6.icon, r6.title, r6.actionIntent);
                goto L6
            }

            public Builder addExtras(Bundle r2) {
                if (r2 == null) goto L4;
                this.mExtras.putAll(r2);
            L4:
                return this;
            }

            public Builder addRemoteInput(RemoteInput r2) {
                if (this.mRemoteInputs != null) goto L5;
                this.mRemoteInputs = new ArrayList();
            L5:
                if (r2 == null) goto L7;
                this.mRemoteInputs.add(r2);
            L7:
                return this;
            }

            public Action build() {
                checkContextualActionNullFields();
                ArrayList r1 = new ArrayList();
                ArrayList r2 = new ArrayList();
                if (this.mRemoteInputs == null) goto L12;
                Iterator<RemoteInput> r3 = this.mRemoteInputs.iterator();
            L6:
                if (r3.hasNext() == false) goto L12;
                RemoteInput r4 = r3.next();
                if (r4.isDataOnly() == true) goto L9;
                r2.add(r4);
                goto L6
            L9:
                r1.add(r4);
            L12:
                RemoteInput[] r5 = null;
                if (r1.isEmpty() == false) goto L15;
                RemoteInput[] r6 = null;
            L16:
                RemoteInput[] r11 = r6;
                if (r2.isEmpty() == true) goto L21;
                r5 = (RemoteInput[]) r2.toArray(new RemoteInput[r2.size()]);
            L21:
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, r5, r11, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface, this.mIsContextual, this.mAuthenticationRequired);
            L15:
                r6 = (RemoteInput[]) r1.toArray(new RemoteInput[r1.size()]);
                goto L16
            }

            public Builder extend(Extender r1) {
                r1.extend(this);
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder setAllowGeneratedReplies(boolean r1) {
                this.mAllowGeneratedReplies = r1;
                return this;
            }

            public Builder setAuthenticationRequired(boolean r1) {
                this.mAuthenticationRequired = r1;
                return this;
            }

            public Builder setContextual(boolean r1) {
                this.mIsContextual = r1;
                return this;
            }

            public Builder setSemanticAction(int r1) {
                this.mSemanticAction = r1;
                return this;
            }

            public Builder setShowsUserInterface(boolean r1) {
                this.mShowsUserInterface = r1;
                return this;
            }
        }

        public interface Extender {
            Builder extend(Builder r1);
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface SemanticAction {
        }

        public static final class WearableExtender implements Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 4;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags;
            private CharSequence mInProgressLabel;

            public WearableExtender() {
                this.mFlags = 1;
            }

            public WearableExtender(Action r4) {
                this.mFlags = 1;
                Bundle r1 = r4.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (r1 == null) goto L6;
                this.mFlags = r1.getInt(KEY_FLAGS, 1);
                this.mInProgressLabel = r1.getCharSequence(KEY_IN_PROGRESS_LABEL);
                this.mConfirmLabel = r1.getCharSequence(KEY_CONFIRM_LABEL);
                this.mCancelLabel = r1.getCharSequence(KEY_CANCEL_LABEL);
                return;
            }

            private void setFlag(int r3, boolean r4) {
                if (r4 == false) goto L4;
                this.mFlags |= r3;
                return;
            L4:
                this.mFlags &= ~r3;
            }

            public WearableExtender clone() {
                WearableExtender r0 = new WearableExtender();
                r0.mFlags = this.mFlags;
                r0.mInProgressLabel = this.mInProgressLabel;
                r0.mConfirmLabel = this.mConfirmLabel;
                r0.mCancelLabel = this.mCancelLabel;
                return r0;
            }

            /* JADX INFO: renamed from: clone, reason: collision with other method in class */
            public /* bridge */ /* synthetic */ Object m452clone() throws CloneNotSupportedException {
                return clone();
            }

            @Override // androidx.core.app.NotificationCompat.Action.Extender
            public Builder extend(Builder r4) {
                Bundle r0 = new Bundle();
                if (this.mFlags == 1) goto L6;
                r0.putInt(KEY_FLAGS, this.mFlags);
            L6:
                if (this.mInProgressLabel == null) goto L9;
                r0.putCharSequence(KEY_IN_PROGRESS_LABEL, this.mInProgressLabel);
            L9:
                if (this.mConfirmLabel == null) goto L12;
                r0.putCharSequence(KEY_CONFIRM_LABEL, this.mConfirmLabel);
            L12:
                if (this.mCancelLabel == null) goto L14;
                r0.putCharSequence(KEY_CANCEL_LABEL, this.mCancelLabel);
            L14:
                r4.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, r0);
                return r4;
            }

            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }

            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            public boolean getHintDisplayActionInline() {
                if ((this.mFlags & 4) == 0) goto L5;
                return true;
            L5:
                return false;
            }

            public boolean getHintLaunchesActivity() {
                if ((this.mFlags & 2) == 0) goto L5;
                return true;
            L5:
                return false;
            }

            @Deprecated
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            public boolean isAvailableOffline() {
                if ((this.mFlags & 1) != 0) goto L7;
                return false;
            L7:
                return true;
            }

            public WearableExtender setAvailableOffline(boolean r2) {
                setFlag(1, r2);
                return this;
            }

            @Deprecated
            public WearableExtender setCancelLabel(CharSequence r1) {
                this.mCancelLabel = r1;
                return this;
            }

            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence r1) {
                this.mConfirmLabel = r1;
                return this;
            }

            public WearableExtender setHintDisplayActionInline(boolean r2) {
                setFlag(4, r2);
                return this;
            }

            public WearableExtender setHintLaunchesActivity(boolean r2) {
                setFlag(2, r2);
                return this;
            }

            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence r1) {
                this.mInProgressLabel = r1;
                return this;
            }
        }

        public Action(int r3, CharSequence r4, PendingIntent r5) {
            IconCompat r0 = null;
            if (r3 == 0) goto L6;
            r0 = IconCompat.createWithResource(null, "", r3);
        L6:
            this(r0, r4, r5);
        }

        Action(int r14, CharSequence r15, PendingIntent r16, Bundle r17, RemoteInput[] r18, RemoteInput[] r19, boolean r20, int r21, boolean r22, boolean r23, boolean r24) {
            IconCompat r0 = null;
            if (r14 == 0) goto L6;
            r0 = IconCompat.createWithResource(null, "", r14);
        L6:
            this(r0, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24);
        }

        public Action(IconCompat r13, CharSequence r14, PendingIntent r15) {
            this(r13, r14, r15, new Bundle(), null, null, true, 0, true, false, false);
        }

        Action(IconCompat r3, CharSequence r4, PendingIntent r5, Bundle r6, RemoteInput[] r7, RemoteInput[] r8, boolean r9, int r10, boolean r11, boolean r12, boolean r13) {
            this.mShowsUserInterface = true;
            this.mIcon = r3;
            if (r3 != null) goto L5;
        L7:
            this.title = Builder.limitCharSequenceLength(r4);
            this.actionIntent = r5;
            if (r6 == null) goto L10;
            Bundle r0 = r6;
        L11:
            this.mExtras = r0;
            this.mRemoteInputs = r7;
            this.mDataOnlyRemoteInputs = r8;
            this.mAllowGeneratedReplies = r9;
            this.mSemanticAction = r10;
            this.mShowsUserInterface = r11;
            this.mIsContextual = r12;
            this.mAuthenticationRequired = r13;
            return;
        L10:
            r0 = new Bundle();
            goto L11
        L5:
            if (r3.getType() != 2) goto L7;
            this.icon = r3.getResId();
            goto L7
        }

        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        @Deprecated
        public int getIcon() {
            return this.icon;
        }

        public IconCompat getIconCompat() {
            if (this.mIcon != null) goto L8;
            if (this.icon == 0) goto L8;
            this.mIcon = IconCompat.createWithResource(null, "", this.icon);
        L8:
            return this.mIcon;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public int getSemanticAction() {
            return this.mSemanticAction;
        }

        public boolean getShowsUserInterface() {
            return this.mShowsUserInterface;
        }

        public CharSequence getTitle() {
            return this.title;
        }

        public boolean isAuthenticationRequired() {
            return this.mAuthenticationRequired;
        }

        public boolean isContextual() {
            return this.mIsContextual;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BadgeIconType {
    }

    public static class BigPictureStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$BigPictureStyle";
        private IconCompat mBigLargeIcon;
        private boolean mBigLargeIconSet;
        private CharSequence mPictureContentDescription;
        private IconCompat mPictureIcon;
        private boolean mShowBigPictureWhenCollapsed;

        private static class Api16Impl {
            private Api16Impl() {
            }

            static void setBigLargeIcon(Notification.BigPictureStyle r0, Bitmap r1) {
                r0.bigLargeIcon(r1);
            }

            static void setSummaryText(Notification.BigPictureStyle r0, CharSequence r1) {
                r0.setSummaryText(r1);
            }
        }

        private static class Api23Impl {
            private Api23Impl() {
            }

            static void setBigLargeIcon(Notification.BigPictureStyle r0, Icon r1) {
                r0.bigLargeIcon(r1);
            }
        }

        private static class Api31Impl {
            private Api31Impl() {
            }

            static void setBigPicture(Notification.BigPictureStyle r0, Icon r1) {
                r0.bigPicture(r1);
            }

            static void setContentDescription(Notification.BigPictureStyle r0, CharSequence r1) {
                r0.setContentDescription(r1);
            }

            static void showBigPictureWhenCollapsed(Notification.BigPictureStyle r0, boolean r1) {
                r0.showBigPictureWhenCollapsed(r1);
            }
        }

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder r1) {
            setBuilder(r1);
        }

        private static IconCompat asIconCompat(Parcelable r1) {
            if (r1 != null) goto L4;
            return null;
        L4:
            if ((r1 instanceof Icon) == false) goto L8;
            return IconCompat.createFromIcon((Icon) r1);
        L8:
            if ((r1 instanceof Bitmap) == true) goto L10;
            return null;
        L10:
            return IconCompat.createWithBitmap((Bitmap) r1);
        }

        public static IconCompat getPictureIcon(Bundle r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            Parcelable r0 = r2.getParcelable(NotificationCompat.EXTRA_PICTURE);
            if (r0 == null) goto L10;
            return asIconCompat(r0);
        L10:
            return asIconCompat(r2.getParcelable(NotificationCompat.EXTRA_PICTURE_ICON));
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r5) {
            Notification.BigPictureStyle r0 = new Notification.BigPictureStyle(r5.getBuilder()).setBigContentTitle(this.mBigContentTitle);
            if (this.mPictureIcon == null) goto L15;
            if (Build.VERSION.SDK_INT < 31) goto L11;
            Context r1 = null;
            if ((r5 instanceof NotificationCompatBuilder) == false) goto L9;
            r1 = ((NotificationCompatBuilder) r5).getContext();
        L9:
            Api31Impl.setBigPicture(r0, this.mPictureIcon.toIcon(r1));
            goto L15
        L11:
            if (this.mPictureIcon.getType() != 1) goto L15;
            r0 = r0.bigPicture(this.mPictureIcon.getBitmap());
        L15:
            if (this.mBigLargeIconSet == false) goto L24;
            if (this.mBigLargeIcon != null) goto L19;
            Api16Impl.setBigLargeIcon(r0, null);
            goto L24
        L19:
            Context r2 = null;
            if ((r5 instanceof NotificationCompatBuilder) == false) goto L22;
            r2 = ((NotificationCompatBuilder) r5).getContext();
        L22:
            Api23Impl.setBigLargeIcon(r0, this.mBigLargeIcon.toIcon(r2));
        L24:
            if (this.mSummaryTextSet == false) goto L27;
            Api16Impl.setSummaryText(r0, this.mSummaryText);
        L27:
            if (Build.VERSION.SDK_INT < 31) goto L30;
            Api31Impl.showBigPictureWhenCollapsed(r0, this.mShowBigPictureWhenCollapsed);
            Api31Impl.setContentDescription(r0, this.mPictureContentDescription);
            return;
        }

        public BigPictureStyle bigLargeIcon(Bitmap r2) {
            if (r2 != null) goto L4;
            IconCompat r0 = null;
        L5:
            this.mBigLargeIcon = r0;
            this.mBigLargeIconSet = true;
            return this;
        L4:
            r0 = IconCompat.createWithBitmap(r2);
            goto L5
        }

        public BigPictureStyle bigPicture(Bitmap r2) {
            if (r2 != null) goto L4;
            IconCompat r0 = null;
        L5:
            this.mPictureIcon = r0;
            return this;
        L4:
            r0 = IconCompat.createWithBitmap(r2);
            goto L5
        }

        public BigPictureStyle bigPicture(Icon r2) {
            this.mPictureIcon = IconCompat.createFromIcon(r2);
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle r2) {
            super.clearCompatExtraKeys(r2);
            r2.remove(NotificationCompat.EXTRA_LARGE_ICON_BIG);
            r2.remove(NotificationCompat.EXTRA_PICTURE);
            r2.remove(NotificationCompat.EXTRA_PICTURE_ICON);
            r2.remove(NotificationCompat.EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle r3) {
            super.restoreFromCompatExtras(r3);
            if (r3.containsKey(NotificationCompat.EXTRA_LARGE_ICON_BIG) == false) goto L5;
            this.mBigLargeIcon = asIconCompat(r3.getParcelable(NotificationCompat.EXTRA_LARGE_ICON_BIG));
            this.mBigLargeIconSet = true;
        L5:
            this.mPictureIcon = getPictureIcon(r3);
            this.mShowBigPictureWhenCollapsed = r3.getBoolean(NotificationCompat.EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED);
        }

        public BigPictureStyle setBigContentTitle(CharSequence r2) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(r2);
            return this;
        }

        public BigPictureStyle setContentDescription(CharSequence r1) {
            this.mPictureContentDescription = r1;
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence r2) {
            this.mSummaryText = Builder.limitCharSequenceLength(r2);
            this.mSummaryTextSet = true;
            return this;
        }

        public BigPictureStyle showBigPictureWhenCollapsed(boolean r1) {
            this.mShowBigPictureWhenCollapsed = r1;
            return this;
        }
    }

    public static class BigTextStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$BigTextStyle";
        private CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder r1) {
            setBuilder(r1);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void addCompatExtras(Bundle r1) {
            super.addCompatExtras(r1);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r3) {
            Notification.BigTextStyle r0 = new Notification.BigTextStyle(r3.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
            if (this.mSummaryTextSet == false) goto L6;
            r0.setSummaryText(this.mSummaryText);
            return;
        }

        public BigTextStyle bigText(CharSequence r2) {
            this.mBigText = Builder.limitCharSequenceLength(r2);
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle r2) {
            super.clearCompatExtraKeys(r2);
            r2.remove(NotificationCompat.EXTRA_BIG_TEXT);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle r2) {
            super.restoreFromCompatExtras(r2);
            this.mBigText = r2.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT);
        }

        public BigTextStyle setBigContentTitle(CharSequence r2) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(r2);
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence r2) {
            this.mSummaryText = Builder.limitCharSequenceLength(r2);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static final class BubbleMetadata {
        private static final int FLAG_AUTO_EXPAND_BUBBLE = 1;
        private static final int FLAG_SUPPRESS_NOTIFICATION = 2;
        private PendingIntent mDeleteIntent;
        private int mDesiredHeight;
        private int mDesiredHeightResId;
        private int mFlags;
        private IconCompat mIcon;
        private PendingIntent mPendingIntent;
        private String mShortcutId;

        private static class Api29Impl {
            private Api29Impl() {
            }

            static BubbleMetadata fromPlatform(Notification.BubbleMetadata r3) {
                if (r3 != null) goto L6;
                return null;
            L6:
                if (r3.getIntent() != null) goto L8;
                return null;
            L8:
                Builder r0 = new Builder(r3.getIntent(), IconCompat.createFromIcon(r3.getIcon())).setAutoExpandBubble(r3.getAutoExpandBubble()).setDeleteIntent(r3.getDeleteIntent()).setSuppressNotification(r3.isNotificationSuppressed());
                if (r3.getDesiredHeight() == 0) goto L12;
                r0.setDesiredHeight(r3.getDesiredHeight());
            L12:
                if (r3.getDesiredHeightResId() == 0) goto L15;
                r0.setDesiredHeightResId(r3.getDesiredHeightResId());
            L15:
                return r0.build();
            }

            static Notification.BubbleMetadata toPlatform(BubbleMetadata r2) {
                if (r2 != null) goto L6;
                return null;
            L6:
                if (r2.getIntent() != null) goto L8;
                return null;
            L8:
                Notification.BubbleMetadata.Builder r0 = new Notification.BubbleMetadata.Builder().setIcon(r2.getIcon().toIcon()).setIntent(r2.getIntent()).setDeleteIntent(r2.getDeleteIntent()).setAutoExpandBubble(r2.getAutoExpandBubble()).setSuppressNotification(r2.isNotificationSuppressed());
                if (r2.getDesiredHeight() == 0) goto L12;
                r0.setDesiredHeight(r2.getDesiredHeight());
            L12:
                if (r2.getDesiredHeightResId() == 0) goto L15;
                r0.setDesiredHeightResId(r2.getDesiredHeightResId());
            L15:
                return r0.build();
            }
        }

        private static class Api30Impl {
            private Api30Impl() {
            }

            static BubbleMetadata fromPlatform(Notification.BubbleMetadata r4) {
                if (r4 != null) goto L6;
                return null;
            L6:
                if (r4.getShortcutId() == null) goto L8;
                Builder r1 = new Builder(r4.getShortcutId());
            L9:
                r1.setAutoExpandBubble(r4.getAutoExpandBubble()).setDeleteIntent(r4.getDeleteIntent()).setSuppressNotification(r4.isNotificationSuppressed());
                if (r4.getDesiredHeight() == 0) goto L13;
                r1.setDesiredHeight(r4.getDesiredHeight());
            L13:
                if (r4.getDesiredHeightResId() == 0) goto L16;
                r1.setDesiredHeightResId(r4.getDesiredHeightResId());
            L16:
                return r1.build();
            L8:
                r1 = new Builder(r4.getIntent(), IconCompat.createFromIcon(r4.getIcon()));
                goto L9
            }

            static Notification.BubbleMetadata toPlatform(BubbleMetadata r4) {
                if (r4 != null) goto L6;
                return null;
            L6:
                if (r4.getShortcutId() == null) goto L8;
                Notification.BubbleMetadata.Builder r1 = new Notification.BubbleMetadata.Builder(r4.getShortcutId());
            L9:
                r1.setDeleteIntent(r4.getDeleteIntent()).setAutoExpandBubble(r4.getAutoExpandBubble()).setSuppressNotification(r4.isNotificationSuppressed());
                if (r4.getDesiredHeight() == 0) goto L13;
                r1.setDesiredHeight(r4.getDesiredHeight());
            L13:
                if (r4.getDesiredHeightResId() == 0) goto L16;
                r1.setDesiredHeightResId(r4.getDesiredHeightResId());
            L16:
                return r1.build();
            L8:
                r1 = new Notification.BubbleMetadata.Builder(r4.getIntent(), r4.getIcon().toIcon());
                goto L9
            }
        }

        public static final class Builder {
            private PendingIntent mDeleteIntent;
            private int mDesiredHeight;
            private int mDesiredHeightResId;
            private int mFlags;
            private IconCompat mIcon;
            private PendingIntent mPendingIntent;
            private String mShortcutId;

            @Deprecated
            public Builder() {
            }

            public Builder(PendingIntent r3, IconCompat r4) {
                if (r3 == null) goto L10;
                if (r4 == null) goto L8;
                this.mPendingIntent = r3;
                this.mIcon = r4;
                return;
            L8:
                throw new NullPointerException("Bubbles require non-null icon");
            L10:
                throw new NullPointerException("Bubble requires non-null pending intent");
            }

            public Builder(String r3) {
                if (TextUtils.isEmpty(r3) == true) goto L7;
                this.mShortcutId = r3;
                return;
            L7:
                throw new NullPointerException("Bubble requires a non-null shortcut id");
            }

            private Builder setFlag(int r3, boolean r4) {
                if (r4 == false) goto L4;
                this.mFlags |= r3;
            L5:
                return this;
            L4:
                this.mFlags &= ~r3;
                goto L5
            }

            public BubbleMetadata build() {
                if (this.mShortcutId != null) goto L10;
                if (this.mPendingIntent != null) goto L10;
                throw new NullPointerException("Must supply pending intent or shortcut to bubble");
            L10:
                if (this.mShortcutId == null) goto L12;
            L16:
                BubbleMetadata r2 = new BubbleMetadata(this.mPendingIntent, this.mDeleteIntent, this.mIcon, this.mDesiredHeight, this.mDesiredHeightResId, this.mFlags, this.mShortcutId, null);
                r2.setFlags(this.mFlags);
                return r2;
            L12:
                if (this.mIcon != null) goto L16;
                throw new NullPointerException("Must supply an icon or shortcut for the bubble");
            }

            public Builder setAutoExpandBubble(boolean r2) {
                setFlag(1, r2);
                return this;
            }

            public Builder setDeleteIntent(PendingIntent r1) {
                this.mDeleteIntent = r1;
                return this;
            }

            public Builder setDesiredHeight(int r3) {
                this.mDesiredHeight = Math.max(r3, 0);
                this.mDesiredHeightResId = 0;
                return this;
            }

            public Builder setDesiredHeightResId(int r2) {
                this.mDesiredHeightResId = r2;
                this.mDesiredHeight = 0;
                return this;
            }

            public Builder setIcon(IconCompat r3) {
                if (this.mShortcutId != null) goto L10;
                if (r3 == null) goto L8;
                this.mIcon = r3;
                return this;
            L8:
                throw new NullPointerException("Bubbles require non-null icon");
            L10:
                throw new IllegalStateException("Created as a shortcut bubble, cannot set an Icon. Consider using BubbleMetadata.Builder(PendingIntent,Icon) instead.");
            }

            public Builder setIntent(PendingIntent r3) {
                if (this.mShortcutId != null) goto L10;
                if (r3 == null) goto L8;
                this.mPendingIntent = r3;
                return this;
            L8:
                throw new NullPointerException("Bubble requires non-null pending intent");
            L10:
                throw new IllegalStateException("Created as a shortcut bubble, cannot set a PendingIntent. Consider using BubbleMetadata.Builder(PendingIntent,Icon) instead.");
            }

            public Builder setSuppressNotification(boolean r2) {
                setFlag(2, r2);
                return this;
            }
        }

        private BubbleMetadata(PendingIntent r1, PendingIntent r2, IconCompat r3, int r4, int r5, int r6, String r7) {
            this.mPendingIntent = r1;
            this.mIcon = r3;
            this.mDesiredHeight = r4;
            this.mDesiredHeightResId = r5;
            this.mDeleteIntent = r2;
            this.mFlags = r6;
            this.mShortcutId = r7;
        }

        /* synthetic */ BubbleMetadata(PendingIntent r1, PendingIntent r2, IconCompat r3, int r4, int r5, int r6, String r7, C00521 r8) {
            this(r1, r2, r3, r4, r5, r6, r7);
        }

        public static BubbleMetadata fromPlatform(Notification.BubbleMetadata r3) {
            if (r3 != null) goto L6;
            return null;
        L6:
            if (Build.VERSION.SDK_INT < 30) goto L10;
            return Api30Impl.fromPlatform(r3);
        L10:
            if (Build.VERSION.SDK_INT == 29) goto L12;
            return null;
        L12:
            return Api29Impl.fromPlatform(r3);
        }

        public static Notification.BubbleMetadata toPlatform(BubbleMetadata r3) {
            if (r3 != null) goto L6;
            return null;
        L6:
            if (Build.VERSION.SDK_INT < 30) goto L10;
            return Api30Impl.toPlatform(r3);
        L10:
            if (Build.VERSION.SDK_INT == 29) goto L12;
            return null;
        L12:
            return Api29Impl.toPlatform(r3);
        }

        public boolean getAutoExpandBubble() {
            if ((this.mFlags & 1) != 0) goto L7;
            return false;
        L7:
            return true;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public int getDesiredHeight() {
            return this.mDesiredHeight;
        }

        public int getDesiredHeightResId() {
            return this.mDesiredHeightResId;
        }

        public IconCompat getIcon() {
            return this.mIcon;
        }

        public PendingIntent getIntent() {
            return this.mPendingIntent;
        }

        public String getShortcutId() {
            return this.mShortcutId;
        }

        public boolean isNotificationSuppressed() {
            if ((this.mFlags & 2) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        public void setFlags(int r1) {
            this.mFlags = r1;
        }
    }

    public static class Builder {
        private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
        public ArrayList<Action> mActions;
        boolean mAllowSystemGeneratedContextualActions;
        int mBadgeIcon;
        RemoteViews mBigContentView;
        BubbleMetadata mBubbleMetadata;
        String mCategory;
        String mChannelId;
        boolean mChronometerCountDown;
        int mColor;
        boolean mColorized;
        boolean mColorizedSet;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        int mFgsDeferBehavior;
        PendingIntent mFullScreenIntent;
        int mGroupAlertBehavior;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        ArrayList<Action> mInvisibleActions;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        LocusIdCompat mLocusId;
        Notification mNotification;
        int mNumber;

        @Deprecated
        public ArrayList<String> mPeople;
        public ArrayList<Person> mPersonList;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        CharSequence mSettingsText;
        String mShortcutId;
        boolean mShowWhen;
        boolean mSilent;
        Icon mSmallIcon;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility;

        @Deprecated
        public Builder(Context r3) {
            String r1 = (String) null;
            this(r3, null);
        }

        public Builder(Context r9, Notification r10) {
            this(r9, NotificationCompat.getChannelId(r10));
            Bundle r0 = r10.extras;
            Style r1 = Style.extractStyleFromNotification(r10);
            setContentTitle(NotificationCompat.getContentTitle(r10)).setContentText(NotificationCompat.getContentText(r10)).setContentInfo(NotificationCompat.getContentInfo(r10)).setSubText(NotificationCompat.getSubText(r10)).setSettingsText(NotificationCompat.getSettingsText(r10)).setStyle(r1).setContentIntent(r10.contentIntent).setGroup(NotificationCompat.getGroup(r10)).setGroupSummary(NotificationCompat.isGroupSummary(r10)).setLocusId(NotificationCompat.getLocusId(r10)).setWhen(r10.when).setShowWhen(NotificationCompat.getShowWhen(r10)).setUsesChronometer(NotificationCompat.getUsesChronometer(r10)).setAutoCancel(NotificationCompat.getAutoCancel(r10)).setOnlyAlertOnce(NotificationCompat.getOnlyAlertOnce(r10)).setOngoing(NotificationCompat.getOngoing(r10)).setLocalOnly(NotificationCompat.getLocalOnly(r10)).setLargeIcon(r10.largeIcon).setBadgeIconType(NotificationCompat.getBadgeIconType(r10)).setCategory(NotificationCompat.getCategory(r10)).setBubbleMetadata(NotificationCompat.getBubbleMetadata(r10)).setNumber(r10.number).setTicker(r10.tickerText).setContentIntent(r10.contentIntent).setDeleteIntent(r10.deleteIntent).setFullScreenIntent(r10.fullScreenIntent, NotificationCompat.getHighPriority(r10)).setSound(r10.sound, r10.audioStreamType).setVibrate(r10.vibrate).setLights(r10.ledARGB, r10.ledOnMS, r10.ledOffMS).setDefaults(r10.defaults).setPriority(r10.priority).setColor(NotificationCompat.getColor(r10)).setVisibility(NotificationCompat.getVisibility(r10)).setPublicVersion(NotificationCompat.getPublicVersion(r10)).setSortKey(NotificationCompat.getSortKey(r10)).setTimeoutAfter(NotificationCompat.getTimeoutAfter(r10)).setShortcutId(NotificationCompat.getShortcutId(r10)).setProgress(r0.getInt(NotificationCompat.EXTRA_PROGRESS_MAX), r0.getInt(NotificationCompat.EXTRA_PROGRESS), r0.getBoolean(NotificationCompat.EXTRA_PROGRESS_INDETERMINATE)).setAllowSystemGeneratedContextualActions(NotificationCompat.getAllowSystemGeneratedContextualActions(r10)).setSmallIcon(r10.icon, r10.iconLevel).addExtras(getExtrasWithoutDuplicateData(r10, r1));
            this.mSmallIcon = r10.getSmallIcon();
            int r3 = 0;
            if (r10.actions != null) goto L5;
        L9:
            List<Action> r2 = NotificationCompat.getInvisibleActions(r10);
            if (r2.isEmpty() == true) goto L15;
            Iterator<Action> r4 = r2.iterator();
        L13:
            if (r4.hasNext() == false) goto L15;
            addInvisibleAction(r4.next());
        L15:
            String[] r5 = r10.extras.getStringArray(NotificationCompat.EXTRA_PEOPLE);
            if (r5 == null) goto L23;
            if (r5.length == 0) goto L23;
            int r6 = r5.length;
        L20:
            if (r3 >= r6) goto L23;
            addPerson(r5[r3]);
            r3 = r3 + 1;
        L23:
            if (Build.VERSION.SDK_INT < 28) goto L33;
            ArrayList r7 = r10.extras.getParcelableArrayList(NotificationCompat.EXTRA_PEOPLE_LIST);
            if (r7 == null) goto L33;
            if (r7.isEmpty() == true) goto L33;
            Iterator r8 = r7.iterator();
        L30:
            if (r8.hasNext() == false) goto L33;
            addPerson(Person.fromAndroidPerson((android.app.Person) r8.next()));
        L33:
            if (Build.VERSION.SDK_INT < 24) goto L38;
            if (r0.containsKey(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN) == false) goto L38;
            setChronometerCountDown(r0.getBoolean(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN));
        L38:
            if (Build.VERSION.SDK_INT >= 26) goto L40;
            return;
        L40:
            if (r0.containsKey(NotificationCompat.EXTRA_COLORIZED) == false) goto L48;
            setColorized(r0.getBoolean(NotificationCompat.EXTRA_COLORIZED));
            return;
        L48:
            return;
        L5:
            if (r10.actions.length == 0) goto L9;
            Notification.Action[] r11 = r10.actions;
            int r12 = r11.length;
            int r13 = 0;
        L7:
            if (r13 >= r12) goto L9;
            addAction(Action.Builder.fromAndroidAction(r11[r13]).build());
            r13 = r13 + 1;
            goto L7
        }

        public Builder(Context r6, String r7) {
            this.mActions = new ArrayList();
            this.mPersonList = new ArrayList();
            this.mInvisibleActions = new ArrayList();
            this.mShowWhen = true;
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            this.mFgsDeferBehavior = 0;
            this.mNotification = new Notification();
            this.mContext = r6;
            this.mChannelId = r7;
            Notification r2 = this.mNotification;
            r2.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList();
            this.mAllowSystemGeneratedContextualActions = true;
        }

        private static Bundle getExtrasWithoutDuplicateData(Notification r4, Style r5) {
            if (r4.extras != null) goto L6;
            return null;
        L6:
            Bundle r0 = new Bundle(r4.extras);
            r0.remove(NotificationCompat.EXTRA_TITLE);
            r0.remove(NotificationCompat.EXTRA_TEXT);
            r0.remove(NotificationCompat.EXTRA_INFO_TEXT);
            r0.remove(NotificationCompat.EXTRA_SUB_TEXT);
            r0.remove(NotificationCompat.EXTRA_CHANNEL_ID);
            r0.remove(NotificationCompat.EXTRA_CHANNEL_GROUP_ID);
            r0.remove(NotificationCompat.EXTRA_SHOW_WHEN);
            r0.remove(NotificationCompat.EXTRA_PROGRESS);
            r0.remove(NotificationCompat.EXTRA_PROGRESS_MAX);
            r0.remove(NotificationCompat.EXTRA_PROGRESS_INDETERMINATE);
            r0.remove(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN);
            r0.remove(NotificationCompat.EXTRA_COLORIZED);
            r0.remove(NotificationCompat.EXTRA_PEOPLE_LIST);
            r0.remove(NotificationCompat.EXTRA_PEOPLE);
            r0.remove(NotificationCompatExtras.EXTRA_SORT_KEY);
            r0.remove(NotificationCompatExtras.EXTRA_GROUP_KEY);
            r0.remove(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
            r0.remove(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
            r0.remove(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
            Bundle r2 = r0.getBundle("android.car.EXTENSIONS");
            if (r2 == null) goto L9;
            Bundle r3 = new Bundle(r2);
            r3.remove("invisible_actions");
            r0.putBundle("android.car.EXTENSIONS", r3);
        L9:
            if (r5 == null) goto L11;
            r5.clearCompatExtraKeys(r0);
        L11:
            return r0;
        }

        protected static CharSequence limitCharSequenceLength(CharSequence r2) {
            if (r2 != null) goto L5;
            return r2;
        L5:
            if (r2.length() > MAX_CHARSEQUENCE_LENGTH) goto L7;
            return r2;
        L7:
            return r2.subSequence(0, MAX_CHARSEQUENCE_LENGTH);
        }

        private Bitmap reduceLargeIconSize(Bitmap r12) {
            if (r12 != null) goto L4;
        L13:
            return r12;
        L4:
            if (Build.VERSION.SDK_INT >= 27) goto L13;
            Resources r0 = this.mContext.getResources();
            int r1 = r0.getDimensionPixelSize(C0039R.dimen.compat_notification_large_icon_max_width);
            int r2 = r0.getDimensionPixelSize(C0039R.dimen.compat_notification_large_icon_max_height);
            if (r12.getWidth() <= r1) goto L9;
        L11:
            double r3 = Math.min(((double) r1) / ((double) Math.max(1, r12.getWidth())), ((double) r2) / ((double) Math.max(1, r12.getHeight())));
            return Bitmap.createScaledBitmap(r12, (int) Math.ceil(((double) r12.getWidth()) * r3), (int) Math.ceil(((double) r12.getHeight()) * r3), true);
        L9:
            if (r12.getHeight() > r2) goto L11;
            return r12;
        }

        private void setFlag(int r4, boolean r5) {
            if (r5 == false) goto L4;
            this.mNotification.flags |= r4;
            return;
        L4:
            this.mNotification.flags &= ~r4;
        }

        private boolean useExistingRemoteView() {
            if (this.mStyle != null) goto L5;
        L8:
            return true;
        L5:
            if (this.mStyle.displayCustomViewInline() == false) goto L8;
            return false;
        }

        public Builder addAction(int r3, CharSequence r4, PendingIntent r5) {
            this.mActions.add(new Action(r3, r4, r5));
            return this;
        }

        public Builder addAction(Action r2) {
            if (r2 == null) goto L4;
            this.mActions.add(r2);
        L4:
            return this;
        }

        public Builder addExtras(Bundle r2) {
            if (r2 != null) goto L4;
        L7:
            return this;
        L4:
            if (this.mExtras != null) goto L6;
            this.mExtras = new Bundle(r2);
            goto L7
        L6:
            this.mExtras.putAll(r2);
            goto L7
        }

        public Builder addInvisibleAction(int r3, CharSequence r4, PendingIntent r5) {
            this.mInvisibleActions.add(new Action(r3, r4, r5));
            return this;
        }

        public Builder addInvisibleAction(Action r2) {
            if (r2 == null) goto L4;
            this.mInvisibleActions.add(r2);
        L4:
            return this;
        }

        public Builder addPerson(Person r2) {
            if (r2 == null) goto L4;
            this.mPersonList.add(r2);
        L4:
            return this;
        }

        @Deprecated
        public Builder addPerson(String r2) {
            if (r2 != null) goto L4;
        L6:
            return this;
        L4:
            if (r2.isEmpty() == true) goto L6;
            this.mPeople.add(r2);
            goto L6
        }

        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }

        public Builder clearActions() {
            this.mActions.clear();
            return this;
        }

        public Builder clearInvisibleActions() {
            this.mInvisibleActions.clear();
            Bundle r0 = this.mExtras.getBundle("android.car.EXTENSIONS");
            if (r0 == null) goto L5;
            Bundle r2 = new Bundle(r0);
            r2.remove("invisible_actions");
            this.mExtras.putBundle("android.car.EXTENSIONS", r2);
        L5:
            return this;
        }

        public Builder clearPeople() {
            this.mPersonList.clear();
            this.mPeople.clear();
            return this;
        }

        public RemoteViews createBigContentView() {
            if (this.mBigContentView != null) goto L5;
        L8:
            NotificationCompatBuilder r0 = new NotificationCompatBuilder(this);
            if (this.mStyle == null) goto L13;
            RemoteViews r1 = this.mStyle.makeBigContentView(r0);
            if (r1 == null) goto L13;
            return r1;
        L13:
            Notification r2 = r0.build();
            if (Build.VERSION.SDK_INT < 24) goto L18;
            return Notification.Builder.recoverBuilder(this.mContext, r2).createBigContentView();
        L18:
            return r2.bigContentView;
        L5:
            if (useExistingRemoteView() == false) goto L8;
            return this.mBigContentView;
        }

        public RemoteViews createContentView() {
            if (this.mContentView != null) goto L5;
        L8:
            NotificationCompatBuilder r0 = new NotificationCompatBuilder(this);
            if (this.mStyle == null) goto L13;
            RemoteViews r1 = this.mStyle.makeContentView(r0);
            if (r1 == null) goto L13;
            return r1;
        L13:
            Notification r2 = r0.build();
            if (Build.VERSION.SDK_INT < 24) goto L18;
            return Notification.Builder.recoverBuilder(this.mContext, r2).createContentView();
        L18:
            return r2.contentView;
        L5:
            if (useExistingRemoteView() == false) goto L8;
            return this.mContentView;
        }

        public RemoteViews createHeadsUpContentView() {
            if (this.mHeadsUpContentView != null) goto L5;
        L8:
            NotificationCompatBuilder r0 = new NotificationCompatBuilder(this);
            if (this.mStyle == null) goto L13;
            RemoteViews r1 = this.mStyle.makeHeadsUpContentView(r0);
            if (r1 == null) goto L13;
            return r1;
        L13:
            Notification r2 = r0.build();
            if (Build.VERSION.SDK_INT < 24) goto L18;
            return Notification.Builder.recoverBuilder(this.mContext, r2).createHeadsUpContentView();
        L18:
            return r2.headsUpContentView;
        L5:
            if (useExistingRemoteView() == false) goto L8;
            return this.mHeadsUpContentView;
        }

        public Builder extend(Extender r1) {
            r1.extend(this);
            return this;
        }

        public RemoteViews getBigContentView() {
            return this.mBigContentView;
        }

        public BubbleMetadata getBubbleMetadata() {
            return this.mBubbleMetadata;
        }

        public int getColor() {
            return this.mColor;
        }

        public RemoteViews getContentView() {
            return this.mContentView;
        }

        public Bundle getExtras() {
            if (this.mExtras != null) goto L6;
            this.mExtras = new Bundle();
        L6:
            return this.mExtras;
        }

        public int getForegroundServiceBehavior() {
            return this.mFgsDeferBehavior;
        }

        public RemoteViews getHeadsUpContentView() {
            return this.mHeadsUpContentView;
        }

        @Deprecated
        public Notification getNotification() {
            return build();
        }

        public int getPriority() {
            return this.mPriority;
        }

        public long getWhenIfShowing() {
            if (this.mShowWhen == true) goto L7;
            return 0;
        L7:
            return this.mNotification.when;
        }

        public Builder setAllowSystemGeneratedContextualActions(boolean r1) {
            this.mAllowSystemGeneratedContextualActions = r1;
            return this;
        }

        public Builder setAutoCancel(boolean r2) {
            setFlag(16, r2);
            return this;
        }

        public Builder setBadgeIconType(int r1) {
            this.mBadgeIcon = r1;
            return this;
        }

        public Builder setBubbleMetadata(BubbleMetadata r1) {
            this.mBubbleMetadata = r1;
            return this;
        }

        public Builder setCategory(String r1) {
            this.mCategory = r1;
            return this;
        }

        public Builder setChannelId(String r1) {
            this.mChannelId = r1;
            return this;
        }

        public Builder setChronometerCountDown(boolean r3) {
            this.mChronometerCountDown = r3;
            getExtras().putBoolean(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN, r3);
            return this;
        }

        public Builder setColor(int r1) {
            this.mColor = r1;
            return this;
        }

        public Builder setColorized(boolean r2) {
            this.mColorized = r2;
            this.mColorizedSet = true;
            return this;
        }

        public Builder setContent(RemoteViews r2) {
            this.mNotification.contentView = r2;
            return this;
        }

        public Builder setContentInfo(CharSequence r2) {
            this.mContentInfo = limitCharSequenceLength(r2);
            return this;
        }

        public Builder setContentIntent(PendingIntent r1) {
            this.mContentIntent = r1;
            return this;
        }

        public Builder setContentText(CharSequence r2) {
            this.mContentText = limitCharSequenceLength(r2);
            return this;
        }

        public Builder setContentTitle(CharSequence r2) {
            this.mContentTitle = limitCharSequenceLength(r2);
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews r1) {
            this.mBigContentView = r1;
            return this;
        }

        public Builder setCustomContentView(RemoteViews r1) {
            this.mContentView = r1;
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews r1) {
            this.mHeadsUpContentView = r1;
            return this;
        }

        public Builder setDefaults(int r3) {
            this.mNotification.defaults = r3;
            if ((r3 & 4) == 0) goto L5;
            this.mNotification.flags |= 1;
        L5:
            return this;
        }

        public Builder setDeleteIntent(PendingIntent r2) {
            this.mNotification.deleteIntent = r2;
            return this;
        }

        public Builder setExtras(Bundle r1) {
            this.mExtras = r1;
            return this;
        }

        public Builder setForegroundServiceBehavior(int r1) {
            this.mFgsDeferBehavior = r1;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent r2, boolean r3) {
            this.mFullScreenIntent = r2;
            setFlag(128, r3);
            return this;
        }

        public Builder setGroup(String r1) {
            this.mGroupKey = r1;
            return this;
        }

        public Builder setGroupAlertBehavior(int r1) {
            this.mGroupAlertBehavior = r1;
            return this;
        }

        public Builder setGroupSummary(boolean r1) {
            this.mGroupSummary = r1;
            return this;
        }

        public Builder setLargeIcon(Bitmap r2) {
            this.mLargeIcon = reduceLargeIconSize(r2);
            return this;
        }

        public Builder setLights(int r6, int r7, int r8) {
            this.mNotification.ledARGB = r6;
            this.mNotification.ledOnMS = r7;
            this.mNotification.ledOffMS = r8;
            int r1 = 1;
            if (this.mNotification.ledOnMS != 0) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Notification r3 = this.mNotification;
            int r4 = this.mNotification.flags & (-2);
            if (r0 == true) goto L12;
            r1 = 0;
        L12:
            r3.flags = r1 | r4;
            return this;
        L5:
            if (this.mNotification.ledOffMS == 0) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setLocalOnly(boolean r1) {
            this.mLocalOnly = r1;
            return this;
        }

        public Builder setLocusId(LocusIdCompat r1) {
            this.mLocusId = r1;
            return this;
        }

        @Deprecated
        public Builder setNotificationSilent() {
            this.mSilent = true;
            return this;
        }

        public Builder setNumber(int r1) {
            this.mNumber = r1;
            return this;
        }

        public Builder setOngoing(boolean r2) {
            setFlag(2, r2);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean r2) {
            setFlag(8, r2);
            return this;
        }

        public Builder setPriority(int r1) {
            this.mPriority = r1;
            return this;
        }

        public Builder setProgress(int r1, int r2, boolean r3) {
            this.mProgressMax = r1;
            this.mProgress = r2;
            this.mProgressIndeterminate = r3;
            return this;
        }

        public Builder setPublicVersion(Notification r1) {
            this.mPublicVersion = r1;
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] r1) {
            this.mRemoteInputHistory = r1;
            return this;
        }

        public Builder setSettingsText(CharSequence r2) {
            this.mSettingsText = limitCharSequenceLength(r2);
            return this;
        }

        public Builder setShortcutId(String r1) {
            this.mShortcutId = r1;
            return this;
        }

        public Builder setShortcutInfo(ShortcutInfoCompat r3) {
            if (r3 != null) goto L4;
            return this;
        L4:
            this.mShortcutId = r3.getId();
            if (this.mLocusId != null) goto L13;
            if (r3.getLocusId() == null) goto L10;
            this.mLocusId = r3.getLocusId();
            goto L13
        L10:
            if (r3.getId() == null) goto L13;
            this.mLocusId = new LocusIdCompat(r3.getId());
        L13:
            if (this.mContentTitle != null) goto L15;
            setContentTitle(r3.getShortLabel());
        L15:
            return this;
        }

        public Builder setShowWhen(boolean r1) {
            this.mShowWhen = r1;
            return this;
        }

        public Builder setSilent(boolean r1) {
            this.mSilent = r1;
            return this;
        }

        public Builder setSmallIcon(int r2) {
            this.mNotification.icon = r2;
            return this;
        }

        public Builder setSmallIcon(int r2, int r3) {
            this.mNotification.icon = r2;
            this.mNotification.iconLevel = r3;
            return this;
        }

        public Builder setSmallIcon(IconCompat r2) {
            this.mSmallIcon = r2.toIcon(this.mContext);
            return this;
        }

        public Builder setSortKey(String r1) {
            this.mSortKey = r1;
            return this;
        }

        public Builder setSound(Uri r4) {
            this.mNotification.sound = r4;
            this.mNotification.audioStreamType = -1;
            Notification r0 = this.mNotification;
            r0.audioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
            return this;
        }

        public Builder setSound(Uri r4, int r5) {
            this.mNotification.sound = r4;
            this.mNotification.audioStreamType = r5;
            Notification r0 = this.mNotification;
            r0.audioAttributes = new AudioAttributes.Builder().setContentType(4).setLegacyStreamType(r5).build();
            return this;
        }

        public Builder setStyle(Style r2) {
            if (this.mStyle == r2) goto L7;
            this.mStyle = r2;
            if (this.mStyle == null) goto L7;
            this.mStyle.setBuilder(this);
        L7:
            return this;
        }

        public Builder setSubText(CharSequence r2) {
            this.mSubText = limitCharSequenceLength(r2);
            return this;
        }

        public Builder setTicker(CharSequence r3) {
            Notification r0 = this.mNotification;
            r0.tickerText = limitCharSequenceLength(r3);
            return this;
        }

        @Deprecated
        public Builder setTicker(CharSequence r3, RemoteViews r4) {
            Notification r0 = this.mNotification;
            r0.tickerText = limitCharSequenceLength(r3);
            this.mTickerView = r4;
            return this;
        }

        public Builder setTimeoutAfter(long r1) {
            this.mTimeout = r1;
            return this;
        }

        public Builder setUsesChronometer(boolean r1) {
            this.mUseChronometer = r1;
            return this;
        }

        public Builder setVibrate(long[] r2) {
            this.mNotification.vibrate = r2;
            return this;
        }

        public Builder setVisibility(int r1) {
            this.mVisibility = r1;
            return this;
        }

        public Builder setWhen(long r2) {
            this.mNotification.when = r2;
            return this;
        }
    }

    public static final class CarExtender implements Extender {
        static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        static final String EXTRA_INVISIBLE_ACTIONS = "invisible_actions";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String KEY_AUTHOR = "author";
        private static final String KEY_MESSAGES = "messages";
        private static final String KEY_ON_READ = "on_read";
        private static final String KEY_ON_REPLY = "on_reply";
        private static final String KEY_PARTICIPANTS = "participants";
        private static final String KEY_REMOTE_INPUT = "remote_input";
        private static final String KEY_TEXT = "text";
        private static final String KEY_TIMESTAMP = "timestamp";
        private int mColor;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        @Deprecated
        public static class UnreadConversation {
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            public static class Builder {
                private long mLatestTimestamp;
                private final List<String> mMessages;
                private final String mParticipant;
                private PendingIntent mReadPendingIntent;
                private RemoteInput mRemoteInput;
                private PendingIntent mReplyPendingIntent;

                public Builder(String r2) {
                    this.mMessages = new ArrayList();
                    this.mParticipant = r2;
                }

                public Builder addMessage(String r2) {
                    if (r2 == null) goto L4;
                    this.mMessages.add(r2);
                L4:
                    return this;
                }

                public UnreadConversation build() {
                    String[] r2 = (String[]) this.mMessages.toArray(new String[this.mMessages.size()]);
                    String[] r6 = {this.mParticipant};
                    return new UnreadConversation(r2, this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, r6, this.mLatestTimestamp);
                }

                public Builder setLatestTimestamp(long r1) {
                    this.mLatestTimestamp = r1;
                    return this;
                }

                public Builder setReadPendingIntent(PendingIntent r1) {
                    this.mReadPendingIntent = r1;
                    return this;
                }

                public Builder setReplyAction(PendingIntent r1, RemoteInput r2) {
                    this.mRemoteInput = r2;
                    this.mReplyPendingIntent = r1;
                    return this;
                }
            }

            UnreadConversation(String[] r1, RemoteInput r2, PendingIntent r3, PendingIntent r4, String[] r5, long r6) {
                this.mMessages = r1;
                this.mRemoteInput = r2;
                this.mReadPendingIntent = r4;
                this.mReplyPendingIntent = r3;
                this.mParticipants = r5;
                this.mLatestTimestamp = r6;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public String getParticipant() {
                if (this.mParticipants.length > 0) goto L7;
                return null;
            L7:
                return this.mParticipants[0];
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }
        }

        public CarExtender() {
            this.mColor = 0;
        }

        public CarExtender(Notification r4) {
            this.mColor = 0;
            if (NotificationCompat.getExtras(r4) != null) goto L5;
            Bundle r1 = null;
        L6:
            if (r1 == null) goto L9;
            this.mLargeIcon = (Bitmap) r1.getParcelable(EXTRA_LARGE_ICON);
            this.mColor = r1.getInt(EXTRA_COLOR, 0);
            this.mUnreadConversation = getUnreadConversationFromBundle(r1.getBundle(EXTRA_CONVERSATION));
            return;
        L9:
            return;
        L5:
            r1 = NotificationCompat.getExtras(r4).getBundle(EXTRA_CAR_EXTENDER);
            goto L6
        }

        private static Bundle getBundleForUnreadConversation(UnreadConversation r7) {
            Bundle r0 = new Bundle();
            String r1 = null;
            if (r7.getParticipants() != null) goto L5;
        L7:
            Parcelable[] r2 = new Parcelable[r7.getMessages().length];
            int r3 = 0;
        L9:
            if (r3 >= r2.length) goto L11;
            Bundle r4 = new Bundle();
            r4.putString("text", r7.getMessages()[r3]);
            r4.putString(KEY_AUTHOR, r1);
            r2[r3] = r4;
            r3 = r3 + 1;
            goto L9
        L11:
            r0.putParcelableArray(KEY_MESSAGES, r2);
            RemoteInput r5 = r7.getRemoteInput();
            if (r5 == null) goto L14;
            r0.putParcelable(KEY_REMOTE_INPUT, new android.app.RemoteInput.Builder(r5.getResultKey()).setLabel(r5.getLabel()).setChoices(r5.getChoices()).setAllowFreeFormInput(r5.getAllowFreeFormInput()).addExtras(r5.getExtras()).build());
        L14:
            r0.putParcelable(KEY_ON_REPLY, r7.getReplyPendingIntent());
            r0.putParcelable(KEY_ON_READ, r7.getReadPendingIntent());
            r0.putStringArray(KEY_PARTICIPANTS, r7.getParticipants());
            r0.putLong(KEY_TIMESTAMP, r7.getLatestTimestamp());
            return r0;
        L5:
            if (r7.getParticipants().length <= 1) goto L7;
            r1 = r7.getParticipants()[0];
            goto L7
        }

        private static UnreadConversation getUnreadConversationFromBundle(Bundle r20) {
            if (r20 != null) goto L5;
            return null;
        L5:
            Parcelable[] r2 = r20.getParcelableArray(KEY_MESSAGES);
            if (r2 == null) goto L20;
            String[] r4 = new String[r2.length];
            boolean r5 = true;
            int r6 = 0;
        L9:
            if (r6 >= r4.length) goto L17;
            if ((r2[r6] instanceof Bundle) == false) goto L12;
            r4[r6] = ((Bundle) r2[r6]).getString("text");
            if (r4[r6] == null) goto L15;
            r6 = r6 + 1;
            goto L9
        L15:
            r5 = false;
            goto L17
        L12:
            r5 = false;
        L17:
            if (r5 == false) goto L19;
            String[] r7 = r4;
        L21:
            PendingIntent r10 = (PendingIntent) r20.getParcelable(KEY_ON_READ);
            PendingIntent r9 = (PendingIntent) r20.getParcelable(KEY_ON_REPLY);
            android.app.RemoteInput r3 = (android.app.RemoteInput) r20.getParcelable(KEY_REMOTE_INPUT);
            String[] r11 = r20.getStringArray(KEY_PARTICIPANTS);
            if (r11 != null) goto L24;
        L35:
            return null;
        L24:
            if (r11.length != 1) goto L35;
            if (r3 == null) goto L32;
            String r13 = r3.getResultKey();
            CharSequence r14 = r3.getLabel();
            CharSequence[] r15 = r3.getChoices();
            boolean r16 = r3.getAllowFreeFormInput();
            if (Build.VERSION.SDK_INT < 29) goto L30;
            int r17 = r3.getEditChoicesBeforeSending();
        L31:
            RemoteInput r8 = new RemoteInput(r13, r14, r15, r16, r17, r3.getExtras(), null);
        L34:
            return new UnreadConversation(r7, r8, r9, r10, r11, r20.getLong(KEY_TIMESTAMP));
        L30:
            r17 = 0;
            goto L31
        L32:
            r8 = null;
            goto L34
        L19:
            return null;
        L20:
            r7 = null;
            goto L21
        }

        @Override // androidx.core.app.NotificationCompat.Extender
        public Builder extend(Builder r4) {
            Bundle r0 = new Bundle();
            if (this.mLargeIcon == null) goto L6;
            r0.putParcelable(EXTRA_LARGE_ICON, this.mLargeIcon);
        L6:
            if (this.mColor == 0) goto L9;
            r0.putInt(EXTRA_COLOR, this.mColor);
        L9:
            if (this.mUnreadConversation == null) goto L11;
            r0.putBundle(EXTRA_CONVERSATION, getBundleForUnreadConversation(this.mUnreadConversation));
        L11:
            r4.getExtras().putBundle(EXTRA_CAR_EXTENDER, r0);
            return r4;
        }

        public int getColor() {
            return this.mColor;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        @Deprecated
        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }

        public CarExtender setColor(int r1) {
            this.mColor = r1;
            return this;
        }

        public CarExtender setLargeIcon(Bitmap r1) {
            this.mLargeIcon = r1;
            return this;
        }

        @Deprecated
        public CarExtender setUnreadConversation(UnreadConversation r1) {
            this.mUnreadConversation = r1;
            return this;
        }
    }

    public static class DecoratedCustomViewStyle extends Style {
        private static final int MAX_ACTION_BUTTONS = 3;
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$DecoratedCustomViewStyle";

        public DecoratedCustomViewStyle() {
        }

        private RemoteViews createRemoteViews(RemoteViews r9, boolean r10) {
            int r2 = 0;
            RemoteViews r0 = applyStandardTemplate(true, C0039R.layout.notification_template_custom_big, false);
            r0.removeAllViews(C0039R.id.actions);
            boolean r1 = false;
            List<Action> r3 = getNonContextualActions(this.mBuilder.mActions);
            if (r10 == false) goto L10;
            if (r3 == null) goto L10;
            int r4 = Math.min(r3.size(), 3);
            if (r4 <= 0) goto L10;
            r1 = true;
            int r5 = 0;
        L8:
            if (r5 >= r4) goto L10;
            RemoteViews r6 = generateActionButton(r3.get(r5));
            r0.addView(C0039R.id.actions, r6);
            r5 = r5 + 1;
        L10:
            if (r1 == true) goto L13;
            r2 = 8;
        L13:
            r0.setViewVisibility(C0039R.id.actions, r2);
            r0.setViewVisibility(C0039R.id.action_divider, r2);
            buildIntoRemoteViews(r0, r9);
            return r0;
        }

        private RemoteViews generateActionButton(Action r7) {
            if (r7.actionIntent != null) goto L5;
            boolean r0 = true;
        L6:
            String r2 = this.mBuilder.mContext.getPackageName();
            if (r0 == false) goto L9;
            int r3 = C0039R.layout.notification_action_tombstone;
        L10:
            RemoteViews r1 = new RemoteViews(r2, r3);
            IconCompat r4 = r7.getIconCompat();
            if (r4 == null) goto L13;
            r1.setImageViewBitmap(C0039R.id.action_image, createColoredBitmap(r4, this.mBuilder.mContext.getResources().getColor(C0039R.color.notification_action_color_filter)));
        L13:
            r1.setTextViewText(C0039R.id.action_text, r7.title);
            if (r0 == true) goto L16;
            r1.setOnClickPendingIntent(C0039R.id.action_container, r7.actionIntent);
        L16:
            r1.setContentDescription(C0039R.id.action_container, r7.title);
            return r1;
        L9:
            r3 = C0039R.layout.notification_action;
            goto L10
        L5:
            r0 = false;
            goto L6
        }

        private static List<Action> getNonContextualActions(List<Action> r4) {
            if (r4 != null) goto L5;
            return null;
        L5:
            ArrayList r0 = new ArrayList();
            Iterator<Action> r1 = r4.iterator();
        L7:
            if (r1.hasNext() == false) goto L12;
            Action r2 = r1.next();
            if (r2.isContextual() == true) goto L7;
            r0.add(r2);
            goto L7
        L12:
            return r0;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r3) {
            if (Build.VERSION.SDK_INT < 24) goto L6;
            r3.getBuilder().setStyle(new Notification.DecoratedCustomViewStyle());
            return;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public boolean displayCustomViewInline() {
            return true;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor r4) {
            if (Build.VERSION.SDK_INT < 24) goto L5;
            return null;
        L5:
            RemoteViews r0 = this.mBuilder.getBigContentView();
            if (r0 == null) goto L8;
            RemoteViews r1 = r0;
        L10:
            if (r1 != null) goto L13;
            return null;
        L13:
            return createRemoteViews(r1, true);
        L8:
            r1 = this.mBuilder.getContentView();
            goto L10
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor r4) {
            if (Build.VERSION.SDK_INT < 24) goto L6;
            return null;
        L6:
            if (this.mBuilder.getContentView() != null) goto L9;
            return null;
        L9:
            return createRemoteViews(this.mBuilder.getContentView(), false);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor r4) {
            if (Build.VERSION.SDK_INT < 24) goto L5;
            return null;
        L5:
            RemoteViews r0 = this.mBuilder.getHeadsUpContentView();
            if (r0 == null) goto L8;
            RemoteViews r1 = r0;
        L9:
            if (r0 != null) goto L12;
            return null;
        L12:
            return createRemoteViews(r1, true);
        L8:
            r1 = this.mBuilder.getContentView();
            goto L9
        }
    }

    public interface Extender {
        Builder extend(Builder r1);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupAlertBehavior {
    }

    public static class InboxStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$InboxStyle";
        private ArrayList<CharSequence> mTexts;

        public InboxStyle() {
            this.mTexts = new ArrayList();
        }

        public InboxStyle(Builder r2) {
            this.mTexts = new ArrayList();
            setBuilder(r2);
        }

        public InboxStyle addLine(CharSequence r3) {
            if (r3 == null) goto L4;
            this.mTexts.add(Builder.limitCharSequenceLength(r3));
        L4:
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r4) {
            Notification.InboxStyle r0 = new Notification.InboxStyle(r4.getBuilder()).setBigContentTitle(this.mBigContentTitle);
            if (this.mSummaryTextSet == false) goto L5;
            r0.setSummaryText(this.mSummaryText);
        L5:
            Iterator<CharSequence> r1 = this.mTexts.iterator();
        L7:
            if (r1.hasNext() == false) goto L9;
            r0.addLine(r1.next());
            goto L7
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle r2) {
            super.clearCompatExtraKeys(r2);
            r2.remove(NotificationCompat.EXTRA_TEXT_LINES);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle r3) {
            super.restoreFromCompatExtras(r3);
            this.mTexts.clear();
            if (r3.containsKey(NotificationCompat.EXTRA_TEXT_LINES) == false) goto L6;
            Collections.addAll(this.mTexts, r3.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES));
            return;
        }

        public InboxStyle setBigContentTitle(CharSequence r2) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(r2);
            return this;
        }

        public InboxStyle setSummaryText(CharSequence r2) {
            this.mSummaryText = Builder.limitCharSequenceLength(r2);
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class MessagingStyle extends Style {
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;
        private static final String TEMPLATE_CLASS_NAME = "androidx.core.app.NotificationCompat$MessagingStyle";
        private CharSequence mConversationTitle;
        private final List<Message> mHistoricMessages;
        private Boolean mIsGroupConversation;
        private final List<Message> mMessages;
        private Person mUser;

        public static final class Message {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_NOTIFICATION_PERSON = "sender_person";
            static final String KEY_PERSON = "person";
            static final String KEY_SENDER = "sender";
            static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final Person mPerson;
            private final CharSequence mText;
            private final long mTimestamp;

            public Message(CharSequence r2, long r3, Person r5) {
                this.mExtras = new Bundle();
                this.mText = r2;
                this.mTimestamp = r3;
                this.mPerson = r5;
            }

            @Deprecated
            public Message(CharSequence r2, long r3, CharSequence r5) {
                this(r2, r3, new Person.Builder().setName(r5).build());
            }

            static Bundle[] getBundleArrayForMessages(List<Message> r4) {
                Bundle[] r0 = new Bundle[r4.size()];
                int r1 = r4.size();
                int r2 = 0;
            L3:
                if (r2 >= r1) goto L5;
                r0[r2] = r4.get(r2).toBundle();
                r2 = r2 + 1;
                goto L3
            L5:
                return r0;
            }

            static Message getMessageFromBundle(Bundle r11) {
                if (r11.containsKey("text") == true) goto L6;
            L28:
                return null;
            L6:
                if (r11.containsKey(KEY_TIMESTAMP) == false) goto L28;
                Person r9 = null;
                if (r11.containsKey(KEY_PERSON) == false) goto L12;
                r9 = Person.fromBundle(r11.getBundle(KEY_PERSON));     // Catch: ClassCastException -> L29
            L19:
                Message r3 = new Message(r11.getCharSequence("text"), r11.getLong(KEY_TIMESTAMP), r9);     // Catch: ClassCastException -> L29
                if (r11.containsKey(KEY_DATA_MIME_TYPE) == false) goto L25;
                if (r11.containsKey(KEY_DATA_URI) == false) goto L25;
                r3.setData(r11.getString(KEY_DATA_MIME_TYPE), (Uri) r11.getParcelable(KEY_DATA_URI));     // Catch: ClassCastException -> L29
            L25:
                if (r11.containsKey(KEY_EXTRAS_BUNDLE) == false) goto L27;
                r3.getExtras().putAll(r11.getBundle(KEY_EXTRAS_BUNDLE));     // Catch: ClassCastException -> L29
            L27:
                return r3;
            L12:
                if (r11.containsKey(KEY_NOTIFICATION_PERSON) == false) goto L17;
                if (Build.VERSION.SDK_INT < 28) goto L17;
                r9 = Person.fromAndroidPerson((android.app.Person) r11.getParcelable(KEY_NOTIFICATION_PERSON));     // Catch: ClassCastException -> L29
            L17:
                if (r11.containsKey(KEY_SENDER) == false) goto L19;
                r9 = new Person.Builder().setName(r11.getCharSequence(KEY_SENDER)).build();     // Catch: ClassCastException -> L29
            L30:
                return null;
            }

            static List<Message> getMessagesFromBundleArray(Parcelable[] r3) {
                ArrayList r0 = new ArrayList(r3.length);
                int r1 = 0;
            L4:
                if (r1 >= r3.length) goto L11;
                if ((r3[r1] instanceof Bundle) == false) goto L10;
                Message r2 = getMessageFromBundle((Bundle) r3[r1]);
                if (r2 == null) goto L10;
                r0.add(r2);
            L10:
                r1 = r1 + 1;
                goto L4
            L11:
                return r0;
            }

            private Bundle toBundle() {
                Bundle r0 = new Bundle();
                if (this.mText == null) goto L5;
                r0.putCharSequence("text", this.mText);
            L5:
                r0.putLong(KEY_TIMESTAMP, this.mTimestamp);
                if (this.mPerson == null) goto L12;
                r0.putCharSequence(KEY_SENDER, this.mPerson.getName());
                if (Build.VERSION.SDK_INT < 28) goto L10;
                r0.putParcelable(KEY_NOTIFICATION_PERSON, this.mPerson.toAndroidPerson());
                goto L12
            L10:
                r0.putBundle(KEY_PERSON, this.mPerson.toBundle());
            L12:
                if (this.mDataMimeType == null) goto L15;
                r0.putString(KEY_DATA_MIME_TYPE, this.mDataMimeType);
            L15:
                if (this.mDataUri == null) goto L18;
                r0.putParcelable(KEY_DATA_URI, this.mDataUri);
            L18:
                if (this.mExtras == null) goto L20;
                r0.putBundle(KEY_EXTRAS_BUNDLE, this.mExtras);
            L20:
                return r0;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Person getPerson() {
                return this.mPerson;
            }

            @Deprecated
            public CharSequence getSender() {
                if (this.mPerson != null) goto L6;
                return null;
            L6:
                return this.mPerson.getName();
            }

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public Message setData(String r1, Uri r2) {
                this.mDataMimeType = r1;
                this.mDataUri = r2;
                return this;
            }

            Notification.MessagingStyle.Message toAndroidMessage() {
                Person r0 = getPerson();
                CharSequence r3 = null;
                android.app.Person r4 = null;
                if (Build.VERSION.SDK_INT < 28) goto L9;
                CharSequence r2 = getText();
                long r5 = getTimestamp();
                if (r0 == null) goto L8;
                r4 = r0.toAndroidPerson();
            L8:
                Notification.MessagingStyle.Message r1 = new Notification.MessagingStyle.Message(r2, r5, r4);
            L15:
                if (getDataMimeType() == null) goto L17;
                r1.setData(getDataMimeType(), getDataUri());
            L17:
                return r1;
            L9:
                CharSequence r6 = getText();
                long r7 = getTimestamp();
                if (r0 == null) goto L13;
                r3 = r0.getName();
            L13:
                r1 = new Notification.MessagingStyle.Message(r6, r7, r3);
                goto L15
            }
        }

        MessagingStyle() {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
        }

        public MessagingStyle(Person r3) {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
            if (TextUtils.isEmpty(r3.getName()) == true) goto L7;
            this.mUser = r3;
            return;
        L7:
            throw new IllegalArgumentException("User's name must not be empty.");
        }

        @Deprecated
        public MessagingStyle(CharSequence r2) {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
            this.mUser = new Person.Builder().setName(r2).build();
        }

        public static MessagingStyle extractMessagingStyleFromNotification(Notification r2) {
            Style r0 = Style.extractStyleFromNotification(r2);
            if ((r0 instanceof MessagingStyle) == true) goto L5;
            return null;
        L5:
            return (MessagingStyle) r0;
        }

        private Message findLatestIncomingMessage() {
            int r0 = this.mMessages.size() - 1;
        L3:
            if (r0 < 0) goto L11;
            Message r1 = this.mMessages.get(r0);
            if (r1.getPerson() == null) goto L9;
            if (TextUtils.isEmpty(r1.getPerson().getName()) == true) goto L9;
            return r1;
        L9:
            r0 = r0 - 1;
            goto L3
        L11:
            if (this.mMessages.isEmpty() == false) goto L13;
            return null;
        L13:
            return this.mMessages.get(this.mMessages.size() - 1);
        }

        private boolean hasMessagesWithoutSender() {
            int r0 = this.mMessages.size() - 1;
        L3:
            if (r0 < 0) goto L10;
            Message r2 = this.mMessages.get(r0);
            if (r2.getPerson() == null) goto L9;
            if (r2.getPerson().getName() != null) goto L9;
            return true;
        L9:
            r0 = r0 - 1;
            goto L3
        L10:
            return false;
        }

        private TextAppearanceSpan makeFontColorSpan(int r7) {
            return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(r7), null);
        }

        private CharSequence makeMessageLine(Message r12) {
            BidiFormatter r0 = BidiFormatter.getInstance();
            SpannableStringBuilder r1 = new SpannableStringBuilder();
            if (1 == 0) goto L5;
            int r3 = ViewCompat.MEASURED_STATE_MASK;
        L6:
            CharSequence r5 = "";
            if (r12.getPerson() != null) goto L9;
            CharSequence r4 = "";
        L11:
            if (TextUtils.isEmpty(r4) == false) goto L19;
            r4 = this.mUser.getName();
            if (1 != 0) goto L15;
        L17:
            int r6 = r3;
        L18:
            r3 = r6;
            goto L19
        L15:
            if (this.mBuilder.getColor() == 0) goto L17;
            r6 = this.mBuilder.getColor();
        L19:
            CharSequence r7 = r0.unicodeWrap(r4);
            r1.append(r7);
            r1.setSpan(makeFontColorSpan(r3), r1.length() - r7.length(), r1.length(), 33);
            if (r12.getText() == null) goto L23;
            r5 = r12.getText();
        L23:
            r1.append("  ").append(r0.unicodeWrap(r5));
            return r1;
        L9:
            r4 = r12.getPerson().getName();
            goto L11
        L5:
            r3 = -1;
            goto L6
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void addCompatExtras(Bundle r3) {
            super.addCompatExtras(r3);
            r3.putCharSequence(NotificationCompat.EXTRA_SELF_DISPLAY_NAME, this.mUser.getName());
            r3.putBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER, this.mUser.toBundle());
            r3.putCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE, this.mConversationTitle);
            if (this.mConversationTitle == null) goto L8;
            if (this.mIsGroupConversation.booleanValue() == false) goto L8;
            r3.putCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE, this.mConversationTitle);
        L8:
            if (this.mMessages.isEmpty() == true) goto L11;
            r3.putParcelableArray(NotificationCompat.EXTRA_MESSAGES, Message.getBundleArrayForMessages(this.mMessages));
        L11:
            if (this.mHistoricMessages.isEmpty() == true) goto L14;
            r3.putParcelableArray(NotificationCompat.EXTRA_HISTORIC_MESSAGES, Message.getBundleArrayForMessages(this.mHistoricMessages));
        L14:
            if (this.mIsGroupConversation == null) goto L17;
            r3.putBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION, this.mIsGroupConversation.booleanValue());
            return;
        }

        public MessagingStyle addHistoricMessage(Message r3) {
            if (r3 == null) goto L6;
            this.mHistoricMessages.add(r3);
            if (this.mHistoricMessages.size() <= 25) goto L6;
            this.mHistoricMessages.remove(0);
        L6:
            return this;
        }

        public MessagingStyle addMessage(Message r3) {
            if (r3 == null) goto L6;
            this.mMessages.add(r3);
            if (this.mMessages.size() <= 25) goto L6;
            this.mMessages.remove(0);
        L6:
            return this;
        }

        public MessagingStyle addMessage(CharSequence r2, long r3, Person r5) {
            addMessage(new Message(r2, r3, r5));
            return this;
        }

        @Deprecated
        public MessagingStyle addMessage(CharSequence r4, long r5, CharSequence r7) {
            this.mMessages.add(new Message(r4, r5, new Person.Builder().setName(r7).build()));
            if (this.mMessages.size() <= 25) goto L5;
            this.mMessages.remove(0);
        L5:
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r10) {
            setGroupConversation(isGroupConversation());
            if (Build.VERSION.SDK_INT >= 24) goto L5;
            Message r0 = findLatestIncomingMessage();
            if (this.mConversationTitle != null) goto L30;
        L32:
            if (r0 == null) goto L36;
            r10.getBuilder().setContentTitle("");
            if (r0.getPerson() == null) goto L36;
            r10.getBuilder().setContentTitle(r0.getPerson().getName());
        L36:
            if (r0 == null) goto L42;
            Notification.Builder r1 = r10.getBuilder();
            if (this.mConversationTitle == null) goto L40;
            CharSequence r2 = makeMessageLine(r0);
        L41:
            r1.setContentText(r2);
            goto L42
        L40:
            r2 = r0.getText();
        L42:
            SpannableStringBuilder r3 = new SpannableStringBuilder();
            if (this.mConversationTitle == null) goto L45;
        L48:
            boolean r4 = true;
        L49:
            int r5 = this.mMessages.size() - 1;
        L50:
            if (r5 < 0) goto L59;
            Message r6 = this.mMessages.get(r5);
            if (r4 == false) goto L54;
            CharSequence r7 = makeMessageLine(r6);
        L56:
            if (r5 == (this.mMessages.size() - 1)) goto L58;
            r3.insert(0, "\n");
        L58:
            r3.insert(0, r7);
            r5 = r5 - 1;
            goto L50
        L54:
            r7 = r6.getText();
            goto L56
        L59:
            new Notification.BigTextStyle(r10.getBuilder()).setBigContentTitle(null).bigText(r3);
            return;
        L45:
            if (hasMessagesWithoutSender() == true) goto L48;
            r4 = false;
            goto L49
        L30:
            if (this.mIsGroupConversation.booleanValue() == false) goto L32;
            r10.getBuilder().setContentTitle(this.mConversationTitle);
            goto L36
        L5:
            if (Build.VERSION.SDK_INT < 28) goto L7;
            Notification.MessagingStyle r8 = new Notification.MessagingStyle(this.mUser.toAndroidPerson());
        L8:
            Iterator<Message> r9 = this.mMessages.iterator();
        L10:
            if (r9.hasNext() == false) goto L13;
            r8.addMessage(r9.next().toAndroidMessage());
            goto L10
        L13:
            if (Build.VERSION.SDK_INT < 26) goto L19;
            Iterator<Message> r11 = this.mHistoricMessages.iterator();
        L16:
            if (r11.hasNext() == false) goto L19;
            r8.addHistoricMessage(r11.next().toAndroidMessage());
        L19:
            if (this.mIsGroupConversation.booleanValue() == false) goto L21;
        L22:
            r8.setConversationTitle(this.mConversationTitle);
        L24:
            if (Build.VERSION.SDK_INT < 28) goto L26;
            r8.setGroupConversation(this.mIsGroupConversation.booleanValue());
        L26:
            r8.setBuilder(r10.getBuilder());
            return;
        L21:
            if (Build.VERSION.SDK_INT < 28) goto L24;
        L7:
            r8 = new Notification.MessagingStyle(this.mUser.getName());
            goto L8
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle r2) {
            super.clearCompatExtraKeys(r2);
            r2.remove(NotificationCompat.EXTRA_MESSAGING_STYLE_USER);
            r2.remove(NotificationCompat.EXTRA_SELF_DISPLAY_NAME);
            r2.remove(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            r2.remove(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            r2.remove(NotificationCompat.EXTRA_MESSAGES);
            r2.remove(NotificationCompat.EXTRA_HISTORIC_MESSAGES);
            r2.remove(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }

        public List<Message> getHistoricMessages() {
            return this.mHistoricMessages;
        }

        public List<Message> getMessages() {
            return this.mMessages;
        }

        public Person getUser() {
            return this.mUser;
        }

        @Deprecated
        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }

        public boolean isGroupConversation() {
            if (this.mBuilder == null) goto L13;
            if (this.mBuilder.mContext.getApplicationInfo().targetSdkVersion >= 28) goto L13;
            if (this.mIsGroupConversation != null) goto L13;
            if (this.mConversationTitle == null) goto L16;
            return true;
        L16:
            return false;
        L13:
            if (this.mIsGroupConversation != null) goto L15;
            return false;
        L15:
            return this.mIsGroupConversation.booleanValue();
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle r5) {
            super.restoreFromCompatExtras(r5);
            this.mMessages.clear();
            if (r5.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER) == false) goto L5;
            this.mUser = Person.fromBundle(r5.getBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER));
        L6:
            this.mConversationTitle = r5.getCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            if (this.mConversationTitle != null) goto L9;
            this.mConversationTitle = r5.getCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
        L9:
            Parcelable[] r0 = r5.getParcelableArray(NotificationCompat.EXTRA_MESSAGES);
            if (r0 == null) goto L12;
            this.mMessages.addAll(Message.getMessagesFromBundleArray(r0));
        L12:
            Parcelable[] r1 = r5.getParcelableArray(NotificationCompat.EXTRA_HISTORIC_MESSAGES);
            if (r1 == null) goto L16;
            this.mHistoricMessages.addAll(Message.getMessagesFromBundleArray(r1));
        L16:
            if (r5.containsKey(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION) == false) goto L19;
            this.mIsGroupConversation = Boolean.valueOf(r5.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION));
            return;
        L19:
            return;
        L5:
            this.mUser = new Person.Builder().setName(r5.getString(NotificationCompat.EXTRA_SELF_DISPLAY_NAME)).build();
            goto L6
        }

        public MessagingStyle setConversationTitle(CharSequence r1) {
            this.mConversationTitle = r1;
            return this;
        }

        public MessagingStyle setGroupConversation(boolean r2) {
            this.mIsGroupConversation = Boolean.valueOf(r2);
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationVisibility {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceNotificationBehavior {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamType {
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        protected Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;

        public Style() {
            this.mSummaryTextSet = false;
        }

        private int calculateTopPadding() {
            Resources r0 = this.mBuilder.mContext.getResources();
            int r1 = r0.getDimensionPixelSize(C0039R.dimen.notification_top_pad);
            int r2 = r0.getDimensionPixelSize(C0039R.dimen.notification_top_pad_large_text);
            float r4 = (constrain(r0.getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
            return Math.round(((1.0f - r4) * r1) + (r2 * r4));
        }

        private static float constrain(float r1, float r2, float r3) {
            if (r1 >= r2) goto L6;
            return r2;
        L6:
            if (r1 <= r3) goto L9;
            return r3;
        L9:
            return r1;
        }

        static Style constructCompatStyleByName(String r1) {
            if (r1 != null) goto L4;
            return null;
        L4:
            switch(r1.hashCode()) {
                case -716705180: goto L19;
                case -171946061: goto L16;
                case 912942987: goto L13;
                case 919595044: goto L10;
                case 2090799565: goto L7;
                default: goto L21;
            };
        L21:
            byte r0 = -1;
        L22:
            switch(r0) {
                case 0: goto L33;
                case 1: goto L31;
                case 2: goto L29;
                case 3: goto L27;
                case 4: goto L25;
                default: goto L36;
            };
        L36:
            return null;
        L25:
            return new MessagingStyle();
        L27:
            return new DecoratedCustomViewStyle();
        L29:
            return new InboxStyle();
        L31:
            return new BigPictureStyle();
        L33:
            return new BigTextStyle();
        L7:
            if (r1.equals("androidx.core.app.NotificationCompat$MessagingStyle") == false) goto L21;
            r0 = 4;
            goto L22
        L10:
            if (r1.equals("androidx.core.app.NotificationCompat$BigTextStyle") == false) goto L21;
            r0 = 0;
            goto L22
        L13:
            if (r1.equals("androidx.core.app.NotificationCompat$InboxStyle") == false) goto L21;
            r0 = 2;
            goto L22
        L16:
            if (r1.equals("androidx.core.app.NotificationCompat$BigPictureStyle") == false) goto L21;
            r0 = 1;
            goto L22
        L19:
            if (r1.equals("androidx.core.app.NotificationCompat$DecoratedCustomViewStyle") == false) goto L21;
            r0 = 3;
            goto L22
        }

        private static Style constructCompatStyleByPlatformName(String r3) {
            if (r3 != null) goto L6;
            return null;
        L6:
            if (r3.equals(Notification.BigPictureStyle.class.getName()) == false) goto L10;
            return new BigPictureStyle();
        L10:
            if (r3.equals(Notification.BigTextStyle.class.getName()) == false) goto L14;
            return new BigTextStyle();
        L14:
            if (r3.equals(Notification.InboxStyle.class.getName()) == false) goto L18;
            return new InboxStyle();
        L18:
            if (Build.VERSION.SDK_INT >= 24) goto L20;
        L27:
            return null;
        L20:
            if (r3.equals(Notification.MessagingStyle.class.getName()) == false) goto L24;
            return new MessagingStyle();
        L24:
            if (r3.equals(Notification.DecoratedCustomViewStyle.class.getName()) == false) goto L27;
            return new DecoratedCustomViewStyle();
        }

        static Style constructCompatStyleForBundle(Bundle r2) {
            Style r0 = constructCompatStyleByName(r2.getString(NotificationCompat.EXTRA_COMPAT_TEMPLATE));
            if (r0 == null) goto L6;
            return r0;
        L6:
            if (r2.containsKey(NotificationCompat.EXTRA_SELF_DISPLAY_NAME) == true) goto L28;
            if (r2.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER) == true) goto L28;
            if (r2.containsKey(NotificationCompat.EXTRA_PICTURE) == true) goto L26;
            if (r2.containsKey(NotificationCompat.EXTRA_PICTURE_ICON) == true) goto L26;
            if (r2.containsKey(NotificationCompat.EXTRA_BIG_TEXT) == false) goto L20;
            return new BigTextStyle();
        L20:
            if (r2.containsKey(NotificationCompat.EXTRA_TEXT_LINES) == false) goto L24;
            return new InboxStyle();
        L24:
            return constructCompatStyleByPlatformName(r2.getString(NotificationCompat.EXTRA_TEMPLATE));
        L26:
            return new BigPictureStyle();
        L28:
            return new MessagingStyle();
        }

        static Style constructStyleForExtras(Bundle r3) {
            Style r0 = constructCompatStyleForBundle(r3);
            if (r0 != null) goto L9;
            return null;
        L9:
            r0.restoreFromCompatExtras(r3);     // Catch: ClassCastException -> L7
            return r0;
        L8:
            return null;
        }

        private Bitmap createColoredBitmap(int r2, int r3, int r4) {
            return createColoredBitmap(IconCompat.createWithResource(this.mBuilder.mContext, r2), r3, r4);
        }

        private Bitmap createColoredBitmap(IconCompat r8, int r9, int r10) {
            Drawable r0 = r8.loadDrawable(this.mBuilder.mContext);
            if (r10 != 0) goto L5;
            int r1 = r0.getIntrinsicWidth();
        L6:
            if (r10 != 0) goto L8;
            int r2 = r0.getIntrinsicHeight();
        L9:
            Bitmap r3 = Bitmap.createBitmap(r1, r2, Bitmap.Config.ARGB_8888);
            r0.setBounds(0, 0, r1, r2);
            if (r9 == 0) goto L12;
            r0.mutate().setColorFilter(new PorterDuffColorFilter(r9, PorterDuff.Mode.SRC_IN));
        L12:
            r0.draw(new Canvas(r3));
            return r3;
        L8:
            r2 = r10;
            goto L9
        L5:
            r1 = r10;
            goto L6
        }

        private Bitmap createIconWithBackground(int r8, int r9, int r10, int r11) {
            int r0 = C0039R.drawable.notification_icon_background;
            if (r11 != 0) goto L5;
            int r1 = 0;
        L6:
            Bitmap r2 = createColoredBitmap(r0, r1, r9);
            Canvas r3 = new Canvas(r2);
            Drawable r4 = this.mBuilder.mContext.getResources().getDrawable(r8).mutate();
            r4.setFilterBitmap(true);
            int r5 = (r9 - r10) / 2;
            r4.setBounds(r5, r5, r10 + r5, r10 + r5);
            r4.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
            r4.draw(r3);
            return r2;
        L5:
            r1 = r11;
            goto L6
        }

        public static Style extractStyleFromNotification(Notification r2) {
            Bundle r0 = NotificationCompat.getExtras(r2);
            if (r0 != null) goto L7;
            return null;
        L7:
            return constructStyleForExtras(r0);
        }

        private void hideNormalContent(RemoteViews r3) {
            r3.setViewVisibility(C0039R.id.title, 8);
            r3.setViewVisibility(C0039R.id.text2, 8);
            r3.setViewVisibility(C0039R.id.text, 8);
        }

        public void addCompatExtras(Bundle r3) {
            if (this.mSummaryTextSet == false) goto L6;
            r3.putCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT, this.mSummaryText);
        L6:
            if (this.mBigContentTitle == null) goto L8;
            r3.putCharSequence(NotificationCompat.EXTRA_TITLE_BIG, this.mBigContentTitle);
        L8:
            String r0 = getClassName();
            if (r0 == null) goto L12;
            r3.putString(NotificationCompat.EXTRA_COMPAT_TEMPLATE, r0);
            return;
        }

        public void apply(NotificationBuilderWithBuilderAccessor r1) {
        }

        public RemoteViews applyStandardTemplate(boolean r19, int r20, boolean r21) {
            Resources r1 = this.mBuilder.mContext.getResources();
            RemoteViews r2 = new RemoteViews(this.mBuilder.mContext.getPackageName(), r20);
            boolean r3 = false;
            boolean r4 = false;
            int r12 = 0;
            if (this.mBuilder.getPriority() >= (-1)) goto L5;
            boolean r6 = true;
        L7:
            if (this.mBuilder.mLargeIcon == null) goto L13;
            r2.setViewVisibility(C0039R.id.icon, 0);
            r2.setImageViewBitmap(C0039R.id.icon, this.mBuilder.mLargeIcon);
            if (r19 == false) goto L18;
            if (this.mBuilder.mNotification.icon == 0) goto L18;
            int r7 = r1.getDimensionPixelSize(C0039R.dimen.notification_right_icon_size);
            r2.setImageViewBitmap(C0039R.id.right_icon, createIconWithBackground(this.mBuilder.mNotification.icon, r7, r7 - (r1.getDimensionPixelSize(C0039R.dimen.notification_small_icon_background_padding) * 2), this.mBuilder.getColor()));
            r2.setViewVisibility(C0039R.id.right_icon, 0);
        L18:
            if (this.mBuilder.mContentTitle == null) goto L21;
            r2.setTextViewText(C0039R.id.title, this.mBuilder.mContentTitle);
        L21:
            if (this.mBuilder.mContentText == null) goto L24;
            r2.setTextViewText(C0039R.id.text, this.mBuilder.mContentText);
            r3 = true;
        L24:
            if (this.mBuilder.mContentInfo == null) goto L27;
            r2.setTextViewText(C0039R.id.info, this.mBuilder.mContentInfo);
            r2.setViewVisibility(C0039R.id.info, 0);
            r3 = true;
            boolean r14 = true;
        L35:
            if (this.mBuilder.mSubText == null) goto L40;
            r2.setTextViewText(C0039R.id.text, this.mBuilder.mSubText);
            if (this.mBuilder.mContentText == null) goto L39;
            r2.setTextViewText(C0039R.id.text2, this.mBuilder.mContentText);
            r2.setViewVisibility(C0039R.id.text2, 0);
            r4 = true;
            int r15 = 8;
        L41:
            if (r4 == false) goto L46;
            if (r21 == false) goto L44;
            r2.setTextViewTextSize(C0039R.id.text, 0, r1.getDimensionPixelSize(C0039R.dimen.notification_subtext_size));
        L44:
            r2.setViewPadding(C0039R.id.line1, 0, 0, 0, 0);
        L46:
            if (this.mBuilder.getWhenIfShowing() != 0) goto L48;
        L56:
            int r8 = C0039R.id.right_side;
            if (r14 == false) goto L59;
            int r9 = 0;
        L60:
            r2.setViewVisibility(r8, r9);
            int r10 = C0039R.id.line3;
            if (r3 == true) goto L64;
            r12 = r15;
        L64:
            r2.setViewVisibility(r10, r12);
            return r2;
        L59:
            r9 = r15;
            goto L60
        L48:
            if (this.mBuilder.mUseChronometer == false) goto L54;
            r2.setViewVisibility(C0039R.id.chronometer, 0);
            r2.setLong(C0039R.id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
            r2.setBoolean(C0039R.id.chronometer, "setStarted", true);
            if (this.mBuilder.mChronometerCountDown == true) goto L52;
        L55:
            r14 = true;
            goto L56
        L52:
            if (Build.VERSION.SDK_INT < 24) goto L55;
            r2.setChronometerCountDown(C0039R.id.chronometer, this.mBuilder.mChronometerCountDown);
            goto L55
        L54:
            r2.setViewVisibility(C0039R.id.time, 0);
            r2.setLong(C0039R.id.time, "setTime", this.mBuilder.getWhenIfShowing());
            goto L55
        L39:
            r15 = 8;
            r2.setViewVisibility(C0039R.id.text2, 8);
            goto L41
        L40:
            r15 = 8;
            goto L41
        L27:
            if (this.mBuilder.mNumber > 0) goto L29;
            r2.setViewVisibility(C0039R.id.info, 8);
            r14 = false;
            goto L35
        L29:
            if (this.mBuilder.mNumber <= r1.getInteger(C0039R.integer.status_bar_notification_info_maxnum)) goto L31;
            r2.setTextViewText(C0039R.id.info, r1.getString(C0039R.string.status_bar_notification_info_overflow));
        L32:
            r2.setViewVisibility(C0039R.id.info, 0);
            r3 = true;
            r14 = true;
            goto L35
        L31:
            r2.setTextViewText(C0039R.id.info, NumberFormat.getIntegerInstance().format(this.mBuilder.mNumber));
            goto L32
        L13:
            if (r19 == false) goto L18;
            if (this.mBuilder.mNotification.icon == 0) goto L18;
            r2.setViewVisibility(C0039R.id.icon, 0);
            r2.setImageViewBitmap(C0039R.id.icon, createIconWithBackground(this.mBuilder.mNotification.icon, r1.getDimensionPixelSize(C0039R.dimen.notification_large_icon_width) - r1.getDimensionPixelSize(C0039R.dimen.notification_big_circle_margin), r1.getDimensionPixelSize(C0039R.dimen.notification_small_icon_size_as_large), this.mBuilder.getColor()));
            goto L18
        L5:
            r6 = false;
            goto L7
        }

        public Notification build() {
            if (this.mBuilder != null) goto L5;
            return null;
        L5:
            return this.mBuilder.build();
        }

        public void buildIntoRemoteViews(RemoteViews r9, RemoteViews r10) {
            hideNormalContent(r9);
            r9.removeAllViews(C0039R.id.notification_main_column);
            r9.addView(C0039R.id.notification_main_column, r10.clone());
            r9.setViewVisibility(C0039R.id.notification_main_column, 0);
            r9.setViewPadding(C0039R.id.notification_main_column_container, 0, calculateTopPadding(), 0, 0);
        }

        protected void clearCompatExtraKeys(Bundle r2) {
            r2.remove(NotificationCompat.EXTRA_SUMMARY_TEXT);
            r2.remove(NotificationCompat.EXTRA_TITLE_BIG);
            r2.remove(NotificationCompat.EXTRA_COMPAT_TEMPLATE);
        }

        public Bitmap createColoredBitmap(int r2, int r3) {
            return createColoredBitmap(r2, r3, 0);
        }

        Bitmap createColoredBitmap(IconCompat r2, int r3) {
            return createColoredBitmap(r2, r3, 0);
        }

        public boolean displayCustomViewInline() {
            return false;
        }

        protected String getClassName() {
            return null;
        }

        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor r2) {
            return null;
        }

        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor r2) {
            return null;
        }

        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor r2) {
            return null;
        }

        protected void restoreFromCompatExtras(Bundle r3) {
            if (r3.containsKey(NotificationCompat.EXTRA_SUMMARY_TEXT) == false) goto L5;
            this.mSummaryText = r3.getCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT);
            this.mSummaryTextSet = true;
        L5:
            this.mBigContentTitle = r3.getCharSequence(NotificationCompat.EXTRA_TITLE_BIG);
        }

        public void setBuilder(Builder r2) {
            if (this.mBuilder == r2) goto L8;
            this.mBuilder = r2;
            if (this.mBuilder == null) goto L9;
            this.mBuilder.setStyle(this);
            return;
        L9:
            return;
        }
    }

    public static final class WearableExtender implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";

        @Deprecated
        public static final int SCREEN_TIMEOUT_LONG = -1;

        @Deprecated
        public static final int SCREEN_TIMEOUT_SHORT = 0;

        @Deprecated
        public static final int SIZE_DEFAULT = 0;

        @Deprecated
        public static final int SIZE_FULL_SCREEN = 5;

        @Deprecated
        public static final int SIZE_LARGE = 4;

        @Deprecated
        public static final int SIZE_MEDIUM = 3;

        @Deprecated
        public static final int SIZE_SMALL = 2;

        @Deprecated
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions;
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex;
        private int mContentIcon;
        private int mContentIconGravity;
        private int mCustomContentHeight;
        private int mCustomSizePreset;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags;
        private int mGravity;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages;

        public WearableExtender() {
            this.mActions = new ArrayList();
            this.mFlags = 1;
            this.mPages = new ArrayList();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = DEFAULT_GRAVITY;
        }

        public WearableExtender(Notification r12) {
            this.mActions = new ArrayList();
            this.mFlags = 1;
            this.mPages = new ArrayList();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = DEFAULT_GRAVITY;
            Bundle r5 = NotificationCompat.getExtras(r12);
            if (r5 == null) goto L5;
            Bundle r6 = r5.getBundle(EXTRA_WEARABLE_EXTENSIONS);
        L7:
            if (r6 == null) goto L21;
            ArrayList r7 = r6.getParcelableArrayList(KEY_ACTIONS);
            if (r7 == null) goto L15;
            Action[] r8 = new Action[r7.size()];
            int r9 = 0;
        L12:
            if (r9 >= r8.length) goto L14;
            r8[r9] = NotificationCompat.getActionCompatFromAction((Notification.Action) r7.get(r9));
            r9 = r9 + 1;
            goto L12
        L14:
            Collections.addAll(this.mActions, r8);
        L15:
            this.mFlags = r6.getInt(KEY_FLAGS, 1);
            this.mDisplayIntent = (PendingIntent) r6.getParcelable(KEY_DISPLAY_INTENT);
            Notification[] r0 = NotificationCompat.getNotificationArrayFromBundle(r6, KEY_PAGES);
            if (r0 == null) goto L18;
            Collections.addAll(this.mPages, r0);
        L18:
            this.mBackground = (Bitmap) r6.getParcelable(KEY_BACKGROUND);
            this.mContentIcon = r6.getInt(KEY_CONTENT_ICON);
            this.mContentIconGravity = r6.getInt(KEY_CONTENT_ICON_GRAVITY, 8388613);
            this.mContentActionIndex = r6.getInt(KEY_CONTENT_ACTION_INDEX, -1);
            this.mCustomSizePreset = r6.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
            this.mCustomContentHeight = r6.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
            this.mGravity = r6.getInt(KEY_GRAVITY, DEFAULT_GRAVITY);
            this.mHintScreenTimeout = r6.getInt(KEY_HINT_SCREEN_TIMEOUT);
            this.mDismissalId = r6.getString(KEY_DISMISSAL_ID);
            this.mBridgeTag = r6.getString(KEY_BRIDGE_TAG);
            return;
        L21:
            return;
        L5:
            r6 = null;
            goto L7
        }

        private static Notification.Action getActionFromActionCompat(Action r7) {
            IconCompat r0 = r7.getIconCompat();
            if (r0 != null) goto L5;
            Icon r2 = null;
        L6:
            Notification.Action.Builder r1 = new Notification.Action.Builder(r2, r7.getTitle(), r7.getActionIntent());
            if (r7.getExtras() == null) goto L9;
            Bundle r3 = new Bundle(r7.getExtras());
        L10:
            r3.putBoolean("android.support.allowGeneratedReplies", r7.getAllowGeneratedReplies());
            if (Build.VERSION.SDK_INT < 24) goto L14;
            r1.setAllowGeneratedReplies(r7.getAllowGeneratedReplies());
        L14:
            if (Build.VERSION.SDK_INT < 31) goto L16;
            r1.setAuthenticationRequired(r7.isAuthenticationRequired());
        L16:
            r1.addExtras(r3);
            RemoteInput[] r4 = r7.getRemoteInputs();
            if (r4 == null) goto L22;
            android.app.RemoteInput[] r5 = RemoteInput.fromCompat(r4);
            int r6 = r5.length;
            int r8 = 0;
        L19:
            if (r8 >= r6) goto L22;
            r1.addRemoteInput(r5[r8]);
            r8 = r8 + 1;
        L22:
            return r1.build();
        L9:
            r3 = new Bundle();
            goto L10
        L5:
            r2 = r0.toIcon();
            goto L6
        }

        private void setFlag(int r3, boolean r4) {
            if (r4 == false) goto L4;
            this.mFlags |= r3;
            return;
        L4:
            this.mFlags &= ~r3;
        }

        public WearableExtender addAction(Action r2) {
            this.mActions.add(r2);
            return this;
        }

        public WearableExtender addActions(List<Action> r2) {
            this.mActions.addAll(r2);
            return this;
        }

        @Deprecated
        public WearableExtender addPage(Notification r2) {
            this.mPages.add(r2);
            return this;
        }

        @Deprecated
        public WearableExtender addPages(List<Notification> r2) {
            this.mPages.addAll(r2);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        @Deprecated
        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        public WearableExtender clone() {
            WearableExtender r0 = new WearableExtender();
            r0.mActions = new ArrayList(this.mActions);
            r0.mFlags = this.mFlags;
            r0.mDisplayIntent = this.mDisplayIntent;
            r0.mPages = new ArrayList(this.mPages);
            r0.mBackground = this.mBackground;
            r0.mContentIcon = this.mContentIcon;
            r0.mContentIconGravity = this.mContentIconGravity;
            r0.mContentActionIndex = this.mContentActionIndex;
            r0.mCustomSizePreset = this.mCustomSizePreset;
            r0.mCustomContentHeight = this.mCustomContentHeight;
            r0.mGravity = this.mGravity;
            r0.mHintScreenTimeout = this.mHintScreenTimeout;
            r0.mDismissalId = this.mDismissalId;
            r0.mBridgeTag = this.mBridgeTag;
            return r0;
        }

        /* JADX INFO: renamed from: clone, reason: collision with other method in class */
        public /* bridge */ /* synthetic */ Object m453clone() throws CloneNotSupportedException {
            return clone();
        }

        @Override // androidx.core.app.NotificationCompat.Extender
        public Builder extend(Builder r6) {
            Bundle r0 = new Bundle();
            if (this.mActions.isEmpty() == true) goto L10;
            ArrayList<? extends Parcelable> r1 = new ArrayList(this.mActions.size());
            Iterator<Action> r2 = this.mActions.iterator();
        L6:
            if (r2.hasNext() == false) goto L8;
            r1.add(getActionFromActionCompat(r2.next()));
            goto L6
        L8:
            r0.putParcelableArrayList(KEY_ACTIONS, r1);
        L10:
            if (this.mFlags == 1) goto L13;
            r0.putInt(KEY_FLAGS, this.mFlags);
        L13:
            if (this.mDisplayIntent == null) goto L16;
            r0.putParcelable(KEY_DISPLAY_INTENT, this.mDisplayIntent);
        L16:
            if (this.mPages.isEmpty() == true) goto L19;
            r0.putParcelableArray(KEY_PAGES, (Parcelable[]) this.mPages.toArray(new Notification[this.mPages.size()]));
        L19:
            if (this.mBackground == null) goto L22;
            r0.putParcelable(KEY_BACKGROUND, this.mBackground);
        L22:
            if (this.mContentIcon == 0) goto L25;
            r0.putInt(KEY_CONTENT_ICON, this.mContentIcon);
        L25:
            if (this.mContentIconGravity == 8388613) goto L28;
            r0.putInt(KEY_CONTENT_ICON_GRAVITY, this.mContentIconGravity);
        L28:
            if (this.mContentActionIndex == (-1)) goto L31;
            r0.putInt(KEY_CONTENT_ACTION_INDEX, this.mContentActionIndex);
        L31:
            if (this.mCustomSizePreset == 0) goto L34;
            r0.putInt(KEY_CUSTOM_SIZE_PRESET, this.mCustomSizePreset);
        L34:
            if (this.mCustomContentHeight == 0) goto L37;
            r0.putInt(KEY_CUSTOM_CONTENT_HEIGHT, this.mCustomContentHeight);
        L37:
            if (this.mGravity == DEFAULT_GRAVITY) goto L40;
            r0.putInt(KEY_GRAVITY, this.mGravity);
        L40:
            if (this.mHintScreenTimeout == 0) goto L43;
            r0.putInt(KEY_HINT_SCREEN_TIMEOUT, this.mHintScreenTimeout);
        L43:
            if (this.mDismissalId == null) goto L46;
            r0.putString(KEY_DISMISSAL_ID, this.mDismissalId);
        L46:
            if (this.mBridgeTag == null) goto L48;
            r0.putString(KEY_BRIDGE_TAG, this.mBridgeTag);
        L48:
            r6.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, r0);
            return r6;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        @Deprecated
        public Bitmap getBackground() {
            return this.mBackground;
        }

        public String getBridgeTag() {
            return this.mBridgeTag;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }

        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public boolean getContentIntentAvailableOffline() {
            if ((this.mFlags & 1) != 0) goto L7;
            return false;
        L7:
            return true;
        }

        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        public String getDismissalId() {
            return this.mDismissalId;
        }

        @Deprecated
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }

        @Deprecated
        public boolean getHintAmbientBigPicture() {
            if ((this.mFlags & 32) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            if ((this.mFlags & 16) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        public boolean getHintContentIntentLaunchesActivity() {
            if ((this.mFlags & 64) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Deprecated
        public boolean getHintHideIcon() {
            if ((this.mFlags & 2) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            if ((this.mFlags & 4) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Deprecated
        public List<Notification> getPages() {
            return this.mPages;
        }

        public boolean getStartScrollBottom() {
            if ((this.mFlags & 8) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        @Deprecated
        public WearableExtender setBackground(Bitmap r1) {
            this.mBackground = r1;
            return this;
        }

        public WearableExtender setBridgeTag(String r1) {
            this.mBridgeTag = r1;
            return this;
        }

        public WearableExtender setContentAction(int r1) {
            this.mContentActionIndex = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setContentIcon(int r1) {
            this.mContentIcon = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setContentIconGravity(int r1) {
            this.mContentIconGravity = r1;
            return this;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean r2) {
            setFlag(1, r2);
            return this;
        }

        @Deprecated
        public WearableExtender setCustomContentHeight(int r1) {
            this.mCustomContentHeight = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setCustomSizePreset(int r1) {
            this.mCustomSizePreset = r1;
            return this;
        }

        public WearableExtender setDismissalId(String r1) {
            this.mDismissalId = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setDisplayIntent(PendingIntent r1) {
            this.mDisplayIntent = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setGravity(int r1) {
            this.mGravity = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setHintAmbientBigPicture(boolean r2) {
            setFlag(32, r2);
            return this;
        }

        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(boolean r2) {
            setFlag(16, r2);
            return this;
        }

        public WearableExtender setHintContentIntentLaunchesActivity(boolean r2) {
            setFlag(64, r2);
            return this;
        }

        @Deprecated
        public WearableExtender setHintHideIcon(boolean r2) {
            setFlag(2, r2);
            return this;
        }

        @Deprecated
        public WearableExtender setHintScreenTimeout(int r1) {
            this.mHintScreenTimeout = r1;
            return this;
        }

        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean r2) {
            setFlag(4, r2);
            return this;
        }

        public WearableExtender setStartScrollBottom(boolean r2) {
            setFlag(8, r2);
            return this;
        }
    }

    @Deprecated
    public NotificationCompat() {
    }

    public static Action getAction(Notification r1, int r2) {
        return getActionCompatFromAction(r1.actions[r2]);
    }

    static Action getActionCompatFromAction(Notification.Action r18) {
        android.app.RemoteInput[] r1 = r18.getRemoteInputs();
        boolean r3 = false;
        if (r1 != null) goto L5;
        RemoteInput[] r11 = null;
    L15:
        if (Build.VERSION.SDK_INT >= 24) goto L17;
        boolean r13 = r18.getExtras().getBoolean("android.support.allowGeneratedReplies");
    L25:
        boolean r15 = r18.getExtras().getBoolean("android.support.action.showsUserInterface", true);
        if (Build.VERSION.SDK_INT < 28) goto L28;
        int r14 = r18.getSemanticAction();
    L30:
        if (Build.VERSION.SDK_INT < 29) goto L32;
        boolean r16 = r18.isContextual();
    L34:
        if (Build.VERSION.SDK_INT < 31) goto L36;
        r3 = r18.isAuthenticationRequired();
    L36:
        boolean r17 = r3;
        if (r18.getIcon() != null) goto L43;
        if (r18.icon == 0) goto L43;
        return new Action(r18.icon, r18.title, r18.actionIntent, r18.getExtras(), r11, null, r13, r14, r15, r16, r17);
    L43:
        if (r18.getIcon() != null) goto L45;
        IconCompat r2 = null;
    L47:
        return new Action(r2, r18.title, r18.actionIntent, r18.getExtras(), r11, null, r13, r14, r15, r16, r17);
    L45:
        r2 = IconCompat.createFromIconOrNullIfZeroResId(r18.getIcon());
        goto L47
    L32:
        r16 = false;
        goto L34
    L28:
        r14 = r18.getExtras().getInt("android.support.action.semanticAction", 0);
        goto L30
    L17:
        if (r18.getExtras().getBoolean("android.support.allowGeneratedReplies") == false) goto L19;
    L22:
        boolean r4 = true;
    L23:
        r13 = r4;
        goto L25
    L19:
        if (r18.getAllowGeneratedReplies() == true) goto L22;
        r4 = false;
        goto L23
    L5:
        RemoteInput[] r5 = new RemoteInput[r1.length];
        int r6 = 0;
    L7:
        if (r6 >= r1.length) goto L13;
        android.app.RemoteInput r7 = r1[r6];
        String r8 = r7.getResultKey();
        CharSequence r9 = r7.getLabel();
        CharSequence[] r10 = r7.getChoices();
        boolean r12 = r7.getAllowFreeFormInput();
        if (Build.VERSION.SDK_INT < 29) goto L11;
        int r19 = r7.getEditChoicesBeforeSending();
    L12:
        r5[r6] = new RemoteInput(r8, r9, r10, r12, r19, r7.getExtras(), null);
        r6 = r6 + 1;
        goto L7
    L11:
        r19 = 0;
        goto L12
    L13:
        r11 = r5;
        goto L15
    }

    public static int getActionCount(Notification r1) {
        if (r1.actions != null) goto L7;
        return 0;
    L7:
        return r1.actions.length;
    }

    public static boolean getAllowSystemGeneratedContextualActions(Notification r2) {
        if (Build.VERSION.SDK_INT >= 29) goto L5;
        return false;
    L5:
        return r2.getAllowSystemGeneratedContextualActions();
    }

    public static boolean getAutoCancel(Notification r1) {
        if ((r1.flags & 16) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static int getBadgeIconType(Notification r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return 0;
    L5:
        return r2.getBadgeIconType();
    }

    public static BubbleMetadata getBubbleMetadata(Notification r2) {
        if (Build.VERSION.SDK_INT >= 29) goto L5;
        return null;
    L5:
        return BubbleMetadata.fromPlatform(r2.getBubbleMetadata());
    }

    public static String getCategory(Notification r1) {
        return r1.category;
    }

    public static String getChannelId(Notification r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return null;
    L5:
        return r2.getChannelId();
    }

    public static int getColor(Notification r1) {
        return r1.color;
    }

    public static CharSequence getContentInfo(Notification r2) {
        return r2.extras.getCharSequence(EXTRA_INFO_TEXT);
    }

    public static CharSequence getContentText(Notification r2) {
        return r2.extras.getCharSequence(EXTRA_TEXT);
    }

    public static CharSequence getContentTitle(Notification r2) {
        return r2.extras.getCharSequence(EXTRA_TITLE);
    }

    public static Bundle getExtras(Notification r1) {
        return r1.extras;
    }

    public static String getGroup(Notification r1) {
        return r1.getGroup();
    }

    public static int getGroupAlertBehavior(Notification r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return 0;
    L5:
        return r2.getGroupAlertBehavior();
    }

    static boolean getHighPriority(Notification r1) {
        if ((r1.flags & 128) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static List<Action> getInvisibleActions(Notification r5) {
        ArrayList r0 = new ArrayList();
        Bundle r1 = r5.extras.getBundle("android.car.EXTENSIONS");
        if (r1 != null) goto L5;
        return r0;
    L5:
        Bundle r2 = r1.getBundle("invisible_actions");
        if (r2 == null) goto L11;
        int r3 = 0;
    L9:
        if (r3 >= r2.size()) goto L11;
        r0.add(NotificationCompatJellybean.getActionFromBundle(r2.getBundle(Integer.toString(r3))));
        r3 = r3 + 1;
    L11:
        return r0;
    }

    public static boolean getLocalOnly(Notification r1) {
        if ((r1.flags & 256) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static LocusIdCompat getLocusId(Notification r3) {
        if (Build.VERSION.SDK_INT < 29) goto L9;
        LocusId r0 = r3.getLocusId();
        if (r0 != null) goto L8;
        return null;
    L8:
        return LocusIdCompat.toLocusIdCompat(r0);
    L9:
        return null;
    }

    static Notification[] getNotificationArrayFromBundle(Bundle r4, String r5) {
        Parcelable[] r0 = r4.getParcelableArray(r5);
        if ((r0 instanceof Notification[]) == true) goto L13;
        if (r0 == null) goto L13;
        Notification[] r1 = new Notification[r0.length];
        int r2 = 0;
    L8:
        if (r2 >= r0.length) goto L10;
        r1[r2] = (Notification) r0[r2];
        r2 = r2 + 1;
        goto L8
    L10:
        r4.putParcelableArray(r5, r1);
        return r1;
    L13:
        return (Notification[]) r0;
    }

    public static boolean getOngoing(Notification r1) {
        if ((r1.flags & 2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static boolean getOnlyAlertOnce(Notification r1) {
        if ((r1.flags & 8) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static List<Person> getPeople(Notification r6) {
        ArrayList r0 = new ArrayList();
        if (Build.VERSION.SDK_INT < 28) goto L13;
        ArrayList r1 = r6.extras.getParcelableArrayList(EXTRA_PEOPLE_LIST);
        if (r1 != null) goto L7;
    L20:
        return r0;
    L7:
        if (r1.isEmpty() == true) goto L20;
        Iterator r2 = r1.iterator();
    L10:
        if (r2.hasNext() == false) goto L20;
        r0.add(Person.fromAndroidPerson((android.app.Person) r2.next()));
        goto L10
    L13:
        String[] r3 = r6.extras.getStringArray(EXTRA_PEOPLE);
        if (r3 == null) goto L20;
        if (r3.length == 0) goto L20;
        int r4 = r3.length;
        int r5 = 0;
    L18:
        if (r5 >= r4) goto L20;
        String r7 = r3[r5];
        r0.add(new Person.Builder().setUri(r7).build());
        r5 = r5 + 1;
        goto L18
    }

    public static Notification getPublicVersion(Notification r1) {
        return r1.publicVersion;
    }

    public static CharSequence getSettingsText(Notification r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return null;
    L5:
        return r2.getSettingsText();
    }

    public static String getShortcutId(Notification r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return null;
    L5:
        return r2.getShortcutId();
    }

    public static boolean getShowWhen(Notification r2) {
        return r2.extras.getBoolean(EXTRA_SHOW_WHEN);
    }

    public static String getSortKey(Notification r1) {
        return r1.getSortKey();
    }

    public static CharSequence getSubText(Notification r2) {
        return r2.extras.getCharSequence(EXTRA_SUB_TEXT);
    }

    public static long getTimeoutAfter(Notification r2) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        return 0;
    L5:
        return r2.getTimeoutAfter();
    }

    public static boolean getUsesChronometer(Notification r2) {
        return r2.extras.getBoolean(EXTRA_SHOW_CHRONOMETER);
    }

    public static int getVisibility(Notification r1) {
        return r1.visibility;
    }

    public static boolean isGroupSummary(Notification r1) {
        if ((r1.flags & 512) == 0) goto L5;
        return true;
    L5:
        return false;
    }
}
