package androidx.media3.exoplayer.source;

import android.content.Context;
import androidx.media3.common.AdViewProvider;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.source.ads.AdsLoader;
import androidx.media3.exoplayer.source.ads.AdsMediaSource;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleExtractor;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultMediaSourceFactory implements MediaSourceFactory {
    private static final String TAG = "DMediaSourceFactory";
    private AdViewProvider adViewProvider;
    private AdsLoader.Provider adsLoaderProvider;
    private DataSource.Factory dataSourceFactory;
    private final DelegateFactoryLoader delegateFactoryLoader;
    private ExternalLoader externalImageLoader;
    private long liveMaxOffsetMs;
    private float liveMaxSpeed;
    private long liveMinOffsetMs;
    private float liveMinSpeed;
    private long liveTargetOffsetMs;
    private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private boolean parseSubtitlesDuringExtraction;
    private MediaSource.Factory serverSideAdInsertionMediaSourceFactory;
    private SubtitleParser.Factory subtitleParserFactory;

    @Deprecated
    public interface AdsLoaderProvider extends AdsLoader.Provider {
    }

    private static final class DelegateFactoryLoader {
        private CmcdConfiguration.Factory cmcdConfigurationFactory;
        private DataSource.Factory dataSourceFactory;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private final ExtractorsFactory extractorsFactory;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private final Map<Integer, MediaSource.Factory> mediaSourceFactories;
        private final Map<Integer, Supplier<MediaSource.Factory>> mediaSourceFactorySuppliers;
        private boolean parseSubtitlesDuringExtraction;
        private SubtitleParser.Factory subtitleParserFactory;

        public DelegateFactoryLoader(ExtractorsFactory r2, SubtitleParser.Factory r3) {
            this.extractorsFactory = r2;
            this.subtitleParserFactory = r3;
            this.mediaSourceFactorySuppliers = new HashMap();
            this.mediaSourceFactories = new HashMap();
            this.parseSubtitlesDuringExtraction = true;
        }

        private void ensureAllSuppliersAreLoaded() {
            maybeLoadSupplier(0);
            maybeLoadSupplier(1);
            maybeLoadSupplier(2);
            maybeLoadSupplier(3);
            maybeLoadSupplier(4);
        }

        static /* synthetic */ MediaSource.Factory lambda$loadSupplier$0(Class r1, DataSource.Factory r2) {
            return DefaultMediaSourceFactory.access$100(r1, r2);
        }

        static /* synthetic */ MediaSource.Factory lambda$loadSupplier$1(Class r1, DataSource.Factory r2) {
            return DefaultMediaSourceFactory.access$100(r1, r2);
        }

        static /* synthetic */ MediaSource.Factory lambda$loadSupplier$2(Class r1, DataSource.Factory r2) {
            return DefaultMediaSourceFactory.access$100(r1, r2);
        }

        static /* synthetic */ MediaSource.Factory lambda$loadSupplier$3(Class r1) {
            return DefaultMediaSourceFactory.access$000(r1);
        }

        private Supplier<MediaSource.Factory> loadSupplier(int r6) throws ClassNotFoundException {
            Supplier<MediaSource.Factory> r0 = this.mediaSourceFactorySuppliers.get(Integer.valueOf(r6));
            if (r0 == null) goto L5;
            return r0;
        L5:
            final DataSource.Factory r1 = (DataSource.Factory) Assertions.checkNotNull(this.dataSourceFactory);
            switch(r6) {
                case 0: goto L13;
                case 1: goto L12;
                case 2: goto L11;
                case 3: goto L10;
                case 4: goto L9;
                default: goto L8;
            };
        L9:
            Supplier<MediaSource.Factory> r2 = new C0332xbfb12933(this, r1);
        L14:
            this.mediaSourceFactorySuppliers.put(Integer.valueOf(r6), r2);
            return r2;
        L10:
            final Class<? extends U> r3 = Class.forName("androidx.media3.exoplayer.rtsp.RtspMediaSource$Factory").asSubclass(MediaSource.Factory.class);
            r2 = new C0331xbfb12932(r3);
            goto L14
        L11:
            final Class<? extends U> r4 = Class.forName("androidx.media3.exoplayer.hls.HlsMediaSource$Factory").asSubclass(MediaSource.Factory.class);
            r2 = new C0330xbfb12931(r4, r1);
            goto L14
        L12:
            final Class<? extends U> r5 = Class.forName("androidx.media3.exoplayer.smoothstreaming.SsMediaSource$Factory").asSubclass(MediaSource.Factory.class);
            r2 = new C0329xbfb12930(r5, r1);
            goto L14
        L13:
            final Class<? extends U> r7 = Class.forName("androidx.media3.exoplayer.dash.DashMediaSource$Factory").asSubclass(MediaSource.Factory.class);
            r2 = new C0328xbfb1292f(r7, r1);
            goto L14
        L8:
            throw new IllegalArgumentException("Unrecognized contentType: " + r6);
        }

        private Supplier<MediaSource.Factory> maybeLoadSupplier(int r3) {
            return loadSupplier(r3);
        L5:
            return null;
        }

        public MediaSource.Factory getMediaSourceFactory(int r5) throws ClassNotFoundException {
            MediaSource.Factory r0 = this.mediaSourceFactories.get(Integer.valueOf(r5));
            if (r0 == null) goto L5;
            return r0;
        L5:
            MediaSource.Factory r1 = loadSupplier(r5).get();
            if (this.cmcdConfigurationFactory == null) goto L9;
            r1.setCmcdConfigurationFactory(this.cmcdConfigurationFactory);
        L9:
            if (this.drmSessionManagerProvider == null) goto L12;
            r1.setDrmSessionManagerProvider(this.drmSessionManagerProvider);
        L12:
            if (this.loadErrorHandlingPolicy == null) goto L14;
            r1.setLoadErrorHandlingPolicy(this.loadErrorHandlingPolicy);
        L14:
            r1.setSubtitleParserFactory(this.subtitleParserFactory);
            r1.experimentalParseSubtitlesDuringExtraction(this.parseSubtitlesDuringExtraction);
            this.mediaSourceFactories.put(Integer.valueOf(r5), r1);
            return r1;
        }

        public int[] getSupportedTypes() {
            ensureAllSuppliersAreLoaded();
            return Ints.toArray(this.mediaSourceFactorySuppliers.keySet());
        }

        /* JADX INFO: renamed from: lambda$loadSupplier$4$androidx-media3-exoplayer-source-DefaultMediaSourceFactory$DelegateFactoryLoader */
        /* synthetic */ MediaSource.Factory m121xa479647d(DataSource.Factory r3) {
            return new ProgressiveMediaSource.Factory(r3, this.extractorsFactory);
        }

        public void setCmcdConfigurationFactory(CmcdConfiguration.Factory r3) {
            this.cmcdConfigurationFactory = r3;
            Iterator<MediaSource.Factory> r0 = this.mediaSourceFactories.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            r0.next().setCmcdConfigurationFactory(r3);
            goto L4
        }

        public void setDataSourceFactory(DataSource.Factory r2) {
            if (r2 == this.dataSourceFactory) goto L6;
            this.dataSourceFactory = r2;
            this.mediaSourceFactorySuppliers.clear();
            this.mediaSourceFactories.clear();
            return;
        }

        public void setDrmSessionManagerProvider(DrmSessionManagerProvider r3) {
            this.drmSessionManagerProvider = r3;
            Iterator<MediaSource.Factory> r0 = this.mediaSourceFactories.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            r0.next().setDrmSessionManagerProvider(r3);
            goto L4
        }

        public void setJpegExtractorFlags(int r2) {
            if ((this.extractorsFactory instanceof DefaultExtractorsFactory) == false) goto L6;
            ((DefaultExtractorsFactory) this.extractorsFactory).setJpegExtractorFlags(r2);
            return;
        }

        public void setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r3) {
            this.loadErrorHandlingPolicy = r3;
            Iterator<MediaSource.Factory> r0 = this.mediaSourceFactories.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            r0.next().setLoadErrorHandlingPolicy(r3);
            goto L4
        }

        public void setParseSubtitlesDuringExtraction(boolean r3) {
            this.parseSubtitlesDuringExtraction = r3;
            this.extractorsFactory.experimentalSetTextTrackTranscodingEnabled(r3);
            Iterator<MediaSource.Factory> r0 = this.mediaSourceFactories.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            r0.next().experimentalParseSubtitlesDuringExtraction(r3);
            goto L4
        }

        public void setSubtitleParserFactory(SubtitleParser.Factory r3) {
            this.subtitleParserFactory = r3;
            this.extractorsFactory.setSubtitleParserFactory(r3);
            Iterator<MediaSource.Factory> r0 = this.mediaSourceFactories.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            r0.next().setSubtitleParserFactory(r3);
            goto L4
        }
    }

    private static final class UnknownSubtitlesExtractor implements Extractor {
        private final Format format;

        public UnknownSubtitlesExtractor(Format r1) {
            this.format = r1;
        }

        @Override // androidx.media3.extractor.Extractor
        public /* synthetic */ List getSniffFailureDetails() {
            return Extractor.CC.$default$getSniffFailureDetails(this);
        }

        @Override // androidx.media3.extractor.Extractor
        public /* synthetic */ Extractor getUnderlyingImplementation() {
            return Extractor.CC.$default$getUnderlyingImplementation(this);
        }

        @Override // androidx.media3.extractor.Extractor
        public void init(ExtractorOutput r5) {
            TrackOutput r0 = r5.track(0, 3);
            r5.seekMap(new SeekMap.Unseekable(C0180C.TIME_UNSET));
            r5.endTracks();
            r0.format(this.format.buildUpon().setSampleMimeType(MimeTypes.TEXT_UNKNOWN).setCodecs(this.format.sampleMimeType).build());
        }

        @Override // androidx.media3.extractor.Extractor
        public int read(ExtractorInput r3, PositionHolder r4) throws IOException {
            if (r3.skip(Integer.MAX_VALUE) != (-1)) goto L5;
            return -1;
        L5:
            return 0;
        }

        @Override // androidx.media3.extractor.Extractor
        public void release() {
        }

        @Override // androidx.media3.extractor.Extractor
        public void seek(long r1, long r3) {
        }

        @Override // androidx.media3.extractor.Extractor
        public boolean sniff(ExtractorInput r2) {
            return true;
        }
    }

    public DefaultMediaSourceFactory(Context r2) {
        this(new DefaultDataSource.Factory(r2));
    }

    public DefaultMediaSourceFactory(Context r2, ExtractorsFactory r3) {
        this(new DefaultDataSource.Factory(r2), r3);
    }

    public DefaultMediaSourceFactory(DataSource.Factory r2) {
        this(r2, new DefaultExtractorsFactory());
    }

    public DefaultMediaSourceFactory(DataSource.Factory r3, ExtractorsFactory r4) {
        this.dataSourceFactory = r3;
        this.subtitleParserFactory = new DefaultSubtitleParserFactory();
        this.delegateFactoryLoader = new DelegateFactoryLoader(r4, this.subtitleParserFactory);
        this.delegateFactoryLoader.setDataSourceFactory(r3);
        this.liveTargetOffsetMs = C0180C.TIME_UNSET;
        this.liveMinOffsetMs = C0180C.TIME_UNSET;
        this.liveMaxOffsetMs = C0180C.TIME_UNSET;
        this.liveMinSpeed = -3.4028235E38f;
        this.liveMaxSpeed = -3.4028235E38f;
        this.parseSubtitlesDuringExtraction = true;
    }

    static /* synthetic */ MediaSource.Factory access$000(Class r1) {
        return newInstance(r1);
    }

    static /* synthetic */ MediaSource.Factory access$100(Class r1, DataSource.Factory r2) {
        return newInstance(r1, r2);
    }

    private static MediaSource maybeClipMediaSource(MediaItem r10, MediaSource r11) {
        if (r10.clippingConfiguration.startPositionUs != 0) goto L10;
        if (r10.clippingConfiguration.endPositionUs != Long.MIN_VALUE) goto L10;
        if (r10.clippingConfiguration.relativeToDefaultPosition == true) goto L10;
        return r11;
    L10:
        return new ClippingMediaSource(r11, r10.clippingConfiguration.startPositionUs, r10.clippingConfiguration.endPositionUs, !r10.clippingConfiguration.startsAtKeyFrame, r10.clippingConfiguration.relativeToLiveWindow, r10.clippingConfiguration.relativeToDefaultPosition);
    }

    private MediaSource maybeWrapWithAdsMediaSource(MediaItem r10, MediaSource r11) {
        Assertions.checkNotNull(r10.localConfiguration);
        MediaItem.AdsConfiguration r0 = r10.localConfiguration.adsConfiguration;
        if (r0 != null) goto L5;
        return r11;
    L5:
        AdsLoader.Provider r1 = this.adsLoaderProvider;
        AdViewProvider r8 = this.adViewProvider;
        if (r1 == null) goto L20;
        if (r8 == null) goto L20;
        AdsLoader r7 = r1.getAdsLoader(r0);
        if (r7 != null) goto L13;
        Log.m48w(TAG, "Playing media without ads, as no AdsLoader was provided.");
        return r11;
    L13:
        DataSpec r4 = new DataSpec(r0.adTagUri);
        if (r0.adsId == null) goto L16;
        Object r5 = r0.adsId;
    L18:
        return new AdsMediaSource(r11, r4, r5, this, r7, r8);
    L16:
        r5 = ImmutableList.m215of(r10.mediaId, r10.localConfiguration.uri, r0.adTagUri);
    L20:
        Log.m48w(TAG, "Playing media without ads. Configure ad support by calling setAdsLoaderProvider and setAdViewProvider.");
        return r11;
    }

    private static MediaSource.Factory newInstance(Class<? extends MediaSource.Factory> r2) {
        return r2.getConstructor(new Class[0]).newInstance(new Object[0]);
    L5:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    private static MediaSource.Factory newInstance(Class<? extends MediaSource.Factory> r4, DataSource.Factory r5) {
        return r4.getConstructor(new Class[]{DataSource.Factory.class}).newInstance(new Object[]{r5});
    L5:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public DefaultMediaSourceFactory clearLocalAdInsertionComponents() {
        this.adsLoaderProvider = null;
        this.adViewProvider = null;
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public MediaSource createMediaSource(MediaItem r18) {
        MediaItem r2 = r18;
        Assertions.checkNotNull(r2.localConfiguration);
        String r3 = r2.localConfiguration.uri.getScheme();
        if (r3 == null) goto L9;
        if (r3.equals(C0180C.SSAI_SCHEME) == false) goto L9;
        return ((MediaSource.Factory) Assertions.checkNotNull(this.serverSideAdInsertionMediaSourceFactory)).createMediaSource(r2);
    L9:
        if (Objects.equals(r2.localConfiguration.mimeType, MimeTypes.APPLICATION_EXTERNALLY_LOADED_IMAGE) == true) goto L11;
        int r4 = Util.inferContentTypeForUriAndMimeType(r2.localConfiguration.uri, r2.localConfiguration.mimeType);
        boolean r5 = true;
        if (r2.localConfiguration.imageDurationMs == C0180C.TIME_UNSET) goto L56;
        this.delegateFactoryLoader.setJpegExtractorFlags(1);
    L56:
        MediaSource.Factory r0 = this.delegateFactoryLoader.getMediaSourceFactory(r4);     // Catch: ClassNotFoundException -> L53
        MediaItem.LiveConfiguration.Builder r6 = r2.liveConfiguration.buildUpon();
        if (r2.liveConfiguration.targetOffsetMs != C0180C.TIME_UNSET) goto L20;
        r6.setTargetOffsetMs(this.liveTargetOffsetMs);
    L20:
        if (r2.liveConfiguration.minPlaybackSpeed != (-3.4028235E38f)) goto L23;
        r6.setMinPlaybackSpeed(this.liveMinSpeed);
    L23:
        if (r2.liveConfiguration.maxPlaybackSpeed != (-3.4028235E38f)) goto L26;
        r6.setMaxPlaybackSpeed(this.liveMaxSpeed);
    L26:
        if (r2.liveConfiguration.minOffsetMs != C0180C.TIME_UNSET) goto L29;
        r6.setMinOffsetMs(this.liveMinOffsetMs);
    L29:
        if (r2.liveConfiguration.maxOffsetMs != C0180C.TIME_UNSET) goto L31;
        r6.setMaxOffsetMs(this.liveMaxOffsetMs);
    L31:
        MediaItem.LiveConfiguration r9 = r6.build();
        if (r9.equals(r2.liveConfiguration) == true) goto L34;
        r2 = r2.buildUpon().setLiveConfiguration(r9).build();
    L34:
        MediaSource r10 = r0.createMediaSource(r2);
        ImmutableList<MediaItem.SubtitleConfiguration> r11 = ((MediaItem.LocalConfiguration) Util.castNonNull(r2.localConfiguration)).subtitleConfigurations;
        if (r11.isEmpty() == true) goto L52;
        MediaSource[] r12 = new MediaSource[r11.size() + 1];
        r12[0] = r10;
        int r13 = 0;
    L38:
        if (r13 >= r11.size()) goto L50;
        if (this.parseSubtitlesDuringExtraction == false) goto L45;
        final Format r14 = new Format.Builder().setSampleMimeType(r11.get(r13).mimeType).setLanguage(r11.get(r13).language).setSelectionFlags(r11.get(r13).selectionFlags).setRoleFlags(r11.get(r13).roleFlags).setLabel(r11.get(r13).label).setId(r11.get(r13).f28id).build();
        ExtractorsFactory r15 = new DefaultMediaSourceFactory$$ExternalSyntheticLambda0(this, r14);
        ProgressiveMediaSource.Factory r7 = new ProgressiveMediaSource.Factory(this.dataSourceFactory, r15).setSuppressPrepareError(r5);
        if (this.loadErrorHandlingPolicy == null) goto L44;
        r7.setLoadErrorHandlingPolicy(this.loadErrorHandlingPolicy);
    L44:
        r12[r13 + 1] = r7.createMediaSource(MediaItem.fromUri(r11.get(r13).uri.toString()));
        long r16 = C0180C.TIME_UNSET;
    L49:
        r13 = r13 + 1;
        r5 = true;
        goto L38
    L45:
        SingleSampleMediaSource.Factory r8 = new SingleSampleMediaSource.Factory(this.dataSourceFactory);
        if (this.loadErrorHandlingPolicy == null) goto L48;
        r8.setLoadErrorHandlingPolicy(this.loadErrorHandlingPolicy);
    L48:
        MediaItem.SubtitleConfiguration r17 = r11.get(r13);
        r16 = C0180C.TIME_UNSET;
        r12[r13 + 1] = r8.createMediaSource(r17, C0180C.TIME_UNSET);
        goto L49
    L50:
        r10 = new MergingMediaSource(r12);
    L52:
        return maybeWrapWithAdsMediaSource(r2, maybeClipMediaSource(r2, r10));
    L53:
        e = move-exception;
        throw new IllegalStateException(e);
    L11:
        return new ExternallyLoadedMediaSource.Factory(Util.msToUs(r2.localConfiguration.imageDurationMs), (ExternalLoader) Assertions.checkNotNull(this.externalImageLoader)).createMediaSource(r2);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    @Deprecated
    public DefaultMediaSourceFactory experimentalParseSubtitlesDuringExtraction(boolean r2) {
        this.parseSubtitlesDuringExtraction = r2;
        this.delegateFactoryLoader.setParseSubtitlesDuringExtraction(r2);
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    @Deprecated
    public /* bridge */ /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
        return experimentalParseSubtitlesDuringExtraction(r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public int[] getSupportedTypes() {
        return this.delegateFactoryLoader.getSupportedTypes();
    }

    /* JADX INFO: renamed from: lambda$createMediaSource$0$androidx-media3-exoplayer-source-DefaultMediaSourceFactory */
    /* synthetic */ Extractor[] m120xeef04c56(Format r4) {
        Extractor[] r0 = new Extractor[1];
        if (this.subtitleParserFactory.supportsFormat(r4) == false) goto L5;
        Extractor r1 = new SubtitleExtractor(this.subtitleParserFactory.create(r4), r4);
    L6:
        r0[0] = r1;
        return r0;
    L5:
        r1 = new UnknownSubtitlesExtractor(r4);
        goto L6
    }

    @Deprecated
    public DefaultMediaSourceFactory setAdViewProvider(AdViewProvider r1) {
        this.adViewProvider = r1;
        return this;
    }

    @Deprecated
    public DefaultMediaSourceFactory setAdsLoaderProvider(AdsLoader.Provider r1) {
        this.adsLoaderProvider = r1;
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public DefaultMediaSourceFactory setCmcdConfigurationFactory(CmcdConfiguration.Factory r3) {
        this.delegateFactoryLoader.setCmcdConfigurationFactory((CmcdConfiguration.Factory) Assertions.checkNotNull(r3));
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* bridge */ /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
        return setCmcdConfigurationFactory(r1);
    }

    public DefaultMediaSourceFactory setDataSourceFactory(DataSource.Factory r2) {
        this.dataSourceFactory = r2;
        this.delegateFactoryLoader.setDataSourceFactory(r2);
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public DefaultMediaSourceFactory setDrmSessionManagerProvider(DrmSessionManagerProvider r3) {
        this.delegateFactoryLoader.setDrmSessionManagerProvider((DrmSessionManagerProvider) Assertions.checkNotNull(r3, "MediaSource.Factory#setDrmSessionManagerProvider no longer handles null by instantiating a new DefaultDrmSessionManagerProvider. Explicitly construct and pass an instance in order to retain the old behavior."));
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* bridge */ /* synthetic */ MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
        return setDrmSessionManagerProvider(r1);
    }

    public DefaultMediaSourceFactory setExternalImageLoader(ExternalLoader r1) {
        this.externalImageLoader = r1;
        return this;
    }

    public DefaultMediaSourceFactory setLiveMaxOffsetMs(long r1) {
        this.liveMaxOffsetMs = r1;
        return this;
    }

    public DefaultMediaSourceFactory setLiveMaxSpeed(float r1) {
        this.liveMaxSpeed = r1;
        return this;
    }

    public DefaultMediaSourceFactory setLiveMinOffsetMs(long r1) {
        this.liveMinOffsetMs = r1;
        return this;
    }

    public DefaultMediaSourceFactory setLiveMinSpeed(float r1) {
        this.liveMinSpeed = r1;
        return this;
    }

    public DefaultMediaSourceFactory setLiveTargetOffsetMs(long r1) {
        this.liveTargetOffsetMs = r1;
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public DefaultMediaSourceFactory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
        this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(r2, "MediaSource.Factory#setLoadErrorHandlingPolicy no longer handles null by instantiating a new DefaultLoadErrorHandlingPolicy. Explicitly construct and pass an instance in order to retain the old behavior.");
        this.delegateFactoryLoader.setLoadErrorHandlingPolicy(r2);
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* bridge */ /* synthetic */ MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
        return setLoadErrorHandlingPolicy(r1);
    }

    public DefaultMediaSourceFactory setLocalAdInsertionComponents(AdsLoader.Provider r2, AdViewProvider r3) {
        this.adsLoaderProvider = (AdsLoader.Provider) Assertions.checkNotNull(r2);
        this.adViewProvider = (AdViewProvider) Assertions.checkNotNull(r3);
        return this;
    }

    public DefaultMediaSourceFactory setServerSideAdInsertionMediaSourceFactory(MediaSource.Factory r1) {
        this.serverSideAdInsertionMediaSourceFactory = r1;
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public DefaultMediaSourceFactory setSubtitleParserFactory(SubtitleParser.Factory r2) {
        this.subtitleParserFactory = (SubtitleParser.Factory) Assertions.checkNotNull(r2);
        this.delegateFactoryLoader.setSubtitleParserFactory(r2);
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* bridge */ /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
        return setSubtitleParserFactory(r1);
    }
}
