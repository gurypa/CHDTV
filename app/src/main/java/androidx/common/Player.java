package androidx.media3.common;

import android.os.Bundle;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface Player {

    @Deprecated
    public static final int COMMAND_ADJUST_DEVICE_VOLUME = 26;
    public static final int COMMAND_ADJUST_DEVICE_VOLUME_WITH_FLAGS = 34;
    public static final int COMMAND_CHANGE_MEDIA_ITEMS = 20;
    public static final int COMMAND_GET_AUDIO_ATTRIBUTES = 21;
    public static final int COMMAND_GET_CURRENT_MEDIA_ITEM = 16;
    public static final int COMMAND_GET_DEVICE_VOLUME = 23;

    @Deprecated
    public static final int COMMAND_GET_MEDIA_ITEMS_METADATA = 18;
    public static final int COMMAND_GET_METADATA = 18;
    public static final int COMMAND_GET_TEXT = 28;
    public static final int COMMAND_GET_TIMELINE = 17;
    public static final int COMMAND_GET_TRACKS = 30;
    public static final int COMMAND_GET_VOLUME = 22;
    public static final int COMMAND_INVALID = -1;
    public static final int COMMAND_PLAY_PAUSE = 1;
    public static final int COMMAND_PREPARE = 2;
    public static final int COMMAND_RELEASE = 32;
    public static final int COMMAND_SEEK_BACK = 11;
    public static final int COMMAND_SEEK_FORWARD = 12;
    public static final int COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM = 5;

    @Deprecated
    public static final int COMMAND_SEEK_IN_CURRENT_WINDOW = 5;
    public static final int COMMAND_SEEK_TO_DEFAULT_POSITION = 4;
    public static final int COMMAND_SEEK_TO_MEDIA_ITEM = 10;
    public static final int COMMAND_SEEK_TO_NEXT = 9;
    public static final int COMMAND_SEEK_TO_NEXT_MEDIA_ITEM = 8;

    @Deprecated
    public static final int COMMAND_SEEK_TO_NEXT_WINDOW = 8;
    public static final int COMMAND_SEEK_TO_PREVIOUS = 7;
    public static final int COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM = 6;

    @Deprecated
    public static final int COMMAND_SEEK_TO_PREVIOUS_WINDOW = 6;

    @Deprecated
    public static final int COMMAND_SEEK_TO_WINDOW = 10;
    public static final int COMMAND_SET_AUDIO_ATTRIBUTES = 35;

    @Deprecated
    public static final int COMMAND_SET_DEVICE_VOLUME = 25;
    public static final int COMMAND_SET_DEVICE_VOLUME_WITH_FLAGS = 33;
    public static final int COMMAND_SET_MEDIA_ITEM = 31;

    @Deprecated
    public static final int COMMAND_SET_MEDIA_ITEMS_METADATA = 19;
    public static final int COMMAND_SET_PLAYLIST_METADATA = 19;
    public static final int COMMAND_SET_REPEAT_MODE = 15;
    public static final int COMMAND_SET_SHUFFLE_MODE = 14;
    public static final int COMMAND_SET_SPEED_AND_PITCH = 13;
    public static final int COMMAND_SET_TRACK_SELECTION_PARAMETERS = 29;
    public static final int COMMAND_SET_VIDEO_SURFACE = 27;
    public static final int COMMAND_SET_VOLUME = 24;
    public static final int COMMAND_STOP = 3;
    public static final int DISCONTINUITY_REASON_AUTO_TRANSITION = 0;
    public static final int DISCONTINUITY_REASON_INTERNAL = 5;
    public static final int DISCONTINUITY_REASON_REMOVE = 4;
    public static final int DISCONTINUITY_REASON_SEEK = 1;
    public static final int DISCONTINUITY_REASON_SEEK_ADJUSTMENT = 2;
    public static final int DISCONTINUITY_REASON_SILENCE_SKIP = 6;
    public static final int DISCONTINUITY_REASON_SKIP = 3;
    public static final int EVENT_AUDIO_ATTRIBUTES_CHANGED = 20;
    public static final int EVENT_AUDIO_SESSION_ID = 21;
    public static final int EVENT_AVAILABLE_COMMANDS_CHANGED = 13;
    public static final int EVENT_CUES = 27;
    public static final int EVENT_DEVICE_INFO_CHANGED = 29;
    public static final int EVENT_DEVICE_VOLUME_CHANGED = 30;
    public static final int EVENT_IS_LOADING_CHANGED = 3;
    public static final int EVENT_IS_PLAYING_CHANGED = 7;
    public static final int EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED = 18;
    public static final int EVENT_MEDIA_ITEM_TRANSITION = 1;
    public static final int EVENT_MEDIA_METADATA_CHANGED = 14;
    public static final int EVENT_METADATA = 28;
    public static final int EVENT_PLAYBACK_PARAMETERS_CHANGED = 12;
    public static final int EVENT_PLAYBACK_STATE_CHANGED = 4;
    public static final int EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED = 6;
    public static final int EVENT_PLAYER_ERROR = 10;
    public static final int EVENT_PLAYLIST_METADATA_CHANGED = 15;
    public static final int EVENT_PLAY_WHEN_READY_CHANGED = 5;
    public static final int EVENT_POSITION_DISCONTINUITY = 11;
    public static final int EVENT_RENDERED_FIRST_FRAME = 26;
    public static final int EVENT_REPEAT_MODE_CHANGED = 8;
    public static final int EVENT_SEEK_BACK_INCREMENT_CHANGED = 16;
    public static final int EVENT_SEEK_FORWARD_INCREMENT_CHANGED = 17;
    public static final int EVENT_SHUFFLE_MODE_ENABLED_CHANGED = 9;
    public static final int EVENT_SKIP_SILENCE_ENABLED_CHANGED = 23;
    public static final int EVENT_SURFACE_SIZE_CHANGED = 24;
    public static final int EVENT_TIMELINE_CHANGED = 0;
    public static final int EVENT_TRACKS_CHANGED = 2;
    public static final int EVENT_TRACK_SELECTION_PARAMETERS_CHANGED = 19;
    public static final int EVENT_VIDEO_SIZE_CHANGED = 25;
    public static final int EVENT_VOLUME_CHANGED = 22;
    public static final int MEDIA_ITEM_TRANSITION_REASON_AUTO = 1;
    public static final int MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED = 3;
    public static final int MEDIA_ITEM_TRANSITION_REASON_REPEAT = 0;
    public static final int MEDIA_ITEM_TRANSITION_REASON_SEEK = 2;
    public static final int PLAYBACK_SUPPRESSION_REASON_NONE = 0;
    public static final int PLAYBACK_SUPPRESSION_REASON_TRANSIENT_AUDIO_FOCUS_LOSS = 1;
    public static final int PLAYBACK_SUPPRESSION_REASON_UNSUITABLE_AUDIO_OUTPUT = 3;

    @Deprecated
    public static final int PLAYBACK_SUPPRESSION_REASON_UNSUITABLE_AUDIO_ROUTE = 2;
    public static final int PLAY_WHEN_READY_CHANGE_REASON_AUDIO_BECOMING_NOISY = 3;
    public static final int PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS = 2;
    public static final int PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM = 5;
    public static final int PLAY_WHEN_READY_CHANGE_REASON_REMOTE = 4;
    public static final int PLAY_WHEN_READY_CHANGE_REASON_SUPPRESSED_TOO_LONG = 6;
    public static final int PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST = 1;
    public static final int REPEAT_MODE_ALL = 2;
    public static final int REPEAT_MODE_OFF = 0;
    public static final int REPEAT_MODE_ONE = 1;
    public static final int STATE_BUFFERING = 2;
    public static final int STATE_ENDED = 4;
    public static final int STATE_IDLE = 1;
    public static final int STATE_READY = 3;
    public static final int TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED = 0;
    public static final int TIMELINE_CHANGE_REASON_SOURCE_UPDATE = 1;

    /* JADX INFO: renamed from: androidx.media3.common.Player$1 */
    static /* synthetic */ class C01921 {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Command {
    }

    public static final class Commands {
        public static final Commands EMPTY = null;
        private static final String FIELD_COMMANDS = null;
        private final FlagSet flags;

        public static final class Builder {
            private static final int[] SUPPORTED_COMMANDS = null;
            private final FlagSet.Builder flagsBuilder;

            static {
                SUPPORTED_COMMANDS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 31, 20, 21, 22, 23, 24, 25, 33, 26, 34, 35, 27, 28, 29, 30, 32};
            }

            public Builder() {
                this.flagsBuilder = new FlagSet.Builder();
            }

            private Builder(Commands r3) {
                this.flagsBuilder = new FlagSet.Builder();
                this.flagsBuilder.addAll(Commands.access$000(r3));
            }

            /* synthetic */ Builder(Commands r1, C01921 r2) {
                this(r1);
            }

            public Builder add(int r2) {
                this.flagsBuilder.add(r2);
                return this;
            }

            public Builder addAll(Commands r3) {
                this.flagsBuilder.addAll(Commands.access$000(r3));
                return this;
            }

            public Builder addAll(int... r2) {
                this.flagsBuilder.addAll(r2);
                return this;
            }

            public Builder addAllCommands() {
                this.flagsBuilder.addAll(SUPPORTED_COMMANDS);
                return this;
            }

            public Builder addIf(int r2, boolean r3) {
                this.flagsBuilder.addIf(r2, r3);
                return this;
            }

            public Commands build() {
                return new Commands(this.flagsBuilder.build(), null);
            }

            public Builder remove(int r2) {
                this.flagsBuilder.remove(r2);
                return this;
            }

            public Builder removeAll(int... r2) {
                this.flagsBuilder.removeAll(r2);
                return this;
            }

            public Builder removeIf(int r2, boolean r3) {
                this.flagsBuilder.removeIf(r2, r3);
                return this;
            }
        }

        static {
            EMPTY = new Builder().build();
            FIELD_COMMANDS = Util.intToStringMaxRadix(0);
        }

        private Commands(FlagSet r1) {
            this.flags = r1;
        }

        /* synthetic */ Commands(FlagSet r1, C01921 r2) {
            this(r1);
        }

        static /* synthetic */ FlagSet access$000(Commands r1) {
            return r1.flags;
        }

        public static Commands fromBundle(Bundle r4) {
            ArrayList<Integer> r0 = r4.getIntegerArrayList(FIELD_COMMANDS);
            if (r0 == null) goto L5;
            Builder r1 = new Builder();
            int r2 = 0;
        L8:
            if (r2 >= r0.size()) goto L11;
            r1.add(r0.get(r2).intValue());
            r2 = r2 + 1;
            goto L8
        L11:
            return r1.build();
        L5:
            return EMPTY;
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean contains(int r2) {
            return this.flags.contains(r2);
        }

        public boolean containsAny(int... r2) {
            return this.flags.containsAny(r2);
        }

        public boolean equals(Object r4) {
            if (this != r4) goto L6;
            return true;
        L6:
            if ((r4 instanceof Commands) == true) goto L10;
            return false;
        L10:
            return this.flags.equals(((Commands) r4).flags);
        }

        public int get(int r2) {
            return this.flags.get(r2);
        }

        public int hashCode() {
            return this.flags.hashCode();
        }

        public int size() {
            return this.flags.size();
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            ArrayList<Integer> r1 = new ArrayList();
            int r2 = 0;
        L4:
            if (r2 >= this.flags.size()) goto L6;
            r1.add(Integer.valueOf(this.flags.get(r2)));
            r2 = r2 + 1;
            goto L4
        L6:
            r0.putIntegerArrayList(FIELD_COMMANDS, r1);
            return r0;
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface DiscontinuityReason {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Event {
    }

    public static final class Events {
        private final FlagSet flags;

        public Events(FlagSet r1) {
            this.flags = r1;
        }

        public boolean contains(int r2) {
            return this.flags.contains(r2);
        }

        public boolean containsAny(int... r2) {
            return this.flags.containsAny(r2);
        }

        public boolean equals(Object r4) {
            if (this != r4) goto L6;
            return true;
        L6:
            if ((r4 instanceof Events) == true) goto L10;
            return false;
        L10:
            return this.flags.equals(((Events) r4).flags);
        }

        public int get(int r2) {
            return this.flags.get(r2);
        }

        public int hashCode() {
            return this.flags.hashCode();
        }

        public int size() {
            return this.flags.size();
        }
    }

    public interface Listener {

        /* JADX INFO: renamed from: androidx.media3.common.Player$Listener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onAudioAttributesChanged(Listener r0, AudioAttributes r1) {
            }

            public static void $default$onAudioSessionIdChanged(Listener r0, int r1) {
            }

            public static void $default$onAvailableCommandsChanged(Listener r0, Commands r1) {
            }

            public static void $default$onCues(Listener r0, CueGroup r1) {
            }

            @Deprecated
            public static void $default$onCues(Listener r0, List r1) {
            }

            public static void $default$onDeviceInfoChanged(Listener r0, DeviceInfo r1) {
            }

            public static void $default$onDeviceVolumeChanged(Listener r0, int r1, boolean r2) {
            }

            public static void $default$onEvents(Listener r0, Player r1, Events r2) {
            }

            public static void $default$onIsLoadingChanged(Listener r0, boolean r1) {
            }

            public static void $default$onIsPlayingChanged(Listener r0, boolean r1) {
            }

            @Deprecated
            public static void $default$onLoadingChanged(Listener r0, boolean r1) {
            }

            public static void $default$onMaxSeekToPreviousPositionChanged(Listener r0, long r1) {
            }

            public static void $default$onMediaItemTransition(Listener r0, MediaItem r1, int r2) {
            }

            public static void $default$onMediaMetadataChanged(Listener r0, MediaMetadata r1) {
            }

            public static void $default$onMetadata(Listener r0, Metadata r1) {
            }

            public static void $default$onPlayWhenReadyChanged(Listener r0, boolean r1, int r2) {
            }

            public static void $default$onPlaybackParametersChanged(Listener r0, PlaybackParameters r1) {
            }

            public static void $default$onPlaybackStateChanged(Listener r0, int r1) {
            }

            public static void $default$onPlaybackSuppressionReasonChanged(Listener r0, int r1) {
            }

            public static void $default$onPlayerError(Listener r0, PlaybackException r1) {
            }

            public static void $default$onPlayerErrorChanged(Listener r0, PlaybackException r1) {
            }

            @Deprecated
            public static void $default$onPlayerStateChanged(Listener r0, boolean r1, int r2) {
            }

            public static void $default$onPlaylistMetadataChanged(Listener r0, MediaMetadata r1) {
            }

            @Deprecated
            public static void $default$onPositionDiscontinuity(Listener r0, int r1) {
            }

            public static void $default$onPositionDiscontinuity(Listener r0, PositionInfo r1, PositionInfo r2, int r3) {
            }

            public static void $default$onRenderedFirstFrame(Listener r0) {
            }

            public static void $default$onRepeatModeChanged(Listener r0, int r1) {
            }

            public static void $default$onSeekBackIncrementChanged(Listener r0, long r1) {
            }

            public static void $default$onSeekForwardIncrementChanged(Listener r0, long r1) {
            }

            public static void $default$onShuffleModeEnabledChanged(Listener r0, boolean r1) {
            }

            public static void $default$onSkipSilenceEnabledChanged(Listener r0, boolean r1) {
            }

            public static void $default$onSurfaceSizeChanged(Listener r0, int r1, int r2) {
            }

            public static void $default$onTimelineChanged(Listener r0, Timeline r1, int r2) {
            }

            public static void $default$onTrackSelectionParametersChanged(Listener r0, TrackSelectionParameters r1) {
            }

            public static void $default$onTracksChanged(Listener r0, Tracks r1) {
            }

            public static void $default$onVideoSizeChanged(Listener r0, VideoSize r1) {
            }

            public static void $default$onVolumeChanged(Listener r0, float r1) {
            }
        }

        void onAudioAttributesChanged(AudioAttributes r1);

        void onAudioSessionIdChanged(int r1);

        void onAvailableCommandsChanged(Commands r1);

        void onCues(CueGroup r1);

        @Deprecated
        void onCues(List<Cue> r1);

        void onDeviceInfoChanged(DeviceInfo r1);

        void onDeviceVolumeChanged(int r1, boolean r2);

        void onEvents(Player r1, Events r2);

        void onIsLoadingChanged(boolean r1);

        void onIsPlayingChanged(boolean r1);

        @Deprecated
        void onLoadingChanged(boolean r1);

        void onMaxSeekToPreviousPositionChanged(long r1);

        void onMediaItemTransition(MediaItem r1, int r2);

        void onMediaMetadataChanged(MediaMetadata r1);

        void onMetadata(Metadata r1);

        void onPlayWhenReadyChanged(boolean r1, int r2);

        void onPlaybackParametersChanged(PlaybackParameters r1);

        void onPlaybackStateChanged(int r1);

        void onPlaybackSuppressionReasonChanged(int r1);

        void onPlayerError(PlaybackException r1);

        void onPlayerErrorChanged(PlaybackException r1);

        @Deprecated
        void onPlayerStateChanged(boolean r1, int r2);

        void onPlaylistMetadataChanged(MediaMetadata r1);

        @Deprecated
        void onPositionDiscontinuity(int r1);

        void onPositionDiscontinuity(PositionInfo r1, PositionInfo r2, int r3);

        void onRenderedFirstFrame();

        void onRepeatModeChanged(int r1);

        void onSeekBackIncrementChanged(long r1);

        void onSeekForwardIncrementChanged(long r1);

        void onShuffleModeEnabledChanged(boolean r1);

        void onSkipSilenceEnabledChanged(boolean r1);

        void onSurfaceSizeChanged(int r1, int r2);

        void onTimelineChanged(Timeline r1, int r2);

        void onTrackSelectionParametersChanged(TrackSelectionParameters r1);

        void onTracksChanged(Tracks r1);

        void onVideoSizeChanged(VideoSize r1);

        void onVolumeChanged(float r1);
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaItemTransitionReason {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayWhenReadyChangeReason {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaybackSuppressionReason {
    }

    public static final class PositionInfo {
        private static final String FIELD_AD_GROUP_INDEX = null;
        private static final String FIELD_AD_INDEX_IN_AD_GROUP = null;
        static final String FIELD_CONTENT_POSITION_MS = null;
        private static final String FIELD_MEDIA_ITEM = null;
        static final String FIELD_MEDIA_ITEM_INDEX = null;
        static final String FIELD_PERIOD_INDEX = null;
        static final String FIELD_POSITION_MS = null;
        public final int adGroupIndex;
        public final int adIndexInAdGroup;
        public final long contentPositionMs;
        public final MediaItem mediaItem;
        public final int mediaItemIndex;
        public final int periodIndex;
        public final Object periodUid;
        public final long positionMs;

        @Deprecated
        public final int windowIndex;
        public final Object windowUid;

        static {
            FIELD_MEDIA_ITEM_INDEX = Util.intToStringMaxRadix(0);
            FIELD_MEDIA_ITEM = Util.intToStringMaxRadix(1);
            FIELD_PERIOD_INDEX = Util.intToStringMaxRadix(2);
            FIELD_POSITION_MS = Util.intToStringMaxRadix(3);
            FIELD_CONTENT_POSITION_MS = Util.intToStringMaxRadix(4);
            FIELD_AD_GROUP_INDEX = Util.intToStringMaxRadix(5);
            FIELD_AD_INDEX_IN_AD_GROUP = Util.intToStringMaxRadix(6);
        }

        public PositionInfo(Object r1, int r2, MediaItem r3, Object r4, int r5, long r6, long r8, int r10, int r11) {
            this.windowUid = r1;
            this.windowIndex = r2;
            this.mediaItemIndex = r2;
            this.mediaItem = r3;
            this.periodUid = r4;
            this.periodIndex = r5;
            this.positionMs = r6;
            this.contentPositionMs = r8;
            this.adGroupIndex = r10;
            this.adIndexInAdGroup = r11;
        }

        @Deprecated
        public PositionInfo(Object r13, int r14, Object r15, int r16, long r17, long r19, int r21, int r22) {
            this(r13, r14, MediaItem.EMPTY, r15, r16, r17, r19, r21, r22);
        }

        public static PositionInfo fromBundle(Bundle r14) {
            int r4 = r14.getInt(FIELD_MEDIA_ITEM_INDEX, 0);
            Bundle r0 = r14.getBundle(FIELD_MEDIA_ITEM);
            if (r0 != null) goto L5;
            MediaItem r2 = null;
        L7:
            return new PositionInfo(null, r4, r2, null, r14.getInt(FIELD_PERIOD_INDEX, 0), r14.getLong(FIELD_POSITION_MS, 0), r14.getLong(FIELD_CONTENT_POSITION_MS, 0), r14.getInt(FIELD_AD_GROUP_INDEX, -1), r14.getInt(FIELD_AD_INDEX_IN_AD_GROUP, -1));
        L5:
            r2 = MediaItem.fromBundle(r0);
            goto L7
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L19:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L19;
            PositionInfo r2 = (PositionInfo) r6;
            if (equalsForBundling(r2) == false) goto L18;
            if (Objects.equal(this.windowUid, r2.windowUid) == false) goto L18;
            if (Objects.equal(this.periodUid, r2.periodUid) == false) goto L18;
            return true;
        L18:
            return false;
        }

        public boolean equalsForBundling(PositionInfo r5) {
            if (this.mediaItemIndex == r5.mediaItemIndex) goto L5;
        L17:
            return false;
        L5:
            if (this.periodIndex != r5.periodIndex) goto L17;
            if (this.positionMs != r5.positionMs) goto L17;
            if (this.contentPositionMs != r5.contentPositionMs) goto L17;
            if (this.adGroupIndex != r5.adGroupIndex) goto L17;
            if (this.adIndexInAdGroup != r5.adIndexInAdGroup) goto L17;
            if (Objects.equal(this.mediaItem, r5.mediaItem) == false) goto L17;
            return true;
        }

        public PositionInfo filterByAvailableCommands(boolean r15, boolean r16) {
            if (r15 == false) goto L5;
            if (r16 == false) goto L5;
            return this;
        L5:
            Object r1 = this.windowUid;
            if (r16 == false) goto L8;
            int r3 = this.mediaItemIndex;
        L9:
            if (r15 == false) goto L11;
            MediaItem r4 = this.mediaItem;
        L12:
            int r5 = 0;
            int r2 = r3;
            MediaItem r6 = r4;
            Object r7 = this.periodUid;
            if (r16 == false) goto L15;
            r5 = this.periodIndex;
        L15:
            long r8 = 0;
            if (r15 == false) goto L18;
            long r9 = this.positionMs;
        L19:
            if (r15 == false) goto L21;
            r8 = this.contentPositionMs;
        L21:
            int r10 = -1;
            if (r15 == false) goto L24;
            int r11 = this.adGroupIndex;
        L25:
            if (r15 == false) goto L28;
            r10 = this.adIndexInAdGroup;
        L28:
            return new PositionInfo(r1, r2, r6, r7, r5, r9, r8, r11, r10);
        L24:
            r11 = -1;
            goto L25
        L18:
            r9 = 0;
            goto L19
        L11:
            r4 = null;
            goto L12
        L8:
            r3 = 0;
            goto L9
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.windowUid, Integer.valueOf(this.mediaItemIndex), this.mediaItem, this.periodUid, Integer.valueOf(this.periodIndex), Long.valueOf(this.positionMs), Long.valueOf(this.contentPositionMs), Integer.valueOf(this.adGroupIndex), Integer.valueOf(this.adIndexInAdGroup)});
        }

        @Deprecated
        public Bundle toBundle() {
            return toBundle(Integer.MAX_VALUE);
        }

        public Bundle toBundle(int r8) {
            Bundle r0 = new Bundle();
            if (r8 >= 3) goto L5;
        L6:
            r0.putInt(FIELD_MEDIA_ITEM_INDEX, this.mediaItemIndex);
        L8:
            if (this.mediaItem == null) goto L10;
            r0.putBundle(FIELD_MEDIA_ITEM, this.mediaItem.toBundle());
        L10:
            if (r8 >= 3) goto L12;
        L13:
            r0.putInt(FIELD_PERIOD_INDEX, this.periodIndex);
        L15:
            if (r8 >= 3) goto L17;
        L18:
            r0.putLong(FIELD_POSITION_MS, this.positionMs);
        L19:
            if (r8 >= 3) goto L21;
        L22:
            r0.putLong(FIELD_CONTENT_POSITION_MS, this.contentPositionMs);
        L24:
            if (this.adGroupIndex == (-1)) goto L27;
            r0.putInt(FIELD_AD_GROUP_INDEX, this.adGroupIndex);
        L27:
            if (this.adIndexInAdGroup == (-1)) goto L29;
            r0.putInt(FIELD_AD_INDEX_IN_AD_GROUP, this.adIndexInAdGroup);
        L29:
            return r0;
        L21:
            if (this.contentPositionMs == 0) goto L24;
        L17:
            if (this.positionMs == 0) goto L19;
        L12:
            if (this.periodIndex == 0) goto L15;
        L5:
            if (this.mediaItemIndex == 0) goto L8;
            goto L6
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimelineChangeReason {
    }

    void addListener(Listener r1);

    void addMediaItem(int r1, MediaItem r2);

    void addMediaItem(MediaItem r1);

    void addMediaItems(int r1, List<MediaItem> r2);

    void addMediaItems(List<MediaItem> r1);

    boolean canAdvertiseSession();

    void clearMediaItems();

    void clearVideoSurface();

    void clearVideoSurface(Surface r1);

    void clearVideoSurfaceHolder(SurfaceHolder r1);

    void clearVideoSurfaceView(SurfaceView r1);

    void clearVideoTextureView(TextureView r1);

    @Deprecated
    void decreaseDeviceVolume();

    void decreaseDeviceVolume(int r1);

    Looper getApplicationLooper();

    AudioAttributes getAudioAttributes();

    Commands getAvailableCommands();

    int getBufferedPercentage();

    long getBufferedPosition();

    long getContentBufferedPosition();

    long getContentDuration();

    long getContentPosition();

    int getCurrentAdGroupIndex();

    int getCurrentAdIndexInAdGroup();

    CueGroup getCurrentCues();

    long getCurrentLiveOffset();

    Object getCurrentManifest();

    MediaItem getCurrentMediaItem();

    int getCurrentMediaItemIndex();

    int getCurrentPeriodIndex();

    long getCurrentPosition();

    Timeline getCurrentTimeline();

    Tracks getCurrentTracks();

    @Deprecated
    int getCurrentWindowIndex();

    DeviceInfo getDeviceInfo();

    int getDeviceVolume();

    long getDuration();

    long getMaxSeekToPreviousPosition();

    MediaItem getMediaItemAt(int r1);

    int getMediaItemCount();

    MediaMetadata getMediaMetadata();

    int getNextMediaItemIndex();

    @Deprecated
    int getNextWindowIndex();

    boolean getPlayWhenReady();

    PlaybackParameters getPlaybackParameters();

    int getPlaybackState();

    int getPlaybackSuppressionReason();

    PlaybackException getPlayerError();

    MediaMetadata getPlaylistMetadata();

    int getPreviousMediaItemIndex();

    @Deprecated
    int getPreviousWindowIndex();

    int getRepeatMode();

    long getSeekBackIncrement();

    long getSeekForwardIncrement();

    boolean getShuffleModeEnabled();

    Size getSurfaceSize();

    long getTotalBufferedDuration();

    TrackSelectionParameters getTrackSelectionParameters();

    VideoSize getVideoSize();

    float getVolume();

    @Deprecated
    boolean hasNext();

    boolean hasNextMediaItem();

    @Deprecated
    boolean hasNextWindow();

    boolean hasPreviousMediaItem();

    @Deprecated
    void increaseDeviceVolume();

    void increaseDeviceVolume(int r1);

    boolean isCommandAvailable(int r1);

    boolean isCurrentMediaItemDynamic();

    boolean isCurrentMediaItemLive();

    boolean isCurrentMediaItemSeekable();

    @Deprecated
    boolean isCurrentWindowDynamic();

    @Deprecated
    boolean isCurrentWindowLive();

    @Deprecated
    boolean isCurrentWindowSeekable();

    boolean isDeviceMuted();

    boolean isLoading();

    boolean isPlaying();

    boolean isPlayingAd();

    void moveMediaItem(int r1, int r2);

    void moveMediaItems(int r1, int r2, int r3);

    @Deprecated
    void next();

    void pause();

    void play();

    void prepare();

    void release();

    void removeListener(Listener r1);

    void removeMediaItem(int r1);

    void removeMediaItems(int r1, int r2);

    void replaceMediaItem(int r1, MediaItem r2);

    void replaceMediaItems(int r1, int r2, List<MediaItem> r3);

    void seekBack();

    void seekForward();

    void seekTo(int r1, long r2);

    void seekTo(long r1);

    void seekToDefaultPosition();

    void seekToDefaultPosition(int r1);

    void seekToNext();

    void seekToNextMediaItem();

    @Deprecated
    void seekToNextWindow();

    void seekToPrevious();

    void seekToPreviousMediaItem();

    @Deprecated
    void seekToPreviousWindow();

    void setAudioAttributes(AudioAttributes r1, boolean r2);

    @Deprecated
    void setDeviceMuted(boolean r1);

    void setDeviceMuted(boolean r1, int r2);

    @Deprecated
    void setDeviceVolume(int r1);

    void setDeviceVolume(int r1, int r2);

    void setMediaItem(MediaItem r1);

    void setMediaItem(MediaItem r1, long r2);

    void setMediaItem(MediaItem r1, boolean r2);

    void setMediaItems(List<MediaItem> r1);

    void setMediaItems(List<MediaItem> r1, int r2, long r3);

    void setMediaItems(List<MediaItem> r1, boolean r2);

    void setPlayWhenReady(boolean r1);

    void setPlaybackParameters(PlaybackParameters r1);

    void setPlaybackSpeed(float r1);

    void setPlaylistMetadata(MediaMetadata r1);

    void setRepeatMode(int r1);

    void setShuffleModeEnabled(boolean r1);

    void setTrackSelectionParameters(TrackSelectionParameters r1);

    void setVideoSurface(Surface r1);

    void setVideoSurfaceHolder(SurfaceHolder r1);

    void setVideoSurfaceView(SurfaceView r1);

    void setVideoTextureView(TextureView r1);

    void setVolume(float r1);

    void stop();
}
