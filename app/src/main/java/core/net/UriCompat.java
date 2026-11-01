package androidx.core.net;

import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public final class UriCompat {
    private UriCompat() {
    }

    public static String toSafeString(Uri r8) {
        String r0 = r8.getScheme();
        String r1 = r8.getSchemeSpecificPart();
        if (r0 != null) goto L5;
    L49:
        StringBuilder r4 = new StringBuilder(64);
        if (r0 == null) goto L52;
        r4.append(r0);
        r4.append(':');
    L52:
        if (r1 == null) goto L55;
        r4.append(r1);
    L55:
        return r4.toString();
    L5:
        if (r0.equalsIgnoreCase("tel") == false) goto L7;
    L33:
        StringBuilder r5 = new StringBuilder(64);
        r5.append(r0);
        r5.append(':');
        if (r1 == null) goto L48;
        int r2 = 0;
    L37:
        if (r2 >= r1.length()) goto L48;
        char r6 = r1.charAt(r2);
        if (r6 == '-') goto L45;
        if (r6 == '@') goto L45;
        if (r6 == '.') goto L45;
        r5.append('x');
    L46:
        r2 = r2 + 1;
    L45:
        r5.append(r6);
    L48:
        return r5.toString();
    L7:
        if (r0.equalsIgnoreCase("sip") == true) goto L33;
        if (r0.equalsIgnoreCase("sms") == true) goto L33;
        if (r0.equalsIgnoreCase("smsto") == true) goto L33;
        if (r0.equalsIgnoreCase("mailto") == true) goto L33;
        if (r0.equalsIgnoreCase("nfc") == true) goto L33;
        if (r0.equalsIgnoreCase("http") == false) goto L20;
    L25:
        StringBuilder r7 = new StringBuilder().append("//");
        String r9 = "";
        if (r8.getHost() == null) goto L28;
        String r10 = r8.getHost();
    L29:
        StringBuilder r11 = r7.append(r10);
        if (r8.getPort() == (-1)) goto L32;
        r9 = ":" + r8.getPort();
    L32:
        r1 = r11.append(r9).append("/...").toString();
        goto L49
    L28:
        r10 = "";
        goto L29
    L20:
        if (r0.equalsIgnoreCase("https") == true) goto L25;
        if (r0.equalsIgnoreCase("ftp") == true) goto L25;
        if (r0.equalsIgnoreCase("rtsp") == false) goto L49;
        goto L25
    }
}
