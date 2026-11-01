package es.antonio.m3u;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes7.dex */
public final class EpgActivity extends ChdtvScreen {
    private String active;
    private final List<Entry> channels;
    private EpgData data;
    private TextView detail;
    private EpgGridView grid;
    private ImageView logo;
    private LogoLoader logos;
    private final Handler main;
    private boolean populating;
    private Spinner provider;
    private Map<Entry, String> providers;
    private Spinner section;
    private String selectedProvider;
    private String selectedSection;
    private final Runnable tick;
    private TextView title;
    private Button watch;
    private final ExecutorService worker;

    public static /* synthetic */ void $r8$lambda$J5Y4XtPbH6zKG1L25oFxcwUk6eo(EpgActivity r0, Entry r1, EpgData.Programme r2) {
        r0.details(r1, r2);
    }

    public EpgActivity() {
        this.worker = Executors.newSingleThreadExecutor();
        this.main = new Handler(Looper.getMainLooper());
        this.data = new EpgData();
        this.channels = new ArrayList();
        this.providers = new IdentityHashMap();
        this.active = "";
        this.selectedProvider = "Todos los proveedores";
        this.selectedSection = "Todas las secciones";
        this.tick = new RunnableC09901(this);
    }

    static /* synthetic */ EpgGridView access$000(EpgActivity r0) {
        return r0.grid;
    }

    static /* synthetic */ Handler access$100(EpgActivity r0) {
        return r0.main;
    }

    static /* synthetic */ int access$1000(EpgActivity r0, int r1) {
        return r0.m399dp(r1);
    }

    static /* synthetic */ boolean access$200(EpgActivity r0) {
        return r0.populating;
    }

    static /* synthetic */ Spinner access$300(EpgActivity r0) {
        return r0.provider;
    }

    static /* synthetic */ String access$400(EpgActivity r0) {
        return r0.selectedProvider;
    }

    static /* synthetic */ String access$402(EpgActivity r0, String r1) {
        r0.selectedProvider = r1;
        return r1;
    }

    static /* synthetic */ String access$500(EpgActivity r0) {
        return r0.active;
    }

    static /* synthetic */ String access$502(EpgActivity r0, String r1) {
        r0.active = r1;
        return r1;
    }

    static /* synthetic */ String access$600(EpgActivity r0) {
        return r0.selectedSection;
    }

    static /* synthetic */ String access$602(EpgActivity r0, String r1) {
        r0.selectedSection = r1;
        return r1;
    }

    static /* synthetic */ void access$700(EpgActivity r0) {
        r0.populateSections();
    }

    static /* synthetic */ void access$800(EpgActivity r0) {
        r0.filter();
    }

    static /* synthetic */ Spinner access$900(EpgActivity r0) {
        return r0.section;
    }

    private Button button(String r6, View.OnClickListener r7) {
        Button r0 = new Button(this);
        r0.setText(r6);
        r0.setTextSize(12.0f);
        r0.setAllCaps(false);
        r0.setTextColor(-1);
        r0.setMinHeight(0);
        r0.setMinimumHeight(0);
        r0.setPadding(m399dp(4), 0, m399dp(4), 0);
        StateListDrawable r1 = new StateListDrawable();
        r1.addState(new int[]{android.R.attr.state_focused}, shape(AppTheme.color(this, -16225645)));
        r1.addState(new int[]{android.R.attr.state_pressed}, shape(AppTheme.color(this, -16225645)));
        r1.addState(new int[0], shape(AppTheme.color(this, 940720218)));
        r0.setBackground(r1);
        r0.setOnClickListener(r7);
        return r0;
    }

