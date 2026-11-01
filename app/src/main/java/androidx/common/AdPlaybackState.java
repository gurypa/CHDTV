package androidx.media3.common;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class AdPlaybackState {
    public static final int AD_STATE_AVAILABLE = 1;
    public static final int AD_STATE_ERROR = 4;
    public static final int AD_STATE_PLAYED = 3;
    public static final int AD_STATE_SKIPPED = 2;
    public static final int AD_STATE_UNAVAILABLE = 0;
    private static final String FIELD_AD_GROUPS = null;
    private static final String FIELD_AD_RESUME_POSITION_US = null;
    private static final String FIELD_CONTENT_DURATION_US = null;
    private static final String FIELD_REMOVED_AD_GROUP_COUNT = null;
    public static final AdPlaybackState NONE = null;
    private static final AdGroup REMOVED_AD_GROUP = null;
    public final int adGroupCount;
    private final AdGroup[] adGroups;
    public final long adResumePositionUs;
    public final Object adsId;
    public final long contentDurationUs;
    public final int removedAdGroupCount;

    /* JADX INFO: renamed from: androidx.media3.common.AdPlaybackState$1 */
    static /* synthetic */ class C01781 {
    }

    public static final class AdGroup {
        private static final String FIELD_CONTENT_RESUME_OFFSET_US = null;
        private static final String FIELD_COUNT = null;
        private static final String FIELD_DURATIONS_US = null;
        private static final String FIELD_IS_SERVER_SIDE_INSERTED = null;
        static final String FIELD_MEDIA_ITEMS = null;
        private static final String FIELD_ORIGINAL_COUNT = null;
        private static final String FIELD_STATES = null;
        private static final String FIELD_TIME_US = null;
        private static final String FIELD_URIS = null;
        public final long contentResumeOffsetUs;
        public final int count;
        public final long[] durationsUs;
        public final boolean isServerSideInserted;
        public final MediaItem[] mediaItems;
        public final int originalCount;
        public final int[] states;
        public final long timeUs;

        @Deprecated
        public final Uri[] uris;

        static {
            FIELD_TIME_US = Util.intToStringMaxRadix(0);
            FIELD_COUNT = Util.intToStringMaxRadix(1);
            FIELD_URIS = Util.intToStringMaxRadix(2);
            FIELD_STATES = Util.intToStringMaxRadix(3);
            FIELD_DURATIONS_US = Util.intToStringMaxRadix(4);
            FIELD_CONTENT_RESUME_OFFSET_US = Util.intToStringMaxRadix(5);
            FIELD_IS_SERVER_SIDE_INSERTED = Util.intToStringMaxRadix(6);
            FIELD_ORIGINAL_COUNT = Util.intToStringMaxRadix(7);
            FIELD_MEDIA_ITEMS = Util.intToStringMaxRadix(8);
        }

        public AdGroup(long r13) {
            this(r13, -1, -1, new int[0], new MediaItem[0], new long[0], 0, false);
        }

        private AdGroup(long r4, int r6, int r7, int[] r8, MediaItem[] r9, long[] r10, long r11, boolean r13) {
            if (r8.length != r9.length) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.timeUs = r4;
            this.count = r6;
            this.originalCount = r7;
            this.states = r8;
            this.mediaItems = r9;
            this.durationsUs = r10;
            this.contentResumeOffsetUs = r11;
            this.isServerSideInserted = r13;
            this.uris = new Uri[r9.length];
            int r1 = 0;
        L8:
            if (r1 >= this.uris.length) goto L14;
            Uri[] r2 = this.uris;
            if (r9[r1] != null) goto L12;
            Uri r3 = null;
        L13:
            r2[r1] = r3;
            r1 = r1 + 1;
            goto L8
        L12:
            r3 = ((MediaItem.LocalConfiguration) Assertions.checkNotNull(r9[r1].localConfiguration)).uri;
            goto L13
        L14:
            return;
        L5:
            r0 = false;
            goto L6
        }

        /* synthetic */ AdGroup(long r1, int r3, int r4, int[] r5, MediaItem[] r6, long[] r7, long r8, boolean r10, C01781 r11) {
            this(r1, r3, r4, r5, r6, r7, r8, r10);
        }

        static /* synthetic */ boolean access$000(AdGroup r1) {
            return r1.isLivePostrollPlaceholder();
        }

        private static long[] copyDurationsUsWithSpaceForAdCount(long[] r4, int r5) {
            int r0 = r4.length;
            int r1 = Math.max(r5, r0);
            long[] r6 = Arrays.copyOf(r4, r1);
            Arrays.fill(r6, r0, r1, C0180C.TIME_UNSET);
            return r6;
        }

        private static int[] copyStatesWithSpaceForAdCount(int[] r3, int r4) {
            int r0 = r3.length;
            int r1 = Math.max(r4, r0);
            int[] r5 = Arrays.copyOf(r3, r1);
            Arrays.fill(r5, r0, r1, 0);
            return r5;
        }

        public static AdGroup fromBundle(Bundle r15) {
            long r2 = r15.getLong(FIELD_TIME_US);
            int r4 = r15.getInt(FIELD_COUNT);
            int r5 = r15.getInt(FIELD_ORIGINAL_COUNT);
            ArrayList r0 = r15.getParcelableArrayList(FIELD_URIS);
            ArrayList r12 = r15.getParcelableArrayList(FIELD_MEDIA_ITEMS);
            int[] r13 = r15.getIntArray(FIELD_STATES);
            long[] r14 = r15.getLongArray(FIELD_DURATIONS_US);
            long r9 = r15.getLong(FIELD_CONTENT_RESUME_OFFSET_US);
            boolean r11 = r15.getBoolean(FIELD_IS_SERVER_SIDE_INSERTED);
            if (r13 != null) goto L5;
            int[] r7 = new int[0];
        L6:
            int[] r6 = r7;
            MediaItem[] r8 = getMediaItemsFromBundleArrays(r12, r0);
            if (r14 != null) goto L9;
            long[] r10 = new long[0];
        L11:
            return new AdGroup(r2, r4, r5, r6, r8, r10, r9, r11);
        L9:
            r10 = r14;
            goto L11
        L5:
            r7 = r13;
            goto L6
        }

        private ArrayList<Bundle> getMediaItemsArrayBundles() {
            ArrayList<Bundle> r0 = new ArrayList();
            MediaItem[] r1 = this.mediaItems;
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L9;
            MediaItem r4 = r1[r3];
            if (r4 != null) goto L7;
            Bundle r5 = null;
        L8:
            r0.add(r5);
            r3 = r3 + 1;
            goto L3
        L7:
            r5 = r4.toBundleIncludeLocalConfiguration();
            goto L8
        L9:
            return r0;
        }

        private static MediaItem[] getMediaItemsFromBundleArrays(ArrayList<Bundle> r5, ArrayList<Uri> r6) {
            if (r5 == null) goto L13;
            MediaItem[] r1 = new MediaItem[r5.size()];
            int r2 = 0;
        L6:
            if (r2 >= r5.size()) goto L12;
            Bundle r3 = r5.get(r2);
            if (r3 != null) goto L10;
            MediaItem r4 = null;
        L11:
            r1[r2] = r4;
            r2 = r2 + 1;
            goto L6
        L10:
            r4 = MediaItem.fromBundle(r3);
            goto L11
        L12:
            return r1;
        L13:
            if (r6 == null) goto L24;
            MediaItem[] r7 = new MediaItem[r6.size()];
            int r8 = 0;
        L16:
            if (r8 >= r6.size()) goto L22;
            Uri r9 = r6.get(r8);
            if (r9 != null) goto L20;
            MediaItem r10 = null;
        L21:
            r7[r8] = r10;
            r8 = r8 + 1;
            goto L16
        L20:
            r10 = MediaItem.fromUri(r9);
            goto L21
        L22:
            return r7;
        L24:
            return new MediaItem[0];
        }

        private boolean isLivePostrollPlaceholder() {
            if (this.isServerSideInserted == true) goto L5;
        L9:
            return false;
        L5:
            if (this.timeUs != Long.MIN_VALUE) goto L9;
            if (this.count != (-1)) goto L9;
            return true;
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if (r8 != null) goto L8;
        L29:
            return false;
        L8:
            if (getClass() != r8.getClass()) goto L29;
            AdGroup r2 = (AdGroup) r8;
            if (this.timeUs != r2.timeUs) goto L28;
            if (this.count != r2.count) goto L28;
            if (this.originalCount != r2.originalCount) goto L28;
            if (Arrays.equals(this.mediaItems, r2.mediaItems) == false) goto L28;
            if (Arrays.equals(this.states, r2.states) == false) goto L28;
            if (Arrays.equals(this.durationsUs, r2.durationsUs) == false) goto L28;
            if (this.contentResumeOffsetUs != r2.contentResumeOffsetUs) goto L28;
            if (this.isServerSideInserted != r2.isServerSideInserted) goto L28;
            return true;
        L28:
            return false;
        }

        public int getFirstAdIndexToPlay() {
            return getNextAdIndexToPlay(-1);
        }

        public int getNextAdIndexToPlay(int r4) {
            int r0 = r4 + 1;
        L4:
            if (r0 >= this.states.length) goto L13;
            if (this.isServerSideInserted == true) goto L13;
            if (this.states[r0] == 0) goto L13;
            if (this.states[r0] == 1) goto L13;
            r0 = r0 + 1;
        L13:
            return r0;
        }

        public boolean hasUnplayedAds() {
            if (this.count != (-1)) goto L5;
            return true;
        L5:
            int r0 = 0;
        L7:
            if (r0 >= this.count) goto L15;
            if (this.states[r0] == 0) goto L14;
            if (this.states[r0] == 1) goto L14;
            r0 = r0 + 1;
        L14:
            return true;
        L15:
            return false;
        }

        public int hashCode() {
            return (((((((((((((this.count * 31) + this.originalCount) * 31) + ((int) (this.timeUs ^ (this.timeUs >>> 32)))) * 31) + Arrays.hashCode(this.mediaItems)) * 31) + Arrays.hashCode(this.states)) * 31) + Arrays.hashCode(this.durationsUs)) * 31) + ((int) (this.contentResumeOffsetUs ^ (this.contentResumeOffsetUs >>> 32)))) * 31) + (this.isServerSideInserted ? 1 : 0);
        }

        public boolean shouldPlayAdGroup() {
            if (this.count != (-1)) goto L5;
        L8:
            return true;
        L5:
            if (getFirstAdIndexToPlay() < this.count) goto L8;
            return false;
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putLong(FIELD_TIME_US, this.timeUs);
            r0.putInt(FIELD_COUNT, this.count);
            r0.putInt(FIELD_ORIGINAL_COUNT, this.originalCount);
            r0.putParcelableArrayList(FIELD_URIS, new ArrayList(Arrays.asList(this.uris)));
            r0.putParcelableArrayList(FIELD_MEDIA_ITEMS, getMediaItemsArrayBundles());
            r0.putIntArray(FIELD_STATES, this.states);
            r0.putLongArray(FIELD_DURATIONS_US, this.durationsUs);
            r0.putLong(FIELD_CONTENT_RESUME_OFFSET_US, this.contentResumeOffsetUs);
            r0.putBoolean(FIELD_IS_SERVER_SIDE_INSERTED, this.isServerSideInserted);
            return r0;
        }

        public AdGroup withAdCount(int r13) {
            int[] r6 = copyStatesWithSpaceForAdCount(this.states, r13);
            long[] r8 = copyDurationsUsWithSpaceForAdCount(this.durationsUs, r13);
            MediaItem[] r7 = (MediaItem[]) Arrays.copyOf(this.mediaItems, r13);
            return new AdGroup(this.timeUs, r13, this.originalCount, r6, r7, r8, this.contentResumeOffsetUs, this.isServerSideInserted);
        }

        public AdGroup withAdDurationsUs(long[] r12) {
            if (r12.length >= this.mediaItems.length) goto L6;
            long[] r7 = copyDurationsUsWithSpaceForAdCount(r12, this.mediaItems.length);
        L12:
            return new AdGroup(this.timeUs, this.count, this.originalCount, this.states, this.mediaItems, r7, this.contentResumeOffsetUs, this.isServerSideInserted);
        L6:
            if (this.count != (-1)) goto L8;
        L10:
            r7 = r12;
            goto L12
        L8:
            if (r12.length <= this.mediaItems.length) goto L10;
            r7 = Arrays.copyOf(r12, this.mediaItems.length);
            goto L12
        }

        public AdGroup withAdMediaItem(MediaItem r14, int r15) {
            int[] r7 = copyStatesWithSpaceForAdCount(this.states, r15 + 1);
            if (this.durationsUs.length != r7.length) goto L5;
            long[] r9 = this.durationsUs;
        L6:
            MediaItem[] r8 = (MediaItem[]) Arrays.copyOf(this.mediaItems, r7.length);
            r8[r15] = r14;
            r7[r15] = 1;
            return new AdGroup(this.timeUs, this.count, this.originalCount, r7, r8, r9, this.contentResumeOffsetUs, this.isServerSideInserted);
        L5:
            r9 = copyDurationsUsWithSpaceForAdCount(this.durationsUs, r7.length);
            goto L6
        }

        public AdGroup withAdState(int r19, int r20) {
            boolean r5 = false;
            if (this.count != (-1)) goto L5;
        L8:
            boolean r3 = true;
        L9:
            Assertions.checkArgument(r3);
            int[] r12 = copyStatesWithSpaceForAdCount(this.states, r20 + 1);
            if (r12[r20] != 0) goto L12;
        L15:
            r5 = true;
        L16:
            Assertions.checkArgument(r5);
            if (this.durationsUs.length != r12.length) goto L19;
            long[] r14 = this.durationsUs;
        L21:
            if (this.mediaItems.length != r12.length) goto L23;
            MediaItem[] r13 = this.mediaItems;
        L24:
            r12[r20] = r19;
            return new AdGroup(this.timeUs, this.count, this.originalCount, r12, r13, r14, this.contentResumeOffsetUs, this.isServerSideInserted);
        L23:
            r13 = (MediaItem[]) Arrays.copyOf(this.mediaItems, r12.length);
            goto L24
        L19:
            r14 = copyDurationsUsWithSpaceForAdCount(this.durationsUs, r12.length);
            goto L21
        L12:
            if (r12[r20] == 1) goto L15;
            if (r12[r20] != r19) goto L16;
        L5:
            if (r20 < this.count) goto L8;
            r3 = false;
            goto L9
        }

        @Deprecated
        public AdGroup withAdUri(Uri r2, int r3) {
            return withAdMediaItem(MediaItem.fromUri(r2), r3);
        }

        public AdGroup withAllAdsReset() {
            if (this.count != (-1)) goto L5;
            return this;
        L5:
            int r4 = this.states.length;
            int[] r6 = Arrays.copyOf(this.states, r4);
            int r0 = 0;
        L6:
            if (r0 >= r4) goto L20;
            if (r6[r0] == 3) goto L14;
            if (r6[r0] == 2) goto L14;
            if (r6[r0] == 4) goto L14;
        L18:
            r0 = r0 + 1;
        L14:
            if (this.mediaItems[r0] != null) goto L16;
            int r1 = 0;
        L17:
            r6[r0] = r1;
            goto L18
        L16:
            r1 = 1;
            goto L17
        L20:
            return new AdGroup(this.timeUs, r4, this.originalCount, r6, this.mediaItems, this.durationsUs, this.contentResumeOffsetUs, this.isServerSideInserted);
        }

        public AdGroup withAllAdsSkipped() {
            if (this.count == (-1)) goto L5;
            int r4 = this.states.length;
            int[] r6 = Arrays.copyOf(this.states, r4);
            int r0 = 0;
        L7:
            if (r0 >= r4) goto L15;
            if (r6[r0] != 1) goto L11;
        L12:
            r6[r0] = 2;
        L13:
            r0 = r0 + 1;
            goto L7
        L11:
            if (r6[r0] != 0) goto L13;
        L15:
            return new AdGroup(this.timeUs, r4, this.originalCount, r6, this.mediaItems, this.durationsUs, this.contentResumeOffsetUs, this.isServerSideInserted);
        L5:
            return new AdGroup(this.timeUs, 0, this.originalCount, new int[0], new MediaItem[0], new long[0], this.contentResumeOffsetUs, this.isServerSideInserted);
        }

        public AdGroup withContentResumeOffsetUs(long r12) {
            return new AdGroup(this.timeUs, this.count, this.originalCount, this.states, this.mediaItems, this.durationsUs, r12, this.isServerSideInserted);
        }

        public AdGroup withIsServerSideInserted(boolean r12) {
            return new AdGroup(this.timeUs, this.count, this.originalCount, this.states, this.mediaItems, this.durationsUs, this.contentResumeOffsetUs, r12);
        }

        public AdGroup withLastAdRemoved() {
            int r4 = this.states.length - 1;
            int[] r6 = Arrays.copyOf(this.states, r4);
            MediaItem[] r7 = (MediaItem[]) Arrays.copyOf(this.mediaItems, r4);
            long[] r0 = this.durationsUs;
            if (this.durationsUs.length <= r4) goto L5;
            long[] r8 = Arrays.copyOf(this.durationsUs, r4);
        L7:
            return new AdGroup(this.timeUs, r4, this.originalCount, r6, r7, r8, Util.sum(r8), this.isServerSideInserted);
        L5:
            r8 = r0;
            goto L7
        }

        public AdGroup withOriginalAdCount(int r12) {
            return new AdGroup(this.timeUs, this.count, r12, this.states, this.mediaItems, this.durationsUs, this.contentResumeOffsetUs, this.isServerSideInserted);
        }

        public AdGroup withTimeUs(long r12) {
            return new AdGroup(r12, this.count, this.originalCount, this.states, this.mediaItems, this.durationsUs, this.contentResumeOffsetUs, this.isServerSideInserted);
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdState {
    }

    static {
        NONE = new AdPlaybackState(null, new AdGroup[0], 0, C0180C.TIME_UNSET, 0);
        REMOVED_AD_GROUP = new AdGroup(0).withAdCount(0);
        FIELD_AD_GROUPS = Util.intToStringMaxRadix(1);
        FIELD_AD_RESUME_POSITION_US = Util.intToStringMaxRadix(2);
        FIELD_CONTENT_DURATION_US = Util.intToStringMaxRadix(3);
        FIELD_REMOVED_AD_GROUP_COUNT = Util.intToStringMaxRadix(4);
    }

    public AdPlaybackState(Object r9, long... r10) {
        this(r9, createEmptyAdGroups(r10), 0, C0180C.TIME_UNSET, 0);
    }

    private AdPlaybackState(Object r2, AdGroup[] r3, long r4, long r6, int r8) {
        this.adsId = r2;
        this.adResumePositionUs = r4;
        this.contentDurationUs = r6;
        this.adGroupCount = r3.length + r8;
        this.adGroups = r3;
        this.removedAdGroupCount = r8;
    }

    private static AdGroup[] createEmptyAdGroups(long[] r5) {
        AdGroup[] r0 = new AdGroup[r5.length];
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L6;
        r0[r1] = new AdGroup(r5[r1]);
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static AdPlaybackState fromAdPlaybackState(Object r17, AdPlaybackState r18) {
        AdGroup[] r4 = new AdGroup[r18.adGroupCount - r18.removedAdGroupCount];
        int r1 = 0;
    L4:
        if (r1 >= r4.length) goto L7;
        AdGroup r2 = r18.adGroups[r1];
        r4[r1] = new AdGroup(r2.timeUs, r2.count, r2.originalCount, Arrays.copyOf(r2.states, r2.states.length), (MediaItem[]) Arrays.copyOf(r2.mediaItems, r2.mediaItems.length), Arrays.copyOf(r2.durationsUs, r2.durationsUs.length), r2.contentResumeOffsetUs, r2.isServerSideInserted, null);
        r1 = r1 + 1;
        goto L4
    L7:
        return new AdPlaybackState(r17, r4, r18.adResumePositionUs, r18.contentDurationUs, r18.removedAdGroupCount);
    }

    public static AdPlaybackState fromBundle(Bundle r11) {
        ArrayList r0 = r11.getParcelableArrayList(FIELD_AD_GROUPS);
        if (r0 != null) goto L5;
        AdGroup[] r5 = new AdGroup[0];
    L11:
        return new AdPlaybackState(null, r5, r11.getLong(FIELD_AD_RESUME_POSITION_US, NONE.adResumePositionUs), r11.getLong(FIELD_CONTENT_DURATION_US, NONE.contentDurationUs), r11.getInt(FIELD_REMOVED_AD_GROUP_COUNT, NONE.removedAdGroupCount));
    L5:
        AdGroup[] r1 = new AdGroup[r0.size()];
        int r2 = 0;
    L7:
        if (r2 >= r0.size()) goto L9;
        r1[r2] = AdGroup.fromBundle((Bundle) r0.get(r2));
        r2 = r2 + 1;
        goto L7
    L9:
        r5 = r1;
        goto L11
    }

    private boolean isPositionBeforeAdGroup(long r9, long r11, int r13) {
        if (r9 != Long.MIN_VALUE) goto L5;
        return false;
    L5:
        AdGroup r2 = getAdGroup(r13);
        long r4 = r2.timeUs;
        if (r4 != Long.MIN_VALUE) goto L18;
        if (r11 == C0180C.TIME_UNSET) goto L16;
        if (r2.isServerSideInserted == false) goto L14;
        if (r2.count == (-1)) goto L16;
    L14:
        if (r9 < r11) goto L16;
        return false;
    L16:
        return true;
    L18:
        if (r9 < r4) goto L20;
        return false;
    L20:
        return true;
    }

    public boolean endsWithLivePostrollPlaceHolder() {
        int r0 = this.adGroupCount - 1;
        if (r0 >= 0) goto L5;
    L7:
        return false;
    L5:
        if (isLivePostrollPlaceholder(r0) == false) goto L7;
        return true;
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L25:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L25;
        AdPlaybackState r2 = (AdPlaybackState) r8;
        if (Util.areEqual(this.adsId, r2.adsId) == false) goto L24;
        if (this.adGroupCount != r2.adGroupCount) goto L24;
        if (this.adResumePositionUs != r2.adResumePositionUs) goto L24;
        if (this.contentDurationUs != r2.contentDurationUs) goto L24;
        if (this.removedAdGroupCount != r2.removedAdGroupCount) goto L24;
        if (Arrays.equals(this.adGroups, r2.adGroups) == false) goto L24;
        return true;
    L24:
        return false;
    }

    public AdGroup getAdGroup(int r3) {
        if (r3 >= this.removedAdGroupCount) goto L6;
        return REMOVED_AD_GROUP;
    L6:
        return this.adGroups[r3 - this.removedAdGroupCount];
    }

    public int getAdGroupIndexAfterPositionUs(long r7, long r9) {
        if (r7 != Long.MIN_VALUE) goto L5;
    L23:
        return -1;
    L5:
        if (r9 != C0180C.TIME_UNSET) goto L7;
    L9:
        int r2 = this.removedAdGroupCount;
    L11:
        if (r2 >= this.adGroupCount) goto L20;
        if (getAdGroup(r2).timeUs == Long.MIN_VALUE) goto L17;
        if (getAdGroup(r2).timeUs > r7) goto L17;
    L18:
        r2 = r2 + 1;
    L17:
        if (getAdGroup(r2).shouldPlayAdGroup() == false) goto L18;
    L20:
        if (r2 < this.adGroupCount) goto L22;
        return -1;
    L22:
        return r2;
    L7:
        if (r7 < r9) goto L9;
        goto L9
    }

    public int getAdGroupIndexForPositionUs(long r8, long r10) {
        int r0 = this.adGroupCount - 1;
        int r6 = r0 - (isLivePostrollPlaceholder(r0) ? 1 : 0);
    L3:
        if (r6 < 0) goto L8;
        long r2 = r8;
        long r4 = r10;
        if (isPositionBeforeAdGroup(r2, r4, r6) == false) goto L8;
        r6 = r6 - 1;
        r8 = r2;
        r10 = r4;
    L8:
        if (r6 >= 0) goto L10;
    L12:
        return -1;
    L10:
        if (getAdGroup(r6).hasUnplayedAds() == false) goto L12;
        return r6;
    }

    public int hashCode() {
        int r1 = this.adGroupCount * 31;
        if (this.adsId != null) goto L5;
        int r2 = 0;
    L7:
        return ((((((((r1 + r2) * 31) + ((int) this.adResumePositionUs)) * 31) + ((int) this.contentDurationUs)) * 31) + this.removedAdGroupCount) * 31) + Arrays.hashCode(this.adGroups);
    L5:
        r2 = this.adsId.hashCode();
        goto L7
    }

    public boolean isAdInErrorState(int r5, int r6) {
        if (r5 < this.adGroupCount) goto L5;
        return false;
    L5:
        AdGroup r0 = getAdGroup(r5);
        if (r0.count != (-1)) goto L8;
    L14:
        return false;
    L8:
        if (r6 >= r0.count) goto L14;
        if (r0.states[r6] != 4) goto L15;
        return true;
    L15:
        return false;
    }

    public boolean isLivePostrollPlaceholder(int r3) {
        if (r3 == (this.adGroupCount - 1)) goto L5;
    L7:
        return false;
    L5:
        if (AdGroup.access$000(getAdGroup(r3)) == false) goto L7;
        return true;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        ArrayList<? extends Parcelable> r1 = new ArrayList();
        AdGroup[] r2 = this.adGroups;
        int r3 = r2.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L6;
        r1.add(r2[r4].toBundle());
        r4 = r4 + 1;
        goto L3
    L6:
        if (r1.isEmpty() == true) goto L9;
        r0.putParcelableArrayList(FIELD_AD_GROUPS, r1);
    L9:
        if (this.adResumePositionUs == NONE.adResumePositionUs) goto L12;
        r0.putLong(FIELD_AD_RESUME_POSITION_US, this.adResumePositionUs);
    L12:
        if (this.contentDurationUs == NONE.contentDurationUs) goto L15;
        r0.putLong(FIELD_CONTENT_DURATION_US, this.contentDurationUs);
    L15:
        if (this.removedAdGroupCount == NONE.removedAdGroupCount) goto L17;
        r0.putInt(FIELD_REMOVED_AD_GROUP_COUNT, this.removedAdGroupCount);
    L17:
        return r0;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        r0.append("AdPlaybackState(adsId=");
        r0.append(this.adsId);
        r0.append(", adResumePositionUs=");
        r0.append(this.adResumePositionUs);
        r0.append(", adGroups=[");
        int r1 = 0;
    L4:
        if (r1 >= this.adGroups.length) goto L24;
        r0.append("adGroup(timeUs=");
        r0.append(this.adGroups[r1].timeUs);
        r0.append(", ads=[");
        int r2 = 0;
    L7:
        if (r2 >= this.adGroups[r1].states.length) goto L20;
        r0.append("ad(state=");
        switch(this.adGroups[r1].states[r2]) {
            case 0: goto L15;
            case 1: goto L14;
            case 2: goto L13;
            case 3: goto L12;
            case 4: goto L11;
            default: goto L10;
        };
    L10:
        r0.append('?');
    L16:
        r0.append(", durationUs=");
        r0.append(this.adGroups[r1].durationsUs[r2]);
        r0.append(')');
        if (r2 >= (this.adGroups[r1].states.length - 1)) goto L19;
        r0.append(", ");
    L19:
        r2 = r2 + 1;
        goto L7
    L11:
        r0.append('!');
        goto L16
    L12:
        r0.append('P');
        goto L16
    L13:
        r0.append('S');
        goto L16
    L14:
        r0.append('R');
        goto L16
    L15:
        r0.append('_');
        goto L16
    L20:
        r0.append("])");
        if (r1 >= (this.adGroups.length - 1)) goto L23;
        r0.append(", ");
    L23:
        r1 = r1 + 1;
        goto L4
    L24:
        r0.append("])");
        return r0.toString();
    }

    public AdPlaybackState withAdCount(int r11, int r12) {
        if (r12 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0);
        int r1 = r11 - this.removedAdGroupCount;
        if (this.adGroups[r1].count != r12) goto L8;
        return this;
    L8:
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r1] = this.adGroups[r1].withAdCount(r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    L4:
        r0 = false;
        goto L5
    }

    public AdPlaybackState withAdDurationsUs(int r11, long... r12) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withAdDurationsUs(r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withAdDurationsUs(long[][] r10) {
        if (this.removedAdGroupCount != 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        AdGroup[] r3 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        int r1 = 0;
    L8:
        if (r1 >= this.adGroupCount) goto L11;
        r3[r1] = r3[r1].withAdDurationsUs(r10[r1]);
        r1 = r1 + 1;
        goto L8
    L11:
        return new AdPlaybackState(this.adsId, r3, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    L5:
        r0 = false;
        goto L6
    }

    public AdPlaybackState withAdGroupTimeUs(int r11, long r12) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = this.adGroups[r0].withTimeUs(r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withAdLoadError(int r11, int r12) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withAdState(4, r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withAdResumePositionUs(long r10) {
        if (this.adResumePositionUs != r10) goto L6;
        return this;
    L6:
        return new AdPlaybackState(this.adsId, this.adGroups, r10, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withAvailableAd(int r2, int r3) {
        return withAvailableAdMediaItem(r2, r3, MediaItem.fromUri(Uri.EMPTY));
    }

    public AdPlaybackState withAvailableAdMediaItem(int r11, int r12, MediaItem r13) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        if (r4[r0].isServerSideInserted == false) goto L5;
    L10:
        boolean r1 = true;
    L11:
        Assertions.checkState(r1);
        r4[r0] = r4[r0].withAdMediaItem(r13, r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    L5:
        if (r13.localConfiguration != null) goto L7;
    L9:
        r1 = false;
        goto L11
    L7:
        if (r13.localConfiguration.uri.equals(Uri.EMPTY) == true) goto L9;
        goto L9
    }

    @Deprecated
    public AdPlaybackState withAvailableAdUri(int r2, int r3, Uri r4) {
        return withAvailableAdMediaItem(r2, r3, MediaItem.fromUri(r4));
    }

    public AdPlaybackState withContentDurationUs(long r10) {
        if (this.contentDurationUs != r10) goto L6;
        return this;
    L6:
        return new AdPlaybackState(this.adsId, this.adGroups, this.adResumePositionUs, r10, this.removedAdGroupCount);
    }

    public AdPlaybackState withContentResumeOffsetUs(int r11, long r12) {
        int r0 = r11 - this.removedAdGroupCount;
        if (this.adGroups[r0].contentResumeOffsetUs != r12) goto L5;
        return this;
    L5:
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withContentResumeOffsetUs(r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withIsServerSideInserted(int r11, boolean r12) {
        int r0 = r11 - this.removedAdGroupCount;
        if (this.adGroups[r0].isServerSideInserted != r12) goto L5;
        return this;
    L5:
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withIsServerSideInserted(r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withLastAdRemoved(int r11) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withLastAdRemoved();
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withLivePostrollPlaceholderAppended() {
        return withNewAdGroup(this.adGroupCount, Long.MIN_VALUE).withIsServerSideInserted(this.adGroupCount, true);
    }

    public AdPlaybackState withNewAdGroup(int r12, long r13) {
        int r0 = r12 - this.removedAdGroupCount;
        AdGroup r1 = new AdGroup(r13);
        AdGroup[] r5 = (AdGroup[]) Util.nullSafeArrayAppend(this.adGroups, r1);
        System.arraycopy(r5, r0, r5, r0 + 1, this.adGroups.length - r0);
        r5[r0] = r1;
        return new AdPlaybackState(this.adsId, r5, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withOriginalAdCount(int r11, int r12) {
        int r0 = r11 - this.removedAdGroupCount;
        if (this.adGroups[r0].originalCount != r12) goto L5;
        return this;
    L5:
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withOriginalAdCount(r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withPlayedAd(int r11, int r12) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withAdState(3, r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withRemovedAdGroupCount(int r11) {
        if (this.removedAdGroupCount != r11) goto L6;
        return this;
    L6:
        if (r11 <= this.removedAdGroupCount) goto L8;
        boolean r0 = true;
    L9:
        Assertions.checkArgument(r0);
        AdGroup[] r4 = new AdGroup[this.adGroupCount - r11];
        System.arraycopy(this.adGroups, r11 - this.removedAdGroupCount, r4, 0, r4.length);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, r11);
    L8:
        r0 = false;
        goto L9
    }

    public AdPlaybackState withResetAdGroup(int r11) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withAllAdsReset();
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withSkippedAd(int r11, int r12) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withAdState(2, r12);
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }

    public AdPlaybackState withSkippedAdGroup(int r11) {
        int r0 = r11 - this.removedAdGroupCount;
        AdGroup[] r4 = (AdGroup[]) Util.nullSafeArrayCopy(this.adGroups, this.adGroups.length);
        r4[r0] = r4[r0].withAllAdsSkipped();
        return new AdPlaybackState(this.adsId, r4, this.adResumePositionUs, this.contentDurationUs, this.removedAdGroupCount);
    }
}
