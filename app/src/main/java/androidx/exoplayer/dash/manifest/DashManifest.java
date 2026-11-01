package androidx.media3.exoplayer.dash.manifest;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.offline.FilterableManifest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DashManifest implements FilterableManifest<DashManifest> {
    public final long availabilityStartTimeMs;
    public final long durationMs;
    public final boolean dynamic;
    public final Uri location;
    public final long minBufferTimeMs;
    public final long minUpdatePeriodMs;
    private final List<Period> periods;
    public final ProgramInformation programInformation;
    public final long publishTimeMs;
    public final ServiceDescriptionElement serviceDescription;
    public final long suggestedPresentationDelayMs;
    public final long timeShiftBufferDepthMs;
    public final UtcTimingElement utcTiming;

    public DashManifest(long r18, long r20, long r22, boolean r24, long r25, long r27, long r29, long r31, ProgramInformation r33, UtcTimingElement r34, ServiceDescriptionElement r35, Uri r36, List<Period> r37) {
        this.availabilityStartTimeMs = r18;
        this.durationMs = r20;
        this.minBufferTimeMs = r22;
        this.dynamic = r24;
        this.minUpdatePeriodMs = r25;
        this.timeShiftBufferDepthMs = r27;
        this.suggestedPresentationDelayMs = r29;
        this.publishTimeMs = r31;
        this.programInformation = r33;
        this.utcTiming = r34;
        this.location = r36;
        this.serviceDescription = r35;
        if (r37 != null) goto L5;
        List<Period> r1 = Collections.emptyList();
    L6:
        this.periods = r1;
        return;
    L5:
        r1 = r37;
        goto L6
    }

    private static ArrayList<AdaptationSet> copyAdaptationSets(List<AdaptationSet> r16, LinkedList<StreamKey> r17) {
        StreamKey r0 = r17.poll();
        int r1 = r0.periodIndex;
        ArrayList<AdaptationSet> r2 = new ArrayList();
    L3:
        int r3 = r0.groupIndex;
        AdaptationSet r5 = r16.get(r3);
        List<Representation> r6 = r5.representations;
        ArrayList r7 = new ArrayList();
    L4:
        r7.add(r6.get(r0.streamIndex));
        r0 = r17.poll();
        if (r0.periodIndex != r1) goto L8;
        if (r0.groupIndex == r3) goto L4;
    L8:
        r2.add(new AdaptationSet(r5.f38id, r5.type, r7, r5.accessibilityDescriptors, r5.essentialProperties, r5.supplementalProperties));
        if (r0.periodIndex == r1) goto L3;
        r17.addFirst(r0);
        return r2;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    public final DashManifest copy(List<StreamKey> r29) {
        LinkedList r1 = new LinkedList(r29);
        Collections.sort(r1);
        r1.add(new StreamKey(-1, -1, -1));
        ArrayList r3 = new ArrayList();
        int r6 = 0;
        long r25 = 0;
    L3:
        int r4 = getPeriodCount();
        long r7 = C0180C.TIME_UNSET;
        if (r6 >= r4) goto L14;
        if (((StreamKey) r1.peek()).periodIndex == r6) goto L11;
        long r5 = getPeriodDurationMs(r6);
        if (r5 == C0180C.TIME_UNSET) goto L12;
        r25 = r25 + r5;
    L12:
        r6 = r6 + 1;
        goto L3
    L11:
        Period r8 = getPeriod(r6);
        r3.add(new Period(r8.f40id, r8.startMs - r25, copyAdaptationSets(r8.adaptationSets, r1), r8.eventStreams));
        goto L12
    L14:
        if (this.durationMs == C0180C.TIME_UNSET) goto L17;
        r7 = this.durationMs - r25;
    L17:
        return new DashManifest(this.availabilityStartTimeMs, r7, this.minBufferTimeMs, this.dynamic, this.minUpdatePeriodMs, this.timeShiftBufferDepthMs, this.suggestedPresentationDelayMs, this.publishTimeMs, this.programInformation, this.utcTiming, this.serviceDescription, this.location, r3);
    }

    @Override // androidx.media3.exoplayer.offline.FilterableManifest
    public /* bridge */ /* synthetic */ DashManifest copy(List r1) {
        return copy(r1);
    }

    public final Period getPeriod(int r2) {
        return this.periods.get(r2);
    }

    public final int getPeriodCount() {
        return this.periods.size();
    }

    public final long getPeriodDurationMs(int r5) {
        if (r5 != (this.periods.size() - 1)) goto L9;
        if (this.durationMs != C0180C.TIME_UNSET) goto L10;
        return C0180C.TIME_UNSET;
    L10:
        return this.durationMs - this.periods.get(r5).startMs;
    L9:
        return this.periods.get(r5 + 1).startMs - this.periods.get(r5).startMs;
    }

    public final long getPeriodDurationUs(int r3) {
        return Util.msToUs(getPeriodDurationMs(r3));
    }
}
