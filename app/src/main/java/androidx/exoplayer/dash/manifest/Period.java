package androidx.media3.exoplayer.dash.manifest;

import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Period {
    public final List<AdaptationSet> adaptationSets;
    public final Descriptor assetIdentifier;
    public final List<EventStream> eventStreams;

    /* JADX INFO: renamed from: id */
    public final String f40id;
    public final long startMs;

    public Period(String r8, long r9, List<AdaptationSet> r11) {
        this(r8, r9, r11, Collections.emptyList(), null);
    }

    public Period(String r8, long r9, List<AdaptationSet> r11, List<EventStream> r12) {
        this(r8, r9, r11, r12, null);
    }

    public Period(String r2, long r3, List<AdaptationSet> r5, List<EventStream> r6, Descriptor r7) {
        this.f40id = r2;
        this.startMs = r3;
        this.adaptationSets = Collections.unmodifiableList(r5);
        this.eventStreams = Collections.unmodifiableList(r6);
        this.assetIdentifier = r7;
    }

    public int getAdaptationSetIndex(int r4) {
        int r0 = this.adaptationSets.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        if (this.adaptationSets.get(r1).type == r4) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return r1;
    L8:
        return -1;
    }
}
