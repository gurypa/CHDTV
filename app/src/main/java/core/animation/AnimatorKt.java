package androidx.core.animation;

import android.animation.Animator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Animator.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u001a¤\u0001\u0010\u0000\u001a\u00020\u0001*\u00020\u00022#\b\u0006\u0010\u0003\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\n\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\u000b\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0086\bø\u0001\u0000\u001aV\u0010\f\u001a\u00020\r*\u00020\u00022#\b\u0002\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0002\u0010\u000f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0007\u001a5\u0010\u0010\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0086\bø\u0001\u0000\u001a5\u0010\u0012\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0086\bø\u0001\u0000\u001a/\u0010\u0013\u001a\u00020\r*\u00020\u00022!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0007\u001a5\u0010\u0014\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0086\bø\u0001\u0000\u001a/\u0010\u0015\u001a\u00020\r*\u00020\u00022!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0007\u001a5\u0010\u0016\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0017"}, m421d2 = {"addListener", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "onEnd", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "animator", "", "onStart", "onCancel", "onRepeat", "addPauseListener", "Landroid/animation/Animator$AnimatorPauseListener;", "onResume", "onPause", "doOnCancel", "action", "doOnEnd", "doOnPause", "doOnRepeat", "doOnResume", "doOnStart", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class AnimatorKt {
    public static final Animator.AnimatorListener addListener(Animator r3, Function1<? super Animator, Unit> r4, Function1<? super Animator, Unit> r5, Function1<? super Animator, Unit> r6, Function1<? super Animator, Unit> r7) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "onEnd");
        Intrinsics.checkNotNullParameter(r5, "onStart");
        Intrinsics.checkNotNullParameter(r6, "onCancel");
        Intrinsics.checkNotNullParameter(r7, "onRepeat");
        AnimatorKt$addListener$listener$1 r1 = new AnimatorKt$addListener$listener$1(r7, r4, r6, r5);
        r3.addListener(r1);
        return r1;
    }

    public static /* synthetic */ Animator.AnimatorListener addListener$default(Animator r1, C00401 r2, C00412 r3, C00423 r4, C00434 r5, int r6, Object r7) {
        if ((r6 & 1) == 0) goto L6;
        r2 = C00401.INSTANCE;
    L6:
        if ((r6 & 2) == 0) goto L9;
        r3 = C00412.INSTANCE;
    L9:
        if ((r6 & 4) == 0) goto L12;
        r4 = C00423.INSTANCE;
    L12:
        if ((r6 & 8) == 0) goto L14;
        r5 = C00434.INSTANCE;
    L14:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "onEnd");
        Intrinsics.checkNotNullParameter(r3, "onStart");
        Intrinsics.checkNotNullParameter(r4, "onCancel");
        Intrinsics.checkNotNullParameter(r5, "onRepeat");
        AnimatorKt$addListener$listener$1 r8 = new AnimatorKt$addListener$listener$1(r5, r2, r4, r3);
        r1.addListener(r8);
        return r8;
    }

    public static final Animator.AnimatorPauseListener addPauseListener(Animator r2, final Function1<? super Animator, Unit> r3, final Function1<? super Animator, Unit> r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "onResume");
        Intrinsics.checkNotNullParameter(r4, "onPause");
        Animator.AnimatorPauseListener r0 = new AnimatorKt$addPauseListener$listener$1(r4, r3);
        Api19Impl.addPauseListener(r2, r0);
        return r0;
    }

    public static /* synthetic */ Animator.AnimatorPauseListener addPauseListener$default(Animator r0, C00441 r1, C00452 r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = C00441.INSTANCE;
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = C00452.INSTANCE;
    L9:
        return addPauseListener(r0, r1, r2);
    }

    public static final Animator.AnimatorListener doOnCancel(Animator r5, final Function1<? super Animator, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Animator.AnimatorListener r3 = new AnimatorKt$doOnCancel$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Animator.AnimatorListener doOnEnd(Animator r5, final Function1<? super Animator, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Animator.AnimatorListener r3 = new AnimatorKt$doOnEnd$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Animator.AnimatorPauseListener doOnPause(Animator r2, Function1<? super Animator, Unit> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        return addPauseListener$default(r2, null, r3, 1, null);
    }

    public static final Animator.AnimatorListener doOnRepeat(Animator r5, final Function1<? super Animator, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Animator.AnimatorListener r3 = new AnimatorKt$doOnRepeat$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Animator.AnimatorPauseListener doOnResume(Animator r2, Function1<? super Animator, Unit> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "action");
        return addPauseListener$default(r2, r3, null, 2, null);
    }

    public static final Animator.AnimatorListener doOnStart(Animator r5, final Function1<? super Animator, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Animator.AnimatorListener r3 = new AnimatorKt$doOnStart$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }
}
