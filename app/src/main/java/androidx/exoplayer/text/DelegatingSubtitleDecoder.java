package androidx.media3.exoplayer.text;

import androidx.media3.extractor.text.SimpleSubtitleDecoder;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;

/* JADX INFO: loaded from: classes.dex */
final class DelegatingSubtitleDecoder extends SimpleSubtitleDecoder {
    private final SubtitleParser subtitleParser;

    public DelegatingSubtitleDecoder(String r1, SubtitleParser r2) {
        super(r1);
        this.subtitleParser = r2;
    }

    @Override // androidx.media3.extractor.text.SimpleSubtitleDecoder
    protected Subtitle decode(byte[] r3, int r4, boolean r5) {
        if (r5 == false) goto L5;
        this.subtitleParser.reset();
    L5:
        return this.subtitleParser.parseToLegacySubtitle(r3, 0, r4);
    }
}
