package kotlin.text;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UHexExtensions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u0005\u001a\u001c\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\b\u001a\u001c\u0010\t\u001a\u00020\n*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u000b\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u000e\u001a\u001c\u0010\u000f\u001a\u00020\u0010*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u0011\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u0013\u0010\u0014\u001a2\u0010\u0012\u001a\u00020\u0002*\u00020\u00072\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00162\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u0018\u0010\u0019\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\u00072\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u001a\u0010\u001b\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\n2\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u001e\u0010\u001f\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\u00102\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b \u0010!¨\u0006\""}, m421d2 = {"hexToUByte", "Lkotlin/UByte;", "", MediaInformation.KEY_FORMAT_PROPERTIES, "Lkotlin/text/HexFormat;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)B", "hexToUByteArray", "Lkotlin/UByteArray;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)[B", "hexToUInt", "Lkotlin/UInt;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)I", "hexToULong", "Lkotlin/ULong;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)J", "hexToUShort", "Lkotlin/UShort;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)S", "toHexString", "toHexString-ZQbaR00", "(BLkotlin/text/HexFormat;)Ljava/lang/String;", "startIndex", "", "endIndex", "toHexString-lZCiFrA", "([BIILkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-zHuV2wU", "([BLkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-8M7LxHw", "(ILkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-8UJCm-I", "(JLkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-r3ox_E0", "(SLkotlin/text/HexFormat;)Ljava/lang/String;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class UHexExtensionsKt {
    private static final byte hexToUByte(String r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UByte.m837constructorimpl(HexExtensionsKt.hexToByte(r1, r2));
    }

    static /* synthetic */ byte hexToUByte$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UByte.m837constructorimpl(HexExtensionsKt.hexToByte(r0, r1));
    }

    private static final byte[] hexToUByteArray(String r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UByteArray.m890constructorimpl(HexExtensionsKt.hexToByteArray(r1, r2));
    }

    static /* synthetic */ byte[] hexToUByteArray$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UByteArray.m890constructorimpl(HexExtensionsKt.hexToByteArray(r0, r1));
    }

    private static final int hexToUInt(String r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UInt.m914constructorimpl(HexExtensionsKt.hexToInt(r1, r2));
    }

    static /* synthetic */ int hexToUInt$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UInt.m914constructorimpl(HexExtensionsKt.hexToInt(r0, r1));
    }

    private static final long hexToULong(String r2, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return ULong.m993constructorimpl(HexExtensionsKt.hexToLong(r2, r3));
    }

    static /* synthetic */ long hexToULong$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return ULong.m993constructorimpl(HexExtensionsKt.hexToLong(r0, r1));
    }

    private static final short hexToUShort(String r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UShort.m1100constructorimpl(HexExtensionsKt.hexToShort(r1, r2));
    }

    static /* synthetic */ short hexToUShort$default(String r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return UShort.m1100constructorimpl(HexExtensionsKt.hexToShort(r0, r1));
    }

    /* JADX INFO: renamed from: toHexString-8M7LxHw, reason: not valid java name */
    private static final String m2119toHexString8M7LxHw(int r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r1, r2);
    }

    /* JADX INFO: renamed from: toHexString-8M7LxHw$default, reason: not valid java name */
    static /* synthetic */ String m2120toHexString8M7LxHw$default(int r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r0, r1);
    }

    /* JADX INFO: renamed from: toHexString-8UJCm-I, reason: not valid java name */
    private static final String m2121toHexString8UJCmI(long r1, HexFormat r3) {
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r1, r3);
    }

    /* JADX INFO: renamed from: toHexString-8UJCm-I$default, reason: not valid java name */
    static /* synthetic */ String m2122toHexString8UJCmI$default(long r0, HexFormat r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L5;
        r2 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r0, r2);
    }

    /* JADX INFO: renamed from: toHexString-ZQbaR00, reason: not valid java name */
    private static final String m2123toHexStringZQbaR00(byte r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r1, r2);
    }

    /* JADX INFO: renamed from: toHexString-ZQbaR00$default, reason: not valid java name */
    static /* synthetic */ String m2124toHexStringZQbaR00$default(byte r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r0, r1);
    }

    /* JADX INFO: renamed from: toHexString-lZCiFrA, reason: not valid java name */
    private static final String m2125toHexStringlZCiFrA(byte[] r1, int r2, int r3, HexFormat r4) {
        Intrinsics.checkNotNullParameter(r1, "$this$toHexString");
        Intrinsics.checkNotNullParameter(r4, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r1, r2, r3, r4);
    }

    /* JADX INFO: renamed from: toHexString-lZCiFrA$default, reason: not valid java name */
    static /* synthetic */ String m2126toHexStringlZCiFrA$default(byte[] r0, int r1, int r2, HexFormat r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = UByteArray.m896getSizeimpl(r0);
    L9:
        if ((r4 & 4) == 0) goto L11;
        r3 = HexFormat.Companion.getDefault();
    L11:
        Intrinsics.checkNotNullParameter(r0, "$this$toHexString");
        Intrinsics.checkNotNullParameter(r3, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r0, r1, r2, r3);
    }

    /* JADX INFO: renamed from: toHexString-r3ox_E0, reason: not valid java name */
    private static final String m2127toHexStringr3ox_E0(short r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r1, r2);
    }

    /* JADX INFO: renamed from: toHexString-r3ox_E0$default, reason: not valid java name */
    static /* synthetic */ String m2128toHexStringr3ox_E0$default(short r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r0, r1);
    }

    /* JADX INFO: renamed from: toHexString-zHuV2wU, reason: not valid java name */
    private static final String m2129toHexStringzHuV2wU(byte[] r1, HexFormat r2) {
        Intrinsics.checkNotNullParameter(r1, "$this$toHexString");
        Intrinsics.checkNotNullParameter(r2, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r1, r2);
    }

    /* JADX INFO: renamed from: toHexString-zHuV2wU$default, reason: not valid java name */
    static /* synthetic */ String m2130toHexStringzHuV2wU$default(byte[] r0, HexFormat r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L5;
        r1 = HexFormat.Companion.getDefault();
    L5:
        Intrinsics.checkNotNullParameter(r0, "$this$toHexString");
        Intrinsics.checkNotNullParameter(r1, MediaInformation.KEY_FORMAT_PROPERTIES);
        return HexExtensionsKt.toHexString(r0, r1);
    }
}
