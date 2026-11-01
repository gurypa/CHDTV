package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes3.dex */
public final class BackupActivity extends ChdtvScreen {
    private Button account;
    private boolean busy;
    private Button direct;
    private File exportFile;
    private int exportMode;
    private Button full;
    private BackupHistoryView history;
    private ScrollView page;
    private String password;
    private boolean publicLink;
    private Button restore;
    private File restoreFile;
    private Button save;
    private SettingsSidePanel settingsPanel;
    private Button share;
    private TextView status;
    private final ExecutorService worker;

    public static /* synthetic */ void $r8$lambda$3vwmzQCI2IsHdEd01iuvVmvflEw(BackupActivity r0) {
        r0.lambda$exportCopy$21();
    }

    public static /* synthetic */ void $r8$lambda$4CPvRp2PH3lF6nVu36xz6ucSAbE(BackupActivity r0, File r1, int r2) {
        r0.lambda$execute$29(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$4k8KKuNYEamlydDe4sM4c7q9XDc(BackupActivity r0, View r1) {
        r0.lambda$onCreate$7(r1);
    }

    public static /* synthetic */ void $r8$lambda$9QO_TlHDu7L1OuvsA2L_nlEZwcw(BackupActivity r0, int r1, Uri r2, String r3) {
        r0.lambda$execute$31(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$B6_JuinA2x2Sp6F8yJbnyxw_kvY(BackupActivity r0, long r1, long r3) {
        r0.lambda$execute$26(r1, r3);
    }

    /* JADX INFO: renamed from: $r8$lambda$CjgJd_vsD-VH6QHTbl-BI5lBcO4, reason: not valid java name */
    public static /* synthetic */ void m509$r8$lambda$CjgJd_vsDVH6QHTblBI5lBcO4(BackupActivity r0, long r1, long r3) {
        r0.lambda$execute$25(r1, r3);
    }

    public static /* synthetic */ void $r8$lambda$FFj60AU5EItLsVdOIvgm8AvNdQ8(BackupActivity r0) {
        r0.lambda$exportCopy$20();
    }

    public static /* synthetic */ void $r8$lambda$FNQKXvFxNJIxaL1f06eWqfmubDA(BackupActivity r0) {
        r0.lambda$onCreate$12();
    }

    public static /* synthetic */ void $r8$lambda$G9yuWgOATIjpDKZU4RwIni6w6WU(BackupActivity r0, View r1) {
        r0.lambda$onCreate$2(r1);
    }

    public static /* synthetic */ void $r8$lambda$HDGbBWHBLzDViwK3KM02qwcmzlU(BackupActivity r0, Uri r1) {
        r0.lambda$onActivityResult$18(r1);
    }

    public static /* synthetic */ void $r8$lambda$IFzZdFItaC1noCMtqMv5iN7buRE(BackupActivity r0) {
        r0.lambda$reveal$19();
    }

    public static /* synthetic */ void $r8$lambda$JqMYF01wPaIv5kojs47c6L20d6E(BackupActivity r0, int r1, String r2, OutputStream r3) {
        r0.lambda$execute$24(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$TDIUu418dwmpkEiy_Vk2Z85SMHg(BackupActivity r0, EditText r1, boolean r2, EditText r3, AlertDialog r4, View r5) {
        r0.lambda$password$16(r1, r2, r3, r4, r5);
    }

    /* JADX INFO: renamed from: $r8$lambda$UtFO8Szh1WLn-Bzueauoa5Hv7L4, reason: not valid java name */
    public static /* synthetic */ void m510$r8$lambda$UtFO8Szh1WLnBzueauoa5Hv7L4(BackupActivity r0, boolean r1, Exception r2) {
        r0.lambda$execute$30(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$VvMAcc858blZYod4YDy4H2kfYHs(BackupActivity r0, View r1) {
        r0.lambda$onCreate$11(r1);
    }

    public static /* synthetic */ void $r8$lambda$XKMIQkhsJegaOmCzb9SnEWNQBzs(BackupActivity r0, DropboxBackupClient.Result r1) {
        r0.lambda$execute$28(r1);
    }

    public static /* synthetic */ void $r8$lambda$f6pkgUqPHhkoCXfpswTrwKpxva8(BackupActivity r0, DialogInterface r1, int r2) {
        r0.lambda$password$14(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$g_-V57ewmRTJscHiG9FNglstlgE, reason: not valid java name */
    public static /* synthetic */ void m511$r8$lambda$g_V57ewmRTJscHiG9FNglstlgE(BackupActivity r0, View r1) {
        r0.lambda$onCreate$1(r1);
    }

    public static /* synthetic */ void $r8$lambda$jPWN0cnMOXSXwCqKJNGdMkITKYo(BackupActivity r0, View r1) {
        r0.lambda$onCreate$10(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$kKtY_lfDMoQ_-e1cyMdl01OPEUQ, reason: not valid java name */
    public static /* synthetic */ void m512$r8$lambda$kKtY_lfDMoQ_e1cyMdl01OPEUQ(BackupActivity r0) {
        r0.lambda$onCreate$8();
    }

    public static /* synthetic */ void $r8$lambda$mjXj3LPRZwQtqyMvZcgIfIcxYBw(BackupActivity r0, View r1) {
        r0.lambda$onCreate$9(r1);
    }

    public static /* synthetic */ void $r8$lambda$s8R3z3YEH_QD4bxbmExFKdr8kqg(BackupActivity r0, View r1) {
        r0.lambda$onCreate$4(r1);
    }

    public static /* synthetic */ void $r8$lambda$sbRYzww2wkUbbG7cNP7gEtEJaMM(BackupActivity r0, DropboxBackupClient.Result r1, DialogInterface r2, int r3) {
        r0.lambda$execute$27(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$tmj7bIbmlpdJ4cKzu3VybYcRhMU(BackupActivity r0) {
        r0.lambda$execute$23();
    }

    public static /* synthetic */ void $r8$lambda$uBN_Rkq2tGTROmkmlcQaCKjm85o(BackupActivity r0) {
        r0.lambda$onCreate$5();
    }

    /* JADX INFO: renamed from: $r8$lambda$uW6FrzMJCRro_Q0-UOfUkB5Wp30, reason: not valid java name */
    public static /* synthetic */ void m513$r8$lambda$uW6FrzMJCRro_Q0UOfUkB5Wp30(BackupActivity r0, View r1) {
        r0.lambda$onCreate$3(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$v8liP0WFyDOBliu-As1WVRSdoy0, reason: not valid java name */
    public static /* synthetic */ void m514$r8$lambda$v8liP0WFyDOBliuAs1WVRSdoy0(BackupActivity r0, CheckBox r1, DialogInterface r2, int r3) {
        r0.lambda$onCreate$6(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$vSyLmFR8LPki57vVSeFCNNTq2jg(BackupActivity r0, DialogInterface r1, int r2) {
        r0.lambda$password$15(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$wSZBISHv3_HpG4NpNQkK5LNO1rI(BackupActivity r0, File r1, Uri r2) {
        r0.lambda$exportCopy$22(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$zTfHRMyCwQwqAmOKVgbveVMCKhE(BackupActivity r0, AlertDialog r1, EditText r2, boolean r3, EditText r4, DialogInterface r5) {
        r0.lambda$password$17(r1, r2, r3, r4, r5);
    }

    /* JADX INFO: renamed from: -$$Nest$fgetbusy, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m515$$Nest$fgetbusy(BackupActivity r0) {
        return r0.busy;
    }

    /* JADX INFO: renamed from: -$$Nest$fputrestoreFile, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m516$$Nest$fputrestoreFile(BackupActivity r0, File r1) {
        r0.restoreFile = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$mexportSaved, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m517$$Nest$mexportSaved(BackupActivity r0, File r1) {
        r0.exportSaved(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mpassword, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m518$$Nest$mpassword(BackupActivity r0, boolean r1) {
        r0.password(r1);
    }

    public BackupActivity() {
        this.worker = Executors.newSingleThreadExecutor();
        this.exportMode = 81;
    }

    private void buttons(boolean r2) {
        this.save.setEnabled(r2);
        this.restore.setEnabled(r2);
        this.full.setEnabled(r2);
        this.share.setEnabled(r2);
        this.direct.setEnabled(r2);
        this.account.setEnabled(r2);
    }

    private void execute(final int r4, final Uri r5) {
        final String r0 = this.password;
        if (r0 != null) goto L5;
        return;
    L5:
        this.password = null;
        this.busy = true;
        buttons(false);
        TextView r1 = this.status;
        if (r4 != 82) goto L8;
        String r2 = "Validando y restaurando…";
    L9:
        r1.setText(r2);
        reveal();
        this.worker.execute(new BackupActivity$$ExternalSyntheticLambda2(this, r4, r5, r0));
        return;
    L8:
        r2 = "Creando copia dentro de CHDTV…";
        goto L9
    }

    private void exportCopy(final File r3, final Uri r4) {
        this.busy = true;
        buttons(false);
        this.status.setText("Guardando copia fuera de CHDTV…");
        this.worker.execute(new BackupActivity$$ExternalSyntheticLambda5(this, r3, r4));
    }

    private void exportSaved(File r4) {
        this.exportFile = r4;
        startActivityForResult(new Intent("android.intent.action.CREATE_DOCUMENT").setType(BackupStore.mime(r4)).addCategory("android.intent.category.OPENABLE").putExtra("android.intent.extra.TITLE", r4.getName()), 86);     // Catch: ActivityNotFoundException -> L5
        return;
    L6:
        this.exportFile = null;
        this.status.setText("Esta TV no tiene selector para guardar fuera. La copia sigue aquí; puedes compartirla.");
        reveal();
    }

    private /* synthetic */ void lambda$execute$23() {
        this.busy = false;
        buttons(true);
        startActivity(new Intent(this, MainActivity.class).addFlags(268468224));
        finish();
    }

    private /* synthetic */ void lambda$execute$24(int r2, String r3, OutputStream r4) throws Exception {
        if (r2 != 81) goto L5;
        BackupArchive.write(this, r4, r3);
        return;
    L5:
        FullBackupArchive.write(this, r4, r3);
    }

    private /* synthetic */ void lambda$execute$25(long r6, long r8) {
        TextView r0 = this.status;
        StringBuilder r1 = new StringBuilder().append("Copia local guardada · Subiendo a Dropbox · ");
        long r3 = 100;
        if (r6 == 0) goto L6;
        r3 = (100 * r8) / r6;
    L6:
        r0.setText(r1.append(r3).append("%").toString());
    }

    private /* synthetic */ void lambda$execute$26(final long r7, final long r9) {
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda24(this, r9, r7));
    }

    private /* synthetic */ void lambda$execute$27(DropboxBackupClient.Result r4, DialogInterface r5, int r6) {
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copia CHDTV", r4.link));
    }

    private /* synthetic */ void lambda$execute$28(final DropboxBackupClient.Result r5) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.busy = false;
        buttons(true);
        TextView r0 = this.status;
        StringBuilder r1 = new StringBuilder().append("✓ Copia guardada aquí y subida a Dropbox: ").append(r5.path);
        if (r5.warning.isEmpty() == false) goto L8;
        String r2 = "";
    L9:
        r0.setText(r1.append(r2).toString());
        reveal();
        if (r5.link.isEmpty() == true) goto L13;
        new AlertDialog.Builder(this).setTitle("Enlace de la copia").setMessage(r5.link).setPositiveButton("Copiar", new BackupActivity$$ExternalSyntheticLambda16(this, r5)).setNegativeButton("Cerrar", null).show();
        return;
    L13:
        return;
    L8:
        r2 = "\n" + r5.warning;
        goto L9
    }

    private /* synthetic */ void lambda$execute$29(File r4, int r5) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.busy = false;
        buttons(true);
        this.status.setText("✓ Copia guardada: " + r4.getName());
        reveal();
        if (r5 != 84) goto L9;
        shareBundle(r4);
        return;
    }

    private /* synthetic */ void lambda$execute$30(boolean r4, Exception r5) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.busy = false;
        buttons(true);
        TextView r0 = this.status;
        if (r4 == false) goto L8;
        String r1 = "La copia está guardada abajo. No se pudo completar la subida a Dropbox; revisa cuenta y conexión.";
    L13:
        r0.setText(r1);
        reveal();
        return;
    L8:
        StringBuilder r2 = new StringBuilder().append("No se pudo crear/restaurar la copia: ");
        if (r5.getMessage() != null) goto L11;
        String r3 = "comprueba contraseña y espacio disponible";
    L12:
        r1 = r2.append(r3).toString();
        goto L13
    L11:
        r3 = r5.getMessage();
        goto L12
    }

    private /* synthetic */ void lambda$execute$31(final int r6, Uri r7, final String r8) {
        if (r6 != 82) goto L24;
        InputStream r1 = getContentResolver().openInputStream(r7);     // Catch: Exception -> L21
        if (r1 == null) goto L14;
        FullBackupArchive.restore(this, r1, r8);     // Catch: Throwable -> L11
        if (r1 == null) goto L9;
        r1.close();     // Catch: Exception -> L21
    L9:
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda8(this));     // Catch: Exception -> L21
        return;
    L14:
        throw new IOException();     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        if (r1 != null) goto L42;
    L20:
        throw th;     // Catch: Exception -> L21
    L42:
        r1.close();     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L21
        goto L20
    L24:
        if (r6 != 81) goto L26;
        String r2 = "datos";
    L27:
        if (r6 != 81) goto L29;
        String r3 = ".chdbak";
    L30:
        final File r4 = BackupStore.create(this, r2, r3, new BackupActivity$$ExternalSyntheticLambda9(this, r6, r8));     // Catch: Exception -> L21
        if (r6 != 85) goto L33;
        final DropboxBackupClient.Result r5 = new DropboxBackupClient(DropboxBackupAccount.load(this)).upload(r4, this.publicLink, new BackupActivity$$ExternalSyntheticLambda10(this));     // Catch: Exception -> L21
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda12(this, r5));     // Catch: Exception -> L21
        return;
    L33:
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda13(this, r4, r6));     // Catch: Exception -> L21
        return;
    L29:
        r3 = ".zip";
        goto L30
    L26:
        r2 = "completa";
    L21:
        e = move-exception;
        if (0 == 0) goto L37;
        final boolean r9 = true;
    L38:
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda14(this, r9, e));
        return;
    L37:
        r9 = false;
        goto L38
    }

    private /* synthetic */ void lambda$exportCopy$20() {
        this.busy = false;
        buttons(true);
        this.status.setText("✓ Copia guardada fuera. El original sigue disponible abajo.");
        reveal();
    }

    private /* synthetic */ void lambda$exportCopy$21() {
        this.busy = false;
        buttons(true);
        this.status.setText("No se pudo guardar fuera. La copia original sigue guardada aquí.");
        reveal();
    }

    private /* synthetic */ void lambda$exportCopy$22(File r5, Uri r6) {
        FileInputStream r0 = new FileInputStream(r5);     // Catch: Exception -> L27
        OutputStream r1 = getContentResolver().openOutputStream(r6, "wt");     // Catch: Throwable -> L21
        if (r1 == null) goto L14;
        BackupArchive.copy(r0, r1, 1342177408);     // Catch: Throwable -> L11
        if (r1 == null) goto L9;
        r1.close();     // Catch: Throwable -> L21
    L9:
        r0.close();     // Catch: Exception -> L27
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda17(this));     // Catch: Exception -> L27
        return;
    L14:
        throw new IOException();     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        if (r1 != null) goto L37;
    L20:
        throw th;     // Catch: Throwable -> L21
    L37:
        r1.close();     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        r0.close();     // Catch: Throwable -> L24
    L26:
        throw th;     // Catch: Exception -> L27
    L24:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L27
    L35:
        DocumentsContract.deleteDocument(getContentResolver(), r6);     // Catch: Exception -> L30
    L31:
        runOnUiThread(new BackupActivity$$ExternalSyntheticLambda18(this));
    }

    private /* synthetic */ void lambda$onActivityResult$18(Uri r2) {
        execute(82, r2);
    }

    static /* synthetic */ WindowInsets lambda$onCreate$0(int r4, View r5, WindowInsets r6) {
        r5.setPadding(r6.getSystemWindowInsetLeft() + r4, r6.getSystemWindowInsetTop() + r4, r6.getSystemWindowInsetRight() + r4, r6.getSystemWindowInsetBottom() + r4);
        return r6;
    }

    private /* synthetic */ void lambda$onCreate$1(View r2) {
        if (this.busy == true) goto L6;
        finish();
        return;
    }

    private /* synthetic */ void lambda$onCreate$10(View r3) {
        if (this.busy == true) goto L6;
        startActivity(new Intent(this, TimedAccessActivity.class));
        return;
    }

    private /* synthetic */ void lambda$onCreate$11(View r2) {
        this.restoreFile = null;
        password(true);
    }

    private /* synthetic */ void lambda$onCreate$12() {
        openPanelToolAction(getIntent().getStringExtra("panelTool"));
    }

    private /* synthetic */ void lambda$onCreate$2(View r2) {
        this.exportMode = 81;
        password(false);
    }

    private /* synthetic */ void lambda$onCreate$3(View r2) {
        this.exportMode = 83;
        password(false);
    }

    private /* synthetic */ void lambda$onCreate$4(View r2) {
        this.exportMode = 84;
        password(false);
    }

    private /* synthetic */ void lambda$onCreate$5() {
        this.status.setText("Configuración Dropbox guardada");
    }

    private /* synthetic */ void lambda$onCreate$6(CheckBox r2, DialogInterface r3, int r4) {
        this.publicLink = r2.isChecked();
        this.exportMode = 85;
        password(false);
    }

    private /* synthetic */ void lambda$onCreate$7(View r5) {
        if (this.busy == false) goto L6;
        return;
    L6:
        if (DropboxBackupAccount.configured(this) == true) goto L9;
        DropboxBackupAccount.settings(this, new BackupActivity$$ExternalSyntheticLambda19(this));
        return;
    L9:
        final CheckBox r0 = new CheckBox(this);
        r0.setText("Crear enlace público de descarga (opcional)");
        new AlertDialog.Builder(this).setTitle("Subir a Dropbox").setMessage("Se sobrescribirá la copia en la ruta configurada. Mantén la app abierta hasta terminar.").setView(r0).setPositiveButton("Continuar", new BackupActivity$$ExternalSyntheticLambda20(this, r0)).setNegativeButton("Cancelar", null).show();
    }

    private /* synthetic */ void lambda$onCreate$8() {
        TextView r0 = this.status;
        if (DropboxBackupAccount.configured(this) == false) goto L5;
        String r1 = "Dropbox configurado";
    L6:
        r0.setText(r1);
        return;
    L5:
        r1 = "Dropbox desconectado";
        goto L6
    }

    private /* synthetic */ void lambda$onCreate$9(View r2) {
        if (this.busy == true) goto L6;
        DropboxBackupAccount.settings(this, new BackupActivity$$ExternalSyntheticLambda3(this));
        return;
    }

    static /* synthetic */ void lambda$onResume$13() {
    }

    private /* synthetic */ void lambda$password$14(DialogInterface r3, int r4) {
        if (this.restoreFile == null) goto L5;
        execute(82, Uri.fromFile(this.restoreFile));
        return;
    L5:
        picker(true);
    }

    private /* synthetic */ void lambda$password$15(DialogInterface r2, int r3) {
        this.password = null;
    }

    private /* synthetic */ void lambda$password$16(EditText r5, boolean r6, EditText r7, AlertDialog r8, View r9) {
        String r0 = r5.getText().toString();
        if (r0.length() < 8) goto L13;
        if (r6 == false) goto L6;
    L8:
        this.password = r0;
        r8.dismiss();
        if (r6 == false) goto L11;
        new AlertDialog.Builder(this).setTitle("Restaurar sustituye los datos actuales").setMessage("Se recuperarán también el PIN y los ajustes guardados en la copia.").setPositiveButton("Elegir copia", new BackupActivity$$ExternalSyntheticLambda21(this)).setNegativeButton("Cancelar", new BackupActivity$$ExternalSyntheticLambda23(this)).show();
        return;
    L11:
        execute(this.exportMode, null);
        return;
    L6:
        if (r0.equals(r7.getText().toString()) == true) goto L8;
    L13:
        r5.setError("Mínimo 8 caracteres; ambas contraseñas deben coincidir");
    }

    private /* synthetic */ void lambda$password$17(final AlertDialog r8, final EditText r9, final boolean r10, final EditText r11, DialogInterface r12) {
        r8.getButton(-1).setOnClickListener(new BackupActivity$$ExternalSyntheticLambda15(this, r9, r10, r11, r8));
    }

    private /* synthetic */ void lambda$reveal$19() {
        this.page.smoothScrollTo(0, this.status.getTop());
    }

    private void password(final boolean r9) {
        if (this.busy == false) goto L6;
        return;
    L6:
        if (this.settingsPanel == null) goto L9;
        this.settingsPanel.hide();
    L9:
        if (TimedAccess.limited(this) == false) goto L13;
        if (r9 == true) goto L13;
        this.status.setText("El acceso temporal no permite exportar los datos cedidos");
        return;
    L13:
        LinearLayout r0 = new LinearLayout(this);
        r0.setOrientation(1);
        final EditText r1 = new EditText(this);
        r1.setHint("Contraseña (mínimo 8 caracteres)");
        r1.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        r0.addView(r1);
        final EditText r7 = new EditText(this);
        r7.setHint("Repetir contraseña");
        r7.setInputType(r1.getInputType());
        if (r9 == true) goto L16;
        r0.addView(r7);
    L16:
        AlertDialog.Builder r2 = new AlertDialog.Builder(this);
        if (r9 == false) goto L19;
        String r3 = "Restaurar copia";
    L20:
        final AlertDialog r4 = r2.setTitle(r3).setView(r0).setPositiveButton("Continuar", null).setNegativeButton("Cancelar", null).create();
        r4.setOnShowListener(new BackupActivity$$ExternalSyntheticLambda7(this, r4, r1, r9, r7));
        r4.show();
        return;
    L19:
        r3 = "Contraseña de la copia";
        goto L20
    }

    private void picker(boolean r4) {
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT").setType("*/*").addCategory("android.intent.category.OPENABLE"), 82);     // Catch: ActivityNotFoundException -> L4
        return;
    L5:
        this.password = null;
        this.status.setText("Esta TV no tiene selector de archivos. Puedes restaurar las copias guardadas abajo.");
        reveal();
    }

    private void reveal() {
        if (this.history == null) goto L6;
        this.history.refresh();
    L6:
        if (this.page == null) goto L9;
        this.page.post(new BackupActivity$$ExternalSyntheticLambda4(this));
        return;
    }

    private void shareBundle(File r5) {
        Intent r0 = BackupStore.share(this, r5);     // Catch: Exception -> L8
        startActivity(new Intent(r0).setPackage("com.dropbox.android"));     // Catch: ActivityNotFoundException -> L5 Exception -> L8
        this.status.setText("Copia preparada. Confirma la carpeta y la subida en Dropbox.");     // Catch: ActivityNotFoundException -> L5 Exception -> L8
    L11:
        return;
    L6:
        startActivity(Intent.createChooser(r0, "Subir copia completa · elige Dropbox"));     // Catch: Exception -> L8
        this.status.setText("Copia preparada. Elige Dropbox u otra app para guardarla.");     // Catch: Exception -> L8
    L9:
        this.status.setText("No hay una app compatible para compartir. La copia ya está guardada y aparece abajo.");
    }

    static Intent shareIntent(Context r3, File r4) {
        Uri r0 = FileProvider.getUriForFile(r3, r3.getPackageName() + ".backups", r4);
        Intent r1 = new Intent("android.intent.action.SEND").setType("application/zip").putExtra("android.intent.extra.STREAM", r0).addFlags(1);
        r1.setClipData(ClipData.newRawUri("Copia completa CHDTV", r0));
        return r1;
    }

    @Override // es.antonio.m3u.ChdtvScreen
    protected String chromeSelection() {
        if (getIntent().getBooleanExtra("dropbox", false) == false) goto L5;
        return "Dropbox";
    L5:
        return "Copia de seguridad";
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r4, int r5, Intent r6) {
        super.onActivityResult(r4, r5, r6);
        if (r4 == 82) goto L7;
        if (r4 == 86) goto L7;
        return;
    L7:
        if (r5 != (-1)) goto L23;
        if (r6 == null) goto L23;
        if (r6.getData() == null) goto L23;
        if (r4 != 86) goto L18;
        File r0 = this.exportFile;
        this.exportFile = null;
        if (r0 == null) goto L25;
        exportCopy(r0, r6.getData());
        return;
    L25:
        return;
    L18:
        if (this.password != null) goto L21;
        this.status.setText("Repite la operación para introducir la contraseña");
        return;
    L21:
        final Uri r1 = r6.getData();
        ParentalControl.authenticate(this, new BackupActivity$$ExternalSyntheticLambda6(this, r1));
        return;
    L23:
        this.password = null;
        this.exportFile = null;
        this.status.setText("Selección cancelada. Las copias guardadas siguen disponibles abajo.");
        reveal();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onBackPressed() {
        if (closeNavigation() == false) goto L6;
        return;
    L6:
        if (this.settingsPanel == null) goto L11;
        if (this.settingsPanel.closeIfOpen() == false) goto L11;
        return;
    L11:
        if (this.busy == true) goto L14;
        super.onBackPressed();
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r22) {
        super.onCreate(r22);
        if (r22 == null) goto L9;
        this.password = r22.getString("password");
        String r2 = r22.getString("exportFile");
        String r3 = r22.getString("restoreFile");
        if (r2 == null) goto L7;
        this.exportFile = new File(getFilesDir(), "backups/" + r2);
    L7:
        if (r3 == null) goto L9;
        this.restoreFile = new File(getFilesDir(), "backups/" + r3);
    L9:
        LinearLayout r4 = new LinearLayout(this);
        r4.setOrientation(1);
        final int r5 = (int) (getResources().getDisplayMetrics().density * 20.0f);
        r4.setPadding(r5, r5, r5, r5);
        r4.setBackgroundColor(-16180435);
        ScrollView r6 = new ScrollView(this);
        this.page = r6;
        r6.setFillViewport(true);
        r6.addView(r4);
        setContentView(r6);
        r4.setOnApplyWindowInsetsListener(new BackupActivity$$ExternalSyntheticLambda11(r5));
        Button r7 = new Button(this);
        r7.setText("‹ Volver");
        r7.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda22(this));
        r4.addView(r7);
        TextView r8 = new TextView(this);
        r8.setText("Copia de seguridad");
        r8.setTextSize(24.0f);
        r8.setTextColor(-12986907);
        r4.addView(r8);
        TextView r9 = new TextView(this);
        r9.setText("Guarda listas, proveedores, favoritos, EPG, Plex, ajustes, PIN y posiciones de reproducción.\n\nLa copia se cifra con una contraseña: consérvala para restaurar. La copia completa añade el APK dentro de un ZIP. No incluye vídeos ni cuentas protegidas de Movistar/DAZN: vuelve a iniciarlas al restaurar. Los permisos para archivos locales deben concederse de nuevo en otro dispositivo.");
        r9.setTextColor(-1182209);
        r4.addView(r9);
        this.save = new Button(this);
        this.save.setText("Crear copia cifrada");
        this.save.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda25(this));
        r4.addView(this.save);
        this.full = new Button(this);
        this.full.setText("Guardar copia completa · APK + datos");
        this.full.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda26(this));
        r4.addView(this.full);
        this.share = new Button(this);
        this.share.setText("Compartir copia completa con otra app");
        this.share.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda27(this));
        r4.addView(this.share);
        this.direct = new Button(this);
        this.direct.setText("Subir copia completa a Dropbox · directo");
        this.direct.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda28(this));
        r4.addView(this.direct);
        this.account = new Button(this);
        this.account.setText("Configurar Dropbox · APP_KEY / REFRESH_TOKEN");
        this.account.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda29(this));
        r4.addView(this.account);
        Button r10 = new Button(this);
        r10.setText("Acceso temporal · crear o introducir código");
        r10.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda30(this));
        r4.addView(r10);
        this.restore = new Button(this);
        this.restore.setText("Restaurar copia");
        this.restore.setOnClickListener(new BackupActivity$$ExternalSyntheticLambda31(this));
        r4.addView(this.restore);
        this.status = new TextView(this);
        this.status.setTextColor(-9181523);
        r4.addView(this.status);
        this.history = new BackupHistoryView(this, new C09691(this));
        r4.addView(this.history);
        r4.removeView(this.save);
        r4.removeView(this.full);
        LinearLayout r11 = new LinearLayout(this);
        int r12 = 0;
        if ((getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density) < 700.0f) goto L12;
        int r13 = 0;
    L13:
        r11.setOrientation(r13);
        r4.addView(r11, r4.indexOfChild(r9) + 1);
        int r15 = 2;
        Button[] r14 = {this.save, this.full};
        int r16 = 0;
    L14:
        if (r16 >= r15) goto L24;
        Button r17 = r14[r16];
        int r18 = r15;
        if (r11.getOrientation() == 0) goto L20;
        r12 = -1;
    L20:
        if (r11.getOrientation() != 0) goto L22;
        float r19 = 1.0f;
    L23:
        LinearLayout.LayoutParams r110 = new LinearLayout.LayoutParams(r12, -2, r19);
        r110.setMargins(0, r5 / 2, r5 / 2, r5 / 2);
        r11.addView(r17, r110);
        r16 = r16 + 1;
        r15 = r18;
        r14 = r14;
        r5 = r5;
        r12 = 0;
        goto L14
    L22:
        r19 = 0.0f;
        goto L23
    L24:
        SettingsSidePanel.style(this.save);
        this.save.setActivated(true);
        SettingsSidePanel.style(this.full);
        this.save.setText("▣  Crear copia cifrada");
        this.full.setText("▤  Copia completa · APK + datos");
        this.settingsPanel = new SettingsSidePanel(this, r4, r6, r7);
        setContentView(this.settingsPanel);
        Button r1 = this.share;
        Button r20 = this.direct;
        Button r21 = this.account;
        Button r111 = this.restore;
        Button[] r112 = new Button[5];
        r112[0] = r1;
        r112[1] = r20;
        r112[r15] = r21;
        r112[3] = r10;
        r112[4] = r111;
        int r113 = 0;
    L25:
        if (r113 >= 5) goto L27;
        this.settingsPanel.addAction(r112[r113]);
        r113 = r113 + 1;
        goto L25
    L27:
        if (r22 == null) goto L29;
        return;
    L29:
        if (getIntent().hasExtra("panelTool") == false) goto L37;
        new Handler(Looper.getMainLooper()).post(new BackupActivity$$ExternalSyntheticLambda1(this));
        return;
    L37:
        return;
    L12:
        r13 = 1;
        goto L13
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.worker.shutdown();
        this.password = null;
        super.onDestroy();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.history == null) goto L5;
        this.history.refresh();
    L5:
        ParentalControl.authenticate(this, new BackupActivity$$ExternalSyntheticLambda0(), true);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r3) {
        super.onSaveInstanceState(r3);
        r3.putString("password", this.password);
        if (this.exportFile == null) goto L6;
        r3.putString("exportFile", this.exportFile.getName());
    L6:
        if (this.restoreFile == null) goto L9;
        r3.putString("restoreFile", this.restoreFile.getName());
        return;
    }

    void openPanelToolAction(String r2) {
        if (r2.equals("Crear copia cifrada") == false) goto L7;
        Button r0 = this.save;
    L5:
        r0.performClick();
        return;
    L7:
        if (r2.equals("Copia completa · APK + datos") == false) goto L10;
        r0 = this.full;
        goto L5
    L10:
        if (r2.equals("Compartir copia completa") == false) goto L13;
        r0 = this.share;
        goto L5
    L13:
        if (r2.equals("Restaurar copia") == false) goto L16;
        r0 = this.restore;
        goto L5
    L16:
        if (r2.equals("Configurar Dropbox") == false) goto L19;
        r0 = this.account;
        goto L5
    L19:
        if (r2.equals("Subir copia completa a Dropbox") == false) goto L21;
        r0 = this.direct;
        goto L5
    L21:
        reveal();
    }
}
