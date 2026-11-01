package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes3.dex */
public final class BlackBoxActivity extends ChdtvScreen {
    private boolean exporting;
    private File pending;
    private Button save;
    private Button share;
    private Button start;
    private TextView status;
    private Button stop;
    private final ExecutorService worker;

    /* JADX INFO: renamed from: $r8$lambda$-GeZuIGdU_Vh3qB5AtebnRVw2sg, reason: not valid java name */
    public static /* synthetic */ void m526$r8$lambda$GeZuIGdU_Vh3qB5AtebnRVw2sg(BlackBoxActivity r0, boolean r1) {
        r0.lambda$onActivityResult$8(r1);
    }

    public static /* synthetic */ void $r8$lambda$4i3060jMDlT_MaH0NC1mpiOdLKM(BlackBoxActivity r0, File r1, Uri r2) {
        r0.lambda$onActivityResult$9(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$5VVgjxz8Ay3izbyfgruMiV-1m-s, reason: not valid java name */
    public static /* synthetic */ void m527$r8$lambda$5VVgjxz8Ay3izbyfgruMiV1ms(BlackBoxActivity r0) {
        r0.lambda$onCreate$0();
    }

    public static /* synthetic */ void $r8$lambda$EEZiLVBCVb0LJuIIjZlke5dc2ME(BlackBoxActivity r0, boolean r1, File r2, Exception r3) {
        r0.lambda$export$7(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$I4uxW6Vgrk4FjFfn7atI6E_AWpY(BlackBoxActivity r0, DialogInterface r1, int r2) {
        r0.lambda$onCreate$4(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$N303cedGm-lLQfSG_oupboloByc, reason: not valid java name */
    public static /* synthetic */ void m528$r8$lambda$N303cedGmlLQfSG_oupboloByc(BlackBoxActivity r0) {
        r0.lambda$onCreate$3();
    }

    public static /* synthetic */ void $r8$lambda$a969jYVJeD1hSFoZo1hcOp6GzBc(BlackBoxActivity r0) {
        r0.lambda$onCreate$1();
    }

    public static /* synthetic */ void $r8$lambda$d4JVP_4kNyAOCfTyIZYxWHCQ4zI(BlackBoxActivity r0) {
        r0.lambda$onCreate$2();
    }

    public static /* synthetic */ void $r8$lambda$qE5V7DfryBjOyTAXmd8YlBmyzjU(BlackBoxActivity r0) {
        r0.lambda$onCreate$5();
    }

    public BlackBoxActivity() {
        this.worker = Executors.newSingleThreadExecutor();
    }

    private Button add(LinearLayout r5, String r6, final Runnable r7) {
        Button r0 = new Button(this);
        r0.setText(r6);
        r0.setAllCaps(false);
        r0.setOnClickListener(new BlackBoxActivity$$ExternalSyntheticLambda3(r7));
        r5.addView(r0, new LinearLayout.LayoutParams(-1, -2));
        return r0;
    }

    private void export(final boolean r2) {
        if (this.exporting == false) goto L5;
        return;
    L5:
        this.exporting = true;
        refresh();
        BlackBox.snapshot(new BlackBoxActivity$$ExternalSyntheticLambda1(this, r2));
    }

    static /* synthetic */ void lambda$add$6(Runnable r0, View r1) {
        r0.run();
    }

    private /* synthetic */ void lambda$export$7(boolean r5, File r6, Exception r7) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.exporting = false;
        refresh();
        if (r7 == null) goto L9;
        Toast.makeText(this, "No se pudo crear el TXT. Inténtalo de nuevo.", 1).show();
        return;
    L9:
        this.pending = r6;
        if (r5 == false) goto L17;
        share(r6);
        return;
    L17:
        startActivityForResult(new Intent("android.intent.action.CREATE_DOCUMENT").setType("text/plain").addCategory("android.intent.category.OPENABLE").putExtra("android.intent.extra.TITLE", r6.getName()), 48154);     // Catch: ActivityNotFoundException -> L14
        return;
    L15:
        Toast.makeText(this, "Usa Compartir TXT: este dispositivo no tiene selector de archivos.", 1).show();
    }

    private /* synthetic */ void lambda$onActivityResult$8(boolean r3) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.exporting = false;
        refresh();
        if (r3 == false) goto L8;
        String r0 = "TXT guardado";
    L9:
        Toast.makeText(this, r0, 1).show();
        return;
    L8:
        r0 = "No se pudo guardar el TXT";
        goto L9
    }

    private /* synthetic */ void lambda$onActivityResult$9(File r8, Uri r9) {
        boolean r0 = false;
        FileInputStream r1 = new FileInputStream(r8);     // Catch: Exception -> L33
        OutputStream r2 = getContentResolver().openOutputStream(r9, "wt");     // Catch: Throwable -> L27
        if (r2 == null) goto L20;
        byte[] r3 = new byte[8192];     // Catch: Throwable -> L17
    L8:
        int r4 = r1.read(r3);     // Catch: Throwable -> L17
        if (r4 == (-1)) goto L12;
        r2.write(r3, 0, r4);     // Catch: Throwable -> L17
        goto L8
    L12:
        r0 = true;
        if (r2 == null) goto L15;
        r2.close();     // Catch: Throwable -> L27
    L15:
        r1.close();     // Catch: Exception -> L33
    L35:
        final boolean r5 = r0;
        runOnUiThread(new BlackBoxActivity$$ExternalSyntheticLambda0(this, r5));
        return;
    L20:
        throw new IOException();     // Catch: Throwable -> L17
    L17:
        th = move-exception;
        if (r2 != null) goto L41;
    L26:
        throw th;     // Catch: Throwable -> L27
    L41:
        r2.close();     // Catch: Throwable -> L24
    L24:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Throwable -> L27
    L27:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L30
    L32:
        throw th;     // Catch: Exception -> L33
    L30:
        th = move-exception;
        th.addSuppressed(th);     // Catch: Exception -> L33
    L33:
        e = move-exception;
        BlackBox.failure("EXPORT_ERROR", e);
        goto L35
    }

    private /* synthetic */ void lambda$onCreate$0() {
        BlackBox.start();
        refresh();
    }

    private /* synthetic */ void lambda$onCreate$1() {
        BlackBox.stop();
        refresh();
    }

    private /* synthetic */ void lambda$onCreate$2() {
        export(false);
    }

    private /* synthetic */ void lambda$onCreate$3() {
        export(true);
    }

    private /* synthetic */ void lambda$onCreate$4(DialogInterface r1, int r2) {
        BlackBox.clear();
        refresh();
    }

    private /* synthetic */ void lambda$onCreate$5() {
        new AlertDialog.Builder(this).setTitle("Borrar registro de diagnóstico").setMessage("Solo se borrará la caja negra. Tus listas y ajustes se conservan.").setNegativeButton("Cancelar", null).setPositiveButton("Borrar", new BlackBoxActivity$$ExternalSyntheticLambda4(this)).show();
    }

    private void refresh() {
        TextView r0 = this.status;
        if (BlackBox.active() == false) goto L5;
        String r1 = "● Registro activo";
    L6:
        r0.setText(r1);
        this.start.setEnabled(!BlackBox.active());
        this.stop.setEnabled(BlackBox.active());
        this.save.setEnabled(!this.exporting);
        this.share.setEnabled(!this.exporting);
        return;
    L5:
        r1 = "Registro detenido · puedes exportar lo guardado";
        goto L6
    }

    private void share(File r5) {
        Uri r1 = FileProvider.getUriForFile(this, getPackageName() + ".backups", r5);     // Catch: Exception -> L5
        Intent r2 = new Intent("android.intent.action.SEND").setType("text/plain").putExtra("android.intent.extra.STREAM", r1).addFlags(1);     // Catch: Exception -> L5
        r2.setClipData(ClipData.newRawUri("Caja negra CHDTV", r1));     // Catch: Exception -> L5
        startActivity(Intent.createChooser(r2, "Compartir diagnóstico TXT"));     // Catch: Exception -> L5
        return;
    L6:
        Toast.makeText(this, "No hay una aplicación disponible para compartir el TXT.", 1).show();
    }

    @Override // es.antonio.m3u.ChdtvScreen
    protected String chromeSelection() {
        return "Reproducción";
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r5, int r6, Intent r7) {
        super.onActivityResult(r5, r6, r7);
        if (r5 == 48154) goto L5;
        return;
    L5:
        if (r6 != (-1)) goto L15;
        if (r7 != null) goto L8;
        return;
    L8:
        if (r7.getData() != null) goto L10;
        return;
    L10:
        if (this.pending == null) goto L18;
        final Uri r0 = r7.getData();
        final File r1 = this.pending;
        this.exporting = true;
        refresh();
        this.worker.execute(new BlackBoxActivity$$ExternalSyntheticLambda2(this, r1, r0));
        return;
    L18:
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r8) {
        super.onCreate(r8);
        if (r8 == null) goto L9;
        String r0 = r8.getString("pendingExport", "");
        if (r0.matches("CHDTV_caja_negra_[0-9_]+\\.txt") == false) goto L9;
        File r1 = new File(new File(getCacheDir(), "shared-backups"), r0);
        if (r1.isFile() == false) goto L9;
        this.pending = r1;
    L9:
        ScrollView r2 = new ScrollView(this);
        LinearLayout r3 = new LinearLayout(this);
        r3.setOrientation(1);
        int r4 = Math.round(getResources().getDisplayMetrics().density * 20.0f);
        r3.setPadding(r4, r4, r4, r4);
        r2.addView(r3);
        setContentView(r2);
        add(r3, "‹ Atrás", new BlackBoxActivity$$ExternalSyntheticLambda5(this));
        TextView r5 = new TextView(this);
        r5.setText("Caja negra · diagnóstico");
        r5.setTextSize(24.0f);
        r3.addView(r5);
        TextView r6 = new TextView(this);
        r6.setText("1. Inicia el registro.\n2. Vuelve y recorre la app hasta reproducir el fallo.\n3. Regresa aquí, detén el registro y guarda o comparte el TXT.\n\nRegistra pantallas, cargas, tiempos, errores y bloqueos de la interfaz. No guarda URLs, contraseñas, tokens ni claves. Se conserva si la app se cierra. Máximo aproximado: 2 MB; sustituye los eventos más antiguos. No se envía automáticamente.");
        r6.setTextSize(16.0f);
        r3.addView(r6);
        this.status = new TextView(this);
        this.status.setTextSize(18.0f);
        r3.addView(this.status);
        this.start = add(r3, "Iniciar registro", new BlackBoxActivity$$ExternalSyntheticLambda6(this));
        this.stop = add(r3, "Detener registro", new BlackBoxActivity$$ExternalSyntheticLambda7(this));
        this.save = add(r3, "Guardar TXT", new BlackBoxActivity$$ExternalSyntheticLambda8(this));
        this.share = add(r3, "Compartir TXT", new BlackBoxActivity$$ExternalSyntheticLambda9(this));
        add(r3, "Borrar registro", new BlackBoxActivity$$ExternalSyntheticLambda10(this));
        refresh();
        LocalPanel.attach(this);
        HlsDiagnostics225.install(this);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.worker.shutdownNow();
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r3) {
        super.onSaveInstanceState(r3);
        if (this.pending == null) goto L6;
        r3.putString("pendingExport", this.pending.getName());
        return;
    }
}
