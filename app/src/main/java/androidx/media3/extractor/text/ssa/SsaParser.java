package androidx.media3.extractor.text.ssa;

import android.text.Layout;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Ascii;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class SsaParser implements SubtitleParser {
    public static final int CUE_REPLACEMENT_BEHAVIOR = 1;
    private static final float DEFAULT_MARGIN = 0.05f;
    private static final String DIALOGUE_LINE_PREFIX = "Dialogue:";
    static final String FORMAT_LINE_PREFIX = "Format:";
    private static final Pattern SSA_TIMECODE_PATTERN = null;
    static final String STYLE_LINE_PREFIX = "Style:";
    private static final String TAG = "SsaParser";
    private final SsaDialogueFormat dialogueFormatFromInitializationData;
    private final boolean haveInitializationData;
    private final ParsableByteArray parsableByteArray;
    private float screenHeight;
    private float screenWidth;
    private Map<String, SsaStyle> styles;

    static {
        SSA_TIMECODE_PATTERN = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)[:.](\\d+)");
    }

    public SsaParser() {
        this(null);
    }

    public SsaParser(List<byte[]> r4) {
        this.screenWidth = -3.4028235E38f;
        this.screenHeight = -3.4028235E38f;
        this.parsableByteArray = new ParsableByteArray();
        if (r4 != null) goto L5;
    L7:
        this.haveInitializationData = false;
        this.dialogueFormatFromInitializationData = null;
        return;
    L5:
        if (r4.isEmpty() == true) goto L7;
        this.haveInitializationData = true;
        String r0 = Util.fromUtf8Bytes(r4.get(0));
        Assertions.checkArgument(r0.startsWith(FORMAT_LINE_PREFIX));
        this.dialogueFormatFromInitializationData = (SsaDialogueFormat) Assertions.checkNotNull(SsaDialogueFormat.fromFormatLine(r0));
        parseHeader(new ParsableByteArray(r4.get(1)), StandardCharsets.UTF_8);
    }

    private static int addCuePlacerholderByTime(long r4, List<Long> r6, List<List<Cue>> r7) {
        int r0 = 0;
        int r1 = r6.size() - 1;
    L3:
        if (r1 < 0) goto L11;
        if (r6.get(r1).longValue() == r4) goto L6;
        if (r6.get(r1).longValue() < r4) goto L9;
        r1 = r1 - 1;
        goto L3
    L9:
        r0 = r1 + 1;
        goto L11
    L6:
        return r1;
    L11:
        r6.add(r0, Long.valueOf(r4));
        if (r0 != 0) goto L14;
        ArrayList r2 = new ArrayList();
    L15:
        r7.add(r0, r2);
        return r0;
    L14:
        r2 = new ArrayList(r7.get(r0 - 1));
        goto L15
    }

    private static float computeDefaultLineOrPosition(int r1) {
        switch(r1) {
            case 0: goto L9;
            case 1: goto L7;
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return -3.4028235E38f;
    L5:
        return 0.95f;
    L7:
        return 0.5f;
    L9:
        return DEFAULT_MARGIN;
    }

    private static Cue createCue(String r8, SsaStyle r9, SsaStyle.Overrides r10, float r11, float r12) {
        SpannableString r0 = new SpannableString(r8);
        Cue.Builder r1 = new Cue.Builder().setText(r0);
        if (r9 == null) goto L35;
        if (r9.primaryColor == null) goto L8;
        r0.setSpan(new ForegroundColorSpan(r9.primaryColor.intValue()), 0, r0.length(), 33);
    L8:
        if (r9.borderStyle != 3) goto L13;
        if (r9.outlineColor == null) goto L13;
        r0.setSpan(new BackgroundColorSpan(r9.outlineColor.intValue()), 0, r0.length(), 33);
    L13:
        if (r9.fontSize == (-3.4028235E38f)) goto L18;
        if (r12 == (-3.4028235E38f)) goto L18;
        r1.setTextSize(r9.fontSize / r12, 1);
    L18:
        if (r9.bold == false) goto L23;
        if (r9.italic == false) goto L23;
        r0.setSpan(new StyleSpan(3), 0, r0.length(), 33);
    L29:
        if (r9.underline == false) goto L32;
        r0.setSpan(new UnderlineSpan(), 0, r0.length(), 33);
    L32:
        if (r9.strikeout == false) goto L35;
        r0.setSpan(new StrikethroughSpan(), 0, r0.length(), 33);
    L23:
        if (r9.bold == false) goto L26;
        r0.setSpan(new StyleSpan(1), 0, r0.length(), 33);
        goto L29
    L26:
        if (r9.italic == false) goto L29;
        r0.setSpan(new StyleSpan(2), 0, r0.length(), 33);
    L35:
        if (r10.alignment == (-1)) goto L37;
        int r4 = r10.alignment;
    L40:
        r1.setTextAlignment(toTextAlignment(r4)).setPositionAnchor(toPositionAnchor(r4)).setLineAnchor(toLineAnchor(r4));
        if (r10.position != null) goto L43;
    L47:
        r1.setPosition(computeDefaultLineOrPosition(r1.getPositionAnchor()));
        r1.setLine(computeDefaultLineOrPosition(r1.getLineAnchor()), 0);
    L49:
        return r1.build();
    L43:
        if (r12 == (-3.4028235E38f)) goto L47;
        if (r11 == (-3.4028235E38f)) goto L47;
        r1.setPosition(r10.position.x / r11);
        r1.setLine(r10.position.y / r12, 0);
        goto L49
    L37:
        if (r9 == null) goto L39;
        r4 = r9.alignment;
        goto L40
    L39:
        r4 = -1;
        goto L40
    }

    private Charset detectUtfCharset(ParsableByteArray r3) {
        Charset r0 = r3.readUtfCharsetFromBom();
        if (r0 == null) goto L6;
        return r0;
    L6:
        return StandardCharsets.UTF_8;
    }

    private void parseDialogueLine(String r19, SsaDialogueFormat r20, List<List<Cue>> r21, List<Long> r22) {
        Assertions.checkArgument(r19.startsWith(DIALOGUE_LINE_PREFIX));
        String[] r5 = r19.substring(DIALOGUE_LINE_PREFIX.length()).split(",", r20.length);
        if (r5.length == r20.length) goto L6;
        Log.m48w(TAG, "Skipping dialogue line with fewer columns than format: " + r19);
        return;
    L6:
        long r6 = parseTimecodeUs(r5[r20.startTimeIndex]);
        if (r6 != C0180C.TIME_UNSET) goto L10;
        Log.m48w(TAG, "Skipping invalid timing: " + r19);
        return;
    L10:
        long r13 = parseTimecodeUs(r5[r20.endTimeIndex]);
        if (r13 != C0180C.TIME_UNSET) goto L15;
        Log.m48w(TAG, "Skipping invalid timing: " + r19);
        return;
    L15:
        if (this.styles != null) goto L17;
    L19:
        SsaStyle r8 = null;
    L20:
        String r9 = r5[r20.textIndex];
        Cue r12 = createCue(SsaStyle.Overrides.stripStyleOverrides(r9).replace("\\N", "\n").replace("\\n", "\n").replace("\\h", " "), r8, SsaStyle.Overrides.parseFromDialogue(r9), this.screenWidth, this.screenHeight);
        int r15 = addCuePlacerholderByTime(r6, r22, r21);
        int r0 = addCuePlacerholderByTime(r13, r22, r21);
        int r1 = r15;
    L21:
        if (r1 >= r0) goto L23;
        r21.get(r1).add(r12);
        r1 = r1 + 1;
        r0 = r0;
        goto L21
    L23:
        return;
    L17:
        if (r20.styleIndex == (-1)) goto L19;
        r8 = this.styles.get(r5[r20.styleIndex].trim());
        goto L20
    }

    private void parseEventBody(ParsableByteArray r5, List<List<Cue>> r6, List<Long> r7, Charset r8) {
        if (this.haveInitializationData == false) goto L5;
        SsaDialogueFormat r0 = this.dialogueFormatFromInitializationData;
    L6:
        String r1 = r5.readLine(r8);
        if (r1 == null) goto L16;
        if (r1.startsWith(FORMAT_LINE_PREFIX) == true) goto L10;
        if (r1.startsWith(DIALOGUE_LINE_PREFIX) == false) goto L6;
        if (r0 == null) goto L14;
        parseDialogueLine(r1, r0, r6, r7);
        goto L6
    L14:
        Log.m48w(TAG, "Skipping dialogue line before complete format: " + r1);
        goto L6
    L10:
        r0 = SsaDialogueFormat.fromFormatLine(r1);
        goto L6
    L16:
        return;
    L5:
        r0 = null;
        goto L6
    }

    private void parseHeader(ParsableByteArray r4, Charset r5) {
    L2:
        String r0 = r4.readLine(r5);
        if (r0 == null) goto L16;
        if ("[Script Info]".equalsIgnoreCase(r0) == true) goto L6;
        if ("[V4+ Styles]".equalsIgnoreCase(r0) == true) goto L9;
        if ("[V4 Styles]".equalsIgnoreCase(r0) == true) goto L12;
        if ("[Events]".equalsIgnoreCase(r0) == false) goto L2;
        return;
    L12:
        Log.m46i(TAG, "[V4 Styles] are not supported");
        goto L2
    L9:
        this.styles = parseStyles(r4, r5);
        goto L2
    L6:
        parseScriptInfo(r4, r5);
        goto L2
    }

    private void parseScriptInfo(ParsableByteArray r7, Charset r8) {
    L2:
        String r0 = r7.readLine(r8);
        if (r0 == null) goto L31;
        if (r7.bytesLeft() != 0) goto L7;
    L8:
        String[] r1 = r0.split(":");
        if (r1.length != 2) goto L2;
        byte r2 = 0;
        String r3 = Ascii.toLowerCase(r1[0].trim());
        switch(r3.hashCode()) {
            case 1879649548: goto L18;
            case 1879649549: goto L15;
            default: goto L20;
        };
    L20:
        r2 = -1;
    L21:
        switch(r2) {
            case 0: goto L32;
            case 1: goto L34;
            default: goto L2;
        };
    L32:
        this.screenWidth = Float.parseFloat(r1[1].trim());     // Catch: NumberFormatException -> L29
        goto L2
    L34:
        this.screenHeight = Float.parseFloat(r1[1].trim());     // Catch: NumberFormatException -> L25
        goto L2
    L15:
        if (r3.equals("playresy") == false) goto L20;
        r2 = 1;
        goto L21
    L18:
        if (r3.equals("playresx") == false) goto L20;
    L7:
        if (r7.peekChar(r8) != '[') goto L8;
        return;
    }

    private static Map<String, SsaStyle> parseStyles(ParsableByteArray r5, Charset r6) {
        LinkedHashMap r0 = new LinkedHashMap();
        SsaStyle.Format r1 = null;
    L3:
        String r2 = r5.readLine(r6);
        if (r2 == null) goto L20;
        if (r5.bytesLeft() == 0) goto L10;
        if (r5.peekChar(r6) == '[') goto L20;
    L10:
        if (r2.startsWith(FORMAT_LINE_PREFIX) == true) goto L11;
        if (r2.startsWith(STYLE_LINE_PREFIX) == false) goto L3;
        if (r1 == null) goto L15;
        SsaStyle r3 = SsaStyle.fromStyleLine(r2, r1);
        if (r3 == null) goto L3;
        r0.put(r3.name, r3);
        goto L3
    L15:
        Log.m48w(TAG, "Skipping 'Style:' line before 'Format:' line: " + r2);
        goto L3
    L11:
        r1 = SsaStyle.Format.fromFormatLine(r2);
    L20:
        return r0;
    }

    private static long parseTimecodeUs(String r9) {
        Matcher r0 = SSA_TIMECODE_PATTERN.matcher(r9.trim());
        if (r0.matches() == true) goto L7;
        return C0180C.TIME_UNSET;
    L7:
        return (((((Long.parseLong((String) Util.castNonNull(r0.group(1))) * 60) * 60) * 1000000) + ((Long.parseLong((String) Util.castNonNull(r0.group(2))) * 60) * 1000000)) + (Long.parseLong((String) Util.castNonNull(r0.group(3))) * 1000000)) + (Long.parseLong((String) Util.castNonNull(r0.group(4))) * Renderer.DEFAULT_DURATION_TO_PROGRESS_US);
    }

    private static int toLineAnchor(int r3) {
        switch(r3) {
            case -1: goto L12;
            case 0: goto L4;
            case 1: goto L10;
            case 2: goto L10;
            case 3: goto L10;
            case 4: goto L8;
            case 5: goto L8;
            case 6: goto L8;
            case 7: goto L6;
            case 8: goto L6;
            case 9: goto L6;
            default: goto L4;
        };
    L4:
        Log.m48w(TAG, "Unknown alignment: " + r3);
        return Integer.MIN_VALUE;
    L6:
        return 0;
    L8:
        return 1;
    L10:
        return 2;
    L12:
        return Integer.MIN_VALUE;
    }

    private static int toPositionAnchor(int r3) {
        switch(r3) {
            case -1: goto L12;
            case 0: goto L4;
            case 1: goto L10;
            case 2: goto L8;
            case 3: goto L6;
            case 4: goto L10;
            case 5: goto L8;
            case 6: goto L6;
            case 7: goto L10;
            case 8: goto L8;
            case 9: goto L6;
            default: goto L4;
        };
    L4:
        Log.m48w(TAG, "Unknown alignment: " + r3);
        return Integer.MIN_VALUE;
    L6:
        return 2;
    L8:
        return 1;
    L10:
        return 0;
    L12:
        return Integer.MIN_VALUE;
    }

    private static Layout.Alignment toTextAlignment(int r3) {
        switch(r3) {
            case -1: goto L12;
            case 0: goto L4;
            case 1: goto L11;
            case 2: goto L9;
            case 3: goto L7;
            case 4: goto L11;
            case 5: goto L9;
            case 6: goto L7;
            case 7: goto L11;
            case 8: goto L9;
            case 9: goto L7;
            default: goto L4;
        };
    L4:
        Log.m48w(TAG, "Unknown alignment: " + r3);
        return null;
    L12:
        return null;
    L7:
        return Layout.Alignment.ALIGN_OPPOSITE;
    L9:
        return Layout.Alignment.ALIGN_CENTER;
    L11:
        return Layout.Alignment.ALIGN_NORMAL;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 1;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r22, int r23, int r24, SubtitleParser.OutputOptions r25, Consumer<CuesWithTiming> r26) {
        ArrayList r4 = new ArrayList();
        ArrayList r5 = new ArrayList();
        this.parsableByteArray.reset(r22, r23 + r24);
        this.parsableByteArray.setPosition(r23);
        Charset r6 = detectUtfCharset(this.parsableByteArray);
        if (this.haveInitializationData == true) goto L5;
        parseHeader(this.parsableByteArray, r6);
    L5:
        parseEventBody(this.parsableByteArray, r4, r5, r6);
        long r9 = r25.startTimeUs;
        long r11 = C0180C.TIME_UNSET;
        if (r9 != C0180C.TIME_UNSET) goto L8;
    L10:
        ArrayList r7 = null;
    L11:
        int r10 = 0;
    L13:
        if (r10 >= r4.size()) goto L31;
        List<Cue> r14 = r4.get(r10);
        if (r14.isEmpty() == false) goto L19;
        if (r10 == 0) goto L19;
        long r19 = r11;
    L28:
        r10 = r10 + 1;
        r11 = r19;
    L19:
        if (r10 == (r4.size() - 1)) goto L30;
        long r15 = r5.get(r10).longValue();
        long r17 = r5.get(r10 + 1).longValue() - r5.get(r10).longValue();
        r19 = r11;
        if (r25.startTimeUs != r19) goto L23;
    L27:
        r26.accept(new CuesWithTiming(r14, r15, r17));
        goto L28
    L23:
        if (r15 >= r25.startTimeUs) goto L27;
        if (r7 == null) goto L28;
        r7.add(new CuesWithTiming(r14, r15, r17));
        goto L28
    L30:
        throw new IllegalStateException();
    L31:
        if (r7 == null) goto L36;
        Iterator r12 = r7.iterator();
    L34:
        if (r12.hasNext() == false) goto L44;
        r26.accept((CuesWithTiming) r12.next());
        goto L34
    L44:
        return;
    L36:
        return;
    L8:
        if (r25.outputAllCues == false) goto L10;
        r7 = new ArrayList();
        goto L11
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public /* synthetic */ void parse(byte[] r1, SubtitleParser.OutputOptions r2, Consumer r3) {
        SubtitleParser.CC.$default$parse(this, r1, r2, r3);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public /* synthetic */ Subtitle parseToLegacySubtitle(byte[] r1, int r2, int r3) {
        return SubtitleParser.CC.$default$parseToLegacySubtitle(this, r1, r2, r3);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public /* synthetic */ void reset() {
        SubtitleParser.CC.$default$reset(this);
    }
}
