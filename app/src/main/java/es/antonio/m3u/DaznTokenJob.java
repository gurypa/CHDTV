package es.antonio.m3u;

import android.app.job.JobParameters;
import android.app.job.JobService;

/* JADX INFO: loaded from: classes3.dex */
public final class DaznTokenJob extends JobService {
    public static /* synthetic */ void $r8$lambda$KJBvw8BQ1iKlLh5jtbduuaM84bw(DaznTokenJob r0, JobParameters r1) {
        r0.lambda$onStartJob$0(r1);
    }

    public static /* synthetic */ void $r8$lambda$gfOJnL9Y2yzXQyrFauNQfuLa8M4(DaznTokenJob r0, JobParameters r1, Boolean r2, Throwable r3) {
        r0.lambda$onStartJob$1(r1, r2, r3);
    }

    public DaznTokenJob() {
    }

    private /* synthetic */ void lambda$onStartJob$0(JobParameters r2) {
        jobFinished(r2, false);
    }

    private /* synthetic */ void lambda$onStartJob$1(final JobParameters r3, Boolean r4, Throwable r5) {
        DaznTokens.main.post(new DaznTokenJob$$ExternalSyntheticLambda1(this, r3));
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters r3) {
        DaznTokens.renew(this, false, null).whenComplete(new DaznTokenJob$$ExternalSyntheticLambda0(this, r3));
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters r2) {
        return DaznTokens.configured(this);
    }
}
