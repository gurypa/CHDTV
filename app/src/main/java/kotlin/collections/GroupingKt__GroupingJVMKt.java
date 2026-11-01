package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.TypeIntrinsics;

/* JADX INFO: compiled from: GroupingJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aZ\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0081\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, m421d2 = {"eachCount", "", "K", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "f", "Lkotlin/Function1;", "", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/GroupingKt")
class GroupingKt__GroupingJVMKt {
    public GroupingKt__GroupingJVMKt() {
    }

    public static final <T, K> Map<K, Integer> eachCount(Grouping<T, ? extends K> r20) {
        Intrinsics.checkNotNullParameter(r20, "<this>");
        LinkedHashMap r0 = new LinkedHashMap();
        Grouping<T, ? extends K> r2 = r20;
        Iterator<T> r6 = r2.sourceIterator();
    L4:
        if (r6.hasNext() == false) goto L16;
        K r8 = r2.keyOf(r6.next());
        Object r9 = r0.get(r8);
        if (r9 == null) goto L8;
    L10:
        boolean r11 = false;
    L12:
        if (r11 == false) goto L14;
        Object r17 = new Ref.IntRef();
    L15:
        Ref.IntRef r10 = (Ref.IntRef) r17;
        r10.element++;
        r0.put(r8, r10);
        r2 = r2;
        goto L4
    L14:
        r17 = r9;
        goto L15
    L8:
        if (r0.containsKey(r8) == true) goto L10;
        r11 = true;
        goto L12
    L16:
        Iterator<T> r1 = r0.entrySet().iterator();
    L18:
        if (r1.hasNext() == false) goto L21;
        Map.Entry r3 = (Map.Entry) r1.next();
        Intrinsics.checkNotNull(r3, "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4, R of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4>");
        TypeIntrinsics.asMutableMapEntry(r3).setValue(Integer.valueOf(((Ref.IntRef) r3.getValue()).element));
        goto L18
    L21:
        return TypeIntrinsics.asMutableMap(r0);
    }

    private static final <K, V, R> Map<K, R> mapValuesInPlace(Map<K, V> r8, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "f");
        Iterator<T> r2 = r8.entrySet().iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        Map.Entry r4 = (Map.Entry) r2.next();
        Intrinsics.checkNotNull(r4, "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4, R of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4>");
        TypeIntrinsics.asMutableMapEntry(r4).setValue(r9.invoke(r4));
        goto L4
    L7:
        return TypeIntrinsics.asMutableMap(r8);
    }
}
