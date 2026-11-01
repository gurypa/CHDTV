package com.google.common.cache;

import com.google.common.base.Supplier;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class LongAddables {
    private static final Supplier<LongAddable> SUPPLIER = null;

    private static final class PureJavaLongAddable extends AtomicLong implements LongAddable {
        private PureJavaLongAddable() {
        }

        /* synthetic */ PureJavaLongAddable(C05821 r1) {
            this();
        }

        @Override // com.google.common.cache.LongAddable
        public void add(long r1) {
            getAndAdd(r1);
        }

        @Override // com.google.common.cache.LongAddable
        public void increment() {
            getAndIncrement();
        }

        @Override // com.google.common.cache.LongAddable
        public long sum() {
            return get();
        }
    }

    static {
        new LongAdder();     // Catch: Throwable -> L4
        Supplier<LongAddable> r1 = new C05821();     // Catch: Throwable -> L4
    L6:
        SUPPLIER = r1;
        return;
    L5:
        r1 = new C05832();
        goto L6
    }

    LongAddables() {
    }

    public static LongAddable create() {
        return SUPPLIER.get();
    }
}
