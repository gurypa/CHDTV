package androidx.media3.exoplayer.drm;

import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class FrameworkCryptoConfig implements CryptoConfig {
    public static final boolean WORKAROUND_DEVICE_NEEDS_KEYS_TO_CONFIGURE_CODEC = false;

    @Deprecated
    public final boolean forceAllowInsecureDecoderComponents;
    public final byte[] sessionId;
    public final UUID uuid;

    static {
        if ("Amazon".equals(Util.MANUFACTURER) == true) goto L5;
    L9:
        boolean r0 = false;
    L10:
        WORKAROUND_DEVICE_NEEDS_KEYS_TO_CONFIGURE_CODEC = r0;
        return;
    L5:
        if ("AFTM".equals(Util.MODEL) == false) goto L7;
    L8:
        r0 = true;
        goto L10
    L7:
        if ("AFTB".equals(Util.MODEL) == false) goto L9;
        goto L8
    }

    public FrameworkCryptoConfig(UUID r2, byte[] r3) {
        this(r2, r3, false);
    }

    @Deprecated
    public FrameworkCryptoConfig(UUID r1, byte[] r2, boolean r3) {
        this.uuid = r1;
        this.sessionId = r2;
        this.forceAllowInsecureDecoderComponents = r3;
    }
}
