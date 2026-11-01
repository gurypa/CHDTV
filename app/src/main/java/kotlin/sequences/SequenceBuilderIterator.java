package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: SequenceBuilder.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004B\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0096\u0002J\u000e\u0010\u001a\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\r\u0010\u001c\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001bJ\u001b\u0010\u001d\u001a\u00020\u00052\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u001fH\u0016¢\u0006\u0002\u0010 J\u0016\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010#J\u001c\u0010$\u001a\u00020\u00052\f\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@¢\u0006\u0002\u0010&R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0012R\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, m421d2 = {"Lkotlin/sequences/SequenceBuilderIterator;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/SequenceScope;", "", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "nextIterator", "nextStep", "getNextStep", "()Lkotlin/coroutines/Continuation;", "setNextStep", "(Lkotlin/coroutines/Continuation;)V", "nextValue", "Ljava/lang/Object;", "state", "", "Lkotlin/sequences/State;", "exceptionalState", "", "hasNext", "", "next", "()Ljava/lang/Object;", "nextNotReady", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "yield", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "iterator", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class SequenceBuilderIterator<T> extends SequenceScope<T> implements Iterator<T>, Continuation<Unit>, KMappedMarker {
    private Iterator<? extends T> nextIterator;
    private Continuation<? super Unit> nextStep;
    private T nextValue;
    private int state;

    public SequenceBuilderIterator() {
    }

    private final Throwable exceptionalState() {
        switch(this.state) {
            case 4: goto L7;
            case 5: goto L9;
            default: goto L8;
        };
    L8:
        return new IllegalStateException("Unexpected state of the iterator: " + this.state);
    L9:
        return new IllegalStateException("Iterator has failed.");
    L7:
        return new NoSuchElementException();
    }

    private final T nextNotReady() {
        if (hasNext() == false) goto L7;
        return next();
    L7:
        throw new NoSuchElementException();
    }

    @Override // kotlin.coroutines.Continuation
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    public final Continuation<Unit> getNextStep() {
        return this.nextStep;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
    L3:
        switch(this.state) {
            case 0: goto L14;
            case 1: goto L9;
            case 2: goto L8;
            case 3: goto L8;
            case 4: goto L6;
            default: goto L5;
        };
    L9:
        Iterator<? extends T> r0 = this.nextIterator;
        Intrinsics.checkNotNull(r0);
        if (r0.hasNext() == true) goto L11;
        this.nextIterator = null;
        goto L14
    L11:
        this.state = 2;
        return true;
    L5:
        throw exceptionalState();
    L6:
        return false;
    L8:
        return true;
    L14:
        this.state = 5;
        Continuation<? super Unit> r1 = this.nextStep;
        Intrinsics.checkNotNull(r1);
        this.nextStep = null;
        Result.Companion r2 = Result.Companion;
        r1.resumeWith(Result.m819constructorimpl(Unit.INSTANCE));
        goto L3
    }

    @Override // java.util.Iterator
    public T next() {
        switch(this.state) {
            case 0: goto L11;
            case 1: goto L11;
            case 2: goto L8;
            case 3: goto L6;
            default: goto L5;
        };
    L6:
        this.state = 0;
        T r0 = this.nextValue;
        this.nextValue = null;
        return r0;
    L8:
        this.state = 1;
        Iterator<? extends T> r1 = this.nextIterator;
        Intrinsics.checkNotNull(r1);
        return r1.next();
    L5:
        throw exceptionalState();
    L11:
        return nextNotReady();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(Object r2) {
        ResultKt.throwOnFailure(r2);
        this.state = 4;
    }

    public final void setNextStep(Continuation<? super Unit> r1) {
        this.nextStep = r1;
    }

    @Override // kotlin.sequences.SequenceScope
    public Object yield(T r3, Continuation<? super Unit> r4) {
        this.nextValue = r3;
        this.state = 3;
        this.nextStep = r4;
        Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (r0 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L6;
        DebugProbesKt.probeCoroutineSuspended(r4);
    L6:
        if (r0 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L9;
        return r0;
    L9:
        return Unit.INSTANCE;
    }

    @Override // kotlin.sequences.SequenceScope
    public Object yieldAll(Iterator<? extends T> r3, Continuation<? super Unit> r4) {
        if (r3.hasNext() == false) goto L5;
        this.nextIterator = r3;
        this.state = 2;
        this.nextStep = r4;
        Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (r0 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L10;
        DebugProbesKt.probeCoroutineSuspended(r4);
    L10:
        if (r0 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) goto L13;
        return r0;
    L13:
        return Unit.INSTANCE;
    L5:
        return Unit.INSTANCE;
    }
}
