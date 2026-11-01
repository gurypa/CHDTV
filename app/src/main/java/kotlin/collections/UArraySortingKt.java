package kotlin.collections;

import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UArraySorting.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0006\u0010\u0007\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\t\u0010\n\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\f\u0010\r\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u000f\u0010\u0010\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0013\u0010\u0014\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0015\u0010\u0016\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0017\u0010\u0018\u001a'\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003¢\u0006\u0004\b\u0019\u0010\u001a\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001e\u0010\u0014\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u001f\u0010\u0016\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b \u0010\u0018\u001a'\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001¢\u0006\u0004\b!\u0010\u001a¨\u0006\""}, m421d2 = {"partition", "", "array", "Lkotlin/UByteArray;", TtmlNode.LEFT, TtmlNode.RIGHT, "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class UArraySortingKt {
    /* JADX INFO: renamed from: partition--nroSd4, reason: not valid java name */
    private static final int m1274partitionnroSd4(long[] r8, int r9, int r10) {
        int r0 = r9;
        int r1 = r10;
        long r2 = ULongArray.m1053getsVKNKU(r8, (r9 + r10) / 2);
    L3:
        if (r0 > r1) goto L12;
    L5:
        if (C0871x7f2744bd.m371m(ULongArray.m1053getsVKNKU(r8, r0), r2) >= 0) goto L8;
        r0 = r0 + 1;
    L8:
        if (C0871x7f2744bd.m371m(ULongArray.m1053getsVKNKU(r8, r1), r2) <= 0) goto L10;
        r1 = r1 - 1;
        goto L8
    L10:
        if (r0 > r1) goto L3;
        long r4 = ULongArray.m1053getsVKNKU(r8, r0);
        ULongArray.m1058setk8EXiF4(r8, r0, ULongArray.m1053getsVKNKU(r8, r1));
        ULongArray.m1058setk8EXiF4(r8, r1, r4);
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    L12:
        return r0;
    }

    /* JADX INFO: renamed from: partition-4UcCI2c, reason: not valid java name */
    private static final int m1275partition4UcCI2c(byte[] r5, int r6, int r7) {
        int r0 = r6;
        int r1 = r7;
        byte r2 = UByteArray.m895getw2LRezQ(r5, (r6 + r7) / 2);
    L3:
        if (r0 > r1) goto L12;
    L5:
        if (Intrinsics.compare(UByteArray.m895getw2LRezQ(r5, r0) & 255, r2 & 255) >= 0) goto L8;
        r0 = r0 + 1;
    L8:
        if (Intrinsics.compare(UByteArray.m895getw2LRezQ(r5, r1) & 255, r2 & 255) <= 0) goto L10;
        r1 = r1 - 1;
        goto L8
    L10:
        if (r0 > r1) goto L3;
        byte r3 = UByteArray.m895getw2LRezQ(r5, r0);
        UByteArray.m900setVurrAj0(r5, r0, UByteArray.m895getw2LRezQ(r5, r1));
        UByteArray.m900setVurrAj0(r5, r1, r3);
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    L12:
        return r0;
    }

    /* JADX INFO: renamed from: partition-Aa5vz7o, reason: not valid java name */
    private static final int m1276partitionAa5vz7o(short[] r6, int r7, int r8) {
        int r0 = r7;
        int r1 = r8;
        short r2 = UShortArray.m1158getMh2AYeg(r6, (r7 + r8) / 2);
    L3:
        if (r0 > r1) goto L12;
    L5:
        if (Intrinsics.compare(UShortArray.m1158getMh2AYeg(r6, r0) & UShort.MAX_VALUE, r2 & UShort.MAX_VALUE) >= 0) goto L8;
        r0 = r0 + 1;
    L8:
        if (Intrinsics.compare(UShortArray.m1158getMh2AYeg(r6, r1) & UShort.MAX_VALUE, r2 & UShort.MAX_VALUE) <= 0) goto L10;
        r1 = r1 - 1;
        goto L8
    L10:
        if (r0 > r1) goto L3;
        short r3 = UShortArray.m1158getMh2AYeg(r6, r0);
        UShortArray.m1163set01HTLdE(r6, r0, UShortArray.m1158getMh2AYeg(r6, r1));
        UShortArray.m1163set01HTLdE(r6, r1, r3);
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    L12:
        return r0;
    }

    /* JADX INFO: renamed from: partition-oBK06Vg, reason: not valid java name */
    private static final int m1277partitionoBK06Vg(int[] r5, int r6, int r7) {
        int r0 = r6;
        int r1 = r7;
        int r2 = UIntArray.m974getpVg5ArA(r5, (r6 + r7) / 2);
    L3:
        if (r0 > r1) goto L12;
    L5:
        if (UByte$$ExternalSyntheticBackport4.m433m(UIntArray.m974getpVg5ArA(r5, r0), r2) >= 0) goto L8;
        r0 = r0 + 1;
    L8:
        if (UByte$$ExternalSyntheticBackport4.m433m(UIntArray.m974getpVg5ArA(r5, r1), r2) <= 0) goto L10;
        r1 = r1 - 1;
        goto L8
    L10:
        if (r0 > r1) goto L3;
        int r3 = UIntArray.m974getpVg5ArA(r5, r0);
        UIntArray.m979setVXSXFK8(r5, r0, UIntArray.m974getpVg5ArA(r5, r1));
        UIntArray.m979setVXSXFK8(r5, r1, r3);
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    L12:
        return r0;
    }

    /* JADX INFO: renamed from: quickSort--nroSd4, reason: not valid java name */
    private static final void m1278quickSortnroSd4(long[] r2, int r3, int r4) {
        int r0 = m1274partitionnroSd4(r2, r3, r4);
        if (r3 >= (r0 - 1)) goto L5;
        m1278quickSortnroSd4(r2, r3, r0 - 1);
    L5:
        if (r0 >= r4) goto L8;
        m1278quickSortnroSd4(r2, r0, r4);
        return;
    }

    /* JADX INFO: renamed from: quickSort-4UcCI2c, reason: not valid java name */
    private static final void m1279quickSort4UcCI2c(byte[] r2, int r3, int r4) {
        int r0 = m1275partition4UcCI2c(r2, r3, r4);
        if (r3 >= (r0 - 1)) goto L5;
        m1279quickSort4UcCI2c(r2, r3, r0 - 1);
    L5:
        if (r0 >= r4) goto L8;
        m1279quickSort4UcCI2c(r2, r0, r4);
        return;
    }

    /* JADX INFO: renamed from: quickSort-Aa5vz7o, reason: not valid java name */
    private static final void m1280quickSortAa5vz7o(short[] r2, int r3, int r4) {
        int r0 = m1276partitionAa5vz7o(r2, r3, r4);
        if (r3 >= (r0 - 1)) goto L5;
        m1280quickSortAa5vz7o(r2, r3, r0 - 1);
    L5:
        if (r0 >= r4) goto L8;
        m1280quickSortAa5vz7o(r2, r0, r4);
        return;
    }

    /* JADX INFO: renamed from: quickSort-oBK06Vg, reason: not valid java name */
    private static final void m1281quickSortoBK06Vg(int[] r2, int r3, int r4) {
        int r0 = m1277partitionoBK06Vg(r2, r3, r4);
        if (r3 >= (r0 - 1)) goto L5;
        m1281quickSortoBK06Vg(r2, r3, r0 - 1);
    L5:
        if (r0 >= r4) goto L8;
        m1281quickSortoBK06Vg(r2, r0, r4);
        return;
    }

    /* JADX INFO: renamed from: sortArray--nroSd4, reason: not valid java name */
    public static final void m1282sortArraynroSd4(long[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "array");
        m1278quickSortnroSd4(r1, r2, r3 - 1);
    }

    /* JADX INFO: renamed from: sortArray-4UcCI2c, reason: not valid java name */
    public static final void m1283sortArray4UcCI2c(byte[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "array");
        m1279quickSort4UcCI2c(r1, r2, r3 - 1);
    }

    /* JADX INFO: renamed from: sortArray-Aa5vz7o, reason: not valid java name */
    public static final void m1284sortArrayAa5vz7o(short[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "array");
        m1280quickSortAa5vz7o(r1, r2, r3 - 1);
    }

    /* JADX INFO: renamed from: sortArray-oBK06Vg, reason: not valid java name */
    public static final void m1285sortArrayoBK06Vg(int[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "array");
        m1281quickSortoBK06Vg(r1, r2, r3 - 1);
    }
}
