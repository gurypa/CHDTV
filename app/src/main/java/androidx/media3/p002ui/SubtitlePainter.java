package androidx.media3.p002ui;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import androidx.core.view.ViewCompat;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
final class SubtitlePainter {
    private static final float INNER_PADDING_RATIO = 0.125f;
    private static final String TAG = "SubtitlePainter";
    private int backgroundColor;
    private final Paint bitmapPaint;
    private Rect bitmapRect;
    private float bottomPaddingFraction;
    private Bitmap cueBitmap;
    private float cueBitmapHeight;
    private float cueLine;
    private int cueLineAnchor;
    private int cueLineType;
    private float cuePosition;
    private int cuePositionAnchor;
    private float cueSize;
    private CharSequence cueText;
    private Layout.Alignment cueTextAlignment;
    private float cueTextSizePx;
    private float defaultTextSizePx;
    private int edgeColor;
    private StaticLayout edgeLayout;
    private int edgeType;
    private int foregroundColor;
    private final float outlineWidth;
    private int parentBottom;
    private int parentLeft;
    private int parentRight;
    private int parentTop;
    private final float shadowOffset;
    private final float shadowRadius;
    private final float spacingAdd;
    private final float spacingMult;
    private StaticLayout textLayout;
    private int textLeft;
    private int textPaddingX;
    private final TextPaint textPaint;
    private int textTop;
    private int windowColor;
    private final Paint windowPaint;

    public SubtitlePainter(Context r9) {
        TypedArray r1 = r9.obtainStyledAttributes(null, new int[]{R.attr.lineSpacingExtra, R.attr.lineSpacingMultiplier}, 0, 0);
        this.spacingAdd = r1.getDimensionPixelSize(0, 0);
        this.spacingMult = r1.getFloat(1, 1.0f);
        r1.recycle();
        int r5 = Math.round((r9.getResources().getDisplayMetrics().densityDpi * 2.0f) / 160.0f);
        this.outlineWidth = r5;
        this.shadowRadius = r5;
        this.shadowOffset = r5;
        this.textPaint = new TextPaint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setSubpixelText(true);
        this.windowPaint = new Paint();
        this.windowPaint.setAntiAlias(true);
        this.windowPaint.setStyle(Paint.Style.FILL);
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
    }

    private static boolean areCharSequencesEqual(CharSequence r1, CharSequence r2) {
        if (r1 == r2) goto L8;
        if (r1 != null) goto L5;
    L7:
        return false;
    L5:
        if (r1.equals(r2) == false) goto L7;
    L8:
        return true;
    }

    @RequiresNonNull({"cueBitmap", "bitmapRect"})
    private void drawBitmapLayout(Canvas r5) {
        r5.drawBitmap(this.cueBitmap, null, this.bitmapRect, this.bitmapPaint);
    }

    private void drawLayout(Canvas r2, boolean r3) {
        if (r3 == false) goto L4;
        drawTextLayout(r2);
        return;
    L4:
        Assertions.checkNotNull(this.bitmapRect);
        Assertions.checkNotNull(this.cueBitmap);
        drawBitmapLayout(r2);
    }

