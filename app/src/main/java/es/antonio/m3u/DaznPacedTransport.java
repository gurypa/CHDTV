package es.antonio.m3u;

import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
final class DaznPacedTransport implements MovistarTokenClient.Transport {
    final Clock clock;
    long last;
    final Pause pause;
    final MovistarTokenClient.Transport transport;

    interface Clock {
        long now();
    }

    interface Pause {
        void waitMs(long r1) throws InterruptedException;
    }

    DaznPacedTransport(MovistarTokenClient.Transport r3) {
        this(r3, new DaznPacedTransport$$ExternalSyntheticLambda0(), new DaznPacedTransport$$ExternalSyntheticLambda1());
    }

    DaznPacedTransport(MovistarTokenClient.Transport r3, Clock r4, Pause r5) {
        this.last = -1;
        this.transport = r3;
        this.clock = r4;
        this.pause = r5;
    }

    @Override // es.antonio.m3u.MovistarTokenClient.Transport
    public String call(String r9, String r10, Map<String, String> r11, String r12) throws Exception {
        if ("GET".equals(r9) == false) goto L15;
        if (DaznPlaybackTransport.playbackUrl(r10) == false) goto L15;
        long r0 = this.clock.now();
        long r6 = 2500;
        if (this.last < 0) goto L11;
        r6 = Math.max(0, 2500 - (r0 - this.last));
    L11:
        if (r6 <= 0) goto L13;
        this.pause.waitMs(r6);
    L13:
        this.last = this.clock.now();
    L15:
        return this.transport.call(r9, r10, r11, r12);
    }
}
