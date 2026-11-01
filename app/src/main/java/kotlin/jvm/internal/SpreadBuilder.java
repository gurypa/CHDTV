package kotlin.jvm.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class SpreadBuilder {
    private final ArrayList<Object> list;

    public SpreadBuilder(int r2) {
        this.list = new ArrayList(r2);
    }

    public void add(Object r2) {
        this.list.add(r2);
    }

    public void addSpread(Object r5) {
        if (r5 != null) goto L5;
        return;
    L5:
        if ((r5 instanceof Object[]) == false) goto L11;
        Object[] r0 = (Object[]) r5;
        if (r0.length <= 0) goto L33;
        this.list.ensureCapacity(this.list.size() + r0.length);
        Collections.addAll(this.list, r0);
        return;
    L33:
        return;
    L11:
        if ((r5 instanceof Collection) == false) goto L14;
        this.list.addAll((Collection) r5);
        return;
    L14:
        if ((r5 instanceof Iterable) == false) goto L21;
        Iterator r1 = ((Iterable) r5).iterator();
    L17:
        if (r1.hasNext() == false) goto L34;
        this.list.add(r1.next());
        goto L17
    L34:
        return;
    L21:
        if ((r5 instanceof Iterator) == false) goto L29;
        Iterator r2 = (Iterator) r5;
    L24:
        if (r2.hasNext() == false) goto L35;
        this.list.add(r2.next());
        goto L24
    L35:
        return;
    L29:
        throw new UnsupportedOperationException("Don't know how to spread " + r5.getClass());
    }

    public int size() {
        return this.list.size();
    }

    public Object[] toArray(Object[] r2) {
        return this.list.toArray(r2);
    }
}
