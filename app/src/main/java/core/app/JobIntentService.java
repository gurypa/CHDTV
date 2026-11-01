package androidx.core.app;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobServiceEngine;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public abstract class JobIntentService extends Service {
    static final boolean DEBUG = false;
    static final String TAG = "JobIntentService";
    static final HashMap<ComponentName, WorkEnqueuer> sClassWorkEnqueuer = null;
    static final Object sLock = null;
    final ArrayList<CompatWorkItem> mCompatQueue;
    WorkEnqueuer mCompatWorkEnqueuer;
    CommandProcessor mCurProcessor;
    boolean mDestroyed;
    boolean mInterruptIfStopped;
    CompatJobEngine mJobImpl;
    boolean mStopped;

    final class CommandProcessor extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ JobIntentService this$0;

        CommandProcessor(JobIntentService r1) {
            this.this$0 = r1;
        }

        @Override // android.os.AsyncTask
        protected /* bridge */ /* synthetic */ Void doInBackground(Void[] r1) {
            return doInBackground2(r1);
        }

        /* JADX INFO: renamed from: doInBackground, reason: avoid collision after fix types in other method */
        protected Void doInBackground2(Void... r4) {
        L2:
            GenericWorkItem r0 = this.this$0.dequeueWork();
            if (r0 == null) goto L5;
            this.this$0.onHandleWork(r0.getIntent());
            r0.complete();
            goto L2
        L5:
            return null;
        }

        @Override // android.os.AsyncTask
        protected /* bridge */ /* synthetic */ void onCancelled(Void r1) {
            onCancelled2(r1);
        }

        /* JADX INFO: renamed from: onCancelled, reason: avoid collision after fix types in other method */
        protected void onCancelled2(Void r2) {
            this.this$0.processorFinished();
        }

        @Override // android.os.AsyncTask
        protected /* bridge */ /* synthetic */ void onPostExecute(Void r1) {
            onPostExecute2(r1);
        }

        /* JADX INFO: renamed from: onPostExecute, reason: avoid collision after fix types in other method */
        protected void onPostExecute2(Void r2) {
            this.this$0.processorFinished();
        }
    }

    interface CompatJobEngine {
        IBinder compatGetBinder();

        GenericWorkItem dequeueWork();
    }

    static final class CompatWorkEnqueuer extends WorkEnqueuer {
        private final Context mContext;
        private final PowerManager.WakeLock mLaunchWakeLock;
        boolean mLaunchingService;
        private final PowerManager.WakeLock mRunWakeLock;
        boolean mServiceProcessing;

        CompatWorkEnqueuer(Context r6, ComponentName r7) {
            super(r7);
            this.mContext = r6.getApplicationContext();
            PowerManager r0 = (PowerManager) r6.getSystemService("power");
            this.mLaunchWakeLock = r0.newWakeLock(1, r7.getClassName() + ":launch");
            this.mLaunchWakeLock.setReferenceCounted(false);
            this.mRunWakeLock = r0.newWakeLock(1, r7.getClassName() + ":run");
            this.mRunWakeLock.setReferenceCounted(false);
        }

        @Override // androidx.core.app.JobIntentService.WorkEnqueuer
        void enqueueWork(Intent r5) {
            Intent r0 = new Intent(r5);
            r0.setComponent(this.mComponentName);
            if (this.mContext.startService(r0) == null) goto L15;
            monitor-enter(this);
        L12:
            th = move-exception;
            throw th;
        L6:
            if (this.mLaunchingService == true) goto L10;
            this.mLaunchingService = true;     // Catch: Throwable -> L12
            if (this.mServiceProcessing == true) goto L10;
            this.mLaunchWakeLock.acquire(60000);     // Catch: Throwable -> L12
        L10:
            monitor-exit(this);     // Catch: Throwable -> L12
            return;
        }

        @Override // androidx.core.app.JobIntentService.WorkEnqueuer
        public void serviceProcessingFinished() {
            monitor-enter(this);
        L11:
            th = move-exception;
            throw th;
        L4:
            if (this.mServiceProcessing == true) goto L6;
        L9:
            monitor-exit(this);     // Catch: Throwable -> L11
            return;
        L6:
            if (this.mLaunchingService == false) goto L8;
            this.mLaunchWakeLock.acquire(60000);     // Catch: Throwable -> L11
        L8:
            this.mServiceProcessing = false;     // Catch: Throwable -> L11
            this.mRunWakeLock.release();     // Catch: Throwable -> L11
            goto L9
        }

        @Override // androidx.core.app.JobIntentService.WorkEnqueuer
        public void serviceProcessingStarted() {
            monitor-enter(this);
        L8:
            th = move-exception;
            throw th;
        L4:
            if (this.mServiceProcessing == true) goto L6;
            this.mServiceProcessing = true;     // Catch: Throwable -> L8
            this.mRunWakeLock.acquire(600000);     // Catch: Throwable -> L8
            this.mLaunchWakeLock.release();     // Catch: Throwable -> L8
        L6:
            monitor-exit(this);     // Catch: Throwable -> L8
        }

        @Override // androidx.core.app.JobIntentService.WorkEnqueuer
        public void serviceStartReceived() {
            monitor-enter(this);
            this.mLaunchingService = false;     // Catch: Throwable -> L7
            monitor-exit(this);     // Catch: Throwable -> L7
            return;
        L7:
            th = move-exception;
            throw th;
        }
    }

    final class CompatWorkItem implements GenericWorkItem {
        final Intent mIntent;
        final int mStartId;
        final /* synthetic */ JobIntentService this$0;

        CompatWorkItem(JobIntentService r1, Intent r2, int r3) {
            this.this$0 = r1;
            this.mIntent = r2;
            this.mStartId = r3;
        }

        @Override // androidx.core.app.JobIntentService.GenericWorkItem
        public void complete() {
            this.this$0.stopSelf(this.mStartId);
        }

        @Override // androidx.core.app.JobIntentService.GenericWorkItem
        public Intent getIntent() {
            return this.mIntent;
        }
    }

    interface GenericWorkItem {
        void complete();

        Intent getIntent();
    }

    static final class JobServiceEngineImpl extends JobServiceEngine implements CompatJobEngine {
        static final boolean DEBUG = false;
        static final String TAG = "JobServiceEngineImpl";
        final Object mLock;
        JobParameters mParams;
        final JobIntentService mService;

        final class WrapperWorkItem implements GenericWorkItem {
            final JobWorkItem mJobWork;
            final /* synthetic */ JobServiceEngineImpl this$0;

            WrapperWorkItem(JobServiceEngineImpl r1, JobWorkItem r2) {
                this.this$0 = r1;
                this.mJobWork = r2;
            }

            @Override // androidx.core.app.JobIntentService.GenericWorkItem
            public void complete() {
                Object r0 = this.this$0.mLock;
                monitor-enter(r0);
            L9:
                th = move-exception;
                throw th;
            L5:
                if (this.this$0.mParams == null) goto L7;
                this.this$0.mParams.completeWork(this.mJobWork);     // Catch: Throwable -> L9
            L7:
                monitor-exit(r0);     // Catch: Throwable -> L9
            }

            @Override // androidx.core.app.JobIntentService.GenericWorkItem
            public Intent getIntent() {
                return this.mJobWork.getIntent();
            }
        }

        JobServiceEngineImpl(JobIntentService r2) {
            super(r2);
            this.mLock = new Object();
            this.mService = r2;
        }

        @Override // androidx.core.app.JobIntentService.CompatJobEngine
        public IBinder compatGetBinder() {
            return getBinder();
        }

        @Override // androidx.core.app.JobIntentService.CompatJobEngine
        public GenericWorkItem dequeueWork() {
            Object r0 = this.mLock;
            monitor-enter(r0);
        L14:
            th = move-exception;
            throw th;
        L5:
            if (this.mParams != null) goto L8;
            monitor-exit(r0);     // Catch: Throwable -> L14
            return null;
        L8:
            JobWorkItem r1 = this.mParams.dequeueWork();     // Catch: Throwable -> L14
            monitor-exit(r0);     // Catch: Throwable -> L14
            if (r1 == null) goto L13;
            r1.getIntent().setExtrasClassLoader(this.mService.getClassLoader());
            return new WrapperWorkItem(this, r1);
        L13:
            return null;
        }

        @Override // android.app.job.JobServiceEngine
        public boolean onStartJob(JobParameters r3) {
            this.mParams = r3;
            this.mService.ensureProcessorRunningLocked(false);
            return true;
        }

        @Override // android.app.job.JobServiceEngine
        public boolean onStopJob(JobParameters r4) {
            boolean r0 = this.mService.doStopCurrentWork();
            Object r1 = this.mLock;
            monitor-enter(r1);
            this.mParams = null;     // Catch: Throwable -> L8
            monitor-exit(r1);     // Catch: Throwable -> L8
            return r0;
        L8:
            th = move-exception;
            throw th;
        }
    }

    static final class JobWorkEnqueuer extends WorkEnqueuer {
        private final JobInfo mJobInfo;
        private final JobScheduler mJobScheduler;

        JobWorkEnqueuer(Context r4, ComponentName r5, int r6) {
            super(r5);
            ensureJobId(r6);
            this.mJobInfo = new JobInfo.Builder(r6, this.mComponentName).setOverrideDeadline(0).build();
            this.mJobScheduler = (JobScheduler) r4.getApplicationContext().getSystemService("jobscheduler");
        }

        @Override // androidx.core.app.JobIntentService.WorkEnqueuer
        void enqueueWork(Intent r4) {
            this.mJobScheduler.enqueue(this.mJobInfo, new JobWorkItem(r4));
        }
    }

    static abstract class WorkEnqueuer {
        final ComponentName mComponentName;
        boolean mHasJobId;
        int mJobId;

        WorkEnqueuer(ComponentName r1) {
            this.mComponentName = r1;
        }

        abstract void enqueueWork(Intent r1);

        void ensureJobId(int r4) {
            if (this.mHasJobId == true) goto L6;
            this.mHasJobId = true;
            this.mJobId = r4;
            return;
        L6:
            if (this.mJobId != r4) goto L9;
            return;
        L9:
            throw new IllegalArgumentException("Given job ID " + r4 + " is different than previous " + this.mJobId);
        }

        public void serviceProcessingFinished() {
        }

        public void serviceProcessingStarted() {
        }

        public void serviceStartReceived() {
        }
    }

    static {
        sLock = new Object();
        sClassWorkEnqueuer = new HashMap();
    }

    public JobIntentService() {
        this.mInterruptIfStopped = false;
        this.mStopped = false;
        this.mDestroyed = false;
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mCompatQueue = null;
        return;
    L5:
        this.mCompatQueue = new ArrayList();
    }

    public static void enqueueWork(Context r2, ComponentName r3, int r4, Intent r5) {
        if (r5 == null) goto L13;
        Object r0 = sLock;
        monitor-enter(r0);
        WorkEnqueuer r1 = getWorkEnqueuer(r2, r3, true, r4);     // Catch: Throwable -> L9
        r1.ensureJobId(r4);     // Catch: Throwable -> L9
        r1.enqueueWork(r5);     // Catch: Throwable -> L9
        monitor-exit(r0);     // Catch: Throwable -> L9
        return;
    L9:
        th = move-exception;
        throw th;
    L13:
        throw new IllegalArgumentException("work must not be null");
    }

    public static void enqueueWork(Context r1, Class<?> r2, int r3, Intent r4) {
        enqueueWork(r1, new ComponentName(r1, r2), r3, r4);
    }

    static WorkEnqueuer getWorkEnqueuer(Context r3, ComponentName r4, boolean r5, int r6) {
        WorkEnqueuer r0 = sClassWorkEnqueuer.get(r4);
        if (r0 == null) goto L5;
    L12:
        return r0;
    L5:
        if (Build.VERSION.SDK_INT < 26) goto L10;
        if (r5 == false) goto L9;
        r0 = new JobWorkEnqueuer(r3, r4, r6);
    L11:
        sClassWorkEnqueuer.put(r4, r0);
        goto L12
    L9:
        throw new IllegalArgumentException("Can't be here without a job id");
    L10:
        r0 = new CompatWorkEnqueuer(r3, r4);
        goto L11
    }

    GenericWorkItem dequeueWork() {
        if (this.mJobImpl != null) goto L5;
        ArrayList<CompatWorkItem> r0 = this.mCompatQueue;
        monitor-enter(r0);
    L16:
        th = move-exception;
        throw th;
    L9:
        if (this.mCompatQueue.size() <= 0) goto L13;
        CompatWorkItem r1 = this.mCompatQueue.remove(0);     // Catch: Throwable -> L16
        monitor-exit(r0);     // Catch: Throwable -> L16
        return r1;
    L13:
        monitor-exit(r0);     // Catch: Throwable -> L16
        return null;
    L5:
        return this.mJobImpl.dequeueWork();
    }

    boolean doStopCurrentWork() {
        if (this.mCurProcessor == null) goto L5;
        this.mCurProcessor.cancel(this.mInterruptIfStopped);
    L5:
        this.mStopped = true;
        return onStopCurrentWork();
    }

    void ensureProcessorRunningLocked(boolean r4) {
        if (this.mCurProcessor != null) goto L10;
        this.mCurProcessor = new CommandProcessor(this);
        if (this.mCompatWorkEnqueuer == null) goto L8;
        if (r4 == false) goto L8;
        this.mCompatWorkEnqueuer.serviceProcessingStarted();
    L8:
        this.mCurProcessor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return;
    }

    public boolean isStopped() {
        return this.mStopped;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent r2) {
        if (this.mJobImpl != null) goto L5;
        return null;
    L5:
        return this.mJobImpl.compatGetBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < 26) goto L5;
        this.mJobImpl = new JobServiceEngineImpl(this);
        this.mCompatWorkEnqueuer = null;
        return;
    L5:
        this.mJobImpl = null;
        this.mCompatWorkEnqueuer = getWorkEnqueuer(this, new ComponentName(this, getClass()), false, 0);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompatQueue == null) goto L13;
        ArrayList<CompatWorkItem> r0 = this.mCompatQueue;
        monitor-enter(r0);
        this.mDestroyed = true;     // Catch: Throwable -> L10
        this.mCompatWorkEnqueuer.serviceProcessingFinished();     // Catch: Throwable -> L10
        monitor-exit(r0);     // Catch: Throwable -> L10
        return;
    L10:
        th = move-exception;
        throw th;
    }

    protected abstract void onHandleWork(Intent r1);

    @Override // android.app.Service
    public int onStartCommand(Intent r5, int r6, int r7) {
        if (this.mCompatQueue == null) goto L17;
        this.mCompatWorkEnqueuer.serviceStartReceived();
        ArrayList<CompatWorkItem> r0 = this.mCompatQueue;
        monitor-enter(r0);
        ArrayList<CompatWorkItem> r1 = this.mCompatQueue;     // Catch: Throwable -> L14
        if (r5 == null) goto L9;
        Intent r3 = r5;
    L10:
        r1.add(new CompatWorkItem(this, r3, r7));     // Catch: Throwable -> L14
        ensureProcessorRunningLocked(true);     // Catch: Throwable -> L14
        monitor-exit(r0);     // Catch: Throwable -> L14
        return 3;
    L9:
        r3 = new Intent();     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        throw th;
    L17:
        return 2;
    }

    public boolean onStopCurrentWork() {
        return true;
    }

    void processorFinished() {
        if (this.mCompatQueue == null) goto L20;
        ArrayList<CompatWorkItem> r0 = this.mCompatQueue;
        monitor-enter(r0);
        this.mCurProcessor = null;     // Catch: Throwable -> L17
        if (this.mCompatQueue == null) goto L13;
        if (this.mCompatQueue.size() <= 0) goto L13;
        ensureProcessorRunningLocked(false);     // Catch: Throwable -> L17
    L15:
        monitor-exit(r0);     // Catch: Throwable -> L17
        return;
    L13:
        if (this.mDestroyed == true) goto L15;
        this.mCompatWorkEnqueuer.serviceProcessingFinished();     // Catch: Throwable -> L17
    L17:
        th = move-exception;
        throw th;
    }

    public void setInterruptIfStopped(boolean r1) {
        this.mInterruptIfStopped = r1;
    }
}
