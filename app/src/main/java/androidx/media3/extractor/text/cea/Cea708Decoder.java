package androidx.media3.extractor.text.cea;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.CodecSpecificDataUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleDecoderException;
import androidx.media3.extractor.text.SubtitleInputBuffer;
import androidx.media3.extractor.text.SubtitleOutputBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kotlin.text.Typography;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
public final class Cea708Decoder extends CeaDecoder {
    private static final int CC_VALID_FLAG = 4;
    private static final int CHARACTER_BIG_CARONS = 42;
    private static final int CHARACTER_BIG_OE = 44;
    private static final int CHARACTER_BOLD_BULLET = 53;
    private static final int CHARACTER_CLOSE_DOUBLE_QUOTE = 52;
    private static final int CHARACTER_CLOSE_SINGLE_QUOTE = 50;
    private static final int CHARACTER_DIAERESIS_Y = 63;
    private static final int CHARACTER_ELLIPSIS = 37;
    private static final int CHARACTER_FIVE_EIGHTHS = 120;
    private static final int CHARACTER_HORIZONTAL_BORDER = 125;
    private static final int CHARACTER_LOWER_LEFT_BORDER = 124;
    private static final int CHARACTER_LOWER_RIGHT_BORDER = 126;
    private static final int CHARACTER_MN = 127;
    private static final int CHARACTER_NBTSP = 33;
    private static final int CHARACTER_ONE_EIGHTH = 118;
    private static final int CHARACTER_OPEN_DOUBLE_QUOTE = 51;
    private static final int CHARACTER_OPEN_SINGLE_QUOTE = 49;
    private static final int CHARACTER_SEVEN_EIGHTHS = 121;
    private static final int CHARACTER_SM = 61;
    private static final int CHARACTER_SMALL_CARONS = 58;
    private static final int CHARACTER_SMALL_OE = 60;
    private static final int CHARACTER_SOLID_BLOCK = 48;
    private static final int CHARACTER_THREE_EIGHTHS = 119;
    private static final int CHARACTER_TM = 57;
    private static final int CHARACTER_TSP = 32;
    private static final int CHARACTER_UPPER_LEFT_BORDER = 127;
    private static final int CHARACTER_UPPER_RIGHT_BORDER = 123;
    private static final int CHARACTER_VERTICAL_BORDER = 122;
    private static final int COMMAND_BS = 8;
    private static final int COMMAND_CLW = 136;
    private static final int COMMAND_CR = 13;
    private static final int COMMAND_CW0 = 128;
    private static final int COMMAND_CW1 = 129;
    private static final int COMMAND_CW2 = 130;
    private static final int COMMAND_CW3 = 131;
    private static final int COMMAND_CW4 = 132;
    private static final int COMMAND_CW5 = 133;
    private static final int COMMAND_CW6 = 134;
    private static final int COMMAND_CW7 = 135;
    private static final int COMMAND_DF0 = 152;
    private static final int COMMAND_DF1 = 153;
    private static final int COMMAND_DF2 = 154;
    private static final int COMMAND_DF3 = 155;
    private static final int COMMAND_DF4 = 156;
    private static final int COMMAND_DF5 = 157;
    private static final int COMMAND_DF6 = 158;
    private static final int COMMAND_DF7 = 159;
    private static final int COMMAND_DLC = 142;
    private static final int COMMAND_DLW = 140;
    private static final int COMMAND_DLY = 141;
    private static final int COMMAND_DSW = 137;
    private static final int COMMAND_ETX = 3;
    private static final int COMMAND_EXT1 = 16;
    private static final int COMMAND_EXT1_END = 23;
    private static final int COMMAND_EXT1_START = 17;
    private static final int COMMAND_FF = 12;
    private static final int COMMAND_HCR = 14;
    private static final int COMMAND_HDW = 138;
    private static final int COMMAND_NUL = 0;
    private static final int COMMAND_P16_END = 31;
    private static final int COMMAND_P16_START = 24;
    private static final int COMMAND_RST = 143;
    private static final int COMMAND_SPA = 144;
    private static final int COMMAND_SPC = 145;
    private static final int COMMAND_SPL = 146;
    private static final int COMMAND_SWA = 151;
    private static final int COMMAND_TGW = 139;
    private static final int DTVCC_PACKET_DATA = 2;
    private static final int DTVCC_PACKET_START = 3;
    private static final int GROUP_C0_END = 31;
    private static final int GROUP_C1_END = 159;
    private static final int GROUP_C2_END = 31;
    private static final int GROUP_C3_END = 159;
    private static final int GROUP_G0_END = 127;
    private static final int GROUP_G1_END = 255;
    private static final int GROUP_G2_END = 127;
    private static final int GROUP_G3_END = 255;
    private static final int NUM_WINDOWS = 8;
    private static final String TAG = "Cea708Decoder";
    private final ParsableBitArray captionChannelPacketData;
    private final ParsableByteArray ccData;
    private final CueInfoBuilder[] cueInfoBuilders;
    private List<Cue> cues;
    private CueInfoBuilder currentCueInfoBuilder;
    private DtvCcPacket currentDtvCcPacket;
    private int currentWindow;
    private final boolean isWideAspectRatio;
    private List<Cue> lastCues;
    private int previousSequenceNumber;
    private final int selectedServiceNumber;

    private static final class Cea708CueInfo {
        private static final Comparator<Cea708CueInfo> LEAST_IMPORTANT_FIRST = null;
        public final Cue cue;
        public final int priority;

        static {
            LEAST_IMPORTANT_FIRST = new Cea708Decoder$Cea708CueInfo$$ExternalSyntheticLambda0();
        }

