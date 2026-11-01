package com.arthenica.ffmpegkit;

import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MediaInformation {
    public static final String KEY_BIT_RATE = "bit_rate";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_FILENAME = "filename";
    public static final String KEY_FORMAT = "format_name";
    public static final String KEY_FORMAT_LONG = "format_long_name";
    public static final String KEY_FORMAT_PROPERTIES = "format";
    public static final String KEY_SIZE = "size";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_TAGS = "tags";
    private final List<Chapter> chapters;
    private final JSONObject jsonObject;
    private final List<StreamInformation> streams;

    public MediaInformation(JSONObject r1, List<StreamInformation> r2, List<Chapter> r3) {
        this.jsonObject = r1;
        this.streams = r2;
        this.chapters = r3;
    }

    public JSONObject getAllProperties() {
        return this.jsonObject;
    }

    public String getBitrate() {
        return getStringFormatProperty("bit_rate");
    }

    public List<Chapter> getChapters() {
        return this.chapters;
    }

    public String getDuration() {
        return getStringFormatProperty(KEY_DURATION);
    }

    public String getFilename() {
        return getStringFormatProperty(KEY_FILENAME);
    }

    public String getFormat() {
        return getStringFormatProperty(KEY_FORMAT);
    }

    public JSONObject getFormatProperties() {
        return this.jsonObject.optJSONObject(KEY_FORMAT_PROPERTIES);
    }

    public JSONObject getFormatProperty(String r3) {
        JSONObject r0 = getFormatProperties();
        if (r0 != null) goto L7;
        return null;
    L7:
        return r0.optJSONObject(r3);
    }

    public String getLongFormat() {
        return getStringFormatProperty(KEY_FORMAT_LONG);
    }

    public Long getNumberFormatProperty(String r4) {
        JSONObject r0 = getFormatProperties();
        if (r0 != null) goto L6;
        return null;
    L6:
        if (r0.has(r4) == true) goto L8;
        return null;
    L8:
        return Long.valueOf(r0.optLong(r4));
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

    public String getSize() {
        return getStringFormatProperty(KEY_SIZE);
    }

    public String getStartTime() {
        return getStringFormatProperty("start_time");
    }

    public List<StreamInformation> getStreams() {
        return this.streams;
    }

    public String getStringFormatProperty(String r4) {
        JSONObject r0 = getFormatProperties();
        if (r0 != null) goto L6;
        return null;
    L6:
        if (r0.has(r4) == true) goto L8;
        return null;
    L8:
        return r0.optString(r4);
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
        return getFormatProperty("tags");
    }
}
