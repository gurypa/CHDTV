package androidx.media3.extractor.text.webvtt;

import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public final class Mp4WebvttParser implements SubtitleParser {
    private static final int BOX_HEADER_SIZE = 8;
    public static final int CUE_REPLACEMENT_BEHAVIOR = 2;
    private static final int TYPE_payl = 1885436268;
    private static final int TYPE_sttg = 1937011815;
    private static final int TYPE_vttc = 1987343459;
    private final ParsableByteArray parsableByteArray;

    public Mp4WebvttParser() {
        this.parsableByteArray = new ParsableByteArray();
    }

    private static Cue parseVttCueBox(ParsableByteArray r9, int r10) {
        Cue.Builder r0 = null;
        CharSequence r1 = null;
    L3:
        if (r10 <= 0) goto L15;
        if (r10 < 8) goto L7;
        boolean r2 = true;
    L8:
        Assertions.checkArgument(r2, "Incomplete vtt cue box header found.");
        int r3 = r9.readInt();
        int r4 = r9.readInt();
        int r5 = r3 - 8;
        String r6 = Util.fromUtf8Bytes(r9.getData(), r9.getPosition(), r5);
        r9.skipBytes(r5);
        r10 = (r10 - 8) - r5;
        if (r4 == TYPE_sttg) goto L10;
        if (r4 != TYPE_payl) goto L3;
        r1 = WebvttCueParser.parseCueText(null, r6.trim(), Collections.emptyList());
        goto L3
    L10:
        r0 = WebvttCueParser.parseCueSettingsList(r6);
        goto L3
    L7:
        r2 = false;
        goto L8
    L15:
        if (r1 != null) goto L17;
        r1 = "";
    L17:
        if (r0 == null) goto L20;
        return r0.setText(r1).build();
    L20:
        return WebvttCueParser.newCueForText(r1);
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 2;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r8, int r9, int r10, SubtitleParser.OutputOptions r11, Consumer<CuesWithTiming> r12) {
        this.parsableByteArray.reset(r8, r9 + r10);
        this.parsableByteArray.setPosition(r9);
        ArrayList r0 = new ArrayList();
    L4:
        if (this.parsableByteArray.bytesLeft() <= 0) goto L14;
        if (this.parsableByteArray.bytesLeft() < 8) goto L8;
        boolean r1 = true;
    L9:
        Assertions.checkArgument(r1, "Incomplete Mp4Webvtt Top Level box header found.");
        int r2 = this.parsableByteArray.readInt();
        if (this.parsableByteArray.readInt() == TYPE_vttc) goto L11;
        this.parsableByteArray.skipBytes(r2 - 8);
        goto L4
    L11:
        r0.add(parseVttCueBox(this.parsableByteArray, r2 - 8));
        goto L4
    L8:
        r1 = false;
        goto L9
    L14:
        r12.accept(new CuesWithTiming(r0, C0180C.TIME_UNSET, C0180C.TIME_UNSET));
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
