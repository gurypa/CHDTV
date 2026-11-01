package androidx.media3.common.audio;

/* JADX INFO: loaded from: classes.dex */
public interface SpeedProvider {
    long getNextSpeedChangeTimeUs(long r1);

    float getSpeed(long r1);
}
