package com.google.common.hash;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public interface Hasher extends PrimitiveSink {

    /* JADX INFO: renamed from: com.google.common.hash.Hasher$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putBoolean(Hasher r0, boolean r1) {
            return r0.putBoolean(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putByte(Hasher r0, byte r1) {
            return r0.putByte(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putBytes(Hasher r0, ByteBuffer r1) {
            return r0.putBytes(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putBytes(Hasher r0, byte[] r1) {
            return r0.putBytes(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putBytes(Hasher r0, byte[] r1, int r2, int r3) {
            return r0.putBytes(r1, r2, r3);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putChar(Hasher r0, char r1) {
            return r0.putChar(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putDouble(Hasher r0, double r1) {
            return r0.putDouble(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putFloat(Hasher r0, float r1) {
            return r0.putFloat(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putInt(Hasher r0, int r1) {
            return r0.putInt(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putLong(Hasher r0, long r1) {
            return r0.putLong(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putShort(Hasher r0, short r1) {
            return r0.putShort(r1);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putString(Hasher r0, CharSequence r1, Charset r2) {
            return r0.putString(r1, r2);
        }

        public static /* bridge */ /* synthetic */ PrimitiveSink $default$putUnencodedChars(Hasher r0, CharSequence r1) {
            return r0.putUnencodedChars(r1);
        }
    }

    HashCode hash();

    @Deprecated
    int hashCode();

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBoolean(boolean r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putByte(byte r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBytes(ByteBuffer r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBytes(byte[] r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBytes(byte[] r1, int r2, int r3);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putChar(char r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putDouble(double r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putFloat(float r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putInt(int r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putLong(long r1);

    <T> Hasher putObject(@ParametricNullness T r1, Funnel<? super T> r2);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putShort(short r1);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putString(CharSequence r1, Charset r2);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putUnencodedChars(CharSequence r1);
}
