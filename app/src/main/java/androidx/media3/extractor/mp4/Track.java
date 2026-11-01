package androidx.media3.extractor.mp4;

import androidx.media3.common.Format;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
public final class Track {
    public static final int TRANSFORMATION_CEA608_CDAT = 1;
    public static final int TRANSFORMATION_NONE = 0;
    public final long durationUs;
    public final long[] editListDurations;
    public final long[] editListMediaTimes;
    public final Format format;

    /* JADX INFO: renamed from: id */
    public final int f60id;
    public final long mediaDurationUs;
    public final long movieTimescale;
    public final int nalUnitLengthFieldLength;
    private final TrackEncryptionBox[] sampleDescriptionEncryptionBoxes;
    public final int sampleTransformation;
    public final long timescale;
    public final int type;

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Transformation {
    }

    public Track(int r17, int r18, long r19, long r21, long r23, long r25, Format r27, int r28, TrackEncryptionBox[] r29, int r30, long[] r31, long[] r32) {
        this.f60id = r17;
        this.type = r18;
        this.timescale = r19;
        this.movieTimescale = r21;
        this.durationUs = r23;
        this.mediaDurationUs = r25;
        this.format = r27;
        this.sampleTransformation = r28;
        this.sampleDescriptionEncryptionBoxes = r29;
        this.nalUnitLengthFieldLength = r30;
        this.editListDurations = r31;
        this.editListMediaTimes = r32;
    }

    public Track copyWithFormat(Format r19) {
        return new Track(this.f60id, this.type, this.timescale, this.movieTimescale, this.durationUs, this.mediaDurationUs, r19, this.sampleTransformation, this.sampleDescriptionEncryptionBoxes, this.nalUnitLengthFieldLength, this.editListDurations, this.editListMediaTimes);
    }

    public Track copyWithoutEditLists() {
        return new Track(this.f60id, this.type, this.timescale, this.movieTimescale, this.durationUs, this.mediaDurationUs, this.format, this.sampleTransformation, this.sampleDescriptionEncryptionBoxes, this.nalUnitLengthFieldLength, null, null);
    }

    public TrackEncryptionBox getSampleDescriptionEncryptionBox(int r2) {
        if (this.sampleDescriptionEncryptionBoxes != null) goto L6;
        return null;
    L6:
        return this.sampleDescriptionEncryptionBoxes[r2];
    }
}