    private void drawTextLayout(Canvas r13) {
        StaticLayout r0 = this.textLayout;
        StaticLayout r1 = this.edgeLayout;
        if (r0 == null) goto L34;
        if (r1 == null) goto L35;
        int r2 = r13.save();
        r13.translate(this.textLeft, this.textTop);
        if (Color.alpha(this.windowColor) <= 0) goto L9;
        this.windowPaint.setColor(this.windowColor);
        Canvas r4 = r13;
        r4.drawRect(-this.textPaddingX, 0.0f, r0.getWidth() + this.textPaddingX, r0.getHeight(), this.windowPaint);
    L10:
        boolean r5 = true;
        if (this.edgeType != 1) goto L14;
        this.textPaint.setStrokeJoin(Paint.Join.ROUND);
        this.textPaint.setStrokeWidth(this.outlineWidth);
        this.textPaint.setColor(this.edgeColor);
        this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        r1.draw(r4);
    L31:
        this.textPaint.setColor(this.foregroundColor);
        this.textPaint.setStyle(Paint.Style.FILL);
        r0.draw(r4);
        this.textPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        r4.restoreToCount(r2);
        return;
    L14:
        if (this.edgeType != 2) goto L17;
        this.textPaint.setShadowLayer(this.shadowRadius, this.shadowOffset, this.shadowOffset, this.edgeColor);
        goto L31
    L17:
        if (this.edgeType == 3) goto L21;
        if (this.edgeType != 4) goto L31;
    L21:
        if (this.edgeType == 3) goto L24;
        r5 = false;
    L24:
        int r14 = -1;
        if (r5 == false) goto L27;
        int r6 = -1;
    L28:
        if (r5 == false) goto L30;
        r14 = this.edgeColor;
    L30:
        float r7 = this.shadowRadius / 2.0f;
        this.textPaint.setColor(this.foregroundColor);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setShadowLayer(this.shadowRadius, -r7, -r7, r6);
        r1.draw(r4);
        this.textPaint.setShadowLayer(this.shadowRadius, r7, r7, r14);
        goto L31
    L27:
        r6 = this.edgeColor;
        goto L28
    L9:
        r4 = r13;
        goto L10
    L35:
        return;
    }

    @RequiresNonNull({"cueBitmap"})
    private void setupBitmapLayout() {
        Bitmap r0 = this.cueBitmap;
        int r1 = this.parentRight - this.parentLeft;
        int r2 = this.parentBottom - this.parentTop;
        float r3 = this.parentLeft + (r1 * this.cuePosition);
        float r4 = this.parentTop + (r2 * this.cueLine);
        int r5 = Math.round(r1 * this.cueSize);
        if (this.cueBitmapHeight == (-3.4028235E38f)) goto L5;
        int r6 = Math.round(r2 * this.cueBitmapHeight);
    L7:
        if (this.cuePositionAnchor != 2) goto L10;
        float r7 = r3 - r5;
    L13:
        int r8 = Math.round(r7);
        if (this.cueLineAnchor != 2) goto L17;
        float r9 = r4 - r6;
    L20:
        int r10 = Math.round(r9);
        this.bitmapRect = new Rect(r8, r10, r8 + r5, r10 + r6);
        return;
    L17:
        if (this.cueLineAnchor != 1) goto L19;
        r9 = r4 - (r6 / 2);
        goto L20
    L19:
        r9 = r4;
        goto L20
    L10:
        if (this.cuePositionAnchor != 1) goto L12;
        r7 = r3 - (r5 / 2);
        goto L13
    L12:
        r7 = r3;
        goto L13
    L5:
        r6 = Math.round(r5 * (r0.getHeight() / r0.getWidth()));
        goto L7
    }

