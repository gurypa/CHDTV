package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.DataReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface DataSource extends DataReader {

    /* JADX INFO: renamed from: androidx.media3.datasource.DataSource$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static Map $default$getResponseHeaders(DataSource r1) {
            return Collections.emptyMap();
        }
    }

    public interface Factory {
        DataSource createDataSource();
    }

    void addTransferListener(TransferListener r1);

    void close() throws IOException;

    Map<String, List<String>> getResponseHeaders();

    Uri getUri();

    long open(DataSpec r1) throws IOException;
}
