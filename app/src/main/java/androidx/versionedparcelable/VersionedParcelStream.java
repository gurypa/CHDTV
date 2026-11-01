package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
class VersionedParcelStream extends VersionedParcel {
    private static final int TYPE_BOOLEAN = 5;
    private static final int TYPE_BOOLEAN_ARRAY = 6;
    private static final int TYPE_DOUBLE = 7;
    private static final int TYPE_DOUBLE_ARRAY = 8;
    private static final int TYPE_FLOAT = 13;
    private static final int TYPE_FLOAT_ARRAY = 14;
    private static final int TYPE_INT = 9;
    private static final int TYPE_INT_ARRAY = 10;
    private static final int TYPE_LONG = 11;
    private static final int TYPE_LONG_ARRAY = 12;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_STRING = 3;
    private static final int TYPE_STRING_ARRAY = 4;
    private static final int TYPE_SUB_BUNDLE = 1;
    private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
    private static final Charset UTF_16 = null;
    int mCount;
    private DataInputStream mCurrentInput;
    private DataOutputStream mCurrentOutput;
    private FieldBuffer mFieldBuffer;
    private int mFieldId;
    int mFieldSize;
    private boolean mIgnoreParcelables;
    private final DataInputStream mMasterInput;
    private final DataOutputStream mMasterOutput;

    private static class FieldBuffer {
        final DataOutputStream mDataStream;
        private final int mFieldId;
        final ByteArrayOutputStream mOutput;
        private final DataOutputStream mTarget;

        FieldBuffer(int r3, DataOutputStream r4) {
            this.mOutput = new ByteArrayOutputStream();
            this.mDataStream = new DataOutputStream(this.mOutput);
            this.mFieldId = r3;
            this.mTarget = r4;
        }

        void flushField() throws IOException {
            this.mDataStream.flush();
            int r0 = this.mOutput.size();
            int r1 = this.mFieldId << 16;
            if (r0 < 65535) goto L5;
            int r3 = 65535;
        L6:
            this.mTarget.writeInt(r1 | r3);
            if (r0 < 65535) goto L9;
            this.mTarget.writeInt(r0);
        L9:
            this.mOutput.writeTo(this.mTarget);
            return;
        L5:
            r3 = r0;
            goto L6
        }
    }

    static {
        UTF_16 = Charset.forName("UTF-16");
    }

    public VersionedParcelStream(InputStream r7, OutputStream r8) {
        this(r7, r8, new ArrayMap(), new ArrayMap(), new ArrayMap());
    }

    private VersionedParcelStream(final InputStream r4, OutputStream r5, ArrayMap<String, Method> r6, ArrayMap<String, Method> r7, ArrayMap<String, Class> r8) {
        super(r6, r7, r8);
        this.mCount = 0;
        this.mFieldId = -1;
        this.mFieldSize = -1;
        DataOutputStream r0 = null;
        if (r4 == null) goto L5;
        DataInputStream r1 = new DataInputStream(new C05241(this, r4));
    L6:
        this.mMasterInput = r1;
        if (r5 == null) goto L9;
        r0 = new DataOutputStream(r5);
    L9:
        this.mMasterOutput = r0;
        this.mCurrentInput = this.mMasterInput;
        this.mCurrentOutput = this.mMasterOutput;
        return;
    L5:
        r1 = null;
        goto L6
    }

    private void readObject(int r4, String r5, Bundle r6) {
        switch(r4) {
            case 0: goto L19;
            case 1: goto L18;
            case 2: goto L17;
            case 3: goto L16;
            case 4: goto L15;
            case 5: goto L14;
            case 6: goto L13;
            case 7: goto L12;
            case 8: goto L11;
            case 9: goto L10;
            case 10: goto L9;
            case 11: goto L8;
            case 12: goto L7;
            case 13: goto L6;
            case 14: goto L5;
            default: goto L4;
        };
    L5:
        r6.putFloatArray(r5, readFloatArray());
        return;
    L6:
        r6.putFloat(r5, readFloat());
        return;
    L7:
        r6.putLongArray(r5, readLongArray());
        return;
    L8:
        r6.putLong(r5, readLong());
        return;
    L9:
        r6.putIntArray(r5, readIntArray());
        return;
    L10:
        r6.putInt(r5, readInt());
        return;
    L11:
        r6.putDoubleArray(r5, readDoubleArray());
        return;
    L12:
        r6.putDouble(r5, readDouble());
        return;
    L13:
        r6.putBooleanArray(r5, readBooleanArray());
        return;
    L14:
        r6.putBoolean(r5, readBoolean());
        return;
    L15:
        r6.putStringArray(r5, (String[]) readArray(new String[0]));
        return;
    L16:
        r6.putString(r5, readString());
        return;
    L17:
        r6.putBundle(r5, readBundle());
        return;
    L18:
        r6.putBundle(r5, readBundle());
        return;
    L19:
        r6.putParcelable(r5, null);
        return;
    L4:
        throw new RuntimeException("Unknown type " + r4);
    }

