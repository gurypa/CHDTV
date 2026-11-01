package es.antonio.m3u;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import com.arthenica.ffmpegkit.MediaInformation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes3.dex */
public final class TimedAccessActivity extends ChdtvScreen {
    private boolean busy;
    private EditText code;
    private Button create;
    private long duration;
    private File exportFile;
    private BackupHistoryView history;
    private Button importButton;
    private ScrollView page;
    private String pending;
    private SettingsSidePanel settingsPanel;
    private TextView status;
    private final ExecutorService worker;

    /* JADX INFO: renamed from: es.antonio.m3u.TimedAccessActivity$1 */
    class C10361 implements BackupHistoryView.Actions {
        final /* synthetic */ TimedAccessActivity this$0;

        public static /* synthetic */ void $r8$lambda$ykX0JQfT_rMQHlBwwtcXMSozIuA(C10361 r0, File r1, String r2, DialogInterface r3, int r4) {
            r0.lambda$restore$0(r1, r2, r3, r4);
        }

        C10361(TimedAccessActivity r1) {
            this.this$0 = r1;
        }

        private /* synthetic */ void lambda$restore$0(File r3, String r4, DialogInterface r5, int r6) {
            TimedAccessActivity.m801$$Nest$mrestoreCopy(this.this$0, Uri.fromFile(r3), r4);
        }

        @Override // es.antonio.m3u.BackupHistoryView.Actions
        public boolean busy() {
            return TimedAccessActivity.m797$$Nest$fgetbusy(this.this$0);
        }

        @Override // es.antonio.m3u.BackupHistoryView.Actions
        public void export(File r2) {
            TimedAccessActivity.m800$$Nest$mexportSaved(this.this$0, r2);
        }

