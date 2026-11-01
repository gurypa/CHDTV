package androidx.media3.extractor.text.webvtt;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class WebvttParserUtil {
    private static final Pattern COMMENT = null;
    private static final String WEBVTT_HEADER = "WEBVTT";

    static {
        COMMENT = Pattern.compile("^NOTE([ \t].*)?$");
    }

    private WebvttParserUtil() {
    }

    public static Matcher findNextCueHeader(ParsableByteArray r3) {
    L2:
        String r0 = r3.readLine();
        if (r0 == null) goto L15;
        if (COMMENT.matcher(r0).matches() == true) goto L6;
        Matcher r1 = WebvttCueParser.CUE_HEADER_PATTERN.matcher(r0);
        if (r1.matches() == false) goto L2;
        return r1;
    L6:
        String r2 = r3.readLine();
        if (r2 == null) goto L2;
        if (r2.isEmpty() == true) goto L2;
    L15:
        return null;
    }

    public static boolean isWebvttHeaderLine(ParsableByteArray r2) {
        String r0 = r2.readLine();
        if (r0 != null) goto L5;
    L7:
        return false;
    L5:
        if (r0.startsWith(WEBVTT_HEADER) == false) goto L7;
        return true;
    }

    public static float parsePercentage(String r2) throws NumberFormatException {
        if (r2.endsWith("%") == false) goto L7;
        return Float.parseFloat(r2.substring(0, r2.length() - 1)) / 100.0f;
    L7:
        throw new NumberFormatException("Percentages must end with %");
    }

    public static long parseTimestampUs(String r11) throws NumberFormatException {
        long r0 = 0;
        String[] r2 = Util.splitAtFirst(r11, "\\.");
        int r3 = 0;
        String[] r4 = Util.split(r2[0], ":");
        int r5 = r4.length;
    L3:
        if (r3 >= r5) goto L5;
        r0 = (60 * r0) + Long.parseLong(r4[r3]);
        r3 = r3 + 1;
        goto L3
    L5:
        long r1 = r0 * 1000;
        if (r2.length != 2) goto L9;
        r1 = r1 + Long.parseLong(r2[1]);
    L9:
        return 1000 * r1;
    }

    public static void validateWebvttHeaderLine(ParsableByteArray r3) throws ParserException {
        int r0 = r3.getPosition();
        if (isWebvttHeaderLine(r3) == false) goto L5;
        return;
    L5:
        r3.setPosition(r0);
        throw ParserException.createForMalformedContainer("Expected WEBVTT. Got " + r3.readLine(), null);
    }
}
