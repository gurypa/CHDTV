package com.google.common.cache;

import com.google.common.base.Preconditions;
import java.util.AbstractMap;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class RemovalNotification<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {
    private static final long serialVersionUID = 0;
    private final RemovalCause cause;

    private RemovalNotification(@CheckForNull K r2, @CheckForNull V r3, RemovalCause r4) {
        super(r2, r3);
        this.cause = (RemovalCause) Preconditions.checkNotNull(r4);
    }

    public static <K, V> RemovalNotification<K, V> create(@CheckForNull K r1, @CheckForNull V r2, RemovalCause r3) {
        return new RemovalNotification(r1, r2, r3);
    }

    public RemovalCause getCause() {
        return this.cause;
    }

    public boolean wasEvicted() {
        return this.cause.wasEvicted();
    }
}
