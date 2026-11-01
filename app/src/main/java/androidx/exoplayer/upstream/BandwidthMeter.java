package androidx.media3.exoplayer.upstream;

import android.os.Handler;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.TransferListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface BandwidthMeter {

    /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.BandwidthMeter$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static long $default$getTimeToFirstByteEstimateUs(BandwidthMeter r2) {
            return C0180C.TIME_UNSET;
        }
    }

    public interface EventListener {

        public static final class EventDispatcher {
            private final CopyOnWriteArrayList<HandlerAndListener> listeners;

            private static final class HandlerAndListener {
                private final Handler handler;
                private final EventListener listener;
                private boolean released;

                public HandlerAndListener(Handler r1, EventListener r2) {
                    this.handler = r1;
                    this.listener = r2;
                }

                static /* synthetic */ EventListener access$000(HandlerAndListener r1) {
                    return r1.listener;
                }

                static /* synthetic */ boolean access$100(HandlerAndListener r1) {
                    return r1.released;
                }

                static /* synthetic */ Handler access$200(HandlerAndListener r1) {
                    return r1.handler;
                }

                public void release() {
                    this.released = true;
                }
            }

            public EventDispatcher() {
                this.listeners = new CopyOnWriteArrayList();
            }

            static /* synthetic */ void lambda$bandwidthSample$0(HandlerAndListener r6, int r7, long r8, long r10) {
                HandlerAndListener.access$000(r6).onBandwidthSample(r7, r8, r10);
            }

            public void addListener(Handler r3, EventListener r4) {
                Assertions.checkNotNull(r3);
                Assertions.checkNotNull(r4);
                removeListener(r4);
                this.listeners.add(new HandlerAndListener(r3, r4));
            }

            public void bandwidthSample(int r10, long r11, long r13) {
                Iterator<HandlerAndListener> r0 = this.listeners.iterator();
            L4:
                if (r0.hasNext() == false) goto L10;
                final HandlerAndListener r3 = r0.next();
                if (HandlerAndListener.access$100(r3) == true) goto L8;
                final int r4 = r10;
                final long r5 = r11;
                final long r7 = r13;
                HandlerAndListener.access$200(r3).post(new RunnableC0367xf722274e(r3, r4, r5, r7));
            L9:
                r10 = r4;
                r11 = r5;
                r13 = r7;
                goto L4
            L8:
                r4 = r10;
                r5 = r11;
                r7 = r13;
                goto L9
            }

            public void removeListener(EventListener r4) {
                Iterator<HandlerAndListener> r0 = this.listeners.iterator();
            L4:
                if (r0.hasNext() == false) goto L9;
                HandlerAndListener r1 = r0.next();
                if (HandlerAndListener.access$000(r1) != r4) goto L4;
                r1.release();
                this.listeners.remove(r1);
                goto L4
            }
        }

        void onBandwidthSample(int r1, long r2, long r4);
    }

    void addEventListener(Handler r1, EventListener r2);

    long getBitrateEstimate();

    long getTimeToFirstByteEstimateUs();

    TransferListener getTransferListener();

    void removeEventListener(EventListener r1);
}
