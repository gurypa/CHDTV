package androidx.media3.exoplayer.audio;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.provider.Settings;
import android.util.Pair;
import android.util.SparseArray;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class AudioCapabilities {
    static final ImmutableMap<Integer, Integer> ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS = null;
    public static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES = null;
    static final int DEFAULT_MAX_CHANNEL_COUNT = 10;
    static final int DEFAULT_SAMPLE_RATE_HZ = 48000;
    private static final ImmutableList<Integer> EXTERNAL_SURROUND_SOUND_ENCODINGS = null;
    private static final String EXTERNAL_SURROUND_SOUND_KEY = "external_surround_sound_enabled";
    private static final String FORCE_EXTERNAL_SURROUND_SOUND_KEY = "use_external_surround_sound_flag";
    private final SparseArray<AudioProfile> encodingToAudioProfile;
    private final int maxChannelCount;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.audio.AudioCapabilities$1 */
    static /* synthetic */ class C02531 {
    }

    private static final class Api23 {
        private Api23() {
        }

        private static ImmutableSet<Integer> getAllBluetoothDeviceTypes() {
            ImmutableSet.Builder r0 = new ImmutableSet.Builder().add(new Integer[]{8, 7});
            if (Util.SDK_INT < 31) goto L6;
            r0.add(new Integer[]{26, 27});
        L6:
            if (Util.SDK_INT < 33) goto L9;
            r0.add(30);
        L9:
            return r0.build();
        }

        public static boolean isBluetoothConnected(AudioManager r8, AudioDeviceInfoApi23 r9) {
            if (r9 != null) goto L5;
            AudioDeviceInfo[] r2 = ((AudioManager) Assertions.checkNotNull(r8)).getDevices(2);
        L6:
            ImmutableSet<Integer> r3 = getAllBluetoothDeviceTypes();
            int r4 = r2.length;
            int r5 = 0;
        L7:
            if (r5 >= r4) goto L12;
            if (r3.contains(Integer.valueOf(r2[r5].getType())) == true) goto L10;
            r5 = r5 + 1;
            goto L7
        L10:
            return true;
        L12:
            return false;
        L5:
            r2 = new AudioDeviceInfo[]{r9.audioDeviceInfo};
            goto L6
        }
    }

    private static final class Api29 {
        private Api29() {
        }

        public static ImmutableList<Integer> getDirectPlaybackSupportedEncodings(AudioAttributes r5) {
            ImmutableList.Builder r0 = ImmutableList.builder();
            UnmodifiableIterator<Integer> r1 = AudioCapabilities.ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.keySet().iterator();
        L4:
            if (r1.hasNext() == false) goto L12;
            int r2 = r1.next().intValue();
            if (Util.SDK_INT < Util.getApiLevelThatAudioFormatIntroducedAudioEncoding(r2)) goto L4;
            if (AudioTrack.isDirectPlaybackSupported(new AudioFormat.Builder().setChannelMask(12).setEncoding(r2).setSampleRate(48000).build(), r5.getAudioAttributesV21().audioAttributes) == false) goto L4;
            r0.add(Integer.valueOf(r2));
            goto L4
        L12:
            r0.add(2);
            return r0.build();
        }

        public static int getMaxSupportedChannelCountForPassthrough(int r4, int r5, AudioAttributes r6) {
            int r0 = 10;
        L3:
            if (r0 <= 0) goto L11;
            int r1 = Util.getAudioTrackChannelConfig(r0);
            if (r1 == 0) goto L10;
            if (AudioTrack.isDirectPlaybackSupported(new AudioFormat.Builder().setEncoding(r4).setSampleRate(r5).setChannelMask(r1).build(), r6.getAudioAttributesV21().audioAttributes) == false) goto L10;
            return r0;
        L10:
            r0 = r0 - 1;
            goto L3
        L11:
            return 0;
        }
    }

    private static final class Api33 {
        private Api33() {
        }

        public static AudioCapabilities getCapabilitiesInternalForDirectPlayback(AudioManager r4, AudioAttributes r5) {
            return new AudioCapabilities(AudioCapabilities.access$000(r4.getDirectProfilesForAttributes(r5.getAudioAttributesV21().audioAttributes)), null);
        }

        public static AudioDeviceInfoApi23 getDefaultRoutedDeviceForAttributes(AudioManager r3, AudioAttributes r4) {
            List<AudioDeviceInfo> r1 = ((AudioManager) Assertions.checkNotNull(r3)).getAudioDevicesForAttributes(r4.getAudioAttributesV21().audioAttributes);     // Catch: RuntimeException -> L9
            if (r1.isEmpty() == false) goto L8;
            return null;
        L8:
            return new AudioDeviceInfoApi23(r1.get(0));
        L10:
            return null;
        }
    }

    private static final class AudioProfile {
        public static final AudioProfile DEFAULT_AUDIO_PROFILE = null;
        private final ImmutableSet<Integer> channelMasks;
        public final int encoding;
        public final int maxChannelCount;

        static {
            if (Util.SDK_INT < 33) goto L5;
            AudioProfile r0 = new AudioProfile(2, getAllChannelMasksForMaxChannelCount(10));
        L6:
            DEFAULT_AUDIO_PROFILE = r0;
            return;
        L5:
            r0 = new AudioProfile(2, 10);
            goto L6
        }

        public AudioProfile(int r2, int r3) {
            this.encoding = r2;
            this.maxChannelCount = r3;
            this.channelMasks = null;
        }

        public AudioProfile(int r5, Set<Integer> r6) {
            this.encoding = r5;
            this.channelMasks = ImmutableSet.copyOf(r6);
            int r0 = 0;
            UnmodifiableIterator<Integer> r1 = this.channelMasks.iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            r0 = Math.max(r0, Integer.bitCount(r1.next().intValue()));
            goto L4
        L6:
            this.maxChannelCount = r0;
        }

        private static ImmutableSet<Integer> getAllChannelMasksForMaxChannelCount(int r3) {
            ImmutableSet.Builder r0 = new ImmutableSet.Builder();
            int r1 = 1;
        L3:
            if (r1 > r3) goto L6;
            r0.add(Integer.valueOf(Util.getAudioTrackChannelConfig(r1)));
            r1 = r1 + 1;
            goto L3
        L6:
            return r0.build();
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if ((r6 instanceof AudioProfile) == true) goto L8;
            return false;
        L8:
            AudioProfile r1 = (AudioProfile) r6;
            if (this.encoding != r1.encoding) goto L16;
            if (this.maxChannelCount != r1.maxChannelCount) goto L16;
            if (Util.areEqual(this.channelMasks, r1.channelMasks) == false) goto L16;
            return true;
        L16:
            return false;
        }

        public int getMaxSupportedChannelCountForPassthrough(int r4, AudioAttributes r5) {
            if (this.channelMasks == null) goto L7;
            return this.maxChannelCount;
        L7:
            if (Util.SDK_INT < 29) goto L11;
            return Api29.getMaxSupportedChannelCountForPassthrough(this.encoding, r4, r5);
        L11:
            return ((Integer) Assertions.checkNotNull(AudioCapabilities.ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.getOrDefault(Integer.valueOf(this.encoding), 0))).intValue();
        }

        public int hashCode() {
            int r0 = ((this.encoding * 31) + this.maxChannelCount) * 31;
            if (this.channelMasks != null) goto L5;
            int r2 = 0;
        L7:
            return r0 + r2;
        L5:
            r2 = this.channelMasks.hashCode();
            goto L7
        }

        public boolean supportsChannelCount(int r4) {
            if (this.channelMasks == null) goto L5;
            int r0 = Util.getAudioTrackChannelConfig(r4);
            if (r0 != 0) goto L12;
            return false;
        L12:
            return this.channelMasks.contains(Integer.valueOf(r0));
        L5:
            if (r4 > this.maxChannelCount) goto L13;
            return true;
        L13:
            return false;
        }

        public String toString() {
            return "AudioProfile[format=" + this.encoding + ", maxChannelCount=" + this.maxChannelCount + ", channelMasks=" + this.channelMasks + "]";
        }
    }

    static {
        DEFAULT_AUDIO_CAPABILITIES = new AudioCapabilities(ImmutableList.m213of(AudioProfile.DEFAULT_AUDIO_PROFILE));
        EXTERNAL_SURROUND_SOUND_ENCODINGS = ImmutableList.m215of(2, 5, 6);
        ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS = new ImmutableMap.Builder().put(5, 6).put(17, 6).put(7, 6).put(30, 10).put(18, 6).put(6, 8).put(8, 8).put(14, 8).buildOrThrow();
    }

    private AudioCapabilities(List<AudioProfile> r5) {
        this.encodingToAudioProfile = new SparseArray();
        int r0 = 0;
    L4:
        if (r0 >= r5.size()) goto L6;
        AudioProfile r1 = r5.get(r0);
        this.encodingToAudioProfile.put(r1.encoding, r1);
        r0 = r0 + 1;
        goto L4
    L6:
        int r2 = 0;
        int r3 = 0;
    L8:
        if (r3 >= this.encodingToAudioProfile.size()) goto L10;
        r2 = Math.max(r2, this.encodingToAudioProfile.valueAt(r3).maxChannelCount);
        r3 = r3 + 1;
        goto L8
    L10:
        this.maxChannelCount = r2;
    }

    /* synthetic */ AudioCapabilities(List r1, C02531 r2) {
        this(r1);
    }

    @Deprecated
    public AudioCapabilities(int[] r2, int r3) {
        this(getAudioProfiles(r2, r3));
    }

    static /* synthetic */ ImmutableList access$000(List r1) {
        return getAudioProfiles(r1);
    }

    private static boolean deviceMaySetExternalSurroundSoundGlobalSetting() {
        if ("Amazon".equals(Util.MANUFACTURER) == false) goto L5;
    L8:
        return true;
    L5:
        if ("Xiaomi".equals(Util.MANUFACTURER) == true) goto L8;
        return false;
    }

    private static ImmutableList<AudioProfile> getAudioProfiles(List<android.media.AudioProfile> r7) {
        HashMap r0 = new HashMap();
        r0.put(2, new HashSet(Ints.asList(new int[]{12})));
        int r1 = 0;
    L4:
        if (r1 >= r7.size()) goto L18;
        android.media.AudioProfile r2 = r7.get(r1);
        if (r2.getEncapsulationType() == 1) goto L17;
        int r3 = r2.getFormat();
        if (Util.isEncodingLinearPcm(r3) == true) goto L14;
        if (ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.containsKey(Integer.valueOf(r3)) == false) goto L17;
    L14:
        if (r0.containsKey(Integer.valueOf(r3)) == false) goto L16;
        ((Set) Assertions.checkNotNull((Set) r0.get(Integer.valueOf(r3)))).addAll(Ints.asList(r2.getChannelMasks()));
        goto L17
    L16:
        r0.put(Integer.valueOf(r3), new HashSet(Ints.asList(r2.getChannelMasks())));
    L17:
        r1 = r1 + 1;
        goto L4
    L18:
        ImmutableList.Builder r4 = ImmutableList.builder();
        Iterator r5 = r0.entrySet().iterator();
    L20:
        if (r5.hasNext() == false) goto L23;
        Map.Entry r6 = (Map.Entry) r5.next();
        r4.add(new AudioProfile(((Integer) r6.getKey()).intValue(), (Set) r6.getValue()));
        goto L20
    L23:
        return r4.build();
    }

    private static ImmutableList<AudioProfile> getAudioProfiles(int[] r4, int r5) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        if (r4 != null) goto L5;
        r4 = new int[0];
    L5:
        int r1 = 0;
    L7:
        if (r1 >= r4.length) goto L10;
        r0.add(new AudioProfile(r4[r1], r5));
        r1 = r1 + 1;
        goto L7
    L10:
        return r0.build();
    }

    @Deprecated
    public static AudioCapabilities getCapabilities(Context r2) {
        return getCapabilities(r2, AudioAttributes.DEFAULT, null);
    }

    public static AudioCapabilities getCapabilities(Context r2, AudioAttributes r3, AudioDeviceInfo r4) {
        if (Util.SDK_INT < 23) goto L6;
        if (r4 == null) goto L6;
        AudioDeviceInfoApi23 r0 = new AudioDeviceInfoApi23(r4);
    L8:
        return getCapabilitiesInternal(r2, r3, r0);
    L6:
        r0 = null;
        goto L8
    }

    static AudioCapabilities getCapabilitiesInternal(Context r10, Intent r11, AudioAttributes r12, AudioDeviceInfoApi23 r13) {
        AudioManager r0 = (AudioManager) Assertions.checkNotNull(r10.getSystemService(MimeTypes.BASE_TYPE_AUDIO));
        if (r13 == null) goto L6;
        AudioDeviceInfoApi23 r2 = r13;
    L10:
        if (Util.SDK_INT < 33) goto L18;
        if (Util.isTv(r10) == true) goto L16;
        if (Util.isAutomotive(r10) == false) goto L18;
    L16:
        return Api33.getCapabilitiesInternalForDirectPlayback(r0, r12);
    L18:
        if (Util.SDK_INT >= 23) goto L20;
    L23:
        ImmutableSet.Builder r1 = new ImmutableSet.Builder();
        r1.add(2);
        if (Util.SDK_INT >= 29) goto L26;
    L31:
        ContentResolver r3 = r10.getContentResolver();
        if (Settings.Global.getInt(r3, FORCE_EXTERNAL_SURROUND_SOUND_KEY, 0) != 1) goto L34;
        boolean r4 = true;
    L35:
        if (r4 == true) goto L39;
        if (deviceMaySetExternalSurroundSoundGlobalSetting() == true) goto L39;
    L41:
        if (r11 == null) goto L51;
        if (r4 == true) goto L51;
        if (r11.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) != 1) goto L51;
        int[] r6 = r11.getIntArrayExtra("android.media.extra.ENCODINGS");
        if (r6 == null) goto L49;
        r1.addAll(Ints.asList(r6));
    L49:
        return new AudioCapabilities(getAudioProfiles(Ints.toArray(r1.build()), r11.getIntExtra("android.media.extra.MAX_CHANNEL_COUNT", 10)));
    L51:
        return new AudioCapabilities(getAudioProfiles(Ints.toArray(r1.build()), 10));
    L39:
        if (Settings.Global.getInt(r3, EXTERNAL_SURROUND_SOUND_KEY, 0) != 1) goto L41;
        r1.addAll(EXTERNAL_SURROUND_SOUND_ENCODINGS);
        goto L41
    L34:
        r4 = false;
        goto L35
    L26:
        if (Util.isTv(r10) == false) goto L28;
    L29:
        r1.addAll(Api29.getDirectPlaybackSupportedEncodings(r12));
        return new AudioCapabilities(getAudioProfiles(Ints.toArray(r1.build()), 10));
    L28:
        if (Util.isAutomotive(r10) == false) goto L31;
    L20:
        if (Api23.isBluetoothConnected(r0, r2) == false) goto L23;
        return DEFAULT_AUDIO_CAPABILITIES;
    L6:
        if (Util.SDK_INT < 33) goto L8;
        r2 = Api33.getDefaultRoutedDeviceForAttributes(r0, r12);
        goto L10
    L8:
        r2 = null;
        goto L10
    }

    static AudioCapabilities getCapabilitiesInternal(Context r2, AudioAttributes r3, AudioDeviceInfoApi23 r4) {
        return getCapabilitiesInternal(r2, r2.registerReceiver(null, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG")), r3, r4);
    }

    private static int getChannelConfigForPassthrough(int r2) {
        if (Util.SDK_INT > 28) goto L15;
        if (r2 != 7) goto L8;
        r2 = 8;
        goto L15
    L8:
        if (r2 != 3) goto L10;
    L13:
        r2 = 6;
        goto L15
    L10:
        if (r2 == 4) goto L13;
        if (r2 == 5) goto L13;
    L15:
        if (Util.SDK_INT > 26) goto L22;
        if ("fugu".equals(Util.DEVICE) == false) goto L22;
        if (r2 != 1) goto L22;
        r2 = 2;
    L22:
        return Util.getAudioTrackChannelConfig(r2);
    }

    static Uri getExternalSurroundSoundGlobalSettingUri() {
        if (deviceMaySetExternalSurroundSoundGlobalSetting() == true) goto L7;
        return null;
    L7:
        return Settings.Global.getUriFor(EXTERNAL_SURROUND_SOUND_KEY);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof AudioCapabilities) == true) goto L8;
        return false;
    L8:
        AudioCapabilities r1 = (AudioCapabilities) r6;
        if (Util.contentEquals(this.encodingToAudioProfile, r1.encodingToAudioProfile) == false) goto L14;
        if (this.maxChannelCount != r1.maxChannelCount) goto L14;
        return true;
    L14:
        return false;
    }

    @Deprecated
    public Pair<Integer, Integer> getEncodingAndChannelConfigForPassthrough(Format r2) {
        return getEncodingAndChannelConfigForPassthrough(r2, AudioAttributes.DEFAULT);
    }

    public Pair<Integer, Integer> getEncodingAndChannelConfigForPassthrough(Format r7, AudioAttributes r8) {
        int r0 = MimeTypes.getEncoding((String) Assertions.checkNotNull(r7.sampleMimeType), r7.codecs);
        if (ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.containsKey(Integer.valueOf(r0)) == true) goto L6;
        return null;
    L6:
        if (r0 != 18) goto L11;
        if (supportsEncoding(18) == true) goto L11;
        r0 = 6;
    L20:
        if (supportsEncoding(r0) == true) goto L22;
        return null;
    L22:
        AudioProfile r3 = (AudioProfile) Assertions.checkNotNull(this.encodingToAudioProfile.get(r0));
        if (r7.channelCount == (-1)) goto L37;
        if (r0 == 18) goto L37;
        int r1 = r7.channelCount;
        if (r7.sampleMimeType.equals(MimeTypes.AUDIO_DTS_X) == false) goto L34;
        if (Util.SDK_INT >= 33) goto L34;
        if (r1 <= 10) goto L41;
        return null;
    L41:
        int r4 = getChannelConfigForPassthrough(r1);
        if (r4 != 0) goto L45;
        return null;
    L45:
        return Pair.create(Integer.valueOf(r0), Integer.valueOf(r4));
    L34:
        if (r3.supportsChannelCount(r1) == true) goto L41;
        return null;
    L37:
        if (r7.sampleRate == (-1)) goto L39;
        int r2 = r7.sampleRate;
    L40:
        r1 = r3.getMaxSupportedChannelCountForPassthrough(r2, r8);
        goto L41
    L39:
        r2 = 48000;
    L11:
        if (r0 != 8) goto L15;
        if (supportsEncoding(8) == true) goto L15;
    L18:
        r0 = 7;
    L15:
        if (r0 != 30) goto L20;
        if (supportsEncoding(30) == true) goto L20;
        goto L18
    }

    public int getMaxChannelCount() {
        return this.maxChannelCount;
    }

    public int hashCode() {
        return this.maxChannelCount + (Util.contentHashCode(this.encodingToAudioProfile) * 31);
    }

    @Deprecated
    public boolean isPassthroughPlaybackSupported(Format r2) {
        return isPassthroughPlaybackSupported(r2, AudioAttributes.DEFAULT);
    }

    public boolean isPassthroughPlaybackSupported(Format r2, AudioAttributes r3) {
        if (getEncodingAndChannelConfigForPassthrough(r2, r3) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean supportsEncoding(int r2) {
        return Util.contains(this.encodingToAudioProfile, r2);
    }

    public String toString() {
        return "AudioCapabilities[maxChannelCount=" + this.maxChannelCount + ", audioProfiles=" + this.encodingToAudioProfile + "]";
    }
}
