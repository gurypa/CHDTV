package com.google.common.escape;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class CharEscaper extends Escaper {
    private static final int DEST_PAD_MULTIPLIER = 2;

    protected CharEscaper() {
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
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        if (escape(r4.charAt(r1)) != null) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return escapeSlow(r4, r1);
    L9:
        return r4;
    }

    @CheckForNull
    protected abstract char[] escape(char r1);

    protected final String escapeSlow(String r12, int r13) {
        int r0 = r12.length();
        char[] r1 = Platform.charBufferFromThreadLocal();
        int r2 = r1.length;
        int r3 = 0;
        int r4 = 0;
    L4:
        if (r13 >= r0) goto L17;
        char[] r6 = escape(r12.charAt(r13));
        if (r6 == null) goto L16;
        int r7 = r6.length;
        int r8 = r13 - r4;
        int r9 = (r3 + r8) + r7;
        if (r2 >= r9) goto L11;
        r2 = r9 + ((r0 - r13) * 2);
        r1 = growBuffer(r1, r3, r2);
    L11:
        if (r8 <= 0) goto L13;
        r12.getChars(r4, r13, r1, r3);
        r3 = r3 + r8;
    L13:
        if (r7 <= 0) goto L15;
        System.arraycopy(r6, 0, r1, r3, r7);
        r3 = r3 + r7;
    L15:
        r4 = r13 + 1;
    L16:
        r13 = r13 + 1;
        goto L4
    L17:
        int r10 = r0 - r4;
        if (r10 <= 0) goto L24;
        int r11 = r3 + r10;
        if (r2 >= r11) goto L22;
        r1 = growBuffer(r1, r3, r11);
    L22:
        r12.getChars(r4, r0, r1, r3);
        r3 = r11;
    L24:
        return new String(r1, 0, r3);
    }
}
