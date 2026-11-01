package androidx.core.animation;

import android.animation.Animator;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Animator.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\t"}, m421d2 = {"Landroidx/core/animation/Api19Impl;", "", "()V", "addPauseListener", "", "animator", "Landroid/animation/Animator;", "listener", "Landroid/animation/Animator$AnimatorPauseListener;", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
final class Api19Impl {
    public static final Api19Impl INSTANCE = null;

    static {
        INSTANCE = new Api19Impl();
    }

    private Api19Impl() {
    }

    @JvmStatic
    public static final void addPauseListener(Animator r1, Animator.AnimatorPauseListener r2) {
        Intrinsics.checkNotNullParameter(r1, "animator");
        Intrinsics.checkNotNullParameter(r2, "listener");
        r1.addPauseListener(r2);
    }
}
