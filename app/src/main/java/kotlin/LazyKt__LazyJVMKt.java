package kotlin;

import androidx.exifinterface.media.ExifInterface;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LazyJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¨\u0006\t"}, m421d2 = {"lazy", "Lkotlin/Lazy;", ExifInterface.GPS_DIRECTION_TRUE, "initializer", "Lkotlin/Function0;", "lock", "", "mode", "Lkotlin/LazyThreadSafetyMode;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/LazyKt")
class LazyKt__LazyJVMKt {

    /* JADX INFO: compiled from: LazyJVM.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            int[] r0 = new int[LazyThreadSafetyMode.values().length];
            r0[LazyThreadSafetyMode.SYNCHRONIZED.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L14:
            r0[LazyThreadSafetyMode.PUBLICATION.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L16:
            r0[LazyThreadSafetyMode.NONE.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L12:
            $EnumSwitchMapping$0 = r0;
        }
    }

    public LazyKt__LazyJVMKt() {
    }

    public static final <T> Lazy<T> lazy(Object r1, Function0<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r2, "initializer");
        return new SynchronizedLazyImpl(r2, r1);
    }

    public static final <T> Lazy<T> lazy(LazyThreadSafetyMode r3, Function0<? extends T> r4) {
        Intrinsics.checkNotNullParameter(r3, "mode");
        Intrinsics.checkNotNullParameter(r4, "initializer");
        switch(WhenMappings.$EnumSwitchMapping$0[r3.ordinal()]) {
            case 1: goto L8;
            case 2: goto L11;
            case 3: goto L10;
            default: goto L5;
        };
    L8:
        DefaultConstructorMarker r2 = null;
        return new SynchronizedLazyImpl(r4, r2, 2, r2);
    L5:
        throw new NoWhenBranchMatchedException();
    L10:
        return new UnsafeLazyImpl(r4);
    L11:
        return new SafePublicationLazyImpl(r4);
    }

    public static final <T> Lazy<T> lazy(Function0<? extends T> r3) {
        Intrinsics.checkNotNullParameter(r3, "initializer");
        DefaultConstructorMarker r1 = null;
        return new SynchronizedLazyImpl(r3, r1, 2, r1);
    }
}
