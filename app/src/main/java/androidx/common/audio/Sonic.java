package androidx.media3.common.audio;

import androidx.media3.common.util.Assertions;
import java.nio.ShortBuffer;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
final class Sonic {
    private static final int AMDF_FREQUENCY = 4000;
    private static final int BYTES_PER_SAMPLE = 2;
    private static final int MAXIMUM_PITCH = 400;
    private static final int MINIMUM_PITCH = 65;
    private double accumulatedSpeedAdjustmentError;
    private final int channelCount;
    private final short[] downSampleBuffer;
    private short[] inputBuffer;
    private int inputFrameCount;
    private final int inputSampleRateHz;
    private int maxDiff;
    private final int maxPeriod;
    private final int maxRequiredFrameCount;
    private int minDiff;
    private final int minPeriod;
    private int newRatePosition;
    private int oldRatePosition;
    private short[] outputBuffer;
    private int outputFrameCount;
    private final float pitch;
    private short[] pitchBuffer;
    private int pitchFrameCount;
    private int prevMinDiff;
    private int prevPeriod;
    private final float rate;
    private int remainingInputToCopyFrameCount;
    private final float speed;

    public Sonic(int r3, int r4, float r5, float r6, int r7) {
        this.inputSampleRateHz = r3;
        this.channelCount = r4;
        this.speed = r5;
        this.pitch = r6;
        this.rate = r3 / r7;
        this.minPeriod = r3 / MAXIMUM_PITCH;
        this.maxPeriod = r3 / MINIMUM_PITCH;
        this.maxRequiredFrameCount = this.maxPeriod * 2;
        this.downSampleBuffer = new short[this.maxRequiredFrameCount];
        this.inputBuffer = new short[this.maxRequiredFrameCount * r4];
        this.outputBuffer = new short[this.maxRequiredFrameCount * r4];
        this.pitchBuffer = new short[this.maxRequiredFrameCount * r4];
    }

