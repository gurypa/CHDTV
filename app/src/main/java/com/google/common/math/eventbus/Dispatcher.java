package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class Dispatcher {

    /* JADX INFO: renamed from: com.google.common.eventbus.Dispatcher$1 */
    static /* synthetic */ class C07841 {
    }

    private static final class ImmediateDispatcher extends Dispatcher {
        private static final ImmediateDispatcher INSTANCE = null;

        static {
            INSTANCE = new ImmediateDispatcher();
        }

        private ImmediateDispatcher() {
        }

        static /* synthetic */ ImmediateDispatcher access$200() {
            return INSTANCE;
        }

        @Override // com.google.common.eventbus.Dispatcher
        void dispatch(Object r2, Iterator<Subscriber> r3) {
            Preconditions.checkNotNull(r2);
        L4:
            if (r3.hasNext() == false) goto L6;
            r3.next().dispatchEvent(r2);
            goto L4
        }
    }

    private static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        private static final class EventWithSubscriber {
            private final Object event;
            private final Subscriber subscriber;

            private EventWithSubscriber(Object r1, Subscriber r2) {
                this.event = r1;
                this.subscriber = r2;
            }

            /* synthetic */ EventWithSubscriber(Object r1, Subscriber r2, C07841 r3) {
                this(r1, r2);
            }

            static /* synthetic */ Object access$700(EventWithSubscriber r1) {
                return r1.event;
            }

            static /* synthetic */ Subscriber access$800(EventWithSubscriber r1) {
                return r1.subscriber;
            }
        }

        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }

        /* synthetic */ LegacyAsyncDispatcher(C07841 r1) {
            this();
        }

        @Override // com.google.common.eventbus.Dispatcher
        void dispatch(Object r5, Iterator<Subscriber> r6) {
            Preconditions.checkNotNull(r5);
        L4:
            if (r6.hasNext() == false) goto L6;
            this.queue.add(new EventWithSubscriber(r5, r6.next(), null));
        L6:
            EventWithSubscriber r0 = this.queue.poll();
            if (r0 == null) goto L9;
            EventWithSubscriber.access$800(r0).dispatchEvent(EventWithSubscriber.access$700(r0));
            goto L6
        }
    }

    private static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Boolean> dispatching;
        private final ThreadLocal<Queue<Event>> queue;

        private static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object r1, Iterator<Subscriber> r2) {
                this.event = r1;
                this.subscribers = r2;
            }

            /* synthetic */ Event(Object r1, Iterator r2, C07841 r3) {
                this(r1, r2);
            }

            static /* synthetic */ Iterator access$400(Event r1) {
                return r1.subscribers;
            }

            static /* synthetic */ Object access$500(Event r1) {
                return r1.event;
            }
        }

        private PerThreadQueuedDispatcher() {
            this.queue = new C07851(this);
            this.dispatching = new C07862(this);
        }

        /* synthetic */ PerThreadQueuedDispatcher(C07841 r1) {
            this();
        }

        @Override // com.google.common.eventbus.Dispatcher
        void dispatch(Object r5, Iterator<Subscriber> r6) {
            Preconditions.checkNotNull(r5);
            Preconditions.checkNotNull(r6);
            Queue r0 = (Queue) Objects.requireNonNull(this.queue.get());
            r0.offer(new Event(r5, r6, null));
            if (this.dispatching.get().booleanValue() == true) goto L15;
            this.dispatching.set(true);
        L16:
            Event r1 = (Event) r0.poll();     // Catch: Throwable -> L12
            if (r1 == null) goto L11;
        L8:
            if (Event.access$400(r1).hasNext() == false) goto L16;
            ((Subscriber) Event.access$400(r1).next()).dispatchEvent(Event.access$500(r1));     // Catch: Throwable -> L12
            goto L8
        L11:
            this.dispatching.remove();
            this.queue.remove();
            return;
        L12:
            th = move-exception;
            this.dispatching.remove();
            this.queue.remove();
            throw th;
        }
    }

    Dispatcher() {
    }

    static Dispatcher immediate() {
        return ImmediateDispatcher.access$200();
    }

    static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher(null);
    }

    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher(null);
    }

    abstract void dispatch(Object r1, Iterator<Subscriber> r2);
}
