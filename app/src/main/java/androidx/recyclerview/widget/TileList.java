package androidx.recyclerview.widget;

import android.util.SparseArray;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
class TileList<T> {
    Tile<T> mLastAccessedTile;
    final int mTileSize;
    private final SparseArray<Tile<T>> mTiles;

    public static class Tile<T> {
        public int mItemCount;
        public final T[] mItems;
        Tile<T> mNext;
        public int mStartPosition;

        Tile(Class<T> r2, int r3) {
            this.mItems = (T[]) ((Object[]) Array.newInstance(r2, r3));
        }

        boolean containsPosition(int r3) {
            if (this.mStartPosition <= r3) goto L5;
        L7:
            return false;
        L5:
            if (r3 >= (this.mStartPosition + this.mItemCount)) goto L7;
            return true;
        }

        T getByPosition(int r3) {
            return this.mItems[r3 - this.mStartPosition];
        }
    }

    public TileList(int r3) {
        this.mTiles = new SparseArray(10);
        this.mTileSize = r3;
    }

    public Tile<T> addOrReplace(Tile<T> r4) {
        int r0 = this.mTiles.indexOfKey(r4.mStartPosition);
        if (r0 >= 0) goto L6;
        this.mTiles.put(r4.mStartPosition, r4);
        return null;
    L6:
        Tile<T> r1 = this.mTiles.valueAt(r0);
        this.mTiles.setValueAt(r0, r4);
        if (this.mLastAccessedTile != r1) goto L9;
        this.mLastAccessedTile = r4;
    L9:
        return r1;
    }

    public void clear() {
        this.mTiles.clear();
    }

    public Tile<T> getAtIndex(int r2) {
        if (r2 >= 0) goto L4;
        return null;
    L4:
        if (r2 < this.mTiles.size()) goto L7;
        return null;
    L7:
        return this.mTiles.valueAt(r2);
    }

    public T getItemAt(int r4) {
        if (this.mLastAccessedTile != null) goto L5;
    L6:
        int r0 = r4 - (r4 % this.mTileSize);
        int r1 = this.mTiles.indexOfKey(r0);
        if (r1 >= 0) goto L10;
        return null;
    L10:
        this.mLastAccessedTile = this.mTiles.valueAt(r1);
    L12:
        return this.mLastAccessedTile.getByPosition(r4);
    L5:
        if (this.mLastAccessedTile.containsPosition(r4) == true) goto L12;
        goto L6
    }

    public Tile<T> removeAtPos(int r3) {
        Tile<T> r0 = this.mTiles.get(r3);
        if (this.mLastAccessedTile != r0) goto L5;
        this.mLastAccessedTile = null;
    L5:
        this.mTiles.delete(r3);
        return r0;
    }

    public int size() {
        return this.mTiles.size();
    }
}
