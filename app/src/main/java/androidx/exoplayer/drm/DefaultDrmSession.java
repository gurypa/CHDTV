package androidx.media3.exoplayer.drm;

import android.media.NotProvisionedException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.CopyOnWriteMultiset;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/* JADX INFO: loaded from: classes.dex */
class DefaultDrmSession implements DrmSession {
    private static final int MAX_LICENSE_DURATION_TO_RENEW_SECONDS = 60;
    private static final int MSG_KEYS = 2;
    private static final int MSG_PROVISION = 1;
    private static final String TAG = "DefaultDrmSession";
    private final MediaDrmCallback callback;
    private CryptoConfig cryptoConfig;
    private ExoMediaDrm.KeyRequest currentKeyRequest;
    private ExoMediaDrm.ProvisionRequest currentProvisionRequest;
    private final CopyOnWriteMultiset<DrmSessionEventListener.EventDispatcher> eventDispatchers;
    private final boolean isPlaceholderSession;
    private final HashMap<String, String> keyRequestParameters;
    private DrmSession.DrmSessionException lastException;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final ExoMediaDrm mediaDrm;
    private final int mode;
    private byte[] offlineLicenseKeySetId;
    private final boolean playClearSamplesWithoutKeys;
    private final Looper playbackLooper;
    private final PlayerId playerId;
    private final ProvisioningManager provisioningManager;
    private int referenceCount;
    private final ReferenceCountListener referenceCountListener;
    private RequestHandler requestHandler;
    private HandlerThread requestHandlerThread;
    private final ResponseHandler responseHandler;
    public final List<DrmInitData.SchemeData> schemeDatas;
    private byte[] sessionId;
    private int state;
    private final UUID uuid;

    public interface ProvisioningManager {
        void onProvisionCompleted();

        void onProvisionError(Exception r1, boolean r2);

        void provisionRequired(DefaultDrmSession r1);
    }

    public interface ReferenceCountListener {
        void onReferenceCountDecremented(DefaultDrmSession r1, int r2);

        void onReferenceCountIncremented(DefaultDrmSession r1, int r2);
    }

    private class RequestHandler extends Handler {
        private boolean isReleased;
        final /* synthetic */ DefaultDrmSession this$0;

        public RequestHandler(DefaultDrmSession r1, Looper r2) {
            this.this$0 = r1;
            super(r2);
        }

        private boolean maybeRetryRequest(Message r22, MediaDrmCallbackException r23) {
            RequestTask r4 = (RequestTask) r22.obj;
            if (r4.allowRetry == true) goto L5;
            return false;
        L5:
            r4.errorCount++;
            if (r4.errorCount <= DefaultDrmSession.access$400(this.this$0).getMinimumLoadableRetryCount(3)) goto L8;
            return false;
        L8:
            LoadEventInfo r9 = new LoadEventInfo(r4.taskId, r23.dataSpec, r23.uriAfterRedirects, r23.responseHeaders, SystemClock.elapsedRealtime(), SystemClock.elapsedRealtime() - r4.startTimeMs, r23.bytesLoaded);
            MediaLoadData r5 = new MediaLoadData(3);
            if ((r23.getCause() instanceof IOException) == false) goto L11;
            IOException r6 = (IOException) r23.getCause();
        L12:
            long r10 = DefaultDrmSession.access$400(this.this$0).getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(r9, r5, r6, r4.errorCount));
            if (r10 != C0180C.TIME_UNSET) goto L15;
            return false;
        L15:
            monitor-enter(this);
        L23:
            th = move-exception;
            throw th;
        L17:
            if (this.isReleased == true) goto L21;
            sendMessageDelayed(Message.obtain(r22), r10);     // Catch: Throwable -> L23
            monitor-exit(this);     // Catch: Throwable -> L23
            return true;
        L21:
            monitor-exit(this);     // Catch: Throwable -> L23
            return false;
        L11:
            r6 = new UnexpectedDrmSessionException(r23.getCause());
            goto L12
        }

