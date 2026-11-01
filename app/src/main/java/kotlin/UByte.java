package kotlin;

import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.primitives.C0871x7f2744bd;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.URangesKt;

/* JADX INFO: compiled from: UByte.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0005\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 v2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001vB\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u000e\u0010\u000fJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u0011\u0010\u0012J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b\u001c\u0010\u000fJ\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u001d\u0010\u0012J\u0018\u0010\u001b\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b'\u0010\u000fJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b(\u0010\u0012J\u0018\u0010&\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b)\u0010\u001fJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0013\u0010.\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b/\u0010\u0005J\u0013\u00100\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u0018\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b3\u0010\u000fJ\u0018\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b4\u0010\u0012J\u0018\u00102\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b5\u0010\u001fJ\u0018\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\b6\u0010\u0018J\u0018\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b8\u0010\u000bJ\u0018\u00107\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b9\u0010\u0012J\u0018\u00107\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b:\u0010\u001fJ\u0018\u00107\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\b¢\u0006\u0004\b;\u0010<J\u0018\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\b>\u0010\u000bJ\u0018\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b@\u0010\u000fJ\u0018\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\bA\u0010\u0012J\u0018\u0010?\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\bB\u0010\u001fJ\u0018\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bC\u0010\u0018J\u0018\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bF\u0010GJ\u0018\u0010H\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bI\u0010GJ\u0018\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bK\u0010\u000fJ\u0018\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\bL\u0010\u0012J\u0018\u0010J\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\bM\u0010\u001fJ\u0018\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bN\u0010\u0018J\u0018\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bP\u0010\u000fJ\u0018\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\bQ\u0010\u0012J\u0018\u0010O\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\bR\u0010\u001fJ\u0018\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\n¢\u0006\u0004\bS\u0010\u0018J\u0010\u0010T\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bU\u0010\u0005J\u0010\u0010V\u001a\u00020WH\u0087\b¢\u0006\u0004\bX\u0010YJ\u0010\u0010Z\u001a\u00020[H\u0087\b¢\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020\rH\u0087\b¢\u0006\u0004\b_\u0010-J\u0010\u0010`\u001a\u00020aH\u0087\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020eH\u0087\b¢\u0006\u0004\bf\u0010gJ\u000f\u0010h\u001a\u00020iH\u0016¢\u0006\u0004\bj\u0010kJ\u0013\u0010l\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bm\u0010\u0005J\u0013\u0010n\u001a\u00020\u0010H\u0087\bø\u0001\u0000¢\u0006\u0004\bo\u0010-J\u0013\u0010p\u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\bq\u0010cJ\u0013\u0010r\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\bs\u0010gJ\u0018\u0010t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bu\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u0004\n\u0002\b!¨\u0006w"}, m421d2 = {"Lkotlin/UByte;", "", "data", "", "constructor-impl", "(B)B", "getData$annotations", "()V", "and", "other", "and-7apg3OU", "(BB)B", "compareTo", "", "compareTo-7apg3OU", "(BB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(BI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(BJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(BS)I", "dec", "dec-w2LRezQ", TtmlNode.TAG_DIV, "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(BJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(BLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(B)I", "inc", "inc-w2LRezQ", "inv", "inv-w2LRezQ", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(BS)S", "or", "or-7apg3OU", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-7apg3OU", "(BB)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-7apg3OU", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "toByte-impl", "toDouble", "", "toDouble-impl", "(B)D", "toFloat", "", "toFloat-impl", "(B)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(B)J", "toShort", "", "toShort-impl", "(B)S", "toString", "", "toString-impl", "(B)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-7apg3OU", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
@JvmInline
public final class UByte implements Comparable<UByte> {
    public static final Companion Companion = null;
    public static final byte MAX_VALUE = -1;
    public static final byte MIN_VALUE = 0;
    public static final int SIZE_BITS = 8;
    public static final int SIZE_BYTES = 1;
    private final byte data;

    /* JADX INFO: compiled from: UByte.kt */
    @Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\n"}, m421d2 = {"Lkotlin/UByte$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UByte;", "B", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
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

    private /* synthetic */ UByte(byte r1) {
        this.data = r1;
    }

    /* JADX INFO: renamed from: and-7apg3OU, reason: not valid java name */
    private static final byte m830and7apg3OU(byte r1, byte r2) {
        return m837constructorimpl((byte) (r1 & r2));
    }

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ UByte m831boximpl(byte r1) {
        return new UByte(r1);
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private int m832compareTo7apg3OU(byte r3) {
        return Intrinsics.compare(m887unboximpl() & 255, r3 & 255);
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static int m833compareTo7apg3OU(byte r2, byte r3) {
        return Intrinsics.compare(r2 & 255, r3 & 255);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m834compareToVKZWuLQ(byte r4, long r5) {
        return C0871x7f2744bd.m371m(ULong.m993constructorimpl(((long) r4) & 255), r5);
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m835compareToWZ4Q5Ns(byte r1, int r2) {
        return UByte$$ExternalSyntheticBackport4.m433m(UInt.m914constructorimpl(r1 & 255), r2);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m836compareToxj2QHRw(byte r2, short r3) {
        return Intrinsics.compare(r2 & 255, 65535 & r3);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static byte m837constructorimpl(byte r0) {
        return r0;
    }

    /* JADX INFO: renamed from: dec-w2LRezQ, reason: not valid java name */
    private static final byte m838decw2LRezQ(byte r1) {
        return m837constructorimpl((byte) (r1 - 1));
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m839div7apg3OU(byte r2, byte r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m840divVKZWuLQ(byte r4, long r5) {
        return UByte$$ExternalSyntheticBackport3.m432m(ULong.m993constructorimpl(((long) r4) & 255), r5);
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m841divWZ4Q5Ns(byte r1, int r2) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r1 & 255), r2);
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m842divxj2QHRw(byte r2, short r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m843equalsimpl(byte r2, Object r3) {
        if ((r3 instanceof UByte) == true) goto L6;
        return false;
    L6:
        if (r2 == ((UByte) r3).m887unboximpl()) goto L8;
        return false;
    L8:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m844equalsimpl0(byte r1, byte r2) {
        if (r1 != r2) goto L4;
        return true;
    L4:
        return false;
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m845floorDiv7apg3OU(byte r2, byte r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m846floorDivVKZWuLQ(byte r4, long r5) {
        return UByte$$ExternalSyntheticBackport3.m432m(ULong.m993constructorimpl(((long) r4) & 255), r5);
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m847floorDivWZ4Q5Ns(byte r1, int r2) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r1 & 255), r2);
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m848floorDivxj2QHRw(byte r2, short r3) {
        return UByte$$ExternalSyntheticBackport0.m429m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(65535 & r3));
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m849hashCodeimpl(byte r0) {
        return r0;
    }

    /* JADX INFO: renamed from: inc-w2LRezQ, reason: not valid java name */
    private static final byte m850incw2LRezQ(byte r1) {
        return m837constructorimpl((byte) (r1 + 1));
    }

    /* JADX INFO: renamed from: inv-w2LRezQ, reason: not valid java name */
    private static final byte m851invw2LRezQ(byte r1) {
        return m837constructorimpl((byte) (~r1));
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m852minus7apg3OU(byte r2, byte r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & 255) - UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m853minusVKZWuLQ(byte r4, long r5) {
        return ULong.m993constructorimpl(ULong.m993constructorimpl(((long) r4) & 255) - r5);
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m854minusWZ4Q5Ns(byte r1, int r2) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r1 & 255) - r2);
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m855minusxj2QHRw(byte r2, short r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & 255) - UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m856mod7apg3OU(byte r2, byte r3) {
        return m837constructorimpl((byte) UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(r3 & 255)));
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m857modVKZWuLQ(byte r4, long r5) {
        return UByte$$ExternalSyntheticBackport2.m431m(ULong.m993constructorimpl(((long) r4) & 255), r5);
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m858modWZ4Q5Ns(byte r1, int r2) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r1 & 255), r2);
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m859modxj2QHRw(byte r2, short r3) {
        return UShort.m1100constructorimpl((short) UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(65535 & r3)));
    }

    /* JADX INFO: renamed from: or-7apg3OU, reason: not valid java name */
    private static final byte m860or7apg3OU(byte r1, byte r2) {
        return m837constructorimpl((byte) (r1 | r2));
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m861plus7apg3OU(byte r2, byte r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & 255) + UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m862plusVKZWuLQ(byte r4, long r5) {
        return ULong.m993constructorimpl(ULong.m993constructorimpl(((long) r4) & 255) + r5);
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m863plusWZ4Q5Ns(byte r1, int r2) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r1 & 255) + r2);
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m864plusxj2QHRw(byte r2, short r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & 255) + UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: rangeTo-7apg3OU, reason: not valid java name */
    private static final UIntRange m865rangeTo7apg3OU(byte r4, byte r5) {
        return new UIntRange(UInt.m914constructorimpl(r4 & 255), UInt.m914constructorimpl(r5 & 255), null);
    }

    /* JADX INFO: renamed from: rangeUntil-7apg3OU, reason: not valid java name */
    private static final UIntRange m866rangeUntil7apg3OU(byte r2, byte r3) {
        return URangesKt.m2087untilJ1ME1BU(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m867rem7apg3OU(byte r2, byte r3) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m868remVKZWuLQ(byte r4, long r5) {
        return UByte$$ExternalSyntheticBackport2.m431m(ULong.m993constructorimpl(((long) r4) & 255), r5);
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m869remWZ4Q5Ns(byte r1, int r2) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r1 & 255), r2);
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m870remxj2QHRw(byte r2, short r3) {
        return UByte$$ExternalSyntheticBackport1.m430m(UInt.m914constructorimpl(r2 & 255), UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m871times7apg3OU(byte r2, byte r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & 255) * UInt.m914constructorimpl(r3 & 255));
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m872timesVKZWuLQ(byte r4, long r5) {
        return ULong.m993constructorimpl(ULong.m993constructorimpl(((long) r4) & 255) * r5);
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m873timesWZ4Q5Ns(byte r1, int r2) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r1 & 255) * r2);
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m874timesxj2QHRw(byte r2, short r3) {
        return UInt.m914constructorimpl(UInt.m914constructorimpl(r2 & 255) * UInt.m914constructorimpl(65535 & r3));
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m875toByteimpl(byte r0) {
        return r0;
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m876toDoubleimpl(byte r2) {
        return r2 & 255;
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m877toFloatimpl(byte r1) {
        return r1 & 255;
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m878toIntimpl(byte r1) {
        return r1 & 255;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m879toLongimpl(byte r4) {
        return ((long) r4) & 255;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m880toShortimpl(byte r1) {
        return (short) (r1 & 255);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m881toStringimpl(byte r1) {
        return String.valueOf(r1 & 255);
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m882toUBytew2LRezQ(byte r0) {
        return r0;
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m883toUIntpVg5ArA(byte r1) {
        return UInt.m914constructorimpl(r1 & 255);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m884toULongsVKNKU(byte r4) {
        return ULong.m993constructorimpl(((long) r4) & 255);
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m885toUShortMh2AYeg(byte r1) {
        return UShort.m1100constructorimpl((short) (r1 & 255));
    }

    /* JADX INFO: renamed from: xor-7apg3OU, reason: not valid java name */
    private static final byte m886xor7apg3OU(byte r1, byte r2) {
        return m837constructorimpl((byte) (r1 ^ r2));
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UByte r3) {
        byte r0 = r3.m887unboximpl();
        return Intrinsics.compare(m887unboximpl() & 255, r0 & 255);
    }

    public boolean equals(Object r2) {
        return m843equalsimpl(this.data, r2);
    }

    public int hashCode() {
        return m849hashCodeimpl(this.data);
    }

    public String toString() {
        return m881toStringimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ byte m887unboximpl() {
        return this.data;
    }
}
