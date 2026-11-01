package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes3.dex */
final class BackupHistoryView extends LinearLayout {
    private final Actions actions;
    private final Activity owner;

    interface Actions {
        boolean busy();

        void export(File r1);

        void restore(File r1);
    }

    public static /* synthetic */ void $r8$lambda$13ELeWKVJVTNbv_lZrrjTY0pyqA(BackupHistoryView r0, File r1) {
        r0.lambda$refresh$1(r1);
    }

    public static /* synthetic */ void $r8$lambda$XajkCvWVIPBD4reyonBTGfQQbtE(BackupHistoryView r0, String r1, DialogInterface r2, int r3) {
        r0.lambda$refresh$5(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$dJrAKI5kSMDtb_jzuJ2sZ9HggsQ(BackupHistoryView r0, File r1) {
        r0.lambda$refresh$2(r1);
    }

    public static /* synthetic */ void $r8$lambda$hzP33yGzc6GForVu9SZduZ7dNzo(BackupHistoryView r0, Runnable r1, View r2) {
        r0.lambda$add$7(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$mgIi-H3eXJyWDJQF3jzrBnF6kgs, reason: not valid java name */
    public static /* synthetic */ void m519$r8$lambda$mgIiH3eXJyWDJQF3jzrBnF6kgs(BackupHistoryView r0, File r1) {
        r0.lambda$refresh$4(r1);
    }

    public static /* synthetic */ void $r8$lambda$rJNHrtdlVQtx2O13GDstbj2AG_0(BackupHistoryView r0, File r1, String r2, View r3) {
        r0.lambda$refresh$6(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$tPKZf7Zbrdl_vrKVqj6QBWak9NE(BackupHistoryView r0, File r1) {
        r0.lambda$refresh$0(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$uOAWXHcN80IQuQn3kl1-V14xGlY, reason: not valid java name */
    public static /* synthetic */ void m520$r8$lambda$uOAWXHcN80IQuQn3kl1V14xGlY(BackupHistoryView r0, File r1, DialogInterface r2, int r3) {
        r0.lambda$refresh$3(r1, r2, r3);
    }

    BackupHistoryView(Activity r2, Actions r3) {
        super(r2);
        this.owner = r2;
        this.actions = r3;
        setOrientation(1);
        refresh();
    }

    private void add(LinearLayout r6, String r7, final Runnable r8) {
        Button r0 = new Button(this.owner);
        r0.setText(r7);
        SettingsSidePanel.style(r0);
        r0.setAllCaps(false);
        r0.setMinWidth(0);
        r0.setMinimumWidth(0);
        r0.setOnClickListener(new BackupHistoryView$$ExternalSyntheticLambda5(this, r8));
        r6.addView(r0, new LinearLayout.LayoutParams(0, -2, 1.0f));
    }

    private /* synthetic */ void lambda$add$7(Runnable r2, View r3) {
        if (this.actions.busy() == true) goto L6;
        r2.run();
        return;
    }

    private /* synthetic */ void lambda$refresh$0(File r5) {
        this.owner.startActivity(Intent.createChooser(BackupStore.share(this.owner, r5), "Compartir copia"));     // Catch: ActivityNotFoundException -> L4
        return;
    L5:
        Toast.makeText(this.owner, "No hay app para compartir. La copia sigue guardada aquí.", 1).show();
    }

    private /* synthetic */ void lambda$refresh$1(File r2) {
        this.actions.export(r2);
    }

    private /* synthetic */ void lambda$refresh$2(File r2) {
        this.actions.restore(r2);
    }

    private /* synthetic */ void lambda$refresh$3(File r5, DialogInterface r6, int r7) {
        if (this.actions.busy() == false) goto L10;
        return;
    L10:
        BackupStore.delete(this.owner, r5);     // Catch: IOException -> L7
        refresh();     // Catch: IOException -> L7
        Toast.makeText(this.owner, "Copia eliminada", 0).show();     // Catch: IOException -> L7
        return;
    L7:
        e = move-exception;
        Toast.makeText(this.owner, e.getMessage(), 1).show();
        refresh();
    }

    private /* synthetic */ void lambda$refresh$4(final File r4) {
        new AlertDialog.Builder(this.owner).setTitle("Eliminar copia").setMessage("¿Borrar «" + r4.getName() + "» de este dispositivo?\nLas copias exportadas o subidas a Dropbox se conservan.").setPositiveButton("Eliminar", new BackupHistoryView$$ExternalSyntheticLambda6(this, r4)).setNegativeButton("Cancelar", null).show();
    }

    private /* synthetic */ void lambda$refresh$5(String r3, DialogInterface r4, int r5) {
        ((ClipboardManager) this.owner.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Código CHDTV", r3));
    }

    private /* synthetic */ void lambda$refresh$6(File r4, final String r5, View r6) {
        if (this.actions.busy() == false) goto L5;
        return;
    L5:
        new AlertDialog.Builder(this.owner).setTitle(r4.getName()).setMessage(r5).setPositiveButton("Copiar código", new BackupHistoryView$$ExternalSyntheticLambda7(this, r5)).setNegativeButton("Cerrar", null).show();
    }

    void refresh() {
        removeAllViews();
        TextView r1 = new TextView(this.owner);
        r1.setText("Copias guardadas en CHDTV");
        r1.setTextSize(21.0f);
        r1.setTextColor(-12986907);
        addView(r1);
        List<File> r2 = BackupStore.list(this.owner);
        if (r2.isEmpty() == false) goto L6;
        TextView r3 = new TextView(this.owner);
        r3.setText("Todavía no hay copias guardadas. Genera una con los botones de arriba.");
        r3.setTextColor(-1182209);
        addView(r3);
        return;
    L6:
        Iterator<File> r4 = r2.iterator();
    L8:
        if (r4.hasNext() == false) goto L18;
        final File r5 = r4.next();
        LinearLayout r6 = new LinearLayout(this.owner);
        r6.setOrientation(1);
        int r8 = (int) (getResources().getDisplayMetrics().density * 14.0f);
        r6.setPadding(r8, r8, r8, r8);
        GradientDrawable r9 = new GradientDrawable();
        r9.setColor(-15916233);
        r9.setCornerRadius(r8);
        r9.setStroke(Math.max(1, r8 / 14), -14071455);
        r6.setBackground(r9);
        LinearLayout.LayoutParams r10 = new LinearLayout.LayoutParams(-1, -2);
        r10.setMargins(0, r8, 0, 0);
        addView(r6, r10);
        TextView r11 = new TextView(this.owner);
        String r12 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date(r5.lastModified()));
        long r13 = BackupStore.expires(this.owner, r5);
        StringBuilder r7 = new StringBuilder().append(r5.getName()).append("\n").append(r12).append(" · ").append(Formatter.formatFileSize(this.owner, r5.length()));
        if (r13 <= 0) goto L12;
        String r14 = "\nVence: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(r13));
    L13:
        r11.setText(r7.append(r14).toString());
        r11.setTextColor(-1182209);
        r11.setTextIsSelectable(true);
        r6.addView(r11);
        LinearLayout r15 = new LinearLayout(this.owner);
        add(r15, "Compartir", new BackupHistoryView$$ExternalSyntheticLambda0(this, r5));
        add(r15, "Guardar fuera", new BackupHistoryView$$ExternalSyntheticLambda1(this, r5));
        add(r15, "Restaurar", new BackupHistoryView$$ExternalSyntheticLambda2(this, r5));
        r6.addView(r15);
        LinearLayout r16 = new LinearLayout(this.owner);
        add(r16, "Eliminar copia", new BackupHistoryView$$ExternalSyntheticLambda3(this, r5));
        r6.addView(r16);
        final String r17 = BackupStore.code(this.owner, r5);
        if (r17.isEmpty() == true) goto L17;
        Button r18 = new Button(this.owner);
        r18.setText("Ver / copiar código de esta copia");
        r18.setOnClickListener(new BackupHistoryView$$ExternalSyntheticLambda4(this, r5, r17));
        SettingsSidePanel.style(r18);
        r6.addView(r18);
    L17:
        r2 = r2;
        r1 = r1;
        goto L8
    L12:
        r14 = "";
        goto L13
    }
}
