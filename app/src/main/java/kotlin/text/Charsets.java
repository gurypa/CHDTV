package kotlin.text;

import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Charsets.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\u00048G¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\u00048G¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u000e\u001a\u00020\u00048G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000bR\u0010\u0010\u0010\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"}, m421d2 = {"Lkotlin/text/Charsets;", "", "()V", "ISO_8859_1", "Ljava/nio/charset/Charset;", "US_ASCII", "UTF_16", "UTF_16BE", "UTF_16LE", "UTF_32", "UTF32", "()Ljava/nio/charset/Charset;", "UTF_32BE", "UTF32_BE", "UTF_32LE", "UTF32_LE", "UTF_8", "utf_32", "utf_32be", "utf_32le", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class Charsets {
    public static final Charsets INSTANCE = null;
    public static final Charset ISO_8859_1 = null;
    public static final Charset US_ASCII = null;
    public static final Charset UTF_16 = null;
    public static final Charset UTF_16BE = null;
    public static final Charset UTF_16LE = null;
    public static final Charset UTF_8 = null;
    private static volatile Charset utf_32;
    private static volatile Charset utf_32be;
    private static volatile Charset utf_32le;

    static {
        INSTANCE = new Charsets();
        Charset r0 = Charset.forName("UTF-8");
        Intrinsics.checkNotNullExpressionValue(r0, "forName(...)");
        UTF_8 = r0;
        Charset r1 = Charset.forName("UTF-16");
        Intrinsics.checkNotNullExpressionValue(r1, "forName(...)");
        UTF_16 = r1;
        Charset r2 = Charset.forName("UTF-16BE");
        Intrinsics.checkNotNullExpressionValue(r2, "forName(...)");
        UTF_16BE = r2;
        Charset r3 = Charset.forName("UTF-16LE");
        Intrinsics.checkNotNullExpressionValue(r3, "forName(...)");
        UTF_16LE = r3;
        Charset r4 = Charset.forName("US-ASCII");
        Intrinsics.checkNotNullExpressionValue(r4, "forName(...)");
        US_ASCII = r4;
        Charset r5 = Charset.forName("ISO-8859-1");
        Intrinsics.checkNotNullExpressionValue(r5, "forName(...)");
        ISO_8859_1 = r5;
    }

    private Charsets() {
    }

    public final Charset UTF32() {
        Charset r0 = utf_32;
        if (r0 != null) goto L6;
        Charset r2 = Charset.forName("UTF-32");
        Intrinsics.checkNotNullExpressionValue(r2, "forName(...)");
        utf_32 = r2;
        return r2;
    L6:
        return r0;
    }

    public final Charset UTF32_BE() {
        Charset r0 = utf_32be;
        if (r0 != null) goto L6;
        Charset r2 = Charset.forName("UTF-32BE");
        Intrinsics.checkNotNullExpressionValue(r2, "forName(...)");
        utf_32be = r2;
        return r2;
    L6:
        return r0;
    }

    public final Charset UTF32_LE() {
        Charset r0 = utf_32le;
        if (r0 != null) goto L6;
        Charset r2 = Charset.forName("UTF-32LE");
        Intrinsics.checkNotNullExpressionValue(r2, "forName(...)");
        utf_32le = r2;
        return r2;
    L6:
        return r0;
    }
}
