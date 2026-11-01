package es.antonio.m3u;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONObject;
import org.json.JSONTokener;

/* JADX INFO: loaded from: classes3.dex */
final class DaznPlaybackTransport implements MovistarTokenClient.Transport {
    final MovistarTokenClient.Transport browser;
    final MovistarTokenClient.Transport http;

    /* JADX INFO: renamed from: es.antonio.m3u.DaznPlaybackTransport$1 */
    class C09781 extends WebViewClient {
        boolean finished;
        int status;
        final /* synthetic */ AtomicBoolean val$cancelled;
        final /* synthetic */ CountDownLatch val$done;
        final /* synthetic */ AtomicReference val$error;
        final /* synthetic */ AtomicReference val$value;
        final /* synthetic */ WebView val$web;

        /* JADX INFO: renamed from: $r8$lambda$GGAC5-483Vt8BMlJrpyyCG_5HBU, reason: not valid java name */
        public static /* synthetic */ void m558$r8$lambda$GGAC5483Vt8BMlJrpyyCG_5HBU(C09781 r0, WebView r1, String r2) {
            r0.lambda$onPageFinished$0(r1, r2);
        }

        C09781(AtomicBoolean r1, AtomicReference r2, AtomicReference r3, CountDownLatch r4, WebView r5) {
            this.val$cancelled = r1;
            this.val$value = r2;
            this.val$error = r3;
            this.val$done = r4;
            this.val$web = r5;
            this.status = 200;
        }

        private /* synthetic */ void lambda$onPageFinished$0(WebView r7, String r8) {
            Object r1 = new JSONTokener(r8).nextValue();     // Catch: Exception -> L12
            if ((r1 instanceof String) == false) goto L11;
            String r2 = (String) r1;     // Catch: Exception -> L12
            if (r2.trim().startsWith("{") == false) goto L9;
            finish(new JSONObject(r2).put("_chdtvBrowserUserAgent", r7.getSettings().getUserAgentString()).toString(), null);     // Catch: Exception -> L12
            return;
        L9:
            throw new IOException("Playback no devolvió JSON · Android WebView");     // Catch: Exception -> L12
        L11:
            throw new IOException("Respuesta de Playback demasiado grande o vacía");     // Catch: Exception -> L12
        L12:
            e = move-exception;
            if ((e instanceof IOException) == false) goto L16;
            IOException r3 = (IOException) e;
        L17:
            finish(null, r3);
            return;
        L16:
            r3 = new IOException("Respuesta de Playback no válida · Android WebView");
            goto L17
        }

        void finish(String r2, IOException r3) {
            if (this.finished == false) goto L5;
            return;
        L5:
            if (this.val$cancelled.get() == true) goto L10;
            this.finished = true;
            this.val$value.set(r2);
            this.val$error.set(r3);
            this.val$done.countDown();
            return;
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView r5, String r6) {
            if (this.finished == false) goto L5;
            return;
        L5:
            if (this.val$cancelled.get() == false) goto L8;
            return;
        L8:
            if (DaznPlaybackTransport.playbackUrl(r6) == true) goto L13;
            IOException r0 = new IOException("Destino de Playback no permitido");
        L10:
            finish(null, r0);
            return;
        L13:
            if (this.status >= 200) goto L15;
        L19:
            r0 = new IOException("HTTP " + this.status + " · /v5/Playback · Android WebView");
            goto L10
        L15:
            if (this.status >= 300) goto L19;
            final WebView r1 = this.val$web;
            r5.evaluateJavascript("(function(){var t=document.body?document.body.innerText:'';return t.length>2097152?null:t;})()", new DaznPlaybackTransport$1$$ExternalSyntheticLambda0(this, r1));
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView r3, WebResourceRequest r4, WebResourceError r5) {
            if (r4.isForMainFrame() == false) goto L6;
            finish(null, new IOException("No se pudo conectar a Playback con Android WebView"));
            return;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedHttpError(WebView r2, WebResourceRequest r3, WebResourceResponse r4) {
            if (r3.isForMainFrame() == false) goto L6;
            this.status = r4.getStatusCode();
            return;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView r3, WebResourceRequest r4) {
            if (DaznPlaybackTransport.playbackUrl(r4.getUrl().toString()) == true) goto L6;
            finish(null, new IOException("Playback redirigió fuera de su servicio"));
            return true;
        L6:
            return false;
        }
    }

    DaznPlaybackTransport(final Context r3) {
        this(new MovistarTokenClient.Http(), new DaznPlaybackTransport$$ExternalSyntheticLambda0(r3));
    }

    DaznPlaybackTransport(MovistarTokenClient.Transport r1, MovistarTokenClient.Transport r2) {
        this.http = r1;
        this.browser = r2;
    }

