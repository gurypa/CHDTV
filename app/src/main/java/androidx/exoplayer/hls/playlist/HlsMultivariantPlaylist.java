package androidx.media3.exoplayer.hls.playlist;

import android.net.Uri;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class HlsMultivariantPlaylist extends HlsPlaylist {
    public static final HlsMultivariantPlaylist EMPTY = null;
    public static final int GROUP_INDEX_AUDIO = 1;
    public static final int GROUP_INDEX_SUBTITLE = 2;
    public static final int GROUP_INDEX_VARIANT = 0;
    public final List<Rendition> audios;
    public final List<Rendition> closedCaptions;
    public final List<Uri> mediaPlaylistUrls;
    public final Format muxedAudioFormat;
    public final List<Format> muxedCaptionFormats;
    public final List<DrmInitData> sessionKeyDrmInitData;
    public final List<Rendition> subtitles;
    public final Map<String, String> variableDefinitions;
    public final List<Variant> variants;
    public final List<Rendition> videos;

    public static final class Rendition {
        public final Format format;
        public final String groupId;
        public final String name;
        public final Uri url;

        public Rendition(Uri r1, Format r2, String r3, String r4) {
            this.url = r1;
            this.format = r2;
            this.groupId = r3;
            this.name = r4;
        }
    }

    public static final class Variant {
        public final String audioGroupId;
        public final String captionGroupId;
        public final Format format;
        public final String subtitleGroupId;
        public final Uri url;
        public final String videoGroupId;

        public Variant(Uri r1, Format r2, String r3, String r4, String r5, String r6) {
            this.url = r1;
            this.format = r2;
            this.videoGroupId = r3;
            this.audioGroupId = r4;
            this.subtitleGroupId = r5;
            this.captionGroupId = r6;
        }

        public static Variant createMediaPlaylistVariantUrl(Uri r8) {
            return new Variant(r8, new Format.Builder().setId("0").setContainerMimeType(MimeTypes.APPLICATION_M3U8).build(), null, null, null, null);
        }

        public Variant copyWithFormat(Format r8) {
            return new Variant(this.url, r8, this.videoGroupId, this.audioGroupId, this.subtitleGroupId, this.captionGroupId);
        }
    }

    static {
        EMPTY = new HlsMultivariantPlaylist("", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, Collections.emptyList(), false, Collections.emptyMap(), Collections.emptyList());
    }

    public HlsMultivariantPlaylist(String r2, List<String> r3, List<Variant> r4, List<Rendition> r5, List<Rendition> r6, List<Rendition> r7, List<Rendition> r8, Format r9, List<Format> r10, boolean r11, Map<String, String> r12, List<DrmInitData> r13) {
        super(r2, r3, r11);
        this.mediaPlaylistUrls = Collections.unmodifiableList(getMediaPlaylistUrls(r4, r5, r6, r7, r8));
        this.variants = Collections.unmodifiableList(r4);
        this.videos = Collections.unmodifiableList(r5);
        this.audios = Collections.unmodifiableList(r6);
        this.subtitles = Collections.unmodifiableList(r7);
        this.closedCaptions = Collections.unmodifiableList(r8);
        this.muxedAudioFormat = r9;
        if (r10 == null) goto L5;
        List<Format> r0 = Collections.unmodifiableList(r10);
    L6:
        this.muxedCaptionFormats = r0;
        this.variableDefinitions = Collections.unmodifiableMap(r12);
        this.sessionKeyDrmInitData = Collections.unmodifiableList(r13);
        return;
    L5:
        r0 = null;
        goto L6
    }

    private static void addMediaPlaylistUrls(List<Rendition> r3, List<Uri> r4) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L11;
        Uri r1 = r3.get(r0).url;
        if (r1 == null) goto L10;
        if (r4.contains(r1) == true) goto L10;
        r4.add(r1);
    L10:
        r0 = r0 + 1;
        goto L4
    }

    private static <T> List<T> copyStreams(List<T> r6, int r7, List<StreamKey> r8) {
        ArrayList r0 = new ArrayList(r8.size());
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L15;
        T r2 = r6.get(r1);
        int r3 = 0;
    L7:
        if (r3 >= r8.size()) goto L14;
        StreamKey r4 = r8.get(r3);
        if (r4.groupIndex != r7) goto L13;
        if (r4.streamIndex != r1) goto L13;
        r0.add(r2);
    L13:
        r3 = r3 + 1;
    L14:
        r1 = r1 + 1;
        goto L4
    L15:
        return r0;
    }

    public static HlsMultivariantPlaylist createSingleVariantMultivariantPlaylist(String r14) {
        List r4 = Collections.singletonList(Variant.createMediaPlaylistVariantUrl(Uri.parse(r14)));
        return new HlsMultivariantPlaylist("", Collections.emptyList(), r4, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null, false, Collections.emptyMap(), Collections.emptyList());
    }

    private static List<Uri> getMediaPlaylistUrls(List<Variant> r4, List<Rendition> r5, List<Rendition> r6, List<Rendition> r7, List<Rendition> r8) {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r4.size()) goto L9;
        Uri r2 = r4.get(r1).url;
        if (r0.contains(r2) == true) goto L8;
        r0.add(r2);
    L8:
        r1 = r1 + 1;
        goto L4
    L9:
        addMediaPlaylistUrls(r5, r0);
        addMediaPlaylistUrls(r6, r0);
        addMediaPlaylistUrls(r7, r0);
        addMediaPlaylistUrls(r8, r0);
        return r0;
    }

    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    public HlsPlaylist copy(List<StreamKey> r14) {
        return new HlsMultivariantPlaylist(this.baseUri, this.tags, copyStreams(this.variants, 0, r14), Collections.emptyList(), copyStreams(this.audios, 1, r14), copyStreams(this.subtitles, 2, r14), Collections.emptyList(), this.muxedAudioFormat, this.muxedCaptionFormats, this.hasIndependentSegments, this.variableDefinitions, this.sessionKeyDrmInitData);
    }

    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    /* JADX INFO: renamed from: copy, reason: avoid collision after fix types in other method */
    public /* bridge */ /* synthetic */ HlsPlaylist copy2(List r1) {
        return copy(r1);
    }
}
