package es.antonio.chdtv.playback.sampleaes;

import android.net.Uri;
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

/* JADX INFO: loaded from: classes5.dex */
public final class SampleAesPlaylistParserFactory implements HlsPlaylistParserFactory {
    private final HlsPlaylistParserFactory delegate;
    private final SampleAesManifest manifest;

    public SampleAesPlaylistParserFactory(HlsPlaylistParserFactory r1, SampleAesManifest r2) {
        this.delegate = r1;
        this.manifest = r2;
    }

    private ParsingLoadable.Parser<HlsPlaylist> wrap(final ParsingLoadable.Parser<HlsPlaylist> r2) {
        return new SampleAesPlaylistParserFactory$$ExternalSyntheticLambda0(this, r2);
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
        return wrap(this.delegate.createPlaylistParser());
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(HlsMultivariantPlaylist r2, HlsMediaPlaylist r3) {
        return wrap(this.delegate.createPlaylistParser(r2, r3));
    }

    /* JADX INFO: renamed from: lambda$wrap$0$es-antonio-chdtv-playback-sampleaes-SampleAesPlaylistParserFactory */
    /* synthetic */ HlsPlaylist m389x9240e3b3(ParsingLoadable.Parser r6, Uri r7, InputStream r8) throws IOException {
        ByteArrayOutputStream r0 = new ByteArrayOutputStream();
        byte[] r1 = new byte[8192];
    L3:
        int r2 = r8.read(r1);
        if (r2 == (-1)) goto L14;
        if (r2 == 0) goto L12;
        if ((r0.size() + r2) > 2097152) goto L10;
        r0.write(r1, 0, r2);
        goto L3
    L10:
        throw new IOException("La playlist HLS supera 2 MB");
    L12:
        throw new IOException("Sample-AES: lectura de playlist sin progreso");
    L14:
        return (HlsPlaylist) r6.parse(r7, new ByteArrayInputStream(this.manifest.rewrite(r7.toString(), new String(r0.toByteArray(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8)));
    }
}
