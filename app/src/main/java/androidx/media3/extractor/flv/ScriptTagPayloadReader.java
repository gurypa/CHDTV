package androidx.media3.extractor.flv;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.DiscardingTrackOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class ScriptTagPayloadReader extends TagPayloadReader {
    private static final int AMF_TYPE_BOOLEAN = 1;
    private static final int AMF_TYPE_DATE = 11;
    private static final int AMF_TYPE_ECMA_ARRAY = 8;
    private static final int AMF_TYPE_END_MARKER = 9;
    private static final int AMF_TYPE_NUMBER = 0;
    private static final int AMF_TYPE_OBJECT = 3;
    private static final int AMF_TYPE_STRICT_ARRAY = 10;
    private static final int AMF_TYPE_STRING = 2;
    private static final String KEY_DURATION = "duration";
    private static final String KEY_FILE_POSITIONS = "filepositions";
    private static final String KEY_KEY_FRAMES = "keyframes";
    private static final String KEY_TIMES = "times";
    private static final String NAME_METADATA = "onMetaData";
    private long durationUs;
    private long[] keyFrameTagPositions;
    private long[] keyFrameTimesUs;

    public ScriptTagPayloadReader() {
        super(new DiscardingTrackOutput());
        this.durationUs = C0180C.TIME_UNSET;
        this.keyFrameTimesUs = new long[0];
        this.keyFrameTagPositions = new long[0];
    }

    private static Boolean readAmfBoolean(ParsableByteArray r2) {
        boolean r1 = true;
        if (r2.readUnsignedByte() == 1) goto L7;
        r1 = false;
    L7:
        return Boolean.valueOf(r1);
    }

    private static Object readAmfData(ParsableByteArray r1, int r2) {
        switch(r2) {
            case 0: goto L18;
            case 1: goto L16;
            case 2: goto L14;
            case 3: goto L12;
            case 4: goto L3;
            case 5: goto L3;
            case 6: goto L3;
            case 7: goto L3;
            case 8: goto L10;
            case 9: goto L3;
            case 10: goto L8;
            case 11: goto L6;
            default: goto L3;
        };
    L3:
        return null;
    L6:
        return readAmfDate(r1);
    L8:
        return readAmfStrictArray(r1);
    L10:
        return readAmfEcmaArray(r1);
    L12:
        return readAmfObject(r1);
    L14:
        return readAmfString(r1);
    L16:
        return readAmfBoolean(r1);
    L18:
        return readAmfDouble(r1);
    }

    private static Date readAmfDate(ParsableByteArray r3) {
        Date r0 = new Date((long) readAmfDouble(r3).doubleValue());
        r3.skipBytes(2);
        return r0;
    }

    private static Double readAmfDouble(ParsableByteArray r2) {
        return Double.valueOf(Double.longBitsToDouble(r2.readLong()));
    }

    private static HashMap<String, Object> readAmfEcmaArray(ParsableByteArray r6) {
        int r0 = r6.readUnsignedIntToInt();
        HashMap<String, Object> r1 = new HashMap(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        String r3 = readAmfString(r6);
        Object r5 = readAmfData(r6, readAmfType(r6));
        if (r5 == null) goto L7;
        r1.put(r3, r5);
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        return r1;
    }

    private static HashMap<String, Object> readAmfObject(ParsableByteArray r4) {
        HashMap<String, Object> r0 = new HashMap();
    L3:
        String r1 = readAmfString(r4);
        int r2 = readAmfType(r4);
        if (r2 == 9) goto L5;
        Object r3 = readAmfData(r4, r2);
        if (r3 == null) goto L3;
        r0.put(r1, r3);
        goto L3
    L5:
        return r0;
    }

    private static ArrayList<Object> readAmfStrictArray(ParsableByteArray r5) {
        int r0 = r5.readUnsignedIntToInt();
        ArrayList<Object> r1 = new ArrayList(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        Object r4 = readAmfData(r5, readAmfType(r5));
        if (r4 == null) goto L7;
        r1.add(r4);
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        return r1;
    }

    private static String readAmfString(ParsableByteArray r4) {
        int r0 = r4.readUnsignedShort();
        int r1 = r4.getPosition();
        r4.skipBytes(r0);
        return new String(r4.getData(), r1, r0);
    }

    private static int readAmfType(ParsableByteArray r1) {
        return r1.readUnsignedByte();
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public long[] getKeyFrameTagPositions() {
        return this.keyFrameTagPositions;
    }

    public long[] getKeyFrameTimesUs() {
        return this.keyFrameTimesUs;
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray r2) {
        return true;
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    protected boolean parsePayload(ParsableByteArray r25, long r26) {
        int r1 = readAmfType(r25);
        if (r1 == 2) goto L5;
        return false;
    L5:
        String r2 = readAmfString(r25);
        if (NAME_METADATA.equals(r2) == true) goto L9;
        return false;
    L9:
        if (r25.bytesLeft() != 0) goto L12;
        return false;
    L12:
        if (readAmfType(r25) == 8) goto L14;
        return false;
    L14:
        HashMap<String, Object> r5 = readAmfEcmaArray(r25);
        Object r6 = r5.get("duration");
        if ((r6 instanceof Double) == false) goto L19;
        double r10 = ((Double) r6).doubleValue();
        if (r10 <= 0.0d) goto L19;
        this.durationUs = (long) (r10 * 1000000.0d);
    L19:
        Object r7 = r5.get(KEY_KEY_FRAMES);
        if ((r7 instanceof Map) == false) goto L36;
        Map r11 = (Map) r7;
        Object r12 = r11.get(KEY_FILE_POSITIONS);
        Object r13 = r11.get(KEY_TIMES);
        if ((r12 instanceof List) == false) goto L43;
        if ((r13 instanceof List) == false) goto L43;
        List r14 = (List) r12;
        List r15 = (List) r13;
        int r16 = r15.size();
        this.keyFrameTimesUs = new long[r16];
        this.keyFrameTagPositions = new long[r16];
        int r8 = 0;
    L26:
        if (r8 >= r16) goto L42;
        Object r9 = r14.get(r8);
        Object r3 = r15.get(r8);
        int r19 = r1;
        if ((r3 instanceof Double) == false) goto L32;
        if ((r9 instanceof Double) == false) goto L32;
        String r22 = r2;
        this.keyFrameTimesUs[r8] = (long) (((Double) r3).doubleValue() * 1000000.0d);
        this.keyFrameTagPositions[r8] = ((Double) r9).longValue();
        r8 = r8 + 1;
        r1 = r19;
        r2 = r22;
    L32:
        this.keyFrameTimesUs = new long[0];
        this.keyFrameTagPositions = new long[0];
        return false;
    L42:
        return false;
    L43:
        return false;
    L36:
        return false;
    }

    @Override // androidx.media3.extractor.flv.TagPayloadReader
    public void seek() {
    }
}
