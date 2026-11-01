package androidx.media3.extractor.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class PrivateCommand extends SpliceCommand {
    public static final Parcelable.Creator<PrivateCommand> CREATOR = null;
    public final byte[] commandBytes;
    public final long identifier;
    public final long ptsAdjustment;

    static {
        CREATOR = new C04311();
    }

    private PrivateCommand(long r1, byte[] r3, long r4) {
        this.ptsAdjustment = r4;
        this.identifier = r1;
        this.commandBytes = r3;
    }

    private PrivateCommand(Parcel r3) {
        this.ptsAdjustment = r3.readLong();
        this.identifier = r3.readLong();
        this.commandBytes = (byte[]) Util.castNonNull(r3.createByteArray());
    }

    /* synthetic */ PrivateCommand(Parcel r1, C04311 r2) {
        this(r1);
    }

    static PrivateCommand parseFromSection(ParsableByteArray r6, int r7, long r8) {
        long r1 = r6.readUnsignedInt();
        byte[] r3 = new byte[r7 - 4];
        r6.readBytes(r3, 0, r3.length);
        return new PrivateCommand(r1, r3, r8);
    }

    @Override // androidx.media3.extractor.metadata.scte35.SpliceCommand
    public String toString() {
        return "SCTE-35 PrivateCommand { ptsAdjustment=" + this.ptsAdjustment + ", identifier= " + this.identifier + " }";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeLong(this.ptsAdjustment);
        r3.writeLong(this.identifier);
        r3.writeByteArray(this.commandBytes);
    }
}
