package es.antonio.m3u;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes3.dex */
public final class EpgData implements Serializable {
    private static final Pattern RX0 = null;
    private static final Pattern RX1 = null;
    private static final Pattern RX2 = null;
    private static final Pattern RX3 = null;
    private static final long serialVersionUID = 1;
    private transient Map<String, String> byBase;
    private transient Map<String, String> byName;
    public long loadedAt;
    public final Map<String, List<String>> names;
    public final Map<String, List<Programme>> programmes;

    public static final class Programme implements Serializable {
        private static final long serialVersionUID = 1;
        public String channel;
        public String description;
        public long start;
        public long stop;
        public String title;

        public Programme() {
            this.channel = "";
            this.title = "";
            this.description = "";
        }
    }

    static {
        RX0 = Pattern.compile("(?i)\\.(tv|es)$");
        RX1 = Pattern.compile("(?i)(?:[ \t._-]+(?:sd|hd|fhd|uhd|4k|720p?|1080p?))+$");
        RX2 = Pattern.compile("\\p{M}");
        RX3 = Pattern.compile("[^\\p{L}\\p{N}]");
    }

    public EpgData() {
        this.programmes = new HashMap();
        this.names = new HashMap();
    }

    public static String baseName(String r0) {
        return EpgNameCache.base(r0);
    }

    public static String epg217BaseNameOriginal(String r4) {
        Pattern r0 = RX2;
        if (r4 != null) goto L5;
        String r2 = "";
    L6:
        String r1 = r0.matcher(Normalizer.normalize(r2, Normalizer.Form.NFD)).replaceAll("").toLowerCase(Locale.ROOT).trim();
        String r3 = RX0.matcher(r1).replaceAll("");
        String r5 = normalize(RX1.matcher(r3).replaceAll(""));
        if (r5.equals("24h") == false) goto L11;
        return "24horas";
    L11:
        if (r5.equals("7rm") == false) goto L15;
        return "la7murcia";
    L15:
        switch(r5.hashCode()) {
            case -1907638519: goto L48;
            case -1514294904: goto L45;
            case -1300116705: goto L42;
            case -781484199: goto L39;
            case -290498216: goto L36;
            case 101224: goto L33;
            case 103994: goto L30;
            case 108463: goto L27;
            case 114688: goto L24;
            case 1066390210: goto L21;
            case 1822038863: goto L18;
            default: goto L50;
        };
    L50:
        byte r6 = -1;
    L51:
        switch(r6) {
            case 0: goto L73;
            case 1: goto L71;
            case 2: goto L69;
            case 3: goto L67;
            case 4: goto L65;
            case 5: goto L63;
            case 6: goto L61;
            case 7: goto L59;
            case 8: goto L57;
            case 9: goto L55;
            case 10: goto L53;
            default: goto L52;
        };
    L52:
        return r5;
    L53:
        return "iberaliatv";
    L55:
        return "eltorotv";
    L57:
        return "ib3tvillesbalears";
    L59:
        return "laochomediterraneo";
    L61:
        return "squirrel2";
    L63:
        return "squirreltv";
    L65:
        return "mtvespana";
    L67:
        return "nickelodeon";
    L69:
        return "historia";
    L71:
        return "teledeporte";
    L73:
        return "factoriadeficcion";
    L18:
        if (r5.equals("squirreldos") == false) goto L50;
        r6 = 6;
        goto L51
    L21:
        if (r5.equals("nicknickelodeon") == false) goto L50;
        r6 = 3;
        goto L51
    L24:
        if (r5.equals("tdp") == false) goto L50;
        r6 = 1;
        goto L51
    L27:
        if (r5.equals("mtv") == false) goto L50;
        r6 = 4;
        goto L51
    L30:
        if (r5.equals("ib3") == false) goto L50;
        r6 = 8;
        goto L51
    L33:
        if (r5.equals("fdf") == false) goto L50;
        r6 = 0;
        goto L51
    L36:
        if (r5.equals("canalhistoria") == false) goto L50;
        r6 = 2;
        goto L51
    L39:
        if (r5.equals("squirrel") == false) goto L50;
        r6 = 5;
        goto L51
    L42:
        if (r5.equals("eltoro") == false) goto L50;
        r6 = 9;
        goto L51
    L45:
        if (r5.equals("la8mediterraneo") == false) goto L50;
        r6 = 7;
        goto L51
    L48:
        if (r5.equals("iberalia") == false) goto L50;
        r6 = 10;
        goto L51
    L5:
        r2 = r4;
        goto L6
    }

