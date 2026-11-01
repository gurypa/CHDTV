package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class Booleans {

    private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final boolean[] array;
        final int end;
        final int start;

        BooleanArrayAsList(boolean[] r3) {
            this(r3, 0, r3.length);
        }

        BooleanArrayAsList(boolean[] r1, int r2, int r3) {
            this.array = r1;
            this.start = r2;
            this.end = r3;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(@CheckForNull Object r5) {
            if ((r5 instanceof Boolean) == true) goto L5;
        L7:
            return false;
        L5:
            if (Booleans.access$000(this.array, ((Boolean) r5).booleanValue(), this.start, this.end) == (-1)) goto L7;
            return true;
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(@CheckForNull Object r9) {
            if (r9 != this) goto L6;
            return true;
        L6:
            if ((r9 instanceof BooleanArrayAsList) == false) goto L18;
            BooleanArrayAsList r1 = (BooleanArrayAsList) r9;
            int r2 = size();
            if (r1.size() == r2) goto L10;
            return false;
        L10:
            int r3 = 0;
        L11:
            if (r3 >= r2) goto L16;
            if (this.array[this.start + r3] != r1.array[r1.start + r3]) goto L14;
            r3 = r3 + 1;
            goto L11
        L14:
            return false;
        L16:
            return true;
        L18:
            return super.equals(r9);
        }

        @Override // java.util.AbstractList, java.util.List
        public Boolean get(int r3) {
            Preconditions.checkElementIndex(r3, size());
            return Boolean.valueOf(this.array[this.start + r3]);
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            int r0 = 1;
            int r1 = this.start;
        L4:
            if (r1 >= this.end) goto L6;
            r0 = (r0 * 31) + Booleans.hashCode(this.array[r1]);
            r1 = r1 + 1;
            goto L4
        L6:
            return r0;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(@CheckForNull Object r5) {
            if ((r5 instanceof Boolean) == false) goto L8;
            int r0 = Booleans.access$000(this.array, ((Boolean) r5).booleanValue(), this.start, this.end);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return r0 - this.start;
        L8:
            return -1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(@CheckForNull Object r5) {
            if ((r5 instanceof Boolean) == false) goto L8;
            int r0 = Booleans.access$100(this.array, ((Boolean) r5).booleanValue(), this.start, this.end);
            if (r0 >= 0) goto L7;
            return -1;
        L7:
            return r0 - this.start;
        L8:
            return -1;
        }

        public Boolean set(int r5, Boolean r6) {
            Preconditions.checkElementIndex(r5, size());
            boolean r0 = this.array[this.start + r5];
            this.array[this.start + r5] = ((Boolean) Preconditions.checkNotNull(r6)).booleanValue();
            return Boolean.valueOf(r0);
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object set(int r1, Object r2) {
            return set(r1, (Boolean) r2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.end - this.start;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Boolean> subList(int r6, int r7) {
            Preconditions.checkPositionIndexes(r6, r7, size());
            if (r6 != r7) goto L7;
            return Collections.emptyList();
        L7:
            return new BooleanArrayAsList(this.array, this.start + r6, this.start + r7);
        }

        boolean[] toBooleanArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            StringBuilder r0 = new StringBuilder(size() * 7);
            if (this.array[this.start] == false) goto L5;
            String r1 = "[true";
        L6:
            r0.append(r1);
            int r2 = this.start;
        L7:
            r2 = r2 + 1;
            if (r2 >= this.end) goto L15;
            if (this.array[r2] == false) goto L12;
            String r3 = ", true";
        L13:
            r0.append(r3);
            goto L7
        L12:
            r3 = ", false";
            goto L13
        L15:
            return r0.append(']').toString();
        L5:
            r1 = "[false";
            goto L6
        }
    }

    private static final enum BooleanComparator extends Enum<BooleanComparator> implements Comparator<Boolean> {
        private static final /* synthetic */ BooleanComparator[] $VALUES = null;
        public static final BooleanComparator FALSE_FIRST = null;
        public static final BooleanComparator TRUE_FIRST = null;
        private final String toString;
        private final int trueValue;

        private static /* synthetic */ BooleanComparator[] $values() {
            return new BooleanComparator[]{TRUE_FIRST, FALSE_FIRST};
        }

        static {
            TRUE_FIRST = new BooleanComparator("TRUE_FIRST", 0, 1, "Booleans.trueFirst()");
            FALSE_FIRST = new BooleanComparator("FALSE_FIRST", 1, -1, "Booleans.falseFirst()");
            $VALUES = $values();
        }

        private BooleanComparator(String r1, int r2, int r3, String r4) {
            super(r1, r2);
            this.trueValue = r3;
            this.toString = r4;
        }

        public static BooleanComparator valueOf(String r1) {
            return (BooleanComparator) Enum.valueOf(BooleanComparator.class, r1);
        }

        public static BooleanComparator[] values() {
            return (BooleanComparator[]) $VALUES.clone();
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(Boolean r4, Boolean r5) {
            int r1 = 0;
            if (r4.booleanValue() == false) goto L5;
            int r0 = this.trueValue;
        L7:
            if (r5.booleanValue() == false) goto L10;
            r1 = this.trueValue;
        L10:
            return r1 - r0;
        L5:
            r0 = 0;
            goto L7
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(Boolean r1, Boolean r2) {
            return compare2(r1, r2);
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.toString;
        }
    }

    private static final enum LexicographicalComparator extends Enum<LexicographicalComparator> implements Comparator<boolean[]> {
        private static final /* synthetic */ LexicographicalComparator[] $VALUES = null;
        public static final LexicographicalComparator INSTANCE = null;

        private static /* synthetic */ LexicographicalComparator[] $values() {
            return new LexicographicalComparator[]{INSTANCE};
        }

        static {
            INSTANCE = new LexicographicalComparator("INSTANCE", 0);
            $VALUES = $values();
        }

        private LexicographicalComparator(String r1, int r2) {
            super(r1, r2);
        }

        public static LexicographicalComparator valueOf(String r1) {
            return (LexicographicalComparator) Enum.valueOf(LexicographicalComparator.class, r1);
        }

        public static LexicographicalComparator[] values() {
            return (LexicographicalComparator[]) $VALUES.clone();
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(boolean[] r1, boolean[] r2) {
            return compare2(r1, r2);
        }

        /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
        public int compare2(boolean[] r5, boolean[] r6) {
            int r0 = Math.min(r5.length, r6.length);
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L9;
            int r2 = Boolean.compare(r5[r1], r6[r1]);
            if (r2 != 0) goto L6;
            r1 = r1 + 1;
            goto L3
        L6:
            return r2;
        L9:
            return r5.length - r6.length;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Booleans.lexicographicalComparator()";
        }
    }

    private Booleans() {
    }

    static /* synthetic */ int access$000(boolean[] r1, boolean r2, int r3, int r4) {
        return indexOf(r1, r2, r3, r4);
    }

    static /* synthetic */ int access$100(boolean[] r1, boolean r2, int r3, int r4) {
        return lastIndexOf(r1, r2, r3, r4);
    }

    public static List<Boolean> asList(boolean... r1) {
        if (r1.length != 0) goto L7;
        return Collections.emptyList();
    L7:
        return new BooleanArrayAsList(r1);
    }

    private static int checkNoOverflow(long r2) {
        if (r2 != ((int) r2)) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0, "the total number of elements (%s) in the arrays must fit in an int", r2);
        return (int) r2;
    L5:
        r0 = false;
        goto L6
    }

    public static int compare(boolean r1, boolean r2) {
        return Boolean.compare(r1, r2);
    }

    public static boolean[] concat(boolean[]... r9) {
        long r0 = 0;
        int r2 = r9.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L5;
        r0 = r0 + ((long) r9[r4].length);
        r4 = r4 + 1;
        goto L3
    L5:
        boolean[] r3 = new boolean[checkNoOverflow(r0)];
        int r5 = 0;
        int r6 = r9.length;
        int r7 = 0;
    L6:
        if (r7 >= r6) goto L8;
        boolean[] r8 = r9[r7];
        System.arraycopy(r8, 0, r3, r5, r8.length);
        r5 = r5 + r8.length;
        r7 = r7 + 1;
        goto L6
    L8:
        return r3;
    }

    public static boolean contains(boolean[] r4, boolean r5) {
        int r0 = r4.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (r4[r2] == r5) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public static int countTrue(boolean... r4) {
        int r0 = 0;
        int r1 = r4.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L8;
        if (r4[r2] == false) goto L7;
        r0 = r0 + 1;
    L7:
        r2 = r2 + 1;
        goto L3
    L8:
        return r0;
    }

    public static boolean[] ensureCapacity(boolean[] r4, int r5, int r6) {
        boolean r0 = true;
        if (r5 < 0) goto L5;
        boolean r2 = true;
    L6:
        Preconditions.checkArgument(r2, "Invalid minLength: %s", r5);
        if (r6 >= 0) goto L10;
        r0 = false;
    L10:
        Preconditions.checkArgument(r0, "Invalid padding: %s", r6);
        if (r4.length >= r5) goto L14;
        return Arrays.copyOf(r4, r5 + r6);
    L14:
        return r4;
    L5:
        r2 = false;
        goto L6
    }

    public static Comparator<Boolean> falseFirst() {
        return BooleanComparator.FALSE_FIRST;
    }

    public static int hashCode(boolean r1) {
        if (r1 == false) goto L4;
        return 1231;
    L4:
        return 1237;
    }

    public static int indexOf(boolean[] r2, boolean r3) {
        return indexOf(r2, r3, 0, r2.length);
    }

    private static int indexOf(boolean[] r2, boolean r3, int r4, int r5) {
        int r0 = r4;
    L3:
        if (r0 >= r5) goto L8;
        if (r2[r0] == r3) goto L6;
        r0 = r0 + 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public static int indexOf(boolean[] r4, boolean[] r5) {
        Preconditions.checkNotNull(r4, "array");
        Preconditions.checkNotNull(r5, "target");
        if (r5.length != 0) goto L6;
        return 0;
    L6:
        int r0 = 0;
    L8:
        if (r0 >= ((r4.length - r5.length) + 1)) goto L17;
        int r1 = 0;
    L11:
        if (r1 >= r5.length) goto L16;
        if (r4[r0 + r1] != r5[r1]) goto L14;
        r1 = r1 + 1;
        goto L11
    L14:
        r0 = r0 + 1;
        goto L8
    L16:
        return r0;
    L17:
        return -1;
    }

    public static String join(String r4, boolean... r5) {
        Preconditions.checkNotNull(r4);
        if (r5.length != 0) goto L6;
        return "";
    L6:
        StringBuilder r0 = new StringBuilder(r5.length * 7);
        r0.append(r5[0]);
        int r1 = 1;
    L8:
        if (r1 >= r5.length) goto L11;
        r0.append(r4).append(r5[r1]);
        r1 = r1 + 1;
        goto L8
    L11:
        return r0.toString();
    }

    public static int lastIndexOf(boolean[] r2, boolean r3) {
        return lastIndexOf(r2, r3, 0, r2.length);
    }

    private static int lastIndexOf(boolean[] r2, boolean r3, int r4, int r5) {
        int r0 = r5 - 1;
    L3:
        if (r0 < r4) goto L8;
        if (r2[r0] == r3) goto L6;
        r0 = r0 - 1;
        goto L3
    L6:
        return r0;
    L8:
        return -1;
    }

    public static Comparator<boolean[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static void reverse(boolean[] r2) {
        Preconditions.checkNotNull(r2);
        reverse(r2, 0, r2.length);
    }

    public static void reverse(boolean[] r4, int r5, int r6) {
        Preconditions.checkNotNull(r4);
        Preconditions.checkPositionIndexes(r5, r6, r4.length);
        int r0 = r5;
        int r1 = r6 - 1;
    L3:
        if (r0 >= r1) goto L5;
        boolean r2 = r4[r0];
        r4[r0] = r4[r1];
        r4[r1] = r2;
        r0 = r0 + 1;
        r1 = r1 - 1;
        goto L3
    }

    public static void rotate(boolean[] r2, int r3) {
        rotate(r2, r3, 0, r2.length);
    }

    public static void rotate(boolean[] r3, int r4, int r5, int r6) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkPositionIndexes(r5, r6, r3.length);
        if (r3.length > 1) goto L5;
        return;
    L5:
        int r0 = r6 - r5;
        int r1 = (-r4) % r0;
        if (r1 >= 0) goto L8;
        int r2 = r1 + r0;
    L9:
        int r7 = r2 + r5;
        if (r7 != r5) goto L12;
        return;
    L12:
        reverse(r3, r5, r7);
        reverse(r3, r7, r6);
        reverse(r3, r5, r6);
        return;
    L8:
        r2 = r1;
        goto L9
    }

    public static boolean[] toArray(Collection<Boolean> r5) {
        if ((r5 instanceof BooleanArrayAsList) == true) goto L5;
        Object[] r0 = r5.toArray();
        int r1 = r0.length;
        boolean[] r2 = new boolean[r1];
        int r3 = 0;
    L7:
        if (r3 >= r1) goto L9;
        r2[r3] = ((Boolean) Preconditions.checkNotNull(r0[r3])).booleanValue();
        r3 = r3 + 1;
        goto L7
    L9:
        return r2;
    L5:
        return ((BooleanArrayAsList) r5).toBooleanArray();
    }

    public static Comparator<Boolean> trueFirst() {
        return BooleanComparator.TRUE_FIRST;
    }
}
