package androidx.media3.exoplayer.offline;

import android.net.Uri;
import androidx.media3.common.StreamKey;
import androidx.media3.exoplayer.offline.FilterableManifest;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class FilteringManifestParser<T extends FilterableManifest<T>> implements ParsingLoadable.Parser<T> {
    private final ParsingLoadable.Parser<? extends T> parser;
    private final List<StreamKey> streamKeys;

    public FilteringManifestParser(ParsingLoadable.Parser<? extends T> r1, List<StreamKey> r2) {
        this.parser = r1;
        this.streamKeys = r2;
    }

    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public T parse(Uri r3, InputStream r4) throws IOException {
        T r0 = this.parser.parse(r3, r4);
        if (this.streamKeys == null) goto L9;
        if (this.streamKeys.isEmpty() == true) goto L9;
        return (T) r0.copy(this.streamKeys);
    L9:
        return r0;
    }

    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public /* bridge */ /* synthetic */ Object parse(Uri r1, InputStream r2) throws IOException {
        return parse(r1, r2);
    }
}
