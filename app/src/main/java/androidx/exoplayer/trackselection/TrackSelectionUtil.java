package androidx.media3.exoplayer.trackselection;

import android.graphics.Point;
import android.os.SystemClock;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class TrackSelectionUtil {

    public interface AdaptiveTrackSelectionFactory {
        ExoTrackSelection createAdaptiveTrackSelection(ExoTrackSelection.Definition r1);
    }

    private TrackSelectionUtil() {
    }

    public static Tracks buildTracks(MappingTrackSelector.MappedTrackInfo r4, TrackSelection[] r5) {
        List[] r0 = new List[r5.length];
        int r1 = 0;
    L4:
        if (r1 >= r5.length) goto L11;
        TrackSelection r2 = r5[r1];
        if (r2 == null) goto L8;
        ImmutableList r3 = ImmutableList.m213of(r2);
    L9:
        r0[r1] = r3;
        r1 = r1 + 1;
        goto L4
    L8:
        r3 = ImmutableList.m212of();
        goto L9
    L11:
        return buildTracks(r4, r0);
    }

    public static Tracks buildTracks(MappingTrackSelector.MappedTrackInfo r16, List<? extends TrackSelection>[] r17) {
        ImmutableList.Builder r1 = new ImmutableList.Builder();
        int r2 = 0;
    L3:
        boolean r4 = false;
        if (r2 >= r16.getRendererCount()) goto L27;
        TrackGroupArray r3 = r16.getTrackGroups(r2);
        List<? extends TrackSelection> r5 = r17[r2];
        int r6 = 0;
    L7:
        if (r6 >= r3.length) goto L26;
        TrackGroup r7 = r3.get(r6);
        if (r16.getAdaptiveSupport(r2, r6, r4) == 0) goto L11;
        boolean r8 = true;
    L12:
        int[] r9 = new int[r7.length];
        boolean[] r10 = new boolean[r7.length];
        int r11 = 0;
    L14:
        if (r11 >= r7.length) goto L25;
        r9[r11] = r16.getTrackSupport(r2, r6, r11);
        boolean r12 = false;
        int r13 = 0;
    L17:
        if (r13 >= r5.size()) goto L24;
        TrackSelection r14 = r5.get(r13);
        if (r14.getTrackGroup().equals(r7) == false) goto L23;
        if (r14.indexOf(r11) == (-1)) goto L23;
        r12 = true;
    L23:
        r13 = r13 + 1;
    L24:
        r10[r11] = r12;
        r11 = r11 + 1;
        goto L14
    L25:
        r1.add(new Tracks.Group(r7, r8, r9, r10));
        r6 = r6 + 1;
        r4 = false;
        goto L7
    L11:
        r8 = r4;
        goto L12
    L26:
        r2 = r2 + 1;
        goto L3
    L27:
        TrackGroupArray r15 = r16.getUnmappedTrackGroups();
        int r18 = 0;
    L29:
        if (r18 >= r15.length) goto L32;
        TrackGroup r19 = r15.get(r18);
        int[] r20 = new int[r19.length];
        Arrays.fill(r20, 0);
        r1.add(new Tracks.Group(r19, false, r20, new boolean[r19.length]));
        r18 = r18 + 1;
        goto L29
    L32:
        return new Tracks(r1.build());
    }

    public static LoadErrorHandlingPolicy.FallbackOptions createFallbackOptions(ExoTrackSelection r7) {
        long r0 = SystemClock.elapsedRealtime();
        int r2 = r7.length();
        int r3 = 0;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L9;
        if (r7.isTrackExcluded(r4, r0) == false) goto L7;
        r3 = r3 + 1;
    L7:
        r4 = r4 + 1;
        goto L3
    L9:
        return new LoadErrorHandlingPolicy.FallbackOptions(1, 0, r2, r3);
    }

    public static ExoTrackSelection[] createTrackSelectionsForDefinitions(ExoTrackSelection.Definition[] r8, AdaptiveTrackSelectionFactory r9) {
        ExoTrackSelection[] r0 = new ExoTrackSelection[r8.length];
        boolean r1 = false;
        int r2 = 0;
    L4:
        if (r2 >= r8.length) goto L14;
        ExoTrackSelection.Definition r3 = r8[r2];
        if (r3 == null) goto L13;
        if (r3.tracks.length <= 1) goto L12;
        if (r1 == true) goto L12;
        r1 = true;
        r0[r2] = r9.createAdaptiveTrackSelection(r3);
    L12:
        r0[r2] = new FixedTrackSelection(r3.group, r3.tracks[0], r3.type);
    L13:
        r2 = r2 + 1;
        goto L4
    L14:
        return r0;
    }

    public static Point getMaxVideoSizeInViewport(boolean r3, int r4, int r5, int r6, int r7) {
        if (r3 == false) goto L13;
        boolean r0 = true;
        if (r6 <= r7) goto L6;
        boolean r2 = true;
    L7:
        if (r4 > r5) goto L10;
        r0 = false;
    L10:
        if (r2 == r0) goto L13;
        r4 = r5;
        r5 = r4;
        goto L13
    L6:
        r2 = false;
    L13:
        if ((r6 * r5) < (r7 * r4)) goto L17;
        return new Point(r4, Util.ceilDivide(r4 * r7, r6));
    L17:
        return new Point(Util.ceilDivide(r5 * r6, r7), r5);
    }

    @Deprecated
    public static DefaultTrackSelector.Parameters updateParametersWithOverride(DefaultTrackSelector.Parameters r2, int r3, TrackGroupArray r4, boolean r5, DefaultTrackSelector.SelectionOverride r6) {
        DefaultTrackSelector.Parameters.Builder r0 = r2.buildUpon().clearSelectionOverrides(r3).setRendererDisabled(r3, r5);
        if (r6 == null) goto L6;
        r0.setSelectionOverride(r3, r4, r6);
    L6:
        return r0.build();
    }
}
