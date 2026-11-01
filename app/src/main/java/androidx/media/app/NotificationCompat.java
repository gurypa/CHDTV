package androidx.media.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.media.C0175R;

/* JADX INFO: loaded from: classes.dex */
public class NotificationCompat {

    private static class Api15Impl {
        private Api15Impl() {
        }

        static void setContentDescription(RemoteViews r0, int r1, CharSequence r2) {
            r0.setContentDescription(r1, r2);
        }
    }

    private static class Api21Impl {
        private Api21Impl() {
        }

        static Notification.MediaStyle createMediaStyle() {
            return new Notification.MediaStyle();
        }

        static Notification.MediaStyle fillInMediaStyle(Notification.MediaStyle r1, int[] r2, MediaSessionCompat.Token r3) {
            if (r2 == null) goto L4;
            setShowActionsInCompactView(r1, r2);
        L4:
            if (r3 == null) goto L6;
            setMediaSession(r1, (MediaSession.Token) r3.getToken());
        L6:
            return r1;
        }

        static void setMediaSession(Notification.MediaStyle r0, MediaSession.Token r1) {
            r0.setMediaSession(r1);
        }

        static void setMediaStyle(Notification.Builder r0, Notification.MediaStyle r1) {
            r0.setStyle(r1);
        }

        static void setShowActionsInCompactView(Notification.MediaStyle r0, int... r1) {
            r0.setShowActionsInCompactView(r1);
        }
    }

    private static class Api24Impl {
        private Api24Impl() {
        }

        static Notification.MediaStyle createDecoratedMediaCustomViewStyle() {
            return new Notification.DecoratedMediaCustomViewStyle();
        }
    }

    private static class Api34Impl {
        private Api34Impl() {
        }

        static Notification.MediaStyle setRemotePlaybackInfo(Notification.MediaStyle r1, CharSequence r2, int r3, PendingIntent r4, Boolean r5) {
            if (r5.booleanValue() == false) goto L5;
            r1.setRemotePlaybackInfo(r2, r3, r4);
        L5:
            return r1;
        }
    }

    public static class DecoratedMediaCustomViewStyle extends MediaStyle {
        public DecoratedMediaCustomViewStyle() {
        }

        private void setBackgroundColor(RemoteViews r4) {
            if (this.mBuilder.getColor() == 0) goto L5;
            int r0 = this.mBuilder.getColor();
        L6:
            r4.setInt(C0175R.id.status_bar_latest_event_content, "setBackgroundColor", r0);
            return;
        L5:
            r0 = this.mBuilder.mContext.getResources().getColor(C0175R.color.notification_material_background_media_default_color);
            goto L6
        }

        @Override // androidx.media.app.NotificationCompat.MediaStyle, androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r7) {
            if (Build.VERSION.SDK_INT < 34) goto L6;
            Api21Impl.setMediaStyle(r7.getBuilder(), Api21Impl.fillInMediaStyle(Api34Impl.setRemotePlaybackInfo(Api24Impl.createDecoratedMediaCustomViewStyle(), this.mDeviceName, this.mDeviceIcon, this.mDeviceIntent, Boolean.valueOf(this.mShowRemotePlaybackInfo)), this.mActionsToShowInCompact, this.mToken));
            return;
        L6:
            if (Build.VERSION.SDK_INT < 24) goto L8;
            Api21Impl.setMediaStyle(r7.getBuilder(), Api21Impl.fillInMediaStyle(Api24Impl.createDecoratedMediaCustomViewStyle(), this.mActionsToShowInCompact, this.mToken));
            return;
        L8:
            super.apply(r7);
        }

        @Override // androidx.media.app.NotificationCompat.MediaStyle
        int getBigContentViewLayoutResource(int r2) {
            if (r2 > 3) goto L6;
            return C0175R.layout.notification_template_big_media_narrow_custom;
        L6:
            return C0175R.layout.notification_template_big_media_custom;
        }

        @Override // androidx.media.app.NotificationCompat.MediaStyle
        int getContentViewLayoutResource() {
            if (this.mBuilder.getContentView() == null) goto L6;
            return C0175R.layout.notification_template_media_custom;
        L6:
            return super.getContentViewLayoutResource();
        }

