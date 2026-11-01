package com.google.common.hash;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class LittleEndianByteArray {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final LittleEndianBytes byteArray = null;

    /* JADX INFO: renamed from: com.google.common.hash.LittleEndianByteArray$1 */
    static /* synthetic */ class C08291 {
    }

    private static abstract enum JavaLittleEndianBytes extends Enum<JavaLittleEndianBytes> implements LittleEndianBytes {
        private static final /* synthetic */ JavaLittleEndianBytes[] $VALUES = null;
        public static final JavaLittleEndianBytes INSTANCE = null;

        private static /* synthetic */ JavaLittleEndianBytes[] $values() {
            return new JavaLittleEndianBytes[]{INSTANCE};
        }

        static {
            final String r1 = "INSTANCE";
            final int r2 = 0;
            INSTANCE = new C08301(r1, r2);
            $VALUES = $values();
        }

        private JavaLittleEndianBytes(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ JavaLittleEndianBytes(String r1, int r2, C08291 r3) {
            this(r1, r2);
        }

        public static JavaLittleEndianBytes valueOf(String r1) {
            return (JavaLittleEndianBytes) Enum.valueOf(JavaLittleEndianBytes.class, r1);
        }

        public static JavaLittleEndianBytes[] values() {
            return (JavaLittleEndianBytes[]) $VALUES.clone();
        }
    }

    private interface LittleEndianBytes {
        long getLongLittleEndian(byte[] r1, int r2);

        void putLongLittleEndian(byte[] r1, int r2, long r3);
    }

    private static abstract enum UnsafeByteArray extends Enum<UnsafeByteArray> implements LittleEndianBytes {
        private static final /* synthetic */ UnsafeByteArray[] $VALUES = null;
        private static final int BYTE_ARRAY_BASE_OFFSET = 0;
        public static final UnsafeByteArray UNSAFE_BIG_ENDIAN = null;
        public static final UnsafeByteArray UNSAFE_LITTLE_ENDIAN = null;
        private static final Unsafe theUnsafe = null;

        private static /* synthetic */ UnsafeByteArray[] $values() {
            return new UnsafeByteArray[]{UNSAFE_LITTLE_ENDIAN, UNSAFE_BIG_ENDIAN};
        }

        static {
            final String r1 = "UNSAFE_LITTLE_ENDIAN";
            final int r2 = 0;
            UNSAFE_LITTLE_ENDIAN = new C08311(r1, r2);
            final String r3 = "UNSAFE_BIG_ENDIAN";
            final int r4 = 1;
            UNSAFE_BIG_ENDIAN = new C08322(r3, r4);
            $VALUES = $values();
            theUnsafe = getUnsafe();
            BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
            if (theUnsafe.arrayIndexScale(byte[].class) != 1) goto L6;
            return;
        L6:
            throw new AssertionError();
        }

        private UnsafeByteArray(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ UnsafeByteArray(String r1, int r2, C08291 r3) {
            this(r1, r2);
        }

        static /* synthetic */ int access$100() {
            return BYTE_ARRAY_BASE_OFFSET;
        }

        static /* synthetic */ Unsafe access$200() {
            return theUnsafe;
        }

        private static Unsafe getUnsafe() {
            return Unsafe.getUnsafe();
        L12:
            return (Unsafe) AccessController.doPrivileged(new LittleEndianByteArray$UnsafeByteArray$$ExternalSyntheticLambda0());
        L7:
            e = move-exception;
            throw new RuntimeException("Could not initialize intrinsics", e.getCause());
        }

        static /* synthetic */ Unsafe lambda$getUnsafe$0() throws Exception {
            Field[] r1 = Unsafe.class.getDeclaredFields();
            int r2 = r1.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L10;
            Field r4 = r1[r3];
            r4.setAccessible(true);
            Object r5 = r4.get(null);
            if (Unsafe.class.isInstance(r5) == true) goto L7;
            r3 = r3 + 1;
            goto L3
        L7:
            return (Unsafe) Unsafe.class.cast(r5);
        L10:
            throw new NoSuchFieldError("the Unsafe");
        }

        public static UnsafeByteArray valueOf(String r1) {
            return (UnsafeByteArray) Enum.valueOf(UnsafeByteArray.class, r1);
        }

        public static UnsafeByteArray[] values() {
            return (UnsafeByteArray[]) $VALUES.clone();
        }
    }

    static {
        LittleEndianBytes r0 = JavaLittleEndianBytes.INSTANCE;
        if ("amd64".equals(System.getProperty("os.arch")) == false) goto L12;
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) == false) goto L8;
        UnsafeByteArray r2 = UnsafeByteArray.UNSAFE_LITTLE_ENDIAN;     // Catch: Throwable -> L11
    L9:
        r0 = r2;
        goto L12
    L8:
        r2 = UnsafeByteArray.UNSAFE_BIG_ENDIAN;     // Catch: Throwable -> L11
    L12:
        byteArray = r0;
    }

    private LittleEndianByteArray() {
    }

    static int load32(byte[] r2, int r3) {
        return (((r2[r3] & 255) | ((r2[r3 + 1] & 255) << 8)) | ((r2[r3 + 2] & 255) << 16)) | ((r2[r3 + 3] & 255) << 24);
    }

    static long load64(byte[] r2, int r3) {
        if (r2.length < (r3 + 8)) goto L7;
        return byteArray.getLongLittleEndian(r2, r3);
    L7:
        throw new AssertionError();
    }

    static long load64Safely(byte[] r8, int r9, int r10) {
        long r0 = 0;
        int r2 = Math.min(r10, 8);
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L5;
        r0 = r0 | ((((long) r8[r9 + r3]) & 255) << (r3 * 8));
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }

    static void store64(byte[] r2, int r3, long r4) {
        if (r3 < 0) goto L8;
        if ((r3 + 8) > r2.length) goto L8;
        byteArray.putLongLittleEndian(r2, r3, r4);
        return;
    L8:
        throw new AssertionError();
    }

    static boolean usingUnsafe() {
        return byteArray instanceof UnsafeByteArray;
    }
}
