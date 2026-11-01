package androidx.media3.exoplayer.source;

import androidx.media3.exoplayer.LoadingInfo;

/* JADX INFO: loaded from: classes.dex */
public interface SequenceableLoader {

    public interface Callback<T extends SequenceableLoader> {
        void onContinueLoadingRequested(T r1);
    }

    boolean continueLoading(LoadingInfo r1);

    long getBufferedPositionUs();

    long getNextLoadPositionUs();

    boolean isLoading();

    void reevaluateBuffer(long r1);
}
