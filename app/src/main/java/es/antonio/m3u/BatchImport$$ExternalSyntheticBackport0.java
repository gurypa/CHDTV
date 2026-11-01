package es.antonio.m3u;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.util.Iterator;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes7.dex */
public final /* synthetic */ class BatchImport$$ExternalSyntheticBackport0 {
    /* JADX INFO: renamed from: m */
    public static /* synthetic */ String m390m(CharSequence r2, Iterable r3) {
        if (r2 == null) goto L11;
        StringBuilder r0 = new StringBuilder();
        Iterator r4 = r3.iterator();
        if (r4.hasNext() == false) goto L9;
    L5:
        r0.append((CharSequence) r4.next());
        if (r4.hasNext() == false) goto L9;
        r0.append(r2);
    L9:
        return r0.toString();
    L11:
        throw new NullPointerException(TtmlNode.RUBY_DELIMITER);
    }
}
