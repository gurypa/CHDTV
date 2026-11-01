package androidx.media3.extractor.text.webvtt;

import android.text.TextUtils;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.LegacySubtitleUtil;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class WebvttParser implements SubtitleParser {
    private static final String COMMENT_START = "NOTE";
    public static final int CUE_REPLACEMENT_BEHAVIOR = 1;
    private static final int EVENT_COMMENT = 1;
    private static final int EVENT_CUE = 3;
    private static final int EVENT_END_OF_FILE = 0;
    private static final int EVENT_NONE = -1;
    private static final int EVENT_STYLE_BLOCK = 2;
    private static final String STYLE_START = "STYLE";
    private final WebvttCssParser cssParser;
    private final ParsableByteArray parsableWebvttData;

    public WebvttParser() {
        this.parsableWebvttData = new ParsableByteArray();
        this.cssParser = new WebvttCssParser();
    }

    private static int getNextEvent(ParsableByteArray r4) {
        int r0 = -1;
        int r1 = 0;
    L4:
        if (r0 != (-1)) goto L16;
        r1 = r4.getPosition();
        String r2 = r4.readLine();
        if (r2 == null) goto L7;
        if (STYLE_START.equals(r2) == true) goto L10;
        if (r2.startsWith(COMMENT_START) == true) goto L13;
        r0 = 3;
        goto L4
    L13:
        r0 = 1;
        goto L4
    L10:
        r0 = 2;
        goto L4
    L7:
        r0 = 0;
        goto L4
    L16:
        r4.setPosition(r1);
        return r0;
    }

    private static void skipComment(ParsableByteArray r1) {
    L3:
        if (TextUtils.isEmpty(r1.readLine()) == false) goto L3;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 1;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r6, int r7, int r8, SubtitleParser.OutputOptions r9, Consumer<CuesWithTiming> r10) {
        this.parsableWebvttData.reset(r6, r7 + r8);
        this.parsableWebvttData.setPosition(r7);
        ArrayList r0 = new ArrayList();
        WebvttParserUtil.validateWebvttHeaderLine(this.parsableWebvttData);     // Catch: ParserException -> L28
    L5:
        if (TextUtils.isEmpty(this.parsableWebvttData.readLine()) == false) goto L5;
        ArrayList r1 = new ArrayList();
    L8:
        int r2 = getNextEvent(this.parsableWebvttData);
        if (r2 == 0) goto L26;
        if (r2 == 1) goto L12;
        if (r2 == 2) goto L16;
        if (r2 != 3) goto L8;
        WebvttCueInfo r3 = WebvttCueParser.parseCue(this.parsableWebvttData, r0);
        if (r3 == null) goto L8;
        r1.add(r3);
        goto L8
    L16:
        if (r1.isEmpty() == false) goto L19;
        this.parsableWebvttData.readLine();
        r0.addAll(this.cssParser.parseBlock(this.parsableWebvttData));
        goto L8
    L19:
        throw new IllegalArgumentException("A style block was found after the first cue.");
    L12:
        skipComment(this.parsableWebvttData);
        goto L8
    L26:
        LegacySubtitleUtil.toCuesWithTiming(new WebvttSubtitle(r1), r9, r10);
        return;
    L28:
        e = move-exception;
        throw new IllegalArgumentException(e);
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
