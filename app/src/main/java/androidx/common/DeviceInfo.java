package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class DeviceInfo {
    private static final String FIELD_MAX_VOLUME = null;
    private static final String FIELD_MIN_VOLUME = null;
    private static final String FIELD_PLAYBACK_TYPE = null;
    private static final String FIELD_ROUTING_CONTROLLER_ID = null;
    public static final int PLAYBACK_TYPE_LOCAL = 0;
    public static final int PLAYBACK_TYPE_REMOTE = 1;
    public static final DeviceInfo UNKNOWN = null;
    public final int maxVolume;
    public final int minVolume;
    public final int playbackType;
    public final String routingControllerId;

    /* JADX INFO: renamed from: androidx.media3.common.DeviceInfo$1 */
    static /* synthetic */ class C01821 {
    }

    public static final class Builder {
        private int maxVolume;
        private int minVolume;
        private final int playbackType;
        private String routingControllerId;

        public Builder(int r1) {
            this.playbackType = r1;
        }

        static /* synthetic */ int access$100(Builder r1) {
            return r1.playbackType;
        }

        static /* synthetic */ int access$200(Builder r1) {
            return r1.minVolume;
        }

        static /* synthetic */ int access$300(Builder r1) {
            return r1.maxVolume;
        }

        static /* synthetic */ String access$400(Builder r1) {
            return r1.routingControllerId;
        }

        public DeviceInfo build() {
            if (this.minVolume > this.maxVolume) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            return new DeviceInfo(this, null);
        L5:
            r0 = false;
            goto L6
        }

        public Builder setMaxVolume(int r1) {
            this.maxVolume = r1;
            return this;
        }

        public Builder setMinVolume(int r1) {
            this.minVolume = r1;
            return this;
        }

        public Builder setRoutingControllerId(String r2) {
            if (this.playbackType != 0) goto L7;
            if (r2 == null) goto L7;
            boolean r0 = false;
        L8:
            Assertions.checkArgument(r0);
            this.routingControllerId = r2;
            return this;
        L7:
            r0 = true;
            goto L8
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaybackType {
    }

    static {
        UNKNOWN = new Builder(0).build();
        FIELD_PLAYBACK_TYPE = Util.intToStringMaxRadix(0);
        FIELD_MIN_VOLUME = Util.intToStringMaxRadix(1);
        FIELD_MAX_VOLUME = Util.intToStringMaxRadix(2);
        FIELD_ROUTING_CONTROLLER_ID = Util.intToStringMaxRadix(3);
    }

    @Deprecated
    public DeviceInfo(int r2, int r3, int r4) {
        this(new Builder(r2).setMinVolume(r3).setMaxVolume(r4));
    }

    private DeviceInfo(Builder r2) {
        this.playbackType = Builder.access$100(r2);
        this.minVolume = Builder.access$200(r2);
        this.maxVolume = Builder.access$300(r2);
        this.routingControllerId = Builder.access$400(r2);
    }

    /* synthetic */ DeviceInfo(Builder r1, C01821 r2) {
        this(r1);
    }

    public static DeviceInfo fromBundle(Bundle r5) {
        int r0 = r5.getInt(FIELD_PLAYBACK_TYPE, 0);
        int r2 = r5.getInt(FIELD_MIN_VOLUME, 0);
        int r1 = r5.getInt(FIELD_MAX_VOLUME, 0);
        String r3 = r5.getString(FIELD_ROUTING_CONTROLLER_ID);
        return new Builder(r0).setMinVolume(r2).setMaxVolume(r1).setRoutingControllerId(r3).build();
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof DeviceInfo) == true) goto L8;
        return false;
    L8:
        DeviceInfo r1 = (DeviceInfo) r6;
        if (this.playbackType != r1.playbackType) goto L18;
        if (this.minVolume != r1.minVolume) goto L18;
        if (this.maxVolume != r1.maxVolume) goto L18;
        if (Util.areEqual(this.routingControllerId, r1.routingControllerId) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        int r0 = ((((((17 * 31) + this.playbackType) * 31) + this.minVolume) * 31) + this.maxVolume) * 31;
        if (this.routingControllerId != null) goto L5;
        int r2 = 0;
    L7:
        return r0 + r2;
    L5:
        r2 = this.routingControllerId.hashCode();
        goto L7
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        if (this.playbackType == 0) goto L6;
        r0.putInt(FIELD_PLAYBACK_TYPE, this.playbackType);
    L6:
        if (this.minVolume == 0) goto L9;
        r0.putInt(FIELD_MIN_VOLUME, this.minVolume);
    L9:
        if (this.maxVolume == 0) goto L12;
        r0.putInt(FIELD_MAX_VOLUME, this.maxVolume);
    L12:
        if (this.routingControllerId == null) goto L14;
        r0.putString(FIELD_ROUTING_CONTROLLER_ID, this.routingControllerId);
    L14:
        return r0;
    }
}
