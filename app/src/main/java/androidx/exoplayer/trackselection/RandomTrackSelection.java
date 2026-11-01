package androidx.media3.exoplayer.trackselection;

import android.os.SystemClock;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public final class RandomTrackSelection extends BaseTrackSelection {
    private final Random random;
    private int selectedIndex;

    public static final class Factory implements ExoTrackSelection.Factory {
        private final Random random;

        public Factory() {
            this.random = new Random();
        }

        public Factory(int r4) {
            this.random = new Random(r4);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection.Factory
        public ExoTrackSelection[] createTrackSelections(ExoTrackSelection.Definition[] r2, BandwidthMeter r3, MediaSource.MediaPeriodId r4, Timeline r5) {
            return TrackSelectionUtil.createTrackSelectionsForDefinitions(r2, new RandomTrackSelection$Factory$$ExternalSyntheticLambda0(this));
        }

        /* JADX INFO: renamed from: lambda$createTrackSelections$0$androidx-media3-exoplayer-trackselection-RandomTrackSelection$Factory */
        /* synthetic */ ExoTrackSelection m148xa167648d(ExoTrackSelection.Definition r6) {
            return new RandomTrackSelection(r6.group, r6.tracks, r6.type, this.random);
        }
    }

    public RandomTrackSelection(TrackGroup r2, int[] r3, int r4, Random r5) {
        super(r2, r3, r4);
        this.random = r5;
        this.selectedIndex = r5.nextInt(this.length);
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public Object getSelectionData() {
        return null;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int getSelectionReason() {
        return 3;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void updateSelectedTrack(long r7, long r9, long r11, List<? extends MediaChunk> r13, MediaChunkIterator[] r14) {
        long r0 = SystemClock.elapsedRealtime();
        int r2 = 0;
        int r3 = 0;
    L4:
        if (r3 >= this.length) goto L9;
        if (isTrackExcluded(r3, r0) == true) goto L8;
        r2 = r2 + 1;
    L8:
        r3 = r3 + 1;
        goto L4
    L9:
        this.selectedIndex = this.random.nextInt(r2);
        if (r2 == this.length) goto L22;
        int r4 = 0;
        int r5 = 0;
    L13:
        if (r5 >= this.length) goto L30;
        if (isTrackExcluded(r5, r0) == true) goto L21;
        int r6 = r4 + 1;
        if (this.selectedIndex == r4) goto L18;
        r4 = r6;
        goto L21
    L18:
        this.selectedIndex = r5;
        return;
    L21:
        r5 = r5 + 1;
        goto L13
    L30:
        return;
    }
}
