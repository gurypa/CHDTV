package androidx.media3.extractor.text.ttml;

import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.Subtitle;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class TtmlSubtitle implements Subtitle {
    private final long[] eventTimesUs;
    private final Map<String, TtmlStyle> globalStyles;
    private final Map<String, String> imageMap;
    private final Map<String, TtmlRegion> regionMap;
    private final TtmlNode root;

    public TtmlSubtitle(TtmlNode r2, Map<String, TtmlStyle> r3, Map<String, TtmlRegion> r4, Map<String, String> r5) {
        this.root = r2;
        this.regionMap = r4;
        this.imageMap = r5;
        if (r3 == null) goto L5;
        Map<String, TtmlStyle> r0 = Collections.unmodifiableMap(r3);
    L6:
        this.globalStyles = r0;
        this.eventTimesUs = r2.getEventTimesUs();
        return;
    L5:
        r0 = Collections.emptyMap();
        goto L6
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public List<Cue> getCues(long r7) {
        return this.root.getCues(r7, this.globalStyles, this.regionMap, this.imageMap);
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public long getEventTime(int r4) {
        return this.eventTimesUs[r4];
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getEventTimeCount() {
        return this.eventTimesUs.length;
    }

    Map<String, TtmlStyle> getGlobalStyles() {
        return this.globalStyles;
    }

    @Override // androidx.media3.extractor.text.Subtitle
    public int getNextEventTimeIndex(long r3) {
        int r0 = Util.binarySearchCeil(this.eventTimesUs, r3, false, false);
        if (r0 < this.eventTimesUs.length) goto L7;
        return -1;
    L7:
        return r0;
    }

    TtmlNode getRoot() {
        return this.root;
    }
}
