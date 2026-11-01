package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.media3.extractor.p001ts.TsExtractor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes3.dex */
public final class CdnTokenActivity extends ChdtvScreen {
    private int accountGeneration;
    private TextView accountLabel;
    private CheckBox autoLists;
    private final ExecutorService catalogWorker;
    private final Map<String, CheckBox> choices;
    private boolean listsReady;
    private EditText password;
    private Button renew;
    private Button save;
    private String savedUsername;
    private TextView status;
    private final Runnable tick;
    private final Handler timer;
    private EditText user;
    private boolean working;

    public static /* synthetic */ void $r8$lambda$0TFEebIDpatgXzp8MtIi20L_LOw(CdnTokenActivity r0, DialogInterface r1, int r2) {
        r0.lambda$onCreate$8(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$4805Q9gI-x20mkOEQWJo9sDAXcI, reason: not valid java name */
    public static /* synthetic */ void m532$r8$lambda$4805Q9gIx20mkOEQWJo9sDAXcI(CdnTokenActivity r0, Boolean r1, Throwable r2) {
        r0.lambda$runRenew$14(r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$99IeSAHAMY-haK-uOj-zb3d8ucU, reason: not valid java name */
    public static /* synthetic */ void m533$r8$lambda$99IeSAHAMYhaKuOjzb3d8ucU(CdnTokenActivity r0, int r1, String r2, String r3) {
        r0.lambda$loadAccount$10(r1, r2, r3);
    }

    public static /* synthetic */ WindowInsets $r8$lambda$EKcLR_uQf_keLeSvjL66BUiKQ34(CdnTokenActivity r0, LinearLayout r1, View r2, WindowInsets r3) {
        return r0.lambda$onCreate$2(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$LjXVt_JeW6WPvhOGgf2BOc3G6nI(CdnTokenActivity r0, LinearLayout r1) {
        r0.lambda$onCreate$5(r1);
    }

    /* JADX INFO: renamed from: $r8$lambda$NEWVNntBEsRjvRG4ktI-W9JDlnU, reason: not valid java name */
    public static /* synthetic */ void m534$r8$lambda$NEWVNntBEsRjvRG4ktIW9JDlnU(CdnTokenActivity r0) {
        r0.save();
    }

    /* JADX INFO: renamed from: $r8$lambda$PSSzjp_GR_T_t2voNUgXEQY-cTk, reason: not valid java name */
    public static /* synthetic */ void m535$r8$lambda$PSSzjp_GR_T_t2voNUgXEQYcTk(CdnTokenActivity r0, LinearLayout r1, List r2, Set r3) {
        r0.lambda$onCreate$4(r1, r2, r3);
    }

    public static /* synthetic */ void $r8$lambda$RVILrqt6VBOTZux51Bj1pvtSQqY(CdnTokenActivity r0, int r1) {
        r0.lambda$loadAccount$11(r1);
    }

    public static /* synthetic */ void $r8$lambda$iJ2Y1kZHQpUkvr9yiP97Bo9mftI(CdnTokenActivity r0, LinearLayout r1) {
        r0.lambda$onCreate$6(r1);
    }

    public static /* synthetic */ void $r8$lambda$jOYzTzPVWqkw_cHbkaquju7MfHc(CdnTokenActivity r0) {
        r0.lambda$onCreate$9();
    }

    public static /* synthetic */ void $r8$lambda$ouPJBye1uOZzcofbPe38_AvdAxA(CdnTokenActivity r0) {
        r0.lambda$runRenew$13();
    }

    public static /* synthetic */ void $r8$lambda$v8KC6ohjO0OZRUoYCu_Lj8kFoao(CdnTokenActivity r0) {
        r0.lambda$onCreate$7();
    }

    public static /* synthetic */ void $r8$lambda$wKtpLPTQOlEkqObrcCd2ga2zFdk(CdnTokenActivity r0) {
        r0.lambda$onResume$15();
    }

    /* JADX INFO: renamed from: $r8$lambda$xMOSXORAg3B-Y3UP8Iy35eh4j3M, reason: not valid java name */
    public static /* synthetic */ void m536$r8$lambda$xMOSXORAg3BY3UP8Iy35eh4j3M(CdnTokenActivity r0) {
        r0.lambda$onCreate$3();
    }

    /* JADX INFO: renamed from: -$$Nest$fgetstatus, reason: not valid java name */
    static /* bridge */ /* synthetic */ TextView m537$$Nest$fgetstatus(CdnTokenActivity r0) {
        return r0.status;
    }

    /* JADX INFO: renamed from: -$$Nest$fgettimer, reason: not valid java name */
    static /* bridge */ /* synthetic */ Handler m538$$Nest$fgettimer(CdnTokenActivity r0) {
        return r0.timer;
    }

    public CdnTokenActivity() {
        this.savedUsername = "";
        this.choices = new LinkedHashMap();
        this.catalogWorker = Executors.newSingleThreadExecutor(new CdnTokenActivity$$ExternalSyntheticLambda7());
        this.timer = new Handler(Looper.getMainLooper());
        this.tick = new RunnableC09721(this);
    }

    private Button add(LinearLayout r5, String r6, final Runnable r7) {
        Button r0 = new Button(this);
        r0.setText(r6);
        r0.setAllCaps(false);
        r0.setOnClickListener(new CdnTokenActivity$$ExternalSyntheticLambda9(r7));
        r5.addView(r0, new LinearLayout.LayoutParams(-1, m392dp(52)));
        return r0;
    }

    private Catalog catalog() {
        return new AppStorage(this).catalog();
    L6:
        return new Catalog();
    }

    /* JADX INFO: renamed from: dp */
    private int m392dp(int r3) {
        return Math.round(r3 * getResources().getDisplayMetrics().density);
    }

    static /* synthetic */ void lambda$add$12(Runnable r0, View r1) {
        r0.run();
    }

    private /* synthetic */ void lambda$loadAccount$10(int r2, String r3, String r4) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (r2 != this.accountGeneration) goto L13;
        this.savedUsername = r3;
        if (this.user.getText().length() != 0) goto L10;
        this.user.setText(r3);
    L10:
        this.accountLabel.setText(r4);
        return;
    }

    private /* synthetic */ void lambda$loadAccount$11(final int r6) {
        String r0 = "";
        String r1 = "No hay una cuenta guardada";
        if (CdnTokens.hasAccount(this) == true) goto L10;
    L8:
        final String r2 = r0;
        final String r3 = r1;
        runOnUiThread(new CdnTokenActivity$$ExternalSyntheticLambda11(this, r6, r2, r3));
        return;
    L10:
        r0 = CdnTokens.account(this).getString("user");     // Catch: Exception -> L6
        r1 = "Cuenta guardada · " + r0;     // Catch: Exception -> L6
    L7:
        r1 = "Hay una cuenta guardada, pero no se puede leer. Introduce de nuevo usuario y contraseña.";
        goto L8
    }

    static /* synthetic */ void lambda$new$0(Runnable r1) {
        Process.setThreadPriority(10);
        r1.run();
    }

    static /* synthetic */ Thread lambda$new$1(final Runnable r3) {
        return new Thread(new CdnTokenActivity$$ExternalSyntheticLambda8(r3), "CHDTVTokenCatalog");
    }

    private /* synthetic */ WindowInsets lambda$onCreate$2(LinearLayout r6, View r7, WindowInsets r8) {
        r6.setPadding(m392dp(24) + r8.getSystemWindowInsetLeft(), m392dp(24) + r8.getSystemWindowInsetTop(), m392dp(24) + r8.getSystemWindowInsetRight(), m392dp(24) + r8.getSystemWindowInsetBottom());
        return r8;
    }

    private /* synthetic */ void lambda$onCreate$3() {
        finish();
    }

    private /* synthetic */ void lambda$onCreate$4(LinearLayout r6, List r7, Set r8) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        r6.removeAllViews();
        Iterator r0 = r7.iterator();
    L7:
        if (r0.hasNext() == false) goto L10;
        String[] r1 = (String[]) r0.next();
        CheckBox r3 = new CheckBox(this);
        r3.setText(r1[1]);
        r3.setTextColor(-1);
        r3.setChecked(r8.contains(r1[0]));
        this.choices.put(r1[0], r3);
        r6.addView(r3);
        goto L7
    L10:
        if (this.choices.isEmpty() == false) goto L12;
        r6.addView(text("Importa una lista de canales, películas o series del CDN Movistar.", 14));
    L12:
        this.listsReady = true;
        this.save.setEnabled(!this.working);
    }

    private /* synthetic */ void lambda$onCreate$5(final LinearLayout r10) {
        Catalog r0 = catalog();
        CdnTokens.reconcile(this, r0);
        final HashSet r1 = new HashSet(CdnTokens.prefs(this).getStringSet("sources", Collections.emptySet()));
        final ArrayList r2 = new ArrayList();
        Iterator<Catalog.Source> r3 = r0.sources.iterator();
    L4:
        if (r3.hasNext() == false) goto L8;
        Catalog.Source r4 = r3.next();
        if (CdnTokens.eligible(r4) == false) goto L4;
        r2.add(new String[]{r4.f177id, r4.name + " · " + r4.kind + " · " + DemandTokenSources.label(r4.entries)});
        goto L4
    L8:
        runOnUiThread(new CdnTokenActivity$$ExternalSyntheticLambda12(this, r10, r2, r1));
    }

    private /* synthetic */ void lambda$onCreate$6(final LinearLayout r3) {
        this.catalogWorker.execute(new CdnTokenActivity$$ExternalSyntheticLambda15(this, r3));
    }

    private /* synthetic */ void lambda$onCreate$7() {
        runRenew();
    }

    private /* synthetic */ void lambda$onCreate$8(DialogInterface r4, int r5) {
        CdnTokens.disable(this);
        this.savedUsername = "";
        this.user.setText("");
        this.password.setText("");
        loadAccount();
        Iterator<CheckBox> r0 = this.choices.values().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().setChecked(false);
        goto L4
    L6:
        this.status.setText(CdnTokens.status(this));
    }

    private /* synthetic */ void lambda$onCreate$9() {
        new AlertDialog.Builder(this).setTitle("Borrar cuenta guardada").setMessage("Se detendrá la renovación. Tus listas importadas se conservarán.").setNegativeButton("Cancelar", null).setPositiveButton("Borrar", new CdnTokenActivity$$ExternalSyntheticLambda14(this)).show();
    }

    private /* synthetic */ void lambda$onResume$15() {
        this.timer.post(this.tick);
    }

    private /* synthetic */ void lambda$runRenew$13() {
        this.working = false;
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        this.save.setEnabled(true);
        this.renew.setEnabled(true);
        this.status.setText(CdnTokens.status(this));
        this.timer.postDelayed(this.tick, 1000);
    }

    private /* synthetic */ void lambda$runRenew$14(Boolean r2, Throwable r3) {
        runOnUiThread(new CdnTokenActivity$$ExternalSyntheticLambda10(this));
    }

    private void loadAccount() {
        final int r0 = this.accountGeneration + 1;
        this.accountGeneration = r0;
        this.catalogWorker.execute(new CdnTokenActivity$$ExternalSyntheticLambda0(this, r0));
    }

    private void runRenew() {
        if (this.working == false) goto L6;
        return;
    L6:
        if (CdnTokens.configured(this) == true) goto L9;
        this.status.setText("Guarda primero la cuenta y sus listas");
        return;
    L9:
        this.working = true;
        this.save.setEnabled(false);
        this.renew.setEnabled(false);
        this.timer.removeCallbacks(this.tick);
        this.status.setText("Renovando token…");
        CdnTokens.renew(this, true).whenComplete(new CdnTokenActivity$$ExternalSyntheticLambda13(this));
    }

    private void save() {
        if (this.working == false) goto L5;
        return;
    L5:
        if (this.listsReady == false) goto L42;
        HashSet r0 = new HashSet();
        Iterator<Map.Entry<String, CheckBox>> r1 = this.choices.entrySet().iterator();
    L9:
        if (r1.hasNext() == false) goto L14;
        Map.Entry<String, CheckBox> r2 = r1.next();
        if (r2.getValue().isChecked() == false) goto L9;
        r0.add(r2.getKey());
        goto L9
    L14:
        if (r0.isEmpty() == false) goto L17;
        this.status.setText("Selecciona al menos una lista");
        return;
    L17:
        String r3 = this.user.getText().toString().trim();
        String r4 = this.password.getText().toString();
        if (CdnTokens.keepAccount(CdnTokens.hasAccount(this), this.savedUsername, r3, r4) == false) goto L23;
        Set<String> r5 = this.choices.keySet();     // Catch: Exception -> L31
        boolean r6 = this.autoLists.isChecked();     // Catch: Exception -> L31
    L21:
        CdnTokens.selectSources(this, r0, r5, r6);     // Catch: Exception -> L31
        this.password.setText("");     // Catch: Exception -> L31
        loadAccount();     // Catch: Exception -> L31
        this.password.setHint("Contraseña (deja vacía para conservar la cuenta)");     // Catch: Exception -> L31
        runRenew();     // Catch: Exception -> L31
        return;
    L23:
        if (r3.isEmpty() == false) goto L25;
    L29:
        this.status.setText("Introduce el usuario y la contraseña de tu cuenta");     // Catch: Exception -> L31
        return;
    L25:
        if (r4.isEmpty() == true) goto L29;
        CdnTokens.configure(this, r3, r4, r0);     // Catch: Exception -> L31
        r5 = this.choices.keySet();     // Catch: Exception -> L31
        r6 = this.autoLists.isChecked();     // Catch: Exception -> L31
        goto L21
    L32:
        this.status.setText("No se pudo guardar la cuenta de forma segura");
        return;
    }

    private TextView text(String r5, int r6) {
        TextView r0 = new TextView(this);
        r0.setText(r5);
        r0.setTextSize(r6);
        r0.setTextColor(-1181697);
        r0.setPadding(0, m392dp(10), 0, m392dp(10));
        return r0;
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r12) {
        super.onCreate(r12);
        getWindow().addFlags(8192);
        ScrollView r0 = new ScrollView(this);
        r0.setBackgroundColor(-16115415);
        final LinearLayout r1 = new LinearLayout(this);
        r1.setOrientation(1);
        r1.setPadding(m392dp(24), m392dp(24), m392dp(24), m392dp(24));
        r0.addView(r1);
        setContentView(r0);
        r0.setOnApplyWindowInsetsListener(new CdnTokenActivity$$ExternalSyntheticLambda16(this, r1));
        add(r1, "‹ Volver", new CdnTokenActivity$$ExternalSyntheticLambda1(this));
        r1.addView(text("Token Movistar · tu cuenta", 24));
        r1.addView(text("Renueva al abrir la app y cada 11 h 55 min. Inicia sesión, obtiene el token y cierra/elimina el dispositivo temporal. Android puede retrasar las tareas en segundo plano.", 14));
        r1.addView(text("La cuenta se guarda cifrada en este dispositivo. Tras restaurar una copia o cambiar de móvil tendrás que introducirla otra vez. Aplica el token renovado al reproducir canales, películas y series de las listas seleccionadas, aunque no tengan cabecera x-tcdn-token. También sustituye ese parámetro cuando ya está en la URL; conserva las demás opciones.", 14));
        this.user = new EditText(this);
        this.user.setHint("Usuario de tu cuenta Movistar");
        this.user.setTextColor(-1);
        this.user.setHintTextColor(-6833968);
        this.user.setSingleLine();
        this.user.setInputType(33);
        this.user.setSaveEnabled(false);
        r1.addView(this.user);
        this.accountLabel = text("", 14);
        this.accountLabel.setTextColor(-11149840);
        r1.addView(this.accountLabel);
        loadAccount();
        this.password = new EditText(this);
        EditText r4 = this.password;
        if (CdnTokens.hasAccount(this) == false) goto L5;
        String r10 = "Contraseña (deja vacía para conservar la cuenta)";
    L6:
        r4.setHint(r10);
        this.password.setTextColor(-1);
        this.password.setHintTextColor(-6833968);
        this.password.setSingleLine();
        this.password.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        this.password.setSaveEnabled(false);
        r1.addView(this.password);
        this.autoLists = new CheckBox(this);
        this.autoLists.setText("Vincular automáticamente las nuevas listas Movistar a esta cuenta");
        this.autoLists.setTextColor(-1);
        this.autoLists.setChecked(CdnTokens.prefs(this).getBoolean("auto_new_sources", true));
        r1.addView(this.autoLists);
        r1.addView(text("Listas que usan esta cuenta", 18));
        final LinearLayout r5 = new LinearLayout(this);
        r5.setOrientation(1);
        r1.addView(r5);
        r5.addView(text("Cargando listas vinculables…", 14));
        this.timer.post(new CdnTokenActivity$$ExternalSyntheticLambda2(this, r5));
        this.status = text(CdnTokens.status(this), 16);
        this.status.setTextColor(-11149840);
        r1.addView(this.status);
        this.save = add(r1, "Guardar y renovar", new CdnTokenActivity$$ExternalSyntheticLambda3(this));
        this.save.setEnabled(false);
        this.renew = add(r1, "Renovar ahora", new CdnTokenActivity$$ExternalSyntheticLambda4(this));
        add(r1, "Desactivar y borrar cuenta", new CdnTokenActivity$$ExternalSyntheticLambda5(this));
        return;
    L5:
        r10 = "Contraseña";
        goto L6
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.catalogWorker.shutdownNow();
        this.timer.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.timer.removeCallbacks(this.tick);
        super.onPause();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        ParentalControl.authenticate(this, new CdnTokenActivity$$ExternalSyntheticLambda6(this), true);
    }
}
