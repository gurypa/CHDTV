package androidx.recyclerview.widget;

import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
interface ViewTypeStorage {

    public static class IsolatedViewTypeStorage implements ViewTypeStorage {
        SparseArray<NestedAdapterWrapper> mGlobalTypeToWrapper;
        int mNextViewType;

        class WrapperViewTypeLookup implements ViewTypeLookup {
            private SparseIntArray mGlobalToLocalMapping;
            private SparseIntArray mLocalToGlobalMapping;
            final NestedAdapterWrapper mWrapper;
            final /* synthetic */ IsolatedViewTypeStorage this$0;

            WrapperViewTypeLookup(IsolatedViewTypeStorage r3, NestedAdapterWrapper r4) {
                this.this$0 = r3;
                this.mLocalToGlobalMapping = new SparseIntArray(1);
                this.mGlobalToLocalMapping = new SparseIntArray(1);
                this.mWrapper = r4;
            }

            @Override // androidx.recyclerview.widget.ViewTypeStorage.ViewTypeLookup
            public void dispose() {
                this.this$0.removeWrapper(this.mWrapper);
            }

            @Override // androidx.recyclerview.widget.ViewTypeStorage.ViewTypeLookup
            public int globalToLocal(int r5) {
                int r0 = this.mGlobalToLocalMapping.indexOfKey(r5);
                if (r0 < 0) goto L7;
                return this.mGlobalToLocalMapping.valueAt(r0);
            L7:
                throw new IllegalStateException("requested global type " + r5 + " does not belong to the adapter:" + this.mWrapper.adapter);
            }

            @Override // androidx.recyclerview.widget.ViewTypeStorage.ViewTypeLookup
            public int localToGlobal(int r4) {
                int r0 = this.mLocalToGlobalMapping.indexOfKey(r4);
                if (r0 > (-1)) goto L5;
                int r1 = this.this$0.obtainViewType(this.mWrapper);
                this.mLocalToGlobalMapping.put(r4, r1);
                this.mGlobalToLocalMapping.put(r1, r4);
                return r1;
            L5:
                return this.mLocalToGlobalMapping.valueAt(r0);
            }
        }

        public IsolatedViewTypeStorage() {
            this.mGlobalTypeToWrapper = new SparseArray();
            this.mNextViewType = 0;
        }

        @Override // androidx.recyclerview.widget.ViewTypeStorage
        public ViewTypeLookup createViewTypeWrapper(NestedAdapterWrapper r2) {
            return new WrapperViewTypeLookup(this, r2);
        }

        @Override // androidx.recyclerview.widget.ViewTypeStorage
        public NestedAdapterWrapper getWrapperForGlobalType(int r5) {
            NestedAdapterWrapper r0 = this.mGlobalTypeToWrapper.get(r5);
            if (r0 == null) goto L6;
            return r0;
        L6:
            throw new IllegalArgumentException("Cannot find the wrapper for global view type " + r5);
        }

        int obtainViewType(NestedAdapterWrapper r3) {
            int r0 = this.mNextViewType;
            this.mNextViewType = r0 + 1;
            this.mGlobalTypeToWrapper.put(r0, r3);
            return r0;
        }

        void removeWrapper(NestedAdapterWrapper r4) {
            int r0 = this.mGlobalTypeToWrapper.size() - 1;
        L3:
            if (r0 < 0) goto L8;
            if (this.mGlobalTypeToWrapper.valueAt(r0) != r4) goto L7;
            this.mGlobalTypeToWrapper.removeAt(r0);
        L7:
            r0 = r0 - 1;
            goto L3
        }
    }

    public static class SharedIdRangeViewTypeStorage implements ViewTypeStorage {
        SparseArray<List<NestedAdapterWrapper>> mGlobalTypeToWrapper;

        class WrapperViewTypeLookup implements ViewTypeLookup {
            final NestedAdapterWrapper mWrapper;
            final /* synthetic */ SharedIdRangeViewTypeStorage this$0;

            WrapperViewTypeLookup(SharedIdRangeViewTypeStorage r1, NestedAdapterWrapper r2) {
                this.this$0 = r1;
                this.mWrapper = r2;
            }

            @Override // androidx.recyclerview.widget.ViewTypeStorage.ViewTypeLookup
            public void dispose() {
                this.this$0.removeWrapper(this.mWrapper);
            }

            @Override // androidx.recyclerview.widget.ViewTypeStorage.ViewTypeLookup
            public int globalToLocal(int r1) {
                return r1;
            }

            @Override // androidx.recyclerview.widget.ViewTypeStorage.ViewTypeLookup
            public int localToGlobal(int r3) {
                List<NestedAdapterWrapper> r0 = this.this$0.mGlobalTypeToWrapper.get(r3);
                if (r0 != null) goto L6;
                r0 = new ArrayList();
                this.this$0.mGlobalTypeToWrapper.put(r3, r0);
            L6:
                if (r0.contains(this.mWrapper) == true) goto L8;
                r0.add(this.mWrapper);
            L8:
                return r3;
            }
        }

        public SharedIdRangeViewTypeStorage() {
            this.mGlobalTypeToWrapper = new SparseArray();
        }

        @Override // androidx.recyclerview.widget.ViewTypeStorage
        public ViewTypeLookup createViewTypeWrapper(NestedAdapterWrapper r2) {
            return new WrapperViewTypeLookup(this, r2);
        }

        @Override // androidx.recyclerview.widget.ViewTypeStorage
        public NestedAdapterWrapper getWrapperForGlobalType(int r5) {
            List<NestedAdapterWrapper> r0 = this.mGlobalTypeToWrapper.get(r5);
            if (r0 == null) goto L9;
            if (r0.isEmpty() == true) goto L9;
            return r0.get(0);
        L9:
            throw new IllegalArgumentException("Cannot find the wrapper for global view type " + r5);
        }

        void removeWrapper(NestedAdapterWrapper r4) {
            int r0 = this.mGlobalTypeToWrapper.size() - 1;
        L3:
            if (r0 < 0) goto L10;
            List<NestedAdapterWrapper> r1 = this.mGlobalTypeToWrapper.valueAt(r0);
            if (r1.remove(r4) == false) goto L9;
            if (r1.isEmpty() == false) goto L9;
            this.mGlobalTypeToWrapper.removeAt(r0);
        L9:
            r0 = r0 - 1;
            goto L3
        }
    }

    public interface ViewTypeLookup {
        void dispose();

        int globalToLocal(int r1);

        int localToGlobal(int r1);
    }

    ViewTypeLookup createViewTypeWrapper(NestedAdapterWrapper r1);

    NestedAdapterWrapper getWrapperForGlobalType(int r1);
}
