package androidx.media3.exoplayer.trackselection;

import android.util.Pair;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.RendererConfiguration;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class MappingTrackSelector extends TrackSelector {
    private MappedTrackInfo currentMappedTrackInfo;

    public static final class MappedTrackInfo {
        public static final int RENDERER_SUPPORT_EXCEEDS_CAPABILITIES_TRACKS = 2;
        public static final int RENDERER_SUPPORT_NO_TRACKS = 0;
        public static final int RENDERER_SUPPORT_PLAYABLE_TRACKS = 3;
        public static final int RENDERER_SUPPORT_UNSUPPORTED_TRACKS = 1;
        private final int rendererCount;
        private final int[][][] rendererFormatSupports;
        private final int[] rendererMixedMimeTypeAdaptiveSupports;
        private final String[] rendererNames;
        private final TrackGroupArray[] rendererTrackGroups;
        private final int[] rendererTrackTypes;
        private final TrackGroupArray unmappedTrackGroups;

        @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface RendererSupport {
        }

        MappedTrackInfo(String[] r2, int[] r3, TrackGroupArray[] r4, int[] r5, int[][][] r6, TrackGroupArray r7) {
            this.rendererNames = r2;
            this.rendererTrackTypes = r3;
            this.rendererTrackGroups = r4;
            this.rendererFormatSupports = r6;
            this.rendererMixedMimeTypeAdaptiveSupports = r5;
            this.unmappedTrackGroups = r7;
            this.rendererCount = r3.length;
        }

        public int getAdaptiveSupport(int r7, int r8, boolean r9) {
            int r0 = this.rendererTrackGroups[r7].get(r8).length;
            int[] r1 = new int[r0];
            int r2 = 0;
            int r3 = 0;
        L3:
            if (r3 >= r0) goto L12;
            int r4 = getTrackSupport(r7, r8, r3);
            if (r4 == 4) goto L9;
            if (r9 == false) goto L10;
            if (r4 == 3) goto L9;
        L10:
            r3 = r3 + 1;
        L9:
            r1[r2] = r3;
            r2 = r2 + 1;
            goto L10
        L12:
            return getAdaptiveSupport(r7, r8, Arrays.copyOf(r1, r2));
        }

        public int getAdaptiveSupport(int r9, int r10, int[] r11) {
            int r0 = 0;
            int r1 = 16;
            boolean r2 = false;
            String r3 = null;
            int r4 = 0;
        L4:
            if (r4 >= r11.length) goto L10;
            String r6 = this.rendererTrackGroups[r9].get(r10).getFormat(r11[r4]).sampleMimeType;
            int r7 = r0 + 1;
            if (r0 != 0) goto L8;
            r3 = r6;
        L9:
            r1 = Math.min(r1, RendererCapabilities.CC.getAdaptiveSupport(this.rendererFormatSupports[r9][r10][r4]));
            r4 = r4 + 1;
            r0 = r7;
            goto L4
        L8:
            r2 = (!Util.areEqual(r3, r6)) | r2;
            goto L9
        L10:
            if (r2 == false) goto L13;
            return Math.min(r1, this.rendererMixedMimeTypeAdaptiveSupports[r9]);
        L13:
            return r1;
        }

        public int getCapabilities(int r2, int r3, int r4) {
            return this.rendererFormatSupports[r2][r3][r4];
        }

        public int getRendererCount() {
            return this.rendererCount;
        }

        public String getRendererName(int r2) {
            return this.rendererNames[r2];
        }

        public int getRendererSupport(int r11) {
            int r0 = 0;
            int[][] r1 = this.rendererFormatSupports[r11];
            int r2 = r1.length;
            int r4 = 0;
        L3:
            if (r4 >= r2) goto L16;
            int[] r5 = r1[r4];
            int r6 = r5.length;
            int r7 = 0;
        L5:
            if (r7 >= r6) goto L15;
            switch(RendererCapabilities.CC.getFormatSupport(r5[r7])) {
                case 0: goto L13;
                case 1: goto L13;
                case 2: goto L13;
                case 3: goto L12;
                case 4: goto L10;
                default: goto L9;
            };
        L12:
            int r9 = 2;
        L14:
            r0 = Math.max(r0, r9);
            r7 = r7 + 1;
            goto L5
        L13:
            r9 = 1;
            goto L14
        L9:
            throw new IllegalStateException();
        L10:
            return 3;
        L15:
            r4 = r4 + 1;
            goto L3
        L16:
            return r0;
        }

        public int getRendererType(int r2) {
            return this.rendererTrackTypes[r2];
        }

        public TrackGroupArray getTrackGroups(int r2) {
            return this.rendererTrackGroups[r2];
        }

        public int getTrackSupport(int r2, int r3, int r4) {
            return RendererCapabilities.CC.getFormatSupport(getCapabilities(r2, r3, r4));
        }

        public int getTypeSupport(int r4) {
            int r0 = 0;
            int r1 = 0;
        L4:
            if (r1 >= this.rendererCount) goto L9;
            if (this.rendererTrackTypes[r1] != r4) goto L8;
            r0 = Math.max(r0, getRendererSupport(r1));
        L8:
            r1 = r1 + 1;
            goto L4
        L9:
            return r0;
        }

        public TrackGroupArray getUnmappedTrackGroups() {
            return this.unmappedTrackGroups;
        }
    }

    public MappingTrackSelector() {
    }

    private static int findRenderer(RendererCapabilities[] r8, TrackGroup r9, int[] r10, boolean r11) throws ExoPlaybackException {
        int r0 = r8.length;
        int r1 = 0;
        boolean r2 = true;
        int r3 = 0;
    L4:
        if (r3 >= r8.length) goto L20;
        RendererCapabilities r4 = r8[r3];
        int r5 = 0;
        int r6 = 0;
    L7:
        if (r6 >= r9.length) goto L10;
        r5 = Math.max(r5, RendererCapabilities.CC.getFormatSupport(r4.supportsFormat(r9.getFormat(r6))));
        r6 = r6 + 1;
        goto L7
    L10:
        if (r10[r3] != 0) goto L12;
        boolean r7 = true;
    L13:
        if (r5 > r1) goto L18;
        if (r5 != r1) goto L19;
        if (r11 == false) goto L19;
        if (r2 == true) goto L19;
        if (r7 == true) goto L18;
    L19:
        r3 = r3 + 1;
    L18:
        r0 = r3;
        r1 = r5;
        r2 = r7;
        goto L19
    L12:
        r7 = false;
        goto L13
    L20:
        return r0;
    }

    private static int[] getFormatSupport(RendererCapabilities r3, TrackGroup r4) throws ExoPlaybackException {
        int[] r0 = new int[r4.length];
        int r1 = 0;
    L4:
        if (r1 >= r4.length) goto L6;
        r0[r1] = r3.supportsFormat(r4.getFormat(r1));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    private static int[] getMixedMimeTypeAdaptationSupports(RendererCapabilities[] r3) throws ExoPlaybackException {
        int[] r0 = new int[r3.length];
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L6;
        r0[r1] = r3[r1].supportsMixedMimeTypeAdaptation();
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public final MappedTrackInfo getCurrentMappedTrackInfo() {
        return this.currentMappedTrackInfo;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public final void onSelectionActivated(Object r2) {
        this.currentMappedTrackInfo = (MappedTrackInfo) r2;
    }

    protected abstract Pair<RendererConfiguration[], ExoTrackSelection[]> selectTracks(MappedTrackInfo r1, int[][][] r2, int[] r3, MediaSource.MediaPeriodId r4, Timeline r5) throws ExoPlaybackException;

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public final TrackSelectorResult selectTracks(RendererCapabilities[] r17, TrackGroupArray r18, MediaSource.MediaPeriodId r19, Timeline r20) throws ExoPlaybackException {
        int[] r2 = new int[r17.length + 1];
        TrackGroup[][] r4 = new TrackGroup[r17.length + 1][];
        int[][][] r11 = new int[r17.length + 1][][];
        int r5 = 0;
    L4:
        if (r5 >= r4.length) goto L6;
        r4[r5] = new TrackGroup[r18.length];
        r11[r5] = new int[r18.length][];
        r5 = r5 + 1;
        goto L4
    L6:
        int[] r9 = getMixedMimeTypeAdaptationSupports(r17);
        int r6 = 0;
    L8:
        if (r6 >= r18.length) goto L18;
        TrackGroup r7 = r18.get(r6);
        if (r7.type != 5) goto L12;
        boolean r8 = true;
    L13:
        int r10 = findRenderer(r17, r7, r2, r8);
        if (r10 != r17.length) goto L16;
        int[] r12 = new int[r7.length];
    L17:
        int r13 = r2[r10];
        r4[r10][r13] = r7;
        r11[r10][r13] = r12;
        r2[r10] = r2[r10] + 1;
        r6 = r6 + 1;
        goto L8
    L16:
        r12 = getFormatSupport(r17[r10], r7);
        goto L17
    L12:
        r8 = false;
        goto L13
    L18:
        TrackGroupArray[] r3 = new TrackGroupArray[r17.length];
        String[] r14 = new String[r17.length];
        int[] r15 = new int[r17.length];
        int r16 = 0;
    L20:
        if (r16 >= r17.length) goto L22;
        int r21 = r2[r16];
        r3[r16] = new TrackGroupArray((TrackGroup[]) Util.nullSafeArrayCopy(r4[r16], r21));
        r11[r16] = (int[][]) Util.nullSafeArrayCopy(r11[r16], r21);
        r14[r16] = r17[r16].getName();
        r15[r16] = r17[r16].getTrackType();
        r16 = r16 + 1;
        goto L20
    L22:
        MappedTrackInfo r22 = new MappedTrackInfo(r14, r15, r3, r9, r11, new TrackGroupArray((TrackGroup[]) Util.nullSafeArrayCopy(r4[r17.length], r2[r17.length])));
        Pair<RendererConfiguration[], ExoTrackSelection[]> r110 = selectTracks(r22, r11, r9, r19, r20);
        return new TrackSelectorResult((RendererConfiguration[]) r110.first, (ExoTrackSelection[]) r110.second, TrackSelectionUtil.buildTracks(r22, (TrackSelection[]) r110.second), r22);
    }
}
