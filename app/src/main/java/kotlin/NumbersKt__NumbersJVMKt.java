package kotlin;

import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: NumbersJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000*\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\t*\u00020\n2\u0006\u0010\b\u001a\u00020\u0001H\u0087\b\u001a\r\u0010\u000b\u001a\u00020\f*\u00020\u0006H\u0087\b\u001a\r\u0010\u000b\u001a\u00020\f*\u00020\tH\u0087\b\u001a\r\u0010\r\u001a\u00020\f*\u00020\u0006H\u0087\b\u001a\r\u0010\r\u001a\u00020\f*\u00020\tH\u0087\b\u001a\r\u0010\u000e\u001a\u00020\f*\u00020\u0006H\u0087\b\u001a\r\u0010\u000e\u001a\u00020\f*\u00020\tH\u0087\b\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0011\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0002*\u00020\u0006H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0001*\u00020\tH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0002*\u00020\u0006H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\tH\u0087\b¨\u0006\u0016"}, m421d2 = {"countLeadingZeroBits", "", "", "countOneBits", "countTrailingZeroBits", "fromBits", "", "Lkotlin/Double$Companion;", "bits", "", "Lkotlin/Float$Companion;", "isFinite", "", "isInfinite", "isNaN", "rotateLeft", "bitCount", "rotateRight", "takeHighestOneBit", "takeLowestOneBit", "toBits", "toRawBits", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/NumbersKt")
class NumbersKt__NumbersJVMKt extends NumbersKt__FloorDivModKt {
    public NumbersKt__NumbersJVMKt() {
    }

    private static final int countLeadingZeroBits(int r1) {
        return Integer.numberOfLeadingZeros(r1);
    }

    private static final int countLeadingZeroBits(long r1) {
        return Long.numberOfLeadingZeros(r1);
    }

    private static final int countOneBits(int r1) {
        return Integer.bitCount(r1);
    }

    private static final int countOneBits(long r1) {
        return Long.bitCount(r1);
    }

    private static final int countTrailingZeroBits(int r1) {
        return Integer.numberOfTrailingZeros(r1);
    }

    private static final int countTrailingZeroBits(long r1) {
        return Long.numberOfTrailingZeros(r1);
    }

    private static final double fromBits(DoubleCompanionObject r2, long r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return Double.longBitsToDouble(r3);
    }

    private static final float fromBits(FloatCompanionObject r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Float.intBitsToFloat(r2);
    }

    private static final boolean isFinite(double r1) {
        if (Double.isInfinite(r1) == false) goto L5;
    L7:
        return false;
    L5:
        if (Double.isNaN(r1) == true) goto L7;
        return true;
    }

    private static final boolean isFinite(float r1) {
        if (Float.isInfinite(r1) == false) goto L5;
    L7:
        return false;
    L5:
        if (Float.isNaN(r1) == true) goto L7;
        return true;
    }

    private static final boolean isInfinite(double r1) {
        return Double.isInfinite(r1);
    }

    private static final boolean isInfinite(float r1) {
        return Float.isInfinite(r1);
    }

    private static final boolean isNaN(double r1) {
        return Double.isNaN(r1);
    }

    private static final boolean isNaN(float r1) {
        return Float.isNaN(r1);
    }

    private static final int rotateLeft(int r1, int r2) {
        return Integer.rotateLeft(r1, r2);
    }

    private static final long rotateLeft(long r2, int r4) {
        return Long.rotateLeft(r2, r4);
    }

    private static final int rotateRight(int r1, int r2) {
        return Integer.rotateRight(r1, r2);
    }

    private static final long rotateRight(long r2, int r4) {
        return Long.rotateRight(r2, r4);
    }

    private static final int takeHighestOneBit(int r1) {
        return Integer.highestOneBit(r1);
    }

    private static final long takeHighestOneBit(long r2) {
        return Long.highestOneBit(r2);
    }

    private static final int takeLowestOneBit(int r1) {
        return Integer.lowestOneBit(r1);
    }

    private static final long takeLowestOneBit(long r2) {
        return Long.lowestOneBit(r2);
    }

    private static final int toBits(float r1) {
        return Float.floatToIntBits(r1);
    }

    private static final long toBits(double r2) {
        return Double.doubleToLongBits(r2);
    }

    private static final int toRawBits(float r1) {
        return Float.floatToRawIntBits(r1);
    }

    private static final long toRawBits(double r2) {
        return Double.doubleToRawLongBits(r2);
    }
}
