package androidx.core.view;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: View.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007¨\u0006\u000b"}, m421d2 = {"Landroidx/core/view/Api16Impl;", "", "()V", "postOnAnimationDelayed", "", "view", "Landroid/view/View;", "action", "Ljava/lang/Runnable;", "delayInMillis", "", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
final class Api16Impl {
    public static final Api16Impl INSTANCE = null;

    static {
        INSTANCE = new Api16Impl();
    }

    private Api16Impl() {
    }

    @JvmStatic
    public static final void postOnAnimationDelayed(View r1, Runnable r2, long r3) {
        Intrinsics.checkNotNullParameter(r1, "view");
        Intrinsics.checkNotNullParameter(r2, "action");
        r1.postOnAnimationDelayed(r2, r3);
    }
}
