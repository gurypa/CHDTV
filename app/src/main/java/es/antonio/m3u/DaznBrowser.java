package es.antonio.m3u;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Looper;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
final class DaznBrowser {
    static Runnable cancelRequest;
    static boolean loggingIn;
    static WebView shared;
    static MutableContextWrapper wrapper;

    private DaznBrowser() {
    }

    static WebView create(Context r5) {
        wrapper = new MutableContextWrapper(r5.getApplicationContext());
        WebView r0 = new WebView(wrapper);
        WebSettings r1 = r0.getSettings();
        r1.setJavaScriptEnabled(true);
        r1.setDomStorageEnabled(true);
        r1.setSaveFormData(false);
        r1.setAllowFileAccess(false);
        r1.setAllowContentAccess(false);
        r1.setMixedContentMode(1);
        r1.setCacheMode(2);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(r0, false);
        return r0;
    }

    static /* synthetic */ void lambda$reset$0() {
        if (cancelRequest == null) goto L6;
        cancelRequest.run();
        cancelRequest = null;
    L6:
        if (shared != null) goto L8;
    L11:
        loggingIn = false;
        return;
    L8:
        if ((shared.getParent() instanceof ViewGroup) == false) goto L10;
        ((ViewGroup) shared.getParent()).removeView(shared);
    L10:
        shared.stopLoading();
        shared.destroy();
        shared = null;
        wrapper = null;
        goto L11
    }

    static WebView login(Context r1) {
        mainThread();
        if (cancelRequest == null) goto L6;
        cancelRequest.run();
        cancelRequest = null;
    L6:
        if (shared != null) goto L8;
        shared = create(r1);
    L8:
        wrapper.setBaseContext(r1);
        loggingIn = true;
        shared.onResume();
        return shared;
    }

    static void mainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) goto L6;
        return;
    L6:
        throw new IllegalStateException("WebView requiere el hilo principal");
    }

    static WebView playback(Context r2) throws IOException {
        mainThread();
        if (loggingIn == true) goto L10;
        if (shared != null) goto L7;
        shared = create(r2);
    L7:
        shared.onResume();
        return shared;
    L10:
        throw new IOException("Completa primero el inicio de sesión DAZN");
    }

    static void releaseLogin(Context r2, WebView r3) {
        mainThread();
        if (r3 == shared) goto L6;
        return;
    L6:
        if ((r3.getParent() instanceof ViewGroup) == false) goto L8;
        ((ViewGroup) r3.getParent()).removeView(r3);
    L8:
        wrapper.setBaseContext(r2.getApplicationContext());
        loggingIn = false;
        r3.setWebViewClient(new WebViewClient());
        CookieManager.getInstance().flush();
        r3.onResume();
    }

    static void reset() {
        DaznTokens.main.post(new DaznBrowser$$ExternalSyntheticLambda0());
    }
}
