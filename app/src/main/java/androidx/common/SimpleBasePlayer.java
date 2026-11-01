package androidx.media3.common;

import android.graphics.Rect;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.ListenerSet;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.Util;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleBasePlayer extends BasePlayer {
    private static final long POSITION_DISCONTINUITY_THRESHOLD_MS = 1000;
    private final HandlerWrapper applicationHandler;
    private final Looper applicationLooper;
    private final ListenerSet<Player.Listener> listeners;
    private final HashSet<ListenableFuture<?>> pendingOperations;
    private final Timeline.Period period;
    private boolean released;
    private State state;

    /* JADX INFO: renamed from: androidx.media3.common.SimpleBasePlayer$1 */
    static /* synthetic */ class C01941 {
    }

    protected static final class MediaItemData {
        public final long defaultPositionUs;
        public final long durationUs;
        public final long elapsedRealtimeEpochOffsetMs;
        public final boolean isDynamic;
        public final boolean isPlaceholder;
        public final boolean isSeekable;
        public final MediaItem.LiveConfiguration liveConfiguration;
        public final Object manifest;
        public final MediaItem mediaItem;
        public final MediaMetadata mediaMetadata;
        private final long[] periodPositionInWindowUs;
        public final ImmutableList<PeriodData> periods;
        public final long positionInFirstPeriodUs;
        public final long presentationStartTimeMs;
        public final Tracks tracks;
        public final Object uid;
        public final long windowStartTimeMs;

        public static final class Builder {
            private long defaultPositionUs;
            private long durationUs;
            private long elapsedRealtimeEpochOffsetMs;
            private boolean isDynamic;
            private boolean isPlaceholder;
            private boolean isSeekable;
            private MediaItem.LiveConfiguration liveConfiguration;
            private Object manifest;
            private MediaItem mediaItem;
            private MediaMetadata mediaMetadata;
            private ImmutableList<PeriodData> periods;
            private long positionInFirstPeriodUs;
            private long presentationStartTimeMs;
            private Tracks tracks;
            private Object uid;
            private long windowStartTimeMs;

            private Builder(MediaItemData r3) {
                this.uid = r3.uid;
                this.tracks = r3.tracks;
                this.mediaItem = r3.mediaItem;
                this.mediaMetadata = r3.mediaMetadata;
                this.manifest = r3.manifest;
                this.liveConfiguration = r3.liveConfiguration;
                this.presentationStartTimeMs = r3.presentationStartTimeMs;
                this.windowStartTimeMs = r3.windowStartTimeMs;
                this.elapsedRealtimeEpochOffsetMs = r3.elapsedRealtimeEpochOffsetMs;
                this.isSeekable = r3.isSeekable;
                this.isDynamic = r3.isDynamic;
                this.defaultPositionUs = r3.defaultPositionUs;
                this.durationUs = r3.durationUs;
                this.positionInFirstPeriodUs = r3.positionInFirstPeriodUs;
                this.isPlaceholder = r3.isPlaceholder;
                this.periods = r3.periods;
            }

            /* synthetic */ Builder(MediaItemData r1, C01941 r2) {
                this(r1);
            }

            public Builder(Object r6) {
                this.uid = r6;
                this.tracks = Tracks.EMPTY;
                this.mediaItem = MediaItem.EMPTY;
                this.mediaMetadata = null;
                this.manifest = null;
                this.liveConfiguration = null;
                this.presentationStartTimeMs = C0180C.TIME_UNSET;
                this.windowStartTimeMs = C0180C.TIME_UNSET;
                this.elapsedRealtimeEpochOffsetMs = C0180C.TIME_UNSET;
                this.isSeekable = false;
                this.isDynamic = false;
                this.defaultPositionUs = 0;
                this.durationUs = C0180C.TIME_UNSET;
                this.positionInFirstPeriodUs = 0;
                this.isPlaceholder = false;
                this.periods = ImmutableList.m212of();
            }

            static /* synthetic */ MediaItem.LiveConfiguration access$5200(Builder r1) {
                return r1.liveConfiguration;
            }

            static /* synthetic */ long access$5300(Builder r2) {
                return r2.presentationStartTimeMs;
            }

            static /* synthetic */ long access$5400(Builder r2) {
                return r2.windowStartTimeMs;
            }

            static /* synthetic */ long access$5500(Builder r2) {
                return r2.elapsedRealtimeEpochOffsetMs;
            }

            static /* synthetic */ ImmutableList access$5600(Builder r1) {
                return r1.periods;
            }

            static /* synthetic */ long access$5700(Builder r2) {
                return r2.durationUs;
            }

            static /* synthetic */ long access$5800(Builder r2) {
                return r2.defaultPositionUs;
            }

            static /* synthetic */ Object access$5900(Builder r1) {
                return r1.uid;
            }

            static /* synthetic */ Tracks access$6000(Builder r1) {
                return r1.tracks;
            }

            static /* synthetic */ MediaItem access$6100(Builder r1) {
                return r1.mediaItem;
            }

            static /* synthetic */ MediaMetadata access$6200(Builder r1) {
                return r1.mediaMetadata;
            }

            static /* synthetic */ Object access$6300(Builder r1) {
                return r1.manifest;
            }

            static /* synthetic */ boolean access$6400(Builder r1) {
                return r1.isSeekable;
            }

            static /* synthetic */ boolean access$6500(Builder r1) {
                return r1.isDynamic;
            }

            static /* synthetic */ long access$6600(Builder r2) {
                return r2.positionInFirstPeriodUs;
            }

            static /* synthetic */ boolean access$6700(Builder r1) {
                return r1.isPlaceholder;
            }

            public MediaItemData build() {
                return new MediaItemData(this, null);
            }

            public Builder setDefaultPositionUs(long r3) {
                if (r3 < 0) goto L5;
                boolean r0 = true;
            L6:
                Assertions.checkArgument(r0);
                this.defaultPositionUs = r3;
                return this;
            L5:
                r0 = false;
                goto L6
            }

            public Builder setDurationUs(long r3) {
                if (r3 != C0180C.TIME_UNSET) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.durationUs = r3;
                return this;
            L5:
                if (r3 >= 0) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setElapsedRealtimeEpochOffsetMs(long r1) {
                this.elapsedRealtimeEpochOffsetMs = r1;
                return this;
            }

            public Builder setIsDynamic(boolean r1) {
                this.isDynamic = r1;
                return this;
            }

            public Builder setIsPlaceholder(boolean r1) {
                this.isPlaceholder = r1;
                return this;
            }

            public Builder setIsSeekable(boolean r1) {
                this.isSeekable = r1;
                return this;
            }

            public Builder setLiveConfiguration(MediaItem.LiveConfiguration r1) {
                this.liveConfiguration = r1;
                return this;
            }

            public Builder setManifest(Object r1) {
                this.manifest = r1;
                return this;
            }

            public Builder setMediaItem(MediaItem r1) {
                this.mediaItem = r1;
                return this;
            }

            public Builder setMediaMetadata(MediaMetadata r1) {
                this.mediaMetadata = r1;
                return this;
            }

            public Builder setPeriods(List<PeriodData> r7) {
                int r0 = r7.size();
                int r1 = 0;
            L4:
                if (r1 >= (r0 - 1)) goto L13;
                if (r7.get(r1).durationUs == C0180C.TIME_UNSET) goto L8;
                boolean r2 = true;
            L9:
                Assertions.checkArgument(r2, "Periods other than last need a duration");
                int r3 = r1 + 1;
            L10:
                if (r3 >= r0) goto L12;
                Assertions.checkArgument(!r7.get(r1).uid.equals(r7.get(r3).uid), "Duplicate PeriodData UIDs in period list");
                r3 = r3 + 1;
                goto L10
            L12:
                r1 = r1 + 1;
                goto L4
            L8:
                r2 = false;
                goto L9
            L13:
                this.periods = ImmutableList.copyOf(r7);
                return this;
            }

            public Builder setPositionInFirstPeriodUs(long r3) {
                if (r3 < 0) goto L5;
                boolean r0 = true;
            L6:
                Assertions.checkArgument(r0);
                this.positionInFirstPeriodUs = r3;
                return this;
            L5:
                r0 = false;
                goto L6
            }

            public Builder setPresentationStartTimeMs(long r1) {
                this.presentationStartTimeMs = r1;
                return this;
            }

            public Builder setTracks(Tracks r1) {
                this.tracks = r1;
                return this;
            }

            public Builder setUid(Object r1) {
                this.uid = r1;
                return this;
            }

            public Builder setWindowStartTimeMs(long r1) {
                this.windowStartTimeMs = r1;
                return this;
            }
        }

        private MediaItemData(Builder r10) {
            boolean r4 = true;
            if (Builder.access$5200(r10) != null) goto L18;
            if (Builder.access$5300(r10) != C0180C.TIME_UNSET) goto L7;
            boolean r0 = true;
        L8:
            Assertions.checkArgument(r0, "presentationStartTimeMs can only be set if liveConfiguration != null");
            if (Builder.access$5400(r10) != C0180C.TIME_UNSET) goto L11;
            boolean r1 = true;
        L12:
            Assertions.checkArgument(r1, "windowStartTimeMs can only be set if liveConfiguration != null");
            if (Builder.access$5500(r10) != C0180C.TIME_UNSET) goto L15;
            boolean r2 = true;
        L16:
            Assertions.checkArgument(r2, "elapsedRealtimeEpochOffsetMs can only be set if liveConfiguration != null");
        L26:
            int r3 = Builder.access$5600(r10).size();
            if (Builder.access$5700(r10) != C0180C.TIME_UNSET) goto L29;
        L33:
            this.uid = Builder.access$5900(r10);
            this.tracks = Builder.access$6000(r10);
            this.mediaItem = Builder.access$6100(r10);
            this.mediaMetadata = Builder.access$6200(r10);
            this.manifest = Builder.access$6300(r10);
            this.liveConfiguration = Builder.access$5200(r10);
            this.presentationStartTimeMs = Builder.access$5300(r10);
            this.windowStartTimeMs = Builder.access$5400(r10);
            this.elapsedRealtimeEpochOffsetMs = Builder.access$5500(r10);
            this.isSeekable = Builder.access$6400(r10);
            this.isDynamic = Builder.access$6500(r10);
            this.defaultPositionUs = Builder.access$5800(r10);
            this.durationUs = Builder.access$5700(r10);
            this.positionInFirstPeriodUs = Builder.access$6600(r10);
            this.isPlaceholder = Builder.access$6700(r10);
            this.periods = Builder.access$5600(r10);
            this.periodPositionInWindowUs = new long[this.periods.size()];
            if (this.periods.isEmpty() == true) goto L39;
            this.periodPositionInWindowUs[0] = -this.positionInFirstPeriodUs;
            int r5 = 0;
        L37:
            if (r5 >= (r3 - 1)) goto L41;
            this.periodPositionInWindowUs[r5 + 1] = this.periodPositionInWindowUs[r5] + this.periods.get(r5).durationUs;
            r5 = r5 + 1;
            goto L37
        L41:
            return;
        L39:
            return;
        L29:
            if (Builder.access$5800(r10) <= Builder.access$5700(r10)) goto L32;
            r4 = false;
        L32:
            Assertions.checkArgument(r4, "defaultPositionUs can't be greater than durationUs");
            goto L33
        L15:
            r2 = false;
            goto L16
        L11:
            r1 = false;
            goto L12
        L7:
            r0 = false;
            goto L8
        L18:
            if (Builder.access$5300(r10) == C0180C.TIME_UNSET) goto L26;
            if (Builder.access$5400(r10) == C0180C.TIME_UNSET) goto L26;
            if (Builder.access$5400(r10) < Builder.access$5300(r10)) goto L24;
            boolean r6 = true;
        L25:
            Assertions.checkArgument(r6, "windowStartTimeMs can't be less than presentationStartTimeMs");
            goto L26
        L24:
            r6 = false;
            goto L25
        }

        /* synthetic */ MediaItemData(Builder r1, C01941 r2) {
            this(r1);
        }

        static /* synthetic */ MediaItemData access$4700(State r1, int r2, Timeline.Period r3, Timeline.Window r4) {
            return buildFromState(r1, r2, r3, r4);
        }

        static /* synthetic */ Object access$4800(MediaItemData r1, int r2) {
            return r1.getPeriodUid(r2);
        }

        static /* synthetic */ Timeline.Window access$4900(MediaItemData r1, int r2, Timeline.Window r3) {
            return r1.getWindow(r2, r3);
        }

        static /* synthetic */ Timeline.Period access$5000(MediaItemData r1, int r2, int r3, Timeline.Period r4) {
            return r1.getPeriod(r2, r3, r4);
        }

        private static MediaItemData buildFromState(State r7, int r8, Timeline.Period r9, Timeline.Window r10) {
            if (SimpleBasePlayer.access$6900(r7) != r8) goto L5;
            boolean r0 = true;
        L6:
            r7.timeline.getWindow(r8, r10);
            ImmutableList.Builder r2 = ImmutableList.builder();
            int r3 = r10.firstPeriodIndex;
        L8:
            if (r3 > r10.lastPeriodIndex) goto L10;
            r7.timeline.getPeriod(r3, r9, true);
            r2.add(new PeriodData.Builder(Assertions.checkNotNull(r9.uid)).setAdPlaybackState(r9.adPlaybackState).setDurationUs(r9.durationUs).setIsPlaceholder(r9.isPlaceholder).build());
            r3 = r3 + 1;
            goto L8
        L10:
            Builder r1 = new Builder(r10.uid).setDefaultPositionUs(r10.defaultPositionUs).setDurationUs(r10.durationUs).setElapsedRealtimeEpochOffsetMs(r10.elapsedRealtimeEpochOffsetMs).setIsDynamic(r10.isDynamic).setIsPlaceholder(r10.isPlaceholder).setIsSeekable(r10.isSeekable).setLiveConfiguration(r10.liveConfiguration).setManifest(r10.manifest).setMediaItem(r10.mediaItem);
            if (r0 == false) goto L13;
            MediaMetadata r4 = r7.currentMetadata;
        L14:
            Builder r5 = r1.setMediaMetadata(r4).setPeriods(r2.build()).setPositionInFirstPeriodUs(r10.positionInFirstPeriodUs).setPresentationStartTimeMs(r10.presentationStartTimeMs);
            if (r0 == false) goto L17;
            Tracks r6 = r7.currentTracks;
        L19:
            return r5.setTracks(r6).setWindowStartTimeMs(r10.windowStartTimeMs).build();
        L17:
            r6 = Tracks.EMPTY;
            goto L19
        L13:
            r4 = null;
            goto L14
        L5:
            r0 = false;
            goto L6
        }

        private Timeline.Period getPeriod(int r24, int r25, Timeline.Period r26) {
            if (this.periods.isEmpty() == false) goto L5;
            r26.set(this.uid, this.uid, r24, this.positionInFirstPeriodUs + this.durationUs, 0, AdPlaybackState.NONE, this.isPlaceholder);
        L6:
            return r26;
        L5:
            PeriodData r2 = this.periods.get(r25);
            Object r14 = r2.uid;
            r26.set(r14, Pair.create(this.uid, r14), r24, r2.durationUs, this.periodPositionInWindowUs[r25], r2.adPlaybackState, r2.isPlaceholder);
            goto L6
        }

        private Object getPeriodUid(int r3) {
            if (this.periods.isEmpty() == true) goto L5;
            Object r0 = this.periods.get(r3).uid;
            return Pair.create(this.uid, r0);
        L5:
            return this.uid;
        }

        private Timeline.Window getWindow(int r26, Timeline.Window r27) {
            if (this.periods.isEmpty() == false) goto L5;
            int r1 = 1;
        L6:
            r27.set(this.uid, this.mediaItem, this.manifest, this.presentationStartTimeMs, this.windowStartTimeMs, this.elapsedRealtimeEpochOffsetMs, this.isSeekable, this.isDynamic, this.liveConfiguration, this.defaultPositionUs, this.durationUs, r26, (r26 + r1) - 1, this.positionInFirstPeriodUs);
            r27.isPlaceholder = this.isPlaceholder;
            return r27;
        L5:
            r1 = this.periods.size();
            goto L6
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof MediaItemData) == true) goto L8;
            return false;
        L8:
            MediaItemData r1 = (MediaItemData) r8;
            if (this.uid.equals(r1.uid) == false) goto L42;
            if (this.tracks.equals(r1.tracks) == false) goto L42;
            if (this.mediaItem.equals(r1.mediaItem) == false) goto L42;
            if (Util.areEqual(this.mediaMetadata, r1.mediaMetadata) == false) goto L42;
            if (Util.areEqual(this.manifest, r1.manifest) == false) goto L42;
            if (Util.areEqual(this.liveConfiguration, r1.liveConfiguration) == false) goto L42;
            if (this.presentationStartTimeMs != r1.presentationStartTimeMs) goto L42;
            if (this.windowStartTimeMs != r1.windowStartTimeMs) goto L42;
            if (this.elapsedRealtimeEpochOffsetMs != r1.elapsedRealtimeEpochOffsetMs) goto L42;
            if (this.isSeekable != r1.isSeekable) goto L42;
            if (this.isDynamic != r1.isDynamic) goto L42;
            if (this.defaultPositionUs != r1.defaultPositionUs) goto L42;
            if (this.durationUs != r1.durationUs) goto L42;
            if (this.positionInFirstPeriodUs != r1.positionInFirstPeriodUs) goto L42;
            if (this.isPlaceholder != r1.isPlaceholder) goto L42;
            if (this.periods.equals(r1.periods) == false) goto L42;
            return true;
        L42:
            return false;
        }

        public int hashCode() {
            int r0 = ((((((7 * 31) + this.uid.hashCode()) * 31) + this.tracks.hashCode()) * 31) + this.mediaItem.hashCode()) * 31;
            int r3 = 0;
            if (this.mediaMetadata != null) goto L5;
            int r2 = 0;
        L6:
            int r1 = (r0 + r2) * 31;
            if (this.manifest != null) goto L9;
            int r4 = 0;
        L10:
            int r5 = (r1 + r4) * 31;
            if (this.liveConfiguration == null) goto L15;
            r3 = this.liveConfiguration.hashCode();
        L15:
            return ((((((((((((((((((((r5 + r3) * 31) + ((int) (this.presentationStartTimeMs ^ (this.presentationStartTimeMs >>> 32)))) * 31) + ((int) (this.windowStartTimeMs ^ (this.windowStartTimeMs >>> 32)))) * 31) + ((int) (this.elapsedRealtimeEpochOffsetMs ^ (this.elapsedRealtimeEpochOffsetMs >>> 32)))) * 31) + (this.isSeekable ? 1 : 0)) * 31) + (this.isDynamic ? 1 : 0)) * 31) + ((int) (this.defaultPositionUs ^ (this.defaultPositionUs >>> 32)))) * 31) + ((int) (this.durationUs ^ (this.durationUs >>> 32)))) * 31) + ((int) (this.positionInFirstPeriodUs ^ (this.positionInFirstPeriodUs >>> 32)))) * 31) + (this.isPlaceholder ? 1 : 0)) * 31) + this.periods.hashCode();
        L9:
            r4 = this.manifest.hashCode();
            goto L10
        L5:
            r2 = this.mediaMetadata.hashCode();
            goto L6
        }
    }

    protected static final class PeriodData {
        public final AdPlaybackState adPlaybackState;
        public final long durationUs;
        public final boolean isPlaceholder;
        public final Object uid;

        public static final class Builder {
            private AdPlaybackState adPlaybackState;
            private long durationUs;
            private boolean isPlaceholder;
            private Object uid;

            private Builder(PeriodData r3) {
                this.uid = r3.uid;
                this.durationUs = r3.durationUs;
                this.adPlaybackState = r3.adPlaybackState;
                this.isPlaceholder = r3.isPlaceholder;
            }

            /* synthetic */ Builder(PeriodData r1, C01941 r2) {
                this(r1);
            }

            public Builder(Object r3) {
                this.uid = r3;
                this.durationUs = 0;
                this.adPlaybackState = AdPlaybackState.NONE;
                this.isPlaceholder = false;
            }

            static /* synthetic */ Object access$7100(Builder r1) {
                return r1.uid;
            }

            static /* synthetic */ long access$7200(Builder r2) {
                return r2.durationUs;
            }

            static /* synthetic */ AdPlaybackState access$7300(Builder r1) {
                return r1.adPlaybackState;
            }

            static /* synthetic */ boolean access$7400(Builder r1) {
                return r1.isPlaceholder;
            }

            public PeriodData build() {
                return new PeriodData(this, null);
            }

            public Builder setAdPlaybackState(AdPlaybackState r1) {
                this.adPlaybackState = r1;
                return this;
            }

            public Builder setDurationUs(long r3) {
                if (r3 != C0180C.TIME_UNSET) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.durationUs = r3;
                return this;
            L5:
                if (r3 >= 0) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setIsPlaceholder(boolean r1) {
                this.isPlaceholder = r1;
                return this;
            }

            public Builder setUid(Object r1) {
                this.uid = r1;
                return this;
            }
        }

        private PeriodData(Builder r3) {
            this.uid = Builder.access$7100(r3);
            this.durationUs = Builder.access$7200(r3);
            this.adPlaybackState = Builder.access$7300(r3);
            this.isPlaceholder = Builder.access$7400(r3);
        }

        /* synthetic */ PeriodData(Builder r1, C01941 r2) {
            this(r1);
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof PeriodData) == true) goto L8;
            return false;
        L8:
            PeriodData r1 = (PeriodData) r8;
            if (this.uid.equals(r1.uid) == false) goto L18;
            if (this.durationUs != r1.durationUs) goto L18;
            if (this.adPlaybackState.equals(r1.adPlaybackState) == false) goto L18;
            if (this.isPlaceholder != r1.isPlaceholder) goto L18;
            return true;
        L18:
            return false;
        }

        public int hashCode() {
            return (((((((7 * 31) + this.uid.hashCode()) * 31) + ((int) (this.durationUs ^ (this.durationUs >>> 32)))) * 31) + this.adPlaybackState.hashCode()) * 31) + (this.isPlaceholder ? 1 : 0);
        }
    }

    private static final class PlaceholderUid {
        private PlaceholderUid() {
        }

        /* synthetic */ PlaceholderUid(C01941 r1) {
            this();
        }
    }

    private static final class PlaylistTimeline extends Timeline {
        private final int[] firstPeriodIndexByWindowIndex;
        private final HashMap<Object, Integer> periodIndexByUid;
        private final ImmutableList<MediaItemData> playlist;
        private final int[] windowIndexByPeriodIndex;

        public PlaylistTimeline(List<MediaItemData> r10) {
            int r0 = r10.size();
            this.playlist = ImmutableList.copyOf(r10);
            this.firstPeriodIndexByWindowIndex = new int[r0];
            int r1 = 0;
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L5;
            MediaItemData r3 = r10.get(r2);
            this.firstPeriodIndexByWindowIndex[r2] = r1;
            r1 = r1 + getPeriodCountInMediaItem(r3);
            r2 = r2 + 1;
            goto L3
        L5:
            this.windowIndexByPeriodIndex = new int[r1];
            this.periodIndexByUid = new HashMap();
            int r4 = 0;
            int r5 = 0;
        L6:
            if (r5 >= r0) goto L12;
            MediaItemData r6 = r10.get(r5);
            int r7 = 0;
        L9:
            if (r7 >= getPeriodCountInMediaItem(r6)) goto L11;
            this.periodIndexByUid.put(MediaItemData.access$4800(r6, r7), Integer.valueOf(r4));
            this.windowIndexByPeriodIndex[r4] = r5;
            r4 = r4 + 1;
            r7 = r7 + 1;
            goto L9
        L11:
            r5 = r5 + 1;
            goto L6
        }

        static /* synthetic */ ImmutableList access$000(PlaylistTimeline r1) {
            return r1.playlist;
        }

        private static int getPeriodCountInMediaItem(MediaItemData r1) {
            if (r1.periods.isEmpty() == false) goto L6;
            return 1;
        L6:
            return r1.periods.size();
        }

        @Override // androidx.media3.common.Timeline
        public int getFirstWindowIndex(boolean r2) {
            return super.getFirstWindowIndex(r2);
        }

        @Override // androidx.media3.common.Timeline
        public int getIndexOfPeriod(Object r3) {
            Integer r0 = this.periodIndexByUid.get(r3);
            if (r0 != null) goto L6;
            return -1;
        L6:
            return r0.intValue();
        }

        @Override // androidx.media3.common.Timeline
        public int getLastWindowIndex(boolean r2) {
            return super.getLastWindowIndex(r2);
        }

        @Override // androidx.media3.common.Timeline
        public int getNextWindowIndex(int r2, int r3, boolean r4) {
            return super.getNextWindowIndex(r2, r3, r4);
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Period getPeriod(int r4, Timeline.Period r5, boolean r6) {
            int r0 = this.windowIndexByPeriodIndex[r4];
            int r1 = r4 - this.firstPeriodIndexByWindowIndex[r0];
            return MediaItemData.access$5000(this.playlist.get(r0), r0, r1, r5);
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Period getPeriodByUid(Object r3, Timeline.Period r4) {
            return getPeriod(((Integer) Assertions.checkNotNull(this.periodIndexByUid.get(r3))).intValue(), r4, true);
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return this.windowIndexByPeriodIndex.length;
        }

        @Override // androidx.media3.common.Timeline
        public int getPreviousWindowIndex(int r2, int r3, boolean r4) {
            return super.getPreviousWindowIndex(r2, r3, r4);
        }

        @Override // androidx.media3.common.Timeline
        public Object getUidOfPeriod(int r4) {
            int r0 = this.windowIndexByPeriodIndex[r4];
            int r1 = r4 - this.firstPeriodIndexByWindowIndex[r0];
            return MediaItemData.access$4800(this.playlist.get(r0), r1);
        }

        @Override // androidx.media3.common.Timeline
        public Timeline.Window getWindow(int r3, Timeline.Window r4, long r5) {
            return MediaItemData.access$4900(this.playlist.get(r3), this.firstPeriodIndexByWindowIndex[r3], r4);
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return this.playlist.size();
        }
    }

    protected interface PositionSupplier {
        public static final PositionSupplier ZERO = null;

        /* JADX INFO: renamed from: androidx.media3.common.SimpleBasePlayer$PositionSupplier$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            static {
                PositionSupplier r0 = PositionSupplier.ZERO;
            }

            public static PositionSupplier getConstant(final long r1) {
                return new SimpleBasePlayer$PositionSupplier$$ExternalSyntheticLambda1(r1);
            }

            public static PositionSupplier getExtrapolating(final long r6, final float r8) {
                final long r3 = SystemClock.elapsedRealtime();
                return new SimpleBasePlayer$PositionSupplier$$ExternalSyntheticLambda0(r6, r3, r8);
            }

            public static /* synthetic */ long lambda$getConstant$0(long r0) {
                return r0;
            }

            public static /* synthetic */ long lambda$getExtrapolating$1(long r4, long r6, float r8) {
                return ((long) ((SystemClock.elapsedRealtime() - r6) * r8)) + r4;
            }
        }

        static {
            ZERO = CC.getConstant(0);
        }

        long get();
    }

    protected static final class State {
        public final PositionSupplier adBufferedPositionMsSupplier;
        public final PositionSupplier adPositionMsSupplier;
        public final AudioAttributes audioAttributes;
        public final Player.Commands availableCommands;
        public final PositionSupplier contentBufferedPositionMsSupplier;
        public final PositionSupplier contentPositionMsSupplier;
        public final int currentAdGroupIndex;
        public final int currentAdIndexInAdGroup;
        public final CueGroup currentCues;
        public final int currentMediaItemIndex;
        public final MediaMetadata currentMetadata;
        public final Tracks currentTracks;
        public final DeviceInfo deviceInfo;
        public final int deviceVolume;
        public final long discontinuityPositionMs;
        public final boolean hasPositionDiscontinuity;
        public final boolean isDeviceMuted;
        public final boolean isLoading;
        public final long maxSeekToPreviousPositionMs;
        public final boolean newlyRenderedFirstFrame;
        public final boolean playWhenReady;
        public final int playWhenReadyChangeReason;
        public final PlaybackParameters playbackParameters;
        public final int playbackState;
        public final int playbackSuppressionReason;
        public final PlaybackException playerError;
        public final MediaMetadata playlistMetadata;
        public final int positionDiscontinuityReason;
        public final int repeatMode;
        public final long seekBackIncrementMs;
        public final long seekForwardIncrementMs;
        public final boolean shuffleModeEnabled;
        public final Size surfaceSize;
        public final Metadata timedMetadata;
        public final Timeline timeline;
        public final PositionSupplier totalBufferedDurationMsSupplier;
        public final TrackSelectionParameters trackSelectionParameters;
        public final VideoSize videoSize;
        public final float volume;

        public static final class Builder {
            private PositionSupplier adBufferedPositionMsSupplier;
            private Long adPositionMs;
            private PositionSupplier adPositionMsSupplier;
            private AudioAttributes audioAttributes;
            private Player.Commands availableCommands;
            private PositionSupplier contentBufferedPositionMsSupplier;
            private Long contentPositionMs;
            private PositionSupplier contentPositionMsSupplier;
            private int currentAdGroupIndex;
            private int currentAdIndexInAdGroup;
            private CueGroup currentCues;
            private int currentMediaItemIndex;
            private MediaMetadata currentMetadata;
            private Tracks currentTracks;
            private DeviceInfo deviceInfo;
            private int deviceVolume;
            private long discontinuityPositionMs;
            private boolean hasPositionDiscontinuity;
            private boolean isDeviceMuted;
            private boolean isLoading;
            private long maxSeekToPreviousPositionMs;
            private boolean newlyRenderedFirstFrame;
            private boolean playWhenReady;
            private int playWhenReadyChangeReason;
            private PlaybackParameters playbackParameters;
            private int playbackState;
            private int playbackSuppressionReason;
            private PlaybackException playerError;
            private ImmutableList<MediaItemData> playlist;
            private MediaMetadata playlistMetadata;
            private int positionDiscontinuityReason;
            private int repeatMode;
            private long seekBackIncrementMs;
            private long seekForwardIncrementMs;
            private boolean shuffleModeEnabled;
            private Size surfaceSize;
            private Metadata timedMetadata;
            private Timeline timeline;
            private PositionSupplier totalBufferedDurationMsSupplier;
            private TrackSelectionParameters trackSelectionParameters;
            private VideoSize videoSize;
            private float volume;

            public Builder() {
                this.availableCommands = Player.Commands.EMPTY;
                this.playWhenReady = false;
                this.playWhenReadyChangeReason = 1;
                this.playbackState = 1;
                this.playbackSuppressionReason = 0;
                this.playerError = null;
                this.repeatMode = 0;
                this.shuffleModeEnabled = false;
                this.isLoading = false;
                this.seekBackIncrementMs = 5000;
                this.seekForwardIncrementMs = C0180C.DEFAULT_SEEK_FORWARD_INCREMENT_MS;
                this.maxSeekToPreviousPositionMs = C0180C.DEFAULT_MAX_SEEK_TO_PREVIOUS_POSITION_MS;
                this.playbackParameters = PlaybackParameters.DEFAULT;
                this.trackSelectionParameters = TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT;
                this.audioAttributes = AudioAttributes.DEFAULT;
                this.volume = 1.0f;
                this.videoSize = VideoSize.UNKNOWN;
                this.currentCues = CueGroup.EMPTY_TIME_ZERO;
                this.deviceInfo = DeviceInfo.UNKNOWN;
                this.deviceVolume = 0;
                this.isDeviceMuted = false;
                this.surfaceSize = Size.UNKNOWN;
                this.newlyRenderedFirstFrame = false;
                this.timedMetadata = new Metadata(C0180C.TIME_UNSET, new Metadata.Entry[0]);
                this.playlist = ImmutableList.m212of();
                this.timeline = Timeline.EMPTY;
                this.currentTracks = null;
                this.currentMetadata = null;
                this.playlistMetadata = MediaMetadata.EMPTY;
                this.currentMediaItemIndex = -1;
                this.currentAdGroupIndex = -1;
                this.currentAdIndexInAdGroup = -1;
                this.contentPositionMs = null;
                this.contentPositionMsSupplier = PositionSupplier.CC.getConstant(C0180C.TIME_UNSET);
                this.adPositionMs = null;
                this.adPositionMsSupplier = PositionSupplier.ZERO;
                this.contentBufferedPositionMsSupplier = PositionSupplier.CC.getConstant(C0180C.TIME_UNSET);
                this.adBufferedPositionMsSupplier = PositionSupplier.ZERO;
                this.totalBufferedDurationMsSupplier = PositionSupplier.ZERO;
                this.hasPositionDiscontinuity = false;
                this.positionDiscontinuityReason = 5;
                this.discontinuityPositionMs = 0;
            }

            private Builder(State r3) {
                this.availableCommands = r3.availableCommands;
                this.playWhenReady = r3.playWhenReady;
                this.playWhenReadyChangeReason = r3.playWhenReadyChangeReason;
                this.playbackState = r3.playbackState;
                this.playbackSuppressionReason = r3.playbackSuppressionReason;
                this.playerError = r3.playerError;
                this.repeatMode = r3.repeatMode;
                this.shuffleModeEnabled = r3.shuffleModeEnabled;
                this.isLoading = r3.isLoading;
                this.seekBackIncrementMs = r3.seekBackIncrementMs;
                this.seekForwardIncrementMs = r3.seekForwardIncrementMs;
                this.maxSeekToPreviousPositionMs = r3.maxSeekToPreviousPositionMs;
                this.playbackParameters = r3.playbackParameters;
                this.trackSelectionParameters = r3.trackSelectionParameters;
                this.audioAttributes = r3.audioAttributes;
                this.volume = r3.volume;
                this.videoSize = r3.videoSize;
                this.currentCues = r3.currentCues;
                this.deviceInfo = r3.deviceInfo;
                this.deviceVolume = r3.deviceVolume;
                this.isDeviceMuted = r3.isDeviceMuted;
                this.surfaceSize = r3.surfaceSize;
                this.newlyRenderedFirstFrame = r3.newlyRenderedFirstFrame;
                this.timedMetadata = r3.timedMetadata;
                this.timeline = r3.timeline;
                if ((r3.timeline instanceof PlaylistTimeline) == false) goto L5;
                this.playlist = PlaylistTimeline.access$000((PlaylistTimeline) r3.timeline);
            L6:
                this.playlistMetadata = r3.playlistMetadata;
                this.currentMediaItemIndex = r3.currentMediaItemIndex;
                this.currentAdGroupIndex = r3.currentAdGroupIndex;
                this.currentAdIndexInAdGroup = r3.currentAdIndexInAdGroup;
                this.contentPositionMs = null;
                this.contentPositionMsSupplier = r3.contentPositionMsSupplier;
                this.adPositionMs = null;
                this.adPositionMsSupplier = r3.adPositionMsSupplier;
                this.contentBufferedPositionMsSupplier = r3.contentBufferedPositionMsSupplier;
                this.adBufferedPositionMsSupplier = r3.adBufferedPositionMsSupplier;
                this.totalBufferedDurationMsSupplier = r3.totalBufferedDurationMsSupplier;
                this.hasPositionDiscontinuity = r3.hasPositionDiscontinuity;
                this.positionDiscontinuityReason = r3.positionDiscontinuityReason;
                this.discontinuityPositionMs = r3.discontinuityPositionMs;
                return;
            L5:
                this.currentTracks = r3.currentTracks;
                this.currentMetadata = r3.currentMetadata;
                goto L6
            }

            /* synthetic */ Builder(State r1, C01941 r2) {
                this(r1);
            }

            static /* synthetic */ PositionSupplier access$1000(Builder r1) {
                return r1.contentPositionMsSupplier;
            }

            static /* synthetic */ ImmutableList access$1200(Builder r1) {
                return r1.playlist;
            }

            static /* synthetic */ PlaybackException access$1400(Builder r1) {
                return r1.playerError;
            }

            static /* synthetic */ boolean access$1500(Builder r1) {
                return r1.isLoading;
            }

            static /* synthetic */ boolean access$1600(Builder r1) {
                return r1.playWhenReady;
            }

            static /* synthetic */ int access$1700(Builder r1) {
                return r1.playbackSuppressionReason;
            }

            static /* synthetic */ PlaybackParameters access$1800(Builder r1) {
                return r1.playbackParameters;
            }

            static /* synthetic */ PositionSupplier access$1900(Builder r1) {
                return r1.adPositionMsSupplier;
            }

            static /* synthetic */ Tracks access$200(Builder r1) {
                return r1.currentTracks;
            }

            static /* synthetic */ Long access$2000(Builder r1) {
                return r1.adPositionMs;
            }

            static /* synthetic */ Player.Commands access$2100(Builder r1) {
                return r1.availableCommands;
            }

            static /* synthetic */ int access$2200(Builder r1) {
                return r1.playWhenReadyChangeReason;
            }

            static /* synthetic */ int access$2300(Builder r1) {
                return r1.repeatMode;
            }

            static /* synthetic */ boolean access$2400(Builder r1) {
                return r1.shuffleModeEnabled;
            }

            static /* synthetic */ long access$2500(Builder r2) {
                return r2.seekBackIncrementMs;
            }

            static /* synthetic */ long access$2600(Builder r2) {
                return r2.seekForwardIncrementMs;
            }

            static /* synthetic */ long access$2700(Builder r2) {
                return r2.maxSeekToPreviousPositionMs;
            }

            static /* synthetic */ TrackSelectionParameters access$2800(Builder r1) {
                return r1.trackSelectionParameters;
            }

            static /* synthetic */ AudioAttributes access$2900(Builder r1) {
                return r1.audioAttributes;
            }

            static /* synthetic */ MediaMetadata access$300(Builder r1) {
                return r1.currentMetadata;
            }

            static /* synthetic */ float access$3000(Builder r1) {
                return r1.volume;
            }

            static /* synthetic */ VideoSize access$3100(Builder r1) {
                return r1.videoSize;
            }

            static /* synthetic */ CueGroup access$3200(Builder r1) {
                return r1.currentCues;
            }

            static /* synthetic */ DeviceInfo access$3300(Builder r1) {
                return r1.deviceInfo;
            }

            static /* synthetic */ int access$3400(Builder r1) {
                return r1.deviceVolume;
            }

            static /* synthetic */ boolean access$3500(Builder r1) {
                return r1.isDeviceMuted;
            }

            static /* synthetic */ Size access$3600(Builder r1) {
                return r1.surfaceSize;
            }

            static /* synthetic */ boolean access$3700(Builder r1) {
                return r1.newlyRenderedFirstFrame;
            }

            static /* synthetic */ Metadata access$3800(Builder r1) {
                return r1.timedMetadata;
            }

            static /* synthetic */ MediaMetadata access$3900(Builder r1) {
                return r1.playlistMetadata;
            }

            static /* synthetic */ Timeline access$400(Builder r1) {
                return r1.timeline;
            }

            static /* synthetic */ PositionSupplier access$4000(Builder r1) {
                return r1.contentBufferedPositionMsSupplier;
            }

            static /* synthetic */ PositionSupplier access$4100(Builder r1) {
                return r1.adBufferedPositionMsSupplier;
            }

            static /* synthetic */ PositionSupplier access$4200(Builder r1) {
                return r1.totalBufferedDurationMsSupplier;
            }

            static /* synthetic */ boolean access$4300(Builder r1) {
                return r1.hasPositionDiscontinuity;
            }

            static /* synthetic */ int access$4400(Builder r1) {
                return r1.positionDiscontinuityReason;
            }

            static /* synthetic */ long access$4500(Builder r2) {
                return r2.discontinuityPositionMs;
            }

            static /* synthetic */ int access$500(Builder r1) {
                return r1.playbackState;
            }

            static /* synthetic */ int access$600(Builder r1) {
                return r1.currentAdGroupIndex;
            }

            static /* synthetic */ int access$700(Builder r1) {
                return r1.currentAdIndexInAdGroup;
            }

            static /* synthetic */ int access$800(Builder r1) {
                return r1.currentMediaItemIndex;
            }

            static /* synthetic */ Long access$900(Builder r1) {
                return r1.contentPositionMs;
            }

            public State build() {
                return new State(this, null);
            }

            public Builder clearPositionDiscontinuity() {
                this.hasPositionDiscontinuity = false;
                return this;
            }

            public Builder setAdBufferedPositionMs(PositionSupplier r1) {
                this.adBufferedPositionMsSupplier = r1;
                return this;
            }

            public Builder setAdPositionMs(long r2) {
                this.adPositionMs = Long.valueOf(r2);
                return this;
            }

            public Builder setAdPositionMs(PositionSupplier r2) {
                this.adPositionMs = null;
                this.adPositionMsSupplier = r2;
                return this;
            }

            public Builder setAudioAttributes(AudioAttributes r1) {
                this.audioAttributes = r1;
                return this;
            }

            public Builder setAvailableCommands(Player.Commands r1) {
                this.availableCommands = r1;
                return this;
            }

            public Builder setContentBufferedPositionMs(PositionSupplier r1) {
                this.contentBufferedPositionMsSupplier = r1;
                return this;
            }

            public Builder setContentPositionMs(long r2) {
                this.contentPositionMs = Long.valueOf(r2);
                return this;
            }

            public Builder setContentPositionMs(PositionSupplier r2) {
                this.contentPositionMs = null;
                this.contentPositionMsSupplier = r2;
                return this;
            }

            public Builder setCurrentAd(int r5, int r6) {
                boolean r0 = true;
                if (r5 != (-1)) goto L5;
                boolean r3 = true;
            L6:
                if (r6 != (-1)) goto L8;
                boolean r2 = true;
            L9:
                if (r3 == r2) goto L12;
                r0 = false;
            L12:
                Assertions.checkArgument(r0);
                this.currentAdGroupIndex = r5;
                this.currentAdIndexInAdGroup = r6;
                return this;
            L8:
                r2 = false;
                goto L9
            L5:
                r3 = false;
                goto L6
            }

            public Builder setCurrentCues(CueGroup r1) {
                this.currentCues = r1;
                return this;
            }

            public Builder setCurrentMediaItemIndex(int r1) {
                this.currentMediaItemIndex = r1;
                return this;
            }

            public Builder setDeviceInfo(DeviceInfo r1) {
                this.deviceInfo = r1;
                return this;
            }

            public Builder setDeviceVolume(int r2) {
                if (r2 < 0) goto L4;
                boolean r0 = true;
            L5:
                Assertions.checkArgument(r0);
                this.deviceVolume = r2;
                return this;
            L4:
                r0 = false;
                goto L5
            }

            public Builder setIsDeviceMuted(boolean r1) {
                this.isDeviceMuted = r1;
                return this;
            }

            public Builder setIsLoading(boolean r1) {
                this.isLoading = r1;
                return this;
            }

            public Builder setMaxSeekToPreviousPositionMs(long r1) {
                this.maxSeekToPreviousPositionMs = r1;
                return this;
            }

            public Builder setNewlyRenderedFirstFrame(boolean r1) {
                this.newlyRenderedFirstFrame = r1;
                return this;
            }

            public Builder setPlayWhenReady(boolean r1, int r2) {
                this.playWhenReady = r1;
                this.playWhenReadyChangeReason = r2;
                return this;
            }

            public Builder setPlaybackParameters(PlaybackParameters r1) {
                this.playbackParameters = r1;
                return this;
            }

            public Builder setPlaybackState(int r1) {
                this.playbackState = r1;
                return this;
            }

            public Builder setPlaybackSuppressionReason(int r1) {
                this.playbackSuppressionReason = r1;
                return this;
            }

            public Builder setPlayerError(PlaybackException r1) {
                this.playerError = r1;
                return this;
            }

            public Builder setPlaylist(Timeline r2, Tracks r3, MediaMetadata r4) {
                this.playlist = null;
                this.timeline = r2;
                this.currentTracks = r3;
                this.currentMetadata = r4;
                return this;
            }

            public Builder setPlaylist(List<MediaItemData> r5) {
                HashSet r0 = new HashSet();
                int r1 = 0;
            L4:
                if (r1 >= r5.size()) goto L6;
                Assertions.checkArgument(r0.add(r5.get(r1).uid), "Duplicate MediaItemData UID in playlist");
                r1 = r1 + 1;
                goto L4
            L6:
                this.playlist = ImmutableList.copyOf(r5);
                this.timeline = new PlaylistTimeline(this.playlist);
                this.currentTracks = null;
                this.currentMetadata = null;
                return this;
            }

            public Builder setPlaylistMetadata(MediaMetadata r1) {
                this.playlistMetadata = r1;
                return this;
            }

            public Builder setPositionDiscontinuity(int r2, long r3) {
                this.hasPositionDiscontinuity = true;
                this.positionDiscontinuityReason = r2;
                this.discontinuityPositionMs = r3;
                return this;
            }

            public Builder setRepeatMode(int r1) {
                this.repeatMode = r1;
                return this;
            }

            public Builder setSeekBackIncrementMs(long r1) {
                this.seekBackIncrementMs = r1;
                return this;
            }

            public Builder setSeekForwardIncrementMs(long r1) {
                this.seekForwardIncrementMs = r1;
                return this;
            }

            public Builder setShuffleModeEnabled(boolean r1) {
                this.shuffleModeEnabled = r1;
                return this;
            }

            public Builder setSurfaceSize(Size r1) {
                this.surfaceSize = r1;
                return this;
            }

            public Builder setTimedMetadata(Metadata r1) {
                this.timedMetadata = r1;
                return this;
            }

            public Builder setTotalBufferedDurationMs(PositionSupplier r1) {
                this.totalBufferedDurationMsSupplier = r1;
                return this;
            }

            public Builder setTrackSelectionParameters(TrackSelectionParameters r1) {
                this.trackSelectionParameters = r1;
                return this;
            }

            public Builder setVideoSize(VideoSize r1) {
                this.videoSize = r1;
                return this;
            }

            public Builder setVolume(float r2) {
                if (r2 >= 0.0f) goto L5;
            L7:
                boolean r0 = false;
            L8:
                Assertions.checkArgument(r0);
                this.volume = r2;
                return this;
            L5:
                if (r2 > 1.0f) goto L7;
                r0 = true;
                goto L8
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private State(Builder r17) {
            Tracks r1 = Builder.access$200(r17);
            MediaMetadata r2 = Builder.access$300(r17);
            boolean r5 = false;
            if (Builder.access$400(r17).isEmpty() == true) goto L5;
            int r3 = Builder.access$800(r17);
            if (r3 != (-1)) goto L26;
            int r11 = 0;
        L31:
            if (Builder.access$600(r17) == (-1)) goto L48;
            Timeline.Period r15 = new Timeline.Period();
            Timeline.Window r14 = new Timeline.Window();
            if (Builder.access$900(r17) == null) goto L35;
            long r12 = Builder.access$900(r17).longValue();
        L36:
            Builder.access$400(r17).getPeriod(SimpleBasePlayer.access$1100(Builder.access$400(r17), r11, r12, r14, r15), r15);
            if (Builder.access$600(r17) >= r15.getAdGroupCount()) goto L39;
            boolean r8 = true;
        L40:
            Assertions.checkArgument(r8, "PeriodData has less ad groups than adGroupIndex");
            int r9 = r15.getAdCountInAdGroup(Builder.access$600(r17));
            if (r9 == (-1)) goto L48;
            if (Builder.access$700(r17) >= r9) goto L45;
            boolean r10 = true;
        L46:
            Assertions.checkArgument(r10, "Ad group has less ads than adIndexInGroupIndex");
            goto L48
        L45:
            r10 = false;
            goto L46
        L39:
            r8 = false;
            goto L40
        L35:
            r12 = Builder.access$1000(r17).get();
        L48:
            if (Builder.access$1200(r17) == null) goto L50;
            MediaItemData r4 = (MediaItemData) Builder.access$1200(r17).get(r11);
            r1 = r4.tracks;
            r2 = r4.mediaMetadata;
        L50:
            if (r2 != null) goto L53;
            r2 = SimpleBasePlayer.access$1300(Builder.access$400(r17).getWindow(r11, new Timeline.Window()).mediaItem, (Tracks) Assertions.checkNotNull(r1));
        L53:
            if (Builder.access$1400(r17) == null) goto L59;
            if (Builder.access$500(r17) != 1) goto L57;
            r5 = true;
        L57:
            Assertions.checkArgument(r5, "Player error only allowed in STATE_IDLE");
        L59:
            if (Builder.access$500(r17) != 1) goto L61;
        L63:
            Assertions.checkArgument(!Builder.access$1500(r17), "isLoading only allowed when not in STATE_IDLE or STATE_ENDED");
        L64:
            PositionSupplier r6 = Builder.access$1000(r17);
            if (Builder.access$900(r17) != null) goto L67;
        L78:
            PositionSupplier r7 = Builder.access$1900(r17);
            if (Builder.access$2000(r17) != null) goto L81;
        L90:
            this.availableCommands = Builder.access$2100(r17);
            this.playWhenReady = Builder.access$1600(r17);
            this.playWhenReadyChangeReason = Builder.access$2200(r17);
            this.playbackState = Builder.access$500(r17);
            this.playbackSuppressionReason = Builder.access$1700(r17);
            this.playerError = Builder.access$1400(r17);
            this.repeatMode = Builder.access$2300(r17);
            this.shuffleModeEnabled = Builder.access$2400(r17);
            this.isLoading = Builder.access$1500(r17);
            this.seekBackIncrementMs = Builder.access$2500(r17);
            this.seekForwardIncrementMs = Builder.access$2600(r17);
            this.maxSeekToPreviousPositionMs = Builder.access$2700(r17);
            this.playbackParameters = Builder.access$1800(r17);
            this.trackSelectionParameters = Builder.access$2800(r17);
            this.audioAttributes = Builder.access$2900(r17);
            this.volume = Builder.access$3000(r17);
            this.videoSize = Builder.access$3100(r17);
            this.currentCues = Builder.access$3200(r17);
            this.deviceInfo = Builder.access$3300(r17);
            this.deviceVolume = Builder.access$3400(r17);
            this.isDeviceMuted = Builder.access$3500(r17);
            this.surfaceSize = Builder.access$3600(r17);
            this.newlyRenderedFirstFrame = Builder.access$3700(r17);
            this.timedMetadata = Builder.access$3800(r17);
            this.timeline = Builder.access$400(r17);
            this.currentTracks = (Tracks) Assertions.checkNotNull(r1);
            this.currentMetadata = r2;
            this.playlistMetadata = Builder.access$3900(r17);
            this.currentMediaItemIndex = Builder.access$800(r17);
            this.currentAdGroupIndex = Builder.access$600(r17);
            this.currentAdIndexInAdGroup = Builder.access$700(r17);
            this.contentPositionMsSupplier = r6;
            this.adPositionMsSupplier = r7;
            this.contentBufferedPositionMsSupplier = Builder.access$4000(r17);
            this.adBufferedPositionMsSupplier = Builder.access$4100(r17);
            this.totalBufferedDurationMsSupplier = Builder.access$4200(r17);
            this.hasPositionDiscontinuity = Builder.access$4300(r17);
            this.positionDiscontinuityReason = Builder.access$4400(r17);
            this.discontinuityPositionMs = Builder.access$4500(r17);
            return;
        L81:
            if (Builder.access$600(r17) != (-1)) goto L83;
        L89:
            r7 = PositionSupplier.CC.getConstant(Builder.access$2000(r17).longValue());
            goto L90
        L83:
            if (Builder.access$1600(r17) == false) goto L89;
            if (Builder.access$500(r17) != 3) goto L89;
            if (Builder.access$1700(r17) != 0) goto L89;
            r7 = PositionSupplier.CC.getExtrapolating(Builder.access$2000(r17).longValue(), 1.0f);
            goto L90
        L67:
            if (Builder.access$600(r17) == (-1)) goto L69;
        L77:
            r6 = PositionSupplier.CC.getConstant(Builder.access$900(r17).longValue());
            goto L78
        L69:
            if (Builder.access$1600(r17) == false) goto L77;
            if (Builder.access$500(r17) != 3) goto L77;
            if (Builder.access$1700(r17) != 0) goto L77;
            if (Builder.access$900(r17).longValue() == C0180C.TIME_UNSET) goto L77;
            r6 = PositionSupplier.CC.getExtrapolating(Builder.access$900(r17).longValue(), Builder.access$1800(r17).speed);
            goto L78
        L61:
            if (Builder.access$500(r17) != 4) goto L64;
        L26:
            if (Builder.access$800(r17) >= Builder.access$400(r17).getWindowCount()) goto L28;
            boolean r13 = true;
        L29:
            Assertions.checkArgument(r13, "currentMediaItemIndex must be less than playlist.size()");
            r11 = r3;
            goto L31
        L28:
            r13 = false;
            goto L29
        L5:
            if (Builder.access$500(r17) != 1) goto L7;
        L10:
            boolean r16 = true;
        L11:
            Assertions.checkArgument(r16, "Empty playlist only allowed in STATE_IDLE or STATE_ENDED");
            if (Builder.access$600(r17) == (-1)) goto L14;
        L16:
            boolean r18 = false;
        L17:
            Assertions.checkArgument(r18, "Ads not allowed if playlist is empty");
            if (r1 != null) goto L20;
            r1 = Tracks.EMPTY;
        L20:
            if (r2 != null) goto L53;
            r2 = MediaMetadata.EMPTY;
            goto L53
        L14:
            if (Builder.access$700(r17) != (-1)) goto L16;
            r18 = true;
            goto L17
        L7:
            if (Builder.access$500(r17) == 4) goto L10;
            r16 = false;
            goto L11
        }

        /* synthetic */ State(Builder r1, C01941 r2) {
            this(r1);
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof State) == true) goto L8;
            return false;
        L8:
            State r1 = (State) r8;
            if (this.playWhenReady != r1.playWhenReady) goto L88;
            if (this.playWhenReadyChangeReason != r1.playWhenReadyChangeReason) goto L88;
            if (this.availableCommands.equals(r1.availableCommands) == false) goto L88;
            if (this.playbackState != r1.playbackState) goto L88;
            if (this.playbackSuppressionReason != r1.playbackSuppressionReason) goto L88;
            if (Objects.equals(this.playerError, r1.playerError) == false) goto L88;
            if (this.repeatMode != r1.repeatMode) goto L88;
            if (this.shuffleModeEnabled != r1.shuffleModeEnabled) goto L88;
            if (this.isLoading != r1.isLoading) goto L88;
            if (this.seekBackIncrementMs != r1.seekBackIncrementMs) goto L88;
            if (this.seekForwardIncrementMs != r1.seekForwardIncrementMs) goto L88;
            if (this.maxSeekToPreviousPositionMs != r1.maxSeekToPreviousPositionMs) goto L88;
            if (this.playbackParameters.equals(r1.playbackParameters) == false) goto L88;
            if (this.trackSelectionParameters.equals(r1.trackSelectionParameters) == false) goto L88;
            if (this.audioAttributes.equals(r1.audioAttributes) == false) goto L88;
            if (this.volume != r1.volume) goto L88;
            if (this.videoSize.equals(r1.videoSize) == false) goto L88;
            if (this.currentCues.equals(r1.currentCues) == false) goto L88;
            if (this.deviceInfo.equals(r1.deviceInfo) == false) goto L88;
            if (this.deviceVolume != r1.deviceVolume) goto L88;
            if (this.isDeviceMuted != r1.isDeviceMuted) goto L88;
            if (this.surfaceSize.equals(r1.surfaceSize) == false) goto L88;
            if (this.newlyRenderedFirstFrame != r1.newlyRenderedFirstFrame) goto L88;
            if (this.timedMetadata.equals(r1.timedMetadata) == false) goto L88;
            if (this.timeline.equals(r1.timeline) == false) goto L88;
            if (this.currentTracks.equals(r1.currentTracks) == false) goto L88;
            if (this.currentMetadata.equals(r1.currentMetadata) == false) goto L88;
            if (this.playlistMetadata.equals(r1.playlistMetadata) == false) goto L88;
            if (this.currentMediaItemIndex != r1.currentMediaItemIndex) goto L88;
            if (this.currentAdGroupIndex != r1.currentAdGroupIndex) goto L88;
            if (this.currentAdIndexInAdGroup != r1.currentAdIndexInAdGroup) goto L88;
            if (this.contentPositionMsSupplier.equals(r1.contentPositionMsSupplier) == false) goto L88;
            if (this.adPositionMsSupplier.equals(r1.adPositionMsSupplier) == false) goto L88;
            if (this.contentBufferedPositionMsSupplier.equals(r1.contentBufferedPositionMsSupplier) == false) goto L88;
            if (this.adBufferedPositionMsSupplier.equals(r1.adBufferedPositionMsSupplier) == false) goto L88;
            if (this.totalBufferedDurationMsSupplier.equals(r1.totalBufferedDurationMsSupplier) == false) goto L88;
            if (this.hasPositionDiscontinuity != r1.hasPositionDiscontinuity) goto L88;
            if (this.positionDiscontinuityReason != r1.positionDiscontinuityReason) goto L88;
            if (this.discontinuityPositionMs != r1.discontinuityPositionMs) goto L88;
            return true;
        L88:
            return false;
        }

        public ImmutableList<MediaItemData> getPlaylist() {
            if ((this.timeline instanceof PlaylistTimeline) == true) goto L5;
            Timeline.Window r0 = new Timeline.Window();
            Timeline.Period r1 = new Timeline.Period();
            ImmutableList.Builder r2 = ImmutableList.builderWithExpectedSize(this.timeline.getWindowCount());
            int r3 = 0;
        L8:
            if (r3 >= this.timeline.getWindowCount()) goto L11;
            r2.add(MediaItemData.access$4700(this, r3, r1, r0));
            r3 = r3 + 1;
            goto L8
        L11:
            return r2.build();
        L5:
            return PlaylistTimeline.access$000((PlaylistTimeline) this.timeline);
        }

        public int hashCode() {
            int r0 = ((((((((((7 * 31) + this.availableCommands.hashCode()) * 31) + (this.playWhenReady ? 1 : 0)) * 31) + this.playWhenReadyChangeReason) * 31) + this.playbackState) * 31) + this.playbackSuppressionReason) * 31;
            if (this.playerError != null) goto L5;
            int r2 = 0;
        L7:
            return ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((r0 + r2) * 31) + this.repeatMode) * 31) + (this.shuffleModeEnabled ? 1 : 0)) * 31) + (this.isLoading ? 1 : 0)) * 31) + ((int) (this.seekBackIncrementMs ^ (this.seekBackIncrementMs >>> 32)))) * 31) + ((int) (this.seekForwardIncrementMs ^ (this.seekForwardIncrementMs >>> 32)))) * 31) + ((int) (this.maxSeekToPreviousPositionMs ^ (this.maxSeekToPreviousPositionMs >>> 32)))) * 31) + this.playbackParameters.hashCode()) * 31) + this.trackSelectionParameters.hashCode()) * 31) + this.audioAttributes.hashCode()) * 31) + Float.floatToRawIntBits(this.volume)) * 31) + this.videoSize.hashCode()) * 31) + this.currentCues.hashCode()) * 31) + this.deviceInfo.hashCode()) * 31) + this.deviceVolume) * 31) + (this.isDeviceMuted ? 1 : 0)) * 31) + this.surfaceSize.hashCode()) * 31) + (this.newlyRenderedFirstFrame ? 1 : 0)) * 31) + this.timedMetadata.hashCode()) * 31) + this.timeline.hashCode()) * 31) + this.currentTracks.hashCode()) * 31) + this.currentMetadata.hashCode()) * 31) + this.playlistMetadata.hashCode()) * 31) + this.currentMediaItemIndex) * 31) + this.currentAdGroupIndex) * 31) + this.currentAdIndexInAdGroup) * 31) + this.contentPositionMsSupplier.hashCode()) * 31) + this.adPositionMsSupplier.hashCode()) * 31) + this.contentBufferedPositionMsSupplier.hashCode()) * 31) + this.adBufferedPositionMsSupplier.hashCode()) * 31) + this.totalBufferedDurationMsSupplier.hashCode()) * 31) + (this.hasPositionDiscontinuity ? 1 : 0)) * 31) + this.positionDiscontinuityReason) * 31) + ((int) (this.discontinuityPositionMs ^ (this.discontinuityPositionMs >>> 32)));
        L5:
            r2 = this.playerError.hashCode();
            goto L7
        }
    }

    public static /* synthetic */ void $r8$lambda$FFq8QvDrngTZgwNelLGa_12THc4(SimpleBasePlayer r0, Runnable r1) {
        r0.postOrRunOnApplicationHandler(r1);
    }

    protected SimpleBasePlayer(Looper r2) {
        this(r2, Clock.DEFAULT);
    }

    protected SimpleBasePlayer(Looper r3, Clock r4) {
        this.applicationLooper = r3;
        this.applicationHandler = r4.createHandler(r3, null);
        this.pendingOperations = new HashSet();
        this.period = new Timeline.Period();
        this.listeners = new ListenerSet(r3, r4, new SimpleBasePlayer$$ExternalSyntheticLambda38(this));
    }

    static /* synthetic */ int access$1100(Timeline r1, int r2, long r3, Timeline.Window r5, Timeline.Period r6) {
        return getPeriodIndexFromWindowPosition(r1, r2, r3, r5, r6);
    }

    static /* synthetic */ MediaMetadata access$1300(MediaItem r1, Tracks r2) {
        return getCombinedMediaMetadata(r1, r2);
    }

    static /* synthetic */ int access$6900(State r1) {
        return getCurrentMediaItemIndexInternal(r1);
    }

    private static List<MediaItemData> buildMutablePlaylistFromState(State r3, Timeline.Period r4, Timeline.Window r5) {
        if ((r3.timeline instanceof PlaylistTimeline) == true) goto L5;
        ArrayList r0 = new ArrayList(r3.timeline.getWindowCount());
        int r1 = 0;
    L8:
        if (r1 >= r3.timeline.getWindowCount()) goto L10;
        r0.add(MediaItemData.access$4700(r3, r1, r4, r5));
        r1 = r1 + 1;
        goto L8
    L10:
        return r0;
    L5:
        return new ArrayList(PlaylistTimeline.access$000((PlaylistTimeline) r3.timeline));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static State buildStateForNewPosition(State.Builder r18, State r19, long r20, Timeline r22, int r23, long r24, boolean r26, Timeline.Window r27) {
        int r3 = r23;
        long r5 = getPositionOrDefaultInMediaItem(r20, r19, r27);
        if (r22.isEmpty() == true) goto L8;
        if (r3 != (-1)) goto L6;
    L7:
        r3 = 0;
        long r9 = C0180C.TIME_UNSET;
    L10:
        if (r22.isEmpty() == false) goto L12;
    L14:
        boolean r11 = false;
        if (r19.timeline.isEmpty() == false) goto L17;
    L20:
        boolean r7 = true;
    L21:
        if (r7 == false) goto L23;
    L25:
        boolean r13 = false;
    L26:
        MediaMetadata r15 = null;
        if (r22.isEmpty() == false) goto L30;
        r18.setPlaylist(r22, Tracks.EMPTY, null);
    L42:
        if (r7 == true) goto L56;
        if (r13 == true) goto L56;
        if (r9 < r5) goto L56;
        if (r9 != r5) goto L54;
        r18.setCurrentMediaItemIndex(r3);
        if (r19.currentAdGroupIndex == (-1)) goto L53;
        if (r26 == false) goto L53;
        r18.setTotalBufferedDurationMs(PositionSupplier.CC.getConstant(r19.adBufferedPositionMsSupplier.get() - r19.adPositionMsSupplier.get()));
    L58:
        return r18.build();
    L53:
        r18.setCurrentAd(-1, -1).setTotalBufferedDurationMs(PositionSupplier.CC.getConstant(getContentBufferedPositionMsInternal(r19, r27) - r5));
        goto L58
    L54:
        long r12 = Math.max(getContentBufferedPositionMsInternal(r19, r27), r9);
        long r8 = Math.max(0, r19.totalBufferedDurationMsSupplier.get() - (r9 - r5));
        r18.setCurrentMediaItemIndex(r3).setCurrentAd(-1, -1).setContentPositionMs(r9).setContentBufferedPositionMs(PositionSupplier.CC.getConstant(r12)).setTotalBufferedDurationMs(PositionSupplier.CC.getConstant(r8));
    L56:
        r18.setCurrentMediaItemIndex(r3).setCurrentAd(-1, -1).setContentPositionMs(r9).setContentBufferedPositionMs(PositionSupplier.CC.getConstant(r9)).setTotalBufferedDurationMs(PositionSupplier.ZERO);
        goto L58
    L30:
        if ((r22 instanceof PlaylistTimeline) == false) goto L32;
        MediaItemData r14 = (MediaItemData) PlaylistTimeline.access$000((PlaylistTimeline) r22).get(r3);
        r18.setPlaylist(r22, r14.tracks, r14.mediaMetadata);
        goto L42
    L32:
        if (r7 == true) goto L36;
        if (r13 == true) goto L36;
        r11 = true;
    L36:
        if (r11 == false) goto L38;
        Tracks r16 = r19.currentTracks;
    L39:
        if (r11 == false) goto L41;
        r15 = r19.currentMetadata;
    L41:
        r18.setPlaylist(r22, r16, r15);
        goto L42
    L38:
        r16 = Tracks.EMPTY;
        goto L39
    L23:
        if (r19.timeline.getWindow(getCurrentMediaItemIndexInternal(r19), r27).uid.equals(r22.getWindow(r3, r27).uid) == true) goto L25;
        r13 = true;
        goto L26
    L17:
        if (r22.isEmpty() == true) goto L20;
        r7 = false;
        goto L21
    L12:
        if (r9 != C0180C.TIME_UNSET) goto L14;
        r9 = r22.getWindow(r3, r27).getDefaultPositionMs();
        goto L14
    L6:
        if (r3 >= r22.getWindowCount()) goto L7;
    L8:
        r9 = r24;
        goto L10
    }

    private void clearVideoOutput(Object r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(27) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleClearVideoOutput(r4), new SimpleBasePlayer$$ExternalSyntheticLambda47(r0));
    }

    private static MediaMetadata getCombinedMediaMetadata(MediaItem r8, Tracks r9) {
        MediaMetadata.Builder r0 = new MediaMetadata.Builder();
        int r1 = r9.getGroups().size();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L18;
        Tracks.Group r3 = r9.getGroups().get(r2);
        int r4 = 0;
    L6:
        if (r4 >= r3.length) goto L16;
        if (r3.isTrackSelected(r4) == false) goto L15;
        Format r5 = r3.getTrackFormat(r4);
        if (r5.metadata == null) goto L15;
        int r6 = 0;
    L13:
        if (r6 >= r5.metadata.length()) goto L15;
        r5.metadata.get(r6).populateMediaMetadata(r0);
        r6 = r6 + 1;
    L15:
        r4 = r4 + 1;
        goto L6
    L16:
        r2 = r2 + 1;
        goto L3
    L18:
        return r0.populate(r8.mediaMetadata).build();
    }

    private static long getContentBufferedPositionMsInternal(State r2, Timeline.Window r3) {
        return getPositionOrDefaultInMediaItem(r2.contentBufferedPositionMsSupplier.get(), r2, r3);
    }

    private static long getContentPositionMsInternal(State r2, Timeline.Window r3) {
        return getPositionOrDefaultInMediaItem(r2.contentPositionMsSupplier.get(), r2, r3);
    }

    private static int getCurrentMediaItemIndexInternal(State r2) {
        if (r2.currentMediaItemIndex != (-1)) goto L5;
        return 0;
    L5:
        return r2.currentMediaItemIndex;
    }

    private static int getCurrentPeriodIndexInternal(State r6, Timeline.Window r7, Timeline.Period r8) {
        int r1 = getCurrentMediaItemIndexInternal(r6);
        if (r6.timeline.isEmpty() == false) goto L6;
        return r1;
    L6:
        return getPeriodIndexFromWindowPosition(r6.timeline, r1, getContentPositionMsInternal(r6, r7), r7, r8);
    }

    private static long getCurrentPeriodOrAdPositionMs(State r4, Object r5, Timeline.Period r6, Timeline.Window r7) {
        if (r4.currentAdGroupIndex == (-1)) goto L6;
        return r4.adPositionMsSupplier.get();
    L6:
        return getContentPositionMsInternal(r4, r7) - r4.timeline.getPeriodByUid(r5, r6).getPositionInWindowMs();
    }

    private static int getMediaItemIndexInNewPlaylist(Timeline r4, Timeline r5, int r6, Timeline.Period r7, Timeline.Window r8) {
        if (r4.isEmpty() == true) goto L5;
        Object r2 = Assertions.checkNotNull(r4.getPeriod(r4.getWindow(r6, r8).firstPeriodIndex, r7, true).uid);
        if (r5.getIndexOfPeriod(r2) != (-1)) goto L12;
        return -1;
    L12:
        return r5.getPeriodByUid(r2, r7).windowIndex;
    L5:
        if (r6 < r5.getWindowCount()) goto L7;
        return -1;
    L7:
        return r6;
    }

    private static int getMediaItemTransitionReason(State r11, State r12, int r13, boolean r14, Timeline.Window r15) {
        Timeline r0 = r11.timeline;
        Timeline r1 = r12.timeline;
        if (r1.isEmpty() == false) goto L8;
        if (r0.isEmpty() == false) goto L8;
        return -1;
    L8:
        if (r1.isEmpty() == r0.isEmpty()) goto L10;
        return 3;
    L10:
        Object r2 = r11.timeline.getWindow(getCurrentMediaItemIndexInternal(r11), r15).uid;
        Object r4 = r12.timeline.getWindow(getCurrentMediaItemIndexInternal(r12), r15).uid;
        if ((r2 instanceof PlaceholderUid) == false) goto L16;
        if ((r4 instanceof PlaceholderUid) == true) goto L16;
        return -1;
    L16:
        if (r2.equals(r4) == true) goto L22;
        if (r13 != 0) goto L19;
        return 1;
    L19:
        if (r13 != 1) goto L21;
        return 2;
    L21:
        return 3;
    L22:
        if (r13 == 0) goto L24;
    L32:
        if (r13 != 1) goto L35;
        if (r14 == false) goto L35;
        return 2;
    L35:
        return -1;
    L24:
        if (getContentPositionMsInternal(r11, r15) <= getContentPositionMsInternal(r12, r15)) goto L26;
        return 0;
    L26:
        if (r12.hasPositionDiscontinuity == false) goto L32;
        if (r12.discontinuityPositionMs != C0180C.TIME_UNSET) goto L32;
        if (r14 == false) goto L32;
        return 0;
    }

    private static int getPeriodIndexFromWindowPosition(Timeline r6, int r7, long r8, Timeline.Window r10, Timeline.Period r11) {
        return r6.getIndexOfPeriod(r6.getPeriodPositionUs(r10, r11, r7, Util.msToUs(r8)).first);
    }

    private static long getPeriodOrAdDurationMs(State r4, Object r5, Timeline.Period r6) {
        r4.timeline.getPeriodByUid(r5, r6);
        if (r4.currentAdGroupIndex != (-1)) goto L5;
        long r0 = r6.durationUs;
    L7:
        return Util.usToMs(r0);
    L5:
        r0 = r6.getAdDurationUs(r4.currentAdGroupIndex, r4.currentAdIndexInAdGroup);
        goto L7
    }

    private static int getPositionDiscontinuityReason(State r20, State r21, boolean r22, Timeline.Window r23, Timeline.Period r24) {
        if (r21.hasPositionDiscontinuity == true) goto L5;
        if (r22 == false) goto L10;
        return 1;
    L10:
        if (r20.timeline.isEmpty() == false) goto L13;
        return -1;
    L13:
        if (r21.timeline.isEmpty() == false) goto L15;
        return 4;
    L15:
        Object r4 = r20.timeline.getUidOfPeriod(getCurrentPeriodIndexInternal(r20, r23, r24));
        Object r7 = r21.timeline.getUidOfPeriod(getCurrentPeriodIndexInternal(r21, r23, r24));
        if ((r4 instanceof PlaceholderUid) == false) goto L21;
        if ((r7 instanceof PlaceholderUid) == true) goto L21;
        return -1;
    L21:
        if (r7.equals(r4) == false) goto L38;
        if (r20.currentAdGroupIndex != r21.currentAdGroupIndex) goto L38;
        if (r20.currentAdIndexInAdGroup != r21.currentAdIndexInAdGroup) goto L38;
        long r12 = getCurrentPeriodOrAdPositionMs(r20, r4, r24, r23);
        if (Math.abs(r12 - getCurrentPeriodOrAdPositionMs(r21, r7, r24, r23)) >= 1000) goto L30;
        return -1;
    L30:
        long r5 = getPeriodOrAdDurationMs(r20, r4, r24);
        if (r5 != C0180C.TIME_UNSET) goto L33;
    L35:
        return 5;
    L33:
        if (r12 < r5) goto L35;
        return 0;
    L38:
        if (r21.timeline.getIndexOfPeriod(r4) != (-1)) goto L40;
        return 4;
    L40:
        long r6 = getCurrentPeriodOrAdPositionMs(r20, r4, r24, r23);
        long r13 = getPeriodOrAdDurationMs(r20, r4, r24);
        if (r13 != C0180C.TIME_UNSET) goto L43;
    L45:
        return 3;
    L43:
        if (r6 < r13) goto L45;
        return 0;
    L5:
        return r21.positionDiscontinuityReason;
    }

    private static Player.PositionInfo getPositionInfo(State r17, boolean r18, Timeline.Window r19, Timeline.Period r20) {
        int r7 = getCurrentMediaItemIndexInternal(r17);
        if (r17.timeline.isEmpty() == true) goto L5;
        int r5 = getCurrentPeriodIndexInternal(r17, r19, r20);
        Object r4 = r17.timeline.getPeriod(r5, r20, true).uid;
        Object r3 = r17.timeline.getWindow(r7, r19).uid;
        Object r9 = r4;
        int r10 = r5;
        MediaItem r8 = r19.mediaItem;
        Object r6 = r3;
    L7:
        if (r18 == false) goto L13;
        long r11 = r17.discontinuityPositionMs;
        if (r17.currentAdGroupIndex != (-1)) goto L11;
        long r12 = r11;
    L12:
        long r13 = r12;
        long r14 = r11;
    L19:
        return new Player.PositionInfo(r6, r7, r8, r9, r10, r14, r13, r17.currentAdGroupIndex, r17.currentAdIndexInAdGroup);
    L11:
        r12 = getContentPositionMsInternal(r17, r19);
        goto L12
    L13:
        long r15 = getContentPositionMsInternal(r17, r19);
        if (r17.currentAdGroupIndex == (-1)) goto L16;
        long r16 = r17.adPositionMsSupplier.get();
    L17:
        r13 = r15;
        r14 = r16;
        goto L19
    L16:
        r16 = r15;
        goto L17
    L5:
        r9 = null;
        r10 = -1;
        r8 = null;
        r6 = null;
        goto L7
    }

    private static long getPositionOrDefaultInMediaItem(long r2, State r4, Timeline.Window r5) {
        if (r2 == C0180C.TIME_UNSET) goto L6;
        return r2;
    L6:
        if (r4.timeline.isEmpty() == false) goto L10;
        return 0;
    L10:
        return r4.timeline.getWindow(getCurrentMediaItemIndexInternal(r4), r5).getDefaultPositionMs();
    }

    private static State getStateWithNewPlaylist(State r12, List<MediaItemData> r13, Timeline.Period r14, Timeline.Window r15) {
        State.Builder r0 = r12.buildUpon();
        PlaylistTimeline r4 = new PlaylistTimeline(r13);
        Timeline r10 = r12.timeline;
        long r2 = r12.contentPositionMsSupplier.get();
        int r11 = getCurrentMediaItemIndexInternal(r12);
        int r1 = getMediaItemIndexInNewPlaylist(r10, r4, r11, r14, r15);
        if (r1 != (-1)) goto L5;
        long r6 = C0180C.TIME_UNSET;
    L6:
        int r8 = r11 + 1;
    L7:
        if (r1 != (-1)) goto L12;
        if (r8 >= r10.getWindowCount()) goto L12;
        r1 = getMediaItemIndexInNewPlaylist(r10, r4, r8, r14, r15);
        r8 = r8 + 1;
    L12:
        if (r12.playbackState == 1) goto L16;
        if (r1 != (-1)) goto L16;
        r0.setPlaybackState(4).setIsLoading(false);
    L16:
        return buildStateForNewPosition(r0, r12, r2, r4, r1, r6, true, r15);
    L5:
        r6 = r2;
        goto L6
    }

    private static State getStateWithNewPlaylistAndPosition(State r10, List<MediaItemData> r11, int r12, long r13, Timeline.Window r15) {
        State.Builder r0 = r10.buildUpon();
        if (r11 != null) goto L5;
        Timeline r1 = r10.timeline;
    L6:
        Timeline r4 = r1;
        if (r10.playbackState == 1) goto L18;
        if (r4.isEmpty() == false) goto L11;
    L16:
        r0.setPlaybackState(4).setIsLoading(false);
        goto L18
    L11:
        if (r12 != (-1)) goto L13;
    L15:
        r0.setPlaybackState(2);
        goto L18
    L13:
        if (r12 < r4.getWindowCount()) goto L15;
    L18:
        return buildStateForNewPosition(r0, r10, r10.contentPositionMsSupplier.get(), r4, r12, r13, false, r15);
    L5:
        r1 = new PlaylistTimeline(r11);
        goto L6
    }

    private static Size getSurfaceHolderSize(SurfaceHolder r4) {
        if (r4.getSurface().isValid() == false) goto L5;
        Rect r0 = r4.getSurfaceFrame();
        return new Size(r0.width(), r0.height());
    L5:
        return Size.ZERO;
    }

    private static int getTimelineChangeReason(Timeline r6, Timeline r7, Timeline.Window r8) {
        if (r6.getWindowCount() == r7.getWindowCount()) goto L5;
        return 0;
    L5:
        int r0 = 0;
    L6:
        boolean r3 = true;
        if (r0 >= r6.getWindowCount()) goto L19;
        Object r1 = r6.getWindow(r0, r8).uid;
        Object r4 = r7.getWindow(r0, r8).uid;
        if ((r1 instanceof PlaceholderUid) == true) goto L11;
    L13:
        r3 = false;
    L15:
        if (r1.equals(r4) == true) goto L18;
        if (r3 == true) goto L18;
        return 0;
    L18:
        r0 = r0 + 1;
        goto L6
    L11:
        if ((r4 instanceof PlaceholderUid) == true) goto L13;
    L19:
        return 1;
    }

    private static boolean isPlaying(State r2) {
        if (r2.playWhenReady == true) goto L5;
    L9:
        return false;
    L5:
        if (r2.playbackState != 3) goto L9;
        if (r2.playbackSuppressionReason != 0) goto L9;
        return true;
    }

    static /* synthetic */ State lambda$clearVideoOutput$21(State r2) {
        return r2.buildUpon().setSurfaceSize(Size.ZERO).build();
    }

    static /* synthetic */ State lambda$decreaseDeviceVolume$26(State r3) {
        return r3.buildUpon().setDeviceVolume(Math.max(0, r3.deviceVolume - 1)).build();
    }

    static /* synthetic */ State lambda$decreaseDeviceVolume$27(State r3) {
        return r3.buildUpon().setDeviceVolume(Math.max(0, r3.deviceVolume - 1)).build();
    }

    static /* synthetic */ ListenableFuture lambda$handleReplaceMediaItems$31(ListenableFuture r0, Object r1) throws Exception {
        return r0;
    }

    static /* synthetic */ State lambda$increaseDeviceVolume$24(State r2) {
        return r2.buildUpon().setDeviceVolume(r2.deviceVolume + 1).build();
    }

    static /* synthetic */ State lambda$increaseDeviceVolume$25(State r2) {
        return r2.buildUpon().setDeviceVolume(r2.deviceVolume + 1).build();
    }

    static /* synthetic */ State lambda$prepare$7(State r2) {
        State.Builder r0 = r2.buildUpon().setPlayerError(null);
        if (r2.timeline.isEmpty() == false) goto L5;
        int r1 = 4;
    L7:
        return r0.setPlaybackState(r1).build();
    L5:
        r1 = 2;
        goto L7
    }

    static /* synthetic */ State lambda$release$13(State r0) {
        return r0;
    }

    static /* synthetic */ State lambda$setAudioAttributes$30(State r1, AudioAttributes r2) {
        return r1.buildUpon().setAudioAttributes(r2).build();
    }

    static /* synthetic */ State lambda$setDeviceMuted$28(State r1, boolean r2) {
        return r1.buildUpon().setIsDeviceMuted(r2).build();
    }

    static /* synthetic */ State lambda$setDeviceMuted$29(State r1, boolean r2) {
        return r1.buildUpon().setIsDeviceMuted(r2).build();
    }

    static /* synthetic */ State lambda$setDeviceVolume$22(State r1, int r2) {
        return r1.buildUpon().setDeviceVolume(r2).build();
    }

    static /* synthetic */ State lambda$setDeviceVolume$23(State r1, int r2) {
        return r1.buildUpon().setDeviceVolume(r2).build();
    }

    static /* synthetic */ State lambda$setPlayWhenReady$1(State r2, boolean r3) {
        return r2.buildUpon().setPlayWhenReady(r3, 1).build();
    }

    static /* synthetic */ State lambda$setPlaybackParameters$11(State r1, PlaybackParameters r2) {
        return r1.buildUpon().setPlaybackParameters(r2).build();
    }

    static /* synthetic */ State lambda$setPlaylistMetadata$15(State r1, MediaMetadata r2) {
        return r1.buildUpon().setPlaylistMetadata(r2).build();
    }

    static /* synthetic */ State lambda$setRepeatMode$8(State r1, int r2) {
        return r1.buildUpon().setRepeatMode(r2).build();
    }

    static /* synthetic */ State lambda$setShuffleModeEnabled$9(State r1, boolean r2) {
        return r1.buildUpon().setShuffleModeEnabled(r2).build();
    }

    static /* synthetic */ State lambda$setTrackSelectionParameters$14(State r1, TrackSelectionParameters r2) {
        return r1.buildUpon().setTrackSelectionParameters(r2).build();
    }

    static /* synthetic */ State lambda$setVideoSurface$17(State r2) {
        return r2.buildUpon().setSurfaceSize(Size.UNKNOWN).build();
    }

    static /* synthetic */ State lambda$setVideoSurfaceHolder$18(State r2, SurfaceHolder r3) {
        return r2.buildUpon().setSurfaceSize(getSurfaceHolderSize(r3)).build();
    }

    static /* synthetic */ State lambda$setVideoSurfaceView$19(State r2, SurfaceView r3) {
        return r2.buildUpon().setSurfaceSize(getSurfaceHolderSize(r3.getHolder())).build();
    }

    static /* synthetic */ State lambda$setVideoTextureView$20(State r1, Size r2) {
        return r1.buildUpon().setSurfaceSize(r2).build();
    }

    static /* synthetic */ State lambda$setVolume$16(State r1, float r2) {
        return r1.buildUpon().setVolume(r2).build();
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$32(State r1, int r2, Player.Listener r3) {
        r3.onTimelineChanged(r1.timeline, r2);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$33(int r0, Player.PositionInfo r1, Player.PositionInfo r2, Player.Listener r3) {
        r3.onPositionDiscontinuity(r0);
        r3.onPositionDiscontinuity(r1, r2, r0);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$34(MediaItem r0, int r1, Player.Listener r2) {
        r2.onMediaItemTransition(r0, r1);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$35(State r1, Player.Listener r2) {
        r2.onPlayerErrorChanged(r1.playerError);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$36(State r1, Player.Listener r2) {
        r2.onPlayerError((PlaybackException) Util.castNonNull(r1.playerError));
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$37(State r1, Player.Listener r2) {
        r2.onTrackSelectionParametersChanged(r1.trackSelectionParameters);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$38(State r1, Player.Listener r2) {
        r2.onTracksChanged(r1.currentTracks);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$39(State r1, Player.Listener r2) {
        r2.onMediaMetadataChanged(r1.currentMetadata);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$40(State r1, Player.Listener r2) {
        r2.onLoadingChanged(r1.isLoading);
        r2.onIsLoadingChanged(r1.isLoading);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$41(State r2, Player.Listener r3) {
        r3.onPlayerStateChanged(r2.playWhenReady, r2.playbackState);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$42(State r1, Player.Listener r2) {
        r2.onPlaybackStateChanged(r1.playbackState);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$43(State r2, Player.Listener r3) {
        r3.onPlayWhenReadyChanged(r2.playWhenReady, r2.playWhenReadyChangeReason);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$44(State r1, Player.Listener r2) {
        r2.onPlaybackSuppressionReasonChanged(r1.playbackSuppressionReason);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$45(State r1, Player.Listener r2) {
        r2.onIsPlayingChanged(isPlaying(r1));
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$46(State r1, Player.Listener r2) {
        r2.onPlaybackParametersChanged(r1.playbackParameters);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$47(State r1, Player.Listener r2) {
        r2.onRepeatModeChanged(r1.repeatMode);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$48(State r1, Player.Listener r2) {
        r2.onShuffleModeEnabledChanged(r1.shuffleModeEnabled);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$49(State r2, Player.Listener r3) {
        r3.onSeekBackIncrementChanged(r2.seekBackIncrementMs);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$50(State r2, Player.Listener r3) {
        r3.onSeekForwardIncrementChanged(r2.seekForwardIncrementMs);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$51(State r2, Player.Listener r3) {
        r3.onMaxSeekToPreviousPositionChanged(r2.maxSeekToPreviousPositionMs);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$52(State r1, Player.Listener r2) {
        r2.onAudioAttributesChanged(r1.audioAttributes);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$53(State r1, Player.Listener r2) {
        r2.onVideoSizeChanged(r1.videoSize);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$54(State r1, Player.Listener r2) {
        r2.onDeviceInfoChanged(r1.deviceInfo);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$55(State r1, Player.Listener r2) {
        r2.onPlaylistMetadataChanged(r1.playlistMetadata);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$56(State r2, Player.Listener r3) {
        r3.onSurfaceSizeChanged(r2.surfaceSize.getWidth(), r2.surfaceSize.getHeight());
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$57(State r1, Player.Listener r2) {
        r2.onVolumeChanged(r1.volume);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$58(State r2, Player.Listener r3) {
        r3.onDeviceVolumeChanged(r2.deviceVolume, r2.isDeviceMuted);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$59(State r1, Player.Listener r2) {
        r2.onCues(r1.currentCues.cues);
        r2.onCues(r1.currentCues);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$60(State r1, Player.Listener r2) {
        r2.onMetadata(r1.timedMetadata);
    }

    static /* synthetic */ void lambda$updateStateAndInformListeners$61(State r1, Player.Listener r2) {
        r2.onAvailableCommandsChanged(r1.availableCommands);
    }

    private void postOrRunOnApplicationHandler(Runnable r3) {
        if (this.applicationHandler.getLooper() != Looper.myLooper()) goto L5;
        r3.run();
        return;
    L5:
        this.applicationHandler.post(r3);
    }

    @RequiresNonNull({"state"})
    private void setMediaItemsInternal(final List<MediaItem> r10, final int r11, final long r12) {
        if (r11 == (-1)) goto L7;
        if (r11 >= 0) goto L7;
        boolean r0 = false;
    L8:
        Assertions.checkArgument(r0);
        final State r5 = this.state;
        if (shouldHandleCommand(20) == false) goto L11;
    L15:
        updateStateForPendingOperation(handleSetMediaItems(r10, r11, r12), new SimpleBasePlayer$$ExternalSyntheticLambda28(this, r10, r5, r11, r12));
        return;
    L11:
        if (r10.size() == 1) goto L13;
        return;
    L13:
        if (shouldHandleCommand(31) == true) goto L15;
        return;
    L7:
        r0 = true;
        goto L8
    }

    @RequiresNonNull({"state"})
    private boolean shouldHandleCommand(int r2) {
        if (this.released == false) goto L5;
    L7:
        return false;
    L5:
        if (this.state.availableCommands.contains(r2) == false) goto L7;
        return true;
    }

    @RequiresNonNull({"state"})
    private void updateStateAndInformListeners(final State r17, boolean r18, boolean r19) {
        State r2 = this.state;
        this.state = r17;
        if (r17.hasPositionDiscontinuity == false) goto L5;
    L6:
        this.state = this.state.buildUpon().clearPositionDiscontinuity().setNewlyRenderedFirstFrame(false).build();
    L8:
        if (r2.playWhenReady == r17.playWhenReady) goto L10;
        boolean r3 = true;
    L12:
        if (r2.playbackState == r17.playbackState) goto L14;
        boolean r5 = true;
    L15:
        final int r7 = getPositionDiscontinuityReason(r2, r17, r18, this.window, this.period);
        boolean r8 = !r2.timeline.equals(r17.timeline);
        final int r10 = getMediaItemTransitionReason(r2, r17, r7, r19, this.window);
        if (r8 == false) goto L19;
        final int r12 = getTimelineChangeReason(r2.timeline, r17.timeline, this.window);
        this.listeners.queueEvent(0, new SimpleBasePlayer$$ExternalSyntheticLambda33(r17, r12));
    L19:
        if (r7 == (-1)) goto L21;
        final Player.PositionInfo r4 = getPositionInfo(r2, false, this.window, this.period);
        final Player.PositionInfo r13 = getPositionInfo(r17, r17.hasPositionDiscontinuity, this.window, this.period);
        this.listeners.queueEvent(11, new SimpleBasePlayer$$ExternalSyntheticLambda5(r7, r4, r13));
    L21:
        if (r10 == (-1)) goto L28;
        if (r17.timeline.isEmpty() == false) goto L25;
        final MediaItem r6 = null;
    L26:
        this.listeners.queueEvent(1, new SimpleBasePlayer$$ExternalSyntheticLambda17(r6, r10));
        goto L28
    L25:
        r6 = r17.timeline.getWindow(getCurrentMediaItemIndexInternal(r17), this.window).mediaItem;
    L28:
        if (Util.areEqual(r2.playerError, r17.playerError) == true) goto L33;
        this.listeners.queueEvent(10, new SimpleBasePlayer$$ExternalSyntheticLambda19(r17));
        if (r17.playerError == null) goto L33;
        this.listeners.queueEvent(10, new SimpleBasePlayer$$ExternalSyntheticLambda20(r17));
    L33:
        if (r2.trackSelectionParameters.equals(r17.trackSelectionParameters) == true) goto L36;
        this.listeners.queueEvent(19, new SimpleBasePlayer$$ExternalSyntheticLambda21(r17));
    L36:
        if (r2.currentTracks.equals(r17.currentTracks) == true) goto L39;
        this.listeners.queueEvent(2, new SimpleBasePlayer$$ExternalSyntheticLambda23(r17));
    L39:
        if (r2.currentMetadata.equals(r17.currentMetadata) == true) goto L42;
        this.listeners.queueEvent(14, new SimpleBasePlayer$$ExternalSyntheticLambda24(r17));
    L42:
        if (r2.isLoading == r17.isLoading) goto L44;
        this.listeners.queueEvent(3, new SimpleBasePlayer$$ExternalSyntheticLambda25(r17));
    L44:
        if (r3 == true) goto L46;
        if (r5 == true) goto L46;
    L47:
        if (r5 == false) goto L49;
        this.listeners.queueEvent(4, new SimpleBasePlayer$$ExternalSyntheticLambda44(r17));
    L49:
        if (r3 == false) goto L51;
    L52:
        this.listeners.queueEvent(5, new SimpleBasePlayer$$ExternalSyntheticLambda55(r17));
    L54:
        if (r2.playbackSuppressionReason == r17.playbackSuppressionReason) goto L57;
        this.listeners.queueEvent(6, new SimpleBasePlayer$$ExternalSyntheticLambda61(r17));
    L57:
        if (isPlaying(r2) == isPlaying(r17)) goto L60;
        this.listeners.queueEvent(7, new SimpleBasePlayer$$ExternalSyntheticLambda62(r17));
    L60:
        if (r2.playbackParameters.equals(r17.playbackParameters) == true) goto L63;
        this.listeners.queueEvent(12, new SimpleBasePlayer$$ExternalSyntheticLambda63(r17));
    L63:
        if (r2.repeatMode == r17.repeatMode) goto L66;
        this.listeners.queueEvent(8, new SimpleBasePlayer$$ExternalSyntheticLambda64(r17));
    L66:
        if (r2.shuffleModeEnabled == r17.shuffleModeEnabled) goto L69;
        this.listeners.queueEvent(9, new SimpleBasePlayer$$ExternalSyntheticLambda1(r17));
    L69:
        if (r2.seekBackIncrementMs == r17.seekBackIncrementMs) goto L72;
        this.listeners.queueEvent(16, new SimpleBasePlayer$$ExternalSyntheticLambda2(r17));
    L72:
        if (r2.seekForwardIncrementMs == r17.seekForwardIncrementMs) goto L75;
        this.listeners.queueEvent(17, new SimpleBasePlayer$$ExternalSyntheticLambda3(r17));
    L75:
        if (r2.maxSeekToPreviousPositionMs == r17.maxSeekToPreviousPositionMs) goto L78;
        this.listeners.queueEvent(18, new SimpleBasePlayer$$ExternalSyntheticLambda4(r17));
    L78:
        if (r2.audioAttributes.equals(r17.audioAttributes) == true) goto L81;
        this.listeners.queueEvent(20, new SimpleBasePlayer$$ExternalSyntheticLambda6(r17));
    L81:
        if (r2.videoSize.equals(r17.videoSize) == true) goto L84;
        this.listeners.queueEvent(25, new SimpleBasePlayer$$ExternalSyntheticLambda7(r17));
    L84:
        if (r2.deviceInfo.equals(r17.deviceInfo) == true) goto L87;
        this.listeners.queueEvent(29, new SimpleBasePlayer$$ExternalSyntheticLambda8(r17));
    L87:
        if (r2.playlistMetadata.equals(r17.playlistMetadata) == true) goto L90;
        this.listeners.queueEvent(15, new SimpleBasePlayer$$ExternalSyntheticLambda9(r17));
    L90:
        if (r17.newlyRenderedFirstFrame == false) goto L93;
        this.listeners.queueEvent(26, new SimpleBasePlayer$$ExternalSyntheticLambda10());
    L93:
        if (r2.surfaceSize.equals(r17.surfaceSize) == true) goto L96;
        this.listeners.queueEvent(24, new SimpleBasePlayer$$ExternalSyntheticLambda12(r17));
    L96:
        if (r2.volume == r17.volume) goto L99;
        this.listeners.queueEvent(22, new SimpleBasePlayer$$ExternalSyntheticLambda13(r17));
    L99:
        if (r2.deviceVolume == r17.deviceVolume) goto L101;
    L102:
        this.listeners.queueEvent(30, new SimpleBasePlayer$$ExternalSyntheticLambda14(r17));
    L104:
        if (r2.currentCues.equals(r17.currentCues) == true) goto L107;
        this.listeners.queueEvent(27, new SimpleBasePlayer$$ExternalSyntheticLambda15(r17));
    L107:
        if (r2.timedMetadata.equals(r17.timedMetadata) == true) goto L112;
        if (r17.timedMetadata.presentationTimeUs == C0180C.TIME_UNSET) goto L112;
        this.listeners.queueEvent(28, new SimpleBasePlayer$$ExternalSyntheticLambda16(r17));
    L112:
        if (r2.availableCommands.equals(r17.availableCommands) == true) goto L114;
        this.listeners.queueEvent(13, new SimpleBasePlayer$$ExternalSyntheticLambda18(r17));
    L114:
        this.listeners.flushEvents();
        return;
    L101:
        if (r2.isDeviceMuted == r17.isDeviceMuted) goto L104;
    L51:
        if (r2.playWhenReadyChangeReason == r17.playWhenReadyChangeReason) goto L54;
    L46:
        this.listeners.queueEvent(-1, new SimpleBasePlayer$$ExternalSyntheticLambda26(r17));
        goto L47
    L14:
        r5 = false;
        goto L15
    L10:
        r3 = false;
        goto L12
    L5:
        if (r17.newlyRenderedFirstFrame == false) goto L8;
        goto L6
    }

    @RequiresNonNull({"state"})
    private void updateStateForPendingOperation(ListenableFuture<?> r2, Supplier<State> r3) {
        updateStateForPendingOperation(r2, r3, false, false);
    }

    @RequiresNonNull({"state"})
    private void updateStateForPendingOperation(final ListenableFuture<?> r4, Supplier<State> r5, boolean r6, boolean r7) {
        if (r4.isDone() == true) goto L5;
    L7:
        this.pendingOperations.add(r4);
        updateStateAndInformListeners(getPlaceholderState(r5.get()), r6, r7);
        r4.addListener(new SimpleBasePlayer$$ExternalSyntheticLambda30(this, r4), new SimpleBasePlayer$$ExternalSyntheticLambda31(this));
        return;
    L5:
        if (this.pendingOperations.isEmpty() == false) goto L7;
        updateStateAndInformListeners(getState(), r6, r7);
    }

    @EnsuresNonNull({"state"})
    private void verifyApplicationThreadAndInitState() {
        verifyApplicationThread();
        if (this.state != null) goto L6;
        this.state = getState();
        return;
    }

    @Override // androidx.media3.common.Player
    public final void addListener(Player.Listener r3) {
        this.listeners.add((Player.Listener) Assertions.checkNotNull(r3));
    }

    @Override // androidx.media3.common.Player
    public final void addMediaItems(int r6, final List<MediaItem> r7) {
        verifyApplicationThreadAndInitState();
        if (r6 < 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        final State r1 = this.state;
        int r2 = r1.timeline.getWindowCount();
        if (shouldHandleCommand(20) == true) goto L9;
        return;
    L9:
        if (r7.isEmpty() == true) goto L14;
        final int r3 = Math.min(r6, r2);
        updateStateForPendingOperation(handleAddMediaItems(r3, r7), new SimpleBasePlayer$$ExternalSyntheticLambda34(this, r1, r7, r3));
        return;
    L14:
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.common.Player
    public final void clearVideoSurface() {
        clearVideoOutput(null);
    }

    @Override // androidx.media3.common.Player
    public final void clearVideoSurface(Surface r1) {
        clearVideoOutput(r1);
    }

    @Override // androidx.media3.common.Player
    public final void clearVideoSurfaceHolder(SurfaceHolder r1) {
        clearVideoOutput(r1);
    }

    @Override // androidx.media3.common.Player
    public final void clearVideoSurfaceView(SurfaceView r1) {
        clearVideoOutput(r1);
    }

    @Override // androidx.media3.common.Player
    public final void clearVideoTextureView(TextureView r1) {
        clearVideoOutput(r1);
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void decreaseDeviceVolume() {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(26) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleDecreaseDeviceVolume(1), new SimpleBasePlayer$$ExternalSyntheticLambda37(r0));
    }

    @Override // androidx.media3.common.Player
    public final void decreaseDeviceVolume(int r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(34) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleDecreaseDeviceVolume(r4), new SimpleBasePlayer$$ExternalSyntheticLambda57(r0));
    }

    @Override // androidx.media3.common.Player
    public final Looper getApplicationLooper() {
        return this.applicationLooper;
    }

    @Override // androidx.media3.common.Player
    public final AudioAttributes getAudioAttributes() {
        verifyApplicationThreadAndInitState();
        return this.state.audioAttributes;
    }

    @Override // androidx.media3.common.Player
    public final Player.Commands getAvailableCommands() {
        verifyApplicationThreadAndInitState();
        return this.state.availableCommands;
    }

    @Override // androidx.media3.common.Player
    public final long getBufferedPosition() {
        verifyApplicationThreadAndInitState();
        if (isPlayingAd() == false) goto L6;
        return Math.max(this.state.adBufferedPositionMsSupplier.get(), this.state.adPositionMsSupplier.get());
    L6:
        return getContentBufferedPosition();
    }

    @Override // androidx.media3.common.Player
    public final long getContentBufferedPosition() {
        verifyApplicationThreadAndInitState();
        return Math.max(getContentBufferedPositionMsInternal(this.state, this.window), getContentPositionMsInternal(this.state, this.window));
    }

    @Override // androidx.media3.common.Player
    public final long getContentPosition() {
        verifyApplicationThreadAndInitState();
        return getContentPositionMsInternal(this.state, this.window);
    }

    @Override // androidx.media3.common.Player
    public final int getCurrentAdGroupIndex() {
        verifyApplicationThreadAndInitState();
        return this.state.currentAdGroupIndex;
    }

    @Override // androidx.media3.common.Player
    public final int getCurrentAdIndexInAdGroup() {
        verifyApplicationThreadAndInitState();
        return this.state.currentAdIndexInAdGroup;
    }

    @Override // androidx.media3.common.Player
    public final CueGroup getCurrentCues() {
        verifyApplicationThreadAndInitState();
        return this.state.currentCues;
    }

    @Override // androidx.media3.common.Player
    public final int getCurrentMediaItemIndex() {
        verifyApplicationThreadAndInitState();
        return getCurrentMediaItemIndexInternal(this.state);
    }

    @Override // androidx.media3.common.Player
    public final int getCurrentPeriodIndex() {
        verifyApplicationThreadAndInitState();
        return getCurrentPeriodIndexInternal(this.state, this.window, this.period);
    }

    @Override // androidx.media3.common.Player
    public final long getCurrentPosition() {
        verifyApplicationThreadAndInitState();
        if (isPlayingAd() == false) goto L6;
        return this.state.adPositionMsSupplier.get();
    L6:
        return getContentPosition();
    }

    @Override // androidx.media3.common.Player
    public final Timeline getCurrentTimeline() {
        verifyApplicationThreadAndInitState();
        return this.state.timeline;
    }

    @Override // androidx.media3.common.Player
    public final Tracks getCurrentTracks() {
        verifyApplicationThreadAndInitState();
        return this.state.currentTracks;
    }

    @Override // androidx.media3.common.Player
    public final DeviceInfo getDeviceInfo() {
        verifyApplicationThreadAndInitState();
        return this.state.deviceInfo;
    }

    @Override // androidx.media3.common.Player
    public final int getDeviceVolume() {
        verifyApplicationThreadAndInitState();
        return this.state.deviceVolume;
    }

    @Override // androidx.media3.common.Player
    public final long getDuration() {
        verifyApplicationThreadAndInitState();
        if (isPlayingAd() == false) goto L7;
        this.state.timeline.getPeriod(getCurrentPeriodIndex(), this.period);
        return Util.usToMs(this.period.getAdDurationUs(this.state.currentAdGroupIndex, this.state.currentAdIndexInAdGroup));
    L7:
        return getContentDuration();
    }

    @Override // androidx.media3.common.Player
    public final long getMaxSeekToPreviousPosition() {
        verifyApplicationThreadAndInitState();
        return this.state.maxSeekToPreviousPositionMs;
    }

    @Override // androidx.media3.common.Player
    public final MediaMetadata getMediaMetadata() {
        verifyApplicationThreadAndInitState();
        return this.state.currentMetadata;
    }

    protected MediaItemData getPlaceholderMediaItemData(MediaItem r4) {
        return new MediaItemData.Builder(new PlaceholderUid(null)).setMediaItem(r4).setIsDynamic(true).setIsPlaceholder(true).build();
    }

    protected State getPlaceholderState(State r1) {
        return r1;
    }

    @Override // androidx.media3.common.Player
    public final boolean getPlayWhenReady() {
        verifyApplicationThreadAndInitState();
        return this.state.playWhenReady;
    }

    @Override // androidx.media3.common.Player
    public final PlaybackParameters getPlaybackParameters() {
        verifyApplicationThreadAndInitState();
        return this.state.playbackParameters;
    }

    @Override // androidx.media3.common.Player
    public final int getPlaybackState() {
        verifyApplicationThreadAndInitState();
        return this.state.playbackState;
    }

    @Override // androidx.media3.common.Player
    public final int getPlaybackSuppressionReason() {
        verifyApplicationThreadAndInitState();
        return this.state.playbackSuppressionReason;
    }

    @Override // androidx.media3.common.Player
    public final PlaybackException getPlayerError() {
        verifyApplicationThreadAndInitState();
        return this.state.playerError;
    }

    @Override // androidx.media3.common.Player
    public final MediaMetadata getPlaylistMetadata() {
        verifyApplicationThreadAndInitState();
        return this.state.playlistMetadata;
    }

    @Override // androidx.media3.common.Player
    public final int getRepeatMode() {
        verifyApplicationThreadAndInitState();
        return this.state.repeatMode;
    }

    @Override // androidx.media3.common.Player
    public final long getSeekBackIncrement() {
        verifyApplicationThreadAndInitState();
        return this.state.seekBackIncrementMs;
    }

    @Override // androidx.media3.common.Player
    public final long getSeekForwardIncrement() {
        verifyApplicationThreadAndInitState();
        return this.state.seekForwardIncrementMs;
    }

    @Override // androidx.media3.common.Player
    public final boolean getShuffleModeEnabled() {
        verifyApplicationThreadAndInitState();
        return this.state.shuffleModeEnabled;
    }

    protected abstract State getState();

    @Override // androidx.media3.common.Player
    public final Size getSurfaceSize() {
        verifyApplicationThreadAndInitState();
        return this.state.surfaceSize;
    }

    @Override // androidx.media3.common.Player
    public final long getTotalBufferedDuration() {
        verifyApplicationThreadAndInitState();
        return this.state.totalBufferedDurationMsSupplier.get();
    }

    @Override // androidx.media3.common.Player
    public final TrackSelectionParameters getTrackSelectionParameters() {
        verifyApplicationThreadAndInitState();
        return this.state.trackSelectionParameters;
    }

    @Override // androidx.media3.common.Player
    public final VideoSize getVideoSize() {
        verifyApplicationThreadAndInitState();
        return this.state.videoSize;
    }

    @Override // androidx.media3.common.Player
    public final float getVolume() {
        verifyApplicationThreadAndInitState();
        return this.state.volume;
    }

    protected ListenableFuture<?> handleAddMediaItems(int r3, List<MediaItem> r4) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_CHANGE_MEDIA_ITEMS");
    }

    protected ListenableFuture<?> handleClearVideoOutput(Object r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_VIDEO_SURFACE");
    }

    protected ListenableFuture<?> handleDecreaseDeviceVolume(int r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_ADJUST_DEVICE_VOLUME or COMMAND_ADJUST_DEVICE_VOLUME_WITH_FLAGS");
    }

    protected ListenableFuture<?> handleIncreaseDeviceVolume(int r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_ADJUST_DEVICE_VOLUME or COMMAND_ADJUST_DEVICE_VOLUME_WITH_FLAGS");
    }

    protected ListenableFuture<?> handleMoveMediaItems(int r3, int r4, int r5) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_CHANGE_MEDIA_ITEMS");
    }

    protected ListenableFuture<?> handlePrepare() {
        throw new IllegalStateException("Missing implementation to handle COMMAND_PREPARE");
    }

    protected ListenableFuture<?> handleRelease() {
        throw new IllegalStateException("Missing implementation to handle COMMAND_RELEASE");
    }

    protected ListenableFuture<?> handleRemoveMediaItems(int r3, int r4) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_CHANGE_MEDIA_ITEMS");
    }

    protected ListenableFuture<?> handleReplaceMediaItems(int r4, int r5, List<MediaItem> r6) {
        ListenableFuture<?> r0 = handleAddMediaItems(r5, r6);
        final ListenableFuture<?> r1 = handleRemoveMediaItems(r4, r5);
        return Util.transformFutureAsync(r0, new SimpleBasePlayer$$ExternalSyntheticLambda53(r1));
    }

    protected ListenableFuture<?> handleSeek(int r3, long r4, int r6) {
        throw new IllegalStateException("Missing implementation to handle one of the COMMAND_SEEK_*");
    }

    protected ListenableFuture<?> handleSetAudioAttributes(AudioAttributes r3, boolean r4) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_AUDIO_ATTRIBUTES");
    }

    protected ListenableFuture<?> handleSetDeviceMuted(boolean r3, int r4) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_ADJUST_DEVICE_VOLUME or COMMAND_ADJUST_DEVICE_VOLUME_WITH_FLAGS");
    }

    protected ListenableFuture<?> handleSetDeviceVolume(int r3, int r4) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_DEVICE_VOLUME or COMMAND_SET_DEVICE_VOLUME_WITH_FLAGS");
    }

    protected ListenableFuture<?> handleSetMediaItems(List<MediaItem> r3, int r4, long r5) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_MEDIA_ITEM(S)");
    }

    protected ListenableFuture<?> handleSetPlayWhenReady(boolean r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_PLAY_PAUSE");
    }

    protected ListenableFuture<?> handleSetPlaybackParameters(PlaybackParameters r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_SPEED_AND_PITCH");
    }

    protected ListenableFuture<?> handleSetPlaylistMetadata(MediaMetadata r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_PLAYLIST_METADATA");
    }

    protected ListenableFuture<?> handleSetRepeatMode(int r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_REPEAT_MODE");
    }

    protected ListenableFuture<?> handleSetShuffleModeEnabled(boolean r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_SHUFFLE_MODE");
    }

    protected ListenableFuture<?> handleSetTrackSelectionParameters(TrackSelectionParameters r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_TRACK_SELECTION_PARAMETERS");
    }

    protected ListenableFuture<?> handleSetVideoOutput(Object r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_VIDEO_SURFACE");
    }

    protected ListenableFuture<?> handleSetVolume(float r3) {
        throw new IllegalStateException("Missing implementation to handle COMMAND_SET_VOLUME");
    }

    protected ListenableFuture<?> handleStop() {
        throw new IllegalStateException("Missing implementation to handle COMMAND_STOP");
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void increaseDeviceVolume() {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(26) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleIncreaseDeviceVolume(1), new SimpleBasePlayer$$ExternalSyntheticLambda49(r0));
    }

    @Override // androidx.media3.common.Player
    public final void increaseDeviceVolume(int r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(34) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleIncreaseDeviceVolume(r4), new SimpleBasePlayer$$ExternalSyntheticLambda32(r0));
    }

    protected final void invalidateState() {
        verifyApplicationThreadAndInitState();
        if (this.pendingOperations.isEmpty() == true) goto L5;
        return;
    L5:
        if (this.released == true) goto L10;
        updateStateAndInformListeners(getState(), false, false);
        return;
    }

    @Override // androidx.media3.common.Player
    public final boolean isDeviceMuted() {
        verifyApplicationThreadAndInitState();
        return this.state.isDeviceMuted;
    }

    @Override // androidx.media3.common.Player
    public final boolean isLoading() {
        verifyApplicationThreadAndInitState();
        return this.state.isLoading;
    }

    @Override // androidx.media3.common.Player
    public final boolean isPlayingAd() {
        verifyApplicationThreadAndInitState();
        if (this.state.currentAdGroupIndex == (-1)) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: lambda$addMediaItems$3$androidx-media3-common-SimpleBasePlayer, reason: not valid java name */
    /* synthetic */ State m461lambda$addMediaItems$3$androidxmedia3commonSimpleBasePlayer(State r9, List r10, int r11) {
        List<MediaItemData> r3 = buildMutablePlaylistFromState(r9, this.period, this.window);
        int r0 = 0;
    L4:
        if (r0 >= r10.size()) goto L7;
        r3.add(r0 + r11, getPlaceholderMediaItemData((MediaItem) r10.get(r0)));
        r0 = r0 + 1;
        goto L4
    L7:
        if (r9.timeline.isEmpty() == true) goto L11;
        return getStateWithNewPlaylist(r9, r3, this.period, this.window);
    L11:
        return getStateWithNewPlaylistAndPosition(r9, r3, r9.currentMediaItemIndex, r9.contentPositionMsSupplier.get(), this.window);
    }

    /* JADX INFO: renamed from: lambda$moveMediaItems$4$androidx-media3-common-SimpleBasePlayer, reason: not valid java name */
    /* synthetic */ State m462lambda$moveMediaItems$4$androidxmedia3commonSimpleBasePlayer(State r4, int r5, int r6, int r7) {
        List<MediaItemData> r0 = buildMutablePlaylistFromState(r4, this.period, this.window);
        Util.moveItems(r0, r5, r6, r7);
        return getStateWithNewPlaylist(r4, r0, this.period, this.window);
    }

    /* JADX INFO: renamed from: lambda$new$0$androidx-media3-common-SimpleBasePlayer, reason: not valid java name */
    /* synthetic */ void m463lambda$new$0$androidxmedia3commonSimpleBasePlayer(Player.Listener r2, FlagSet r3) {
        r2.onEvents(this, new Player.Events(r3));
    }

    /* JADX INFO: renamed from: lambda$removeMediaItems$6$androidx-media3-common-SimpleBasePlayer */
    /* synthetic */ State m38x3b22ba57(State r4, int r5, int r6) {
        List<MediaItemData> r0 = buildMutablePlaylistFromState(r4, this.period, this.window);
        Util.removeRange(r0, r5, r6);
        return getStateWithNewPlaylist(r4, r0, this.period, this.window);
    }

    /* JADX INFO: renamed from: lambda$replaceMediaItems$5$androidx-media3-common-SimpleBasePlayer */
    /* synthetic */ State m39x7bc5132c(State r9, List r10, int r11, int r12) {
        List<MediaItemData> r3 = buildMutablePlaylistFromState(r9, this.period, this.window);
        int r0 = 0;
    L4:
        if (r0 >= r10.size()) goto L7;
        r3.add(r0 + r11, getPlaceholderMediaItemData((MediaItem) r10.get(r0)));
        r0 = r0 + 1;
        goto L4
    L7:
        if (r9.timeline.isEmpty() == true) goto L9;
        State r1 = getStateWithNewPlaylist(r9, r3, this.period, this.window);
    L10:
        if (r12 >= r11) goto L13;
        Util.removeRange(r3, r12, r11);
        return getStateWithNewPlaylist(r1, r3, this.period, this.window);
    L13:
        return r1;
    L9:
        r1 = getStateWithNewPlaylistAndPosition(r9, r3, r9.currentMediaItemIndex, r9.contentPositionMsSupplier.get(), this.window);
        goto L10
    }

    /* JADX INFO: renamed from: lambda$seekTo$10$androidx-media3-common-SimpleBasePlayer, reason: not valid java name */
    /* synthetic */ State m464lambda$seekTo$10$androidxmedia3commonSimpleBasePlayer(boolean r7, State r8, int r9, long r10) {
        if (r7 == false) goto L5;
        return r8;
    L5:
        return getStateWithNewPlaylistAndPosition(r8, null, r9, r10, this.window);
    }

    /* JADX INFO: renamed from: lambda$setMediaItemsInternal$2$androidx-media3-common-SimpleBasePlayer */
    /* synthetic */ State m40x396b5ff4(List r8, State r9, int r10, long r11) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r8.size()) goto L7;
        r0.add(getPlaceholderMediaItemData((MediaItem) r8.get(r1)));
        r1 = r1 + 1;
        goto L4
    L7:
        return getStateWithNewPlaylistAndPosition(r9, r0, r10, r11, this.window);
    }

    /* JADX INFO: renamed from: lambda$stop$12$androidx-media3-common-SimpleBasePlayer, reason: not valid java name */
    /* synthetic */ State m465lambda$stop$12$androidxmedia3commonSimpleBasePlayer(State r4) {
        return r4.buildUpon().setPlaybackState(1).setTotalBufferedDurationMs(PositionSupplier.ZERO).setContentBufferedPositionMs(PositionSupplier.CC.getConstant(getContentPositionMsInternal(r4, this.window))).setAdBufferedPositionMs(r4.adPositionMsSupplier).setIsLoading(false).build();
    }

    /* JADX INFO: renamed from: lambda$updateStateForPendingOperation$62$androidx-media3-common-SimpleBasePlayer */
    /* synthetic */ void m41x9bf3217a(ListenableFuture r3) {
        Util.castNonNull(this.state);
        this.pendingOperations.remove(r3);
        if (this.pendingOperations.isEmpty() == true) goto L5;
        return;
    L5:
        if (this.released == true) goto L9;
        updateStateAndInformListeners(getState(), false, false);
        return;
    }

    @Override // androidx.media3.common.Player
    public final void moveMediaItems(final int r9, int r10, int r11) {
        verifyApplicationThreadAndInitState();
        if (r9 < 0) goto L7;
        if (r10 < r9) goto L7;
        if (r11 < 0) goto L7;
        boolean r0 = true;
    L8:
        Assertions.checkArgument(r0);
        final State r3 = this.state;
        int r1 = r3.timeline.getWindowCount();
        if (shouldHandleCommand(20) == false) goto L22;
        if (r1 == 0) goto L22;
        if (r9 >= r1) goto L24;
        final int r5 = Math.min(r10, r1);
        final int r6 = Math.min(r11, r1 - (r5 - r9));
        if (r9 == r5) goto L20;
        if (r6 == r9) goto L23;
        updateStateForPendingOperation(handleMoveMediaItems(r9, r5, r6), new SimpleBasePlayer$$ExternalSyntheticLambda48(this, r3, r9, r5, r6));
        return;
    L23:
        return;
    L20:
        return;
    L24:
        return;
    L22:
        return;
    L7:
        r0 = false;
        goto L8
    }

    @Override // androidx.media3.common.Player
    public final void prepare() {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(2) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handlePrepare(), new SimpleBasePlayer$$ExternalSyntheticLambda56(r0));
    }

    @Override // androidx.media3.common.Player
    public final void release() {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(32) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleRelease(), new SimpleBasePlayer$$ExternalSyntheticLambda43(r0));
        this.released = true;
        this.listeners.release();
        this.state = this.state.buildUpon().setPlaybackState(1).setTotalBufferedDurationMs(PositionSupplier.ZERO).setContentBufferedPositionMs(PositionSupplier.CC.getConstant(getContentPositionMsInternal(r0, this.window))).setAdBufferedPositionMs(r0.adPositionMsSupplier).setIsLoading(false).build();
    }

    @Override // androidx.media3.common.Player
    public final void removeListener(Player.Listener r2) {
        verifyApplicationThreadAndInitState();
        this.listeners.remove(r2);
    }

    @Override // androidx.media3.common.Player
    public final void removeMediaItems(final int r6, int r7) {
        verifyApplicationThreadAndInitState();
        if (r6 < 0) goto L6;
        if (r7 < r6) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkArgument(r0);
        final State r1 = this.state;
        int r2 = r1.timeline.getWindowCount();
        if (shouldHandleCommand(20) == false) goto L17;
        if (r2 == 0) goto L18;
        if (r6 >= r2) goto L19;
        final int r3 = Math.min(r7, r2);
        if (r6 != r3) goto L15;
        return;
    L15:
        updateStateForPendingOperation(handleRemoveMediaItems(r6, r3), new SimpleBasePlayer$$ExternalSyntheticLambda52(this, r1, r6, r3));
        return;
    L19:
        return;
    L18:
        return;
    L17:
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.common.Player
    public final void replaceMediaItems(final int r9, int r10, final List<MediaItem> r11) {
        verifyApplicationThreadAndInitState();
        if (r9 < 0) goto L6;
        if (r9 > r10) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkArgument(r0);
        final State r3 = this.state;
        int r1 = r3.timeline.getWindowCount();
        if (shouldHandleCommand(20) == false) goto L14;
        if (r9 > r1) goto L15;
        final int r5 = Math.min(r10, r1);
        updateStateForPendingOperation(handleReplaceMediaItems(r9, r5, r11), new SimpleBasePlayer$$ExternalSyntheticLambda27(this, r3, r11, r5, r9));
        return;
    L15:
        return;
    L14:
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.common.BasePlayer
    public final void seekTo(final int r11, final long r12, int r14, boolean r15) {
        verifyApplicationThreadAndInitState();
        boolean r7 = false;
        if (r11 == (-1)) goto L7;
        if (r11 >= 0) goto L7;
        boolean r2 = false;
    L8:
        Assertions.checkArgument(r2);
        final State r3 = this.state;
        if (shouldHandleCommand(r14) == true) goto L11;
        return;
    L11:
        if (r11 != (-1)) goto L13;
    L20:
        final boolean r4 = true;
    L21:
        ListenableFuture<?> r9 = handleSeek(r11, r12, r14);
        Supplier<State> r0 = new SimpleBasePlayer$$ExternalSyntheticLambda51(this, r4, r3, r11, r12);
        if (r4 == true) goto L24;
        r7 = true;
    L24:
        updateStateForPendingOperation(r9, r0, r7, r15);
        return;
    L13:
        if (isPlayingAd() == true) goto L20;
        if (r3.timeline.isEmpty() == false) goto L17;
    L19:
        r4 = false;
        goto L21
    L17:
        if (r11 < r3.timeline.getWindowCount()) goto L19;
    L7:
        r2 = true;
        goto L8
    }

    @Override // androidx.media3.common.Player
    public final void setAudioAttributes(final AudioAttributes r4, boolean r5) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(35) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetAudioAttributes(r4, r5), new SimpleBasePlayer$$ExternalSyntheticLambda50(r0, r4));
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void setDeviceMuted(final boolean r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(26) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetDeviceMuted(r4, 1), new SimpleBasePlayer$$ExternalSyntheticLambda46(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setDeviceMuted(final boolean r4, int r5) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(34) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetDeviceMuted(r4, r5), new SimpleBasePlayer$$ExternalSyntheticLambda59(r0, r4));
    }

    @Override // androidx.media3.common.Player
    @Deprecated
    public final void setDeviceVolume(final int r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(25) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetDeviceVolume(r4, 1), new SimpleBasePlayer$$ExternalSyntheticLambda11(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setDeviceVolume(final int r4, int r5) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(33) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetDeviceVolume(r4, r5), new SimpleBasePlayer$$ExternalSyntheticLambda36(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setMediaItems(List<MediaItem> r2, int r3, long r4) {
        verifyApplicationThreadAndInitState();
        if (r3 != (-1)) goto L5;
        r3 = this.state.currentMediaItemIndex;
        r4 = this.state.contentPositionMsSupplier.get();
    L5:
        setMediaItemsInternal(r2, r3, r4);
    }

    @Override // androidx.media3.common.Player
    public final void setMediaItems(List<MediaItem> r4, boolean r5) {
        verifyApplicationThreadAndInitState();
        if (r5 == false) goto L5;
        int r0 = -1;
    L6:
        if (r5 == false) goto L8;
        long r1 = C0180C.TIME_UNSET;
    L9:
        setMediaItemsInternal(r4, r0, r1);
        return;
    L8:
        r1 = this.state.contentPositionMsSupplier.get();
        goto L9
    L5:
        r0 = this.state.currentMediaItemIndex;
        goto L6
    }

    @Override // androidx.media3.common.Player
    public final void setPlayWhenReady(final boolean r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(1) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetPlayWhenReady(r4), new SimpleBasePlayer$$ExternalSyntheticLambda0(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setPlaybackParameters(final PlaybackParameters r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(13) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetPlaybackParameters(r4), new SimpleBasePlayer$$ExternalSyntheticLambda58(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setPlaylistMetadata(final MediaMetadata r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(19) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetPlaylistMetadata(r4), new SimpleBasePlayer$$ExternalSyntheticLambda39(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setRepeatMode(final int r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(15) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetRepeatMode(r4), new SimpleBasePlayer$$ExternalSyntheticLambda40(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setShuffleModeEnabled(final boolean r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(14) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetShuffleModeEnabled(r4), new SimpleBasePlayer$$ExternalSyntheticLambda35(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setTrackSelectionParameters(final TrackSelectionParameters r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(29) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetTrackSelectionParameters(r4), new SimpleBasePlayer$$ExternalSyntheticLambda60(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setVideoSurface(Surface r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(27) == true) goto L5;
        return;
    L5:
        if (r4 != null) goto L8;
        clearVideoSurface();
        return;
    L8:
        updateStateForPendingOperation(handleSetVideoOutput(r4), new SimpleBasePlayer$$ExternalSyntheticLambda29(r0));
    }

    @Override // androidx.media3.common.Player
    public final void setVideoSurfaceHolder(final SurfaceHolder r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(27) == true) goto L5;
        return;
    L5:
        if (r4 != null) goto L8;
        clearVideoSurface();
        return;
    L8:
        updateStateForPendingOperation(handleSetVideoOutput(r4), new SimpleBasePlayer$$ExternalSyntheticLambda42(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setVideoSurfaceView(final SurfaceView r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(27) == true) goto L5;
        return;
    L5:
        if (r4 != null) goto L8;
        clearVideoSurface();
        return;
    L8:
        updateStateForPendingOperation(handleSetVideoOutput(r4), new SimpleBasePlayer$$ExternalSyntheticLambda45(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void setVideoTextureView(TextureView r5) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(27) == true) goto L5;
        return;
    L5:
        if (r5 != null) goto L9;
        clearVideoSurface();
        return;
    L9:
        if (r5.isAvailable() == false) goto L11;
        final Size r1 = new Size(r5.getWidth(), r5.getHeight());
    L12:
        updateStateForPendingOperation(handleSetVideoOutput(r5), new SimpleBasePlayer$$ExternalSyntheticLambda22(r0, r1));
        return;
    L11:
        r1 = Size.ZERO;
        goto L12
    }

    @Override // androidx.media3.common.Player
    public final void setVolume(final float r4) {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(24) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleSetVolume(r4), new SimpleBasePlayer$$ExternalSyntheticLambda41(r0, r4));
    }

    @Override // androidx.media3.common.Player
    public final void stop() {
        verifyApplicationThreadAndInitState();
        final State r0 = this.state;
        if (shouldHandleCommand(3) == true) goto L5;
        return;
    L5:
        updateStateForPendingOperation(handleStop(), new SimpleBasePlayer$$ExternalSyntheticLambda54(this, r0));
    }

    protected final void verifyApplicationThread() {
        if (Thread.currentThread() != this.applicationLooper.getThread()) goto L6;
        return;
    L6:
        throw new IllegalStateException(Util.formatInvariant("Player is accessed on the wrong thread.\nCurrent thread: '%s'\nExpected thread: '%s'\n", new Object[]{Thread.currentThread().getName(), this.applicationLooper.getThread().getName()}));
    }
}
