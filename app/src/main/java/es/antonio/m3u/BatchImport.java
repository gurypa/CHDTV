package es.antonio.m3u;

import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/* JADX INFO: loaded from: classes7.dex */
final class BatchImport implements Runnable {
    final MainActivity activity;
    final Uri file;
    final Catalog snapshot;

    BatchImport(MainActivity r1, Uri r2, Catalog r3) {
        this.activity = r1;
        this.file = r2;
        this.snapshot = r3;
    }

    static /* synthetic */ Catalog lambda$2(AppStorage r2, Catalog r3) throws Exception {
        StringBuilder r0 = new StringBuilder("entries=");
        if (r3.changedSource != null) goto L5;
        int r1 = 0;
    L6:
        BlackBox.event("TXT_LIST_SAVE_START", r0.append(r1).toString());
        r2.write("catalog.dat", r3);
        Catalog r4 = r2.catalog();
        BlackBox.event("TXT_LIST_SAVED", "catalog_entries=" + r4.size());
        return r4;
    L5:
        r1 = r3.changedSource.entries.size();
        goto L6
    }

    /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-BatchImport, reason: not valid java name */
    /* synthetic */ List m521lambda$0$esantoniom3uBatchImport(BatchPlaylist.Row r8) throws Exception {
        BlackBox.event("TXT_LIST_LOAD_START", "line=" + r8.line + " kind=" + r8.kind + " heap_used=" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        Throwable r0 = null;
        InputStream r2 = this.activity.batchOpen(r8.url, null, Long.MAX_VALUE);     // Catch: Throwable -> L24
        Reader r3 = PlaylistInput.reader(r2);     // Catch: Throwable -> L16
        List<Entry> r4 = JsonPlaylist.read(r3, r8.kind).entries;     // Catch: Throwable -> L11
        BlackBox.event("TXT_LIST_PARSED", "line=" + r8.line + " entries=" + r4.size());     // Catch: Throwable -> L11
        if (r3 == null) goto L8;
        r3.close();     // Catch: Throwable -> L16
    L8:
        if (r2 == null) goto L10;
        r2.close();     // Catch: Throwable -> L24
    L10:
        return r4;
    L11:
        th = move-exception;
        if (r3 == null) goto L34;
        r3.close();     // Catch: Throwable -> L16
        throw th;     // Catch: Throwable -> L16
    L34:
        throw th;     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        if (0 != 0) goto L19;
        r0 = th;
    L21:
        if (r2 == null) goto L35;
        r2.close();     // Catch: Throwable -> L24
        throw r0;     // Catch: Throwable -> L24
    L35:
        throw r0;     // Catch: Throwable -> L24
    L19:
        if (null == th) goto L21;
        r0.addSuppressed(th);     // Catch: Throwable -> L24
    L24:
        th = move-exception;
        if (0 == 0) goto L36;
        if (null == th) goto L29;
        r0.addSuppressed(th);
    L29:
        throw null;
    L36:
        throw th;
    }

    /* JADX INFO: renamed from: lambda$1$es-antonio-m3u-BatchImport, reason: not valid java name */
    /* synthetic */ void m522lambda$1$esantoniom3uBatchImport(final BatchPlaylist.Row r3, final int r4, final int r5) {
        this.activity.runOnUiThread(new BatchImport$$ExternalSyntheticLambda1(this, r4, r5, r3));
    }

    /* JADX INFO: renamed from: lambda$3$es-antonio-m3u-BatchImport, reason: not valid java name */
    /* synthetic */ void m523lambda$3$esantoniom3uBatchImport(int r4, int r5, BatchPlaylist.Row r6) {
        this.activity.batchProgress("Importando " + r4 + "/" + r5 + " · " + r6.kind + " · " + r6.provider);
    }

    /* JADX INFO: renamed from: lambda$4$es-antonio-m3u-BatchImport, reason: not valid java name */
    /* synthetic */ void m524lambda$4$esantoniom3uBatchImport(BatchPlaylist.Result r2, String r3) {
        this.activity.batchFinished(r2.catalog, r3);
    }

    @Override // java.lang.Runnable
    public void run() {
        Throwable r1 = null;
        InputStream r2 = this.activity.batchOpen(null, this.file, PlaybackStateCompat.ACTION_SET_PLAYBACK_SPEED);     // Catch: Throwable -> L35
        Reader r3 = PlaylistInput.reader(r2);     // Catch: Throwable -> L27
        BatchPlaylist.Document r4 = BatchPlaylist.parse(r3);     // Catch: Throwable -> L23
        if (r3 == null) goto L8;
        r3.close();     // Catch: Throwable -> L27
    L8:
        if (r2 == null) goto L10;
        r2.close();     // Catch: Throwable -> L35
    L10:
        BlackBox.event("TXT_IMPORT_BEGIN", "lists=" + r4.rows.size());     // Catch: Exception -> L41
        final AppStorage r5 = new AppStorage(this.activity.getApplicationContext());     // Catch: Exception -> L41
        final BatchPlaylist.Result r6 = BatchPlaylist.importLists(r4, this.snapshot, new BatchImport$$ExternalSyntheticLambda2(this), new BatchImport$$ExternalSyntheticLambda3(this), new BatchImport$$ExternalSyntheticLambda4(r5));     // Catch: Exception -> L41
        if (r6.imported <= 0) goto L13;
        CdnTokens.reconcile(this.activity.getApplicationContext(), r6.catalog);     // Catch: Exception -> L41
    L13:
        final String r7 = String.valueOf(r6.imported) + " listas importadas · " + r6.entries + " entradas";     // Catch: Exception -> L41
        if (r6.imported != 0) goto L19;
        if (r6.errors.isEmpty() == false) goto L19;
        r7 = "El TXT no contiene listas";
    L19:
        if (r6.errors.isEmpty() == true) goto L21;
        r7 = String.valueOf(r7) + "\n\n" + BatchImport$$ExternalSyntheticBackport0.m390m("\n", r6.errors);     // Catch: Exception -> L41
    L21:
        BlackBox.event("TXT_IMPORT_FINISHED", "lists=" + r6.imported + " entries=" + r6.entries + " errors=" + r6.errors.size());     // Catch: Exception -> L41
        this.activity.runOnUiThread(new BatchImport$$ExternalSyntheticLambda5(this, r6, r7));     // Catch: Exception -> L41
        return;
    L23:
        th = move-exception;
        if (r3 == null) goto L26;
        r3.close();     // Catch: Throwable -> L27
    L26:
        throw th;     // Catch: Throwable -> L27
    L27:
        th = move-exception;
        if (0 != 0) goto L30;
        r1 = th;
    L32:
        if (r2 == null) goto L48;
        r2.close();     // Catch: Throwable -> L35
        throw r1;     // Catch: Throwable -> L35
    L48:
        throw r1;     // Catch: Throwable -> L35
    L30:
        if (null == th) goto L32;
        r1.addSuppressed(th);     // Catch: Throwable -> L35
    L41:
        e = move-exception;
        BlackBox.event("TXT_IMPORT_FAILED", "type=" + e.getClass().getSimpleName());
        this.activity.batchFailed(e);
        return;
    L35:
        th = move-exception;
        if (0 == 0) goto L49;
        if (null == th) goto L40;
        r1.addSuppressed(th);     // Catch: Exception -> L41
    L40:
        throw null;     // Catch: Exception -> L41
    }
}
