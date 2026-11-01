package com.google.common.hash;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.security.AccessController;
import java.util.Random;
import javax.annotation.CheckForNull;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class Striped64 extends Number {
    static final int NCPU = 0;
    private static final Unsafe UNSAFE = null;
    private static final long baseOffset = 0;
    private static final long busyOffset = 0;
    static final Random rng = null;
    static final ThreadLocal<int[]> threadHashCode = null;
    volatile transient long base;
    volatile transient int busy;

    @CheckForNull
    volatile transient Cell[] cells;

    static final class Cell {
        private static final Unsafe UNSAFE = null;
        private static final long valueOffset = 0;

        /* JADX INFO: renamed from: p0 */
        volatile long f146p0;

        /* JADX INFO: renamed from: p1 */
        volatile long f147p1;

        /* JADX INFO: renamed from: p2 */
        volatile long f148p2;

        /* JADX INFO: renamed from: p3 */
        volatile long f149p3;

        /* JADX INFO: renamed from: p4 */
        volatile long f150p4;

        /* JADX INFO: renamed from: p5 */
        volatile long f151p5;

        /* JADX INFO: renamed from: p6 */
        volatile long f152p6;

        /* JADX INFO: renamed from: q0 */
        volatile long f153q0;

        /* JADX INFO: renamed from: q1 */
        volatile long f154q1;

        /* JADX INFO: renamed from: q2 */
        volatile long f155q2;

        /* JADX INFO: renamed from: q3 */
        volatile long f156q3;

        /* JADX INFO: renamed from: q4 */
        volatile long f157q4;

        /* JADX INFO: renamed from: q5 */
        volatile long f158q5;

        /* JADX INFO: renamed from: q6 */
        volatile long f159q6;
        volatile long value;

        static {
            UNSAFE = Striped64.access$000();     // Catch: Exception -> L4
            valueOffset = UNSAFE.objectFieldOffset(Cell.class.getDeclaredField("value"));     // Catch: Exception -> L4
            return;
        L4:
            e = move-exception;
            throw new Error(e);
        }

        Cell(long r1) {
            this.value = r1;
        }

        final boolean cas(long r9, long r11) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, r9, r11);
        }
    }

    static {
        threadHashCode = new ThreadLocal();
        rng = new Random();
        NCPU = Runtime.getRuntime().availableProcessors();
        UNSAFE = getUnsafe();     // Catch: Exception -> L5
        baseOffset = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField(TtmlNode.RUBY_BASE));     // Catch: Exception -> L5
        busyOffset = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("busy"));     // Catch: Exception -> L5
        return;
    L5:
        e = move-exception;
        throw new Error(e);
    }

    Striped64() {
    }

    static /* synthetic */ Unsafe access$000() {
        return getUnsafe();
    }

    private static Unsafe getUnsafe() {
        return Unsafe.getUnsafe();
    L12:
        return (Unsafe) AccessController.doPrivileged(new C08371());
    L7:
        e = move-exception;
        throw new RuntimeException("Could not initialize intrinsics", e.getCause());
    }

    final boolean casBase(long r9, long r11) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, r9, r11);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    /* JADX INFO: renamed from: fn */
    abstract long mo340fn(long r1, long r3);

    final void internalReset(long r5) {
        Cell[] r0 = this.cells;
        this.base = r5;
        if (r0 == null) goto L10;
        int r1 = r0.length;
        int r2 = 0;
    L5:
        if (r2 >= r1) goto L14;
        Cell r3 = r0[r2];
        if (r3 == null) goto L9;
        r3.value = r5;
    L9:
        r2 = r2 + 1;
        goto L5
    L14:
        return;
    }

    final void retryUpdate(long r18, @CheckForNull int[] r20, boolean r21) {
        int r4 = 0;
        if (r20 != null) goto L9;
        int r5 = 1;
        int[] r6 = new int[1];
        int[] r7 = r6;
        threadHashCode.set(r6);
        int r0 = rng.nextInt();
        if (r0 == 0) goto L8;
        r5 = r0;
    L8:
        r7[0] = r5;
    L10:
        boolean r8 = false;
        int r9 = r5;
        boolean r10 = r21;
    L11:
        Cell[] r1 = this.cells;
        if (r1 == null) goto L71;
        int r2 = r1.length;
        if (r2 <= 0) goto L71;
        Cell r3 = r1[(r2 - 1) & r9];
        if (r3 == null) goto L18;
        if (r10 == false) goto L40;
        long r12 = r3.value;
        boolean r22 = r10;
        if (r3.cas(r12, mo340fn(r12, r18)) == true) goto L122;
        if (r2 >= NCPU) goto L66;
        if (this.cells != r1) goto L66;
        if (r8 == false) goto L50;
        if (this.busy != 0) goto L65;
        if (casBusy() == false) goto L65;
        if (this.cells != r1) goto L61;
        Cell[] r11 = new Cell[r2 << 1];     // Catch: Throwable -> L62
        int r13 = 0;
    L58:
        if (r13 >= r2) goto L60;
        r11[r13] = r1[r13];     // Catch: Throwable -> L62
        r13 = r13 + 1;     // Catch: Throwable -> L62
        goto L58
    L60:
        this.cells = r11;     // Catch: Throwable -> L62
    L61:
        r4 = 0;
        this.busy = 0;
        r8 = false;
        r10 = r22;
    L62:
        th = move-exception;
        this.busy = 0;
        throw th;
    L65:
        r10 = r22;
    L67:
        int r14 = (r9 << 13) ^ r9;
        int r15 = r14 ^ (r14 >>> 17);
        int r16 = r15 ^ (r15 << 5);
        r7[0] = r16;
        r9 = r16;
        boolean r17 = r10;
        int r19 = 0;
    L92:
        r4 = r19;
        r10 = r17;
        goto L11
    L50:
        r8 = true;
        r10 = r22;
    L66:
        r8 = false;
        r10 = r22;
        goto L67
    L122:
        return;
    L40:
        r10 = true;
        goto L67
    L18:
        if (this.busy != 0) goto L38;
        Cell r23 = new Cell(r18);
        if (this.busy != 0) goto L38;
        if (casBusy() == false) goto L38;
        boolean r110 = false;
        Cell[] r24 = this.cells;     // Catch: Throwable -> L35
        if (r24 == null) goto L32;
        int r25 = r24.length;     // Catch: Throwable -> L35
        if (r25 <= 0) goto L32;
        int r26 = (r25 - 1) & r9;     // Catch: Throwable -> L35
        if (r24[r26] != null) goto L32;
        r24[r26] = r23;     // Catch: Throwable -> L35
        r110 = true;
    L32:
        this.busy = r4;
        if (r110 == false) goto L11;
        return;
    L35:
        th = move-exception;
        this.busy = r4;
        throw th;
    L38:
        r8 = false;
    L71:
        if (this.busy == 0) goto L73;
    L88:
        r19 = 0;
        long r111 = this.base;
        if (casBase(r111, mo340fn(r111, r18)) == true) goto L124;
    L91:
        r17 = r10;
        goto L92
    L124:
        return;
    L73:
        if (this.cells != r1) goto L88;
        if (casBusy() == false) goto L88;
        boolean r27 = false;
    L85:
        th = move-exception;
        this.busy = 0;
        throw th;
    L78:
        if (this.cells != r1) goto L81;
        Cell[] r28 = new Cell[2];     // Catch: Throwable -> L85
        r28[r9 & 1] = new Cell(r18);     // Catch: Throwable -> L85
        this.cells = r28;     // Catch: Throwable -> L85
        r27 = true;
    L81:
        this.busy = 0;
        if (r27 == true) goto L123;
        r19 = 0;
        goto L91
    L123:
        return;
    L9:
        r5 = r20[0];
        r7 = r20;
        goto L10
    }
}
