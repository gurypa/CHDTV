package com.google.common.util.concurrent;

import androidx.core.app.NotificationCompat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ListenerCallQueue<L> {
    private static final LazyLogger logger = null;
    private final List<PerListenerQueue<L>> listeners;

    interface Event<L> {
        void call(L r1);
    }

    private static final class PerListenerQueue<L> implements Runnable {
        final Executor executor;
        boolean isThreadScheduled;
        final Queue<Object> labelQueue;
        final L listener;
        final Queue<Event<L>> waitQueue;

        PerListenerQueue(L r2, Executor r3) {
            this.waitQueue = Queues.newArrayDeque();
            this.labelQueue = Queues.newArrayDeque();
            this.listener = (L) Preconditions.checkNotNull(r2);
            this.executor = (Executor) Preconditions.checkNotNull(r3);
        }

        synchronized void add(Event<L> r2, Object r3) {
            monitor-enter(this);
            this.waitQueue.add(r2);     // Catch: Throwable -> L6
            this.labelQueue.add(r3);     // Catch: Throwable -> L6
            monitor-exit(this);
            return;
        L6:
            th = move-exception;
            throw th;
        }

        void dispatch() {
            boolean r0 = false;
            monitor-enter(this);
        L22:
            th = move-exception;
            throw th;
        L5:
            if (this.isThreadScheduled == true) goto L7;
            this.isThreadScheduled = true;     // Catch: Throwable -> L22
            r0 = true;
        L7:
            monitor-exit(this);     // Catch: Throwable -> L22
            if (r0 == true) goto L29;
            return;
        L29:
            this.executor.execute(this);     // Catch: Exception -> L11
            return;
        L11:
            e = move-exception;
            monitor-enter(this);
            this.isThreadScheduled = false;     // Catch: Throwable -> L18
            ListenerCallQueue.access$000().get().log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
            throw e;
        L18:
            th = move-exception;
            throw th;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean r0 = true;
        L45:
            monitor-enter(this);     // Catch: Throwable -> L27
            Preconditions.checkState(this.isThreadScheduled);     // Catch: Throwable -> L24
            Event<L> r2 = this.waitQueue.poll();     // Catch: Throwable -> L24
            Object r3 = this.labelQueue.poll();     // Catch: Throwable -> L24
            if (r2 == null) goto L7;
            monitor-exit(this);     // Catch: Throwable -> L24
            r2.call(this.listener);     // Catch: Exception -> L21 Throwable -> L27
        L21:
            e = move-exception;
            ListenerCallQueue.access$000().get().log(Level.SEVERE, "Exception while executing callback: " + this.listener + " " + r3, e);     // Catch: Throwable -> L27
            goto L45
        L7:
            this.isThreadScheduled = false;     // Catch: Throwable -> L24
            r0 = false;
            monitor-exit(this);     // Catch: Throwable -> L24
            if (0 == 0) goto L17;
            monitor-enter(this);
            this.isThreadScheduled = false;     // Catch: Throwable -> L14
            monitor-exit(this);     // Catch: Throwable -> L14
            return;
        L14:
            th = move-exception;
            throw th;
        L17:
            return;
        L24:
            th = move-exception;
            throw th;     // Catch: Throwable -> L27
        L27:
            th = move-exception;
            if (r0 == false) goto L36;
            monitor-enter(this);
            this.isThreadScheduled = false;     // Catch: Throwable -> L33
        L33:
            th = move-exception;
            throw th;
        L36:
            throw th;
        }
    }

    static {
        logger = new LazyLogger(ListenerCallQueue.class);
    }

    ListenerCallQueue() {
        this.listeners = Collections.synchronizedList(new ArrayList());
    }

    static /* synthetic */ LazyLogger access$000() {
        return logger;
    }

    private void enqueueHelper(Event<L> r4, Object r5) {
        Preconditions.checkNotNull(r4, NotificationCompat.CATEGORY_EVENT);
        Preconditions.checkNotNull(r5, "label");
        List<PerListenerQueue<L>> r0 = this.listeners;
        monitor-enter(r0);
        Iterator<PerListenerQueue<L>> r1 = this.listeners.iterator();     // Catch: Throwable -> L10
    L6:
        if (r1.hasNext() == false) goto L8;
        r1.next().add(r4, r5);     // Catch: Throwable -> L10
        goto L6
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return;
    L10:
        th = move-exception;
        throw th;
    }

    public void addListener(L r3, Executor r4) {
        Preconditions.checkNotNull(r3, "listener");
        Preconditions.checkNotNull(r4, "executor");
        this.listeners.add(new PerListenerQueue(r3, r4));
    }

    public void dispatch() {
        int r0 = 0;
    L4:
        if (r0 >= this.listeners.size()) goto L6;
        this.listeners.get(r0).dispatch();
        r0 = r0 + 1;
        goto L4
    }

    public void enqueue(Event<L> r1) {
        enqueueHelper(r1, r1);
    }

    public void enqueue(Event<L> r1, String r2) {
        enqueueHelper(r1, r2);
    }
}
