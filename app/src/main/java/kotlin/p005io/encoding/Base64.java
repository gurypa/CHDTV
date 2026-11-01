package kotlin.p005io.encoding;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.base.Ascii;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: Base64.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\b\u0017\u0018\u0000 22\u00020\u0001:\u00012B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000¢\u0006\u0002\b\rJ%\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u0013J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0011H\u0002J%\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u001bJ\"\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J\"\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J0\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J4\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J4\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J \u0010 \u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J\"\u0010!\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J4\u0010\"\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J5\u0010#\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b$J\u0010\u0010%\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u0011H\u0002J=\u0010&\u001a\u0002H'\"\f\b\u0000\u0010'*\u00060(j\u0002`)2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u0002H'2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011¢\u0006\u0002\u0010*J\"\u0010+\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J%\u0010,\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b-J(\u0010.\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u00100\u001a\u00020\u0011H\u0002J \u00101\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002R\u0014\u0010\u0004\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u00063"}, m421d2 = {"Lkotlin/io/encoding/Base64;", "", "isUrlSafe", "", "isMimeScheme", "(ZZ)V", "isMimeScheme$kotlin_stdlib", "()Z", "isUrlSafe$kotlin_stdlib", "bytesToStringImpl", "", "source", "", "bytesToStringImpl$kotlin_stdlib", "charsToBytesImpl", "", "startIndex", "", "endIndex", "charsToBytesImpl$kotlin_stdlib", "checkDestinationBounds", "", "destinationSize", "destinationOffset", "capacityNeeded", "checkSourceBounds", "sourceSize", "checkSourceBounds$kotlin_stdlib", "decode", "decodeImpl", "destination", "decodeIntoByteArray", "decodeSize", "encode", "encodeIntoByteArray", "encodeIntoByteArrayImpl", "encodeIntoByteArrayImpl$kotlin_stdlib", "encodeSize", "encodeToAppendable", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "([BLjava/lang/Appendable;II)Ljava/lang/Appendable;", "encodeToByteArray", "encodeToByteArrayImpl", "encodeToByteArrayImpl$kotlin_stdlib", "handlePaddingSymbol", "padIndex", "byteStart", "skipIllegalSymbolsIfMime", "Default", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class Base64 {
    public static final Default Default = null;
    private static final Base64 Mime = null;
    private static final Base64 UrlSafe = null;
    private static final int bitsPerByte = 8;
    private static final int bitsPerSymbol = 6;
    public static final int bytesPerGroup = 3;
    private static final int mimeGroupsPerLine = 19;
    public static final int mimeLineLength = 76;
    private static final byte[] mimeLineSeparatorSymbols = null;
    public static final byte padSymbol = 61;
    public static final int symbolsPerGroup = 4;
    private final boolean isMimeScheme;
    private final boolean isUrlSafe;

    /* JADX INFO: compiled from: Base64.kt */
    @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0006\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0005R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\u000fX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, m421d2 = {"Lkotlin/io/encoding/Base64$Default;", "Lkotlin/io/encoding/Base64;", "()V", "Mime", "getMime", "()Lkotlin/io/encoding/Base64;", "UrlSafe", "getUrlSafe", "bitsPerByte", "", "bitsPerSymbol", "bytesPerGroup", "mimeGroupsPerLine", "mimeLineLength", "mimeLineSeparatorSymbols", "", "getMimeLineSeparatorSymbols$kotlin_stdlib", "()[B", "padSymbol", "", "symbolsPerGroup", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Default extends Base64 {
        private Default() {
            boolean r0 = false;
            super(r0, r0, null);
        }

        public /* synthetic */ Default(DefaultConstructorMarker r1) {
            this();
        }

        public final Base64 getMime() {
            return Base64.access$getMime$cp();
        }

        public final byte[] getMimeLineSeparatorSymbols$kotlin_stdlib() {
            return Base64.access$getMimeLineSeparatorSymbols$cp();
        }

        public final Base64 getUrlSafe() {
            return Base64.access$getUrlSafe$cp();
        }
    }

    static {
        Default = new Default(null);
        mimeLineSeparatorSymbols = new byte[]{Ascii.f79CR, 10};
        UrlSafe = new Base64(true, false);
        Mime = new Base64(false, true);
    }

    private Base64(boolean r3, boolean r4) {
        this.isUrlSafe = r3;
        this.isMimeScheme = r4;
        if (this.isUrlSafe == true) goto L5;
    L8:
        boolean r0 = true;
    L9:
        if (r0 == false) goto L12;
        return;
    L12:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L5:
        if (this.isMimeScheme == false) goto L8;
        r0 = false;
        goto L9
    }

    public /* synthetic */ Base64(boolean r1, boolean r2, DefaultConstructorMarker r3) {
        this(r1, r2);
    }

    public static final /* synthetic */ Base64 access$getMime$cp() {
        return Mime;
    }

    public static final /* synthetic */ byte[] access$getMimeLineSeparatorSymbols$cp() {
        return mimeLineSeparatorSymbols;
    }

    public static final /* synthetic */ Base64 access$getUrlSafe$cp() {
        return UrlSafe;
    }

    private final void checkDestinationBounds(int r6, int r7, int r8) {
        if (r7 < 0) goto L12;
        if (r7 > r6) goto L12;
        int r1 = r7 + r8;
        if (r1 < 0) goto L10;
        if (r1 > r6) goto L10;
        return;
    L10:
        throw new IndexOutOfBoundsException("The destination array does not have enough capacity, destination offset: " + r7 + ", destination size: " + r6 + ", capacity needed: " + r8);
    L12:
        throw new IndexOutOfBoundsException("destination offset: " + r7 + ", destination size: " + r6);
    }

    public static /* synthetic */ byte[] decode$default(Base64 r0, CharSequence r1, int r2, int r3, int r4, Object r5) {
        if (r5 != null) goto L12;
        if ((r4 & 2) == 0) goto L7;
        r2 = 0;
    L7:
        if ((r4 & 4) == 0) goto L10;
        r3 = r1.length();
    L10:
        return r0.decode(r1, r2, r3);
    L12:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decode");
    }

    public static /* synthetic */ byte[] decode$default(Base64 r0, byte[] r1, int r2, int r3, int r4, Object r5) {
        if (r5 != null) goto L12;
        if ((r4 & 2) == 0) goto L7;
        r2 = 0;
    L7:
        if ((r4 & 4) == 0) goto L10;
        r3 = r1.length;
    L10:
        return r0.decode(r1, r2, r3);
    L12:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decode");
    }

    private final int decodeImpl(byte[] r20, byte[] r21, int r22, int r23, int r24) {
        if (this.isUrlSafe == false) goto L5;
        int[] r3 = Base64Kt.access$getBase64UrlDecodeMap$p();
    L6:
        int r4 = 0;
        int r5 = -8;
        int r6 = r23;
        int r7 = r22;
    L8:
        if (r6 >= r24) goto L30;
        if (r5 != (-8)) goto L17;
        if ((r6 + 3) >= r24) goto L17;
        int r13 = r6 + 1;
        int r14 = r13 + 1;
        int r15 = r14 + 1;
        int r16 = r15 + 1;
        int r18 = 8;
        int r12 = (((r3[r20[r6] & 255] << 18) | (r3[r20[r13] & 255] << 12)) | (r3[r20[r14] & 255] << 6)) | r3[r20[r15] & 255];
        if (r12 >= 0) goto L15;
        r6 = r16 - 4;
    L18:
        int r17 = r20[r6] & 255;
        int r19 = r3[r17];
        if (r19 < 0) goto L20;
        r6 = r6 + 1;
        r4 = (r4 << 6) | r19;
        r5 = r5 + 6;
        if (r5 < 0) goto L8;
        r21[r7] = (byte) (r4 >>> r5);
        r4 = r4 & ((1 << r5) - 1);
        r5 = r5 - 8;
        r7 = r7 + 1;
        goto L8
    L20:
        if (r19 == (-2)) goto L21;
        if (this.isMimeScheme == false) goto L25;
        r6 = r6 + 1;
        goto L8
    L25:
        StringBuilder r10 = new StringBuilder().append("Invalid symbol '").append((char) r17).append("'(");
        String r110 = Integer.toString(r17, CharsKt.checkRadix(r18));
        Intrinsics.checkNotNullExpressionValue(r110, "toString(...)");
        throw new IllegalArgumentException(r10.append(r110).append(") at index ").append(r6).toString());
    L21:
        r6 = handlePaddingSymbol(r20, r6, r24, r5);
    L31:
        if (r5 == (-2)) goto L39;
        int r8 = skipIllegalSymbolsIfMime(r20, r6, r24);
        if (r8 >= r24) goto L35;
        int r11 = r20[r8] & 255;
        StringBuilder r111 = new StringBuilder().append("Symbol '").append((char) r11).append("'(");
        String r112 = Integer.toString(r11, CharsKt.checkRadix(r18));
        Intrinsics.checkNotNullExpressionValue(r112, "toString(...)");
        throw new IllegalArgumentException(r111.append(r112).append(") at index ").append(r8 - 1).append(" is prohibited after the pad character").toString());
    L35:
        return r7 - r22;
    L39:
        throw new IllegalArgumentException("The last unit of input does not have enough bits");
    L15:
        int r9 = r7 + 1;
        r21[r7] = (byte) (r12 >> 16);
        int r25 = r9 + 1;
        r21[r9] = (byte) (r12 >> 8);
        r21[r25] = (byte) r12;
        r7 = r25 + 1;
        r6 = r16;
    L17:
        r18 = 8;
        goto L18
    L30:
        r18 = 8;
        goto L31
    L5:
        r3 = Base64Kt.access$getBase64DecodeMap$p();
        goto L6
    }

    public static /* synthetic */ int decodeIntoByteArray$default(Base64 r1, CharSequence r2, byte[] r3, int r4, int r5, int r6, int r7, Object r8) {
        if (r8 != null) goto L15;
        if ((r7 & 4) == 0) goto L7;
        r4 = 0;
    L7:
        if ((r7 & 8) == 0) goto L10;
        r5 = 0;
    L10:
        if ((r7 & 16) == 0) goto L13;
        r6 = r2.length();
    L13:
        return r1.decodeIntoByteArray(r2, r3, r4, r5, r6);
    L15:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decodeIntoByteArray");
    }

    public static /* synthetic */ int decodeIntoByteArray$default(Base64 r1, byte[] r2, byte[] r3, int r4, int r5, int r6, int r7, Object r8) {
        if (r8 != null) goto L15;
        if ((r7 & 4) == 0) goto L7;
        r4 = 0;
    L7:
        if ((r7 & 8) == 0) goto L10;
        r5 = 0;
    L10:
        if ((r7 & 16) == 0) goto L13;
        r6 = r2.length;
    L13:
        return r1.decodeIntoByteArray(r2, r3, r4, r5, r6);
    L15:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decodeIntoByteArray");
    }

    private final int decodeSize(byte[] r6, int r7, int r8) {
        int r0 = r8 - r7;
        if (r0 != 0) goto L7;
        return 0;
    L7:
        if (r0 == 1) goto L27;
        if (this.isMimeScheme == false) goto L20;
        int r1 = r7;
    L11:
        if (r1 >= r8) goto L25;
        int r3 = Base64Kt.access$getBase64DecodeMap$p()[r6[r1] & 255];
        if (r3 >= 0) goto L18;
        if (r3 == (-2)) goto L16;
        r0 = r0 - 1;
        goto L18
    L16:
        r0 = r0 - (r8 - r1);
    L18:
        r1 = r1 + 1;
    L25:
        return (int) ((((long) r0) * ((long) 6)) / ((long) 8));
    L20:
        if (r6[r8 - 1] != 61) goto L25;
        r0 = r0 - 1;
        if (r6[r8 - 2] != 61) goto L25;
        r0 = r0 - 1;
        goto L25
    L27:
        throw new IllegalArgumentException("Input should have at list 2 symbols for Base64 decoding, startIndex: " + r7 + ", endIndex: " + r8);
    }

    public static /* synthetic */ String encode$default(Base64 r0, byte[] r1, int r2, int r3, int r4, Object r5) {
        if (r5 != null) goto L12;
        if ((r4 & 2) == 0) goto L7;
        r2 = 0;
    L7:
        if ((r4 & 4) == 0) goto L10;
        r3 = r1.length;
    L10:
        return r0.encode(r1, r2, r3);
    L12:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encode");
    }

    public static /* synthetic */ int encodeIntoByteArray$default(Base64 r1, byte[] r2, byte[] r3, int r4, int r5, int r6, int r7, Object r8) {
        if (r8 != null) goto L15;
        if ((r7 & 4) == 0) goto L7;
        r4 = 0;
    L7:
        if ((r7 & 8) == 0) goto L10;
        r5 = 0;
    L10:
        if ((r7 & 16) == 0) goto L13;
        r6 = r2.length;
    L13:
        return r1.encodeIntoByteArray(r2, r3, r4, r5, r6);
    L15:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeIntoByteArray");
    }

    private final int encodeSize(int r6) {
        int r0 = ((r6 + 3) - 1) / 3;
        if (this.isMimeScheme == false) goto L5;
        int r1 = (r0 - 1) / 19;
    L6:
        int r2 = (r0 * 4) + (r1 * 2);
        if (r2 < 0) goto L10;
        return r2;
    L10:
        throw new IllegalArgumentException("Input is too big");
    L5:
        r1 = 0;
        goto L6
    }

    public static /* synthetic */ Appendable encodeToAppendable$default(Base64 r0, byte[] r1, Appendable r2, int r3, int r4, int r5, Object r6) {
        if (r6 != null) goto L12;
        if ((r5 & 4) == 0) goto L7;
        r3 = 0;
    L7:
        if ((r5 & 8) == 0) goto L10;
        r4 = r1.length;
    L10:
        return r0.encodeToAppendable(r1, r2, r3, r4);
    L12:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeToAppendable");
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(Base64 r0, byte[] r1, int r2, int r3, int r4, Object r5) {
        if (r5 != null) goto L12;
        if ((r4 & 2) == 0) goto L7;
        r2 = 0;
    L7:
        if ((r4 & 4) == 0) goto L10;
        r3 = r1.length;
    L10:
        return r0.encodeToByteArray(r1, r2, r3);
    L12:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeToByteArray");
    }

    private final int handlePaddingSymbol(byte[] r5, int r6, int r7, int r8) {
        switch(r8) {
            case -8: goto L16;
            case -7: goto L4;
            case -6: goto L14;
            case -5: goto L4;
            case -4: goto L6;
            case -3: goto L4;
            case -2: goto L17;
            default: goto L4;
        };
    L6:
        int r0 = skipIllegalSymbolsIfMime(r5, r6 + 1, r7);
        if (r0 == r7) goto L12;
        if (r5[r0] != 61) goto L12;
        return r0 + 1;
    L12:
        throw new IllegalArgumentException("Missing one pad character at index " + r0);
    L4:
        throw new IllegalStateException("Unreachable".toString());
    L17:
        return r6 + 1;
    L14:
        return r6 + 1;
    L16:
        throw new IllegalArgumentException("Redundant pad character at index " + r6);
    }

    private final int skipIllegalSymbolsIfMime(byte[] r5, int r6, int r7) {
        if (this.isMimeScheme == true) goto L5;
        return r6;
    L5:
        int r0 = r6;
    L6:
        if (r0 >= r7) goto L11;
        int r1 = r5[r0] & 255;
        if (Base64Kt.access$getBase64DecodeMap$p()[r1] != (-1)) goto L9;
        r0 = r0 + 1;
        goto L6
    L9:
        return r0;
    L11:
        return r0;
    }

    public final String bytesToStringImpl$kotlin_stdlib(byte[] r6) {
        Intrinsics.checkNotNullParameter(r6, "source");
        StringBuilder r0 = new StringBuilder(r6.length);
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0.append((char) r6[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        String r3 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r3, "toString(...)");
        return r3;
    }

    public final byte[] charsToBytesImpl$kotlin_stdlib(CharSequence r7, int r8, int r9) {
        Intrinsics.checkNotNullParameter(r7, "source");
        checkSourceBounds$kotlin_stdlib(r7.length(), r8, r9);
        byte[] r0 = new byte[r9 - r8];
        int r1 = 0;
        int r2 = r8;
    L3:
        if (r2 >= r9) goto L9;
        char r3 = r7.charAt(r2);
        if (r3 > 255) goto L7;
        int r4 = r1 + 1;
        r0[r1] = (byte) r3;
    L8:
        r1 = r4;
        r2 = r2 + 1;
        goto L3
    L7:
        r4 = r1 + 1;
        r0[r1] = 63;
        goto L8
    L9:
        return r0;
    }

    public final void checkSourceBounds$kotlin_stdlib(int r2, int r3, int r4) {
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(r3, r4, r2);
    }

    public final byte[] decode(CharSequence r8, int r9, int r10) {
        Intrinsics.checkNotNullParameter(r8, "source");
        if ((r8 instanceof String) == false) goto L5;
        checkSourceBounds$kotlin_stdlib(r8.length(), r9, r10);
        String r0 = ((String) r8).substring(r9, r10);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        Charset r1 = Charsets.ISO_8859_1;
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        byte[] r2 = r0.getBytes(r1);
        Intrinsics.checkNotNullExpressionValue(r2, "getBytes(...)");
    L7:
        return decode$default(this, r2, 0, 0, 6, null);
    L5:
        r2 = charsToBytesImpl$kotlin_stdlib(r8, r9, r10);
        goto L7
    }

    public final byte[] decode(byte[] r8, int r9, int r10) {
        Intrinsics.checkNotNullParameter(r8, "source");
        checkSourceBounds$kotlin_stdlib(r8.length, r9, r10);
        byte[] r3 = new byte[decodeSize(r8, r9, r10)];
        if (decodeImpl(r8, r3, 0, r9, r10) != r3.length) goto L5;
        boolean r11 = true;
    L6:
        if (r11 == false) goto L9;
        return r3;
    L9:
        throw new IllegalStateException("Check failed.".toString());
    L5:
        r11 = false;
        goto L6
    }

    public final int decodeIntoByteArray(CharSequence r10, byte[] r11, int r12, int r13, int r14) {
        Intrinsics.checkNotNullParameter(r10, "source");
        Intrinsics.checkNotNullParameter(r11, "destination");
        if ((r10 instanceof String) == false) goto L5;
        checkSourceBounds$kotlin_stdlib(r10.length(), r13, r14);
        String r0 = ((String) r10).substring(r13, r14);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        Charset r1 = Charsets.ISO_8859_1;
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.lang.String");
        byte[] r2 = r0.getBytes(r1);
        Intrinsics.checkNotNullExpressionValue(r2, "getBytes(...)");
    L7:
        return decodeIntoByteArray$default(this, r2, r11, r12, 0, 0, 24, null);
    L5:
        r2 = charsToBytesImpl$kotlin_stdlib(r10, r13, r14);
        goto L7
    }

    public final int decodeIntoByteArray(byte[] r3, byte[] r4, int r5, int r6, int r7) {
        Intrinsics.checkNotNullParameter(r3, "source");
        Intrinsics.checkNotNullParameter(r4, "destination");
        checkSourceBounds$kotlin_stdlib(r3.length, r6, r7);
        checkDestinationBounds(r4.length, r5, decodeSize(r3, r6, r7));
        return decodeImpl(r3, r4, r5, r6, r7);
    }

    public final String encode(byte[] r4, int r5, int r6) {
        Intrinsics.checkNotNullParameter(r4, "source");
        return new String(encodeToByteArrayImpl$kotlin_stdlib(r4, r5, r6), Charsets.ISO_8859_1);
    }

    public final int encodeIntoByteArray(byte[] r2, byte[] r3, int r4, int r5, int r6) {
        Intrinsics.checkNotNullParameter(r2, "source");
        Intrinsics.checkNotNullParameter(r3, "destination");
        return encodeIntoByteArrayImpl$kotlin_stdlib(r2, r3, r4, r5, r6);
    }

    public final int encodeIntoByteArrayImpl$kotlin_stdlib(byte[] r21, byte[] r22, int r23, int r24, int r25) {
        Intrinsics.checkNotNullParameter(r21, "source");
        Intrinsics.checkNotNullParameter(r22, "destination");
        checkSourceBounds$kotlin_stdlib(r21.length, r24, r25);
        checkDestinationBounds(r22.length, r23, encodeSize(r25 - r24));
        if (this.isUrlSafe == false) goto L5;
        byte[] r6 = Base64Kt.access$getBase64UrlEncodeMap$p();
    L6:
        int r7 = r24;
        int r8 = r23;
        if (this.isMimeScheme == false) goto L9;
        int r9 = 19;
    L10:
        boolean r11 = false;
        if ((r7 + 2) >= r25) goto L19;
        int r10 = Math.min((r25 - r7) / 3, r9);
        int r13 = 0;
    L13:
        if (r13 >= r10) goto L15;
        int r14 = r7 + 1;
        int r15 = r14 + 1;
        int r17 = (((r21[r7] & 255) << 16) | ((r21[r14] & 255) << 8)) | (r21[r15] & 255);
        int r18 = r8 + 1;
        r22[r8] = r6[r17 >>> 18];
        int r12 = r18 + 1;
        r22[r18] = r6[(r17 >>> 12) & 63];
        int r19 = r12 + 1;
        r22[r12] = r6[(r17 >>> 6) & 63];
        r8 = r19 + 1;
        r22[r19] = r6[r17 & 63];
        r13 = r13 + 1;
        r7 = r15 + 1;
        goto L13
    L15:
        if (r10 != r9) goto L10;
        if (r7 == r25) goto L10;
        int r16 = r8 + 1;
        r22[r8] = mimeLineSeparatorSymbols[0];
        r8 = r16 + 1;
        r22[r16] = mimeLineSeparatorSymbols[1];
        goto L10
    L19:
        switch((r25 - r7)) {
            case 1: goto L22;
            case 2: goto L21;
            default: goto L23;
        };
    L21:
        int r110 = r7 + 1;
        int r111 = ((r21[r7] & 255) << 10) | ((r21[r110] & 255) << 2);
        int r112 = r8 + 1;
        r22[r8] = r6[r111 >>> 12];
        int r20 = r112 + 1;
        r22[r112] = r6[(r111 >>> 6) & 63];
        int r113 = r20 + 1;
        r22[r20] = r6[r111 & 63];
        r8 = r113 + 1;
        r22[r113] = padSymbol;
        r7 = r110 + 1;
        goto L23
    L22:
        int r114 = (r21[r7] & 255) << 4;
        int r115 = r8 + 1;
        r22[r8] = r6[r114 >>> 6];
        int r26 = r115 + 1;
        r22[r115] = r6[r114 & 63];
        int r116 = r26 + 1;
        r22[r26] = padSymbol;
        r8 = r116 + 1;
        r22[r116] = padSymbol;
        r7 = r7 + 1;
    L23:
        if (r7 != r25) goto L25;
        r11 = true;
    L25:
        if (r11 == false) goto L29;
        return r8 - r23;
    L29:
        throw new IllegalStateException("Check failed.".toString());
    L9:
        r9 = Integer.MAX_VALUE;
        goto L10
    L5:
        r6 = Base64Kt.access$getBase64EncodeMap$p();
        goto L6
    }

    public final <A extends Appendable> A encodeToAppendable(byte[] r4, A r5, int r6, int r7) {
        Intrinsics.checkNotNullParameter(r4, "source");
        Intrinsics.checkNotNullParameter(r5, "destination");
        r5.append(new String(encodeToByteArrayImpl$kotlin_stdlib(r4, r6, r7), Charsets.ISO_8859_1));
        return r5;
    }

    public final byte[] encodeToByteArray(byte[] r2, int r3, int r4) {
        Intrinsics.checkNotNullParameter(r2, "source");
        return encodeToByteArrayImpl$kotlin_stdlib(r2, r3, r4);
    }

    public final byte[] encodeToByteArrayImpl$kotlin_stdlib(byte[] r8, int r9, int r10) {
        Intrinsics.checkNotNullParameter(r8, "source");
        checkSourceBounds$kotlin_stdlib(r8.length, r9, r10);
        byte[] r3 = new byte[encodeSize(r10 - r9)];
        encodeIntoByteArrayImpl$kotlin_stdlib(r8, r3, 0, r9, r10);
        return r3;
    }

    public final boolean isMimeScheme$kotlin_stdlib() {
        return this.isMimeScheme;
    }

    public final boolean isUrlSafe$kotlin_stdlib() {
        return this.isUrlSafe;
    }
}
