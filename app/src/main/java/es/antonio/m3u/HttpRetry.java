package es.antonio.m3u;

import androidx.media3.datasource.HttpDataSource;
import com.google.common.net.HttpHeaders;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes3.dex */
final class HttpRetry {
    private HttpRetry() {
    }

    static long delay(Map<String, List<String>> r13, long r14) {
        String r0 = null;
        if (r13 == null) goto L16;
        Iterator<Map.Entry<String, List<String>>> r2 = r13.entrySet().iterator();
    L6:
        if (r2.hasNext() == false) goto L16;
        Map.Entry<String, List<String>> r3 = r2.next();
        if (r3.getKey() == null) goto L6;
        if (r3.getKey().equalsIgnoreCase(HttpHeaders.RETRY_AFTER) == false) goto L6;
        if (r3.getValue() == null) goto L6;
        if (r3.getValue().isEmpty() == true) goto L6;
        r0 = r3.getValue().get(0);
    L16:
        if (r0 == null) goto L39;
        String r1 = r0.trim();
        if (r1.matches("[0-9]+") == false) goto L26;
        long r4 = Long.parseLong(r1);     // Catch: NumberFormatException -> L37
        if (r4 <= 9223372036854775L) goto L25;
        return Long.MAX_VALUE;
    L25:
        return r4 * 1000;
    L26:
        String[] r5 = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};
        int r6 = r5.length;
        int r7 = 0;
    L27:
        if (r7 >= r6) goto L61;
        SimpleDateFormat r8 = new SimpleDateFormat(r5[r7], Locale.US);     // Catch: RuntimeException -> L35
        r8.setTimeZone(TimeZone.getTimeZone("GMT"));     // Catch: RuntimeException -> L35
        r8.setLenient(false);     // Catch: RuntimeException -> L35
        ParsePosition r9 = new ParsePosition(0);     // Catch: RuntimeException -> L35
        Date r10 = r8.parse(r1, r9);     // Catch: RuntimeException -> L35
        if (r10 == null) goto L36;
        if (r9.getIndex() != r1.length()) goto L36;
        return Math.max(0, r10.getTime() - r14);
    L36:
        r7 = r7 + 1;
        goto L27
    L61:
        return 60000;
    L38:
        return Long.MAX_VALUE;
    L39:
        return 60000;
    }

    static HttpDataSource.InvalidResponseCodeException response(Throwable r2) {
        Throwable r0 = r2;
    L3:
        if (r0 == null) goto L9;
        if ((r0 instanceof HttpDataSource.InvalidResponseCodeException) == true) goto L7;
        r0 = r0.getCause();
        goto L3
    L7:
        return (HttpDataSource.InvalidResponseCodeException) r0;
    L9:
        return null;
    }
}
