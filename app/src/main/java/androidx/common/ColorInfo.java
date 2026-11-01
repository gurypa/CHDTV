package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Util;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.dataflow.qual.Pure;

/* JADX INFO: loaded from: classes.dex */
public final class ColorInfo {
    private static final String FIELD_CHROMA_BITDEPTH = null;
    private static final String FIELD_COLOR_RANGE = null;
    private static final String FIELD_COLOR_SPACE = null;
    private static final String FIELD_COLOR_TRANSFER = null;
    private static final String FIELD_HDR_STATIC_INFO = null;
    private static final String FIELD_LUMA_BITDEPTH = null;
    public static final ColorInfo SDR_BT709_LIMITED = null;
    public static final ColorInfo SRGB_BT709_FULL = null;
    public final int chromaBitdepth;
    public final int colorRange;
    public final int colorSpace;
    public final int colorTransfer;
    private int hashCode;
    public final byte[] hdrStaticInfo;
    public final int lumaBitdepth;

    /* JADX INFO: renamed from: androidx.media3.common.ColorInfo$1 */
    static /* synthetic */ class C01811 {
    }

    public static final class Builder {
        private int chromaBitdepth;
        private int colorRange;
        private int colorSpace;
        private int colorTransfer;
        private byte[] hdrStaticInfo;
        private int lumaBitdepth;

        public Builder() {
            this.colorSpace = -1;
            this.colorRange = -1;
            this.colorTransfer = -1;
            this.lumaBitdepth = -1;
            this.chromaBitdepth = -1;
        }

        private Builder(ColorInfo r2) {
            this.colorSpace = r2.colorSpace;
            this.colorRange = r2.colorRange;
            this.colorTransfer = r2.colorTransfer;
            this.hdrStaticInfo = r2.hdrStaticInfo;
            this.lumaBitdepth = r2.lumaBitdepth;
            this.chromaBitdepth = r2.chromaBitdepth;
        }

        /* synthetic */ Builder(ColorInfo r1, C01811 r2) {
            this(r1);
        }

        public ColorInfo build() {
            return new ColorInfo(this.colorSpace, this.colorRange, this.colorTransfer, this.hdrStaticInfo, this.lumaBitdepth, this.chromaBitdepth, null);
        }

        public Builder setChromaBitdepth(int r1) {
            this.chromaBitdepth = r1;
            return this;
        }

        public Builder setColorRange(int r1) {
            this.colorRange = r1;
            return this;
        }

        public Builder setColorSpace(int r1) {
            this.colorSpace = r1;
            return this;
        }

        public Builder setColorTransfer(int r1) {
            this.colorTransfer = r1;
            return this;
        }

        public Builder setHdrStaticInfo(byte[] r1) {
            this.hdrStaticInfo = r1;
            return this;
        }

        public Builder setLumaBitdepth(int r1) {
            this.lumaBitdepth = r1;
            return this;
        }
    }

    static {
        SDR_BT709_LIMITED = new Builder().setColorSpace(1).setColorRange(2).setColorTransfer(3).build();
        SRGB_BT709_FULL = new Builder().setColorSpace(1).setColorRange(1).setColorTransfer(2).build();
        FIELD_COLOR_SPACE = Util.intToStringMaxRadix(0);
        FIELD_COLOR_RANGE = Util.intToStringMaxRadix(1);
        FIELD_COLOR_TRANSFER = Util.intToStringMaxRadix(2);
        FIELD_HDR_STATIC_INFO = Util.intToStringMaxRadix(3);
        FIELD_LUMA_BITDEPTH = Util.intToStringMaxRadix(4);
        FIELD_CHROMA_BITDEPTH = Util.intToStringMaxRadix(5);
    }

    private ColorInfo(int r1, int r2, int r3, byte[] r4, int r5, int r6) {
        this.colorSpace = r1;
        this.colorRange = r2;
        this.colorTransfer = r3;
        this.hdrStaticInfo = r4;
        this.lumaBitdepth = r5;
        this.chromaBitdepth = r6;
    }

    /* synthetic */ ColorInfo(int r1, int r2, int r3, byte[] r4, int r5, int r6, C01811 r7) {
        this(r1, r2, r3, r4, r5, r6);
    }

    private static String chromaBitdepthToString(int r2) {
        if (r2 != (-1)) goto L7;
        return "NA";
    L7:
        return r2 + "bit Chroma";
    }