    private void writeObject(Object r4) {
        if (r4 != null) goto L5;
        writeInt(0);
        return;
    L5:
        if ((r4 instanceof Bundle) == false) goto L8;
        writeInt(1);
        writeBundle((Bundle) r4);
        return;
    L8:
        if ((r4 instanceof String) == false) goto L11;
        writeInt(3);
        writeString((String) r4);
        return;
    L11:
        if ((r4 instanceof String[]) == false) goto L14;
        writeInt(4);
        writeArray((String[]) r4);
        return;
    L14:
        if ((r4 instanceof Boolean) == false) goto L17;
        writeInt(5);
        writeBoolean(((Boolean) r4).booleanValue());
        return;
    L17:
        if ((r4 instanceof boolean[]) == false) goto L20;
        writeInt(6);
        writeBooleanArray((boolean[]) r4);
        return;
    L20:
        if ((r4 instanceof Double) == false) goto L23;
        writeInt(7);
        writeDouble(((Double) r4).doubleValue());
        return;
    L23:
        if ((r4 instanceof double[]) == false) goto L26;
        writeInt(8);
        writeDoubleArray((double[]) r4);
        return;
    L26:
        if ((r4 instanceof Integer) == false) goto L29;
        writeInt(9);
        writeInt(((Integer) r4).intValue());
        return;
    L29:
        if ((r4 instanceof int[]) == false) goto L32;
        writeInt(10);
        writeIntArray((int[]) r4);
        return;
    L32:
        if ((r4 instanceof Long) == false) goto L35;
        writeInt(11);
        writeLong(((Long) r4).longValue());
        return;
    L35:
        if ((r4 instanceof long[]) == false) goto L38;
        writeInt(12);
        writeLongArray((long[]) r4);
        return;
    L38:
        if ((r4 instanceof Float) == false) goto L41;
        writeInt(13);
        writeFloat(((Float) r4).floatValue());
        return;
    L41:
        if ((r4 instanceof float[]) == false) goto L45;
        writeInt(14);
        writeFloatArray((float[]) r4);
        return;
    L45:
        throw new IllegalArgumentException("Unsupported type " + r4.getClass());
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void closeField() {
        if (this.mFieldBuffer != null) goto L12;
        return;
    L12:
    L8:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    L5:
        if (this.mFieldBuffer.mOutput.size() == 0) goto L7;
        this.mFieldBuffer.flushField();     // Catch: IOException -> L8
    L7:
        this.mFieldBuffer = null;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected VersionedParcel createSubParcel() {
        return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput, this.mReadCache, this.mWriteCache, this.mParcelizerCache);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean isStream() {
        return true;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean readBoolean() {
        return this.mCurrentInput.readBoolean();
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public Bundle readBundle() {
        int r0 = readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        Bundle r1 = new Bundle();
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        String r3 = readString();
        readObject(readInt(), r3, r1);
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public byte[] readByteArray() {
        int r0 = this.mCurrentInput.readInt();     // Catch: IOException -> L8
        if (r0 <= 0) goto L6;
        byte[] r1 = new byte[r0];     // Catch: IOException -> L8
        this.mCurrentInput.readFully(r1);     // Catch: IOException -> L8
        return r1;
    L6:
        return null;
    L8:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected CharSequence readCharSequence() {
        return null;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public double readDouble() {
        return this.mCurrentInput.readDouble();
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean readField(int r6) {
    L20:
        if (this.mFieldId == r6) goto L5;
        if (String.valueOf(this.mFieldId).compareTo(String.valueOf(r6)) > 0) goto L9;
        if (this.mCount >= this.mFieldSize) goto L13;
        this.mMasterInput.skip(this.mFieldSize - this.mCount);     // Catch: IOException -> L18
    L13:
        this.mFieldSize = -1;     // Catch: IOException -> L18
        int r1 = this.mMasterInput.readInt();     // Catch: IOException -> L18
        this.mCount = 0;     // Catch: IOException -> L18
        int r3 = r1 & 65535;     // Catch: IOException -> L18
        if (r3 != 65535) goto L16;
        r3 = this.mMasterInput.readInt();     // Catch: IOException -> L18
    L16:
        this.mFieldId = 65535 & (r1 >> 16);     // Catch: IOException -> L18
        this.mFieldSize = r3;     // Catch: IOException -> L18
        goto L20
    L9:
        return false;
    L5:
        return true;
    L19:
        return false;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public float readFloat() {
        return this.mCurrentInput.readFloat();
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public int readInt() {
        return this.mCurrentInput.readInt();
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public long readLong() {
        return this.mCurrentInput.readLong();
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public <T extends Parcelable> T readParcelable() {
        return null;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public String readString() {
        int r0 = this.mCurrentInput.readInt();     // Catch: IOException -> L8
        if (r0 <= 0) goto L6;
        byte[] r1 = new byte[r0];     // Catch: IOException -> L8
        this.mCurrentInput.readFully(r1);     // Catch: IOException -> L8
        return new String(r1, UTF_16);
    L6:
        return null;
    L8:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public IBinder readStrongBinder() {
        return null;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void setOutputField(int r3) {
        closeField();
        this.mFieldBuffer = new FieldBuffer(r3, this.mMasterOutput);
        this.mCurrentOutput = this.mFieldBuffer.mDataStream;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void setSerializationFlags(boolean r3, boolean r4) {
        if (r3 == false) goto L6;
        this.mIgnoreParcelables = r4;
        return;
    L6:
        throw new RuntimeException("Serialization of this object is not allowed");
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeBoolean(boolean r3) {
        this.mCurrentOutput.writeBoolean(r3);     // Catch: IOException -> L4
        return;
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeBundle(Bundle r5) {
        if (r5 == null) goto L8;
        Set<String> r0 = r5.keySet();     // Catch: IOException -> L11
        this.mCurrentOutput.writeInt(r0.size());     // Catch: IOException -> L11
        Iterator<String> r1 = r0.iterator();     // Catch: IOException -> L11
    L5:
        if (r1.hasNext() == false) goto L16;
        String r2 = r1.next();     // Catch: IOException -> L11
        writeString(r2);     // Catch: IOException -> L11
        writeObject(r5.get(r2));     // Catch: IOException -> L11
        goto L5
    L16:
        return;
    L8:
        this.mCurrentOutput.writeInt(-1);     // Catch: IOException -> L11
        return;
    L11:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeByteArray(byte[] r3) {
        if (r3 == null) goto L4;
        this.mCurrentOutput.writeInt(r3.length);     // Catch: IOException -> L7
        this.mCurrentOutput.write(r3);     // Catch: IOException -> L7
    L10:
        return;
    L4:
        this.mCurrentOutput.writeInt(-1);     // Catch: IOException -> L7
    L7:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeByteArray(byte[] r3, int r4, int r5) {
        if (r3 == null) goto L4;
        this.mCurrentOutput.writeInt(r5);     // Catch: IOException -> L7
        this.mCurrentOutput.write(r3, r4, r5);     // Catch: IOException -> L7
    L10:
        return;
    L4:
        this.mCurrentOutput.writeInt(-1);     // Catch: IOException -> L7
    L7:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected void writeCharSequence(CharSequence r3) {
        if (this.mIgnoreParcelables == false) goto L6;
        return;
    L6:
        throw new RuntimeException("CharSequence cannot be written to an OutputStream");
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeDouble(double r3) {
        this.mCurrentOutput.writeDouble(r3);     // Catch: IOException -> L4
        return;
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeFloat(float r3) {
        this.mCurrentOutput.writeFloat(r3);     // Catch: IOException -> L4
        return;
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeInt(int r3) {
        this.mCurrentOutput.writeInt(r3);     // Catch: IOException -> L4
        return;
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeLong(long r3) {
        this.mCurrentOutput.writeLong(r3);     // Catch: IOException -> L4
        return;
    L4:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeParcelable(Parcelable r3) {
        if (this.mIgnoreParcelables == false) goto L6;
        return;
    L6:
        throw new RuntimeException("Parcelables cannot be written to an OutputStream");
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeString(String r4) {
        if (r4 == null) goto L4;
        byte[] r0 = r4.getBytes(UTF_16);     // Catch: IOException -> L7
        this.mCurrentOutput.writeInt(r0.length);     // Catch: IOException -> L7
        this.mCurrentOutput.write(r0);     // Catch: IOException -> L7
    L10:
        return;
    L4:
        this.mCurrentOutput.writeInt(-1);     // Catch: IOException -> L7
    L7:
        e = move-exception;
        throw new VersionedParcel.ParcelException(e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeStrongBinder(IBinder r3) {
        if (this.mIgnoreParcelables == false) goto L6;
        return;
    L6:
        throw new RuntimeException("Binders cannot be written to an OutputStream");
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void writeStrongInterface(IInterface r3) {
        if (this.mIgnoreParcelables == false) goto L6;
        return;
    L6:
        throw new RuntimeException("Binders cannot be written to an OutputStream");
    }
}
