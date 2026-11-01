package kotlin.text;

import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.ULong;
import kotlin.collections.AbstractList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: HexExtensions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\n\n\u0002\b\u0004\u001a \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0002\u001a@\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\tH\u0000\u001a@\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\tH\u0000\u001a \u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tH\u0002\u001a,\u0010\u0016\u001a\u00020\t*\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u0003H\u0002\u001a,\u0010\u001b\u001a\u00020\u001c*\u00020\u00032\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 H\u0002\u001a\u001c\u0010!\u001a\u00020\t*\u00020\u00032\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\tH\u0002\u001a\u0014\u0010\"\u001a\u00020\t*\u00020\u00032\u0006\u0010\u0018\u001a\u00020\tH\u0002\u001a*\u0010#\u001a\u00020$*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010#\u001a\u00020$*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010'\u001a\u00020(*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010'\u001a\u00020(*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010)\u001a\u00020\t*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010)\u001a\u00020\t*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010*\u001a\u00020\u0006*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010*\u001a\u00020\u0006*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a0\u0010+\u001a\u00020\u0006*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\u0006\u0010%\u001a\u00020&2\u0006\u0010\u001e\u001a\u00020\tH\u0003\u001a*\u0010,\u001a\u00020-*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010,\u001a\u00020-*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020$2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010.\u001a\u00020\u0003*\u00020(2\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020(2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020\u00062\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020-2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u001c\u0010/\u001a\u00020\u0003*\u00020\u00062\u0006\u0010%\u001a\u00020&2\u0006\u00100\u001a\u00020\tH\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000¨\u00061"}, m421d2 = {"HEX_DIGITS_TO_DECIMAL", "", "LOWER_CASE_HEX_DIGITS", "", "UPPER_CASE_HEX_DIGITS", "charsPerSet", "", "charsPerElement", "elementsPerSet", "", "elementSeparatorLength", "formattedStringLength", "totalBytes", "bytesPerLine", "bytesPerGroup", "groupSeparatorLength", "byteSeparatorLength", "bytePrefixLength", "byteSuffixLength", "parsedByteArrayMaxSize", "stringLength", "wholeElementsPerSet", "checkContainsAt", "part", StreamInformation.KEY_INDEX, "endIndex", "partName", "checkHexLength", "", "startIndex", "maxDigits", "requireMaxLength", "", "checkNewLineAt", "decimalFromHexDigitAt", "hexToByte", "", MediaInformation.KEY_FORMAT_PROPERTIES, "Lkotlin/text/HexFormat;", "hexToByteArray", "", "hexToInt", "hexToLong", "hexToLongImpl", "hexToShort", "", "toHexString", "toHexStringImpl", "bits", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class HexExtensionsKt {
    private static final int[] HEX_DIGITS_TO_DECIMAL = null;
    private static final String LOWER_CASE_HEX_DIGITS = "0123456789abcdef";
    private static final String UPPER_CASE_HEX_DIGITS = "0123456789ABCDEF";

    static {
        int[] r1 = new int[128];
        int r2 = 0;
        int r3 = 0;
    L3:
        if (r3 >= 128) goto L5;
        r1[r3] = -1;
        r3 = r3 + 1;
        goto L3
    L5:
        int r6 = 0;
        int r7 = 0;
    L7:
        if (r7 >= r4.length()) goto L9;
        r1[r4.charAt(r7)] = r6;
        r7 = r7 + 1;
        r6 = r6 + 1;
        goto L7
    L9:
        int r8 = 0;
    L11:
        if (r2 >= r4.length()) goto L13;
        r1[r4.charAt(r2)] = r8;
        r2 = r2 + 1;
        r8 = r8 + 1;
        goto L11
    L13:
        HEX_DIGITS_TO_DECIMAL = r1;
    }

    private static final long charsPerSet(long r8, int r10, int r11) {
        if (r10 <= 0) goto L4;
        boolean r0 = true;
    L5:
        if (r0 == false) goto L9;
        return (((long) r10) * r8) + (((long) r11) * (((long) r10) - 1));
    L9:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L4:
        r0 = false;
        goto L5
    }

    private static final int checkContainsAt(String r7, String r8, int r9, int r10, String r11) {
        int r0 = r8.length() + r9;
        if (r0 > r10) goto L7;
        String r1 = r7;
        String r3 = r8;
        int r2 = r9;
        if (StringsKt.regionMatches(r1, r2, r3, 0, r8.length(), true) == false) goto L8;
        return r0;
    L8:
        StringBuilder r12 = new StringBuilder().append("Expected ").append(r11).append(" \"").append(r3).append("\" at index ").append(r2).append(", but was ");
        int r13 = RangesKt.coerceAtMost(r0, r10);
        Intrinsics.checkNotNull(r1, "null cannot be cast to non-null type java.lang.String");
        String r14 = r1.substring(r2, r13);
        Intrinsics.checkNotNullExpressionValue(r14, "substring(...)");
        throw new NumberFormatException(r12.append(r14).toString());
    L7:
        r1 = r7;
        r3 = r8;
        r2 = r9;
        goto L8
    }

    private static final void checkHexLength(String r7, int r8, int r9, int r10, boolean r11) {
        int r0 = r9 - r8;
        boolean r1 = true;
        if (r11 == false) goto L6;
        if (r0 == r10) goto L9;
    L8:
        r1 = false;
    L9:
        if (r1 == true) goto L15;
        if (r11 == false) goto L12;
        String r2 = "exactly";
    L13:
        Intrinsics.checkNotNull(r7, "null cannot be cast to non-null type java.lang.String");
        String r3 = r7.substring(r8, r9);
        Intrinsics.checkNotNullExpressionValue(r3, "substring(...)");
        throw new NumberFormatException("Expected " + r2 + ' ' + r10 + " hexadecimal digits at index " + r8 + ", but was " + r3 + " of length " + r0);
    L12:
        r2 = "at most";
        goto L13
    L15:
        return;
    L6:
        if (r0 > r10) goto L8;
        goto L8
    }

    private static final int checkNewLineAt(String r3, int r4, int r5) {
        if (r3.charAt(r4) != '\r') goto L11;
        if ((r4 + 1) >= r5) goto L17;
        if (r3.charAt(r4 + 1) != '\n') goto L17;
        return r4 + 2;
    L17:
        return r4 + 1;
    L11:
        if (r3.charAt(r4) != '\n') goto L15;
        return r4 + 1;
    L15:
        throw new NumberFormatException("Expected a new line at index " + r4 + ", but was " + r3.charAt(r4));
    }

    private static final int decimalFromHexDigitAt(String r4, int r5) {
        char r0 = r4.charAt(r5);
        if (r0 > 127) goto L9;
        if (HEX_DIGITS_TO_DECIMAL[r0] < 0) goto L9;
        return HEX_DIGITS_TO_DECIMAL[r0];
    L9:
        throw new NumberFormatException("Expected a hexadecimal digit at index " + r5 + ", but was " + r4.charAt(r5));
    }

    public static final int formattedStringLength(int r17, int r18, int r19, int r20, int r21, int r22, int r23) {
        if (r17 <= 0) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L17;
        int r3 = (r17 - 1) / r18;
        int r4 = (r18 - 1) / r19;
        int r5 = r17 % r18;
        if (r5 != 0) goto L10;
        r5 = r18;
    L10:
        int r7 = (r3 * r4) + ((r5 - 1) / r19);
        long r6 = ((((long) r3) + (((long) r7) * ((long) r20))) + (((long) (((r17 - 1) - r3) - r7)) * ((long) r21))) + (((long) r17) * ((((long) r22) + 2) + ((long) r23)));
        if (RangesKt.intRangeContains(new IntRange(0, Integer.MAX_VALUE), r6) == false) goto L15;
        return (int) r6;
    L15:
        throw new IllegalArgumentException("The resulting string length is too big: " + ULong.m1039toStringimpl(ULong.m993constructorimpl(r6)));
    L17:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L5:
        r2 = false;
        goto L6
    }

    private static final byte hexToByte(String r2, int r3, int r4, HexFormat r5) {
        return (byte) hexToLongImpl(r2, r3, r4, r5, 2);
    }

    public static final byte hexToByte(String r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return hexToByte(r2, 0, r2.length(), r3);
    }

    static /* synthetic */ byte hexToByte$default(String r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = HexFormat.Companion.getDefault();
    L12:
        return hexToByte(r0, r1, r2, r3);
    }

    public static /* synthetic */ byte hexToByte$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return hexToByte(r0, r1);
    }

    private static final byte[] hexToByteArray(String r18, int r19, int r20, HexFormat r21) {
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r19, r20, r18.length());
        if (r19 == r20) goto L5;
        HexFormat.BytesHexFormat r3 = r21.getBytes();
        int r5 = r3.getBytesPerLine();
        int r6 = r3.getBytesPerGroup();
        String r11 = r3.getBytePrefix();
        String r12 = r3.getByteSuffix();
        String r13 = r3.getByteSeparator();
        String r14 = r3.getGroupSeparator();
        int r4 = parsedByteArrayMaxSize(r20 - r19, r5, r6, r14.length(), r13.length(), r11.length(), r12.length());
        byte[] r7 = new byte[r4];
        int r8 = r19;
        int r9 = 0;
        int r10 = 0;
        int r15 = 0;
    L7:
        if (r8 >= r20) goto L16;
        if (r10 != r5) goto L10;
        r8 = checkNewLineAt(r18, r8, r20);
        r10 = 0;
        r15 = 0;
    L14:
        r10 = r10 + 1;
        r15 = r15 + 1;
        int r1 = checkContainsAt(r18, r11, r8, r20, "byte prefix");
        HexFormat.BytesHexFormat r16 = r3;
        int r17 = r4;
        checkHexLength(r18, r1, RangesKt.coerceAtMost(r1 + 2, r20), 2, true);
        int r22 = r1 + 1;
        int r23 = r22 + 1;
        r7[r9] = (byte) ((decimalFromHexDigitAt(r18, r1) << 4) | decimalFromHexDigitAt(r18, r22));
        r8 = checkContainsAt(r18, r12, r23, r20, "byte suffix");
        r9 = r9 + 1;
        r3 = r16;
        r4 = r17;
        goto L7
    L10:
        if (r15 != r6) goto L12;
        r8 = checkContainsAt(r18, r14, r8, r20, "group separator");
        r15 = 0;
        goto L14
    L12:
        if (r15 == 0) goto L14;
        r8 = checkContainsAt(r18, r13, r8, r20, "byte separator");
        goto L14
    L16:
        if (r9 == r7.length) goto L25;
        byte[] r2 = Arrays.copyOf(r7, r9);
        Intrinsics.checkNotNullExpressionValue(r2, "copyOf(...)");
        return r2;
    L25:
        return r7;
    L5:
        return new byte[0];
    }

    public static final byte[] hexToByteArray(String r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return hexToByteArray(r2, 0, r2.length(), r3);
    }

    static /* synthetic */ byte[] hexToByteArray$default(String r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = HexFormat.Companion.getDefault();
    L12:
        return hexToByteArray(r0, r1, r2, r3);
    }

    public static /* synthetic */ byte[] hexToByteArray$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return hexToByteArray(r0, r1);
    }

    private static final int hexToInt(String r2, int r3, int r4, HexFormat r5) {
        return (int) hexToLongImpl(r2, r3, r4, r5, 8);
    }

    public static final int hexToInt(String r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return hexToInt(r2, 0, r2.length(), r3);
    }

    static /* synthetic */ int hexToInt$default(String r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = HexFormat.Companion.getDefault();
    L12:
        return hexToInt(r0, r1, r2, r3);
    }

    public static /* synthetic */ int hexToInt$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return hexToInt(r0, r1);
    }

    private static final long hexToLong(String r2, int r3, int r4, HexFormat r5) {
        return hexToLongImpl(r2, r3, r4, r5, 16);
    }

    public static final long hexToLong(String r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return hexToLong(r2, 0, r2.length(), r3);
    }

    static /* synthetic */ long hexToLong$default(String r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = HexFormat.Companion.getDefault();
    L12:
        return hexToLong(r0, r1, r2, r3);
    }

    public static /* synthetic */ long hexToLong$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return hexToLong(r0, r1);
    }

    private static final long hexToLongImpl(String r11, int r12, int r13, HexFormat r14, int r15) {
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r12, r13, r11.length());
        String r0 = r14.getNumber().getPrefix();
        String r1 = r14.getNumber().getSuffix();
        if ((r0.length() + r1.length()) >= (r13 - r12)) goto L8;
        int r2 = checkContainsAt(r11, r0, r12, r13, "prefix");
        int r3 = r13 - r1.length();
        checkContainsAt(r11, r1, r3, r13, "suffix");
        checkHexLength(r11, r2, r3, r15, false);
        long r4 = 0;
        int r6 = r2;
    L5:
        if (r6 >= r3) goto L7;
        r4 = (r4 << 4) | ((long) decimalFromHexDigitAt(r11, r6));
        r6 = r6 + 1;
        goto L5
    L7:
        return r4;
    L8:
        StringBuilder r5 = new StringBuilder().append("Expected a hexadecimal number with prefix \"").append(r0).append("\" and suffix \"").append(r1).append("\", but was ");
        Intrinsics.checkNotNull(r11, "null cannot be cast to non-null type java.lang.String");
        String r7 = r11.substring(r12, r13);
        Intrinsics.checkNotNullExpressionValue(r7, "substring(...)");
        throw new NumberFormatException(r5.append(r7).toString());
    }

    static /* synthetic */ long hexToLongImpl$default(String r0, int r1, int r2, HexFormat r3, int r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r5 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        return hexToLongImpl(r0, r1, r2, r3, r4);
    }

    private static final short hexToShort(String r2, int r3, int r4, HexFormat r5) {
        return (short) hexToLongImpl(r2, r3, r4, r5, 4);
    }

    public static final short hexToShort(String r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return hexToShort(r2, 0, r2.length(), r3);
    }

    static /* synthetic */ short hexToShort$default(String r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = HexFormat.Companion.getDefault();
    L12:
        return hexToShort(r0, r1, r2, r3);
    }

    public static /* synthetic */ short hexToShort$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return hexToShort(r0, r1);
    }

    public static final int parsedByteArrayMaxSize(int r22, int r23, int r24, int r25, int r26, int r27, int r28) {
        if (r22 <= 0) goto L5;
        boolean r7 = true;
    L6:
        if (r7 == false) goto L21;
        long r8 = (((long) r27) + 2) + ((long) r28);
        long r11 = charsPerSet(r8, r24, r26);
        if (r23 > r24) goto L10;
        long r13 = charsPerSet(r8, r23, r26);
    L14:
        long r5 = r22;
        long r16 = wholeElementsPerSet(r5, r13, 1);
        long r6 = r5 - ((r13 + 1) * r16);
        long r18 = wholeElementsPerSet(r6, r11, r25);
        long r9 = r6 - ((((long) r25) + r11) * r18);
        long r20 = wholeElementsPerSet(r9, r8, r26);
        if ((r9 - ((((long) r26) + r8) * r20)) <= 0) goto L17;
        int r15 = 1;
    L19:
        return (int) ((((((long) r23) * r16) + (((long) r24) * r18)) + r20) + ((long) r15));
    L17:
        r15 = 0;
        goto L19
    L10:
        long r14 = charsPerSet(r11, r23 / r24, r25);
        int r10 = r23 % r24;
        if (r10 == 0) goto L13;
        r14 = (r14 + ((long) r25)) + charsPerSet(r8, r10, r26);
    L13:
        r13 = r14;
        goto L14
    L21:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L5:
        r7 = false;
        goto L6
    }

    public static final String toHexString(byte r3, HexFormat r4) {
        Intrinsics.checkNotNullParameter(r4, MediaInformation.KEY_FORMAT_PROPERTIES);
        return toHexStringImpl(r3, r4, 8);
    }

    public static final String toHexString(int r3, HexFormat r4) {
        Intrinsics.checkNotNullParameter(r4, MediaInformation.KEY_FORMAT_PROPERTIES);
        return toHexStringImpl(r3, r4, 32);
    }

    public static final String toHexString(long r1, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return toHexStringImpl(r1, r3, 64);
    }

    public static final String toHexString(short r3, HexFormat r4) {
        Intrinsics.checkNotNullParameter(r4, MediaInformation.KEY_FORMAT_PROPERTIES);
        return toHexStringImpl(r3, r4, 16);
    }

    public static final String toHexString(byte[] r20, int r21, int r22, HexFormat r23) {
        Intrinsics.checkNotNullParameter(r20, "<this>");
        Intrinsics.checkNotNullParameter(r23, MediaInformation.KEY_FORMAT_PROPERTIES);
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r21, r22, r20.length);
        if (r21 != r22) goto L7;
        return "";
    L7:
        if (r23.getUpperCase() == false) goto L9;
        String r3 = UPPER_CASE_HEX_DIGITS;
    L10:
        HexFormat.BytesHexFormat r5 = r23.getBytes();
        int r7 = r5.getBytesPerLine();
        int r8 = r5.getBytesPerGroup();
        String r13 = r5.getBytePrefix();
        String r14 = r5.getByteSuffix();
        String r15 = r5.getByteSeparator();
        String r6 = r5.getGroupSeparator();
        int r9 = formattedStringLength(r22 - r21, r7, r8, r6.length(), r15.length(), r13.length(), r14.length());
        int r10 = 0;
        int r11 = 0;
        StringBuilder r12 = new StringBuilder(r9);
        int r1 = r21;
    L11:
        boolean r17 = true;
        if (r1 >= r22) goto L23;
        int r18 = r1;
        int r2 = r20[r18] & 255;
        if (r10 != r7) goto L16;
        int r19 = r2;
        r12.append('\n');
        r10 = 0;
        r11 = 0;
    L19:
        if (r11 == 0) goto L21;
        r12.append(r15);
    L21:
        r12.append(r13);
        r12.append(r3.charAt(r19 >> 4));
        r12.append(r3.charAt(r19 & 15));
        r12.append(r14);
        r11 = r11 + 1;
        r10 = r10 + 1;
        r1 = r18 + 1;
        goto L11
    L16:
        r19 = r2;
        if (r11 != r8) goto L19;
        r12.append(r6);
        r11 = 0;
        goto L19
    L23:
        if (r9 == r12.length()) goto L26;
        r17 = false;
    L26:
        if (r17 == false) goto L30;
        String r4 = r12.toString();
        Intrinsics.checkNotNullExpressionValue(r4, "toString(...)");
        return r4;
    L30:
        throw new IllegalStateException("Check failed.".toString());
    L9:
        r3 = LOWER_CASE_HEX_DIGITS;
        goto L10
    }

    public static final String toHexString(byte[] r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return toHexString(r2, 0, r2.length, r3);
    }

    public static /* synthetic */ String toHexString$default(byte r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return toHexString(r0, r1);
    }

    public static /* synthetic */ String toHexString$default(int r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return toHexString(r0, r1);
    }

    public static /* synthetic */ String toHexString$default(long r0, HexFormat r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r2 = HexFormat.Companion.getDefault();
    L6:
        return toHexString(r0, r2);
    }

    public static /* synthetic */ String toHexString$default(short r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return toHexString(r0, r1);
    }

    public static /* synthetic */ String toHexString$default(byte[] r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.length;
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = HexFormat.Companion.getDefault();
    L12:
        return toHexString(r0, r1, r2, r3);
    }

    public static /* synthetic */ String toHexString$default(byte[] r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = HexFormat.Companion.getDefault();
    L6:
        return toHexString(r0, r1);
    }

    private static final String toHexStringImpl(long r17, HexFormat r19, int r20) {
        if ((r20 & 3) != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L25;
        if (r19.getUpperCase() == false) goto L10;
        String r1 = UPPER_CASE_HEX_DIGITS;
    L11:
        String r5 = r19.getNumber().getPrefix();
        String r6 = r19.getNumber().getSuffix();
        int r7 = (r5.length() + (r20 >> 2)) + r6.length();
        boolean r8 = r19.getNumber().getRemoveLeadingZeros();
        StringBuilder r9 = new StringBuilder(r7);
        r9.append(r5);
        int r12 = r20;
    L12:
        if (r12 <= 0) goto L22;
        r12 = r12 - 4;
        int r13 = (int) ((r17 >> r12) & 15);
        if (r8 == false) goto L18;
        if (r13 != 0) goto L18;
        if (r12 <= 0) goto L18;
        boolean r14 = true;
    L19:
        r8 = r14;
        if (r8 == true) goto L12;
        r9.append(r1.charAt(r13));
    L18:
        r14 = false;
        goto L19
    L22:
        r9.append(r6);
        String r2 = r9.toString();
        Intrinsics.checkNotNullExpressionValue(r2, "toString(...)");
        return r2;
    L10:
        r1 = LOWER_CASE_HEX_DIGITS;
        goto L11
    L25:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L5:
        r0 = false;
        goto L6
    }

    private static final long wholeElementsPerSet(long r4, long r6, int r8) {
        if (r4 > 0) goto L5;
        return 0;
    L5:
        if (r6 > 0) goto L8;
        return 0;
    L8:
        return (((long) r8) + r4) / (((long) r8) + r6);
    }
}
