package es.antonio.m3u;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackException;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException;
import androidx.media3.exoplayer.mediacodec.MediaCodecRenderer;
import java.net.UnknownHostException;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes3.dex */
final class PlaybackFailure {
    private PlaybackFailure() {
    }

    static String details(PlaybackException r2) {
        return PlaybackReason222.details(r2) + "\n\n" + details221(r2);
    }

    static String details221(PlaybackException r16) {
        StringBuilder r3 = new StringBuilder("Chdtv ").append("2.27-sampleaes-tv-mobile-test").append("\nAndroid ").append(Build.VERSION.RELEASE).append(" · ").append(Build.MANUFACTURER).append(' ').append(Build.MODEL).append("\n").append(r16.getErrorCodeName());
        HttpDataSource.InvalidResponseCodeException r4 = HttpRetry.response(r16);
        if (r4 == null) goto L8;
        r3.append("\nHTTP: ").append(r4.responseCode);
        if (r4.responseCode != 429) goto L8;
        r3.append("\nEspera antes del reintento: ").append(HttpRetry.delay(r4.headerFields, System.currentTimeMillis()) / 1000).append(" s");
    L8:
        if (r16.getErrorCodeName().startsWith("ERROR_CODE_DRM") == false) goto L11;
        r3.append("\nRevisa claves, licencia y compatibilidad del cifrado. Renovar el token CDN no corrige las claves.");
    L11:
        if ((r16 instanceof ExoPlaybackException) == false) goto L13;
        ExoPlaybackException r5 = (ExoPlaybackException) r16;
        Format r0 = r5.rendererFormat;
        r3.append("\nRenderizador: ").append(r5.rendererName).append("\nCompatibilidad declarada: ").append(r5.rendererFormatSupport);
        Format r6 = r0;
    L14:
        if (r6 == null) goto L17;
        r3.append("\nFormato: ").append(r6.sampleMimeType).append("\nResolución: ").append(r6.width).append(Typography.times).append(r6.height).append("\nFPS: ").append(r6.frameRate);
    L17:
        if (dns(r16) == false) goto L31;
        r3.append("\nFallo DNS: no se pudo resolver el servidor.");
        int r1 = 0;
        Throwable r7 = r16;
    L19:
        if (r7 == null) goto L31;
        int r8 = r1 + 1;
        if (r1 >= 10) goto L31;
        if ((r7 instanceof HttpDataSource.HttpDataSourceException) == false) goto L30;
        DataSpec r2 = ((HttpDataSource.HttpDataSourceException) r7).dataSpec;
        if (r2 != null) goto L27;
        String r9 = null;
    L28:
        if (r9 == null) goto L30;
        r3.append("\nServidor: ").append(r9);
        goto L31
    L27:
        r9 = r2.uri.getHost();
    L30:
        r7 = r7.getCause();
        r1 = r8;
    L31:
        int r10 = 0;
        Throwable r11 = r16;
    L32:
        if (r11 == null) goto L54;
        int r12 = r10 + 1;
        if (r10 >= 10) goto L54;
        r3.append("\nCausa: ").append(r11.getClass().getSimpleName());
        if ((r11 instanceof MediaCodecRenderer.DecoderInitializationException) == false) goto L43;
        MediaCodecRenderer.DecoderInitializationException r13 = (MediaCodecRenderer.DecoderInitializationException) r11;
        StringBuilder r14 = r3.append("\nInicialización: ").append(r13.mimeType).append(" · ");
        if (r13.codecInfo != null) goto L40;
        String r15 = "Sin decodificador seleccionado";
    L41:
        r14.append(r15);
        goto L43
    L40:
        r15 = r13.codecInfo.name;
    L43:
        if ((r11 instanceof MediaCodecDecoderException) == false) goto L50;
        MediaCodecDecoderException r17 = (MediaCodecDecoderException) r11;
        StringBuilder r18 = r3.append("\nDecodificador: ");
        if (r17.codecInfo != null) goto L47;
        String r19 = "Sin identificar";
    L48:
        r18.append(r19);
        goto L50
    L47:
        r19 = r17.codecInfo.name;
    L50:
        if ((r11 instanceof MediaCodec.CodecException) == false) goto L52;
        MediaCodec.CodecException r20 = (MediaCodec.CodecException) r11;
        r3.append("\nCódigo MediaCodec: ").append(r20.getErrorCode()).append("\nTransitorio: ").append(r20.isTransient()).append(" · Recuperable: ").append(r20.isRecoverable());
    L52:
        r11 = r11.getCause();
        r10 = r12;
    L54:
        if (r6 == null) goto L75;
        if (r6.sampleMimeType == null) goto L75;
        r3.append("\nDecodificadores Android para ").append(r6.sampleMimeType).append(':');
        boolean r21 = false;
        MediaCodecInfo[] r22 = new MediaCodecList(1).getCodecInfos();     // Catch: RuntimeException -> L70
        int r23 = r22.length;     // Catch: RuntimeException -> L70
        int r24 = 0;
    L59:
        if (r24 >= r23) goto L72;
        MediaCodecInfo r110 = r22[r24];     // Catch: RuntimeException -> L70
        if (r110.isEncoder() == true) goto L69;
        String[] r111 = r110.getSupportedTypes();     // Catch: RuntimeException -> L70
        int r112 = r111.length;     // Catch: RuntimeException -> L70
        int r113 = 0;
    L63:
        if (r113 >= r112) goto L69;
        if (r111[r113].equalsIgnoreCase(r6.sampleMimeType) == false) goto L68;
        r3.append("\n• ").append(r110.getName());     // Catch: RuntimeException -> L70
        r21 = true;
    L68:
        r113 = r113 + 1;
    L69:
        r24 = r24 + 1;
    L72:
        if (r21 == true) goto L75;
        r3.append("\nNinguno anunciado por Android");
    L71:
        r3.append("\nConsulta no disponible");
        r21 = true;
    L75:
        return r3.toString();
    L13:
        r6 = null;
        goto L14
    }

    static boolean dns(Throwable r3) {
        int r0 = 0;
    L3:
        if (r3 == null) goto L11;
        int r1 = r0 + 1;
        if (r0 >= 10) goto L16;
        if ((r3 instanceof UnknownHostException) == true) goto L8;
        r3 = r3.getCause();
        r0 = r1;
        goto L3
    L8:
        return true;
    L16:
        return false;
    L11:
        return false;
    }

    static String summary(PlaybackException r0) {
        return PlaybackReason222.summary(r0);
    }
}
