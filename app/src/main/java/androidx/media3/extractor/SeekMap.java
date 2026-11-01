package androidx.media3.extractor;

import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public interface SeekMap {

    public static final class SeekPoints {
        public final SeekPoint first;
        public final SeekPoint second;

        public SeekPoints(SeekPoint r1) {
            this(r1, r1);
        }

        public SeekPoints(SeekPoint r2, SeekPoint r3) {
            this.first = (SeekPoint) Assertions.checkNotNull(r2);
            this.second = (SeekPoint) Assertions.checkNotNull(r3);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L17:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L17;
            SeekPoints r2 = (SeekPoints) r6;
            if (this.first.equals(r2.first) == false) goto L16;
            if (this.second.equals(r2.second) == false) goto L16;
            return true;
        L16:
            return false;
        }

        public int hashCode() {
            return (this.first.hashCode() * 31) + this.second.hashCode();
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder().append("[").append(this.first);
            if (this.first.equals(this.second) == false) goto L5;
            String r1 = "";
        L7:
            return r0.append(r1).append("]").toString();
        L5:
            r1 = ", " + this.second;
            goto L7
        }
    }

    public static class Unseekable implements SeekMap {
        private final long durationUs;
        private final SeekPoints startSeekPoints;

        public Unseekable(long r3) {
            this(r3, 0);
        }

        public Unseekable(long r5, long r7) {
            this.durationUs = r5;
            if (r7 != 0) goto L5;
            SeekPoint r1 = SeekPoint.START;
        L6:
            this.startSeekPoints = new SeekPoints(r1);
            return;
        L5:
            r1 = new SeekPoint(0, r7);
            goto L6
        }

        @Override // androidx.media3.extractor.SeekMap
        public long getDurationUs() {
            return this.durationUs;
        }

        @Override // androidx.media3.extractor.SeekMap
        public SeekPoints getSeekPoints(long r2) {
            return this.startSeekPoints;
        }

        @Override // androidx.media3.extractor.SeekMap
        public boolean isSeekable() {
            return false;
        }
    }

    long getDurationUs();

    SeekPoints getSeekPoints(long r1);

    boolean isSeekable();
}
