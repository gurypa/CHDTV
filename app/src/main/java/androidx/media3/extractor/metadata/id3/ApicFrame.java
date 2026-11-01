package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class ApicFrame extends Id3Frame {
    public static final Parcelable.Creator<ApicFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f50ID = "APIC";
    public final String description;
    public final String mimeType;
    public final byte[] pictureData;
    public final int pictureType;

    static {
        CREATOR = new C04161();
    }

    ApicFrame(Parcel r2) {
        super(f50ID);
        this.mimeType = (String) Util.castNonNull(r2.readString());
        this.description = r2.readString();
        this.pictureType = r2.readInt();
        this.pictureData = (byte[]) Util.castNonNull(r2.createByteArray());
    }

    public ApicFrame(String r2, String r3, int r4, byte[] r5) {
        super(f50ID);
        this.mimeType = r2;
        this.description = r3;
        this.pictureType = r4;
        this.pictureData = r5;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L21:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L21;
        ApicFrame r2 = (ApicFrame) r6;
        if (this.pictureType != r2.pictureType) goto L20;
        if (Util.areEqual(this.mimeType, r2.mimeType) == false) goto L20;
        if (Util.areEqual(this.description, r2.description) == false) goto L20;
        if (Arrays.equals(this.pictureData, r2.pictureData) == false) goto L20;
        return true;
    L20:
        return false;
    }

    public int hashCode() {
        int r0 = ((17 * 31) + this.pictureType) * 31;
        int r3 = 0;
        if (this.mimeType == null) goto L5;
        int r2 = this.mimeType.hashCode();
    L6:
        int r1 = (r0 + r2) * 31;
        if (this.description == null) goto L10;
        r3 = this.description.hashCode();
    L10:
        return ((r1 + r3) * 31) + Arrays.hashCode(this.pictureData);
    L5:
        r2 = 0;
        goto L6
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame, androidx.media3.common.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder r3) {
        r3.maybeSetArtworkData(this.pictureData, this.pictureType);
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame
    public String toString() {
        return this.f55id + ": mimeType=" + this.mimeType + ", description=" + this.description;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeString(this.mimeType);
        r2.writeString(this.description);
        r2.writeInt(this.pictureType);
        r2.writeByteArray(this.pictureData);
    }
}
