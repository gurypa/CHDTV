package androidx.media3.decoder;

/* JADX INFO: loaded from: classes.dex */
public class CryptoException extends Exception {
    public final int errorCode;

    public CryptoException(int r1, String r2) {
        super(r2);
        this.errorCode = r1;
    }
}
