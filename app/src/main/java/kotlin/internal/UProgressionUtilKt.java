package kotlin.internal;

import androidx.media3.exoplayer.upstream.CmcdData;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport1;
import kotlin.UByte$$ExternalSyntheticBackport2;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UInt;
import kotlin.ULong;

/* JADX INFO: compiled from: UProgressionUtil.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0005\u0010\u0006\u001a'\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\b\u0010\t\u001a'\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001¢\u0006\u0004\b\u000f\u0010\u0006\u001a'\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001¢\u0006\u0004\b\u0011\u0010\t¨\u0006\u0012"}, m421d2 = {"differenceModulo", "Lkotlin/UInt;", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class UProgressionUtilKt {
    /* JADX INFO: renamed from: differenceModulo-WZ9TVnA, reason: not valid java name */
    private static final int m2025differenceModuloWZ9TVnA(int r3, int r4, int r5) {
        int r0 = UByte$$ExternalSyntheticBackport1.m430m(r3, r5);
        int r1 = UByte$$ExternalSyntheticBackport1.m430m(r4, r5);
        if (UByte$$ExternalSyntheticBackport4.m433m(r0, r1) < 0) goto L5;
        int r2 = r0 - r1;
    L7:
        return UInt.m914constructorimpl(r2);
    L5:
        r2 = UInt.m914constructorimpl(r0 - r1) + r5;
        goto L7
    }

    /* JADX INFO: renamed from: differenceModulo-sambcqE, reason: not valid java name */
    private static final long m2026differenceModulosambcqE(long r6, long r8, long r10) {
        long r0 = UByte$$ExternalSyntheticBackport2.m431m(r6, r10);
        long r2 = UByte$$ExternalSyntheticBackport2.m431m(r8, r10);
        if (C0871x7f2744bd.m371m(r0, r2) < 0) goto L5;
        long r4 = r0 - r2;
    L7:
        return ULong.m993constructorimpl(r4);
    L5:
        r4 = ULong.m993constructorimpl(r0 - r2) + r10;
        goto L7
    }

    /* JADX INFO: renamed from: getProgressionLastElement-7ftBX0g, reason: not valid java name */
    public static final long m2027getProgressionLastElement7ftBX0g(long r7, long r9, long r11) {
        if (r11 <= 0) goto L9;
        if (C0871x7f2744bd.m371m(r7, r9) < 0) goto L17;
    L18:
        return r9;
    L17:
        return ULong.m993constructorimpl(r9 - m2026differenceModulosambcqE(r9, r7, ULong.m993constructorimpl(r11)));
    L9:
        if (r11 >= 0) goto L16;
        if (C0871x7f2744bd.m371m(r7, r9) <= 0) goto L18;
        return ULong.m993constructorimpl(m2026differenceModulosambcqE(r7, r9, ULong.m993constructorimpl(-r11)) + r9);
    L16:
        throw new IllegalArgumentException("Step is zero.");
    }

    /* JADX INFO: renamed from: getProgressionLastElement-Nkh28Cs, reason: not valid java name */
    public static final int m2028getProgressionLastElementNkh28Cs(int r2, int r3, int r4) {
        if (r4 > 0) goto L4;
        if (r4 >= 0) goto L14;
        if (UByte$$ExternalSyntheticBackport4.m433m(r2, r3) > 0) goto L12;
    L16:
        return r3;
    L12:
        return UInt.m914constructorimpl(m2025differenceModuloWZ9TVnA(r2, r3, UInt.m914constructorimpl(-r4)) + r3);
    L14:
        throw new IllegalArgumentException("Step is zero.");
    L4:
        if (UByte$$ExternalSyntheticBackport4.m433m(r2, r3) >= 0) goto L16;
        return UInt.m914constructorimpl(r3 - m2025differenceModuloWZ9TVnA(r3, r2, UInt.m914constructorimpl(r4)));
    }
}
