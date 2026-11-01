package es.antonio.chdtv.playback.sampleaes;

import android.os.Build;
import android.util.Log;
import androidx.media3.extractor.p001ts.TsExtractor;
import com.cineplex.sampleaes.NativeDecryptor;
import com.google.common.base.Ascii;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes5.dex */
public final class SampleAesProcessor {

    public static final enum Result extends Enum<Result> {
        private static final /* synthetic */ Result[] $VALUES = null;
        public static final Result VALID_TS_ENCRYPTION_UNKNOWN = null;

        private static /* synthetic */ Result[] $values() {
            return new Result[]{VALID_TS_ENCRYPTION_UNKNOWN};
        }

        static {
            VALID_TS_ENCRYPTION_UNKNOWN = new Result("VALID_TS_ENCRYPTION_UNKNOWN", 0);
            $VALUES = $values();
        }

        private Result(String r1, int r2) {
            super(r1, r2);
        }

        public static Result valueOf(String r1) {
            return (Result) Enum.valueOf(Result.class, r1);
        }

        public static Result[] values() {
            return (Result[]) $VALUES.clone();
        }
    }

    public static final class SegmentOutputException extends IOException {
        public SegmentOutputException(String r1, Throwable r2) {
            super(r1, r2);
        }
    }

    private SampleAesProcessor() {
    }

    public static byte[] decryptSegment(byte[] r13, byte[] r14, byte[] r15) throws IOException {
        if (r13 == null) goto L90;
        if (r13.length == 0) goto L90;
        if ((r13[0] & 255) == 71) goto L8;
        if (r14 == null) goto L88;
        if (r14.length != 16) goto L88;
        if (r15 == null) goto L88;
        if (r15.length != 16) goto L88;
        byte[] r16 = (byte[]) r13.clone();
        byte[] r17 = (byte[]) r14.clone();
        byte[] r18 = (byte[]) r15.clone();
        int r1 = 0;
    L16:
        int r4 = r1 + 3;
    L80:
        th = move-exception;
        Arrays.fill(r17, (byte) 0);
        Arrays.fill(r18, (byte) 0);
        throw th;
    L82:
        e = move-exception;
        throw new IOException("Sample-AES: no se pudo descifrar el audio AAC", e);     // Catch: Throwable -> L80
    L18:
        if (r4 > r16.length) goto L48;
        if (r16[r1] != 73) goto L48;
        if (r16[r1 + 1] != 68) goto L48;
        if (r16[r1 + 2] != 51) goto L48;
        if ((r16.length - r1) < 10) goto L47;
        int r5 = 6;
        int r8 = 0;
    L29:
        if (r5 >= 10) goto L36;
        int r9 = r16[r1 + r5] & 255;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        if (r9 > 127) goto L35;
        r8 = (r8 << 7) | r9;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        r5 = r5 + 1;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        goto L29
    L35:
        throw new IOException("Sample-AES: tamaño ID3 inválido");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L36:
        long r10 = (((long) r1) + 10) + ((long) r8);     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        if ((r16[r4] & 255) != 4) goto L42;
        if ((r16[r1 + 5] & Ascii.DLE) == 0) goto L42;
        r10 = r10 + 10;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L42:
        if (r10 > r16.length) goto L45;
        r1 = (int) r10;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        goto L16
    L45:
        throw new IOException("Sample-AES: ID3 incompleto");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L47:
        throw new IOException("Sample-AES: ID3 incompleto");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L48:
        Cipher r6 = Cipher.getInstance("AES/CBC/NoPadding");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        int r7 = 0;
    L50:
        if (r1 >= r16.length) goto L75;
        int r11 = 7;
        if ((r16.length - r1) < 7) goto L74;
        if ((r16[r1] & 255) != 255) goto L74;
        int r12 = r1 + 1;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        if ((r16[r12] & 246) != 240) goto L74;
        if ((r16[r12] & 1) == 1) goto L61;
        r11 = 9;
    L61:
        int r19 = (((r16[r1 + 3] & 3) << 11) | ((r16[r1 + 4] & 255) << 3)) | ((r16[r1 + 5] & 255) >>> 5);     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        if (r19 < r11) goto L72;
        if (r19 > (r16.length - r1)) goto L72;
        if ((r16[r1 + 6] & 3) != 0) goto L72;
        int r20 = Math.max(0, ((r19 - r11) - 16) / 16) * 16;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        if (r20 <= 0) goto L70;
        r6.init(2, new SecretKeySpec(r17, "AES"), new IvParameterSpec(r18));     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        int r21 = (r11 + r1) + 16;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        byte[] r22 = r6.doFinal(r16, r21, r20);     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        System.arraycopy(r22, 0, r16, r21, r22.length);     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        Arrays.fill(r22, (byte) 0);     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L70:
        r7 = r7 + 1;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
        r1 = r1 + r19;     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L72:
        throw new IOException("Sample-AES: trama AAC inválida o incompleta");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L74:
        throw new IOException("Sample-AES: el audio necesita tramas AAC ADTS");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L75:
        if (r7 == 0) goto L79;
        Arrays.fill(r17, (byte) 0);
        Arrays.fill(r18, (byte) 0);
        return r16;
    L79:
        throw new IOException("Sample-AES: segmento sin tramas AAC");     // Catch: Throwable -> L80 GeneralSecurityException -> L82
    L88:
        throw new IllegalArgumentException("Se necesitan una clave AES y un IV de 16 bytes");
    L8:
        return decryptTransportStream(r13, r14, r15);
    L90:
        throw new IOException("Sample-AES: segmento vacío");
    }

