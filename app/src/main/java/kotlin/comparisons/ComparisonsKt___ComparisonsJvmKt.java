package kotlin.comparisons;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.exoplayer.upstream.CmcdData;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: _ComparisonsJvm.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000F\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a9\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00010\t\"\u0002H\u0001H\u0007¢\u0006\u0002\u0010\n\u001a\u0019\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\n\u0010\b\u001a\u00020\f\"\u00020\u000bH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\n\u0010\b\u001a\u00020\u000e\"\u00020\rH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\n\u0010\b\u001a\u00020\u0010\"\u00020\u000fH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\n\u0010\b\u001a\u00020\u0012\"\u00020\u0011H\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\n\u0010\b\u001a\u00020\u0014\"\u00020\u0013H\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0015H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\n\u0010\b\u001a\u00020\u0016\"\u00020\u0015H\u0007\u001a-\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a9\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00010\t\"\u0002H\u0001H\u0007¢\u0006\u0002\u0010\n\u001a\u0019\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\n\u0010\b\u001a\u00020\f\"\u00020\u000bH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\n\u0010\b\u001a\u00020\u000e\"\u00020\rH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\n\u0010\b\u001a\u00020\u0010\"\u00020\u000fH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\n\u0010\b\u001a\u00020\u0012\"\u00020\u0011H\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\n\u0010\b\u001a\u00020\u0014\"\u00020\u0013H\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0015H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\n\u0010\b\u001a\u00020\u0016\"\u00020\u0015H\u0007¨\u0006\u0018"}, m421d2 = {"maxOf", ExifInterface.GPS_DIRECTION_TRUE, "", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "c", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "other", "", "(Ljava/lang/Comparable;[Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "", "", "", "", "", "", "minOf", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/comparisons/ComparisonsKt")
class ComparisonsKt___ComparisonsJvmKt extends ComparisonsKt__ComparisonsKt {
    public ComparisonsKt___ComparisonsJvmKt() {
    }

    private static final byte maxOf(byte r1, byte r2) {
        return (byte) Math.max(r1, r2);
    }

    private static final byte maxOf(byte r1, byte r2, byte r3) {
        return (byte) Math.max(r1, Math.max(r2, r3));
    }

