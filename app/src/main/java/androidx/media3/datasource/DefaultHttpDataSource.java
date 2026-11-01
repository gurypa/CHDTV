package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import com.google.common.base.Predicate;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.net.HttpHeaders;
import com.google.common.p003io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes.dex */
public class DefaultHttpDataSource extends BaseDataSource implements HttpDataSource {
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 8000;
    public static final int DEFAULT_READ_TIMEOUT_MILLIS = 8000;
    private static final int HTTP_STATUS_PERMANENT_REDIRECT = 308;
    private static final int HTTP_STATUS_TEMPORARY_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "DefaultHttpDataSource";
    private final boolean allowCrossProtocolRedirects;
    private long bytesRead;
    private long bytesToRead;
    private final int connectTimeoutMillis;
    private HttpURLConnection connection;
    private final Predicate<String> contentTypePredicate;
    private final boolean crossProtocolRedirectsForceOriginal;
    private DataSpec dataSpec;
    private final HttpDataSource.RequestProperties defaultRequestProperties;
    private InputStream inputStream;
    private final boolean keepPostFor302Redirects;
    private final int readTimeoutMillis;
    private final HttpDataSource.RequestProperties requestProperties;
    private int responseCode;
    private boolean transferStarted;
    private final String userAgent;

    /* JADX INFO: renamed from: androidx.media3.datasource.DefaultHttpDataSource$1 */
    static /* synthetic */ class C02111 {
    }

    public static final class Factory implements HttpDataSource.Factory {
        private boolean allowCrossProtocolRedirects;
        private int connectTimeoutMs;
        private Predicate<String> contentTypePredicate;
        private boolean crossProtocolRedirectsForceOriginal;
        private final HttpDataSource.RequestProperties defaultRequestProperties;
        private boolean keepPostFor302Redirects;
        private int readTimeoutMs;
        private TransferListener transferListener;
        private String userAgent;

