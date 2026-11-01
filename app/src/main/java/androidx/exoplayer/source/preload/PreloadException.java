package androidx.media3.exoplayer.source.preload;

import androidx.media3.common.MediaItem;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class PreloadException extends Exception {
    public final MediaItem mediaItem;

    public PreloadException(MediaItem r1, String r2, Throwable r3) {
        super(r2, r3);
        this.mediaItem = r1;
    }

    public boolean errorInfoEquals(PreloadException r7) {
        if (this != r7) goto L6;
        return true;
    L6:
        if (r7 != null) goto L8;
    L30:
        return false;
    L8:
        if (getClass() != r7.getClass()) goto L30;
        Throwable r2 = getCause();
        Throwable r3 = r7.getCause();
        if (r2 == null) goto L19;
        if (r3 == null) goto L19;
        if (Objects.equals(r2.getMessage(), r3.getMessage()) == true) goto L17;
        return false;
    L17:
        if (Objects.equals(r2.getClass(), r3.getClass()) == true) goto L23;
        return false;
    L23:
        if (Objects.equals(this.mediaItem, r7.mediaItem) == false) goto L28;
        if (Objects.equals(getMessage(), r7.getMessage()) == false) goto L28;
        return true;
    L28:
        return false;
    L19:
        if (r2 != null) goto L29;
        if (r3 == null) goto L23;
    L29:
        return false;
    }
}
