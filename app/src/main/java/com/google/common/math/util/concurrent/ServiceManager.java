package com.google.common.util.concurrent;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class ServiceManager implements ServiceManagerBridge {
    private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = null;
    private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = null;
    private static final LazyLogger logger = null;
    private final ImmutableList<Service> services;
    private final ServiceManagerState state;

    private static final class EmptyServiceManagerWarning extends Throwable {
        private EmptyServiceManagerWarning() {
        }

        /* synthetic */ EmptyServiceManagerWarning(C09581 r1) {
            this();
        }
    }

    private static final class FailedService extends Throwable {
        FailedService(Service r4) {
            super(r4.toString(), r4.failureCause(), false, false);
        }
    }

    public static abstract class Listener {
        public Listener() {
        }

        public void failure(Service r1) {
        }

        public void healthy() {
        }

        public void stopped() {
        }
    }

    private static final class NoOpService extends AbstractService {
        private NoOpService() {
        }

        /* synthetic */ NoOpService(C09581 r1) {
            this();
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected void doStart() {
            notifyStarted();
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected void doStop() {
            notifyStopped();
        }
    }

    private static final class ServiceListener extends Service.Listener {
        final Service service;
        final WeakReference<ServiceManagerState> state;

        ServiceListener(Service r1, WeakReference<ServiceManagerState> r2) {
            this.service = r1;
            this.state = r2;
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void failed(Service.State r7, Throwable r8) {
            ServiceManagerState r0 = this.state.get();
            if (r0 == null) goto L13;
            boolean r2 = true;
            boolean r1 = !(this.service instanceof NoOpService);
            if (r7 != Service.State.STARTING) goto L9;
            r2 = false;
        L9:
            if ((r1 & r2) == false) goto L11;
            ServiceManager.access$200().get().log(Level.SEVERE, "Service " + this.service + " has failed in the " + r7 + " state.", r8);
        L11:
            r0.transitionService(this.service, r7, Service.State.FAILED);
            return;
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void running() {
            ServiceManagerState r0 = this.state.get();
            if (r0 == null) goto L6;
            r0.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
            return;
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void starting() {
            ServiceManagerState r0 = this.state.get();
            if (r0 == null) goto L8;
            r0.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
            if ((this.service instanceof NoOpService) == true) goto L9;
            ServiceManager.access$200().get().log(Level.FINE, "Starting {0}.", this.service);
            return;
        L9:
            return;
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void stopping(Service.State r4) {
            ServiceManagerState r0 = this.state.get();
            if (r0 == null) goto L6;
            r0.transitionService(this.service, r4, Service.State.STOPPING);
            return;
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void terminated(Service.State r7) {
            ServiceManagerState r0 = this.state.get();
            if (r0 != null) goto L5;
            return;
        L5:
            if ((this.service instanceof NoOpService) == true) goto L7;
            ServiceManager.access$200().get().log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[]{this.service, r7});
        L7:
            r0.transitionService(this.service, r7, Service.State.TERMINATED);
        }
    }

    private static final class ServiceManagerState {
        final Monitor.Guard awaitHealthGuard;
        final ListenerCallQueue<Listener> listeners;
        final Monitor monitor;
        final int numberOfServices;
        boolean ready;
        final SetMultimap<Service.State, Service> servicesByState;
        final IdentityHashMap<Service, Stopwatch> startupTimers;
        final Multiset<Service.State> states;
        final Monitor.Guard stoppedGuard;
        boolean transitioned;

        final class AwaitHealthGuard extends Monitor.Guard {
            final /* synthetic */ ServiceManagerState this$0;

            AwaitHealthGuard(ServiceManagerState r2) {
                this.this$0 = r2;
                super(r2.monitor);
            }

            @Override // com.google.common.util.concurrent.Monitor.Guard
            public boolean isSatisfied() {
                if (this.this$0.states.count(Service.State.RUNNING) != this.this$0.numberOfServices) goto L5;
            L12:
                return true;
            L5:
                if (this.this$0.states.contains(Service.State.STOPPING) == true) goto L12;
                if (this.this$0.states.contains(Service.State.TERMINATED) == true) goto L12;
                if (this.this$0.states.contains(Service.State.FAILED) == true) goto L12;
                return false;
            }
        }

        final class StoppedGuard extends Monitor.Guard {
            final /* synthetic */ ServiceManagerState this$0;

            StoppedGuard(ServiceManagerState r2) {
                this.this$0 = r2;
                super(r2.monitor);
            }

            @Override // com.google.common.util.concurrent.Monitor.Guard
            public boolean isSatisfied() {
                if ((this.this$0.states.count(Service.State.TERMINATED) + this.this$0.states.count(Service.State.FAILED)) != this.this$0.numberOfServices) goto L5;
                return true;
            L5:
                return false;
            }
        }

        ServiceManagerState(ImmutableCollection<Service> r3) {
            this.monitor = new Monitor();
            this.servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
            this.states = this.servicesByState.keys();
            this.startupTimers = new IdentityHashMap();
            this.awaitHealthGuard = new AwaitHealthGuard(this);
            this.stoppedGuard = new StoppedGuard(this);
            this.listeners = new ListenerCallQueue();
            this.numberOfServices = r3.size();
            this.servicesByState.putAll(Service.State.NEW, r3);
        }

        void addListener(Listener r2, Executor r3) {
            this.listeners.addListener(r2, r3);
        }

        void awaitHealthy() {
            this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
            checkHealthy();     // Catch: Throwable -> L6
            this.monitor.leave();
            return;
        L6:
            th = move-exception;
            this.monitor.leave();
            throw th;
        }

        void awaitHealthy(long r6, TimeUnit r8) throws TimeoutException {
            this.monitor.enter();
        L10:
            th = move-exception;
            this.monitor.leave();
            throw th;
        L4:
            if (this.monitor.waitForUninterruptibly(this.awaitHealthGuard, r6, r8) == false) goto L9;
            checkHealthy();     // Catch: Throwable -> L10
            this.monitor.leave();
            return;
        L9:
            throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(this.servicesByState, Predicates.m186in(ImmutableSet.m261of(Service.State.NEW, Service.State.STARTING))));     // Catch: Throwable -> L10
        }

        void awaitStopped() {
            this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
            this.monitor.leave();
        }

        void awaitStopped(long r6, TimeUnit r8) throws TimeoutException {
            this.monitor.enter();
        L9:
            th = move-exception;
            this.monitor.leave();
            throw th;
        L4:
            if (this.monitor.waitForUninterruptibly(this.stoppedGuard, r6, r8) == false) goto L8;
            this.monitor.leave();
            return;
        L8:
            throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.m186in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));     // Catch: Throwable -> L9
        }

        void checkHealthy() {
            if (this.states.count(Service.State.RUNNING) == this.numberOfServices) goto L9;
            IllegalStateException r0 = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
            Iterator<Service> r1 = this.servicesByState.get(Service.State.FAILED).iterator();
        L6:
            if (r1.hasNext() == false) goto L8;
            r0.addSuppressed(new FailedService(r1.next()));
            goto L6
        L8:
            throw r0;
        }

        void dispatchListenerEvents() {
            Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
            this.listeners.dispatch();
        }

        void enqueueFailedEvent(final Service r3) {
            this.listeners.enqueue(new C09612(this, r3));
        }

        void enqueueHealthyEvent() {
            this.listeners.enqueue(ServiceManager.access$400());
        }

        void enqueueStoppedEvent() {
            this.listeners.enqueue(ServiceManager.access$300());
        }

        void markReady() {
            this.monitor.enter();
        L17:
            th = move-exception;
            this.monitor.leave();
            throw th;
        L4:
            if (this.transitioned == true) goto L8;
            this.ready = true;     // Catch: Throwable -> L17
            this.monitor.leave();
            return;
        L8:
            ArrayList r0 = Lists.newArrayList();     // Catch: Throwable -> L17
            UnmodifiableIterator<Service> r1 = servicesByState().values().iterator();     // Catch: Throwable -> L17
        L10:
            if (r1.hasNext() == false) goto L16;
            Service r2 = r1.next();     // Catch: Throwable -> L17
            if (r2.state() == Service.State.NEW) goto L10;
            r0.add(r2);     // Catch: Throwable -> L17
            goto L10
        L16:
            throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + r0);     // Catch: Throwable -> L17
        }

        ImmutableSetMultimap<Service.State, Service> servicesByState() {
            ImmutableSetMultimap.Builder r0 = ImmutableSetMultimap.builder();
            this.monitor.enter();
            Iterator<Map.Entry<Service.State, Service>> r1 = this.servicesByState.entries().iterator();     // Catch: Throwable -> L12
        L5:
            if (r1.hasNext() == false) goto L10;
            Map.Entry<Service.State, Service> r2 = r1.next();     // Catch: Throwable -> L12
            if ((r2.getValue() instanceof NoOpService) == true) goto L5;
            r0.put(r2);     // Catch: Throwable -> L12
            goto L5
        L10:
            this.monitor.leave();
            return r0.build();
        L12:
            th = move-exception;
            this.monitor.leave();
            throw th;
        }

        ImmutableMap<Service, Long> startupTimes() {
            this.monitor.enter();
            ArrayList r0 = Lists.newArrayListWithCapacity(this.startupTimers.size());     // Catch: Throwable -> L14
            Iterator<Map.Entry<Service, Stopwatch>> r1 = this.startupTimers.entrySet().iterator();     // Catch: Throwable -> L14
        L5:
            if (r1.hasNext() == false) goto L12;
            Map.Entry<Service, Stopwatch> r2 = r1.next();     // Catch: Throwable -> L14
            Service r3 = r2.getKey();     // Catch: Throwable -> L14
            Stopwatch r4 = r2.getValue();     // Catch: Throwable -> L14
            if (r4.isRunning() == true) goto L5;
            if ((r3 instanceof NoOpService) == true) goto L5;
            r0.add(Maps.immutableEntry(r3, Long.valueOf(r4.elapsed(TimeUnit.MILLISECONDS))));     // Catch: Throwable -> L14
            goto L5
        L12:
            this.monitor.leave();
            Collections.sort(r0, Ordering.natural().onResultOf(new C09601(this)));
            return ImmutableMap.copyOf(r0);
        L14:
            th = move-exception;
            this.monitor.leave();
            throw th;
        }

        void transitionService(Service r8, Service.State r9, Service.State r10) {
            Preconditions.checkNotNull(r8);
            if (r9 == r10) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkArgument(r2);
            this.monitor.enter();
            this.transitioned = true;     // Catch: Throwable -> L32
            if (this.ready == true) goto L11;
            this.monitor.leave();
            dispatchListenerEvents();
            return;
        L11:
            Preconditions.checkState(this.servicesByState.remove(r9, r8), "Service %s not at the expected location in the state map %s", r8, r9);     // Catch: Throwable -> L32
            Preconditions.checkState(this.servicesByState.put(r10, r8), "Service %s in the state map unexpectedly at %s", r8, r10);     // Catch: Throwable -> L32
            Stopwatch r3 = this.startupTimers.get(r8);     // Catch: Throwable -> L32
            if (r3 != null) goto L15;
            r3 = Stopwatch.createStarted();     // Catch: Throwable -> L32
            this.startupTimers.put(r8, r3);     // Catch: Throwable -> L32
        L15:
            if (r10.compareTo(Service.State.RUNNING) < 0) goto L22;
            if (r3.isRunning() == false) goto L22;
            r3.stop();     // Catch: Throwable -> L32
            if ((r8 instanceof NoOpService) == true) goto L22;
            ServiceManager.access$200().get().log(Level.FINE, "Started {0} in {1}.", new Object[]{r8, r3});     // Catch: Throwable -> L32
        L22:
            if (r10 != Service.State.FAILED) goto L25;
            enqueueFailedEvent(r8);     // Catch: Throwable -> L32
        L25:
            if (this.states.count(Service.State.RUNNING) != this.numberOfServices) goto L28;
            enqueueHealthyEvent();     // Catch: Throwable -> L32
        L30:
            this.monitor.leave();
            dispatchListenerEvents();
            return;
        L28:
            if ((this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED)) != this.numberOfServices) goto L30;
            enqueueStoppedEvent();     // Catch: Throwable -> L32
        L32:
            th = move-exception;
            this.monitor.leave();
            dispatchListenerEvents();
            throw th;
        L5:
            r2 = false;
            goto L6
        }

        void tryStartTiming(Service r4) {
            this.monitor.enter();
        L8:
            th = move-exception;
            this.monitor.leave();
            throw th;
        L4:
            if (this.startupTimers.get(r4) != null) goto L6;
            this.startupTimers.put(r4, Stopwatch.createStarted());     // Catch: Throwable -> L8
        L6:
            this.monitor.leave();
        }
    }

    static {
        logger = new LazyLogger(ServiceManager.class);
        HEALTHY_EVENT = new C09581();
        STOPPED_EVENT = new C09592();
    }

    public ServiceManager(Iterable<? extends Service> r7) {
        ImmutableList<Service> r0 = ImmutableList.copyOf(r7);
        if (r0.isEmpty() == false) goto L5;
        C09581 r4 = null;
        logger.get().log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning(r4));
        r0 = ImmutableList.m213of(new NoOpService(r4));
    L5:
        this.state = new ServiceManagerState(r0);
        this.services = r0;
        WeakReference r1 = new WeakReference(this.state);
        UnmodifiableIterator<Service> r2 = r0.iterator();
    L7:
        if (r2.hasNext() == false) goto L13;
        Service r3 = r2.next();
        r3.addListener(new ServiceListener(r3, r1), MoreExecutors.directExecutor());
        if (r3.state() != Service.State.NEW) goto L11;
        boolean r5 = true;
    L12:
        Preconditions.checkArgument(r5, "Can only manage NEW services, %s", r3);
        goto L7
    L11:
        r5 = false;
        goto L12
    L13:
        this.state.markReady();
    }

    static /* synthetic */ LazyLogger access$200() {
        return logger;
    }

    static /* synthetic */ ListenerCallQueue.Event access$300() {
        return STOPPED_EVENT;
    }

    static /* synthetic */ ListenerCallQueue.Event access$400() {
        return HEALTHY_EVENT;
    }

    public void addListener(Listener r2, Executor r3) {
        this.state.addListener(r2, r3);
    }

    public void awaitHealthy() {
        this.state.awaitHealthy();
    }

    public void awaitHealthy(long r2, TimeUnit r4) throws TimeoutException {
        this.state.awaitHealthy(r2, r4);
    }

    public void awaitStopped() {
        this.state.awaitStopped();
    }

    public void awaitStopped(long r2, TimeUnit r4) throws TimeoutException {
        this.state.awaitStopped(r2, r4);
    }

    public boolean isHealthy() {
        UnmodifiableIterator<Service> r0 = this.services.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().isRunning() == true) goto L4;
        return false;
    L10:
        return true;
    }

    @Override // com.google.common.util.concurrent.ServiceManagerBridge
    public /* bridge */ /* synthetic */ ImmutableMultimap servicesByState() {
        return servicesByState();
    }

    @Override // com.google.common.util.concurrent.ServiceManagerBridge
    public ImmutableSetMultimap<Service.State, Service> servicesByState() {
        return this.state.servicesByState();
    }

    public ServiceManager startAsync() {
        UnmodifiableIterator<Service> r0 = this.services.iterator();
    L4:
        if (r0.hasNext() == false) goto L10;
        if (r0.next().state() != Service.State.NEW) goto L8;
        boolean r2 = true;
    L9:
        Preconditions.checkState(r2, "Not all services are NEW, cannot start %s", this);
        goto L4
    L8:
        r2 = false;
        goto L9
    L10:
        UnmodifiableIterator<Service> r1 = this.services.iterator();
    L12:
        if (r1.hasNext() == false) goto L19;
        Service r3 = r1.next();
        this.state.tryStartTiming(r3);     // Catch: IllegalStateException -> L16
        r3.startAsync();     // Catch: IllegalStateException -> L16
    L16:
        e = move-exception;
        logger.get().log(Level.WARNING, "Unable to start Service " + r3, e);
        goto L12
    L19:
        return this;
    }

    public ImmutableMap<Service, Long> startupTimes() {
        return this.state.startupTimes();
    }

    public ServiceManager stopAsync() {
        UnmodifiableIterator<Service> r0 = this.services.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().stopAsync();
        goto L4
    L6:
        return this;
    }

    public String toString() {
        return MoreObjects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
    }
}
