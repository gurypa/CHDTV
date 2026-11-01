package androidx.media;

import android.media.VolumeProvider;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public abstract class VolumeProviderCompat {
    public static final int VOLUME_CONTROL_ABSOLUTE = 2;
    public static final int VOLUME_CONTROL_FIXED = 0;
    public static final int VOLUME_CONTROL_RELATIVE = 1;
    private Callback mCallback;
    private final String mControlId;
    private final int mControlType;
    private int mCurrentVolume;
    private final int mMaxVolume;
    private VolumeProvider mVolumeProviderFwk;

    private static class Api21Impl {
        private Api21Impl() {
        }

        static void setCurrentVolume(VolumeProvider r0, int r1) {
            r0.setCurrentVolume(r1);
        }
    }

    public static abstract class Callback {
        public Callback() {
        }

        public abstract void onVolumeChanged(VolumeProviderCompat r1);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ControlType {
    }

    public VolumeProviderCompat(int r2, int r3, int r4) {
        this(r2, r3, r4, null);
    }

    public VolumeProviderCompat(int r1, int r2, int r3, String r4) {
        this.mControlType = r1;
        this.mMaxVolume = r2;
        this.mCurrentVolume = r3;
        this.mControlId = r4;
    }

    public final int getCurrentVolume() {
        return this.mCurrentVolume;
    }

    public final int getMaxVolume() {
        return this.mMaxVolume;
    }

    public final int getVolumeControl() {
        return this.mControlType;
    }

    public final String getVolumeControlId() {
        return this.mControlId;
    }

    public Object getVolumeProvider() {
        if (this.mVolumeProviderFwk == null) goto L5;
        final VolumeProviderCompat r3 = this;
    L10:
        return r3.mVolumeProviderFwk;
    L5:
        if (Build.VERSION.SDK_INT < 30) goto L7;
        final int r4 = this.mControlType;
        final int r5 = this.mMaxVolume;
        final int r6 = this.mCurrentVolume;
        final String r7 = this.mControlId;
        r3 = this;
        r3.mVolumeProviderFwk = new C01761(r3, r4, r5, r6, r7);
        goto L10
    L7:
        r3 = this;
        final int r1 = r3.mControlType;
        final int r2 = r3.mMaxVolume;
        final int r8 = r3.mCurrentVolume;
        r3.mVolumeProviderFwk = new C01772(this, r1, r2, r8);
        goto L10
    }

    public void onAdjustVolume(int r1) {
    }

    public void onSetVolumeTo(int r1) {
    }

    public void setCallback(Callback r1) {
        this.mCallback = r1;
    }

    public final void setCurrentVolume(int r2) {
        this.mCurrentVolume = r2;
        Api21Impl.setCurrentVolume((VolumeProvider) getVolumeProvider(), r2);
        if (this.mCallback == null) goto L6;
        this.mCallback.onVolumeChanged(this);
        return;
    }
}