        public Factory() {
            this.defaultRequestProperties = new HttpDataSource.RequestProperties();
            this.connectTimeoutMs = 8000;
            this.readTimeoutMs = 8000;
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory, androidx.media3.datasource.DataSource.Factory
        public DefaultHttpDataSource createDataSource() {
            DefaultHttpDataSource r0 = new DefaultHttpDataSource(this.userAgent, this.connectTimeoutMs, this.readTimeoutMs, this.allowCrossProtocolRedirects, this.crossProtocolRedirectsForceOriginal, this.defaultRequestProperties, this.contentTypePredicate, this.keepPostFor302Redirects, null);
            if (this.transferListener == null) goto L5;
            r0.addTransferListener(this.transferListener);
        L5:
            return r0;
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory, androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ HttpDataSource createDataSource() {
            return createDataSource();
        }

        public Factory setAllowCrossProtocolRedirects(boolean r1) {
            this.allowCrossProtocolRedirects = r1;
            return this;
        }

        public Factory setConnectTimeoutMs(int r1) {
            this.connectTimeoutMs = r1;
            return this;
        }

        public Factory setContentTypePredicate(Predicate<String> r1) {
            this.contentTypePredicate = r1;
            return this;
        }

        public Factory setCrossProtocolRedirectsForceOriginal(boolean r1) {
            this.crossProtocolRedirectsForceOriginal = r1;
            return this;
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory
        public Factory setDefaultRequestProperties(Map<String, String> r2) {
            this.defaultRequestProperties.clearAndSet(r2);
            return this;
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory
        public /* bridge */ /* synthetic */ HttpDataSource.Factory setDefaultRequestProperties(Map r1) {
            return setDefaultRequestProperties(r1);
        }

        public Factory setKeepPostFor302Redirects(boolean r1) {
            this.keepPostFor302Redirects = r1;
            return this;
        }

        public Factory setReadTimeoutMs(int r1) {
            this.readTimeoutMs = r1;
            return this;
        }

        public Factory setTransferListener(TransferListener r1) {
            this.transferListener = r1;
            return this;
        }

        public Factory setUserAgent(String r1) {
            this.userAgent = r1;
            return this;
        }
    }

    private static class NullFilteringHeadersMap extends ForwardingMap<String, List<String>> {
        private final Map<String, List<String>> headers;

        public NullFilteringHeadersMap(Map<String, List<String>> r1) {
            this.headers = r1;
        }

        static /* synthetic */ boolean lambda$entrySet$1(Map.Entry r1) {
            if (r1.getKey() == null) goto L5;
            return true;
        L5:
            return false;
        }

        static /* synthetic */ boolean lambda$keySet$0(String r1) {
            if (r1 == null) goto L4;
            return true;
        L4:
            return false;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public boolean containsKey(Object r2) {
            if (r2 != null) goto L4;
        L6:
            return false;
        L4:
            if (super.containsKey(r2) == false) goto L6;
            return true;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public boolean containsValue(Object r2) {
            return super.standardContainsValue(r2);
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected /* bridge */ /* synthetic */ Object delegate() {
            return delegate();
        }

        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        protected Map<String, List<String>> delegate() {
            return this.headers;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<Map.Entry<String, List<String>>> entrySet() {
            return Sets.filter(super.entrySet(), new C0212x828bc3d2());
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public boolean equals(Object r2) {
            if (r2 != null) goto L4;
        L6:
            return false;
        L4:
            if (super.standardEquals(r2) == false) goto L6;
            return true;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public /* bridge */ /* synthetic */ Object get(Object r1) {
            return get(r1);
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public List<String> get(Object r2) {
            if (r2 != null) goto L5;
            return null;
        L5:
            return (List) super.get(r2);
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public int hashCode() {
            return super.standardHashCode();
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public boolean isEmpty() {
            if (super.isEmpty() == false) goto L5;
            return true;
        L5:
            if (super.size() == 1) goto L7;
        L9:
            return false;
        L7:
            if (super.containsKey(null) == false) goto L9;
            return true;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<String> keySet() {
            return Sets.filter(super.keySet(), new C0213x828bc3d3());
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public int size() {
            return super.size() - (super.containsKey(null) ? 1 : 0);
        }
    }

    private DefaultHttpDataSource(String r3, int r4, int r5, boolean r6, boolean r7, HttpDataSource.RequestProperties r8, Predicate<String> r9, boolean r10) {
        super(true);
        this.userAgent = r3;
        this.connectTimeoutMillis = r4;
        this.readTimeoutMillis = r5;
        this.allowCrossProtocolRedirects = r6;
        this.crossProtocolRedirectsForceOriginal = r7;
        if (r6 == false) goto L8;
        if (r7 == false) goto L8;
        throw new IllegalArgumentException("crossProtocolRedirectsForceOriginal should not be set if allowCrossProtocolRedirects is true");
    L8:
        this.defaultRequestProperties = r8;
        this.contentTypePredicate = r9;
        this.requestProperties = new HttpDataSource.RequestProperties();
        this.keepPostFor302Redirects = r10;
    }

    /* synthetic */ DefaultHttpDataSource(String r1, int r2, int r3, boolean r4, boolean r5, HttpDataSource.RequestProperties r6, Predicate r7, boolean r8, C02111 r9) {
        this(r1, r2, r3, r4, r5, r6, r7, r8);
    }

    private void closeConnectionQuietly() {
        if (this.connection == null) goto L11;
        this.connection.disconnect();     // Catch: Exception -> L6
        return;
    L6:
        e = move-exception;
        Log.m45e(TAG, "Unexpected error while disconnecting", e);
        return;
    }

    private URL handleRedirect(URL r8, String r9, DataSpec r10) throws HttpDataSource.HttpDataSourceException {
        if (r9 == null) goto L30;
        URL r2 = new URL(r8, r9);     // Catch: MalformedURLException -> L26
        String r3 = r2.getProtocol();
        if ("https".equals(r3) == true) goto L13;
        if ("http".equals(r3) == true) goto L13;
        throw new HttpDataSource.HttpDataSourceException("Unsupported protocol redirect: " + r3, r10, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, 1);
    L13:
        if (this.allowCrossProtocolRedirects == false) goto L15;
        return r2;
    L15:
        if (r3.equals(r8.getProtocol()) == false) goto L17;
        return r2;
    L17:
        if (this.crossProtocolRedirectsForceOriginal == false) goto L24;
        return new URL(r2.toString().replaceFirst(r3, r8.getProtocol()));
    L20:
        e = move-exception;
        throw new HttpDataSource.HttpDataSourceException(e, r10, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, 1);
    L24:
        throw new HttpDataSource.HttpDataSourceException("Disallowed cross-protocol redirect (" + r8.getProtocol() + " to " + r3 + ")", r10, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, 1);
    L26:
        e = move-exception;
        throw new HttpDataSource.HttpDataSourceException(e, r10, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, 1);
    L30:
        throw new HttpDataSource.HttpDataSourceException("Null location redirect", r10, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, 1);
    }

    private static boolean isCompressed(HttpURLConnection r2) {
        return "gzip".equalsIgnoreCase(r2.getHeaderField(HttpHeaders.CONTENT_ENCODING));
    }

    private HttpURLConnection makeConnection(DataSpec r17) throws IOException {
        URL r1 = new URL(r17.uri.toString());
        int r2 = r17.httpMethod;
        byte[] r3 = r17.httpBody;
        long r4 = r17.position;
        long r6 = r17.length;
        int r12 = 1;
        boolean r8 = r17.isFlagSet(1);
        if (this.allowCrossProtocolRedirects == false) goto L5;
    L10:
        int r0 = 0;
    L11:
        int r13 = r0 + 1;
        if (r0 > 20) goto L50;
        HttpURLConnection r9 = makeConnection(r1, r2, r3, r4, r6, r8, false, r17.httpRequestHeaders);
        int r10 = r9.getResponseCode();
        String r14 = r9.getHeaderField(HttpHeaders.LOCATION);
        if (r2 == r12) goto L18;
        if (r2 == 3) goto L18;
    L30:
        if (r2 != 2) goto L46;
        if (r10 != 300) goto L34;
    L38:
        r9.disconnect();
        if (this.keepPostFor302Redirects == false) goto L42;
        if (r10 != 302) goto L42;
        boolean r15 = true;
    L43:
        if (r15 == true) goto L45;
        r2 = 1;
        r3 = null;
    L45:
        r1 = handleRedirect(r1, r14, r17);
    L48:
        r0 = r13;
        r12 = 1;
    L42:
        r15 = false;
        goto L43
    L34:
        if (r10 == 301) goto L38;
        if (r10 == 302) goto L38;
        if (r10 == 303) goto L38;
    L46:
        return r9;
    L18:
        if (r10 != 300) goto L20;
    L47:
        r9.disconnect();
        r1 = handleRedirect(r1, r14, r17);
        goto L48
    L20:
        if (r10 == 301) goto L47;
        if (r10 == 302) goto L47;
        if (r10 == 303) goto L47;
        if (r10 == HTTP_STATUS_TEMPORARY_REDIRECT) goto L47;
        if (r10 != HTTP_STATUS_PERMANENT_REDIRECT) goto L30;
    L50:
        throw new HttpDataSource.HttpDataSourceException(new NoRouteToHostException("Too many redirects: " + r13), r17, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, 1);
    L5:
        if (this.crossProtocolRedirectsForceOriginal == true) goto L10;
        if (this.keepPostFor302Redirects == true) goto L10;
        return makeConnection(r1, r2, r3, r4, r6, r8, true, r17.httpRequestHeaders);
    }

    private HttpURLConnection makeConnection(URL r8, int r9, byte[] r10, long r11, long r13, boolean r15, boolean r16, Map<String, String> r17) throws IOException {
        HttpURLConnection r0 = openConnection(r8);
        r0.setConnectTimeout(this.connectTimeoutMillis);
        r0.setReadTimeout(this.readTimeoutMillis);
        HashMap r1 = new HashMap();
        if (this.defaultRequestProperties == null) goto L5;
        r1.putAll(this.defaultRequestProperties.getSnapshot());
    L5:
        r1.putAll(this.requestProperties.getSnapshot());
        r1.putAll(r17);
        Iterator r3 = r1.entrySet().iterator();
    L7:
        if (r3.hasNext() == false) goto L9;
        Map.Entry r4 = (Map.Entry) r3.next();
        r0.setRequestProperty((String) r4.getKey(), (String) r4.getValue());
        goto L7
    L9:
        String r5 = HttpUtil.buildRangeRequestHeader(r11, r13);
        if (r5 == null) goto L13;
        r0.setRequestProperty(HttpHeaders.RANGE, r5);
    L13:
        if (this.userAgent == null) goto L15;
        r0.setRequestProperty(HttpHeaders.USER_AGENT, this.userAgent);
    L15:
        if (r15 == false) goto L17;
        String r6 = "gzip";
    L18:
        r0.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, r6);
        r0.setInstanceFollowRedirects(r16);
        if (r10 == null) goto L21;
        boolean r7 = true;
    L22:
        r0.setDoOutput(r7);
        r0.setRequestMethod(DataSpec.getStringForHttpMethod(r9));
        if (r10 == null) goto L25;
        r0.setFixedLengthStreamingMode(r10.length);
        r0.connect();
        OutputStream r12 = r0.getOutputStream();
        r12.write(r10);
        r12.close();
    L26:
        return r0;
    L25:
        r0.connect();
        goto L26
    L21:
        r7 = false;
        goto L22
    L17:
        r6 = "identity";
        goto L18
    }

    private int readInternal(byte[] r7, int r8, int r9) throws IOException {
        if (r9 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesToRead == (-1)) goto L11;
        long r2 = this.bytesToRead - this.bytesRead;
        if (r2 != 0) goto L10;
        return -1;
    L10:
        r9 = (int) Math.min(r9, r2);
    L11:
        int r0 = ((InputStream) Util.castNonNull(this.inputStream)).read(r7, r8, r9);
        if (r0 != (-1)) goto L14;
        return -1;
    L14:
        this.bytesRead += (long) r0;
        bytesTransferred(r0);
        return r0;
    }

    private void skipFully(long r8, DataSpec r10) throws IOException {
        if (r8 != 0) goto L5;
        return;
    L5:
        byte[] r2 = new byte[4096];
    L7:
        if (r8 <= 0) goto L17;
        int r4 = ((InputStream) Util.castNonNull(this.inputStream)).read(r2, 0, (int) Math.min(r8, r2.length));
        if (Thread.currentThread().isInterrupted() == true) goto L16;
        if (r4 == (-1)) goto L14;
        r8 = r8 - ((long) r4);
        bytesTransferred(r4);
        goto L7
    L14:
        throw new HttpDataSource.HttpDataSourceException(r10, 2008, 1);
    L16:
        throw new HttpDataSource.HttpDataSourceException(new InterruptedIOException(), r10, 2000, 1);
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public void clearAllRequestProperties() {
        this.requestProperties.clear();
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public void clearRequestProperty(String r2) {
        Assertions.checkNotNull(r2);
        this.requestProperties.remove(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws HttpDataSource.HttpDataSourceException {
        InputStream r2 = this.inputStream;     // Catch: Throwable -> L15
        if (r2 != null) goto L22;
    L10:
        this.inputStream = null;
        closeConnectionQuietly();
        if (this.transferStarted == false) goto L13;
        this.transferStarted = false;
        transferEnded();
    L13:
        this.connection = null;
        this.dataSpec = null;
        return;
    L22:
        r2.close();     // Catch: IOException -> L7 Throwable -> L15
    L7:
        e = move-exception;
        throw new HttpDataSource.HttpDataSourceException(e, (DataSpec) Util.castNonNull(this.dataSpec), 2000, 3);     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        this.inputStream = null;
        closeConnectionQuietly();
        if (this.transferStarted == false) goto L19;
        this.transferStarted = false;
        transferEnded();
    L19:
        this.connection = null;
        this.dataSpec = null;
        throw th;
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public int getResponseCode() {
        if (this.connection != null) goto L5;
    L8:
        return -1;
    L5:
        if (this.responseCode <= 0) goto L8;
        return this.responseCode;
    }

    @Override // androidx.media3.datasource.BaseDataSource, androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        if (this.connection != null) goto L7;
        return ImmutableMap.m231of();
    L7:
        return new NullFilteringHeadersMap(this.connection.getHeaderFields());
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        if (this.connection == null) goto L7;
        return Uri.parse(this.connection.getURL().toString());
    L7:
        if (this.dataSpec != null) goto L9;
        return null;
    L9:
        return this.dataSpec.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r15) throws HttpDataSource.HttpDataSourceException {
        this.dataSpec = r15;
        long r0 = 0;
        this.bytesRead = 0;
        this.bytesToRead = 0;
        transferInitializing(r15);
        this.connection = makeConnection(r15);     // Catch: IOException -> L70
        HttpURLConnection r3 = this.connection;     // Catch: IOException -> L70
        this.responseCode = r3.getResponseCode();     // Catch: IOException -> L70
        String r4 = r3.getResponseMessage();     // Catch: IOException -> L70
        long r8 = -1;
        if (this.responseCode >= 200) goto L7;
    L49:
        Map<String, List<String>> r9 = r3.getHeaderFields();
        if (this.responseCode == 416) goto L52;
    L57:
        InputStream r1 = r3.getErrorStream();
        if (r1 == null) goto L60;
        byte[] r2 = ByteStreams.toByteArray(r1);     // Catch: IOException -> L62
    L61:
        byte[] r11 = r2;
    L64:
        closeConnectionQuietly();
        if (this.responseCode != 416) goto L67;
        DataSourceException r10 = new DataSourceException(2008);
    L69:
        throw new HttpDataSource.InvalidResponseCodeException(this.responseCode, r4, r10, r9, r15, r11);
    L67:
        r10 = null;
        goto L69
    L60:
        r2 = Util.EMPTY_BYTE_ARRAY;     // Catch: IOException -> L62
    L63:
        r11 = Util.EMPTY_BYTE_ARRAY;
        goto L64
    L52:
        if (r15.position != HttpUtil.getDocumentSize(r3.getHeaderField(HttpHeaders.CONTENT_RANGE))) goto L57;
        this.transferStarted = true;
        transferStarted(r15);
        if (r15.length != (-1)) goto L56;
        return 0;
    L56:
        return r15.length;
    L7:
        if (this.responseCode > 299) goto L49;
        String r5 = r3.getContentType();
        if (this.contentTypePredicate == null) goto L17;
        if (this.contentTypePredicate.apply(r5) == true) goto L17;
        closeConnectionQuietly();
        throw new HttpDataSource.InvalidContentTypeException(r5, r15);
    L17:
        if (this.responseCode == 200) goto L19;
    L21:
        long r12 = r0;
        boolean r6 = isCompressed(r3);
        if (r6 == false) goto L24;
        this.bytesToRead = r15.length;
    L73:
        this.inputStream = r3.getInputStream();     // Catch: IOException -> L46
        if (r6 == false) goto L35;
        this.inputStream = new GZIPInputStream(this.inputStream);     // Catch: IOException -> L46
    L35:
        this.transferStarted = true;
        transferStarted(r15);
        skipFully(r12, r15);     // Catch: IOException -> L39
        return this.bytesToRead;
    L39:
        e = move-exception;
        closeConnectionQuietly();
        if ((e instanceof HttpDataSource.HttpDataSourceException) == false) goto L45;
        throw ((HttpDataSource.HttpDataSourceException) e);
    L45:
        throw new HttpDataSource.HttpDataSourceException(e, r15, 2000, 1);
    L46:
        e = move-exception;
        closeConnectionQuietly();
        throw new HttpDataSource.HttpDataSourceException(e, r15, 2000, 1);
    L24:
        if (r15.length == (-1)) goto L26;
        this.bytesToRead = r15.length;
        goto L73
    L26:
        long r7 = HttpUtil.getContentLength(r3.getHeaderField(HttpHeaders.CONTENT_LENGTH), r3.getHeaderField(HttpHeaders.CONTENT_RANGE));
        if (r7 == (-1)) goto L29;
        r8 = r7 - r12;
    L29:
        this.bytesToRead = r8;
        goto L73
    L19:
        if (r15.position == 0) goto L21;
        r0 = r15.position;
    L70:
        e = move-exception;
        closeConnectionQuietly();
        throw HttpDataSource.HttpDataSourceException.createForIOException(e, r15, 1);
    }

    HttpURLConnection openConnection(URL r2) throws IOException {
        return (HttpURLConnection) r2.openConnection();
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r4, int r5, int r6) throws HttpDataSource.HttpDataSourceException {
        return readInternal(r4, r5, r6);
    L4:
        e = move-exception;
        throw HttpDataSource.HttpDataSourceException.createForIOException(e, (DataSpec) Util.castNonNull(this.dataSpec), 2);
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public void setRequestProperty(String r2, String r3) {
        Assertions.checkNotNull(r2);
        Assertions.checkNotNull(r3);
        this.requestProperties.set(r2, r3);
    }
}
