package androidx.media3.exoplayer.offline;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.database.DatabaseProvider;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.scheduler.Requirements;
import androidx.media3.exoplayer.scheduler.RequirementsWatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class DownloadManager {
    public static final int DEFAULT_MAX_PARALLEL_DOWNLOADS = 3;
    public static final int DEFAULT_MIN_RETRY_COUNT = 5;
    public static final Requirements DEFAULT_REQUIREMENTS = null;
    private static final int MSG_ADD_DOWNLOAD = 7;
    private static final int MSG_CONTENT_LENGTH_CHANGED = 11;
    private static final int MSG_DOWNLOAD_UPDATE = 3;
    private static final int MSG_INITIALIZE = 1;
    private static final int MSG_INITIALIZED = 1;
    private static final int MSG_PROCESSED = 2;
    private static final int MSG_RELEASE = 13;
    private static final int MSG_REMOVE_ALL_DOWNLOADS = 9;
    private static final int MSG_REMOVE_DOWNLOAD = 8;
    private static final int MSG_SET_DOWNLOADS_PAUSED = 2;
    private static final int MSG_SET_MAX_PARALLEL_DOWNLOADS = 5;
    private static final int MSG_SET_MIN_RETRY_COUNT = 6;
    private static final int MSG_SET_NOT_MET_REQUIREMENTS = 3;
    private static final int MSG_SET_STOP_REASON = 4;
    private static final int MSG_TASK_STOPPED = 10;
    private static final int MSG_UPDATE_PROGRESS = 12;
    private static final String TAG = "DownloadManager";
    private int activeTaskCount;
    private final Handler applicationHandler;
    private final Context context;
    private final WritableDownloadIndex downloadIndex;
    private List<Download> downloads;
    private boolean downloadsPaused;
    private boolean initialized;
    private final InternalHandler internalHandler;
    private final CopyOnWriteArraySet<Listener> listeners;
    private int maxParallelDownloads;
    private int minRetryCount;
    private int notMetRequirements;
    private int pendingMessages;
    private final RequirementsWatcher.Listener requirementsListener;
    private RequirementsWatcher requirementsWatcher;
    private boolean waitingForRequirements;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.offline.DownloadManager$1 */
    static /* synthetic */ class C03161 {
    }

    private static final class DownloadUpdate {
        public final Download download;
        public final List<Download> downloads;
        public final Exception finalException;
        public final boolean isRemove;

        public DownloadUpdate(Download r1, boolean r2, List<Download> r3, Exception r4) {
            this.download = r1;
            this.isRemove = r2;
            this.downloads = r3;
            this.finalException = r4;
        }
    }

    private static final class InternalHandler extends Handler {
        private static final int UPDATE_PROGRESS_INTERVAL_MS = 5000;
        private int activeDownloadTaskCount;
        private final HashMap<String, Task> activeTasks;
        private final WritableDownloadIndex downloadIndex;
        private final DownloaderFactory downloaderFactory;
        private final ArrayList<Download> downloads;
        private boolean downloadsPaused;
        private boolean hasActiveRemoveTask;
        private final Handler mainHandler;
        private int maxParallelDownloads;
        private int minRetryCount;
        private int notMetRequirements;
        public boolean released;
        private final HandlerThread thread;

        /* JADX INFO: renamed from: $r8$lambda$mYoRCebAyoibjFuCntJMlH-mrFQ, reason: not valid java name */
        public static /* synthetic */ int m476$r8$lambda$mYoRCebAyoibjFuCntJMlHmrFQ(Download r0, Download r1) {
            return compareStartTimes(r0, r1);
        }

        public InternalHandler(HandlerThread r2, WritableDownloadIndex r3, DownloaderFactory r4, Handler r5, int r6, int r7, boolean r8) {
            super(r2.getLooper());
            this.thread = r2;
            this.downloadIndex = r3;
            this.downloaderFactory = r4;
            this.mainHandler = r5;
            this.maxParallelDownloads = r6;
            this.minRetryCount = r7;
            this.downloadsPaused = r8;
            this.downloads = new ArrayList();
            this.activeTasks = new HashMap();
        }

        private void addDownload(DownloadRequest r14, int r15) {
            int r1 = 1;
            Download r0 = getDownload(r14.f41id, true);
            long r5 = System.currentTimeMillis();
            if (r0 == null) goto L6;
            putDownload(DownloadManager.mergeRequest(r0, r14, r15, r5));
        L10:
            syncTasks();
            return;
        L6:
            if (r15 != 0) goto L9;
            r1 = 0;
        L9:
            putDownload(new Download(r14, r1, r5, r5, -1, r15, 0));
            goto L10
        }

        private boolean canDownloadsRun() {
            if (this.downloadsPaused == false) goto L5;
        L7:
            return false;
        L5:
            if (this.notMetRequirements != 0) goto L7;
            return true;
        }

        private static int compareStartTimes(Download r4, Download r5) {
            return Util.compareLong(r4.startTimeMs, r5.startTimeMs);
        }

        private static Download copyDownloadWithState(Download r12, int r13, int r14) {
            return new Download(r12.request, r13, r12.startTimeMs, System.currentTimeMillis(), r12.contentLength, r14, 0, r12.progress);
        }

        private Download getDownload(String r5, boolean r6) {
            int r0 = getDownloadIndex(r5);
            if (r0 != (-1)) goto L5;
            if (r6 == false) goto L15;
            return this.downloadIndex.getDownload(r5);
        L9:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to load download: " + r5, e);
            return null;
        L15:
            return null;
        L5:
            return this.downloads.get(r0);
        }

        private int getDownloadIndex(String r4) {
            int r0 = 0;
        L4:
            if (r0 >= this.downloads.size()) goto L9;
            if (this.downloads.get(r0).request.f41id.equals(r4) == true) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return r0;
        L9:
            return -1;
        }

        private void initialize(int r8) {
            this.notMetRequirements = r8;
            DownloadCursor r0 = null;
            this.downloadIndex.setDownloadingStatesToQueued();     // Catch: Throwable -> L8 IOException -> L10
            r0 = this.downloadIndex.getDownloads(new int[]{0, 1, 2, 5, 7});     // Catch: Throwable -> L8 IOException -> L10
        L5:
            if (r0.moveToNext() == false) goto L12;
            this.downloads.add(r0.getDownload());     // Catch: Throwable -> L8 IOException -> L10
        L12:
            Util.closeQuietly(r0);
            ArrayList r2 = new ArrayList(this.downloads);
            this.mainHandler.obtainMessage(1, r2).sendToTarget();
            syncTasks();
            return;
        L10:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to load index.", e);     // Catch: Throwable -> L8
            this.downloads.clear();     // Catch: Throwable -> L8
        L8:
            th = move-exception;
            Util.closeQuietly(r0);
            throw th;
        }

        private void onContentLengthChanged(Task r17, long r18) {
            Download r2 = (Download) Assertions.checkNotNull(getDownload(Task.access$200(r17).f41id, false));
            if (r18 != r2.contentLength) goto L5;
            return;
        L5:
            if (r18 == (-1)) goto L10;
            putDownload(new Download(r2.request, r2.state, r2.startTimeMs, System.currentTimeMillis(), r18, r2.stopReason, r2.failureReason, r2.progress));
            return;
        }

        private void onDownloadTaskStopped(Download r18, Exception r19) {
            DownloadRequest r4 = r18.request;
            if (r19 != null) goto L5;
            int r5 = 3;
        L6:
            long r6 = r18.startTimeMs;
            long r8 = System.currentTimeMillis();
            long r10 = r18.contentLength;
            int r12 = r18.stopReason;
            if (r19 != null) goto L9;
            int r14 = 0;
        L10:
            Download r3 = new Download(r4, r5, r6, r8, r10, r12, r14, r18.progress);
            this.downloads.remove(getDownloadIndex(r3.request.f41id));
            this.downloadIndex.putDownload(r3);     // Catch: IOException -> L13
        L15:
            this.mainHandler.obtainMessage(3, new DownloadUpdate(r3, false, new ArrayList(this.downloads), r19)).sendToTarget();
            return;
        L13:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to update index.", e);
            goto L15
        L9:
            r14 = 1;
            goto L10
        L5:
            r5 = 4;
            goto L6
        }

        private void onRemoveTaskStopped(Download r6) {
            int r2 = 1;
            if (r6.state == 7) goto L5;
            int r0 = getDownloadIndex(r6.request.f41id);
            this.downloads.remove(r0);
            this.downloadIndex.removeDownload(r6.request.f41id);     // Catch: IOException -> L11
        L13:
            DownloadUpdate r1 = new DownloadUpdate(r6, true, new ArrayList(this.downloads), null);
            this.mainHandler.obtainMessage(3, r1).sendToTarget();
            return;
        L12:
            Log.m44e(DownloadManager.TAG, "Failed to remove from database");
            goto L13
        L5:
            if (r6.stopReason != 0) goto L7;
            r2 = 0;
        L7:
            putDownloadWithState(r6, r2, r6.stopReason);
            syncTasks();
        }

        private void onTaskStopped(Task r7) {
            String r0 = Task.access$200(r7).f41id;
            this.activeTasks.remove(r0);
            boolean r1 = Task.access$000(r7);
            if (r1 == false) goto L5;
            this.hasActiveRemoveTask = false;
        L9:
            if (Task.access$300(r7) == false) goto L12;
            syncTasks();
            return;
        L12:
            Exception r3 = Task.access$400(r7);
            if (r3 == null) goto L15;
            Log.m45e(DownloadManager.TAG, "Task failed: " + Task.access$200(r7) + ", " + r1, r3);
        L15:
            Download r2 = (Download) Assertions.checkNotNull(getDownload(r0, false));
            switch(r2.state) {
                case 2: goto L20;
                case 5: goto L19;
                case 7: goto L19;
                default: goto L18;
            };
        L19:
            Assertions.checkState(r1);
            onRemoveTaskStopped(r2);
        L21:
            syncTasks();
            return;
        L20:
            Assertions.checkState(!r1);
            onDownloadTaskStopped(r2, r3);
            goto L21
        L18:
            throw new IllegalStateException();
        L5:
            int r4 = this.activeDownloadTaskCount - 1;
            this.activeDownloadTaskCount = r4;
            if (r4 != 0) goto L9;
            removeMessages(12);
            goto L9
        }

        private Download putDownload(Download r9) {
            boolean r1 = true;
            if (r9.state != 3) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            int r2 = getDownloadIndex(r9.request.f41id);
            if (r2 != (-1)) goto L12;
            this.downloads.add(r9);
            Collections.sort(this.downloads, new DownloadManager$InternalHandler$$ExternalSyntheticLambda0());
        L24:
            this.downloadIndex.putDownload(r9);     // Catch: IOException -> L20
        L22:
            this.mainHandler.obtainMessage(3, new DownloadUpdate(r9, false, new ArrayList(this.downloads), null)).sendToTarget();
            return r9;
        L20:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to update index.", e);
            goto L22
        L12:
            if (r9.startTimeMs != this.downloads.get(r2).startTimeMs) goto L15;
            r1 = false;
        L15:
            this.downloads.set(r2, r9);
            if (r1 == false) goto L24;
            Collections.sort(this.downloads, new DownloadManager$InternalHandler$$ExternalSyntheticLambda0());
            goto L24
        L5:
            if (r9.state == 4) goto L7;
            r0 = true;
            goto L8
        }

        private Download putDownloadWithState(Download r2, int r3, int r4) {
            if (r3 != 3) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkState(r0);
            return putDownload(copyDownloadWithState(r2, r3, r4));
        L5:
            if (r3 == 4) goto L7;
            r0 = true;
            goto L8
        }

        private void release() {
            Iterator<Task> r0 = this.activeTasks.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L18;
            r0.next().cancel(true);
            goto L4
        L18:
            this.downloadIndex.setDownloadingStatesToQueued();     // Catch: IOException -> L8
        L10:
            this.downloads.clear();
            this.thread.quit();
            monitor-enter(this);
            this.released = true;     // Catch: Throwable -> L15
            notifyAll();     // Catch: Throwable -> L15
            monitor-exit(this);     // Catch: Throwable -> L15
            return;
        L15:
            th = move-exception;
            throw th;
        L8:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to update index.", e);
            goto L10
        }

        private void removeAllDownloads() {
            ArrayList r1 = new ArrayList();
            DownloadCursor r3 = this.downloadIndex.getDownloads(new int[]{3, 4});     // Catch: IOException -> L18
        L40:
        L11:
            th = move-exception;
            if (r3 != null) goto L44;
        L17:
            throw th;     // Catch: IOException -> L18
        L44:
            r3.close();     // Catch: Throwable -> L15
        L15:
            th = move-exception;
            th.addSuppressed(th);     // Catch: IOException -> L18
            goto L17
        L5:
            if (r3.moveToNext() == false) goto L8;
            r1.add(r3.getDownload());     // Catch: Throwable -> L11
            goto L19
        L8:
            if (r3 == null) goto L20;
            r3.close();     // Catch: IOException -> L18
        L20:
            int r4 = 0;
        L22:
            if (r4 >= this.downloads.size()) goto L24;
            this.downloads.set(r4, copyDownloadWithState(this.downloads.get(r4), 5, 0));
            r4 = r4 + 1;
            goto L22
        L24:
            int r5 = 0;
        L26:
            if (r5 >= r1.size()) goto L28;
            this.downloads.add(copyDownloadWithState((Download) r1.get(r5), 5, 0));
            r5 = r5 + 1;
            goto L26
        L28:
            Collections.sort(this.downloads, new DownloadManager$InternalHandler$$ExternalSyntheticLambda0());
            this.downloadIndex.setStatesToRemoving();     // Catch: IOException -> L31
        L33:
            ArrayList r0 = new ArrayList(this.downloads);
            int r6 = 0;
        L35:
            if (r6 >= this.downloads.size()) goto L37;
            DownloadUpdate r7 = new DownloadUpdate(this.downloads.get(r6), false, r0, null);
            this.mainHandler.obtainMessage(3, r7).sendToTarget();
            r6 = r6 + 1;
            goto L35
        L37:
            syncTasks();
            return;
        L31:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to update index.", e);
        L19:
            Log.m44e(DownloadManager.TAG, "Failed to load downloads.");
            goto L20
        }

        private void removeDownload(String r4) {
            Download r0 = getDownload(r4, true);
            if (r0 != null) goto L6;
            Log.m44e(DownloadManager.TAG, "Failed to remove nonexistent download: " + r4);
            return;
        L6:
            putDownloadWithState(r0, 5, 0);
            syncTasks();
        }

        private void setDownloadsPaused(boolean r1) {
            this.downloadsPaused = r1;
            syncTasks();
        }

        private void setMaxParallelDownloads(int r1) {
            this.maxParallelDownloads = r1;
            syncTasks();
        }

        private void setMinRetryCount(int r1) {
            this.minRetryCount = r1;
        }

        private void setNotMetRequirements(int r1) {
            this.notMetRequirements = r1;
            syncTasks();
        }

        private void setStopReason(Download r14, int r15) {
            if (r15 != 0) goto L8;
            if (r14.state != 1) goto L20;
            putDownloadWithState(r14, 0, 0);
            return;
        L20:
            return;
        L8:
            if (r15 == r14.stopReason) goto L18;
            int r0 = r14.state;
            if (r0 != 0) goto L12;
        L15:
            int r3 = 1;
        L16:
            putDownload(new Download(r14.request, r3, r14.startTimeMs, System.currentTimeMillis(), r14.contentLength, r15, 0, r14.progress));
            return;
        L12:
            if (r0 == 2) goto L15;
            r3 = r0;
            goto L16
        }

        private void setStopReason(String r6, int r7) {
            if (r6 != null) goto L13;
            int r1 = 0;
        L6:
            if (r1 >= this.downloads.size()) goto L24;
            setStopReason(this.downloads.get(r1), r7);
            r1 = r1 + 1;
            goto L6
        L24:
            this.downloadIndex.setStopReason(r7);     // Catch: IOException -> L10
        L20:
            syncTasks();
            return;
        L10:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to set manual stop reason", e);
            goto L20
        L13:
            Download r2 = getDownload(r6, false);
            if (r2 == null) goto L22;
            setStopReason(r2, r7);
            goto L20
        L22:
            this.downloadIndex.setStopReason(r6, r7);     // Catch: IOException -> L18
        L18:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to set manual stop reason: " + r6, e);
            goto L20
        }

        private void syncDownloadingDownload(Task r2, Download r3, int r4) {
            Assertions.checkState(!Task.access$000(r2));
            if (canDownloadsRun() == true) goto L5;
        L6:
            putDownloadWithState(r3, 0, 0);
            r2.cancel(false);
            return;
        L5:
            if (r4 >= this.maxParallelDownloads) goto L6;
        }

        private Task syncQueuedDownload(Task r11, Download r12) {
            if (r11 == null) goto L7;
            Assertions.checkState(!Task.access$000(r11));
            r11.cancel(false);
            return r11;
        L7:
            if (canDownloadsRun() == false) goto L17;
            if (this.activeDownloadTaskCount >= this.maxParallelDownloads) goto L19;
            Download r13 = putDownloadWithState(r12, 2, 0);
            Task r2 = new Task(r13.request, this.downloaderFactory.createDownloader(r13.request), r13.progress, false, this.minRetryCount, this, null);
            this.activeTasks.put(r13.request.f41id, r2);
            int r14 = this.activeDownloadTaskCount;
            this.activeDownloadTaskCount = r14 + 1;
            if (r14 != 0) goto L14;
            sendEmptyMessageDelayed(12, 5000);
        L14:
            r2.start();
            return r2;
        L19:
            return null;
        L17:
            return null;
        }

        private void syncRemovingDownload(Task r11, Download r12) {
            if (r11 == null) goto L8;
            if (Task.access$000(r11) == true) goto L12;
            r11.cancel(false);
            return;
        L12:
            return;
        L8:
            if (this.hasActiveRemoveTask == false) goto L10;
            return;
        L10:
            Downloader r4 = this.downloaderFactory.createDownloader(r12.request);
            boolean r6 = true;
            Task r2 = new Task(r12.request, r4, r12.progress, r6, this.minRetryCount, this, null);
            this.activeTasks.put(r12.request.f41id, r2);
            this.hasActiveRemoveTask = true;
            r2.start();
        }

        private void syncStoppedDownload(Task r2) {
            if (r2 == null) goto L5;
            Assertions.checkState(!Task.access$000(r2));
            r2.cancel(false);
            return;
        }

        private void syncTasks() {
            int r0 = 0;
            int r1 = 0;
        L4:
            if (r1 >= this.downloads.size()) goto L18;
            Download r2 = this.downloads.get(r1);
            Task r3 = this.activeTasks.get(r2.request.f41id);
            switch(r2.state) {
                case 0: goto L12;
                case 1: goto L11;
                case 2: goto L10;
                case 3: goto L8;
                case 4: goto L8;
                case 5: goto L9;
                case 6: goto L8;
                case 7: goto L9;
                default: goto L8;
            };
        L9:
            syncRemovingDownload(r3, r2);
        L13:
            if (r3 == null) goto L17;
            if (Task.access$000(r3) == true) goto L17;
            r0 = r0 + 1;
        L17:
            r1 = r1 + 1;
            goto L4
        L10:
            Assertions.checkNotNull(r3);
            syncDownloadingDownload(r3, r2, r0);
            goto L13
        L11:
            syncStoppedDownload(r3);
            goto L13
        L12:
            r3 = syncQueuedDownload(r3, r2);
            goto L13
        L8:
            throw new IllegalStateException();
        }

        private void updateProgress() {
            int r0 = 0;
        L4:
            if (r0 >= this.downloads.size()) goto L12;
            Download r1 = this.downloads.get(r0);
            if (r1.state != 2) goto L11;
            this.downloadIndex.putDownload(r1);     // Catch: IOException -> L9
        L9:
            e = move-exception;
            Log.m45e(DownloadManager.TAG, "Failed to update index.", e);
        L11:
            r0 = r0 + 1;
            goto L4
        L12:
            sendEmptyMessageDelayed(12, 5000);
        }

        @Override // android.os.Handler
        public void handleMessage(Message r5) {
            int r0 = 1;
            switch(r5.what) {
                case 1: goto L25;
                case 2: goto L21;
                case 3: goto L19;
                case 4: goto L18;
                case 5: goto L17;
                case 6: goto L16;
                case 7: goto L15;
                case 8: goto L14;
                case 9: goto L13;
                case 10: goto L12;
                case 11: goto L10;
                case 12: goto L8;
                case 13: goto L6;
                default: goto L5;
            };
        L6:
            release();
            return;
        L8:
            updateProgress();
            return;
        L10:
            onContentLengthChanged((Task) r5.obj, Util.toLong(r5.arg1, r5.arg2));
            return;
        L12:
            onTaskStopped((Task) r5.obj);
            r0 = 0;
        L26:
            this.mainHandler.obtainMessage(2, r0, this.activeTasks.size()).sendToTarget();
            return;
        L13:
            removeAllDownloads();
            goto L26
        L14:
            removeDownload((String) r5.obj);
            goto L26
        L15:
            addDownload((DownloadRequest) r5.obj, r5.arg1);
            goto L26
        L16:
            setMinRetryCount(r5.arg1);
            goto L26
        L17:
            setMaxParallelDownloads(r5.arg1);
            goto L26
        L18:
            setStopReason((String) r5.obj, r5.arg1);
            goto L26
        L19:
            setNotMetRequirements(r5.arg1);
            goto L26
        L25:
            initialize(r5.arg1);
            goto L26
        L5:
            throw new IllegalStateException();
        L21:
            if (r5.arg1 == 0) goto L23;
            boolean r1 = true;
        L24:
            setDownloadsPaused(r1);
            goto L26
        L23:
            r1 = false;
            goto L24
        }
    }

    public interface Listener {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.offline.DownloadManager$Listener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onDownloadChanged(Listener r0, DownloadManager r1, Download r2, Exception r3) {
            }

            public static void $default$onDownloadRemoved(Listener r0, DownloadManager r1, Download r2) {
            }

            public static void $default$onDownloadsPausedChanged(Listener r0, DownloadManager r1, boolean r2) {
            }

            public static void $default$onIdle(Listener r0, DownloadManager r1) {
            }

            public static void $default$onInitialized(Listener r0, DownloadManager r1) {
            }

            public static void $default$onRequirementsStateChanged(Listener r0, DownloadManager r1, Requirements r2, int r3) {
            }

            public static void $default$onWaitingForRequirementsChanged(Listener r0, DownloadManager r1, boolean r2) {
            }
        }

        void onDownloadChanged(DownloadManager r1, Download r2, Exception r3);

        void onDownloadRemoved(DownloadManager r1, Download r2);

        void onDownloadsPausedChanged(DownloadManager r1, boolean r2);

        void onIdle(DownloadManager r1);

        void onInitialized(DownloadManager r1);

        void onRequirementsStateChanged(DownloadManager r1, Requirements r2, int r3);

        void onWaitingForRequirementsChanged(DownloadManager r1, boolean r2);
    }

    private static class Task extends Thread implements Downloader.ProgressListener {
        private long contentLength;
        private final DownloadProgress downloadProgress;
        private final Downloader downloader;
        private Exception finalException;
        private volatile InternalHandler internalHandler;
        private volatile boolean isCanceled;
        private final boolean isRemove;
        private final int minRetryCount;
        private final DownloadRequest request;

        private Task(DownloadRequest r3, Downloader r4, DownloadProgress r5, boolean r6, int r7, InternalHandler r8) {
            this.request = r3;
            this.downloader = r4;
            this.downloadProgress = r5;
            this.isRemove = r6;
            this.minRetryCount = r7;
            this.internalHandler = r8;
            this.contentLength = -1;
        }

        /* synthetic */ Task(DownloadRequest r1, Downloader r2, DownloadProgress r3, boolean r4, int r5, InternalHandler r6, C03161 r7) {
            this(r1, r2, r3, r4, r5, r6);
        }

        static /* synthetic */ boolean access$000(Task r1) {
            return r1.isRemove;
        }

        static /* synthetic */ DownloadRequest access$200(Task r1) {
            return r1.request;
        }

        static /* synthetic */ boolean access$300(Task r1) {
            return r1.isCanceled;
        }

        static /* synthetic */ Exception access$400(Task r1) {
            return r1.finalException;
        }

        private static int getRetryDelayMillis(int r2) {
            return Math.min((r2 - 1) * 1000, 5000);
        }

        public void cancel(boolean r2) {
            if (r2 == false) goto L5;
            this.internalHandler = null;
        L5:
            if (this.isCanceled == true) goto L8;
            this.isCanceled = true;
            this.downloader.cancel();
            interrupt();
            return;
        }

        @Override // androidx.media3.exoplayer.offline.Downloader.ProgressListener
        public void onProgress(long r5, long r7, float r9) {
            this.downloadProgress.bytesDownloaded = r7;
            this.downloadProgress.percentDownloaded = r9;
            if (r5 == this.contentLength) goto L8;
            this.contentLength = r5;
            InternalHandler r0 = this.internalHandler;
            if (r0 == null) goto L9;
            r0.obtainMessage(11, (int) (r5 >> 32), (int) r5, this).sendToTarget();
            return;
        L9:
            return;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
        L21:
            e = move-exception;
            this.finalException = e;
        L26:
            InternalHandler r0 = this.internalHandler;
            if (r0 == null) goto L40;
            r0.obtainMessage(10, this).sendToTarget();
            return;
        L40:
            return;
        L3:
            if (this.isRemove == false) goto L5;
            this.downloader.remove();     // Catch: Exception -> L21 InterruptedException -> L23
            goto L26
        L5:
            int r1 = 0;
            long r2 = -1;
        L7:
            if (this.isCanceled == true) goto L26;
            this.downloader.download(this);     // Catch: IOException -> L10 Exception -> L21 InterruptedException -> L23
        L10:
            e = move-exception;
            if (this.isCanceled == true) goto L7;
            long r4 = this.downloadProgress.bytesDownloaded;     // Catch: Exception -> L21 InterruptedException -> L23
            if (r4 == r2) goto L16;
            r2 = r4;
            r1 = 0;
        L16:
            r1 = r1 + 1;     // Catch: Exception -> L21 InterruptedException -> L23
            if (r1 > this.minRetryCount) goto L19;
            Thread.sleep(getRetryDelayMillis(r1));     // Catch: Exception -> L21 InterruptedException -> L23
            goto L7
        L19:
            throw e;     // Catch: Exception -> L21 InterruptedException -> L23
        L24:
            Thread.currentThread().interrupt();
            goto L26
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$1-zn8lRsfBc96-3Z6whWTVu4ReM, reason: not valid java name */
    public static /* synthetic */ void m475$r8$lambda$1zn8lRsfBc963Z6whWTVu4ReM(DownloadManager r0, RequirementsWatcher r1, int r2) {
        r0.onRequirementsStateChanged(r1, r2);
    }

    public static /* synthetic */ boolean $r8$lambda$Ljilo1rwNXzdYIqsr2zvGuwCKeY(DownloadManager r0, Message r1) {
        return r0.handleMainMessage(r1);
    }

    static {
        DEFAULT_REQUIREMENTS = new Requirements(1);
    }

    public DownloadManager(Context r4, DatabaseProvider r5, Cache r6, DataSource.Factory r7, Executor r8) {
        this(r4, new DefaultDownloadIndex(r5), new DefaultDownloaderFactory(new CacheDataSource.Factory().setCache(r6).setUpstreamDataSourceFactory(r7), r8));
    }

    public DownloadManager(Context r11, WritableDownloadIndex r12, DownloaderFactory r13) {
        this.context = r11.getApplicationContext();
        this.downloadIndex = r12;
        this.maxParallelDownloads = 3;
        this.minRetryCount = 5;
        this.downloadsPaused = true;
        this.downloads = Collections.emptyList();
        this.listeners = new CopyOnWriteArraySet();
        Handler r6 = Util.createHandlerForCurrentOrMainLooper(new DownloadManager$$ExternalSyntheticLambda0(this));
        this.applicationHandler = r6;
        HandlerThread r3 = new HandlerThread("ExoPlayer:DownloadManager");
        r3.start();
        this.internalHandler = new InternalHandler(r3, r12, r13, r6, this.maxParallelDownloads, this.minRetryCount, this.downloadsPaused);
        RequirementsWatcher.Listener r14 = new DownloadManager$$ExternalSyntheticLambda1(this);
        this.requirementsListener = r14;
        this.requirementsWatcher = new RequirementsWatcher(r11, r14, DEFAULT_REQUIREMENTS);
        this.notMetRequirements = this.requirementsWatcher.start();
        this.pendingMessages = 1;
        this.internalHandler.obtainMessage(1, this.notMetRequirements, 0).sendToTarget();
    }

    private boolean handleMainMessage(Message r3) {
        switch(r3.what) {
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            default: goto L5;
        };
    L6:
        onDownloadUpdate((DownloadUpdate) r3.obj);
        return true;
    L7:
        onMessageProcessed(r3.arg1, r3.arg2);
        return true;
    L8:
        onInitialized((List) r3.obj);
        return true;
    L5:
        throw new IllegalStateException();
    }

    static Download mergeRequest(Download r16, DownloadRequest r17, int r18, long r19) {
        int r1 = r16.state;
        if (r1 != 5) goto L5;
    L8:
        long r3 = r19;
    L9:
        long r8 = r3;
        if (r1 != 5) goto L12;
    L17:
        int r7 = 7;
    L19:
        return new Download(r16.request.copyWithMergedRequest(r17), r7, r8, r19, -1, r18, 0);
    L12:
        if (r1 == 7) goto L17;
        if (r18 == 0) goto L16;
        r7 = 1;
        goto L19
    L16:
        r7 = 0;
        goto L19
    L5:
        if (r16.isTerminalState() == true) goto L8;
        r3 = r16.startTimeMs;
        goto L9
    }

    private void notifyWaitingForRequirementsChanged() {
        Iterator<Listener> r0 = this.listeners.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r0.next().onWaitingForRequirementsChanged(this, this.waitingForRequirements);
        goto L4
    }

    private void onDownloadUpdate(DownloadUpdate r6) {
        this.downloads = Collections.unmodifiableList(r6.downloads);
        Download r0 = r6.download;
        boolean r1 = updateWaitingForRequirements();
        if (r6.isRemove == false) goto L9;
        Iterator<Listener> r2 = this.listeners.iterator();
    L6:
        if (r2.hasNext() == false) goto L13;
        r2.next().onDownloadRemoved(this, r0);
    L13:
        if (r1 == false) goto L18;
        notifyWaitingForRequirementsChanged();
        return;
    L18:
        return;
    L9:
        Iterator<Listener> r3 = this.listeners.iterator();
    L11:
        if (r3.hasNext() == false) goto L13;
        r3.next().onDownloadChanged(this, r0, r6.finalException);
        goto L11
    }

    private void onInitialized(List<Download> r4) {
        this.initialized = true;
        this.downloads = Collections.unmodifiableList(r4);
        boolean r0 = updateWaitingForRequirements();
        Iterator<Listener> r1 = this.listeners.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r1.next().onInitialized(this);
        goto L4
    L6:
        if (r0 == false) goto L10;
        notifyWaitingForRequirementsChanged();
        return;
    }

    private void onMessageProcessed(int r3, int r4) {
        this.pendingMessages -= r3;
        this.activeTaskCount = r4;
        if (isIdle() == false) goto L8;
        Iterator<Listener> r0 = this.listeners.iterator();
    L6:
        if (r0.hasNext() == false) goto L10;
        r0.next().onIdle(this);
        goto L6
    L10:
        return;
    }

    private void onRequirementsStateChanged(RequirementsWatcher r5, int r6) {
        Requirements r0 = r5.getRequirements();
        if (this.notMetRequirements == r6) goto L5;
        this.notMetRequirements = r6;
        this.pendingMessages++;
        this.internalHandler.obtainMessage(3, r6, 0).sendToTarget();
    L5:
        boolean r1 = updateWaitingForRequirements();
        Iterator<Listener> r2 = this.listeners.iterator();
    L7:
        if (r2.hasNext() == false) goto L9;
        r2.next().onRequirementsStateChanged(this, r0, r6);
        goto L7
    L9:
        if (r1 == false) goto L13;
        notifyWaitingForRequirementsChanged();
        return;
    }

    private void setDownloadsPaused(boolean r4) {
        if (this.downloadsPaused != r4) goto L5;
        return;
    L5:
        this.downloadsPaused = r4;
        this.pendingMessages++;
        this.internalHandler.obtainMessage(2, r4 ? 1 : 0, 0).sendToTarget();
        boolean r0 = updateWaitingForRequirements();
        Iterator<Listener> r1 = this.listeners.iterator();
    L7:
        if (r1.hasNext() == false) goto L9;
        r1.next().onDownloadsPausedChanged(this, r4);
        goto L7
    L9:
        if (r0 == false) goto L13;
        notifyWaitingForRequirementsChanged();
        return;
    }

    private boolean updateWaitingForRequirements() {
        boolean r0 = false;
        if (this.downloadsPaused == true) goto L14;
        if (this.notMetRequirements == 0) goto L14;
        int r1 = 0;
    L8:
        if (r1 >= this.downloads.size()) goto L14;
        if (this.downloads.get(r1).state == 0) goto L11;
        r1 = r1 + 1;
        goto L8
    L11:
        r0 = true;
    L14:
        if (this.waitingForRequirements == r0) goto L16;
        boolean r2 = true;
    L17:
        this.waitingForRequirements = r0;
        return r2;
    L16:
        r2 = false;
        goto L17
    }

    public void addDownload(DownloadRequest r2) {
        addDownload(r2, 0);
    }

    public void addDownload(DownloadRequest r4, int r5) {
        this.pendingMessages++;
        this.internalHandler.obtainMessage(7, r5, 0, r4).sendToTarget();
    }

    public void addListener(Listener r2) {
        Assertions.checkNotNull(r2);
        this.listeners.add(r2);
    }

    public Looper getApplicationLooper() {
        return this.applicationHandler.getLooper();
    }

    public List<Download> getCurrentDownloads() {
        return this.downloads;
    }

    public DownloadIndex getDownloadIndex() {
        return this.downloadIndex;
    }

    public boolean getDownloadsPaused() {
        return this.downloadsPaused;
    }

    public int getMaxParallelDownloads() {
        return this.maxParallelDownloads;
    }

    public int getMinRetryCount() {
        return this.minRetryCount;
    }

    public int getNotMetRequirements() {
        return this.notMetRequirements;
    }

    public Requirements getRequirements() {
        return this.requirementsWatcher.getRequirements();
    }

    public boolean isIdle() {
        if (this.activeTaskCount == 0) goto L5;
    L7:
        return false;
    L5:
        if (this.pendingMessages != 0) goto L7;
        return true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public boolean isWaitingForRequirements() {
        return this.waitingForRequirements;
    }

    public void pauseDownloads() {
        setDownloadsPaused(true);
    }

    public void release() {
        InternalHandler r0 = this.internalHandler;
        monitor-enter(r0);
    L21:
        th = move-exception;
        throw th;
    L5:
        if (this.internalHandler.released == false) goto L8;
        monitor-exit(r0);     // Catch: Throwable -> L21
        return;
    L8:
        this.internalHandler.sendEmptyMessage(13);     // Catch: Throwable -> L21
        boolean r1 = false;
    L10:
        if (this.internalHandler.released == true) goto L16;
        this.internalHandler.wait();     // Catch: InterruptedException -> L13 Throwable -> L21
    L14:
        r1 = true;
        goto L10
    L16:
        if (r1 == false) goto L18;
        Thread.currentThread().interrupt();     // Catch: Throwable -> L21
    L18:
        this.applicationHandler.removeCallbacksAndMessages(null);     // Catch: Throwable -> L21
        this.requirementsWatcher.stop();     // Catch: Throwable -> L21
        this.downloads = Collections.emptyList();     // Catch: Throwable -> L21
        this.pendingMessages = 0;     // Catch: Throwable -> L21
        this.activeTaskCount = 0;     // Catch: Throwable -> L21
        this.initialized = false;     // Catch: Throwable -> L21
        this.notMetRequirements = 0;     // Catch: Throwable -> L21
        this.waitingForRequirements = false;     // Catch: Throwable -> L21
        monitor-exit(r0);     // Catch: Throwable -> L21
    }

    public void removeAllDownloads() {
        this.pendingMessages++;
        this.internalHandler.obtainMessage(9).sendToTarget();
    }

    public void removeDownload(String r3) {
        this.pendingMessages++;
        this.internalHandler.obtainMessage(8, r3).sendToTarget();
    }

    public void removeListener(Listener r2) {
        this.listeners.remove(r2);
    }

    public void resumeDownloads() {
        setDownloadsPaused(false);
    }

    public void setMaxParallelDownloads(int r4) {
        if (r4 <= 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (this.maxParallelDownloads != r4) goto L9;
        return;
    L9:
        this.maxParallelDownloads = r4;
        this.pendingMessages++;
        this.internalHandler.obtainMessage(5, r4, 0).sendToTarget();
        return;
    L5:
        r2 = false;
        goto L6
    }

    public void setMinRetryCount(int r4) {
        if (r4 < 0) goto L5;
        boolean r2 = true;
    L6:
        Assertions.checkArgument(r2);
        if (this.minRetryCount != r4) goto L9;
        return;
    L9:
        this.minRetryCount = r4;
        this.pendingMessages++;
        this.internalHandler.obtainMessage(6, r4, 0).sendToTarget();
        return;
    L5:
        r2 = false;
        goto L6
    }

    public void setRequirements(Requirements r4) {
        if (r4.equals(this.requirementsWatcher.getRequirements()) == false) goto L5;
        return;
    L5:
        this.requirementsWatcher.stop();
        this.requirementsWatcher = new RequirementsWatcher(this.context, this.requirementsListener, r4);
        int r0 = this.requirementsWatcher.start();
        onRequirementsStateChanged(this.requirementsWatcher, r0);
    }

    public void setStopReason(String r4, int r5) {
        this.pendingMessages++;
        this.internalHandler.obtainMessage(4, r5, 0, r4).sendToTarget();
    }
}
