package androidx.core.content.p000pm;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import androidx.media3.common.C0180C;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class PackageInfoCompat {

    private static class Api28Impl {
        private Api28Impl() {
        }

        static Signature[] getApkContentsSigners(SigningInfo r1) {
            return r1.getApkContentsSigners();
        }

        static long getLongVersionCode(PackageInfo r2) {
            return r2.getLongVersionCode();
        }

        static Signature[] getSigningCertificateHistory(SigningInfo r1) {
            return r1.getSigningCertificateHistory();
        }

        static boolean hasMultipleSigners(SigningInfo r1) {
            return r1.hasMultipleSigners();
        }

        static boolean hasSigningCertificate(PackageManager r1, String r2, byte[] r3, int r4) {
            return r1.hasSigningCertificate(r2, r3, r4);
        }
    }

    private PackageInfoCompat() {
    }

    private static boolean byteArrayContains(byte[][] r5, byte[] r6) {
        int r0 = r5.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (Arrays.equals(r6, r5[r2]) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    private static byte[] computeSHA256Digest(byte[] r3) {
        return MessageDigest.getInstance("SHA256").digest(r3);
    L4:
        e = move-exception;
        throw new RuntimeException("Device doesn't support SHA256 cert checking", e);
    }

    public static long getLongVersionCode(PackageInfo r2) {
        if (Build.VERSION.SDK_INT < 28) goto L7;
        return Api28Impl.getLongVersionCode(r2);
    L7:
        return r2.versionCode;
    }

    public static List<Signature> getSignatures(PackageManager r3, String r4) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT < 28) goto L9;
        SigningInfo r1 = r3.getPackageInfo(r4, C0180C.BUFFER_FLAG_FIRST_SAMPLE).signingInfo;
        if (Api28Impl.hasMultipleSigners(r1) == false) goto L7;
        Signature[] r2 = Api28Impl.getApkContentsSigners(r1);
    L10:
        if (r2 != null) goto L14;
        return Collections.emptyList();
    L14:
        return Arrays.asList(r2);
    L7:
        r2 = Api28Impl.getSigningCertificateHistory(r1);
        goto L10
    L9:
        r2 = r3.getPackageInfo(r4, 64).signatures;
        goto L10
    }

    public static boolean hasSignatures(PackageManager r10, String r11, Map<byte[], Integer> r12, boolean r13) throws PackageManager.NameNotFoundException {
        if (r12.isEmpty() == false) goto L5;
        return false;
    L5:
        Set<byte[]> r0 = r12.keySet();
        Iterator<byte[]> r2 = r0.iterator();
    L7:
        if (r2.hasNext() == false) goto L21;
        byte[] r3 = r2.next();
        if (r3 == null) goto L20;
        Integer r5 = r12.get(r3);
        if (r5 == null) goto L18;
        switch(r5.intValue()) {
            case 0: goto L7;
            case 1: goto L7;
            default: goto L15;
        };
    L15:
        throw new IllegalArgumentException("Unsupported certificate type " + r5 + " when verifying " + r11);
    L18:
        throw new IllegalArgumentException("Type must be specified for cert when verifying " + r11);
    L20:
        throw new IllegalArgumentException("Cert byte array cannot be null when verifying " + r11);
    L21:
        List<Signature> r4 = getSignatures(r10, r11);
        if (r13 == true) goto L34;
        if (Build.VERSION.SDK_INT < 28) goto L34;
        Iterator<byte[]> r6 = r0.iterator();
    L27:
        if (r6.hasNext() == false) goto L32;
        byte[] r7 = r6.next();
        if (Api28Impl.hasSigningCertificate(r10, r11, r7, r12.get(r7).intValue()) == true) goto L27;
        return false;
    L32:
        return true;
    L34:
        if (r4.size() != 0) goto L36;
    L61:
        return false;
    L36:
        if (r12.size() > r4.size()) goto L61;
        if (r13 == true) goto L39;
    L41:
        byte[][] r8 = null;
        if (r12.containsValue(1) == false) goto L47;
        r8 = new byte[r4.size()][];
        int r9 = 0;
    L45:
        if (r9 >= r4.size()) goto L47;
        r8[r9] = computeSHA256Digest(r4.get(r9).toByteArray());
        r9 = r9 + 1;
    L47:
        Iterator<byte[]> r14 = r0.iterator();
        if (r14.hasNext() == false) goto L60;
        byte[] r15 = r14.next();
        Integer r16 = r12.get(r15);
        switch(r16.intValue()) {
            case 0: goto L57;
            case 1: goto L54;
            default: goto L52;
        };
    L52:
        throw new IllegalArgumentException("Unsupported certificate type " + r16);
    L54:
        if (byteArrayContains(r8, r15) == true) goto L59;
        return false;
    L59:
        return true;
    L57:
        if (r4.contains(new Signature(r15)) == true) goto L59;
        return false;
    L60:
        return false;
    L39:
        if (r12.size() == r4.size()) goto L41;
        goto L41
    }
}
