package androidx.media3.exoplayer.dash.offline;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.dash.BaseUrlExclusionList;
import androidx.media3.exoplayer.dash.DashSegmentIndex;
import androidx.media3.exoplayer.dash.DashUtil;
import androidx.media3.exoplayer.dash.DashWrappingSegmentIndex;
import androidx.media3.exoplayer.dash.manifest.AdaptationSet;
import androidx.media3.exoplayer.dash.manifest.BaseUrl;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.dash.manifest.DashManifestParser;
import androidx.media3.exoplayer.dash.manifest.Period;
import androidx.media3.exoplayer.dash.manifest.RangedUri;
import androidx.media3.exoplayer.dash.manifest.Representation;
import androidx.media3.exoplayer.offline.DownloadException;
import androidx.media3.exoplayer.offline.FilterableManifest;
import androidx.media3.exoplayer.offline.SegmentDownloader;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.ChunkIndex;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class DashDownloader extends SegmentDownloader<DashManifest> {
    private final BaseUrlExclusionList baseUrlExclusionList;

    public DashDownloader(MediaItem r2, CacheDataSource.Factory r3) {
        this(r2, r3, new DashDownloader$$ExternalSyntheticLambda0());
    }

    public DashDownloader(MediaItem r8, CacheDataSource.Factory r9, Executor r10) {
        this(r8, new DashManifestParser(), r9, r10, 20000);
    }

    @Deprecated
    public DashDownloader(MediaItem r8, ParsingLoadable.Parser<DashManifest> r9, CacheDataSource.Factory r10, Executor r11) {
        this(r8, r9, r10, r11, 20000);
    }

    public DashDownloader(MediaItem r3, ParsingLoadable.Parser<DashManifest> r4, CacheDataSource.Factory r5, Executor r6, long r7) {
        super(r3, r4, r5, r6, r7);
        this.baseUrlExclusionList = new BaseUrlExclusionList();
    }

    private void addSegmentsForAdaptationSet(DataSource r29, AdaptationSet r30, long r31, long r33, boolean r35, ArrayList<SegmentDownloader.Segment> r36) throws IOException, InterruptedException {
        int r10 = 0;
    L4:
        if (r10 >= r30.representations.size()) goto L41;
        Representation r2 = r30.representations.get(r10);
        DashSegmentIndex r0 = getSegmentIndex(r29, r30.type, r2, r35);     // Catch: IOException -> L32
        if (r0 == null) goto L46;
        long r14 = r0.getSegmentCount(r33);
        if (r14 == (-1)) goto L26;
        String r3 = ((BaseUrl) Util.castNonNull(this.baseUrlExclusionList.selectBaseUrl(r2.baseUrls))).url;
        RangedUri r6 = r2.getInitializationUri();
        if (r6 == null) goto L16;
        r36.add(createSegment(r2, r3, r31, r6));
    L16:
        RangedUri r7 = r2.getIndexUri();
        if (r7 == null) goto L20;
        r36.add(createSegment(r2, r3, r31, r7));
    L20:
        long r18 = r0.getFirstSegmentNum();
        long r22 = (r18 + r14) - 1;
        long r4 = r18;
    L22:
        if (r4 > r22) goto L38;
        r36.add(createSegment(r2, r3, r31 + r0.getTimeUs(r4), r0.getSegmentUrl(r4)));
        r4 = r4 + 1;
    L38:
        r10 = r10 + 1;
        goto L4
    L26:
        throw new DownloadException("Unbounded segment index");
    L46:
    L30:
        e = e;
    L37:
        if (r35 == true) goto L38;
        throw e;
    L29:
        throw new DownloadException("Missing segment index");     // Catch: IOException -> L30
    L32:
        e = e;
    L34:
        e = e;
        goto L37
    }

    private SegmentDownloader.Segment createSegment(Representation r3, String r4, long r5, RangedUri r7) {
        return new SegmentDownloader.Segment(r5, DashUtil.buildDataSpec(r3, r4, r7, 0, ImmutableMap.m231of()));
    }

    private DashSegmentIndex getSegmentIndex(final DataSource r7, final int r8, final Representation r9, boolean r10) throws IOException, InterruptedException {
        DashSegmentIndex r0 = r9.getIndex();
        if (r0 == null) goto L5;
        return r0;
    L5:
        ChunkIndex r2 = (ChunkIndex) execute(new C02771(this, r7, r8, r9), r10);
        if (r2 != null) goto L9;
        return null;
    L9:
        return new DashWrappingSegmentIndex(r2, r9.presentationTimeOffsetUs);
    }

    protected List<SegmentDownloader.Segment> getSegments(DataSource r15, DashManifest r16, boolean r17) throws IOException, InterruptedException {
        ArrayList<SegmentDownloader.Segment> r1 = new ArrayList();
        int r2 = 0;
    L4:
        if (r2 >= r16.getPeriodCount()) goto L10;
        Period r11 = r16.getPeriod(r2);
        long r5 = Util.msToUs(r11.startMs);
        long r7 = r16.getPeriodDurationUs(r2);
        List<AdaptationSet> r12 = r11.adaptationSets;
        int r13 = 0;
    L7:
        if (r13 >= r12.size()) goto L9;
        addSegmentsForAdaptationSet(r15, r12.get(r13), r5, r7, r17, r1);
        r13 = r13 + 1;
        goto L7
    L9:
        r2 = r2 + 1;
        goto L4
    L10:
        return r1;
    }

    @Override // androidx.media3.exoplayer.offline.SegmentDownloader
    protected /* bridge */ /* synthetic */ List getSegments(DataSource r1, FilterableManifest r2, boolean r3) throws IOException, InterruptedException {
        return getSegments(r1, (DashManifest) r2, r3);
    }
}
