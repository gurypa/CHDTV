package androidx.media3.exoplayer.source;

import androidx.core.os.EnvironmentCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.upstream.Allocator;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ClippingMediaSource extends WrappingMediaSource {
    private final boolean allowDynamicClippingUpdates;
    private IllegalClippingException clippingError;
    private ClippingTimeline clippingTimeline;
    private final boolean enableInitialDiscontinuity;
    private final long endUs;
    private final ArrayList<ClippingMediaPeriod> mediaPeriods;
    private long periodEndUs;
    private long periodStartUs;
    private final boolean relativeToDefaultPosition;
    private final long startUs;
    private final Timeline.Window window;

    private static final class ClippingTimeline extends ForwardingTimeline {
        private final long durationUs;
        private final long endUs;
        private final boolean isDynamic;
        private final long startUs;

        public ClippingTimeline(Timeline r14, long r15, long r17) throws IllegalClippingException {
            super(r14);
            boolean r3 = false;
            if (r14.getPeriodCount() != 1) goto L43;
            Timeline.Window r2 = r14.getWindow(0, new Timeline.Window());
            long r9 = Math.max(0, r15);
            if (r2.isPlaceholder == true) goto L14;
            if (r9 == 0) goto L14;
            if (r2.isSeekable == true) goto L14;
            throw new IllegalClippingException(1);
        L14:
            if (r17 != Long.MIN_VALUE) goto L16;
            long r5 = r2.durationUs;
        L18:
            if (r2.durationUs != C0180C.TIME_UNSET) goto L20;
        L27:
            this.startUs = r9;
            this.endUs = r5;
            if (r5 != C0180C.TIME_UNSET) goto L30;
            long r7 = -9223372036854775807L;
        L31:
            this.durationUs = r7;
            if (r2.isDynamic == true) goto L34;
        L40:
            this.isDynamic = r3;
            return;
        L34:
            if (r5 != C0180C.TIME_UNSET) goto L36;
        L39:
            r3 = true;
            goto L40
        L36:
            if (r2.durationUs == C0180C.TIME_UNSET) goto L40;
            if (r5 != r2.durationUs) goto L40;
        L30:
            r7 = r5 - r9;
            goto L31
        L20:
            if (r5 <= r2.durationUs) goto L23;
            r5 = r2.durationUs;
        L23:
            if (r9 <= r5) goto L27;
            throw new IllegalClippingException(2, r9, r5);
        L16:
            r5 = Math.max(0, r17);
            goto L18
        L43:
            throw new IllegalClippingException(0);
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r13, Timeline.Period r14, boolean r15) {
            this.timeline.getPeriod(0, r14, r15);
            long r10 = r14.getPositionInWindowUs() - this.startUs;
            long r0 = this.durationUs;
            long r2 = C0180C.TIME_UNSET;
            if (r0 == C0180C.TIME_UNSET) goto L6;
            r2 = this.durationUs - r10;
        L6:
            Object r5 = r14.f30id;
            Object r6 = r14.uid;
            return r14.set(r5, r6, 0, r2, r10);
        }

        @Override // androidx.media3.exoplayer.source.ForwardingTimeline, androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r7, Timeline.Window r8, long r9) {
            this.timeline.getWindow(0, r8, 0);
            r8.positionInFirstPeriodUs += this.startUs;
            r8.durationUs = this.durationUs;
            r8.isDynamic = this.isDynamic;
            if (r8.defaultPositionUs == C0180C.TIME_UNSET) goto L9;
            r8.defaultPositionUs = Math.max(r8.defaultPositionUs, this.startUs);
            if (this.endUs != C0180C.TIME_UNSET) goto L7;
            long r0 = r8.defaultPositionUs;
        L8:
            r8.defaultPositionUs = r0;
            r8.defaultPositionUs -= this.startUs;
            goto L9
        L7:
            r0 = Math.min(r8.defaultPositionUs, this.endUs);
        L9:
            long r1 = Util.usToMs(this.startUs);
            if (r8.presentationStartTimeMs == C0180C.TIME_UNSET) goto L13;
            r8.presentationStartTimeMs += r1;
        L13:
            if (r8.windowStartTimeMs == C0180C.TIME_UNSET) goto L15;
            r8.windowStartTimeMs += r1;
        L15:
            return r8;
        }
    }

    public static final class IllegalClippingException extends IOException {
        public static final int REASON_INVALID_PERIOD_COUNT = 0;
        public static final int REASON_NOT_SEEKABLE_TO_START = 1;
        public static final int REASON_START_EXCEEDS_END = 2;
        public final int reason;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface Reason {
        }

        public IllegalClippingException(int r7) {
            this(r7, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        }

        public IllegalClippingException(int r3, long r4, long r6) {
            super("Illegal clipping: " + getReasonDescription(r3, r4, r6));
            this.reason = r3;
        }

        private static String getReasonDescription(int r3, long r4, long r6) {
            switch(r3) {
                case 0: goto L15;
                case 1: goto L13;
                case 2: goto L6;
                default: goto L3;
            };
        L3:
            return EnvironmentCompat.MEDIA_UNKNOWN;
        L13:
            return "not seekable to start";
        L15:
            return "invalid period count";
        L6:
            if (r4 != C0180C.TIME_UNSET) goto L8;
        L10:
            boolean r0 = false;
        L11:
            Assertions.checkState(r0);
            return "start exceeds end. Start time: " + r4 + ", End time: " + r6;
        L8:
            if (r6 == C0180C.TIME_UNSET) goto L10;
            r0 = true;
            goto L11
        }
    }

    public ClippingMediaSource(MediaSource r10, long r11) {
        this(r10, 0, r11, true, false, true);
    }

    public ClippingMediaSource(MediaSource r10, long r11, long r13) {
        this(r10, r11, r13, true, false, false);
    }

    public ClippingMediaSource(MediaSource r3, long r4, long r6, boolean r8, boolean r9, boolean r10) {
        super((MediaSource) Assertions.checkNotNull(r3));
        if (r4 < 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.startUs = r4;
        this.endUs = r6;
        this.enableInitialDiscontinuity = r8;
        this.allowDynamicClippingUpdates = r9;
        this.relativeToDefaultPosition = r10;
        this.mediaPeriods = new ArrayList();
        this.window = new Timeline.Window();
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void refreshClippedTimeline(Timeline r15) {
        r15.getWindow(0, this.window);
        long r8 = this.window.getPositionInFirstPeriodUs();
        long r1 = Long.MIN_VALUE;
        if (this.clippingTimeline != null) goto L5;
    L14:
        long r4 = this.startUs;
        long r6 = this.endUs;
        if (this.relativeToDefaultPosition == false) goto L17;
        long r10 = this.window.getDefaultPositionUs();
        r4 = r4 + r10;
        r6 = r6 + r10;
    L17:
        this.periodStartUs = r8 + r4;
        if (this.endUs == Long.MIN_VALUE) goto L21;
        r1 = r8 + r6;
    L21:
        this.periodEndUs = r1;
        int r0 = this.mediaPeriods.size();
        int r2 = 0;
    L22:
        if (r2 >= r0) goto L34;
        this.mediaPeriods.get(r2).updateClipping(this.periodStartUs, this.periodEndUs);
        r2 = r2 + 1;
    L34:
        this.clippingTimeline = new ClippingTimeline(r15, r4, r6);     // Catch: IllegalClippingException -> L28
        refreshSourceInfo(this.clippingTimeline);
        return;
    L28:
        e = move-exception;
        this.clippingError = e;
        int r3 = 0;
    L31:
        if (r3 >= this.mediaPeriods.size()) goto L33;
        this.mediaPeriods.get(r3).setClippingError(this.clippingError);
        r3 = r3 + 1;
        goto L31
    L33:
        return;
    L5:
        if (this.mediaPeriods.isEmpty() == true) goto L14;
        if (this.allowDynamicClippingUpdates == true) goto L14;
        r4 = this.periodStartUs - r8;
        if (this.endUs == Long.MIN_VALUE) goto L13;
        r1 = this.periodEndUs - r8;
    L13:
        r6 = r1;
        goto L34
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean canUpdateMediaItem(MediaItem r3) {
        if (getMediaItem().clippingConfiguration.equals(r3.clippingConfiguration) == true) goto L5;
    L7:
        return false;
    L5:
        if (this.mediaSource.canUpdateMediaItem(r3) == false) goto L7;
        return true;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r8, Allocator r9, long r10) {
        ClippingMediaPeriod r0 = new ClippingMediaPeriod(this.mediaSource.createPeriod(r8, r9, r10), this.enableInitialDiscontinuity, this.periodStartUs, this.periodEndUs);
        this.mediaPeriods.add(r0);
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.clippingError != null) goto L7;
        super.maybeThrowSourceInfoRefreshError();
        return;
    L7:
        throw this.clippingError;
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource
    protected void onChildSourceInfoRefreshed(Timeline r2) {
        if (this.clippingError == null) goto L5;
        return;
    L5:
        refreshClippedTimeline(r2);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r3) {
        Assertions.checkState(this.mediaPeriods.remove(r3));
        this.mediaSource.releasePeriod(((ClippingMediaPeriod) r3).mediaPeriod);
        if (this.mediaPeriods.isEmpty() == true) goto L5;
        return;
    L5:
        if (this.allowDynamicClippingUpdates == true) goto L9;
        refreshClippedTimeline(((ClippingTimeline) Assertions.checkNotNull(this.clippingTimeline)).timeline);
        return;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void releaseSourceInternal() {
        super.releaseSourceInternal();
        this.clippingError = null;
        this.clippingTimeline = null;
    }
}
