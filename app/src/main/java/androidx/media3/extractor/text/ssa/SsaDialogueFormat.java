package androidx.media3.extractor.text.ssa;

import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.common.base.Ascii;

/* JADX INFO: loaded from: classes.dex */
final class SsaDialogueFormat {
    public final int endTimeIndex;
    public final int length;
    public final int startTimeIndex;
    public final int styleIndex;
    public final int textIndex;

    private SsaDialogueFormat(int r1, int r2, int r3, int r4, int r5) {
        this.startTimeIndex = r1;
        this.endTimeIndex = r2;
        this.styleIndex = r3;
        this.textIndex = r4;
        this.length = r5;
    }

    public static SsaDialogueFormat fromFormatLine(String r12) {
        Assertions.checkArgument(r12.startsWith("Format:"));
        String[] r4 = TextUtils.split(r12.substring("Format:".length()), ",");
        int r5 = 0;
        int r7 = -1;
        int r8 = -1;
        int r9 = -1;
        int r10 = -1;
    L3:
        byte r1 = -1;
        if (r5 >= r4.length) goto L27;
        String r0 = Ascii.toLowerCase(r4[r5].trim());
        switch(r0.hashCode()) {
            case 100571: goto L18;
            case 3556653: goto L15;
            case 109757538: goto L12;
            case 109780401: goto L9;
            default: goto L20;
        };
    L20:
        switch(r1) {
            case 0: goto L25;
            case 1: goto L24;
            case 2: goto L23;
            case 3: goto L22;
            default: goto L26;
        };
    L22:
        r10 = r5;
        goto L26
    L23:
        r9 = r5;
        goto L26
    L24:
        r8 = r5;
        goto L26
    L25:
        r7 = r5;
    L26:
        r5 = r5 + 1;
        goto L3
    L9:
        if (r0.equals(TtmlNode.TAG_STYLE) == false) goto L20;
        r1 = 2;
        goto L20
    L12:
        if (r0.equals("start") == false) goto L20;
        r1 = 0;
        goto L20
    L15:
        if (r0.equals("text") == false) goto L20;
        r1 = 3;
        goto L20
    L18:
        if (r0.equals("end") == false) goto L20;
        r1 = 1;
        goto L20
    L27:
        if (r7 == (-1)) goto L31;
        if (r8 == (-1)) goto L31;
        if (r10 == (-1)) goto L31;
        return new SsaDialogueFormat(r7, r8, r9, r10, r4.length);
    L31:
        return null;
    }
}
