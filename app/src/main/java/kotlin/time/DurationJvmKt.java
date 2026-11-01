package kotlin.time;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DurationJvm.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000.\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\u001a\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0000\u001a\u0018\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u001c\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\b¨\u0006\u0011"}, m421d2 = {"durationAssertionsEnabled", "", "getDurationAssertionsEnabled", "()Z", "precisionFormats", "", "Ljava/lang/ThreadLocal;", "Ljava/text/DecimalFormat;", "[Ljava/lang/ThreadLocal;", "createFormatForDecimals", "decimals", "", "formatToExactDecimals", "", "value", "", "formatUpToDecimals", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class DurationJvmKt {
    private static final boolean durationAssertionsEnabled = false;
    private static final ThreadLocal<DecimalFormat>[] precisionFormats = null;

    static {
        int r0 = 0;
        durationAssertionsEnabled = false;
        ThreadLocal<DecimalFormat>[] r2 = new ThreadLocal[4];
    L3:
        if (r0 >= 4) goto L5;
        r2[r0] = new ThreadLocal();
        r0 = r0 + 1;
        goto L3
    L5:
        precisionFormats = r2;
    }

    private static final DecimalFormat createFormatForDecimals(int r4) {
        DecimalFormat r0 = new DecimalFormat("0");
        if (r4 <= 0) goto L5;
        r0.setMinimumFractionDigits(r4);
    L5:
        r0.setRoundingMode(RoundingMode.HALF_UP);
        return r0;
    }

    public static final String formatToExactDecimals(double r3, int r5) {
        if (r5 >= precisionFormats.length) goto L9;
        ThreadLocal<DecimalFormat> r0 = precisionFormats[r5];
        DecimalFormat r1 = r0.get();
        if (r1 != null) goto L7;
        r1 = createFormatForDecimals(r5);
        r0.set(r1);
    L8:
        DecimalFormat r2 = r1;
    L10:
        String r4 = r2.format(r3);
        Intrinsics.checkNotNullExpressionValue(r4, "format(...)");
        return r4;
    L7:
        Intrinsics.checkNotNull(r1);
        goto L8
    L9:
        r2 = createFormatForDecimals(r5);
        goto L10
    }

    public static final String formatUpToDecimals(double r3, int r5) {
        DecimalFormat r0 = createFormatForDecimals(0);
        r0.setMaximumFractionDigits(r5);
        String r1 = r0.format(r3);
        Intrinsics.checkNotNullExpressionValue(r1, "format(...)");
        return r1;
    }

    public static final boolean getDurationAssertionsEnabled() {
        return durationAssertionsEnabled;
    }
}
