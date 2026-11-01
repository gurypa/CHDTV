package es.antonio.m3u;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.media3.extractor.p001ts.TsExtractor;

/* JADX INFO: loaded from: classes3.dex */
class ResponsiveCatalogGrid extends GridView {
    private int announcedSelection;
    private boolean channelCatalog;
    private int internalSelectionMode;
    private boolean recoveringAnchor;
    private int remoteSelection;
    private int restoreAttempts;
    private final Runnable restoreOffset;
    private int restorePosition;
    private int restoreTop;
    private AdapterView.OnItemSelectedListener selectionListener;
    private final DataSetObserver selectionObserver;
    private boolean touchBrowsing;

    public static /* synthetic */ void $r8$lambda$QVeik0mGaA_QGoC6cDm77Go29P8(ResponsiveCatalogGrid r0) {
        r0.lambda$new$0();
    }

    /* JADX INFO: renamed from: -$$Nest$fputannouncedSelection, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m782$$Nest$fputannouncedSelection(ResponsiveCatalogGrid r0, int r1) {
        r0.announcedSelection = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$fputremoteSelection, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m783$$Nest$fputremoteSelection(ResponsiveCatalogGrid r0, int r1) {
        r0.remoteSelection = r1;
    }

    /* JADX INFO: renamed from: -$$Nest$mannounceSelection, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m784$$Nest$mannounceSelection(ResponsiveCatalogGrid r0) {
        r0.announceSelection();
    }

    /* JADX INFO: renamed from: -$$Nest$mremoteMode, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m785$$Nest$mremoteMode(ResponsiveCatalogGrid r0) {
        return r0.remoteMode();
    }

    /* JADX INFO: renamed from: -$$Nest$mremotePosition, reason: not valid java name */
    static /* bridge */ /* synthetic */ int m786$$Nest$mremotePosition(ResponsiveCatalogGrid r0) {
        return r0.remotePosition();
    }

    ResponsiveCatalogGrid(Context r3) {
        super(r3);
        this.remoteSelection = -1;
        this.announcedSelection = -1;
        this.selectionObserver = new C10321(this);
        this.restorePosition = -1;
        this.restoreOffset = new ResponsiveCatalogGrid$$ExternalSyntheticLambda0(this);
    }

    static /* synthetic */ void access$001(ResponsiveCatalogGrid r0, int r1) {
        super.scrollListBy(r1);
    }

    private void anchorRemoteSelection(int r3) {
        this.internalSelectionMode = 1;     // Catch: Throwable -> L6
        super.setSelection(-1);     // Catch: Throwable -> L6
        this.internalSelectionMode = 2;     // Catch: Throwable -> L6
        super.setSelection(r3);     // Catch: Throwable -> L6
        this.internalSelectionMode = 0;
        return;
    L6:
        th = move-exception;
        this.internalSelectionMode = 0;
        throw th;
    }

    private void announceSelection() {
        int r3 = remotePosition();
        View r2 = getSelectedView();
        if (this.selectionListener != null) goto L5;
        return;
    L5:
        if (remoteMode() == true) goto L7;
        return;
    L7:
        if (hasFocus() == false) goto L16;
        if (r3 < 0) goto L17;
        if (r2 != null) goto L11;
        return;
    L11:
        if (r3 == this.announcedSelection) goto L19;
        this.announcedSelection = r3;
        this.selectionListener.onItemSelected(this, r2, r3, getAdapter().getItemId(r3));
        return;
    L19:
        return;
    L17:
        return;
    }

    private void cancelRestore() {
        removeCallbacks(this.restoreOffset);
        this.restorePosition = -1;
        this.restoreAttempts = 0;
    }

    private /* synthetic */ void lambda$new$0() {
        int r0 = this.restorePosition;
        if (r0 >= 0) goto L5;
    L21:
        cancelRestore();
        return;
    L5:
        if (isAttachedToWindow() == false) goto L21;
        if (r0 >= getCount()) goto L21;
        View r1 = getChildAt(0);
        if (r1 != null) goto L12;
    L16:
        int r2 = this.restoreAttempts + 1;
        this.restoreAttempts = r2;
        if (r2 < 3) goto L19;
        cancelRestore();
        return;
    L19:
        postOnAnimation(this.restoreOffset);
        return;
    L12:
        if (getFirstVisiblePosition() != r0) goto L16;
        int r3 = r1.getTop() - this.restoreTop;
        cancelRestore();
        if (r3 == 0) goto L23;
        access$001(this, r3);
        return;
    }

