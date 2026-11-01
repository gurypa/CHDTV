package androidx.media3.common.text;

import android.os.Bundle;
import androidx.media3.common.util.BundleCollectionUtil;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class CueGroup {
    public static final CueGroup EMPTY_TIME_ZERO = null;
    private static final String FIELD_CUES = null;
    private static final String FIELD_PRESENTATION_TIME_US = null;
    public final ImmutableList<Cue> cues;
    public final long presentationTimeUs;

    static {
        EMPTY_TIME_ZERO = new CueGroup(ImmutableList.m212of(), 0);
        FIELD_CUES = Util.intToStringMaxRadix(0);
        FIELD_PRESENTATION_TIME_US = Util.intToStringMaxRadix(1);
    }

    public CueGroup(List<Cue> r2, long r3) {
        this.cues = ImmutableList.copyOf(r2);
        this.presentationTimeUs = r3;
    }

    private static ImmutableList<Cue> filterOutBitmapCues(List<Cue> r3) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        int r1 = 0;
    L4:
        if (r1 >= r3.size()) goto L11;
        if (r3.get(r1).bitmap != null) goto L9;
        r0.add(r3.get(r1));
    L9:
        r1 = r1 + 1;
        goto L4
    L11:
        return r0.build();
    }

    public static CueGroup fromBundle(Bundle r5) {
        ArrayList r0 = r5.getParcelableArrayList(FIELD_CUES);
        if (r0 != null) goto L5;
        ImmutableList r1 = ImmutableList.m212of();
    L7:
        return new CueGroup(r1, r5.getLong(FIELD_PRESENTATION_TIME_US));
    L5:
        r1 = BundleCollectionUtil.fromBundleList(new CueGroup$$ExternalSyntheticLambda0(), r0);
        goto L7
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putParcelableArrayList(FIELD_CUES, BundleCollectionUtil.toBundleArrayList(filterOutBitmapCues(this.cues), new CueGroup$$ExternalSyntheticLambda1()));
        r0.putLong(FIELD_PRESENTATION_TIME_US, this.presentationTimeUs);
        return r0;
    }
}
