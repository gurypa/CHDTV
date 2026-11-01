package es.antonio.m3u;

import android.os.Handler;
import android.os.Looper;
import androidx.media3.common.PlaybackException;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes7.dex */
final class ChannelFallbackWork {
    static final Map<PlayerActivity, Search> ACTIVE = null;
    static final Handler MAIN = null;
    static final ThreadPoolExecutor POOL = null;

    static final class Delivery implements Runnable {
        final ChannelFallback.Choice choice;
        final Throwable failure;
        final Search task;

        Delivery(Search r1, ChannelFallback.Choice r2, Throwable r3) {
            this.task = r1;
            this.choice = r2;
            this.failure = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            PlayerActivity r1 = this.task.owner.get();
            if (r1 != null) goto L5;
            return;
        L5:
            if (this.task.cancelled == true) goto L22;
            Map<PlayerActivity, Search> r0 = ChannelFallbackWork.ACTIVE;
            monitor-enter(r0);
        L17:
            th = move-exception;
            throw th;
        L10:
            if (ChannelFallbackWork.ACTIVE.get(r1) == this.task) goto L13;
            monitor-exit(r0);     // Catch: Throwable -> L17
            return;
        L13:
            ChannelFallbackWork.ACTIVE.remove(r1);     // Catch: Throwable -> L17
            monitor-exit(r0);     // Catch: Throwable -> L17
            r1.cfReady(this.task.current, this.task.ticket, this.task.error, this.choice, this.failure);
            return;
        }
    }

    static final class Lookup implements Callable<ChannelFallback.Choice> {
        final Catalog catalog;
        final ChannelFallback chain;
        final Entry current;

        Lookup(Catalog r1, ChannelFallback r2, Entry r3) {
            this.catalog = r1;
            this.chain = r2;
            this.current = r3;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public ChannelFallback.Choice call() {
            long r4 = System.nanoTime();
            ChannelFallback.Choice r8 = this.chain.next(this.catalog, this.current);     // Catch: Throwable -> L6
            BlackBox.event("CHANNEL_FALLBACK_SEARCH", "duration_ms=" + ((System.nanoTime() - r4) / 1000000) + " format=" + StreamFormat.label(this.current) + " background=true tv_only=true");
            return r8;
        L6:
            th = move-exception;
            BlackBox.event("CHANNEL_FALLBACK_SEARCH", "duration_ms=" + ((System.nanoTime() - r4) / 1000000) + " format=" + StreamFormat.label(this.current) + " background=true tv_only=true");
            throw th;
        }

        @Override // java.util.concurrent.Callable
        public /* bridge */ /* synthetic */ ChannelFallback.Choice call() throws Exception {
            return call();
        }
    }

    static final class Search extends FutureTask<ChannelFallback.Choice> {
        volatile boolean cancelled;
        final Entry current;
        final PlaybackException error;
        final WeakReference<PlayerActivity> owner;
        final int ticket;

        Search(PlayerActivity r2, Catalog r3, ChannelFallback r4, Entry r5, int r6, PlaybackException r7) {
            super(new Lookup(r3, r4, r5));
            this.owner = new WeakReference(r2);
            this.current = r5;
            this.ticket = r6;
            this.error = r7;
        }

        @Override // java.util.concurrent.FutureTask
        protected void done() {
            if (this.cancelled == false) goto L5;
            return;
        L5:
            if (isCancelled() == true) goto L19;
            ChannelFallback.Choice r0 = null;
            e = null;
            r0 = get();     // Catch: Exception -> L10 ExecutionException -> L12
        L14:
            ChannelFallbackWork.MAIN.post(new Delivery(this, r0, e));
            return;
        L12:
            e = move-exception;
            e = e.getCause();
        L10:
            e = e;
            goto L14
        }

        void reject(Throwable r1) {
            setException(r1);
        }
    }

    static final class Select implements Runnable {
        final String list;
        final WeakReference<PlayerActivity> owner;
        final Entry requested;
        final Entry target;
        final int ticket;

        Select(PlayerActivity r2, Entry r3, int r4, Entry r5, String r6) {
            this.owner = new WeakReference(r2);
            this.requested = r3;
            this.ticket = r4;
            this.target = r5;
            this.list = r6;
        }

        @Override // java.lang.Runnable
        public void run() {
            PlayerActivity r0 = this.owner.get();
            if (r0 == null) goto L6;
            r0.cfSelect(this.requested, this.ticket, this.target, this.list);
            return;
        }
    }

    static {
        MAIN = new Handler(Looper.getMainLooper());
        ACTIVE = new WeakHashMap();
        POOL = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(2), new ThreadFactoryC09731());
        POOL.allowCoreThreadTimeOut(true);
    }

    private ChannelFallbackWork() {
    }

    public static void cancel(Object r2) {
        Map<PlayerActivity, Search> r0 = ACTIVE;
        monitor-enter(r0);
        Search r3 = ACTIVE.remove(r2);     // Catch: Throwable -> L9
        monitor-exit(r0);     // Catch: Throwable -> L9
        if (r3 == null) goto L14;
        r3.cancelled = true;
        r3.cancel(true);
        POOL.remove(r3);
        return;
    L14:
        return;
    L9:
        th = move-exception;
        throw th;
    }

    static void submit(PlayerActivity r8, Catalog r9, ChannelFallback r10, Entry r11, int r12, PlaybackException r13) {
        cancel(r8);
        Search r7 = new Search(r8, r9, r10, r11, r12, r13);
        Map<PlayerActivity, Search> r14 = ACTIVE;
        monitor-enter(r14);
        ACTIVE.put(r8, r7);     // Catch: Throwable -> L11
        monitor-exit(r14);     // Catch: Throwable -> L11
        POOL.execute(r7);     // Catch: RejectedExecutionException -> L8
        return;
    L8:
        e = move-exception;
        r7.reject(e);
        return;
    L11:
        th = move-exception;
        throw th;
    }
}
