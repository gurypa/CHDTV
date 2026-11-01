package androidx.media3.common;

import android.util.SparseBooleanArray;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;

/* JADX INFO: loaded from: classes.dex */
public final class FlagSet {
    private final SparseBooleanArray flags;

    /* JADX INFO: renamed from: androidx.media3.common.FlagSet$1 */
    static /* synthetic */ class C01851 {
    }

    public static final class Builder {
        private boolean buildCalled;
        private final SparseBooleanArray flags;

        public Builder() {
            this.flags = new SparseBooleanArray();
        }

        public Builder add(int r3) {
            Assertions.checkState(!this.buildCalled);
            this.flags.append(r3, true);
            return this;
        }

        public Builder addAll(FlagSet r3) {
            int r0 = 0;
        L4:
            if (r0 >= r3.size()) goto L6;
            add(r3.get(r0));
            r0 = r0 + 1;
            goto L4
        L6:
            return this;
        }

        public Builder addAll(int... r4) {
            int r0 = r4.length;
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            add(r4[r1]);
            r1 = r1 + 1;
            goto L3
        L5:
            return this;
        }

        public Builder addIf(int r2, boolean r3) {
            if (r3 == true) goto L4;
            return this;
        L4:
            return add(r2);
        }

        public FlagSet build() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            return new FlagSet(this.flags, null);
        }

        public Builder remove(int r2) {
            Assertions.checkState(!this.buildCalled);
            this.flags.delete(r2);
            return this;
        }

        public Builder removeAll(int... r4) {
            int r0 = r4.length;
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            remove(r4[r1]);
            r1 = r1 + 1;
            goto L3
        L5:
            return this;
        }

        public Builder removeIf(int r2, boolean r3) {
            if (r3 == true) goto L4;
            return this;
        L4:
            return remove(r2);
        }
    }

    private FlagSet(SparseBooleanArray r1) {
        this.flags = r1;
    }

    /* synthetic */ FlagSet(SparseBooleanArray r1, C01851 r2) {
        this(r1);
    }

    public boolean contains(int r2) {
        return this.flags.get(r2);
    }

    public boolean containsAny(int... r6) {
        int r0 = r6.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L9;
        if (contains(r6[r2]) == true) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return true;
    L9:
        return false;
    }

    public boolean equals(Object r7) {
        if (this != r7) goto L6;
        return true;
    L6:
        if ((r7 instanceof FlagSet) == true) goto L8;
        return false;
    L8:
        FlagSet r1 = (FlagSet) r7;
        if (Util.SDK_INT >= 24) goto L22;
        if (size() == r1.size()) goto L13;
        return false;
    L13:
        int r3 = 0;
    L15:
        if (r3 >= size()) goto L20;
        if (get(r3) != r1.get(r3)) goto L18;
        r3 = r3 + 1;
        goto L15
    L18:
        return false;
    L20:
        return true;
    L22:
        return this.flags.equals(r1.flags);
    }

    public int get(int r3) {
        Assertions.checkIndex(r3, 0, size());
        return this.flags.keyAt(r3);
    }

    public int hashCode() {
        if (Util.SDK_INT >= 24) goto L10;
        int r0 = size();
        int r1 = 0;
    L6:
        if (r1 >= size()) goto L8;
        r0 = (r0 * 31) + get(r1);
        r1 = r1 + 1;
        goto L6
    L8:
        return r0;
    L10:
        return this.flags.hashCode();
    }

    public int size() {
        return this.flags.size();
    }
}
