package androidx.media3.datasource;

import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class HttpUtil {
    private static final Pattern CONTENT_RANGE_WITH_SIZE = null;
    private static final Pattern CONTENT_RANGE_WITH_START_AND_END = null;
    private static final String TAG = "HttpUtil";

    static {
        CONTENT_RANGE_WITH_START_AND_END = Pattern.compile("bytes (\\d+)-(\\d+)/(?:\\d+|\\*)");
        CONTENT_RANGE_WITH_SIZE = Pattern.compile("bytes (?:(?:\\d+-\\d+)|\\*)/(\\d+)");
    }

    private HttpUtil() {
    }

    public static String buildRangeRequestHeader(long r5, long r7) {
        if (r5 == 0) goto L5;
    L8:
        StringBuilder r0 = new StringBuilder();
        r0.append("bytes=");
        r0.append(r5);
        r0.append("-");
        if (r7 == (-1)) goto L12;
        r0.append((r5 + r7) - 1);
    L12:
        return r0.toString();
    L5:
        if (r7 != (-1)) goto L8;
        return null;
    }

    public static long getContentLength(String r9, String r10) {
        long r0 = -1;
        if (TextUtils.isEmpty(r9) == true) goto L9;
        r0 = Long.parseLong(r9);     // Catch: NumberFormatException -> L6
    L7:
        Log.m44e(TAG, "Unexpected Content-Length [" + r9 + "]");
    L9:
        if (TextUtils.isEmpty(r10) == true) goto L28;
        Matcher r2 = CONTENT_RANGE_WITH_START_AND_END.matcher(r10);
        if (r2.matches() == true) goto L24;
        return r0;
    L24:
        long r5 = (Long.parseLong((String) Assertions.checkNotNull(r2.group(2))) - Long.parseLong((String) Assertions.checkNotNull(r2.group(1)))) + 1;     // Catch: NumberFormatException -> L21
        if (r0 >= 0) goto L17;
        return r5;
    L17:
        if (r0 == r5) goto L32;
        Log.m48w(TAG, "Inconsistent headers [" + r9 + "] [" + r10 + "]");     // Catch: NumberFormatException -> L21
        return Math.max(r0, r5);
    L32:
        return r0;
    L22:
        Log.m44e(TAG, "Unexpected Content-Range [" + r10 + "]");
        return r0;
    L28:
        return r0;
    }

    public static long getDocumentSize(String r4) {
        if (TextUtils.isEmpty(r4) == false) goto L5;
        return -1;
    L5:
        Matcher r0 = CONTENT_RANGE_WITH_SIZE.matcher(r4);
        if (r0.matches() == true) goto L8;
        return -1;
    L8:
        return Long.parseLong((String) Assertions.checkNotNull(r0.group(1)));
    }
}
