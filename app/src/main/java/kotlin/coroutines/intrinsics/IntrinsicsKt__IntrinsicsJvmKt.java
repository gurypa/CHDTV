package kotlin.coroutines.intrinsics;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* JADX INFO: compiled from: IntrinsicsJvm.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b¢\u0006\u0002\b\b\u001a'\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0002¢\u0006\u0002\b\n\u001aA\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007¢\u0006\u0002\u0010\f\u001aZ\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001a\u001e\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001a>\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b¢\u0006\u0002\u0010\u0014\u001aW\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b¢\u0006\u0002\u0010\u0015\u001ak\u0010\u0013\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0016\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0017¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\u0006\u0010\u0018\u001a\u0002H\u00162\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\b¢\u0006\u0002\u0010\u0019\u001a=\u0010\u001a\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0001¢\u0006\u0002\u0010\u0014\u001aV\u0010\u001a\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0001¢\u0006\u0002\u0010\u0015\u001aj\u0010\u001a\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u0016\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0017¢\u0006\u0002\b\u000f2\u0006\u0010\u0010\u001a\u0002H\r2\u0006\u0010\u0018\u001a\u0002H\u00162\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0001¢\u0006\u0002\u0010\u0019¨\u0006\u001b"}, m421d2 = {"createCoroutineFromSuspendFunction", "Lkotlin/coroutines/Continuation;", "", ExifInterface.GPS_DIRECTION_TRUE, "completion", "block", "Lkotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createSimpleCoroutineForSuspendFunction", "createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "P", "Lkotlin/Function3;", "param", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "wrapWithContinuationImpl", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/coroutines/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt {
    public IntrinsicsKt__IntrinsicsJvmKt() {
    }

    private static final <T> Continuation<Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(final Continuation<? super T> r3, final Function1<? super Continuation<? super T>, ? extends Object> r4) {
        final CoroutineContext r1 = r3.getContext();
        if (r1 != EmptyCoroutineContext.INSTANCE) goto L6;
        return new C1085x96e8297a(r3, r4);
    L6:
        return new C1086x96e8297b(r3, r1, r4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Continuation<Unit> createCoroutineUnintercepted(final Function1<? super Continuation<? super T>, ? extends Object> r4, Continuation<? super T> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "completion");
        final Continuation<?> r0 = DebugProbesKt.probeCoroutineCreated(r5);
        if ((r4 instanceof BaseContinuationImpl) == true) goto L11;
        final CoroutineContext r2 = r0.getContext();
        if (r2 != EmptyCoroutineContext.INSTANCE) goto L8;
        ContinuationImpl r3 = new C1087xa50de660(r0, r4);
    L10:
        return r3;
    L8:
        r3 = new C1088xa50de661(r0, r2, r4);
        goto L10
    L11:
        return ((BaseContinuationImpl) r4).create(r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(final Function2<? super R, ? super Continuation<? super T>, ? extends Object> r4, final R r5, Continuation<? super T> r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r6, "completion");
        final Continuation<?> r0 = DebugProbesKt.probeCoroutineCreated(r6);
        if ((r4 instanceof BaseContinuationImpl) == true) goto L11;
        final CoroutineContext r2 = r0.getContext();
        if (r2 != EmptyCoroutineContext.INSTANCE) goto L8;
        ContinuationImpl r3 = new C1089xa50de662(r0, r4, r5);
    L10:
        return r3;
    L8:
        r3 = new C1090xa50de663(r0, r2, r4, r5);
        goto L10
    L11:
        return ((BaseContinuationImpl) r4).create(r5, r0);
    }

    /* JADX INFO: renamed from: createSimpleCoroutineForSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt */
    private static final <T> Continuation<T> m435xca667f36(final Continuation<? super T> r2) {
        final CoroutineContext r0 = r2.getContext();
        if (r0 != EmptyCoroutineContext.INSTANCE) goto L6;
        return new C1091xcc43cd03(r2);
    L6:
        return new C1092xcc43cd04(r2, r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Continuation<T> intercepted(Continuation<? super T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof ContinuationImpl) == false) goto L5;
        ContinuationImpl r0 = (ContinuationImpl) r1;
    L6:
        if (r0 == null) goto L10;
        Continuation<T> r2 = (Continuation<T>) r0.intercepted();
        if (r2 == null) goto L10;
        return r2;
    L10:
        return r1;
    L5:
        r0 = null;
        goto L6
    }

    private static final <T> Object startCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> r1, Continuation<? super T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "completion");
        if ((r1 instanceof BaseContinuationImpl) == true) goto L6;
        return IntrinsicsKt.wrapWithContinuationImpl(r1, r2);
    L6:
        return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r1, 1)).invoke(r2);
    }

    private static final <R, T> Object startCoroutineUninterceptedOrReturn(Function2<? super R, ? super Continuation<? super T>, ? extends Object> r1, R r2, Continuation<? super T> r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r3, "completion");
        if ((r1 instanceof BaseContinuationImpl) == true) goto L6;
        return IntrinsicsKt.wrapWithContinuationImpl(r1, r2, r3);
    L6:
        return ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r1, 2)).invoke(r2, r3);
    }

    private static final <R, P, T> Object startCoroutineUninterceptedOrReturn(Function3<? super R, ? super P, ? super Continuation<? super T>, ? extends Object> r1, R r2, P r3, Continuation<? super T> r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r4, "completion");
        if ((r1 instanceof BaseContinuationImpl) == true) goto L6;
        return IntrinsicsKt.wrapWithContinuationImpl(r1, r2, r3, r4);
    L6:
        return ((Function3) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r1, 3)).invoke(r2, r3, r4);
    }

    public static final <T> Object wrapWithContinuationImpl(Function1<? super Continuation<? super T>, ? extends Object> r2, Continuation<? super T> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "completion");
        Continuation r0 = m435xca667f36(DebugProbesKt.probeCoroutineCreated(r3));
        return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r2, 1)).invoke(r0);
    }

    public static final <R, T> Object wrapWithContinuationImpl(Function2<? super R, ? super Continuation<? super T>, ? extends Object> r2, R r3, Continuation<? super T> r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r4, "completion");
        Continuation r0 = m435xca667f36(DebugProbesKt.probeCoroutineCreated(r4));
        return ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r2, 2)).invoke(r3, r0);
    }

    public static final <R, P, T> Object wrapWithContinuationImpl(Function3<? super R, ? super P, ? super Continuation<? super T>, ? extends Object> r2, R r3, P r4, Continuation<? super T> r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r5, "completion");
        Continuation r0 = m435xca667f36(DebugProbesKt.probeCoroutineCreated(r5));
        return ((Function3) TypeIntrinsics.beforeCheckcastToFunctionOfArity(r2, 3)).invoke(r3, r4, r0);
    }
}
