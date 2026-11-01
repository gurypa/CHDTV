package androidx.media3.datasource.cache;

import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public interface ContentMetadata {
    public static final String KEY_CONTENT_LENGTH = "exo_len";
    public static final String KEY_CUSTOM_PREFIX = "custom_";
    public static final String KEY_REDIRECTED_URI = "exo_redir";

    /* JADX INFO: renamed from: androidx.media3.datasource.cache.ContentMetadata$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static long getContentLength(ContentMetadata r3) {
            return r3.get(ContentMetadata.KEY_CONTENT_LENGTH, -1);
        }

        public static Uri getRedirectedUri(ContentMetadata r2) {
            String r1 = (String) null;
            String r3 = r2.get(ContentMetadata.KEY_REDIRECTED_URI, null);
            if (r3 != null) goto L6;
            return null;
        L6:
            return Uri.parse(r3);
        }
    }

    boolean contains(String r1);

    long get(String r1, long r2);

    String get(String r1, String r2);

    byte[] get(String r1, byte[] r2);
}
