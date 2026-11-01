package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Uninterruptibles {
    private Uninterruptibles() {
    }

    public static void awaitTerminationUninterruptibly(ExecutorService r3) {
        Verify.verify(awaitTerminationUninterruptibly(r3, Long.MAX_VALUE, TimeUnit.NANOSECONDS));
    }

    public static boolean awaitTerminationUninterruptibly(ExecutorService r8, long r9, TimeUnit r11) {
        boolean r0 = false;
        long r1 = r11.toNanos(r9);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        boolean r5 = r8.awaitTermination(r1, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L8;
        Thread.currentThread().interrupt();
    L8:
        return r5;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    public static void awaitUninterruptibly(CountDownLatch r3) {
        boolean r0 = false;
    L13:
        r3.await();     // Catch: Throwable -> L7 InterruptedException -> L11
        goto L12
    L4:
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
        return;
    L16:
        return;
    L7:
        th = move-exception;
        if (r0 == false) goto L10;
        Thread.currentThread().interrupt();
    L10:
        throw th;
    L12:
        r0 = true;
        goto L13
    }

    public static boolean awaitUninterruptibly(CountDownLatch r8, long r9, TimeUnit r11) {
        boolean r0 = false;
        long r1 = r11.toNanos(r9);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        boolean r5 = r8.await(r1, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L8;
        Thread.currentThread().interrupt();
    L8:
        return r5;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    public static boolean awaitUninterruptibly(Condition r8, long r9, TimeUnit r11) {
        boolean r0 = false;
        long r1 = r11.toNanos(r9);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        boolean r5 = r8.await(r1, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L8;
        Thread.currentThread().interrupt();
    L8:
        return r5;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    @ParametricNullness
    public static <V> V getUninterruptibly(Future<V> r3) throws ExecutionException {
        boolean r0 = false;
    L13:
        V r1 = r3.get();     // Catch: Throwable -> L7 InterruptedException -> L11
        goto L12
    L4:
        if (r0 == false) goto L6;
        Thread.currentThread().interrupt();
    L6:
        return r1;
    L7:
        th = move-exception;
        if (r0 == false) goto L10;
        Thread.currentThread().interrupt();
    L10:
        throw th;
    L12:
        r0 = true;
        goto L13
    }

    @ParametricNullness
    public static <V> V getUninterruptibly(Future<V> r8, long r9, TimeUnit r11) throws ExecutionException, TimeoutException {
        boolean r0 = false;
        long r1 = r11.toNanos(r9);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        V r5 = r8.get(r1, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L8;
        Thread.currentThread().interrupt();
    L8:
        return r5;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    public static void joinUninterruptibly(Thread r3) {
        boolean r0 = false;
    L13:
        r3.join();     // Catch: Throwable -> L7 InterruptedException -> L11
        goto L12
    L4:
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
        return;
    L16:
        return;
    L7:
        th = move-exception;
        if (r0 == false) goto L10;
        Thread.currentThread().interrupt();
    L10:
        throw th;
    L12:
        r0 = true;
        goto L13
    }

    public static void joinUninterruptibly(Thread r8, long r9, TimeUnit r11) {
        Preconditions.checkNotNull(r8);
        boolean r0 = false;
        long r1 = r11.toNanos(r9);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        TimeUnit.NANOSECONDS.timedJoin(r8, r1);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L22;
        Thread.currentThread().interrupt();
        return;
    L22:
        return;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    public static <E> void putUninterruptibly(BlockingQueue<E> r3, E r4) {
        boolean r0 = false;
    L13:
        r3.put(r4);     // Catch: Throwable -> L7 InterruptedException -> L11
        goto L12
    L4:
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
        return;
    L16:
        return;
    L7:
        th = move-exception;
        if (r0 == false) goto L10;
        Thread.currentThread().interrupt();
    L10:
        throw th;
    L12:
        r0 = true;
        goto L13
    }

    public static void sleepUninterruptibly(long r8, TimeUnit r10) {
        boolean r0 = false;
        long r1 = r10.toNanos(r8);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        TimeUnit.NANOSECONDS.sleep(r1);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L22;
        Thread.currentThread().interrupt();
        return;
    L22:
        return;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    public static <E> E takeUninterruptibly(BlockingQueue<E> r3) {
        boolean r0 = false;
    L13:
        E r1 = r3.take();     // Catch: Throwable -> L7 InterruptedException -> L11
        goto L12
    L4:
        if (r0 == false) goto L6;
        Thread.currentThread().interrupt();
    L6:
        return r1;
    L7:
        th = move-exception;
        if (r0 == false) goto L10;
        Thread.currentThread().interrupt();
    L10:
        throw th;
    L12:
        r0 = true;
        goto L13
    }

    public static boolean tryAcquireUninterruptibly(Semaphore r8, int r9, long r10, TimeUnit r12) {
        boolean r0 = false;
        long r1 = r12.toNanos(r10);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        boolean r5 = r8.tryAcquire(r9, r1, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L8;
        Thread.currentThread().interrupt();
    L8:
        return r5;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }

    public static boolean tryAcquireUninterruptibly(Semaphore r1, long r2, TimeUnit r4) {
        return tryAcquireUninterruptibly(r1, 1, r2, r4);
    }

    public static boolean tryLockUninterruptibly(Lock r8, long r9, TimeUnit r11) {
        boolean r0 = false;
        long r1 = r11.toNanos(r9);     // Catch: Throwable -> L13
        long r3 = System.nanoTime() + r1;
    L19:
        boolean r5 = r8.tryLock(r1, TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L9 Throwable -> L13
        goto L10
    L6:
        if (r0 == false) goto L8;
        Thread.currentThread().interrupt();
    L8:
        return r5;
    L10:
        r0 = true;
        r1 = r3 - System.nanoTime();
    L13:
        th = move-exception;
        if (r0 == false) goto L16;
        Thread.currentThread().interrupt();
    L16:
        throw th;
    }
}
