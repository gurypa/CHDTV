package com.google.common.base;

import java.lang.ref.SoftReference;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference {
    protected FinalizableSoftReference(@CheckForNull T r2, FinalizableReferenceQueue r3) {
        super(r2, r3.queue);
        r3.cleanUp();
    }
}