        @Override // android.os.Handler
        public void handleMessage(Message r6) {
            RequestTask r0 = (RequestTask) r6.obj;
        L12:
            e = move-exception;
            boolean r2 = maybeRetryRequest(r6, e);
            Object r1 = e;
            if (r2 == false) goto L18;
            return;
        L18:
            DefaultDrmSession.access$400(this.this$0).onLoadTaskConcluded(r0.taskId);
            monitor-enter(this);
        L25:
            th = move-exception;
            throw th;
        L21:
            if (this.isReleased == true) goto L23;
            DefaultDrmSession.access$500(this.this$0).obtainMessage(r6.what, Pair.create(r0.request, r1)).sendToTarget();     // Catch: Throwable -> L25
        L23:
            monitor-exit(this);     // Catch: Throwable -> L25
            return;
        L10:
            e = move-exception;
            Log.m49w(DefaultDrmSession.TAG, "Key/provisioning request produced an unexpected exception. Not retrying.", e);
            r1 = e;
            goto L18
        L4:
            switch(r6.what) {
                case 1: goto L7;
                case 2: goto L6;
                default: goto L9;
            };     // Catch: Exception -> L10 MediaDrmCallbackException -> L12
        L6:
            r1 = DefaultDrmSession.access$300(this.this$0).executeKeyRequest(DefaultDrmSession.access$200(this.this$0), (ExoMediaDrm.KeyRequest) r0.request);     // Catch: Exception -> L10 MediaDrmCallbackException -> L12
            goto L18
        L7:
            r1 = DefaultDrmSession.access$300(this.this$0).executeProvisionRequest(DefaultDrmSession.access$200(this.this$0), (ExoMediaDrm.ProvisionRequest) r0.request);     // Catch: Exception -> L10 MediaDrmCallbackException -> L12
            goto L18
        L9:
            throw new RuntimeException();     // Catch: Exception -> L10 MediaDrmCallbackException -> L12
        }

        void post(int r8, Object r9, boolean r10) {
            obtainMessage(r8, new RequestTask(LoadEventInfo.getNewId(), r10, SystemClock.elapsedRealtime(), r9)).sendToTarget();
        }

