package es.antonio.m3u;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Util;
import androidx.media3.common.util.XmlPullParserUtil;
import androidx.media3.exoplayer.dash.manifest.SegmentBase;
import java.io.EOFException;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes7.dex */
public final class DemandTimeline extends AbstractList<SegmentBase.SegmentTimelineElement> implements RandomAccess {
    private long[] durations;
    private int[] ends;
    private int runs;
    private long[] starts;
    private int total;

    public DemandTimeline() {
        this.starts = new long[16];
        this.durations = new long[16];
        this.ends = new int[16];
    }

    static long number(XmlPullParser r1, String r2, long r3) {
        String r4 = r1.getAttributeValue(null, r2);
        if (r4 != null) goto L6;
        return r3;
    L6:
        return Long.parseLong(r4);
    }

    public static List<SegmentBase.SegmentTimelineElement> read(XmlPullParser r20, long r21, long r23) throws XmlPullParserException, IOException {
        DemandTimeline r9 = new DemandTimeline();
        boolean r1 = false;
        int r6 = 0;
        long r2 = 0;
        long r4 = -9223372036854775807L;
    L3:
        r20.next();
        if (XmlPullParserUtil.isStartTag(r20, ExifInterface.LATITUDE_SOUTH) == false) goto L13;
        long r15 = number(r20, "t", C0180C.TIME_UNSET);
        if (r1 == false) goto L9;
        r2 = r9.append(r2, r4, r6, r15);
    L9:
        if (r15 != C0180C.TIME_UNSET) goto L12;
        r15 = r2;
    L12:
        r4 = number(r20, "d", C0180C.TIME_UNSET);
        r6 = (int) number(r20, "r", 0);
        r1 = true;
        r2 = r15;
    L31:
        if (XmlPullParserUtil.isEndTag(r20, "SegmentTimeline") == false) goto L3;
        if (r1 == false) goto L34;
        r9.append(r2, r4, r6, Util.scaleLargeTimestamp(r23, r21, 1000));
    L34:
        return r9;
    L13:
        int r8 = 2;
        if (r20.getEventType() != 2) goto L29;
        int r7 = 1;
    L16:
        if (r7 == 0) goto L31;
        int r16 = r20.next();
        if (r16 == r8) goto L20;
        if (r16 != 3) goto L24;
        r7 = r7 - 1;
    L25:
        r8 = 2;
        goto L16
    L24:
        if (r16 != 1) goto L25;
        throw new EOFException("DASH timeline tag");
    L20:
        r7 = r7 + 1;
        goto L16
    L29:
        if (r20.getEventType() != 1) goto L31;
        throw new EOFException("DASH timeline");
    }

    void addRun(long r6, long r8, int r10) throws IOException {
        if (r10 > 0) goto L5;
        return;
    L5:
        if (r10 > (Integer.MAX_VALUE - this.total)) goto L24;
        if (this.runs <= 0) goto L19;
        if (r8 != this.durations[this.runs - 1]) goto L19;
        if (this.runs != 1) goto L13;
        int r0 = 0;
    L15:
        if (r6 != (this.starts[this.runs - 1] + (((long) (this.total - r0)) * r8))) goto L19;
        this.total += r10;
        this.ends[this.runs - 1] = this.total;
        return;
    L13:
        r0 = this.ends[this.runs - 2];
    L19:
        if (this.runs != this.starts.length) goto L21;
        this.starts = Arrays.copyOf(this.starts, this.runs * 2);
        this.durations = Arrays.copyOf(this.durations, this.runs * 2);
        this.ends = Arrays.copyOf(this.ends, this.runs * 2);
    L21:
        this.starts[this.runs] = r6;
        this.durations[this.runs] = r8;
        this.total += r10;
        int[] r7 = this.ends;
        int r9 = this.runs;
        this.runs = r9 + 1;
        r7[r9] = this.total;
        return;
    L24:
        throw new IOException("DASH timeline too large");
    }

    long append(long r7, long r9, int r11, long r12) throws IOException {
        if (r11 < 0) goto L4;
        int r13 = r11 + 1;
    L5:
        addRun(r7, r9, r13);
        return r7 + (((long) Math.max(0, r13)) * r9);
    L4:
        r13 = (int) Util.ceilDivide(r12 - r7, r9);
        goto L5
    }

    @Override // java.util.AbstractList, java.util.List
    public SegmentBase.SegmentTimelineElement get(int r11) {
        if (r11 < 0) goto L17;
        if (r11 >= this.total) goto L17;
        int r0 = this.runs - 1;
        int r1 = 0;
        int r2 = 0;
    L6:
        if (r2 >= r0) goto L7;
        int r3 = (r2 + r0) >>> 1;
        if (r11 < this.ends[r3]) goto L14;
        r2 = r3 + 1;
        goto L6
    L14:
        r0 = r3;
        goto L6
    L7:
        if (r2 == 0) goto L11;
        r1 = this.ends[r2 - 1];
    L11:
        return new SegmentBase.SegmentTimelineElement(this.starts[r2] + (((long) (r11 - r1)) * this.durations[r2]), this.durations[r2]);
    L17:
        throw new IndexOutOfBoundsException();
    }

    @Override // java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ Object get(int r1) {
        return get(r1);
    }

    int runCount() {
        return this.runs;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.total;
    }
}