    @RequiresNonNull({"cueText"})
    private void setupTextLayout() {
        if ((this.cueText instanceof SpannableStringBuilder) == false) goto L5;
        SpannableStringBuilder r1 = (SpannableStringBuilder) this.cueText;
    L6:
        SpannableStringBuilder r3 = r1;
        int r2 = this.parentRight - this.parentLeft;
        int r10 = this.parentBottom - this.parentTop;
        this.textPaint.setTextSize(this.defaultTextSizePx);
        int r11 = (int) ((this.defaultTextSizePx * INNER_PADDING_RATIO) + 0.5f);
        int r4 = r2 - (r11 * 2);
        if (this.cueSize == (-3.4028235E38f)) goto L9;
        int r5 = (int) (r4 * this.cueSize);
    L11:
        if (r5 > 0) goto L15;
        Log.m48w(TAG, "Skipped drawing subtitle cue (insufficient space)");
        return;
    L15:
        if (this.cueTextSizePx <= 0.0f) goto L17;
        r3.setSpan(new AbsoluteSizeSpan((int) this.cueTextSizePx), 0, r3.length(), 16711680);
    L17:
        SpannableStringBuilder r6 = new SpannableStringBuilder(r3);
        if (this.edgeType != 1) goto L23;
        ForegroundColorSpan[] r7 = (ForegroundColorSpan[]) r6.getSpans(0, r6.length(), ForegroundColorSpan.class);
        int r8 = r7.length;
        int r9 = 0;
    L20:
        if (r9 >= r8) goto L23;
        r6.removeSpan(r7[r9]);
        r9 = r9 + 1;
    L23:
        if (Color.alpha(this.backgroundColor) <= 0) goto L32;
        if (this.edgeType != 0) goto L27;
    L30:
        r3.setSpan(new BackgroundColorSpan(this.backgroundColor), 0, r3.length(), 16711680);
        goto L32
    L27:
        if (this.edgeType == 2) goto L30;
        r6.setSpan(new BackgroundColorSpan(this.backgroundColor), 0, r6.length(), 16711680);
    L32:
        if (this.cueTextAlignment != null) goto L34;
        Layout.Alignment r12 = Layout.Alignment.ALIGN_CENTER;
    L35:
        Layout.Alignment r13 = r12;
        int r14 = r5;
        this.textLayout = new StaticLayout(r3, this.textPaint, r5, r13, this.spacingMult, this.spacingAdd, true);
        int r24 = this.textLayout.getHeight();
        int r15 = 0;
        int r16 = this.textLayout.getLineCount();
        int r17 = 0;
    L36:
        if (r17 >= r16) goto L39;
        r15 = Math.max((int) Math.ceil(this.textLayout.getLineWidth(r17)), r15);
        r17 = r17 + 1;
        goto L36
    L39:
        if (this.cueSize == (-3.4028235E38f)) goto L42;
        if (r15 >= r14) goto L42;
        r15 = r14;
    L42:
        int r18 = r15 + (r11 * 2);
        if (this.cuePosition == (-3.4028235E38f)) goto L50;
        int r19 = Math.round(r2 * this.cuePosition) + this.parentLeft;
        switch(this.cuePositionAnchor) {
            case 1: goto L48;
            case 2: goto L47;
            default: goto L46;
        };
    L46:
        int r20 = r19;
    L49:
        int r21 = Math.max(r20, this.parentLeft);
        int r110 = Math.min(r21 + r18, this.parentRight);
    L51:
        int r22 = r110 - r21;
        if (r22 > 0) goto L56;
        Log.m48w(TAG, "Skipped drawing subtitle cue (invalid horizontal positioning)");
        return;
    L56:
        if (this.cueLine != (-3.4028235E38f)) goto L58;
        int r111 = (this.parentBottom - r24) - ((int) (r10 * this.bottomPaddingFraction));
    L79:
        this.textLayout = new StaticLayout(r3, this.textPaint, r22, r13, this.spacingMult, this.spacingAdd, true);
        this.edgeLayout = new StaticLayout(r6, this.textPaint, r22, r13, this.spacingMult, this.spacingAdd, true);
        this.textLeft = r21;
        this.textTop = r111;
        this.textPaddingX = r11;
        return;
    L58:
        if (this.cueLineType != 0) goto L67;
        int r23 = Math.round(r10 * this.cueLine) + this.parentTop;
        if (this.cueLineAnchor != 2) goto L63;
        int r25 = r23 - r24;
    L72:
        if ((r25 + r24) <= this.parentBottom) goto L75;
        r111 = this.parentBottom - r24;
        goto L79
    L75:
        if (r25 >= this.parentTop) goto L77;
        r111 = this.parentTop;
        goto L79
    L77:
        r111 = r25;
        goto L79
    L63:
        if (this.cueLineAnchor != 1) goto L65;
        r25 = ((r23 * 2) - r24) / 2;
        goto L72
    L65:
        r25 = r23;
        goto L72
    L67:
        int r26 = this.textLayout.getLineBottom(0) - this.textLayout.getLineTop(0);
        if (this.cueLine < 0.0f) goto L70;
        r25 = Math.round(this.cueLine * r26) + this.parentTop;
        goto L72
    L70:
        r25 = (Math.round((this.cueLine + 1.0f) * r26) + this.parentBottom) - r24;
        goto L72
    L47:
        r20 = r19 - r18;
        goto L49
    L48:
        r20 = ((r19 * 2) - r18) / 2;
        goto L49
    L50:
        r21 = this.parentLeft + ((r2 - r18) / 2);
        r110 = r21 + r18;
        goto L51
    L34:
        r12 = this.cueTextAlignment;
        goto L35
    L9:
        r5 = r4;
        goto L11
    L5:
        r1 = new SpannableStringBuilder(this.cueText);
        goto L6
    }