    static String browserGet(final Context r13, final String r14, final Map<String, String> r15) throws Exception {
        if (playbackUrl(r14) == false) goto L27;
        if (Looper.myLooper() == Looper.getMainLooper()) goto L25;
        Handler r0 = new Handler(Looper.getMainLooper());
        final CountDownLatch r1 = new CountDownLatch(1);
        final AtomicReference r11 = new AtomicReference();
        final AtomicReference r8 = new AtomicReference();
        final AtomicReference r6 = new AtomicReference();
        final AtomicBoolean r4 = new AtomicBoolean();
        final AtomicReference r10 = new AtomicReference();
        r0.post(new DaznPlaybackTransport$$ExternalSyntheticLambda1(r4, r13, r6, r15, r8, r1, r10, r11, r14));
    L21:
        th = move-exception;
        r4.set(true);
        r0.post(new DaznPlaybackTransport$$ExternalSyntheticLambda2(r6, r10));
        throw th;
    L8:
        if (r1.await(45, TimeUnit.SECONDS) == false) goto L30;
        if (r8.get() != null) goto L29;
        if (r11.get() == null) goto L17;
        String r16 = (String) r11.get();     // Catch: Throwable -> L21
        r4.set(true);
        r0.post(new DaznPlaybackTransport$$ExternalSyntheticLambda2(r6, r10));
        return r16;
    L17:
        throw new IOException("Playback no devolvió respuesta");     // Catch: Throwable -> L21
    L29:
        throw ((IOException) r8.get());     // Catch: Throwable -> L21
    L30:
        throw new IOException("Playback agotó el tiempo de espera · Android WebView");     // Catch: Throwable -> L21
    L25:
        throw new IOException("Playback debe ejecutarse en segundo plano");
    L27:
        throw new IOException("Destino de Playback no permitido");
    }

    static /* synthetic */ void lambda$browserGet$1(AtomicReference r2, AtomicBoolean r3, CountDownLatch r4, WebView r5) {
        r2.set(new IOException("Consulta de Playback interrumpida para iniciar sesión"));
        r3.set(true);
        r4.countDown();
        r5.stopLoading();
    }

    static /* synthetic */ void lambda$browserGet$2(final AtomicBoolean r8, Context r9, AtomicReference r10, Map r11, final AtomicReference r12, final CountDownLatch r13, AtomicReference r14, AtomicReference r15, String r16) {
        if (r8.get() == false) goto L15;
        return;
    L15:
        final WebView r0 = DaznBrowser.playback(r9);     // Catch: Exception -> L11
        r10.set(r0);     // Catch: Exception -> L11
        LinkedHashMap r1 = new LinkedHashMap(r11);     // Catch: Exception -> L11
        r1.remove(HttpHeaders.USER_AGENT);     // Catch: Exception -> L11
        Runnable r2 = new DaznPlaybackTransport$$ExternalSyntheticLambda3(r12, r8, r13, r0);     // Catch: Exception -> L11
        r14.set(r2);     // Catch: Exception -> L11
        DaznBrowser.cancelRequest = r2;     // Catch: Exception -> L11
        r0.setWebViewClient(new C09781(r8, r15, r12, r13, r0));     // Catch: Exception -> L11
        r0.loadUrl(r16, r1);     // Catch: Exception -> L9
        return;
    L13:
        r12.set(new IOException("Android WebView no está disponible o no pudo iniciar"));
        r13.countDown();
    }

    static /* synthetic */ void lambda$browserGet$3(AtomicReference r4, AtomicReference r5) {
        WebView r1 = (WebView) r4.getAndSet(null);
        if (r1 != null) goto L5;
        return;
    L5:
        if (DaznBrowser.loggingIn == false) goto L7;
        return;
    L7:
        if (DaznBrowser.cancelRequest != r5.get()) goto L12;
        r1.stopLoading();
        r1.setWebViewClient(new WebViewClient());
        DaznBrowser.cancelRequest = null;
        return;
    }

    static /* synthetic */ String lambda$new$0(Context r1, String r2, String r3, Map r4, String r5) throws Exception {
        return browserGet(r1, r3, r4);
    }

    static boolean playbackUrl(String r4) {
        URI r1 = new URI(r4);     // Catch: Exception -> L18
        if ("https".equals(r1.getScheme()) == true) goto L6;
        return false;
    L6:
        if ("api.playback.indazn.com".equals(r1.getHost()) == true) goto L8;
        return false;
    L8:
        if ("/v5/Playback".equals(r1.getPath()) == true) goto L10;
        return false;
    L10:
        if (r1.getUserInfo() == null) goto L12;
        return false;
    L12:
        if (r1.getPort() == (-1)) goto L16;
        if (r1.getPort() == 443) goto L16;
        return false;
    L16:
        return true;
    L19:
        return false;
    }

    @Override // es.antonio.m3u.MovistarTokenClient.Transport
    public String call(String r3, String r4, Map<String, String> r5, String r6) throws Exception {
        if ("GET".equals(r3) == false) goto L10;
        if (r6 != null) goto L10;
        if (playbackUrl(r4) == false) goto L10;
        return this.browser.call(r3, r4, r5, null);
    L10:
        return this.http.call(r3, r4, r5, r6);
    }
}
