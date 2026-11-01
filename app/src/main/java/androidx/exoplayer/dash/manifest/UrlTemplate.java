package androidx.media3.exoplayer.dash.manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class UrlTemplate {
    private static final String BANDWIDTH = "Bandwidth";
    private static final int BANDWIDTH_ID = 3;
    private static final String DEFAULT_FORMAT_TAG = "%01d";
    private static final String ESCAPED_DOLLAR = "$$";
    private static final String NUMBER = "Number";
    private static final int NUMBER_ID = 2;
    private static final String REPRESENTATION = "RepresentationID";
    private static final int REPRESENTATION_ID = 1;
    private static final String TIME = "Time";
    private static final int TIME_ID = 4;
    private final List<String> identifierFormatTags;
    private final List<Integer> identifiers;
    private final List<String> urlPieces;

    private UrlTemplate(List<String> r1, List<Integer> r2, List<String> r3) {
        this.urlPieces = r1;
        this.identifiers = r2;
        this.identifierFormatTags = r3;
    }

    public static UrlTemplate compile(String r4) {
        ArrayList r0 = new ArrayList();
        ArrayList r1 = new ArrayList();
        ArrayList r2 = new ArrayList();
        parseTemplate(r4, r0, r1, r2);
        return new UrlTemplate(r0, r1, r2);
    }

    private static void parseTemplate(String r12, List<String> r13, List<Integer> r14, List<String> r15) {
        r13.add("");
        int r1 = 0;
    L4:
        if (r1 >= r12.length()) goto L47;
        int r3 = r12.indexOf("$", r1);
        byte r4 = -1;
        if (r3 == (-1)) goto L7;
        if (r3 != r1) goto L9;
        if (r12.startsWith(ESCAPED_DOLLAR, r1) == true) goto L12;
        r15.add("");
        int r2 = r12.indexOf("$", r1 + 1);
        String r5 = r12.substring(r1 + 1, r2);
        if (r5.equals(REPRESENTATION) == false) goto L16;
        r14.add(1);
    L45:
        r13.add("");
        r1 = r2 + 1;
        goto L4
    L16:
        int r6 = r5.indexOf("%0");
        String r8 = DEFAULT_FORMAT_TAG;
        if (r6 == (-1)) goto L27;
        r8 = r5.substring(r6);
        if (r8.endsWith("d") == false) goto L21;
    L25:
        r5 = r5.substring(0, r6);
        goto L27
    L21:
        if (r8.endsWith("x") == true) goto L25;
        if (r8.endsWith("X") == true) goto L25;
        r8 = r8 + "d";
    L27:
        switch(r5.hashCode()) {
            case -1950496919: goto L36;
            case 2606829: goto L33;
            case 38199441: goto L30;
            default: goto L38;
        };
    L38:
        switch(r4) {
            case 0: goto L43;
            case 1: goto L42;
            case 2: goto L41;
            default: goto L40;
        };
    L41:
        r14.add(4);
    L44:
        r15.set(r14.size() - 1, r8);
        goto L45
    L42:
        r14.add(3);
        goto L44
    L43:
        r14.add(2);
        goto L44
    L40:
        throw new IllegalArgumentException("Invalid template: " + r12);
    L30:
        if (r5.equals(BANDWIDTH) == false) goto L38;
        r4 = 1;
        goto L38
    L33:
        if (r5.equals(TIME) == false) goto L38;
        r4 = 2;
        goto L38
    L36:
        if (r5.equals(NUMBER) == false) goto L38;
        r4 = 0;
        goto L38
    L12:
        r13.set(r14.size(), r13.get(r14.size()) + "$");
        r1 = r1 + 2;
        goto L4
    L9:
        r13.set(r14.size(), r13.get(r14.size()) + r12.substring(r1, r3));
        r1 = r3;
        goto L4
    L7:
        r13.set(r14.size(), r13.get(r14.size()) + r12.substring(r1));
        r1 = r12.length();
        goto L4
    }

    public String buildUri(String r8, long r9, int r11, long r12) {
        StringBuilder r0 = new StringBuilder();
        int r1 = 0;
    L4:
        if (r1 >= this.identifiers.size()) goto L18;
        r0.append(this.urlPieces.get(r1));
        if (this.identifiers.get(r1).intValue() != 1) goto L9;
        r0.append(r8);
    L17:
        r1 = r1 + 1;
        goto L4
    L9:
        if (this.identifiers.get(r1).intValue() != 2) goto L12;
        r0.append(String.format(Locale.US, this.identifierFormatTags.get(r1), new Object[]{Long.valueOf(r9)}));
        goto L17
    L12:
        if (this.identifiers.get(r1).intValue() != 3) goto L15;
        r0.append(String.format(Locale.US, this.identifierFormatTags.get(r1), new Object[]{Integer.valueOf(r11)}));
        goto L17
    L15:
        if (this.identifiers.get(r1).intValue() != 4) goto L17;
        r0.append(String.format(Locale.US, this.identifierFormatTags.get(r1), new Object[]{Long.valueOf(r12)}));
        goto L17
    L18:
        r0.append(this.urlPieces.get(this.identifiers.size()));
        return r0.toString();
    }
}
