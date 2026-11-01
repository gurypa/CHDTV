package androidx.media3.container;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import com.google.common.primitives.Longs;

/* JADX INFO: loaded from: classes.dex */
public final class Mp4TimestampData implements Metadata.Entry {
    public static final Parcelable.Creator<Mp4TimestampData> CREATOR = null;
    public static final int TIMESCALE_UNSET = -1;
    private static final int UNIX_EPOCH_TO_MP4_TIME_DELTA_SECONDS = 2082844800;
    public final long creationTimestampSeconds;
    public final long modificationTimestampSeconds;
    public final long timescale;

    static {
        CREATOR = new C02061();
    }

    public Mp4TimestampData(long r3, long r5) {
        this.creationTimestampSeconds = r3;
        this.modificationTimestampSeconds = r5;
        this.timescale = -1;
    }

    public Mp4TimestampData(long r1, long r3, long r5) {
        this.creationTimestampSeconds = r1;
        this.modificationTimestampSeconds = r3;
        this.timescale = r5;
    }

    private Mp4TimestampData(Parcel r3) {
        this.creationTimestampSeconds = r3.readLong();
        this.modificationTimestampSeconds = r3.readLong();
        this.timescale = r3.readLong();
    }

    /* synthetic */ Mp4TimestampData(Parcel r1, C02061 r2) {
        this(r1);
    }

    public static long unixTimeToMp4TimeSeconds(long r4) {
        return (r4 / 1000) + 2082844800;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if ((r8 instanceof Mp4TimestampData) == true) goto L8;
        return false;
    L8:
        Mp4TimestampData r1 = (Mp4TimestampData) r8;
        if (this.creationTimestampSeconds != r1.creationTimestampSeconds) goto L16;
        if (this.modificationTimestampSeconds != r1.modificationTimestampSeconds) goto L16;
        if (this.timescale != r1.timescale) goto L16;
        return true;
    L16:
        return false;
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
        return (((((17 * 31) + Longs.hashCode(this.creationTimestampSeconds)) * 31) + Longs.hashCode(this.modificationTimestampSeconds)) * 31) + Longs.hashCode(this.timescale);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "Mp4Timestamp: creation time=" + this.creationTimestampSeconds + ", modification time=" + this.modificationTimestampSeconds + ", timescale=" + this.timescale;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeLong(this.creationTimestampSeconds);
        r3.writeLong(this.modificationTimestampSeconds);
        r3.writeLong(this.timescale);
    }
}
