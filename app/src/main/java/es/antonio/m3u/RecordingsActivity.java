package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.media3.common.MimeTypes;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes7.dex */
public final class RecordingsActivity extends ChdtvScreen {
    private ArrayAdapter<String> adapter;
    private String exportId;
    private TextView heading;

    /* JADX INFO: renamed from: io */
    private final ExecutorService f189io;
    private ListView list;
    private boolean loading;
    private final Handler main;
    private List<RecordingTask> permissionBatch;
    private RecordingTask permissionPending;
    private boolean permissionScheduled;
    private final Runnable refresh;
    private LinearLayout root;
    private List<RecordingTask> shown;

    /* JADX INFO: renamed from: $r8$lambda$Bj-cNsQs5TpqvXjJlFc8UoEwNc8, reason: not valid java name */
    public static /* synthetic */ void m747$r8$lambda$BjcNsQs5TpqvXjJlFc8UoEwNc8(RecordingsActivity r0) {
        r0.load();
    }

    public RecordingsActivity() {
        this.shown = new ArrayList();
        this.main = new Handler(Looper.getMainLooper());
        this.f189io = Executors.newSingleThreadExecutor();
        this.exportId = "";
        this.refresh = new RunnableC10311(this);
    }

    static /* synthetic */ void access$0(RecordingsActivity r0) {
        r0.load();
    }

    static /* synthetic */ Handler access$1(RecordingsActivity r0) {
        return r0.main;
    }

