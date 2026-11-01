package com.google.common.net;

import com.google.common.base.Preconditions;
import com.google.common.escape.UnicodeEscaper;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class PercentEscaper extends UnicodeEscaper {
    private static final char[] PLUS_SIGN = null;
    private static final char[] UPPER_HEX_DIGITS = null;
    private final boolean plusForSpace;
    private final boolean[] safeOctets;

    static {
        PLUS_SIGN = new char[]{'+'};
        UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    }

    public PercentEscaper(String r3, boolean r4) {
        Preconditions.checkNotNull(r3);
        if (r3.matches(".*[0-9A-Za-z].*") == true) goto L14;
        String r5 = r3 + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        if (r4 == true) goto L7;
    L11:
        this.plusForSpace = r4;
        this.safeOctets = createSafeOctets(r5);
        return;
    L7:
        if (r5.contains(" ") == false) goto L11;
        throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
    L14:
        throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
    }

    private static boolean[] createSafeOctets(String r7) {
        int r0 = -1;
        char[] r1 = r7.toCharArray();
        int r2 = r1.length;
        int r3 = 0;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L5;
        r0 = Math.max(r1[r4], r0);
        r4 = r4 + 1;
        goto L3
    L5:
        boolean[] r5 = new boolean[r0 + 1];
        int r6 = r1.length;
    L6:
        if (r3 >= r6) goto L8;
        r5[r1[r3]] = true;
        r3 = r3 + 1;
        goto L6
    L8:
        return r5;
    }

    @Override // com.google.common.escape.UnicodeEscaper, com.google.common.escape.Escaper
    public String escape(String r5) {
        Preconditions.checkNotNull(r5);
        int r0 = r5.length();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L12;
        char r2 = r5.charAt(r1);
        if (r2 >= this.safeOctets.length) goto L11;
        if (this.safeOctets[r2] == false) goto L11;
        r1 = r1 + 1;
    L11:
        return escapeSlow(r5, r1);
    L12:
        return r5;
    }

    @Override // com.google.common.escape.UnicodeEscaper
    @CheckForNull
    protected char[] escape(int r13) {
        if (r13 >= this.safeOctets.length) goto L9;
        if (this.safeOctets[r13] == false) goto L9;
        return null;
    L9:
        if (r13 != 32) goto L15;
        if (this.plusForSpace == false) goto L15;
        return PLUS_SIGN;
    L15:
        if (r13 > 127) goto L19;
        return new char[]{'%', UPPER_HEX_DIGITS[r13 >>> 4], UPPER_HEX_DIGITS[r13 & 15]};
    L19:
        if (r13 > 2047) goto L23;
        char[] r0 = {'%', UPPER_HEX_DIGITS[(r13 >>> 4) | 12], UPPER_HEX_DIGITS[r13 & 15], '%', UPPER_HEX_DIGITS[(r13 & 3) | 8], UPPER_HEX_DIGITS[r13 & 15]};
        int r14 = r13 >>> 4;
        int r15 = r14 >>> 2;
        return r0;
    L23:
        if (r13 > 65535) goto L27;
        char[] r1 = {'%', 'E', r1[r13 >>> 2], '%', UPPER_HEX_DIGITS[(r13 & 3) | 8], UPPER_HEX_DIGITS[r13 & 15], '%', UPPER_HEX_DIGITS[(r13 & 3) | 8], UPPER_HEX_DIGITS[r13 & 15]};
        int r16 = r13 >>> 4;
        int r17 = r16 >>> 2;
        int r18 = r17 >>> 4;
        char[] r2 = UPPER_HEX_DIGITS;
        return r1;
    L27:
        if (r13 > 1114111) goto L31;
        char[] r3 = {'%', 'F', UPPER_HEX_DIGITS[(r13 >>> 2) & 7], '%', UPPER_HEX_DIGITS[(r13 & 3) | 8], UPPER_HEX_DIGITS[r13 & 15], '%', UPPER_HEX_DIGITS[(r13 & 3) | 8], UPPER_HEX_DIGITS[r13 & 15], '%', UPPER_HEX_DIGITS[(r13 & 3) | 8], UPPER_HEX_DIGITS[r13 & 15]};
        int r19 = r13 >>> 4;
        int r110 = r19 >>> 2;
        int r111 = r110 >>> 4;
        int r112 = r111 >>> 2;
        int r113 = r112 >>> 4;
        return r3;
    L31:
        throw new IllegalArgumentException("Invalid unicode character value " + r13);
    }

    @Override // com.google.common.escape.UnicodeEscaper
    protected int nextEscapeIndex(CharSequence r3, int r4, int r5) {
        Preconditions.checkNotNull(r3);
    L3:
        if (r4 >= r5) goto L10;
        char r0 = r3.charAt(r4);
        if (r0 >= this.safeOctets.length) goto L10;
        if (this.safeOctets[r0] == false) goto L10;
        r4 = r4 + 1;
    L10:
        return r4;
    }
}
