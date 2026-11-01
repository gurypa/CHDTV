package androidx.media3.exoplayer.hls.offline;

import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UriUtil;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.dash.offline.DashDownloader$$ExternalSyntheticLambda0;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistParser;
import androidx.media3.exoplayer.offline.FilterableManifest;
import androidx.media3.exoplayer.offline.SegmentDownloader;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class HlsDownloader extends SegmentDownloader<HlsPlaylist> {
    public HlsDownloader(MediaItem r2, CacheDataSource.Factory r3) {
        this(r2, r3, new DashDownloader$$ExternalSyntheticLambda0());
    }

    public HlsDownloader(MediaItem r8, CacheDataSource.Factory r9, Executor r10) {
        this(r8, new HlsPlaylistParser(), r9, r10, 20000);
    }

    @Deprecated
    public HlsDownloader(MediaItem r8, ParsingLoadable.Parser<HlsPlaylist> r9, CacheDataSource.Factory r10, Executor r11) {
        this(r8, r9, r10, r11, 20000);
    }

    public HlsDownloader(MediaItem r1, ParsingLoadable.Parser<HlsPlaylist> r2, CacheDataSource.Factory r3, Executor r4, long r5) {
        super(r1, r2, r3, r4, r5);
    }

    private void addMediaPlaylistDataSpecs(List<Uri> r3, List<DataSpec> r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L6;
        r4.add(SegmentDownloader.getCompressibleDataSpec(r3.get(r0)));
        r0 = r0 + 1;
        goto L4
    }

    private void addSegment(HlsMediaPlaylist r11, HlsMediaPlaylist.Segment r12, HashSet<Uri> r13, ArrayList<SegmentDownloader.Segment> r14) {
        String r0 = r11.baseUri;
        long r1 = r11.startTimeUs + r12.relativeStartTimeUs;
        if (r12.fullSegmentEncryptionKeyUri == null) goto L7;
        Uri r3 = UriUtil.resolveToUri(r0, r12.fullSegmentEncryptionKeyUri);
        if (r13.add(r3) == false) goto L7;
        r14.add(new SegmentDownloader.Segment(r1, SegmentDownloader.getCompressibleDataSpec(r3)));
    L7:
        r14.add(new SegmentDownloader.Segment(r1, new DataSpec(UriUtil.resolveToUri(r0, r12.url), r12.byteRangeOffset, r12.byteRangeLength)));
    }

    protected List<SegmentDownloader.Segment> getSegments(DataSource r12, HlsPlaylist r13, boolean r14) throws IOException, InterruptedException {
        ArrayList r0 = new ArrayList();
        if ((r13 instanceof HlsMultivariantPlaylist) == false) goto L5;
        addMediaPlaylistDataSpecs(((HlsMultivariantPlaylist) r13).mediaPlaylistUrls, r0);
    L6:
        ArrayList<SegmentDownloader.Segment> r1 = new ArrayList();
        HashSet<Uri> r2 = new HashSet();
        Iterator r3 = r0.iterator();
    L8:
        if (r3.hasNext() == false) goto L24;
        DataSpec r4 = (DataSpec) r3.next();
        r1.add(new SegmentDownloader.Segment(0, r4));
        HlsMediaPlaylist r5 = (HlsMediaPlaylist) getManifest(r12, r4, r14);     // Catch: IOException -> L20
        HlsMediaPlaylist.Segment r6 = null;
        List<HlsMediaPlaylist.Segment> r7 = r5.segments;
        int r8 = 0;
    L13:
        if (r8 >= r7.size()) goto L8;
        HlsMediaPlaylist.Segment r9 = r7.get(r8);
        HlsMediaPlaylist.Segment r10 = r9.initializationSegment;
        if (r10 == null) goto L18;
        if (r10 == r6) goto L18;
        r6 = r10;
        addSegment(r5, r10, r2, r1);
    L18:
        addSegment(r5, r9, r2, r1);
        r8 = r8 + 1;
    L20:
        e = move-exception;
        if (r14 == true) goto L8;
        throw e;
    L24:
        return r1;
    L5:
        r0.add(SegmentDownloader.getCompressibleDataSpec(Uri.parse(r13.baseUri)));
        goto L6
    }

    @Override // androidx.media3.exoplayer.offline.SegmentDownloader
    protected /* bridge */ /* synthetic */ List getSegments(DataSource r1, FilterableManifest r2, boolean r3) throws IOException, InterruptedException {
        return getSegments(r1, (HlsPlaylist) r2, r3);
    }
}
