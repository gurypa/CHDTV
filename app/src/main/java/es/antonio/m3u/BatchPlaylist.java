package es.antonio.m3u;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* JADX INFO: loaded from: classes7.dex */
final class BatchPlaylist {
    static final long MAX_BYTES = Long.MAX_VALUE;

    static final class Document {
        final List<String> errors;
        final List<Row> rows;

        Document() {
            this.rows = new ArrayList();
            this.errors = new ArrayList();
        }
    }

    interface Loader {
        List<Entry> load(Row r1) throws Exception;
    }

    interface Progress {
        void loading(Row r1, int r2, int r3);
    }

    static final class Result {
        Catalog catalog;
        int entries;
        final List<String> errors;
        int imported;

        Result(Catalog r1, List<String> r2) {
            this.catalog = r1;
            this.errors = new ArrayList(r2);
        }
    }

    static final class Row {
        final String kind;
        final int line;
        final String provider;
        final String url;

        Row(int r1, String r2, String r3, String r4) {
            this.line = r1;
            this.kind = r2;
            this.provider = r3;
            this.url = r4;
        }
    }

    interface Sink {
        Catalog save(Catalog r1) throws Exception;
    }

    private BatchPlaylist() {
    }

    static Result importLists(Document r1, Catalog r2, Loader r3, Progress r4) {
        return importLists(r1, r2, r3, r4, null);
    }

    static Result importLists(Document r16, Catalog r17, Loader r18, Progress r19, Sink r20) {
        Document r1 = r16;
        Result r3 = new Result(r17, r1.errors);
        Iterator<Row> r4 = r1.rows.iterator();
        int r0 = 0;
    L4:
        if (r4.hasNext() == false) goto L5;
        Row r5 = r4.next();
        int r6 = r0 + 1;
        r19.loading(r5, r6, r1.rows.size());
        List<Entry> r2 = r18.load(r5);     // Catch: Exception -> L27
        if (r2.isEmpty() == true) goto L26;
        Iterator<Entry> r9 = r2.iterator();     // Catch: Exception -> L27
    L11:
        if (r9.hasNext() == false) goto L12;
        r9.next().kind = r5.kind;     // Catch: Exception -> L27
        goto L11
    L12:
        Catalog.Source r15 = r3.catalog.findOrigin(r5.url, r5.kind);     // Catch: Exception -> L27
        if (r15 != null) goto L15;
        String r10 = UUID.randomUUID().toString();     // Catch: Exception -> L27
    L16:
        Catalog.Source r14 = new Catalog.Source(r10, r5.provider, r5.url, r5.kind, r2);     // Catch: Exception -> L27
        r14.provider = r5.provider;     // Catch: Exception -> L27
        if (r15 != null) goto L19;
        String r11 = null;
    L20:
        r14.originalFilename = r11;     // Catch: Exception -> L27
        Catalog r7 = r3.catalog.replace(r14);     // Catch: Exception -> L27
        if (r20 == null) goto L23;
        r7 = r20.save(r7);     // Catch: Exception -> L27
    L23:
        r3.catalog = r7;     // Catch: Exception -> L27
        r3.imported++;     // Catch: Exception -> L27
        r3.entries += r2.size();     // Catch: Exception -> L27
    L29:
        r1 = r16;
        r0 = r6;
        goto L4
    L19:
        r11 = r15.originalFilename;     // Catch: Exception -> L27
        goto L20
    L15:
        r10 = r15.f177id;     // Catch: Exception -> L27
        goto L16
    L26:
        throw new IOException("Lista sin contenido válido");     // Catch: Exception -> L27
    L27:
        e = move-exception;
        BlackBox.event("TXT_LIST_FAILED", "line=" + r5.line + " kind=" + r5.kind + " type=" + e.getClass().getSimpleName());
        r3.errors.add("Línea " + r5.line + " · " + r5.kind + " · " + r5.provider + ": no se pudo importar la lista");
        goto L29
    L5:
        return r3;
    }

    static Document parse(Reader r11) throws IOException {
        Document r0 = new Document();
        BufferedReader r1 = new BufferedReader(r11);
        HashSet r12 = new HashSet();
        int r3 = 0;
    L3:
        String r4 = r1.readLine();
        if (r4 == null) goto L5;
        r3 = r3 + 1;
        if (r3 == 1) goto L9;
    L11:
        String r5 = r4.trim();
        if (r5.isEmpty() == true) goto L3;
        if (r5.startsWith("#") == true) goto L3;
        String[] r6 = r5.split("\\|", 3);
        if (r6.length != 3) goto L18;
        String r7 = section(r6[0]);
        String r8 = r6[1].trim();
        String r9 = r6[2].trim();
        if (r7 == null) goto L38;
        if (r8.isEmpty() == true) goto L38;
        URI r10 = new URI(r9);     // Catch: Exception -> L36
        if (r10.getHost() == null) goto L35;
        if ("http".equalsIgnoreCase(r10.getScheme()) == true) goto L31;
        if ("https".equalsIgnoreCase(r10.getScheme()) == false) goto L35;
    L31:
        if (r12.add(String.valueOf(r7) + "\n" + r9) == false) goto L32;
        r0.rows.add(new Row(r3, r7, r8, r9));
        goto L3
    L32:
        r0.errors.add("Línea " + r3 + ": URL repetida en la misma sección");
    L35:
        throw new IllegalArgumentException();     // Catch: Exception -> L36
    L37:
        r0.errors.add("Línea " + r3 + ": URL HTTP/HTTPS no válida");
    L38:
        r0.errors.add("Línea " + r3 + ": sección o proveedor no válidos");
        goto L3
    L18:
        r0.errors.add("Línea " + r3 + ": usa Sección | Proveedor | URL");
        goto L3
    L9:
        if (r4.startsWith("\ufeff") == false) goto L11;
        r4 = r4.substring(1);
        goto L11
    L5:
        return r0;
    }

    private static String section(String r2) {
        String r3 = Normalizer.normalize(r2, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toLowerCase(Locale.ROOT);
        switch(r3.hashCode()) {
            case -905838985: goto L19;
            case -474020614: goto L14;
            case 3714: goto L9;
            case 550162921: goto L6;
            default: goto L23;
        };
    L23:
        return null;
    L6:
        if (r3.equals("canales") == false) goto L26;
        return "Canales";
    L26:
        return null;
    L9:
        if (r3.equals("tv") == false) goto L27;
        return "Canales";
    L27:
        return null;
    L14:
        if (r3.equals("peliculas") == false) goto L28;
        return "Películas";
    L28:
        return null;
    L19:
        if (r3.equals("series") == false) goto L29;
        return "Series";
    L29:
        return null;
    }
}
