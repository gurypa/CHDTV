package androidx.media3.common;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class StreamKey implements Comparable<StreamKey>, Parcelable {
    public static final Parcelable.Creator<StreamKey> CREATOR = null;
    private static final String FIELD_GROUP_INDEX = null;
    private static final String FIELD_PERIOD_INDEX = null;
    private static final String FIELD_STREAM_INDEX = null;
    public final int groupIndex;
    public final int periodIndex;
    public final int streamIndex;

    static {
        CREATOR = new C01951();
        FIELD_PERIOD_INDEX = Util.intToStringMaxRadix(0);
        FIELD_GROUP_INDEX = Util.intToStringMaxRadix(1);
        FIELD_STREAM_INDEX = Util.intToStringMaxRadix(2);
    }

    public StreamKey(int r2, int r3) {
        this(0, r2, r3);
    }

    public StreamKey(int r1, int r2, int r3) {
        this.periodIndex = r1;
        this.groupIndex = r2;
        this.streamIndex = r3;
    }

    StreamKey(Parcel r2) {
        this.periodIndex = r2.readInt();
        this.groupIndex = r2.readInt();
        this.streamIndex = r2.readInt();
    }

    public static StreamKey fromBundle(Bundle r5) {
        return new StreamKey(r5.getInt(FIELD_PERIOD_INDEX, 0), r5.getInt(FIELD_GROUP_INDEX, 0), r5.getInt(FIELD_STREAM_INDEX, 0));
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(StreamKey r4) {
        int r0 = this.periodIndex - r4.periodIndex;
        if (r0 != 0) goto L8;
        int r1 = this.groupIndex - r4.groupIndex;
        if (r1 == 0) goto L7;
        return r1;
    L7:
        return this.streamIndex - r4.streamIndex;
    L8:
        return r0;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(StreamKey r1) {
        return compareTo2(r1);
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
    L19:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L19;
        StreamKey r2 = (StreamKey) r6;
        if (this.periodIndex != r2.periodIndex) goto L18;
        if (this.groupIndex != r2.groupIndex) goto L18;
        if (this.streamIndex != r2.streamIndex) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        return (((this.periodIndex * 31) + this.groupIndex) * 31) + this.streamIndex;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        if (this.periodIndex == 0) goto L6;
        r0.putInt(FIELD_PERIOD_INDEX, this.periodIndex);
    L6:
        if (this.groupIndex == 0) goto L9;
        r0.putInt(FIELD_GROUP_INDEX, this.groupIndex);
    L9:
        if (this.streamIndex == 0) goto L11;
        r0.putInt(FIELD_STREAM_INDEX, this.streamIndex);
    L11:
        return r0;
    }

    public String toString() {
        return this.periodIndex + "." + this.groupIndex + "." + this.streamIndex;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r2, int r3) {
        r2.writeInt(this.periodIndex);
        r2.writeInt(this.groupIndex);
        r2.writeInt(this.streamIndex);
    }
}
