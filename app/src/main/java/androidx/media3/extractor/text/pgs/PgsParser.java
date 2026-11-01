package androidx.media3.extractor.text.pgs;

import android.graphics.Bitmap;
import androidx.media3.common.C0180C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.text.CuesWithTiming;
import androidx.media3.extractor.text.Subtitle;
import androidx.media3.extractor.text.SubtitleParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

/* JADX INFO: loaded from: classes.dex */
public final class PgsParser implements SubtitleParser {
    public static final int CUE_REPLACEMENT_BEHAVIOR = 2;
    private static final byte INFLATE_HEADER = 120;
    private static final int SECTION_TYPE_BITMAP_PICTURE = 21;
    private static final int SECTION_TYPE_END = 128;
    private static final int SECTION_TYPE_IDENTIFIER = 22;
    private static final int SECTION_TYPE_PALETTE = 20;
    private final ParsableByteArray buffer;
    private final CueBuilder cueBuilder;
    private final ParsableByteArray inflatedBuffer;
    private Inflater inflater;

    private static final class CueBuilder {
        private final ParsableByteArray bitmapData;
        private int bitmapHeight;
        private int bitmapWidth;
        private int bitmapX;
        private int bitmapY;
        private final int[] colors;
        private boolean colorsSet;
        private int planeHeight;
        private int planeWidth;

        public CueBuilder() {
            this.bitmapData = new ParsableByteArray();
            this.colors = new int[256];
        }

        static /* synthetic */ void access$000(CueBuilder r0, ParsableByteArray r1, int r2) {
            r0.parsePaletteSection(r1, r2);
        }

        static /* synthetic */ void access$100(CueBuilder r0, ParsableByteArray r1, int r2) {
            r0.parseBitmapSection(r1, r2);
        }

        static /* synthetic */ void access$200(CueBuilder r0, ParsableByteArray r1, int r2) {
            r0.parseIdentifierSection(r1, r2);
        }

        private void parseBitmapSection(ParsableByteArray r7, int r8) {
            if (r8 >= 4) goto L5;
            return;
        L5:
            r7.skipBytes(3);
            if ((r7.readUnsignedByte() & 128) == 0) goto L8;
            boolean r1 = true;
        L9:
            int r9 = r8 - 4;
            if (r1 == true) goto L12;
        L18:
            int r0 = this.bitmapData.getPosition();
            int r2 = this.bitmapData.limit();
            if (r0 >= r2) goto L23;
            if (r9 <= 0) goto L24;
            int r3 = Math.min(r9, r2 - r0);
            r7.readBytes(this.bitmapData.getData(), r0, r3);
            this.bitmapData.setPosition(r0 + r3);
            return;
        L24:
            return;
        L23:
            return;
        L12:
            if (r9 >= 7) goto L14;
            return;
        L14:
            int r4 = r7.readUnsignedInt24();
            if (r4 >= 4) goto L17;
            return;
        L17:
            this.bitmapWidth = r7.readUnsignedShort();
            this.bitmapHeight = r7.readUnsignedShort();
            this.bitmapData.reset(r4 - 4);
            r9 = r9 - 7;
            goto L18
        L8:
            r1 = false;
            goto L9
        }

        private void parseIdentifierSection(ParsableByteArray r2, int r3) {
            if (r3 >= 19) goto L5;
            return;
        L5:
            this.planeWidth = r2.readUnsignedShort();
            this.planeHeight = r2.readUnsignedShort();
            r2.skipBytes(11);
            this.bitmapX = r2.readUnsignedShort();
            this.bitmapY = r2.readUnsignedShort();
        }

        private void parsePaletteSection(ParsableByteArray r19, int r20) {
            if ((r20 % 5) == 2) goto L5;
            return;
        L5:
            r19.skipBytes(2);
            Arrays.fill(this.colors, 0);
            int r2 = r20 / 5;
            int r4 = 0;
        L6:
            if (r4 >= r2) goto L8;
            int r5 = r19.readUnsignedByte();
            int r6 = r19.readUnsignedByte();
            int r7 = r19.readUnsignedByte();
            int r8 = r19.readUnsignedByte();
            this.colors[r5] = (((r19.readUnsignedByte() << 24) | (Util.constrainValue((int) (((double) r6) + (((double) (r7 - 128)) * 1.402d)), 0, 255) << 16)) | (Util.constrainValue((int) ((((double) r6) - (((double) (r8 - 128)) * 0.34414d)) - (((double) (r7 - 128)) * 0.71414d)), 0, 255) << 8)) | Util.constrainValue((int) (((double) r6) + (((double) (r8 - 128)) * 1.772d)), 0, 255);
            r4 = r4 + 1;
            goto L6
        L8:
            this.colorsSet = true;
        }