    private static String colorRangeToString(int r2) {
        switch(r2) {
            case -1: goto L9;
            case 0: goto L4;
            case 1: goto L7;
            case 2: goto L5;
            default: goto L4;
        };
    L5:
        return "Limited range";
    L7:
        return "Full range";
    L9:
        return "Unset color range";
    L4:
        return "Undefined color range " + r2;
    }

    public static int colorSpaceToIsoColorPrimaries(int r1) {
        switch(r1) {
            case -1: goto L9;
            case 1: goto L9;
            case 2: goto L7;
            case 6: goto L5;
            default: goto L4;
        };
    L4:
        return 1;
    L5:
        return 9;
    L7:
        return 5;
    L9:
        return 1;
    }

    public static int colorSpaceToIsoMatrixCoefficients(int r1) {
        switch(r1) {
            case -1: goto L9;
            case 1: goto L9;
            case 2: goto L7;
            case 6: goto L5;
            default: goto L4;
        };
    L4:
        return 1;
    L5:
        return 9;
    L7:
        return 6;
    L9:
        return 1;
    }

    private static String colorSpaceToString(int r2) {
        switch(r2) {
            case -1: goto L11;
            case 1: goto L9;
            case 2: goto L7;
            case 6: goto L5;
            default: goto L4;
        };
    L5:
        return "BT2020";
    L7:
        return "BT601";
    L9:
        return "BT709";
    L11:
        return "Unset color space";
    L4:
        return "Undefined color space " + r2;
    }

    public static int colorTransferToIsoTransferCharacteristics(int r1) {
        switch(r1) {
            case -1: goto L15;
            case 0: goto L4;
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L15;
            case 4: goto L4;
            case 5: goto L4;
            case 6: goto L9;
            case 7: goto L7;
            case 8: goto L4;
            case 9: goto L4;
            case 10: goto L5;
            default: goto L4;
        };
    L4:
        return 1;
    L5:
        return 4;
    L7:
        return 18;
    L9:
        return 16;
    L11:
        return 13;
    L13:
        return 8;
    L15:
        return 1;
    }

    private static String colorTransferToString(int r2) {
        switch(r2) {
            case -1: goto L17;
            case 0: goto L4;
            case 1: goto L15;
            case 2: goto L13;
            case 3: goto L11;
            case 4: goto L4;
            case 5: goto L4;
            case 6: goto L9;
            case 7: goto L7;
            case 8: goto L4;
            case 9: goto L4;
            case 10: goto L5;
            default: goto L4;
        };
    L5:
        return "Gamma 2.2";
    L7:
        return "HLG";
    L9:
        return "ST2084 PQ";
    L11:
        return "SDR SMPTE 170M";
    L13:
        return "sRGB";
    L15:
        return "Linear";
    L17:
        return "Unset color transfer";
    L4:
        return "Undefined color transfer " + r2;
    }

    public static ColorInfo fromBundle(Bundle r9) {
        return new ColorInfo(r9.getInt(FIELD_COLOR_SPACE, -1), r9.getInt(FIELD_COLOR_RANGE, -1), r9.getInt(FIELD_COLOR_TRANSFER, -1), r9.getByteArray(FIELD_HDR_STATIC_INFO), r9.getInt(FIELD_LUMA_BITDEPTH, -1), r9.getInt(FIELD_CHROMA_BITDEPTH, -1));
    }

    @EnsuresNonNullIf(expression = {"#1"}, result = false)
    public static boolean isEquivalentToAssumedSdrDefault(ColorInfo r4) {
        if (r4 != null) goto L6;
        return true;
    L6:
        if (r4.colorSpace == (-1)) goto L12;
        if (r4.colorSpace == 1) goto L12;
        if (r4.colorSpace == 2) goto L12;
    L30:
        return false;
    L12:
        if (r4.colorRange == (-1)) goto L16;
        if (r4.colorRange != 2) goto L30;
    L16:
        if (r4.colorTransfer == (-1)) goto L20;
        if (r4.colorTransfer != 3) goto L30;
    L20:
        if (r4.hdrStaticInfo != null) goto L30;
        if (r4.chromaBitdepth == (-1)) goto L26;
        if (r4.chromaBitdepth != 8) goto L30;
    L26:
        if (r4.lumaBitdepth != (-1)) goto L28;
        return true;
    L28:
        if (r4.lumaBitdepth != 8) goto L30;
        return true;
    }

    public static boolean isTransferHdr(ColorInfo r2) {
        if (r2 != null) goto L4;
    L8:
        return false;
    L4:
        if (r2.colorTransfer != 7) goto L6;
    L7:
        return true;
    L6:
        if (r2.colorTransfer != 6) goto L8;
        goto L7
    }

