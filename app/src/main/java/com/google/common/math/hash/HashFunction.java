package com.google.common.hash;

import com.google.errorprone.annotations.Immutable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
public interface HashFunction {
    int bits();

    HashCode hashBytes(ByteBuffer r1);

    HashCode hashBytes(byte[] r1);

    HashCode hashBytes(byte[] r1, int r2, int r3);

    HashCode hashInt(int r1);

    HashCode hashLong(long r1);

    <T> HashCode hashObject(@ParametricNullness T r1, Funnel<? super T> r2);

    HashCode hashString(CharSequence r1, Charset r2);

    HashCode hashUnencodedChars(CharSequence r1);

    Hasher newHasher();

    Hasher newHasher(int r1);
}
