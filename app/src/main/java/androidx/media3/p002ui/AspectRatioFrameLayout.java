package androidx.media3.p002ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class AspectRatioFrameLayout extends FrameLayout {
    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f;
    public static final int RESIZE_MODE_FILL = 3;
    public static final int RESIZE_MODE_FIT = 0;
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    public static final int RESIZE_MODE_ZOOM = 4;
    private AspectRatioListener aspectRatioListener;
    private final AspectRatioUpdateDispatcher aspectRatioUpdateDispatcher;
    private int resizeMode;
    private float videoAspectRatio;

    /* JADX INFO: renamed from: androidx.media3.ui.AspectRatioFrameLayout$1 */
    static /* synthetic */ class C04501 {
    }

    public interface AspectRatioListener {
        void onAspectRatioUpdated(float r1, float r2, boolean r3);
    }

    private final class AspectRatioUpdateDispatcher implements Runnable {
        private boolean aspectRatioMismatch;
        private boolean isScheduled;
        private float naturalAspectRatio;
        private float targetAspectRatio;
        final /* synthetic */ AspectRatioFrameLayout this$0;

        private AspectRatioUpdateDispatcher(AspectRatioFrameLayout r1) {
            this.this$0 = r1;
        }

        /* synthetic */ AspectRatioUpdateDispatcher(AspectRatioFrameLayout r1, C04501 r2) {
            this(r1);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.isScheduled = false;
            if (AspectRatioFrameLayout.access$100(this.this$0) != null) goto L5;
            return;
        L5:
            AspectRatioFrameLayout.access$100(this.this$0).onAspectRatioUpdated(this.targetAspectRatio, this.naturalAspectRatio, this.aspectRatioMismatch);
        }

        public void scheduleUpdate(float r2, float r3, boolean r4) {
            this.targetAspectRatio = r2;
            this.naturalAspectRatio = r3;
            this.aspectRatioMismatch = r4;
            if (this.isScheduled == true) goto L6;
            this.isScheduled = true;
            this.this$0.post(this);
            return;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResizeMode {
    }

    public AspectRatioFrameLayout(Context r2) {
        this(r2, null);
    }

    public AspectRatioFrameLayout(Context r4, AttributeSet r5) {
        super(r4, r5);
        this.resizeMode = 0;
        if (r5 == null) goto L10;
        TypedArray r1 = r4.getTheme().obtainStyledAttributes(r5, C0467R.styleable.AspectRatioFrameLayout, 0, 0);
        this.resizeMode = r1.getInt(C0467R.styleable.AspectRatioFrameLayout_resize_mode, 0);     // Catch: Throwable -> L7
        r1.recycle();
    L7:
        th = move-exception;
        r1.recycle();
        throw th;
    L10:
        this.aspectRatioUpdateDispatcher = new AspectRatioUpdateDispatcher(this, null);
    }

    static /* synthetic */ AspectRatioListener access$100(AspectRatioFrameLayout r1) {
        return r1.aspectRatioListener;
    }

    public int getResizeMode() {
        return this.resizeMode;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int r8, int r9) {
        super.onMeasure(r8, r9);
        if (this.videoAspectRatio > 0.0f) goto L5;
        return;
    L5:
        int r0 = getMeasuredWidth();
        int r2 = getMeasuredHeight();
        float r3 = r0 / r2;
        float r4 = (this.videoAspectRatio / r3) - 1.0f;
        if (Math.abs(r4) > MAX_ASPECT_RATIO_DEFORMATION_FRACTION) goto L10;
        this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, r3, false);
        return;
    L10:
        switch(this.resizeMode) {
            case 0: goto L19;
            case 1: goto L17;
            case 2: goto L16;
            case 3: goto L22;
            case 4: goto L13;
            default: goto L22;
        };
    L16:
        r0 = (int) (r2 * this.videoAspectRatio);
        goto L22
    L17:
        r2 = (int) (r0 / this.videoAspectRatio);
    L22:
        this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, r3, true);
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(r0, 1073741824), View.MeasureSpec.makeMeasureSpec(r2, 1073741824));
        return;
    L13:
        if (r4 <= 0.0f) goto L15;
        r0 = (int) (r2 * this.videoAspectRatio);
        goto L22
    L15:
        r2 = (int) (r0 / this.videoAspectRatio);
        goto L22
    L19:
        if (r4 <= 0.0f) goto L21;
        r2 = (int) (r0 / this.videoAspectRatio);
        goto L22
    L21:
        r0 = (int) (r2 * this.videoAspectRatio);
        goto L22
    }

    public void setAspectRatio(float r2) {
        if (this.videoAspectRatio == r2) goto L6;
        this.videoAspectRatio = r2;
        requestLayout();
        return;
    }

    public void setAspectRatioListener(AspectRatioListener r1) {
        this.aspectRatioListener = r1;
    }

    public void setResizeMode(int r2) {
        if (this.resizeMode == r2) goto L6;
        this.resizeMode = r2;
        requestLayout();
        return;
    }
}
