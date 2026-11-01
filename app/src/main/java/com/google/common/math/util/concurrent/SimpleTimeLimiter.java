package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class SimpleTimeLimiter implements TimeLimiter {
    private final ExecutorService executor;

    /* JADX INFO: renamed from: com.google.common.util.concurrent.SimpleTimeLimiter$1 */
    class C09621 implements InvocationHandler {
        final /* synthetic */ SimpleTimeLimiter this$0;
        final /* synthetic */ Set val$interruptibleMethods;
        final /* synthetic */ Object val$target;
        final /* synthetic */ long val$timeoutDuration;
        final /* synthetic */ TimeUnit val$timeoutUnit;

        C09621(SimpleTimeLimiter r1, Object r2, long r3, TimeUnit r5, Set r6) {
            this.val$target = r2;
            this.val$timeoutDuration = r3;
            this.val$timeoutUnit = r5;
            this.val$interruptibleMethods = r6;
            this.this$0 = r1;
        }

        static /* synthetic */ Object lambda$invoke$0(Method r2, Object r3, Object[] r4) throws Exception {
            return r2.invoke(r3, r4);
        L4:
            e = move-exception;
            throw SimpleTimeLimiter.access$100(e, false);
        }

        @Override // java.lang.reflect.InvocationHandler
        @CheckForNull
        public Object invoke(Object r9, final Method r10, @CheckForNull final Object[] r11) throws Throwable {
            final Object r0 = this.val$target;
            Callable r1 = new SimpleTimeLimiter$1$$ExternalSyntheticLambda0(r10, r0, r11);
            return SimpleTimeLimiter.access$000(this.this$0, r1, this.val$timeoutDuration, this.val$timeoutUnit, this.val$interruptibleMethods.contains(r10));
        }
    }

    private SimpleTimeLimiter(ExecutorService r2) {
        this.executor = (ExecutorService) Preconditions.checkNotNull(r2);
    }

    static /* synthetic */ Object access$000(SimpleTimeLimiter r1, Callable r2, long r3, TimeUnit r5, boolean r6) throws Exception {
        return r1.callWithTimeout(r2, r3, r5, r6);
    }

    static /* synthetic */ Exception access$100(Exception r1, boolean r2) throws Exception {
        return throwCause(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @ParametricNullness
    private <T> T callWithTimeout(Callable<T> r4, long r5, TimeUnit r7, boolean r8) throws Exception {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r7);
        checkPositiveTimeout(r5);
        Future<T> r0 = this.executor.submit(r4);
        boolean r1 = (T) (true ? 1 : 0);
        if (r8 == false) goto L5;
        r1 = r0.get(r5, r7);     // Catch: TimeoutException -> L7 ExecutionException -> L10 InterruptedException -> L13
    L16:
        return (T) r1;
    L5:
        r1 = (T) Uninterruptibles.getUninterruptibly(r0, r5, r7);
    L13:
        e = move-exception;
        r0.cancel(r1);
        throw e;
    L10:
        e = move-exception;
        throw throwCause(e, r1);
    L7:
        e = move-exception;
        r0.cancel(r1);
        throw new UncheckedTimeoutException(e);
    }

    private static void checkPositiveTimeout(long r2) {
        if (r2 <= 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "timeout must be positive: %s", r2);
        return;
    L5:
        r0 = false;
        goto L6
    }

    public static SimpleTimeLimiter create(ExecutorService r1) {
        return new SimpleTimeLimiter(r1);
    }

    private static boolean declaresInterruptedEx(Method r6) {
        Class<?>[] r0 = r6.getExceptionTypes();
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L9;
        if (r0[r3] == InterruptedException.class) goto L6;
        r3 = r3 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    private static Set<Method> findInterruptibleMethods(Class<?> r6) {
        HashSet r0 = Sets.newHashSet();
        Method[] r1 = r6.getMethods();
        int r2 = r1.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L8;
        Method r4 = r1[r3];
        if (declaresInterruptedEx(r4) == false) goto L7;
        r0.add(r4);
    L7:
        r3 = r3 + 1;
        goto L3
    L8:
        return r0;
    }

    private static <T> T newProxy(Class<T> r3, InvocationHandler r4) {
        return r3.cast(Proxy.newProxyInstance(r3.getClassLoader(), new Class[]{r3}, r4));
    }

    private static Exception throwCause(Exception r4, boolean r5) throws Exception {
        Throwable r0 = r4.getCause();
        if (r0 == null) goto L15;
        if (r5 == false) goto L7;
        r0.setStackTrace((StackTraceElement[]) ObjectArrays.concat(r0.getStackTrace(), r4.getStackTrace(), StackTraceElement.class));
    L7:
        if ((r0 instanceof Exception) == true) goto L14;
        if ((r0 instanceof Error) == true) goto L11;
        throw r4;
    L11:
        throw ((Error) r0);
    L14:
        throw ((Exception) r0);
    L15:
        throw r4;
    }

    private void wrapAndThrowExecutionExceptionOrError(Throwable r3) throws ExecutionException {
        if ((r3 instanceof Error) == true) goto L11;
        if ((r3 instanceof RuntimeException) == false) goto L9;
        throw new UncheckedExecutionException(r3);
    L9:
        throw new ExecutionException(r3);
    L11:
        throw new ExecutionError((Error) r3);
    }

    private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable r3) {
        if ((r3 instanceof Error) == false) goto L7;
        throw new ExecutionError((Error) r3);
    L7:
        throw new UncheckedExecutionException(r3);
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    @ParametricNullness
    public <T> T callUninterruptiblyWithTimeout(Callable<T> r4, long r5, TimeUnit r7) throws TimeoutException, ExecutionException {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r7);
        checkPositiveTimeout(r5);
        Future<T> r0 = this.executor.submit(r4);
        return (T) Uninterruptibles.getUninterruptibly(r0, r5, r7);
    L5:
        e = move-exception;
        wrapAndThrowExecutionExceptionOrError(e.getCause());
        throw new AssertionError();
    L8:
        e = move-exception;
        r0.cancel(true);
        throw e;
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    @ParametricNullness
    public <T> T callWithTimeout(Callable<T> r4, long r5, TimeUnit r7) throws TimeoutException, InterruptedException, ExecutionException {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r7);
        checkPositiveTimeout(r5);
        Future<T> r0 = this.executor.submit(r4);
        return r0.get(r5, r7);
    L10:
        e = e;
    L11:
        r0.cancel(true);
        throw e;
    L5:
        e = move-exception;
        wrapAndThrowExecutionExceptionOrError(e.getCause());
        throw new AssertionError();
    L8:
        e = e;
        goto L11
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public <T> T newProxy(T r10, Class<T> r11, long r12, TimeUnit r14) {
        Preconditions.checkNotNull(r10);
        Preconditions.checkNotNull(r11);
        Preconditions.checkNotNull(r14);
        checkPositiveTimeout(r12);
        Preconditions.checkArgument(r11.isInterface(), "interfaceType must be an interface type");
        return (T) newProxy(r11, new C09621(this, r10, r12, r14, findInterruptibleMethods(r11)));
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runUninterruptiblyWithTimeout(Runnable r4, long r5, TimeUnit r7) throws TimeoutException {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r7);
        checkPositiveTimeout(r5);
        Future<?> r0 = this.executor.submit(r4);
        Uninterruptibles.getUninterruptibly(r0, r5, r7);     // Catch: ExecutionException -> L5 TimeoutException -> L8
        return;
    L5:
        e = move-exception;
        wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
        throw new AssertionError();
    L8:
        e = move-exception;
        r0.cancel(true);
        throw e;
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runWithTimeout(Runnable r4, long r5, TimeUnit r7) throws TimeoutException, InterruptedException {
        Preconditions.checkNotNull(r4);
        Preconditions.checkNotNull(r7);
        checkPositiveTimeout(r5);
        Future<?> r0 = this.executor.submit(r4);
        r0.get(r5, r7);     // Catch: ExecutionException -> L5 TimeoutException -> L8 InterruptedException -> L10
        return;
    L10:
        e = e;
    L11:
        r0.cancel(true);
        throw e;
    L5:
        e = move-exception;
        wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
        throw new AssertionError();
    L8:
        e = e;
        goto L11
    }
}
