package es.antonio.m3u;

import androidx.core.os.EnvironmentCompat;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class TvHlsTrace {
    private final Map<String, Integer> reported;
    private final Map<String, Track> tracks;

    static final class Track {
        final String kind;
        final String role;

        Track(String r1, String r2) {
            this.kind = r1;
            this.role = r2;
        }
    }

    TvHlsTrace() {
        this.tracks = new LinkedHashMap();
        this.reported = new HashMap();
    }

    private static String key(String r8) {
        URI r0 = new URI(r8);     // Catch: Exception -> L4
        return new URI(r0.getScheme(), r0.getAuthority(), r0.getPath(), null, null).toString();
    L5:
        return r8;
    }

    synchronized void register(String r3, String r4, String r5) {
        monitor-enter(this);
        String r6 = key(r3);     // Catch: Throwable -> L18
        Track r0 = this.tracks.get(r6);     // Catch: Throwable -> L18
        if (r0 != null) goto L6;
    L10:
        this.tracks.put(r6, new Track(r4, r5));     // Catch: Throwable -> L18
    L11:
        if (this.tracks.size() <= 256) goto L14;
        this.tracks.remove(this.tracks.keySet().iterator().next());     // Catch: Throwable -> L18
        goto L11
    L14:
        monitor-exit(this);
        return;
    L6:
        if (r0.role.equals(r5) == true) goto L8;
    L9:
        r5 = EnvironmentCompat.MEDIA_UNKNOWN;
        goto L10
    L8:
        if (r0.kind.equals(r4) == true) goto L10;
    L18:
        th = move-exception;
        throw th;
    }

    synchronized String report(String r5, long r6, long r8, long r10, long r12, boolean r14) {
        monitor-enter(this);
        Track r7 = this.tracks.get(key(r5));     // Catch: Throwable -> L18
        if (r7 != null) goto L8;
    L6:
        monitor-exit(this);
        return null;
    L8:
        String r1 = String.valueOf(r7.kind) + ":" + r7.role;     // Catch: Throwable -> L18
        if (this.reported.containsKey(r1) == false) goto L11;
        int r2 = this.reported.get(r1).intValue();     // Catch: Throwable -> L18
    L13:
        if (r2 >= 3) goto L6;
        int r3 = r2 + 1;     // Catch: Throwable -> L18
        this.reported.put(r1, Integer.valueOf(r3));     // Catch: Throwable -> L18
        String r9 = "kind=" + r7.kind + " role=" + r7.role + " sample=" + r3 + " open_ms=" + r6 + " first_byte_ms=" + r8 + " read_elapsed_ms=" + r10 + " bytes=" + r12 + " cached=" + r14;     // Catch: Throwable -> L18
        monitor-exit(this);
        return r9;
    L11:
        r2 = 0;
    L18:
        th = move-exception;
        throw th;
    }

    synchronized String role(String r2) {
        monitor-enter(this);
        Track r3 = this.tracks.get(key(r2));     // Catch: Throwable -> L9
        if (r3 != null) goto L6;
        String r4 = EnvironmentCompat.MEDIA_UNKNOWN;
    L7:
        monitor-exit(this);
        return r4;
    L6:
        r4 = r3.role;     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        throw th;
    }

    synchronized int size() {
        monitor-enter(this);
        int r0 = this.tracks.size();     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }
}
