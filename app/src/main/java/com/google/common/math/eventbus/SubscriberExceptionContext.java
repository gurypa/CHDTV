package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class SubscriberExceptionContext {
    private final Object event;
    private final EventBus eventBus;
    private final Object subscriber;
    private final Method subscriberMethod;

    SubscriberExceptionContext(EventBus r2, Object r3, Object r4, Method r5) {
        this.eventBus = (EventBus) Preconditions.checkNotNull(r2);
        this.event = Preconditions.checkNotNull(r3);
        this.subscriber = Preconditions.checkNotNull(r4);
        this.subscriberMethod = (Method) Preconditions.checkNotNull(r5);
    }

    public Object getEvent() {
        return this.event;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public Object getSubscriber() {
        return this.subscriber;
    }

    public Method getSubscriberMethod() {
        return this.subscriberMethod;
    }
}
