package androidx.recyclerview.widget;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DiffUtil {
    private static final Comparator<Diagonal> DIAGONAL_COMPARATOR = null;

    public static abstract class Callback {
        public Callback() {
        }

        public abstract boolean areContentsTheSame(int r1, int r2);

        public abstract boolean areItemsTheSame(int r1, int r2);

        public Object getChangePayload(int r2, int r3) {
            return null;
        }

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    static class CenteredArray {
        private final int[] mData;
        private final int mMid;

        CenteredArray(int r2) {
            this.mData = new int[r2];
            this.mMid = this.mData.length / 2;
        }

        int[] backingData() {
            return this.mData;
        }

        public void fill(int r2) {
            Arrays.fill(this.mData, r2);
        }

        int get(int r3) {
            return this.mData[this.mMid + r3];
        }

        void set(int r3, int r4) {
            this.mData[this.mMid + r3] = r4;
        }
    }

    static class Diagonal {
        public final int size;

        /* JADX INFO: renamed from: x */
        public final int f69x;

        /* JADX INFO: renamed from: y */
        public final int f70y;

        Diagonal(int r1, int r2, int r3) {
            this.f69x = r1;
            this.f70y = r2;
            this.size = r3;
        }

        int endX() {
            return this.f69x + this.size;
        }

        int endY() {
            return this.f70y + this.size;
        }
    }

    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_MASK = 15;
        private static final int FLAG_MOVED = 12;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 4;
        public static final int NO_POSITION = -1;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final List<Diagonal> mDiagonals;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;

        DiffResult(Callback r3, List<Diagonal> r4, int[] r5, int[] r6, boolean r7) {
            this.mDiagonals = r4;
            this.mOldItemStatuses = r5;
            this.mNewItemStatuses = r6;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = r3;
            this.mOldListSize = r3.getOldListSize();
            this.mNewListSize = r3.getNewListSize();
            this.mDetectMoves = r7;
            addEdgeDiagonals();
            findMatchingItems();
        }

        private void addEdgeDiagonals() {
            if (this.mDiagonals.isEmpty() == false) goto L5;
            Diagonal r0 = null;
        L6:
            if (r0 != null) goto L8;
        L11:
            this.mDiagonals.add(0, new Diagonal(0, 0, 0));
        L12:
            this.mDiagonals.add(new Diagonal(this.mOldListSize, this.mNewListSize, 0));
            return;
        L8:
            if (r0.f69x != 0) goto L11;
            if (r0.f70y == 0) goto L12;
        L5:
            r0 = this.mDiagonals.get(0);
            goto L6
        }

        private void findMatchingAddition(int r10) {
            int r0 = 0;
            int r1 = this.mDiagonals.size();
            int r2 = 0;
        L3:
            if (r2 >= r1) goto L19;
            Diagonal r3 = this.mDiagonals.get(r2);
        L6:
            if (r0 >= r3.f70y) goto L18;
            if (this.mNewItemStatuses[r0] != 0) goto L17;
            if (this.mCallback.areItemsTheSame(r10, r0) == false) goto L17;
            if (this.mCallback.areContentsTheSame(r10, r0) == false) goto L14;
            int r6 = 8;
        L15:
            this.mOldItemStatuses[r10] = (r0 << 4) | r6;
            this.mNewItemStatuses[r0] = (r10 << 4) | r6;
            return;
        L14:
            r6 = 4;
        L17:
            r0 = r0 + 1;
            goto L6
        L18:
            r0 = r3.endY();
            r2 = r2 + 1;
            goto L3
        }

        private void findMatchingItems() {
            Iterator<Diagonal> r0 = this.mDiagonals.iterator();
        L4:
            if (r0.hasNext() == false) goto L15;
            Diagonal r1 = r0.next();
            int r2 = 0;
        L7:
            if (r2 >= r1.size) goto L4;
            int r3 = r1.f69x + r2;
            int r4 = r1.f70y + r2;
            if (this.mCallback.areContentsTheSame(r3, r4) == false) goto L11;
            int r6 = 1;
        L12:
            this.mOldItemStatuses[r3] = (r4 << 4) | r6;
            this.mNewItemStatuses[r4] = (r3 << 4) | r6;
            r2 = r2 + 1;
            goto L7
        L11:
            r6 = 2;
            goto L12
        L15:
            if (this.mDetectMoves == false) goto L22;
            findMoveMatches();
            return;
        }

        private void findMoveMatches() {
            int r0 = 0;
            Iterator<Diagonal> r1 = this.mDiagonals.iterator();
        L4:
            if (r1.hasNext() == false) goto L13;
            Diagonal r2 = r1.next();
        L7:
            if (r0 >= r2.f69x) goto L12;
            if (this.mOldItemStatuses[r0] != 0) goto L11;
            findMatchingAddition(r0);
        L11:
            r0 = r0 + 1;
            goto L7
        L12:
            r0 = r2.endX();
            goto L4
        }

        private static PostponedUpdate getPostponedUpdate(Collection<PostponedUpdate> r4, int r5, boolean r6) {
            PostponedUpdate r0 = null;
            Iterator<PostponedUpdate> r1 = r4.iterator();
        L4:
            if (r1.hasNext() == false) goto L12;
            PostponedUpdate r2 = r1.next();
            if (r2.posInOwnerList != r5) goto L4;
            if (r2.removal != r6) goto L4;
            r0 = r2;
            r1.remove();
        L12:
            if (r1.hasNext() == false) goto L18;
            PostponedUpdate r3 = r1.next();
            if (r6 == true) goto L15;
            r3.currentPos++;
            goto L12
        L15:
            r3.currentPos--;
            goto L12
        L18:
            return r0;
        }

        public int convertNewPositionToOld(int r4) {
            if (r4 < 0) goto L12;
            if (r4 >= this.mNewListSize) goto L12;
            int r0 = this.mNewItemStatuses[r4];
            if ((r0 & 15) != 0) goto L10;
            return -1;
        L10:
            return r0 >> 4;
        L12:
            throw new IndexOutOfBoundsException("Index out of bounds - passed position = " + r4 + ", new list size = " + this.mNewListSize);
        }

        public int convertOldPositionToNew(int r4) {
            if (r4 < 0) goto L12;
            if (r4 >= this.mOldListSize) goto L12;
            int r0 = this.mOldItemStatuses[r4];
            if ((r0 & 15) != 0) goto L10;
            return -1;
        L10:
            return r0 >> 4;
        L12:
            throw new IndexOutOfBoundsException("Index out of bounds - passed position = " + r4 + ", old list size = " + this.mOldListSize);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void dispatchUpdatesTo(ListUpdateCallback r19) {
            ListUpdateCallback r1 = r19;
            if ((r1 instanceof BatchingListUpdateCallback) == false) goto L5;
            BatchingListUpdateCallback r2 = (BatchingListUpdateCallback) r1;
        L6:
            int r3 = this.mOldListSize;
            ArrayDeque r4 = new ArrayDeque();
            int r5 = this.mOldListSize;
            int r6 = this.mNewListSize;
            int r8 = 1;
            int r7 = this.mDiagonals.size() - 1;
        L7:
            if (r7 < 0) goto L46;
            Diagonal r9 = this.mDiagonals.get(r7);
            int r10 = r9.endX();
            int r11 = r9.endY();
        L9:
            boolean r12 = false;
            if (r5 <= r10) goto L24;
            r5 = r5 - 1;
            int r13 = this.mOldItemStatuses[r5];
            if ((r13 & 12) == 0) goto L22;
            int r14 = r13 >> 4;
            PostponedUpdate r15 = getPostponedUpdate(r4, r14, false);
            if (r15 == null) goto L20;
            int r16 = r3 - r15.currentPos;
            r2.onMoved(r5, r16 - 1);
            if ((r13 & 4) == 0) goto L18;
            int r17 = r3;
            r2.onChanged(r16 - 1, 1, this.mCallback.getChangePayload(r5, r14));
        L21:
            r3 = r17;
        L23:
            r1 = r1;
            r8 = 1;
            goto L9
        L18:
            r17 = r3;
            goto L21
        L20:
            r17 = r3;
            boolean r18 = r8;
            r4.add(new PostponedUpdate(r5, (r17 - r5) - (r18 ? 1 : 0), r18));
            goto L21
        L22:
            r1 = r1;
            r2.onRemoved(r5, r8);
            r3 = r3 - 1;
            goto L23
        L24:
            ListUpdateCallback r110 = r1;
        L25:
            if (r6 <= r11) goto L37;
            r6 = r6 - 1;
            int r20 = this.mNewItemStatuses[r6];
            if ((r20 & 12) == 0) goto L35;
            int r21 = r20 >> 4;
            PostponedUpdate r111 = getPostponedUpdate(r4, r21, true);
            if (r111 != null) goto L31;
            r4.add(new PostponedUpdate(r6, r3 - r5, r12));
        L36:
            r12 = false;
            goto L25
        L31:
            r2.onMoved((r3 - r111.currentPos) - 1, r5);
            if ((r20 & 4) == 0) goto L36;
            r2.onChanged(r5, 1, this.mCallback.getChangePayload(r21, r6));
            goto L36
        L35:
            r2.onInserted(r5, 1);
            r3 = r3 + 1;
            goto L36
        L37:
            int r22 = r9.f69x;
            int r23 = r9.f70y;
            int r24 = 0;
        L39:
            if (r24 >= r9.size) goto L45;
            if ((this.mOldItemStatuses[r22] & 15) != 2) goto L44;
            r2.onChanged(r22, 1, this.mCallback.getChangePayload(r22, r23));
        L44:
            r22 = r22 + 1;
            r23 = r23 + 1;
            r24 = r24 + 1;
            goto L39
        L45:
            int r25 = r9.f69x;
            r6 = r9.f70y;
            r7 = r7 - 1;
            r5 = r25;
            r8 = 1;
            r1 = r110;
            goto L7
        L46:
            r2.dispatchLastEvent();
            return;
        L5:
            r2 = new BatchingListUpdateCallback(r1);
            r1 = r2;
            goto L6
        }

        public void dispatchUpdatesTo(RecyclerView.Adapter r2) {
            dispatchUpdatesTo(new AdapterListUpdateCallback(r2));
        }
    }

    public static abstract class ItemCallback<T> {
        public ItemCallback() {
        }

        public abstract boolean areContentsTheSame(T r1, T r2);

        public abstract boolean areItemsTheSame(T r1, T r2);

        public Object getChangePayload(T r2, T r3) {
            return null;
        }
    }

    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        PostponedUpdate(int r1, int r2, boolean r3) {
            this.posInOwnerList = r1;
            this.currentPos = r2;
            this.removal = r3;
        }
    }

    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int r1, int r2, int r3, int r4) {
            this.oldListStart = r1;
            this.oldListEnd = r2;
            this.newListStart = r3;
            this.newListEnd = r4;
        }

        int newSize() {
            return this.newListEnd - this.newListStart;
        }

        int oldSize() {
            return this.oldListEnd - this.oldListStart;
        }
    }

    static class Snake {
        public int endX;
        public int endY;
        public boolean reverse;
        public int startX;
        public int startY;

        Snake() {
        }

        int diagonalSize() {
            return Math.min(this.endX - this.startX, this.endY - this.startY);
        }

        boolean hasAdditionOrRemoval() {
            if ((this.endY - this.startY) == (this.endX - this.startX)) goto L5;
            return true;
        L5:
            return false;
        }

        boolean isAddition() {
            if ((this.endY - this.startY) <= (this.endX - this.startX)) goto L5;
            return true;
        L5:
            return false;
        }

        Diagonal toDiagonal() {
            if (hasAdditionOrRemoval() == false) goto L15;
            if (this.reverse == false) goto L9;
            return new Diagonal(this.startX, this.startY, diagonalSize());
        L9:
            if (isAddition() == false) goto L13;
            return new Diagonal(this.startX, this.startY + 1, diagonalSize());
        L13:
            return new Diagonal(this.startX + 1, this.startY, diagonalSize());
        L15:
            return new Diagonal(this.startX, this.startY, this.endX - this.startX);
        }
    }

    static {
        DIAGONAL_COMPARATOR = new C04841();
    }

    private DiffUtil() {
    }

    private static Snake backward(Range r11, Callback r12, CenteredArray r13, CenteredArray r14, int r15) {
        if (((r11.oldSize() - r11.newSize()) % 2) != 0) goto L5;
        boolean r0 = true;
    L6:
        int r2 = r11.oldSize() - r11.newSize();
        int r3 = -r15;
    L7:
        if (r3 > r15) goto L39;
        if (r3 == (-r15)) goto L15;
        if (r3 != r15) goto L12;
    L14:
        int r4 = r14.get(r3 - 1);
        int r5 = r4 - 1;
    L16:
        int r6 = r11.newListEnd - ((r11.oldListEnd - r5) - r3);
        if (r15 == 0) goto L21;
        if (r5 != r4) goto L21;
        int r7 = r6 + 1;
    L23:
        if (r5 <= r11.oldListStart) goto L29;
        if (r6 <= r11.newListStart) goto L29;
        if (r12.areItemsTheSame(r5 - 1, r6 - 1) == false) goto L29;
        r5 = r5 - 1;
        r6 = r6 - 1;
    L29:
        r14.set(r3, r5);
        if (r0 == false) goto L38;
        int r8 = r2 - r3;
        if (r8 < (-r15)) goto L38;
        if (r8 > r15) goto L38;
        if (r13.get(r8) < r5) goto L38;
        Snake r9 = new Snake();
        r9.startX = r5;
        r9.startY = r6;
        r9.endX = r4;
        r9.endY = r7;
        r9.reverse = true;
        return r9;
    L38:
        r3 = r3 + 2;
    L21:
        r7 = r6;
        goto L23
    L12:
        if (r14.get(r3 + 1) >= r14.get(r3 - 1)) goto L14;
    L15:
        r5 = r14.get(r3 + 1);
        r4 = r5;
        goto L16
    L39:
        return null;
    L5:
        r0 = false;
        goto L6
    }

    public static DiffResult calculateDiff(Callback r1) {
        return calculateDiff(r1, true);
    }

    public static DiffResult calculateDiff(Callback r13, boolean r14) {
        int r0 = r13.getOldListSize();
        int r1 = r13.getNewListSize();
        ArrayList r2 = new ArrayList();
        ArrayList r3 = new ArrayList();
        r3.add(new Range(0, r0, 0, r1));
        int r9 = ((r0 + r1) + 1) / 2;
        CenteredArray r4 = new CenteredArray((r9 * 2) + 1);
        CenteredArray r5 = new CenteredArray((r9 * 2) + 1);
        ArrayList r6 = new ArrayList();
    L4:
        if (r3.isEmpty() == true) goto L17;
        Range r7 = (Range) r3.remove(r3.size() - 1);
        Snake r8 = midPoint(r7, r13, r4, r5);
        if (r8 != null) goto L8;
        r6.add(r7);
        goto L4
    L8:
        if (r8.diagonalSize() <= 0) goto L11;
        r2.add(r8.toDiagonal());
    L11:
        if (r6.isEmpty() == false) goto L13;
        Range r10 = new Range();
    L14:
        r10.oldListStart = r7.oldListStart;
        r10.newListStart = r7.newListStart;
        r10.oldListEnd = r8.startX;
        r10.newListEnd = r8.startY;
        r3.add(r10);
        r7.oldListEnd = r7.oldListEnd;
        r7.newListEnd = r7.newListEnd;
        r7.oldListStart = r8.endX;
        r7.newListStart = r8.endY;
        r3.add(r7);
        goto L4
    L13:
        r10 = (Range) r6.remove(r6.size() - 1);
        goto L14
    L17:
        Collections.sort(r2, DIAGONAL_COMPARATOR);
        return new DiffResult(r13, r2, r4.backingData(), r5.backingData(), r14);
    }

    private static Snake forward(Range r17, Callback r18, CenteredArray r19, CenteredArray r20, int r21) {
        if ((Math.abs(r17.oldSize() - r17.newSize()) % 2) != 1) goto L5;
        boolean r3 = true;
    L6:
        int r6 = r17.oldSize() - r17.newSize();
        int r7 = -r21;
    L7:
        if (r7 > r21) goto L44;
        if (r7 == (-r21)) goto L15;
        if (r7 != r21) goto L12;
    L14:
        int r8 = r19.get(r7 - 1);
        int r9 = r8 + 1;
    L16:
        int r10 = (r17.newListStart + (r9 - r17.oldListStart)) - r7;
        if (r21 == 0) goto L21;
        if (r9 != r8) goto L21;
        int r11 = r10 - 1;
    L23:
        if (r9 >= r17.oldListEnd) goto L30;
        if (r10 >= r17.newListEnd) goto L30;
        if (r18.areItemsTheSame(r9, r10) == false) goto L30;
        r9 = r9 + 1;
        r10 = r10 + 1;
    L30:
        r19.set(r7, r9);
        if (r3 == false) goto L42;
        int r13 = r6 - r7;
        if (r13 < ((-r21) + 1)) goto L42;
        if (r13 > (r21 - 1)) goto L42;
        if (r20.get(r13) > r9) goto L42;
        Snake r5 = new Snake();
        r5.startX = r8;
        r5.startY = r11;
        r5.endX = r9;
        r5.endY = r10;
        r5.reverse = false;
        return r5;
    L42:
        r7 = r7 + 2;
    L21:
        r11 = r10;
        goto L23
    L12:
        if (r19.get(r7 + 1) <= r19.get(r7 - 1)) goto L14;
    L15:
        r9 = r19.get(r7 + 1);
        r8 = r9;
        goto L16
    L44:
        return null;
    L5:
        r3 = false;
        goto L6
    }

    private static Snake midPoint(Range r4, Callback r5, CenteredArray r6, CenteredArray r7) {
        if (r4.oldSize() >= 1) goto L5;
    L17:
        return null;
    L5:
        if (r4.newSize() < 1) goto L17;
        int r0 = ((r4.oldSize() + r4.newSize()) + 1) / 2;
        r6.set(1, r4.oldListStart);
        r7.set(1, r4.oldListEnd);
        int r2 = 0;
    L8:
        if (r2 >= r0) goto L16;
        Snake r3 = forward(r4, r5, r6, r7, r2);
        if (r3 != null) goto L11;
        Snake r8 = backward(r4, r5, r6, r7, r2);
        if (r8 != null) goto L14;
        r2 = r2 + 1;
        goto L8
    L14:
        return r8;
    L11:
        return r3;
    L16:
        return null;
    }
}
