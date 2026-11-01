package androidx.core.util;

/* JADX INFO: loaded from: classes.dex */
public final class Pools {

    public interface Pool<T> {
        T acquire();

        boolean release(T r1);
    }

    public static class SimplePool<T> implements Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        public SimplePool(int r3) {
            if (r3 <= 0) goto L7;
            this.mPool = new Object[r3];
            return;
        L7:
            throw new IllegalArgumentException("The max pool size must be > 0");
        }

        private boolean isInPool(T r3) {
            int r0 = 0;
        L4:
            if (r0 >= this.mPoolSize) goto L10;
            if (this.mPool[r0] == r3) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return true;
        L10:
            return false;
        }

        @Override // androidx.core.util.Pools.Pool
        public T acquire() {
            if (this.mPoolSize <= 0) goto L6;
            int r0 = this.mPoolSize - 1;
            T r2 = (T) this.mPool[r0];
            this.mPool[r0] = null;
            this.mPoolSize--;
            return r2;
        L6:
            return null;
        }

        @Override // androidx.core.util.Pools.Pool
        public boolean release(T r3) {
            if (isInPool(r3) == true) goto L11;
            if (this.mPoolSize >= this.mPool.length) goto L8;
            this.mPool[this.mPoolSize] = r3;
            this.mPoolSize++;
            return true;
        L8:
            return false;
        L11:
            throw new IllegalStateException("Already in the pool!");
        }
    }

    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object mLock;

        public SynchronizedPool(int r2) {
            super(r2);
            this.mLock = new Object();
        }

        @Override // androidx.core.util.Pools.SimplePool, androidx.core.util.Pools.Pool
        public T acquire() {
            Object r0 = this.mLock;
            monitor-enter(r0);
            T r1 = (T) super.acquire();
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }

        @Override // androidx.core.util.Pools.SimplePool, androidx.core.util.Pools.Pool
        public boolean release(T r3) {
            Object r0 = this.mLock;
            monitor-enter(r0);
            boolean r1 = super.release(r3);     // Catch: Throwable -> L7
            monitor-exit(r0);     // Catch: Throwable -> L7
            return r1;
        L7:
            th = move-exception;
            throw th;
        }
    }

    private Pools() {
    }
}
