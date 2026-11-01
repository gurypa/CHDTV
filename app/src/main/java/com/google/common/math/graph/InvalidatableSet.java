package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ForwardingSet;
import java.util.Collection;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class InvalidatableSet<E> extends ForwardingSet<E> {
    private final Set<E> delegate;
    private final Supplier<String> errorMessage;
    private final Supplier<Boolean> validator;

    private InvalidatableSet(Set<E> r1, Supplier<Boolean> r2, Supplier<String> r3) {
        this.delegate = r1;
        this.validator = r2;
        this.errorMessage = r3;
    }

    /* JADX INFO: renamed from: of */
    public static final <E> InvalidatableSet<E> m336of(Set<E> r4, Supplier<Boolean> r5, Supplier<String> r6) {
        return new InvalidatableSet((Set) Preconditions.checkNotNull(r4), (Supplier) Preconditions.checkNotNull(r5), (Supplier) Preconditions.checkNotNull(r6));
    }

    private void validate() {
        if (this.validator.get().booleanValue() == false) goto L6;
        return;
    L6:
        throw new IllegalStateException(this.errorMessage.get());
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Object delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected /* bridge */ /* synthetic */ Collection delegate() {
        return delegate();
    }

    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected Set<E> delegate() {
        validate();
        return this.delegate;
    }

    @Override // com.google.common.collect.ForwardingSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return this.delegate.hashCode();
    }
}
