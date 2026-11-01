package androidx.media3.exoplayer.source;

import java.util.Arrays;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public interface ShuffleOrder {

    public static class DefaultShuffleOrder implements ShuffleOrder {
        private final int[] indexInShuffled;
        private final Random random;
        private final int[] shuffled;

        public DefaultShuffleOrder(int r2) {
            this(r2, new Random());
        }

        public DefaultShuffleOrder(int r2, long r3) {
            this(r2, new Random(r3));
        }

        private DefaultShuffleOrder(int r2, Random r3) {
            this(createShuffledList(r2, r3), r3);
        }

        public DefaultShuffleOrder(int[] r3, long r4) {
            this(Arrays.copyOf(r3, r3.length), new Random(r4));
        }

        private DefaultShuffleOrder(int[] r4, Random r5) {
            this.shuffled = r4;
            this.random = r5;
            this.indexInShuffled = new int[r4.length];
            int r0 = 0;
        L4:
            if (r0 >= r4.length) goto L6;
            this.indexInShuffled[r4[r0]] = r0;
            r0 = r0 + 1;
            goto L4
        }

        private static int[] createShuffledList(int r4, Random r5) {
            int[] r0 = new int[r4];
            int r1 = 0;
        L3:
            if (r1 >= r4) goto L5;
            int r2 = r5.nextInt(r1 + 1);
            r0[r1] = r0[r2];
            r0[r2] = r1;
            r1 = r1 + 1;
            goto L3
        L5:
            return r0;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public ShuffleOrder cloneAndClear() {
            return new DefaultShuffleOrder(0, new Random(this.random.nextLong()));
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public ShuffleOrder cloneAndInsert(int r10, int r11) {
            int[] r0 = new int[r11];
            int[] r1 = new int[r11];
            int r2 = 0;
        L3:
            if (r2 >= r11) goto L5;
            r0[r2] = this.random.nextInt(this.shuffled.length + 1);
            int r3 = this.random.nextInt(r2 + 1);
            r1[r2] = r1[r3];
            r1[r3] = r2 + r10;
            r2 = r2 + 1;
            goto L3
        L5:
            Arrays.sort(r0);
            int[] r4 = new int[this.shuffled.length + r11];
            int r5 = 0;
            int r6 = 0;
            int r7 = 0;
        L7:
            if (r7 >= (this.shuffled.length + r11)) goto L18;
            if (r6 < r11) goto L10;
        L12:
            int r8 = r5 + 1;
            r4[r7] = this.shuffled[r5];
            if (r4[r7] < r10) goto L15;
            r4[r7] = r4[r7] + r11;
        L15:
            r5 = r8;
        L16:
            r7 = r7 + 1;
            goto L7
        L10:
            if (r5 != r0[r6]) goto L12;
            int r9 = r6 + 1;
            r4[r7] = r1[r6];
            r6 = r9;
            goto L16
        L18:
            return new DefaultShuffleOrder(r4, new Random(this.random.nextLong()));
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public ShuffleOrder cloneAndRemove(int r8, int r9) {
            int r0 = r9 - r8;
            int[] r1 = new int[this.shuffled.length - r0];
            int r2 = 0;
            int r3 = 0;
        L4:
            if (r3 >= this.shuffled.length) goto L17;
            if (this.shuffled[r3] >= r8) goto L8;
        L10:
            int r4 = r3 - r2;
            if (this.shuffled[r3] < r8) goto L13;
            int r5 = this.shuffled[r3] - r0;
        L14:
            r1[r4] = r5;
        L15:
            r3 = r3 + 1;
            goto L4
        L13:
            r5 = this.shuffled[r3];
            goto L14
        L8:
            if (this.shuffled[r3] >= r9) goto L10;
            r2 = r2 + 1;
            goto L15
        L17:
            return new DefaultShuffleOrder(r1, new Random(this.random.nextLong()));
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getFirstIndex() {
            if (this.shuffled.length > 0) goto L7;
            return -1;
        L7:
            return this.shuffled[0];
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getLastIndex() {
            if (this.shuffled.length > 0) goto L7;
            return -1;
        L7:
            return this.shuffled[this.shuffled.length - 1];
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getLength() {
            return this.shuffled.length;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getNextIndex(int r3) {
            int r0 = this.indexInShuffled[r3] + 1;
            if (r0 < this.shuffled.length) goto L7;
            return -1;
        L7:
            return this.shuffled[r0];
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getPreviousIndex(int r3) {
            int r0 = this.indexInShuffled[r3] - 1;
            if (r0 >= 0) goto L5;
            return -1;
        L5:
            return this.shuffled[r0];
        }
    }

    public static final class UnshuffledShuffleOrder implements ShuffleOrder {
        private final int length;

        public UnshuffledShuffleOrder(int r1) {
            this.length = r1;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public ShuffleOrder cloneAndClear() {
            return new UnshuffledShuffleOrder(0);
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public ShuffleOrder cloneAndInsert(int r3, int r4) {
            return new UnshuffledShuffleOrder(this.length + r4);
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public ShuffleOrder cloneAndRemove(int r3, int r4) {
            return new UnshuffledShuffleOrder((this.length - r4) + r3);
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getFirstIndex() {
            if (this.length <= 0) goto L5;
            return 0;
        L5:
            return -1;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getLastIndex() {
            if (this.length > 0) goto L7;
            return -1;
        L7:
            return this.length - 1;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getLength() {
            return this.length;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getNextIndex(int r2) {
            int r3 = r2 + 1;
            if (r3 < this.length) goto L7;
            return -1;
        L7:
            return r3;
        }

        @Override // androidx.media3.exoplayer.source.ShuffleOrder
        public int getPreviousIndex(int r2) {
            int r3 = r2 - 1;
            if (r3 >= 0) goto L5;
            return -1;
        L5:
            return r3;
        }
    }

    ShuffleOrder cloneAndClear();

    ShuffleOrder cloneAndInsert(int r1, int r2);

    ShuffleOrder cloneAndRemove(int r1, int r2);

    int getFirstIndex();

    int getLastIndex();

    int getLength();

    int getNextIndex(int r1);

    int getPreviousIndex(int r1);
}