        public Cea708CueInfo(CharSequence r3, Layout.Alignment r4, float r5, int r6, int r7, float r8, int r9, float r10, boolean r11, int r12, int r13) {
            Cue.Builder r0 = new Cue.Builder().setText(r3).setTextAlignment(r4).setLine(r5, r6).setLineAnchor(r7).setPosition(r8).setPositionAnchor(r9).setSize(r10);
            if (r11 == false) goto L5;
            r0.setWindowColor(r12);
        L5:
            this.cue = r0.build();
            this.priority = r13;
        }

        static /* synthetic */ Comparator access$000() {
            return LEAST_IMPORTANT_FIRST;
        }

        static /* synthetic */ int lambda$static$0(Cea708CueInfo r2, Cea708CueInfo r3) {
            return Integer.compare(r3.priority, r2.priority);
        }
    }

    private static final class CueInfoBuilder {
        private static final int BORDER_AND_EDGE_TYPE_NONE = 0;
        private static final int BORDER_AND_EDGE_TYPE_UNIFORM = 3;
        public static final int COLOR_SOLID_BLACK = 0;
        public static final int COLOR_SOLID_WHITE = 0;
        public static final int COLOR_TRANSPARENT = 0;
        private static final int DEFAULT_PRIORITY = 4;
        private static final int DIRECTION_BOTTOM_TO_TOP = 3;
        private static final int DIRECTION_LEFT_TO_RIGHT = 0;
        private static final int DIRECTION_RIGHT_TO_LEFT = 1;
        private static final int DIRECTION_TOP_TO_BOTTOM = 2;
        private static final int HORIZONTAL_SIZE = 209;
        private static final int JUSTIFICATION_CENTER = 2;
        private static final int JUSTIFICATION_FULL = 3;
        private static final int JUSTIFICATION_LEFT = 0;
        private static final int JUSTIFICATION_RIGHT = 1;
        private static final int MAXIMUM_ROW_COUNT = 15;
        private static final int PEN_FONT_STYLE_DEFAULT = 0;
        private static final int PEN_FONT_STYLE_MONOSPACED_WITHOUT_SERIFS = 3;
        private static final int PEN_FONT_STYLE_MONOSPACED_WITH_SERIFS = 1;
        private static final int PEN_FONT_STYLE_PROPORTIONALLY_SPACED_WITHOUT_SERIFS = 4;
        private static final int PEN_FONT_STYLE_PROPORTIONALLY_SPACED_WITH_SERIFS = 2;
        private static final int PEN_OFFSET_NORMAL = 1;
        private static final int PEN_SIZE_STANDARD = 1;
        private static final int[] PEN_STYLE_BACKGROUND = null;
        private static final int[] PEN_STYLE_EDGE_TYPE = null;
        private static final int[] PEN_STYLE_FONT_STYLE = null;
        private static final int RELATIVE_CUE_SIZE = 99;
        private static final int VERTICAL_SIZE = 74;
        private static final int[] WINDOW_STYLE_FILL = null;
        private static final int[] WINDOW_STYLE_JUSTIFICATION = null;
        private static final int[] WINDOW_STYLE_PRINT_DIRECTION = null;
        private static final int[] WINDOW_STYLE_SCROLL_DIRECTION = null;
        private static final boolean[] WINDOW_STYLE_WORD_WRAP = null;
        private int anchorId;
        private int backgroundColor;
        private int backgroundColorStartPosition;
        private final SpannableStringBuilder captionStringBuilder;
        private boolean defined;
        private int foregroundColor;
        private int foregroundColorStartPosition;
        private int horizontalAnchor;
        private int italicsStartPosition;
        private int justification;
        private int penStyleId;
        private int priority;
        private boolean relativePositioning;
        private final List<SpannableString> rolledUpCaptions;
        private int row;
        private int rowCount;
        private int underlineStartPosition;
        private int verticalAnchor;
        private boolean visible;
        private int windowFillColor;
        private int windowStyleId;

        static {
            COLOR_SOLID_WHITE = getArgbColorFromCeaColor(2, 2, 2, 0);
            COLOR_SOLID_BLACK = getArgbColorFromCeaColor(0, 0, 0, 0);
            COLOR_TRANSPARENT = getArgbColorFromCeaColor(0, 0, 0, 3);
            WINDOW_STYLE_JUSTIFICATION = new int[]{0, 0, 0, 0, 0, 2, 0};
            WINDOW_STYLE_PRINT_DIRECTION = new int[]{0, 0, 0, 0, 0, 0, 2};
            WINDOW_STYLE_SCROLL_DIRECTION = new int[]{3, 3, 3, 3, 3, 3, 1};
            WINDOW_STYLE_WORD_WRAP = new boolean[]{false, false, false, true, true, true, false};
            WINDOW_STYLE_FILL = new int[]{COLOR_SOLID_BLACK, COLOR_TRANSPARENT, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_TRANSPARENT, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK};
            PEN_STYLE_FONT_STYLE = new int[]{0, 1, 2, 3, 4, 3, 4};
            PEN_STYLE_EDGE_TYPE = new int[]{0, 0, 0, 0, 0, 3, 3};
            PEN_STYLE_BACKGROUND = new int[]{COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_TRANSPARENT, COLOR_TRANSPARENT};
        }

        public CueInfoBuilder() {
            this.rolledUpCaptions = new ArrayList();
            this.captionStringBuilder = new SpannableStringBuilder();
            reset();
        }

        public static int getArgbColorFromCeaColor(int r1, int r2, int r3) {
            return getArgbColorFromCeaColor(r1, r2, r3, 0);
        }

