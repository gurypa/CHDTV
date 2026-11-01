package androidx.media3.exoplayer;

import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.primitives.Longs;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultLivePlaybackSpeedControl implements LivePlaybackSpeedControl {
    public static final float DEFAULT_FALLBACK_MAX_PLAYBACK_SPEED = 1.03f;
    public static final float DEFAULT_FALLBACK_MIN_PLAYBACK_SPEED = 0.97f;
    public static final long DEFAULT_MAX_LIVE_OFFSET_ERROR_MS_FOR_UNIT_SPEED = 20;
    public static final float DEFAULT_MIN_POSSIBLE_LIVE_OFFSET_SMOOTHING_FACTOR = 0.999f;
    public static final long DEFAULT_MIN_UPDATE_INTERVAL_MS = 1000;
    public static final float DEFAULT_PROPORTIONAL_CONTROL_FACTOR = 0.1f;
    public static final long DEFAULT_TARGET_LIVE_OFFSET_INCREMENT_ON_REBUFFER_MS = 500;
    private float adjustedPlaybackSpeed;
    private long currentTargetLiveOffsetUs;
    private final float fallbackMaxPlaybackSpeed;
    private final float fallbackMinPlaybackSpeed;
    private long idealTargetLiveOffsetUs;
    private long lastPlaybackSpeedUpdateMs;
    private final long maxLiveOffsetErrorUsForUnitSpeed;
    private float maxPlaybackSpeed;
    private long maxTargetLiveOffsetUs;
    private long mediaConfigurationTargetLiveOffsetUs;
    private float minPlaybackSpeed;
    private final float minPossibleLiveOffsetSmoothingFactor;
    private long minTargetLiveOffsetUs;
    private final long minUpdateIntervalMs;
    private final float proportionalControlFactor;
    private long smoothedMinPossibleLiveOffsetDeviationUs;
    private long smoothedMinPossibleLiveOffsetUs;
    private long targetLiveOffsetOverrideUs;
    private final long targetLiveOffsetRebufferDeltaUs;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.DefaultLivePlaybackSpeedControl$1 */
    static /* synthetic */ class C02221 {
    }

    public static final class Builder {
        private float fallbackMaxPlaybackSpeed;
        private float fallbackMinPlaybackSpeed;
        private long maxLiveOffsetErrorUsForUnitSpeed;
        private float minPossibleLiveOffsetSmoothingFactor;
        private long minUpdateIntervalMs;
        private float proportionalControlFactorUs;
        private long targetLiveOffsetIncrementOnRebufferUs;

        public Builder() {
            this.fallbackMinPlaybackSpeed = 0.97f;
            this.fallbackMaxPlaybackSpeed = 1.03f;
            this.minUpdateIntervalMs = 1000;
            this.proportionalControlFactorUs = 1.0E-7f;
            this.maxLiveOffsetErrorUsForUnitSpeed = Util.msToUs(20);
            this.targetLiveOffsetIncrementOnRebufferUs = Util.msToUs(500);
            this.minPossibleLiveOffsetSmoothingFactor = 0.999f;
        }

        public DefaultLivePlaybackSpeedControl build() {
            return new DefaultLivePlaybackSpeedControl(this.fallbackMinPlaybackSpeed, this.fallbackMaxPlaybackSpeed, this.minUpdateIntervalMs, this.proportionalControlFactorUs, this.maxLiveOffsetErrorUsForUnitSpeed, this.targetLiveOffsetIncrementOnRebufferUs, this.minPossibleLiveOffsetSmoothingFactor, null);
        }

        public Builder setFallbackMaxPlaybackSpeed(float r2) {
            if (r2 < 1.0f) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.fallbackMaxPlaybackSpeed = r2;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setFallbackMinPlaybackSpeed(float r2) {
            if (0.0f < r2) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkArgument(r0);
            this.fallbackMinPlaybackSpeed = r2;
            return this;
        L5:
            if (r2 > 1.0f) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setMaxLiveOffsetErrorMsForUnitSpeed(long r3) {
            if (r3 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.maxLiveOffsetErrorUsForUnitSpeed = Util.msToUs(r3);
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setMinPossibleLiveOffsetSmoothingFactor(float r2) {
            if (r2 >= 0.0f) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkArgument(r0);
            this.minPossibleLiveOffsetSmoothingFactor = r2;
            return this;
        L5:
            if (r2 >= 1.0f) goto L7;
            r0 = true;
            goto L8
        }

        public Builder setMinUpdateIntervalMs(long r3) {
            if (r3 <= 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.minUpdateIntervalMs = r3;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setProportionalControlFactor(float r2) {
            if (r2 <= 0.0f) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.proportionalControlFactorUs = r2 / 1000000.0f;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Builder setTargetLiveOffsetIncrementOnRebufferMs(long r3) {
            if (r3 < 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.targetLiveOffsetIncrementOnRebufferUs = Util.msToUs(r3);
            return this;
        L5:
            r0 = false;
            goto L6
        }
    }

    private DefaultLivePlaybackSpeedControl(float r4, float r5, long r6, float r8, long r9, long r11, float r13) {
        this.fallbackMinPlaybackSpeed = r4;
        this.fallbackMaxPlaybackSpeed = r5;
        this.minUpdateIntervalMs = r6;
        this.proportionalControlFactor = r8;
        this.maxLiveOffsetErrorUsForUnitSpeed = r9;
        this.targetLiveOffsetRebufferDeltaUs = r11;
        this.minPossibleLiveOffsetSmoothingFactor = r13;
        this.mediaConfigurationTargetLiveOffsetUs = C0180C.TIME_UNSET;
        this.targetLiveOffsetOverrideUs = C0180C.TIME_UNSET;
        this.minTargetLiveOffsetUs = C0180C.TIME_UNSET;
        this.maxTargetLiveOffsetUs = C0180C.TIME_UNSET;
        this.minPlaybackSpeed = r4;
        this.maxPlaybackSpeed = r5;
        this.adjustedPlaybackSpeed = 1.0f;
        this.lastPlaybackSpeedUpdateMs = C0180C.TIME_UNSET;
        this.idealTargetLiveOffsetUs = C0180C.TIME_UNSET;
        this.currentTargetLiveOffsetUs = C0180C.TIME_UNSET;
        this.smoothedMinPossibleLiveOffsetUs = C0180C.TIME_UNSET;
        this.smoothedMinPossibleLiveOffsetDeviationUs = C0180C.TIME_UNSET;
    }

    /* synthetic */ DefaultLivePlaybackSpeedControl(float r1, float r2, long r3, float r5, long r6, long r8, float r10, C02221 r11) {
        this(r1, r2, r3, r5, r6, r8, r10);
    }

    private void adjustTargetLiveOffsetUs(long r18) {
        long r11 = this.smoothedMinPossibleLiveOffsetUs + (this.smoothedMinPossibleLiveOffsetDeviationUs * 3);
        if (this.currentTargetLiveOffsetUs <= r11) goto L5;
        long r3 = Util.msToUs(this.minUpdateIntervalMs);
        this.currentTargetLiveOffsetUs = Longs.max(new long[]{r11, this.idealTargetLiveOffsetUs, this.currentTargetLiveOffsetUs - (((long) ((this.adjustedPlaybackSpeed - 1.0f) * r3)) + ((long) ((this.maxPlaybackSpeed - 1.0f) * r3)))});
        return;
    L5:
        this.currentTargetLiveOffsetUs = Util.constrainValue(r18 - ((long) (Math.max(0.0f, this.adjustedPlaybackSpeed - 1.0f) / this.proportionalControlFactor)), this.currentTargetLiveOffsetUs, r11);
        if (this.maxTargetLiveOffsetUs != C0180C.TIME_UNSET) goto L8;
        return;
    L8:
        if (this.currentTargetLiveOffsetUs <= this.maxTargetLiveOffsetUs) goto L13;
        this.currentTargetLiveOffsetUs = this.maxTargetLiveOffsetUs;
        return;
    }

    private void maybeResetTargetLiveOffsetUs() {
        long r0 = C0180C.TIME_UNSET;
        if (this.mediaConfigurationTargetLiveOffsetUs == C0180C.TIME_UNSET) goto L18;
        if (this.targetLiveOffsetOverrideUs == C0180C.TIME_UNSET) goto L7;
        r0 = this.targetLiveOffsetOverrideUs;
        goto L18
    L7:
        r0 = this.mediaConfigurationTargetLiveOffsetUs;
        if (this.minTargetLiveOffsetUs == C0180C.TIME_UNSET) goto L13;
        if (r0 >= this.minTargetLiveOffsetUs) goto L13;
        r0 = this.minTargetLiveOffsetUs;
    L13:
        if (this.maxTargetLiveOffsetUs == C0180C.TIME_UNSET) goto L18;
        if (r0 <= this.maxTargetLiveOffsetUs) goto L18;
        r0 = this.maxTargetLiveOffsetUs;
    L18:
        if (this.idealTargetLiveOffsetUs != r0) goto L20;
        return;
    L20:
        this.idealTargetLiveOffsetUs = r0;
        this.currentTargetLiveOffsetUs = r0;
        this.smoothedMinPossibleLiveOffsetUs = C0180C.TIME_UNSET;
        this.smoothedMinPossibleLiveOffsetDeviationUs = C0180C.TIME_UNSET;
        this.lastPlaybackSpeedUpdateMs = C0180C.TIME_UNSET;
    }

    private static long smooth(long r3, long r5, float r7) {
        return (long) ((r3 * r7) + ((1.0f - r7) * r5));
    }

    private void updateSmoothedMinPossibleLiveOffsetUs(long r8, long r10) {
        long r0 = r8 - r10;
        if (this.smoothedMinPossibleLiveOffsetUs != C0180C.TIME_UNSET) goto L5;
        this.smoothedMinPossibleLiveOffsetUs = r0;
        this.smoothedMinPossibleLiveOffsetDeviationUs = 0;
        return;
    L5:
        this.smoothedMinPossibleLiveOffsetUs = Math.max(r0, smooth(this.smoothedMinPossibleLiveOffsetUs, r0, this.minPossibleLiveOffsetSmoothingFactor));
        long r2 = Math.abs(r0 - this.smoothedMinPossibleLiveOffsetUs);
        this.smoothedMinPossibleLiveOffsetDeviationUs = smooth(this.smoothedMinPossibleLiveOffsetDeviationUs, r2, this.minPossibleLiveOffsetSmoothingFactor);
    }

    @Override // androidx.media3.exoplayer.LivePlaybackSpeedControl
    public float getAdjustedPlaybackSpeed(long r9, long r11) {
        if (this.mediaConfigurationTargetLiveOffsetUs != C0180C.TIME_UNSET) goto L5;
        return 1.0f;
    L5:
        updateSmoothedMinPossibleLiveOffsetUs(r9, r11);
        if (this.lastPlaybackSpeedUpdateMs != C0180C.TIME_UNSET) goto L8;
    L11:
        this.lastPlaybackSpeedUpdateMs = SystemClock.elapsedRealtime();
        adjustTargetLiveOffsetUs(r9);
        long r2 = r9 - this.currentTargetLiveOffsetUs;
        if (Math.abs(r2) >= this.maxLiveOffsetErrorUsForUnitSpeed) goto L14;
        this.adjustedPlaybackSpeed = 1.0f;
    L16:
        return this.adjustedPlaybackSpeed;
    L14:
        this.adjustedPlaybackSpeed = Util.constrainValue((this.proportionalControlFactor * r2) + 1.0f, this.minPlaybackSpeed, this.maxPlaybackSpeed);
        goto L16
    L8:
        if ((SystemClock.elapsedRealtime() - this.lastPlaybackSpeedUpdateMs) >= this.minUpdateIntervalMs) goto L11;
        return this.adjustedPlaybackSpeed;
    }

    @Override // androidx.media3.exoplayer.LivePlaybackSpeedControl
    public long getTargetLiveOffsetUs() {
        return this.currentTargetLiveOffsetUs;
    }

    @Override // androidx.media3.exoplayer.LivePlaybackSpeedControl
    public void notifyRebuffer() {
        if (this.currentTargetLiveOffsetUs != C0180C.TIME_UNSET) goto L5;
        return;
    L5:
        this.currentTargetLiveOffsetUs += this.targetLiveOffsetRebufferDeltaUs;
        if (this.maxTargetLiveOffsetUs != C0180C.TIME_UNSET) goto L8;
    L10:
        this.lastPlaybackSpeedUpdateMs = C0180C.TIME_UNSET;
        return;
    L8:
        if (this.currentTargetLiveOffsetUs <= this.maxTargetLiveOffsetUs) goto L10;
        this.currentTargetLiveOffsetUs = this.maxTargetLiveOffsetUs;
        goto L10
    }

    @Override // androidx.media3.exoplayer.LivePlaybackSpeedControl
    public void setLiveConfiguration(MediaItem.LiveConfiguration r3) {
        this.mediaConfigurationTargetLiveOffsetUs = Util.msToUs(r3.targetOffsetMs);
        this.minTargetLiveOffsetUs = Util.msToUs(r3.minOffsetMs);
        this.maxTargetLiveOffsetUs = Util.msToUs(r3.maxOffsetMs);
        if (r3.minPlaybackSpeed == (-3.4028235E38f)) goto L5;
        float r0 = r3.minPlaybackSpeed;
    L6:
        this.minPlaybackSpeed = r0;
        if (r3.maxPlaybackSpeed == (-3.4028235E38f)) goto L9;
        float r1 = r3.maxPlaybackSpeed;
    L10:
        this.maxPlaybackSpeed = r1;
        if (this.minPlaybackSpeed == 1.0f) goto L13;
    L15:
        maybeResetTargetLiveOffsetUs();
        return;
    L13:
        if (this.maxPlaybackSpeed != 1.0f) goto L15;
        this.mediaConfigurationTargetLiveOffsetUs = C0180C.TIME_UNSET;
        goto L15
    L9:
        r1 = this.fallbackMaxPlaybackSpeed;
        goto L10
    L5:
        r0 = this.fallbackMinPlaybackSpeed;
        goto L6
    }

    @Override // androidx.media3.exoplayer.LivePlaybackSpeedControl
    public void setTargetLiveOffsetOverrideUs(long r1) {
        this.targetLiveOffsetOverrideUs = r1;
        maybeResetTargetLiveOffsetUs();
    }
}
