package com.google.common.hash;

import com.google.common.base.Supplier;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class LongAddables {
    private static final Supplier<LongAddable> SUPPLIER = null;

    private static final class PureJavaLongAddable extends AtomicLong implements LongAddable {
        private PureJavaLongAddable() {
        }

        /* synthetic */ PureJavaLongAddable(C08331 r1) {
            this();
        }

        @Override // com.google.common.hash.LongAddable
        public void add(long r1) {
            getAndAdd(r1);
        }

        @Override // com.google.common.hash.LongAddable
        public void increment() {
            getAndIncrement();
        }

        @Override // com.google.common.hash.LongAddable
        public long sum() {
            return get();
        }
    }

    static {
        new LongAdder();     // Catch: Throwable -> L4
        Supplier<LongAddable> r1 = new C08331();     // Catch: Throwable -> L4
    L6:
        SUPPLIER = r1;
        return;
    L5:
        r1 = new C08342();
        goto L6
    }

    LongAddables() {
    }

    public static LongAddable create() {
        return SUPPLIER.get();
    }
}