    public void draw(Cue r5, CaptionStyleCompat r6, float r7, float r8, float r9, Canvas r10, int r11, int r12, int r13, int r14) {
        if (r5.bitmap != null) goto L5;
        boolean r0 = true;
    L6:
        int r1 = ViewCompat.MEASURED_STATE_MASK;
        if (r0 == false) goto L17;
        if (TextUtils.isEmpty(r5.text) == false) goto L12;
        return;
    L12:
        if (r5.windowColorSet == false) goto L14;
        int r2 = r5.windowColor;
    L15:
        r1 = r2;
        goto L17
    L14:
        r2 = r6.windowColor;
    L17:
        if (areCharSequencesEqual(this.cueText, r5.text) == true) goto L19;
    L64:
        this.cueText = r5.text;
        this.cueTextAlignment = r5.textAlignment;
        this.cueBitmap = r5.bitmap;
        this.cueLine = r5.line;
        this.cueLineType = r5.lineType;
        this.cueLineAnchor = r5.lineAnchor;
        this.cuePosition = r5.position;
        this.cuePositionAnchor = r5.positionAnchor;
        this.cueSize = r5.size;
        this.cueBitmapHeight = r5.bitmapHeight;
        this.foregroundColor = r6.foregroundColor;
        this.backgroundColor = r6.backgroundColor;
        this.windowColor = r1;
        this.edgeType = r6.edgeType;
        this.edgeColor = r6.edgeColor;
        this.textPaint.setTypeface(r6.typeface);
        this.defaultTextSizePx = r7;
        this.cueTextSizePx = r8;
        this.bottomPaddingFraction = r9;
        this.parentLeft = r11;
        this.parentTop = r12;
        this.parentRight = r13;
        this.parentBottom = r14;
        if (r0 == false) goto L67;
        Assertions.checkNotNull(this.cueText);
        setupTextLayout();
    L68:
        drawLayout(r10, r0);
        return;
    L67:
        Assertions.checkNotNull(this.cueBitmap);
        setupBitmapLayout();
        goto L68
    L19:
        if (Util.areEqual(this.cueTextAlignment, r5.textAlignment) == false) goto L64;
        if (this.cueBitmap != r5.bitmap) goto L64;
        if (this.cueLine != r5.line) goto L64;
        if (this.cueLineType != r5.lineType) goto L64;
        if (Util.areEqual(Integer.valueOf(this.cueLineAnchor), Integer.valueOf(r5.lineAnchor)) == false) goto L64;
        if (this.cuePosition != r5.position) goto L64;
        if (Util.areEqual(Integer.valueOf(this.cuePositionAnchor), Integer.valueOf(r5.positionAnchor)) == false) goto L64;
        if (this.cueSize != r5.size) goto L64;
        if (this.cueBitmapHeight != r5.bitmapHeight) goto L64;
        if (this.foregroundColor != r6.foregroundColor) goto L64;
        if (this.backgroundColor != r6.backgroundColor) goto L64;
        if (this.windowColor != r1) goto L64;
        if (this.edgeType != r6.edgeType) goto L64;
        if (this.edgeColor != r6.edgeColor) goto L64;
        if (Util.areEqual(this.textPaint.getTypeface(), r6.typeface) == false) goto L64;
        if (this.defaultTextSizePx != r7) goto L64;
        if (this.cueTextSizePx != r8) goto L64;
        if (this.bottomPaddingFraction != r9) goto L64;
        if (this.parentLeft != r11) goto L64;
        if (this.parentTop != r12) goto L64;
        if (this.parentRight != r13) goto L64;
        if (this.parentBottom != r14) goto L64;
        drawLayout(r10, r0);
        return;
    L5:
        r0 = false;
        goto L6
    }
}
