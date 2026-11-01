package androidx.media3.extractor.mp3;

import androidx.media3.common.C0180C;
import androidx.media3.extractor.SeekMap;

/* JADX INFO: loaded from: classes.dex */
interface Seeker extends SeekMap {

    public static class UnseekableSeeker extends SeekMap.Unseekable implements Seeker {
        public UnseekableSeeker() {
            super(C0180C.TIME_UNSET);
        }

        @Override // androidx.media3.extractor.mp3.Seeker
        public int getAverageBitrate() {
            return C0180C.RATE_UNSET_INT;
        }

        @Override // androidx.media3.extractor.mp3.Seeker
        public long getDataEndPosition() {
            return -1;
        }

        @Override // androidx.media3.extractor.mp3.Seeker
        public long getTimeUs(long r3) {
            return 0;
        }
    }

    int getAverageBitrate();

    long getDataEndPosition();

    long getTimeUs(long r1);
}
