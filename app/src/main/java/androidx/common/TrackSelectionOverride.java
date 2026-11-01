package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class TrackSelectionOverride {
    private static final String FIELD_TRACKS = null;
    private static final String FIELD_TRACK_GROUP = null;
    public final TrackGroup mediaTrackGroup;
    public final ImmutableList<Integer> trackIndices;

    static {
        FIELD_TRACK_GROUP = Util.intToStringMaxRadix(0);
        FIELD_TRACKS = Util.intToStringMaxRadix(1);
    }

    public TrackSelectionOverride(TrackGroup r2, int r3) {
        this(r2, ImmutableList.m213of(Integer.valueOf(r3)));
    }

    public TrackSelectionOverride(TrackGroup r3, List<Integer> r4) {
        if (r4.isEmpty() == false) goto L5;
    L11:
        this.mediaTrackGroup = r3;
        this.trackIndices = ImmutableList.copyOf(r4);
        return;
    L5:
        if (((Integer) Collections.min(r4)).intValue() < 0) goto L10;
        if (((Integer) Collections.max(r4)).intValue() < r3.length) goto L11;
    L10:
        throw new IndexOutOfBoundsException();
    }

    public static TrackSelectionOverride fromBundle(Bundle r5) {
        return new TrackSelectionOverride(TrackGroup.fromBundle((Bundle) Assertions.checkNotNull(r5.getBundle(FIELD_TRACK_GROUP))), Ints.asList((int[]) Assertions.checkNotNull(r5.getIntArray(FIELD_TRACKS))));
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
        TrackSelectionOverride r2 = (TrackSelectionOverride) r6;
        if (this.mediaTrackGroup.equals(r2.mediaTrackGroup) == false) goto L16;
        if (this.trackIndices.equals(r2.trackIndices) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public int getType() {
        return this.mediaTrackGroup.type;
    }

    public int hashCode() {
        return this.mediaTrackGroup.hashCode() + (this.trackIndices.hashCode() * 31);
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putBundle(FIELD_TRACK_GROUP, this.mediaTrackGroup.toBundle());
        r0.putIntArray(FIELD_TRACKS, Ints.toArray(this.trackIndices));
        return r0;
    }
}
