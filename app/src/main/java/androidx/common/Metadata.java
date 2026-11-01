package androidx.media3.common;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.util.Util;
import com.google.common.primitives.Longs;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Metadata implements Parcelable {
    public static final Parcelable.Creator<Metadata> CREATOR = null;
    private final Entry[] entries;
    public final long presentationTimeUs;

    public interface Entry extends Parcelable {

        /* JADX INFO: renamed from: androidx.media3.common.Metadata$Entry$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static byte[] $default$getWrappedMetadataBytes(Entry r1) {
                return null;
            }

            public static Format $default$getWrappedMetadataFormat(Entry r1) {
                return null;
            }

            public static void $default$populateMediaMetadata(Entry r0, MediaMetadata.Builder r1) {
            }
        }

        byte[] getWrappedMetadataBytes();

        Format getWrappedMetadataFormat();

        void populateMediaMetadata(MediaMetadata.Builder r1);
    }

    static {
        CREATOR = new C01911();
    }

    public Metadata(long r2, List<? extends Entry> r4) {
        this(r2, (Entry[]) r4.toArray(new Entry[0]));
    }

    public Metadata(long r1, Entry... r3) {
        this.presentationTimeUs = r1;
        this.entries = r3;
    }

    Metadata(Parcel r4) {
        this.entries = new Entry[r4.readInt()];
        int r0 = 0;
    L4:
        if (r0 >= this.entries.length) goto L6;
        this.entries[r0] = (Entry) r4.readParcelable(Entry.class.getClassLoader());
        r0 = r0 + 1;
        goto L4
    L6:
        this.presentationTimeUs = r4.readLong();
    }

    public Metadata(List<? extends Entry> r2) {
        this((Entry[]) r2.toArray(new Entry[0]));
    }

    public Metadata(Entry... r3) {
        this(C0180C.TIME_UNSET, r3);
    }

    public Metadata copyWithAppendedEntries(Entry... r5) {
        if (r5.length != 0) goto L6;
        return this;
    L6:
        return new Metadata(this.presentationTimeUs, (Entry[]) Util.nullSafeArrayConcatenation(this.entries, r5));
    }

    public Metadata copyWithAppendedEntriesFrom(Metadata r2) {
        if (r2 != null) goto L5;
        return this;
    L5:
        return copyWithAppendedEntries(r2.entries);
    }

    public Metadata copyWithPresentationTimeUs(long r3) {
        if (this.presentationTimeUs != r3) goto L6;
        return this;
    L6:
        return new Metadata(r3, this.entries);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if (r8 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r8.getClass()) goto L17;
        Metadata r2 = (Metadata) r8;
        if (Arrays.equals(this.entries, r2.entries) == false) goto L16;
        if (this.presentationTimeUs != r2.presentationTimeUs) goto L16;
        return true;
    L16:
        return false;
    }

    public Entry get(int r2) {
        return this.entries[r2];
    }

    public int hashCode() {
        return (Arrays.hashCode(this.entries) * 31) + Longs.hashCode(this.presentationTimeUs);
    }

    public int length() {
        return this.entries.length;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append("entries=").append(Arrays.toString(this.entries));
        if (this.presentationTimeUs != C0180C.TIME_UNSET) goto L5;
        String r1 = "";
    L7:
        return r0.append(r1).toString();
    L5:
        r1 = ", presentationTimeUs=" + this.presentationTimeUs;
        goto L7
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r6, int r7) {
        r6.writeInt(this.entries.length);
        Entry[] r0 = this.entries;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r6.writeParcelable(r0[r3], 0);
        r3 = r3 + 1;
        goto L3
    L5:
        r6.writeLong(this.presentationTimeUs);
    }
}
