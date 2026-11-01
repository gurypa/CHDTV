package com.google.common.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Strings {
    private Strings() {
    }

    public static String commonPrefix(CharSequence r4, CharSequence r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        int r0 = Math.min(r4.length(), r5.length());
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        if (r4.charAt(r1) != r5.charAt(r1)) goto L8;
        r1 = r1 + 1;
    L8:
        if (validSurrogatePairAt(r4, r1 - 1) == false) goto L10;
    L11:
        r1 = r1 - 1;
    L13:
        return r4.subSequence(0, r1).toString();
    L10:
        if (validSurrogatePairAt(r5, r1 - 1) == false) goto L13;
        goto L11
    }

    public static String commonSuffix(CharSequence r4, CharSequence r5) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r5);
        int r0 = Math.min(r4.length(), r5.length());
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        if (r4.charAt((r4.length() - r1) - 1) != r5.charAt((r5.length() - r1) - 1)) goto L8;
        r1 = r1 + 1;
    L8:
        if (validSurrogatePairAt(r4, (r4.length() - r1) - 1) == false) goto L10;
    L11:
        r1 = r1 - 1;
    L13:
        return r4.subSequence(r4.length() - r1, r4.length()).toString();
    L10:
        if (validSurrogatePairAt(r5, (r5.length() - r1) - 1) == false) goto L13;
        goto L11
    }

    @CheckForNull
    public static String emptyToNull(@CheckForNull String r1) {
        return Platform.emptyToNull(r1);
    }

    public static boolean isNullOrEmpty(@CheckForNull String r1) {
        return Platform.stringIsNullOrEmpty(r1);
    }

    public static String lenientFormat(@CheckForNull String r5, @CheckForNull Object... r6) {
        String r7 = String.valueOf(r5);
        if (r6 != null) goto L5;
        r6 = new Object[]{"(Object[])null"};
    L9:
        StringBuilder r0 = new StringBuilder(r7.length() + (r6.length * 16));
        int r1 = 0;
        int r2 = 0;
    L11:
        if (r2 >= r6.length) goto L16;
        int r3 = r7.indexOf("%s", r1);
        if (r3 == (-1)) goto L16;
        r0.append(r7, r1, r3);
        r0.append(r6[r2]);
        r1 = r3 + 2;
        r2 = r2 + 1;
    L16:
        r0.append(r7, r1, r7.length());
        if (r2 >= r6.length) goto L24;
        r0.append(" [");
        int r4 = r2 + 1;
        r0.append(r6[r2]);
    L19:
        int r8 = r4;
        if (r8 >= r6.length) goto L22;
        r0.append(", ");
        r4 = r8 + 1;
        r0.append(r6[r8]);
        goto L19
    L22:
        r0.append(']');
    L24:
        return r0.toString();
    L5:
        int r9 = 0;
    L7:
        if (r9 >= r6.length) goto L9;
        r6[r9] = lenientToString(r6[r9]);
        r9 = r9 + 1;
        goto L7
    }

    private static String lenientToString(@CheckForNull Object r6) {
        if (r6 != null) goto L10;
        return "null";
    L10:
        return r6.toString();
    L7:
        e = move-exception;
        String r1 = r6.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(r6));
        Logger.getLogger("com.google.common.base.Strings").log(Level.WARNING, "Exception during lenientFormat for " + r1, e);
        return "<" + r1 + " threw " + e.getClass().getName() + ">";
    }

    public static String nullToEmpty(@CheckForNull String r1) {
        return Platform.nullToEmpty(r1);
    }

    public static String padEnd(String r2, int r3, char r4) {
        Preconditions.checkNotNull(r2);
        if (r2.length() < r3) goto L5;
        return r2;
    L5:
        StringBuilder r0 = new StringBuilder(r3);
        r0.append(r2);
        int r1 = r2.length();
    L6:
        if (r1 >= r3) goto L9;
        r0.append(r4);
        r1 = r1 + 1;
        goto L6
    L9:
        return r0.toString();
    }

    public static String padStart(String r2, int r3, char r4) {
        Preconditions.checkNotNull(r2);
        if (r2.length() < r3) goto L5;
        return r2;
    L5:
        StringBuilder r0 = new StringBuilder(r3);
        int r1 = r2.length();
    L6:
        if (r1 >= r3) goto L8;
        r0.append(r4);
        r1 = r1 + 1;
        goto L6
    L8:
        r0.append(r2);
        return r0.toString();
    }

    public static String repeat(String r8, int r9) {
        Preconditions.checkNotNull(r8);
        boolean r0 = false;
        if (r9 > 1) goto L11;
        if (r9 < 0) goto L6;
        r0 = true;
    L6:
        Preconditions.checkArgument(r0, "invalid count: %s", r9);
        if (r9 != 0) goto L10;
        return "";
    L10:
        return r8;
    L11:
        int r1 = r8.length();
        long r2 = ((long) r1) * ((long) r9);
        int r4 = (int) r2;
        if (r4 != r2) goto L20;
        char[] r5 = new char[r4];
        r8.getChars(0, r1, r5, 0);
        int r6 = r1;
    L15:
        if (r6 >= (r4 - r6)) goto L17;
        System.arraycopy(r5, 0, r5, r6, r6);
        r6 = r6 << 1;
        goto L15
    L17:
        System.arraycopy(r5, 0, r5, r6, r4 - r6);
        return new String(r5);
    L20:
        throw new ArrayIndexOutOfBoundsException("Required array size too large: " + r2);
    }

    static boolean validSurrogatePairAt(CharSequence r1, int r2) {
        if (r2 >= 0) goto L4;
    L10:
        return false;
    L4:
        if (r2 > (r1.length() - 2)) goto L10;
        if (Character.isHighSurrogate(r1.charAt(r2)) == false) goto L10;
        if (Character.isLowSurrogate(r1.charAt(r2 + 1)) == false) goto L10;
        return true;
    }
}
