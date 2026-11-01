package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
final class MacHashFunction extends AbstractHashFunction {
    private final int bits;
    private final Key key;
    private final Mac prototype;
    private final boolean supportsClone;
    private final String toString;

    /* JADX INFO: renamed from: com.google.common.hash.MacHashFunction$1 */
    static /* synthetic */ class C08351 {
    }

    private static final class MacHasher extends AbstractByteHasher {
        private boolean done;
        private final Mac mac;

        private MacHasher(Mac r1) {
            this.mac = r1;
        }

        /* synthetic */ MacHasher(Mac r1, C08351 r2) {
            this(r1);
        }

        private void checkNotDone() {
            Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            checkNotDone();
            this.done = true;
            return HashCode.fromBytesNoCopy(this.mac.doFinal());
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte r2) {
            checkNotDone();
            this.mac.update(r2);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(ByteBuffer r2) {
            checkNotDone();
            Preconditions.checkNotNull(r2);
            this.mac.update(r2);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] r2) {
            checkNotDone();
            this.mac.update(r2);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] r2, int r3, int r4) {
            checkNotDone();
            this.mac.update(r2, r3, r4);
        }
    }

    MacHashFunction(String r2, Key r3, String r4) {
        this.prototype = getMac(r2, r3);
        this.key = (Key) Preconditions.checkNotNull(r3);
        this.toString = (String) Preconditions.checkNotNull(r4);
        this.bits = this.prototype.getMacLength() * 8;
        this.supportsClone = supportsClone(this.prototype);
    }

    private static Mac getMac(String r2, Key r3) {
        Mac r0 = Mac.getInstance(r2);     // Catch: InvalidKeyException -> L4 NoSuchAlgorithmException -> L7
        r0.init(r3);     // Catch: InvalidKeyException -> L4 NoSuchAlgorithmException -> L7
        return r0;
    L4:
        e = move-exception;
        throw new IllegalArgumentException(e);
    L7:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    private static boolean supportsClone(Mac r2) {
        r2.clone();     // Catch: CloneNotSupportedException -> L5
        return true;
    L6:
        return false;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return this.bits;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        C08351 r1 = null;
        if (this.supportsClone == false) goto L8;
        return new MacHasher((Mac) this.prototype.clone(), r1);
    L8:
        return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key), r1);
    }

    public String toString() {
        return this.toString;
    }
}
