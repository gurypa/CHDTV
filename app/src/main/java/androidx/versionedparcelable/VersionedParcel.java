package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class VersionedParcel {
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_FLOAT = 8;
    private static final int TYPE_INTEGER = 7;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;
    protected final ArrayMap<String, Class> mParcelizerCache;
    protected final ArrayMap<String, Method> mReadCache;
    protected final ArrayMap<String, Method> mWriteCache;

    public static class ParcelException extends RuntimeException {
        public ParcelException(Throwable r1) {
            super(r1);
        }
    }

    public VersionedParcel(ArrayMap<String, Method> r1, ArrayMap<String, Method> r2, ArrayMap<String, Class> r3) {
        this.mReadCache = r1;
        this.mWriteCache = r2;
        this.mParcelizerCache = r3;
    }

    private Exception createException(int r4, String r5) {
        switch(r4) {
            case -9: goto L20;
            case -8: goto L4;
            case -7: goto L18;
            case -6: goto L16;
            case -5: goto L14;
            case -4: goto L12;
            case -3: goto L10;
            case -2: goto L8;
            case -1: goto L6;
            default: goto L4;
        };
    L4:
        return new RuntimeException("Unknown exception code: " + r4 + " msg " + r5);
    L6:
        return new SecurityException(r5);
    L8:
        return new BadParcelableException(r5);
    L10:
        return new IllegalArgumentException(r5);
    L12:
        return new NullPointerException(r5);
    L14:
        return new IllegalStateException(r5);
    L16:
        return new NetworkOnMainThreadException();
    L18:
        return new UnsupportedOperationException(r5);
    L20:
        return (Exception) readParcelable();
    }

    private Class findParcelClass(Class<? extends VersionedParcelable> r7) throws ClassNotFoundException {
        Class r0 = this.mParcelizerCache.get(r7.getName());
        if (r0 != null) goto L6;
        Class<?> r1 = Class.forName(String.format("%s.%sParcelizer", new Object[]{r7.getPackage().getName(), r7.getSimpleName()}), false, r7.getClassLoader());
        this.mParcelizerCache.put(r7.getName(), r1);
        return r1;
    L6:
        return r0;
    }

    private Method getReadMethod(String r8) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method r0 = this.mReadCache.get(r8);
        if (r0 != null) goto L6;
        System.currentTimeMillis();
        Method r1 = Class.forName(r8, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", new Class[]{VersionedParcel.class});
        this.mReadCache.put(r8, r1);
        return r1;
    L6:
        return r0;
    }

    protected static Throwable getRootCause(Throwable r1) {
    L3:
        if (r1.getCause() == null) goto L5;
        r1 = r1.getCause();
        goto L3
    L5:
        return r1;
    }

    private <T> int getType(T r4) {
        if ((r4 instanceof String) == false) goto L7;
        return 4;
    L7:
        if ((r4 instanceof Parcelable) == false) goto L11;
        return 2;
    L11:
        if ((r4 instanceof VersionedParcelable) == false) goto L15;
        return 1;
    L15:
        if ((r4 instanceof Serializable) == false) goto L19;
        return 3;
    L19:
        if ((r4 instanceof IBinder) == false) goto L23;
        return 5;
    L23:
        if ((r4 instanceof Integer) == false) goto L27;
        return 7;
    L27:
        if ((r4 instanceof Float) == false) goto L31;
        return 8;
    L31:
        throw new IllegalArgumentException(r4.getClass().getName() + " cannot be VersionedParcelled");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Method getWriteMethod(Class r8) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method r0 = this.mWriteCache.get(r8.getName());
        if (r0 != null) goto L6;
        Class r1 = findParcelClass(r8);
        System.currentTimeMillis();
        Method r2 = r1.getDeclaredMethod("write", new Class[]{r8, VersionedParcel.class});
        this.mWriteCache.put(r8.getName(), r2);
        return r2;
    L6:
        return r0;
    }

    private <T, S extends Collection<T>> S readCollection(S r4) {
        int r0 = readInt();
        if (r0 >= 0) goto L5;
        return null;
    L5:
        if (r0 == 0) goto L21;
        int r2 = readInt();
        if (r0 >= 0) goto L9;
        return null;
    L9:
        switch(r2) {
            case 1: goto L19;
            case 2: goto L17;
            case 3: goto L15;
            case 4: goto L13;
            case 5: goto L11;
            default: goto L21;
        };
    L11:
        if (r0 <= 0) goto L21;
        r4.add(readStrongBinder());
        r0 = r0 - 1;
    L13:
        if (r0 <= 0) goto L21;
        r4.add(readString());
        r0 = r0 - 1;
    L15:
        if (r0 <= 0) goto L21;
        r4.add(readSerializable());
        r0 = r0 - 1;
    L17:
        if (r0 <= 0) goto L21;
        r4.add(readParcelable());
        r0 = r0 - 1;
    L19:
        if (r0 <= 0) goto L21;
        r4.add(readVersionedParcelable());
        r0 = r0 - 1;
    L21:
        return r4;
    }

    private Exception readException(int r2, String r3) {
        return createException(r2, r3);
    }

    private int readExceptionCode() {
        return readInt();
    }

    private <T> void writeCollection(Collection<T> r6) {
        if (r6 != null) goto L5;
        writeInt(-1);
        return;
    L5:
        int r0 = r6.size();
        writeInt(r0);
        if (r0 <= 0) goto L44;
        int r1 = getType(r6.iterator().next());
        writeInt(r1);
        switch(r1) {
            case 1: goto L39;
            case 2: goto L34;
            case 3: goto L29;
            case 4: goto L24;
            case 5: goto L19;
            case 6: goto L52;
            case 7: goto L14;
            case 8: goto L10;
            default: goto L52;
        };
    L10:
        Iterator<T> r2 = r6.iterator();
    L12:
        if (r2.hasNext() == false) goto L53;
        writeFloat(((Float) r2.next()).floatValue());
        goto L12
    L53:
        return;
    L14:
        Iterator<T> r3 = r6.iterator();
    L16:
        if (r3.hasNext() == false) goto L54;
        writeInt(((Integer) r3.next()).intValue());
        goto L16
    L54:
        return;
    L19:
        Iterator<T> r4 = r6.iterator();
    L21:
        if (r4.hasNext() == false) goto L55;
        writeStrongBinder((IBinder) r4.next());
        goto L21
    L55:
        return;
    L24:
        Iterator<T> r5 = r6.iterator();
    L26:
        if (r5.hasNext() == false) goto L56;
        writeString((String) r5.next());
        goto L26
    L56:
        return;
    L29:
        Iterator<T> r7 = r6.iterator();
    L31:
        if (r7.hasNext() == false) goto L57;
        writeSerializable((Serializable) r7.next());
        goto L31
    L57:
        return;
    L34:
        Iterator<T> r8 = r6.iterator();
    L36:
        if (r8.hasNext() == false) goto L58;
        writeParcelable((Parcelable) r8.next());
        goto L36
    L58:
        return;
    L39:
        Iterator<T> r9 = r6.iterator();
    L41:
        if (r9.hasNext() == false) goto L59;
        writeVersionedParcelable((VersionedParcelable) r9.next());
        goto L41
    L59:
        return;
    L52:
        return;
    }

    private <T> void writeCollection(Collection<T> r1, int r2) {
        setOutputField(r2);
        writeCollection(r1);
    }

    private void writeSerializable(Serializable r7) {
        if (r7 != null) goto L5;
        writeString(null);
        return;
    L5:
        String r0 = r7.getClass().getName();
        writeString(r0);
        ByteArrayOutputStream r1 = new ByteArrayOutputStream();
        ObjectOutputStream r2 = new ObjectOutputStream(r1);     // Catch: IOException -> L8
        r2.writeObject(r7);     // Catch: IOException -> L8
        r2.close();     // Catch: IOException -> L8
        writeByteArray(r1.toByteArray());     // Catch: IOException -> L8
        return;
    L8:
        e = move-exception;
        throw new RuntimeException("VersionedParcelable encountered IOException writing serializable object (name = " + r0 + ")", e);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeVersionedParcelableCreator(VersionedParcelable r6) {
        writeString(findParcelClass(r6.getClass()).getName());
        return;
    L6:
        e = move-exception;
        throw new RuntimeException(r6.getClass().getSimpleName() + " does not have a Parcelizer", e);
    }

    protected abstract void closeField();

    protected abstract VersionedParcel createSubParcel();

    public boolean isStream() {
        return false;
    }

    protected <T> T[] readArray(T[] r5) {
        int r0 = readInt();
        if (r0 >= 0) goto L5;
        return null;
    L5:
        ArrayList r2 = new ArrayList(r0);
        if (r0 == 0) goto L23;
        int r3 = readInt();
        if (r0 >= 0) goto L10;
        return null;
    L10:
        switch(r3) {
            case 1: goto L20;
            case 2: goto L18;
            case 3: goto L16;
            case 4: goto L14;
            case 5: goto L12;
            default: goto L23;
        };
    L12:
        if (r0 <= 0) goto L23;
        r2.add(readStrongBinder());
        r0 = r0 - 1;
    L14:
        if (r0 <= 0) goto L23;
        r2.add(readString());
        r0 = r0 - 1;
    L16:
        if (r0 <= 0) goto L23;
        r2.add(readSerializable());
        r0 = r0 - 1;
    L18:
        if (r0 <= 0) goto L23;
        r2.add(readParcelable());
        r0 = r0 - 1;
    L20:
        if (r0 <= 0) goto L23;
        r2.add(readVersionedParcelable());
        r0 = r0 - 1;
    L23:
        return (T[]) r2.toArray(r5);
    }

    public <T> T[] readArray(T[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return (T[]) readArray(r2);
    }

    protected abstract boolean readBoolean();

    public boolean readBoolean(boolean r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readBoolean();
    }

    protected boolean[] readBooleanArray() {
        int r0 = readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        boolean[] r1 = new boolean[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L13;
        if (readInt() == 0) goto L11;
        boolean r3 = true;
    L12:
        r1[r2] = r3;
        r2 = r2 + 1;
        goto L7
    L11:
        r3 = false;
        goto L12
    L13:
        return r1;
    }

    public boolean[] readBooleanArray(boolean[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readBooleanArray();
    }

    protected abstract Bundle readBundle();

    public Bundle readBundle(Bundle r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readBundle();
    }

    public byte readByte(byte r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return (byte) (readInt() & 255);
    }

    protected abstract byte[] readByteArray();

    public byte[] readByteArray(byte[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readByteArray();
    }

    public char[] readCharArray(char[] r5, int r6) {
        if (readField(r6) == true) goto L5;
        return r5;
    L5:
        int r0 = readInt();
        if (r0 >= 0) goto L9;
        return null;
    L9:
        char[] r1 = new char[r0];
        int r2 = 0;
    L10:
        if (r2 >= r0) goto L12;
        r1[r2] = (char) readInt();
        r2 = r2 + 1;
        goto L10
    L12:
        return r1;
    }

    protected abstract CharSequence readCharSequence();

    public CharSequence readCharSequence(CharSequence r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readCharSequence();
    }

    protected abstract double readDouble();

    public double readDouble(double r3, int r5) {
        if (readField(r5) == true) goto L6;
        return r3;
    L6:
        return readDouble();
    }

    protected double[] readDoubleArray() {
        int r0 = readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        double[] r1 = new double[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        r1[r2] = readDouble();
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    }

    public double[] readDoubleArray(double[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readDoubleArray();
    }

    public Exception readException(Exception r4, int r5) {
        if (readField(r5) == true) goto L5;
        return r4;
    L5:
        int r0 = readExceptionCode();
        if (r0 != 0) goto L8;
        return r4;
    L8:
        return readException(r0, readString());
    }

    protected abstract boolean readField(int r1);

    protected abstract float readFloat();

    public float readFloat(float r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readFloat();
    }

    protected float[] readFloatArray() {
        int r0 = readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        float[] r1 = new float[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        r1[r2] = readFloat();
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    }

    public float[] readFloatArray(float[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readFloatArray();
    }

    protected <T extends VersionedParcelable> T readFromParcel(String r4, VersionedParcel r5) {
        return (T) getReadMethod(r4).invoke(null, new Object[]{r5});
    L4:
        e = move-exception;
        throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e);
    L17:
        e = move-exception;
        throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e);
    L7:
        e = move-exception;
        throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e);
    L10:
        e = move-exception;
        if ((e.getCause() instanceof RuntimeException) == false) goto L16;
        throw ((RuntimeException) e.getCause());
    L16:
        throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e);
    }

    protected abstract int readInt();

    public int readInt(int r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readInt();
    }

    protected int[] readIntArray() {
        int r0 = readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        int[] r1 = new int[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        r1[r2] = readInt();
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    }

    public int[] readIntArray(int[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readIntArray();
    }

    public <T> List<T> readList(List<T> r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return (List) readCollection(new ArrayList());
    }

    protected abstract long readLong();

    public long readLong(long r3, int r5) {
        if (readField(r5) == true) goto L6;
        return r3;
    L6:
        return readLong();
    }

    protected long[] readLongArray() {
        int r0 = readInt();
        if (r0 >= 0) goto L6;
        return null;
    L6:
        long[] r1 = new long[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        r1[r2] = readLong();
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    }

    public long[] readLongArray(long[] r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readLongArray();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <K, V> Map<K, V> readMap(Map<K, V> r8, int r9) {
        if (readField(r9) == true) goto L5;
        return r8;
    L5:
        int r0 = readInt();
        if (r0 >= 0) goto L9;
        return null;
    L9:
        ArrayMap r1 = new ArrayMap();
        if (r0 != 0) goto L12;
        return r1;
    L12:
        ArrayList r2 = new ArrayList();
        ArrayList r3 = new ArrayList();
        readCollection(r2);
        readCollection(r3);
        int r4 = 0;
    L13:
        if (r4 >= r0) goto L15;
        r1.put(r2.get(r4), r3.get(r4));
        r4 = r4 + 1;
        goto L13
    L15:
        return r1;
    }

    protected abstract <T extends Parcelable> T readParcelable();

    public <T extends Parcelable> T readParcelable(T r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return (T) readParcelable();
    }

    protected Serializable readSerializable() {
        String r1 = readString();
        if (r1 != null) goto L6;
        return null;
    L6:
        final ByteArrayInputStream r3 = new ByteArrayInputStream(readByteArray());
        return (Serializable) new C05231(this, r3).readObject();
    L12:
        e = move-exception;
        throw new RuntimeException("VersionedParcelable encountered IOException reading a Serializable object (name = " + r1 + ")", e);
    L9:
        e = move-exception;
        throw new RuntimeException("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = " + r1 + ")", e);
    }

    public <T> Set<T> readSet(Set<T> r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return (Set) readCollection(new ArraySet());
    }

    public Size readSize(Size r4, int r5) {
        if (readField(r5) == true) goto L6;
        return r4;
    L6:
        if (readBoolean() == true) goto L8;
        return null;
    L8:
        return new Size(readInt(), readInt());
    }

    public SizeF readSizeF(SizeF r4, int r5) {
        if (readField(r5) == true) goto L6;
        return r4;
    L6:
        if (readBoolean() == true) goto L8;
        return null;
    L8:
        return new SizeF(readFloat(), readFloat());
    }

    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray r6, int r7) {
        if (readField(r7) == true) goto L5;
        return r6;
    L5:
        int r0 = readInt();
        if (r0 >= 0) goto L9;
        return null;
    L9:
        SparseBooleanArray r1 = new SparseBooleanArray(r0);
        int r2 = 0;
    L10:
        if (r2 >= r0) goto L12;
        r1.put(readInt(), readBoolean());
        r2 = r2 + 1;
        goto L10
    L12:
        return r1;
    }

    protected abstract String readString();

    public String readString(String r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readString();
    }

    protected abstract IBinder readStrongBinder();

    public IBinder readStrongBinder(IBinder r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return readStrongBinder();
    }

    protected <T extends VersionedParcelable> T readVersionedParcelable() {
        String r0 = readString();
        if (r0 != null) goto L7;
        return null;
    L7:
        return (T) readFromParcel(r0, createSubParcel());
    }

    public <T extends VersionedParcelable> T readVersionedParcelable(T r2, int r3) {
        if (readField(r3) == true) goto L6;
        return r2;
    L6:
        return (T) readVersionedParcelable();
    }

    protected abstract void setOutputField(int r1);

    public void setSerializationFlags(boolean r1, boolean r2) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <T> void writeArray(T[] r5) {
        if (r5 != 0) goto L5;
        writeInt(-1);
        return;
    L5:
        int r0 = r5.length;
        int r1 = 0;
        writeInt(r0);
        if (r0 <= 0) goto L20;
        int r2 = getType(r5[0]);
        writeInt(r2);
        switch(r2) {
            case 1: goto L18;
            case 2: goto L16;
            case 3: goto L14;
            case 4: goto L12;
            case 5: goto L10;
            default: goto L26;
        };
    L10:
        if (r1 >= r0) goto L27;
        writeStrongBinder((IBinder) r5[r1]);
        r1 = r1 + 1;
        goto L10
    L27:
        return;
    L12:
        if (r1 >= r0) goto L28;
        writeString((String) r5[r1]);
        r1 = r1 + 1;
        goto L12
    L28:
        return;
    L14:
        if (r1 >= r0) goto L29;
        writeSerializable((Serializable) r5[r1]);
        r1 = r1 + 1;
        goto L14
    L29:
        return;
    L16:
        if (r1 >= r0) goto L30;
        writeParcelable((Parcelable) r5[r1]);
        r1 = r1 + 1;
        goto L16
    L30:
        return;
    L18:
        if (r1 >= r0) goto L31;
        writeVersionedParcelable((VersionedParcelable) r5[r1]);
        r1 = r1 + 1;
        goto L18
    L31:
        return;
    L26:
        return;
    }

    public <T> void writeArray(T[] r1, int r2) {
        setOutputField(r2);
        writeArray(r1);
    }

    protected abstract void writeBoolean(boolean r1);

    public void writeBoolean(boolean r1, int r2) {
        setOutputField(r2);
        writeBoolean(r1);
    }

    protected void writeBooleanArray(boolean[] r4) {
        if (r4 == null) goto L7;
        int r0 = r4.length;
        writeInt(r0);
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L10;
        writeInt(r4[r1] ? 1 : 0);
        r1 = r1 + 1;
        goto L4
    L10:
        return;
    L7:
        writeInt(-1);
    }

    public void writeBooleanArray(boolean[] r1, int r2) {
        setOutputField(r2);
        writeBooleanArray(r1);
    }

    protected abstract void writeBundle(Bundle r1);

    public void writeBundle(Bundle r1, int r2) {
        setOutputField(r2);
        writeBundle(r1);
    }

    public void writeByte(byte r1, int r2) {
        setOutputField(r2);
        writeInt(r1);
    }

    protected abstract void writeByteArray(byte[] r1);

    public void writeByteArray(byte[] r1, int r2) {
        setOutputField(r2);
        writeByteArray(r1);
    }

    protected abstract void writeByteArray(byte[] r1, int r2, int r3);

    public void writeByteArray(byte[] r1, int r2, int r3, int r4) {
        setOutputField(r4);
        writeByteArray(r1, r2, r3);
    }

    public void writeCharArray(char[] r4, int r5) {
        setOutputField(r5);
        if (r4 == null) goto L8;
        int r0 = r4.length;
        writeInt(r0);
        int r1 = 0;
    L5:
        if (r1 >= r0) goto L11;
        writeInt(r4[r1]);
        r1 = r1 + 1;
        goto L5
    L11:
        return;
    L8:
        writeInt(-1);
    }

    protected abstract void writeCharSequence(CharSequence r1);

    public void writeCharSequence(CharSequence r1, int r2) {
        setOutputField(r2);
        writeCharSequence(r1);
    }

    protected abstract void writeDouble(double r1);

    public void writeDouble(double r1, int r3) {
        setOutputField(r3);
        writeDouble(r1);
    }

    protected void writeDoubleArray(double[] r5) {
        if (r5 == null) goto L7;
        int r0 = r5.length;
        writeInt(r0);
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L10;
        writeDouble(r5[r1]);
        r1 = r1 + 1;
        goto L4
    L10:
        return;
    L7:
        writeInt(-1);
    }

    public void writeDoubleArray(double[] r1, int r2) {
        setOutputField(r2);
        writeDoubleArray(r1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeException(Exception r4, int r5) {
        setOutputField(r5);
        if (r4 != 0) goto L6;
        writeNoException();
        return;
    L6:
        int r0 = 0;
        if ((r4 instanceof Parcelable) == false) goto L12;
        if (r4.getClass().getClassLoader() != Parcelable.class.getClassLoader()) goto L12;
        r0 = EX_PARCELABLE;
    L32:
        writeInt(r0);
        if (r0 == 0) goto L35;
        writeString(r4.getMessage());
        switch(r0) {
            case -9: goto L43;
            default: goto L45;
        };
    L43:
        writeParcelable((Parcelable) r4);
        return;
    L45:
        return;
    L35:
        if ((r4 instanceof RuntimeException) == false) goto L39;
        throw ((RuntimeException) r4);
    L39:
        throw new RuntimeException(r4);
    L12:
        if ((r4 instanceof SecurityException) == false) goto L15;
        r0 = -1;
        goto L32
    L15:
        if ((r4 instanceof BadParcelableException) == false) goto L18;
        r0 = -2;
        goto L32
    L18:
        if ((r4 instanceof IllegalArgumentException) == false) goto L21;
        r0 = -3;
        goto L32
    L21:
        if ((r4 instanceof NullPointerException) == false) goto L24;
        r0 = -4;
        goto L32
    L24:
        if ((r4 instanceof IllegalStateException) == false) goto L27;
        r0 = -5;
        goto L32
    L27:
        if ((r4 instanceof NetworkOnMainThreadException) == false) goto L30;
        r0 = -6;
        goto L32
    L30:
        if ((r4 instanceof UnsupportedOperationException) == false) goto L32;
        r0 = EX_UNSUPPORTED_OPERATION;
        goto L32
    }

    protected abstract void writeFloat(float r1);

    public void writeFloat(float r1, int r2) {
        setOutputField(r2);
        writeFloat(r1);
    }

    protected void writeFloatArray(float[] r4) {
        if (r4 == null) goto L7;
        int r0 = r4.length;
        writeInt(r0);
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L10;
        writeFloat(r4[r1]);
        r1 = r1 + 1;
        goto L4
    L10:
        return;
    L7:
        writeInt(-1);
    }

    public void writeFloatArray(float[] r1, int r2) {
        setOutputField(r2);
        writeFloatArray(r1);
    }

    protected abstract void writeInt(int r1);

    public void writeInt(int r1, int r2) {
        setOutputField(r2);
        writeInt(r1);
    }

    protected void writeIntArray(int[] r4) {
        if (r4 == null) goto L7;
        int r0 = r4.length;
        writeInt(r0);
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L10;
        writeInt(r4[r1]);
        r1 = r1 + 1;
        goto L4
    L10:
        return;
    L7:
        writeInt(-1);
    }

    public void writeIntArray(int[] r1, int r2) {
        setOutputField(r2);
        writeIntArray(r1);
    }

    public <T> void writeList(List<T> r1, int r2) {
        writeCollection(r1, r2);
    }

    protected abstract void writeLong(long r1);

    public void writeLong(long r1, int r3) {
        setOutputField(r3);
        writeLong(r1);
    }

    protected void writeLongArray(long[] r5) {
        if (r5 == null) goto L7;
        int r0 = r5.length;
        writeInt(r0);
        int r1 = 0;
    L4:
        if (r1 >= r0) goto L10;
        writeLong(r5[r1]);
        r1 = r1 + 1;
        goto L4
    L10:
        return;
    L7:
        writeInt(-1);
    }

    public void writeLongArray(long[] r1, int r2) {
        setOutputField(r2);
        writeLongArray(r1);
    }

    public <K, V> void writeMap(Map<K, V> r7, int r8) {
        setOutputField(r8);
        if (r7 != null) goto L6;
        writeInt(-1);
        return;
    L6:
        int r0 = r7.size();
        writeInt(r0);
        if (r0 != 0) goto L9;
        return;
    L9:
        ArrayList r1 = new ArrayList();
        ArrayList r2 = new ArrayList();
        Iterator<Map.Entry<K, V>> r3 = r7.entrySet().iterator();
    L11:
        if (r3.hasNext() == false) goto L13;
        Map.Entry<K, V> r4 = r3.next();
        r1.add(r4.getKey());
        r2.add(r4.getValue());
        goto L11
    L13:
        writeCollection(r1);
        writeCollection(r2);
    }

    protected void writeNoException() {
        writeInt(0);
    }

    protected abstract void writeParcelable(Parcelable r1);

    public void writeParcelable(Parcelable r1, int r2) {
        setOutputField(r2);
        writeParcelable(r1);
    }

    public void writeSerializable(Serializable r1, int r2) {
        setOutputField(r2);
        writeSerializable(r1);
    }

    public <T> void writeSet(Set<T> r1, int r2) {
        writeCollection(r1, r2);
    }

    public void writeSize(Size r2, int r3) {
        setOutputField(r3);
        if (r2 == null) goto L5;
        boolean r0 = true;
    L6:
        writeBoolean(r0);
        if (r2 == null) goto L10;
        writeInt(r2.getWidth());
        writeInt(r2.getHeight());
        return;
    L10:
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void writeSizeF(SizeF r2, int r3) {
        setOutputField(r3);
        if (r2 == null) goto L5;
        boolean r0 = true;
    L6:
        writeBoolean(r0);
        if (r2 == null) goto L10;
        writeFloat(r2.getWidth());
        writeFloat(r2.getHeight());
        return;
    L10:
        return;
    L5:
        r0 = false;
        goto L6
    }

    public void writeSparseBooleanArray(SparseBooleanArray r4, int r5) {
        setOutputField(r5);
        if (r4 != null) goto L6;
        writeInt(-1);
        return;
    L6:
        int r0 = r4.size();
        writeInt(r0);
        int r1 = 0;
    L7:
        if (r1 >= r0) goto L9;
        writeInt(r4.keyAt(r1));
        writeBoolean(r4.valueAt(r1));
        r1 = r1 + 1;
        goto L7
    }

    protected abstract void writeString(String r1);

    public void writeString(String r1, int r2) {
        setOutputField(r2);
        writeString(r1);
    }

    protected abstract void writeStrongBinder(IBinder r1);

    public void writeStrongBinder(IBinder r1, int r2) {
        setOutputField(r2);
        writeStrongBinder(r1);
    }

    protected abstract void writeStrongInterface(IInterface r1);

    public void writeStrongInterface(IInterface r1, int r2) {
        setOutputField(r2);
        writeStrongInterface(r1);
    }

    protected <T extends VersionedParcelable> void writeToParcel(T r4, VersionedParcel r5) {
        getWriteMethod(r4.getClass()).invoke(null, new Object[]{r4, r5});     // Catch: ClassNotFoundException -> L4 NoSuchMethodException -> L7 InvocationTargetException -> L10 IllegalAccessException -> L17
        return;
    L4:
        e = move-exception;
        throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e);
    L17:
        e = move-exception;
        throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e);
    L7:
        e = move-exception;
        throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e);
    L10:
        e = move-exception;
        if ((e.getCause() instanceof RuntimeException) == false) goto L16;
        throw ((RuntimeException) e.getCause());
    L16:
        throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e);
    }

    protected void writeVersionedParcelable(VersionedParcelable r2) {
        if (r2 != null) goto L5;
        writeString(null);
        return;
    L5:
        writeVersionedParcelableCreator(r2);
        VersionedParcel r0 = createSubParcel();
        writeToParcel(r2, r0);
        r0.closeField();
    }

    public void writeVersionedParcelable(VersionedParcelable r1, int r2) {
        setOutputField(r2);
        writeVersionedParcelable(r1);
    }
}
