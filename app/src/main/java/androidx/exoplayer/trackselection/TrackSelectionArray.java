package androidx.media3.exoplayer.trackselection;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class TrackSelectionArray {
    private int hashCode;
    public final int length;
    private final TrackSelection[] trackSelections;

    public TrackSelectionArray(TrackSelection... r2) {
        this.trackSelections = r2;
        this.length = r2.length;
    }

    public boolean equals(Object r4) {
        if (this != r4) goto L5;
        return true;
    L5:
        if (r4 != null) goto L7;
        return false;
    L7:
        if (getClass() == r4.getClass()) goto L10;
        return false;
    L10:
        return Arrays.equals(this.trackSelections, ((TrackSelectionArray) r4).trackSelections);
    }

    public TrackSelection get(int r2) {
        return this.trackSelections[r2];
    }

    public TrackSelection[] getAll() {
        return (TrackSelection[]) this.trackSelections.clone();
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L6;
        this.hashCode = (17 * 31) + Arrays.hashCode(this.trackSelections);
    L6:
        return this.hashCode;
    }
}
