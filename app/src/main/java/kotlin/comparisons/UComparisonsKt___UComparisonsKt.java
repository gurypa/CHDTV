package kotlin.comparisons;

import androidx.media3.exoplayer.upstream.CmcdData;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: _UComparisons.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0010\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a(\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b\u0007\u0010\b\u001a#\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\n\u0010\t\u001a\u00020\n\"\u00020\u0001H\u0007¢\u0006\u0004\b\u000b\u0010\f\u001a\u001f\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0007¢\u0006\u0004\b\u000e\u0010\u000f\u001a(\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¢\u0006\u0004\b\u0010\u0010\u0011\u001a#\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\n\u0010\t\u001a\u00020\u0012\"\u00020\rH\u0007¢\u0006\u0004\b\u0013\u0010\u0014\u001a\u001f\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u0015H\u0007¢\u0006\u0004\b\u0016\u0010\u0017\u001a(\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b¢\u0006\u0004\b\u0018\u0010\u0019\u001a#\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\n\u0010\t\u001a\u00020\u001a\"\u00020\u0015H\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a\u001f\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001dH\u0007¢\u0006\u0004\b\u001e\u0010\u001f\u001a(\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001d2\u0006\u0010\u0006\u001a\u00020\u001dH\u0087\b¢\u0006\u0004\b \u0010!\u001a#\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\n\u0010\t\u001a\u00020\"\"\u00020\u001dH\u0007¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007¢\u0006\u0004\b&\u0010\u0005\u001a(\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b'\u0010\b\u001a#\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\n\u0010\t\u001a\u00020\n\"\u00020\u0001H\u0007¢\u0006\u0004\b(\u0010\f\u001a\u001f\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0007¢\u0006\u0004\b)\u0010\u000f\u001a(\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¢\u0006\u0004\b*\u0010\u0011\u001a#\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\n\u0010\t\u001a\u00020\u0012\"\u00020\rH\u0007¢\u0006\u0004\b+\u0010\u0014\u001a\u001f\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u0015H\u0007¢\u0006\u0004\b,\u0010\u0017\u001a(\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b¢\u0006\u0004\b-\u0010\u0019\u001a#\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\n\u0010\t\u001a\u00020\u001a\"\u00020\u0015H\u0007¢\u0006\u0004\b.\u0010\u001c\u001a\u001f\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001dH\u0007¢\u0006\u0004\b/\u0010\u001f\u001a(\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001d2\u0006\u0010\u0006\u001a\u00020\u001dH\u0087\b¢\u0006\u0004\b0\u0010!\u001a#\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\n\u0010\t\u001a\u00020\"\"\u00020\u001dH\u0007¢\u0006\u0004\b1\u0010$¨\u00062"}, m421d2 = {"maxOf", "Lkotlin/UByte;", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "maxOf-Kr8caGY", "(BB)B", "c", "maxOf-b33U2AM", "(BBB)B", "other", "Lkotlin/UByteArray;", "maxOf-Wr6uiD8", "(B[B)B", "Lkotlin/UInt;", "maxOf-J1ME1BU", "(II)I", "maxOf-WZ9TVnA", "(III)I", "Lkotlin/UIntArray;", "maxOf-Md2H83M", "(I[I)I", "Lkotlin/ULong;", "maxOf-eb3DHEI", "(JJ)J", "maxOf-sambcqE", "(JJJ)J", "Lkotlin/ULongArray;", "maxOf-R03FKyM", "(J[J)J", "Lkotlin/UShort;", "maxOf-5PvTz6A", "(SS)S", "maxOf-VKSA0NQ", "(SSS)S", "Lkotlin/UShortArray;", "maxOf-t1qELG4", "(S[S)S", "minOf", "minOf-Kr8caGY", "minOf-b33U2AM", "minOf-Wr6uiD8", "minOf-J1ME1BU", "minOf-WZ9TVnA", "minOf-Md2H83M", "minOf-eb3DHEI", "minOf-sambcqE", "minOf-R03FKyM", "minOf-5PvTz6A", "minOf-VKSA0NQ", "minOf-t1qELG4", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/comparisons/UComparisonsKt")
class UComparisonsKt___UComparisonsKt {
    public UComparisonsKt___UComparisonsKt() {
    }

    /* JADX INFO: renamed from: maxOf-5PvTz6A, reason: not valid java name */
    public static final short m2000maxOf5PvTz6A(short r2, short r3) {
        if (Intrinsics.compare(r2 & UShort.MAX_VALUE, 65535 & r3) < 0) goto L6;
        return r2;
    L6:
        return r3;
    }

    /* JADX INFO: renamed from: maxOf-J1ME1BU, reason: not valid java name */
    public static final int m2001maxOfJ1ME1BU(int r1, int r2) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r1, r2) < 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: maxOf-Kr8caGY, reason: not valid java name */
    public static final byte m2002maxOfKr8caGY(byte r2, byte r3) {
        if (Intrinsics.compare(r2 & 255, r3 & 255) < 0) goto L6;
        return r2;
    L6:
        return r3;
    }

    /* JADX INFO: renamed from: maxOf-Md2H83M, reason: not valid java name */
    public static final int m2003maxOfMd2H83M(int r4, int... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        int r0 = r4;
        int r1 = UIntArray.m975getSizeimpl(r5);
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = UComparisonsKt.m2001maxOfJ1ME1BU(r0, UIntArray.m974getpVg5ArA(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: maxOf-R03FKyM, reason: not valid java name */
    public static final long m2004maxOfR03FKyM(long r6, long... r8) {
        Intrinsics.checkNotNullParameter(r8, "other");
        long r0 = r6;
        int r2 = ULongArray.m1054getSizeimpl(r8);
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = UComparisonsKt.m2009maxOfeb3DHEI(r0, ULongArray.m1053getsVKNKU(r8, r3));
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: maxOf-VKSA0NQ, reason: not valid java name */
    private static final short m2005maxOfVKSA0NQ(short r1, short r2, short r3) {
        return UComparisonsKt.m2000maxOf5PvTz6A(r1, UComparisonsKt.m2000maxOf5PvTz6A(r2, r3));
    }

    /* JADX INFO: renamed from: maxOf-WZ9TVnA, reason: not valid java name */
    private static final int m2006maxOfWZ9TVnA(int r1, int r2, int r3) {
        return UComparisonsKt.m2001maxOfJ1ME1BU(r1, UComparisonsKt.m2001maxOfJ1ME1BU(r2, r3));
    }

    /* JADX INFO: renamed from: maxOf-Wr6uiD8, reason: not valid java name */
    public static final byte m2007maxOfWr6uiD8(byte r4, byte... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        byte r0 = r4;
        int r1 = UByteArray.m896getSizeimpl(r5);
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = UComparisonsKt.m2002maxOfKr8caGY(r0, UByteArray.m895getw2LRezQ(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: maxOf-b33U2AM, reason: not valid java name */
    private static final byte m2008maxOfb33U2AM(byte r1, byte r2, byte r3) {
        return UComparisonsKt.m2002maxOfKr8caGY(r1, UComparisonsKt.m2002maxOfKr8caGY(r2, r3));
    }

    /* JADX INFO: renamed from: maxOf-eb3DHEI, reason: not valid java name */
    public static final long m2009maxOfeb3DHEI(long r2, long r4) {
        if (C0871x7f2744bd.m371m(r2, r4) < 0) goto L6;
        return r2;
    L6:
        return r4;
    }

    /* JADX INFO: renamed from: maxOf-sambcqE, reason: not valid java name */
    private static final long m2010maxOfsambcqE(long r2, long r4, long r6) {
        return UComparisonsKt.m2009maxOfeb3DHEI(r2, UComparisonsKt.m2009maxOfeb3DHEI(r4, r6));
    }

    /* JADX INFO: renamed from: maxOf-t1qELG4, reason: not valid java name */
    public static final short m2011maxOft1qELG4(short r4, short... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        short r0 = r4;
        int r1 = UShortArray.m1159getSizeimpl(r5);
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = UComparisonsKt.m2000maxOf5PvTz6A(r0, UShortArray.m1158getMh2AYeg(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: minOf-5PvTz6A, reason: not valid java name */
    public static final short m2012minOf5PvTz6A(short r2, short r3) {
        if (Intrinsics.compare(r2 & UShort.MAX_VALUE, 65535 & r3) > 0) goto L6;
        return r2;
    L6:
        return r3;
    }

    /* JADX INFO: renamed from: minOf-J1ME1BU, reason: not valid java name */
    public static final int m2013minOfJ1ME1BU(int r1, int r2) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r1, r2) > 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: minOf-Kr8caGY, reason: not valid java name */
    public static final byte m2014minOfKr8caGY(byte r2, byte r3) {
        if (Intrinsics.compare(r2 & 255, r3 & 255) > 0) goto L6;
        return r2;
    L6:
        return r3;
    }

    /* JADX INFO: renamed from: minOf-Md2H83M, reason: not valid java name */
    public static final int m2015minOfMd2H83M(int r4, int... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        int r0 = r4;
        int r1 = UIntArray.m975getSizeimpl(r5);
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = UComparisonsKt.m2013minOfJ1ME1BU(r0, UIntArray.m974getpVg5ArA(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: minOf-R03FKyM, reason: not valid java name */
    public static final long m2016minOfR03FKyM(long r6, long... r8) {
        Intrinsics.checkNotNullParameter(r8, "other");
        long r0 = r6;
        int r2 = ULongArray.m1054getSizeimpl(r8);
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = UComparisonsKt.m2021minOfeb3DHEI(r0, ULongArray.m1053getsVKNKU(r8, r3));
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: minOf-VKSA0NQ, reason: not valid java name */
    private static final short m2017minOfVKSA0NQ(short r1, short r2, short r3) {
        return UComparisonsKt.m2012minOf5PvTz6A(r1, UComparisonsKt.m2012minOf5PvTz6A(r2, r3));
    }

    /* JADX INFO: renamed from: minOf-WZ9TVnA, reason: not valid java name */
    private static final int m2018minOfWZ9TVnA(int r1, int r2, int r3) {
        return UComparisonsKt.m2013minOfJ1ME1BU(r1, UComparisonsKt.m2013minOfJ1ME1BU(r2, r3));
    }

    /* JADX INFO: renamed from: minOf-Wr6uiD8, reason: not valid java name */
    public static final byte m2019minOfWr6uiD8(byte r4, byte... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        byte r0 = r4;
        int r1 = UByteArray.m896getSizeimpl(r5);
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = UComparisonsKt.m2014minOfKr8caGY(r0, UByteArray.m895getw2LRezQ(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    /* JADX INFO: renamed from: minOf-b33U2AM, reason: not valid java name */
    private static final byte m2020minOfb33U2AM(byte r1, byte r2, byte r3) {
        return UComparisonsKt.m2014minOfKr8caGY(r1, UComparisonsKt.m2014minOfKr8caGY(r2, r3));
    }

    /* JADX INFO: renamed from: minOf-eb3DHEI, reason: not valid java name */
    public static final long m2021minOfeb3DHEI(long r2, long r4) {
        if (C0871x7f2744bd.m371m(r2, r4) > 0) goto L6;
        return r2;
    L6:
        return r4;
    }

    /* JADX INFO: renamed from: minOf-sambcqE, reason: not valid java name */
    private static final long m2022minOfsambcqE(long r2, long r4, long r6) {
        return UComparisonsKt.m2021minOfeb3DHEI(r2, UComparisonsKt.m2021minOfeb3DHEI(r4, r6));
    }

    /* JADX INFO: renamed from: minOf-t1qELG4, reason: not valid java name */
    public static final short m2023minOft1qELG4(short r4, short... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        short r0 = r4;
        int r1 = UShortArray.m1159getSizeimpl(r5);
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = UComparisonsKt.m2012minOf5PvTz6A(r0, UShortArray.m1158getMh2AYeg(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }
}