    private void details(Entry r8, EpgData.Programme r9) {
        Button r0 = this.watch;
        if (r8 == null) goto L5;
        boolean r1 = true;
    L6:
        r0.setEnabled(r1);
        if (r8 != null) goto L10;
        this.detail.setText("No hay canales en este filtro.");
        return;
    L10:
        this.active = r8.identity();
        this.logos.show(this.logo, r8.logo);
        if (r9 != null) goto L18;
        TextView r10 = this.detail;
        StringBuilder r11 = new StringBuilder().append(r8.title).append("\n");
        if (this.data.count() != 0) goto L15;
        String r2 = "Carga una guía XMLTV desde Ajustes → EPG.";
    L16:
        r10.setText(r11.append(r2).toString());
        return;
    L15:
        r2 = "Sin programación en este horario. Puedes reproducir el canal.";
        goto L16
    L18:
        SimpleDateFormat r3 = new SimpleDateFormat("EEE d · HH:mm", Locale.getDefault());
        SimpleDateFormat r4 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        TextView r5 = this.detail;
        StringBuilder r12 = new StringBuilder().append(r8.title).append(" · ").append(r3.format(new Date(r9.start))).append("–").append(r4.format(new Date(r9.stop))).append("\n").append(r9.title).append("\n");
        if (r9.description.isEmpty() == false) goto L21;
        String r13 = "Sin descripción";
    L22:
        r5.setText(r12.append(r13).toString());
        return;
    L21:
        r13 = r9.description;
        goto L22
    L5:
        r1 = false;
        goto L6
    }

    /* JADX INFO: renamed from: dp */
    private int m399dp(int r2) {
        return Math.round(r2 * getResources().getDisplayMetrics().density);
    }

    private void filter() {
        ArrayList r0 = new ArrayList();
        Iterator<Entry> r1 = this.channels.iterator();
    L4:
        if (r1.hasNext() == false) goto L12;
        Entry r2 = r1.next();
        if (matchesProvider(r2) == false) goto L4;
        if (this.selectedSection.equals("Todas las secciones") == true) goto L11;
        if (this.selectedSection.equals(r2.group) == false) goto L4;
    L11:
        r0.add(r2);
        goto L4
    L12:
        this.grid.setChannels(r0, this.data, this.active);
        this.title.setText("TV / Guía · " + r0.size() + " canales");
    }

    private void immersiveGuide() {
        getWindow().getDecorView().setSystemUiVisibility(5894);
    }

    static /* synthetic */ void lambda$onResume$15() {
    }

    private boolean matchesProvider(Entry r3) {
        if (this.selectedProvider.equals("Todos los proveedores") == false) goto L5;
    L8:
        return true;
    L5:
        if (this.selectedProvider.equals(this.providers.get(r3)) == true) goto L8;
        return false;
    }

    private void populateProviders() {
        this.populating = true;
        TreeSet r0 = new TreeSet();
        Iterator<Entry> r1 = this.channels.iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        Entry r2 = r1.next();
        String r3 = this.providers.get(r2);
        if (r3 == null) goto L4;
        r0.add(r3);
        goto L4
    L8:
        this.selectedProvider = GuideSelection.firstProvider(r0, this.selectedProvider);
        List<String> r4 = new ArrayList();
        r4.add("Todos los proveedores");
        r4.addAll(r0);
        spinner(this.provider, r4, this.selectedProvider);
        this.populating = false;
    }

    private void populateSections() {
        this.populating = true;
        TreeSet r0 = new TreeSet();
        Iterator<Entry> r1 = this.channels.iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        Entry r2 = r1.next();
        if (matchesProvider(r2) == false) goto L4;
        r0.add(r2.group);
        goto L4
    L8:
        List<String> r3 = new ArrayList();
        r3.add("Todas las secciones");
        r3.addAll(r0);
        spinner(this.section, r3, this.selectedSection);
        this.populating = false;
    }

    private GradientDrawable shape(int r3) {
        GradientDrawable r0 = new GradientDrawable();
        r0.setColor(r3);
        r0.setCornerRadius(m399dp(8));
        r0.setStroke(m399dp(1), AppTheme.color(this, 1431229933));
        return r0;
    }

    private void spinner(Spinner r3, final List<String> r4, String r5) {
        final int r1 = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> r0 = new C09934(this, this, r1, r4);
        r0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        r3.setAdapter(r0);
        r3.setSelection(Math.max(0, r4.indexOf(r5)));
    }

    private TextView text(String r2, int r3, int r4) {
        TextView r0 = new TextView(this);
        r0.setText(r2);
        r0.setTextSize(r3);
        r0.setTextColor(r4);
        r0.setGravity(16);
        return r0;
    }

