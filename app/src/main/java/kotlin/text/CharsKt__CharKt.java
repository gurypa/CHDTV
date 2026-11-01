package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.CharRange;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: Char.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\f\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0007\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\f\u0010\u0004\u001a\u00020\u0002*\u00020\u0001H\u0007\u001a\u0014\u0010\u0004\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0007\u001a\u001c\u0010\b\u001a\u00020\t*\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\t\u001a\n\u0010\f\u001a\u00020\t*\u00020\u0001\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00012\u0006\u0010\n\u001a\u00020\u000eH\u0087\n\u001a\f\u0010\u000f\u001a\u00020\u000e*\u00020\u0001H\u0007¨\u0006\u0010"}, m421d2 = {"digitToChar", "", "", "radix", "digitToInt", "digitToIntOrNull", "(C)Ljava/lang/Integer;", "(CI)Ljava/lang/Integer;", "equals", "", "other", "ignoreCase", "isSurrogate", "plus", "", "titlecase", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/CharsKt")
class CharsKt__CharKt extends CharsKt__CharJVMKt {
    public CharsKt__CharKt() {
    }

    public static final char digitToChar(int r3) {
        if (new IntRange(0, 9).contains(r3) == false) goto L7;
        return (char) (r3 + 48);
    L7:
        throw new IllegalArgumentException("Int " + r3 + " is not a decimal digit");
    }

    public static final char digitToChar(int r3, int r4) {
        if (new IntRange(2, 36).contains(r4) == false) goto L14;
        if (r3 < 0) goto L12;
        if (r3 >= r4) goto L12;
        if (r3 >= 10) goto L10;
        return (char) (r3 + 48);
    L10:
        return (char) (((char) (r3 + 65)) - '\n');
    L12:
        throw new IllegalArgumentException("Digit " + r3 + " does not represent a valid digit in radix " + r4);
    L14:
        throw new IllegalArgumentException("Invalid radix: " + r4 + ". Valid radix values are in range 2..36");
    }

    public static final int digitToInt(char r5) {
        int r0 = CharsKt.digitOf(r5, 10);
        if (r0 < 0) goto L6;
        return r0;
    L6:
        throw new IllegalArgumentException("Char " + r5 + " is not a decimal digit");
    }

    public static final int digitToInt(char r3, int r4) {
        Integer r0 = CharsKt.digitToIntOrNull(r3, r4);
        if (r0 == null) goto L7;
        return r0.intValue();
    L7:
        throw new IllegalArgumentException("Char " + r3 + " is not a digit in the given radix=" + r4);
    }

    public static final Integer digitToIntOrNull(char r4) {
        Integer r0 = Integer.valueOf(CharsKt.digitOf(r4, 10));
        if (r0.intValue() < 0) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == true) goto L10;
        return null;
    L10:
        return r0;
    L5:
        r3 = false;
        goto L6
    }

    public static final Integer digitToIntOrNull(char r4, int r5) {
        CharsKt.checkRadix(r5);
        Integer r0 = Integer.valueOf(CharsKt.digitOf(r4, r5));
        if (r0.intValue() < 0) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == true) goto L10;
        return null;
    L10:
        return r0;
    L5:
        r3 = false;
        goto L6
    }

    public static final boolean equals(char r6, char r7, boolean r8) {
        if (r6 != r7) goto L6;
        return true;
    L6:
        if (r8 == true) goto L8;
        return false;
    L8:
        char r2 = Character.toUpperCase(r6);
        char r3 = Character.toUpperCase(r7);
        if (r2 != r3) goto L11;
        return true;
    L11:
        if (Character.toLowerCase(r2) != Character.toLowerCase(r3)) goto L14;
        return true;
    L14:
        return false;
    }

    public static /* synthetic */ boolean equals$default(char r0, char r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return CharsKt.equals(r0, r1, r2);
    }

    public static final boolean isSurrogate(char r3) {
        return new CharRange(55296, 57343).contains(r3);
    }

    private static final String plus(char r1, String r2) {
        Intrinsics.checkNotNullParameter(r2, "other");
        return r1 + r2;
    }

    public static final String titlecase(char r1) {
        return _OneToManyTitlecaseMappingsKt.titlecaseImpl(r1);
    }
}
