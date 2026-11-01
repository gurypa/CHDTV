package androidx.core.util;

/* JADX INFO: loaded from: classes.dex */
public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F r1, S r2) {
        this.first = r1;
        this.second = r2;
    }

    public static <A, B> Pair<A, B> create(A r1, B r2) {
        return new Pair(r1, r2);
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof Pair) == true) goto L5;
        return false;
    L5:
        Pair r0 = (Pair) r5;
        if (ObjectsCompat.equals(r0.first, this.first) == true) goto L8;
        return false;
    L8:
        if (ObjectsCompat.equals(r0.second, this.second) == false) goto L12;
        return true;
    L12:
        return false;
    }

    public int hashCode() {
        int r1 = 0;
        if (this.first != null) goto L5;
        int r0 = 0;
    L7:
        if (this.second == null) goto L11;
        r1 = this.second.hashCode();
    L11:
        return r0 ^ r1;
    L5:
        r0 = this.first.hashCode();
        goto L7
    }

    public String toString() {
        return "Pair{" + this.first + " " + this.second + "}";
    }
}
