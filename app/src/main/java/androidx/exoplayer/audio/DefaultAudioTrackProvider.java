package androidx.media3.exoplayer.audio;

import android.media.AudioFormat;
import android.media.AudioTrack;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public class DefaultAudioTrackProvider implements DefaultAudioSink.AudioTrackProvider {
    public DefaultAudioTrackProvider() {
    }

    private AudioTrack createAudioTrackV21(AudioSink.AudioTrackConfig r7, AudioAttributes r8, int r9) {
        return new AudioTrack(getAudioTrackAttributesV21(r8, r7.tunneling), Util.getAudioFormat(r7.sampleRate, r7.channelConfig, r7.encoding), r7.bufferSize, 1, r9);
    }

    private AudioTrack createAudioTrackV23(AudioSink.AudioTrackConfig r6, AudioAttributes r7, int r8) {
        AudioFormat r0 = Util.getAudioFormat(r6.sampleRate, r6.channelConfig, r6.encoding);
        android.media.AudioAttributes r1 = getAudioTrackAttributesV21(r7, r6.tunneling);
        AudioTrack.Builder r2 = new AudioTrack.Builder().setAudioAttributes(r1).setAudioFormat(r0).setTransferMode(1).setBufferSizeInBytes(r6.bufferSize).setSessionId(r8);
        if (Util.SDK_INT < 29) goto L6;
        setOffloadedPlaybackV29(r2, r6.offload);
    L6:
        return customizeAudioTrackBuilder(r2).build();
    }

    private android.media.AudioAttributes getAudioTrackAttributesV21(AudioAttributes r2, boolean r3) {
        if (r3 == false) goto L6;
        return getAudioTrackTunnelingAttributesV21();
    L6:
        return r2.getAudioAttributesV21().audioAttributes;
    }

    private android.media.AudioAttributes getAudioTrackTunnelingAttributesV21() {
        return new android.media.AudioAttributes.Builder().setContentType(3).setFlags(16).setUsage(1).build();
    }

    private void setOffloadedPlaybackV29(AudioTrack.Builder r1, boolean r2) {
        r1.setOffloadedPlayback(r2);
    }

    protected AudioTrack.Builder customizeAudioTrackBuilder(AudioTrack.Builder r1) {
        return r1;
    }

    @Override // androidx.media3.exoplayer.audio.DefaultAudioSink.AudioTrackProvider
    public final AudioTrack getAudioTrack(AudioSink.AudioTrackConfig r3, AudioAttributes r4, int r5) {
        if (Util.SDK_INT < 23) goto L7;
        return createAudioTrackV23(r3, r4, r5);
    L7:
        return createAudioTrackV21(r3, r4, r5);
    }
}
