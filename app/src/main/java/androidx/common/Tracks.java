package androidx.media3.common;

import android.os.Bundle;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Util;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Booleans;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Tracks {
    public static final Tracks EMPTY = null;
    private static final String FIELD_TRACK_GROUPS = null;
    private final ImmutableList<Group> groups;

    public static final class Group {
        private static final String FIELD_ADAPTIVE_SUPPORTED = null;
        private static final String FIELD_TRACK_GROUP = null;
        private static final String FIELD_TRACK_SELECTED = null;
        private static final String FIELD_TRACK_SUPPORT = null;
        private final boolean adaptiveSupported;
        public final int length;
        private final TrackGroup mediaTrackGroup;
        private final boolean[] trackSelected;
        private final int[] trackSupport;

        static {
            FIELD_TRACK_GROUP = Util.intToStringMaxRadix(0);
            FIELD_TRACK_SUPPORT = Util.intToStringMaxRadix(1);
            FIELD_TRACK_SELECTED = Util.intToStringMaxRadix(3);
            FIELD_ADAPTIVE_SUPPORTED = Util.intToStringMaxRadix(4);
        }

        public Group(TrackGroup r5, boolean r6, int[] r7, boolean[] r8) {
            this.length = r5.length;
            boolean r2 = false;
            if (this.length == r7.length) goto L5;
        L7:
            boolean r0 = false;
        L8:
            Assertions.checkArgument(r0);
            this.mediaTrackGroup = r5;
            if (r6 == true) goto L11;
        L13:
            this.adaptiveSupported = r2;
            this.trackSupport = (int[]) r7.clone();
            this.trackSelected = (boolean[]) r8.clone();
            return;
        L11:
            if (this.length <= 1) goto L13;
            r2 = true;
            goto L13
        L5:
            if (this.length != r8.length) goto L7;
            r0 = true;
            goto L8
        }

        public static Group fromBundle(Bundle r5) {
            TrackGroup r0 = TrackGroup.fromBundle((Bundle) Assertions.checkNotNull(r5.getBundle(FIELD_TRACK_GROUP)));
            int[] r1 = (int[]) MoreObjects.firstNonNull(r5.getIntArray(FIELD_TRACK_SUPPORT), new int[r0.length]);
            boolean[] r2 = (boolean[]) MoreObjects.firstNonNull(r5.getBooleanArray(FIELD_TRACK_SELECTED), new boolean[r0.length]);
            return new Group(r0, r5.getBoolean(FIELD_ADAPTIVE_SUPPORTED, false), r1, r2);
        }

        public Group copyWithId(String r6) {
            return new Group(this.mediaTrackGroup.copyWithId(r6), this.adaptiveSupported, this.trackSupport, this.trackSelected);
        }

        public boolean equals(Object r6) {
            if (this != r6) goto L6;
            return true;
        L6:
            if (r6 != null) goto L8;
        L21:
            return false;
        L8:
            if (getClass() != r6.getClass()) goto L21;
            Group r2 = (Group) r6;
            if (this.adaptiveSupported != r2.adaptiveSupported) goto L20;
            if (this.mediaTrackGroup.equals(r2.mediaTrackGroup) == false) goto L20;
            if (Arrays.equals(this.trackSupport, r2.trackSupport) == false) goto L20;
            if (Arrays.equals(this.trackSelected, r2.trackSelected) == false) goto L20;
            return true;
        L20:
            return false;
        }

        public TrackGroup getMediaTrackGroup() {
            return this.mediaTrackGroup;
        }

        public Format getTrackFormat(int r2) {
            return this.mediaTrackGroup.getFormat(r2);
        }

        public int getTrackSupport(int r2) {
            return this.trackSupport[r2];
        }

        public int getType() {
            return this.mediaTrackGroup.type;
        }

        public int hashCode() {
            return (((((this.mediaTrackGroup.hashCode() * 31) + (this.adaptiveSupported ? 1 : 0)) * 31) + Arrays.hashCode(this.trackSupport)) * 31) + Arrays.hashCode(this.trackSelected);
        }

        public boolean isAdaptiveSupported() {
            return this.adaptiveSupported;
        }

        public boolean isSelected() {
            return Booleans.contains(this.trackSelected, true);
        }

        public boolean isSupported() {
            return isSupported(false);
        }

        public boolean isSupported(boolean r3) {
            int r0 = 0;
        L4:
            if (r0 >= this.trackSupport.length) goto L10;
            if (isTrackSupported(r0, r3) == true) goto L7;
            r0 = r0 + 1;
            goto L4
        L7:
            return true;
        L10:
            return false;
        }

        public boolean isTrackSelected(int r2) {
            return this.trackSelected[r2];
        }

        public boolean isTrackSupported(int r2) {
            return isTrackSupported(r2, false);
        }

        public boolean isTrackSupported(int r3, boolean r4) {
            if (this.trackSupport[r3] == 4) goto L9;
            if (r4 == true) goto L6;
        L8:
            return false;
        L6:
            if (this.trackSupport[r3] != 3) goto L8;
        L9:
            return true;
        }

        public Bundle toBundle() {
            Bundle r0 = new Bundle();
            r0.putBundle(FIELD_TRACK_GROUP, this.mediaTrackGroup.toBundle());
            r0.putIntArray(FIELD_TRACK_SUPPORT, this.trackSupport);
            r0.putBooleanArray(FIELD_TRACK_SELECTED, this.trackSelected);
            r0.putBoolean(FIELD_ADAPTIVE_SUPPORTED, this.adaptiveSupported);
            return r0;
        }
    }

    static {
        EMPTY = new Tracks(ImmutableList.m212of());
        FIELD_TRACK_GROUPS = Util.intToStringMaxRadix(0);
    }

    public Tracks(List<Group> r2) {
        this.groups = ImmutableList.copyOf(r2);
    }

    public static Tracks fromBundle(Bundle r3) {
        ArrayList r0 = r3.getParcelableArrayList(FIELD_TRACK_GROUPS);
        if (r0 != null) goto L5;
        ImmutableList r1 = ImmutableList.m212of();
    L7:
        return new Tracks(r1);
    L5:
        r1 = BundleCollectionUtil.fromBundleList(new Tracks$$ExternalSyntheticLambda1(), r0);
        goto L7
    }

    public boolean containsType(int r3) {
        int r0 = 0;
    L4:
        if (r0 >= this.groups.size()) goto L10;
        if (this.groups.get(r0).getType() == r3) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return true;
    L10:
        return false;
    }

    public boolean equals(Object r4) {
        if (this != r4) goto L5;
        return true;
    L5:
        if (r4 != null) goto L7;
        return false;
    L7:
        if (getClass() == r4.getClass()) goto L10;
        return false;
    L10:
        return this.groups.equals(((Tracks) r4).groups);
    }

    public ImmutableList<Group> getGroups() {
        return this.groups;
    }

    public int hashCode() {
        return this.groups.hashCode();
    }

    public boolean isEmpty() {
        return this.groups.isEmpty();
    }

    public boolean isTypeSelected(int r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.groups.size()) goto L12;
        Group r1 = this.groups.get(r0);
        if (r1.isSelected() == false) goto L11;
        if (r1.getType() != r4) goto L11;
        return true;
    L11:
        r0 = r0 + 1;
        goto L4
    L12:
        return false;
    }

    public boolean isTypeSupported(int r2) {
        return isTypeSupported(r2, false);
    }

    public boolean isTypeSupported(int r3, boolean r4) {
        int r0 = 0;
    L4:
        if (r0 >= this.groups.size()) goto L12;
        if (this.groups.get(r0).getType() != r3) goto L11;
        if (this.groups.get(r0).isSupported(r4) == false) goto L11;
        return true;
    L11:
        r0 = r0 + 1;
        goto L4
    L12:
        return false;
    }

    @Deprecated
    public boolean isTypeSupportedOrEmpty(int r2) {
        return isTypeSupportedOrEmpty(r2, false);
    }

    @Deprecated
    public boolean isTypeSupportedOrEmpty(int r2, boolean r3) {
        if (containsType(r2) == true) goto L5;
    L8:
        return true;
    L5:
        if (isTypeSupported(r2, r3) == true) goto L8;
        return false;
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putParcelableArrayList(FIELD_TRACK_GROUPS, BundleCollectionUtil.toBundleArrayList(this.groups, new Tracks$$ExternalSyntheticLambda0()));
        return r0;
    }
}
