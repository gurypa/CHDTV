package androidx.media3.exoplayer.upstream;

import android.net.Uri;
import android.text.TextUtils;
import androidx.media3.common.C0180C;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.trackselection.ExoTrackSelection;
import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class CmcdData {
    private static final Joiner COMMA_JOINER = null;
    private final CmcdObject cmcdObject;
    private final CmcdRequest cmcdRequest;
    private final CmcdSession cmcdSession;
    private final CmcdStatus cmcdStatus;
    private final int dataTransmissionMode;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.CmcdData$1 */
    static /* synthetic */ class C03691 {
    }

    private static final class CmcdObject {
        public final int bitrateKbps;
        public final ImmutableList<String> customDataList;
        public final long objectDurationMs;
        public final String objectType;
        public final int topBitrateKbps;

        public static final class Builder {
            private int bitrateKbps;
            private ImmutableList<String> customDataList;
            private long objectDurationMs;
            private String objectType;
            private int topBitrateKbps;

            public Builder() {
                this.bitrateKbps = C0180C.RATE_UNSET_INT;
                this.topBitrateKbps = C0180C.RATE_UNSET_INT;
                this.objectDurationMs = C0180C.TIME_UNSET;
                this.customDataList = ImmutableList.m212of();
            }

            static /* synthetic */ int access$200(Builder r1) {
                return r1.bitrateKbps;
            }

            static /* synthetic */ int access$300(Builder r1) {
                return r1.topBitrateKbps;
            }

            static /* synthetic */ long access$400(Builder r2) {
                return r2.objectDurationMs;
            }

            static /* synthetic */ String access$500(Builder r1) {
                return r1.objectType;
            }

            static /* synthetic */ ImmutableList access$600(Builder r1) {
                return r1.customDataList;
            }

            public CmcdObject build() {
                return new CmcdObject(this, null);
            }

            public Builder setBitrateKbps(int r2) {
                if (r2 < 0) goto L4;
            L7:
                boolean r0 = true;
            L8:
                Assertions.checkArgument(r0);
                this.bitrateKbps = r2;
                return this;
            L4:
                if (r2 == (-2147483647)) goto L7;
                r0 = false;
                goto L8
            }

            public Builder setCustomDataList(List<String> r2) {
                this.customDataList = ImmutableList.copyOf(r2);
                return this;
            }

            public Builder setObjectDurationMs(long r3) {
                if (r3 < 0) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.objectDurationMs = r3;
                return this;
            L5:
                if (r3 == C0180C.TIME_UNSET) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setObjectType(String r1) {
                this.objectType = r1;
                return this;
            }

            public Builder setTopBitrateKbps(int r2) {
                if (r2 < 0) goto L4;
            L7:
                boolean r0 = true;
            L8:
                Assertions.checkArgument(r0);
                this.topBitrateKbps = r2;
                return this;
            L4:
                if (r2 == (-2147483647)) goto L7;
                r0 = false;
                goto L8
            }
        }

        private CmcdObject(Builder r3) {
            this.bitrateKbps = Builder.access$200(r3);
            this.topBitrateKbps = Builder.access$300(r3);
            this.objectDurationMs = Builder.access$400(r3);
            this.objectType = Builder.access$500(r3);
            this.customDataList = Builder.access$600(r3);
        }

        /* synthetic */ CmcdObject(Builder r1, C03691 r2) {
            this(r1);
        }

        public void populateCmcdDataMap(ArrayListMultimap<String, String> r6) {
            ArrayList r0 = new ArrayList();
            if (this.bitrateKbps == (-2147483647)) goto L6;
            r0.add("br=" + this.bitrateKbps);
        L6:
            if (this.topBitrateKbps == (-2147483647)) goto L9;
            r0.add("tb=" + this.topBitrateKbps);
        L9:
            if (this.objectDurationMs == C0180C.TIME_UNSET) goto L12;
            r0.add("d=" + this.objectDurationMs);
        L12:
            if (TextUtils.isEmpty(this.objectType) == true) goto L14;
            r0.add("ot=" + this.objectType);
        L14:
            r0.addAll(this.customDataList);
            if (r0.isEmpty() == true) goto L18;
            r6.putAll(CmcdConfiguration.KEY_CMCD_OBJECT, r0);
            return;
        }
    }

    private static final class CmcdRequest {
        public final long bufferLengthMs;
        public final ImmutableList<String> customDataList;
        public final long deadlineMs;
        public final long measuredThroughputInKbps;
        public final String nextObjectRequest;
        public final String nextRangeRequest;
        public final boolean startup;

        public static final class Builder {
            private long bufferLengthMs;
            private ImmutableList<String> customDataList;
            private long deadlineMs;
            private long measuredThroughputInKbps;
            private String nextObjectRequest;
            private String nextRangeRequest;
            private boolean startup;

            public Builder() {
                this.bufferLengthMs = C0180C.TIME_UNSET;
                this.measuredThroughputInKbps = -2147483647L;
                this.deadlineMs = C0180C.TIME_UNSET;
                this.customDataList = ImmutableList.m212of();
            }

            static /* synthetic */ long access$1000(Builder r2) {
                return r2.deadlineMs;
            }

            static /* synthetic */ boolean access$1100(Builder r1) {
                return r1.startup;
            }

            static /* synthetic */ String access$1200(Builder r1) {
                return r1.nextObjectRequest;
            }

            static /* synthetic */ String access$1300(Builder r1) {
                return r1.nextRangeRequest;
            }

            static /* synthetic */ ImmutableList access$1400(Builder r1) {
                return r1.customDataList;
            }

            static /* synthetic */ long access$800(Builder r2) {
                return r2.bufferLengthMs;
            }

            static /* synthetic */ long access$900(Builder r2) {
                return r2.measuredThroughputInKbps;
            }

            public CmcdRequest build() {
                return new CmcdRequest(this, null);
            }

            public Builder setBufferLengthMs(long r5) {
                if (r5 < 0) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.bufferLengthMs = ((50 + r5) / 100) * 100;
                return this;
            L5:
                if (r5 == C0180C.TIME_UNSET) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setCustomDataList(List<String> r2) {
                this.customDataList = ImmutableList.copyOf(r2);
                return this;
            }

            public Builder setDeadlineMs(long r5) {
                if (r5 < 0) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.deadlineMs = ((50 + r5) / 100) * 100;
                return this;
            L5:
                if (r5 == C0180C.TIME_UNSET) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setMeasuredThroughputInKbps(long r5) {
                if (r5 < 0) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.measuredThroughputInKbps = ((50 + r5) / 100) * 100;
                return this;
            L5:
                if (r5 == (-2147483647L)) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setNextObjectRequest(String r2) {
                if (r2 != null) goto L4;
                String r0 = null;
            L5:
                this.nextObjectRequest = r0;
                return this;
            L4:
                r0 = Uri.encode(r2);
                goto L5
            }

            public Builder setNextRangeRequest(String r1) {
                this.nextRangeRequest = r1;
                return this;
            }

            public Builder setStartup(boolean r1) {
                this.startup = r1;
                return this;
            }
        }

        private CmcdRequest(Builder r3) {
            this.bufferLengthMs = Builder.access$800(r3);
            this.measuredThroughputInKbps = Builder.access$900(r3);
            this.deadlineMs = Builder.access$1000(r3);
            this.startup = Builder.access$1100(r3);
            this.nextObjectRequest = Builder.access$1200(r3);
            this.nextRangeRequest = Builder.access$1300(r3);
            this.customDataList = Builder.access$1400(r3);
        }

        /* synthetic */ CmcdRequest(Builder r1, C03691 r2) {
            this(r1);
        }

        public void populateCmcdDataMap(ArrayListMultimap<String, String> r9) {
            ArrayList r0 = new ArrayList();
            if (this.bufferLengthMs == C0180C.TIME_UNSET) goto L6;
            r0.add("bl=" + this.bufferLengthMs);
        L6:
            if (this.measuredThroughputInKbps == (-2147483647L)) goto L9;
            r0.add("mtp=" + this.measuredThroughputInKbps);
        L9:
            if (this.deadlineMs == C0180C.TIME_UNSET) goto L12;
            r0.add("dl=" + this.deadlineMs);
        L12:
            if (this.startup == false) goto L15;
            r0.add(CmcdConfiguration.KEY_STARTUP);
        L15:
            if (TextUtils.isEmpty(this.nextObjectRequest) == true) goto L18;
            r0.add(Util.formatInvariant("%s=\"%s\"", new Object[]{CmcdConfiguration.KEY_NEXT_OBJECT_REQUEST, this.nextObjectRequest}));
        L18:
            if (TextUtils.isEmpty(this.nextRangeRequest) == true) goto L20;
            r0.add(Util.formatInvariant("%s=\"%s\"", new Object[]{CmcdConfiguration.KEY_NEXT_RANGE_REQUEST, this.nextRangeRequest}));
        L20:
            r0.addAll(this.customDataList);
            if (r0.isEmpty() == true) goto L24;
            r9.putAll(CmcdConfiguration.KEY_CMCD_REQUEST, r0);
            return;
        }
    }

    private static final class CmcdSession {
        public static final int VERSION = 1;
        public final String contentId;
        public final ImmutableList<String> customDataList;
        public final float playbackRate;
        public final String sessionId;
        public final String streamType;
        public final String streamingFormat;

        public static final class Builder {
            private String contentId;
            private ImmutableList<String> customDataList;
            private float playbackRate;
            private String sessionId;
            private String streamType;
            private String streamingFormat;

            public Builder() {
                this.customDataList = ImmutableList.m212of();
            }

            static /* synthetic */ String access$1600(Builder r1) {
                return r1.contentId;
            }

            static /* synthetic */ String access$1700(Builder r1) {
                return r1.sessionId;
            }

            static /* synthetic */ String access$1800(Builder r1) {
                return r1.streamingFormat;
            }

            static /* synthetic */ String access$1900(Builder r1) {
                return r1.streamType;
            }

            static /* synthetic */ float access$2000(Builder r1) {
                return r1.playbackRate;
            }

            static /* synthetic */ ImmutableList access$2100(Builder r1) {
                return r1.customDataList;
            }

            public CmcdSession build() {
                return new CmcdSession(this, null);
            }

            public Builder setContentId(String r3) {
                if (r3 != null) goto L4;
            L7:
                boolean r0 = true;
            L8:
                Assertions.checkArgument(r0);
                this.contentId = r3;
                return this;
            L4:
                if (r3.length() <= 64) goto L7;
                r0 = false;
                goto L8
            }

            public Builder setCustomDataList(List<String> r2) {
                this.customDataList = ImmutableList.copyOf(r2);
                return this;
            }

            public Builder setPlaybackRate(float r2) {
                if (r2 <= 0.0f) goto L5;
            L8:
                boolean r0 = true;
            L9:
                Assertions.checkArgument(r0);
                this.playbackRate = r2;
                return this;
            L5:
                if (r2 == (-3.4028235E38f)) goto L8;
                r0 = false;
                goto L9
            }

            public Builder setSessionId(String r3) {
                if (r3 != null) goto L4;
            L7:
                boolean r0 = true;
            L8:
                Assertions.checkArgument(r0);
                this.sessionId = r3;
                return this;
            L4:
                if (r3.length() <= 64) goto L7;
                r0 = false;
                goto L8
            }

            public Builder setStreamType(String r1) {
                this.streamType = r1;
                return this;
            }

            public Builder setStreamingFormat(String r1) {
                this.streamingFormat = r1;
                return this;
            }
        }

        private CmcdSession(Builder r2) {
            this.contentId = Builder.access$1600(r2);
            this.sessionId = Builder.access$1700(r2);
            this.streamingFormat = Builder.access$1800(r2);
            this.streamType = Builder.access$1900(r2);
            this.playbackRate = Builder.access$2000(r2);
            this.customDataList = Builder.access$2100(r2);
        }

        /* synthetic */ CmcdSession(Builder r1, C03691 r2) {
            this(r1);
        }

        public void populateCmcdDataMap(ArrayListMultimap<String, String> r9) {
            ArrayList r0 = new ArrayList();
            if (TextUtils.isEmpty(this.contentId) == true) goto L6;
            r0.add(Util.formatInvariant("%s=\"%s\"", new Object[]{CmcdConfiguration.KEY_CONTENT_ID, this.contentId}));
        L6:
            if (TextUtils.isEmpty(this.sessionId) == true) goto L9;
            r0.add(Util.formatInvariant("%s=\"%s\"", new Object[]{CmcdConfiguration.KEY_SESSION_ID, this.sessionId}));
        L9:
            if (TextUtils.isEmpty(this.streamingFormat) == true) goto L12;
            r0.add("sf=" + this.streamingFormat);
        L12:
            if (TextUtils.isEmpty(this.streamType) == true) goto L15;
            r0.add("st=" + this.streamType);
        L15:
            if (this.playbackRate != (-3.4028235E38f)) goto L17;
        L19:
            r0.addAll(this.customDataList);
            if (r0.isEmpty() == true) goto L23;
            r9.putAll(CmcdConfiguration.KEY_CMCD_SESSION, r0);
            return;
        L23:
            return;
        L17:
            if (this.playbackRate == 1.0f) goto L19;
            r0.add(Util.formatInvariant("%s=%.2f", new Object[]{CmcdConfiguration.KEY_PLAYBACK_RATE, Float.valueOf(this.playbackRate)}));
            goto L19
        }
    }

    private static final class CmcdStatus {
        public final boolean bufferStarvation;
        public final ImmutableList<String> customDataList;
        public final int maximumRequestedThroughputKbps;

        public static final class Builder {
            private boolean bufferStarvation;
            private ImmutableList<String> customDataList;
            private int maximumRequestedThroughputKbps;

            public Builder() {
                this.maximumRequestedThroughputKbps = C0180C.RATE_UNSET_INT;
                this.customDataList = ImmutableList.m212of();
            }

            static /* synthetic */ int access$2300(Builder r1) {
                return r1.maximumRequestedThroughputKbps;
            }

            static /* synthetic */ boolean access$2400(Builder r1) {
                return r1.bufferStarvation;
            }

            static /* synthetic */ ImmutableList access$2500(Builder r1) {
                return r1.customDataList;
            }

            public CmcdStatus build() {
                return new CmcdStatus(this, null);
            }

            public Builder setBufferStarvation(boolean r1) {
                this.bufferStarvation = r1;
                return this;
            }

            public Builder setCustomDataList(List<String> r2) {
                this.customDataList = ImmutableList.copyOf(r2);
                return this;
            }

            public Builder setMaximumRequestedThroughputKbps(int r3) {
                if (r3 >= 0) goto L7;
                if (r3 == (-2147483647)) goto L7;
                boolean r1 = false;
            L8:
                Assertions.checkArgument(r1);
                if (r3 != (-2147483647)) goto L11;
                int r0 = r3;
            L12:
                this.maximumRequestedThroughputKbps = r0;
                return this;
            L11:
                r0 = ((r3 + 50) / 100) * 100;
            L7:
                r1 = true;
                goto L8
            }
        }

        private CmcdStatus(Builder r2) {
            this.maximumRequestedThroughputKbps = Builder.access$2300(r2);
            this.bufferStarvation = Builder.access$2400(r2);
            this.customDataList = Builder.access$2500(r2);
        }

        /* synthetic */ CmcdStatus(Builder r1, C03691 r2) {
            this(r1);
        }

        public void populateCmcdDataMap(ArrayListMultimap<String, String> r4) {
            ArrayList r0 = new ArrayList();
            if (this.maximumRequestedThroughputKbps == (-2147483647)) goto L6;
            r0.add("rtp=" + this.maximumRequestedThroughputKbps);
        L6:
            if (this.bufferStarvation == false) goto L8;
            r0.add(CmcdConfiguration.KEY_BUFFER_STARVATION);
        L8:
            r0.addAll(this.customDataList);
            if (r0.isEmpty() == true) goto L12;
            r4.putAll(CmcdConfiguration.KEY_CMCD_STATUS, r0);
            return;
        }
    }

    public static final class Factory {
        private static final Pattern CUSTOM_KEY_NAME_PATTERN = null;
        public static final String OBJECT_TYPE_AUDIO_ONLY = "a";
        public static final String OBJECT_TYPE_INIT_SEGMENT = "i";
        public static final String OBJECT_TYPE_MUXED_AUDIO_AND_VIDEO = "av";
        public static final String OBJECT_TYPE_VIDEO_ONLY = "v";
        public static final String STREAMING_FORMAT_DASH = "d";
        public static final String STREAMING_FORMAT_HLS = "h";
        public static final String STREAMING_FORMAT_SS = "s";
        public static final String STREAM_TYPE_LIVE = "l";
        public static final String STREAM_TYPE_VOD = "v";
        private final long bufferedDurationUs;
        private long chunkDurationUs;
        private final CmcdConfiguration cmcdConfiguration;
        private final boolean didRebuffer;
        private final boolean isBufferEmpty;
        private final boolean isLive;
        private String nextObjectRequest;
        private String nextRangeRequest;
        private String objectType;
        private final float playbackRate;
        private final String streamingFormat;
        private final ExoTrackSelection trackSelection;

        static {
            CUSTOM_KEY_NAME_PATTERN = Pattern.compile(".*-.*");
        }

        public Factory(CmcdConfiguration r4, ExoTrackSelection r5, long r6, float r8, String r9, boolean r10, boolean r11, boolean r12) {
            boolean r1 = true;
            if (r6 < 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            if (r8 != (-3.4028235E38f)) goto L9;
        L12:
            Assertions.checkArgument(r1);
            this.cmcdConfiguration = r4;
            this.trackSelection = r5;
            this.bufferedDurationUs = r6;
            this.playbackRate = r8;
            this.streamingFormat = r9;
            this.isLive = r10;
            this.didRebuffer = r11;
            this.isBufferEmpty = r12;
            this.chunkDurationUs = C0180C.TIME_UNSET;
            return;
        L9:
            if (r8 > 0.0f) goto L12;
            r1 = false;
            goto L12
        L5:
            r0 = false;
            goto L6
        }

        private boolean getIsInitSegment() {
            if (this.objectType != null) goto L5;
        L7:
            return false;
        L5:
            if (this.objectType.equals(OBJECT_TYPE_INIT_SEGMENT) == false) goto L7;
            return true;
        }

        public static String getObjectType(ExoTrackSelection r3) {
            if (r3 == null) goto L5;
            boolean r1 = true;
        L6:
            Assertions.checkArgument(r1);
            int r2 = MimeTypes.getTrackType(r3.getSelectedFormat().sampleMimeType);
            if (r2 != (-1)) goto L9;
            r2 = MimeTypes.getTrackType(r3.getSelectedFormat().containerMimeType);
        L9:
            if (r2 != 1) goto L13;
            return OBJECT_TYPE_AUDIO_ONLY;
        L13:
            if (r2 != 2) goto L16;
            return "v";
        L16:
            return null;
        L5:
            r1 = false;
            goto L6
        }

        private void validateCustomDataListFormat(List<String> r5) {
            Iterator<String> r0 = r5.iterator();
        L4:
            if (r0.hasNext() == false) goto L6;
            String r2 = Util.split(r0.next(), "=")[0];
            Assertions.checkState(CUSTOM_KEY_NAME_PATTERN.matcher(r2).matches());
            goto L4
        }

        public CmcdData createCmcdData() {
            ImmutableListMultimap<String, String> r0 = this.cmcdConfiguration.requestConfig.getCustomData();
            UnmodifiableIterator<String> r1 = r0.keySet().iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            validateCustomDataListFormat(r0.get(r1.next()));
            goto L4
        L6:
            int r2 = Util.ceilDivide(this.trackSelection.getSelectedFormat().bitrate, 1000);
            CmcdObject.Builder r3 = new CmcdObject.Builder();
            if (getIsInitSegment() == true) goto L22;
            if (this.cmcdConfiguration.isBitrateLoggingAllowed() == false) goto L12;
            r3.setBitrateKbps(r2);
        L12:
            if (this.cmcdConfiguration.isTopBitrateLoggingAllowed() == false) goto L19;
            TrackGroup r4 = this.trackSelection.getTrackGroup();
            int r5 = this.trackSelection.getSelectedFormat().bitrate;
            int r6 = 0;
        L15:
            if (r6 >= r4.length) goto L17;
            r5 = Math.max(r5, r4.getFormat(r6).bitrate);
            r6 = r6 + 1;
            goto L15
        L17:
            r3.setTopBitrateKbps(Util.ceilDivide(r5, 1000));
        L19:
            if (this.cmcdConfiguration.isObjectDurationLoggingAllowed() == false) goto L22;
            r3.setObjectDurationMs(Util.usToMs(this.chunkDurationUs));
        L22:
            if (this.cmcdConfiguration.isObjectTypeLoggingAllowed() == false) goto L25;
            r3.setObjectType(this.objectType);
        L25:
            if (r0.containsKey(CmcdConfiguration.KEY_CMCD_OBJECT) == false) goto L27;
            r3.setCustomDataList(r0.get(CmcdConfiguration.KEY_CMCD_OBJECT));
        L27:
            CmcdRequest.Builder r7 = new CmcdRequest.Builder();
            if (getIsInitSegment() == true) goto L33;
            if (this.cmcdConfiguration.isBufferLengthLoggingAllowed() == false) goto L33;
            r7.setBufferLengthMs(Util.usToMs(this.bufferedDurationUs));
        L33:
            if (this.cmcdConfiguration.isMeasuredThroughputLoggingAllowed() == false) goto L38;
            if (this.trackSelection.getLatestBitrateEstimate() == (-2147483647L)) goto L38;
            r7.setMeasuredThroughputInKbps(Util.ceilDivide(this.trackSelection.getLatestBitrateEstimate(), 1000));
        L38:
            if (this.cmcdConfiguration.isDeadlineLoggingAllowed() == false) goto L41;
            r7.setDeadlineMs(Util.usToMs((long) (this.bufferedDurationUs / this.playbackRate)));
        L41:
            if (this.cmcdConfiguration.isStartupLoggingAllowed() == false) goto L51;
            if (this.didRebuffer == false) goto L45;
        L48:
            boolean r8 = true;
        L49:
            r7.setStartup(r8);
            goto L51
        L45:
            if (this.isBufferEmpty == true) goto L48;
            r8 = false;
        L51:
            if (this.cmcdConfiguration.isNextObjectRequestLoggingAllowed() == false) goto L54;
            r7.setNextObjectRequest(this.nextObjectRequest);
        L54:
            if (this.cmcdConfiguration.isNextRangeRequestLoggingAllowed() == false) goto L57;
            r7.setNextRangeRequest(this.nextRangeRequest);
        L57:
            if (r0.containsKey(CmcdConfiguration.KEY_CMCD_REQUEST) == false) goto L59;
            r7.setCustomDataList(r0.get(CmcdConfiguration.KEY_CMCD_REQUEST));
        L59:
            CmcdSession.Builder r9 = new CmcdSession.Builder();
            if (this.cmcdConfiguration.isContentIdLoggingAllowed() == false) goto L63;
            r9.setContentId(this.cmcdConfiguration.contentId);
        L63:
            if (this.cmcdConfiguration.isSessionIdLoggingAllowed() == false) goto L66;
            r9.setSessionId(this.cmcdConfiguration.sessionId);
        L66:
            if (this.cmcdConfiguration.isStreamingFormatLoggingAllowed() == false) goto L69;
            r9.setStreamingFormat(this.streamingFormat);
        L69:
            if (this.cmcdConfiguration.isStreamTypeLoggingAllowed() == false) goto L76;
            if (this.isLive == false) goto L73;
            String r10 = STREAM_TYPE_LIVE;
        L74:
            r9.setStreamType(r10);
            goto L76
        L73:
            r10 = "v";
        L76:
            if (this.cmcdConfiguration.isPlaybackRateLoggingAllowed() == false) goto L79;
            r9.setPlaybackRate(this.playbackRate);
        L79:
            if (r0.containsKey(CmcdConfiguration.KEY_CMCD_SESSION) == false) goto L81;
            r9.setCustomDataList(r0.get(CmcdConfiguration.KEY_CMCD_SESSION));
        L81:
            CmcdStatus.Builder r11 = new CmcdStatus.Builder();
            if (this.cmcdConfiguration.isMaximumRequestThroughputLoggingAllowed() == false) goto L85;
            r11.setMaximumRequestedThroughputKbps(this.cmcdConfiguration.requestConfig.getRequestedMaximumThroughputKbps(r2));
        L85:
            if (this.cmcdConfiguration.isBufferStarvationLoggingAllowed() == false) goto L88;
            r11.setBufferStarvation(this.didRebuffer);
        L88:
            if (r0.containsKey(CmcdConfiguration.KEY_CMCD_STATUS) == false) goto L91;
            r11.setCustomDataList(r0.get(CmcdConfiguration.KEY_CMCD_STATUS));
        L91:
            return new CmcdData(r3.build(), r7.build(), r9.build(), r11.build(), this.cmcdConfiguration.dataTransmissionMode, null);
        }

        public Factory setChunkDurationUs(long r3) {
            if (r3 < 0) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.chunkDurationUs = r3;
            return this;
        L5:
            r0 = false;
            goto L6
        }

        public Factory setNextObjectRequest(String r1) {
            this.nextObjectRequest = r1;
            return this;
        }

        public Factory setNextRangeRequest(String r1) {
            this.nextRangeRequest = r1;
            return this;
        }

        public Factory setObjectType(String r1) {
            this.objectType = r1;
            return this;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ObjectType {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamType {
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamingFormat {
    }

    static {
        COMMA_JOINER = Joiner.m184on(",");
    }

    private CmcdData(CmcdObject r1, CmcdRequest r2, CmcdSession r3, CmcdStatus r4, int r5) {
        this.cmcdObject = r1;
        this.cmcdRequest = r2;
        this.cmcdSession = r3;
        this.cmcdStatus = r4;
        this.dataTransmissionMode = r5;
    }

    /* synthetic */ CmcdData(CmcdObject r1, CmcdRequest r2, CmcdSession r3, CmcdStatus r4, int r5, C03691 r6) {
        this(r1, r2, r3, r4, r5);
    }

    public DataSpec addToDataSpec(DataSpec r7) {
        ArrayListMultimap<String, String> r0 = ArrayListMultimap.create();
        this.cmcdObject.populateCmcdDataMap(r0);
        this.cmcdRequest.populateCmcdDataMap(r0);
        this.cmcdSession.populateCmcdDataMap(r0);
        this.cmcdStatus.populateCmcdDataMap(r0);
        if (this.dataTransmissionMode != 0) goto L10;
        ImmutableMap.Builder r1 = ImmutableMap.builder();
        Iterator r2 = r0.keySet().iterator();
    L6:
        if (r2.hasNext() == false) goto L9;
        String r3 = (String) r2.next();
        List r4 = r0.get(r3);
        Collections.sort(r4);
        r1.put(r3, COMMA_JOINER.join(r4));
        goto L6
    L9:
        return r7.withAdditionalHeaders(r1.buildOrThrow());
    L10:
        ArrayList r5 = new ArrayList();
        Iterator r6 = r0.asMap().values().iterator();
    L12:
        if (r6.hasNext() == false) goto L14;
        r5.addAll((Collection) r6.next());
        goto L12
    L14:
        Collections.sort(r5);
        Uri.Builder r8 = r7.uri.buildUpon().appendQueryParameter(CmcdConfiguration.CMCD_QUERY_PARAMETER_KEY, COMMA_JOINER.join(r5));
        return r7.buildUpon().setUri(r8.build()).build();
    }
}
