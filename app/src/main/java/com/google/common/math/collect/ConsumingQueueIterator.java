package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Queue;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class ConsumingQueueIterator<T> extends AbstractIterator<T> {
    private final Queue<T> queue;

    ConsumingQueueIterator(Queue<T> r2) {
        this.queue = (Queue) Preconditions.checkNotNull(r2);
    }

    @Override // com.google.common.collect.AbstractIterator
    @CheckForNull
    protected T computeNext() {
        if (this.queue.isEmpty() == false) goto L7;
        return endOfData();
    L7:
        return this.queue.remove();
    }
}
