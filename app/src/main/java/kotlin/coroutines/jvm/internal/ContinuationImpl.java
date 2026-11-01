package kotlin.coroutines.jvm.internal;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ContinuationImpl.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b!\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005B!\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003J\b\u0010\r\u001a\u00020\u000eH\u0014R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0018\u0010\f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, m421d2 = {"Lkotlin/coroutines/jvm/internal/ContinuationImpl;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "completion", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/coroutines/Continuation;)V", "_context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/Continuation;Lkotlin/coroutines/CoroutineContext;)V", "context", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "intercepted", "releaseIntercepted", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public abstract class ContinuationImpl extends BaseContinuationImpl {
    private final CoroutineContext _context;
    private transient Continuation<Object> intercepted;

    public ContinuationImpl(Continuation<Object> r2) {
        if (r2 == null) goto L4;
        CoroutineContext r0 = r2.getContext();
    L5:
        this(r2, r0);
        return;
    L4:
        r0 = null;
        goto L5
    }

    public ContinuationImpl(Continuation<Object> r1, CoroutineContext r2) {
        super(r1);
        this._context = r2;
    }

    @Override // kotlin.coroutines.Continuation
    public CoroutineContext getContext() {
        CoroutineContext r0 = this._context;
        Intrinsics.checkNotNull(r0);
        return r0;
    }

    public final Continuation<Object> intercepted() {
        ContinuationImpl r0 = this.intercepted;
        if (r0 != null) goto L10;
        ContinuationInterceptor r1 = (ContinuationInterceptor) getContext().get(ContinuationInterceptor.Key);
        if (r1 == null) goto L8;
        r0 = r1.interceptContinuation(this);
        if (r0 == null) goto L8;
    L9:
        this.intercepted = r0;
    L8:
        r0 = this;
    L10:
        return r0;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    protected void releaseIntercepted() {
        Continuation<?> r0 = this.intercepted;
        if (r0 == null) goto L6;
        if (r0 == this) goto L6;
        CoroutineContext.Element r1 = getContext().get(ContinuationInterceptor.Key);
        Intrinsics.checkNotNull(r1);
        ((ContinuationInterceptor) r1).releaseInterceptedContinuation(r0);
    L6:
        this.intercepted = CompletedContinuation.INSTANCE;
    }
}
