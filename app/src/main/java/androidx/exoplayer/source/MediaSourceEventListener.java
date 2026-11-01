package androidx.media3.exoplayer.source;

import android.os.Handler;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface MediaSourceEventListener {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.MediaSourceEventListener$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onDownstreamFormatChanged(MediaSourceEventListener r0, int r1, MediaSource.MediaPeriodId r2, MediaLoadData r3) {
        }

        public static void $default$onLoadCanceled(MediaSourceEventListener r0, int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4) {
        }

        public static void $default$onLoadCompleted(MediaSourceEventListener r0, int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4) {
        }

        public static void $default$onLoadError(MediaSourceEventListener r0, int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4, IOException r5, boolean r6) {
        }

        public static void $default$onLoadStarted(MediaSourceEventListener r0, int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4) {
        }

        public static void $default$onUpstreamDiscarded(MediaSourceEventListener r0, int r1, MediaSource.MediaPeriodId r2, MediaLoadData r3) {
        }
    }

    public static class EventDispatcher {
        private final CopyOnWriteArrayList<ListenerAndHandler> listenerAndHandlers;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final int windowIndex;

        private static final class ListenerAndHandler {
            public Handler handler;
            public MediaSourceEventListener listener;

            public ListenerAndHandler(Handler r1, MediaSourceEventListener r2) {
                this.handler = r1;
                this.listener = r2;
            }
        }

        public EventDispatcher() {
            this(new CopyOnWriteArrayList(), 0, null);
        }

        private EventDispatcher(CopyOnWriteArrayList<ListenerAndHandler> r1, int r2, MediaSource.MediaPeriodId r3) {
            this.listenerAndHandlers = r1;
            this.windowIndex = r2;
            this.mediaPeriodId = r3;
        }

        static /* synthetic */ void lambda$dispatchEvent$6(Consumer r0, MediaSourceEventListener r1) {
            r0.accept(r1);
        }

        public void addEventListener(Handler r3, MediaSourceEventListener r4) {
            Assertions.checkNotNull(r3);
            Assertions.checkNotNull(r4);
            this.listenerAndHandlers.add(new ListenerAndHandler(r3, r4));
        }

        public void dispatchEvent(final Consumer<MediaSourceEventListener> r6) {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final MediaSourceEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0342x95a756a1(r6, r2));
            goto L4
        }

        public void downstreamFormatChanged(int r11, Format r12, int r13, Object r14, long r15) {
            downstreamFormatChanged(new MediaLoadData(1, r11, r12, r13, r14, Util.usToMs(r15), C0180C.TIME_UNSET));
        }

        public void downstreamFormatChanged(final MediaLoadData r2) {
            dispatchEvent(new C0336x95a7569b(this, r2));
        }

        /* JADX INFO: renamed from: lambda$downstreamFormatChanged$5$androidx-media3-exoplayer-source-MediaSourceEventListener$EventDispatcher */
        /* synthetic */ void m122xc39c8e5f(MediaLoadData r3, MediaSourceEventListener r4) {
            r4.onDownstreamFormatChanged(this.windowIndex, this.mediaPeriodId, r3);
        }

        /* JADX INFO: renamed from: lambda$loadCanceled$2$androidx-media3-exoplayer-source-MediaSourceEventListener$EventDispatcher */
        /* synthetic */ void m123x7abee11a(LoadEventInfo r3, MediaLoadData r4, MediaSourceEventListener r5) {
            r5.onLoadCanceled(this.windowIndex, this.mediaPeriodId, r3, r4);
        }

        /* JADX INFO: renamed from: lambda$loadCompleted$1$androidx-media3-exoplayer-source-MediaSourceEventListener$EventDispatcher */
        /* synthetic */ void m124xeec2c4e5(LoadEventInfo r3, MediaLoadData r4, MediaSourceEventListener r5) {
            r5.onLoadCompleted(this.windowIndex, this.mediaPeriodId, r3, r4);
        }

        /* JADX INFO: renamed from: lambda$loadError$3$androidx-media3-exoplayer-source-MediaSourceEventListener$EventDispatcher */
        /* synthetic */ void m125xa1507124(LoadEventInfo r8, MediaLoadData r9, IOException r10, boolean r11, MediaSourceEventListener r12) {
            r12.onLoadError(this.windowIndex, this.mediaPeriodId, r8, r9, r10, r11);
        }

        /* JADX INFO: renamed from: lambda$loadStarted$0$androidx-media3-exoplayer-source-MediaSourceEventListener$EventDispatcher */
        /* synthetic */ void m126x7ecff69a(LoadEventInfo r3, MediaLoadData r4, MediaSourceEventListener r5) {
            r5.onLoadStarted(this.windowIndex, this.mediaPeriodId, r3, r4);
        }

        /* JADX INFO: renamed from: lambda$upstreamDiscarded$4$androidx-media3-exoplayer-source-MediaSourceEventListener$EventDispatcher */
        /* synthetic */ void m127x1ba5ea45(MediaSource.MediaPeriodId r2, MediaLoadData r3, MediaSourceEventListener r4) {
            r4.onUpstreamDiscarded(this.windowIndex, r2, r3);
        }

        public void loadCanceled(LoadEventInfo r12, int r13) {
            loadCanceled(r12, r13, -1, null, 0, null, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        }

        public void loadCanceled(LoadEventInfo r11, int r12, int r13, Format r14, int r15, Object r16, long r17, long r19) {
            loadCanceled(r11, new MediaLoadData(r12, r13, r14, r15, r16, Util.usToMs(r17), Util.usToMs(r19)));
        }

        public void loadCanceled(final LoadEventInfo r2, final MediaLoadData r3) {
            dispatchEvent(new C0340x95a7569f(this, r2, r3));
        }

        public void loadCompleted(LoadEventInfo r12, int r13) {
            loadCompleted(r12, r13, -1, null, 0, null, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        }

        public void loadCompleted(LoadEventInfo r11, int r12, int r13, Format r14, int r15, Object r16, long r17, long r19) {
            loadCompleted(r11, new MediaLoadData(r12, r13, r14, r15, r16, Util.usToMs(r17), Util.usToMs(r19)));
        }

        public void loadCompleted(final LoadEventInfo r2, final MediaLoadData r3) {
            dispatchEvent(new C0338x95a7569d(this, r2, r3));
        }

        public void loadError(LoadEventInfo r11, int r12, int r13, Format r14, int r15, Object r16, long r17, long r19, IOException r21, boolean r22) {
            loadError(r11, new MediaLoadData(r12, r13, r14, r15, r16, Util.usToMs(r17), Util.usToMs(r19)), r21, r22);
        }

        public void loadError(LoadEventInfo r14, int r15, IOException r16, boolean r17) {
            loadError(r14, r15, -1, null, 0, null, C0180C.TIME_UNSET, C0180C.TIME_UNSET, r16, r17);
        }

        public void loadError(final LoadEventInfo r7, final MediaLoadData r8, final IOException r9, final boolean r10) {
            dispatchEvent(new C0339x95a7569e(this, r7, r8, r9, r10));
        }

        public void loadStarted(LoadEventInfo r12, int r13) {
            loadStarted(r12, r13, -1, null, 0, null, C0180C.TIME_UNSET, C0180C.TIME_UNSET);
        }

        public void loadStarted(LoadEventInfo r11, int r12, int r13, Format r14, int r15, Object r16, long r17, long r19) {
            loadStarted(r11, new MediaLoadData(r12, r13, r14, r15, r16, Util.usToMs(r17), Util.usToMs(r19)));
        }

        public void loadStarted(final LoadEventInfo r2, final MediaLoadData r3) {
            dispatchEvent(new C0337x95a7569c(this, r2, r3));
        }

        public void removeEventListener(MediaSourceEventListener r4) {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L9;
            ListenerAndHandler r1 = r0.next();
            if (r1.listener != r4) goto L4;
            this.listenerAndHandlers.remove(r1);
            goto L4
        }

        public void upstreamDiscarded(int r11, long r12, long r14) {
            upstreamDiscarded(new MediaLoadData(1, r11, null, 3, null, Util.usToMs(r12), Util.usToMs(r14)));
        }

        public void upstreamDiscarded(final MediaLoadData r3) {
            final MediaSource.MediaPeriodId r0 = (MediaSource.MediaPeriodId) Assertions.checkNotNull(this.mediaPeriodId);
            dispatchEvent(new C0341x95a756a0(this, r0, r3));
        }

        public EventDispatcher withParameters(int r3, MediaSource.MediaPeriodId r4) {
            return new EventDispatcher(this.listenerAndHandlers, r3, r4);
        }

        @Deprecated
        public EventDispatcher withParameters(int r3, MediaSource.MediaPeriodId r4, long r5) {
            return new EventDispatcher(this.listenerAndHandlers, r3, r4);
        }
    }

    void onDownstreamFormatChanged(int r1, MediaSource.MediaPeriodId r2, MediaLoadData r3);

    void onLoadCanceled(int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4);

    void onLoadCompleted(int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4);

    void onLoadError(int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4, IOException r5, boolean r6);

    void onLoadStarted(int r1, MediaSource.MediaPeriodId r2, LoadEventInfo r3, MediaLoadData r4);

    void onUpstreamDiscarded(int r1, MediaSource.MediaPeriodId r2, MediaLoadData r3);
}
