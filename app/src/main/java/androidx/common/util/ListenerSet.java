package androidx.media3.common.util;

import android.os.Looper;
import android.os.Message;
import androidx.media3.common.FlagSet;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: loaded from: classes.dex */
public final class ListenerSet<T> {
    private static final int MSG_ITERATION_FINISHED = 1;
    private final Clock clock;
    private final ArrayDeque<Runnable> flushingEvents;
    private final HandlerWrapper handler;
    private final IterationFinishedEvent<T> iterationFinishedEvent;
    private final CopyOnWriteArraySet<ListenerHolder<T>> listeners;
    private final ArrayDeque<Runnable> queuedEvents;
    private boolean released;
    private final Object releasedLock;
    private boolean throwsWhenUsingWrongThread;

    public interface Event<T> {
        void invoke(T r1);
    }

    public interface IterationFinishedEvent<T> {
        void invoke(T r1, FlagSet r2);
    }

    private static final class ListenerHolder<T> {
        private FlagSet.Builder flagsBuilder;
        public final T listener;
        private boolean needsIterationFinishedEvent;
        private boolean released;

        public ListenerHolder(T r2) {
            this.listener = r2;
            this.flagsBuilder = new FlagSet.Builder();
        }

        public boolean equals(Object r3) {
            if (this != r3) goto L5;
            return true;
        L5:
            if (r3 != null) goto L7;
            return false;
        L7:
            if (getClass() == r3.getClass()) goto L10;
            return false;
        L10:
            return this.listener.equals(((ListenerHolder) r3).listener);
        }

        public int hashCode() {
            return this.listener.hashCode();
        }

        public void invoke(int r2, Event<T> r3) {
            if (this.released == false) goto L5;
            return;
        L5:
            if (r2 == (-1)) goto L7;
            this.flagsBuilder.add(r2);
        L7:
            this.needsIterationFinishedEvent = true;
            r3.invoke(this.listener);
        }

        public void iterationFinished(IterationFinishedEvent<T> r3) {
            if (this.released == false) goto L5;
            return;
        L5:
            if (this.needsIterationFinishedEvent == false) goto L9;
            FlagSet r0 = this.flagsBuilder.build();
            this.flagsBuilder = new FlagSet.Builder();
            this.needsIterationFinishedEvent = false;
            r3.invoke(this.listener, r0);
            return;
        }

