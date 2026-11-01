package androidx.media3.exoplayer.drm;

import android.media.ResourceBusyException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class DefaultDrmSessionManager implements DrmSessionManager {
    public static final long DEFAULT_SESSION_KEEPALIVE_MS = 300000;
    public static final int INITIAL_DRM_REQUEST_RETRY_COUNT = 3;
    public static final int MODE_DOWNLOAD = 2;
    public static final int MODE_PLAYBACK = 0;
    public static final int MODE_QUERY = 1;
    public static final int MODE_RELEASE = 3;
    public static final String PLAYREADY_CUSTOM_DATA_KEY = "PRCustomData";
    private static final String TAG = "DefaultDrmSessionMgr";
    private final MediaDrmCallback callback;
    private ExoMediaDrm exoMediaDrm;
    private final ExoMediaDrm.Provider exoMediaDrmProvider;
    private final Set<DefaultDrmSession> keepaliveSessions;
    private final HashMap<String, String> keyRequestParameters;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    volatile MediaDrmHandler mediaDrmHandler;
    private int mode;
    private final boolean multiSession;
    private DefaultDrmSession noMultiSessionDrmSession;
    private byte[] offlineLicenseKeySetId;
    private DefaultDrmSession placeholderDrmSession;
    private final boolean playClearSamplesWithoutKeys;
    private Handler playbackHandler;
    private Looper playbackLooper;
    private PlayerId playerId;
    private final Set<PreacquiredSessionReference> preacquiredSessionReferences;
    private int prepareCallsCount;
    private final ProvisioningManagerImpl provisioningManagerImpl;
    private final ReferenceCountListenerImpl referenceCountListener;
    private final long sessionKeepaliveMs;
    private final List<DefaultDrmSession> sessions;
    private final int[] useDrmSessionsForClearContentTrackTypes;
    private final UUID uuid;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.drm.DefaultDrmSessionManager$1 */
    static /* synthetic */ class C02781 {
    }

    public static final class Builder {
        private ExoMediaDrm.Provider exoMediaDrmProvider;
        private final HashMap<String, String> keyRequestParameters;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private boolean multiSession;
        private boolean playClearSamplesWithoutKeys;
        private long sessionKeepaliveMs;
        private int[] useDrmSessionsForClearContentTrackTypes;
        private UUID uuid;

        public Builder() {
            this.keyRequestParameters = new HashMap();
            this.uuid = C0180C.WIDEVINE_UUID;
            this.exoMediaDrmProvider = FrameworkMediaDrm.DEFAULT_PROVIDER;
            this.useDrmSessionsForClearContentTrackTypes = new int[0];
            this.playClearSamplesWithoutKeys = true;
            this.loadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy();
            this.sessionKeepaliveMs = 300000;
        }

        public DefaultDrmSessionManager build(MediaDrmCallback r13) {
            return new DefaultDrmSessionManager(this.uuid, this.exoMediaDrmProvider, r13, this.keyRequestParameters, this.multiSession, this.useDrmSessionsForClearContentTrackTypes, this.playClearSamplesWithoutKeys, this.loadErrorHandlingPolicy, this.sessionKeepaliveMs, null);
        }

        public Builder setKeyRequestParameters(Map<String, String> r2) {
            this.keyRequestParameters.clear();
            if (r2 == null) goto L5;
            this.keyRequestParameters.putAll(r2);
        L5:
            return this;
        }

        public Builder setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy r2) {
            this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(r2);
            return this;
        }

        public Builder setMultiSession(boolean r1) {
            this.multiSession = r1;
            return this;
        }

        public Builder setPlayClearSamplesWithoutKeys(boolean r1) {
            this.playClearSamplesWithoutKeys = r1;
            return this;
        }

        public Builder setSessionKeepaliveMs(long r3) {
            if (r3 <= 0) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkArgument(r0);
            this.sessionKeepaliveMs = r3;
            return this;
        L5:
            if (r3 == C0180C.TIME_UNSET) goto L8;
            r0 = false;
            goto L9
        }

        public Builder setUseDrmSessionsForClearContent(int... r7) {
            int r0 = r7.length;
            int r2 = 0;
        L3:
            if (r2 >= r0) goto L10;
            int r3 = r7[r2];
            boolean r5 = true;
            if (r3 == 2) goto L9;
            if (r3 == 1) goto L9;
            r5 = false;
        L9:
            Assertions.checkArgument(r5);
            r2 = r2 + 1;
            goto L3
        L10:
            this.useDrmSessionsForClearContentTrackTypes = (int[]) r7.clone();
            return this;
        }

        public Builder setUuidAndExoMediaDrmProvider(UUID r2, ExoMediaDrm.Provider r3) {
            this.uuid = (UUID) Assertions.checkNotNull(r2);
            this.exoMediaDrmProvider = (ExoMediaDrm.Provider) Assertions.checkNotNull(r3);
            return this;
        }
    }

    private class MediaDrmEventListener implements ExoMediaDrm.OnEventListener {
        final /* synthetic */ DefaultDrmSessionManager this$0;

        private MediaDrmEventListener(DefaultDrmSessionManager r1) {
            this.this$0 = r1;
        }

        /* synthetic */ MediaDrmEventListener(DefaultDrmSessionManager r1, C02781 r2) {
            this(r1);
        }

        @Override // androidx.media3.exoplayer.drm.ExoMediaDrm.OnEventListener
        public void onEvent(ExoMediaDrm r2, byte[] r3, int r4, int r5, byte[] r6) {
            ((MediaDrmHandler) Assertions.checkNotNull(this.this$0.mediaDrmHandler)).obtainMessage(r4, r3).sendToTarget();
        }
    }

    private class MediaDrmHandler extends Handler {
        final /* synthetic */ DefaultDrmSessionManager this$0;

        public MediaDrmHandler(DefaultDrmSessionManager r1, Looper r2) {
            this.this$0 = r1;
            super(r2);
        }

        @Override // android.os.Handler
        public void handleMessage(Message r5) {
            byte[] r0 = (byte[]) r5.obj;
            if (r0 != null) goto L5;
            return;
        L5:
            Iterator r1 = DefaultDrmSessionManager.access$400(this.this$0).iterator();
        L7:
            if (r1.hasNext() == false) goto L13;
            DefaultDrmSession r2 = (DefaultDrmSession) r1.next();
            if (r2.hasSessionId(r0) == false) goto L7;
            r2.onMediaDrmEvent(r5.what);
            return;
        }
    }

    public static final class MissingSchemeDataException extends Exception {
        private MissingSchemeDataException(UUID r3) {
            super("Media does not support uuid: " + r3);
        }

        /* synthetic */ MissingSchemeDataException(UUID r1, C02781 r2) {
            this(r1);
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private class PreacquiredSessionReference implements DrmSessionManager.DrmSessionReference {
        private final DrmSessionEventListener.EventDispatcher eventDispatcher;
        private boolean isReleased;
        private DrmSession session;
        final /* synthetic */ DefaultDrmSessionManager this$0;

        public PreacquiredSessionReference(DefaultDrmSessionManager r1, DrmSessionEventListener.EventDispatcher r2) {
            this.this$0 = r1;
            this.eventDispatcher = r2;
        }

        public void acquire(final Format r3) {
            ((Handler) Assertions.checkNotNull(DefaultDrmSessionManager.access$700(this.this$0))).post(new RunnableC0279x386b0ac6(this, r3));
        }

        /* JADX INFO: renamed from: lambda$acquire$0$androidx-media3-exoplayer-drm-DefaultDrmSessionManager$PreacquiredSessionReference */
        /* synthetic */ void m92x937f548e(Format r5) {
            if (DefaultDrmSessionManager.access$800(this.this$0) != 0) goto L5;
            return;
        L5:
            if (this.isReleased == true) goto L10;
            this.session = DefaultDrmSessionManager.access$1500(this.this$0, (Looper) Assertions.checkNotNull(DefaultDrmSessionManager.access$1400(this.this$0)), this.eventDispatcher, r5, false);
            DefaultDrmSessionManager.access$1300(this.this$0).add(this);
            return;
        }

        /* JADX INFO: renamed from: lambda$release$1$androidx-media3-exoplayer-drm-DefaultDrmSessionManager$PreacquiredSessionReference */
        /* synthetic */ void m93xce10c5c() {
            if (this.isReleased == false) goto L6;
            return;
        L6:
            if (this.session == null) goto L8;
            this.session.release(this.eventDispatcher);
        L8:
            DefaultDrmSessionManager.access$1300(this.this$0).remove(this);
            this.isReleased = true;
        }

        @Override // androidx.media3.exoplayer.drm.DrmSessionManager.DrmSessionReference
        public void release() {
            Util.postOrRun((Handler) Assertions.checkNotNull(DefaultDrmSessionManager.access$700(this.this$0)), new RunnableC0280x386b0ac7(this));
        }
    }

    private class ProvisioningManagerImpl implements DefaultDrmSession.ProvisioningManager {
        private DefaultDrmSession provisioningSession;
        private final Set<DefaultDrmSession> sessionsAwaitingProvisioning;
        final /* synthetic */ DefaultDrmSessionManager this$0;

        public ProvisioningManagerImpl(DefaultDrmSessionManager r1) {
            this.this$0 = r1;
            this.sessionsAwaitingProvisioning = new HashSet();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.media3.exoplayer.drm.DefaultDrmSession.ProvisioningManager
        public void onProvisionCompleted() {
            this.provisioningSession = null;
            ImmutableList r0 = ImmutableList.copyOf(this.sessionsAwaitingProvisioning);
            this.sessionsAwaitingProvisioning.clear();
            UnmodifiableIterator r1 = r0.iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            ((DefaultDrmSession) r1.next()).onProvisionCompleted();
            goto L4
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.media3.exoplayer.drm.DefaultDrmSession.ProvisioningManager
        public void onProvisionError(Exception r4, boolean r5) {
            this.provisioningSession = null;
            ImmutableList r0 = ImmutableList.copyOf(this.sessionsAwaitingProvisioning);
            this.sessionsAwaitingProvisioning.clear();
            UnmodifiableIterator r1 = r0.iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            ((DefaultDrmSession) r1.next()).onProvisionError(r4, r5);
            goto L4
        }

        public void onSessionFullyReleased(DefaultDrmSession r2) {
            this.sessionsAwaitingProvisioning.remove(r2);
            if (this.provisioningSession != r2) goto L8;
            this.provisioningSession = null;
            if (this.sessionsAwaitingProvisioning.isEmpty() == true) goto L9;
            this.provisioningSession = this.sessionsAwaitingProvisioning.iterator().next();
            this.provisioningSession.provision();
            return;
        L9:
            return;
        }

        @Override // androidx.media3.exoplayer.drm.DefaultDrmSession.ProvisioningManager
        public void provisionRequired(DefaultDrmSession r2) {
            this.sessionsAwaitingProvisioning.add(r2);
            if (this.provisioningSession == null) goto L5;
            return;
        L5:
            this.provisioningSession = r2;
            r2.provision();
        }
    }

    private class ReferenceCountListenerImpl implements DefaultDrmSession.ReferenceCountListener {
        final /* synthetic */ DefaultDrmSessionManager this$0;

        private ReferenceCountListenerImpl(DefaultDrmSessionManager r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ReferenceCountListenerImpl(DefaultDrmSessionManager r1, C02781 r2) {
            this(r1);
        }

        static /* synthetic */ void lambda$onReferenceCountDecremented$0(DefaultDrmSession r1) {
            r1.release(null);
        }

        @Override // androidx.media3.exoplayer.drm.DefaultDrmSession.ReferenceCountListener
        public void onReferenceCountDecremented(final DefaultDrmSession r7, int r8) {
            if (r8 == 1) goto L5;
        L9:
            if (r8 != 0) goto L19;
            DefaultDrmSessionManager.access$400(this.this$0).remove(r7);
            if (DefaultDrmSessionManager.access$900(this.this$0) != r7) goto L14;
            DefaultDrmSessionManager.access$902(this.this$0, null);
        L14:
            if (DefaultDrmSessionManager.access$1000(this.this$0) != r7) goto L16;
            DefaultDrmSessionManager.access$1002(this.this$0, null);
        L16:
            DefaultDrmSessionManager.access$1100(this.this$0).onSessionFullyReleased(r7);
            if (DefaultDrmSessionManager.access$500(this.this$0) == C0180C.TIME_UNSET) goto L19;
            ((Handler) Assertions.checkNotNull(DefaultDrmSessionManager.access$700(this.this$0))).removeCallbacksAndMessages(r7);
            DefaultDrmSessionManager.access$600(this.this$0).remove(r7);
        L19:
            DefaultDrmSessionManager.access$1200(this.this$0);
            return;
        L5:
            if (DefaultDrmSessionManager.access$800(this.this$0) <= 0) goto L9;
            if (DefaultDrmSessionManager.access$500(this.this$0) == C0180C.TIME_UNSET) goto L9;
            DefaultDrmSessionManager.access$600(this.this$0).add(r7);
            ((Handler) Assertions.checkNotNull(DefaultDrmSessionManager.access$700(this.this$0))).postAtTime(new RunnableC0281xb150a836(r7), r7, SystemClock.uptimeMillis() + DefaultDrmSessionManager.access$500(this.this$0));
            goto L19
        }

        @Override // androidx.media3.exoplayer.drm.DefaultDrmSession.ReferenceCountListener
        public void onReferenceCountIncremented(DefaultDrmSession r5, int r6) {
            if (DefaultDrmSessionManager.access$500(this.this$0) == C0180C.TIME_UNSET) goto L6;
            DefaultDrmSessionManager.access$600(this.this$0).remove(r5);
            ((Handler) Assertions.checkNotNull(DefaultDrmSessionManager.access$700(this.this$0))).removeCallbacksAndMessages(r5);
            return;
        }
    }

    private DefaultDrmSessionManager(UUID r3, ExoMediaDrm.Provider r4, MediaDrmCallback r5, HashMap<String, String> r6, boolean r7, int[] r8, boolean r9, LoadErrorHandlingPolicy r10, long r11) {
        Assertions.checkNotNull(r3);
        Assertions.checkArgument(!C0180C.COMMON_PSSH_UUID.equals(r3), "Use C.CLEARKEY_UUID instead");
        this.uuid = r3;
        this.exoMediaDrmProvider = r4;
        this.callback = r5;
        this.keyRequestParameters = r6;
        this.multiSession = r7;
        this.useDrmSessionsForClearContentTrackTypes = r8;
        this.playClearSamplesWithoutKeys = r9;
        this.loadErrorHandlingPolicy = r10;
        this.provisioningManagerImpl = new ProvisioningManagerImpl(this);
        this.referenceCountListener = new ReferenceCountListenerImpl(this, null);
        this.mode = 0;
        this.sessions = new ArrayList();
        this.preacquiredSessionReferences = Sets.newIdentityHashSet();
        this.keepaliveSessions = Sets.newIdentityHashSet();
        this.sessionKeepaliveMs = r11;
    }

    /* synthetic */ DefaultDrmSessionManager(UUID r1, ExoMediaDrm.Provider r2, MediaDrmCallback r3, HashMap r4, boolean r5, int[] r6, boolean r7, LoadErrorHandlingPolicy r8, long r9, C02781 r11) {
        this(r1, r2, r3, r4, r5, r6, r7, r8, r9);
    }

    static /* synthetic */ DefaultDrmSession access$1000(DefaultDrmSessionManager r1) {
        return r1.noMultiSessionDrmSession;
    }

    static /* synthetic */ DefaultDrmSession access$1002(DefaultDrmSessionManager r0, DefaultDrmSession r1) {
        r0.noMultiSessionDrmSession = r1;
        return r1;
    }

    static /* synthetic */ ProvisioningManagerImpl access$1100(DefaultDrmSessionManager r1) {
        return r1.provisioningManagerImpl;
    }

    static /* synthetic */ void access$1200(DefaultDrmSessionManager r0) {
        r0.maybeReleaseMediaDrm();
    }

    static /* synthetic */ Set access$1300(DefaultDrmSessionManager r1) {
        return r1.preacquiredSessionReferences;
    }

    static /* synthetic */ Looper access$1400(DefaultDrmSessionManager r1) {
        return r1.playbackLooper;
    }

    static /* synthetic */ DrmSession access$1500(DefaultDrmSessionManager r1, Looper r2, DrmSessionEventListener.EventDispatcher r3, Format r4, boolean r5) {
        return r1.acquireSession(r2, r3, r4, r5);
    }

    static /* synthetic */ List access$400(DefaultDrmSessionManager r1) {
        return r1.sessions;
    }

    static /* synthetic */ long access$500(DefaultDrmSessionManager r2) {
        return r2.sessionKeepaliveMs;
    }

    static /* synthetic */ Set access$600(DefaultDrmSessionManager r1) {
        return r1.keepaliveSessions;
    }

    static /* synthetic */ Handler access$700(DefaultDrmSessionManager r1) {
        return r1.playbackHandler;
    }

    static /* synthetic */ int access$800(DefaultDrmSessionManager r1) {
        return r1.prepareCallsCount;
    }

    static /* synthetic */ DefaultDrmSession access$900(DefaultDrmSessionManager r1) {
        return r1.placeholderDrmSession;
    }

    static /* synthetic */ DefaultDrmSession access$902(DefaultDrmSessionManager r0, DefaultDrmSession r1) {
        r0.placeholderDrmSession = r1;
        return r1;
    }

    private DrmSession acquireSession(Looper r7, DrmSessionEventListener.EventDispatcher r8, Format r9, boolean r10) {
        maybeCreateMediaDrmHandler(r7);
        if (r9.drmInitData == null) goto L5;
        List<DrmInitData.SchemeData> r0 = null;
        if (this.offlineLicenseKeySetId != null) goto L16;
        r0 = getSchemeDatas((DrmInitData) Assertions.checkNotNull(r9.drmInitData), this.uuid, false);
        if (r0.isEmpty() == false) goto L16;
        MissingSchemeDataException r1 = new MissingSchemeDataException(this.uuid, null);
        Log.m45e(TAG, "DRM error", r1);
        if (r8 == null) goto L14;
        r8.drmSessionManagerError(r1);
    L14:
        return new ErrorStateDrmSession(new DrmSession.DrmSessionException(r1, PlaybackException.ERROR_CODE_DRM_CONTENT_ERROR));
    L16:
        if (this.multiSession == true) goto L18;
        DefaultDrmSession r2 = this.noMultiSessionDrmSession;
    L25:
        if (r2 != null) goto L30;
        r2 = createAndAcquireSessionWithRetry(r0, false, r8, r10);
        if (this.multiSession == true) goto L29;
        this.noMultiSessionDrmSession = r2;
    L29:
        this.sessions.add(r2);
    L31:
        return r2;
    L30:
        r2.acquire(r8);
        goto L31
    L18:
        r2 = null;
        Iterator<DefaultDrmSession> r3 = this.sessions.iterator();
    L20:
        if (r3.hasNext() == false) goto L25;
        DefaultDrmSession r4 = r3.next();
        if (Util.areEqual(r4.schemeDatas, r0) == false) goto L20;
        r2 = r4;
        goto L25
    L5:
        return maybeAcquirePlaceholderSession(MimeTypes.getTrackType(r9.sampleMimeType), r10);
    }

    private static boolean acquisitionFailedIndicatingResourceShortage(DrmSession r4) {
        if (r4.getState() == 1) goto L5;
        return false;
    L5:
        Throwable r0 = ((DrmSession.DrmSessionException) Assertions.checkNotNull(r4.getError())).getCause();
        if ((r0 instanceof ResourceBusyException) == true) goto L10;
        if (DrmUtil.isFailureToConstructResourceBusyException(r0) == true) goto L10;
        return false;
    L10:
        return true;
    }

    private boolean canAcquireSession(DrmInitData r7) {
        if (this.offlineLicenseKeySetId == null) goto L6;
        return true;
    L6:
        if (getSchemeDatas(r7, this.uuid, true).isEmpty() == true) goto L8;
    L13:
        String r2 = r7.schemeType;
        if (r2 != null) goto L16;
    L32:
        return true;
    L16:
        if (C0180C.CENC_TYPE_cenc.equals(r2) == true) goto L32;
        if (C0180C.CENC_TYPE_cbcs.equals(r2) == false) goto L26;
        if (Util.SDK_INT < 25) goto L24;
        return true;
    L24:
        return false;
    L26:
        if (C0180C.CENC_TYPE_cbc1.equals(r2) == false) goto L28;
    L31:
        return false;
    L28:
        if (C0180C.CENC_TYPE_cens.equals(r2) == true) goto L31;
        return true;
    L8:
        if (r7.schemeDataCount == 1) goto L10;
    L12:
        return false;
    L10:
        if (r7.get(0).matches(C0180C.COMMON_PSSH_UUID) == false) goto L12;
        Log.m48w(TAG, "DrmInitData only contains common PSSH SchemeData. Assuming support for: " + this.uuid);
        goto L13
    }

    private DefaultDrmSession createAndAcquireSession(List<DrmInitData.SchemeData> r18, boolean r19, DrmSessionEventListener.EventDispatcher r20) {
        Assertions.checkNotNull(this.exoMediaDrm);
        boolean r9 = this.playClearSamplesWithoutKeys | r19;
        DefaultDrmSession r2 = new DefaultDrmSession(this.uuid, this.exoMediaDrm, this.provisioningManagerImpl, this.referenceCountListener, r18, this.mode, r9, r19, this.offlineLicenseKeySetId, this.keyRequestParameters, this.callback, (Looper) Assertions.checkNotNull(this.playbackLooper), this.loadErrorHandlingPolicy, (PlayerId) Assertions.checkNotNull(this.playerId));
        r2.acquire(r20);
        if (this.sessionKeepaliveMs == C0180C.TIME_UNSET) goto L5;
        r2.acquire(null);
    L5:
        return r2;
    }

    private DefaultDrmSession createAndAcquireSessionWithRetry(List<DrmInitData.SchemeData> r3, boolean r4, DrmSessionEventListener.EventDispatcher r5, boolean r6) {
        DefaultDrmSession r0 = createAndAcquireSession(r3, r4, r5);
        if (acquisitionFailedIndicatingResourceShortage(r0) == false) goto L8;
        if (this.keepaliveSessions.isEmpty() == true) goto L8;
        releaseAllKeepaliveSessions();
        undoAcquisition(r0, r5);
        r0 = createAndAcquireSession(r3, r4, r5);
    L8:
        if (acquisitionFailedIndicatingResourceShortage(r0) == false) goto L17;
        if (r6 == true) goto L11;
        return r0;
    L11:
        if (this.preacquiredSessionReferences.isEmpty() == true) goto L19;
        releaseAllPreacquiredSessions();
        if (this.keepaliveSessions.isEmpty() == true) goto L15;
        releaseAllKeepaliveSessions();
    L15:
        undoAcquisition(r0, r5);
        return createAndAcquireSession(r3, r4, r5);
    L19:
        return r0;
    L17:
        return r0;
    }

    private static List<DrmInitData.SchemeData> getSchemeDatas(DrmInitData r5, UUID r6, boolean r7) {
        ArrayList r0 = new ArrayList(r5.schemeDataCount);
        int r1 = 0;
    L4:
        if (r1 >= r5.schemeDataCount) goto L20;
        DrmInitData.SchemeData r2 = r5.get(r1);
        if (r2.matches(r6) == false) goto L8;
    L13:
        boolean r3 = true;
    L14:
        if (r3 == false) goto L19;
        if (r2.data != null) goto L18;
        if (r7 == false) goto L19;
    L18:
        r0.add(r2);
    L19:
        r1 = r1 + 1;
        goto L4
    L8:
        if (C0180C.CLEARKEY_UUID.equals(r6) == true) goto L10;
    L12:
        r3 = false;
        goto L14
    L10:
        if (r2.matches(C0180C.COMMON_PSSH_UUID) == false) goto L12;
    L20:
        return r0;
    }

    @EnsuresNonNull({"this.playbackLooper", "this.playbackHandler"})
    private synchronized void initPlaybackLooper(Looper r2) {
        monitor-enter(this);
    L13:
        th = move-exception;
        throw th;
    L4:
        if (this.playbackLooper != null) goto L7;
        this.playbackLooper = r2;     // Catch: Throwable -> L13
        this.playbackHandler = new Handler(r2);     // Catch: Throwable -> L13
    L11:
        monitor-exit(this);
        return;
    L7:
        if (this.playbackLooper != r2) goto L9;
        boolean r0 = true;
    L10:
        Assertions.checkState(r0);     // Catch: Throwable -> L13
        Assertions.checkNotNull(this.playbackHandler);     // Catch: Throwable -> L13
        goto L11
    L9:
        r0 = false;
        goto L10
    }

    private DrmSession maybeAcquirePlaceholderSession(int r7, boolean r8) {
        ExoMediaDrm r0 = (ExoMediaDrm) Assertions.checkNotNull(this.exoMediaDrm);
        if (r0.getCryptoType() == 2) goto L5;
    L7:
        boolean r1 = false;
    L9:
        if (r1 == false) goto L11;
    L21:
        return null;
    L11:
        if (Util.linearSearch(this.useDrmSessionsForClearContentTrackTypes, r7) == (-1)) goto L21;
        if (r0.getCryptoType() == 1) goto L21;
        if (this.placeholderDrmSession != null) goto L18;
        DefaultDrmSession r2 = createAndAcquireSessionWithRetry(ImmutableList.m212of(), true, null, r8);
        this.sessions.add(r2);
        this.placeholderDrmSession = r2;
    L20:
        return this.placeholderDrmSession;
    L18:
        this.placeholderDrmSession.acquire(null);
        goto L20
    L5:
        if (FrameworkCryptoConfig.WORKAROUND_DEVICE_NEEDS_KEYS_TO_CONFIGURE_CODEC == false) goto L7;
        r1 = true;
        goto L9
    }

    private void maybeCreateMediaDrmHandler(Looper r2) {
        if (this.mediaDrmHandler != null) goto L6;
        this.mediaDrmHandler = new MediaDrmHandler(this, r2);
        return;
    }

    private void maybeReleaseMediaDrm() {
        if (this.exoMediaDrm != null) goto L5;
        return;
    L5:
        if (this.prepareCallsCount == 0) goto L7;
        return;
    L7:
        if (this.sessions.isEmpty() == true) goto L9;
        return;
    L9:
        if (this.preacquiredSessionReferences.isEmpty() == false) goto L15;
        ((ExoMediaDrm) Assertions.checkNotNull(this.exoMediaDrm)).release();
        this.exoMediaDrm = null;
        return;
    }

    private void releaseAllKeepaliveSessions() {
        UnmodifiableIterator r1 = ImmutableSet.copyOf(this.keepaliveSessions).iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        ((DrmSession) r1.next()).release(null);
        goto L4
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void releaseAllPreacquiredSessions() {
        UnmodifiableIterator r1 = ImmutableSet.copyOf(this.preacquiredSessionReferences).iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        ((PreacquiredSessionReference) r1.next()).release();
        goto L4
    }

    private void undoAcquisition(DrmSession r5, DrmSessionEventListener.EventDispatcher r6) {
        r5.release(r6);
        if (this.sessionKeepaliveMs == C0180C.TIME_UNSET) goto L6;
        r5.release(null);
        return;
    }

    private void verifyPlaybackThread(boolean r4) {
        if (r4 == false) goto L8;
        if (this.playbackLooper != null) goto L8;
        Log.m49w(TAG, "DefaultDrmSessionManager accessed before setPlayer(), possibly on the wrong thread.", new IllegalStateException());
        return;
    L8:
        if (Thread.currentThread() == ((Looper) Assertions.checkNotNull(this.playbackLooper)).getThread()) goto L12;
        Log.m49w(TAG, "DefaultDrmSessionManager accessed on the wrong thread.\nCurrent thread: " + Thread.currentThread().getName() + "\nExpected thread: " + this.playbackLooper.getThread().getName(), new IllegalStateException());
        return;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManager
    public DrmSession acquireSession(DrmSessionEventListener.EventDispatcher r4, Format r5) {
        boolean r0 = false;
        verifyPlaybackThread(false);
        if (this.prepareCallsCount <= 0) goto L5;
        r0 = true;
    L5:
        Assertions.checkState(r0);
        Assertions.checkStateNotNull(this.playbackLooper);
        return acquireSession(this.playbackLooper, r4, r5, true);
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManager
    public int getCryptoType(Format r6) {
        verifyPlaybackThread(false);
        int r1 = ((ExoMediaDrm) Assertions.checkNotNull(this.exoMediaDrm)).getCryptoType();
        if (r6.drmInitData != null) goto L10;
        int r2 = MimeTypes.getTrackType(r6.sampleMimeType);
        if (Util.linearSearch(this.useDrmSessionsForClearContentTrackTypes, r2) != (-1)) goto L8;
        return 0;
    L8:
        return r1;
    L10:
        if (canAcquireSession(r6.drmInitData) == true) goto L15;
        return 1;
    L15:
        return r1;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManager
    public DrmSessionManager.DrmSessionReference preacquireSession(DrmSessionEventListener.EventDispatcher r2, Format r3) {
        if (this.prepareCallsCount <= 0) goto L5;
        boolean r0 = true;
    L6:
        Assertions.checkState(r0);
        Assertions.checkStateNotNull(this.playbackLooper);
        PreacquiredSessionReference r1 = new PreacquiredSessionReference(this, r2);
        r1.acquire(r3);
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManager
    public final void prepare() {
        verifyPlaybackThread(true);
        int r0 = this.prepareCallsCount;
        this.prepareCallsCount = r0 + 1;
        if (r0 == 0) goto L5;
        return;
    L5:
        C02781 r1 = null;
        if (this.exoMediaDrm != null) goto L9;
        this.exoMediaDrm = this.exoMediaDrmProvider.acquireExoMediaDrm(this.uuid);
        this.exoMediaDrm.setOnEventListener(new MediaDrmEventListener(this, r1));
        return;
    L9:
        if (this.sessionKeepaliveMs == C0180C.TIME_UNSET) goto L16;
        int r2 = 0;
    L12:
        if (r2 >= this.sessions.size()) goto L17;
        this.sessions.get(r2).acquire(null);
        r2 = r2 + 1;
        goto L12
    L17:
        return;
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManager
    public final void release() {
        verifyPlaybackThread(true);
        int r1 = this.prepareCallsCount - 1;
        this.prepareCallsCount = r1;
        if (r1 == 0) goto L6;
        return;
    L6:
        if (this.sessionKeepaliveMs == C0180C.TIME_UNSET) goto L11;
        ArrayList r0 = new ArrayList(this.sessions);
        int r2 = 0;
    L9:
        if (r2 >= r0.size()) goto L11;
        ((DefaultDrmSession) r0.get(r2)).release(null);
        r2 = r2 + 1;
    L11:
        releaseAllPreacquiredSessions();
        maybeReleaseMediaDrm();
    }

    public void setMode(int r2, byte[] r3) {
        Assertions.checkState(this.sessions.isEmpty());
        if (r2 != 1) goto L5;
    L6:
        Assertions.checkNotNull(r3);
    L7:
        this.mode = r2;
        this.offlineLicenseKeySetId = r3;
        return;
    L5:
        if (r2 != 3) goto L7;
        goto L6
    }

    @Override // androidx.media3.exoplayer.drm.DrmSessionManager
    public void setPlayer(Looper r1, PlayerId r2) {
        initPlaybackLooper(r1);
        this.playerId = r2;
    }
}