        @Override // es.antonio.m3u.BackupHistoryView.Actions
        public void restore(final File r6) {
            if (r6.getName().endsWith(".chdtime") == true) goto L6;
            TimedAccessActivity.m799$$Nest$fgetstatus(this.this$0).setText("Esta copia es normal. Restáurala en Copia de seguridad.");
            TimedAccessActivity.m802$$Nest$mreveal(this.this$0);
            return;
        L6:
            String r0 = BackupStore.code(this.this$0, r6);
            if (r0.isEmpty() == false) goto L9;
            r0 = TimedAccessActivity.m798$$Nest$fgetcode(this.this$0).getText().toString().trim();
        L9:
            final String r1 = r0;
            new AlertDialog.Builder(this.this$0).setTitle("Restaurar esta copia temporal").setMessage("Sustituye listas, PIN y ajustes. Esta instalación quedará limitada a la fecha de vencimiento de la copia.").setPositiveButton("Restaurar", new TimedAccessActivity$1$$ExternalSyntheticLambda0(this, r6, r1)).setNegativeButton("Cancelar", null).show();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$18zM1CTEXl_OUMVaBr1-_M7BsYo, reason: not valid java name */
    public static /* synthetic */ void m787$r8$lambda$18zM1CTEXl_OUMVaBr1_M7BsYo(TimedAccessActivity r0) {
        r0.lambda$restoreCopy$22();
    }

    /* JADX INFO: renamed from: $r8$lambda$74e_drGJUUBQYbJNAX-sj2DqsvI, reason: not valid java name */
    public static /* synthetic */ void m788$r8$lambda$74e_drGJUUBQYbJNAXsj2DqsvI(TimedAccessActivity r0) {
        r0.lambda$onCreate$8();
    }

    /* JADX INFO: renamed from: $r8$lambda$7MncZwE0oZgv-PXtmsUw8v_H2os, reason: not valid java name */
    public static /* synthetic */ void m789$r8$lambda$7MncZwE0oZgvPXtmsUw8v_H2os(TimedAccessActivity r0, File r1, Uri r2) {
        r0.lambda$copyOutside$21(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$A18hdm27tIEHrPtHrdBLcLcu24A(TimedAccessActivity r0, View r1) {
        r0.lambda$onCreate$0(r1);
    }

    public static /* synthetic */ void $r8$lambda$D1NM383A9WQbq3MKZ1j3Ki1lZro(TimedAccessActivity r0, AlertDialog r1, EditText r2, Spinner r3, DialogInterface r4) {
        r0.lambda$custom$12(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$D5hhjwyn5po25kPdwF1MkzfwUt8(TimedAccessActivity r0) {
        r0.lambda$copyOutside$20();
    }

    public static /* synthetic */ void $r8$lambda$DF9BQNGShlztZj4bG2nwZSJMvCg(TimedAccessActivity r0, View r1) {
        r0.lambda$onCreate$4(r1);
    }

    public static /* synthetic */ void $r8$lambda$EOWjETGQxI1WhwOTsy89cJylD6I(TimedAccessActivity r0, Exception r1) {
        r0.lambda$restoreCopy$23(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$M72k3ZgIa-icqNLcfXJh5QiKJ_U, reason: not valid java name */
    public static /* synthetic */ void m790$r8$lambda$M72k3ZgIaicqNLcfXJh5QiKJ_U(TimedAccessActivity r0, DialogInterface r1, int r2) {
        r0.lambda$onCreate$5(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$PWMg6jK-tK8rx2UcESaD9nSR1T8, reason: not valid java name */
    public static /* synthetic */ void m791$r8$lambda$PWMg6jKtK8rx2UcESaD9nSR1T8(TimedAccessActivity r0, String r1, long r2) {
        r0.lambda$export$18(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$PixOFtcBTzqDuoqNSYVd5aeFkJ4(TimedAccessActivity r0, EditText r1, Spinner r2, AlertDialog r3, View r4) {
        r0.lambda$custom$11(r1, r2, r3, r4);
    }

    public static /* synthetic */ void $r8$lambda$U8rkLEJOJMN907o5diEuXhcyb9c(TimedAccessActivity r0, View r1) {
        r0.lambda$onCreate$6(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$V08TUI1hatozlEsCVhJS-uK3NWg, reason: not valid java name */
    public static /* synthetic */ void m792$r8$lambda$V08TUI1hatozlEsCVhJSuK3NWg(TimedAccessActivity r0, Uri r1, String r2) {
        r0.lambda$restoreCopy$24(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$Yf8oSl9giAISap4eS39hWumCtnY(TimedAccessActivity r0, DialogInterface r1, int r2) {
        r0.lambda$onCreate$3(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$_tYYPj-HsQgm5I2dcQ3DDOBJ8vI, reason: not valid java name */
    public static /* synthetic */ void m793$r8$lambda$_tYYPjHsQgm5I2dcQ3DDOBJ8vI(TimedAccessActivity r0) {
        r0.lambda$reveal$13();
    }

    /* JADX INFO: renamed from: $r8$lambda$bTfHDzS5XyoP5naNn6rtqmO_L-Y, reason: not valid java name */
    public static /* synthetic */ void m794$r8$lambda$bTfHDzS5XyoP5naNn6rtqmO_LY(TimedAccessActivity r0, Exception r1) {
        r0.lambda$export$17(r1);
    }

    public static /* synthetic */ void $r8$lambda$dRfHuqJMcr0pv_B2VP_pX5gMJBs(TimedAccessActivity r0, View r1) {
        r0.lambda$onCreate$7(r1);
    }

    public static /* synthetic */ void $r8$lambda$hyqrNBFMm7ovuTd6eCKhp3QxTq8(TimedAccessActivity r0, DialogInterface r1, int r2) {
        r0.lambda$chooseDuration$10(r1, r2);
    }

    public static /* synthetic */ void $r8$lambda$n0xNANCSKzAAV2GCSsQwFUJS4LU(TimedAccessActivity r0) {
        r0.lambda$copyOutside$19();
    }

    public static /* synthetic */ void $r8$lambda$qfxrRgw9loQj2tmcX_oMUzfWXpU(TimedAccessActivity r0, String r1, DialogInterface r2, int r3) {
        r0.lambda$onCreate$1(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$tIZnkJO7uHw7w7vJQSzR6OmYjZE(TimedAccessActivity r0, String r1, DialogInterface r2, int r3) {
        r0.lambda$export$15(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$uiQ7VOPxVc_bT3L_ifUA6YSjadU(TimedAccessActivity r0, long[] r1, String r2, long r3, OutputStream r5) {
        r0.lambda$export$14(r1, r2, r3, r5);
    }

    /* JADX INFO: renamed from: $r8$lambda$w2Jw-xP23RI9y_yjfFRwo6-c9aM, reason: not valid java name */
    public static /* synthetic */ void m795$r8$lambda$w2JwxP23RI9y_yjfFRwo6c9aM(TimedAccessActivity r0, File r1, String r2) {
        r0.lambda$export$16(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$xQRQ-4oAdkBr6aZc67KbpS_SuDs, reason: not valid java name */
    public static /* synthetic */ void m796$r8$lambda$xQRQ4oAdkBr6aZc67KbpS_SuDs(TimedAccessActivity r0, View r1) {
        r0.lambda$onCreate$2(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$fgetbusy, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m797$$Nest$fgetbusy(TimedAccessActivity r0) {
        return r0.busy;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetcode, reason: not valid java name */
    static /* bridge */ /* synthetic */ EditText m798$$Nest$fgetcode(TimedAccessActivity r0) {
        return r0.code;
    }

    /* JADX INFO: renamed from: -$$Nest$fgetstatus, reason: not valid java name */
    static /* bridge */ /* synthetic */ TextView m799$$Nest$fgetstatus(TimedAccessActivity r0) {
        return r0.status;
    }

    /* JADX INFO: renamed from: -$$Nest$mexportSaved, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m800$$Nest$mexportSaved(TimedAccessActivity r0, File r1) {
        r0.exportSaved(r1);
    }

    /* JADX INFO: renamed from: -$$Nest$mrestoreCopy, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m801$$Nest$mrestoreCopy(TimedAccessActivity r0, Uri r1, String r2) {
        r0.restoreCopy(r1, r2);
    }

    /* JADX INFO: renamed from: -$$Nest$mreveal, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m802$$Nest$mreveal(TimedAccessActivity r0) {
        r0.reveal();
    }

    public TimedAccessActivity() {
        this.worker = Executors.newSingleThreadExecutor();
    }

    private void chooseDuration() {
        if (this.busy == false) goto L5;
        return;
    L5:
        if (TimedAccess.limited(this) == false) goto L8;
        return;
    L8:
        if (this.settingsPanel == null) goto L10;
        this.settingsPanel.hide();
    L10:
        new AlertDialog.Builder(this).setTitle("Duración desde la generación").setItems(new String[]{"1 hora", "24 horas", "1 año (365 días)", "Personalizada"}, new TimedAccessActivity$$ExternalSyntheticLambda16(this)).show();
    }

    private void copyOutside(final File r3, final Uri r4) {
        working(true, "Guardando copia fuera…");
        this.worker.execute(new TimedAccessActivity$$ExternalSyntheticLambda9(this, r3, r4));
    }

    private void custom() {
        LinearLayout r0 = new LinearLayout(this);
        r0.setOrientation(1);
        final EditText r1 = new EditText(this);
        r1.setHint("Cantidad");
        r1.setInputType(2);
        r1.setText(IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
        r0.addView(r1);
        final Spinner r2 = new Spinner(this);
        r2.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Minutos", "Horas", "Días", "Años (365 días)"}));
        r0.addView(r2);
        final AlertDialog r3 = new AlertDialog.Builder(this).setTitle("De 1 minuto a 10 años").setView(r0).setPositiveButton("Generar", null).setNegativeButton("Cancelar", null).create();
        r3.setOnShowListener(new TimedAccessActivity$$ExternalSyntheticLambda15(this, r3, r1, r2));
        r3.show();
    }

    private void export() {
        if (this.busy == false) goto L5;
        return;
    L5:
        final String r0 = TimedBackupArchive.newCode();
        final long r1 = this.duration;
        working(true, "Generando copia temporal dentro de CHDTV…");
        this.worker.execute(new TimedAccessActivity$$ExternalSyntheticLambda11(this, r0, r1));
    }

    private void exportSaved(File r4) {
        this.exportFile = r4;
        startActivityForResult(new Intent("android.intent.action.CREATE_DOCUMENT").setType(BackupStore.mime(r4)).addCategory("android.intent.category.OPENABLE").putExtra("android.intent.extra.TITLE", r4.getName()), 96);     // Catch: ActivityNotFoundException -> L5
        return;
    L6:
        this.exportFile = null;
        this.status.setText("Esta TV no tiene selector para guardar fuera. La copia ya está guardada aquí; puedes compartirla.");
        reveal();
    }

    private /* synthetic */ void lambda$chooseDuration$10(DialogInterface r4, int r5) {
        if (r5 >= 3) goto L5;
        this.duration = new long[]{3600000, 86400000, 31536000000L}[r5];
        export();
        return;
    L5:
        custom();
    }

    private /* synthetic */ void lambda$copyOutside$19() {
        if (isDestroyed() == true) goto L6;
        working(false, "✓ Copia guardada fuera. El original y su código siguen disponibles aquí.");
        return;
    }

    private /* synthetic */ void lambda$copyOutside$20() {
        if (isDestroyed() == true) goto L6;
        working(false, "No se pudo guardar fuera. El original y su código siguen guardados aquí.");
        return;
    }

    private /* synthetic */ void lambda$copyOutside$21(File r5, Uri r6) {
        FileInputStream r0 = new FileInputStream(r5);     // Catch: Exception -> L27
        OutputStream r1 = getContentResolver().openOutputStream(r6, "wt");     // Catch: Throwable -> L21
        if (r1 == null) goto L14;
        BackupArchive.copy(r0, r1, 1342177408);     // Catch: Throwable -> L11
        if (r1 == null) goto L9;
        r1.close();     // Catch: Throwable -> L21
    L9:
        r0.close();     // Catch: Exception -> L27
        runOnUiThread(new TimedAccessActivity$$ExternalSyntheticLambda10(this));     // Catch: Exception -> L27
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
        runOnUiThread(new TimedAccessActivity$$ExternalSyntheticLambda12(this));
    }

    private /* synthetic */ void lambda$custom$11(EditText r6, Spinner r7, AlertDialog r8, View r9) {
        this.duration = TimedAccessActivity$$ExternalSyntheticBackport0.m409m(Long.parseLong(r6.getText().toString()), new long[]{60000, 3600000, 86400000, 31536000000L}[r7.getSelectedItemPosition()]);     // Catch: Exception -> L9
        if (this.duration < 60000) goto L8;
        if (this.duration > 315360000000L) goto L8;
        r8.dismiss();     // Catch: Exception -> L9
        export();     // Catch: Exception -> L9
        return;
    L8:
        throw new IllegalArgumentException();     // Catch: Exception -> L9
    L10:
        r6.setError("Duración no válida");
    }

    private /* synthetic */ void lambda$custom$12(final AlertDialog r3, final EditText r4, final Spinner r5, DialogInterface r6) {
        r3.getButton(-1).setOnClickListener(new TimedAccessActivity$$ExternalSyntheticLambda14(this, r4, r5, r3));
    }

    private /* synthetic */ void lambda$export$14(long[] r4, String r5, long r6, OutputStream r8) throws Exception {
        r4[0] = TimedBackupArchive.write(this, r8, r5, r6);
    }

    private /* synthetic */ void lambda$export$15(String r3, DialogInterface r4, int r5) {
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Acceso temporal CHDTV", r3));
    }

    private /* synthetic */ void lambda$export$16(File r4, final String r5) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        working(false, "✓ Copia temporal guardada: " + r4.getName());
        new AlertDialog.Builder(this).setTitle("Copia generada · comparte archivo y código").setMessage(r5 + "\n\nLa copia y su vencimiento aparecen abajo en Copias guardadas.").setPositiveButton("Copiar código", new TimedAccessActivity$$ExternalSyntheticLambda2(this, r5)).setNegativeButton("Cerrar", null).show();
    }

    private /* synthetic */ void lambda$export$17(Exception r3) {
        if (isDestroyed() == true) goto L10;
        StringBuilder r0 = new StringBuilder().append("No se pudo generar la copia: ");
        if (r3.getMessage() != null) goto L7;
        String r1 = "comprueba espacio disponible";
    L8:
        working(false, r0.append(r1).toString());
        return;
    L7:
        r1 = r3.getMessage();
        goto L8
    }

    private /* synthetic */ void lambda$export$18(final String r11, final long r12) {
        final long[] r0 = {0};     // Catch: Exception -> L9
        final File r13 = BackupStore.create(this, "temporal", ".chdtime", new TimedAccessActivity$$ExternalSyntheticLambda3(this, r0, r11, r12));     // Catch: Exception -> L7
        BackupStore.record(this, r13, r11, r0[0]);     // Catch: Exception -> L7
        getSharedPreferences("timed-issued-codes", 0).edit().putString("last", r11).commit();     // Catch: Exception -> L7
        runOnUiThread(new TimedAccessActivity$$ExternalSyntheticLambda4(this, r13, r11));     // Catch: Exception -> L7
        return;
    L7:
        e = move-exception;
        final Exception r14 = e;
    L11:
        runOnUiThread(new TimedAccessActivity$$ExternalSyntheticLambda5(this, r14));
        return;
    L9:
        e = move-exception;
        r14 = e;
        goto L11
    }

    private /* synthetic */ void lambda$onCreate$0(View r1) {
        chooseDuration();
    }

    private /* synthetic */ void lambda$onCreate$1(String r3, DialogInterface r4, int r5) {
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Código CHDTV", r3));
    }

    private /* synthetic */ void lambda$onCreate$2(View r5) {
        final String r0 = BackupStore.lastCode(this);
        if (r0.isEmpty() == false) goto L6;
        this.status.setText("Todavía no hay un código generado. Elige Generar copia y una duración.");
        reveal();
        return;
    L6:
        new AlertDialog.Builder(this).setTitle("Último código (comparte también su copia)").setMessage(r0).setPositiveButton("Copiar", new TimedAccessActivity$$ExternalSyntheticLambda6(this, r0)).setNegativeButton("Cerrar", null).show();
    }

    private /* synthetic */ void lambda$onCreate$3(DialogInterface r4, int r5) {
        this.pending = this.code.getText().toString().trim();
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT").setType("*/*").addCategory("android.intent.category.OPENABLE"), 92);     // Catch: ActivityNotFoundException -> L5
        return;
    L6:
        this.status.setText("Esta TV no tiene selector de archivos. Puedes elegir Restaurar en una copia guardada abajo.");
        reveal();
    }

    private /* synthetic */ void lambda$onCreate$4(View r4) {
        if (this.busy == false) goto L11;
        return;
    L11:
        TimedBackupArchive.codeKey(this.code.getText().toString().trim());     // Catch: Exception -> L8
        new AlertDialog.Builder(this).setTitle("La copia sustituirá los datos actuales").setMessage("También se restauran el PIN y los ajustes. El acceso a los datos importados caducará en la fecha de la copia.").setPositiveButton("Elegir copia", new TimedAccessActivity$$ExternalSyntheticLambda17(this)).setNegativeButton("Cancelar", null).show();
        return;
    L9:
        this.code.setError("Introduce un código válido");
    }

    private /* synthetic */ void lambda$onCreate$5(DialogInterface r2, int r3) {
        ((ActivityManager) getSystemService(ActivityManager.class)).clearApplicationUserData();
    }

    private /* synthetic */ void lambda$onCreate$6(View r4) {
        if (this.busy == false) goto L5;
        return;
    L5:
        new AlertDialog.Builder(this).setTitle("Eliminar datos de esta instalación").setMessage("Se borrarán listas, favoritos, EPG, cuentas y ajustes. No afecta a la copia del emisor. Esta acción no se puede deshacer.").setPositiveButton("Eliminar", new TimedAccessActivity$$ExternalSyntheticLambda13(this)).setNegativeButton("Cancelar", null).show();
    }

    private /* synthetic */ void lambda$onCreate$7(View r1) {
        onBackPressed();
    }

    private /* synthetic */ void lambda$onCreate$8() {
        openPanelToolAction(getIntent().getStringExtra("panelTool"));
    }

    static /* synthetic */ void lambda$onResume$9() {
    }

    private /* synthetic */ void lambda$restoreCopy$22() {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.busy = false;
        startActivity(new Intent(this, MainActivity.class).addFlags(268468224));
        finish();
    }

    private /* synthetic */ void lambda$restoreCopy$23(Exception r3) {
        if (isDestroyed() == true) goto L10;
        StringBuilder r0 = new StringBuilder().append("No se pudo importar: ");
        if (r3.getMessage() != null) goto L7;
        String r1 = "comprueba copia y código";
    L8:
        working(false, r0.append(r1).toString());
        return;
    L7:
        r1 = r3.getMessage();
        goto L8
    }

    private /* synthetic */ void lambda$restoreCopy$24(Uri r4, String r5) {
        InputStream r0 = getContentResolver().openInputStream(r4);     // Catch: Exception -> L19
        if (r0 == null) goto L12;
        TimedBackupArchive.restore(this, r0, r5);     // Catch: Throwable -> L9
        if (r0 == null) goto L7;
        r0.close();     // Catch: Exception -> L19
    L7:
        runOnUiThread(new TimedAccessActivity$$ExternalSyntheticLambda7(this));     // Catch: Exception -> L19
        return;
    L12:
        throw new IOException();     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        if (r0 != null) goto L23;
    L18:
        throw th;     // Catch: Exception -> L19
    L23:
        r0.close();     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L19
    L19:
        e = move-exception;
        runOnUiThread(new TimedAccessActivity$$ExternalSyntheticLambda8(this, e));
    }

    private /* synthetic */ void lambda$reveal$13() {
        this.page.smoothScrollTo(0, this.status.getTop());
    }

    private void restoreCopy(final Uri r3, final String r4) {
        if (this.busy == false) goto L5;
        return;
    L5:
        working(true, "Validando código y copia…");
        this.worker.execute(new TimedAccessActivity$$ExternalSyntheticLambda20(this, r3, r4));
    }

    private void reveal() {
        if (this.history == null) goto L6;
        this.history.refresh();
    L6:
        if (this.page == null) goto L9;
        this.page.post(new TimedAccessActivity$$ExternalSyntheticLambda18(this));
        return;
    }

    private void working(boolean r3, String r4) {
        if (this.settingsPanel == null) goto L5;
        this.settingsPanel.hide();
    L5:
        this.busy = r3;
        Button r0 = this.create;
        if (r3 == false) goto L8;
    L10:
        boolean r1 = false;
    L11:
        r0.setEnabled(r1);
        this.importButton.setEnabled(!r3);
        this.status.setText(r4);
        reveal();
        return;
    L8:
        if (TimedAccess.limited(this) == true) goto L10;
        r1 = true;
        goto L11
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r4, int r5, Intent r6) {
        super.onActivityResult(r4, r5, r6);
        if (r4 == 92) goto L7;
        if (r4 == 96) goto L7;
        return;
    L7:
        if (r5 != (-1)) goto L24;
        if (r6 == null) goto L24;
        if (r6.getData() == null) goto L24;
        if (r4 != 96) goto L18;
        File r1 = this.exportFile;
        this.exportFile = null;
        if (r1 == null) goto L26;
        copyOutside(r1, r6.getData());
        return;
    L26:
        return;
    L18:
        String r2 = this.pending;
        this.pending = null;
        if (r2 != null) goto L22;
        this.status.setText("Vuelve a introducir el código");
        return;
    L22:
        restoreCopy(r6.getData(), r2);
        return;
    L24:
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
        if (this.busy == false) goto L14;
        return;
    L14:
        if (TimedAccess.allowed(this) == true) goto L17;
        moveTaskToBack(true);
        return;
    L17:
        super.onBackPressed();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r11) {
        super.onCreate(r11);
        if (r11 == null) goto L7;
        this.pending = r11.getString("pending");
        this.duration = r11.getLong(MediaInformation.KEY_DURATION);
        String r0 = r11.getString("exportFile");
        if (r0 == null) goto L7;
        this.exportFile = new File(getFilesDir(), "backups/" + r0);
    L7:
        LinearLayout r1 = new LinearLayout(this);
        r1.setOrientation(1);
        int r2 = (int) (getResources().getDisplayMetrics().density * 24.0f);
        r1.setPadding(r2, r2, r2, r2);
        r1.setBackgroundColor(-16180435);
        ScrollView r4 = new ScrollView(this);
        this.page = r4;
        r4.addView(r1);
        setContentView(r4);
        TextView r5 = new TextView(this);
        r5.setText("CHDTV · Acceso temporal");
        r5.setTextColor(-12986907);
        r5.setTextSize(24.0f);
        r1.addView(r5);
        TextView r3 = new TextView(this);
        r3.setText("Comparte una copia completa y su código. El plazo empieza al generar la copia y NO se reinicia al importarla de nuevo. Se restauran listas, proveedores, EPG, favoritos y ajustes; las cuentas protegidas se inician de nuevo.\n\nAl vencer se bloquea el uso en esta APK, incluso si estaba reproduciendo. Es un control local: no protege frente a extracción de listas, modificación de la app ni reinstalaciones con cambios de fecha.");
        r3.setTextColor(-1182209);
        r1.addView(r3);
        this.create = new Button(this);
        this.create.setText("Generar copia y código con duración");
        this.create.setEnabled(!TimedAccess.limited(this));
        this.create.setOnClickListener(new TimedAccessActivity$$ExternalSyntheticLambda21(this));
        r1.addView(this.create);
        if (TimedAccess.limited(this) == true) goto L10;
        Button r6 = new Button(this);
        r6.setText("Ver último código generado");
        r6.setOnClickListener(new TimedAccessActivity$$ExternalSyntheticLambda22(this));
        r1.addView(r6);
    L10:
        this.code = new EditText(this);
        this.code.setHint("Pegar código CHD1-…");
        this.code.setSingleLine(true);
        r1.addView(this.code);
        this.importButton = new Button(this);
        this.importButton.setText("Importar copia temporal con este código");
        this.importButton.setOnClickListener(new TimedAccessActivity$$ExternalSyntheticLambda23(this));
        r1.addView(this.importButton);
        this.status = new TextView(this);
        this.status.setTextColor(-9181523);
        TextView r7 = this.status;
        if (TimedAccess.limited(this) == true) goto L13;
        String r8 = "Sin límite de acceso en esta instalación";
    L17:
        r7.setText(r8);
        r1.addView(this.status);
        if (TimedAccess.limited(this) == false) goto L20;
        Button r9 = new Button(this);
        r9.setText("Eliminar datos cedidos y empezar vacío");
        r9.setOnClickListener(new TimedAccessActivity$$ExternalSyntheticLambda24(this));
        r1.addView(r9);
    L20:
        Button r10 = new Button(this);
        r10.setText("Volver");
        r10.setOnClickListener(new TimedAccessActivity$$ExternalSyntheticLambda25(this));
        r1.addView(r10);
        this.history = new BackupHistoryView(this, new C10361(this));
        r1.addView(this.history);
        this.settingsPanel = new SettingsSidePanel(this, r1, r4, r10);
        ArrayList r12 = new ArrayList();
        int r13 = 0;
    L22:
        if (r13 >= r1.getChildCount()) goto L27;
        if ((r1.getChildAt(r13) instanceof Button) == false) goto L26;
        r12.add((Button) r1.getChildAt(r13));
    L26:
        r13 = r13 + 1;
        goto L22
    L27:
        setContentView(this.settingsPanel);
        Iterator r14 = r12.iterator();
    L29:
        if (r14.hasNext() == false) goto L31;
        this.settingsPanel.addAction((Button) r14.next());
        goto L29
    L31:
        if (r11 == null) goto L33;
        return;
    L33:
        if (getIntent().hasExtra("panelTool") == false) goto L41;
        new Handler(Looper.getMainLooper()).post(new TimedAccessActivity$$ExternalSyntheticLambda1(this));
        return;
    L41:
        return;
    L13:
        if (TimedAccess.allowed(this) == false) goto L15;
        r8 = TimedAccess.description(this);
        goto L17
    L15:
        r8 = "Acceso caducado · introduce otra copia con código vigente";
        goto L17
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.worker.shutdown();
        super.onDestroy();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.history == null) goto L6;
        this.history.refresh();
    L6:
        if (TimedAccess.limited(this) == true) goto L9;
        ParentalControl.authenticate(this, new TimedAccessActivity$$ExternalSyntheticLambda19(), true);
        return;
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r4) {
        super.onSaveInstanceState(r4);
        r4.putString("pending", this.pending);
        r4.putLong(MediaInformation.KEY_DURATION, this.duration);
        if (this.exportFile == null) goto L6;
        r4.putString("exportFile", this.exportFile.getName());
        return;
    }

    void openPanelToolAction(String r5) {
        if (r5.equals("Generar copia y código con duración") == false) goto L7;
        this.create.performClick();
        return;
    L7:
        if (r5.equals("Importar copia temporal con código") == false) goto L10;
        this.code.requestFocus();
        return;
    L10:
        String r0 = BackupStore.lastCode(this);
        AlertDialog.Builder r1 = new AlertDialog.Builder(this).setTitle("Último código generado");
        if (r0.isEmpty() == false) goto L13;
        String r2 = "Todavía no hay un código generado";
    L14:
        r1.setMessage(r2).setPositiveButton("Cerrar", null).show();
        return;
    L13:
        r2 = r0;
        goto L14
    }
}
