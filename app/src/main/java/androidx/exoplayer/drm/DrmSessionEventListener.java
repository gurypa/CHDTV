package androidx.media3.exoplayer.drm;

import android.os.Handler;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.MediaSource;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface DrmSessionEventListener {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.drm.DrmSessionEventListener$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onDrmKeysLoaded(DrmSessionEventListener r0, int r1, MediaSource.MediaPeriodId r2) {
        }

        public static void $default$onDrmKeysRemoved(DrmSessionEventListener r0, int r1, MediaSource.MediaPeriodId r2) {
        }

        public static void $default$onDrmKeysRestored(DrmSessionEventListener r0, int r1, MediaSource.MediaPeriodId r2) {
        }

        public static void $default$onDrmSessionAcquired(DrmSessionEventListener r0, int r1, MediaSource.MediaPeriodId r2, int r3) {
        }

        public static void $default$onDrmSessionManagerError(DrmSessionEventListener r0, int r1, MediaSource.MediaPeriodId r2, Exception r3) {
        }

        public static void $default$onDrmSessionReleased(DrmSessionEventListener r0, int r1, MediaSource.MediaPeriodId r2) {
        }
    }

    public static class EventDispatcher {
        private final CopyOnWriteArrayList<ListenerAndHandler> listenerAndHandlers;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final int windowIndex;

        private static final class ListenerAndHandler {
            public Handler handler;
            public DrmSessionEventListener listener;

            public ListenerAndHandler(Handler r1, DrmSessionEventListener r2) {
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

        public void addEventListener(Handler r3, DrmSessionEventListener r4) {
            Assertions.checkNotNull(r3);
            Assertions.checkNotNull(r4);
            this.listenerAndHandlers.add(new ListenerAndHandler(r3, r4));
        }

        public void drmKeysLoaded() {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final DrmSessionEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0286x283b7c87(this, r2));
            goto L4
        }

        public void drmKeysRemoved() {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final DrmSessionEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0283x283b7c84(this, r2));
            goto L4
        }

        public void drmKeysRestored() {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final DrmSessionEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0284x283b7c85(this, r2));
            goto L4
        }

        public void drmSessionAcquired(final int r6) {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final DrmSessionEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0285x283b7c86(this, r2, r6));
            goto L4
        }

        public void drmSessionManagerError(final Exception r6) {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final DrmSessionEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0282x283b7c83(this, r2, r6));
            goto L4
        }

        public void drmSessionReleased() {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            ListenerAndHandler r1 = r0.next();
            final DrmSessionEventListener r2 = r1.listener;
            Util.postOrRun(r1.handler, new RunnableC0287x283b7c88(this, r2));
            goto L4
        }

        /* JADX INFO: renamed from: lambda$drmKeysLoaded$1$androidx-media3-exoplayer-drm-DrmSessionEventListener$EventDispatcher */
        /* synthetic */ void m94x8fe293c0(DrmSessionEventListener r3) {
            r3.onDrmKeysLoaded(this.windowIndex, this.mediaPeriodId);
        }

        /* JADX INFO: renamed from: lambda$drmKeysRemoved$4$androidx-media3-exoplayer-drm-DrmSessionEventListener$EventDispatcher */
        /* synthetic */ void m95x5d8fdb2(DrmSessionEventListener r3) {
            r3.onDrmKeysRemoved(this.windowIndex, this.mediaPeriodId);
        }

        /* JADX INFO: renamed from: lambda$drmKeysRestored$3$androidx-media3-exoplayer-drm-DrmSessionEventListener$EventDispatcher */
        /* synthetic */ void m96xcfc47b53(DrmSessionEventListener r3) {
            r3.onDrmKeysRestored(this.windowIndex, this.mediaPeriodId);
        }

        /* JADX INFO: renamed from: lambda$drmSessionAcquired$0$androidx-media3-exoplayer-drm-DrmSessionEventListener$EventDispatcher */
        /* synthetic */ void m97x3233dce6(DrmSessionEventListener r3, int r4) {
            r3.onDrmSessionAcquired(this.windowIndex, this.mediaPeriodId, r4);
        }

        /* JADX INFO: renamed from: lambda$drmSessionManagerError$2$androidx-media3-exoplayer-drm-DrmSessionEventListener$EventDispatcher */
        /* synthetic */ void m98x18253075(DrmSessionEventListener r3, Exception r4) {
            r3.onDrmSessionManagerError(this.windowIndex, this.mediaPeriodId, r4);
        }

        /* JADX INFO: renamed from: lambda$drmSessionReleased$5$androidx-media3-exoplayer-drm-DrmSessionEventListener$EventDispatcher */
        /* synthetic */ void m99x690251a(DrmSessionEventListener r3) {
            r3.onDrmSessionReleased(this.windowIndex, this.mediaPeriodId);
        }

        public void removeEventListener(DrmSessionEventListener r4) {
            Iterator<ListenerAndHandler> r0 = this.listenerAndHandlers.iterator();
        L4:
            if (r0.hasNext() == false) goto L9;
            ListenerAndHandler r1 = r0.next();
            if (r1.listener != r4) goto L4;
            this.listenerAndHandlers.remove(r1);
            goto L4
        }

        public EventDispatcher withParameters(int r3, MediaSource.MediaPeriodId r4) {
            return new EventDispatcher(this.listenerAndHandlers, r3, r4);
        }
    }

    void onDrmKeysLoaded(int r1, MediaSource.MediaPeriodId r2);

    void onDrmKeysRemoved(int r1, MediaSource.MediaPeriodId r2);

    void onDrmKeysRestored(int r1, MediaSource.MediaPeriodId r2);

    void onDrmSessionAcquired(int r1, MediaSource.MediaPeriodId r2, int r3);

    void onDrmSessionManagerError(int r1, MediaSource.MediaPeriodId r2, Exception r3);

    void onDrmSessionReleased(int r1, MediaSource.MediaPeriodId r2);
}
