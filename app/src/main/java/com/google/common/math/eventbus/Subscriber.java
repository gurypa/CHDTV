package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class Subscriber {
    private EventBus bus;
    private final Executor executor;
    private final Method method;
    final Object target;

    /* JADX INFO: renamed from: com.google.common.eventbus.Subscriber$1 */
    static /* synthetic */ class C07871 {
    }

    static final class SynchronizedSubscriber extends Subscriber {
        private SynchronizedSubscriber(EventBus r2, Object r3, Method r4) {
            super(r2, r3, r4, null);
        }

        /* synthetic */ SynchronizedSubscriber(EventBus r1, Object r2, Method r3, C07871 r4) {
            this(r1, r2, r3);
        }

        @Override // com.google.common.eventbus.Subscriber
        void invokeSubscriberMethod(Object r2) throws InvocationTargetException {
            monitor-enter(this);
            super.invokeSubscriberMethod(r2);     // Catch: Throwable -> L6
            monitor-exit(this);     // Catch: Throwable -> L6
            return;
        L6:
            th = move-exception;
            throw th;
        }
    }

    private Subscriber(EventBus r2, Object r3, Method r4) {
        this.bus = r2;
        this.target = Preconditions.checkNotNull(r3);
        this.method = r4;
        r4.setAccessible(true);
        this.executor = r2.executor();
    }

    /* synthetic */ Subscriber(EventBus r1, Object r2, Method r3, C07871 r4) {
        this(r1, r2, r3);
    }

    private SubscriberExceptionContext context(Object r5) {
        return new SubscriberExceptionContext(this.bus, r5, this.target, this.method);
    }

    static Subscriber create(EventBus r2, Object r3, Method r4) {
        if (isDeclaredThreadSafe(r4) == false) goto L6;
        return new Subscriber(r2, r3, r4);
    L6:
        return new SynchronizedSubscriber(r2, r3, r4, null);
    }

    private static boolean isDeclaredThreadSafe(Method r1) {
        if (r1.getAnnotation(AllowConcurrentEvents.class) == null) goto L5;
        return true;
    L5:
        return false;
    }

    final void dispatchEvent(final Object r3) {
        this.executor.execute(new Subscriber$$ExternalSyntheticLambda0(this, r3));
    }

    public final boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof Subscriber) == false) goto L10;
        Subscriber r0 = (Subscriber) r5;
        if (this.target == r0.target) goto L7;
        return false;
    L7:
        if (this.method.equals(r0.method) == false) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    public final int hashCode() {
        return ((this.method.hashCode() + 31) * 31) + System.identityHashCode(this.target);
    }

    void invokeSubscriberMethod(Object r6) throws InvocationTargetException {
        this.method.invoke(this.target, new Object[]{Preconditions.checkNotNull(r6)});     // Catch: InvocationTargetException -> L4 IllegalAccessException -> L10 IllegalArgumentException -> L13
        return;
    L10:
        e = move-exception;
        throw new Error("Method became inaccessible: " + r6, e);
    L13:
        e = move-exception;
        throw new Error("Method rejected target/argument: " + r6, e);
    L4:
        e = move-exception;
        if ((e.getCause() instanceof Error) == true) goto L8;
        throw e;
    L8:
        throw ((Error) e.getCause());
    }

    /* JADX INFO: renamed from: lambda$dispatchEvent$0$com-google-common-eventbus-Subscriber, reason: not valid java name */
    /* synthetic */ void m501lambda$dispatchEvent$0$comgooglecommoneventbusSubscriber(Object r5) {
        invokeSubscriberMethod(r5);     // Catch: InvocationTargetException -> L4
        return;
    L4:
        e = move-exception;
        this.bus.handleSubscriberException(e.getCause(), context(r5));
    }
}
