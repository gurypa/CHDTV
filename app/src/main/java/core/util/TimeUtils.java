package androidx.core.util;

import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public final class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync = null;

    static {
        sFormatSync = new Object();
        sFormatStr = new char[24];
    }

    private TimeUtils() {
    }

    private static int accumField(int r1, int r2, boolean r3, int r4) {
        if (r1 > 99) goto L24;
        if (r3 == false) goto L9;
        if (r4 >= 3) goto L24;
    L9:
        if (r1 > 9) goto L22;
        if (r3 == true) goto L12;
    L14:
        if (r3 == true) goto L20;
        if (r1 > 0) goto L20;
        return 0;
    L20:
        return r2 + 1;
    L12:
        if (r4 < 2) goto L14;
    L22:
        return r2 + 2;
    L24:
        return r2 + 3;
    }

    public static void formatDuration(long r3, long r5, PrintWriter r7) {
        if (r3 != 0) goto L6;
        r7.print("--");
        return;
    L6:
        formatDuration(r3 - r5, r7, 0);
    }

    public static void formatDuration(long r1, PrintWriter r3) {
        formatDuration(r1, r3, 0);
    }

    public static void formatDuration(long r5, PrintWriter r7, int r8) {
        Object r0 = sFormatSync;
        monitor-enter(r0);
        int r1 = formatDurationLocked(r5, r8);     // Catch: Throwable -> L7
        r7.print(new String(sFormatStr, 0, r1));     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public static void formatDuration(long r4, StringBuilder r6) {
        Object r0 = sFormatSync;
        monitor-enter(r0);
        int r2 = formatDurationLocked(r4, 0);     // Catch: Throwable -> L8
        r6.append(sFormatStr, 0, r2);     // Catch: Throwable -> L8
        monitor-exit(r0);     // Catch: Throwable -> L8
        return;
    L8:
        th = move-exception;
        throw th;
    }

    private static int formatDurationLocked(long r20, int r22) {
        long r0 = r20;
        if (sFormatStr.length >= r22) goto L5;
        sFormatStr = new char[r22];
    L5:
        char[] r4 = sFormatStr;
        if (r0 != 0) goto L13;
        int r2 = r22 - 1;
    L8:
        if (0 >= r2) goto L10;
        r4[0] = ' ';
        goto L8
    L10:
        r4[0] = '0';
        return 0 + 1;
    L13:
        if (r0 <= 0) goto L15;
        char r3 = '+';
    L16:
        int r10 = (int) (r0 % 1000);
        int r5 = (int) Math.floor(r0 / 1000);
        int r6 = 0;
        if (r5 <= SECONDS_PER_DAY) goto L20;
        r6 = r5 / SECONDS_PER_DAY;
        r5 = r5 - (SECONDS_PER_DAY * r6);
    L20:
        if (r5 <= SECONDS_PER_HOUR) goto L22;
        int r8 = r5 / SECONDS_PER_HOUR;
        r5 = r5 - (r8 * SECONDS_PER_HOUR);
        int r11 = r8;
    L24:
        if (r5 <= SECONDS_PER_MINUTE) goto L26;
        int r9 = r5 / SECONDS_PER_MINUTE;
        int r12 = r5 - (r9 * SECONDS_PER_MINUTE);
        int r13 = r9;
    L27:
        int r7 = 0;
        if (r22 == 0) goto L48;
        int r16 = accumField(r6, 1, false, 0);
        if (r16 <= 0) goto L32;
        boolean r14 = true;
    L33:
        int r17 = r16 + accumField(r11, 1, r14, 2);
        if (r17 <= 0) goto L36;
        boolean r15 = true;
    L37:
        int r18 = r17 + accumField(r13, 1, r15, 2);
        if (r18 <= 0) goto L40;
        boolean r19 = true;
    L41:
        int r110 = r18 + accumField(r12, 1, r19, 2);
        if (r110 <= 0) goto L44;
        int r21 = 3;
    L45:
        int r23 = r110 + (accumField(r10, 2, true, r21) + 1);
    L46:
        if (r23 >= r22) goto L48;
        r4[r7] = ' ';
        r7 = r7 + 1;
        r23 = r23 + 1;
        goto L46
    L44:
        r21 = 0;
        goto L45
    L40:
        r19 = false;
        goto L41
    L36:
        r15 = false;
        goto L37
    L32:
        r14 = false;
    L48:
        r4[r7] = r3;
        int r24 = r7 + 1;
        if (r22 == 0) goto L51;
        boolean r111 = true;
    L52:
        int r25 = printField(r4, r6, 'd', r24, false, 0);
        if (r25 == r24) goto L55;
        boolean r26 = true;
    L56:
        if (r111 == false) goto L58;
        int r27 = 2;
    L59:
        int r28 = printField(r4, r11, 'h', r25, r26, r27);
        if (r28 == r24) goto L62;
        boolean r29 = true;
    L63:
        if (r111 == false) goto L65;
        int r30 = 2;
    L66:
        int r31 = printField(r4, r13, 'm', r28, r29, r30);
        if (r31 == r24) goto L69;
        boolean r32 = true;
    L70:
        if (r111 == false) goto L72;
        int r33 = 2;
    L73:
        int r34 = printField(r4, r12, 's', r31, r32, r33);
        if (r111 == false) goto L77;
        if (r34 == r24) goto L77;
        int r35 = 3;
    L78:
        int r36 = printField(r4, r10, 'm', r34, true, r35);
        r4[r36] = 's';
        return r36 + 1;
    L77:
        r35 = 0;
        goto L78
    L72:
        r33 = 0;
        goto L73
    L69:
        r32 = false;
        goto L70
    L65:
        r30 = 0;
        goto L66
    L62:
        r29 = false;
        goto L63
    L58:
        r27 = 0;
        goto L59
    L55:
        r26 = false;
        goto L56
    L51:
        r111 = false;
        goto L52
    L26:
        r12 = r5;
        r13 = 0;
        goto L27
    L22:
        r11 = 0;
        goto L24
    L15:
        r3 = '-';
        r0 = -r0;
        goto L16
    }

    private static int printField(char[] r3, int r4, char r5, int r6, boolean r7, int r8) {
        if (r7 == true) goto L5;
        if (r4 > 0) goto L5;
        return r6;
    L5:
        if (r7 == false) goto L9;
        if (r8 < 3) goto L9;
    L10:
        int r1 = r4 / 100;
        r3[r6] = (char) (r1 + 48);
        r6 = r6 + 1;
        r4 = r4 - (r1 * 100);
    L11:
        if (r7 == false) goto L15;
        if (r8 < 2) goto L15;
    L17:
        int r2 = r4 / 10;
        r3[r6] = (char) (r2 + 48);
        r6 = r6 + 1;
        r4 = r4 - (r2 * 10);
    L18:
        r3[r6] = (char) (r4 + 48);
        int r9 = r6 + 1;
        r3[r9] = r5;
        return r9 + 1;
    L15:
        if (r4 > 9) goto L17;
        if (r6 == r6) goto L18;
    L9:
        if (r4 <= 99) goto L11;
        goto L10
    }
}
