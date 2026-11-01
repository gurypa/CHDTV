package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.AacUtil;
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/* JADX INFO: loaded from: classes3.dex */
final class ParentalControl {
    static int failures;
    private static final ExecutorService loader = null;
    static final Map<Activity, List<Runnable>> pending = null;
    static volatile boolean ready;
    static long retryAt;
    static boolean unlocked;

    static {
        pending = new WeakHashMap();
        loader = Executors.newSingleThreadExecutor(new ParentalControl$$ExternalSyntheticLambda10());
        unlocked = false;
        retryAt = 0;
        failures = 0;
    }

    ParentalControl() {
    }

    static void afterLoaded(final Activity r3, final Runnable r4) {
        if (ready == false) goto L6;
        r4.run();
        return;
    L6:
        final View r0 = r3.findViewById(android.R.id.content);
        if (r0 == null) goto L9;
        r0.setVisibility(4);
    L9:
        loader.execute(new ParentalControl$$ExternalSyntheticLambda2(r3, r0, r4));
    }

    static void authenticate(Activity r1, Runnable r2) {
        authenticate(r1, r2, false);
    }

    static void authenticate(final Activity r7, Runnable r8, final boolean r9) {
        if (enabled(r7) == false) goto L14;
        if (unlocked == true) goto L14;
        if (pending.containsKey(r7) == false) goto L11;
        pending.get(r7).add(r8);
        return;
    L11:
        final View r5 = r7.findViewById(android.R.id.content);
        r5.setVisibility(4);
        final ArrayList r6 = new ArrayList();
        r6.add(r8);
        pending.put(r7, r6);
        final EditText r3 = field(r7, "PIN de 4 a 8 cifras");
        final AlertDialog r2 = new AlertDialog.Builder(r7).setTitle("Control parental").setView(r3).setPositiveButton("Desbloquear", null).setNegativeButton("Cancelar", new ParentalControl$$ExternalSyntheticLambda19(r7, r5, r9)).setCancelable(false).create();
        r2.setOnShowListener(new ParentalControl$$ExternalSyntheticLambda20(r2, r3, r7, r5, r6));
        r2.show();
        return;
    L14:
        r8.run();
    }

    static Set<String> blocked(Context r4) {
        return new HashSet(prefs(r4).getStringSet("blocked", Collections.emptySet()));
    }

    static boolean blocks(Context r3, String r4) {
        if (enabled(r3) == true) goto L5;
        return false;
    L5:
        if (prefs(r3).getBoolean(TtmlNode.COMBINE_ALL, false) == false) goto L7;
    L12:
        return true;
    L7:
        if (blocked(r3).contains(r4) == true) goto L12;
        if (r4.equals("kind:Plex") == true) goto L11;
        return false;
    L11:
        if (blocked(r3).contains("provider:Plex") == true) goto L12;
        return false;
    }

    static void change(Context r3, String r4, boolean r5) {
        Set<String> r0 = blocked(r3);
        if (r5 == false) goto L5;
        r0.add(r4);
    L6:
        prefs(r3).edit().putStringSet("blocked", r0).commit();
        return;
    L5:
        r0.remove(r4);
        goto L6
    }

