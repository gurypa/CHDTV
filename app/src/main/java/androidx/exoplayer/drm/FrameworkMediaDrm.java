package androidx.media3.exoplayer.drm;

import android.media.DeniedByServerException;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaDrm;
import android.media.MediaDrmException;
import android.media.NotProvisionedException;
import android.media.UnsupportedSchemeException;
import android.media.metrics.LogSessionId;
import android.os.PersistableBundle;
import android.text.TextUtils;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.extractor.mp4.PsshAtomUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class FrameworkMediaDrm implements ExoMediaDrm {
    private static final String CENC_SCHEME_MIME_TYPE = "cenc";
    public static final ExoMediaDrm.Provider DEFAULT_PROVIDER = null;
    private static final String MOCK_LA_URL = "<LA_URL>https://x</LA_URL>";
    private static final String MOCK_LA_URL_VALUE = "https://x";
    private static final String TAG = "FrameworkMediaDrm";
    private static final int UTF_16_BYTES_PER_CHARACTER = 2;
    private final MediaDrm mediaDrm;
    private int referenceCount;
    private final UUID uuid;

    private static class Api31 {
        private Api31() {
        }

        public static boolean requiresSecureDecoder(MediaDrm r1, String r2, int r3) {
            return r1.requiresSecureDecoder(r2, r3);
        }

        public static void setLogSessionIdOnMediaDrmSession(MediaDrm r2, byte[] r3, PlayerId r4) {
            LogSessionId r0 = r4.getLogSessionId();
            if (r0.equals(LogSessionId.LOG_SESSION_ID_NONE) == true) goto L6;
            ((MediaDrm.PlaybackComponent) Assertions.checkNotNull(r2.getPlaybackComponent(r3))).setLogSessionId(r0);
            return;
        }
    }

    static {
        DEFAULT_PROVIDER = new FrameworkMediaDrm$$ExternalSyntheticLambda0();
    }

    private FrameworkMediaDrm(UUID r4) throws UnsupportedSchemeException {
        Assertions.checkNotNull(r4);
        Assertions.checkArgument(!C0180C.COMMON_PSSH_UUID.equals(r4), "Use C.CLEARKEY_UUID instead");
        this.uuid = r4;
        this.mediaDrm = new MediaDrm(adjustUuid(r4));
        this.referenceCount = 1;
        if (C0180C.WIDEVINE_UUID.equals(r4) == true) goto L5;
        return;
    L5:
        if (needsForceWidevineL3Workaround() == false) goto L9;
        forceWidevineL3(this.mediaDrm);
        return;
    }

    private static byte[] addLaUrlAttributeIfMissing(byte[] r11) {
        ParsableByteArray r0 = new ParsableByteArray(r11);
        int r1 = r0.readLittleEndianInt();
        short r2 = r0.readLittleEndianShort();
        short r3 = r0.readLittleEndianShort();
        if (r2 != 1) goto L14;
        if (r3 != 1) goto L14;
        String r6 = r0.readString(r0.readLittleEndianShort(), StandardCharsets.UTF_16LE);
        if (r6.contains("<LA_URL>") == false) goto L9;
        return r11;
    L9:
        int r7 = r6.indexOf("</DATA>");
        if (r7 != (-1)) goto L12;
        Log.m48w(TAG, "Could not find the </DATA> tag. Skipping LA_URL workaround.");
    L12:
        String r4 = r6.substring(0, r7) + MOCK_LA_URL + r6.substring(r7);
        int r8 = MOCK_LA_URL.length() * 2;
        ByteBuffer r9 = ByteBuffer.allocate(r1 + r8);
        r9.order(ByteOrder.LITTLE_ENDIAN);
        r9.putInt(r1 + r8);
        r9.putShort(r2);
        r9.putShort(r3);
        r9.putShort((short) (r4.length() * 2));
        r9.put(r4.getBytes(StandardCharsets.UTF_16LE));
        return r9.array();
    L14:
        Log.m46i(TAG, "Unexpected record count or type. Skipping LA_URL workaround.");
        return r11;
    }

    private String adjustLicenseServerUrl(String r4) {
        if (MOCK_LA_URL.equals(r4) == false) goto L6;
        return "";
    L6:
        if (Util.SDK_INT >= 33) goto L8;
    L14:
        return r4;
    L8:
        if ("https://default.url".equals(r4) == false) goto L14;
        String r0 = getPropertyString("version");
        if (Objects.equals(r0, "1.2") == false) goto L12;
    L13:
        return "";
    L12:
        if (Objects.equals(r0, "aidl-1") == false) goto L14;
        goto L13
    }

    private static byte[] adjustRequestData(UUID r1, byte[] r2) {
        if (C0180C.CLEARKEY_UUID.equals(r1) == true) goto L5;
        return r2;
    L5:
        return ClearKeyUtil.adjustRequestData(r2);
    }

    private static byte[] adjustRequestInitData(UUID r3, byte[] r4) {
        if (C0180C.PLAYREADY_UUID.equals(r3) == false) goto L9;
        byte[] r0 = PsshAtomUtil.parseSchemeSpecificData(r4, r3);
        if (r0 != null) goto L7;
        r0 = r4;
    L7:
        r4 = PsshAtomUtil.buildPsshAtom(C0180C.PLAYREADY_UUID, addLaUrlAttributeIfMissing(r0));
    L9:
        if (Util.SDK_INT >= 23) goto L13;
        if (C0180C.WIDEVINE_UUID.equals(r3) == false) goto L13;
    L24:
        byte[] r1 = PsshAtomUtil.parseSchemeSpecificData(r4, r3);
        if (r1 == null) goto L27;
        return r1;
    L27:
        return r4;
    L13:
        if (C0180C.PLAYREADY_UUID.equals(r3) == false) goto L27;
        if ("Amazon".equals(Util.MANUFACTURER) == false) goto L27;
        if ("AFTB".equals(Util.MODEL) == true) goto L24;
        if ("AFTS".equals(Util.MODEL) == true) goto L24;
        if ("AFTM".equals(Util.MODEL) == true) goto L24;
        if ("AFTT".equals(Util.MODEL) == false) goto L27;
        goto L24
    }

    private static String adjustRequestMimeType(UUID r2, String r3) {
        if (Util.SDK_INT < 26) goto L5;
    L12:
        return r3;
    L5:
        if (C0180C.CLEARKEY_UUID.equals(r2) == false) goto L12;
        if (MimeTypes.VIDEO_MP4.equals(r3) == false) goto L9;
        return "cenc";
    L9:
        if (MimeTypes.AUDIO_MP4.equals(r3) == false) goto L12;
        return "cenc";
    }

    private static UUID adjustUuid(UUID r2) {
        if (Util.SDK_INT >= 27) goto L8;
        if (C0180C.CLEARKEY_UUID.equals(r2) == false) goto L8;
        return C0180C.COMMON_PSSH_UUID;
    L8:
        return r2;
    }

    private static void forceWidevineL3(MediaDrm r2) {
        r2.setPropertyString("securityLevel", "L3");
    }

    private static DrmInitData.SchemeData getSchemeData(UUID r10, List<DrmInitData.SchemeData> r11) {
        if (C0180C.WIDEVINE_UUID.equals(r10) == true) goto L7;
        return r11.get(0);
    L7:
        if (Util.SDK_INT >= 28) goto L9;
    L28:
        int r0 = 0;
    L30:
        if (r0 >= r11.size()) goto L41;
        DrmInitData.SchemeData r2 = r11.get(r0);
        int r4 = PsshAtomUtil.parseVersion((byte[]) Assertions.checkNotNull(r2.data));
        if (Util.SDK_INT >= 23) goto L36;
        if (r4 != 0) goto L36;
        return r2;
    L36:
        if (Util.SDK_INT < 23) goto L39;
        if (r4 != 1) goto L39;
        return r2;
    L39:
        r0 = r0 + 1;
        goto L30
    L41:
        return r11.get(0);
    L9:
        if (r11.size() <= 1) goto L28;
        DrmInitData.SchemeData r1 = r11.get(0);
        int r3 = 0;
        boolean r5 = true;
        int r6 = 0;
    L12:
        if (r6 >= r11.size()) goto L21;
        DrmInitData.SchemeData r7 = r11.get(r6);
        byte[] r8 = (byte[]) Assertions.checkNotNull(r7.data);
        if (Util.areEqual(r7.mimeType, r1.mimeType) == false) goto L20;
        if (Util.areEqual(r7.licenseServerUrl, r1.licenseServerUrl) == false) goto L20;
        if (PsshAtomUtil.isPsshAtom(r8) == false) goto L20;
        r3 = r3 + r8.length;
        r6 = r6 + 1;
    L20:
        r5 = false;
    L21:
        if (r5 == false) goto L28;
        byte[] r9 = new byte[r3];
        int r12 = 0;
        int r13 = 0;
    L24:
        if (r13 >= r11.size()) goto L27;
        byte[] r14 = (byte[]) Assertions.checkNotNull(r11.get(r13).data);
        int r15 = r14.length;
        System.arraycopy(r14, 0, r9, r12, r15);
        r12 = r12 + r15;
        r13 = r13 + 1;
        goto L24
    L27:
        return r1.copyWithData(r9);
    }

    public static boolean isCryptoSchemeSupported(UUID r1) {
        return MediaDrm.isCryptoSchemeSupported(adjustUuid(r1));
    }

    private boolean isMediaDrmRequiresSecureDecoderImplemented() {
        if (this.uuid.equals(C0180C.WIDEVINE_UUID) == false) goto L16;
        String r0 = getPropertyString("version");
        if (r0.startsWith("v5.") == false) goto L7;
    L13:
        return false;
    L7:
        if (r0.startsWith("14.") == true) goto L13;
        if (r0.startsWith("15.") == true) goto L13;
        if (r0.startsWith("16.0") == true) goto L13;
        return true;
    L16:
        return this.uuid.equals(C0180C.CLEARKEY_UUID);
    }

    static /* synthetic */ ExoMediaDrm lambda$static$0(UUID r3) {
        return newInstance(r3);
    L5:
        Log.m44e(TAG, "Failed to instantiate a FrameworkMediaDrm for uuid: " + r3 + ".");
        return new DummyExoMediaDrm();
    }

    private static boolean needsForceWidevineL3Workaround() {
        return "ASUS_Z00AD".equals(Util.MODEL);
    }

    public static FrameworkMediaDrm newInstance(UUID r3) throws UnsupportedDrmException {
        return new FrameworkMediaDrm(r3);
    L7:
        e = move-exception;
        throw new UnsupportedDrmException(1, e);
    L4:
        e = move-exception;
        throw new UnsupportedDrmException(2, e);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public synchronized void acquire() {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.referenceCount <= 0) goto L6;
        boolean r0 = true;
    L7:
        Assertions.checkState(r0);     // Catch: Throwable -> L10
        this.referenceCount++;     // Catch: Throwable -> L10
        monitor-exit(this);
        return;
    L6:
        r0 = false;
        goto L7
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void closeSession(byte[] r2) {
        this.mediaDrm.closeSession(r2);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public /* bridge */ /* synthetic */ CryptoConfig createCryptoConfig(byte[] r1) throws MediaCryptoException {
        return createCryptoConfig(r1);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public FrameworkCryptoConfig createCryptoConfig(byte[] r3) throws MediaCryptoException {
        return new FrameworkCryptoConfig(adjustUuid(this.uuid), r3);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public int getCryptoType() {
        return 2;
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public ExoMediaDrm.KeyRequest getKeyRequest(byte[] r9, List<DrmInitData.SchemeData> r10, int r11, HashMap<String, String> r12) throws NotProvisionedException {
        DrmInitData.SchemeData r0 = null;
        if (r10 == null) goto L5;
        r0 = getSchemeData(this.uuid, r10);
        byte[] r1 = adjustRequestInitData(this.uuid, (byte[]) Assertions.checkNotNull(r0.data));
        byte[] r3 = r1;
        String r4 = adjustRequestMimeType(this.uuid, r0.mimeType);
    L6:
        MediaDrm.KeyRequest r13 = this.mediaDrm.getKeyRequest(r9, r3, r4, r11, r12);
        byte[] r14 = adjustRequestData(this.uuid, r13.getData());
        String r15 = adjustLicenseServerUrl(r13.getDefaultUrl());
        if (TextUtils.isEmpty(r15) == false) goto L13;
        if (r0 == null) goto L13;
        if (TextUtils.isEmpty(r0.licenseServerUrl) == true) goto L13;
        r15 = r0.licenseServerUrl;
    L13:
        if (Util.SDK_INT < 23) goto L15;
        int r2 = r13.getRequestType();
    L17:
        return new ExoMediaDrm.KeyRequest(r14, r15, r2);
    L15:
        r2 = Integer.MIN_VALUE;
        goto L17
    L5:
        r3 = null;
        r4 = null;
        goto L6
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public PersistableBundle getMetrics() {
        if (Util.SDK_INT >= 28) goto L7;
        return null;
    L7:
        return this.mediaDrm.getMetrics();
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public List<byte[]> getOfflineLicenseKeySetIds() {
        if (Util.SDK_INT < 29) goto L7;
        return this.mediaDrm.getOfflineLicenseKeySetIds();
    L7:
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public byte[] getPropertyByteArray(String r2) {
        return this.mediaDrm.getPropertyByteArray(r2);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public String getPropertyString(String r2) {
        return this.mediaDrm.getPropertyString(r2);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public ExoMediaDrm.ProvisionRequest getProvisionRequest() {
        MediaDrm.ProvisionRequest r0 = this.mediaDrm.getProvisionRequest();
        return new ExoMediaDrm.ProvisionRequest(r0.getData(), r0.getDefaultUrl());
    }

    /* JADX INFO: renamed from: lambda$setOnEventListener$1$androidx-media3-exoplayer-drm-FrameworkMediaDrm */
    /* synthetic */ void m100x5e84e274(ExoMediaDrm.OnEventListener r7, MediaDrm r8, byte[] r9, int r10, int r11, byte[] r12) {
        r7.onEvent(this, r9, r10, r11, r12);
    }

    /* JADX INFO: renamed from: lambda$setOnExpirationUpdateListener$3$androidx-media3-exoplayer-drm-FrameworkMediaDrm */
    /* synthetic */ void m101x3bcdcffc(ExoMediaDrm.OnExpirationUpdateListener r1, MediaDrm r2, byte[] r3, long r4) {
        r1.onExpirationUpdate(this, r3, r4);
    }

    /* JADX INFO: renamed from: lambda$setOnKeyStatusChangeListener$2$androidx-media3-exoplayer-drm-FrameworkMediaDrm */
    /* synthetic */ void m102xc78bb65c(ExoMediaDrm.OnKeyStatusChangeListener r7, MediaDrm r8, byte[] r9, List r10, boolean r11) {
        ArrayList r0 = new ArrayList();
        Iterator r1 = r10.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        MediaDrm.KeyStatus r2 = (MediaDrm.KeyStatus) r1.next();
        r0.add(new ExoMediaDrm.KeyStatus(r2.getStatusCode(), r2.getKeyId()));
        goto L4
    L6:
        r7.onKeyStatusChange(this, r9, r0, r11);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public byte[] openSession() throws MediaDrmException {
        return this.mediaDrm.openSession();
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public byte[] provideKeyResponse(byte[] r3, byte[] r4) throws NotProvisionedException, DeniedByServerException {
        if (C0180C.CLEARKEY_UUID.equals(this.uuid) == false) goto L6;
        r4 = ClearKeyUtil.adjustResponseData(r4);
    L6:
        return this.mediaDrm.provideKeyResponse(r3, r4);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void provideProvisionResponse(byte[] r2) throws DeniedByServerException {
        this.mediaDrm.provideProvisionResponse(r2);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public Map<String, String> queryKeyStatus(byte[] r2) {
        return this.mediaDrm.queryKeyStatus(r2);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public synchronized void release() {
        monitor-enter(this);
        int r0 = this.referenceCount - 1;     // Catch: Throwable -> L8
        this.referenceCount = r0;     // Catch: Throwable -> L8
        if (r0 != 0) goto L6;
        this.mediaDrm.release();     // Catch: Throwable -> L8
    L6:
        monitor-exit(this);
        return;
    L8:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void removeOfflineLicense(byte[] r3) {
        if (Util.SDK_INT < 29) goto L7;
        this.mediaDrm.removeOfflineLicense(r3);
        return;
    L7:
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public boolean requiresSecureDecoder(byte[] r4, String r5) {
        if (Util.SDK_INT >= 31) goto L5;
    L7:
        MediaCrypto r0 = null;
        r0 = new MediaCrypto(this.uuid, r4);     // Catch: Throwable -> L10 MediaCryptoException -> L14
        boolean r1 = r0.requiresSecureDecoderComponent(r5);     // Catch: Throwable -> L10 MediaCryptoException -> L14
    L17:
        r0.release();
    L19:
        return r1;
    L10:
        th = move-exception;
        if (r0 == null) goto L13;
        r0.release();
    L13:
        throw th;
    L15:
        r1 = true;
        if (r0 == null) goto L19;
    L5:
        if (isMediaDrmRequiresSecureDecoderImplemented() == false) goto L7;
        return Api31.requiresSecureDecoder(this.mediaDrm, r5, this.mediaDrm.getSecurityLevel(r4));
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void restoreKeys(byte[] r2, byte[] r3) {
        this.mediaDrm.restoreKeys(r2, r3);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void setOnEventListener(final ExoMediaDrm.OnEventListener r3) {
        MediaDrm r0 = this.mediaDrm;
        if (r3 != null) goto L5;
        MediaDrm.OnEventListener r1 = null;
    L6:
        r0.setOnEventListener(r1);
        return;
    L5:
        r1 = new FrameworkMediaDrm$$ExternalSyntheticLambda2(this, r3);
        goto L6
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void setOnExpirationUpdateListener(final ExoMediaDrm.OnExpirationUpdateListener r4) {
        if (Util.SDK_INT < 23) goto L11;
        MediaDrm r0 = this.mediaDrm;
        if (r4 != null) goto L7;
        MediaDrm.OnExpirationUpdateListener r2 = null;
    L8:
        r0.setOnExpirationUpdateListener(r2, null);
        return;
    L7:
        r2 = new FrameworkMediaDrm$$ExternalSyntheticLambda1(this, r4);
        goto L8
    L11:
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void setOnKeyStatusChangeListener(final ExoMediaDrm.OnKeyStatusChangeListener r4) {
        if (Util.SDK_INT < 23) goto L11;
        MediaDrm r0 = this.mediaDrm;
        if (r4 != null) goto L7;
        MediaDrm.OnKeyStatusChangeListener r2 = null;
    L8:
        r0.setOnKeyStatusChangeListener(r2, null);
        return;
    L7:
        r2 = new FrameworkMediaDrm$$ExternalSyntheticLambda3(this, r4);
        goto L8
    L11:
        throw new UnsupportedOperationException();
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void setPlayerIdForSession(byte[] r4, PlayerId r5) {
        if (Util.SDK_INT < 31) goto L11;
        Api31.setLogSessionIdOnMediaDrmSession(this.mediaDrm, r4, r5);     // Catch: UnsupportedOperationException -> L6
        return;
    L7:
        Log.m48w(TAG, "setLogSessionId failed.");
        return;
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void setPropertyByteArray(String r2, byte[] r3) {
        this.mediaDrm.setPropertyByteArray(r2, r3);
    }

    @Override // androidx.media3.exoplayer.drm.ExoMediaDrm
    public void setPropertyString(String r2, String r3) {
        this.mediaDrm.setPropertyString(r2, r3);
    }
}
