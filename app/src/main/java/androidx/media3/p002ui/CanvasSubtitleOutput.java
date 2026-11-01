package androidx.media3.p002ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.media3.common.text.Cue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class CanvasSubtitleOutput extends View implements SubtitleView.Output {
    private float bottomPaddingFraction;
    private List<Cue> cues;
    private final List<SubtitlePainter> painters;
    private CaptionStyleCompat style;
    private float textSize;
    private int textSizeType;

    public CanvasSubtitleOutput(Context r2) {
        this(r2, null);
    }

    public CanvasSubtitleOutput(Context r2, AttributeSet r3) {
        super(r2, r3);
        this.painters = new ArrayList();
        this.cues = Collections.emptyList();
        this.textSizeType = 0;
        this.textSize = 0.0533f;
        this.style = CaptionStyleCompat.DEFAULT;
        this.bottomPaddingFraction = 0.08f;
    }

    private static Cue repositionVerticalCue(Cue r4) {
        Cue.Builder r0 = r4.buildUpon().setPosition(-3.4028235E38f).setPositionAnchor(Integer.MIN_VALUE).setTextAlignment(null);
        if (r4.lineType != 0) goto L5;
        r0.setLine(1.0f - r4.line, 0);
    L7:
        switch(r4.lineAnchor) {
            case 0: goto L10;
            case 1: goto L12;
            case 2: goto L9;
            default: goto L12;
        };
    L9:
        r0.setLineAnchor(0);
        goto L12
    L10:
        r0.setLineAnchor(2);
    L12:
        return r0.build();
    L5:
        r0.setLine((-r4.line) - 1.0f, 1);
        goto L7
    }

    @Override // android.view.View
    public void dispatchDraw(Canvas r18) {
        List<Cue> r1 = this.cues;
        if (r1.isEmpty() == false) goto L5;
        return;
    L5:
        int r2 = getHeight();
        int r10 = getPaddingLeft();
        int r11 = getPaddingTop();
        int r12 = getWidth() - getPaddingRight();
        int r13 = r2 - getPaddingBottom();
        if (r13 <= r11) goto L19;
        if (r12 <= r10) goto L23;
        int r14 = r13 - r11;
        float r6 = SubtitleViewUtils.resolveTextSize(this.textSizeType, this.textSize, r2, r14);
        if (r6 > 0.0f) goto L12;
        return;
    L12:
        int r15 = r1.size();
        int r3 = 0;
    L13:
        if (r3 >= r15) goto L18;
        Cue r4 = r1.get(r3);
        if (r4.verticalType == Integer.MIN_VALUE) goto L17;
        r4 = repositionVerticalCue(r4);
    L17:
        float r7 = SubtitleViewUtils.resolveTextSize(r4.textSizeType, r4.textSize, r2, r14);
        this.painters.get(r3).draw(r4, this.style, r6, r7, this.bottomPaddingFraction, r18, r10, r11, r12, r13);
        r3 = r3 + 1;
        goto L13
    L18:
        return;
    L23:
        return;
    }

    @Override // androidx.media3.ui.SubtitleView.Output
    public void update(List<Cue> r4, CaptionStyleCompat r5, float r6, int r7, float r8) {
        this.cues = r4;
        this.style = r5;
        this.textSize = r6;
        this.textSizeType = r7;
        this.bottomPaddingFraction = r8;
    L4:
        if (this.painters.size() >= r4.size()) goto L6;
        this.painters.add(new SubtitlePainter(getContext()));
        goto L4
    L6:
        invalidate();
    }
}