    public static byte[] decryptTransportStream(byte[] r5, byte[] r6, byte[] r7) throws IOException {
        inspectTransportStream(r5);
        if (Build.VERSION.SDK_INT < 26) goto L32;
        if (r6 == null) goto L30;
        if (r6.length != 16) goto L30;
        if (r7 == null) goto L30;
        if (r7.length != 16) goto L30;
        byte[] r0 = (byte[]) r5.clone();
        byte[] r8 = (byte[]) r6.clone();
        byte[] r9 = (byte[]) r7.clone();
        byte[] r1 = NativeDecryptor.decrypt(r0, r8, r9);     // Catch: Throwable -> L19 RuntimeException -> L21 LinkageError -> L24
        if (r1 != null) goto L14;
    L16:
        byte[] r10 = validateNativeOutput(r5, r1);     // Catch: Throwable -> L19 RuntimeException -> L21 LinkageError -> L24
        Arrays.fill(r8, (byte) 0);
        Arrays.fill(r9, (byte) 0);
        return r10;
    L14:
        if (r1.length == r5.length) goto L16;
        Log.i("CHDTV", "SAMPLE-AES remultiplexado: entrada=" + r5.length + " bytes, salida=" + r1.length + " bytes");     // Catch: Throwable -> L19 RuntimeException -> L21 LinkageError -> L24
    L19:
        th = move-exception;
        Arrays.fill(r8, (byte) 0);
        Arrays.fill(r9, (byte) 0);
        throw th;
    L24:
        e = move-exception;
        throw new IOException("Sample-AES: motor nativo no disponible en este dispositivo", e);     // Catch: Throwable -> L19
    L21:
        e = move-exception;
        throw new IOException("Sample-AES: no se pudo procesar el segmento", e);     // Catch: Throwable -> L19
    L30:
        throw new IllegalArgumentException("Se necesitan una clave AES y un IV de 16 bytes");
    L32:
        throw new IOException("Sample-AES: el motor TS necesita Android 8.0 o posterior");
    }

    public static Result inspectTransportStream(byte[] r3) {
        if (r3 == null) goto L20;
        if (r3.length == 0) goto L20;
        if ((r3.length % TsExtractor.TS_PACKET_SIZE) != 0) goto L18;
        int r0 = 0;
    L9:
        if (r0 >= r3.length) goto L16;
        if ((r3[r0] & 255) != 71) goto L14;
        r0 = r0 + TsExtractor.TS_PACKET_SIZE;
        goto L9
    L14:
        throw new IllegalArgumentException("Sin sincronización MPEG-TS en el paquete " + (r0 / TsExtractor.TS_PACKET_SIZE));
    L16:
        return Result.VALID_TS_ENCRYPTION_UNKNOWN;
    L18:
        throw new IllegalArgumentException("El segmento no está alineado a paquetes MPEG-TS de 188 bytes");
    L20:
        throw new IllegalArgumentException("TS vacío");
    }

    static byte[] validateNativeOutput(byte[] r6, byte[] r7) throws IOException {
        if (r7 != null) goto L4;
        int r0 = 0;
    L6:
        if (r7 == null) goto L16;
        if (r0 == 0) goto L16;
        if (r0 > 33554432) goto L16;
        inspectTransportStream(r7);     // Catch: IllegalArgumentException -> L12
        return r7;
    L12:
        e = move-exception;
        throw new SegmentOutputException("Sample-AES: salida nativa con paquetes incompletos o sin sincronización (entrada=" + r6.length + ", salida=" + r0 + ")", e);
    L16:
        throw new SegmentOutputException("Sample-AES: salida nativa vacía o fuera de límite (entrada=" + r6.length + ", salida=" + r0 + ")", null);
    L4:
        r0 = r7.length;
        goto L6
    }
}
