package androidx.media3.exoplayer.source;

import android.os.Bundle;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class TrackGroupArray {
    public static final TrackGroupArray EMPTY = null;
    private static final String FIELD_TRACK_GROUPS = null;
    private static final String TAG = "TrackGroupArray";
    private int hashCode;
    public final int length;
    private final ImmutableList<TrackGroup> trackGroups;

    static {
        EMPTY = new TrackGroupArray(new TrackGroup[0]);
        FIELD_TRACK_GROUPS = Util.intToStringMaxRadix(0);
    }

    public TrackGroupArray(TrackGroup... r2) {
        this.trackGroups = ImmutableList.copyOf(r2);
        this.length = r2.length;
        verifyCorrectness();
    }

    public static TrackGroupArray fromBundle(Bundle r4) {
        ArrayList r0 = r4.getParcelableArrayList(FIELD_TRACK_GROUPS);
        if (r0 != null) goto L7;
        return new TrackGroupArray(new TrackGroup[0]);
    L7:
        return new TrackGroupArray((TrackGroup[]) BundleCollectionUtil.fromBundleList(new TrackGroupArray$$ExternalSyntheticLambda2(), r0).toArray(new TrackGroup[0]));
    }

    static /* synthetic */ Integer lambda$getTrackTypes$0(TrackGroup r1) {
        return Integer.valueOf(r1.type);
    }

    private void verifyCorrectness() {
        int r0 = 0;
    L4:
        if (r0 >= this.trackGroups.size()) goto L13;
        int r1 = r0 + 1;
    L7:
        if (r1 >= this.trackGroups.size()) goto L12;
        if (this.trackGroups.get(r0).equals(this.trackGroups.get(r1)) == false) goto L11;
        Log.m45e(TAG, "", new IllegalArgumentException("Multiple identical TrackGroups added to one TrackGroupArray."));
    L11:
        r1 = r1 + 1;
        goto L7
    L12:
        r0 = r0 + 1;
        goto L4
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
        TrackGroupArray r2 = (TrackGroupArray) r6;
        if (this.length != r2.length) goto L16;
        if (this.trackGroups.equals(r2.trackGroups) == false) goto L16;
        return true;
    L16:
        return false;
    }

    public TrackGroup get(int r2) {
        return this.trackGroups.get(r2);
    }

    public ImmutableList<Integer> getTrackTypes() {
        return ImmutableList.copyOf(Lists.transform(this.trackGroups, new TrackGroupArray$$ExternalSyntheticLambda0()));
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L6;
        this.hashCode = this.trackGroups.hashCode();
    L6:
        return this.hashCode;
    }

    public int indexOf(TrackGroup r3) {
        int r0 = this.trackGroups.indexOf(r3);
        if (r0 >= 0) goto L7;
        return -1;
    L7:
        return r0;
    }

    public boolean isEmpty() {
        if (this.length != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putParcelableArrayList(FIELD_TRACK_GROUPS, BundleCollectionUtil.toBundleArrayList(this.trackGroups, new TrackGroupArray$$ExternalSyntheticLambda1()));
        return r0;
    }
}