    static void choose(final Activity r12, boolean r13) {
        final TreeMap r2 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        Iterator<Catalog.Source> r3 = new AppStorage(r12).catalog().sources.iterator();     // Catch: Exception -> L37
    L5:
        if (r3.hasNext() == false) goto L24;
        Catalog.Source r4 = r3.next();     // Catch: Exception -> L37
        Iterator<Entry> r5 = r4.entries.iterator();     // Catch: Exception -> L37
    L8:
        if (r5.hasNext() == false) goto L5;
        Entry r6 = r5.next();     // Catch: Exception -> L37
        if (r13 == true) goto L11;
        StringBuilder r8 = new StringBuilder().append(r6.kind).append(" · ");     // Catch: Exception -> L37
        if (r6.kind.equals("Series") == false) goto L15;
        String r9 = r6.series + " · S" + r6.season + ExifInterface.LONGITUDE_EAST + r6.episode + " · ";     // Catch: Exception -> L37
    L16:
        r2.put(r8.append(r9).append(r6.title).append(" · ").append(r6.group).toString(), "entry:" + WatchHistory.m418id(r6));     // Catch: Exception -> L37
        if (r6.kind.equals("Series") == false) goto L8;
        StringBuilder r10 = new StringBuilder().append("Serie completa · ");     // Catch: Exception -> L37
        if (r6.series.isEmpty() == false) goto L21;
        String r11 = r6.group;     // Catch: Exception -> L37
    L22:
        r2.put(r10.append(r11).append(" · ").append(r6.group).toString(), seriesKey(r6));     // Catch: Exception -> L37
        goto L8
    L21:
        r11 = r6.series;     // Catch: Exception -> L37
        goto L22
    L15:
        r9 = "";
        goto L16
    L11:
        String r14 = Providers.name(r6, r4.name);     // Catch: Exception -> L37
        r2.put(r14, "provider:" + r14);     // Catch: Exception -> L37
        goto L8
    L24:
        if (r13 == false) goto L26;
        String r0 = "Plex";
    L27:
        if (r13 == false) goto L29;
        String r1 = "provider:Plex";
    L30:
        r2.put(r0, r1);     // Catch: Exception -> L37
        final Set<String> r15 = blocked(r12);
        final ArrayList r7 = new ArrayList();
        LinearLayout r16 = new LinearLayout(r12);
        r16.setOrientation(1);
        final EditText r17 = new EditText(r12);
        r17.setHint("Buscar para elegir qué bloquear");
        r17.setSingleLine();
        r16.addView(r17);
        final ListView r18 = new ListView(r12);
        r18.setChoiceMode(2);
        r16.addView(r18, new LinearLayout.LayoutParams(-1, Math.min((int) (r12.getResources().getDisplayMetrics().density * 360.0f), r12.getResources().getDisplayMetrics().heightPixels / 2)));
        final ArrayAdapter r19 = new ArrayAdapter(r12, android.R.layout.simple_list_item_multiple_choice, r7);
        r18.setAdapter(r19);
        final Runnable r20 = new ParentalControl$$ExternalSyntheticLambda0(r7, r17, r2, r19, r18, r15);
        r18.setOnItemClickListener(new ParentalControl$$ExternalSyntheticLambda11(r2, r7, r18, r15));
        r17.addTextChangedListener(new C10081(r20));
        r20.run();
        AlertDialog.Builder r21 = new AlertDialog.Builder(r12);
        if (r13 == false) goto L34;
        String r22 = "Bloquear proveedores";
    L35:
        r21.setTitle(r22).setView(r16).setPositiveButton("Guardar", new ParentalControl$$ExternalSyntheticLambda13(r12, r15)).setNegativeButton("Cancelar", null).show();
        return;
    L34:
        r22 = "Bloquear contenido";
        goto L35
    L29:
        r1 = "kind:Plex";
        goto L30
    L26:
        r0 = "Plex · Toda la sección";
    L38:
        Toast.makeText(r12, "Importa una lista para elegir contenido", 0).show();
    }

    static void configure(final Activity r6) {
        LinearLayout r0 = new LinearLayout(r6);
        r0.setOrientation(1);
        final EditText r1 = field(r6, "Nuevo PIN");
        final EditText r2 = field(r6, "Repetir PIN");
        r0.addView(r1);
        r0.addView(r2);
        final AlertDialog r3 = new AlertDialog.Builder(r6).setTitle("Crear PIN").setView(r0).setPositiveButton("Guardar", null).setNegativeButton("Cancelar", null).create();
        r3.setOnShowListener(new ParentalControl$$ExternalSyntheticLambda14(r3, r1, r2, r6));
        r3.show();
    }

    static boolean enabled(Context r2) {
        return prefs(r2).contains("hash");
    }

    static EditText field(Activity r2, String r3) {
        EditText r0 = new EditText(r2);
        r0.setHint(r3);
        r0.setInputType(18);
        return r0;
    }

