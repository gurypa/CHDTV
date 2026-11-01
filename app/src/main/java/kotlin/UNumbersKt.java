package kotlin;

/* JADX INFO: compiled from: UNumbers.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b)\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u0005H\u0087\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\bH\u0087\b¢\u0006\u0004\b\t\u0010\n\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u000bH\u0087\b¢\u0006\u0004\b\f\u0010\r\u001a\u0014\u0010\u000e\u001a\u00020\u0001*\u00020\u0002H\u0087\b¢\u0006\u0004\b\u000f\u0010\u0004\u001a\u0014\u0010\u000e\u001a\u00020\u0001*\u00020\u0005H\u0087\b¢\u0006\u0004\b\u0010\u0010\u0007\u001a\u0014\u0010\u000e\u001a\u00020\u0001*\u00020\bH\u0087\b¢\u0006\u0004\b\u0011\u0010\n\u001a\u0014\u0010\u000e\u001a\u00020\u0001*\u00020\u000bH\u0087\b¢\u0006\u0004\b\u0012\u0010\r\u001a\u0014\u0010\u0013\u001a\u00020\u0001*\u00020\u0002H\u0087\b¢\u0006\u0004\b\u0014\u0010\u0004\u001a\u0014\u0010\u0013\u001a\u00020\u0001*\u00020\u0005H\u0087\b¢\u0006\u0004\b\u0015\u0010\u0007\u001a\u0014\u0010\u0013\u001a\u00020\u0001*\u00020\bH\u0087\b¢\u0006\u0004\b\u0016\u0010\n\u001a\u0014\u0010\u0013\u001a\u00020\u0001*\u00020\u000bH\u0087\b¢\u0006\u0004\b\u0017\u0010\r\u001a\u001c\u0010\u0018\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b\u001a\u0010\u001b\u001a\u001c\u0010\u0018\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\b*\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b\u001e\u0010\u001f\u001a\u001c\u0010\u0018\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b \u0010!\u001a\u001c\u0010\"\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b#\u0010\u001b\u001a\u001c\u0010\"\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b$\u0010\u001d\u001a\u001c\u0010\"\u001a\u00020\b*\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b%\u0010\u001f\u001a\u001c\u0010\"\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b&\u0010!\u001a\u0014\u0010'\u001a\u00020\u0002*\u00020\u0002H\u0087\b¢\u0006\u0004\b(\u0010)\u001a\u0014\u0010'\u001a\u00020\u0005*\u00020\u0005H\u0087\b¢\u0006\u0004\b*\u0010\u0007\u001a\u0014\u0010'\u001a\u00020\b*\u00020\bH\u0087\b¢\u0006\u0004\b+\u0010,\u001a\u0014\u0010'\u001a\u00020\u000b*\u00020\u000bH\u0087\b¢\u0006\u0004\b-\u0010.\u001a\u0014\u0010/\u001a\u00020\u0002*\u00020\u0002H\u0087\b¢\u0006\u0004\b0\u0010)\u001a\u0014\u0010/\u001a\u00020\u0005*\u00020\u0005H\u0087\b¢\u0006\u0004\b1\u0010\u0007\u001a\u0014\u0010/\u001a\u00020\b*\u00020\bH\u0087\b¢\u0006\u0004\b2\u0010,\u001a\u0014\u0010/\u001a\u00020\u000b*\u00020\u000bH\u0087\b¢\u0006\u0004\b3\u0010.¨\u00064"}, m421d2 = {"countLeadingZeroBits", "", "Lkotlin/UByte;", "countLeadingZeroBits-7apg3OU", "(B)I", "Lkotlin/UInt;", "countLeadingZeroBits-WZ4Q5Ns", "(I)I", "Lkotlin/ULong;", "countLeadingZeroBits-VKZWuLQ", "(J)I", "Lkotlin/UShort;", "countLeadingZeroBits-xj2QHRw", "(S)I", "countOneBits", "countOneBits-7apg3OU", "countOneBits-WZ4Q5Ns", "countOneBits-VKZWuLQ", "countOneBits-xj2QHRw", "countTrailingZeroBits", "countTrailingZeroBits-7apg3OU", "countTrailingZeroBits-WZ4Q5Ns", "countTrailingZeroBits-VKZWuLQ", "countTrailingZeroBits-xj2QHRw", "rotateLeft", "bitCount", "rotateLeft-LxnNnR4", "(BI)B", "rotateLeft-V7xB4Y4", "(II)I", "rotateLeft-JSWoG40", "(JI)J", "rotateLeft-olVBNx4", "(SI)S", "rotateRight", "rotateRight-LxnNnR4", "rotateRight-V7xB4Y4", "rotateRight-JSWoG40", "rotateRight-olVBNx4", "takeHighestOneBit", "takeHighestOneBit-7apg3OU", "(B)B", "takeHighestOneBit-WZ4Q5Ns", "takeHighestOneBit-VKZWuLQ", "(J)J", "takeHighestOneBit-xj2QHRw", "(S)S", "takeLowestOneBit", "takeLowestOneBit-7apg3OU", "takeLowestOneBit-WZ4Q5Ns", "takeLowestOneBit-VKZWuLQ", "takeLowestOneBit-xj2QHRw", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class UNumbersKt {
    /* JADX INFO: renamed from: countLeadingZeroBits-7apg3OU, reason: not valid java name */
    private static final int m1065countLeadingZeroBits7apg3OU(byte r1) {
        return Integer.numberOfLeadingZeros(r1 & 255) - 24;
    }

    /* JADX INFO: renamed from: countLeadingZeroBits-VKZWuLQ, reason: not valid java name */
    private static final int m1066countLeadingZeroBitsVKZWuLQ(long r1) {
        return Long.numberOfLeadingZeros(r1);
    }

    /* JADX INFO: renamed from: countLeadingZeroBits-WZ4Q5Ns, reason: not valid java name */
    private static final int m1067countLeadingZeroBitsWZ4Q5Ns(int r1) {
        return Integer.numberOfLeadingZeros(r1);
    }

    /* JADX INFO: renamed from: countLeadingZeroBits-xj2QHRw, reason: not valid java name */
    private static final int m1068countLeadingZeroBitsxj2QHRw(short r1) {
        return Integer.numberOfLeadingZeros(65535 & r1) - 16;
    }

    /* JADX INFO: renamed from: countOneBits-7apg3OU, reason: not valid java name */
    private static final int m1069countOneBits7apg3OU(byte r1) {
        return Integer.bitCount(UInt.m914constructorimpl(r1 & 255));
    }

    /* JADX INFO: renamed from: countOneBits-VKZWuLQ, reason: not valid java name */
    private static final int m1070countOneBitsVKZWuLQ(long r1) {
        return Long.bitCount(r1);
    }

    /* JADX INFO: renamed from: countOneBits-WZ4Q5Ns, reason: not valid java name */
    private static final int m1071countOneBitsWZ4Q5Ns(int r1) {
        return Integer.bitCount(r1);
    }

    /* JADX INFO: renamed from: countOneBits-xj2QHRw, reason: not valid java name */
    private static final int m1072countOneBitsxj2QHRw(short r1) {
        return Integer.bitCount(UInt.m914constructorimpl(65535 & r1));
    }

    /* JADX INFO: renamed from: countTrailingZeroBits-7apg3OU, reason: not valid java name */
    private static final int m1073countTrailingZeroBits7apg3OU(byte r1) {
        return Integer.numberOfTrailingZeros(r1 | 256);
    }

    /* JADX INFO: renamed from: countTrailingZeroBits-VKZWuLQ, reason: not valid java name */
    private static final int m1074countTrailingZeroBitsVKZWuLQ(long r1) {
        return Long.numberOfTrailingZeros(r1);
    }

    /* JADX INFO: renamed from: countTrailingZeroBits-WZ4Q5Ns, reason: not valid java name */
    private static final int m1075countTrailingZeroBitsWZ4Q5Ns(int r1) {
        return Integer.numberOfTrailingZeros(r1);
    }

    /* JADX INFO: renamed from: countTrailingZeroBits-xj2QHRw, reason: not valid java name */
    private static final int m1076countTrailingZeroBitsxj2QHRw(short r1) {
        return Integer.numberOfTrailingZeros(65536 | r1);
    }

    /* JADX INFO: renamed from: rotateLeft-JSWoG40, reason: not valid java name */
    private static final long m1077rotateLeftJSWoG40(long r2, int r4) {
        return ULong.m993constructorimpl(Long.rotateLeft(r2, r4));
    }

    /* JADX INFO: renamed from: rotateLeft-LxnNnR4, reason: not valid java name */
    private static final byte m1078rotateLeftLxnNnR4(byte r1, int r2) {
        return UByte.m837constructorimpl(NumbersKt.rotateLeft(r1, r2));
    }

    /* JADX INFO: renamed from: rotateLeft-V7xB4Y4, reason: not valid java name */
    private static final int m1079rotateLeftV7xB4Y4(int r1, int r2) {
        return UInt.m914constructorimpl(Integer.rotateLeft(r1, r2));
    }

    /* JADX INFO: renamed from: rotateLeft-olVBNx4, reason: not valid java name */
    private static final short m1080rotateLeftolVBNx4(short r1, int r2) {
        return UShort.m1100constructorimpl(NumbersKt.rotateLeft(r1, r2));
    }

    /* JADX INFO: renamed from: rotateRight-JSWoG40, reason: not valid java name */
    private static final long m1081rotateRightJSWoG40(long r2, int r4) {
        return ULong.m993constructorimpl(Long.rotateRight(r2, r4));
    }

    /* JADX INFO: renamed from: rotateRight-LxnNnR4, reason: not valid java name */
    private static final byte m1082rotateRightLxnNnR4(byte r1, int r2) {
        return UByte.m837constructorimpl(NumbersKt.rotateRight(r1, r2));
    }

    /* JADX INFO: renamed from: rotateRight-V7xB4Y4, reason: not valid java name */
    private static final int m1083rotateRightV7xB4Y4(int r1, int r2) {
        return UInt.m914constructorimpl(Integer.rotateRight(r1, r2));
    }

    /* JADX INFO: renamed from: rotateRight-olVBNx4, reason: not valid java name */
    private static final short m1084rotateRightolVBNx4(short r1, int r2) {
        return UShort.m1100constructorimpl(NumbersKt.rotateRight(r1, r2));
    }

    /* JADX INFO: renamed from: takeHighestOneBit-7apg3OU, reason: not valid java name */
    private static final byte m1085takeHighestOneBit7apg3OU(byte r1) {
        return UByte.m837constructorimpl((byte) Integer.highestOneBit(r1 & 255));
    }

    /* JADX INFO: renamed from: takeHighestOneBit-VKZWuLQ, reason: not valid java name */
    private static final long m1086takeHighestOneBitVKZWuLQ(long r2) {
        return ULong.m993constructorimpl(Long.highestOneBit(r2));
    }

    /* JADX INFO: renamed from: takeHighestOneBit-WZ4Q5Ns, reason: not valid java name */
    private static final int m1087takeHighestOneBitWZ4Q5Ns(int r1) {
        return UInt.m914constructorimpl(Integer.highestOneBit(r1));
    }

    /* JADX INFO: renamed from: takeHighestOneBit-xj2QHRw, reason: not valid java name */
    private static final short m1088takeHighestOneBitxj2QHRw(short r1) {
        return UShort.m1100constructorimpl((short) Integer.highestOneBit(65535 & r1));
    }

    /* JADX INFO: renamed from: takeLowestOneBit-7apg3OU, reason: not valid java name */
    private static final byte m1089takeLowestOneBit7apg3OU(byte r1) {
        return UByte.m837constructorimpl((byte) Integer.lowestOneBit(r1 & 255));
    }

    /* JADX INFO: renamed from: takeLowestOneBit-VKZWuLQ, reason: not valid java name */
    private static final long m1090takeLowestOneBitVKZWuLQ(long r2) {
        return ULong.m993constructorimpl(Long.lowestOneBit(r2));
    }

    /* JADX INFO: renamed from: takeLowestOneBit-WZ4Q5Ns, reason: not valid java name */
    private static final int m1091takeLowestOneBitWZ4Q5Ns(int r1) {
        return UInt.m914constructorimpl(Integer.lowestOneBit(r1));
    }

    /* JADX INFO: renamed from: takeLowestOneBit-xj2QHRw, reason: not valid java name */
    private static final short m1092takeLowestOneBitxj2QHRw(short r1) {
        return UShort.m1100constructorimpl((short) Integer.lowestOneBit(65535 & r1));
    }
}
