package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Utf8 {
    private Utf8() {
    }

    public static int encodedLength(CharSequence r9) {
        int r0 = r9.length();
        int r1 = r0;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L7;
        if (r9.charAt(r2) >= 128) goto L7;
        r2 = r2 + 1;
    L7:
        if (r2 >= r0) goto L12;
        char r3 = r9.charAt(r2);
        if (r3 >= 2048) goto L11;
        r1 = r1 + ((127 - r3) >>> 31);
        r2 = r2 + 1;
        goto L7
    L11:
        r1 = r1 + encodedLengthGeneral(r9, r2);
    L12:
        if (r1 < r0) goto L15;
        return r1;
    L15:
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) r1) + 4294967296L));
    }

    private static int encodedLengthGeneral(CharSequence r6, int r7) {
        int r0 = r6.length();
        int r1 = 0;
        int r2 = r7;
    L3:
        if (r2 >= r0) goto L17;
        char r3 = r6.charAt(r2);
        if (r3 >= 2048) goto L7;
        r1 = r1 + ((127 - r3) >>> 31);
    L16:
        r2 = r2 + 1;
        goto L3
    L7:
        r1 = r1 + 2;
        if (55296 > r3) goto L16;
        if (r3 > 57343) goto L16;
        if (Character.codePointAt(r6, r2) == r3) goto L15;
        r2 = r2 + 1;
        goto L16
    L15:
        throw new IllegalArgumentException(unpairedSurrogateMsg(r2));
    L17:
        return r1;
    }

    public static boolean isWellFormed(byte[] r2) {
        return isWellFormed(r2, 0, r2.length);
    }

    public static boolean isWellFormed(byte[] r3, int r4, int r5) {
        int r0 = r4 + r5;
        Preconditions.checkPositionIndexes(r4, r0, r3.length);
        int r1 = r4;
    L3:
        if (r1 >= r0) goto L9;
        if (r3[r1] < 0) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return isWellFormedSlowPath(r3, r1, r0);
    L9:
        return true;
    }

    private static boolean isWellFormedSlowPath(byte[] r7, int r8, int r9) {
        int r0 = r8;
    L3:
        if (r0 >= r9) goto L4;
        int r1 = r0 + 1;
        byte r2 = r7[r0];
        if (r2 < 0) goto L9;
        r0 = r1;
        goto L3
    L9:
        if (r2 < (-32)) goto L10;
        if (r2 < (-16)) goto L21;
        if ((r1 + 2) >= r9) goto L38;
        int r3 = r1 + 1;
        byte r4 = r7[r1];
        if (r4 > (-65)) goto L50;
        if ((((r2 << Ascii.f82FS) + (r4 + 112)) >> 30) != 0) goto L50;
        int r5 = r3 + 1;
        if (r7[r3] > (-65)) goto L50;
        r0 = r5 + 1;
        if (r7[r5] <= (-65)) goto L3;
    L50:
        return false;
    L38:
        return false;
    L21:
        if ((r1 + 1) >= r9) goto L22;
        int r6 = r1 + 1;
        byte r10 = r7[r1];
        if (r10 > (-65)) goto L35;
        if (r2 != (-32)) goto L29;
        if (r10 < (-96)) goto L35;
    L29:
        if (r2 != (-19)) goto L31;
        if ((-96) <= r10) goto L35;
    L31:
        r0 = r6 + 1;
        if (r7[r6] <= (-65)) goto L3;
    L35:
        return false;
    L22:
        return false;
    L10:
        if (r1 == r9) goto L11;
        if (r2 < (-62)) goto L17;
        r0 = r1 + 1;
        if (r7[r1] <= (-65)) goto L3;
    L17:
        return false;
    L11:
        return false;
    L4:
        return true;
    }

    private static String unpairedSurrogateMsg(int r2) {
        return "Unpaired surrogate at index " + r2;
    }
}
