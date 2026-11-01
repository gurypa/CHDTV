package androidx.media3.exoplayer.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class PlatformScheduler implements Scheduler {
    private static final String KEY_REQUIREMENTS = "requirements";
    private static final String KEY_SERVICE_ACTION = "service_action";
    private static final String KEY_SERVICE_PACKAGE = "service_package";
    private static final int SUPPORTED_REQUIREMENTS = 0;
    private static final String TAG = "PlatformScheduler";
    private final int jobId;
    private final JobScheduler jobScheduler;
    private final ComponentName jobServiceComponentName;

    public static final class PlatformSchedulerService extends JobService {
        public PlatformSchedulerService() {
        }

        @Override // android.app.job.JobService
        public boolean onStartJob(JobParameters r7) {
            PersistableBundle r0 = r7.getExtras();
            int r2 = new Requirements(r0.getInt("requirements")).getNotMetRequirements(this);
            if (r2 != 0) goto L5;
            String r3 = (String) Assertions.checkNotNull(r0.getString(PlatformScheduler.KEY_SERVICE_ACTION));
            Util.startForegroundService(this, new Intent(r3).setPackage((String) Assertions.checkNotNull(r0.getString(PlatformScheduler.KEY_SERVICE_PACKAGE))));
            return false;
        L5:
            Log.m48w(PlatformScheduler.TAG, "Requirements not met: " + r2);
            jobFinished(r7, true);
            return false;
        }

        @Override // android.app.job.JobService
        public boolean onStopJob(JobParameters r2) {
            return false;
        }
    }

    static {
        if (Util.SDK_INT < 26) goto L5;
        int r0 = 16;
    L6:
        SUPPORTED_REQUIREMENTS = r0 | 15;
        return;
    L5:
        r0 = 0;
        goto L6
    }

    public PlatformScheduler(Context r3, int r4) {
        Context r5 = r3.getApplicationContext();
        this.jobId = r4;
        this.jobServiceComponentName = new ComponentName(r5, PlatformSchedulerService.class);
        this.jobScheduler = (JobScheduler) Assertions.checkNotNull((JobScheduler) r5.getSystemService("jobscheduler"));
    }

    private static JobInfo buildJobInfo(int r5, ComponentName r6, Requirements r7, String r8, String r9) {
        Requirements r0 = r7.filterRequirements(SUPPORTED_REQUIREMENTS);
        if (r0.equals(r7) == true) goto L5;
        Log.m48w(TAG, "Ignoring unsupported requirements: " + (r0.getRequirements() ^ r7.getRequirements()));
    L5:
        JobInfo.Builder r1 = new JobInfo.Builder(r5, r6);
        if (r7.isUnmeteredNetworkRequired() == false) goto L9;
        r1.setRequiredNetworkType(2);
    L11:
        r1.setRequiresDeviceIdle(r7.isIdleRequired());
        r1.setRequiresCharging(r7.isChargingRequired());
        if (Util.SDK_INT >= 26) goto L14;
    L16:
        r1.setPersisted(true);
        PersistableBundle r2 = new PersistableBundle();
        r2.putString(KEY_SERVICE_ACTION, r8);
        r2.putString(KEY_SERVICE_PACKAGE, r9);
        r2.putInt("requirements", r7.getRequirements());
        r1.setExtras(r2);
        return r1.build();
    L14:
        if (r7.isStorageNotLowRequired() == false) goto L16;
        r1.setRequiresStorageNotLow(true);
        goto L16
    L9:
        if (r7.isNetworkRequired() == false) goto L11;
        r1.setRequiredNetworkType(1);
        goto L11
    }

    @Override // androidx.media3.exoplayer.scheduler.Scheduler
    public boolean cancel() {
        this.jobScheduler.cancel(this.jobId);
        return true;
    }

    @Override // androidx.media3.exoplayer.scheduler.Scheduler
    public Requirements getSupportedRequirements(Requirements r2) {
        return r2.filterRequirements(SUPPORTED_REQUIREMENTS);
    }

    @Override // androidx.media3.exoplayer.scheduler.Scheduler
    public boolean schedule(Requirements r4, String r5, String r6) {
        JobInfo r0 = buildJobInfo(this.jobId, this.jobServiceComponentName, r4, r6, r5);
        if (this.jobScheduler.schedule(r0) == 1) goto L7;
        return false;
    L7:
        return true;
    }
}
