package androidx.media3.p002ui;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import androidx.media3.common.Format;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.Assertions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TrackSelectionView extends LinearLayout {
    private boolean allowAdaptiveSelections;
    private boolean allowMultipleOverrides;
    private final ComponentListener componentListener;
    private final CheckedTextView defaultView;
    private final CheckedTextView disableView;
    private final LayoutInflater inflater;
    private boolean isDisabled;
    private TrackSelectionListener listener;
    private final Map<TrackGroup, TrackSelectionOverride> overrides;
    private final int selectableItemBackgroundResourceId;
    private final List<Tracks.Group> trackGroups;
    private Comparator<TrackInfo> trackInfoComparator;
    private TrackNameProvider trackNameProvider;
    private CheckedTextView[][] trackViews;

    /* JADX INFO: renamed from: androidx.media3.ui.TrackSelectionView$1 */
    static /* synthetic */ class C04691 {
    }

    private class ComponentListener implements View.OnClickListener {
        final /* synthetic */ TrackSelectionView this$0;

        private ComponentListener(TrackSelectionView r1) {
            this.this$0 = r1;
        }

        /* synthetic */ ComponentListener(TrackSelectionView r1, C04691 r2) {
            this(r1);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View r2) {
            TrackSelectionView.access$100(this.this$0, r2);
        }
    }

    private static final class TrackInfo {
        public final Tracks.Group trackGroup;
        public final int trackIndex;

        public TrackInfo(Tracks.Group r1, int r2) {
            this.trackGroup = r1;
            this.trackIndex = r2;
        }

        public Format getFormat() {
            return this.trackGroup.getTrackFormat(this.trackIndex);
        }
    }

    public interface TrackSelectionListener {
        void onTrackSelectionChanged(boolean r1, Map<TrackGroup, TrackSelectionOverride> r2);
    }

    public TrackSelectionView(Context r2) {
        this(r2, null);
    }

    public TrackSelectionView(Context r2, AttributeSet r3) {
        this(r2, r3, 0);
    }

    public TrackSelectionView(Context r7, AttributeSet r8, int r9) {
        super(r7, r8, r9);
        setOrientation(1);
        setSaveFromParentEnabled(false);
        TypedArray r2 = r7.getTheme().obtainStyledAttributes(new int[]{R.attr.selectableItemBackground});
        this.selectableItemBackgroundResourceId = r2.getResourceId(0, 0);
        r2.recycle();
        this.inflater = LayoutInflater.from(r7);
        this.componentListener = new ComponentListener(this, null);
        this.trackNameProvider = new DefaultTrackNameProvider(getResources());
        this.trackGroups = new ArrayList();
        this.overrides = new HashMap();
        this.disableView = (CheckedTextView) this.inflater.inflate(R.layout.simple_list_item_single_choice, this, false);
        this.disableView.setBackgroundResource(this.selectableItemBackgroundResourceId);
        this.disableView.setText(C0467R.string.exo_track_selection_none);
        this.disableView.setEnabled(false);
        this.disableView.setFocusable(true);
        this.disableView.setOnClickListener(this.componentListener);
        this.disableView.setVisibility(8);
        addView(this.disableView);
        addView(this.inflater.inflate(C0467R.layout.exo_list_divider, this, false));
        this.defaultView = (CheckedTextView) this.inflater.inflate(R.layout.simple_list_item_single_choice, this, false);
        this.defaultView.setBackgroundResource(this.selectableItemBackgroundResourceId);
        this.defaultView.setText(C0467R.string.exo_track_selection_auto);
        this.defaultView.setEnabled(false);
        this.defaultView.setFocusable(true);
        this.defaultView.setOnClickListener(this.componentListener);
        addView(this.defaultView);
    }

    static /* synthetic */ void access$100(TrackSelectionView r0, View r1) {
        r0.onClick(r1);
    }

    public static Map<TrackGroup, TrackSelectionOverride> filterOverrides(Map<TrackGroup, TrackSelectionOverride> r5, List<Tracks.Group> r6, boolean r7) {
        HashMap r0 = new HashMap();
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L12;
        TrackSelectionOverride r3 = r5.get(r6.get(r1).getMediaTrackGroup());
        if (r3 == null) goto L11;
        if (r7 == false) goto L9;
    L10:
        r0.put(r3.mediaTrackGroup, r3);
        goto L11
    L9:
        if (r0.isEmpty() == true) goto L10;
    L11:
        r1 = r1 + 1;
        goto L4
    L12:
        return r0;
    }

    static /* synthetic */ int lambda$init$0(Comparator r2, TrackInfo r3, TrackInfo r4) {
        return r2.compare(r3.getFormat(), r4.getFormat());
    }

    private void onClick(View r4) {
        if (r4 != this.disableView) goto L6;
        onDisableViewClicked();
    L9:
        updateViewStates();
        if (this.listener == null) goto L13;
        this.listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
        return;
    L13:
        return;
    L6:
        if (r4 != this.defaultView) goto L8;
        onDefaultViewClicked();
        goto L9
    L8:
        onTrackViewClicked(r4);
        goto L9
    }

    private void onDefaultViewClicked() {
        this.isDisabled = false;
        this.overrides.clear();
    }

    private void onDisableViewClicked() {
        this.isDisabled = true;
        this.overrides.clear();
    }

    private void onTrackViewClicked(View r12) {
        boolean r0 = false;
        this.isDisabled = false;
        TrackInfo r1 = (TrackInfo) Assertions.checkNotNull(r12.getTag());
        TrackGroup r2 = r1.trackGroup.getMediaTrackGroup();
        int r3 = r1.trackIndex;
        TrackSelectionOverride r4 = this.overrides.get(r2);
        if (r4 == null) goto L5;
        ArrayList r5 = new ArrayList(r4.trackIndices);
        boolean r6 = ((CheckedTextView) r12).isChecked();
        boolean r7 = shouldEnableAdaptiveSelection(r1.trackGroup);
        if (r7 == false) goto L13;
    L14:
        r0 = true;
    L15:
        if (r6 == false) goto L21;
        if (r0 == false) goto L21;
        r5.remove(Integer.valueOf(r3));
        if (r5.isEmpty() == false) goto L20;
        this.overrides.remove(r2);
        return;
    L20:
        this.overrides.put(r2, new TrackSelectionOverride(r2, r5));
        return;
    L21:
        if (r6 == true) goto L29;
        if (r7 == false) goto L24;
        r5.add(Integer.valueOf(r3));
        this.overrides.put(r2, new TrackSelectionOverride(r2, r5));
        return;
    L24:
        this.overrides.put(r2, new TrackSelectionOverride(r2, ImmutableList.m213of(Integer.valueOf(r3))));
        return;
    L29:
        return;
    L13:
        if (shouldEnableMultiGroupSelection() == false) goto L15;
    L5:
        if (this.allowMultipleOverrides == false) goto L7;
    L9:
        this.overrides.put(r2, new TrackSelectionOverride(r2, ImmutableList.m213of(Integer.valueOf(r3))));
        return;
    L7:
        if (this.overrides.size() <= 0) goto L9;
        this.overrides.clear();
        goto L9
    }

    private boolean shouldEnableAdaptiveSelection(Tracks.Group r2) {
        if (this.allowAdaptiveSelections == true) goto L5;
    L7:
        return false;
    L5:
        if (r2.isAdaptiveSupported() == false) goto L7;
        return true;
    }

    private boolean shouldEnableMultiGroupSelection() {
        if (this.allowMultipleOverrides == true) goto L5;
    L7:
        return false;
    L5:
        if (this.trackGroups.size() <= 1) goto L7;
        return true;
    }

    private void updateViewStates() {
        this.disableView.setChecked(this.isDisabled);
        CheckedTextView r0 = this.defaultView;
        if (this.isDisabled == false) goto L5;
    L7:
        boolean r1 = false;
    L8:
        r0.setChecked(r1);
        int r2 = 0;
    L10:
        if (r2 >= this.trackViews.length) goto L19;
        TrackSelectionOverride r3 = this.overrides.get(this.trackGroups.get(r2).getMediaTrackGroup());
        int r4 = 0;
    L13:
        if (r4 >= this.trackViews[r2].length) goto L18;
        if (r3 == null) goto L16;
        this.trackViews[r2][r4].setChecked(r3.trackIndices.contains(Integer.valueOf(((TrackInfo) Assertions.checkNotNull(this.trackViews[r2][r4].getTag())).trackIndex)));
    L17:
        r4 = r4 + 1;
        goto L13
    L16:
        this.trackViews[r2][r4].setChecked(false);
        goto L17
    L18:
        r2 = r2 + 1;
        goto L10
    L19:
        return;
    L5:
        if (this.overrides.size() != 0) goto L7;
        r1 = true;
        goto L8
    }

    private void updateViews() {
        int r0 = getChildCount() - 1;
    L4:
        if (r0 < 3) goto L7;
        removeViewAt(r0);
        r0 = r0 - 1;
        goto L4
    L7:
        if (this.trackGroups.isEmpty() == false) goto L10;
        this.disableView.setEnabled(false);
        this.defaultView.setEnabled(false);
        return;
    L10:
        this.disableView.setEnabled(true);
        this.defaultView.setEnabled(true);
        this.trackViews = new CheckedTextView[this.trackGroups.size()][];
        boolean r1 = shouldEnableMultiGroupSelection();
        int r3 = 0;
    L12:
        if (r3 >= this.trackGroups.size()) goto L36;
        Tracks.Group r4 = this.trackGroups.get(r3);
        boolean r5 = shouldEnableAdaptiveSelection(r4);
        this.trackViews[r3] = new CheckedTextView[r4.length];
        TrackInfo[] r6 = new TrackInfo[r4.length];
        int r7 = 0;
    L15:
        if (r7 >= r4.length) goto L18;
        r6[r7] = new TrackInfo(r4, r7);
        r7 = r7 + 1;
        goto L15
    L18:
        if (this.trackInfoComparator == null) goto L20;
        Arrays.sort(r6, this.trackInfoComparator);
    L20:
        int r8 = 0;
    L22:
        if (r8 >= r6.length) goto L35;
        if (r8 != 0) goto L25;
        addView(this.inflater.inflate(C0467R.layout.exo_list_divider, this, false));
    L25:
        if (r5 == true) goto L29;
        if (r1 == true) goto L29;
        int r9 = R.layout.simple_list_item_single_choice;
    L30:
        CheckedTextView r10 = (CheckedTextView) this.inflater.inflate(r9, this, false);
        r10.setBackgroundResource(this.selectableItemBackgroundResourceId);
        r10.setText(this.trackNameProvider.getTrackName(r6[r8].getFormat()));
        r10.setTag(r6[r8]);
        if (r4.isTrackSupported(r8) == false) goto L33;
        r10.setFocusable(true);
        r10.setOnClickListener(this.componentListener);
    L34:
        this.trackViews[r3][r8] = r10;
        addView(r10);
        r8 = r8 + 1;
        goto L22
    L33:
        r10.setFocusable(false);
        r10.setEnabled(false);
    L29:
        r9 = R.layout.simple_list_item_multiple_choice;
        goto L30
    L35:
        r3 = r3 + 1;
        goto L12
    L36:
        updateViewStates();
    }

    public boolean getIsDisabled() {
        return this.isDisabled;
    }

    public Map<TrackGroup, TrackSelectionOverride> getOverrides() {
        return this.overrides;
    }

    public void init(List<Tracks.Group> r3, boolean r4, Map<TrackGroup, TrackSelectionOverride> r5, final Comparator<Format> r6, TrackSelectionListener r7) {
        this.isDisabled = r4;
        if (r6 != null) goto L5;
        Comparator<TrackInfo> r0 = null;
    L6:
        this.trackInfoComparator = r0;
        this.listener = r7;
        this.trackGroups.clear();
        this.trackGroups.addAll(r3);
        this.overrides.clear();
        this.overrides.putAll(filterOverrides(r5, r3, this.allowMultipleOverrides));
        updateViews();
        return;
    L5:
        r0 = new TrackSelectionView$$ExternalSyntheticLambda0(r6);
        goto L6
    }

    public void setAllowAdaptiveSelections(boolean r2) {
        if (this.allowAdaptiveSelections == r2) goto L6;
        this.allowAdaptiveSelections = r2;
        updateViews();
        return;
    }

    public void setAllowMultipleOverrides(boolean r4) {
        if (this.allowMultipleOverrides == r4) goto L11;
        this.allowMultipleOverrides = r4;
        if (r4 == false) goto L7;
    L9:
        updateViews();
        return;
    L7:
        if (this.overrides.size() <= 1) goto L9;
        Map<TrackGroup, TrackSelectionOverride> r0 = filterOverrides(this.overrides, this.trackGroups, false);
        this.overrides.clear();
        this.overrides.putAll(r0);
        goto L9
    }

    public void setShowDisableOption(boolean r3) {
        CheckedTextView r0 = this.disableView;
        if (r3 == false) goto L5;
        int r1 = 0;
    L6:
        r0.setVisibility(r1);
        return;
    L5:
        r1 = 8;
        goto L6
    }

    public void setTrackNameProvider(TrackNameProvider r2) {
        this.trackNameProvider = (TrackNameProvider) Assertions.checkNotNull(r2);
        updateViews();
    }
}
