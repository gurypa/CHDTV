package es.antonio.m3u;

import android.net.Uri;
import android.util.Base64;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistParserFactory;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.mp4.PsshAtomUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
public final class ClearKeyHls implements HlsPlaylistParserFactory {
    private static final int MAX_PLAYLIST = 2097152;
    private final DefaultHlsPlaylistParserFactory delegate;
    final byte[] pssh;

    static final class UnsupportedEncryptionException extends IOException {
        UnsupportedEncryptionException() {
            super("HLS con cifrado CBCS/SAMPLE-AES no compatible con la ruta ClearKey de Chdtv");
        }
    }

    public static /* synthetic */ HlsPlaylist $r8$lambda$K5dwG5FKXytoZEwDYebzuesSDmk(ClearKeyHls r0, ParsingLoadable.Parser r1, Uri r2, InputStream r3) {
        return r0.lambda$wrap$0(r1, r2, r3);
    }

    public ClearKeyHls(byte[] r11) throws JSONException {
        this.delegate = new DefaultHlsPlaylistParserFactory();
        JSONArray r0 = new JSONObject(new String(r11, StandardCharsets.UTF_8)).getJSONArray("keys");
        UUID[] r1 = new UUID[r0.length()];
        if (r1.length == 0) goto L15;
        int r2 = 0;
    L6:
        if (r2 >= r1.length) goto L12;
        byte[] r3 = Base64.decode(r0.getJSONObject(r2).getString("kid"), 8);
        if (r3.length != 16) goto L11;
        ByteBuffer r4 = ByteBuffer.wrap(r3);
        r1[r2] = new UUID(r4.getLong(), r4.getLong());
        r2 = r2 + 1;
        goto L6
    L11:
        throw new IllegalArgumentException("KID ClearKey inválido");
    L12:
        this.pssh = PsshAtomUtil.buildPsshAtom(C0180C.COMMON_PSSH_UUID, r1, null);
        return;
    L15:
        throw new IllegalArgumentException("Faltan claves ClearKey");
    }

    private DrmInitData drm(DrmInitData r8) throws IOException {
        if (r8 != null) goto L6;
        return null;
    L6:
        if (C0180C.CENC_TYPE_cenc.equals(r8.schemeType) == false) goto L10;
        return new DrmInitData(C0180C.CENC_TYPE_cenc, new DrmInitData.SchemeData[]{new DrmInitData.SchemeData(C0180C.CLEARKEY_UUID, MimeTypes.VIDEO_MP4, this.pssh)});
    L10:
        throw new IOException("ClearKey HLS requiere cifrado CENC/AES-CTR");
    }

