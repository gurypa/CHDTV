package androidx.media3.extractor.mp4;

import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
final class FixedSampleSizeRechunker {
    private static final int MAX_SAMPLE_SIZE = 8192;

    /* JADX INFO: renamed from: androidx.media3.extractor.mp4.FixedSampleSizeRechunker$1 */
    static /* synthetic */ class C04391 {
    }

    public static final class Results {
        public final long duration;
        public final int[] flags;
        public final int maximumSize;
        public final long[] offsets;
        public final int[] sizes;
        public final long[] timestamps;

        private Results(long[] r1, int[] r2, int r3, long[] r4, int[] r5, long r6) {
            this.offsets = r1;
            this.sizes = r2;
            this.maximumSize = r3;
            this.timestamps = r4;
            this.flags = r5;
            this.duration = r6;
        }

        /* synthetic */ Results(long[] r1, int[] r2, int r3, long[] r4, int[] r5, long r6, C04391 r8) {
            this(r1, r2, r3, r4, r5, r6);
        }
    }

    private FixedSampleSizeRechunker() {
    }

    public static Results rechunk(int r17, long[] r18, int[] r19, long r20) {
        int[] r0 = r19;
        int r1 = 8192 / r17;
        int r2 = 0;
        int r3 = r0.length;
        int r4 = 0;
    L3:
        if (r4 >= r3) goto L5;
        r2 = r2 + Util.ceilDivide(r0[r4], r1);
        r4 = r4 + 1;
        goto L3
    L5:
        long[] r6 = new long[r2];
        int[] r7 = new int[r2];
        long[] r9 = new long[r2];
        int[] r10 = new int[r2];
        int r5 = 0;
        int r8 = 0;
        int r11 = 0;
        int r12 = 0;
    L7:
        if (r12 >= r0.length) goto L13;
        int r13 = r0[r12];
        long r14 = r18[r12];
    L9:
        if (r13 <= 0) goto L11;
        int r15 = Math.min(r1, r13);
        r6[r11] = r14;
        r7[r11] = r17 * r15;
        r8 = Math.max(r8, r7[r11]);
        r9[r11] = ((long) r5) * r20;
        r10[r11] = 1;
        r14 = r14 + ((long) r7[r11]);
        r5 = r5 + r15;
        r13 = r13 - r15;
        r11 = r11 + 1;
        r1 = r1;
        goto L9
    L11:
        r12 = r12 + 1;
        r0 = r19;
        goto L7
    L13:
        return new Results(r6, r7, r8, r9, r10, r20 * ((long) r5), null);
    }
}
