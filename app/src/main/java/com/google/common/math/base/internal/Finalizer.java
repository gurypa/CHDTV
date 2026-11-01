package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
public class Finalizer implements Runnable {
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";

    @CheckForNull
    private static final Constructor<Thread> bigThreadConstructor = null;

    @CheckForNull
    private static final Field inheritableThreadLocals = null;
    private static final Logger logger = null;
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;

    static {
        logger = Logger.getLogger(Finalizer.class.getName());
        bigThreadConstructor = getBigThreadConstructor();
        if (bigThreadConstructor != null) goto L5;
        Field r0 = getInheritableThreadLocalsField();
    L6:
        inheritableThreadLocals = r0;
        return;
    L5:
        r0 = null;
        goto L6
    }

    private Finalizer(Class<?> r2, ReferenceQueue<Object> r3, PhantomReference<Object> r4) {
        this.queue = r3;
        this.finalizableReferenceClassReference = new WeakReference(r2);
        this.frqReference = r4;
    }

    private boolean cleanUp(Reference<?> r5) {
        Method r0 = getFinalizeReferentMethod();
        if (r0 != null) goto L6;
        return false;
    L6:
        if (finalizeReference(r5, r0) == true) goto L8;
        return false;
    L8:
        Reference<? extends Object> r2 = this.queue.poll();
        if (r2 == null) goto L10;
        if (finalizeReference(r2, r0) == true) goto L8;
        return false;
    L10:
        return true;
    }

    private boolean finalizeReference(Reference<?> r5, Method r6) {
        r5.clear();
        if (r5 != this.frqReference) goto L11;
        return false;
    L11:
        r6.invoke(r5, new Object[0]);     // Catch: Throwable -> L7
        return true;
    L7:
        th = move-exception;
        logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
        return true;
    }

    @CheckForNull
    private static Constructor<Thread> getBigThreadConstructor() {
        return Thread.class.getConstructor(new Class[]{ThreadGroup.class, Runnable.class, String.class, Long.TYPE, Boolean.TYPE});
    L5:
        return null;
    }

    @CheckForNull
    private Method getFinalizeReferentMethod() {
        Class<?> r0 = this.finalizableReferenceClassReference.get();
        if (r0 != null) goto L11;
        return null;
    L11:
        return r0.getMethod("finalizeReferent", new Class[0]);
    L8:
        e = move-exception;
        throw new AssertionError(e);
    }

    @CheckForNull
    private static Field getInheritableThreadLocalsField() {
        Field r0 = Thread.class.getDeclaredField("inheritableThreadLocals");     // Catch: Throwable -> L4
        r0.setAccessible(true);     // Catch: Throwable -> L4
        return r0;
    L5:
        logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
        return null;
    }

    public static void startFinalizer(Class<?> r13, ReferenceQueue<Object> r14, PhantomReference<Object> r15) {
        if (r13.getName().equals(FINALIZABLE_REFERENCE) == false) goto L23;
        Finalizer r0 = new Finalizer(r13, r14, r15);
        String r1 = Finalizer.class.getName();
        Thread r2 = null;
        if (bigThreadConstructor != null) goto L24;
    L11:
        if (r2 != null) goto L13;
        ThreadGroup r6 = (ThreadGroup) null;
        r2 = new Thread(null, r0, r1);
    L13:
        r2.setDaemon(true);
    L18:
        th = move-exception;
        logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", th);
    L20:
        r2.start();
        return;
    L15:
        if (inheritableThreadLocals == null) goto L20;
        inheritableThreadLocals.set(r2, null);     // Catch: Throwable -> L18
        goto L20
    L24:
        ThreadGroup r9 = (ThreadGroup) null;     // Catch: Throwable -> L9
        r2 = bigThreadConstructor.newInstance(new Object[]{null, r0, r1, 0L, false});     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        logger.log(Level.INFO, "Failed to create a thread without inherited thread-local values", th);
        goto L11
    L23:
        throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
    }

    @Override // java.lang.Runnable
    public void run() {
    L7:
        if (cleanUp(this.queue.remove()) == true) goto L7;
    }
}
