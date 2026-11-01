package kotlin.text;

import com.google.common.primitives.C0871x7f2744bd;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByte$$ExternalSyntheticBackport0;
import kotlin.UByte$$ExternalSyntheticBackport3;
import kotlin.UByte$$ExternalSyntheticBackport4;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UStrings.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\u000b\u0010\f\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\u000e\u0010\u000f\u001a\u0011\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001a\u0019\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u0012\u001a\u000e\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u001a\u0016\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0011\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0015\u001a\u0019\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u0016\u001a\u000e\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007\u001a\u0016\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0011\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0019\u001a\u0019\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u001a\u001a\u000e\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007\u001a\u0016\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0011\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u001d\u001a\u0019\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u001e\u001a\u000e\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007\u001a\u0016\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006 "}, m421d2 = {"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class UStringsKt {
    /* JADX INFO: renamed from: toString-JSWoG40, reason: not valid java name */
    public static final String m2131toStringJSWoG40(long r1, int r3) {
        return UnsignedKt.ulongToString(r1, CharsKt.checkRadix(r3));
    }

    /* JADX INFO: renamed from: toString-LxnNnR4, reason: not valid java name */
    public static final String m2132toStringLxnNnR4(byte r2, int r3) {
        String r0 = Integer.toString(r2 & 255, CharsKt.checkRadix(r3));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }

    /* JADX INFO: renamed from: toString-V7xB4Y4, reason: not valid java name */
    public static final String m2133toStringV7xB4Y4(int r4, int r5) {
        String r0 = Long.toString(((long) r4) & 4294967295L, CharsKt.checkRadix(r5));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }

    /* JADX INFO: renamed from: toString-olVBNx4, reason: not valid java name */
    public static final String m2134toStringolVBNx4(short r2, int r3) {
        String r0 = Integer.toString(65535 & r2, CharsKt.checkRadix(r3));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }

    public static final byte toUByte(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        UByte r0 = toUByteOrNull(r1);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r1);
        throw new KotlinNothingValueException();
    L5:
        return r0.m887unboximpl();
    }

    public static final byte toUByte(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        UByte r0 = toUByteOrNull(r1, r2);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r1);
        throw new KotlinNothingValueException();
    L5:
        return r0.m887unboximpl();
    }

    public static final UByte toUByteOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return toUByteOrNull(r1, 10);
    }

    public static final UByte toUByteOrNull(String r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        UInt r0 = toUIntOrNull(r3, r4);
        if (r0 == null) goto L9;
        int r1 = r0.m966unboximpl();
        if (UByte$$ExternalSyntheticBackport4.m433m(r1, UInt.m914constructorimpl(255)) <= 0) goto L8;
        return null;
    L8:
        return UByte.m831boximpl(UByte.m837constructorimpl((byte) r1));
    L9:
        return null;
    }

    public static final int toUInt(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        UInt r0 = toUIntOrNull(r1);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r1);
        throw new KotlinNothingValueException();
    L5:
        return r0.m966unboximpl();
    }

    public static final int toUInt(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        UInt r0 = toUIntOrNull(r1, r2);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r1);
        throw new KotlinNothingValueException();
    L5:
        return r0.m966unboximpl();
    }

    public static final UInt toUIntOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return toUIntOrNull(r1, 10);
    }

    public static final UInt toUIntOrNull(String r13, int r14) {
        Intrinsics.checkNotNullParameter(r13, "<this>");
        CharsKt.checkRadix(r14);
        int r0 = r13.length();
        if (r0 != 0) goto L5;
        return null;
    L5:
        char r4 = r13.charAt(0);
        if (Intrinsics.compare(r4, 48) < 0) goto L8;
        int r3 = 0;
    L15:
        int r6 = 119304647;
        int r7 = UInt.m914constructorimpl(r14);
        int r8 = 0;
        int r9 = r3;
    L16:
        if (r9 >= r0) goto L32;
        int r10 = CharsKt.digitOf(r13.charAt(r9), r14);
        if (r10 < 0) goto L19;
        if (UByte$$ExternalSyntheticBackport4.m433m(r8, r6) <= 0) goto L27;
        if (r6 != 119304647) goto L26;
        r6 = UByte$$ExternalSyntheticBackport0.m429m(-1, r7);
        if (UByte$$ExternalSyntheticBackport4.m433m(r8, r6) <= 0) goto L27;
        return null;
    L26:
        return null;
    L27:
        int r11 = UInt.m914constructorimpl(r8 * r7);
        r8 = UInt.m914constructorimpl(UInt.m914constructorimpl(r10) + r11);
        if (UByte$$ExternalSyntheticBackport4.m433m(r8, r11) < 0) goto L29;
        r9 = r9 + 1;
        goto L16
    L29:
        return null;
    L19:
        return null;
    L32:
        return UInt.m908boximpl(r8);
    L8:
        if (r0 != 1) goto L10;
    L13:
        return null;
    L10:
        if (r4 != '+') goto L13;
        r3 = 1;
        goto L15
    }

    public static final long toULong(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        ULong r0 = toULongOrNull(r2);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r2);
        throw new KotlinNothingValueException();
    L5:
        return r0.m1045unboximpl();
    }

    public static final long toULong(String r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        ULong r0 = toULongOrNull(r2, r3);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r2);
        throw new KotlinNothingValueException();
    L5:
        return r0.m1045unboximpl();
    }

    public static final ULong toULongOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return toULongOrNull(r1, 10);
    }

    public static final ULong toULongOrNull(String r23, int r24) {
        String r0 = r23;
        int r1 = r24;
        Intrinsics.checkNotNullParameter(r0, "<this>");
        CharsKt.checkRadix(r1);
        int r2 = r0.length();
        if (r2 != 0) goto L5;
        return null;
    L5:
        char r7 = r0.charAt(0);
        if (Intrinsics.compare(r7, 48) < 0) goto L8;
        int r6 = 0;
    L15:
        long r10 = 512409557603043100L;
        long r12 = ULong.m993constructorimpl(r1);
        long r14 = 0;
        int r3 = r6;
    L16:
        if (r3 >= r2) goto L33;
        int r17 = r2;
        int r4 = CharsKt.digitOf(r0.charAt(r3), r1);
        if (r4 < 0) goto L19;
        if (C0871x7f2744bd.m371m(r14, r10) > 0) goto L23;
    L28:
        long r15 = ULong.m993constructorimpl(r14 * r12);
        r14 = ULong.m993constructorimpl(ULong.m993constructorimpl(((long) UInt.m914constructorimpl(r4)) & 4294967295L) + r15);
        if (C0871x7f2744bd.m371m(r14, r15) < 0) goto L30;
        r3 = r3 + 1;
        r0 = r23;
        r1 = r24;
        r2 = r17;
        goto L16
    L30:
        return null;
    L23:
        if (r10 != 512409557603043100L) goto L27;
        r10 = UByte$$ExternalSyntheticBackport3.m432m(-1, r12);
        if (C0871x7f2744bd.m371m(r14, r10) <= 0) goto L28;
        return null;
    L27:
        return null;
    L19:
        return null;
    L33:
        return ULong.m987boximpl(r14);
    L8:
        if (r2 != 1) goto L10;
    L13:
        return null;
    L10:
        if (r7 != '+') goto L13;
        r6 = 1;
        goto L15
    }

    public static final short toUShort(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        UShort r0 = toUShortOrNull(r1);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r1);
        throw new KotlinNothingValueException();
    L5:
        return r0.m1150unboximpl();
    }

    public static final short toUShort(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        UShort r0 = toUShortOrNull(r1, r2);
        if (r0 != null) goto L5;
        StringsKt.numberFormatError(r1);
        throw new KotlinNothingValueException();
    L5:
        return r0.m1150unboximpl();
    }

    public static final UShort toUShortOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return toUShortOrNull(r1, 10);
    }

    public static final UShort toUShortOrNull(String r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        UInt r0 = toUIntOrNull(r3, r4);
        if (r0 == null) goto L9;
        int r1 = r0.m966unboximpl();
        if (UByte$$ExternalSyntheticBackport4.m433m(r1, UInt.m914constructorimpl(65535)) <= 0) goto L8;
        return null;
    L8:
        return UShort.m1094boximpl(UShort.m1100constructorimpl((short) r1));
    L9:
        return null;
    }
}
