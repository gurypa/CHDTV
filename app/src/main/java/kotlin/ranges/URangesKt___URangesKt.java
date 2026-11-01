package kotlin.ranges;

import com.google.common.primitives.C0871x7f2744bd;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.URandomKt;

/* JADX INFO: compiled from: _URanges.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\n\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u001b\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u001b\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a\u001b\u0010\u0000\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000bH\u0007¢\u0006\u0004\b\f\u0010\r\u001a\u001b\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0010\u0010\u0004\u001a\u001b\u0010\u000e\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0011\u0010\u0007\u001a\u001b\u0010\u000e\u001a\u00020\b*\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007¢\u0006\u0004\b\u0012\u0010\n\u001a\u001b\u0010\u000e\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007¢\u0006\u0004\b\u0013\u0010\r\u001a#\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0015\u0010\u0016\u001a#\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0017\u0010\u0018\u001a!\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001aH\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a#\u0010\u0014\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007¢\u0006\u0004\b\u001d\u0010\u001e\u001a!\u0010\u0014\u001a\u00020\b*\u00020\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001aH\u0007¢\u0006\u0004\b\u001f\u0010 \u001a#\u0010\u0014\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007¢\u0006\u0004\b!\u0010\"\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002¢\u0006\u0004\b'\u0010(\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\b\u0010)\u001a\u0004\u0018\u00010\u0005H\u0087\n¢\u0006\u0002\b*\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\bH\u0087\u0002¢\u0006\u0004\b+\u0010,\u001a\u001c\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002¢\u0006\u0004\b-\u0010.\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002¢\u0006\u0004\b0\u00101\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0004\b2\u00103\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\b\u0010)\u001a\u0004\u0018\u00010\bH\u0087\n¢\u0006\u0002\b4\u001a\u001c\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002¢\u0006\u0004\b5\u00106\u001a\u001c\u00107\u001a\u000208*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004¢\u0006\u0004\b:\u0010;\u001a\u001c\u00107\u001a\u000208*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004¢\u0006\u0004\b<\u0010=\u001a\u001c\u00107\u001a\u00020>*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004¢\u0006\u0004\b?\u0010@\u001a\u001c\u00107\u001a\u000208*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004¢\u0006\u0004\bA\u0010B\u001a\u0011\u0010C\u001a\u00020\u0005*\u000208H\u0007¢\u0006\u0002\u0010D\u001a\u0011\u0010C\u001a\u00020\b*\u00020>H\u0007¢\u0006\u0002\u0010E\u001a\u000e\u0010F\u001a\u0004\u0018\u00010\u0005*\u000208H\u0007\u001a\u000e\u0010F\u001a\u0004\u0018\u00010\b*\u00020>H\u0007\u001a\u0011\u0010G\u001a\u00020\u0005*\u000208H\u0007¢\u0006\u0002\u0010D\u001a\u0011\u0010G\u001a\u00020\b*\u00020>H\u0007¢\u0006\u0002\u0010E\u001a\u000e\u0010H\u001a\u0004\u0018\u00010\u0005*\u000208H\u0007\u001a\u000e\u0010H\u001a\u0004\u0018\u00010\b*\u00020>H\u0007\u001a\u0012\u0010I\u001a\u00020\u0005*\u00020%H\u0087\b¢\u0006\u0002\u0010J\u001a\u0019\u0010I\u001a\u00020\u0005*\u00020%2\u0006\u0010I\u001a\u00020KH\u0007¢\u0006\u0002\u0010L\u001a\u0012\u0010I\u001a\u00020\b*\u00020/H\u0087\b¢\u0006\u0002\u0010M\u001a\u0019\u0010I\u001a\u00020\b*\u00020/2\u0006\u0010I\u001a\u00020KH\u0007¢\u0006\u0002\u0010N\u001a\u000f\u0010O\u001a\u0004\u0018\u00010\u0005*\u00020%H\u0087\b\u001a\u0016\u0010O\u001a\u0004\u0018\u00010\u0005*\u00020%2\u0006\u0010I\u001a\u00020KH\u0007\u001a\u000f\u0010O\u001a\u0004\u0018\u00010\b*\u00020/H\u0087\b\u001a\u0016\u0010O\u001a\u0004\u0018\u00010\b*\u00020/2\u0006\u0010I\u001a\u00020KH\u0007\u001a\f\u0010P\u001a\u000208*\u000208H\u0007\u001a\f\u0010P\u001a\u00020>*\u00020>H\u0007\u001a\u0015\u0010Q\u001a\u000208*\u0002082\u0006\u0010Q\u001a\u00020RH\u0087\u0004\u001a\u0015\u0010Q\u001a\u00020>*\u00020>2\u0006\u0010Q\u001a\u00020SH\u0087\u0004\u001a\u001c\u0010T\u001a\u00020%*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004¢\u0006\u0004\bU\u0010V\u001a\u001c\u0010T\u001a\u00020%*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004¢\u0006\u0004\bW\u0010X\u001a\u001c\u0010T\u001a\u00020/*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004¢\u0006\u0004\bY\u0010Z\u001a\u001c\u0010T\u001a\u00020%*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004¢\u0006\u0004\b[\u0010\\¨\u0006]"}, m421d2 = {"coerceAtLeast", "Lkotlin/UByte;", "minimumValue", "coerceAtLeast-Kr8caGY", "(BB)B", "Lkotlin/UInt;", "coerceAtLeast-J1ME1BU", "(II)I", "Lkotlin/ULong;", "coerceAtLeast-eb3DHEI", "(JJ)J", "Lkotlin/UShort;", "coerceAtLeast-5PvTz6A", "(SS)S", "coerceAtMost", "maximumValue", "coerceAtMost-Kr8caGY", "coerceAtMost-J1ME1BU", "coerceAtMost-eb3DHEI", "coerceAtMost-5PvTz6A", "coerceIn", "coerceIn-b33U2AM", "(BBB)B", "coerceIn-WZ9TVnA", "(III)I", "range", "Lkotlin/ranges/ClosedRange;", "coerceIn-wuiCnnA", "(ILkotlin/ranges/ClosedRange;)I", "coerceIn-sambcqE", "(JJJ)J", "coerceIn-JPwROB0", "(JLkotlin/ranges/ClosedRange;)J", "coerceIn-VKSA0NQ", "(SSS)S", "contains", "", "Lkotlin/ranges/UIntRange;", "value", "contains-68kG9v0", "(Lkotlin/ranges/UIntRange;B)Z", "element", "contains-biwQdVI", "contains-fz5IDCE", "(Lkotlin/ranges/UIntRange;J)Z", "contains-ZsK3CEQ", "(Lkotlin/ranges/UIntRange;S)Z", "Lkotlin/ranges/ULongRange;", "contains-ULb-yJY", "(Lkotlin/ranges/ULongRange;B)Z", "contains-Gab390E", "(Lkotlin/ranges/ULongRange;I)Z", "contains-GYNo2lE", "contains-uhHAxoY", "(Lkotlin/ranges/ULongRange;S)Z", "downTo", "Lkotlin/ranges/UIntProgression;", "to", "downTo-Kr8caGY", "(BB)Lkotlin/ranges/UIntProgression;", "downTo-J1ME1BU", "(II)Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ULongProgression;", "downTo-eb3DHEI", "(JJ)Lkotlin/ranges/ULongProgression;", "downTo-5PvTz6A", "(SS)Lkotlin/ranges/UIntProgression;", "first", "(Lkotlin/ranges/UIntProgression;)I", "(Lkotlin/ranges/ULongProgression;)J", "firstOrNull", "last", "lastOrNull", "random", "(Lkotlin/ranges/UIntRange;)I", "Lkotlin/random/Random;", "(Lkotlin/ranges/UIntRange;Lkotlin/random/Random;)I", "(Lkotlin/ranges/ULongRange;)J", "(Lkotlin/ranges/ULongRange;Lkotlin/random/Random;)J", "randomOrNull", "reversed", "step", "", "", "until", "until-Kr8caGY", "(BB)Lkotlin/ranges/UIntRange;", "until-J1ME1BU", "(II)Lkotlin/ranges/UIntRange;", "until-eb3DHEI", "(JJ)Lkotlin/ranges/ULongRange;", "until-5PvTz6A", "(SS)Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/ranges/URangesKt")
class URangesKt___URangesKt {
    public URangesKt___URangesKt() {
    }

    /* JADX INFO: renamed from: coerceAtLeast-5PvTz6A, reason: not valid java name */
    public static final short m2060coerceAtLeast5PvTz6A(short r2, short r3) {
        if (Intrinsics.compare(r2 & UShort.MAX_VALUE, 65535 & r3) >= 0) goto L6;
        return r3;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: coerceAtLeast-J1ME1BU, reason: not valid java name */
    public static final int m2061coerceAtLeastJ1ME1BU(int r1, int r2) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r1, r2) >= 0) goto L6;
        return r2;
    L6:
        return r1;
    }

    /* JADX INFO: renamed from: coerceAtLeast-Kr8caGY, reason: not valid java name */
    public static final byte m2062coerceAtLeastKr8caGY(byte r2, byte r3) {
        if (Intrinsics.compare(r2 & 255, r3 & 255) >= 0) goto L6;
        return r3;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: coerceAtLeast-eb3DHEI, reason: not valid java name */
    public static final long m2063coerceAtLeasteb3DHEI(long r2, long r4) {
        if (C0871x7f2744bd.m371m(r2, r4) >= 0) goto L6;
        return r4;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: coerceAtMost-5PvTz6A, reason: not valid java name */
    public static final short m2064coerceAtMost5PvTz6A(short r2, short r3) {
        if (Intrinsics.compare(r2 & UShort.MAX_VALUE, 65535 & r3) <= 0) goto L6;
        return r3;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: coerceAtMost-J1ME1BU, reason: not valid java name */
    public static final int m2065coerceAtMostJ1ME1BU(int r1, int r2) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r1, r2) <= 0) goto L6;
        return r2;
    L6:
        return r1;
    }

    /* JADX INFO: renamed from: coerceAtMost-Kr8caGY, reason: not valid java name */
    public static final byte m2066coerceAtMostKr8caGY(byte r2, byte r3) {
        if (Intrinsics.compare(r2 & 255, r3 & 255) <= 0) goto L6;
        return r3;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: coerceAtMost-eb3DHEI, reason: not valid java name */
    public static final long m2067coerceAtMosteb3DHEI(long r2, long r4) {
        if (C0871x7f2744bd.m371m(r2, r4) <= 0) goto L6;
        return r4;
    L6:
        return r2;
    }

    /* JADX INFO: renamed from: coerceIn-JPwROB0, reason: not valid java name */
    public static final long m2068coerceInJPwROB0(long r3, ClosedRange<ULong> r5) {
        Intrinsics.checkNotNullParameter(r5, "range");
        if ((r5 instanceof ClosedFloatingPointRange) == false) goto L7;
        return ((ULong) RangesKt.coerceIn(ULong.m987boximpl(r3), (ClosedFloatingPointRange) r5)).m1045unboximpl();
    L7:
        if (r5.isEmpty() == true) goto L17;
        if (C0871x7f2744bd.m371m(r3, ((ULong) r5.getStart()).m1045unboximpl()) >= 0) goto L12;
        return ((ULong) r5.getStart()).m1045unboximpl();
    L12:
        if (C0871x7f2744bd.m371m(r3, ((ULong) r5.getEndInclusive()).m1045unboximpl()) <= 0) goto L15;
        return ((ULong) r5.getEndInclusive()).m1045unboximpl();
    L15:
        return r3;
    L17:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + r5 + '.');
    }

    /* JADX INFO: renamed from: coerceIn-VKSA0NQ, reason: not valid java name */
    public static final short m2069coerceInVKSA0NQ(short r3, short r4, short r5) {
        if (Intrinsics.compare(r4 & UShort.MAX_VALUE, r5 & UShort.MAX_VALUE) > 0) goto L12;
        if (Intrinsics.compare(r3 & UShort.MAX_VALUE, r4 & UShort.MAX_VALUE) >= 0) goto L8;
        return r4;
    L8:
        if (Intrinsics.compare(r3 & UShort.MAX_VALUE, 65535 & r5) <= 0) goto L10;
        return r5;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UShort.m1144toStringimpl(r5) + " is less than minimum " + UShort.m1144toStringimpl(r4) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-WZ9TVnA, reason: not valid java name */
    public static final int m2070coerceInWZ9TVnA(int r3, int r4, int r5) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r4, r5) > 0) goto L12;
        if (UByte$$ExternalSyntheticBackport4.m433m(r3, r4) >= 0) goto L8;
        return r4;
    L8:
        if (UByte$$ExternalSyntheticBackport4.m433m(r3, r5) <= 0) goto L10;
        return r5;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UInt.m960toStringimpl(r5) + " is less than minimum " + UInt.m960toStringimpl(r4) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-b33U2AM, reason: not valid java name */
    public static final byte m2071coerceInb33U2AM(byte r3, byte r4, byte r5) {
        if (Intrinsics.compare(r4 & 255, r5 & 255) > 0) goto L12;
        if (Intrinsics.compare(r3 & 255, r4 & 255) >= 0) goto L8;
        return r4;
    L8:
        if (Intrinsics.compare(r3 & 255, r5 & 255) <= 0) goto L10;
        return r5;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UByte.m881toStringimpl(r5) + " is less than minimum " + UByte.m881toStringimpl(r4) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-sambcqE, reason: not valid java name */
    public static final long m2072coerceInsambcqE(long r3, long r5, long r7) {
        if (C0871x7f2744bd.m371m(r5, r7) > 0) goto L12;
        if (C0871x7f2744bd.m371m(r3, r5) >= 0) goto L8;
        return r5;
    L8:
        if (C0871x7f2744bd.m371m(r3, r7) <= 0) goto L10;
        return r7;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ULong.m1039toStringimpl(r7) + " is less than minimum " + ULong.m1039toStringimpl(r5) + '.');
    }

    /* JADX INFO: renamed from: coerceIn-wuiCnnA, reason: not valid java name */
    public static final int m2073coerceInwuiCnnA(int r3, ClosedRange<UInt> r4) {
        Intrinsics.checkNotNullParameter(r4, "range");
        if ((r4 instanceof ClosedFloatingPointRange) == false) goto L7;
        return ((UInt) RangesKt.coerceIn(UInt.m908boximpl(r3), (ClosedFloatingPointRange) r4)).m966unboximpl();
    L7:
        if (r4.isEmpty() == true) goto L17;
        if (UByte$$ExternalSyntheticBackport4.m433m(r3, ((UInt) r4.getStart()).m966unboximpl()) >= 0) goto L12;
        return ((UInt) r4.getStart()).m966unboximpl();
    L12:
        if (UByte$$ExternalSyntheticBackport4.m433m(r3, ((UInt) r4.getEndInclusive()).m966unboximpl()) <= 0) goto L15;
        return ((UInt) r4.getEndInclusive()).m966unboximpl();
    L15:
        return r3;
    L17:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + r4 + '.');
    }

    /* JADX INFO: renamed from: contains-68kG9v0, reason: not valid java name */
    public static final boolean m2074contains68kG9v0(UIntRange r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "$this$contains");
        return r1.m2047containsWZ4Q5Ns(UInt.m914constructorimpl(r2 & 255));
    }

    /* JADX INFO: renamed from: contains-GYNo2lE, reason: not valid java name */
    private static final boolean m2075containsGYNo2lE(ULongRange r2, ULong r3) {
        Intrinsics.checkNotNullParameter(r2, "$this$contains");
        if (r3 != null) goto L5;
    L7:
        return false;
    L5:
        if (r2.m2056containsVKZWuLQ(r3.m1045unboximpl()) == false) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: contains-Gab390E, reason: not valid java name */
    public static final boolean m2076containsGab390E(ULongRange r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "$this$contains");
        return r4.m2056containsVKZWuLQ(ULong.m993constructorimpl(((long) r5) & 4294967295L));
    }

    /* JADX INFO: renamed from: contains-ULb-yJY, reason: not valid java name */
    public static final boolean m2077containsULbyJY(ULongRange r4, byte r5) {
        Intrinsics.checkNotNullParameter(r4, "$this$contains");
        return r4.m2056containsVKZWuLQ(ULong.m993constructorimpl(((long) r5) & 255));
    }

    /* JADX INFO: renamed from: contains-ZsK3CEQ, reason: not valid java name */
    public static final boolean m2078containsZsK3CEQ(UIntRange r1, short r2) {
        Intrinsics.checkNotNullParameter(r1, "$this$contains");
        return r1.m2047containsWZ4Q5Ns(UInt.m914constructorimpl(65535 & r2));
    }

    /* JADX INFO: renamed from: contains-biwQdVI, reason: not valid java name */
    private static final boolean m2079containsbiwQdVI(UIntRange r1, UInt r2) {
        Intrinsics.checkNotNullParameter(r1, "$this$contains");
        if (r2 != null) goto L5;
    L7:
        return false;
    L5:
        if (r1.m2047containsWZ4Q5Ns(r2.m966unboximpl()) == false) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: contains-fz5IDCE, reason: not valid java name */
    public static final boolean m2080containsfz5IDCE(UIntRange r4, long r5) {
        Intrinsics.checkNotNullParameter(r4, "$this$contains");
        if (ULong.m993constructorimpl(r5 >>> 32) == 0) goto L5;
    L7:
        return false;
    L5:
        if (r4.m2047containsWZ4Q5Ns(UInt.m914constructorimpl((int) r5)) == false) goto L7;
        return true;
    }

    /* JADX INFO: renamed from: contains-uhHAxoY, reason: not valid java name */
    public static final boolean m2081containsuhHAxoY(ULongRange r4, short r5) {
        Intrinsics.checkNotNullParameter(r4, "$this$contains");
        return r4.m2056containsVKZWuLQ(ULong.m993constructorimpl(((long) r5) & 65535));
    }

    /* JADX INFO: renamed from: downTo-5PvTz6A, reason: not valid java name */
    public static final UIntProgression m2082downTo5PvTz6A(short r4, short r5) {
        return UIntProgression.Companion.m2044fromClosedRangeNkh28Cs(UInt.m914constructorimpl(r4 & UShort.MAX_VALUE), UInt.m914constructorimpl(65535 & r5), -1);
    }

    /* JADX INFO: renamed from: downTo-J1ME1BU, reason: not valid java name */
    public static final UIntProgression m2083downToJ1ME1BU(int r2, int r3) {
        return UIntProgression.Companion.m2044fromClosedRangeNkh28Cs(r2, r3, -1);
    }

    /* JADX INFO: renamed from: downTo-Kr8caGY, reason: not valid java name */
    public static final UIntProgression m2084downToKr8caGY(byte r4, byte r5) {
        return UIntProgression.Companion.m2044fromClosedRangeNkh28Cs(UInt.m914constructorimpl(r4 & 255), UInt.m914constructorimpl(r5 & 255), -1);
    }

    /* JADX INFO: renamed from: downTo-eb3DHEI, reason: not valid java name */
    public static final ULongProgression m2085downToeb3DHEI(long r7, long r9) {
        return ULongProgression.Companion.m2053fromClosedRange7ftBX0g(r7, r9, -1);
    }

    public static final int first(UIntProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.m2042getFirstpVg5ArA();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final long first(ULongProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.m2051getFirstsVKNKU();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final UInt firstOrNull(UIntProgression r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return UInt.m908boximpl(r1.m2042getFirstpVg5ArA());
    }

    public static final ULong firstOrNull(ULongProgression r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isEmpty() == false) goto L6;
        return null;
    L6:
        return ULong.m987boximpl(r2.m2051getFirstsVKNKU());
    }

    public static final int last(UIntProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.m2043getLastpVg5ArA();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final long last(ULongProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.m2052getLastsVKNKU();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final UInt lastOrNull(UIntProgression r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return UInt.m908boximpl(r1.m2043getLastpVg5ArA());
    }

    public static final ULong lastOrNull(ULongProgression r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isEmpty() == false) goto L6;
        return null;
    L6:
        return ULong.m987boximpl(r2.m2052getLastsVKNKU());
    }

    private static final int random(UIntRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return URangesKt.random(r1, Random.Default);
    }

    public static final int random(UIntRange r3, Random r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "random");
        return URandomKt.nextUInt(r4, r3);
    L5:
        e = move-exception;
        throw new NoSuchElementException(e.getMessage());
    }

    private static final long random(ULongRange r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return URangesKt.random(r2, Random.Default);
    }

    public static final long random(ULongRange r3, Random r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "random");
        return URandomKt.nextULong(r4, r3);
    L5:
        e = move-exception;
        throw new NoSuchElementException(e.getMessage());
    }

    private static final UInt randomOrNull(UIntRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return URangesKt.randomOrNull(r1, Random.Default);
    }

    public static final UInt randomOrNull(UIntRange r1, Random r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "random");
        if (r1.isEmpty() == false) goto L7;
        return null;
    L7:
        return UInt.m908boximpl(URandomKt.nextUInt(r2, r1));
    }

    private static final ULong randomOrNull(ULongRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return URangesKt.randomOrNull(r1, Random.Default);
    }

    public static final ULong randomOrNull(ULongRange r2, Random r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "random");
        if (r2.isEmpty() == false) goto L7;
        return null;
    L7:
        return ULong.m987boximpl(URandomKt.nextULong(r3, r2));
    }

    public static final UIntProgression reversed(UIntProgression r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        return UIntProgression.Companion.m2044fromClosedRangeNkh28Cs(r4.m2043getLastpVg5ArA(), r4.m2042getFirstpVg5ArA(), -r4.getStep());
    }

    public static final ULongProgression reversed(ULongProgression r8) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        return ULongProgression.Companion.m2053fromClosedRange7ftBX0g(r8.m2052getLastsVKNKU(), r8.m2051getFirstsVKNKU(), -r8.getStep());
    }

    public static final UIntProgression step(UIntProgression r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        if (r5 <= 0) goto L5;
        boolean r0 = true;
    L6:
        RangesKt.checkStepIsPositive(r0, Integer.valueOf(r5));
        UIntProgression.Companion r1 = UIntProgression.Companion;
        int r2 = r4.m2042getFirstpVg5ArA();
        int r3 = r4.m2043getLastpVg5ArA();
        if (r4.getStep() <= 0) goto L9;
        int r6 = r5;
    L11:
        return r1.m2044fromClosedRangeNkh28Cs(r2, r3, r6);
    L9:
        r6 = -r5;
        goto L11
    L5:
        r0 = false;
        goto L6
    }

    public static final ULongProgression step(ULongProgression r11, long r12) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        if (r12 <= 0) goto L5;
        boolean r2 = true;
    L6:
        RangesKt.checkStepIsPositive(r2, Long.valueOf(r12));
        ULongProgression.Companion r4 = ULongProgression.Companion;
        long r5 = r11.m2051getFirstsVKNKU();
        long r7 = r11.m2052getLastsVKNKU();
        if (r11.getStep() <= 0) goto L9;
        long r9 = r12;
    L11:
        return r4.m2053fromClosedRange7ftBX0g(r5, r7, r9);
    L9:
        r9 = -r12;
        goto L11
    L5:
        r2 = false;
        goto L6
    }

    /* JADX INFO: renamed from: until-5PvTz6A, reason: not valid java name */
    public static final UIntRange m2086until5PvTz6A(short r4, short r5) {
        if (Intrinsics.compare(r5 & UShort.MAX_VALUE, 0) > 0) goto L7;
        return UIntRange.Companion.getEMPTY();
    L7:
        return new UIntRange(UInt.m914constructorimpl(r4 & UShort.MAX_VALUE), UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r5) - 1), null);
    }

    /* JADX INFO: renamed from: until-J1ME1BU, reason: not valid java name */
    public static final UIntRange m2087untilJ1ME1BU(int r3, int r4) {
        if (UByte$$ExternalSyntheticBackport4.m433m(r4, 0) > 0) goto L7;
        return UIntRange.Companion.getEMPTY();
    L7:
        return new UIntRange(r3, UInt.m914constructorimpl(r4 - 1), null);
    }

    /* JADX INFO: renamed from: until-Kr8caGY, reason: not valid java name */
    public static final UIntRange m2088untilKr8caGY(byte r4, byte r5) {
        if (Intrinsics.compare(r5 & 255, 0) > 0) goto L7;
        return UIntRange.Companion.getEMPTY();
    L7:
        return new UIntRange(UInt.m914constructorimpl(r4 & 255), UInt.m914constructorimpl(UInt.m914constructorimpl(r5 & 255) - 1), null);
    }

    /* JADX INFO: renamed from: until-eb3DHEI, reason: not valid java name */
    public static final ULongRange m2089untileb3DHEI(long r8, long r10) {
        if (C0871x7f2744bd.m371m(r10, 0) > 0) goto L7;
        return ULongRange.Companion.getEMPTY();
    L7:
        return new ULongRange(r8, ULong.m993constructorimpl(r10 - ULong.m993constructorimpl(((long) 1) & 4294967295L)), null);
    }
}
