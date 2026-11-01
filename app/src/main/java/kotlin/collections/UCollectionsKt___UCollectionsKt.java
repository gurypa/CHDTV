package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: _UCollections.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007¢\u0006\u0004\b\u000b\u0010\u0005\u001a\u0017\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007¢\u0006\u0002\u0010\u000f\u001a\u0017\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007¢\u0006\u0002\u0010\u0012\u001a\u0017\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007¢\u0006\u0002\u0010\u0015\u001a\u0017\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007¢\u0006\u0002\u0010\u0018¨\u0006\u0019"}, m421d2 = {"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/UCollectionsKt")
class UCollectionsKt___UCollectionsKt {
    public UCollectionsKt___UCollectionsKt() {
    }

    public static final int sumOfUByte(Iterable<UByte> r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        int r0 = 0;
        Iterator<UByte> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = UInt.m914constructorimpl(UInt.m914constructorimpl(r1.next().m887unboximpl() & 255) + r0);
        goto L4
    L6:
        return r0;
    }

    public static final int sumOfUInt(Iterable<UInt> r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        int r0 = 0;
        Iterator<UInt> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = UInt.m914constructorimpl(r0 + r1.next().m966unboximpl());
        goto L4
    L6:
        return r0;
    }

    public static final long sumOfULong(Iterable<ULong> r7) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        long r0 = 0;
        Iterator<ULong> r2 = r7.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        r0 = ULong.m993constructorimpl(r0 + r2.next().m1045unboximpl());
        goto L4
    L6:
        return r0;
    }

    public static final int sumOfUShort(Iterable<UShort> r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        int r0 = 0;
        Iterator<UShort> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0 = UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r1.next().m1150unboximpl()) + r0);
        goto L4
    L6:
        return r0;
    }

    public static final byte[] toUByteArray(Collection<UByte> r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        byte[] r0 = UByteArray.m889constructorimpl(r5.size());
        int r1 = 0;
        Iterator<UByte> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        UByteArray.m900setVurrAj0(r0, r1, r2.next().m887unboximpl());
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static final int[] toUIntArray(Collection<UInt> r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        int[] r0 = UIntArray.m968constructorimpl(r5.size());
        int r1 = 0;
        Iterator<UInt> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        UIntArray.m979setVXSXFK8(r0, r1, r2.next().m966unboximpl());
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static final long[] toULongArray(Collection<ULong> r6) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        long[] r0 = ULongArray.m1047constructorimpl(r6.size());
        int r1 = 0;
        Iterator<ULong> r2 = r6.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        ULongArray.m1058setk8EXiF4(r0, r1, r2.next().m1045unboximpl());
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static final short[] toUShortArray(Collection<UShort> r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        short[] r0 = UShortArray.m1152constructorimpl(r5.size());
        int r1 = 0;
        Iterator<UShort> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L6;
        UShortArray.m1163set01HTLdE(r0, r1, r2.next().m1150unboximpl());
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }
}
