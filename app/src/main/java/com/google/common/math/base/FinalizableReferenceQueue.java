package com.google.common.base;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class FinalizableReferenceQueue implements Closeable {
    private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
    private static final Logger logger = null;
    private static final Method startFinalizer = null;
    final PhantomReference<Object> frqRef;
    final ReferenceQueue<Object> queue;
    final boolean threadStarted;

    static class DecoupledLoader implements FinalizerLoader {
        private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";

        DecoupledLoader() {
        }

        URL getBaseUrl() throws IOException {
            String r0 = FinalizableReferenceQueue.FINALIZER_CLASS_NAME.replace('.', '/') + ".class";
            URL r1 = getClass().getClassLoader().getResource(r0);
            if (r1 == null) goto L11;
            String r2 = r1.toString();
            if (r2.endsWith(r0) == false) goto L9;
            return new URL(r1, r2.substring(0, r2.length() - r0.length()));
        L9:
            throw new IOException("Unsupported path style: " + r2);
        L11:
            throw new FileNotFoundException(r0);
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        @CheckForNull
        public Class<?> loadFinalizer() {
            return newLoader(getBaseUrl()).loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
        L4:
            e = move-exception;
            FinalizableReferenceQueue.access$000().log(Level.WARNING, LOADING_ERROR, e);
            return null;
        }

        URLClassLoader newLoader(URL r4) {
            return new URLClassLoader(new URL[]{r4}, null);
        }
    }

    static class DirectLoader implements FinalizerLoader {
        DirectLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        public Class<?> loadFinalizer() {
            return Class.forName(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
        L4:
            e = move-exception;
            throw new AssertionError(e);
        }
    }

    interface FinalizerLoader {
        @CheckForNull
        Class<?> loadFinalizer();
    }

    static class SystemLoader implements FinalizerLoader {
        static boolean disabled;

        SystemLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        @CheckForNull
        public Class<?> loadFinalizer() {
            if (disabled == false) goto L17;
            return null;
        L17:
            ClassLoader r0 = ClassLoader.getSystemClassLoader();     // Catch: SecurityException -> L12
            if (r0 != null) goto L15;
            return null;
        L15:
            return r0.loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
        L10:
            return null;
        L13:
            FinalizableReferenceQueue.access$000().info("Not allowed to access system class loader.");
            return null;
        }
    }

    static {
        logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
        startFinalizer = getStartFinalizer(loadFinalizer(new FinalizerLoader[]{new SystemLoader(), new DecoupledLoader(), new DirectLoader()}));
    }

    public FinalizableReferenceQueue() {
        this.queue = new ReferenceQueue();
        this.frqRef = new PhantomReference(this, this.queue);
        boolean r0 = false;
        startFinalizer.invoke(null, new Object[]{FinalizableReference.class, this.queue, this.frqRef});     // Catch: Throwable -> L5 IllegalAccessException -> L9
        r0 = true;
    L7:
        this.threadStarted = r0;
        return;
    L9:
        e = move-exception;
        throw new AssertionError(e);
    L5:
        th = move-exception;
        logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", th);
        goto L7
    }

    static /* synthetic */ Logger access$000() {
        return logger;
    }

    static Method getStartFinalizer(Class<?> r4) {
        return r4.getMethod("startFinalizer", new Class[]{Class.class, ReferenceQueue.class, PhantomReference.class});
    L4:
        e = move-exception;
        throw new AssertionError(e);
    }

    private static Class<?> loadFinalizer(FinalizerLoader... r4) {
        int r0 = r4.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L9;
        Class<?> r3 = r4[r1].loadFinalizer();
        if (r3 != null) goto L6;
        r1 = r1 + 1;
        goto L3
    L6:
        return r3;
    L9:
        throw new AssertionError();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void cleanUp() {
        if (this.threadStarted == false) goto L5;
        return;
    L5:
        Reference<? extends Object> r0 = this.queue.poll();
        if (r0 == 0) goto L13;
        r0.clear();
        ((FinalizableReference) r0).finalizeReferent();     // Catch: Throwable -> L10
    L10:
        th = move-exception;
        logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
        goto L5
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.frqRef.enqueue();
        cleanUp();
    }
}
