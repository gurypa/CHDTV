package androidx.media3.exoplayer.trackselection;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.Spatializer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.RendererCapabilities;
import androidx.media3.exoplayer.RendererConfiguration;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.audio.ExecutorC0271x59586989;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class DefaultTrackSelector extends MappingTrackSelector implements RendererCapabilities.Listener {
    private static final String AUDIO_CHANNEL_COUNT_CONSTRAINTS_WARN_MESSAGE = "Audio channel count constraints cannot be applied without reference to Context. Build the track selector instance with one of the non-deprecated constructors that take a Context argument.";
    private static final Ordering<Integer> FORMAT_VALUE_ORDERING = null;
    private static final float FRACTION_TO_CONSIDER_FULLSCREEN = 0.98f;
    protected static final int SELECTION_ELIGIBILITY_ADAPTIVE = 2;
    protected static final int SELECTION_ELIGIBILITY_FIXED = 1;
    protected static final int SELECTION_ELIGIBILITY_NO = 0;
    private static final String TAG = "DefaultTrackSelector";
    private AudioAttributes audioAttributes;
    public final Context context;
    private final boolean deviceIsTV;
    private final Object lock;
    private Parameters parameters;
    private SpatializerWrapperV32 spatializer;
    private final ExoTrackSelection.Factory trackSelectionFactory;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.trackselection.DefaultTrackSelector$1 */
    static /* synthetic */ class C03631 {
    }

    private static final class AudioTrackInfo extends TrackInfo<AudioTrackInfo> implements Comparable<AudioTrackInfo> {
        private final boolean allowMixedMimeTypes;
        private final int bitrate;
        private final int channelCount;
        private final boolean hasMainOrNoRoleFlag;
        private final boolean isDefaultSelectionFlag;
        private final boolean isObjectBasedAudio;
        private final boolean isWithinConstraints;
        private final boolean isWithinRendererCapabilities;
        private final String language;
        private final int localeLanguageMatchIndex;
        private final int localeLanguageScore;
        private final Parameters parameters;
        private final int preferredLanguageIndex;
        private final int preferredLanguageScore;
        private final int preferredMimeTypeMatchIndex;
        private final int preferredRoleFlagsScore;
        private final int sampleRate;
        private final int selectionEligibility;
        private final boolean usesHardwareAcceleration;
        private final boolean usesPrimaryDecoder;

        public AudioTrackInfo(int r17, TrackGroup r18, int r19, Parameters r20, int r21, boolean r22, Predicate<Format> r23, int r24) {
            super(r17, r18, r19);
            this.parameters = r20;
            if (r20.allowAudioNonSeamlessAdaptiveness == false) goto L5;
            int r3 = 24;
        L6:
            boolean r5 = false;
            if (r20.allowAudioMixedMimeTypeAdaptiveness == true) goto L9;
        L11:
            boolean r4 = false;
        L12:
            this.allowMixedMimeTypes = r4;
            this.language = DefaultTrackSelector.normalizeUndeterminedLanguageToNull(this.format.language);
            this.isWithinRendererCapabilities = RendererCapabilities.CC.isFormatSupported(r21, false);
            int r6 = 0;
            int r7 = Integer.MAX_VALUE;
            int r8 = 0;
        L14:
            if (r8 >= r20.preferredAudioLanguages.size()) goto L19;
            int r9 = DefaultTrackSelector.getFormatLanguageScore(this.format, r20.preferredAudioLanguages.get(r8), false);
            if (r9 > 0) goto L17;
            r8 = r8 + 1;
            goto L14
        L17:
            r7 = r8;
            r6 = r9;
        L19:
            this.preferredLanguageIndex = r7;
            this.preferredLanguageScore = r6;
            this.preferredRoleFlagsScore = DefaultTrackSelector.access$4200(this.format.roleFlags, r20.preferredAudioRoleFlags);
            if (this.format.roleFlags != 0) goto L22;
        L25:
            boolean r10 = true;
        L26:
            this.hasMainOrNoRoleFlag = r10;
            if ((this.format.selectionFlags & 1) == 0) goto L29;
            boolean r11 = true;
        L30:
            this.isDefaultSelectionFlag = r11;
            this.isObjectBasedAudio = DefaultTrackSelector.access$4500(this.format);
            this.channelCount = this.format.channelCount;
            this.sampleRate = this.format.sampleRate;
            this.bitrate = this.format.bitrate;
            if (this.format.bitrate == (-1)) goto L37;
            if (this.format.bitrate <= r20.maxAudioBitrate) goto L37;
        L43:
            boolean r12 = false;
        L44:
            this.isWithinConstraints = r12;
            String[] r13 = Util.getSystemLanguageCodes();
            int r14 = Integer.MAX_VALUE;
            int r15 = 0;
            int r16 = 0;
        L46:
            if (r16 >= r13.length) goto L51;
            int r110 = DefaultTrackSelector.getFormatLanguageScore(this.format, r13[r16], false);
            if (r110 > 0) goto L49;
            r16 = r16 + 1;
            goto L46
        L49:
            r14 = r16;
            r15 = r110;
        L51:
            this.localeLanguageMatchIndex = r14;
            this.localeLanguageScore = r15;
            int r111 = Integer.MAX_VALUE;
            int r112 = 0;
        L53:
            if (r112 >= r20.preferredAudioMimeTypes.size()) goto L60;
            if (this.format.sampleMimeType == null) goto L59;
            if (this.format.sampleMimeType.equals(r20.preferredAudioMimeTypes.get(r112)) == false) goto L59;
            r111 = r112;
        L59:
            r112 = r112 + 1;
        L60:
            this.preferredMimeTypeMatchIndex = r111;
            if (RendererCapabilities.CC.getDecoderSupport(r21) != 128) goto L63;
            boolean r113 = true;
        L64:
            this.usesPrimaryDecoder = r113;
            if (RendererCapabilities.CC.getHardwareAccelerationSupport(r21) != 64) goto L67;
            r5 = true;
        L67:
            this.usesHardwareAcceleration = r5;
            this.selectionEligibility = evaluateSelectionEligibility(r21, r22, r3);
            return;
        L63:
            r113 = false;
        L37:
            if (this.format.channelCount == (-1)) goto L41;
            if (this.format.channelCount > r20.maxAudioChannelCount) goto L43;
        L41:
            if (r23.apply(this.format) == false) goto L43;
            r12 = true;
            goto L44
        L29:
            r11 = false;
            goto L30
        L22:
            if ((this.format.roleFlags & 1) != 0) goto L25;
            r10 = false;
            goto L26
        L9:
            if ((r24 & r3) == 0) goto L11;
            r4 = true;
            goto L12
        L5:
            r3 = 16;
            goto L6
        }

        public static int compareSelections(List<AudioTrackInfo> r2, List<AudioTrackInfo> r3) {
            return ((AudioTrackInfo) Collections.max(r2)).compareTo2((AudioTrackInfo) Collections.max(r3));
        }

        public static ImmutableList<AudioTrackInfo> createForTrackGroup(int r11, TrackGroup r12, Parameters r13, int[] r14, boolean r15, Predicate<Format> r16, int r17) {
            ImmutableList.Builder r0 = ImmutableList.builder();
            int r5 = 0;
        L4:
            if (r5 >= r12.length) goto L7;
            r0.add(new AudioTrackInfo(r11, r12, r5, r13, r14[r5], r15, r16, r17));
            r5 = r5 + 1;
            goto L4
        L7:
            return r0.build();
        }

        private int evaluateSelectionEligibility(int r5, boolean r6, int r7) {
            if (RendererCapabilities.CC.isFormatSupported(r5, this.parameters.exceedRendererCapabilitiesIfNecessary) == true) goto L6;
            return 0;
        L6:
            if (this.isWithinConstraints == true) goto L11;
            if (this.parameters.exceedAudioConstraintsIfNecessary == true) goto L11;
            return 0;
        L11:
            if (this.parameters.audioOffloadPreferences.audioOffloadMode != 2) goto L16;
            if (DefaultTrackSelector.access$4600(this.parameters, r5, this.format) == true) goto L16;
            return 0;
        L16:
            if (RendererCapabilities.CC.isFormatSupported(r5, false) == true) goto L18;
        L33:
            return 1;
        L18:
            if (this.isWithinConstraints == false) goto L33;
            if (this.format.bitrate == (-1)) goto L33;
            if (this.parameters.forceHighestSupportedBitrate == true) goto L33;
            if (this.parameters.forceLowestBitrate == true) goto L33;
            if (this.parameters.allowMultipleAdaptiveSelections == true) goto L29;
            if (r6 == true) goto L33;
        L29:
            if (this.parameters.audioOffloadPreferences.audioOffloadMode == 2) goto L33;
            if ((r5 & r7) == 0) goto L33;
            return 2;
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(AudioTrackInfo r6) {
            if (this.isWithinConstraints == true) goto L5;
        L7:
            Ordering r0 = DefaultTrackSelector.access$4400().reverse();
        L8:
            ComparisonChain r1 = ComparisonChain.start().compareFalseFirst(this.isWithinRendererCapabilities, r6.isWithinRendererCapabilities).compare(Integer.valueOf(this.preferredLanguageIndex), Integer.valueOf(r6.preferredLanguageIndex), Ordering.natural().reverse()).compare(this.preferredLanguageScore, r6.preferredLanguageScore).compare(this.preferredRoleFlagsScore, r6.preferredRoleFlagsScore).compareFalseFirst(this.isDefaultSelectionFlag, r6.isDefaultSelectionFlag).compareFalseFirst(this.hasMainOrNoRoleFlag, r6.hasMainOrNoRoleFlag).compare(Integer.valueOf(this.localeLanguageMatchIndex), Integer.valueOf(r6.localeLanguageMatchIndex), Ordering.natural().reverse()).compare(this.localeLanguageScore, r6.localeLanguageScore).compareFalseFirst(this.isWithinConstraints, r6.isWithinConstraints).compare(Integer.valueOf(this.preferredMimeTypeMatchIndex), Integer.valueOf(r6.preferredMimeTypeMatchIndex), Ordering.natural().reverse());
            if (this.parameters.forceLowestBitrate == false) goto L11;
            r1 = r1.compare(Integer.valueOf(this.bitrate), Integer.valueOf(r6.bitrate), DefaultTrackSelector.access$4400().reverse());
        L11:
            ComparisonChain r2 = r1.compareFalseFirst(this.usesPrimaryDecoder, r6.usesPrimaryDecoder).compareFalseFirst(this.usesHardwareAcceleration, r6.usesHardwareAcceleration).compareFalseFirst(this.isObjectBasedAudio, r6.isObjectBasedAudio).compare(Integer.valueOf(this.channelCount), Integer.valueOf(r6.channelCount), r0).compare(Integer.valueOf(this.sampleRate), Integer.valueOf(r6.sampleRate), r0);
            if (Util.areEqual(this.language, r6.language) == false) goto L15;
            r2 = r2.compare(Integer.valueOf(this.bitrate), Integer.valueOf(r6.bitrate), r0);
        L15:
            return r2.result();
        L5:
            if (this.isWithinRendererCapabilities == false) goto L7;
            r0 = DefaultTrackSelector.access$4400();
            goto L8
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(AudioTrackInfo r1) {
            return compareTo2(r1);
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public int getSelectionEligibility() {
            return this.selectionEligibility;
        }

        public boolean isCompatibleForAdaptationWith(AudioTrackInfo r4) {
            if (this.parameters.allowAudioMixedChannelCountAdaptiveness == true) goto L9;
            if (this.format.channelCount != (-1)) goto L7;
        L27:
            return false;
        L7:
            if (this.format.channelCount != r4.format.channelCount) goto L27;
        L9:
            if (this.allowMixedMimeTypes == true) goto L15;
            if (this.format.sampleMimeType == null) goto L27;
            if (TextUtils.equals(this.format.sampleMimeType, r4.format.sampleMimeType) == false) goto L27;
        L15:
            if (this.parameters.allowAudioMixedSampleRateAdaptiveness == true) goto L21;
            if (this.format.sampleRate == (-1)) goto L27;
            if (this.format.sampleRate != r4.format.sampleRate) goto L27;
        L21:
            if (this.parameters.allowAudioMixedDecoderSupportAdaptiveness == false) goto L23;
        L28:
            return true;
        L23:
            if (this.usesPrimaryDecoder != r4.usesPrimaryDecoder) goto L27;
            if (this.usesHardwareAcceleration != r4.usesHardwareAcceleration) goto L27;
            goto L27
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public /* bridge */ /* synthetic */ boolean isCompatibleForAdaptationWith(TrackInfo r1) {
            return isCompatibleForAdaptationWith((AudioTrackInfo) r1);
        }
    }

    private static final class ImageTrackInfo extends TrackInfo<ImageTrackInfo> implements Comparable<ImageTrackInfo> {
        private final int pixelCount;
        private final int selectionEligibility;

        public ImageTrackInfo(int r2, TrackGroup r3, int r4, Parameters r5, int r6) {
            super(r2, r3, r4);
            if (RendererCapabilities.CC.isFormatSupported(r6, r5.exceedRendererCapabilitiesIfNecessary) == false) goto L5;
            int r0 = 1;
        L6:
            this.selectionEligibility = r0;
            this.pixelCount = this.format.getPixelCount();
            return;
        L5:
            r0 = 0;
            goto L6
        }

        public static int compareSelections(List<ImageTrackInfo> r2, List<ImageTrackInfo> r3) {
            return r2.get(0).compareTo2(r3.get(0));
        }

        public static ImmutableList<ImageTrackInfo> createForTrackGroup(int r8, TrackGroup r9, Parameters r10, int[] r11) {
            ImmutableList.Builder r0 = ImmutableList.builder();
            int r5 = 0;
        L4:
            if (r5 >= r9.length) goto L7;
            r0.add(new ImageTrackInfo(r8, r9, r5, r10, r11[r5]));
            r5 = r5 + 1;
            goto L4
        L7:
            return r0.build();
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(ImageTrackInfo r3) {
            return Integer.compare(this.pixelCount, r3.pixelCount);
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(ImageTrackInfo r1) {
            return compareTo2(r1);
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public int getSelectionEligibility() {
            return this.selectionEligibility;
        }

        public boolean isCompatibleForAdaptationWith(ImageTrackInfo r2) {
            return false;
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public /* bridge */ /* synthetic */ boolean isCompatibleForAdaptationWith(TrackInfo r1) {
            return isCompatibleForAdaptationWith((ImageTrackInfo) r1);
        }
    }

    private static final class OtherTrackScore implements Comparable<OtherTrackScore> {
        private final boolean isDefault;
        private final boolean isWithinRendererCapabilities;

        public OtherTrackScore(Format r4, int r5) {
            boolean r1 = true;
            if ((r4.selectionFlags & 1) != 0) goto L6;
            r1 = false;
        L6:
            this.isDefault = r1;
            this.isWithinRendererCapabilities = RendererCapabilities.CC.isFormatSupported(r5, false);
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(OtherTrackScore r4) {
            return ComparisonChain.start().compareFalseFirst(this.isWithinRendererCapabilities, r4.isWithinRendererCapabilities).compareFalseFirst(this.isDefault, r4.isDefault).result();
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(OtherTrackScore r1) {
            return compareTo2(r1);
        }
    }

    public static final class Parameters extends TrackSelectionParameters {

        @Deprecated
        public static final Parameters DEFAULT = null;
        public static final Parameters DEFAULT_WITHOUT_CONTEXT = null;
        private static final String FIELD_ALLOW_AUDIO_MIXED_CHANNEL_COUNT_ADAPTIVENESS = null;
        private static final String FIELD_ALLOW_AUDIO_MIXED_DECODER_SUPPORT_ADAPTIVENESS = null;
        private static final String FIELD_ALLOW_AUDIO_MIXED_MIME_TYPE_ADAPTIVENESS = null;
        private static final String FIELD_ALLOW_AUDIO_MIXED_SAMPLE_RATE_ADAPTIVENESS = null;
        private static final String FIELD_ALLOW_AUDIO_NON_SEAMLESS_ADAPTIVENESS = null;

        /* JADX INFO: renamed from: FIELD_ALLOW_INVALIDATE_SELECTIONS_ON_RENDERER_CAPABILITIES_CHANGE */
        private static final String f48xaec1bc6e = null;
        private static final String FIELD_ALLOW_MULTIPLE_ADAPTIVE_SELECTIONS = null;
        private static final String FIELD_ALLOW_VIDEO_MIXED_DECODER_SUPPORT_ADAPTIVENESS = null;
        private static final String FIELD_ALLOW_VIDEO_MIXED_MIME_TYPE_ADAPTIVENESS = null;
        private static final String FIELD_ALLOW_VIDEO_NON_SEAMLESS_ADAPTIVENESS = null;
        private static final String FIELD_CONSTRAIN_AUDIO_CHANNEL_COUNT_TO_DEVICE_CAPABILITIES = null;
        private static final String FIELD_EXCEED_AUDIO_CONSTRAINTS_IF_NECESSARY = null;
        private static final String FIELD_EXCEED_RENDERER_CAPABILITIES_IF_NECESSARY = null;
        private static final String FIELD_EXCEED_VIDEO_CONSTRAINTS_IF_NECESSARY = null;
        private static final String FIELD_RENDERER_DISABLED_INDICES = null;
        private static final String FIELD_SELECTION_OVERRIDES = null;
        private static final String FIELD_SELECTION_OVERRIDES_RENDERER_INDICES = null;
        private static final String FIELD_SELECTION_OVERRIDES_TRACK_GROUP_ARRAYS = null;
        private static final String FIELD_TUNNELING_ENABLED = null;
        public final boolean allowAudioMixedChannelCountAdaptiveness;
        public final boolean allowAudioMixedDecoderSupportAdaptiveness;
        public final boolean allowAudioMixedMimeTypeAdaptiveness;
        public final boolean allowAudioMixedSampleRateAdaptiveness;
        public final boolean allowAudioNonSeamlessAdaptiveness;
        public final boolean allowInvalidateSelectionsOnRendererCapabilitiesChange;
        public final boolean allowMultipleAdaptiveSelections;
        public final boolean allowVideoMixedDecoderSupportAdaptiveness;
        public final boolean allowVideoMixedMimeTypeAdaptiveness;
        public final boolean allowVideoNonSeamlessAdaptiveness;
        public final boolean constrainAudioChannelCountToDeviceCapabilities;
        public final boolean exceedAudioConstraintsIfNecessary;
        public final boolean exceedRendererCapabilitiesIfNecessary;
        public final boolean exceedVideoConstraintsIfNecessary;
        private final SparseBooleanArray rendererDisabledFlags;
        private final SparseArray<Map<TrackGroupArray, SelectionOverride>> selectionOverrides;
        public final boolean tunnelingEnabled;

        public static final class Builder extends TrackSelectionParameters.Builder {
            private boolean allowAudioMixedChannelCountAdaptiveness;
            private boolean allowAudioMixedDecoderSupportAdaptiveness;
            private boolean allowAudioMixedMimeTypeAdaptiveness;
            private boolean allowAudioMixedSampleRateAdaptiveness;
            private boolean allowAudioNonSeamlessAdaptiveness;
            private boolean allowInvalidateSelectionsOnRendererCapabilitiesChange;
            private boolean allowMultipleAdaptiveSelections;
            private boolean allowVideoMixedDecoderSupportAdaptiveness;
            private boolean allowVideoMixedMimeTypeAdaptiveness;
            private boolean allowVideoNonSeamlessAdaptiveness;
            private boolean constrainAudioChannelCountToDeviceCapabilities;
            private boolean exceedAudioConstraintsIfNecessary;
            private boolean exceedRendererCapabilitiesIfNecessary;
            private boolean exceedVideoConstraintsIfNecessary;
            private final SparseBooleanArray rendererDisabledFlags;
            private final SparseArray<Map<TrackGroupArray, SelectionOverride>> selectionOverrides;
            private boolean tunnelingEnabled;

            @Deprecated
            public Builder() {
                this.selectionOverrides = new SparseArray();
                this.rendererDisabledFlags = new SparseBooleanArray();
                init();
            }

            public Builder(Context r2) {
                super(r2);
                this.selectionOverrides = new SparseArray();
                this.rendererDisabledFlags = new SparseBooleanArray();
                init();
            }

            private Builder(Bundle r4) {
                super(r4);
                init();
                Parameters r0 = Parameters.DEFAULT_WITHOUT_CONTEXT;
                setExceedVideoConstraintsIfNecessary(r4.getBoolean(Parameters.access$200(), r0.exceedVideoConstraintsIfNecessary));
                setAllowVideoMixedMimeTypeAdaptiveness(r4.getBoolean(Parameters.access$300(), r0.allowVideoMixedMimeTypeAdaptiveness));
                setAllowVideoNonSeamlessAdaptiveness(r4.getBoolean(Parameters.access$400(), r0.allowVideoNonSeamlessAdaptiveness));
                setAllowVideoMixedDecoderSupportAdaptiveness(r4.getBoolean(Parameters.access$500(), r0.allowVideoMixedDecoderSupportAdaptiveness));
                setExceedAudioConstraintsIfNecessary(r4.getBoolean(Parameters.access$600(), r0.exceedAudioConstraintsIfNecessary));
                setAllowAudioMixedMimeTypeAdaptiveness(r4.getBoolean(Parameters.access$700(), r0.allowAudioMixedMimeTypeAdaptiveness));
                setAllowAudioMixedSampleRateAdaptiveness(r4.getBoolean(Parameters.access$800(), r0.allowAudioMixedSampleRateAdaptiveness));
                setAllowAudioMixedChannelCountAdaptiveness(r4.getBoolean(Parameters.access$900(), r0.allowAudioMixedChannelCountAdaptiveness));
                setAllowAudioMixedDecoderSupportAdaptiveness(r4.getBoolean(Parameters.access$1000(), r0.allowAudioMixedDecoderSupportAdaptiveness));
                setAllowAudioNonSeamlessAdaptiveness(r4.getBoolean(Parameters.access$1100(), r0.allowAudioNonSeamlessAdaptiveness));
                setConstrainAudioChannelCountToDeviceCapabilities(r4.getBoolean(Parameters.access$1200(), r0.constrainAudioChannelCountToDeviceCapabilities));
                setExceedRendererCapabilitiesIfNecessary(r4.getBoolean(Parameters.access$1300(), r0.exceedRendererCapabilitiesIfNecessary));
                setTunnelingEnabled(r4.getBoolean(Parameters.access$1400(), r0.tunnelingEnabled));
                setAllowMultipleAdaptiveSelections(r4.getBoolean(Parameters.access$1500(), r0.allowMultipleAdaptiveSelections));
                setAllowInvalidateSelectionsOnRendererCapabilitiesChange(r4.getBoolean(Parameters.access$1600(), r0.allowInvalidateSelectionsOnRendererCapabilitiesChange));
                this.selectionOverrides = new SparseArray();
                setSelectionOverridesFromBundle(r4);
                this.rendererDisabledFlags = makeSparseBooleanArrayFromTrueKeys(r4.getIntArray(Parameters.access$1700()));
            }

            /* synthetic */ Builder(Bundle r1, C03631 r2) {
                this(r1);
            }

            private Builder(Parameters r2) {
                super(r2);
                this.exceedVideoConstraintsIfNecessary = r2.exceedVideoConstraintsIfNecessary;
                this.allowVideoMixedMimeTypeAdaptiveness = r2.allowVideoMixedMimeTypeAdaptiveness;
                this.allowVideoNonSeamlessAdaptiveness = r2.allowVideoNonSeamlessAdaptiveness;
                this.allowVideoMixedDecoderSupportAdaptiveness = r2.allowVideoMixedDecoderSupportAdaptiveness;
                this.exceedAudioConstraintsIfNecessary = r2.exceedAudioConstraintsIfNecessary;
                this.allowAudioMixedMimeTypeAdaptiveness = r2.allowAudioMixedMimeTypeAdaptiveness;
                this.allowAudioMixedSampleRateAdaptiveness = r2.allowAudioMixedSampleRateAdaptiveness;
                this.allowAudioMixedChannelCountAdaptiveness = r2.allowAudioMixedChannelCountAdaptiveness;
                this.allowAudioMixedDecoderSupportAdaptiveness = r2.allowAudioMixedDecoderSupportAdaptiveness;
                this.allowAudioNonSeamlessAdaptiveness = r2.allowAudioNonSeamlessAdaptiveness;
                this.constrainAudioChannelCountToDeviceCapabilities = r2.constrainAudioChannelCountToDeviceCapabilities;
                this.exceedRendererCapabilitiesIfNecessary = r2.exceedRendererCapabilitiesIfNecessary;
                this.tunnelingEnabled = r2.tunnelingEnabled;
                this.allowMultipleAdaptiveSelections = r2.allowMultipleAdaptiveSelections;
                this.allowInvalidateSelectionsOnRendererCapabilitiesChange = r2.allowInvalidateSelectionsOnRendererCapabilitiesChange;
                this.selectionOverrides = cloneSelectionOverrides(Parameters.access$000(r2));
                this.rendererDisabledFlags = Parameters.access$100(r2).clone();
            }

            /* synthetic */ Builder(Parameters r1, C03631 r2) {
                this(r1);
            }

            static /* synthetic */ boolean access$2200(Builder r1) {
                return r1.exceedVideoConstraintsIfNecessary;
            }

            static /* synthetic */ boolean access$2300(Builder r1) {
                return r1.allowVideoMixedMimeTypeAdaptiveness;
            }

            static /* synthetic */ boolean access$2400(Builder r1) {
                return r1.allowVideoNonSeamlessAdaptiveness;
            }

            static /* synthetic */ boolean access$2500(Builder r1) {
                return r1.allowVideoMixedDecoderSupportAdaptiveness;
            }

            static /* synthetic */ boolean access$2600(Builder r1) {
                return r1.exceedAudioConstraintsIfNecessary;
            }

            static /* synthetic */ boolean access$2700(Builder r1) {
                return r1.allowAudioMixedMimeTypeAdaptiveness;
            }

            static /* synthetic */ boolean access$2800(Builder r1) {
                return r1.allowAudioMixedSampleRateAdaptiveness;
            }

            static /* synthetic */ boolean access$2900(Builder r1) {
                return r1.allowAudioMixedChannelCountAdaptiveness;
            }

            static /* synthetic */ boolean access$3000(Builder r1) {
                return r1.allowAudioMixedDecoderSupportAdaptiveness;
            }

            static /* synthetic */ boolean access$3100(Builder r1) {
                return r1.allowAudioNonSeamlessAdaptiveness;
            }

            static /* synthetic */ boolean access$3200(Builder r1) {
                return r1.constrainAudioChannelCountToDeviceCapabilities;
            }

            static /* synthetic */ boolean access$3300(Builder r1) {
                return r1.exceedRendererCapabilitiesIfNecessary;
            }

            static /* synthetic */ boolean access$3400(Builder r1) {
                return r1.tunnelingEnabled;
            }

            static /* synthetic */ boolean access$3500(Builder r1) {
                return r1.allowMultipleAdaptiveSelections;
            }

            static /* synthetic */ boolean access$3600(Builder r1) {
                return r1.allowInvalidateSelectionsOnRendererCapabilitiesChange;
            }

            static /* synthetic */ SparseArray access$3700(Builder r1) {
                return r1.selectionOverrides;
            }

            static /* synthetic */ SparseBooleanArray access$3800(Builder r1) {
                return r1.rendererDisabledFlags;
            }

            private static SparseArray<Map<TrackGroupArray, SelectionOverride>> cloneSelectionOverrides(SparseArray<Map<TrackGroupArray, SelectionOverride>> r5) {
                SparseArray<Map<TrackGroupArray, SelectionOverride>> r0 = new SparseArray();
                int r1 = 0;
            L4:
                if (r1 >= r5.size()) goto L6;
                r0.put(r5.keyAt(r1), new HashMap(r5.valueAt(r1)));
                r1 = r1 + 1;
                goto L4
            L6:
                return r0;
            }

            private void init() {
                this.exceedVideoConstraintsIfNecessary = true;
                this.allowVideoMixedMimeTypeAdaptiveness = false;
                this.allowVideoNonSeamlessAdaptiveness = true;
                this.allowVideoMixedDecoderSupportAdaptiveness = false;
                this.exceedAudioConstraintsIfNecessary = true;
                this.allowAudioMixedMimeTypeAdaptiveness = false;
                this.allowAudioMixedSampleRateAdaptiveness = false;
                this.allowAudioMixedChannelCountAdaptiveness = false;
                this.allowAudioMixedDecoderSupportAdaptiveness = false;
                this.allowAudioNonSeamlessAdaptiveness = true;
                this.constrainAudioChannelCountToDeviceCapabilities = true;
                this.exceedRendererCapabilitiesIfNecessary = true;
                this.tunnelingEnabled = false;
                this.allowMultipleAdaptiveSelections = true;
                this.allowInvalidateSelectionsOnRendererCapabilitiesChange = false;
            }

            private SparseBooleanArray makeSparseBooleanArrayFromTrueKeys(int[] r6) {
                if (r6 == null) goto L4;
                SparseBooleanArray r0 = new SparseBooleanArray(r6.length);
                int r1 = r6.length;
                int r2 = 0;
            L6:
                if (r2 >= r1) goto L8;
                r0.append(r6[r2], true);
                r2 = r2 + 1;
                goto L6
            L8:
                return r0;
            L4:
                return new SparseBooleanArray();
            }

            /* JADX WARN: Multi-variable type inference failed */
            private void setSelectionOverridesFromBundle(Bundle r10) {
                int[] r0 = r10.getIntArray(Parameters.access$1900());
                ArrayList r1 = r10.getParcelableArrayList(Parameters.access$2000());
                if (r1 != null) goto L5;
                ImmutableList r2 = ImmutableList.m212of();
            L6:
                SparseArray r3 = r10.getSparseParcelableArray(Parameters.access$2100());
                if (r3 != null) goto L9;
                SparseArray r4 = new SparseArray();
            L11:
                if (r0 != null) goto L13;
                return;
            L13:
                if (r0.length != r2.size()) goto L22;
                int r5 = 0;
            L17:
                if (r5 >= r0.length) goto L19;
                setSelectionOverride(r0[r5], (TrackGroupArray) r2.get(r5), (SelectionOverride) r4.get(r5));
                r5 = r5 + 1;
                goto L17
            L19:
                return;
            L22:
                return;
            L9:
                r4 = BundleCollectionUtil.fromBundleSparseArray(new C0365xedc2178c(), r3);
                goto L11
            L5:
                r2 = BundleCollectionUtil.fromBundleList(new C0364xedc2178b(), r1);
                goto L6
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder addOverride(TrackSelectionOverride r1) {
                return addOverride(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder addOverride(TrackSelectionOverride r1) {
                super.addOverride(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters build() {
                return build();
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Parameters build() {
                return new Parameters(this, null);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearOverride(TrackGroup r1) {
                return clearOverride(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder clearOverride(TrackGroup r1) {
                super.clearOverride(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearOverrides() {
                return clearOverrides();
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder clearOverrides() {
                super.clearOverrides();
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearOverridesOfType(int r1) {
                return clearOverridesOfType(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder clearOverridesOfType(int r1) {
                super.clearOverridesOfType(r1);
                return this;
            }

            @Deprecated
            public Builder clearSelectionOverride(int r3, TrackGroupArray r4) {
                Map<TrackGroupArray, SelectionOverride> r0 = this.selectionOverrides.get(r3);
                if (r0 != null) goto L5;
            L11:
                return this;
            L5:
                if (r0.containsKey(r4) == false) goto L11;
                r0.remove(r4);
                if (r0.isEmpty() == false) goto L10;
                this.selectionOverrides.remove(r3);
            L10:
                return this;
            }

            @Deprecated
            public Builder clearSelectionOverrides() {
                if (this.selectionOverrides.size() != 0) goto L5;
                return this;
            L5:
                this.selectionOverrides.clear();
                return this;
            }

            @Deprecated
            public Builder clearSelectionOverrides(int r3) {
                Map<TrackGroupArray, SelectionOverride> r0 = this.selectionOverrides.get(r3);
                if (r0 != null) goto L5;
            L9:
                return this;
            L5:
                if (r0.isEmpty() == true) goto L9;
                this.selectionOverrides.remove(r3);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearVideoSizeConstraints() {
                return clearVideoSizeConstraints();
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder clearVideoSizeConstraints() {
                super.clearVideoSizeConstraints();
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearViewportSizeConstraints() {
                return clearViewportSizeConstraints();
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder clearViewportSizeConstraints() {
                super.clearViewportSizeConstraints();
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            protected /* bridge */ /* synthetic */ TrackSelectionParameters.Builder set(TrackSelectionParameters r1) {
                return set(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            protected Builder set(TrackSelectionParameters r1) {
                super.set(r1);
                return this;
            }

            public Builder setAllowAudioMixedChannelCountAdaptiveness(boolean r1) {
                this.allowAudioMixedChannelCountAdaptiveness = r1;
                return this;
            }

            public Builder setAllowAudioMixedDecoderSupportAdaptiveness(boolean r1) {
                this.allowAudioMixedDecoderSupportAdaptiveness = r1;
                return this;
            }

            public Builder setAllowAudioMixedMimeTypeAdaptiveness(boolean r1) {
                this.allowAudioMixedMimeTypeAdaptiveness = r1;
                return this;
            }

            public Builder setAllowAudioMixedSampleRateAdaptiveness(boolean r1) {
                this.allowAudioMixedSampleRateAdaptiveness = r1;
                return this;
            }

            public Builder setAllowAudioNonSeamlessAdaptiveness(boolean r1) {
                this.allowAudioNonSeamlessAdaptiveness = r1;
                return this;
            }

            public Builder setAllowInvalidateSelectionsOnRendererCapabilitiesChange(boolean r1) {
                this.allowInvalidateSelectionsOnRendererCapabilitiesChange = r1;
                return this;
            }

            public Builder setAllowMultipleAdaptiveSelections(boolean r1) {
                this.allowMultipleAdaptiveSelections = r1;
                return this;
            }

            public Builder setAllowVideoMixedDecoderSupportAdaptiveness(boolean r1) {
                this.allowVideoMixedDecoderSupportAdaptiveness = r1;
                return this;
            }

            public Builder setAllowVideoMixedMimeTypeAdaptiveness(boolean r1) {
                this.allowVideoMixedMimeTypeAdaptiveness = r1;
                return this;
            }

            public Builder setAllowVideoNonSeamlessAdaptiveness(boolean r1) {
                this.allowVideoNonSeamlessAdaptiveness = r1;
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setAudioOffloadPreferences(TrackSelectionParameters.AudioOffloadPreferences r1) {
                return setAudioOffloadPreferences(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setAudioOffloadPreferences(TrackSelectionParameters.AudioOffloadPreferences r1) {
                super.setAudioOffloadPreferences(r1);
                return this;
            }

            public Builder setConstrainAudioChannelCountToDeviceCapabilities(boolean r1) {
                this.constrainAudioChannelCountToDeviceCapabilities = r1;
                return this;
            }

            @Deprecated
            public Builder setDisabledTextTrackSelectionFlags(int r2) {
                return setIgnoredTextSelectionFlags(r2);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            @Deprecated
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setDisabledTrackTypes(Set r1) {
                return setDisabledTrackTypes(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            @Deprecated
            public Builder setDisabledTrackTypes(Set<Integer> r1) {
                super.setDisabledTrackTypes(r1);
                return this;
            }

            public Builder setExceedAudioConstraintsIfNecessary(boolean r1) {
                this.exceedAudioConstraintsIfNecessary = r1;
                return this;
            }

            public Builder setExceedRendererCapabilitiesIfNecessary(boolean r1) {
                this.exceedRendererCapabilitiesIfNecessary = r1;
                return this;
            }

            public Builder setExceedVideoConstraintsIfNecessary(boolean r1) {
                this.exceedVideoConstraintsIfNecessary = r1;
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setForceHighestSupportedBitrate(boolean r1) {
                return setForceHighestSupportedBitrate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setForceHighestSupportedBitrate(boolean r1) {
                super.setForceHighestSupportedBitrate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setForceLowestBitrate(boolean r1) {
                return setForceLowestBitrate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setForceLowestBitrate(boolean r1) {
                super.setForceLowestBitrate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setIgnoredTextSelectionFlags(int r1) {
                return setIgnoredTextSelectionFlags(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setIgnoredTextSelectionFlags(int r1) {
                super.setIgnoredTextSelectionFlags(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxAudioBitrate(int r1) {
                return setMaxAudioBitrate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMaxAudioBitrate(int r1) {
                super.setMaxAudioBitrate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxAudioChannelCount(int r1) {
                return setMaxAudioChannelCount(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMaxAudioChannelCount(int r1) {
                super.setMaxAudioChannelCount(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoBitrate(int r1) {
                return setMaxVideoBitrate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMaxVideoBitrate(int r1) {
                super.setMaxVideoBitrate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoFrameRate(int r1) {
                return setMaxVideoFrameRate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMaxVideoFrameRate(int r1) {
                super.setMaxVideoFrameRate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoSize(int r1, int r2) {
                return setMaxVideoSize(r1, r2);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMaxVideoSize(int r1, int r2) {
                super.setMaxVideoSize(r1, r2);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoSizeSd() {
                return setMaxVideoSizeSd();
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMaxVideoSizeSd() {
                super.setMaxVideoSizeSd();
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMinVideoBitrate(int r1) {
                return setMinVideoBitrate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMinVideoBitrate(int r1) {
                super.setMinVideoBitrate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMinVideoFrameRate(int r1) {
                return setMinVideoFrameRate(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMinVideoFrameRate(int r1) {
                super.setMinVideoFrameRate(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMinVideoSize(int r1, int r2) {
                return setMinVideoSize(r1, r2);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setMinVideoSize(int r1, int r2) {
                super.setMinVideoSize(r1, r2);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setOverrideForType(TrackSelectionOverride r1) {
                return setOverrideForType(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setOverrideForType(TrackSelectionOverride r1) {
                super.setOverrideForType(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioLanguage(String r1) {
                return setPreferredAudioLanguage(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredAudioLanguage(String r1) {
                super.setPreferredAudioLanguage(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioLanguages(String[] r1) {
                return setPreferredAudioLanguages(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredAudioLanguages(String... r1) {
                super.setPreferredAudioLanguages(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioMimeType(String r1) {
                return setPreferredAudioMimeType(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredAudioMimeType(String r1) {
                super.setPreferredAudioMimeType(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioMimeTypes(String[] r1) {
                return setPreferredAudioMimeTypes(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredAudioMimeTypes(String... r1) {
                super.setPreferredAudioMimeTypes(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioRoleFlags(int r1) {
                return setPreferredAudioRoleFlags(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredAudioRoleFlags(int r1) {
                super.setPreferredAudioRoleFlags(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextLanguage(String r1) {
                return setPreferredTextLanguage(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredTextLanguage(String r1) {
                super.setPreferredTextLanguage(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(Context r1) {
                return setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(Context r1) {
                super.setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextLanguages(String[] r1) {
                return setPreferredTextLanguages(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredTextLanguages(String... r1) {
                super.setPreferredTextLanguages(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextRoleFlags(int r1) {
                return setPreferredTextRoleFlags(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredTextRoleFlags(int r1) {
                super.setPreferredTextRoleFlags(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredVideoMimeType(String r1) {
                return setPreferredVideoMimeType(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredVideoMimeType(String r1) {
                super.setPreferredVideoMimeType(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredVideoMimeTypes(String[] r1) {
                return setPreferredVideoMimeTypes(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredVideoMimeTypes(String... r1) {
                super.setPreferredVideoMimeTypes(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredVideoRoleFlags(int r1) {
                return setPreferredVideoRoleFlags(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPreferredVideoRoleFlags(int r1) {
                super.setPreferredVideoRoleFlags(r1);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPrioritizeImageOverVideoEnabled(boolean r1) {
                return setPrioritizeImageOverVideoEnabled(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setPrioritizeImageOverVideoEnabled(boolean r1) {
                super.setPrioritizeImageOverVideoEnabled(r1);
                return this;
            }

            public Builder setRendererDisabled(int r3, boolean r4) {
                if (this.rendererDisabledFlags.get(r3) != r4) goto L5;
                return this;
            L5:
                if (r4 == false) goto L7;
                this.rendererDisabledFlags.put(r3, true);
            L8:
                return this;
            L7:
                this.rendererDisabledFlags.delete(r3);
                goto L8
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setSelectUndeterminedTextLanguage(boolean r1) {
                return setSelectUndeterminedTextLanguage(r1);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setSelectUndeterminedTextLanguage(boolean r1) {
                super.setSelectUndeterminedTextLanguage(r1);
                return this;
            }

            @Deprecated
            public Builder setSelectionOverride(int r3, TrackGroupArray r4, SelectionOverride r5) {
                Map<TrackGroupArray, SelectionOverride> r0 = this.selectionOverrides.get(r3);
                if (r0 != null) goto L6;
                r0 = new HashMap();
                this.selectionOverrides.put(r3, r0);
            L6:
                if (r0.containsKey(r4) == true) goto L8;
            L10:
                r0.put(r4, r5);
                return this;
            L8:
                if (Util.areEqual(r0.get(r4), r5) == false) goto L10;
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setTrackTypeDisabled(int r1, boolean r2) {
                return setTrackTypeDisabled(r1, r2);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setTrackTypeDisabled(int r1, boolean r2) {
                super.setTrackTypeDisabled(r1, r2);
                return this;
            }

            public Builder setTunnelingEnabled(boolean r1) {
                this.tunnelingEnabled = r1;
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setViewportSize(int r1, int r2, boolean r3) {
                return setViewportSize(r1, r2, r3);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setViewportSize(int r1, int r2, boolean r3) {
                super.setViewportSize(r1, r2, r3);
                return this;
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setViewportSizeToPhysicalDisplaySize(Context r1, boolean r2) {
                return setViewportSizeToPhysicalDisplaySize(r1, r2);
            }

            @Override // androidx.media3.common.TrackSelectionParameters.Builder
            public Builder setViewportSizeToPhysicalDisplaySize(Context r1, boolean r2) {
                super.setViewportSizeToPhysicalDisplaySize(r1, r2);
                return this;
            }
        }

        static {
            DEFAULT_WITHOUT_CONTEXT = new Builder().build();
            DEFAULT = DEFAULT_WITHOUT_CONTEXT;
            FIELD_EXCEED_VIDEO_CONSTRAINTS_IF_NECESSARY = Util.intToStringMaxRadix(1000);
            FIELD_ALLOW_VIDEO_MIXED_MIME_TYPE_ADAPTIVENESS = Util.intToStringMaxRadix(1001);
            FIELD_ALLOW_VIDEO_NON_SEAMLESS_ADAPTIVENESS = Util.intToStringMaxRadix(1002);
            FIELD_EXCEED_AUDIO_CONSTRAINTS_IF_NECESSARY = Util.intToStringMaxRadix(1003);
            FIELD_ALLOW_AUDIO_MIXED_MIME_TYPE_ADAPTIVENESS = Util.intToStringMaxRadix(1004);
            FIELD_ALLOW_AUDIO_MIXED_SAMPLE_RATE_ADAPTIVENESS = Util.intToStringMaxRadix(AnalyticsListener.EVENT_UPSTREAM_DISCARDED);
            FIELD_ALLOW_AUDIO_MIXED_CHANNEL_COUNT_ADAPTIVENESS = Util.intToStringMaxRadix(1006);
            FIELD_EXCEED_RENDERER_CAPABILITIES_IF_NECESSARY = Util.intToStringMaxRadix(1007);
            FIELD_TUNNELING_ENABLED = Util.intToStringMaxRadix(1008);
            FIELD_ALLOW_MULTIPLE_ADAPTIVE_SELECTIONS = Util.intToStringMaxRadix(1009);
            FIELD_SELECTION_OVERRIDES_RENDERER_INDICES = Util.intToStringMaxRadix(1010);
            FIELD_SELECTION_OVERRIDES_TRACK_GROUP_ARRAYS = Util.intToStringMaxRadix(1011);
            FIELD_SELECTION_OVERRIDES = Util.intToStringMaxRadix(1012);
            FIELD_RENDERER_DISABLED_INDICES = Util.intToStringMaxRadix(1013);
            FIELD_ALLOW_VIDEO_MIXED_DECODER_SUPPORT_ADAPTIVENESS = Util.intToStringMaxRadix(1014);
            FIELD_ALLOW_AUDIO_MIXED_DECODER_SUPPORT_ADAPTIVENESS = Util.intToStringMaxRadix(1015);
            FIELD_CONSTRAIN_AUDIO_CHANNEL_COUNT_TO_DEVICE_CAPABILITIES = Util.intToStringMaxRadix(1016);
            f48xaec1bc6e = Util.intToStringMaxRadix(1017);
            FIELD_ALLOW_AUDIO_NON_SEAMLESS_ADAPTIVENESS = Util.intToStringMaxRadix(1018);
        }

        private Parameters(Builder r2) {
            super(r2);
            this.exceedVideoConstraintsIfNecessary = Builder.access$2200(r2);
            this.allowVideoMixedMimeTypeAdaptiveness = Builder.access$2300(r2);
            this.allowVideoNonSeamlessAdaptiveness = Builder.access$2400(r2);
            this.allowVideoMixedDecoderSupportAdaptiveness = Builder.access$2500(r2);
            this.exceedAudioConstraintsIfNecessary = Builder.access$2600(r2);
            this.allowAudioMixedMimeTypeAdaptiveness = Builder.access$2700(r2);
            this.allowAudioMixedSampleRateAdaptiveness = Builder.access$2800(r2);
            this.allowAudioMixedChannelCountAdaptiveness = Builder.access$2900(r2);
            this.allowAudioMixedDecoderSupportAdaptiveness = Builder.access$3000(r2);
            this.allowAudioNonSeamlessAdaptiveness = Builder.access$3100(r2);
            this.constrainAudioChannelCountToDeviceCapabilities = Builder.access$3200(r2);
            this.exceedRendererCapabilitiesIfNecessary = Builder.access$3300(r2);
            this.tunnelingEnabled = Builder.access$3400(r2);
            this.allowMultipleAdaptiveSelections = Builder.access$3500(r2);
            this.allowInvalidateSelectionsOnRendererCapabilitiesChange = Builder.access$3600(r2);
            this.selectionOverrides = Builder.access$3700(r2);
            this.rendererDisabledFlags = Builder.access$3800(r2);
        }

        /* synthetic */ Parameters(Builder r1, C03631 r2) {
            this(r1);
        }

        static /* synthetic */ SparseArray access$000(Parameters r1) {
            return r1.selectionOverrides;
        }

        static /* synthetic */ SparseBooleanArray access$100(Parameters r1) {
            return r1.rendererDisabledFlags;
        }

        static /* synthetic */ String access$1000() {
            return FIELD_ALLOW_AUDIO_MIXED_DECODER_SUPPORT_ADAPTIVENESS;
        }

        static /* synthetic */ String access$1100() {
            return FIELD_ALLOW_AUDIO_NON_SEAMLESS_ADAPTIVENESS;
        }

        static /* synthetic */ String access$1200() {
            return FIELD_CONSTRAIN_AUDIO_CHANNEL_COUNT_TO_DEVICE_CAPABILITIES;
        }

        static /* synthetic */ String access$1300() {
            return FIELD_EXCEED_RENDERER_CAPABILITIES_IF_NECESSARY;
        }

        static /* synthetic */ String access$1400() {
            return FIELD_TUNNELING_ENABLED;
        }

        static /* synthetic */ String access$1500() {
            return FIELD_ALLOW_MULTIPLE_ADAPTIVE_SELECTIONS;
        }

        static /* synthetic */ String access$1600() {
            return f48xaec1bc6e;
        }

        static /* synthetic */ String access$1700() {
            return FIELD_RENDERER_DISABLED_INDICES;
        }

        static /* synthetic */ String access$1900() {
            return FIELD_SELECTION_OVERRIDES_RENDERER_INDICES;
        }

        static /* synthetic */ String access$200() {
            return FIELD_EXCEED_VIDEO_CONSTRAINTS_IF_NECESSARY;
        }

        static /* synthetic */ String access$2000() {
            return FIELD_SELECTION_OVERRIDES_TRACK_GROUP_ARRAYS;
        }

        static /* synthetic */ String access$2100() {
            return FIELD_SELECTION_OVERRIDES;
        }

        static /* synthetic */ String access$300() {
            return FIELD_ALLOW_VIDEO_MIXED_MIME_TYPE_ADAPTIVENESS;
        }

        static /* synthetic */ String access$400() {
            return FIELD_ALLOW_VIDEO_NON_SEAMLESS_ADAPTIVENESS;
        }

        static /* synthetic */ String access$500() {
            return FIELD_ALLOW_VIDEO_MIXED_DECODER_SUPPORT_ADAPTIVENESS;
        }

        static /* synthetic */ String access$600() {
            return FIELD_EXCEED_AUDIO_CONSTRAINTS_IF_NECESSARY;
        }

        static /* synthetic */ String access$700() {
            return FIELD_ALLOW_AUDIO_MIXED_MIME_TYPE_ADAPTIVENESS;
        }

        static /* synthetic */ String access$800() {
            return FIELD_ALLOW_AUDIO_MIXED_SAMPLE_RATE_ADAPTIVENESS;
        }

        static /* synthetic */ String access$900() {
            return FIELD_ALLOW_AUDIO_MIXED_CHANNEL_COUNT_ADAPTIVENESS;
        }

        private static boolean areRendererDisabledFlagsEqual(SparseBooleanArray r4, SparseBooleanArray r5) {
            int r0 = r4.size();
            if (r5.size() == r0) goto L5;
            return false;
        L5:
            int r1 = 0;
        L6:
            if (r1 >= r0) goto L11;
            if (r5.indexOfKey(r4.keyAt(r1)) < 0) goto L9;
            r1 = r1 + 1;
            goto L6
        L9:
            return false;
        L11:
            return true;
        }

        private static boolean areSelectionOverridesEqual(SparseArray<Map<TrackGroupArray, SelectionOverride>> r6, SparseArray<Map<TrackGroupArray, SelectionOverride>> r7) {
            int r0 = r6.size();
            if (r7.size() == r0) goto L5;
            return false;
        L5:
            int r1 = 0;
        L6:
            if (r1 >= r0) goto L14;
            int r3 = r7.indexOfKey(r6.keyAt(r1));
            if (r3 < 0) goto L13;
            if (areSelectionOverridesEqual(r6.valueAt(r1), r7.valueAt(r3)) == false) goto L13;
            r1 = r1 + 1;
        L13:
            return false;
        L14:
            return true;
        }

        private static boolean areSelectionOverridesEqual(Map<TrackGroupArray, SelectionOverride> r7, Map<TrackGroupArray, SelectionOverride> r8) {
            int r0 = r7.size();
            if (r8.size() == r0) goto L5;
            return false;
        L5:
            Iterator<Map.Entry<TrackGroupArray, SelectionOverride>> r1 = r7.entrySet().iterator();
        L7:
            if (r1.hasNext() == false) goto L15;
            Map.Entry<TrackGroupArray, SelectionOverride> r3 = r1.next();
            TrackGroupArray r4 = r3.getKey();
            if (r8.containsKey(r4) == false) goto L14;
            if (Util.areEqual(r3.getValue(), r8.get(r4)) == true) goto L7;
        L14:
            return false;
        L15:
            return true;
        }

        public static Parameters fromBundle(Bundle r2) {
            return new Builder(r2, null).build();
        }

        public static Parameters getDefaults(Context r1) {
            return new Builder(r1).build();
        }

        private static int[] getKeysFromSparseBooleanArray(SparseBooleanArray r3) {
            int[] r0 = new int[r3.size()];
            int r1 = 0;
        L4:
            if (r1 >= r3.size()) goto L6;
            r0[r1] = r3.keyAt(r1);
            r1 = r1 + 1;
            goto L4
        L6:
            return r0;
        }

        private static void putSelectionOverridesToBundle(Bundle r9, SparseArray<Map<TrackGroupArray, SelectionOverride>> r10) {
            ArrayList r0 = new ArrayList();
            ArrayList r1 = new ArrayList();
            SparseArray r2 = new SparseArray();
            int r3 = 0;
        L4:
            if (r3 >= r10.size()) goto L13;
            int r4 = r10.keyAt(r3);
            Iterator<Map.Entry<TrackGroupArray, SelectionOverride>> r5 = r10.valueAt(r3).entrySet().iterator();
        L7:
            if (r5.hasNext() == false) goto L12;
            Map.Entry<TrackGroupArray, SelectionOverride> r6 = r5.next();
            SelectionOverride r7 = r6.getValue();
            if (r7 == null) goto L11;
            r2.put(r1.size(), r7);
        L11:
            r1.add(r6.getKey());
            r0.add(Integer.valueOf(r4));
            goto L7
        L12:
            r9.putIntArray(FIELD_SELECTION_OVERRIDES_RENDERER_INDICES, Ints.toArray(r0));
            r9.putParcelableArrayList(FIELD_SELECTION_OVERRIDES_TRACK_GROUP_ARRAYS, BundleCollectionUtil.toBundleArrayList(r1, new DefaultTrackSelector$Parameters$$ExternalSyntheticLambda0()));
            r9.putSparseParcelableArray(FIELD_SELECTION_OVERRIDES, BundleCollectionUtil.toBundleSparseArray(r2, new DefaultTrackSelector$Parameters$$ExternalSyntheticLambda1()));
            r3 = r3 + 1;
            goto L4
        }

        @Override // androidx.media3.common.TrackSelectionParameters
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder buildUpon() {
            return buildUpon();
        }

        @Override // androidx.media3.common.TrackSelectionParameters
        public Builder buildUpon() {
            return new Builder(this, null);
        }

        @Override // androidx.media3.common.TrackSelectionParameters
        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L49:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L49;
            Parameters r2 = (Parameters) r6;
            if (super.equals(r2) == false) goto L48;
            if (this.exceedVideoConstraintsIfNecessary != r2.exceedVideoConstraintsIfNecessary) goto L48;
            if (this.allowVideoMixedMimeTypeAdaptiveness != r2.allowVideoMixedMimeTypeAdaptiveness) goto L48;
            if (this.allowVideoNonSeamlessAdaptiveness != r2.allowVideoNonSeamlessAdaptiveness) goto L48;
            if (this.allowVideoMixedDecoderSupportAdaptiveness != r2.allowVideoMixedDecoderSupportAdaptiveness) goto L48;
            if (this.exceedAudioConstraintsIfNecessary != r2.exceedAudioConstraintsIfNecessary) goto L48;
            if (this.allowAudioMixedMimeTypeAdaptiveness != r2.allowAudioMixedMimeTypeAdaptiveness) goto L48;
            if (this.allowAudioMixedSampleRateAdaptiveness != r2.allowAudioMixedSampleRateAdaptiveness) goto L48;
            if (this.allowAudioMixedChannelCountAdaptiveness != r2.allowAudioMixedChannelCountAdaptiveness) goto L48;
            if (this.allowAudioMixedDecoderSupportAdaptiveness != r2.allowAudioMixedDecoderSupportAdaptiveness) goto L48;
            if (this.allowAudioNonSeamlessAdaptiveness != r2.allowAudioNonSeamlessAdaptiveness) goto L48;
            if (this.constrainAudioChannelCountToDeviceCapabilities != r2.constrainAudioChannelCountToDeviceCapabilities) goto L48;
            if (this.exceedRendererCapabilitiesIfNecessary != r2.exceedRendererCapabilitiesIfNecessary) goto L48;
            if (this.tunnelingEnabled != r2.tunnelingEnabled) goto L48;
            if (this.allowMultipleAdaptiveSelections != r2.allowMultipleAdaptiveSelections) goto L48;
            if (this.allowInvalidateSelectionsOnRendererCapabilitiesChange != r2.allowInvalidateSelectionsOnRendererCapabilitiesChange) goto L48;
            if (areRendererDisabledFlagsEqual(this.rendererDisabledFlags, r2.rendererDisabledFlags) == false) goto L48;
            if (areSelectionOverridesEqual(this.selectionOverrides, r2.selectionOverrides) == false) goto L48;
            return true;
        L48:
            return false;
        }

        public boolean getRendererDisabled(int r2) {
            return this.rendererDisabledFlags.get(r2);
        }

        @Deprecated
        public SelectionOverride getSelectionOverride(int r3, TrackGroupArray r4) {
            Map<TrackGroupArray, SelectionOverride> r0 = this.selectionOverrides.get(r3);
            if (r0 != null) goto L7;
            return null;
        L7:
            return r0.get(r4);
        }

        @Deprecated
        public boolean hasSelectionOverride(int r3, TrackGroupArray r4) {
            Map<TrackGroupArray, SelectionOverride> r0 = this.selectionOverrides.get(r3);
            if (r0 != null) goto L5;
        L7:
            return false;
        L5:
            if (r0.containsKey(r4) == false) goto L7;
            return true;
        }

        @Override // androidx.media3.common.TrackSelectionParameters
        public int hashCode() {
            return (((((((((((((((((((((((((((((((1 * 31) + super.hashCode()) * 31) + (this.exceedVideoConstraintsIfNecessary ? 1 : 0)) * 31) + (this.allowVideoMixedMimeTypeAdaptiveness ? 1 : 0)) * 31) + (this.allowVideoNonSeamlessAdaptiveness ? 1 : 0)) * 31) + (this.allowVideoMixedDecoderSupportAdaptiveness ? 1 : 0)) * 31) + (this.exceedAudioConstraintsIfNecessary ? 1 : 0)) * 31) + (this.allowAudioMixedMimeTypeAdaptiveness ? 1 : 0)) * 31) + (this.allowAudioMixedSampleRateAdaptiveness ? 1 : 0)) * 31) + (this.allowAudioMixedChannelCountAdaptiveness ? 1 : 0)) * 31) + (this.allowAudioMixedDecoderSupportAdaptiveness ? 1 : 0)) * 31) + (this.allowAudioNonSeamlessAdaptiveness ? 1 : 0)) * 31) + (this.constrainAudioChannelCountToDeviceCapabilities ? 1 : 0)) * 31) + (this.exceedRendererCapabilitiesIfNecessary ? 1 : 0)) * 31) + (this.tunnelingEnabled ? 1 : 0)) * 31) + (this.allowMultipleAdaptiveSelections ? 1 : 0)) * 31) + (this.allowInvalidateSelectionsOnRendererCapabilitiesChange ? 1 : 0);
        }

        @Override // androidx.media3.common.TrackSelectionParameters
        public Bundle toBundle() {
            Bundle r0 = super.toBundle();
            r0.putBoolean(FIELD_EXCEED_VIDEO_CONSTRAINTS_IF_NECESSARY, this.exceedVideoConstraintsIfNecessary);
            r0.putBoolean(FIELD_ALLOW_VIDEO_MIXED_MIME_TYPE_ADAPTIVENESS, this.allowVideoMixedMimeTypeAdaptiveness);
            r0.putBoolean(FIELD_ALLOW_VIDEO_NON_SEAMLESS_ADAPTIVENESS, this.allowVideoNonSeamlessAdaptiveness);
            r0.putBoolean(FIELD_ALLOW_VIDEO_MIXED_DECODER_SUPPORT_ADAPTIVENESS, this.allowVideoMixedDecoderSupportAdaptiveness);
            r0.putBoolean(FIELD_EXCEED_AUDIO_CONSTRAINTS_IF_NECESSARY, this.exceedAudioConstraintsIfNecessary);
            r0.putBoolean(FIELD_ALLOW_AUDIO_MIXED_MIME_TYPE_ADAPTIVENESS, this.allowAudioMixedMimeTypeAdaptiveness);
            r0.putBoolean(FIELD_ALLOW_AUDIO_MIXED_SAMPLE_RATE_ADAPTIVENESS, this.allowAudioMixedSampleRateAdaptiveness);
            r0.putBoolean(FIELD_ALLOW_AUDIO_MIXED_CHANNEL_COUNT_ADAPTIVENESS, this.allowAudioMixedChannelCountAdaptiveness);
            r0.putBoolean(FIELD_ALLOW_AUDIO_MIXED_DECODER_SUPPORT_ADAPTIVENESS, this.allowAudioMixedDecoderSupportAdaptiveness);
            r0.putBoolean(FIELD_ALLOW_AUDIO_NON_SEAMLESS_ADAPTIVENESS, this.allowAudioNonSeamlessAdaptiveness);
            r0.putBoolean(FIELD_CONSTRAIN_AUDIO_CHANNEL_COUNT_TO_DEVICE_CAPABILITIES, this.constrainAudioChannelCountToDeviceCapabilities);
            r0.putBoolean(FIELD_EXCEED_RENDERER_CAPABILITIES_IF_NECESSARY, this.exceedRendererCapabilitiesIfNecessary);
            r0.putBoolean(FIELD_TUNNELING_ENABLED, this.tunnelingEnabled);
            r0.putBoolean(FIELD_ALLOW_MULTIPLE_ADAPTIVE_SELECTIONS, this.allowMultipleAdaptiveSelections);
            r0.putBoolean(f48xaec1bc6e, this.allowInvalidateSelectionsOnRendererCapabilitiesChange);
            putSelectionOverridesToBundle(r0, this.selectionOverrides);
            r0.putIntArray(FIELD_RENDERER_DISABLED_INDICES, getKeysFromSparseBooleanArray(this.rendererDisabledFlags));
            return r0;
        }
    }

    @Deprecated
    public static final class ParametersBuilder extends TrackSelectionParameters.Builder {
        private final Parameters.Builder delegate;

        @Deprecated
        public ParametersBuilder() {
            this.delegate = new Parameters.Builder();
        }

        public ParametersBuilder(Context r2) {
            this.delegate = new Parameters.Builder(r2);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder addOverride(TrackSelectionOverride r1) {
            return addOverride(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder addOverride(TrackSelectionOverride r2) {
            this.delegate.addOverride(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters build() {
            return build();
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public Parameters build() {
            return this.delegate.build();
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearOverride(TrackGroup r1) {
            return clearOverride(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder clearOverride(TrackGroup r2) {
            this.delegate.clearOverride(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearOverrides() {
            return clearOverrides();
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder clearOverrides() {
            this.delegate.clearOverrides();
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearOverridesOfType(int r1) {
            return clearOverridesOfType(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder clearOverridesOfType(int r2) {
            this.delegate.clearOverridesOfType(r2);
            return this;
        }

        @Deprecated
        public ParametersBuilder clearSelectionOverride(int r2, TrackGroupArray r3) {
            this.delegate.clearSelectionOverride(r2, r3);
            return this;
        }

        @Deprecated
        public ParametersBuilder clearSelectionOverrides() {
            this.delegate.clearSelectionOverrides();
            return this;
        }

        @Deprecated
        public ParametersBuilder clearSelectionOverrides(int r2) {
            this.delegate.clearSelectionOverrides(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearVideoSizeConstraints() {
            return clearVideoSizeConstraints();
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder clearVideoSizeConstraints() {
            this.delegate.clearVideoSizeConstraints();
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder clearViewportSizeConstraints() {
            return clearViewportSizeConstraints();
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder clearViewportSizeConstraints() {
            this.delegate.clearViewportSizeConstraints();
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        protected /* bridge */ /* synthetic */ TrackSelectionParameters.Builder set(TrackSelectionParameters r1) {
            return set(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        protected ParametersBuilder set(TrackSelectionParameters r2) {
            this.delegate.set(r2);
            return this;
        }

        public ParametersBuilder setAllowAudioMixedChannelCountAdaptiveness(boolean r2) {
            this.delegate.setAllowAudioMixedChannelCountAdaptiveness(r2);
            return this;
        }

        public ParametersBuilder setAllowAudioMixedDecoderSupportAdaptiveness(boolean r2) {
            this.delegate.setAllowAudioMixedDecoderSupportAdaptiveness(r2);
            return this;
        }

        public ParametersBuilder setAllowAudioMixedMimeTypeAdaptiveness(boolean r2) {
            this.delegate.setAllowAudioMixedMimeTypeAdaptiveness(r2);
            return this;
        }

        public ParametersBuilder setAllowAudioMixedSampleRateAdaptiveness(boolean r2) {
            this.delegate.setAllowAudioMixedSampleRateAdaptiveness(r2);
            return this;
        }

        public ParametersBuilder setAllowMultipleAdaptiveSelections(boolean r2) {
            this.delegate.setAllowMultipleAdaptiveSelections(r2);
            return this;
        }

        public ParametersBuilder setAllowVideoMixedDecoderSupportAdaptiveness(boolean r2) {
            this.delegate.setAllowVideoMixedDecoderSupportAdaptiveness(r2);
            return this;
        }

        public ParametersBuilder setAllowVideoMixedMimeTypeAdaptiveness(boolean r2) {
            this.delegate.setAllowVideoMixedMimeTypeAdaptiveness(r2);
            return this;
        }

        public ParametersBuilder setAllowVideoNonSeamlessAdaptiveness(boolean r2) {
            this.delegate.setAllowVideoNonSeamlessAdaptiveness(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setAudioOffloadPreferences(TrackSelectionParameters.AudioOffloadPreferences r1) {
            return setAudioOffloadPreferences(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setAudioOffloadPreferences(TrackSelectionParameters.AudioOffloadPreferences r2) {
            this.delegate.setAudioOffloadPreferences(r2);
            return this;
        }

        @Deprecated
        public ParametersBuilder setDisabledTextTrackSelectionFlags(int r2) {
            this.delegate.setDisabledTextTrackSelectionFlags(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        @Deprecated
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setDisabledTrackTypes(Set r1) {
            return setDisabledTrackTypes(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        @Deprecated
        public ParametersBuilder setDisabledTrackTypes(Set<Integer> r2) {
            this.delegate.setDisabledTrackTypes(r2);
            return this;
        }

        public ParametersBuilder setExceedAudioConstraintsIfNecessary(boolean r2) {
            this.delegate.setExceedAudioConstraintsIfNecessary(r2);
            return this;
        }

        public ParametersBuilder setExceedRendererCapabilitiesIfNecessary(boolean r2) {
            this.delegate.setExceedRendererCapabilitiesIfNecessary(r2);
            return this;
        }

        public ParametersBuilder setExceedVideoConstraintsIfNecessary(boolean r2) {
            this.delegate.setExceedVideoConstraintsIfNecessary(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setForceHighestSupportedBitrate(boolean r1) {
            return setForceHighestSupportedBitrate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setForceHighestSupportedBitrate(boolean r2) {
            this.delegate.setForceHighestSupportedBitrate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setForceLowestBitrate(boolean r1) {
            return setForceLowestBitrate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setForceLowestBitrate(boolean r2) {
            this.delegate.setForceLowestBitrate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setIgnoredTextSelectionFlags(int r1) {
            return setIgnoredTextSelectionFlags(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setIgnoredTextSelectionFlags(int r2) {
            this.delegate.setIgnoredTextSelectionFlags(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxAudioBitrate(int r1) {
            return setMaxAudioBitrate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMaxAudioBitrate(int r2) {
            this.delegate.setMaxAudioBitrate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxAudioChannelCount(int r1) {
            return setMaxAudioChannelCount(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMaxAudioChannelCount(int r2) {
            this.delegate.setMaxAudioChannelCount(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoBitrate(int r1) {
            return setMaxVideoBitrate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMaxVideoBitrate(int r2) {
            this.delegate.setMaxVideoBitrate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoFrameRate(int r1) {
            return setMaxVideoFrameRate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMaxVideoFrameRate(int r2) {
            this.delegate.setMaxVideoFrameRate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoSize(int r1, int r2) {
            return setMaxVideoSize(r1, r2);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMaxVideoSize(int r2, int r3) {
            this.delegate.setMaxVideoSize(r2, r3);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMaxVideoSizeSd() {
            return setMaxVideoSizeSd();
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMaxVideoSizeSd() {
            this.delegate.setMaxVideoSizeSd();
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMinVideoBitrate(int r1) {
            return setMinVideoBitrate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMinVideoBitrate(int r2) {
            this.delegate.setMinVideoBitrate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMinVideoFrameRate(int r1) {
            return setMinVideoFrameRate(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMinVideoFrameRate(int r2) {
            this.delegate.setMinVideoFrameRate(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setMinVideoSize(int r1, int r2) {
            return setMinVideoSize(r1, r2);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setMinVideoSize(int r2, int r3) {
            this.delegate.setMinVideoSize(r2, r3);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setOverrideForType(TrackSelectionOverride r1) {
            return setOverrideForType(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setOverrideForType(TrackSelectionOverride r2) {
            this.delegate.setOverrideForType(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioLanguage(String r1) {
            return setPreferredAudioLanguage(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredAudioLanguage(String r2) {
            this.delegate.setPreferredAudioLanguage(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioLanguages(String[] r1) {
            return setPreferredAudioLanguages(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredAudioLanguages(String... r2) {
            this.delegate.setPreferredAudioLanguages(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioMimeType(String r1) {
            return setPreferredAudioMimeType(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredAudioMimeType(String r2) {
            this.delegate.setPreferredAudioMimeType(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioMimeTypes(String[] r1) {
            return setPreferredAudioMimeTypes(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredAudioMimeTypes(String... r2) {
            this.delegate.setPreferredAudioMimeTypes(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredAudioRoleFlags(int r1) {
            return setPreferredAudioRoleFlags(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredAudioRoleFlags(int r2) {
            this.delegate.setPreferredAudioRoleFlags(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextLanguage(String r1) {
            return setPreferredTextLanguage(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredTextLanguage(String r2) {
            this.delegate.setPreferredTextLanguage(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(Context r1) {
            return setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(Context r2) {
            this.delegate.setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextLanguages(String[] r1) {
            return setPreferredTextLanguages(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredTextLanguages(String... r2) {
            this.delegate.setPreferredTextLanguages(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredTextRoleFlags(int r1) {
            return setPreferredTextRoleFlags(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredTextRoleFlags(int r2) {
            this.delegate.setPreferredTextRoleFlags(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredVideoMimeType(String r1) {
            return setPreferredVideoMimeType(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredVideoMimeType(String r2) {
            this.delegate.setPreferredVideoMimeType(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredVideoMimeTypes(String[] r1) {
            return setPreferredVideoMimeTypes(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredVideoMimeTypes(String... r2) {
            this.delegate.setPreferredVideoMimeTypes(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPreferredVideoRoleFlags(int r1) {
            return setPreferredVideoRoleFlags(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPreferredVideoRoleFlags(int r2) {
            this.delegate.setPreferredVideoRoleFlags(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setPrioritizeImageOverVideoEnabled(boolean r1) {
            return setPrioritizeImageOverVideoEnabled(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setPrioritizeImageOverVideoEnabled(boolean r2) {
            this.delegate.setPrioritizeImageOverVideoEnabled(r2);
            return this;
        }

        public ParametersBuilder setRendererDisabled(int r2, boolean r3) {
            this.delegate.setRendererDisabled(r2, r3);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setSelectUndeterminedTextLanguage(boolean r1) {
            return setSelectUndeterminedTextLanguage(r1);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setSelectUndeterminedTextLanguage(boolean r2) {
            this.delegate.setSelectUndeterminedTextLanguage(r2);
            return this;
        }

        @Deprecated
        public ParametersBuilder setSelectionOverride(int r2, TrackGroupArray r3, SelectionOverride r4) {
            this.delegate.setSelectionOverride(r2, r3, r4);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setTrackTypeDisabled(int r1, boolean r2) {
            return setTrackTypeDisabled(r1, r2);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setTrackTypeDisabled(int r2, boolean r3) {
            this.delegate.setTrackTypeDisabled(r2, r3);
            return this;
        }

        public ParametersBuilder setTunnelingEnabled(boolean r2) {
            this.delegate.setTunnelingEnabled(r2);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setViewportSize(int r1, int r2, boolean r3) {
            return setViewportSize(r1, r2, r3);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setViewportSize(int r2, int r3, boolean r4) {
            this.delegate.setViewportSize(r2, r3, r4);
            return this;
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public /* bridge */ /* synthetic */ TrackSelectionParameters.Builder setViewportSizeToPhysicalDisplaySize(Context r1, boolean r2) {
            return setViewportSizeToPhysicalDisplaySize(r1, r2);
        }

        @Override // androidx.media3.common.TrackSelectionParameters.Builder
        public ParametersBuilder setViewportSizeToPhysicalDisplaySize(Context r2, boolean r3) {
            this.delegate.setViewportSizeToPhysicalDisplaySize(r2, r3);
            return this;
        }
    }

    public static final class SelectionOverride {
        private static final String FIELD_GROUP_INDEX = null;
        private static final String FIELD_TRACKS = null;
        private static final String FIELD_TRACK_TYPE = null;
        public final int groupIndex;
        public final int length;
        public final int[] tracks;
        public final int type;

        static {
            FIELD_GROUP_INDEX = Util.intToStringMaxRadix(0);
            FIELD_TRACKS = Util.intToStringMaxRadix(1);
            FIELD_TRACK_TYPE = Util.intToStringMaxRadix(2);
        }

        public SelectionOverride(int r2, int... r3) {
            this(r2, r3, 0);
        }

        public SelectionOverride(int r2, int[] r3, int r4) {
            this.groupIndex = r2;
            this.tracks = Arrays.copyOf(r3, r3.length);
            this.length = r3.length;
            this.type = r4;
            Arrays.sort(this.tracks);
        }

        public static SelectionOverride fromBundle(Bundle r4) {
            int r0 = r4.getInt(FIELD_GROUP_INDEX, -1);
            int[] r2 = r4.getIntArray(FIELD_TRACKS);
            int r1 = r4.getInt(FIELD_TRACK_TYPE, -1);
            if (r0 < 0) goto L6;
            if (r1 < 0) goto L6;
            boolean r3 = true;
        L7:
            Assertions.checkArgument(r3);
            Assertions.checkNotNull(r2);
            return new SelectionOverride(r0, r2, r1);
        L6:
            r3 = false;
            goto L7
        }

        public boolean containsTrack(int r6) {
            int[] r0 = this.tracks;
            int r1 = r0.length;
            int r3 = 0;
        L3:
            if (r3 >= r1) goto L9;
            if (r0[r3] == r6) goto L6;
            r3 = r3 + 1;
            goto L3
        L6:
            return true;
        L9:
            return false;
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L19:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L19;
            SelectionOverride r2 = (SelectionOverride) r6;
            if (this.groupIndex != r2.groupIndex) goto L18;
            if (Arrays.equals(this.tracks, r2.tracks) == false) goto L18;
            if (this.type != r2.type) goto L18;
            return true;
        L18:
            return false;
        }

        public int hashCode() {
            return (((this.groupIndex * 31) + Arrays.hashCode(this.tracks)) * 31) + this.type;
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putInt(FIELD_GROUP_INDEX, this.groupIndex);
            r0.putIntArray(FIELD_TRACKS, this.tracks);
            r0.putInt(FIELD_TRACK_TYPE, this.type);
            return r0;
        }
    }

    private static class SpatializerWrapperV32 {
        private Handler handler;
        private Spatializer.OnSpatializerStateChangedListener listener;
        private final boolean spatializationSupported;
        private final Spatializer spatializer;

        private SpatializerWrapperV32(Spatializer r2) {
            this.spatializer = r2;
            if (r2.getImmersiveAudioLevel() == 0) goto L5;
            boolean r0 = true;
        L6:
            this.spatializationSupported = r0;
            return;
        L5:
            r0 = false;
            goto L6
        }

        public static SpatializerWrapperV32 tryCreateInstance(Context r3) {
            AudioManager r0 = (AudioManager) r3.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
            if (r0 != null) goto L6;
            return null;
        L6:
            return new SpatializerWrapperV32(r0.getSpatializer());
        }

        public boolean canBeSpatialized(AudioAttributes r7, Format r8) {
            if (Objects.equals(r8.sampleMimeType, MimeTypes.AUDIO_E_AC3_JOC) == false) goto L10;
            if (r8.channelCount != 16) goto L7;
            int r0 = 12;
        L27:
            int r2 = Util.getAudioTrackChannelConfig(r0);
            if (r2 != 0) goto L31;
            return false;
        L31:
            AudioFormat.Builder r3 = new AudioFormat.Builder().setEncoding(2).setChannelMask(r2);
            if (r8.sampleRate == (-1)) goto L35;
            r3.setSampleRate(r8.sampleRate);
        L35:
            return this.spatializer.canBeSpatialized(r7.getAudioAttributesV21().audioAttributes, r3.build());
        L7:
            r0 = r8.channelCount;
            goto L27
        L10:
            if (Objects.equals(r8.sampleMimeType, MimeTypes.AUDIO_IAMF) == false) goto L17;
            if (r8.channelCount != (-1)) goto L14;
            r0 = 6;
            goto L27
        L14:
            r0 = r8.channelCount;
            goto L27
        L17:
            if (Objects.equals(r8.sampleMimeType, MimeTypes.AUDIO_AC4) == true) goto L19;
            r0 = r8.channelCount;
            goto L27
        L19:
            if (r8.channelCount != 18) goto L21;
        L24:
            r0 = 24;
            goto L27
        L21:
            if (r8.channelCount == 21) goto L24;
            r0 = r8.channelCount;
            goto L27
        }

        public void ensureInitialized(final DefaultTrackSelector r4, Looper r5) {
            if (this.listener == null) goto L5;
            return;
        L5:
            if (this.handler != null) goto L10;
            this.listener = new C03661(this, r4);
            this.handler = new Handler(r5);
            Spatializer r0 = this.spatializer;
            Handler r1 = this.handler;
            Objects.requireNonNull(r1);
            r0.addOnSpatializerStateChangedListener(new ExecutorC0271x59586989(r1), this.listener);
            return;
        }

        public boolean isAvailable() {
            return this.spatializer.isAvailable();
        }

        public boolean isEnabled() {
            return this.spatializer.isEnabled();
        }

        public boolean isSpatializationSupported() {
            return this.spatializationSupported;
        }

        public void release() {
            if (this.listener != null) goto L5;
            return;
        L5:
            if (this.handler == null) goto L10;
            this.spatializer.removeOnSpatializerStateChangedListener(this.listener);
            ((Handler) Util.castNonNull(this.handler)).removeCallbacksAndMessages(null);
            this.handler = null;
            this.listener = null;
            return;
        }
    }

    private static final class TextTrackInfo extends TrackInfo<TextTrackInfo> implements Comparable<TextTrackInfo> {
        private final boolean hasCaptionRoleFlags;
        private final boolean isDefault;
        private final boolean isForced;
        private final boolean isWithinRendererCapabilities;
        private final int preferredLanguageIndex;
        private final int preferredLanguageScore;
        private final int preferredRoleFlagsScore;
        private final int selectedAudioLanguageScore;
        private final int selectionEligibility;

        public TextTrackInfo(int r13, TrackGroup r14, int r15, Parameters r16, int r17, String r18) {
            super(r13, r14, r15);
            int r2 = 0;
            this.isWithinRendererCapabilities = RendererCapabilities.CC.isFormatSupported(r17, false);
            int r3 = this.format.selectionFlags & (~r16.ignoredTextSelectionFlags);
            if ((r3 & 1) == 0) goto L5;
            boolean r4 = true;
        L6:
            this.isDefault = r4;
            if ((r3 & 2) == 0) goto L9;
            boolean r5 = true;
        L10:
            this.isForced = r5;
            int r6 = Integer.MAX_VALUE;
            int r7 = 0;
            if (r16.preferredTextLanguages.isEmpty() == false) goto L13;
            ImmutableList<String> r8 = ImmutableList.m213of("");
        L14:
            int r9 = 0;
        L16:
            if (r9 >= r8.size()) goto L21;
            int r10 = DefaultTrackSelector.getFormatLanguageScore(this.format, r8.get(r9), r16.selectUndeterminedTextLanguage);
            if (r10 > 0) goto L19;
            r9 = r9 + 1;
            goto L16
        L19:
            r6 = r9;
            r7 = r10;
        L21:
            this.preferredLanguageIndex = r6;
            this.preferredLanguageScore = r7;
            this.preferredRoleFlagsScore = DefaultTrackSelector.access$4200(this.format.roleFlags, r16.preferredTextRoleFlags);
            if ((this.format.roleFlags & 1088) == 0) goto L24;
            boolean r11 = true;
        L25:
            this.hasCaptionRoleFlags = r11;
            if (DefaultTrackSelector.normalizeUndeterminedLanguageToNull(r18) != null) goto L28;
            boolean r12 = true;
        L29:
            this.selectedAudioLanguageScore = DefaultTrackSelector.getFormatLanguageScore(this.format, r18, r12);
            if (this.preferredLanguageScore <= 0) goto L32;
        L43:
            boolean r19 = true;
        L45:
            if (RendererCapabilities.CC.isFormatSupported(r17, r16.exceedRendererCapabilitiesIfNecessary) == false) goto L49;
            if (r19 == false) goto L49;
            r2 = 1;
        L49:
            this.selectionEligibility = r2;
            return;
        L32:
            if (r16.preferredTextLanguages.isEmpty() == false) goto L36;
            if (this.preferredRoleFlagsScore > 0) goto L43;
        L36:
            if (this.isDefault == true) goto L43;
            if (this.isForced == true) goto L40;
        L42:
            r19 = false;
            goto L45
        L40:
            if (this.selectedAudioLanguageScore <= 0) goto L42;
        L28:
            r12 = false;
            goto L29
        L24:
            r11 = false;
            goto L25
        L13:
            r8 = r16.preferredTextLanguages;
            goto L14
        L9:
            r5 = false;
            goto L10
        L5:
            r4 = false;
            goto L6
        }

        public static int compareSelections(List<TextTrackInfo> r2, List<TextTrackInfo> r3) {
            return r2.get(0).compareTo2(r3.get(0));
        }

        public static ImmutableList<TextTrackInfo> createForTrackGroup(int r9, TrackGroup r10, Parameters r11, int[] r12, String r13) {
            ImmutableList.Builder r0 = ImmutableList.builder();
            int r5 = 0;
        L4:
            if (r5 >= r10.length) goto L7;
            r0.add(new TextTrackInfo(r9, r10, r5, r11, r12[r5], r13));
            r5 = r5 + 1;
            goto L4
        L7:
            return r0.build();
        }

        /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
        public int compareTo2(TextTrackInfo r5) {
            ComparisonChain r0 = ComparisonChain.start().compareFalseFirst(this.isWithinRendererCapabilities, r5.isWithinRendererCapabilities).compare(Integer.valueOf(this.preferredLanguageIndex), Integer.valueOf(r5.preferredLanguageIndex), Ordering.natural().reverse()).compare(this.preferredLanguageScore, r5.preferredLanguageScore).compare(this.preferredRoleFlagsScore, r5.preferredRoleFlagsScore).compareFalseFirst(this.isDefault, r5.isDefault);
            Boolean r1 = Boolean.valueOf(this.isForced);
            Boolean r2 = Boolean.valueOf(r5.isForced);
            if (this.preferredLanguageScore != 0) goto L5;
            Ordering r3 = Ordering.natural();
        L6:
            ComparisonChain r4 = r0.compare(r1, r2, r3).compare(this.selectedAudioLanguageScore, r5.selectedAudioLanguageScore);
            if (this.preferredRoleFlagsScore != 0) goto L10;
            r4 = r4.compareTrueFirst(this.hasCaptionRoleFlags, r5.hasCaptionRoleFlags);
        L10:
            return r4.result();
        L5:
            r3 = Ordering.natural().reverse();
            goto L6
        }

        @Override // java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(TextTrackInfo r1) {
            return compareTo2(r1);
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public int getSelectionEligibility() {
            return this.selectionEligibility;
        }

        public boolean isCompatibleForAdaptationWith(TextTrackInfo r2) {
            return false;
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public /* bridge */ /* synthetic */ boolean isCompatibleForAdaptationWith(TrackInfo r1) {
            return isCompatibleForAdaptationWith((TextTrackInfo) r1);
        }
    }

    private static abstract class TrackInfo<T extends TrackInfo<T>> {
        public final Format format;
        public final int rendererIndex;
        public final TrackGroup trackGroup;
        public final int trackIndex;

        public interface Factory<T extends TrackInfo<T>> {
            List<T> create(int r1, TrackGroup r2, int[] r3);
        }

        public TrackInfo(int r2, TrackGroup r3, int r4) {
            this.rendererIndex = r2;
            this.trackGroup = r3;
            this.trackIndex = r4;
            this.format = r3.getFormat(r4);
        }

        public abstract int getSelectionEligibility();

        public abstract boolean isCompatibleForAdaptationWith(T r1);
    }

    private static final class VideoTrackInfo extends TrackInfo<VideoTrackInfo> {
        private static final float MIN_REASONABLE_FRAME_RATE = 10.0f;
        private final boolean allowMixedMimeTypes;
        private final int bitrate;
        private final int codecPreferenceScore;
        private final boolean hasMainOrNoRoleFlag;
        private final boolean hasReasonableFrameRate;
        private final boolean isWithinMaxConstraints;
        private final boolean isWithinMinConstraints;
        private final boolean isWithinRendererCapabilities;
        private final Parameters parameters;
        private final int pixelCount;
        private final int preferredMimeTypeMatchIndex;
        private final int preferredRoleFlagsScore;
        private final int selectionEligibility;
        private final boolean usesHardwareAcceleration;
        private final boolean usesPrimaryDecoder;

        /* JADX INFO: renamed from: $r8$lambda$G8uVDNHx35S9o7viZ4i-yMcwZzU, reason: not valid java name */
        public static /* synthetic */ int m481$r8$lambda$G8uVDNHx35S9o7viZ4iyMcwZzU(VideoTrackInfo r0, VideoTrackInfo r1) {
            return compareQualityPreferences(r0, r1);
        }

        public static /* synthetic */ int $r8$lambda$mN6XX8i7M4d49ufL_31PyFTAA68(VideoTrackInfo r0, VideoTrackInfo r1) {
            return compareNonQualityPreferences(r0, r1);
        }

        public VideoTrackInfo(int r8, TrackGroup r9, int r10, Parameters r11, int r12, int r13, boolean r14) {
            super(r8, r9, r10);
            this.parameters = r11;
            if (r11.allowVideoNonSeamlessAdaptiveness == false) goto L5;
            int r0 = 24;
        L6:
            boolean r2 = false;
            if (r11.allowVideoMixedMimeTypeAdaptiveness == true) goto L9;
        L11:
            boolean r1 = false;
        L12:
            this.allowMixedMimeTypes = r1;
            if (r14 == true) goto L15;
        L31:
            boolean r5 = false;
        L32:
            this.isWithinMaxConstraints = r5;
            if (r14 == true) goto L35;
        L51:
            boolean r4 = false;
        L52:
            this.isWithinMinConstraints = r4;
            this.isWithinRendererCapabilities = RendererCapabilities.CC.isFormatSupported(r12, false);
            if (this.format.frameRate != (-1.0f)) goto L55;
        L57:
            boolean r3 = false;
        L58:
            this.hasReasonableFrameRate = r3;
            this.bitrate = this.format.bitrate;
            this.pixelCount = this.format.getPixelCount();
            this.preferredRoleFlagsScore = DefaultTrackSelector.access$4200(this.format.roleFlags, r11.preferredVideoRoleFlags);
            if (this.format.roleFlags != 0) goto L61;
        L64:
            boolean r6 = true;
        L65:
            this.hasMainOrNoRoleFlag = r6;
            int r7 = Integer.MAX_VALUE;
            int r15 = 0;
        L67:
            if (r15 >= r11.preferredVideoMimeTypes.size()) goto L74;
            if (this.format.sampleMimeType == null) goto L73;
            if (this.format.sampleMimeType.equals(r11.preferredVideoMimeTypes.get(r15)) == false) goto L73;
            r7 = r15;
        L73:
            r15 = r15 + 1;
        L74:
            this.preferredMimeTypeMatchIndex = r7;
            if (RendererCapabilities.CC.getDecoderSupport(r12) != 128) goto L77;
            boolean r16 = true;
        L78:
            this.usesPrimaryDecoder = r16;
            if (RendererCapabilities.CC.getHardwareAccelerationSupport(r12) != 64) goto L81;
            r2 = true;
        L81:
            this.usesHardwareAcceleration = r2;
            this.codecPreferenceScore = DefaultTrackSelector.access$4300(this.format.sampleMimeType);
            this.selectionEligibility = evaluateSelectionEligibility(r12, r0);
            return;
        L77:
            r16 = false;
            goto L78
        L61:
            if ((this.format.roleFlags & 1) != 0) goto L64;
            r6 = false;
            goto L65
        L55:
            if (this.format.frameRate < MIN_REASONABLE_FRAME_RATE) goto L57;
            r3 = true;
            goto L58
        L35:
            if (this.format.width == (-1)) goto L39;
            if (this.format.width < r11.minVideoWidth) goto L51;
        L39:
            if (this.format.height == (-1)) goto L43;
            if (this.format.height < r11.minVideoHeight) goto L51;
        L43:
            if (this.format.frameRate == (-1.0f)) goto L47;
            if (this.format.frameRate < r11.minVideoFrameRate) goto L51;
        L47:
            if (this.format.bitrate != (-1)) goto L49;
        L50:
            r4 = true;
            goto L52
        L49:
            if (this.format.bitrate < r11.minVideoBitrate) goto L51;
        L15:
            if (this.format.width == (-1)) goto L19;
            if (this.format.width > r11.maxVideoWidth) goto L31;
        L19:
            if (this.format.height == (-1)) goto L23;
            if (this.format.height > r11.maxVideoHeight) goto L31;
        L23:
            if (this.format.frameRate == (-1.0f)) goto L27;
            if (this.format.frameRate > r11.maxVideoFrameRate) goto L31;
        L27:
            if (this.format.bitrate != (-1)) goto L29;
        L30:
            r5 = true;
            goto L32
        L29:
            if (this.format.bitrate > r11.maxVideoBitrate) goto L31;
        L9:
            if ((r13 & r0) == 0) goto L11;
            r1 = true;
            goto L12
        L5:
            r0 = 16;
            goto L6
        }

        private static int compareNonQualityPreferences(VideoTrackInfo r4, VideoTrackInfo r5) {
            ComparisonChain r0 = ComparisonChain.start().compareFalseFirst(r4.isWithinRendererCapabilities, r5.isWithinRendererCapabilities).compare(r4.preferredRoleFlagsScore, r5.preferredRoleFlagsScore).compareFalseFirst(r4.hasMainOrNoRoleFlag, r5.hasMainOrNoRoleFlag).compareFalseFirst(r4.hasReasonableFrameRate, r5.hasReasonableFrameRate).compareFalseFirst(r4.isWithinMaxConstraints, r5.isWithinMaxConstraints).compareFalseFirst(r4.isWithinMinConstraints, r5.isWithinMinConstraints).compare(Integer.valueOf(r4.preferredMimeTypeMatchIndex), Integer.valueOf(r5.preferredMimeTypeMatchIndex), Ordering.natural().reverse()).compareFalseFirst(r4.usesPrimaryDecoder, r5.usesPrimaryDecoder).compareFalseFirst(r4.usesHardwareAcceleration, r5.usesHardwareAcceleration);
            if (r4.usesPrimaryDecoder == false) goto L8;
            if (r4.usesHardwareAcceleration == false) goto L8;
            r0 = r0.compare(r4.codecPreferenceScore, r5.codecPreferenceScore);
        L8:
            return r0.result();
        }

        private static int compareQualityPreferences(VideoTrackInfo r5, VideoTrackInfo r6) {
            if (r5.isWithinMaxConstraints == true) goto L5;
        L7:
            Ordering r0 = DefaultTrackSelector.access$4400().reverse();
        L8:
            ComparisonChain r1 = ComparisonChain.start();
            if (r5.parameters.forceLowestBitrate == false) goto L12;
            r1 = r1.compare(Integer.valueOf(r5.bitrate), Integer.valueOf(r6.bitrate), DefaultTrackSelector.access$4400().reverse());
        L12:
            return r1.compare(Integer.valueOf(r5.pixelCount), Integer.valueOf(r6.pixelCount), r0).compare(Integer.valueOf(r5.bitrate), Integer.valueOf(r6.bitrate), r0).result();
        L5:
            if (r5.isWithinRendererCapabilities == false) goto L7;
            r0 = DefaultTrackSelector.access$4400();
            goto L8
        }

        public static int compareSelections(List<VideoTrackInfo> r4, List<VideoTrackInfo> r5) {
            return ComparisonChain.start().compare((VideoTrackInfo) Collections.max(r4, new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0()), (VideoTrackInfo) Collections.max(r5, new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0()), new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0()).compare(r4.size(), r5.size()).compare((VideoTrackInfo) Collections.max(r4, new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1()), (VideoTrackInfo) Collections.max(r5, new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1()), new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1()).result();
        }

        public static ImmutableList<VideoTrackInfo> createForTrackGroup(int r11, TrackGroup r12, Parameters r13, int[] r14, int r15) {
            int r0 = DefaultTrackSelector.access$4100(r12, r13.viewportWidth, r13.viewportHeight, r13.viewportOrientationMayChange);
            ImmutableList.Builder r1 = ImmutableList.builder();
            int r6 = 0;
        L4:
            if (r6 >= r12.length) goto L15;
            int r2 = r12.getFormat(r6).getPixelCount();
            if (r0 != Integer.MAX_VALUE) goto L8;
        L12:
            boolean r3 = true;
        L13:
            r1.add(new VideoTrackInfo(r11, r12, r6, r13, r14[r6], r15, r3));
            r6 = r6 + 1;
            goto L4
        L8:
            if (r2 == (-1)) goto L11;
            if (r2 <= r0) goto L12;
        L11:
            r3 = false;
            goto L13
        L15:
            return r1.build();
        }

        private int evaluateSelectionEligibility(int r3, int r4) {
            if ((this.format.roleFlags & 16384) == 0) goto L6;
            return 0;
        L6:
            if (RendererCapabilities.CC.isFormatSupported(r3, this.parameters.exceedRendererCapabilitiesIfNecessary) == true) goto L9;
            return 0;
        L9:
            if (this.isWithinMaxConstraints == true) goto L14;
            if (this.parameters.exceedVideoConstraintsIfNecessary == true) goto L14;
            return 0;
        L14:
            if (RendererCapabilities.CC.isFormatSupported(r3, false) == true) goto L16;
        L28:
            return 1;
        L16:
            if (this.isWithinMinConstraints == false) goto L28;
            if (this.isWithinMaxConstraints == false) goto L28;
            if (this.format.bitrate == (-1)) goto L28;
            if (this.parameters.forceHighestSupportedBitrate == true) goto L28;
            if (this.parameters.forceLowestBitrate == true) goto L28;
            if ((r3 & r4) == 0) goto L28;
            return 2;
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public int getSelectionEligibility() {
            return this.selectionEligibility;
        }

        @Override // androidx.media3.exoplayer.trackselection.DefaultTrackSelector.TrackInfo
        public /* bridge */ /* synthetic */ boolean isCompatibleForAdaptationWith(TrackInfo r1) {
            return isCompatibleForAdaptationWith((VideoTrackInfo) r1);
        }

        public boolean isCompatibleForAdaptationWith(VideoTrackInfo r3) {
            if (this.allowMixedMimeTypes == true) goto L7;
            if (Util.areEqual(this.format.sampleMimeType, r3.format.sampleMimeType) == true) goto L7;
        L13:
            return false;
        L7:
            if (this.parameters.allowVideoMixedDecoderSupportAdaptiveness == false) goto L9;
        L14:
            return true;
        L9:
            if (this.usesPrimaryDecoder != r3.usesPrimaryDecoder) goto L13;
            if (this.usesHardwareAcceleration != r3.usesHardwareAcceleration) goto L13;
            goto L13
        }
    }

    public static /* synthetic */ boolean $r8$lambda$xSqYhF9RuDotO9TC1HyBFBSO7Fw(DefaultTrackSelector r0, Format r1) {
        return r0.isAudioFormatWithinAudioChannelCountConstraints(r1);
    }

    static {
        FORMAT_VALUE_ORDERING = Ordering.from(new DefaultTrackSelector$$ExternalSyntheticLambda0());
    }

    public DefaultTrackSelector(Context r2) {
        this(r2, new AdaptiveTrackSelection.Factory());
    }

    public DefaultTrackSelector(Context r2, TrackSelectionParameters r3) {
        this(r2, r3, new AdaptiveTrackSelection.Factory());
    }

    public DefaultTrackSelector(Context r1, TrackSelectionParameters r2, ExoTrackSelection.Factory r3) {
        this(r2, r3, r1);
    }

    public DefaultTrackSelector(Context r2, ExoTrackSelection.Factory r3) {
        this(r2, Parameters.getDefaults(r2), r3);
    }

    @Deprecated
    public DefaultTrackSelector(TrackSelectionParameters r2, ExoTrackSelection.Factory r3) {
        this(r2, r3, null);
    }

    private DefaultTrackSelector(TrackSelectionParameters r3, ExoTrackSelection.Factory r4, Context r5) {
        this.lock = new Object();
        if (r5 == null) goto L5;
        Context r0 = r5.getApplicationContext();
    L6:
        this.context = r0;
        this.trackSelectionFactory = r4;
        if ((r3 instanceof Parameters) == false) goto L9;
        this.parameters = (Parameters) r3;
    L13:
        this.audioAttributes = AudioAttributes.DEFAULT;
        if (r5 != null) goto L16;
    L18:
        boolean r1 = false;
    L19:
        this.deviceIsTV = r1;
        if (this.deviceIsTV == true) goto L26;
        if (r5 == null) goto L26;
        if (Util.SDK_INT < 32) goto L26;
        this.spatializer = SpatializerWrapperV32.tryCreateInstance(r5);
    L26:
        if (this.parameters.constrainAudioChannelCountToDeviceCapabilities == false) goto L30;
        if (r5 != null) goto L31;
        Log.m48w(TAG, AUDIO_CHANNEL_COUNT_CONSTRAINTS_WARN_MESSAGE);
        return;
    L31:
        return;
    L30:
        return;
    L16:
        if (Util.isTv(r5) == false) goto L18;
        r1 = true;
        goto L19
    L9:
        if (r5 != null) goto L11;
        Parameters r2 = Parameters.DEFAULT_WITHOUT_CONTEXT;
    L12:
        this.parameters = r2.buildUpon().set(r3).build();
        goto L13
    L11:
        r2 = Parameters.getDefaults(r5);
        goto L12
    L5:
        r0 = null;
        goto L6
    }

    static /* synthetic */ int access$4100(TrackGroup r1, int r2, int r3, boolean r4) {
        return getMaxVideoPixelsToRetainForViewport(r1, r2, r3, r4);
    }

    static /* synthetic */ int access$4200(int r1, int r2) {
        return getRoleFlagMatchScore(r1, r2);
    }

    static /* synthetic */ int access$4300(String r1) {
        return getVideoCodecPreferenceScore(r1);
    }

    static /* synthetic */ Ordering access$4400() {
        return FORMAT_VALUE_ORDERING;
    }

    static /* synthetic */ boolean access$4500(Format r1) {
        return isObjectBasedAudio(r1);
    }

    static /* synthetic */ boolean access$4600(Parameters r1, int r2, Format r3) {
        return rendererSupportsOffload(r1, r2, r3);
    }

    static /* synthetic */ void access$4700(DefaultTrackSelector r0) {
        r0.maybeInvalidateForAudioChannelCountConstraints();
    }

    private static void applyLegacyRendererOverrides(MappingTrackSelector.MappedTrackInfo r8, Parameters r9, ExoTrackSelection.Definition[] r10) {
        int r0 = r8.getRendererCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        TrackGroupArray r2 = r8.getTrackGroups(r1);
        if (r9.hasSelectionOverride(r1, r2) == false) goto L14;
        SelectionOverride r3 = r9.getSelectionOverride(r1, r2);
        if (r3 != null) goto L10;
    L12:
        ExoTrackSelection.Definition r4 = null;
    L13:
        r10[r1] = r4;
        goto L14
    L10:
        if (r3.tracks.length == 0) goto L12;
        r4 = new ExoTrackSelection.Definition(r2.get(r3.groupIndex), r3.tracks, r3.type);
    L14:
        r1 = r1 + 1;
        goto L3
    }

    private static void applyTrackSelectionOverrides(MappingTrackSelector.MappedTrackInfo r8, TrackSelectionParameters r9, ExoTrackSelection.Definition[] r10) {
        int r0 = r8.getRendererCount();
        HashMap r1 = new HashMap();
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        collectTrackSelectionOverrides(r8.getTrackGroups(r2), r9, r1);
        r2 = r2 + 1;
        goto L3
    L5:
        collectTrackSelectionOverrides(r8.getUnmappedTrackGroups(), r9, r1);
        int r3 = 0;
    L6:
        if (r3 >= r0) goto L18;
        TrackSelectionOverride r4 = (TrackSelectionOverride) r1.get(Integer.valueOf(r8.getRendererType(r3)));
        if (r4 == null) goto L17;
        if (r4.trackIndices.isEmpty() == false) goto L13;
    L15:
        ExoTrackSelection.Definition r5 = null;
    L16:
        r10[r3] = r5;
        goto L17
    L13:
        if (r8.getTrackGroups(r3).indexOf(r4.mediaTrackGroup) == (-1)) goto L15;
        r5 = new ExoTrackSelection.Definition(r4.mediaTrackGroup, Ints.toArray(r4.trackIndices));
    L17:
        r3 = r3 + 1;
        goto L6
    }

    private static void collectTrackSelectionOverrides(TrackGroupArray r5, TrackSelectionParameters r6, Map<Integer, TrackSelectionOverride> r7) {
        int r0 = 0;
    L4:
        if (r0 >= r5.length) goto L16;
        TrackGroup r1 = r5.get(r0);
        TrackSelectionOverride r2 = r6.overrides.get(r1);
        if (r2 == null) goto L15;
        TrackSelectionOverride r3 = r7.get(Integer.valueOf(r2.getType()));
        if (r3 != null) goto L11;
    L14:
        r7.put(Integer.valueOf(r2.getType()), r2);
        goto L15
    L11:
        if (r3.trackIndices.isEmpty() == false) goto L15;
        if (r2.trackIndices.isEmpty() == false) goto L14;
    L15:
        r0 = r0 + 1;
        goto L4
    }

    protected static int getFormatLanguageScore(Format r5, String r6, boolean r7) {
        if (TextUtils.isEmpty(r6) == false) goto L5;
    L8:
        String r8 = normalizeUndeterminedLanguageToNull(r6);
        String r0 = normalizeUndeterminedLanguageToNull(r5.language);
        if (r0 == null) goto L24;
        if (r8 == null) goto L24;
        if (r0.startsWith(r8) == false) goto L15;
        return 3;
    L15:
        if (r8.startsWith(r0) == false) goto L18;
        return 3;
    L18:
        if (Util.splitAtFirst(r0, "-")[0].equals(Util.splitAtFirst(r8, "-")[0]) == false) goto L21;
        return 2;
    L21:
        return 0;
    L24:
        if (r7 == false) goto L28;
        if (r0 != null) goto L29;
        return 1;
    L29:
        return 0;
    L28:
        return 0;
    L5:
        if (r6.equals(r5.language) == false) goto L8;
        return 4;
    }

    private static int getMaxVideoPixelsToRetainForViewport(TrackGroup r8, int r9, int r10, boolean r11) {
        if (r9 == Integer.MAX_VALUE) goto L21;
        if (r10 == Integer.MAX_VALUE) goto L21;
        int r0 = Integer.MAX_VALUE;
        int r1 = 0;
    L8:
        if (r1 >= r8.length) goto L20;
        Format r2 = r8.getFormat(r1);
        if (r2.width <= 0) goto L19;
        if (r2.height <= 0) goto L19;
        Point r3 = TrackSelectionUtil.getMaxVideoSizeInViewport(r11, r9, r10, r2.width, r2.height);
        int r4 = r2.width * r2.height;
        if (r2.width < ((int) (r3.x * FRACTION_TO_CONSIDER_FULLSCREEN))) goto L19;
        if (r2.height < ((int) (r3.y * FRACTION_TO_CONSIDER_FULLSCREEN))) goto L19;
        if (r4 >= r0) goto L19;
        r0 = r4;
    L19:
        r1 = r1 + 1;
        goto L8
    L20:
        return r0;
    L21:
        return Integer.MAX_VALUE;
    }

    private static int getRoleFlagMatchScore(int r1, int r2) {
        if (r1 == 0) goto L7;
        if (r1 != r2) goto L7;
        return Integer.MAX_VALUE;
    L7:
        return Integer.bitCount(r1 & r2);
    }

    private static int getVideoCodecPreferenceScore(String r6) {
        if (r6 != null) goto L6;
        return 0;
    L6:
        switch(r6.hashCode()) {
            case -1851077871: goto L21;
            case -1662735862: goto L18;
            case -1662541442: goto L15;
            case 1331836730: goto L12;
            case 1599127257: goto L9;
            default: goto L23;
        };
    L23:
        byte r1 = -1;
    L24:
        switch(r1) {
            case 0: goto L30;
            case 1: goto L29;
            case 2: goto L28;
            case 3: goto L27;
            case 4: goto L26;
            default: goto L25;
        };
    L25:
        return 0;
    L26:
        return 1;
    L27:
        return 2;
    L28:
        return 3;
    L29:
        return 4;
    L30:
        return 5;
    L9:
        if (r6.equals(MimeTypes.VIDEO_VP9) == false) goto L23;
        r1 = 3;
        goto L24
    L12:
        if (r6.equals(MimeTypes.VIDEO_H264) == false) goto L23;
        r1 = 4;
        goto L24
    L15:
        if (r6.equals(MimeTypes.VIDEO_H265) == false) goto L23;
        r1 = 2;
        goto L24
    L18:
        if (r6.equals(MimeTypes.VIDEO_AV1) == false) goto L23;
        r1 = 1;
        goto L24
    L21:
        if (r6.equals(MimeTypes.VIDEO_DOLBY_VISION) == false) goto L23;
        r1 = 0;
        goto L24
    }

    private boolean isAudioFormatWithinAudioChannelCountConstraints(Format r4) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L37:
        th = move-exception;
        throw th;
    L5:
        if (this.parameters.constrainAudioChannelCountToDeviceCapabilities == true) goto L7;
    L34:
        boolean r1 = true;
    L35:
        monitor-exit(r0);     // Catch: Throwable -> L37
        return r1;
    L7:
        if (this.deviceIsTV == true) goto L34;
        if (r4.channelCount == (-1)) goto L34;
        if (r4.channelCount <= 2) goto L34;
        if (isDolbyAudio(r4) == false) goto L21;
        if (Util.SDK_INT < 32) goto L34;
        if (this.spatializer == null) goto L34;
        if (this.spatializer.isSpatializationSupported() == false) goto L34;
    L21:
        if (Util.SDK_INT >= 32) goto L23;
    L33:
        r1 = false;
        goto L35
    L23:
        if (this.spatializer == null) goto L33;
        if (this.spatializer.isSpatializationSupported() == false) goto L33;
        if (this.spatializer.isAvailable() == false) goto L33;
        if (this.spatializer.isEnabled() == false) goto L33;
        if (this.spatializer.canBeSpatialized(this.audioAttributes, r4) == false) goto L33;
        goto L33
    }

    private static boolean isDolbyAudio(Format r4) {
        if (r4.sampleMimeType != null) goto L5;
        return false;
    L5:
        String r0 = r4.sampleMimeType;
        switch(r0.hashCode()) {
            case -2123537834: goto L18;
            case 187078296: goto L15;
            case 187078297: goto L12;
            case 1504578661: goto L9;
            default: goto L20;
        };
    L20:
        byte r1 = -1;
    L21:
        switch(r1) {
            case 0: goto L23;
            case 1: goto L23;
            case 2: goto L23;
            case 3: goto L23;
            default: goto L22;
        };
    L22:
        return false;
    L23:
        return true;
    L9:
        if (r0.equals(MimeTypes.AUDIO_E_AC3) == false) goto L20;
        r1 = 1;
        goto L21
    L12:
        if (r0.equals(MimeTypes.AUDIO_AC4) == false) goto L20;
        r1 = 3;
        goto L21
    L15:
        if (r0.equals(MimeTypes.AUDIO_AC3) == false) goto L20;
        r1 = 0;
        goto L21
    L18:
        if (r0.equals(MimeTypes.AUDIO_E_AC3_JOC) == false) goto L20;
        r1 = 2;
        goto L21
    }

    private static boolean isObjectBasedAudio(Format r4) {
        if (r4.sampleMimeType != null) goto L5;
        return false;
    L5:
        String r0 = r4.sampleMimeType;
        switch(r0.hashCode()) {
            case -2123537834: goto L15;
            case 187078297: goto L12;
            case 1504698186: goto L9;
            default: goto L17;
        };
    L17:
        byte r1 = -1;
    L18:
        switch(r1) {
            case 0: goto L20;
            case 1: goto L20;
            case 2: goto L20;
            default: goto L19;
        };
    L19:
        return false;
    L20:
        return true;
    L9:
        if (r0.equals(MimeTypes.AUDIO_IAMF) == false) goto L17;
        r1 = 2;
        goto L18
    L12:
        if (r0.equals(MimeTypes.AUDIO_AC4) == false) goto L17;
        r1 = 1;
        goto L18
    L15:
        if (r0.equals(MimeTypes.AUDIO_E_AC3_JOC) == false) goto L17;
        r1 = 0;
        goto L18
    }

    @Deprecated
    protected static boolean isSupported(int r1, boolean r2) {
        return RendererCapabilities.CC.isFormatSupported(r1, r2);
    }

    static /* synthetic */ List lambda$selectImageTrack$4(Parameters r1, int r2, TrackGroup r3, int[] r4) {
        return ImageTrackInfo.createForTrackGroup(r2, r3, r1, r4);
    }

    static /* synthetic */ List lambda$selectTextTrack$3(Parameters r1, String r2, int r3, TrackGroup r4, int[] r5) {
        return TextTrackInfo.createForTrackGroup(r3, r4, r1, r5, r2);
    }

    static /* synthetic */ List lambda$selectVideoTrack$1(Parameters r1, int[] r2, int r3, TrackGroup r4, int[] r5) {
        return VideoTrackInfo.createForTrackGroup(r3, r4, r1, r5, r2[r3]);
    }

    static /* synthetic */ int lambda$static$0(Integer r2, Integer r3) {
        if (r2.intValue() != (-1)) goto L8;
        if (r3.intValue() != (-1)) goto L12;
        return 0;
    L12:
        return -1;
    L8:
        if (r3.intValue() != (-1)) goto L11;
        return 1;
    L11:
        return r2.intValue() - r3.intValue();
    }

    private static void maybeConfigureRendererForOffload(Parameters r9, MappingTrackSelector.MappedTrackInfo r10, int[][][] r11, RendererConfiguration[] r12, ExoTrackSelection[] r13) {
        int r0 = -1;
        int r1 = 0;
        boolean r2 = false;
        int r3 = 0;
    L3:
        boolean r5 = false;
        if (r3 >= r10.getRendererCount()) goto L17;
        int r4 = r10.getRendererType(r3);
        ExoTrackSelection r7 = r13[r3];
        if (r4 == 1) goto L9;
        if (r7 == null) goto L9;
        r2 = true;
    L9:
        if (r4 != 1) goto L16;
        if (r7 == null) goto L16;
        if (r7.length() != 1) goto L16;
        if (rendererSupportsOffload(r9, r11[r3][r10.getTrackGroups(r3).indexOf(r7.getTrackGroup())][r7.getIndexInTrackGroup(0)], r7.getSelectedFormat()) == false) goto L16;
        r0 = r3;
        r1 = r1 + 1;
    L16:
        r3 = r3 + 1;
    L17:
        if (r2 == true) goto L37;
        if (r1 == 1) goto L20;
        return;
    L20:
        if (r9.audioOffloadPreferences.isGaplessSupportRequired == false) goto L22;
        int r6 = 1;
    L24:
        if (r12[r0] != null) goto L26;
    L28:
        r12[r0] = new RendererConfiguration(r6, r5);
        return;
    L26:
        if (r12[r0].tunneling == false) goto L28;
        r5 = true;
        goto L28
    L22:
        r6 = 2;
        goto L24
    }

    private static void maybeConfigureRenderersForTunneling(MappingTrackSelector.MappedTrackInfo r10, int[][][] r11, RendererConfiguration[] r12, ExoTrackSelection[] r13) {
        int r0 = -1;
        int r1 = -1;
        boolean r2 = true;
        int r3 = 0;
    L4:
        if (r3 >= r10.getRendererCount()) goto L21;
        int r4 = r10.getRendererType(r3);
        ExoTrackSelection r7 = r13[r3];
        if (r4 != 1) goto L8;
    L9:
        if (r7 == null) goto L19;
        if (rendererSupportsTunneling(r11[r3], r10.getTrackGroups(r3), r7) == false) goto L19;
        if (r4 != 1) goto L16;
        if (r0 != (-1)) goto L14;
        r0 = r3;
        goto L19
    L14:
        r2 = false;
        goto L21
    L16:
        if (r1 != (-1)) goto L17;
        r1 = r3;
        goto L19
    L17:
        r2 = false;
    L19:
        r3 = r3 + 1;
        goto L4
    L8:
        if (r4 != 2) goto L19;
    L21:
        if (r0 == (-1)) goto L24;
        if (r1 == (-1)) goto L24;
        boolean r5 = true;
    L26:
        if ((r2 & r5) == false) goto L37;
        RendererConfiguration r6 = new RendererConfiguration(0, true);
        r12[r0] = r6;
        r12[r1] = r6;
        return;
    L37:
        return;
    L24:
        r5 = false;
        goto L26
    }

    private void maybeInvalidateForAudioChannelCountConstraints() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L20:
        th = move-exception;
        throw th;
    L5:
        if (this.parameters.constrainAudioChannelCountToDeviceCapabilities == true) goto L7;
    L15:
        boolean r1 = false;
    L16:
        monitor-exit(r0);     // Catch: Throwable -> L20
        if (r1 == false) goto L25;
        invalidate();
        return;
    L25:
        return;
    L7:
        if (this.deviceIsTV == true) goto L15;
        if (Util.SDK_INT < 32) goto L15;
        if (this.spatializer == null) goto L15;
        if (this.spatializer.isSpatializationSupported() == false) goto L15;
        r1 = true;
        goto L16
    }

    private void maybeInvalidateForRendererCapabilitiesChange(Renderer r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
        boolean r1 = this.parameters.allowInvalidateSelectionsOnRendererCapabilitiesChange;     // Catch: Throwable -> L9
        monitor-exit(r0);     // Catch: Throwable -> L9
        if (r1 == false) goto L14;
        invalidateForRendererCapabilitiesChange(r3);
        return;
    L14:
        return;
    L9:
        th = move-exception;
        throw th;
    }

    protected static String normalizeUndeterminedLanguageToNull(String r1) {
        if (TextUtils.isEmpty(r1) == false) goto L5;
    L8:
        return null;
    L5:
        if (TextUtils.equals(r1, C0180C.LANGUAGE_UNDETERMINED) == true) goto L8;
        return r1;
    }

    private static boolean rendererSupportsOffload(Parameters r4, int r5, Format r6) {
        if (RendererCapabilities.CC.getAudioOffloadSupport(r5) != 0) goto L6;
        return false;
    L6:
        if (r4.audioOffloadPreferences.isSpeedChangeSupportRequired == false) goto L11;
        if ((RendererCapabilities.CC.getAudioOffloadSupport(r5) & 2048) != 0) goto L11;
        return false;
    L11:
        if (r4.audioOffloadPreferences.isGaplessSupportRequired == true) goto L13;
        return true;
    L13:
        if (r6.encoderDelay == 0) goto L15;
    L18:
        boolean r0 = true;
    L20:
        if ((RendererCapabilities.CC.getAudioOffloadSupport(r5) & 1024) == 0) goto L22;
        boolean r3 = true;
    L23:
        if (r0 == false) goto L26;
        if (r3 == true) goto L26;
        return false;
    L26:
        return true;
    L22:
        r3 = false;
        goto L23
    L15:
        if (r6.encoderPadding != 0) goto L18;
        r0 = false;
        goto L20
    }

    private static boolean rendererSupportsTunneling(int[][] r6, TrackGroupArray r7, ExoTrackSelection r8) {
        if (r8 != null) goto L5;
        return false;
    L5:
        int r1 = r7.indexOf(r8.getTrackGroup());
        int r2 = 0;
    L7:
        if (r2 >= r8.length()) goto L12;
        if (RendererCapabilities.CC.getTunnelingSupport(r6[r1][r8.getIndexInTrackGroup(r2)]) != 32) goto L10;
        r2 = r2 + 1;
        goto L7
    L10:
        return false;
    L12:
        return true;
    }

    private <T extends TrackInfo<T>> Pair<ExoTrackSelection.Definition, Integer> selectTracksForType(int r21, MappingTrackSelector.MappedTrackInfo r22, int[][][] r23, TrackInfo.Factory<T> r24, Comparator<List<T>> r25) {
        MappingTrackSelector.MappedTrackInfo r0 = r22;
        ArrayList r1 = new ArrayList();
        int r2 = r0.getRendererCount();
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L37;
        if (r21 != r0.getRendererType(r3)) goto L35;
        TrackGroupArray r4 = r0.getTrackGroups(r3);
        int r6 = 0;
    L8:
        if (r6 >= r4.length) goto L35;
        TrackGroup r7 = r4.get(r6);
        List<T> r10 = r24.create(r3, r7, r23[r3][r6]);
        boolean[] r11 = new boolean[r7.length];
        int r12 = 0;
    L11:
        if (r12 >= r7.length) goto L32;
        T r13 = r10.get(r12);
        int r14 = r13.getSelectionEligibility();
        if (r11[r12] == true) goto L30;
        if (r14 != 0) goto L17;
        int r17 = r2;
        int r19 = r3;
    L31:
        r12 = r12 + 1;
        r2 = r17;
        r3 = r19;
        goto L11
    L17:
        if (r14 != 1) goto L19;
        List r15 = ImmutableList.m213of(r13);
        r17 = r2;
        r19 = r3;
    L29:
        r1.add(r15);
        goto L31
    L19:
        r15 = new ArrayList();
        r15.add(r13);
        int r5 = r12 + 1;
    L20:
        r17 = r2;
        if (r5 >= r7.length) goto L28;
        T r8 = r10.get(r5);
        int r18 = r5;
        int r110 = r3;
        if (r8.getSelectionEligibility() != 2) goto L27;
        if (r13.isCompatibleForAdaptationWith(r8) == false) goto L27;
        r15.add(r8);
        r11[r18] = true;
    L27:
        r5 = r18 + 1;
        r2 = r17;
        r3 = r110;
        goto L20
    L28:
        r19 = r3;
        goto L29
    L30:
        r17 = r2;
        r19 = r3;
        goto L31
    L32:
        r6 = r6 + 1;
    L35:
        r3 = r3 + 1;
        r0 = r22;
        r2 = r2;
        goto L3
    L37:
        if (r1.isEmpty() == false) goto L40;
        return null;
    L40:
        List r9 = (List) Collections.max(r1, r25);
        int[] r16 = new int[r9.size()];
        int r20 = 0;
    L42:
        if (r20 >= r9.size()) goto L44;
        r16[r20] = ((TrackInfo) r9.get(r20)).trackIndex;
        r20 = r20 + 1;
        goto L42
    L44:
        TrackInfo r26 = (TrackInfo) r9.get(0);
        return Pair.create(new ExoTrackSelection.Definition(r26.trackGroup, r16), Integer.valueOf(r26.rendererIndex));
    }

    private void setParametersInternal(Parameters r4) {
        Assertions.checkNotNull(r4);
        Object r0 = this.lock;
        monitor-enter(r0);
    L18:
        th = move-exception;
        throw th;
    L5:
        if (this.parameters.equals(r4) == true) goto L7;
        boolean r1 = true;
    L8:
        this.parameters = r4;     // Catch: Throwable -> L18
        monitor-exit(r0);     // Catch: Throwable -> L18
        if (r1 == true) goto L12;
        return;
    L12:
        if (r4.constrainAudioChannelCountToDeviceCapabilities == true) goto L14;
    L16:
        invalidate();
        return;
    L14:
        if (this.context != null) goto L16;
        Log.m48w(TAG, AUDIO_CHANNEL_COUNT_CONSTRAINTS_WARN_MESSAGE);
        goto L16
    L7:
        r1 = false;
        goto L8
    }

    public Parameters.Builder buildUponParameters() {
        return getParameters().buildUpon();
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public /* bridge */ /* synthetic */ TrackSelectionParameters getParameters() {
        return getParameters();
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public Parameters getParameters() {
        Object r0 = this.lock;
        monitor-enter(r0);
        Parameters r1 = this.parameters;     // Catch: Throwable -> L7
        monitor-exit(r0);     // Catch: Throwable -> L7
        return r1;
    L7:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public RendererCapabilities.Listener getRendererCapabilitiesListener() {
        return this;
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public boolean isSetParametersSupported() {
        return true;
    }

    /* JADX INFO: renamed from: lambda$selectAudioTrack$2$androidx-media3-exoplayer-trackselection-DefaultTrackSelector */
    /* synthetic */ List m147x92d8c743(Parameters r8, boolean r9, int[] r10, int r11, TrackGroup r12, int[] r13) {
        return AudioTrackInfo.createForTrackGroup(r11, r12, r8, r13, r9, new DefaultTrackSelector$$ExternalSyntheticLambda1(this), r10[r11]);
    }

    @Override // androidx.media3.exoplayer.RendererCapabilities.Listener
    public void onRendererCapabilitiesChanged(Renderer r1) {
        maybeInvalidateForRendererCapabilitiesChange(r1);
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public void release() {
        Object r0 = this.lock;
        monitor-enter(r0);
    L12:
        th = move-exception;
        throw th;
    L5:
        if (Util.SDK_INT >= 32) goto L7;
    L9:
        monitor-exit(r0);     // Catch: Throwable -> L12
        super.release();
        return;
    L7:
        if (this.spatializer == null) goto L9;
        this.spatializer.release();     // Catch: Throwable -> L12
        goto L9
    }

    protected ExoTrackSelection.Definition[] selectAllTracks(MappingTrackSelector.MappedTrackInfo r12, int[][][] r13, int[] r14, Parameters r15) throws ExoPlaybackException {
        int r0 = r12.getRendererCount();
        ExoTrackSelection.Definition[] r1 = new ExoTrackSelection.Definition[r0];
        Pair<ExoTrackSelection.Definition, Integer> r2 = selectVideoTrack(r12, r13, r14, r15);
        String r4 = null;
        if (r15.isPrioritizeImageOverVideoEnabled == true) goto L7;
        if (r2 == null) goto L7;
        Pair<ExoTrackSelection.Definition, Integer> r3 = null;
    L9:
        if (r3 == null) goto L11;
        r1[((Integer) r3.second).intValue()] = (ExoTrackSelection.Definition) r3.first;
    L13:
        Pair<ExoTrackSelection.Definition, Integer> r5 = selectAudioTrack(r12, r13, r14, r15);
        if (r5 == null) goto L16;
        r1[((Integer) r5.second).intValue()] = (ExoTrackSelection.Definition) r5.first;
    L16:
        if (r5 == null) goto L19;
        r4 = ((ExoTrackSelection.Definition) r5.first).group.getFormat(((ExoTrackSelection.Definition) r5.first).tracks[0]).language;
    L19:
        Pair<ExoTrackSelection.Definition, Integer> r6 = selectTextTrack(r12, r13, r15, r4);
        if (r6 == null) goto L22;
        r1[((Integer) r6.second).intValue()] = (ExoTrackSelection.Definition) r6.first;
    L22:
        int r7 = 0;
    L23:
        if (r7 >= r0) goto L34;
        int r8 = r12.getRendererType(r7);
        if (r8 == 2) goto L33;
        if (r8 == 1) goto L33;
        if (r8 == 3) goto L33;
        if (r8 == 4) goto L33;
        r1[r7] = selectOtherTrack(r8, r12.getTrackGroups(r7), r13[r7], r15);
    L33:
        r7 = r7 + 1;
        goto L23
    L34:
        return r1;
    L11:
        if (r2 == null) goto L13;
        r1[((Integer) r2.second).intValue()] = (ExoTrackSelection.Definition) r2.first;
    L7:
        r3 = selectImageTrack(r12, r13, r15);
        goto L9
    }

    protected Pair<ExoTrackSelection.Definition, Integer> selectAudioTrack(MappingTrackSelector.MappedTrackInfo r9, int[][][] r10, final int[] r11, final Parameters r12) throws ExoPlaybackException {
        boolean r0 = false;
        int r1 = 0;
    L4:
        if (r1 >= r9.getRendererCount()) goto L11;
        if (2 != r9.getRendererType(r1)) goto L10;
        if (r9.getTrackGroups(r1).length <= 0) goto L10;
        r0 = true;
    L10:
        r1 = r1 + 1;
    L11:
        final boolean r2 = r0;
        return selectTracksForType(1, r9, r10, new DefaultTrackSelector$$ExternalSyntheticLambda4(this, r12, r2, r11), new DefaultTrackSelector$$ExternalSyntheticLambda5());
    }

    protected Pair<ExoTrackSelection.Definition, Integer> selectImageTrack(MappingTrackSelector.MappedTrackInfo r8, int[][][] r9, final Parameters r10) throws ExoPlaybackException {
        if (r10.audioOffloadPreferences.audioOffloadMode != 2) goto L7;
        return null;
    L7:
        return selectTracksForType(4, r8, r9, new DefaultTrackSelector$$ExternalSyntheticLambda2(r10), new DefaultTrackSelector$$ExternalSyntheticLambda3());
    }

    protected ExoTrackSelection.Definition selectOtherTrack(int r12, TrackGroupArray r13, int[][] r14, Parameters r15) throws ExoPlaybackException {
        if (r15.audioOffloadPreferences.audioOffloadMode != 2) goto L5;
        return null;
    L5:
        TrackGroup r0 = null;
        int r1 = 0;
        OtherTrackScore r3 = null;
        int r4 = 0;
    L7:
        if (r4 >= r13.length) goto L20;
        TrackGroup r5 = r13.get(r4);
        int[] r6 = r14[r4];
        int r7 = 0;
    L10:
        if (r7 >= r5.length) goto L19;
        if (RendererCapabilities.CC.isFormatSupported(r6[r7], r15.exceedRendererCapabilitiesIfNecessary) == false) goto L18;
        OtherTrackScore r9 = new OtherTrackScore(r5.getFormat(r7), r6[r7]);
        if (r3 != null) goto L16;
    L17:
        r0 = r5;
        r1 = r7;
        r3 = r9;
        goto L18
    L16:
        if (r9.compareTo2(r3) > 0) goto L17;
    L18:
        r7 = r7 + 1;
        goto L10
    L19:
        r4 = r4 + 1;
        goto L7
    L20:
        if (r0 != null) goto L23;
        return null;
    L23:
        return new ExoTrackSelection.Definition(r0, new int[]{r1});
    }

    protected Pair<ExoTrackSelection.Definition, Integer> selectTextTrack(MappingTrackSelector.MappedTrackInfo r8, int[][][] r9, final Parameters r10, final String r11) throws ExoPlaybackException {
        if (r10.audioOffloadPreferences.audioOffloadMode != 2) goto L7;
        return null;
    L7:
        return selectTracksForType(3, r8, r9, new DefaultTrackSelector$$ExternalSyntheticLambda8(r10, r11), new DefaultTrackSelector$$ExternalSyntheticLambda9());
    }

    @Override // androidx.media3.exoplayer.trackselection.MappingTrackSelector
    protected final Pair<RendererConfiguration[], ExoTrackSelection[]> selectTracks(MappingTrackSelector.MappedTrackInfo r18, int[][][] r19, int[] r20, MediaSource.MediaPeriodId r21, Timeline r22) throws ExoPlaybackException {
        Object r4 = this.lock;
        monitor-enter(r4);
        Parameters r0 = this.parameters;     // Catch: Throwable -> L49
        if (r0.constrainAudioChannelCountToDeviceCapabilities == true) goto L7;
    L11:
        monitor-exit(r4);     // Catch: Throwable -> L49
        int r5 = r18.getRendererCount();
        ExoTrackSelection.Definition[] r6 = selectAllTracks(r18, r19, r20, r0);
        applyTrackSelectionOverrides(r18, r0, r6);
        applyLegacyRendererOverrides(r18, r0, r6);
        int r7 = 0;
    L14:
        if (r7 >= r5) goto L21;
        int r9 = r18.getRendererType(r7);
        if (r0.getRendererDisabled(r7) == false) goto L18;
    L19:
        r6[r7] = null;
    L20:
        r7 = r7 + 1;
        goto L14
    L18:
        if (r0.disabledTrackTypes.contains(Integer.valueOf(r9)) == false) goto L20;
    L21:
        ExoTrackSelection[] r8 = this.trackSelectionFactory.createTrackSelections(r6, getBandwidthMeter(), r21, r22);
        RendererConfiguration[] r10 = new RendererConfiguration[r5];
        int r12 = 0;
    L22:
        if (r12 >= r5) goto L42;
        int r13 = r18.getRendererType(r12);
        if (r0.getRendererDisabled(r12) == false) goto L26;
    L29:
        boolean r11 = true;
    L30:
        if (r11 == false) goto L32;
    L36:
        boolean r15 = false;
    L37:
        if (r15 == false) goto L39;
        RendererConfiguration r14 = RendererConfiguration.DEFAULT;
    L40:
        r10[r12] = r14;
        r12 = r12 + 1;
        goto L22
    L39:
        r14 = null;
        goto L40
    L32:
        if (r18.getRendererType(r12) != (-2)) goto L34;
    L35:
        r15 = true;
        goto L37
    L34:
        if (r8[r12] == null) goto L36;
    L26:
        if (r0.disabledTrackTypes.contains(Integer.valueOf(r13)) == true) goto L29;
        r11 = false;
        goto L30
    L42:
        if (r0.tunnelingEnabled == false) goto L45;
        maybeConfigureRenderersForTunneling(r18, r19, r10, r8);
    L45:
        if (r0.audioOffloadPreferences.audioOffloadMode == 0) goto L48;
        maybeConfigureRendererForOffload(r0, r18, r19, r10, r8);
    L48:
        return Pair.create(r10, r8);
    L7:
        if (Util.SDK_INT < 32) goto L11;
        if (this.spatializer == null) goto L11;
        this.spatializer.ensureInitialized(this, (Looper) Assertions.checkStateNotNull(Looper.myLooper()));     // Catch: Throwable -> L49
    L49:
        th = th;
    L55:
        throw th;
    L53:
        th = th;
        goto L55
    }

    protected Pair<ExoTrackSelection.Definition, Integer> selectVideoTrack(MappingTrackSelector.MappedTrackInfo r8, int[][][] r9, final int[] r10, final Parameters r11) throws ExoPlaybackException {
        if (r11.audioOffloadPreferences.audioOffloadMode != 2) goto L7;
        return null;
    L7:
        return selectTracksForType(2, r8, r9, new DefaultTrackSelector$$ExternalSyntheticLambda6(r11, r10), new DefaultTrackSelector$$ExternalSyntheticLambda7());
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public void setAudioAttributes(AudioAttributes r3) {
        Object r0 = this.lock;
        monitor-enter(r0);
    L13:
        th = move-exception;
        throw th;
    L5:
        if (this.audioAttributes.equals(r3) == true) goto L7;
        boolean r1 = true;
    L8:
        this.audioAttributes = r3;     // Catch: Throwable -> L13
        monitor-exit(r0);     // Catch: Throwable -> L13
        if (r1 == false) goto L18;
        maybeInvalidateForAudioChannelCountConstraints();
        return;
    L18:
        return;
    L7:
        r1 = false;
        goto L8
    }

    @Override // androidx.media3.exoplayer.trackselection.TrackSelector
    public void setParameters(TrackSelectionParameters r4) {
        if ((r4 instanceof Parameters) == false) goto L5;
        setParametersInternal((Parameters) r4);
    L5:
        setParametersInternal(new Parameters.Builder(getParameters(), null).set(r4).build());
    }

    public void setParameters(Parameters.Builder r2) {
        setParametersInternal(r2.build());
    }

    @Deprecated
    public void setParameters(ParametersBuilder r2) {
        setParametersInternal(r2.build());
    }
}
