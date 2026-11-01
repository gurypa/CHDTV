package androidx.media3.common;

import android.net.Uri;
import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class MediaItem {
    public static final String DEFAULT_MEDIA_ID = "";
    public static final MediaItem EMPTY = null;
    private static final String FIELD_CLIPPING_PROPERTIES = null;
    private static final String FIELD_LIVE_CONFIGURATION = null;
    private static final String FIELD_LOCAL_CONFIGURATION = null;
    private static final String FIELD_MEDIA_ID = null;
    private static final String FIELD_MEDIA_METADATA = null;
    private static final String FIELD_REQUEST_METADATA = null;
    public final ClippingConfiguration clippingConfiguration;

    @Deprecated
    public final ClippingProperties clippingProperties;
    public final LiveConfiguration liveConfiguration;
    public final LocalConfiguration localConfiguration;
    public final String mediaId;
    public final MediaMetadata mediaMetadata;

    @Deprecated
    public final LocalConfiguration playbackProperties;
    public final RequestMetadata requestMetadata;

    /* JADX INFO: renamed from: androidx.media3.common.MediaItem$1 */
    static /* synthetic */ class C01891 {
    }

    public static final class AdsConfiguration {
        private static final String FIELD_AD_TAG_URI = null;
        public final Uri adTagUri;
        public final Object adsId;

        public static final class Builder {
            private Uri adTagUri;
            private Object adsId;

            public Builder(Uri r1) {
                this.adTagUri = r1;
            }

            static /* synthetic */ Uri access$1600(Builder r1) {
                return r1.adTagUri;
            }

            static /* synthetic */ Object access$1700(Builder r1) {
                return r1.adsId;
            }

            public AdsConfiguration build() {
                return new AdsConfiguration(this, null);
            }

            public Builder setAdTagUri(Uri r1) {
                this.adTagUri = r1;
                return this;
            }

            public Builder setAdsId(Object r1) {
                this.adsId = r1;
                return this;
            }
        }

        static {
            FIELD_AD_TAG_URI = Util.intToStringMaxRadix(0);
        }

        private AdsConfiguration(Builder r2) {
            this.adTagUri = Builder.access$1600(r2);
            this.adsId = Builder.access$1700(r2);
        }

        /* synthetic */ AdsConfiguration(Builder r1, C01891 r2) {
            this(r1);
        }

        public static AdsConfiguration fromBundle(Bundle r2) {
            Uri r0 = (Uri) r2.getParcelable(FIELD_AD_TAG_URI);
            Assertions.checkNotNull(r0);
            return new Builder(r0).build();
        }

        public Builder buildUpon() {
            return new Builder(this.adTagUri).setAdsId(this.adsId);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof AdsConfiguration) == true) goto L8;
            return false;
        L8:
            AdsConfiguration r1 = (AdsConfiguration) r6;
            if (this.adTagUri.equals(r1.adTagUri) == false) goto L14;
            if (Util.areEqual(this.adsId, r1.adsId) == false) goto L14;
            return true;
        L14:
            return false;
        }

        public int hashCode() {
            int r1 = this.adTagUri.hashCode() * 31;
            if (this.adsId == null) goto L5;
            int r2 = this.adsId.hashCode();
        L7:
            return r1 + r2;
        L5:
            r2 = 0;
            goto L7
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putParcelable(FIELD_AD_TAG_URI, this.adTagUri);
            return r0;
        }
    }

    public static final class Builder {
        private AdsConfiguration adsConfiguration;
        private ClippingConfiguration.Builder clippingConfiguration;
        private String customCacheKey;
        private DrmConfiguration.Builder drmConfiguration;
        private long imageDurationMs;
        private LiveConfiguration.Builder liveConfiguration;
        private String mediaId;
        private MediaMetadata mediaMetadata;
        private String mimeType;
        private RequestMetadata requestMetadata;
        private List<StreamKey> streamKeys;
        private ImmutableList<SubtitleConfiguration> subtitleConfigurations;
        private Object tag;
        private Uri uri;

        public Builder() {
            this.clippingConfiguration = new ClippingConfiguration.Builder();
            this.drmConfiguration = new DrmConfiguration.Builder(null);
            this.streamKeys = Collections.emptyList();
            this.subtitleConfigurations = ImmutableList.m212of();
            this.liveConfiguration = new LiveConfiguration.Builder();
            this.requestMetadata = RequestMetadata.EMPTY;
            this.imageDurationMs = C0180C.TIME_UNSET;
        }

        private Builder(MediaItem r4) {
            this();
            this.clippingConfiguration = r4.clippingConfiguration.buildUpon();
            this.mediaId = r4.mediaId;
            this.mediaMetadata = r4.mediaMetadata;
            this.liveConfiguration = r4.liveConfiguration.buildUpon();
            this.requestMetadata = r4.requestMetadata;
            LocalConfiguration r0 = r4.localConfiguration;
            if (r0 == null) goto L10;
            this.customCacheKey = r0.customCacheKey;
            this.mimeType = r0.mimeType;
            this.uri = r0.uri;
            this.streamKeys = r0.streamKeys;
            this.subtitleConfigurations = r0.subtitleConfigurations;
            this.tag = r0.tag;
            if (r0.drmConfiguration == null) goto L7;
            DrmConfiguration.Builder r1 = r0.drmConfiguration.buildUpon();
        L8:
            this.drmConfiguration = r1;
            this.adsConfiguration = r0.adsConfiguration;
            this.imageDurationMs = r0.imageDurationMs;
            return;
        L7:
            r1 = new DrmConfiguration.Builder(null);
            goto L8
        }

        /* synthetic */ Builder(MediaItem r1, C01891 r2) {
            this(r1);
        }

        public MediaItem build() {
            if (DrmConfiguration.Builder.access$200(this.drmConfiguration) != null) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkState(r0);
            Uri r2 = this.uri;
            if (r2 == null) goto L16;
            String r3 = this.mimeType;
            if (DrmConfiguration.Builder.access$300(this.drmConfiguration) == null) goto L14;
            DrmConfiguration r4 = this.drmConfiguration.build();
        L15:
            LocalConfiguration r6 = new LocalConfiguration(r2, r3, r4, this.adsConfiguration, this.streamKeys, this.customCacheKey, this.subtitleConfigurations, this.tag, this.imageDurationMs, null);
        L18:
            if (this.mediaId == null) goto L20;
            String r1 = this.mediaId;
        L21:
            String r5 = r1;
            ClippingProperties r7 = this.clippingConfiguration.buildClippingProperties();
            LiveConfiguration r8 = this.liveConfiguration.build();
            if (this.mediaMetadata == null) goto L24;
            MediaMetadata r9 = this.mediaMetadata;
        L26:
            return new MediaItem(r5, r7, r6, r8, r9, this.requestMetadata, null);
        L24:
            r9 = MediaMetadata.EMPTY;
            goto L26
        L20:
            r1 = "";
            goto L21
        L14:
            r4 = null;
            goto L15
        L16:
            r6 = null;
            goto L18
        L5:
            if (DrmConfiguration.Builder.access$300(this.drmConfiguration) != null) goto L8;
            r0 = false;
            goto L9
        }

        @Deprecated
        public Builder setAdTagUri(Uri r2) {
            return setAdTagUri(r2, null);
        }

        @Deprecated
        public Builder setAdTagUri(Uri r2, Object r3) {
            if (r2 == null) goto L4;
            AdsConfiguration r0 = new AdsConfiguration.Builder(r2).setAdsId(r3).build();
        L5:
            this.adsConfiguration = r0;
            return this;
        L4:
            r0 = null;
            goto L5
        }

        @Deprecated
        public Builder setAdTagUri(String r2) {
            if (r2 == null) goto L4;
            Uri r0 = Uri.parse(r2);
        L6:
            return setAdTagUri(r0);
        L4:
            r0 = null;
            goto L6
        }

        public Builder setAdsConfiguration(AdsConfiguration r1) {
            this.adsConfiguration = r1;
            return this;
        }

        @Deprecated
        public Builder setClipEndPositionMs(long r2) {
            this.clippingConfiguration.setEndPositionMs(r2);
            return this;
        }

        @Deprecated
        public Builder setClipRelativeToDefaultPosition(boolean r2) {
            this.clippingConfiguration.setRelativeToDefaultPosition(r2);
            return this;
        }

        @Deprecated
        public Builder setClipRelativeToLiveWindow(boolean r2) {
            this.clippingConfiguration.setRelativeToLiveWindow(r2);
            return this;
        }

        @Deprecated
        public Builder setClipStartPositionMs(long r2) {
            this.clippingConfiguration.setStartPositionMs(r2);
            return this;
        }

        @Deprecated
        public Builder setClipStartsAtKeyFrame(boolean r2) {
            this.clippingConfiguration.setStartsAtKeyFrame(r2);
            return this;
        }

        public Builder setClippingConfiguration(ClippingConfiguration r2) {
            this.clippingConfiguration = r2.buildUpon();
            return this;
        }

        public Builder setCustomCacheKey(String r1) {
            this.customCacheKey = r1;
            return this;
        }

        public Builder setDrmConfiguration(DrmConfiguration r3) {
            if (r3 == null) goto L4;
            DrmConfiguration.Builder r0 = r3.buildUpon();
        L5:
            this.drmConfiguration = r0;
            return this;
        L4:
            r0 = new DrmConfiguration.Builder(null);
            goto L5
        }

        @Deprecated
        public Builder setDrmForceDefaultLicenseUri(boolean r2) {
            this.drmConfiguration.setForceDefaultLicenseUri(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmKeySetId(byte[] r2) {
            this.drmConfiguration.setKeySetId(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmLicenseRequestHeaders(Map<String, String> r3) {
            DrmConfiguration.Builder r0 = this.drmConfiguration;
            if (r3 == null) goto L5;
            Map<String, String> r1 = r3;
        L6:
            r0.setLicenseRequestHeaders(r1);
            return this;
        L5:
            r1 = ImmutableMap.m231of();
            goto L6
        }

        @Deprecated
        public Builder setDrmLicenseUri(Uri r2) {
            this.drmConfiguration.setLicenseUri(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmLicenseUri(String r2) {
            this.drmConfiguration.setLicenseUri(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmMultiSession(boolean r2) {
            this.drmConfiguration.setMultiSession(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmPlayClearContentWithoutKey(boolean r2) {
            this.drmConfiguration.setPlayClearContentWithoutKey(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmSessionForClearPeriods(boolean r2) {
            this.drmConfiguration.setForceSessionsForAudioAndVideoTracks(r2);
            return this;
        }

        @Deprecated
        public Builder setDrmSessionForClearTypes(List<Integer> r3) {
            DrmConfiguration.Builder r0 = this.drmConfiguration;
            if (r3 == null) goto L5;
            List<Integer> r1 = r3;
        L6:
            r0.setForcedSessionTrackTypes(r1);
            return this;
        L5:
            r1 = ImmutableList.m212of();
            goto L6
        }

        @Deprecated
        public Builder setDrmUuid(UUID r2) {
            DrmConfiguration.Builder.access$100(this.drmConfiguration, r2);
            return this;
        }

        public Builder setImageDurationMs(long r3) {
            if (r3 <= 0) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkArgument(r0);
            this.imageDurationMs = r3;
            return this;
        L5:
            if (r3 == C0180C.TIME_UNSET) goto L8;
            r0 = false;
            goto L9
        }

        public Builder setLiveConfiguration(LiveConfiguration r2) {
            this.liveConfiguration = r2.buildUpon();
            return this;
        }

        @Deprecated
        public Builder setLiveMaxOffsetMs(long r2) {
            this.liveConfiguration.setMaxOffsetMs(r2);
            return this;
        }

        @Deprecated
        public Builder setLiveMaxPlaybackSpeed(float r2) {
            this.liveConfiguration.setMaxPlaybackSpeed(r2);
            return this;
        }

        @Deprecated
        public Builder setLiveMinOffsetMs(long r2) {
            this.liveConfiguration.setMinOffsetMs(r2);
            return this;
        }

        @Deprecated
        public Builder setLiveMinPlaybackSpeed(float r2) {
            this.liveConfiguration.setMinPlaybackSpeed(r2);
            return this;
        }

        @Deprecated
        public Builder setLiveTargetOffsetMs(long r2) {
            this.liveConfiguration.setTargetOffsetMs(r2);
            return this;
        }

        public Builder setMediaId(String r2) {
            this.mediaId = (String) Assertions.checkNotNull(r2);
            return this;
        }

        public Builder setMediaMetadata(MediaMetadata r1) {
            this.mediaMetadata = r1;
            return this;
        }

        public Builder setMimeType(String r1) {
            this.mimeType = r1;
            return this;
        }

        public Builder setRequestMetadata(RequestMetadata r1) {
            this.requestMetadata = r1;
            return this;
        }

        public Builder setStreamKeys(List<StreamKey> r2) {
            if (r2 != null) goto L4;
        L6:
            List<StreamKey> r0 = Collections.emptyList();
        L7:
            this.streamKeys = r0;
            return this;
        L4:
            if (r2.isEmpty() == true) goto L6;
            r0 = Collections.unmodifiableList(new ArrayList(r2));
            goto L7
        }

        public Builder setSubtitleConfigurations(List<SubtitleConfiguration> r2) {
            this.subtitleConfigurations = ImmutableList.copyOf(r2);
            return this;
        }

        @Deprecated
        public Builder setSubtitles(List<Subtitle> r2) {
            if (r2 == null) goto L4;
            ImmutableList<SubtitleConfiguration> r0 = ImmutableList.copyOf(r2);
        L5:
            this.subtitleConfigurations = r0;
            return this;
        L4:
            r0 = ImmutableList.m212of();
            goto L5
        }

        public Builder setTag(Object r1) {
            this.tag = r1;
            return this;
        }

        public Builder setUri(Uri r1) {
            this.uri = r1;
            return this;
        }

        public Builder setUri(String r2) {
            if (r2 != null) goto L4;
            Uri r0 = null;
        L6:
            return setUri(r0);
        L4:
            r0 = Uri.parse(r2);
            goto L6
        }
    }

    public static class ClippingConfiguration {
        private static final String FIELD_END_POSITION_MS = null;
        static final String FIELD_END_POSITION_US = null;
        private static final String FIELD_RELATIVE_TO_DEFAULT_POSITION = null;
        private static final String FIELD_RELATIVE_TO_LIVE_WINDOW = null;
        private static final String FIELD_STARTS_AT_KEY_FRAME = null;
        private static final String FIELD_START_POSITION_MS = null;
        static final String FIELD_START_POSITION_US = null;
        public static final ClippingConfiguration UNSET = null;
        public final long endPositionMs;
        public final long endPositionUs;
        public final boolean relativeToDefaultPosition;
        public final boolean relativeToLiveWindow;
        public final long startPositionMs;
        public final long startPositionUs;
        public final boolean startsAtKeyFrame;

        public static final class Builder {
            private long endPositionUs;
            private boolean relativeToDefaultPosition;
            private boolean relativeToLiveWindow;
            private long startPositionUs;
            private boolean startsAtKeyFrame;

            public Builder() {
                this.endPositionUs = Long.MIN_VALUE;
            }

            private Builder(ClippingConfiguration r3) {
                this.startPositionUs = r3.startPositionUs;
                this.endPositionUs = r3.endPositionUs;
                this.relativeToLiveWindow = r3.relativeToLiveWindow;
                this.relativeToDefaultPosition = r3.relativeToDefaultPosition;
                this.startsAtKeyFrame = r3.startsAtKeyFrame;
            }

            /* synthetic */ Builder(ClippingConfiguration r1, C01891 r2) {
                this(r1);
            }

            static /* synthetic */ long access$3900(Builder r2) {
                return r2.startPositionUs;
            }

            static /* synthetic */ long access$4000(Builder r2) {
                return r2.endPositionUs;
            }

            static /* synthetic */ boolean access$4100(Builder r1) {
                return r1.relativeToLiveWindow;
            }

            static /* synthetic */ boolean access$4200(Builder r1) {
                return r1.relativeToDefaultPosition;
            }

            static /* synthetic */ boolean access$4300(Builder r1) {
                return r1.startsAtKeyFrame;
            }

            public ClippingConfiguration build() {
                return new ClippingConfiguration(this, null);
            }

            @Deprecated
            public ClippingProperties buildClippingProperties() {
                return new ClippingProperties(this, null);
            }

            public Builder setEndPositionMs(long r3) {
                return setEndPositionUs(Util.msToUs(r3));
            }

            public Builder setEndPositionUs(long r3) {
                if (r3 != Long.MIN_VALUE) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.endPositionUs = r3;
                return this;
            L5:
                if (r3 >= 0) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setRelativeToDefaultPosition(boolean r1) {
                this.relativeToDefaultPosition = r1;
                return this;
            }

            public Builder setRelativeToLiveWindow(boolean r1) {
                this.relativeToLiveWindow = r1;
                return this;
            }

            public Builder setStartPositionMs(long r3) {
                return setStartPositionUs(Util.msToUs(r3));
            }

            public Builder setStartPositionUs(long r3) {
                if (r3 < 0) goto L5;
                boolean r0 = true;
            L6:
                Assertions.checkArgument(r0);
                this.startPositionUs = r3;
                return this;
            L5:
                r0 = false;
                goto L6
            }

            public Builder setStartsAtKeyFrame(boolean r1) {
                this.startsAtKeyFrame = r1;
                return this;
            }
        }

        static {
            UNSET = new Builder().build();
            FIELD_START_POSITION_MS = Util.intToStringMaxRadix(0);
            FIELD_END_POSITION_MS = Util.intToStringMaxRadix(1);
            FIELD_RELATIVE_TO_LIVE_WINDOW = Util.intToStringMaxRadix(2);
            FIELD_RELATIVE_TO_DEFAULT_POSITION = Util.intToStringMaxRadix(3);
            FIELD_STARTS_AT_KEY_FRAME = Util.intToStringMaxRadix(4);
            FIELD_START_POSITION_US = Util.intToStringMaxRadix(5);
            FIELD_END_POSITION_US = Util.intToStringMaxRadix(6);
        }

        private ClippingConfiguration(Builder r3) {
            this.startPositionMs = Util.usToMs(Builder.access$3900(r3));
            this.endPositionMs = Util.usToMs(Builder.access$4000(r3));
            this.startPositionUs = Builder.access$3900(r3);
            this.endPositionUs = Builder.access$4000(r3);
            this.relativeToLiveWindow = Builder.access$4100(r3);
            this.relativeToDefaultPosition = Builder.access$4200(r3);
            this.startsAtKeyFrame = Builder.access$4300(r3);
        }

        /* synthetic */ ClippingConfiguration(Builder r1, C01891 r2) {
            this(r1);
        }

        public static ClippingProperties fromBundle(Bundle r7) {
            Builder r0 = new Builder().setStartPositionMs(r7.getLong(FIELD_START_POSITION_MS, UNSET.startPositionMs)).setEndPositionMs(r7.getLong(FIELD_END_POSITION_MS, UNSET.endPositionMs)).setRelativeToLiveWindow(r7.getBoolean(FIELD_RELATIVE_TO_LIVE_WINDOW, UNSET.relativeToLiveWindow)).setRelativeToDefaultPosition(r7.getBoolean(FIELD_RELATIVE_TO_DEFAULT_POSITION, UNSET.relativeToDefaultPosition)).setStartsAtKeyFrame(r7.getBoolean(FIELD_STARTS_AT_KEY_FRAME, UNSET.startsAtKeyFrame));
            long r1 = r7.getLong(FIELD_START_POSITION_US, UNSET.startPositionUs);
            if (r1 == UNSET.startPositionUs) goto L5;
            r0.setStartPositionUs(r1);
        L5:
            long r3 = r7.getLong(FIELD_END_POSITION_US, UNSET.endPositionUs);
            if (r3 == UNSET.endPositionUs) goto L9;
            r0.setEndPositionUs(r3);
        L9:
            return r0.buildClippingProperties();
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof ClippingConfiguration) == true) goto L8;
            return false;
        L8:
            ClippingConfiguration r1 = (ClippingConfiguration) r8;
            if (this.startPositionUs != r1.startPositionUs) goto L20;
            if (this.endPositionUs != r1.endPositionUs) goto L20;
            if (this.relativeToLiveWindow != r1.relativeToLiveWindow) goto L20;
            if (this.relativeToDefaultPosition != r1.relativeToDefaultPosition) goto L20;
            if (this.startsAtKeyFrame != r1.startsAtKeyFrame) goto L20;
            return true;
        L20:
            return false;
        }

        public int hashCode() {
            return (((((((((int) (this.startPositionUs ^ (this.startPositionUs >>> 32))) * 31) + ((int) (this.endPositionUs ^ (this.endPositionUs >>> 32)))) * 31) + (this.relativeToLiveWindow ? 1 : 0)) * 31) + (this.relativeToDefaultPosition ? 1 : 0)) * 31) + (this.startsAtKeyFrame ? 1 : 0);
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            if (this.startPositionMs == UNSET.startPositionMs) goto L6;
            r0.putLong(FIELD_START_POSITION_MS, this.startPositionMs);
        L6:
            if (this.endPositionMs == UNSET.endPositionMs) goto L9;
            r0.putLong(FIELD_END_POSITION_MS, this.endPositionMs);
        L9:
            if (this.startPositionUs == UNSET.startPositionUs) goto L12;
            r0.putLong(FIELD_START_POSITION_US, this.startPositionUs);
        L12:
            if (this.endPositionUs == UNSET.endPositionUs) goto L15;
            r0.putLong(FIELD_END_POSITION_US, this.endPositionUs);
        L15:
            if (this.relativeToLiveWindow == UNSET.relativeToLiveWindow) goto L18;
            r0.putBoolean(FIELD_RELATIVE_TO_LIVE_WINDOW, this.relativeToLiveWindow);
        L18:
            if (this.relativeToDefaultPosition == UNSET.relativeToDefaultPosition) goto L21;
            r0.putBoolean(FIELD_RELATIVE_TO_DEFAULT_POSITION, this.relativeToDefaultPosition);
        L21:
            if (this.startsAtKeyFrame == UNSET.startsAtKeyFrame) goto L23;
            r0.putBoolean(FIELD_STARTS_AT_KEY_FRAME, this.startsAtKeyFrame);
        L23:
            return r0;
        }
    }

    @Deprecated
    public static final class ClippingProperties extends ClippingConfiguration {
        public static final ClippingProperties UNSET = null;

        static {
            UNSET = new ClippingConfiguration.Builder().buildClippingProperties();
        }

        private ClippingProperties(ClippingConfiguration.Builder r2) {
            super(r2, null);
        }

        /* synthetic */ ClippingProperties(ClippingConfiguration.Builder r1, C01891 r2) {
            this(r1);
        }
    }

    public static final class DrmConfiguration {
        private static final String FIELD_FORCED_SESSION_TRACK_TYPES = null;
        private static final String FIELD_FORCE_DEFAULT_LICENSE_URI = null;
        private static final String FIELD_KEY_SET_ID = null;
        private static final String FIELD_LICENSE_REQUEST_HEADERS = null;
        private static final String FIELD_LICENSE_URI = null;
        private static final String FIELD_MULTI_SESSION = null;
        static final String FIELD_PLAY_CLEAR_CONTENT_WITHOUT_KEY = null;
        private static final String FIELD_SCHEME = null;
        public final boolean forceDefaultLicenseUri;
        public final ImmutableList<Integer> forcedSessionTrackTypes;
        private final byte[] keySetId;
        public final ImmutableMap<String, String> licenseRequestHeaders;
        public final Uri licenseUri;
        public final boolean multiSession;
        public final boolean playClearContentWithoutKey;

        @Deprecated
        public final ImmutableMap<String, String> requestHeaders;
        public final UUID scheme;

        @Deprecated
        public final ImmutableList<Integer> sessionForClearTypes;

        @Deprecated
        public final UUID uuid;

        public static final class Builder {
            private boolean forceDefaultLicenseUri;
            private ImmutableList<Integer> forcedSessionTrackTypes;
            private byte[] keySetId;
            private ImmutableMap<String, String> licenseRequestHeaders;
            private Uri licenseUri;
            private boolean multiSession;
            private boolean playClearContentWithoutKey;
            private UUID scheme;

            @Deprecated
            private Builder() {
                this.licenseRequestHeaders = ImmutableMap.m231of();
                this.playClearContentWithoutKey = true;
                this.forcedSessionTrackTypes = ImmutableList.m212of();
            }

            /* synthetic */ Builder(C01891 r1) {
                this();
            }

            private Builder(DrmConfiguration r2) {
                this.scheme = r2.scheme;
                this.licenseUri = r2.licenseUri;
                this.licenseRequestHeaders = r2.licenseRequestHeaders;
                this.multiSession = r2.multiSession;
                this.playClearContentWithoutKey = r2.playClearContentWithoutKey;
                this.forceDefaultLicenseUri = r2.forceDefaultLicenseUri;
                this.forcedSessionTrackTypes = r2.forcedSessionTrackTypes;
                this.keySetId = DrmConfiguration.access$600(r2);
            }

            /* synthetic */ Builder(DrmConfiguration r1, C01891 r2) {
                this(r1);
            }

            public Builder(UUID r1) {
                this();
                this.scheme = r1;
            }

            static /* synthetic */ Builder access$100(Builder r1, UUID r2) {
                return r1.setNullableScheme(r2);
            }

            static /* synthetic */ boolean access$1000(Builder r1) {
                return r1.multiSession;
            }

            static /* synthetic */ boolean access$1100(Builder r1) {
                return r1.playClearContentWithoutKey;
            }

            static /* synthetic */ ImmutableList access$1200(Builder r1) {
                return r1.forcedSessionTrackTypes;
            }

            static /* synthetic */ byte[] access$1300(Builder r1) {
                return r1.keySetId;
            }

            static /* synthetic */ Uri access$200(Builder r1) {
                return r1.licenseUri;
            }

            static /* synthetic */ UUID access$300(Builder r1) {
                return r1.scheme;
            }

            static /* synthetic */ boolean access$800(Builder r1) {
                return r1.forceDefaultLicenseUri;
            }

            static /* synthetic */ ImmutableMap access$900(Builder r1) {
                return r1.licenseRequestHeaders;
            }

            @Deprecated
            private Builder setNullableScheme(UUID r1) {
                this.scheme = r1;
                return this;
            }

            public DrmConfiguration build() {
                return new DrmConfiguration(this, null);
            }

            @Deprecated
            public Builder forceSessionsForAudioAndVideoTracks(boolean r2) {
                return setForceSessionsForAudioAndVideoTracks(r2);
            }

            public Builder setForceDefaultLicenseUri(boolean r1) {
                this.forceDefaultLicenseUri = r1;
                return this;
            }

            public Builder setForceSessionsForAudioAndVideoTracks(boolean r3) {
                if (r3 == false) goto L4;
                ImmutableList r0 = ImmutableList.m214of(2, 1);
            L5:
                setForcedSessionTrackTypes(r0);
                return this;
            L4:
                r0 = ImmutableList.m212of();
                goto L5
            }

            public Builder setForcedSessionTrackTypes(List<Integer> r2) {
                this.forcedSessionTrackTypes = ImmutableList.copyOf(r2);
                return this;
            }

            public Builder setKeySetId(byte[] r2) {
                if (r2 == null) goto L4;
                byte[] r0 = Arrays.copyOf(r2, r2.length);
            L5:
                this.keySetId = r0;
                return this;
            L4:
                r0 = null;
                goto L5
            }

            public Builder setLicenseRequestHeaders(Map<String, String> r2) {
                this.licenseRequestHeaders = ImmutableMap.copyOf(r2);
                return this;
            }

            public Builder setLicenseUri(Uri r1) {
                this.licenseUri = r1;
                return this;
            }

            public Builder setLicenseUri(String r2) {
                if (r2 != null) goto L4;
                Uri r0 = null;
            L5:
                this.licenseUri = r0;
                return this;
            L4:
                r0 = Uri.parse(r2);
                goto L5
            }

            public Builder setMultiSession(boolean r1) {
                this.multiSession = r1;
                return this;
            }

            public Builder setPlayClearContentWithoutKey(boolean r1) {
                this.playClearContentWithoutKey = r1;
                return this;
            }

            public Builder setScheme(UUID r1) {
                this.scheme = r1;
                return this;
            }
        }

        static {
            FIELD_SCHEME = Util.intToStringMaxRadix(0);
            FIELD_LICENSE_URI = Util.intToStringMaxRadix(1);
            FIELD_LICENSE_REQUEST_HEADERS = Util.intToStringMaxRadix(2);
            FIELD_MULTI_SESSION = Util.intToStringMaxRadix(3);
            FIELD_PLAY_CLEAR_CONTENT_WITHOUT_KEY = Util.intToStringMaxRadix(4);
            FIELD_FORCE_DEFAULT_LICENSE_URI = Util.intToStringMaxRadix(5);
            FIELD_FORCED_SESSION_TRACK_TYPES = Util.intToStringMaxRadix(6);
            FIELD_KEY_SET_ID = Util.intToStringMaxRadix(7);
        }

        private DrmConfiguration(Builder r3) {
            if (Builder.access$800(r3) == true) goto L5;
        L8:
            boolean r0 = true;
        L9:
            Assertions.checkState(r0);
            this.scheme = (UUID) Assertions.checkNotNull(Builder.access$300(r3));
            this.uuid = this.scheme;
            this.licenseUri = Builder.access$200(r3);
            this.requestHeaders = Builder.access$900(r3);
            this.licenseRequestHeaders = Builder.access$900(r3);
            this.multiSession = Builder.access$1000(r3);
            this.forceDefaultLicenseUri = Builder.access$800(r3);
            this.playClearContentWithoutKey = Builder.access$1100(r3);
            this.sessionForClearTypes = Builder.access$1200(r3);
            this.forcedSessionTrackTypes = Builder.access$1200(r3);
            if (Builder.access$1300(r3) == null) goto L12;
            byte[] r1 = Arrays.copyOf(Builder.access$1300(r3), Builder.access$1300(r3).length);
        L13:
            this.keySetId = r1;
            return;
        L12:
            r1 = null;
            goto L13
        L5:
            if (Builder.access$200(r3) != null) goto L8;
            r0 = false;
            goto L9
        }

        /* synthetic */ DrmConfiguration(Builder r1, C01891 r2) {
            this(r1);
        }

        static /* synthetic */ byte[] access$600(DrmConfiguration r1) {
            return r1.keySetId;
        }

        public static DrmConfiguration fromBundle(Bundle r12) {
            UUID r0 = UUID.fromString((String) Assertions.checkNotNull(r12.getString(FIELD_SCHEME)));
            Uri r1 = (Uri) r12.getParcelable(FIELD_LICENSE_URI);
            ImmutableMap<String, String> r3 = BundleCollectionUtil.bundleToStringImmutableMap(BundleCollectionUtil.getBundleWithDefault(r12, FIELD_LICENSE_REQUEST_HEADERS, Bundle.EMPTY));
            boolean r4 = r12.getBoolean(FIELD_MULTI_SESSION, false);
            boolean r6 = r12.getBoolean(FIELD_PLAY_CLEAR_CONTENT_WITHOUT_KEY, false);
            boolean r5 = r12.getBoolean(FIELD_FORCE_DEFAULT_LICENSE_URI, false);
            ImmutableList r8 = ImmutableList.copyOf(BundleCollectionUtil.getIntegerArrayListWithDefault(r12, FIELD_FORCED_SESSION_TRACK_TYPES, new ArrayList()));
            byte[] r9 = r12.getByteArray(FIELD_KEY_SET_ID);
            return new Builder(r0).setLicenseUri(r1).setLicenseRequestHeaders(r3).setMultiSession(r4).setForceDefaultLicenseUri(r5).setPlayClearContentWithoutKey(r6).setForcedSessionTrackTypes(r8).setKeySetId(r9).build();
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof DrmConfiguration) == true) goto L8;
            return false;
        L8:
            DrmConfiguration r1 = (DrmConfiguration) r6;
            if (this.scheme.equals(r1.scheme) == false) goto L26;
            if (Util.areEqual(this.licenseUri, r1.licenseUri) == false) goto L26;
            if (Util.areEqual(this.licenseRequestHeaders, r1.licenseRequestHeaders) == false) goto L26;
            if (this.multiSession != r1.multiSession) goto L26;
            if (this.forceDefaultLicenseUri != r1.forceDefaultLicenseUri) goto L26;
            if (this.playClearContentWithoutKey != r1.playClearContentWithoutKey) goto L26;
            if (this.forcedSessionTrackTypes.equals(r1.forcedSessionTrackTypes) == false) goto L26;
            if (Arrays.equals(this.keySetId, r1.keySetId) == false) goto L26;
            return true;
        L26:
            return false;
        }

        public byte[] getKeySetId() {
            if (this.keySetId != null) goto L7;
            return null;
        L7:
            return Arrays.copyOf(this.keySetId, this.keySetId.length);
        }

        public int hashCode() {
            int r1 = this.scheme.hashCode() * 31;
            if (this.licenseUri == null) goto L5;
            int r2 = this.licenseUri.hashCode();
        L7:
            return ((((((((((((r1 + r2) * 31) + this.licenseRequestHeaders.hashCode()) * 31) + (this.multiSession ? 1 : 0)) * 31) + (this.forceDefaultLicenseUri ? 1 : 0)) * 31) + (this.playClearContentWithoutKey ? 1 : 0)) * 31) + this.forcedSessionTrackTypes.hashCode()) * 31) + Arrays.hashCode(this.keySetId);
        L5:
            r2 = 0;
            goto L7
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putString(FIELD_SCHEME, this.scheme.toString());
            if (this.licenseUri == null) goto L6;
            r0.putParcelable(FIELD_LICENSE_URI, this.licenseUri);
        L6:
            if (this.licenseRequestHeaders.isEmpty() == true) goto L9;
            r0.putBundle(FIELD_LICENSE_REQUEST_HEADERS, BundleCollectionUtil.stringMapToBundle(this.licenseRequestHeaders));
        L9:
            if (this.multiSession == false) goto L12;
            r0.putBoolean(FIELD_MULTI_SESSION, this.multiSession);
        L12:
            if (this.playClearContentWithoutKey == false) goto L15;
            r0.putBoolean(FIELD_PLAY_CLEAR_CONTENT_WITHOUT_KEY, this.playClearContentWithoutKey);
        L15:
            if (this.forceDefaultLicenseUri == false) goto L18;
            r0.putBoolean(FIELD_FORCE_DEFAULT_LICENSE_URI, this.forceDefaultLicenseUri);
        L18:
            if (this.forcedSessionTrackTypes.isEmpty() == true) goto L21;
            r0.putIntegerArrayList(FIELD_FORCED_SESSION_TRACK_TYPES, new ArrayList(this.forcedSessionTrackTypes));
        L21:
            if (this.keySetId == null) goto L23;
            r0.putByteArray(FIELD_KEY_SET_ID, this.keySetId);
        L23:
            return r0;
        }
    }

    public static final class LiveConfiguration {
        private static final String FIELD_MAX_OFFSET_MS = null;
        private static final String FIELD_MAX_PLAYBACK_SPEED = null;
        private static final String FIELD_MIN_OFFSET_MS = null;
        private static final String FIELD_MIN_PLAYBACK_SPEED = null;
        private static final String FIELD_TARGET_OFFSET_MS = null;
        public static final LiveConfiguration UNSET = null;
        public final long maxOffsetMs;
        public final float maxPlaybackSpeed;
        public final long minOffsetMs;
        public final float minPlaybackSpeed;
        public final long targetOffsetMs;

        public static final class Builder {
            private long maxOffsetMs;
            private float maxPlaybackSpeed;
            private long minOffsetMs;
            private float minPlaybackSpeed;
            private long targetOffsetMs;

            public Builder() {
                this.targetOffsetMs = C0180C.TIME_UNSET;
                this.minOffsetMs = C0180C.TIME_UNSET;
                this.maxOffsetMs = C0180C.TIME_UNSET;
                this.minPlaybackSpeed = -3.4028235E38f;
                this.maxPlaybackSpeed = -3.4028235E38f;
            }

            private Builder(LiveConfiguration r3) {
                this.targetOffsetMs = r3.targetOffsetMs;
                this.minOffsetMs = r3.minOffsetMs;
                this.maxOffsetMs = r3.maxOffsetMs;
                this.minPlaybackSpeed = r3.minPlaybackSpeed;
                this.maxPlaybackSpeed = r3.maxPlaybackSpeed;
            }

            /* synthetic */ Builder(LiveConfiguration r1, C01891 r2) {
                this(r1);
            }

            static /* synthetic */ long access$2000(Builder r2) {
                return r2.targetOffsetMs;
            }

            static /* synthetic */ long access$2100(Builder r2) {
                return r2.minOffsetMs;
            }

            static /* synthetic */ long access$2200(Builder r2) {
                return r2.maxOffsetMs;
            }

            static /* synthetic */ float access$2300(Builder r1) {
                return r1.minPlaybackSpeed;
            }

            static /* synthetic */ float access$2400(Builder r1) {
                return r1.maxPlaybackSpeed;
            }

            public LiveConfiguration build() {
                return new LiveConfiguration(this, null);
            }

            public Builder setMaxOffsetMs(long r1) {
                this.maxOffsetMs = r1;
                return this;
            }

            public Builder setMaxPlaybackSpeed(float r1) {
                this.maxPlaybackSpeed = r1;
                return this;
            }

            public Builder setMinOffsetMs(long r1) {
                this.minOffsetMs = r1;
                return this;
            }

            public Builder setMinPlaybackSpeed(float r1) {
                this.minPlaybackSpeed = r1;
                return this;
            }

            public Builder setTargetOffsetMs(long r1) {
                this.targetOffsetMs = r1;
                return this;
            }
        }

        static {
            UNSET = new Builder().build();
            FIELD_TARGET_OFFSET_MS = Util.intToStringMaxRadix(0);
            FIELD_MIN_OFFSET_MS = Util.intToStringMaxRadix(1);
            FIELD_MAX_OFFSET_MS = Util.intToStringMaxRadix(2);
            FIELD_MIN_PLAYBACK_SPEED = Util.intToStringMaxRadix(3);
            FIELD_MAX_PLAYBACK_SPEED = Util.intToStringMaxRadix(4);
        }

        @Deprecated
        public LiveConfiguration(long r1, long r3, long r5, float r7, float r8) {
            this.targetOffsetMs = r1;
            this.minOffsetMs = r3;
            this.maxOffsetMs = r5;
            this.minPlaybackSpeed = r7;
            this.maxPlaybackSpeed = r8;
        }

        private LiveConfiguration(Builder r10) {
            this(Builder.access$2000(r10), Builder.access$2100(r10), Builder.access$2200(r10), Builder.access$2300(r10), Builder.access$2400(r10));
        }

        /* synthetic */ LiveConfiguration(Builder r1, C01891 r2) {
            this(r1);
        }

        public static LiveConfiguration fromBundle(Bundle r4) {
            return new Builder().setTargetOffsetMs(r4.getLong(FIELD_TARGET_OFFSET_MS, UNSET.targetOffsetMs)).setMinOffsetMs(r4.getLong(FIELD_MIN_OFFSET_MS, UNSET.minOffsetMs)).setMaxOffsetMs(r4.getLong(FIELD_MAX_OFFSET_MS, UNSET.maxOffsetMs)).setMinPlaybackSpeed(r4.getFloat(FIELD_MIN_PLAYBACK_SPEED, UNSET.minPlaybackSpeed)).setMaxPlaybackSpeed(r4.getFloat(FIELD_MAX_PLAYBACK_SPEED, UNSET.maxPlaybackSpeed)).build();
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if ((r8 instanceof LiveConfiguration) == true) goto L8;
            return false;
        L8:
            LiveConfiguration r1 = (LiveConfiguration) r8;
            if (this.targetOffsetMs != r1.targetOffsetMs) goto L20;
            if (this.minOffsetMs != r1.minOffsetMs) goto L20;
            if (this.maxOffsetMs != r1.maxOffsetMs) goto L20;
            if (this.minPlaybackSpeed != r1.minPlaybackSpeed) goto L20;
            if (this.maxPlaybackSpeed != r1.maxPlaybackSpeed) goto L20;
            return true;
        L20:
            return false;
        }

        public int hashCode() {
            int r1 = ((((((int) (this.targetOffsetMs ^ (this.targetOffsetMs >>> 32))) * 31) + ((int) (this.minOffsetMs ^ (this.minOffsetMs >>> 32)))) * 31) + ((int) (this.maxOffsetMs ^ (this.maxOffsetMs >>> 32)))) * 31;
            int r4 = 0;
            if (this.minPlaybackSpeed == 0.0f) goto L5;
            int r2 = Float.floatToIntBits(this.minPlaybackSpeed);
        L6:
            int r0 = (r1 + r2) * 31;
            if (this.maxPlaybackSpeed == 0.0f) goto L10;
            r4 = Float.floatToIntBits(this.maxPlaybackSpeed);
        L10:
            return r0 + r4;
        L5:
            r2 = 0;
            goto L6
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            if (this.targetOffsetMs == UNSET.targetOffsetMs) goto L6;
            r0.putLong(FIELD_TARGET_OFFSET_MS, this.targetOffsetMs);
        L6:
            if (this.minOffsetMs == UNSET.minOffsetMs) goto L9;
            r0.putLong(FIELD_MIN_OFFSET_MS, this.minOffsetMs);
        L9:
            if (this.maxOffsetMs == UNSET.maxOffsetMs) goto L12;
            r0.putLong(FIELD_MAX_OFFSET_MS, this.maxOffsetMs);
        L12:
            if (this.minPlaybackSpeed == UNSET.minPlaybackSpeed) goto L15;
            r0.putFloat(FIELD_MIN_PLAYBACK_SPEED, this.minPlaybackSpeed);
        L15:
            if (this.maxPlaybackSpeed == UNSET.maxPlaybackSpeed) goto L17;
            r0.putFloat(FIELD_MAX_PLAYBACK_SPEED, this.maxPlaybackSpeed);
        L17:
            return r0;
        }
    }

    public static final class LocalConfiguration {
        private static final String FIELD_ADS_CONFIGURATION = null;
        private static final String FIELD_CUSTOM_CACHE_KEY = null;
        private static final String FIELD_DRM_CONFIGURATION = null;
        private static final String FIELD_IMAGE_DURATION_MS = null;
        private static final String FIELD_MIME_TYPE = null;
        private static final String FIELD_STREAM_KEYS = null;
        private static final String FIELD_SUBTITLE_CONFIGURATION = null;
        private static final String FIELD_URI = null;
        public final AdsConfiguration adsConfiguration;
        public final String customCacheKey;
        public final DrmConfiguration drmConfiguration;
        public final long imageDurationMs;
        public final String mimeType;
        public final List<StreamKey> streamKeys;
        public final ImmutableList<SubtitleConfiguration> subtitleConfigurations;

        @Deprecated
        public final List<Subtitle> subtitles;
        public final Object tag;
        public final Uri uri;

        static {
            FIELD_URI = Util.intToStringMaxRadix(0);
            FIELD_MIME_TYPE = Util.intToStringMaxRadix(1);
            FIELD_DRM_CONFIGURATION = Util.intToStringMaxRadix(2);
            FIELD_ADS_CONFIGURATION = Util.intToStringMaxRadix(3);
            FIELD_STREAM_KEYS = Util.intToStringMaxRadix(4);
            FIELD_CUSTOM_CACHE_KEY = Util.intToStringMaxRadix(5);
            FIELD_SUBTITLE_CONFIGURATION = Util.intToStringMaxRadix(6);
            FIELD_IMAGE_DURATION_MS = Util.intToStringMaxRadix(7);
        }

        private LocalConfiguration(Uri r4, String r5, DrmConfiguration r6, AdsConfiguration r7, List<StreamKey> r8, String r9, ImmutableList<SubtitleConfiguration> r10, Object r11, long r12) {
            this.uri = r4;
            this.mimeType = MimeTypes.normalizeMimeType(r5);
            this.drmConfiguration = r6;
            this.adsConfiguration = r7;
            this.streamKeys = r8;
            this.customCacheKey = r9;
            this.subtitleConfigurations = r10;
            ImmutableList.Builder r0 = ImmutableList.builder();
            int r1 = 0;
        L4:
            if (r1 >= r10.size()) goto L6;
            r0.add(SubtitleConfiguration.Builder.access$1800(r10.get(r1).buildUpon()));
            r1 = r1 + 1;
            goto L4
        L6:
            this.subtitles = r0.build();
            this.tag = r11;
            this.imageDurationMs = r12;
        }

        /* synthetic */ LocalConfiguration(Uri r1, String r2, DrmConfiguration r3, AdsConfiguration r4, List r5, String r6, ImmutableList r7, Object r8, long r9, C01891 r11) {
            this(r1, r2, r3, r4, r5, r6, r7, r8, r9);
        }

        public static LocalConfiguration fromBundle(Bundle r15) {
            Bundle r0 = r15.getBundle(FIELD_DRM_CONFIGURATION);
            AdsConfiguration r1 = null;
            if (r0 != null) goto L5;
            DrmConfiguration r2 = null;
        L6:
            DrmConfiguration r6 = r2;
            Bundle r3 = r15.getBundle(FIELD_ADS_CONFIGURATION);
            if (r3 == null) goto L10;
            r1 = AdsConfiguration.fromBundle(r3);
        L10:
            AdsConfiguration r7 = r1;
            ArrayList r4 = r15.getParcelableArrayList(FIELD_STREAM_KEYS);
            if (r4 != null) goto L13;
            ImmutableList r8 = ImmutableList.m212of();
        L14:
            ArrayList r14 = r15.getParcelableArrayList(FIELD_SUBTITLE_CONFIGURATION);
            if (r14 != null) goto L17;
            ImmutableList r10 = ImmutableList.m212of();
        L19:
            return new LocalConfiguration((Uri) Assertions.checkNotNull((Uri) r15.getParcelable(FIELD_URI)), r15.getString(FIELD_MIME_TYPE), r6, r7, r8, r15.getString(FIELD_CUSTOM_CACHE_KEY), r10, null, r15.getLong(FIELD_IMAGE_DURATION_MS, C0180C.TIME_UNSET));
        L17:
            r10 = BundleCollectionUtil.fromBundleList(new MediaItem$LocalConfiguration$$ExternalSyntheticLambda3(), r14);
            goto L19
        L13:
            r8 = BundleCollectionUtil.fromBundleList(new MediaItem$LocalConfiguration$$ExternalSyntheticLambda2(), r4);
            goto L14
        L5:
            r2 = DrmConfiguration.fromBundle(r0);
            goto L6
        }

        public boolean equals(Object r7) {
            if (this != r7) goto L6;
            return true;
        L6:
            if ((r7 instanceof LocalConfiguration) == true) goto L8;
            return false;
        L8:
            LocalConfiguration r1 = (LocalConfiguration) r7;
            if (this.uri.equals(r1.uri) == false) goto L28;
            if (Util.areEqual(this.mimeType, r1.mimeType) == false) goto L28;
            if (Util.areEqual(this.drmConfiguration, r1.drmConfiguration) == false) goto L28;
            if (Util.areEqual(this.adsConfiguration, r1.adsConfiguration) == false) goto L28;
            if (this.streamKeys.equals(r1.streamKeys) == false) goto L28;
            if (Util.areEqual(this.customCacheKey, r1.customCacheKey) == false) goto L28;
            if (this.subtitleConfigurations.equals(r1.subtitleConfigurations) == false) goto L28;
            if (Util.areEqual(this.tag, r1.tag) == false) goto L28;
            if (Util.areEqual(Long.valueOf(this.imageDurationMs), Long.valueOf(r1.imageDurationMs)) == false) goto L28;
            return true;
        L28:
            return false;
        }

        public int hashCode() {
            int r1 = this.uri.hashCode() * 31;
            int r3 = 0;
            if (this.mimeType != null) goto L5;
            int r2 = 0;
        L6:
            int r0 = (r1 + r2) * 31;
            if (this.drmConfiguration != null) goto L9;
            int r4 = 0;
        L10:
            int r5 = (r0 + r4) * 31;
            if (this.adsConfiguration != null) goto L13;
            int r6 = 0;
        L14:
            int r7 = (((r5 + r6) * 31) + this.streamKeys.hashCode()) * 31;
            if (this.customCacheKey != null) goto L17;
            int r8 = 0;
        L18:
            int r9 = (((r7 + r8) * 31) + this.subtitleConfigurations.hashCode()) * 31;
            if (this.tag == null) goto L23;
            r3 = this.tag.hashCode();
        L23:
            return (int) ((((long) (r9 + r3)) * 31) + this.imageDurationMs);
        L17:
            r8 = this.customCacheKey.hashCode();
            goto L18
        L13:
            r6 = this.adsConfiguration.hashCode();
            goto L14
        L9:
            r4 = this.drmConfiguration.hashCode();
            goto L10
        L5:
            r2 = this.mimeType.hashCode();
            goto L6
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putParcelable(FIELD_URI, this.uri);
            if (this.mimeType == null) goto L6;
            r0.putString(FIELD_MIME_TYPE, this.mimeType);
        L6:
            if (this.drmConfiguration == null) goto L9;
            r0.putBundle(FIELD_DRM_CONFIGURATION, this.drmConfiguration.toBundle());
        L9:
            if (this.adsConfiguration == null) goto L12;
            r0.putBundle(FIELD_ADS_CONFIGURATION, this.adsConfiguration.toBundle());
        L12:
            if (this.streamKeys.isEmpty() == true) goto L15;
            r0.putParcelableArrayList(FIELD_STREAM_KEYS, BundleCollectionUtil.toBundleArrayList(this.streamKeys, new MediaItem$LocalConfiguration$$ExternalSyntheticLambda0()));
        L15:
            if (this.customCacheKey == null) goto L18;
            r0.putString(FIELD_CUSTOM_CACHE_KEY, this.customCacheKey);
        L18:
            if (this.subtitleConfigurations.isEmpty() == true) goto L21;
            r0.putParcelableArrayList(FIELD_SUBTITLE_CONFIGURATION, BundleCollectionUtil.toBundleArrayList(this.subtitleConfigurations, new MediaItem$LocalConfiguration$$ExternalSyntheticLambda1()));
        L21:
            if (this.imageDurationMs == C0180C.TIME_UNSET) goto L23;
            r0.putLong(FIELD_IMAGE_DURATION_MS, this.imageDurationMs);
        L23:
            return r0;
        }
    }

    public static final class RequestMetadata {
        public static final RequestMetadata EMPTY = null;
        private static final String FIELD_EXTRAS = null;
        private static final String FIELD_MEDIA_URI = null;
        private static final String FIELD_SEARCH_QUERY = null;
        public final Bundle extras;
        public final Uri mediaUri;
        public final String searchQuery;

        public static final class Builder {
            private Bundle extras;
            private Uri mediaUri;
            private String searchQuery;

            public Builder() {
            }

            private Builder(RequestMetadata r2) {
                this.mediaUri = r2.mediaUri;
                this.searchQuery = r2.searchQuery;
                this.extras = r2.extras;
            }

            /* synthetic */ Builder(RequestMetadata r1, C01891 r2) {
                this(r1);
            }

            static /* synthetic */ Uri access$4600(Builder r1) {
                return r1.mediaUri;
            }

            static /* synthetic */ String access$4700(Builder r1) {
                return r1.searchQuery;
            }

            static /* synthetic */ Bundle access$4800(Builder r1) {
                return r1.extras;
            }

            public RequestMetadata build() {
                return new RequestMetadata(this, null);
            }

            public Builder setExtras(Bundle r1) {
                this.extras = r1;
                return this;
            }

            public Builder setMediaUri(Uri r1) {
                this.mediaUri = r1;
                return this;
            }

            public Builder setSearchQuery(String r1) {
                this.searchQuery = r1;
                return this;
            }
        }

        static {
            EMPTY = new Builder().build();
            FIELD_MEDIA_URI = Util.intToStringMaxRadix(0);
            FIELD_SEARCH_QUERY = Util.intToStringMaxRadix(1);
            FIELD_EXTRAS = Util.intToStringMaxRadix(2);
        }

        private RequestMetadata(Builder r2) {
            this.mediaUri = Builder.access$4600(r2);
            this.searchQuery = Builder.access$4700(r2);
            this.extras = Builder.access$4800(r2);
        }

        /* synthetic */ RequestMetadata(Builder r1, C01891 r2) {
            this(r1);
        }

        public static RequestMetadata fromBundle(Bundle r2) {
            return new Builder().setMediaUri((Uri) r2.getParcelable(FIELD_MEDIA_URI)).setSearchQuery(r2.getString(FIELD_SEARCH_QUERY)).setExtras(r2.getBundle(FIELD_EXTRAS)).build();
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof RequestMetadata) == true) goto L8;
            return false;
        L8:
            RequestMetadata r1 = (RequestMetadata) r6;
            if (Util.areEqual(this.mediaUri, r1.mediaUri) == false) goto L23;
            if (Util.areEqual(this.searchQuery, r1.searchQuery) == false) goto L23;
            if (this.extras != null) goto L15;
            boolean r3 = true;
        L17:
            if (r1.extras != null) goto L19;
            boolean r4 = true;
        L20:
            if (r3 != r4) goto L23;
            return true;
        L19:
            r4 = false;
            goto L20
        L15:
            r3 = false;
        L23:
            return false;
        }

        public int hashCode() {
            int r1 = 0;
            if (this.mediaUri != null) goto L5;
            int r0 = 0;
        L6:
            int r2 = r0 * 31;
            if (this.searchQuery != null) goto L9;
            int r3 = 0;
        L10:
            int r4 = (r2 + r3) * 31;
            if (this.extras == null) goto L15;
            r1 = 1;
        L15:
            return r4 + r1;
        L9:
            r3 = this.searchQuery.hashCode();
            goto L10
        L5:
            r0 = this.mediaUri.hashCode();
            goto L6
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            if (this.mediaUri == null) goto L6;
            r0.putParcelable(FIELD_MEDIA_URI, this.mediaUri);
        L6:
            if (this.searchQuery == null) goto L9;
            r0.putString(FIELD_SEARCH_QUERY, this.searchQuery);
        L9:
            if (this.extras == null) goto L11;
            r0.putBundle(FIELD_EXTRAS, this.extras);
        L11:
            return r0;
        }
    }

    @Deprecated
    public static final class Subtitle extends SubtitleConfiguration {
        @Deprecated
        public Subtitle(Uri r2, String r3, String r4) {
            this(r2, r3, r4, 0);
        }

        @Deprecated
        public Subtitle(Uri r8, String r9, String r10, int r11) {
            this(r8, r9, r10, r11, 0, null);
        }

        @Deprecated
        public Subtitle(Uri r10, String r11, String r12, int r13, int r14, String r15) {
            super(r10, r11, r12, r13, r14, r15, null, null);
        }

        private Subtitle(SubtitleConfiguration.Builder r2) {
            super(r2, null);
        }

        /* synthetic */ Subtitle(SubtitleConfiguration.Builder r1, C01891 r2) {
            this(r1);
        }
    }

    public static class SubtitleConfiguration {
        private static final String FIELD_ID = null;
        private static final String FIELD_LABEL = null;
        private static final String FIELD_LANGUAGE = null;
        private static final String FIELD_MIME_TYPE = null;
        private static final String FIELD_ROLE_FLAGS = null;
        private static final String FIELD_SELECTION_FLAGS = null;
        private static final String FIELD_URI = null;

        /* JADX INFO: renamed from: id */
        public final String f28id;
        public final String label;
        public final String language;
        public final String mimeType;
        public final int roleFlags;
        public final int selectionFlags;
        public final Uri uri;

        public static final class Builder {

            /* JADX INFO: renamed from: id */
            private String f29id;
            private String label;
            private String language;
            private String mimeType;
            private int roleFlags;
            private int selectionFlags;
            private Uri uri;

            public Builder(Uri r1) {
                this.uri = r1;
            }

            private Builder(SubtitleConfiguration r2) {
                this.uri = r2.uri;
                this.mimeType = r2.mimeType;
                this.language = r2.language;
                this.selectionFlags = r2.selectionFlags;
                this.roleFlags = r2.roleFlags;
                this.label = r2.label;
                this.f29id = r2.f28id;
            }

            /* synthetic */ Builder(SubtitleConfiguration r1, C01891 r2) {
                this(r1);
            }

            static /* synthetic */ Subtitle access$1800(Builder r1) {
                return r1.buildSubtitle();
            }

            static /* synthetic */ Uri access$2800(Builder r1) {
                return r1.uri;
            }

            static /* synthetic */ String access$2900(Builder r1) {
                return r1.mimeType;
            }

            static /* synthetic */ String access$3000(Builder r1) {
                return r1.language;
            }

            static /* synthetic */ int access$3100(Builder r1) {
                return r1.selectionFlags;
            }

            static /* synthetic */ int access$3200(Builder r1) {
                return r1.roleFlags;
            }

            static /* synthetic */ String access$3300(Builder r1) {
                return r1.label;
            }

            static /* synthetic */ String access$3400(Builder r1) {
                return r1.f29id;
            }

            private Subtitle buildSubtitle() {
                return new Subtitle(this, null);
            }

            public SubtitleConfiguration build() {
                return new SubtitleConfiguration(this, null);
            }

            public Builder setId(String r1) {
                this.f29id = r1;
                return this;
            }

            public Builder setLabel(String r1) {
                this.label = r1;
                return this;
            }

            public Builder setLanguage(String r1) {
                this.language = r1;
                return this;
            }

            public Builder setMimeType(String r2) {
                this.mimeType = MimeTypes.normalizeMimeType(r2);
                return this;
            }

            public Builder setRoleFlags(int r1) {
                this.roleFlags = r1;
                return this;
            }

            public Builder setSelectionFlags(int r1) {
                this.selectionFlags = r1;
                return this;
            }

            public Builder setUri(Uri r1) {
                this.uri = r1;
                return this;
            }
        }

        static {
            FIELD_URI = Util.intToStringMaxRadix(0);
            FIELD_MIME_TYPE = Util.intToStringMaxRadix(1);
            FIELD_LANGUAGE = Util.intToStringMaxRadix(2);
            FIELD_SELECTION_FLAGS = Util.intToStringMaxRadix(3);
            FIELD_ROLE_FLAGS = Util.intToStringMaxRadix(4);
            FIELD_LABEL = Util.intToStringMaxRadix(5);
            FIELD_ID = Util.intToStringMaxRadix(6);
        }

        private SubtitleConfiguration(Uri r2, String r3, String r4, int r5, int r6, String r7, String r8) {
            this.uri = r2;
            this.mimeType = MimeTypes.normalizeMimeType(r3);
            this.language = r4;
            this.selectionFlags = r5;
            this.roleFlags = r6;
            this.label = r7;
            this.f28id = r8;
        }

        /* synthetic */ SubtitleConfiguration(Uri r1, String r2, String r3, int r4, int r5, String r6, String r7, C01891 r8) {
            this(r1, r2, r3, r4, r5, r6, r7);
        }

        private SubtitleConfiguration(Builder r2) {
            this.uri = Builder.access$2800(r2);
            this.mimeType = Builder.access$2900(r2);
            this.language = Builder.access$3000(r2);
            this.selectionFlags = Builder.access$3100(r2);
            this.roleFlags = Builder.access$3200(r2);
            this.label = Builder.access$3300(r2);
            this.f28id = Builder.access$3400(r2);
        }

        /* synthetic */ SubtitleConfiguration(Builder r1, C01891 r2) {
            this(r1);
        }

        public static SubtitleConfiguration fromBundle(Bundle r9) {
            Uri r0 = (Uri) Assertions.checkNotNull((Uri) r9.getParcelable(FIELD_URI));
            String r1 = r9.getString(FIELD_MIME_TYPE);
            String r2 = r9.getString(FIELD_LANGUAGE);
            int r3 = r9.getInt(FIELD_SELECTION_FLAGS, 0);
            int r4 = r9.getInt(FIELD_ROLE_FLAGS, 0);
            String r5 = r9.getString(FIELD_LABEL);
            String r6 = r9.getString(FIELD_ID);
            return new Builder(r0).setMimeType(r1).setLanguage(r2).setSelectionFlags(r3).setRoleFlags(r4).setLabel(r5).setId(r6).build();
        }

        public Builder buildUpon() {
            return new Builder(this, null);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof SubtitleConfiguration) == true) goto L8;
            return false;
        L8:
            SubtitleConfiguration r1 = (SubtitleConfiguration) r6;
            if (this.uri.equals(r1.uri) == false) goto L24;
            if (Util.areEqual(this.mimeType, r1.mimeType) == false) goto L24;
            if (Util.areEqual(this.language, r1.language) == false) goto L24;
            if (this.selectionFlags != r1.selectionFlags) goto L24;
            if (this.roleFlags != r1.roleFlags) goto L24;
            if (Util.areEqual(this.label, r1.label) == false) goto L24;
            if (Util.areEqual(this.f28id, r1.f28id) == false) goto L24;
            return true;
        L24:
            return false;
        }

        public int hashCode() {
            int r1 = this.uri.hashCode() * 31;
            int r3 = 0;
            if (this.mimeType != null) goto L5;
            int r2 = 0;
        L6:
            int r0 = (r1 + r2) * 31;
            if (this.language != null) goto L9;
            int r4 = 0;
        L10:
            int r5 = (((((r0 + r4) * 31) + this.selectionFlags) * 31) + this.roleFlags) * 31;
            if (this.label != null) goto L13;
            int r6 = 0;
        L14:
            int r7 = (r5 + r6) * 31;
            if (this.f28id == null) goto L19;
            r3 = this.f28id.hashCode();
        L19:
            return r7 + r3;
        L13:
            r6 = this.label.hashCode();
            goto L14
        L9:
            r4 = this.language.hashCode();
            goto L10
        L5:
            r2 = this.mimeType.hashCode();
            goto L6
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putParcelable(FIELD_URI, this.uri);
            if (this.mimeType == null) goto L6;
            r0.putString(FIELD_MIME_TYPE, this.mimeType);
        L6:
            if (this.language == null) goto L9;
            r0.putString(FIELD_LANGUAGE, this.language);
        L9:
            if (this.selectionFlags == 0) goto L12;
            r0.putInt(FIELD_SELECTION_FLAGS, this.selectionFlags);
        L12:
            if (this.roleFlags == 0) goto L15;
            r0.putInt(FIELD_ROLE_FLAGS, this.roleFlags);
        L15:
            if (this.label == null) goto L18;
            r0.putString(FIELD_LABEL, this.label);
        L18:
            if (this.f28id == null) goto L20;
            r0.putString(FIELD_ID, this.f28id);
        L20:
            return r0;
        }
    }

    static {
        EMPTY = new Builder().build();
        FIELD_MEDIA_ID = Util.intToStringMaxRadix(0);
        FIELD_LIVE_CONFIGURATION = Util.intToStringMaxRadix(1);
        FIELD_MEDIA_METADATA = Util.intToStringMaxRadix(2);
        FIELD_CLIPPING_PROPERTIES = Util.intToStringMaxRadix(3);
        FIELD_REQUEST_METADATA = Util.intToStringMaxRadix(4);
        FIELD_LOCAL_CONFIGURATION = Util.intToStringMaxRadix(5);
    }

    private MediaItem(String r1, ClippingProperties r2, LocalConfiguration r3, LiveConfiguration r4, MediaMetadata r5, RequestMetadata r6) {
        this.mediaId = r1;
        this.localConfiguration = r3;
        this.playbackProperties = r3;
        this.liveConfiguration = r4;
        this.mediaMetadata = r5;
        this.clippingConfiguration = r2;
        this.clippingProperties = r2;
        this.requestMetadata = r6;
    }

    /* synthetic */ MediaItem(String r1, ClippingProperties r2, LocalConfiguration r3, LiveConfiguration r4, MediaMetadata r5, RequestMetadata r6, C01891 r7) {
        this(r1, r2, r3, r4, r5, r6);
    }

    public static MediaItem fromBundle(Bundle r12) {
        String r2 = (String) Assertions.checkNotNull(r12.getString(FIELD_MEDIA_ID, ""));
        Bundle r0 = r12.getBundle(FIELD_LIVE_CONFIGURATION);
        if (r0 != null) goto L5;
        LiveConfiguration r5 = LiveConfiguration.UNSET;
    L6:
        Bundle r8 = r12.getBundle(FIELD_MEDIA_METADATA);
        if (r8 != null) goto L9;
        MediaMetadata r6 = MediaMetadata.EMPTY;
    L10:
        Bundle r9 = r12.getBundle(FIELD_CLIPPING_PROPERTIES);
        if (r9 != null) goto L13;
        ClippingProperties r3 = ClippingProperties.UNSET;
    L14:
        Bundle r10 = r12.getBundle(FIELD_REQUEST_METADATA);
        if (r10 != null) goto L17;
        RequestMetadata r7 = RequestMetadata.EMPTY;
    L18:
        Bundle r11 = r12.getBundle(FIELD_LOCAL_CONFIGURATION);
        if (r11 != null) goto L21;
        LocalConfiguration r4 = null;
    L23:
        return new MediaItem(r2, r3, r4, r5, r6, r7);
    L21:
        r4 = LocalConfiguration.fromBundle(r11);
        goto L23
    L17:
        r7 = RequestMetadata.fromBundle(r10);
        goto L18
    L13:
        r3 = ClippingConfiguration.fromBundle(r9);
        goto L14
    L9:
        r6 = MediaMetadata.fromBundle(r8);
        goto L10
    L5:
        r5 = LiveConfiguration.fromBundle(r0);
        goto L6
    }

    public static MediaItem fromUri(Uri r1) {
        return new Builder().setUri(r1).build();
    }

    public static MediaItem fromUri(String r1) {
        return new Builder().setUri(r1).build();
    }

    private Bundle toBundle(boolean r4) {
        Bundle r0 = new Bundle();
        if (this.mediaId.equals("") == true) goto L6;
        r0.putString(FIELD_MEDIA_ID, this.mediaId);
    L6:
        if (this.liveConfiguration.equals(LiveConfiguration.UNSET) == true) goto L9;
        r0.putBundle(FIELD_LIVE_CONFIGURATION, this.liveConfiguration.toBundle());
    L9:
        if (this.mediaMetadata.equals(MediaMetadata.EMPTY) == true) goto L12;
        r0.putBundle(FIELD_MEDIA_METADATA, this.mediaMetadata.toBundle());
    L12:
        if (this.clippingConfiguration.equals(ClippingConfiguration.UNSET) == true) goto L15;
        r0.putBundle(FIELD_CLIPPING_PROPERTIES, this.clippingConfiguration.toBundle());
    L15:
        if (this.requestMetadata.equals(RequestMetadata.EMPTY) == true) goto L17;
        r0.putBundle(FIELD_REQUEST_METADATA, this.requestMetadata.toBundle());
    L17:
        if (r4 == true) goto L19;
    L21:
        return r0;
    L19:
        if (this.localConfiguration == null) goto L21;
        r0.putBundle(FIELD_LOCAL_CONFIGURATION, this.localConfiguration.toBundle());
        goto L21
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof MediaItem) == true) goto L8;
        return false;
    L8:
        MediaItem r1 = (MediaItem) r6;
        if (Util.areEqual(this.mediaId, r1.mediaId) == false) goto L22;
        if (this.clippingConfiguration.equals(r1.clippingConfiguration) == false) goto L22;
        if (Util.areEqual(this.localConfiguration, r1.localConfiguration) == false) goto L22;
        if (Util.areEqual(this.liveConfiguration, r1.liveConfiguration) == false) goto L22;
        if (Util.areEqual(this.mediaMetadata, r1.mediaMetadata) == false) goto L22;
        if (Util.areEqual(this.requestMetadata, r1.requestMetadata) == false) goto L22;
        return true;
    L22:
        return false;
    }

    public int hashCode() {
        int r1 = this.mediaId.hashCode() * 31;
        if (this.localConfiguration == null) goto L5;
        int r2 = this.localConfiguration.hashCode();
    L7:
        return ((((((((r1 + r2) * 31) + this.liveConfiguration.hashCode()) * 31) + this.clippingConfiguration.hashCode()) * 31) + this.mediaMetadata.hashCode()) * 31) + this.requestMetadata.hashCode();
    L5:
        r2 = 0;
        goto L7
    }

    public Bundle toBundle() {
        return toBundle(false);
    }

    public Bundle toBundleIncludeLocalConfiguration() {
        return toBundle(true);
    }
}