    private void paintSelection() {
        if (remoteMode() == true) goto L5;
    L7:
        boolean r0 = false;
    L8:
        int r3 = remotePosition();
        int r4 = 0;
    L10:
        if (r4 >= getChildCount()) goto L24;
        View r5 = getChildAt(r4);
        if (r0 == false) goto L19;
        if ((getFirstVisiblePosition() + r4) != r3) goto L16;
        boolean r6 = true;
    L17:
        r5.setActivated(r6);
        r5.setSelected(r6);
    L23:
        r4 = r4 + 1;
        goto L10
    L16:
        r6 = false;
        goto L17
    L19:
        if (this.touchBrowsing == true) goto L22;
        r5.setActivated(false);
    L22:
        r5.setSelected(false);
        goto L23
    L24:
        announceSelection();
        return;
    L5:
        if (hasFocus() == false) goto L7;
        r0 = true;
        goto L8
    }

    private boolean remoteMode() {
        return !this.touchBrowsing;
    }

    private int remotePosition() {
        if (this.remoteSelection >= 0) goto L5;
    L8:
        return -1;
    L5:
        if (getCount() == 0) goto L8;
        return Math.min(getCount() - 1, this.remoteSelection);
    }

    protected int columnsForWidth(float r5) {
        boolean r0 = AppTheme.cineStage(getContext());
        if (r0 == false) goto L5;
        int r1 = 8;
    L6:
        float r2 = 10.0f + r5;
        if (this.channelCatalog == false) goto L9;
        int r3 = TsExtractor.TS_STREAM_TYPE_HDMV_DTS;
    L13:
        return Math.max(1, Math.min(r1, (int) (r2 / r3)));
    L9:
        if (r0 == false) goto L11;
        r3 = 142;
        goto L13
    L11:
        r3 = 170;
        goto L13
    L5:
        r1 = 6;
        goto L6
    }

    @Override // android.widget.AdapterView
    public long getSelectedItemId() {
        int r0 = getSelectedItemPosition();
        if (r0 >= 0) goto L5;
    L8:
        return Long.MIN_VALUE;
    L5:
        if (getAdapter() == null) goto L8;
        return getAdapter().getItemId(r0);
    }

