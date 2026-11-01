package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.Checksum;
import javax.annotation.CheckForNull;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Hashing {
    static final int GOOD_FAST_HASH_SEED = 0;

    /* JADX INFO: renamed from: com.google.common.hash.Hashing$1 */
    static /* synthetic */ class C08261 {
    }

    @Immutable
    static abstract enum ChecksumType extends Enum<ChecksumType> implements ImmutableSupplier<Checksum> {
        private static final /* synthetic */ ChecksumType[] $VALUES = null;
        public static final ChecksumType ADLER_32 = null;
        public static final ChecksumType CRC_32 = null;
        public final HashFunction hashFunction;

        private static /* synthetic */ ChecksumType[] $values() {
            return new ChecksumType[]{CRC_32, ADLER_32};
        }

        static {
            final int r1 = 0;
            final String r2 = "Hashing.crc32()";
            final String r3 = "CRC_32";
            CRC_32 = new C08271(r3, r1, r2);
            final int r4 = 1;
            final String r5 = "Hashing.adler32()";
            final String r6 = "ADLER_32";
            ADLER_32 = new C08282(r6, r4, r5);
            $VALUES = $values();
        }

        private ChecksumType(String r1, int r2, String r3) {
            super(r1, r2);
            this.hashFunction = new ChecksumHashFunction(this, 32, r3);
        }

        /* synthetic */ ChecksumType(String r1, int r2, String r3, C08261 r4) {
            this(r1, r2, r3);
        }

        public static ChecksumType valueOf(String r1) {
            return (ChecksumType) Enum.valueOf(ChecksumType.class, r1);
        }

        public static ChecksumType[] values() {
            return (ChecksumType[]) $VALUES.clone();
        }
    }

    private static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
        private ConcatenatedHashFunction(HashFunction... r8) {
            super(r8);
            int r0 = r8.length;
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L9;
            HashFunction r3 = r8[r2];
            if ((r3.bits() % 8) != 0) goto L7;
            boolean r4 = true;
        L8:
            Preconditions.checkArgument(r4, "the number of bits (%s) in hashFunction (%s) must be divisible by 8", r3.bits(), r3);
            r2 = r2 + 1;
            goto L3
        L7:
            r4 = false;
            goto L8
        }

        /* synthetic */ ConcatenatedHashFunction(HashFunction[] r1, C08261 r2) {
            this(r1);
        }

        @Override // com.google.common.hash.HashFunction
        public int bits() {
            int r0 = 0;
            HashFunction[] r1 = this.functions;
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L5;
            r0 = r0 + r1[r3].bits();
            r3 = r3 + 1;
            goto L3
        L5:
            return r0;
        }

        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof ConcatenatedHashFunction) == true) goto L5;
            return false;
        L5:
            return Arrays.equals(this.functions, ((ConcatenatedHashFunction) r4).functions);
        }

        public int hashCode() {
            return Arrays.hashCode(this.functions);
        }

        @Override // com.google.common.hash.AbstractCompositeHashFunction
        HashCode makeHash(Hasher[] r8) {
            byte[] r0 = new byte[bits() / 8];
            int r1 = 0;
            int r2 = r8.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            HashCode r5 = r8[r3].hash();
            r1 = r1 + r5.writeBytesTo(r0, r1, r5.bits() / 8);
            r3 = r3 + 1;
            goto L3
        L6:
            return HashCode.fromBytesNoCopy(r0);
        }
    }

    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long r1) {
            this.state = r1;
        }

        public double nextDouble() {
            this.state = (this.state * 2862933555777941757L) + 1;
            return ((double) (((int) (this.state >>> 33)) + 1)) / 2.147483648E9d;
        }
    }

    private static class Md5Holder {
        static final HashFunction MD5 = null;

        static {
            MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
        }

        private Md5Holder() {
        }
    }

    private static class Sha1Holder {
        static final HashFunction SHA_1 = null;

        static {
            SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
        }

        private Sha1Holder() {
        }
    }

    private static class Sha256Holder {
        static final HashFunction SHA_256 = null;

        static {
            SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
        }

        private Sha256Holder() {
        }
    }

    private static class Sha384Holder {
        static final HashFunction SHA_384 = null;

        static {
            SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
        }

        private Sha384Holder() {
        }
    }

    private static class Sha512Holder {
        static final HashFunction SHA_512 = null;

        static {
            SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
        }

        private Sha512Holder() {
        }
    }

    static {
        GOOD_FAST_HASH_SEED = (int) System.currentTimeMillis();
    }

    private Hashing() {
    }

    public static HashFunction adler32() {
        return ChecksumType.ADLER_32.hashFunction;
    }

    static int checkPositiveAndMakeMultipleOf32(int r2) {
        if (r2 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "Number of bits must be positive");
        return (r2 + 31) & (-32);
    L4:
        r0 = false;
        goto L5
    }

    public static HashCode combineOrdered(Iterable<HashCode> r9) {
        Iterator<HashCode> r0 = r9.iterator();
        Preconditions.checkArgument(r0.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] r2 = new byte[r0.next().bits() / 8];
        Iterator<HashCode> r3 = r9.iterator();
    L4:
        if (r3.hasNext() == false) goto L15;
        byte[] r5 = r3.next().asBytes();
        if (r5.length != r2.length) goto L8;
        boolean r6 = true;
    L9:
        Preconditions.checkArgument(r6, "All hashcodes must have the same bit length.");
        int r7 = 0;
    L11:
        if (r7 >= r5.length) goto L4;
        r2[r7] = (byte) ((r2[r7] * 37) ^ r5[r7]);
        r7 = r7 + 1;
        goto L11
    L8:
        r6 = false;
        goto L9
    L15:
        return HashCode.fromBytesNoCopy(r2);
    }

    public static HashCode combineUnordered(Iterable<HashCode> r8) {
        Iterator<HashCode> r0 = r8.iterator();
        Preconditions.checkArgument(r0.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] r1 = new byte[r0.next().bits() / 8];
        Iterator<HashCode> r2 = r8.iterator();
    L4:
        if (r2.hasNext() == false) goto L15;
        byte[] r4 = r2.next().asBytes();
        if (r4.length != r1.length) goto L8;
        boolean r5 = true;
    L9:
        Preconditions.checkArgument(r5, "All hashcodes must have the same bit length.");
        int r6 = 0;
    L11:
        if (r6 >= r4.length) goto L4;
        r1[r6] = (byte) (r1[r6] + r4[r6]);
        r6 = r6 + 1;
        goto L11
    L8:
        r5 = false;
        goto L9
    L15:
        return HashCode.fromBytesNoCopy(r1);
    }

    public static HashFunction concatenating(HashFunction r4, HashFunction r5, HashFunction... r6) {
        ArrayList r0 = new ArrayList();
        r0.add(r4);
        r0.add(r5);
        Collections.addAll(r0, r6);
        return new ConcatenatedHashFunction((HashFunction[]) r0.toArray(new HashFunction[0]), null);
    }

    public static HashFunction concatenating(Iterable<HashFunction> r4) {
        Preconditions.checkNotNull(r4);
        ArrayList r0 = new ArrayList();
        Iterator<HashFunction> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(r1.next());
        goto L4
    L6:
        Preconditions.checkArgument(!r0.isEmpty(), "number of hash functions (%s) must be > 0", r0.size());
        return new ConcatenatedHashFunction((HashFunction[]) r0.toArray(new HashFunction[0]), null);
    }

    public static int consistentHash(long r6, int r8) {
        if (r8 <= 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "buckets must be positive: %s", r8);
        LinearCongruentialGenerator r1 = new LinearCongruentialGenerator(r6);
        int r2 = 0;
    L6:
        int r3 = (int) (((double) (r2 + 1)) / r1.nextDouble());
        if (r3 < 0) goto L10;
        if (r3 >= r8) goto L10;
        r2 = r3;
    L10:
        return r2;
    L4:
        r0 = false;
        goto L5
    }

    public static int consistentHash(HashCode r2, int r3) {
        return consistentHash(r2.padToLong(), r3);
    }

    public static HashFunction crc32() {
        return ChecksumType.CRC_32.hashFunction;
    }

    public static HashFunction crc32c() {
        return Crc32cHashFunction.CRC_32_C;
    }

    public static HashFunction farmHashFingerprint64() {
        return FarmHashFingerprint64.FARMHASH_FINGERPRINT_64;
    }

    public static HashFunction fingerprint2011() {
        return Fingerprint2011.FINGERPRINT_2011;
    }

    public static HashFunction goodFastHash(int r6) {
        int r0 = checkPositiveAndMakeMultipleOf32(r6);
        if (r0 != 32) goto L7;
        return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
    L7:
        if (r0 <= 128) goto L9;
        int r2 = (r0 + 127) / 128;
        HashFunction[] r1 = new HashFunction[r2];
        r1[0] = Murmur3_128HashFunction.GOOD_FAST_HASH_128;
        int r3 = GOOD_FAST_HASH_SEED;
        int r4 = 1;
    L11:
        if (r4 >= r2) goto L14;
        r3 = r3 + 1500450271;
        r1[r4] = murmur3_128(r3);
        r4 = r4 + 1;
        goto L11
    L14:
        return new ConcatenatedHashFunction(r1, null);
    L9:
        return Murmur3_128HashFunction.GOOD_FAST_HASH_128;
    }

    public static HashFunction hmacMd5(Key r3) {
        return new MacHashFunction("HmacMD5", r3, hmacToString("hmacMd5", r3));
    }

    public static HashFunction hmacMd5(byte[] r3) {
        return hmacMd5(new SecretKeySpec((byte[]) Preconditions.checkNotNull(r3), "HmacMD5"));
    }

    public static HashFunction hmacSha1(Key r3) {
        return new MacHashFunction("HmacSHA1", r3, hmacToString("hmacSha1", r3));
    }

    public static HashFunction hmacSha1(byte[] r3) {
        return hmacSha1(new SecretKeySpec((byte[]) Preconditions.checkNotNull(r3), "HmacSHA1"));
    }

    public static HashFunction hmacSha256(Key r3) {
        return new MacHashFunction("HmacSHA256", r3, hmacToString("hmacSha256", r3));
    }

    public static HashFunction hmacSha256(byte[] r3) {
        return hmacSha256(new SecretKeySpec((byte[]) Preconditions.checkNotNull(r3), "HmacSHA256"));
    }

    public static HashFunction hmacSha512(Key r3) {
        return new MacHashFunction("HmacSHA512", r3, hmacToString("hmacSha512", r3));
    }

    public static HashFunction hmacSha512(byte[] r3) {
        return hmacSha512(new SecretKeySpec((byte[]) Preconditions.checkNotNull(r3), "HmacSHA512"));
    }

    private static String hmacToString(String r2, Key r3) {
        return "Hashing." + r2 + "(Key[algorithm=" + r3.getAlgorithm() + ", format=" + r3.getFormat() + "])";
    }

    @Deprecated
    public static HashFunction md5() {
        return Md5Holder.MD5;
    }

    public static HashFunction murmur3_128() {
        return Murmur3_128HashFunction.MURMUR3_128;
    }

    public static HashFunction murmur3_128(int r1) {
        return new Murmur3_128HashFunction(r1);
    }

    @Deprecated
    public static HashFunction murmur3_32() {
        return Murmur3_32HashFunction.MURMUR3_32;
    }

    @Deprecated
    public static HashFunction murmur3_32(int r2) {
        return new Murmur3_32HashFunction(r2, false);
    }

    public static HashFunction murmur3_32_fixed() {
        return Murmur3_32HashFunction.MURMUR3_32_FIXED;
    }

    public static HashFunction murmur3_32_fixed(int r2) {
        return new Murmur3_32HashFunction(r2, true);
    }

    @Deprecated
    public static HashFunction sha1() {
        return Sha1Holder.SHA_1;
    }

    public static HashFunction sha256() {
        return Sha256Holder.SHA_256;
    }

    public static HashFunction sha384() {
        return Sha384Holder.SHA_384;
    }

    public static HashFunction sha512() {
        return Sha512Holder.SHA_512;
    }

    public static HashFunction sipHash24() {
        return SipHashFunction.SIP_HASH_24;
    }

    public static HashFunction sipHash24(long r7, long r9) {
        return new SipHashFunction(2, 4, r7, r9);
    }
}
