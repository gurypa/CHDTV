package androidx.core.content.p000pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.text.TextUtils;
import androidx.core.app.Person;
import androidx.core.content.LocusIdCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.net.UriCompat;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ShortcutInfoCompat {
    private static final String EXTRA_LOCUS_ID = "extraLocusId";
    private static final String EXTRA_LONG_LIVED = "extraLongLived";
    private static final String EXTRA_PERSON_ = "extraPerson_";
    private static final String EXTRA_PERSON_COUNT = "extraPersonCount";
    private static final String EXTRA_SLICE_URI = "extraSliceUri";
    public static final int SURFACE_LAUNCHER = 1;
    ComponentName mActivity;
    Set<String> mCategories;
    Context mContext;
    CharSequence mDisabledMessage;
    int mDisabledReason;
    int mExcludedSurfaces;
    PersistableBundle mExtras;
    boolean mHasKeyFieldsOnly;
    IconCompat mIcon;
    String mId;
    Intent[] mIntents;
    boolean mIsAlwaysBadged;
    boolean mIsCached;
    boolean mIsDeclaredInManifest;
    boolean mIsDynamic;
    boolean mIsEnabled;
    boolean mIsImmutable;
    boolean mIsLongLived;
    boolean mIsPinned;
    CharSequence mLabel;
    long mLastChangedTimestamp;
    LocusIdCompat mLocusId;
    CharSequence mLongLabel;
    String mPackageName;
    Person[] mPersons;
    int mRank;
    Bundle mTransientExtras;
    UserHandle mUser;

    public static class Builder {
        private Map<String, Map<String, List<String>>> mCapabilityBindingParams;
        private Set<String> mCapabilityBindings;
        private final ShortcutInfoCompat mInfo;
        private boolean mIsConversation;
        private Uri mSliceUri;

        public Builder(Context r5, ShortcutInfo r6) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = r5;
            ShortcutInfoCompat r0 = this.mInfo;
            r0.mId = r6.getId();
            ShortcutInfoCompat r1 = this.mInfo;
            r1.mPackageName = r6.getPackage();
            Intent[] r2 = r6.getIntents();
            ShortcutInfoCompat r3 = this.mInfo;
            r3.mIntents = (Intent[]) Arrays.copyOf(r2, r2.length);
            ShortcutInfoCompat r4 = this.mInfo;
            r4.mActivity = r6.getActivity();
            ShortcutInfoCompat r7 = this.mInfo;
            r7.mLabel = r6.getShortLabel();
            ShortcutInfoCompat r8 = this.mInfo;
            r8.mLongLabel = r6.getLongLabel();
            ShortcutInfoCompat r9 = this.mInfo;
            r9.mDisabledMessage = r6.getDisabledMessage();
            if (Build.VERSION.SDK_INT < 28) goto L5;
            ShortcutInfoCompat r10 = this.mInfo;
            r10.mDisabledReason = r6.getDisabledReason();
        L10:
            ShortcutInfoCompat r11 = this.mInfo;
            r11.mCategories = r6.getCategories();
            ShortcutInfoCompat r12 = this.mInfo;
            r12.mPersons = ShortcutInfoCompat.getPersonsFromExtra(r6.getExtras());
            ShortcutInfoCompat r13 = this.mInfo;
            r13.mUser = r6.getUserHandle();
            ShortcutInfoCompat r14 = this.mInfo;
            r14.mLastChangedTimestamp = r6.getLastChangedTimestamp();
            if (Build.VERSION.SDK_INT < 30) goto L13;
            ShortcutInfoCompat r15 = this.mInfo;
            r15.mIsCached = r6.isCached();
        L13:
            ShortcutInfoCompat r16 = this.mInfo;
            r16.mIsDynamic = r6.isDynamic();
            ShortcutInfoCompat r17 = this.mInfo;
            r17.mIsPinned = r6.isPinned();
            ShortcutInfoCompat r18 = this.mInfo;
            r18.mIsDeclaredInManifest = r6.isDeclaredInManifest();
            ShortcutInfoCompat r19 = this.mInfo;
            r19.mIsImmutable = r6.isImmutable();
            ShortcutInfoCompat r20 = this.mInfo;
            r20.mIsEnabled = r6.isEnabled();
            ShortcutInfoCompat r21 = this.mInfo;
            r21.mHasKeyFieldsOnly = r6.hasKeyFieldsOnly();
            ShortcutInfoCompat r22 = this.mInfo;
            r22.mLocusId = ShortcutInfoCompat.getLocusId(r6);
            ShortcutInfoCompat r23 = this.mInfo;
            r23.mRank = r6.getRank();
            ShortcutInfoCompat r24 = this.mInfo;
            r24.mExtras = r6.getExtras();
            return;
        L5:
            ShortcutInfoCompat r25 = this.mInfo;
            if (r6.isEnabled() == false) goto L8;
            int r26 = 0;
        L9:
            r25.mDisabledReason = r26;
            goto L10
        L8:
            r26 = 3;
            goto L9
        }

        public Builder(Context r2, String r3) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = r2;
            this.mInfo.mId = r3;
        }

        public Builder(ShortcutInfoCompat r4) {
            this.mInfo = new ShortcutInfoCompat();
            ShortcutInfoCompat r0 = this.mInfo;
            r0.mContext = r4.mContext;
            ShortcutInfoCompat r1 = this.mInfo;
            r1.mId = r4.mId;
            ShortcutInfoCompat r2 = this.mInfo;
            r2.mPackageName = r4.mPackageName;
            ShortcutInfoCompat r3 = this.mInfo;
            r3.mIntents = (Intent[]) Arrays.copyOf(r4.mIntents, r4.mIntents.length);
            ShortcutInfoCompat r5 = this.mInfo;
            r5.mActivity = r4.mActivity;
            ShortcutInfoCompat r6 = this.mInfo;
            r6.mLabel = r4.mLabel;
            ShortcutInfoCompat r7 = this.mInfo;
            r7.mLongLabel = r4.mLongLabel;
            ShortcutInfoCompat r8 = this.mInfo;
            r8.mDisabledMessage = r4.mDisabledMessage;
            ShortcutInfoCompat r9 = this.mInfo;
            r9.mDisabledReason = r4.mDisabledReason;
            ShortcutInfoCompat r10 = this.mInfo;
            r10.mIcon = r4.mIcon;
            ShortcutInfoCompat r11 = this.mInfo;
            r11.mIsAlwaysBadged = r4.mIsAlwaysBadged;
            ShortcutInfoCompat r12 = this.mInfo;
            r12.mUser = r4.mUser;
            ShortcutInfoCompat r13 = this.mInfo;
            r13.mLastChangedTimestamp = r4.mLastChangedTimestamp;
            ShortcutInfoCompat r14 = this.mInfo;
            r14.mIsCached = r4.mIsCached;
            ShortcutInfoCompat r15 = this.mInfo;
            r15.mIsDynamic = r4.mIsDynamic;
            ShortcutInfoCompat r16 = this.mInfo;
            r16.mIsPinned = r4.mIsPinned;
            ShortcutInfoCompat r17 = this.mInfo;
            r17.mIsDeclaredInManifest = r4.mIsDeclaredInManifest;
            ShortcutInfoCompat r18 = this.mInfo;
            r18.mIsImmutable = r4.mIsImmutable;
            ShortcutInfoCompat r19 = this.mInfo;
            r19.mIsEnabled = r4.mIsEnabled;
            ShortcutInfoCompat r20 = this.mInfo;
            r20.mLocusId = r4.mLocusId;
            ShortcutInfoCompat r21 = this.mInfo;
            r21.mIsLongLived = r4.mIsLongLived;
            ShortcutInfoCompat r22 = this.mInfo;
            r22.mHasKeyFieldsOnly = r4.mHasKeyFieldsOnly;
            ShortcutInfoCompat r23 = this.mInfo;
            r23.mRank = r4.mRank;
            if (r4.mPersons == null) goto L6;
            ShortcutInfoCompat r24 = this.mInfo;
            r24.mPersons = (Person[]) Arrays.copyOf(r4.mPersons, r4.mPersons.length);
        L6:
            if (r4.mCategories == null) goto L9;
            ShortcutInfoCompat r25 = this.mInfo;
            r25.mCategories = new HashSet(r4.mCategories);
        L9:
            if (r4.mExtras == null) goto L11;
            ShortcutInfoCompat r26 = this.mInfo;
            r26.mExtras = r4.mExtras;
        L11:
            ShortcutInfoCompat r27 = this.mInfo;
            r27.mExcludedSurfaces = r4.mExcludedSurfaces;
        }

        public Builder addCapabilityBinding(String r2) {
            if (this.mCapabilityBindings != null) goto L5;
            this.mCapabilityBindings = new HashSet();
        L5:
            this.mCapabilityBindings.add(r2);
            return this;
        }

        public Builder addCapabilityBinding(String r3, String r4, List<String> r5) {
            addCapabilityBinding(r3);
            if (r5.isEmpty() == false) goto L5;
        L11:
            return this;
        L5:
            if (this.mCapabilityBindingParams != null) goto L8;
            this.mCapabilityBindingParams = new HashMap();
        L8:
            if (this.mCapabilityBindingParams.get(r3) != null) goto L10;
            this.mCapabilityBindingParams.put(r3, new HashMap());
        L10:
            this.mCapabilityBindingParams.get(r3).put(r4, r5);
            goto L11
        }

        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel) == true) goto L48;
            if (this.mInfo.mIntents == null) goto L46;
            if (this.mInfo.mIntents.length == 0) goto L46;
            if (this.mIsConversation == false) goto L15;
            if (this.mInfo.mLocusId != null) goto L13;
            this.mInfo.mLocusId = new LocusIdCompat(this.mInfo.mId);
        L13:
            this.mInfo.mIsLongLived = true;
        L15:
            if (this.mCapabilityBindings == null) goto L21;
            if (this.mInfo.mCategories != null) goto L19;
            this.mInfo.mCategories = new HashSet();
        L19:
            this.mInfo.mCategories.addAll(this.mCapabilityBindings);
        L21:
            if (this.mCapabilityBindingParams == null) goto L38;
            if (this.mInfo.mExtras != null) goto L25;
            this.mInfo.mExtras = new PersistableBundle();
        L25:
            Iterator<String> r0 = this.mCapabilityBindingParams.keySet().iterator();
        L27:
            if (r0.hasNext() == false) goto L38;
            String r1 = r0.next();
            Map<String, List<String>> r2 = this.mCapabilityBindingParams.get(r1);
            this.mInfo.mExtras.putStringArray(r1, (String[]) r2.keySet().toArray(new String[0]));
            Iterator<String> r4 = r2.keySet().iterator();
        L30:
            if (r4.hasNext() == false) goto L27;
            String r6 = r4.next();
            List<String> r7 = r2.get(r6);
            PersistableBundle r8 = this.mInfo.mExtras;
            String r9 = r1 + "/" + r6;
            String[] r10 = new String[0];
            if (r7 == null) goto L35;
            r10 = (String[]) r7.toArray(r10);
        L35:
            r8.putStringArray(r9, r10);
        L38:
            if (this.mSliceUri == null) goto L44;
            if (this.mInfo.mExtras != null) goto L42;
            this.mInfo.mExtras = new PersistableBundle();
        L42:
            this.mInfo.mExtras.putString(ShortcutInfoCompat.EXTRA_SLICE_URI, UriCompat.toSafeString(this.mSliceUri));
        L44:
            return this.mInfo;
        L46:
            throw new IllegalArgumentException("Shortcut must have an intent");
        L48:
            throw new IllegalArgumentException("Shortcut must have a non-empty label");
        }

        public Builder setActivity(ComponentName r2) {
            this.mInfo.mActivity = r2;
            return this;
        }

        public Builder setAlwaysBadged() {
            this.mInfo.mIsAlwaysBadged = true;
            return this;
        }

        public Builder setCategories(Set<String> r2) {
            this.mInfo.mCategories = r2;
            return this;
        }

        public Builder setDisabledMessage(CharSequence r2) {
            this.mInfo.mDisabledMessage = r2;
            return this;
        }

        public Builder setExcludedFromSurfaces(int r2) {
            this.mInfo.mExcludedSurfaces = r2;
            return this;
        }

        public Builder setExtras(PersistableBundle r2) {
            this.mInfo.mExtras = r2;
            return this;
        }

        public Builder setIcon(IconCompat r2) {
            this.mInfo.mIcon = r2;
            return this;
        }

        public Builder setIntent(Intent r3) {
            return setIntents(new Intent[]{r3});
        }

        public Builder setIntents(Intent[] r2) {
            this.mInfo.mIntents = r2;
            return this;
        }

        public Builder setIsConversation() {
            this.mIsConversation = true;
            return this;
        }

        public Builder setLocusId(LocusIdCompat r2) {
            this.mInfo.mLocusId = r2;
            return this;
        }

        public Builder setLongLabel(CharSequence r2) {
            this.mInfo.mLongLabel = r2;
            return this;
        }

        @Deprecated
        public Builder setLongLived() {
            this.mInfo.mIsLongLived = true;
            return this;
        }

        public Builder setLongLived(boolean r2) {
            this.mInfo.mIsLongLived = r2;
            return this;
        }

        public Builder setPerson(Person r3) {
            return setPersons(new Person[]{r3});
        }

        public Builder setPersons(Person[] r2) {
            this.mInfo.mPersons = r2;
            return this;
        }

        public Builder setRank(int r2) {
            this.mInfo.mRank = r2;
            return this;
        }

        public Builder setShortLabel(CharSequence r2) {
            this.mInfo.mLabel = r2;
            return this;
        }

        public Builder setSliceUri(Uri r1) {
            this.mSliceUri = r1;
            return this;
        }

        public Builder setTransientExtras(Bundle r3) {
            ShortcutInfoCompat r0 = this.mInfo;
            r0.mTransientExtras = (Bundle) Preconditions.checkNotNull(r3);
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Surface {
    }

    ShortcutInfoCompat() {
        this.mIsEnabled = true;
    }

    private PersistableBundle buildLegacyExtrasBundle() {
        if (this.mExtras != null) goto L6;
        this.mExtras = new PersistableBundle();
    L6:
        if (this.mPersons == null) goto L14;
        if (this.mPersons.length <= 0) goto L14;
        this.mExtras.putInt(EXTRA_PERSON_COUNT, this.mPersons.length);
        int r0 = 0;
    L11:
        if (r0 >= this.mPersons.length) goto L14;
        this.mExtras.putPersistableBundle(EXTRA_PERSON_ + (r0 + 1), this.mPersons[r0].toPersistableBundle());
        r0 = r0 + 1;
    L14:
        if (this.mLocusId == null) goto L16;
        this.mExtras.putString(EXTRA_LOCUS_ID, this.mLocusId.getId());
    L16:
        this.mExtras.putBoolean(EXTRA_LONG_LIVED, this.mIsLongLived);
        return this.mExtras;
    }

    static List<ShortcutInfoCompat> fromShortcuts(Context r4, List<ShortcutInfo> r5) {
        ArrayList r0 = new ArrayList(r5.size());
        Iterator<ShortcutInfo> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(new Builder(r4, r1.next()).build());
        goto L4
    L6:
        return r0;
    }

    static LocusIdCompat getLocusId(ShortcutInfo r2) {
        if (Build.VERSION.SDK_INT < 29) goto L11;
        if (r2.getLocusId() != null) goto L9;
        return null;
    L9:
        return LocusIdCompat.toLocusIdCompat(r2.getLocusId());
    L11:
        return getLocusIdFromExtra(r2.getExtras());
    }

    private static LocusIdCompat getLocusIdFromExtra(PersistableBundle r2) {
        if (r2 != null) goto L5;
        return null;
    L5:
        String r1 = r2.getString(EXTRA_LOCUS_ID);
        if (r1 != null) goto L9;
        return null;
    L9:
        return new LocusIdCompat(r1);
    }

    static boolean getLongLivedFromExtra(PersistableBundle r2) {
        if (r2 != null) goto L4;
        return false;
    L4:
        if (r2.containsKey(EXTRA_LONG_LIVED) == true) goto L7;
        return false;
    L7:
        return r2.getBoolean(EXTRA_LONG_LIVED);
    }

    static Person[] getPersonsFromExtra(PersistableBundle r5) {
        if (r5 != null) goto L4;
        return null;
    L4:
        if (r5.containsKey(EXTRA_PERSON_COUNT) == false) goto L13;
        int r0 = r5.getInt(EXTRA_PERSON_COUNT);
        Person[] r1 = new Person[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        r1[r2] = Person.fromPersistableBundle(r5.getPersistableBundle(EXTRA_PERSON_ + (r2 + 1)));
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    L13:
        return null;
    }

    Intent addToIntent(Intent r4) {
        r4.putExtra("android.intent.extra.shortcut.INTENT", this.mIntents[this.mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon == null) goto L14;
        Drawable r0 = null;
        if (this.mIsAlwaysBadged == false) goto L13;
        PackageManager r1 = this.mContext.getPackageManager();
        if (this.mActivity != null) goto L15;
    L11:
        if (r0 != null) goto L13;
        r0 = this.mContext.getApplicationInfo().loadIcon(r1);
        goto L13
    L15:
        r0 = r1.getActivityIcon(this.mActivity);     // Catch: PackageManager.NameNotFoundException -> L10
    L13:
        this.mIcon.addToShortcutIntent(r4, r0, this.mContext);
    L14:
        return r4;
    }

    public ComponentName getActivity() {
        return this.mActivity;
    }

    public Set<String> getCategories() {
        return this.mCategories;
    }

    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }

    public int getDisabledReason() {
        return this.mDisabledReason;
    }

    public int getExcludedFromSurfaces() {
        return this.mExcludedSurfaces;
    }

    public PersistableBundle getExtras() {
        return this.mExtras;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getId() {
        return this.mId;
    }

    public Intent getIntent() {
        return this.mIntents[this.mIntents.length - 1];
    }

    public Intent[] getIntents() {
        return (Intent[]) Arrays.copyOf(this.mIntents, this.mIntents.length);
    }

    public long getLastChangedTimestamp() {
        return this.mLastChangedTimestamp;
    }

    public LocusIdCompat getLocusId() {
        return this.mLocusId;
    }

    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }

    public String getPackage() {
        return this.mPackageName;
    }

    public int getRank() {
        return this.mRank;
    }

    public CharSequence getShortLabel() {
        return this.mLabel;
    }

    public Bundle getTransientExtras() {
        return this.mTransientExtras;
    }

    public UserHandle getUserHandle() {
        return this.mUser;
    }

    public boolean hasKeyFieldsOnly() {
        return this.mHasKeyFieldsOnly;
    }

    public boolean isCached() {
        return this.mIsCached;
    }

    public boolean isDeclaredInManifest() {
        return this.mIsDeclaredInManifest;
    }

    public boolean isDynamic() {
        return this.mIsDynamic;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public boolean isExcludedFromSurfaces(int r2) {
        if ((this.mExcludedSurfaces & r2) == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isImmutable() {
        return this.mIsImmutable;
    }

    public boolean isPinned() {
        return this.mIsPinned;
    }

    public ShortcutInfo toShortcutInfo() {
        ShortcutInfo.Builder r0 = new ShortcutInfo.Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        if (this.mIcon == null) goto L6;
        r0.setIcon(this.mIcon.toIcon(this.mContext));
    L6:
        if (TextUtils.isEmpty(this.mLongLabel) == true) goto L9;
        r0.setLongLabel(this.mLongLabel);
    L9:
        if (TextUtils.isEmpty(this.mDisabledMessage) == true) goto L12;
        r0.setDisabledMessage(this.mDisabledMessage);
    L12:
        if (this.mActivity == null) goto L15;
        r0.setActivity(this.mActivity);
    L15:
        if (this.mCategories == null) goto L17;
        r0.setCategories(this.mCategories);
    L17:
        r0.setRank(this.mRank);
        if (this.mExtras == null) goto L21;
        r0.setExtras(this.mExtras);
    L21:
        if (Build.VERSION.SDK_INT >= 29) goto L23;
        r0.setExtras(buildLegacyExtrasBundle());
    L37:
        return r0.build();
    L23:
        if (this.mPersons == null) goto L32;
        if (this.mPersons.length <= 0) goto L32;
        android.app.Person[] r1 = new android.app.Person[this.mPersons.length];
        int r2 = 0;
    L28:
        if (r2 >= r1.length) goto L30;
        r1[r2] = this.mPersons[r2].toAndroidPerson();
        r2 = r2 + 1;
        goto L28
    L30:
        r0.setPersons(r1);
    L32:
        if (this.mLocusId == null) goto L34;
        r0.setLocusId(this.mLocusId.toLocusId());
    L34:
        r0.setLongLived(this.mIsLongLived);
        goto L37
    }
}
