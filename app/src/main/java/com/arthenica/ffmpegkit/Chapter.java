package com.arthenica.ffmpegkit;

import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Chapter {
    public static final String KEY_END = "end";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_ID = "id";
    public static final String KEY_START = "start";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_TIME_BASE = "time_base";
    private final JSONObject jsonObject;

    public Chapter(JSONObject r1) {
        this.jsonObject = r1;
    }

    public JSONObject getAllProperties() {
        return this.jsonObject;
    }

    public Long getEnd() {
        return getNumberProperty("end");
    }

    public String getEndTime() {
        return getStringProperty(KEY_END_TIME);
    }

    public Long getId() {
        return getNumberProperty("id");
    }

    public Long getNumberProperty(String r4) {
        JSONObject r0 = getAllProperties();
        if (r0 != null) goto L6;
        return null;
    L6:
        if (r0.has(r4) == true) goto L8;
        return null;
    L8:
        return Long.valueOf(r0.optLong(r4));
    }

    public JSONObject getProperty(String r3) {
        JSONObject r0 = getAllProperties();
        if (r0 != null) goto L7;
        return null;
    L7:
        return r0.optJSONObject(r3);
    }

    public Long getStart() {
        return getNumberProperty("start");
    }

    public String getStartTime() {
        return getStringProperty("start_time");
    }

    public String getStringProperty(String r4) {
        JSONObject r0 = getAllProperties();
        if (r0 != null) goto L6;
        return null;
    L6:
        if (r0.has(r4) == true) goto L8;
        return null;
    L8:
        return r0.optString(r4);
    }

    public JSONObject getTags() {
        return getProperty("tags");
    }

    public String getTimeBase() {
        return getStringProperty("time_base");
    }
}
