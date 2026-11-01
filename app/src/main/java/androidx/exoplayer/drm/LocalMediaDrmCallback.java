package androidx.media3.exoplayer.drm;

import androidx.media3.common.util.Assertions;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class LocalMediaDrmCallback implements MediaDrmCallback {
    private final byte[] keyResponse;

    public LocalMediaDrmCallback(byte[] r2) {
        this.keyResponse = (byte[]) Assertions.checkNotNull(r2);
    }

    @Override // androidx.media3.exoplayer.drm.MediaDrmCallback
    public byte[] executeKeyRequest(UUID r2, ExoMediaDrm.KeyRequest r3) {
        return this.keyResponse;
    }

    @Override // androidx.media3.exoplayer.drm.MediaDrmCallback
    public byte[] executeProvisionRequest(UUID r2, ExoMediaDrm.ProvisionRequest r3) {
        throw new UnsupportedOperationException();
    }
}
