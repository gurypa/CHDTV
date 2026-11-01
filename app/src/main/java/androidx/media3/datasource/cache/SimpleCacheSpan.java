package androidx.media3.datasource.cache;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class SimpleCacheSpan extends CacheSpan {
    private static final Pattern CACHE_FILE_PATTERN_V1 = null;
    private static final Pattern CACHE_FILE_PATTERN_V2 = null;
    private static final Pattern CACHE_FILE_PATTERN_V3 = null;
    static final String COMMON_SUFFIX = ".exo";
    private static final String SUFFIX = ".v3.exo";

    static {
        CACHE_FILE_PATTERN_V1 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v1\\.exo$", 32);
        CACHE_FILE_PATTERN_V2 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v2\\.exo$", 32);
        CACHE_FILE_PATTERN_V3 = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)\\.v3\\.exo$", 32);
    }

    private SimpleCacheSpan(String r1, long r2, long r4, long r6, File r8) {
        super(r1, r2, r4, r6, r8);
    }

    public static SimpleCacheSpan createCacheEntry(File r13, long r14, long r16, CachedContentIndex r18) {
        String r1 = r13.getName();
        if (r1.endsWith(SUFFIX) == true) goto L8;
        File r2 = upgradeFile(r13, r18);
        if (r2 != null) goto L7;
        return null;
    L7:
        r1 = r2.getName();
        File r12 = r2;
    L9:
        Matcher r15 = CACHE_FILE_PATTERN_V3.matcher(r1);
        if (r15.matches() == true) goto L12;
        return null;
    L12:
        String r5 = r18.getKeyForId(Integer.parseInt((String) Assertions.checkNotNull(r15.group(1))));
        if (r5 != null) goto L16;
        return null;
    L16:
        if (r14 != (-1)) goto L18;
        long r8 = r12.length();
    L20:
        if (r8 != 0) goto L22;
        return null;
    L22:
        long r6 = Long.parseLong((String) Assertions.checkNotNull(r15.group(2)));
        if (r16 != C0180C.TIME_UNSET) goto L25;
        long r10 = Long.parseLong((String) Assertions.checkNotNull(r15.group(3)));
    L27:
        return new SimpleCacheSpan(r5, r6, r8, r10, r12);
    L25:
        r10 = r16;
        goto L27
    L18:
        r8 = r14;
        goto L20
    L8:
        r12 = r13;
        goto L9
    }

    public static SimpleCacheSpan createCacheEntry(File r6, long r7, CachedContentIndex r9) {
        return createCacheEntry(r6, r7, C0180C.TIME_UNSET, r9);
    }

    public static SimpleCacheSpan createHole(String r9, long r10, long r12) {
        return new SimpleCacheSpan(r9, r10, r12, C0180C.TIME_UNSET, null);
    }

    public static SimpleCacheSpan createLookup(String r9, long r10) {
        return new SimpleCacheSpan(r9, r10, -1, C0180C.TIME_UNSET, null);
    }

    public static File getCacheFile(File r3, int r4, long r5, long r7) {
        return new File(r3, r4 + "." + r5 + "." + r7 + SUFFIX);
    }

    private static File upgradeFile(File r11, CachedContentIndex r12) {
        String r0 = null;
        String r1 = r11.getName();
        Matcher r2 = CACHE_FILE_PATTERN_V2.matcher(r1);
        if (r2.matches() == false) goto L5;
        r0 = Util.unescapeFileName((String) Assertions.checkNotNull(r2.group(1)));
    L9:
        if (r0 != null) goto L11;
        return null;
    L11:
        File r4 = getCacheFile((File) Assertions.checkStateNotNull(r11.getParentFile()), r12.assignIdForKey(r0), Long.parseLong((String) Assertions.checkNotNull(r2.group(2))), Long.parseLong((String) Assertions.checkNotNull(r2.group(3))));
        if (r11.renameTo(r4) == true) goto L14;
        return null;
    L14:
        return r4;
    L5:
        r2 = CACHE_FILE_PATTERN_V1.matcher(r1);
        if (r2.matches() == false) goto L9;
        r0 = (String) Assertions.checkNotNull(r2.group(1));
        goto L9
    }

    public SimpleCacheSpan copyWithFileAndLastTouchTimestamp(File r11, long r12) {
        Assertions.checkState(this.isCached);
        return new SimpleCacheSpan(this.key, this.position, this.length, r12, r11);
    }
}
