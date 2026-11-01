package androidx.media3.datasource;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public final class AesFlushingCipher {
    private final int blockSize;
    private final Cipher cipher;
    private final byte[] flushedBlock;
    private int pendingXorBytes;
    private final byte[] zerosBlock;

    public AesFlushingCipher(int r9, byte[] r10, long r11, long r13) {
        this.cipher = Cipher.getInstance("AES/CTR/NoPadding");     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        this.blockSize = this.cipher.getBlockSize();     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        this.zerosBlock = new byte[this.blockSize];     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        this.flushedBlock = new byte[this.blockSize];     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        long r0 = r13 / ((long) this.blockSize);     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        int r2 = (int) (r13 % ((long) this.blockSize));     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        this.cipher.init(r9, new SecretKeySpec(r10, Util.splitAtFirst(this.cipher.getAlgorithm(), "/")[0]), new IvParameterSpec(getInitializationVector(r11, r0)));     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        if (r2 == 0) goto L19;
        updateInPlace(new byte[r2], 0, r2);     // Catch: Throwable -> L8 InvalidKeyException -> L10 NoSuchPaddingException -> L12 NoSuchAlgorithmException -> L14
        return;
    L19:
        return;
    L8:
        e = move-exception;
        throw new RuntimeException(e);
    }

    public AesFlushingCipher(int r8, byte[] r9, String r10, long r11) {
        this(r8, r9, getFNV64Hash(r10), r11);
    }

    private static long getFNV64Hash(String r7) {
        if (r7 != null) goto L5;
        return 0;
    L5:
        long r0 = 0;
        int r2 = 0;
    L7:
        if (r2 >= r7.length()) goto L9;
        long r1 = r0 ^ ((long) r7.charAt(r2));
        r0 = r1 + ((((((r1 << 1) + (r1 << 4)) + (r1 << 5)) + (r1 << 7)) + (r1 << 8)) + (r1 << 40));
        r2 = r2 + 1;
        goto L7
    L9:
        return r0;
    }

    private byte[] getInitializationVector(long r2, long r4) {
        return ByteBuffer.allocate(16).putLong(r2).putLong(r4).array();
    }

    private int nonFlushingUpdate(byte[] r7, int r8, int r9, byte[] r10, int r11) {
        return this.cipher.update(r7, r8, r9, r10, r11);
    L6:
        e = move-exception;
        ShortBufferException r12 = e;
    L11:
        throw new RuntimeException(r12);
    L8:
        e = move-exception;
        r12 = e;
        goto L11
    }

    public void update(byte[] r14, int r15, int r16, byte[] r17, int r18) {
        int r2 = r15;
        int r3 = r16;
        int r5 = r18;
    L3:
        boolean r6 = true;
        if (this.pendingXorBytes <= 0) goto L8;
        r17[r5] = (byte) (r14[r2] ^ this.flushedBlock[this.blockSize - this.pendingXorBytes]);
        r5 = r5 + 1;
        r2 = r2 + 1;
        this.pendingXorBytes--;
        r3 = r3 - 1;
        if (r3 != 0) goto L3;
        return;
    L8:
        int r7 = nonFlushingUpdate(r14, r2, r3, r17, r5);
        int r9 = r3;
        if (r9 != r7) goto L11;
        return;
    L11:
        int r10 = r9 - r7;
        if (r10 >= this.blockSize) goto L14;
        boolean r1 = true;
    L15:
        Assertions.checkState(r1);
        int r12 = r5 + r7;
        this.pendingXorBytes = this.blockSize - r10;
        if (nonFlushingUpdate(this.zerosBlock, 0, this.pendingXorBytes, this.flushedBlock, 0) == this.blockSize) goto L19;
        r6 = false;
    L19:
        Assertions.checkState(r6);
        int r4 = 0;
    L20:
        if (r4 >= r10) goto L22;
        r17[r12] = this.flushedBlock[r4];
        r4 = r4 + 1;
        r12 = r12 + 1;
        goto L20
    L22:
        return;
    L14:
        r1 = false;
        goto L15
    }

    public void updateInPlace(byte[] r7, int r8, int r9) {
        update(r7, r8, r9, r7, r8);
    }
}
