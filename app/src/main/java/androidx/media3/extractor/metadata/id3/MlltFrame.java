package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class MlltFrame extends Id3Frame {
    public static final Parcelable.Creator<MlltFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f57ID = "MLLT";
    public final int bytesBetweenReference;
    public final int[] bytesDeviations;
    public final int millisecondsBetweenReference;
    public final int[] millisecondsDeviations;
    public final int mpegFramesBetweenReference;

    static {
        CREATOR = new C04231();
    }

    public MlltFrame(int r2, int r3, int r4, int[] r5, int[] r6) {
        super(f57ID);
        this.mpegFramesBetweenReference = r2;
        this.bytesBetweenReference = r3;
        this.millisecondsBetweenReference = r4;
        this.bytesDeviations = r5;
        this.millisecondsDeviations = r6;
    }

    MlltFrame(Parcel r2) {
        super(f57ID);
        this.mpegFramesBetweenReference = r2.readInt();
        this.bytesBetweenReference = r2.readInt();
        this.millisecondsBetweenReference = r2.readInt();
        this.bytesDeviations = (int[]) Util.castNonNull(r2.createIntArray());
        this.millisecondsDeviations = (int[]) Util.castNonNull(r2.createIntArray());
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L23:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L23;
        MlltFrame r2 = (MlltFrame) r6;
        if (this.mpegFramesBetweenReference != r2.mpegFramesBetweenReference) goto L22;
        if (this.bytesBetweenReference != r2.bytesBetweenReference) goto L22;
        if (this.millisecondsBetweenReference != r2.millisecondsBetweenReference) goto L22;
        if (Arrays.equals(this.bytesDeviations, r2.bytesDeviations) == false) goto L22;
        if (Arrays.equals(this.millisecondsDeviations, r2.millisecondsDeviations) == false) goto L22;
        return true;
    L22:
        return false;
    }

    public int hashCode() {
        return (((((((((17 * 31) + this.mpegFramesBetweenReference) * 31) + this.bytesBetweenReference) * 31) + this.millisecondsBetweenReference) * 31) + Arrays.hashCode(this.bytesDeviations)) * 31) + Arrays.hashCode(this.millisecondsDeviations);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.mpegFramesBetweenReference);
        r2.writeInt(this.bytesBetweenReference);
        r2.writeInt(this.millisecondsBetweenReference);
        r2.writeIntArray(this.bytesDeviations);
        r2.writeIntArray(this.millisecondsDeviations);
    }
}