    public static final byte maxOf(byte r5, byte... r6) {
        Intrinsics.checkNotNullParameter(r6, "other");
        byte r0 = r5;
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = (byte) Math.max(r0, r6[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final double maxOf(double r2, double r4) {
        return Math.max(r2, r4);
    }

    private static final double maxOf(double r2, double r4, double r6) {
        return Math.max(r2, Math.max(r4, r6));
    }

    public static final double maxOf(double r6, double... r8) {
        Intrinsics.checkNotNullParameter(r8, "other");
        double r0 = r6;
        int r2 = r8.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = Math.max(r0, r8[r3]);
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final float maxOf(float r1, float r2) {
        return Math.max(r1, r2);
    }

    private static final float maxOf(float r1, float r2, float r3) {
        return Math.max(r1, Math.max(r2, r3));
    }

    public static final float maxOf(float r4, float... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        float r0 = r4;
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = Math.max(r0, r5[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final int maxOf(int r1, int r2) {
        return Math.max(r1, r2);
    }

    private static final int maxOf(int r1, int r2, int r3) {
        return Math.max(r1, Math.max(r2, r3));
    }

    public static final int maxOf(int r4, int... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        int r0 = r4;
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = Math.max(r0, r5[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final long maxOf(long r2, long r4) {
        return Math.max(r2, r4);
    }

    private static final long maxOf(long r2, long r4, long r6) {
        return Math.max(r2, Math.max(r4, r6));
    }

    public static final long maxOf(long r6, long... r8) {
        Intrinsics.checkNotNullParameter(r8, "other");
        long r0 = r6;
        int r2 = r8.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = Math.max(r0, r8[r3]);
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    public static final <T extends Comparable<? super T>> T maxOf(T r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY);
        Intrinsics.checkNotNullParameter(r2, "b");
        if (r1.compareTo(r2) < 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    public static final <T extends Comparable<? super T>> T maxOf(T r1, T r2, T r3) {
        Intrinsics.checkNotNullParameter(r1, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY);
        Intrinsics.checkNotNullParameter(r2, "b");
        Intrinsics.checkNotNullParameter(r3, "c");
        return (T) ComparisonsKt.maxOf(r1, ComparisonsKt.maxOf(r2, r3));
    }

    public static final <T extends Comparable<? super T>> T maxOf(T r4, T... r5) {
        Intrinsics.checkNotNullParameter(r4, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY);
        Intrinsics.checkNotNullParameter(r5, "other");
        T r0 = r4;
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        T r3 = r0;
        r0 = (T) ComparisonsKt.maxOf(r3, r5[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final short maxOf(short r1, short r2) {
        return (short) Math.max(r1, r2);
    }

    private static final short maxOf(short r1, short r2, short r3) {
        return (short) Math.max(r1, Math.max(r2, r3));
    }

    public static final short maxOf(short r5, short... r6) {
        Intrinsics.checkNotNullParameter(r6, "other");
        short r0 = r5;
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = (short) Math.max(r0, r6[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final byte minOf(byte r1, byte r2) {
        return (byte) Math.min(r1, r2);
    }

    private static final byte minOf(byte r1, byte r2, byte r3) {
        return (byte) Math.min(r1, Math.min(r2, r3));
    }

    public static final byte minOf(byte r5, byte... r6) {
        Intrinsics.checkNotNullParameter(r6, "other");
        byte r0 = r5;
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = (byte) Math.min(r0, r6[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final double minOf(double r2, double r4) {
        return Math.min(r2, r4);
    }

    private static final double minOf(double r2, double r4, double r6) {
        return Math.min(r2, Math.min(r4, r6));
    }

    public static final double minOf(double r6, double... r8) {
        Intrinsics.checkNotNullParameter(r8, "other");
        double r0 = r6;
        int r2 = r8.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = Math.min(r0, r8[r3]);
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final float minOf(float r1, float r2) {
        return Math.min(r1, r2);
    }

    private static final float minOf(float r1, float r2, float r3) {
        return Math.min(r1, Math.min(r2, r3));
    }

    public static final float minOf(float r4, float... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        float r0 = r4;
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = Math.min(r0, r5[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final int minOf(int r1, int r2) {
        return Math.min(r1, r2);
    }

    private static final int minOf(int r1, int r2, int r3) {
        return Math.min(r1, Math.min(r2, r3));
    }

    public static final int minOf(int r4, int... r5) {
        Intrinsics.checkNotNullParameter(r5, "other");
        int r0 = r4;
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = Math.min(r0, r5[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final long minOf(long r2, long r4) {
        return Math.min(r2, r4);
    }

    private static final long minOf(long r2, long r4, long r6) {
        return Math.min(r2, Math.min(r4, r6));
    }

    public static final long minOf(long r6, long... r8) {
        Intrinsics.checkNotNullParameter(r8, "other");
        long r0 = r6;
        int r2 = r8.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = Math.min(r0, r8[r3]);
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    public static final <T extends Comparable<? super T>> T minOf(T r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY);
        Intrinsics.checkNotNullParameter(r2, "b");
        if (r1.compareTo(r2) > 0) goto L6;
        return r1;
    L6:
        return r2;
    }

    public static final <T extends Comparable<? super T>> T minOf(T r1, T r2, T r3) {
        Intrinsics.checkNotNullParameter(r1, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY);
        Intrinsics.checkNotNullParameter(r2, "b");
        Intrinsics.checkNotNullParameter(r3, "c");
        return (T) ComparisonsKt.minOf(r1, ComparisonsKt.minOf(r2, r3));
    }

    public static final <T extends Comparable<? super T>> T minOf(T r4, T... r5) {
        Intrinsics.checkNotNullParameter(r4, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY);
        Intrinsics.checkNotNullParameter(r5, "other");
        T r0 = r4;
        int r1 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        T r3 = r0;
        r0 = (T) ComparisonsKt.minOf(r3, r5[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }

    private static final short minOf(short r1, short r2) {
        return (short) Math.min(r1, r2);
    }

    private static final short minOf(short r1, short r2, short r3) {
        return (short) Math.min(r1, Math.min(r2, r3));
    }

    public static final short minOf(short r5, short... r6) {
        Intrinsics.checkNotNullParameter(r6, "other");
        short r0 = r5;
        int r1 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0 = (short) Math.min(r0, r6[r2]);
        r2 = r2 + 1;
        goto L3
    L5:
        return r0;
    }
}
