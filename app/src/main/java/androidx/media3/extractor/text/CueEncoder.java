package androidx.media3.extractor.text;

import android.os.Bundle;
import android.os.Parcel;
import androidx.media3.common.text.Cue;
import androidx.media3.common.util.BundleCollectionUtil;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class CueEncoder {
    public CueEncoder() {
    }

    public byte[] encode(List<Cue> r5, long r6) {
        ArrayList<Bundle> r0 = BundleCollectionUtil.toBundleArrayList(r5, new CueEncoder$$ExternalSyntheticLambda0());
        Bundle r1 = new Bundle();
        r1.putParcelableArrayList("c", r0);
        r1.putLong("d", r6);
        Parcel r2 = Parcel.obtain();
        r2.writeBundle(r1);
        byte[] r3 = r2.marshall();
        r2.recycle();
        return r3;
    }
}