    static boolean hasProviderBlock(Set<String> r3) {
        Iterator<String> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().startsWith("provider:") == false) goto L4;
        return true;
    L10:
        return false;
    }

    static String hash(String r4, byte[] r5) throws Exception {
        PBEKeySpec r0 = new PBEKeySpec(r4.toCharArray(), r5, AacUtil.AAC_LC_MAX_RATE_BYTES_PER_SECOND, 256);
        String r1 = Base64.encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(r0).getEncoded(), 2);     // Catch: Throwable -> L6
        r0.clearPassword();
        return r1;
    L6:
        th = move-exception;
        r0.clearPassword();
        throw th;
    }

    static /* synthetic */ void lambda$afterLoaded$2(Activity r1, View r2, Runnable r3) {
        if (r1.isFinishing() == false) goto L5;
        return;
    L5:
        if (r1.isDestroyed() == true) goto L13;
        if (r2 != null) goto L8;
    L10:
        r3.run();
        return;
    L8:
        if (pending.containsKey(r1) == true) goto L10;
        r2.setVisibility(0);
        goto L10
    }

    static /* synthetic */ void lambda$afterLoaded$3(final Activity r1, final View r2, final Runnable r3) {
        prefs(r1.getApplicationContext()).getAll();
        ready = true;
        r1.runOnUiThread(new ParentalControl$$ExternalSyntheticLambda15(r1, r2, r3));
    }

    static /* synthetic */ void lambda$authenticate$5(Activity r1, View r2, boolean r3, DialogInterface r4, int r5) {
        pending.remove(r1);
        r2.setVisibility(0);
        if (r3 == false) goto L6;
        r1.finish();
        return;
    }

    static /* synthetic */ void lambda$authenticate$6(EditText r5, Activity r6, AlertDialog r7, View r8, List r9, View r10) {
        if (SystemClock.elapsedRealtime() >= retryAt) goto L7;
        r5.setError("Espera 30 segundos antes de intentarlo");
        return;
    L7:
        if (verify(r6, r5.getText().toString()) == false) goto L14;
        unlocked = true;
        failures = 0;
        r7.dismiss();
        r8.setVisibility(0);
        pending.remove(r6);
        Iterator r0 = r9.iterator();
    L10:
        if (r0.hasNext() == false) goto L24;
        Runnable r1 = (Runnable) r0.next();
        if (r6.isDestroyed() == true) goto L10;
        r1.run();
        goto L10
    L24:
        return;
    L14:
        r5.setText("");
        r5.setError("PIN incorrecto");
        int r2 = failures + 1;
        failures = r2;
        if (r2 < 5) goto L23;
        retryAt = SystemClock.elapsedRealtime() + 30000;
        failures = 0;
        return;
    }

    static /* synthetic */ void lambda$authenticate$7(final AlertDialog r7, final EditText r8, final Activity r9, final View r10, final List r11, DialogInterface r12) {
        r7.getButton(-1).setOnClickListener(new ParentalControl$$ExternalSyntheticLambda1(r8, r9, r7, r10, r11));
    }

    static /* synthetic */ void lambda$choose$15(List r4, EditText r5, TreeMap r6, ArrayAdapter r7, ListView r8, Set r9) {
        r4.clear();
        String r0 = r5.getText().toString().toLowerCase(Locale.ROOT);
        Iterator r1 = r6.keySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        String r2 = (String) r1.next();
        if (r2.toLowerCase(Locale.ROOT).contains(r0) == false) goto L4;
        r4.add(r2);
        goto L4
    L8:
        r7.notifyDataSetChanged();
        r8.clearChoices();
        int r3 = 0;
    L10:
        if (r3 >= r4.size()) goto L12;
        r8.setItemChecked(r3, r9.contains(r6.get(r4.get(r3))));
        r3 = r3 + 1;
        goto L10
    }

    static /* synthetic */ void lambda$choose$16(TreeMap r2, List r3, ListView r4, Set r5, AdapterView r6, View r7, int r8, long r9) {
        String r0 = (String) r2.get(r3.get(r8));
        if (r4.isItemChecked(r8) == false) goto L5;
        r5.add(r0);
        return;
    L5:
        r5.remove(r0);
    }

    static /* synthetic */ void lambda$choose$17(Activity r2, Set r3, DialogInterface r4, int r5) {
        prefs(r2).edit().putStringSet("blocked", r3).commit();
    }

    static /* synthetic */ void lambda$configure$19(EditText r3, EditText r4, Activity r5, AlertDialog r6, View r7) {
        String r0 = r3.getText().toString();
        if (r0.matches("[0-9]{4,8}") == true) goto L5;
    L12:
        r4.setError("Repite el mismo PIN de 4 a 8 cifras");
        return;
    L5:
        if (r0.equals(r4.getText().toString()) == false) goto L12;
        set(r5, r0);     // Catch: Exception -> L9
        r6.dismiss();     // Catch: Exception -> L9
        Toast.makeText(r5, "PIN guardado. Ahora elige qué bloquear", 0).show();     // Catch: Exception -> L9
        settings(r5);     // Catch: Exception -> L9
        return;
    L10:
        r4.setError("No se pudo guardar el PIN");
    }

    static /* synthetic */ void lambda$configure$20(final AlertDialog r2, final EditText r3, final EditText r4, final Activity r5, DialogInterface r6) {
        r2.getButton(-1).setOnClickListener(new ParentalControl$$ExternalSyntheticLambda12(r3, r4, r5, r2));
    }

    static /* synthetic */ void lambda$removeBlocks$18(Activity r2, List r3, DialogInterface r4, int r5) {
        change(r2, (String) r3.get(r5), false);
        removeBlocks(r2);
    }

    static /* synthetic */ void lambda$require$4(Activity r3, Runnable r4) {
        if (prefs(r3).getBoolean(TtmlNode.COMBINE_ALL, false) == true) goto L6;
        r4.run();
        return;
    L6:
        authenticate(r3, r4, true);
    }

    static /* synthetic */ void lambda$requireEntry$9(Activity r1, Entry r2, Runnable r3) {
        if (restricted(r1, r2) == false) goto L5;
        authenticate(r1, r3, r1 instanceof PlayerActivity);
        return;
    L5:
        r3.run();
    }

    static /* synthetic */ void lambda$requireKey$8(Activity r1, String r2, Runnable r3) {
        if (blocks(r1, r2) == true) goto L5;
        r3.run();
        return;
    L5:
        if ((r1 instanceof PlayerActivity) == false) goto L7;
    L12:
        boolean r0 = true;
    L13:
        authenticate(r1, r3, r0);
        return;
    L7:
        if ((r1 instanceof PlexActivity) == true) goto L12;
        if ((r1 instanceof EpgActivity) == true) goto L12;
        r0 = false;
        goto L13
    }

    static /* synthetic */ void lambda$sections$13(boolean[] r0, DialogInterface r1, int r2, boolean r3) {
        r0[r2] = r3;
    }

    static /* synthetic */ void lambda$sections$14(Activity r3, boolean[] r4, String[] r5, DialogInterface r6, int r7) {
        prefs(r3).edit().putBoolean(TtmlNode.COMBINE_ALL, r4[0]).commit();
        int r0 = 1;
    L4:
        if (r0 >= r5.length) goto L6;
        change(r3, r5[r0], r4[r0]);
        r0 = r0 + 1;
        goto L4
    }

    static /* synthetic */ void lambda$settings$10(Activity r1, DialogInterface r2, int r3) {
        prefs(r1).edit().clear().commit();
        unlocked = false;
    }

    static /* synthetic */ void lambda$settings$11(final Activity r3, DialogInterface r4, int r5) {
        if (enabled(r3) == false) goto L22;
        if (r5 == 0) goto L22;
        if (r5 != 1) goto L10;
        sections(r3);
        return;
    L10:
        if (r5 != 2) goto L13;
        choose(r3, true);
        return;
    L13:
        if (r5 != 3) goto L16;
        choose(r3, false);
        return;
    L16:
        if (r5 != 4) goto L19;
        removeBlocks(r3);
        return;
    L19:
        if (r5 != 5) goto L21;
        unlocked = false;
        Toast.makeText(r3, "Se pedirá el PIN al abrir contenido bloqueado", 0).show();
        return;
    L21:
        new AlertDialog.Builder(r3).setTitle("Desactivar control parental?").setPositiveButton("Desactivar", new ParentalControl$$ExternalSyntheticLambda7(r3)).setNegativeButton("Cancelar", null).show();
        return;
    L22:
        configure(r3);
    }

    static /* synthetic */ void lambda$settings$12(final Activity r4) {
        if (enabled(r4) == false) goto L5;
        String[] r0 = {"Cambiar mi PIN", "Elegir secciones", "Elegir proveedores", "Elegir canales, películas o series", "Ver y quitar bloqueos", "Bloquear sesión ahora", "Desactivar control parental"};
    L6:
        new AlertDialog.Builder(r4).setTitle("Control parental · Tú eliges qué bloquear").setItems(r0, new ParentalControl$$ExternalSyntheticLambda6(r4)).setNegativeButton("Cerrar", null).show();
        return;
    L5:
        r0 = new String[]{"Crear mi PIN"};
        goto L6
    }

    static /* synthetic */ Thread lambda$static$0(Runnable r2) {
        return new Thread(r2, "CHDTVParental");
    }

    static /* synthetic */ void lambda$warm$1(Context r1) {
        prefs(r1).getAll();
        ready = true;
    }

    static SharedPreferences prefs(Context r2) {
        return r2.getSharedPreferences("parental", 0);
    }

    static String provider(Context r7, Entry r8) {
        if ("Plex".equals(r8.group) == false) goto L40;
        return "Plex";
    L40:
        String r0 = WatchHistory.m418id(r8);     // Catch: Exception -> L37
        Catalog r1 = AppStorage.cachedCatalog();     // Catch: Exception -> L37
        if (r1 != null) goto L8;
        r1 = new AppStorage(r7).catalog();     // Catch: Exception -> L37
    L8:
        Iterator<Catalog.Source> r9 = r1.sources.iterator();     // Catch: Exception -> L37
    L10:
        if (r9.hasNext() == false) goto L39;
        Catalog.Source r2 = r9.next();     // Catch: Exception -> L37
        if ((r2.entries instanceof DemandCatalog.Rows) == true) goto L15;
        if ((r2.entries instanceof SectionStore.DiskEntries) == false) goto L26;
        SectionStore.DiskEntries r3 = (SectionStore.DiskEntries) r2.entries;     // Catch: Exception -> L37
        String r4 = r8.kind;     // Catch: Exception -> L37
        if ("Series".equals(r8.kind) == false) goto L23;
        String r5 = CatalogCounts.series(r8);     // Catch: Exception -> L37
    L24:
        List<Entry> r6 = r3.select(r4, null, r5, null);     // Catch: Exception -> L37
    L29:
        Iterator<Entry> r10 = r6.iterator();     // Catch: Exception -> L37
    L31:
        if (r10.hasNext() == false) goto L10;
        if (WatchHistory.m418id(r10.next()).equals(r0) == false) goto L31;
        return Providers.name(r8, r2.name);
    L23:
        r5 = null;
        goto L24
    L26:
        if ((r2.entries instanceof CatalogCache.LazyEntries) == false) goto L28;
        r6 = ((CatalogCache.LazyEntries) r2.entries).select(r8.kind, null);     // Catch: Exception -> L37
        goto L29
    L28:
        r6 = r2.entries;     // Catch: Exception -> L37
        goto L29
    L15:
        if (((DemandCatalog.Rows) r2.entries).find(r8, null) == null) goto L10;
        return Providers.name(r8, r2.name);
    L39:
        return Providers.name(r8, "");
    }

    static void removeBlocks(final Activity r6) {
        final ArrayList r0 = new ArrayList(blocked(r6));
        Collections.sort(r0);
        if (r0.isEmpty() == true) goto L5;
        String[] r1 = new String[r0.size()];
        int r2 = 0;
    L12:
        if (r2 >= r0.size()) goto L14;
        r1[r2] = ((String) r0.get(r2)).replace("kind:", "Sección · ").replace("provider:", "Proveedor · ").replace("entry:", "Título · ").replace("series:", "Serie · ").replace('\n', ' ');
        r2 = r2 + 1;
        goto L12
    L14:
        new AlertDialog.Builder(r6).setTitle("Pulsa un bloqueo para quitarlo").setItems(r1, new ParentalControl$$ExternalSyntheticLambda17(r6, r0)).setNegativeButton("Cerrar", null).show();
        return;
    L5:
        if (prefs(r6).getBoolean(TtmlNode.COMBINE_ALL, false) == false) goto L7;
        String r3 = "Está bloqueada toda la app; cámbialo en Elegir secciones";
    L8:
        Toast.makeText(r6, r3, 0).show();
        return;
    L7:
        r3 = "No hay contenido bloqueado";
        goto L8
    }

    static void require(final Activity r1, final Runnable r2) {
        afterLoaded(r1, new ParentalControl$$ExternalSyntheticLambda9(r1, r2));
    }

    static void requireEntry(final Activity r1, final Entry r2, final Runnable r3) {
        afterLoaded(r1, new ParentalControl$$ExternalSyntheticLambda8(r1, r2, r3));
    }

    static void requireKey(final Activity r1, final String r2, final Runnable r3) {
        afterLoaded(r1, new ParentalControl$$ExternalSyntheticLambda16(r1, r2, r3));
    }

    static boolean restricted(Context r4, Entry r5) {
        if (enabled(r4) == true) goto L5;
        return false;
    L5:
        Set<String> r0 = blocked(r4);
        if (prefs(r4).getBoolean(TtmlNode.COMBINE_ALL, false) == false) goto L8;
    L23:
        return true;
    L8:
        if (r0.contains("kind:" + r5.kind) == true) goto L23;
        if (hasProviderBlock(r0) == false) goto L14;
        if (r0.contains("provider:" + provider(r4, r5)) == true) goto L23;
    L14:
        if (r0.contains("entry:" + WatchHistory.m418id(r5)) == true) goto L23;
        if (r5.kind.equals("Series") == false) goto L20;
        if (r0.contains(seriesKey(r5)) == true) goto L23;
    L20:
        if ("Plex".equals(r5.group) == true) goto L22;
        return false;
    L22:
        if (r0.contains("kind:Plex") == true) goto L23;
        return false;
    }

    static void sections(final Activity r7) {
        String[] r0 = {"Toda la app", "TV", "Películas", "Series", "Plex"};
        final String[] r1 = {TtmlNode.COMBINE_ALL, "kind:Canales", "kind:Películas", "kind:Series", "kind:Plex"};
        final boolean[] r2 = new boolean[r1.length];
        int r4 = 0;
    L4:
        if (r4 >= r1.length) goto L9;
        if (r4 != 0) goto L7;
        boolean r5 = prefs(r7).getBoolean(TtmlNode.COMBINE_ALL, false);
    L8:
        r2[r4] = r5;
        r4 = r4 + 1;
        goto L4
    L7:
        r5 = blocked(r7).contains(r1[r4]);
        goto L8
    L9:
        new AlertDialog.Builder(r7).setTitle("Qué secciones bloquear").setMultiChoiceItems(r0, r2, new ParentalControl$$ExternalSyntheticLambda4(r2)).setPositiveButton("Guardar", new ParentalControl$$ExternalSyntheticLambda5(r7, r2, r1)).setNegativeButton("Cancelar", null).show();
    }

    static String seriesKey(Entry r2) {
        StringBuilder r0 = new StringBuilder().append("series:").append(r2.group).append("\n");
        if (r2.series.isEmpty() == false) goto L5;
        String r1 = r2.group;
    L7:
        return r0.append(r1).toString();
    L5:
        r1 = r2.series;
        goto L7
    }

    static void set(Context r4, String r5) throws Exception {
        byte[] r0 = new byte[16];
        new SecureRandom().nextBytes(r0);
        if (prefs(r4).edit().putString("salt", Base64.encodeToString(r0, 2)).putString("hash", hash(r5, r0)).commit() == false) goto L7;
        unlocked = true;
        return;
    L7:
        throw new IOException("No se pudo guardar el PIN");
    }

    static void settings(final Activity r1) {
        authenticate(r1, new ParentalControl$$ExternalSyntheticLambda3(r1));
    }

    static boolean verify(Context r6, String r7) {
        return MessageDigest.isEqual(hash(r7, Base64.decode(prefs(r6).getString("salt", ""), 0)).getBytes(StandardCharsets.UTF_8), prefs(r6).getString("hash", "").getBytes(StandardCharsets.UTF_8));
    L6:
        return false;
    }

    static void warm(Context r3) {
        final Context r0 = r3.getApplicationContext();
        loader.execute(new ParentalControl$$ExternalSyntheticLambda18(r0));
    }
}
