package androidx.media3.extractor.text.dvb;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.SparseArray;
import androidx.core.view.ViewCompat;
import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableBitArray;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DvbParser implements SubtitleParser {
    public static final int CUE_REPLACEMENT_BEHAVIOR = 2;
    private static final int DATA_TYPE_24_TABLE_DATA = 32;
    private static final int DATA_TYPE_28_TABLE_DATA = 33;
    private static final int DATA_TYPE_2BP_CODE_STRING = 16;
    private static final int DATA_TYPE_48_TABLE_DATA = 34;
    private static final int DATA_TYPE_4BP_CODE_STRING = 17;
    private static final int DATA_TYPE_8BP_CODE_STRING = 18;
    private static final int DATA_TYPE_END_LINE = 240;
    private static final int OBJECT_CODING_PIXELS = 0;
    private static final int OBJECT_CODING_STRING = 1;
    private static final int PAGE_STATE_NORMAL = 0;
    private static final int REGION_DEPTH_4_BIT = 2;
    private static final int REGION_DEPTH_8_BIT = 3;
    private static final int SEGMENT_TYPE_CLUT_DEFINITION = 18;
    private static final int SEGMENT_TYPE_DISPLAY_DEFINITION = 20;
    private static final int SEGMENT_TYPE_OBJECT_DATA = 19;
    private static final int SEGMENT_TYPE_PAGE_COMPOSITION = 16;
    private static final int SEGMENT_TYPE_REGION_COMPOSITION = 17;
    private static final String TAG = "DvbParser";
    private static final byte[] defaultMap2To4 = null;
    private static final byte[] defaultMap2To8 = null;
    private static final byte[] defaultMap4To8 = null;
    private Bitmap bitmap;
    private final Canvas canvas;
    private final ClutDefinition defaultClutDefinition;
    private final DisplayDefinition defaultDisplayDefinition;
    private final Paint defaultPaint;
    private final Paint fillRegionPaint;
    private final SubtitleService subtitleService;

    private static final class ClutDefinition {
        public final int[] clutEntries2Bit;
        public final int[] clutEntries4Bit;
        public final int[] clutEntries8Bit;

        /* JADX INFO: renamed from: id */
        public final int f61id;

        public ClutDefinition(int r1, int[] r2, int[] r3, int[] r4) {
            this.f61id = r1;
            this.clutEntries2Bit = r2;
            this.clutEntries4Bit = r3;
            this.clutEntries8Bit = r4;
        }
    }

    private static final class DisplayDefinition {
        public final int height;
        public final int horizontalPositionMaximum;
        public final int horizontalPositionMinimum;
        public final int verticalPositionMaximum;
        public final int verticalPositionMinimum;
        public final int width;

        public DisplayDefinition(int r1, int r2, int r3, int r4, int r5, int r6) {
            this.width = r1;
            this.height = r2;
            this.horizontalPositionMinimum = r3;
            this.horizontalPositionMaximum = r4;
            this.verticalPositionMinimum = r5;
            this.verticalPositionMaximum = r6;
        }
    }

    private static final class ObjectData {
        public final byte[] bottomFieldData;

        /* JADX INFO: renamed from: id */
        public final int f62id;
        public final boolean nonModifyingColorFlag;
        public final byte[] topFieldData;

        public ObjectData(int r1, boolean r2, byte[] r3, byte[] r4) {
            this.f62id = r1;
            this.nonModifyingColorFlag = r2;
            this.topFieldData = r3;
            this.bottomFieldData = r4;
        }
    }

    private static final class PageComposition {
        public final SparseArray<PageRegion> regions;
        public final int state;
        public final int timeOutSecs;
        public final int version;

        public PageComposition(int r1, int r2, int r3, SparseArray<PageRegion> r4) {
            this.timeOutSecs = r1;
            this.version = r2;
            this.state = r3;
            this.regions = r4;
        }
    }

    private static final class PageRegion {
        public final int horizontalAddress;
        public final int verticalAddress;

        public PageRegion(int r1, int r2) {
            this.horizontalAddress = r1;
            this.verticalAddress = r2;
        }
    }

    private static final class RegionComposition {
        public final int clutId;
        public final int depth;
        public final boolean fillFlag;
        public final int height;

        /* JADX INFO: renamed from: id */
        public final int f63id;
        public final int levelOfCompatibility;
        public final int pixelCode2Bit;
        public final int pixelCode4Bit;
        public final int pixelCode8Bit;
        public final SparseArray<RegionObject> regionObjects;
        public final int width;

        public RegionComposition(int r1, boolean r2, int r3, int r4, int r5, int r6, int r7, int r8, int r9, int r10, SparseArray<RegionObject> r11) {
            this.f63id = r1;
            this.fillFlag = r2;
            this.width = r3;
            this.height = r4;
            this.levelOfCompatibility = r5;
            this.depth = r6;
            this.clutId = r7;
            this.pixelCode8Bit = r8;
            this.pixelCode4Bit = r9;
            this.pixelCode2Bit = r10;
            this.regionObjects = r11;
        }

        public void mergeFrom(RegionComposition r6) {
            SparseArray<RegionObject> r0 = r6.regionObjects;
            int r1 = 0;
        L4:
            if (r1 >= r0.size()) goto L6;
            this.regionObjects.put(r0.keyAt(r1), r0.valueAt(r1));
            r1 = r1 + 1;
            goto L4
        }
    }

    private static final class RegionObject {
        public final int backgroundPixelCode;
        public final int foregroundPixelCode;
        public final int horizontalPosition;
        public final int provider;
        public final int type;
        public final int verticalPosition;

        public RegionObject(int r1, int r2, int r3, int r4, int r5, int r6) {
            this.type = r1;
            this.provider = r2;
            this.horizontalPosition = r3;
            this.verticalPosition = r4;
            this.foregroundPixelCode = r5;
            this.backgroundPixelCode = r6;
        }
    }

    private static final class SubtitleService {
        public final SparseArray<ClutDefinition> ancillaryCluts;
        public final SparseArray<ObjectData> ancillaryObjects;
        public final int ancillaryPageId;
        public final SparseArray<ClutDefinition> cluts;
        public DisplayDefinition displayDefinition;
        public final SparseArray<ObjectData> objects;
        public PageComposition pageComposition;
        public final SparseArray<RegionComposition> regions;
        public final int subtitlePageId;

        public SubtitleService(int r2, int r3) {
            this.subtitlePageId = r2;
            this.ancillaryPageId = r3;
            this.regions = new SparseArray();
            this.cluts = new SparseArray();
            this.objects = new SparseArray();
            this.ancillaryCluts = new SparseArray();
            this.ancillaryObjects = new SparseArray();
        }

        public void reset() {
            this.regions.clear();
            this.cluts.clear();
            this.objects.clear();
            this.ancillaryCluts.clear();
            this.ancillaryObjects.clear();
            this.displayDefinition = null;
            this.pageComposition = null;
        }
    }

    static {
        defaultMap2To4 = new byte[]{0, 7, 8, Ascii.f88SI};
        defaultMap2To8 = new byte[]{0, 119, -120, -1};
        defaultMap4To8 = new byte[]{0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1};
    }

    public DvbParser(List<byte[]> r13) {
        ParsableByteArray r0 = new ParsableByteArray(r13.get(0));
        int r2 = r0.readUnsignedShort();
        int r3 = r0.readUnsignedShort();
        this.defaultPaint = new Paint();
        this.defaultPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.defaultPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.defaultPaint.setPathEffect(null);
        this.fillRegionPaint = new Paint();
        this.fillRegionPaint.setStyle(Paint.Style.FILL);
        this.fillRegionPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        this.fillRegionPaint.setPathEffect(null);
        this.canvas = new Canvas();
        this.defaultDisplayDefinition = new DisplayDefinition(AdaptiveTrackSelection.DEFAULT_MAX_HEIGHT_TO_DISCARD, 575, 0, AdaptiveTrackSelection.DEFAULT_MAX_HEIGHT_TO_DISCARD, 0, 575);
        this.defaultClutDefinition = new ClutDefinition(0, generateDefault2BitClutEntries(), generateDefault4BitClutEntries(), generateDefault8BitClutEntries());
        this.subtitleService = new SubtitleService(r2, r3);
    }

    private static byte[] buildClutMapTable(int r3, int r4, ParsableBitArray r5) {
        byte[] r0 = new byte[r3];
        int r1 = 0;
    L3:
        if (r1 >= r3) goto L5;
        r0[r1] = (byte) r5.readBits(r4);
        r1 = r1 + 1;
        goto L3
    L5:
        return r0;
    }

    private static int[] generateDefault2BitClutEntries() {
        return new int[]{0, -1, ViewCompat.MEASURED_STATE_MASK, -8421505};
    }

    private static int[] generateDefault4BitClutEntries() {
        int[] r0 = new int[16];
        r0[0] = 0;
        int r2 = 1;
    L4:
        if (r2 >= r0.length) goto L34;
        if (r2 < 8) goto L8;
        int r5 = 127;
        if ((r2 & 1) == 0) goto L23;
        int r3 = 127;
    L25:
        if ((r2 & 2) == 0) goto L27;
        int r6 = 127;
    L29:
        if ((r2 & 4) != 0) goto L32;
        r5 = 0;
    L32:
        r0[r2] = getColor(255, r3, r6, r5);
    L33:
        r2 = r2 + 1;
        goto L4
    L27:
        r6 = 0;
        goto L29
    L23:
        r3 = 0;
        goto L25
    L8:
        if ((r2 & 1) == 0) goto L10;
        int r4 = 255;
    L12:
        if ((r2 & 2) == 0) goto L14;
        int r7 = 255;
    L16:
        if ((r2 & 4) == 0) goto L18;
        int r8 = 255;
    L19:
        r0[r2] = getColor(255, r4, r7, r8);
        goto L33
    L18:
        r8 = 0;
        goto L19
    L14:
        r7 = 0;
        goto L16
    L10:
        r4 = 0;
        goto L12
    L34:
        return r0;
    }

    private static int[] generateDefault8BitClutEntries() {
        int[] r0 = new int[256];
        r0[0] = 0;
        int r2 = 0;
    L4:
        if (r2 >= r0.length) goto L124;
        int r4 = 255;
        if (r2 < 8) goto L8;
        int r6 = 170;
        int r7 = 43;
        int r8 = 85;
        switch((r2 & TsExtractor.TS_STREAM_TYPE_DTS_HD)) {
            case 0: goto L99;
            case 8: goto L74;
            case 128: goto L49;
            case 136: goto L24;
            default: goto L123;
        };
    L24:
        if ((r2 & 1) == 0) goto L26;
        int r3 = 43;
    L28:
        if ((r2 & 16) == 0) goto L30;
        int r5 = 85;
    L31:
        int r9 = r3 + r5;
        if ((r2 & 2) == 0) goto L34;
        int r10 = 43;
    L36:
        if ((r2 & 32) == 0) goto L38;
        int r11 = 85;
    L39:
        int r12 = r10 + r11;
        if ((r2 & 4) != 0) goto L44;
        r7 = 0;
    L44:
        if ((r2 & 64) != 0) goto L47;
        r8 = 0;
    L47:
        r0[r2] = getColor(255, r9, r12, r7 + r8);
        goto L123
    L38:
        r11 = 0;
        goto L39
    L34:
        r10 = 0;
        goto L36
    L30:
        r5 = 0;
        goto L31
    L26:
        r3 = 0;
        goto L28
    L49:
        if ((r2 & 1) == 0) goto L51;
        int r13 = 43;
    L52:
        int r14 = r13 + 127;
        if ((r2 & 16) == 0) goto L55;
        int r15 = 85;
    L56:
        int r16 = r14 + r15;
        if ((r2 & 2) == 0) goto L59;
        int r17 = 43;
    L60:
        int r18 = r17 + 127;
        if ((r2 & 32) == 0) goto L63;
        int r19 = 85;
    L64:
        int r20 = r18 + r19;
        if ((r2 & 4) != 0) goto L68;
        r7 = 0;
    L68:
        int r21 = r7 + 127;
        if ((r2 & 64) != 0) goto L72;
        r8 = 0;
    L72:
        r0[r2] = getColor(255, r16, r20, r21 + r8);
        goto L123
    L63:
        r19 = 0;
        goto L64
    L59:
        r17 = 0;
        goto L60
    L55:
        r15 = 0;
        goto L56
    L51:
        r13 = 0;
        goto L52
    L74:
        if ((r2 & 1) == 0) goto L76;
        int r22 = 85;
    L78:
        if ((r2 & 16) == 0) goto L80;
        int r23 = 170;
    L81:
        int r24 = r22 + r23;
        if ((r2 & 2) == 0) goto L84;
        int r25 = 85;
    L86:
        if ((r2 & 32) == 0) goto L88;
        int r26 = 170;
    L89:
        int r27 = r25 + r26;
        if ((r2 & 4) != 0) goto L94;
        r8 = 0;
    L94:
        if ((r2 & 64) != 0) goto L97;
        r6 = 0;
    L97:
        r0[r2] = getColor(127, r24, r27, r8 + r6);
        goto L123
    L88:
        r26 = 0;
        goto L89
    L84:
        r25 = 0;
        goto L86
    L80:
        r23 = 0;
        goto L81
    L76:
        r22 = 0;
        goto L78
    L99:
        if ((r2 & 1) == 0) goto L101;
        int r28 = 85;
    L103:
        if ((r2 & 16) == 0) goto L105;
        int r29 = 170;
    L106:
        int r30 = r28 + r29;
        if ((r2 & 2) == 0) goto L109;
        int r31 = 85;
    L111:
        if ((r2 & 32) == 0) goto L113;
        int r32 = 170;
    L114:
        int r33 = r31 + r32;
        if ((r2 & 4) != 0) goto L119;
        r8 = 0;
    L119:
        if ((r2 & 64) != 0) goto L122;
        r6 = 0;
    L122:
        r0[r2] = getColor(255, r30, r33, r8 + r6);
        goto L123
    L113:
        r32 = 0;
        goto L114
    L109:
        r31 = 0;
        goto L111
    L105:
        r29 = 0;
        goto L106
    L101:
        r28 = 0;
    L123:
        r2 = r2 + 1;
        goto L4
    L8:
        if ((r2 & 1) == 0) goto L10;
        int r34 = 255;
    L12:
        if ((r2 & 2) == 0) goto L14;
        int r35 = 255;
    L16:
        if ((r2 & 4) != 0) goto L19;
        r4 = 0;
    L19:
        r0[r2] = getColor(63, r34, r35, r4);
        goto L123
    L14:
        r35 = 0;
        goto L16
    L10:
        r34 = 0;
        goto L12
    L124:
        return r0;
    }

    private static int getColor(int r2, int r3, int r4, int r5) {
        return (((r2 << 24) | (r3 << 16)) | (r4 << 8)) | r5;
    }

    private static int paint2BitPixelCodeString(ParsableBitArray r10, int[] r11, byte[] r12, int r13, int r14, Paint r15, Canvas r16) {
        boolean r0 = false;
    L3:
        int r6 = r10.readBits(2);
        if (r6 == 0) goto L7;
        boolean r7 = r0;
        int r8 = 1;
        int r9 = r6;
    L19:
        if (r8 == 0) goto L25;
        if (r15 == 0) goto L25;
        if (r12 == null) goto L23;
        int r1 = r12[r9];
    L24:
        r15.setColor(r11[r1]);
        r16.drawRect(r13, r14, r13 + r8, r14 + 1, r15);
        goto L25
    L23:
        r1 = r9;
    L25:
        r13 = r13 + r8;
        if (r7 == true) goto L27;
        r0 = r7;
        goto L3
    L27:
        return r13;
    L7:
        if (r10.readBit() == false) goto L10;
        r7 = r0;
        r8 = r10.readBits(3) + 3;
        r9 = r10.readBits(2);
        goto L19
    L10:
        if (r10.readBit() == false) goto L13;
        r7 = r0;
        r8 = 1;
        r9 = 0;
        goto L19
    L13:
        switch(r10.readBits(2)) {
            case 0: goto L18;
            case 1: goto L17;
            case 2: goto L16;
            case 3: goto L15;
            default: goto L14;
        };
    L14:
        r7 = r0;
        r8 = 0;
        r9 = 0;
        goto L19
    L15:
        r7 = r0;
        r8 = r10.readBits(8) + 29;
        r9 = r10.readBits(2);
        goto L19
    L16:
        r7 = r0;
        r8 = r10.readBits(4) + 12;
        r9 = r10.readBits(2);
        goto L19
    L17:
        r7 = r0;
        r8 = 2;
        r9 = 0;
        goto L19
    L18:
        r7 = true;
        r8 = 0;
        r9 = 0;
        goto L19
    }

    private static int paint4BitPixelCodeString(ParsableBitArray r10, int[] r11, byte[] r12, int r13, int r14, Paint r15, Canvas r16) {
        boolean r0 = false;
    L3:
        int r4 = r10.readBits(4);
        if (r4 == 0) goto L7;
        boolean r6 = r0;
        int r7 = 1;
        int r8 = r4;
    L22:
        if (r7 == 0) goto L28;
        if (r15 == 0) goto L28;
        if (r12 == null) goto L26;
        int r1 = r12[r8];
    L27:
        r15.setColor(r11[r1]);
        r16.drawRect(r13, r14, r13 + r7, r14 + 1, r15);
        goto L28
    L26:
        r1 = r8;
    L28:
        r13 = r13 + r7;
        if (r6 == true) goto L30;
        r0 = r6;
        goto L3
    L30:
        return r13;
    L7:
        if (r10.readBit() == true) goto L13;
        int r5 = r10.readBits(3);
        if (r5 == 0) goto L11;
        r6 = r0;
        r7 = r5 + 2;
        r8 = 0;
        goto L22
    L11:
        r6 = true;
        r7 = 0;
        r8 = 0;
        goto L22
    L13:
        if (r10.readBit() == true) goto L16;
        r6 = r0;
        r7 = r10.readBits(2) + 4;
        r8 = r10.readBits(4);
        goto L22
    L16:
        switch(r10.readBits(2)) {
            case 0: goto L21;
            case 1: goto L20;
            case 2: goto L19;
            case 3: goto L18;
            default: goto L17;
        };
    L17:
        r6 = r0;
        r7 = 0;
        r8 = 0;
        goto L22
    L18:
        r6 = r0;
        r7 = r10.readBits(8) + 25;
        r8 = r10.readBits(4);
        goto L22
    L19:
        r6 = r0;
        r7 = r10.readBits(4) + 9;
        r8 = r10.readBits(4);
        goto L22
    L20:
        r6 = r0;
        r7 = 2;
        r8 = 0;
        goto L22
    L21:
        r6 = r0;
        r7 = 1;
        r8 = 0;
        goto L22
    }

    private static int paint8BitPixelCodeString(ParsableBitArray r10, int[] r11, byte[] r12, int r13, int r14, Paint r15, Canvas r16) {
        boolean r0 = false;
    L3:
        int r4 = r10.readBits(8);
        if (r4 == 0) goto L7;
        boolean r6 = r0;
        int r7 = 1;
        int r8 = r4;
    L13:
        if (r7 == 0) goto L19;
        if (r15 == 0) goto L19;
        if (r12 == null) goto L17;
        int r1 = r12[r8];
    L18:
        r15.setColor(r11[r1]);
        r16.drawRect(r13, r14, r13 + r7, r14 + 1, r15);
        goto L19
    L17:
        r1 = r8;
    L19:
        r13 = r13 + r7;
        if (r6 == true) goto L21;
        r0 = r6;
        goto L3
    L21:
        return r13;
    L7:
        if (r10.readBit() == true) goto L12;
        int r5 = r10.readBits(7);
        if (r5 == 0) goto L11;
        r6 = r0;
        r7 = r5;
        r8 = 0;
        goto L13
    L11:
        r6 = true;
        r7 = 0;
        r8 = 0;
        goto L13
    L12:
        r6 = r0;
        r7 = r10.readBits(7);
        r8 = r10.readBits(8);
        goto L13
    }

    private static void paintPixelDataSubBlock(byte[] r11, int[] r12, int r13, int r14, int r15, Paint r16, Canvas r17) {
        ParsableBitArray r0 = new ParsableBitArray(r11);
        byte[] r8 = null;
        byte[] r9 = null;
        int r4 = r14;
        int r5 = r15;
        byte[] r1 = null;
    L4:
        if (r0.bitsLeft() == 0) goto L34;
        switch(r0.readBits(8)) {
            case 16: goto L20;
            case 17: goto L13;
            case 18: goto L12;
            case 32: goto L11;
            case 33: goto L10;
            case 34: goto L9;
            case 240: goto L8;
            default: goto L4;
        };
    L8:
        r5 = r5 + 2;
        r4 = r14;
        goto L4
    L9:
        r9 = buildClutMapTable(16, 8, r0);
        goto L4
    L10:
        r8 = buildClutMapTable(4, 8, r0);
        goto L4
    L11:
        r1 = buildClutMapTable(4, 4, r0);
        goto L4
    L12:
        r4 = paint8BitPixelCodeString(r0, r12, null, r4, r5, r16, r17);
        goto L4
    L13:
        if (r13 != 3) goto L18;
        if (r9 != null) goto L16;
        byte[] r2 = defaultMap4To8;
    L19:
        r4 = paint4BitPixelCodeString(r0, r12, r2, r4, r5, r16, r17);
        r0.byteAlign();
        goto L4
    L16:
        r2 = r9;
        goto L19
    L18:
        r2 = null;
        goto L19
    L20:
        if (r13 != 3) goto L26;
        if (r8 != null) goto L23;
        byte[] r3 = defaultMap2To8;
    L32:
        r4 = paint2BitPixelCodeString(r0, r12, r3, r4, r5, r16, r17);
        r0.byteAlign();
        goto L4
    L23:
        r3 = r8;
        goto L32
    L26:
        if (r13 != 2) goto L31;
        if (r1 != null) goto L29;
        r3 = defaultMap2To4;
        goto L32
    L29:
        r3 = r1;
        goto L32
    L31:
        r3 = null;
        goto L32
    }

    private static void paintPixelDataSubBlocks(ObjectData r8, ClutDefinition r9, int r10, int r11, int r12, Paint r13, Canvas r14) {
        if (r10 != 3) goto L6;
        int[] r2 = r9.clutEntries8Bit;
    L9:
        paintPixelDataSubBlock(r8.topFieldData, r2, r10, r11, r12, r13, r14);
        paintPixelDataSubBlock(r8.bottomFieldData, r2, r10, r11, r12 + 1, r13, r14);
        return;
    L6:
        if (r10 != 2) goto L8;
        r2 = r9.clutEntries4Bit;
        goto L9
    L8:
        r2 = r9.clutEntries2Bit;
        goto L9
    }

    private CuesWithTiming parse(ParsableBitArray r26) {
    L4:
        if (r26.bitsLeft() < 48) goto L8;
        if (r26.readBits(8) != 15) goto L8;
        parseSubtitlingSegment(r26, this.subtitleService);
    L8:
        PageComposition r2 = this.subtitleService.pageComposition;
        if (r2 != null) goto L13;
        return new CuesWithTiming(ImmutableList.m212of(), C0180C.TIME_UNSET, C0180C.TIME_UNSET);
    L13:
        if (this.subtitleService.displayDefinition == null) goto L15;
        DisplayDefinition r3 = this.subtitleService.displayDefinition;
    L17:
        if (this.bitmap != null) goto L19;
    L22:
        this.bitmap = Bitmap.createBitmap(r3.width + 1, r3.height + 1, Bitmap.Config.ARGB_8888);
        this.canvas.setBitmap(this.bitmap);
    L23:
        ArrayList r4 = new ArrayList();
        SparseArray<PageRegion> r5 = r2.regions;
        int r6 = 0;
    L25:
        if (r6 >= r5.size()) goto L60;
        this.canvas.save();
        PageRegion r7 = r5.valueAt(r6);
        RegionComposition r9 = this.subtitleService.regions.get(r5.keyAt(r6));
        int r10 = r7.horizontalAddress + r3.horizontalPositionMinimum;
        int r11 = r7.verticalAddress + r3.verticalPositionMinimum;
        this.canvas.clipRect(r10, r11, Math.min(r9.width + r10, r3.horizontalPositionMaximum), Math.min(r9.height + r11, r3.verticalPositionMaximum));
        ClutDefinition r14 = this.subtitleService.cluts.get(r9.clutId);
        if (r14 != null) goto L32;
        ClutDefinition r15 = this.subtitleService.ancillaryCluts.get(r9.clutId);
        if (r15 != null) goto L31;
        ClutDefinition r16 = this.defaultClutDefinition;
    L33:
        SparseArray<RegionObject> r1 = r9.regionObjects;
        int r17 = 0;
    L35:
        if (r17 >= r1.size()) goto L48;
        int r18 = r1.keyAt(r17);
        SparseArray<RegionObject> r22 = r1;
        RegionObject r8 = r1.valueAt(r17);
        PageComposition r23 = r2;
        ObjectData r12 = this.subtitleService.objects.get(r18);
        if (r12 != null) goto L40;
        r12 = this.subtitleService.ancillaryObjects.get(r18);
    L40:
        if (r12 == null) goto L47;
        if (r12.nonModifyingColorFlag == false) goto L44;
        Paint r13 = null;
    L45:
        ObjectData r19 = r12;
        paintPixelDataSubBlocks(r19, r16, r9.depth, r10 + r8.horizontalPosition, r11 + r8.verticalPosition, r13, this.canvas);
        goto L47
    L44:
        r13 = this.defaultPaint;
    L47:
        r17 = r17 + 1;
        r16 = r16;
        r1 = r22;
        r2 = r23;
        r5 = r5;
        goto L35
    L48:
        PageComposition r24 = r2;
        SparseArray<PageRegion> r25 = r5;
        ClutDefinition r20 = r16;
        if (r9.fillFlag == false) goto L58;
        if (r9.depth != 3) goto L54;
        int r21 = r20.clutEntries8Bit[r9.pixelCode8Bit];
    L57:
        this.fillRegionPaint.setColor(r21);
        this.canvas.drawRect(r10, r11, r9.width + r10, r9.height + r11, this.fillRegionPaint);
        goto L58
    L54:
        if (r9.depth != 2) goto L56;
        r21 = r20.clutEntries4Bit[r9.pixelCode4Bit];
        goto L57
    L56:
        r21 = r20.clutEntries2Bit[r9.pixelCode2Bit];
    L58:
        r4.add(new Cue.Builder().setBitmap(Bitmap.createBitmap(this.bitmap, r10, r11, r9.width, r9.height)).setPosition(r10 / r3.width).setPositionAnchor(0).setLine(r11 / r3.height, 0).setLineAnchor(0).setSize(r9.width / r3.width).setBitmapHeight(r9.height / r3.height).build());
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.canvas.restore();
        r6 = r6 + 1;
        r2 = r24;
        r5 = r25;
        goto L25
    L31:
        r16 = r15;
        goto L33
    L32:
        r16 = r14;
        goto L33
    L60:
        return new CuesWithTiming(r4, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
    L19:
        if ((r3.width + 1) != this.bitmap.getWidth()) goto L22;
        if ((r3.height + 1) == this.bitmap.getHeight()) goto L23;
    L15:
        r3 = this.defaultDisplayDefinition;
        goto L17
    }

    private static ClutDefinition parseClutDefinition(ParsableBitArray r22, int r23) {
        ParsableBitArray r0 = r22;
        int r1 = 8;
        int r2 = r0.readBits(8);
        r0.skipBits(8);
        int r3 = r23 - 2;
        int[] r4 = generateDefault2BitClutEntries();
        int[] r5 = generateDefault4BitClutEntries();
        int[] r6 = generateDefault8BitClutEntries();
    L3:
        if (r3 <= 0) goto L19;
        int r7 = r0.readBits(r1);
        int r8 = r0.readBits(r1);
        int r9 = r3 - 2;
        if ((r8 & 128) == 0) goto L8;
        int[] r10 = r4;
    L12:
        if ((r8 & 1) == 0) goto L14;
        int r11 = r0.readBits(r1);
        int r12 = r0.readBits(r1);
        int r13 = r0.readBits(r1);
        int r14 = r0.readBits(r1);
        int r15 = r9 - 4;
    L15:
        if (r11 != 0) goto L17;
        r12 = 0;
        r13 = 0;
        r14 = 255;
    L17:
        r10[r7] = getColor((byte) (255 - (r14 & 255)), Util.constrainValue((int) (((double) r11) + (((double) (r12 - 128)) * 1.402d)), 0, 255), Util.constrainValue((int) ((((double) r11) - (((double) (r13 - 128)) * 0.34414d)) - (((double) (r12 - 128)) * 0.71414d)), 0, 255), Util.constrainValue((int) (((double) r11) + (((double) (r13 - 128)) * 1.772d)), 0, 255));
        r1 = 8;
        r0 = r22;
        r3 = r15;
        r2 = r2;
        goto L3
    L14:
        int r16 = r0.readBits(6) << 2;
        int r17 = r0.readBits(4) << 4;
        int r18 = r0.readBits(4) << 4;
        r15 = r9 - 2;
        r13 = r18;
        r14 = r0.readBits(2) << 6;
        r11 = r16;
        r12 = r17;
        goto L15
    L8:
        if ((r8 & 64) == 0) goto L10;
        r10 = r5;
        goto L12
    L10:
        r10 = r6;
        goto L12
    L19:
        return new ClutDefinition(r2, r4, r5, r6);
    }

    private static DisplayDefinition parseDisplayDefinition(ParsableBitArray r9) {
        r9.skipBits(4);
        boolean r0 = r9.readBit();
        r9.skipBits(3);
        int r3 = r9.readBits(16);
        int r4 = r9.readBits(16);
        if (r0 == false) goto L5;
        int r2 = r9.readBits(16);
        int r5 = r9.readBits(16);
        int r6 = r9.readBits(16);
        int r8 = r9.readBits(16);
        int r7 = r6;
        int r10 = r5;
        int r11 = r2;
    L7:
        return new DisplayDefinition(r3, r4, r11, r10, r7, r8);
    L5:
        r8 = r4;
        r7 = 0;
        r10 = r3;
        r11 = 0;
        goto L7
    }

    private static ObjectData parseObjectData(ParsableBitArray r8) {
        int r1 = r8.readBits(16);
        r8.skipBits(4);
        int r2 = r8.readBits(2);
        boolean r3 = r8.readBit();
        r8.skipBits(1);
        byte[] r5 = Util.EMPTY_BYTE_ARRAY;
        byte[] r6 = Util.EMPTY_BYTE_ARRAY;
        if (r2 != 1) goto L5;
        r8.skipBits(r8.readBits(8) * 16);
    L14:
        return new ObjectData(r1, r3, r5, r6);
    L5:
        if (r2 != 0) goto L14;
        int r4 = r8.readBits(16);
        int r0 = r8.readBits(16);
        if (r4 <= 0) goto L9;
        r5 = new byte[r4];
        r8.readBytes(r5, 0, r4);
    L9:
        if (r0 <= 0) goto L11;
        r6 = new byte[r0];
        r8.readBytes(r6, 0, r0);
        goto L14
    L11:
        r6 = r5;
        goto L14
    }

    private static PageComposition parsePageComposition(ParsableBitArray r10, int r11) {
        int r1 = r10.readBits(8);
        int r2 = r10.readBits(4);
        int r4 = r10.readBits(2);
        r10.skipBits(2);
        int r3 = r11 - 2;
        SparseArray r5 = new SparseArray();
    L3:
        if (r3 <= 0) goto L6;
        int r6 = r10.readBits(8);
        r10.skipBits(8);
        r3 = r3 - 6;
        r5.put(r6, new PageRegion(r10.readBits(16), r10.readBits(16)));
        goto L3
    L6:
        return new PageComposition(r1, r2, r4, r5);
    }

    private static RegionComposition parseRegionComposition(ParsableBitArray r25, int r26) {
        int r3 = r25.readBits(8);
        int r2 = 4;
        r25.skipBits(4);
        boolean r4 = r25.readBit();
        r25.skipBits(3);
        int r6 = 16;
        int r7 = r25.readBits(16);
        int r8 = r25.readBits(16);
        int r9 = r25.readBits(3);
        int r5 = r25.readBits(3);
        int r10 = 2;
        r25.skipBits(2);
        int r11 = r25.readBits(8);
        int r12 = r25.readBits(8);
        int r13 = r25.readBits(4);
        int r14 = r25.readBits(2);
        r25.skipBits(2);
        int r15 = r26 - 10;
        SparseArray r16 = new SparseArray();
    L3:
        if (r15 <= 0) goto L14;
        int r1 = r25.readBits(r6);
        int r17 = r25.readBits(r10);
        int r20 = r25.readBits(r10);
        int r21 = r25.readBits(12);
        r25.skipBits(r2);
        int r22 = r25.readBits(12);
        r15 = r15 - 6;
        int r18 = 0;
        int r19 = 0;
        if (r17 == 1) goto L11;
        if (r17 == 2) goto L11;
        char r23 = '\b';
    L12:
        r16.put(r1, new RegionObject(r17, r20, r21, r22, r18, r19));
        r2 = 4;
        r6 = 16;
        r10 = 2;
    L11:
        r23 = '\b';
        r18 = r25.readBits(8);
        r19 = r25.readBits(8);
        r15 = r15 - 2;
        goto L12
    L14:
        return new RegionComposition(r3, r4, r7, r8, r9, r5, r11, r12, r13, r14, r16);
    }

    private static void parseSubtitlingSegment(ParsableBitArray r8, SubtitleService r9) {
        int r0 = r8.readBits(8);
        int r2 = r8.readBits(16);
        int r1 = r8.readBits(16);
        int r3 = r8.getBytePosition() + r1;
        if ((r1 * 8) <= r8.bitsLeft()) goto L6;
        Log.m48w(TAG, "Data field length exceeds limit");
        r8.skipBits(r8.bitsLeft());
        return;
    L6:
        switch(r0) {
            case 16: goto L35;
            case 17: goto L25;
            case 18: goto L19;
            case 19: goto L12;
            case 20: goto L9;
            default: goto L44;
        };
    L25:
        PageComposition r4 = r9.pageComposition;
        if (r2 != r9.subtitlePageId) goto L44;
        if (r4 == null) goto L44;
        RegionComposition r5 = parseRegionComposition(r8, r1);
        if (r4.state != 0) goto L33;
        RegionComposition r6 = r9.regions.get(r5.f63id);
        if (r6 == null) goto L33;
        r5.mergeFrom(r6);
    L33:
        r9.regions.put(r5.f63id, r5);
    L44:
        r8.skipBytes(r3 - r8.getBytePosition());
        return;
    L9:
        if (r2 != r9.subtitlePageId) goto L44;
        r9.displayDefinition = parseDisplayDefinition(r8);
        goto L44
    L12:
        if (r2 != r9.subtitlePageId) goto L15;
        ObjectData r7 = parseObjectData(r8);
        r9.objects.put(r7.f62id, r7);
        goto L44
    L15:
        if (r2 != r9.ancillaryPageId) goto L44;
        ObjectData r10 = parseObjectData(r8);
        r9.ancillaryObjects.put(r10.f62id, r10);
        goto L44
    L19:
        if (r2 != r9.subtitlePageId) goto L22;
        ClutDefinition r11 = parseClutDefinition(r8, r1);
        r9.cluts.put(r11.f61id, r11);
        goto L44
    L22:
        if (r2 != r9.ancillaryPageId) goto L44;
        ClutDefinition r12 = parseClutDefinition(r8, r1);
        r9.ancillaryCluts.put(r12.f61id, r12);
        goto L44
    L35:
        if (r2 != r9.subtitlePageId) goto L44;
        PageComposition r13 = r9.pageComposition;
        PageComposition r14 = parsePageComposition(r8, r1);
        if (r14.state == 0) goto L39;
        r9.pageComposition = r14;
        r9.regions.clear();
        r9.cluts.clear();
        r9.objects.clear();
        goto L44
    L39:
        if (r13 == null) goto L44;
        if (r13.version == r14.version) goto L44;
        r9.pageComposition = r14;
        goto L44
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 2;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r3, int r4, int r5, SubtitleParser.OutputOptions r6, Consumer<CuesWithTiming> r7) {
        ParsableBitArray r0 = new ParsableBitArray(r3, r4 + r5);
        r0.setPosition(r4);
        r7.accept(parse(r0));
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
    public void reset() {
        this.subtitleService.reset();
    }
}