    @Pure
    public static int isoColorPrimariesToColorSpace(int r1) {
        switch(r1) {
            case 1: goto L9;
            case 2: goto L3;
            case 3: goto L3;
            case 4: goto L7;
            case 5: goto L7;
            case 6: goto L7;
            case 7: goto L7;
            case 8: goto L3;
            case 9: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 6;
    L7:
        return 2;
    L9:
        return 1;
    }

    @Pure
    public static int isoTransferCharacteristicsToColorTransfer(int r1) {
        switch(r1) {
            case 1: goto L13;
            case 4: goto L11;
            case 6: goto L13;
            case 7: goto L13;
            case 13: goto L9;
            case 16: goto L7;
            case 18: goto L5;
            default: goto L3;
        };
    L3:
        return -1;
    L5:
        return 7;
    L7:
        return 6;
    L9:
        return 2;
    L11:
        return 10;
    L13:
        return 3;
    }

    private static String lumaBitdepthToString(int r2) {
        if (r2 != (-1)) goto L7;
        return "NA";
    L7:
        return r2 + "bit Luma";
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L25:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L25;
        ColorInfo r2 = (ColorInfo) r6;
        if (this.colorSpace != r2.colorSpace) goto L24;
        if (this.colorRange != r2.colorRange) goto L24;
        if (this.colorTransfer != r2.colorTransfer) goto L24;
        if (Arrays.equals(this.hdrStaticInfo, r2.hdrStaticInfo) == false) goto L24;
        if (this.lumaBitdepth != r2.lumaBitdepth) goto L24;
        if (this.chromaBitdepth != r2.chromaBitdepth) goto L24;
        return true;
    L24:
        return false;
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L6;
        this.hashCode = (((((((((((17 * 31) + this.colorSpace) * 31) + this.colorRange) * 31) + this.colorTransfer) * 31) + Arrays.hashCode(this.hdrStaticInfo)) * 31) + this.lumaBitdepth) * 31) + this.chromaBitdepth;
    L6:
        return this.hashCode;
    }

    public boolean isBitdepthValid() {
        if (this.lumaBitdepth != (-1)) goto L5;
    L7:
        return false;
    L5:
        if (this.chromaBitdepth == (-1)) goto L7;
        return true;
    }

    public boolean isDataSpaceValid() {
        if (this.colorSpace != (-1)) goto L5;
    L9:
        return false;
    L5:
        if (this.colorRange == (-1)) goto L9;
        if (this.colorTransfer == (-1)) goto L9;
        return true;
    }

    public boolean isValid() {
        if (isBitdepthValid() == false) goto L5;
    L8:
        return true;
    L5:
        if (isDataSpaceValid() == true) goto L8;
        return false;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_COLOR_SPACE, this.colorSpace);
        r0.putInt(FIELD_COLOR_RANGE, this.colorRange);
        r0.putInt(FIELD_COLOR_TRANSFER, this.colorTransfer);
        r0.putByteArray(FIELD_HDR_STATIC_INFO, this.hdrStaticInfo);
        r0.putInt(FIELD_LUMA_BITDEPTH, this.lumaBitdepth);
        r0.putInt(FIELD_CHROMA_BITDEPTH, this.chromaBitdepth);
        return r0;
    }

    public String toLogString() {
        if (isDataSpaceValid() == false) goto L5;
        String r0 = Util.formatInvariant("%s/%s/%s", new Object[]{colorSpaceToString(this.colorSpace), colorRangeToString(this.colorRange), colorTransferToString(this.colorTransfer)});
    L7:
        if (isBitdepthValid() == false) goto L9;
        String r1 = this.lumaBitdepth + "/" + this.chromaBitdepth;
    L11:
        return r0 + "/" + r1;
    L9:
        r1 = "NA/NA";
        goto L11
    L5:
        r0 = "NA/NA/NA";
        goto L7
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append("ColorInfo(").append(colorSpaceToString(this.colorSpace)).append(", ").append(colorRangeToString(this.colorRange)).append(", ").append(colorTransferToString(this.colorTransfer)).append(", ");
        if (this.hdrStaticInfo == null) goto L5;
        boolean r2 = true;
    L7:
        return r0.append(r2).append(", ").append(lumaBitdepthToString(this.lumaBitdepth)).append(", ").append(chromaBitdepthToString(this.chromaBitdepth)).append(")").toString();
    L5:
        r2 = false;
        goto L7
    }
}
