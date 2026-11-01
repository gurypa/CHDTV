package com.google.common.base;

import java.util.BitSet;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class SmallCharMatcher extends CharMatcher.NamedFastMatcher {

    /* JADX INFO: renamed from: C1 */
    private static final int f97C1 = -862048943;

    /* JADX INFO: renamed from: C2 */
    private static final int f98C2 = 461845907;
    private static final double DESIRED_LOAD_FACTOR = 0.5d;
    static final int MAX_SIZE = 1023;
    private final boolean containsZero;
    private final long filter;
    private final char[] table;

    private SmallCharMatcher(char[] r1, long r2, boolean r4, String r5) {
        super(r5);
        this.table = r1;
        this.filter = r2;
        this.containsZero = r4;
    }

    private boolean checkFilter(int r5) {
        if (1 != ((this.filter >> r5) & 1)) goto L5;
        return true;
    L5:
        return false;
    }

    static int chooseTableSize(int r5) {
        if (r5 != 1) goto L6;
        return 2;
    L6:
        int r0 = Integer.highestOneBit(r5 - 1) << 1;
    L8:
        if ((((double) r0) * 0.5d) >= r5) goto L10;
        r0 = r0 << 1;
        goto L8
    L10:
        return r0;
    }

    static CharMatcher from(BitSet r11, String r12) {
        long r0 = 0;
        int r2 = r11.cardinality();
        boolean r8 = r11.get(0);
        char[] r5 = new char[chooseTableSize(r2)];
        int r10 = r5.length - 1;
        int r3 = r11.nextSetBit(0);
    L3:
        long r6 = r0;
        if (r3 == (-1)) goto L11;
        r0 = (1 << r3) | r6;
        int r4 = smear(r3) & r10;
    L7:
        if (r5[r4] == 0) goto L8;
        r4 = (r4 + 1) & r10;
        goto L7
    L8:
        r5[r4] = (char) r3;
        r3 = r11.nextSetBit(r3 + 1);
        goto L3
    L11:
        return new SmallCharMatcher(r5, r6, r8, r12);
    }

    static int smear(int r2) {
        return Integer.rotateLeft(f97C1 * r2, 15) * f98C2;
    }

    @Override // com.google.common.base.CharMatcher
    public boolean matches(char r7) {
        if (r7 != 0) goto L6;
        return this.containsZero;
    L6:
        if (checkFilter(r7) == true) goto L8;
        return false;
    L8:
        int r0 = this.table.length - 1;
        int r3 = smear(r7) & r0;
        int r4 = r3;
    L10:
        if (this.table[r4] == 0) goto L11;
        if (this.table[r4] == r7) goto L14;
        r4 = (r4 + 1) & r0;
        if (r4 != r3) goto L10;
        return false;
    L14:
        return true;
    L11:
        return false;
    }

    @Override // com.google.common.base.CharMatcher
    void setBits(BitSet r5) {
        int r1 = 0;
        if (this.containsZero == false) goto L5;
        r5.set(0);
    L5:
        char[] r0 = this.table;
        int r2 = r0.length;
    L6:
        if (r1 >= r2) goto L11;
        char r3 = r0[r1];
        if (r3 == 0) goto L10;
        r5.set(r3);
    L10:
        r1 = r1 + 1;
        goto L6
    }
}
