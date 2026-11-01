package androidx.media3.exoplayer.offline;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface DownloadIndex {
    Download getDownload(String r1) throws IOException;

    DownloadCursor getDownloads(int... r1) throws IOException;
}
