package androidx.media3.exoplayer.smoothstreaming;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.StreamKey;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.source.CompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.source.chunk.ChunkSampleStream;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoaderErrorThrower;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class SsMediaPeriod implements MediaPeriod, SequenceableLoader.Callback<ChunkSampleStream<SsChunkSource>> {
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final SsChunkSource.Factory chunkSourceFactory;
    private final CmcdConfiguration cmcdConfiguration;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private SsManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
    private ChunkSampleStream<SsChunkSource>[] sampleStreams;
    private final TrackGroupArray trackGroups;
    private final TransferListener transferListener;

    public SsMediaPeriod(SsManifest r2, SsChunkSource.Factory r3, TransferListener r4, CompositeSequenceableLoaderFactory r5, CmcdConfiguration r6, DrmSessionManager r7, DrmSessionEventListener.EventDispatcher r8, LoadErrorHandlingPolicy r9, MediaSourceEventListener.EventDispatcher r10, LoaderErrorThrower r11, Allocator r12) {
        this.manifest = r2;
        this.chunkSourceFactory = r3;
        this.transferListener = r4;
        this.manifestLoaderErrorThrower = r11;
        this.cmcdConfiguration = r6;
        this.drmSessionManager = r7;
        this.drmEventDispatcher = r8;
        this.loadErrorHandlingPolicy = r9;
        this.mediaSourceEventDispatcher = r10;
        this.allocator = r12;
        this.compositeSequenceableLoaderFactory = r5;
        this.trackGroups = buildTrackGroups(r2, r7, r3);
        this.sampleStreams = newSampleStreamArray(0);
        this.compositeSequenceableLoader = r5.empty();
    }

    private ChunkSampleStream<SsChunkSource> buildSampleStream(ExoTrackSelection r17, long r18) {
        int r9 = this.trackGroups.indexOf(r17.getTrackGroup());
        SsChunkSource r4 = this.chunkSourceFactory.createChunkSource(this.manifestLoaderErrorThrower, this.manifest, r9, r17, this.transferListener, this.cmcdConfiguration);
        return new ChunkSampleStream(this.manifest.streamElements[r9].type, null, null, r4, this, this.allocator, r18, this.drmSessionManager, this.drmEventDispatcher, this.loadErrorHandlingPolicy, this.mediaSourceEventDispatcher, false, null);
    }

    private static TrackGroupArray buildTrackGroups(SsManifest r8, DrmSessionManager r9, SsChunkSource.Factory r10) {
        TrackGroup[] r0 = new TrackGroup[r8.streamElements.length];
        int r1 = 0;
    L4:
        if (r1 >= r8.streamElements.length) goto L11;
        Format[] r2 = r8.streamElements[r1].formats;
        Format[] r3 = new Format[r2.length];
        int r4 = 0;
    L7:
        if (r4 >= r2.length) goto L9;
        Format r5 = r2[r4];
        r3[r4] = r10.getOutputTextFormat(r5.buildUpon().setCryptoType(r9.getCryptoType(r5)).build());
        r4 = r4 + 1;
        goto L7
    L9:
        r0[r1] = new TrackGroup(Integer.toString(r1), r3);
        r1 = r1 + 1;
        goto L4
    L11:
        return new TrackGroupArray(r0);
    }

    static /* synthetic */ List lambda$selectTracks$0(ChunkSampleStream r1) {
        return ImmutableList.m213of(Integer.valueOf(r1.primaryTrackType));
    }

    private static ChunkSampleStream<SsChunkSource>[] newSampleStreamArray(int r1) {
        return new ChunkSampleStream[r1];
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r2) {
        return this.compositeSequenceableLoader.continueLoading(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r5, boolean r7) {
        ChunkSampleStream<SsChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].discardBuffer(r5, r7);
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r7, SeekParameters r9) {
        ChunkSampleStream<SsChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L9;
        ChunkSampleStream<SsChunkSource> r3 = r0[r2];
        if (r3.primaryTrackType == 2) goto L7;
        r2 = r2 + 1;
        goto L3
    L7:
        return r3.getAdjustedSeekPositionUs(r7, r9);
    L9:
        return r7;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r8) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r8.size()) goto L10;
        ExoTrackSelection r2 = r8.get(r1);
        int r3 = this.trackGroups.indexOf(r2.getTrackGroup());
        int r4 = 0;
    L7:
        if (r4 >= r2.length()) goto L9;
        r0.add(new StreamKey(r3, r2.getIndexInTrackGroup(r4)));
        r4 = r4 + 1;
        goto L7
    L9:
        r1 = r1 + 1;
        goto L4
    L10:
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.compositeSequenceableLoader.isLoading();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
    public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
        onContinueLoadingRequested((ChunkSampleStream) r1);
    }

    public void onContinueLoadingRequested(ChunkSampleStream<SsChunkSource> r2) {
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r1, long r2) {
        this.callback = r1;
        r1.onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        return C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r2) {
        this.compositeSequenceableLoader.reevaluateBuffer(r2);
    }

    public void release() {
        ChunkSampleStream<SsChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    L5:
        this.callback = null;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r5) {
        ChunkSampleStream<SsChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].seekToUs(r5);
        r2 = r2 + 1;
        goto L3
    L5:
        return r5;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r6, boolean[] r7, SampleStream[] r8, boolean[] r9, long r10) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r6.length) goto L20;
        if (r8[r1] == null) goto L15;
        ChunkSampleStream r2 = (ChunkSampleStream) r8[r1];
        if (r6[r1] != null) goto L10;
    L13:
        r2.release();
        r8[r1] = null;
        goto L15
    L10:
        if (r7[r1] == false) goto L13;
        ((SsChunkSource) r2.getChunkSource()).updateTrackSelection((ExoTrackSelection) Assertions.checkNotNull(r6[r1]));
        r0.add(r2);
    L15:
        if (r8[r1] != null) goto L19;
        if (r6[r1] == null) goto L19;
        ChunkSampleStream<SsChunkSource> r3 = buildSampleStream(r6[r1], r10);
        r0.add(r3);
        r8[r1] = r3;
        r9[r1] = true;
    L19:
        r1 = r1 + 1;
        goto L4
    L20:
        this.sampleStreams = newSampleStreamArray(r0.size());
        r0.toArray(this.sampleStreams);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.create(r0, Lists.transform(r0, new SsMediaPeriod$$ExternalSyntheticLambda0()));
        return r10;
    }

    public void updateManifest(SsManifest r6) {
        this.manifest = r6;
        ChunkSampleStream<SsChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        ((SsChunkSource) r0[r2].getChunkSource()).updateManifest(r6);
        r2 = r2 + 1;
        goto L3
    L5:
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
    }
}
