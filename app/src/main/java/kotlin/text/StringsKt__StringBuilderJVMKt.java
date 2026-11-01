package kotlin.text;

import com.arthenica.ffmpegkit.StreamInformation;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StringBuilderJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\\\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u0012H\u0007\u001a\u001d\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0014H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0015H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0016H\u0087\b\u001a%\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a\u0014\u0010\u0017\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001d\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\bH\u0087\b\u001a%\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a!\u0010\u001c\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\n\u001a-\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0016H\u0087\b\u001a7\u0010\u001f\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010 \u001a\u00020\f2\b\b\u0002\u0010!\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\bH\u0087\b¨\u0006\""}, m421d2 = {"appendLine", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "value", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "appendRange", "", "startIndex", "endIndex", "", "appendln", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "", "", "", "", "clear", "deleteAt", StreamInformation.KEY_INDEX, "deleteRange", "insertRange", "set", "", "setRange", "toCharArray", "destination", "destinationOffset", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt__StringBuilderJVMKt extends StringsKt__RegexExtensionsKt {
    public StringsKt__StringBuilderJVMKt() {
    }

    private static final StringBuilder appendLine(StringBuilder r3, byte r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, double r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, float r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, StringBuffer r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, StringBuilder r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendLine(StringBuilder r3, short r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        StringBuilder r0 = r3.append(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        StringBuilder r1 = r0.append('\n');
        Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
        return r1;
    }

    private static final StringBuilder appendRange(StringBuilder r2, CharSequence r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "value");
        StringBuilder r0 = r2.append(r3, r4, r5);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return r0;
    }

    private static final StringBuilder appendRange(StringBuilder r2, char[] r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "value");
        StringBuilder r0 = r2.append(r3, r4, r5 - r4);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return r0;
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine()", imports = {}))
    public static final Appendable appendln(Appendable r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Appendable r0 = r2.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return r0;
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final Appendable appendln(Appendable r2, char r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Appendable r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final Appendable appendln(Appendable r2, CharSequence r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Appendable r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine()", imports = {}))
    public static final StringBuilder appendln(StringBuilder r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return r0;
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, byte r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, char r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, double r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, float r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, long r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, CharSequence r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, Object r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, String r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, StringBuffer r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, StringBuilder r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, short r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final StringBuilder appendln(StringBuilder r2, char[] r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "value");
        StringBuilder r0 = r2.append(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
        return StringsKt.appendln(r0);
    }

    public static final StringBuilder clear(StringBuilder r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        r3.setLength(0);
        return r3;
    }

    private static final StringBuilder deleteAt(StringBuilder r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.deleteCharAt(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "deleteCharAt(...)");
        return r0;
    }

    private static final StringBuilder deleteRange(StringBuilder r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        StringBuilder r0 = r2.delete(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r0, "delete(...)");
        return r0;
    }

    private static final StringBuilder insertRange(StringBuilder r2, int r3, CharSequence r4, int r5, int r6) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, "value");
        StringBuilder r0 = r2.insert(r3, r4, r5, r6);
        Intrinsics.checkNotNullExpressionValue(r0, "insert(...)");
        return r0;
    }

    private static final StringBuilder insertRange(StringBuilder r2, int r3, char[] r4, int r5, int r6) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, "value");
        StringBuilder r0 = r2.insert(r3, r4, r5, r6 - r5);
        Intrinsics.checkNotNullExpressionValue(r0, "insert(...)");
        return r0;
    }

    private static final void set(StringBuilder r1, int r2, char r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.setCharAt(r2, r3);
    }

    private static final StringBuilder setRange(StringBuilder r2, int r3, int r4, String r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r5, "value");
        StringBuilder r0 = r2.replace(r3, r4, r5);
        Intrinsics.checkNotNullExpressionValue(r0, "replace(...)");
        return r0;
    }

    private static final void toCharArray(StringBuilder r1, char[] r2, int r3, int r4, int r5) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "destination");
        r1.getChars(r4, r5, r2, r3);
    }

    static /* synthetic */ void toCharArray$default(StringBuilder r0, char[] r1, int r2, int r3, int r4, int r5, Object r6) {
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
    }
}
