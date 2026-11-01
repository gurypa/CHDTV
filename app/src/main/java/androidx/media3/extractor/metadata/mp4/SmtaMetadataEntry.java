package androidx.media3.extractor.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import com.google.common.primitives.Floats;

/* JADX INFO: loaded from: classes.dex */
public final class SmtaMetadataEntry implements Metadata.Entry {
    public static final Parcelable.Creator<SmtaMetadataEntry> CREATOR = null;
    public final float captureFrameRate;
    public final int svcTemporalLayerCount;

    static {
        CREATOR = new C04301();
    }

    public SmtaMetadataEntry(float r1, int r2) {
        this.captureFrameRate = r1;
        this.svcTemporalLayerCount = r2;
    }

    private SmtaMetadataEntry(Parcel r2) {
        this.captureFrameRate = r2.readFloat();
        this.svcTemporalLayerCount = r2.readInt();
    }

    /* synthetic */ SmtaMetadataEntry(Parcel r1, C04301 r2) {
        this(r1);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L17;
        SmtaMetadataEntry r2 = (SmtaMetadataEntry) r6;
        if (this.captureFrameRate != r2.captureFrameRate) goto L16;
        if (this.svcTemporalLayerCount != r2.svcTemporalLayerCount) goto L16;
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
        return (((17 * 31) + Floats.hashCode(this.captureFrameRate)) * 31) + this.svcTemporalLayerCount;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "smta: captureFrameRate=" + this.captureFrameRate + ", svcTemporalLayerCount=" + this.svcTemporalLayerCount;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeFloat(this.captureFrameRate);
        r2.writeInt(this.svcTemporalLayerCount);
    }
}
