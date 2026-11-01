package kotlin;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.URangesKt;

/* JADX INFO: compiled from: UShort.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\n\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 v2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001vB\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u0012\u0010\u0013J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b\u0015\u0010\u0016J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b\u001c\u0010\u0010J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b\u001d\u0010\u0013J\u0018\u0010\u001b\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u0018\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b'\u0010\u0010J\u0018\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b(\u0010\u0013J\u0018\u0010&\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\b¢\u0006\u0004\b)\u0010\u001fJ\u0018\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0013\u0010.\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b/\u0010\u0005J\u0013\u00100\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u0018\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b3\u0010\u0010J\u0018\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\b4\u0010\u0013J\u0018\u00102\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\b5\u0010\u001fJ\u0018\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b6\u0010\u0018J\u0018\u00107\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\b¢\u0006\u0004\b8\u00109J\u0018\u00107\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\b¢\u0006\u0004\b:\u0010\u0013J\u0018\u00107\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\b¢\u0006\u0004\b;\u0010\u001fJ\u0018\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b<\u0010\u000bJ\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b>\u0010\u000bJ\u0018\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\b@\u0010\u0010J\u0018\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bA\u0010\u0013J\u0018\u0010?\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bB\u0010\u001fJ\u0018\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bC\u0010\u0018J\u0018\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bF\u0010GJ\u0018\u0010H\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bI\u0010GJ\u0018\u0010J\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bK\u0010\u0010J\u0018\u0010J\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bL\u0010\u0013J\u0018\u0010J\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bM\u0010\u001fJ\u0018\u0010J\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bN\u0010\u0018J\u0018\u0010O\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\n¢\u0006\u0004\bP\u0010\u0010J\u0018\u0010O\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n¢\u0006\u0004\bQ\u0010\u0013J\u0018\u0010O\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\n¢\u0006\u0004\bR\u0010\u001fJ\u0018\u0010O\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bS\u0010\u0018J\u0010\u0010T\u001a\u00020UH\u0087\b¢\u0006\u0004\bV\u0010WJ\u0010\u0010X\u001a\u00020YH\u0087\b¢\u0006\u0004\bZ\u0010[J\u0010\u0010\\\u001a\u00020]H\u0087\b¢\u0006\u0004\b^\u0010_J\u0010\u0010`\u001a\u00020\rH\u0087\b¢\u0006\u0004\ba\u0010-J\u0010\u0010b\u001a\u00020cH\u0087\b¢\u0006\u0004\bd\u0010eJ\u0010\u0010f\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bg\u0010\u0005J\u000f\u0010h\u001a\u00020iH\u0016¢\u0006\u0004\bj\u0010kJ\u0013\u0010l\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\bm\u0010WJ\u0013\u0010n\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\bo\u0010-J\u0013\u0010p\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\bq\u0010eJ\u0013\u0010r\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bs\u0010\u0005J\u0018\u0010t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bu\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006w"}, m421d2 = {"Lkotlin/UShort;", "", "data", "", "constructor-impl", "(S)S", "getData$annotations", "()V", "and", "other", "and-xj2QHRw", "(SS)S", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(SB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(SI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(SJ)I", "compareTo-xj2QHRw", "(SS)I", "dec", "dec-Mh2AYeg", TtmlNode.TAG_DIV, "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(SJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(SLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(S)I", "inc", "inc-Mh2AYeg", "inv", "inv-Mh2AYeg", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(SB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "or", "or-xj2QHRw", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-xj2QHRw", "(SS)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-xj2QHRw", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(S)B", "toDouble", "", "toDouble-impl", "(S)D", "toFloat", "", "toFloat-impl", "(S)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(S)J", "toShort", "toShort-impl", "toString", "", "toString-impl", "(S)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-xj2QHRw", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
@JvmInline
public final class UShort implements Comparable<UShort> {
    public static final Companion Companion = null;
    public static final short MAX_VALUE = -1;
    public static final short MIN_VALUE = 0;
    public static final int SIZE_BITS = 16;
    public static final int SIZE_BYTES = 2;
    private final short data;

    /* JADX INFO: compiled from: UShort.kt */
    @Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, m421d2 = {"Lkotlin/UShort$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UShort;", ExifInterface.LATITUDE_SOUTH, "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    static {
        Companion = new Companion(null);
    }

    private /* synthetic */ UShort(short r1) {
        this.data = r1;
    }

    /* JADX INFO: renamed from: and-xj2QHRw, reason: not valid java name */
    private static final short m1093andxj2QHRw(short r1, short r2) {
        return m1100constructorimpl((short) (r1 & r2));
    }

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ UShort m1094boximpl(short r1) {
        return new UShort(r1);
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static final int m1095compareTo7apg3OU(short r2, byte r3) {
        return Intrinsics.compare(65535 & r2, r3 & 255);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m1096compareToVKZWuLQ(short r4, long r5) {
        return C0871x7f2744bd.m371m(ULong.m993constructorimpl(((long) r4) & 65535), r5);
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m1097compareToWZ4Q5Ns(short r1, int r2) {
        return UByte$$ExternalSyntheticBackport4.m433m(UInt.m914constructorimpl(65535 & r1), r2);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private int m1098compareToxj2QHRw(short r3) {
        return Intrinsics.compare(m1150unboximpl() & MAX_VALUE, 65535 & r3);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static int m1099compareToxj2QHRw(short r2, short r3) {
        return Intrinsics.compare(r2 & MAX_VALUE, 65535 & r3);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static short m1100constructorimpl(short r0) {
        return r0;
    }

    /* JADX INFO: renamed from: dec-Mh2AYeg, reason: not valid java name */
    private static final short m1101decMh2AYeg(short r1) {
        return m1100constructorimpl((short) (r1 - 1));
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m1102div7apg3OU(short r2, byte r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(65535 & r2), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m1103divVKZWuLQ(short r4, long r5) {
        return UByte$$ExternalSyntheticBackport3.m432m(ULong.m993constructorimpl(((long) r4) & 65535), r5);
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m1104divWZ4Q5Ns(short r1, int r2) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(65535 & r1), r2);
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m1105divxj2QHRw(short r2, short r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r2 & MAX_VALUE), UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m1106equalsimpl(short r2, Object r3) {
        if ((r3 instanceof UShort) == true) goto L6;
        return false;
    L6:
        if (r2 == ((UShort) r3).m1150unboximpl()) goto L8;
        return false;
    L8:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m1107equalsimpl0(short r1, short r2) {
        if (r1 != r2) goto L4;
        return true;
    L4:
        return false;
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m1108floorDiv7apg3OU(short r2, byte r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(65535 & r2), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m1109floorDivVKZWuLQ(short r4, long r5) {
        return UByte$$ExternalSyntheticBackport3.m432m(ULong.m993constructorimpl(((long) r4) & 65535), r5);
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m1110floorDivWZ4Q5Ns(short r1, int r2) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(65535 & r1), r2);
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m1111floorDivxj2QHRw(short r2, short r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r2 & MAX_VALUE), UInt.m914constructorimpl(65535 & r3));
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m1112hashCodeimpl(short r0) {
        return r0;
    }

    /* JADX INFO: renamed from: inc-Mh2AYeg, reason: not valid java name */
    private static final short m1113incMh2AYeg(short r1) {
        return m1100constructorimpl((short) (r1 + 1));
    }

    /* JADX INFO: renamed from: inv-Mh2AYeg, reason: not valid java name */
    private static final short m1114invMh2AYeg(short r1) {
        return m1100constructorimpl((short) (~r1));
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m1115minus7apg3OU(short r2, byte r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r2) - UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m1116minusVKZWuLQ(short r4, long r5) {
        return ULong.m993constructorimpl(ULong.m993constructorimpl(((long) r4) & 65535) - r5);
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m1117minusWZ4Q5Ns(short r1, int r2) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r1) - r2);
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m1118minusxj2QHRw(short r2, short r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & MAX_VALUE) - UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m1119mod7apg3OU(short r2, byte r3) {
        return UByte.m837constructorimpl((byte) UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(65535 & r2), UInt.m914constructorimpl(r3 & 255)));
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m1120modVKZWuLQ(short r4, long r5) {
        return UByte$$ExternalSyntheticBackport2.m431m(ULong.m993constructorimpl(((long) r4) & 65535), r5);
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m1121modWZ4Q5Ns(short r1, int r2) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(65535 & r1), r2);
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m1122modxj2QHRw(short r2, short r3) {
        return m1100constructorimpl((short) UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r2 & MAX_VALUE), UInt.m914constructorimpl(65535 & r3)));
    }

    /* JADX INFO: renamed from: or-xj2QHRw, reason: not valid java name */
    private static final short m1123orxj2QHRw(short r1, short r2) {
        return m1100constructorimpl((short) (r1 | r2));
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m1124plus7apg3OU(short r2, byte r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r2) + UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m1125plusVKZWuLQ(short r4, long r5) {
        return ULong.m993constructorimpl(ULong.m993constructorimpl(((long) r4) & 65535) + r5);
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m1126plusWZ4Q5Ns(short r1, int r2) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r1) + r2);
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m1127plusxj2QHRw(short r2, short r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & MAX_VALUE) + UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: rangeTo-xj2QHRw, reason: not valid java name */
    private static final UIntRange m1128rangeToxj2QHRw(short r4, short r5) {
        return new UIntRange(UInt.m914constructorimpl(r4 & MAX_VALUE), UInt.m914constructorimpl(65535 & r5), null);
    }

    /* JADX INFO: renamed from: rangeUntil-xj2QHRw, reason: not valid java name */
    private static final UIntRange m1129rangeUntilxj2QHRw(short r2, short r3) {
        return URangesKt.m2087untilJ1ME1BU(UInt.m914constructorimpl(r2 & MAX_VALUE), UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m1130rem7apg3OU(short r2, byte r3) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(65535 & r2), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m1131remVKZWuLQ(short r4, long r5) {
        return UByte$$ExternalSyntheticBackport2.m431m(ULong.m993constructorimpl(((long) r4) & 65535), r5);
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m1132remWZ4Q5Ns(short r1, int r2) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(65535 & r1), r2);
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m1133remxj2QHRw(short r2, short r3) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r2 & MAX_VALUE), UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m1134times7apg3OU(short r2, byte r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r2) * UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m1135timesVKZWuLQ(short r4, long r5) {
        return ULong.m993constructorimpl(ULong.m993constructorimpl(((long) r4) & 65535) * r5);
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m1136timesWZ4Q5Ns(short r1, int r2) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(65535 & r1) * r2);
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m1137timesxj2QHRw(short r2, short r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & MAX_VALUE) * UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m1138toByteimpl(short r1) {
        return (byte) r1;
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m1139toDoubleimpl(short r2) {
        return 65535 & r2;
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m1140toFloatimpl(short r1) {
        return 65535 & r1;
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m1141toIntimpl(short r1) {
        return 65535 & r1;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m1142toLongimpl(short r4) {
        return ((long) r4) & 65535;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m1143toShortimpl(short r0) {
        return r0;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m1144toStringimpl(short r1) {
        return String.valueOf(65535 & r1);
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m1145toUBytew2LRezQ(short r1) {
        return UByte.m837constructorimpl((byte) r1);
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m1146toUIntpVg5ArA(short r1) {
        return UInt.m914constructorimpl(65535 & r1);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m1147toULongsVKNKU(short r4) {
        return ULong.m993constructorimpl(((long) r4) & 65535);
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m1148toUShortMh2AYeg(short r0) {
        return r0;
    }

    /* JADX INFO: renamed from: xor-xj2QHRw, reason: not valid java name */
    private static final short m1149xorxj2QHRw(short r1, short r2) {
        return m1100constructorimpl((short) (r1 ^ r2));
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UShort r4) {
        short r0 = r4.m1150unboximpl();
        return Intrinsics.compare(m1150unboximpl() & MAX_VALUE, r0 & MAX_VALUE);
    }

    public boolean equals(Object r2) {
        return m1106equalsimpl(this.data, r2);
    }

    public int hashCode() {
        return m1112hashCodeimpl(this.data);
    }

    public String toString() {
        return m1144toStringimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ short m1150unboximpl() {
        return this.data;
    }
}
