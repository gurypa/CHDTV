package androidx.media3.extractor;

/* JADX INFO: loaded from: classes.dex */
public final class SeekPoint {
    public static final SeekPoint START = null;
    public final long position;
    public final long timeUs;

    static {
        START = new SeekPoint(0, 0);
    }

    public SeekPoint(long r1, long r3) {
        this.timeUs = r1;
        this.position = r3;
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L17;
        SeekPoint r2 = (SeekPoint) r8;
        if (this.timeUs != r2.timeUs) goto L16;
        if (this.position != r2.position) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        return (((int) this.timeUs) * 31) + ((int) this.position);
    }

    public String toString() {
        return "[timeUs=" + this.timeUs + ", position=" + this.position + "]";
    }
}
