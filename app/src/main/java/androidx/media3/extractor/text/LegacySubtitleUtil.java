package androidx.media3.extractor.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Consumer;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LegacySubtitleUtil {
    private LegacySubtitleUtil() {
    }

    private static int getStartIndex(Subtitle r3, long r4) {
        if (r4 != C0180C.TIME_UNSET) goto L6;
        return 0;
    L6:
        int r0 = r3.getNextEventTimeIndex(r4);
        if (r0 != (-1)) goto L9;
        r0 = r3.getEventTimeCount();
    L9:
        if (r0 > 0) goto L11;
        return r0;
    L11:
        if (r3.getEventTime(r0 - 1) == r4) goto L13;
        return r0;
    L13:
        return r0 - 1;
    }

    private static void outputSubtitleEvent(Subtitle r8, int r9, Consumer<CuesWithTiming> r10) {
        long r2 = r8.getEventTime(r9);
        List<Cue> r1 = r8.getCues(r2);
        if (r1.isEmpty() == false) goto L6;
        return;
    L6:
        if (r9 == (r8.getEventTimeCount() - 1)) goto L12;
        long r4 = r8.getEventTime(r9 + 1) - r8.getEventTime(r9);
        if (r4 <= 0) goto L13;
        r10.accept(new CuesWithTiming(r1, r2, r4));
        return;
    L13:
        return;
    L12:
        throw new IllegalStateException();
    }

    public static void toCuesWithTiming(Subtitle r12, SubtitleParser.OutputOptions r13, Consumer<CuesWithTiming> r14) {
        int r0 = getStartIndex(r12, r13.startTimeUs);
        boolean r1 = false;
        if (r13.startTimeUs != C0180C.TIME_UNSET) goto L5;
    L11:
        int r2 = r0;
    L13:
        if (r2 >= r12.getEventTimeCount()) goto L16;
        outputSubtitleEvent(r12, r2, r14);
        r2 = r2 + 1;
        goto L13
    L16:
        if (r13.outputAllCues == false) goto L28;
        if (r1 == false) goto L19;
        int r3 = r0 - 1;
    L20:
        int r4 = 0;
    L21:
        if (r4 >= r3) goto L23;
        outputSubtitleEvent(r12, r4, r14);
        r4 = r4 + 1;
        goto L21
    L23:
        if (r1 == false) goto L29;
        r14.accept(new CuesWithTiming(r12.getCues(r13.startTimeUs), r12.getEventTime(r3), r13.startTimeUs - r12.getEventTime(r3)));
        return;
    L29:
        return;
    L19:
        r3 = r0;
        goto L20
    L28:
        return;
    L5:
        if (r0 >= r12.getEventTimeCount()) goto L11;
        List<Cue> r5 = r12.getCues(r13.startTimeUs);
        long r6 = r12.getEventTime(r0);
        if (r5.isEmpty() == true) goto L11;
        if (r13.startTimeUs >= r6) goto L11;
        r14.accept(new CuesWithTiming(r5, r13.startTimeUs, r6 - r13.startTimeUs));
        r1 = true;
        goto L11
    }
}
