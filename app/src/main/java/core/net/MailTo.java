package androidx.core.net;

import android.net.Uri;
import androidx.core.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
public final class MailTo {
    private static final String BCC = "bcc";
    private static final String BODY = "body";

    /* JADX INFO: renamed from: CC */
    private static final String f15CC = "cc";
    private static final String MAILTO = "mailto";
    public static final String MAILTO_SCHEME = "mailto:";
    private static final String SUBJECT = "subject";

    /* JADX INFO: renamed from: TO */
    private static final String f16TO = "to";
    private HashMap<String, String> mHeaders;

    private MailTo() {
        this.mHeaders = new HashMap();
    }

    public static boolean isMailTo(Uri r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (MAILTO.equals(r2.getScheme()) == false) goto L6;
        return true;
    }

    public static boolean isMailTo(String r1) {
        if (r1 != null) goto L4;
    L6:
        return false;
    L4:
        if (r1.startsWith(MAILTO_SCHEME) == false) goto L6;
        return true;
    }

    public static MailTo parse(Uri r1) throws ParseException {
        return parse(r1.toString());
    }

    public static MailTo parse(String r14) throws ParseException {
        Preconditions.checkNotNull(r14);
        if (isMailTo(r14) == false) goto L30;
        int r0 = r14.indexOf(35);
        if (r0 == (-1)) goto L7;
        r14 = r14.substring(0, r0);
    L7:
        int r3 = r14.indexOf(63);
        if (r3 != (-1)) goto L10;
        String r1 = Uri.decode(r14.substring(MAILTO_SCHEME.length()));
        String r4 = null;
    L11:
        MailTo r5 = new MailTo();
        if (r4 == null) goto L24;
        String[] r6 = r4.split("&");
        int r7 = r6.length;
        int r8 = 0;
    L14:
        if (r8 >= r7) goto L24;
        String[] r10 = r6[r8].split("=", 2);
        if (r10.length == 0) goto L23;
        String r11 = Uri.decode(r10[0]).toLowerCase(Locale.ROOT);
        if (r10.length <= 1) goto L21;
        String r12 = Uri.decode(r10[1]);
    L22:
        r5.mHeaders.put(r11, r12);
        goto L23
    L21:
        r12 = null;
    L23:
        r8 = r8 + 1;
    L24:
        String r2 = r5.getTo();
        if (r2 == null) goto L27;
        r1 = r1 + ", " + r2;
    L27:
        r5.mHeaders.put(f16TO, r1);
        return r5;
    L10:
        r1 = Uri.decode(r14.substring(MAILTO_SCHEME.length(), r3));
        r4 = r14.substring(r3 + 1);
        goto L11
    L30:
        throw new ParseException("Not a mailto scheme");
    }

    public String getBcc() {
        return this.mHeaders.get(BCC);
    }

    public String getBody() {
        return this.mHeaders.get("body");
    }

    public String getCc() {
        return this.mHeaders.get(f15CC);
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public String getSubject() {
        return this.mHeaders.get(SUBJECT);
    }

    public String getTo() {
        return this.mHeaders.get(f16TO);
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder(MAILTO_SCHEME);
        r0.append('?');
        Iterator<Map.Entry<String, String>> r1 = this.mHeaders.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L7;
        Map.Entry<String, String> r2 = r1.next();
        r0.append(Uri.encode(r2.getKey()));
        r0.append('=');
        r0.append(Uri.encode(r2.getValue()));
        r0.append(Typography.amp);
        goto L4
    L7:
        return r0.toString();
    }
}
