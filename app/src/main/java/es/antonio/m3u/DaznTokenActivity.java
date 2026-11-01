package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes3.dex */
public final class DaznTokenActivity extends ChdtvScreen {
    String auth;
    String channels;
    final Handler main;
    String notice;
    Button renew;
    TextView status;
    final Runnable tick;

    public static /* synthetic */ void $r8$lambda$4dMurB6xUyeGjDuZFt4P_xEpBw8(DaznTokenActivity r0) {
        r0.lambda$onCreate$3();
    }

    public static /* synthetic */ void $r8$lambda$5VwVUcryCx_Gxyria1C82FNBmm0(DaznTokenActivity r0) {
        r0.lambda$onCreate$6();
    }

    public static /* synthetic */ void $r8$lambda$8sTH5O0X7SWnYCg4Kx3yA2AXNDA(DaznTokenActivity r0) {
        r0.lambda$renew$16();
    }

    public static /* synthetic */ void $r8$lambda$BgFat1Nrk6oCV5v3IgYsid8i08Y(DaznTokenActivity r0) {
        r0.lambda$onCreate$8();
    }

    public static /* synthetic */ void $r8$lambda$G9cEkJxhgNeWJqLdLijkIi5v0YM(DaznTokenActivity r0, Exception r1) {
        r0.lambda$importError$14(r1);
    }

    public static /* synthetic */ void $r8$lambda$Liya32QNaxSZfc9WmbL3BFsRy5E(DaznTokenActivity r0) {
        r0.lambda$onCreate$0();
    }

