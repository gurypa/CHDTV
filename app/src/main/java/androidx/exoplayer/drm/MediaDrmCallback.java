package androidx.media3.exoplayer.drm;

import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public interface MediaDrmCallback {
    byte[] executeKeyRequest(UUID r1, ExoMediaDrm.KeyRequest r2) throws MediaDrmCallbackException;

    byte[] executeProvisionRequest(UUID r1, ExoMediaDrm.ProvisionRequest r2) throws MediaDrmCallbackException;
}
