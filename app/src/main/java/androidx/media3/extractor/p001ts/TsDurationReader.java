package androidx.media3.extractor.p001ts;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.PositionHolder;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
final class TsDurationReader {
    private static final String TAG = "TsDurationReader";
    private long durationUs;
    private long firstPcrValue;
    private boolean isDurationRead;
    private boolean isFirstPcrValueRead;
    private boolean isLastPcrValueRead;
    private long lastPcrValue;
    private final ParsableByteArray packetBuffer;
    private final TimestampAdjuster pcrTimestampAdjuster;
    private final int timestampSearchBytes;

    TsDurationReader(int r4) {
        this.timestampSearchBytes = r4;
        this.pcrTimestampAdjuster = new TimestampAdjuster(0);
        this.firstPcrValue = C0180C.TIME_UNSET;
        this.lastPcrValue = C0180C.TIME_UNSET;
        this.durationUs = C0180C.TIME_UNSET;
        this.packetBuffer = new ParsableByteArray();
    }

    private int finishReadDuration(ExtractorInput r3) {
        this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
        this.isDurationRead = true;
        r3.resetPeekPosition();
        return 0;
    }

    private int readFirstPcrValue(ExtractorInput r8, PositionHolder r9, int r10) throws IOException {
        int r0 = (int) Math.min(this.timestampSearchBytes, r8.getLength());
        if (r8.getPosition() == 0) goto L6;
        r9.position = 0;
        return 1;
    L6:
        this.packetBuffer.reset(r0);
        r8.resetPeekPosition();
        r8.peekFully(this.packetBuffer.getData(), 0, r0);
        this.firstPcrValue = readFirstPcrValueFromBuffer(this.packetBuffer, r10);
        this.isFirstPcrValueRead = true;
        return 0;
    }

    private long readFirstPcrValueFromBuffer(ParsableByteArray r8, int r9) {
        int r0 = r8.getPosition();
        int r1 = r8.limit();
        int r2 = r0;
    L4:
        if (r2 >= r1) goto L12;
        if (r8.getData()[r2] != 71) goto L11;
        long r5 = TsUtil.readPcrFromPacket(r8, r2, r9);
        if (r5 == C0180C.TIME_UNSET) goto L11;
        return r5;
    L11:
        r2 = r2 + 1;
        goto L4
    L12:
        return C0180C.TIME_UNSET;
    }

    private int readLastPcrValue(ExtractorInput r11, PositionHolder r12, int r13) throws IOException {
        long r0 = r11.getLength();
        int r2 = (int) Math.min(this.timestampSearchBytes, r0);
        long r3 = r0 - ((long) r2);
        if (r11.getPosition() == r3) goto L6;
        r12.position = r3;
        return 1;
    L6:
        this.packetBuffer.reset(r2);
        r11.resetPeekPosition();
        r11.peekFully(this.packetBuffer.getData(), 0, r2);
        this.lastPcrValue = readLastPcrValueFromBuffer(this.packetBuffer, r13);
        this.isLastPcrValueRead = true;
        return 0;
    }

    private long readLastPcrValueFromBuffer(ParsableByteArray r8, int r9) {
        int r0 = r8.getPosition();
        int r1 = r8.limit();
        int r2 = r1 - 188;
    L4:
        if (r2 < r0) goto L12;
        if (TsUtil.isStartOfTsPacket(r8.getData(), r0, r1, r2) == false) goto L11;
        long r5 = TsUtil.readPcrFromPacket(r8, r2, r9);
        if (r5 == C0180C.TIME_UNSET) goto L11;
        return r5;
    L11:
        r2 = r2 - 1;
        goto L4
    L12:
        return C0180C.TIME_UNSET;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public TimestampAdjuster getPcrTimestampAdjuster() {
        return this.pcrTimestampAdjuster;
    }

    public boolean isDurationReadFinished() {
        return this.isDurationRead;
    }

    public int readDuration(ExtractorInput r7, PositionHolder r8, int r9) throws IOException {
        if (r9 > 0) goto L6;
        return finishReadDuration(r7);
    L6:
        if (this.isLastPcrValueRead == true) goto L10;
        return readLastPcrValue(r7, r8, r9);
    L10:
        if (this.lastPcrValue != C0180C.TIME_UNSET) goto L14;
        return finishReadDuration(r7);
    L14:
        if (this.isFirstPcrValueRead == true) goto L18;
        return readFirstPcrValue(r7, r8, r9);
    L18:
        if (this.firstPcrValue == C0180C.TIME_UNSET) goto L20;
        long r0 = this.pcrTimestampAdjuster.adjustTsTimestamp(this.firstPcrValue);
        this.durationUs = this.pcrTimestampAdjuster.adjustTsTimestampGreaterThanPreviousTimestamp(this.lastPcrValue) - r0;
        return finishReadDuration(r7);
    L20:
        return finishReadDuration(r7);
    }
}
