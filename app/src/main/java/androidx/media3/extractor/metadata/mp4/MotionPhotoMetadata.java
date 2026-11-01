package androidx.media3.extractor.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import com.google.common.primitives.Longs;

/* JADX INFO: loaded from: classes.dex */
public final class MotionPhotoMetadata implements Metadata.Entry {
    public static final Parcelable.Creator<MotionPhotoMetadata> CREATOR = null;
    public final long photoPresentationTimestampUs;
    public final long photoSize;
    public final long photoStartPosition;
    public final long videoSize;
    public final long videoStartPosition;

    static {
        CREATOR = new C04271();
    }

    public MotionPhotoMetadata(long r1, long r3, long r5, long r7, long r9) {
        this.photoStartPosition = r1;
        this.photoSize = r3;
        this.photoPresentationTimestampUs = r5;
        this.videoStartPosition = r7;
        this.videoSize = r9;
    }

    private MotionPhotoMetadata(Parcel r3) {
        this.photoStartPosition = r3.readLong();
        this.photoSize = r3.readLong();
        this.photoPresentationTimestampUs = r3.readLong();
        this.videoStartPosition = r3.readLong();
        this.videoSize = r3.readLong();
    }

    /* synthetic */ MotionPhotoMetadata(Parcel r1, C04271 r2) {
        this(r1);
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
    L23:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L23;
        MotionPhotoMetadata r2 = (MotionPhotoMetadata) r8;
        if (this.photoStartPosition != r2.photoStartPosition) goto L22;
        if (this.photoSize != r2.photoSize) goto L22;
        if (this.photoPresentationTimestampUs != r2.photoPresentationTimestampUs) goto L22;
        if (this.videoStartPosition != r2.videoStartPosition) goto L22;
        if (this.videoSize != r2.videoSize) goto L22;
        return true;
    L22:
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
        return (((((((((17 * 31) + Longs.hashCode(this.photoStartPosition)) * 31) + Longs.hashCode(this.photoSize)) * 31) + Longs.hashCode(this.photoPresentationTimestampUs)) * 31) + Longs.hashCode(this.videoStartPosition)) * 31) + Longs.hashCode(this.videoSize);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "Motion photo metadata: photoStartPosition=" + this.photoStartPosition + ", photoSize=" + this.photoSize + ", photoPresentationTimestampUs=" + this.photoPresentationTimestampUs + ", videoStartPosition=" + this.videoStartPosition + ", videoSize=" + this.videoSize;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeLong(this.photoStartPosition);
        r3.writeLong(this.photoSize);
        r3.writeLong(this.photoPresentationTimestampUs);
        r3.writeLong(this.videoStartPosition);
        r3.writeLong(this.videoSize);
    }
}
