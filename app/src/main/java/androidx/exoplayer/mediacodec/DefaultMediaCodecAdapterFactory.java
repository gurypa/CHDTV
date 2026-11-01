package androidx.media3.exoplayer.mediacodec;

import android.content.Context;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultMediaCodecAdapterFactory implements MediaCodecAdapter.Factory {
    private static final int MODE_DEFAULT = 0;
    private static final int MODE_DISABLED = 2;
    private static final int MODE_ENABLED = 1;
    private static final String TAG = "DMCodecAdapterFactory";
    private boolean asyncCryptoFlagEnabled;
    private int asynchronousMode;
    private final Context context;

    @Deprecated
    public DefaultMediaCodecAdapterFactory() {
        this.asynchronousMode = 0;
        this.asyncCryptoFlagEnabled = false;
        this.context = null;
    }

    public DefaultMediaCodecAdapterFactory(Context r2) {
        this.context = r2;
        this.asynchronousMode = 0;
        this.asyncCryptoFlagEnabled = false;
    }

    private boolean shouldUseAsynchronousAdapterInDefaultMode() {
        if (Util.SDK_INT < 31) goto L6;
        return true;
    L6:
        if (this.context != null) goto L8;
        return false;
    L8:
        if (Util.SDK_INT >= 28) goto L10;
        return false;
    L10:
        if (this.context.getPackageManager().hasSystemFeature("com.amazon.hardware.tv_screen") == false) goto L15;
        return true;
    L15:
        return false;
    }

    @Override // androidx.media3.exoplayer.mediacodec.MediaCodecAdapter.Factory
    public MediaCodecAdapter createAdapter(MediaCodecAdapter.Configuration r4) throws IOException {
        if (Util.SDK_INT < 23) goto L13;
        if (this.asynchronousMode != 1) goto L7;
    L10:
        int r0 = MimeTypes.getTrackType(r4.format.sampleMimeType);
        Log.m46i(TAG, "Creating an asynchronous MediaCodec adapter for track type " + Util.getTrackTypeString(r0));
        AsynchronousMediaCodecAdapter.Factory r1 = new AsynchronousMediaCodecAdapter.Factory(r0);
        r1.experimentalSetAsyncCryptoFlagEnabled(this.asyncCryptoFlagEnabled);
        return r1.createAdapter(r4);
    L7:
        if (this.asynchronousMode != 0) goto L13;
        if (shouldUseAsynchronousAdapterInDefaultMode() == true) goto L10;
    L13:
        return new SynchronousMediaCodecAdapter.Factory().createAdapter(r4);
    }

    public DefaultMediaCodecAdapterFactory experimentalSetAsyncCryptoFlagEnabled(boolean r1) {
        this.asyncCryptoFlagEnabled = r1;
        return this;
    }

    public DefaultMediaCodecAdapterFactory forceDisableAsynchronous() {
        this.asynchronousMode = 2;
        return this;
    }

    public DefaultMediaCodecAdapterFactory forceEnableAsynchronous() {
        this.asynchronousMode = 1;
        return this;
    }
}
