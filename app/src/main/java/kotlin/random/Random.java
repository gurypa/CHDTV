package kotlin.random;

import com.arthenica.ffmpegkit.MediaInformation;
import java.io.Serializable;
import kotlin.Metadata;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: Random.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016¨\u0006\u0018"}, m421d2 = {"Lkotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", MediaInformation.KEY_SIZE, "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "Default", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public abstract class Random {
    public static final Default Default = null;
    private static final Random defaultRandom = null;

    /* JADX INFO: compiled from: Random.kt */
    @Metadata(m420d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u001cB\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0007H\u0016J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\u0018\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, m421d2 = {"Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", MediaInformation.KEY_SIZE, "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "writeReplace", "", "Serialized", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Default extends Random implements Serializable {

        /* JADX INFO: compiled from: Random.kt */
        @Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\bÂ\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\b"}, m421d2 = {"Lkotlin/random/Random$Default$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "serialVersionUID", "", "readResolve", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        private static final class Serialized implements Serializable {
            public static final Serialized INSTANCE = null;
            private static final long serialVersionUID = 0;

            static {
                INSTANCE = new Serialized();
            }

            private Serialized() {
            }

            private final Object readResolve() {
                return Random.Default;
            }
        }

        private Default() {
        }

        public /* synthetic */ Default(DefaultConstructorMarker r1) {
            this();
        }

        private final Object writeReplace() {
            return Serialized.INSTANCE;
        }

        @Override // kotlin.random.Random
        public int nextBits(int r2) {
            return Random.access$getDefaultRandom$cp().nextBits(r2);
        }

        @Override // kotlin.random.Random
        public boolean nextBoolean() {
            return Random.access$getDefaultRandom$cp().nextBoolean();
        }

        @Override // kotlin.random.Random
        public byte[] nextBytes(int r2) {
            return Random.access$getDefaultRandom$cp().nextBytes(r2);
        }

        @Override // kotlin.random.Random
        public byte[] nextBytes(byte[] r2) {
            Intrinsics.checkNotNullParameter(r2, "array");
            return Random.access$getDefaultRandom$cp().nextBytes(r2);
        }

        @Override // kotlin.random.Random
        public byte[] nextBytes(byte[] r2, int r3, int r4) {
            Intrinsics.checkNotNullParameter(r2, "array");
            return Random.access$getDefaultRandom$cp().nextBytes(r2, r3, r4);
        }

        @Override // kotlin.random.Random
        public double nextDouble() {
            return Random.access$getDefaultRandom$cp().nextDouble();
        }

        @Override // kotlin.random.Random
        public double nextDouble(double r3) {
            return Random.access$getDefaultRandom$cp().nextDouble(r3);
        }

        @Override // kotlin.random.Random
        public double nextDouble(double r3, double r5) {
            return Random.access$getDefaultRandom$cp().nextDouble(r3, r5);
        }

        @Override // kotlin.random.Random
        public float nextFloat() {
            return Random.access$getDefaultRandom$cp().nextFloat();
        }

        @Override // kotlin.random.Random
        public int nextInt() {
            return Random.access$getDefaultRandom$cp().nextInt();
        }

        @Override // kotlin.random.Random
        public int nextInt(int r2) {
            return Random.access$getDefaultRandom$cp().nextInt(r2);
        }

        @Override // kotlin.random.Random
        public int nextInt(int r2, int r3) {
            return Random.access$getDefaultRandom$cp().nextInt(r2, r3);
        }

        @Override // kotlin.random.Random
        public long nextLong() {
            return Random.access$getDefaultRandom$cp().nextLong();
        }

        @Override // kotlin.random.Random
        public long nextLong(long r3) {
            return Random.access$getDefaultRandom$cp().nextLong(r3);
        }

        @Override // kotlin.random.Random
        public long nextLong(long r3, long r5) {
            return Random.access$getDefaultRandom$cp().nextLong(r3, r5);
        }
    }

    static {
        Default = new Default(null);
        defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();
    }

    public Random() {
    }

    public static final /* synthetic */ Random access$getDefaultRandom$cp() {
        return defaultRandom;
    }

    public static /* synthetic */ byte[] nextBytes$default(Random r0, byte[] r1, int r2, int r3, int r4, Object r5) {
        if (r5 != null) goto L12;
        if ((r4 & 2) == 0) goto L7;
        r2 = 0;
    L7:
        if ((r4 & 4) == 0) goto L10;
        r3 = r1.length;
    L10:
        return r0.nextBytes(r1, r2, r3);
    L12:
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: nextBytes");
    }

    public abstract int nextBits(int r1);

    public boolean nextBoolean() {
        if (nextBits(1) != 0) goto L7;
        return false;
    L7:
        return true;
    }

    public byte[] nextBytes(int r2) {
        return nextBytes(new byte[r2]);
    }

    public byte[] nextBytes(byte[] r3) {
        Intrinsics.checkNotNullParameter(r3, "array");
        return nextBytes(r3, 0, r3.length);
    }

    public byte[] nextBytes(byte[] r9, int r10, int r11) {
        Intrinsics.checkNotNullParameter(r9, "array");
        int r2 = 0;
        boolean r1 = true;
        if (new IntRange(0, r9.length).contains(r10) == true) goto L5;
    L7:
        boolean r0 = false;
    L9:
        if (r0 == false) goto L24;
        if (r10 <= r11) goto L13;
        r1 = false;
    L13:
        if (r1 == false) goto L22;
        int r3 = (r11 - r10) / 4;
        int r4 = r10;
    L15:
        if (r2 >= r3) goto L17;
        int r5 = nextInt();
        r9[r4] = (byte) r5;
        r9[r4 + 1] = (byte) (r5 >>> 8);
        r9[r4 + 2] = (byte) (r5 >>> 16);
        r9[r4 + 3] = (byte) (r5 >>> 24);
        r4 = r4 + 4;
        r2 = r2 + 1;
        goto L15
    L17:
        int r6 = r11 - r4;
        int r7 = nextBits(r6 * 8);
        int r8 = 0;
    L18:
        if (r8 >= r6) goto L20;
        r9[r4 + r8] = (byte) (r7 >>> (r8 * 8));
        r8 = r8 + 1;
        goto L18
    L20:
        return r9;
    L22:
        throw new IllegalArgumentException(("fromIndex (" + r10 + ") must be not greater than toIndex (" + r11 + ").").toString());
    L24:
        throw new IllegalArgumentException(("fromIndex (" + r10 + ") or toIndex (" + r11 + ") are out of range: 0.." + r9.length + '.').toString());
    L5:
        if (new IntRange(0, r9.length).contains(r11) == false) goto L7;
        r0 = true;
        goto L9
    }

    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(nextBits(26), nextBits(27));
    }

    public double nextDouble(double r3) {
        return nextDouble(0.0d, r3);
    }

    public double nextDouble(double r9, double r11) {
        RandomKt.checkRangeBounds(r9, r11);
        double r0 = r11 - r9;
        if (Double.isInfinite(r0) == false) goto L19;
        boolean r3 = true;
        if (Double.isInfinite(r9) == false) goto L7;
    L9:
        boolean r2 = false;
    L10:
        if (r2 == false) goto L19;
        if (Double.isInfinite(r11) == false) goto L14;
    L16:
        r3 = false;
    L17:
        if (r3 == false) goto L19;
        double r4 = 2;
        double r5 = nextDouble() * ((r11 / r4) - (r9 / r4));
        double r6 = (r9 + r5) + r5;
    L21:
        if (r6 < r11) goto L24;
        return Math.nextAfter(r11, Double.NEGATIVE_INFINITY);
    L24:
        return r6;
    L14:
        if (Double.isNaN(r11) == true) goto L16;
    L7:
        if (Double.isNaN(r9) == true) goto L9;
        r2 = true;
    L19:
        r6 = r9 + (nextDouble() * r0);
        goto L21
    }

    public float nextFloat() {
        return nextBits(24) / 1.6777216E7f;
    }

    public int nextInt() {
        return nextBits(32);
    }

    public int nextInt(int r2) {
        return nextInt(0, r2);
    }

    public int nextInt(int r7, int r8) {
        RandomKt.checkRangeBounds(r7, r8);
        int r0 = r8 - r7;
        if (r0 > 0) goto L14;
        if (r0 == Integer.MIN_VALUE) goto L14;
    L7:
        int r2 = nextInt();
        boolean r3 = false;
        if (r7 > r2) goto L11;
        if (r2 >= r8) goto L11;
        r3 = true;
    L11:
        if (r3 == false) goto L7;
        return r2;
    L14:
        if (((-r0) & r0) != r0) goto L17;
        int r1 = nextBits(RandomKt.fastLog2(r0));
    L21:
        return r7 + r1;
    L17:
        int r4 = nextInt() >>> 1;
        int r5 = r4 % r0;
        if (((r4 - r5) + (r0 - 1)) < 0) goto L17;
        r1 = r5;
        goto L21
    }

    public long nextLong() {
        return (((long) nextInt()) << 32) + ((long) nextInt());
    }

    public long nextLong(long r3) {
        return nextLong(0, r3);
    }

    public long nextLong(long r18, long r20) {
        RandomKt.checkRangeBounds(r18, r20);
        long r1 = r20 - r18;
        if (r1 > 0) goto L5;
    L19:
        long r3 = nextLong();
        boolean r7 = false;
        if (r18 > r3) goto L24;
        if (r3 >= r20) goto L24;
        r7 = true;
    L24:
        if (r7 == false) goto L19;
        return r3;
    L5:
        if (((-r1) & r1) != r1) goto L14;
        int r4 = (int) r1;
        int r5 = (int) (r1 >>> 32);
        if (r4 == 0) goto L9;
        long r9 = 4294967295L & ((long) nextBits(RandomKt.fastLog2(r4)));
    L18:
        return r18 + r9;
    L9:
        if (r5 != 1) goto L11;
        r9 = 4294967295L & ((long) nextInt());
        goto L18
    L11:
        r9 = (4294967295L & ((long) nextInt())) + (((long) nextBits(RandomKt.fastLog2(r5))) << 32);
    L14:
        long r11 = nextLong() >>> 1;
        r9 = r11 % r1;
        if (((r11 - r9) + (r1 - 1)) < 0) goto L14;
        goto L18
    }
}
