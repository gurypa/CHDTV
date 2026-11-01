package androidx.media3.extractor.text.webvtt;

import android.text.TextUtils;
import com.google.common.base.Ascii;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class WebvttCssStyle {
    public static final int FONT_SIZE_UNIT_EM = 2;
    public static final int FONT_SIZE_UNIT_PERCENT = 3;
    public static final int FONT_SIZE_UNIT_PIXEL = 1;
    private static final int OFF = 0;

    /* JADX INFO: renamed from: ON */
    private static final int f67ON = 1;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_BOLD_ITALIC = 3;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_NORMAL = 0;
    public static final int UNSPECIFIED = -1;
    private int backgroundColor;
    private int bold;
    private boolean combineUpright;
    private int fontColor;
    private String fontFamily;
    private float fontSize;
    private int fontSizeUnit;
    private boolean hasBackgroundColor;
    private boolean hasFontColor;
    private int italic;
    private int linethrough;
    private int rubyPosition;
    private Set<String> targetClasses;
    private String targetId;
    private String targetTag;
    private String targetVoice;
    private int underline;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FontSizeUnit {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface StyleFlags {
    }

    public WebvttCssStyle() {
        this.targetId = "";
        this.targetTag = "";
        this.targetClasses = Collections.emptySet();
        this.targetVoice = "";
        this.fontFamily = null;
        this.hasFontColor = false;
        this.hasBackgroundColor = false;
        this.linethrough = -1;
        this.underline = -1;
        this.bold = -1;
        this.italic = -1;
        this.fontSizeUnit = -1;
        this.rubyPosition = -1;
        this.combineUpright = false;
    }

    private static int updateScoreForMatch(int r2, String r3, String r4, int r5) {
        if (r3.isEmpty() == false) goto L5;
    L11:
        return r2;
    L5:
        if (r2 == (-1)) goto L11;
        if (r3.equals(r4) == true) goto L10;
        return -1;
    L10:
        return r2 + r5;
    }

    public int getBackgroundColor() {
        if (this.hasBackgroundColor == false) goto L7;
        return this.backgroundColor;
    L7:
        throw new IllegalStateException("Background color not defined.");
    }

    public boolean getCombineUpright() {
        return this.combineUpright;
    }

    public int getFontColor() {
        if (this.hasFontColor == false) goto L7;
        return this.fontColor;
    L7:
        throw new IllegalStateException("Font color not defined");
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public int getFontSizeUnit() {
        return this.fontSizeUnit;
    }

    public int getRubyPosition() {
        return this.rubyPosition;
    }

    public int getSpecificityScore(String r4, String r5, Set<String> r6, String r7) {
        if (this.targetId.isEmpty() == true) goto L5;
    L12:
        int r0 = updateScoreForMatch(updateScoreForMatch(updateScoreForMatch(0, this.targetId, r4, 1073741824), this.targetTag, r5, 2), this.targetVoice, r7, 4);
        if (r0 != (-1)) goto L15;
        return 0;
    L15:
        if (r6.containsAll(this.targetClasses) == true) goto L18;
        return 0;
    L18:
        return r0 + (this.targetClasses.size() * 4);
    L5:
        if (this.targetTag.isEmpty() == false) goto L12;
        if (this.targetClasses.isEmpty() == false) goto L12;
        if (this.targetVoice.isEmpty() == false) goto L12;
        return TextUtils.isEmpty(r5) ? 1 : 0;
    }

    public int getStyle() {
        if (this.bold == (-1)) goto L5;
    L7:
        int r1 = 0;
        if (this.bold != 1) goto L10;
        int r0 = 1;
    L12:
        if (this.italic != 1) goto L15;
        r1 = 2;
    L15:
        return r0 | r1;
    L10:
        r0 = 0;
        goto L12
    L5:
        if (this.italic != (-1)) goto L7;
        return -1;
    }

    public boolean hasBackgroundColor() {
        return this.hasBackgroundColor;
    }

    public boolean hasFontColor() {
        return this.hasFontColor;
    }

    public boolean isLinethrough() {
        if (this.linethrough == 1) goto L7;
        return false;
    L7:
        return true;
    }

    public boolean isUnderline() {
        if (this.underline == 1) goto L7;
        return false;
    L7:
        return true;
    }

    public WebvttCssStyle setBackgroundColor(int r2) {
        this.backgroundColor = r2;
        this.hasBackgroundColor = true;
        return this;
    }

    public WebvttCssStyle setBold(boolean r1) {
        this.bold = r1 ? 1 : 0;
        return this;
    }

    public WebvttCssStyle setCombineUpright(boolean r1) {
        this.combineUpright = r1;
        return this;
    }

    public WebvttCssStyle setFontColor(int r2) {
        this.fontColor = r2;
        this.hasFontColor = true;
        return this;
    }

    public WebvttCssStyle setFontFamily(String r2) {
        if (r2 != null) goto L4;
        String r0 = null;
    L5:
        this.fontFamily = r0;
        return this;
    L4:
        r0 = Ascii.toLowerCase(r2);
        goto L5
    }

    public WebvttCssStyle setFontSize(float r1) {
        this.fontSize = r1;
        return this;
    }

    public WebvttCssStyle setFontSizeUnit(int r1) {
        this.fontSizeUnit = r1;
        return this;
    }

    public WebvttCssStyle setItalic(boolean r1) {
        this.italic = r1 ? 1 : 0;
        return this;
    }

    public WebvttCssStyle setLinethrough(boolean r1) {
        this.linethrough = r1 ? 1 : 0;
        return this;
    }

    public WebvttCssStyle setRubyPosition(int r1) {
        this.rubyPosition = r1;
        return this;
    }

    public void setTargetClasses(String[] r3) {
        this.targetClasses = new HashSet(Arrays.asList(r3));
    }

    public void setTargetId(String r1) {
        this.targetId = r1;
    }

    public void setTargetTagName(String r1) {
        this.targetTag = r1;
    }

    public void setTargetVoice(String r1) {
        this.targetVoice = r1;
    }

    public WebvttCssStyle setUnderline(boolean r1) {
        this.underline = r1 ? 1 : 0;
        return this;
    }
}
