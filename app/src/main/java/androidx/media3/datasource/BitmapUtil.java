package androidx.media3.datasource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import androidx.media3.common.ParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class BitmapUtil {
    private BitmapUtil() {
    }

    public static Bitmap decode(byte[] r10, int r11, BitmapFactory.Options r12, int r13) throws IOException {
        if (r13 == (-1)) goto L9;
        if (r12 != null) goto L6;
        r12 = new BitmapFactory.Options();
    L6:
        r12.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(r10, 0, r11, r12);
        int r0 = Math.max(r12.outWidth, r12.outHeight);
        r12.inJustDecodeBounds = false;
        r12.inSampleSize = 1;
    L7:
        if (r0 <= r13) goto L9;
        r12.inSampleSize *= 2;
        r0 = r0 / 2;
    L9:
        Bitmap r3 = BitmapFactory.decodeByteArray(r10, 0, r11, r12);
        if (r12 == null) goto L12;
        r12.inSampleSize = 1;
    L12:
        if (r3 == null) goto L27;
        ByteArrayInputStream r1 = new ByteArrayInputStream(r10);
        ExifInterface r2 = new ExifInterface(r1);     // Catch: Throwable -> L19
        r1.close();
        int r4 = r2.getRotationDegrees();
        if (r4 == 0) goto L33;
        Matrix r8 = new Matrix();
        r8.postRotate(r4);
        return Bitmap.createBitmap(r3, 0, 0, r3.getWidth(), r3.getHeight(), r8, false);
    L33:
        return r3;
    L19:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L23
        throw th;
    L23:
        th = move-exception;
        th.addSuppressed(th);
        throw th;
    L27:
        throw ParserException.createForMalformedContainer("Could not decode image data", new IllegalStateException());
    }
}
