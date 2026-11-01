package com.google.common.hash;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class HashCode {
    private static final char[] hexDigits = null;

    private static final class BytesHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final byte[] bytes;

        BytesHashCode(byte[] r2) {
            this.bytes = (byte[]) Preconditions.checkNotNull(r2);
        }

        @Override // com.google.common.hash.HashCode
        public byte[] asBytes() {
            return (byte[]) this.bytes.clone();
        }

        @Override // com.google.common.hash.HashCode
        public int asInt() {
            if (this.bytes.length < 4) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
            return (((this.bytes[0] & 255) | ((this.bytes[1] & 255) << 8)) | ((this.bytes[2] & 255) << 16)) | ((this.bytes[3] & 255) << 24);
        L5:
            r0 = false;
            goto L6
        }

        @Override // com.google.common.hash.HashCode
        public long asLong() {
            if (this.bytes.length < 8) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
            return padToLong();
        L5:
            r0 = false;
            goto L6
        }

        @Override // com.google.common.hash.HashCode
        public int bits() {
            return this.bytes.length * 8;
        }

        @Override // com.google.common.hash.HashCode
        boolean equalsSameBits(HashCode r6) {
            if (this.bytes.length == r6.getBytesInternal().length) goto L5;
            return false;
        L5:
            boolean r0 = true;
            int r1 = 0;
        L7:
            if (r1 >= this.bytes.length) goto L13;
            if (this.bytes[r1] != r6.getBytesInternal()[r1]) goto L11;
            boolean r3 = true;
        L12:
            r0 = r0 & r3;
            r1 = r1 + 1;
            goto L7
        L11:
            r3 = false;
            goto L12
        L13:
            return r0;
        }

        @Override // com.google.common.hash.HashCode
        byte[] getBytesInternal() {
            return this.bytes;
        }

        @Override // com.google.common.hash.HashCode
        public long padToLong() {
            long r0 = this.bytes[0] & 255;
            int r2 = 1;
        L4:
            if (r2 >= Math.min(this.bytes.length, 8)) goto L6;
            r0 = r0 | ((((long) this.bytes[r2]) & 255) << (r2 * 8));
            r2 = r2 + 1;
            goto L4
        L6:
            return r0;
        }

        @Override // com.google.common.hash.HashCode
        void writeBytesToImpl(byte[] r3, int r4, int r5) {
            System.arraycopy(this.bytes, 0, r3, r4, r5);
        }
    }

    private static final class IntHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final int hash;

        IntHashCode(int r1) {
            this.hash = r1;
        }

        @Override // com.google.common.hash.HashCode
        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24)};
        }

        @Override // com.google.common.hash.HashCode
        public int asInt() {
            return this.hash;
        }

        @Override // com.google.common.hash.HashCode
        public long asLong() {
            throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
        }

        @Override // com.google.common.hash.HashCode
        public int bits() {
            return 32;
        }

        @Override // com.google.common.hash.HashCode
        boolean equalsSameBits(HashCode r3) {
            if (this.hash != r3.asInt()) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.hash.HashCode
        public long padToLong() {
            return UnsignedInts.toLong(this.hash);
        }

        @Override // com.google.common.hash.HashCode
        void writeBytesToImpl(byte[] r5, int r6, int r7) {
            int r0 = 0;
        L3:
            if (r0 >= r7) goto L5;
            r5[r6 + r0] = (byte) (this.hash >> (r0 * 8));
            r0 = r0 + 1;
            goto L3
        }
    }

    private static final class LongHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final long hash;

        LongHashCode(long r1) {
            this.hash = r1;
        }

        @Override // com.google.common.hash.HashCode
        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24), (byte) (this.hash >> 32), (byte) (this.hash >> 40), (byte) (this.hash >> 48), (byte) (this.hash >> 56)};
        }

        @Override // com.google.common.hash.HashCode
        public int asInt() {
            return (int) this.hash;
        }

        @Override // com.google.common.hash.HashCode
        public long asLong() {
            return this.hash;
        }

        @Override // com.google.common.hash.HashCode
        public int bits() {
            return 64;
        }

        @Override // com.google.common.hash.HashCode
        boolean equalsSameBits(HashCode r5) {
            if (this.hash != r5.asLong()) goto L5;
            return true;
        L5:
            return false;
        }

        @Override // com.google.common.hash.HashCode
        public long padToLong() {
            return this.hash;
        }

        @Override // com.google.common.hash.HashCode
        void writeBytesToImpl(byte[] r6, int r7, int r8) {
            int r0 = 0;
        L3:
            if (r0 >= r8) goto L5;
            r6[r7 + r0] = (byte) (this.hash >> (r0 * 8));
            r0 = r0 + 1;
            goto L3
        }
    }

    static {
        hexDigits = "0123456789abcdef".toCharArray();
    }

    HashCode() {
    }

    private static int decode(char r3) {
        if (r3 < '0') goto L9;
        if (r3 > '9') goto L9;
        return r3 - '0';
    L9:
        if (r3 < 'a') goto L15;
        if (r3 > 'f') goto L15;
        return (r3 - 'a') + 10;
    L15:
        throw new IllegalArgumentException("Illegal hexadecimal character: " + r3);
    }

    public static HashCode fromBytes(byte[] r2) {
        boolean r1 = true;
        if (r2.length >= 1) goto L6;
        r1 = false;
    L6:
        Preconditions.checkArgument(r1, "A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy((byte[]) r2.clone());
    }

    static HashCode fromBytesNoCopy(byte[] r1) {
        return new BytesHashCode(r1);
    }

    public static HashCode fromInt(int r1) {
        return new IntHashCode(r1);
    }

    public static HashCode fromLong(long r1) {
        return new LongHashCode(r1);
    }

    public static HashCode fromString(String r6) {
        boolean r1 = false;
        if (r6.length() < 2) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "input string (%s) must have at least 2 characters", r6);
        if ((r6.length() % 2) != 0) goto L9;
        r1 = true;
    L9:
        Preconditions.checkArgument(r1, "input string (%s) must have an even number of characters", r6);
        byte[] r2 = new byte[r6.length() / 2];
        int r3 = 0;
    L11:
        if (r3 >= r6.length()) goto L14;
        r2[r3 / 2] = (byte) ((decode(r6.charAt(r3)) << 4) + decode(r6.charAt(r3 + 1)));
        r3 = r3 + 2;
        goto L11
    L14:
        return fromBytesNoCopy(r2);
    L5:
        r0 = false;
        goto L6
    }

    public abstract byte[] asBytes();

    public abstract int asInt();

    public abstract long asLong();

    public abstract int bits();

    public final boolean equals(@CheckForNull Object r5) {
        if ((r5 instanceof HashCode) == false) goto L10;
        HashCode r0 = (HashCode) r5;
        if (bits() == r0.bits()) goto L7;
        return false;
    L7:
        if (equalsSameBits(r0) == false) goto L12;
        return true;
    L12:
        return false;
    L10:
        return false;
    }

    abstract boolean equalsSameBits(HashCode r1);

    byte[] getBytesInternal() {
        return asBytes();
    }

    public final int hashCode() {
        if (bits() >= 32) goto L5;
        byte[] r0 = getBytesInternal();
        int r1 = r0[0] & 255;
        int r2 = 1;
    L8:
        if (r2 >= r0.length) goto L10;
        r1 = r1 | ((r0[r2] & 255) << (r2 * 8));
        r2 = r2 + 1;
        goto L8
    L10:
        return r1;
    L5:
        return asInt();
    }

    public abstract long padToLong();

    public final String toString() {
        byte[] r0 = getBytesInternal();
        StringBuilder r1 = new StringBuilder(r0.length * 2);
        int r2 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L6;
        byte r4 = r0[r3];
        r1.append(hexDigits[(r4 >> 4) & 15]).append(hexDigits[r4 & Ascii.f88SI]);
        r3 = r3 + 1;
        goto L3
    L6:
        return r1.toString();
    }

    public int writeBytesTo(byte[] r3, int r4, int r5) {
        int r6 = Ints.min(new int[]{r5, bits() / 8});
        Preconditions.checkPositionIndexes(r4, r4 + r6, r3.length);
        writeBytesToImpl(r3, r4, r6);
        return r6;
    }

    abstract void writeBytesToImpl(byte[] r1, int r2, int r3);
}
