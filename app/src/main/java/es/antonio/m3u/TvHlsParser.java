package es.antonio.m3u;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* JADX INFO: loaded from: classes7.dex */
public final class TvHlsParser implements HlsPlaylistParserFactory {
    private final HlsPlaylistParserFactory delegate;

    public TvHlsParser(HlsPlaylistParserFactory r1) {
        this.delegate = r1;
    }

    static /* synthetic */ HlsPlaylist lambda$0(boolean r5, ParsingLoadable.Parser r6, Uri r7, InputStream r8) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        byte[] r1 = new byte[8192];
    L3:
        int r2 = r8.read(r1);
        if (r2 == (-1)) goto L5;
        if (r2 == 0) goto L16;
        if ((r0.size() + r2) > 2097152) goto L16;
        r0.write(r1, 0, r2);
    L16:
        throw new IOException("HLS playlist size/progress");
    L5:
        byte[] r9 = r0.toByteArray();
        long r3 = System.nanoTime();
        HlsMediaPlaylist r10 = parse(r7.toString(), new String(r9, StandardCharsets.UTF_8), r5);
        if (r10 == null) goto L10;
        BlackBox.event("HLS_FAST_PARSE", "segments=" + r10.segments.size() + " duration_us=" + ((System.nanoTime() - r3) / 1000) + " full_window=true");
        return r10;
    L10:
        return (HlsPlaylist) r6.parse(r7, new ByteArrayInputStream(r9));
    }

    static long micros(String r14) {
        if (r14.isEmpty() == true) goto L33;
        long r0 = 0;
        int r4 = 0;
        boolean r5 = false;
        boolean r6 = false;
        int r7 = 0;
        long r2 = 0;
    L6:
        if (r4 >= r14.length()) goto L7;
        char r8 = r14.charAt(r4);
        if (r8 != '.') goto L20;
        if (r6 == true) goto L20;
        r6 = true;
    L29:
        r4 = r4 + 1;
    L20:
        if (r8 < '0') goto L31;
        if (r8 > '9') goto L31;
        if (r6 == true) goto L26;
        r2 = TvHlsParser$$ExternalSyntheticBackport1.m413m(TvHlsParser$$ExternalSyntheticBackport0.m412m(r2, 10), r8 - '0');
    L25:
        r5 = true;
        goto L29
    L26:
        if (r7 >= 6) goto L28;
        r0 = ((r0 * 10) + ((long) r8)) - 48;
    L28:
        r7 = r7 + 1;
    L31:
        throw new IllegalArgumentException();
    L7:
        if (r5 == false) goto L14;
    L8:
        int r15 = r7 + 1;
        if (r7 >= 6) goto L11;
        r0 = r0 * 10;
        r7 = r15;
        goto L8
    L11:
        return TvHlsParser$$ExternalSyntheticBackport1.m413m(TvHlsParser$$ExternalSyntheticBackport2.m414m(r2, 1000000), r0);
    L14:
        throw new IllegalArgumentException();
    L33:
        throw new IllegalArgumentException();
    }

    static HlsMediaPlaylist parse(String r61, String r62, boolean r63) {
        if (r62.startsWith("#EXTM3U\n") == false) goto L6;
    L8:
        ArrayList r2 = new ArrayList();     // Catch: Throwable -> L162 IOException -> L164
        int r4 = 0;
    L10:
        String r9 = "#EXT-X-PROGRAM-DATE-TIME:";
        String r3 = "#EXTINF:";
        String r0 = "#EXTM3U";
        if (r4 >= r62.length()) goto L13;
        int r1 = r62.indexOf(10, r4);     // Catch: Throwable -> L162 IOException -> L164
        if (r1 >= 0) goto L116;
        r1 = r62.length();     // Catch: Throwable -> L162 IOException -> L164
    L116:
        String r5 = r62.substring(r4, r1).trim();     // Catch: Throwable -> L162 IOException -> L164
        int r6 = r1 + 1;     // Catch: Throwable -> L162 IOException -> L164
        if (r5.isEmpty() == false) goto L120;
    L161:
        r4 = r6;
        goto L10
    L120:
        if (r5.equals("#EXTM3U") == false) goto L126;
        if (r2.isEmpty() == true) goto L126;
        return null;
    L126:
        if (r5.contains("{$") == true) goto L123;
        if (r5.startsWith("#") == true) goto L131;
    L160:
        r2.add(r5);     // Catch: Throwable -> L162 IOException -> L164
        goto L161
    L131:
        if (r5.equals("#EXTM3U") == true) goto L160;
        if (r5.startsWith("#EXTINF:") == true) goto L160;
        if (r5.startsWith("#EXT-X-ALLOW-CACHE:") == true) goto L160;
        if (r5.startsWith("#EXT-X-TARGETDURATION:") == true) goto L160;
        if (r5.startsWith("#EXT-X-MEDIA-SEQUENCE:") == true) goto L160;
        if (r5.startsWith("#EXT-X-VERSION:") == true) goto L160;
        if (r5.startsWith("#EXT-X-PLAYLIST-TYPE:") == true) goto L160;
        if (r5.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE:") == true) goto L160;
        if (r5.equals("#EXT-X-DISCONTINUITY") == true) goto L160;
        if (r5.equals("#EXT-X-INDEPENDENT-SEGMENTS") == true) goto L160;
        if (r5.equals("#EXT-X-ENDLIST") == true) goto L160;
        if (r5.equals("#EXT-X-GAP") == true) goto L160;
        if (r5.startsWith("#EXT-X-PROGRAM-DATE-TIME:") == true) goto L160;
        if (r5.equals("#EXT-X-KEY:METHOD=NONE") == true) goto L160;
        return null;
    L123:
        return null;
    L13:
        if (r2.size() >= 4) goto L16;
        return null;
    L16:
        ArrayList r7 = new ArrayList();     // Catch: Throwable -> L162 IOException -> L164
        ArrayList r8 = new ArrayList();     // Catch: Throwable -> L162 IOException -> L164
        Iterator r10 = r2.iterator();     // Catch: Throwable -> L162 IOException -> L164
        String r23 = "#";
        boolean r38 = r63;
        Object r34 = "#EXT-X-GAP";
        Object r35 = "#EXT-X-ENDLIST";
        String r44 = "";
        long r36 = -9223372036854775807L;
        long r64 = 0;
        long r11 = 0;
        long r26 = 0;
        long r30 = 0;
        boolean r24 = false;
        int r25 = 0;
        boolean r28 = false;
        int r29 = 0;
        int r32 = 1;
        int r33 = 0;
        boolean r40 = false;
        boolean r57 = false;
    L19:
        if (r10.hasNext() == false) goto L20;
        String r12 = (String) r10.next();     // Catch: Throwable -> L162 IOException -> L164
        if (r12.equals(r0) == true) goto L36;
        String r39 = r0;
        if (r12.startsWith("#EXT") == false) goto L41;
        r7.add(r12);     // Catch: Throwable -> L162 IOException -> L164
    L41:
        if (r12.startsWith(r3) == false) goto L49;
        if (r24 == true) goto L43;
        Iterator r59 = r10;
        int r13 = r12.indexOf(44, 8);     // Catch: Throwable -> L162 IOException -> L164
        if (r13 < 0) goto L47;
        long r41 = micros(r12.substring(8, r13));     // Catch: Throwable -> L162 IOException -> L164
        r44 = r12.substring(r13 + 1);     // Catch: Throwable -> L162 IOException -> L164
        r0 = r39;
        r64 = r41;
        r10 = r59;
        r24 = true;
        goto L19
    L47:
        return null;
    L43:
        return null;
    L49:
        Iterator r510 = r10;
        if (r12.startsWith("#EXT-X-TARGETDURATION:") == false) goto L56;
        String r60 = r3;
        r36 = TvHlsParser$$ExternalSyntheticBackport3.m415m(Integer.parseInt(r12.substring(22)), 1000000);     // Catch: Throwable -> L162 IOException -> L164
        if (r36 <= 0) goto L53;
        r0 = r39;
        r10 = r510;
        r3 = r60;
        goto L19
    L53:
        return null;
    L56:
        String r65 = r3;
        if (r12.startsWith("#EXT-X-MEDIA-SEQUENCE:") == false) goto L64;
        r30 = Long.parseLong(r12.substring(22));     // Catch: Throwable -> L162 IOException -> L164
        if (r30 < 0) goto L60;
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L60:
        return null;
    L64:
        if (r12.startsWith("#EXT-X-VERSION:") == false) goto L71;
        int r14 = Integer.parseInt(r12.substring(15));     // Catch: Throwable -> L162 IOException -> L164
        if (r14 < 1) goto L67;
        r32 = r14;
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L67:
        return null;
    L71:
        if (r12.startsWith("#EXT-X-PLAYLIST-TYPE:") == false) goto L82;
        String r15 = r12.substring(21);     // Catch: Throwable -> L162 IOException -> L164
        if (r15.equals("VOD") == false) goto L77;
        r25 = 1;
    L75:
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L77:
        if (r15.equals("EVENT") == false) goto L79;
        r25 = 2;
        goto L75
    L79:
        return null;
    L82:
        if (r12.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE:") == false) goto L85;
        r29 = Integer.parseInt(r12.substring(30));     // Catch: Throwable -> L162 IOException -> L164
        r28 = true;
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L85:
        if (r12.equals("#EXT-X-DISCONTINUITY") == false) goto L88;
        r33 = r33 + 1;     // Catch: Throwable -> L162 IOException -> L164
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L88:
        if (r12.startsWith(r9) == true) goto L90;
        if (r12.equals("#EXT-X-INDEPENDENT-SEGMENTS") == false) goto L96;
        r38 = true;
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L96:
        Object r16 = r35;
        if (r12.equals(r16) == false) goto L99;
        r35 = r16;
        r40 = true;
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L99:
        Object r17 = r34;
        if (r12.equals(r17) == false) goto L102;
        r35 = r16;
        r57 = true;
        r34 = r17;
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L102:
        String r37 = r9;
        String r18 = r23;
        if (r12.startsWith(r18) == true) goto L112;
        if (r24 == false) goto L227;
        ArrayList r27 = r7;
        if (r8.size() >= 16384) goto L109;
        r8.add(new HlsMediaPlaylist.Segment(r12, null, r44, r64, r33, r11, null, null, null, 0, -1, r57, Collections.emptyList()));     // Catch: Throwable -> L162 IOException -> L164
        r11 = TvHlsParser$$ExternalSyntheticBackport1.m413m(r11, r64);     // Catch: Throwable -> L162 IOException -> L164
        r35 = r16;
        r44 = "";
        r64 = 0;
        r7 = r27;
        r0 = r39;
        r10 = r510;
        r24 = false;
        r57 = false;
        r23 = r18;
        r9 = r37;
        r34 = r17;
        r3 = r65;
        goto L19
    L109:
        return null;
    L227:
        return null;
    L112:
        r35 = r16;
        r64 = r64;
        r7 = r7;
        r0 = r39;
        r10 = r510;
        r23 = r18;
        r9 = r37;
        r34 = r17;
        r3 = r65;
        goto L19
    L90:
        if (r26 != 0) goto L92;
        r26 = TvHlsParser$$ExternalSyntheticBackport5.m417m(TvHlsParser$$ExternalSyntheticBackport4.m416m(Util.parseXsDateTime(r12.substring(25)), 1000), r11);     // Catch: Throwable -> L162 IOException -> L164
        r0 = r39;
        r10 = r510;
        r3 = r65;
        goto L19
    L92:
        r17 = r34;
        r16 = r35;
        r37 = r9;
        r18 = r23;
        goto L112
    L36:
        r39 = r0;
        r510 = r10;
        r65 = r3;
        r17 = r34;
        r16 = r35;
        r37 = r9;
        r18 = r23;
        goto L112
    L20:
        if (r24 == false) goto L22;
        return null;
    L22:
        if (r8.isEmpty() == false) goto L24;
        return null;
    L24:
        if (r36 != C0180C.TIME_UNSET) goto L27;
        return null;
    L27:
        if (r26 == 0) goto L29;
        boolean r19 = true;
    L30:
        return new HlsMediaPlaylist(r25, r61, r7, C0180C.TIME_UNSET, false, r26, r28, r29, r30, r32, r36, C0180C.TIME_UNSET, r38, r40, r19, null, r8, Collections.emptyList(), new HlsMediaPlaylist.ServerControl(C0180C.TIME_UNSET, false, C0180C.TIME_UNSET, C0180C.TIME_UNSET, false), Collections.emptyMap());
    L29:
        r19 = false;
        goto L30
    L6:
        if (r62.startsWith("#EXTM3U\r\n") == true) goto L8;
        return null;
    L165:
        return null;
    }

    private ParsingLoadable.Parser<HlsPlaylist> wrap(final ParsingLoadable.Parser<HlsPlaylist> r2, final boolean r3) {
        return new TvHlsParser$$ExternalSyntheticLambda6(r3, r2);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
        return wrap(this.delegate.createPlaylistParser(), false);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(HlsMultivariantPlaylist r2, HlsMediaPlaylist r3) {
        return wrap(this.delegate.createPlaylistParser(r2, r3), r2.hasIndependentSegments);
    }
}
