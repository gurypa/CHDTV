package com.arthenica.ffmpegkit;

import com.arthenica.smartexception.java.Exceptions;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MediaInformationJsonParser {
    public static final String KEY_CHAPTERS = "chapters";
    public static final String KEY_STREAMS = "streams";

    public MediaInformationJsonParser() {
    }

    public static MediaInformation from(String r4) {
        return fromWithError(r4);
    L4:
        e = move-exception;
        android.util.Log.e("ffmpeg-kit", String.format("MediaInformation parsing failed.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        return null;
    }

    public static MediaInformation fromWithError(String r8) throws JSONException {
        JSONObject r0 = new JSONObject(r8);
        JSONArray r1 = r0.optJSONArray(KEY_STREAMS);
        JSONArray r2 = r0.optJSONArray(KEY_CHAPTERS);
        ArrayList r3 = new ArrayList();
        int r4 = 0;
    L3:
        if (r1 == null) goto L10;
        if (r4 >= r1.length()) goto L10;
        JSONObject r5 = r1.optJSONObject(r4);
        if (r5 == null) goto L9;
        r3.add(new StreamInformation(r5));
    L9:
        r4 = r4 + 1;
    L10:
        ArrayList r6 = new ArrayList();
        int r7 = 0;
    L11:
        if (r2 == null) goto L19;
        if (r7 >= r2.length()) goto L19;
        JSONObject r9 = r2.optJSONObject(r7);
        if (r9 == null) goto L17;
        r6.add(new Chapter(r9));
    L17:
        r7 = r7 + 1;
    L19:
        return new MediaInformation(r0, r3, r6);
    }
}
