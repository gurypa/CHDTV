package es.antonio.m3u;

import java.util.Map;

/* JADX INFO: loaded from: classes7.dex */
final class EpgNameCache {
    private static final Map<String, Keys> cache = null;

    private static final class Keys {
        String base;
        String normalized;

        private Keys() {
        }

        /* synthetic */ Keys(C09951 r1) {
            this();
        }
    }

    static {
        final float r1 = 0.75f;
        final boolean r2 = true;
        final int r3 = 512;
        cache = new C09951(r3, r1, r2);
    }

    EpgNameCache() {
    }

    static String base(String r4) {
        if (r4 != null) goto L4;
        String r0 = "";
    L6:
        if (r0.length() > 4096) goto L8;
        Map<String, Keys> r1 = cache;
        monitor-enter(r1);
        Keys r2 = cache.get(r0);     // Catch: Throwable -> L30
        if (r2 != null) goto L14;
    L18:
        monitor-exit(r1);     // Catch: Throwable -> L30
        String r5 = EpgData.epg217BaseNameOriginal(r4);
        Map<String, Keys> r3 = cache;
        monitor-enter(r3);
        Keys r6 = cache.get(r0);     // Catch: Throwable -> L27
        if (r6 != null) goto L24;
        r6 = new Keys(null);     // Catch: Throwable -> L27
        cache.put(r0, r6);     // Catch: Throwable -> L27
    L24:
        r6.base = r5;     // Catch: Throwable -> L27
        monitor-exit(r3);     // Catch: Throwable -> L27
        return r5;
    L27:
        th = move-exception;
        throw th;
    L14:
        if (r2.base == null) goto L18;
        String r7 = r2.base;     // Catch: Throwable -> L30
        monitor-exit(r1);     // Catch: Throwable -> L30
        return r7;
    L30:
        th = move-exception;
        throw th;
    L8:
        return EpgData.epg217BaseNameOriginal(r4);
    L4:
        r0 = r4;
        goto L6
    }

    static String normalize(String r4) {
        if (r4 != null) goto L4;
        String r0 = "";
    L6:
        if (r0.length() > 4096) goto L8;
        Map<String, Keys> r1 = cache;
        monitor-enter(r1);
        Keys r2 = cache.get(r0);     // Catch: Throwable -> L30
        if (r2 != null) goto L14;
    L18:
        monitor-exit(r1);     // Catch: Throwable -> L30
        String r5 = EpgData.epg217NormalizeOriginal(r4);
        Map<String, Keys> r3 = cache;
        monitor-enter(r3);
        Keys r6 = cache.get(r0);     // Catch: Throwable -> L27
        if (r6 != null) goto L24;
        r6 = new Keys(null);     // Catch: Throwable -> L27
        cache.put(r0, r6);     // Catch: Throwable -> L27
    L24:
        r6.normalized = r5;     // Catch: Throwable -> L27
        monitor-exit(r3);     // Catch: Throwable -> L27
        return r5;
    L27:
        th = move-exception;
        throw th;
    L14:
        if (r2.normalized == null) goto L18;
        String r7 = r2.normalized;     // Catch: Throwable -> L30
        monitor-exit(r1);     // Catch: Throwable -> L30
        return r7;
    L30:
        th = move-exception;
        throw th;
    L8:
        return EpgData.epg217NormalizeOriginal(r4);
    L4:
        r0 = r4;
        goto L6
    }
}
