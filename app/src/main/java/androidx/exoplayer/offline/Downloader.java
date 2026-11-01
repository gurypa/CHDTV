package androidx.media3.exoplayer.offline;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface Downloader {

    public interface ProgressListener {
        void onProgress(long r1, long r3, float r5);
    }

    void cancel();

    void download(ProgressListener r1) throws IOException, InterruptedException;

    void remove();
}
