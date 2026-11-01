package androidx.customview.widget;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
class FocusStrategy {

    public interface BoundsAdapter<T> {
        void obtainBounds(T r1, Rect r2);
    }

    public interface CollectionAdapter<T, V> {
        V get(T r1, int r2);

        int size(T r1);
    }

    private static class SequentialComparator<T> implements Comparator<T> {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1;
        private final Rect mTemp2;

        SequentialComparator(boolean r2, BoundsAdapter<T> r3) {
            this.mTemp1 = new Rect();
            this.mTemp2 = new Rect();
            this.mIsLayoutRtl = r2;
            this.mAdapter = r3;
        }

        @Override // java.util.Comparator
        public int compare(T r7, T r8) {
            Rect r0 = this.mTemp1;
            Rect r1 = this.mTemp2;
            this.mAdapter.obtainBounds(r7, r0);
            this.mAdapter.obtainBounds(r8, r1);
            if (r0.top >= r1.top) goto L6;
            return -1;
        L6:
            if (r0.top <= r1.top) goto L9;
            return 1;
        L9:
            if (r0.left >= r1.left) goto L15;
            if (this.mIsLayoutRtl == true) goto L13;
            return -1;
        L13:
            return 1;
        L15:
            if (r0.left <= r1.left) goto L22;
            if (this.mIsLayoutRtl == false) goto L20;
            return -1;
        L20:
            return 1;
        L22:
            if (r0.bottom >= r1.bottom) goto L25;
            return -1;
        L25:
            if (r0.bottom <= r1.bottom) goto L28;
            return 1;
        L28:
            if (r0.right >= r1.right) goto L34;
            if (this.mIsLayoutRtl == true) goto L32;
            return -1;
        L32:
            return 1;
        L34:
            if (r0.right > r1.right) goto L36;
            return 0;
        L36:
            if (this.mIsLayoutRtl == false) goto L39;
            return -1;
        L39:
            return 1;
        }
    }

    private FocusStrategy() {
    }

    private static boolean beamBeats(int r6, Rect r7, Rect r8, Rect r9) {
        boolean r0 = beamsOverlap(r6, r7, r8);
        if (beamsOverlap(r6, r7, r9) == true) goto L19;
        if (r0 == false) goto L19;
        if (isToDirectionOf(r6, r7, r9) == true) goto L10;
        return true;
    L10:
        if (r6 != 17) goto L12;
    L18:
        return true;
    L12:
        if (r6 == 66) goto L18;
        if (majorAxisDistance(r6, r7, r8) < majorAxisDistanceToFarEdge(r6, r7, r9)) goto L17;
        return false;
    L17:
        return true;
    L19:
        return false;
    }

    private static boolean beamsOverlap(int r4, Rect r5, Rect r6) {
        switch(r4) {
            case 17: goto L14;
            case 33: goto L7;
            case 66: goto L14;
            case 130: goto L7;
            default: goto L5;
        };
    L5:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L7:
        if (r6.right < r5.left) goto L12;
        if (r6.left > r5.right) goto L12;
        return true;
    L12:
        return false;
    L14:
        if (r6.bottom < r5.top) goto L19;
        if (r6.top > r5.bottom) goto L19;
        return true;
    L19:
        return false;
    }

