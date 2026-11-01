package androidx.media3.common;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Looper;
import android.view.accessibility.CaptioningManager;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

/* JADX INFO: loaded from: classes.dex */
public class TrackSelectionParameters {

    @Deprecated
    public static final TrackSelectionParameters DEFAULT = null;
    public static final TrackSelectionParameters DEFAULT_WITHOUT_CONTEXT = null;
    private static final String FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE = null;
    private static final String FIELD_AUDIO_OFFLOAD_PREFERENCES = null;
    protected static final int FIELD_CUSTOM_ID_BASE = 1000;
    private static final String FIELD_DISABLED_TRACK_TYPE = null;
    private static final String FIELD_FORCE_HIGHEST_SUPPORTED_BITRATE = null;
    private static final String FIELD_FORCE_LOWEST_BITRATE = null;
    private static final String FIELD_IGNORED_TEXT_SELECTION_FLAGS = null;
    private static final String FIELD_IS_GAPLESS_SUPPORT_REQUIRED = null;
    private static final String FIELD_IS_PREFER_IMAGE_OVER_VIDEO_ENABLED = null;
    private static final String FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED = null;
    private static final String FIELD_MAX_AUDIO_BITRATE = null;
    private static final String FIELD_MAX_AUDIO_CHANNEL_COUNT = null;
    private static final String FIELD_MAX_VIDEO_BITRATE = null;
    private static final String FIELD_MAX_VIDEO_FRAMERATE = null;
    private static final String FIELD_MAX_VIDEO_HEIGHT = null;
    private static final String FIELD_MAX_VIDEO_WIDTH = null;
    private static final String FIELD_MIN_VIDEO_BITRATE = null;
    private static final String FIELD_MIN_VIDEO_FRAMERATE = null;
    private static final String FIELD_MIN_VIDEO_HEIGHT = null;
    private static final String FIELD_MIN_VIDEO_WIDTH = null;
    private static final String FIELD_PREFERRED_AUDIO_LANGUAGES = null;
    private static final String FIELD_PREFERRED_AUDIO_MIME_TYPES = null;
    private static final String FIELD_PREFERRED_AUDIO_ROLE_FLAGS = null;
    private static final String FIELD_PREFERRED_TEXT_LANGUAGES = null;
    private static final String FIELD_PREFERRED_TEXT_ROLE_FLAGS = null;
    private static final String FIELD_PREFERRED_VIDEO_MIMETYPES = null;
    private static final String FIELD_PREFERRED_VIDEO_ROLE_FLAGS = null;
    private static final String FIELD_SELECTION_OVERRIDES = null;
    private static final String FIELD_SELECT_UNDETERMINED_TEXT_LANGUAGE = null;
    private static final String FIELD_VIEWPORT_HEIGHT = null;
    private static final String FIELD_VIEWPORT_ORIENTATION_MAY_CHANGE = null;
    private static final String FIELD_VIEWPORT_WIDTH = null;
    public final AudioOffloadPreferences audioOffloadPreferences;
    public final ImmutableSet<Integer> disabledTrackTypes;
    public final boolean forceHighestSupportedBitrate;
    public final boolean forceLowestBitrate;
    public final int ignoredTextSelectionFlags;
    public final boolean isPrioritizeImageOverVideoEnabled;
    public final int maxAudioBitrate;
    public final int maxAudioChannelCount;
    public final int maxVideoBitrate;
    public final int maxVideoFrameRate;
    public final int maxVideoHeight;
    public final int maxVideoWidth;
    public final int minVideoBitrate;
    public final int minVideoFrameRate;
    public final int minVideoHeight;
    public final int minVideoWidth;
    public final ImmutableMap<TrackGroup, TrackSelectionOverride> overrides;
    public final ImmutableList<String> preferredAudioLanguages;
    public final ImmutableList<String> preferredAudioMimeTypes;
    public final int preferredAudioRoleFlags;
    public final ImmutableList<String> preferredTextLanguages;
    public final int preferredTextRoleFlags;
    public final ImmutableList<String> preferredVideoMimeTypes;
    public final int preferredVideoRoleFlags;
    public final boolean selectUndeterminedTextLanguage;
    public final int viewportHeight;
    public final boolean viewportOrientationMayChange;
    public final int viewportWidth;

    /* JADX INFO: renamed from: androidx.media3.common.TrackSelectionParameters$1 */
    static /* synthetic */ class C01971 {
    }

    public static final class AudioOffloadPreferences {
        public static final int AUDIO_OFFLOAD_MODE_DISABLED = 0;
        public static final int AUDIO_OFFLOAD_MODE_ENABLED = 1;
        public static final int AUDIO_OFFLOAD_MODE_REQUIRED = 2;
        public static final AudioOffloadPreferences DEFAULT = null;
        private static final String FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE = null;
        private static final String FIELD_IS_GAPLESS_SUPPORT_REQUIRED = null;
        private static final String FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED = null;
        public final int audioOffloadMode;
        public final boolean isGaplessSupportRequired;
        public final boolean isSpeedChangeSupportRequired;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface AudioOffloadMode {
        }

        public static final class Builder {
            private int audioOffloadMode;
            private boolean isGaplessSupportRequired;
            private boolean isSpeedChangeSupportRequired;

            public Builder() {
                this.audioOffloadMode = 0;
                this.isGaplessSupportRequired = false;
                this.isSpeedChangeSupportRequired = false;
            }

            static /* synthetic */ int access$3200(Builder r1) {
                return r1.audioOffloadMode;
            }

            static /* synthetic */ boolean access$3300(Builder r1) {
                return r1.isGaplessSupportRequired;
            }

            static /* synthetic */ boolean access$3400(Builder r1) {
                return r1.isSpeedChangeSupportRequired;
            }

            public AudioOffloadPreferences build() {
                return new AudioOffloadPreferences(this, null);
            }

            public Builder setAudioOffloadMode(int r1) {
                this.audioOffloadMode = r1;
                return this;
            }

