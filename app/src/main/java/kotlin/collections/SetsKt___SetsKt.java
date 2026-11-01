package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

/* JADX INFO: compiled from: _Sets.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004¨\u0006\r"}, m421d2 = {"minus", "", ExifInterface.GPS_DIRECTION_TRUE, "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/SetsKt")
class SetsKt___SetsKt extends SetsKt__SetsKt {
    public SetsKt___SetsKt() {
    }

    public static final <T> Set<T> minus(Set<? extends T> r8, Iterable<? extends T> r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "elements");
        Collection<?> r0 = CollectionsKt.convertToListIfNotCollection(r9);
        if (r0.isEmpty() == false) goto L7;
        return CollectionsKt.toSet(r8);
    L7:
        if ((r0 instanceof Set) == false) goto L16;
        LinkedHashSet r2 = new LinkedHashSet();
        Iterator<T> r4 = r8.iterator();
    L10:
        if (r4.hasNext() == false) goto L15;
        T r5 = r4.next();
        if (r0.contains(r5) == true) goto L10;
        r2.add(r5);
        goto L10
    L15:
        return r2;
    L16:
        LinkedHashSet r1 = new LinkedHashSet(r8);
        r1.removeAll(r0);
        return r1;
    }

    public static final <T> Set<T> minus(Set<? extends T> r9, T r10) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        LinkedHashSet r0 = new LinkedHashSet(MapsKt.mapCapacity(r9.size()));
        boolean r1 = false;
        Iterator<T> r4 = r9.iterator();
    L4:
        if (r4.hasNext() == false) goto L14;
        T r5 = r4.next();
        if (r1 == false) goto L8;
    L10:
        boolean r8 = true;
    L11:
        if (r8 == false) goto L4;
        r0.add(r5);
        goto L4
    L8:
        if (Intrinsics.areEqual(r5, r10) == false) goto L10;
        r1 = true;
        r8 = false;
        goto L11
    L14:
        return r0;
    }

    public static final <T> Set<T> minus(Set<? extends T> r2, Sequence<? extends T> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "elements");
        LinkedHashSet r0 = new LinkedHashSet(r2);
        CollectionsKt.removeAll(r0, r3);
        return r0;
    }

    public static final <T> Set<T> minus(Set<? extends T> r2, T[] r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "elements");
        LinkedHashSet r0 = new LinkedHashSet(r2);
        CollectionsKt.removeAll(r0, r3);
        return r0;
    }

    private static final <T> Set<T> minusElement(Set<? extends T> r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return SetsKt.minus(r1, r2);
    }

    public static final <T> Set<T> plus(Set<? extends T> r3, Iterable<? extends T> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "elements");
        Integer r0 = CollectionsKt.collectionSizeOrNull(r4);
        if (r0 == null) goto L5;
        int r1 = r0.intValue();
        int r2 = r3.size() + r1;
    L6:
        LinkedHashSet r5 = new LinkedHashSet(MapsKt.mapCapacity(r2));
        r5.addAll(r3);
        CollectionsKt.addAll(r5, r4);
        return r5;
    L5:
        r2 = r3.size() * 2;
        goto L6
    }

    public static final <T> Set<T> plus(Set<? extends T> r2, T r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        LinkedHashSet r0 = new LinkedHashSet(MapsKt.mapCapacity(r2.size() + 1));
        r0.addAll(r2);
        r0.add(r3);
        return r0;
    }

    public static final <T> Set<T> plus(Set<? extends T> r2, Sequence<? extends T> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "elements");
        LinkedHashSet r0 = new LinkedHashSet(MapsKt.mapCapacity(r2.size() * 2));
        r0.addAll(r2);
        CollectionsKt.addAll(r0, r3);
        return r0;
    }

    public static final <T> Set<T> plus(Set<? extends T> r3, T[] r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "elements");
        LinkedHashSet r0 = new LinkedHashSet(MapsKt.mapCapacity(r3.size() + r4.length));
        r0.addAll(r3);
        CollectionsKt.addAll(r0, r4);
        return r0;
    }

    private static final <T> Set<T> plusElement(Set<? extends T> r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return SetsKt.plus(r1, r2);
    }
}