    public static <L, T> T findNextFocusInAbsoluteDirection(L r7, CollectionAdapter<L, T> r8, BoundsAdapter<T> r9, T r10, Rect r11, int r12) {
        Rect r0 = new Rect(r11);
        switch(r12) {
            case 17: goto L9;
            case 33: goto L8;
            case 66: goto L7;
            case 130: goto L6;
            default: goto L5;
        };
    L6:
        r0.offset(0, -(r11.height() + 1));
    L10:
        T r1 = null;
        int r2 = r8.size(r7);
        Rect r3 = new Rect();
        int r4 = 0;
    L11:
        if (r4 >= r2) goto L19;
        T r5 = r8.get(r7, r4);
        if (r5 == r10) goto L18;
        r9.obtainBounds(r5, r3);
        if (isBetterCandidate(r12, r11, r3, r0) == false) goto L18;
        r0.set(r3);
        r1 = r5;
    L18:
        r4 = r4 + 1;
        goto L11
    L19:
        return r1;
    L7:
        r0.offset(-(r11.width() + 1), 0);
        goto L10
    L8:
        r0.offset(0, r11.height() + 1);
        goto L10
    L9:
        r0.offset(r11.width() + 1, 0);
        goto L10
    L5:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    public static <L, T> T findNextFocusInRelativeDirection(L r5, CollectionAdapter<L, T> r6, BoundsAdapter<T> r7, T r8, int r9, boolean r10, boolean r11) {
        int r0 = r6.size(r5);
        ArrayList r1 = new ArrayList(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1.add(r6.get(r5, r2));
        r2 = r2 + 1;
        goto L3
    L5:
        Collections.sort(r1, new SequentialComparator(r10, r7));
        switch(r9) {
            case 1: goto L12;
            case 2: goto L10;
            default: goto L8;
        };
    L8:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
    L10:
        return (T) getNextFocusable(r8, r1, r11);
    L12:
        return (T) getPreviousFocusable(r8, r1, r11);
    }

    private static <T> T getNextFocusable(T r3, ArrayList<T> r4, boolean r5) {
        int r0 = r4.size();
        if (r3 != null) goto L5;
        int r1 = -1;
    L6:
        int r2 = r1 + 1;
        if (r2 < r0) goto L9;
        if (r5 == false) goto L14;
        if (r0 > 0) goto L13;
        return null;
    L13:
        return r4.get(0);
    L14:
        return null;
    L9:
        return r4.get(r2);
    L5:
        r1 = r4.lastIndexOf(r3);
        goto L6
    }

    private static <T> T getPreviousFocusable(T r3, ArrayList<T> r4, boolean r5) {
        int r0 = r4.size();
        if (r3 != null) goto L5;
        int r1 = r0;
    L6:
        int r2 = r1 - 1;
        if (r2 >= 0) goto L9;
        if (r5 == false) goto L14;
        if (r0 > 0) goto L13;
        return null;
    L13:
        return r4.get(r0 - 1);
    L14:
        return null;
    L9:
        return r4.get(r2);
    L5:
        r1 = r4.indexOf(r3);
        goto L6
    }

    private static int getWeightedDistanceFor(int r2, int r3) {
        return ((r2 * 13) * r2) + (r3 * r3);
    }

    private static boolean isBetterCandidate(int r5, Rect r6, Rect r7, Rect r8) {
        if (isCandidate(r6, r7, r5) == true) goto L6;
        return false;
    L6:
        if (isCandidate(r6, r8, r5) == true) goto L9;
        return true;
    L9:
        if (beamBeats(r5, r6, r7, r8) == false) goto L12;
        return true;
    L12:
        if (beamBeats(r5, r6, r8, r7) == false) goto L15;
        return false;
    L15:
        if (getWeightedDistanceFor(majorAxisDistance(r5, r6, r7), minorAxisDistance(r5, r6, r7)) < getWeightedDistanceFor(majorAxisDistance(r5, r6, r8), minorAxisDistance(r5, r6, r8))) goto L17;
        return false;
    L17:
        return true;
    }

    private static boolean isCandidate(Rect r4, Rect r5, int r6) {
        switch(r6) {
            case 17: goto L34;
            case 33: goto L25;
            case 66: goto L16;
            case 130: goto L7;
            default: goto L5;
        };
    L5:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L7:
        if (r4.top < r5.top) goto L11;
        if (r4.bottom <= r5.top) goto L11;
    L14:
        return false;
    L11:
        if (r4.bottom >= r5.bottom) goto L14;
        return true;
    L16:
        if (r4.left < r5.left) goto L20;
        if (r4.right <= r5.left) goto L20;
    L23:
        return false;
    L20:
        if (r4.right >= r5.right) goto L23;
        return true;
    L25:
        if (r4.bottom > r5.bottom) goto L29;
        if (r4.top >= r5.bottom) goto L29;
    L32:
        return false;
    L29:
        if (r4.top <= r5.top) goto L32;
        return true;
    L34:
        if (r4.right > r5.right) goto L38;
        if (r4.left >= r5.right) goto L38;
    L41:
        return false;
    L38:
        if (r4.left <= r5.left) goto L41;
        return true;
    }

    private static boolean isToDirectionOf(int r4, Rect r5, Rect r6) {
        switch(r4) {
            case 17: goto L22;
            case 33: goto L17;
            case 66: goto L12;
            case 130: goto L7;
            default: goto L5;
        };
    L5:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L7:
        if (r5.bottom > r6.top) goto L10;
        return true;
    L10:
        return false;
    L12:
        if (r5.right > r6.left) goto L15;
        return true;
    L15:
        return false;
    L17:
        if (r5.top < r6.bottom) goto L20;
        return true;
    L20:
        return false;
    L22:
        if (r5.left < r6.right) goto L25;
        return true;
    L25:
        return false;
    }

    private static int majorAxisDistance(int r2, Rect r3, Rect r4) {
        return Math.max(0, majorAxisDistanceRaw(r2, r3, r4));
    }

    private static int majorAxisDistanceRaw(int r2, Rect r3, Rect r4) {
        switch(r2) {
            case 17: goto L12;
            case 33: goto L10;
            case 66: goto L8;
            case 130: goto L6;
            default: goto L4;
        };
    L4:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L6:
        return r4.top - r3.bottom;
    L8:
        return r4.left - r3.right;
    L10:
        return r3.top - r4.bottom;
    L12:
        return r3.left - r4.right;
    }

    private static int majorAxisDistanceToFarEdge(int r2, Rect r3, Rect r4) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(r2, r3, r4));
    }

    private static int majorAxisDistanceToFarEdgeRaw(int r2, Rect r3, Rect r4) {
        switch(r2) {
            case 17: goto L12;
            case 33: goto L10;
            case 66: goto L8;
            case 130: goto L6;
            default: goto L4;
        };
    L4:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L6:
        return r4.bottom - r3.bottom;
    L8:
        return r4.right - r3.right;
    L10:
        return r3.top - r4.top;
    L12:
        return r3.left - r4.left;
    }

    private static int minorAxisDistance(int r3, Rect r4, Rect r5) {
        switch(r3) {
            case 17: goto L8;
            case 33: goto L6;
            case 66: goto L8;
            case 130: goto L6;
            default: goto L4;
        };
    L4:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    L6:
        return Math.abs((r4.left + (r4.width() / 2)) - (r5.left + (r5.width() / 2)));
    L8:
        return Math.abs((r4.top + (r4.height() / 2)) - (r5.top + (r5.height() / 2)));
    }
}