    public static String epg217NormalizeOriginal(String r5) {
        Pattern r0 = RX3;
        Pattern r1 = RX2;
        if (r5 != null) goto L5;
        String r3 = "";
    L7:
        return r0.matcher(r1.matcher(Normalizer.normalize(r3, Normalizer.Form.NFD)).replaceAll("").toLowerCase(Locale.ROOT)).replaceAll("");
    L5:
        r3 = r5;
        goto L7
    }

    static /* synthetic */ int lambda$index$0(Programme r4, Programme r5) {
        return Long.compare(r4.start, r5.start);
    }

    public static Set<String> matchKeys(String r2) {
        HashSet r0 = new HashSet();
        r0.add(normalize(r2));
        r0.add(baseName(r2));
        r0.remove("");
        return r0;
    }

    public static String normalize(String r0) {
        return EpgNameCache.normalize(r0);
    }

    private static void putUnique(Map<String, String> r2, Set<String> r3, String r4, String r5) {
        if (r4.isEmpty() == false) goto L5;
        return;
    L5:
        String r0 = r2.get(r4);
        if (r0 != null) goto L8;
    L10:
        r2.put(r4, r5);
        return;
    L8:
        if (r0.equals(r5) == true) goto L10;
        r3.add(r4);
    }

    public String channel(Entry r5) {
        if (r5.tvgId == null) goto L11;
        if (r5.tvgId.isEmpty() == true) goto L11;
        if (this.programmes.containsKey(r5.tvgId) == false) goto L11;
        return r5.tvgId;
    L11:
        if (this.byName != null) goto L13;
    L14:
        index();
    L15:
        Iterator r0 = Arrays.asList(new String[]{r5.tvgId, r5.tvgName, r5.title}).iterator();
    L17:
        if (r0.hasNext() == false) goto L22;
        String r1 = (String) r0.next();
        String r2 = this.byName.get(normalize(r1));
        if (r2 == null) goto L17;
        return r2;
    L22:
        Iterator r3 = Arrays.asList(new String[]{r5.tvgId, r5.tvgName, r5.title}).iterator();
    L24:
        if (r3.hasNext() == false) goto L29;
        String r4 = (String) r3.next();
        String r6 = this.byBase.get(baseName(r4));
        if (r6 == null) goto L24;
        return r6;
    L29:
        return null;
    L13:
        if (this.byBase != null) goto L15;
        goto L14
    }

    public int count() {
        int r0 = 0;
        Iterator<List<Programme>> r1 = this.programmes.values().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = r0 + r1.next().size();
        goto L4
    L6:
        return r0;
    }