    private /* synthetic */ HlsPlaylist lambda$wrap$0(ParsingLoadable.Parser r10, Uri r11, InputStream r12) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        byte[] r1 = new byte[8192];
    L3:
        int r2 = r12.read(r1);
        int r5 = 0;
        if (r2 == (-1)) goto L10;
        if ((r0.size() + r2) > 2097152) goto L9;
        r0.write(r1, 0, r2);
        goto L3
    L9:
        throw new IOException("La playlist HLS supera 2 MB");
    L10:
        String[] r4 = r0.toString("UTF-8").split("\n");
        int r6 = r4.length;
    L11:
        if (r5 >= r6) goto L35;
        String r7 = r4[r5];
        if (r7.startsWith("#EXT-X-KEY:") == true) goto L17;
        if (r7.startsWith("#EXT-X-SESSION-KEY:") == true) goto L17;
    L31:
        r5 = r5 + 1;
    L17:
        if (r7.matches(".*METHOD=SAMPLE-AES(?:,|$).*") == true) goto L33;
        if (r7.matches(".*METHOD=SAMPLE-AES-(?:CTR|CENC)(?:,|$).*") == false) goto L31;
        if (r7.contains("edef8ba9") == true) goto L31;
        if (r7.contains("com.widevine") == true) goto L31;
        if (r7.contains("com.microsoft.playready") == true) goto L31;
        if (r7.contains("PRMNAGRA") == true) goto L31;
        throw new IOException("Señalización de cifrado HLS no compatible con ClearKey");
    L33:
        throw new UnsupportedEncryptionException();
    L35:
        return convert((HlsPlaylist) r10.parse(r11, new ByteArrayInputStream(r0.toByteArray())));
    }

    private HlsMediaPlaylist.Part part(HlsMediaPlaylist.Part r23) throws IOException {
        return new HlsMediaPlaylist.Part(r23.url, segment(r23.initializationSegment), r23.durationUs, r23.relativeDiscontinuitySequence, r23.relativeStartTimeUs, drm(r23.drmInitData), r23.fullSegmentEncryptionKeyUri, r23.encryptionIV, r23.byteRangeOffset, r23.byteRangeLength, r23.hasGapTag, r23.isIndependent, r23.isPreload);
    }

    private HlsMediaPlaylist.Segment segment(HlsMediaPlaylist.Segment r24) throws IOException {
        if (r24 != null) goto L6;
        return null;
    L6:
        ArrayList r2 = new ArrayList();
        Iterator<HlsMediaPlaylist.Part> r3 = r24.parts.iterator();
    L8:
        if (r3.hasNext() == false) goto L11;
        r2.add(part(r3.next()));
        goto L8
    L11:
        return new HlsMediaPlaylist.Segment(r24.url, segment(r24.initializationSegment), r24.title, r24.durationUs, r24.relativeDiscontinuitySequence, r24.relativeStartTimeUs, drm(r24.drmInitData), r24.fullSegmentEncryptionKeyUri, r24.encryptionIV, r24.byteRangeOffset, r24.byteRangeLength, r24.hasGapTag, r2);
    }

    private ParsingLoadable.Parser<HlsPlaylist> wrap(final ParsingLoadable.Parser<HlsPlaylist> r2) {
        return new ClearKeyHls$$ExternalSyntheticLambda0(this, r2);
    }

    HlsPlaylist convert(HlsPlaylist r31) throws IOException {
        if ((r31 instanceof HlsMultivariantPlaylist) == false) goto L10;
        HlsMultivariantPlaylist r2 = (HlsMultivariantPlaylist) r31;
        ArrayList r3 = new ArrayList();
        Iterator<DrmInitData> r4 = r2.sessionKeyDrmInitData.iterator();
    L6:
        if (r4.hasNext() == false) goto L9;
        r3.add(drm(r4.next()));
        goto L6
    L9:
        return new HlsMultivariantPlaylist(r2.baseUri, r2.tags, r2.variants, r2.videos, r2.audios, r2.subtitles, r2.closedCaptions, r2.muxedAudioFormat, r2.muxedCaptionFormats, r2.hasIndependentSegments, r2.variableDefinitions, r3);
    L10:
        HlsMediaPlaylist r5 = (HlsMediaPlaylist) r31;
        ArrayList r6 = new ArrayList();
        Iterator<HlsMediaPlaylist.Segment> r7 = r5.segments.iterator();
    L12:
        if (r7.hasNext() == false) goto L14;
        r6.add(segment(r7.next()));
        goto L12
    L14:
        ArrayList r27 = new ArrayList();
        Iterator<HlsMediaPlaylist.Part> r8 = r5.trailingParts.iterator();
    L16:
        if (r8.hasNext() == false) goto L19;
        r27.add(part(r8.next()));
        goto L16
    L19:
        if (r5.hasPositiveStartOffset == false) goto L23;
        long r9 = r5.startOffsetUs;
    L21:
        long r10 = r9;
    L27:
        return new HlsMediaPlaylist(r5.playlistType, r5.baseUri, r5.tags, r10, r5.preciseStart, r5.startTimeUs, r5.hasDiscontinuitySequence, r5.discontinuitySequence, r5.mediaSequence, r5.version, r5.targetDurationUs, r5.partTargetDurationUs, r5.hasIndependentSegments, r5.hasEndTag, r5.hasProgramDateTime, drm(r5.protectionSchemes), r6, r27, r5.serverControl, r5.renditionReports);
    L23:
        if (r5.startOffsetUs != C0180C.TIME_UNSET) goto L25;
        r10 = -9223372036854775807L;
        goto L27
    L25:
        r9 = r5.startOffsetUs - r5.durationUs;
        goto L21
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
        return wrap(this.delegate.createPlaylistParser());
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(HlsMultivariantPlaylist r2, HlsMediaPlaylist r3) {
        return wrap(this.delegate.createPlaylistParser(r2, r3));
    }
}
