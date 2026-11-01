package androidx.media3.extractor.text.cea;

import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import androidx.core.internal.view.SupportMenu;
import androidx.core.location.LocationRequestCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.decoder.DecoderException;
import androidx.media3.extractor.metadata.dvbsi.AppInfoTableDecoder;
import androidx.media3.extractor.p001ts.PsExtractor;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleDecoderException;
import androidx.media3.extractor.text.SubtitleInputBuffer;
import androidx.media3.extractor.text.SubtitleOutputBuffer;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.common.base.Ascii;
import es.antonio.m3u.BuildConfig;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Cea608Decoder extends CeaDecoder {
    private static final int[] BASIC_CHARACTER_SET = null;
    private static final int CC_FIELD_FLAG = 1;
    private static final byte CC_IMPLICIT_DATA_HEADER = -4;
    private static final int CC_MODE_PAINT_ON = 3;
    private static final int CC_MODE_POP_ON = 2;
    private static final int CC_MODE_ROLL_UP = 1;
    private static final int CC_MODE_UNKNOWN = 0;
    private static final int CC_TYPE_FLAG = 2;
    private static final int CC_VALID_FLAG = 4;
    private static final int[] COLUMN_INDICES = null;
    private static final byte CTRL_BACKSPACE = 33;
    private static final byte CTRL_CARRIAGE_RETURN = 45;
    private static final byte CTRL_DELETE_TO_END_OF_ROW = 36;
    private static final byte CTRL_END_OF_CAPTION = 47;
    private static final byte CTRL_ERASE_DISPLAYED_MEMORY = 44;
    private static final byte CTRL_ERASE_NON_DISPLAYED_MEMORY = 46;
    private static final byte CTRL_RESUME_CAPTION_LOADING = 32;
    private static final byte CTRL_RESUME_DIRECT_CAPTIONING = 41;
    private static final byte CTRL_RESUME_TEXT_DISPLAY = 43;
    private static final byte CTRL_ROLL_UP_CAPTIONS_2_ROWS = 37;
    private static final byte CTRL_ROLL_UP_CAPTIONS_3_ROWS = 38;
    private static final byte CTRL_ROLL_UP_CAPTIONS_4_ROWS = 39;
    private static final byte CTRL_TEXT_RESTART = 42;
    private static final int DEFAULT_CAPTIONS_ROW_COUNT = 4;
    public static final long MIN_DATA_CHANNEL_TIMEOUT_MS = 16000;
    private static final int NTSC_CC_CHANNEL_1 = 0;
    private static final int NTSC_CC_CHANNEL_2 = 1;
    private static final int NTSC_CC_FIELD_1 = 0;
    private static final int NTSC_CC_FIELD_2 = 1;
    private static final boolean[] ODD_PARITY_BYTE_TABLE = null;
    private static final int[] ROW_INDICES = null;
    private static final int[] SPECIAL_CHARACTER_SET = null;
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET = null;
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET = null;
    private static final int[] STYLE_COLORS = null;
    private static final int STYLE_ITALICS = 7;
    private static final int STYLE_UNCHANGED = 8;
    private static final String TAG = "Cea608Decoder";
    private int captionMode;
    private int captionRowCount;
    private final ParsableByteArray ccData;
    private final ArrayList<CueBuilder> cueBuilders;
    private List<Cue> cues;
    private int currentChannel;
    private CueBuilder currentCueBuilder;
    private boolean isCaptionValid;
    private boolean isInCaptionService;
    private long lastCueUpdateUs;
    private List<Cue> lastCues;
    private final int packetLength;
    private byte repeatableControlCc1;
    private byte repeatableControlCc2;
    private boolean repeatableControlSet;
    private final int selectedChannel;
    private final int selectedField;
    private final long validDataChannelTimeoutUs;

    private static final class CueBuilder {
        private static final int BASE_ROW = 15;
        private static final int SCREEN_CHARWIDTH = 32;
        private int captionMode;
        private int captionRowCount;
        private final StringBuilder captionStringBuilder;
        private final List<CueStyle> cueStyles;
        private int indent;
        private final List<SpannableString> rolledUpCaptions;
        private int row;
        private int tabOffset;

        private static class CueStyle {
            public int start;
            public final int style;
            public final boolean underline;

            public CueStyle(int r1, boolean r2, int r3) {
                this.style = r1;
                this.underline = r2;
                this.start = r3;
            }
        }

        public CueBuilder(int r2, int r3) {
            this.cueStyles = new ArrayList();
            this.rolledUpCaptions = new ArrayList();
            this.captionStringBuilder = new StringBuilder();
            reset(r2);
            this.captionRowCount = r3;
        }

        static /* synthetic */ int access$002(CueBuilder r0, int r1) {
            r0.tabOffset = r1;
            return r1;
        }

        static /* synthetic */ int access$100(CueBuilder r1) {
            return r1.row;
        }

        static /* synthetic */ int access$102(CueBuilder r0, int r1) {
            r0.row = r1;
            return r1;
        }

        static /* synthetic */ int access$202(CueBuilder r0, int r1) {
            r0.indent = r1;
            return r1;
        }

        private SpannableString buildCurrentLine() {
            SpannableStringBuilder r1 = new SpannableStringBuilder(this.captionStringBuilder);
            int r2 = r1.length();
            int r3 = -1;
            int r4 = -1;
            int r5 = 0;
            int r6 = -1;
            boolean r7 = false;
            int r8 = -1;
            int r9 = 0;
        L4:
            if (r9 >= this.cueStyles.size()) goto L41;
            CueStyle r10 = this.cueStyles.get(r9);
            boolean r12 = r10.underline;
            int r13 = r10.style;
            if (r13 != 8) goto L8;
        L15:
            int r14 = r10.start;
            if ((r9 + 1) >= this.cueStyles.size()) goto L18;
            int r11 = this.cueStyles.get(r9 + 1).start;
        L19:
            if (r14 == r11) goto L39;
            if (r3 == (-1)) goto L26;
            if (r12 == true) goto L26;
            setUnderlineSpan(r1, r3, r14);
            r3 = -1;
        L30:
            if (r4 == (-1)) goto L34;
            if (r7 == true) goto L34;
            setItalicSpan(r1, r4, r14);
            r4 = -1;
        L37:
            if (r8 == r6) goto L39;
            setColorSpan(r1, r5, r14, r6);
            r6 = r8;
            r5 = r14;
        L34:
            if (r4 != (-1)) goto L37;
            if (r7 == false) goto L37;
            r4 = r14;
        L26:
            if (r3 != (-1)) goto L30;
            if (r12 == false) goto L30;
            r3 = r14;
        L39:
            r9 = r9 + 1;
            goto L4
        L18:
            r11 = r2;
            goto L19
        L8:
            if (r13 != 7) goto L10;
            boolean r15 = true;
        L11:
            if (r13 != 7) goto L13;
            int r16 = r8;
        L14:
            r8 = r16;
            r7 = r15;
            goto L15
        L13:
            r16 = Cea608Decoder.access$300()[r13];
            goto L14
        L10:
            r15 = false;
            goto L11
        L41:
            if (r3 == (-1)) goto L44;
            if (r3 == r2) goto L44;
            setUnderlineSpan(r1, r3, r2);
        L44:
            if (r4 == (-1)) goto L47;
            if (r4 == r2) goto L47;
            setItalicSpan(r1, r4, r2);
        L47:
            if (r5 == r2) goto L50;
            setColorSpan(r1, r5, r2, r6);
        L50:
            return new SpannableString(r1);
        }

        private static void setColorSpan(SpannableStringBuilder r2, int r3, int r4, int r5) {
            if (r5 != (-1)) goto L5;
            return;
        L5:
            r2.setSpan(new ForegroundColorSpan(r5), r3, r4, 33);
        }

        private static void setItalicSpan(SpannableStringBuilder r2, int r3, int r4) {
            r2.setSpan(new StyleSpan(2), r3, r4, 33);
        }

        private static void setUnderlineSpan(SpannableStringBuilder r2, int r3, int r4) {
            r2.setSpan(new UnderlineSpan(), r3, r4, 33);
        }

        public void append(char r3) {
            if (this.captionStringBuilder.length() >= 32) goto L6;
            this.captionStringBuilder.append(r3);
            return;
        }

        public void backspace() {
            int r0 = this.captionStringBuilder.length();
            if (r0 <= 0) goto L9;
            this.captionStringBuilder.delete(r0 - 1, r0);
            int r1 = this.cueStyles.size() - 1;
        L5:
            if (r1 < 0) goto L12;
            CueStyle r2 = this.cueStyles.get(r1);
            if (r2.start != r0) goto L13;
            r2.start--;
            r1 = r1 - 1;
            goto L5
        L13:
            return;
        L12:
            return;
        }

        public Cue build(int r11) {
            SpannableStringBuilder r0 = new SpannableStringBuilder();
            int r1 = 0;
        L4:
            if (r1 >= this.rolledUpCaptions.size()) goto L6;
            r0.append(this.rolledUpCaptions.get(r1));
            r0.append('\n');
            r1 = r1 + 1;
            goto L4
        L6:
            r0.append(buildCurrentLine());
            if (r0.length() != 0) goto L10;
            return null;
        L10:
            int r2 = this.indent + this.tabOffset;
            int r3 = (32 - r2) - r0.length();
            int r4 = r2 - r3;
            if (r11 == Integer.MIN_VALUE) goto L14;
            int r5 = r11;
        L25:
            switch(r5) {
                case 1: goto L28;
                case 2: goto L27;
                default: goto L26;
            };
        L26:
            float r6 = (0.8f * (r2 / 32.0f)) + 0.1f;
        L30:
            if (this.row <= 7) goto L33;
            int r7 = (this.row - 15) - 2;
        L37:
            return new Cue.Builder().setText(r0).setTextAlignment(Layout.Alignment.ALIGN_NORMAL).setLine(r7, 1).setPosition(r6).setPositionAnchor(r5).build();
        L33:
            if (this.captionMode != 1) goto L35;
            r7 = this.row - (this.captionRowCount - 1);
            goto L37
        L35:
            r7 = this.row;
            goto L37
        L27:
            r6 = (0.8f * ((32 - r3) / 32.0f)) + 0.1f;
            goto L30
        L28:
            r6 = 0.5f;
            goto L30
        L14:
            if (this.captionMode != 2) goto L20;
            if (Math.abs(r4) < 3) goto L18;
            if (r3 >= 0) goto L20;
        L18:
            r5 = 1;
        L20:
            if (this.captionMode != 2) goto L23;
            if (r4 <= 0) goto L23;
            r5 = 2;
        L23:
            r5 = 0;
            goto L25
        }

        public boolean isEmpty() {
            if (this.cueStyles.isEmpty() == true) goto L5;
        L9:
            return false;
        L5:
            if (this.rolledUpCaptions.isEmpty() == false) goto L9;
            if (this.captionStringBuilder.length() != 0) goto L9;
            return true;
        }

        public void reset(int r3) {
            this.captionMode = r3;
            this.cueStyles.clear();
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.setLength(0);
            this.row = 15;
            this.indent = 0;
            this.tabOffset = 0;
        }

        public void rollUp() {
            this.rolledUpCaptions.add(buildCurrentLine());
            this.captionStringBuilder.setLength(0);
            this.cueStyles.clear();
            int r0 = Math.min(this.captionRowCount, this.row);
        L4:
            if (this.rolledUpCaptions.size() < r0) goto L6;
            this.rolledUpCaptions.remove(0);
            goto L4
        }

        public void setCaptionMode(int r1) {
            this.captionMode = r1;
        }

        public void setCaptionRowCount(int r1) {
            this.captionRowCount = r1;
        }

        public void setStyle(int r4, boolean r5) {
            this.cueStyles.add(new CueStyle(r4, r5, this.captionStringBuilder.length()));
        }
    }

    static {
        ROW_INDICES = new int[]{11, 1, 3, 12, 14, 5, 7, 9};
        COLUMN_INDICES = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
        STYLE_COLORS = new int[]{-1, -16711936, -16776961, -16711681, SupportMenu.CATEGORY_MASK, InputDeviceCompat.SOURCE_ANY, -65281};
        BASIC_CHARACTER_SET = new int[]{32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 225, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, BuildConfig.VERSION_CODE, 93, 237, 243, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, 97, 98, 99, 100, 101, LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY, 103, LocationRequestCompat.QUALITY_LOW_POWER, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, AppInfoTableDecoder.APPLICATION_INFORMATION_TABLE_ID, 117, 118, 119, 120, 121, 122, 231, 247, 209, 241, 9632};
        SPECIAL_CHARACTER_SET = new int[]{174, 176, PsExtractor.PRIVATE_STREAM_1, 191, 8482, 162, 163, 9834, 224, 32, 232, 226, 234, 238, 244, 251};
        SPECIAL_ES_FR_CHARACTER_SET = new int[]{193, 201, 211, 218, 220, 252, 8216, 161, 42, 39, 8212, 169, 8480, 8226, 8220, 8221, PsExtractor.AUDIO_STREAM, 194, 199, 200, 202, 203, 235, 206, 207, 239, 212, 217, 249, 219, 171, 187};
        SPECIAL_PT_DE_CHARACTER_SET = new int[]{195, 227, 205, 204, 236, 210, 242, 213, 245, 123, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, 165, 164, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496};
        ODD_PARITY_BYTE_TABLE = new boolean[]{false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};
    }

    public Cea608Decoder(String r8, int r9, long r10) {
        this.ccData = new ParsableByteArray();
        this.cueBuilders = new ArrayList();
        this.currentCueBuilder = new CueBuilder(0, 4);
        this.currentChannel = 0;
        if (r10 != C0180C.TIME_UNSET) goto L5;
        this.validDataChannelTimeoutUs = C0180C.TIME_UNSET;
    L11:
        if (MimeTypes.APPLICATION_MP4CEA608.equals(r8) == false) goto L13;
        int r3 = 2;
    L14:
        this.packetLength = r3;
        switch(r9) {
            case 1: goto L20;
            case 2: goto L19;
            case 3: goto L18;
            case 4: goto L17;
            default: goto L16;
        };
    L16:
        Log.m48w(TAG, "Invalid channel. Defaulting to CC1.");
        this.selectedChannel = 0;
        this.selectedField = 0;
    L21:
        setCaptionMode(0);
        resetCueBuilders();
        this.isInCaptionService = true;
        this.lastCueUpdateUs = C0180C.TIME_UNSET;
        return;
    L17:
        this.selectedChannel = 1;
        this.selectedField = 1;
        goto L21
    L18:
        this.selectedChannel = 0;
        this.selectedField = 1;
        goto L21
    L19:
        this.selectedChannel = 1;
        this.selectedField = 0;
        goto L21
    L20:
        this.selectedChannel = 0;
        this.selectedField = 0;
        goto L21
    L13:
        r3 = 3;
        goto L14
    L5:
        if (r10 < MIN_DATA_CHANNEL_TIMEOUT_MS) goto L7;
        boolean r4 = true;
    L8:
        Assertions.checkArgument(r4);
        this.validDataChannelTimeoutUs = 1000 * r10;
        goto L11
    L7:
        r4 = false;
        goto L8
    }

    static /* synthetic */ int[] access$300() {
        return STYLE_COLORS;
    }

    private static char getBasicChar(byte r2) {
        return (char) BASIC_CHARACTER_SET[(r2 & 127) - 32];
    }

    private static int getChannel(byte r1) {
        return (r1 >> 3) & 1;
    }

    private List<Cue> getDisplayCues() {
        int r0 = 2;
        int r1 = this.cueBuilders.size();
        ArrayList r2 = new ArrayList(r1);
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L8;
        Cue r4 = this.cueBuilders.get(r3).build(Integer.MIN_VALUE);
        r2.add(r4);
        if (r4 == null) goto L7;
        r0 = Math.min(r0, r4.positionAnchor);
    L7:
        r3 = r3 + 1;
        goto L3
    L8:
        ArrayList r5 = new ArrayList(r1);
        int r6 = 0;
    L9:
        if (r6 >= r1) goto L17;
        Cue r7 = (Cue) r2.get(r6);
        if (r7 == null) goto L16;
        if (r7.positionAnchor == r0) goto L15;
        r7 = (Cue) Assertions.checkNotNull(this.cueBuilders.get(r6).build(r0));
    L15:
        r5.add(r7);
    L16:
        r6 = r6 + 1;
        goto L9
    L17:
        return r5;
    }

    private static char getExtendedEsFrChar(byte r2) {
        int r0 = r2 & Ascii.f91US;
        return (char) SPECIAL_ES_FR_CHARACTER_SET[r0];
    }

    private static char getExtendedPtDeChar(byte r2) {
        int r0 = r2 & Ascii.f91US;
        return (char) SPECIAL_PT_DE_CHARACTER_SET[r0];
    }

    private static char getExtendedWestEuropeanChar(byte r1, byte r2) {
        if ((r1 & 1) != 0) goto L7;
        return getExtendedEsFrChar(r2);
    L7:
        return getExtendedPtDeChar(r2);
    }

    private static char getSpecialNorthAmericanChar(byte r2) {
        int r0 = r2 & Ascii.f88SI;
        return (char) SPECIAL_CHARACTER_SET[r0];
    }

    private void handleMidrowCtrl(byte r4) {
        this.currentCueBuilder.append(' ');
        boolean r1 = true;
        if ((r4 & 1) == 1) goto L6;
        r1 = false;
    L6:
        CueBuilder r2 = this.currentCueBuilder;
        r2.setStyle((r4 >> 1) & 7, r1);
    }

    private void handleMiscCode(byte r4) {
        switch(r4) {
            case 32: goto L15;
            case 37: goto L13;
            case 38: goto L11;
            case 39: goto L9;
            case 41: goto L7;
            default: goto L5;
        };
    L7:
        setCaptionMode(3);
        return;
    L9:
        setCaptionMode(1);
        setCaptionRowCount(4);
        return;
    L11:
        setCaptionMode(1);
        setCaptionRowCount(3);
        return;
    L13:
        setCaptionMode(1);
        setCaptionRowCount(2);
        return;
    L15:
        setCaptionMode(2);
        return;
    L5:
        if (this.captionMode != 0) goto L17;
        return;
    L17:
        switch(r4) {
            case 33: goto L32;
            case 36: goto L41;
            case 44: goto L26;
            case 45: goto L22;
            case 46: goto L20;
            case 47: goto L19;
            default: goto L41;
        };
    L19:
        this.cues = getDisplayCues();
        resetCueBuilders();
        return;
    L20:
        resetCueBuilders();
        return;
    L26:
        this.cues = Collections.emptyList();
        if (this.captionMode != 1) goto L29;
    L30:
        resetCueBuilders();
        return;
    L29:
        if (this.captionMode == 3) goto L30;
        return;
    L32:
        this.currentCueBuilder.backspace();
        return;
    L41:
        return;
    L22:
        if (this.captionMode == 1) goto L24;
        return;
    L24:
        if (this.currentCueBuilder.isEmpty() == true) goto L37;
        this.currentCueBuilder.rollUp();
        return;
    }

    private void handlePreambleAddressCode(byte r8, byte r9) {
        int r0 = ROW_INDICES[r8 & 7];
        boolean r2 = false;
        if ((r9 & 32) == 0) goto L5;
        boolean r1 = true;
    L6:
        if (r1 == false) goto L9;
        r0 = r0 + 1;
    L9:
        if (r0 == CueBuilder.access$100(this.currentCueBuilder)) goto L17;
        if (this.captionMode != 1) goto L13;
    L15:
        CueBuilder.access$102(this.currentCueBuilder, r0);
        goto L17
    L13:
        if (this.currentCueBuilder.isEmpty() == true) goto L15;
        this.currentCueBuilder = new CueBuilder(this.captionMode, this.captionRowCount);
        this.cueBuilders.add(this.currentCueBuilder);
    L17:
        if ((r9 & Ascii.DLE) != 16) goto L19;
        boolean r4 = true;
    L21:
        if ((r9 & 1) != 1) goto L23;
        r2 = true;
    L23:
        int r3 = (r9 >> 1) & 7;
        CueBuilder r5 = this.currentCueBuilder;
        if (r4 == false) goto L26;
        int r6 = 8;
    L27:
        r5.setStyle(r6, r2);
        if (r4 == false) goto L31;
        CueBuilder.access$202(this.currentCueBuilder, COLUMN_INDICES[r3]);
        return;
    L31:
        return;
    L26:
        r6 = r3;
        goto L27
    L19:
        r4 = false;
        goto L21
    L5:
        r1 = false;
        goto L6
    }

    private static boolean isCtrlCode(byte r1) {
        if ((r1 & 224) != 0) goto L5;
        return true;
    L5:
        return false;
    }

    private static boolean isExtendedWestEuropeanChar(byte r2, byte r3) {
        if ((r2 & 246) == 18) goto L5;
    L7:
        return false;
    L5:
        if ((r3 & 224) != 32) goto L7;
        return true;
    }

    private static boolean isMidrowCtrlCode(byte r2, byte r3) {
        if ((r2 & 247) == 17) goto L5;
    L7:
        return false;
    L5:
        if ((r3 & 240) != 32) goto L7;
        return true;
    }

    private static boolean isMiscCode(byte r2, byte r3) {
        if ((r2 & 246) == 20) goto L5;
    L7:
        return false;
    L5:
        if ((r3 & 240) != 32) goto L7;
        return true;
    }

    private static boolean isPreambleAddressCode(byte r2, byte r3) {
        if ((r2 & 240) == 16) goto L5;
    L7:
        return false;
    L5:
        if ((r3 & 192) != 64) goto L7;
        return true;
    }

    private static boolean isRepeatable(byte r2) {
        if ((r2 & 240) != 16) goto L5;
        return true;
    L5:
        return false;
    }

    private boolean isRepeatedCommand(boolean r4, byte r5, byte r6) {
        if (r4 == true) goto L5;
    L15:
        this.repeatableControlSet = false;
    L16:
        return false;
    L5:
        if (isRepeatable(r5) == false) goto L15;
        if (this.repeatableControlSet == true) goto L9;
    L14:
        this.repeatableControlSet = true;
        this.repeatableControlCc1 = r5;
        this.repeatableControlCc2 = r6;
        goto L16
    L9:
        if (this.repeatableControlCc1 != r5) goto L14;
        if (this.repeatableControlCc2 != r6) goto L14;
        this.repeatableControlSet = false;
        return true;
    }

    private static boolean isServiceSwitchCommand(byte r2) {
        if ((r2 & 246) != 20) goto L5;
        return true;
    L5:
        return false;
    }

    private static boolean isSpecialNorthAmericanChar(byte r2, byte r3) {
        if ((r2 & 247) == 17) goto L5;
    L7:
        return false;
    L5:
        if ((r3 & 240) != 48) goto L7;
        return true;
    }

    private static boolean isTabCtrlCode(byte r2, byte r3) {
        if ((r2 & 247) == 23) goto L5;
    L9:
        return false;
    L5:
        if (r3 < 33) goto L9;
        if (r3 > 35) goto L9;
        return true;
    }

    private static boolean isXdsControlCode(byte r2) {
        if (1 <= r2) goto L5;
    L7:
        return false;
    L5:
        if (r2 > 15) goto L7;
        return true;
    }

    private void maybeUpdateIsInCaptionService(byte r3, byte r4) {
        if (isXdsControlCode(r3) == false) goto L6;
        this.isInCaptionService = false;
        return;
    L6:
        if (isServiceSwitchCommand(r3) == false) goto L13;
        switch(r4) {
            case 32: goto L10;
            case 37: goto L10;
            case 38: goto L10;
            case 39: goto L10;
            case 41: goto L10;
            case 42: goto L9;
            case 43: goto L9;
            case 47: goto L10;
            default: goto L15;
        };
    L9:
        this.isInCaptionService = false;
        return;
    L10:
        this.isInCaptionService = true;
        return;
    L15:
        return;
    }

    private void resetCueBuilders() {
        this.currentCueBuilder.reset(this.captionMode);
        this.cueBuilders.clear();
        this.cueBuilders.add(this.currentCueBuilder);
    }

    private void setCaptionMode(int r4) {
        if (this.captionMode != r4) goto L5;
        return;
    L5:
        int r0 = this.captionMode;
        this.captionMode = r4;
        if (r4 != 3) goto L12;
        int r1 = 0;
    L9:
        if (r1 >= this.cueBuilders.size()) goto L11;
        this.cueBuilders.get(r1).setCaptionMode(r4);
        r1 = r1 + 1;
        goto L9
    L11:
        return;
    L12:
        resetCueBuilders();
        if (r0 != 3) goto L15;
    L17:
        this.cues = Collections.emptyList();
        return;
    L15:
        if (r4 == 1) goto L17;
        if (r4 == 0) goto L17;
    }

    private void setCaptionRowCount(int r2) {
        this.captionRowCount = r2;
        this.currentCueBuilder.setCaptionRowCount(r2);
    }

    private boolean shouldClearStuckCaptions() {
        if (this.validDataChannelTimeoutUs != C0180C.TIME_UNSET) goto L5;
    L11:
        return false;
    L5:
        if (this.lastCueUpdateUs == C0180C.TIME_UNSET) goto L11;
        if ((getPositionUs() - this.lastCueUpdateUs) < this.validDataChannelTimeoutUs) goto L12;
        return true;
    L12:
        return false;
    }

    private boolean updateAndVerifyCurrentChannel(byte r3) {
        if (isCtrlCode(r3) == false) goto L6;
        this.currentChannel = getChannel(r3);
    L6:
        if (this.currentChannel != this.selectedChannel) goto L8;
        return true;
    L8:
        return false;
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder
    protected Subtitle createSubtitle() {
        this.lastCues = this.cues;
        return new CeaSubtitle((List) Assertions.checkNotNull(this.cues));
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder
    protected void decode(SubtitleInputBuffer r12) {
        ByteBuffer r0 = (ByteBuffer) Assertions.checkNotNull(r12.data);
        this.ccData.reset(r0.array(), r0.limit());
        boolean r1 = false;
    L3:
        boolean r4 = true;
        if (this.ccData.bytesLeft() < this.packetLength) goto L64;
        if (this.packetLength != 2) goto L8;
        int r2 = -4;
    L9:
        int r3 = this.ccData.readUnsignedByte();
        int r5 = this.ccData.readUnsignedByte();
        if ((r2 & 2) != 0) goto L3;
        if ((r2 & 1) != this.selectedField) goto L3;
        byte r6 = (byte) (r3 & 127);
        byte r7 = (byte) (r5 & 127);
        if (r6 != 0) goto L19;
        if (r7 == 0) goto L3;
    L19:
        boolean r8 = this.isCaptionValid;
        if ((r2 & 4) == 4) goto L22;
    L26:
        r4 = false;
    L27:
        this.isCaptionValid = r4;
        if (isRepeatedCommand(this.isCaptionValid, r6, r7) == true) goto L3;
        if (this.isCaptionValid == false) goto L32;
        maybeUpdateIsInCaptionService(r6, r7);
        if (this.isInCaptionService == false) goto L3;
        if (updateAndVerifyCurrentChannel(r6) == false) goto L3;
        if (isCtrlCode(r6) == true) goto L43;
        this.currentCueBuilder.append(getBasicChar(r6));
        if ((r7 & 224) == 0) goto L63;
        this.currentCueBuilder.append(getBasicChar(r7));
    L63:
        r1 = true;
        goto L3
    L43:
        if (isSpecialNorthAmericanChar(r6, r7) == false) goto L46;
        this.currentCueBuilder.append(getSpecialNorthAmericanChar(r7));
        goto L63
    L46:
        if (isExtendedWestEuropeanChar(r6, r7) == false) goto L49;
        this.currentCueBuilder.backspace();
        this.currentCueBuilder.append(getExtendedWestEuropeanChar(r6, r7));
        goto L63
    L49:
        if (isMidrowCtrlCode(r6, r7) == false) goto L52;
        handleMidrowCtrl(r7);
        goto L63
    L52:
        if (isPreambleAddressCode(r6, r7) == false) goto L55;
        handlePreambleAddressCode(r6, r7);
        goto L63
    L55:
        if (isTabCtrlCode(r6, r7) == false) goto L58;
        CueBuilder.access$002(this.currentCueBuilder, r7 - 32);
        goto L63
    L58:
        if (isMiscCode(r6, r7) == false) goto L63;
        handleMiscCode(r7);
        goto L63
    L32:
        if (r8 == false) goto L3;
        resetCueBuilders();
        r1 = true;
        goto L3
    L22:
        if (ODD_PARITY_BYTE_TABLE[r3] == false) goto L26;
        if (ODD_PARITY_BYTE_TABLE[r5] == false) goto L26;
    L8:
        r2 = this.ccData.readUnsignedByte();
        goto L9
    L64:
        if (r1 == true) goto L66;
        return;
    L66:
        if (this.captionMode != 1) goto L68;
    L69:
        this.cues = getDisplayCues();
        this.lastCueUpdateUs = getPositionUs();
        return;
    L68:
        if (this.captionMode == 3) goto L69;
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        return super.dequeueInputBuffer();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        SubtitleOutputBuffer r0 = super.dequeueOutputBuffer();
        if (r0 == null) goto L6;
        return r0;
    L6:
        if (shouldClearStuckCaptions() == false) goto L14;
        SubtitleOutputBuffer r2 = getAvailableOutputBuffer();
        if (r2 == null) goto L12;
        this.cues = Collections.emptyList();
        this.lastCueUpdateUs = C0180C.TIME_UNSET;
        Subtitle r5 = createSubtitle();
        r2.setContent(getPositionUs(), r5, Long.MAX_VALUE);
        return r2;
    L12:
        return null;
    L14:
        return null;
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() throws DecoderException {
        return dequeueOutputBuffer();
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.decoder.Decoder
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        setCaptionMode(0);
        setCaptionRowCount(4);
        resetCueBuilders();
        this.isCaptionValid = false;
        this.repeatableControlSet = false;
        this.repeatableControlCc1 = 0;
        this.repeatableControlCc2 = 0;
        this.currentChannel = 0;
        this.isInCaptionService = true;
        this.lastCueUpdateUs = C0180C.TIME_UNSET;
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
    public void release() {
    }

    @Override // androidx.media3.extractor.text.cea.CeaDecoder, androidx.media3.extractor.text.SubtitleDecoder
    public /* bridge */ /* synthetic */ void setPositionUs(long r1) {
        super.setPositionUs(r1);
    }
}
