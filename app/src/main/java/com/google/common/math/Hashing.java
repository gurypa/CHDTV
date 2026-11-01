package com.google.common.collect;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Hashing {

    /* JADX INFO: renamed from: C1 */
    private static final long f116C1 = -862048943;

    /* JADX INFO: renamed from: C2 */
    private static final long f117C2 = 461845907;
    private static final int MAX_TABLE_SIZE = 1073741824;

    private Hashing() {
    }

    static int closedTableSize(int r3, double r4) {
        int r5 = Math.max(r3, 2);
        int r0 = Integer.highestOneBit(r5);
        if (r5 <= ((int) (((double) r0) * r4))) goto L9;
        int r1 = r0 << 1;
        if (r1 > 0) goto L10;
        return 1073741824;
    L10:
        return r1;
    L9:
        return r0;
    }

    static boolean needsResizing(int r4, int r5, double r6) {
        if (r4 > (((double) r5) * r6)) goto L5;
    L7:
        return false;
    L5:
        if (r5 >= 1073741824) goto L7;
        return true;
    }

    static int smear(int r4) {
        return (int) (((long) Integer.rotateLeft((int) (((long) r4) * f116C1), 15)) * f117C2);
    }

    static int smearedHash(@CheckForNull Object r1) {
        if (r1 != null) goto L4;
        int r0 = 0;
    L6:
        return smear(r0);
    L4:
        r0 = r1.hashCode();
        goto L6
    }
}
