package androidx.media3.exoplayer.image;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public interface ImageOutput {
    public static final ImageOutput NO_OP = null;

    static {
        NO_OP = new C03031();
    }

    void onDisabled();

    void onImageAvailable(long r1, Bitmap r3);
}
