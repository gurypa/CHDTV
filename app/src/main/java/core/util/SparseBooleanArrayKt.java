package androidx.core.util;

import android.util.SparseBooleanArray;
import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.BooleanIterator;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SparseBooleanArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000>\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0086\n\u001a\u0015\u0010\b\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010\t\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\n\u001a\u00020\u0006H\u0086\b\u001aH\u0010\u000b\u001a\u00020\f*\u00020\u000226\u0010\r\u001a2\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0007\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\f0\u000eH\u0086\bø\u0001\u0000\u001a\u001d\u0010\u0011\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0006H\u0086\b\u001a&\u0010\u0013\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014H\u0086\bø\u0001\u0000\u001a\r\u0010\u0015\u001a\u00020\u0006*\u00020\u0002H\u0086\b\u001a\r\u0010\u0016\u001a\u00020\u0006*\u00020\u0002H\u0086\b\u001a\n\u0010\u0017\u001a\u00020\u0018*\u00020\u0002\u001a\u0015\u0010\u0019\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002H\u0086\u0002\u001a\u0012\u0010\u001b\u001a\u00020\f*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u001a\u0010\u001c\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0006\u001a\u001d\u0010\u001d\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0006H\u0086\n\u001a\n\u0010\u001e\u001a\u00020\u001f*\u00020\u0002\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006 "}, m421d2 = {MediaInformation.KEY_SIZE, "", "Landroid/util/SparseBooleanArray;", "getSize", "(Landroid/util/SparseBooleanArray;)I", "contains", "", "key", "containsKey", "containsValue", "value", "forEach", "", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "getOrDefault", "defaultValue", "getOrElse", "Lkotlin/Function0;", "isEmpty", "isNotEmpty", "keyIterator", "Lkotlin/collections/IntIterator;", "plus", "other", "putAll", "remove", "set", "valueIterator", "Lkotlin/collections/BooleanIterator;", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class SparseBooleanArrayKt {
    public static final boolean contains(SparseBooleanArray r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.indexOfKey(r3) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean containsKey(SparseBooleanArray r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.indexOfKey(r3) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean containsValue(SparseBooleanArray r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.indexOfValue(r3) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final void forEach(SparseBooleanArray r5, Function2<? super Integer, ? super Boolean, Unit> r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "action");
        int r1 = 0;
        int r2 = r5.size();
    L3:
        if (r1 >= r2) goto L5;
        r6.invoke(Integer.valueOf(r5.keyAt(r1)), Boolean.valueOf(r5.valueAt(r1)));
        r1 = r1 + 1;
        goto L3
    }

    public static final boolean getOrDefault(SparseBooleanArray r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.get(r3, r4);
    }

    public static final boolean getOrElse(SparseBooleanArray r4, int r5, Function0<Boolean> r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r6, "defaultValue");
        int r1 = r4.indexOfKey(r5);
        if (r1 < 0) goto L6;
        return r4.valueAt(r1);
    L6:
        return r6.invoke().booleanValue();
    }

    public static final int getSize(SparseBooleanArray r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.size();
    }

    public static final boolean isEmpty(SparseBooleanArray r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean isNotEmpty(SparseBooleanArray r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.size() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final IntIterator keyIterator(final SparseBooleanArray r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C01061(r1);
    }

    public static final SparseBooleanArray plus(SparseBooleanArray r3, SparseBooleanArray r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "other");
        SparseBooleanArray r0 = new SparseBooleanArray(r3.size() + r4.size());
        putAll(r0, r3);
        putAll(r0, r4);
        return r0;
    }

    public static final void putAll(SparseBooleanArray r7, SparseBooleanArray r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "other");
        int r2 = 0;
        int r3 = r8.size();
    L3:
        if (r2 >= r3) goto L6;
        r7.put(r8.keyAt(r2), r8.valueAt(r2));
        r2 = r2 + 1;
        goto L3
    }

    public static final boolean remove(SparseBooleanArray r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        int r0 = r2.indexOfKey(r3);
        if (r0 >= 0) goto L5;
        return false;
    L5:
        if (r4 != r2.valueAt(r0)) goto L10;
        r2.delete(r3);
        return true;
    L10:
        return false;
    }

    public static final void set(SparseBooleanArray r1, int r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.put(r2, r3);
    }

    public static final BooleanIterator valueIterator(final SparseBooleanArray r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C01071(r1);
    }
}