            public Builder setIsGaplessSupportRequired(boolean r1) {
                this.isGaplessSupportRequired = r1;
                return this;
            }

            public Builder setIsSpeedChangeSupportRequired(boolean r1) {
                this.isSpeedChangeSupportRequired = r1;
                return this;
            }
        }

        static {
            DEFAULT = new Builder().build();
            FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE = Util.intToStringMaxRadix(1);
            FIELD_IS_GAPLESS_SUPPORT_REQUIRED = Util.intToStringMaxRadix(2);
            FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED = Util.intToStringMaxRadix(3);
        }

        private AudioOffloadPreferences(Builder r2) {
            this.audioOffloadMode = Builder.access$3200(r2);
            this.isGaplessSupportRequired = Builder.access$3300(r2);
            this.isSpeedChangeSupportRequired = Builder.access$3400(r2);
        }

        /* synthetic */ AudioOffloadPreferences(Builder r1, C01971 r2) {
            this(r1);
        }

        public static AudioOffloadPreferences fromBundle(Bundle r3) {
            return new Builder().setAudioOffloadMode(r3.getInt(FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE, DEFAULT.audioOffloadMode)).setIsGaplessSupportRequired(r3.getBoolean(FIELD_IS_GAPLESS_SUPPORT_REQUIRED, DEFAULT.isGaplessSupportRequired)).setIsSpeedChangeSupportRequired(r3.getBoolean(FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED, DEFAULT.isSpeedChangeSupportRequired)).build();
        }

        public Builder buildUpon() {
            return new Builder().setAudioOffloadMode(this.audioOffloadMode).setIsGaplessSupportRequired(this.isGaplessSupportRequired).setIsSpeedChangeSupportRequired(this.isSpeedChangeSupportRequired);
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
            AudioOffloadPreferences r2 = (AudioOffloadPreferences) r6;
            if (this.audioOffloadMode != r2.audioOffloadMode) goto L18;
            if (this.isGaplessSupportRequired != r2.isGaplessSupportRequired) goto L18;
            if (this.isSpeedChangeSupportRequired != r2.isSpeedChangeSupportRequired) goto L18;
            return true;
        L18:
            return false;
        }

        public int hashCode() {
            return (((((1 * 31) + this.audioOffloadMode) * 31) + (this.isGaplessSupportRequired ? 1 : 0)) * 31) + (this.isSpeedChangeSupportRequired ? 1 : 0);
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putInt(FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE, this.audioOffloadMode);
            r0.putBoolean(FIELD_IS_GAPLESS_SUPPORT_REQUIRED, this.isGaplessSupportRequired);
            r0.putBoolean(FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED, this.isSpeedChangeSupportRequired);
            return r0;
        }
    }

    public static class Builder {
        private AudioOffloadPreferences audioOffloadPreferences;
        private HashSet<Integer> disabledTrackTypes;
        private boolean forceHighestSupportedBitrate;
        private boolean forceLowestBitrate;
        private int ignoredTextSelectionFlags;
        private boolean isPrioritizeImageOverVideoEnabled;
        private int maxAudioBitrate;
        private int maxAudioChannelCount;
        private int maxVideoBitrate;
        private int maxVideoFrameRate;
        private int maxVideoHeight;
        private int maxVideoWidth;
        private int minVideoBitrate;
        private int minVideoFrameRate;
        private int minVideoHeight;
        private int minVideoWidth;
        private HashMap<TrackGroup, TrackSelectionOverride> overrides;
        private ImmutableList<String> preferredAudioLanguages;
        private ImmutableList<String> preferredAudioMimeTypes;
        private int preferredAudioRoleFlags;
        private ImmutableList<String> preferredTextLanguages;
        private int preferredTextRoleFlags;
        private ImmutableList<String> preferredVideoMimeTypes;
        private int preferredVideoRoleFlags;
        private boolean selectUndeterminedTextLanguage;
        private int viewportHeight;
        private boolean viewportOrientationMayChange;
        private int viewportWidth;

        @Deprecated
        public Builder() {
            this.maxVideoWidth = Integer.MAX_VALUE;
            this.maxVideoHeight = Integer.MAX_VALUE;
            this.maxVideoFrameRate = Integer.MAX_VALUE;
            this.maxVideoBitrate = Integer.MAX_VALUE;
            this.viewportWidth = Integer.MAX_VALUE;
            this.viewportHeight = Integer.MAX_VALUE;
            this.viewportOrientationMayChange = true;
            this.preferredVideoMimeTypes = ImmutableList.m212of();
            this.preferredVideoRoleFlags = 0;
            this.preferredAudioLanguages = ImmutableList.m212of();
            this.preferredAudioRoleFlags = 0;
            this.maxAudioChannelCount = Integer.MAX_VALUE;
            this.maxAudioBitrate = Integer.MAX_VALUE;
            this.preferredAudioMimeTypes = ImmutableList.m212of();
            this.audioOffloadPreferences = AudioOffloadPreferences.DEFAULT;
            this.preferredTextLanguages = ImmutableList.m212of();
            this.preferredTextRoleFlags = 0;
            this.ignoredTextSelectionFlags = 0;
            this.selectUndeterminedTextLanguage = false;
            this.isPrioritizeImageOverVideoEnabled = false;
            this.forceLowestBitrate = false;
            this.forceHighestSupportedBitrate = false;
            this.overrides = new HashMap();
            this.disabledTrackTypes = new HashSet();
        }

