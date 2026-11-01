package androidx.media3.exoplayer.audio;

import android.os.Handler;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;

/* JADX INFO: loaded from: classes.dex */
public interface AudioRendererEventListener {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.AudioRendererEventListener$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onAudioCodecError(AudioRendererEventListener r0, Exception r1) {
        }

        public static void $default$onAudioDecoderInitialized(AudioRendererEventListener r0, String r1, long r2, long r4) {
        }

        public static void $default$onAudioDecoderReleased(AudioRendererEventListener r0, String r1) {
        }

        public static void $default$onAudioDisabled(AudioRendererEventListener r0, DecoderCounters r1) {
        }

        public static void $default$onAudioEnabled(AudioRendererEventListener r0, DecoderCounters r1) {
        }

        public static void $default$onAudioInputFormatChanged(AudioRendererEventListener r0, Format r1, DecoderReuseEvaluation r2) {
        }

        public static void $default$onAudioPositionAdvancing(AudioRendererEventListener r0, long r1) {
        }

        public static void $default$onAudioSinkError(AudioRendererEventListener r0, Exception r1) {
        }

        public static void $default$onAudioTrackInitialized(AudioRendererEventListener r0, AudioSink.AudioTrackConfig r1) {
        }

        public static void $default$onAudioTrackReleased(AudioRendererEventListener r0, AudioSink.AudioTrackConfig r1) {
        }

        public static void $default$onAudioUnderrun(AudioRendererEventListener r0, int r1, long r2, long r4) {
        }

        public static void $default$onSkipSilenceEnabledChanged(AudioRendererEventListener r0, boolean r1) {
        }
    }

    public static final class EventDispatcher {
        private final Handler handler;
        private final AudioRendererEventListener listener;

        public EventDispatcher(Handler r2, AudioRendererEventListener r3) {
            if (r3 == null) goto L5;
            Handler r0 = (Handler) Assertions.checkNotNull(r2);
        L6:
            this.handler = r0;
            this.listener = r3;
            return;
        L5:
            r0 = null;
            goto L6
        }

        public void audioCodecError(final Exception r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0264x1350c7c7(this, r3));
            return;
        }

        public void audioSinkError(final Exception r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0265x1350c7c8(this, r3));
            return;
        }

        public void audioTrackInitialized(final AudioSink.AudioTrackConfig r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0262x1350c7c5(this, r3));
            return;
        }

        public void audioTrackReleased(final AudioSink.AudioTrackConfig r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0263x1350c7c6(this, r3));
            return;
        }

        public void decoderInitialized(final String r9, final long r10, final long r12) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0258x56c830ae(this, r9, r10, r12));
            return;
        }

        public void decoderReleased(final String r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0259x56c830af(this, r3));
            return;
        }

        public void disabled(final DecoderCounters r3) {
            r3.ensureUpdated();
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0257x1350c7c2(this, r3));
            return;
        }

        public void enabled(final DecoderCounters r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0256x1350c7c1(this, r3));
            return;
        }

        public void inputFormatChanged(final Format r3, final DecoderReuseEvaluation r4) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0266x1350c7c9(this, r3, r4));
            return;
        }

        /* JADX INFO: renamed from: lambda$audioCodecError$9$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m79xdf751697(Exception r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioCodecError(r2);
        }

        /* JADX INFO: renamed from: lambda$audioSinkError$8$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m80xc89a3787(Exception r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioSinkError(r2);
        }

        /* JADX INFO: renamed from: lambda$audioTrackInitialized$10$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m81xee74b056(AudioSink.AudioTrackConfig r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioTrackInitialized(r2);
        }

        /* JADX INFO: renamed from: lambda$audioTrackReleased$11$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m82x12b02702(AudioSink.AudioTrackConfig r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioTrackReleased(r2);
        }

        /* JADX INFO: renamed from: lambda$decoderInitialized$1$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m83x4e7a341d(String r8, long r9, long r11) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioDecoderInitialized(r8, r9, r11);
        }

        /* JADX INFO: renamed from: lambda$decoderReleased$5$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m84x102cf822(String r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioDecoderReleased(r2);
        }

        /* JADX INFO: renamed from: lambda$disabled$6$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m85xe116cfac(DecoderCounters r2) {
            r2.ensureUpdated();
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioDisabled(r2);
        }

        /* JADX INFO: renamed from: lambda$enabled$0$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m86x55ee20a7(DecoderCounters r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioEnabled(r2);
        }

        /* JADX INFO: renamed from: lambda$inputFormatChanged$2$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m87x1ee33b39(Format r2, DecoderReuseEvaluation r3) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioInputFormatChanged(r2, r3);
        }

        /* JADX INFO: renamed from: lambda$positionAdvancing$3$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m88xa4e1944f(long r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioPositionAdvancing(r2);
        }

        /* JADX INFO: renamed from: lambda$skipSilenceEnabledChanged$7$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m89x60b531cc(boolean r2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onSkipSilenceEnabledChanged(r2);
        }

        /* JADX INFO: renamed from: lambda$underrun$4$androidx-media3-exoplayer-audio-AudioRendererEventListener$EventDispatcher */
        /* synthetic */ void m90x8e019017(int r8, long r9, long r11) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioUnderrun(r8, r9, r11);
        }

        public void positionAdvancing(final long r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0260x1350c7c3(this, r3));
            return;
        }

        public void skipSilenceEnabledChanged(final boolean r3) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0267x1350c7ca(this, r3));
            return;
        }

        public void underrun(final int r9, final long r10, final long r12) {
            if (this.handler == null) goto L6;
            this.handler.post(new RunnableC0261x1350c7c4(this, r9, r10, r12));
            return;
        }
    }

    void onAudioCodecError(Exception r1);

    void onAudioDecoderInitialized(String r1, long r2, long r4);

    void onAudioDecoderReleased(String r1);

    void onAudioDisabled(DecoderCounters r1);

    void onAudioEnabled(DecoderCounters r1);

    void onAudioInputFormatChanged(Format r1, DecoderReuseEvaluation r2);

    void onAudioPositionAdvancing(long r1);

    void onAudioSinkError(Exception r1);

    void onAudioTrackInitialized(AudioSink.AudioTrackConfig r1);

    void onAudioTrackReleased(AudioSink.AudioTrackConfig r1);

    void onAudioUnderrun(int r1, long r2, long r4);

    void onSkipSilenceEnabledChanged(boolean r1);
}
