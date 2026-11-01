package androidx.media3.common;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class Format {
    public static final int CUE_REPLACEMENT_BEHAVIOR_MERGE = 1;
    public static final int CUE_REPLACEMENT_BEHAVIOR_REPLACE = 2;
    private static final Format DEFAULT = null;
    private static final String FIELD_ACCESSIBILITY_CHANNEL = null;
    private static final String FIELD_AUXILIARY_TRACK_TYPE = null;
    private static final String FIELD_AVERAGE_BITRATE = null;
    private static final String FIELD_CHANNEL_COUNT = null;
    private static final String FIELD_CODECS = null;
    private static final String FIELD_COLOR_INFO = null;
    private static final String FIELD_CONTAINER_MIME_TYPE = null;
    private static final String FIELD_CRYPTO_TYPE = null;
    private static final String FIELD_DRM_INIT_DATA = null;
    private static final String FIELD_ENCODER_DELAY = null;
    private static final String FIELD_ENCODER_PADDING = null;
    private static final String FIELD_FRAME_RATE = null;
    private static final String FIELD_HEIGHT = null;
    private static final String FIELD_ID = null;
    private static final String FIELD_INITIALIZATION_DATA = null;
    private static final String FIELD_LABEL = null;
    private static final String FIELD_LABELS = null;
    private static final String FIELD_LANGUAGE = null;
    private static final String FIELD_MAX_INPUT_SIZE = null;
    private static final String FIELD_METADATA = null;
    private static final String FIELD_PCM_ENCODING = null;
    private static final String FIELD_PEAK_BITRATE = null;
    private static final String FIELD_PIXEL_WIDTH_HEIGHT_RATIO = null;
    private static final String FIELD_PROJECTION_DATA = null;
    private static final String FIELD_ROLE_FLAGS = null;
    private static final String FIELD_ROTATION_DEGREES = null;
    private static final String FIELD_SAMPLE_MIME_TYPE = null;
    private static final String FIELD_SAMPLE_RATE = null;
    private static final String FIELD_SELECTION_FLAGS = null;
    private static final String FIELD_STEREO_MODE = null;
    private static final String FIELD_SUBSAMPLE_OFFSET_US = null;
    private static final String FIELD_TILE_COUNT_HORIZONTAL = null;
    private static final String FIELD_TILE_COUNT_VERTICAL = null;
    private static final String FIELD_WIDTH = null;
    public static final int NO_VALUE = -1;
    public static final long OFFSET_SAMPLE_RELATIVE = Long.MAX_VALUE;
    public final int accessibilityChannel;
    public final int auxiliaryTrackType;
    public final int averageBitrate;
    public final int bitrate;
    public final int channelCount;
    public final String codecs;
    public final ColorInfo colorInfo;
    public final String containerMimeType;
    public final int cryptoType;
    public final int cueReplacementBehavior;
    public final Object customData;
    public final DrmInitData drmInitData;
    public final int encoderDelay;
    public final int encoderPadding;
    public final float frameRate;
    public final boolean hasPrerollSamples;
    private int hashCode;
    public final int height;

    /* JADX INFO: renamed from: id */
    public final String f26id;
    public final List<byte[]> initializationData;
    public final String label;
    public final List<Label> labels;
    public final String language;
    public final int maxInputSize;
    public final int maxNumReorderSamples;
    public final Metadata metadata;
    public final int pcmEncoding;
    public final int peakBitrate;
    public final float pixelWidthHeightRatio;
    public final byte[] projectionData;
    public final int roleFlags;
    public final int rotationDegrees;
    public final String sampleMimeType;
    public final int sampleRate;
    public final int selectionFlags;
    public final int stereoMode;
    public final long subsampleOffsetUs;
    public final int tileCountHorizontal;
    public final int tileCountVertical;
    public final int width;

    /* JADX INFO: renamed from: androidx.media3.common.Format$1 */
    static /* synthetic */ class C01861 {
    }

    public static final class Builder {
        private int accessibilityChannel;
        private int auxiliaryTrackType;
        private int averageBitrate;
        private int channelCount;
        private String codecs;
        private ColorInfo colorInfo;
        private String containerMimeType;
        private int cryptoType;
        private int cueReplacementBehavior;
        private Object customData;
        private DrmInitData drmInitData;
        private int encoderDelay;
        private int encoderPadding;
        private float frameRate;
        private boolean hasPrerollSamples;
        private int height;

        /* JADX INFO: renamed from: id */
        private String f27id;
        private List<byte[]> initializationData;
        private String label;
        private List<Label> labels;
        private String language;
        private int maxInputSize;
        private int maxNumReorderSamples;
        private Metadata metadata;
        private int pcmEncoding;
        private int peakBitrate;
        private float pixelWidthHeightRatio;
        private byte[] projectionData;
        private int roleFlags;
        private int rotationDegrees;
        private String sampleMimeType;
        private int sampleRate;
        private int selectionFlags;
        private int stereoMode;
        private long subsampleOffsetUs;
        private int tileCountHorizontal;
        private int tileCountVertical;
        private int width;

        public Builder() {
            this.labels = ImmutableList.m212of();
            this.averageBitrate = -1;
            this.peakBitrate = -1;
            this.maxInputSize = -1;
            this.maxNumReorderSamples = -1;
            this.subsampleOffsetUs = Long.MAX_VALUE;
            this.width = -1;
            this.height = -1;
            this.frameRate = -1.0f;
            this.pixelWidthHeightRatio = 1.0f;
            this.stereoMode = -1;
            this.channelCount = -1;
            this.sampleRate = -1;
            this.pcmEncoding = -1;
            this.accessibilityChannel = -1;
            this.cueReplacementBehavior = 1;
            this.tileCountHorizontal = -1;
            this.tileCountVertical = -1;
            this.cryptoType = 0;
            this.auxiliaryTrackType = 0;
        }

        private Builder(Format r3) {
            this.f27id = r3.f26id;
            this.label = r3.label;
            this.labels = r3.labels;
            this.language = r3.language;
            this.selectionFlags = r3.selectionFlags;
            this.roleFlags = r3.roleFlags;
            this.averageBitrate = r3.averageBitrate;
            this.peakBitrate = r3.peakBitrate;
            this.codecs = r3.codecs;
            this.metadata = r3.metadata;
            this.customData = r3.customData;
            this.containerMimeType = r3.containerMimeType;
            this.sampleMimeType = r3.sampleMimeType;
            this.maxInputSize = r3.maxInputSize;
            this.maxNumReorderSamples = r3.maxNumReorderSamples;
            this.initializationData = r3.initializationData;
            this.drmInitData = r3.drmInitData;
            this.subsampleOffsetUs = r3.subsampleOffsetUs;
            this.hasPrerollSamples = r3.hasPrerollSamples;
            this.width = r3.width;
            this.height = r3.height;
            this.frameRate = r3.frameRate;
            this.rotationDegrees = r3.rotationDegrees;
            this.pixelWidthHeightRatio = r3.pixelWidthHeightRatio;
            this.projectionData = r3.projectionData;
            this.stereoMode = r3.stereoMode;
            this.colorInfo = r3.colorInfo;
            this.channelCount = r3.channelCount;
            this.sampleRate = r3.sampleRate;
            this.pcmEncoding = r3.pcmEncoding;
            this.encoderDelay = r3.encoderDelay;
            this.encoderPadding = r3.encoderPadding;
            this.accessibilityChannel = r3.accessibilityChannel;
            this.cueReplacementBehavior = r3.cueReplacementBehavior;
            this.tileCountHorizontal = r3.tileCountHorizontal;
            this.tileCountVertical = r3.tileCountVertical;
            this.cryptoType = r3.cryptoType;
        }

        /* synthetic */ Builder(Format r1, C01861 r2) {
            this(r1);
        }

        static /* synthetic */ List access$100(Builder r1) {
            return r1.labels;
        }

        static /* synthetic */ String access$1000(Builder r1) {
            return r1.codecs;
        }

        static /* synthetic */ Metadata access$1100(Builder r1) {
            return r1.metadata;
        }

        static /* synthetic */ Object access$1200(Builder r1) {
            return r1.customData;
        }

        static /* synthetic */ String access$1300(Builder r1) {
            return r1.containerMimeType;
        }

        static /* synthetic */ String access$1400(Builder r1) {
            return r1.sampleMimeType;
        }

        static /* synthetic */ int access$1500(Builder r1) {
            return r1.maxInputSize;
        }

        static /* synthetic */ int access$1600(Builder r1) {
            return r1.maxNumReorderSamples;
        }

        static /* synthetic */ List access$1700(Builder r1) {
            return r1.initializationData;
        }

        static /* synthetic */ DrmInitData access$1800(Builder r1) {
            return r1.drmInitData;
        }

        static /* synthetic */ long access$1900(Builder r2) {
            return r2.subsampleOffsetUs;
        }

        static /* synthetic */ String access$200(Builder r1) {
            return r1.label;
        }

        static /* synthetic */ boolean access$2000(Builder r1) {
            return r1.hasPrerollSamples;
        }

        static /* synthetic */ int access$2100(Builder r1) {
            return r1.width;
        }

        static /* synthetic */ int access$2200(Builder r1) {
            return r1.height;
        }

        static /* synthetic */ float access$2300(Builder r1) {
            return r1.frameRate;
        }

        static /* synthetic */ int access$2400(Builder r1) {
            return r1.rotationDegrees;
        }

        static /* synthetic */ float access$2500(Builder r1) {
            return r1.pixelWidthHeightRatio;
        }

        static /* synthetic */ byte[] access$2600(Builder r1) {
            return r1.projectionData;
        }

        static /* synthetic */ int access$2700(Builder r1) {
            return r1.stereoMode;
        }

        static /* synthetic */ ColorInfo access$2800(Builder r1) {
            return r1.colorInfo;
        }

        static /* synthetic */ int access$2900(Builder r1) {
            return r1.channelCount;
        }

        static /* synthetic */ String access$300(Builder r1) {
            return r1.f27id;
        }

        static /* synthetic */ int access$3000(Builder r1) {
            return r1.sampleRate;
        }

        static /* synthetic */ int access$3100(Builder r1) {
            return r1.pcmEncoding;
        }

        static /* synthetic */ int access$3200(Builder r1) {
            return r1.encoderDelay;
        }

        static /* synthetic */ int access$3300(Builder r1) {
            return r1.encoderPadding;
        }

        static /* synthetic */ int access$3400(Builder r1) {
            return r1.accessibilityChannel;
        }

        static /* synthetic */ int access$3500(Builder r1) {
            return r1.cueReplacementBehavior;
        }

        static /* synthetic */ int access$3600(Builder r1) {
            return r1.tileCountHorizontal;
        }

        static /* synthetic */ int access$3700(Builder r1) {
            return r1.tileCountVertical;
        }

        static /* synthetic */ int access$3800(Builder r1) {
            return r1.cryptoType;
        }

        static /* synthetic */ String access$400(Builder r1) {
            return r1.language;
        }

        static /* synthetic */ int access$500(Builder r1) {
            return r1.selectionFlags;
        }

        static /* synthetic */ int access$600(Builder r1) {
            return r1.auxiliaryTrackType;
        }

        static /* synthetic */ int access$700(Builder r1) {
            return r1.roleFlags;
        }

        static /* synthetic */ int access$800(Builder r1) {
            return r1.averageBitrate;
        }

        static /* synthetic */ int access$900(Builder r1) {
            return r1.peakBitrate;
        }

        public Format build() {
            return new Format(this, null);
        }

        public Builder setAccessibilityChannel(int r1) {
            this.accessibilityChannel = r1;
            return this;
        }

        public Builder setAuxiliaryTrackType(int r1) {
            this.auxiliaryTrackType = r1;
            return this;
        }

        public Builder setAverageBitrate(int r1) {
            this.averageBitrate = r1;
            return this;
        }

        public Builder setChannelCount(int r1) {
            this.channelCount = r1;
            return this;
        }

        public Builder setCodecs(String r1) {
            this.codecs = r1;
            return this;
        }

        public Builder setColorInfo(ColorInfo r1) {
            this.colorInfo = r1;
            return this;
        }

        public Builder setContainerMimeType(String r2) {
            this.containerMimeType = MimeTypes.normalizeMimeType(r2);
            return this;
        }

        public Builder setCryptoType(int r1) {
            this.cryptoType = r1;
            return this;
        }

        public Builder setCueReplacementBehavior(int r1) {
            this.cueReplacementBehavior = r1;
            return this;
        }

        public Builder setCustomData(Object r1) {
            this.customData = r1;
            return this;
        }

        public Builder setDrmInitData(DrmInitData r1) {
            this.drmInitData = r1;
            return this;
        }

        public Builder setEncoderDelay(int r1) {
            this.encoderDelay = r1;
            return this;
        }

        public Builder setEncoderPadding(int r1) {
            this.encoderPadding = r1;
            return this;
        }

        public Builder setFrameRate(float r1) {
            this.frameRate = r1;
            return this;
        }

        public Builder setHasPrerollSamples(boolean r1) {
            this.hasPrerollSamples = r1;
            return this;
        }

        public Builder setHeight(int r1) {
            this.height = r1;
            return this;
        }

        public Builder setId(int r2) {
            this.f27id = Integer.toString(r2);
            return this;
        }

        public Builder setId(String r1) {
            this.f27id = r1;
            return this;
        }

        public Builder setInitializationData(List<byte[]> r1) {
            this.initializationData = r1;
            return this;
        }

        public Builder setLabel(String r1) {
            this.label = r1;
            return this;
        }

        public Builder setLabels(List<Label> r2) {
            this.labels = ImmutableList.copyOf(r2);
            return this;
        }

        public Builder setLanguage(String r1) {
            this.language = r1;
            return this;
        }

        public Builder setMaxInputSize(int r1) {
            this.maxInputSize = r1;
            return this;
        }

        public Builder setMaxNumReorderSamples(int r1) {
            this.maxNumReorderSamples = r1;
            return this;
        }

        public Builder setMetadata(Metadata r1) {
            this.metadata = r1;
            return this;
        }

        public Builder setPcmEncoding(int r1) {
            this.pcmEncoding = r1;
            return this;
        }

        public Builder setPeakBitrate(int r1) {
            this.peakBitrate = r1;
            return this;
        }

        public Builder setPixelWidthHeightRatio(float r1) {
            this.pixelWidthHeightRatio = r1;
            return this;
        }

        public Builder setProjectionData(byte[] r1) {
            this.projectionData = r1;
            return this;
        }

        public Builder setRoleFlags(int r1) {
            this.roleFlags = r1;
            return this;
        }

        public Builder setRotationDegrees(int r1) {
            this.rotationDegrees = r1;
            return this;
        }

        public Builder setSampleMimeType(String r2) {
            this.sampleMimeType = MimeTypes.normalizeMimeType(r2);
            return this;
        }

        public Builder setSampleRate(int r1) {
            this.sampleRate = r1;
            return this;
        }

        public Builder setSelectionFlags(int r1) {
            this.selectionFlags = r1;
            return this;
        }

        public Builder setStereoMode(int r1) {
            this.stereoMode = r1;
            return this;
        }

        public Builder setSubsampleOffsetUs(long r1) {
            this.subsampleOffsetUs = r1;
            return this;
        }

        public Builder setTileCountHorizontal(int r1) {
            this.tileCountHorizontal = r1;
            return this;
        }

        public Builder setTileCountVertical(int r1) {
            this.tileCountVertical = r1;
            return this;
        }

        public Builder setWidth(int r1) {
            this.width = r1;
            return this;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface CueReplacementBehavior {
    }

    static {
        DEFAULT = new Builder().build();
        FIELD_ID = Util.intToStringMaxRadix(0);
        FIELD_LABEL = Util.intToStringMaxRadix(1);
        FIELD_LANGUAGE = Util.intToStringMaxRadix(2);
        FIELD_SELECTION_FLAGS = Util.intToStringMaxRadix(3);
        FIELD_ROLE_FLAGS = Util.intToStringMaxRadix(4);
        FIELD_AVERAGE_BITRATE = Util.intToStringMaxRadix(5);
        FIELD_PEAK_BITRATE = Util.intToStringMaxRadix(6);
        FIELD_CODECS = Util.intToStringMaxRadix(7);
        FIELD_METADATA = Util.intToStringMaxRadix(8);
        FIELD_CONTAINER_MIME_TYPE = Util.intToStringMaxRadix(9);
        FIELD_SAMPLE_MIME_TYPE = Util.intToStringMaxRadix(10);
        FIELD_MAX_INPUT_SIZE = Util.intToStringMaxRadix(11);
        FIELD_INITIALIZATION_DATA = Util.intToStringMaxRadix(12);
        FIELD_DRM_INIT_DATA = Util.intToStringMaxRadix(13);
        FIELD_SUBSAMPLE_OFFSET_US = Util.intToStringMaxRadix(14);
        FIELD_WIDTH = Util.intToStringMaxRadix(15);
        FIELD_HEIGHT = Util.intToStringMaxRadix(16);
        FIELD_FRAME_RATE = Util.intToStringMaxRadix(17);
        FIELD_ROTATION_DEGREES = Util.intToStringMaxRadix(18);
        FIELD_PIXEL_WIDTH_HEIGHT_RATIO = Util.intToStringMaxRadix(19);
        FIELD_PROJECTION_DATA = Util.intToStringMaxRadix(20);
        FIELD_STEREO_MODE = Util.intToStringMaxRadix(21);
        FIELD_COLOR_INFO = Util.intToStringMaxRadix(22);
        FIELD_CHANNEL_COUNT = Util.intToStringMaxRadix(23);
        FIELD_SAMPLE_RATE = Util.intToStringMaxRadix(24);
        FIELD_PCM_ENCODING = Util.intToStringMaxRadix(25);
        FIELD_ENCODER_DELAY = Util.intToStringMaxRadix(26);
        FIELD_ENCODER_PADDING = Util.intToStringMaxRadix(27);
        FIELD_ACCESSIBILITY_CHANNEL = Util.intToStringMaxRadix(28);
        FIELD_CRYPTO_TYPE = Util.intToStringMaxRadix(29);
        FIELD_TILE_COUNT_HORIZONTAL = Util.intToStringMaxRadix(30);
        FIELD_TILE_COUNT_VERTICAL = Util.intToStringMaxRadix(31);
        FIELD_LABELS = Util.intToStringMaxRadix(32);
        FIELD_AUXILIARY_TRACK_TYPE = Util.intToStringMaxRadix(33);
    }

    private Format(Builder r7) {
        this.f26id = Builder.access$300(r7);
        this.language = Util.normalizeLanguageCode(Builder.access$400(r7));
        if (Builder.access$100(r7).isEmpty() == false) goto L8;
        if (Builder.access$200(r7) == null) goto L8;
        this.labels = ImmutableList.m213of(new Label(this.language, Builder.access$200(r7)));
        this.label = Builder.access$200(r7);
    L13:
        this.selectionFlags = Builder.access$500(r7);
        int r2 = 0;
        if (Builder.access$600(r7) != 0) goto L16;
    L19:
        boolean r0 = true;
    L20:
        Assertions.checkState(r0, "Auxiliary track type must only be set to a value other than AUXILIARY_TRACK_TYPE_UNDEFINED only when ROLE_FLAG_AUXILIARY is set");
        this.roleFlags = Builder.access$700(r7);
        this.auxiliaryTrackType = Builder.access$600(r7);
        this.averageBitrate = Builder.access$800(r7);
        this.peakBitrate = Builder.access$900(r7);
        if (this.peakBitrate == (-1)) goto L23;
        int r1 = this.peakBitrate;
    L24:
        this.bitrate = r1;
        this.codecs = Builder.access$1000(r7);
        this.metadata = Builder.access$1100(r7);
        this.customData = Builder.access$1200(r7);
        this.containerMimeType = Builder.access$1300(r7);
        this.sampleMimeType = Builder.access$1400(r7);
        this.maxInputSize = Builder.access$1500(r7);
        this.maxNumReorderSamples = Builder.access$1600(r7);
        if (Builder.access$1700(r7) != null) goto L27;
        List<byte[]> r3 = Collections.emptyList();
    L28:
        this.initializationData = r3;
        this.drmInitData = Builder.access$1800(r7);
        this.subsampleOffsetUs = Builder.access$1900(r7);
        this.hasPrerollSamples = Builder.access$2000(r7);
        this.width = Builder.access$2100(r7);
        this.height = Builder.access$2200(r7);
        this.frameRate = Builder.access$2300(r7);
        if (Builder.access$2400(r7) != (-1)) goto L31;
        int r4 = 0;
    L32:
        this.rotationDegrees = r4;
        if (Builder.access$2500(r7) != (-1.0f)) goto L35;
        float r5 = 1.0f;
    L36:
        this.pixelWidthHeightRatio = r5;
        this.projectionData = Builder.access$2600(r7);
        this.stereoMode = Builder.access$2700(r7);
        this.colorInfo = Builder.access$2800(r7);
        this.channelCount = Builder.access$2900(r7);
        this.sampleRate = Builder.access$3000(r7);
        this.pcmEncoding = Builder.access$3100(r7);
        if (Builder.access$3200(r7) != (-1)) goto L39;
        int r6 = 0;
    L40:
        this.encoderDelay = r6;
        if (Builder.access$3300(r7) == (-1)) goto L44;
        r2 = Builder.access$3300(r7);
    L44:
        this.encoderPadding = r2;
        this.accessibilityChannel = Builder.access$3400(r7);
        this.cueReplacementBehavior = Builder.access$3500(r7);
        this.tileCountHorizontal = Builder.access$3600(r7);
        this.tileCountVertical = Builder.access$3700(r7);
        if (Builder.access$3800(r7) == 0) goto L47;
    L49:
        this.cryptoType = Builder.access$3800(r7);
        return;
    L47:
        if (this.drmInitData == null) goto L49;
        this.cryptoType = 1;
        return;
    L39:
        r6 = Builder.access$3200(r7);
        goto L40
    L35:
        r5 = Builder.access$2500(r7);
        goto L36
    L31:
        r4 = Builder.access$2400(r7);
        goto L32
    L27:
        r3 = Builder.access$1700(r7);
        goto L28
    L23:
        r1 = this.averageBitrate;
        goto L24
    L16:
        if ((Builder.access$700(r7) & 32768) != 0) goto L19;
        r0 = false;
    L8:
        if (Builder.access$100(r7).isEmpty() == false) goto L10;
    L12:
        Assertions.checkState(isLabelPartOfLabels(r7));
        this.labels = Builder.access$100(r7);
        this.label = Builder.access$200(r7);
        goto L13
    L10:
        if (Builder.access$200(r7) != null) goto L12;
        this.labels = Builder.access$100(r7);
        this.label = getDefaultLabel(Builder.access$100(r7), this.language);
        goto L13
    }

    /* synthetic */ Format(Builder r1, C01861 r2) {
        this(r1);
    }

    private static <T> T defaultIfNull(T r1, T r2) {
        if (r1 == null) goto L5;
        return r1;
    L5:
        return r2;
    }

    public static Format fromBundle(Bundle r8) {
        Builder r0 = new Builder();
        BundleCollectionUtil.ensureClassLoader(r8);
        r0.setId((String) defaultIfNull(r8.getString(FIELD_ID), DEFAULT.f26id)).setLabel((String) defaultIfNull(r8.getString(FIELD_LABEL), DEFAULT.label));
        ArrayList r1 = r8.getParcelableArrayList(FIELD_LABELS);
        if (r1 != null) goto L5;
        ImmutableList r2 = ImmutableList.m212of();
    L6:
        r0.setLabels(r2).setLanguage((String) defaultIfNull(r8.getString(FIELD_LANGUAGE), DEFAULT.language)).setSelectionFlags(r8.getInt(FIELD_SELECTION_FLAGS, DEFAULT.selectionFlags)).setRoleFlags(r8.getInt(FIELD_ROLE_FLAGS, DEFAULT.roleFlags)).setAuxiliaryTrackType(r8.getInt(FIELD_AUXILIARY_TRACK_TYPE, DEFAULT.auxiliaryTrackType)).setAverageBitrate(r8.getInt(FIELD_AVERAGE_BITRATE, DEFAULT.averageBitrate)).setPeakBitrate(r8.getInt(FIELD_PEAK_BITRATE, DEFAULT.peakBitrate)).setCodecs((String) defaultIfNull(r8.getString(FIELD_CODECS), DEFAULT.codecs)).setMetadata((Metadata) defaultIfNull((Metadata) r8.getParcelable(FIELD_METADATA), DEFAULT.metadata)).setContainerMimeType((String) defaultIfNull(r8.getString(FIELD_CONTAINER_MIME_TYPE), DEFAULT.containerMimeType)).setSampleMimeType((String) defaultIfNull(r8.getString(FIELD_SAMPLE_MIME_TYPE), DEFAULT.sampleMimeType)).setMaxInputSize(r8.getInt(FIELD_MAX_INPUT_SIZE, DEFAULT.maxInputSize));
        ArrayList r3 = new ArrayList();
        int r4 = 0;
    L7:
        byte[] r5 = r8.getByteArray(keyForInitializationData(r4));
        if (r5 == null) goto L9;
        r3.add(r5);
        r4 = r4 + 1;
        goto L7
    L9:
        r0.setInitializationData(r3).setDrmInitData((DrmInitData) r8.getParcelable(FIELD_DRM_INIT_DATA)).setSubsampleOffsetUs(r8.getLong(FIELD_SUBSAMPLE_OFFSET_US, DEFAULT.subsampleOffsetUs)).setWidth(r8.getInt(FIELD_WIDTH, DEFAULT.width)).setHeight(r8.getInt(FIELD_HEIGHT, DEFAULT.height)).setFrameRate(r8.getFloat(FIELD_FRAME_RATE, DEFAULT.frameRate)).setRotationDegrees(r8.getInt(FIELD_ROTATION_DEGREES, DEFAULT.rotationDegrees)).setPixelWidthHeightRatio(r8.getFloat(FIELD_PIXEL_WIDTH_HEIGHT_RATIO, DEFAULT.pixelWidthHeightRatio)).setProjectionData(r8.getByteArray(FIELD_PROJECTION_DATA)).setStereoMode(r8.getInt(FIELD_STEREO_MODE, DEFAULT.stereoMode));
        Bundle r6 = r8.getBundle(FIELD_COLOR_INFO);
        if (r6 == null) goto L12;
        r0.setColorInfo(ColorInfo.fromBundle(r6));
    L12:
        r0.setChannelCount(r8.getInt(FIELD_CHANNEL_COUNT, DEFAULT.channelCount)).setSampleRate(r8.getInt(FIELD_SAMPLE_RATE, DEFAULT.sampleRate)).setPcmEncoding(r8.getInt(FIELD_PCM_ENCODING, DEFAULT.pcmEncoding)).setEncoderDelay(r8.getInt(FIELD_ENCODER_DELAY, DEFAULT.encoderDelay)).setEncoderPadding(r8.getInt(FIELD_ENCODER_PADDING, DEFAULT.encoderPadding)).setAccessibilityChannel(r8.getInt(FIELD_ACCESSIBILITY_CHANNEL, DEFAULT.accessibilityChannel)).setTileCountHorizontal(r8.getInt(FIELD_TILE_COUNT_HORIZONTAL, DEFAULT.tileCountHorizontal)).setTileCountVertical(r8.getInt(FIELD_TILE_COUNT_VERTICAL, DEFAULT.tileCountVertical)).setCryptoType(r8.getInt(FIELD_CRYPTO_TYPE, DEFAULT.cryptoType));
        return r0.build();
    L5:
        r2 = BundleCollectionUtil.fromBundleList(new Format$$ExternalSyntheticLambda0(), r1);
        goto L6
    }

    private static String getDefaultLabel(List<Label> r3, String r4) {
        Iterator<Label> r0 = r3.iterator();
    L4:
        if (r0.hasNext() == false) goto L11;
        Label r1 = r0.next();
        if (TextUtils.equals(r1.language, r4) == false) goto L4;
        return r1.value;
    L11:
        return r3.get(0).value;
    }

    private static boolean isLabelPartOfLabels(Builder r4) {
        if (Builder.access$100(r4).isEmpty() == true) goto L5;
    L7:
        int r0 = 0;
    L9:
        if (r0 >= Builder.access$100(r4).size()) goto L14;
        if (((Label) Builder.access$100(r4).get(r0)).value.equals(Builder.access$200(r4)) == true) goto L12;
        r0 = r0 + 1;
        goto L9
    L12:
        return true;
    L14:
        return false;
    L5:
        if (Builder.access$200(r4) != null) goto L7;
        return true;
    }

    private static String keyForInitializationData(int r2) {
        return FIELD_INITIALIZATION_DATA + "_" + Integer.toString(r2, 36);
    }

    static /* synthetic */ String lambda$toLogString$0(Label r2) {
        return r2.language + ": " + r2.value;
    }

    public static String toLogString(Format r10) {
        if (r10 != null) goto L5;
        return "null";
    L5:
        Joiner r0 = Joiner.m183on(',');
        StringBuilder r1 = new StringBuilder();
        r1.append("id=").append(r10.f26id).append(", mimeType=").append(r10.sampleMimeType);
        if (r10.containerMimeType == null) goto L9;
        r1.append(", container=").append(r10.containerMimeType);
    L9:
        if (r10.bitrate == (-1)) goto L12;
        r1.append(", bitrate=").append(r10.bitrate);
    L12:
        if (r10.codecs == null) goto L15;
        r1.append(", codecs=").append(r10.codecs);
    L15:
        if (r10.drmInitData == null) goto L38;
        LinkedHashSet r2 = new LinkedHashSet();
        int r4 = 0;
    L18:
        if (r4 >= r10.drmInitData.schemeDataCount) goto L36;
        UUID r5 = r10.drmInitData.get(r4).uuid;
        if (r5.equals(C0180C.COMMON_PSSH_UUID) == false) goto L23;
        r2.add(C0180C.CENC_TYPE_cenc);
    L35:
        r4 = r4 + 1;
        goto L18
    L23:
        if (r5.equals(C0180C.CLEARKEY_UUID) == false) goto L26;
        r2.add("clearkey");
        goto L35
    L26:
        if (r5.equals(C0180C.PLAYREADY_UUID) == false) goto L29;
        r2.add("playready");
        goto L35
    L29:
        if (r5.equals(C0180C.WIDEVINE_UUID) == false) goto L32;
        r2.add("widevine");
        goto L35
    L32:
        if (r5.equals(C0180C.UUID_NIL) == false) goto L34;
        r2.add("universal");
        goto L35
    L34:
        r2.add("unknown (" + r5 + ")");
        goto L35
    L36:
        r1.append(", drm=[");
        r0.appendTo(r1, r2);
        r1.append(']');
    L38:
        if (r10.width == (-1)) goto L43;
        if (r10.height == (-1)) goto L43;
        r1.append(", res=").append(r10.width).append("x").append(r10.height);
    L43:
        if (DoubleMath.fuzzyEquals(r10.pixelWidthHeightRatio, 1.0d, 0.001d) == true) goto L46;
        r1.append(", par=").append(Util.formatInvariant("%.3f", new Object[]{Float.valueOf(r10.pixelWidthHeightRatio)}));
    L46:
        if (r10.colorInfo == null) goto L51;
        if (r10.colorInfo.isValid() == false) goto L51;
        r1.append(", color=").append(r10.colorInfo.toLogString());
    L51:
        if (r10.frameRate == (-1.0f)) goto L54;
        r1.append(", fps=").append(r10.frameRate);
    L54:
        if (r10.channelCount == (-1)) goto L57;
        r1.append(", channels=").append(r10.channelCount);
    L57:
        if (r10.sampleRate == (-1)) goto L60;
        r1.append(", sample_rate=").append(r10.sampleRate);
    L60:
        if (r10.language == null) goto L63;
        r1.append(", language=").append(r10.language);
    L63:
        if (r10.labels.isEmpty() == true) goto L66;
        r1.append(", labels=[");
        r0.appendTo(r1, Lists.transform(r10.labels, new Format$$ExternalSyntheticLambda2()));
        r1.append("]");
    L66:
        if (r10.selectionFlags == 0) goto L69;
        r1.append(", selectionFlags=[");
        r0.appendTo(r1, Util.getSelectionFlagStrings(r10.selectionFlags));
        r1.append("]");
    L69:
        if (r10.roleFlags == 0) goto L72;
        r1.append(", roleFlags=[");
        r0.appendTo(r1, Util.getRoleFlagStrings(r10.roleFlags));
        r1.append("]");
    L72:
        if (r10.customData == null) goto L75;
        r1.append(", customData=").append(r10.customData);
    L75:
        if ((r10.roleFlags & 32768) == 0) goto L78;
        r1.append(", auxiliaryTrackType=").append(Util.getAuxiliaryTrackTypeString(r10.auxiliaryTrackType));
    L78:
        return r1.toString();
    }

    public Builder buildUpon() {
        return new Builder(this, null);
    }

    public Format copyWithCryptoType(int r2) {
        return buildUpon().setCryptoType(r2).build();
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L90:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L90;
        Format r2 = (Format) r8;
        if (this.hashCode == 0) goto L18;
        if (r2.hashCode == 0) goto L18;
        if (this.hashCode == r2.hashCode) goto L18;
        return false;
    L18:
        if (this.selectionFlags != r2.selectionFlags) goto L89;
        if (this.roleFlags != r2.roleFlags) goto L89;
        if (this.auxiliaryTrackType != r2.auxiliaryTrackType) goto L89;
        if (this.averageBitrate != r2.averageBitrate) goto L89;
        if (this.peakBitrate != r2.peakBitrate) goto L89;
        if (this.maxInputSize != r2.maxInputSize) goto L89;
        if (this.subsampleOffsetUs != r2.subsampleOffsetUs) goto L89;
        if (this.width != r2.width) goto L89;
        if (this.height != r2.height) goto L89;
        if (this.rotationDegrees != r2.rotationDegrees) goto L89;
        if (this.stereoMode != r2.stereoMode) goto L89;
        if (this.channelCount != r2.channelCount) goto L89;
        if (this.sampleRate != r2.sampleRate) goto L89;
        if (this.pcmEncoding != r2.pcmEncoding) goto L89;
        if (this.encoderDelay != r2.encoderDelay) goto L89;
        if (this.encoderPadding != r2.encoderPadding) goto L89;
        if (this.accessibilityChannel != r2.accessibilityChannel) goto L89;
        if (this.tileCountHorizontal != r2.tileCountHorizontal) goto L89;
        if (this.tileCountVertical != r2.tileCountVertical) goto L89;
        if (this.cryptoType != r2.cryptoType) goto L89;
        if (Float.compare(this.frameRate, r2.frameRate) != 0) goto L89;
        if (Float.compare(this.pixelWidthHeightRatio, r2.pixelWidthHeightRatio) != 0) goto L89;
        if (Objects.equals(this.f26id, r2.f26id) == false) goto L89;
        if (Objects.equals(this.label, r2.label) == false) goto L89;
        if (this.labels.equals(r2.labels) == false) goto L89;
        if (Objects.equals(this.codecs, r2.codecs) == false) goto L89;
        if (Objects.equals(this.containerMimeType, r2.containerMimeType) == false) goto L89;
        if (Objects.equals(this.sampleMimeType, r2.sampleMimeType) == false) goto L89;
        if (Objects.equals(this.language, r2.language) == false) goto L89;
        if (Arrays.equals(this.projectionData, r2.projectionData) == false) goto L89;
        if (Objects.equals(this.metadata, r2.metadata) == false) goto L89;
        if (Objects.equals(this.colorInfo, r2.colorInfo) == false) goto L89;
        if (Objects.equals(this.drmInitData, r2.drmInitData) == false) goto L89;
        if (initializationDataEquals(r2) == false) goto L89;
        if (Objects.equals(this.customData, r2.customData) == false) goto L89;
        return true;
    L89:
        return false;
    }

    public int getPixelCount() {
        if (this.width != (-1)) goto L5;
        return -1;
    L5:
        if (this.height == (-1)) goto L10;
        int r0 = this.width;
        return this.height * r0;
    L10:
        return -1;
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L38;
        int r1 = 17 * 31;
        int r3 = 0;
        if (this.f26id != null) goto L7;
        int r2 = 0;
    L8:
        int r0 = (r1 + r2) * 31;
        if (this.label != null) goto L11;
        int r4 = 0;
    L12:
        int r5 = (((r0 + r4) * 31) + this.labels.hashCode()) * 31;
        if (this.language != null) goto L15;
        int r6 = 0;
    L16:
        int r7 = (((((((((((r5 + r6) * 31) + this.selectionFlags) * 31) + this.roleFlags) * 31) + this.auxiliaryTrackType) * 31) + this.averageBitrate) * 31) + this.peakBitrate) * 31;
        if (this.codecs != null) goto L19;
        int r8 = 0;
    L20:
        int r9 = (r7 + r8) * 31;
        if (this.metadata != null) goto L23;
        int r10 = 0;
    L24:
        int r11 = (r9 + r10) * 31;
        if (this.customData != null) goto L27;
        int r12 = 0;
    L28:
        int r13 = (r11 + r12) * 31;
        if (this.containerMimeType != null) goto L31;
        int r14 = 0;
    L32:
        int r15 = (r13 + r14) * 31;
        if (this.sampleMimeType == null) goto L36;
        r3 = this.sampleMimeType.hashCode();
    L36:
        this.hashCode = ((((((((((((((((((((((((((((((((((r15 + r3) * 31) + this.maxInputSize) * 31) + ((int) this.subsampleOffsetUs)) * 31) + this.width) * 31) + this.height) * 31) + Float.floatToIntBits(this.frameRate)) * 31) + this.rotationDegrees) * 31) + Float.floatToIntBits(this.pixelWidthHeightRatio)) * 31) + this.stereoMode) * 31) + this.channelCount) * 31) + this.sampleRate) * 31) + this.pcmEncoding) * 31) + this.encoderDelay) * 31) + this.encoderPadding) * 31) + this.accessibilityChannel) * 31) + this.tileCountHorizontal) * 31) + this.tileCountVertical) * 31) + this.cryptoType;
        goto L38
    L31:
        r14 = this.containerMimeType.hashCode();
        goto L32
    L27:
        r12 = this.customData.hashCode();
        goto L28
    L23:
        r10 = this.metadata.hashCode();
        goto L24
    L19:
        r8 = this.codecs.hashCode();
        goto L20
    L15:
        r6 = this.language.hashCode();
        goto L16
    L11:
        r4 = this.label.hashCode();
        goto L12
    L7:
        r2 = this.f26id.hashCode();
    L38:
        return this.hashCode;
    }

    public boolean initializationDataEquals(Format r5) {
        if (this.initializationData.size() == r5.initializationData.size()) goto L5;
        return false;
    L5:
        int r0 = 0;
    L7:
        if (r0 >= this.initializationData.size()) goto L12;
        if (Arrays.equals(this.initializationData.get(r0), r5.initializationData.get(r0)) == false) goto L10;
        r0 = r0 + 1;
        goto L7
    L10:
        return false;
    L12:
        return true;
    }

    @Deprecated
    public Bundle toBundle() {
        return toBundle(false);
    }

    public Bundle toBundle(boolean r5) {
        Bundle r0 = new Bundle();
        r0.putString(FIELD_ID, this.f26id);
        r0.putString(FIELD_LABEL, this.label);
        r0.putParcelableArrayList(FIELD_LABELS, BundleCollectionUtil.toBundleArrayList(this.labels, new Format$$ExternalSyntheticLambda1()));
        r0.putString(FIELD_LANGUAGE, this.language);
        r0.putInt(FIELD_SELECTION_FLAGS, this.selectionFlags);
        r0.putInt(FIELD_ROLE_FLAGS, this.roleFlags);
        if (this.auxiliaryTrackType == DEFAULT.auxiliaryTrackType) goto L5;
        r0.putInt(FIELD_AUXILIARY_TRACK_TYPE, this.auxiliaryTrackType);
    L5:
        r0.putInt(FIELD_AVERAGE_BITRATE, this.averageBitrate);
        r0.putInt(FIELD_PEAK_BITRATE, this.peakBitrate);
        r0.putString(FIELD_CODECS, this.codecs);
        if (r5 == true) goto L8;
        r0.putParcelable(FIELD_METADATA, this.metadata);
    L8:
        r0.putString(FIELD_CONTAINER_MIME_TYPE, this.containerMimeType);
        r0.putString(FIELD_SAMPLE_MIME_TYPE, this.sampleMimeType);
        r0.putInt(FIELD_MAX_INPUT_SIZE, this.maxInputSize);
        int r1 = 0;
    L10:
        if (r1 >= this.initializationData.size()) goto L12;
        r0.putByteArray(keyForInitializationData(r1), this.initializationData.get(r1));
        r1 = r1 + 1;
        goto L10
    L12:
        r0.putParcelable(FIELD_DRM_INIT_DATA, this.drmInitData);
        r0.putLong(FIELD_SUBSAMPLE_OFFSET_US, this.subsampleOffsetUs);
        r0.putInt(FIELD_WIDTH, this.width);
        r0.putInt(FIELD_HEIGHT, this.height);
        r0.putFloat(FIELD_FRAME_RATE, this.frameRate);
        r0.putInt(FIELD_ROTATION_DEGREES, this.rotationDegrees);
        r0.putFloat(FIELD_PIXEL_WIDTH_HEIGHT_RATIO, this.pixelWidthHeightRatio);
        r0.putByteArray(FIELD_PROJECTION_DATA, this.projectionData);
        r0.putInt(FIELD_STEREO_MODE, this.stereoMode);
        if (this.colorInfo == null) goto L15;
        r0.putBundle(FIELD_COLOR_INFO, this.colorInfo.toBundle());
    L15:
        r0.putInt(FIELD_CHANNEL_COUNT, this.channelCount);
        r0.putInt(FIELD_SAMPLE_RATE, this.sampleRate);
        r0.putInt(FIELD_PCM_ENCODING, this.pcmEncoding);
        r0.putInt(FIELD_ENCODER_DELAY, this.encoderDelay);
        r0.putInt(FIELD_ENCODER_PADDING, this.encoderPadding);
        r0.putInt(FIELD_ACCESSIBILITY_CHANNEL, this.accessibilityChannel);
        r0.putInt(FIELD_TILE_COUNT_HORIZONTAL, this.tileCountHorizontal);
        r0.putInt(FIELD_TILE_COUNT_VERTICAL, this.tileCountVertical);
        r0.putInt(FIELD_CRYPTO_TYPE, this.cryptoType);
        return r0;
    }

    public String toString() {
        return "Format(" + this.f26id + ", " + this.label + ", " + this.containerMimeType + ", " + this.sampleMimeType + ", " + this.codecs + ", " + this.bitrate + ", " + this.language + ", [" + this.width + ", " + this.height + ", " + this.frameRate + ", " + this.colorInfo + "], [" + this.channelCount + ", " + this.sampleRate + "])";
    }

    public Format withManifestFormatInfo(Format r18) {
        if (this != r18) goto L5;
        return this;
    L5:
        int r2 = MimeTypes.getTrackType(this.sampleMimeType);
        String r3 = r18.f26id;
        int r4 = r18.tileCountHorizontal;
        int r5 = r18.tileCountVertical;
        if (r18.label == null) goto L8;
        String r6 = r18.label;
    L10:
        if (r18.labels.isEmpty() == true) goto L12;
        List<Label> r7 = r18.labels;
    L13:
        String r8 = this.language;
        if (r2 == 3) goto L17;
        if (r2 == 1) goto L17;
    L20:
        if (this.averageBitrate != (-1)) goto L22;
        int r9 = r18.averageBitrate;
    L24:
        if (this.peakBitrate != (-1)) goto L26;
        int r11 = r18.peakBitrate;
    L27:
        String r12 = this.codecs;
        if (r12 != null) goto L33;
        String r13 = Util.getCodecsOfType(r18.codecs, r2);
        if (Util.splitCodecs(r13).length != 1) goto L33;
        r12 = r13;
    L33:
        if (this.metadata != null) goto L35;
        Metadata r10 = r18.metadata;
    L36:
        float r14 = this.frameRate;
        if (r14 == (-1.0f)) goto L39;
    L41:
        int r15 = this.selectionFlags | r18.selectionFlags;
        int r16 = this.roleFlags;
        int r17 = r18.roleFlags | r16;
        DrmInitData r1 = DrmInitData.createSessionCreationData(r18.drmInitData, this.drmInitData);
        return buildUpon().setId(r3).setLabel(r6).setLabels(r7).setLanguage(r8).setSelectionFlags(r15).setRoleFlags(r17).setAverageBitrate(r9).setPeakBitrate(r11).setCodecs(r12).setMetadata(r10).setDrmInitData(r1).setFrameRate(r14).setTileCountHorizontal(r4).setTileCountVertical(r5).build();
    L39:
        if (r2 != 2) goto L41;
        r14 = r18.frameRate;
        goto L41
    L35:
        r10 = this.metadata.copyWithAppendedEntriesFrom(r18.metadata);
        goto L36
    L26:
        r11 = this.peakBitrate;
        goto L27
    L22:
        r9 = this.averageBitrate;
    L17:
        if (r18.language == null) goto L20;
        r8 = r18.language;
        goto L20
    L12:
        r7 = this.labels;
        goto L13
    L8:
        r6 = this.label;
        goto L10
    }
}