    @Override // android.widget.AdapterView
    public int getSelectedItemPosition() {
        int r0 = remotePosition();
        if (remoteMode() == false) goto L7;
        if (r0 < 0) goto L7;
        return r0;
    L7:
        return super.getSelectedItemPosition();
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView
    public View getSelectedView() {
        if (remoteMode() == false) goto L9;
        if (remotePosition() < 0) goto L9;
        return getChildAt(remotePosition() - getFirstVisiblePosition());
    L9:
        return super.getSelectedView();
    }

    boolean hasTouchBrowsing() {
        return this.touchBrowsing;
    }

    @Override // android.view.View
    public boolean isInTouchMode() {
        if (this.internalSelectionMode != 1) goto L6;
        return false;
    L6:
        if (this.internalSelectionMode != 2) goto L9;
        return true;
    L9:
        return super.isInTouchMode();
    }

    @Override // android.widget.GridView, android.widget.AbsListView
    protected void layoutChildren() {
        super.layoutChildren();
        int r0 = remotePosition();
        if (remoteMode() == false) goto L18;
        if (hasFocus() == false) goto L18;
        if (r0 < 0) goto L18;
        if (r0 < getFirstVisiblePosition()) goto L21;
        if (r0 < (getFirstVisiblePosition() + getChildCount())) goto L18;
    L21:
        this.recoveringAnchor = true;     // Catch: Throwable -> L14
        anchorRemoteSelection(r0);     // Catch: Throwable -> L14
        this.recoveringAnchor = false;
        super.layoutChildren();
    L14:
        th = move-exception;
        this.recoveringAnchor = false;
        throw th;
    L18:
        if (this.restorePosition < 0) goto L23;
        removeCallbacks(this.restoreOffset);
        post(this.restoreOffset);
        return;
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        cancelRestore();
        super.onDetachedFromWindow();
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onFocusChanged(boolean r3, int r4, Rect r5) {
        super.onFocusChanged(r3, r4, r5);
        if (r3 == true) goto L5;
    L11:
        paintSelection();
        return;
    L5:
        if (this.remoteSelection >= 0) goto L11;
        if (getCount() <= 0) goto L11;
        if (this.touchBrowsing == true) goto L11;
        this.remoteSelection = Math.max(0, getFirstVisiblePosition());
        goto L11
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean r1, int r2, int r3, int r4, int r5) {
        super.onLayout(r1, r2, r3, r4, r5);
        paintSelection();
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int r4, int r5) {
        int r0 = (View.MeasureSpec.getSize(r4) - getPaddingLeft()) - getPaddingRight();
        if (r0 <= 0) goto L7;
        int r1 = columnsForWidth(r0 / getResources().getDisplayMetrics().density);
        if (r1 == getNumColumns()) goto L7;
        setNumColumns(r1);
    L7:
        super.onMeasure(r4, r5);
    }

    @Override // android.view.View
    protected void onScrollChanged(int r1, int r2, int r3, int r4) {
        super.onScrollChanged(r1, r2, r3, r4);
        paintSelection();
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent r4) {
        if (r4.getActionMasked() != 0) goto L11;
        cancelRestore();
        if (this.touchBrowsing == true) goto L10;
        int r0 = 0;
    L8:
        if (r0 >= getChildCount()) goto L10;
        getChildAt(r0).setActivated(false);
        r0 = r0 + 1;
    L10:
        this.touchBrowsing = true;
    L11:
        boolean r1 = super.onTouchEvent(r4);
        paintSelection();
        return r1;
    }

    @Override // android.widget.AbsListView, android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.recoveringAnchor == true) goto L6;
        super.requestLayout();
        return;
    }

    @Override // android.widget.AdapterView
    public /* bridge */ /* synthetic */ void setAdapter(Adapter r1) {
        setAdapter((ListAdapter) r1);
    }

    @Override // android.widget.GridView, android.widget.AbsListView
    public void setAdapter(ListAdapter r3) {
        ListAdapter r0 = getAdapter();
        if (r0 == null) goto L5;
        r0.unregisterDataSetObserver(this.selectionObserver);
    L5:
        cancelRestore();
        this.touchBrowsing = false;
        this.remoteSelection = -1;
        this.announcedSelection = -1;
        super.setAdapter(r3);
        if (r3 == null) goto L9;
        r3.registerDataSetObserver(this.selectionObserver);
        return;
    }

    void setChannelCatalog(boolean r1) {
        this.channelCatalog = r1;
        requestLayout();
    }

    @Override // android.widget.AdapterView
    public void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener r2) {
        this.selectionListener = r2;
        this.announcedSelection = -1;
        if (r2 != null) goto L5;
        AdapterView.OnItemSelectedListener r0 = null;
    L6:
        super.setOnItemSelectedListener(r0);
        return;
    L5:
        r0 = new C10332(this, r2);
        goto L6
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    public void setSelection(int r2) {
        cancelRestore();
        this.touchBrowsing = false;
        if (getCount() == 0) goto L7;
        if (r2 < 0) goto L7;
        int r0 = Math.min(getCount() - 1, r2);
    L8:
        this.remoteSelection = r0;
        if (this.remoteSelection < 0) goto L11;
        anchorRemoteSelection(this.remoteSelection);
    L12:
        paintSelection();
        return;
    L11:
        super.setSelection(r2);
    L7:
        r0 = -1;
        goto L8
    }

    @Override // android.widget.AbsListView
    public void setSelectionFromTop(int r3, int r4) {
        cancelRestore();
        if (getCount() != 0) goto L5;
        return;
    L5:
        this.touchBrowsing = true;
        this.remoteSelection = -1;
        this.restorePosition = Math.max(0, Math.min(getCount() - 1, r3));
        this.restoreTop = getListPaddingTop() + r4;
        super.setSelection(this.restorePosition);
        post(this.restoreOffset);
    }
}
