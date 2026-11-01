package androidx.concurrent.futures;

import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
public final class CallbackToFutureAdapter {

    public static final class Completer<T> {
        private boolean attemptedSetting;
        private ResolvableFuture<Void> cancellationFuture;
        SafeFuture<T> future;
        Object tag;

        Completer() {
            this.cancellationFuture = ResolvableFuture.create();
        }

        private void setCompletedNormally() {
            this.tag = null;
            this.future = null;
            this.cancellationFuture = null;
        }

        public void addCancellationListener(Runnable r2, Executor r3) {
            ResolvableFuture<Void> r0 = this.cancellationFuture;
            if (r0 == null) goto L6;
            r0.addListener(r2, r3);
            return;
        }

        protected void finalize() {
            SafeFuture<T> r0 = this.future;
            if (r0 == null) goto L8;
            if (r0.isDone() == true) goto L8;
            r0.setException(new FutureGarbageCollectedException("The completer object was garbage collected - this future would otherwise never complete. The tag was: " + this.tag));
        L8:
            if (this.attemptedSetting == true) goto L13;
            ResolvableFuture<Void> r1 = this.cancellationFuture;
            if (r1 == null) goto L14;
            r1.set(null);
            return;
        L14:
            return;
        }

        void fireCancellationListeners() {
            this.tag = null;
            this.future = null;
            this.cancellationFuture.set(null);
        }

        public boolean set(T r4) {
            boolean r0 = true;
            this.attemptedSetting = true;
            SafeFuture<T> r1 = this.future;
            if (r1 != null) goto L5;
        L7:
            r0 = false;
        L8:
            if (r0 == false) goto L10;
            setCompletedNormally();
        L10:
            return r0;
        L5:
            if (r1.set(r4) == false) goto L7;
            goto L7
        }

        public boolean setCancelled() {
            boolean r0 = true;
            this.attemptedSetting = true;
            SafeFuture<T> r1 = this.future;
            if (r1 != null) goto L5;
        L7:
            r0 = false;
        L8:
            if (r0 == false) goto L10;
            setCompletedNormally();
        L10:
            return r0;
        L5:
            if (r1.cancelWithoutNotifyingCompleter(true) == false) goto L7;
            goto L7
        }

        public boolean setException(Throwable r4) {
            boolean r0 = true;
            this.attemptedSetting = true;
            SafeFuture<T> r1 = this.future;
            if (r1 != null) goto L5;
        L7:
            r0 = false;
        L8:
            if (r0 == false) goto L10;
            setCompletedNormally();
        L10:
            return r0;
        L5:
            if (r1.setException(r4) == false) goto L7;
            goto L7
        }
    }

    static final class FutureGarbageCollectedException extends Throwable {
        FutureGarbageCollectedException(String r1) {
            super(r1);
        }

        @Override // java.lang.Throwable
        public synchronized Throwable fillInStackTrace() {
            monitor-enter(this);
            monitor-exit(this);
            return this;
        }
    }

    public interface Resolver<T> {
        Object attachCompleter(Completer<T> r1) throws Exception;
    }

    private static final class SafeFuture<T> implements ListenableFuture<T> {
        final WeakReference<Completer<T>> completerWeakReference;
        private final AbstractResolvableFuture<T> delegate;

        SafeFuture(Completer<T> r2) {
            this.delegate = new C00381(this);
            this.completerWeakReference = new WeakReference(r2);
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public void addListener(Runnable r2, Executor r3) {
            this.delegate.addListener(r2, r3);
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean r3) {
            Completer<T> r0 = this.completerWeakReference.get();
            boolean r1 = this.delegate.cancel(r3);
            if (r1 == false) goto L6;
            if (r0 == null) goto L6;
            r0.fireCancellationListeners();
        L6:
            return r1;
        }

        boolean cancelWithoutNotifyingCompleter(boolean r2) {
            return this.delegate.cancel(r2);
        }

        @Override // java.util.concurrent.Future
        public T get() throws InterruptedException, ExecutionException {
            return this.delegate.get();
        }

        @Override // java.util.concurrent.Future
        public T get(long r2, TimeUnit r4) throws InterruptedException, ExecutionException, TimeoutException {
            return this.delegate.get(r2, r4);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return this.delegate.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.delegate.isDone();
        }

        boolean set(T r2) {
            return this.delegate.set(r2);
        }

        boolean setException(Throwable r2) {
            return this.delegate.setException(r2);
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    private CallbackToFutureAdapter() {
    }

    public static <T> ListenableFuture<T> getFuture(Resolver<T> r3) {
        Completer<T> r0 = new Completer();
        SafeFuture<T> r1 = new SafeFuture(r0);
        r0.future = r1;
        r0.tag = r3.getClass();
        Object r2 = r3.attachCompleter(r0);     // Catch: Exception -> L7
        if (r2 == null) goto L9;
        r0.tag = r2;     // Catch: Exception -> L7
    L9:
        return r1;
    L7:
        e = move-exception;
        r1.setException(e);
        goto L9
    }
}
