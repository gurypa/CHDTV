package androidx.media3.exoplayer.dash;

import android.util.Pair;
import android.util.SparseArray;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.LoadingInfo;
import androidx.media3.exoplayer.SeekParameters;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.dash.manifest.AdaptationSet;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.dash.manifest.Descriptor;
import androidx.media3.exoplayer.dash.manifest.EventStream;
import androidx.media3.exoplayer.dash.manifest.Period;
import androidx.media3.exoplayer.dash.manifest.Representation;
import androidx.media3.exoplayer.drm.DrmSessionEventListener;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.media3.exoplayer.source.CompositeSequenceableLoaderFactory;
import androidx.media3.exoplayer.source.EmptySampleStream;
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
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class DashMediaPeriod implements MediaPeriod, SequenceableLoader.Callback<ChunkSampleStream<DashChunkSource>>, ChunkSampleStream.ReleaseCallback<DashChunkSource> {
    private static final Pattern CEA608_SERVICE_DESCRIPTOR_REGEX = null;
    private static final Pattern CEA708_SERVICE_DESCRIPTOR_REGEX = null;
    private final Allocator allocator;
    private final BaseUrlExclusionList baseUrlExclusionList;
    private MediaPeriod.Callback callback;
    private boolean canReportInitialDiscontinuity;
    private final DashChunkSource.Factory chunkSourceFactory;
    private final CmcdConfiguration cmcdConfiguration;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private final long elapsedRealtimeOffsetMs;
    private EventSampleStream[] eventSampleStreams;
    private List<EventStream> eventStreams;

    /* JADX INFO: renamed from: id */
    final int f37id;
    private long initialStartTimeUs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final MediaSourceEventListener.EventDispatcher mediaSourceEventDispatcher;
    private int periodIndex;
    private final PlayerEmsgHandler playerEmsgHandler;
    private final PlayerId playerId;
    private ChunkSampleStream<DashChunkSource>[] sampleStreams;
    private final IdentityHashMap<ChunkSampleStream<DashChunkSource>, PlayerEmsgHandler.PlayerTrackEmsgHandler> trackEmsgHandlerBySampleStream;
    private final TrackGroupInfo[] trackGroupInfos;
    private final TrackGroupArray trackGroups;
    private final TransferListener transferListener;

    private static final class TrackGroupInfo {
        private static final int CATEGORY_EMBEDDED = 1;
        private static final int CATEGORY_MANIFEST_EVENTS = 2;
        private static final int CATEGORY_PRIMARY = 0;
        public final int[] adaptationSetIndices;
        public final int embeddedClosedCaptionTrackGroupIndex;
        public final ImmutableList<Format> embeddedClosedCaptionTrackOriginalFormats;
        public final int embeddedEventMessageTrackGroupIndex;
        public final int eventStreamGroupIndex;
        public final int primaryTrackGroupIndex;
        public final int trackGroupCategory;
        public final int trackType;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface TrackGroupCategory {
        }

        private TrackGroupInfo(int r1, int r2, int[] r3, int r4, int r5, int r6, int r7, ImmutableList<Format> r8) {
            this.trackType = r1;
            this.adaptationSetIndices = r3;
            this.trackGroupCategory = r2;
            this.primaryTrackGroupIndex = r4;
            this.embeddedEventMessageTrackGroupIndex = r5;
            this.embeddedClosedCaptionTrackGroupIndex = r6;
            this.eventStreamGroupIndex = r7;
            this.embeddedClosedCaptionTrackOriginalFormats = r8;
        }

        public static TrackGroupInfo embeddedClosedCaptionTrack(int[] r9, int r10, ImmutableList<Format> r11) {
            return new TrackGroupInfo(3, 1, r9, r10, -1, -1, -1, r11);
        }

        public static TrackGroupInfo embeddedEmsgTrack(int[] r9, int r10) {
            return new TrackGroupInfo(5, 1, r9, r10, -1, -1, -1, ImmutableList.m212of());
        }

        public static TrackGroupInfo mpdEventTrack(int r9) {
            return new TrackGroupInfo(5, 2, new int[0], -1, -1, -1, r9, ImmutableList.m212of());
        }

        public static TrackGroupInfo primaryTrack(int r9, int[] r10, int r11, int r12, int r13) {
            return new TrackGroupInfo(r9, 0, r10, r11, r12, r13, -1, ImmutableList.m212of());
        }
    }

    static {
        CEA608_SERVICE_DESCRIPTOR_REGEX = Pattern.compile("CC([1-4])=(.+)");
        CEA708_SERVICE_DESCRIPTOR_REGEX = Pattern.compile("([1-4])=lang:(\\w+)(,.+)?");
    }

    public DashMediaPeriod(int r18, DashManifest r19, BaseUrlExclusionList r20, int r21, DashChunkSource.Factory r22, TransferListener r23, CmcdConfiguration r24, DrmSessionManager r25, DrmSessionEventListener.EventDispatcher r26, LoadErrorHandlingPolicy r27, MediaSourceEventListener.EventDispatcher r28, long r29, LoaderErrorThrower r31, Allocator r32, CompositeSequenceableLoaderFactory r33, PlayerEmsgHandler.PlayerEmsgCallback r34, PlayerId r35) {
        this.f37id = r18;
        this.manifest = r19;
        this.baseUrlExclusionList = r20;
        this.periodIndex = r21;
        this.chunkSourceFactory = r22;
        this.transferListener = r23;
        this.cmcdConfiguration = r24;
        this.drmSessionManager = r25;
        this.drmEventDispatcher = r26;
        this.loadErrorHandlingPolicy = r27;
        this.mediaSourceEventDispatcher = r28;
        this.elapsedRealtimeOffsetMs = r29;
        this.manifestLoaderErrorThrower = r31;
        this.allocator = r32;
        this.compositeSequenceableLoaderFactory = r33;
        this.playerId = r35;
        this.canReportInitialDiscontinuity = true;
        this.playerEmsgHandler = new PlayerEmsgHandler(r19, r34, r32);
        this.sampleStreams = newSampleStreamArray(0);
        this.eventSampleStreams = new EventSampleStream[0];
        this.trackEmsgHandlerBySampleStream = new IdentityHashMap();
        this.compositeSequenceableLoader = r33.empty();
        Period r6 = r19.getPeriod(r21);
        this.eventStreams = r6.eventStreams;
        Pair<TrackGroupArray, TrackGroupInfo[]> r1 = buildTrackGroups(r25, r22, r6.adaptationSets, this.eventStreams);
        this.trackGroups = (TrackGroupArray) r1.first;
        this.trackGroupInfos = (TrackGroupInfo[]) r1.second;
    }

    private static void buildManifestEventTrackGroupInfos(List<EventStream> r7, TrackGroup[] r8, TrackGroupInfo[] r9, int r10) {
        int r0 = 0;
    L4:
        if (r0 >= r7.size()) goto L6;
        EventStream r1 = r7.get(r0);
        r8[r10] = new TrackGroup(r1.m91id() + ":" + r0, new Format[]{new Format.Builder().setId(r1.m91id()).setSampleMimeType(MimeTypes.APPLICATION_EMSG).build()});
        r9[r10] = TrackGroupInfo.mpdEventTrack(r0);
        r0 = r0 + 1;
        r10 = r10 + 1;
        goto L4
    }

    private static int buildPrimaryAndEmbeddedTrackGroupInfos(DrmSessionManager r19, DashChunkSource.Factory r20, List<AdaptationSet> r21, int[][] r22, int r23, boolean[] r24, Format[][] r25, TrackGroup[] r26, TrackGroupInfo[] r27) {
        int r2 = 0;
        int r3 = 0;
    L4:
        if (r3 >= r23) goto L32;
        int[] r5 = r22[r3];
        ArrayList r6 = new ArrayList();
        int r7 = r5.length;
        int r9 = 0;
    L6:
        if (r9 >= r7) goto L8;
        r6.addAll(r21.get(r5[r9]).representations);
        r9 = r9 + 1;
        goto L6
    L8:
        Format[] r8 = new Format[r6.size()];
        int r10 = 0;
    L10:
        if (r10 >= r8.length) goto L12;
        Format r11 = ((Representation) r6.get(r10)).format;
        r8[r10] = r11.buildUpon().setCryptoType(r19.getCryptoType(r11)).build();
        r10 = r10 + 1;
        goto L10
    L12:
        AdaptationSet r12 = r21.get(r5[0]);
        if (r12.f38id == (-1)) goto L15;
        String r13 = Long.toString(r12.f38id);
    L16:
        int r14 = r2 + 1;
        if (r24[r3] == false) goto L19;
        int r15 = r14 + 1;
    L21:
        if (r25[r3].length == 0) goto L23;
        int r16 = r15;
        r15 = r15 + 1;
    L24:
        maybeUpdateFormatsForParsedText(r20, r8);
        r26[r2] = new TrackGroup(r13, r8);
        r27[r2] = TrackGroupInfo.primaryTrack(r12.type, r5, r2, r14, r16);
        if (r14 == (-1)) goto L28;
        String r17 = r13 + ":emsg";
        r26[r14] = new TrackGroup(r17, new Format[]{new Format.Builder().setId(r17).setSampleMimeType(MimeTypes.APPLICATION_EMSG).build()});
        r27[r14] = TrackGroupInfo.embeddedEmsgTrack(r5, r2);
    L28:
        if (r16 == (-1)) goto L30;
        r27[r16] = TrackGroupInfo.embeddedClosedCaptionTrack(r5, r2, ImmutableList.copyOf(r25[r3]));
        maybeUpdateFormatsForParsedText(r20, r25[r3]);
        r26[r16] = new TrackGroup(r13 + ":cc", r25[r3]);
    L30:
        r3 = r3 + 1;
        r2 = r15;
        goto L4
    L23:
        r16 = -1;
        goto L24
    L19:
        r15 = r14;
        r14 = -1;
        goto L21
    L15:
        r13 = "unset:" + r3;
        goto L16
    L32:
        return r2;
    }

    private ChunkSampleStream<DashChunkSource> buildSampleStream(TrackGroupInfo r34, ExoTrackSelection r35, long r36) {
        int r0 = 0;
        if (r34.embeddedEventMessageTrackGroupIndex == (-1)) goto L5;
        boolean r1 = true;
    L6:
        boolean r26 = r1;
        TrackGroup r2 = null;
        if (r26 == false) goto L10;
        r2 = this.trackGroups.get(r34.embeddedEventMessageTrackGroupIndex);
        r0 = 0 + 1;
    L10:
        if (r34.embeddedClosedCaptionTrackGroupIndex == (-1)) goto L12;
        ImmutableList<Format> r3 = this.trackGroupInfos[r34.embeddedClosedCaptionTrackGroupIndex].embeddedClosedCaptionTrackOriginalFormats;
    L13:
        int r4 = r0 + r3.size();
        Format[] r5 = new Format[r4];
        int[] r6 = new int[r4];
        int r7 = 0;
        if (r26 == false) goto L16;
        r5[0] = r2.getFormat(0);
        r6[0] = 5;
        r7 = 0 + 1;
    L16:
        ArrayList r27 = new ArrayList();
        int r8 = 0;
        int r32 = r7;
    L18:
        if (r8 >= r3.size()) goto L21;
        r5[r32] = r3.get(r8);
        r6[r32] = 3;
        r27.add(r5[r32]);
        r32 = r32 + 1;
        r8 = r8 + 1;
        goto L18
    L21:
        if (this.manifest.dynamic == false) goto L24;
        if (r26 == false) goto L24;
        PlayerEmsgHandler.PlayerTrackEmsgHandler r28 = this.playerEmsgHandler.newPlayerTrackEmsgHandler();
    L25:
        PlayerEmsgHandler.PlayerTrackEmsgHandler r15 = r28;
        ChunkSampleStream<DashChunkSource> r9 = new ChunkSampleStream(r34.trackType, r6, r5, this.chunkSourceFactory.createDashChunkSource(this.manifestLoaderErrorThrower, this.manifest, this.baseUrlExclusionList, this.periodIndex, r34.adaptationSetIndices, r35, r34.trackType, this.elapsedRealtimeOffsetMs, r26, r27, r28, this.transferListener, this.playerId, this.cmcdConfiguration), this, this.allocator, r36, this.drmSessionManager, this.drmEventDispatcher, this.loadErrorHandlingPolicy, this.mediaSourceEventDispatcher, this.canReportInitialDiscontinuity, null);
        monitor-enter(this);
        this.trackEmsgHandlerBySampleStream.put(r9, r15);     // Catch: Throwable -> L30
        monitor-exit(this);     // Catch: Throwable -> L30
        return r9;
    L30:
        th = move-exception;
        throw th;
    L24:
        r28 = null;
        goto L25
    L12:
        r3 = ImmutableList.m212of();
        goto L13
    L5:
        r1 = false;
        goto L6
    }

    private static Pair<TrackGroupArray, TrackGroupInfo[]> buildTrackGroups(DrmSessionManager r11, DashChunkSource.Factory r12, List<AdaptationSet> r13, List<EventStream> r14) {
        int[][] r3 = getGroupedAdaptationSetIndices(r13);
        int r4 = r3.length;
        boolean[] r5 = new boolean[r4];
        Format[][] r6 = new Format[r4][];
        int r10 = (r4 + identifyEmbeddedTracks(r4, r13, r3, r5, r6)) + r14.size();
        TrackGroup[] r7 = new TrackGroup[r10];
        TrackGroupInfo[] r8 = new TrackGroupInfo[r10];
        buildManifestEventTrackGroupInfos(r14, r7, r8, buildPrimaryAndEmbeddedTrackGroupInfos(r11, r12, r13, r3, r4, r5, r6, r7, r8));
        return Pair.create(new TrackGroupArray(r7), r8);
    }

    private static Descriptor findAdaptationSetSwitchingProperty(List<Descriptor> r1) {
        return findDescriptor(r1, "urn:mpeg:dash:adaptation-set-switching:2016");
    }

    private static Descriptor findDescriptor(List<Descriptor> r3, String r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L9;
        Descriptor r1 = r3.get(r0);
        if (r4.equals(r1.schemeIdUri) == true) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r1;
    L9:
        return null;
    }

    private static Descriptor findTrickPlayProperty(List<Descriptor> r1) {
        return findDescriptor(r1, "http://dashif.org/guidelines/trickmode");
    }

    private static Format[] getClosedCaptionTrackFormats(List<AdaptationSet> r10, int[] r11) {
        int r0 = r11.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L18;
        int r3 = r11[r2];
        AdaptationSet r4 = r10.get(r3);
        List<Descriptor> r5 = r10.get(r3).accessibilityDescriptors;
        int r6 = 0;
    L6:
        if (r6 >= r5.size()) goto L16;
        Descriptor r7 = r5.get(r6);
        if ("urn:scte:dash:cc:cea-608:2015".equals(r7.schemeIdUri) == true) goto L10;
        if ("urn:scte:dash:cc:cea-708:2015".equals(r7.schemeIdUri) == true) goto L14;
        r6 = r6 + 1;
        goto L6
    L14:
        return parseClosedCaptionDescriptor(r7, CEA708_SERVICE_DESCRIPTOR_REGEX, new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_CEA708).setId(r4.f38id + ":cea708").build());
    L10:
        return parseClosedCaptionDescriptor(r7, CEA608_SERVICE_DESCRIPTOR_REGEX, new Format.Builder().setSampleMimeType(MimeTypes.APPLICATION_CEA608).setId(r4.f38id + ":cea608").build());
    L16:
        r2 = r2 + 1;
        goto L3
    L18:
        return new Format[0];
    }

    private static int[][] getGroupedAdaptationSetIndices(List<AdaptationSet> r15) {
        int r0 = r15.size();
        HashMap r1 = Maps.newHashMapWithExpectedSize(r0);
        ArrayList r2 = new ArrayList(r0);
        SparseArray r3 = new SparseArray(r0);
        int r4 = 0;
    L3:
        if (r4 >= r0) goto L5;
        r1.put(Long.valueOf(r15.get(r4).f38id), Integer.valueOf(r4));
        ArrayList r5 = new ArrayList();
        r5.add(Integer.valueOf(r4));
        r2.add(r5);
        r3.put(r4, r5);
        r4 = r4 + 1;
        goto L3
    L5:
        int r6 = 0;
    L6:
        if (r6 >= r0) goto L26;
        int r7 = r6;
        AdaptationSet r8 = r15.get(r6);
        Descriptor r9 = findTrickPlayProperty(r8.essentialProperties);
        if (r9 != null) goto L10;
        r9 = findTrickPlayProperty(r8.supplementalProperties);
    L10:
        if (r9 == null) goto L14;
        Integer r10 = (Integer) r1.get(Long.valueOf(Long.parseLong(r9.value)));
        if (r10 == null) goto L14;
        r7 = r10.intValue();
    L14:
        if (r7 != r6) goto L23;
        Descriptor r11 = findAdaptationSetSwitchingProperty(r8.supplementalProperties);
        if (r11 == null) goto L23;
        String[] r12 = Util.split(r11.value, ",");
        int r13 = r12.length;
        int r14 = 0;
    L18:
        if (r14 >= r13) goto L23;
        Integer r16 = (Integer) r1.get(Long.valueOf(Long.parseLong(r12[r14])));
        if (r16 == null) goto L22;
        r7 = Math.min(r7, r16.intValue());
    L22:
        r14 = r14 + 1;
    L23:
        if (r7 == r6) goto L25;
        List r17 = (List) r3.get(r6);
        List r18 = (List) r3.get(r7);
        r18.addAll(r17);
        r3.put(r6, r18);
        r2.remove(r17);
    L25:
        r6 = r6 + 1;
        goto L6
    L26:
        int[][] r19 = new int[r2.size()][];
        int r20 = 0;
    L28:
        if (r20 >= r19.length) goto L30;
        r19[r20] = Ints.toArray((Collection) r2.get(r20));
        Arrays.sort(r19[r20]);
        r20 = r20 + 1;
        goto L28
    L30:
        return r19;
    }

    private int getPrimaryStreamIndex(int r7, int[] r8) {
        int r0 = r8[r7];
        if (r0 != (-1)) goto L5;
        return -1;
    L5:
        int r2 = this.trackGroupInfos[r0].primaryTrackGroupIndex;
        int r3 = 0;
    L7:
        if (r3 >= r8.length) goto L14;
        int r4 = r8[r3];
        if (r4 != r2) goto L13;
        if (this.trackGroupInfos[r4].trackGroupCategory != 0) goto L13;
        return r3;
    L13:
        r3 = r3 + 1;
        goto L7
    L14:
        return -1;
    }

    private int[] getStreamIndexToTrackGroupIndex(ExoTrackSelection[] r5) {
        int[] r0 = new int[r5.length];
        int r1 = 0;
    L4:
        if (r1 >= r5.length) goto L10;
        if (r5[r1] == null) goto L8;
        r0[r1] = this.trackGroups.indexOf(r5[r1].getTrackGroup());
    L9:
        r1 = r1 + 1;
        goto L4
    L8:
        r0[r1] = -1;
        goto L9
    L10:
        return r0;
    }

    private static boolean hasEventMessageTrack(List<AdaptationSet> r8, int[] r9) {
        int r0 = r9.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L13;
        List<Representation> r4 = r8.get(r9[r2]).representations;
        int r5 = 0;
    L6:
        if (r5 >= r4.size()) goto L12;
        if (r4.get(r5).inbandEventStreams.isEmpty() == false) goto L9;
        r5 = r5 + 1;
        goto L6
    L9:
        return true;
    L12:
        r2 = r2 + 1;
        goto L3
    L13:
        return false;
    }

    private static int identifyEmbeddedTracks(int r3, List<AdaptationSet> r4, int[][] r5, boolean[] r6, Format[][] r7) {
        int r0 = 0;
        int r1 = 0;
    L3:
        if (r1 >= r3) goto L11;
        if (hasEventMessageTrack(r4, r5[r1]) == false) goto L7;
        r6[r1] = true;
        r0 = r0 + 1;
    L7:
        r7[r1] = getClosedCaptionTrackFormats(r4, r5[r1]);
        if (r7[r1].length == 0) goto L10;
        r0 = r0 + 1;
    L10:
        r1 = r1 + 1;
        goto L3
    L11:
        return r0;
    }

    static /* synthetic */ List lambda$selectTracks$0(ChunkSampleStream r1) {
        return ImmutableList.m213of(Integer.valueOf(r1.primaryTrackType));
    }

    private static void maybeUpdateFormatsForParsedText(DashChunkSource.Factory r2, Format[] r3) {
        int r0 = 0;
    L4:
        if (r0 >= r3.length) goto L6;
        r3[r0] = r2.getOutputTextFormat(r3[r0]);
        r0 = r0 + 1;
        goto L4
    }

    private static ChunkSampleStream<DashChunkSource>[] newSampleStreamArray(int r1) {
        return new ChunkSampleStream[r1];
    }

    private static Format[] parseClosedCaptionDescriptor(Descriptor r11, Pattern r12, Format r13) {
        String r0 = r11.value;
        if (r0 == null) goto L5;
        String[] r3 = Util.split(r0, ";");
        Format[] r4 = new Format[r3.length];
        int r5 = 0;
    L8:
        if (r5 >= r3.length) goto L14;
        Matcher r6 = r12.matcher(r3[r5]);
        if (r6.matches() == false) goto L12;
        int r7 = Integer.parseInt(r6.group(1));
        r4[r5] = r13.buildUpon().setId(r13.f26id + ":" + r7).setAccessibilityChannel(r7).setLanguage(r6.group(2)).build();
        r5 = r5 + 1;
        goto L8
    L12:
        return new Format[]{r13};
    L14:
        return r4;
    L5:
        return new Format[]{r13};
    }

    private void releaseDisabledStreams(ExoTrackSelection[] r3, boolean[] r4, SampleStream[] r5) {
        int r0 = 0;
    L4:
        if (r0 >= r3.length) goto L18;
        if (r3[r0] == null) goto L10;
        if (r4[r0] == false) goto L10;
    L17:
        r0 = r0 + 1;
    L10:
        if ((r5[r0] instanceof ChunkSampleStream) == false) goto L13;
        ((ChunkSampleStream) r5[r0]).release(this);
    L16:
        r5[r0] = null;
        goto L17
    L13:
        if ((r5[r0] instanceof ChunkSampleStream.EmbeddedSampleStream) == false) goto L16;
        ((ChunkSampleStream.EmbeddedSampleStream) r5[r0]).release();
        goto L16
    }

    private void releaseOrphanEmbeddedStreams(ExoTrackSelection[] r5, SampleStream[] r6, int[] r7) {
        int r0 = 0;
    L4:
        if (r0 >= r5.length) goto L24;
        if ((r6[r0] instanceof EmptySampleStream) == false) goto L8;
    L9:
        int r1 = getPrimaryStreamIndex(r0, r7);
        if (r1 != (-1)) goto L13;
        boolean r2 = r6[r0] instanceof EmptySampleStream;
    L18:
        if (r2 == true) goto L23;
        if ((r6[r0] instanceof ChunkSampleStream.EmbeddedSampleStream) == false) goto L22;
        ((ChunkSampleStream.EmbeddedSampleStream) r6[r0]).release();
    L22:
        r6[r0] = null;
    L23:
        r0 = r0 + 1;
        goto L4
    L13:
        if ((r6[r0] instanceof ChunkSampleStream.EmbeddedSampleStream) == true) goto L15;
    L17:
        r2 = false;
        goto L18
    L15:
        if (((ChunkSampleStream.EmbeddedSampleStream) r6[r0]).parent != r6[r1]) goto L17;
        r2 = true;
        goto L18
    L8:
        if ((r6[r0] instanceof ChunkSampleStream.EmbeddedSampleStream) == false) goto L23;
    }

    private void selectNewStreams(ExoTrackSelection[] r9, SampleStream[] r10, boolean[] r11, long r12, int[] r14) {
        int r0 = 0;
    L4:
        if (r0 >= r9.length) goto L21;
        ExoTrackSelection r1 = r9[r0];
        if (r1 == null) goto L20;
        if (r10[r0] != null) goto L17;
        r11[r0] = true;
        int r2 = r14[r0];
        TrackGroupInfo r3 = this.trackGroupInfos[r2];
        if (r3.trackGroupCategory != 0) goto L14;
        r10[r0] = buildSampleStream(r3, r1, r12);
        goto L20
    L14:
        if (r3.trackGroupCategory != 2) goto L20;
        r10[r0] = new EventSampleStream(this.eventStreams.get(r3.eventStreamGroupIndex), r1.getTrackGroup().getFormat(0), this.manifest.dynamic);
        goto L20
    L17:
        if ((r10[r0] instanceof ChunkSampleStream) == false) goto L20;
        ((DashChunkSource) ((ChunkSampleStream) r10[r0]).getChunkSource()).updateTrackSelection(r1);
    L20:
        r0 = r0 + 1;
        goto L4
    L21:
        int r4 = 0;
    L23:
        if (r4 >= r9.length) goto L35;
        if (r10[r4] != null) goto L34;
        if (r9[r4] == null) goto L34;
        int r5 = r14[r4];
        TrackGroupInfo r6 = this.trackGroupInfos[r5];
        if (r6.trackGroupCategory != 1) goto L34;
        int r7 = getPrimaryStreamIndex(r4, r14);
        if (r7 != (-1)) goto L33;
        r10[r4] = new EmptySampleStream();
        goto L34
    L33:
        r10[r4] = ((ChunkSampleStream) r10[r7]).selectEmbeddedTrack(r12, r6.trackType);
    L34:
        r4 = r4 + 1;
        goto L23
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo r2) {
        return this.compositeSequenceableLoader.continueLoading(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void discardBuffer(long r5, boolean r7) {
        ChunkSampleStream<DashChunkSource>[] r0 = this.sampleStreams;
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
        ChunkSampleStream<DashChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L9;
        ChunkSampleStream<DashChunkSource> r3 = r0[r2];
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
    public List<StreamKey> getStreamKeys(List<ExoTrackSelection> r19) {
        DashMediaPeriod r0 = this;
        List<AdaptationSet> r1 = r0.manifest.getPeriod(r0.periodIndex).adaptationSets;
        ArrayList r2 = new ArrayList();
        Iterator<ExoTrackSelection> r3 = r19.iterator();
    L4:
        if (r3.hasNext() == false) goto L20;
        ExoTrackSelection r4 = r3.next();
        int r5 = r0.trackGroups.indexOf(r4.getTrackGroup());
        TrackGroupInfo r6 = r0.trackGroupInfos[r5];
        if (r6.trackGroupCategory != 0) goto L4;
        int[] r7 = r6.adaptationSetIndices;
        int[] r8 = new int[r4.length()];
        int r9 = 0;
    L10:
        if (r9 >= r4.length()) goto L12;
        r8[r9] = r4.getIndexInTrackGroup(r9);
        r9 = r9 + 1;
        goto L10
    L12:
        Arrays.sort(r8);
        int r10 = 0;
        int r11 = 0;
        int r12 = 0;
        int r13 = r1.get(r7[0]).representations.size();
        int r14 = r8.length;
    L13:
        if (r12 >= r14) goto L19;
        int r15 = r8[r12];
    L16:
        if (r15 < (r11 + r13)) goto L18;
        r10 = r10 + 1;
        r11 = r11 + r13;
        r13 = r1.get(r7[r10]).representations.size();
        goto L16
    L18:
        List<AdaptationSet> r16 = r1;
        Iterator<ExoTrackSelection> r17 = r3;
        r2.add(new StreamKey(r0.periodIndex, r7[r10], r15 - r11));
        r12 = r12 + 1;
        r0 = this;
        r1 = r16;
        r3 = r17;
        goto L13
    L19:
        r0 = this;
        goto L4
    L20:
        return r2;
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

    public void onContinueLoadingRequested(ChunkSampleStream<DashChunkSource> r2) {
        this.callback.onContinueLoadingRequested(this);
    }

    @Override // androidx.media3.exoplayer.source.chunk.ChunkSampleStream.ReleaseCallback
    public synchronized void onSampleStreamReleased(ChunkSampleStream<DashChunkSource> r2) {
        monitor-enter(this);
        PlayerEmsgHandler.PlayerTrackEmsgHandler r0 = this.trackEmsgHandlerBySampleStream.remove(r2);     // Catch: Throwable -> L8
        if (r0 == null) goto L6;
        r0.release();     // Catch: Throwable -> L8
    L6:
        monitor-exit(this);
        return;
    L8:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public void prepare(MediaPeriod.Callback r1, long r2) {
        this.callback = r1;
        r1.onPrepared(this);
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long readDiscontinuity() {
        ChunkSampleStream<DashChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L9;
        if (r0[r2].consumeInitialDiscontinuity() == true) goto L7;
        r2 = r2 + 1;
        goto L3
    L7:
        return this.initialStartTimeUs;
    L9:
        return C0180C.TIME_UNSET;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod, androidx.media3.exoplayer.source.SequenceableLoader
    public void reevaluateBuffer(long r2) {
        this.compositeSequenceableLoader.reevaluateBuffer(r2);
    }

    public void release() {
        this.playerEmsgHandler.release();
        ChunkSampleStream<DashChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release(this);
        r2 = r2 + 1;
        goto L3
    L5:
        this.callback = null;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long seekToUs(long r6) {
        ChunkSampleStream<DashChunkSource>[] r0 = this.sampleStreams;
        int r1 = r0.length;
        int r2 = 0;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r0[r3].seekToUs(r6);
        r3 = r3 + 1;
        goto L3
    L5:
        EventSampleStream[] r4 = this.eventSampleStreams;
        int r5 = r4.length;
    L6:
        if (r2 >= r5) goto L8;
        r4[r2].seekToUs(r6);
        r2 = r2 + 1;
        goto L6
    L8:
        return r6;
    }

    @Override // androidx.media3.exoplayer.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] r10, boolean[] r11, SampleStream[] r12, boolean[] r13, long r14) {
        int[] r6 = getStreamIndexToTrackGroupIndex(r10);
        releaseDisabledStreams(r10, r11, r12);
        releaseOrphanEmbeddedStreams(r10, r12, r6);
        selectNewStreams(r10, r12, r13, r14, r6);
        ArrayList r15 = new ArrayList();
        ArrayList r16 = new ArrayList();
        int r17 = r12.length;
        int r18 = 0;
    L3:
        if (r18 >= r17) goto L12;
        SampleStream r7 = r12[r18];
        if ((r7 instanceof ChunkSampleStream) == false) goto L8;
        r15.add((ChunkSampleStream) r7);
    L11:
        r18 = r18 + 1;
        goto L3
    L8:
        if ((r7 instanceof EventSampleStream) == false) goto L11;
        r16.add((EventSampleStream) r7);
        goto L11
    L12:
        this.sampleStreams = newSampleStreamArray(r15.size());
        r15.toArray(this.sampleStreams);
        this.eventSampleStreams = new EventSampleStream[r16.size()];
        r16.toArray(this.eventSampleStreams);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.create(r15, Lists.transform(r15, new DashMediaPeriod$$ExternalSyntheticLambda0()));
        if (this.canReportInitialDiscontinuity == false) goto L15;
        this.canReportInitialDiscontinuity = false;
        this.initialStartTimeUs = r14;
    L15:
        return r14;
    }

    public void updateManifest(DashManifest r10, int r11) {
        this.manifest = r10;
        this.periodIndex = r11;
        this.playerEmsgHandler.updateManifest(r10);
        if (this.sampleStreams == null) goto L8;
        ChunkSampleStream<DashChunkSource>[] r0 = this.sampleStreams;
        int r2 = r0.length;
        int r3 = 0;
    L5:
        if (r3 >= r2) goto L7;
        ((DashChunkSource) r0[r3].getChunkSource()).updateManifest(r10, r11);
        r3 = r3 + 1;
        goto L5
    L7:
        this.callback.onContinueLoadingRequested(this);
    L8:
        this.eventStreams = r10.getPeriod(r11).eventStreams;
        EventSampleStream[] r1 = this.eventSampleStreams;
        int r4 = r1.length;
        int r5 = 0;
    L9:
        if (r5 >= r4) goto L23;
        EventSampleStream r6 = r1[r5];
        Iterator<EventStream> r7 = this.eventStreams.iterator();
    L12:
        if (r7.hasNext() == false) goto L22;
        EventStream r8 = r7.next();
        if (r8.m91id().equals(r6.eventStreamId()) == false) goto L12;
        boolean r9 = true;
        int r12 = r10.getPeriodCount() - 1;
        if (r10.dynamic == false) goto L19;
        if (r11 != r12) goto L19;
    L20:
        r6.updateEventStream(r8, r9);
    L19:
        r9 = false;
    L22:
        r5 = r5 + 1;
        goto L9
    }
}
