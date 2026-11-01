package es.antonio.m3u;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes7.dex */
final class DemandTokenSources {
    static final Map<DemandCatalog.Directory, Map<Long, Boolean>> hot = null;
    static long scanned;

    static {
        hot = new WeakHashMap();
    }

    DemandTokenSources() {
    }

    static Boolean cached(DemandCatalog.Rows r4) {
        Map<DemandCatalog.Directory, Map<Long, Boolean>> r0 = hot;
        monitor-enter(r0);
        Map<Long, Boolean> r1 = hot.get(r4.directory);     // Catch: Throwable -> L10
        if (r1 != null) goto L7;
        Boolean r5 = null;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r5;
    L7:
        r5 = r1.get(Long.valueOf(r4.records));     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        throw th;
    }

    static boolean dash(String r3, String r4) {
        if (r3 != null) goto L4;
        String r5 = "";
    L6:
        if (r5.equals("mpd") == false) goto L8;
    L33:
        return true;
    L8:
        if (r5.equals("dash") == true) goto L33;
        if (r5.isEmpty() == false) goto L13;
    L34:
        String r6 = new URI(r4).getPath();     // Catch: Exception -> L31
        if (r6 == null) goto L30;
        if (r6.toLowerCase(Locale.ROOT).endsWith(".mpd") == false) goto L30;
        return true;
    L30:
        return false;
    L32:
        return false;
    L13:
        if (r5.equals("hls") == false) goto L15;
    L24:
        return false;
    L15:
        if (r5.equals("m3u8") == true) goto L24;
        if (r5.equals("ism") == true) goto L24;
        if (r5.equals("isml") == true) goto L24;
        if (r5.equals("ss") == true) goto L24;
        if (r5.equals("smoothstreaming") == false) goto L34;
    L4:
        r5 = r3.trim().toLowerCase(Locale.ROOT);
        goto L6
    }

    static boolean eligible(Catalog.Source r2) {
        boolean r0 = r2.entries instanceof DemandCatalog.Rows;
        List<Entry> r3 = r2.entries;
        if (r0 == true) goto L5;
        Iterator<Entry> r4 = r3.iterator();
    L8:
        if (r4.hasNext() == false) goto L9;
        Entry r1 = Entry.details(r4.next());
        if (MovistarStreams.allowedHost(r1.url) == false) goto L8;
        if (dash(r1.manifest, r1.url) == false) goto L8;
        return true;
    L9:
        return false;
    L5:
        return load((DemandCatalog.Rows) r3, new DemandTokenSources$$ExternalSyntheticLambda1());
    }

    static String label(List<Entry> r0) {
        return "DASH";
    }

    static /* synthetic */ boolean lambda$0(String r0) {
        return MovistarStreams.allowedHost(r0);
    }

    static /* synthetic */ void lambda$1(boolean r1, OutputStream r2) throws IOException {
        DataOutputStream r0 = new DataOutputStream(r2);
        r0.writeInt(1128813587);
        r0.writeBoolean(r1);
    }

