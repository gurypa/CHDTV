package com.google.common.collect;

import java.util.Collection;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
class EmptyImmutableSetMultimap extends ImmutableSetMultimap<Object, Object> {
    static final EmptyImmutableSetMultimap INSTANCE = null;
    private static final long serialVersionUID = 0;

    static {
        INSTANCE = new EmptyImmutableSetMultimap();
    }

    private EmptyImmutableSetMultimap() {
        super(ImmutableMap.m231of(), 0, null);
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public ImmutableMap<Object, Collection<Object>> asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.ImmutableMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Map asMap() {
        return asMap();
    }
}
