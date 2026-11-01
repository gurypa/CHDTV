package com.google.common.hash;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.math.DoubleMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class BloomFilter<T> implements Predicate<T>, Serializable {
    private static final long serialVersionUID = 912559;
    private final BloomFilterStrategies.LockFreeBitArray bits;
    private final Funnel<? super T> funnel;
    private final int numHashFunctions;
    private final Strategy strategy;

    /* JADX INFO: renamed from: com.google.common.hash.BloomFilter$1 */
    static /* synthetic */ class C08221 {
    }

    private static class SerialForm<T> implements Serializable {
        private static final long serialVersionUID = 1;
        final long[] data;
        final Funnel<? super T> funnel;
        final int numHashFunctions;
        final Strategy strategy;

        SerialForm(BloomFilter<T> r2) {
            this.data = BloomFilterStrategies.LockFreeBitArray.toPlainArray(BloomFilter.access$000(r2).data);
            this.numHashFunctions = BloomFilter.access$100(r2);
            this.funnel = BloomFilter.access$200(r2);
            this.strategy = BloomFilter.access$300(r2);
        }

        Object readResolve() {
            return new BloomFilter(new BloomFilterStrategies.LockFreeBitArray(this.data), this.numHashFunctions, this.funnel, this.strategy, null);
        }
    }

    interface Strategy extends Serializable {
        <T> boolean mightContain(@ParametricNullness T r1, Funnel<? super T> r2, int r3, BloomFilterStrategies.LockFreeBitArray r4);

        int ordinal();

        <T> boolean put(@ParametricNullness T r1, Funnel<? super T> r2, int r3, BloomFilterStrategies.LockFreeBitArray r4);
    }

    private BloomFilter(BloomFilterStrategies.LockFreeBitArray r5, int r6, Funnel<? super T> r7, Strategy r8) {
        boolean r0 = true;
        if (r6 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "numHashFunctions (%s) must be > 0", r6);
        if (r6 <= 255) goto L10;
        r0 = false;
    L10:
        Preconditions.checkArgument(r0, "numHashFunctions (%s) must be <= 255", r6);
        this.bits = (BloomFilterStrategies.LockFreeBitArray) Preconditions.checkNotNull(r5);
        this.numHashFunctions = r6;
        this.funnel = (Funnel) Preconditions.checkNotNull(r7);
        this.strategy = (Strategy) Preconditions.checkNotNull(r8);
        return;
    L5:
        r2 = false;
        goto L6
    }

    /* synthetic */ BloomFilter(BloomFilterStrategies.LockFreeBitArray r1, int r2, Funnel r3, Strategy r4, C08221 r5) {
        this(r1, r2, r3, r4);
    }

    static /* synthetic */ BloomFilterStrategies.LockFreeBitArray access$000(BloomFilter r1) {
        return r1.bits;
    }

    static /* synthetic */ int access$100(BloomFilter r1) {
        return r1.numHashFunctions;
    }

    static /* synthetic */ Funnel access$200(BloomFilter r1) {
        return r1.funnel;
    }

    static /* synthetic */ Strategy access$300(BloomFilter r1) {
        return r1.strategy;
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> r2, int r3) {
        return create(r2, r3);
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> r2, int r3, double r4) {
        return create(r2, r3, r4);
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> r2, long r3) {
        return create(r2, r3, 0.03d);
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> r6, long r7, double r9) {
        return create(r6, r7, r9, BloomFilterStrategies.MURMUR128_MITZ_64);
    }

    static <T> BloomFilter<T> create(Funnel<? super T> r7, long r8, double r10, Strategy r12) {
        Preconditions.checkNotNull(r7);
        boolean r3 = true;
        if (r8 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "Expected insertions (%s) must be >= 0", r8);
        if (r10 <= 0.0d) goto L9;
        boolean r4 = true;
    L10:
        Preconditions.checkArgument(r4, "False positive probability (%s) must be > 0.0", Double.valueOf(r10));
        if (r10 < 1.0d) goto L14;
        r3 = false;
    L14:
        Preconditions.checkArgument(r3, "False positive probability (%s) must be < 1.0", Double.valueOf(r10));
        Preconditions.checkNotNull(r12);
        if (r8 != 0) goto L17;
        r8 = 1;
    L17:
        long r0 = optimalNumOfBits(r8, r10);
        return new BloomFilter(new BloomFilterStrategies.LockFreeBitArray(r0), optimalNumOfHashFunctions(r8, r0), r7, r12);
    L20:
        e = move-exception;
        throw new IllegalArgumentException("Could not create BloomFilter of " + r0 + " bits", e);
    L9:
        r4 = false;
        goto L10
    L5:
        r2 = false;
        goto L6
    }

    static long optimalNumOfBits(long r6, double r8) {
        if (r8 != 0.0d) goto L6;
        r8 = Double.MIN_VALUE;
    L6:
        return (long) (((-r6) * Math.log(r8)) / (Math.log(2.0d) * Math.log(2.0d)));
    }

    static int optimalNumOfHashFunctions(long r4, long r6) {
        return Math.max(1, (int) Math.round((r6 / r4) * Math.log(2.0d)));
    }

    public static <T> BloomFilter<T> readFrom(InputStream r10, Funnel<? super T> r11) throws IOException {
        Preconditions.checkNotNull(r10, "InputStream");
        Preconditions.checkNotNull(r11, "Funnel");
        byte r0 = -1;
        int r1 = -1;
        int r2 = -1;
        DataInputStream r3 = new DataInputStream(r10);     // Catch: Exception -> L8 IOException -> L11
        r0 = r3.readByte();     // Catch: Exception -> L8 IOException -> L11
        r1 = UnsignedBytes.toInt(r3.readByte());     // Catch: Exception -> L8 IOException -> L11
        r2 = r3.readInt();     // Catch: Exception -> L8 IOException -> L11
        BloomFilterStrategies r4 = BloomFilterStrategies.values()[r0];     // Catch: Exception -> L8 IOException -> L11
        BloomFilterStrategies.LockFreeBitArray r5 = new BloomFilterStrategies.LockFreeBitArray(LongMath.checkedMultiply(r2, 64));     // Catch: Exception -> L8 IOException -> L11
        int r6 = 0;
    L4:
        if (r6 >= r2) goto L6;
        r5.putData(r6, r3.readLong());     // Catch: Exception -> L8 IOException -> L11
        r6 = r6 + 1;     // Catch: Exception -> L8 IOException -> L11
        goto L4
    L6:
        return new BloomFilter(r5, r1, r11, r4);
    L11:
        e = move-exception;
        throw e;
    L8:
        e = move-exception;
        throw new IOException("Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + r0 + " numHashFunctions: " + r1 + " dataLength: " + r2, e);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    private Object writeReplace() {
        return new SerialForm(this);
    }

    @Override // com.google.common.base.Predicate
    @Deprecated
    public boolean apply(@ParametricNullness T r2) {
        return mightContain(r2);
    }

    public long approximateElementCount() {
        long r0 = this.bits.bitSize();
        return DoubleMath.roundToLong(((-Math.log1p(-(this.bits.bitCount() / r0))) * r0) / ((double) this.numHashFunctions), RoundingMode.HALF_UP);
    }

    long bitSize() {
        return this.bits.bitSize();
    }

    public BloomFilter<T> copy() {
        return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
    }

    @Override // com.google.common.base.Predicate
    public boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof BloomFilter) == false) goto L18;
        BloomFilter r1 = (BloomFilter) r6;
        if (this.numHashFunctions != r1.numHashFunctions) goto L17;
        if (this.funnel.equals(r1.funnel) == false) goto L17;
        if (this.bits.equals(r1.bits) == false) goto L17;
        if (this.strategy.equals(r1.strategy) == false) goto L17;
        return true;
    L17:
        return false;
    L18:
        return false;
    }

    public double expectedFpp() {
        return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits});
    }

    public boolean isCompatible(BloomFilter<T> r5) {
        Preconditions.checkNotNull(r5);
        if (this != r5) goto L5;
    L13:
        return false;
    L5:
        if (this.numHashFunctions != r5.numHashFunctions) goto L13;
        if (bitSize() != r5.bitSize()) goto L13;
        if (this.strategy.equals(r5.strategy) == false) goto L13;
        if (this.funnel.equals(r5.funnel) == false) goto L13;
        return true;
    }

    public boolean mightContain(@ParametricNullness T r5) {
        return this.strategy.mightContain(r5, this.funnel, this.numHashFunctions, this.bits);
    }

    public boolean put(@ParametricNullness T r5) {
        return this.strategy.put(r5, this.funnel, this.numHashFunctions, this.bits);
    }

    public void putAll(BloomFilter<T> r10) {
        Preconditions.checkNotNull(r10);
        if (this == r10) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "Cannot combine a BloomFilter with itself.");
        if (this.numHashFunctions != r10.numHashFunctions) goto L9;
        boolean r3 = true;
    L10:
        Preconditions.checkArgument(r3, "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, r10.numHashFunctions);
        if (bitSize() != r10.bitSize()) goto L13;
        boolean r4 = true;
    L14:
        Preconditions.checkArgument(r4, "BloomFilters must have the same size underlying bit arrays (%s != %s)", bitSize(), r10.bitSize());
        Preconditions.checkArgument(this.strategy.equals(r10.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, r10.strategy);
        Preconditions.checkArgument(this.funnel.equals(r10.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, r10.funnel);
        this.bits.putAll(r10.bits);
        return;
    L13:
        r4 = false;
        goto L14
    L9:
        r3 = false;
        goto L10
    L5:
        r2 = false;
        goto L6
    }

    public void writeTo(OutputStream r5) throws IOException {
        DataOutputStream r0 = new DataOutputStream(r5);
        r0.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
        r0.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
        r0.writeInt(this.bits.data.length());
        int r1 = 0;
    L4:
        if (r1 >= this.bits.data.length()) goto L6;
        r0.writeLong(this.bits.data.get(r1));
        r1 = r1 + 1;
        goto L4
    }
}
