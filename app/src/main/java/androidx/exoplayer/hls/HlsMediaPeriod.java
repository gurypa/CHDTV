package androidx.media3.exoplayer.hls;

import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.Label;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker;
import androidx.media3.exoplayer.source.CompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.MediaPeriod;
import androidx.media3.exoplayer.source.MediaSourceEventListener;
import androidx.media3.exoplayer.source.SampleStream;
import androidx.media3.exoplayer.source.SequenceableLoader;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import androidx.media3.exoplayer.upstream.Allocator;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class HlsMediaPeriod implements MediaPeriod, HlsPlaylistTracker.PlaylistEventListener {
    private final Allocator allocator;
    private final boolean allowChunklessPreparation;
    private int audioVideoSampleStreamWrapperCount;
    private final CmcdConfiguration cmcdConfiguration;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private HlsSampleStreamWrapper[] enabledSampleStreamWrappers;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private int[][] manifestUrlIndicesPerWrapper;
    private MediaPeriod.Callback mediaPeriodCallback;
    private final TransferListener mediaTransferListener;
    private final int metadataType;
    private int pendingPrepareCount;
    private final PlayerId playerId;
    private final HlsPlaylistTracker playlistTracker;
    private final HlsSampleStreamWrapper.Callback sampleStreamWrapperCallback;
    private HlsSampleStreamWrapper[] sampleStreamWrappers;
    private final IdentityHashMap<SampleStream, Integer> streamWrapperIndices;
    private final long timestampAdjusterInitializationTimeoutMs;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private TrackGroupArray trackGroups;
    private final boolean useSessionKeys;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.hls.HlsMediaPeriod$1 */
    static /* synthetic */ class C02911 {
    }

    private class SampleStreamWrapperCallback implements HlsSampleStreamWrapper.Callback {
        final /* synthetic */ HlsMediaPeriod this$0;

        private SampleStreamWrapperCallback(HlsMediaPeriod r1) {
            this.this$0 = r1;
        }

        /* synthetic */ SampleStreamWrapperCallback(HlsMediaPeriod r1, C02911 r2) {
            this(r1);
        }

        public void onContinueLoadingRequested(HlsSampleStreamWrapper r3) {
            HlsMediaPeriod.access$400(this.this$0).onContinueLoadingRequested(this.this$0);
        }

        @Override // androidx.media3.exoplayer.source.SequenceableLoader.Callback
        public /* bridge */ /* synthetic */ void onContinueLoadingRequested(SequenceableLoader r1) {
            onContinueLoadingRequested((HlsSampleStreamWrapper) r1);
        }

        @Override // androidx.media3.exoplayer.hls.HlsSampleStreamWrapper.Callback
        public void onPlaylistRefreshRequired(Uri r2) {
            HlsMediaPeriod.access$500(this.this$0).refreshPlaylist(r2);
        }

        @Override // androidx.media3.exoplayer.hls.HlsSampleStreamWrapper.Callback
        public void onPrepared() {
            if (HlsMediaPeriod.access$106(this.this$0) <= 0) goto L5;
            return;
        L5:
            int r0 = 0;
            HlsSampleStreamWrapper[] r1 = HlsMediaPeriod.access$200(this.this$0);
            int r2 = r1.length;
            int r3 = 0;
            int r4 = 0;
        L6:
            if (r4 >= r2) goto L8;
            r0 = r0 + r1[r4].getTrackGroups().length;
            r4 = r4 + 1;
            goto L6
        L8:
            TrackGroup[] r5 = new TrackGroup[r0];
            int r6 = 0;
            HlsSampleStreamWrapper[] r7 = HlsMediaPeriod.access$200(this.this$0);
            int r8 = r7.length;
        L9:
            if (r3 >= r8) goto L14;
            HlsSampleStreamWrapper r9 = r7[r3];
            int r10 = r9.getTrackGroups().length;
            int r11 = 0;
        L11:
            if (r11 >= r10) goto L13;
            r5[r6] = r9.getTrackGroups().get(r11);
            r11 = r11 + 1;
            r6 = r6 + 1;
            goto L11
        L13:
            r3 = r3 + 1;
            goto L9
        L14:
            HlsMediaPeriod.access$302(this.this$0, new TrackGroupArray(r5));
            HlsMediaPeriod.access$400(this.this$0).onPrepared(this.this$0);
        }
    }

    public HlsMediaPeriod(HlsExtractorFactory r17, HlsPlaylistTracker r18, HlsDataSourceFactory r19, TransferListener r20, CmcdConfiguration r21, DrmSessionManager r22, DrmSessionEventListener.EventDispatcher r23, LoadErrorHandlingPolicy r24, MediaSourceEventListener.EventDispatcher r25, Allocator r26, CompositeSequenceableLoaderFactory r27, boolean r28, int r29, boolean r30, PlayerId r31, long r32) {
        this.extractorFactory = r17;
        this.playlistTracker = r18;
        this.dataSourceFactory = r19;
        this.mediaTransferListener = r20;
        this.cmcdConfiguration = r21;
        this.drmSessionManager = r22;
        this.drmEventDispatcher = r23;
        this.loadErrorHandlingPolicy = r24;
        this.eventDispatcher = r25;
        this.allocator = r26;
        this.compositeSequenceableLoaderFactory = r27;
        this.allowChunklessPreparation = r28;
        this.metadataType = r29;
        this.useSessionKeys = r30;
        this.playerId = r31;
        this.timestampAdjusterInitializationTimeoutMs = r32;
        this.sampleStreamWrapperCallback = new SampleStreamWrapperCallback(this, null);
        this.compositeSequenceableLoader = r27.empty();
        this.streamWrapperIndices = new IdentityHashMap();
        this.timestampAdjusterProvider = new TimestampAdjusterProvider();
        this.sampleStreamWrappers = new HlsSampleStreamWrapper[0];
        this.enabledSampleStreamWrappers = new HlsSampleStreamWrapper[0];
        this.manifestUrlIndicesPerWrapper = new int[0][];
    }

    static /* synthetic */ int access$106(HlsMediaPeriod r1) {
        int r0 = r1.pendingPrepareCount - 1;
        r1.pendingPrepareCount = r0;
        return r0;
    }

    static /* synthetic */ HlsSampleStreamWrapper[] access$200(HlsMediaPeriod r1) {
        return r1.sampleStreamWrappers;
    }

    static /* synthetic */ TrackGroupArray access$302(HlsMediaPeriod r0, TrackGroupArray r1) {
        r0.trackGroups = r1;
        return r1;
    }

    static /* synthetic */ MediaPeriod.Callback access$400(HlsMediaPeriod r1) {
        return r1.mediaPeriodCallback;
    }

    static /* synthetic */ HlsPlaylistTracker access$500(HlsMediaPeriod r1) {
        return r1.playlistTracker;
    }

    private void buildAndPrepareAudioSampleStreamWrappers(long r23, List<HlsMultivariantPlaylist.Rendition> r25, List<HlsSampleStreamWrapper> r26, List<int[]> r27, Map<String, DrmInitData> r28) {
        List<HlsMultivariantPlaylist.Rendition> r0 = r25;
        ArrayList r1 = new ArrayList(r0.size());
        ArrayList r2 = new ArrayList(r0.size());
        ArrayList r3 = new ArrayList(r0.size());
        HashSet r4 = new HashSet();
        int r5 = 0;
    L4:
        if (r5 >= r0.size()) goto L25;
        String r6 = r0.get(r5).name;
        if (r4.add(r6) == false) goto L23;
        boolean r7 = true;
        r1.clear();
        r2.clear();
        r3.clear();
        int r8 = 0;
    L9:
        boolean r10 = true;
        if (r8 >= r0.size()) goto L19;
        if (Util.areEqual(r6, r0.get(r8).name) == false) goto L18;
        HlsMultivariantPlaylist.Rendition r9 = r0.get(r8);
        r3.add(Integer.valueOf(r8));
        r1.add(r9.url);
        r2.add(r9.format);
        if (Util.getCodecCountOfType(r9.format.codecs, 1) == 1) goto L17;
        r10 = false;
    L17:
        r7 = r7 & r10;
    L18:
        r8 = r8 + 1;
        goto L9
    L19:
        String r13 = "audio:" + r6;
        HlsSampleStreamWrapper r11 = buildSampleStreamWrapper(r13, 1, (Uri[]) r1.toArray((Uri[]) Util.castNonNullTypeArray(new Uri[0])), (Format[]) r2.toArray(new Format[0]), null, Collections.emptyList(), r28, r23);
        r27.add(Ints.toArray(r3));
        r26.add(r11);
        if (this.allowChunklessPreparation == false) goto L23;
        if (r7 == false) goto L23;
        r11.prepareWithMultivariantPlaylistInfo(new TrackGroup[]{new TrackGroup(r13, (Format[]) r2.toArray(new Format[0]))}, 0, new int[0]);
    L23:
        r5 = r5 + 1;
        r0 = r25;
        goto L4
    }

    private void buildAndPrepareMainSampleStreamWrapper(HlsMultivariantPlaylist r30, long r31, List<HlsSampleStreamWrapper> r33, List<int[]> r34, Map<String, DrmInitData> r35) {
        int[] r11 = new int[r30.variants.size()];
        int r2 = 0;
        int r12 = 0;
        int r13 = 0;
    L4:
        if (r2 >= r30.variants.size()) goto L16;
        Format r3 = r30.variants.get(r2).format;
        if (r3.height <= 0) goto L8;
    L14:
        r11[r2] = 2;
        r12 = r12 + 1;
    L15:
        r2 = r2 + 1;
        goto L4
    L8:
        if (Util.getCodecsOfType(r3.codecs, 2) != null) goto L14;
        if (Util.getCodecsOfType(r3.codecs, 1) == null) goto L13;
        r11[r2] = 1;
        r13 = r13 + 1;
        goto L15
    L13:
        r11[r2] = -1;
        goto L15
    L16:
        int r4 = r11.length;
        if (r12 <= 0) goto L20;
        boolean r15 = true;
        boolean r16 = false;
        int r0 = r12;
    L23:
        Uri[] r5 = new Uri[r0];
        Format[] r6 = new Format[r0];
        int[] r7 = new int[r0];
        int r8 = 0;
        int r17 = 0;
    L25:
        if (r8 >= r30.variants.size()) goto L34;
        if (r15 == true) goto L28;
    L29:
        if (r16 == true) goto L31;
    L32:
        HlsMultivariantPlaylist.Variant r9 = r30.variants.get(r8);
        r5[r17] = r9.url;
        r6[r17] = r9.format;
        r7[r17] = r8;
        r17 = r17 + 1;
    L33:
        r8 = r8 + 1;
        goto L25
    L31:
        if (r11[r8] == 1) goto L33;
    L28:
        if (r11[r8] != 2) goto L33;
    L34:
        String r10 = r6[0].codecs;
        int r1 = Util.getCodecCountOfType(r10, 2);
        int r14 = Util.getCodecCountOfType(r10, 1);
        if (r14 == 1) goto L39;
        if (r14 == 0) goto L38;
    L43:
        boolean r18 = false;
    L44:
        boolean r19 = r18;
        if (r15 == true) goto L48;
        if (r14 <= 0) goto L48;
        int r20 = 1;
    L49:
        int r110 = r0;
        HlsMediaPeriod r21 = this;
        int r22 = r20;
        HlsSampleStreamWrapper r23 = r21.buildSampleStreamWrapper("main", r22, r5, r6, r30.muxedAudioFormat, r30.muxedCaptionFormats, r35, r31);
        r33.add(r23);
        r34.add(r7);
        if (r21.allowChunklessPreparation == false) goto L82;
        if (r19 == false) goto L82;
        ArrayList r24 = new ArrayList();
        if (r1 <= 0) goto L75;
        Format[] r25 = new Format[r110];
        int r111 = 0;
    L55:
        int r26 = r22;
        if (r111 >= r25.length) goto L58;
        r25[r111] = deriveVideoFormat(r6[r111]);
        r111 = r111 + 1;
        r22 = r26;
        goto L55
    L58:
        r24.add(new TrackGroup("main", r25));
        if (r14 <= 0) goto L68;
        if (r30.muxedAudioFormat == null) goto L63;
    L66:
        r24.add(new TrackGroup("main:audio", new Format[]{deriveAudioFormat(r6[0], r30.muxedAudioFormat, false)}));
        goto L68
    L63:
        if (r30.audios.isEmpty() == true) goto L66;
    L68:
        List<Format> r27 = r30.muxedCaptionFormats;
        if (r27 == null) goto L80;
        int r28 = 0;
    L72:
        if (r28 >= r27.size()) goto L80;
        r24.add(new TrackGroup("main:cc:" + r28, new Format[]{r21.extractorFactory.getOutputTextFormat(r27.get(r28))}));
        r28 = r28 + 1;
        r21 = this;
    L80:
        TrackGroup r29 = new TrackGroup("main:id3", new Format[]{new Format.Builder().setId("ID3").setSampleMimeType(MimeTypes.APPLICATION_ID3).build()});
        r24.add(r29);
        r23.prepareWithMultivariantPlaylistInfo((TrackGroup[]) r24.toArray(new TrackGroup[0]), 0, new int[]{r24.indexOf(r29)});
        return;
    L75:
        Format[] r32 = new Format[r110];
        int r36 = 0;
    L77:
        if (r36 >= r32.length) goto L79;
        r32[r36] = deriveAudioFormat(r6[r36], r30.muxedAudioFormat, true);
        r36 = r36 + 1;
        goto L77
    L79:
        r24.add(new TrackGroup("main", r32));
    L82:
        return;
    L48:
        r20 = 0;
        goto L49
    L38:
        if (r30.audios.isEmpty() == false) goto L43;
    L39:
        if (r1 > 1) goto L43;
        if ((r14 + r1) <= 0) goto L43;
        r18 = true;
        goto L44
    L20:
        if (r13 >= r11.length) goto L22;
        r15 = false;
        r16 = true;
        r0 = r11.length - r13;
        goto L23
    L22:
        r15 = false;
        r16 = false;
        r0 = r4;
        goto L23
    }

    private void buildAndPrepareSampleStreamWrappers(long r22) {
        HlsMultivariantPlaylist r1 = (HlsMultivariantPlaylist) Assertions.checkNotNull(this.playlistTracker.getMultivariantPlaylist());
        if (this.useSessionKeys == false) goto L5;
        Map<String, DrmInitData> r6 = deriveOverridingDrmInitData(r1.sessionKeyDrmInitData);
    L6:
        int r10 = 1;
        boolean r11 = !r1.variants.isEmpty();
        List<HlsMultivariantPlaylist.Rendition> r7 = r1.audios;
        List<HlsMultivariantPlaylist.Rendition> r12 = r1.subtitles;
        int r13 = 0;
        this.pendingPrepareCount = 0;
        ArrayList r4 = new ArrayList();
        ArrayList r5 = new ArrayList();
        if (r11 == false) goto L10;
        buildAndPrepareMainSampleStreamWrapper(r1, r22, r4, r5, r6);
    L10:
        buildAndPrepareAudioSampleStreamWrappers(r22, r7, r4, r5, r6);
        this.audioVideoSampleStreamWrapperCount = r4.size();
        int r2 = 0;
    L12:
        if (r2 >= r12.size()) goto L14;
        HlsMultivariantPlaylist.Rendition r3 = r12.get(r2);
        String r8 = "subtitle:" + r2 + ":" + r3.name;
        Format r9 = r3.format;
        int r14 = r2;
        Uri[] r15 = new Uri[r10];
        r15[r13] = r3.url;
        ArrayList r16 = r4;
        Format[] r17 = new Format[r10];
        r17[r13] = r9;
        Map<String, DrmInitData> r18 = r6;
        ArrayList r19 = r5;
        int r110 = r13;
        HlsSampleStreamWrapper r20 = buildSampleStreamWrapper(r8, 3, r15, r17, null, Collections.emptyList(), r18, r22);
        r6 = r18;
        r19.add(new int[]{r14});
        r16.add(r20);
        TrackGroup[] r21 = new TrackGroup[1];
        Format[] r23 = new Format[1];
        r23[r110] = this.extractorFactory.getOutputTextFormat(r9);
        r21[r110] = new TrackGroup(r8, r23);
        r20.prepareWithMultivariantPlaylistInfo(r21, r110, new int[r110]);
        r2 = r14 + 1;
        r5 = r19;
        r4 = r16;
        r11 = r11;
        r10 = 1;
        r13 = r110;
        goto L12
    L14:
        int r24 = r13;
        this.sampleStreamWrappers = (HlsSampleStreamWrapper[]) r4.toArray(new HlsSampleStreamWrapper[r24]);
        this.manifestUrlIndicesPerWrapper = (int[][]) r5.toArray(new int[r24][]);
        this.pendingPrepareCount = this.sampleStreamWrappers.length;
        int r25 = 0;
    L16:
        if (r25 >= this.audioVideoSampleStreamWrapperCount) goto L18;
        this.sampleStreamWrappers[r25].setIsPrimaryTimestampSource(true);
        r25 = r25 + 1;
        goto L16
    L18:
        HlsSampleStreamWrapper[] r26 = this.sampleStreamWrappers;
        int r27 = r26.length;
    L19:
        if (r24 >= r27) goto L21;
        r26[r24].continuePreparing();
        r24 = r24 + 1;
        goto L19
    L21:
        this.enabledSampleStreamWrappers = this.sampleStreamWrappers;
        return;
    L5:
        r6 = Collections.emptyMap();
        goto L6
    }

    private HlsSampleStreamWrapper buildSampleStreamWrapper(String r18, int r19, Uri[] r20, Format[] r21, Format r22, List<Format> r23, Map<String, DrmInitData> r24, long r25) {
        HlsChunkSource r1 = new HlsChunkSource(this.extractorFactory, this.playlistTracker, r20, r21, this.dataSourceFactory, this.mediaTransferListener, this.timestampAdjusterProvider, this.timestampAdjusterInitializationTimeoutMs, r23, this.playerId, this.cmcdConfiguration);
        return new HlsSampleStreamWrapper(r18, r19, this.sampleStreamWrapperCallback, r1, r24, this.allocator, r25, r22, this.drmSessionManager, this.drmEventDispatcher, this.loadErrorHandlingPolicy, this.eventDispatcher, this.metadataType);
    }

    private static Format deriveAudioFormat(Format r13, Format r14, boolean r15) {
        int r0 = -1;
        int r1 = 0;
        int r2 = 0;
        String r3 = null;
        String r4 = null;
        List<Label> r5 = ImmutableList.m212of();
        if (r14 == null) goto L5;
        String r6 = r14.codecs;
        Metadata r7 = r14.metadata;
        r0 = r14.channelCount;
        r1 = r14.selectionFlags;
        r2 = r14.roleFlags;
        r3 = r14.language;
        r4 = r14.label;
        r5 = r14.labels;
    L8:
        String r8 = MimeTypes.getMediaMimeType(r6);
        int r9 = -1;
        if (r15 == false) goto L11;
        int r10 = r13.averageBitrate;
    L12:
        if (r15 == false) goto L15;
        r9 = r13.peakBitrate;
    L15:
        return new Format.Builder().setId(r13.f26id).setLabel(r4).setLabels(r5).setContainerMimeType(r13.containerMimeType).setSampleMimeType(r8).setCodecs(r6).setMetadata(r7).setAverageBitrate(r10).setPeakBitrate(r9).setChannelCount(r0).setSelectionFlags(r1).setRoleFlags(r2).setLanguage(r3).build();
    L11:
        r10 = -1;
        goto L12
    L5:
        r6 = Util.getCodecsOfType(r13.codecs, 1);
        r7 = r13.metadata;
        if (r15 == false) goto L8;
        r0 = r13.channelCount;
        r1 = r13.selectionFlags;
        r2 = r13.roleFlags;
        r3 = r13.language;
        r4 = r13.label;
        r5 = r13.labels;
        goto L8
    }

    private static Map<String, DrmInitData> deriveOverridingDrmInitData(List<DrmInitData> r8) {
        ArrayList r0 = new ArrayList(r8);
        HashMap r1 = new HashMap();
        int r2 = 0;
    L4:
        if (r2 >= r0.size()) goto L14;
        DrmInitData r3 = r8.get(r2);
        String r4 = r3.schemeType;
        int r5 = r2 + 1;
    L7:
        if (r5 >= r0.size()) goto L13;
        DrmInitData r6 = (DrmInitData) r0.get(r5);
        if (TextUtils.equals(r6.schemeType, r4) == true) goto L10;
        r5 = r5 + 1;
        goto L7
    L10:
        r3 = r3.merge(r6);
        r0.remove(r5);
        goto L7
    L13:
        r1.put(r4, r3);
        r2 = r2 + 1;
        goto L4
    L14:
        return r1;
    }

    private static Format deriveVideoFormat(Format r4) {
        String r0 = Util.getCodecsOfType(r4.codecs, 2);
        String r1 = MimeTypes.getMediaMimeType(r0);
        return new Format.Builder().setId(r4.f26id).setLabel(r4.label).setLabels(r4.labels).setContainerMimeType(r4.containerMimeType).setSampleMimeType(r1).setCodecs(r0).setMetadata(r4.metadata).setAverageBitrate(r4.averageBitrate).setPeakBitrate(r4.peakBitrate).setWidth(r4.width).setHeight(r4.height).setFrameRate(r4.frameRate).setSelectionFlags(r4.selectionFlags).setRoleFlags(r4.roleFlags).build();
    }

    static /* synthetic */ List lambda$selectTracks$0(HlsSampleStreamWrapper r1) {
        return r1.getTrackGroups().getTrackTypes();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r6) {
        if (this.trackGroups != null) goto L9;
        HlsSampleStreamWrapper[] r0 = this.sampleStreamWrappers;
        int r1 = r0.length;
        int r3 = 0;
    L5:
        if (r3 >= r1) goto L7;
        r0[r3].continuePreparing();
        r3 = r3 + 1;
        goto L5
    L7:
        return false;
    L9:
        return this.compositeSequenceableLoader.continueLoading(r6);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r5, boolean r7) {
        HlsSampleStreamWrapper[] r0 = this.enabledSampleStreamWrappers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].discardBuffer(r5, r7);
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long r8, SeekParameters r10) {
        HlsSampleStreamWrapper[] r2 = this.enabledSampleStreamWrappers;
        int r3 = r2.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L11;
        HlsSampleStreamWrapper r5 = r2[r4];
        if (r5.isVideoSampleStream() == true) goto L8;
        r4 = r4 + 1;
        goto L3
    L8:
        return r5.getAdjustedSeekPositionUs(r8, r10);
    L11:
        return r8;
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
    public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r24) {
        HlsMediaPeriod r0 = this;
        HlsMultivariantPlaylist r1 = (HlsMultivariantPlaylist) Assertions.checkNotNull(r0.playlistTracker.getMultivariantPlaylist());
        boolean r2 = !r1.variants.isEmpty();
        if (r2 == false) goto L5;
        int r5 = 1;
    L6:
        int r6 = r0.sampleStreamWrappers.length - r1.subtitles.size();
        if (r2 == false) goto L9;
        HlsSampleStreamWrapper r7 = r0.sampleStreamWrappers[0];
        int[] r8 = r0.manifestUrlIndicesPerWrapper[0];
        TrackGroupArray r9 = r7.getTrackGroups();
        int r10 = r7.getPrimaryTrackGroupIndex();
    L10:
        ArrayList r11 = new ArrayList();
        boolean r12 = false;
        boolean r13 = false;
        Iterator<ExoTrackSelection> r14 = r24.iterator();
    L12:
        if (r14.hasNext() == false) goto L39;
        ExoTrackSelection r15 = r14.next();
        TrackGroup r16 = r15.getTrackGroup();
        int r3 = r9.indexOf(r16);
        if (r3 == (-1)) goto L22;
        if (r3 != r10) goto L21;
        boolean r4 = true;
        int r17 = 0;
    L17:
        boolean r18 = r2;
        if (r17 >= r15.length()) goto L20;
        r11.add(new StreamKey(0, r8[r15.getIndexInTrackGroup(r17)]));
        r17 = r17 + 1;
        r2 = r18;
        r3 = r3;
        r4 = r4;
        goto L17
    L20:
        r13 = r4;
        int r19 = r5;
    L37:
        r0 = this;
        r2 = r18;
        r5 = r19;
        goto L12
    L21:
        r18 = r2;
        r12 = true;
        r19 = r5;
        goto L37
    L22:
        r18 = r2;
        int r20 = r5;
    L24:
        if (r20 >= r0.sampleStreamWrappers.length) goto L36;
        r19 = r5;
        if (r0.sampleStreamWrappers[r20].getTrackGroups().indexOf(r16) != (-1)) goto L27;
        r20 = r20 + 1;
        r0 = this;
        r5 = r19;
        goto L24
    L27:
        if (r20 >= r6) goto L29;
        int r21 = 1;
    L30:
        int[] r22 = r0.manifestUrlIndicesPerWrapper[r20];
        int r23 = 0;
    L31:
        int[] r25 = r22;
        if (r23 >= r15.length()) goto L37;
        r11.add(new StreamKey(r21, r25[r15.getIndexInTrackGroup(r23)]));
        r23 = r23 + 1;
        r22 = r25;
        goto L31
    L29:
        r21 = 2;
        goto L30
    L36:
        r19 = r5;
        goto L37
    L39:
        if (r12 == false) goto L49;
        if (r13 == true) goto L49;
        int r26 = r8[0];
        int r27 = r1.variants.get(r8[0]).format.bitrate;
        int r28 = 1;
    L43:
        if (r28 >= r8.length) goto L48;
        int r29 = r1.variants.get(r8[r28]).format.bitrate;
        if (r29 >= r27) goto L47;
        r27 = r29;
        r26 = r8[r28];
    L47:
        r28 = r28 + 1;
        goto L43
    L48:
        r11.add(new StreamKey(0, r26));
    L49:
        return r11;
    L9:
        r8 = new int[0];
        r9 = TrackGroupArray.EMPTY;
        r10 = 0;
        goto L10
    L5:
        r5 = 0;
        goto L6
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return (TrackGroupArray) Assertions.checkNotNull(this.trackGroups);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean isLoading() {
        return this.compositeSequenceableLoader.isLoading();
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        HlsSampleStreamWrapper[] r0 = this.sampleStreamWrappers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].maybeThrowPrepareError();
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
    public void onPlaylistChanged() {
        HlsSampleStreamWrapper[] r0 = this.sampleStreamWrappers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].onPlaylistUpdated();
        r2 = r2 + 1;
        goto L3
    L5:
        this.mediaPeriodCallback.onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
    public boolean onPlaylistError(Uri r7, LoadErrorHandlingPolicy.LoadErrorInfo r8, boolean r9) {
        boolean r0 = true;
        HlsSampleStreamWrapper[] r1 = this.sampleStreamWrappers;
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = r0 & r1[r3].onPlaylistError(r7, r8, r9);
        r3 = r3 + 1;
        goto L3
    L5:
        this.mediaPeriodCallback.onContinueLoadingRequested(this);
        return r0;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r2, long r3) {
        this.mediaPeriodCallback = r2;
        this.playlistTracker.addListener(this);
        buildAndPrepareSampleStreamWrappers(r3);
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
        this.playlistTracker.removeListener(this);
        HlsSampleStreamWrapper[] r0 = this.sampleStreamWrappers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    L5:
        this.mediaPeriodCallback = null;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r4) {
        if (this.enabledSampleStreamWrappers.length <= 0) goto L10;
        boolean r0 = this.enabledSampleStreamWrappers[0].seekToUs(r4, false);
        int r1 = 1;
    L6:
        if (r1 >= this.enabledSampleStreamWrappers.length) goto L8;
        this.enabledSampleStreamWrappers[r1].seekToUs(r4, r0);
        r1 = r1 + 1;
        goto L6
    L8:
        if (r0 == false) goto L10;
        this.timestampAdjusterProvider.reset();
    L10:
        return r4;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r21, boolean[] r22, SampleStream[] r23, boolean[] r24, long r25) {
        ExoTrackSelection[] r1 = r21;
        int[] r3 = new int[r1.length];
        int[] r4 = new int[r1.length];
        int r5 = 0;
    L4:
        if (r5 >= r1.length) goto L19;
        if (r23[r5] != null) goto L8;
        int r6 = -1;
    L9:
        r3[r5] = r6;
        r4[r5] = -1;
        if (r1[r5] == null) goto L18;
        TrackGroup r7 = r1[r5].getTrackGroup();
        int r8 = 0;
    L13:
        if (r8 >= this.sampleStreamWrappers.length) goto L18;
        if (this.sampleStreamWrappers[r8].getTrackGroups().indexOf(r7) != (-1)) goto L16;
        r8 = r8 + 1;
        goto L13
    L16:
        r4[r5] = r8;
    L18:
        r5 = r5 + 1;
        goto L4
    L8:
        r6 = this.streamWrapperIndices.get(r23[r5]).intValue();
        goto L9
    L19:
        this.streamWrapperIndices.clear();
        SampleStream[] r9 = new SampleStream[r1.length];
        SampleStream[] r11 = new SampleStream[r1.length];
        ExoTrackSelection[] r10 = new ExoTrackSelection[r1.length];
        int r12 = 0;
        HlsSampleStreamWrapper[] r13 = new HlsSampleStreamWrapper[this.sampleStreamWrappers.length];
        boolean r15 = false;
        int r14 = 0;
    L21:
        if (r14 >= this.sampleStreamWrappers.length) goto L63;
        int r16 = 0;
    L24:
        if (r16 >= r1.length) goto L33;
        ExoTrackSelection r17 = null;
        if (r3[r16] != r14) goto L28;
        SampleStream r18 = r23[r16];
    L29:
        r11[r16] = r18;
        if (r4[r16] != r14) goto L32;
        r17 = r1[r16];
    L32:
        r10[r16] = r17;
        r16 = r16 + 1;
        goto L24
    L28:
        r18 = null;
        goto L29
    L33:
        HlsSampleStreamWrapper r19 = this.sampleStreamWrappers[r14];
        int[] r110 = r3;
        HlsSampleStreamWrapper[] r20 = r13;
        boolean r111 = r19.selectTracks(r10, r22, r11, r24, r25, r15);
        boolean r112 = false;
        int r113 = 0;
    L35:
        if (r113 >= r1.length) goto L46;
        SampleStream r114 = r11[r113];
        if (r4[r113] != r14) goto L40;
        Assertions.checkNotNull(r114);
        r9[r113] = r114;
        r112 = true;
        this.streamWrapperIndices.put(r114, Integer.valueOf(r14));
    L45:
        r113 = r113 + 1;
        r1 = r21;
        goto L35
    L40:
        if (r110[r113] != r14) goto L45;
        if (r114 != null) goto L43;
        boolean r115 = true;
    L44:
        Assertions.checkState(r115);
        goto L45
    L43:
        r115 = false;
        goto L44
    L46:
        if (r112 == false) goto L62;
        r20[r12] = r19;
        int r2 = r12 + 1;
        if (r12 != 0) goto L57;
        r19.setIsPrimaryTimestampSource(true);
        if (r111 == false) goto L52;
    L55:
        this.timestampAdjusterProvider.reset();
        r15 = true;
        r12 = r2;
        goto L62
    L52:
        if (this.enabledSampleStreamWrappers.length == 0) goto L55;
        if (r19 != this.enabledSampleStreamWrappers[0]) goto L55;
    L61:
        r12 = r2;
        goto L62
    L57:
        if (r14 >= this.audioVideoSampleStreamWrapperCount) goto L59;
        boolean r116 = true;
    L60:
        r19.setIsPrimaryTimestampSource(r116);
        goto L61
    L59:
        r116 = false;
    L62:
        r14 = r14 + 1;
        r1 = r21;
        r13 = r20;
        r3 = r110;
        goto L21
    L63:
        System.arraycopy(r9, 0, r23, 0, r9.length);
        this.enabledSampleStreamWrappers = (HlsSampleStreamWrapper[]) Util.nullSafeArrayCopy(r13, r12);
        ImmutableList r26 = ImmutableList.copyOf(this.enabledSampleStreamWrappers);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.create(r26, Lists.transform(r26, new HlsMediaPeriod$$ExternalSyntheticLambda0()));
        return r25;
    }
}
