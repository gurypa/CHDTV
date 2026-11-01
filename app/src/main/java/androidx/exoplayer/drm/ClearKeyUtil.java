package androidx.media3.exoplayer.drm;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class ClearKeyUtil {
    private static final String TAG = "ClearKeyUtil";

    private ClearKeyUtil() {
    }

    public static byte[] adjustRequestData(byte[] r2) {
        if (Util.SDK_INT < 27) goto L6;
        return r2;
    L6:
        return Util.getUtf8Bytes(base64ToBase64Url(Util.fromUtf8Bytes(r2)));
    }

    public static byte[] adjustResponseData(byte[] r6) {
        if (Util.SDK_INT < 27) goto L16;
        return r6;
    L16:
        JSONObject r0 = new JSONObject(Util.fromUtf8Bytes(r6));     // Catch: JSONException -> L13
        StringBuilder r1 = new StringBuilder("{\"keys\":[");     // Catch: JSONException -> L13
        JSONArray r2 = r0.getJSONArray("keys");     // Catch: JSONException -> L13
        int r3 = 0;
    L7:
        if (r3 >= r2.length()) goto L11;
        if (r3 == 0) goto L10;
        r1.append(",");     // Catch: JSONException -> L13
    L10:
        JSONObject r4 = r2.getJSONObject(r3);     // Catch: JSONException -> L13
        r1.append("{\"k\":\"");     // Catch: JSONException -> L13
        r1.append(base64UrlToBase64(r4.getString("k")));     // Catch: JSONException -> L13
        r1.append("\",\"kid\":\"");     // Catch: JSONException -> L13
        r1.append(base64UrlToBase64(r4.getString("kid")));     // Catch: JSONException -> L13
        r1.append("\",\"kty\":\"");     // Catch: JSONException -> L13
        r1.append(r4.getString("kty"));     // Catch: JSONException -> L13
        r1.append("\"}");     // Catch: JSONException -> L13
        r3 = r3 + 1;     // Catch: JSONException -> L13
        goto L7
    L11:
        r1.append("]}");     // Catch: JSONException -> L13
        return Util.getUtf8Bytes(r1.toString());
    L13:
        e = move-exception;
        Log.m45e(TAG, "Failed to adjust response data: " + Util.fromUtf8Bytes(r6), e);
        return r6;
    }

    private static String base64ToBase64Url(String r3) {
        return r3.replace('+', '-').replace('/', '_');
    }

    private static String base64UrlToBase64(String r3) {
        return r3.replace('-', '+').replace('_', '/');
    }
}
