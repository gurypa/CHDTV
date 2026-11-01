package androidx.media3.extractor.mp4;

import androidx.media3.common.Metadata;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.metadata.mp4.SlowMotionData;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class SefReader {
    private static final Splitter ASTERISK_SPLITTER = null;
    private static final Splitter COLON_SPLITTER = null;
    private static final int LENGTH_OF_ONE_SDR = 12;
    private static final int SAMSUNG_TAIL_SIGNATURE = 1397048916;
    private static final int STATE_CHECKING_FOR_SEF = 1;
    private static final int STATE_READING_SDRS = 2;
    private static final int STATE_READING_SEF_DATA = 3;
    private static final int STATE_SHOULD_CHECK_FOR_SEF = 0;
    private static final String TAG = "SefReader";
    private static final int TAIL_FOOTER_LENGTH = 8;
    private static final int TAIL_HEADER_LENGTH = 12;
    private static final int TYPE_SLOW_MOTION_DATA = 2192;
    private static final int TYPE_SUPER_SLOW_DEFLICKERING_ON = 2820;
    private static final int TYPE_SUPER_SLOW_MOTION_BGM = 2817;
    private static final int TYPE_SUPER_SLOW_MOTION_DATA = 2816;
    private static final int TYPE_SUPER_SLOW_MOTION_EDIT_DATA = 2819;
    private final List<DataReference> dataReferences;
    private int readerState;
    private int tailLength;

    private static final class DataReference {
        public final int dataType;
        public final int size;
        public final long startOffset;

        public DataReference(int r1, long r2, int r4) {
            this.dataType = r1;
            this.startOffset = r2;
            this.size = r4;
        }
    }

    static {
        COLON_SPLITTER = Splitter.m190on(':');
        ASTERISK_SPLITTER = Splitter.m190on('*');
    }

    public SefReader() {
        this.dataReferences = new ArrayList();
        this.readerState = 0;
    }

    private void checkForSefData(ExtractorInput r6, PositionHolder r7) throws IOException {
        ParsableByteArray r0 = new ParsableByteArray(8);
        r6.readFully(r0.getData(), 0, 8);
        this.tailLength = r0.readLittleEndianInt() + 8;
        if (r0.readInt() == SAMSUNG_TAIL_SIGNATURE) goto L6;
        r7.position = 0;
        return;
    L6:
        r7.position = r6.getPosition() - ((long) (this.tailLength - 12));
        this.readerState = 2;
    }

    private static int nameToDataType(String r2) throws ParserException {
        switch(r2.hashCode()) {
            case -1711564334: goto L18;
            case -1332107749: goto L15;
            case -1251387154: goto L12;
            case -830665521: goto L9;
            case 1760745220: goto L6;
            default: goto L20;
        };
    L20:
        byte r0 = -1;
    L21:
        switch(r0) {
            case 0: goto L32;
            case 1: goto L30;
            case 2: goto L28;
            case 3: goto L26;
            case 4: goto L24;
            default: goto L23;
        };
    L24:
        return TYPE_SUPER_SLOW_DEFLICKERING_ON;
    L26:
        return TYPE_SUPER_SLOW_MOTION_EDIT_DATA;
    L28:
        return TYPE_SUPER_SLOW_MOTION_BGM;
    L30:
        return TYPE_SUPER_SLOW_MOTION_DATA;
    L32:
        return TYPE_SLOW_MOTION_DATA;
    L23:
        throw ParserException.createForMalformedContainer("Invalid SEF name", null);
    L6:
        if (r2.equals("Super_SlowMotion_BGM") == false) goto L20;
        r0 = 2;
        goto L21
    L9:
        if (r2.equals("Super_SlowMotion_Deflickering_On") == false) goto L20;
        r0 = 4;
        goto L21
    L12:
        if (r2.equals("Super_SlowMotion_Data") == false) goto L20;
        r0 = 1;
        goto L21
    L15:
        if (r2.equals("Super_SlowMotion_Edit_Data") == false) goto L20;
        r0 = 3;
        goto L21
    L18:
        if (r2.equals("SlowMotion_Data") == false) goto L20;
        r0 = 0;
        goto L21
    }

    private void readSdrs(ExtractorInput r14, PositionHolder r15) throws IOException {
        long r0 = r14.getLength();
        int r2 = (this.tailLength - 12) - 8;
        ParsableByteArray r4 = new ParsableByteArray(r2);
        r14.readFully(r4.getData(), 0, r2);
        int r5 = 0;
    L4:
        if (r5 >= (r2 / 12)) goto L11;
        r4.skipBytes(2);
        short r7 = r4.readLittleEndianShort();
        switch(r7) {
            case 2192: goto L8;
            case 2816: goto L8;
            case 2817: goto L8;
            case 2819: goto L8;
            case 2820: goto L8;
            default: goto L7;
        };
    L7:
        r4.skipBytes(8);
    L9:
        r5 = r5 + 1;
        goto L4
    L8:
        this.dataReferences.add(new DataReference(r7, (r0 - ((long) this.tailLength)) - ((long) r4.readLittleEndianInt()), r4.readLittleEndianInt()));
        goto L9
    L11:
        if (this.dataReferences.isEmpty() == false) goto L14;
        r15.position = 0;
        return;
    L14:
        this.readerState = 3;
        r15.position = this.dataReferences.get(0).startOffset;
    }

    private void readSefData(ExtractorInput r13, List<Metadata.Entry> r14) throws IOException {
        long r0 = r13.getPosition();
        int r2 = (int) ((r13.getLength() - r13.getPosition()) - ((long) this.tailLength));
        ParsableByteArray r3 = new ParsableByteArray(r2);
        r13.readFully(r3.getData(), 0, r2);
        int r4 = 0;
    L4:
        if (r4 >= this.dataReferences.size()) goto L12;
        DataReference r5 = this.dataReferences.get(r4);
        r3.setPosition((int) (r5.startOffset - r0));
        r3.skipBytes(4);
        int r7 = r3.readLittleEndianInt();
        int r9 = nameToDataType(r3.readString(r7));
        int r10 = r5.size - (r7 + 8);
        switch(r9) {
            case 2192: goto L10;
            case 2816: goto L11;
            case 2817: goto L11;
            case 2819: goto L11;
            case 2820: goto L11;
            default: goto L8;
        };
    L10:
        r14.add(readSlowMotionData(r3, r10));
        goto L11
    L8:
        throw new IllegalStateException();
    L11:
        r4 = r4 + 1;
        goto L4
    }

    private static SlowMotionData readSlowMotionData(ParsableByteArray r16, int r17) throws ParserException {
        ArrayList r0 = new ArrayList();
        List<String> r3 = ASTERISK_SPLITTER.splitToList(r16.readString(r17));
        int r4 = 0;
    L4:
        if (r4 >= r3.size()) goto L16;
        List<String> r5 = COLON_SPLITTER.splitToList(r3.get(r4));
        if (r5.size() != 3) goto L14;
        r0.add(new SlowMotionData.Segment(Long.parseLong(r5.get(0)), Long.parseLong(r5.get(1)), 1 << (Integer.parseInt(r5.get(2)) - 1)));     // Catch: NumberFormatException -> L10
        r4 = r4 + 1;
    L10:
        e = move-exception;
        throw ParserException.createForMalformedContainer(null, e);
    L14:
        throw ParserException.createForMalformedContainer(null, null);
    L16:
        return new SlowMotionData(r0);
    }

    public int read(ExtractorInput r9, PositionHolder r10, List<Metadata.Entry> r11) throws IOException {
        long r2 = 0;
        switch(this.readerState) {
            case 0: goto L9;
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            default: goto L5;
        };
    L6:
        readSefData(r9, r11);
        r10.position = 0;
    L17:
        return 1;
    L7:
        readSdrs(r9, r10);
        goto L17
    L8:
        checkForSefData(r9, r10);
        goto L17
    L9:
        long r4 = r9.getLength();
        if (r4 != (-1)) goto L12;
    L16:
        r10.position = r2;
        this.readerState = 1;
        goto L17
    L12:
        if (r4 < 8) goto L16;
        r2 = r4 - 8;
        goto L16
    L5:
        throw new IllegalStateException();
    }

    public void reset() {
        this.dataReferences.clear();
        this.readerState = 0;
    }
}
