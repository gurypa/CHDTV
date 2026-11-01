package androidx.media3.extractor.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.C0180C;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;

/* JADX INFO: loaded from: classes.dex */
public final class TimeSignalCommand extends SpliceCommand {
    public static final Parcelable.Creator<TimeSignalCommand> CREATOR = null;
    public final long playbackPositionUs;
    public final long ptsTime;

    static {
        CREATOR = new C04351();
    }

    private TimeSignalCommand(long r1, long r3) {
        this.ptsTime = r1;
        this.playbackPositionUs = r3;
    }

    /* synthetic */ TimeSignalCommand(long r1, long r3, C04351 r5) {
        this(r1, r3);
    }

    static TimeSignalCommand parseFromSection(ParsableByteArray r5, long r6, TimestampAdjuster r8) {
        long r0 = parseSpliceTime(r5, r6);
        return new TimeSignalCommand(r0, r8.adjustTsTimestamp(r0));
    }

    static long parseSpliceTime(ParsableByteArray r8, long r9) {
        long r0 = r8.readUnsignedByte();
        if ((128 & r0) != 0) goto L5;
        return C0180C.TIME_UNSET;
    L5:
        return ((((1 & r0) << 32) | r8.readUnsignedInt()) + r9) & 8589934591L;
    }

    @Override // androidx.media3.extractor.metadata.scte35.SpliceCommand
    public String toString() {
        return "SCTE-35 TimeSignalCommand { ptsTime=" + this.ptsTime + ", playbackPositionUs= " + this.playbackPositionUs + " }";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeLong(this.ptsTime);
        r3.writeLong(this.playbackPositionUs);
    }
}
