package androidx.core.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class NotificationChannelCompat {
    public static final String DEFAULT_CHANNEL_ID = "miscellaneous";
    private static final int DEFAULT_LIGHT_COLOR = 0;
    private static final boolean DEFAULT_SHOW_BADGE = true;
    AudioAttributes mAudioAttributes;
    private boolean mBypassDnd;
    private boolean mCanBubble;
    String mConversationId;
    String mDescription;
    String mGroupId;
    final String mId;
    int mImportance;
    private boolean mImportantConversation;
    int mLightColor;
    boolean mLights;
    private int mLockscreenVisibility;
    CharSequence mName;
    String mParentId;
    boolean mShowBadge;
    Uri mSound;
    boolean mVibrationEnabled;
    long[] mVibrationPattern;

    public static class Builder {
        private final NotificationChannelCompat mChannel;

        public Builder(String r2, int r3) {
            this.mChannel = new NotificationChannelCompat(r2, r3);
        }

        public NotificationChannelCompat build() {
            return this.mChannel;
        }

        public Builder setConversationId(String r3, String r4) {
            if (Build.VERSION.SDK_INT < 30) goto L5;
            this.mChannel.mParentId = r3;
            this.mChannel.mConversationId = r4;
        L5:
            return this;
        }

        public Builder setDescription(String r2) {
            this.mChannel.mDescription = r2;
            return this;
        }

        public Builder setGroup(String r2) {
            this.mChannel.mGroupId = r2;
            return this;
        }

        public Builder setImportance(int r2) {
            this.mChannel.mImportance = r2;
            return this;
        }

        public Builder setLightColor(int r2) {
            this.mChannel.mLightColor = r2;
            return this;
        }

        public Builder setLightsEnabled(boolean r2) {
            this.mChannel.mLights = r2;
            return this;
        }

        public Builder setName(CharSequence r2) {
            this.mChannel.mName = r2;
            return this;
        }

        public Builder setShowBadge(boolean r2) {
            this.mChannel.mShowBadge = r2;
            return this;
        }

        public Builder setSound(Uri r2, AudioAttributes r3) {
            this.mChannel.mSound = r2;
            this.mChannel.mAudioAttributes = r3;
            return this;
        }

        public Builder setVibrationEnabled(boolean r2) {
            this.mChannel.mVibrationEnabled = r2;
            return this;
        }

        public Builder setVibrationPattern(long[] r3) {
            NotificationChannelCompat r0 = this.mChannel;
            if (r3 != null) goto L5;
        L7:
            boolean r1 = false;
        L8:
            r0.mVibrationEnabled = r1;
            this.mChannel.mVibrationPattern = r3;
            return this;
        L5:
            if (r3.length <= 0) goto L7;
            r1 = true;
            goto L8
        }
    }

    NotificationChannelCompat(NotificationChannel r4) {
        this(r4.getId(), r4.getImportance());
        this.mName = r4.getName();
        this.mDescription = r4.getDescription();
        this.mGroupId = r4.getGroup();
        this.mShowBadge = r4.canShowBadge();
        this.mSound = r4.getSound();
        this.mAudioAttributes = r4.getAudioAttributes();
        this.mLights = r4.shouldShowLights();
        this.mLightColor = r4.getLightColor();
        this.mVibrationEnabled = r4.shouldVibrate();
        this.mVibrationPattern = r4.getVibrationPattern();
        if (Build.VERSION.SDK_INT < 30) goto L5;
        this.mParentId = r4.getParentChannelId();
        this.mConversationId = r4.getConversationId();
    L5:
        this.mBypassDnd = r4.canBypassDnd();
        this.mLockscreenVisibility = r4.getLockscreenVisibility();
        if (Build.VERSION.SDK_INT < 29) goto L9;
        this.mCanBubble = r4.canBubble();
    L9:
        if (Build.VERSION.SDK_INT < 30) goto L12;
        this.mImportantConversation = r4.isImportantConversation();
        return;
    }

    NotificationChannelCompat(String r2, int r3) {
        this.mShowBadge = true;
        this.mSound = Settings.System.DEFAULT_NOTIFICATION_URI;
        this.mLightColor = 0;
        this.mId = (String) Preconditions.checkNotNull(r2);
        this.mImportance = r3;
        this.mAudioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
    }

    public boolean canBubble() {
        return this.mCanBubble;
    }

    public boolean canBypassDnd() {
        return this.mBypassDnd;
    }

    public boolean canShowBadge() {
        return this.mShowBadge;
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public String getConversationId() {
        return this.mConversationId;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getGroup() {
        return this.mGroupId;
    }

    public String getId() {
        return this.mId;
    }

    public int getImportance() {
        return this.mImportance;
    }

    public int getLightColor() {
        return this.mLightColor;
    }

    public int getLockscreenVisibility() {
        return this.mLockscreenVisibility;
    }

    public CharSequence getName() {
        return this.mName;
    }

    NotificationChannel getNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) goto L6;
        return null;
    L6:
        NotificationChannel r0 = new NotificationChannel(this.mId, this.mName, this.mImportance);
        r0.setDescription(this.mDescription);
        r0.setGroup(this.mGroupId);
        r0.setShowBadge(this.mShowBadge);
        r0.setSound(this.mSound, this.mAudioAttributes);
        r0.enableLights(this.mLights);
        r0.setLightColor(this.mLightColor);
        r0.setVibrationPattern(this.mVibrationPattern);
        r0.enableVibration(this.mVibrationEnabled);
        if (Build.VERSION.SDK_INT >= 30) goto L9;
    L13:
        return r0;
    L9:
        if (this.mParentId == null) goto L13;
        if (this.mConversationId == null) goto L13;
        r0.setConversationId(this.mParentId, this.mConversationId);
        goto L13
    }

    public String getParentChannelId() {
        return this.mParentId;
    }

    public Uri getSound() {
        return this.mSound;
    }

    public long[] getVibrationPattern() {
        return this.mVibrationPattern;
    }

    public boolean isImportantConversation() {
        return this.mImportantConversation;
    }

    public boolean shouldShowLights() {
        return this.mLights;
    }

    public boolean shouldVibrate() {
        return this.mVibrationEnabled;
    }

    public Builder toBuilder() {
        return new Builder(this.mId, this.mImportance).setName(this.mName).setDescription(this.mDescription).setGroup(this.mGroupId).setShowBadge(this.mShowBadge).setSound(this.mSound, this.mAudioAttributes).setLightsEnabled(this.mLights).setLightColor(this.mLightColor).setVibrationEnabled(this.mVibrationEnabled).setVibrationPattern(this.mVibrationPattern).setConversationId(this.mParentId, this.mConversationId);
    }
}
