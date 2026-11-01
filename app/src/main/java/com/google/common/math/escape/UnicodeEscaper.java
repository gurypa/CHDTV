package com.google.common.escape;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class UnicodeEscaper extends Escaper {
    private static final int DEST_PAD = 32;

    protected UnicodeEscaper() {
    }

    protected static int codePointAt(CharSequence r9, int r10, int r11) {
        Preconditions.checkNotNull(r9);
        if (r10 >= r11) goto L24;
        int r0 = r10 + 1;
        char r12 = r9.charAt(r10);
        if (r12 >= 55296) goto L7;
    L22:
        return r12;
    L7:
        if (r12 > 57343) goto L22;
        if (r12 > 56319) goto L21;
        if (r0 == r11) goto L13;
        char r1 = r9.charAt(r0);
        if (Character.isLowSurrogate(r1) == false) goto L19;
        return Character.toCodePoint(r12, r1);
    L19:
        throw new IllegalArgumentException("Expected low surrogate but got char '" + r1 + "' with value " + r1 + " at index " + r0 + " in '" + r9 + "'");
    L13:
        return -r12;
    L21:
        throw new IllegalArgumentException("Unexpected low surrogate character '" + r12 + "' with value " + r12 + " at index " + (r0 - 1) + " in '" + r9 + "'");
    L24:
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    private static char[] growBuffer(char[] r2, int r3, int r4) {
        if (r4 < 0) goto L8;
        char[] r0 = new char[r4];
        if (r3 <= 0) goto L6;
        System.arraycopy(r2, 0, r0, 0, r3);
    L6:
        return r0;
    L8:
        throw new AssertionError("Cannot increase internal buffer any further");
    }

    @Override // com.google.common.escape.Escaper
    public String escape(String r4) {
        Preconditions.checkNotNull(r4);
        int r0 = r4.length();
        int r1 = nextEscapeIndex(r4, 0, r0);
        if (r1 != r0) goto L6;
        return r4;
    L6:
        return escapeSlow(r4, r1);
    }

    @CheckForNull
    protected abstract char[] escape(int r1);

    protected final String escapeSlow(String r12, int r13) {
        int r0 = r12.length();
        char[] r1 = Platform.charBufferFromThreadLocal();
        int r2 = 0;
        int r3 = 0;
    L4:
        if (r13 >= r0) goto L25;
        int r5 = codePointAt(r12, r13, r0);
        if (r5 < 0) goto L24;
        char[] r6 = escape(r5);
        if (Character.isSupplementaryCodePoint(r5) == false) goto L10;
        int r7 = 2;
    L11:
        int r8 = r7 + r13;
        if (r6 == null) goto L22;
        int r9 = r13 - r3;
        int r10 = (r2 + r9) + r6.length;
        if (r1.length >= r10) goto L16;
        r1 = growBuffer(r1, r2, ((r0 - r13) + r10) + 32);
    L16:
        if (r9 <= 0) goto L19;
        r12.getChars(r3, r13, r1, r2);
        r2 = r2 + r9;
    L19:
        if (r6.length <= 0) goto L21;
        System.arraycopy(r6, 0, r1, r2, r6.length);
        r2 = r2 + r6.length;
    L21:
        r3 = r8;
    L22:
        r13 = nextEscapeIndex(r12, r8, r0);
        goto L4
    L10:
        r7 = 1;
        goto L11
    L24:
        throw new IllegalArgumentException("Trailing high surrogate at end of input");
    L25:
        int r11 = r0 - r3;
        if (r11 <= 0) goto L32;
        int r14 = r2 + r11;
        if (r1.length >= r14) goto L30;
        r1 = growBuffer(r1, r2, r14);
    L30:
        r12.getChars(r3, r0, r1, r2);
        r2 = r14;
    L32:
        return new String(r1, 0, r2);
    }

    protected int nextEscapeIndex(CharSequence r4, int r5, int r6) {
        int r0 = r5;
    L3:
        if (r0 >= r6) goto L14;
        int r1 = codePointAt(r4, r0, r6);
        if (r1 < 0) goto L14;
        if (escape(r1) != null) goto L14;
        if (Character.isSupplementaryCodePoint(r1) == false) goto L12;
        int r2 = 2;
    L13:
        r0 = r0 + r2;
        goto L3
    L12:
        r2 = 1;
    L14:
        return r0;
    }
}
