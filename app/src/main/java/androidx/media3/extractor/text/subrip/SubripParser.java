package androidx.media3.extractor.text.subrip;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.collect.ImmutableList;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class SubripParser implements SubtitleParser {
    private static final String ALIGN_BOTTOM_LEFT = "{\\an1}";
    private static final String ALIGN_BOTTOM_MID = "{\\an2}";
    private static final String ALIGN_BOTTOM_RIGHT = "{\\an3}";
    private static final String ALIGN_MID_LEFT = "{\\an4}";
    private static final String ALIGN_MID_MID = "{\\an5}";
    private static final String ALIGN_MID_RIGHT = "{\\an6}";
    private static final String ALIGN_TOP_LEFT = "{\\an7}";
    private static final String ALIGN_TOP_MID = "{\\an8}";
    private static final String ALIGN_TOP_RIGHT = "{\\an9}";
    public static final int CUE_REPLACEMENT_BEHAVIOR = 1;
    private static final float END_FRACTION = 0.92f;
    private static final float MID_FRACTION = 0.5f;
    private static final float START_FRACTION = 0.08f;
    private static final String SUBRIP_ALIGNMENT_TAG = "\\{\\\\an[1-9]\\}";
    private static final Pattern SUBRIP_TAG_PATTERN = null;
    private static final String SUBRIP_TIMECODE = "(?:(\\d+):)?(\\d+):(\\d+)(?:,(\\d+))?";
    private static final Pattern SUBRIP_TIMING_LINE = null;
    private static final String TAG = "SubripParser";
    private final ParsableByteArray parsableByteArray;
    private final ArrayList<String> tags;
    private final StringBuilder textBuilder;

    static {
        SUBRIP_TIMING_LINE = Pattern.compile("\\s*((?:(\\d+):)?(\\d+):(\\d+)(?:,(\\d+))?)\\s*-->\\s*((?:(\\d+):)?(\\d+):(\\d+)(?:,(\\d+))?)\\s*");
        SUBRIP_TAG_PATTERN = Pattern.compile("\\{\\\\.*?\\}");
    }

    public SubripParser() {
        this.textBuilder = new StringBuilder();
        this.tags = new ArrayList();
        this.parsableByteArray = new ParsableByteArray();
    }

    private Cue buildCue(Spanned r18, String r19) {
        Cue.Builder r1 = new Cue.Builder().setText(r18);
        if (r19 != null) goto L7;
        return r1.build();
    L7:
        switch(r19.hashCode()) {
            case -685620710: goto L34;
            case -685620679: goto L31;
            case -685620648: goto L28;
            case -685620617: goto L25;
            case -685620586: goto L22;
            case -685620555: goto L19;
            case -685620524: goto L16;
            case -685620493: goto L13;
            case -685620462: goto L10;
            default: goto L36;
        };
    L36:
        byte r3 = -1;
    L37:
        switch(r3) {
            case 0: goto L40;
            case 1: goto L40;
            case 2: goto L40;
            case 3: goto L39;
            case 4: goto L39;
            case 5: goto L39;
            default: goto L38;
        };
    L38:
        r1.setPositionAnchor(1);
    L42:
        switch(r19.hashCode()) {
            case -685620710: goto L69;
            case -685620679: goto L66;
            case -685620648: goto L63;
            case -685620617: goto L60;
            case -685620586: goto L57;
            case -685620555: goto L54;
            case -685620524: goto L51;
            case -685620493: goto L48;
            case -685620462: goto L45;
            default: goto L71;
        };
    L71:
        byte r4 = -1;
    L72:
        switch(r4) {
            case 0: goto L75;
            case 1: goto L75;
            case 2: goto L75;
            case 3: goto L74;
            case 4: goto L74;
            case 5: goto L74;
            default: goto L73;
        };
    L73:
        r1.setLineAnchor(1);
    L77:
        return r1.setPosition(getFractionalPositionForAnchorType(r1.getPositionAnchor())).setLine(getFractionalPositionForAnchorType(r1.getLineAnchor()), 0).build();
    L74:
        r1.setLineAnchor(0);
        goto L77
    L75:
        r1.setLineAnchor(2);
        goto L77
    L45:
        if (r19.equals(ALIGN_TOP_RIGHT) == false) goto L78;
        r4 = 5;
        goto L72
    L78:
        r4 = -1;
        goto L72
    L48:
        if (r19.equals(ALIGN_TOP_MID) == false) goto L79;
        r4 = 4;
        goto L72
    L79:
        r4 = -1;
        goto L72
    L51:
        if (r19.equals(ALIGN_TOP_LEFT) == false) goto L80;
        r4 = 3;
        goto L72
    L80:
        r4 = -1;
        goto L72
    L54:
        if (r19.equals(ALIGN_MID_RIGHT) == false) goto L81;
        r4 = 8;
        goto L72
    L81:
        r4 = -1;
        goto L72
    L57:
        if (r19.equals(ALIGN_MID_MID) == false) goto L82;
        r4 = 7;
        goto L72
    L82:
        r4 = -1;
        goto L72
    L60:
        if (r19.equals(ALIGN_MID_LEFT) == false) goto L83;
        r4 = 6;
        goto L72
    L83:
        r4 = -1;
        goto L72
    L63:
        if (r19.equals(ALIGN_BOTTOM_RIGHT) == false) goto L84;
        r4 = 2;
        goto L72
    L84:
        r4 = -1;
        goto L72
    L66:
        if (r19.equals(ALIGN_BOTTOM_MID) == false) goto L85;
        r4 = 1;
        goto L72
    L85:
        r4 = -1;
        goto L72
    L69:
        if (r19.equals(ALIGN_BOTTOM_LEFT) == false) goto L86;
        r4 = 0;
        goto L72
    L86:
        r4 = -1;
        goto L72
    L39:
        r1.setPositionAnchor(2);
        goto L42
    L40:
        r1.setPositionAnchor(0);
        goto L42
    L10:
        if (r19.equals(ALIGN_TOP_RIGHT) == false) goto L87;
        r3 = 5;
        goto L37
    L87:
        r3 = -1;
        goto L37
    L13:
        if (r19.equals(ALIGN_TOP_MID) == false) goto L88;
        r3 = 8;
        goto L37
    L88:
        r3 = -1;
        goto L37
    L16:
        if (r19.equals(ALIGN_TOP_LEFT) == false) goto L89;
        r3 = 2;
        goto L37
    L89:
        r3 = -1;
        goto L37
    L19:
        if (r19.equals(ALIGN_MID_RIGHT) == false) goto L90;
        r3 = 4;
        goto L37
    L90:
        r3 = -1;
        goto L37
    L22:
        if (r19.equals(ALIGN_MID_MID) == false) goto L91;
        r3 = 7;
        goto L37
    L91:
        r3 = -1;
        goto L37
    L25:
        if (r19.equals(ALIGN_MID_LEFT) == false) goto L92;
        r3 = 1;
        goto L37
    L92:
        r3 = -1;
        goto L37
    L28:
        if (r19.equals(ALIGN_BOTTOM_RIGHT) == false) goto L93;
        r3 = 3;
        goto L37
    L93:
        r3 = -1;
        goto L37
    L31:
        if (r19.equals(ALIGN_BOTTOM_MID) == false) goto L94;
        r3 = 6;
        goto L37
    L94:
        r3 = -1;
        goto L37
    L34:
        if (r19.equals(ALIGN_BOTTOM_LEFT) == false) goto L95;
        r3 = 0;
        goto L37
    L95:
        r3 = -1;
        goto L37
    }

    private Charset detectUtfCharset(ParsableByteArray r3) {
        Charset r0 = r3.readUtfCharsetFromBom();
        if (r0 == null) goto L6;
        return r0;
    L6:
        return StandardCharsets.UTF_8;
    }

    public static float getFractionalPositionForAnchorType(int r1) {
        switch(r1) {
            case 0: goto L9;
            case 1: goto L7;
            case 2: goto L5;
            default: goto L4;
        };
    L5:
        return END_FRACTION;
    L7:
        return 0.5f;
    L9:
        return 0.08f;
    L4:
        throw new IllegalArgumentException();
    }

    private static long parseTimecode(Matcher r9, int r10) {
        String r0 = r9.group(r10 + 1);
        if (r0 == null) goto L5;
        long r5 = ((Long.parseLong(r0) * 60) * 60) * 1000;
    L6:
        long r6 = (r5 + ((Long.parseLong((String) Assertions.checkNotNull(r9.group(r10 + 2))) * 60) * 1000)) + (Long.parseLong((String) Assertions.checkNotNull(r9.group(r10 + 3))) * 1000);
        String r1 = r9.group(r10 + 4);
        if (r1 == null) goto L10;
        r6 = r6 + Long.parseLong(r1);
    L10:
        return 1000 * r6;
    L5:
        r5 = 0;
        goto L6
    }

    private String processLine(String r9, ArrayList<String> r10) {
        String r11 = r9.trim();
        int r0 = 0;
        StringBuilder r1 = new StringBuilder(r11);
        Matcher r2 = SUBRIP_TAG_PATTERN.matcher(r11);
    L4:
        if (r2.find() == false) goto L7;
        String r3 = r2.group();
        r10.add(r3);
        int r4 = r2.start() - r0;
        int r5 = r3.length();
        r1.replace(r4, r4 + r5, "");
        r0 = r0 + r5;
        goto L4
    L7:
        return r1.toString();
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 1;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r24, int r25, int r26, SubtitleParser.OutputOptions r27, Consumer<CuesWithTiming> r28) {
        this.parsableByteArray.reset(r24, r25 + r26);
        this.parsableByteArray.setPosition(r25);
        Charset r6 = detectUtfCharset(this.parsableByteArray);
        long r8 = r27.startTimeUs;
        long r10 = C0180C.TIME_UNSET;
        if (r8 != C0180C.TIME_UNSET) goto L5;
    L7:
        ArrayList r0 = null;
    L8:
        ArrayList r9 = r0;
    L9:
        String r1 = this.parsableByteArray.readLine(r6);
        if (r1 == null) goto L47;
        if (r1.length() == 0) goto L9;
        Integer.parseInt(r1);     // Catch: NumberFormatException -> L45
        String r11 = this.parsableByteArray.readLine(r6);
        if (r11 == null) goto L17;
        Matcher r2 = SUBRIP_TIMING_LINE.matcher(r11);
        if (r2.matches() == true) goto L20;
        Log.m48w(TAG, "Skipping invalid timing: " + r11);
        r10 = r10;
        goto L9
    L20:
        long r15 = parseTimecode(r2, 1);
        long r19 = parseTimecode(r2, 6);
        this.textBuilder.setLength(0);
        this.tags.clear();
        String r12 = this.parsableByteArray.readLine(r6);
    L22:
        if (TextUtils.isEmpty(r12) == true) goto L27;
        if (this.textBuilder.length() <= 0) goto L26;
        this.textBuilder.append("<br>");
    L26:
        this.textBuilder.append(processLine(r12, this.tags));
        r12 = this.parsableByteArray.readLine(r6);
        goto L22
    L27:
        Spanned r13 = Html.fromHtml(this.textBuilder.toString());
        int r14 = 0;
    L28:
        long r21 = r10;
        if (r14 >= this.tags.size()) goto L34;
        String r16 = this.tags.get(r14);
        if (r16.matches(SUBRIP_ALIGNMENT_TAG) == true) goto L36;
        r14 = r14 + 1;
        r10 = r21;
    L36:
        if (r27.startTimeUs != r21) goto L38;
    L42:
        r28.accept(new CuesWithTiming(ImmutableList.m213of(buildCue(r13, r16)), r15, r19 - r15));
    L43:
        r10 = r21;
        goto L9
    L38:
        if (r15 >= r27.startTimeUs) goto L42;
        if (r9 == null) goto L43;
        r9.add(new CuesWithTiming(ImmutableList.m213of(buildCue(r13, r16)), r15, r19 - r15));
        goto L43
    L34:
        r16 = null;
        goto L36
    L17:
        Log.m48w(TAG, "Unexpected end");
    L46:
        Log.m48w(TAG, "Skipping invalid index: " + r1);
        r10 = r10;
    L47:
        if (r9 == null) goto L52;
        Iterator r3 = r9.iterator();
    L50:
        if (r3.hasNext() == false) goto L71;
        r28.accept((CuesWithTiming) r3.next());
        goto L50
    L71:
        return;
    L52:
        return;
    L5:
        if (r27.outputAllCues == false) goto L7;
        r0 = new ArrayList();
        goto L8
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
