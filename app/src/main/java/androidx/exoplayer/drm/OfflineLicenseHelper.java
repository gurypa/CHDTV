package androidx.media3.exoplayer.drm;

import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Pair;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.analytics.PlayerId;
import com.google.common.util.concurrent.SettableFuture;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes.dex */
public final class OfflineLicenseHelper {
    private static final Format FORMAT_WITH_EMPTY_DRM_INIT_DATA = null;
    private final ConditionVariable drmListenerConditionVariable;
    private final DefaultDrmSessionManager drmSessionManager;
    private final DrmSessionEventListener.EventDispatcher eventDispatcher;
    private final Handler handler;
    private final HandlerThread handlerThread;

    static {
        FORMAT_WITH_EMPTY_DRM_INIT_DATA = new Format.Builder().setDrmInitData(new DrmInitData(new DrmInitData.SchemeData[0])).build();
    }

    public OfflineLicenseHelper(DefaultDrmSessionManager r4, DrmSessionEventListener.EventDispatcher r5) {
        this.drmSessionManager = r4;
        this.eventDispatcher = r5;
        this.handlerThread = new HandlerThread("ExoPlayer:OfflineLicenseHelper");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
        this.drmListenerConditionVariable = new ConditionVariable();
        DrmSessionEventListener r0 = new C02891(this);
        r5.addEventListener(new Handler(this.handlerThread.getLooper()), r0);
    }

