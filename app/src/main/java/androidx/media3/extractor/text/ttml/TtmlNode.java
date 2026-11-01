package androidx.media3.extractor.text.ttml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
final class TtmlNode {
    public static final String ANNOTATION_POSITION_AFTER = "after";
    public static final String ANNOTATION_POSITION_BEFORE = "before";
    public static final String ANNOTATION_POSITION_OUTSIDE = "outside";
    public static final String ANONYMOUS_REGION_ID = "";
    public static final String ATTR_EBUTTS_MULTI_ROW_ALIGN = "multiRowAlign";
    public static final String ATTR_ID = "id";
    public static final String ATTR_TTS_BACKGROUND_COLOR = "backgroundColor";
    public static final String ATTR_TTS_COLOR = "color";
    public static final String ATTR_TTS_DISPLAY_ALIGN = "displayAlign";
    public static final String ATTR_TTS_EXTENT = "extent";
    public static final String ATTR_TTS_FONT_FAMILY = "fontFamily";
    public static final String ATTR_TTS_FONT_SIZE = "fontSize";
    public static final String ATTR_TTS_FONT_STYLE = "fontStyle";
    public static final String ATTR_TTS_FONT_WEIGHT = "fontWeight";
    public static final String ATTR_TTS_ORIGIN = "origin";
    public static final String ATTR_TTS_RUBY = "ruby";
    public static final String ATTR_TTS_RUBY_POSITION = "rubyPosition";
    public static final String ATTR_TTS_SHEAR = "shear";
    public static final String ATTR_TTS_TEXT_ALIGN = "textAlign";
    public static final String ATTR_TTS_TEXT_COMBINE = "textCombine";
    public static final String ATTR_TTS_TEXT_DECORATION = "textDecoration";
    public static final String ATTR_TTS_TEXT_EMPHASIS = "textEmphasis";
    public static final String ATTR_TTS_WRITING_MODE = "writingMode";
    public static final String BOLD = "bold";
    public static final String CENTER = "center";
    public static final String COMBINE_ALL = "all";
    public static final String COMBINE_NONE = "none";
    public static final String END = "end";
    public static final String ITALIC = "italic";
    public static final String LEFT = "left";
    public static final String LINETHROUGH = "linethrough";
    public static final String NO_LINETHROUGH = "nolinethrough";
    public static final String NO_UNDERLINE = "nounderline";
    public static final String RIGHT = "right";
    public static final String RUBY_BASE = "base";
    public static final String RUBY_BASE_CONTAINER = "baseContainer";
    public static final String RUBY_CONTAINER = "container";
    public static final String RUBY_DELIMITER = "delimiter";
    public static final String RUBY_TEXT = "text";
    public static final String RUBY_TEXT_CONTAINER = "textContainer";
    public static final String START = "start";
    public static final String TAG_BODY = "body";
    public static final String TAG_BR = "br";
    public static final String TAG_DATA = "data";
    public static final String TAG_DIV = "div";
    public static final String TAG_HEAD = "head";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_INFORMATION = "information";
    public static final String TAG_LAYOUT = "layout";
    public static final String TAG_METADATA = "metadata";
    public static final String TAG_P = "p";
    public static final String TAG_REGION = "region";
    public static final String TAG_SPAN = "span";
    public static final String TAG_STYLE = "style";
    public static final String TAG_STYLING = "styling";
    public static final String TAG_TT = "tt";
    public static final String TEXT_EMPHASIS_AUTO = "auto";
    public static final String TEXT_EMPHASIS_MARK_CIRCLE = "circle";
    public static final String TEXT_EMPHASIS_MARK_DOT = "dot";
    public static final String TEXT_EMPHASIS_MARK_FILLED = "filled";
    public static final String TEXT_EMPHASIS_MARK_OPEN = "open";
    public static final String TEXT_EMPHASIS_MARK_SESAME = "sesame";
    public static final String TEXT_EMPHASIS_NONE = "none";
    public static final String UNDERLINE = "underline";
    public static final String VERTICAL = "tb";
    public static final String VERTICAL_LR = "tblr";
    public static final String VERTICAL_RL = "tbrl";
    private List<TtmlNode> children;
    public final long endTimeUs;
    public final String imageId;
    public final boolean isTextNode;
    private final HashMap<String, Integer> nodeEndsByRegion;
    private final HashMap<String, Integer> nodeStartsByRegion;
    public final TtmlNode parent;
    public final String regionId;
    public final long startTimeUs;
    public final TtmlStyle style;
    private final String[] styleIds;
    public final String tag;
    public final String text;