    private void batchForm(Entry r9) {
        String r0 = RecordingCommand.limitation(r9);
        if (r0.isEmpty() == true) goto L6;
        message(r0);
        return;
    L6:
        Serializable r1 = getIntent().getSerializableExtra("recordProgrammes");
        if ((r1 instanceof ArrayList) == true) goto L10;
        message("Selección EPG no válida");
        return;
    L10:
        final ArrayList r2 = new ArrayList();
        StringBuilder r3 = new StringBuilder();
        SimpleDateFormat r4 = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        Iterator r5 = ((ArrayList) r1).iterator();
    L12:
        if (r5.hasNext() == false) goto L13;
        Object r6 = r5.next();
        if ((r6 instanceof EpgData.Programme) == false) goto L12;
        EpgData.Programme r7 = (EpgData.Programme) r6;
        RecordingTask r8 = new RecordingTask();
        r8.entry = r9;
        r8.title = r7.title;
        r8.start = r7.start;
        r8.end = r7.stop;
        r2.add(r8);
        r3.append(r4.format(new Date(r8.start))).append(" · ").append(r8.title).append("\n");
        goto L12
    L13:
        LinearLayout r10 = new LinearLayout(this);
        r10.setOrientation(1);
        TextView r11 = new TextView(this);
        r11.setText(r3 + "\nSe usarán las horas exactas de la EPG. Una grabación a la vez; el dispositivo debe estar encendido.");
        r10.addView(r11);
        final Spinner r12 = new Spinner(this);
        r12.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"MP4", "MKV"}));
        r10.addView(r12);
        ScrollView r13 = new ScrollView(this);
        r13.addView(r10);
        final AlertDialog r14 = new AlertDialog.Builder(this).setTitle("Programar " + r2.size() + " programas · " + r9.title).setView(r13).setPositiveButton("Programar", null).setNegativeButton("Cancelar", null).create();
        r14.setOnShowListener(new RecordingsActivity$$ExternalSyntheticLambda19(this, r14, r2, r12));
        r14.show();
    }

    private EditText field(LinearLayout r2, String r3, String r4, int r5) {
        EditText r0 = new EditText(this);
        r0.setHint(r3);
        r0.setText(r4);
        r0.setInputType(r5);
        r0.setSingleLine();
        r2.addView(r0);
        return r0;
    }

    private void form(final Entry r17) {
        String r0 = RecordingCommand.limitation(r17);
        if (r0.isEmpty() == true) goto L6;
        message(r0);
        return;
    L6:
        long r1 = System.currentTimeMillis();
        long r2 = getIntent().getLongExtra("recordStart", r1);
        long r4 = getIntent().getLongExtra("recordEnd", 0);
        long r8 = Math.max(r1, r2);
        final SimpleDateFormat r6 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        r6.setLenient(false);
        LinearLayout r12 = new LinearLayout(this);
        r12.setOrientation(1);
        int r14 = chromeDp(12);
        r12.setPadding(r14, r14, r14, r14);
        if (getIntent().getStringExtra("recordTitle") != null) goto L9;
        String r15 = r17.title;
    L10:
        final EditText r16 = field(r12, "Nombre", r15, 1);
        final CheckBox r18 = new CheckBox(this);
        r18.setText("Programar para una fecha");
        if (r2 <= r1) goto L13;
        boolean r11 = true;
    L14:
        r18.setChecked(r11);
        r12.addView(r18);
        final EditText r13 = field(r12, "Inicio · dd/MM/yyyy HH:mm", r6.format(new Date(r8)), 4);
        r13.setEnabled(r18.isChecked());
        r18.setOnCheckedChangeListener(new RecordingsActivity$$ExternalSyntheticLambda30(r13));
        if (r4 <= r8) goto L18;
        long r3 = Math.max(1, ((r4 - r8) + 59999) / 60000);
    L22:
        final EditText r5 = field(r12, "Duración en minutos · 1 a 360", String.valueOf(Math.min(360, r3)), 2);
        final Spinner r9 = new Spinner(this);
        r9.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"MP4", "MKV"}));
        r12.addView(r9);
        TextView r7 = new TextView(this);
        r7.setText("Se guardará sin recomprimir. Para películas y episodios termina antes si finaliza el contenido. Una grabación simultánea. El dispositivo debe estar encendido; puedes salir de CHDTV.\nClearKey DASH: audio y vídeo usan su propia clave. La rotación de claves dentro de una misma pista todavía no está admitida.");
        r12.addView(r7);
        ScrollView r10 = new ScrollView(this);
        r10.addView(r12);
        final AlertDialog r19 = new AlertDialog.Builder(this).setTitle("Grabar · " + r17.title).setView(r10).setPositiveButton("Guardar", null).setNegativeButton("Cancelar", null).create();
        r19.setOnShowListener(new RecordingsActivity$$ExternalSyntheticLambda31(this, r19, r5, r18, r6, r13, r17, r16, r9));
        r19.show();
        return;
    L18:
        if ("Canales".equals(r17.kind) == false) goto L20;
        int r20 = 30;
    L21:
        r3 = r20;
        goto L22
    L20:
        r20 = 360;
        goto L21
    L13:
        r11 = false;
        goto L14
    L9:
        r15 = getIntent().getStringExtra("recordTitle");
        goto L10
    }

    static /* synthetic */ void lambda$6(EditText r0, CompoundButton r1, boolean r2) {
        r0.setEnabled(r2);
    }

    private void load() {
        if (this.loading == false) goto L5;
        return;
    L5:
        if (this.f189io.isShutdown() == true) goto L10;
        this.loading = true;
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda10(this));
        return;
    }

    private void message(String r3) {
        if (isFinishing() == false) goto L5;
        return;
    L5:
        if (isDestroyed() == true) goto L9;
        new AlertDialog.Builder(this).setTitle("Grabaciones").setMessage(r3).setPositiveButton("Aceptar", null).show();
        return;
    }

    private void options(final RecordingTask r7) {
        if (r7.active() == false) goto L8;
        AlertDialog.Builder r8 = new AlertDialog.Builder(this).setTitle(r7.title).setItems(new String[]{"Detener grabación", "Detener y eliminar"}, new RecordingsActivity$$ExternalSyntheticLambda3(this, r7));
    L5:
        r8.show();
        return;
    L8:
        if ("Programada".equals(r7.state) == false) goto L10;
        r8 = new AlertDialog.Builder(this).setTitle(r7.title).setMessage("Inicio: " + DateFormat.getDateTimeInstance().format(new Date(r7.start))).setPositiveButton("Cancelar programación", new RecordingsActivity$$ExternalSyntheticLambda4(this, r7)).setNegativeButton("Cerrar", null);
        goto L5
    L10:
        final ArrayList r0 = new ArrayList();
        if (r7.playable() == false) goto L13;
        r0.add("Reproducir");
    L13:
        r0.add("Exportar archivo");
        r0.add("Eliminar");
        new AlertDialog.Builder(this).setTitle(r7.title).setItems((CharSequence[]) r0.toArray(new String[0]), new RecordingsActivity$$ExternalSyntheticLambda5(this, r0, r7)).show();
    }

    private void play(final RecordingTask r3) {
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda20(this, r3));
    }

    private void submit(final RecordingTask r3, final boolean r4) {
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda29(this, r4, r3));
    }

    private void submitBatch(final List<RecordingTask> r3) {
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda32(this, r3));
    }

    /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m748lambda$0$esantoniom3uRecordingsActivity(View r1) {
        finish();
    }

    /* JADX INFO: renamed from: lambda$1$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m749lambda$1$esantoniom3uRecordingsActivity(AdapterView r1, View r2, int r3, long r4) {
        options(this.shown.get(r3));
    }

    /* JADX INFO: renamed from: lambda$10$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m750lambda$10$esantoniom3uRecordingsActivity(final AlertDialog r2, final List r3, final Spinner r4, DialogInterface r5) {
        r2.getButton(-1).setOnClickListener(new RecordingsActivity$$ExternalSyntheticLambda23(this, r3, r4, r2));
    }

    /* JADX INFO: renamed from: lambda$11$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m751lambda$11$esantoniom3uRecordingsActivity(List r3, Spinner r4, AlertDialog r5, View r6) {
        if (RecordingScheduler.permitted(this) == true) goto L6;
        new AlertDialog.Builder(this).setMessage("Activa Alarmas y recordatorios y vuelve a pulsar Programar.").setPositiveButton("Abrir ajustes", new RecordingsActivity$$ExternalSyntheticLambda11(this)).setNegativeButton("Cancelar", null).show();
        return;
    L6:
        Iterator r7 = r3.iterator();
    L8:
        if (r7.hasNext() == false) goto L9;
        RecordingTask r0 = (RecordingTask) r7.next();
        if (r4.getSelectedItemPosition() != 0) goto L19;
        String r1 = "mp4";
    L20:
        r0.format = r1;
        goto L8
    L19:
        r1 = "mkv";
        goto L20
    L9:
        r5.dismiss();
        if (Build.VERSION.SDK_INT >= 33) goto L12;
    L14:
        submitBatch(r3);
        return;
    L12:
        if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") == 0) goto L14;
        this.permissionBatch = r3;
        requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 144);
    }

    /* JADX INFO: renamed from: lambda$12$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m752lambda$12$esantoniom3uRecordingsActivity(DialogInterface r2, int r3) {
        startActivity(new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM").setData(Uri.parse("package:" + getPackageName())));
    }

    /* JADX INFO: renamed from: lambda$13$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m753lambda$13$esantoniom3uRecordingsActivity(final List r3) {
        RecordingScheduler.scheduleAll(this, r3);     // Catch: Exception -> L4
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda8(this, r3));     // Catch: Exception -> L4
        return;
    L4:
        e = move-exception;
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda9(this, e));
    }

    /* JADX INFO: renamed from: lambda$14$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m754lambda$14$esantoniom3uRecordingsActivity(List r2) {
        load();
        message(String.valueOf(r2.size()) + " programas de la EPG programados. Puedes verlos y cancelarlos en Grabaciones.");
    }

    /* JADX INFO: renamed from: lambda$15$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m755lambda$15$esantoniom3uRecordingsActivity(Exception r2) {
        if (r2.getMessage() != null) goto L5;
        String r3 = "No se pudieron programar las grabaciones";
    L6:
        message(r3);
        return;
    L5:
        r3 = r2.getMessage();
        goto L6
    }

    /* JADX INFO: renamed from: lambda$16$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m756lambda$16$esantoniom3uRecordingsActivity(final boolean r3, RecordingTask r4) {
        if (r3 == false) goto L4;
        RecordingScheduler.schedule(this, r4);     // Catch: Exception -> L7
    L5:
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda6(this, r3));     // Catch: Exception -> L7
        return;
    L4:
        RecordingStore.put(this, r4);     // Catch: Exception -> L7
        RecordingScheduler.start(this, r4.f188id);     // Catch: Exception -> L7
    L15:
        r4.state = "Error";     // Catch: Exception -> L10
        r4.message = "No se pudo iniciar o programar la grabación";     // Catch: Exception -> L10
        RecordingStore.put(this, r4);     // Catch: Exception -> L10
    L11:
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda7(this));
    }

    /* JADX INFO: renamed from: lambda$17$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m757lambda$17$esantoniom3uRecordingsActivity(boolean r1) {
        load();
        if (r1 == false) goto L5;
        String r2 = "Grabación programada. El dispositivo debe estar encendido.";
    L6:
        message(r2);
        return;
    L5:
        r2 = "Preparando grabación. Comprueba su estado y detenla desde la notificación o desde Grabaciones.";
        goto L6
    }

    /* JADX INFO: renamed from: lambda$18$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m758lambda$18$esantoniom3uRecordingsActivity() {
        message("No se pudo iniciar o programar la grabación. Revisa permisos y espacio libre.");
    }

    /* JADX INFO: renamed from: lambda$19$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m759lambda$19$esantoniom3uRecordingsActivity(final RecordingTask r1, DialogInterface r2, int r3) {
        if (r3 != 0) goto L4;
        startService(new Intent(this, RecordingService.class).setAction("stop").putExtra("id", r1.f188id));
        return;
    L4:
        new AlertDialog.Builder(this).setTitle("Eliminar grabación en curso").setMessage("Se detendrá la grabación y se borrará su archivo.").setPositiveButton("Eliminar", new RecordingsActivity$$ExternalSyntheticLambda16(this, r1)).setNegativeButton("Cancelar", null).show();
    }

    /* JADX INFO: renamed from: lambda$2$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m760lambda$2$esantoniom3uRecordingsActivity(Entry r3) {
        if (getIntent().hasExtra("recordProgrammes") == false) goto L5;
        batchForm(r3);
        return;
    L5:
        form(r3);
    }

    /* JADX INFO: renamed from: lambda$20$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m761lambda$20$esantoniom3uRecordingsActivity(RecordingTask r1, DialogInterface r2, int r3) {
        startService(new Intent(this, RecordingService.class).setAction("delete").putExtra("id", r1.f188id));
    }

    /* JADX INFO: renamed from: lambda$21$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m762lambda$21$esantoniom3uRecordingsActivity(final RecordingTask r1, DialogInterface r2, int r3) {
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda34(this, r1));
    }

    /* JADX INFO: renamed from: lambda$22$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m763lambda$22$esantoniom3uRecordingsActivity(RecordingTask r2) {
        RecordingScheduler.cancel(this, r2);     // Catch: Exception -> L4
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda12(this));     // Catch: Exception -> L4
        return;
    L5:
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda13(this));
    }

    /* JADX INFO: renamed from: lambda$24$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m764lambda$24$esantoniom3uRecordingsActivity() {
        message("No se pudo cancelar");
    }

    /* JADX INFO: renamed from: lambda$25$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m765lambda$25$esantoniom3uRecordingsActivity(List r3, final RecordingTask r4, DialogInterface r5, int r6) {
        String r7 = (String) r3.get(r6);
        if (r7.equals("Reproducir") == false) goto L6;
        play(r4);
        return;
    L6:
        if (r7.equals("Exportar archivo") == false) goto L12;
        this.exportId = r4.f188id;
        Intent r8 = new Intent("android.intent.action.CREATE_DOCUMENT").addCategory("android.intent.category.OPENABLE");
        if (r4.format.equals("mp4") == false) goto L10;
        String r9 = MimeTypes.VIDEO_MP4;
    L11:
        startActivityForResult(r8.setType(r9).putExtra("android.intent.extra.TITLE", String.valueOf(r4.title.replaceAll("[\\\\/:*?\"<>|]", "_")) + "." + r4.format), 142);
        return;
    L10:
        r9 = MimeTypes.VIDEO_MATROSKA;
        goto L11
    L12:
        new AlertDialog.Builder(this).setTitle("Eliminar grabación").setMessage("Se borrará el vídeo y su registro.").setPositiveButton("Eliminar", new RecordingsActivity$$ExternalSyntheticLambda22(this, r4)).setNegativeButton("Cancelar", null).show();
    }

    /* JADX INFO: renamed from: lambda$26$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m766lambda$26$esantoniom3uRecordingsActivity(final RecordingTask r1, DialogInterface r2, int r3) {
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda17(this, r1));
    }

    /* JADX INFO: renamed from: lambda$27$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m767lambda$27$esantoniom3uRecordingsActivity(RecordingTask r3) {
        RecordingControl.request(this, r3.f188id, true);     // Catch: Exception -> L11
        File r0 = RecordingStore.file(this, r3);     // Catch: Exception -> L11
        if (r0.exists() == true) goto L5;
    L9:
        RecordingStore.remove(this, r3.f188id);     // Catch: Exception -> L11
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda12(this));     // Catch: Exception -> L11
        return;
    L5:
        if (r0.delete() == true) goto L9;
        throw new IOException();     // Catch: Exception -> L11
    L12:
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda35(this));
    }

    /* JADX INFO: renamed from: lambda$29$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m768lambda$29$esantoniom3uRecordingsActivity() {
        message("No se pudo eliminar la grabación");
    }

    /* JADX INFO: renamed from: lambda$3$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m769lambda$3$esantoniom3uRecordingsActivity() {
        final List<RecordingTask> r0 = RecordingStore.all(this);     // Catch: Exception -> L4
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda27(this, r0));     // Catch: Exception -> L4
        return;
    L5:
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda28(this));
    }

    /* JADX INFO: renamed from: lambda$30$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m770lambda$30$esantoniom3uRecordingsActivity(final RecordingTask r5) {
        File r0 = RecordingStore.file(this, r5);     // Catch: Exception -> L10
        if (r0.isFile() == false) goto L9;
        if ("mp4".equals(r5.format) == false) goto L7;
        RecordingNative.configure(this);     // Catch: Exception -> L10
        RecordingMedia.ensureSeekable(r0);     // Catch: Exception -> L10
    L7:
        final Entry r1 = new Entry();     // Catch: Exception -> L10
        r1.title = r5.title;     // Catch: Exception -> L10
        r1.kind = "Películas";     // Catch: Exception -> L10
        r1.url = FileProvider.getUriForFile(this, String.valueOf(getPackageName()) + ".backups", r0).toString();     // Catch: Exception -> L10
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda14(this, r5, r1));     // Catch: Exception -> L10
        return;
    L9:
        throw new IOException("No se encuentra el archivo grabado");     // Catch: Exception -> L10
    L10:
        e = move-exception;
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda15(this, e));
    }

    /* JADX INFO: renamed from: lambda$31$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m771lambda$31$esantoniom3uRecordingsActivity(RecordingTask r2, final Entry r3) {
        if (isFinishing() == false) goto L5;
        return;
    L5:
        if (isDestroyed() == true) goto L10;
        ParentalControl.requireEntry(this, r2.entry, new RecordingsActivity$$ExternalSyntheticLambda33(this, r3));
        return;
    }

    /* JADX INFO: renamed from: lambda$32$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m772lambda$32$esantoniom3uRecordingsActivity(Entry r5) {
        startActivity(new Intent(this, PlayerActivity.class).putExtra("playbackRequestedAt", SystemClock.elapsedRealtime()).putExtra("entry", r5).addFlags(1));
    }

    /* JADX INFO: renamed from: lambda$33$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m773lambda$33$esantoniom3uRecordingsActivity(Exception r2) {
        if (r2.getMessage() != null) goto L5;
        String r3 = "No se pudo abrir el archivo grabado";
    L6:
        message(r3);
        return;
    L5:
        r3 = r2.getMessage();
        goto L6
    }

    /* JADX INFO: renamed from: lambda$34$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m774lambda$34$esantoniom3uRecordingsActivity(String r5, Uri r6) {
        RecordingTask r7 = RecordingStore.get(this, r5);     // Catch: Exception -> L42
        if (r7 == null) goto L52;
        Throwable r0 = null;
        FileInputStream r1 = new FileInputStream(RecordingStore.file(this, r7));     // Catch: Throwable -> L34
        OutputStream r8 = getContentResolver().openOutputStream(r6);     // Catch: Throwable -> L27
        if (r8 == null) goto L23;
        byte[] r9 = new byte[65536];     // Catch: Throwable -> L20
    L10:
        int r2 = r1.read(r9);     // Catch: Throwable -> L20
        if (r2 == (-1)) goto L13;
        r8.write(r9, 0, r2);     // Catch: Throwable -> L20
        goto L10
    L13:
        if (r8 == null) goto L15;
        r8.close();     // Catch: Throwable -> L27
    L15:
        r1.close();     // Catch: Throwable -> L34
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda24(this));     // Catch: Exception -> L42
        return;
    L23:
        throw new IOException();     // Catch: Throwable -> L20
    L20:
        th = move-exception;
        if (r8 == null) goto L50;
        r8.close();     // Catch: Throwable -> L27
        throw th;     // Catch: Throwable -> L27
    L50:
        throw th;     // Catch: Throwable -> L27
    L27:
        th = move-exception;
        if (0 == 0) goto L31;
        if (null == th) goto L32;
        r0.addSuppressed(th);     // Catch: Throwable -> L34
    L32:
        r1.close();     // Catch: Throwable -> L34
        throw r0;     // Catch: Throwable -> L34
    L31:
        r0 = th;
    L34:
        th = move-exception;
        if (0 == 0) goto L51;
        if (null == th) goto L39;
        r0.addSuppressed(th);     // Catch: Exception -> L42
    L39:
        throw null;     // Catch: Exception -> L42
    L51:
        throw th;     // Catch: Exception -> L42
    L52:
        throw new IOException();     // Catch: Exception -> L42
    L43:
        this.main.post(new RecordingsActivity$$ExternalSyntheticLambda25(this));
    }

    /* JADX INFO: renamed from: lambda$35$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m775lambda$35$esantoniom3uRecordingsActivity() {
        message("Archivo exportado");
    }

    /* JADX INFO: renamed from: lambda$36$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m776lambda$36$esantoniom3uRecordingsActivity() {
        message("No se pudo exportar el archivo");
    }

    /* JADX INFO: renamed from: lambda$4$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m777lambda$4$esantoniom3uRecordingsActivity(List r11) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.shown = r11;
        ArrayList r12 = new ArrayList();
        SimpleDateFormat r0 = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        Iterator<RecordingTask> r1 = this.shown.iterator();
    L7:
        if (r1.hasNext() == false) goto L8;
        RecordingTask r2 = r1.next();
        String r5 = "";
        if (r2.active() == false) goto L17;
        String r4 = "🔴 ";
    L18:
        StringBuilder r3 = new StringBuilder(String.valueOf(r4)).append(r2.title).append("\n").append(r2.state).append(" · ").append(r0.format(new Date(r2.start))).append(" · ").append(RecordingStore.size(r2.bytes));
        if (r2.message.isEmpty() == true) goto L22;
        r5 = "\n" + r2.message;
    L22:
        r12.add(r3.append(r5).toString());
        goto L7
    L17:
        r4 = "";
        goto L18
    L8:
        this.adapter.clear();
        this.adapter.addAll(r12);
        this.adapter.notifyDataSetChanged();
        TextView r13 = this.heading;
        if (this.shown.isEmpty() == false) goto L11;
        String r6 = "No hay grabaciones. Selecciona Grabar en un título o en el reproductor.";
    L12:
        r13.setText(r6);
        this.loading = false;
        return;
    L11:
        r6 = String.valueOf(this.shown.size()) + " grabaciones · pulsa para ver opciones";
        goto L12
    }

    /* JADX INFO: renamed from: lambda$5$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m778lambda$5$esantoniom3uRecordingsActivity() {
        this.loading = false;
        this.heading.setText("No se pudo leer el índice de grabaciones");
    }

    /* JADX INFO: renamed from: lambda$7$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m779lambda$7$esantoniom3uRecordingsActivity(final AlertDialog r13, final EditText r14, final CheckBox r15, final SimpleDateFormat r16, final EditText r17, final Entry r18, final EditText r19, final Spinner r20, DialogInterface r21) {
        r13.getButton(-1).setOnClickListener(new RecordingsActivity$$ExternalSyntheticLambda21(this, r14, r15, r16, r17, r18, r19, r20, r13));
    }

    /* JADX INFO: renamed from: lambda$8$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m780lambda$8$esantoniom3uRecordingsActivity(EditText r4, CheckBox r5, SimpleDateFormat r6, EditText r7, Entry r8, EditText r9, Spinner r10, AlertDialog r11, View r12) {
        int r13 = Integer.parseInt(r4.getText().toString());     // Catch: Exception -> L39
        if (r13 < 1) goto L47;
        if (r13 > 360) goto L47;
        if (r5.isChecked() == false) goto L10;
        long r14 = r6.parse(r7.getText().toString()).getTime();     // Catch: Exception -> L39
    L12:
        if (r5.isChecked() == true) goto L14;
    L18:
        RecordingTask r0 = new RecordingTask();     // Catch: Exception -> L39
        r0.entry = r8;     // Catch: Exception -> L39
        r0.title = r9.getText().toString().trim();     // Catch: Exception -> L39
        if (r0.title.isEmpty() == false) goto L21;
        r0.title = r8.title;     // Catch: Exception -> L39
    L21:
        r0.start = r14;     // Catch: Exception -> L39
        r0.end = r14 + (((long) r13) * 60000);     // Catch: Exception -> L39
        if (r10.getSelectedItemPosition() != 0) goto L24;
        String r15 = "mp4";
    L25:
        r0.format = r15;     // Catch: Exception -> L39
        boolean r16 = r5.isChecked();     // Catch: Exception -> L39
        if (r16 == true) goto L28;
    L31:
        r11.dismiss();     // Catch: Exception -> L39
        if (Build.VERSION.SDK_INT >= 33) goto L34;
    L36:
        submit(r0, r16);     // Catch: Exception -> L39
        return;
    L34:
        if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") == 0) goto L36;
        this.permissionPending = r0;     // Catch: Exception -> L39
        this.permissionScheduled = r16;     // Catch: Exception -> L39
        requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 142);     // Catch: Exception -> L39
        return;
    L28:
        if (RecordingScheduler.permitted(this) == true) goto L31;
        new AlertDialog.Builder(this).setTitle("Permiso para programar").setMessage("Activa Alarmas y recordatorios y vuelve a pulsar Guardar.").setPositiveButton("Abrir ajustes", new RecordingsActivity$$ExternalSyntheticLambda26(this)).setNegativeButton("Cancelar", null).show();     // Catch: Exception -> L39
        return;
    L24:
        r15 = "mkv";
        goto L25
    L14:
        if (r14 > System.currentTimeMillis()) goto L18;
        throw new IllegalArgumentException("El inicio programado debe ser futuro");     // Catch: Exception -> L39
    L10:
        r14 = System.currentTimeMillis();     // Catch: Exception -> L39
    L47:
        throw new IllegalArgumentException("La duración debe estar entre 1 y 360 minutos");     // Catch: Exception -> L39
    L39:
        e = move-exception;
        if ((e instanceof IllegalArgumentException) == false) goto L43;
        String r17 = e.getMessage();
    L44:
        message(r17);
        return;
    L43:
        r17 = "Revisa la fecha y la duración";
        goto L44
    }

    /* JADX INFO: renamed from: lambda$9$es-antonio-m3u-RecordingsActivity, reason: not valid java name */
    /* synthetic */ void m781lambda$9$esantoniom3uRecordingsActivity(DialogInterface r2, int r3) {
        startActivity(new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM").setData(Uri.parse("package:" + getPackageName())));
    }

    @Override // android.app.Activity
    protected void onActivityResult(int r2, int r3, Intent r4) {
        super.onActivityResult(r2, r3, r4);
        if (r2 == 142) goto L5;
        return;
    L5:
        if (r3 != (-1)) goto L13;
        if (r4 != null) goto L8;
        return;
    L8:
        if (r4.getData() == null) goto L15;
        final Uri r5 = r4.getData();
        final String r6 = this.exportId;
        this.f189io.execute(new RecordingsActivity$$ExternalSyntheticLambda18(this, r6, r5));
        return;
    L15:
        return;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r7) {
        super.onCreate(r7);
        if (r7 == null) goto L5;
        this.exportId = r7.getString("exportId", "");
    L5:
        this.root = new LinearLayout(this);
        this.root.setOrientation(1);
        this.root.setPadding(chromeDp(12), chromeDp(8), chromeDp(12), chromeDp(8));
        this.root.setBackgroundColor(AppTheme.color(this, -16180435));
        setContentView(this.root);
        setChromeTitle("Grabaciones");
        Button r0 = new Button(this);
        r0.setOnClickListener(new RecordingsActivity$$ExternalSyntheticLambda0(this));
        setChromeActions(r0, null, null);
        this.heading = new TextView(this);
        this.heading.setTextColor(AppTheme.color(this, -1903617));
        this.heading.setTextSize(16.0f);
        this.heading.setText("Cargando grabaciones…");
        this.root.addView(this.heading);
        this.list = new ListView(this);
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList());
        this.adapter.setNotifyOnChange(false);
        this.list.setAdapter(this.adapter);
        this.list.setOnItemClickListener(new RecordingsActivity$$ExternalSyntheticLambda1(this));
        this.root.addView(this.list, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        final Entry r1 = (Entry) getIntent().getSerializableExtra("recordEntry");
        if (r1 == null) goto L10;
        if (r7 != null) goto L11;
        new Handler(Looper.getMainLooper()).post(new RecordingsActivity$$ExternalSyntheticLambda2(this, r1));
        return;
    L11:
        return;
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.f189io.shutdown();
        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int r3, String[] r4, int[] r5) {
        super.onRequestPermissionsResult(r3, r4, r5);
        if (r3 != 144) goto L14;
        if (this.permissionBatch == null) goto L14;
        List<RecordingTask> r6 = this.permissionBatch;
        this.permissionBatch = null;
        if (r5.length > 0) goto L9;
    L11:
        message("Activa las notificaciones para programar las grabaciones.");
        return;
    L9:
        if (r5[0] != 0) goto L11;
        submitBatch(r6);
        return;
    L14:
        if (r3 == 142) goto L16;
        return;
    L16:
        if (this.permissionPending == null) goto L27;
        RecordingTask r7 = this.permissionPending;
        this.permissionPending = null;
        if (r5.length != 0) goto L20;
    L23:
        message("Activa las notificaciones para poder ver y detener la grabación en segundo plano.");
        return;
    L20:
        if (r5[0] != 0) goto L23;
        submit(r7, this.permissionScheduled);
        return;
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle r3) {
        r3.putString("exportId", this.exportId);
        super.onSaveInstanceState(r3);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.main.post(this.refresh);
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onStop() {
        this.main.removeCallbacks(this.refresh);
        super.onStop();
    }
}
