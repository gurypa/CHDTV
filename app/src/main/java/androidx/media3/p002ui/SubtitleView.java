package androidx.media3.p002ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.accessibility.CaptioningManager;
import android.widget.FrameLayout;
import androidx.media3.common.text.Cue;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SubtitleView extends FrameLayout {
    public static final float DEFAULT_BOTTOM_PADDING_FRACTION = 0.08f;
    public static final float DEFAULT_TEXT_SIZE_FRACTION = 0.0533f;
    public static final int VIEW_TYPE_CANVAS = 1;
    public static final int VIEW_TYPE_WEB = 2;
    private boolean applyEmbeddedFontSizes;
    private boolean applyEmbeddedStyles;
    private float bottomPaddingFraction;
    private List<Cue> cues;
    private float defaultTextSize;
    private int defaultTextSizeType;
    private View innerSubtitleView;
    private Output output;
    private CaptionStyleCompat style;
    private int viewType;

    interface Output {
        void update(List<Cue> r1, CaptionStyleCompat r2, float r3, int r4, float r5);
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
    }

    public SubtitleView(Context r2) {
        this(r2, null);
    }

    public SubtitleView(Context r4, AttributeSet r5) {
        super(r4, r5);
        this.cues = Collections.emptyList();
        this.style = CaptionStyleCompat.DEFAULT;
        this.defaultTextSizeType = 0;
        this.defaultTextSize = 0.0533f;
        this.bottomPaddingFraction = 0.08f;
        this.applyEmbeddedStyles = true;
        this.applyEmbeddedFontSizes = true;
        CanvasSubtitleOutput r1 = new CanvasSubtitleOutput(r4);
        this.output = r1;
        this.innerSubtitleView = r1;
        addView(this.innerSubtitleView);
        this.viewType = 1;
    }

    private List<Cue> getCuesWithStylingPreferencesApplied() {
        if (this.applyEmbeddedStyles == true) goto L5;
    L8:
        ArrayList r0 = new ArrayList(this.cues.size());
        int r1 = 0;
    L10:
        if (r1 >= this.cues.size()) goto L12;
        r0.add(removeEmbeddedStyling(this.cues.get(r1)));
        r1 = r1 + 1;
        goto L10
    L12:
        return r0;
    L5:
        if (this.applyEmbeddedFontSizes == false) goto L8;
        return this.cues;
    }

    private float getUserCaptionFontScale() {
        if (isInEditMode() == false) goto L5;
        return 1.0f;
    L5:
        CaptioningManager r0 = (CaptioningManager) getContext().getSystemService("captioning");
        if (r0 != null) goto L8;
        return 1.0f;
    L8:
        if (r0.isEnabled() == true) goto L11;
        return 1.0f;
    L11:
        return r0.getFontScale();
    }

    private CaptionStyleCompat getUserCaptionStyle() {
        if (isInEditMode() == true) goto L5;
        CaptioningManager r0 = (CaptioningManager) getContext().getSystemService("captioning");
        if (r0 == null) goto L12;
        if (r0.isEnabled() == false) goto L12;
        return CaptionStyleCompat.createFromCaptionStyle(r0.getUserStyle());
    L12:
        return CaptionStyleCompat.DEFAULT;
    L5:
        return CaptionStyleCompat.DEFAULT;
    }

    private Cue removeEmbeddedStyling(Cue r3) {
        Cue.Builder r0 = r3.buildUpon();
        if (this.applyEmbeddedStyles == true) goto L6;
        SubtitleViewUtils.removeAllEmbeddedStyling(r0);
    L9:
        return r0.build();
    L6:
        if (this.applyEmbeddedFontSizes == true) goto L9;
        SubtitleViewUtils.removeEmbeddedFontSizes(r0);
        goto L9
    }

    private void setTextSize(int r1, float r2) {
        this.defaultTextSizeType = r1;
        this.defaultTextSize = r2;
        updateOutput();
    }

    private <T extends View & Output> void setView(T r2) {
        removeView(this.innerSubtitleView);
        if ((this.innerSubtitleView instanceof WebViewSubtitleOutput) == false) goto L5;
        ((WebViewSubtitleOutput) this.innerSubtitleView).destroy();
    L5:
        this.innerSubtitleView = r2;
        this.output = r2;
        addView(r2);
    }

    private void updateOutput() {
        this.output.update(getCuesWithStylingPreferencesApplied(), this.style, this.defaultTextSize, this.defaultTextSizeType, this.bottomPaddingFraction);
    }

    public void setApplyEmbeddedFontSizes(boolean r1) {
        this.applyEmbeddedFontSizes = r1;
        updateOutput();
    }

    public void setApplyEmbeddedStyles(boolean r1) {
        this.applyEmbeddedStyles = r1;
        updateOutput();
    }

    public void setBottomPaddingFraction(float r1) {
        this.bottomPaddingFraction = r1;
        updateOutput();
    }

    public void setCues(List<Cue> r2) {
        if (r2 == null) goto L4;
        List<Cue> r0 = r2;
    L5:
        this.cues = r0;
        updateOutput();
        return;
    L4:
        r0 = Collections.emptyList();
        goto L5
    }

    public void setFixedTextSize(int r5, float r6) {
        Context r0 = getContext();
        if (r0 != null) goto L5;
        Resources r1 = Resources.getSystem();
    L6:
        setTextSize(2, TypedValue.applyDimension(r5, r6, r1.getDisplayMetrics()));
        return;
    L5:
        r1 = r0.getResources();
        goto L6
    }

    public void setFractionalTextSize(float r2) {
        setFractionalTextSize(r2, false);
    }

    public void setFractionalTextSize(float r2, boolean r3) {
        if (r3 == false) goto L4;
        int r0 = 1;
    L5:
        setTextSize(r0, r2);
        return;
    L4:
        r0 = 0;
        goto L5
    }

    public void setStyle(CaptionStyleCompat r1) {
        this.style = r1;
        updateOutput();
    }

    public void setUserDefaultStyle() {
        setStyle(getUserCaptionStyle());
    }

    public void setUserDefaultTextSize() {
        setFractionalTextSize(getUserCaptionFontScale() * 0.0533f);
    }

    public void setViewType(int r3) {
        if (this.viewType != r3) goto L5;
        return;
    L5:
        switch(r3) {
            case 1: goto L9;
            case 2: goto L8;
            default: goto L7;
        };
    L8:
        setView(new WebViewSubtitleOutput(getContext()));
    L10:
        this.viewType = r3;
        return;
    L9:
        setView(new CanvasSubtitleOutput(getContext()));
        goto L10
    L7:
        throw new IllegalArgumentException();
    }
}
