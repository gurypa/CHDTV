package kotlin.random;

import com.arthenica.ffmpegkit.MediaInformation;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongRange;

/* JADX INFO: compiled from: URandom.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001f\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u0019\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007¢\u0006\u0002\u0010\u0010\u001a\u001b\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007¢\u0006\u0004\b\u0012\u0010\u0013\u001a/\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0011\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007¢\u0006\u0002\u0010\u0019\u001a\u001b\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0004\b\u001a\u0010\u001b\u001a#\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u0019\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007¢\u0006\u0002\u0010 \u001a\u0011\u0010!\u001a\u00020\b*\u00020\rH\u0007¢\u0006\u0002\u0010\"\u001a\u001b\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007¢\u0006\u0004\b#\u0010$\u001a#\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007¢\u0006\u0004\b%\u0010&\u001a\u0019\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007¢\u0006\u0002\u0010(¨\u0006)"}, m421d2 = {"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", MediaInformation.KEY_SIZE, "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class URandomKt {
    /* JADX INFO: renamed from: checkUIntRangeBounds-J1ME1BU, reason: not valid java name */
    public static final void m2033checkUIntRangeBoundsJ1ME1BU(int r3, int r4) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r4, r3) <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return;
    L9:
        throw new IllegalArgumentException(RandomKt.boundsErrorMessage(UInt.m908boximpl(r3), UInt.m908boximpl(r4)).toString());
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: checkULongRangeBounds-eb3DHEI, reason: not valid java name */
    public static final void m2034checkULongRangeBoundseb3DHEI(long r3, long r5) {
        if (C0871x7f2744bd.m371m(r5, r3) <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return;
    L9:
        throw new IllegalArgumentException(RandomKt.boundsErrorMessage(ULong.m987boximpl(r3), ULong.m987boximpl(r5)).toString());
    L5:
        r0 = false;
        goto L6
    }

    public static final byte[] nextUBytes(Random r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return UByteArray.m890constructorimpl(r1.nextBytes(r2));
    }

    /* JADX INFO: renamed from: nextUBytes-EVgfTAA, reason: not valid java name */
    public static final byte[] m2035nextUBytesEVgfTAA(Random r1, byte[] r2) {
        Intrinsics.checkNotNullParameter(r1, "$this$nextUBytes");
        Intrinsics.checkNotNullParameter(r2, "array");
        r1.nextBytes(r2);
        return r2;
    }

    /* JADX INFO: renamed from: nextUBytes-Wvrt4B4, reason: not valid java name */
    public static final byte[] m2036nextUBytesWvrt4B4(Random r1, byte[] r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r1, "$this$nextUBytes");
        Intrinsics.checkNotNullParameter(r2, "array");
        r1.nextBytes(r2, r3, r4);
        return r2;
    }

    /* JADX INFO: renamed from: nextUBytes-Wvrt4B4$default, reason: not valid java name */
    public static /* synthetic */ byte[] m2037nextUBytesWvrt4B4$default(Random r0, byte[] r1, int r2, int r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = UByteArray.m896getSizeimpl(r1);
    L9:
        return m2036nextUBytesWvrt4B4(r0, r1, r2, r3);
    }

    public static final int nextUInt(Random r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return UInt.m914constructorimpl(r1.nextInt());
    }

    public static final int nextUInt(Random r3, UIntRange r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "range");
        if (r4.isEmpty() == true) goto L13;
        if (UByte$$ExternalSyntheticBackport4.m433m(r4.m2043getLastpVg5ArA(), -1) >= 0) goto L8;
        return m2038nextUInta8DCA5k(r3, r4.m2042getFirstpVg5ArA(), UInt.m914constructorimpl(r4.m2043getLastpVg5ArA() + 1));
    L8:
        if (UByte$$ExternalSyntheticBackport4.m433m(r4.m2042getFirstpVg5ArA(), 0) <= 0) goto L11;
        return UInt.m914constructorimpl(m2038nextUInta8DCA5k(r3, UInt.m914constructorimpl(r4.m2042getFirstpVg5ArA() - 1), r4.m2043getLastpVg5ArA()) + 1);
    L11:
        return nextUInt(r3);
    L13:
        throw new IllegalArgumentException("Cannot get random in empty range: " + r4);
    }

    /* JADX INFO: renamed from: nextUInt-a8DCA5k, reason: not valid java name */
    public static final int m2038nextUInta8DCA5k(Random r4, int r5, int r6) {
        Intrinsics.checkNotNullParameter(r4, "$this$nextUInt");
        m2033checkUIntRangeBoundsJ1ME1BU(r5, r6);
        return UInt.m914constructorimpl(Integer.MIN_VALUE ^ r4.nextInt(r5 ^ Integer.MIN_VALUE, r6 ^ Integer.MIN_VALUE));
    }

    /* JADX INFO: renamed from: nextUInt-qCasIEU, reason: not valid java name */
    public static final int m2039nextUIntqCasIEU(Random r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "$this$nextUInt");
        return m2038nextUInta8DCA5k(r1, 0, r2);
    }

    public static final long nextULong(Random r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return ULong.m993constructorimpl(r2.nextLong());
    }

    public static final long nextULong(Random r10, ULongRange r11) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        Intrinsics.checkNotNullParameter(r11, "range");
        if (r11.isEmpty() == true) goto L13;
        if (C0871x7f2744bd.m371m(r11.m2052getLastsVKNKU(), -1) >= 0) goto L8;
        return m2041nextULongjmpaWc(r10, r11.m2051getFirstsVKNKU(), ULong.m993constructorimpl(r11.m2052getLastsVKNKU() + ULong.m993constructorimpl(4294967295L & ((long) 1))));
    L8:
        if (C0871x7f2744bd.m371m(r11.m2051getFirstsVKNKU(), 0) <= 0) goto L11;
        long r1 = 4294967295L & ((long) 1);
        return ULong.m993constructorimpl(m2041nextULongjmpaWc(r10, ULong.m993constructorimpl(r11.m2051getFirstsVKNKU() - ULong.m993constructorimpl(r1)), r11.m2052getLastsVKNKU()) + ULong.m993constructorimpl(r1));
    L11:
        return nextULong(r10);
    L13:
        throw new IllegalArgumentException("Cannot get random in empty range: " + r11);
    }

    /* JADX INFO: renamed from: nextULong-V1Xi4fY, reason: not valid java name */
    public static final long m2040nextULongV1Xi4fY(Random r2, long r3) {
        Intrinsics.checkNotNullParameter(r2, "$this$nextULong");
        return m2041nextULongjmpaWc(r2, 0, r3);
    }

    /* JADX INFO: renamed from: nextULong-jmpaW-c, reason: not valid java name */
    public static final long m2041nextULongjmpaWc(Random r8, long r9, long r11) {
        Intrinsics.checkNotNullParameter(r8, "$this$nextULong");
        m2034checkULongRangeBoundseb3DHEI(r9, r11);
        return ULong.m993constructorimpl(Long.MIN_VALUE ^ r8.nextLong(r9 ^ Long.MIN_VALUE, r11 ^ Long.MIN_VALUE));
    }
}
