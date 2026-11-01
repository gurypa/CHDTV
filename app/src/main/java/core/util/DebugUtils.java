package androidx.core.util;

/* JADX INFO: loaded from: classes.dex */
public class DebugUtils {
    private DebugUtils() {
    }

    public static void buildShortClassTag(Object r3, StringBuilder r4) {
        if (r3 != null) goto L4;
        r4.append("null");
        return;
    L4:
        String r0 = r3.getClass().getSimpleName();
        if (r0 != null) goto L7;
    L8:
        r0 = r3.getClass().getName();
        int r1 = r0.lastIndexOf(46);
        if (r1 <= 0) goto L11;
        r0 = r0.substring(r1 + 1);
    L11:
        r4.append(r0);
        r4.append('{');
        r4.append(Integer.toHexString(System.identityHashCode(r3)));
        return;
    L7:
        if (r0.length() > 0) goto L11;
        goto L8
    }
}
