package androidx.media3.exoplayer.trackselection;

import androidx.media3.common.Tracks;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.RendererConfiguration;

/* JADX INFO: loaded from: classes.dex */
public final class TrackSelectorResult {
    public final Object info;
    public final int length;
    public final RendererConfiguration[] rendererConfigurations;
    public final ExoTrackSelection[] selections;
    public final Tracks tracks;

    public TrackSelectorResult(RendererConfiguration[] r3, ExoTrackSelection[] r4, Tracks r5, Object r6) {
        if (r3.length != r4.length) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        this.rendererConfigurations = r3;
        this.selections = (ExoTrackSelection[]) r4.clone();
        this.tracks = r5;
        this.info = r6;
        this.length = r3.length;
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Deprecated
    public TrackSelectorResult(RendererConfiguration[] r2, ExoTrackSelection[] r3, Object r4) {
        this(r2, r3, Tracks.EMPTY, r4);
    }

    public boolean isEquivalent(TrackSelectorResult r4) {
        if (r4 != null) goto L5;
    L16:
        return false;
    L5:
        if (r4.selections.length != this.selections.length) goto L16;
        int r1 = 0;
    L9:
        if (r1 >= this.selections.length) goto L14;
        if (isEquivalent(r4, r1) == false) goto L12;
        r1 = r1 + 1;
        goto L9
    L12:
        return false;
    L14:
        return true;
    }

    public boolean isEquivalent(TrackSelectorResult r4, int r5) {
        if (r4 != null) goto L6;
        return false;
    L6:
        if (Util.areEqual(this.rendererConfigurations[r5], r4.rendererConfigurations[r5]) == true) goto L8;
        return false;
    L8:
        if (Util.areEqual(this.selections[r5], r4.selections[r5]) == false) goto L13;
        return true;
    L13:
        return false;
    }

    public boolean isRendererEnabled(int r2) {
        if (this.rendererConfigurations[r2] == null) goto L5;
        return true;
    L5:
        return false;
    }
}
