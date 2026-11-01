package androidx.recyclerview.widget;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
class MessageThreadUtil<T> implements ThreadUtil<T> {

    /* JADX INFO: renamed from: androidx.recyclerview.widget.MessageThreadUtil$1 */
    class C04981 implements ThreadUtil.MainThreadCallback<T> {
        static final int ADD_TILE = 2;
        static final int REMOVE_TILE = 3;
        static final int UPDATE_ITEM_COUNT = 1;
        private final Handler mMainThreadHandler;
        private Runnable mMainThreadRunnable;
        final MessageQueue mQueue;
        final /* synthetic */ MessageThreadUtil this$0;
        final /* synthetic */ ThreadUtil.MainThreadCallback val$callback;

        C04981(MessageThreadUtil r2, ThreadUtil.MainThreadCallback r3) {
            this.this$0 = r2;
            this.val$callback = r3;
            this.mQueue = new MessageQueue();
            this.mMainThreadHandler = new Handler(Looper.getMainLooper());
            this.mMainThreadRunnable = new AnonymousClass1(this);
        }

        private void sendMessage(SyncQueueItem r3) {
            this.mQueue.sendMessage(r3);
            this.mMainThreadHandler.post(this.mMainThreadRunnable);
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.MainThreadCallback
        public void addTile(int r2, TileList.Tile<T> r3) {
            sendMessage(SyncQueueItem.obtainMessage(2, r2, r3));
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.MainThreadCallback
        public void removeTile(int r2, int r3) {
            sendMessage(SyncQueueItem.obtainMessage(3, r2, r3));
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.MainThreadCallback
        public void updateItemCount(int r2, int r3) {
            sendMessage(SyncQueueItem.obtainMessage(1, r2, r3));
        }
    }

    /* JADX INFO: renamed from: androidx.recyclerview.widget.MessageThreadUtil$2 */
    class C04992 implements ThreadUtil.BackgroundCallback<T> {
        static final int LOAD_TILE = 3;
        static final int RECYCLE_TILE = 4;
        static final int REFRESH = 1;
        static final int UPDATE_RANGE = 2;
        private Runnable mBackgroundRunnable;
        AtomicBoolean mBackgroundRunning;
        private final Executor mExecutor;
        final MessageQueue mQueue;
        final /* synthetic */ MessageThreadUtil this$0;
        final /* synthetic */ ThreadUtil.BackgroundCallback val$callback;

        C04992(MessageThreadUtil r2, ThreadUtil.BackgroundCallback r3) {
            this.this$0 = r2;
            this.val$callback = r3;
            this.mQueue = new MessageQueue();
            this.mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            this.mBackgroundRunning = new AtomicBoolean(false);
            this.mBackgroundRunnable = new AnonymousClass1(this);
        }

        private void maybeExecuteBackgroundRunnable() {
            if (this.mBackgroundRunning.compareAndSet(false, true) == false) goto L6;
            this.mExecutor.execute(this.mBackgroundRunnable);
            return;
        }

        private void sendMessage(SyncQueueItem r2) {
            this.mQueue.sendMessage(r2);
            maybeExecuteBackgroundRunnable();
        }

        private void sendMessageAtFrontOfQueue(SyncQueueItem r2) {
            this.mQueue.sendMessageAtFrontOfQueue(r2);
            maybeExecuteBackgroundRunnable();
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.BackgroundCallback
        public void loadTile(int r2, int r3) {
            sendMessage(SyncQueueItem.obtainMessage(3, r2, r3));
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.BackgroundCallback
        public void recycleTile(TileList.Tile<T> r3) {
            sendMessage(SyncQueueItem.obtainMessage(4, 0, r3));
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.BackgroundCallback
        public void refresh(int r3) {
            sendMessageAtFrontOfQueue(SyncQueueItem.obtainMessage(1, r3, null));
        }

        @Override // androidx.recyclerview.widget.ThreadUtil.BackgroundCallback
        public void updateRange(int r8, int r9, int r10, int r11, int r12) {
            sendMessageAtFrontOfQueue(SyncQueueItem.obtainMessage(2, r8, r9, r10, r11, r12, null));
        }
    }

    static class MessageQueue {
        private final Object mLock;
        private SyncQueueItem mRoot;

        MessageQueue() {
            this.mLock = new Object();
        }

        SyncQueueItem next() {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L12:
            th = move-exception;
            throw th;
        L5:
            if (this.mRoot != null) goto L9;
            monitor-exit(r0);     // Catch: Throwable -> L12
            return null;
        L9:
            SyncQueueItem r1 = this.mRoot;     // Catch: Throwable -> L12
            this.mRoot = this.mRoot.next;     // Catch: Throwable -> L12
            monitor-exit(r0);     // Catch: Throwable -> L12
            return r1;
        }

        void removeMessages(int r6) {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L23:
        L20:
            th = move-exception;
            throw th;
        L5:
            if (this.mRoot == null) goto L10;
            if (this.mRoot.what != r6) goto L10;
            SyncQueueItem r1 = this.mRoot;     // Catch: Throwable -> L20
            this.mRoot = this.mRoot.next;     // Catch: Throwable -> L20
            r1.recycle();     // Catch: Throwable -> L20
        L10:
            if (this.mRoot == null) goto L18;
            SyncQueueItem r2 = this.mRoot;     // Catch: Throwable -> L20
            SyncQueueItem r3 = r2.next;     // Catch: Throwable -> L20
        L12:
            if (r3 == null) goto L18;
            SyncQueueItem r4 = r3.next;     // Catch: Throwable -> L20
            if (r3.what != r6) goto L16;
            r2.next = r4;     // Catch: Throwable -> L20
            r3.recycle();     // Catch: Throwable -> L20
        L17:
            r3 = r4;
            goto L12
        L16:
            r2 = r3;
        L18:
            monitor-exit(r0);     // Catch: Throwable -> L20
        }

        void sendMessage(SyncQueueItem r4) {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L16:
            th = move-exception;
            throw th;
        L5:
            if (this.mRoot != null) goto L9;
            this.mRoot = r4;     // Catch: Throwable -> L16
            monitor-exit(r0);     // Catch: Throwable -> L16
            return;
        L9:
            SyncQueueItem r1 = this.mRoot;     // Catch: Throwable -> L16
        L11:
            if (r1.next == null) goto L13;
            r1 = r1.next;     // Catch: Throwable -> L16
            goto L11
        L13:
            r1.next = r4;     // Catch: Throwable -> L16
            monitor-exit(r0);     // Catch: Throwable -> L16
        }

        void sendMessageAtFrontOfQueue(SyncQueueItem r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            r3.next = this.mRoot;     // Catch: Throwable -> L7
            this.mRoot = r3;     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }
    }

    static class SyncQueueItem {
        private static SyncQueueItem sPool;
        private static final Object sPoolLock = null;
        public int arg1;
        public int arg2;
        public int arg3;
        public int arg4;
        public int arg5;
        public Object data;
        SyncQueueItem next;
        public int what;

        static {
            sPoolLock = new Object();
        }

        SyncQueueItem() {
        }

        static SyncQueueItem obtainMessage(int r7, int r8, int r9) {
            return obtainMessage(r7, r8, r9, 0, 0, 0, null);
        }

        static SyncQueueItem obtainMessage(int r3, int r4, int r5, int r6, int r7, int r8, Object r9) {
            Object r0 = sPoolLock;
            monitor-enter(r0);
        L11:
            th = move-exception;
            throw th;
        L5:
            if (sPool != null) goto L7;
            SyncQueueItem r1 = new SyncQueueItem();     // Catch: Throwable -> L11
        L8:
            r1.what = r3;     // Catch: Throwable -> L11
            r1.arg1 = r4;     // Catch: Throwable -> L11
            r1.arg2 = r5;     // Catch: Throwable -> L11
            r1.arg3 = r6;     // Catch: Throwable -> L11
            r1.arg4 = r7;     // Catch: Throwable -> L11
            r1.arg5 = r8;     // Catch: Throwable -> L11
            r1.data = r9;     // Catch: Throwable -> L11
            monitor-exit(r0);     // Catch: Throwable -> L11
            return r1;
        L7:
            r1 = sPool;     // Catch: Throwable -> L11
            sPool = sPool.next;     // Catch: Throwable -> L11
            r1.next = null;     // Catch: Throwable -> L11
            goto L8
        }

        static SyncQueueItem obtainMessage(int r7, int r8, Object r9) {
            return obtainMessage(r7, r8, 0, 0, 0, 0, r9);
        }

        void recycle() {
            this.next = null;
            this.arg5 = 0;
            this.arg4 = 0;
            this.arg3 = 0;
            this.arg2 = 0;
            this.arg1 = 0;
            this.what = 0;
            this.data = null;
            Object r0 = sPoolLock;
            monitor-enter(r0);
        L10:
            th = move-exception;
            throw th;
        L5:
            if (sPool == null) goto L7;
            this.next = sPool;     // Catch: Throwable -> L10
        L7:
            sPool = this;     // Catch: Throwable -> L10
            monitor-exit(r0);     // Catch: Throwable -> L10
        }
    }

    MessageThreadUtil() {
    }

    @Override // androidx.recyclerview.widget.ThreadUtil
    public ThreadUtil.BackgroundCallback<T> getBackgroundProxy(ThreadUtil.BackgroundCallback<T> r2) {
        return new C04992(this, r2);
    }

    @Override // androidx.recyclerview.widget.ThreadUtil
    public ThreadUtil.MainThreadCallback<T> getMainThreadProxy(ThreadUtil.MainThreadCallback<T> r2) {
        return new C04981(this, r2);
    }
}
