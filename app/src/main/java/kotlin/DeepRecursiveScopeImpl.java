package kotlin;

import androidx.exifinterface.media.ExifInterface;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* JADX INFO: compiled from: DeepRecursive.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004BH\u00129\u0010\u0005\u001a5\b\u0001\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\b\u0012\u0006\u0010\t\u001a\u00028\u0000¢\u0006\u0002\u0010\nJ\u0016\u0010\u0015\u001a\u00028\u00012\u0006\u0010\t\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0016J`\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000429\u0010\u0018\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\b2\u000e\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0002¢\u0006\u0002\u0010\u0019J\u001b\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0013H\u0016¢\u0006\u0002\u0010\u001cJ\u000b\u0010\u001d\u001a\u00028\u0001¢\u0006\u0002\u0010\u001eJ2\u0010\u0015\u001a\u0002H\u001f\"\u0004\b\u0002\u0010 \"\u0004\b\u0003\u0010\u001f*\u000e\u0012\u0004\u0012\u0002H \u0012\u0004\u0012\u0002H\u001f0!2\u0006\u0010\t\u001a\u0002H H\u0096@¢\u0006\u0002\u0010\"R\u0018\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fRC\u0010\u0010\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006¢\u0006\u0002\b\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0011R\u001b\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0013X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0014R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006#"}, m421d2 = {"Lkotlin/DeepRecursiveScopeImpl;", ExifInterface.GPS_DIRECTION_TRUE, "R", "Lkotlin/DeepRecursiveScope;", "Lkotlin/coroutines/Continuation;", "block", "Lkotlin/Function3;", "", "Lkotlin/ExtensionFunctionType;", "value", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;)V", "cont", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "function", "Lkotlin/jvm/functions/Function3;", "result", "Lkotlin/Result;", "Ljava/lang/Object;", "callRecursive", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "crossFunctionCompletion", "currentFunction", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "resumeWith", "", "(Ljava/lang/Object;)V", "runCallLoop", "()Ljava/lang/Object;", ExifInterface.LATITUDE_SOUTH, "U", "Lkotlin/DeepRecursiveFunction;", "(Lkotlin/DeepRecursiveFunction;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class DeepRecursiveScopeImpl<T, R> extends DeepRecursiveScope<T, R> implements Continuation<R> {
    private Continuation<Object> cont;
    private Function3<? super DeepRecursiveScope<?, ?>, Object, ? super Continuation<Object>, ? extends Object> function;
    private Object result;
    private Object value;

    /* JADX WARN: Multi-variable type inference failed */
    public DeepRecursiveScopeImpl(Function3<? super DeepRecursiveScope<T, R>, ? super T, ? super Continuation<? super R>, ? extends Object> r2, T r3) {
        Intrinsics.checkNotNullParameter(r2, "block");
        super(null);
        this.function = r2;
        this.value = r3;
        Intrinsics.checkNotNull(this, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        this.cont = this;
        this.result = DeepRecursiveKt.access$getUNDEFINED_RESULT$p();
    }

    public static final /* synthetic */ void access$setCont$p(DeepRecursiveScopeImpl r0, Continuation r1) {
        r0.cont = r1;
    }

    public static final /* synthetic */ void access$setFunction$p(DeepRecursiveScopeImpl r0, Function3 r1) {
        r0.function = r1;
    }

    public static final /* synthetic */ void access$setResult$p(DeepRecursiveScopeImpl r0, Object r1) {
        r0.result = r1;
    }

    private final Continuation<Object> crossFunctionCompletion(final Function3<? super DeepRecursiveScope<?, ?>, Object, ? super Continuation<Object>, ? extends Object> r3, final Continuation<Object> r4) {
        final EmptyCoroutineContext r0 = EmptyCoroutineContext.INSTANCE;
        return new C1039x3ce6e4bb(r0, this, r3, r4);
    }

    @Override // kotlin.DeepRecursiveScope
    public Object callRecursive(T r4, Continuation<? super R> r5) {
        Intrinsics.checkNotNull(r5, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        this.cont = r5;
        this.value = r4;
        Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (r0 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L5;
        DebugProbesKt.probeCoroutineSuspended(r5);
    L5:
        return r0;
    }

    @Override // kotlin.DeepRecursiveScope
    public <U, S> Object callRecursive(DeepRecursiveFunction<U, S> r8, U r9, Continuation<? super S> r10) {
        Function3<DeepRecursiveScope<U, S>, U, Continuation<? super S>, Object> r2 = r8.getBlock$kotlin_stdlib();
        Intrinsics.checkNotNull(r2, "null cannot be cast to non-null type @[ExtensionFunctionType] kotlin.coroutines.SuspendFunction2<kotlin.DeepRecursiveScope<*, *>, kotlin.Any?, kotlin.Any?>{ kotlin.DeepRecursiveKt.DeepRecursiveFunctionBlock }");
        DeepRecursiveScopeImpl<T, R> r3 = this;
        Function3<? super DeepRecursiveScope<?, ?>, Object, ? super Continuation<Object>, ? extends Object> r5 = r3.function;
        if (r2 == r5) goto L5;
        r3.function = r2;
        Intrinsics.checkNotNull(r10, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        r3.cont = r3.crossFunctionCompletion(r5, r10);
    L6:
        r3.value = r9;
        Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (r0 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L9;
        DebugProbesKt.probeCoroutineSuspended(r10);
    L9:
        return r0;
    L5:
        Intrinsics.checkNotNull(r10, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        r3.cont = r10;
        goto L6
    }

    @Override // kotlin.coroutines.Continuation
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(Object r2) {
        this.cont = null;
        this.result = r2;
    }

    public final R runCallLoop() {
    L2:
        R r0 = (R) this.result;
        Continuation<Object> r1 = this.cont;
        if (r1 == null) goto L4;
        if (Result.m821equalsimpl0(DeepRecursiveKt.access$getUNDEFINED_RESULT$p(), r0) == false) goto L17;
        Function3<? super DeepRecursiveScope<?, ?>, Object, ? super Continuation<Object>, ? extends Object> r2 = this.function;     // Catch: Throwable -> L15
        Object r3 = this.value;     // Catch: Throwable -> L15
        if ((r2 instanceof BaseContinuationImpl) == true) goto L11;
        Object r4 = IntrinsicsKt.wrapWithContinuationImpl(r2, this, r3, r1);     // Catch: Throwable -> L15
    L13:
        if (r4 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L2;
        Result.Companion r5 = Result.Companion;
        r1.resumeWith(Result.m819constructorimpl(r4));
        goto L2
    L11:
        r4 = ((Function3) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r2, 3)).invoke(this, r3, r1);     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        Result.Companion r6 = Result.Companion;
        r1.resumeWith(Result.m819constructorimpl(ResultKt.createFailure(th)));
        goto L2
    L17:
        this.result = DeepRecursiveKt.access$getUNDEFINED_RESULT$p();
        r1.resumeWith(r0);
        goto L2
    L4:
        ResultKt.throwOnFailure(r0);
        return r0;
    }
}
