package androidx.media3.datasource;

import android.text.TextUtils;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import com.google.common.base.Ascii;
import com.google.common.base.Predicate;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface HttpDataSource extends DataSource {
    public static final Predicate<String> REJECT_PAYWALL_TYPES = null;

    /* JADX INFO: renamed from: androidx.media3.datasource.HttpDataSource$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        static {
            Predicate<String> r0 = HttpDataSource.REJECT_PAYWALL_TYPES;
        }

        public static /* synthetic */ boolean lambda$static$0(String r2) {
            if (r2 != null) goto L5;
            return false;
        L5:
            String r3 = Ascii.toLowerCase(r2);
            if (TextUtils.isEmpty(r3) == false) goto L8;
            return false;
        L8:
            if (r3.contains("text") == false) goto L12;
            if (r3.contains(MimeTypes.TEXT_VTT) == true) goto L12;
            return false;
        L12:
            if (r3.contains("html") == false) goto L14;
            return false;
        L14:
            if (r3.contains("xml") == true) goto L21;
            return true;
        L21:
            return false;
        }
    }

    public static abstract class BaseFactory implements Factory {
        private final RequestProperties defaultRequestProperties;

        public BaseFactory() {
            this.defaultRequestProperties = new RequestProperties();
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory, androidx.media3.datasource.DataSource.Factory
        public final HttpDataSource createDataSource() {
            return createDataSourceInternal(this.defaultRequestProperties);
        }

        protected abstract HttpDataSource createDataSourceInternal(RequestProperties r1);

        @Override // androidx.media3.datasource.HttpDataSource.Factory
        public final Factory setDefaultRequestProperties(Map<String, String> r2) {
            this.defaultRequestProperties.clearAndSet(r2);
            return this;
        }
    }

    public static final class CleartextNotPermittedException extends HttpDataSourceException {
        public CleartextNotPermittedException(IOException r7, DataSpec r8) {
            super("Cleartext HTTP traffic not permitted. See https://developer.android.com/guide/topics/media/issues/cleartext-not-permitted", r7, r8, PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED, 1);
        }
    }

    public interface Factory extends DataSource.Factory {

        /* JADX INFO: renamed from: androidx.media3.datasource.HttpDataSource$Factory$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static /* bridge */ /* synthetic */ DataSource $default$createDataSource(Factory r1) {
                return r1.createDataSource();
            }
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        HttpDataSource createDataSource();

        Factory setDefaultRequestProperties(Map<String, String> r1);
    }

    public static class HttpDataSourceException extends DataSourceException {
        public static final int TYPE_CLOSE = 3;
        public static final int TYPE_OPEN = 1;
        public static final int TYPE_READ = 2;
        public final DataSpec dataSpec;
        public final int type;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {
        }

        @Deprecated
        public HttpDataSourceException(DataSpec r2, int r3) {
            this(r2, 2000, r3);
        }

        public HttpDataSourceException(DataSpec r2, int r3, int r4) {
            super(assignErrorCode(r3, r4));
            this.dataSpec = r2;
            this.type = r4;
        }

        @Deprecated
        public HttpDataSourceException(IOException r2, DataSpec r3, int r4) {
            this(r2, r3, 2000, r4);
        }

        public HttpDataSourceException(IOException r2, DataSpec r3, int r4, int r5) {
            super(r2, assignErrorCode(r4, r5));
            this.dataSpec = r3;
            this.type = r5;
        }

        @Deprecated
        public HttpDataSourceException(String r2, DataSpec r3, int r4) {
            this(r2, r3, 2000, r4);
        }

        public HttpDataSourceException(String r2, DataSpec r3, int r4, int r5) {
            super(r2, assignErrorCode(r4, r5));
            this.dataSpec = r3;
            this.type = r5;
        }

        @Deprecated
        public HttpDataSourceException(String r7, IOException r8, DataSpec r9, int r10) {
            this(r7, r8, r9, 2000, r10);
        }

        public HttpDataSourceException(String r2, IOException r3, DataSpec r4, int r5, int r6) {
            super(r2, r3, assignErrorCode(r5, r6));
            this.dataSpec = r4;
            this.type = r6;
        }

        private static int assignErrorCode(int r1, int r2) {
            if (r1 != 2000) goto L8;
            if (r2 != 1) goto L8;
            return PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED;
        L8:
            return r1;
        }

        public static HttpDataSourceException createForIOException(IOException r3, DataSpec r4, int r5) {
            String r0 = r3.getMessage();
            if ((r3 instanceof SocketTimeoutException) == false) goto L6;
            int r1 = PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT;
        L14:
            if (r1 != 2007) goto L17;
            return new CleartextNotPermittedException(r3, r4);
        L17:
            return new HttpDataSourceException(r3, r4, r1, r5);
        L6:
            if ((r3 instanceof InterruptedIOException) == false) goto L8;
            r1 = 1004;
            goto L14
        L8:
            if (r0 != null) goto L10;
        L12:
            r1 = PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED;
            goto L14
        L10:
            if (Ascii.toLowerCase(r0).matches("cleartext.*not permitted.*") == false) goto L12;
            r1 = PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED;
            goto L14
        }
    }

    public static final class InvalidContentTypeException extends HttpDataSourceException {
        public final String contentType;

        public InvalidContentTypeException(String r4, DataSpec r5) {
            super("Invalid content type: " + r4, r5, PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE, 1);
            this.contentType = r4;
        }
    }

    public static final class InvalidResponseCodeException extends HttpDataSourceException {
        public final Map<String, List<String>> headerFields;
        public final byte[] responseBody;
        public final int responseCode;
        public final String responseMessage;

        public InvalidResponseCodeException(int r8, String r9, IOException r10, Map<String, List<String>> r11, DataSpec r12, byte[] r13) {
            super("Response code: " + r8, r10, r12, PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS, 1);
            this.responseCode = r8;
            this.responseMessage = r9;
            this.headerFields = r11;
            this.responseBody = r13;
        }
    }

    public static final class RequestProperties {
        private final Map<String, String> requestProperties;
        private Map<String, String> requestPropertiesSnapshot;

        public RequestProperties() {
            this.requestProperties = new HashMap();
        }

        public synchronized void clear() {
            monitor-enter(this);
            this.requestPropertiesSnapshot = null;     // Catch: Throwable -> L7
            this.requestProperties.clear();     // Catch: Throwable -> L7
            monitor-exit(this);
            return;
        L7:
            th = move-exception;
            throw th;
        }

        public synchronized void clearAndSet(Map<String, String> r2) {
            monitor-enter(this);
            this.requestPropertiesSnapshot = null;     // Catch: Throwable -> L7
            this.requestProperties.clear();     // Catch: Throwable -> L7
            this.requestProperties.putAll(r2);     // Catch: Throwable -> L7
            monitor-exit(this);
            return;
        L7:
            th = move-exception;
            throw th;
        }

        public synchronized Map<String, String> getSnapshot() {
            monitor-enter(this);
        L9:
            th = move-exception;
            throw th;
        L4:
            if (this.requestPropertiesSnapshot != null) goto L6;
            this.requestPropertiesSnapshot = Collections.unmodifiableMap(new HashMap(this.requestProperties));     // Catch: Throwable -> L9
        L6:
            Map<String, String> r0 = this.requestPropertiesSnapshot;     // Catch: Throwable -> L9
            monitor-exit(this);
            return r0;
        }

        public synchronized void remove(String r2) {
            monitor-enter(this);
            this.requestPropertiesSnapshot = null;     // Catch: Throwable -> L7
            this.requestProperties.remove(r2);     // Catch: Throwable -> L7
            monitor-exit(this);
            return;
        L7:
            th = move-exception;
            throw th;
        }

        public synchronized void set(String r2, String r3) {
            monitor-enter(this);
            this.requestPropertiesSnapshot = null;     // Catch: Throwable -> L7
            this.requestProperties.put(r2, r3);     // Catch: Throwable -> L7
            monitor-exit(this);
            return;
        L7:
            th = move-exception;
            throw th;
        }

        public synchronized void set(Map<String, String> r2) {
            monitor-enter(this);
            this.requestPropertiesSnapshot = null;     // Catch: Throwable -> L7
            this.requestProperties.putAll(r2);     // Catch: Throwable -> L7
            monitor-exit(this);
            return;
        L7:
            th = move-exception;
            throw th;
        }
    }

    static {
        REJECT_PAYWALL_TYPES = new HttpDataSource$$ExternalSyntheticLambda0();
    }

    void clearAllRequestProperties();

    void clearRequestProperty(String r1);

    @Override // androidx.media3.datasource.DataSource
    void close() throws HttpDataSourceException;

    int getResponseCode();

    @Override // androidx.media3.datasource.DataSource
    Map<String, List<String>> getResponseHeaders();

    @Override // androidx.media3.datasource.DataSource
    long open(DataSpec r1) throws HttpDataSourceException;

    @Override // androidx.media3.common.DataReader
    int read(byte[] r1, int r2, int r3) throws HttpDataSourceException;

    void setRequestProperty(String r1, String r2);
}
