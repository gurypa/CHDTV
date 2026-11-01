package es.antonio.m3u;

import androidx.media3.extractor.text.ttml.TtmlNode;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class MovistarStreams$$ExternalSyntheticBackport0 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ String m403m(CharSequence r3, CharSequence[] r4) {
        if (r3 == null) goto L12;
        StringBuilder r0 = new StringBuilder();
        if (r4.length <= 0) goto L10;
        r0.append(r4[0]);
        int r1 = 1;
    L7:
        if (r1 >= r4.length) goto L10;
        r0.append(r3);
        r0.append(r4[r1]);
        r1 = r1 + 1;
    L10:
        return r0.toString();
    L12:
        throw new NullPointerException(TtmlNode.RUBY_DELIMITER);
    }
}
