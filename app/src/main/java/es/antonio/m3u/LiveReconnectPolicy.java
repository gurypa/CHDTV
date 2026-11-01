package es.antonio.m3u;

import androidx.media3.common.C0180C;
import androidx.media3.common.PlaybackException;
import es.antonio.chdtv.playback.sampleaes.SampleAesProcessor;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/* JADX INFO: loaded from: classes3.dex */
final class LiveReconnectPolicy {
    private static final long[] DELAYS = null;
    private int attempts;

    static {
        DELAYS = new long[]{1000, C0180C.DEFAULT_MAX_SEEK_TO_PREVIOUS_POSITION_MS, 6000};
    }

    LiveReconnectPolicy() {
    }

    static boolean recoverable(PlaybackException r5) {
        if (r5.errorCode < 6000) goto L8;
        if (r5.errorCode > 6008) goto L8;
        return false;
    L8:
        if (HttpRetry.response(r5) == null) goto L11;
        return false;
    L11:
        if (r5.errorCode != 1002) goto L13;
    L45:
        return true;
    L13:
        if (r5.errorCode == 2001) goto L45;
        if (r5.errorCode == 2002) goto L45;
        Throwable r0 = r5;
    L18:
        if (r0 == null) goto L44;
        if ((r0 instanceof SampleAesProcessor.SegmentOutputException) == true) goto L43;
        if ((r0 instanceof SocketTimeoutException) == true) goto L43;
        if ((r0 instanceof ConnectException) == true) goto L43;
        if ((r0 instanceof UnknownHostException) == true) goto L43;
        String r1 = r0.getMessage();
        if ((r0 instanceof IOException) == false) goto L42;
        if (r1 == null) goto L42;
        if (r1.equals("Sample-AES: segmento incompleto") == true) goto L41;
        if (r1.equals("Sample-AES: descarga sin progreso") == true) goto L41;
        if (r1.equals("Sample-AES: el motor devolvió un segmento incompleto") == true) goto L41;
        if (r1.equals("Invalid encrypted MPEG-TS segment") == true) goto L41;
        if (r1.equals("Native decryptor failed") == false) goto L42;
    L41:
        return true;
    L42:
        r0 = r0.getCause();
    L43:
        return true;
    L44:
        return false;
    }

    int attempts() {
        return this.attempts;
    }

    long next(Entry r4, PlaybackException r5) {
        if (r4 != null) goto L4;
        return -1;
    L4:
        if ("Canales".equals(r4.kind) == true) goto L6;
        return -1;
    L6:
        if (recoverable(r5) == true) goto L8;
        return -1;
    L8:
        if (this.attempts == DELAYS.length) goto L16;
        long[] r0 = DELAYS;
        int r1 = this.attempts;
        this.attempts = r1 + 1;
        return r0[r1];
    L16:
        return -1;
    }

    void reset() {
        this.attempts = 0;
    }
}
