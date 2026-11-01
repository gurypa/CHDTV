package androidx.media3.exoplayer.offline;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.SparseIntArray;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Metadata;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.DefaultRendererCapabilitiesList;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.RendererCapabilitiesList;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.trackselection.BaseTrackSelection;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelectionUtil;
import androidx.media3.exoplayer.trackselection.TrackSelectorResult;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.upstream.DefaultAllocator;
import androidx.media3.extractor.ExtractorsFactory;
import com.google.common.collect.UnmodifiableIterator;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class DownloadHelper {
    public static final DefaultTrackSelector.Parameters DEFAULT_TRACK_SELECTOR_PARAMETERS_WITHOUT_CONTEXT = null;
    private Callback callback;
    private final Handler callbackHandler;
    private List<ExoTrackSelection>[][] immutableTrackSelectionsByPeriodAndRenderer;
    private boolean isPreparedWithMedia;
    private final MediaItem.LocalConfiguration localConfiguration;
    private MappingTrackSelector.MappedTrackInfo[] mappedTrackInfos;
    private MediaPreparer mediaPreparer;
    private final MediaSource mediaSource;
    private final RendererCapabilitiesList rendererCapabilities;
    private final SparseIntArray scratchSet;
    private TrackGroupArray[] trackGroupArrays;
    private List<ExoTrackSelection>[][] trackSelectionsByPeriodAndRenderer;
    private final DefaultTrackSelector trackSelector;
    private final Timeline.Window window;

    public interface Callback {
        void onPrepareError(DownloadHelper r1, IOException r2);

        void onPrepared(DownloadHelper r1);
    }

    private static final class DownloadTrackSelection extends BaseTrackSelection {

        private static final class Factory implements ExoTrackSelection.Factory {
            private Factory() {
            }

            /* synthetic */ Factory(C03141 r1) {
                this();
            }

            @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection.Factory
            public ExoTrackSelection[] createTrackSelections(ExoTrackSelection.Definition[] r6, BandwidthMeter r7, MediaSource.MediaPeriodId r8, Timeline r9) {
                ExoTrackSelection[] r0 = new ExoTrackSelection[r6.length];
                int r1 = 0;
            L4:
                if (r1 >= r6.length) goto L10;
                if (r6[r1] != null) goto L8;
                DownloadTrackSelection r2 = null;
            L9:
                r0[r1] = r2;
                r1 = r1 + 1;
                goto L4
            L8:
                r2 = new DownloadTrackSelection(r6[r1].group, r6[r1].tracks);
                goto L9
            L10:
                return r0;
            }
        }

        public DownloadTrackSelection(TrackGroup r1, int[] r2) {
            super(r1, r2);
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectedIndex() {
            return 0;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public Object getSelectionData() {
            return null;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public int getSelectionReason() {
            return 0;
        }

        @Override // androidx.media3.exoplayer.trackselection.ExoTrackSelection
        public void updateSelectedTrack(long r1, long r3, long r5, List<? extends MediaChunk> r7, MediaChunkIterator[] r8) {
        }
    }

    private static final class FakeBandwidthMeter implements BandwidthMeter {
        private FakeBandwidthMeter() {
        }

        /* synthetic */ FakeBandwidthMeter(C03141 r1) {
            this();
        }

        @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
        public void addEventListener(Handler r1, BandwidthMeter.EventListener r2) {
        }

        @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
        public long getBitrateEstimate() {
            return 0;
        }

        @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
        public /* synthetic */ long getTimeToFirstByteEstimateUs() {
            return BandwidthMeter.CC.$default$getTimeToFirstByteEstimateUs(this);
        }

        @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
        public TransferListener getTransferListener() {
            return null;
        }

        @Override // androidx.media3.exoplayer.upstream.BandwidthMeter
        public void removeEventListener(BandwidthMeter.EventListener r1) {
        }
    }

    public static class LiveContentUnsupportedException extends IOException {
        public LiveContentUnsupportedException() {
        }
    }

    private static final class MediaPreparer implements MediaSource.MediaSourceCaller, MediaPeriod.Callback, Handler.Callback {
        private static final int DOWNLOAD_HELPER_CALLBACK_MESSAGE_FAILED = 2;
        private static final int DOWNLOAD_HELPER_CALLBACK_MESSAGE_PREPARED = 1;
        private static final int MESSAGE_CHECK_FOR_FAILURE = 2;
        private static final int MESSAGE_CONTINUE_LOADING = 3;
        private static final int MESSAGE_PREPARE_SOURCE = 1;
        private static final int MESSAGE_RELEASE = 4;
        private final Allocator allocator;
        private final DownloadHelper downloadHelper;
        private final Handler downloadHelperHandler;
        public MediaPeriod[] mediaPeriods;
        private final MediaSource mediaSource;
        private final Handler mediaSourceHandler;
        private final HandlerThread mediaSourceThread;
        private final ArrayList<MediaPeriod> pendingMediaPeriods;
        private boolean released;
        public Timeline timeline;

        /* JADX INFO: renamed from: $r8$lambda$baGNhU-egd_sMwXWLMn53wsW6Yw, reason: not valid java name */
        public static /* synthetic */ boolean m474$r8$lambda$baGNhUegd_sMwXWLMn53wsW6Yw(MediaPreparer r0, Message r1) {
            return r0.handleDownloadHelperCallbackMessage(r1);
        }

        public MediaPreparer(MediaSource r5, DownloadHelper r6) {
            this.mediaSource = r5;
            this.downloadHelper = r6;
            this.allocator = new DefaultAllocator(true, 65536);
            this.pendingMediaPeriods = new ArrayList();
            this.downloadHelperHandler = Util.createHandlerForCurrentOrMainLooper(new DownloadHelper$MediaPreparer$$ExternalSyntheticLambda0(this));
            this.mediaSourceThread = new HandlerThread("ExoPlayer:DownloadHelper");
            this.mediaSourceThread.start();
            this.mediaSourceHandler = Util.createHandler(this.mediaSourceThread.getLooper(), this);
            this.mediaSourceHandler.sendEmptyMessage(1);
        }

        private boolean handleDownloadHelperCallbackMessage(Message r6) {
            if (this.released == false) goto L6;
            return false;
        L6:
            switch(r6.what) {
                case 1: goto L15;
                case 2: goto L8;
                default: goto L7;
            };
        L7:
            return false;
        L8:
            release();
            DownloadHelper.access$400(this.downloadHelper, (IOException) Util.castNonNull(r6.obj));
            return true;
        L15:
            DownloadHelper.access$300(this.downloadHelper);     // Catch: ExoPlaybackException -> L12
        L14:
            return true;
        L12:
            e = move-exception;
            this.downloadHelperHandler.obtainMessage(2, new IOException(e)).sendToTarget();
            goto L14
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message r8) {
            int r1 = 0;
            switch(r8.what) {
                case 1: goto L28;
                case 2: goto L30;
                case 3: goto L12;
                case 4: goto L6;
                default: goto L4;
            };
        L4:
            return false;
        L12:
            MediaPeriod r0 = (MediaPeriod) r8.obj;
            if (this.pendingMediaPeriods.contains(r0) == false) goto L15;
            r0.continueLoading(new LoadingInfo.Builder().setPlaybackPositionUs(0).build());
        L15:
            return true;
        L28:
            this.mediaSource.prepareSource(this, null, PlayerId.UNSET);
            this.mediaSourceHandler.sendEmptyMessage(2);
            return true;
        L30:
        L25:
            e = move-exception;
            this.downloadHelperHandler.obtainMessage(2, e).sendToTarget();
        L27:
            return true;
        L17:
            if (this.mediaPeriods != null) goto L19;
            this.mediaSource.maybeThrowSourceInfoRefreshError();     // Catch: IOException -> L25
        L23:
            this.mediaSourceHandler.sendEmptyMessageDelayed(2, 100);     // Catch: IOException -> L25
            goto L27
        L19:
            int r2 = 0;
        L21:
            if (r2 >= this.pendingMediaPeriods.size()) goto L23;
            this.pendingMediaPeriods.get(r2).maybeThrowPrepareError();     // Catch: IOException -> L25
            r2 = r2 + 1;     // Catch: IOException -> L25
            goto L21
        L6:
            if (this.mediaPeriods == null) goto L10;
            MediaPeriod[] r3 = this.mediaPeriods;
            int r4 = r3.length;
        L8:
            if (r1 >= r4) goto L10;
            MediaPeriod r5 = r3[r1];
            this.mediaSource.releasePeriod(r5);
            r1 = r1 + 1;
        L10:
            this.mediaSource.releaseSource(this);
            this.mediaSourceHandler.removeCallbacksAndMessages(null);
            this.mediaSourceThread.quit();
            return true;
        }

        public void onContinueLoadingRequested(MediaPeriod r3) {
            if (this.pendingMediaPeriods.contains(r3) == false) goto L6;
            this.mediaSourceHandler.obtainMessage(3, r3).sendToTarget();
            return;
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
        public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
            onContinueLoadingRequested((MediaPeriod) r1);
        }

        @Override // androidx.media3.exoplayer.source.MediaPeriod.Callback
        public void onPrepared(MediaPeriod r3) {
            this.pendingMediaPeriods.remove(r3);
            if (this.pendingMediaPeriods.isEmpty() == false) goto L6;
            this.mediaSourceHandler.removeMessages(2);
            this.downloadHelperHandler.sendEmptyMessage(1);
            return;
        }

        @Override // androidx.media3.exoplayer.source.MediaSource.MediaSourceCaller
        public void onSourceInfoRefreshed(MediaSource r8, Timeline r9) {
            if (this.timeline == null) goto L5;
            return;
        L5:
            int r1 = 0;
            if (r9.getWindow(0, new Timeline.Window()).isLive() == false) goto L9;
            this.downloadHelperHandler.obtainMessage(2, new LiveContentUnsupportedException()).sendToTarget();
            return;
        L9:
            this.timeline = r9;
            this.mediaPeriods = new MediaPeriod[r9.getPeriodCount()];
            int r0 = 0;
        L11:
            if (r0 >= this.mediaPeriods.length) goto L13;
            MediaPeriod r2 = this.mediaSource.createPeriod(new MediaSource.MediaPeriodId(r9.getUidOfPeriod(r0)), this.allocator, 0);
            this.mediaPeriods[r0] = r2;
            this.pendingMediaPeriods.add(r2);
            r0 = r0 + 1;
            goto L11
        L13:
            MediaPeriod[] r3 = this.mediaPeriods;
            int r4 = r3.length;
        L14:
            if (r1 >= r4) goto L16;
            r3[r1].prepare(this, 0);
            r1 = r1 + 1;
            goto L14
        }

        public void release() {
            if (this.released == false) goto L5;
            return;
        L5:
            this.released = true;
            this.mediaSourceHandler.sendEmptyMessage(4);
        }
    }

    private static final class UnreleaseableRendererCapabilitiesList implements RendererCapabilitiesList {
        private final RendererCapabilities[] rendererCapabilities;

        private UnreleaseableRendererCapabilitiesList(RendererCapabilities[] r1) {
            this.rendererCapabilities = r1;
        }

        /* synthetic */ UnreleaseableRendererCapabilitiesList(RendererCapabilities[] r1, C03141 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.RendererCapabilitiesList
        public RendererCapabilities[] getRendererCapabilities() {
            return this.rendererCapabilities;
        }

        @Override // androidx.media3.exoplayer.RendererCapabilitiesList
        public void release() {
        }

        @Override // androidx.media3.exoplayer.RendererCapabilitiesList
        public int size() {
            return this.rendererCapabilities.length;
        }
    }

    static {
        DEFAULT_TRACK_SELECTOR_PARAMETERS_WITHOUT_CONTEXT = DefaultTrackSelector.Parameters.DEFAULT_WITHOUT_CONTEXT.buildUpon().setForceHighestSupportedBitrate(true).setConstrainAudioChannelCountToDeviceCapabilities(false).build();
    }

    public DownloadHelper(MediaItem r5, MediaSource r6, TrackSelectionParameters r7, RendererCapabilitiesList r8) {
        this.localConfiguration = (MediaItem.LocalConfiguration) Assertions.checkNotNull(r5.localConfiguration);
        this.mediaSource = r6;
        C03141 r2 = null;
        this.trackSelector = new DefaultTrackSelector(r7, new DownloadTrackSelection.Factory(r2));
        this.rendererCapabilities = r8;
        this.scratchSet = new SparseIntArray();
        this.trackSelector.init(new DownloadHelper$$ExternalSyntheticLambda6(), new FakeBandwidthMeter(r2));
        this.callbackHandler = Util.createHandlerForCurrentOrMainLooper();
        this.window = new Timeline.Window();
    }

    @Deprecated
    public DownloadHelper(MediaItem r3, MediaSource r4, TrackSelectionParameters r5, RendererCapabilities[] r6) {
        this(r3, r4, r5, new UnreleaseableRendererCapabilitiesList(r6, null));
    }

    static /* synthetic */ void access$300(DownloadHelper r0) throws ExoPlaybackException {
        r0.onMediaPrepared();
    }

    static /* synthetic */ void access$400(DownloadHelper r0, IOException r1) {
        r0.onMediaPreparationFailed(r1);
    }

    @RequiresNonNull({"trackGroupArrays", "trackSelectionsByPeriodAndRenderer", "mediaPreparer", "mediaPreparer.timeline"})
    private void addTrackSelectionInternal(int r5, TrackSelectionParameters r6) throws ExoPlaybackException {
        this.trackSelector.setParameters(r6);
        runTrackSelection(r5);
        UnmodifiableIterator<TrackSelectionOverride> r0 = r6.overrides.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        TrackSelectionOverride r1 = r0.next();
        this.trackSelector.setParameters(r6.buildUpon().setOverrideForType(r1).build());
        runTrackSelection(r5);
        goto L4
    }

    @EnsuresNonNull({"trackGroupArrays", "mappedTrackInfos", "trackSelectionsByPeriodAndRenderer", "immutableTrackSelectionsByPeriodAndRenderer", "mediaPreparer", "mediaPreparer.timeline", "mediaPreparer.mediaPeriods"})
    private void assertPreparedWithMedia() {
        Assertions.checkState(this.isPreparedWithMedia);
    }

    public static MediaSource createMediaSource(DownloadRequest r1, DataSource.Factory r2) {
        return createMediaSource(r1, r2, null);
    }

    public static MediaSource createMediaSource(DownloadRequest r1, DataSource.Factory r2, DrmSessionManager r3) {
        return createMediaSourceInternal(r1.toMediaItem(), r2, r3);
    }

    private static MediaSource createMediaSourceInternal(MediaItem r2, DataSource.Factory r3, final DrmSessionManager r4) {
        DefaultMediaSourceFactory r0 = new DefaultMediaSourceFactory(r3, ExtractorsFactory.EMPTY);
        if (r4 == null) goto L6;
        r0.setDrmSessionManagerProvider(new DownloadHelper$$ExternalSyntheticLambda0(r4));
    L6:
        return r0.createMediaSource(r2);
    }

    public static DownloadHelper forMediaItem(Context r2, MediaItem r3) {
        Assertions.checkArgument(isProgressive((MediaItem.LocalConfiguration) Assertions.checkNotNull(r3.localConfiguration)));
        return forMediaItem(r3, getDefaultTrackSelectorParameters(r2), null, null, null);
    }

    public static DownloadHelper forMediaItem(Context r2, MediaItem r3, RenderersFactory r4, DataSource.Factory r5) {
        return forMediaItem(r3, getDefaultTrackSelectorParameters(r2), r4, r5, null);
    }

    public static DownloadHelper forMediaItem(MediaItem r1, TrackSelectionParameters r2, RenderersFactory r3, DataSource.Factory r4) {
        return forMediaItem(r1, r2, r3, r4, null);
    }

    public static DownloadHelper forMediaItem(MediaItem r6, TrackSelectionParameters r7, RenderersFactory r8, DataSource.Factory r9, DrmSessionManager r10) {
        boolean r0 = isProgressive((MediaItem.LocalConfiguration) Assertions.checkNotNull(r6.localConfiguration));
        if (r0 == true) goto L7;
        if (r9 != null) goto L7;
        boolean r2 = false;
    L8:
        Assertions.checkArgument(r2);
        C03141 r3 = null;
        if (r0 == false) goto L11;
        MediaSource r4 = null;
    L12:
        if (r8 == null) goto L14;
        RendererCapabilitiesList r1 = new DefaultRendererCapabilitiesList.Factory(r8).createRendererCapabilitiesList();
    L16:
        return new DownloadHelper(r6, r4, r7, r1);
    L14:
        r1 = new UnreleaseableRendererCapabilitiesList(new RendererCapabilities[0], r3);
        goto L16
    L11:
        r4 = createMediaSourceInternal(r6, (DataSource.Factory) Util.castNonNull(r9), r10);
    L7:
        r2 = true;
        goto L8
    }

    public static DefaultTrackSelector.Parameters getDefaultTrackSelectorParameters(Context r2) {
        return DefaultTrackSelector.Parameters.getDefaults(r2).buildUpon().setForceHighestSupportedBitrate(true).setConstrainAudioChannelCountToDeviceCapabilities(false).build();
    }

    @Deprecated
    public static RendererCapabilities[] getRendererCapabilities(RenderersFactory r6) {
        Renderer[] r7 = r6.createRenderers(Util.createHandlerForCurrentOrMainLooper(), new C03141(), new C03152(), new DownloadHelper$$ExternalSyntheticLambda1(), new DownloadHelper$$ExternalSyntheticLambda2());
        RendererCapabilities[] r1 = new RendererCapabilities[r7.length];
        int r2 = 0;
    L4:
        if (r2 >= r7.length) goto L6;
        r1[r2] = r7[r2].getCapabilities();
        r2 = r2 + 1;
        goto L4
    L6:
        return r1;
    }

    private static boolean isProgressive(MediaItem.LocalConfiguration r2) {
        if (Util.inferContentTypeForUriAndMimeType(r2.uri, r2.mimeType) != 4) goto L5;
        return true;
    L5:
        return false;
    }

    static /* synthetic */ DrmSessionManager lambda$createMediaSourceInternal$6(DrmSessionManager r0, MediaItem r1) {
        return r0;
    }

    static /* synthetic */ void lambda$getRendererCapabilities$0(CueGroup r0) {
    }

    static /* synthetic */ void lambda$getRendererCapabilities$1(Metadata r0) {
    }

    static /* synthetic */ void lambda$new$2() {
    }

    private void onMediaPreparationFailed(final IOException r3) {
        ((Handler) Assertions.checkNotNull(this.callbackHandler)).post(new DownloadHelper$$ExternalSyntheticLambda4(this, r3));
    }

    private void onMediaPrepared() throws ExoPlaybackException {
        Assertions.checkNotNull(this.mediaPreparer);
        Assertions.checkNotNull(this.mediaPreparer.mediaPeriods);
        Assertions.checkNotNull(this.mediaPreparer.timeline);
        int r0 = this.mediaPreparer.mediaPeriods.length;
        int r1 = this.rendererCapabilities.size();
        this.trackSelectionsByPeriodAndRenderer = (List[][]) Array.newInstance(List.class, new int[]{r0, r1});
        this.immutableTrackSelectionsByPeriodAndRenderer = (List[][]) Array.newInstance(List.class, new int[]{r0, r1});
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        int r3 = 0;
    L5:
        if (r3 >= r1) goto L7;
        this.trackSelectionsByPeriodAndRenderer[r2][r3] = new ArrayList();
        this.immutableTrackSelectionsByPeriodAndRenderer[r2][r3] = Collections.unmodifiableList(this.trackSelectionsByPeriodAndRenderer[r2][r3]);
        r3 = r3 + 1;
        goto L5
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        this.trackGroupArrays = new TrackGroupArray[r0];
        this.mappedTrackInfos = new MappingTrackSelector.MappedTrackInfo[r0];
        int r4 = 0;
    L9:
        if (r4 >= r0) goto L11;
        this.trackGroupArrays[r4] = this.mediaPreparer.mediaPeriods[r4].getTrackGroups();
        this.trackSelector.onSelectionActivated(runTrackSelection(r4).info);
        this.mappedTrackInfos[r4] = (MappingTrackSelector.MappedTrackInfo) Assertions.checkNotNull(this.trackSelector.getCurrentMappedTrackInfo());
        r4 = r4 + 1;
        goto L9
    L11:
        setPreparedWithMedia();
        ((Handler) Assertions.checkNotNull(this.callbackHandler)).post(new DownloadHelper$$ExternalSyntheticLambda3(this));
    }

    @RequiresNonNull({"trackGroupArrays", "trackSelectionsByPeriodAndRenderer", "mediaPreparer", "mediaPreparer.timeline"})
    private TrackSelectorResult runTrackSelection(int r12) throws ExoPlaybackException {
        TrackSelectorResult r0 = this.trackSelector.selectTracks(this.rendererCapabilities.getRendererCapabilities(), this.trackGroupArrays[r12], new MediaSource.MediaPeriodId(this.mediaPreparer.timeline.getUidOfPeriod(r12)), this.mediaPreparer.timeline);
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L30;
        ExoTrackSelection r2 = r0.selections[r1];
        if (r2 == null) goto L29;
        List<ExoTrackSelection> r3 = this.trackSelectionsByPeriodAndRenderer[r12][r1];
        boolean r4 = false;
        int r5 = 0;
    L10:
        if (r5 >= r3.size()) goto L27;
        ExoTrackSelection r6 = r3.get(r5);
        if (r6.getTrackGroup().equals(r2.getTrackGroup()) == true) goto L13;
        r5 = r5 + 1;
        goto L10
    L13:
        this.scratchSet.clear();
        int r7 = 0;
    L15:
        if (r7 >= r6.length()) goto L17;
        this.scratchSet.put(r6.getIndexInTrackGroup(r7), 0);
        r7 = r7 + 1;
        goto L15
    L17:
        int r8 = 0;
    L19:
        if (r8 >= r2.length()) goto L21;
        this.scratchSet.put(r2.getIndexInTrackGroup(r8), 0);
        r8 = r8 + 1;
        goto L19
    L21:
        int[] r9 = new int[this.scratchSet.size()];
        int r10 = 0;
    L23:
        if (r10 >= this.scratchSet.size()) goto L25;
        r9[r10] = this.scratchSet.keyAt(r10);
        r10 = r10 + 1;
        goto L23
    L25:
        r3.set(r5, new DownloadTrackSelection(r6.getTrackGroup(), r9));
        r4 = true;
    L27:
        if (r4 == true) goto L29;
        r3.add(r2);
    L29:
        r1 = r1 + 1;
        goto L4
    L30:
        return r0;
    }

    @RequiresNonNull({"trackGroupArrays", "mappedTrackInfos", "trackSelectionsByPeriodAndRenderer", "immutableTrackSelectionsByPeriodAndRenderer", "mediaPreparer", "mediaPreparer.timeline", "mediaPreparer.mediaPeriods"})
    private void setPreparedWithMedia() {
        this.isPreparedWithMedia = true;
    }

    public void addAudioLanguagesToSelection(String... r10) {
        assertPreparedWithMedia();     // Catch: ExoPlaybackException -> L18
        DefaultTrackSelector.Parameters.Builder r0 = DEFAULT_TRACK_SELECTOR_PARAMETERS_WITHOUT_CONTEXT.buildUpon();     // Catch: ExoPlaybackException -> L18
        r0.setForceHighestSupportedBitrate(true);     // Catch: ExoPlaybackException -> L18
        RendererCapabilities[] r2 = this.rendererCapabilities.getRendererCapabilities();     // Catch: ExoPlaybackException -> L18
        int r3 = r2.length;     // Catch: ExoPlaybackException -> L18
        int r4 = 0;
        int r5 = 0;
    L3:
        if (r5 >= r3) goto L9;
        int r7 = r2[r5].getTrackType();     // Catch: ExoPlaybackException -> L18
        if (r7 == 1) goto L7;
        boolean r8 = true;
    L8:
        r0.setTrackTypeDisabled(r7, r8);     // Catch: ExoPlaybackException -> L18
        r5 = r5 + 1;     // Catch: ExoPlaybackException -> L18
        goto L3
    L7:
        r8 = false;
        goto L8
    L9:
        int r1 = getPeriodCount();     // Catch: ExoPlaybackException -> L18
        int r6 = r10.length;     // Catch: ExoPlaybackException -> L18
    L10:
        if (r4 >= r6) goto L17;
        TrackSelectionParameters r9 = r0.setPreferredAudioLanguage(r10[r4]).build();     // Catch: ExoPlaybackException -> L18
        int r11 = 0;
    L12:
        if (r11 >= r1) goto L15;
        addTrackSelectionInternal(r11, r9);     // Catch: ExoPlaybackException -> L18
        r11 = r11 + 1;
        goto L12
    L15:
        r4 = r4 + 1;
        goto L10
    L17:
        return;
    L18:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public void addTextLanguagesToSelection(boolean r10, String... r11) {
        assertPreparedWithMedia();     // Catch: ExoPlaybackException -> L18
        DefaultTrackSelector.Parameters.Builder r0 = DEFAULT_TRACK_SELECTOR_PARAMETERS_WITHOUT_CONTEXT.buildUpon();     // Catch: ExoPlaybackException -> L18
        r0.setSelectUndeterminedTextLanguage(r10);     // Catch: ExoPlaybackException -> L18
        r0.setForceHighestSupportedBitrate(true);     // Catch: ExoPlaybackException -> L18
        RendererCapabilities[] r2 = this.rendererCapabilities.getRendererCapabilities();     // Catch: ExoPlaybackException -> L18
        int r3 = r2.length;     // Catch: ExoPlaybackException -> L18
        int r4 = 0;
        int r5 = 0;
    L3:
        if (r5 >= r3) goto L9;
        int r7 = r2[r5].getTrackType();     // Catch: ExoPlaybackException -> L18
        if (r7 == 3) goto L7;
        boolean r8 = true;
    L8:
        r0.setTrackTypeDisabled(r7, r8);     // Catch: ExoPlaybackException -> L18
        r5 = r5 + 1;     // Catch: ExoPlaybackException -> L18
        goto L3
    L7:
        r8 = false;
        goto L8
    L9:
        int r1 = getPeriodCount();     // Catch: ExoPlaybackException -> L18
        int r6 = r11.length;     // Catch: ExoPlaybackException -> L18
    L10:
        if (r4 >= r6) goto L17;
        TrackSelectionParameters r9 = r0.setPreferredTextLanguage(r11[r4]).build();     // Catch: ExoPlaybackException -> L18
        int r12 = 0;
    L12:
        if (r12 >= r1) goto L15;
        addTrackSelectionInternal(r12, r9);     // Catch: ExoPlaybackException -> L18
        r12 = r12 + 1;
        goto L12
    L15:
        r4 = r4 + 1;
        goto L10
    L17:
        return;
    L18:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public void addTrackSelection(int r3, TrackSelectionParameters r4) {
        assertPreparedWithMedia();     // Catch: ExoPlaybackException -> L4
        addTrackSelectionInternal(r3, r4);     // Catch: ExoPlaybackException -> L4
        return;
    L4:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public void addTrackSelectionForSingleRenderer(int r5, int r6, DefaultTrackSelector.Parameters r7, List<DefaultTrackSelector.SelectionOverride> r8) {
        assertPreparedWithMedia();     // Catch: ExoPlaybackException -> L19
        DefaultTrackSelector.Parameters.Builder r0 = r7.buildUpon();     // Catch: ExoPlaybackException -> L19
        int r1 = 0;
    L4:
        if (r1 >= this.mappedTrackInfos[r5].getRendererCount()) goto L10;
        if (r1 == r6) goto L7;
        boolean r2 = true;
    L8:
        r0.setRendererDisabled(r1, r2);     // Catch: ExoPlaybackException -> L19
        r1 = r1 + 1;     // Catch: ExoPlaybackException -> L19
        goto L4
    L7:
        r2 = false;
        goto L8
    L10:
        if (r8.isEmpty() == false) goto L12;
        addTrackSelectionInternal(r5, r0.build());     // Catch: ExoPlaybackException -> L19
        return;
    L12:
        TrackGroupArray r3 = this.mappedTrackInfos[r5].getTrackGroups(r6);     // Catch: ExoPlaybackException -> L19
        int r4 = 0;
    L14:
        if (r4 >= r8.size()) goto L27;
        r0.setSelectionOverride(r6, r3, r8.get(r4));     // Catch: ExoPlaybackException -> L19
        addTrackSelectionInternal(r5, r0.build());     // Catch: ExoPlaybackException -> L19
        r4 = r4 + 1;
        goto L14
    L27:
        return;
    L19:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public void clearTrackSelections(int r3) {
        assertPreparedWithMedia();
        int r0 = 0;
    L4:
        if (r0 >= this.rendererCapabilities.size()) goto L6;
        this.trackSelectionsByPeriodAndRenderer[r3][r0].clear();
        r0 = r0 + 1;
        goto L4
    }

    public DownloadRequest getDownloadRequest(String r9, byte[] r10) {
        DownloadRequest.Builder r0 = new DownloadRequest.Builder(r9, this.localConfiguration.uri).setMimeType(this.localConfiguration.mimeType);
        if (this.localConfiguration.drmConfiguration == null) goto L5;
        byte[] r1 = this.localConfiguration.drmConfiguration.getKeySetId();
    L6:
        DownloadRequest.Builder r2 = r0.setKeySetId(r1).setCustomCacheKey(this.localConfiguration.customCacheKey).setData(r10);
        if (this.mediaSource == null) goto L9;
        assertPreparedWithMedia();
        ArrayList r3 = new ArrayList();
        ArrayList r4 = new ArrayList();
        int r5 = this.trackSelectionsByPeriodAndRenderer.length;
        int r6 = 0;
    L11:
        if (r6 >= r5) goto L17;
        r4.clear();
        int r7 = this.trackSelectionsByPeriodAndRenderer[r6].length;
        int r8 = 0;
    L13:
        if (r8 >= r7) goto L15;
        r4.addAll(this.trackSelectionsByPeriodAndRenderer[r6][r8]);
        r8 = r8 + 1;
        goto L13
    L15:
        r3.addAll(this.mediaPreparer.mediaPeriods[r6].getStreamKeys(r4));
        r6 = r6 + 1;
        goto L11
    L17:
        return r2.setStreamKeys(r3).build();
    L9:
        return r2.build();
    L5:
        r1 = null;
        goto L6
    }

    public DownloadRequest getDownloadRequest(byte[] r2) {
        return getDownloadRequest(this.localConfiguration.uri.toString(), r2);
    }

    public Object getManifest() {
        if (this.mediaSource != null) goto L5;
        return null;
    L5:
        assertPreparedWithMedia();
        if (this.mediaPreparer.timeline.getWindowCount() > 0) goto L9;
        return null;
    L9:
        return this.mediaPreparer.timeline.getWindow(0, this.window).manifest;
    }

    public MappingTrackSelector.MappedTrackInfo getMappedTrackInfo(int r2) {
        assertPreparedWithMedia();
        return this.mappedTrackInfos[r2];
    }

    public int getPeriodCount() {
        if (this.mediaSource != null) goto L6;
        return 0;
    L6:
        assertPreparedWithMedia();
        return this.trackGroupArrays.length;
    }

    public TrackGroupArray getTrackGroups(int r2) {
        assertPreparedWithMedia();
        return this.trackGroupArrays[r2];
    }

    public List<ExoTrackSelection> getTrackSelections(int r2, int r3) {
        assertPreparedWithMedia();
        return this.immutableTrackSelectionsByPeriodAndRenderer[r2][r3];
    }

    public Tracks getTracks(int r3) {
        assertPreparedWithMedia();
        return TrackSelectionUtil.buildTracks(this.mappedTrackInfos[r3], this.immutableTrackSelectionsByPeriodAndRenderer[r3]);
    }

    /* JADX INFO: renamed from: lambda$onMediaPreparationFailed$5$androidx-media3-exoplayer-offline-DownloadHelper */
    /* synthetic */ void m113x674611d1(IOException r2) {
        ((Callback) Assertions.checkNotNull(this.callback)).onPrepareError(this, r2);
    }

    /* JADX INFO: renamed from: lambda$onMediaPrepared$4$androidx-media3-exoplayer-offline-DownloadHelper */
    /* synthetic */ void m114xa451279() {
        ((Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
    }

    /* JADX INFO: renamed from: lambda$prepare$3$androidx-media3-exoplayer-offline-DownloadHelper */
    /* synthetic */ void m115xc63c3cd5(Callback r1) {
        r1.onPrepared(this);
    }

    public void prepare(final Callback r3) {
        if (this.callback != null) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        this.callback = r3;
        if (this.mediaSource == null) goto L9;
        this.mediaPreparer = new MediaPreparer(this.mediaSource, this);
        return;
    L9:
        this.callbackHandler.post(new DownloadHelper$$ExternalSyntheticLambda5(this, r3));
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void release() {
        if (this.mediaPreparer == null) goto L5;
        this.mediaPreparer.release();
    L5:
        this.trackSelector.release();
        this.rendererCapabilities.release();
    }

    public void replaceTrackSelections(int r3, TrackSelectionParameters r4) {
        assertPreparedWithMedia();     // Catch: ExoPlaybackException -> L4
        clearTrackSelections(r3);     // Catch: ExoPlaybackException -> L4
        addTrackSelectionInternal(r3, r4);     // Catch: ExoPlaybackException -> L4
        return;
    L4:
        e = move-exception;
        throw new IllegalStateException(e);
    }
}
