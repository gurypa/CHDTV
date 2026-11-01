package androidx.media3.exoplayer.metadata;

import androidx.media3.common.Format;
import androidx.media3.extractor.metadata.MetadataDecoder;

/* JADX INFO: loaded from: classes.dex */
public interface MetadataDecoderFactory {
    public static final MetadataDecoderFactory DEFAULT = null;

    static {
        DEFAULT = new C03121();
    }

    MetadataDecoder createDecoder(Format r1);

    boolean supportsFormat(Format r1);
}
