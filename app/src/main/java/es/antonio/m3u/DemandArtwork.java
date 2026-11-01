package es.antonio.m3u;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes7.dex */
final class DemandArtwork {
    private static final Map<String, File> legacy = null;
    private static final ThreadPoolExecutor local = null;

    static final class Ready implements Runnable {
        final Bitmap bitmap;
        final LogoLoader owner;
        final String url;

        Ready(LogoLoader r1, String r2, Bitmap r3) {
            this.owner = r1;
            this.url = r2;
            this.bitmap = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.owner.artworkReady(this.url, this.bitmap);
        }
    }

    static final class Work implements Runnable {
        final Runnable job;
        final ThreadPoolExecutor network;
        final LogoLoader owner;
        final boolean probe;
        final String url;

        Work(LogoLoader r1, ThreadPoolExecutor r2, Runnable r3, String r4, boolean r5) {
            this.owner = r1;
            this.network = r2;
            this.job = r3;
            this.url = r4;
            this.probe = r5;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.owner.artworkWanted(this.url) == true) goto L7;
            this.owner.artworkDiscard(this.url);
            return;
        L7:
            if (this.probe == true) goto L9;
        L16:
            this.job.run();
            return;
        L9:
            if (this.owner.artworkCached(this.url) == true) goto L16;
            DemandArtwork.prune(this.network, this.owner, false);
            this.network.execute(new Work(this.owner, this.network, this.job, this.url, false));     // Catch: RejectedExecutionException -> L13
            return;
        L14:
            this.owner.artworkDiscard(this.url);
        }
    }

    static {
        local = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(64), new DemandArtwork$$ExternalSyntheticLambda1(), new ThreadPoolExecutor.AbortPolicy());
        legacy = new HashMap();
    }

    DemandArtwork() {
    }

    static void cancel(LogoLoader r2, ThreadPoolExecutor r3) {
        prune(local, r2, true);
        prune(r3, r2, true);
    }

    public static void dispatch(LogoLoader r8, ThreadPoolExecutor r9, Runnable r10, String r11) {
        local.execute(new Work(r8, r9, r10, r11, true));
    }

    static /* synthetic */ Thread lambda$0(final Runnable r2) {
        Thread r0 = new Thread(new DemandArtwork$$ExternalSyntheticLambda2(r2), "CHDTVArtworkDisk");
        r0.setPriority(1);
        r0.setDaemon(true);
        return r0;
    }

    static /* synthetic */ void lambda$1(Runnable r0) {
        BackgroundWork.lowPriority();
        r0.run();
    }

    static /* synthetic */ void lambda$2(Bitmap r2, OutputStream r3) throws IOException {
        if (r2.hasAlpha() == false) goto L5;
        Bitmap.CompressFormat r0 = Bitmap.CompressFormat.PNG;
    L7:
        if (r2.compress(r0, 85, r3) == false) goto L10;
        return;
    L10:
        throw new IOException("Artwork encode");
    L5:
        r0 = Bitmap.CompressFormat.JPEG;
        goto L7
    }

    static File previous(File r2, File r3) {
        Map<String, File> r0 = legacy;
        monitor-enter(r0);
        File r4 = legacy.get(r2.getAbsolutePath());     // Catch: Throwable -> L10
        if (r4 != null) goto L7;
        File r5 = null;
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r5;
    L7:
        r5 = new File(r4, r3.getName());     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        throw th;
    }

    static void prune(ThreadPoolExecutor r3, LogoLoader r4, boolean r5) {
        Iterator r0 = r3.getQueue().iterator();
    L4:
        if (r0.hasNext() == false) goto L5;
        Runnable r1 = (Runnable) r0.next();
        if ((r1 instanceof Work) == false) goto L4;
        Work r2 = (Work) r1;
        if (r2.owner != r4) goto L4;
        if (r5 == true) goto L14;
        if (r4.artworkWanted(r2.url) == true) goto L4;
    L14:
        if (r3.remove(r2) == false) goto L4;
        r4.artworkDiscard(r2.url);
        goto L4
    }

    public static File root(Context r6) {
        File r0 = r6.getFilesDir();
        Map<String, File> r1 = legacy;
        monitor-enter(r1);
        legacy.put(new File(r0, "artwork-v1").getAbsolutePath(), new File(r6.getCacheDir(), "artwork-v1"));     // Catch: Throwable -> L7
        monitor-exit(r1);     // Catch: Throwable -> L7
        return r0;
    L7:
        th = move-exception;
        throw th;
    }

    static void save(File r6, File r7, final Bitmap r8) {
        DemandDiskCache.put(r6, r7, new DemandArtwork$$ExternalSyntheticLambda0(r8), 268435456, 10000);
    }
}
