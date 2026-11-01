package androidx.recyclerview.widget;

import android.R;
import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.customview.poolingcontainer.PoolingContainer;
import androidx.customview.view.AbsSavedState;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.recyclerview.C0472R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2, NestedScrollingChild3 {
    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC = false;
    static final boolean ALLOW_THREAD_GAP_WORK = false;
    static final boolean DEBUG = false;
    private static final float DECELERATION_RATE = 0.0f;
    static final int DEFAULT_ORIENTATION = 1;
    static final boolean DISPATCH_TEMP_DETACH = false;
    private static final float FLING_DESTRETCH_FACTOR = 4.0f;
    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION = false;
    static final boolean FORCE_INVALIDATE_DISPLAY_LIST = false;
    static final long FOREVER_NS = Long.MAX_VALUE;
    public static final int HORIZONTAL = 0;
    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD = false;
    private static final float INFLEXION = 0.35f;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = null;
    static final int MAX_SCROLL_DURATION = 2000;
    private static final int[] NESTED_SCROLLING_ATTRS = null;
    public static final long NO_ID = -1;
    public static final int NO_POSITION = -1;
    static final boolean POST_UPDATES_ON_ANIMATION = false;
    private static final float SCROLL_FRICTION = 0.015f;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    static final String TRACE_PREFETCH_TAG = "RV Prefetch";
    static final String TRACE_SCROLL_TAG = "RV Scroll";
    public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
    static final boolean VERBOSE_TRACING = false;
    public static final int VERTICAL = 1;
    static final StretchEdgeEffectFactory sDefaultEdgeEffectFactory = null;
    static final Interpolator sQuinticInterpolator = null;
    RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    boolean mAdapterUpdateDuringMeasure;
    private EdgeEffect mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    boolean mClipToPadding;
    boolean mDataSetHasChangedAfterLayout;
    boolean mDispatchItemsChangedEvent;
    private int mDispatchScrollCounter;
    private int mEatenAccessibilityChangeFlags;
    private EdgeEffectFactory mEdgeEffectFactory;
    boolean mEnableFastScroller;
    boolean mFirstLayoutComplete;
    GapWorker mGapWorker;
    boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mInterceptRequestLayoutDepth;
    private OnItemTouchListener mInterceptingOnItemTouchListener;
    boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastAutoMeasureNonExactMeasuredHeight;
    private int mLastAutoMeasureNonExactMeasuredWidth;
    private boolean mLastAutoMeasureSkippedDueToExact;
    private int mLastTouchX;
    private int mLastTouchY;
    LayoutManager mLayout;
    private int mLayoutOrScrollCounter;
    boolean mLayoutSuppressed;
    boolean mLayoutWasDefered;
    private EdgeEffect mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private OnFlingListener mOnFlingListener;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    final List<ViewHolder> mPendingAccessibilityImportanceChange;
    SavedState mPendingSavedState;
    private final float mPhysicalCoef;
    boolean mPostedAnimatorRunner;
    GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
    private boolean mPreserveFocusAfterLayout;
    final Recycler mRecycler;
    RecyclerListener mRecyclerListener;
    final List<RecyclerListener> mRecyclerListeners;
    final int[] mReusableIntPair;
    private EdgeEffect mRightGlow;
    private float mScaledHorizontalScrollFactor;
    private float mScaledVerticalScrollFactor;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    final Rect mTempRect;
    private final Rect mTempRect2;
    final RectF mTempRectF;
    private EdgeEffect mTopGlow;
    private int mTouchSlop;
    final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;

    /* JADX INFO: renamed from: androidx.recyclerview.widget.RecyclerView$7 */
    static /* synthetic */ class C05107 {

        /* JADX INFO: renamed from: $SwitchMap$androidx$recyclerview$widget$RecyclerView$Adapter$StateRestorationPolicy */
        static final /* synthetic */ int[] f74x3d106619 = null;

        static {
            f74x3d106619 = new int[Adapter.StateRestorationPolicy.values().length];
            f74x3d106619[Adapter.StateRestorationPolicy.PREVENT.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L12:
            f74x3d106619[Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
            return;
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private boolean mHasStableIds;
        private final AdapterDataObservable mObservable;
        private StateRestorationPolicy mStateRestorationPolicy;

        public static final enum StateRestorationPolicy extends Enum<StateRestorationPolicy> {
            private static final /* synthetic */ StateRestorationPolicy[] $VALUES = null;
            public static final StateRestorationPolicy ALLOW = null;
            public static final StateRestorationPolicy PREVENT = null;
            public static final StateRestorationPolicy PREVENT_WHEN_EMPTY = null;

            static {
                ALLOW = new StateRestorationPolicy("ALLOW", 0);
                PREVENT_WHEN_EMPTY = new StateRestorationPolicy("PREVENT_WHEN_EMPTY", 1);
                PREVENT = new StateRestorationPolicy("PREVENT", 2);
                $VALUES = new StateRestorationPolicy[]{ALLOW, PREVENT_WHEN_EMPTY, PREVENT};
            }

            private StateRestorationPolicy(String r1, int r2) {
                super(r1, r2);
            }

            public static StateRestorationPolicy valueOf(String r1) {
                return (StateRestorationPolicy) Enum.valueOf(StateRestorationPolicy.class, r1);
            }

            public static StateRestorationPolicy[] values() {
                return (StateRestorationPolicy[]) $VALUES.clone();
            }
        }

        public Adapter() {
            this.mObservable = new AdapterDataObservable();
            this.mHasStableIds = false;
            this.mStateRestorationPolicy = StateRestorationPolicy.ALLOW;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final void bindViewHolder(VH r5, int r6) {
            if (r5.mBindingAdapter != null) goto L5;
            boolean r0 = true;
        L6:
            if (r0 == false) goto L11;
            r5.mPosition = r6;
            if (hasStableIds() == false) goto L10;
            r5.mItemId = getItemId(r6);
        L10:
            r5.setFlags(1, 519);
            TraceCompat.beginSection(RecyclerView.TRACE_BIND_VIEW_TAG);
        L11:
            r5.mBindingAdapter = this;
            onBindViewHolder(r5, r6, r5.getUnmodifiedPayloads());
            if (r0 == false) goto L18;
            r5.clearPayload();
            ViewGroup.LayoutParams r2 = r5.itemView.getLayoutParams();
            if ((r2 instanceof LayoutParams) == false) goto L16;
            ((LayoutParams) r2).mInsetsDirty = true;
        L16:
            TraceCompat.endSection();
            return;
        L18:
            return;
        L5:
            r0 = false;
            goto L6
        }

        boolean canRestoreState() {
            switch(C05107.f74x3d106619[this.mStateRestorationPolicy.ordinal()]) {
                case 1: goto L10;
                case 2: goto L6;
                default: goto L4;
            };
        L4:
            return true;
        L10:
            return false;
        L6:
            if (getItemCount() <= 0) goto L9;
            return true;
        L9:
            return false;
        }

        public final VH createViewHolder(ViewGroup r4, int r5) {
            TraceCompat.beginSection(RecyclerView.TRACE_CREATE_VIEW_TAG);     // Catch: Throwable -> L9
            VH r0 = (VH) onCreateViewHolder(r4, r5);
            if (r0.itemView.getParent() != null) goto L8;
            r0.mItemViewType = r5;     // Catch: Throwable -> L9
            TraceCompat.endSection();
            return r0;
        L8:
            throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");     // Catch: Throwable -> L9
        L9:
            th = move-exception;
            TraceCompat.endSection();
            throw th;
        }

        public int findRelativeAdapterPositionIn(Adapter<? extends ViewHolder> r2, ViewHolder r3, int r4) {
            if (r2 != this) goto L4;
            return r4;
        L4:
            return -1;
        }

        public abstract int getItemCount();

        public long getItemId(int r3) {
            return -1;
        }

        public int getItemViewType(int r2) {
            return 0;
        }

        public final StateRestorationPolicy getStateRestorationPolicy() {
            return this.mStateRestorationPolicy;
        }

        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int r3) {
            this.mObservable.notifyItemRangeChanged(r3, 1);
        }

        public final void notifyItemChanged(int r3, Object r4) {
            this.mObservable.notifyItemRangeChanged(r3, 1, r4);
        }

        public final void notifyItemInserted(int r3) {
            this.mObservable.notifyItemRangeInserted(r3, 1);
        }

        public final void notifyItemMoved(int r2, int r3) {
            this.mObservable.notifyItemMoved(r2, r3);
        }

        public final void notifyItemRangeChanged(int r2, int r3) {
            this.mObservable.notifyItemRangeChanged(r2, r3);
        }

        public final void notifyItemRangeChanged(int r2, int r3, Object r4) {
            this.mObservable.notifyItemRangeChanged(r2, r3, r4);
        }

        public final void notifyItemRangeInserted(int r2, int r3) {
            this.mObservable.notifyItemRangeInserted(r2, r3);
        }

        public final void notifyItemRangeRemoved(int r2, int r3) {
            this.mObservable.notifyItemRangeRemoved(r2, r3);
        }

        public final void notifyItemRemoved(int r3) {
            this.mObservable.notifyItemRangeRemoved(r3, 1);
        }

        public void onAttachedToRecyclerView(RecyclerView r1) {
        }

        public abstract void onBindViewHolder(VH r1, int r2);

        public void onBindViewHolder(VH r1, int r2, List<Object> r3) {
            onBindViewHolder(r1, r2);
        }

        public abstract VH onCreateViewHolder(ViewGroup r1, int r2);

        public void onDetachedFromRecyclerView(RecyclerView r1) {
        }

        public boolean onFailedToRecycleView(VH r2) {
            return false;
        }

        public void onViewAttachedToWindow(VH r1) {
        }

        public void onViewDetachedFromWindow(VH r1) {
        }

        public void onViewRecycled(VH r1) {
        }

        public void registerAdapterDataObserver(AdapterDataObserver r2) {
            this.mObservable.registerObserver(r2);
        }

        public void setHasStableIds(boolean r3) {
            if (hasObservers() == true) goto L7;
            this.mHasStableIds = r3;
            return;
        L7:
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
        }

        public void setStateRestorationPolicy(StateRestorationPolicy r2) {
            this.mStateRestorationPolicy = r2;
            this.mObservable.notifyStateRestorationPolicyChanged();
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver r2) {
            this.mObservable.unregisterObserver(r2);
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            return !this.mObservers.isEmpty();
        }

        public void notifyChanged() {
            int r0 = this.mObservers.size() - 1;
        L3:
            if (r0 < 0) goto L5;
            ((AdapterDataObserver) this.mObservers.get(r0)).onChanged();
            r0 = r0 - 1;
            goto L3
        }

        public void notifyItemMoved(int r4, int r5) {
            int r0 = this.mObservers.size() - 1;
        L3:
            if (r0 < 0) goto L5;
            ((AdapterDataObserver) this.mObservers.get(r0)).onItemRangeMoved(r4, r5, 1);
            r0 = r0 - 1;
            goto L3
        }

        public void notifyItemRangeChanged(int r2, int r3) {
            notifyItemRangeChanged(r2, r3, null);
        }

        public void notifyItemRangeChanged(int r3, int r4, Object r5) {
            int r0 = this.mObservers.size() - 1;
        L3:
            if (r0 < 0) goto L5;
            ((AdapterDataObserver) this.mObservers.get(r0)).onItemRangeChanged(r3, r4, r5);
            r0 = r0 - 1;
            goto L3
        }

        public void notifyItemRangeInserted(int r3, int r4) {
            int r0 = this.mObservers.size() - 1;
        L3:
            if (r0 < 0) goto L5;
            ((AdapterDataObserver) this.mObservers.get(r0)).onItemRangeInserted(r3, r4);
            r0 = r0 - 1;
            goto L3
        }

        public void notifyItemRangeRemoved(int r3, int r4) {
            int r0 = this.mObservers.size() - 1;
        L3:
            if (r0 < 0) goto L5;
            ((AdapterDataObserver) this.mObservers.get(r0)).onItemRangeRemoved(r3, r4);
            r0 = r0 - 1;
            goto L3
        }

        public void notifyStateRestorationPolicyChanged() {
            int r0 = this.mObservers.size() - 1;
        L3:
            if (r0 < 0) goto L5;
            ((AdapterDataObserver) this.mObservers.get(r0)).onStateRestorationPolicyChanged();
            r0 = r0 - 1;
            goto L3
        }
    }

    public static abstract class AdapterDataObserver {
        public AdapterDataObserver() {
        }

        public void onChanged() {
        }

        public void onItemRangeChanged(int r1, int r2) {
        }

        public void onItemRangeChanged(int r1, int r2, Object r3) {
            onItemRangeChanged(r1, r2);
        }

        public void onItemRangeInserted(int r1, int r2) {
        }

        public void onItemRangeMoved(int r1, int r2, int r3) {
        }

        public void onItemRangeRemoved(int r1, int r2) {
        }

        public void onStateRestorationPolicyChanged() {
        }
    }

    public interface ChildDrawingOrderCallback {
        int onGetChildDrawingOrder(int r1, int r2);
    }

    public static class EdgeEffectFactory {
        public static final int DIRECTION_BOTTOM = 3;
        public static final int DIRECTION_LEFT = 0;
        public static final int DIRECTION_RIGHT = 2;
        public static final int DIRECTION_TOP = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface EdgeDirection {
        }

        public EdgeEffectFactory() {
        }

        protected EdgeEffect createEdgeEffect(RecyclerView r3, int r4) {
            return new EdgeEffect(r3.getContext());
        }
    }

    public static abstract class ItemAnimator {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration;
        private long mChangeDuration;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners;
        private ItemAnimatorListener mListener;
        private long mMoveDuration;
        private long mRemoveDuration;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges {
        }

        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        interface ItemAnimatorListener {
            void onAnimationFinished(ViewHolder r1);
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo() {
            }

            public ItemHolderInfo setFrom(ViewHolder r2) {
                return setFrom(r2, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder r3, int r4) {
                View r0 = r3.itemView;
                this.left = r0.getLeft();
                this.top = r0.getTop();
                this.right = r0.getRight();
                this.bottom = r0.getBottom();
                return this;
            }
        }

        public ItemAnimator() {
            this.mListener = null;
            this.mFinishedListeners = new ArrayList();
            this.mAddDuration = 120;
            this.mRemoveDuration = 120;
            this.mMoveDuration = 250;
            this.mChangeDuration = 250;
        }

        static int buildAdapterChangeFlagsForAnimations(ViewHolder r4) {
            int r0 = r4.mFlags & 14;
            if (r4.isInvalid() == false) goto L7;
            return 4;
        L7:
            if ((r0 & 4) != 0) goto L14;
            int r1 = r4.getOldPosition();
            int r2 = r4.getAbsoluteAdapterPosition();
            if (r1 == (-1)) goto L15;
            if (r2 == (-1)) goto L16;
            if (r1 != r2) goto L13;
            return r0;
        L13:
            return r0 | 2048;
        L16:
            return r0;
        L15:
            return r0;
        L14:
            return r0;
        }

        public abstract boolean animateAppearance(ViewHolder r1, ItemHolderInfo r2, ItemHolderInfo r3);

        public abstract boolean animateChange(ViewHolder r1, ViewHolder r2, ItemHolderInfo r3, ItemHolderInfo r4);

        public abstract boolean animateDisappearance(ViewHolder r1, ItemHolderInfo r2, ItemHolderInfo r3);

        public abstract boolean animatePersistence(ViewHolder r1, ItemHolderInfo r2, ItemHolderInfo r3);

        public boolean canReuseUpdatedViewHolder(ViewHolder r2) {
            return true;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder r2, List<Object> r3) {
            return canReuseUpdatedViewHolder(r2);
        }

        public final void dispatchAnimationFinished(ViewHolder r2) {
            onAnimationFinished(r2);
            if (this.mListener == null) goto L6;
            this.mListener.onAnimationFinished(r2);
            return;
        }

        public final void dispatchAnimationStarted(ViewHolder r1) {
            onAnimationStarted(r1);
        }

        public final void dispatchAnimationsFinished() {
            int r0 = this.mFinishedListeners.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            this.mFinishedListeners.get(r1).onAnimationsFinished();
            r1 = r1 + 1;
            goto L3
        L5:
            this.mFinishedListeners.clear();
        }

        public abstract void endAnimation(ViewHolder r1);

        public abstract void endAnimations();

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public abstract boolean isRunning();

        public final boolean isRunning(ItemAnimatorFinishedListener r3) {
            boolean r0 = isRunning();
            if (r3 == null) goto L7;
            if (r0 == true) goto L6;
            r3.onAnimationsFinished();
            goto L7
        L6:
            this.mFinishedListeners.add(r3);
        L7:
            return r0;
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }

        public void onAnimationFinished(ViewHolder r1) {
        }

        public void onAnimationStarted(ViewHolder r1) {
        }

        public ItemHolderInfo recordPostLayoutInformation(State r2, ViewHolder r3) {
            return obtainHolderInfo().setFrom(r3);
        }

        public ItemHolderInfo recordPreLayoutInformation(State r2, ViewHolder r3, int r4, List<Object> r5) {
            return obtainHolderInfo().setFrom(r3);
        }

        public abstract void runPendingAnimations();

        public void setAddDuration(long r1) {
            this.mAddDuration = r1;
        }

        public void setChangeDuration(long r1) {
            this.mChangeDuration = r1;
        }

        void setListener(ItemAnimatorListener r1) {
            this.mListener = r1;
        }

        public void setMoveDuration(long r1) {
            this.mMoveDuration = r1;
        }

        public void setRemoveDuration(long r1) {
            this.mRemoveDuration = r1;
        }
    }

    private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
        final /* synthetic */ RecyclerView this$0;

        ItemAnimatorRestoreListener(RecyclerView r1) {
            this.this$0 = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemAnimatorListener
        public void onAnimationFinished(ViewHolder r4) {
            r4.setIsRecyclable(true);
            if (r4.mShadowedHolder != null) goto L5;
        L7:
            r4.mShadowingHolder = null;
            if (r4.shouldBeKeptAsChild() == false) goto L10;
            return;
        L10:
            if (this.this$0.removeAnimatingView(r4.itemView) == false) goto L12;
            return;
        L12:
            if (r4.isTmpDetached() == false) goto L17;
            this.this$0.removeDetachedView(r4.itemView, false);
            return;
        L17:
            return;
        L5:
            if (r4.mShadowingHolder != null) goto L7;
            r4.mShadowedHolder = null;
            goto L7
        }
    }

    public static abstract class ItemDecoration {
        public ItemDecoration() {
        }

        @Deprecated
        public void getItemOffsets(Rect r2, int r3, RecyclerView r4) {
            r2.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect r2, View r3, RecyclerView r4, State r5) {
            getItemOffsets(r2, ((LayoutParams) r3.getLayoutParams()).getViewLayoutPosition(), r4);
        }

        @Deprecated
        public void onDraw(Canvas r1, RecyclerView r2) {
        }

        public void onDraw(Canvas r1, RecyclerView r2, State r3) {
            onDraw(r1, r2);
        }

        @Deprecated
        public void onDrawOver(Canvas r1, RecyclerView r2) {
        }

        public void onDrawOver(Canvas r1, RecyclerView r2, State r3) {
            onDrawOver(r1, r2);
        }
    }

    public static abstract class LayoutManager {
        boolean mAutoMeasure;
        ChildHelper mChildHelper;
        private int mHeight;
        private int mHeightMode;
        ViewBoundsCheck mHorizontalBoundCheck;
        private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback;
        boolean mIsAttachedToWindow;
        private boolean mItemPrefetchEnabled;
        private boolean mMeasurementCacheEnabled;
        int mPrefetchMaxCountObserved;
        boolean mPrefetchMaxObservedInInitialPrefetch;
        RecyclerView mRecyclerView;
        boolean mRequestedSimpleAnimations;
        SmoothScroller mSmoothScroller;
        ViewBoundsCheck mVerticalBoundCheck;
        private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback;
        private int mWidth;
        private int mWidthMode;

        public interface LayoutPrefetchRegistry {
            void addPosition(int r1, int r2);
        }

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;

            public Properties() {
            }
        }

        public LayoutManager() {
            this.mHorizontalBoundCheckCallback = new C05111(this);
            this.mVerticalBoundCheckCallback = new C05122(this);
            this.mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
            this.mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
            this.mRequestedSimpleAnimations = false;
            this.mIsAttachedToWindow = false;
            this.mAutoMeasure = false;
            this.mMeasurementCacheEnabled = true;
            this.mItemPrefetchEnabled = true;
        }

        private void addViewInt(View r7, int r8, boolean r9) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r7);
            if (r9 == false) goto L5;
        L8:
            this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(r0);
        L9:
            LayoutParams r1 = (LayoutParams) r7.getLayoutParams();
            if (r0.wasReturnedFromScrap() == true) goto L31;
            if (r0.isScrap() == true) goto L31;
            if (r7.getParent() != this.mRecyclerView) goto L25;
            int r2 = this.mChildHelper.indexOfChild(r7);
            if (r8 != (-1)) goto L19;
            r8 = this.mChildHelper.getChildCount();
        L19:
            if (r2 == (-1)) goto L24;
            if (r2 == r8) goto L36;
            this.mRecyclerView.mLayout.moveView(r2, r8);
        L36:
            if (r1.mPendingInvalidate == false) goto L39;
            r0.itemView.invalidate();
            r1.mPendingInvalidate = false;
            return;
        L39:
            return;
        L24:
            throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(r7) + this.mRecyclerView.exceptionLabel());
        L25:
            this.mChildHelper.addView(r7, r8, false);
            r1.mInsetsDirty = true;
            if (this.mSmoothScroller == null) goto L36;
            if (this.mSmoothScroller.isRunning() == false) goto L36;
            this.mSmoothScroller.onChildAttachedToWindow(r7);
        L31:
            if (r0.isScrap() == false) goto L33;
            r0.unScrap();
        L34:
            this.mChildHelper.attachViewToParent(r7, r8, r7.getLayoutParams(), false);
            goto L36
        L33:
            r0.clearReturnedFromScrapFlag();
            goto L34
        L5:
            if (r0.isRemoved() == true) goto L8;
            this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(r0);
            goto L9
        }

        public static int chooseSize(int r3, int r4, int r5) {
            int r0 = View.MeasureSpec.getMode(r3);
            int r1 = View.MeasureSpec.getSize(r3);
            switch(r0) {
                case -2147483648: goto L8;
                case 1073741824: goto L6;
                default: goto L5;
            };
        L6:
            return r1;
        L5:
            return Math.max(r4, r5);
        L8:
            return Math.min(r1, Math.max(r4, r5));
        }

        private void detachViewInternal(int r2, View r3) {
            this.mChildHelper.detachViewFromParent(r2);
        }

        public static int getChildMeasureSpec(int r5, int r6, int r7, int r8, boolean r9) {
            int r0 = Math.max(0, r5 - r7);
            int r1 = 0;
            int r2 = 0;
            if (r9 == false) goto L14;
            if (r8 < 0) goto L6;
            r1 = r8;
            r2 = 1073741824;
        L27:
            return View.MeasureSpec.makeMeasureSpec(r1, r2);
        L6:
            if (r8 != (-1)) goto L12;
            switch(r6) {
                case -2147483648: goto L10;
                case 0: goto L9;
                case 1073741824: goto L10;
                default: goto L27;
            };
        L9:
            r1 = 0;
            r2 = 0;
            goto L27
        L10:
            r1 = r0;
            r2 = r6;
            goto L27
        L12:
            if (r8 != (-2)) goto L27;
            r1 = 0;
            r2 = 0;
            goto L27
        L14:
            if (r8 < 0) goto L16;
            r1 = r8;
            r2 = 1073741824;
            goto L27
        L16:
            if (r8 != (-1)) goto L18;
            r1 = r0;
            r2 = r6;
            goto L27
        L18:
            if (r8 != (-2)) goto L27;
            r1 = r0;
            if (r6 != Integer.MIN_VALUE) goto L22;
        L25:
            r2 = Integer.MIN_VALUE;
            goto L27
        L22:
            if (r6 == 1073741824) goto L25;
            r2 = 0;
            goto L27
        }

        @Deprecated
        public static int getChildMeasureSpec(int r4, int r5, int r6, boolean r7) {
            int r0 = Math.max(0, r4 - r5);
            int r1 = 0;
            int r2 = 0;
            if (r7 == false) goto L7;
            if (r6 < 0) goto L6;
            r1 = r6;
            r2 = 1073741824;
        L16:
            return View.MeasureSpec.makeMeasureSpec(r1, r2);
        L6:
            r1 = 0;
            r2 = 0;
            goto L16
        L7:
            if (r6 < 0) goto L10;
            r1 = r6;
            r2 = 1073741824;
            goto L16
        L10:
            if (r6 != (-1)) goto L13;
            r1 = r0;
            r2 = 1073741824;
            goto L16
        L13:
            if (r6 != (-2)) goto L16;
            r1 = r0;
            r2 = Integer.MIN_VALUE;
            goto L16
        }

        private int[] getChildRectangleOnScreenScrollAmount(View r19, Rect r20) {
            int[] r1 = new int[2];
            int r2 = getPaddingLeft();
            int r3 = getPaddingTop();
            int r4 = getWidth() - getPaddingRight();
            int r5 = getHeight() - getPaddingBottom();
            int r6 = (r19.getLeft() + r20.left) - r19.getScrollX();
            int r7 = (r19.getTop() + r20.top) - r19.getScrollY();
            int r8 = r20.width() + r6;
            int r9 = r20.height() + r7;
            int r10 = Math.min(0, r6 - r2);
            int r12 = Math.min(0, r7 - r3);
            int r13 = Math.max(0, r8 - r4);
            int r14 = Math.max(0, r9 - r5);
            if (getLayoutDirection() != 1) goto L8;
            if (r13 == 0) goto L6;
            int r15 = r13;
        L12:
            if (r12 == 0) goto L14;
            int r11 = r12;
        L15:
            r1[0] = r15;
            r1[1] = r11;
            return r1;
        L14:
            r11 = Math.min(r7 - r3, r14);
            goto L15
        L6:
            r15 = Math.max(r10, r8 - r4);
            goto L12
        L8:
            if (r10 == 0) goto L10;
            r15 = r10;
            goto L12
        L10:
            r15 = Math.min(r6 - r2, r13);
            goto L12
        }

        public static Properties getProperties(Context r4, AttributeSet r5, int r6, int r7) {
            Properties r0 = new Properties();
            TypedArray r1 = r4.obtainStyledAttributes(r5, C0472R.styleable.RecyclerView, r6, r7);
            r0.orientation = r1.getInt(C0472R.styleable.RecyclerView_android_orientation, 1);
            r0.spanCount = r1.getInt(C0472R.styleable.RecyclerView_spanCount, 1);
            r0.reverseLayout = r1.getBoolean(C0472R.styleable.RecyclerView_reverseLayout, false);
            r0.stackFromEnd = r1.getBoolean(C0472R.styleable.RecyclerView_stackFromEnd, false);
            r1.recycle();
            return r0;
        }

        private boolean isFocusedChildVisibleAfterScrolling(RecyclerView r9, int r10, int r11) {
            View r0 = r9.getFocusedChild();
            if (r0 != null) goto L5;
            return false;
        L5:
            int r2 = getPaddingLeft();
            int r3 = getPaddingTop();
            int r4 = getWidth() - getPaddingRight();
            int r5 = getHeight() - getPaddingBottom();
            Rect r6 = this.mRecyclerView.mTempRect;
            getDecoratedBoundsWithMargins(r0, r6);
            if ((r6.left - r10) < r4) goto L8;
        L16:
            return false;
        L8:
            if ((r6.right - r10) <= r2) goto L16;
            if ((r6.top - r11) >= r5) goto L16;
            if ((r6.bottom - r11) <= r3) goto L16;
            return true;
        }

        private static boolean isMeasurementUpToDate(int r4, int r5, int r6) {
            int r0 = View.MeasureSpec.getMode(r5);
            int r1 = View.MeasureSpec.getSize(r5);
            if (r6 <= 0) goto L7;
            if (r4 == r6) goto L7;
            return false;
        L7:
            switch(r0) {
                case -2147483648: goto L13;
                case 0: goto L12;
                case 1073741824: goto L9;
                default: goto L8;
            };
        L8:
            return false;
        L9:
            if (r1 == r4) goto L11;
            return false;
        L11:
            return true;
        L12:
            return true;
        L13:
            if (r1 >= r4) goto L15;
            return false;
        L15:
            return true;
        }

        private void scrapOrRecycleView(Recycler r3, int r4, View r5) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r5);
            if (r0.shouldIgnore() == false) goto L6;
            return;
        L6:
            if (r0.isInvalid() == true) goto L8;
        L12:
            detachViewAt(r4);
            r3.scrapView(r5);
            this.mRecyclerView.mViewInfoStore.onViewDetached(r0);
            return;
        L8:
            if (r0.isRemoved() == true) goto L12;
            if (this.mRecyclerView.mAdapter.hasStableIds() == true) goto L12;
            removeViewAt(r4);
            r3.recycleViewHolderInternal(r0);
        }

        public void addDisappearingView(View r2) {
            addDisappearingView(r2, -1);
        }

        public void addDisappearingView(View r2, int r3) {
            addViewInt(r2, r3, true);
        }

        public void addView(View r2) {
            addView(r2, -1);
        }

        public void addView(View r2, int r3) {
            addViewInt(r2, r3, false);
        }

        public void assertInLayoutOrScroll(String r2) {
            if (this.mRecyclerView == null) goto L6;
            this.mRecyclerView.assertInLayoutOrScroll(r2);
            return;
        }

        public void assertNotInLayoutOrScroll(String r2) {
            if (this.mRecyclerView == null) goto L6;
            this.mRecyclerView.assertNotInLayoutOrScroll(r2);
            return;
        }

        public void attachView(View r2) {
            attachView(r2, -1);
        }

        public void attachView(View r2, int r3) {
            attachView(r2, r3, (LayoutParams) r2.getLayoutParams());
        }

        public void attachView(View r4, int r5, LayoutParams r6) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r4);
            if (r0.isRemoved() == false) goto L5;
            this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(r0);
        L6:
            this.mChildHelper.attachViewToParent(r4, r5, r6, r0.isRemoved());
            return;
        L5:
            this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(r0);
            goto L6
        }

        public void calculateItemDecorationsForChild(View r2, Rect r3) {
            if (this.mRecyclerView != null) goto L6;
            r3.set(0, 0, 0, 0);
            return;
        L6:
            r3.set(this.mRecyclerView.getItemDecorInsetsForChild(r2));
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public boolean checkLayoutParams(LayoutParams r2) {
            if (r2 == null) goto L4;
            return true;
        L4:
            return false;
        }

        public void collectAdjacentPrefetchPositions(int r1, int r2, State r3, LayoutPrefetchRegistry r4) {
        }

        public void collectInitialPrefetchPositions(int r1, LayoutPrefetchRegistry r2) {
        }

        public int computeHorizontalScrollExtent(State r2) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State r2) {
            return 0;
        }

        public int computeHorizontalScrollRange(State r2) {
            return 0;
        }

        public int computeVerticalScrollExtent(State r2) {
            return 0;
        }

        public int computeVerticalScrollOffset(State r2) {
            return 0;
        }

        public int computeVerticalScrollRange(State r2) {
            return 0;
        }

        public void detachAndScrapAttachedViews(Recycler r4) {
            int r1 = getChildCount() - 1;
        L3:
            if (r1 < 0) goto L5;
            scrapOrRecycleView(r4, r1, getChildAt(r1));
            r1 = r1 - 1;
            goto L3
        }

        public void detachAndScrapView(View r2, Recycler r3) {
            scrapOrRecycleView(r3, this.mChildHelper.indexOfChild(r2), r2);
        }

        public void detachAndScrapViewAt(int r2, Recycler r3) {
            scrapOrRecycleView(r3, r2, getChildAt(r2));
        }

        public void detachView(View r2) {
            int r0 = this.mChildHelper.indexOfChild(r2);
            if (r0 < 0) goto L6;
            detachViewInternal(r0, r2);
            return;
        }

        public void detachViewAt(int r2) {
            detachViewInternal(r2, getChildAt(r2));
        }

        void dispatchAttachedToWindow(RecyclerView r2) {
            this.mIsAttachedToWindow = true;
            onAttachedToWindow(r2);
        }

        void dispatchDetachedFromWindow(RecyclerView r2, Recycler r3) {
            this.mIsAttachedToWindow = false;
            onDetachedFromWindow(r2, r3);
        }

        public void endAnimation(View r3) {
            if (this.mRecyclerView.mItemAnimator == null) goto L6;
            this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(r3));
            return;
        }

        public View findContainingItemView(View r4) {
            if (this.mRecyclerView != null) goto L5;
            return null;
        L5:
            View r0 = this.mRecyclerView.findContainingItemView(r4);
            if (r0 != null) goto L9;
            return null;
        L9:
            if (this.mChildHelper.isHidden(r0) == false) goto L11;
            return null;
        L11:
            return r0;
        }

        public View findViewByPosition(int r6) {
            int r0 = getChildCount();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L17;
            View r2 = getChildAt(r1);
            ViewHolder r3 = RecyclerView.getChildViewHolderInt(r2);
            if (r3 == null) goto L16;
            if (r3.getLayoutPosition() != r6) goto L16;
            if (r3.shouldIgnore() == true) goto L16;
            if (this.mRecyclerView.mState.isPreLayout() == true) goto L15;
            if (r3.isRemoved() == true) goto L16;
        L15:
            return r2;
        L16:
            r1 = r1 + 1;
            goto L3
        L17:
            return null;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        public LayoutParams generateLayoutParams(Context r2, AttributeSet r3) {
            return new LayoutParams(r2, r3);
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams r3) {
            if ((r3 instanceof LayoutParams) == false) goto L7;
            return new LayoutParams((LayoutParams) r3);
        L7:
            if ((r3 instanceof ViewGroup.MarginLayoutParams) == false) goto L11;
            return new LayoutParams((ViewGroup.MarginLayoutParams) r3);
        L11:
            return new LayoutParams(r3);
        }

        public int getBaseline() {
            return -1;
        }

        public int getBottomDecorationHeight(View r2) {
            return ((LayoutParams) r2.getLayoutParams()).mDecorInsets.bottom;
        }

        public View getChildAt(int r2) {
            if (this.mChildHelper != null) goto L7;
            return null;
        L7:
            return this.mChildHelper.getChildAt(r2);
        }

        public int getChildCount() {
            if (this.mChildHelper != null) goto L7;
            return 0;
        L7:
            return this.mChildHelper.getChildCount();
        }

        public boolean getClipToPadding() {
            if (this.mRecyclerView != null) goto L5;
        L7:
            return false;
        L5:
            if (this.mRecyclerView.mClipToPadding == false) goto L7;
            return true;
        }

        public int getColumnCountForAccessibility(Recycler r2, State r3) {
            return -1;
        }

        public int getDecoratedBottom(View r3) {
            return r3.getBottom() + getBottomDecorationHeight(r3);
        }

        public void getDecoratedBoundsWithMargins(View r1, Rect r2) {
            RecyclerView.getDecoratedBoundsWithMarginsInt(r1, r2);
        }

        public int getDecoratedLeft(View r3) {
            return r3.getLeft() - getLeftDecorationWidth(r3);
        }

        public int getDecoratedMeasuredHeight(View r4) {
            Rect r0 = ((LayoutParams) r4.getLayoutParams()).mDecorInsets;
            return (r4.getMeasuredHeight() + r0.top) + r0.bottom;
        }

        public int getDecoratedMeasuredWidth(View r4) {
            Rect r0 = ((LayoutParams) r4.getLayoutParams()).mDecorInsets;
            return (r4.getMeasuredWidth() + r0.left) + r0.right;
        }

        public int getDecoratedRight(View r3) {
            return r3.getRight() + getRightDecorationWidth(r3);
        }

        public int getDecoratedTop(View r3) {
            return r3.getTop() - getTopDecorationHeight(r3);
        }

        public View getFocusedChild() {
            if (this.mRecyclerView != null) goto L5;
            return null;
        L5:
            View r0 = this.mRecyclerView.getFocusedChild();
            if (r0 != null) goto L8;
        L11:
            return null;
        L8:
            if (this.mChildHelper.isHidden(r0) == true) goto L11;
            return r0;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getHeightMode() {
            return this.mHeightMode;
        }

        public int getItemCount() {
            if (this.mRecyclerView == null) goto L5;
            Adapter r0 = this.mRecyclerView.getAdapter();
        L6:
            if (r0 != null) goto L10;
            return 0;
        L10:
            return r0.getItemCount();
        L5:
            r0 = null;
            goto L6
        }

        public int getItemViewType(View r2) {
            return RecyclerView.getChildViewHolderInt(r2).getItemViewType();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection(this.mRecyclerView);
        }

        public int getLeftDecorationWidth(View r2) {
            return ((LayoutParams) r2.getLayoutParams()).mDecorInsets.left;
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight(this.mRecyclerView);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth(this.mRecyclerView);
        }

        public int getPaddingBottom() {
            if (this.mRecyclerView != null) goto L7;
            return 0;
        L7:
            return this.mRecyclerView.getPaddingBottom();
        }

        public int getPaddingEnd() {
            if (this.mRecyclerView != null) goto L7;
            return 0;
        L7:
            return ViewCompat.getPaddingEnd(this.mRecyclerView);
        }

        public int getPaddingLeft() {
            if (this.mRecyclerView != null) goto L7;
            return 0;
        L7:
            return this.mRecyclerView.getPaddingLeft();
        }

        public int getPaddingRight() {
            if (this.mRecyclerView != null) goto L7;
            return 0;
        L7:
            return this.mRecyclerView.getPaddingRight();
        }

        public int getPaddingStart() {
            if (this.mRecyclerView != null) goto L7;
            return 0;
        L7:
            return ViewCompat.getPaddingStart(this.mRecyclerView);
        }

        public int getPaddingTop() {
            if (this.mRecyclerView != null) goto L7;
            return 0;
        L7:
            return this.mRecyclerView.getPaddingTop();
        }

        public int getPosition(View r2) {
            return ((LayoutParams) r2.getLayoutParams()).getViewLayoutPosition();
        }

        public int getRightDecorationWidth(View r2) {
            return ((LayoutParams) r2.getLayoutParams()).mDecorInsets.right;
        }

        public int getRowCountForAccessibility(Recycler r2, State r3) {
            return -1;
        }

        public int getSelectionModeForAccessibility(Recycler r2, State r3) {
            return 0;
        }

        public int getTopDecorationHeight(View r2) {
            return ((LayoutParams) r2.getLayoutParams()).mDecorInsets.top;
        }

        public void getTransformedBoundingBox(View r8, boolean r9, Rect r10) {
            if (r9 == false) goto L4;
            Rect r0 = ((LayoutParams) r8.getLayoutParams()).mDecorInsets;
            r10.set(-r0.left, -r0.top, r8.getWidth() + r0.right, r8.getHeight() + r0.bottom);
        L6:
            if (this.mRecyclerView == null) goto L12;
            Matrix r1 = r8.getMatrix();
            if (r1 == null) goto L12;
            if (r1.isIdentity() == true) goto L12;
            RectF r2 = this.mRecyclerView.mTempRectF;
            r2.set(r10);
            r1.mapRect(r2);
            r10.set((int) Math.floor(r2.left), (int) Math.floor(r2.top), (int) Math.ceil(r2.right), (int) Math.ceil(r2.bottom));
        L12:
            r10.offset(r8.getLeft(), r8.getTop());
            return;
        L4:
            r10.set(0, 0, r8.getWidth(), r8.getHeight());
            goto L6
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getWidthMode() {
            return this.mWidthMode;
        }

        boolean hasFlexibleChildInBothOrientations() {
            int r0 = getChildCount();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L11;
            ViewGroup.LayoutParams r3 = getChildAt(r1).getLayoutParams();
            if (r3.width >= 0) goto L10;
            if (r3.height >= 0) goto L10;
            return true;
        L10:
            r1 = r1 + 1;
            goto L3
        L11:
            return false;
        }

        public boolean hasFocus() {
            if (this.mRecyclerView != null) goto L5;
        L7:
            return false;
        L5:
            if (this.mRecyclerView.hasFocus() == false) goto L7;
            return true;
        }

        public void ignoreView(View r4) {
            if (r4.getParent() != this.mRecyclerView) goto L9;
            if (this.mRecyclerView.indexOfChild(r4) == (-1)) goto L9;
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r4);
            r0.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder(r0);
            return;
        L9:
            throw new IllegalArgumentException("View should be fully attached to be ignored" + this.mRecyclerView.exceptionLabel());
        }

        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }

        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }

        public boolean isFocused() {
            if (this.mRecyclerView != null) goto L5;
        L7:
            return false;
        L5:
            if (this.mRecyclerView.isFocused() == false) goto L7;
            return true;
        }

        public final boolean isItemPrefetchEnabled() {
            return this.mItemPrefetchEnabled;
        }

        public boolean isLayoutHierarchical(Recycler r2, State r3) {
            return false;
        }

        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }

        public boolean isSmoothScrolling() {
            if (this.mSmoothScroller != null) goto L5;
        L7:
            return false;
        L5:
            if (this.mSmoothScroller.isRunning() == false) goto L7;
            return true;
        }

        public boolean isViewPartiallyVisible(View r5, boolean r6, boolean r7) {
            if (this.mHorizontalBoundCheck.isViewWithinBoundFlags(r5, 24579) == true) goto L5;
        L7:
            boolean r1 = false;
        L8:
            if (r6 == false) goto L10;
            return r1;
        L10:
            if (r1 == true) goto L13;
            return true;
        L13:
            return false;
        L5:
            if (this.mVerticalBoundCheck.isViewWithinBoundFlags(r5, 24579) == false) goto L7;
            r1 = true;
            goto L8
        }

        public void layoutDecorated(View r6, int r7, int r8, int r9, int r10) {
            Rect r0 = ((LayoutParams) r6.getLayoutParams()).mDecorInsets;
            r6.layout(r0.left + r7, r0.top + r8, r9 - r0.right, r10 - r0.bottom);
        }

        public void layoutDecoratedWithMargins(View r8, int r9, int r10, int r11, int r12) {
            LayoutParams r0 = (LayoutParams) r8.getLayoutParams();
            Rect r1 = r0.mDecorInsets;
            r8.layout((r1.left + r9) + r0.leftMargin, (r1.top + r10) + r0.topMargin, (r11 - r1.right) - r0.rightMargin, (r12 - r1.bottom) - r0.bottomMargin);
        }

        public void measureChild(View r9, int r10, int r11) {
            LayoutParams r0 = (LayoutParams) r9.getLayoutParams();
            Rect r1 = this.mRecyclerView.getItemDecorInsetsForChild(r9);
            int r12 = r10 + (r1.left + r1.right);
            int r13 = r11 + (r1.top + r1.bottom);
            int r2 = getChildMeasureSpec(getWidth(), getWidthMode(), (getPaddingLeft() + getPaddingRight()) + r12, r0.width, canScrollHorizontally());
            int r3 = getChildMeasureSpec(getHeight(), getHeightMode(), (getPaddingTop() + getPaddingBottom()) + r13, r0.height, canScrollVertically());
            if (shouldMeasureChild(r9, r2, r3, r0) == false) goto L6;
            r9.measure(r2, r3);
            return;
        }

        public void measureChildWithMargins(View r9, int r10, int r11) {
            LayoutParams r0 = (LayoutParams) r9.getLayoutParams();
            Rect r1 = this.mRecyclerView.getItemDecorInsetsForChild(r9);
            int r12 = r10 + (r1.left + r1.right);
            int r13 = r11 + (r1.top + r1.bottom);
            int r2 = getChildMeasureSpec(getWidth(), getWidthMode(), (((getPaddingLeft() + getPaddingRight()) + r0.leftMargin) + r0.rightMargin) + r12, r0.width, canScrollHorizontally());
            int r3 = getChildMeasureSpec(getHeight(), getHeightMode(), (((getPaddingTop() + getPaddingBottom()) + r0.topMargin) + r0.bottomMargin) + r13, r0.height, canScrollVertically());
            if (shouldMeasureChild(r9, r2, r3, r0) == false) goto L6;
            r9.measure(r2, r3);
            return;
        }

        public void moveView(int r5, int r6) {
            View r0 = getChildAt(r5);
            if (r0 == null) goto L7;
            detachViewAt(r5);
            attachView(r0, r6);
            return;
        L7:
            throw new IllegalArgumentException("Cannot move a child from non-existing index:" + r5 + this.mRecyclerView.toString());
        }

        public void offsetChildrenHorizontal(int r2) {
            if (this.mRecyclerView == null) goto L6;
            this.mRecyclerView.offsetChildrenHorizontal(r2);
            return;
        }

        public void offsetChildrenVertical(int r2) {
            if (this.mRecyclerView == null) goto L6;
            this.mRecyclerView.offsetChildrenVertical(r2);
            return;
        }

        public void onAdapterChanged(Adapter r1, Adapter r2) {
        }

        public boolean onAddFocusables(RecyclerView r2, ArrayList<View> r3, int r4, int r5) {
            return false;
        }

        public void onAttachedToWindow(RecyclerView r1) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView r1) {
        }

        public void onDetachedFromWindow(RecyclerView r1, Recycler r2) {
            onDetachedFromWindow(r1);
        }

        public View onFocusSearchFailed(View r2, int r3, Recycler r4, State r5) {
            return null;
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent r3) {
            onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, r3);
        }

        public void onInitializeAccessibilityEvent(Recycler r4, State r5, AccessibilityEvent r6) {
            if (this.mRecyclerView == null) goto L21;
            if (r6 == null) goto L23;
            boolean r1 = true;
            if (this.mRecyclerView.canScrollVertically(1) == false) goto L9;
        L17:
            r6.setScrollable(r1);
            if (this.mRecyclerView.mAdapter == null) goto L22;
            r6.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
            return;
        L22:
            return;
        L9:
            if (this.mRecyclerView.canScrollVertically(-1) == true) goto L17;
            if (this.mRecyclerView.canScrollHorizontally(-1) == true) goto L17;
            if (this.mRecyclerView.canScrollHorizontally(1) == true) goto L17;
            r1 = false;
            goto L17
        L23:
            return;
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat r3) {
            onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, r3);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler r5, State r6, AccessibilityNodeInfoCompat r7) {
            if (this.mRecyclerView.canScrollVertically(-1) == false) goto L5;
        L6:
            r7.addAction(8192);
            r7.setScrollable(true);
        L8:
            if (this.mRecyclerView.canScrollVertically(1) == false) goto L10;
        L11:
            r7.addAction(4096);
            r7.setScrollable(true);
        L12:
            r7.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(r5, r6), getColumnCountForAccessibility(r5, r6), isLayoutHierarchical(r5, r6), getSelectionModeForAccessibility(r5, r6)));
            return;
        L10:
            if (this.mRecyclerView.canScrollHorizontally(1) == false) goto L12;
        L5:
            if (this.mRecyclerView.canScrollHorizontally(-1) == false) goto L8;
            goto L6
        }

        void onInitializeAccessibilityNodeInfoForItem(View r4, AccessibilityNodeInfoCompat r5) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r4);
            if (r0 != null) goto L5;
            return;
        L5:
            if (r0.isRemoved() == false) goto L7;
            return;
        L7:
            if (this.mChildHelper.isHidden(r0.itemView) == true) goto L12;
            onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, r4, r5);
            return;
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler r1, State r2, View r3, AccessibilityNodeInfoCompat r4) {
        }

        public View onInterceptFocusSearch(View r2, int r3) {
            return null;
        }

        public void onItemsAdded(RecyclerView r1, int r2, int r3) {
        }

        public void onItemsChanged(RecyclerView r1) {
        }

        public void onItemsMoved(RecyclerView r1, int r2, int r3, int r4) {
        }

        public void onItemsRemoved(RecyclerView r1, int r2, int r3) {
        }

        public void onItemsUpdated(RecyclerView r1, int r2, int r3) {
        }

        public void onItemsUpdated(RecyclerView r1, int r2, int r3, Object r4) {
            onItemsUpdated(r1, r2, r3);
        }

        public void onLayoutChildren(Recycler r3, State r4) {
            Log.e(RecyclerView.TAG, "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onLayoutCompleted(State r1) {
        }

        public void onMeasure(Recycler r2, State r3, int r4, int r5) {
            this.mRecyclerView.defaultOnMeasure(r4, r5);
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView r2, View r3, View r4) {
            if (isSmoothScrolling() == false) goto L5;
        L8:
            return true;
        L5:
            if (r2.isComputingLayout() == true) goto L8;
            return false;
        }

        public boolean onRequestChildFocus(RecyclerView r2, State r3, View r4, View r5) {
            return onRequestChildFocus(r2, r4, r5);
        }

        public void onRestoreInstanceState(Parcelable r1) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onScrollStateChanged(int r1) {
        }

        void onSmoothScrollerStopped(SmoothScroller r2) {
            if (this.mSmoothScroller != r2) goto L6;
            this.mSmoothScroller = null;
            return;
        }

        boolean performAccessibilityAction(int r3, Bundle r4) {
            return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, r3, r4);
        }

        public boolean performAccessibilityAction(Recycler r14, State r15, int r16, Bundle r17) {
            if (this.mRecyclerView != null) goto L5;
            return false;
        L5:
            int r0 = 0;
            int r3 = getHeight();
            int r4 = getWidth();
            Rect r5 = new Rect();
            if (this.mRecyclerView.getMatrix().isIdentity() == false) goto L11;
            if (this.mRecyclerView.getGlobalVisibleRect(r5) == false) goto L11;
            r3 = r5.height();
            r4 = r5.width();
        L11:
            switch(r16) {
                case 4096: goto L21;
                case 8192: goto L14;
                default: goto L12;
            };
        L12:
            int r9 = 0;
            int r8 = 0;
        L27:
            if (r9 != 0) goto L30;
            if (r8 != 0) goto L30;
            return false;
        L30:
            this.mRecyclerView.smoothScrollBy(r8, r9, null, Integer.MIN_VALUE, true);
            return true;
        L14:
            if (this.mRecyclerView.canScrollVertically(-1) == false) goto L17;
            r0 = -((r3 - getPaddingTop()) - getPaddingBottom());
        L17:
            if (this.mRecyclerView.canScrollHorizontally(-1) == false) goto L19;
            r9 = r0;
            r8 = -((r4 - getPaddingLeft()) - getPaddingRight());
            goto L27
        L19:
            r9 = r0;
            r8 = 0;
            goto L27
        L21:
            if (this.mRecyclerView.canScrollVertically(1) == false) goto L24;
            r0 = (r3 - getPaddingTop()) - getPaddingBottom();
        L24:
            if (this.mRecyclerView.canScrollHorizontally(1) == false) goto L26;
            r9 = r0;
            r8 = (r4 - getPaddingLeft()) - getPaddingRight();
            goto L27
        L26:
            r9 = r0;
            r8 = 0;
            goto L27
        }

        boolean performAccessibilityActionForItem(View r8, int r9, Bundle r10) {
            return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, r8, r9, r10);
        }

        public boolean performAccessibilityActionForItem(Recycler r2, State r3, View r4, int r5, Bundle r6) {
            return false;
        }

        public void postOnAnimation(Runnable r2) {
            if (this.mRecyclerView == null) goto L6;
            ViewCompat.postOnAnimation(this.mRecyclerView, r2);
            return;
        }

        public void removeAllViews() {
            int r1 = getChildCount() - 1;
        L3:
            if (r1 < 0) goto L5;
            this.mChildHelper.removeViewAt(r1);
            r1 = r1 - 1;
            goto L3
        }

        public void removeAndRecycleAllViews(Recycler r4) {
            int r0 = getChildCount() - 1;
        L3:
            if (r0 < 0) goto L8;
            if (RecyclerView.getChildViewHolderInt(getChildAt(r0)).shouldIgnore() == true) goto L7;
            removeAndRecycleViewAt(r0, r4);
        L7:
            r0 = r0 - 1;
            goto L3
        }

        void removeAndRecycleScrapInt(Recycler r7) {
            int r0 = r7.getScrapCount();
            int r1 = r0 - 1;
        L3:
            if (r1 < 0) goto L15;
            View r2 = r7.getScrapViewAt(r1);
            ViewHolder r3 = RecyclerView.getChildViewHolderInt(r2);
            if (r3.shouldIgnore() == true) goto L14;
            r3.setIsRecyclable(false);
            if (r3.isTmpDetached() == false) goto L11;
            this.mRecyclerView.removeDetachedView(r2, false);
        L11:
            if (this.mRecyclerView.mItemAnimator == null) goto L13;
            this.mRecyclerView.mItemAnimator.endAnimation(r3);
        L13:
            r3.setIsRecyclable(true);
            r7.quickRecycleScrapView(r2);
        L14:
            r1 = r1 - 1;
            goto L3
        L15:
            r7.clearScrap();
            if (r0 <= 0) goto L22;
            this.mRecyclerView.invalidate();
            return;
        }

        public void removeAndRecycleView(View r1, Recycler r2) {
            removeView(r1);
            r2.recycleView(r1);
        }

        public void removeAndRecycleViewAt(int r2, Recycler r3) {
            View r0 = getChildAt(r2);
            removeViewAt(r2);
            r3.recycleView(r0);
        }

        public boolean removeCallbacks(Runnable r2) {
            if (this.mRecyclerView != null) goto L5;
            return false;
        L5:
            return this.mRecyclerView.removeCallbacks(r2);
        }

        public void removeDetachedView(View r3) {
            this.mRecyclerView.removeDetachedView(r3, false);
        }

        public void removeView(View r2) {
            this.mChildHelper.removeView(r2);
        }

        public void removeViewAt(int r3) {
            if (getChildAt(r3) == null) goto L6;
            this.mChildHelper.removeViewAt(r3);
            return;
        }

        public boolean requestChildRectangleOnScreen(RecyclerView r7, View r8, Rect r9, boolean r10) {
            return requestChildRectangleOnScreen(r7, r8, r9, r10, false);
        }

        public boolean requestChildRectangleOnScreen(RecyclerView r7, View r8, Rect r9, boolean r10, boolean r11) {
            int[] r0 = getChildRectangleOnScreenScrollAmount(r8, r9);
            int r2 = r0[0];
            int r4 = r0[1];
            if (r11 == true) goto L5;
        L6:
            if (r2 != 0) goto L10;
            if (r4 != 0) goto L10;
        L9:
            return false;
        L10:
            if (r10 == false) goto L12;
            r7.scrollBy(r2, r4);
        L13:
            return true;
        L12:
            r7.smoothScrollBy(r2, r4);
            goto L13
        L5:
            if (isFocusedChildVisibleAfterScrolling(r7, r2, r4) == false) goto L9;
            goto L6
        }

        public void requestLayout() {
            if (this.mRecyclerView == null) goto L6;
            this.mRecyclerView.requestLayout();
            return;
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int scrollHorizontallyBy(int r2, Recycler r3, State r4) {
            return 0;
        }

        public void scrollToPosition(int r1) {
        }

        public int scrollVerticallyBy(int r2, Recycler r3, State r4) {
            return 0;
        }

        @Deprecated
        public void setAutoMeasureEnabled(boolean r1) {
            this.mAutoMeasure = r1;
        }

        void setExactMeasureSpecsFrom(RecyclerView r4) {
            setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(r4.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(r4.getHeight(), 1073741824));
        }

        public final void setItemPrefetchEnabled(boolean r2) {
            if (r2 == this.mItemPrefetchEnabled) goto L8;
            this.mItemPrefetchEnabled = r2;
            this.mPrefetchMaxCountObserved = 0;
            if (this.mRecyclerView == null) goto L9;
            this.mRecyclerView.mRecycler.updateViewCacheSize();
            return;
        L9:
            return;
        }

        void setMeasureSpecs(int r3, int r4) {
            this.mWidth = View.MeasureSpec.getSize(r3);
            this.mWidthMode = View.MeasureSpec.getMode(r3);
            if (this.mWidthMode == 0) goto L5;
        L7:
            this.mHeight = View.MeasureSpec.getSize(r4);
            this.mHeightMode = View.MeasureSpec.getMode(r4);
            if (this.mHeightMode == 0) goto L10;
            return;
        L10:
            if (RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC == true) goto L14;
            this.mHeight = 0;
            return;
        L14:
            return;
        L5:
            if (RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC == true) goto L7;
            this.mWidth = 0;
            goto L7
        }

        public void setMeasuredDimension(int r2, int r3) {
            RecyclerView.access$300(this.mRecyclerView, r2, r3);
        }

        public void setMeasuredDimension(Rect r5, int r6, int r7) {
            int r0 = (r5.width() + getPaddingLeft()) + getPaddingRight();
            int r1 = (r5.height() + getPaddingTop()) + getPaddingBottom();
            setMeasuredDimension(chooseSize(r6, r0, getMinimumWidth()), chooseSize(r7, r1, getMinimumHeight()));
        }

        void setMeasuredDimensionFromChildren(int r10, int r11) {
            int r0 = getChildCount();
            if (r0 != 0) goto L6;
            this.mRecyclerView.defaultOnMeasure(r10, r11);
            return;
        L6:
            int r1 = Integer.MAX_VALUE;
            int r2 = Integer.MAX_VALUE;
            int r3 = Integer.MIN_VALUE;
            int r4 = Integer.MIN_VALUE;
            int r5 = 0;
        L7:
            if (r5 >= r0) goto L21;
            View r6 = getChildAt(r5);
            Rect r7 = this.mRecyclerView.mTempRect;
            getDecoratedBoundsWithMargins(r6, r7);
            if (r7.left >= r1) goto L12;
            r1 = r7.left;
        L12:
            if (r7.right <= r3) goto L15;
            r3 = r7.right;
        L15:
            if (r7.top >= r2) goto L18;
            r2 = r7.top;
        L18:
            if (r7.bottom <= r4) goto L20;
            r4 = r7.bottom;
        L20:
            r5 = r5 + 1;
            goto L7
        L21:
            this.mRecyclerView.mTempRect.set(r1, r2, r3, r4);
            setMeasuredDimension(this.mRecyclerView.mTempRect, r10, r11);
        }

        public void setMeasurementCacheEnabled(boolean r1) {
            this.mMeasurementCacheEnabled = r1;
        }

        void setRecyclerView(RecyclerView r2) {
            if (r2 != null) goto L4;
            this.mRecyclerView = null;
            this.mChildHelper = null;
            this.mWidth = 0;
            this.mHeight = 0;
        L5:
            this.mWidthMode = 1073741824;
            this.mHeightMode = 1073741824;
            return;
        L4:
            this.mRecyclerView = r2;
            this.mChildHelper = r2.mChildHelper;
            this.mWidth = r2.getWidth();
            this.mHeight = r2.getHeight();
            goto L5
        }

        boolean shouldMeasureChild(View r3, int r4, int r5, LayoutParams r6) {
            if (r3.isLayoutRequested() == false) goto L5;
        L12:
            return true;
        L5:
            if (this.mMeasurementCacheEnabled == false) goto L12;
            if (isMeasurementUpToDate(r3.getWidth(), r4, r6.width) == false) goto L12;
            if (isMeasurementUpToDate(r3.getHeight(), r5, r6.height) == false) goto L12;
            return false;
        }

        boolean shouldMeasureTwice() {
            return false;
        }

        boolean shouldReMeasureChild(View r3, int r4, int r5, LayoutParams r6) {
            if (this.mMeasurementCacheEnabled == true) goto L5;
        L10:
            return true;
        L5:
            if (isMeasurementUpToDate(r3.getMeasuredWidth(), r4, r6.width) == false) goto L10;
            if (isMeasurementUpToDate(r3.getMeasuredHeight(), r5, r6.height) == false) goto L10;
            return false;
        }

        public void smoothScrollToPosition(RecyclerView r3, State r4, int r5) {
            Log.e(RecyclerView.TAG, "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller r3) {
            if (this.mSmoothScroller != null) goto L5;
        L9:
            this.mSmoothScroller = r3;
            this.mSmoothScroller.start(this.mRecyclerView, this);
            return;
        L5:
            if (r3 == this.mSmoothScroller) goto L9;
            if (this.mSmoothScroller.isRunning() == false) goto L9;
            this.mSmoothScroller.stop();
            goto L9
        }

        public void stopIgnoringView(View r3) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r3);
            r0.stopIgnoring();
            r0.resetInternal();
            r0.addFlags(4);
        }

        void stopSmoothScroller() {
            if (this.mSmoothScroller == null) goto L6;
            this.mSmoothScroller.stop();
            return;
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets;
        boolean mInsetsDirty;
        boolean mPendingInvalidate;
        ViewHolder mViewHolder;

        public LayoutParams(int r2, int r3) {
            super(r2, r3);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(Context r2, AttributeSet r3) {
            super(r2, r3);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(ViewGroup.LayoutParams r2) {
            super(r2);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams r2) {
            super(r2);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(LayoutParams r2) {
            super(r2);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public int getAbsoluteAdapterPosition() {
            return this.mViewHolder.getAbsoluteAdapterPosition();
        }

        public int getBindingAdapterPosition() {
            return this.mViewHolder.getBindingAdapterPosition();
        }

        @Deprecated
        public int getViewAdapterPosition() {
            return this.mViewHolder.getBindingAdapterPosition();
        }

        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }

        @Deprecated
        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }

        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }
    }

    public interface OnChildAttachStateChangeListener {
        void onChildViewAttachedToWindow(View r1);

        void onChildViewDetachedFromWindow(View r1);
    }

    public static abstract class OnFlingListener {
        public OnFlingListener() {
        }

        public abstract boolean onFling(int r1, int r2);
    }

    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent(RecyclerView r1, MotionEvent r2);

        void onRequestDisallowInterceptTouchEvent(boolean r1);

        void onTouchEvent(RecyclerView r1, MotionEvent r2);
    }

    public static abstract class OnScrollListener {
        public OnScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView r1, int r2) {
        }

        public void onScrolled(RecyclerView r1, int r2, int r3) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        int mAttachCountForClearing;
        Set<Adapter<?>> mAttachedAdaptersForPoolingContainer;
        SparseArray<ScrapData> mScrap;

        static class ScrapData {
            long mBindRunningAverageNs;
            long mCreateRunningAverageNs;
            int mMaxScrap;
            final ArrayList<ViewHolder> mScrapHeap;

            ScrapData() {
                this.mScrapHeap = new ArrayList();
                this.mMaxScrap = 5;
                this.mCreateRunningAverageNs = 0;
                this.mBindRunningAverageNs = 0;
            }
        }

        public RecycledViewPool() {
            this.mScrap = new SparseArray();
            this.mAttachCountForClearing = 0;
            this.mAttachedAdaptersForPoolingContainer = Collections.newSetFromMap(new IdentityHashMap());
        }

        private ScrapData getScrapDataForType(int r3) {
            ScrapData r0 = this.mScrap.get(r3);
            if (r0 != null) goto L6;
            ScrapData r1 = new ScrapData();
            this.mScrap.put(r3, r1);
            return r1;
        L6:
            return r0;
        }

        void attach() {
            this.mAttachCountForClearing++;
        }

        void attachForPoolingContainer(Adapter<?> r2) {
            this.mAttachedAdaptersForPoolingContainer.add(r2);
        }

        public void clear() {
            int r0 = 0;
        L4:
            if (r0 >= this.mScrap.size()) goto L10;
            ScrapData r1 = this.mScrap.valueAt(r0);
            Iterator<ViewHolder> r2 = r1.mScrapHeap.iterator();
        L7:
            if (r2.hasNext() == false) goto L9;
            PoolingContainer.callPoolingContainerOnRelease(r2.next().itemView);
            goto L7
        L9:
            r1.mScrapHeap.clear();
            r0 = r0 + 1;
            goto L4
        }

        void detach() {
            this.mAttachCountForClearing--;
        }

        void detachForPoolingContainer(Adapter<?> r5, boolean r6) {
            this.mAttachedAdaptersForPoolingContainer.remove(r5);
            if (this.mAttachedAdaptersForPoolingContainer.size() != 0) goto L13;
            if (r6 == true) goto L16;
            int r0 = 0;
        L7:
            if (r0 >= this.mScrap.size()) goto L17;
            ArrayList<ViewHolder> r1 = this.mScrap.get(this.mScrap.keyAt(r0)).mScrapHeap;
            int r2 = 0;
        L10:
            if (r2 >= r1.size()) goto L12;
            PoolingContainer.callPoolingContainerOnRelease(r1.get(r2).itemView);
            r2 = r2 + 1;
            goto L10
        L12:
            r0 = r0 + 1;
            goto L7
        L17:
            return;
        L16:
            return;
        }

        void factorInBindTime(int r4, long r5) {
            ScrapData r0 = getScrapDataForType(r4);
            r0.mBindRunningAverageNs = runningAverage(r0.mBindRunningAverageNs, r5);
        }

        void factorInCreateTime(int r4, long r5) {
            ScrapData r0 = getScrapDataForType(r4);
            r0.mCreateRunningAverageNs = runningAverage(r0.mCreateRunningAverageNs, r5);
        }

        public ViewHolder getRecycledView(int r5) {
            ScrapData r0 = this.mScrap.get(r5);
            if (r0 != null) goto L5;
            return null;
        L5:
            if (r0.mScrapHeap.isEmpty() == true) goto L17;
            ArrayList<ViewHolder> r1 = r0.mScrapHeap;
            int r2 = r1.size() - 1;
        L7:
            if (r2 < 0) goto L18;
            if (r1.get(r2).isAttachedToTransitionOverlay() == false) goto L11;
            r2 = r2 - 1;
            goto L7
        L11:
            return r1.remove(r2);
        L18:
            return null;
        L17:
            return null;
        }

        public int getRecycledViewCount(int r2) {
            return getScrapDataForType(r2).mScrapHeap.size();
        }

        void onAdapterChanged(Adapter<?> r2, Adapter<?> r3, boolean r4) {
            if (r2 == null) goto L4;
            detach();
        L4:
            if (r4 == false) goto L6;
        L8:
            if (r3 == null) goto L11;
            attach();
            return;
        L11:
            return;
        L6:
            if (this.mAttachCountForClearing != 0) goto L8;
            clear();
            goto L8
        }

        public void putRecycledView(ViewHolder r5) {
            int r0 = r5.getItemViewType();
            ArrayList<ViewHolder> r1 = getScrapDataForType(r0).mScrapHeap;
            if (this.mScrap.get(r0).mMaxScrap > r1.size()) goto L6;
            PoolingContainer.callPoolingContainerOnRelease(r5.itemView);
            return;
        L6:
            r5.resetInternal();
            r1.add(r5);
        }

        long runningAverage(long r7, long r9) {
            if (r7 != 0) goto L6;
            return r9;
        L6:
            return ((r7 / 4) * 3) + (r9 / 4);
        }

        public void setMaxRecycledViews(int r4, int r5) {
            ScrapData r0 = getScrapDataForType(r4);
            r0.mMaxScrap = r5;
            ArrayList<ViewHolder> r1 = r0.mScrapHeap;
        L4:
            if (r1.size() <= r5) goto L6;
            r1.remove(r1.size() - 1);
            goto L4
        }

        int size() {
            int r0 = 0;
            int r1 = 0;
        L4:
            if (r1 >= this.mScrap.size()) goto L9;
            ArrayList<ViewHolder> r2 = this.mScrap.valueAt(r1).mScrapHeap;
            if (r2 == null) goto L8;
            r0 = r0 + r2.size();
        L8:
            r1 = r1 + 1;
            goto L4
        L9:
            return r0;
        }

        boolean willBindInTime(int r5, long r6, long r8) {
            long r0 = getScrapDataForType(r5).mBindRunningAverageNs;
            if (r0 != 0) goto L5;
        L8:
            return true;
        L5:
            if ((r6 + r0) < r8) goto L8;
            return false;
        }

        boolean willCreateInTime(int r5, long r6, long r8) {
            long r0 = getScrapDataForType(r5).mCreateRunningAverageNs;
            if (r0 != 0) goto L5;
        L8:
            return true;
        L5:
            if ((r6 + r0) < r8) goto L8;
            return false;
        }
    }

    public final class Recycler {
        static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap;
        final ArrayList<ViewHolder> mCachedViews;
        ArrayList<ViewHolder> mChangedScrap;
        RecycledViewPool mRecyclerPool;
        private int mRequestedCacheMax;
        private final List<ViewHolder> mUnmodifiableAttachedScrap;
        private ViewCacheExtension mViewCacheExtension;
        int mViewCacheMax;
        final /* synthetic */ RecyclerView this$0;

        public Recycler(RecyclerView r2) {
            this.this$0 = r2;
            this.mAttachedScrap = new ArrayList();
            this.mChangedScrap = null;
            this.mCachedViews = new ArrayList();
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
            this.mRequestedCacheMax = 2;
            this.mViewCacheMax = 2;
        }

        private void attachAccessibilityDelegateOnBind(ViewHolder r4) {
            if (this.this$0.isAccessibilityEnabled() == false) goto L15;
            View r0 = r4.itemView;
            if (ViewCompat.getImportantForAccessibility(r0) != 0) goto L8;
            ViewCompat.setImportantForAccessibility(r0, 1);
        L8:
            if (this.this$0.mAccessibilityDelegate != null) goto L10;
            return;
        L10:
            AccessibilityDelegateCompat r1 = this.this$0.mAccessibilityDelegate.getItemDelegate();
            if ((r1 instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) == false) goto L13;
            ((RecyclerViewAccessibilityDelegate.ItemDelegate) r1).saveOriginalDelegate(r0);
        L13:
            ViewCompat.setAccessibilityDelegate(r0, r1);
            return;
        }

        private void invalidateDisplayListInt(ViewGroup r5, boolean r6) {
            int r0 = r5.getChildCount() - 1;
        L3:
            if (r0 < 0) goto L8;
            View r2 = r5.getChildAt(r0);
            if ((r2 instanceof ViewGroup) == false) goto L7;
            invalidateDisplayListInt((ViewGroup) r2, true);
        L7:
            r0 = r0 - 1;
            goto L3
        L8:
            if (r6 == true) goto L11;
            return;
        L11:
            if (r5.getVisibility() != 4) goto L13;
            r5.setVisibility(0);
            r5.setVisibility(4);
            return;
        L13:
            int r1 = r5.getVisibility();
            r5.setVisibility(4);
            r5.setVisibility(r1);
        }

        private void invalidateDisplayListInt(ViewHolder r3) {
            if ((r3.itemView instanceof ViewGroup) == false) goto L6;
            invalidateDisplayListInt((ViewGroup) r3.itemView, false);
            return;
        }

        private void maybeSendPoolingContainerAttach() {
            if (this.mRecyclerPool != null) goto L5;
            return;
        L5:
            if (this.this$0.mAdapter != null) goto L7;
            return;
        L7:
            if (this.this$0.isAttachedToWindow() == false) goto L12;
            this.mRecyclerPool.attachForPoolingContainer(this.this$0.mAdapter);
            return;
        }

        private void poolingContainerDetach(Adapter<?> r2) {
            poolingContainerDetach(r2, false);
        }

        private void poolingContainerDetach(Adapter<?> r2, boolean r3) {
            if (this.mRecyclerPool == null) goto L6;
            this.mRecyclerPool.detachForPoolingContainer(r2, r3);
            return;
        }

        private boolean tryBindViewHolderByDeadline(ViewHolder r10, int r11, int r12, long r13) {
            r10.mBindingAdapter = null;
            r10.mOwnerRecyclerView = this.this$0;
            int r2 = r10.getItemViewType();
            long r3 = this.this$0.getNanoTime();
            if (r13 == Long.MAX_VALUE) goto L9;
            if (this.mRecyclerPool.willBindInTime(r2, r3, r13) == true) goto L9;
            return false;
        L9:
            this.this$0.mAdapter.bindViewHolder(r10, r11);
            long r14 = this.this$0.getNanoTime();
            this.mRecyclerPool.factorInBindTime(r10.getItemViewType(), r14 - r3);
            attachAccessibilityDelegateOnBind(r10);
            if (this.this$0.mState.isPreLayout() == false) goto L14;
            r10.mPreLayoutPosition = r12;
            return true;
        L14:
            return true;
        }

        void addViewHolderToRecycledViewPool(ViewHolder r5, boolean r6) {
            RecyclerView.clearNestedRecyclerViewIfNotNested(r5);
            View r0 = r5.itemView;
            if (this.this$0.mAccessibilityDelegate == null) goto L8;
            AccessibilityDelegateCompat r1 = this.this$0.mAccessibilityDelegate.getItemDelegate();
            AccessibilityDelegateCompat r2 = null;
            if ((r1 instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) == false) goto L7;
            r2 = ((RecyclerViewAccessibilityDelegate.ItemDelegate) r1).getAndRemoveOriginalDelegateForItem(r0);
        L7:
            ViewCompat.setAccessibilityDelegate(r0, r2);
        L8:
            if (r6 == false) goto L10;
            dispatchViewRecycled(r5);
        L10:
            r5.mBindingAdapter = null;
            r5.mOwnerRecyclerView = null;
            getRecycledViewPool().putRecycledView(r5);
        }

        public void bindViewToPosition(View r8, int r9) {
            ViewHolder r1 = RecyclerView.getChildViewHolderInt(r8);
            if (r1 == null) goto L24;
            int r2 = this.this$0.mAdapterHelper.findPositionOffset(r9);
            if (r2 < 0) goto L22;
            if (r2 >= this.this$0.mAdapter.getItemCount()) goto L22;
            tryBindViewHolderByDeadline(r1, r2, r9, Long.MAX_VALUE);
            ViewGroup.LayoutParams r10 = r1.itemView.getLayoutParams();
            if (r10 != null) goto L12;
            LayoutParams r4 = (LayoutParams) this.this$0.generateDefaultLayoutParams();
            r1.itemView.setLayoutParams(r4);
        L15:
            boolean r5 = true;
            r4.mInsetsDirty = true;
            r4.mViewHolder = r1;
            if (r1.itemView.getParent() == null) goto L19;
            r5 = false;
        L19:
            r4.mPendingInvalidate = r5;
            return;
        L12:
            if (this.this$0.checkLayoutParams(r10) == true) goto L14;
            r4 = (LayoutParams) this.this$0.generateLayoutParams(r10);
            r1.itemView.setLayoutParams(r4);
            goto L15
        L14:
            r4 = (LayoutParams) r10;
        L22:
            throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + r9 + "(offset:" + r2 + ").state:" + this.this$0.mState.getItemCount() + this.this$0.exceptionLabel());
        L24:
            throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter" + this.this$0.exceptionLabel());
        }

        public void clear() {
            this.mAttachedScrap.clear();
            recycleAndClearCachedViews();
        }

        void clearOldPositions() {
            int r0 = this.mCachedViews.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L5;
            this.mCachedViews.get(r1).clearOldPosition();
            r1 = r1 + 1;
            goto L3
        L5:
            int r2 = this.mAttachedScrap.size();
            int r3 = 0;
        L6:
            if (r3 >= r2) goto L9;
            this.mAttachedScrap.get(r3).clearOldPosition();
            r3 = r3 + 1;
            goto L6
        L9:
            if (this.mChangedScrap == null) goto L13;
            int r4 = this.mChangedScrap.size();
            int r5 = 0;
        L11:
            if (r5 >= r4) goto L17;
            this.mChangedScrap.get(r5).clearOldPosition();
            r5 = r5 + 1;
            goto L11
        L17:
            return;
        }

        void clearScrap() {
            this.mAttachedScrap.clear();
            if (this.mChangedScrap == null) goto L6;
            this.mChangedScrap.clear();
            return;
        }

        public int convertPreLayoutPositionToPostLayout(int r4) {
            if (r4 < 0) goto L11;
            if (r4 >= this.this$0.mState.getItemCount()) goto L11;
            if (this.this$0.mState.isPreLayout() == true) goto L9;
            return r4;
        L9:
            return this.this$0.mAdapterHelper.findPositionOffset(r4);
        L11:
            throw new IndexOutOfBoundsException("invalid position " + r4 + ". State item count is " + this.this$0.mState.getItemCount() + this.this$0.exceptionLabel());
        }

        void dispatchViewRecycled(ViewHolder r4) {
            if (this.this$0.mRecyclerListener == null) goto L5;
            this.this$0.mRecyclerListener.onViewRecycled(r4);
        L5:
            int r0 = this.this$0.mRecyclerListeners.size();
            int r1 = 0;
        L6:
            if (r1 >= r0) goto L9;
            this.this$0.mRecyclerListeners.get(r1).onViewRecycled(r4);
            r1 = r1 + 1;
            goto L6
        L9:
            if (this.this$0.mAdapter == null) goto L12;
            this.this$0.mAdapter.onViewRecycled(r4);
        L12:
            if (this.this$0.mState == null) goto L16;
            this.this$0.mViewInfoStore.removeViewHolder(r4);
            return;
        }

        ViewHolder getChangedScrapViewForPosition(int r11) {
            if (this.mChangedScrap == null) goto L33;
            int r0 = this.mChangedScrap.size();
            if (r0 == 0) goto L33;
            int r1 = 0;
        L9:
            if (r1 >= r0) goto L18;
            ViewHolder r4 = this.mChangedScrap.get(r1);
            if (r4.wasReturnedFromScrap() == true) goto L16;
            if (r4.getLayoutPosition() != r11) goto L16;
            r4.addFlags(32);
            return r4;
        L16:
            r1 = r1 + 1;
            goto L9
        L18:
            if (this.this$0.mAdapter.hasStableIds() == false) goto L32;
            int r2 = this.this$0.mAdapterHelper.findPositionOffset(r11);
            if (r2 <= 0) goto L32;
            if (r2 >= this.this$0.mAdapter.getItemCount()) goto L32;
            long r5 = this.this$0.mAdapter.getItemId(r2);
            int r6 = 0;
        L24:
            if (r6 >= r0) goto L32;
            ViewHolder r7 = this.mChangedScrap.get(r6);
            if (r7.wasReturnedFromScrap() == true) goto L31;
            if (r7.getItemId() != r5) goto L31;
            r7.addFlags(32);
            return r7;
        L31:
            r6 = r6 + 1;
        L32:
            return null;
        L33:
            return null;
        }

        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool != null) goto L6;
            this.mRecyclerPool = new RecycledViewPool();
            maybeSendPoolingContainerAttach();
        L6:
            return this.mRecyclerPool;
        }

        int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        ViewHolder getScrapOrCachedViewForId(long r8, int r10, boolean r11) {
            int r1 = this.mAttachedScrap.size() - 1;
        L3:
            if (r1 < 0) goto L19;
            ViewHolder r2 = this.mAttachedScrap.get(r1);
            if (r2.getItemId() != r8) goto L18;
            if (r2.wasReturnedFromScrap() == true) goto L18;
            if (r10 == r2.getItemViewType()) goto L10;
            if (r11 == true) goto L18;
            this.mAttachedScrap.remove(r1);
            this.this$0.removeDetachedView(r2.itemView, false);
            quickRecycleScrapView(r2.itemView);
            goto L18
        L10:
            r2.addFlags(32);
            if (r2.isRemoved() == true) goto L13;
        L15:
            return r2;
        L13:
            if (this.this$0.mState.isPreLayout() == true) goto L15;
            r2.setFlags(2, 14);
        L18:
            r1 = r1 - 1;
            goto L3
        L19:
            int r3 = this.mCachedViews.size() - 1;
        L21:
            if (r3 < 0) goto L35;
            ViewHolder r4 = this.mCachedViews.get(r3);
            if (r4.getItemId() != r8) goto L34;
            if (r4.isAttachedToTransitionOverlay() == true) goto L34;
            if (r10 == r4.getItemViewType()) goto L28;
            if (r11 == true) goto L34;
            recycleCachedViewAt(r3);
            return null;
        L28:
            if (r11 == true) goto L30;
            this.mCachedViews.remove(r3);
        L30:
            return r4;
        L34:
            r3 = r3 - 1;
            goto L21
        L35:
            return null;
        }

        ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int r8, boolean r9) {
            int r0 = this.mAttachedScrap.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L17;
            ViewHolder r2 = this.mAttachedScrap.get(r1);
            if (r2.wasReturnedFromScrap() == true) goto L16;
            if (r2.getLayoutPosition() != r8) goto L16;
            if (r2.isInvalid() == true) goto L16;
            if (this.this$0.mState.mInPreLayout == true) goto L14;
            if (r2.isRemoved() == true) goto L16;
        L14:
            r2.addFlags(32);
            return r2;
        L16:
            r1 = r1 + 1;
            goto L3
        L17:
            if (r9 == true) goto L26;
            View r3 = this.this$0.mChildHelper.findHiddenNonRemovedView(r8);
            if (r3 == null) goto L26;
            ViewHolder r4 = RecyclerView.getChildViewHolderInt(r3);
            this.this$0.mChildHelper.unhide(r3);
            int r5 = this.this$0.mChildHelper.indexOfChild(r3);
            if (r5 == (-1)) goto L25;
            this.this$0.mChildHelper.detachViewFromParent(r5);
            scrapView(r3);
            r4.addFlags(8224);
            return r4;
        L25:
            throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + r4 + this.this$0.exceptionLabel());
        L26:
            int r6 = this.mCachedViews.size();
            int r7 = 0;
        L27:
            if (r7 >= r6) goto L38;
            ViewHolder r10 = this.mCachedViews.get(r7);
            if (r10.isInvalid() == true) goto L37;
            if (r10.getLayoutPosition() != r8) goto L37;
            if (r10.isAttachedToTransitionOverlay() == true) goto L37;
            if (r9 == true) goto L36;
            this.mCachedViews.remove(r7);
        L36:
            return r10;
        L37:
            r7 = r7 + 1;
            goto L27
        L38:
            return null;
        }

        View getScrapViewAt(int r2) {
            return this.mAttachedScrap.get(r2).itemView;
        }

        public View getViewForPosition(int r2) {
            return getViewForPosition(r2, false);
        }

        View getViewForPosition(int r3, boolean r4) {
            return tryGetViewHolderForPositionByDeadline(r3, r4, Long.MAX_VALUE).itemView;
        }

        void markItemDecorInsetsDirty() {
            int r0 = this.mCachedViews.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L8;
            LayoutParams r3 = (LayoutParams) this.mCachedViews.get(r1).itemView.getLayoutParams();
            if (r3 == null) goto L7;
            r3.mInsetsDirty = true;
        L7:
            r1 = r1 + 1;
            goto L3
        }

        void markKnownViewsInvalid() {
            int r0 = this.mCachedViews.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L9;
            ViewHolder r2 = this.mCachedViews.get(r1);
            if (r2 == null) goto L7;
            r2.addFlags(6);
            r2.addChangePayload(null);
        L7:
            r1 = r1 + 1;
            goto L3
        L9:
            if (this.this$0.mAdapter != null) goto L11;
        L12:
            recycleAndClearCachedViews();
            return;
        L11:
            if (this.this$0.mAdapter.hasStableIds() == false) goto L12;
        }

        void offsetPositionRecordsForInsert(int r5, int r6) {
            int r0 = this.mCachedViews.size();
            int r1 = 0;
        L3:
            if (r1 >= r0) goto L10;
            ViewHolder r2 = this.mCachedViews.get(r1);
            if (r2 == null) goto L9;
            if (r2.mPosition < r5) goto L9;
            r2.offsetPosition(r6, false);
        L9:
            r1 = r1 + 1;
            goto L3
        }

        void offsetPositionRecordsForMove(int r9, int r10) {
            if (r9 >= r10) goto L4;
            int r0 = r9;
            int r1 = r10;
            int r2 = -1;
        L5:
            int r3 = this.mCachedViews.size();
            int r4 = 0;
        L6:
            if (r4 >= r3) goto L19;
            ViewHolder r5 = this.mCachedViews.get(r4);
            if (r5 == null) goto L18;
            if (r5.mPosition < r0) goto L18;
            if (r5.mPosition > r1) goto L18;
            if (r5.mPosition != r9) goto L17;
            r5.offsetPosition(r10 - r9, false);
            goto L18
        L17:
            r5.offsetPosition(r2, false);
        L18:
            r4 = r4 + 1;
            goto L6
        L19:
            return;
        L4:
            r0 = r10;
            r1 = r9;
            r2 = 1;
            goto L5
        }

        void offsetPositionRecordsForRemove(int r6, int r7, boolean r8) {
            int r0 = r6 + r7;
            int r2 = this.mCachedViews.size() - 1;
        L3:
            if (r2 < 0) goto L13;
            ViewHolder r3 = this.mCachedViews.get(r2);
            if (r3 == null) goto L12;
            if (r3.mPosition < r0) goto L10;
            r3.offsetPosition(-r7, r8);
            goto L12
        L10:
            if (r3.mPosition < r6) goto L12;
            r3.addFlags(8);
            recycleCachedViewAt(r2);
        L12:
            r2 = r2 - 1;
            goto L3
        }

        void onAdapterChanged(Adapter<?> r2, Adapter<?> r3, boolean r4) {
            clear();
            poolingContainerDetach(r2, true);
            getRecycledViewPool().onAdapterChanged(r2, r3, r4);
            maybeSendPoolingContainerAttach();
        }

        void onAttachedToWindow() {
            maybeSendPoolingContainerAttach();
        }

        void onDetachedFromWindow() {
            int r0 = 0;
        L4:
            if (r0 >= this.mCachedViews.size()) goto L6;
            PoolingContainer.callPoolingContainerOnRelease(this.mCachedViews.get(r0).itemView);
            r0 = r0 + 1;
            goto L4
        L6:
            poolingContainerDetach(this.this$0.mAdapter);
        }

        void quickRecycleScrapView(View r3) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r3);
            r0.mScrapContainer = null;
            r0.mInChangeScrap = false;
            r0.clearReturnedFromScrapFlag();
            recycleViewHolderInternal(r0);
        }

        void recycleAndClearCachedViews() {
            int r1 = this.mCachedViews.size() - 1;
        L3:
            if (r1 < 0) goto L5;
            recycleCachedViewAt(r1);
            r1 = r1 - 1;
            goto L3
        L5:
            this.mCachedViews.clear();
            if (RecyclerView.ALLOW_THREAD_GAP_WORK == false) goto L10;
            this.this$0.mPrefetchRegistry.clearPrefetchPositions();
            return;
        }

        void recycleCachedViewAt(int r3) {
            addViewHolderToRecycledViewPool(this.mCachedViews.get(r3), true);
            this.mCachedViews.remove(r3);
        }

        public void recycleView(View r4) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r4);
            if (r0.isTmpDetached() == false) goto L6;
            this.this$0.removeDetachedView(r4, false);
        L6:
            if (r0.isScrap() == false) goto L9;
            r0.unScrap();
        L11:
            recycleViewHolderInternal(r0);
            if (this.this$0.mItemAnimator != null) goto L14;
            return;
        L14:
            if (r0.isRecyclable() == true) goto L18;
            this.this$0.mItemAnimator.endAnimation(r0);
            return;
        L18:
            return;
        L9:
            if (r0.wasReturnedFromScrap() == false) goto L11;
            r0.clearReturnedFromScrapFlag();
            goto L11
        }

        void recycleViewHolderInternal(ViewHolder r11) {
            boolean r1 = false;
            if (r11.isScrap() == false) goto L5;
        L55:
            StringBuilder r3 = new StringBuilder().append("Scrapped or attached views may not be recycled. isScrap:").append(r11.isScrap()).append(" isAttached:");
            if (r11.itemView.getParent() == null) goto L59;
            r1 = true;
        L59:
            throw new IllegalArgumentException(r3.append(r1).append(this.this$0.exceptionLabel()).toString());
        L5:
            if (r11.itemView.getParent() != null) goto L55;
            if (r11.isTmpDetached() == true) goto L54;
            if (r11.shouldIgnore() == true) goto L52;
            boolean r0 = r11.doesTransientStatePreventRecycling();
            if (this.this$0.mAdapter == null) goto L17;
            if (r0 == false) goto L17;
            if (this.this$0.mAdapter.onFailedToRecycleView(r11) == false) goto L17;
            boolean r4 = true;
        L18:
            boolean r5 = false;
            boolean r6 = false;
            if (r4 == true) goto L23;
            if (r11.isRecyclable() == true) goto L23;
        L45:
            this.this$0.mViewInfoStore.removeViewHolder(r11);
            if (r5 == true) goto L62;
            if (r6 == true) goto L63;
            if (r0 == false) goto L64;
            PoolingContainer.callPoolingContainerOnRelease(r11.itemView);
            r11.mBindingAdapter = null;
            r11.mOwnerRecyclerView = null;
            return;
        L64:
            return;
        L63:
            return;
        L62:
            return;
        L23:
            if (this.mViewCacheMax > 0) goto L25;
        L43:
            if (r5 == true) goto L45;
            addViewHolderToRecycledViewPool(r11, true);
            r6 = true;
            goto L45
        L25:
            if (r11.hasAnyOfTheFlags(526) == true) goto L43;
            int r7 = this.mCachedViews.size();
            if (r7 < this.mViewCacheMax) goto L30;
            if (r7 <= 0) goto L30;
            recycleCachedViewAt(0);
            r7 = r7 - 1;
        L30:
            int r2 = r7;
            if (RecyclerView.ALLOW_THREAD_GAP_WORK == false) goto L42;
            if (r7 <= 0) goto L42;
            if (this.this$0.mPrefetchRegistry.lastPrefetchIncludedPosition(r11.mPosition) == true) goto L42;
            int r8 = r7 - 1;
        L36:
            if (r8 < 0) goto L41;
            if (this.this$0.mPrefetchRegistry.lastPrefetchIncludedPosition(this.mCachedViews.get(r8).mPosition) == false) goto L41;
            r8 = r8 - 1;
        L41:
            r2 = r8 + 1;
        L42:
            this.mCachedViews.add(r2, r11);
            r5 = true;
        L17:
            r4 = false;
            goto L18
        L52:
            throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle." + this.this$0.exceptionLabel());
        L54:
            throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + r11 + this.this$0.exceptionLabel());
        }

        void scrapView(View r5) {
            ViewHolder r0 = RecyclerView.getChildViewHolderInt(r5);
            if (r0.hasAnyOfTheFlags(12) == true) goto L14;
            if (r0.isUpdated() == false) goto L14;
            if (this.this$0.canReuseUpdatedViewHolder(r0) == true) goto L14;
            if (this.mChangedScrap != null) goto L12;
            this.mChangedScrap = new ArrayList();
        L12:
            r0.setScrapContainer(this, true);
            this.mChangedScrap.add(r0);
            return;
        L14:
            if (r0.isInvalid() == true) goto L16;
        L22:
            r0.setScrapContainer(this, false);
            this.mAttachedScrap.add(r0);
            return;
        L16:
            if (r0.isRemoved() == true) goto L22;
            if (this.this$0.mAdapter.hasStableIds() == true) goto L22;
            throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool." + this.this$0.exceptionLabel());
        }

        void setRecycledViewPool(RecycledViewPool r2) {
            poolingContainerDetach(this.this$0.mAdapter);
            if (this.mRecyclerPool == null) goto L5;
            this.mRecyclerPool.detach();
        L5:
            this.mRecyclerPool = r2;
            if (this.mRecyclerPool != null) goto L8;
        L10:
            maybeSendPoolingContainerAttach();
            return;
        L8:
            if (this.this$0.getAdapter() == null) goto L10;
            this.mRecyclerPool.attach();
            goto L10
        }

        void setViewCacheExtension(ViewCacheExtension r1) {
            this.mViewCacheExtension = r1;
        }

        public void setViewCacheSize(int r1) {
            this.mRequestedCacheMax = r1;
            updateViewCacheSize();
        }

        ViewHolder tryGetViewHolderForPositionByDeadline(int r17, boolean r18, long r19) {
            if (r17 < 0) goto L111;
            if (r17 >= this.this$0.mState.getItemCount()) goto L111;
            boolean r1 = false;
            ViewHolder r2 = null;
            if (this.this$0.mState.isPreLayout() == false) goto L13;
            r2 = getChangedScrapViewForPosition(r17);
            if (r2 == null) goto L11;
            boolean r4 = true;
        L12:
            r1 = r4;
            goto L13
        L11:
            r4 = false;
        L13:
            if (r2 != null) goto L28;
            r2 = getScrapOrHiddenOrCachedHolderForPosition(r17, r18);
            if (r2 == null) goto L28;
            if (validateViewHolderForOffsetPosition(r2) == true) goto L27;
            if (r18 == true) goto L26;
            r2.addFlags(4);
            if (r2.isScrap() == false) goto L23;
            this.this$0.removeDetachedView(r2.itemView, false);
            r2.unScrap();
        L25:
            recycleViewHolderInternal(r2);
            goto L26
        L23:
            if (r2.wasReturnedFromScrap() == false) goto L25;
            r2.clearReturnedFromScrapFlag();
        L26:
            r2 = null;
            goto L28
        L27:
            r1 = true;
        L28:
            if (r2 != null) goto L74;
            int r5 = this.this$0.mAdapterHelper.findPositionOffset(r17);
            if (r5 < 0) goto L73;
            if (r5 >= this.this$0.mAdapter.getItemCount()) goto L73;
            int r10 = this.this$0.mAdapter.getItemViewType(r5);
            if (this.this$0.mAdapter.hasStableIds() == false) goto L38;
            r2 = getScrapOrCachedViewForId(this.this$0.mAdapter.getItemId(r5), r10, r18);
            if (r2 == null) goto L38;
            r2.mPosition = r5;
            r1 = true;
        L38:
            if (r2 == null) goto L40;
        L52:
            if (r2 != null) goto L58;
            r2 = getRecycledViewPool().getRecycledView(r10);
            if (r2 == null) goto L58;
            r2.resetInternal();
            if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST == false) goto L58;
            invalidateDisplayListInt(r2);
        L58:
            if (r2 != null) goto L71;
            long r11 = this.this$0.getNanoTime();
            if (r19 != Long.MAX_VALUE) goto L62;
        L65:
            ViewHolder r3 = this.this$0.mAdapter.createViewHolder(this.this$0, r10);
            if (RecyclerView.ALLOW_THREAD_GAP_WORK == false) goto L70;
            RecyclerView r6 = RecyclerView.findNestedRecyclerView(r3.itemView);
            if (r6 == null) goto L70;
            r3.mNestedRecyclerView = new WeakReference(r6);
        L70:
            this.mRecyclerPool.factorInCreateTime(r10, this.this$0.getNanoTime() - r11);
            boolean r7 = r1;
            ViewHolder r8 = r3;
        L75:
            if (r7 == true) goto L77;
        L84:
            boolean r15 = false;
        L85:
            boolean r9 = false;
            if (this.this$0.mState.isPreLayout() == false) goto L91;
            if (r8.isBound() == false) goto L91;
            r8.mPreLayoutPosition = r17;
        L97:
            ViewGroup.LayoutParams r12 = r8.itemView.getLayoutParams();
            if (r12 != null) goto L101;
            LayoutParams r13 = (LayoutParams) this.this$0.generateDefaultLayoutParams();
            r8.itemView.setLayoutParams(r13);
        L104:
            r13.mViewHolder = r8;
            if (r7 == false) goto L108;
            if (r9 == false) goto L108;
            r15 = true;
        L108:
            r13.mPendingInvalidate = r15;
            return r8;
        L101:
            if (this.this$0.checkLayoutParams(r12) == true) goto L103;
            r13 = (LayoutParams) this.this$0.generateLayoutParams(r12);
            r8.itemView.setLayoutParams(r13);
            goto L104
        L103:
            r13 = (LayoutParams) r12;
        L91:
            if (r8.isBound() == true) goto L93;
        L96:
            r9 = tryBindViewHolderByDeadline(r8, this.this$0.mAdapterHelper.findPositionOffset(r17), r17, r19);
            goto L97
        L93:
            if (r8.needsUpdate() == true) goto L96;
            if (r8.isInvalid() == false) goto L97;
        L77:
            if (this.this$0.mState.isPreLayout() == true) goto L84;
            if (r8.hasAnyOfTheFlags(8192) == false) goto L83;
            r15 = false;
            r8.setFlags(0, 8192);
            if (this.this$0.mState.mRunSimpleAnimations == false) goto L85;
            this.this$0.recordAnimationInfoIfBouncedHiddenView(r8, this.this$0.mItemAnimator.recordPreLayoutInformation(this.this$0.mState, r8, ItemAnimator.buildAdapterChangeFlagsForAnimations(r8) | 4096, r8.getUnmodifiedPayloads()));
            goto L85
        L83:
            r15 = false;
            goto L85
        L62:
            if (this.mRecyclerPool.willCreateInTime(r10, r11, r19) == true) goto L65;
            return null;
        L71:
            r7 = r1;
            r8 = r2;
            goto L75
        L40:
            if (this.mViewCacheExtension == null) goto L52;
            View r14 = this.mViewCacheExtension.getViewForPositionAndType(this, r17, r10);
            if (r14 == null) goto L52;
            r2 = this.this$0.getChildViewHolder(r14);
            if (r2 == null) goto L51;
            if (r2.shouldIgnore() == false) goto L52;
            throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view." + this.this$0.exceptionLabel());
        L51:
            throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder" + this.this$0.exceptionLabel());
        L73:
            throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + r17 + "(offset:" + r5 + ").state:" + this.this$0.mState.getItemCount() + this.this$0.exceptionLabel());
        L74:
            r7 = r1;
            r8 = r2;
        L111:
            throw new IndexOutOfBoundsException("Invalid item position " + r17 + "(" + r17 + "). Item count:" + this.this$0.mState.getItemCount() + this.this$0.exceptionLabel());
        }

        void unscrapView(ViewHolder r2) {
            if (r2.mInChangeScrap == false) goto L5;
            this.mChangedScrap.remove(r2);
        L6:
            r2.mScrapContainer = null;
            r2.mInChangeScrap = false;
            r2.clearReturnedFromScrapFlag();
            return;
        L5:
            this.mAttachedScrap.remove(r2);
            goto L6
        }

        void updateViewCacheSize() {
            if (this.this$0.mLayout == null) goto L5;
            int r0 = this.this$0.mLayout.mPrefetchMaxCountObserved;
        L6:
            this.mViewCacheMax = this.mRequestedCacheMax + r0;
            int r1 = this.mCachedViews.size() - 1;
        L7:
            if (r1 < 0) goto L11;
            if (this.mCachedViews.size() <= this.mViewCacheMax) goto L14;
            recycleCachedViewAt(r1);
            r1 = r1 - 1;
            goto L7
        L14:
            return;
        L11:
            return;
        L5:
            r0 = 0;
            goto L6
        }

        boolean validateViewHolderForOffsetPosition(ViewHolder r8) {
            if (r8.isRemoved() == false) goto L7;
            return this.this$0.mState.isPreLayout();
        L7:
            if (r8.mPosition < 0) goto L23;
            if (r8.mPosition >= this.this$0.mAdapter.getItemCount()) goto L23;
            if (this.this$0.mState.isPreLayout() == true) goto L16;
            if (this.this$0.mAdapter.getItemViewType(r8.mPosition) == r8.getItemViewType()) goto L16;
            return false;
        L16:
            if (this.this$0.mAdapter.hasStableIds() == true) goto L18;
            return true;
        L18:
            if (r8.getItemId() == this.this$0.mAdapter.getItemId(r8.mPosition)) goto L20;
            return false;
        L20:
            return true;
        L23:
            throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + r8 + this.this$0.exceptionLabel());
        }

        void viewRangeUpdate(int r7, int r8) {
            int r0 = r7 + r8;
            int r2 = this.mCachedViews.size() - 1;
        L3:
            if (r2 < 0) goto L12;
            ViewHolder r3 = this.mCachedViews.get(r2);
            if (r3 == null) goto L11;
            int r4 = r3.mPosition;
            if (r4 < r7) goto L11;
            if (r4 >= r0) goto L11;
            r3.addFlags(2);
            recycleCachedViewAt(r2);
        L11:
            r2 = r2 - 1;
            goto L3
        }
    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder r1);
    }

    private class RecyclerViewDataObserver extends AdapterDataObserver {
        final /* synthetic */ RecyclerView this$0;

        RecyclerViewDataObserver(RecyclerView r1) {
            this.this$0 = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            this.this$0.assertNotInLayoutOrScroll(null);
            this.this$0.mState.mStructureChanged = true;
            this.this$0.processDataSetCompletelyChanged(true);
            if (this.this$0.mAdapterHelper.hasPendingUpdates() == true) goto L6;
            this.this$0.requestLayout();
            return;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int r3, int r4, Object r5) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeChanged(r3, r4, r5) == false) goto L6;
            triggerUpdateProcessor();
            return;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int r3, int r4) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeInserted(r3, r4) == false) goto L6;
            triggerUpdateProcessor();
            return;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int r3, int r4, int r5) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeMoved(r3, r4, r5) == false) goto L6;
            triggerUpdateProcessor();
            return;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int r3, int r4) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeRemoved(r3, r4) == false) goto L6;
            triggerUpdateProcessor();
            return;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onStateRestorationPolicyChanged() {
            if (this.this$0.mPendingSavedState != null) goto L5;
            return;
        L5:
            Adapter r0 = this.this$0.mAdapter;
            if (r0 != null) goto L8;
            return;
        L8:
            if (r0.canRestoreState() == false) goto L12;
            this.this$0.requestLayout();
            return;
        }

        void triggerUpdateProcessor() {
            if (RecyclerView.POST_UPDATES_ON_ANIMATION == true) goto L5;
        L9:
            this.this$0.mAdapterUpdateDuringMeasure = true;
            this.this$0.requestLayout();
            return;
        L5:
            if (this.this$0.mHasFixedSize == false) goto L9;
            if (this.this$0.mIsAttached == false) goto L9;
            ViewCompat.postOnAnimation(this.this$0, this.this$0.mUpdateChildViewsRunnable);
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = null;
        Parcelable mLayoutState;

        static {
            CREATOR = new C05131();
        }

        SavedState(Parcel r2, ClassLoader r3) {
            super(r2, r3);
            if (r3 == null) goto L5;
            ClassLoader r0 = r3;
        L6:
            this.mLayoutState = r2.readParcelable(r0);
            return;
        L5:
            r0 = LayoutManager.class.getClassLoader();
            goto L6
        }

        SavedState(Parcelable r1) {
            super(r1);
        }

        void copyFrom(SavedState r2) {
            this.mLayoutState = r2.mLayoutState;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel r3, int r4) {
            super.writeToParcel(r3, r4);
            r3.writeParcelable(this.mLayoutState, 0);
        }
    }

    public static class SimpleOnItemTouchListener implements OnItemTouchListener {
        public SimpleOnItemTouchListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public boolean onInterceptTouchEvent(RecyclerView r2, MotionEvent r3) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public void onRequestDisallowInterceptTouchEvent(boolean r1) {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public void onTouchEvent(RecyclerView r1, MotionEvent r2) {
        }
    }

    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction;
        private boolean mRunning;
        private boolean mStarted;
        private int mTargetPosition;
        private View mTargetView;

        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean mChanged;
            private int mConsecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;

            public Action(int r3, int r4) {
                this(r3, r4, Integer.MIN_VALUE, null);
            }

            public Action(int r2, int r3, int r4) {
                this(r2, r3, r4, null);
            }

            public Action(int r2, int r3, int r4, Interpolator r5) {
                this.mJumpToPosition = -1;
                this.mChanged = false;
                this.mConsecutiveUpdates = 0;
                this.mDx = r2;
                this.mDy = r3;
                this.mDuration = r4;
                this.mInterpolator = r5;
            }

            private void validate() {
                if (this.mInterpolator == null) goto L10;
                if (this.mDuration >= 1) goto L10;
                throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
            L10:
                if (this.mDuration < 1) goto L13;
                return;
            L13:
                throw new IllegalStateException("Scroll duration must be a positive number");
            }

            public int getDuration() {
                return this.mDuration;
            }

            public int getDx() {
                return this.mDx;
            }

            public int getDy() {
                return this.mDy;
            }

            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }

            boolean hasJumpTarget() {
                if (this.mJumpToPosition < 0) goto L5;
                return true;
            L5:
                return false;
            }

            public void jumpTo(int r1) {
                this.mJumpToPosition = r1;
            }

            void runIfNecessary(RecyclerView r7) {
                if (this.mJumpToPosition < 0) goto L7;
                int r0 = this.mJumpToPosition;
                this.mJumpToPosition = -1;
                r7.jumpToPositionForSmoothScroller(r0);
                this.mChanged = false;
                return;
            L7:
                if (this.mChanged == false) goto L12;
                validate();
                r7.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                this.mConsecutiveUpdates++;
                if (this.mConsecutiveUpdates <= 10) goto L11;
                Log.e(RecyclerView.TAG, "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
            L11:
                this.mChanged = false;
                return;
            L12:
                this.mConsecutiveUpdates = 0;
            }

            public void setDuration(int r2) {
                this.mChanged = true;
                this.mDuration = r2;
            }

            public void setDx(int r2) {
                this.mChanged = true;
                this.mDx = r2;
            }

            public void setDy(int r2) {
                this.mChanged = true;
                this.mDy = r2;
            }

            public void setInterpolator(Interpolator r2) {
                this.mChanged = true;
                this.mInterpolator = r2;
            }

            public void update(int r2, int r3, int r4, Interpolator r5) {
                this.mDx = r2;
                this.mDy = r3;
                this.mDuration = r4;
                this.mInterpolator = r5;
                this.mChanged = true;
            }
        }

        public interface ScrollVectorProvider {
            PointF computeScrollVectorForPosition(int r1);
        }

        public SmoothScroller() {
            this.mTargetPosition = -1;
            this.mRecyclingAction = new Action(0, 0);
        }

        public PointF computeScrollVectorForPosition(int r4) {
            Object r0 = getLayoutManager();
            if ((r0 instanceof ScrollVectorProvider) == true) goto L5;
            Log.w(RecyclerView.TAG, "You should override computeScrollVectorForPosition when the LayoutManager does not implement " + ScrollVectorProvider.class.getCanonicalName());
            return null;
        L5:
            return ((ScrollVectorProvider) r0).computeScrollVectorForPosition(r4);
        }

        public View findViewByPosition(int r2) {
            return this.mRecyclerView.mLayout.findViewByPosition(r2);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public int getChildPosition(View r2) {
            return this.mRecyclerView.getChildLayoutPosition(r2);
        }

        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        @Deprecated
        public void instantScrollToPosition(int r2) {
            this.mRecyclerView.scrollToPosition(r2);
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        protected void normalize(PointF r4) {
            float r0 = (float) Math.sqrt((r4.x * r4.x) + (r4.y * r4.y));
            r4.x /= r0;
            r4.y /= r0;
        }

        void onAnimation(int r6, int r7) {
            RecyclerView r0 = this.mRecyclerView;
            if (this.mTargetPosition == (-1)) goto L5;
            if (r0 == null) goto L5;
        L7:
            if (this.mPendingInitialRun == true) goto L9;
        L19:
            this.mPendingInitialRun = false;
            if (this.mTargetView == null) goto L26;
            if (getChildPosition(this.mTargetView) != this.mTargetPosition) goto L24;
            onTargetFound(this.mTargetView, r0.mState, this.mRecyclingAction);
            this.mRecyclingAction.runIfNecessary(r0);
            stop();
            goto L26
        L24:
            Log.e(RecyclerView.TAG, "Passed over target position while smooth scrolling.");
            this.mTargetView = null;
        L26:
            if (this.mRunning == false) goto L33;
            onSeekTargetStep(r6, r7, r0.mState, this.mRecyclingAction);
            boolean r1 = this.mRecyclingAction.hasJumpTarget();
            this.mRecyclingAction.runIfNecessary(r0);
            if (r1 == true) goto L30;
            return;
        L30:
            if (this.mRunning == false) goto L35;
            this.mPendingInitialRun = true;
            r0.mViewFlinger.postOnAnimation();
            return;
        L35:
            return;
        L33:
            return;
        L9:
            if (this.mTargetView != null) goto L19;
            if (this.mLayoutManager == null) goto L19;
            PointF r2 = computeScrollVectorForPosition(this.mTargetPosition);
            if (r2 == null) goto L19;
            if (r2.x == 0.0f) goto L17;
        L18:
            r0.scrollStep((int) Math.signum(r2.x), (int) Math.signum(r2.y), null);
            goto L19
        L17:
            if (r2.y == 0.0f) goto L19;
        L5:
            stop();
            goto L7
        }

        protected void onChildAttachedToWindow(View r3) {
            if (getChildPosition(r3) != getTargetPosition()) goto L6;
            this.mTargetView = r3;
            return;
        }

        protected abstract void onSeekTargetStep(int r1, int r2, State r3, Action r4);

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onTargetFound(View r1, State r2, Action r3);

        public void setTargetPosition(int r1) {
            this.mTargetPosition = r1;
        }

        void start(RecyclerView r3, LayoutManager r4) {
            r3.mViewFlinger.stop();
            if (this.mStarted == false) goto L5;
            Log.w(RecyclerView.TAG, "An instance of " + getClass().getSimpleName() + " was started more than once. Each instance of" + getClass().getSimpleName() + " is intended to only be used once. You should create a new instance for each use.");
        L5:
            this.mRecyclerView = r3;
            this.mLayoutManager = r4;
            if (this.mTargetPosition == (-1)) goto L10;
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = findViewByPosition(getTargetPosition());
            onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
            this.mStarted = true;
            return;
        L10:
            throw new IllegalArgumentException("Invalid target position");
        }

        protected final void stop() {
            if (this.mRunning == true) goto L5;
            return;
        L5:
            this.mRunning = false;
            onStop();
            this.mRecyclerView.mState.mTargetPosition = -1;
            this.mTargetView = null;
            this.mTargetPosition = -1;
            this.mPendingInitialRun = false;
            this.mLayoutManager.onSmoothScrollerStopped(this);
            this.mLayoutManager = null;
            this.mRecyclerView = null;
        }
    }

    public static class State {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout;
        boolean mIsMeasuring;
        int mItemCount;
        int mLayoutStep;
        int mPreviousLayoutItemCount;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations;
        boolean mRunSimpleAnimations;
        boolean mStructureChanged;
        int mTargetPosition;
        boolean mTrackOldChangeHolders;

        public State() {
            this.mTargetPosition = -1;
            this.mPreviousLayoutItemCount = 0;
            this.mDeletedInvisibleItemCountSincePreviousLayout = 0;
            this.mLayoutStep = 1;
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
            this.mRunSimpleAnimations = false;
            this.mRunPredictiveAnimations = false;
        }

        void assertLayoutStep(int r4) {
            if ((this.mLayoutStep & r4) == 0) goto L6;
            return;
        L6:
            throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(r4) + " but it is " + Integer.toBinaryString(this.mLayoutStep));
        }

        public boolean didStructureChange() {
            return this.mStructureChanged;
        }

        public <T> T get(int r2) {
            if (this.mData != null) goto L7;
            return null;
        L7:
            return (T) this.mData.get(r2);
        }

        public int getItemCount() {
            if (this.mInPreLayout == false) goto L6;
            return this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
        L6:
            return this.mItemCount;
        }

        public int getRemainingScrollHorizontal() {
            return this.mRemainingScrollHorizontal;
        }

        public int getRemainingScrollVertical() {
            return this.mRemainingScrollVertical;
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            if (this.mTargetPosition == (-1)) goto L5;
            return true;
        L5:
            return false;
        }

        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        void prepareForNestedPrefetch(Adapter r2) {
            this.mLayoutStep = 1;
            this.mItemCount = r2.getItemCount();
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }

        public void put(int r2, Object r3) {
            if (this.mData != null) goto L5;
            this.mData = new SparseArray();
        L5:
            this.mData.put(r2, r3);
        }

        public void remove(int r2) {
            if (this.mData != null) goto L5;
            return;
        L5:
            this.mData.remove(r2);
        }

        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mIsMeasuring=" + this.mIsMeasuring + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }
    }

    static class StretchEdgeEffectFactory extends EdgeEffectFactory {
        StretchEdgeEffectFactory() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory
        protected EdgeEffect createEdgeEffect(RecyclerView r3, int r4) {
            return new EdgeEffect(r3.getContext());
        }
    }

    public static abstract class ViewCacheExtension {
        public ViewCacheExtension() {
        }

        public abstract View getViewForPositionAndType(Recycler r1, int r2, int r3);
    }

    class ViewFlinger implements Runnable {
        private boolean mEatRunOnAnimationRequest;
        Interpolator mInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        OverScroller mOverScroller;
        private boolean mReSchedulePostAnimationCallback;
        final /* synthetic */ RecyclerView this$0;

        ViewFlinger(RecyclerView r4) {
            this.this$0 = r4;
            this.mInterpolator = RecyclerView.sQuinticInterpolator;
            this.mEatRunOnAnimationRequest = false;
            this.mReSchedulePostAnimationCallback = false;
            this.mOverScroller = new OverScroller(r4.getContext(), RecyclerView.sQuinticInterpolator);
        }

        private int computeScrollDuration(int r8, int r9) {
            int r0 = Math.abs(r8);
            int r1 = Math.abs(r9);
            if (r0 <= r1) goto L5;
            boolean r2 = true;
        L6:
            RecyclerView r3 = this.this$0;
            if (r2 == false) goto L9;
            int r4 = r3.getWidth();
        L10:
            if (r2 == false) goto L12;
            int r5 = r0;
        L14:
            return Math.min((int) (((r5 / r4) + 1.0f) * 300.0f), 2000);
        L12:
            r5 = r1;
            goto L14
        L9:
            r4 = r3.getHeight();
            goto L10
        L5:
            r2 = false;
            goto L6
        }

        private void internalPostOnAnimation() {
            this.this$0.removeCallbacks(this);
            ViewCompat.postOnAnimation(this.this$0, this);
        }

        public void fling(int r13, int r14) {
            this.this$0.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            if (this.mInterpolator == RecyclerView.sQuinticInterpolator) goto L5;
            this.mInterpolator = RecyclerView.sQuinticInterpolator;
            this.mOverScroller = new OverScroller(this.this$0.getContext(), RecyclerView.sQuinticInterpolator);
        L5:
            this.mOverScroller.fling(0, 0, r13, r14, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest == false) goto L5;
            this.mReSchedulePostAnimationCallback = true;
            return;
        L5:
            internalPostOnAnimation();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.this$0.mLayout != null) goto L6;
            stop();
            return;
        L6:
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
            this.this$0.consumePendingUpdateOperations();
            OverScroller r3 = this.mOverScroller;
            if (r3.computeScrollOffset() == false) goto L86;
            int r4 = r3.getCurrX();
            int r5 = r3.getCurrY();
            int r6 = r4 - this.mLastFlingX;
            int r7 = r5 - this.mLastFlingY;
            this.mLastFlingX = r4;
            this.mLastFlingY = r5;
            int r10 = this.this$0.consumeFlingInHorizontalStretch(r6);
            int r11 = this.this$0.consumeFlingInVerticalStretch(r7);
            this.this$0.mReusableIntPair[0] = 0;
            this.this$0.mReusableIntPair[1] = 0;
            if (this.this$0.dispatchNestedPreScroll(r10, r11, this.this$0.mReusableIntPair, null, 1) == false) goto L12;
            r10 = r10 - this.this$0.mReusableIntPair[0];
            r11 = r11 - this.this$0.mReusableIntPair[1];
        L12:
            if (this.this$0.getOverScrollMode() == 2) goto L15;
            this.this$0.considerReleasingGlowsOnScroll(r10, r11);
        L15:
            if (this.this$0.mAdapter == null) goto L30;
            this.this$0.mReusableIntPair[0] = 0;
            this.this$0.mReusableIntPair[1] = 0;
            this.this$0.scrollStep(r10, r11, this.this$0.mReusableIntPair);
            int r8 = this.this$0.mReusableIntPair[0];
            int r9 = this.this$0.mReusableIntPair[1];
            int r12 = r10 - r8;
            int r13 = r11 - r9;
            SmoothScroller r14 = this.this$0.mLayout.mSmoothScroller;
            if (r14 != null) goto L19;
        L29:
            int r15 = r8;
            int r16 = r9;
            int r17 = r12;
            int r18 = r13;
        L32:
            if (this.this$0.mItemDecorations.isEmpty() == true) goto L34;
            this.this$0.invalidate();
        L34:
            this.this$0.mReusableIntPair[0] = 0;
            this.this$0.mReusableIntPair[1] = 0;
            this.this$0.dispatchNestedScroll(r15, r16, r17, r18, null, 1, this.this$0.mReusableIntPair);
            int r19 = r16;
            int r110 = r17 - this.this$0.mReusableIntPair[0];
            int r111 = r18 - this.this$0.mReusableIntPair[1];
            if (r15 != 0) goto L37;
            if (r19 != 0) goto L37;
        L39:
            if (RecyclerView.access$200(this.this$0) == true) goto L42;
            this.this$0.invalidate();
        L42:
            if (r3.getCurrX() != r3.getFinalX()) goto L44;
            boolean r20 = true;
        L46:
            if (r3.getCurrY() != r3.getFinalY()) goto L48;
            boolean r21 = true;
        L50:
            if (r3.isFinished() == true) goto L57;
            if (r20 == true) goto L53;
            if (r110 != 0) goto L53;
        L56:
            boolean r112 = false;
        L58:
            SmoothScroller r113 = this.this$0.mLayout.mSmoothScroller;
            if (r113 != null) goto L61;
        L63:
            boolean r114 = false;
        L64:
            if (r114 == true) goto L83;
            if (r112 == false) goto L83;
            if (this.this$0.getOverScrollMode() == 2) goto L81;
            int r22 = (int) r3.getCurrVelocity();
            if (r110 >= 0) goto L71;
            int r115 = -r22;
        L74:
            if (r111 >= 0) goto L76;
            int r116 = -r22;
        L79:
            this.this$0.absorbGlows(r115, r116);
            goto L81
        L76:
            if (r111 <= 0) goto L78;
            r116 = r22;
            goto L79
        L78:
            r116 = 0;
            goto L79
        L71:
            if (r110 <= 0) goto L73;
            r115 = r22;
            goto L74
        L73:
            r115 = 0;
        L81:
            if (RecyclerView.ALLOW_THREAD_GAP_WORK == false) goto L86;
            this.this$0.mPrefetchRegistry.clearPrefetchPositions();
        L83:
            postOnAnimation();
            if (this.this$0.mGapWorker == null) goto L86;
            this.this$0.mGapWorker.postFromTraversal(this.this$0, r15, r19);
            goto L86
        L61:
            if (r113.isPendingInitialRun() == false) goto L63;
            r114 = true;
        L53:
            if (r21 == true) goto L57;
            if (r111 == 0) goto L56;
        L57:
            r112 = true;
            goto L58
        L48:
            r21 = false;
            goto L50
        L44:
            r20 = false;
        L37:
            this.this$0.dispatchOnScrolled(r15, r19);
            goto L39
        L19:
            if (r14.isPendingInitialRun() == true) goto L29;
            if (r14.isRunning() == false) goto L29;
            int r117 = this.this$0.mState.getItemCount();
            if (r117 != 0) goto L26;
            r14.stop();
            goto L29
        L26:
            if (r14.getTargetPosition() < r117) goto L28;
            r14.setTargetPosition(r117 - 1);
            r14.onAnimation(r8, r9);
            goto L29
        L28:
            r14.onAnimation(r8, r9);
            goto L29
        L30:
            r15 = 0;
            r16 = 0;
            r17 = r10;
            r18 = r11;
        L86:
            SmoothScroller r2 = this.this$0.mLayout.mSmoothScroller;
            if (r2 != null) goto L89;
        L91:
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback == false) goto L94;
            internalPostOnAnimation();
            return;
        L94:
            this.this$0.setScrollState(0);
            this.this$0.stopNestedScroll(1);
            return;
        L89:
            if (r2.isPendingInitialRun() == false) goto L91;
            r2.onAnimation(0, 0);
            goto L91
        }

        public void smoothScrollBy(int r7, int r8, int r9, Interpolator r10) {
            if (r9 != Integer.MIN_VALUE) goto L5;
            int r5 = computeScrollDuration(r7, r8);
        L6:
            if (r10 != null) goto L9;
            r10 = RecyclerView.sQuinticInterpolator;
        L9:
            if (this.mInterpolator == r10) goto L11;
            this.mInterpolator = r10;
            this.mOverScroller = new OverScroller(this.this$0.getContext(), r10);
        L11:
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.this$0.setScrollState(2);
            this.mOverScroller.startScroll(0, 0, r7, r8, r5);
            postOnAnimation();
            return;
        L5:
            r5 = r9;
            goto L6
        }

        public void stop() {
            this.this$0.removeCallbacks(this);
            this.mOverScroller.abortAnimation();
        }
    }

    public static abstract class ViewHolder {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS = null;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        public final View itemView;
        Adapter<? extends ViewHolder> mBindingAdapter;
        int mFlags;
        boolean mInChangeScrap;
        private int mIsRecyclableCount;
        long mItemId;
        int mItemViewType;
        WeakReference<RecyclerView> mNestedRecyclerView;
        int mOldPosition;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads;
        int mPendingAccessibilityState;
        int mPosition;
        int mPreLayoutPosition;
        Recycler mScrapContainer;
        ViewHolder mShadowedHolder;
        ViewHolder mShadowingHolder;
        List<Object> mUnmodifiedPayloads;
        private int mWasImportantForAccessibilityBeforeHidden;

        static {
            FULLUPDATE_PAYLOADS = Collections.emptyList();
        }

        public ViewHolder(View r4) {
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1;
            this.mItemViewType = -1;
            this.mPreLayoutPosition = -1;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.mPayloads = null;
            this.mUnmodifiedPayloads = null;
            this.mIsRecyclableCount = 0;
            this.mScrapContainer = null;
            this.mInChangeScrap = false;
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            if (r4 == null) goto L7;
            this.itemView = r4;
            return;
        L7:
            throw new IllegalArgumentException("itemView may not be null");
        }

        private void createPayloadsIfNeeded() {
            if (this.mPayloads != null) goto L6;
            this.mPayloads = new ArrayList();
            this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
            return;
        }

        void addChangePayload(Object r3) {
            if (r3 != null) goto L6;
            addFlags(1024);
            return;
        L6:
            if ((1024 & this.mFlags) != 0) goto L10;
            createPayloadsIfNeeded();
            this.mPayloads.add(r3);
            return;
        }

        void addFlags(int r2) {
            this.mFlags |= r2;
        }

        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        void clearPayload() {
            if (this.mPayloads == null) goto L5;
            this.mPayloads.clear();
        L5:
            this.mFlags &= -1025;
        }

        void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        boolean doesTransientStatePreventRecycling() {
            if ((this.mFlags & 16) == 0) goto L5;
        L7:
            return false;
        L5:
            if (ViewCompat.hasTransientState(this.itemView) == false) goto L7;
            return true;
        }

        void flagRemovedAndOffsetPosition(int r2, int r3, boolean r4) {
            addFlags(8);
            offsetPosition(r3, r4);
            this.mPosition = r2;
        }

        public final int getAbsoluteAdapterPosition() {
            if (this.mOwnerRecyclerView != null) goto L7;
            return -1;
        L7:
            return this.mOwnerRecyclerView.getAdapterPositionInRecyclerView(this);
        }

        @Deprecated
        public final int getAdapterPosition() {
            return getBindingAdapterPosition();
        }

        public final Adapter<? extends ViewHolder> getBindingAdapter() {
            return this.mBindingAdapter;
        }

        public final int getBindingAdapterPosition() {
            if (this.mBindingAdapter != null) goto L6;
            return -1;
        L6:
            if (this.mOwnerRecyclerView != null) goto L8;
            return -1;
        L8:
            Adapter r0 = this.mOwnerRecyclerView.getAdapter();
            if (r0 != null) goto L11;
            return -1;
        L11:
            int r2 = this.mOwnerRecyclerView.getAdapterPositionInRecyclerView(this);
            if (r2 != (-1)) goto L15;
            return -1;
        L15:
            return r0.findRelativeAdapterPositionIn(this.mBindingAdapter, this, r2);
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        public final int getLayoutPosition() {
            if (this.mPreLayoutPosition != (-1)) goto L6;
            return this.mPosition;
        L6:
            return this.mPreLayoutPosition;
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        @Deprecated
        public final int getPosition() {
            if (this.mPreLayoutPosition != (-1)) goto L6;
            return this.mPosition;
        L6:
            return this.mPreLayoutPosition;
        }

        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) != 0) goto L14;
            if (this.mPayloads == null) goto L12;
            if (this.mPayloads.size() == 0) goto L12;
            return this.mUnmodifiedPayloads;
        L12:
            return FULLUPDATE_PAYLOADS;
        L14:
            return FULLUPDATE_PAYLOADS;
        }

        boolean hasAnyOfTheFlags(int r2) {
            if ((this.mFlags & r2) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        boolean isAdapterPositionUnknown() {
            if ((this.mFlags & 512) == 0) goto L5;
        L8:
            return true;
        L5:
            if (isInvalid() == true) goto L8;
            return false;
        }

        boolean isAttachedToTransitionOverlay() {
            if (this.itemView.getParent() != null) goto L5;
        L7:
            return false;
        L5:
            if (this.itemView.getParent() == this.mOwnerRecyclerView) goto L7;
            return true;
        }

        boolean isBound() {
            if ((this.mFlags & 1) != 0) goto L7;
            return false;
        L7:
            return true;
        }

        boolean isInvalid() {
            if ((this.mFlags & 4) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        public final boolean isRecyclable() {
            if ((this.mFlags & 16) == 0) goto L5;
        L7:
            return false;
        L5:
            if (ViewCompat.hasTransientState(this.itemView) == true) goto L7;
            return true;
        }

        boolean isRemoved() {
            if ((this.mFlags & 8) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        boolean isScrap() {
            if (this.mScrapContainer == null) goto L5;
            return true;
        L5:
            return false;
        }

        boolean isTmpDetached() {
            if ((this.mFlags & 256) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        boolean isUpdated() {
            if ((this.mFlags & 2) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        boolean needsUpdate() {
            if ((this.mFlags & 2) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        void offsetPosition(int r3, boolean r4) {
            if (this.mOldPosition != (-1)) goto L6;
            this.mOldPosition = this.mPosition;
        L6:
            if (this.mPreLayoutPosition != (-1)) goto L8;
            this.mPreLayoutPosition = this.mPosition;
        L8:
            if (r4 == false) goto L10;
            this.mPreLayoutPosition += r3;
        L10:
            this.mPosition += r3;
            if (this.itemView.getLayoutParams() == null) goto L14;
            ((LayoutParams) this.itemView.getLayoutParams()).mInsetsDirty = true;
            return;
        }

        void onEnteredHiddenState(RecyclerView r3) {
            if (this.mPendingAccessibilityState == (-1)) goto L5;
            this.mWasImportantForAccessibilityBeforeHidden = this.mPendingAccessibilityState;
        L6:
            r3.setChildImportantForAccessibilityInternal(this, 4);
            return;
        L5:
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            goto L6
        }

        void onLeftHiddenState(RecyclerView r2) {
            r2.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            RecyclerView.clearNestedRecyclerViewIfNotNested(this);
        }

        void saveOldPosition() {
            if (this.mOldPosition != (-1)) goto L6;
            this.mOldPosition = this.mPosition;
            return;
        }

        void setFlags(int r3, int r4) {
            this.mFlags = (this.mFlags & (~r4)) | (r3 & r4);
        }

        public final void setIsRecyclable(boolean r3) {
            int r1 = this.mIsRecyclableCount;
            if (r3 == false) goto L5;
            int r2 = r1 - 1;
        L6:
            this.mIsRecyclableCount = r2;
            if (this.mIsRecyclableCount >= 0) goto L9;
            this.mIsRecyclableCount = 0;
            Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            return;
        L9:
            if (r3 == false) goto L11;
        L13:
            if (r3 == true) goto L15;
            return;
        L15:
            if (this.mIsRecyclableCount != 0) goto L21;
            this.mFlags &= -17;
            return;
        L21:
            return;
        L11:
            if (this.mIsRecyclableCount != 1) goto L13;
            this.mFlags |= 16;
            return;
        L5:
            r2 = r1 + 1;
            goto L6
        }

        void setScrapContainer(Recycler r1, boolean r2) {
            this.mScrapContainer = r1;
            this.mInChangeScrap = r2;
        }

        boolean shouldBeKeptAsChild() {
            if ((this.mFlags & 16) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        boolean shouldIgnore() {
            if ((this.mFlags & 128) == 0) goto L5;
            return true;
        L5:
            return false;
        }

        void stopIgnoring() {
            this.mFlags &= -129;
        }

        public String toString() {
            if (getClass().isAnonymousClass() == false) goto L5;
            String r0 = "ViewHolder";
        L6:
            StringBuilder r1 = new StringBuilder(r0 + "{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
            if (isScrap() == false) goto L14;
            StringBuilder r2 = r1.append(" scrap ");
            if (this.mInChangeScrap == false) goto L11;
            String r3 = "[changeScrap]";
        L12:
            r2.append(r3);
            goto L14
        L11:
            r3 = "[attachedScrap]";
        L14:
            if (isInvalid() == false) goto L17;
            r1.append(" invalid");
        L17:
            if (isBound() == true) goto L20;
            r1.append(" unbound");
        L20:
            if (needsUpdate() == false) goto L23;
            r1.append(" update");
        L23:
            if (isRemoved() == false) goto L26;
            r1.append(" removed");
        L26:
            if (shouldIgnore() == false) goto L29;
            r1.append(" ignored");
        L29:
            if (isTmpDetached() == false) goto L32;
            r1.append(" tmpDetached");
        L32:
            if (isRecyclable() == true) goto L35;
            r1.append(" not recyclable(" + this.mIsRecyclableCount + ")");
        L35:
            if (isAdapterPositionUnknown() == false) goto L38;
            r1.append(" undefined adapter position");
        L38:
            if (this.itemView.getParent() != null) goto L40;
            r1.append(" no parent");
        L40:
            r1.append("}");
            return r1.toString();
        L5:
            r0 = getClass().getSimpleName();
            goto L6
        }

        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            if ((this.mFlags & 32) == 0) goto L5;
            return true;
        L5:
            return false;
        }
    }

    static {
        NESTED_SCROLLING_ATTRS = new int[]{R.attr.nestedScrollingEnabled};
        DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));
        FORCE_INVALIDATE_DISPLAY_LIST = false;
        ALLOW_SIZE_IN_UNSPECIFIED_SPEC = true;
        POST_UPDATES_ON_ANIMATION = true;
        ALLOW_THREAD_GAP_WORK = true;
        FORCE_ABS_FOCUS_SEARCH_DIRECTION = false;
        IGNORE_DETACHED_FOCUSED_CHILD = false;
        Class<?> r0 = Integer.TYPE;
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, r0, r0};
        sQuinticInterpolator = new InterpolatorC05063();
        sDefaultEdgeEffectFactory = new StretchEdgeEffectFactory();
    }

    public RecyclerView(Context r2) {
        this(r2, null);
    }

    public RecyclerView(Context r2, AttributeSet r3) {
        this(r2, r3, C0472R.attr.recyclerViewStyle);
    }

    public RecyclerView(Context r15, AttributeSet r16, int r17) {
        super(r15, r16, r17);
        this.mObserver = new RecyclerViewDataObserver(this);
        this.mRecycler = new Recycler(this);
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new RunnableC05041(this);
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mRecyclerListeners = new ArrayList();
        this.mItemDecorations = new ArrayList();
        this.mOnItemTouchListeners = new ArrayList();
        this.mInterceptRequestLayoutDepth = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mEdgeEffectFactory = sDefaultEdgeEffectFactory;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger(this);
        if (ALLOW_THREAD_GAP_WORK == false) goto L5;
        GapWorker.LayoutPrefetchRegistryImpl r0 = new GapWorker.LayoutPrefetchRegistryImpl();
    L6:
        this.mPrefetchRegistry = r0;
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener(this);
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mNestedOffsets = new int[2];
        this.mReusableIntPair = new int[2];
        this.mPendingAccessibilityImportanceChange = new ArrayList();
        this.mItemAnimatorRunner = new RunnableC05052(this);
        this.mLastAutoMeasureNonExactMeasuredWidth = 0;
        this.mLastAutoMeasureNonExactMeasuredHeight = 0;
        this.mViewInfoProcessCallback = new C05074(this);
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        ViewConfiguration r10 = ViewConfiguration.get(r15);
        this.mTouchSlop = r10.getScaledTouchSlop();
        this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(r10, r15);
        this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(r10, r15);
        this.mMinFlingVelocity = r10.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = r10.getScaledMaximumFlingVelocity();
        this.mPhysicalCoef = (386.0878f * (r15.getResources().getDisplayMetrics().density * 160.0f)) * 0.84f;
        if (getOverScrollMode() != 2) goto L9;
        boolean r1 = true;
    L10:
        setWillNotDraw(r1);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        initAdapterManager();
        initChildrenHelper();
        initAutofill();
        if (ViewCompat.getImportantForAccessibility(this) != 0) goto L13;
        ViewCompat.setImportantForAccessibility(this, 1);
    L13:
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        TypedArray r4 = r15.obtainStyledAttributes(r16, C0472R.styleable.RecyclerView, r17, 0);
        ViewCompat.saveAttributeDataForStyleable(this, r15, C0472R.styleable.RecyclerView, r16, r4, r17, 0);
        String r2 = r4.getString(C0472R.styleable.RecyclerView_layoutManager);
        if (r4.getInt(C0472R.styleable.RecyclerView_android_descendantFocusability, -1) != (-1)) goto L16;
        setDescendantFocusability(262144);
    L16:
        this.mClipToPadding = r4.getBoolean(C0472R.styleable.RecyclerView_android_clipToPadding, true);
        this.mEnableFastScroller = r4.getBoolean(C0472R.styleable.RecyclerView_fastScrollEnabled, false);
        if (this.mEnableFastScroller == false) goto L19;
        initFastScroller((StateListDrawable) r4.getDrawable(C0472R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), r4.getDrawable(C0472R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable) r4.getDrawable(C0472R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), r4.getDrawable(C0472R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable));
    L19:
        r4.recycle();
        createLayoutManager(r15, r2, r16, r17, 0);
        TypedArray r5 = r15.obtainStyledAttributes(r16, NESTED_SCROLLING_ATTRS, r17, 0);
        ViewCompat.saveAttributeDataForStyleable(this, r15, NESTED_SCROLLING_ATTRS, r16, r5, r17, 0);
        boolean r3 = r5.getBoolean(0, true);
        r5.recycle();
        setNestedScrollingEnabled(r3);
        PoolingContainer.setPoolingContainer(this, true);
        return;
    L9:
        r1 = false;
        goto L10
    L5:
        r0 = null;
        goto L6
    }

    static /* synthetic */ void access$000(RecyclerView r0, View r1, int r2, ViewGroup.LayoutParams r3) {
        r0.attachViewToParent(r1, r2, r3);
    }

    static /* synthetic */ void access$100(RecyclerView r0, int r1) {
        r0.detachViewFromParent(r1);
    }

    static /* synthetic */ boolean access$200(RecyclerView r1) {
        return r1.awakenScrollBars();
    }

    static /* synthetic */ void access$300(RecyclerView r0, int r1, int r2) {
        r0.setMeasuredDimension(r1, r2);
    }

    private void addAnimatingView(ViewHolder r7) {
        View r0 = r7.itemView;
        if (r0.getParent() != this) goto L5;
        boolean r1 = true;
    L6:
        this.mRecycler.unscrapView(getChildViewHolder(r0));
        if (r7.isTmpDetached() == false) goto L9;
        this.mChildHelper.attachViewToParent(r0, -1, r0.getLayoutParams(), true);
        return;
    L9:
        if (r1 == true) goto L11;
        this.mChildHelper.addView(r0, true);
        return;
    L11:
        this.mChildHelper.hide(r0);
        return;
    L5:
        r1 = false;
        goto L6
    }

    private void animateChange(ViewHolder r3, ViewHolder r4, ItemAnimator.ItemHolderInfo r5, ItemAnimator.ItemHolderInfo r6, boolean r7, boolean r8) {
        r3.setIsRecyclable(false);
        if (r7 == false) goto L5;
        addAnimatingView(r3);
    L5:
        if (r3 == r4) goto L10;
        if (r8 == false) goto L8;
        addAnimatingView(r4);
    L8:
        r3.mShadowedHolder = r4;
        addAnimatingView(r3);
        this.mRecycler.unscrapView(r3);
        r4.setIsRecyclable(false);
        r4.mShadowingHolder = r3;
    L10:
        if (this.mItemAnimator.animateChange(r3, r4, r5, r6) == false) goto L13;
        postAnimationRunner();
        return;
    }

    private void cancelScroll() {
        resetScroll();
        setScrollState(0);
    }

    static void clearNestedRecyclerViewIfNotNested(ViewHolder r3) {
        if (r3.mNestedRecyclerView == null) goto L23;
        RecyclerView r0 = r3.mNestedRecyclerView.get();
    L5:
        if (r0 == null) goto L14;
        if (r0 == r3.itemView) goto L8;
        Object r1 = r0.getParent();
        if ((r1 instanceof View) == true) goto L11;
        r0 = null;
        goto L5
    L11:
        r0 = (View) r1;
        goto L5
    L8:
        return;
    L14:
        r3.mNestedRecyclerView = null;
        return;
    }

    private int consumeFlingInStretch(int r5, EdgeEffect r6, EdgeEffect r7, int r8) {
        if (r5 <= 0) goto L12;
        if (r6 == null) goto L12;
        if (EdgeEffectCompat.getDistance(r6) == 0.0f) goto L12;
        int r0 = Math.round(((-r8) / FLING_DESTRETCH_FACTOR) * EdgeEffectCompat.onPullDistance(r6, ((-r5) * FLING_DESTRETCH_FACTOR) / r8, 0.5f));
        if (r0 == r5) goto L11;
        r6.finish();
    L11:
        return r5 - r0;
    L12:
        if (r5 >= 0) goto L21;
        if (r7 == null) goto L21;
        if (EdgeEffectCompat.getDistance(r7) == 0.0f) goto L21;
        int r1 = Math.round((r8 / FLING_DESTRETCH_FACTOR) * EdgeEffectCompat.onPullDistance(r7, (r5 * FLING_DESTRETCH_FACTOR) / r8, 0.5f));
        if (r1 == r5) goto L20;
        r7.finish();
    L20:
        return r5 - r1;
    L21:
        return r5;
    }

    private void createLayoutManager(Context r12, String r13, AttributeSet r14, int r15, int r16) {
        if (r13 == null) goto L37;
        String r17 = r13.trim();
        if (r17.isEmpty() == true) goto L43;
        String r18 = getFullClassName(r12, r17);
    L22:
        e = move-exception;
        throw new IllegalStateException(r14.getPositionDescription() + ": Class is not a LayoutManager " + r18, e);
    L34:
        e = move-exception;
        throw new IllegalStateException(r14.getPositionDescription() + ": Unable to find LayoutManager " + r18, e);
    L25:
        e = move-exception;
        throw new IllegalStateException(r14.getPositionDescription() + ": Cannot access non-public constructor " + r18, e);
    L28:
        e = move-exception;
        throw new IllegalStateException(r14.getPositionDescription() + ": Could not instantiate the LayoutManager: " + r18, e);
    L31:
        e = move-exception;
        throw new IllegalStateException(r14.getPositionDescription() + ": Could not instantiate the LayoutManager: " + r18, e);
    L8:
        if (isInEditMode() == false) goto L10;
        ClassLoader r2 = getClass().getClassLoader();     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
    L11:
        Class<? extends U> r0 = Class.forName(r18, false, r2).asSubclass(LayoutManager.class);     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
        Object[] r5 = null;
        Constructor r1 = r0.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);     // Catch: NoSuchMethodException -> L15 ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
        r5 = new Object[]{r12, r14, Integer.valueOf(r15), Integer.valueOf(r16)};     // Catch: NoSuchMethodException -> L15 ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
    L18:
        r1.setAccessible(true);     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
        setLayoutManager((LayoutManager) r1.newInstance(r5));     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
        return;
    L15:
        e = move-exception;
        r1 = r0.getConstructor(new Class[0]);     // Catch: NoSuchMethodException -> L19 ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
    L19:
        e = move-exception;
        e.initCause(e);     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
        throw new IllegalStateException(r14.getPositionDescription() + ": Error creating LayoutManager " + r18, e);     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
    L10:
        r2 = r12.getClassLoader();     // Catch: ClassCastException -> L22 IllegalAccessException -> L25 InstantiationException -> L28 InvocationTargetException -> L31 ClassNotFoundException -> L34
        goto L11
    L43:
        return;
    }

    private boolean didChildRangeChange(int r4, int r5) {
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mMinMaxLayoutPositions[0] != r4) goto L7;
        if (this.mMinMaxLayoutPositions[1] != r5) goto L7;
        return false;
    L7:
        return true;
    }

    private void dispatchContentChangedIfNecessary() {
        int r0 = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (r0 != 0) goto L5;
        return;
    L5:
        if (isAccessibilityEnabled() == false) goto L9;
        AccessibilityEvent r1 = AccessibilityEvent.obtain();
        r1.setEventType(2048);
        AccessibilityEventCompat.setContentChangeTypes(r1, r0);
        sendAccessibilityEventUnchecked(r1);
        return;
    }

    private void dispatchLayoutStep1() {
        boolean r1 = true;
        this.mState.assertLayoutStep(1);
        fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        startInterceptRequestLayout();
        this.mViewInfoStore.clear();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        saveFocusInfo();
        State r0 = this.mState;
        if (this.mState.mRunSimpleAnimations == true) goto L5;
    L7:
        r1 = false;
    L8:
        r0.mTrackOldChangeHolders = r1;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations == false) goto L32;
        int r2 = this.mChildHelper.getChildCount();
        int r3 = 0;
    L11:
        if (r3 >= r2) goto L32;
        ViewHolder r4 = getChildViewHolderInt(this.mChildHelper.getChildAt(r3));
        if (r4.shouldIgnore() == true) goto L30;
        if (r4.isInvalid() == true) goto L17;
    L19:
        this.mViewInfoStore.addToPreLayout(r4, this.mItemAnimator.recordPreLayoutInformation(this.mState, r4, ItemAnimator.buildAdapterChangeFlagsForAnimations(r4), r4.getUnmodifiedPayloads()));
        if (this.mState.mTrackOldChangeHolders == false) goto L30;
        if (r4.isUpdated() == false) goto L30;
        if (r4.isRemoved() == true) goto L30;
        if (r4.shouldIgnore() == true) goto L30;
        if (r4.isInvalid() == true) goto L30;
        this.mViewInfoStore.addToOldChangeHolders(getChangedHolderKey(r4), r4);
        goto L30
    L17:
        if (this.mAdapter.hasStableIds() == true) goto L19;
    L30:
        r3 = r3 + 1;
    L32:
        if (this.mState.mRunPredictiveAnimations == false) goto L50;
        saveOldPositions();
        boolean r5 = this.mState.mStructureChanged;
        this.mState.mStructureChanged = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = r5;
        int r6 = 0;
    L35:
        if (r6 >= this.mChildHelper.getChildCount()) goto L49;
        ViewHolder r7 = getChildViewHolderInt(this.mChildHelper.getChildAt(r6));
        if (r7.shouldIgnore() == true) goto L48;
        if (this.mViewInfoStore.isInPreLayout(r7) == true) goto L48;
        int r8 = ItemAnimator.buildAdapterChangeFlagsForAnimations(r7);
        boolean r9 = r7.hasAnyOfTheFlags(8192);
        if (r9 == true) goto L44;
        r8 = r8 | 4096;
    L44:
        ItemAnimator.ItemHolderInfo r10 = this.mItemAnimator.recordPreLayoutInformation(this.mState, r7, r8, r7.getUnmodifiedPayloads());
        if (r9 == false) goto L47;
        recordAnimationInfoIfBouncedHiddenView(r7, r10);
        goto L48
    L47:
        this.mViewInfoStore.addToAppearedInPreLayoutHolders(r7, r10);
    L48:
        r6 = r6 + 1;
        goto L35
    L49:
        clearOldPositions();
    L51:
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        this.mState.mLayoutStep = 2;
        return;
    L50:
        clearOldPositions();
        goto L51
    L5:
        if (this.mItemsChanged == false) goto L7;
        goto L7
    }

    private void dispatchLayoutStep2() {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        if (this.mPendingSavedState != null) goto L5;
    L10:
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        State r0 = this.mState;
        if (this.mState.mRunSimpleAnimations == true) goto L13;
    L15:
        boolean r2 = false;
    L16:
        r0.mRunSimpleAnimations = r2;
        this.mState.mLayoutStep = 4;
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        return;
    L13:
        if (this.mItemAnimator == null) goto L15;
        r2 = true;
        goto L16
    L5:
        if (this.mAdapter.canRestoreState() == false) goto L10;
        if (this.mPendingSavedState.mLayoutState == null) goto L9;
        this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
    L9:
        this.mPendingSavedState = null;
        goto L10
    }

    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.mLayoutStep = 1;
        if (this.mState.mRunSimpleAnimations == false) goto L25;
        int r0 = this.mChildHelper.getChildCount() - 1;
    L5:
        if (r0 < 0) goto L24;
        ViewHolder r5 = getChildViewHolderInt(this.mChildHelper.getChildAt(r0));
        if (r5.shouldIgnore() == true) goto L23;
        long r10 = getChangedHolderKey(r5);
        ItemAnimator.ItemHolderInfo r2 = this.mItemAnimator.recordPostLayoutInformation(this.mState, r5);
        ViewHolder r4 = this.mViewInfoStore.getFromOldChangeHolders(r10);
        if (r4 != null) goto L12;
    L22:
        this.mViewInfoStore.addToPostLayout(r5, r2);
        goto L23
    L12:
        if (r4.shouldIgnore() == true) goto L22;
        boolean r8 = this.mViewInfoStore.isDisappearing(r4);
        boolean r9 = this.mViewInfoStore.isDisappearing(r5);
        if (r8 == false) goto L17;
        if (r4 != r5) goto L17;
        this.mViewInfoStore.addToPostLayout(r5, r2);
    L17:
        ItemAnimator.ItemHolderInfo r6 = this.mViewInfoStore.popFromPreLayout(r4);
        this.mViewInfoStore.addToPostLayout(r5, r2);
        ItemAnimator.ItemHolderInfo r7 = this.mViewInfoStore.popFromPostLayout(r5);
        if (r6 != null) goto L20;
        handleMissingPreInfoForChangeError(r10, r5, r4);
        goto L23
    L20:
        animateChange(r4, r5, r6, r7, r8, r9);
    L23:
        r0 = r0 - 1;
        goto L5
    L24:
        RecyclerView r3 = this;
        r3.mViewInfoStore.process(r3.mViewInfoProcessCallback);
    L26:
        r3.mLayout.removeAndRecycleScrapInt(r3.mRecycler);
        r3.mState.mPreviousLayoutItemCount = r3.mState.mItemCount;
        r3.mDataSetHasChangedAfterLayout = false;
        r3.mDispatchItemsChangedEvent = false;
        r3.mState.mRunSimpleAnimations = false;
        r3.mState.mRunPredictiveAnimations = false;
        r3.mLayout.mRequestedSimpleAnimations = false;
        if (r3.mRecycler.mChangedScrap == null) goto L30;
        r3.mRecycler.mChangedScrap.clear();
    L30:
        if (r3.mLayout.mPrefetchMaxObservedInInitialPrefetch == false) goto L32;
        r3.mLayout.mPrefetchMaxCountObserved = 0;
        r3.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
        r3.mRecycler.updateViewCacheSize();
    L32:
        r3.mLayout.onLayoutCompleted(r3.mState);
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        r3.mViewInfoStore.clear();
        if (didChildRangeChange(r3.mMinMaxLayoutPositions[0], r3.mMinMaxLayoutPositions[1]) == false) goto L35;
        dispatchOnScrolled(0, 0);
    L35:
        recoverFocusFromState();
        resetFocusInfo();
        return;
    L25:
        r3 = this;
        goto L26
    }

    private boolean dispatchToOnItemTouchListeners(MotionEvent r4) {
        if (this.mInterceptingOnItemTouchListener == null) goto L5;
        this.mInterceptingOnItemTouchListener.onTouchEvent(this, r4);
        int r0 = r4.getAction();
        if (r0 == 3) goto L13;
        if (r0 == 1) goto L13;
    L14:
        return true;
    L13:
        this.mInterceptingOnItemTouchListener = null;
        goto L14
    L5:
        if (r4.getAction() != 0) goto L9;
        return false;
    L9:
        return findInterceptingOnItemTouchListener(r4);
    }

    private boolean findInterceptingOnItemTouchListener(MotionEvent r6) {
        int r0 = r6.getAction();
        int r1 = this.mOnItemTouchListeners.size();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L11;
        OnItemTouchListener r3 = this.mOnItemTouchListeners.get(r2);
        if (r3.onInterceptTouchEvent(this, r6) == false) goto L10;
        if (r0 == 3) goto L10;
        this.mInterceptingOnItemTouchListener = r3;
        return true;
    L10:
        r2 = r2 + 1;
        goto L3
    L11:
        return false;
    }

    private void findMinMaxChildLayoutPositions(int[] r9) {
        int r0 = this.mChildHelper.getChildCount();
        if (r0 != 0) goto L6;
        r9[0] = -1;
        r9[1] = -1;
        return;
    L6:
        int r3 = Integer.MAX_VALUE;
        int r4 = Integer.MIN_VALUE;
        int r5 = 0;
    L7:
        if (r5 >= r0) goto L17;
        ViewHolder r6 = getChildViewHolderInt(this.mChildHelper.getChildAt(r5));
        if (r6.shouldIgnore() == true) goto L16;
        int r7 = r6.getLayoutPosition();
        if (r7 >= r3) goto L14;
        r3 = r7;
    L14:
        if (r7 <= r4) goto L16;
        r4 = r7;
    L16:
        r5 = r5 + 1;
        goto L7
    L17:
        r9[0] = r3;
        r9[1] = r4;
    }

    static RecyclerView findNestedRecyclerView(View r6) {
        if ((r6 instanceof ViewGroup) == true) goto L6;
        return null;
    L6:
        if ((r6 instanceof RecyclerView) == true) goto L8;
        ViewGroup r0 = (ViewGroup) r6;
        int r2 = r0.getChildCount();
        int r3 = 0;
    L10:
        if (r3 >= r2) goto L15;
        RecyclerView r5 = findNestedRecyclerView(r0.getChildAt(r3));
        if (r5 != null) goto L13;
        r3 = r3 + 1;
        goto L10
    L13:
        return r5;
    L15:
        return null;
    L8:
        return (RecyclerView) r6;
    }

    private View findNextViewToFocus() {
        if (this.mState.mFocusedItemPosition == (-1)) goto L5;
        int r0 = this.mState.mFocusedItemPosition;
    L6:
        int r1 = this.mState.getItemCount();
        int r2 = r0;
    L7:
        if (r2 >= r1) goto L16;
        ViewHolder r3 = findViewHolderForAdapterPosition(r2);
        if (r3 == null) goto L16;
        if (r3.itemView.hasFocusable() == true) goto L14;
        r2 = r2 + 1;
        goto L7
    L14:
        return r3.itemView;
    L16:
        int r4 = Math.min(r1, r0) - 1;
    L18:
        if (r4 < 0) goto L27;
        ViewHolder r5 = findViewHolderForAdapterPosition(r4);
        if (r5 == null) goto L21;
        if (r5.itemView.hasFocusable() == true) goto L25;
        r4 = r4 - 1;
        goto L18
    L25:
        return r5.itemView;
    L21:
        return null;
    L27:
        return null;
    L5:
        r0 = 0;
        goto L6
    }

    static ViewHolder getChildViewHolderInt(View r1) {
        if (r1 != null) goto L6;
        return null;
    L6:
        return ((LayoutParams) r1.getLayoutParams()).mViewHolder;
    }

    static void getDecoratedBoundsWithMarginsInt(View r7, Rect r8) {
        LayoutParams r0 = (LayoutParams) r7.getLayoutParams();
        Rect r1 = r0.mDecorInsets;
        r8.set((r7.getLeft() - r1.left) - r0.leftMargin, (r7.getTop() - r1.top) - r0.topMargin, (r7.getRight() + r1.right) + r0.rightMargin, (r7.getBottom() + r1.bottom) + r0.bottomMargin);
    }

    private int getDeepestFocusedViewWithId(View r4) {
        int r0 = r4.getId();
    L4:
        if (r4.isFocused() == true) goto L13;
        if ((r4 instanceof ViewGroup) == false) goto L13;
        if (r4.hasFocus() == false) goto L13;
        r4 = ((ViewGroup) r4).getFocusedChild();
        if (r4.getId() == (-1)) goto L4;
        r0 = r4.getId();
    L13:
        return r0;
    }

    private String getFullClassName(Context r4, String r5) {
        if (r5.charAt(0) != '.') goto L7;
        return r4.getPackageName() + r5;
    L7:
        if (r5.contains(".") == false) goto L10;
        return r5;
    L10:
        return RecyclerView.class.getPackage().getName() + '.' + r5;
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper != null) goto L6;
        this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
    L6:
        return this.mScrollingChildHelper;
    }

    private float getSplineFlingDistance(int r10) {
        return (float) (((double) (this.mPhysicalCoef * SCROLL_FRICTION)) * Math.exp((((double) DECELERATION_RATE) / (((double) DECELERATION_RATE) - 1.0d)) * Math.log((Math.abs(r10) * INFLEXION) / (this.mPhysicalCoef * SCROLL_FRICTION))));
    }

    private void handleMissingPreInfoForChangeError(long r11, ViewHolder r13, ViewHolder r14) {
        int r0 = this.mChildHelper.getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L18;
        ViewHolder r3 = getChildViewHolderInt(this.mChildHelper.getChildAt(r1));
        if (r3 == r13) goto L17;
        if (getChangedHolderKey(r3) != r11) goto L17;
        if (this.mAdapter == null) goto L16;
        if (this.mAdapter.hasStableIds() == false) goto L16;
        throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + r3 + " \n View Holder 2:" + r13 + exceptionLabel());
    L16:
        throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + r3 + " \n View Holder 2:" + r13 + exceptionLabel());
    L17:
        r1 = r1 + 1;
        goto L3
    L18:
        Log.e(TAG, "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + r14 + " cannot be found but it is necessary for " + r13 + exceptionLabel());
    }

    private boolean hasUpdatedView() {
        int r0 = this.mChildHelper.getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L14;
        ViewHolder r2 = getChildViewHolderInt(this.mChildHelper.getChildAt(r1));
        if (r2 == null) goto L13;
        if (r2.shouldIgnore() == true) goto L13;
        if (r2.isUpdated() == false) goto L13;
        return true;
    L13:
        r1 = r1 + 1;
        goto L3
    L14:
        return false;
    }

    private void initAutofill() {
        if (ViewCompat.getImportantForAutofill(this) != 0) goto L6;
        ViewCompat.setImportantForAutofill(this, 8);
        return;
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new C05085(this));
    }

    private boolean isPreferredNextFocus(View r8, View r9, int r10) {
        if (r9 == null) goto L75;
        if (r9 == this) goto L75;
        if (r9 == r8) goto L75;
        if (findContainingItemView(r9) != null) goto L11;
        return false;
    L11:
        if (r8 != null) goto L14;
        return true;
    L14:
        if (findContainingItemView(r8) != null) goto L16;
        return true;
    L16:
        this.mTempRect.set(0, 0, r8.getWidth(), r8.getHeight());
        this.mTempRect2.set(0, 0, r9.getWidth(), r9.getHeight());
        offsetDescendantRectToMyCoords(r8, this.mTempRect);
        offsetDescendantRectToMyCoords(r9, this.mTempRect2);
        if (this.mLayout.getLayoutDirection() != 1) goto L19;
        int r2 = -1;
    L20:
        int r3 = 0;
        if (this.mTempRect.left < this.mTempRect2.left) goto L25;
        if (this.mTempRect.right <= this.mTempRect2.left) goto L25;
    L28:
        if (this.mTempRect.right > this.mTempRect2.right) goto L32;
        if (this.mTempRect.left >= this.mTempRect2.right) goto L32;
    L34:
        byte r4 = 0;
        if (this.mTempRect.top < this.mTempRect2.top) goto L39;
        if (this.mTempRect.bottom <= this.mTempRect2.top) goto L39;
    L42:
        if (this.mTempRect.bottom > this.mTempRect2.bottom) goto L46;
        if (this.mTempRect.top >= this.mTempRect2.bottom) goto L46;
    L48:
        switch(r10) {
            case 1: goto L69;
            case 2: goto L63;
            case 17: goto L60;
            case 33: goto L57;
            case 66: goto L54;
            case 130: goto L51;
            default: goto L50;
        };
    L51:
        if (r4 > 0) goto L53;
        return false;
    L53:
        return true;
    L54:
        if (r3 > 0) goto L56;
        return false;
    L56:
        return true;
    L57:
        if (r4 < 0) goto L59;
        return false;
    L59:
        return true;
    L60:
        if (r3 < 0) goto L62;
        return false;
    L62:
        return true;
    L63:
        if (r4 > 0) goto L68;
        if (r4 == 0) goto L66;
        return false;
    L66:
        if ((r3 * r2) > 0) goto L68;
        return false;
    L68:
        return true;
    L69:
        if (r4 < 0) goto L74;
        if (r4 == 0) goto L72;
        return false;
    L72:
        if ((r3 * r2) < 0) goto L74;
        return false;
    L74:
        return true;
    L50:
        throw new IllegalArgumentException("Invalid direction: " + r10 + exceptionLabel());
    L46:
        if (this.mTempRect.top <= this.mTempRect2.top) goto L48;
        r4 = -1;
    L39:
        if (this.mTempRect.bottom >= this.mTempRect2.bottom) goto L42;
        r4 = 1;
    L32:
        if (this.mTempRect.left <= this.mTempRect2.left) goto L34;
        r3 = -1;
    L25:
        if (this.mTempRect.right >= this.mTempRect2.right) goto L28;
        r3 = 1;
        goto L34
    L19:
        r2 = 1;
    L75:
        return false;
    }

    private void nestedScrollByInternal(int r17, int r18, MotionEvent r19, int r20) {
        if (this.mLayout != null) goto L7;
        Log.e(TAG, "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
    L7:
        if (this.mLayoutSuppressed == false) goto L9;
        return;
    L9:
        int r7 = 0;
        this.mReusableIntPair[0] = 0;
        this.mReusableIntPair[1] = 0;
        boolean r9 = this.mLayout.canScrollHorizontally();
        boolean r10 = this.mLayout.canScrollVertically();
        int r3 = 0;
        if (r9 == false) goto L12;
        r3 = 0 | 1;
    L12:
        if (r10 == false) goto L14;
        int r11 = r3 | 2;
    L16:
        if (r19 != null) goto L18;
        float r4 = getHeight() / 2.0f;
    L19:
        float r12 = r4;
        if (r19 != null) goto L22;
        float r5 = getWidth() / 2.0f;
    L23:
        int r14 = r17 - releaseHorizontalGlow(r17, r12);
        int r15 = r18 - releaseVerticalGlow(r18, r5);
        startNestedScroll(r11, r20);
        if (r9 == false) goto L26;
        int r1 = r14;
    L27:
        if (r10 == false) goto L29;
        int r2 = r15;
    L31:
        if (dispatchNestedPreScroll(r1, r2, this.mReusableIntPair, this.mScrollOffset, r20) == false) goto L34;
        r14 = r14 - this.mReusableIntPair[0];
        r15 = r15 - this.mReusableIntPair[1];
    L34:
        if (r9 == false) goto L36;
        int r6 = r14;
    L37:
        if (r10 == false) goto L39;
        r7 = r15;
    L39:
        scrollByInternal(r6, r7, r19, r20);
        if (this.mGapWorker == null) goto L44;
        if (r14 != 0) goto L43;
        if (r15 == 0) goto L44;
    L43:
        this.mGapWorker.postFromTraversal(this, r14, r15);
    L44:
        stopNestedScroll(r20);
        return;
    L36:
        r6 = 0;
        goto L37
    L29:
        r2 = 0;
        goto L31
    L26:
        r1 = 0;
        goto L27
    L22:
        r5 = r19.getX();
        goto L23
    L18:
        r4 = r19.getY();
        goto L19
    L14:
        r11 = r3;
        goto L16
    }

    private void onPointerUp(MotionEvent r5) {
        int r0 = r5.getActionIndex();
        if (r5.getPointerId(r0) != this.mScrollPointerId) goto L9;
        if (r0 != 0) goto L6;
        int r1 = 1;
    L7:
        this.mScrollPointerId = r5.getPointerId(r1);
        int r2 = (int) (r5.getX(r1) + 0.5f);
        this.mLastTouchX = r2;
        this.mInitialTouchX = r2;
        int r3 = (int) (r5.getY(r1) + 0.5f);
        this.mLastTouchY = r3;
        this.mInitialTouchY = r3;
        return;
    L6:
        r1 = 0;
        goto L7
    }

    private boolean predictiveItemAnimationsEnabled() {
        if (this.mItemAnimator != null) goto L5;
    L7:
        return false;
    L5:
        if (this.mLayout.supportsPredictiveItemAnimations() == false) goto L7;
        return true;
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (this.mDataSetHasChangedAfterLayout == false) goto L8;
        this.mAdapterHelper.reset();
        if (this.mDispatchItemsChangedEvent == false) goto L8;
        this.mLayout.onItemsChanged(this);
    L8:
        if (predictiveItemAnimationsEnabled() == false) goto L10;
        this.mAdapterHelper.preProcess();
    L11:
        boolean r1 = false;
        if (this.mItemsAddedOrRemoved == false) goto L14;
    L17:
        boolean r0 = true;
    L18:
        State r3 = this.mState;
        if (this.mFirstLayoutComplete == true) goto L21;
    L32:
        boolean r4 = false;
    L33:
        r3.mRunSimpleAnimations = r4;
        State r5 = this.mState;
        if (this.mState.mRunSimpleAnimations == false) goto L42;
        if (r0 == false) goto L42;
        if (this.mDataSetHasChangedAfterLayout == true) goto L42;
        if (predictiveItemAnimationsEnabled() == false) goto L42;
        r1 = true;
    L42:
        r5.mRunPredictiveAnimations = r1;
        return;
    L21:
        if (this.mItemAnimator == null) goto L32;
        if (this.mDataSetHasChangedAfterLayout == true) goto L28;
        if (r0 == true) goto L28;
        if (this.mLayout.mRequestedSimpleAnimations == false) goto L32;
    L28:
        if (this.mDataSetHasChangedAfterLayout == true) goto L30;
    L31:
        r4 = true;
        goto L33
    L30:
        if (this.mAdapter.hasStableIds() == false) goto L32;
    L14:
        if (this.mItemsChanged == true) goto L17;
        r0 = false;
        goto L18
    L10:
        this.mAdapterHelper.consumeUpdatesInOnePass();
        goto L11
    }

    private void pullGlows(float r7, float r8, float r9, float r10) {
        boolean r0 = false;
        if (r8 >= 0.0f) goto L6;
        ensureLeftGlow();
        EdgeEffectCompat.onPullDistance(this.mLeftGlow, (-r8) / getWidth(), 1.0f - (r9 / getHeight()));
        r0 = true;
    L9:
        if (r10 >= 0.0f) goto L12;
        ensureTopGlow();
        EdgeEffectCompat.onPullDistance(this.mTopGlow, (-r10) / getHeight(), r7 / getWidth());
        r0 = true;
    L14:
        if (r0 == false) goto L16;
    L19:
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    L16:
        if (r8 != 0.0f) goto L19;
        if (r10 != 0.0f) goto L19;
        return;
    L12:
        if (r10 <= 0.0f) goto L14;
        ensureBottomGlow();
        EdgeEffectCompat.onPullDistance(this.mBottomGlow, r10 / getHeight(), 1.0f - (r7 / getWidth()));
        r0 = true;
        goto L14
    L6:
        if (r8 <= 0.0f) goto L9;
        ensureRightGlow();
        EdgeEffectCompat.onPullDistance(this.mRightGlow, r8 / getWidth(), r9 / getHeight());
        r0 = true;
        goto L9
    }

    private void recoverFocusFromState() {
        if (this.mPreserveFocusAfterLayout == true) goto L5;
        return;
    L5:
        if (this.mAdapter != null) goto L7;
        return;
    L7:
        if (hasFocus() == true) goto L9;
        return;
    L9:
        if (getDescendantFocusability() != 393216) goto L11;
        return;
    L11:
        if (getDescendantFocusability() != 131072) goto L16;
        if (isFocused() == false) goto L16;
        return;
    L16:
        if (isFocused() == true) goto L30;
        View r0 = getFocusedChild();
        if (IGNORE_DETACHED_FOCUSED_CHILD == false) goto L28;
        if (r0.getParent() == null) goto L24;
        if (r0.hasFocus() == true) goto L28;
    L24:
        if (this.mChildHelper.getChildCount() != 0) goto L30;
        requestFocus();
        return;
    L28:
        if (this.mChildHelper.isHidden(r0) == true) goto L30;
        return;
    L30:
        ViewHolder r1 = null;
        if (this.mState.mFocusedItemId != (-1)) goto L33;
    L35:
        View r2 = null;
        if (r1 == null) goto L44;
        if (this.mChildHelper.isHidden(r1.itemView) == true) goto L44;
        if (r1.itemView.hasFocusable() == false) goto L44;
        r2 = r1.itemView;
    L46:
        if (r2 != null) goto L48;
        return;
    L48:
        if (this.mState.mFocusedSubChildId == (-1)) goto L54;
        View r3 = r2.findViewById(this.mState.mFocusedSubChildId);
        if (r3 == null) goto L54;
        if (r3.isFocusable() == false) goto L54;
        r2 = r3;
    L54:
        r2.requestFocus();
        return;
    L44:
        if (this.mChildHelper.getChildCount() <= 0) goto L46;
        r2 = findNextViewToFocus();
        goto L46
    L33:
        if (this.mAdapter.hasStableIds() == false) goto L35;
        r1 = findViewHolderForItemId(this.mState.mFocusedItemId);
        goto L35
    }

    private void releaseGlows() {
        boolean r0 = false;
        if (this.mLeftGlow == null) goto L6;
        this.mLeftGlow.onRelease();
        r0 = this.mLeftGlow.isFinished();
    L6:
        if (this.mTopGlow == null) goto L9;
        this.mTopGlow.onRelease();
        r0 = r0 | this.mTopGlow.isFinished();
    L9:
        if (this.mRightGlow == null) goto L12;
        this.mRightGlow.onRelease();
        r0 = r0 | this.mRightGlow.isFinished();
    L12:
        if (this.mBottomGlow == null) goto L14;
        this.mBottomGlow.onRelease();
        r0 = r0 | this.mBottomGlow.isFinished();
    L14:
        if (r0 == false) goto L17;
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    }

    private int releaseHorizontalGlow(int r8, float r9) {
        float r0 = 0.0f;
        float r1 = r9 / getHeight();
        float r2 = r8 / getWidth();
        if (this.mLeftGlow == null) goto L14;
        if (EdgeEffectCompat.getDistance(this.mLeftGlow) == 0.0f) goto L14;
        if (canScrollHorizontally(-1) == false) goto L9;
        this.mLeftGlow.onRelease();
    L12:
        invalidate();
    L25:
        return Math.round(getWidth() * r0);
    L9:
        r0 = -EdgeEffectCompat.onPullDistance(this.mLeftGlow, -r2, 1.0f - r1);
        if (EdgeEffectCompat.getDistance(this.mLeftGlow) != 0.0f) goto L12;
        this.mLeftGlow.onRelease();
    L14:
        if (this.mRightGlow == null) goto L25;
        if (EdgeEffectCompat.getDistance(this.mRightGlow) == 0.0f) goto L25;
        if (canScrollHorizontally(1) == false) goto L20;
        this.mRightGlow.onRelease();
    L23:
        invalidate();
        goto L25
    L20:
        r0 = EdgeEffectCompat.onPullDistance(this.mRightGlow, r2, r1);
        if (EdgeEffectCompat.getDistance(this.mRightGlow) != 0.0f) goto L23;
        this.mRightGlow.onRelease();
        goto L23
    }

    private int releaseVerticalGlow(int r7, float r8) {
        float r0 = 0.0f;
        float r1 = r8 / getWidth();
        float r2 = r7 / getHeight();
        if (this.mTopGlow == null) goto L14;
        if (EdgeEffectCompat.getDistance(this.mTopGlow) == 0.0f) goto L14;
        if (canScrollVertically(-1) == false) goto L9;
        this.mTopGlow.onRelease();
    L12:
        invalidate();
    L25:
        return Math.round(getHeight() * r0);
    L9:
        r0 = -EdgeEffectCompat.onPullDistance(this.mTopGlow, -r2, r1);
        if (EdgeEffectCompat.getDistance(this.mTopGlow) != 0.0f) goto L12;
        this.mTopGlow.onRelease();
    L14:
        if (this.mBottomGlow == null) goto L25;
        if (EdgeEffectCompat.getDistance(this.mBottomGlow) == 0.0f) goto L25;
        if (canScrollVertically(1) == false) goto L20;
        this.mBottomGlow.onRelease();
    L23:
        invalidate();
        goto L25
    L20:
        r0 = EdgeEffectCompat.onPullDistance(this.mBottomGlow, r2, 1.0f - r1);
        if (EdgeEffectCompat.getDistance(this.mBottomGlow) != 0.0f) goto L23;
        this.mBottomGlow.onRelease();
        goto L23
    }

    private void requestChildOnScreen(View r12, View r13) {
        if (r13 == null) goto L4;
        View r0 = r13;
    L5:
        this.mTempRect.set(0, 0, r0.getWidth(), r0.getHeight());
        ViewGroup.LayoutParams r1 = r0.getLayoutParams();
        if ((r1 instanceof LayoutParams) == false) goto L10;
        LayoutParams r2 = (LayoutParams) r1;
        if (r2.mInsetsDirty == true) goto L10;
        Rect r3 = r2.mDecorInsets;
        this.mTempRect.left -= r3.left;
        this.mTempRect.right += r3.right;
        this.mTempRect.top -= r3.top;
        this.mTempRect.bottom += r3.bottom;
    L10:
        if (r13 == null) goto L12;
        offsetDescendantRectToMyCoords(r13, this.mTempRect);
        offsetRectIntoDescendantCoords(r12, this.mTempRect);
    L12:
        LayoutManager r5 = this.mLayout;
        Rect r8 = this.mTempRect;
        boolean r9 = !this.mFirstLayoutComplete;
        if (r13 != null) goto L15;
        boolean r10 = true;
    L16:
        r5.requestChildRectangleOnScreen(this, r12, r8, r9, r10);
        return;
    L15:
        r10 = false;
        goto L16
    L4:
        r0 = r12;
        goto L5
    }

    private void resetFocusInfo() {
        this.mState.mFocusedItemId = -1;
        this.mState.mFocusedItemPosition = -1;
        this.mState.mFocusedSubChildId = -1;
    }

    private void resetScroll() {
        if (this.mVelocityTracker == null) goto L5;
        this.mVelocityTracker.clear();
    L5:
        stopNestedScroll(0);
        releaseGlows();
    }

    private void saveFocusInfo() {
        View r0 = null;
        if (this.mPreserveFocusAfterLayout == true) goto L5;
    L9:
        if (r0 != null) goto L11;
        ViewHolder r1 = null;
    L12:
        if (r1 != null) goto L14;
        resetFocusInfo();
        return;
    L14:
        State r2 = this.mState;
        if (this.mAdapter.hasStableIds() == false) goto L17;
        long r3 = r1.getItemId();
    L18:
        r2.mFocusedItemId = r3;
        State r4 = this.mState;
        if (this.mDataSetHasChangedAfterLayout == false) goto L22;
        int r5 = -1;
    L25:
        r4.mFocusedItemPosition = r5;
        State r6 = this.mState;
        r6.mFocusedSubChildId = getDeepestFocusedViewWithId(r1.itemView);
        return;
    L22:
        if (r1.isRemoved() == false) goto L24;
        r5 = r1.mOldPosition;
        goto L25
    L24:
        r5 = r1.getAbsoluteAdapterPosition();
        goto L25
    L17:
        r3 = -1;
        goto L18
    L11:
        r1 = findContainingViewHolder(r0);
        goto L12
    L5:
        if (hasFocus() == false) goto L9;
        if (this.mAdapter == null) goto L9;
        r0 = getFocusedChild();
        goto L9
    }

    private void setAdapterInternal(Adapter<?> r4, boolean r5, boolean r6) {
        if (this.mAdapter == null) goto L5;
        this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
        this.mAdapter.onDetachedFromRecyclerView(this);
    L5:
        if (r5 == false) goto L7;
        if (r6 == true) goto L7;
    L8:
        this.mAdapterHelper.reset();
        Adapter<?> r0 = this.mAdapter;
        this.mAdapter = r4;
        if (r4 == null) goto L12;
        r4.registerAdapterDataObserver(this.mObserver);
        r4.onAttachedToRecyclerView(this);
    L12:
        if (this.mLayout == null) goto L14;
        this.mLayout.onAdapterChanged(r0, this.mAdapter);
    L14:
        this.mRecycler.onAdapterChanged(r0, this.mAdapter, r5);
        this.mState.mStructureChanged = true;
        return;
    L7:
        removeAndRecycleViews();
        goto L8
    }

    private boolean shouldAbsorb(EdgeEffect r5, int r6, int r7) {
        if (r6 <= 0) goto L6;
        return true;
    L6:
        if (getSplineFlingDistance(-r6) < (EdgeEffectCompat.getDistance(r5) * r7)) goto L10;
        return false;
    L10:
        return true;
    }

    private boolean stopGlowAnimations(MotionEvent r9) {
        boolean r0 = false;
        if (this.mLeftGlow == null) goto L10;
        if (EdgeEffectCompat.getDistance(this.mLeftGlow) == 0.0f) goto L10;
        if (canScrollHorizontally(-1) == true) goto L10;
        EdgeEffectCompat.onPullDistance(this.mLeftGlow, 0.0f, 1.0f - (r9.getY() / getHeight()));
        r0 = true;
    L10:
        if (this.mRightGlow == null) goto L17;
        if (EdgeEffectCompat.getDistance(this.mRightGlow) == 0.0f) goto L17;
        if (canScrollHorizontally(1) == true) goto L17;
        EdgeEffectCompat.onPullDistance(this.mRightGlow, 0.0f, r9.getY() / getHeight());
        r0 = true;
    L17:
        if (this.mTopGlow == null) goto L24;
        if (EdgeEffectCompat.getDistance(this.mTopGlow) == 0.0f) goto L24;
        if (canScrollVertically(-1) == true) goto L24;
        EdgeEffectCompat.onPullDistance(this.mTopGlow, 0.0f, r9.getX() / getWidth());
        r0 = true;
    L24:
        if (this.mBottomGlow != null) goto L26;
        return r0;
    L26:
        if (EdgeEffectCompat.getDistance(this.mBottomGlow) != 0.0f) goto L28;
        return r0;
    L28:
        if (canScrollVertically(1) == true) goto L33;
        EdgeEffectCompat.onPullDistance(this.mBottomGlow, 0.0f, 1.0f - (r9.getX() / getWidth()));
        return true;
    L33:
        return r0;
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        if (this.mLayout == null) goto L6;
        this.mLayout.stopSmoothScroller();
        return;
    }

    void absorbGlows(int r3, int r4) {
        if (r3 >= 0) goto L6;
        ensureLeftGlow();
        if (this.mLeftGlow.isFinished() == false) goto L10;
        this.mLeftGlow.onAbsorb(-r3);
    L10:
        if (r4 >= 0) goto L14;
        ensureTopGlow();
        if (this.mTopGlow.isFinished() == false) goto L18;
        this.mTopGlow.onAbsorb(-r4);
    L18:
        if (r3 != 0) goto L20;
        if (r4 != 0) goto L20;
        return;
    L20:
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    L14:
        if (r4 <= 0) goto L18;
        ensureBottomGlow();
        if (this.mBottomGlow.isFinished() == false) goto L18;
        this.mBottomGlow.onAbsorb(r4);
        goto L18
    L6:
        if (r3 <= 0) goto L10;
        ensureRightGlow();
        if (this.mRightGlow.isFinished() == false) goto L10;
        this.mRightGlow.onAbsorb(r3);
        goto L10
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> r2, int r3, int r4) {
        if (this.mLayout != null) goto L5;
    L6:
        super.addFocusables(r2, r3, r4);
        return;
    L5:
        if (this.mLayout.onAddFocusables(this, r2, r3, r4) == false) goto L6;
    }

    public void addItemDecoration(ItemDecoration r2) {
        addItemDecoration(r2, -1);
    }

    public void addItemDecoration(ItemDecoration r3, int r4) {
        if (this.mLayout == null) goto L6;
        this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
    L6:
        if (this.mItemDecorations.isEmpty() == false) goto L8;
        setWillNotDraw(false);
    L8:
        if (r4 >= 0) goto L10;
        this.mItemDecorations.add(r3);
    L11:
        markItemDecorInsetsDirty();
        requestLayout();
        return;
    L10:
        this.mItemDecorations.add(r4, r3);
        goto L11
    }

    public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener r2) {
        if (this.mOnChildAttachStateListeners != null) goto L5;
        this.mOnChildAttachStateListeners = new ArrayList();
    L5:
        this.mOnChildAttachStateListeners.add(r2);
    }

    public void addOnItemTouchListener(OnItemTouchListener r2) {
        this.mOnItemTouchListeners.add(r2);
    }

    public void addOnScrollListener(OnScrollListener r2) {
        if (this.mScrollListeners != null) goto L5;
        this.mScrollListeners = new ArrayList();
    L5:
        this.mScrollListeners.add(r2);
    }

    public void addRecyclerListener(RecyclerListener r3) {
        if (r3 == null) goto L4;
        boolean r0 = true;
    L5:
        Preconditions.checkArgument(r0, "'listener' arg cannot be null.");
        this.mRecyclerListeners.add(r3);
        return;
    L4:
        r0 = false;
        goto L5
    }

    void animateAppearance(ViewHolder r2, ItemAnimator.ItemHolderInfo r3, ItemAnimator.ItemHolderInfo r4) {
        r2.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(r2, r3, r4) == false) goto L6;
        postAnimationRunner();
        return;
    }

    void animateDisappearance(ViewHolder r2, ItemAnimator.ItemHolderInfo r3, ItemAnimator.ItemHolderInfo r4) {
        addAnimatingView(r2);
        r2.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(r2, r3, r4) == false) goto L6;
        postAnimationRunner();
        return;
    }

    void assertInLayoutOrScroll(String r4) {
        if (isComputingLayout() == true) goto L9;
        if (r4 != null) goto L8;
        throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling" + exceptionLabel());
    L8:
        throw new IllegalStateException(r4 + exceptionLabel());
    }

    void assertNotInLayoutOrScroll(String r4) {
        if (isComputingLayout() == false) goto L10;
        if (r4 != null) goto L8;
        throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling" + exceptionLabel());
    L8:
        throw new IllegalStateException(r4);
    L10:
        if (this.mDispatchScrollCounter <= 0) goto L13;
        Log.w(TAG, "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException("" + exceptionLabel()));
        return;
    }

    boolean canReuseUpdatedViewHolder(ViewHolder r3) {
        if (this.mItemAnimator != null) goto L5;
    L8:
        return true;
    L5:
        if (this.mItemAnimator.canReuseUpdatedViewHolder(r3, r3.getUnmodifiedPayloads()) == true) goto L8;
        return false;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams r3) {
        if ((r3 instanceof LayoutParams) == true) goto L5;
    L7:
        return false;
    L5:
        if (this.mLayout.checkLayoutParams((LayoutParams) r3) == false) goto L7;
        return true;
    }

    void clearOldPositions() {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        ViewHolder r2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r1));
        if (r2.shouldIgnore() == true) goto L7;
        r2.clearOldPosition();
    L7:
        r1 = r1 + 1;
        goto L3
    L8:
        this.mRecycler.clearOldPositions();
    }

    public void clearOnChildAttachStateChangeListeners() {
        if (this.mOnChildAttachStateListeners == null) goto L6;
        this.mOnChildAttachStateListeners.clear();
        return;
    }

    public void clearOnScrollListeners() {
        if (this.mScrollListeners == null) goto L6;
        this.mScrollListeners.clear();
        return;
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeHorizontalScrollExtent() {
        if (this.mLayout != null) goto L6;
        return 0;
    L6:
        if (this.mLayout.canScrollHorizontally() == true) goto L8;
        return 0;
    L8:
        return this.mLayout.computeHorizontalScrollExtent(this.mState);
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeHorizontalScrollOffset() {
        if (this.mLayout != null) goto L6;
        return 0;
    L6:
        if (this.mLayout.canScrollHorizontally() == true) goto L8;
        return 0;
    L8:
        return this.mLayout.computeHorizontalScrollOffset(this.mState);
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeHorizontalScrollRange() {
        if (this.mLayout != null) goto L6;
        return 0;
    L6:
        if (this.mLayout.canScrollHorizontally() == true) goto L8;
        return 0;
    L8:
        return this.mLayout.computeHorizontalScrollRange(this.mState);
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeVerticalScrollExtent() {
        if (this.mLayout != null) goto L6;
        return 0;
    L6:
        if (this.mLayout.canScrollVertically() == true) goto L8;
        return 0;
    L8:
        return this.mLayout.computeVerticalScrollExtent(this.mState);
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeVerticalScrollOffset() {
        if (this.mLayout != null) goto L6;
        return 0;
    L6:
        if (this.mLayout.canScrollVertically() == true) goto L8;
        return 0;
    L8:
        return this.mLayout.computeVerticalScrollOffset(this.mState);
    }

    @Override // android.view.View, androidx.core.view.ScrollingView
    public int computeVerticalScrollRange() {
        if (this.mLayout != null) goto L6;
        return 0;
    L6:
        if (this.mLayout.canScrollVertically() == true) goto L8;
        return 0;
    L8:
        return this.mLayout.computeVerticalScrollRange(this.mState);
    }

    void considerReleasingGlowsOnScroll(int r3, int r4) {
        boolean r0 = false;
        if (this.mLeftGlow == null) goto L9;
        if (this.mLeftGlow.isFinished() == true) goto L9;
        if (r3 <= 0) goto L9;
        this.mLeftGlow.onRelease();
        r0 = this.mLeftGlow.isFinished();
    L9:
        if (this.mRightGlow == null) goto L15;
        if (this.mRightGlow.isFinished() == true) goto L15;
        if (r3 >= 0) goto L15;
        this.mRightGlow.onRelease();
        r0 = r0 | this.mRightGlow.isFinished();
    L15:
        if (this.mTopGlow == null) goto L21;
        if (this.mTopGlow.isFinished() == true) goto L21;
        if (r4 <= 0) goto L21;
        this.mTopGlow.onRelease();
        r0 = r0 | this.mTopGlow.isFinished();
    L21:
        if (this.mBottomGlow != null) goto L23;
    L26:
        if (r0 == false) goto L29;
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    L29:
        return;
    L23:
        if (this.mBottomGlow.isFinished() == true) goto L26;
        if (r4 >= 0) goto L26;
        this.mBottomGlow.onRelease();
        r0 = r0 | this.mBottomGlow.isFinished();
        goto L26
    }

    int consumeFlingInHorizontalStretch(int r4) {
        return consumeFlingInStretch(r4, this.mLeftGlow, this.mRightGlow, getWidth());
    }

    int consumeFlingInVerticalStretch(int r4) {
        return consumeFlingInStretch(r4, this.mTopGlow, this.mBottomGlow, getHeight());
    }

    void consumePendingUpdateOperations() {
        if (this.mFirstLayoutComplete == true) goto L5;
    L25:
        TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
        dispatchLayout();
        TraceCompat.endSection();
        return;
    L5:
        if (this.mDataSetHasChangedAfterLayout == true) goto L25;
        if (this.mAdapterHelper.hasPendingUpdates() == true) goto L11;
        return;
    L11:
        if (this.mAdapterHelper.hasAnyUpdateTypes(4) == false) goto L22;
        if (this.mAdapterHelper.hasAnyUpdateTypes(11) == true) goto L22;
        TraceCompat.beginSection(TRACE_HANDLE_ADAPTER_UPDATES_TAG);
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mAdapterHelper.preProcess();
        if (this.mLayoutWasDefered == false) goto L17;
    L20:
        stopInterceptRequestLayout(true);
        onExitLayoutOrScroll();
        TraceCompat.endSection();
        return;
    L17:
        if (hasUpdatedView() == false) goto L19;
        dispatchLayout();
        goto L20
    L19:
        this.mAdapterHelper.consumePostponedUpdates();
    L22:
        if (this.mAdapterHelper.hasPendingUpdates() == false) goto L28;
        TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
        dispatchLayout();
        TraceCompat.endSection();
        return;
    }

    void defaultOnMeasure(int r4, int r5) {
        setMeasuredDimension(LayoutManager.chooseSize(r4, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth(this)), LayoutManager.chooseSize(r5, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight(this)));
    }

    void dispatchChildAttached(View r5) {
        ViewHolder r0 = getChildViewHolderInt(r5);
        onChildAttachedToWindow(r5);
        if (this.mAdapter == null) goto L7;
        if (r0 == null) goto L7;
        this.mAdapter.onViewAttachedToWindow(r0);
    L7:
        if (this.mOnChildAttachStateListeners == null) goto L11;
        int r2 = this.mOnChildAttachStateListeners.size() - 1;
    L9:
        if (r2 < 0) goto L13;
        this.mOnChildAttachStateListeners.get(r2).onChildViewAttachedToWindow(r5);
        r2 = r2 - 1;
        goto L9
    L13:
        return;
    }

    void dispatchChildDetached(View r5) {
        ViewHolder r0 = getChildViewHolderInt(r5);
        onChildDetachedFromWindow(r5);
        if (this.mAdapter == null) goto L7;
        if (r0 == null) goto L7;
        this.mAdapter.onViewDetachedFromWindow(r0);
    L7:
        if (this.mOnChildAttachStateListeners == null) goto L11;
        int r2 = this.mOnChildAttachStateListeners.size() - 1;
    L9:
        if (r2 < 0) goto L13;
        this.mOnChildAttachStateListeners.get(r2).onChildViewDetachedFromWindow(r5);
        r2 = r2 - 1;
        goto L9
    L13:
        return;
    }

    void dispatchLayout() {
        if (this.mAdapter != null) goto L7;
        Log.w(TAG, "No adapter attached; skipping layout");
        return;
    L7:
        if (this.mLayout != null) goto L10;
        Log.e(TAG, "No layout manager attached; skipping layout");
        return;
    L10:
        this.mState.mIsMeasuring = false;
        if (this.mLastAutoMeasureSkippedDueToExact == true) goto L13;
    L17:
        boolean r0 = false;
    L18:
        this.mLastAutoMeasureNonExactMeasuredWidth = 0;
        this.mLastAutoMeasureNonExactMeasuredHeight = 0;
        this.mLastAutoMeasureSkippedDueToExact = false;
        if (this.mState.mLayoutStep != 1) goto L22;
        dispatchLayoutStep1();
        this.mLayout.setExactMeasureSpecsFrom(this);
        dispatchLayoutStep2();
    L31:
        dispatchLayoutStep3();
        return;
    L22:
        if (this.mAdapterHelper.hasUpdates() == true) goto L30;
        if (r0 == true) goto L30;
        if (this.mLayout.getWidth() != getWidth()) goto L30;
        if (this.mLayout.getHeight() != getHeight()) goto L30;
        this.mLayout.setExactMeasureSpecsFrom(this);
    L30:
        this.mLayout.setExactMeasureSpecsFrom(this);
        dispatchLayoutStep2();
        goto L31
    L13:
        if (this.mLastAutoMeasureNonExactMeasuredWidth == getWidth()) goto L15;
    L16:
        r0 = true;
        goto L18
    L15:
        if (this.mLastAutoMeasureNonExactMeasuredHeight == getHeight()) goto L17;
        goto L16
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedFling(float r2, float r3, boolean r4) {
        return getScrollingChildHelper().dispatchNestedFling(r2, r3, r4);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedPreFling(float r2, float r3) {
        return getScrollingChildHelper().dispatchNestedPreFling(r2, r3);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedPreScroll(int r2, int r3, int[] r4, int[] r5) {
        return getScrollingChildHelper().dispatchNestedPreScroll(r2, r3, r4, r5);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean dispatchNestedPreScroll(int r7, int r8, int[] r9, int[] r10, int r11) {
        return getScrollingChildHelper().dispatchNestedPreScroll(r7, r8, r9, r10, r11);
    }

    @Override // androidx.core.view.NestedScrollingChild3
    public final void dispatchNestedScroll(int r9, int r10, int r11, int r12, int[] r13, int r14, int[] r15) {
        getScrollingChildHelper().dispatchNestedScroll(r9, r10, r11, r12, r13, r14, r15);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedScroll(int r7, int r8, int r9, int r10, int[] r11) {
        return getScrollingChildHelper().dispatchNestedScroll(r7, r8, r9, r10, r11);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean dispatchNestedScroll(int r8, int r9, int r10, int r11, int[] r12, int r13) {
        return getScrollingChildHelper().dispatchNestedScroll(r8, r9, r10, r11, r12, r13);
    }

    void dispatchOnScrollStateChanged(int r3) {
        if (this.mLayout == null) goto L5;
        this.mLayout.onScrollStateChanged(r3);
    L5:
        onScrollStateChanged(r3);
        if (this.mScrollListener == null) goto L9;
        this.mScrollListener.onScrollStateChanged(this, r3);
    L9:
        if (this.mScrollListeners == null) goto L13;
        int r0 = this.mScrollListeners.size() - 1;
    L11:
        if (r0 < 0) goto L15;
        this.mScrollListeners.get(r0).onScrollStateChanged(this, r3);
        r0 = r0 - 1;
        goto L11
    L15:
        return;
    }

    void dispatchOnScrolled(int r5, int r6) {
        this.mDispatchScrollCounter++;
        int r0 = getScrollX();
        int r1 = getScrollY();
        onScrollChanged(r0, r1, r0 - r5, r1 - r6);
        onScrolled(r5, r6);
        if (this.mScrollListener == null) goto L6;
        this.mScrollListener.onScrolled(this, r5, r6);
    L6:
        if (this.mScrollListeners == null) goto L10;
        int r2 = this.mScrollListeners.size() - 1;
    L8:
        if (r2 < 0) goto L10;
        this.mScrollListeners.get(r2).onScrolled(this, r5, r6);
        r2 = r2 - 1;
    L10:
        this.mDispatchScrollCounter--;
    }

    void dispatchPendingImportantForAccessibilityChanges() {
        int r0 = this.mPendingAccessibilityImportanceChange.size() - 1;
    L3:
        if (r0 < 0) goto L13;
        ViewHolder r1 = this.mPendingAccessibilityImportanceChange.get(r0);
        if (r1.itemView.getParent() != this) goto L12;
        if (r1.shouldIgnore() == true) goto L12;
        int r2 = r1.mPendingAccessibilityState;
        if (r2 == (-1)) goto L12;
        ViewCompat.setImportantForAccessibility(r1.itemView, r2);
        r1.mPendingAccessibilityState = -1;
    L12:
        r0 = r0 - 1;
        goto L3
    L13:
        this.mPendingAccessibilityImportanceChange.clear();
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent r2) {
        onPopulateAccessibilityEvent(r2);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> r1) {
        dispatchThawSelfOnly(r1);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> r1) {
        dispatchFreezeSelfOnly(r1);
    }

    @Override // android.view.View
    public void draw(Canvas r10) {
        super.draw(r10);
        int r0 = this.mItemDecorations.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.mItemDecorations.get(r1).onDrawOver(r10, this, this.mState);
        r1 = r1 + 1;
        goto L3
    L5:
        boolean r2 = false;
        boolean r3 = true;
        if (this.mLeftGlow == null) goto L21;
        if (this.mLeftGlow.isFinished() == true) goto L21;
        int r4 = r10.save();
        if (this.mClipToPadding == false) goto L12;
        int r5 = getPaddingBottom();
    L13:
        r10.rotate(270.0f);
        r10.translate((-getHeight()) + r5, 0.0f);
        if (this.mLeftGlow != null) goto L16;
    L18:
        boolean r6 = false;
    L19:
        r2 = r6;
        r10.restoreToCount(r4);
        goto L21
    L16:
        if (this.mLeftGlow.draw(r10) == false) goto L18;
        r6 = true;
        goto L19
    L12:
        r5 = 0;
    L21:
        if (this.mTopGlow == null) goto L35;
        if (this.mTopGlow.isFinished() == true) goto L35;
        int r7 = r10.save();
        if (this.mClipToPadding == false) goto L28;
        r10.translate(getPaddingLeft(), getPaddingTop());
    L28:
        if (this.mTopGlow != null) goto L30;
    L32:
        boolean r8 = false;
    L33:
        r2 = r2 | r8;
        r10.restoreToCount(r7);
        goto L35
    L30:
        if (this.mTopGlow.draw(r10) == false) goto L32;
        r8 = true;
    L35:
        if (this.mRightGlow == null) goto L50;
        if (this.mRightGlow.isFinished() == true) goto L50;
        int r9 = r10.save();
        int r11 = getWidth();
        if (this.mClipToPadding == false) goto L41;
        int r12 = getPaddingTop();
    L42:
        r10.rotate(90.0f);
        r10.translate(r12, -r11);
        if (this.mRightGlow != null) goto L45;
    L47:
        boolean r13 = false;
    L48:
        r2 = r2 | r13;
        r10.restoreToCount(r9);
        goto L50
    L45:
        if (this.mRightGlow.draw(r10) == false) goto L47;
        r13 = true;
        goto L48
    L41:
        r12 = 0;
    L50:
        if (this.mBottomGlow != null) goto L52;
    L64:
        if (r2 == false) goto L66;
    L72:
        if (r2 == false) goto L76;
        ViewCompat.postInvalidateOnAnimation(this);
        return;
    L76:
        return;
    L66:
        if (this.mItemAnimator == null) goto L72;
        if (this.mItemDecorations.size() <= 0) goto L72;
        if (this.mItemAnimator.isRunning() == false) goto L72;
        r2 = true;
        goto L72
    L52:
        if (this.mBottomGlow.isFinished() == true) goto L64;
        int r14 = r10.save();
        r10.rotate(180.0f);
        if (this.mClipToPadding == false) goto L56;
        r10.translate((-getWidth()) + getPaddingRight(), (-getHeight()) + getPaddingBottom());
    L58:
        if (this.mBottomGlow != null) goto L60;
    L62:
        r3 = false;
    L63:
        r2 = r2 | r3;
        r10.restoreToCount(r14);
        goto L64
    L60:
        if (this.mBottomGlow.draw(r10) == false) goto L62;
    L56:
        r10.translate(-getWidth(), -getHeight());
        goto L58
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas r2, View r3, long r4) {
        return super.drawChild(r2, r3, r4);
    }

    void ensureBottomGlow() {
        if (this.mBottomGlow == null) goto L5;
        return;
    L5:
        this.mBottomGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
        if (this.mClipToPadding == false) goto L8;
        this.mBottomGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
        return;
    L8:
        this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    }

    void ensureLeftGlow() {
        if (this.mLeftGlow == null) goto L5;
        return;
    L5:
        this.mLeftGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
        if (this.mClipToPadding == false) goto L8;
        this.mLeftGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
        return;
    L8:
        this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    }

    void ensureRightGlow() {
        if (this.mRightGlow == null) goto L5;
        return;
    L5:
        this.mRightGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
        if (this.mClipToPadding == false) goto L8;
        this.mRightGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
        return;
    L8:
        this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    }

    void ensureTopGlow() {
        if (this.mTopGlow == null) goto L5;
        return;
    L5:
        this.mTopGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
        if (this.mClipToPadding == false) goto L8;
        this.mTopGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
        return;
    L8:
        this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    }

    String exceptionLabel() {
        return " " + super.toString() + ", adapter:" + this.mAdapter + ", layout:" + this.mLayout + ", context:" + getContext();
    }

    final void fillRemainingScrollValues(State r4) {
        if (getScrollState() != 2) goto L5;
        OverScroller r0 = this.mViewFlinger.mOverScroller;
        r4.mRemainingScrollHorizontal = r0.getFinalX() - r0.getCurrX();
        r4.mRemainingScrollVertical = r0.getFinalY() - r0.getCurrY();
        return;
    L5:
        r4.mRemainingScrollHorizontal = 0;
        r4.mRemainingScrollVertical = 0;
    }

    public View findChildViewUnder(float r7, float r8) {
        int r1 = this.mChildHelper.getChildCount() - 1;
    L3:
        if (r1 < 0) goto L14;
        View r2 = this.mChildHelper.getChildAt(r1);
        float r3 = r2.getTranslationX();
        float r4 = r2.getTranslationY();
        if (r7 < (r2.getLeft() + r3)) goto L13;
        if (r7 > (r2.getRight() + r3)) goto L13;
        if (r8 < (r2.getTop() + r4)) goto L13;
        if (r8 > (r2.getBottom() + r4)) goto L13;
        return r2;
    L13:
        r1 = r1 - 1;
        goto L3
    L14:
        return null;
    }

    public View findContainingItemView(View r3) {
        ViewParent r0 = r3.getParent();
    L3:
        if (r0 == null) goto L8;
        if (r0 == this) goto L8;
        if ((r0 instanceof View) == false) goto L8;
        r3 = r0;
        r0 = r3.getParent();
    L8:
        if (r0 == this) goto L15;
        return null;
    L15:
        return r3;
    }

    public ViewHolder findContainingViewHolder(View r3) {
        View r0 = findContainingItemView(r3);
        if (r0 != null) goto L6;
        return null;
    L6:
        return getChildViewHolder(r0);
    }

    public ViewHolder findViewHolderForAdapterPosition(int r7) {
        if (this.mDataSetHasChangedAfterLayout == false) goto L6;
        return null;
    L6:
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder r1 = null;
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L19;
        ViewHolder r3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r2));
        if (r3 == null) goto L18;
        if (r3.isRemoved() == true) goto L18;
        if (getAdapterPositionInRecyclerView(r3) != r7) goto L18;
        if (this.mChildHelper.isHidden(r3.itemView) == false) goto L17;
        r1 = r3;
        goto L18
    L17:
        return r3;
    L18:
        r2 = r2 + 1;
        goto L7
    L19:
        return r1;
    }

    public ViewHolder findViewHolderForItemId(long r7) {
        if (this.mAdapter != null) goto L5;
        return null;
    L5:
        if (this.mAdapter.hasStableIds() == false) goto L29;
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder r1 = null;
        int r2 = 0;
    L8:
        if (r2 >= r0) goto L20;
        ViewHolder r3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r2));
        if (r3 == null) goto L19;
        if (r3.isRemoved() == true) goto L19;
        if (r3.getItemId() != r7) goto L19;
        if (this.mChildHelper.isHidden(r3.itemView) == false) goto L18;
        r1 = r3;
        goto L19
    L18:
        return r3;
    L19:
        r2 = r2 + 1;
        goto L8
    L20:
        return r1;
    L29:
        return null;
    }

    public ViewHolder findViewHolderForLayoutPosition(int r2) {
        return findViewHolderForPosition(r2, false);
    }

    @Deprecated
    public ViewHolder findViewHolderForPosition(int r2) {
        return findViewHolderForPosition(r2, false);
    }

    ViewHolder findViewHolderForPosition(int r7, boolean r8) {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder r1 = null;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L20;
        ViewHolder r3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r2));
        if (r3 == null) goto L19;
        if (r3.isRemoved() == true) goto L19;
        if (r8 == false) goto L13;
        if (r3.mPosition != r7) goto L19;
    L16:
        if (this.mChildHelper.isHidden(r3.itemView) == false) goto L18;
        r1 = r3;
        goto L19
    L18:
        return r3;
    L13:
        if (r3.getLayoutPosition() == r7) goto L16;
    L19:
        r2 = r2 + 1;
        goto L3
    L20:
        return r1;
    }

    public boolean fling(int r10, int r11) {
        if (this.mLayout != null) goto L7;
        Log.e(TAG, "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return false;
    L7:
        if (this.mLayoutSuppressed == false) goto L9;
        return false;
    L9:
        boolean r0 = this.mLayout.canScrollHorizontally();
        boolean r2 = this.mLayout.canScrollVertically();
        if (r0 == true) goto L12;
    L13:
        r10 = 0;
    L14:
        if (r2 == true) goto L16;
    L17:
        r11 = 0;
    L18:
        if (r10 != 0) goto L21;
        if (r11 != 0) goto L21;
        return false;
    L21:
        int r3 = 0;
        int r4 = 0;
        if (r10 != 0) goto L24;
    L41:
        if (r11 != 0) goto L43;
    L60:
        if (r3 != 0) goto L62;
        if (r4 != 0) goto L62;
    L64:
        if (r10 != 0) goto L71;
        if (r11 != 0) goto L71;
        if (r3 != 0) goto L69;
        if (r4 != 0) goto L69;
        return false;
    L69:
        return true;
    L71:
        if (dispatchNestedPreFling(r10, r11) == true) goto L90;
        if (r0 == true) goto L76;
        if (r2 == true) goto L76;
        boolean r6 = false;
    L77:
        dispatchNestedFling(r10, r11, r6);
        if (this.mOnFlingListener != null) goto L80;
    L82:
        if (r6 == false) goto L90;
        int r1 = 0;
        if (r0 == false) goto L86;
        r1 = 0 | 1;
    L86:
        if (r2 == false) goto L88;
        r1 = r1 | 2;
    L88:
        startNestedScroll(r1, 1);
        this.mViewFlinger.fling(Math.max(-this.mMaxFlingVelocity, Math.min(r10, this.mMaxFlingVelocity)), Math.max(-this.mMaxFlingVelocity, Math.min(r11, this.mMaxFlingVelocity)));
        return true;
    L80:
        if (this.mOnFlingListener.onFling(r10, r11) == false) goto L82;
        return true;
    L76:
        r6 = true;
    L90:
        return false;
    L62:
        r3 = Math.max(-this.mMaxFlingVelocity, Math.min(r3, this.mMaxFlingVelocity));
        r4 = Math.max(-this.mMaxFlingVelocity, Math.min(r4, this.mMaxFlingVelocity));
        this.mViewFlinger.fling(r3, r4);
        goto L64
    L43:
        if (this.mTopGlow == null) goto L52;
        if (EdgeEffectCompat.getDistance(this.mTopGlow) == 0.0f) goto L52;
        if (shouldAbsorb(this.mTopGlow, -r11, getHeight()) == false) goto L49;
        this.mTopGlow.onAbsorb(-r11);
    L50:
        r11 = 0;
        goto L60
    L49:
        r4 = r11;
    L52:
        if (this.mBottomGlow == null) goto L60;
        if (EdgeEffectCompat.getDistance(this.mBottomGlow) == 0.0f) goto L60;
        if (shouldAbsorb(this.mBottomGlow, r11, getHeight()) == false) goto L58;
        this.mBottomGlow.onAbsorb(r11);
    L59:
        r11 = 0;
        goto L60
    L58:
        r4 = r11;
        goto L59
    L24:
        if (this.mLeftGlow == null) goto L33;
        if (EdgeEffectCompat.getDistance(this.mLeftGlow) == 0.0f) goto L33;
        if (shouldAbsorb(this.mLeftGlow, -r10, getWidth()) == false) goto L30;
        this.mLeftGlow.onAbsorb(-r10);
    L31:
        r10 = 0;
        goto L41
    L30:
        r3 = r10;
    L33:
        if (this.mRightGlow == null) goto L41;
        if (EdgeEffectCompat.getDistance(this.mRightGlow) == 0.0f) goto L41;
        if (shouldAbsorb(this.mRightGlow, r10, getWidth()) == false) goto L39;
        this.mRightGlow.onAbsorb(r10);
    L40:
        r10 = 0;
        goto L41
    L39:
        r3 = r10;
        goto L40
    L16:
        if (Math.abs(r11) >= this.mMinFlingVelocity) goto L18;
    L12:
        if (Math.abs(r10) >= this.mMinFlingVelocity) goto L14;
        goto L13
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View r12, int r13) {
        View r0 = this.mLayout.onInterceptFocusSearch(r12, r13);
        if (r0 == null) goto L5;
        return r0;
    L5:
        boolean r2 = true;
        if (this.mAdapter != null) goto L8;
    L14:
        boolean r1 = false;
    L15:
        FocusFinder r4 = FocusFinder.getInstance();
        if (r1 == true) goto L18;
    L59:
        View r3 = r4.findNextFocus(this, r12, r13);
        if (r3 != null) goto L66;
        if (r1 == false) goto L66;
        consumePendingUpdateOperations();
        if (findContainingItemView(r12) != null) goto L65;
        return null;
    L65:
        startInterceptRequestLayout();
        r3 = this.mLayout.onFocusSearchFailed(r12, r13, this.mRecycler, this.mState);
        stopInterceptRequestLayout(false);
    L66:
        if (r3 == null) goto L76;
        if (r3.hasFocusable() == true) goto L76;
        if (getFocusedChild() == null) goto L72;
        requestChildOnScreen(r3, null);
        return r12;
    L72:
        return super.focusSearch(r12, r13);
    L76:
        if (isPreferredNextFocus(r12, r3, r13) == false) goto L79;
        return r3;
    L79:
        return super.focusSearch(r12, r13);
    L18:
        if (r13 == 2) goto L20;
        if (r13 != 1) goto L59;
    L20:
        boolean r7 = false;
        if (this.mLayout.canScrollVertically() == false) goto L32;
        if (r13 != 2) goto L24;
        int r8 = TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
    L26:
        if (r4.findNextFocus(this, r12, r8) != null) goto L28;
        boolean r10 = true;
    L29:
        r7 = r10;
        if (FORCE_ABS_FOCUS_SEARCH_DIRECTION == false) goto L32;
        r13 = r8;
        goto L32
    L28:
        r10 = false;
        goto L29
    L24:
        r8 = 33;
    L32:
        if (r7 == false) goto L34;
    L53:
        if (r7 == false) goto L58;
        consumePendingUpdateOperations();
        if (findContainingItemView(r12) != null) goto L57;
        return null;
    L57:
        startInterceptRequestLayout();
        this.mLayout.onFocusSearchFailed(r12, r13, this.mRecycler, this.mState);
        stopInterceptRequestLayout(false);
    L58:
        r3 = r4.findNextFocus(this, r12, r13);
        goto L66
    L34:
        if (this.mLayout.canScrollHorizontally() == false) goto L53;
        if (this.mLayout.getLayoutDirection() != 1) goto L38;
        boolean r9 = true;
    L39:
        if (r13 != 2) goto L41;
        boolean r6 = true;
    L43:
        if ((r6 ^ r9) == false) goto L45;
        int r11 = 66;
    L47:
        if (r4.findNextFocus(this, r12, r11) == null) goto L50;
        r2 = false;
    L50:
        r7 = r2;
        if (FORCE_ABS_FOCUS_SEARCH_DIRECTION == false) goto L53;
        r13 = r11;
        goto L53
    L45:
        r11 = 17;
        goto L47
    L41:
        r6 = false;
        goto L43
    L38:
        r9 = false;
        goto L39
    L8:
        if (this.mLayout == null) goto L14;
        if (isComputingLayout() == true) goto L14;
        if (this.mLayoutSuppressed == true) goto L14;
        r1 = true;
        goto L15
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout == null) goto L7;
        return this.mLayout.generateDefaultLayoutParams();
    L7:
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet r4) {
        if (this.mLayout == null) goto L7;
        return this.mLayout.generateLayoutParams(getContext(), r4);
    L7:
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams r4) {
        if (this.mLayout == null) goto L7;
        return this.mLayout.generateLayoutParams(r4);
    L7:
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return "androidx.recyclerview.widget.RecyclerView";
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    int getAdapterPositionInRecyclerView(ViewHolder r3) {
        if (r3.hasAnyOfTheFlags(524) == false) goto L5;
        return -1;
    L5:
        if (r3.isBound() == true) goto L8;
        return -1;
    L8:
        return this.mAdapterHelper.applyPendingUpdatesToPosition(r3.mPosition);
    }

    @Override // android.view.View
    public int getBaseline() {
        if (this.mLayout == null) goto L7;
        return this.mLayout.getBaseline();
    L7:
        return super.getBaseline();
    }

    long getChangedHolderKey(ViewHolder r3) {
        if (this.mAdapter.hasStableIds() == false) goto L6;
        return r3.getItemId();
    L6:
        return r3.mPosition;
    }

    public int getChildAdapterPosition(View r3) {
        ViewHolder r0 = getChildViewHolderInt(r3);
        if (r0 != null) goto L7;
        return -1;
    L7:
        return r0.getAbsoluteAdapterPosition();
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int r2, int r3) {
        if (this.mChildDrawingOrderCallback != null) goto L7;
        return super.getChildDrawingOrder(r2, r3);
    L7:
        return this.mChildDrawingOrderCallback.onGetChildDrawingOrder(r2, r3);
    }

    public long getChildItemId(View r4) {
        if (this.mAdapter != null) goto L5;
    L11:
        return -1;
    L5:
        if (this.mAdapter.hasStableIds() == false) goto L11;
        ViewHolder r0 = getChildViewHolderInt(r4);
        if (r0 != null) goto L10;
        return -1;
    L10:
        return r0.getItemId();
    }

    public int getChildLayoutPosition(View r3) {
        ViewHolder r0 = getChildViewHolderInt(r3);
        if (r0 != null) goto L7;
        return -1;
    L7:
        return r0.getLayoutPosition();
    }

    @Deprecated
    public int getChildPosition(View r2) {
        return getChildAdapterPosition(r2);
    }

    public ViewHolder getChildViewHolder(View r5) {
        ViewParent r0 = r5.getParent();
        if (r0 == null) goto L9;
        if (r0 == this) goto L9;
        throw new IllegalArgumentException("View " + r5 + " is not a direct child of " + this);
    L9:
        return getChildViewHolderInt(r5);
    }

    @Override // android.view.ViewGroup
    public boolean getClipToPadding() {
        return this.mClipToPadding;
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void getDecoratedBoundsWithMargins(View r1, Rect r2) {
        getDecoratedBoundsWithMarginsInt(r1, r2);
    }

    public EdgeEffectFactory getEdgeEffectFactory() {
        return this.mEdgeEffectFactory;
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    Rect getItemDecorInsetsForChild(View r9) {
        LayoutParams r0 = (LayoutParams) r9.getLayoutParams();
        if (r0.mInsetsDirty == true) goto L7;
        return r0.mDecorInsets;
    L7:
        if (this.mState.isPreLayout() == true) goto L9;
    L14:
        Rect r1 = r0.mDecorInsets;
        r1.set(0, 0, 0, 0);
        int r3 = this.mItemDecorations.size();
        int r4 = 0;
    L15:
        if (r4 >= r3) goto L17;
        this.mTempRect.set(0, 0, 0, 0);
        this.mItemDecorations.get(r4).getItemOffsets(this.mTempRect, r9, this, this.mState);
        r1.left += this.mTempRect.left;
        r1.top += this.mTempRect.top;
        r1.right += this.mTempRect.right;
        r1.bottom += this.mTempRect.bottom;
        r4 = r4 + 1;
        goto L15
    L17:
        r0.mInsetsDirty = false;
        return r1;
    L9:
        if (r0.isItemChanged() == true) goto L13;
        if (r0.isViewInvalid() == false) goto L14;
    L13:
        return r0.mDecorInsets;
    }

    public ItemDecoration getItemDecorationAt(int r5) {
        int r0 = getItemDecorationCount();
        if (r5 < 0) goto L8;
        if (r5 >= r0) goto L8;
        return this.mItemDecorations.get(r5);
    L8:
        throw new IndexOutOfBoundsException(r5 + " is an invalid index for size " + r0);
    }

    public int getItemDecorationCount() {
        return this.mItemDecorations.size();
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }

    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }

    long getNanoTime() {
        if (ALLOW_THREAD_GAP_WORK == true) goto L5;
        return 0;
    L5:
        return System.nanoTime();
    }

    public OnFlingListener getOnFlingListener() {
        return this.mOnFlingListener;
    }

    public boolean getPreserveFocusAfterLayout() {
        return this.mPreserveFocusAfterLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean hasNestedScrollingParent(int r2) {
        return getScrollingChildHelper().hasNestedScrollingParent(r2);
    }

    public boolean hasPendingAdapterUpdates() {
        if (this.mFirstLayoutComplete == true) goto L5;
    L10:
        return true;
    L5:
        if (this.mDataSetHasChangedAfterLayout == true) goto L10;
        if (this.mAdapterHelper.hasPendingUpdates() == true) goto L10;
        return false;
    }

    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new C05096(this));
    }

    void initFastScroller(StateListDrawable r11, Drawable r12, StateListDrawable r13, Drawable r14) {
        if (r11 == null) goto L9;
        if (r12 == null) goto L9;
        if (r13 == null) goto L9;
        if (r14 == null) goto L9;
        Resources r0 = getContext().getResources();
        new FastScroller(this, r11, r12, r13, r14, r0.getDimensionPixelSize(C0472R.dimen.fastscroll_default_thickness), r0.getDimensionPixelSize(C0472R.dimen.fastscroll_minimum_range), r0.getDimensionPixelOffset(C0472R.dimen.fastscroll_margin));
        return;
    L9:
        throw new IllegalArgumentException("Trying to set fast scroller without both required drawables." + exceptionLabel());
    }

    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() != 0) goto L6;
        return;
    L6:
        if (this.mLayout == null) goto L8;
        this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
    L8:
        markItemDecorInsetsDirty();
        requestLayout();
    }

    boolean isAccessibilityEnabled() {
        if (this.mAccessibilityManager != null) goto L5;
    L7:
        return false;
    L5:
        if (this.mAccessibilityManager.isEnabled() == false) goto L7;
        return true;
    }

    public boolean isAnimating() {
        if (this.mItemAnimator != null) goto L5;
    L7:
        return false;
    L5:
        if (this.mItemAnimator.isRunning() == false) goto L7;
        return true;
    }

    @Override // android.view.View
    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    public boolean isComputingLayout() {
        if (this.mLayoutOrScrollCounter <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Deprecated
    public boolean isLayoutFrozen() {
        return isLayoutSuppressed();
    }

    @Override // android.view.ViewGroup
    public final boolean isLayoutSuppressed() {
        return this.mLayoutSuppressed;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    void jumpToPositionForSmoothScroller(int r2) {
        if (this.mLayout != null) goto L5;
        return;
    L5:
        setScrollState(2);
        this.mLayout.scrollToPosition(r2);
        awakenScrollBars();
    }

    void markItemDecorInsetsDirty() {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        ((LayoutParams) this.mChildHelper.getUnfilteredChildAt(r1).getLayoutParams()).mInsetsDirty = true;
        r1 = r1 + 1;
        goto L3
    L5:
        this.mRecycler.markItemDecorInsetsDirty();
    }

    void markKnownViewsInvalid() {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L10;
        ViewHolder r2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r1));
        if (r2 == null) goto L9;
        if (r2.shouldIgnore() == true) goto L9;
        r2.addFlags(6);
    L9:
        r1 = r1 + 1;
        goto L3
    L10:
        markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public void nestedScrollBy(int r3, int r4) {
        nestedScrollByInternal(r3, r4, null, 1);
    }

    public void offsetChildrenHorizontal(int r4) {
        int r0 = this.mChildHelper.getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.mChildHelper.getChildAt(r1).offsetLeftAndRight(r4);
        r1 = r1 + 1;
        goto L3
    }

    public void offsetChildrenVertical(int r4) {
        int r0 = this.mChildHelper.getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.mChildHelper.getChildAt(r1).offsetTopAndBottom(r4);
        r1 = r1 + 1;
        goto L3
    }

    void offsetPositionRecordsForInsert(int r6, int r7) {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L12;
        ViewHolder r2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r1));
        if (r2 == null) goto L11;
        if (r2.shouldIgnore() == true) goto L11;
        if (r2.mPosition < r6) goto L11;
        r2.offsetPosition(r7, false);
        this.mState.mStructureChanged = true;
    L11:
        r1 = r1 + 1;
        goto L3
    L12:
        this.mRecycler.offsetPositionRecordsForInsert(r6, r7);
        requestLayout();
    }

    void offsetPositionRecordsForMove(int r9, int r10) {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        if (r9 >= r10) goto L5;
        int r1 = r9;
        int r2 = r10;
        int r3 = -1;
    L6:
        int r4 = 0;
    L7:
        if (r4 >= r0) goto L21;
        ViewHolder r5 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r4));
        if (r5 == null) goto L20;
        if (r5.mPosition < r1) goto L20;
        if (r5.mPosition > r2) goto L20;
        if (r5.mPosition != r9) goto L18;
        r5.offsetPosition(r10 - r9, false);
    L19:
        this.mState.mStructureChanged = true;
        goto L20
    L18:
        r5.offsetPosition(r3, false);
    L20:
        r4 = r4 + 1;
        goto L7
    L21:
        this.mRecycler.offsetPositionRecordsForMove(r9, r10);
        requestLayout();
        return;
    L5:
        r1 = r10;
        r2 = r9;
        r3 = 1;
        goto L6
    }

    void offsetPositionRecordsForRemove(int r8, int r9, boolean r10) {
        int r0 = r8 + r9;
        int r1 = this.mChildHelper.getUnfilteredChildCount();
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L15;
        ViewHolder r3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r2));
        if (r3 == null) goto L14;
        if (r3.shouldIgnore() == true) goto L14;
        if (r3.mPosition < r0) goto L12;
        r3.offsetPosition(-r9, r10);
        this.mState.mStructureChanged = true;
        goto L14
    L12:
        if (r3.mPosition < r8) goto L14;
        r3.flagRemovedAndOffsetPosition(r8 - 1, -r9, r10);
        this.mState.mStructureChanged = true;
    L14:
        r2 = r2 + 1;
        goto L3
    L15:
        this.mRecycler.offsetPositionRecordsForRemove(r8, r9, r10);
        requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        boolean r1 = true;
        this.mIsAttached = true;
        if (this.mFirstLayoutComplete == true) goto L5;
    L7:
        r1 = false;
    L8:
        this.mFirstLayoutComplete = r1;
        this.mRecycler.onAttachedToWindow();
        if (this.mLayout == null) goto L11;
        this.mLayout.dispatchAttachedToWindow(this);
    L11:
        this.mPostedAnimatorRunner = false;
        if (ALLOW_THREAD_GAP_WORK == false) goto L24;
        this.mGapWorker = GapWorker.sGapWorker.get();
        if (this.mGapWorker != null) goto L22;
        this.mGapWorker = new GapWorker();
        Display r0 = ViewCompat.getDisplay(this);
        float r2 = 60.0f;
        if (isInEditMode() == true) goto L21;
        if (r0 == null) goto L21;
        float r3 = r0.getRefreshRate();
        if (r3 < 30.0f) goto L21;
        r2 = r3;
    L21:
        this.mGapWorker.mFrameIntervalNs = (long) (1.0E9f / r2);
        GapWorker.sGapWorker.set(this.mGapWorker);
    L22:
        this.mGapWorker.add(this);
        return;
    L24:
        return;
    L5:
        if (isLayoutRequested() == true) goto L7;
        goto L7
    }

    public void onChildAttachedToWindow(View r1) {
    }

    public void onChildDetachedFromWindow(View r1) {
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator == null) goto L5;
        this.mItemAnimator.endAnimations();
    L5:
        stopScroll();
        this.mIsAttached = false;
        if (this.mLayout == null) goto L8;
        this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
    L8:
        this.mPendingAccessibilityImportanceChange.clear();
        removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
        this.mRecycler.onDetachedFromWindow();
        PoolingContainer.callPoolingContainerOnReleaseForChildren(this);
        if (ALLOW_THREAD_GAP_WORK == true) goto L11;
        return;
    L11:
        if (this.mGapWorker == null) goto L15;
        this.mGapWorker.remove(this);
        this.mGapWorker = null;
        return;
    }

    @Override // android.view.View
    public void onDraw(Canvas r5) {
        super.onDraw(r5);
        int r0 = this.mItemDecorations.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.mItemDecorations.get(r1).onDraw(r5, this, this.mState);
        r1 = r1 + 1;
        goto L3
    }

    void onEnterLayoutOrScroll() {
        this.mLayoutOrScrollCounter++;
    }

    void onExitLayoutOrScroll() {
        onExitLayoutOrScroll(true);
    }

    void onExitLayoutOrScroll(boolean r3) {
        this.mLayoutOrScrollCounter--;
        if (this.mLayoutOrScrollCounter >= 1) goto L8;
        this.mLayoutOrScrollCounter = 0;
        if (r3 == false) goto L9;
        dispatchContentChangedIfNecessary();
        dispatchPendingImportantForAccessibilityChanges();
        return;
    L9:
        return;
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent r7) {
        if (this.mLayout != null) goto L6;
        return false;
    L6:
        if (this.mLayoutSuppressed == false) goto L9;
        return false;
    L9:
        if (r7.getAction() == 8) goto L11;
    L36:
        return false;
    L11:
        if ((r7.getSource() & 2) == 0) goto L21;
        if (this.mLayout.canScrollVertically() == false) goto L15;
        float r0 = -r7.getAxisValue(9);
    L17:
        if (this.mLayout.canScrollHorizontally() == false) goto L19;
        float r2 = r7.getAxisValue(10);
    L32:
        if (r0 == 0.0f) goto L34;
    L35:
        nestedScrollByInternal((int) (this.mScaledHorizontalScrollFactor * r2), (int) (this.mScaledVerticalScrollFactor * r0), r7, 1);
        goto L36
    L34:
        if (r2 == 0.0f) goto L36;
    L19:
        r2 = 0.0f;
        goto L32
    L15:
        r0 = 0.0f;
        goto L17
    L21:
        if ((r7.getSource() & 4194304) == 0) goto L30;
        float r1 = r7.getAxisValue(26);
        if (this.mLayout.canScrollVertically() == false) goto L26;
        r0 = -r1;
        r2 = 0.0f;
        goto L32
    L26:
        if (this.mLayout.canScrollHorizontally() == false) goto L28;
        r0 = 0.0f;
        r2 = r1;
        goto L32
    L28:
        r0 = 0.0f;
        r2 = 0.0f;
        goto L32
    L30:
        r0 = 0.0f;
        r2 = 0.0f;
        goto L32
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent r15) {
        if (this.mLayoutSuppressed == false) goto L5;
        return false;
    L5:
        this.mInterceptingOnItemTouchListener = null;
        if (findInterceptingOnItemTouchListener(r15) == false) goto L10;
        cancelScroll();
        return true;
    L10:
        if (this.mLayout != null) goto L12;
        return false;
    L12:
        boolean r0 = this.mLayout.canScrollHorizontally();
        boolean r3 = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker != null) goto L15;
        this.mVelocityTracker = VelocityTracker.obtain();
    L15:
        this.mVelocityTracker.addMovement(r15);
        int r4 = r15.getActionMasked();
        int r5 = r15.getActionIndex();
        switch(r4) {
            case 0: goto L41;
            case 1: goto L39;
            case 2: goto L21;
            case 3: goto L20;
            case 4: goto L55;
            case 5: goto L19;
            case 6: goto L18;
            default: goto L55;
        };
    L18:
        onPointerUp(r15);
        goto L55
    L19:
        this.mScrollPointerId = r15.getPointerId(r5);
        int r7 = (int) (r15.getX(r5) + 0.5f);
        this.mLastTouchX = r7;
        this.mInitialTouchX = r7;
        int r6 = (int) (r15.getY(r5) + 0.5f);
        this.mLastTouchY = r6;
        this.mInitialTouchY = r6;
        goto L55
    L20:
        cancelScroll();
        goto L55
    L21:
        int r8 = r15.findPointerIndex(this.mScrollPointerId);
        if (r8 >= 0) goto L25;
        Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
        return false;
    L25:
        int r9 = (int) (r15.getX(r8) + 0.5f);
        int r10 = (int) (r15.getY(r8) + 0.5f);
        if (this.mScrollState == 1) goto L55;
        int r11 = r9 - this.mInitialTouchX;
        int r12 = r10 - this.mInitialTouchY;
        boolean r13 = false;
        if (r0 == true) goto L30;
    L32:
        if (r3 == true) goto L34;
    L36:
        if (r13 == false) goto L55;
        setScrollState(1);
        goto L55
    L34:
        if (Math.abs(r12) <= this.mTouchSlop) goto L36;
        this.mLastTouchY = r10;
        r13 = true;
        goto L36
    L30:
        if (Math.abs(r11) <= this.mTouchSlop) goto L32;
        this.mLastTouchX = r9;
        r13 = true;
        goto L32
    L39:
        this.mVelocityTracker.clear();
        stopNestedScroll(0);
        goto L55
    L41:
        if (this.mIgnoreMotionEventTillDown == false) goto L43;
        this.mIgnoreMotionEventTillDown = false;
    L43:
        this.mScrollPointerId = r15.getPointerId(0);
        int r14 = (int) (r15.getX() + 0.5f);
        this.mLastTouchX = r14;
        this.mInitialTouchX = r14;
        int r16 = (int) (r15.getY() + 0.5f);
        this.mLastTouchY = r16;
        this.mInitialTouchY = r16;
        if (stopGlowAnimations(r15) == false) goto L46;
    L47:
        getParent().requestDisallowInterceptTouchEvent(true);
        setScrollState(1);
        stopNestedScroll(1);
    L48:
        int[] r17 = this.mNestedOffsets;
        this.mNestedOffsets[1] = 0;
        r17[0] = 0;
        int r18 = 0;
        if (r0 == false) goto L51;
        r18 = 0 | 1;
    L51:
        if (r3 == false) goto L53;
        r18 = r18 | 2;
    L53:
        startNestedScroll(r18, 0);
        goto L55
    L46:
        if (this.mScrollState != 2) goto L48;
    L55:
        if (this.mScrollState == 1) goto L57;
        return false;
    L57:
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean r2, int r3, int r4, int r5, int r6) {
        TraceCompat.beginSection(TRACE_ON_LAYOUT_TAG);
        dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }

    @Override // android.view.View
    protected void onMeasure(int r8, int r9) {
        if (this.mLayout != null) goto L6;
        defaultOnMeasure(r8, r9);
        return;
    L6:
        boolean r2 = false;
        if (this.mLayout.isAutoMeasureEnabled() == false) goto L26;
        int r0 = View.MeasureSpec.getMode(r8);
        int r3 = View.MeasureSpec.getMode(r9);
        this.mLayout.onMeasure(this.mRecycler, this.mState, r8, r9);
        if (r0 != 1073741824) goto L12;
        if (r3 != 1073741824) goto L12;
        r2 = true;
    L12:
        this.mLastAutoMeasureSkippedDueToExact = r2;
        if (this.mLastAutoMeasureSkippedDueToExact == false) goto L15;
        return;
    L15:
        if (this.mAdapter != null) goto L18;
        return;
    L18:
        if (this.mState.mLayoutStep != 1) goto L20;
        dispatchLayoutStep1();
    L20:
        this.mLayout.setMeasureSpecs(r8, r9);
        this.mState.mIsMeasuring = true;
        dispatchLayoutStep2();
        this.mLayout.setMeasuredDimensionFromChildren(r8, r9);
        if (this.mLayout.shouldMeasureTwice() == false) goto L23;
        this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        this.mState.mIsMeasuring = true;
        dispatchLayoutStep2();
        this.mLayout.setMeasuredDimensionFromChildren(r8, r9);
    L23:
        this.mLastAutoMeasureNonExactMeasuredWidth = getMeasuredWidth();
        this.mLastAutoMeasureNonExactMeasuredHeight = getMeasuredHeight();
        return;
    L26:
        if (this.mHasFixedSize == false) goto L30;
        this.mLayout.onMeasure(this.mRecycler, this.mState, r8, r9);
        return;
    L30:
        if (this.mAdapterUpdateDuringMeasure == false) goto L37;
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        onExitLayoutOrScroll();
        if (this.mState.mRunPredictiveAnimations == false) goto L34;
        this.mState.mInPreLayout = true;
    L35:
        this.mAdapterUpdateDuringMeasure = false;
        stopInterceptRequestLayout(false);
    L41:
        if (this.mAdapter == null) goto L43;
        this.mState.mItemCount = this.mAdapter.getItemCount();
    L44:
        startInterceptRequestLayout();
        this.mLayout.onMeasure(this.mRecycler, this.mState, r8, r9);
        stopInterceptRequestLayout(false);
        this.mState.mInPreLayout = false;
        return;
    L43:
        this.mState.mItemCount = 0;
        goto L44
    L34:
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mInPreLayout = false;
        goto L35
    L37:
        if (this.mState.mRunPredictiveAnimations == false) goto L41;
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int r2, Rect r3) {
        if (isComputingLayout() == false) goto L7;
        return false;
    L7:
        return super.onRequestFocusInDescendants(r2, r3);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable r2) {
        if ((r2 instanceof SavedState) == true) goto L6;
        super.onRestoreInstanceState(r2);
        return;
    L6:
        this.mPendingSavedState = (SavedState) r2;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState r0 = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState == null) goto L6;
        r0.copyFrom(this.mPendingSavedState);
    L9:
        return r0;
    L6:
        if (this.mLayout == null) goto L8;
        r0.mLayoutState = this.mLayout.onSaveInstanceState();
        goto L9
    L8:
        r0.mLayoutState = null;
        goto L9
    }

    public void onScrollStateChanged(int r1) {
    }

    public void onScrolled(int r1, int r2) {
    }

    @Override // android.view.View
    protected void onSizeChanged(int r1, int r2, int r3, int r4) {
        super.onSizeChanged(r1, r2, r3, r4);
        if (r1 != r3) goto L5;
        if (r2 != r4) goto L5;
        return;
    L5:
        invalidateGlows();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent r21) {
        if (this.mLayoutSuppressed == false) goto L5;
    L104:
        return false;
    L5:
        if (this.mIgnoreMotionEventTillDown == true) goto L104;
        int r8 = 1;
        if (dispatchToOnItemTouchListeners(r21) == false) goto L12;
        cancelScroll();
        return true;
    L12:
        if (this.mLayout != null) goto L14;
        return false;
    L14:
        boolean r9 = this.mLayout.canScrollHorizontally();
        boolean r10 = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker != null) goto L17;
        this.mVelocityTracker = VelocityTracker.obtain();
    L17:
        boolean r11 = false;
        int r12 = r21.getActionMasked();
        int r13 = r21.getActionIndex();
        if (r12 != 0) goto L20;
        int[] r1 = this.mNestedOffsets;
        this.mNestedOffsets[1] = 0;
        r1[0] = 0;
    L20:
        MotionEvent r14 = MotionEvent.obtain(r21);
        r14.offsetLocation(this.mNestedOffsets[0], this.mNestedOffsets[1]);
        switch(r12) {
            case 0: goto L94;
            case 1: goto L79;
            case 2: goto L26;
            case 3: goto L25;
            case 4: goto L100;
            case 5: goto L24;
            case 6: goto L23;
            default: goto L100;
        };
    L23:
        onPointerUp(r21);
        goto L100
    L24:
        this.mScrollPointerId = r21.getPointerId(r13);
        int r2 = (int) (r21.getX(r13) + 0.5f);
        this.mLastTouchX = r2;
        this.mInitialTouchX = r2;
        int r3 = (int) (r21.getY(r13) + 0.5f);
        this.mLastTouchY = r3;
        this.mInitialTouchY = r3;
        goto L100
    L25:
        cancelScroll();
        goto L100
    L26:
        int r15 = r21.findPointerIndex(this.mScrollPointerId);
        if (r15 >= 0) goto L30;
        Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
        return false;
    L30:
        int r4 = (int) (r21.getX(r15) + 0.5f);
        int r5 = (int) (r21.getY(r15) + 0.5f);
        int r6 = this.mLastTouchX - r4;
        int r7 = this.mLastTouchY - r5;
        if (this.mScrollState == 1) goto L49;
        boolean r16 = false;
        if (r9 == false) goto L39;
        if (r6 <= 0) goto L36;
        r6 = Math.max(0, r6 - this.mTouchSlop);
    L37:
        if (r6 == 0) goto L39;
        r16 = true;
        goto L39
    L36:
        r6 = Math.min(0, this.mTouchSlop + r6);
    L39:
        if (r10 == false) goto L45;
        if (r7 <= 0) goto L42;
        r7 = Math.max(0, r7 - this.mTouchSlop);
    L43:
        if (r7 == 0) goto L45;
        r16 = true;
        goto L45
    L42:
        r7 = Math.min(0, this.mTouchSlop + r7);
    L45:
        if (r16 == false) goto L47;
        r8 = 1;
        setScrollState(1);
        goto L49
    L47:
        r8 = 1;
    L49:
        if (this.mScrollState != r8) goto L100;
        this.mReusableIntPair[0] = 0;
        this.mReusableIntPair[r8] = 0;
        int r17 = r6 - releaseHorizontalGlow(r6, r21.getY());
        int r18 = r7 - releaseVerticalGlow(r7, r21.getX());
        if (r9 == false) goto L53;
        int r19 = r17;
    L55:
        if (r10 == false) goto L57;
        int r20 = r18;
    L59:
        if (dispatchNestedPreScroll(r19, r20, this.mReusableIntPair, this.mScrollOffset, 0) == false) goto L61;
        r17 = r17 - this.mReusableIntPair[0];
        char r22 = 1;
        int r110 = r18 - this.mReusableIntPair[1];
        int[] r23 = this.mNestedOffsets;
        r23[0] = r23[0] + this.mScrollOffset[0];
        int[] r24 = this.mNestedOffsets;
        r24[1] = r24[1] + this.mScrollOffset[1];
        getParent().requestDisallowInterceptTouchEvent(true);
        int r25 = r110;
    L62:
        this.mLastTouchX = r4 - this.mScrollOffset[0];
        this.mLastTouchY = r5 - this.mScrollOffset[r22];
        if (r9 == false) goto L65;
        int r26 = r17;
    L66:
        if (r10 == false) goto L68;
        int r27 = r25;
    L70:
        if (scrollByInternal(r26, r27, r21, 0) == false) goto L73;
        getParent().requestDisallowInterceptTouchEvent(true);
    L73:
        if (this.mGapWorker == null) goto L100;
        if (r17 != 0) goto L76;
        if (r25 == 0) goto L100;
    L76:
        this.mGapWorker.postFromTraversal(this, r17, r25);
        goto L100
    L68:
        r27 = 0;
        goto L70
    L65:
        r26 = 0;
        goto L66
    L61:
        r22 = 1;
        r25 = r18;
        goto L62
    L57:
        r20 = 0;
        goto L59
    L53:
        r19 = 0;
        goto L55
    L79:
        this.mVelocityTracker.addMovement(r14);
        r11 = true;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxFlingVelocity);
        if (r9 == false) goto L82;
        float r28 = -this.mVelocityTracker.getXVelocity(this.mScrollPointerId);
    L83:
        if (r10 == false) goto L85;
        float r29 = -this.mVelocityTracker.getYVelocity(this.mScrollPointerId);
    L87:
        if (r28 != 0.0f) goto L91;
        if (r29 != 0.0f) goto L91;
    L92:
        setScrollState(0);
    L93:
        resetScroll();
    L91:
        if (fling((int) r28, (int) r29) == true) goto L93;
    L85:
        r29 = 0.0f;
        goto L87
    L82:
        r28 = 0.0f;
        goto L83
    L94:
        this.mScrollPointerId = r21.getPointerId(0);
        int r30 = (int) (r21.getX() + 0.5f);
        this.mLastTouchX = r30;
        this.mInitialTouchX = r30;
        int r31 = (int) (r21.getY() + 0.5f);
        this.mLastTouchY = r31;
        this.mInitialTouchY = r31;
        int r32 = 0;
        if (r9 == false) goto L97;
        r32 = 0 | 1;
    L97:
        if (r10 == false) goto L99;
        r32 = r32 | 2;
    L99:
        startNestedScroll(r32, 0);
    L100:
        if (r11 == true) goto L102;
        this.mVelocityTracker.addMovement(r14);
    L102:
        r14.recycle();
        return true;
    }

    void postAnimationRunner() {
        if (this.mPostedAnimatorRunner == false) goto L5;
        return;
    L5:
        if (this.mIsAttached == false) goto L9;
        ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
        this.mPostedAnimatorRunner = true;
        return;
    }

    void processDataSetCompletelyChanged(boolean r2) {
        this.mDispatchItemsChangedEvent |= r2;
        this.mDataSetHasChangedAfterLayout = true;
        markKnownViewsInvalid();
    }

    void recordAnimationInfoIfBouncedHiddenView(ViewHolder r4, ItemAnimator.ItemHolderInfo r5) {
        r4.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders == true) goto L5;
    L11:
        this.mViewInfoStore.addToPreLayout(r4, r5);
        return;
    L5:
        if (r4.isUpdated() == false) goto L11;
        if (r4.isRemoved() == true) goto L11;
        if (r4.shouldIgnore() == true) goto L11;
        long r0 = getChangedHolderKey(r4);
        this.mViewInfoStore.addToOldChangeHolders(r0, r4);
        goto L11
    }

    void removeAndRecycleViews() {
        if (this.mItemAnimator == null) goto L6;
        this.mItemAnimator.endAnimations();
    L6:
        if (this.mLayout == null) goto L8;
        this.mLayout.removeAndRecycleAllViews(this.mRecycler);
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    L8:
        this.mRecycler.clear();
    }

    boolean removeAnimatingView(View r4) {
        startInterceptRequestLayout();
        boolean r0 = this.mChildHelper.removeViewIfHidden(r4);
        if (r0 == false) goto L5;
        ViewHolder r1 = getChildViewHolderInt(r4);
        this.mRecycler.unscrapView(r1);
        this.mRecycler.recycleViewHolderInternal(r1);
    L5:
        stopInterceptRequestLayout(!r0);
        return r0;
    }

    @Override // android.view.ViewGroup
    protected void removeDetachedView(View r5, boolean r6) {
        ViewHolder r0 = getChildViewHolderInt(r5);
        if (r0 != null) goto L5;
    L12:
        r5.clearAnimation();
        dispatchChildDetached(r5);
        super.removeDetachedView(r5, r6);
        return;
    L5:
        if (r0.isTmpDetached() == false) goto L8;
        r0.clearTmpDetachFlag();
        goto L12
    L8:
        if (r0.shouldIgnore() == true) goto L12;
        throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + r0 + exceptionLabel());
    }

    public void removeItemDecoration(ItemDecoration r3) {
        if (this.mLayout == null) goto L5;
        this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
    L5:
        this.mItemDecorations.remove(r3);
        if (this.mItemDecorations.isEmpty() == true) goto L8;
    L12:
        markItemDecorInsetsDirty();
        requestLayout();
        return;
    L8:
        if (getOverScrollMode() != 2) goto L10;
        boolean r0 = true;
    L11:
        setWillNotDraw(r0);
        goto L12
    L10:
        r0 = false;
        goto L11
    }

    public void removeItemDecorationAt(int r5) {
        int r0 = getItemDecorationCount();
        if (r5 < 0) goto L8;
        if (r5 >= r0) goto L8;
        removeItemDecoration(getItemDecorationAt(r5));
        return;
    L8:
        throw new IndexOutOfBoundsException(r5 + " is an invalid index for size " + r0);
    }

    public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener r2) {
        if (this.mOnChildAttachStateListeners != null) goto L5;
        return;
    L5:
        this.mOnChildAttachStateListeners.remove(r2);
    }

    public void removeOnItemTouchListener(OnItemTouchListener r2) {
        this.mOnItemTouchListeners.remove(r2);
        if (this.mInterceptingOnItemTouchListener != r2) goto L6;
        this.mInterceptingOnItemTouchListener = null;
        return;
    }

    public void removeOnScrollListener(OnScrollListener r2) {
        if (this.mScrollListeners == null) goto L6;
        this.mScrollListeners.remove(r2);
        return;
    }

    public void removeRecyclerListener(RecyclerListener r2) {
        this.mRecyclerListeners.remove(r2);
    }

    void repositionShadowingViews() {
        int r0 = this.mChildHelper.getChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L15;
        View r2 = this.mChildHelper.getChildAt(r1);
        ViewHolder r3 = getChildViewHolder(r2);
        if (r3 == null) goto L14;
        if (r3.mShadowingHolder == null) goto L14;
        View r4 = r3.mShadowingHolder.itemView;
        int r5 = r2.getLeft();
        int r6 = r2.getTop();
        if (r5 == r4.getLeft()) goto L11;
    L13:
        r4.layout(r5, r6, r4.getWidth() + r5, r4.getHeight() + r6);
        goto L14
    L11:
        if (r6 != r4.getTop()) goto L13;
    L14:
        r1 = r1 + 1;
        goto L3
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View r3, View r4) {
        if (this.mLayout.onRequestChildFocus(this, this.mState, r3, r4) == true) goto L6;
        if (r4 == null) goto L6;
        requestChildOnScreen(r3, r4);
    L6:
        super.requestChildFocus(r3, r4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View r2, Rect r3, boolean r4) {
        return this.mLayout.requestChildRectangleOnScreen(this, r2, r3, r4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean r4) {
        int r0 = this.mOnItemTouchListeners.size();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L5;
        this.mOnItemTouchListeners.get(r1).onRequestDisallowInterceptTouchEvent(r4);
        r1 = r1 + 1;
        goto L3
    L5:
        super.requestDisallowInterceptTouchEvent(r4);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.mInterceptRequestLayoutDepth == 0) goto L5;
    L7:
        this.mLayoutWasDefered = true;
        return;
    L5:
        if (this.mLayoutSuppressed == true) goto L7;
        super.requestLayout();
    }

    void saveOldPositions() {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        ViewHolder r2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(r1));
        if (r2.shouldIgnore() == true) goto L7;
        r2.saveOldPosition();
    L7:
        r1 = r1 + 1;
        goto L3
    }

    @Override // android.view.View
    public void scrollBy(int r7, int r8) {
        if (this.mLayout != null) goto L7;
        Log.e(TAG, "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
    L7:
        if (this.mLayoutSuppressed == false) goto L9;
        return;
    L9:
        boolean r0 = this.mLayout.canScrollHorizontally();
        boolean r1 = this.mLayout.canScrollVertically();
        if (r0 == true) goto L13;
        if (r1 == true) goto L13;
        return;
    L13:
        if (r0 == false) goto L15;
        int r3 = r7;
    L16:
        if (r1 == false) goto L18;
        int r4 = r8;
    L19:
        scrollByInternal(r3, r4, null, 0);
        return;
    L18:
        r4 = 0;
        goto L19
    L15:
        r3 = 0;
        goto L16
    }

    boolean scrollByInternal(int r17, int r18, MotionEvent r19, int r20) {
        consumePendingUpdateOperations();
        if (this.mAdapter == null) goto L5;
        this.mReusableIntPair[0] = 0;
        this.mReusableIntPair[1] = 0;
        scrollStep(r17, r18, this.mReusableIntPair);
        int r3 = this.mReusableIntPair[0];
        int r4 = this.mReusableIntPair[1];
        int r5 = r17 - r3;
        int r1 = r3;
        int r6 = r18 - r4;
        int r2 = r4;
    L7:
        if (this.mItemDecorations.isEmpty() == true) goto L9;
        invalidate();
    L9:
        this.mReusableIntPair[0] = 0;
        this.mReusableIntPair[1] = 0;
        dispatchNestedScroll(r1, r2, r5, r6, this.mScrollOffset, r20, this.mReusableIntPair);
        int r7 = r5 - this.mReusableIntPair[0];
        int r8 = r6 - this.mReusableIntPair[1];
        if (this.mReusableIntPair[0] == 0) goto L12;
    L15:
        boolean r9 = true;
    L16:
        this.mLastTouchX -= this.mScrollOffset[0];
        this.mLastTouchY -= this.mScrollOffset[1];
        int[] r10 = this.mNestedOffsets;
        r10[0] = r10[0] + this.mScrollOffset[0];
        int[] r11 = this.mNestedOffsets;
        r11[1] = r11[1] + this.mScrollOffset[1];
        if (getOverScrollMode() == 2) goto L23;
        if (r19 != null) goto L20;
    L22:
        considerReleasingGlowsOnScroll(r17, r18);
        goto L23
    L20:
        if (MotionEventCompat.isFromSource(r19, InputDeviceCompat.SOURCE_MOUSE) == true) goto L22;
        pullGlows(r19.getX(), r7, r19.getY(), r8);
    L23:
        if (r1 != 0) goto L25;
        if (r2 != 0) goto L25;
    L27:
        if (awakenScrollBars() == true) goto L29;
        invalidate();
    L29:
        if (r9 == true) goto L35;
        if (r1 != 0) goto L36;
        if (r2 == 0) goto L34;
        return true;
    L34:
        return false;
    L36:
        return true;
    L35:
        return true;
    L25:
        dispatchOnScrolled(r1, r2);
        goto L27
    L12:
        if (this.mReusableIntPair[1] != 0) goto L15;
        r9 = false;
        goto L16
    L5:
        r5 = 0;
        r1 = 0;
        r6 = 0;
        r2 = 0;
        goto L7
    }

    void scrollStep(int r6, int r7, int[] r8) {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        TraceCompat.beginSection(TRACE_SCROLL_TAG);
        fillRemainingScrollValues(this.mState);
        int r0 = 0;
        int r1 = 0;
        if (r6 == 0) goto L5;
        r0 = this.mLayout.scrollHorizontallyBy(r6, this.mRecycler, this.mState);
    L5:
        if (r7 == 0) goto L7;
        r1 = this.mLayout.scrollVerticallyBy(r7, this.mRecycler, this.mState);
    L7:
        TraceCompat.endSection();
        repositionShadowingViews();
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        if (r8 == null) goto L11;
        r8[0] = r0;
        r8[1] = r1;
        return;
    }

    @Override // android.view.View
    public void scrollTo(int r3, int r4) {
        Log.w(TAG, "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    public void scrollToPosition(int r3) {
        if (this.mLayoutSuppressed == false) goto L5;
        return;
    L5:
        stopScroll();
        if (this.mLayout != null) goto L9;
        Log.e(TAG, "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
    L9:
        this.mLayout.scrollToPosition(r3);
        awakenScrollBars();
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEventUnchecked(AccessibilityEvent r2) {
        if (shouldDeferAccessibilityEvent(r2) == false) goto L5;
        return;
    L5:
        super.sendAccessibilityEventUnchecked(r2);
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate r2) {
        this.mAccessibilityDelegate = r2;
        ViewCompat.setAccessibilityDelegate(this, this.mAccessibilityDelegate);
    }

    public void setAdapter(Adapter r3) {
        setLayoutFrozen(false);
        setAdapterInternal(r3, false, true);
        processDataSetCompletelyChanged(false);
        requestLayout();
    }

    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback r2) {
        if (r2 != this.mChildDrawingOrderCallback) goto L5;
        return;
    L5:
        this.mChildDrawingOrderCallback = r2;
        if (this.mChildDrawingOrderCallback == null) goto L8;
        boolean r0 = true;
    L9:
        setChildrenDrawingOrderEnabled(r0);
        return;
    L8:
        r0 = false;
        goto L9
    }

    boolean setChildImportantForAccessibilityInternal(ViewHolder r2, int r3) {
        if (isComputingLayout() == false) goto L6;
        r2.mPendingAccessibilityState = r3;
        this.mPendingAccessibilityImportanceChange.add(r2);
        return false;
    L6:
        ViewCompat.setImportantForAccessibility(r2.itemView, r3);
        return true;
    }

    @Override // android.view.ViewGroup
    public void setClipToPadding(boolean r2) {
        if (r2 == this.mClipToPadding) goto L5;
        invalidateGlows();
    L5:
        this.mClipToPadding = r2;
        super.setClipToPadding(r2);
        if (this.mFirstLayoutComplete == false) goto L9;
        requestLayout();
        return;
    }

    public void setEdgeEffectFactory(EdgeEffectFactory r1) {
        Preconditions.checkNotNull(r1);
        this.mEdgeEffectFactory = r1;
        invalidateGlows();
    }

    public void setHasFixedSize(boolean r1) {
        this.mHasFixedSize = r1;
    }

    public void setItemAnimator(ItemAnimator r3) {
        if (this.mItemAnimator == null) goto L5;
        this.mItemAnimator.endAnimations();
        this.mItemAnimator.setListener(null);
    L5:
        this.mItemAnimator = r3;
        if (this.mItemAnimator == null) goto L9;
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        return;
    }

    public void setItemViewCacheSize(int r2) {
        this.mRecycler.setViewCacheSize(r2);
    }

    @Deprecated
    public void setLayoutFrozen(boolean r1) {
        suppressLayout(r1);
    }

    public void setLayoutManager(LayoutManager r4) {
        if (r4 != this.mLayout) goto L5;
        return;
    L5:
        stopScroll();
        if (this.mLayout != null) goto L8;
        this.mRecycler.clear();
    L15:
        this.mChildHelper.removeAllViewsUnfiltered();
        this.mLayout = r4;
        if (r4 != null) goto L18;
    L24:
        this.mRecycler.updateViewCacheSize();
        requestLayout();
        return;
    L18:
        if (r4.mRecyclerView != null) goto L23;
        this.mLayout.setRecyclerView(this);
        if (this.mIsAttached == false) goto L24;
        this.mLayout.dispatchAttachedToWindow(this);
        goto L24
    L23:
        throw new IllegalArgumentException("LayoutManager " + r4 + " is already attached to a RecyclerView:" + r4.mRecyclerView.exceptionLabel());
    L8:
        if (this.mItemAnimator == null) goto L10;
        this.mItemAnimator.endAnimations();
    L10:
        this.mLayout.removeAndRecycleAllViews(this.mRecycler);
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mRecycler.clear();
        if (this.mIsAttached == false) goto L13;
        this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
    L13:
        this.mLayout.setRecyclerView(null);
        this.mLayout = null;
        goto L15
    }

    @Override // android.view.ViewGroup
    @Deprecated
    public void setLayoutTransition(LayoutTransition r3) {
        if (r3 != null) goto L6;
        super.setLayoutTransition(null);
        return;
    L6:
        throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void setNestedScrollingEnabled(boolean r2) {
        getScrollingChildHelper().setNestedScrollingEnabled(r2);
    }

    public void setOnFlingListener(OnFlingListener r1) {
        this.mOnFlingListener = r1;
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener r1) {
        this.mScrollListener = r1;
    }

    public void setPreserveFocusAfterLayout(boolean r1) {
        this.mPreserveFocusAfterLayout = r1;
    }

    public void setRecycledViewPool(RecycledViewPool r2) {
        this.mRecycler.setRecycledViewPool(r2);
    }

    @Deprecated
    public void setRecyclerListener(RecyclerListener r1) {
        this.mRecyclerListener = r1;
    }

    void setScrollState(int r2) {
        if (r2 != this.mScrollState) goto L5;
        return;
    L5:
        this.mScrollState = r2;
        if (r2 == 2) goto L8;
        stopScrollersInternal();
    L8:
        dispatchOnScrollStateChanged(r2);
    }

    public void setScrollingTouchSlop(int r4) {
        ViewConfiguration r0 = ViewConfiguration.get(getContext());
        switch(r4) {
            case 0: goto L6;
            case 1: goto L5;
            default: goto L4;
        };
    L4:
        Log.w(TAG, "setScrollingTouchSlop(): bad argument constant " + r4 + "; using default value");
        goto L6
    L5:
        this.mTouchSlop = r0.getScaledPagingTouchSlop();
        return;
    L6:
        this.mTouchSlop = r0.getScaledTouchSlop();
    }

    public void setViewCacheExtension(ViewCacheExtension r2) {
        this.mRecycler.setViewCacheExtension(r2);
    }

    boolean shouldDeferAccessibilityEvent(AccessibilityEvent r3) {
        if (isComputingLayout() == false) goto L11;
        int r0 = 0;
        if (r3 == null) goto L7;
        r0 = AccessibilityEventCompat.getContentChangeTypes(r3);
    L7:
        if (r0 != 0) goto L9;
        r0 = 0;
    L9:
        this.mEatenAccessibilityChangeFlags |= r0;
        return true;
    L11:
        return false;
    }

    public void smoothScrollBy(int r2, int r3) {
        smoothScrollBy(r2, r3, null);
    }

    public void smoothScrollBy(int r2, int r3, Interpolator r4) {
        smoothScrollBy(r2, r3, r4, Integer.MIN_VALUE);
    }

    public void smoothScrollBy(int r7, int r8, Interpolator r9, int r10) {
        smoothScrollBy(r7, r8, r9, r10, false);
    }

    void smoothScrollBy(int r4, int r5, Interpolator r6, int r7, boolean r8) {
        if (this.mLayout != null) goto L7;
        Log.e(TAG, "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
    L7:
        if (this.mLayoutSuppressed == false) goto L10;
        return;
    L10:
        if (this.mLayout.canScrollHorizontally() == true) goto L13;
        r4 = 0;
    L13:
        if (this.mLayout.canScrollVertically() == true) goto L15;
        r5 = 0;
    L15:
        if (r4 != 0) goto L18;
        if (r5 != 0) goto L18;
        return;
    L18:
        if (r7 == Integer.MIN_VALUE) goto L22;
        if (r7 > 0) goto L22;
        boolean r0 = false;
    L23:
        if (r0 == false) goto L32;
        if (r8 == false) goto L31;
        int r2 = 0;
        if (r4 == 0) goto L28;
        r2 = 0 | 1;
    L28:
        if (r5 == 0) goto L30;
        r2 = r2 | 2;
    L30:
        startNestedScroll(r2, 1);
    L31:
        this.mViewFlinger.smoothScrollBy(r4, r5, r7, r6);
        return;
    L32:
        scrollBy(r4, r5);
        return;
    L22:
        r0 = true;
        goto L23
    }

    public void smoothScrollToPosition(int r3) {
        if (this.mLayoutSuppressed == false) goto L6;
        return;
    L6:
        if (this.mLayout != null) goto L9;
        Log.e(TAG, "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        return;
    L9:
        this.mLayout.smoothScrollToPosition(this, this.mState, r3);
    }

    void startInterceptRequestLayout() {
        this.mInterceptRequestLayoutDepth++;
        if (this.mInterceptRequestLayoutDepth == 1) goto L5;
        return;
    L5:
        if (this.mLayoutSuppressed == true) goto L9;
        this.mLayoutWasDefered = false;
        return;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean startNestedScroll(int r2) {
        return getScrollingChildHelper().startNestedScroll(r2);
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public boolean startNestedScroll(int r2, int r3) {
        return getScrollingChildHelper().startNestedScroll(r2, r3);
    }

    void stopInterceptRequestLayout(boolean r4) {
        if (this.mInterceptRequestLayoutDepth >= 1) goto L6;
        this.mInterceptRequestLayoutDepth = 1;
    L6:
        if (r4 == true) goto L11;
        if (this.mLayoutSuppressed == true) goto L11;
        this.mLayoutWasDefered = false;
    L11:
        if (this.mInterceptRequestLayoutDepth != 1) goto L25;
        if (r4 == false) goto L23;
        if (this.mLayoutWasDefered == false) goto L23;
        if (this.mLayoutSuppressed == true) goto L23;
        if (this.mLayout == null) goto L23;
        if (this.mAdapter == null) goto L23;
        dispatchLayout();
    L23:
        if (this.mLayoutSuppressed == true) goto L25;
        this.mLayoutWasDefered = false;
    L25:
        this.mInterceptRequestLayoutDepth--;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override // androidx.core.view.NestedScrollingChild2
    public void stopNestedScroll(int r2) {
        getScrollingChildHelper().stopNestedScroll(r2);
    }

    public void stopScroll() {
        setScrollState(0);
        stopScrollersInternal();
    }

    @Override // android.view.ViewGroup
    public final void suppressLayout(boolean r11) {
        if (r11 == this.mLayoutSuppressed) goto L16;
        assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
        if (r11 == true) goto L14;
        this.mLayoutSuppressed = false;
        if (this.mLayoutWasDefered == true) goto L9;
    L13:
        this.mLayoutWasDefered = false;
        return;
    L9:
        if (this.mLayout == null) goto L13;
        if (this.mAdapter == null) goto L13;
        requestLayout();
        goto L13
    L14:
        long r2 = SystemClock.uptimeMillis();
        onTouchEvent(MotionEvent.obtain(r2, r2, 3, 0.0f, 0.0f, 0));
        this.mLayoutSuppressed = true;
        this.mIgnoreMotionEventTillDown = true;
        stopScroll();
        return;
    }

    public void swapAdapter(Adapter r2, boolean r3) {
        setLayoutFrozen(false);
        setAdapterInternal(r2, true, r3);
        processDataSetCompletelyChanged(true);
        requestLayout();
    }

    void viewRangeUpdate(int r8, int r9, Object r10) {
        int r0 = this.mChildHelper.getUnfilteredChildCount();
        int r1 = r8 + r9;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L15;
        View r3 = this.mChildHelper.getUnfilteredChildAt(r2);
        ViewHolder r4 = getChildViewHolderInt(r3);
        if (r4 == null) goto L14;
        if (r4.shouldIgnore() == true) goto L14;
        if (r4.mPosition < r8) goto L14;
        if (r4.mPosition >= r1) goto L14;
        r4.addFlags(2);
        r4.addChangePayload(r10);
        ((LayoutParams) r3.getLayoutParams()).mInsetsDirty = true;
    L14:
        r2 = r2 + 1;
        goto L3
    L15:
        this.mRecycler.viewRangeUpdate(r8, r9);
    }
}
