package androidx.media3.exoplayer.trackselection;

import androidx.media3.common.Format;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Log;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ExoTrackSelection extends TrackSelection {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.trackselection.ExoTrackSelection$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static long $default$getLatestBitrateEstimate(ExoTrackSelection r2) {
            return -2147483647L;
        }

        public static void $default$onDiscontinuity(ExoTrackSelection r0) {
        }

        public static void $default$onPlayWhenReadyChanged(ExoTrackSelection r0, boolean r1) {
        }

        public static void $default$onRebuffer(ExoTrackSelection r0) {
        }

        public static boolean $default$shouldCancelChunkLoad(ExoTrackSelection r1, long r2, Chunk r4, List r5) {
            return false;
        }
    }

    public static final class Definition {
        private static final String TAG = "ETSDefinition";
        public final TrackGroup group;
        public final int[] tracks;
        public final int type;

        public Definition(TrackGroup r2, int... r3) {
            this(r2, r3, 0);
        }

        public Definition(TrackGroup r4, int[] r5, int r6) {
            if (r5.length != 0) goto L5;
            Log.m45e(TAG, "Empty tracks are not allowed", new IllegalArgumentException());
        L5:
            this.group = r4;
            this.tracks = r5;
            this.type = r6;
        }
    }

    public interface Factory {
        ExoTrackSelection[] createTrackSelections(Definition[] r1, BandwidthMeter r2, MediaSource.MediaPeriodId r3, Timeline r4);
    }

    void disable();

    void enable();

    int evaluateQueueSize(long r1, List<? extends MediaChunk> r3);

    boolean excludeTrack(int r1, long r2);

    long getLatestBitrateEstimate();

    Format getSelectedFormat();

    int getSelectedIndex();

    int getSelectedIndexInTrackGroup();

    Object getSelectionData();

    int getSelectionReason();

    boolean isTrackExcluded(int r1, long r2);

    void onDiscontinuity();

    void onPlayWhenReadyChanged(boolean r1);

    void onPlaybackSpeed(float r1);

    void onRebuffer();

    boolean shouldCancelChunkLoad(long r1, Chunk r3, List<? extends MediaChunk> r4);

    void updateSelectedTrack(long r1, long r3, long r5, List<? extends MediaChunk> r7, MediaChunkIterator[] r8);
}
