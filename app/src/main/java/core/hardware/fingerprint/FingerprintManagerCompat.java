package androidx.core.hardware.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class FingerprintManagerCompat {
    private final Context mContext;

    static class Api23Impl {
        private Api23Impl() {
        }

        static void authenticate(Object r6, Object r7, CancellationSignal r8, int r9, Object r10, Handler r11) {
            ((FingerprintManager) r6).authenticate((FingerprintManager.CryptoObject) r7, r8, r9, (FingerprintManager.AuthenticationCallback) r10, r11);
        }

        static FingerprintManager.CryptoObject getCryptoObject(Object r1) {
            return ((FingerprintManager.AuthenticationResult) r1).getCryptoObject();
        }

        public static FingerprintManager getFingerprintManagerOrNull(Context r2) {
            if (Build.VERSION.SDK_INT != 23) goto L7;
            return (FingerprintManager) r2.getSystemService(FingerprintManager.class);
        L7:
            if (Build.VERSION.SDK_INT > 23) goto L9;
            return null;
        L9:
            if (r2.getPackageManager().hasSystemFeature("android.hardware.fingerprint") == true) goto L11;
            return null;
        L11:
            return (FingerprintManager) r2.getSystemService(FingerprintManager.class);
        }

        static boolean hasEnrolledFingerprints(Object r1) {
            return ((FingerprintManager) r1).hasEnrolledFingerprints();
        }

        static boolean isHardwareDetected(Object r1) {
            return ((FingerprintManager) r1).isHardwareDetected();
        }

        public static CryptoObject unwrapCryptoObject(Object r3) {
            FingerprintManager.CryptoObject r0 = (FingerprintManager.CryptoObject) r3;
            if (r0 != null) goto L6;
            return null;
        L6:
            if (r0.getCipher() == null) goto L10;
            return new CryptoObject(r0.getCipher());
        L10:
            if (r0.getSignature() == null) goto L14;
            return new CryptoObject(r0.getSignature());
        L14:
            if (r0.getMac() != null) goto L16;
            return null;
        L16:
            return new CryptoObject(r0.getMac());
        }

        public static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject r2) {
            if (r2 != null) goto L6;
            return null;
        L6:
            if (r2.getCipher() == null) goto L10;
            return new FingerprintManager.CryptoObject(r2.getCipher());
        L10:
            if (r2.getSignature() == null) goto L14;
            return new FingerprintManager.CryptoObject(r2.getSignature());
        L14:
            if (r2.getMac() != null) goto L16;
            return null;
        L16:
            return new FingerprintManager.CryptoObject(r2.getMac());
        }
    }

    public static abstract class AuthenticationCallback {
        public AuthenticationCallback() {
        }

        public void onAuthenticationError(int r1, CharSequence r2) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int r1, CharSequence r2) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult r1) {
        }
    }

    public static final class AuthenticationResult {
        private final CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject r1) {
            this.mCryptoObject = r1;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature r2) {
            this.mSignature = r2;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(Cipher r2) {
            this.mCipher = r2;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(Mac r2) {
            this.mMac = r2;
            this.mCipher = null;
            this.mSignature = null;
        }

        public Cipher getCipher() {
            return this.mCipher;
        }

        public Mac getMac() {
            return this.mMac;
        }

        public Signature getSignature() {
            return this.mSignature;
        }
    }

    private FingerprintManagerCompat(Context r1) {
        this.mContext = r1;
    }

    public static FingerprintManagerCompat from(Context r1) {
        return new FingerprintManagerCompat(r1);
    }

    private static FingerprintManager getFingerprintManagerOrNull(Context r1) {
        return Api23Impl.getFingerprintManagerOrNull(r1);
    }

    static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject r1) {
        return Api23Impl.unwrapCryptoObject(r1);
    }

    private static FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback r1) {
        return new C00641(r1);
    }

    private static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject r1) {
        return Api23Impl.wrapCryptoObject(r1);
    }

    public void authenticate(CryptoObject r8, int r9, androidx.core.os.CancellationSignal r10, AuthenticationCallback r11, Handler r12) {
        FingerprintManager r1 = getFingerprintManagerOrNull(this.mContext);
        if (r1 == null) goto L9;
        if (r10 == null) goto L6;
        CancellationSignal r3 = (CancellationSignal) r10.getCancellationSignalObject();
    L7:
        Api23Impl.authenticate(r1, wrapCryptoObject(r8), r3, r9, wrapCallback(r11), r12);
        return;
    L6:
        r3 = null;
        goto L7
    }

    public boolean hasEnrolledFingerprints() {
        FingerprintManager r0 = getFingerprintManagerOrNull(this.mContext);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (Api23Impl.hasEnrolledFingerprints(r0) == false) goto L7;
        return true;
    }

    public boolean isHardwareDetected() {
        FingerprintManager r0 = getFingerprintManagerOrNull(this.mContext);
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (Api23Impl.isHardwareDetected(r0) == false) goto L7;
        return true;
    }
}