        public synchronized void release() {
            monitor-enter(this);
            removeCallbacksAndMessages(null);     // Catch: Throwable -> L7
            this.isReleased = true;     // Catch: Throwable -> L7
            monitor-exit(this);
            return;
        L7:
            th = move-exception;
            throw th;
        }
    }

    private static final class RequestTask {
        public final boolean allowRetry;
        public int errorCount;
        public final Object request;
        public final long startTimeMs;
        public final long taskId;

        public RequestTask(long r1, boolean r3, long r4, Object r6) {
            this.taskId = r1;
            this.allowRetry = r3;
            this.startTimeMs = r4;
            this.request = r6;
        }
    }

    private class ResponseHandler extends Handler {
        final /* synthetic */ DefaultDrmSession this$0;

        public ResponseHandler(DefaultDrmSession r1, Looper r2) {
            this.this$0 = r1;
            super(r2);
        }

        @Override // android.os.Handler
        public void handleMessage(Message r5) {
            Pair r0 = (Pair) r5.obj;
            Object r1 = r0.first;
            Object r2 = r0.second;
            switch(r5.what) {
                case 1: goto L6;
                case 2: goto L5;
                default: goto L9;
            };
        L5:
            DefaultDrmSession.access$100(this.this$0, r1, r2);
            return;
        L6:
            DefaultDrmSession.access$000(this.this$0, r1, r2);
            return;
        }
    }

    public static final class UnexpectedDrmSessionException extends IOException {
        public UnexpectedDrmSessionException(Throwable r1) {
            super(r1);
        }
    }

    public DefaultDrmSession(UUID r2, ExoMediaDrm r3, ProvisioningManager r4, ReferenceCountListener r5, List<DrmInitData.SchemeData> r6, int r7, boolean r8, boolean r9, byte[] r10, HashMap<String, String> r11, MediaDrmCallback r12, Looper r13, LoadErrorHandlingPolicy r14, PlayerId r15) {
        if (r7 != 1) goto L5;
    L6:
        Assertions.checkNotNull(r10);
    L7:
        this.uuid = r2;
        this.provisioningManager = r4;
        this.referenceCountListener = r5;
        this.mediaDrm = r3;
        this.mode = r7;
        this.playClearSamplesWithoutKeys = r8;
        this.isPlaceholderSession = r9;
        if (r10 == null) goto L10;
        this.offlineLicenseKeySetId = r10;
        this.schemeDatas = null;
    L11:
        this.keyRequestParameters = r11;
        this.callback = r12;
        this.eventDispatchers = new CopyOnWriteMultiset();
        this.loadErrorHandlingPolicy = r14;
        this.playerId = r15;
        this.state = 2;
        this.playbackLooper = r13;
        this.responseHandler = new ResponseHandler(this, r13);
        return;
    L10:
        this.schemeDatas = Collections.unmodifiableList((List) Assertions.checkNotNull(r6));
        goto L11
    L5:
        if (r7 != 3) goto L7;
        goto L6
    }

    static /* synthetic */ void access$000(DefaultDrmSession r0, Object r1, Object r2) {
        r0.onProvisionResponse(r1, r2);
    }

    static /* synthetic */ void access$100(DefaultDrmSession r0, Object r1, Object r2) {
        r0.onKeyResponse(r1, r2);
    }

    static /* synthetic */ UUID access$200(DefaultDrmSession r1) {
        return r1.uuid;
    }

    static /* synthetic */ MediaDrmCallback access$300(DefaultDrmSession r1) {
        return r1.callback;
    }

    static /* synthetic */ LoadErrorHandlingPolicy access$400(DefaultDrmSession r1) {
        return r1.loadErrorHandlingPolicy;
    }

    static /* synthetic */ ResponseHandler access$500(DefaultDrmSession r1) {
        return r1.responseHandler;
    }

    private void dispatchEvent(Consumer<DrmSessionEventListener.EventDispatcher> r3) {
        Iterator<DrmSessionEventListener.EventDispatcher> r0 = this.eventDispatchers.elementSet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r3.accept(r0.next());
        goto L4
    }

    @RequiresNonNull({"sessionId"})
    private void doLicense(boolean r9) {
        if (this.isPlaceholderSession == false) goto L5;
        return;
    L5:
        byte[] r0 = (byte[]) Util.castNonNull(this.sessionId);
        switch(this.mode) {
            case 0: goto L15;
            case 1: goto L15;
            case 2: goto L10;
            case 3: goto L8;
            default: goto L36;
        };
    L8:
        Assertions.checkNotNull(this.offlineLicenseKeySetId);
        Assertions.checkNotNull(this.sessionId);
        postKeyRequest(this.offlineLicenseKeySetId, 3, r9);
        return;
    L36:
        return;
    L10:
        if (this.offlineLicenseKeySetId != null) goto L12;
    L13:
        postKeyRequest(r0, 2, r9);
        return;
    L12:
        if (restoreKeys() == true) goto L13;
        return;
    L15:
        if (this.offlineLicenseKeySetId != null) goto L18;
        postKeyRequest(r0, 1, r9);
        return;
    L18:
        if (this.state != 4) goto L20;
    L21:
        long r4 = getLicenseDurationRemainingSec();
        if (this.mode != 0) goto L27;
        if (r4 > 60) goto L27;
        Log.m42d(TAG, "Offline license has expired or will expire soon. Remaining seconds: " + r4);
        postKeyRequest(r0, 2, r9);
        return;
    L27:
        if (r4 > 0) goto L29;
        onError(new KeysExpiredException(), 2);
        return;
    L29:
        this.state = 4;
        dispatchEvent(new DefaultDrmSession$$ExternalSyntheticLambda4());
        return;
    L20:
        if (restoreKeys() == true) goto L21;
    }

    private long getLicenseDurationRemainingSec() {
        if (C0180C.WIDEVINE_UUID.equals(this.uuid) == true) goto L6;
        return Long.MAX_VALUE;
    L6:
        Pair r0 = (Pair) Assertions.checkNotNull(WidevineUtil.getLicenseDurationRemainingSec(this));
        return Math.min(((Long) r0.first).longValue(), ((Long) r0.second).longValue());
    }

    @EnsuresNonNullIf(expression = {"sessionId"}, result = true)
    private boolean isOpen() {
        if (this.state != 3) goto L5;
    L8:
        return true;
    L5:
        if (this.state == 4) goto L8;
        return false;
    }

    static /* synthetic */ void lambda$onError$1(Throwable r1, DrmSessionEventListener.EventDispatcher r2) {
        r2.drmSessionManagerError((Exception) r1);
    }

    static /* synthetic */ void lambda$openInternal$0(int r0, DrmSessionEventListener.EventDispatcher r1) {
        r1.drmSessionAcquired(r0);
    }

    private void onError(final Throwable r3, int r4) {
        this.lastException = new DrmSession.DrmSessionException(r3, DrmUtil.getErrorCodeForMediaDrmException(r3, r4));
        Log.m45e(TAG, "DRM session error", r3);
        if ((r3 instanceof Exception) == false) goto L6;
        dispatchEvent(new DefaultDrmSession$$ExternalSyntheticLambda3(r3));
    L15:
        if (this.state == 4) goto L20;
        this.state = 1;
        return;
    L20:
        return;
    L6:
        if ((r3 instanceof Error) == false) goto L19;
        if (DrmUtil.isFailureToConstructResourceBusyException(r3) == true) goto L15;
        if (DrmUtil.isFailureToConstructNotProvisionedException(r3) == true) goto L15;
        throw ((Error) r3);
    L19:
        throw new IllegalStateException("Unexpected Throwable subclass", r3);
    }

    private void onKeyResponse(Object r5, Object r6) {
        if (r5 == this.currentKeyRequest) goto L5;
        return;
    L5:
        if (isOpen() == false) goto L38;
        this.currentKeyRequest = null;
        if ((r6 instanceof Exception) == false) goto L10;
    L32:
        onKeysError((Throwable) r6, false);
        return;
    L10:
        if ((r6 instanceof NoSuchMethodError) == true) goto L32;
        byte[] r0 = (byte[]) r6;     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        if (this.mode != 3) goto L15;
        this.mediaDrm.provideKeyResponse((byte[]) Util.castNonNull(this.offlineLicenseKeySetId), r0);     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        dispatchEvent(new DefaultDrmSession$$ExternalSyntheticLambda1());     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        return;
    L15:
        byte[] r1 = this.mediaDrm.provideKeyResponse(this.sessionId, r0);     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        if (this.mode != 2) goto L18;
    L21:
        if (r1 != null) goto L23;
    L25:
        this.state = 4;     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        dispatchEvent(new DefaultDrmSession$$ExternalSyntheticLambda2());     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        return;
    L23:
        if (r1.length == 0) goto L25;
        this.offlineLicenseKeySetId = r1;     // Catch: NoSuchMethodError -> L27 Throwable -> L29
        goto L25
    L18:
        if (this.mode != 0) goto L25;
        if (this.offlineLicenseKeySetId == null) goto L25;
    L29:
        e = move-exception;
        onKeysError(e, true);
        return;
    }

    private void onKeysError(Throwable r2, boolean r3) {
        if ((r2 instanceof NotProvisionedException) == false) goto L5;
    L12:
        this.provisioningManager.provisionRequired(this);
        return;
    L5:
        if (DrmUtil.isFailureToConstructNotProvisionedException(r2) == true) goto L12;
        if (r3 == false) goto L10;
        int r0 = 1;
    L11:
        onError(r2, r0);
        return;
    L10:
        r0 = 2;
        goto L11
    }

    private void onKeysRequired() {
        if (this.mode == 0) goto L5;
        return;
    L5:
        if (this.state != 4) goto L9;
        Util.castNonNull(this.sessionId);
        doLicense(false);
        return;
    }

    private void onProvisionResponse(Object r4, Object r5) {
        if (r4 == this.currentProvisionRequest) goto L5;
        return;
    L5:
        if (this.state != 2) goto L7;
    L9:
        this.currentProvisionRequest = null;
        if ((r5 instanceof Exception) == false) goto L20;
        this.provisioningManager.onProvisionError((Exception) r5, false);
        return;
    L20:
        this.mediaDrm.provideProvisionResponse((byte[]) r5);     // Catch: Exception -> L16
        this.provisioningManager.onProvisionCompleted();
        return;
    L16:
        e = move-exception;
        this.provisioningManager.onProvisionError(e, true);
        return;
    L7:
        if (isOpen() == true) goto L9;
    }

    @EnsuresNonNullIf(expression = {"sessionId"}, result = true)
    private boolean openInternal() {
        if (isOpen() == false) goto L18;
        return true;
    L18:
        this.sessionId = this.mediaDrm.openSession();     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        this.mediaDrm.setPlayerIdForSession(this.sessionId, this.playerId);     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        this.cryptoConfig = this.mediaDrm.createCryptoConfig(this.sessionId);     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        this.state = 3;     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        final int r0 = this.state;     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        dispatchEvent(new DefaultDrmSession$$ExternalSyntheticLambda0(r0));     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        Assertions.checkNotNull(this.sessionId);     // Catch: NoSuchMethodError -> L7 Exception -> L9 NotProvisionedException -> L14
        return true;
    L9:
        e = e;
    L11:
        if (DrmUtil.isFailureToConstructNotProvisionedException(e) == false) goto L13;
        this.provisioningManager.provisionRequired(this);
        return false;
    L13:
        onError(e, 1);
        return false;
    L7:
        e = e;
    L15:
        this.provisioningManager.provisionRequired(this);
        return false;
    }

    private void postKeyRequest(byte[] r4, int r5, boolean r6) {
        this.currentKeyRequest = this.mediaDrm.getKeyRequest(r4, this.schemeDatas, r5, this.keyRequestParameters);     // Catch: Throwable -> L4 Exception -> L6
        ((RequestHandler) Util.castNonNull(this.requestHandler)).post(2, Assertions.checkNotNull(this.currentKeyRequest), r6);     // Catch: Throwable -> L4 Exception -> L6
        return;
    L4:
        e = move-exception;
        onKeysError(e, true);
    }

    @RequiresNonNull({"sessionId", "offlineLicenseKeySetId"})
    private boolean restoreKeys() {
        this.mediaDrm.restoreKeys(this.sessionId, this.offlineLicenseKeySetId);     // Catch: NoSuchMethodError -> L5 Throwable -> L7
        return true;
    L7:
        e = move-exception;
        onError(e, 1);
        return false;
    }

    private void verifyPlaybackThread() {
        if (Thread.currentThread() == this.playbackLooper.getThread()) goto L6;
        Log.m49w(TAG, "DefaultDrmSession accessed on the wrong thread.\nCurrent thread: " + Thread.currentThread().getName() + "\nExpected thread: " + this.playbackLooper.getThread().getName(), new IllegalStateException());
        return;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public void acquire(DrmSessionEventListener.EventDispatcher r5) {
        verifyPlaybackThread();
        boolean r1 = false;
        if (this.referenceCount >= 0) goto L5;
        Log.m44e(TAG, "Session reference count less than zero: " + this.referenceCount);
        this.referenceCount = 0;
    L5:
        if (r5 == null) goto L7;
        this.eventDispatchers.add(r5);
    L7:
        int r0 = this.referenceCount + 1;
        this.referenceCount = r0;
        if (r0 == 1) goto L10;
        if (r5 != null) goto L17;
    L21:
        this.referenceCountListener.onReferenceCountIncremented(this, this.referenceCount);
        return;
    L17:
        if (isOpen() == false) goto L21;
        if (this.eventDispatchers.count(r5) != 1) goto L21;
        r5.drmSessionAcquired(this.state);
        goto L21
    L10:
        if (this.state != 2) goto L12;
        r1 = true;
    L12:
        Assertions.checkState(r1);
        this.requestHandlerThread = new HandlerThread("ExoPlayer:DrmRequestHandler");
        this.requestHandlerThread.start();
        this.requestHandler = new RequestHandler(this, this.requestHandlerThread.getLooper());
        if (openInternal() == false) goto L21;
        doLicense(true);
        goto L21
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public final CryptoConfig getCryptoConfig() {
        verifyPlaybackThread();
        return this.cryptoConfig;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public final DrmSession.DrmSessionException getError() {
        verifyPlaybackThread();
        if (this.state == 1) goto L7;
        return null;
    L7:
        return this.lastException;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public byte[] getOfflineLicenseKeySetId() {
        verifyPlaybackThread();
        return this.offlineLicenseKeySetId;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public final UUID getSchemeUuid() {
        verifyPlaybackThread();
        return this.uuid;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public final int getState() {
        verifyPlaybackThread();
        return this.state;
    }

    public boolean hasSessionId(byte[] r2) {
        verifyPlaybackThread();
        return Arrays.equals(this.sessionId, r2);
    }

    void onMediaDrmEvent(int r1) {
        switch(r1) {
            case 2: goto L4;
            default: goto L6;
        };
    L4:
        onKeysRequired();
        return;
    }

    void onProvisionCompleted() {
        if (openInternal() == false) goto L6;
        doLicense(true);
        return;
    }

    void onProvisionError(Exception r2, boolean r3) {
        if (r3 == false) goto L4;
        int r0 = 1;
    L5:
        onError(r2, r0);
        return;
    L4:
        r0 = 3;
        goto L5
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public boolean playClearSamplesWithoutKeys() {
        verifyPlaybackThread();
        return this.playClearSamplesWithoutKeys;
    }

    void provision() {
        this.currentProvisionRequest = this.mediaDrm.getProvisionRequest();
        ((RequestHandler) Util.castNonNull(this.requestHandler)).post(1, Assertions.checkNotNull(this.currentProvisionRequest), true);
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public Map<String, String> queryKeyStatus() {
        verifyPlaybackThread();
        if (this.sessionId != null) goto L6;
        return null;
    L6:
        return this.mediaDrm.queryKeyStatus(this.sessionId);
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public void release(DrmSessionEventListener.EventDispatcher r4) {
        verifyPlaybackThread();
        if (this.referenceCount > 0) goto L6;
        Log.m44e(TAG, "release() called on a session that's already fully released.");
        return;
    L6:
        int r0 = this.referenceCount - 1;
        this.referenceCount = r0;
        if (r0 != 0) goto L11;
        this.state = 0;
        ((ResponseHandler) Util.castNonNull(this.responseHandler)).removeCallbacksAndMessages(null);
        ((RequestHandler) Util.castNonNull(this.requestHandler)).release();
        this.requestHandler = null;
        ((HandlerThread) Util.castNonNull(this.requestHandlerThread)).quit();
        this.requestHandlerThread = null;
        this.cryptoConfig = null;
        this.lastException = null;
        this.currentKeyRequest = null;
        this.currentProvisionRequest = null;
        if (this.sessionId == null) goto L11;
        this.mediaDrm.closeSession(this.sessionId);
        this.sessionId = null;
    L11:
        if (r4 == null) goto L15;
        this.eventDispatchers.remove(r4);
        if (this.eventDispatchers.count(r4) != 0) goto L15;
        r4.drmSessionReleased();
    L15:
        this.referenceCountListener.onReferenceCountDecremented(this, this.referenceCount);
    }

    @Override // androidx.media3.exoplayer.drm.DrmSession
    public boolean requiresSecureDecoder(String r3) {
        verifyPlaybackThread();
        return this.mediaDrm.requiresSecureDecoder((byte[]) Assertions.checkStateNotNull(this.sessionId), r3);
    }
}
