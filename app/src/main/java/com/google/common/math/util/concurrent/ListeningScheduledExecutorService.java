package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface ListeningScheduledExecutorService extends ScheduledExecutorService, ListeningExecutorService {

    /* JADX INFO: renamed from: com.google.common.util.concurrent.ListeningScheduledExecutorService$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ ScheduledFuture $default$schedule(ListeningScheduledExecutorService r0, Runnable r1, long r2, TimeUnit r4) {
            return r0.schedule(r1, r2, r4);
        }

        public static /* bridge */ /* synthetic */ ScheduledFuture $default$schedule(ListeningScheduledExecutorService r0, Callable r1, long r2, TimeUnit r4) {
            return r0.schedule(r1, r2, r4);
        }

        public static /* bridge */ /* synthetic */ ScheduledFuture $default$scheduleAtFixedRate(ListeningScheduledExecutorService r0, Runnable r1, long r2, long r4, TimeUnit r6) {
            return r0.scheduleAtFixedRate(r1, r2, r4, r6);
        }

        public static /* bridge */ /* synthetic */ ScheduledFuture $default$scheduleWithFixedDelay(ListeningScheduledExecutorService r0, Runnable r1, long r2, long r4, TimeUnit r6) {
            return r0.scheduleWithFixedDelay(r1, r2, r4, r6);
        }
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    ListenableScheduledFuture<?> schedule(Runnable r1, long r2, TimeUnit r4);

    @Override // java.util.concurrent.ScheduledExecutorService
    <V> ListenableScheduledFuture<V> schedule(Callable<V> r1, long r2, TimeUnit r4);

    @Override // java.util.concurrent.ScheduledExecutorService
    ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable r1, long r2, long r4, TimeUnit r6);

    @Override // java.util.concurrent.ScheduledExecutorService
    ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable r1, long r2, long r4, TimeUnit r6);
}
