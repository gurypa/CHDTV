package androidx.media3.extractor.p001ts;

import android.util.SparseArray;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.ExtractorOutput;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface TsPayloadReader {
    public static final int FLAG_DATA_ALIGNMENT_INDICATOR = 4;
    public static final int FLAG_PAYLOAD_UNIT_START_INDICATOR = 1;
    public static final int FLAG_RANDOM_ACCESS_INDICATOR = 2;

    public static final class DvbSubtitleInfo {
        public final byte[] initializationData;
        public final String language;
        public final int type;

        public DvbSubtitleInfo(String r1, int r2, byte[] r3) {
            this.language = r1;
            this.type = r2;
            this.initializationData = r3;
        }
    }

    public static final class EsInfo {
        public static final int AUDIO_TYPE_CLEAN_EFFECTS = 1;
        public static final int AUDIO_TYPE_HEARING_IMPAIRED = 2;
        public static final int AUDIO_TYPE_UNDEFINED = 0;
        public static final int AUDIO_TYPE_VISUAL_IMPAIRED_COMMENTARY = 3;
        public final int audioType;
        public final byte[] descriptorBytes;
        public final List<DvbSubtitleInfo> dvbSubtitleInfos;
        public final String language;
        public final int streamType;

        @Target({ElementType.TYPE_USE})
        @Documented
        @Retention(RetentionPolicy.SOURCE)
        public @interface AudioType {
        }

        public EsInfo(int r2, String r3, int r4, List<DvbSubtitleInfo> r5, byte[] r6) {
            this.streamType = r2;
            this.language = r3;
            this.audioType = r4;
            if (r5 != null) goto L5;
            List<DvbSubtitleInfo> r0 = Collections.emptyList();
        L6:
            this.dvbSubtitleInfos = r0;
            this.descriptorBytes = r6;
            return;
        L5:
            r0 = Collections.unmodifiableList(r5);
            goto L6
        }

        public int getRoleFlags() {
            switch(this.audioType) {
                case 2: goto L8;
                case 3: goto L6;
                default: goto L4;
            };
        L4:
            return 0;
        L6:
            return 512;
        L8:
            return 2048;
        }
    }

    public interface Factory {
        SparseArray<TsPayloadReader> createInitialPayloadReaders();

        TsPayloadReader createPayloadReader(int r1, EsInfo r2);
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public static final class TrackIdGenerator {
        private static final int ID_UNSET = Integer.MIN_VALUE;
        private final int firstTrackId;
        private String formatId;
        private final String formatIdPrefix;
        private int trackId;
        private final int trackIdIncrement;

        public TrackIdGenerator(int r2, int r3) {
            this(Integer.MIN_VALUE, r2, r3);
        }

        public TrackIdGenerator(int r5, int r6, int r7) {
            if (r5 == Integer.MIN_VALUE) goto L5;
            String r2 = r5 + "/";
        L6:
            this.formatIdPrefix = r2;
            this.firstTrackId = r6;
            this.trackIdIncrement = r7;
            this.trackId = Integer.MIN_VALUE;
            this.formatId = "";
            return;
        L5:
            r2 = "";
            goto L6
        }

        private void maybeThrowUninitializedError() {
            if (this.trackId == Integer.MIN_VALUE) goto L6;
            return;
        L6:
            throw new IllegalStateException("generateNewId() must be called before retrieving ids.");
        }

        public void generateNewId() {
            if (this.trackId != Integer.MIN_VALUE) goto L5;
            int r0 = this.firstTrackId;
        L6:
            this.trackId = r0;
            this.formatId = this.formatIdPrefix + this.trackId;
            return;
        L5:
            r0 = this.trackId + this.trackIdIncrement;
            goto L6
        }

        public String getFormatId() {
            maybeThrowUninitializedError();
            return this.formatId;
        }

        public int getTrackId() {
            maybeThrowUninitializedError();
            return this.trackId;
        }
    }

    void consume(ParsableByteArray r1, int r2) throws ParserException;

    void init(TimestampAdjuster r1, ExtractorOutput r2, TrackIdGenerator r3);

    void seek();
}
