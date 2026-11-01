package androidx.core.animation;

import android.animation.Animator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Animator.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\t"}, m421d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "onAnimationCancel", "", "animator", "Landroid/animation/Animator;", "onAnimationEnd", "onAnimationRepeat", "onAnimationStart", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 176)
public final class AnimatorKt$addListener$listener$1 implements Animator.AnimatorListener {
    final /* synthetic */ Function1<Animator, Unit> $onCancel;
    final /* synthetic */ Function1<Animator, Unit> $onEnd;
    final /* synthetic */ Function1<Animator, Unit> $onRepeat;
    final /* synthetic */ Function1<Animator, Unit> $onStart;

    /* JADX WARN: Multi-variable type inference failed */
    public AnimatorKt$addListener$listener$1(Function1<? super Animator, Unit> r1, Function1<? super Animator, Unit> r2, Function1<? super Animator, Unit> r3, Function1<? super Animator, Unit> r4) {
        this.$onRepeat = r1;
        this.$onEnd = r2;
        this.$onCancel = r3;
        this.$onStart = r4;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator r2) {
        Intrinsics.checkNotNullParameter(r2, "animator");
        this.$onCancel.invoke(r2);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator r2) {
        Intrinsics.checkNotNullParameter(r2, "animator");
        this.$onEnd.invoke(r2);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator r2) {
        Intrinsics.checkNotNullParameter(r2, "animator");
        this.$onRepeat.invoke(r2);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator r2) {
        Intrinsics.checkNotNullParameter(r2, "animator");
        this.$onStart.invoke(r2);
    }
}
