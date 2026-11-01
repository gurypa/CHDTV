package androidx.core.transition;

import android.transition.Transition;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Transition.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\u001aÉ\u0001\u0010\u0000\u001a\u00020\u0001*\u00020\u00022#\b\u0006\u0010\u0003\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\n\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\u000b\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\bø\u0001\u0000\u001a5\u0010\r\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\bø\u0001\u0000\u001a5\u0010\u000f\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\bø\u0001\u0000\u001a5\u0010\u0010\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\bø\u0001\u0000\u001a5\u0010\u0011\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\bø\u0001\u0000\u001a5\u0010\u0012\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0013"}, m421d2 = {"addListener", "Landroid/transition/Transition$TransitionListener;", "Landroid/transition/Transition;", "onEnd", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "transition", "", "onStart", "onCancel", "onResume", "onPause", "doOnCancel", "action", "doOnEnd", "doOnPause", "doOnResume", "doOnStart", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class TransitionKt {
    public static final Transition.TransitionListener addListener(Transition r7, Function1<? super Transition, Unit> r8, Function1<? super Transition, Unit> r9, Function1<? super Transition, Unit> r10, Function1<? super Transition, Unit> r11, Function1<? super Transition, Unit> r12) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "onEnd");
        Intrinsics.checkNotNullParameter(r9, "onStart");
        Intrinsics.checkNotNullParameter(r10, "onCancel");
        Intrinsics.checkNotNullParameter(r11, "onResume");
        Intrinsics.checkNotNullParameter(r12, "onPause");
        TransitionKt$addListener$listener$1 r1 = new TransitionKt$addListener$listener$1(r8, r11, r12, r10, r9);
        r7.addListener(r1);
        return r1;
    }

    public static /* synthetic */ Transition.TransitionListener addListener$default(Transition r1, C00921 r2, Function1 r3, C00943 r4, C00954 r5, C00965 r6, int r7, Object r8) {
        if ((r7 & 1) == 0) goto L6;
        r2 = C00921.INSTANCE;
    L6:
        if ((r7 & 2) == 0) goto L8;
        Function1 r9 = C00932.INSTANCE;
    L10:
        if ((r7 & 4) == 0) goto L13;
        r4 = C00943.INSTANCE;
    L13:
        if ((r7 & 8) == 0) goto L16;
        r5 = C00954.INSTANCE;
    L16:
        if ((r7 & 16) == 0) goto L18;
        r6 = C00965.INSTANCE;
    L18:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "onEnd");
        Intrinsics.checkNotNullParameter(r9, "onStart");
        Intrinsics.checkNotNullParameter(r4, "onCancel");
        Intrinsics.checkNotNullParameter(r5, "onResume");
        Intrinsics.checkNotNullParameter(r6, "onPause");
        Function1 r10 = r4;
        TransitionKt$addListener$listener$1 r11 = new TransitionKt$addListener$listener$1(r2, r5, r6, r10, r9);
        r1.addListener(r11);
        return r11;
    L8:
        r9 = r3;
        goto L10
    }

    public static final Transition.TransitionListener doOnCancel(Transition r5, final Function1<? super Transition, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Transition.TransitionListener r3 = new TransitionKt$doOnCancel$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Transition.TransitionListener doOnEnd(Transition r5, final Function1<? super Transition, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Transition.TransitionListener r3 = new TransitionKt$doOnEnd$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Transition.TransitionListener doOnPause(Transition r5, final Function1<? super Transition, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Transition.TransitionListener r3 = new TransitionKt$doOnPause$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Transition.TransitionListener doOnResume(Transition r5, final Function1<? super Transition, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Transition.TransitionListener r3 = new TransitionKt$doOnResume$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }

    public static final Transition.TransitionListener doOnStart(Transition r5, final Function1<? super Transition, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        Transition.TransitionListener r3 = new TransitionKt$doOnStart$$inlined$addListener$default$1(r6);
        r5.addListener(r3);
        return r3;
    }
}
