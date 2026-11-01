package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.util.Assertions;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class PriorityDataSource implements DataSource {
    private final int priority;
    private final PriorityTaskManager priorityTaskManager;
    private final DataSource upstream;

    public static final class Factory implements DataSource.Factory {
        private final int priority;
        private final PriorityTaskManager priorityTaskManager;
        private final DataSource.Factory upstreamFactory;

        public Factory(DataSource.Factory r1, PriorityTaskManager r2, int r3) {
            this.upstreamFactory = r1;
            this.priorityTaskManager = r2;
            this.priority = r3;
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public /* bridge */ /* synthetic */ DataSource createDataSource() {
            return createDataSource();
        }

        @Override // androidx.media3.datasource.DataSource.Factory
        public PriorityDataSource createDataSource() {
            return new PriorityDataSource(this.upstreamFactory.createDataSource(), this.priorityTaskManager, this.priority);
        }
    }

    public PriorityDataSource(DataSource r2, PriorityTaskManager r3, int r4) {
        this.upstream = (DataSource) Assertions.checkNotNull(r2);
        this.priorityTaskManager = (PriorityTaskManager) Assertions.checkNotNull(r3);
        this.priority = r4;
    }

    @Override // androidx.media3.datasource.DataSource
    public void addTransferListener(TransferListener r2) {
        Assertions.checkNotNull(r2);
        this.upstream.addTransferListener(r2);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() throws IOException {
        this.upstream.close();
    }

    @Override // androidx.media3.datasource.DataSource
    public Map<String, List<String>> getResponseHeaders() {
        return this.upstream.getResponseHeaders();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.upstream.getUri();
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r3) throws IOException {
        this.priorityTaskManager.proceedOrThrow(this.priority);
        return this.upstream.open(r3);
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r3, int r4, int r5) throws IOException {
        this.priorityTaskManager.proceedOrThrow(this.priority);
        return this.upstream.read(r3, r4, r5);
    }
}
