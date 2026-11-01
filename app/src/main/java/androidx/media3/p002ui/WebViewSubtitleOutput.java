package androidx.media3.p002ui;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.FrameLayout;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.StreamInformation;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class WebViewSubtitleOutput extends FrameLayout implements SubtitleView.Output {
    private static final float CSS_LINE_HEIGHT = 1.2f;
    private static final String DEFAULT_BACKGROUND_CSS_CLASS = "default_bg";
    private float bottomPaddingFraction;
    private final CanvasSubtitleOutput canvasSubtitleOutput;
    private float defaultTextSize;
    private int defaultTextSizeType;
    private CaptionStyleCompat style;
    private List<Cue> textCues;
    private final WebView webView;

    /* JADX INFO: renamed from: androidx.media3.ui.WebViewSubtitleOutput$2 */
    static /* synthetic */ class C04712 {
        static final /* synthetic */ int[] $SwitchMap$android$text$Layout$Alignment = null;

        static {
            $SwitchMap$android$text$Layout$Alignment = new int[Layout.Alignment.values().length];
            $SwitchMap$android$text$Layout$Alignment[Layout.Alignment.ALIGN_NORMAL.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L17:
            $SwitchMap$android$text$Layout$Alignment[Layout.Alignment.ALIGN_OPPOSITE.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L13:
            $SwitchMap$android$text$Layout$Alignment[Layout.Alignment.ALIGN_CENTER.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
            return;
        }
    }

    public WebViewSubtitleOutput(Context r2) {
        this(r2, null);
    }

    public WebViewSubtitleOutput(final Context r3, final AttributeSet r4) {
        super(r3, r4);
        this.textCues = Collections.emptyList();
        this.style = CaptionStyleCompat.DEFAULT;
        this.defaultTextSize = 0.0533f;
        this.defaultTextSizeType = 0;
        this.bottomPaddingFraction = 0.08f;
        this.canvasSubtitleOutput = new CanvasSubtitleOutput(r3, r4);
        this.webView = new C04701(this, r3, r4);
        this.webView.setBackgroundColor(0);
        addView(this.canvasSubtitleOutput);
        addView(this.webView);
    }

    private static int anchorTypeToTranslatePercent(int r1) {
        switch(r1) {
            case 1: goto L7;
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return 0;
    L5:
        return -100;
    L7:
        return -50;
    }

    private static String convertAlignmentToCss(Layout.Alignment r3) {
        if (r3 != null) goto L6;
        return TtmlNode.CENTER;
    L6:
        switch(C04712.$SwitchMap$android$text$Layout$Alignment[r3.ordinal()]) {
            case 1: goto L10;
            case 2: goto L8;
            default: goto L7;
        };
    L7:
        return TtmlNode.CENTER;
    L8:
        return "end";
    L10:
        return "start";
    }

    private static String convertCaptionStyleToCssTextShadow(CaptionStyleCompat r3) {
        switch(r3.edgeType) {
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L9;
            case 4: goto L7;
            default: goto L4;
        };
    L4:
        return "unset";
    L7:
        return Util.formatInvariant("-0.05em -0.05em 0.15em %s", new Object[]{HtmlUtils.toCssRgba(r3.edgeColor)});
    L9:
        return Util.formatInvariant("0.06em 0.08em 0.15em %s", new Object[]{HtmlUtils.toCssRgba(r3.edgeColor)});
    L11:
        return Util.formatInvariant("0.1em 0.12em 0.15em %s", new Object[]{HtmlUtils.toCssRgba(r3.edgeColor)});
    L13:
        return Util.formatInvariant("1px 1px 0 %1$s, 1px -1px 0 %1$s, -1px 1px 0 %1$s, -1px -1px 0 %1$s", new Object[]{HtmlUtils.toCssRgba(r3.edgeColor)});
    }

    private String convertTextSizeToCss(int r6, float r7) {
        float r0 = SubtitleViewUtils.resolveTextSize(r6, r7, getHeight(), (getHeight() - getPaddingTop()) - getPaddingBottom());
        if (r0 != (-3.4028235E38f)) goto L7;
        return "unset";
    L7:
        return Util.formatInvariant("%.2fpx", new Object[]{Float.valueOf(r0 / getContext().getResources().getDisplayMetrics().density)});
    }

    private static String convertVerticalTypeToCss(int r1) {
        switch(r1) {
            case 1: goto L7;
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return "horizontal-tb";
    L5:
        return "vertical-lr";
    L7:
        return "vertical-rl";
    }

    private static String getBlockShearTransformFunction(Cue r5) {
        if (r5.shearDegrees != 0.0f) goto L5;
        return "";
    L5:
        if (r5.verticalType != 2) goto L7;
    L10:
        String r0 = "skewY";
    L12:
        return Util.formatInvariant("%s(%.2fdeg)", new Object[]{r0, Float.valueOf(r5.shearDegrees)});
    L7:
        if (r5.verticalType == 1) goto L10;
        r0 = "skewX";
        goto L12
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateWebView() {
        StringBuilder r1 = new StringBuilder();
        String r2 = HtmlUtils.toCssRgba(this.style.foregroundColor);
        String r3 = convertTextSizeToCss(this.defaultTextSizeType, this.defaultTextSize);
        float r4 = CSS_LINE_HEIGHT;
        char r7 = 4;
        int r9 = 0;
        int r5 = 1;
        int r6 = 2;
        char r8 = 3;
        r1.append(Util.formatInvariant("<body><div style='-webkit-user-select:none;position:fixed;top:0;bottom:0;left:0;right:0;color:%s;font-size:%s;line-height:%.2f;text-shadow:%s;'>", new Object[]{r2, r3, Float.valueOf(CSS_LINE_HEIGHT), convertCaptionStyleToCssTextShadow(this.style)}));
        HashMap r10 = new HashMap();
        r10.put(HtmlUtils.cssAllClassDescendantsSelector(DEFAULT_BACKGROUND_CSS_CLASS), Util.formatInvariant("background-color:%s;", new Object[]{HtmlUtils.toCssRgba(this.style.backgroundColor)}));
        int r11 = 0;
    L4:
        if (r11 >= this.textCues.size()) goto L69;
        Cue r12 = this.textCues.get(r11);
        if (r12.position == (-3.4028235E38f)) goto L8;
        float r13 = r12.position * 100.0f;
    L9:
        int r15 = anchorTypeToTranslatePercent(r12.positionAnchor);
        boolean r16 = false;
        int r17 = 0;
        float r18 = r4;
        char r20 = r8;
        if (r12.line != (-3.4028235E38f)) goto L12;
        char r21 = r7;
        Object[] r14 = new Object[r5];
        r14[r9] = Float.valueOf((1.0f - this.bottomPaddingFraction) * 100.0f);
        String r19 = Util.formatInvariant("%.2f%%", r14);
        r17 = -100;
    L24:
        if (r12.size == (-8388609(0xffffffffff7fffff, float:-3.4028235E38))) goto L26;
        Object[] r110 = new Object[r5];
        r110[r9] = Float.valueOf(r12.size * 100.0f);
        String r22 = Util.formatInvariant("%.2f%%", r110);
    L27:
        String r23 = convertAlignmentToCss(r12.textAlignment);
        String r111 = convertVerticalTypeToCss(r12.verticalType);
        int r112 = r9;
        String r24 = convertTextSizeToCss(r12.textSizeType, r12.textSize);
        if (r12.windowColorSet == false) goto L30;
        int r113 = r12.windowColor;
    L31:
        String r114 = HtmlUtils.toCssRgba(r113);
        int r25 = r12.verticalType;
        String r26 = TtmlNode.LEFT;
        switch(r25) {
            case 1: goto L38;
            case 2: goto L35;
            default: goto L33;
        };
    L33:
        if (r16 == false) goto L42;
        String r27 = "bottom";
    L43:
        r26 = r27;
        String r28 = TtmlNode.LEFT;
    L44:
        String r29 = r28;
        if (r12.verticalType == r6) goto L50;
        int r210 = r6;
        if (r12.verticalType == 1) goto L51;
        String r30 = StreamInformation.KEY_WIDTH;
        int r31 = r15;
        int r211 = r17;
    L52:
        String r212 = r30;
        int r213 = r31;
        SpannedToHtmlConverter.HtmlAndCss r32 = SpannedToHtmlConverter.convert(r12.text, getContext().getResources().getDisplayMetrics().density);
        Iterator r33 = r10.keySet().iterator();
    L54:
        if (r33.hasNext() == false) goto L64;
        Iterator r34 = r33;
        String r35 = (String) r33.next();
        String r36 = r19;
        String r37 = (String) r10.put(r35, (String) r10.get(r35));
        if (r37 == null) goto L61;
        String r214 = r22;
        if (r37.equals(r10.get(r35)) == true) goto L62;
        boolean r38 = r112;
    L63:
        Assertions.checkState(r38);
        r22 = r214;
        r33 = r34;
        r19 = r36;
    L62:
        r38 = 1;
        goto L63
    L61:
        r214 = r22;
        goto L62
    L64:
        String r39 = r19;
        String r215 = r22;
        Integer r40 = Integer.valueOf(r11);
        Float r41 = Float.valueOf(r13);
        Integer r42 = Integer.valueOf(r213);
        Integer r310 = Integer.valueOf(r211);
        String r311 = getBlockShearTransformFunction(r12);
        Object[] r43 = new Object[14];
        r43[r112] = r40;
        r43[1] = r29;
        r43[r210] = r41;
        r43[r20] = r26;
        r43[r21] = r39;
        r43[5] = r212;
        r43[6] = r215;
        r43[7] = r23;
        r43[8] = r111;
        r43[9] = r24;
        r43[10] = r114;
        r43[11] = r42;
        r43[12] = r310;
        r43[13] = r311;
        StringBuilder r44 = r1.append(Util.formatInvariant("<div style='position:absolute;z-index:%s;%s:%.2f%%;%s:%s;%s:%s;text-align:%s;writing-mode:%s;font-size:%s;background-color:%s;transform:translate(%s%%,%s%%)%s;'>", r43));
        Object[] r45 = new Object[1];
        r45[r112] = DEFAULT_BACKGROUND_CSS_CLASS;
        r44.append(Util.formatInvariant("<span class='%s'>", r45));
        if (r12.multiRowAlignment == null) goto L67;
        Object[] r46 = new Object[1];
        r46[r112] = convertAlignmentToCss(r12.multiRowAlignment);
        r1.append(Util.formatInvariant("<span style='display:inline-block; text-align:%s;'>", r46)).append(r32.html).append("</span>");
    L68:
        r1.append("</span>").append("</div>");
        r11 = r11 + 1;
        r4 = r18;
        r9 = r112;
        r8 = r20;
        r7 = r21;
        r6 = r210;
        r5 = 1;
        goto L4
    L67:
        r1.append(r32.html);
    L51:
        r30 = StreamInformation.KEY_HEIGHT;
        r31 = r17;
        r211 = r15;
        goto L52
    L50:
        r210 = r6;
        goto L51
    L42:
        r27 = "top";
        goto L43
    L35:
        if (r16 == false) goto L37;
        r26 = TtmlNode.RIGHT;
    L37:
        r28 = "top";
        goto L44
    L38:
        if (r16 == true) goto L41;
        r26 = TtmlNode.RIGHT;
    L41:
        r28 = "top";
        goto L44
    L30:
        r113 = this.style.windowColor;
        goto L31
    L26:
        r22 = "fit-content";
        goto L27
    L12:
        switch(r12.lineType) {
            case 1: goto L16;
            default: goto L13;
        };
    L13:
        r21 = r7;
        Object[] r47 = new Object[r5];
        r47[r9] = Float.valueOf(r12.line * 100.0f);
        r19 = Util.formatInvariant("%.2f%%", r47);
        if (r12.verticalType != r5) goto L20;
        int r48 = -anchorTypeToTranslatePercent(r12.lineAnchor);
    L21:
        r17 = r48;
        goto L24
    L20:
        r48 = anchorTypeToTranslatePercent(r12.lineAnchor);
        goto L21
    L16:
        r21 = r7;
        if (r12.line < 0.0f) goto L19;
        Object[] r115 = new Object[r5];
        r115[r9] = Float.valueOf(r12.line * r18);
        r19 = Util.formatInvariant("%.2fem", r115);
        goto L24
    L19:
        Object[] r116 = new Object[r5];
        r116[r9] = Float.valueOf(((-r12.line) - 1.0f) * r18);
        r19 = Util.formatInvariant("%.2fem", r116);
        r16 = true;
        goto L24
    L8:
        r13 = 50.0f;
        goto L9
    L69:
        int r117 = r9;
        r1.append("</div></body></html>");
        StringBuilder r49 = new StringBuilder();
        r49.append("<html><head><style>");
        Iterator r50 = r10.keySet().iterator();
    L71:
        if (r50.hasNext() == false) goto L73;
        String r51 = (String) r50.next();
        r49.append(r51).append("{").append((String) r10.get(r51)).append("}");
        goto L71
    L73:
        r49.append("</style></head>");
        r1.insert(r117, r49.toString());
        this.webView.loadData(Base64.encodeToString(r1.toString().getBytes(StandardCharsets.UTF_8), 1), "text/html", "base64");
    }

    public void destroy() {
        this.webView.destroy();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean r3, int r4, int r5, int r6, int r7) {
        super.onLayout(r3, r4, r5, r6, r7);
        if (r3 == true) goto L5;
        return;
    L5:
        if (this.textCues.isEmpty() == true) goto L9;
        updateWebView();
        return;
    }

    @Override // androidx.media3.ui.SubtitleView.Output
    public void update(List<Cue> r8, CaptionStyleCompat r9, float r10, int r11, float r12) {
        this.style = r9;
        this.defaultTextSize = r10;
        this.defaultTextSizeType = r11;
        this.bottomPaddingFraction = r12;
        ArrayList r0 = new ArrayList();
        ArrayList r1 = new ArrayList();
        int r2 = 0;
    L4:
        if (r2 >= r8.size()) goto L11;
        Cue r3 = r8.get(r2);
        if (r3.bitmap == null) goto L8;
        r0.add(r3);
    L9:
        r2 = r2 + 1;
        goto L4
    L8:
        r1.add(r3);
        goto L9
    L11:
        if (this.textCues.isEmpty() == true) goto L13;
    L14:
        this.textCues = r1;
        updateWebView();
    L15:
        this.canvasSubtitleOutput.update(r0, r9, r10, r11, r12);
        invalidate();
        return;
    L13:
        if (r1.isEmpty() == true) goto L15;
        goto L14
    }
}
