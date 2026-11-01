package es.antonio.m3u;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONTokener;

/* JADX INFO: loaded from: classes3.dex */
public final class DaznLoginActivity extends ChdtvScreen {
    final Handler main;
    boolean paused;
    final Runnable poll;
    boolean saving;
    TextView status;
    WebView web;

    /* JADX INFO: renamed from: es.antonio.m3u.DaznLoginActivity$1 */
    class RunnableC09761 implements Runnable {
        final /* synthetic */ DaznLoginActivity this$0;

        /* JADX INFO: renamed from: $r8$lambda$74Jtl458kMa37x1OcuKrAJi-iWc, reason: not valid java name */
        public static /* synthetic */ void m556$r8$lambda$74Jtl458kMa37x1OcuKrAJiiWc(RunnableC09761 r0, String r1) {
            r0.lambda$run$3(r1);
        }

        /* JADX INFO: renamed from: $r8$lambda$Bb-_jiGfR7w9JeLdQ7rY2nwY5YQ, reason: not valid java name */
        public static /* synthetic */ void m557$r8$lambda$Bb_jiGfR7w9JeLdQ7rY2nwY5YQ(RunnableC09761 r0) {
            r0.lambda$run$0();
        }

        public static /* synthetic */ void $r8$lambda$_mQxRH5TbAYrxgWg5XdquSI0Iw4(RunnableC09761 r0) {
            r0.lambda$run$1();
        }

        public static /* synthetic */ void $r8$lambda$cs35Z_FcCrKTWEEaY31q2IZjyh8(RunnableC09761 r0, String r1) {
            r0.lambda$run$2(r1);
        }

        RunnableC09761(DaznLoginActivity r1) {
            this.this$0 = r1;
        }

        private /* synthetic */ void lambda$run$0() {
            if (this.this$0.isDestroyed() == true) goto L6;
            this.this$0.setResult(-1);
            this.this$0.finish();
            return;
        }

        private /* synthetic */ void lambda$run$1() {
            this.this$0.saving = false;
            this.this$0.status.setText("No se pudo guardar la sesión. Vuelve a intentarlo.");
        }

        private /* synthetic */ void lambda$run$2(String r4) {
            DaznTokens.saveLogin(this.this$0, r4);     // Catch: Exception -> L4
            this.this$0.runOnUiThread(new DaznLoginActivity$1$$ExternalSyntheticLambda1(this));     // Catch: Exception -> L4
            return;
        L5:
            this.this$0.runOnUiThread(new DaznLoginActivity$1$$ExternalSyntheticLambda2(this));
        }

        private /* synthetic */ void lambda$run$3(String r6) {
            if (this.this$0.paused == false) goto L5;
            return;
        L5:
            if (this.this$0.saving == false) goto L7;
            return;
        L7:
            if (this.this$0.isDestroyed() == true) goto L21;
            Object r0 = new JSONTokener(r6).nextValue();     // Catch: Exception -> L13
            if ((r0 instanceof String) == false) goto L20;
            final String r1 = DaznLogin.session((String) r0);     // Catch: Exception -> L13
            this.this$0.saving = true;     // Catch: Exception -> L13
            this.this$0.status.setText("Guardando sesión cifrada…");     // Catch: Exception -> L13
            new Thread(new DaznLoginActivity$1$$ExternalSyntheticLambda0(this, r1), "chdtv-dazn-login").start();     // Catch: Exception -> L13
            return;
        L20:
            return;
        L14:
            this.this$0.status.setText("DAZN no devolvió una sesión válida. Completa el inicio de sesión.");
            return;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.this$0.paused == false) goto L5;
            return;
        L5:
            if (this.this$0.saving == false) goto L7;
            return;
        L7:
            if (this.this$0.web != null) goto L10;
            return;
        L10:
            if (DaznLogin.officialPage(this.this$0.web.getUrl()) == false) goto L12;
            this.this$0.web.evaluateJavascript("window.__chdtvDaznSession||null", new DaznLoginActivity$1$$ExternalSyntheticLambda3(this));
        L12:
            this.this$0.main.postDelayed(this, 1000);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$YuV5L-xQRL6ZvK_WaDELvMa94zk, reason: not valid java name */
    public static /* synthetic */ void m555$r8$lambda$YuV5LxQRL6ZvK_WaDELvMa94zk(DaznLoginActivity r0, View r1) {
        r0.lambda$onCreate$0(r1);
    }

    public static /* synthetic */ void $r8$lambda$fe7XcugLFWJM0DQXZilUEU_lrns(DaznLoginActivity r0) {
        r0.lambda$onResume$1();
    }

    public DaznLoginActivity() {
        this.main = new Handler(Looper.getMainLooper());
        this.paused = true;
        this.poll = new RunnableC09761(this);
    }

    private /* synthetic */ void lambda$onCreate$0(View r1) {
        finish();
    }

    private /* synthetic */ void lambda$onResume$1() {
        this.paused = false;
        if (this.web == null) goto L5;
        this.web.onResume();
    L5:
        this.main.removeCallbacks(this.poll);
        this.main.post(this.poll);
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r9) {
        super.onCreate(r9);
        getWindow().addFlags(8192);
        LinearLayout r0 = new LinearLayout(this);
        r0.setOrientation(1);
        r0.setBackgroundColor(-16115415);
        setContentView(r0);
        Button r2 = ChdtvDialogs.action(this, "‹ Volver a DAZN", false);
        r2.setOnClickListener(new DaznLoginActivity$$ExternalSyntheticLambda1(this));
        r0.addView(r2);
        this.status = new TextView(this);
        this.status.setTextColor(-11149840);
        this.status.setPadding(20, 12, 20, 12);
        this.status.setText("Inicia sesión en la página oficial. Chdtv guarda la sesión cifrada; no guarda tu contraseña.");
        r0.addView(this.status);
        this.web = DaznBrowser.login(this);     // Catch: Exception -> L5
        WebSettings r4 = this.web.getSettings();     // Catch: Exception -> L5
        r4.setJavaScriptEnabled(true);     // Catch: Exception -> L5
        r4.setDomStorageEnabled(true);     // Catch: Exception -> L5
        r4.setSaveFormData(false);     // Catch: Exception -> L5
        r4.setAllowFileAccess(false);     // Catch: Exception -> L5
        r4.setAllowContentAccess(false);     // Catch: Exception -> L5
        r4.setMixedContentMode(1);     // Catch: Exception -> L5
        CookieManager.getInstance().setAcceptCookie(true);     // Catch: Exception -> L5
        CookieManager.getInstance().setAcceptThirdPartyCookies(this.web, false);     // Catch: Exception -> L5
        this.web.setWebViewClient(new C09772(this));     // Catch: Exception -> L5
        r0.addView(this.web, new LinearLayout.LayoutParams(-1, 0, 1.0f));     // Catch: Exception -> L5
        this.web.loadUrl("https://www.dazn.com/es-ES/signin");     // Catch: Exception -> L5
        return;
    L6:
        this.status.setText("No se pudo iniciar Android WebView. Comprueba que esté instalado y actualizado.");
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.main.removeCallbacks(this.poll);
        if (this.web == null) goto L5;
        DaznBrowser.releaseLogin(this, this.web);
        this.web = null;
    L5:
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.paused = true;
        this.main.removeCallbacks(this.poll);
        if (this.web == null) goto L5;
        this.web.onPause();
    L5:
        super.onPause();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        ParentalControl.authenticate(this, new DaznLoginActivity$$ExternalSyntheticLambda0(this), true);
    }
}
