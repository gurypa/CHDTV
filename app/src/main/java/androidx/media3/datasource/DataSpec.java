package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.Assertions;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DataSpec {
    public static final int FLAG_ALLOW_CACHE_FRAGMENTATION = 4;
    public static final int FLAG_ALLOW_GZIP = 1;
    public static final int FLAG_DONT_CACHE_IF_LENGTH_UNKNOWN = 2;
    public static final int FLAG_MIGHT_NOT_USE_FULL_NETWORK_SPEED = 8;
    public static final int HTTP_METHOD_GET = 1;
    public static final int HTTP_METHOD_HEAD = 3;
    public static final int HTTP_METHOD_POST = 2;

    @Deprecated
    public final long absoluteStreamPosition;
    public final Object customData;
    public final int flags;
    public final byte[] httpBody;
    public final int httpMethod;
    public final Map<String, String> httpRequestHeaders;
    public final String key;
    public final long length;
    public final long position;
    public final Uri uri;
    public final long uriPositionOffset;

    /* JADX INFO: renamed from: androidx.media3.datasource.DataSpec$1 */
    static /* synthetic */ class C02101 {
    }

    public static final class Builder {
        private Object customData;
        private int flags;
        private byte[] httpBody;
        private int httpMethod;
        private Map<String, String> httpRequestHeaders;
        private String key;
        private long length;
        private long position;
        private Uri uri;
        private long uriPositionOffset;

        public Builder() {
            this.httpMethod = 1;
            this.httpRequestHeaders = Collections.emptyMap();
            this.length = -1;
        }

        private Builder(DataSpec r3) {
            this.uri = r3.uri;
            this.uriPositionOffset = r3.uriPositionOffset;
            this.httpMethod = r3.httpMethod;
            this.httpBody = r3.httpBody;
            this.httpRequestHeaders = r3.httpRequestHeaders;
            this.position = r3.position;
            this.length = r3.length;
            this.key = r3.key;
            this.flags = r3.flags;
            this.customData = r3.customData;
        }

        /* synthetic */ Builder(DataSpec r1, C02101 r2) {
            this(r1);
        }

        public DataSpec build() {
            Assertions.checkStateNotNull(this.uri, "The uri must be set.");
            return new DataSpec(this.uri, this.uriPositionOffset, this.httpMethod, this.httpBody, this.httpRequestHeaders, this.position, this.length, this.key, this.flags, this.customData, null);
        }

        public Builder setCustomData(Object r1) {
            this.customData = r1;
            return this;
        }

        public Builder setFlags(int r1) {
            this.flags = r1;
            return this;
        }

        public Builder setHttpBody(byte[] r1) {
            this.httpBody = r1;
            return this;
        }

        public Builder setHttpMethod(int r1) {
            this.httpMethod = r1;
            return this;
        }

        public Builder setHttpRequestHeaders(Map<String, String> r1) {
            this.httpRequestHeaders = r1;
            return this;
        }

        public Builder setKey(String r1) {
            this.key = r1;
            return this;
        }

        public Builder setLength(long r1) {
            this.length = r1;
            return this;
        }

        public Builder setPosition(long r1) {
            this.position = r1;
            return this;
        }

        public Builder setUri(Uri r1) {
            this.uri = r1;
            return this;
        }

        public Builder setUri(String r2) {
            this.uri = Uri.parse(r2);
            return this;
        }

        public Builder setUriPositionOffset(long r1) {
            this.uriPositionOffset = r1;
            return this;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface HttpMethod {
    }

    static {
        MediaLibraryInfo.registerModule("media3.datasource");
    }

    public DataSpec(Uri r7) {
        this(r7, 0, -1);
    }

    private DataSpec(Uri r10, long r11, int r13, byte[] r14, Map<String, String> r15, long r16, long r18, String r20, int r21, Object r22) {
        boolean r5 = true;
        if ((r11 + r16) < 0) goto L5;
        boolean r4 = true;
    L6:
        Assertions.checkArgument(r4);
        if (r16 < 0) goto L9;
        boolean r6 = true;
    L10:
        Assertions.checkArgument(r6);
        if (r18 <= 0) goto L13;
    L16:
        Assertions.checkArgument(r5);
        this.uri = (Uri) Assertions.checkNotNull(r10);
        this.uriPositionOffset = r11;
        this.httpMethod = r13;
        if (r14 != null) goto L19;
    L21:
        byte[] r7 = null;
    L22:
        this.httpBody = r7;
        this.httpRequestHeaders = Collections.unmodifiableMap(new HashMap(r15));
        this.position = r16;
        this.absoluteStreamPosition = r11 + r16;
        this.length = r18;
        this.key = r20;
        this.flags = r21;
        this.customData = r22;
        return;
    L19:
        if (r14.length == 0) goto L21;
        r7 = r14;
        goto L22
    L13:
        if (r18 == (-1)) goto L16;
        r5 = false;
        goto L16
    L9:
        r6 = false;
        goto L10
    L5:
        r4 = false;
        goto L6
    }

    /* synthetic */ DataSpec(Uri r1, long r2, int r4, byte[] r5, Map r6, long r7, long r9, String r11, int r12, Object r13, C02101 r14) {
        this(r1, r2, r4, r5, r6, r7, r9, r11, r12, r13);
    }

    public DataSpec(Uri r8, long r9, long r11) {
        this(r8, r9, r11, null);
    }

    @Deprecated
    public DataSpec(Uri r15, long r16, long r18, String r20) {
        this(r15, 0, 1, null, Collections.emptyMap(), r16, r18, r20, 0, null);
    }

    public static String getStringForHttpMethod(int r1) {
        switch(r1) {
            case 1: goto L9;
            case 2: goto L7;
            case 3: goto L5;
            default: goto L4;
        };
    L5:
        return "HEAD";
    L7:
        return "POST";
    L9:
        return "GET";
    L4:
        throw new IllegalStateException();
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public final String getHttpMethodString() {
        return getStringForHttpMethod(this.httpMethod);
    }

    public boolean isFlagSet(int r2) {
        if ((this.flags & r2) != r2) goto L5;
        return true;
    L5:
        return false;
    }

    public DataSpec subrange(long r5) {
        long r2 = -1;
        if (this.length == (-1)) goto L7;
        r2 = this.length - r5;
    L7:
        return subrange(r5, r2);
    }

    public DataSpec subrange(long r17, long r19) {
        if (r17 != 0) goto L8;
        if (this.length != r19) goto L8;
        return this;
    L8:
        return new DataSpec(this.uri, this.uriPositionOffset, this.httpMethod, this.httpBody, this.httpRequestHeaders, this.position + r17, r19, this.key, this.flags, this.customData);
    }

    public String toString() {
        return "DataSpec[" + getHttpMethodString() + " " + this.uri + ", " + this.position + ", " + this.length + ", " + this.key + ", " + this.flags + "]";
    }

    public DataSpec withAdditionalHeaders(Map<String, String> r18) {
        HashMap r1 = new HashMap(this.httpRequestHeaders);
        r1.putAll(r18);
        return new DataSpec(this.uri, this.uriPositionOffset, this.httpMethod, this.httpBody, r1, this.position, this.length, this.key, this.flags, this.customData);
    }

    public DataSpec withRequestHeaders(Map<String, String> r15) {
        return new DataSpec(this.uri, this.uriPositionOffset, this.httpMethod, this.httpBody, r15, this.position, this.length, this.key, this.flags, this.customData);
    }

    public DataSpec withUri(Uri r15) {
        return new DataSpec(r15, this.uriPositionOffset, this.httpMethod, this.httpBody, this.httpRequestHeaders, this.position, this.length, this.key, this.flags, this.customData);
    }
}
