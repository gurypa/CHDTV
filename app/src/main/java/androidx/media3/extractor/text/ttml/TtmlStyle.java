package androidx.media3.extractor.text.ttml;

import android.text.Layout;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
final class TtmlStyle {
    public static final int FONT_SIZE_UNIT_EM = 2;
    public static final int FONT_SIZE_UNIT_PERCENT = 3;
    public static final int FONT_SIZE_UNIT_PIXEL = 1;
    private static final int OFF = 0;

    /* JADX INFO: renamed from: ON */
    private static final int f65ON = 1;
    public static final int RUBY_TYPE_BASE = 2;
    public static final int RUBY_TYPE_CONTAINER = 1;
    public static final int RUBY_TYPE_DELIMITER = 4;
    public static final int RUBY_TYPE_TEXT = 3;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_BOLD_ITALIC = 3;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_NORMAL = 0;
    public static final int UNSPECIFIED = -1;
    public static final float UNSPECIFIED_SHEAR = Float.MAX_VALUE;
    private int backgroundColor;
    private int bold;
    private int fontColor;
    private String fontFamily;
    private float fontSize;
    private int fontSizeUnit;
    private boolean hasBackgroundColor;
    private boolean hasFontColor;

    /* JADX INFO: renamed from: id */
    private String f66id;
    private int italic;
    private int linethrough;
    private Layout.Alignment multiRowAlign;
    private int rubyPosition;
    private int rubyType;
    private float shearPercentage;
    private Layout.Alignment textAlign;
    private int textCombine;
    private TextEmphasis textEmphasis;
    private int underline;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FontSizeUnit {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface RubyType {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface StyleFlags {
    }

    public TtmlStyle() {
        this.linethrough = -1;
        this.underline = -1;
        this.bold = -1;
        this.italic = -1;
        this.fontSizeUnit = -1;
        this.rubyType = -1;
        this.rubyPosition = -1;
        this.textCombine = -1;
        this.shearPercentage = Float.MAX_VALUE;
    }

    private TtmlStyle inherit(TtmlStyle r4, boolean r5) {
        if (r4 != null) goto L4;
    L62:
        return this;
    L4:
        if (this.hasFontColor == true) goto L9;
        if (r4.hasFontColor == false) goto L9;
        setFontColor(r4.fontColor);
    L9:
        if (this.bold != (-1)) goto L12;
        this.bold = r4.bold;
    L12:
        if (this.italic != (-1)) goto L15;
        this.italic = r4.italic;
    L15:
        if (this.fontFamily != null) goto L20;
        if (r4.fontFamily == null) goto L20;
        this.fontFamily = r4.fontFamily;
    L20:
        if (this.linethrough != (-1)) goto L23;
        this.linethrough = r4.linethrough;
    L23:
        if (this.underline != (-1)) goto L26;
        this.underline = r4.underline;
    L26:
        if (this.rubyPosition != (-1)) goto L29;
        this.rubyPosition = r4.rubyPosition;
    L29:
        if (this.textAlign != null) goto L34;
        if (r4.textAlign == null) goto L34;
        this.textAlign = r4.textAlign;
    L34:
        if (this.multiRowAlign != null) goto L39;
        if (r4.multiRowAlign == null) goto L39;
        this.multiRowAlign = r4.multiRowAlign;
    L39:
        if (this.textCombine != (-1)) goto L42;
        this.textCombine = r4.textCombine;
    L42:
        if (this.fontSizeUnit != (-1)) goto L45;
        this.fontSizeUnit = r4.fontSizeUnit;
        this.fontSize = r4.fontSize;
    L45:
        if (this.textEmphasis != null) goto L48;
        this.textEmphasis = r4.textEmphasis;
    L48:
        if (this.shearPercentage != Float.MAX_VALUE) goto L50;
        this.shearPercentage = r4.shearPercentage;
    L50:
        if (r5 == true) goto L52;
    L56:
        if (r5 == false) goto L62;
        if (this.rubyType != (-1)) goto L62;
        if (r4.rubyType == (-1)) goto L62;
        this.rubyType = r4.rubyType;
        goto L62
    L52:
        if (this.hasBackgroundColor == true) goto L56;
        if (r4.hasBackgroundColor == false) goto L56;
        setBackgroundColor(r4.backgroundColor);
        goto L56
    }

    public TtmlStyle chain(TtmlStyle r2) {
        return inherit(r2, true);
    }

    public int getBackgroundColor() {
        if (this.hasBackgroundColor == false) goto L7;
        return this.backgroundColor;
    L7:
        throw new IllegalStateException("Background color has not been defined.");
    }

    public int getFontColor() {
        if (this.hasFontColor == false) goto L7;
        return this.fontColor;
    L7:
        throw new IllegalStateException("Font color has not been defined.");
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

    public String getId() {
        return this.f66id;
    }

    public Layout.Alignment getMultiRowAlign() {
        return this.multiRowAlign;
    }

    public int getRubyPosition() {
        return this.rubyPosition;
    }

    public int getRubyType() {
        return this.rubyType;
    }

    public float getShearPercentage() {
        return this.shearPercentage;
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

    public Layout.Alignment getTextAlign() {
        return this.textAlign;
    }

    public boolean getTextCombine() {
        if (this.textCombine == 1) goto L7;
        return false;
    L7:
        return true;
    }

    public TextEmphasis getTextEmphasis() {
        return this.textEmphasis;
    }

    public boolean hasBackgroundColor() {
        return this.hasBackgroundColor;
    }

    public boolean hasFontColor() {
        return this.hasFontColor;
    }

    public TtmlStyle inherit(TtmlStyle r2) {
        return inherit(r2, false);
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

    public TtmlStyle setBackgroundColor(int r2) {
        this.backgroundColor = r2;
        this.hasBackgroundColor = true;
        return this;
    }

    public TtmlStyle setBold(boolean r1) {
        this.bold = r1 ? 1 : 0;
        return this;
    }

    public TtmlStyle setFontColor(int r2) {
        this.fontColor = r2;
        this.hasFontColor = true;
        return this;
    }

    public TtmlStyle setFontFamily(String r1) {
        this.fontFamily = r1;
        return this;
    }

    public TtmlStyle setFontSize(float r1) {
        this.fontSize = r1;
        return this;
    }

    public TtmlStyle setFontSizeUnit(int r1) {
        this.fontSizeUnit = r1;
        return this;
    }

    public TtmlStyle setId(String r1) {
        this.f66id = r1;
        return this;
    }

    public TtmlStyle setItalic(boolean r1) {
        this.italic = r1 ? 1 : 0;
        return this;
    }

    public TtmlStyle setLinethrough(boolean r1) {
        this.linethrough = r1 ? 1 : 0;
        return this;
    }

    public TtmlStyle setMultiRowAlign(Layout.Alignment r1) {
        this.multiRowAlign = r1;
        return this;
    }

    public TtmlStyle setRubyPosition(int r1) {
        this.rubyPosition = r1;
        return this;
    }

    public TtmlStyle setRubyType(int r1) {
        this.rubyType = r1;
        return this;
    }

    public TtmlStyle setShearPercentage(float r1) {
        this.shearPercentage = r1;
        return this;
    }

    public TtmlStyle setTextAlign(Layout.Alignment r1) {
        this.textAlign = r1;
        return this;
    }

    public TtmlStyle setTextCombine(boolean r1) {
        this.textCombine = r1 ? 1 : 0;
        return this;
    }

    public TtmlStyle setTextEmphasis(TextEmphasis r1) {
        this.textEmphasis = r1;
        return this;
    }

    public TtmlStyle setUnderline(boolean r1) {
        this.underline = r1 ? 1 : 0;
        return this;
    }
}
