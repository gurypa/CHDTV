package androidx.media3.p002ui;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import androidx.media3.common.Format;
import androidx.media3.common.Player;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class TrackSelectionDialogBuilder {
    private boolean allowAdaptiveSelections;
    private boolean allowMultipleOverrides;
    private final DialogCallback callback;
    private final Context context;
    private boolean isDisabled;
    private ImmutableMap<TrackGroup, TrackSelectionOverride> overrides;
    private boolean showDisableOption;
    private int themeResId;
    private final CharSequence title;
    private Comparator<Format> trackFormatComparator;
    private final List<Tracks.Group> trackGroups;
    private TrackNameProvider trackNameProvider;

    public interface DialogCallback {
        void onTracksSelected(boolean r1, Map<TrackGroup, TrackSelectionOverride> r2);
    }

    public TrackSelectionDialogBuilder(Context r6, CharSequence r7, final Player r8, final int r9) {
        this.context = r6;
        this.title = r7;
        if (r8.isCommandAvailable(30) == false) goto L5;
        Tracks r0 = r8.getCurrentTracks();
    L6:
        ImmutableList<Tracks.Group> r1 = r0.getGroups();
        this.trackGroups = new ArrayList();
        int r2 = 0;
    L8:
        if (r2 >= r1.size()) goto L13;
        Tracks.Group r3 = r1.get(r2);
        if (r3.getType() != r9) goto L12;
        this.trackGroups.add(r3);
    L12:
        r2 = r2 + 1;
        goto L8
    L13:
        this.overrides = r8.getTrackSelectionParameters().overrides;
        this.callback = new TrackSelectionDialogBuilder$$ExternalSyntheticLambda1(r8, r9);
        return;
    L5:
        r0 = Tracks.EMPTY;
        goto L6
    }

    public TrackSelectionDialogBuilder(Context r2, CharSequence r3, List<Tracks.Group> r4, DialogCallback r5) {
        this.context = r2;
        this.title = r3;
        this.trackGroups = ImmutableList.copyOf(r4);
        this.callback = r5;
        this.overrides = ImmutableMap.m231of();
    }

    private Dialog buildForAndroidX() {
        Class<?> r1 = Class.forName("androidx.appcompat.app.AlertDialog$Builder");     // Catch: Exception -> L5 ClassNotFoundException -> L8
        Object r4 = r1.getConstructor(new Class[]{Context.class, Integer.TYPE}).newInstance(new Object[]{this.context, Integer.valueOf(this.themeResId)});     // Catch: Exception -> L5 ClassNotFoundException -> L8
        View r9 = LayoutInflater.from((Context) r1.getMethod("getContext", new Class[0]).invoke(r4, new Object[0])).inflate(C0467R.layout.exo_track_selection_dialog, null);     // Catch: Exception -> L5 ClassNotFoundException -> L8
        DialogInterface.OnClickListener r10 = setUpDialogView(r9);     // Catch: Exception -> L5 ClassNotFoundException -> L8
        r1.getMethod("setTitle", new Class[]{CharSequence.class}).invoke(r4, new Object[]{this.title});     // Catch: Exception -> L5 ClassNotFoundException -> L8
        r1.getMethod("setView", new Class[]{View.class}).invoke(r4, new Object[]{r9});     // Catch: Exception -> L5 ClassNotFoundException -> L8
        r1.getMethod("setPositiveButton", new Class[]{Integer.TYPE, DialogInterface.OnClickListener.class}).invoke(r4, new Object[]{Integer.valueOf(R.string.ok), r10});     // Catch: Exception -> L5 ClassNotFoundException -> L8
        r1.getMethod("setNegativeButton", new Class[]{Integer.TYPE, DialogInterface.OnClickListener.class}).invoke(r4, new Object[]{Integer.valueOf(R.string.cancel), null});     // Catch: Exception -> L5 ClassNotFoundException -> L8
        return (Dialog) r1.getMethod("create", new Class[0]).invoke(r4, new Object[0]);
    L5:
        e = move-exception;
        throw new IllegalStateException(e);
    L9:
        return null;
    }

    private Dialog buildForPlatform() {
        AlertDialog.Builder r0 = new AlertDialog.Builder(this.context, this.themeResId);
        View r2 = LayoutInflater.from(r0.getContext()).inflate(C0467R.layout.exo_track_selection_dialog, null);
        DialogInterface.OnClickListener r4 = setUpDialogView(r2);
        return r0.setTitle(this.title).setView(r2).setPositiveButton(R.string.ok, r4).setNegativeButton(R.string.cancel, null).create();
    }

    static /* synthetic */ void lambda$new$0(Player r3, int r4, boolean r5, Map r6) {
        if (r3.isCommandAvailable(29) == true) goto L5;
        return;
    L5:
        TrackSelectionParameters.Builder r0 = r3.getTrackSelectionParameters().buildUpon();
        r0.setTrackTypeDisabled(r4, r5);
        r0.clearOverridesOfType(r4);
        Iterator r1 = r6.values().iterator();
    L7:
        if (r1.hasNext() == false) goto L9;
        r0.addOverride((TrackSelectionOverride) r1.next());
        goto L7
    L9:
        r3.setTrackSelectionParameters(r0.build());
    }

    private DialogInterface.OnClickListener setUpDialogView(View r8) {
        final TrackSelectionView r1 = (TrackSelectionView) r8.findViewById(C0467R.id.exo_track_selection_view);
        r1.setAllowMultipleOverrides(this.allowMultipleOverrides);
        r1.setAllowAdaptiveSelections(this.allowAdaptiveSelections);
        r1.setShowDisableOption(this.showDisableOption);
        if (this.trackNameProvider == null) goto L5;
        r1.setTrackNameProvider(this.trackNameProvider);
    L5:
        r1.init(this.trackGroups, this.isDisabled, this.overrides, this.trackFormatComparator, null);
        return new TrackSelectionDialogBuilder$$ExternalSyntheticLambda0(this, r1);
    }

    public Dialog build() {
        Dialog r0 = buildForAndroidX();
        if (r0 != null) goto L6;
        return buildForPlatform();
    L6:
        return r0;
    }

    /* JADX INFO: renamed from: lambda$setUpDialogView$1$androidx-media3-ui-TrackSelectionDialogBuilder */
    /* synthetic */ void m176xc121951d(TrackSelectionView r4, DialogInterface r5, int r6) {
        this.callback.onTracksSelected(r4.getIsDisabled(), r4.getOverrides());
    }

    public TrackSelectionDialogBuilder setAllowAdaptiveSelections(boolean r1) {
        this.allowAdaptiveSelections = r1;
        return this;
    }

    public TrackSelectionDialogBuilder setAllowMultipleOverrides(boolean r1) {
        this.allowMultipleOverrides = r1;
        return this;
    }

    public TrackSelectionDialogBuilder setIsDisabled(boolean r1) {
        this.isDisabled = r1;
        return this;
    }

    public TrackSelectionDialogBuilder setOverride(TrackSelectionOverride r2) {
        if (r2 != null) goto L4;
        Map<TrackGroup, TrackSelectionOverride> r0 = Collections.emptyMap();
    L6:
        return setOverrides(r0);
    L4:
        r0 = ImmutableMap.m232of(r2.mediaTrackGroup, r2);
        goto L6
    }

    public TrackSelectionDialogBuilder setOverrides(Map<TrackGroup, TrackSelectionOverride> r2) {
        this.overrides = ImmutableMap.copyOf(r2);
        return this;
    }

    public TrackSelectionDialogBuilder setShowDisableOption(boolean r1) {
        this.showDisableOption = r1;
        return this;
    }

    public TrackSelectionDialogBuilder setTheme(int r1) {
        this.themeResId = r1;
        return this;
    }

    public void setTrackFormatComparator(Comparator<Format> r1) {
        this.trackFormatComparator = r1;
    }

    public TrackSelectionDialogBuilder setTrackNameProvider(TrackNameProvider r1) {
        this.trackNameProvider = r1;
        return this;
    }
}
