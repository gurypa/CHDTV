package kotlin.coroutines.jvm.internal;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RunSuspend.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0006\u0010\u000e\u001a\u00020\u0002J\u001b\u0010\u000f\u001a\u00020\u00022\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\tH\u0016¢\u0006\u0002\u0010\u0010R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R%\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\tX\u0086\u000eø\u0001\u0000¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0011"}, m421d2 = {"Lkotlin/coroutines/jvm/internal/RunSuspend;", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "result", "Lkotlin/Result;", "getResult-xLWZpok", "()Lkotlin/Result;", "setResult", "(Lkotlin/Result;)V", "await", "resumeWith", "(Ljava/lang/Object;)V", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class RunSuspend implements Continuation<Unit> {
    private Result<Unit> result;

    public RunSuspend() {
    }

    public final void await() {
        monitor-enter(this);
    L14:
        Result<Unit> r1 = this.result;     // Catch: Throwable -> L11
        if (r1 != null) goto L8;
        Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");     // Catch: Throwable -> L11
        wait();     // Catch: Throwable -> L11
        goto L14
    L8:
        ResultKt.throwOnFailure(r1.m828unboximpl());     // Catch: Throwable -> L11
        monitor-exit(this);
        return;
    L11:
        th = move-exception;
        throw th;
    }

    @Override // kotlin.coroutines.Continuation
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    /* JADX INFO: renamed from: getResult-xLWZpok, reason: not valid java name */
    public final Result<Unit> m2024getResultxLWZpok() {
        return this.result;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(Object r3) {
        monitor-enter(this);
        this.result = Result.m818boximpl(r3);     // Catch: Throwable -> L7
        Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");     // Catch: Throwable -> L7
        notifyAll();     // Catch: Throwable -> L7
        Unit r0 = Unit.INSTANCE;     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public final void setResult(Result<Unit> r1) {
        this.result = r1;
    }
}
