package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: _USequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u0019\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007¢\u0006\u0004\b\u000b\u0010\u0005¨\u0006\f"}, m421d2 = {"sum", "Lkotlin/UInt;", "Lkotlin/sequences/Sequence;", "Lkotlin/UByte;", "sumOfUByte", "(Lkotlin/sequences/Sequence;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Lkotlin/sequences/Sequence;)J", "Lkotlin/UShort;", "sumOfUShort", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/sequences/USequencesKt")
class USequencesKt___USequencesKt {
    public USequencesKt___USequencesKt() {
    }

    public static final int sumOfUByte(Sequence<UByte> r4) {
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

    public static final int sumOfUInt(Sequence<UInt> r4) {
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

    public static final long sumOfULong(Sequence<ULong> r7) {
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

    public static final int sumOfUShort(Sequence<UShort> r4) {
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
}
