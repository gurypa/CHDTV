package com.google.common.cache;

import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
interface ReferenceEntry<K, V> {
    long getAccessTime();

    int getHash();

    @CheckForNull
    K getKey();

    @CheckForNull
    ReferenceEntry<K, V> getNext();

    ReferenceEntry<K, V> getNextInAccessQueue();

    ReferenceEntry<K, V> getNextInWriteQueue();

    ReferenceEntry<K, V> getPreviousInAccessQueue();

    ReferenceEntry<K, V> getPreviousInWriteQueue();

    @CheckForNull
    LocalCache.ValueReference<K, V> getValueReference();

    long getWriteTime();

    void setAccessTime(long r1);

    void setNextInAccessQueue(ReferenceEntry<K, V> r1);

    void setNextInWriteQueue(ReferenceEntry<K, V> r1);

    void setPreviousInAccessQueue(ReferenceEntry<K, V> r1);

    void setPreviousInWriteQueue(ReferenceEntry<K, V> r1);

    void setValueReference(LocalCache.ValueReference<K, V> r1);

    void setWriteTime(long r1);
}
