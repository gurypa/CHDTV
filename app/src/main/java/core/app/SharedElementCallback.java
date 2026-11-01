package androidx.core.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class SharedElementCallback {
    private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
    private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
    private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
    private static final int MAX_IMAGE_SIZE = 1048576;
    private Matrix mTempMatrix;

    public interface OnSharedElementsReadyListener {
        void onSharedElementsReady();
    }

    public SharedElementCallback() {
    }

    private static Bitmap createDrawableBitmap(Drawable r13) {
        int r0 = r13.getIntrinsicWidth();
        int r1 = r13.getIntrinsicHeight();
        if (r0 <= 0) goto L14;
        if (r1 <= 0) goto L16;
        float r3 = Math.min(1.0f, 1048576.0f / (r0 * r1));
        if ((r13 instanceof BitmapDrawable) == true) goto L9;
    L12:
        int r2 = (int) (r0 * r3);
        int r4 = (int) (r1 * r3);
        Bitmap r5 = Bitmap.createBitmap(r2, r4, Bitmap.Config.ARGB_8888);
        Canvas r6 = new Canvas(r5);
        Rect r7 = r13.getBounds();
        int r8 = r7.left;
        int r9 = r7.top;
        int r10 = r7.right;
        int r11 = r7.bottom;
        r13.setBounds(0, 0, r2, r4);
        r13.draw(r6);
        r13.setBounds(r8, r9, r10, r11);
        return r5;
    L9:
        if (r3 != 1.0f) goto L12;
        return ((BitmapDrawable) r13).getBitmap();
    L16:
        return null;
    L14:
        return null;
    }

    public Parcelable onCaptureSharedElementSnapshot(View r9, Matrix r10, RectF r11) {
        if ((r9 instanceof ImageView) == false) goto L13;
        ImageView r0 = (ImageView) r9;
        Drawable r1 = r0.getDrawable();
        Drawable r2 = r0.getBackground();
        if (r1 == null) goto L13;
        if (r2 != null) goto L13;
        Bitmap r3 = createDrawableBitmap(r1);
        if (r3 == null) goto L13;
        Bundle r4 = new Bundle();
        r4.putParcelable(BUNDLE_SNAPSHOT_BITMAP, r3);
        r4.putString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE, r0.getScaleType().toString());
        if (r0.getScaleType() != ImageView.ScaleType.MATRIX) goto L12;
        float[] r6 = new float[9];
        r0.getImageMatrix().getValues(r6);
        r4.putFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX, r6);
    L12:
        return r4;
    L13:
        int r5 = Math.round(r11.width());
        int r7 = Math.round(r11.height());
        if (r5 <= 0) goto L21;
        if (r7 <= 0) goto L22;
        float r8 = Math.min(1.0f, 1048576.0f / (r5 * r7));
        int r12 = (int) (r5 * r8);
        int r13 = (int) (r7 * r8);
        if (this.mTempMatrix != null) goto L19;
        this.mTempMatrix = new Matrix();
    L19:
        this.mTempMatrix.set(r10);
        this.mTempMatrix.postTranslate(-r11.left, -r11.top);
        this.mTempMatrix.postScale(r8, r8);
        Bitmap r14 = Bitmap.createBitmap(r12, r13, Bitmap.Config.ARGB_8888);
        Canvas r15 = new Canvas(r14);
        r15.concat(this.mTempMatrix);
        r9.draw(r15);
        return r14;
    L22:
        return null;
    L21:
        return null;
    }

    public View onCreateSnapshotView(Context r7, Parcelable r8) {
        if ((r8 instanceof Bundle) == false) goto L12;
        Bundle r1 = (Bundle) r8;
        Bitmap r2 = (Bitmap) r1.getParcelable(BUNDLE_SNAPSHOT_BITMAP);
        if (r2 != null) goto L8;
        return null;
    L8:
        ImageView r3 = new ImageView(r7);
        r3.setImageBitmap(r2);
        r3.setScaleType(ImageView.ScaleType.valueOf(r1.getString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE)));
        if (r3.getScaleType() != ImageView.ScaleType.MATRIX) goto L16;
        float[] r4 = r1.getFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX);
        Matrix r5 = new Matrix();
        r5.setValues(r4);
        r3.setImageMatrix(r5);
        return r3;
    L16:
        return r3;
    L12:
        if ((r8 instanceof Bitmap) == false) goto L18;
        ImageView r6 = new ImageView(r7);
        r6.setImageBitmap((Bitmap) r8);
        return r6;
    L18:
        return null;
    }

    public void onMapSharedElements(List<String> r1, Map<String, View> r2) {
    }

    public void onRejectSharedElements(List<View> r1) {
    }

    public void onSharedElementEnd(List<String> r1, List<View> r2, List<View> r3) {
    }

    public void onSharedElementStart(List<String> r1, List<View> r2, List<View> r3) {
    }

    public void onSharedElementsArrived(List<String> r1, List<View> r2, OnSharedElementsReadyListener r3) {
        r3.onSharedElementsReady();
    }
}
