package android.support.v4.media.session;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class ParcelableVolumeInfo implements Parcelable {
    public static final Parcelable.Creator<ParcelableVolumeInfo> CREATOR = null;
    public int audioStream;
    public int controlType;
    public int currentVolume;
    public int maxVolume;
    public int volumeType;

    static {
        CREATOR = new C00261();
    }

    public ParcelableVolumeInfo(int r1, int r2, int r3, int r4, int r5) {
        this.volumeType = r1;
        this.audioStream = r2;
        this.controlType = r3;
        this.maxVolume = r4;
        this.currentVolume = r5;
    }

    public ParcelableVolumeInfo(Parcel r2) {
        this.volumeType = r2.readInt();
        this.controlType = r2.readInt();
        this.maxVolume = r2.readInt();
        this.currentVolume = r2.readInt();
        this.audioStream = r2.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.volumeType);
        r2.writeInt(this.controlType);
        r2.writeInt(this.maxVolume);
        r2.writeInt(this.currentVolume);
        r2.writeInt(this.audioStream);
    }
}
