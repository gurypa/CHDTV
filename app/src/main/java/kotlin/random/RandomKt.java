package kotlin.random;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongRange;

/* JADX INFO: compiled from: Random.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0007\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\fH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000\u001a\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0000\u001a\u0014\u0010\u000f\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0014\u0010\u0012\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0013H\u0007\u001a\u0014\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0000¨\u0006\u0016"}, m421d2 = {"Random", "Lkotlin/random/Random;", "seed", "", "", "boundsErrorMessage", "", "from", "", "until", "checkRangeBounds", "", "", "fastLog2", "value", "nextInt", "range", "Lkotlin/ranges/IntRange;", "nextLong", "Lkotlin/ranges/LongRange;", "takeUpperBits", "bitCount", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class RandomKt {
    public static final Random Random(int r2) {
        return new XorWowRandom(r2, r2 >> 31);
    }

    public static final Random Random(long r4) {
        return new XorWowRandom((int) r4, (int) (r4 >> 32));
    }

    public static final String boundsErrorMessage(Object r2, Object r3) {
        Intrinsics.checkNotNullParameter(r2, "from");
        Intrinsics.checkNotNullParameter(r3, "until");
        return "Random range is empty: [" + r2 + ", " + r3 + ").";
    }

    public static final void checkRangeBounds(double r3, double r5) {
        if (r5 <= r3) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return;
    L9:
        throw new IllegalArgumentException(boundsErrorMessage(Double.valueOf(r3), Double.valueOf(r5)).toString());
    L5:
        r0 = false;
        goto L6
    }

    public static final void checkRangeBounds(int r3, int r4) {
        if (r4 <= r3) goto L4;
        boolean r0 = true;
    L5:
        if (r0 == false) goto L8;
        return;
    L8:
        throw new IllegalArgumentException(boundsErrorMessage(Integer.valueOf(r3), Integer.valueOf(r4)).toString());
    L4:
        r0 = false;
        goto L5
    }

    public static final void checkRangeBounds(long r3, long r5) {
        if (r5 <= r3) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return;
    L9:
        throw new IllegalArgumentException(boundsErrorMessage(Long.valueOf(r3), Long.valueOf(r5)).toString());
    L5:
        r0 = false;
        goto L6
    }

    public static final int fastLog2(int r1) {
        return 31 - Integer.numberOfLeadingZeros(r1);
    }

    public static final int nextInt(Random r3, IntRange r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "range");
        if (r4.isEmpty() == true) goto L13;
        if (r4.getLast() >= Integer.MAX_VALUE) goto L8;
        return r3.nextInt(r4.getFirst(), r4.getLast() + 1);
    L8:
        if (r4.getFirst() <= Integer.MIN_VALUE) goto L11;
        return r3.nextInt(r4.getFirst() - 1, r4.getLast()) + 1;
    L11:
        return r3.nextInt();
    L13:
        throw new IllegalArgumentException("Cannot get random in empty range: " + r4);
    }

    public static final long nextLong(Random r7, LongRange r8) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "range");
        if (r8.isEmpty() == true) goto L13;
        if (r8.getLast() >= Long.MAX_VALUE) goto L8;
        return r7.nextLong(r8.getFirst(), r8.getLast() + 1);
    L8:
        if (r8.getFirst() <= Long.MIN_VALUE) goto L11;
        return 1 + r7.nextLong(r8.getFirst() - 1, r8.getLast());
    L11:
        return r7.nextLong();
    L13:
        throw new IllegalArgumentException("Cannot get random in empty range: " + r8);
    }

    public static final int takeUpperBits(int r2, int r3) {
        return (r2 >>> (32 - r3)) & ((-r3) >> 31);
    }
}
