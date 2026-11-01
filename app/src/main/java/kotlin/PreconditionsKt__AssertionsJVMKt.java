package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AssertionsJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0007"}, m421d2 = {"assert", "", "value", "", "lazyMessage", "Lkotlin/Function0;", "", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/PreconditionsKt")
class PreconditionsKt__AssertionsJVMKt {
    public PreconditionsKt__AssertionsJVMKt() {
    }

    /* JADX INFO: renamed from: assert, reason: not valid java name */
    private static final void m816assert(boolean r2) {
        if (r2 == false) goto L5;
        return;
    L5:
        throw new AssertionError("Assertion failed");
    }

    /* JADX INFO: renamed from: assert, reason: not valid java name */
    private static final void m817assert(boolean r2, Function0<? extends Object> r3) {
        Intrinsics.checkNotNullParameter(r3, "lazyMessage");
        if (r2 == false) goto L6;
        return;
    L6:
        throw new AssertionError(r3.invoke());
    }
}
