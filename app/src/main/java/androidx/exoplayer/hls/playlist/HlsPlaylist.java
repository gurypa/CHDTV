package androidx.media3.exoplayer.hls.playlist;

import androidx.media3.exoplayer.offline.FilterableManifest;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class HlsPlaylist implements FilterableManifest<HlsPlaylist> {
    public final String baseUri;
    public final boolean hasIndependentSegments;
    public final List<String> tags;

    protected HlsPlaylist(String r2, List<String> r3, boolean r4) {
        this.baseUri = r2;
        this.tags = Collections.unmodifiableList(r3);
        this.hasIndependentSegments = r4;
    }
}