    private void adjustRate(float r13, int r14) {
        if (this.outputFrameCount != r14) goto L5;
        return;
    L5:
        long r9 = (long) (this.inputSampleRateHz / r13);
        long r7 = this.inputSampleRateHz;
    L7:
        if (r9 == 0) goto L15;
        if (r7 == 0) goto L15;
        if ((r9 % 2) != 0) goto L15;
        if ((r7 % 2) != 0) goto L15;
        r9 = r9 / 2;
        r7 = r7 / 2;
    L15:
        moveNewSamplesToPitchBuffer(r14);
        int r0 = 0;
    L16:
        boolean r2 = true;
        if (r0 >= (this.pitchFrameCount - 1)) goto L33;
    L19:
        if ((((long) (this.oldRatePosition + 1)) * r9) <= (((long) this.newRatePosition) * r7)) goto L25;
        this.outputBuffer = ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, 1);
        int r1 = 0;
    L22:
        if (r1 >= this.channelCount) goto L24;
        this.outputBuffer[(this.outputFrameCount * this.channelCount) + r1] = interpolate(this.pitchBuffer, (this.channelCount * r0) + r1, r7, r9);
        r1 = r1 + 1;
        goto L22
    L24:
        this.newRatePosition++;
        this.outputFrameCount++;
        goto L19
    L25:
        this.oldRatePosition++;
        if (this.oldRatePosition != r7) goto L32;
        this.oldRatePosition = 0;
        if (this.newRatePosition == r9) goto L31;
        r2 = false;
    L31:
        Assertions.checkState(r2);
        this.newRatePosition = 0;
    L32:
        r0 = r0 + 1;
        goto L16
    L33:
        removePitchFrames(this.pitchFrameCount - 1);
    }

    private void changeSpeed(double r9) {
        if (this.inputFrameCount >= this.maxRequiredFrameCount) goto L5;
        return;
    L5:
        int r0 = this.inputFrameCount;
        int r4 = 0;
    L7:
        if (this.remainingInputToCopyFrameCount <= 0) goto L9;
        r4 = r4 + copyInputToOutput(r4);
        Sonic r2 = this;
        double r5 = r9;
    L14:
        if ((r2.maxRequiredFrameCount + r4) > r0) goto L15;
        r9 = r5;
        goto L7
    L15:
        removeProcessedInputFrames(r4);
        return;
    L9:
        int r7 = findPitchPeriod(this.inputBuffer, r4);
        if (r9 <= 1.0d) goto L12;
        r2 = this;
        r5 = r9;
        r4 = r4 + (r2.skipPitchPeriod(this.inputBuffer, r4, r5, r7) + r7);
        goto L14
    L12:
        r2 = this;
        r5 = r9;
        r4 = r4 + r2.insertPitchPeriod(r2.inputBuffer, r4, r5, r7);
        goto L14
    }

    private int copyInputToOutput(int r3) {
        int r0 = Math.min(this.maxRequiredFrameCount, this.remainingInputToCopyFrameCount);
        copyToOutput(this.inputBuffer, r3, r0);
        this.remainingInputToCopyFrameCount -= r0;
        return r0;
    }

    private void copyToOutput(short[] r5, int r6, int r7) {
        this.outputBuffer = ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, r7);
        System.arraycopy(r5, this.channelCount * r6, this.outputBuffer, this.outputFrameCount * this.channelCount, this.channelCount * r7);
        this.outputFrameCount += r7;
    }

    private void downSampleInput(short[] r7, int r8, int r9) {
        int r0 = this.maxRequiredFrameCount / r9;
        int r1 = this.channelCount * r9;
        int r10 = r8 * this.channelCount;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        int r3 = 0;
        int r4 = 0;
    L5:
        if (r4 >= r1) goto L7;
        r3 = r3 + r7[((r2 * r1) + r10) + r4];
        r4 = r4 + 1;
        goto L5
    L7:
        this.downSampleBuffer[r2] = (short) (r3 / r1);
        r2 = r2 + 1;
        goto L3
    }

    private short[] ensureSpaceForAdditionalFrames(short[] r4, int r5, int r6) {
        int r0 = r4.length / this.channelCount;
        if ((r5 + r6) > r0) goto L5;
        return r4;
    L5:
        int r2 = this.channelCount;
        return Arrays.copyOf(r4, r2 * (((r0 * 3) / 2) + r6));
    }

    private int findPitchPeriod(short[] r8, int r9) {
        if (this.inputSampleRateHz <= AMDF_FREQUENCY) goto L5;
        int r0 = this.inputSampleRateHz / AMDF_FREQUENCY;
    L7:
        if (this.channelCount != 1) goto L10;
        if (r0 != 1) goto L10;
        int r1 = findPitchPeriodInRange(r8, r9, this.minPeriod, this.maxPeriod);
    L24:
        if (previousPeriodBetter(this.minDiff, this.maxDiff) == false) goto L26;
        int r2 = this.prevPeriod;
    L27:
        this.prevMinDiff = this.minDiff;
        this.prevPeriod = r1;
        return r2;
    L26:
        r2 = r1;
    L10:
        downSampleInput(r8, r9, r0);
        int r3 = findPitchPeriodInRange(this.downSampleBuffer, 0, this.minPeriod / r0, this.maxPeriod / r0);
        if (r0 == 1) goto L22;
        int r4 = r3 * r0;
        int r5 = r4 - (r0 * 4);
        int r6 = (r0 * 4) + r4;
        if (r5 >= this.minPeriod) goto L16;
        r5 = this.minPeriod;
    L16:
        if (r6 <= this.maxPeriod) goto L19;
        r6 = this.maxPeriod;
    L19:
        if (this.channelCount != 1) goto L21;
        r1 = findPitchPeriodInRange(r8, r9, r5, r6);
        goto L24
    L21:
        downSampleInput(r8, r9, 1);
        r1 = findPitchPeriodInRange(this.downSampleBuffer, 0, r5, r6);
        goto L24
    L22:
        r1 = r3;
        goto L24
    L5:
        r0 = 1;
        goto L7
    }

    private int findPitchPeriodInRange(short[] r11, int r12, int r13, int r14) {
        int r0 = 0;
        int r1 = 255;
        int r2 = 1;
        int r3 = 0;
        int r15 = r12 * this.channelCount;
        int r4 = r13;
    L3:
        if (r4 > r14) goto L14;
        int r5 = 0;
        int r6 = 0;
    L5:
        if (r6 >= r4) goto L8;
        r5 = r5 + Math.abs(r11[r15 + r6] - r11[(r15 + r4) + r6]);
        r6 = r6 + 1;
        goto L5
    L8:
        if ((r5 * r0) >= (r2 * r4)) goto L11;
        r2 = r5;
        r0 = r4;
    L11:
        if ((r5 * r1) <= (r3 * r4)) goto L13;
        r3 = r5;
        r1 = r4;
    L13:
        r4 = r4 + 1;
        goto L3
    L14:
        this.minDiff = r2 / r0;
        this.maxDiff = r3 / r1;
        return r0;
    }

    private int insertPitchPeriod(short[] r15, int r16, double r17, int r19) {
        if (r17 >= 0.5d) goto L5;
        double r4 = ((((double) r19) * r17) / (1.0d - r17)) + this.accumulatedSpeedAdjustmentError;
        int r1 = (int) Math.round(r4);
        this.accumulatedSpeedAdjustmentError = r4 - ((double) r1);
        int r6 = r1;
    L6:
        this.outputBuffer = ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, r19 + r6);
        System.arraycopy(r15, this.channelCount * r16, this.outputBuffer, this.outputFrameCount * this.channelCount, this.channelCount * r19);
        overlapAdd(r6, this.channelCount, this.outputBuffer, this.outputFrameCount + r19, r15, r16 + r19, r15, r16);
        this.outputFrameCount += r19 + r6;
        return r6;
    L5:
        double r5 = ((((double) r19) * ((2.0d * r17) - 1.0d)) / (1.0d - r17)) + this.accumulatedSpeedAdjustmentError;
        this.remainingInputToCopyFrameCount = (int) Math.round(r5);
        this.accumulatedSpeedAdjustmentError = r5 - ((double) this.remainingInputToCopyFrameCount);
        r6 = r19;
        goto L6
    }

    private short interpolate(short[] r19, int r20, long r21, long r23) {
        short r1 = r19[r20];
        short r2 = r19[r20 + this.channelCount];
        long r3 = ((long) this.newRatePosition) * r21;
        long r5 = ((long) this.oldRatePosition) * r23;
        long r7 = ((long) (this.oldRatePosition + 1)) * r23;
        long r9 = r7 - r3;
        long r11 = r7 - r5;
        return (short) (((((long) r1) * r9) + ((r11 - r9) * ((long) r2))) / r11);
    }

    private void moveNewSamplesToPitchBuffer(int r7) {
        int r0 = this.outputFrameCount - r7;
        this.pitchBuffer = ensureSpaceForAdditionalFrames(this.pitchBuffer, this.pitchFrameCount, r0);
        System.arraycopy(this.outputBuffer, this.channelCount * r7, this.pitchBuffer, this.pitchFrameCount * this.channelCount, this.channelCount * r0);
        this.outputFrameCount = r7;
        this.pitchFrameCount += r0;
    }

    private static void overlapAdd(int r7, int r8, short[] r9, int r10, short[] r11, int r12, short[] r13, int r14) {
        int r0 = 0;
    L3:
        if (r0 >= r8) goto L8;
        int r1 = (r10 * r8) + r0;
        int r2 = (r14 * r8) + r0;
        int r3 = (r12 * r8) + r0;
        int r4 = 0;
    L5:
        if (r4 >= r7) goto L7;
        r9[r1] = (short) (((r11[r3] * (r7 - r4)) + (r13[r2] * r4)) / r7);
        r1 = r1 + r8;
        r3 = r3 + r8;
        r2 = r2 + r8;
        r4 = r4 + 1;
        goto L5
    L7:
        r0 = r0 + 1;
        goto L3
    }

    private boolean previousPeriodBetter(int r4, int r5) {
        if (r4 != 0) goto L5;
    L15:
        return false;
    L5:
        if (this.prevPeriod == 0) goto L15;
        if (r5 <= (r4 * 3)) goto L11;
        return false;
    L11:
        if ((r4 * 2) > (this.prevMinDiff * 3)) goto L13;
        return false;
    L13:
        return true;
    }

    private void processStreamInput() {
        int r0 = this.outputFrameCount;
        double r1 = this.speed / this.pitch;
        float r3 = this.rate * this.pitch;
        if (r1 <= 1.00001d) goto L5;
    L8:
        changeSpeed(r1);
    L10:
        if (r3 == 1.0f) goto L13;
        adjustRate(r3, r0);
        return;
    L13:
        return;
    L5:
        if (r1 < 0.99999d) goto L8;
        copyToOutput(this.inputBuffer, 0, this.inputFrameCount);
        this.inputFrameCount = 0;
        goto L10
    }

    private void removePitchFrames(int r6) {
        if (r6 != 0) goto L4;
        return;
    L4:
        System.arraycopy(this.pitchBuffer, this.channelCount * r6, this.pitchBuffer, 0, (this.pitchFrameCount - r6) * this.channelCount);
        this.pitchFrameCount -= r6;
    }

    private void removeProcessedInputFrames(int r7) {
        int r0 = this.inputFrameCount - r7;
        System.arraycopy(this.inputBuffer, this.channelCount * r7, this.inputBuffer, 0, this.channelCount * r0);
        this.inputFrameCount = r0;
    }

    private int skipPitchPeriod(short[] r18, int r19, double r20, int r22) {
        if (r20 < 2.0d) goto L5;
        double r2 = (((double) r22) / (r20 - 1.0d)) + this.accumulatedSpeedAdjustmentError;
        int r4 = (int) Math.round(r2);
        this.accumulatedSpeedAdjustmentError = r2 - ((double) r4);
        int r9 = r4;
    L6:
        this.outputBuffer = ensureSpaceForAdditionalFrames(this.outputBuffer, this.outputFrameCount, r9);
        overlapAdd(r9, this.channelCount, this.outputBuffer, this.outputFrameCount, r18, r19, r18, r19 + r22);
        this.outputFrameCount += r9;
        return r9;
    L5:
        double r7 = ((((double) r22) * (2.0d - r20)) / (r20 - 1.0d)) + this.accumulatedSpeedAdjustmentError;
        this.remainingInputToCopyFrameCount = (int) Math.round(r7);
        this.accumulatedSpeedAdjustmentError = r7 - ((double) this.remainingInputToCopyFrameCount);
        r9 = r22;
        goto L6
    }

    public void flush() {
        this.inputFrameCount = 0;
        this.outputFrameCount = 0;
        this.pitchFrameCount = 0;
        this.oldRatePosition = 0;
        this.newRatePosition = 0;
        this.remainingInputToCopyFrameCount = 0;
        this.prevPeriod = 0;
        this.prevMinDiff = 0;
        this.minDiff = 0;
        this.maxDiff = 0;
        this.accumulatedSpeedAdjustmentError = 0.0d;
    }

    public void getOutput(ShortBuffer r8) {
        int r0 = Math.min(r8.remaining() / this.channelCount, this.outputFrameCount);
        r8.put(this.outputBuffer, 0, this.channelCount * r0);
        this.outputFrameCount -= r0;
        System.arraycopy(this.outputBuffer, this.channelCount * r0, this.outputBuffer, 0, this.outputFrameCount * this.channelCount);
    }

    public int getOutputSize() {
        return (this.outputFrameCount * this.channelCount) * 2;
    }

    public int getPendingInputBytes() {
        return (this.inputFrameCount * this.channelCount) * 2;
    }

    public void queueEndOfStream() {
        int r0 = this.inputFrameCount;
        double r1 = this.speed / this.pitch;
        int r6 = this.outputFrameCount + ((int) ((((((((double) (r0 - this.remainingInputToCopyFrameCount)) / r1) + ((double) this.remainingInputToCopyFrameCount)) + this.accumulatedSpeedAdjustmentError) + ((double) this.pitchFrameCount)) / (this.rate * this.pitch)) + 0.5d));
        this.accumulatedSpeedAdjustmentError = 0.0d;
        this.inputBuffer = ensureSpaceForAdditionalFrames(this.inputBuffer, this.inputFrameCount, (this.maxRequiredFrameCount * 2) + r0);
        int r7 = 0;
    L4:
        if (r7 >= ((this.maxRequiredFrameCount * 2) * this.channelCount)) goto L6;
        this.inputBuffer[(this.channelCount * r0) + r7] = 0;
        r7 = r7 + 1;
        goto L4
    L6:
        this.inputFrameCount += this.maxRequiredFrameCount * 2;
        processStreamInput();
        if (this.outputFrameCount <= r6) goto L9;
        this.outputFrameCount = r6;
    L9:
        this.inputFrameCount = 0;
        this.remainingInputToCopyFrameCount = 0;
        this.pitchFrameCount = 0;
    }

    public void queueInput(ShortBuffer r6) {
        int r0 = r6.remaining() / this.channelCount;
        int r1 = (this.channelCount * r0) * 2;
        this.inputBuffer = ensureSpaceForAdditionalFrames(this.inputBuffer, this.inputFrameCount, r0);
        r6.get(this.inputBuffer, this.inputFrameCount * this.channelCount, r1 / 2);
        this.inputFrameCount += r0;
        processStreamInput();
    }
}
