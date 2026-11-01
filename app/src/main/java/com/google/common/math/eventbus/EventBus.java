package com.google.common.eventbus;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class EventBus {
    private static final Logger logger = null;
    private final Dispatcher dispatcher;
    private final SubscriberExceptionHandler exceptionHandler;
    private final Executor executor;
    private final String identifier;
    private final SubscriberRegistry subscribers;

    static final class LoggingHandler implements SubscriberExceptionHandler {
        static final LoggingHandler INSTANCE = null;

        static {
            INSTANCE = new LoggingHandler();
        }

        LoggingHandler() {
        }

        private static Logger logger(SubscriberExceptionContext r2) {
            return Logger.getLogger(EventBus.class.getName() + "." + r2.getEventBus().identifier());
        }

        private static String message(SubscriberExceptionContext r4) {
            Method r0 = r4.getSubscriberMethod();
            return "Exception thrown by subscriber method " + r0.getName() + '(' + r0.getParameterTypes()[0].getName() + ") on subscriber " + r4.getSubscriber() + " when dispatching event: " + r4.getEvent();
        }

        @Override // com.google.common.eventbus.SubscriberExceptionHandler
        public void handleException(Throwable r4, SubscriberExceptionContext r5) {
            Logger r0 = logger(r5);
            if (r0.isLoggable(Level.SEVERE) == false) goto L6;
            r0.log(Level.SEVERE, message(r5), r4);
            return;
        }
    }

    static {
        logger = Logger.getLogger(EventBus.class.getName());
    }

    public EventBus() {
        this("default");
    }

    public EventBus(SubscriberExceptionHandler r4) {
        this("default", MoreExecutors.directExecutor(), Dispatcher.perThreadDispatchQueue(), r4);
    }

    public EventBus(String r4) {
        this(r4, MoreExecutors.directExecutor(), Dispatcher.perThreadDispatchQueue(), LoggingHandler.INSTANCE);
    }

    EventBus(String r2, Executor r3, Dispatcher r4, SubscriberExceptionHandler r5) {
        this.subscribers = new SubscriberRegistry(this);
        this.identifier = (String) Preconditions.checkNotNull(r2);
        this.executor = (Executor) Preconditions.checkNotNull(r3);
        this.dispatcher = (Dispatcher) Preconditions.checkNotNull(r4);
        this.exceptionHandler = (SubscriberExceptionHandler) Preconditions.checkNotNull(r5);
    }

    final Executor executor() {
        return this.executor;
    }

    void handleSubscriberException(Throwable r7, SubscriberExceptionContext r8) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        this.exceptionHandler.handleException(r7, r8);     // Catch: Throwable -> L5
        return;
    L5:
        th = move-exception;
        logger.log(Level.SEVERE, String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", new Object[]{th, r7}), th);
    }

    public final String identifier() {
        return this.identifier;
    }

    public void post(Object r3) {
        Iterator<Subscriber> r0 = this.subscribers.getSubscribers(r3);
        if (r0.hasNext() == false) goto L6;
        this.dispatcher.dispatch(r3, r0);
        return;
    L6:
        if ((r3 instanceof DeadEvent) == true) goto L10;
        post(new DeadEvent(this, r3));
        return;
    }

    public void register(Object r2) {
        this.subscribers.register(r2);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(this.identifier).toString();
    }

    public void unregister(Object r2) {
        this.subscribers.unregister(r2);
    }
}
