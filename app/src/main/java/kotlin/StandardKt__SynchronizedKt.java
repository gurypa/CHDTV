package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Synchronized.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0012\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a:\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u0006\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0007"}, m421d2 = {"synchronized", "R", "lock", "", "block", "Lkotlin/Function0;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/StandardKt")
class StandardKt__SynchronizedKt extends StandardKt__StandardKt {
    public StandardKt__SynchronizedKt() {
    }

    /* JADX INFO: renamed from: synchronized, reason: not valid java name */
    private static final <R> R m829synchronized(Object r2, Function0<? extends R> r3) {
        Intrinsics.checkNotNullParameter(r2, "lock");
        Intrinsics.checkNotNullParameter(r3, "block");
        monitor-enter(r2);
        R r1 = r3.invoke();     // Catch: Throwable -> L7
        monitor-exit(r2);
        return r1;
    L7:
        th = move-exception;
        throw th;
    }
}
