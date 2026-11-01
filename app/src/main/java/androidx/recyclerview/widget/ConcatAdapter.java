package androidx.recyclerview.widget;

import android.util.Pair;
import android.view.ViewGroup;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class ConcatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final String TAG = "ConcatAdapter";
    private final ConcatAdapterController mController;

    public static final class Config {
        public static final Config DEFAULT = null;
        public final boolean isolateViewTypes;
        public final StableIdMode stableIdMode;

        public static final class Builder {
            private boolean mIsolateViewTypes;
            private StableIdMode mStableIdMode;

            public Builder() {
                this.mIsolateViewTypes = Config.DEFAULT.isolateViewTypes;
                this.mStableIdMode = Config.DEFAULT.stableIdMode;
            }

            public Config build() {
                return new Config(this.mIsolateViewTypes, this.mStableIdMode);
            }

            public Builder setIsolateViewTypes(boolean r1) {
                this.mIsolateViewTypes = r1;
                return this;
            }

            public Builder setStableIdMode(StableIdMode r1) {
                this.mStableIdMode = r1;
                return this;
            }
        }

        public static final enum StableIdMode extends Enum<StableIdMode> {
            private static final /* synthetic */ StableIdMode[] $VALUES = null;
            public static final StableIdMode ISOLATED_STABLE_IDS = null;
            public static final StableIdMode NO_STABLE_IDS = null;
            public static final StableIdMode SHARED_STABLE_IDS = null;

            static {
                NO_STABLE_IDS = new StableIdMode("NO_STABLE_IDS", 0);
                ISOLATED_STABLE_IDS = new StableIdMode("ISOLATED_STABLE_IDS", 1);
                SHARED_STABLE_IDS = new StableIdMode("SHARED_STABLE_IDS", 2);
                $VALUES = new StableIdMode[]{NO_STABLE_IDS, ISOLATED_STABLE_IDS, SHARED_STABLE_IDS};
            }

            private StableIdMode(String r1, int r2) {
                super(r1, r2);
            }

            public static StableIdMode valueOf(String r1) {
                return (StableIdMode) Enum.valueOf(StableIdMode.class, r1);
            }

            public static StableIdMode[] values() {
                return (StableIdMode[]) $VALUES.clone();
            }
        }

        static {
            DEFAULT = new Config(true, StableIdMode.NO_STABLE_IDS);
        }

        Config(boolean r1, StableIdMode r2) {
            this.isolateViewTypes = r1;
            this.stableIdMode = r2;
        }
    }

    public ConcatAdapter(Config r3, List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> r4) {
        this.mController = new ConcatAdapterController(this, r3);
        Iterator<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> r0 = r4.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        addAdapter(r0.next());
        goto L4
    L6:
        super.setHasStableIds(this.mController.hasStableIds());
    }

    @SafeVarargs
    public ConcatAdapter(Config r2, RecyclerView.Adapter<? extends RecyclerView.ViewHolder>... r3) {
        this(r2, Arrays.asList(r3));
    }

    public ConcatAdapter(List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> r2) {
        this(Config.DEFAULT, r2);
    }

    @SafeVarargs
    public ConcatAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder>... r2) {
        this(Config.DEFAULT, r2);
    }

    public boolean addAdapter(int r2, RecyclerView.Adapter<? extends RecyclerView.ViewHolder> r3) {
        return this.mController.addAdapter(r2, r3);
    }

    public boolean addAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> r2) {
        return this.mController.addAdapter(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int findRelativeAdapterPositionIn(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> r2, RecyclerView.ViewHolder r3, int r4) {
        return this.mController.getLocalAdapterPosition(r2, r3, r4);
    }

    public List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> getAdapters() {
        return Collections.unmodifiableList(this.mController.getCopyOfAdapters());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mController.getTotalCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int r3) {
        return this.mController.getItemId(r3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int r2) {
        return this.mController.getItemViewType(r2);
    }

    public Pair<RecyclerView.Adapter<? extends RecyclerView.ViewHolder>, Integer> getWrappedAdapterAndPosition(int r2) {
        return this.mController.getWrappedAdapterAndPosition(r2);
    }

    void internalSetStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy r1) {
        super.setStateRestorationPolicy(r1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView r2) {
        this.mController.onAttachedToRecyclerView(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder r2, int r3) {
        this.mController.onBindViewHolder(r2, r3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup r2, int r3) {
        return this.mController.onCreateViewHolder(r2, r3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView r2) {
        this.mController.onDetachedFromRecyclerView(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder r2) {
        return this.mController.onFailedToRecycleView(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder r2) {
        this.mController.onViewAttachedToWindow(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder r2) {
        this.mController.onViewDetachedFromWindow(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder r2) {
        this.mController.onViewRecycled(r2);
    }

    public boolean removeAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> r2) {
        return this.mController.removeAdapter(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void setHasStableIds(boolean r3) {
        throw new UnsupportedOperationException("Calling setHasStableIds is not allowed on the ConcatAdapter. Use the Config object passed in the constructor to control this behavior");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy r3) {
        throw new UnsupportedOperationException("Calling setStateRestorationPolicy is not allowed on the ConcatAdapter. This value is inferred from added adapters");
    }
}
