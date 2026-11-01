package androidx.recyclerview.widget;

import androidx.collection.LongSparseArray;

/* JADX INFO: loaded from: classes.dex */
interface StableIdStorage {

    public static class IsolatedStableIdStorage implements StableIdStorage {
        long mNextStableId;

        class WrapperStableIdLookup implements StableIdLookup {
            private final LongSparseArray<Long> mLocalToGlobalLookup;
            final /* synthetic */ IsolatedStableIdStorage this$0;

            WrapperStableIdLookup(IsolatedStableIdStorage r2) {
                this.this$0 = r2;
                this.mLocalToGlobalLookup = new LongSparseArray();
            }

            @Override // androidx.recyclerview.widget.StableIdStorage.StableIdLookup
            public long localToGlobal(long r4) {
                Long r0 = this.mLocalToGlobalLookup.get(r4);
                if (r0 != null) goto L6;
                r0 = Long.valueOf(this.this$0.obtainId());
                this.mLocalToGlobalLookup.put(r4, r0);
            L6:
                return r0.longValue();
            }
        }

        public IsolatedStableIdStorage() {
            this.mNextStableId = 0;
        }

        @Override // androidx.recyclerview.widget.StableIdStorage
        public StableIdLookup createStableIdLookup() {
            return new WrapperStableIdLookup(this);
        }

        long obtainId() {
            long r0 = this.mNextStableId;
            this.mNextStableId = 1 + r0;
            return r0;
        }
    }

    public static class NoStableIdStorage implements StableIdStorage {
        private final StableIdLookup mNoIdLookup;

        public NoStableIdStorage() {
            this.mNoIdLookup = new C05161(this);
        }

        @Override // androidx.recyclerview.widget.StableIdStorage
        public StableIdLookup createStableIdLookup() {
            return this.mNoIdLookup;
        }
    }

    public static class SharedPoolStableIdStorage implements StableIdStorage {
        private final StableIdLookup mSameIdLookup;

        public SharedPoolStableIdStorage() {
            this.mSameIdLookup = new C05171(this);
        }

        @Override // androidx.recyclerview.widget.StableIdStorage
        public StableIdLookup createStableIdLookup() {
            return this.mSameIdLookup;
        }
    }

    public interface StableIdLookup {
        long localToGlobal(long r1);
    }

    StableIdLookup createStableIdLookup();
}
