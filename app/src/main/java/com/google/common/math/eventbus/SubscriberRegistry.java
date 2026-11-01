package com.google.common.eventbus;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class SubscriberRegistry {
    private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = null;
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = null;
    private final EventBus bus;
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers;

    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method r2) {
            this.name = r2.getName();
            this.parameterTypes = Arrays.asList(r2.getParameterTypes());
        }

        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof MethodIdentifier) == false) goto L10;
            MethodIdentifier r0 = (MethodIdentifier) r5;
            if (this.name.equals(r0.name) == true) goto L7;
            return false;
        L7:
            if (this.parameterTypes.equals(r0.parameterTypes) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.name, this.parameterTypes});
        }
    }

    static {
        subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new C07881());
        flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new C07892());
    }

    SubscriberRegistry(EventBus r2) {
        this.subscribers = Maps.newConcurrentMap();
        this.bus = (EventBus) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ ImmutableList access$000(Class r1) {
        return getAnnotatedMethodsNotCached(r1);
    }

    private Multimap<Class<?>, Subscriber> findAllSubscribers(Object r8) {
        HashMultimap r0 = HashMultimap.create();
        UnmodifiableIterator<Method> r2 = getAnnotatedMethods(r8.getClass()).iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        Method r3 = r2.next();
        r0.put(r3.getParameterTypes()[0], Subscriber.create(this.bus, r8, r3));
        goto L4
    L6:
        return r0;
    }

    static ImmutableSet<Class<?>> flattenHierarchy(Class<?> r2) {
        return flattenHierarchyCache.getUnchecked(r2);
    L4:
        e = move-exception;
        throw Throwables.propagate(e.getCause());
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> r2) {
        return subscriberMethodsCache.getUnchecked(r2);
    L4:
        e = move-exception;
        Throwables.throwIfUnchecked(e.getCause());
        throw e;
    }

    private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> r14) {
        Set r0 = TypeToken.m375of(r14).getTypes().rawTypes();
        HashMap r1 = Maps.newHashMap();
        Iterator r2 = r0.iterator();
    L4:
        if (r2.hasNext() == false) goto L21;
        Method[] r4 = ((Class) r2.next()).getDeclaredMethods();
        int r5 = r4.length;
        int r7 = 0;
    L6:
        if (r7 >= r5) goto L4;
        Method r8 = r4[r7];
        if (r8.isAnnotationPresent(Subscribe.class) == true) goto L10;
    L18:
        r7 = r7 + 1;
        goto L6
    L10:
        if (r8.isSynthetic() == true) goto L18;
        Class<?>[] r9 = r8.getParameterTypes();
        if (r9.length != 1) goto L14;
        boolean r10 = true;
    L15:
        Preconditions.checkArgument(r10, "Method %s has @Subscribe annotation but has %s parameters. Subscriber methods must have exactly 1 parameter.", r8, r9.length);
        Preconditions.checkArgument(!r9[0].isPrimitive(), "@Subscribe method %s's parameter is %s. Subscriber methods cannot accept primitives. Consider changing the parameter to %s.", r8, r9[0].getName(), Primitives.wrap(r9[0]).getSimpleName());
        MethodIdentifier r11 = new MethodIdentifier(r8);
        if (r1.containsKey(r11) == true) goto L18;
        r1.put(r11, r8);
        goto L18
    L14:
        r10 = false;
        goto L15
    L21:
        return ImmutableList.copyOf(r1.values());
    }

    Iterator<Subscriber> getSubscribers(Object r7) {
        ImmutableSet<Class<?>> r0 = flattenHierarchy(r7.getClass());
        ArrayList r1 = Lists.newArrayListWithCapacity(r0.size());
        UnmodifiableIterator<Class<?>> r2 = r0.iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        Class<?> r3 = r2.next();
        CopyOnWriteArraySet<Subscriber> r4 = this.subscribers.get(r3);
        if (r4 == null) goto L4;
        r1.add(r4.iterator());
        goto L4
    L10:
        return Iterators.concat(r1.iterator());
    }

    Set<Subscriber> getSubscribersForTesting(Class<?> r3) {
        return (Set) MoreObjects.firstNonNull(this.subscribers.get(r3), ImmutableSet.m259of());
    }

    void register(Object r9) {
        Iterator<Map.Entry<Class<?>, Collection<Subscriber>>> r1 = findAllSubscribers(r9).asMap().entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        Map.Entry<Class<?>, Collection<Subscriber>> r2 = r1.next();
        Class<?> r3 = r2.getKey();
        Collection<Subscriber> r4 = r2.getValue();
        CopyOnWriteArraySet<Subscriber> r5 = this.subscribers.get(r3);
        if (r5 != null) goto L8;
        CopyOnWriteArraySet<Subscriber> r6 = new CopyOnWriteArraySet();
        r5 = (CopyOnWriteArraySet) MoreObjects.firstNonNull(this.subscribers.putIfAbsent(r3, r6), r6);
    L8:
        r5.addAll(r4);
        goto L4
    }

    void unregister(Object r9) {
        Iterator<Map.Entry<Class<?>, Collection<Subscriber>>> r1 = findAllSubscribers(r9).asMap().entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L12;
        Map.Entry<Class<?>, Collection<Subscriber>> r2 = r1.next();
        Class<?> r3 = r2.getKey();
        Collection<Subscriber> r4 = r2.getValue();
        CopyOnWriteArraySet<Subscriber> r5 = this.subscribers.get(r3);
        if (r5 == null) goto L11;
        if (r5.removeAll(r4) == true) goto L4;
    L11:
        throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + r9 + " registered?");
    }
}
