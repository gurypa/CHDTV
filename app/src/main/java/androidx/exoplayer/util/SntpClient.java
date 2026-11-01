package androidx.media3.exoplayer.util;

import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.upstream.Loader;
import com.google.common.base.Ascii;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

/* JADX INFO: loaded from: classes.dex */
public final class SntpClient {
    public static final String DEFAULT_NTP_HOST = "time.android.com";
    public static final int DEFAULT_TIMEOUT_MS = 1000;
    private static final int MAX_RETRY_COUNT = 10;
    private static final int NTP_LEAP_NOSYNC = 3;
    private static final int NTP_MODE_BROADCAST = 5;
    private static final int NTP_MODE_CLIENT = 3;
    private static final int NTP_MODE_SERVER = 4;
    private static final int NTP_PACKET_SIZE = 48;
    private static final int NTP_PORT = 123;
    private static final int NTP_STRATUM_DEATH = 0;
    private static final int NTP_STRATUM_MAX = 15;
    private static final int NTP_VERSION = 3;
    private static final long OFFSET_1900_TO_1970 = 2208988800L;
    private static final int ORIGINATE_TIME_OFFSET = 24;
    private static final int RECEIVE_TIME_OFFSET = 32;
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private static long elapsedRealtimeOffsetMs;
    private static boolean isInitialized;
    private static long lastUpdateElapsedRealtime;
    private static final Object loaderLock = null;
    private static long maxElapsedTimeUntilUpdateMs;
    private static String ntpHost;
    private static int timeoutMs;
    private static final Object valueLock = null;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.util.SntpClient$1 */
    static /* synthetic */ class C03801 {
    }

    public interface InitializationCallback {
        void onInitializationFailed(IOException r1);

        void onInitialized();
    }

    private static final class NtpTimeCallback implements Loader.Callback<Loader.Loadable> {
        private final InitializationCallback callback;

