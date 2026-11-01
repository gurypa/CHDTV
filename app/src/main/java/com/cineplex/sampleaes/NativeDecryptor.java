package com.cineplex.sampleaes;

/* JADX INFO: loaded from: classes6.dex */
public final class NativeDecryptor {
    static {
        System.loadLibrary("sampleaes_jni");
    }

    private NativeDecryptor() {
    }

    public static native byte[] decrypt(byte[] r0, byte[] r1, byte[] r2);
}
