package androidx.media3.extractor.text.tx3g;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Tx3gParser implements SubtitleParser {
    public static final int CUE_REPLACEMENT_BEHAVIOR = 2;
    private static final int DEFAULT_COLOR = -1;
    private static final int DEFAULT_FONT_FACE = 0;
    private static final String DEFAULT_FONT_FAMILY = "sans-serif";
    private static final float DEFAULT_VERTICAL_PLACEMENT = 0.85f;
    private static final int FONT_FACE_BOLD = 1;
    private static final int FONT_FACE_ITALIC = 2;
    private static final int FONT_FACE_UNDERLINE = 4;
    private static final int SIZE_ATOM_HEADER = 8;
    private static final int SIZE_SHORT = 2;
    private static final int SIZE_STYLE_RECORD = 12;
    private static final int SPAN_PRIORITY_HIGH = 0;
    private static final int SPAN_PRIORITY_LOW = 16711680;
    private static final String TAG = "Tx3gParser";
    private static final String TX3G_SERIF = "Serif";
    private static final int TYPE_STYL = 1937013100;
    private static final int TYPE_TBOX = 1952608120;
    private final int calculatedVideoTrackHeight;
    private final boolean customVerticalPlacement;
    private final int defaultColorRgba;
    private final int defaultFontFace;
    private final String defaultFontFamily;
    private final float defaultVerticalPlacement;
    private final ParsableByteArray parsableByteArray;

    public Tx3gParser(List<byte[]> r8) {
        this.parsableByteArray = new ParsableByteArray();
        String r2 = "sans-serif";
        boolean r3 = false;
        if (r8.size() == 1) goto L5;
    L19:
        this.defaultFontFace = 0;
        this.defaultColorRgba = -1;
        this.defaultFontFamily = "sans-serif";
        this.customVerticalPlacement = false;
        this.defaultVerticalPlacement = DEFAULT_VERTICAL_PLACEMENT;
        this.calculatedVideoTrackHeight = -1;
        return;
    L5:
        if (r8.get(0).length != 48) goto L7;
    L8:
        byte[] r0 = r8.get(0);
        this.defaultFontFace = r0[24];
        this.defaultColorRgba = ((((r0[26] & 255) << 24) | ((r0[27] & 255) << 16)) | ((r0[28] & 255) << 8)) | (r0[29] & 255);
        if (TX3G_SERIF.equals(Util.fromUtf8Bytes(r0, 43, r0.length - 43)) == false) goto L11;
        r2 = C0180C.SERIF_NAME;
    L11:
        this.defaultFontFamily = r2;
        this.calculatedVideoTrackHeight = r0[25] * Ascii.DC4;
        if ((r0[0] & 32) == 0) goto L14;
        r3 = true;
    L14:
        this.customVerticalPlacement = r3;
        if (this.customVerticalPlacement == false) goto L17;
        this.defaultVerticalPlacement = Util.constrainValue((((r0[10] & 255) << 8) | (r0[11] & 255)) / this.calculatedVideoTrackHeight, 0.0f, 0.95f);
        return;
    L17:
        this.defaultVerticalPlacement = DEFAULT_VERTICAL_PLACEMENT;
        return;
    L7:
        if (r8.get(0).length != 53) goto L19;
        goto L8
    }

    private void applyStyleRecord(ParsableByteArray r10, SpannableStringBuilder r11) {
        if (r10.bytesLeft() < 12) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        int r6 = r10.readUnsignedShort();
        int r1 = r10.readUnsignedShort();
        r10.skipBytes(2);
        int r4 = r10.readUnsignedByte();
        r10.skipBytes(1);
        int r2 = r10.readInt();
        if (r1 <= r11.length()) goto L9;
        Log.m48w(TAG, "Truncating styl end (" + r1 + ") to cueText.length() (" + r11.length() + ").");
        int r7 = r11.length();
    L10:
        if (r6 < r7) goto L13;
        Log.m48w(TAG, "Ignoring styl with start (" + r6 + ") >= end (" + r7 + ").");
        return;
    L13:
        attachFontFace(r11, r4, this.defaultFontFace, r6, r7, 0);
        attachColor(r11, r2, this.defaultColorRgba, r6, r7, 0);
        return;
    L9:
        r7 = r1;
        goto L10
    L5:
        r0 = false;
        goto L6
    }

    private static void attachColor(SpannableStringBuilder r3, int r4, int r5, int r6, int r7, int r8) {
        if (r4 == r5) goto L5;
        r3.setSpan(new ForegroundColorSpan(((r4 & 255) << 24) | (r4 >>> 8)), r6, r7, r8 | 33);
        return;
    }

    private static void attachFontFace(SpannableStringBuilder r7, int r8, int r9, int r10, int r11, int r12) {
        if (r8 == r9) goto L28;
        int r0 = r12 | 33;
        boolean r3 = true;
        if ((r8 & 1) == 0) goto L6;
        boolean r1 = true;
    L8:
        if ((r8 & 2) == 0) goto L10;
        boolean r4 = true;
    L11:
        if (r1 == false) goto L15;
        if (r4 == false) goto L14;
        r7.setSpan(new StyleSpan(3), r10, r11, r0);
    L18:
        if ((r8 & 4) != 0) goto L21;
        r3 = false;
    L21:
        if (r3 == false) goto L23;
        r7.setSpan(new UnderlineSpan(), r10, r11, r0);
    L23:
        if (r3 == true) goto L29;
        if (r1 == true) goto L30;
        if (r4 == true) goto L31;
        r7.setSpan(new StyleSpan(0), r10, r11, r0);
        return;
    L31:
        return;
    L30:
        return;
    L29:
        return;
    L14:
        r7.setSpan(new StyleSpan(1), r10, r11, r0);
        goto L18
    L15:
        if (r4 == false) goto L18;
        r7.setSpan(new StyleSpan(2), r10, r11, r0);
        goto L18
    L10:
        r4 = false;
        goto L11
    L6:
        r1 = false;
        goto L8
    }

    private static void attachFontFamily(SpannableStringBuilder r2, String r3, int r4, int r5) {
        if (r3 == "sans-serif") goto L6;
        r2.setSpan(new TypefaceSpan(r3), r4, r5, 16711713);
        return;
    }

    private static String readSubtitleText(ParsableByteArray r6) {
        if (r6.bytesLeft() < 2) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkArgument(r0);
        int r1 = r6.readUnsignedShort();
        if (r1 != 0) goto L10;
        return "";
    L10:
        int r2 = r6.getPosition();
        Charset r3 = r6.readUtfCharsetFromBom();
        int r4 = r1 - (r6.getPosition() - r2);
        if (r3 == null) goto L13;
        Charset r5 = r3;
    L15:
        return r6.readString(r4, r5);
    L13:
        r5 = StandardCharsets.UTF_8;
        goto L15
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 2;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r11, int r12, int r13, SubtitleParser.OutputOptions r14, Consumer<CuesWithTiming> r15) {
        this.parsableByteArray.reset(r11, r12 + r13);
        this.parsableByteArray.setPosition(r12);
        String r0 = readSubtitleText(this.parsableByteArray);
        if (r0.isEmpty() == false) goto L6;
        r15.accept(new CuesWithTiming(ImmutableList.m212of(), C0180C.TIME_UNSET, C0180C.TIME_UNSET));
        return;
    L6:
        SpannableStringBuilder r1 = new SpannableStringBuilder(r0);
        attachFontFace(r1, this.defaultFontFace, 0, 0, r1.length(), SPAN_PRIORITY_LOW);
        attachColor(r1, this.defaultColorRgba, -1, 0, r1.length(), SPAN_PRIORITY_LOW);
        attachFontFamily(r1, this.defaultFontFamily, 0, r1.length());
        float r2 = this.defaultVerticalPlacement;
    L8:
        if (this.parsableByteArray.bytesLeft() < 8) goto L30;
        int r3 = this.parsableByteArray.getPosition();
        int r5 = this.parsableByteArray.readInt();
        int r6 = this.parsableByteArray.readInt();
        boolean r9 = true;
        if (r6 != TYPE_STYL) goto L20;
        if (this.parsableByteArray.bytesLeft() >= 2) goto L15;
        r9 = false;
    L15:
        Assertions.checkArgument(r9);
        int r7 = this.parsableByteArray.readUnsignedShort();
        int r8 = 0;
    L16:
        if (r8 >= r7) goto L29;
        applyStyleRecord(this.parsableByteArray, r1);
        r8 = r8 + 1;
    L29:
        this.parsableByteArray.setPosition(r3 + r5);
        goto L8
    L20:
        if (r6 != TYPE_TBOX) goto L29;
        if (this.customVerticalPlacement == false) goto L29;
        if (this.parsableByteArray.bytesLeft() >= 2) goto L27;
        r9 = false;
    L27:
        Assertions.checkArgument(r9);
        r2 = Util.constrainValue(this.parsableByteArray.readUnsignedShort() / this.calculatedVideoTrackHeight, 0.0f, 0.95f);
        goto L29
    L30:
        r15.accept(new CuesWithTiming(ImmutableList.m213of(new Cue.Builder().setText(r1).setLine(r2, 0).setLineAnchor(0).build()), C0180C.TIME_UNSET, C0180C.TIME_UNSET));
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
