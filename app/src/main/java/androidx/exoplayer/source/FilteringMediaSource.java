package androidx.media3.exoplayer.source;

import androidx.media3.common.StreamKey;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class FilteringMediaSource extends WrappingMediaSource {
    private final ImmutableSet<Integer> trackTypes;

    private static final class FilteringMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
        private MediaPeriod.Callback callback;
        private TrackGroupArray filteredTrackGroups;
        public final MediaPeriod mediaPeriod;
        private final ImmutableSet<Integer> trackTypes;

        public FilteringMediaPeriod(MediaPeriod r1, ImmutableSet<Integer> r2) {
            this.mediaPeriod = r1;
            this.trackTypes = r2;
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public boolean continueLoading(LoadingInfo r2) {
            return this.mediaPeriod.continueLoading(r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void discardBuffer(long r2, boolean r4) {
            this.mediaPeriod.discardBuffer(r2, r4);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long getAdjustedSeekPositionUs(long r3, SeekParameters r5) {
            return this.mediaPeriod.getAdjustedSeekPositionUs(r3, r5);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public long getBufferedPositionUs() {
            return this.mediaPeriod.getBufferedPositionUs();
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public long getNextLoadPositionUs() {
            return this.mediaPeriod.getNextLoadPositionUs();
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r2) {
            return this.mediaPeriod.getStreamKeys(r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public TrackGroupArray getTrackGroups() {
            return (TrackGroupArray) Assertions.checkNotNull(this.filteredTrackGroups);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public boolean isLoading() {
            return this.mediaPeriod.isLoading();
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void maybeThrowPrepareError() throws IOException {
            this.mediaPeriod.maybeThrowPrepareError();
        }

        public void onContinueLoadingRequested(MediaPeriod r2) {
            ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
        public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
            onContinueLoadingRequested((MediaPeriod) r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
        public void onPrepared(MediaPeriod r7) {
            TrackGroupArray r0 = r7.getTrackGroups();
            ImmutableList.Builder r1 = ImmutableList.builder();
            int r2 = 0;
        L4:
            if (r2 >= r0.length) goto L9;
            TrackGroup r3 = r0.get(r2);
            if (this.trackTypes.contains(Integer.valueOf(r3.type)) == false) goto L8;
            r1.add(r3);
        L8:
            r2 = r2 + 1;
            goto L4
        L9:
            this.filteredTrackGroups = new TrackGroupArray((TrackGroup[]) r1.build().toArray(new TrackGroup[0]));
            ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public void prepare(MediaPeriod.Callback r2, long r3) {
            this.callback = r2;
            this.mediaPeriod.prepare(this, r3);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long readDiscontinuity() {
            return this.mediaPeriod.readDiscontinuity();
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
        public void reevaluateBuffer(long r2) {
            this.mediaPeriod.reevaluateBuffer(r2);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long seekToUs(long r3) {
            return this.mediaPeriod.seekToUs(r3);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod
        public long selectTracks(ExoTrackSelection[] r8, boolean[] r9, SampleStream[] r10, boolean[] r11, long r12) {
            return this.mediaPeriod.selectTracks(r8, r9, r10, r11, r12);
        }
    }

    public FilteringMediaSource(MediaSource r2, int r3) {
        this(r2, ImmutableSet.m260of(Integer.valueOf(r3)));
    }

    public FilteringMediaSource(MediaSource r2, Set<Integer> r3) {
        super(r2);
        this.trackTypes = ImmutableSet.copyOf(r3);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r4, Allocator r5, long r6) {
        return new FilteringMediaPeriod(super.createPeriod(r4, r5, r6), this.trackTypes);
    }

    @Override // androidx.media3.exoplayer.source.WrappingMediaSource, androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r2) {
        super.releasePeriod(((FilteringMediaPeriod) r2).mediaPeriod);
    }
}
