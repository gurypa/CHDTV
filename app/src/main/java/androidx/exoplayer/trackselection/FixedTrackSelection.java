package androidx.media3.exoplayer.trackselection;

import androidx.media3.common.TrackGroup;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class FixedTrackSelection extends BaseTrackSelection {
    private final Object data;
    private final int reason;

    public FixedTrackSelection(TrackGroup r2, int r3) {
        this(r2, r3, 0);
    }

    public FixedTrackSelection(TrackGroup r7, int r8, int r9) {
        this(r7, r8, r9, 0, null);
    }

    public FixedTrackSelection(TrackGroup r2, int r3, int r4, int r5, Object r6) {
        super(r2, new int[]{r3}, r4);
        this.reason = r5;
        this.data = r6;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int getSelectedIndex() {
        return 0;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public Object getSelectionData() {
        return this.data;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public int getSelectionReason() {
        return this.reason;
    }

    @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
    public void updateSelectedTrack(long r1, long r3, long r5, List<? extends MediaChunk> r7, MediaChunkIterator[] r8) {
    }
}
