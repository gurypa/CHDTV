package androidx.recyclerview.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public class RecyclerViewAccessibilityDelegate extends AccessibilityDelegateCompat {
    private final ItemDelegate mItemDelegate;
    final RecyclerView mRecyclerView;

    public static class ItemDelegate extends AccessibilityDelegateCompat {
        private Map<View, AccessibilityDelegateCompat> mOriginalItemDelegates;
        final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate;

        public ItemDelegate(RecyclerViewAccessibilityDelegate r2) {
            this.mOriginalItemDelegates = new WeakHashMap();
            this.mRecyclerViewDelegate = r2;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean dispatchPopulateAccessibilityEvent(View r3, AccessibilityEvent r4) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r3);
            if (r0 == null) goto L7;
            return r0.dispatchPopulateAccessibilityEvent(r3, r4);
        L7:
            return super.dispatchPopulateAccessibilityEvent(r3, r4);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View r3) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r3);
            if (r0 == null) goto L7;
            return r0.getAccessibilityNodeProvider(r3);
        L7:
            return super.getAccessibilityNodeProvider(r3);
        }

        AccessibilityDelegateCompat getAndRemoveOriginalDelegateForItem(View r2) {
            return this.mOriginalItemDelegates.remove(r2);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View r2, AccessibilityEvent r3) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r2);
            if (r0 == null) goto L5;
            r0.onInitializeAccessibilityEvent(r2, r3);
            return;
        L5:
            super.onInitializeAccessibilityEvent(r2, r3);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View r2, AccessibilityNodeInfoCompat r3) {
            if (this.mRecyclerViewDelegate.shouldIgnore() == false) goto L5;
        L11:
            super.onInitializeAccessibilityNodeInfo(r2, r3);
            return;
        L5:
            if (this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() == null) goto L11;
            this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfoForItem(r2, r3);
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r2);
            if (r0 == null) goto L9;
            r0.onInitializeAccessibilityNodeInfo(r2, r3);
            return;
        L9:
            super.onInitializeAccessibilityNodeInfo(r2, r3);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View r2, AccessibilityEvent r3) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r2);
            if (r0 == null) goto L5;
            r0.onPopulateAccessibilityEvent(r2, r3);
            return;
        L5:
            super.onPopulateAccessibilityEvent(r2, r3);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup r3, View r4, AccessibilityEvent r5) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r3);
            if (r0 == null) goto L7;
            return r0.onRequestSendAccessibilityEvent(r3, r4, r5);
        L7:
            return super.onRequestSendAccessibilityEvent(r3, r4, r5);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View r4, int r5, Bundle r6) {
            if (this.mRecyclerViewDelegate.shouldIgnore() == true) goto L17;
            if (this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() == null) goto L17;
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r4);
            if (r0 == null) goto L12;
            if (r0.performAccessibilityAction(r4, r5, r6) == false) goto L15;
            return true;
        L15:
            return this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().performAccessibilityActionForItem(r4, r5, r6);
        L12:
            if (super.performAccessibilityAction(r4, r5, r6) == false) goto L15;
            return true;
        L17:
            return super.performAccessibilityAction(r4, r5, r6);
        }

        void saveOriginalDelegate(View r3) {
            AccessibilityDelegateCompat r0 = ViewCompat.getAccessibilityDelegate(r3);
            if (r0 == null) goto L7;
            if (r0 == this) goto L8;
            this.mOriginalItemDelegates.put(r3, r0);
            return;
        L8:
            return;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void sendAccessibilityEvent(View r2, int r3) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r2);
            if (r0 == null) goto L5;
            r0.sendAccessibilityEvent(r2, r3);
            return;
        L5:
            super.sendAccessibilityEvent(r2, r3);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void sendAccessibilityEventUnchecked(View r2, AccessibilityEvent r3) {
            AccessibilityDelegateCompat r0 = this.mOriginalItemDelegates.get(r2);
            if (r0 == null) goto L5;
            r0.sendAccessibilityEventUnchecked(r2, r3);
            return;
        L5:
            super.sendAccessibilityEventUnchecked(r2, r3);
        }
    }

    public RecyclerViewAccessibilityDelegate(RecyclerView r3) {
        this.mRecyclerView = r3;
        AccessibilityDelegateCompat r0 = getItemDelegate();
        if (r0 != null) goto L5;
    L7:
        this.mItemDelegate = new ItemDelegate(this);
        return;
    L5:
        if ((r0 instanceof ItemDelegate) == false) goto L7;
        this.mItemDelegate = (ItemDelegate) r0;
    }

    public AccessibilityDelegateCompat getItemDelegate() {
        return this.mItemDelegate;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityEvent(View r3, AccessibilityEvent r4) {
        super.onInitializeAccessibilityEvent(r3, r4);
        if ((r3 instanceof RecyclerView) == true) goto L5;
        return;
    L5:
        if (shouldIgnore() == true) goto L11;
        RecyclerView r0 = (RecyclerView) r3;
        if (r0.getLayoutManager() == null) goto L12;
        r0.getLayoutManager().onInitializeAccessibilityEvent(r4);
        return;
    L12:
        return;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View r2, AccessibilityNodeInfoCompat r3) {
        super.onInitializeAccessibilityNodeInfo(r2, r3);
        if (shouldIgnore() == false) goto L5;
        return;
    L5:
        if (this.mRecyclerView.getLayoutManager() == null) goto L9;
        this.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(r3);
        return;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public boolean performAccessibilityAction(View r2, int r3, Bundle r4) {
        if (super.performAccessibilityAction(r2, r3, r4) == false) goto L7;
        return true;
    L7:
        if (shouldIgnore() == false) goto L9;
        return false;
    L9:
        if (this.mRecyclerView.getLayoutManager() != null) goto L11;
        return false;
    L11:
        return this.mRecyclerView.getLayoutManager().performAccessibilityAction(r3, r4);
    }

    boolean shouldIgnore() {
        return this.mRecyclerView.hasPendingAdapterUpdates();
    }
}
