package es.antonio.m3u;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes3.dex */
final class ProviderArtwork {

    private static final class Wordmark extends Drawable {
        private final String label;
        private final Paint paint;

        Wordmark(String r4) {
            this.paint = new Paint(1);
            this.label = r4;
            this.paint.setColor(-8790273);
            this.paint.setTypeface(Typeface.create(C0180C.SANS_SERIF_NAME, 1));
            this.paint.setTextAlign(Paint.Align.CENTER);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas r8) {
            Rect r0 = getBounds();
            this.paint.setTextSize(r0.height() * 0.24f);
            float r1 = this.paint.measureText(this.label);
            if (r1 <= (r0.width() * 0.9f)) goto L5;
            this.paint.setTextSize(((this.paint.getTextSize() * r0.width()) * 0.9f) / r1);
        L5:
            r8.drawText(this.label, r0.exactCenterX(), r0.exactCenterY() - ((this.paint.ascent() + this.paint.descent()) / 2.0f), this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int r2) {
            this.paint.setAlpha(r2);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter r2) {
            this.paint.setColorFilter(r2);
        }
    }

    private ProviderArtwork() {
    }

    static void show(ImageView r3, String r4) {
        r3.clearColorFilter();
        int r0 = 0;
        String r1 = Providers.base(r4);
        switch(r1.hashCode()) {
            case -2052827562: goto L30;
            case -1924984242: goto L27;
            case -1833722525: goto L24;
            case -958615497: goto L21;
            case -787338382: goto L18;
            case -35146809: goto L15;
            case 2091121: goto L12;
            case 1439268153: goto L9;
            case 2021119269: goto L6;
            default: goto L32;
        };
    L32:
        byte r2 = -1;
    L33:
        switch(r2) {
            case 0: goto L43;
            case 1: goto L42;
            case 2: goto L41;
            case 3: goto L40;
            case 4: goto L39;
            case 5: goto L38;
            case 6: goto L37;
            case 7: goto L36;
            case 8: goto L35;
            default: goto L44;
        };
    L35:
        r0 = C1028R.drawable.provider_vodafone;
        goto L44
    L36:
        r0 = C1028R.drawable.provider_dazn;
        goto L44
    L37:
        r0 = C1028R.drawable.provider_netflix;
        goto L44
    L38:
        r0 = C1028R.drawable.provider_orange;
        goto L44
    L39:
        r0 = C1028R.drawable.provider_movistar;
        goto L44
    L40:
        r0 = C1028R.drawable.provider_hbo;
        goto L44
    L41:
        r0 = C1028R.drawable.provider_primevideo;
        goto L44
    L42:
        r0 = C1028R.drawable.provider_disney;
        goto L44
    L43:
        r0 = C1028R.drawable.provider_clictv;
    L44:
        if (r0 == 0) goto L46;
        r3.setImageResource(r0);
        return;
    L46:
        r3.setImageDrawable(new Wordmark(r4));
        return;
    L6:
        if (r1.equals("ClicTV") == false) goto L32;
        r2 = 0;
        goto L33
    L9:
        if (r1.equals("HBO Max") == false) goto L32;
        r2 = 3;
        goto L33
    L12:
        if (r1.equals("DAZN") == false) goto L32;
        r2 = 7;
        goto L33
    L15:
        if (r1.equals("Movistar") == false) goto L32;
        r2 = 4;
        goto L33
    L18:
        if (r1.equals("Netflix") == false) goto L32;
        r2 = 6;
        goto L33
    L21:
        if (r1.equals("Disney+") == false) goto L32;
        r2 = 1;
        goto L33
    L24:
        if (r1.equals("Amazon Prime") == false) goto L32;
        r2 = 2;
        goto L33
    L27:
        if (r1.equals("Orange") == false) goto L32;
        r2 = 5;
        goto L33
    L30:
        if (r1.equals("Vodafone") == false) goto L32;
        r2 = 8;
        goto L33
    }
}
