package androidx.media3.exoplayer.upstream;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.TraceUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.util.ReleasableExecutor;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class Loader implements LoaderErrorThrower {
    private static final int ACTION_TYPE_DONT_RETRY = 2;
    private static final int ACTION_TYPE_DONT_RETRY_FATAL = 3;
    private static final int ACTION_TYPE_RETRY = 0;
    private static final int ACTION_TYPE_RETRY_AND_RESET_ERROR_COUNT = 1;
    public static final LoadErrorAction DONT_RETRY = null;
    public static final LoadErrorAction DONT_RETRY_FATAL = null;
    public static final LoadErrorAction RETRY = null;
    public static final LoadErrorAction RETRY_RESET_ERROR_COUNT = null;
    private static final String THREAD_NAME_PREFIX = "ExoPlayer:Loader:";
    private LoadTask<? extends Loadable> currentTask;
    private final ReleasableExecutor downloadExecutor;
    private IOException fatalError;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.Loader$1 */
    static /* synthetic */ class C03711 {
    }

    public interface Callback<T extends Loadable> {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.Loader$Callback$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onLoadStarted(Callback r0, Loadable r1, long r2, long r4, int r6) {
            }
        }

        void onLoadCanceled(T r1, long r2, long r4, boolean r6);

        void onLoadCompleted(T r1, long r2, long r4);

        LoadErrorAction onLoadError(T r1, long r2, long r4, IOException r6, int r7);

        void onLoadStarted(T r1, long r2, long r4, int r6);
    }

    public static final class LoadErrorAction {
        private final long retryDelayMillis;
        private final int type;

        private LoadErrorAction(int r1, long r2) {
            this.type = r1;
            this.retryDelayMillis = r2;
        }

        /* synthetic */ LoadErrorAction(int r1, long r2, C03711 r4) {
            this(r1, r2);
        }

        static /* synthetic */ int access$300(LoadErrorAction r1) {
            return r1.type;
        }

        static /* synthetic */ long access$400(LoadErrorAction r2) {
            return r2.retryDelayMillis;
        }

        public boolean isRetry() {
            if (this.type != 0) goto L5;
            return true;
        L5:
            if (this.type == 1) goto L10;
            return false;
        L10:
            return true;
        }
    }

    private final class LoadTask<T extends Loadable> extends Handler implements Runnable {
        private static final int MSG_FATAL_ERROR = 4;
        private static final int MSG_FINISH = 2;
        private static final int MSG_IO_EXCEPTION = 3;
        private static final int MSG_START = 1;
        private static final String TAG = "LoadTask";
        private Callback<T> callback;
        private boolean canceled;
        private IOException currentError;
        public final int defaultMinRetryCount;
        private int errorCount;
        private Thread executorThread;
        private final T loadable;
        private volatile boolean released;
        private final long startTimeMs;
        final /* synthetic */ Loader this$0;

        public LoadTask(Loader r1, Looper r2, T r3, Callback<T> r4, int r5, long r6) {
            this.this$0 = r1;
            super(r2);
            this.loadable = r3;
            this.callback = r4;
            this.defaultMinRetryCount = r5;
            this.startTimeMs = r6;
        }

        private void execute() {
            long r2 = SystemClock.elapsedRealtime();
            long r4 = r2 - this.startTimeMs;
            ((Callback) Assertions.checkNotNull(this.callback)).onLoadStarted(this.loadable, r2, r4, this.errorCount);
            this.currentError = null;
            Loader.access$500(this.this$0).execute((Runnable) Assertions.checkNotNull(Loader.access$100(this.this$0)));
        }

        private void finish() {
            Loader.access$102(this.this$0, null);
        }

        private long getRetryDelayMillis() {
            return Math.min((this.errorCount - 1) * 1000, 5000);
        }

        public void cancel(boolean r9) {
            this.released = r9;
            this.currentError = null;
            if (hasMessages(1) == false) goto L7;
            this.canceled = true;
            removeMessages(1);
            if (r9 == true) goto L12;
            sendEmptyMessage(2);
        L12:
            if (r9 == false) goto L20;
            finish();
            long r3 = SystemClock.elapsedRealtime();
            ((Callback) Assertions.checkNotNull(this.callback)).onLoadCanceled(this.loadable, r3, r3 - this.startTimeMs, true);
            this.callback = null;
            return;
        L20:
            return;
        L7:
            monitor-enter(this);
            this.canceled = true;     // Catch: Throwable -> L15
            this.loadable.cancelLoad();     // Catch: Throwable -> L15
            Thread r1 = this.executorThread;     // Catch: Throwable -> L15
            if (r1 == null) goto L11;
            r1.interrupt();     // Catch: Throwable -> L15
        L11:
            monitor-exit(this);     // Catch: Throwable -> L15
        L15:
            th = move-exception;
            throw th;
        }

        @Override // android.os.Handler
        public void handleMessage(Message r12) {
            if (this.released == false) goto L6;
            return;
        L6:
            if (r12.what != 1) goto L10;
            execute();
            return;
        L10:
            if (r12.what == 4) goto L37;
            finish();
            long r5 = SystemClock.elapsedRealtime();
            long r7 = r5 - this.startTimeMs;
            Callback r3 = (Callback) Assertions.checkNotNull(this.callback);
            if (this.canceled == false) goto L16;
            r3.onLoadCanceled(this.loadable, r5, r7, false);
            return;
        L16:
            switch(r12.what) {
                case 2: goto L38;
                case 3: goto L18;
                default: goto L43;
            };
        L18:
            this.currentError = (IOException) r12.obj;
            this.errorCount++;
            LoadErrorAction r0 = r3.onLoadError(this.loadable, r5, r7, this.currentError, this.errorCount);
            if (LoadErrorAction.access$300(r0) != 3) goto L22;
            Loader.access$202(this.this$0, this.currentError);
            return;
        L22:
            if (LoadErrorAction.access$300(r0) != 2) goto L24;
            return;
        L24:
            if (LoadErrorAction.access$300(r0) != 1) goto L27;
            this.errorCount = 1;
        L27:
            if (LoadErrorAction.access$400(r0) == C0180C.TIME_UNSET) goto L29;
            long r1 = LoadErrorAction.access$400(r0);
        L30:
            start(r1);
            return;
        L29:
            r1 = getRetryDelayMillis();
            goto L30
        L38:
            r3.onLoadCompleted(this.loadable, r5, r7);     // Catch: RuntimeException -> L33
            return;
        L33:
            e = move-exception;
            Log.m45e(TAG, "Unexpected exception handling load completed", e);
            Loader.access$202(this.this$0, new UnexpectedLoaderException(e));
            return;
        L43:
            return;
        L37:
            throw ((Error) r12.obj);
        }

        public void maybeThrowError(int r2) throws IOException {
            if (this.currentError != null) goto L5;
            return;
        L5:
            if (this.errorCount > r2) goto L8;
            return;
        L8:
            throw this.currentError;
        }

        @Override // java.lang.Runnable
        public void run() {
            monitor-enter(this);     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
        L28:
            th = move-exception;
            throw th;     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
        L5:
            if (this.canceled == true) goto L7;
            boolean r1 = true;
        L8:
            this.executorThread = Thread.currentThread();     // Catch: Throwable -> L28
            monitor-exit(this);     // Catch: Throwable -> L28
            if (r1 == false) goto L17;
            TraceUtil.beginSection("load:" + this.loadable.getClass().getSimpleName());     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
            this.loadable.load();     // Catch: Throwable -> L14
            TraceUtil.endSection();     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
        L14:
            th = move-exception;
            TraceUtil.endSection();     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
            throw th;     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
        L17:
            monitor-enter(this);     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
            this.executorThread = null;     // Catch: Throwable -> L25
            Thread.interrupted();     // Catch: Throwable -> L25
            monitor-exit(this);     // Catch: Throwable -> L25
            if (this.released == true) goto L57;
            sendEmptyMessage(2);     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
            return;
        L57:
            return;
        L25:
            th = move-exception;
            throw th;     // Catch: Error -> L31 OutOfMemoryError -> L36 Exception -> L40 IOException -> L44
        L7:
            r1 = false;
        L44:
            e = move-exception;
            if (this.released == true) goto L62;
            obtainMessage(3, e).sendToTarget();
            return;
        L62:
            return;
        L31:
            e = move-exception;
            if (this.released == true) goto L35;
            Log.m45e(TAG, "Unexpected error loading stream", e);
            obtainMessage(4, e).sendToTarget();
        L35:
            throw e;
        L40:
            e = move-exception;
            if (this.released == true) goto L60;
            Log.m45e(TAG, "Unexpected exception loading stream", e);
            obtainMessage(3, new UnexpectedLoaderException(e)).sendToTarget();
            return;
        L60:
            return;
        L36:
            e = move-exception;
            if (this.released == true) goto L58;
            Log.m45e(TAG, "OutOfMemory error loading stream", e);
            obtainMessage(3, new UnexpectedLoaderException(e)).sendToTarget();
            return;
        }

        public void start(long r5) {
            if (Loader.access$100(this.this$0) != null) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkState(r0);
            Loader.access$102(this.this$0, this);
            if (r5 <= 0) goto L9;
            sendEmptyMessageDelayed(1, r5);
            return;
        L9:
            execute();
            return;
        L5:
            r0 = false;
            goto L6
        }
    }

    public interface Loadable {
        void cancelLoad();

        void load() throws IOException;
    }

    public interface ReleaseCallback {
        void onLoaderReleased();
    }

    private static final class ReleaseTask implements Runnable {
        private final ReleaseCallback callback;

        public ReleaseTask(ReleaseCallback r1) {
            this.callback = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.callback.onLoaderReleased();
        }
    }

    public static final class UnexpectedLoaderException extends IOException {
        public UnexpectedLoaderException(Throwable r4) {
            StringBuilder r0 = new StringBuilder().append("Unexpected ").append(r4.getClass().getSimpleName());
            if (r4.getMessage() == null) goto L5;
            String r1 = ": " + r4.getMessage();
        L6:
            super(r0.append(r1).toString(), r4);
            return;
        L5:
            r1 = "";
            goto L6
        }
    }

    static {
        long r1 = C0180C.TIME_UNSET;
        RETRY = createRetryAction(false, C0180C.TIME_UNSET);
        RETRY_RESET_ERROR_COUNT = createRetryAction(true, C0180C.TIME_UNSET);
        C03711 r4 = null;
        DONT_RETRY = new LoadErrorAction(2, r1, r4);
        DONT_RETRY_FATAL = new LoadErrorAction(3, r1, r4);
    }

    public Loader(ReleasableExecutor r1) {
        this.downloadExecutor = r1;
    }

    public Loader(String r3) {
        this(ReleasableExecutor.CC.from(Util.newSingleThreadExecutor(THREAD_NAME_PREFIX + r3), new Loader$$ExternalSyntheticLambda0()));
    }

    static /* synthetic */ LoadTask access$100(Loader r1) {
        return r1.currentTask;
    }

    static /* synthetic */ LoadTask access$102(Loader r0, LoadTask r1) {
        r0.currentTask = r1;
        return r1;
    }

    static /* synthetic */ IOException access$202(Loader r0, IOException r1) {
        r0.fatalError = r1;
        return r1;
    }

    static /* synthetic */ ReleasableExecutor access$500(Loader r1) {
        return r1.downloadExecutor;
    }

    public static LoadErrorAction createRetryAction(boolean r2, long r3) {
        int r4 = r2 ? 1 : 0;
        return new LoadErrorAction(r4, r3, null);
    }

    public void cancelLoading() {
        ((LoadTask) Assertions.checkStateNotNull(this.currentTask)).cancel(false);
    }

    public void clearFatalError() {
        this.fatalError = null;
    }

    public boolean hasFatalError() {
        if (this.fatalError == null) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isLoading() {
        if (this.currentTask == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.media3.exoplayer.upstream.LoaderErrorThrower
    public void maybeThrowError() throws IOException {
        maybeThrowError(Integer.MIN_VALUE);
    }

    @Override // androidx.media3.exoplayer.upstream.LoaderErrorThrower
    public void maybeThrowError(int r3) throws IOException {
        if (this.fatalError != null) goto L13;
        if (this.currentTask == null) goto L14;
        LoadTask<? extends Loadable> r0 = this.currentTask;
        if (r3 != Integer.MIN_VALUE) goto L9;
        int r1 = this.currentTask.defaultMinRetryCount;
    L10:
        r0.maybeThrowError(r1);
        return;
    L9:
        r1 = r3;
        goto L10
    L14:
        return;
    L13:
        throw this.fatalError;
    }

    public void release() {
        release(null);
    }

    public void release(ReleaseCallback r3) {
        if (this.currentTask == null) goto L5;
        this.currentTask.cancel(true);
    L5:
        if (r3 == null) goto L7;
        this.downloadExecutor.execute(new ReleaseTask(r3));
    L7:
        this.downloadExecutor.release();
    }

    public <T extends Loadable> long startLoading(T r10, Callback<T> r11, int r12) {
        Looper r3 = (Looper) Assertions.checkStateNotNull(Looper.myLooper());
        this.fatalError = null;
        long r7 = SystemClock.elapsedRealtime();
        new LoadTask(this, r3, r10, r11, r12, r7).start(0);
        return r7;
    }
}
