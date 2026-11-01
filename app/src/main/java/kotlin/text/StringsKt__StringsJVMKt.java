package kotlin.text;

import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: StringsJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000~\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\n\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\u0019\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a)\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a\f\u0010\u0017\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010\u0017\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010\u001d\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010 \u001a\u00020\u0011*\u00020\u00022\u0006\u0010!\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\f\u0010$\u001a\u00020\u0002*\u00020\u0014H\u0007\u001a \u0010$\u001a\u00020\u0002*\u00020\u00142\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\u0019\u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'H\u0087\u0004\u001a \u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'2\u0006\u0010\"\u001a\u00020#H\u0007\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0087\b\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010(\u001a\u00020'H\u0087\b\u001a\f\u0010)\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010)\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\f\u0010*\u001a\u00020\u0002*\u00020\rH\u0007\u001a*\u0010*\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\f\u0010,\u001a\u00020\r*\u00020\u0002H\u0007\u001a*\u0010,\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\u001c\u0010-\u001a\u00020#*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a \u0010/\u001a\u00020#*\u0004\u0018\u00010\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u00100\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00104\u001a*\u00100\u001a\u00020\u0002*\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00105\u001a<\u00100\u001a\u00020\u0002*\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00106\u001a2\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00107\u001a\r\u00108\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u00109\u001a\u00020#*\u00020'\u001a\r\u0010:\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010:\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u001d\u0010;\u001a\u00020\u0011*\u00020\u00022\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010;\u001a\u00020\u0011*\u00020\u00022\u0006\u0010?\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010@\u001a\u00020\u0011*\u00020\u00022\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010@\u001a\u00020\u0011*\u00020\u00022\u0006\u0010?\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010B\u001a\u00020\u0011H\u0087\b\u001a4\u0010C\u001a\u00020#*\u00020'2\u0006\u0010D\u001a\u00020\u00112\u0006\u0010!\u001a\u00020'2\u0006\u0010E\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u0010C\u001a\u00020#*\u00020\u00022\u0006\u0010D\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0012\u0010F\u001a\u00020\u0002*\u00020'2\u0006\u0010G\u001a\u00020\u0011\u001a$\u0010H\u001a\u00020\u0002*\u00020\u00022\u0006\u0010I\u001a\u00020=2\u0006\u0010J\u001a\u00020=2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010H\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010M\u001a\u00020\u0002*\u00020\u00022\u0006\u0010I\u001a\u00020=2\u0006\u0010J\u001a\u00020=2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010M\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\"\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00020O*\u00020'2\u0006\u0010P\u001a\u00020Q2\b\b\u0002\u0010R\u001a\u00020\u0011\u001a\u001c\u0010S\u001a\u00020#*\u00020\u00022\u0006\u0010T\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010S\u001a\u00020#*\u00020\u00022\u0006\u0010T\u001a\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0015\u0010U\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010U\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u0017\u0010V\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\r\u0010W\u001a\u00020\u0014*\u00020\u0002H\u0087\b\u001a3\u0010W\u001a\u00020\u0014*\u00020\u00022\u0006\u0010X\u001a\u00020\u00142\b\b\u0002\u0010Y\u001a\u00020\u00112\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a \u0010W\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\r\u0010Z\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010Z\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u0017\u0010[\u001a\u00020Q*\u00020\u00022\b\b\u0002\u0010\\\u001a\u00020\u0011H\u0087\b\u001a\r\u0010]\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010]\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\r\u0010^\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010^\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\"%\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00020\u0002`\u0003*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006_"}, m421d2 = {"CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/Comparator;", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "locale", "Ljava/util/Locale;", "codePointAt", StreamInformation.KEY_INDEX, "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "concatToString", "startIndex", "contentEquals", "", "charSequence", "decapitalize", "decodeToString", "throwOnInvalidSequence", "encodeToByteArray", "endsWith", "suffix", "equals", MediaInformation.KEY_FORMAT_PROPERTIES, "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "lowercase", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "uppercase", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt {
    public StringsKt__StringsJVMKt() {
    }

    private static final String String(StringBuffer r1) {
        Intrinsics.checkNotNullParameter(r1, "stringBuffer");
        return new String(r1);
    }

    private static final String String(StringBuilder r1) {
        Intrinsics.checkNotNullParameter(r1, "stringBuilder");
        return new String(r1);
    }

    private static final String String(byte[] r2) {
        Intrinsics.checkNotNullParameter(r2, "bytes");
        return new String(r2, Charsets.UTF_8);
    }

    private static final String String(byte[] r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "bytes");
        return new String(r2, r3, r4, Charsets.UTF_8);
    }

    private static final String String(byte[] r1, int r2, int r3, Charset r4) {
        Intrinsics.checkNotNullParameter(r1, "bytes");
        Intrinsics.checkNotNullParameter(r4, "charset");
        return new String(r1, r2, r3, r4);
    }

    private static final String String(byte[] r1, Charset r2) {
        Intrinsics.checkNotNullParameter(r1, "bytes");
        Intrinsics.checkNotNullParameter(r2, "charset");
        return new String(r1, r2);
    }

    private static final String String(char[] r1) {
        Intrinsics.checkNotNullParameter(r1, "chars");
        return new String(r1);
    }

    private static final String String(char[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "chars");
        return new String(r1, r2, r3);
    }

    private static final String String(int[] r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "codePoints");
        return new String(r1, r2, r3);
    }

    @Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @ReplaceWith(expression = "replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }", imports = {"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    public static final String capitalize(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Locale r0 = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(r0, "getDefault(...)");
        return StringsKt.capitalize(r2, r0);
    }

    @Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @ReplaceWith(expression = "replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    public static final String capitalize(String r9, Locale r10) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        Intrinsics.checkNotNullParameter(r10, "locale");
        if (r9.length() <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L15;
        char r1 = r9.charAt(0);
        if (Character.isLowerCase(r1) == false) goto L15;
        StringBuilder r3 = new StringBuilder();
        char r6 = Character.toTitleCase(r1);
        if (r6 == Character.toUpperCase(r1)) goto L12;
        r3.append(r6);
    L13:
        String r2 = r9.substring(1);
        Intrinsics.checkNotNullExpressionValue(r2, "substring(...)");
        r3.append(r2);
        String r4 = r3.toString();
        Intrinsics.checkNotNullExpressionValue(r4, "toString(...)");
        return r4;
    L12:
        String r5 = r9.substring(0, 1);
        Intrinsics.checkNotNullExpressionValue(r5, "substring(...)");
        Intrinsics.checkNotNull(r5, "null cannot be cast to non-null type java.lang.String");
        String r7 = r5.toUpperCase(r10);
        Intrinsics.checkNotNullExpressionValue(r7, "toUpperCase(...)");
        r3.append(r7);
    L15:
        return r9;
    L5:
        r0 = false;
        goto L6
    }

    private static final int codePointAt(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.codePointAt(r2);
    }

    private static final int codePointBefore(String r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.codePointBefore(r2);
    }

    private static final int codePointCount(String r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.codePointCount(r2, r3);
    }

    public static final int compareTo(String r1, String r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "other");
        if (r3 == false) goto L7;
        return r1.compareToIgnoreCase(r2);
    L7:
        return r1.compareTo(r2);
    }

    public static /* synthetic */ int compareTo$default(String r0, String r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.compareTo(r0, r1, r2);
    }

    public static final String concatToString(char[] r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new String(r1);
    }

    public static final String concatToString(char[] r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r3, r4, r2.length);
        return new String(r2, r3, r4 - r3);
    }

    public static /* synthetic */ String concatToString$default(char[] r0, int r1, int r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = r0.length;
    L9:
        return StringsKt.concatToString(r0, r1, r2);
    }

    public static final boolean contentEquals(CharSequence r1, CharSequence r2) {
        if ((r1 instanceof String) == false) goto L7;
        if (r2 == null) goto L7;
        return ((String) r1).contentEquals(r2);
    L7:
        return StringsKt.contentEqualsImpl(r1, r2);
    }

    public static final boolean contentEquals(CharSequence r1, CharSequence r2, boolean r3) {
        if (r3 == false) goto L5;
        return StringsKt.contentEqualsIgnoreCaseImpl(r1, r2);
    L5:
        return StringsKt.contentEquals(r1, r2);
    }

    private static final boolean contentEquals(String r1, CharSequence r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "charSequence");
        return r1.contentEquals(r2);
    }

    private static final boolean contentEquals(String r1, StringBuffer r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "stringBuilder");
        return r1.contentEquals(r2);
    }

    @Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @ReplaceWith(expression = "replaceFirstChar { it.lowercase(Locale.getDefault()) }", imports = {"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    public static final String decapitalize(String r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        if (r5.length() <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L11;
        if (Character.isLowerCase(r5.charAt(0)) == true) goto L11;
        StringBuilder r1 = new StringBuilder();
        String r2 = r5.substring(0, 1);
        Intrinsics.checkNotNullExpressionValue(r2, "substring(...)");
        Intrinsics.checkNotNull(r2, "null cannot be cast to non-null type java.lang.String");
        String r3 = r2.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(r3, "toLowerCase(...)");
        StringBuilder r4 = r1.append(r3);
        String r6 = r5.substring(1);
        Intrinsics.checkNotNullExpressionValue(r6, "substring(...)");
        return r4.append(r6).toString();
    L11:
        return r5;
    L5:
        r0 = false;
        goto L6
    }

    @Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @ReplaceWith(expression = "replaceFirstChar { it.lowercase(locale) }", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    public static final String decapitalize(String r5, Locale r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "locale");
        if (r5.length() <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L11;
        if (Character.isLowerCase(r5.charAt(0)) == true) goto L11;
        StringBuilder r1 = new StringBuilder();
        String r2 = r5.substring(0, 1);
        Intrinsics.checkNotNullExpressionValue(r2, "substring(...)");
        Intrinsics.checkNotNull(r2, "null cannot be cast to non-null type java.lang.String");
        String r3 = r2.toLowerCase(r6);
        Intrinsics.checkNotNullExpressionValue(r3, "toLowerCase(...)");
        StringBuilder r4 = r1.append(r3);
        String r7 = r5.substring(1);
        Intrinsics.checkNotNullExpressionValue(r7, "substring(...)");
        return r4.append(r7).toString();
    L11:
        return r5;
    L5:
        r0 = false;
        goto L6
    }

    public static final String decodeToString(byte[] r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new String(r2, Charsets.UTF_8);
    }

    public static final String decodeToString(byte[] r3, int r4, int r5, boolean r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r4, r5, r3.length);
        if (r6 == false) goto L5;
        String r1 = Charsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(r3, r4, r5 - r4)).toString();
        Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
        return r1;
    L5:
        return new String(r3, r4, r5 - r4, Charsets.UTF_8);
    }

    public static /* synthetic */ String decodeToString$default(byte[] r1, int r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r5 & 2) == 0) goto L9;
        r3 = r1.length;
    L9:
        if ((r5 & 4) == 0) goto L12;
        r4 = false;
    L12:
        return StringsKt.decodeToString(r1, r2, r3, r4);
    }

    public static final byte[] encodeToByteArray(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        byte[] r0 = r2.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(r0, "getBytes(...)");
        return r0;
    }

    public static final byte[] encodeToByteArray(String r5, int r6, int r7, boolean r8) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r6, r7, r5.length());
        if (r8 == true) goto L6;
        String r0 = r5.substring(r6, r7);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        Charset r1 = Charsets.UTF_8;
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        byte[] r2 = r0.getBytes(r1);
        Intrinsics.checkNotNullExpressionValue(r2, "getBytes(...)");
        return r2;
    L6:
        ByteBuffer r3 = Charsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT).encode(CharBuffer.wrap(r5, r6, r7));
        if (r3.hasArray() == true) goto L9;
    L13:
        byte[] r4 = new byte[r3.remaining()];
        r3.get(r4);
        return r4;
    L9:
        if (r3.arrayOffset() != 0) goto L13;
        int r9 = r3.remaining();
        byte[] r10 = r3.array();
        Intrinsics.checkNotNull(r10);
        if (r9 != r10.length) goto L13;
        byte[] r11 = r3.array();
        Intrinsics.checkNotNull(r11);
        return r11;
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(String r1, int r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 1) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r5 & 2) == 0) goto L9;
        r3 = r1.length();
    L9:
        if ((r5 & 4) == 0) goto L12;
        r4 = false;
    L12:
        return StringsKt.encodeToByteArray(r1, r2, r3, r4);
    }

    public static final boolean endsWith(String r8, String r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "suffix");
        if (r10 == true) goto L7;
        return r8.endsWith(r9);
    L7:
        return StringsKt.regionMatches(r8, r8.length() - r9.length(), r9, 0, r9.length(), true);
    }

    public static /* synthetic */ boolean endsWith$default(String r0, String r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.endsWith(r0, r1, r2);
    }

    public static final boolean equals(String r1, String r2, boolean r3) {
        if (r1 != null) goto L7;
        if (r2 != null) goto L5;
        return true;
    L5:
        return false;
    L7:
        if (r3 == true) goto L10;
        return r1.equals(r2);
    L10:
        return r1.equalsIgnoreCase(r2);
    }

    public static /* synthetic */ boolean equals$default(String r0, String r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.equals(r0, r1, r2);
    }

    private static final String format(String r2, Locale r3, Object... r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, "args");
        String r0 = String.format(r3, r2, Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "format(...)");
        return r0;
    }

    private static final String format(String r2, Object... r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "args");
        String r0 = String.format(r2, Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "format(...)");
        return r0;
    }

    private static final String format(StringCompanionObject r2, String r3, Object... r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        Intrinsics.checkNotNullParameter(r4, "args");
        String r0 = String.format(r3, Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "format(...)");
        return r0;
    }

    private static final String format(StringCompanionObject r2, Locale r3, String r4, Object... r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, MediaInformation.KEY_FORMAT_PROPERTIES);
        Intrinsics.checkNotNullParameter(r5, "args");
        String r0 = String.format(r3, r4, Arrays.copyOf(r5, r5.length));
        Intrinsics.checkNotNullExpressionValue(r0, "format(...)");
        return r0;
    }

    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(StringCompanionObject r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Comparator<String> r0 = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkNotNullExpressionValue(r0, "CASE_INSENSITIVE_ORDER");
        return r0;
    }

    private static final String intern(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.intern();
        Intrinsics.checkNotNullExpressionValue(r0, "intern(...)");
        return r0;
    }

    public static final boolean isBlank(CharSequence r9) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        if (r9.length() == 0) goto L24;
        Iterable r0 = StringsKt.getIndices(r9);
        if ((r0 instanceof Collection) == true) goto L7;
    L9:
        Iterator r3 = r0.iterator();
    L11:
        if (r3.hasNext() == false) goto L15;
        if (CharsKt.isWhitespace(r9.charAt(((IntIterator) r3).nextInt())) == true) goto L11;
        boolean r1 = false;
    L16:
        if (r1 == false) goto L20;
        return true;
    L20:
        return false;
    L15:
        r1 = true;
        goto L16
    L7:
        if (((Collection) r0).isEmpty() == false) goto L9;
        r1 = true;
        goto L16
    L24:
        return true;
    }

    private static final String lowercase(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r0, "toLowerCase(...)");
        return r0;
    }

    private static final String lowercase(String r2, Locale r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "locale");
        String r0 = r2.toLowerCase(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "toLowerCase(...)");
        return r0;
    }

    private static final int nativeIndexOf(String r1, char r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.indexOf(r2, r3);
    }

    private static final int nativeIndexOf(String r1, String r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "str");
        return r1.indexOf(r2, r3);
    }

    private static final int nativeLastIndexOf(String r1, char r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.lastIndexOf(r2, r3);
    }

    private static final int nativeLastIndexOf(String r1, String r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "str");
        return r1.lastIndexOf(r2, r3);
    }

    private static final int offsetByCodePoints(String r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.offsetByCodePoints(r2, r3);
    }

    public static final boolean regionMatches(CharSequence r7, int r8, CharSequence r9, int r10, int r11, boolean r12) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "other");
        if ((r7 instanceof String) == false) goto L9;
        if ((r9 instanceof String) == false) goto L9;
        return StringsKt.regionMatches((String) r7, r8, (String) r9, r10, r11, r12);
    L9:
        return StringsKt.regionMatchesImpl(r7, r8, r9, r10, r11, r12);
    }

    public static final boolean regionMatches(String r2, int r3, String r4, int r5, int r6, boolean r7) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, "other");
        if (r7 == true) goto L6;
        return r2.regionMatches(r3, r4, r5, r6);
    L6:
        return r2.regionMatches(r7, r3, r4, r5, r6);
    }

    public static /* synthetic */ boolean regionMatches$default(CharSequence r6, int r7, CharSequence r8, int r9, int r10, boolean r11, int r12, Object r13) {
        if ((r12 & 16) == 0) goto L6;
        r11 = false;
    L6:
        return StringsKt.regionMatches(r6, r7, r8, r9, r10, r11);
    }

    public static /* synthetic */ boolean regionMatches$default(String r6, int r7, String r8, int r9, int r10, boolean r11, int r12, Object r13) {
        if ((r12 & 16) == 0) goto L6;
        r11 = false;
    L6:
        return StringsKt.regionMatches(r6, r7, r8, r9, r10, r11);
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [kotlin.collections.IntIterator] */
    public static final String repeat(CharSequence r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        int r1 = 0;
        if (r5 < 0) goto L5;
        boolean r2 = true;
    L6:
        if (r2 == false) goto L25;
        switch(r5) {
            case 0: goto L30;
            case 1: goto L28;
            default: goto L10;
        };
    L30:
        return "";
    L10:
        switch(r4.length()) {
            case 0: goto L31;
            case 1: goto L14;
            default: goto L11;
        };
    L11:
        StringBuilder r3 = new StringBuilder(r4.length() * r5);
        ?? r0 = new IntRange(1, r5).iterator();
    L20:
        if (r0.hasNext() == false) goto L22;
        r0.nextInt();
        r3.append(r4);
        goto L20
    L22:
        String r6 = r3.toString();
        Intrinsics.checkNotNull(r6);
        return r6;
    L14:
        char r7 = r4.charAt(0);
        char[] r8 = new char[r5];
    L15:
        if (r1 >= r5) goto L29;
        r8[r1] = r7;
        r1 = r1 + 1;
        goto L15
    L29:
        return new String(r8);
    L31:
        return "";
    L28:
        return r4.toString();
    L25:
        throw new IllegalArgumentException(("Count 'n' must be non-negative, but was " + r5 + '.').toString());
    L5:
        r2 = false;
        goto L6
    }

    public static final String replace(String r10, char r11, char r12, boolean r13) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        if (r13 == true) goto L6;
        String r0 = r10.replace(r11, r12);
        Intrinsics.checkNotNullExpressionValue(r0, "replace(...)");
        return r0;
    L6:
        StringBuilder r1 = new StringBuilder(r10.length());
        String r3 = r10;
        int r5 = 0;
    L8:
        if (r5 >= r3.length()) goto L14;
        char r6 = r3.charAt(r5);
        if (CharsKt.equals(r6, r11, r13) == false) goto L12;
        char r9 = r12;
    L13:
        r1.append(r9);
        r5 = r5 + 1;
        goto L8
    L12:
        r9 = r6;
        goto L13
    L14:
        String r2 = r1.toString();
        Intrinsics.checkNotNullExpressionValue(r2, "toString(...)");
        return r2;
    }

    public static final String replace(String r10, String r11, String r12, boolean r13) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        Intrinsics.checkNotNullParameter(r11, "oldValue");
        Intrinsics.checkNotNullParameter(r12, "newValue");
        int r2 = StringsKt.indexOf(r10, r11, 0, r13);
        if (r2 >= 0) goto L5;
        return r10;
    L5:
        int r3 = r11.length();
        int r4 = RangesKt.coerceAtLeast(r3, 1);
        int r5 = (r10.length() - r3) + r12.length();
        if (r5 < 0) goto L15;
        StringBuilder r6 = new StringBuilder(r5);
        int r7 = 0;
    L8:
        r6.append(r10, r7, r2).append(r12);
        r7 = r2 + r3;
        if (r2 >= r10.length()) goto L12;
        r2 = StringsKt.indexOf(r10, r11, r2 + r4, r13);
        if (r2 > 0) goto L8;
    L12:
        String r8 = r6.append(r10, r7, r10.length()).toString();
        Intrinsics.checkNotNullExpressionValue(r8, "toString(...)");
        return r8;
    L15:
        throw new OutOfMemoryError();
    }

    public static /* synthetic */ String replace$default(String r0, char r1, char r2, boolean r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = false;
    L6:
        return StringsKt.replace(r0, r1, r2, r3);
    }

    public static /* synthetic */ String replace$default(String r0, String r1, String r2, boolean r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = false;
    L6:
        return StringsKt.replace(r0, r1, r2, r3);
    }

    public static final String replaceFirst(String r7, char r8, char r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        int r11 = StringsKt.indexOf$default(r7, r8, 0, r10, 2, null);
        if (r11 < 0) goto L7;
        String r1 = r7;
        return StringsKt.replaceRange(r1, r11, r11 + 1, String.valueOf(r9)).toString();
    L7:
        return r7;
    }

    public static final String replaceFirst(String r7, String r8, String r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "oldValue");
        Intrinsics.checkNotNullParameter(r9, "newValue");
        int r11 = StringsKt.indexOf$default(r7, r8, 0, r10, 2, null);
        if (r11 < 0) goto L7;
        String r0 = r7;
        return StringsKt.replaceRange(r0, r11, r8.length() + r11, r9).toString();
    L7:
        return r7;
    }

    public static /* synthetic */ String replaceFirst$default(String r0, char r1, char r2, boolean r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = false;
    L6:
        return StringsKt.replaceFirst(r0, r1, r2, r3);
    }

    public static /* synthetic */ String replaceFirst$default(String r0, String r1, String r2, boolean r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = false;
    L6:
        return StringsKt.replaceFirst(r0, r1, r2, r3);
    }

    public static final List<String> split(CharSequence r2, Pattern r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "regex");
        StringsKt.requireNonNegativeLimit(r4);
        if (r4 != 0) goto L5;
        int r0 = -1;
    L6:
        String[] r1 = r3.split(r2, r0);
        Intrinsics.checkNotNullExpressionValue(r1, "split(...)");
        return ArraysKt.asList(r1);
    L5:
        r0 = r4;
        goto L6
    }

    public static /* synthetic */ List split$default(CharSequence r0, Pattern r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        return StringsKt.split(r0, r1, r2);
    }

    public static final boolean startsWith(String r7, String r8, int r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "prefix");
        if (r10 == true) goto L7;
        return r7.startsWith(r8, r9);
    L7:
        return StringsKt.regionMatches(r7, r9, r8, 0, r8.length(), r10);
    }

    public static final boolean startsWith(String r7, String r8, boolean r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "prefix");
        if (r9 == true) goto L7;
        return r7.startsWith(r8);
    L7:
        return StringsKt.regionMatches(r7, 0, r8, 0, r8.length(), r9);
    }

    public static /* synthetic */ boolean startsWith$default(String r0, String r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = false;
    L6:
        return StringsKt.startsWith(r0, r1, r2, r3);
    }

    public static /* synthetic */ boolean startsWith$default(String r0, String r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.startsWith(r0, r1, r2);
    }

    private static final String substring(String r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.substring(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    }

    private static final String substring(String r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.substring(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    }

    private static final byte[] toByteArray(String r2, Charset r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "charset");
        byte[] r0 = r2.getBytes(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "getBytes(...)");
        return r0;
    }

    static /* synthetic */ byte[] toByteArray$default(String r0, Charset r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = Charsets.UTF_8;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "charset");
        byte[] r4 = r0.getBytes(r1);
        Intrinsics.checkNotNullExpressionValue(r4, "getBytes(...)");
        return r4;
    }

    private static final char[] toCharArray(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        char[] r0 = r2.toCharArray();
        Intrinsics.checkNotNullExpressionValue(r0, "toCharArray(...)");
        return r0;
    }

    public static final char[] toCharArray(String r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r3, r4, r2.length());
        char[] r0 = new char[r4 - r3];
        r2.getChars(r3, r4, r0, 0);
        return r0;
    }

    private static final char[] toCharArray(String r1, char[] r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "destination");
        r1.getChars(r4, r5, r2, r3);
        return r2;
    }

    public static /* synthetic */ char[] toCharArray$default(String r0, int r1, int r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = r0.length();
    L9:
        return StringsKt.toCharArray(r0, r1, r2);
    }

    static /* synthetic */ char[] toCharArray$default(String r0, char[] r1, int r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r3 = 0;
    L9:
        if ((r5 & 8) == 0) goto L11;
        r4 = r0.length();
    L11:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "destination");
        r0.getChars(r3, r4, r1, r2);
        return r1;
    }

    @Deprecated(message = "Use lowercase() instead.", replaceWith = @ReplaceWith(expression = "lowercase(Locale.getDefault())", imports = {"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final String toLowerCase(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(r0, "toLowerCase(...)");
        return r0;
    }

    @Deprecated(message = "Use lowercase() instead.", replaceWith = @ReplaceWith(expression = "lowercase(locale)", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final String toLowerCase(String r2, Locale r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "locale");
        String r0 = r2.toLowerCase(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "toLowerCase(...)");
        return r0;
    }

    private static final Pattern toPattern(String r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Pattern r0 = Pattern.compile(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "compile(...)");
        return r0;
    }

    static /* synthetic */ Pattern toPattern$default(String r0, int r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = 0;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Pattern r4 = Pattern.compile(r0, r1);
        Intrinsics.checkNotNullExpressionValue(r4, "compile(...)");
        return r4;
    }

    @Deprecated(message = "Use uppercase() instead.", replaceWith = @ReplaceWith(expression = "uppercase(Locale.getDefault())", imports = {"java.util.Locale"}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final String toUpperCase(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.toUpperCase();
        Intrinsics.checkNotNullExpressionValue(r0, "toUpperCase(...)");
        return r0;
    }

    @Deprecated(message = "Use uppercase() instead.", replaceWith = @ReplaceWith(expression = "uppercase(locale)", imports = {}))
    @DeprecatedSinceKotlin(warningSince = "1.5")
    private static final String toUpperCase(String r2, Locale r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "locale");
        String r0 = r2.toUpperCase(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "toUpperCase(...)");
        return r0;
    }

    private static final String uppercase(String r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(r0, "toUpperCase(...)");
        return r0;
    }

    private static final String uppercase(String r2, Locale r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "locale");
        String r0 = r2.toUpperCase(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "toUpperCase(...)");
        return r0;
    }
}
