package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.PositionHolder;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class PsDurationReader {
    private static final String TAG = "PsDurationReader";
    private static final int TIMESTAMP_SEARCH_BYTES = 20000;
    private long durationUs;
    private long firstScrValue;
    private boolean isDurationRead;
    private boolean isFirstScrValueRead;
    private boolean isLastScrValueRead;
    private long lastScrValue;
    private final ParsableByteArray packetBuffer;
    private final TimestampAdjuster scrTimestampAdjuster;

    PsDurationReader() {
        this.scrTimestampAdjuster = new TimestampAdjuster(0);
        this.firstScrValue = C0180C.TIME_UNSET;
        this.lastScrValue = C0180C.TIME_UNSET;
        this.durationUs = C0180C.TIME_UNSET;
        this.packetBuffer = new ParsableByteArray();
    }

    private static boolean checkMarkerBits(byte[] r4) {
        if ((r4[0] & 196) == 68) goto L6;
        return false;
    L6:
        if ((r4[2] & 4) == 4) goto L9;
        return false;
    L9:
        if ((r4[4] & 4) == 4) goto L12;
        return false;
    L12:
        if ((r4[5] & 1) == 1) goto L15;
        return false;
    L15:
        if ((r4[8] & 3) == 3) goto L17;
        return false;
    L17:
        return true;
    }

    private int finishReadDuration(ExtractorInput r3) {
        this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        this.isDurationRead = true;
        r3.resetPeekPosition();
        return 0;
    }

    private int peekIntAtPosition(byte[] r3, int r4) {
        return ((((r3[r4] & 255) << 24) | ((r3[r4 + 1] & 255) << 16)) | ((r3[r4 + 2] & 255) << 8)) | (r3[r4 + 3] & 255);
    }

    private int readFirstScrValue(ExtractorInput r8, PositionHolder r9) throws IOException {
        int r0 = (int) Math.min(20000, r8.getLength());
        if (r8.getPosition() == 0) goto L6;
        r9.position = 0;
        return 1;
    L6:
        this.packetBuffer.reset(r0);
        r8.resetPeekPosition();
        r8.peekFully(this.packetBuffer.getData(), 0, r0);
        this.firstScrValue = readFirstScrValueFromBuffer(this.packetBuffer);
        this.isFirstScrValueRead = true;
        return 0;
    }

    private long readFirstScrValueFromBuffer(ParsableByteArray r9) {
        int r0 = r9.getPosition();
        int r1 = r9.limit();
        int r2 = r0;
    L4:
        if (r2 >= (r1 - 3)) goto L11;
        if (peekIntAtPosition(r9.getData(), r2) != 442) goto L10;
        r9.setPosition(r2 + 4);
        long r6 = readScrValueFromPack(r9);
        if (r6 == C0180C.TIME_UNSET) goto L10;
        return r6;
    L10:
        r2 = r2 + 1;
        goto L4
    L11:
        return C0180C.TIME_UNSET;
    }

    private int readLastScrValue(ExtractorInput r11, PositionHolder r12) throws IOException {
        long r0 = r11.getLength();
        int r2 = (int) Math.min(20000, r0);
        long r3 = r0 - ((long) r2);
        if (r11.getPosition() == r3) goto L6;
        r12.position = r3;
        return 1;
    L6:
        this.packetBuffer.reset(r2);
        r11.resetPeekPosition();
        r11.peekFully(this.packetBuffer.getData(), 0, r2);
        this.lastScrValue = readLastScrValueFromBuffer(this.packetBuffer);
        this.isLastScrValueRead = true;
        return 0;
    }

    private long readLastScrValueFromBuffer(ParsableByteArray r9) {
        int r0 = r9.getPosition();
        int r2 = r9.limit() - 4;
    L4:
        if (r2 < r0) goto L11;
        if (peekIntAtPosition(r9.getData(), r2) != 442) goto L10;
        r9.setPosition(r2 + 4);
        long r6 = readScrValueFromPack(r9);
        if (r6 == C0180C.TIME_UNSET) goto L10;
        return r6;
    L10:
        r2 = r2 - 1;
        goto L4
    L11:
        return C0180C.TIME_UNSET;
    }

    public static long readScrValueFromPack(ParsableByteArray r6) {
        int r0 = r6.getPosition();
        if (r6.bytesLeft() >= 9) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        byte[] r1 = new byte[9];
        r6.readBytes(r1, 0, r1.length);
        r6.setPosition(r0);
        if (checkMarkerBits(r1) == true) goto L9;
        return C0180C.TIME_UNSET;
    L9:
        return readScrValueFromPackHeader(r1);
    }

    private static long readScrValueFromPackHeader(byte[] r13) {
        return ((((((((((long) r13[0]) & 56) >> 3) << 30) | ((((long) r13[0]) & 3) << 28)) | ((((long) r13[1]) & 255) << 20)) | (((((long) r13[2]) & 248) >> 3) << 15)) | ((((long) r13[2]) & 3) << 13)) | ((((long) r13[3]) & 255) << 5)) | ((((long) r13[4]) & 248) >> 3);
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public TimestampAdjuster getScrTimestampAdjuster() {
        return this.scrTimestampAdjuster;
    }

    public boolean isDurationReadFinished() {
        return this.isDurationRead;
    }

    public int readDuration(ExtractorInput r7, PositionHolder r8) throws IOException {
        if (this.isLastScrValueRead == true) goto L7;
        return readLastScrValue(r7, r8);
    L7:
        if (this.lastScrValue != C0180C.TIME_UNSET) goto L11;
        return finishReadDuration(r7);
    L11:
        if (this.isFirstScrValueRead == true) goto L15;
        return readFirstScrValue(r7, r8);
    L15:
        if (this.firstScrValue == C0180C.TIME_UNSET) goto L17;
        long r0 = this.scrTimestampAdjuster.adjustTsTimestamp(this.firstScrValue);
        this.durationUs = this.scrTimestampAdjuster.adjustTsTimestampGreaterThanPreviousTimestamp(this.lastScrValue) - r0;
        return finishReadDuration(r7);
    L17:
        return finishReadDuration(r7);
    }
}
