package es.antonio.m3u;

import android.content.Context;
import androidx.media3.p002ui.AspectRatioFrameLayout;
import androidx.media3.p002ui.C0467R;
import androidx.media3.p002ui.PlayerView;

/* JADX INFO: loaded from: classes3.dex */
final class LogoAwarePlayerView extends PlayerView {
    static final float MAX_ENLARGEMENT = 1.11f;

    LogoAwarePlayerView(Context r1) {
        super(r1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean r7, int r8, int r9, int r10, int r11) {
        super.onLayout(r7, r8, r9, r10, r11);
        AspectRatioFrameLayout r1 = (AspectRatioFrameLayout) findViewById(C0467R.id.exo_content_frame);
        if (r1 != null) goto L5;
        return;
    L5:
        if (r1.getWidth() != 0) goto L7;
        return;
    L7:
        if (r1.getHeight() == 0) goto L16;
        float r2 = 1.0f;
        if (getResizeMode() != 4) goto L12;
        r2 = Math.min(1.0f, MAX_ENLARGEMENT * Math.min(getWidth() / r1.getWidth(), getHeight() / r1.getHeight()));
    L12:
        r1.setPivotX(r1.getWidth() / 2.0f);
        r1.setPivotY(r1.getHeight() / 2.0f);
        r1.setScaleX(r2);
        r1.setScaleY(r2);
        return;
    }
}