    public synchronized void ensureIndexed() {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.byName != null) goto L6;
    L7:
        index();     // Catch: Throwable -> L10
    L8:
        monitor-exit(this);
        return;
    L6:
        if (this.byBase != null) goto L8;
        goto L7
    }

    public synchronized void epg217CopyNames(EpgData r11) {
        monitor-enter(this);
    L26:
        th = move-exception;
        throw th;
    L4:
        if (this.byName != null) goto L6;
    L7:
        HashMap r0 = new HashMap();     // Catch: Throwable -> L26
        HashMap r1 = new HashMap();     // Catch: Throwable -> L26
        HashSet r2 = new HashSet();     // Catch: Throwable -> L26
        HashSet r3 = new HashSet();     // Catch: Throwable -> L26
        Iterator<Map.Entry<String, List<String>>> r4 = this.names.entrySet().iterator();     // Catch: Throwable -> L26
    L9:
        if (r4.hasNext() == false) goto L14;
        Map.Entry<String, List<String>> r5 = r4.next();     // Catch: Throwable -> L26
        Iterator<String> r6 = r5.getValue().iterator();     // Catch: Throwable -> L26
    L12:
        if (r6.hasNext() == false) goto L9;
        String r7 = r6.next();     // Catch: Throwable -> L26
        putUnique(r0, r2, normalize(r7), r5.getKey());     // Catch: Throwable -> L26
        putUnique(r1, r3, baseName(r7), r5.getKey());     // Catch: Throwable -> L26
        goto L12
    L14:
        Iterator r8 = r2.iterator();     // Catch: Throwable -> L26
    L16:
        if (r8.hasNext() == false) goto L18;
        r0.remove((String) r8.next());     // Catch: Throwable -> L26
        goto L16
    L18:
        Iterator r9 = r3.iterator();     // Catch: Throwable -> L26
    L20:
        if (r9.hasNext() == false) goto L22;
        r1.remove((String) r9.next());     // Catch: Throwable -> L26
        goto L20
    L22:
        this.byName = r0;     // Catch: Throwable -> L26
        this.byBase = r1;     // Catch: Throwable -> L26
    L23:
        r11.byName = this.byName;     // Catch: Throwable -> L26
        r11.byBase = this.byBase;     // Catch: Throwable -> L26
        monitor-exit(this);
        return;
    L6:
        if (this.byBase != null) goto L23;
        goto L7
    }

    public synchronized void epg217ResetNames() {
        monitor-enter(this);
        this.byName = null;     // Catch: Throwable -> L7
        this.byBase = null;     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized void index() {
        monitor-enter(this);
        this.byName = new HashMap();     // Catch: Throwable -> L36
        this.byBase = new HashMap();     // Catch: Throwable -> L36
        HashSet r0 = new HashSet();     // Catch: Throwable -> L36
        HashSet r1 = new HashSet();     // Catch: Throwable -> L36
        Iterator<Map.Entry<String, List<String>>> r2 = this.names.entrySet().iterator();     // Catch: Throwable -> L36
    L5:
        if (r2.hasNext() == false) goto L11;
        Map.Entry<String, List<String>> r3 = r2.next();     // Catch: Throwable -> L36
        Iterator<String> r4 = r3.getValue().iterator();     // Catch: Throwable -> L36
    L8:
        if (r4.hasNext() == false) goto L5;
        String r5 = r4.next();     // Catch: Throwable -> L36
        putUnique(this.byName, r0, normalize(r5), r3.getKey());     // Catch: Throwable -> L36
        putUnique(this.byBase, r1, baseName(r5), r3.getKey());     // Catch: Throwable -> L36
        goto L8
    L11:
        Iterator r6 = r0.iterator();     // Catch: Throwable -> L36
    L13:
        if (r6.hasNext() == false) goto L15;
        String r7 = (String) r6.next();     // Catch: Throwable -> L36
        this.byName.remove(r7);     // Catch: Throwable -> L36
        goto L13
    L15:
        Iterator r8 = r1.iterator();     // Catch: Throwable -> L36
    L17:
        if (r8.hasNext() == false) goto L19;
        String r9 = (String) r8.next();     // Catch: Throwable -> L36
        this.byBase.remove(r9);     // Catch: Throwable -> L36
        goto L17
    L19:
        Iterator<List<Programme>> r10 = this.programmes.values().iterator();     // Catch: Throwable -> L36
    L20:
        if (r10.hasNext() == false) goto L34;
        List<Programme> r11 = r10.next();     // Catch: Throwable -> L36
        Collections.sort(r11, new EpgData$$ExternalSyntheticLambda0());     // Catch: Throwable -> L36
        int r12 = 0;
    L24:
        if (r12 >= r11.size()) goto L20;
        Programme r13 = r11.get(r12);     // Catch: Throwable -> L36
        if (r13.stop > r13.start) goto L32;
        if ((r12 + 1) >= r11.size()) goto L30;
        long r14 = r11.get(r12 + 1).start;     // Catch: Throwable -> L36
    L31:
        r13.stop = r14;     // Catch: Throwable -> L36
        goto L32
    L30:
        r14 = r13.start + 7200000;     // Catch: Throwable -> L36
    L32:
        r12 = r12 + 1;
        goto L24
    L34:
        monitor-exit(this);
        return;
    L36:
        th = move-exception;
        throw th;
    }

    public Programme[] nowNext(Entry r8, long r9) {
        List<Programme> r0 = schedule(r8);
        int r1 = 0;
        int r2 = r0.size();
    L4:
        if (r1 >= r2) goto L9;
        int r4 = (r1 + r2) / 2;
        if (r0.get(r4).start <= r9) goto L7;
        r2 = r4;
        goto L4
    L7:
        r1 = r4 + 1;
        goto L4
    L9:
        Programme r5 = null;
        if (r1 > 0) goto L12;
    L14:
        Programme r6 = null;
    L16:
        if (r1 >= r0.size()) goto L19;
        r5 = r0.get(r1);
    L19:
        return new Programme[]{r6, r5};
    L12:
        if (r0.get(r1 - 1).stop <= r9) goto L14;
        r6 = r0.get(r1 - 1);
        goto L16
    }

    public List<Programme> schedule(Entry r4) {
        String r0 = channel(r4);
        if (r0 != null) goto L5;
        List<Programme> r1 = null;
    L6:
        if (r1 != null) goto L9;
        return Collections.emptyList();
    L9:
        return r1;
    L5:
        r1 = this.programmes.get(r0);
        goto L6
    }
}