    private void watch() {
        Entry r0 = this.grid.selectedChannel();
        if (r0 != null) goto L6;
        return;
    L6:
        if (getIntent().getBooleanExtra("pick", false) == false) goto L8;
        setResult(-1, new Intent().putExtra("entry", r0));
        finish();
        return;
    L8:
        startActivity(new Intent(this, PlayerActivity.class).putExtra("playbackRequestedAt", SystemClock.elapsedRealtime()).putExtra("entry", r0));
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ WindowInsets m564lambda$onCreate$0$esantoniom3uEpgActivity(LinearLayout r5, View r6, WindowInsets r7) {
        r5.setPadding(m399dp(8) + r7.getSystemWindowInsetLeft(), m399dp(4), m399dp(8) + r7.getSystemWindowInsetRight(), m399dp(4));
        return r7;
    }

    /* JADX INFO: renamed from: lambda$onCreate$1$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m565lambda$onCreate$1$esantoniom3uEpgActivity(View r1) {
        finish();
    }

    /* JADX INFO: renamed from: lambda$onCreate$10$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m566lambda$onCreate$10$esantoniom3uEpgActivity(List r3, Map r4) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (isFinishing() == true) goto L21;
        this.channels.addAll(r3);
        this.providers = r4;
        Iterator<Entry> r5 = this.channels.iterator();
    L9:
        if (r5.hasNext() == false) goto L13;
        Entry r6 = r5.next();
        if (r6.identity().equals(this.active) == false) goto L9;
        this.selectedProvider = this.providers.get(r6);
        goto L9
    L13:
        populateProviders();
        populateSections();
        filter();
        this.grid.requestFocus();
        this.detail.setText("Leyendo programación guardada… Los canales ya están disponibles.");
        return;
    }

    /* JADX INFO: renamed from: lambda$onCreate$11$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m567lambda$onCreate$11$esantoniom3uEpgActivity(EpgData r2) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (isFinishing() == true) goto L13;
        this.data = r2;
        filter();
        if (r2.count() != 0) goto L12;
        this.detail.setText("Buscando programación EPG… Los canales ya están disponibles.");
        return;
    L12:
        return;
    }

    /* JADX INFO: renamed from: lambda$onCreate$12$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m568lambda$onCreate$12$esantoniom3uEpgActivity(EpgData r2) {
        if (isDestroyed() == false) goto L5;
        return;
    L5:
        if (isFinishing() == true) goto L10;
        this.data = r2;
        filter();
        return;
    }

    /* JADX INFO: renamed from: lambda$onCreate$13$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m569lambda$onCreate$13$esantoniom3uEpgActivity() {
        if (isDestroyed() == true) goto L6;
        this.detail.setText("No se pudo abrir el catálogo. Importa una lista desde Ajustes.");
        return;
    }

    /* JADX INFO: renamed from: lambda$onCreate$14$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m570lambda$onCreate$14$esantoniom3uEpgActivity() {
        long r3 = SystemClock.elapsedRealtime();
        AppStorage r5 = new AppStorage(getApplicationContext());     // Catch: Exception -> L27
        CatalogIndex r6 = AppStorage.indexFor(r5.catalog());     // Catch: Exception -> L27
        final Map<Entry, String> r7 = r6.providers;     // Catch: Exception -> L27
        final ArrayList r8 = new ArrayList();     // Catch: Exception -> L27
        HashSet r9 = new HashSet();     // Catch: Exception -> L27
        Iterator<Entry> r10 = r6.select("Canales", null, null, null).iterator();     // Catch: Exception -> L27
    L5:
        if (r10.hasNext() == false) goto L11;
        Entry r11 = r10.next();     // Catch: Exception -> L27
        if ("Canales".equals(r11.kind) == false) goto L5;
        if (r9.add(r11.identity() + "\n" + r7.get(r11)) == false) goto L5;
        r8.add(r11);     // Catch: Exception -> L27
        goto L5
    L11:
        runOnUiThread(new EpgActivity$$ExternalSyntheticLambda11(this, r8, r7));     // Catch: Exception -> L27
        BlackBox.event("EPG_CHANNELS_READY", "channels=" + r8.size() + " duration_ms=" + (SystemClock.elapsedRealtime() - r3));     // Catch: Exception -> L27
        long r4 = SystemClock.elapsedRealtime();     // Catch: Exception -> L27
        final EpgData r1 = new EpgData();     // Catch: Exception -> L27
        Object r12 = r5.read("epg.dat");     // Catch: Exception -> L19
        if ((r12 instanceof EpgData) == false) goto L20;
        EpgData r13 = (EpgData) r12;     // Catch: Exception -> L19
        r13.ensureIndexed();     // Catch: Exception -> L17
        r1 = r13;
    L18:
        r1 = r13;
    L20:
        runOnUiThread(new EpgActivity$$ExternalSyntheticLambda12(this, r1));     // Catch: Exception -> L27
        BlackBox.event("EPG_GUIDE_CACHE", "programmes=" + r1.count() + " duration_ms=" + (SystemClock.elapsedRealtime() - r4));     // Catch: Exception -> L27
        if (r8.isEmpty() == true) goto L44;
        long r14 = SystemClock.elapsedRealtime();     // Catch: Exception -> L27
        final EpgData r15 = BackgroundWork.guide(new EpgRepository(getApplicationContext()));     // Catch: Exception -> L27
        if (r15 == r1) goto L25;
        runOnUiThread(new EpgActivity$$ExternalSyntheticLambda13(this, r15));     // Catch: Exception -> L27
    L25:
        BlackBox.event("EPG_GUIDE_REFRESH", "programmes=" + r15.count() + " duration_ms=" + (SystemClock.elapsedRealtime() - r14));     // Catch: Exception -> L27
        return;
    L44:
        return;
    L27:
        e = move-exception;
        BlackBox.event("EPG_GUIDE_ERROR", e.getClass().getSimpleName());
        runOnUiThread(new EpgActivity$$ExternalSyntheticLambda14(this));
    }

    /* JADX INFO: renamed from: lambda$onCreate$2$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m571lambda$onCreate$2$esantoniom3uEpgActivity(View r2) {
        this.grid.shift(-1);
    }

    /* JADX INFO: renamed from: lambda$onCreate$3$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m572lambda$onCreate$3$esantoniom3uEpgActivity(View r1) {
        this.grid.now();
    }

    /* JADX INFO: renamed from: lambda$onCreate$4$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m573lambda$onCreate$4$esantoniom3uEpgActivity(View r2) {
        this.grid.shift(1);
    }

    /* JADX INFO: renamed from: lambda$onCreate$5$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m574lambda$onCreate$5$esantoniom3uEpgActivity(View r1) {
        this.detail.performClick();
    }

    /* JADX INFO: renamed from: lambda$onCreate$6$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m575lambda$onCreate$6$esantoniom3uEpgActivity(Entry r1, EpgData.Programme r2, DialogInterface r3, int r4) {
        RecordingUi.programme(this, r1, r2);
    }

    /* JADX INFO: renamed from: lambda$onCreate$7$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m576lambda$onCreate$7$esantoniom3uEpgActivity(View r5) {
        final Entry r6 = this.grid.selectedChannel();
        final EpgData.Programme r0 = this.grid.selectedProgramme();
        if (r6 == null) goto L7;
        if (r0 == null) goto L8;
        AlertDialog r7 = new AlertDialog.Builder(this).setTitle(r6.title + " · " + r0.title).setMessage(this.detail.getText()).setNeutralButton("Programar grabación", new EpgActivity$$ExternalSyntheticLambda15(this, r6, r0)).setPositiveButton("Cerrar", null).create();
        r7.show();
        r7.getWindow().setBackgroundDrawable(shape(AppTheme.color(this, -300795569)));
        return;
    L8:
        return;
    }

    /* JADX INFO: renamed from: lambda$onCreate$8$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m577lambda$onCreate$8$esantoniom3uEpgActivity(View r4) {
        RecordingUi.choose(this, this.grid.selectedChannel(), this.grid.selectedProgramme(), this.data.schedule(this.grid.selectedChannel()));
    }

    /* JADX INFO: renamed from: lambda$onCreate$9$es-antonio-m3u-EpgActivity, reason: not valid java name */
    /* synthetic */ void m578lambda$onCreate$9$esantoniom3uEpgActivity(Entry r6) {
        EpgData.Programme r0 = this.grid.selectedProgramme();
        if (r0 != null) goto L5;
    L7:
        watch();
        return;
    L5:
        if (r0.start <= System.currentTimeMillis()) goto L7;
        RecordingUi.programme(this, r6, r0);
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    public void onCreate(Bundle r12) {
        super.onCreate(r12);
        setRequestedOrientation(6);
        getWindow().addFlags(128);
        this.logos = new LogoLoader(this);
        Entry r13 = (Entry) getIntent().getSerializableExtra("entry");
        if (r13 == null) goto L5;
        this.active = r13.identity();
    L5:
        final LinearLayout r14 = new LinearLayout(this);
        r14.setOrientation(1);
        r14.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{AppTheme.color(this, -16313825), AppTheme.color(this, -15320240), AppTheme.color(this, -16247260)}));
        r14.setPadding(m399dp(8), m399dp(4), m399dp(8), m399dp(4));
        r14.setOnApplyWindowInsetsListener(new EpgActivity$$ExternalSyntheticLambda0(this, r14));
        getWindow().setContentView(r14);
        immersiveGuide();
        LinearLayout r1 = new LinearLayout(this);
        r1.setGravity(16);
        r1.setBackground(shape(AppTheme.color(this, 1712470096)));
        r14.addView(r1, new LinearLayout.LayoutParams(-1, m399dp(48)));
        r1.addView(button("‹", new EpgActivity$$ExternalSyntheticLambda2(this)), new LinearLayout.LayoutParams(m399dp(40), -1));
        this.title = text("TV / Guía · Cargando…", 16, AppTheme.color(this, -4855308));
        this.provider = new Spinner(this);
        this.section = new Spinner(this);
        r1.addView(this.provider, new LinearLayout.LayoutParams(0, -1, 1.0f));
        r1.addView(this.section, new LinearLayout.LayoutParams(0, -1, 1.0f));
        r1.addView(button("−1 h", new EpgActivity$$ExternalSyntheticLambda3(this)), new LinearLayout.LayoutParams(m399dp(48), -1));
        r1.addView(button("Ahora", new EpgActivity$$ExternalSyntheticLambda4(this)), new LinearLayout.LayoutParams(m399dp(54), -1));
        r1.addView(button("+1 h", new EpgActivity$$ExternalSyntheticLambda5(this)), new LinearLayout.LayoutParams(m399dp(48), -1));
        r1.addView(button("Info", new EpgActivity$$ExternalSyntheticLambda6(this)), new LinearLayout.LayoutParams(m399dp(48), -1));
        this.grid = new EpgGridView(this);
        r14.addView(this.grid, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.grid.setSelectionListener(new EpgActivity$$ExternalSyntheticLambda7(this));
        LinearLayout r15 = new LinearLayout(this);
        r15.setGravity(16);
        r15.setPadding(m399dp(8), m399dp(4), m399dp(8), m399dp(4));
        r15.setBackgroundColor(AppTheme.color(this, -1072547243));
        this.logo = new ImageView(this);
        this.logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        r15.addView(this.logo, new LinearLayout.LayoutParams(m399dp(68), m399dp(68)));
        this.detail = text("Cargando canales y programación…", 13, -1);
        this.detail.setMaxLines(4);
        this.detail.setEllipsize(TextUtils.TruncateAt.END);
        this.detail.setPadding(m399dp(10), 0, m399dp(10), 0);
        r15.addView(this.detail, new LinearLayout.LayoutParams(0, -1, 1.0f));
        this.detail.setOnClickListener(new EpgActivity$$ExternalSyntheticLambda8(this));
        this.watch = button("Grabar", new EpgActivity$$ExternalSyntheticLambda9(this));
        this.watch.setEnabled(false);
        r1.addView(this.watch, new LinearLayout.LayoutParams(m399dp(72), -1));
        this.grid.setActivationListener(new EpgActivity$$ExternalSyntheticLambda10(this));
        this.provider.setOnItemSelectedListener(new C09912(this));
        this.section.setOnItemSelectedListener(new C09923(this));
        this.worker.execute(new EpgActivity$$ExternalSyntheticLambda1(this));
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.worker.shutdownNow();
        if (this.logos == null) goto L5;
        this.logos.close();
    L5:
        this.main.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onResume() {
        super.onResume();
        immersiveGuide();
        RecordingIndicator.attach(this);
        ParentalControl.requireKey(this, "kind:Canales", new EpgActivity$$ExternalSyntheticLambda16());
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.main.post(this.tick);
    }

    @Override // es.antonio.m3u.ChdtvScreen, android.app.Activity
    protected void onStop() {
        this.main.removeCallbacks(this.tick);
        super.onStop();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean r1) {
        super.onWindowFocusChanged(r1);
        if (r1 == false) goto L6;
        immersiveGuide();
        return;
    }

    void refreshRecordingIndicator() {
        if (this.grid == null) goto L6;
        this.grid.invalidate();
        return;
    }
}
