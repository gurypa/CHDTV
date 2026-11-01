package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.DecoderException;

/* JADX INFO: loaded from: classes.dex */
public class MediaCodecDecoderException extends DecoderException {
    public final MediaCodecInfo codecInfo;
    public final String diagnosticInfo;
    public final int errorCode;

    public MediaCodecDecoderException(Throwable r4, MediaCodecInfo r5) {
        StringBuilder r0 = new StringBuilder().append("Decoder failed: ");
        String r1 = null;
        if (r5 != null) goto L5;
        String r2 = null;
    L6:
        super(r0.append(r2).toString(), r4);
        this.codecInfo = r5;
        if ((r4 instanceof MediaCodec.CodecException) == false) goto L10;
        r1 = ((MediaCodec.CodecException) r4).getDiagnosticInfo();
    L10:
        this.diagnosticInfo = r1;
        if (Util.SDK_INT < 23) goto L13;
        int r3 = getErrorCodeV23(r4);
    L14:
        this.errorCode = r3;
        return;
    L13:
        r3 = Util.getErrorCodeFromPlatformDiagnosticsInfo(this.diagnosticInfo);
        goto L14
    L5:
        r2 = r5.name;
        goto L6
    }

    private static int getErrorCodeV23(Throwable r1) {
        if ((r1 instanceof MediaCodec.CodecException) == true) goto L5;
        return 0;
    L5:
        return ((MediaCodec.CodecException) r1).getErrorCode();
    }
}
