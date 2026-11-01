package es.antonio.chdtv.playback;

import android.content.Context;
import android.net.Uri;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistParserFactory;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory;
import es.antonio.chdtv.playback.sampleaes.SampleAesDataSourceFactory;
import es.antonio.chdtv.playback.sampleaes.SampleAesKeys;
import es.antonio.chdtv.playback.sampleaes.SampleAesManifest;
import es.antonio.chdtv.playback.sampleaes.SampleAesPlaylistParserFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public final class HlsPlaybackModule {
    private HlsPlaybackModule() {
    }

    private static Map<String, String> copyHeaders(Map<String, String> r4) {
        if (r4 == null) goto L4;
        HashMap r0 = new HashMap();
        Iterator<Map.Entry<String, String>> r5 = r4.entrySet().iterator();
    L7:
        if (r5.hasNext() == false) goto L21;
        Map.Entry<String, String> r1 = r5.next();
        String r2 = r1.getKey();
        String r3 = r1.getValue();
        if (r2 == null) goto L19;
        if (r3 == null) goto L19;
        if (r2.matches("[!#$%&'*+.^_`|~0-9A-Za-z-]+") == false) goto L19;
        if (r3.indexOf(13) >= 0) goto L19;
        if (r3.indexOf(10) >= 0) goto L19;
        r0.put(r2, r3);
    L19:
        throw new IllegalArgumentException("Cabecera HTTP inválida");
    L21:
        return Collections.unmodifiableMap(r0);
    L4:
        return Collections.emptyMap();
    }

    public static HlsMediaSource.Factory createSampleAesFactory(Context r1, DataSource.Factory r2, byte[] r3, HlsPlaylistParserFactory r4) throws IOException {
        DefaultDataSource.Factory r0 = new DefaultDataSource.Factory(r1.getApplicationContext(), r2);
        SampleAesManifest r5 = new SampleAesManifest(new SampleAesKeys(r3, r0));
        HlsMediaSource.Factory r6 = new HlsMediaSource.Factory(new SampleAesDataSourceFactory(r0, r5)).setAllowChunklessPreparation(true);
        if (r4 != null) goto L6;
        r4 = new DefaultHlsPlaylistParserFactory();
    L6:
        return r6.setPlaylistParserFactory(new SampleAesPlaylistParserFactory(r4, r5));
    }

    public static HlsMediaSource createSource(Context r7, String r8, String r9, Map<String, String> r10, String r11, Map<String, String> r12) {
        return createSource(r7, r8, r9, r10, r11, r12, null);
    }

    public static HlsMediaSource createSource(Context r2, String r3, String r4, Map<String, String> r5, String r6, Map<String, String> r7, DataSource.Factory r8) {
        requireHttpUrl(r3);
        DefaultHttpDataSource.Factory r9 = new DefaultHttpDataSource.Factory().setConnectTimeoutMs(15000).setReadTimeoutMs(AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH).setAllowCrossProtocolRedirects(false).setDefaultRequestProperties(copyHeaders(r5));
        if (r4 != null) goto L5;
    L7:
        MediaItem.Builder r10 = new MediaItem.Builder().setUri(r3).setMimeType(MimeTypes.APPLICATION_M3U8);
        if (r6 != null) goto L10;
    L12:
        Context r11 = r2.getApplicationContext();
        if (r8 != null) goto L16;
        r8 = r9;
    L16:
        return new HlsMediaSource.Factory(new DefaultDataSource.Factory(r11, r8)).createMediaSource(r10.build());
    L10:
        if (r6.trim().isEmpty() == true) goto L12;
        requireHttpUrl(r6);
        r10.setDrmConfiguration(new MediaItem.DrmConfiguration.Builder(C0180C.WIDEVINE_UUID).setLicenseUri(r6).setLicenseRequestHeaders(copyHeaders(r7)).build());
        goto L12
    L5:
        if (r4.trim().isEmpty() == true) goto L7;
        r9.setUserAgent(r4.trim());
        goto L7
    }

    private static void requireHttpUrl(String r2) {
        if (r2 == null) goto L19;
        if (r2.trim().isEmpty() == true) goto L19;
        Uri r3 = Uri.parse(r2);
        String r0 = r3.getScheme();
        if ("http".equalsIgnoreCase(r0) == true) goto L10;
        if ("https".equalsIgnoreCase(r0) == true) goto L10;
    L17:
        throw new IllegalArgumentException("Se necesita una URL HTTP/HTTPS sin credenciales en la dirección");
    L10:
        if (r3.getHost() == null) goto L17;
        if (r3.getHost().isEmpty() == true) goto L17;
        if (r3.getUserInfo() != null) goto L17;
        return;
    L19:
        throw new IllegalArgumentException("Falta una URL HTTP/HTTPS");
    }
}
