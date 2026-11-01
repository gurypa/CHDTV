package androidx.media3.exoplayer.video;

import android.view.Surface;
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;

/* JADX INFO: loaded from: classes.dex */
public class MediaCodecVideoDecoderException extends MediaCodecDecoderException {
    public final boolean isSurfaceValid;
    public final int surfaceIdentityHashCode;

    public MediaCodecVideoDecoderException(Throwable r2, MediaCodecInfo r3, Surface r4) {
        super(r2, r3);
        this.surfaceIdentityHashCode = System.identityHashCode(r4);
        if (r4 != null) goto L5;
    L8:
        boolean r0 = true;
    L9:
        this.isSurfaceValid = r0;
        return;
    L5:
        if (r4.isValid() == true) goto L8;
        r0 = false;
        goto L9
    }
}
