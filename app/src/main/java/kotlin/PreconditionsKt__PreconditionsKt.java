package kotlin;

import androidx.exifinterface.media.ExifInterface;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Preconditions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0001\n\u0002\b\u0004\u001a\u001c\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a/\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\bH\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\t\u001a@\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\n\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a-\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a/\u0010\u000f\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\bH\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\t\u001a@\u0010\u000f\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0010"}, m421d2 = {"check", "", "value", "", "lazyMessage", "Lkotlin/Function0;", "", "checkNotNull", ExifInterface.GPS_DIRECTION_TRUE, "(Ljava/lang/Object;)Ljava/lang/Object;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "error", "", "message", "require", "requireNotNull", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/PreconditionsKt")
class PreconditionsKt__PreconditionsKt extends PreconditionsKt__AssertionsJVMKt {
    public PreconditionsKt__PreconditionsKt() {
    }

    private static final void check(boolean r2) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalStateException("Check failed.".toString());
    }

    private static final void check(boolean r3, Function0<? extends Object> r4) {
        Intrinsics.checkNotNullParameter(r4, "lazyMessage");
        if (r3 == false) goto L6;
        return;
    L6:
        throw new IllegalStateException(r4.invoke().toString());
    }

    private static final <T> T checkNotNull(T r2) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new IllegalStateException("Required value was null.".toString());
    }

    private static final <T> T checkNotNull(T r3, Function0<? extends Object> r4) {
        Intrinsics.checkNotNullParameter(r4, "lazyMessage");
        if (r3 == null) goto L6;
        return r3;
    L6:
        throw new IllegalStateException(r4.invoke().toString());
    }

    private static final Void error(Object r2) {
        Intrinsics.checkNotNullParameter(r2, "message");
        throw new IllegalStateException(r2.toString());
    }

    private static final void require(boolean r2) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    private static final void require(boolean r3, Function0<? extends Object> r4) {
        Intrinsics.checkNotNullParameter(r4, "lazyMessage");
        if (r3 == false) goto L6;
        return;
    L6:
        throw new IllegalArgumentException(r4.invoke().toString());
    }

    private static final <T> T requireNotNull(T r2) {
        if (r2 == null) goto L5;
        return r2;
    L5:
        throw new IllegalArgumentException("Required value was null.".toString());
    }

    private static final <T> T requireNotNull(T r3, Function0<? extends Object> r4) {
        Intrinsics.checkNotNullParameter(r4, "lazyMessage");
        if (r3 == null) goto L6;
        return r3;
    L6:
        throw new IllegalArgumentException(r4.invoke().toString());
    }
}
