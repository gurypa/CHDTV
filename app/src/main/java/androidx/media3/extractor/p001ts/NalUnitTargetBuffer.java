package androidx.media3.extractor.p001ts;

import androidx.media3.common.util.Assertions;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class NalUnitTargetBuffer {
    private boolean isCompleted;
    private boolean isFilling;
    public byte[] nalData;
    public int nalLength;
    private final int targetType;

    public NalUnitTargetBuffer(int r4, int r5) {
        this.targetType = r4;
        this.nalData = new byte[r5 + 3];
        this.nalData[2] = 1;
    }

    public void appendToNalUnit(byte[] r4, int r5, int r6) {
        if (this.isFilling == true) goto L5;
        return;
    L5:
        int r0 = r6 - r5;
        if (this.nalData.length >= (this.nalLength + r0)) goto L8;
        this.nalData = Arrays.copyOf(this.nalData, (this.nalLength + r0) * 2);
    L8:
        System.arraycopy(r4, r5, this.nalData, this.nalLength, r0);
        this.nalLength += r0;
    }

    public boolean endNalUnit(int r3) {
        if (this.isFilling == true) goto L5;
        return false;
    L5:
        this.nalLength -= r3;
        this.isFilling = false;
        this.isCompleted = true;
        return true;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void reset() {
        this.isFilling = false;
        this.isCompleted = false;
    }

    public void startNalUnit(int r4) {
        boolean r1 = true;
        Assertions.checkState(!this.isFilling);
        if (r4 == this.targetType) goto L6;
        r1 = false;
    L6:
        this.isFilling = r1;
        if (this.isFilling == false) goto L10;
        this.nalLength = 3;
        this.isCompleted = false;
        return;
    }
}
