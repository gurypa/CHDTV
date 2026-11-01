package androidx.core.content;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class MimeTypeFilter {
    private MimeTypeFilter() {
    }

    public static String matches(String r8, String[] r9) {
        if (r8 != null) goto L5;
        return null;
    L5:
        String[] r2 = r8.split("/");
        int r3 = r9.length;
        int r4 = 0;
    L6:
        if (r4 >= r3) goto L11;
        String r5 = r9[r4];
        if (mimeTypeAgainstFilter(r2, r5.split("/")) == true) goto L9;
        r4 = r4 + 1;
        goto L6
    L9:
        return r5;
    L11:
        return null;
    }

    public static String matches(String[] r8, String r9) {
        if (r8 != null) goto L5;
        return null;
    L5:
        String[] r2 = r9.split("/");
        int r3 = r8.length;
        int r4 = 0;
    L6:
        if (r4 >= r3) goto L11;
        String r5 = r8[r4];
        if (mimeTypeAgainstFilter(r5.split("/"), r2) == true) goto L9;
        r4 = r4 + 1;
        goto L6
    L9:
        return r5;
    L11:
        return null;
    }

    public static boolean matches(String r3, String r4) {
        if (r3 != null) goto L6;
        return false;
    L6:
        return mimeTypeAgainstFilter(r3.split("/"), r4.split("/"));
    }

    public static String[] matchesMany(String[] r8, String r9) {
        int r0 = 0;
        if (r8 == null) goto L5;
        ArrayList r1 = new ArrayList();
        String[] r3 = r9.split("/");
        int r4 = r8.length;
    L7:
        if (r0 >= r4) goto L13;
        String r5 = r8[r0];
        if (mimeTypeAgainstFilter(r5.split("/"), r3) == false) goto L11;
        r1.add(r5);
    L11:
        r0 = r0 + 1;
        goto L7
    L13:
        return (String[]) r1.toArray(new String[r1.size()]);
    L5:
        return new String[0];
    }

    private static boolean mimeTypeAgainstFilter(String[] r5, String[] r6) {
        if (r6.length != 2) goto L25;
        if (r6[0].isEmpty() == true) goto L23;
        if (r6[1].isEmpty() == true) goto L23;
        if (r5.length == 2) goto L12;
        return false;
    L12:
        if ("*".equals(r6[0]) == true) goto L17;
        if (r6[0].equals(r5[0]) == true) goto L17;
        return false;
    L17:
        if ("*".equals(r6[1]) == false) goto L19;
    L21:
        return true;
    L19:
        if (r6[1].equals(r5[1]) == true) goto L21;
        return false;
    L23:
        throw new IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
    L25:
        throw new IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
    }
}
