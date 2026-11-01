package es.antonio.m3u;

import java.io.IOException;
import java.net.URI;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DaznLogin {
    static final String HOOK = "(function(){\n if(window.__chdtvDaznHook)return;window.__chdtvDaznHook=true;\n function capture(url,status,text){try{var u=new URL(url,location.href);if(u.protocol!=='https:'||!/(^|\\.)indazn\\.com$/.test(u.hostname)||!/\\/signin(?:\\/|$)/i.test(u.pathname)||status!==200||text.length>131072)return;var d=JSON.parse(text);if(d.AuthToken&&typeof d.AuthToken.Token==='string')window.__chdtvDaznSession=JSON.stringify({AuthToken:d.AuthToken});}catch(e){}}\n var originalFetch=window.fetch;if(originalFetch)window.fetch=function(){return originalFetch.apply(this,arguments).then(function(r){if(/signin/i.test(r.url))r.clone().text().then(function(t){capture(r.url,r.status,t);}).catch(function(){});return r;});};\n var send=XMLHttpRequest.prototype.send;XMLHttpRequest.prototype.send=function(){this.addEventListener('load',function(){try{capture(this.responseURL,this.status,this.responseType==='json'?JSON.stringify(this.response):this.responseText);}catch(e){}});return send.apply(this,arguments);};\n})();\n";

    private DaznLogin() {
    }

    static boolean officialPage(String r5) {
        URI r1 = new URI(r5);     // Catch: Exception -> L18
        String r2 = r1.getHost();     // Catch: Exception -> L18
        if ("https".equals(r1.getScheme()) == true) goto L6;
        return false;
    L6:
        if ("www.dazn.com".equals(r2) == true) goto L10;
        if ("dazn.com".equals(r2) == true) goto L10;
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

    static String session(String r10) throws Exception {
        if (r10 == null) goto L14;
        if (r10.length() > 131072) goto L14;
        JSONObject r0 = new JSONObject(r10).getJSONObject("AuthToken");
        JSONObject r3 = DaznTokenClient.payload(r0.getString("Token"));
        if (r3.optString("deviceId").isEmpty() == true) goto L12;
        if ((r3.optLong("exp", 0) * 1000) <= (System.currentTimeMillis() + 30000)) goto L12;
        return new JSONObject().put("AuthToken", r0).toString();
    L12:
        throw new IOException("DAZN no devolvió una sesión vigente");
    L14:
        throw new IOException("Respuesta de inicio de sesión no válida");
    }
}
