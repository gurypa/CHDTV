package com.google.common.collect;

import com.google.common.base.Preconditions;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class CollectPreconditions {
    CollectPreconditions() {
    }

    static void checkEntryNotNull(Object r3, Object r4) {
        if (r3 == null) goto L8;
        if (r4 == null) goto L6;
        return;
    L6:
        throw new NullPointerException("null value in entry: " + r3 + "=null");
    L8:
        throw new NullPointerException("null key in entry: null=" + r4);
    }

    static int checkNonnegative(int r3, String r4) {
        if (r3 < 0) goto L5;
        return r3;
    L5:
        throw new IllegalArgumentException(r4 + " cannot be negative but was: " + r3);
    }

    static long checkNonnegative(long r3, String r5) {
        if (r3 < 0) goto L6;
        return r3;
    L6:
        throw new IllegalArgumentException(r5 + " cannot be negative but was: " + r3);
    }

    static void checkPositive(int r3, String r4) {
        if (r3 <= 0) goto L5;
        return;
    L5:
        throw new IllegalArgumentException(r4 + " must be positive but was: " + r3);
    }

    static void checkRemove(boolean r1) {
        Preconditions.checkState(r1, "no calls to next() since the last call to remove()");
    }
}
