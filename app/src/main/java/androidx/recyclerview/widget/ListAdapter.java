package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    final AsyncListDiffer<T> mDiffer;
    private final AsyncListDiffer.ListListener<T> mListener;

    protected ListAdapter(AsyncDifferConfig<T> r3) {
        this.mListener = new C04971(this);
        this.mDiffer = new AsyncListDiffer(new AdapterListUpdateCallback(this), r3);
        this.mDiffer.addListListener(this.mListener);
    }

    protected ListAdapter(DiffUtil.ItemCallback<T> r4) {
        this.mListener = new C04971(this);
        this.mDiffer = new AsyncListDiffer(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder(r4).build());
        this.mDiffer.addListListener(this.mListener);
    }

    public List<T> getCurrentList() {
        return this.mDiffer.getCurrentList();
    }

    protected T getItem(int r2) {
        return this.mDiffer.getCurrentList().get(r2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDiffer.getCurrentList().size();
    }

    public void onCurrentListChanged(List<T> r1, List<T> r2) {
    }

    public void submitList(List<T> r2) {
        this.mDiffer.submitList(r2);
    }

    public void submitList(List<T> r2, Runnable r3) {
        this.mDiffer.submitList(r2, r3);
    }
}
