package androidx.media3.exoplayer.source.chunk;

import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseMediaChunkIterator implements MediaChunkIterator {
    private long currentIndex;
    private final long fromIndex;
    private final long toIndex;

    public BaseMediaChunkIterator(long r1, long r3) {
        this.fromIndex = r1;
        this.toIndex = r3;
        reset();
    }

    protected final void checkInBounds() {
        if (this.currentIndex < this.fromIndex) goto L8;
        if (this.currentIndex > this.toIndex) goto L8;
        return;
    L8:
        throw new NoSuchElementException();
    }

    protected final long getCurrentIndex() {
        return this.currentIndex;
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
    public boolean isEnded() {
        if (this.currentIndex <= this.toIndex) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
    public boolean next() {
        this.currentIndex++;
        return !isEnded();
    }

    @Override // androidx.media3.exoplayer.source.chunk.MediaChunkIterator
    public void reset() {
        this.currentIndex = this.fromIndex - 1;
    }
}
