package kotlin;

import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: UnsignedUtils.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0015\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001¢\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\u001f\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\r\u0010\u000e\u001a\u001f\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\u001f\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001¢\u0006\u0004\b\u0015\u0010\u0016\u001a\u001f\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001¢\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000¨\u0006\u001d"}, m421d2 = {"doubleToUInt", "Lkotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Lkotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", TtmlNode.RUBY_BASE, "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class UnsignedKt {
    public static final int doubleToUInt(double r4) {
        if (Double.isNaN(r4) == false) goto L6;
        return 0;
    L6:
        if (r4 > uintToDouble(0)) goto L9;
        return 0;
    L9:
        if (r4 < uintToDouble(-1)) goto L12;
        return -1;
    L12:
        if (r4 > 2.147483647E9d) goto L15;
        return UInt.m914constructorimpl((int) r4);
    L15:
        return UInt.m914constructorimpl(UInt.m914constructorimpl((int) (r4 - ((double) Integer.MAX_VALUE))) + UInt.m914constructorimpl(Integer.MAX_VALUE));
    }

    public static final long doubleToULong(double r5) {
        if (Double.isNaN(r5) == false) goto L6;
        return 0;
    L6:
        if (r5 > ulongToDouble(0)) goto L9;
        return 0;
    L9:
        if (r5 < ulongToDouble(-1)) goto L12;
        return -1;
    L12:
        if (r5 >= 9.223372036854776E18d) goto L15;
        return ULong.m993constructorimpl((long) r5);
    L15:
        return ULong.m993constructorimpl(ULong.m993constructorimpl((long) (r5 - 9.223372036854776E18d)) - Long.MIN_VALUE);
    }

    public static final int uintCompare(int r2, int r3) {
        return Intrinsics.compare(r2 ^ Integer.MIN_VALUE, Integer.MIN_VALUE ^ r3);
    }

    /* JADX INFO: renamed from: uintDivide-J1ME1BU, reason: not valid java name */
    public static final int m1170uintDivideJ1ME1BU(int r6, int r7) {
        return UInt.m914constructorimpl((int) ((((long) r6) & 4294967295L) / (4294967295L & ((long) r7))));
    }

    /* JADX INFO: renamed from: uintRemainder-J1ME1BU, reason: not valid java name */
    public static final int m1171uintRemainderJ1ME1BU(int r6, int r7) {
        return UInt.m914constructorimpl((int) ((((long) r6) & 4294967295L) % (4294967295L & ((long) r7))));
    }

    public static final double uintToDouble(int r6) {
        return ((double) (Integer.MAX_VALUE & r6)) + (((double) ((r6 >>> 31) << 30)) * ((double) 2));
    }

    public static final int ulongCompare(long r4, long r6) {
        return Intrinsics.compare(r4 ^ Long.MIN_VALUE, Long.MIN_VALUE ^ r6);
    }

    /* JADX INFO: renamed from: ulongDivide-eb3DHEI, reason: not valid java name */
    public static final long m1172ulongDivideeb3DHEI(long r13, long r15) {
        long r4 = 0;
        if (r15 >= 0) goto L11;
        if (C0871x7f2744bd.m371m(r13, r15) < 0) goto L9;
        r4 = 1;
    L9:
        return ULong.m993constructorimpl(r4);
    L11:
        if (r13 >= 0) goto L13;
        int r5 = 1;
        long r6 = ((r13 >>> 1) / r15) << 1;
        if (C0871x7f2744bd.m371m(ULong.m993constructorimpl(r13 - (r6 * r15)), ULong.m993constructorimpl(r15)) >= 0) goto L19;
        r5 = 0;
    L19:
        return ULong.m993constructorimpl(((long) r5) + r6);
    L13:
        return ULong.m993constructorimpl(r13 / r15);
    }

    /* JADX INFO: renamed from: ulongRemainder-eb3DHEI, reason: not valid java name */
    public static final long m1173ulongRemaindereb3DHEI(long r14, long r16) {
        long r4 = 0;
        if (r16 >= 0) goto L10;
        if (C0871x7f2744bd.m371m(r14, r16) >= 0) goto L8;
        return r14;
    L8:
        return ULong.m993constructorimpl(r14 - r16);
    L10:
        if (r14 >= 0) goto L12;
        long r8 = r14 - ((((r14 >>> 1) / r16) << 1) * r16);
        if (C0871x7f2744bd.m371m(ULong.m993constructorimpl(r8), ULong.m993constructorimpl(r16)) < 0) goto L17;
        r4 = r16;
    L17:
        return ULong.m993constructorimpl(r8 - r4);
    L12:
        return ULong.m993constructorimpl(r14 % r16);
    }

    public static final double ulongToDouble(long r4) {
        return ((r4 >>> 11) * ((double) 2048)) + (2047 & r4);
    }

    public static final String ulongToString(long r1) {
        return ulongToString(r1, 10);
    }

    public static final String ulongToString(long r8, int r10) {
        if (r8 < 0) goto L6;
        String r0 = Long.toString(r8, CharsKt.checkRadix(r10));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    L6:
        long r2 = ((r8 >>> 1) / ((long) r10)) << 1;
        long r4 = r8 - (((long) r10) * r2);
        if (r4 < r10) goto L9;
        r4 = r4 - ((long) r10);
        r2 = r2 + 1;
    L9:
        StringBuilder r1 = new StringBuilder();
        String r6 = Long.toString(r2, CharsKt.checkRadix(r10));
        Intrinsics.checkNotNullExpressionValue(r6, "toString(...)");
        StringBuilder r3 = r1.append(r6);
        String r7 = Long.toString(r4, CharsKt.checkRadix(r10));
        Intrinsics.checkNotNullExpressionValue(r7, "toString(...)");
        return r3.append(r7).toString();
    }
}