        public NtpTimeCallback(InitializationCallback r1) {
            this.callback = r1;
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public void onLoadCanceled(Loader.Loadable r1, long r2, long r4, boolean r6) {
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public void onLoadCompleted(Loader.Loadable r4, long r5, long r7) {
            if (this.callback != null) goto L5;
            return;
        L5:
            if (SntpClient.isInitialized() == true) goto L7;
            this.callback.onInitializationFailed(new IOException(new ConcurrentModificationException()));
            return;
        L7:
            this.callback.onInitialized();
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public Loader.LoadErrorAction onLoadError(Loader.Loadable r2, long r3, long r5, IOException r7, int r8) {
            if (this.callback == null) goto L6;
            this.callback.onInitializationFailed(r7);
        L6:
            return Loader.DONT_RETRY;
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Callback
        public /* synthetic */ void onLoadStarted(Loader.Loadable r1, long r2, long r4, int r6) {
            Loader.Callback.CC.$default$onLoadStarted(this, r1, r2, r4, r6);
        }
    }

    private static final class NtpTimeLoadable implements Loader.Loadable {
        private NtpTimeLoadable() {
        }

        /* synthetic */ NtpTimeLoadable(C03801 r1) {
            this();
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
        public void cancelLoad() {
        }

        @Override // androidx.media3.exoplayer.upstream.Loader.Loadable
        public void load() throws IOException {
            Object r0 = SntpClient.access$100();
            monitor-enter(r0);
            Object r1 = SntpClient.access$200();     // Catch: Throwable -> L24
            monitor-enter(r1);     // Catch: Throwable -> L24
        L21:
            th = move-exception;
            throw th;     // Catch: Throwable -> L24
        L7:
            if (SntpClient.access$300() == false) goto L11;
            monitor-exit(r1);     // Catch: Throwable -> L21
            monitor-exit(r0);     // Catch: Throwable -> L24
            return;
        L11:
            monitor-exit(r1);     // Catch: Throwable -> L21
            long r2 = SntpClient.access$400();     // Catch: Throwable -> L24
            Object r3 = SntpClient.access$200();     // Catch: Throwable -> L24
            monitor-enter(r3);     // Catch: Throwable -> L24
            SntpClient.access$502(SystemClock.elapsedRealtime());     // Catch: Throwable -> L18
            SntpClient.access$602(r2);     // Catch: Throwable -> L18
            SntpClient.access$302(true);     // Catch: Throwable -> L18
            monitor-exit(r3);     // Catch: Throwable -> L18
            monitor-exit(r0);     // Catch: Throwable -> L24
            return;
        L18:
            th = move-exception;
            throw th;     // Catch: Throwable -> L24
        L24:
            th = move-exception;
            throw th;
        }
    }

    static {
        loaderLock = new Object();
        valueLock = new Object();
        ntpHost = DEFAULT_NTP_HOST;
        timeoutMs = 1000;
        maxElapsedTimeUntilUpdateMs = C0180C.TIME_UNSET;
        lastUpdateElapsedRealtime = C0180C.TIME_UNSET;
    }

    private SntpClient() {
    }

    static /* synthetic */ Object access$100() {
        return loaderLock;
    }

    static /* synthetic */ Object access$200() {
        return valueLock;
    }

    static /* synthetic */ boolean access$300() {
        return isInitialized;
    }

    static /* synthetic */ boolean access$302(boolean r0) {
        isInitialized = r0;
        return r0;
    }

    static /* synthetic */ long access$400() throws IOException {
        return loadNtpTimeOffsetMs();
    }

    static /* synthetic */ long access$502(long r0) {
        lastUpdateElapsedRealtime = r0;
        return r0;
    }

    static /* synthetic */ long access$602(long r0) {
        elapsedRealtimeOffsetMs = r0;
        return r0;
    }

    private static void checkValidServerReply(byte r3, byte r4, int r5, long r6) throws IOException {
        if (r3 == 3) goto L22;
        if (r4 != 4) goto L7;
    L11:
        if (r5 == 0) goto L20;
        if (r5 > 15) goto L20;
        if (r6 == 0) goto L18;
        return;
    L18:
        throw new IOException("SNTP: Zero transmitTime");
    L20:
        throw new IOException("SNTP: Untrusted stratum: " + r5);
    L7:
        if (r4 == 5) goto L11;
        throw new IOException("SNTP: Untrusted mode: " + r4);
    L22:
        throw new IOException("SNTP: Unsynchronized server");
    }

    public static long getElapsedRealtimeOffsetMs() {
        Object r0 = valueLock;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (isInitialized == false) goto L7;
        long r1 = elapsedRealtimeOffsetMs;     // Catch: Throwable -> L10
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = C0180C.TIME_UNSET;
        goto L8
    }

    public static long getMaxElapsedTimeUntilUpdateMs() {
        Object r0 = valueLock;
        monitor-enter(r0);
        long r1 = maxElapsedTimeUntilUpdateMs;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public static String getNtpHost() {
        Object r0 = valueLock;
        monitor-enter(r0);
        String r1 = ntpHost;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public static int getTimeoutMs() {
        Object r0 = valueLock;
        monitor-enter(r0);
        int r1 = timeoutMs;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    public static void initialize(Loader r3, InitializationCallback r4) {
        if (isInitialized() == false) goto L7;
        if (r4 == null) goto L11;
        r4.onInitialized();
        return;
    L11:
        return;
    L7:
        if (r3 != null) goto L9;
        r3 = new Loader("SntpClient");
    L9:
        r3.startLoading(new NtpTimeLoadable(null), new NtpTimeCallback(r4), 1);
    }

    public static boolean isInitialized() {
        Object r0 = valueLock;
        monitor-enter(r0);
    L18:
        th = move-exception;
        throw th;
    L5:
        if (lastUpdateElapsedRealtime != C0180C.TIME_UNSET) goto L7;
    L15:
        boolean r1 = isInitialized;     // Catch: Throwable -> L18
        monitor-exit(r0);     // Catch: Throwable -> L18
        return r1;
    L7:
        if (maxElapsedTimeUntilUpdateMs == C0180C.TIME_UNSET) goto L15;
        long r2 = SystemClock.elapsedRealtime() - lastUpdateElapsedRealtime;     // Catch: Throwable -> L18
        if (isInitialized == true) goto L11;
    L13:
        boolean r3 = false;
    L14:
        isInitialized = r3;     // Catch: Throwable -> L18
        goto L15
    L11:
        if (r2 >= maxElapsedTimeUntilUpdateMs) goto L13;
        r3 = true;
        goto L14
    }

    private static long loadNtpTimeOffsetMs() throws IOException {
        DatagramSocket r1 = new DatagramSocket();
        r1.setSoTimeout(getTimeoutMs());     // Catch: Throwable -> L26
        InetAddress[] r3 = InetAddress.getAllByName(getNtpHost());     // Catch: Throwable -> L26
        int r4 = r3.length;     // Catch: Throwable -> L26
        char r5 = 0;
        SocketTimeoutException r6 = null;
        int r7 = 0;
        int r2 = 0;
    L4:
        if (r7 >= r4) goto L23;
        InetAddress r0 = r3[r7];     // Catch: Throwable -> L26
        byte[] r8 = new byte[NTP_PACKET_SIZE];     // Catch: Throwable -> L26
        DatagramPacket r9 = new DatagramPacket(r8, r8.length, r0, NTP_PORT);     // Catch: Throwable -> L26
        r8[r5] = Ascii.ESC;     // Catch: Throwable -> L26
        long r11 = System.currentTimeMillis();     // Catch: Throwable -> L26
        long r13 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L26
        writeTimestamp(r8, 40, r11);     // Catch: Throwable -> L26
        r1.send(r9);     // Catch: Throwable -> L26
        char r16 = r5;
        r1.receive(new DatagramPacket(r8, r8.length));     // Catch: SocketTimeoutException -> L12 Throwable -> L26
    L7:
        long r10 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L26
        long r17 = r11 + (r10 - r13);     // Catch: Throwable -> L26
        byte r12 = (byte) ((r8[r16] >> 6) & 3);     // Catch: Throwable -> L26
        byte r14 = (byte) (r8[r16] & 7);
        DatagramSocket r19 = r1;
        int r15 = r8[1] & 255;     // Catch: Throwable -> L24
        long r21 = readTimestamp(r8, 24);     // Catch: Throwable -> L24
        long r23 = readTimestamp(r8, 32);     // Catch: Throwable -> L24
        long r25 = readTimestamp(r8, 40);     // Catch: Throwable -> L24
        checkValidServerReply(r12, r14, r15, r25);     // Catch: Throwable -> L24
        long r33 = (r17 + (((r23 - r21) + (r25 - r17)) / 2)) - r10;
        r19.close();
        return r33;
    L12:
        e = move-exception;
        DatagramSocket r110 = r1;
        InetAddress[] r20 = r3;
        if (r6 != null) goto L16;
        r6 = e;
    L37:
        int r18 = r2 + 1;     // Catch: Throwable -> L24
        if (r2 >= 10) goto L23;
        r7 = r7 + 1;     // Catch: Throwable -> L24
        r2 = r18;
        r5 = r16;
        r1 = r110;
        r3 = r20;
        goto L4
    L16:
        r6.addSuppressed(e);     // Catch: Throwable -> L24
    L24:
        th = move-exception;
        Throwable r22 = th;
    L35:
        r110.close();     // Catch: Throwable -> L30
        throw r22;
    L30:
        th = move-exception;
        r22.addSuppressed(th);
        throw r22;
    L23:
        throw ((SocketTimeoutException) Assertions.checkNotNull(r6));     // Catch: Throwable -> L24
    L26:
        th = move-exception;
        r110 = r1;
        r22 = th;
        goto L35
    }

    private static long read32(byte[] r13, int r14) {
        byte r0 = r13[r14];
        byte r1 = r13[r14 + 1];
        byte r2 = r13[r14 + 2];
        byte r3 = r13[r14 + 3];
        if ((r0 & 128) != 128) goto L5;
        int r4 = (r0 & 127) + 128;
    L7:
        if ((r1 & 128) != 128) goto L9;
        int r6 = (r1 & 127) + 128;
    L11:
        if ((r2 & 128) != 128) goto L13;
        int r7 = (r2 & 127) + 128;
    L15:
        if ((r3 & 128) != 128) goto L17;
        int r8 = (r3 & 127) + 128;
    L19:
        return (((((long) r4) << 24) + (((long) r6) << 16)) + (((long) r7) << 8)) + ((long) r8);
    L17:
        r8 = r3;
        goto L19
    L13:
        r7 = r2;
        goto L15
    L9:
        r6 = r1;
        goto L11
    L5:
        r4 = r0;
        goto L7
    }

    private static long readTimestamp(byte[] r10, int r11) {
        long r0 = read32(r10, r11);
        long r2 = read32(r10, r11 + 4);
        if (r0 != 0) goto L8;
        if (r2 != 0) goto L8;
        return 0;
    L8:
        return ((r0 - OFFSET_1900_TO_1970) * 1000) + ((1000 * r2) / 4294967296L);
    }

    public static void setMaxElapsedTimeUntilUpdateMs(long r2) {
        Object r0 = valueLock;
        monitor-enter(r0);
        maxElapsedTimeUntilUpdateMs = r2;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public static void setNtpHost(String r2) {
        Object r0 = valueLock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (ntpHost.equals(r2) == true) goto L7;
        ntpHost = r2;     // Catch: Throwable -> L9
        isInitialized = false;     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    public static void setTimeoutMs(int r2) {
        Object r0 = valueLock;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (timeoutMs == r2) goto L7;
        timeoutMs = r2;     // Catch: Throwable -> L9
        isInitialized = false;     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    private static void writeTimestamp(byte[] r13, int r14, long r15) {
        if (r15 != 0) goto L6;
        Arrays.fill(r13, r14, r14 + 8, (byte) 0);
        return;
    L6:
        long r4 = r15 / 1000;
        long r6 = r15 - (r4 * 1000);
        long r5 = r4 + OFFSET_1900_TO_1970;
        int r0 = r14 + 1;
        r13[r14] = (byte) (r5 >> 24);
        int r16 = r0 + 1;
        r13[r0] = (byte) (r5 >> 16);
        int r1 = r16 + 1;
        r13[r16] = (byte) (r5 >> 8);
        int r17 = r1 + 1;
        r13[r1] = (byte) (r5 >> 0);
        long r2 = (4294967296L * r6) / 1000;
        int r3 = r17 + 1;
        r13[r17] = (byte) (r2 >> 24);
        int r18 = r3 + 1;
        r13[r3] = (byte) (r2 >> 16);
        int r7 = r18 + 1;
        r13[r18] = (byte) (r2 >> 8);
        int r19 = r7 + 1;
        r13[r7] = (byte) (Math.random() * 255.0d);
    }
}
