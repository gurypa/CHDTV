package androidx.media3.common;

import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class MediaLibraryInfo {
    public static final boolean ASSERTIONS_ENABLED = true;
    public static final String TAG = "AndroidXMedia3";
    public static final boolean TRACE_ENABLED = true;
    public static final String VERSION = "1.5.1";
    public static final int VERSION_INT = 1005001300;
    public static final String VERSION_SLASHY = "AndroidXMedia3/1.5.1";
    private static final HashSet<String> registeredModules = null;
    private static String registeredModulesString;

    static {
        registeredModules = new HashSet();
        registeredModulesString = "media3.common";
    }

    private MediaLibraryInfo() {
    }

    public static synchronized void registerModule(String r3) {
        monitor-enter(MediaLibraryInfo.class);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (registeredModules.add(r3) == false) goto L7;
        registeredModulesString += ", " + r3;     // Catch: Throwable -> L9
    L7:
        monitor-exit(MediaLibraryInfo.class);
    }

    public static synchronized String registeredModules() {
        monitor-enter(MediaLibraryInfo.class);
        String r1 = registeredModulesString;     // Catch: Throwable -> L7
        monitor-exit(MediaLibraryInfo.class);
        return r1;
    L7:
        th = move-exception;
        throw th;
    }
}
