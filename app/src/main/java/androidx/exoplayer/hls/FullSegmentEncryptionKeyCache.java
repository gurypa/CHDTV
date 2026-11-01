package androidx.media3.exoplayer.hls;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import java.util.LinkedHashMap;

/* JADX INFO: loaded from: classes.dex */
final class FullSegmentEncryptionKeyCache {
    private final LinkedHashMap<Uri, byte[]> backingMap;

    public FullSegmentEncryptionKeyCache(final int r7) {
        final int r2 = r7 + 1;
        final float r3 = 1.0f;
        final boolean r4 = false;
        this.backingMap = new C02901(this, r2, r3, r4, r7);
    }

    public boolean containsUri(Uri r3) {
        return this.backingMap.containsKey(Assertions.checkNotNull(r3));
    }

    public byte[] get(Uri r2) {
        if (r2 != null) goto L6;
        return null;
    L6:
        return this.backingMap.get(r2);
    }

    public byte[] put(Uri r4, byte[] r5) {
        return this.backingMap.put((Uri) Assertions.checkNotNull(r4), (byte[]) Assertions.checkNotNull(r5));
    }

    public byte[] remove(Uri r3) {
        return this.backingMap.remove(Assertions.checkNotNull(r3));
    }
}
