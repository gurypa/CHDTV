package androidx.media3.datasource;

import androidx.media3.common.PriorityTaskManager;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class PriorityDataSourceFactory implements DataSource.Factory {
    private final int priority;
    private final PriorityTaskManager priorityTaskManager;
    private final DataSource.Factory upstreamFactory;

    public PriorityDataSourceFactory(DataSource.Factory r1, PriorityTaskManager r2, int r3) {
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
