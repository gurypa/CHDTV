package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.zip.Checksum;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
final class ChecksumHashFunction extends AbstractHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int bits;
    private final ImmutableSupplier<? extends Checksum> checksumSupplier;
    private final String toString;

    /* JADX INFO: renamed from: com.google.common.hash.ChecksumHashFunction$1 */
    static /* synthetic */ class C08251 {
    }

    private final class ChecksumHasher extends AbstractByteHasher {
        private final Checksum checksum;
        final /* synthetic */ ChecksumHashFunction this$0;

        private ChecksumHasher(ChecksumHashFunction r1, Checksum r2) {
            this.this$0 = r1;
            this.checksum = (Checksum) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ ChecksumHasher(ChecksumHashFunction r1, Checksum r2, C08251 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            long r0 = this.checksum.getValue();
            if (ChecksumHashFunction.access$100(this.this$0) != 32) goto L7;
            return HashCode.fromInt((int) r0);
        L7:
            return HashCode.fromLong(r0);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte r2) {
            this.checksum.update(r2);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] r2, int r3, int r4) {
            this.checksum.update(r2, r3, r4);
        }
    }

    ChecksumHashFunction(ImmutableSupplier<? extends Checksum> r3, int r4, String r5) {
        this.checksumSupplier = (ImmutableSupplier) Preconditions.checkNotNull(r3);
        if (r4 != 32) goto L5;
    L8:
        boolean r0 = true;
    L9:
        Preconditions.checkArgument(r0, "bits (%s) must be either 32 or 64", r4);
        this.bits = r4;
        this.toString = (String) Preconditions.checkNotNull(r5);
        return;
    L5:
        if (r4 == 64) goto L8;
        r0 = false;
        goto L9
    }

    static /* synthetic */ int access$100(ChecksumHashFunction r1) {
        return r1.bits;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return this.bits;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new ChecksumHasher(this, this.checksumSupplier.get(), null);
    }

    public String toString() {
        return this.toString;
    }
}
