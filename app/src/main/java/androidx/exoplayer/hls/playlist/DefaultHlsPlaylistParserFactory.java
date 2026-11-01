package androidx.media3.exoplayer.hls.playlist;

import androidx.media3.exoplayer.upstream.ParsingLoadable;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultHlsPlaylistParserFactory implements HlsPlaylistParserFactory {
    public DefaultHlsPlaylistParserFactory() {
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
        return new HlsPlaylistParser();
    }

    @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
    public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(HlsMultivariantPlaylist r2, HlsMediaPlaylist r3) {
        return new HlsPlaylistParser(r2, r3);
    }
}
