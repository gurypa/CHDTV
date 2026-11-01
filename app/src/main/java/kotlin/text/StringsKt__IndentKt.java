package kotlin.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: Indent.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\f\u0010\u0013\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0016\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002H\u0007¨\u0006\u0015"}, m421d2 = {"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt__IndentKt extends StringsKt__AppendableKt {
    public StringsKt__IndentKt() {
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(final String r1) {
        if (r1.length() != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
    L9:
        return new StringsKt__IndentKt$getIndentFunction$2(r1);
    L5:
        r0 = false;
        goto L6
    }

    private static final int indentWidth$StringsKt__IndentKt(String r8) {
        String r0 = r8;
        int r2 = 0;
        int r3 = r0.length();
    L4:
        if (r2 >= r3) goto L9;
        if (CharsKt.isWhitespace(r0.charAt(r2)) == false) goto L11;
        r2 = r2 + 1;
    L11:
        if (r2 == (-1)) goto L13;
        return r2;
    L13:
        return r8.length();
    L9:
        r2 = -1;
        goto L11
    }

    public static final String prependIndent(String r11, final String r12) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "indent");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence(r11), new C11591(r12)), "\n", null, null, 0, null, null, 62, null);
    }

    public static /* synthetic */ String prependIndent$default(String r0, String r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = "    ";
    L6:
        return StringsKt.prependIndent(r0, r1);
    }

    private static final String reindent$StringsKt__IndentKt(List<String> r20, int r21, Function1<? super String, String> r22, Function1<? super String, String> r23) {
        boolean r0 = false;
        int r1 = CollectionsKt.getLastIndex(r20);
        ArrayList r4 = new ArrayList();
        int r9 = 0;
        Iterator<T> r10 = r20.iterator();
    L4:
        if (r10.hasNext() == false) goto L32;
        Object r11 = r10.next();
        int r12 = r9 + 1;
        if (r9 < 0) goto L8;
    L12:
        String r15 = (String) r11;
        int r16 = r9;
        if (r16 == 0) goto L17;
        boolean r18 = r0;
        int r2 = r16;
        if (r2 == r1) goto L19;
    L21:
        String r3 = r23.invoke(r15);
        if (r3 == null) goto L26;
        int r19 = r1;
        String r5 = r22.invoke(r3);
        if (r5 != null) goto L29;
    L27:
        r5 = r15;
    L29:
        if (r5 == null) goto L31;
        r4.add(r5);
    L31:
        r9 = r12;
        r0 = r18;
        r1 = r19;
        goto L4
    L26:
        r19 = r1;
    L19:
        if (StringsKt.isBlank(r15) == false) goto L21;
        r5 = null;
        r19 = r1;
        goto L29
    L17:
        r18 = r0;
        r2 = r16;
        goto L19
    L8:
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0) == false) goto L11;
        CollectionsKt.throwIndexOverflow();
        goto L12
    L11:
        throw new ArithmeticException("Index overflow has happened.");
    L32:
        String r6 = ((StringBuilder) CollectionsKt.joinTo$default(r4, new StringBuilder(r21), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(r6, "toString(...)");
        return r6;
    }

    public static final String replaceIndent(String r27, String r28) {
        Intrinsics.checkNotNullParameter(r27, "<this>");
        Intrinsics.checkNotNullParameter(r28, "newIndent");
        List<String> r1 = StringsKt.lines(r27);
        ArrayList r5 = new ArrayList();
        Iterator<T> r8 = r1.iterator();
    L4:
        if (r8.hasNext() == false) goto L8;
        Object r9 = r8.next();
        if (StringsKt.isBlank((String) r9) == true) goto L4;
        r5.add(r9);
        goto L4
    L8:
        ArrayList r6 = r5;
        ArrayList r4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r6, 10));
        Iterator r10 = r6.iterator();
    L10:
        if (r10.hasNext() == false) goto L12;
        r4.add(Integer.valueOf(indentWidth$StringsKt__IndentKt((String) r10.next())));
        goto L10
    L12:
        Integer r3 = (Integer) CollectionsKt.minOrNull(r4);
        if (r3 == null) goto L15;
        int r7 = r3.intValue();
    L16:
        int r11 = r27.length() + (r28.length() * r1.size());
        Function1<String, String> r12 = getIndentFunction$StringsKt__IndentKt(r28);
        int r13 = CollectionsKt.getLastIndex(r1);
        ArrayList r14 = new ArrayList();
        int r16 = 0;
        Iterator<T> r17 = r1.iterator();
    L18:
        if (r17.hasNext() == false) goto L40;
        Object r18 = r17.next();
        int r19 = r16 + 1;
        if (r16 >= 0) goto L22;
        CollectionsKt.throwIndexOverflow();
    L22:
        String r22 = (String) r18;
        int r23 = r16;
        if (r23 == 0) goto L27;
        int r0 = r23;
        if (r0 == r13) goto L29;
    L31:
        String r2 = StringsKt.drop(r22, r7);
        if (r2 == null) goto L35;
        String r24 = r12.invoke(r2);
        if (r24 == null) goto L35;
    L37:
        if (r24 == null) goto L39;
        r14.add(r24);
    L39:
        r16 = r19;
    L35:
        r24 = r22;
    L29:
        if (StringsKt.isBlank(r22) == false) goto L31;
        r24 = null;
        goto L37
    L27:
        r0 = r23;
        goto L29
    L40:
        String r15 = ((StringBuilder) CollectionsKt.joinTo$default(r14, new StringBuilder(r11), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(r15, "toString(...)");
        return r15;
    L15:
        r7 = 0;
        goto L16
    }

    public static /* synthetic */ String replaceIndent$default(String r0, String r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = "";
    L6:
        return StringsKt.replaceIndent(r0, r1);
    }

    public static final String replaceIndentByMargin(String r34, String r35, String r36) {
        Intrinsics.checkNotNullParameter(r34, "<this>");
        Intrinsics.checkNotNullParameter(r35, "newIndent");
        Intrinsics.checkNotNullParameter(r36, "marginPrefix");
        if (StringsKt.isBlank(r36) == true) goto L43;
        List<String> r8 = StringsKt.lines(r34);
        int r9 = r34.length() + (r35.length() * r8.size());
        Function1<String, String> r10 = getIndentFunction$StringsKt__IndentKt(r35);
        int r13 = CollectionsKt.getLastIndex(r8);
        ArrayList r1 = new ArrayList();
        int r3 = 0;
        Iterator<T> r20 = r8.iterator();
    L6:
        if (r20.hasNext() == false) goto L40;
        Object r21 = r20.next();
        int r22 = r3 + 1;
        if (r3 >= 0) goto L10;
        CollectionsKt.throwIndexOverflow();
    L10:
        String r26 = (String) r21;
        String r28 = null;
        if (r3 == 0) goto L14;
        if (r3 == r13) goto L14;
    L16:
        ArrayList r4 = r1;
        String r5 = r26;
        int r0 = r5.length();
        int r2 = 0;
    L18:
        if (r2 >= r0) goto L23;
        if (CharsKt.isWhitespace(r5.charAt(r2)) == false) goto L25;
        r2 = r2 + 1;
    L25:
        if (r2 != (-1)) goto L27;
        ArrayList r6 = r4;
        String r7 = null;
    L32:
        if (r7 == null) goto L35;
        r28 = r10.invoke(r7);
        if (r28 == null) goto L35;
    L37:
        if (r28 == null) goto L39;
        r6.add(r28);
    L39:
        r1 = r6;
        r3 = r22;
    L35:
        r28 = r26;
        goto L37
    L27:
        r6 = r4;
        int r11 = r2;
        if (StringsKt.startsWith$default(r26, r36, r11, false, 4, null) == false) goto L30;
        int r12 = r36.length() + r11;
        Intrinsics.checkNotNull(r26, "null cannot be cast to non-null type java.lang.String");
        r7 = r26.substring(r12);
        Intrinsics.checkNotNullExpressionValue(r7, "substring(...)");
        goto L32
    L30:
        r7 = null;
        goto L32
    L23:
        r2 = -1;
    L14:
        if (StringsKt.isBlank(r26) == false) goto L16;
        r6 = r1;
        goto L37
    L40:
        String r14 = ((StringBuilder) CollectionsKt.joinTo$default(r1, new StringBuilder(r9), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(r14, "toString(...)");
        return r14;
    L43:
        throw new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
    }

    public static /* synthetic */ String replaceIndentByMargin$default(String r0, String r1, String r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = "";
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = "|";
    L9:
        return StringsKt.replaceIndentByMargin(r0, r1, r2);
    }

    public static final String trimIndent(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.replaceIndent(r1, "");
    }

    public static final String trimMargin(String r1, String r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "marginPrefix");
        return StringsKt.replaceIndentByMargin(r1, "", r2);
    }

    public static /* synthetic */ String trimMargin$default(String r0, String r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = "|";
    L6:
        return StringsKt.trimMargin(r0, r1);
    }
}
