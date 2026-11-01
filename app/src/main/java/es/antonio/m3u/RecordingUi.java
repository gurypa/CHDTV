package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes3.dex */
final class RecordingUi {
    private RecordingUi() {
    }

    static void choose(final Activity r3, final Entry r4, final EpgData.Programme r5, final List<EpgData.Programme> r6) {
        if (r4 != null) goto L4;
        return;
    L4:
        new AlertDialog.Builder(r3).setTitle("Grabaciones EPG · " + r4.title).setItems(new String[]{"Grabar el programa seleccionado", "Elegir varios programas"}, new RecordingUi$$ExternalSyntheticLambda5(r5, r3, r4, r6)).show();
    }

    static /* synthetic */ void lambda$choose$2(boolean[] r0, DialogInterface r1, int r2, boolean r3) {
        r0[r2] = r3;
    }

    static /* synthetic */ void lambda$choose$3(Activity r2, Entry r3, ArrayList r4) {
        r2.startActivity(new Intent(r2, RecordingsActivity.class).putExtra("recordEntry", r3).putExtra("recordProgrammes", r4));
    }

    static /* synthetic */ void lambda$choose$4(boolean[] r3, ArrayList r4, final Activity r5, AlertDialog r6, final Entry r7, View r8) {
        final ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= r3.length) goto L10;
        if (r3[r1] == false) goto L8;
        r0.add((EpgData.Programme) r4.get(r1));
    L8:
        r1 = r1 + 1;
        goto L4
    L10:
        if (r0.isEmpty() == false) goto L13;
        Toast.makeText(r5, "Selecciona al menos un programa", 1).show();
        return;
    L13:
        r6.dismiss();
        ParentalControl.requireEntry(r5, r7, new RecordingUi$$ExternalSyntheticLambda6(r5, r7, r0));
    }

    static /* synthetic */ void lambda$choose$5(final AlertDialog r7, final boolean[] r8, final ArrayList r9, final Activity r10, final Entry r11, DialogInterface r12) {
        r7.getButton(-1).setOnClickListener(new RecordingUi$$ExternalSyntheticLambda4(r8, r9, r10, r7, r11));
    }

    static /* synthetic */ void lambda$choose$6(EpgData.Programme r13, final Activity r14, final Entry r15, List r16, DialogInterface r17, int r18) {
        if (r18 != 0) goto L8;
        if (r13 == null) goto L6;
        programme(r14, r15, r13);
        return;
    L6:
        Toast.makeText(r14, "Selecciona un programa de la guía", 1).show();
        return;
    L8:
        final ArrayList r3 = new ArrayList();
        long r6 = System.currentTimeMillis();
        Iterator r1 = r16.iterator();
    L10:
        if (r1.hasNext() == false) goto L17;
        EpgData.Programme r2 = (EpgData.Programme) r1.next();
        if (r2.start <= r6) goto L10;
        if (r2.stop <= r2.start) goto L10;
        r3.add(r2);
        goto L10
    L17:
        if (r3.isEmpty() == false) goto L20;
        Toast.makeText(r14, "No hay programas futuros en esta guía", 1).show();
        return;
    L20:
        String[] r8 = new String[r3.size()];
        final boolean[] r4 = new boolean[r3.size()];
        SimpleDateFormat r0 = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        int r5 = 0;
    L22:
        if (r5 >= r8.length) goto L24;
        EpgData.Programme r7 = (EpgData.Programme) r3.get(r5);
        r8[r5] = r0.format(new Date(r7.start)) + " · " + r7.title;
        r5 = r5 + 1;
        goto L22
    L24:
        final AlertDialog r9 = new AlertDialog.Builder(r14).setTitle("Selecciona programas · " + r15.title).setMultiChoiceItems(r8, r4, new RecordingUi$$ExternalSyntheticLambda0(r4)).setPositiveButton("Continuar", null).setNegativeButton("Cancelar", null).create();
        r9.setOnShowListener(new RecordingUi$$ExternalSyntheticLambda1(r9, r4, r3, r14, r15));
        r9.show();
    }

    static /* synthetic */ void lambda$open$0(Activity r2, Entry r3) {
        r2.startActivity(new Intent(r2, RecordingsActivity.class).putExtra("recordEntry", r3));
    }

    static /* synthetic */ void lambda$programme$1(Activity r4, Entry r5, EpgData.Programme r6) {
        r4.startActivity(new Intent(r4, RecordingsActivity.class).putExtra("recordEntry", r5).putExtra("recordTitle", r6.title).putExtra("recordStart", r6.start).putExtra("recordEnd", r6.stop));
    }

    static void open(final Activity r1, final Entry r2) {
        ParentalControl.requireEntry(r1, r2, new RecordingUi$$ExternalSyntheticLambda3(r1, r2));
    }

    static void programme(final Activity r4, final Entry r5, final EpgData.Programme r6) {
        if (r6 != null) goto L6;
        open(r4, r5);
        return;
    L6:
        if (r6.stop > System.currentTimeMillis()) goto L9;
        Toast.makeText(r4, "Este programa ya terminó; la grabación sólo guarda emisiones nuevas", 1).show();
        return;
    L9:
        ParentalControl.requireEntry(r4, r5, new RecordingUi$$ExternalSyntheticLambda2(r4, r5, r6));
    }
}
