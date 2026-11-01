package androidx.media3.exoplayer.smoothstreaming.offline;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.dash.offline.DashDownloader$$ExternalSyntheticLambda0;
import androidx.media3.exoplayer.offline.FilterableManifest;
import androidx.media3.exoplayer.offline.SegmentDownloader;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class SsDownloader extends SegmentDownloader<SsManifest> {
    public SsDownloader(MediaItem r2, CacheDataSource.Factory r3) {
        this(r2, r3, new DashDownloader$$ExternalSyntheticLambda0());
    }

    public SsDownloader(MediaItem r9, CacheDataSource.Factory r10, Executor r11) {
        this(r9.buildUpon().setUri(Util.fixSmoothStreamingIsmManifestUri(((MediaItem.LocalConfiguration) Assertions.checkNotNull(r9.localConfiguration)).uri)).build(), new SsManifestParser(), r10, r11, 20000);
    }

    @Deprecated
    public SsDownloader(MediaItem r8, ParsingLoadable.Parser<SsManifest> r9, CacheDataSource.Factory r10, Executor r11) {
        this(r8, r9, r10, r11, 20000);
    }

    public SsDownloader(MediaItem r1, ParsingLoadable.Parser<SsManifest> r2, CacheDataSource.Factory r3, Executor r4, long r5) {
        super(r1, r2, r3, r4, r5);
    }

    @Override // androidx.media3.exoplayer.offline.SegmentDownloader
    protected /* bridge */ /* synthetic */ List getSegments(DataSource r1, FilterableManifest r2, boolean r3) throws IOException, InterruptedException {
        return getSegments(r1, (SsManifest) r2, r3);
    }

    protected List<SegmentDownloader.Segment> getSegments(DataSource r13, SsManifest r14, boolean r15) {
        ArrayList r0 = new ArrayList();
        SsManifest.StreamElement[] r1 = r14.streamElements;
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L13;
        SsManifest.StreamElement r4 = r1[r3];
        int r5 = 0;
    L6:
        if (r5 >= r4.formats.length) goto L12;
        int r6 = 0;
    L9:
        if (r6 >= r4.chunkCount) goto L11;
        r0.add(new SegmentDownloader.Segment(r4.getStartTimeUs(r6), new DataSpec(r4.buildRequestUri(r5, r6))));
        r6 = r6 + 1;
        goto L9
    L11:
        r5 = r5 + 1;
        goto L6
    L12:
        r3 = r3 + 1;
        goto L3
    L13:
        return r0;
    }
}
