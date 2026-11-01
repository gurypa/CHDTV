package androidx.media3.exoplayer;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Handler;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
final class AudioFocusManager {
    private static final int AUDIOFOCUS_GAIN = 1;
    private static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    private static final int AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE = 4;
    private static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    private static final int AUDIOFOCUS_NONE = 0;
    private static final int AUDIO_FOCUS_STATE_HAVE_FOCUS = 2;
    private static final int AUDIO_FOCUS_STATE_LOSS_TRANSIENT = 3;
    private static final int AUDIO_FOCUS_STATE_LOSS_TRANSIENT_DUCK = 4;
    private static final int AUDIO_FOCUS_STATE_NOT_REQUESTED = 0;
    private static final int AUDIO_FOCUS_STATE_NO_FOCUS = 1;
    public static final int PLAYER_COMMAND_DO_NOT_PLAY = -1;
    public static final int PLAYER_COMMAND_PLAY_WHEN_READY = 1;
    public static final int PLAYER_COMMAND_WAIT_FOR_CALLBACK = 0;
    private static final String TAG = "AudioFocusManager";
    private static final float VOLUME_MULTIPLIER_DEFAULT = 1.0f;
    private static final float VOLUME_MULTIPLIER_DUCK = 0.2f;
    private AudioAttributes audioAttributes;
    private AudioFocusRequest audioFocusRequest;
    private int audioFocusState;
    private final Supplier<AudioManager> audioManager;
    private int focusGainToRequest;
    private final AudioFocusListener focusListener;
    private PlayerControl playerControl;
    private boolean rebuildAudioFocusRequest;
    private float volumeMultiplier;

    private class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
        private final Handler eventHandler;
        final /* synthetic */ AudioFocusManager this$0;

        public AudioFocusListener(AudioFocusManager r1, Handler r2) {
            this.this$0 = r1;
            this.eventHandler = r2;
        }

