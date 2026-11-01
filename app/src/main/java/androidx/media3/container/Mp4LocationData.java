package androidx.media3.container;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import com.google.common.primitives.Floats;

/* JADX INFO: loaded from: classes.dex */
public final class Mp4LocationData implements Metadata.Entry {
    public static final Parcelable.Creator<Mp4LocationData> CREATOR = null;
    public final float latitude;
    public final float longitude;

    static {
        CREATOR = new C02041();
    }

    public Mp4LocationData(float r3, float r4) {
        if (r3 >= (-90.0f)) goto L5;
    L11:
        boolean r0 = false;
    L12:
        Assertions.checkArgument(r0, "Invalid latitude or longitude");
        this.latitude = r3;
        this.longitude = r4;
        return;
    L5:
        if (r3 > 90.0f) goto L11;
        if (r4 < (-180.0f)) goto L11;
        if (r4 > 180.0f) goto L11;
        r0 = true;
        goto L12
    }

    private Mp4LocationData(Parcel r2) {
        this.latitude = r2.readFloat();
        this.longitude = r2.readFloat();
    }

    /* synthetic */ Mp4LocationData(Parcel r1, C02041 r2) {
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
        Mp4LocationData r2 = (Mp4LocationData) r6;
        if (this.latitude != r2.latitude) goto L16;
        if (this.longitude != r2.longitude) goto L16;
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
        return (((17 * 31) + Floats.hashCode(this.latitude)) * 31) + Floats.hashCode(this.longitude);
    }

    @Override // androidx.media3.common.Metadata.Entry
    public /* synthetic */ void populateMediaMetadata(MediaMetadata.Builder r1) {
        Metadata.Entry.CC.$default$populateMediaMetadata(this, r1);
    }

    public String toString() {
        return "xyz: latitude=" + this.latitude + ", longitude=" + this.longitude;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeFloat(this.latitude);
        r2.writeFloat(this.longitude);
    }
}
