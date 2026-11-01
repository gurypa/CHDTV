package androidx.media3.extractor.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class IcyInfo implements Metadata.Entry {
    public static final Parcelable.Creator<IcyInfo> CREATOR = null;
    public final byte[] rawMetadata;
    public final String title;
    public final String url;

    static {
        CREATOR = new C04151();
    }

    IcyInfo(Parcel r2) {
        this.rawMetadata = (byte[]) Assertions.checkNotNull(r2.createByteArray());
        this.title = r2.readString();
        this.url = r2.readString();
    }

    public IcyInfo(byte[] r1, String r2, String r3) {
        this.rawMetadata = r1;
        this.title = r2;
        this.url = r3;
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
        return Arrays.equals(this.rawMetadata, ((IcyInfo) r4).rawMetadata);
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
        return Arrays.hashCode(this.rawMetadata);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public void populateMediaMetadata(MediaMetadata.Builder r2) {
        if (this.title == null) goto L6;
        r2.setTitle(this.title);
        return;
    }

    public String toString() {
        return String.format("ICY: title=\"%s\", url=\"%s\", rawMetadata.length=\"%s\"", new Object[]{this.title, this.url, Integer.valueOf(this.rawMetadata.length)});
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeByteArray(this.rawMetadata);
        r2.writeString(this.title);
        r2.writeString(this.url);
    }
}
