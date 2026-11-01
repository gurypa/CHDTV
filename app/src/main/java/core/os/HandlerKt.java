package androidx.core.os;

import android.os.Handler;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Handler.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a4\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u000e\b\u0004\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0086\bø\u0001\u0000\u001a4\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u000e\b\u0004\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\f"}, m421d2 = {"postAtTime", "Ljava/lang/Runnable;", "Landroid/os/Handler;", "uptimeMillis", "", "token", "", "action", "Lkotlin/Function0;", "", "postDelayed", "delayInMillis", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class HandlerKt {
    public static final Runnable postAtTime(Handler r2, long r3, Object r5, Function0<Unit> r6) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        HandlerKt$postAtTime$runnable$1 r1 = new HandlerKt$postAtTime$runnable$1(r6);
        r2.postAtTime(r1, r5, r3);
        return r1;
    }

    public static /* synthetic */ Runnable postAtTime$default(Handler r0, long r1, Object r3, Function0 r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L5;
        r3 = null;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r4, "action");
        HandlerKt$postAtTime$runnable$1 r7 = new HandlerKt$postAtTime$runnable$1(r4);
        r0.postAtTime(r7, r3, r1);
        return r7;
    }

    public static final Runnable postDelayed(Handler r2, long r3, Object r5, Function0<Unit> r6) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        HandlerKt$postDelayed$runnable$1 r1 = new HandlerKt$postDelayed$runnable$1(r6);
        if (r5 != null) goto L5;
        r2.postDelayed(r1, r3);
    L6:
        return r1;
    L5:
        HandlerCompat.postDelayed(r2, r1, r5, r3);
        goto L6
    }

    public static /* synthetic */ Runnable postDelayed$default(Handler r0, long r1, Object r3, Function0 r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L5;
        r3 = null;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r4, "action");
        HandlerKt$postDelayed$runnable$1 r7 = new HandlerKt$postDelayed$runnable$1(r4);
        if (r3 != null) goto L8;
        r0.postDelayed(r7, r1);
    L9:
        return r7;
    L8:
        HandlerCompat.postDelayed(r0, r7, r3, r1);
        goto L9
    }
}
