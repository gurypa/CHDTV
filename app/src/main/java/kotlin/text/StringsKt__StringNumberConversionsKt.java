package kotlin.text;

import androidx.media3.common.C0180C;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StringNumberConversions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015"}, m421d2 = {"numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
    public StringsKt__StringNumberConversionsKt() {
    }

    public static final Void numberFormatError(String r3) {
        Intrinsics.checkNotNullParameter(r3, "input");
        throw new NumberFormatException("Invalid number format: '" + r3 + '\'');
    }

    public static final Byte toByteOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.toByteOrNull(r1, 10);
    }

    public static final Byte toByteOrNull(String r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Integer r0 = StringsKt.toIntOrNull(r3, r4);
        if (r0 == null) goto L12;
        int r1 = r0.intValue();
        if (r1 >= (-128)) goto L7;
    L11:
        return null;
    L7:
        if (r1 > 127) goto L11;
        return Byte.valueOf((byte) r1);
    L12:
        return null;
    }

    public static final Integer toIntOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.toIntOrNull(r1, 10);
    }

    public static final Integer toIntOrNull(String r12, int r13) {
        Intrinsics.checkNotNullParameter(r12, "<this>");
        CharsKt.checkRadix(r13);
        int r0 = r12.length();
        if (r0 != 0) goto L5;
        return null;
    L5:
        char r5 = r12.charAt(0);
        if (Intrinsics.compare(r5, 48) < 0) goto L8;
        int r2 = 0;
        boolean r3 = false;
        int r4 = C0180C.RATE_UNSET_INT;
    L18:
        int r7 = -59652323;
        int r8 = 0;
        int r9 = r2;
    L19:
        if (r9 >= r0) goto L33;
        int r10 = CharsKt.digitOf(r12.charAt(r9), r13);
        if (r10 < 0) goto L22;
        if (r8 >= r7) goto L29;
        if (r7 != (-59652323)) goto L28;
        r7 = r4 / r13;
        if (r8 >= r7) goto L29;
        return null;
    L28:
        return null;
    L29:
        int r11 = r8 * r13;
        if (r11 < (r4 + r10)) goto L31;
        r8 = r11 - r10;
        r9 = r9 + 1;
        goto L19
    L31:
        return null;
    L22:
        return null;
    L33:
        if (r3 == false) goto L36;
        return Integer.valueOf(r8);
    L36:
        return Integer.valueOf(-r8);
    L8:
        if (r0 != 1) goto L10;
        return null;
    L10:
        r2 = 1;
        if (r5 != '-') goto L14;
        r3 = true;
        r4 = Integer.MIN_VALUE;
        goto L18
    L14:
        if (r5 != '+') goto L16;
        r3 = false;
        r4 = C0180C.RATE_UNSET_INT;
        goto L18
    L16:
        return null;
    }

    public static final Long toLongOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.toLongOrNull(r1, 10);
    }

    public static final Long toLongOrNull(String r19, int r20) {
        Intrinsics.checkNotNullParameter(r19, "<this>");
        CharsKt.checkRadix(r20);
        int r2 = r19.length();
        Long r3 = null;
        if (r2 != 0) goto L5;
        return null;
    L5:
        char r8 = r19.charAt(0);
        if (Intrinsics.compare(r8, 48) < 0) goto L8;
        int r4 = 0;
        boolean r5 = false;
        long r6 = C0180C.TIME_UNSET;
    L18:
        long r11 = -256204778801521550L;
        long r13 = 0;
        int r15 = r4;
    L19:
        if (r15 >= r2) goto L38;
        Long r16 = r3;
        int r7 = CharsKt.digitOf(r19.charAt(r15), r20);
        if (r7 < 0) goto L22;
        if (r13 >= r11) goto L33;
        if (r11 != (-256204778801521550L)) goto L31;
        long r9 = r6 / ((long) r20);
        if (r13 < r9) goto L29;
        r11 = r9;
        goto L33
    L29:
        return r16;
    L31:
        return r16;
    L33:
        long r14 = r13 * ((long) r20);
        if (r14 < (((long) r7) + r6)) goto L35;
        r13 = r14 - ((long) r7);
        r15 = r15 + 1;
        r3 = r16;
        r4 = r4;
        r5 = r5;
        goto L19
    L35:
        return r16;
    L22:
        return r16;
    L38:
        if (r5 == false) goto L41;
        return Long.valueOf(r13);
    L41:
        return Long.valueOf(-r13);
    L8:
        if (r2 != 1) goto L10;
        return null;
    L10:
        r4 = 1;
        if (r8 != '-') goto L14;
        r5 = true;
        r6 = Long.MIN_VALUE;
        goto L18
    L14:
        if (r8 != '+') goto L16;
        r5 = false;
        r6 = C0180C.TIME_UNSET;
        goto L18
    L16:
        return null;
    }

    public static final Short toShortOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.toShortOrNull(r1, 10);
    }

    public static final Short toShortOrNull(String r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Integer r0 = StringsKt.toIntOrNull(r3, r4);
        if (r0 == null) goto L12;
        int r1 = r0.intValue();
        if (r1 >= (-32768)) goto L7;
    L11:
        return null;
    L7:
        if (r1 > 32767) goto L11;
        return Short.valueOf((short) r1);
    L12:
        return null;
    }
}