    private TtmlNode(String r2, String r3, long r4, long r6, TtmlStyle r8, String[] r9, String r10, String r11, TtmlNode r12) {
        this.tag = r2;
        this.text = r3;
        this.imageId = r11;
        this.style = r8;
        this.styleIds = r9;
        if (r3 == null) goto L5;
        boolean r0 = true;
    L6:
        this.isTextNode = r0;
        this.startTimeUs = r4;
        this.endTimeUs = r6;
        this.regionId = (String) Assertions.checkNotNull(r10);
        this.parent = r12;
        this.nodeStartsByRegion = new HashMap();
        this.nodeEndsByRegion = new HashMap();
        return;
    L5:
        r0 = false;
        goto L6
    }

    private void applyStyleToOutput(Map<String, TtmlStyle> r10, Cue.Builder r11, int r12, int r13, int r14) {
        TtmlStyle r5 = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, r10);
        SpannableStringBuilder r0 = (SpannableStringBuilder) r11.getText();
        if (r0 != null) goto L5;
        SpannableStringBuilder r1 = new SpannableStringBuilder();
        r11.setText(r1);
        SpannableStringBuilder r2 = r1;
    L6:
        if (r5 == null) goto L19;
        TtmlRenderUtil.applyStylesToSpan(r2, r12, r13, r5, this.parent, r10, r14);
        if (TAG_P.equals(this.tag) == true) goto L10;
        return;
    L10:
        if (r5.getShearPercentage() == Float.MAX_VALUE) goto L13;
        r11.setShearDegrees((r5.getShearPercentage() * (-90.0f)) / 100.0f);
    L13:
        if (r5.getTextAlign() == null) goto L16;
        r11.setTextAlignment(r5.getTextAlign());
    L16:
        if (r5.getMultiRowAlign() == null) goto L21;
        r11.setMultiRowAlignment(r5.getMultiRowAlign());
        return;
    L21:
        return;
    L19:
        return;
    L5:
        r2 = r0;
        goto L6
    }

    public static TtmlNode buildNode(String r12, long r13, long r15, TtmlStyle r17, String[] r18, String r19, String r20, TtmlNode r21) {
        return new TtmlNode(r12, null, r13, r15, r17, r18, r19, r20, r21);
    }

    public static TtmlNode buildTextNode(String r12) {
        return new TtmlNode(null, TtmlRenderUtil.applyTextElementSpacePolicy(r12), C0180C.TIME_UNSET, C0180C.TIME_UNSET, null, null, "", null, null);
    }

    private static void cleanUpText(SpannableStringBuilder r8) {
        DeleteTextSpan[] r0 = (DeleteTextSpan[]) r8.getSpans(0, r8.length(), DeleteTextSpan.class);
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        DeleteTextSpan r4 = r0[r3];
        r8.replace(r8.getSpanStart(r4), r8.getSpanEnd(r4), "");
        r3 = r3 + 1;
        goto L3
    L5:
        int r2 = 0;
    L7:
        if (r2 >= r8.length()) goto L21;
        if (r8.charAt(r2) != ' ') goto L19;
        int r5 = r2 + 1;
    L12:
        if (r5 >= r8.length()) goto L16;
        if (r8.charAt(r5) != ' ') goto L16;
        r5 = r5 + 1;
    L16:
        int r6 = r5 - (r2 + 1);
        if (r6 <= 0) goto L19;
        r8.delete(r2, r2 + r6);
    L19:
        r2 = r2 + 1;
        goto L7
    L21:
        if (r8.length() > 0) goto L23;
    L25:
        int r7 = 0;
    L27:
        if (r7 >= (r8.length() - 1)) goto L35;
        if (r8.charAt(r7) != '\n') goto L33;
        if (r8.charAt(r7 + 1) != ' ') goto L33;
        r8.delete(r7 + 1, r7 + 2);
    L33:
        r7 = r7 + 1;
        goto L27
    L35:
        if (r8.length() > 0) goto L37;
    L39:
        int r9 = 0;
    L41:
        if (r9 >= (r8.length() - 1)) goto L49;
        if (r8.charAt(r9) != ' ') goto L47;
        if (r8.charAt(r9 + 1) != '\n') goto L47;
        r8.delete(r9, r9 + 1);
    L47:
        r9 = r9 + 1;
        goto L41
    L49:
        if (r8.length() > 0) goto L51;
        return;
    L51:
        if (r8.charAt(r8.length() - 1) != '\n') goto L70;
        r8.delete(r8.length() - 1, r8.length());
        return;
    L70:
        return;
    L37:
        if (r8.charAt(r8.length() - 1) != ' ') goto L39;
        r8.delete(r8.length() - 1, r8.length());
        goto L39
    L23:
        if (r8.charAt(0) != ' ') goto L25;
        r8.delete(0, 1);
        goto L25
    }

    private void getEventTimes(TreeSet<Long> r7, boolean r8) {
        boolean r0 = TAG_P.equals(this.tag);
        boolean r1 = TAG_DIV.equals(this.tag);
        if (r8 == true) goto L9;
        if (r0 == true) goto L9;
        if (r1 == false) goto L15;
        if (this.imageId != null) goto L9;
    L15:
        if (this.children != null) goto L17;
        return;
    L17:
        int r2 = 0;
    L19:
        if (r2 >= this.children.size()) goto L27;
        TtmlNode r3 = this.children.get(r2);
        if (r8 == true) goto L25;
        if (r0 == true) goto L25;
        boolean r4 = false;
    L26:
        r3.getEventTimes(r7, r4);
        r2 = r2 + 1;
    L25:
        r4 = true;
        goto L26
    L27:
        return;
    L9:
        if (this.startTimeUs == C0180C.TIME_UNSET) goto L12;
        r7.add(Long.valueOf(this.startTimeUs));
    L12:
        if (this.endTimeUs == C0180C.TIME_UNSET) goto L15;
        r7.add(Long.valueOf(this.endTimeUs));
        goto L15
    }

    private static SpannableStringBuilder getRegionOutputText(String r2, Map<String, Cue.Builder> r3) {
        if (r3.containsKey(r2) == true) goto L6;
        Cue.Builder r0 = new Cue.Builder();
        r0.setText(new SpannableStringBuilder());
        r3.put(r2, r0);
    L6:
        return (SpannableStringBuilder) Assertions.checkNotNull(r3.get(r2).getText());
    }

    private void traverseForImage(long r4, String r6, List<Pair<String, String>> r7) {
        if ("".equals(this.regionId) == false) goto L5;
        String r0 = r6;
    L7:
        if (isActive(r4) == true) goto L9;
    L14:
        int r1 = 0;
    L16:
        if (r1 >= getChildCount()) goto L18;
        getChild(r1).traverseForImage(r4, r0, r7);
        r1 = r1 + 1;
        goto L16
    L18:
        return;
    L9:
        if (TAG_DIV.equals(this.tag) == false) goto L14;
        if (this.imageId == null) goto L14;
        r7.add(new Pair(r0, this.imageId));
        return;
    L5:
        r0 = this.regionId;
        goto L7
    }

    private void traverseForStyle(long r13, Map<String, TtmlStyle> r15, Map<String, TtmlRegion> r16, String r17, Map<String, Cue.Builder> r18) {
        if (isActive(r13) == true) goto L6;
        return;
    L6:
        if ("".equals(this.regionId) == false) goto L8;
        String r1 = r17;
    L9:
        String r7 = r1;
        Iterator<Map.Entry<String, Integer>> r6 = this.nodeEndsByRegion.entrySet().iterator();
    L11:
        if (r6.hasNext() == false) goto L21;
        Map.Entry<String, Integer> r8 = r6.next();
        String r9 = r8.getKey();
        if (this.nodeStartsByRegion.containsKey(r9) == false) goto L15;
        int r2 = this.nodeStartsByRegion.get(r9).intValue();
    L16:
        int r3 = r2;
        int r4 = r8.getValue().intValue();
        if (r3 == r4) goto L11;
        applyStyleToOutput(r15, (Cue.Builder) Assertions.checkNotNull(r18.get(r9)), r3, r4, ((TtmlRegion) Assertions.checkNotNull(r16.get(r7))).verticalType);
        goto L11
    L15:
        r2 = 0;
        goto L16
    L21:
        int r5 = 0;
    L23:
        if (r5 >= getChildCount()) goto L25;
        getChild(r5).traverseForStyle(r13, r15, r16, r7, r18);
        r5 = r5 + 1;
        goto L23
    L25:
        return;
    L8:
        r1 = this.regionId;
        goto L9
    }

    private void traverseForText(long r9, boolean r11, String r12, Map<String, Cue.Builder> r13) {
        this.nodeStartsByRegion.clear();
        this.nodeEndsByRegion.clear();
        if (TAG_METADATA.equals(this.tag) == false) goto L6;
        return;
    L6:
        if ("".equals(this.regionId) == false) goto L8;
        String r0 = r12;
    L9:
        String r5 = r0;
        if (this.isTextNode == false) goto L14;
        if (r11 == false) goto L14;
        getRegionOutputText(r5, r13).append((CharSequence) Assertions.checkNotNull(this.text));
        return;
    L14:
        if ("br".equals(this.tag) == false) goto L18;
        if (r11 == false) goto L18;
        getRegionOutputText(r5, r13).append('\n');
        return;
    L18:
        if (isActive(r9) == false) goto L41;
        Iterator<Map.Entry<String, Cue.Builder>> r1 = r13.entrySet().iterator();
    L21:
        if (r1.hasNext() == false) goto L23;
        Map.Entry<String, Cue.Builder> r2 = r1.next();
        this.nodeStartsByRegion.put(r2.getKey(), Integer.valueOf(((CharSequence) Assertions.checkNotNull(r2.getValue().getText())).length()));
        goto L21
    L23:
        boolean r3 = TAG_P.equals(this.tag);
        int r7 = 0;
    L25:
        if (r7 >= getChildCount()) goto L34;
        TtmlNode r4 = getChild(r7);
        if (r11 == true) goto L31;
        if (r3 == true) goto L31;
        boolean r6 = false;
    L32:
        r4.traverseForText(r9, r6, r5, r13);
        r7 = r7 + 1;
    L31:
        r6 = true;
        goto L32
    L34:
        if (r3 == false) goto L36;
        TtmlRenderUtil.endParagraph(getRegionOutputText(r5, r13));
    L36:
        Iterator<Map.Entry<String, Cue.Builder>> r10 = r13.entrySet().iterator();
    L38:
        if (r10.hasNext() == false) goto L49;
        Map.Entry<String, Cue.Builder> r14 = r10.next();
        this.nodeEndsByRegion.put(r14.getKey(), Integer.valueOf(((CharSequence) Assertions.checkNotNull(r14.getValue().getText())).length()));
        goto L38
    L49:
        return;
    L41:
        return;
    L8:
        r0 = this.regionId;
        goto L9
    }

    public void addChild(TtmlNode r2) {
        if (this.children != null) goto L5;
        this.children = new ArrayList();
    L5:
        this.children.add(r2);
    }

    public TtmlNode getChild(int r2) {
        if (this.children == null) goto L7;
        return this.children.get(r2);
    L7:
        throw new IndexOutOfBoundsException();
    }

    public int getChildCount() {
        if (this.children != null) goto L6;
        return 0;
    L6:
        return this.children.size();
    }

    public List<Cue> getCues(long r15, Map<String, TtmlStyle> r17, Map<String, TtmlRegion> r18, Map<String, String> r19) {
        List<Pair<String, String>> r0 = new ArrayList();
        traverseForImage(r15, this.regionId, r0);
        TreeMap r6 = new TreeMap();
        traverseForText(r15, false, this.regionId, r6);
        traverseForStyle(r15, r17, r18, this.regionId, r6);
        ArrayList r1 = new ArrayList();
        Iterator<Pair<String, String>> r2 = r0.iterator();
    L4:
        if (r2.hasNext() == false) goto L9;
        Pair<String, String> r3 = r2.next();
        String r4 = r19.get(r3.second);
        if (r4 == null) goto L4;
        byte[] r9 = Base64.decode(r4, 0);
        Bitmap r10 = BitmapFactory.decodeByteArray(r9, 0, r9.length);
        TtmlRegion r11 = (TtmlRegion) Assertions.checkNotNull(r18.get(r3.first));
        r1.add(new Cue.Builder().setBitmap(r10).setPosition(r11.position).setPositionAnchor(0).setLine(r11.line, 0).setLineAnchor(r11.lineAnchor).setSize(r11.width).setBitmapHeight(r11.height).setVerticalType(r11.verticalType).build());
        goto L4
    L9:
        Iterator r5 = r6.entrySet().iterator();
    L11:
        if (r5.hasNext() == false) goto L13;
        Map.Entry r7 = (Map.Entry) r5.next();
        TtmlRegion r8 = (TtmlRegion) Assertions.checkNotNull(r18.get(r7.getKey()));
        Cue.Builder r12 = (Cue.Builder) r7.getValue();
        cleanUpText((SpannableStringBuilder) Assertions.checkNotNull(r12.getText()));
        r12.setLine(r8.line, r8.lineType);
        r12.setLineAnchor(r8.lineAnchor);
        r12.setPosition(r8.position);
        r12.setSize(r8.width);
        r12.setTextSize(r8.textSize, r8.textSizeType);
        r12.setVerticalType(r8.verticalType);
        r1.add(r12.build());
        goto L11
    L13:
        return r1;
    }

    public long[] getEventTimesUs() {
        TreeSet<Long> r0 = new TreeSet();
        getEventTimes(r0, false);
        long[] r1 = new long[r0.size()];
        int r2 = 0;
        Iterator<Long> r3 = r0.iterator();
    L4:
        if (r3.hasNext() == false) goto L6;
        r1[r2] = r3.next().longValue();
        r2 = r2 + 1;
        goto L4
    L6:
        return r1;
    }

    public String[] getStyleIds() {
        return this.styleIds;
    }

    public boolean isActive(long r5) {
        if (this.startTimeUs != C0180C.TIME_UNSET) goto L7;
        if (this.endTimeUs != C0180C.TIME_UNSET) goto L7;
    L18:
        return true;
    L7:
        if (this.startTimeUs > r5) goto L11;
        if (this.endTimeUs == C0180C.TIME_UNSET) goto L18;
    L11:
        if (this.startTimeUs != C0180C.TIME_UNSET) goto L15;
        if (r5 < this.endTimeUs) goto L18;
    L15:
        if (this.startTimeUs <= r5) goto L17;
    L19:
        return false;
    L17:
        if (r5 >= this.endTimeUs) goto L19;
        goto L18
    }
}
