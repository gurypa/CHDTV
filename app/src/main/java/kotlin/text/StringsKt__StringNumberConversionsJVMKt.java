package kotlin.text;

import androidx.exifinterface.media.ExifInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StringNumberConversionsJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000X\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a4\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0005H\u0082\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0003H\u0087\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u0003H\u0007\u001a\u0016\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u001a\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\u000f\u0010\u0012\u001a\u00020\u0013*\u0004\u0018\u00010\u0003H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0015*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u0017*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u0018\u001a\u0004\u0018\u00010\u0017*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0019\u001a\r\u0010\u001a\u001a\u00020\u001b*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u001b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001d\u001a\r\u0010\u001e\u001a\u00020\u0010*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001e\u001a\u00020\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u001f\u001a\u00020 *\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010!\u001a\u00020\"*\u00020\u0003H\u0087\b\u001a\u0015\u0010!\u001a\u00020\"*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020 2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\"2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b¨\u0006$"}, m421d2 = {"screenFloatValue", ExifInterface.GPS_DIRECTION_TRUE, "str", "", "parse", "Lkotlin/Function1;", "screenFloatValue$StringsKt__StringNumberConversionsJVMKt", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "toBigDecimal", "Ljava/math/BigDecimal;", "mathContext", "Ljava/math/MathContext;", "toBigDecimalOrNull", "toBigInteger", "Ljava/math/BigInteger;", "radix", "", "toBigIntegerOrNull", "toBoolean", "", "toByte", "", "toDouble", "", "toDoubleOrNull", "(Ljava/lang/String;)Ljava/lang/Double;", "toFloat", "", "toFloatOrNull", "(Ljava/lang/String;)Ljava/lang/Float;", "toInt", "toLong", "", "toShort", "", "toString", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt {
    public StringsKt__StringNumberConversionsJVMKt() {
    }

    private static final <T> T screenFloatValue$StringsKt__StringNumberConversionsJVMKt(String r4, Function1<? super String, ? extends T> r5) {
        if (ScreenFloatValueRegEx.value.matches(r4) == false) goto L14;
        return r5.invoke(r4);
    L14:
        return null;
    L10:
        return null;
    }

    private static final BigDecimal toBigDecimal(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new BigDecimal(r1);
    }

    private static final BigDecimal toBigDecimal(String r1, MathContext r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "mathContext");
        return new BigDecimal(r1, r2);
    }

    public static final BigDecimal toBigDecimalOrNull(String r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        if (ScreenFloatValueRegEx.value.matches(r5) == false) goto L15;
        return new BigDecimal(r5);
    L15:
        return null;
    L11:
        return null;
    }

    public static final BigDecimal toBigDecimalOrNull(String r5, MathContext r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "mathContext");
        if (ScreenFloatValueRegEx.value.matches(r5) == false) goto L15;
        return new BigDecimal(r5, r6);
    L15:
        return null;
    L11:
        return null;
    }

    private static final BigInteger toBigInteger(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new BigInteger(r1);
    }

    private static final BigInteger toBigInteger(String r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new BigInteger(r2, CharsKt.checkRadix(r3));
    }

    public static final BigInteger toBigIntegerOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.toBigIntegerOrNull(r1, 10);
    }

    public static final BigInteger toBigIntegerOrNull(String r5, int r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        CharsKt.checkRadix(r6);
        int r0 = r5.length();
        int r1 = 0;
        switch(r0) {
            case 0: goto L10;
            case 1: goto L8;
            default: goto L5;
        };
    L10:
        return null;
    L5:
        if (r5.charAt(0) != '-') goto L11;
        r1 = 1;
    L11:
        int r3 = r1;
    L12:
        if (r3 >= r0) goto L18;
        if (CharsKt.digitOf(r5.charAt(r3), r6) < 0) goto L15;
        r3 = r3 + 1;
        goto L12
    L15:
        return null;
    L18:
        return new BigInteger(r5, CharsKt.checkRadix(r6));
    L8:
        if (CharsKt.digitOf(r5.charAt(0), r6) >= 0) goto L18;
        return null;
    }

    private static final boolean toBoolean(String r1) {
        return Boolean.parseBoolean(r1);
    }

    private static final byte toByte(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Byte.parseByte(r1);
    }

    private static final byte toByte(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Byte.parseByte(r1, CharsKt.checkRadix(r2));
    }

    private static final double toDouble(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return Double.parseDouble(r2);
    }

    public static final Double toDoubleOrNull(String r6) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        if (ScreenFloatValueRegEx.value.matches(r6) == false) goto L15;
        return Double.valueOf(Double.parseDouble(r6));
    L15:
        return null;
    L11:
        return null;
    }

    private static final float toFloat(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Float.parseFloat(r1);
    }

    public static final Float toFloatOrNull(String r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        if (ScreenFloatValueRegEx.value.matches(r5) == false) goto L15;
        return Float.valueOf(Float.parseFloat(r5));
    L15:
        return null;
    L11:
        return null;
    }

    private static final int toInt(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Integer.parseInt(r1);
    }

    private static final int toInt(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Integer.parseInt(r1, CharsKt.checkRadix(r2));
    }

    private static final long toLong(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return Long.parseLong(r2);
    }

    private static final long toLong(String r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return Long.parseLong(r2, CharsKt.checkRadix(r3));
    }

    private static final short toShort(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Short.parseShort(r1);
    }

    private static final short toShort(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Short.parseShort(r1, CharsKt.checkRadix(r2));
    }

    private static final String toString(byte r2, int r3) {
        String r0 = Integer.toString(r2, CharsKt.checkRadix(r3));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }

    private static final String toString(int r2, int r3) {
        String r0 = Integer.toString(r2, CharsKt.checkRadix(r3));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }

    private static final String toString(long r2, int r4) {
        String r0 = Long.toString(r2, CharsKt.checkRadix(r4));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }

    private static final String toString(short r2, int r3) {
        String r0 = Integer.toString(r2, CharsKt.checkRadix(r3));
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }
}
