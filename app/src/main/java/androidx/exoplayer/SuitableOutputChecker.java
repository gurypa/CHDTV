package androidx.media3.exoplayer;

/* JADX INFO: loaded from: classes.dex */
public interface SuitableOutputChecker {

    public interface Callback {
        void onSelectedOutputSuitabilityChanged(boolean r1);
    }

    void disable();

    void enable(Callback r1);

    boolean isSelectedOutputSuitableForPlayback();
}
