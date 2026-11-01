package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import androidx.collection.ArrayMap;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class VersionedParcelParcel extends VersionedParcel {
    private static final boolean DEBUG = false;
    private static final String TAG = "VersionedParcelParcel";
    private int mCurrentField;
    private final int mEnd;
    private int mFieldId;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;

    VersionedParcelParcel(Parcel r9) {
        this(r9, r9.dataPosition(), r9.dataSize(), "", new ArrayMap(), new ArrayMap(), new ArrayMap());
    }

    private VersionedParcelParcel(Parcel r3, int r4, int r5, String r6, ArrayMap<String, Method> r7, ArrayMap<String, Method> r8, ArrayMap<String, Class> r9) {
        super(r7, r8, r9);
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mFieldId = -1;
        this.mParcel = r3;
        this.mOffset = r4;
        this.mEnd = r5;
        this.mNextRead = this.mOffset;
        this.mPrefix = r6;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void closeField() {
        if (this.mCurrentField < 0) goto L6;
        int r0 = this.mPositionLookup.get(this.mCurrentField);
        int r1 = this.mParcel.dataPosition();
        this.mParcel.setDataPosition(r0);
        Parcel r3 = this.mParcel;
        r3.writeInt(r1 - r0);
        this.mParcel.setDataPosition(r1);
        return;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected VersionedParcel createSubParcel() {
        Parcel r1 = this.mParcel;
        int r2 = this.mParcel.dataPosition();
        if (this.mNextRead != this.mOffset) goto L5;
        int r3 = this.mEnd;
    L7:
        return new VersionedParcelParcel(r1, r2, r3, this.mPrefix + "  ", this.mReadCache, this.mWriteCache, this.mParcelizerCache);
    L5:
        r3 = this.mNextRead;
        goto L7
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean readBoolean() {
        if (this.mParcel.readInt() == 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public Bundle readBundle() {
        return this.mParcel.readBundle(getClass().getClassLoader());
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public byte[] readByteArray() {
        int r0 = this.mParcel.readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        byte[] r1 = new byte[r0];
        this.mParcel.readByteArray(r1);
        return r1;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected CharSequence readCharSequence() {
        return (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.mParcel);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public double readDouble() {
        return this.mParcel.readDouble();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean readField(int r5) {
    L3:
        if (this.mNextRead >= this.mEnd) goto L12;
        if (this.mFieldId == r5) goto L6;
        if (String.valueOf(this.mFieldId).compareTo(String.valueOf(r5)) > 0) goto L9;
        this.mParcel.setDataPosition(this.mNextRead);
        int r0 = this.mParcel.readInt();
        this.mFieldId = this.mParcel.readInt();
        this.mNextRead += r0;
        goto L3
    L9:
        return false;
    L6:
        return true;
    L12:
        if (this.mFieldId != r5) goto L15;
        return true;
    L15:
        return false;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public float readFloat() {
        return this.mParcel.readFloat();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public int readInt() {
        return this.mParcel.readInt();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public long readLong() {
        return this.mParcel.readLong();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public <T extends Parcelable> T readParcelable() {
        return (T) this.mParcel.readParcelable(getClass().getClassLoader());
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public String readString() {
        return this.mParcel.readString();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public IBinder readStrongBinder() {
        return this.mParcel.readStrongBinder();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void setOutputField(int r3) {
        closeField();
        this.mCurrentField = r3;
        this.mPositionLookup.put(r3, this.mParcel.dataPosition());
        writeInt(0);
        writeInt(r3);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeBoolean(boolean r2) {
        this.mParcel.writeInt(r2 ? 1 : 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeBundle(Bundle r2) {
        this.mParcel.writeBundle(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeByteArray(byte[] r3) {
        if (r3 == null) goto L4;
        this.mParcel.writeInt(r3.length);
        this.mParcel.writeByteArray(r3);
        return;
    L4:
        this.mParcel.writeInt(-1);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeByteArray(byte[] r3, int r4, int r5) {
        if (r3 == null) goto L4;
        this.mParcel.writeInt(r3.length);
        this.mParcel.writeByteArray(r3, r4, r5);
        return;
    L4:
        this.mParcel.writeInt(-1);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected void writeCharSequence(CharSequence r3) {
        TextUtils.writeToParcel(r3, this.mParcel, 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeDouble(double r2) {
        this.mParcel.writeDouble(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeFloat(float r2) {
        this.mParcel.writeFloat(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeInt(int r2) {
        this.mParcel.writeInt(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeLong(long r2) {
        this.mParcel.writeLong(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeParcelable(Parcelable r3) {
        this.mParcel.writeParcelable(r3, 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeString(String r2) {
        this.mParcel.writeString(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeStrongBinder(IBinder r2) {
        this.mParcel.writeStrongBinder(r2);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeStrongInterface(IInterface r2) {
        this.mParcel.writeStrongInterface(r2);
    }
}
