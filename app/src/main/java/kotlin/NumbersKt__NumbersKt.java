package kotlin;

/* JADX INFO: compiled from: Numbers.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0005\n\u0002\u0010\n\n\u0002\b\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0003H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0003H\u0087\b\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0003H\u0087\b\u001a\u0014\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\u0006\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\b\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\r\u0010\t\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\t\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u001a\r\u0010\n\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\n\u001a\u00020\u0003*\u00020\u0003H\u0087\b¨\u0006\u000b"}, m421d2 = {"countLeadingZeroBits", "", "", "", "countOneBits", "countTrailingZeroBits", "rotateLeft", "bitCount", "rotateRight", "takeHighestOneBit", "takeLowestOneBit", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/NumbersKt")
class NumbersKt__NumbersKt extends NumbersKt__NumbersJVMKt {
    public NumbersKt__NumbersKt() {
    }

    private static final int countLeadingZeroBits(byte r1) {
        return Integer.numberOfLeadingZeros(r1 & 255) - 24;
    }

    private static final int countLeadingZeroBits(short r1) {
        return Integer.numberOfLeadingZeros(65535 & r1) - 16;
    }

    private static final int countOneBits(byte r1) {
        return Integer.bitCount(r1 & 255);
    }

    private static final int countOneBits(short r1) {
        return Integer.bitCount(65535 & r1);
    }

    private static final int countTrailingZeroBits(byte r1) {
        return Integer.numberOfTrailingZeros(r1 | 256);
    }

    private static final int countTrailingZeroBits(short r1) {
        return Integer.numberOfTrailingZeros(65536 | r1);
    }

    public static final byte rotateLeft(byte r3, int r4) {
        return (byte) ((r3 << (r4 & 7)) | ((r3 & 255) >>> (8 - (r4 & 7))));
    }

    public static final short rotateLeft(short r3, int r4) {
        return (short) ((r3 << (r4 & 15)) | ((65535 & r3) >>> (16 - (r4 & 15))));
    }

    public static final byte rotateRight(byte r3, int r4) {
        return (byte) ((r3 << (8 - (r4 & 7))) | ((r3 & 255) >>> (r4 & 7)));
    }

    public static final short rotateRight(short r3, int r4) {
        return (short) ((r3 << (16 - (r4 & 15))) | ((65535 & r3) >>> (r4 & 15)));
    }

    private static final byte takeHighestOneBit(byte r1) {
        return (byte) Integer.highestOneBit(r1 & 255);
    }

    private static final short takeHighestOneBit(short r1) {
        return (short) Integer.highestOneBit(65535 & r1);
    }

    private static final byte takeLowestOneBit(byte r1) {
        return (byte) Integer.lowestOneBit(r1);
    }

    private static final short takeLowestOneBit(short r1) {
        return (short) Integer.lowestOneBit(r1);
    }
}
