package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class SinglePeriodTimeline extends Timeline {
    private static final MediaItem MEDIA_ITEM = null;
    private static final Object UID = null;
    private final long elapsedRealtimeEpochOffsetMs;
    private final boolean isDynamic;
    private final boolean isSeekable;
    private final MediaItem.LiveConfiguration liveConfiguration;
    private final Object manifest;
    private final MediaItem mediaItem;
    private final long periodDurationUs;
    private final long presentationStartTimeMs;
    private final boolean suppressPositionProjection;
    private final long windowDefaultStartPositionUs;
    private final long windowDurationUs;
    private final long windowPositionInPeriodUs;
    private final long windowStartTimeMs;

    static {
        UID = new Object();
        MEDIA_ITEM = new MediaItem.Builder().setMediaId("SinglePeriodTimeline").setUri(Uri.EMPTY).build();
    }

    @Deprecated
    public SinglePeriodTimeline(long r22, long r24, long r26, long r28, long r30, long r32, long r34, boolean r36, boolean r37, Object r38, MediaItem r39, MediaItem.LiveConfiguration r40) {
        this(r22, r24, r26, r28, r30, r32, r34, r36, r37, false, r38, r39, r40);
    }

    public SinglePeriodTimeline(long r18, long r20, long r22, long r24, long r26, long r28, long r30, boolean r32, boolean r33, boolean r34, Object r35, MediaItem r36, MediaItem.LiveConfiguration r37) {
        this.presentationStartTimeMs = r18;
        this.windowStartTimeMs = r20;
        this.elapsedRealtimeEpochOffsetMs = r22;
        this.periodDurationUs = r24;
        this.windowDurationUs = r26;
        this.windowPositionInPeriodUs = r28;
        this.windowDefaultStartPositionUs = r30;
        this.isSeekable = r32;
        this.isDynamic = r33;
        this.suppressPositionProjection = r34;
        this.manifest = r35;
        this.mediaItem = (MediaItem) Assertions.checkNotNull(r36);
        this.liveConfiguration = r37;
    }

    @Deprecated
    public SinglePeriodTimeline(long r24, long r26, long r28, long r30, long r32, long r34, long r36, boolean r38, boolean r39, boolean r40, Object r41, Object r42) {
        MediaItem r21 = MEDIA_ITEM.buildUpon().setTag(r42).build();
        if (r40 == false) goto L5;
        MediaItem.LiveConfiguration r0 = MEDIA_ITEM.liveConfiguration;
    L6:
        this(r24, r26, r28, r30, r32, r34, r36, r38, r39, false, r41, r21, r0);
        return;
    L5:
        r0 = null;
        goto L6
    }

    public SinglePeriodTimeline(long r22, long r24, long r26, long r28, boolean r30, boolean r31, boolean r32, Object r33, MediaItem r34) {
        if (r32 == false) goto L4;
        MediaItem r0 = r34;
        MediaItem.LiveConfiguration r1 = r0.liveConfiguration;
    L5:
        MediaItem r19 = r0;
        this(C0180C.TIME_UNSET, C0180C.TIME_UNSET, C0180C.TIME_UNSET, r22, r24, r26, r28, r30, r31, false, r33, r19, r1);
        return;
    L4:
        r0 = r34;
        r1 = null;
        goto L5
    }

    @Deprecated
    public SinglePeriodTimeline(long r21, long r23, long r25, long r27, boolean r29, boolean r30, boolean r31, Object r32, Object r33) {
        this(C0180C.TIME_UNSET, C0180C.TIME_UNSET, C0180C.TIME_UNSET, r21, r23, r25, r27, r29, r30, r31, r32, r33);
    }

    public SinglePeriodTimeline(long r15, boolean r17, boolean r18, boolean r19, Object r20, MediaItem r21) {
        this(r15, r15, 0, 0, r17, r18, r19, r20, r21);
    }

    @Deprecated
    public SinglePeriodTimeline(long r15, boolean r17, boolean r18, boolean r19, Object r20, Object r21) {
        this(r15, r15, 0, 0, r17, r18, r19, r20, r21);
    }

    @Override // androidx.media3.common.Timeline
    public int getIndexOfPeriod(Object r2) {
        if (UID.equals(r2) == false) goto L5;
        return 0;
    L5:
        return -1;
    }

    @Override // androidx.media3.common.Timeline
    public Timeline.Period getPeriod(int r10, Timeline.Period r11, boolean r12) {
        Assertions.checkIndex(r10, 0, 1);
        if (r12 == false) goto L5;
        Object r0 = UID;
    L7:
        return r11.set(null, r0, 0, this.periodDurationUs, -this.windowPositionInPeriodUs);
    L5:
        r0 = null;
        goto L7
    }

    @Override // androidx.media3.common.Timeline
    public int getPeriodCount() {
        return 1;
    }

    @Override // androidx.media3.common.Timeline
    public Object getUidOfPeriod(int r3) {
        Assertions.checkIndex(r3, 0, 1);
        return UID;
    }

    @Override // androidx.media3.common.Timeline
    public Timeline.Window getWindow(int r26, Timeline.Window r27, long r28) {
        Assertions.checkIndex(r26, 0, 1);
        long r1 = this.windowDefaultStartPositionUs;
        if (this.isDynamic == true) goto L5;
    L15:
        long r17 = r1;
    L17:
        return r27.set(Timeline.Window.SINGLE_WINDOW_UID, this.mediaItem, this.manifest, this.presentationStartTimeMs, this.windowStartTimeMs, this.elapsedRealtimeEpochOffsetMs, this.isSeekable, this.isDynamic, this.liveConfiguration, r17, this.windowDurationUs, 0, 0, this.windowPositionInPeriodUs);
    L5:
        if (this.suppressPositionProjection == true) goto L15;
        if (r28 == 0) goto L15;
        if (this.windowDurationUs != C0180C.TIME_UNSET) goto L11;
        r17 = -9223372036854775807L;
        goto L17
    L11:
        long r2 = r1 + r28;
        if (r2 <= this.windowDurationUs) goto L14;
        r17 = -9223372036854775807L;
        goto L17
    L14:
        r17 = r2;
        goto L17
    }

    @Override // androidx.media3.common.Timeline
    public int getWindowCount() {
        return 1;
    }
}
