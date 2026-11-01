package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class SortedLists {

    /* JADX INFO: renamed from: com.google.common.collect.SortedLists$1 */
    static /* synthetic */ class C07321 {
    }

    static abstract enum KeyAbsentBehavior extends Enum<KeyAbsentBehavior> {
        private static final /* synthetic */ KeyAbsentBehavior[] $VALUES = null;
        public static final KeyAbsentBehavior INVERTED_INSERTION_INDEX = null;
        public static final KeyAbsentBehavior NEXT_HIGHER = null;
        public static final KeyAbsentBehavior NEXT_LOWER = null;

        private static /* synthetic */ KeyAbsentBehavior[] $values() {
            return new KeyAbsentBehavior[]{NEXT_LOWER, NEXT_HIGHER, INVERTED_INSERTION_INDEX};
        }

        static {
            final String r1 = "NEXT_LOWER";
            final int r2 = 0;
            NEXT_LOWER = new C07331(r1, r2);
            final String r3 = "NEXT_HIGHER";
            final int r4 = 1;
            NEXT_HIGHER = new C07342(r3, r4);
            final String r5 = "INVERTED_INSERTION_INDEX";
            final int r6 = 2;
            INVERTED_INSERTION_INDEX = new C07353(r5, r6);
            $VALUES = $values();
        }

        private KeyAbsentBehavior(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ KeyAbsentBehavior(String r1, int r2, C07321 r3) {
            this(r1, r2);
        }

        public static KeyAbsentBehavior valueOf(String r1) {
            return (KeyAbsentBehavior) Enum.valueOf(KeyAbsentBehavior.class, r1);
        }

        public static KeyAbsentBehavior[] values() {
            return (KeyAbsentBehavior[]) $VALUES.clone();
        }

        abstract int resultIndex(int r1);
    }

    static abstract enum KeyPresentBehavior extends Enum<KeyPresentBehavior> {
        private static final /* synthetic */ KeyPresentBehavior[] $VALUES = null;
        public static final KeyPresentBehavior ANY_PRESENT = null;
        public static final KeyPresentBehavior FIRST_AFTER = null;
        public static final KeyPresentBehavior FIRST_PRESENT = null;
        public static final KeyPresentBehavior LAST_BEFORE = null;
        public static final KeyPresentBehavior LAST_PRESENT = null;

        private static /* synthetic */ KeyPresentBehavior[] $values() {
            return new KeyPresentBehavior[]{ANY_PRESENT, LAST_PRESENT, FIRST_PRESENT, FIRST_AFTER, LAST_BEFORE};
        }

        static {
            final String r1 = "ANY_PRESENT";
            final int r2 = 0;
            ANY_PRESENT = new C07361(r1, r2);
            final String r3 = "LAST_PRESENT";
            final int r4 = 1;
            LAST_PRESENT = new C07372(r3, r4);
            final String r5 = "FIRST_PRESENT";
            final int r6 = 2;
            FIRST_PRESENT = new C07383(r5, r6);
            final String r7 = "FIRST_AFTER";
            final int r8 = 3;
            FIRST_AFTER = new C07394(r7, r8);
            final String r9 = "LAST_BEFORE";
            final int r10 = 4;
            LAST_BEFORE = new C07405(r9, r10);
            $VALUES = $values();
        }

        private KeyPresentBehavior(String r1, int r2) {
            super(r1, r2);
        }

        /* synthetic */ KeyPresentBehavior(String r1, int r2, C07321 r3) {
            this(r1, r2);
        }

        public static KeyPresentBehavior valueOf(String r1) {
            return (KeyPresentBehavior) Enum.valueOf(KeyPresentBehavior.class, r1);
        }

        public static KeyPresentBehavior[] values() {
            return (KeyPresentBehavior[]) $VALUES.clone();
        }

        abstract <E> int resultIndex(Comparator<? super E> r1, @ParametricNullness E r2, List<? extends E> r3, int r4);
    }

    private SortedLists() {
    }

    public static <E, K extends Comparable> int binarySearch(List<E> r6, Function<? super E, K> r7, K r8, KeyPresentBehavior r9, KeyAbsentBehavior r10) {
        Preconditions.checkNotNull(r8);
        return binarySearch(r6, r7, r8, Ordering.natural(), r9, r10);
    }

    public static <E, K> int binarySearch(List<E> r1, Function<? super E, K> r2, @ParametricNullness K r3, Comparator<? super K> r4, KeyPresentBehavior r5, KeyAbsentBehavior r6) {
        return binarySearch(Lists.transform(r1, r2), r3, r4, r5, r6);
    }

    public static <E extends Comparable> int binarySearch(List<? extends E> r1, E r2, KeyPresentBehavior r3, KeyAbsentBehavior r4) {
        Preconditions.checkNotNull(r2);
        return binarySearch(r1, r2, Ordering.natural(), r3, r4);
    }

    public static <E> int binarySearch(List<? extends E> r6, @ParametricNullness E r7, Comparator<? super E> r8, KeyPresentBehavior r9, KeyAbsentBehavior r10) {
        Preconditions.checkNotNull(r8);
        Preconditions.checkNotNull(r6);
        Preconditions.checkNotNull(r9);
        Preconditions.checkNotNull(r10);
        if ((r6 instanceof RandomAccess) == true) goto L5;
        r6 = new ArrayList(r6);
    L5:
        int r0 = 0;
        int r1 = r6.size() - 1;
    L6:
        if (r0 > r1) goto L16;
        int r2 = (r0 + r1) >>> 1;
        int r3 = r8.compare(r7, r6.get(r2));
        if (r3 < 0) goto L9;
        if (r3 <= 0) goto L14;
        r0 = r2 + 1;
        goto L6
    L14:
        return r9.resultIndex(r8, r7, r6.subList(r0, r1 + 1), r2 - r0) + r0;
    L9:
        r1 = r2 - 1;
        goto L6
    L16:
        return r10.resultIndex(r0);
    }
}
