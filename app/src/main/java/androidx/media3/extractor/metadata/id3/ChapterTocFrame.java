package androidx.media3.extractor.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class ChapterTocFrame extends Id3Frame {
    public static final Parcelable.Creator<ChapterTocFrame> CREATOR = null;

    /* JADX INFO: renamed from: ID */
    public static final String f52ID = "CTOC";
    public final String[] children;
    public final String elementId;
    public final boolean isOrdered;
    public final boolean isRoot;
    private final Id3Frame[] subFrames;

    static {
        CREATOR = new C04191();
    }

    ChapterTocFrame(Parcel r5) {
        super(f52ID);
        this.elementId = (String) Util.castNonNull(r5.readString());
        boolean r1 = false;
        if (r5.readByte() == 0) goto L5;
        boolean r0 = true;
    L6:
        this.isRoot = r0;
        if (r5.readByte() == 0) goto L9;
        r1 = true;
    L9:
        this.isOrdered = r1;
        this.children = (String[]) Util.castNonNull(r5.createStringArray());
        int r2 = r5.readInt();
        this.subFrames = new Id3Frame[r2];
        int r3 = 0;
    L10:
        if (r3 >= r2) goto L12;
        this.subFrames[r3] = (Id3Frame) r5.readParcelable(Id3Frame.class.getClassLoader());
        r3 = r3 + 1;
        goto L10
    L12:
        return;
    L5:
        r0 = false;
        goto L6
    }

    public ChapterTocFrame(String r2, boolean r3, boolean r4, String[] r5, Id3Frame[] r6) {
        super(f52ID);
        this.elementId = r2;
        this.isRoot = r3;
        this.isOrdered = r4;
        this.children = r5;
        this.subFrames = r6;
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
        ChapterTocFrame r2 = (ChapterTocFrame) r6;
        if (this.isRoot != r2.isRoot) goto L22;
        if (this.isOrdered != r2.isOrdered) goto L22;
        if (Util.areEqual(this.elementId, r2.elementId) == false) goto L22;
        if (Arrays.equals(this.children, r2.children) == false) goto L22;
        if (Arrays.equals(this.subFrames, r2.subFrames) == false) goto L22;
        return true;
    L22:
        return false;
    }

    public Id3Frame getSubFrame(int r2) {
        return this.subFrames[r2];
    }

    public int getSubFrameCount() {
        return this.subFrames.length;
    }

    public int hashCode() {
        int r1 = ((((17 * 31) + (this.isRoot ? 1 : 0)) * 31) + (this.isOrdered ? 1 : 0)) * 31;
        if (this.elementId == null) goto L5;
        int r2 = this.elementId.hashCode();
    L7:
        return r1 + r2;
    L5:
        r2 = 0;
        goto L7
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r6, int r7) {
        r6.writeString(this.elementId);
        r6.writeByte(this.isRoot ? 1 : 0);
        r6.writeByte(this.isOrdered ? 1 : 0);
        r6.writeStringArray(this.children);
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
