package androidx.media;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.util.ObjectsCompat;

/* JADX INFO: loaded from: classes.dex */
public class AudioFocusRequestCompat {
    static final AudioAttributesCompat FOCUS_DEFAULT_ATTR = null;
    private final AudioAttributesCompat mAudioAttributesCompat;
    private final Handler mFocusChangeHandler;
    private final int mFocusGain;
    private final Object mFrameworkAudioFocusRequest;
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    private final boolean mPauseOnDuck;

    private static class Api26Impl {
        private Api26Impl() {
        }

        static AudioFocusRequest createInstance(int r1, AudioAttributes r2, boolean r3, AudioManager.OnAudioFocusChangeListener r4, Handler r5) {
            return new AudioFocusRequest.Builder(r1).setAudioAttributes(r2).setWillPauseWhenDucked(r3).setOnAudioFocusChangeListener(r4, r5).build();
        }
    }

    public static final class Builder {
        private AudioAttributesCompat mAudioAttributesCompat;
        private Handler mFocusChangeHandler;
        private int mFocusGain;
        private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
        private boolean mPauseOnDuck;

        public Builder(int r2) {
            this.mAudioAttributesCompat = AudioFocusRequestCompat.FOCUS_DEFAULT_ATTR;
            setFocusGain(r2);
        }

        public Builder(AudioFocusRequestCompat r3) {
            this.mAudioAttributesCompat = AudioFocusRequestCompat.FOCUS_DEFAULT_ATTR;
            if (r3 == null) goto L7;
            this.mFocusGain = r3.getFocusGain();
            this.mOnAudioFocusChangeListener = r3.getOnAudioFocusChangeListener();
            this.mFocusChangeHandler = r3.getFocusChangeHandler();
            this.mAudioAttributesCompat = r3.getAudioAttributesCompat();
            this.mPauseOnDuck = r3.willPauseWhenDucked();
            return;
        L7:
            throw new IllegalArgumentException("AudioFocusRequestCompat to copy must not be null");
        }

        private static boolean isValidFocusGain(int r1) {
            switch(r1) {
                case 1: goto L5;
                case 2: goto L5;
                case 3: goto L5;
                case 4: goto L5;
                default: goto L3;
            };
        L3:
            return false;
        L5:
            return true;
        }

        public AudioFocusRequestCompat build() {
            if (this.mOnAudioFocusChangeListener == null) goto L7;
            return new AudioFocusRequestCompat(this.mFocusGain, this.mOnAudioFocusChangeListener, this.mFocusChangeHandler, this.mAudioAttributesCompat, this.mPauseOnDuck);
        L7:
            throw new IllegalStateException("Can't build an AudioFocusRequestCompat instance without a listener");
        }

        public Builder setAudioAttributes(AudioAttributesCompat r3) {
            if (r3 == null) goto L6;
            this.mAudioAttributesCompat = r3;
            return this;
        L6:
            throw new NullPointerException("Illegal null AudioAttributes");
        }

        public Builder setFocusGain(int r4) {
            if (isValidFocusGain(r4) == false) goto L7;
            this.mFocusGain = r4;
            return this;
        L7:
            throw new IllegalArgumentException("Illegal audio focus gain type " + r4);
        }

        public Builder setOnAudioFocusChangeListener(AudioManager.OnAudioFocusChangeListener r3) {
            return setOnAudioFocusChangeListener(r3, new Handler(Looper.getMainLooper()));
        }

        public Builder setOnAudioFocusChangeListener(AudioManager.OnAudioFocusChangeListener r3, Handler r4) {
            if (r3 == null) goto L9;
            if (r4 == null) goto L7;
            this.mOnAudioFocusChangeListener = r3;
            this.mFocusChangeHandler = r4;
            return this;
        L7:
            throw new IllegalArgumentException("Handler must not be null");
        L9:
            throw new IllegalArgumentException("OnAudioFocusChangeListener must not be null");
        }

        public Builder setWillPauseWhenDucked(boolean r1) {
            this.mPauseOnDuck = r1;
            return this;
        }
    }

