package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
final class MessageDigestHashFunction extends AbstractHashFunction implements Serializable {
    private final int bytes;
    private final MessageDigest prototype;
    private final boolean supportsClone;
    private final String toString;

    /* JADX INFO: renamed from: com.google.common.hash.MessageDigestHashFunction$1 */
    static /* synthetic */ class C08361 {
    }

    private static final class MessageDigestHasher extends AbstractByteHasher {
        private final int bytes;
        private final MessageDigest digest;
        private boolean done;

        private MessageDigestHasher(MessageDigest r1, int r2) {
            this.digest = r1;
            this.bytes = r2;
        }

        /* synthetic */ MessageDigestHasher(MessageDigest r1, int r2, C08361 r3) {
            this(r1, r2);
        }

        private void checkNotDone() {
            Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            checkNotDone();
            this.done = true;
            if (this.bytes != this.digest.getDigestLength()) goto L6;
            return HashCode.fromBytesNoCopy(this.digest.digest());
        L6:
            return HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte r2) {
            checkNotDone();
            this.digest.update(r2);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(ByteBuffer r2) {
            checkNotDone();
            this.digest.update(r2);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] r2, int r3, int r4) {
            checkNotDone();
            this.digest.update(r2, r3, r4);
        }
    }

    private static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final String algorithmName;
        private final int bytes;
        private final String toString;

        private SerializedForm(String r1, int r2, String r3) {
            this.algorithmName = r1;
            this.bytes = r2;
            this.toString = r3;
        }

        /* synthetic */ SerializedForm(String r1, int r2, String r3, C08361 r4) {
            this(r1, r2, r3);
        }

        private Object readResolve() {
            return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
        }
    }

    MessageDigestHashFunction(String r4, int r5, String r6) {
        this.toString = (String) Preconditions.checkNotNull(r6);
        this.prototype = getMessageDigest(r4);
        int r0 = this.prototype.getDigestLength();
        if (r5 < 4) goto L6;
        if (r5 > r0) goto L6;
        boolean r1 = true;
    L7:
        Preconditions.checkArgument(r1, "bytes (%s) must be >= 4 and < %s", r5, r0);
        this.bytes = r5;
        this.supportsClone = supportsClone(this.prototype);
        return;
    L6:
        r1 = false;
        goto L7
    }

    MessageDigestHashFunction(String r2, String r3) {
        this.prototype = getMessageDigest(r2);
        this.bytes = this.prototype.getDigestLength();
        this.toString = (String) Preconditions.checkNotNull(r3);
        this.supportsClone = supportsClone(this.prototype);
    }

    private static MessageDigest getMessageDigest(String r2) {
        return MessageDigest.getInstance(r2);
    L4:
        e = move-exception;
        throw new AssertionError(e);
    }

    private void readObject(ObjectInputStream r3) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    private static boolean supportsClone(MessageDigest r2) {
        r2.clone();     // Catch: CloneNotSupportedException -> L5
        return true;
    L6:
        return false;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return this.bytes * 8;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        C08361 r1 = null;
        if (this.supportsClone == false) goto L8;
        return new MessageDigestHasher((MessageDigest) this.prototype.clone(), this.bytes, r1);
    L8:
        return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes, r1);
    }

    public String toString() {
        return this.toString;
    }

    Object writeReplace() {
        return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString, null);
    }
}
