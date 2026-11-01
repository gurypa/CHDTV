package es.antonio.m3u;

import android.content.Context;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes7.dex */
final class DemandMetadata {
    private static final Object[] locks = null;

    static {
        locks = new Object[64];
        int r0 = 0;
    L4:
        if (r0 >= locks.length) goto L5;
        locks[r0] = new Object();
        r0 = r0 + 1;
        goto L4
    }

    DemandMetadata() {
    }

    static void clear(Context r0) {
        DemandDiskCache.clear(directory(r0));
    }

    static File directory(Context r2) {
        return new File(r2.getFilesDir(), "tmdb-title-cache-v183");
    }

    static JSONObject get(Context r4, String r5) {
        File r0 = new File(directory(r4), String.valueOf(DemandDiskCache.hash(r5)) + ".json");
        if (r0.isFile() == true) goto L10;
        return null;
    L10:
        return new JSONObject(new String(DemandDiskCache.read(r0, 2097152), StandardCharsets.UTF_8));
    L8:
        r0.delete();
        return null;
    }

    static /* synthetic */ void lambda$0(byte[] r0, OutputStream r1) throws IOException {
        r1.write(r0);
    }

    static Object lock(String r2) {
        return locks[(r2.hashCode() & Integer.MAX_VALUE) % locks.length];
    }

    static void put(Context r6, String r7, JSONObject r8) {
        final byte[] r9 = r8.toString().getBytes(StandardCharsets.UTF_8);
        File r0 = directory(r6);
        DemandDiskCache.put(r0, new File(r0, String.valueOf(DemandDiskCache.hash(r7)) + ".json"), new DemandMetadata$$ExternalSyntheticLambda0(r9), 100663296, AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);
    }
}
