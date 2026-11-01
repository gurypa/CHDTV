package androidx.media3.common.util;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.media3.common.MediaMetadata;
import com.google.common.util.concurrent.ListenableFuture;

/* JADX INFO: loaded from: classes.dex */
public interface BitmapLoader {

    /* JADX INFO: renamed from: androidx.media3.common.util.BitmapLoader$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static ListenableFuture $default$loadBitmapFromMetadata(BitmapLoader r1, MediaMetadata r2) {
            if (r2.artworkData == null) goto L6;
            return r1.decodeBitmap(r2.artworkData);
        L6:
            if (r2.artworkUri != null) goto L11;
            return null;
        L11:
            return r1.loadBitmap(r2.artworkUri);
        }
    }

    ListenableFuture<Bitmap> decodeBitmap(byte[] r1);

    ListenableFuture<Bitmap> loadBitmap(Uri r1);

    ListenableFuture<Bitmap> loadBitmapFromMetadata(MediaMetadata r1);

    boolean supportsMimeType(String r1);
}
