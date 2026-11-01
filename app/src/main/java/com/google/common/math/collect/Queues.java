package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Queues {
    private Queues() {
    }

    public static <E> int drain(BlockingQueue<E> r6, Collection<? super E> r7, int r8, long r9, TimeUnit r11) throws InterruptedException {
        Preconditions.checkNotNull(r7);
        long r0 = System.nanoTime() + r11.toNanos(r9);
        int r2 = 0;
    L3:
        if (r2 >= r8) goto L10;
        r2 = r2 + r6.drainTo(r7, r8 - r2);
        if (r2 >= r8) goto L3;
        E r3 = r6.poll(r0 - System.nanoTime(), TimeUnit.NANOSECONDS);
        if (r3 == null) goto L10;
        r7.add(r3);
        r2 = r2 + 1;
    L10:
        return r2;
    }

    public static <E> int drainUninterruptibly(BlockingQueue<E> r7, Collection<? super E> r8, int r9, long r10, TimeUnit r12) {
        Preconditions.checkNotNull(r8);
        long r0 = System.nanoTime() + r12.toNanos(r10);
        int r2 = 0;
        boolean r3 = false;
    L3:
        if (r2 >= r9) goto L19;
    L15:
        th = move-exception;
        if (r3 == false) goto L18;
        Thread.currentThread().interrupt();
    L18:
        throw th;
    L6:
        r2 = r2 + r7.drainTo(r8, r9 - r2);
        if (r2 >= r9) goto L3;
    L24:
        E r4 = r7.poll(r0 - System.nanoTime(), TimeUnit.NANOSECONDS);     // Catch: InterruptedException -> L13 Throwable -> L15
        if (r4 == null) goto L19;
        r8.add(r4);     // Catch: Throwable -> L15
        r2 = r2 + 1;
    L14:
        r3 = true;
    L19:
        if (r3 == false) goto L21;
        Thread.currentThread().interrupt();
    L21:
        return r2;
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int r1) {
        return new ArrayBlockingQueue(r1);
    }

    public static <E> ArrayDeque<E> newArrayDeque() {
        return new ArrayDeque();
    }

    public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        ArrayDeque<E> r0 = new ArrayDeque();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new ArrayDeque((Collection) r2);
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue();
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        ConcurrentLinkedQueue<E> r0 = new ConcurrentLinkedQueue();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new ConcurrentLinkedQueue((Collection) r2);
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
        return new LinkedBlockingDeque();
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int r1) {
        return new LinkedBlockingDeque(r1);
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        LinkedBlockingDeque<E> r0 = new LinkedBlockingDeque();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new LinkedBlockingDeque((Collection) r2);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue();
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int r1) {
        return new LinkedBlockingQueue(r1);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        LinkedBlockingQueue<E> r0 = new LinkedBlockingQueue();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new LinkedBlockingQueue((Collection) r2);
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue();
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        PriorityBlockingQueue<E> r0 = new PriorityBlockingQueue();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new PriorityBlockingQueue((Collection) r2);
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue();
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> r2) {
        if ((r2 instanceof Collection) == true) goto L5;
        PriorityQueue<E> r0 = new PriorityQueue();
        Iterables.addAll(r0, r2);
        return r0;
    L5:
        return new PriorityQueue((Collection) r2);
    }

    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return new SynchronousQueue();
    }

    public static <E> Deque<E> synchronizedDeque(Deque<E> r1) {
        return Synchronized.deque(r1, null);
    }

    public static <E> Queue<E> synchronizedQueue(Queue<E> r1) {
        return Synchronized.queue(r1, null);
    }
}
