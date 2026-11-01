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
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;

/* JADX INFO: compiled from: ViewGroup.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m421d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Landroid/view/View;"}, m422k = 3, m423mv = {1, 7, 1}, m425xi = 48)
@DebugMetadata(m436c = "androidx.core.view.ViewGroupKt$descendants$1", m437f = "ViewGroup.kt", m438i = {0, 0, 0, 0, 1, 1, 1}, m439l = {119, 121}, m440m = "invokeSuspend", m441n = {"$this$sequence", "$this$forEach$iv", "child", "index$iv", "$this$sequence", "$this$forEach$iv", "index$iv"}, m442s = {"L$0", "L$1", "L$2", "I$0", "L$0", "L$1", "I$0"})
final class ViewGroupKt$descendants$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ViewGroup $this_descendants;
    int I$0;
    int I$1;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    ViewGroupKt$descendants$1(ViewGroup r2, Continuation<? super ViewGroupKt$descendants$1> r3) {
        this.$this_descendants = r2;
        super(2, r3);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object r3, Continuation<?> r4) {
        ViewGroupKt$descendants$1 r0 = new ViewGroupKt$descendants$1(this.$this_descendants, r4);
        r0.L$0 = r3;
        return r0;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(SequenceScope<? super View> r2, Continuation<? super Unit> r3) {
        return invoke2(r2, r3);
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final Object invoke2(SequenceScope<? super View> r3, Continuation<? super Unit> r4) {
        return ((ViewGroupKt$descendants$1) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0093 -> B:20:0x0095). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x009a -> B:22:0x009e). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object r14) {
        Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch(this.label) {
            case 0: goto L8;
            case 1: goto L7;
            case 2: goto L6;
            default: goto L5;
        };
    L6:
        ViewGroupKt$descendants$1 r1 = this;
        boolean r3 = false;
        int r5 = r1.I$1;
        int r6 = r1.I$0;
        ViewGroup r7 = (ViewGroup) r1.L$1;
        SequenceScope r8 = (SequenceScope) r1.L$0;
        ResultKt.throwOnFailure(r14);
    L20:
        ViewGroup r4 = r7;
        int r9 = r5;
        boolean r10 = r3;
        SequenceScope r11 = r8;
    L22:
        r6 = r6 + 1;
    L9:
        if (r6 >= r9) goto L24;
        View r12 = r4.getChildAt(r6);
        Intrinsics.checkNotNullExpressionValue(r12, "getChildAt(index)");
        r1.L$0 = r11;
        r1.L$1 = r4;
        r1.L$2 = r12;
        r1.I$0 = r6;
        r1.I$1 = r9;
        r1.label = 1;
        if (r11.yield(r12, r1) != r0) goto L13;
        return r0;
    L13:
        SequenceScope r13 = r11;
        r3 = r10;
        r5 = r9;
        View r15 = r12;
        ViewGroup r16 = r4;
    L15:
        if ((r15 instanceof ViewGroup) == false) goto L21;
        Sequence<View> r17 = ViewGroupKt.getDescendants((ViewGroup) r15);
        r1.L$0 = r13;
        r1.L$1 = r16;
        r1.L$2 = null;
        r1.I$0 = r6;
        r1.I$1 = r5;
        r1.label = 2;
        if (r13.yieldAll(r17, r1) != r0) goto L19;
        return r0;
    L19:
        r7 = r16;
        r8 = r13;
        goto L20
    L21:
        r9 = r5;
        r4 = r16;
        r10 = r3;
        r11 = r13;
        goto L22
    L24:
        return Unit.INSTANCE;
    L7:
        r1 = this;
        r3 = false;
        r5 = r1.I$1;
        r6 = r1.I$0;
        r15 = (View) r1.L$2;
        r16 = (ViewGroup) r1.L$1;
        r13 = (SequenceScope) r1.L$0;
        ResultKt.throwOnFailure(r14);
        goto L15
    L8:
        ResultKt.throwOnFailure(r14);
        r1 = this;
        r11 = (SequenceScope) r1.L$0;
        r4 = r1.$this_descendants;
        r10 = false;
        r6 = 0;
        r9 = r4.getChildCount();
        goto L9
    L5:
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
