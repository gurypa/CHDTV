package es.antonio.m3u;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
final class EpgTime {
    private EpgTime() {
    }

    static ParseException bad() {
        return new ParseException("Fecha XMLTV inválida", 0);
    }

    private static long legacy(String r5) throws ParseException {
        String[] r6 = r5.trim().split("\\s+");
        String r1 = r6[0];
    L4:
        if (r1.length() >= 14) goto L7;
        r1 = r1 + "00";
        goto L4
    L7:
        if (r6.length <= 1) goto L9;
        String r7 = r6[1];
    L10:
        SimpleDateFormat r2 = new SimpleDateFormat("yyyyMMddHHmmss Z", Locale.ROOT);
        r2.setLenient(false);
        return r2.parse(r1 + " " + r7).getTime();
    L9:
        r7 = "+0000";
        goto L10
    }

    static int number(String r5, int r6, int r7) throws ParseException {
        int r0 = 0;
        int r1 = r6;
    L4:
        if (r1 >= (r6 + r7)) goto L12;
        char r2 = r5.charAt(r1);
        if (r2 < '0') goto L11;
        if (r2 > '9') goto L11;
        r0 = ((r0 * 10) + r2) - 48;
        r1 = r1 + 1;
    L11:
        throw bad();
    L12:
        return r0;
    }

    static long parse(String r17) throws ParseException {
        if (r17 == null) goto L111;
        String r1 = r17.trim();
        int r2 = 0;
    L6:
        if (r2 >= r1.length()) goto L11;
        if (space(r1.charAt(r2)) == true) goto L11;
        r2 = r2 + 1;
    L11:
        if (r2 == 8) goto L18;
        if (r2 == 10) goto L18;
        if (r2 == 12) goto L18;
        if (r2 == 14) goto L18;
        throw bad();
    L18:
        int r8 = number(r1, 0, 4);
        int r10 = number(r1, 4, 2);
        int r12 = number(r1, 6, 2);
        if (r2 < 10) goto L21;
        int r4 = number(r1, 8, 2);
    L22:
        if (r2 < 12) goto L24;
        int r5 = number(r1, 10, 2);
    L25:
        if (r2 != 14) goto L27;
        int r3 = number(r1, 12, 2);
    L30:
        if (r2 >= r1.length()) goto L35;
        if (space(r1.charAt(r2)) == false) goto L35;
        r2 = r2 + 1;
    L35:
        if (r2 >= r1.length()) goto L60;
        int r13 = r2;
    L38:
        if (r13 >= r1.length()) goto L43;
        if (space(r1.charAt(r13)) == true) goto L43;
        r13 = r13 + 1;
    L43:
        if ((r13 - r2) != 5) goto L59;
        if (r1.charAt(r2) != '+') goto L47;
    L48:
        int r11 = number(r1, r2 + 1, 2);
        int r14 = number(r1, r2 + 3, 2);
        if (r11 > 23) goto L57;
        if (r14 > 59) goto L57;
        int r15 = (r11 * 60) + r14;
        if (r1.charAt(r2) != '-') goto L54;
        int r6 = -1;
    L55:
        int r16 = r15 * r6;
    L62:
        if (r8 < 1600) goto L109;
        if (r16 < (-780)) goto L109;
        if (r16 > 840) goto L109;
        if ((r8 % 4) == 0) goto L71;
    L75:
        boolean r18 = false;
    L76:
        if (r10 < 1) goto L107;
        if (r10 > 12) goto L107;
        if (r12 < 1) goto L107;
        if (r10 != 2) goto L84;
        if (r18 == false) goto L82;
        int r0 = 29;
    L94:
        if (r12 > r0) goto L107;
        if (r4 > 23) goto L107;
        if (r5 > 59) goto L107;
        if (r3 > 59) goto L107;
        long r7 = 1;
        long r19 = ((((((((long) r8) * 365) + ((long) ((r8 + 3) / 4))) - ((long) ((r8 + 99) / 100))) + ((long) ((r8 + 399) / 400))) + ((long) (((r10 * 367) - 362) / 12))) + ((long) r12)) - 1;
        if (r10 <= 2) goto L105;
        if (r18 == true) goto L103;
        r7 = 2;
    L103:
        r19 = r19 - r7;
    L105:
        return (((r19 - 719528) * 86400000) + ((((((long) r4) * 3600) + (((long) r5) * 60)) + ((long) r3)) * 1000)) - (((long) r16) * 60000);
    L82:
        r0 = 28;
        goto L94
    L84:
        if (r10 != 4) goto L86;
    L93:
        r0 = 30;
        goto L94
    L86:
        if (r10 == 6) goto L93;
        if (r10 == 9) goto L93;
        if (r10 == 11) goto L93;
        r0 = 31;
    L107:
        throw bad();
    L71:
        if ((r8 % 100) == 0) goto L73;
    L74:
        r18 = true;
        goto L76
    L73:
        if ((r8 % 400) != 0) goto L75;
    L109:
        return legacy(r17);
    L54:
        r6 = 1;
    L57:
        throw bad();
    L47:
        if (r1.charAt(r2) == '-') goto L48;
    L59:
        throw bad();
    L60:
        r16 = 0;
        goto L62
    L27:
        r3 = 0;
        goto L30
    L24:
        r5 = 0;
        goto L25
    L21:
        r4 = 0;
        goto L22
    L111:
        throw new ParseException("Fecha ausente", 0);
    }

    static boolean space(char r1) {
        if (r1 != ' ') goto L5;
    L16:
        return true;
    L5:
        if (r1 == '\t') goto L16;
        if (r1 == '\n') goto L16;
        if (r1 == '\r') goto L16;
        if (r1 == '\f') goto L16;
        if (r1 == 11) goto L16;
        return false;
    }
}
