package androidx.core.os;

import android.os.PersistableBundle;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;

/* JADX INFO: compiled from: PersistableBundle.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J$\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u0007¨\u0006\r"}, m421d2 = {"Landroidx/core/os/PersistableBundleApi21ImplKt;", "", "()V", "createPersistableBundle", "Landroid/os/PersistableBundle;", "capacity", "", "putValue", "", "persistableBundle", "key", "", "value", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
final class PersistableBundleApi21ImplKt {
    public static final PersistableBundleApi21ImplKt INSTANCE = null;

    static {
        INSTANCE = new PersistableBundleApi21ImplKt();
    }

    private PersistableBundleApi21ImplKt() {
    }

    @JvmStatic
    public static final PersistableBundle createPersistableBundle(int r1) {
        return new PersistableBundle(r1);
    }

    @JvmStatic
    public static final void putValue(PersistableBundle r9, String r10, Object r11) {
        Intrinsics.checkNotNullParameter(r9, "persistableBundle");
        if (r11 != null) goto L6;
        r9.putString(r10, null);
        return;
    L6:
        if ((r11 instanceof Boolean) == false) goto L9;
        PersistableBundleApi22ImplKt.putBoolean(r9, r10, ((Boolean) r11).booleanValue());
        return;
    L9:
        if ((r11 instanceof Double) == false) goto L12;
        r9.putDouble(r10, ((Number) r11).doubleValue());
        return;
    L12:
        if ((r11 instanceof Integer) == false) goto L15;
        r9.putInt(r10, ((Number) r11).intValue());
        return;
    L15:
        if ((r11 instanceof Long) == false) goto L18;
        r9.putLong(r10, ((Number) r11).longValue());
        return;
    L18:
        if ((r11 instanceof String) == false) goto L21;
        r9.putString(r10, (String) r11);
        return;
    L21:
        if ((r11 instanceof boolean[]) == false) goto L24;
        PersistableBundleApi22ImplKt.putBooleanArray(r9, r10, (boolean[]) r11);
        return;
    L24:
        if ((r11 instanceof double[]) == false) goto L27;
        r9.putDoubleArray(r10, (double[]) r11);
        return;
    L27:
        if ((r11 instanceof int[]) == false) goto L30;
        r9.putIntArray(r10, (int[]) r11);
        return;
    L30:
        if ((r11 instanceof long[]) == false) goto L33;
        r9.putLongArray(r10, (long[]) r11);
        return;
    L33:
        if ((r11 instanceof Object[]) == false) goto L42;
        Class<?> r2 = r11.getClass().getComponentType();
        Intrinsics.checkNotNull(r2);
        if (String.class.isAssignableFrom(r2) == false) goto L40;
        Intrinsics.checkNotNull(r11, "null cannot be cast to non-null type kotlin.Array<kotlin.String>");
        r9.putStringArray(r10, (String[]) r11);
        return;
    L40:
        throw new IllegalArgumentException("Illegal value array type " + r2.getCanonicalName() + " for key \"" + r10 + Typography.quote);
    L42:
        throw new IllegalArgumentException("Illegal value type " + r11.getClass().getCanonicalName() + " for key \"" + r10 + Typography.quote);
    }
}
