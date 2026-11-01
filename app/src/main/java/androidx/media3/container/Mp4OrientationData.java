package androidx.media3.container;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;

/* JADX INFO: loaded from: classes.dex */
public final class Mp4OrientationData implements Metadata.Entry {
    public static final Parcelable.Creator<Mp4OrientationData> CREATOR = null;
    public final int orientation;

    static {
        CREATOR = new C02051();
    }

    public Mp4OrientationData(int r3) {
        if (r3 != 0) goto L5;
    L12:
        boolean r0 = true;
    L13:
        Assertions.checkArgument(r0, "Unsupported orientation");
        this.orientation = r3;
        return;
    L5:
        if (r3 == 90) goto L12;
        if (r3 == 180) goto L12;
        if (r3 == 270) goto L12;
        r0 = false;
        goto L13
    }

    private Mp4OrientationData(Parcel r2) {
        this.orientation = r2.readInt();
    }

    /* synthetic */ Mp4OrientationData(Parcel r1, C02051 r2) {
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
        if ((r6 instanceof Mp4OrientationData) == true) goto L9;
        return false;
    L9:
        if (this.orientation != ((Mp4OrientationData) r6).orientation) goto L12;
        return true;
    L12:
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
        return (17 * 31) + this.orientation;
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "Orientation= " + this.orientation;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.orientation);
    }
}
