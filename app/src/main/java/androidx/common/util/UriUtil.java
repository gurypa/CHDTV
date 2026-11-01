package androidx.media3.common.util;

import android.net.Uri;
import android.text.TextUtils;
import com.google.common.base.Ascii;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class UriUtil {
    private static final int FRAGMENT = 3;
    private static final int INDEX_COUNT = 4;
    private static final int PATH = 1;
    private static final int QUERY = 2;
    private static final int SCHEME_COLON = 0;

    private UriUtil() {
    }

    public static String getRelativePath(Uri r11, Uri r12) {
        if (r11.isOpaque() == true) goto L42;
        if (r12.isOpaque() == true) goto L42;
        String r0 = r11.getScheme();
        String r1 = r12.getScheme();
        boolean r2 = false;
        if (r0 != null) goto L11;
        if (r1 != null) goto L16;
        r2 = true;
    L16:
        if (r2 == false) goto L40;
        if (Util.areEqual(r11.getAuthority(), r12.getAuthority()) == false) goto L40;
        List<String> r4 = r11.getPathSegments();
        List<String> r5 = r12.getPathSegments();
        int r6 = 0;
        int r7 = Math.min(r4.size(), r5.size());
        int r8 = 0;
    L21:
        if (r8 >= r7) goto L26;
        if (r4.get(r8).equals(r5.get(r8)) == false) goto L26;
        r6 = r6 + 1;
        r8 = r8 + 1;
    L26:
        StringBuilder r9 = new StringBuilder();
        int r10 = r6;
    L28:
        if (r10 >= r4.size()) goto L30;
        r9.append("../");
        r10 = r10 + 1;
        goto L28
    L30:
        int r13 = r6;
    L32:
        if (r13 >= r5.size()) goto L38;
        r9.append(r5.get(r13));
        if (r13 >= (r5.size() - 1)) goto L36;
        r9.append("/");
    L36:
        r13 = r13 + 1;
        goto L32
    L38:
        return r9.toString();
    L40:
        return r12.toString();
    L11:
        if (r1 == null) goto L16;
        if (Ascii.equalsIgnoreCase(r0, r1) == false) goto L16;
        r2 = true;
    L42:
        return r12.toString();
    }

    private static int[] getUriIndices(String r12) {
        int[] r0 = new int[4];
        if (TextUtils.isEmpty(r12) == false) goto L6;
        r0[0] = -1;
        return r0;
    L6:
        int r1 = r12.length();
        int r4 = r12.indexOf(35);
        if (r4 != (-1)) goto L9;
        r4 = r1;
    L9:
        int r5 = r12.indexOf(63);
        if (r5 == (-1)) goto L12;
        if (r5 > r4) goto L12;
    L13:
        int r7 = r12.indexOf(47);
        if (r7 == (-1)) goto L16;
        if (r7 > r5) goto L16;
    L17:
        int r8 = r12.indexOf(58);
        if (r8 <= r7) goto L21;
        r8 = -1;
    L21:
        if ((r8 + 2) < r5) goto L23;
    L27:
        boolean r9 = false;
    L28:
        if (r9 == false) goto L33;
        int r6 = r12.indexOf(47, r8 + 3);
        if (r6 == (-1)) goto L32;
        if (r6 > r5) goto L32;
    L34:
        r0[0] = r8;
        r0[1] = r6;
        r0[2] = r5;
        r0[3] = r4;
        return r0;
    L32:
        r6 = r5;
        goto L34
    L33:
        r6 = r8 + 1;
        goto L34
    L23:
        if (r12.charAt(r8 + 1) != '/') goto L27;
        if (r12.charAt(r8 + 2) != '/') goto L27;
        r9 = true;
    L16:
        r7 = r5;
    L12:
        r5 = r4;
        goto L13
    }

    public static boolean isAbsolute(String r3) {
        if (r3 != null) goto L5;
        return false;
    L5:
        if (getUriIndices(r3)[0] == (-1)) goto L9;
        return true;
    L9:
        return false;
    }

    private static String removeDotSegments(StringBuilder r7, int r8, int r9) {
        if (r8 < r9) goto L6;
        return r7.toString();
    L6:
        if (r7.charAt(r8) != '/') goto L8;
        r8 = r8 + 1;
    L8:
        int r0 = r8;
        int r2 = r8;
    L9:
        if (r2 > r9) goto L35;
        if (r2 == r9) goto L11;
        if (r7.charAt(r2) == '/') goto L14;
        r2 = r2 + 1;
        goto L9
    L14:
        int r3 = r2 + 1;
    L16:
        if (r2 != (r0 + 1)) goto L21;
        if (r7.charAt(r0) != '.') goto L21;
        r7.delete(r0, r3);
        r9 = r9 - (r3 - r0);
        r2 = r0;
    L21:
        if (r2 != (r0 + 2)) goto L31;
        if (r7.charAt(r0) != '.') goto L31;
        if (r7.charAt(r0 + 1) != '.') goto L31;
        int r4 = r7.lastIndexOf("/", r0 - 2) + 1;
        if (r4 <= r8) goto L29;
        int r5 = r4;
    L30:
        r7.delete(r5, r3);
        r9 = r9 - (r3 - r5);
        r0 = r4;
        r2 = r4;
        goto L9
    L29:
        r5 = r8;
    L31:
        r2 = r2 + 1;
        r0 = r2;
        goto L9
    L11:
        r3 = r2;
        goto L16
    L35:
        return r7.toString();
    }

    public static Uri removeQueryParameter(Uri r5, String r6) {
        Uri.Builder r0 = r5.buildUpon();
        r0.clearQuery();
        Iterator<String> r1 = r5.getQueryParameterNames().iterator();
    L4:
        if (r1.hasNext() == false) goto L13;
        String r2 = r1.next();
        if (r2.equals(r6) == true) goto L4;
        Iterator<String> r3 = r5.getQueryParameters(r2).iterator();
    L9:
        if (r3.hasNext() == false) goto L4;
        r0.appendQueryParameter(r2, r3.next());
        goto L9
    L13:
        return r0.build();
    }

    public static String resolve(String r10, String r11) {
        StringBuilder r0 = new StringBuilder();
        String r1 = "";
        if (r10 != null) goto L5;
        String r2 = "";
    L6:
        if (r11 == null) goto L9;
        r1 = r11;
    L9:
        int[] r12 = getUriIndices(r1);
        if (r12[0] == (-1)) goto L13;
        r0.append(r1);
        removeDotSegments(r0, r12[1], r12[2]);
        return r0.toString();
    L13:
        int[] r3 = getUriIndices(r2);
        if (r12[3] != 0) goto L18;
        return r0.append(r2, 0, r3[3]).append(r1).toString();
    L18:
        if (r12[2] != 0) goto L22;
        return r0.append(r2, 0, r3[2]).append(r1).toString();
    L22:
        if (r12[1] == 0) goto L26;
        int r4 = r3[0] + 1;
        r0.append(r2, 0, r4).append(r1);
        return removeDotSegments(r0, r12[1] + r4, r12[2] + r4);
    L26:
        if (r1.charAt(r12[1]) != '/') goto L30;
        r0.append(r2, 0, r3[1]).append(r1);
        return removeDotSegments(r0, r3[1], r3[1] + r12[2]);
    L30:
        if ((r3[0] + 2) < r3[1]) goto L32;
    L35:
        int r7 = r2.lastIndexOf(47, r3[2] - 1);
        if (r7 != (-1)) goto L38;
        int r5 = r3[1];
    L39:
        r0.append(r2, 0, r5).append(r1);
        return removeDotSegments(r0, r3[1], r12[2] + r5);
    L38:
        r5 = r7 + 1;
        goto L39
    L32:
        if (r3[1] != r3[2]) goto L35;
        r0.append(r2, 0, r3[1]).append('/').append(r1);
        return removeDotSegments(r0, r3[1], (r3[1] + r12[2]) + 1);
    L5:
        r2 = r10;
        goto L6
    }

    public static Uri resolveToUri(String r1, String r2) {
        return Uri.parse(resolve(r1, r2));
    }
}
