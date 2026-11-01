package com.google.common.collect;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class UsingToStringOrdering extends Ordering<Object> implements Serializable {
    static final UsingToStringOrdering INSTANCE = null;
    private static final long serialVersionUID = 0;

    static {
        INSTANCE = new UsingToStringOrdering();
    }

    private UsingToStringOrdering() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(Object r3, Object r4) {
        return r3.toString().compareTo(r4.toString());
    }

    public String toString() {
        return "Ordering.usingToString()";
    }
}
