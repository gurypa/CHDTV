package com.google.common.base;

import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Set;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use Optional.of(value) or Optional.absent()")
@ElementTypesAreNonnullByDefault
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    Optional() {
    }

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> fromNullable(@CheckForNull T r1) {
        if (r1 != null) goto L5;
        return absent();
    L5:
        return new Present(r1);
    }

    /* JADX INFO: renamed from: of */
    public static <T> Optional<T> m185of(T r2) {
        return new Present(Preconditions.checkNotNull(r2));
    }

    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> r1) {
        Preconditions.checkNotNull(r1);
        return new C05431(r1);
    }

    public abstract Set<T> asSet();

    public abstract boolean equals(@CheckForNull Object r1);

    public abstract T get();

    public abstract int hashCode();

    public abstract boolean isPresent();

    /* JADX INFO: renamed from: or */
    public abstract Optional<T> mo177or(Optional<? extends T> r1);

    /* JADX INFO: renamed from: or */
    public abstract T mo178or(Supplier<? extends T> r1);

    /* JADX INFO: renamed from: or */
    public abstract T mo179or(T r1);

    @CheckForNull
    public abstract T orNull();

    public abstract String toString();

    public abstract <V> Optional<V> transform(Function<? super T, V> r1);
}
