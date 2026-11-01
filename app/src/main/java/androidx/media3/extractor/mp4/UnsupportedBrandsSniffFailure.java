package androidx.media3.extractor.mp4;

import androidx.media3.extractor.SniffFailure;
import com.google.common.primitives.ImmutableIntArray;

/* JADX INFO: loaded from: classes.dex */
public final class UnsupportedBrandsSniffFailure implements SniffFailure {
    public final ImmutableIntArray compatibleBrands;
    public final int majorBrand;

    public UnsupportedBrandsSniffFailure(int r2, int[] r3) {
        this.majorBrand = r2;
        if (r3 == null) goto L5;
        ImmutableIntArray r0 = ImmutableIntArray.copyOf(r3);
    L6:
        this.compatibleBrands = r0;
        return;
    L5:
        r0 = ImmutableIntArray.m355of();
        goto L6
    }
}