    public static /* synthetic */ void $r8$lambda$LoiWEg3KPdbI8zlm0VkRmG8vqkU(DaznTokenActivity r0, String r1, String r2) {
        r0.lambda$onCreate$5(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$MoB2FZiP7TXc0aI8wTpK1Dzfkdk(DaznTokenActivity r0, EditText r1, AlertDialog r2, boolean r3, int r4, View r5) {
        r0.lambda$inputDialog$12(r1, r2, r3, r4, r5);
    }

    public static /* synthetic */ void $r8$lambda$NkuiEaH_A1hcUPIz33p3EsN730o(DaznTokenActivity r0) {
        r0.lambda$onCreate$1();
    }

    public static /* synthetic */ void $r8$lambda$POVy5S9uO2j5KvlkJmi93TV3wN4(DaznTokenActivity r0, boolean r1, String r2, int r3) {
        r0.lambda$inputDialog$11(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$Vv71aAiS-CaSBQEJBzROYZjpUiQ, reason: not valid java name */
    public static /* synthetic */ void m559$r8$lambda$Vv71aAiSCaSBQEJBzROYZjpUiQ(DaznTokenActivity r0, DialogInterface r1, int r2) {
        r0.lambda$onCreate$7(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$XXM8BLKDvhcgvNYL0fZgbVuYaC4(DaznTokenActivity r0, Intent r1, int r2) {
        r0.lambda$onActivityResult$15(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$ZEyMMTg3RlbvYmGnPUuafSIEwlc(DaznTokenActivity r0) {
        r0.lambda$onCreate$2();
    }

    public static /* synthetic */ void $r8$lambda$cCa3z4egDBAuetM6EV1f2ephIm8(DaznTokenActivity r0, int r1, DialogInterface r2, int r3) {
        r0.lambda$importMenu$10(r1, r2, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$gi-uLZFSCRqrItUTFgqOUPhts3Q, reason: not valid java name */
    public static /* synthetic */ void m560$r8$lambda$giuLZFSCRqrItUTFgqOUPhts3Q(DaznTokenActivity r0) {
        r0.lambda$onResume$18();
    }

    public static /* synthetic */ void $r8$lambda$j24HxpMk8QaCGX_DWEkikzGV0Jw(DaznTokenActivity r0) {
        r0.lambda$onCreate$4();
    }

    /* JADX INFO: renamed from: $r8$lambda$rr-B5eF9IkOaXwLxLTkXJvflNCk, reason: not valid java name */
    public static /* synthetic */ void m561$r8$lambda$rrB5eF9IkOaXwLxLTkXJvflNCk(DaznTokenActivity r0, Boolean r1, Throwable r2) {
        r0.lambda$renew$17(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$yhXLLlphC_2IWJZH9N0PrNsuLK8(DaznTokenActivity r0, int r1, String r2) {
        r0.lambda$accept$13(r1, r2);
    }

    public DaznTokenActivity() {
        this.notice = "";
        this.main = new Handler(Looper.getMainLooper());
        this.tick = new RunnableC09791(this);
    }

    private /* synthetic */ void lambda$accept$13(int r3, String r4) {
        if (isDestroyed() == false) goto L6;
        return;
    L6:
        if (r3 != 71) goto L8;
        this.auth = r4;
    L9:
        if (r3 != 71) goto L11;
        String r0 = "Sesión cargada · pulsa Guardar y renovar cuando tengas el mapa";
    L12:
        this.notice = r0;
        this.status.setText(this.notice);
        return;
    L11:
        r0 = "Mapa cargado · pulsa Guardar y renovar cuando tengas la sesión";
        goto L12
    L8:
        this.channels = r4;
        goto L9
    }

    static /* synthetic */ void lambda$button$9(Runnable r0, View r1) {
        r0.run();
    }

    private /* synthetic */ void lambda$importError$14(Exception r5) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        String r0 = r5.getMessage();
        if ((r5 instanceof IOException) == false) goto L33;
        if (r0 == null) goto L33;
        if (r0.startsWith("El contenido ") == false) goto L11;
    L32:
        boolean r1 = true;
    L34:
        if (r1 == false) goto L36;
        String r2 = r0;
    L37:
        this.notice = r2;
        this.status.setText(this.notice);
        return;
    L36:
        r2 = "No se pudo importar: comprueba el contenido o el permiso del enlace";
        goto L37
    L11:
        if (r0.startsWith("El enlace ") == true) goto L32;
        if (r0.startsWith("La sesión ") == true) goto L32;
        if (r0.startsWith("Sesión no válida") == true) goto L32;
        if (r0.startsWith("Mapa no válido") == true) goto L32;
        if (r0.startsWith("El JSON ") == true) goto L32;
        if (r0.startsWith("El mapa ") == true) goto L32;
        if (r0.startsWith("Máximo ") == true) goto L32;
        if (r0.startsWith("Usa un ") == true) goto L32;
        if (r0.startsWith("Comparte el archivo") == true) goto L32;
        if (r0.startsWith("Descarga HTTP ") == true) goto L32;
        if (r0.startsWith("Demasiadas redirecciones") == true) goto L32;
    L33:
        r1 = false;
        goto L34
    }

    private /* synthetic */ void lambda$importMenu$10(int r2, DialogInterface r3, int r4) {
        if (r4 != 0) goto L5;
        pick(r2);
        return;
    L5:
        if (r4 != 2) goto L7;
        boolean r0 = true;
    L8:
        inputDialog(r2, r0);
        return;
    L7:
        r0 = false;
        goto L8
    }

    private /* synthetic */ void lambda$inputDialog$11(boolean r4, String r5, int r6) {
        if (r4 == true) goto L24;
        String r1 = r5;
    L18:
        accept(r6, r1);     // Catch: Exception -> L15
        return;
    L24:
        InputStream r0 = PlexDownload.open(r5);     // Catch: Exception -> L15
        r1 = DaznImport.read(r0);     // Catch: Throwable -> L8
        if (r0 == null) goto L18;
        r0.close();     // Catch: Exception -> L15
        goto L18
    L8:
        th = move-exception;
        if (r0 != null) goto L25;
    L14:
        throw th;     // Catch: Exception -> L15
    L25:
        r0.close();     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L15
    L15:
        e = move-exception;
        importError(e);
    }

    private /* synthetic */ void lambda$inputDialog$12(EditText r4, AlertDialog r5, final boolean r6, final int r7, View r8) {
        final String r0 = r4.getText().toString().trim();
        if (r0.isEmpty() == false) goto L6;
        r4.setError("Introduce el contenido o la URL");
        return;
    L6:
        r5.dismiss();
        this.notice = "Importando…";
        DaznTokens.worker.execute(new DaznTokenActivity$$ExternalSyntheticLambda19(this, r6, r0, r7));
    }

    private /* synthetic */ void lambda$onActivityResult$15(Intent r4, int r5) {
        InputStream r0 = getContentResolver().openInputStream(r4.getData());     // Catch: Exception -> L14
        accept(r5, DaznImport.read(r0));     // Catch: Throwable -> L7
        if (r0 == null) goto L22;
        r0.close();     // Catch: Exception -> L14
        return;
    L22:
        return;
    L7:
        th = move-exception;
        if (r0 != null) goto L17;
    L13:
        throw th;     // Catch: Exception -> L14
    L17:
        r0.close();     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L14
    L14:
        e = move-exception;
        importError(e);
    }

    private /* synthetic */ void lambda$onCreate$0() {
        finish();
    }

    private /* synthetic */ void lambda$onCreate$1() {
        startActivityForResult(new Intent(this, DaznLoginActivity.class), 73);
    }

    private /* synthetic */ void lambda$onCreate$2() {
        importMenu(71);
    }

    private /* synthetic */ void lambda$onCreate$3() {
        importMenu(72);
    }

    private /* synthetic */ void lambda$onCreate$4() {
        this.notice = "No se pudo guardar: comprueba sesión y AssetId del JSON";
        this.status.setText(this.notice);
    }

    private /* synthetic */ void lambda$onCreate$5(String r3, String r4) {
        DaznTokens.savePrepared(this, r3, r4);     // Catch: Exception -> L4
        this.auth = null;     // Catch: Exception -> L4
        this.channels = null;     // Catch: Exception -> L4
        runOnUiThread(new DaznTokenActivity$$ExternalSyntheticLambda0(this));     // Catch: Exception -> L4
        return;
    L5:
        runOnUiThread(new DaznTokenActivity$$ExternalSyntheticLambda11(this));
    }

    private /* synthetic */ void lambda$onCreate$6() {
        if (this.auth != null) goto L7;
        if (DaznTokens.hasSession(this) == true) goto L7;
    L10:
        this.notice = "Inicia sesión o impórtala, y carga el mapa antes de guardar";
        this.status.setText(this.notice);
        return;
    L7:
        if (this.channels == null) goto L9;
    L12:
        final String r0 = this.auth;
        final String r1 = this.channels;
        DaznTokens.worker.execute(new DaznTokenActivity$$ExternalSyntheticLambda7(this, r0, r1));
        return;
    L9:
        if (DaznTokens.configured(this) == true) goto L12;
        goto L10
    }

    private /* synthetic */ void lambda$onCreate$7(DialogInterface r3, int r4) {
        DaznTokens.disable(this);
        this.auth = null;
        this.channels = null;
        this.notice = "";
        this.status.setText(DaznTokens.status(this));
    }

    private /* synthetic */ void lambda$onCreate$8() {
        new AlertDialog.Builder(this).setTitle("Borrar sesión DAZN").setMessage("Se conserva el contenido de tus listas.").setNegativeButton("Cancelar", null).setPositiveButton("Borrar", new DaznTokenActivity$$ExternalSyntheticLambda5(this)).show();
    }

    private /* synthetic */ void lambda$onResume$18() {
        this.main.post(this.tick);
    }

    private /* synthetic */ void lambda$renew$16() {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.renew.setEnabled(true);
        this.status.setText(DaznTokens.status(this));
    }

    private /* synthetic */ void lambda$renew$17(Boolean r2, Throwable r3) {
        runOnUiThread(new DaznTokenActivity$$ExternalSyntheticLambda2(this));
    }

    void accept(final int r3, String r4) throws Exception {
        if (r3 != 71) goto L5;
        final String r0 = DaznImport.prepare(r4, true);
    L6:
        runOnUiThread(new DaznTokenActivity$$ExternalSyntheticLambda6(this, r3, r0));
        return;
    L5:
        r0 = DaznImport.prepareMap(this, r4);
        goto L6
    }

    Button button(LinearLayout r5, String r6, final Runnable r7) {
        Button r0 = ChdtvDialogs.action(this, r6, false);
        r0.setOnClickListener(new DaznTokenActivity$$ExternalSyntheticLambda9(r7));
        r5.addView(r0, new LinearLayout.LayoutParams(-1, ChdtvDialogs.m393dp(this, 52)));
        return r0;
    }

    void importError(final Exception r2) {
        runOnUiThread(new DaznTokenActivity$$ExternalSyntheticLambda8(this, r2));
    }

    void importMenu(final int r5) {
        AlertDialog.Builder r0 = new AlertDialog.Builder(this);
        if (r5 != 71) goto L5;
        String r1 = "Importar sesión DAZN";
    L6:
        r0.setTitle(r1).setItems(new String[]{"Archivo del dispositivo", "Pegar contenido", "URL o Dropbox"}, new DaznTokenActivity$$ExternalSyntheticLambda12(this, r5)).show();
        return;
    L5:
        r1 = "Importar mapa DAZN";
        goto L6
    }

    void inputDialog(final int r8, final boolean r9) {
        final EditText r0 = new EditText(this);
        r0.setTextColor(-1181697);
        r0.setHintTextColor(-5322788);
        r0.setPadding(20, 20, 20, 20);
        r0.setBackgroundColor(-15716796);
        r0.setSingleLine(r9);
        if (r9 == false) goto L5;
        int r1 = 16;
    L6:
        r0.setInputType(r1 | 1);
        if (r9 == true) goto L10;
        r0.setMinLines(5);
        r0.setMaxLines(10);
    L10:
        if (r9 == false) goto L12;
        String r2 = "Enlace del archivo o Dropbox";
    L15:
        r0.setHint(r2);
        r0.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2097152)});
        AlertDialog.Builder r3 = new AlertDialog.Builder(this);
        if (r9 == false) goto L18;
        String r4 = "URL o Dropbox";
    L21:
        final AlertDialog r5 = r3.setTitle(r4).setView(r0).setNegativeButton("Cancelar", null).setPositiveButton("Importar", null).create();
        r5.show();
        r5.getWindow().addFlags(8192);
        r5.getWindow().setBackgroundDrawable(ChdtvDialogs.shape(-267376320, -2141856273, ChdtvDialogs.m393dp(this, 18)));
        r5.getButton(-1).setOnClickListener(new DaznTokenActivity$$ExternalSyntheticLambda4(this, r0, r5, r9, r8));
        return;
    L18:
        if (r8 != 71) goto L20;
        r4 = "Pegar sesión DAZN";
        goto L21
    L20:
        r4 = "Pegar mapa JSON";
        goto L21
    L12:
        if (r8 != 71) goto L14;
        r2 = "Contenido de auto_token.txt o token de sesión";
        goto L15
    L14:
        r2 = "Contenido de Dazn.json o Dazn_streams.json";
        goto L15
    L5:
        r1 = 655360;
        goto L6
    }

    @Override // android.app.Activity
    protected void onActivityResult(final int r3, int r4, final Intent r5) {
        super.onActivityResult(r3, r4, r5);
        if (r3 != 73) goto L11;
        if (r4 != (-1)) goto L21;
        this.auth = null;
        this.notice = "✓ Sesión obtenida desde este Android y guardada";
        if (DaznTokens.configured(this) == false) goto L8;
        renew();
        return;
    L8:
        this.notice += " · importa el mapa de canales";
        return;
    L21:
        return;
    L11:
        if (r3 != 71) goto L13;
    L14:
        if (r4 != (-1)) goto L24;
        if (r5 != null) goto L17;
        return;
    L17:
        if (r5.getData() == null) goto L26;
        this.notice = "Importando…";
        DaznTokens.worker.execute(new DaznTokenActivity$$ExternalSyntheticLambda10(this, r5, r3));
        return;
    L26:
        return;
    L24:
        return;
    L13:
        if (r3 == 72) goto L14;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r8) {
        super.onCreate(r8);
        getWindow().addFlags(8192);
        ScrollView r0 = new ScrollView(this);
        r0.setBackgroundColor(-16115415);
        LinearLayout r1 = new LinearLayout(this);
        r1.setOrientation(1);
        r1.setPadding(28, 28, 28, 28);
        r0.addView(r1);
        setContentView(r0);
        button(r1, "‹ Volver", new DaznTokenActivity$$ExternalSyntheticLambda13(this));
        TextView r2 = new TextView(this);
        r2.setText("Token DAZN · renovación por canal");
        r2.setTextSize(24.0f);
        r2.setTextColor(-11149840);
        r1.addView(r2);
        TextView r4 = new TextView(this);
        r4.setText("Inicia sesión con tu cuenta desde este Android e importa Dazn.json o Dazn_streams.json. Con el mapa guardado también puedes cargar Dazn.m3u aquí. También puedes importar una sesión existente. Se guardan cifrados en este dispositivo. La lista M3U debe estar importada y contener los mismos canales. Se renueva cada 23 h 55 min o antes si el token caduca. Android puede retrasar las tareas de fondo. Si DAZN pide identificarte de nuevo, vuelve a iniciar sesión.");
        r4.setTextColor(-1181697);
        r1.addView(r4);
        button(r1, "1 · Iniciar sesión con DAZN", new DaznTokenActivity$$ExternalSyntheticLambda14(this));
        button(r1, "Importar sesión existente", new DaznTokenActivity$$ExternalSyntheticLambda15(this));
        button(r1, "2 · Importar mapa, estado CDN o M3U", new DaznTokenActivity$$ExternalSyntheticLambda16(this));
        button(r1, "Guardar y renovar", new DaznTokenActivity$$ExternalSyntheticLambda17(this));
        this.renew = button(r1, "Renovar ahora", new DaznTokenActivity$$ExternalSyntheticLambda0(this));
        button(r1, "Desactivar y borrar sesión", new DaznTokenActivity$$ExternalSyntheticLambda18(this));
        this.status = new TextView(this);
        this.status.setTextColor(-11149840);
        this.status.setTextSize(16.0f);
        r1.addView(this.status);
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.main.removeCallbacks(this.tick);
        super.onPause();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        ParentalControl.authenticate(this, new DaznTokenActivity$$ExternalSyntheticLambda3(this), true);
    }

    void pick(int r3) {
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT").setType("*/*").addCategory("android.intent.category.OPENABLE"), r3);
    }

    void renew() {
        this.notice = "";
        if (DaznTokens.configured(this) == true) goto L6;
        this.status.setText("Guarda primero la sesión y el mapa");
        return;
    L6:
        this.renew.setEnabled(false);
        DaznTokens.renew(this, true, null).whenComplete(new DaznTokenActivity$$ExternalSyntheticLambda1(this));
    }
}