    static boolean load(DemandCatalog.Rows r8, Predicate<String> r9) {
        Boolean r0 = cached(r8);
        if (r0 != null) goto L5;
        DemandCatalog.Directory r1 = r8.directory;
        String r2 = String.valueOf(r1.file.getName()) + ":" + r8.records + ":" + r8.count + ":" + r1.owner.modified + ":" + r1.owner.size + ":" + r1.file.length() + ":" + r1.file.lastModified();
        File r3 = new File(r1.owner.files, "source-token-v213");
        File r4 = new File(r3, String.valueOf(DemandDiskCache.hash(r2)) + ".json");
        Throwable r5 = null;
        DataInputStream r6 = new DataInputStream(new FileInputStream(r4));     // Catch: Throwable -> L22
    L19:
        th = move-exception;
        r6.close();     // Catch: Throwable -> L22
        throw th;     // Catch: Throwable -> L22
    L9:
        if (r6.readInt() != 1128813587) goto L43;
        boolean r7 = r6.readBoolean();     // Catch: Throwable -> L19
        if (r6.read() != (-1)) goto L16;
        remember(r8, r7);     // Catch: Throwable -> L19
        r6.close();     // Catch: Throwable -> L22
        return r7;
    L16:
        throw new IOException("Token source cache trailing data");     // Catch: Throwable -> L19
    L43:
        throw new IOException("Token source cache");     // Catch: Throwable -> L19
    L22:
        th = move-exception;
        if (0 == 0) goto L27;
        if (null == th) goto L44;
        r5.addSuppressed(th);     // Catch: IOException -> L28
        throw null;     // Catch: IOException -> L28
    L41:
        final boolean r10 = scan(r8, r9);     // Catch: Throwable -> L32 IOException -> L34
        remember(r8, r10);
        DemandDiskCache.put(r3, r4, new DemandTokenSources$$ExternalSyntheticLambda0(r10), PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE, 2000);
        return r10;
    L32:
        e = move-exception;
        BlackBox.event("TOKEN_SOURCE_SCAN_ERROR", e.getClass().getSimpleName());
        return false;
    L44:
        throw null;     // Catch: IOException -> L28
    L27:
        throw th;     // Catch: IOException -> L28
        goto L41
    L5:
        return r0.booleanValue();
    }

    static void remember(DemandCatalog.Rows r4, boolean r5) {
        Map<DemandCatalog.Directory, Map<Long, Boolean>> r0 = hot;
        monitor-enter(r0);
        Map<Long, Boolean> r1 = hot.get(r4.directory);     // Catch: Throwable -> L10
        if (r1 != null) goto L7;
        r1 = new HashMap();     // Catch: Throwable -> L10
        hot.put(r4.directory, r1);     // Catch: Throwable -> L10
    L7:
        r1.put(Long.valueOf(r4.records), Boolean.valueOf(r5));     // Catch: Throwable -> L10
        monitor-exit(r0);     // Catch: Throwable -> L10
        return;
    L10:
        th = move-exception;
        throw th;
    }

    static boolean scan(DemandCatalog.Rows r16, Predicate<String> r17) throws IOException {
        DemandCatalog.Directory r1 = r16.directory;
        long r2 = scanned;
        Throwable r4 = null;
        DemandFile r5 = new DemandFile(r1.file, "r");     // Catch: Throwable -> L24
        boolean r6 = false;
        int r7 = 0;
    L30:
        if (r7 >= r16.count) goto L16;
        int r8 = Math.min(1024, r16.count - r7);     // Catch: Throwable -> L20
        byte[] r9 = new byte[r8 * 72];     // Catch: Throwable -> L20
        r5.seek(r16.records + (((long) r7) * 72));     // Catch: Throwable -> L20
        r5.readFully(r9);     // Catch: Throwable -> L20
        IntBuffer r10 = ByteBuffer.wrap(r9).asIntBuffer();     // Catch: Throwable -> L20
        int r11 = 0;
    L9:
        if (r11 >= r8) goto L10;
        scanned++;     // Catch: Throwable -> L20
        int r12 = r11 * 18;     // Catch: Throwable -> L20
        String r13 = r1.value(r5, r10.get(r12 + 3));     // Catch: Throwable -> L20
        if (r17.test(r13) == false) goto L19;
        if (dash(r1.value(r5, r10.get(r12 + 6)), r13) == false) goto L19;
        r6 = true;
    L19:
        r11 = r11 + 1;
        goto L9
    L10:
        r7 = r7 + 1024;     // Catch: Throwable -> L20
    L16:
        r5.close();     // Catch: Throwable -> L24
        BlackBox.event("TOKEN_SOURCE_METADATA", "rows=" + (scanned - r2) + " entries_created=0 eligible_dash=" + r6);
        return r6;
    L20:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L24
        throw th;     // Catch: Throwable -> L24
    L24:
        th = move-exception;
        if (0 == 0) goto L29;
        if (null == th) goto L38;
        r4.addSuppressed(th);
        throw null;
    L38:
        throw null;
    L29:
        throw th;
    }
}
