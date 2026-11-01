package androidx.core.os;

import android.os.Bundle;
import android.os.IBinder;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Bundle.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0007¨\u0006\u000b"}, m421d2 = {"Landroidx/core/os/BundleApi18ImplKt;", "", "()V", "putBinder", "", "bundle", "Landroid/os/Bundle;", "key", "", "value", "Landroid/os/IBinder;", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
final class BundleApi18ImplKt {
    public static final BundleApi18ImplKt INSTANCE = null;

    static {
        INSTANCE = new BundleApi18ImplKt();
    }

    private BundleApi18ImplKt() {
    }

    @JvmStatic
    public static final void putBinder(Bundle r1, String r2, IBinder r3) {
        Intrinsics.checkNotNullParameter(r1, "bundle");
        Intrinsics.checkNotNullParameter(r2, "key");
        r1.putBinder(r2, r3);
    }
}
