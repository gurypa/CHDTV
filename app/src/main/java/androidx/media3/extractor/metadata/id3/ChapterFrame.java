package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class ChapterFrame extends Id3Frame {
    public static final Parcelable.Creator<ChapterFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f51ID = "CHAP";
    public final String chapterId;
    public final long endOffset;
    public final int endTimeMs;
    public final long startOffset;
    public final int startTimeMs;
    private final Id3Frame[] subFrames;

    static {
        CREATOR = new C04181();
    }

    ChapterFrame(Parcel r5) {
        super(f51ID);
        this.chapterId = (String) Util.castNonNull(r5.readString());
        this.startTimeMs = r5.readInt();
        this.endTimeMs = r5.readInt();
        this.startOffset = r5.readLong();
        this.endOffset = r5.readLong();
        int r0 = r5.readInt();
        this.subFrames = new Id3Frame[r0];
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.subFrames[r1] = (Id3Frame) r5.readParcelable(Id3Frame.class.getClassLoader());
        r1 = r1 + 1;
        goto L3
    }

    public ChapterFrame(String r2, int r3, int r4, long r5, long r7, Id3Frame[] r9) {
        super(f51ID);
        this.chapterId = r2;
        this.startTimeMs = r3;
        this.endTimeMs = r4;
        this.startOffset = r5;
        this.endOffset = r7;
        this.subFrames = r9;
    }

    @Override // androidx.media3.extractor.metadata.id3.Id3Frame, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L25:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L25;
        ChapterFrame r2 = (ChapterFrame) r8;
        if (this.startTimeMs != r2.startTimeMs) goto L24;
        if (this.endTimeMs != r2.endTimeMs) goto L24;
        if (this.startOffset != r2.startOffset) goto L24;
        if (this.endOffset != r2.endOffset) goto L24;
        if (Util.areEqual(this.chapterId, r2.chapterId) == false) goto L24;
        if (Arrays.equals(this.subFrames, r2.subFrames) == false) goto L24;
        return true;
    L24:
        return false;
    }

    public Id3Frame getSubFrame(int r2) {
        return this.subFrames[r2];
    }

    public int getSubFrameCount() {
        return this.subFrames.length;
    }

    public int hashCode() {
        int r1 = ((((((((17 * 31) + this.startTimeMs) * 31) + this.endTimeMs) * 31) + ((int) this.startOffset)) * 31) + ((int) this.endOffset)) * 31;
        if (this.chapterId == null) goto L5;
        int r2 = this.chapterId.hashCode();
    L7:
        return r1 + r2;
    L5:
        r2 = 0;
        goto L7
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r6, int r7) {
        r6.writeString(this.chapterId);
        r6.writeInt(this.startTimeMs);
        r6.writeInt(this.endTimeMs);
        r6.writeLong(this.startOffset);
        r6.writeLong(this.endOffset);
        r6.writeInt(this.subFrames.length);
        Id3Frame[] r0 = this.subFrames;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r6.writeParcelable(r0[r3], 0);
        r3 = r3 + 1;
        goto L3
    }
}
