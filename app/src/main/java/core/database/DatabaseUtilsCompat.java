package androidx.core.database;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class DatabaseUtilsCompat {
    private DatabaseUtilsCompat() {
    }

    @Deprecated
    public static String[] appendSelectionArgs(String[] r4, String[] r5) {
        if (r4 != null) goto L4;
    L8:
        return r5;
    L4:
        if (r4.length == 0) goto L8;
        String[] r0 = new String[r4.length + r5.length];
        System.arraycopy(r4, 0, r0, 0, r4.length);
        System.arraycopy(r5, 0, r0, r4.length, r5.length);
        return r0;
    }

    @Deprecated
    public static String concatenateWhere(String r2, String r3) {
        if (TextUtils.isEmpty(r2) == false) goto L6;
        return r3;
    L6:
        if (TextUtils.isEmpty(r3) == false) goto L9;
        return r2;
    L9:
        return "(" + r2 + ") AND (" + r3 + ")";
    }
}
