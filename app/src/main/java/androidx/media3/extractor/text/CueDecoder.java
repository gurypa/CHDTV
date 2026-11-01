package androidx.media3.extractor.text;

import android.os.Bundle;
import android.os.Parcel;
import androidx.media3.common.text.CueGroup$$ExternalSyntheticLambda0;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.BundleCollectionUtil;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class CueDecoder {
    static final String BUNDLE_FIELD_CUES = "c";
    static final String BUNDLE_FIELD_DURATION_US = "d";

    public CueDecoder() {
    }

    public CuesWithTiming decode(long r10, byte[] r12, int r13, int r14) {
        Parcel r0 = Parcel.obtain();
        r0.unmarshall(r12, r13, r14);
        r0.setDataPosition(0);
        Bundle r1 = r0.readBundle(Bundle.class.getClassLoader());
        r0.recycle();
        ArrayList r2 = (ArrayList) Assertions.checkNotNull(r1.getParcelableArrayList(BUNDLE_FIELD_CUES));
        return new CuesWithTiming(BundleCollectionUtil.fromBundleList(new CueGroup$$ExternalSyntheticLambda0(), r2), r10, r1.getLong("d"));
    }
}
