package androidx.media3.datasource.cache;

import androidx.media3.datasource.DataSpec;

/* JADX INFO: loaded from: classes.dex */
public interface CacheKeyFactory {
    public static final CacheKeyFactory DEFAULT = null;

    /* JADX INFO: renamed from: androidx.media3.datasource.cache.CacheKeyFactory$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        static {
            CacheKeyFactory r0 = CacheKeyFactory.DEFAULT;
        }

        public static /* synthetic */ String lambda$static$0(DataSpec r1) {
            if (r1.key == null) goto L6;
            return r1.key;
        L6:
            return r1.uri.toString();
        }
    }

    static {
        DEFAULT = new CacheKeyFactory$$ExternalSyntheticLambda0();
    }

    String buildCacheKey(DataSpec r1);
}
