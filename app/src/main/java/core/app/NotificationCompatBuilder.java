package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;
import androidx.collection.ArraySet;
import androidx.core.graphics.drawable.IconCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private final List<Bundle> mActionExtrasList;
    private RemoteViews mBigContentView;
    private final Notification.Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Context mContext;
    private final Bundle mExtras;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;

    NotificationCompatBuilder(NotificationCompat.Builder r15) {
        this.mActionExtrasList = new ArrayList();
        this.mExtras = new Bundle();
        this.mBuilderCompat = r15;
        this.mContext = r15.mContext;
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mBuilder = new Notification.Builder(r15.mContext, r15.mChannelId);
    L6:
        Notification r0 = r15.mNotification;
        Notification.Builder r2 = this.mBuilder.setWhen(r0.when).setSmallIcon(r0.icon, r0.iconLevel).setContent(r0.contentView).setTicker(r0.tickerText, r15.mTickerView).setVibrate(r0.vibrate).setLights(r0.ledARGB, r0.ledOnMS, r0.ledOffMS);
        if ((r0.flags & 2) == 0) goto L9;
        boolean r3 = true;
    L10:
        Notification.Builder r4 = r2.setOngoing(r3);
        if ((r0.flags & 8) == 0) goto L13;
        boolean r5 = true;
    L14:
        Notification.Builder r6 = r4.setOnlyAlertOnce(r5);
        if ((r0.flags & 16) == 0) goto L17;
        boolean r7 = true;
    L18:
        Notification.Builder r8 = r6.setAutoCancel(r7).setDefaults(r0.defaults).setContentTitle(r15.mContentTitle).setContentText(r15.mContentText).setContentInfo(r15.mContentInfo).setContentIntent(r15.mContentIntent).setDeleteIntent(r0.deleteIntent);
        PendingIntent r9 = r15.mFullScreenIntent;
        if ((r0.flags & 128) == 0) goto L21;
        boolean r10 = true;
    L22:
        r8.setFullScreenIntent(r9, r10).setLargeIcon(r15.mLargeIcon).setNumber(r15.mNumber).setProgress(r15.mProgressMax, r15.mProgress, r15.mProgressIndeterminate);
        this.mBuilder.setSubText(r15.mSubText).setUsesChronometer(r15.mUseChronometer).setPriority(r15.mPriority);
        Iterator<NotificationCompat.Action> r11 = r15.mActions.iterator();
    L24:
        if (r11.hasNext() == false) goto L27;
        addAction(r11.next());
        goto L24
    L27:
        if (r15.mExtras == null) goto L29;
        this.mExtras.putAll(r15.mExtras);
    L29:
        this.mContentView = r15.mContentView;
        this.mBigContentView = r15.mBigContentView;
        this.mBuilder.setShowWhen(r15.mShowWhen);
        this.mBuilder.setLocalOnly(r15.mLocalOnly).setGroup(r15.mGroupKey).setGroupSummary(r15.mGroupSummary).setSortKey(r15.mSortKey);
        this.mGroupAlertBehavior = r15.mGroupAlertBehavior;
        this.mBuilder.setCategory(r15.mCategory).setColor(r15.mColor).setVisibility(r15.mVisibility).setPublicVersion(r15.mPublicVersion).setSound(r0.sound, r0.audioAttributes);
        if (Build.VERSION.SDK_INT >= 28) goto L32;
        List r12 = combineLists(getPeople(r15.mPersonList), r15.mPeople);
    L33:
        if (r12 != null) goto L35;
    L40:
        this.mHeadsUpContentView = r15.mHeadsUpContentView;
        if (r15.mInvisibleActions.size() <= 0) goto L51;
        Bundle r13 = r15.getExtras().getBundle("android.car.EXTENSIONS");
        if (r13 != null) goto L45;
        r13 = new Bundle();
    L45:
        Bundle r14 = new Bundle(r13);
        Bundle r16 = new Bundle();
        int r17 = 0;
    L47:
        if (r17 >= r15.mInvisibleActions.size()) goto L49;
        r16.putBundle(Integer.toString(r17), NotificationCompatJellybean.getBundleForAction(r15.mInvisibleActions.get(r17)));
        r17 = r17 + 1;
        goto L47
    L49:
        r13.putBundle("invisible_actions", r16);
        r14.putBundle("invisible_actions", r16);
        r15.getExtras().putBundle("android.car.EXTENSIONS", r13);
        this.mExtras.putBundle("android.car.EXTENSIONS", r14);
    L51:
        if (r15.mSmallIcon == null) goto L54;
        this.mBuilder.setSmallIcon(r15.mSmallIcon);
    L54:
        if (Build.VERSION.SDK_INT < 24) goto L65;
        this.mBuilder.setExtras(r15.mExtras).setRemoteInputHistory(r15.mRemoteInputHistory);
        if (r15.mContentView == null) goto L59;
        this.mBuilder.setCustomContentView(r15.mContentView);
    L59:
        if (r15.mBigContentView == null) goto L62;
        this.mBuilder.setCustomBigContentView(r15.mBigContentView);
    L62:
        if (r15.mHeadsUpContentView == null) goto L65;
        this.mBuilder.setCustomHeadsUpContentView(r15.mHeadsUpContentView);
    L65:
        if (Build.VERSION.SDK_INT < 26) goto L73;
        this.mBuilder.setBadgeIconType(r15.mBadgeIcon).setSettingsText(r15.mSettingsText).setShortcutId(r15.mShortcutId).setTimeoutAfter(r15.mTimeout).setGroupAlertBehavior(r15.mGroupAlertBehavior);
        if (r15.mColorizedSet == false) goto L70;
        this.mBuilder.setColorized(r15.mColorized);
    L70:
        if (TextUtils.isEmpty(r15.mChannelId) == true) goto L73;
        this.mBuilder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
    L73:
        if (Build.VERSION.SDK_INT < 28) goto L79;
        Iterator<Person> r18 = r15.mPersonList.iterator();
    L76:
        if (r18.hasNext() == false) goto L79;
        this.mBuilder.addPerson(r18.next().toAndroidPerson());
    L79:
        if (Build.VERSION.SDK_INT < 29) goto L84;
        this.mBuilder.setAllowSystemGeneratedContextualActions(r15.mAllowSystemGeneratedContextualActions);
        this.mBuilder.setBubbleMetadata(NotificationCompat.BubbleMetadata.toPlatform(r15.mBubbleMetadata));
        if (r15.mLocusId == null) goto L84;
        this.mBuilder.setLocusId(r15.mLocusId.toLocusId());
    L84:
        if (Build.VERSION.SDK_INT < 31) goto L89;
        if (r15.mFgsDeferBehavior == 0) goto L89;
        this.mBuilder.setForegroundServiceBehavior(r15.mFgsDeferBehavior);
    L89:
        if (r15.mSilent == true) goto L91;
        return;
    L91:
        if (this.mBuilderCompat.mGroupSummary == false) goto L93;
        this.mGroupAlertBehavior = 2;
    L94:
        this.mBuilder.setVibrate(null);
        this.mBuilder.setSound(null);
        r0.defaults &= -2;
        r0.defaults &= -3;
        this.mBuilder.setDefaults(r0.defaults);
        if (Build.VERSION.SDK_INT >= 26) goto L97;
        return;
    L97:
        if (TextUtils.isEmpty(this.mBuilderCompat.mGroupKey) == false) goto L99;
        this.mBuilder.setGroup(NotificationCompat.GROUP_KEY_SILENT);
    L99:
        this.mBuilder.setGroupAlertBehavior(this.mGroupAlertBehavior);
        return;
    L93:
        this.mGroupAlertBehavior = 1;
        goto L94
    L35:
        if (r12.isEmpty() == true) goto L40;
        Iterator r19 = r12.iterator();
    L38:
        if (r19.hasNext() == false) goto L40;
        this.mBuilder.addPerson((String) r19.next());
        goto L38
    L32:
        r12 = r15.mPeople;
        goto L33
    L21:
        r10 = false;
        goto L22
    L17:
        r7 = false;
        goto L18
    L13:
        r5 = false;
        goto L14
    L9:
        r3 = false;
        goto L10
    L5:
        this.mBuilder = new Notification.Builder(r15.mContext);
        goto L6
    }

    private void addAction(NotificationCompat.Action r7) {
        IconCompat r0 = r7.getIconCompat();
        if (r0 == null) goto L5;
        Icon r2 = r0.toIcon();
    L6:
        Notification.Action.Builder r1 = new Notification.Action.Builder(r2, r7.getTitle(), r7.getActionIntent());
        if (r7.getRemoteInputs() == null) goto L12;
        android.app.RemoteInput[] r3 = RemoteInput.fromCompat(r7.getRemoteInputs());
        int r4 = r3.length;
        int r5 = 0;
    L9:
        if (r5 >= r4) goto L12;
        r1.addRemoteInput(r3[r5]);
        r5 = r5 + 1;
    L12:
        if (r7.getExtras() == null) goto L14;
        Bundle r6 = new Bundle(r7.getExtras());
    L15:
        r6.putBoolean("android.support.allowGeneratedReplies", r7.getAllowGeneratedReplies());
        if (Build.VERSION.SDK_INT < 24) goto L18;
        r1.setAllowGeneratedReplies(r7.getAllowGeneratedReplies());
    L18:
        r6.putInt("android.support.action.semanticAction", r7.getSemanticAction());
        if (Build.VERSION.SDK_INT < 28) goto L22;
        r1.setSemanticAction(r7.getSemanticAction());
    L22:
        if (Build.VERSION.SDK_INT < 29) goto L25;
        r1.setContextual(r7.isContextual());
    L25:
        if (Build.VERSION.SDK_INT < 31) goto L27;
        r1.setAuthenticationRequired(r7.isAuthenticationRequired());
    L27:
        r6.putBoolean("android.support.action.showsUserInterface", r7.getShowsUserInterface());
        r1.addExtras(r6);
        this.mBuilder.addAction(r1.build());
        return;
    L14:
        r6 = new Bundle();
        goto L15
    L5:
        r2 = null;
        goto L6
    }

    private static List<String> combineLists(List<String> r3, List<String> r4) {
        if (r3 != null) goto L4;
        return r4;
    L4:
        if (r4 != null) goto L6;
        return r3;
    L6:
        ArraySet r0 = new ArraySet(r3.size() + r4.size());
        r0.addAll(r3);
        r0.addAll(r4);
        return new ArrayList(r0);
    }

    private static List<String> getPeople(List<Person> r4) {
        if (r4 != null) goto L5;
        return null;
    L5:
        ArrayList r0 = new ArrayList(r4.size());
        Iterator<Person> r1 = r4.iterator();
    L7:
        if (r1.hasNext() == false) goto L9;
        r0.add(r1.next().resolveToLegacyUri());
        goto L7
    L9:
        return r0;
    }

    private void removeSoundAndVibration(Notification r2) {
        r2.sound = null;
        r2.vibrate = null;
        r2.defaults &= -2;
        r2.defaults &= -3;
    }

    public Notification build() {
        NotificationCompat.Style r0 = this.mBuilderCompat.mStyle;
        if (r0 == null) goto L5;
        r0.apply(this);
    L5:
        if (r0 == null) goto L7;
        RemoteViews r1 = r0.makeContentView(this);
    L8:
        Notification r2 = buildInternal();
        if (r1 == null) goto L12;
        r2.contentView = r1;
    L14:
        if (r0 == null) goto L18;
        RemoteViews r3 = r0.makeBigContentView(this);
        if (r3 == null) goto L18;
        r2.bigContentView = r3;
    L18:
        if (r0 == null) goto L22;
        RemoteViews r4 = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
        if (r4 == null) goto L22;
        r2.headsUpContentView = r4;
    L22:
        if (r0 == null) goto L26;
        Bundle r5 = NotificationCompat.getExtras(r2);
        if (r5 == null) goto L26;
        r0.addCompatExtras(r5);
    L26:
        return r2;
    L12:
        if (this.mBuilderCompat.mContentView == null) goto L14;
        r2.contentView = this.mBuilderCompat.mContentView;
        goto L14
    L7:
        r1 = null;
        goto L8
    }

    protected Notification buildInternal() {
        if (Build.VERSION.SDK_INT < 26) goto L7;
        return this.mBuilder.build();
    L7:
        if (Build.VERSION.SDK_INT < 24) goto L25;
        Notification r0 = this.mBuilder.build();
        if (this.mGroupAlertBehavior != 0) goto L11;
    L24:
        return r0;
    L11:
        if (r0.getGroup() == null) goto L18;
        if ((r0.flags & 512) == 0) goto L18;
        if (this.mGroupAlertBehavior != 2) goto L18;
        removeSoundAndVibration(r0);
    L18:
        if (r0.getGroup() == null) goto L24;
        if ((r0.flags & 512) != 0) goto L24;
        if (this.mGroupAlertBehavior != 1) goto L24;
        removeSoundAndVibration(r0);
        goto L24
    L25:
        this.mBuilder.setExtras(this.mExtras);
        Notification r1 = this.mBuilder.build();
        if (this.mContentView == null) goto L29;
        r1.contentView = this.mContentView;
    L29:
        if (this.mBigContentView == null) goto L32;
        r1.bigContentView = this.mBigContentView;
    L32:
        if (this.mHeadsUpContentView == null) goto L35;
        r1.headsUpContentView = this.mHeadsUpContentView;
    L35:
        if (this.mGroupAlertBehavior != 0) goto L37;
    L50:
        return r1;
    L37:
        if (r1.getGroup() == null) goto L44;
        if ((r1.flags & 512) == 0) goto L44;
        if (this.mGroupAlertBehavior != 2) goto L44;
        removeSoundAndVibration(r1);
    L44:
        if (r1.getGroup() == null) goto L50;
        if ((r1.flags & 512) != 0) goto L50;
        if (this.mGroupAlertBehavior != 1) goto L50;
        removeSoundAndVibration(r1);
        goto L50
    }

    @Override // androidx.core.app.NotificationBuilderWithBuilderAccessor
    public Notification.Builder getBuilder() {
        return this.mBuilder;
    }

    Context getContext() {
        return this.mContext;
    }
}
