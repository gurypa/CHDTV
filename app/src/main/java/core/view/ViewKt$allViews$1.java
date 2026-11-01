package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

/* JADX INFO: compiled from: View.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m421d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Landroid/view/View;"}, m422k = 3, m423mv = {1, 7, 1}, m425xi = 48)
@DebugMetadata(m436c = "androidx.core.view.ViewKt$allViews$1", m437f = "View.kt", m438i = {0}, m439l = {414, 416}, m440m = "invokeSuspend", m441n = {"$this$sequence"}, m442s = {"L$0"})
final class ViewKt$allViews$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ View $this_allViews;
    private /* synthetic */ Object L$0;
    int label;

    ViewKt$allViews$1(View r2, Continuation<? super ViewKt$allViews$1> r3) {
        this.$this_allViews = r2;
        super(2, r3);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object r3, Continuation<?> r4) {
        ViewKt$allViews$1 r0 = new ViewKt$allViews$1(this.$this_allViews, r4);
        r0.L$0 = r3;
        return r0;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(SequenceScope<? super View> r2, Continuation<? super Unit> r3) {
        return invoke2(r2, r3);
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final Object invoke2(SequenceScope<? super View> r3, Continuation<? super Unit> r4) {
        return ((ViewKt$allViews$1) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object r7) {
        Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch(this.label) {
            case 0: goto L8;
            case 1: goto L7;
            case 2: goto L6;
            default: goto L5;
        };
    L6:
        ViewKt$allViews$1 r1 = this;
        ResultKt.throwOnFailure(r7);
    L19:
        return Unit.INSTANCE;
    L7:
        ViewKt$allViews$1 r2 = this;
        SequenceScope r3 = (SequenceScope) r2.L$0;
        ResultKt.throwOnFailure(r7);
    L12:
        if ((r2.$this_allViews instanceof ViewGroup) == false) goto L19;
        r2.L$0 = null;
        r2.label = 2;
        if (r3.yieldAll(ViewGroupKt.getDescendants((ViewGroup) r2.$this_allViews), r2) != r0) goto L16;
        return r0;
    L16:
        r1 = r2;
        goto L19
    L8:
        ResultKt.throwOnFailure(r7);
        r2 = this;
        r3 = (SequenceScope) r2.L$0;
        r2.L$0 = r3;
        r2.label = 1;
        if (r3.yield(r2.$this_allViews, r2) != r0) goto L12;
        return r0;
    L5:
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
