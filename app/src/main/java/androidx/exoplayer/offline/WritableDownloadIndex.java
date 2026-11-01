package androidx.media3.exoplayer.offline;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface WritableDownloadIndex extends DownloadIndex {
    void putDownload(Download r1) throws IOException;

    void removeDownload(String r1) throws IOException;

    void setDownloadingStatesToQueued() throws IOException;

    void setStatesToRemoving() throws IOException;

    void setStopReason(int r1) throws IOException;

    void setStopReason(String r1, int r2) throws IOException;
}
