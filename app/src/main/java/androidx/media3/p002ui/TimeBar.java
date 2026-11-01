package androidx.media3.p002ui;

/* JADX INFO: loaded from: classes.dex */
public interface TimeBar {

    public interface OnScrubListener {
        void onScrubMove(TimeBar r1, long r2);

        void onScrubStart(TimeBar r1, long r2);

        void onScrubStop(TimeBar r1, long r2, boolean r4);
    }

    void addListener(OnScrubListener r1);

    long getPreferredUpdateDelay();

    void removeListener(OnScrubListener r1);

    void setAdGroupTimesMs(long[] r1, boolean[] r2, int r3);

    void setBufferedPosition(long r1);

    void setDuration(long r1);

    void setEnabled(boolean r1);

    void setKeyCountIncrement(int r1);

    void setKeyTimeIncrement(long r1);

    void setPosition(long r1);
}