        public void release(IterationFinishedEvent<T> r3) {
            this.released = true;
            if (this.needsIterationFinishedEvent == false) goto L6;
            this.needsIterationFinishedEvent = false;
            r3.invoke(this.listener, this.flagsBuilder.build());
            return;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$rFcF5Pkb99AL585p5-2u78YfNkY, reason: not valid java name */
    public static /* synthetic */ boolean m466$r8$lambda$rFcF5Pkb99AL585p52u78YfNkY(ListenerSet r0, Message r1) {
        return r0.handleMessage(r1);
    }

    public ListenerSet(Looper r7, Clock r8, IterationFinishedEvent<T> r9) {
        this(new CopyOnWriteArraySet(), r7, r8, r9, true);
    }

    private ListenerSet(CopyOnWriteArraySet<ListenerHolder<T>> r2, Looper r3, Clock r4, IterationFinishedEvent<T> r5, boolean r6) {
        this.clock = r4;
        this.listeners = r2;
        this.iterationFinishedEvent = r5;
        this.releasedLock = new Object();
        this.flushingEvents = new ArrayDeque();
        this.queuedEvents = new ArrayDeque();
        this.handler = r4.createHandler(r3, new ListenerSet$$ExternalSyntheticLambda0(this));
        this.throwsWhenUsingWrongThread = r6;
    }

    private boolean handleMessage(Message r5) {
        Iterator<ListenerHolder<T>> r0 = this.listeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        r0.next().iterationFinished(this.iterationFinishedEvent);
        if (this.handler.hasMessages(1) == false) goto L4;
    L9:
        return true;
    }

    static /* synthetic */ void lambda$queueEvent$0(CopyOnWriteArraySet r2, int r3, Event r4) {
        Iterator r0 = r2.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        ((ListenerHolder) r0.next()).invoke(r3, r4);
        goto L4
    }

    private void verifyCurrentThread() {
        if (this.throwsWhenUsingWrongThread == true) goto L6;
        return;
    L6:
        if (Thread.currentThread() != this.handler.getLooper().getThread()) goto L8;
        boolean r0 = true;
    L9:
        Assertions.checkState(r0);
        return;
    L8:
        r0 = false;
        goto L9
    }

    public void add(T r4) {
        Assertions.checkNotNull(r4);
        Object r0 = this.releasedLock;
        monitor-enter(r0);
    L11:
        th = move-exception;
        throw th;
    L5:
        if (this.released == false) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L11
        return;
    L8:
        this.listeners.add(new ListenerHolder(r4));     // Catch: Throwable -> L11
        monitor-exit(r0);     // Catch: Throwable -> L11
    }

    public void clear() {
        verifyCurrentThread();
        Iterator<ListenerHolder<T>> r0 = this.listeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().release(this.iterationFinishedEvent);
        goto L4
    L6:
        this.listeners.clear();
    }

    public ListenerSet<T> copy(Looper r7, Clock r8, IterationFinishedEvent<T> r9) {
        return new ListenerSet(this.listeners, r7, r8, r9, this.throwsWhenUsingWrongThread);
    }

    public ListenerSet<T> copy(Looper r2, IterationFinishedEvent<T> r3) {
        return copy(r2, this.clock, r3);
    }

    public void flushEvents() {
        verifyCurrentThread();
        if (this.queuedEvents.isEmpty() == false) goto L6;
        return;
    L6:
        if (this.handler.hasMessages(1) == true) goto L8;
        this.handler.sendMessageAtFrontOfQueue(this.handler.obtainMessage(1));
    L8:
        boolean r0 = !this.flushingEvents.isEmpty();
        this.flushingEvents.addAll(this.queuedEvents);
        this.queuedEvents.clear();
        if (r0 == false) goto L12;
        return;
    L12:
        if (this.flushingEvents.isEmpty() == true) goto L14;
        this.flushingEvents.peekFirst().run();
        this.flushingEvents.removeFirst();
        goto L12
    }

    public void queueEvent(final int r4, final Event<T> r5) {
        verifyCurrentThread();
        final CopyOnWriteArraySet r0 = new CopyOnWriteArraySet(this.listeners);
        this.queuedEvents.add(new ListenerSet$$ExternalSyntheticLambda1(r0, r4, r5));
    }

    public void release() {
        verifyCurrentThread();
        Object r0 = this.releasedLock;
        monitor-enter(r0);
        this.released = true;     // Catch: Throwable -> L13
        monitor-exit(r0);     // Catch: Throwable -> L13
        Iterator<ListenerHolder<T>> r1 = this.listeners.iterator();
    L9:
        if (r1.hasNext() == false) goto L11;
        r1.next().release(this.iterationFinishedEvent);
        goto L9
    L11:
        this.listeners.clear();
        return;
    L13:
        th = move-exception;
        throw th;
    }

    public void remove(T r4) {
        verifyCurrentThread();
        Iterator<ListenerHolder<T>> r0 = this.listeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        ListenerHolder<T> r1 = r0.next();
        if (r1.listener.equals(r4) == false) goto L4;
        r1.release(this.iterationFinishedEvent);
        this.listeners.remove(r1);
        goto L4
    }

    public void sendEvent(int r1, Event<T> r2) {
        queueEvent(r1, r2);
        flushEvents();
    }

    @Deprecated
    public void setThrowsWhenUsingWrongThread(boolean r1) {
        this.throwsWhenUsingWrongThread = r1;
    }

    public int size() {
        verifyCurrentThread();
        return this.listeners.size();
    }
}
