package androidx.media3.exoplayer.trackselection;

import android.os.SystemClock;
import androidx.media3.common.Format;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.chunk.Chunk;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseTrackSelection implements ExoTrackSelection {
    private final long[] excludeUntilTimes;
    private final Format[] formats;
    protected final TrackGroup group;
    private int hashCode;
    protected final int length;
    private boolean playWhenReady;
    protected final int[] tracks;
    private final int type;

    public BaseTrackSelection(TrackGroup r2, int... r3) {
        this(r2, r3, 0);
    }

    public BaseTrackSelection(TrackGroup r5, int[] r6, int r7) {
        if (r6.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.type = r7;
        this.group = (TrackGroup) Assertions.checkNotNull(r5);
        this.length = r6.length;
        this.formats = new Format[this.length];
        int r1 = 0;
    L8:
        if (r1 >= r6.length) goto L10;
        this.formats[r1] = r5.getFormat(r6[r1]);
        r1 = r1 + 1;
        goto L8
    L10:
        Arrays.sort(this.formats, new BaseTrackSelection$$ExternalSyntheticLambda0());
        this.tracks = new int[this.length];
        int r2 = 0;
    L12:
        if (r2 >= this.length) goto L14;
        this.tracks[r2] = r5.indexOf(this.formats[r2]);
        r2 = r2 + 1;
        goto L12
    L14:
        this.excludeUntilTimes = new long[this.length];
        this.playWhenReady = false;
        return;
    L5:
        r0 = false;
        goto L6
    }

    static /* synthetic */ int lambda$new$0(Format r2, Format r3) {
        return r3.bitrate - r2.bitrate;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void disable() {
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void enable() {
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L17;
        BaseTrackSelection r2 = (BaseTrackSelection) r6;
        if (this.group.equals(r2.group) == false) goto L16;
        if (Arrays.equals(this.tracks, r2.tracks) == false) goto L16;
        return true;
    L16:
        return false;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int evaluateQueueSize(long r2, List<? extends MediaChunk> r4) {
        return r4.size();
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public boolean excludeTrack(int r12, long r13) {
        long r0 = SystemClock.elapsedRealtime();
        int r3 = 0;
        boolean r6 = isTrackExcluded(r12, r0);
    L3:
        boolean r4 = false;
        if (r3 >= this.length) goto L11;
        if (r6 == true) goto L11;
        if (r3 == r12) goto L10;
        if (isTrackExcluded(r3, r0) == true) goto L10;
        r4 = true;
    L10:
        r6 = r4;
        r3 = r3 + 1;
    L11:
        if (r6 == true) goto L13;
        return false;
    L13:
        this.excludeUntilTimes[r12] = Math.max(this.excludeUntilTimes[r12], Util.addWithOverflowDefault(r0, r13, Long.MAX_VALUE));
        return true;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final Format getFormat(int r2) {
        return this.formats[r2];
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final int getIndexInTrackGroup(int r2) {
        return this.tracks[r2];
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public /* synthetic */ long getLatestBitrateEstimate() {
        return ExoTrackSelection.CC.$default$getLatestBitrateEstimate(this);
    }

    protected final boolean getPlayWhenReady() {
        return this.playWhenReady;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public final Format getSelectedFormat() {
        return this.formats[getSelectedIndex()];
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public final int getSelectedIndexInTrackGroup() {
        return this.tracks[getSelectedIndex()];
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final TrackGroup getTrackGroup() {
        return this.group;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final int getType() {
        return this.type;
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L6;
        this.hashCode = (System.identityHashCode(this.group) * 31) + Arrays.hashCode(this.tracks);
    L6:
        return this.hashCode;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final int indexOf(int r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.length) goto L9;
        if (this.tracks[r0] == r3) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final int indexOf(Format r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.length) goto L9;
        if (this.formats[r0] == r3) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L9:
        return -1;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public boolean isTrackExcluded(int r4, long r5) {
        if (this.excludeUntilTimes[r4] <= r5) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelection
    public final int length() {
        return this.tracks.length;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public /* synthetic */ void onDiscontinuity() {
        ExoTrackSelection.CC.$default$onDiscontinuity(this);
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void onPlayWhenReadyChanged(boolean r1) {
        this.playWhenReady = r1;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void onPlaybackSpeed(float r1) {
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public /* synthetic */ void onRebuffer() {
        ExoTrackSelection.CC.$default$onRebuffer(this);
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public /* synthetic */ boolean shouldCancelChunkLoad(long r1, Chunk r3, List r4) {
        return ExoTrackSelection.CC.$default$shouldCancelChunkLoad(this, r1, r3, r4);
    }
}
