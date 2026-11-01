package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractCompositeHashFunction extends AbstractHashFunction {
    private static final long serialVersionUID = 0;
    final HashFunction[] functions;

    AbstractCompositeHashFunction(HashFunction... r4) {
        int r0 = r4.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        Preconditions.checkNotNull(r4[r1]);
        r1 = r1 + 1;
        goto L3
    L5:
        this.functions = r4;
    }

    private Hasher fromHashers(final Hasher[] r2) {
        return new C08211(this, r2);
    }

    abstract HashCode makeHash(Hasher[] r1);

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        Hasher[] r0 = new Hasher[this.functions.length];
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L7;
        r0[r1] = this.functions[r1].newHasher();
        r1 = r1 + 1;
        goto L4
    L7:
        return fromHashers(r0);
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public Hasher newHasher(int r4) {
        if (r4 < 0) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0);
        Hasher[] r1 = new Hasher[this.functions.length];
        int r2 = 0;
    L7:
        if (r2 >= r1.length) goto L10;
        r1[r2] = this.functions[r2].newHasher(r4);
        r2 = r2 + 1;
        goto L7
    L10:
        return fromHashers(r1);
    L4:
        r0 = false;
        goto L5
    }
}