        public static int getArgbColorFromCeaColor(int r6, int r7, int r8, int r9) {
            int r0 = 0;
            Assertions.checkIndex(r6, 0, 4);
            Assertions.checkIndex(r7, 0, 4);
            Assertions.checkIndex(r8, 0, 4);
            Assertions.checkIndex(r9, 0, 4);
            switch(r9) {
                case 0: goto L7;
                case 1: goto L7;
                case 2: goto L6;
                case 3: goto L5;
                default: goto L4;
            };
        L4:
            int r1 = 255;
        L9:
            if (r6 <= 1) goto L11;
            int r4 = 255;
        L12:
            if (r7 <= 1) goto L14;
            int r5 = 255;
        L15:
            if (r8 <= 1) goto L18;
            r0 = 255;
        L18:
            return Color.argb(r1, r4, r5, r0);
        L14:
            r5 = 0;
            goto L15
        L11:
            r4 = 0;
            goto L12
        L5:
            r1 = 0;
            goto L9
        L6:
            r1 = 127;
            goto L9
        L7:
            r1 = 255;
            goto L9
        }

        public void append(char r4) {
            if (r4 != '\n') goto L23;
            this.rolledUpCaptions.add(buildSpannableString());
            this.captionStringBuilder.clear();
            if (this.italicsStartPosition == (-1)) goto L8;
            this.italicsStartPosition = 0;
        L8:
            if (this.underlineStartPosition == (-1)) goto L11;
            this.underlineStartPosition = 0;
        L11:
            if (this.foregroundColorStartPosition == (-1)) goto L14;
            this.foregroundColorStartPosition = 0;
        L14:
            if (this.backgroundColorStartPosition == (-1)) goto L17;
            this.backgroundColorStartPosition = 0;
        L17:
            if (this.rolledUpCaptions.size() >= this.rowCount) goto L22;
            if (this.rolledUpCaptions.size() >= 15) goto L22;
            this.row = this.rolledUpCaptions.size();
            return;
        L22:
            this.rolledUpCaptions.remove(0);
            goto L17
        L23:
            this.captionStringBuilder.append(r4);
        }

        public void backspace() {
            int r0 = this.captionStringBuilder.length();
            if (r0 <= 0) goto L6;
            this.captionStringBuilder.delete(r0 - 1, r0);
            return;
        }

        public Cea708CueInfo build() {
            if (isEmpty() == false) goto L6;
            return null;
        L6:
            SpannableStringBuilder r0 = new SpannableStringBuilder();
            int r1 = 0;
        L8:
            if (r1 >= this.rolledUpCaptions.size()) goto L10;
            r0.append(this.rolledUpCaptions.get(r1));
            r0.append('\n');
            r1 = r1 + 1;
            goto L8
        L10:
            r0.append(buildSpannableString());
            switch(this.justification) {
                case 0: goto L16;
                case 1: goto L15;
                case 2: goto L14;
                case 3: goto L16;
                default: goto L13;
            };
        L14:
            Layout.Alignment r3 = Layout.Alignment.ALIGN_CENTER;
        L18:
            if (this.relativePositioning == false) goto L20;
            float r2 = this.horizontalAnchor / 99.0f;
            float r4 = this.verticalAnchor / 99.0f;
        L21:
            float r7 = (r2 * 0.9f) + 0.05f;
            float r5 = (0.9f * r4) + 0.05f;
            boolean r6 = true;
            if ((this.anchorId / 3) != 0) goto L25;
            int r8 = 0;
        L29:
            if ((this.anchorId % 3) != 0) goto L32;
            int r9 = 0;
        L36:
            if (this.windowFillColor != COLOR_SOLID_BLACK) goto L40;
            r6 = false;
        L40:
            return new Cea708CueInfo(r0, r3, r5, 0, r8, r7, r9, -3.4028235E38f, r6, this.windowFillColor, this.priority);
        L32:
            if ((this.anchorId % 3) != 1) goto L34;
            r9 = 1;
            goto L36
        L34:
            r9 = 2;
            goto L36
        L25:
            if ((this.anchorId / 3) != 1) goto L27;
            r8 = 1;
            goto L29
        L27:
            r8 = 2;
            goto L29
        L20:
            r2 = this.horizontalAnchor / 209.0f;
            r4 = this.verticalAnchor / 74.0f;
            goto L21
        L15:
            r3 = Layout.Alignment.ALIGN_OPPOSITE;
            goto L18
        L16:
            r3 = Layout.Alignment.ALIGN_NORMAL;
            goto L18
        L13:
            throw new IllegalArgumentException("Unexpected justification value: " + this.justification);
        }

