package com.google.common.base;

import java.lang.ref.WeakReference;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class FinalizableWeakReference<T> extends WeakReference<T> implements FinalizableReference {
    protected FinalizableWeakReference(@CheckForNull T r2, FinalizableReferenceQueue r3) {
        super(r2, r3.queue);
        r3.cleanUp();
    }
}