        public Builder(Context r2) {
            this();
            setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(r2);
            setViewportSizeToPhysicalDisplaySize(r2, true);
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected Builder(Bundle r10) {
            this.maxVideoWidth = r10.getInt(TrackSelectionParameters.access$000(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.maxVideoWidth);
            this.maxVideoHeight = r10.getInt(TrackSelectionParameters.access$100(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.maxVideoHeight);
            this.maxVideoFrameRate = r10.getInt(TrackSelectionParameters.access$200(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.maxVideoFrameRate);
            this.maxVideoBitrate = r10.getInt(TrackSelectionParameters.access$300(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.maxVideoBitrate);
            this.minVideoWidth = r10.getInt(TrackSelectionParameters.access$400(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.minVideoWidth);
            this.minVideoHeight = r10.getInt(TrackSelectionParameters.access$500(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.minVideoHeight);
            this.minVideoFrameRate = r10.getInt(TrackSelectionParameters.access$600(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.minVideoFrameRate);
            this.minVideoBitrate = r10.getInt(TrackSelectionParameters.access$700(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.minVideoBitrate);
            this.viewportWidth = r10.getInt(TrackSelectionParameters.access$800(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.viewportWidth);
            this.viewportHeight = r10.getInt(TrackSelectionParameters.access$900(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.viewportHeight);
            this.viewportOrientationMayChange = r10.getBoolean(TrackSelectionParameters.access$1000(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.viewportOrientationMayChange);
            int r1 = 0;
            this.preferredVideoMimeTypes = ImmutableList.copyOf((String[]) MoreObjects.firstNonNull(r10.getStringArray(TrackSelectionParameters.access$1100()), new String[0]));
            this.preferredVideoRoleFlags = r10.getInt(TrackSelectionParameters.access$1200(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.preferredVideoRoleFlags);
            this.preferredAudioLanguages = normalizeLanguageCodes((String[]) MoreObjects.firstNonNull(r10.getStringArray(TrackSelectionParameters.access$1300()), new String[0]));
            this.preferredAudioRoleFlags = r10.getInt(TrackSelectionParameters.access$1400(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.preferredAudioRoleFlags);
            this.maxAudioChannelCount = r10.getInt(TrackSelectionParameters.access$1500(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.maxAudioChannelCount);
            this.maxAudioBitrate = r10.getInt(TrackSelectionParameters.access$1600(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.maxAudioBitrate);
            this.preferredAudioMimeTypes = ImmutableList.copyOf((String[]) MoreObjects.firstNonNull(r10.getStringArray(TrackSelectionParameters.access$1700()), new String[0]));
            this.audioOffloadPreferences = getAudioOffloadPreferencesFromBundle(r10);
            this.preferredTextLanguages = normalizeLanguageCodes((String[]) MoreObjects.firstNonNull(r10.getStringArray(TrackSelectionParameters.access$1800()), new String[0]));
            this.preferredTextRoleFlags = r10.getInt(TrackSelectionParameters.access$1900(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.preferredTextRoleFlags);
            this.ignoredTextSelectionFlags = r10.getInt(TrackSelectionParameters.access$2000(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.ignoredTextSelectionFlags);
            this.selectUndeterminedTextLanguage = r10.getBoolean(TrackSelectionParameters.access$2100(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.selectUndeterminedTextLanguage);
            this.isPrioritizeImageOverVideoEnabled = r10.getBoolean(TrackSelectionParameters.access$2200(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.isPrioritizeImageOverVideoEnabled);
            this.forceLowestBitrate = r10.getBoolean(TrackSelectionParameters.access$2300(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.forceLowestBitrate);
            this.forceHighestSupportedBitrate = r10.getBoolean(TrackSelectionParameters.access$2400(), TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT.forceHighestSupportedBitrate);
            ArrayList r2 = r10.getParcelableArrayList(TrackSelectionParameters.access$2500());
            if (r2 != null) goto L5;
            ImmutableList r3 = ImmutableList.m212of();
        L6:
            this.overrides = new HashMap();
            int r4 = 0;
        L8:
            if (r4 >= r3.size()) goto L10;
            TrackSelectionOverride r5 = (TrackSelectionOverride) r3.get(r4);
            this.overrides.put(r5.mediaTrackGroup, r5);
            r4 = r4 + 1;
            goto L8
        L10:
            int[] r6 = (int[]) MoreObjects.firstNonNull(r10.getIntArray(TrackSelectionParameters.access$2600()), new int[0]);
            this.disabledTrackTypes = new HashSet();
            int r7 = r6.length;
        L11:
            if (r1 >= r7) goto L13;
            int r8 = r6[r1];
            this.disabledTrackTypes.add(Integer.valueOf(r8));
            r1 = r1 + 1;
            goto L11
        L13:
            return;
        L5:
            r3 = BundleCollectionUtil.fromBundleList(new TrackSelectionParameters$Builder$$ExternalSyntheticLambda0(), r2);
            goto L6
        }

        protected Builder(TrackSelectionParameters r1) {
            init(r1);
        }

        static /* synthetic */ int access$3500(Builder r1) {
            return r1.maxVideoWidth;
        }

        static /* synthetic */ int access$3600(Builder r1) {
            return r1.maxVideoHeight;
        }

        static /* synthetic */ int access$3700(Builder r1) {
            return r1.maxVideoFrameRate;
        }

        static /* synthetic */ int access$3800(Builder r1) {
            return r1.maxVideoBitrate;
        }

        static /* synthetic */ int access$3900(Builder r1) {
            return r1.minVideoWidth;
        }

        static /* synthetic */ int access$4000(Builder r1) {
            return r1.minVideoHeight;
        }

        static /* synthetic */ int access$4100(Builder r1) {
            return r1.minVideoFrameRate;
        }

        static /* synthetic */ int access$4200(Builder r1) {
            return r1.minVideoBitrate;
        }

        static /* synthetic */ int access$4300(Builder r1) {
            return r1.viewportWidth;
        }

        static /* synthetic */ int access$4400(Builder r1) {
            return r1.viewportHeight;
        }

        static /* synthetic */ boolean access$4500(Builder r1) {
            return r1.viewportOrientationMayChange;
        }

        static /* synthetic */ ImmutableList access$4600(Builder r1) {
            return r1.preferredVideoMimeTypes;
        }

        static /* synthetic */ int access$4700(Builder r1) {
            return r1.preferredVideoRoleFlags;
        }

        static /* synthetic */ ImmutableList access$4800(Builder r1) {
            return r1.preferredAudioLanguages;
        }

        static /* synthetic */ int access$4900(Builder r1) {
            return r1.preferredAudioRoleFlags;
        }

        static /* synthetic */ int access$5000(Builder r1) {
            return r1.maxAudioChannelCount;
        }

        static /* synthetic */ int access$5100(Builder r1) {
            return r1.maxAudioBitrate;
        }

        static /* synthetic */ ImmutableList access$5200(Builder r1) {
            return r1.preferredAudioMimeTypes;
        }

        static /* synthetic */ AudioOffloadPreferences access$5300(Builder r1) {
            return r1.audioOffloadPreferences;
        }

        static /* synthetic */ ImmutableList access$5400(Builder r1) {
            return r1.preferredTextLanguages;
        }

        static /* synthetic */ int access$5500(Builder r1) {
            return r1.preferredTextRoleFlags;
        }

        static /* synthetic */ int access$5600(Builder r1) {
            return r1.ignoredTextSelectionFlags;
        }

        static /* synthetic */ boolean access$5700(Builder r1) {
            return r1.selectUndeterminedTextLanguage;
        }

        static /* synthetic */ boolean access$5800(Builder r1) {
            return r1.isPrioritizeImageOverVideoEnabled;
        }

        static /* synthetic */ boolean access$5900(Builder r1) {
            return r1.forceLowestBitrate;
        }

        static /* synthetic */ boolean access$6000(Builder r1) {
            return r1.forceHighestSupportedBitrate;
        }

        static /* synthetic */ HashMap access$6100(Builder r1) {
            return r1.overrides;
        }

        static /* synthetic */ HashSet access$6200(Builder r1) {
            return r1.disabledTrackTypes;
        }

        private static AudioOffloadPreferences getAudioOffloadPreferencesFromBundle(Bundle r4) {
            Bundle r0 = r4.getBundle(TrackSelectionParameters.access$2700());
            if (r0 == null) goto L6;
            return AudioOffloadPreferences.fromBundle(r0);
        L6:
            return new AudioOffloadPreferences.Builder().setAudioOffloadMode(r4.getInt(TrackSelectionParameters.access$3000(), AudioOffloadPreferences.DEFAULT.audioOffloadMode)).setIsGaplessSupportRequired(r4.getBoolean(TrackSelectionParameters.access$2900(), AudioOffloadPreferences.DEFAULT.isGaplessSupportRequired)).setIsSpeedChangeSupportRequired(r4.getBoolean(TrackSelectionParameters.access$2800(), AudioOffloadPreferences.DEFAULT.isSpeedChangeSupportRequired)).build();
        }

        @EnsuresNonNull({"preferredVideoMimeTypes", "preferredAudioLanguages", "preferredAudioMimeTypes", "audioOffloadPreferences", "preferredTextLanguages", "overrides", "disabledTrackTypes"})
        private void init(TrackSelectionParameters r3) {
            this.maxVideoWidth = r3.maxVideoWidth;
            this.maxVideoHeight = r3.maxVideoHeight;
            this.maxVideoFrameRate = r3.maxVideoFrameRate;
            this.maxVideoBitrate = r3.maxVideoBitrate;
            this.minVideoWidth = r3.minVideoWidth;
            this.minVideoHeight = r3.minVideoHeight;
            this.minVideoFrameRate = r3.minVideoFrameRate;
            this.minVideoBitrate = r3.minVideoBitrate;
            this.viewportWidth = r3.viewportWidth;
            this.viewportHeight = r3.viewportHeight;
            this.viewportOrientationMayChange = r3.viewportOrientationMayChange;
            this.preferredVideoMimeTypes = r3.preferredVideoMimeTypes;
            this.preferredVideoRoleFlags = r3.preferredVideoRoleFlags;
            this.preferredAudioLanguages = r3.preferredAudioLanguages;
            this.preferredAudioRoleFlags = r3.preferredAudioRoleFlags;
            this.maxAudioChannelCount = r3.maxAudioChannelCount;
            this.maxAudioBitrate = r3.maxAudioBitrate;
            this.preferredAudioMimeTypes = r3.preferredAudioMimeTypes;
            this.audioOffloadPreferences = r3.audioOffloadPreferences;
            this.preferredTextLanguages = r3.preferredTextLanguages;
            this.preferredTextRoleFlags = r3.preferredTextRoleFlags;
            this.ignoredTextSelectionFlags = r3.ignoredTextSelectionFlags;
            this.selectUndeterminedTextLanguage = r3.selectUndeterminedTextLanguage;
            this.isPrioritizeImageOverVideoEnabled = r3.isPrioritizeImageOverVideoEnabled;
            this.forceLowestBitrate = r3.forceLowestBitrate;
            this.forceHighestSupportedBitrate = r3.forceHighestSupportedBitrate;
            this.disabledTrackTypes = new HashSet(r3.disabledTrackTypes);
            this.overrides = new HashMap(r3.overrides);
        }

        private static ImmutableList<String> normalizeLanguageCodes(String[] r6) {
            ImmutableList.Builder r0 = ImmutableList.builder();
            String[] r1 = (String[]) Assertions.checkNotNull(r6);
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            r0.add(Util.normalizeLanguageCode((String) Assertions.checkNotNull(r1[r3])));
            r3 = r3 + 1;
            goto L3
        L6:
            return r0.build();
        }

        public Builder addOverride(TrackSelectionOverride r3) {
            this.overrides.put(r3.mediaTrackGroup, r3);
            return this;
        }

        public TrackSelectionParameters build() {
            return new TrackSelectionParameters(this);
        }

        public Builder clearOverride(TrackGroup r2) {
            this.overrides.remove(r2);
            return this;
        }

        public Builder clearOverrides() {
            this.overrides.clear();
            return this;
        }

        public Builder clearOverridesOfType(int r4) {
            Iterator<TrackSelectionOverride> r0 = this.overrides.values().iterator();
        L4:
            if (r0.hasNext() == false) goto L9;
            if (r0.next().getType() != r4) goto L4;
            r0.remove();
            goto L4
        L9:
            return this;
        }

        public Builder clearVideoSizeConstraints() {
            return setMaxVideoSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public Builder clearViewportSizeConstraints() {
            return setViewportSize(Integer.MAX_VALUE, Integer.MAX_VALUE, true);
        }

        protected Builder set(TrackSelectionParameters r1) {
            init(r1);
            return this;
        }

        public Builder setAudioOffloadPreferences(AudioOffloadPreferences r1) {
            this.audioOffloadPreferences = r1;
            return this;
        }

        @Deprecated
        public Builder setDisabledTrackTypes(Set<Integer> r2) {
            this.disabledTrackTypes.clear();
            this.disabledTrackTypes.addAll(r2);
            return this;
        }

        public Builder setForceHighestSupportedBitrate(boolean r1) {
            this.forceHighestSupportedBitrate = r1;
            return this;
        }

        public Builder setForceLowestBitrate(boolean r1) {
            this.forceLowestBitrate = r1;
            return this;
        }

        public Builder setIgnoredTextSelectionFlags(int r1) {
            this.ignoredTextSelectionFlags = r1;
            return this;
        }

        public Builder setMaxAudioBitrate(int r1) {
            this.maxAudioBitrate = r1;
            return this;
        }

        public Builder setMaxAudioChannelCount(int r1) {
            this.maxAudioChannelCount = r1;
            return this;
        }

        public Builder setMaxVideoBitrate(int r1) {
            this.maxVideoBitrate = r1;
            return this;
        }

        public Builder setMaxVideoFrameRate(int r1) {
            this.maxVideoFrameRate = r1;
            return this;
        }

        public Builder setMaxVideoSize(int r1, int r2) {
            this.maxVideoWidth = r1;
            this.maxVideoHeight = r2;
            return this;
        }

        public Builder setMaxVideoSizeSd() {
            return setMaxVideoSize(AdaptiveTrackSelection.DEFAULT_MAX_WIDTH_TO_DISCARD, AdaptiveTrackSelection.DEFAULT_MAX_HEIGHT_TO_DISCARD);
        }

        public Builder setMinVideoBitrate(int r1) {
            this.minVideoBitrate = r1;
            return this;
        }

        public Builder setMinVideoFrameRate(int r1) {
            this.minVideoFrameRate = r1;
            return this;
        }

        public Builder setMinVideoSize(int r1, int r2) {
            this.minVideoWidth = r1;
            this.minVideoHeight = r2;
            return this;
        }

        public Builder setOverrideForType(TrackSelectionOverride r3) {
            clearOverridesOfType(r3.getType());
            this.overrides.put(r3.mediaTrackGroup, r3);
            return this;
        }

        public Builder setPreferredAudioLanguage(String r2) {
            if (r2 != null) goto L5;
            return setPreferredAudioLanguages(new String[0]);
        L5:
            return setPreferredAudioLanguages(new String[]{r2});
        }

        public Builder setPreferredAudioLanguages(String... r2) {
            this.preferredAudioLanguages = normalizeLanguageCodes(r2);
            return this;
        }

        public Builder setPreferredAudioMimeType(String r2) {
            if (r2 != null) goto L5;
            return setPreferredAudioMimeTypes(new String[0]);
        L5:
            return setPreferredAudioMimeTypes(new String[]{r2});
        }

        public Builder setPreferredAudioMimeTypes(String... r2) {
            this.preferredAudioMimeTypes = ImmutableList.copyOf(r2);
            return this;
        }

        public Builder setPreferredAudioRoleFlags(int r1) {
            this.preferredAudioRoleFlags = r1;
            return this;
        }

        public Builder setPreferredTextLanguage(String r2) {
            if (r2 != null) goto L5;
            return setPreferredTextLanguages(new String[0]);
        L5:
            return setPreferredTextLanguages(new String[]{r2});
        }

        public Builder setPreferredTextLanguageAndRoleFlagsToCaptioningManagerSettings(Context r4) {
            if (Util.SDK_INT < 23) goto L5;
        L7:
            CaptioningManager r0 = (CaptioningManager) r4.getSystemService("captioning");
            if (r0 != null) goto L10;
        L16:
            return this;
        L10:
            if (r0.isEnabled() == false) goto L16;
            this.preferredTextRoleFlags = 1088;
            Locale r1 = r0.getLocale();
            if (r1 == null) goto L15;
            this.preferredTextLanguages = ImmutableList.m213of(Util.getLocaleLanguageTag(r1));
        L15:
            return this;
        L5:
            if (Looper.myLooper() != null) goto L7;
            return this;
        }

        public Builder setPreferredTextLanguages(String... r2) {
            this.preferredTextLanguages = normalizeLanguageCodes(r2);
            return this;
        }

        public Builder setPreferredTextRoleFlags(int r1) {
            this.preferredTextRoleFlags = r1;
            return this;
        }

        public Builder setPreferredVideoMimeType(String r2) {
            if (r2 != null) goto L5;
            return setPreferredVideoMimeTypes(new String[0]);
        L5:
            return setPreferredVideoMimeTypes(new String[]{r2});
        }

        public Builder setPreferredVideoMimeTypes(String... r2) {
            this.preferredVideoMimeTypes = ImmutableList.copyOf(r2);
            return this;
        }

        public Builder setPreferredVideoRoleFlags(int r1) {
            this.preferredVideoRoleFlags = r1;
            return this;
        }

        public Builder setPrioritizeImageOverVideoEnabled(boolean r1) {
            this.isPrioritizeImageOverVideoEnabled = r1;
            return this;
        }

        public Builder setSelectUndeterminedTextLanguage(boolean r1) {
            this.selectUndeterminedTextLanguage = r1;
            return this;
        }

        public Builder setTrackTypeDisabled(int r3, boolean r4) {
            if (r4 == false) goto L4;
            this.disabledTrackTypes.add(Integer.valueOf(r3));
        L5:
            return this;
        L4:
            this.disabledTrackTypes.remove(Integer.valueOf(r3));
            goto L5
        }

        public Builder setViewportSize(int r1, int r2, boolean r3) {
            this.viewportWidth = r1;
            this.viewportHeight = r2;
            this.viewportOrientationMayChange = r3;
            return this;
        }

        public Builder setViewportSizeToPhysicalDisplaySize(Context r4, boolean r5) {
            Point r0 = Util.getCurrentDisplayModeSize(r4);
            return setViewportSize(r0.x, r0.y, r5);
        }
    }

    static {
        DEFAULT_WITHOUT_CONTEXT = new Builder().build();
        DEFAULT = DEFAULT_WITHOUT_CONTEXT;
        FIELD_PREFERRED_AUDIO_LANGUAGES = Util.intToStringMaxRadix(1);
        FIELD_PREFERRED_AUDIO_ROLE_FLAGS = Util.intToStringMaxRadix(2);
        FIELD_PREFERRED_TEXT_LANGUAGES = Util.intToStringMaxRadix(3);
        FIELD_PREFERRED_TEXT_ROLE_FLAGS = Util.intToStringMaxRadix(4);
        FIELD_SELECT_UNDETERMINED_TEXT_LANGUAGE = Util.intToStringMaxRadix(5);
        FIELD_MAX_VIDEO_WIDTH = Util.intToStringMaxRadix(6);
        FIELD_MAX_VIDEO_HEIGHT = Util.intToStringMaxRadix(7);
        FIELD_MAX_VIDEO_FRAMERATE = Util.intToStringMaxRadix(8);
        FIELD_MAX_VIDEO_BITRATE = Util.intToStringMaxRadix(9);
        FIELD_MIN_VIDEO_WIDTH = Util.intToStringMaxRadix(10);
        FIELD_MIN_VIDEO_HEIGHT = Util.intToStringMaxRadix(11);
        FIELD_MIN_VIDEO_FRAMERATE = Util.intToStringMaxRadix(12);
        FIELD_MIN_VIDEO_BITRATE = Util.intToStringMaxRadix(13);
        FIELD_VIEWPORT_WIDTH = Util.intToStringMaxRadix(14);
        FIELD_VIEWPORT_HEIGHT = Util.intToStringMaxRadix(15);
        FIELD_VIEWPORT_ORIENTATION_MAY_CHANGE = Util.intToStringMaxRadix(16);
        FIELD_PREFERRED_VIDEO_MIMETYPES = Util.intToStringMaxRadix(17);
        FIELD_MAX_AUDIO_CHANNEL_COUNT = Util.intToStringMaxRadix(18);
        FIELD_MAX_AUDIO_BITRATE = Util.intToStringMaxRadix(19);
        FIELD_PREFERRED_AUDIO_MIME_TYPES = Util.intToStringMaxRadix(20);
        FIELD_FORCE_LOWEST_BITRATE = Util.intToStringMaxRadix(21);
        FIELD_FORCE_HIGHEST_SUPPORTED_BITRATE = Util.intToStringMaxRadix(22);
        FIELD_SELECTION_OVERRIDES = Util.intToStringMaxRadix(23);
        FIELD_DISABLED_TRACK_TYPE = Util.intToStringMaxRadix(24);
        FIELD_PREFERRED_VIDEO_ROLE_FLAGS = Util.intToStringMaxRadix(25);
        FIELD_IGNORED_TEXT_SELECTION_FLAGS = Util.intToStringMaxRadix(26);
        FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE = Util.intToStringMaxRadix(27);
        FIELD_IS_GAPLESS_SUPPORT_REQUIRED = Util.intToStringMaxRadix(28);
        FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED = Util.intToStringMaxRadix(29);
        FIELD_AUDIO_OFFLOAD_PREFERENCES = Util.intToStringMaxRadix(30);
        FIELD_IS_PREFER_IMAGE_OVER_VIDEO_ENABLED = Util.intToStringMaxRadix(31);
    }

    protected TrackSelectionParameters(Builder r2) {
        this.maxVideoWidth = Builder.access$3500(r2);
        this.maxVideoHeight = Builder.access$3600(r2);
        this.maxVideoFrameRate = Builder.access$3700(r2);
        this.maxVideoBitrate = Builder.access$3800(r2);
        this.minVideoWidth = Builder.access$3900(r2);
        this.minVideoHeight = Builder.access$4000(r2);
        this.minVideoFrameRate = Builder.access$4100(r2);
        this.minVideoBitrate = Builder.access$4200(r2);
        this.viewportWidth = Builder.access$4300(r2);
        this.viewportHeight = Builder.access$4400(r2);
        this.viewportOrientationMayChange = Builder.access$4500(r2);
        this.preferredVideoMimeTypes = Builder.access$4600(r2);
        this.preferredVideoRoleFlags = Builder.access$4700(r2);
        this.preferredAudioLanguages = Builder.access$4800(r2);
        this.preferredAudioRoleFlags = Builder.access$4900(r2);
        this.maxAudioChannelCount = Builder.access$5000(r2);
        this.maxAudioBitrate = Builder.access$5100(r2);
        this.preferredAudioMimeTypes = Builder.access$5200(r2);
        this.audioOffloadPreferences = Builder.access$5300(r2);
        this.preferredTextLanguages = Builder.access$5400(r2);
        this.preferredTextRoleFlags = Builder.access$5500(r2);
        this.ignoredTextSelectionFlags = Builder.access$5600(r2);
        this.selectUndeterminedTextLanguage = Builder.access$5700(r2);
        this.isPrioritizeImageOverVideoEnabled = Builder.access$5800(r2);
        this.forceLowestBitrate = Builder.access$5900(r2);
        this.forceHighestSupportedBitrate = Builder.access$6000(r2);
        this.overrides = ImmutableMap.copyOf(Builder.access$6100(r2));
        this.disabledTrackTypes = ImmutableSet.copyOf(Builder.access$6200(r2));
    }

    static /* synthetic */ String access$000() {
        return FIELD_MAX_VIDEO_WIDTH;
    }

    static /* synthetic */ String access$100() {
        return FIELD_MAX_VIDEO_HEIGHT;
    }

    static /* synthetic */ String access$1000() {
        return FIELD_VIEWPORT_ORIENTATION_MAY_CHANGE;
    }

    static /* synthetic */ String access$1100() {
        return FIELD_PREFERRED_VIDEO_MIMETYPES;
    }

    static /* synthetic */ String access$1200() {
        return FIELD_PREFERRED_VIDEO_ROLE_FLAGS;
    }

    static /* synthetic */ String access$1300() {
        return FIELD_PREFERRED_AUDIO_LANGUAGES;
    }

    static /* synthetic */ String access$1400() {
        return FIELD_PREFERRED_AUDIO_ROLE_FLAGS;
    }

    static /* synthetic */ String access$1500() {
        return FIELD_MAX_AUDIO_CHANNEL_COUNT;
    }

    static /* synthetic */ String access$1600() {
        return FIELD_MAX_AUDIO_BITRATE;
    }

    static /* synthetic */ String access$1700() {
        return FIELD_PREFERRED_AUDIO_MIME_TYPES;
    }

    static /* synthetic */ String access$1800() {
        return FIELD_PREFERRED_TEXT_LANGUAGES;
    }

    static /* synthetic */ String access$1900() {
        return FIELD_PREFERRED_TEXT_ROLE_FLAGS;
    }

    static /* synthetic */ String access$200() {
        return FIELD_MAX_VIDEO_FRAMERATE;
    }

    static /* synthetic */ String access$2000() {
        return FIELD_IGNORED_TEXT_SELECTION_FLAGS;
    }

    static /* synthetic */ String access$2100() {
        return FIELD_SELECT_UNDETERMINED_TEXT_LANGUAGE;
    }

    static /* synthetic */ String access$2200() {
        return FIELD_IS_PREFER_IMAGE_OVER_VIDEO_ENABLED;
    }

    static /* synthetic */ String access$2300() {
        return FIELD_FORCE_LOWEST_BITRATE;
    }

    static /* synthetic */ String access$2400() {
        return FIELD_FORCE_HIGHEST_SUPPORTED_BITRATE;
    }

    static /* synthetic */ String access$2500() {
        return FIELD_SELECTION_OVERRIDES;
    }

    static /* synthetic */ String access$2600() {
        return FIELD_DISABLED_TRACK_TYPE;
    }

    static /* synthetic */ String access$2700() {
        return FIELD_AUDIO_OFFLOAD_PREFERENCES;
    }

    static /* synthetic */ String access$2800() {
        return FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED;
    }

    static /* synthetic */ String access$2900() {
        return FIELD_IS_GAPLESS_SUPPORT_REQUIRED;
    }

    static /* synthetic */ String access$300() {
        return FIELD_MAX_VIDEO_BITRATE;
    }

    static /* synthetic */ String access$3000() {
        return FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE;
    }

    static /* synthetic */ String access$400() {
        return FIELD_MIN_VIDEO_WIDTH;
    }

    static /* synthetic */ String access$500() {
        return FIELD_MIN_VIDEO_HEIGHT;
    }

    static /* synthetic */ String access$600() {
        return FIELD_MIN_VIDEO_FRAMERATE;
    }

    static /* synthetic */ String access$700() {
        return FIELD_MIN_VIDEO_BITRATE;
    }

    static /* synthetic */ String access$800() {
        return FIELD_VIEWPORT_WIDTH;
    }

    static /* synthetic */ String access$900() {
        return FIELD_VIEWPORT_HEIGHT;
    }

    public static TrackSelectionParameters fromBundle(Bundle r1) {
        return new Builder(r1).build();
    }

    public static TrackSelectionParameters getDefaults(Context r1) {
        return new Builder(r1).build();
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L69:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L69;
        TrackSelectionParameters r2 = (TrackSelectionParameters) r6;
        if (this.maxVideoWidth != r2.maxVideoWidth) goto L68;
        if (this.maxVideoHeight != r2.maxVideoHeight) goto L68;
        if (this.maxVideoFrameRate != r2.maxVideoFrameRate) goto L68;
        if (this.maxVideoBitrate != r2.maxVideoBitrate) goto L68;
        if (this.minVideoWidth != r2.minVideoWidth) goto L68;
        if (this.minVideoHeight != r2.minVideoHeight) goto L68;
        if (this.minVideoFrameRate != r2.minVideoFrameRate) goto L68;
        if (this.minVideoBitrate != r2.minVideoBitrate) goto L68;
        if (this.viewportOrientationMayChange != r2.viewportOrientationMayChange) goto L68;
        if (this.viewportWidth != r2.viewportWidth) goto L68;
        if (this.viewportHeight != r2.viewportHeight) goto L68;
        if (this.preferredVideoMimeTypes.equals(r2.preferredVideoMimeTypes) == false) goto L68;
        if (this.preferredVideoRoleFlags != r2.preferredVideoRoleFlags) goto L68;
        if (this.preferredAudioLanguages.equals(r2.preferredAudioLanguages) == false) goto L68;
        if (this.preferredAudioRoleFlags != r2.preferredAudioRoleFlags) goto L68;
        if (this.maxAudioChannelCount != r2.maxAudioChannelCount) goto L68;
        if (this.maxAudioBitrate != r2.maxAudioBitrate) goto L68;
        if (this.preferredAudioMimeTypes.equals(r2.preferredAudioMimeTypes) == false) goto L68;
        if (this.audioOffloadPreferences.equals(r2.audioOffloadPreferences) == false) goto L68;
        if (this.preferredTextLanguages.equals(r2.preferredTextLanguages) == false) goto L68;
        if (this.preferredTextRoleFlags != r2.preferredTextRoleFlags) goto L68;
        if (this.ignoredTextSelectionFlags != r2.ignoredTextSelectionFlags) goto L68;
        if (this.selectUndeterminedTextLanguage != r2.selectUndeterminedTextLanguage) goto L68;
        if (this.isPrioritizeImageOverVideoEnabled != r2.isPrioritizeImageOverVideoEnabled) goto L68;
        if (this.forceLowestBitrate != r2.forceLowestBitrate) goto L68;
        if (this.forceHighestSupportedBitrate != r2.forceHighestSupportedBitrate) goto L68;
        if (this.overrides.equals(r2.overrides) == false) goto L68;
        if (this.disabledTrackTypes.equals(r2.disabledTrackTypes) == false) goto L68;
        return true;
    L68:
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((1 * 31) + this.maxVideoWidth) * 31) + this.maxVideoHeight) * 31) + this.maxVideoFrameRate) * 31) + this.maxVideoBitrate) * 31) + this.minVideoWidth) * 31) + this.minVideoHeight) * 31) + this.minVideoFrameRate) * 31) + this.minVideoBitrate) * 31) + (this.viewportOrientationMayChange ? 1 : 0)) * 31) + this.viewportWidth) * 31) + this.viewportHeight) * 31) + this.preferredVideoMimeTypes.hashCode()) * 31) + this.preferredVideoRoleFlags) * 31) + this.preferredAudioLanguages.hashCode()) * 31) + this.preferredAudioRoleFlags) * 31) + this.maxAudioChannelCount) * 31) + this.maxAudioBitrate) * 31) + this.preferredAudioMimeTypes.hashCode()) * 31) + this.audioOffloadPreferences.hashCode()) * 31) + this.preferredTextLanguages.hashCode()) * 31) + this.preferredTextRoleFlags) * 31) + this.ignoredTextSelectionFlags) * 31) + (this.selectUndeterminedTextLanguage ? 1 : 0)) * 31) + (this.isPrioritizeImageOverVideoEnabled ? 1 : 0)) * 31) + (this.forceLowestBitrate ? 1 : 0)) * 31) + (this.forceHighestSupportedBitrate ? 1 : 0)) * 31) + this.overrides.hashCode()) * 31) + this.disabledTrackTypes.hashCode();
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_MAX_VIDEO_WIDTH, this.maxVideoWidth);
        r0.putInt(FIELD_MAX_VIDEO_HEIGHT, this.maxVideoHeight);
        r0.putInt(FIELD_MAX_VIDEO_FRAMERATE, this.maxVideoFrameRate);
        r0.putInt(FIELD_MAX_VIDEO_BITRATE, this.maxVideoBitrate);
        r0.putInt(FIELD_MIN_VIDEO_WIDTH, this.minVideoWidth);
        r0.putInt(FIELD_MIN_VIDEO_HEIGHT, this.minVideoHeight);
        r0.putInt(FIELD_MIN_VIDEO_FRAMERATE, this.minVideoFrameRate);
        r0.putInt(FIELD_MIN_VIDEO_BITRATE, this.minVideoBitrate);
        r0.putInt(FIELD_VIEWPORT_WIDTH, this.viewportWidth);
        r0.putInt(FIELD_VIEWPORT_HEIGHT, this.viewportHeight);
        r0.putBoolean(FIELD_VIEWPORT_ORIENTATION_MAY_CHANGE, this.viewportOrientationMayChange);
        r0.putStringArray(FIELD_PREFERRED_VIDEO_MIMETYPES, (String[]) this.preferredVideoMimeTypes.toArray(new String[0]));
        r0.putInt(FIELD_PREFERRED_VIDEO_ROLE_FLAGS, this.preferredVideoRoleFlags);
        r0.putStringArray(FIELD_PREFERRED_AUDIO_LANGUAGES, (String[]) this.preferredAudioLanguages.toArray(new String[0]));
        r0.putInt(FIELD_PREFERRED_AUDIO_ROLE_FLAGS, this.preferredAudioRoleFlags);
        r0.putInt(FIELD_MAX_AUDIO_CHANNEL_COUNT, this.maxAudioChannelCount);
        r0.putInt(FIELD_MAX_AUDIO_BITRATE, this.maxAudioBitrate);
        r0.putStringArray(FIELD_PREFERRED_AUDIO_MIME_TYPES, (String[]) this.preferredAudioMimeTypes.toArray(new String[0]));
        r0.putStringArray(FIELD_PREFERRED_TEXT_LANGUAGES, (String[]) this.preferredTextLanguages.toArray(new String[0]));
        r0.putInt(FIELD_PREFERRED_TEXT_ROLE_FLAGS, this.preferredTextRoleFlags);
        r0.putInt(FIELD_IGNORED_TEXT_SELECTION_FLAGS, this.ignoredTextSelectionFlags);
        r0.putBoolean(FIELD_SELECT_UNDETERMINED_TEXT_LANGUAGE, this.selectUndeterminedTextLanguage);
        r0.putInt(FIELD_AUDIO_OFFLOAD_MODE_PREFERENCE, this.audioOffloadPreferences.audioOffloadMode);
        r0.putBoolean(FIELD_IS_GAPLESS_SUPPORT_REQUIRED, this.audioOffloadPreferences.isGaplessSupportRequired);
        r0.putBoolean(FIELD_IS_SPEED_CHANGE_SUPPORT_REQUIRED, this.audioOffloadPreferences.isSpeedChangeSupportRequired);
        r0.putBundle(FIELD_AUDIO_OFFLOAD_PREFERENCES, this.audioOffloadPreferences.toBundle());
        r0.putBoolean(FIELD_IS_PREFER_IMAGE_OVER_VIDEO_ENABLED, this.isPrioritizeImageOverVideoEnabled);
        r0.putBoolean(FIELD_FORCE_LOWEST_BITRATE, this.forceLowestBitrate);
        r0.putBoolean(FIELD_FORCE_HIGHEST_SUPPORTED_BITRATE, this.forceHighestSupportedBitrate);
        r0.putParcelableArrayList(FIELD_SELECTION_OVERRIDES, BundleCollectionUtil.toBundleArrayList(this.overrides.values(), new TrackSelectionParameters$$ExternalSyntheticLambda0()));
        r0.putIntArray(FIELD_DISABLED_TRACK_TYPE, Ints.toArray(this.disabledTrackTypes));
        return r0;
    }
}
