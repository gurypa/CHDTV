package androidx.media3.exoplayer.source;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.LoadingInfo;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class CompositeSequenceableLoader implements SequenceableLoader {
    private long lastAudioVideoBufferedPositionUs;
    private final ImmutableList<SequenceableLoaderWithTrackTypes> loadersWithTrackTypes;

    private static final class SequenceableLoaderWithTrackTypes implements SequenceableLoader {
        private final SequenceableLoader loader;
        private final ImmutableList<Integer> trackTypes;

        public SequenceableLoaderWithTrackTypes(SequenceableLoader r2, List<Integer> r3) {
            this.loader = r2;
            this.trackTypes = ImmutableList.copyOf(r3);
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader
        public boolean continueLoading(LoadingInfo r2) {
            return this.loader.continueLoading(r2);
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader
        public long getBufferedPositionUs() {
            return this.loader.getBufferedPositionUs();
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader
        public long getNextLoadPositionUs() {
            return this.loader.getNextLoadPositionUs();
        }

        public ImmutableList<Integer> getTrackTypes() {
            return this.trackTypes;
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader
        public boolean isLoading() {
            return this.loader.isLoading();
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader
        public void reevaluateBuffer(long r2) {
            this.loader.reevaluateBuffer(r2);
        }
    }

    public CompositeSequenceableLoader(List<? extends SequenceableLoader> r6, List<List<Integer>> r7) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        if (r6.size() != r7.size()) goto L5;
        boolean r1 = true;
    L6:
        Assertions.checkArgument(r1);
        int r2 = 0;
    L8:
        if (r2 >= r6.size()) goto L10;
        r0.add(new SequenceableLoaderWithTrackTypes(r6.get(r2), r7.get(r2)));
        r2 = r2 + 1;
        goto L8
    L10:
        this.loadersWithTrackTypes = r0.build();
        this.lastAudioVideoBufferedPositionUs = C0180C.TIME_UNSET;
        return;
    L5:
        r1 = false;
        goto L6
    }

    @Deprecated
    public CompositeSequenceableLoader(SequenceableLoader[] r4) {
        this(ImmutableList.copyOf(r4), Collections.nCopies(r4.length, ImmutableList.m213of(-1)));
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r12) {
        boolean r0 = false;
    L3:
        boolean r1 = false;
        long r2 = getNextLoadPositionUs();
        if (r2 == Long.MIN_VALUE) goto L22;
        int r6 = 0;
    L8:
        if (r6 >= this.loadersWithTrackTypes.size()) goto L20;
        long r7 = this.loadersWithTrackTypes.get(r6).getNextLoadPositionUs();
        if (r7 != Long.MIN_VALUE) goto L12;
    L14:
        boolean r9 = false;
    L16:
        if (r7 == r2) goto L18;
        if (r9 == true) goto L18;
    L19:
        r6 = r6 + 1;
    L18:
        r1 = r1 | this.loadersWithTrackTypes.get(r6).continueLoading(r12);
        goto L19
    L12:
        if (r7 > r12.playbackPositionUs) goto L14;
        r9 = true;
        goto L16
    L20:
        r0 = r0 | r1;
        if (r1 == true) goto L3;
    L22:
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        long r0 = Long.MAX_VALUE;
        long r2 = Long.MAX_VALUE;
        int r4 = 0;
    L4:
        if (r4 >= this.loadersWithTrackTypes.size()) goto L19;
        SequenceableLoaderWithTrackTypes r5 = this.loadersWithTrackTypes.get(r4);
        long r8 = r5.getBufferedPositionUs();
        if (r5.getTrackTypes().contains(1) == true) goto L12;
        if (r5.getTrackTypes().contains(2) == true) goto L12;
        if (r5.getTrackTypes().contains(4) == true) goto L12;
    L15:
        if (r8 == Long.MIN_VALUE) goto L17;
        r0 = Math.min(r0, r8);
    L17:
        r4 = r4 + 1;
    L12:
        if (r8 == Long.MIN_VALUE) goto L15;
        r2 = Math.min(r2, r8);
        goto L15
    L19:
        if (r2 == Long.MAX_VALUE) goto L23;
        this.lastAudioVideoBufferedPositionUs = r2;
        return r2;
    L23:
        if (r0 != Long.MAX_VALUE) goto L25;
        return Long.MIN_VALUE;
    L25:
        if (this.lastAudioVideoBufferedPositionUs == C0180C.TIME_UNSET) goto L28;
        return this.lastAudioVideoBufferedPositionUs;
    L28:
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long r0 = Long.MAX_VALUE;
        int r2 = 0;
    L4:
        if (r2 >= this.loadersWithTrackTypes.size()) goto L10;
        long r6 = this.loadersWithTrackTypes.get(r2).getNextLoadPositionUs();
        if (r6 == Long.MIN_VALUE) goto L8;
        r0 = Math.min(r0, r6);
    L8:
        r2 = r2 + 1;
        goto L4
    L10:
        if (r0 != Long.MAX_VALUE) goto L13;
        return Long.MIN_VALUE;
    L13:
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        int r0 = 0;
    L4:
        if (r0 >= this.loadersWithTrackTypes.size()) goto L10;
        if (this.loadersWithTrackTypes.get(r0).isLoading() == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return true;
    L10:
        return false;
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.loadersWithTrackTypes.size()) goto L6;
        this.loadersWithTrackTypes.get(r0).reevaluateBuffer(r3);
        r0 = r0 + 1;
        goto L4
    }
}