    private static class OnAudioFocusChangeListenerHandlerCompat implements Handler.Callback, AudioManager.OnAudioFocusChangeListener {
        private static final int FOCUS_CHANGE = 2782386;
        private final Handler mHandler;
        private final AudioManager.OnAudioFocusChangeListener mListener;

        OnAudioFocusChangeListenerHandlerCompat(AudioManager.OnAudioFocusChangeListener r3, Handler r4) {
            this.mListener = r3;
            this.mHandler = new Handler(r4.getLooper(), this);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message r3) {
            if (r3.what != FOCUS_CHANGE) goto L6;
            this.mListener.onAudioFocusChange(r3.arg1);
            return true;
        L6:
            return false;
        }

        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(int r5) {
            this.mHandler.sendMessage(Message.obtain(this.mHandler, FOCUS_CHANGE, r5, 0));
        }
    }

    static {
        FOCUS_DEFAULT_ATTR = new AudioAttributesCompat.Builder().setUsage(1).build();
    }

    AudioFocusRequestCompat(int r6, AudioManager.OnAudioFocusChangeListener r7, Handler r8, AudioAttributesCompat r9, boolean r10) {
        this.mFocusGain = r6;
        this.mFocusChangeHandler = r8;
        this.mAudioAttributesCompat = r9;
        this.mPauseOnDuck = r10;
        if (Build.VERSION.SDK_INT < 26) goto L5;
    L7:
        this.mOnAudioFocusChangeListener = r7;
    L9:
        if (Build.VERSION.SDK_INT < 26) goto L11;
        this.mFrameworkAudioFocusRequest = Api26Impl.createInstance(this.mFocusGain, getAudioAttributes(), this.mPauseOnDuck, this.mOnAudioFocusChangeListener, this.mFocusChangeHandler);
        return;
    L11:
        this.mFrameworkAudioFocusRequest = null;
        return;
    L5:
        if (this.mFocusChangeHandler.getLooper() == Looper.getMainLooper()) goto L7;
        this.mOnAudioFocusChangeListener = new OnAudioFocusChangeListenerHandlerCompat(r7, r8);
        goto L9
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof AudioFocusRequestCompat) == true) goto L8;
        return false;
    L8:
        AudioFocusRequestCompat r1 = (AudioFocusRequestCompat) r6;
        if (this.mFocusGain != r1.mFocusGain) goto L20;
        if (this.mPauseOnDuck != r1.mPauseOnDuck) goto L20;
        if (ObjectsCompat.equals(this.mOnAudioFocusChangeListener, r1.mOnAudioFocusChangeListener) == false) goto L20;
        if (ObjectsCompat.equals(this.mFocusChangeHandler, r1.mFocusChangeHandler) == false) goto L20;
        if (ObjectsCompat.equals(this.mAudioAttributesCompat, r1.mAudioAttributesCompat) == false) goto L20;
        return true;
    L20:
        return false;
    }

    AudioAttributes getAudioAttributes() {
        if (this.mAudioAttributesCompat != null) goto L7;
        return null;
    L7:
        return (AudioAttributes) this.mAudioAttributesCompat.unwrap();
    }

    public AudioAttributesCompat getAudioAttributesCompat() {
        return this.mAudioAttributesCompat;
    }

    AudioFocusRequest getAudioFocusRequest() {
        return (AudioFocusRequest) this.mFrameworkAudioFocusRequest;
    }

    public Handler getFocusChangeHandler() {
        return this.mFocusChangeHandler;
    }

    public int getFocusGain() {
        return this.mFocusGain;
    }

    public AudioManager.OnAudioFocusChangeListener getOnAudioFocusChangeListener() {
        return this.mOnAudioFocusChangeListener;
    }

    public int hashCode() {
        return ObjectsCompat.hash(new Object[]{Integer.valueOf(this.mFocusGain), this.mOnAudioFocusChangeListener, this.mFocusChangeHandler, this.mAudioAttributesCompat, Boolean.valueOf(this.mPauseOnDuck)});
    }

    public boolean willPauseWhenDucked() {
        return this.mPauseOnDuck;
    }
}
