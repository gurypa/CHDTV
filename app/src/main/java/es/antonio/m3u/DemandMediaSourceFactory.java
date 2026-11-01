package es.antonio.m3u;

import android.content.Context;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.dash.DashMediaSource;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.upstream.CmcdConfiguration;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.extractor.text.SubtitleParser;

/* JADX INFO: loaded from: classes7.dex */
public final class DemandMediaSourceFactory implements MediaSource.Factory {
    private final DashMediaSource.Factory dash;
    private final DefaultMediaSourceFactory original;
    private final boolean vod;

    private DemandMediaSourceFactory(Context r3, DataSource.Factory r4) {
        this.original = new DefaultMediaSourceFactory(r4);
        DashMediaSource.Factory r1 = null;
        if ((r3 instanceof PlayerActivity) == false) goto L5;
        Entry r5 = ((PlayerActivity) r3).tvCurrent();
    L6:
        if (r5 != null) goto L8;
    L10:
        boolean r0 = false;
    L11:
        this.vod = r0;
        if (this.vod == false) goto L14;
        r1 = new DashMediaSource.Factory(r4).setManifestParser(new DemandDashParser(foldSeries(r5.kind)));
    L14:
        this.dash = r1;
        return;
    L8:
        if (isVod(r5.kind) == false) goto L10;
        r0 = true;
        goto L11
    L5:
        r5 = null;
        goto L6
    }

    static boolean compactRoute(boolean r0, int r1) {
        if (r0 == false) goto L6;
        if (r1 != 0) goto L8;
        return true;
    L8:
        return false;
    L6:
        return false;
    }

    public static DemandMediaSourceFactory create(Context r1, DataSource.Factory r2) {
        return new DemandMediaSourceFactory(r1, r2);
    }

    static boolean foldSeries(String r1) {
        return "Series".equals(r1);
    }

    static boolean isVod(String r1) {
        if ("Películas".equals(r1) == false) goto L5;
        return true;
    L5:
        if ("Series".equals(r1) == true) goto L10;
        return false;
    L10:
        return true;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public MediaSource createMediaSource(MediaItem r4) {
        if (r4.localConfiguration == null) goto L9;
        if (compactRoute(this.vod, Util.inferContentTypeForUriAndMimeType(r4.localConfiguration.uri, r4.localConfiguration.mimeType)) == false) goto L9;
        return this.dash.createMediaSource(r4);
    L9:
        return this.original.createMediaSource(r4);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* synthetic */ MediaSource.Factory experimentalParseSubtitlesDuringExtraction(boolean r1) {
        return MediaSource.Factory.CC.$default$experimentalParseSubtitlesDuringExtraction(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public int[] getSupportedTypes() {
        return this.original.getSupportedTypes();
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* synthetic */ MediaSource.Factory setCmcdConfigurationFactory(CmcdConfiguration.Factory r1) {
        return MediaSource.Factory.CC.$default$setCmcdConfigurationFactory(this, r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* bridge */ /* synthetic */ MediaSource.Factory setDrmSessionManagerProvider(DrmSessionManagerProvider r1) {
        return setDrmSessionManagerProvider(r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public DemandMediaSourceFactory setDrmSessionManagerProvider(DrmSessionManagerProvider r2) {
        this.original.setDrmSessionManagerProvider(r2);
        if (this.dash == null) goto L5;
        this.dash.setDrmSessionManagerProvider(r2);
    L5:
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* bridge */ /* synthetic */ MediaSource.Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r1) {
        return setLoadErrorHandlingPolicy(r1);
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public DemandMediaSourceFactory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
        this.original.setLoadErrorHandlingPolicy(r2);
        if (this.dash == null) goto L5;
        this.dash.setLoadErrorHandlingPolicy(r2);
    L5:
        return this;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource.Factory
    public /* synthetic */ MediaSource.Factory setSubtitleParserFactory(SubtitleParser.Factory r1) {
        return MediaSource.Factory.CC.$default$setSubtitleParserFactory(this, r1);
    }
}
