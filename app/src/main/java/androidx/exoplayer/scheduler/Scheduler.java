package androidx.media3.exoplayer.scheduler;

/* JADX INFO: loaded from: classes.dex */
public interface Scheduler {
    boolean cancel();

    Requirements getSupportedRequirements(Requirements r1);

    boolean schedule(Requirements r1, String r2, String r3);
}
