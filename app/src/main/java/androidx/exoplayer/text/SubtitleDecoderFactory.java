package androidx.media3.exoplayer.text;

import androidx.media3.common.Format;
import androidx.media3.extractor.text.SubtitleDecoder;

/* JADX INFO: loaded from: classes.dex */
public interface SubtitleDecoderFactory {
    public static final SubtitleDecoderFactory DEFAULT = null;

    static {
        DEFAULT = new C03621();
    }

    SubtitleDecoder createDecoder(Format r1);

    boolean supportsFormat(Format r1);
}
