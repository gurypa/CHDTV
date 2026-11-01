package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Ascii {
    public static final byte ACK = 6;
    public static final byte BEL = 7;

    /* JADX INFO: renamed from: BS */
    public static final byte f78BS = 8;
    public static final byte CAN = 24;
    private static final char CASE_MASK = ' ';

    /* JADX INFO: renamed from: CR */
    public static final byte f79CR = 13;
    public static final byte DC1 = 17;
    public static final byte DC2 = 18;
    public static final byte DC3 = 19;
    public static final byte DC4 = 20;
    public static final byte DEL = 127;
    public static final byte DLE = 16;

    /* JADX INFO: renamed from: EM */
    public static final byte f80EM = 25;
    public static final byte ENQ = 5;
    public static final byte EOT = 4;
    public static final byte ESC = 27;
    public static final byte ETB = 23;
    public static final byte ETX = 3;

    /* JADX INFO: renamed from: FF */
    public static final byte f81FF = 12;

    /* JADX INFO: renamed from: FS */
    public static final byte f82FS = 28;

    /* JADX INFO: renamed from: GS */
    public static final byte f83GS = 29;

    /* JADX INFO: renamed from: HT */
    public static final byte f84HT = 9;

    /* JADX INFO: renamed from: LF */
    public static final byte f85LF = 10;
    public static final char MAX = 127;
    public static final char MIN = 0;
    public static final byte NAK = 21;

    /* JADX INFO: renamed from: NL */
    public static final byte f86NL = 10;
    public static final byte NUL = 0;

    /* JADX INFO: renamed from: RS */
    public static final byte f87RS = 30;

    /* JADX INFO: renamed from: SI */
    public static final byte f88SI = 15;

    /* JADX INFO: renamed from: SO */
    public static final byte f89SO = 14;
    public static final byte SOH = 1;

    /* JADX INFO: renamed from: SP */
    public static final byte f90SP = 32;
    public static final byte SPACE = 32;
    public static final byte STX = 2;
    public static final byte SUB = 26;
    public static final byte SYN = 22;

    /* JADX INFO: renamed from: US */
    public static final byte f91US = 31;

    /* JADX INFO: renamed from: VT */
    public static final byte f92VT = 11;
    public static final byte XOFF = 19;
    public static final byte XON = 17;

    private Ascii() {
    }

    public static boolean equalsIgnoreCase(CharSequence r8, CharSequence r9) {
        int r0 = r8.length();
        if (r8 != r9) goto L6;
        return true;
    L6:
        if (r0 == r9.length()) goto L8;
        return false;
    L8:
        int r2 = 0;
    L9:
        if (r2 >= r0) goto L19;
        char r4 = r8.charAt(r2);
        char r5 = r9.charAt(r2);
        if (r4 == r5) goto L17;
        int r6 = getAlphaIndex(r4);
        if (r6 >= 26) goto L18;
        if (r6 == getAlphaIndex(r5)) goto L17;
    L18:
        return false;
    L17:
        r2 = r2 + 1;
        goto L9
    L19:
        return true;
    }

    private static int getAlphaIndex(char r1) {
        return (char) ((r1 | CASE_MASK) - 97);
    }

    public static boolean isLowerCase(char r1) {
        if (r1 >= 'a') goto L5;
    L7:
        return false;
    L5:
        if (r1 > 'z') goto L7;
        return true;
    }

    public static boolean isUpperCase(char r1) {
        if (r1 >= 'A') goto L5;
    L7:
        return false;
    L5:
        if (r1 > 'Z') goto L7;
        return true;
    }

    public static char toLowerCase(char r1) {
        if (isUpperCase(r1) == false) goto L6;
        return (char) (r1 ^ CASE_MASK);
    L6:
        return r1;
    }

    public static String toLowerCase(CharSequence r3) {
        if ((r3 instanceof String) == true) goto L5;
        char[] r0 = new char[r3.length()];
        int r1 = 0;
    L8:
        if (r1 >= r0.length) goto L11;
        r0[r1] = toLowerCase(r3.charAt(r1));
        r1 = r1 + 1;
        goto L8
    L11:
        return String.valueOf(r0);
    L5:
        return toLowerCase((String) r3);
    }

    public static String toLowerCase(String r5) {
        int r0 = r5.length();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        if (isUpperCase(r5.charAt(r1)) == true) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        char[] r2 = r5.toCharArray();
    L7:
        if (r1 >= r0) goto L13;
        char r3 = r2[r1];
        if (isUpperCase(r3) == false) goto L11;
        r2[r1] = (char) (r3 ^ CASE_MASK);
    L11:
        r1 = r1 + 1;
        goto L7
    L13:
        return String.valueOf(r2);
    L15:
        return r5;
    }

    public static char toUpperCase(char r1) {
        if (isLowerCase(r1) == false) goto L6;
        return (char) (r1 ^ CASE_MASK);
    L6:
        return r1;
    }

    public static String toUpperCase(CharSequence r3) {
        if ((r3 instanceof String) == true) goto L5;
        char[] r0 = new char[r3.length()];
        int r1 = 0;
    L8:
        if (r1 >= r0.length) goto L11;
        r0[r1] = toUpperCase(r3.charAt(r1));
        r1 = r1 + 1;
        goto L8
    L11:
        return String.valueOf(r0);
    L5:
        return toUpperCase((String) r3);
    }

    public static String toUpperCase(String r5) {
        int r0 = r5.length();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        if (isLowerCase(r5.charAt(r1)) == true) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        char[] r2 = r5.toCharArray();
    L7:
        if (r1 >= r0) goto L13;
        char r3 = r2[r1];
        if (isLowerCase(r3) == false) goto L11;
        r2[r1] = (char) (r3 ^ CASE_MASK);
    L11:
        r1 = r1 + 1;
        goto L7
    L13:
        return String.valueOf(r2);
    L15:
        return r5;
    }

    public static String truncate(CharSequence r5, int r6, String r7) {
        Preconditions.checkNotNull(r5);
        int r0 = r6 - r7.length();
        if (r0 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "maxLength (%s) must be >= length of the truncation indicator (%s)", r6, r7.length());
        if (r5.length() > r6) goto L13;
        String r3 = r5.toString();
        if (r3.length() > r6) goto L11;
        return r3;
    L11:
        r5 = r3;
    L13:
        return new StringBuilder(r6).append(r5, 0, r0).append(r7).toString();
    L5:
        r2 = false;
        goto L6
    }
}