        /* JADX INFO: renamed from: lambda$onAudioFocusChange$0$androidx-media3-exoplayer-AudioFocusManager$AudioFocusListener */
        /* synthetic */ void m58x93d291e3(int r2) {
            AudioFocusManager.access$000(this.this$0, r2);
        }

        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(final int r3) {
            this.eventHandler.post(new AudioFocusManager$AudioFocusListener$$ExternalSyntheticLambda0(this, r3));
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerCommand {
    }

    public interface PlayerControl {
        void executePlayerCommand(int r1);

        void setVolumeMultiplier(float r1);
    }

    public AudioFocusManager(final Context r2, Handler r3, PlayerControl r4) {
        this.volumeMultiplier = 1.0f;
        this.audioManager = Suppliers.memoize(new AudioFocusManager$$ExternalSyntheticLambda0(r2));
        this.playerControl = r4;
        this.focusListener = new AudioFocusListener(this, r3);
        this.audioFocusState = 0;
    }

    private void abandonAudioFocusDefault() {
        this.audioManager.get().abandonAudioFocus(this.focusListener);
    }

    private void abandonAudioFocusIfHeld() {
        if (this.audioFocusState != 1) goto L5;
        return;
    L5:
        if (this.audioFocusState != 0) goto L8;
        return;
    L8:
        if (Util.SDK_INT < 26) goto L10;
        abandonAudioFocusV26();
        return;
    L10:
        abandonAudioFocusDefault();
    }

    private void abandonAudioFocusV26() {
        if (this.audioFocusRequest == null) goto L6;
        this.audioManager.get().abandonAudioFocusRequest(this.audioFocusRequest);
        return;
    }

    static /* synthetic */ void access$000(AudioFocusManager r0, int r1) {
        r0.handlePlatformAudioFocusChange(r1);
    }

    private static int convertAudioAttributesToFocusGain(AudioAttributes r6) {
        if (r6 != null) goto L6;
        return 0;
    L6:
        switch(r6.usage) {
            case 0: goto L19;
            case 1: goto L18;
            case 2: goto L17;
            case 3: goto L16;
            case 4: goto L17;
            case 5: goto L15;
            case 6: goto L15;
            case 7: goto L15;
            case 8: goto L15;
            case 9: goto L15;
            case 10: goto L15;
            case 11: goto L12;
            case 12: goto L15;
            case 13: goto L15;
            case 14: goto L18;
            case 15: goto L7;
            case 16: goto L9;
            default: goto L7;
        };
    L7:
        Log.m48w(TAG, "Unidentified audio usage: " + r6.usage);
        return 0;
    L9:
        return 4;
    L15:
        return 3;
    L16:
        return 0;
    L17:
        return 2;
    L18:
        return 1;
    L19:
        Log.m48w(TAG, "Specify a proper usage in the audio attributes for audio focus handling. Using AUDIOFOCUS_GAIN by default.");
        return 1;
    L12:
        if (r6.contentType != 1) goto L14;
        return 2;
    L14:
        return 3;
    }

    private void executePlayerCommand(int r2) {
        if (this.playerControl == null) goto L6;
        this.playerControl.executePlayerCommand(r2);
        return;
    }

    private void handlePlatformAudioFocusChange(int r3) {
        switch(r3) {
            case -3: goto L11;
            case -2: goto L11;
            case -1: goto L8;
            case 0: goto L4;
            case 1: goto L6;
            default: goto L4;
        };
    L4:
        Log.m48w(TAG, "Unknown focus change type: " + r3);
        return;
    L6:
        setAudioFocusState(2);
        executePlayerCommand(1);
        return;
    L8:
        executePlayerCommand(-1);
        abandonAudioFocusIfHeld();
        setAudioFocusState(1);
        return;
    L11:
        if (r3 != (-2)) goto L13;
    L16:
        executePlayerCommand(0);
        setAudioFocusState(3);
        return;
    L13:
        if (willPauseWhenDucked() == true) goto L16;
        setAudioFocusState(4);
    }

    static /* synthetic */ AudioManager lambda$new$0(Context r2) {
        return (AudioManager) Assertions.checkNotNull((AudioManager) r2.getApplicationContext().getSystemService(MimeTypes.BASE_TYPE_AUDIO));
    }

    private int requestAudioFocus() {
        if (this.audioFocusState != 2) goto L6;
        return 1;
    L6:
        if (Util.SDK_INT < 26) goto L8;
        int r0 = requestAudioFocusV26();
    L9:
        if (r0 != 1) goto L12;
        setAudioFocusState(2);
        return 1;
    L12:
        setAudioFocusState(1);
        return -1;
    L8:
        r0 = requestAudioFocusDefault();
        goto L9
    }

    private int requestAudioFocusDefault() {
        return this.audioManager.get().requestAudioFocus(this.focusListener, Util.getStreamTypeForAudioUsage(((AudioAttributes) Assertions.checkNotNull(this.audioAttributes)).usage), this.focusGainToRequest);
    }

    private int requestAudioFocusV26() {
        if (this.audioFocusRequest == null) goto L7;
        if (this.rebuildAudioFocusRequest == true) goto L7;
    L12:
        return this.audioManager.get().requestAudioFocus(this.audioFocusRequest);
    L7:
        if (this.audioFocusRequest != null) goto L9;
        AudioFocusRequest.Builder r0 = new AudioFocusRequest.Builder(this.focusGainToRequest);
    L10:
        boolean r1 = willPauseWhenDucked();
        this.audioFocusRequest = r0.setAudioAttributes(((AudioAttributes) Assertions.checkNotNull(this.audioAttributes)).getAudioAttributesV21().audioAttributes).setWillPauseWhenDucked(r1).setOnAudioFocusChangeListener(this.focusListener).build();
        this.rebuildAudioFocusRequest = false;
        goto L12
    L9:
        r0 = new AudioFocusRequest.Builder(this.audioFocusRequest);
        goto L10
    }

    private void setAudioFocusState(int r3) {
        if (this.audioFocusState != r3) goto L5;
        return;
    L5:
        this.audioFocusState = r3;
        if (r3 != 4) goto L8;
        float r0 = 0.2f;
    L10:
        if (this.volumeMultiplier != r0) goto L12;
        return;
    L12:
        this.volumeMultiplier = r0;
        if (this.playerControl == null) goto L16;
        this.playerControl.setVolumeMultiplier(r0);
        return;
    L16:
        return;
    L8:
        r0 = 1.0f;
        goto L10
    }

    private boolean shouldHandleAudioFocus(int r3) {
        if (r3 != 1) goto L5;
    L7:
        return false;
    L5:
        if (this.focusGainToRequest != 1) goto L7;
        return true;
    }

    private boolean willPauseWhenDucked() {
        if (this.audioAttributes != null) goto L5;
    L7:
        return false;
    L5:
        if (this.audioAttributes.contentType != 1) goto L7;
        return true;
    }

    AudioManager.OnAudioFocusChangeListener getFocusListener() {
        return this.focusListener;
    }

    public float getVolumeMultiplier() {
        return this.volumeMultiplier;
    }

    public void release() {
        this.playerControl = null;
        abandonAudioFocusIfHeld();
        setAudioFocusState(0);
    }

    public void setAudioAttributes(AudioAttributes r3) {
        if (Util.areEqual(this.audioAttributes, r3) == true) goto L12;
        this.audioAttributes = r3;
        this.focusGainToRequest = convertAudioAttributesToFocusGain(r3);
        boolean r1 = true;
        if (this.focusGainToRequest != 1) goto L7;
    L10:
        Assertions.checkArgument(r1, "Automatic handling of audio focus is only available for USAGE_MEDIA and USAGE_GAME.");
        return;
    L7:
        if (this.focusGainToRequest == 0) goto L10;
        r1 = false;
        goto L10
    }

    public int updateAudioFocus(boolean r4, int r5) {
        if (shouldHandleAudioFocus(r5) == true) goto L6;
        abandonAudioFocusIfHeld();
        setAudioFocusState(0);
        return 1;
    L6:
        if (r4 == false) goto L10;
        return requestAudioFocus();
    L10:
        switch(this.audioFocusState) {
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L12;
            default: goto L11;
        };
    L11:
        return 1;
    L12:
        return 0;
    L13:
        return -1;
    }
}