    static /* synthetic */ ConditionVariable access$000(OfflineLicenseHelper r1) {
        return r1.drmListenerConditionVariable;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private DrmSession acquireFirstSessionOnHandlerThread(final int r8, final byte[] r9, final Format r10) throws DrmSession.DrmSessionException {
        Assertions.checkNotNull(r10.drmInitData);
        final SettableFuture r5 = SettableFuture.create();
        this.drmListenerConditionVariable.close();
        this.handler.post(new OfflineLicenseHelper$$ExternalSyntheticLambda1(this, r8, r9, r5, r10));
        final DrmSession r11 = (DrmSession) r5.get();     // Catch: Throwable -> L15 ExecutionException -> L17
        this.drmListenerConditionVariable.block();
        final SettableFuture r12 = SettableFuture.create();
        this.handler.post(new OfflineLicenseHelper$$ExternalSyntheticLambda2(this, r11, r12));
    L12:
        e = move-exception;
        throw new IllegalStateException(e);
    L6:
        if (r12.get() != 0) goto L9;
        return r11;
    L9:
        throw ((DrmSession.DrmSessionException) r12.get());     // Catch: ExecutionException -> L10 Throwable -> L12
    L15:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private byte[] acquireSessionAndGetOfflineLicenseKeySetIdOnHandlerThread(int r5, byte[] r6, Format r7) throws DrmSession.DrmSessionException {
        final DrmSession r0 = acquireFirstSessionOnHandlerThread(r5, r6, r7);
        final SettableFuture r1 = SettableFuture.create();
        this.handler.post(new OfflineLicenseHelper$$ExternalSyntheticLambda4(this, r1, r0));
        byte[] r2 = (byte[]) Assertions.checkNotNull((byte[]) r1.get());     // Catch: Throwable -> L6 Throwable -> L8 ExecutionException -> L10
        releaseManagerOnHandlerThread();
        return r2;
    L6:
        th = move-exception;
        releaseManagerOnHandlerThread();
        throw th;
    L8:
        e = move-exception;
        throw new IllegalStateException(e);     // Catch: Throwable -> L6
    }

    public static OfflineLicenseHelper newWidevineInstance(String r1, DataSource.Factory r2, DrmSessionEventListener.EventDispatcher r3) {
        return newWidevineInstance(r1, false, r2, r3);
    }

    public static OfflineLicenseHelper newWidevineInstance(String r1, boolean r2, DataSource.Factory r3, DrmSessionEventListener.EventDispatcher r4) {
        return newWidevineInstance(r1, r2, r3, null, r4);
    }

    public static OfflineLicenseHelper newWidevineInstance(String r3, boolean r4, DataSource.Factory r5, Map<String, String> r6, DrmSessionEventListener.EventDispatcher r7) {
        return new OfflineLicenseHelper(new DefaultDrmSessionManager.Builder().setKeyRequestParameters(r6).build(new HttpMediaDrmCallback(r3, r4, r5)), r7);
    }

    private void releaseManagerOnHandlerThread() {
        final SettableFuture r0 = SettableFuture.create();
        this.handler.post(new OfflineLicenseHelper$$ExternalSyntheticLambda3(this, r0));
        r0.get();     // Catch: ExecutionException -> L5 Throwable -> L7
        return;
    L7:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    public synchronized byte[] downloadLicense(Format r3) throws DrmSession.DrmSessionException {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (r3.drmInitData == null) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkArgument(r0);     // Catch: Throwable -> L10
        byte[] r1 = acquireSessionAndGetOfflineLicenseKeySetIdOnHandlerThread(2, null, r3);     // Catch: Throwable -> L10
        monitor-exit(this);
        return r1;
    L6:
        r0 = false;
        goto L7
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized Pair<Long, Long> getLicenseDurationRemainingSec(byte[] r5) throws DrmSession.DrmSessionException {
        monitor-enter(this);
        Assertions.checkNotNull(r5);     // Catch: Throwable -> L26
        final DrmSession r0 = acquireFirstSessionOnHandlerThread(1, r5, FORMAT_WITH_EMPTY_DRM_INIT_DATA);     // Catch: DrmSession.DrmSessionException -> L19 Throwable -> L26
        final SettableFuture r1 = SettableFuture.create();     // Catch: Throwable -> L26
        this.handler.post(new OfflineLicenseHelper$$ExternalSyntheticLambda0(this, r1, r0));     // Catch: Throwable -> L26
        Pair<Long, Long> r2 = (Pair) r1.get();     // Catch: Throwable -> L10 InterruptedException -> L12 Throwable -> L14
        releaseManagerOnHandlerThread();     // Catch: Throwable -> L26
        monitor-exit(this);
        return r2;
    L10:
        th = move-exception;
        releaseManagerOnHandlerThread();     // Catch: Throwable -> L26
        throw th;     // Catch: Throwable -> L26
    L14:
        e = move-exception;
        throw new IllegalStateException(e);     // Catch: Throwable -> L10
    L19:
        e = move-exception;
        if ((e.getCause() instanceof KeysExpiredException) == true) goto L24;
        throw e;     // Catch: Throwable -> L26
    L24:
        return Pair.create(0L, 0L);
    L26:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: lambda$acquireFirstSessionOnHandlerThread$2$androidx-media3-exoplayer-drm-OfflineLicenseHelper */
    /* synthetic */ void m103xf4292904(int r4, byte[] r5, SettableFuture r6, Format r7) {
        this.drmSessionManager.setPlayer((Looper) Assertions.checkNotNull(Looper.myLooper()), PlayerId.UNSET);     // Catch: Throwable -> L8
        this.drmSessionManager.prepare();     // Catch: Throwable -> L8
        this.drmSessionManager.setMode(r4, r5);     // Catch: Throwable -> L5
        r6.set((DrmSession) Assertions.checkNotNull(this.drmSessionManager.acquireSession(this.eventDispatcher, r7)));     // Catch: Throwable -> L5
    L14:
        return;
    L5:
        th = move-exception;
        this.drmSessionManager.release();     // Catch: Throwable -> L8
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        r6.setException(th);
    }

    /* JADX INFO: renamed from: lambda$acquireFirstSessionOnHandlerThread$3$androidx-media3-exoplayer-drm-OfflineLicenseHelper */
    /* synthetic */ void m104x2201c363(DrmSession r4, SettableFuture r5) {
        DrmSession.DrmSessionException r0 = r4.getError();     // Catch: Throwable -> L7
        if (r4.getState() != 1) goto L5;
        r4.release(this.eventDispatcher);     // Catch: Throwable -> L7
        this.drmSessionManager.release();     // Catch: Throwable -> L7
    L5:
        r5.set(r0);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        r5.setException(th);
        r4.release(this.eventDispatcher);
        this.drmSessionManager.release();
    }

    /* JADX INFO: renamed from: lambda$acquireSessionAndGetOfflineLicenseKeySetIdOnHandlerThread$1$androidx-media3-exoplayer-drm-OfflineLicenseHelper */
    /* synthetic */ void m105x4ec00c9e(SettableFuture r3, DrmSession r4) {
        r3.set(r4.getOfflineLicenseKeySetId());     // Catch: Throwable -> L4
    L3:
        r4.release(this.eventDispatcher);
        return;
    L4:
        th = move-exception;
        r3.setException(th);     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        r4.release(this.eventDispatcher);
        throw th;
    }

    /* JADX INFO: renamed from: lambda$getLicenseDurationRemainingSec$0$androidx-media3-exoplayer-drm-OfflineLicenseHelper */
    /* synthetic */ void m106xc6195285(SettableFuture r3, DrmSession r4) {
        r3.set((Pair) Assertions.checkNotNull(WidevineUtil.getLicenseDurationRemainingSec(r4)));     // Catch: Throwable -> L4
    L6:
        r4.release(this.eventDispatcher);
        return;
    L4:
        th = move-exception;
        r3.setException(th);     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        r4.release(this.eventDispatcher);
        throw th;
    }

    /* JADX INFO: renamed from: lambda$releaseManagerOnHandlerThread$4$androidx-media3-exoplayer-drm-OfflineLicenseHelper */
    /* synthetic */ void m107xf255d686(SettableFuture r2) {
        this.drmSessionManager.release();     // Catch: Throwable -> L4
        r2.set(null);     // Catch: Throwable -> L4
        return;
    L4:
        th = move-exception;
        r2.setException(th);
    }

    public void release() {
        this.handlerThread.quit();
    }

    public synchronized void releaseLicense(byte[] r3) throws DrmSession.DrmSessionException {
        monitor-enter(this);
        Assertions.checkNotNull(r3);     // Catch: Throwable -> L6
        acquireSessionAndGetOfflineLicenseKeySetIdOnHandlerThread(3, r3, FORMAT_WITH_EMPTY_DRM_INIT_DATA);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized byte[] renewLicense(byte[] r3) throws DrmSession.DrmSessionException {
        monitor-enter(this);
        Assertions.checkNotNull(r3);     // Catch: Throwable -> L6
        byte[] r0 = acquireSessionAndGetOfflineLicenseKeySetIdOnHandlerThread(2, r3, FORMAT_WITH_EMPTY_DRM_INIT_DATA);     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }
}