        public Cue build() {
            if (this.planeWidth != 0) goto L5;
            return null;
        L5:
            if (this.planeHeight != 0) goto L7;
            return null;
        L7:
            if (this.bitmapWidth != 0) goto L9;
            return null;
        L9:
            if (this.bitmapHeight != 0) goto L11;
            return null;
        L11:
            if (this.bitmapData.limit() != 0) goto L13;
            return null;
        L13:
            if (this.bitmapData.getPosition() == this.bitmapData.limit()) goto L15;
            return null;
        L15:
            if (this.colorsSet == false) goto L52;
            this.bitmapData.setPosition(0);
            int[] r0 = new int[this.bitmapWidth * this.bitmapHeight];
            int r2 = 0;
        L19:
            if (r2 >= r0.length) goto L36;
            int r3 = this.bitmapData.readUnsignedByte();
            if (r3 != 0) goto L22;
            int r4 = this.bitmapData.readUnsignedByte();
            if (r4 == 0) goto L19;
            if ((r4 & 64) != 0) goto L28;
            int r5 = r4 & 63;
        L30:
            if ((r4 & 128) != 0) goto L32;
            int r6 = this.colors[0];
        L33:
            Arrays.fill(r0, r2, r2 + r5, r6);
            r2 = r2 + r5;
            goto L19
        L32:
            r6 = this.colors[this.bitmapData.readUnsignedByte()];
            goto L33
        L28:
            r5 = ((r4 & 63) << 8) | this.bitmapData.readUnsignedByte();
            goto L30
        L22:
            r0[r2] = this.colors[r3];
            r2 = r2 + 1;
            goto L19
        L36:
            return new Cue.Builder().setBitmap(Bitmap.createBitmap(r0, this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888)).setPosition(this.bitmapX / this.planeWidth).setPositionAnchor(0).setLine(this.bitmapY / this.planeHeight, 0).setLineAnchor(0).setSize(this.bitmapWidth / this.planeWidth).setBitmapHeight(this.bitmapHeight / this.planeHeight).build();
        L52:
            return null;
        }

        public void reset() {
            this.planeWidth = 0;
            this.planeHeight = 0;
            this.bitmapX = 0;
            this.bitmapY = 0;
            this.bitmapWidth = 0;
            this.bitmapHeight = 0;
            this.bitmapData.reset(0);
            this.colorsSet = false;
        }
    }

    public PgsParser() {
        this.buffer = new ParsableByteArray();
        this.inflatedBuffer = new ParsableByteArray();
        this.cueBuilder = new CueBuilder();
    }

    private void maybeInflateData(ParsableByteArray r3) {
        if (r3.bytesLeft() > 0) goto L5;
        return;
    L5:
        if (r3.peekUnsignedByte() == 120) goto L7;
        return;
    L7:
        if (this.inflater != null) goto L10;
        this.inflater = new Inflater();
    L10:
        if (Util.inflate(r3, this.inflatedBuffer, this.inflater) == false) goto L15;
        r3.reset(this.inflatedBuffer.getData(), this.inflatedBuffer.limit());
        return;
    }

    private static Cue readNextSection(ParsableByteArray r5, CueBuilder r6) {
        int r0 = r5.limit();
        int r1 = r5.readUnsignedByte();
        int r2 = r5.readUnsignedShort();
        int r3 = r5.getPosition() + r2;
        if (r3 <= r0) goto L6;
        r5.setPosition(r0);
        return null;
    L6:
        Cue r4 = null;
        switch(r1) {
            case 20: goto L12;
            case 21: goto L11;
            case 22: goto L10;
            case 128: goto L9;
            default: goto L13;
        };
    L9:
        r4 = r6.build();
        r6.reset();
        goto L13
    L10:
        CueBuilder.access$200(r6, r5, r2);
        goto L13
    L11:
        CueBuilder.access$100(r6, r5, r2);
        goto L13
    L12:
        CueBuilder.access$000(r6, r5, r2);
    L13:
        r5.setPosition(r3);
        return r4;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public int getCueReplacementBehavior() {
        return 2;
    }

    @Override // androidx.media3.extractor.text.SubtitleParser
    public void parse(byte[] r8, int r9, int r10, SubtitleParser.OutputOptions r11, Consumer<CuesWithTiming> r12) {
        this.buffer.reset(r8, r9 + r10);
        this.buffer.setPosition(r9);
        maybeInflateData(this.buffer);
        this.cueBuilder.reset();
        ArrayList r0 = new ArrayList();
    L4:
        if (this.buffer.bytesLeft() < 3) goto L9;
        Cue r1 = readNextSection(this.buffer, this.cueBuilder);
        if (r1 == null) goto L4;
        r0.add(r1);
        goto L4
    L9:
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
