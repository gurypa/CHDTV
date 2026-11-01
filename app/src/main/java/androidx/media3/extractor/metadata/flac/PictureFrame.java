package androidx.media3.extractor.metadata.flac;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class PictureFrame implements Metadata.Entry {
    public static final Parcelable.Creator<PictureFrame> CREATOR = null;
    public final int colors;
    public final int depth;
    public final String description;
    public final int height;
    public final String mimeType;
    public final byte[] pictureData;
    public final int pictureType;
    public final int width;

    static {
        CREATOR = new C04121();
    }

    public PictureFrame(int r1, String r2, String r3, int r4, int r5, int r6, int r7, byte[] r8) {
        this.pictureType = r1;
        this.mimeType = r2;
        this.description = r3;
        this.width = r4;
        this.height = r5;
        this.depth = r6;
        this.colors = r7;
        this.pictureData = r8;
    }

    PictureFrame(Parcel r2) {
        this.pictureType = r2.readInt();
        this.mimeType = (String) Util.castNonNull(r2.readString());
        this.description = (String) Util.castNonNull(r2.readString());
        this.width = r2.readInt();
        this.height = r2.readInt();
        this.depth = r2.readInt();
        this.colors = r2.readInt();
        this.pictureData = (byte[]) Util.castNonNull(r2.createByteArray());
    }

    public static PictureFrame fromPictureBlock(ParsableByteArray r12) {
        int r1 = r12.readInt();
        String r2 = MimeTypes.normalizeMimeType(r12.readString(r12.readInt(), StandardCharsets.US_ASCII));
        String r3 = r12.readString(r12.readInt());
        int r4 = r12.readInt();
        int r5 = r12.readInt();
        int r6 = r12.readInt();
        int r7 = r12.readInt();
        int r11 = r12.readInt();
        byte[] r8 = new byte[r11];
        r12.readBytes(r8, 0, r11);
        return new PictureFrame(r1, r2, r3, r4, r5, r6, r7, r8);
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
    L29:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L29;
        PictureFrame r2 = (PictureFrame) r6;
        if (this.pictureType != r2.pictureType) goto L28;
        if (this.mimeType.equals(r2.mimeType) == false) goto L28;
        if (this.description.equals(r2.description) == false) goto L28;
        if (this.width != r2.width) goto L28;
        if (this.height != r2.height) goto L28;
        if (this.depth != r2.depth) goto L28;
        if (this.colors != r2.colors) goto L28;
        if (Arrays.equals(this.pictureData, r2.pictureData) == false) goto L28;
        return true;
    L28:
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
        return (((((((((((((((17 * 31) + this.pictureType) * 31) + this.mimeType.hashCode()) * 31) + this.description.hashCode()) * 31) + this.width) * 31) + this.height) * 31) + this.depth) * 31) + this.colors) * 31) + Arrays.hashCode(this.pictureData);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder r3) {
        r3.maybeSetArtworkData(this.pictureData, this.pictureType);
    }

    public String toString() {
        return "Picture: mimeType=" + this.mimeType + ", description=" + this.description;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.pictureType);
        r2.writeString(this.mimeType);
        r2.writeString(this.description);
        r2.writeInt(this.width);
        r2.writeInt(this.height);
        r2.writeInt(this.depth);
        r2.writeInt(this.colors);
        r2.writeByteArray(this.pictureData);
    }
}
