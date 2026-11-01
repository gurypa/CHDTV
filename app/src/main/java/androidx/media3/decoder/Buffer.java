package androidx.media3.decoder;

import androidx.media3.common.C0180C;

/* JADX INFO: loaded from: classes.dex */
public abstract class Buffer {
    private int flags;

    public Buffer() {
    }

    public final void addFlag(int r2) {
        this.flags |= r2;
    }

    public void clear() {
        this.flags = 0;
    }

    public final void clearFlag(int r3) {
        this.flags &= ~r3;
    }

    protected final boolean getFlag(int r2) {
        if ((this.flags & r2) != r2) goto L5;
        return true;
    L5:
        return false;
    }

    public final boolean hasSupplementalData() {
        return getFlag(268435456);
    }

    public final boolean isEndOfStream() {
        return getFlag(4);
    }

    public final boolean isFirstSample() {
        return getFlag(C0180C.BUFFER_FLAG_FIRST_SAMPLE);
    }

    public final boolean isKeyFrame() {
        return getFlag(1);
    }

    public final boolean isLastSample() {
        return getFlag(C0180C.BUFFER_FLAG_LAST_SAMPLE);
    }

    public final boolean notDependedOn() {
        return getFlag(C0180C.BUFFER_FLAG_NOT_DEPENDED_ON);
    }

    public final void setFlags(int r1) {
        this.flags = r1;
    }
}
