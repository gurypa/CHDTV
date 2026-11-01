package androidx.media3.exoplayer.dash;

import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.dash.manifest.DashManifestParser;
import androidx.media3.exoplayer.dash.manifest.Period;
import androidx.media3.exoplayer.dash.manifest.RangedUri;
import androidx.media3.exoplayer.dash.manifest.Representation;
import androidx.media3.exoplayer.source.chunk.BundledChunkExtractor;
import androidx.media3.exoplayer.source.chunk.ChunkExtractor;
import androidx.media3.exoplayer.source.chunk.InitializationChunk;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.mkv.MatroskaExtractor;
import androidx.media3.extractor.mp4.FragmentedMp4Extractor;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DashUtil {
    private DashUtil() {
    }

    @Deprecated
    public static DataSpec buildDataSpec(Representation r2, RangedUri r3, int r4) {
        return buildDataSpec(r2, r2.baseUrls.get(0).url, r3, r4, ImmutableMap.m231of());
    }

    @Deprecated
    public static DataSpec buildDataSpec(Representation r1, String r2, RangedUri r3, int r4) {
        return buildDataSpec(r1, r2, r3, r4, ImmutableMap.m231of());
    }

    public static DataSpec buildDataSpec(Representation r3, String r4, RangedUri r5, int r6, Map<String, String> r7) {
        return new DataSpec.Builder().setUri(r5.resolveUri(r4)).setPosition(r5.start).setLength(r5.length).setKey(resolveCacheKey(r3, r5)).setFlags(r6).setHttpRequestHeaders(r7).build();
    }

    private static Representation getFirstRepresentation(Period r4, int r5) {
        int r0 = r4.getAdaptationSetIndex(r5);
        if (r0 != (-1)) goto L5;
        return null;
    L5:
        List<Representation> r1 = r4.adaptationSets.get(r0).representations;
        if (r1.isEmpty() == false) goto L9;
        return null;
    L9:
        return r1.get(0);
    }

    public static ChunkIndex loadChunkIndex(DataSource r1, int r2, Representation r3) throws IOException {
        return loadChunkIndex(r1, r2, r3, 0);
    }

    public static ChunkIndex loadChunkIndex(DataSource r2, int r3, Representation r4, int r5) throws IOException {
        if (r4.getInitializationUri() != null) goto L6;
        return null;
    L6:
        ChunkExtractor r0 = newChunkExtractor(r3, r4.format);
        loadInitializationData(r0, r2, r4, r5, true);     // Catch: Throwable -> L10
        r0.release();
        return r0.getChunkIndex();
    L10:
        th = move-exception;
        r0.release();
        throw th;
    }

    public static Format loadFormatWithDrmInitData(DataSource r5, Period r6) throws IOException {
        int r0 = 2;
        Representation r1 = getFirstRepresentation(r6, 2);
        if (r1 != null) goto L8;
        r0 = 1;
        r1 = getFirstRepresentation(r6, 1);
        if (r1 != null) goto L8;
        return null;
    L8:
        Format r2 = r1.format;
        Format r3 = loadSampleFormat(r5, r0, r1);
        if (r3 != null) goto L12;
        return r2;
    L12:
        return r3.withManifestFormatInfo(r2);
    }

    private static void loadInitializationData(DataSource r10, Representation r11, int r12, ChunkExtractor r13, RangedUri r14) throws IOException {
        new InitializationChunk(r10, buildDataSpec(r11, r11.baseUrls.get(r12).url, r14, 0, ImmutableMap.m231of()), r11.format, 0, null, r13).load();
    }

    private static void loadInitializationData(ChunkExtractor r3, DataSource r4, Representation r5, int r6, boolean r7) throws IOException {
        RangedUri r0 = (RangedUri) Assertions.checkNotNull(r5.getInitializationUri());
        if (r7 == false) goto L11;
        RangedUri r1 = r5.getIndexUri();
        if (r1 != null) goto L7;
        return;
    L7:
        RangedUri r2 = r0.attemptMerge(r1, r5.baseUrls.get(r6).url);
        if (r2 != null) goto L12;
        loadInitializationData(r4, r5, r6, r3, r0);
        r2 = r1;
    L12:
        loadInitializationData(r4, r5, r6, r3, r2);
        return;
    L11:
        r2 = r0;
        goto L12
    }

    public static void loadInitializationData(ChunkExtractor r1, DataSource r2, Representation r3, boolean r4) throws IOException {
        loadInitializationData(r1, r2, r3, 0, r4);
    }

    public static DashManifest loadManifest(DataSource r2, Uri r3) throws IOException {
        return (DashManifest) ParsingLoadable.load(r2, new DashManifestParser(), r3, 4);
    }

    public static Format loadSampleFormat(DataSource r1, int r2, Representation r3) throws IOException {
        return loadSampleFormat(r1, r2, r3, 0);
    }

    public static Format loadSampleFormat(DataSource r3, int r4, Representation r5, int r6) throws IOException {
        if (r5.getInitializationUri() != null) goto L6;
        return null;
    L6:
        ChunkExtractor r0 = newChunkExtractor(r4, r5.format);
        loadInitializationData(r0, r3, r5, r6, false);     // Catch: Throwable -> L10
        r0.release();
        return ((Format[]) Assertions.checkStateNotNull(r0.getSampleFormats()))[0];
    L10:
        th = move-exception;
        r0.release();
        throw th;
    }

    private static ChunkExtractor newChunkExtractor(int r5, Format r6) {
        String r0 = r6.containerMimeType;
        if (r0 != null) goto L5;
    L9:
        boolean r1 = false;
    L10:
        if (r1 == false) goto L12;
        Extractor r2 = new MatroskaExtractor(SubtitleParser.Factory.UNSUPPORTED, 2);
    L14:
        return new BundledChunkExtractor(r2, r5, r6);
    L12:
        r2 = new FragmentedMp4Extractor(SubtitleParser.Factory.UNSUPPORTED, 32);
        goto L14
    L5:
        if (r0.startsWith(MimeTypes.VIDEO_WEBM) == false) goto L7;
    L8:
        r1 = true;
        goto L10
    L7:
        if (r0.startsWith(MimeTypes.AUDIO_WEBM) == false) goto L9;
        goto L8
    }

    public static String resolveCacheKey(Representation r3, RangedUri r4) {
        String r0 = r3.getCacheKey();
        if (r0 == null) goto L6;
        return r0;
    L6:
        return r4.resolveUri(r3.baseUrls.get(0).url).toString();
    }
}
