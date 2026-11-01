package androidx.media3.extractor.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SlowMotionData implements Metadata.Entry {
    public static final Parcelable.Creator<SlowMotionData> CREATOR = null;
    public final List<Segment> segments;

    public static final class Segment implements Parcelable {
        public static final Comparator<Segment> BY_START_THEN_END_THEN_DIVISOR = null;
        public static final Parcelable.Creator<Segment> CREATOR = null;
        public final long endTimeMs;
        public final int speedDivisor;
        public final long startTimeMs;

        static {
            BY_START_THEN_END_THEN_DIVISOR = new SlowMotionData$Segment$$ExternalSyntheticLambda0();
            CREATOR = new C04291();
        }

        public Segment(long r2, long r4, int r6) {
            if (r2 >= r4) goto L5;
            boolean r0 = true;
        L6:
            Assertions.checkArgument(r0);
            this.startTimeMs = r2;
            this.endTimeMs = r4;
            this.speedDivisor = r6;
            return;
        L5:
            r0 = false;
            goto L6
        }

        static /* synthetic */ int lambda$static$0(Segment r5, Segment r6) {
            return ComparisonChain.start().compare(r5.startTimeMs, r6.startTimeMs).compare(r5.endTimeMs, r6.endTimeMs).compare(r5.speedDivisor, r6.speedDivisor).result();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object r8) {
            if (this != r8) goto L6;
            return true;
        L6:
            if (r8 != null) goto L8;
        L19:
            return false;
        L8:
            if (getClass() != r8.getClass()) goto L19;
            Segment r2 = (Segment) r8;
            if (this.startTimeMs != r2.startTimeMs) goto L18;
            if (this.endTimeMs != r2.endTimeMs) goto L18;
            if (this.speedDivisor != r2.speedDivisor) goto L18;
            return true;
        L18:
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{Long.valueOf(this.startTimeMs), Long.valueOf(this.endTimeMs), Integer.valueOf(this.speedDivisor)});
        }

        public String toString() {
            return Util.formatInvariant("Segment: startTimeMs=%d, endTimeMs=%d, speedDivisor=%d", new Object[]{Long.valueOf(this.startTimeMs), Long.valueOf(this.endTimeMs), Integer.valueOf(this.speedDivisor)});
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r3, int r4) {
            r3.writeLong(this.startTimeMs);
            r3.writeLong(this.endTimeMs);
            r3.writeInt(this.speedDivisor);
        }
    }

    static {
        CREATOR = new C04281();
    }

    public SlowMotionData(List<Segment> r2) {
        this.segments = r2;
        Assertions.checkArgument(!doSegmentsOverlap(r2));
    }

    private static boolean doSegmentsOverlap(List<Segment> r6) {
        if (r6.isEmpty() == false) goto L5;
        return false;
    L5:
        long r2 = r6.get(0).endTimeMs;
        int r0 = 1;
    L7:
        if (r0 >= r6.size()) goto L13;
        if (r6.get(r0).startTimeMs < r2) goto L10;
        r2 = r6.get(r0).endTimeMs;
        r0 = r0 + 1;
        goto L7
    L10:
        return true;
    L13:
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r4) {
        if (this != r4) goto L5;
        return true;
    L5:
        if (r4 != null) goto L7;
        return false;
    L7:
        if (getClass() == r4.getClass()) goto L10;
        return false;
    L10:
        return this.segments.equals(((SlowMotionData) r4).segments);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ byte[] getWrappedMetadataBytes() {
        return Metadata.Entry.CC.$default$getWrappedMetadataBytes(this);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ Format getWrappedMetadataFormat() {
        return Metadata.Entry.CC.$default$getWrappedMetadataFormat(this);
    }

    public int hashCode() {
        return this.segments.hashCode();
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "SlowMotion: segments=" + this.segments;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeList(this.segments);
    }
}
