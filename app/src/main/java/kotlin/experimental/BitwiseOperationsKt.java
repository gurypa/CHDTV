package kotlin.experimental;

import kotlin.Metadata;

/* JADX INFO: compiled from: bitwiseOperations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0002\b\u0004\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0005\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f¨\u0006\u0007"}, m421d2 = {"and", "", "other", "", "inv", "or", "xor", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class BitwiseOperationsKt {
    private static final byte and(byte r1, byte r2) {
        return (byte) (r1 & r2);
    }

    private static final short and(short r1, short r2) {
        return (short) (r1 & r2);
    }

    private static final byte inv(byte r1) {
        return (byte) (~r1);
    }

    private static final short inv(short r1) {
        return (short) (~r1);
    }

    /* JADX INFO: renamed from: or */
    private static final byte m444or(byte r1, byte r2) {
        return (byte) (r1 | r2);
    }

    /* JADX INFO: renamed from: or */
    private static final short m445or(short r1, short r2) {
        return (short) (r1 | r2);
    }

    private static final byte xor(byte r1, byte r2) {
        return (byte) (r1 ^ r2);
    }

    private static final short xor(short r1, short r2) {
        return (short) (r1 ^ r2);
    }
}
