package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class AsyncListDiffer<T> {
    private static final Executor sMainThreadExecutor = null;
    final AsyncDifferConfig<T> mConfig;
    private List<T> mList;
    private final List<ListListener<T>> mListeners;
    Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;

    public interface ListListener<T> {
        void onCurrentListChanged(List<T> r1, List<T> r2);
    }

    private static class MainThreadExecutor implements Executor {
        final Handler mHandler;

        MainThreadExecutor() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable r2) {
            this.mHandler.post(r2);
        }
    }

    static {
        sMainThreadExecutor = new MainThreadExecutor();
    }

    public AsyncListDiffer(ListUpdateCallback r2, AsyncDifferConfig<T> r3) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = r2;
        this.mConfig = r3;
        if (r3.getMainThreadExecutor() == null) goto L5;
        this.mMainThreadExecutor = r3.getMainThreadExecutor();
        return;
    L5:
        this.mMainThreadExecutor = sMainThreadExecutor;
    }

    public AsyncListDiffer(RecyclerView.Adapter r3, DiffUtil.ItemCallback<T> r4) {
        this(new AdapterListUpdateCallback(r3), new AsyncDifferConfig.Builder(r4).build());
    }

    private void onCurrentListChanged(List<T> r4, Runnable r5) {
        Iterator<ListListener<T>> r0 = this.mListeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onCurrentListChanged(r4, this.mReadOnlyList);
        goto L4
    L6:
        if (r5 == null) goto L10;
        r5.run();
        return;
    }

    public void addListListener(ListListener<T> r2) {
        this.mListeners.add(r2);
    }

    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    void latchList(List<T> r3, DiffUtil.DiffResult r4, Runnable r5) {
        List<T> r0 = this.mReadOnlyList;
        this.mList = r3;
        this.mReadOnlyList = Collections.unmodifiableList(r3);
        r4.dispatchUpdatesTo(this.mUpdateCallback);
        onCurrentListChanged(r0, r5);
    }

    public void removeListListener(ListListener<T> r2) {
        this.mListeners.remove(r2);
    }

    public void submitList(List<T> r2) {
        submitList(r2, null);
    }

    public void submitList(final List<T> r9, final Runnable r10) {
        final int r0 = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = r0;
        if (r9 != this.mList) goto L7;
        if (r10 == null) goto L17;
        r10.run();
        return;
    L17:
        return;
    L7:
        List<T> r1 = this.mReadOnlyList;
        if (r9 != null) goto L12;
        int r2 = this.mList.size();
        this.mList = null;
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback.onRemoved(0, r2);
        onCurrentListChanged(r1, r10);
        return;
    L12:
        if (this.mList != null) goto L15;
        this.mList = r9;
        this.mReadOnlyList = Collections.unmodifiableList(r9);
        this.mUpdateCallback.onInserted(0, r9.size());
        onCurrentListChanged(r1, r10);
        return;
    L15:
        final List<T> r3 = this.mList;
        this.mConfig.getBackgroundThreadExecutor().execute(new RunnableC04731(this, r3, r9, r0, r10));
    }
}
