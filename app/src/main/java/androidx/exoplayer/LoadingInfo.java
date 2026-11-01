package androidx.media3.exoplayer;

import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import com.google.common.base.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class LoadingInfo {
    public final long lastRebufferRealtimeMs;
    public final long playbackPositionUs;
    public final float playbackSpeed;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.LoadingInfo$1 */
    static /* synthetic */ class C02341 {
    }

    public static final class Builder {
        private long lastRebufferRealtimeMs;
        private long playbackPositionUs;
        private float playbackSpeed;

        public Builder() {
            this.playbackPositionUs = C0180C.TIME_UNSET;
            this.playbackSpeed = -3.4028235E38f;
            this.lastRebufferRealtimeMs = C0180C.TIME_UNSET;
        }

        private Builder(LoadingInfo r3) {
            this.playbackPositionUs = r3.playbackPositionUs;
            this.playbackSpeed = r3.playbackSpeed;
            this.lastRebufferRealtimeMs = r3.lastRebufferRealtimeMs;
        }

        /* synthetic */ Builder(LoadingInfo r1, C02341 r2) {
            this(r1);
        }

        static /* synthetic */ long access$100(Builder r2) {
            return r2.playbackPositionUs;
        }

        static /* synthetic */ float access$200(Builder r1) {
            return r1.playbackSpeed;
        }

        static /* synthetic */ long access$300(Builder r2) {
            return r2.lastRebufferRealtimeMs;
        }

        public LoadingInfo build() {
            return new LoadingInfo(this, null);
        }

        public Builder setLastRebufferRealtimeMs(long r3) {
            if (r3 < 0) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkArgument(r0);
            this.lastRebufferRealtimeMs = r3;
            return this;
        L5:
            if (r3 == C0180C.TIME_UNSET) goto L8;
            r0 = false;
            goto L9
        }

        public Builder setPlaybackPositionUs(long r1) {
            this.playbackPositionUs = r1;
            return this;
        }

        public Builder setPlaybackSpeed(float r2) {
            if (r2 <= 0.0f) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkArgument(r0);
            this.playbackSpeed = r2;
            return this;
        L5:
            if (r2 == (-3.4028235E38f)) goto L8;
            r0 = false;
            goto L9
        }
    }

    private LoadingInfo(Builder r3) {
        this.playbackPositionUs = Builder.access$100(r3);
        this.playbackSpeed = Builder.access$200(r3);
        this.lastRebufferRealtimeMs = Builder.access$300(r3);
    }

    /* synthetic */ LoadingInfo(Builder r1, C02341 r2) {
        this(r1);
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if ((r8 instanceof LoadingInfo) == true) goto L8;
        return false;
    L8:
        LoadingInfo r1 = (LoadingInfo) r8;
        if (this.playbackPositionUs != r1.playbackPositionUs) goto L16;
        if (this.playbackSpeed != r1.playbackSpeed) goto L16;
        if (this.lastRebufferRealtimeMs != r1.lastRebufferRealtimeMs) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{Long.valueOf(this.playbackPositionUs), Float.valueOf(this.playbackSpeed), Long.valueOf(this.lastRebufferRealtimeMs)});
    }

    public boolean rebufferedSince(long r5) {
        if (this.lastRebufferRealtimeMs != C0180C.TIME_UNSET) goto L5;
    L9:
        return false;
    L5:
        if (r5 == C0180C.TIME_UNSET) goto L9;
        if (this.lastRebufferRealtimeMs < r5) goto L9;
        return true;
    }
}
