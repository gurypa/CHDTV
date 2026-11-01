package com.google.common.base;

import java.lang.ref.PhantomReference;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class FinalizablePhantomReference<T> extends PhantomReference<T> implements FinalizableReference {
    protected FinalizablePhantomReference(@CheckForNull T r2, FinalizableReferenceQueue r3) {
        super(r2, r3.queue);
        r3.cleanUp();
    }
}
