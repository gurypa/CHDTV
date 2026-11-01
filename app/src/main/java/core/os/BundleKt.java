package androidx.core.os;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import java.io.Serializable;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;

/* JADX INFO: compiled from: Bundle.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u0006\u0010\u0000\u001a\u00020\u0001\u001a;\u0010\u0000\u001a\u00020\u00012.\u0010\u0002\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003\"\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004¢\u0006\u0002\u0010\u0007¨\u0006\b"}, m421d2 = {"bundleOf", "Landroid/os/Bundle;", "pairs", "", "Lkotlin/Pair;", "", "", "([Lkotlin/Pair;)Landroid/os/Bundle;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class BundleKt {
    public static final Bundle bundleOf() {
        return new Bundle(0);
    }

    public static final Bundle bundleOf(Pair<String, ? extends Object>... r11) {
        Intrinsics.checkNotNullParameter(r11, "pairs");
        Bundle r0 = new Bundle(r11.length);
        int r3 = r11.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L96;
        Pair<String, ? extends Object> r5 = r11[r4];
        String r6 = r5.component1();
        Object r7 = r5.component2();
        if (r7 != null) goto L8;
        r0.putString(r6, null);
    L92:
        r4 = r4 + 1;
        goto L3
    L8:
        if ((r7 instanceof Boolean) == false) goto L11;
        r0.putBoolean(r6, ((Boolean) r7).booleanValue());
        goto L92
    L11:
        if ((r7 instanceof Byte) == false) goto L14;
        r0.putByte(r6, ((Number) r7).byteValue());
        goto L92
    L14:
        if ((r7 instanceof Character) == false) goto L17;
        r0.putChar(r6, ((Character) r7).charValue());
        goto L92
    L17:
        if ((r7 instanceof Double) == false) goto L20;
        r0.putDouble(r6, ((Number) r7).doubleValue());
        goto L92
    L20:
        if ((r7 instanceof Float) == false) goto L23;
        r0.putFloat(r6, ((Number) r7).floatValue());
        goto L92
    L23:
        if ((r7 instanceof Integer) == false) goto L26;
        r0.putInt(r6, ((Number) r7).intValue());
        goto L92
    L26:
        if ((r7 instanceof Long) == false) goto L29;
        r0.putLong(r6, ((Number) r7).longValue());
        goto L92
    L29:
        if ((r7 instanceof Short) == false) goto L32;
        r0.putShort(r6, ((Number) r7).shortValue());
        goto L92
    L32:
        if ((r7 instanceof Bundle) == false) goto L35;
        r0.putBundle(r6, (Bundle) r7);
        goto L92
    L35:
        if ((r7 instanceof CharSequence) == false) goto L38;
        r0.putCharSequence(r6, (CharSequence) r7);
        goto L92
    L38:
        if ((r7 instanceof Parcelable) == false) goto L41;
        r0.putParcelable(r6, (Parcelable) r7);
        goto L92
    L41:
        if ((r7 instanceof boolean[]) == false) goto L44;
        r0.putBooleanArray(r6, (boolean[]) r7);
        goto L92
    L44:
        if ((r7 instanceof byte[]) == false) goto L47;
        r0.putByteArray(r6, (byte[]) r7);
        goto L92
    L47:
        if ((r7 instanceof char[]) == false) goto L50;
        r0.putCharArray(r6, (char[]) r7);
        goto L92
    L50:
        if ((r7 instanceof double[]) == false) goto L53;
        r0.putDoubleArray(r6, (double[]) r7);
        goto L92
    L53:
        if ((r7 instanceof float[]) == false) goto L56;
        r0.putFloatArray(r6, (float[]) r7);
        goto L92
    L56:
        if ((r7 instanceof int[]) == false) goto L59;
        r0.putIntArray(r6, (int[]) r7);
        goto L92
    L59:
        if ((r7 instanceof long[]) == false) goto L62;
        r0.putLongArray(r6, (long[]) r7);
        goto L92
    L62:
        if ((r7 instanceof short[]) == false) goto L65;
        r0.putShortArray(r6, (short[]) r7);
        goto L92
    L65:
        if ((r7 instanceof Object[]) == false) goto L81;
        Class<?> r8 = r7.getClass().getComponentType();
        Intrinsics.checkNotNull(r8);
        if (Parcelable.class.isAssignableFrom(r8) == false) goto L70;
        Intrinsics.checkNotNull(r7, "null cannot be cast to non-null type kotlin.Array<android.os.Parcelable>");
        r0.putParcelableArray(r6, (Parcelable[]) r7);
        goto L92
    L70:
        if (String.class.isAssignableFrom(r8) == false) goto L73;
        Intrinsics.checkNotNull(r7, "null cannot be cast to non-null type kotlin.Array<kotlin.String>");
        r0.putStringArray(r6, (String[]) r7);
        goto L92
    L73:
        if (CharSequence.class.isAssignableFrom(r8) == false) goto L76;
        Intrinsics.checkNotNull(r7, "null cannot be cast to non-null type kotlin.Array<kotlin.CharSequence>");
        r0.putCharSequenceArray(r6, (CharSequence[]) r7);
        goto L92
    L76:
        if (Serializable.class.isAssignableFrom(r8) == false) goto L79;
        r0.putSerializable(r6, (Serializable) r7);
        goto L92
    L79:
        throw new IllegalArgumentException("Illegal value array type " + r8.getCanonicalName() + " for key \"" + r6 + Typography.quote);
    L81:
        if ((r7 instanceof Serializable) == false) goto L84;
        r0.putSerializable(r6, (Serializable) r7);
        goto L92
    L84:
        if ((r7 instanceof IBinder) == false) goto L87;
        BundleApi18ImplKt.putBinder(r0, r6, (IBinder) r7);
        goto L92
    L87:
        if ((r7 instanceof Size) == false) goto L90;
        BundleApi21ImplKt.putSize(r0, r6, (Size) r7);
        goto L92
    L90:
        if ((r7 instanceof SizeF) == false) goto L94;
        BundleApi21ImplKt.putSizeF(r0, r6, (SizeF) r7);
        goto L92
    L94:
        throw new IllegalArgumentException("Illegal value type " + r7.getClass().getCanonicalName() + " for key \"" + r6 + Typography.quote);
    L96:
        return r0;
    }
}