        public SpannableString buildSpannableString() {
            SpannableStringBuilder r0 = new SpannableStringBuilder(this.captionStringBuilder);
            int r1 = r0.length();
            if (r1 <= 0) goto L17;
            if (this.italicsStartPosition == (-1)) goto L8;
            r0.setSpan(new StyleSpan(2), this.italicsStartPosition, r1, 33);
        L8:
            if (this.underlineStartPosition == (-1)) goto L11;
            r0.setSpan(new UnderlineSpan(), this.underlineStartPosition, r1, 33);
        L11:
            if (this.foregroundColorStartPosition == (-1)) goto L14;
            r0.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, r1, 33);
        L14:
            if (this.backgroundColorStartPosition == (-1)) goto L17;
            r0.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, r1, 33);
        L17:
            return new SpannableString(r0);
        }

        public void clear() {
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.clear();
            this.italicsStartPosition = -1;
            this.underlineStartPosition = -1;
            this.foregroundColorStartPosition = -1;
            this.backgroundColorStartPosition = -1;
            this.row = 0;
        }

        public void defineWindow(boolean r18, int r19, boolean r20, int r21, int r22, int r23, int r24, int r25, int r26) {
            this.defined = true;
            this.visible = r18;
            this.priority = r19;
            this.relativePositioning = r20;
            this.verticalAnchor = r21;
            this.horizontalAnchor = r22;
            this.anchorId = r24;
            if (this.rowCount == (r23 + 1)) goto L10;
            this.rowCount = r23 + 1;
        L6:
            if (this.rolledUpCaptions.size() >= this.rowCount) goto L9;
            if (this.rolledUpCaptions.size() < 15) goto L10;
        L9:
            this.rolledUpCaptions.remove(0);
        L10:
            if (r25 != 0) goto L12;
        L14:
            if (r26 != 0) goto L16;
            return;
        L16:
            if (this.penStyleId == r26) goto L23;
            this.penStyleId = r26;
            int r16 = r26 - 1;
            setPenAttributes(0, 1, 1, false, false, PEN_STYLE_EDGE_TYPE[r16], PEN_STYLE_FONT_STYLE[r16]);
            setPenColor(COLOR_SOLID_WHITE, PEN_STYLE_BACKGROUND[r16], COLOR_SOLID_BLACK);
            return;
        L23:
            return;
        L12:
            if (this.windowStyleId == r25) goto L14;
            this.windowStyleId = r25;
            int r17 = r25 - 1;
            setWindowAttributes(WINDOW_STYLE_FILL[r17], COLOR_TRANSPARENT, WINDOW_STYLE_WORD_WRAP[r17], 0, WINDOW_STYLE_PRINT_DIRECTION[r17], WINDOW_STYLE_SCROLL_DIRECTION[r17], WINDOW_STYLE_JUSTIFICATION[r17]);
            goto L14
        }

        public boolean isDefined() {
            return this.defined;
        }

        public boolean isEmpty() {
            if (isDefined() == true) goto L5;
        L10:
            return true;
        L5:
            if (this.rolledUpCaptions.isEmpty() == true) goto L7;
        L9:
            return false;
        L7:
            if (this.captionStringBuilder.length() != 0) goto L9;
            goto L9
        }

        public boolean isVisible() {
            return this.visible;
        }

        public void reset() {
            clear();
            this.defined = false;
            this.visible = false;
            this.priority = 4;
            this.relativePositioning = false;
            this.verticalAnchor = 0;
            this.horizontalAnchor = 0;
            this.anchorId = 0;
            this.rowCount = 15;
            this.justification = 0;
            this.windowStyleId = 0;
            this.penStyleId = 0;
            this.windowFillColor = COLOR_SOLID_BLACK;
            this.foregroundColor = COLOR_SOLID_WHITE;
            this.backgroundColor = COLOR_SOLID_BLACK;
        }

        public void setPenAttributes(int r7, int r8, int r9, boolean r10, boolean r11, int r12, int r13) {
            if (this.italicsStartPosition == (-1)) goto L6;
            if (r10 == true) goto L9;
            this.captionStringBuilder.setSpan(new StyleSpan(2), this.italicsStartPosition, this.captionStringBuilder.length(), 33);
            this.italicsStartPosition = -1;
        L9:
            if (this.underlineStartPosition == (-1)) goto L12;
            if (r11 == true) goto L15;
            this.captionStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, this.captionStringBuilder.length(), 33);
            this.underlineStartPosition = -1;
            return;
        L15:
            return;
        L12:
            if (r11 == false) goto L17;
            this.underlineStartPosition = this.captionStringBuilder.length();
            return;
        L17:
            return;
        L6:
            if (r10 == false) goto L9;
            this.italicsStartPosition = this.captionStringBuilder.length();
            goto L9
        }

        public void setPenColor(int r7, int r8, int r9) {
            if (this.foregroundColorStartPosition == (-1)) goto L8;
            if (this.foregroundColor == r7) goto L8;
            this.captionStringBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, this.captionStringBuilder.length(), 33);
        L8:
            if (r7 == COLOR_SOLID_WHITE) goto L11;
            this.foregroundColorStartPosition = this.captionStringBuilder.length();
            this.foregroundColor = r7;
        L11:
            if (this.backgroundColorStartPosition == (-1)) goto L16;
            if (this.backgroundColor == r8) goto L16;
            this.captionStringBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, this.captionStringBuilder.length(), 33);
        L16:
            if (r8 == COLOR_SOLID_BLACK) goto L19;
            this.backgroundColorStartPosition = this.captionStringBuilder.length();
            this.backgroundColor = r8;
            return;
        }

        public void setPenLocation(int r2, int r3) {
            if (this.row == r2) goto L5;
            append('\n');
        L5:
            this.row = r2;
        }

        public void setVisibility(boolean r1) {
            this.visible = r1;
        }

        public void setWindowAttributes(int r1, int r2, boolean r3, int r4, int r5, int r6, int r7) {
            this.windowFillColor = r1;
            this.justification = r7;
        }
    }

    private static final class DtvCcPacket {
        int currentIndex;
        public final byte[] packetData;
        public final int packetSize;
        public final int sequenceNumber;

        public DtvCcPacket(int r2, int r3) {
            this.sequenceNumber = r2;
            this.packetSize = r3;
            this.packetData = new byte[(r3 * 2) - 1];
            this.currentIndex = 0;
        }
    }

    public Cea708Decoder(int r6, List<byte[]> r7) {
        this.ccData = new ParsableByteArray();
        this.captionChannelPacketData = new ParsableBitArray();
        this.previousSequenceNumber = -1;
        boolean r1 = true;
        if (r6 != (-1)) goto L5;
        int r0 = 1;
    L6:
        this.selectedServiceNumber = r0;
        if (r7 != null) goto L9;
    L11:
        r1 = false;
    L12:
        this.isWideAspectRatio = r1;
        this.cueInfoBuilders = new CueInfoBuilder[8];
        int r2 = 0;
    L13:
        if (r2 >= 8) goto L15;
        this.cueInfoBuilders[r2] = new CueInfoBuilder();
        r2 = r2 + 1;
        goto L13
    L15:
        this.currentCueInfoBuilder = this.cueInfoBuilders[0];
        return;
    L9:
        if (CodecSpecificDataUtil.parseCea708InitializationData(r7) == false) goto L11;
    L5:
        r0 = r6;
        goto L6
    }

    private void finalizeCurrentPacket() {
        if (this.currentDtvCcPacket != null) goto L5;
        return;
    L5:
        processCurrentPacket();
        this.currentDtvCcPacket = null;
    }

    private List<Cue> getDisplayCues() {
        ArrayList r0 = new ArrayList();
        int r1 = 0;
    L4:
        if (r1 >= 8) goto L13;
        if (this.cueInfoBuilders[r1].isEmpty() == true) goto L12;
        if (this.cueInfoBuilders[r1].isVisible() == false) goto L12;
        Cea708CueInfo r2 = this.cueInfoBuilders[r1].build();
        if (r2 == null) goto L12;
        r0.add(r2);
    L12:
        r1 = r1 + 1;
        goto L4
    L13:
        Collections.sort(r0, Cea708CueInfo.access$000());
        ArrayList r3 = new ArrayList(r0.size());
        int r4 = 0;
    L15:
        if (r4 >= r0.size()) goto L18;
        r3.add(((Cea708CueInfo) r0.get(r4)).cue);
        r4 = r4 + 1;
        goto L15
    L18:
        return Collections.unmodifiableList(r3);
    }

    private void handleC0Command(int r4) {
        switch(r4) {
            case 0: goto L27;
            case 3: goto L12;
            case 8: goto L11;
            case 12: goto L10;
            case 13: goto L9;
            case 14: goto L27;
            default: goto L4;
        };
    L9:
        this.currentCueInfoBuilder.append('\n');
        return;
    L10:
        resetCueBuilders();
        return;
    L11:
        this.currentCueInfoBuilder.backspace();
        return;
    L12:
        this.cues = getDisplayCues();
        return;
    L27:
        return;
    L4:
        if (r4 < 17) goto L15;
        if (r4 > 23) goto L15;
        Log.m48w(TAG, "Currently unsupported COMMAND_EXT1 Command: " + r4);
        this.captionChannelPacketData.skipBits(8);
        return;
    L15:
        if (r4 >= 24) goto L17;
    L19:
        Log.m48w(TAG, "Invalid C0 command: " + r4);
        return;
    L17:
        if (r4 > 31) goto L19;
        Log.m48w(TAG, "Currently unsupported COMMAND_P16 Command: " + r4);
        this.captionChannelPacketData.skipBits(16);
    }

    private void handleC1Command(int r6) {
        switch(r6) {
            case 128: goto L62;
            case 129: goto L62;
            case 130: goto L62;
            case 131: goto L62;
            case 132: goto L62;
            case 133: goto L62;
            case 134: goto L62;
            case 135: goto L62;
            case 136: goto L55;
            case 137: goto L48;
            case 138: goto L41;
            case 139: goto L34;
            case 140: goto L27;
            case 141: goto L26;
            case 142: goto L95;
            case 143: goto L24;
            case 144: goto L21;
            case 145: goto L17;
            case 146: goto L13;
            case 147: goto L4;
            case 148: goto L4;
            case 149: goto L4;
            case 150: goto L4;
            case 151: goto L9;
            case 152: goto L5;
            case 153: goto L5;
            case 154: goto L5;
            case 155: goto L5;
            case 156: goto L5;
            case 157: goto L5;
            case 158: goto L5;
            case 159: goto L5;
            default: goto L4;
        };
    L4:
        Log.m48w(TAG, "Invalid C1 command: " + r6);
        return;
    L5:
        int r0 = r6 - 152;
        handleDefineWindow(r0);
        if (this.currentWindow == r0) goto L82;
        this.currentWindow = r0;
        this.currentCueInfoBuilder = this.cueInfoBuilders[r0];
        return;
    L82:
        return;
    L24:
        resetCueBuilders();
        return;
    L26:
        this.captionChannelPacketData.skipBits(8);
        return;
    L27:
        int r1 = 1;
    L28:
        if (r1 > 8) goto L96;
        if (this.captionChannelPacketData.readBit() == false) goto L32;
        this.cueInfoBuilders[8 - r1].reset();
    L32:
        r1 = r1 + 1;
        goto L28
    L96:
        return;
    L34:
        int r2 = 1;
    L35:
        if (r2 > 8) goto L97;
        if (this.captionChannelPacketData.readBit() == false) goto L39;
        CueInfoBuilder r3 = this.cueInfoBuilders[8 - r2];
        r3.setVisibility(!r3.isVisible());
    L39:
        r2 = r2 + 1;
        goto L35
    L97:
        return;
    L41:
        int r4 = 1;
    L42:
        if (r4 > 8) goto L98;
        if (this.captionChannelPacketData.readBit() == false) goto L46;
        this.cueInfoBuilders[8 - r4].setVisibility(false);
    L46:
        r4 = r4 + 1;
        goto L42
    L98:
        return;
    L48:
        int r5 = 1;
    L49:
        if (r5 > 8) goto L99;
        if (this.captionChannelPacketData.readBit() == false) goto L53;
        this.cueInfoBuilders[8 - r5].setVisibility(true);
    L53:
        r5 = r5 + 1;
        goto L49
    L99:
        return;
    L55:
        int r7 = 1;
    L56:
        if (r7 > 8) goto L100;
        if (this.captionChannelPacketData.readBit() == false) goto L60;
        this.cueInfoBuilders[8 - r7].clear();
    L60:
        r7 = r7 + 1;
        goto L56
    L100:
        return;
    L62:
        int r8 = r6 - 128;
        if (this.currentWindow == r8) goto L94;
        this.currentWindow = r8;
        this.currentCueInfoBuilder = this.cueInfoBuilders[r8];
        return;
    L94:
        return;
    L95:
        return;
    L9:
        if (this.currentCueInfoBuilder.isDefined() == true) goto L11;
        this.captionChannelPacketData.skipBits(32);
        return;
    L11:
        handleSetWindowAttributes();
        return;
    L13:
        if (this.currentCueInfoBuilder.isDefined() == true) goto L15;
        this.captionChannelPacketData.skipBits(16);
        return;
    L15:
        handleSetPenLocation();
        return;
    L17:
        if (this.currentCueInfoBuilder.isDefined() == true) goto L19;
        this.captionChannelPacketData.skipBits(24);
        return;
    L19:
        handleSetPenColor();
        return;
    L21:
        if (this.currentCueInfoBuilder.isDefined() == true) goto L23;
        this.captionChannelPacketData.skipBits(16);
        return;
    L23:
        handleSetPenAttributes();
    }

    private void handleC2Command(int r3) {
        if (r3 > 7) goto L6;
        return;
    L6:
        if (r3 > 15) goto L9;
        this.captionChannelPacketData.skipBits(8);
        return;
    L9:
        if (r3 > 23) goto L12;
        this.captionChannelPacketData.skipBits(16);
        return;
    L12:
        if (r3 > 31) goto L17;
        this.captionChannelPacketData.skipBits(24);
        return;
    }

    private void handleC3Command(int r4) {
        if (r4 > 135) goto L6;
        this.captionChannelPacketData.skipBits(32);
        return;
    L6:
        if (r4 > COMMAND_RST) goto L9;
        this.captionChannelPacketData.skipBits(40);
        return;
    L9:
        if (r4 > 159) goto L14;
        this.captionChannelPacketData.skipBits(2);
        int r0 = this.captionChannelPacketData.readBits(6);
        this.captionChannelPacketData.skipBits(r0 * 8);
        return;
    }

    private void handleDefineWindow(int r13) {
        CueInfoBuilder r1 = this.cueInfoBuilders[r13];
        this.captionChannelPacketData.skipBits(2);
        boolean r0 = this.captionChannelPacketData.readBit();
        this.captionChannelPacketData.skipBits(2);
        int r3 = this.captionChannelPacketData.readBits(3);
        boolean r5 = this.captionChannelPacketData.readBit();
        int r6 = this.captionChannelPacketData.readBits(7);
        int r7 = this.captionChannelPacketData.readBits(8);
        int r8 = this.captionChannelPacketData.readBits(4);
        int r9 = this.captionChannelPacketData.readBits(4);
        this.captionChannelPacketData.skipBits(2);
        this.captionChannelPacketData.skipBits(6);
        this.captionChannelPacketData.skipBits(2);
        r1.defineWindow(r0, r3, r5, r6, r7, r9, r8, this.captionChannelPacketData.readBits(3), this.captionChannelPacketData.readBits(3));
    }

    private void handleG0Character(int r3) {
        if (r3 != 127) goto L5;
        this.currentCueInfoBuilder.append(9835);
        return;
    L5:
        this.currentCueInfoBuilder.append((char) (r3 & 255));
    }

    private void handleG1Character(int r3) {
        this.currentCueInfoBuilder.append((char) (r3 & 255));
    }

    private void handleG2Character(int r3) {
        switch(r3) {
            case 32: goto L29;
            case 33: goto L28;
            case 37: goto L27;
            case 42: goto L26;
            case 44: goto L25;
            case 48: goto L24;
            case 49: goto L23;
            case 50: goto L22;
            case 51: goto L21;
            case 52: goto L20;
            case 53: goto L19;
            case 57: goto L18;
            case 58: goto L17;
            case 60: goto L16;
            case 61: goto L15;
            case 63: goto L14;
            case 118: goto L13;
            case 119: goto L12;
            case 120: goto L11;
            case 121: goto L10;
            case 122: goto L9;
            case 123: goto L8;
            case 124: goto L7;
            case 125: goto L6;
            case 126: goto L5;
            case 127: goto L4;
            default: goto L3;
        };
    L3:
        Log.m48w(TAG, "Invalid G2 character: " + r3);
        return;
    L4:
        this.currentCueInfoBuilder.append(9484);
        return;
    L5:
        this.currentCueInfoBuilder.append(9496);
        return;
    L6:
        this.currentCueInfoBuilder.append(9472);
        return;
    L7:
        this.currentCueInfoBuilder.append(9492);
        return;
    L8:
        this.currentCueInfoBuilder.append(9488);
        return;
    L9:
        this.currentCueInfoBuilder.append(9474);
        return;
    L10:
        this.currentCueInfoBuilder.append(8542);
        return;
    L11:
        this.currentCueInfoBuilder.append(8541);
        return;
    L12:
        this.currentCueInfoBuilder.append(8540);
        return;
    L13:
        this.currentCueInfoBuilder.append(8539);
        return;
    L14:
        this.currentCueInfoBuilder.append(376);
        return;
    L15:
        this.currentCueInfoBuilder.append(8480);
        return;
    L16:
        this.currentCueInfoBuilder.append(339);
        return;
    L17:
        this.currentCueInfoBuilder.append(353);
        return;
    L18:
        this.currentCueInfoBuilder.append(Typography.f202tm);
        return;
    L19:
        this.currentCueInfoBuilder.append(Typography.bullet);
        return;
    L20:
        this.currentCueInfoBuilder.append(Typography.rightDoubleQuote);
        return;
    L21:
        this.currentCueInfoBuilder.append(Typography.leftDoubleQuote);
        return;
    L22:
        this.currentCueInfoBuilder.append(Typography.rightSingleQuote);
        return;
    L23:
        this.currentCueInfoBuilder.append(Typography.leftSingleQuote);
        return;
    L24:
        this.currentCueInfoBuilder.append(9608);
        return;
    L25:
        this.currentCueInfoBuilder.append(338);
        return;
    L26:
        this.currentCueInfoBuilder.append(352);
        return;
    L27:
        this.currentCueInfoBuilder.append(Typography.ellipsis);
        return;
    L28:
        this.currentCueInfoBuilder.append(Typography.nbsp);
        return;
    L29:
        this.currentCueInfoBuilder.append(' ');
    }

    private void handleG3Character(int r3) {
        if (r3 != 160) goto L5;
        this.currentCueInfoBuilder.append(13252);
        return;
    L5:
        Log.m48w(TAG, "Invalid G3 character: " + r3);
        this.currentCueInfoBuilder.append('_');
    }

    private void handleSetPenAttributes() {
        int r3 = this.captionChannelPacketData.readBits(4);
        int r4 = this.captionChannelPacketData.readBits(2);
        int r5 = this.captionChannelPacketData.readBits(2);
        boolean r6 = this.captionChannelPacketData.readBit();
        boolean r7 = this.captionChannelPacketData.readBit();
        int r8 = this.captionChannelPacketData.readBits(3);
        int r9 = this.captionChannelPacketData.readBits(3);
        this.currentCueInfoBuilder.setPenAttributes(r3, r4, r5, r6, r7, r8, r9);
    }

    private void handleSetPenColor() {
        int r0 = this.captionChannelPacketData.readBits(2);
        int r5 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), r0);
        int r6 = this.captionChannelPacketData.readBits(2);
        int r10 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), r6);
        this.captionChannelPacketData.skipBits(2);
        int r13 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2));
        this.currentCueInfoBuilder.setPenColor(r5, r10, r13);
    }

    private void handleSetPenLocation() {
        this.captionChannelPacketData.skipBits(4);
        int r0 = this.captionChannelPacketData.readBits(4);
        this.captionChannelPacketData.skipBits(2);
        int r1 = this.captionChannelPacketData.readBits(6);
        this.currentCueInfoBuilder.setPenLocation(r0, r1);
    }

    private void handleSetWindowAttributes() {
        int r1 = this.captionChannelPacketData.readBits(2);
        int r7 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), r1);
        int r6 = this.captionChannelPacketData.readBits(2);
        int r8 = CueInfoBuilder.getArgbColorFromCeaColor(this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2), this.captionChannelPacketData.readBits(2));
        if (this.captionChannelPacketData.readBit() == false) goto L5;
        int r10 = r6 | 4;
    L6:
        boolean r9 = this.captionChannelPacketData.readBit();
        int r11 = this.captionChannelPacketData.readBits(2);
        int r12 = this.captionChannelPacketData.readBits(2);
        int r13 = this.captionChannelPacketData.readBits(2);
        this.captionChannelPacketData.skipBits(8);
        this.currentCueInfoBuilder.setWindowAttributes(r7, r8, r9, r10, r11, r12, r13);
        return;
    L5:
        r10 = r6;
        goto L6
    }

    @RequiresNonNull({"currentDtvCcPacket"})
    private void processCurrentPacket() {
        if (this.currentDtvCcPacket.currentIndex == ((this.currentDtvCcPacket.packetSize * 2) - 1)) goto L5;
        Log.m42d(TAG, "DtvCcPacket ended prematurely; size is " + ((this.currentDtvCcPacket.packetSize * 2) - 1) + ", but current index is " + this.currentDtvCcPacket.currentIndex + " (sequence number " + this.currentDtvCcPacket.sequenceNumber + ");");
    L5:
        boolean r0 = false;
        this.captionChannelPacketData.reset(this.currentDtvCcPacket.packetData, this.currentDtvCcPacket.currentIndex);
    L7:
        if (this.captionChannelPacketData.bitsLeft() <= 0) goto L45;
        int r1 = this.captionChannelPacketData.readBits(3);
        int r4 = this.captionChannelPacketData.readBits(5);
        if (r1 != 7) goto L13;
        this.captionChannelPacketData.skipBits(2);
        r1 = this.captionChannelPacketData.readBits(6);
        if (r1 >= 7) goto L13;
        Log.m48w(TAG, "Invalid extended service number: " + r1);
    L13:
        if (r4 == 0) goto L14;
        if (r1 != this.selectedServiceNumber) goto L18;
        int r5 = this.captionChannelPacketData.getPosition() + (r4 * 8);
    L21:
        if (this.captionChannelPacketData.getPosition() >= r5) goto L7;
        int r6 = this.captionChannelPacketData.readBits(8);
        if (r6 != 16) goto L24;
        int r7 = this.captionChannelPacketData.readBits(8);
        if (r7 <= 31) goto L35;
        if (r7 <= 127) goto L37;
        if (r7 <= 159) goto L39;
        if (r7 <= 255) goto L41;
        Log.m48w(TAG, "Invalid extended command: " + r7);
        goto L21
    L41:
        handleG3Character(r7);
        r0 = true;
        goto L21
    L39:
        handleC3Command(r7);
        goto L21
    L37:
        handleG2Character(r7);
        r0 = true;
        goto L21
    L35:
        handleC2Command(r7);
        goto L21
    L24:
        if (r6 <= 31) goto L25;
        if (r6 <= 127) goto L27;
        if (r6 <= 159) goto L29;
        if (r6 <= 255) goto L31;
        Log.m48w(TAG, "Invalid base command: " + r6);
        goto L21
    L31:
        handleG1Character(r6);
        r0 = true;
        goto L21
    L29:
        handleC1Command(r6);
        r0 = true;
        goto L21
    L27:
        handleG0Character(r6);
        r0 = true;
        goto L21
    L25:
        handleC0Command(r6);
        goto L21
    L18:
        this.captionChannelPacketData.skipBytes(r4);
        goto L7
    L14:
        if (r1 == 0) goto L45;
        Log.m48w(TAG, "serviceNumber is non-zero (" + r1 + ") when blockSize is 0");
    L45:
        if (r0 == false) goto L84;
        this.cues = getDisplayCues();
        return;
    }

    private void resetCueBuilders() {
        int r0 = 0;
    L4:
        if (r0 >= 8) goto L6;
        this.cueInfoBuilders[r0].reset();
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder
    protected Subtitle createSubtitle() {
        this.lastCues = this.cues;
        return new CeaSubtitle((List) Assertions.checkNotNull(this.cues));
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder
    protected void decode(SubtitleInputBuffer r18) {
        ByteBuffer r2 = (ByteBuffer) Assertions.checkNotNull(r18.data);
        this.ccData.reset(r2.array(), r2.limit());
    L4:
        if (this.ccData.bytesLeft() < 3) goto L37;
        int r4 = this.ccData.readUnsignedByte() & 7;
        int r6 = r4 & 3;
        boolean r8 = false;
        if ((r4 & 4) != 4) goto L8;
        boolean r7 = true;
    L9:
        byte r11 = (byte) this.ccData.readUnsignedByte();
        byte r12 = (byte) this.ccData.readUnsignedByte();
        if (r6 == 2) goto L13;
        if (r6 != 3) goto L4;
    L13:
        if (r7 == false) goto L4;
        if (r6 == 3) goto L17;
        if (r6 != 2) goto L29;
        r8 = true;
    L29:
        Assertions.checkArgument(r8);
        if (this.currentDtvCcPacket != null) goto L32;
        Log.m44e(TAG, "Encountered DTVCC_PACKET_DATA before DTVCC_PACKET_START");
        goto L4
    L32:
        byte[] r5 = this.currentDtvCcPacket.packetData;
        DtvCcPacket r9 = this.currentDtvCcPacket;
        int r10 = r9.currentIndex;
        r9.currentIndex = r10 + 1;
        r5[r10] = r11;
        byte[] r13 = this.currentDtvCcPacket.packetData;
        DtvCcPacket r14 = this.currentDtvCcPacket;
        int r15 = r14.currentIndex;
        r14.currentIndex = r15 + 1;
        r13[r15] = r12;
    L34:
        if (this.currentDtvCcPacket.currentIndex != ((this.currentDtvCcPacket.packetSize * 2) - 1)) goto L4;
        finalizeCurrentPacket();
        goto L4
    L17:
        finalizeCurrentPacket();
        int r16 = (r11 & 192) >> 6;
        if (this.previousSequenceNumber != (-1)) goto L20;
    L22:
        this.previousSequenceNumber = r16;
        int r17 = r11 & 63;
        if (r17 != 0) goto L25;
        r17 = 64;
    L25:
        this.currentDtvCcPacket = new DtvCcPacket(r16, r17);
        byte[] r19 = this.currentDtvCcPacket.packetData;
        DtvCcPacket r110 = this.currentDtvCcPacket;
        int r111 = r110.currentIndex;
        r110.currentIndex = r111 + 1;
        r19[r111] = r12;
        goto L34
    L20:
        if (r16 == ((this.previousSequenceNumber + 1) % 4)) goto L22;
        resetCueBuilders();
        Log.m48w(TAG, "Sequence number discontinuity. previous=" + this.previousSequenceNumber + " current=" + r16);
        goto L22
    L8:
        r7 = false;
        goto L9
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        return super.dequeueInputBuffer();
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        return super.dequeueOutputBuffer();
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        this.currentWindow = 0;
        this.currentCueInfoBuilder = this.cueInfoBuilders[this.currentWindow];
        resetCueBuilders();
        this.currentDtvCcPacket = null;
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public String getName() {
        return TAG;
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder
    protected boolean isNewSubtitleDataAvailable() {
        if (this.cues == this.lastCues) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder
    public /* bridge */ /* synthetic */ void queueInputBuffer(SubtitleInputBuffer r1) throws SubtitleDecoderException {
        super.queueInputBuffer(r1);
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.extractor.text.SubtitleDecoder
    public /* bridge */ /* synthetic */ void setPositionUs(long r1) {
        super.setPositionUs(r1);
    }
}
