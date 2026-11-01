package androidx.media3.exoplayer.audio;

import android.media.AudioDeviceInfo;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.AuxEffectInfo;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.Clock;
import androidx.media3.exoplayer.analytics.PlayerId;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public interface AudioSink {
    public static final long CURRENT_POSITION_NOT_SET = Long.MIN_VALUE;
    public static final int OFFLOAD_MODE_DISABLED = 0;
    public static final int OFFLOAD_MODE_ENABLED_GAPLESS_NOT_REQUIRED = 2;
    public static final int OFFLOAD_MODE_ENABLED_GAPLESS_REQUIRED = 1;
    public static final int SINK_FORMAT_SUPPORTED_DIRECTLY = 2;
    public static final int SINK_FORMAT_SUPPORTED_WITH_TRANSCODING = 1;
    public static final int SINK_FORMAT_UNSUPPORTED = 0;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.AudioSink$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static AudioOffloadSupport $default$getFormatOffloadSupport(AudioSink r1, Format r2) {
            return AudioOffloadSupport.DEFAULT_UNSUPPORTED;
        }

        public static void $default$release(AudioSink r0) {
        }

        public static void $default$setClock(AudioSink r0, Clock r1) {
        }

        public static void $default$setOffloadDelayPadding(AudioSink r0, int r1, int r2) {
        }

        public static void $default$setOffloadMode(AudioSink r0, int r1) {
        }

        public static void $default$setOutputStreamOffsetUs(AudioSink r0, long r1) {
        }

        public static void $default$setPlayerId(AudioSink r0, PlayerId r1) {
        }

        public static void $default$setPreferredDevice(AudioSink r0, AudioDeviceInfo r1) {
        }
    }

    public static final class AudioTrackConfig {
        public final int bufferSize;
        public final int channelConfig;
        public final int encoding;
        public final boolean offload;
        public final int sampleRate;
        public final boolean tunneling;

        public AudioTrackConfig(int r1, int r2, int r3, boolean r4, boolean r5, int r6) {
            this.encoding = r1;
            this.sampleRate = r2;
            this.channelConfig = r3;
            this.tunneling = r4;
            this.offload = r5;
            this.bufferSize = r6;
        }
    }

    public static final class ConfigurationException extends Exception {
        public final Format format;

        public ConfigurationException(String r1, Format r2) {
            super(r1);
            this.format = r2;
        }

        public ConfigurationException(Throwable r1, Format r2) {
            super(r1);
            this.format = r2;
        }
    }

    public static final class InitializationException extends Exception {
        public final int audioTrackState;
        public final Format format;
        public final boolean isRecoverable;

        public InitializationException(int r8, int r9, int r10, int r11, Format r12, boolean r13, Exception r14) {
            StringBuilder r0 = new StringBuilder().append("AudioTrack init failed ").append(r8).append(" ").append("Config(").append(r9).append(", ").append(r10).append(", ").append(r11).append(")").append(" ").append(r12);
            if (r13 == false) goto L5;
            String r1 = " (recoverable)";
        L6:
            this(r0.append(r1).toString(), r8, r12, r13, r14);
            return;
        L5:
            r1 = "";
            goto L6
        }

        public InitializationException(String r1, int r2, Format r3, boolean r4, Throwable r5) {
            super(r1, r5);
            this.audioTrackState = r2;
            this.isRecoverable = r4;
            this.format = r3;
        }
    }

    public interface Listener {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.AudioSink$Listener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onAudioCapabilitiesChanged(Listener r0) {
            }

            public static void $default$onAudioSinkError(Listener r0, Exception r1) {
            }

            public static void $default$onAudioTrackInitialized(Listener r0, AudioTrackConfig r1) {
            }

            public static void $default$onAudioTrackReleased(Listener r0, AudioTrackConfig r1) {
            }

            public static void $default$onOffloadBufferEmptying(Listener r0) {
            }

            public static void $default$onOffloadBufferFull(Listener r0) {
            }

            public static void $default$onPositionAdvancing(Listener r0, long r1) {
            }

            public static void $default$onSilenceSkipped(Listener r0) {
            }
        }

        void onAudioCapabilitiesChanged();

        void onAudioSinkError(Exception r1);

        void onAudioTrackInitialized(AudioTrackConfig r1);

        void onAudioTrackReleased(AudioTrackConfig r1);

        void onOffloadBufferEmptying();

        void onOffloadBufferFull();

        void onPositionAdvancing(long r1);

        void onPositionDiscontinuity();

        void onSilenceSkipped();

        void onSkipSilenceEnabledChanged(boolean r1);

        void onUnderrun(int r1, long r2, long r4);
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface OffloadMode {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SinkFormatSupport {
    }

    public static final class UnexpectedDiscontinuityException extends Exception {
        public final long actualPresentationTimeUs;
        public final long expectedPresentationTimeUs;

        public UnexpectedDiscontinuityException(long r3, long r5) {
            super("Unexpected audio track timestamp discontinuity: expected " + r5 + ", got " + r3);
            this.actualPresentationTimeUs = r3;
            this.expectedPresentationTimeUs = r5;
        }
    }

    public static final class WriteException extends Exception {
        public final int errorCode;
        public final Format format;
        public final boolean isRecoverable;

        public WriteException(int r3, Format r4, boolean r5) {
            super("AudioTrack write failed: " + r3);
            this.isRecoverable = r5;
            this.errorCode = r3;
            this.format = r4;
        }
    }

    void configure(Format r1, int r2, int[] r3) throws ConfigurationException;

    void disableTunneling();

    void enableTunnelingV21();

    void flush();

    AudioAttributes getAudioAttributes();

    long getCurrentPositionUs(boolean r1);

    AudioOffloadSupport getFormatOffloadSupport(Format r1);

    int getFormatSupport(Format r1);

    PlaybackParameters getPlaybackParameters();

    boolean getSkipSilenceEnabled();

    boolean handleBuffer(ByteBuffer r1, long r2, int r4) throws InitializationException, WriteException;

    void handleDiscontinuity();

    boolean hasPendingData();

    boolean isEnded();

    void pause();

    void play();

    void playToEndOfStream() throws WriteException;

    void release();

    void reset();

    void setAudioAttributes(AudioAttributes r1);

    void setAudioSessionId(int r1);

    void setAuxEffectInfo(AuxEffectInfo r1);

    void setClock(Clock r1);

    void setListener(Listener r1);

    void setOffloadDelayPadding(int r1, int r2);

    void setOffloadMode(int r1);

    void setOutputStreamOffsetUs(long r1);

    void setPlaybackParameters(PlaybackParameters r1);

    void setPlayerId(PlayerId r1);

    void setPreferredDevice(AudioDeviceInfo r1);

    void setSkipSilenceEnabled(boolean r1);

    void setVolume(float r1);

    boolean supportsFormat(Format r1);
}