        @Override // androidx.media.app.NotificationCompat.MediaStyle, androidx.core.app.NotificationCompat.Style
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor r4) {
            if (Build.VERSION.SDK_INT < 24) goto L6;
            return null;
        L6:
            if (this.mBuilder.getBigContentView() == null) goto L8;
            RemoteViews r0 = this.mBuilder.getBigContentView();
        L10:
            if (r0 != null) goto L12;
            return null;
        L12:
            RemoteViews r1 = generateBigContentView();
            buildIntoRemoteViews(r1, r0);
            setBackgroundColor(r1);
            return r1;
        L8:
            r0 = this.mBuilder.getContentView();
            goto L10
        }

        @Override // androidx.media.app.NotificationCompat.MediaStyle, androidx.core.app.NotificationCompat.Style
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor r6) {
            if (Build.VERSION.SDK_INT < 24) goto L5;
            return null;
        L5:
            boolean r1 = true;
            if (this.mBuilder.getContentView() == null) goto L8;
            boolean r0 = true;
        L10:
            if (r0 == false) goto L12;
        L16:
            if (r1 == false) goto L23;
            RemoteViews r2 = generateContentView();
            if (r0 == false) goto L20;
            buildIntoRemoteViews(r2, this.mBuilder.getContentView());
        L20:
            setBackgroundColor(r2);
            return r2;
        L23:
            return null;
        L12:
            if (this.mBuilder.getBigContentView() != null) goto L16;
            r1 = false;
            goto L16
        L8:
            r0 = false;
            goto L10
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor r4) {
            if (Build.VERSION.SDK_INT < 24) goto L6;
            return null;
        L6:
            if (this.mBuilder.getHeadsUpContentView() == null) goto L8;
            RemoteViews r0 = this.mBuilder.getHeadsUpContentView();
        L10:
            if (r0 != null) goto L12;
            return null;
        L12:
            RemoteViews r1 = generateBigContentView();
            buildIntoRemoteViews(r1, r0);
            setBackgroundColor(r1);
            return r1;
        L8:
            r0 = this.mBuilder.getContentView();
            goto L10
        }
    }

    public static class MediaStyle extends androidx.core.app.NotificationCompat.Style {
        private static final int MAX_MEDIA_BUTTONS = 5;
        private static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
        int[] mActionsToShowInCompact;
        PendingIntent mCancelButtonIntent;
        int mDeviceIcon;
        PendingIntent mDeviceIntent;
        CharSequence mDeviceName;
        boolean mShowCancelButton;
        boolean mShowRemotePlaybackInfo;
        MediaSessionCompat.Token mToken;

        public MediaStyle() {
            this.mActionsToShowInCompact = null;
            this.mShowRemotePlaybackInfo = false;
        }

        public MediaStyle(androidx.core.app.NotificationCompat.Builder r2) {
            this.mActionsToShowInCompact = null;
            this.mShowRemotePlaybackInfo = false;
            setBuilder(r2);
        }

        private RemoteViews generateMediaActionButton(androidx.core.app.NotificationCompat.Action r5) {
            if (r5.getActionIntent() != null) goto L5;
            boolean r0 = true;
        L6:
            RemoteViews r1 = new RemoteViews(this.mBuilder.mContext.getPackageName(), C0175R.layout.notification_media_action);
            r1.setImageViewResource(C0175R.id.action0, r5.getIcon());
            if (r0 == true) goto L9;
            r1.setOnClickPendingIntent(C0175R.id.action0, r5.getActionIntent());
        L9:
            Api15Impl.setContentDescription(r1, C0175R.id.action0, r5.getTitle());
            return r1;
        L5:
            r0 = false;
            goto L6
        }

        public static MediaSessionCompat.Token getMediaSession(Notification r3) {
            Bundle r0 = androidx.core.app.NotificationCompat.getExtras(r3);
            if (r0 == null) goto L9;
            Parcelable r1 = r0.getParcelable(androidx.core.app.NotificationCompat.EXTRA_MEDIA_SESSION);
            if (r1 != null) goto L7;
            return null;
        L7:
            return MediaSessionCompat.Token.fromToken(r1);
        L9:
            return null;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor r7) {
            if (Build.VERSION.SDK_INT < 34) goto L5;
            Api21Impl.setMediaStyle(r7.getBuilder(), Api21Impl.fillInMediaStyle(Api34Impl.setRemotePlaybackInfo(Api21Impl.createMediaStyle(), this.mDeviceName, this.mDeviceIcon, this.mDeviceIntent, Boolean.valueOf(this.mShowRemotePlaybackInfo)), this.mActionsToShowInCompact, this.mToken));
            return;
        L5:
            Api21Impl.setMediaStyle(r7.getBuilder(), Api21Impl.fillInMediaStyle(Api21Impl.createMediaStyle(), this.mActionsToShowInCompact, this.mToken));
        }

        RemoteViews generateBigContentView() {
            int r0 = Math.min(this.mBuilder.mActions.size(), 5);
            RemoteViews r1 = applyStandardTemplate(false, getBigContentViewLayoutResource(r0), false);
            r1.removeAllViews(C0175R.id.media_actions);
            if (r0 <= 0) goto L8;
            int r3 = 0;
        L5:
            if (r3 >= r0) goto L8;
            RemoteViews r4 = generateMediaActionButton(this.mBuilder.mActions.get(r3));
            r1.addView(C0175R.id.media_actions, r4);
            r3 = r3 + 1;
        L8:
            if (this.mShowCancelButton == false) goto L10;
            r1.setViewVisibility(C0175R.id.cancel_action, 0);
            r1.setInt(C0175R.id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(C0175R.integer.cancel_button_image_alpha));
            r1.setOnClickPendingIntent(C0175R.id.cancel_action, this.mCancelButtonIntent);
        L11:
            return r1;
        L10:
            r1.setViewVisibility(C0175R.id.cancel_action, 8);
            goto L11
        }

        RemoteViews generateContentView() {
            RemoteViews r0 = applyStandardTemplate(false, getContentViewLayoutResource(), true);
            int r3 = this.mBuilder.mActions.size();
            if (this.mActionsToShowInCompact != null) goto L5;
            int r4 = 0;
        L6:
            r0.removeAllViews(C0175R.id.media_actions);
            if (r4 <= 0) goto L15;
            int r5 = 0;
        L9:
            if (r5 >= r4) goto L15;
            if (r5 >= r3) goto L13;
            r0.addView(C0175R.id.media_actions, generateMediaActionButton(this.mBuilder.mActions.get(this.mActionsToShowInCompact[r5])));
            r5 = r5 + 1;
            goto L9
        L13:
            throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[]{Integer.valueOf(r5), Integer.valueOf(r3 - 1)}));
        L15:
            if (this.mShowCancelButton == false) goto L17;
            r0.setViewVisibility(C0175R.id.end_padder, 8);
            r0.setViewVisibility(C0175R.id.cancel_action, 0);
            r0.setOnClickPendingIntent(C0175R.id.cancel_action, this.mCancelButtonIntent);
            r0.setInt(C0175R.id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(C0175R.integer.cancel_button_image_alpha));
        L18:
            return r0;
        L17:
            r0.setViewVisibility(C0175R.id.end_padder, 0);
            r0.setViewVisibility(C0175R.id.cancel_action, 8);
            goto L18
        L5:
            r4 = Math.min(this.mActionsToShowInCompact.length, 3);
            goto L6
        }

        int getBigContentViewLayoutResource(int r2) {
            if (r2 > 3) goto L6;
            return C0175R.layout.notification_template_big_media_narrow;
        L6:
            return C0175R.layout.notification_template_big_media;
        }

        int getContentViewLayoutResource() {
            return C0175R.layout.notification_template_media;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor r2) {
            return null;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor r2) {
            return null;
        }

        public MediaStyle setCancelButtonIntent(PendingIntent r1) {
            this.mCancelButtonIntent = r1;
            return this;
        }

        public MediaStyle setMediaSession(MediaSessionCompat.Token r1) {
            this.mToken = r1;
            return this;
        }

        public MediaStyle setRemotePlaybackInfo(CharSequence r2, int r3, PendingIntent r4) {
            this.mDeviceName = r2;
            this.mDeviceIcon = r3;
            this.mDeviceIntent = r4;
            this.mShowRemotePlaybackInfo = true;
            return this;
        }

        public MediaStyle setShowActionsInCompactView(int... r1) {
            this.mActionsToShowInCompact = r1;
            return this;
        }

        public MediaStyle setShowCancelButton(boolean r1) {
            return this;
        }
    }

    private NotificationCompat() {
    }
}
