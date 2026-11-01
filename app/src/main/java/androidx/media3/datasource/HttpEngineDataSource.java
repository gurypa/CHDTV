package androidx.media3.datasource;

import android.net.Uri;
import android.net.http.HttpEngine;
import android.net.http.HttpException;
import android.net.http.NetworkException;
import android.net.http.UrlRequest;
import android.net.http.UrlResponseInfo;
import android.text.TextUtils;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.Util;
import com.google.common.base.Ascii;
import com.google.common.base.Predicate;
import com.google.common.net.HttpHeaders;
import com.google.common.primitives.Longs;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class HttpEngineDataSource extends BaseDataSource implements HttpDataSource {
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 8000;
    public static final int DEFAULT_READ_TIMEOUT_MILLIS = 8000;
    private static final int READ_BUFFER_SIZE_BYTES = 32768;
    private long bytesRemaining;
    private final Clock clock;
    private final int connectTimeoutMs;
    private Predicate<String> contentTypePredicate;
    private volatile long currentConnectTimeoutMs;
    private DataSpec currentDataSpec;
    private UrlRequestWrapper currentUrlRequestWrapper;
    private final HttpDataSource.RequestProperties defaultRequestProperties;
    private IOException exception;
    private final Executor executor;
    private boolean finished;
    private final boolean handleSetCookieRequests;
    private final HttpEngine httpEngine;
    private final boolean keepPostFor302Redirects;
    private final ConditionVariable operation;
    private ByteBuffer readBuffer;
    private final int readTimeoutMs;
    private final int requestPriority;
    private final HttpDataSource.RequestProperties requestProperties;
    private final boolean resetTimeoutOnRedirects;
    private UrlResponseInfo responseInfo;
    private boolean transferStarted;
    private final String userAgent;

    /* JADX INFO: renamed from: androidx.media3.datasource.HttpEngineDataSource$1 */
    static /* synthetic */ class C02141 {
    }

    public static final class Factory implements HttpDataSource.Factory {
        private int connectTimeoutMs;
        private Predicate<String> contentTypePredicate;
        private final HttpDataSource.RequestProperties defaultRequestProperties;
        private final Executor executor;
        private boolean handleSetCookieRequests;
        private final HttpEngine httpEngine;
        private boolean keepPostFor302Redirects;
        private int readTimeoutMs;
        private int requestPriority;
        private boolean resetTimeoutOnRedirects;
        private TransferListener transferListener;
        private String userAgent;

        public Factory(HttpEngine r2, Executor r3) {
            this.httpEngine = (HttpEngine) Assertions.checkNotNull(r2);
            this.executor = r3;
            this.defaultRequestProperties = new HttpDataSource.RequestProperties();
            this.requestPriority = 3;
            this.connectTimeoutMs = 8000;
            this.readTimeoutMs = 8000;
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory, androidx.media3.datasource.DataSource.Factory
        public HttpDataSource createDataSource() {
            HttpEngineDataSource r0 = new HttpEngineDataSource(this.httpEngine, this.executor, this.requestPriority, this.connectTimeoutMs, this.readTimeoutMs, this.resetTimeoutOnRedirects, this.handleSetCookieRequests, this.userAgent, this.defaultRequestProperties, this.contentTypePredicate, this.keepPostFor302Redirects);
            if (this.transferListener == null) goto L5;
            r0.addTransferListener(this.transferListener);
        L5:
            return r0;
        }

        public Factory setConnectionTimeoutMs(int r1) {
            this.connectTimeoutMs = r1;
            return this;
        }

        public Factory setContentTypePredicate(Predicate<String> r1) {
            this.contentTypePredicate = r1;
            return this;
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory
        public /* bridge */ /* synthetic */ HttpDataSource.Factory setDefaultRequestProperties(Map r1) {
            return setDefaultRequestProperties(r1);
        }

        @Override // androidx.media3.datasource.HttpDataSource.Factory
        public final Factory setDefaultRequestProperties(Map<String, String> r2) {
            this.defaultRequestProperties.clearAndSet(r2);
            return this;
        }

        public Factory setHandleSetCookieRequests(boolean r1) {
            this.handleSetCookieRequests = r1;
            return this;
        }

        public Factory setKeepPostFor302Redirects(boolean r1) {
            this.keepPostFor302Redirects = r1;
            return this;
        }

        public Factory setReadTimeoutMs(int r1) {
            this.readTimeoutMs = r1;
            return this;
        }

        public Factory setRequestPriority(int r1) {
            this.requestPriority = r1;
            return this;
        }

        public Factory setResetTimeoutOnRedirects(boolean r1) {
            this.resetTimeoutOnRedirects = r1;
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

    public static final class OpenException extends HttpDataSource.HttpDataSourceException {
        public final int httpEngineConnectionStatus;

        public OpenException(DataSpec r2, int r3, int r4) {
            super(r2, r3, 1);
            this.httpEngineConnectionStatus = r4;
        }

        public OpenException(IOException r2, DataSpec r3, int r4, int r5) {
            super(r2, r3, r4, 1);
            this.httpEngineConnectionStatus = r5;
        }

        public OpenException(String r2, DataSpec r3, int r4, int r5) {
            super(r2, r3, r4, 1);
            this.httpEngineConnectionStatus = r5;
        }
    }

    private final class UrlRequestCallback implements UrlRequest.Callback {
        private volatile boolean isClosed;
        final /* synthetic */ HttpEngineDataSource this$0;

        private UrlRequestCallback(HttpEngineDataSource r1) {
            this.this$0 = r1;
            this.isClosed = false;
        }

        /* synthetic */ UrlRequestCallback(HttpEngineDataSource r1, C02141 r2) {
            this(r1);
        }

        public void close() {
            this.isClosed = true;
        }

        @Override // android.net.http.UrlRequest.Callback
        public synchronized void onCanceled(UrlRequest r1, UrlResponseInfo r2) {
            monitor-enter(this);
            monitor-exit(this);
        }

        @Override // android.net.http.UrlRequest.Callback
        public synchronized void onFailed(UrlRequest r3, UrlResponseInfo r4, HttpException r5) {
            monitor-enter(this);
        L16:
            th = move-exception;
            throw th;
        L4:
            if (this.isClosed == false) goto L8;
            monitor-exit(this);
            return;
        L8:
            if ((r5 instanceof NetworkException) == true) goto L10;
        L12:
            HttpEngineDataSource.access$202(this.this$0, r5);     // Catch: Throwable -> L16
        L13:
            HttpEngineDataSource.access$300(this.this$0).open();     // Catch: Throwable -> L16
            monitor-exit(this);
            return;
        L10:
            if (((NetworkException) r5).getErrorCode() != 1) goto L12;
            HttpEngineDataSource.access$202(this.this$0, new UnknownHostException());     // Catch: Throwable -> L16
            goto L13
        }

        @Override // android.net.http.UrlRequest.Callback
        public synchronized void onReadCompleted(UrlRequest r2, UrlResponseInfo r3, ByteBuffer r4) {
            monitor-enter(this);
        L10:
            th = move-exception;
            throw th;
        L4:
            if (this.isClosed == false) goto L7;
            monitor-exit(this);
            return;
        L7:
            HttpEngineDataSource.access$300(this.this$0).open();     // Catch: Throwable -> L10
            monitor-exit(this);
        }

        @Override // android.net.http.UrlRequest.Callback
        public synchronized void onRedirectReceived(UrlRequest r9, UrlResponseInfo r10, String r11) {
            monitor-enter(this);
        L64:
            th = move-exception;
            throw th;
        L4:
            if (this.isClosed == false) goto L7;
            monitor-exit(this);
            return;
        L7:
            DataSpec r0 = (DataSpec) Assertions.checkNotNull(HttpEngineDataSource.access$100(this.this$0));     // Catch: Throwable -> L64
            int r2 = r10.getHttpStatusCode();     // Catch: Throwable -> L64
            if (r0.httpMethod != 2) goto L17;
            if (r2 != 307) goto L12;
        L13:
            HttpEngineDataSource.access$202(this.this$0, new HttpDataSource.InvalidResponseCodeException(r2, r10.getHttpStatusText(), null, r10.getHeaders().getAsMap(), r0, Util.EMPTY_BYTE_ARRAY));     // Catch: Throwable -> L64
            HttpEngineDataSource.access$300(this.this$0).open();     // Catch: Throwable -> L64
            monitor-exit(this);
            return;
        L12:
            if (r2 == 308) goto L13;
        L17:
            if (HttpEngineDataSource.access$400(this.this$0) == false) goto L20;
            HttpEngineDataSource.access$500(this.this$0);     // Catch: Throwable -> L64
        L20:
            if (HttpEngineDataSource.access$600(this.this$0) == true) goto L22;
        L26:
            boolean r1 = false;
        L27:
            boolean r4 = r1;
            if (r4 == false) goto L30;
        L35:
            String r3 = HttpEngineDataSource.access$800(r10.getHeaders().getAsMap().get(HttpHeaders.SET_COOKIE));     // Catch: Throwable -> L64
            if (r4 == false) goto L38;
        L42:
            r9.cancel();     // Catch: Throwable -> L64
            if (r4 == false) goto L45;
        L47:
            DataSpec r5 = r0.withUri(Uri.parse(r11));     // Catch: Throwable -> L64
        L49:
            if (TextUtils.isEmpty(r3) == true) goto L52;
            HashMap r6 = new HashMap();     // Catch: Throwable -> L64
            r6.putAll(r0.httpRequestHeaders);     // Catch: Throwable -> L64
            r6.put(HttpHeaders.COOKIE, r3);     // Catch: Throwable -> L64
            DataSpec r7 = r5.buildUpon().setHttpRequestHeaders(r6).build();     // Catch: Throwable -> L64
        L68:
            UrlRequestWrapper r8 = HttpEngineDataSource.access$900(this.this$0, r7);     // Catch: IOException -> L60 Throwable -> L64
        L55:
            if (HttpEngineDataSource.access$1000(this.this$0) == null) goto L57;
            HttpEngineDataSource.access$1000(this.this$0).close();     // Catch: Throwable -> L64
        L57:
            HttpEngineDataSource.access$1002(this.this$0, r8);     // Catch: Throwable -> L64
            HttpEngineDataSource.access$1000(this.this$0).start();     // Catch: Throwable -> L64
            monitor-exit(this);
            return;
        L60:
            e = move-exception;
            HttpEngineDataSource.access$202(this.this$0, e);     // Catch: Throwable -> L64
            return;
        L52:
            r7 = r5;
            goto L68
        L45:
            if (r0.httpMethod != 2) goto L47;
            r5 = r0.buildUpon().setUri(r11).setHttpMethod(1).setHttpBody(null).build();     // Catch: Throwable -> L64
            goto L49
        L38:
            if (TextUtils.isEmpty(r3) == false) goto L42;
            r9.followRedirect();     // Catch: Throwable -> L64
            monitor-exit(this);
            return;
        L30:
            if (HttpEngineDataSource.access$700(this.this$0) == true) goto L35;
            r9.followRedirect();     // Catch: Throwable -> L64
            monitor-exit(this);
            return;
        L22:
            if (r0.httpMethod != 2) goto L26;
            if (r2 != 302) goto L26;
            r1 = true;
            goto L27
        }

        @Override // android.net.http.UrlRequest.Callback
        public synchronized void onResponseStarted(UrlRequest r2, UrlResponseInfo r3) {
            monitor-enter(this);
        L10:
            th = move-exception;
            throw th;
        L4:
            if (this.isClosed == false) goto L7;
            monitor-exit(this);
            return;
        L7:
            HttpEngineDataSource.access$1102(this.this$0, r3);     // Catch: Throwable -> L10
            HttpEngineDataSource.access$300(this.this$0).open();     // Catch: Throwable -> L10
            monitor-exit(this);
        }

        @Override // android.net.http.UrlRequest.Callback
        public synchronized void onSucceeded(UrlRequest r3, UrlResponseInfo r4) {
            monitor-enter(this);
        L10:
            th = move-exception;
            throw th;
        L4:
            if (this.isClosed == false) goto L7;
            monitor-exit(this);
            return;
        L7:
            HttpEngineDataSource.access$1202(this.this$0, true);     // Catch: Throwable -> L10
            HttpEngineDataSource.access$300(this.this$0).open();     // Catch: Throwable -> L10
            monitor-exit(this);
        }
    }

    private static final class UrlRequestWrapper {
        private final UrlRequest urlRequest;
        private final UrlRequestCallback urlRequestCallback;

        UrlRequestWrapper(UrlRequest r1, UrlRequestCallback r2) {
            this.urlRequest = r1;
            this.urlRequestCallback = r2;
        }

        public void close() {
            this.urlRequestCallback.close();
            this.urlRequest.cancel();
        }

        public int getStatus() throws InterruptedException {
            final ConditionVariable r0 = new ConditionVariable();
            final int[] r1 = new int[1];
            this.urlRequest.getStatus(new C02151(this, r1, r0));
            r0.block();
            return r1[0];
        }

        public UrlRequest.Callback getUrlRequestCallback() {
            return this.urlRequestCallback;
        }

        public void read(ByteBuffer r2) {
            this.urlRequest.read(r2);
        }

        public void start() {
            this.urlRequest.start();
        }
    }

    HttpEngineDataSource(HttpEngine r2, Executor r3, int r4, int r5, int r6, boolean r7, boolean r8, String r9, HttpDataSource.RequestProperties r10, Predicate<String> r11, boolean r12) {
        super(true);
        this.httpEngine = (HttpEngine) Assertions.checkNotNull(r2);
        this.executor = (Executor) Assertions.checkNotNull(r3);
        this.requestPriority = r4;
        this.connectTimeoutMs = r5;
        this.readTimeoutMs = r6;
        this.resetTimeoutOnRedirects = r7;
        this.handleSetCookieRequests = r8;
        this.userAgent = r9;
        this.defaultRequestProperties = r10;
        this.contentTypePredicate = r11;
        this.keepPostFor302Redirects = r12;
        this.clock = Clock.DEFAULT;
        this.requestProperties = new HttpDataSource.RequestProperties();
        this.operation = new ConditionVariable();
    }

    static /* synthetic */ DataSpec access$100(HttpEngineDataSource r1) {
        return r1.currentDataSpec;
    }

    static /* synthetic */ UrlRequestWrapper access$1000(HttpEngineDataSource r1) {
        return r1.currentUrlRequestWrapper;
    }

    static /* synthetic */ UrlRequestWrapper access$1002(HttpEngineDataSource r0, UrlRequestWrapper r1) {
        r0.currentUrlRequestWrapper = r1;
        return r1;
    }

    static /* synthetic */ UrlResponseInfo access$1102(HttpEngineDataSource r0, UrlResponseInfo r1) {
        r0.responseInfo = r1;
        return r1;
    }

    static /* synthetic */ boolean access$1202(HttpEngineDataSource r0, boolean r1) {
        r0.finished = r1;
        return r1;
    }

    static /* synthetic */ IOException access$202(HttpEngineDataSource r0, IOException r1) {
        r0.exception = r1;
        return r1;
    }

    static /* synthetic */ ConditionVariable access$300(HttpEngineDataSource r1) {
        return r1.operation;
    }

    static /* synthetic */ boolean access$400(HttpEngineDataSource r1) {
        return r1.resetTimeoutOnRedirects;
    }

    static /* synthetic */ void access$500(HttpEngineDataSource r0) {
        r0.resetConnectTimeout();
    }

    static /* synthetic */ boolean access$600(HttpEngineDataSource r1) {
        return r1.keepPostFor302Redirects;
    }

    static /* synthetic */ boolean access$700(HttpEngineDataSource r1) {
        return r1.handleSetCookieRequests;
    }

    static /* synthetic */ String access$800(List r1) {
        return parseCookies(r1);
    }

    static /* synthetic */ UrlRequestWrapper access$900(HttpEngineDataSource r1, DataSpec r2) throws IOException {
        return r1.buildRequestWrapper(r2);
    }

    private boolean blockUntilConnectTimeout() throws InterruptedException {
        long r0 = this.clock.elapsedRealtime();
        boolean r2 = false;
    L3:
        if (r2 == true) goto L7;
        if (r0 >= this.currentConnectTimeoutMs) goto L7;
        r2 = this.operation.block((this.currentConnectTimeoutMs - r0) + 5);
        r0 = this.clock.elapsedRealtime();
    L7:
        return r2;
    }

    private UrlRequest.Builder buildRequestBuilder(DataSpec r7, UrlRequest.Callback r8) throws IOException {
        UrlRequest.Builder r0 = this.httpEngine.newUrlRequestBuilder(r7.uri.toString(), this.executor, r8).setPriority(this.requestPriority).setDirectExecutorAllowed(true);
        HashMap r1 = new HashMap();
        if (this.defaultRequestProperties == null) goto L5;
        r1.putAll(this.defaultRequestProperties.getSnapshot());
    L5:
        r1.putAll(this.requestProperties.getSnapshot());
        r1.putAll(r7.httpRequestHeaders);
        Iterator r2 = r1.entrySet().iterator();
    L7:
        if (r2.hasNext() == false) goto L10;
        Map.Entry r3 = (Map.Entry) r2.next();
        r0.addHeader((String) r3.getKey(), (String) r3.getValue());
        goto L7
    L10:
        if (r7.httpBody != null) goto L12;
    L16:
        String r4 = HttpUtil.buildRangeRequestHeader(r7.position, r7.length);
        if (r4 == null) goto L20;
        r0.addHeader(HttpHeaders.RANGE, r4);
    L20:
        if (this.userAgent == null) goto L22;
        r0.addHeader(HttpHeaders.USER_AGENT, this.userAgent);
    L22:
        r0.setHttpMethod(r7.getHttpMethodString());
        if (r7.httpBody == null) goto L25;
        r0.setUploadDataProvider(new ByteArrayUploadDataProvider(r7.httpBody), this.executor);
    L25:
        return r0;
    L12:
        if (r1.containsKey(HttpHeaders.CONTENT_TYPE) == true) goto L16;
        throw new OpenException("HTTP request with non-empty body must set Content-Type", r7, 1004, 0);
    }

    private UrlRequestWrapper buildRequestWrapper(DataSpec r4) throws IOException {
        UrlRequestCallback r0 = new UrlRequestCallback(this, null);
        return new UrlRequestWrapper(buildRequestBuilder(r4, r0).build(), r0);
    }

    private static int copyByteBuffer(ByteBuffer r3, ByteBuffer r4) {
        int r0 = Math.min(r3.remaining(), r4.remaining());
        int r1 = r3.limit();
        r3.limit(r3.position() + r0);
        r4.put(r3);
        r3.limit(r1);
        return r0;
    }

    private static String getFirstHeader(Map<String, List<String>> r2, String r3) {
        List<String> r0 = r2.get(r3);
        if (r0 != null) goto L5;
    L7:
        return null;
    L5:
        if (r0.isEmpty() == true) goto L7;
        return r0.get(0);
    }

    private ByteBuffer getOrCreateReadBuffer() {
        if (this.readBuffer != null) goto L6;
        this.readBuffer = ByteBuffer.allocateDirect(32768);
        this.readBuffer.limit(0);
    L6:
        return this.readBuffer;
    }

    private static boolean isCompressed(UrlResponseInfo r4) {
        Iterator<Map.Entry<String, String>> r0 = r4.getHeaders().getAsList().iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        Map.Entry<String, String> r1 = r0.next();
        if (r1.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_ENCODING) == false) goto L4;
        return !r1.getValue().equalsIgnoreCase("identity");
    L10:
        return false;
    }

    private static String parseCookies(List<String> r1) {
        if (r1 != null) goto L4;
        return null;
    L4:
        if (r1.isEmpty() == false) goto L7;
        return null;
    L7:
        return TextUtils.join(";", r1);
    }

    private void readInternal(ByteBuffer r6, DataSpec r7) throws HttpDataSource.HttpDataSourceException {
        ((UrlRequestWrapper) Util.castNonNull(this.currentUrlRequestWrapper)).read(r6);
    L8:
        e = move-exception;
        if (r6 != this.readBuffer) goto L12;
        this.readBuffer = null;
    L12:
        this.exception = new HttpDataSource.HttpDataSourceException(e, r7, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT, 2);
    L20:
        if (this.exception != null) goto L22;
        return;
    L22:
        if ((this.exception instanceof HttpDataSource.HttpDataSourceException) == false) goto L26;
        throw ((HttpDataSource.HttpDataSourceException) this.exception);
    L26:
        throw HttpDataSource.HttpDataSourceException.createForIOException(this.exception, r7, 2);
    L4:
        if (this.operation.block(this.readTimeoutMs) == true) goto L20;
        throw new SocketTimeoutException();     // Catch: SocketTimeoutException -> L8 InterruptedException -> L13
    L15:
        if (r6 != this.readBuffer) goto L17;
        this.readBuffer = null;
    L17:
        Thread.currentThread().interrupt();
        this.exception = new InterruptedIOException();
        goto L20
    }

    private byte[] readResponseBody() throws IOException {
        byte[] r0 = Util.EMPTY_BYTE_ARRAY;
        ByteBuffer r1 = getOrCreateReadBuffer();
    L4:
        if (this.finished == true) goto L8;
        this.operation.close();
        r1.clear();
        readInternal(r1, (DataSpec) Util.castNonNull(this.currentDataSpec));
        r1.flip();
        if (r1.remaining() <= 0) goto L4;
        int r2 = r0.length;
        r0 = Arrays.copyOf(r0, r0.length + r1.remaining());
        r1.get(r0, r2, r1.remaining());
        goto L4
    L8:
        return r0;
    }

    private void resetConnectTimeout() {
        this.currentConnectTimeoutMs = this.clock.elapsedRealtime() + ((long) this.connectTimeoutMs);
    }

    private void skipFully(long r8, DataSpec r10) throws HttpDataSource.HttpDataSourceException {
        if (r8 != 0) goto L5;
        return;
    L5:
        ByteBuffer r2 = getOrCreateReadBuffer();
    L7:
        if (r8 <= 0) goto L30;
        this.operation.close();     // Catch: IOException -> L18
        r2.clear();     // Catch: IOException -> L18
        readInternal(r2, r10);     // Catch: IOException -> L18
        if (Thread.currentThread().isInterrupted() == true) goto L17;
        if (this.finished == true) goto L15;
        r2.flip();     // Catch: IOException -> L18
        Assertions.checkState(r2.hasRemaining());     // Catch: IOException -> L18
        int r4 = (int) Math.min(r2.remaining(), r8);     // Catch: IOException -> L18
        r2.position(r2.position() + r4);     // Catch: IOException -> L18
        r8 = r8 - ((long) r4);     // Catch: IOException -> L18
        goto L7
    L15:
        throw new OpenException(r10, 2008, 14);     // Catch: IOException -> L18
    L17:
        throw new InterruptedIOException();     // Catch: IOException -> L18
    L18:
        e = move-exception;
        if ((e instanceof HttpDataSource.HttpDataSourceException) == true) goto L28;
        if ((e instanceof SocketTimeoutException) == false) goto L24;
        int r5 = PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT;
    L26:
        throw new OpenException(e, r10, r5, 14);
    L24:
        r5 = PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED;
        goto L26
    L28:
        throw ((HttpDataSource.HttpDataSourceException) e);
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public void clearAllRequestProperties() {
        this.requestProperties.clear();
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public void clearRequestProperty(String r2) {
        this.requestProperties.remove(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public synchronized void close() {
        monitor-enter(this);
    L14:
        th = move-exception;
        throw th;
    L4:
        if (this.currentUrlRequestWrapper == null) goto L7;
        this.currentUrlRequestWrapper.close();     // Catch: Throwable -> L14
        this.currentUrlRequestWrapper = null;     // Catch: Throwable -> L14
    L7:
        if (this.readBuffer == null) goto L9;
        this.readBuffer.limit(0);     // Catch: Throwable -> L14
    L9:
        this.currentDataSpec = null;     // Catch: Throwable -> L14
        this.responseInfo = null;     // Catch: Throwable -> L14
        this.exception = null;     // Catch: Throwable -> L14
        this.finished = false;     // Catch: Throwable -> L14
        if (this.transferStarted == false) goto L12;
        this.transferStarted = false;     // Catch: Throwable -> L14
        transferEnded();     // Catch: Throwable -> L14
    L12:
        monitor-exit(this);
    }

    UrlRequest.Callback getCurrentUrlRequestCallback() {
        if (this.currentUrlRequestWrapper != null) goto L6;
        return null;
    L6:
        return this.currentUrlRequestWrapper.getUrlRequestCallback();
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public int getResponseCode() {
        if (this.responseInfo != null) goto L5;
    L8:
        return -1;
    L5:
        if (this.responseInfo.getHttpStatusCode() <= 0) goto L8;
        return this.responseInfo.getHttpStatusCode();
    }

    @Override // androidx.media3.datasource.BaseDataSource, androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        if (this.responseInfo != null) goto L6;
        return Collections.emptyMap();
    L6:
        return this.responseInfo.getHeaders().getAsMap();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        if (this.responseInfo == null) goto L7;
        return Uri.parse(this.responseInfo.getUrl());
    L7:
        if (this.currentDataSpec != null) goto L9;
        return null;
    L9:
        return this.currentDataSpec.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r20) throws HttpDataSource.HttpDataSourceException {
        Assertions.checkNotNull(r20);
        Assertions.checkState(!this.transferStarted);
        this.operation.close();
        resetConnectTimeout();
        this.currentDataSpec = r20;
        UrlRequestWrapper r0 = buildRequestWrapper(r20);     // Catch: IOException -> L73
        this.currentUrlRequestWrapper = r0;     // Catch: IOException -> L73
        r0.start();
        transferInitializing(r20);
        boolean r1 = blockUntilConnectTimeout();     // Catch: InterruptedException -> L70
        IOException r3 = this.exception;     // Catch: InterruptedException -> L70
        if (r3 == null) goto L15;
        String r2 = r3.getMessage();     // Catch: InterruptedException -> L70
        if (r2 == null) goto L14;
        if (Ascii.toLowerCase(r2).contains("err_cleartext_not_permitted") == false) goto L14;
        throw new HttpDataSource.CleartextNotPermittedException(r3, r20);     // Catch: InterruptedException -> L70
    L14:
        throw new OpenException(r3, r20, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, r0.getStatus());     // Catch: InterruptedException -> L70
    L15:
        if (r1 == false) goto L69;
        UrlResponseInfo r10 = (UrlResponseInfo) Assertions.checkNotNull(this.responseInfo);
        int r4 = r10.getHttpStatusCode();
        Map<String, List<String>> r6 = r10.getHeaders().getAsMap();
        if (r4 >= 200) goto L19;
        long r15 = 0;
        long r17 = -1;
    L51:
        if (r4 == 416) goto L53;
    L81:
        byte[] r8 = readResponseBody();     // Catch: IOException -> L61
    L63:
        if (r4 != 416) goto L65;
        DataSourceException r5 = new DataSourceException(2008);
    L67:
        throw new HttpDataSource.InvalidResponseCodeException(r4, r10.getHttpStatusText(), r5, r6, r20, r8);
    L65:
        r5 = null;
    L62:
        r8 = Util.EMPTY_BYTE_ARRAY;
        goto L63
    L53:
        if (r20.position != HttpUtil.getDocumentSize(getFirstHeader(r6, HttpHeaders.CONTENT_RANGE))) goto L81;
        this.transferStarted = true;
        transferStarted(r20);
        if (r20.length == r17) goto L58;
        return r20.length;
    L58:
        return r15;
    L19:
        if (r4 <= 299) goto L21;
        r15 = 0;
        r17 = -1;
        goto L51
    L21:
        Predicate<String> r7 = this.contentTypePredicate;
        if (r7 == null) goto L30;
        String r9 = getFirstHeader(r6, HttpHeaders.CONTENT_TYPE);
        if (r9 == null) goto L30;
        if (r7.apply(r9) == true) goto L30;
        throw new HttpDataSource.InvalidContentTypeException(r9, r20);
    L30:
        if (r4 != 200) goto L34;
        long r16 = 0;
        if (r20.position == 0) goto L35;
        long r11 = r20.position;
    L37:
        if (isCompressed(r10) == false) goto L39;
        this.bytesRemaining = r20.length;
    L47:
        this.transferStarted = true;
        transferStarted(r20);
        skipFully(r11, r20);
        return this.bytesRemaining;
    L39:
        if (r20.length == (-1)) goto L41;
        this.bytesRemaining = r20.length;
        goto L47
    L41:
        long r13 = HttpUtil.getContentLength(getFirstHeader(r6, HttpHeaders.CONTENT_LENGTH), getFirstHeader(r6, HttpHeaders.CONTENT_RANGE));
        if (r13 == (-1)) goto L44;
        long r12 = r13 - r11;
    L45:
        this.bytesRemaining = r12;
        goto L47
    L44:
        r12 = -1;
    L35:
        r11 = r16;
        goto L37
    L34:
        r16 = 0;
        goto L35
    L69:
        throw new OpenException(new SocketTimeoutException(), r20, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT, r0.getStatus());     // Catch: InterruptedException -> L70
    L71:
        Thread.currentThread().interrupt();
        throw new OpenException(new InterruptedIOException(), r20, 1004, -1);
    L73:
        e = move-exception;
        if ((e instanceof HttpDataSource.HttpDataSourceException) == false) goto L79;
        throw ((HttpDataSource.HttpDataSourceException) e);
    L79:
        throw new OpenException(e, r20, 2000, 0);
    }

    public int read(ByteBuffer r9) throws HttpDataSource.HttpDataSourceException {
        Assertions.checkState(this.transferStarted);
        if (r9.isDirect() == false) goto L32;
        boolean r1 = false;
        if (r9.hasRemaining() == true) goto L8;
        return 0;
    L8:
        if (this.bytesRemaining != 0) goto L10;
        return -1;
    L10:
        int r0 = r9.remaining();
        if (this.readBuffer == null) goto L19;
        int r3 = copyByteBuffer(this.readBuffer, r9);
        if (r3 == 0) goto L19;
        if (this.bytesRemaining == (-1)) goto L17;
        this.bytesRemaining -= (long) r3;
    L17:
        bytesTransferred(r3);
        return r3;
    L19:
        this.operation.close();
        readInternal(r9, (DataSpec) Util.castNonNull(this.currentDataSpec));
        if (this.finished == false) goto L24;
        this.bytesRemaining = 0;
        return -1;
    L24:
        if (r0 <= r9.remaining()) goto L26;
        r1 = true;
    L26:
        Assertions.checkState(r1);
        int r2 = r0 - r9.remaining();
        if (this.bytesRemaining == (-1)) goto L29;
        this.bytesRemaining -= (long) r2;
    L29:
        bytesTransferred(r2);
        return r2;
    L32:
        throw new IllegalArgumentException("Passed buffer is not a direct ByteBuffer");
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r12, int r13, int r14) throws HttpDataSource.HttpDataSourceException {
        Assertions.checkState(this.transferStarted);
        if (r14 != 0) goto L6;
        return 0;
    L6:
        if (this.bytesRemaining != 0) goto L8;
        return -1;
    L8:
        ByteBuffer r1 = getOrCreateReadBuffer();
        if (r1.hasRemaining() == true) goto L16;
        this.operation.close();
        r1.clear();
        readInternal(r1, (DataSpec) Util.castNonNull(this.currentDataSpec));
        if (this.finished == false) goto L14;
        this.bytesRemaining = 0;
        return -1;
    L14:
        r1.flip();
        Assertions.checkState(r1.hasRemaining());
    L16:
        if (this.bytesRemaining == (-1)) goto L18;
        long r2 = this.bytesRemaining;
    L19:
        int r0 = (int) Longs.min(new long[]{r2, r1.remaining(), r14});
        r1.get(r12, r13, r0);
        if (this.bytesRemaining == (-1)) goto L22;
        this.bytesRemaining -= (long) r0;
    L22:
        bytesTransferred(r0);
        return r0;
    L18:
        r2 = Long.MAX_VALUE;
        goto L19
    }

    @Override // androidx.media3.datasource.HttpDataSource
    public void setRequestProperty(String r2, String r3) {
        this.requestProperties.set(r2, r3);
    }
}
