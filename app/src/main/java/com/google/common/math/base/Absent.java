package com.google.common.base;

import java.util.Collections;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class Absent<T> extends Optional<T> {
    static final Absent<Object> INSTANCE = null;
    private static final long serialVersionUID = 0;

    static {
        INSTANCE = new Absent();
    }

    private Absent() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    static <T> Optional<T> withType() {
        return INSTANCE;
    }

    @Override // com.google.common.base.Optional
    public Set<T> asSet() {
        return Collections.emptySet();
    }

    @Override // com.google.common.base.Optional
    public boolean equals(@CheckForNull Object r2) {
        if (r2 != this) goto L4;
        return true;
    L4:
        return false;
    }

    @Override // com.google.common.base.Optional
    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    @Override // com.google.common.base.Optional
    public int hashCode() {
        return 2040732332;
    }

    @Override // com.google.common.base.Optional
    public boolean isPresent() {
        return false;
    }

    @Override // com.google.common.base.Optional
    /* JADX INFO: renamed from: or */
    public Optional<T> mo177or(Optional<? extends T> r2) {
        return (Optional) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.base.Optional
    /* JADX INFO: renamed from: or */
    public T mo178or(Supplier<? extends T> r3) {
        return (T) Preconditions.checkNotNull(r3.get(), "use Optional.orNull() instead of a Supplier that returns null");
    }

    @Override // com.google.common.base.Optional
    /* JADX INFO: renamed from: or */
    public T mo179or(T r2) {
        return (T) Preconditions.checkNotNull(r2, "use Optional.orNull() instead of Optional.or(null)");
    }

    @Override // com.google.common.base.Optional
    @CheckForNull
    public T orNull() {
        return null;
    }

    @Override // com.google.common.base.Optional
    public String toString() {
        return "Optional.absent()";
    }

    @Override // com.google.common.base.Optional
    public <V> Optional<V> transform(Function<? super T, V> r2) {
        Preconditions.checkNotNull(r2);
        return Optional.absent();
    }
}
