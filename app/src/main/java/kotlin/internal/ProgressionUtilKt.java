package kotlin.internal;

import androidx.media3.exoplayer.upstream.CmcdData;
import kotlin.Metadata;

/* JADX INFO: compiled from: progressionUtil.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u001a \u0010\u0000\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a \u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a \u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H\u0001\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0002¨\u0006\u000b"}, m421d2 = {"differenceModulo", "", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "c", "", "getProgressionLastElement", "start", "end", "step", "mod", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class ProgressionUtilKt {
    private static final int differenceModulo(int r2, int r3, int r4) {
        return mod(mod(r2, r4) - mod(r3, r4), r4);
    }

    private static final long differenceModulo(long r4, long r6, long r8) {
        return mod(mod(r4, r8) - mod(r6, r8), r8);
    }

    public static final int getProgressionLastElement(int r2, int r3, int r4) {
        if (r4 <= 0) goto L6;
        if (r2 >= r3) goto L14;
        return r3 - differenceModulo(r3, r2, r4);
    L14:
        return r3;
    L6:
        if (r4 >= 0) goto L12;
        if (r2 <= r3) goto L14;
        return differenceModulo(r2, r3, -r4) + r3;
    L12:
        throw new IllegalArgumentException("Step is zero.");
    }

    public static final long getProgressionLastElement(long r6, long r8, long r10) {
        if (r10 <= 0) goto L9;
        if (r6 < r8) goto L17;
    L18:
        return r8;
    L17:
        return r8 - differenceModulo(r8, r6, r10);
    L9:
        if (r10 >= 0) goto L16;
        if (r6 <= r8) goto L18;
        return differenceModulo(r6, r8, -r10) + r8;
    L16:
        throw new IllegalArgumentException("Step is zero.");
    }

    private static final int mod(int r2, int r3) {
        int r0 = r2 % r3;
        if (r0 < 0) goto L6;
        return r0;
    L6:
        return r0 + r3;
    }

    private static final long mod(long r4, long r6) {
        long r0 = r4 % r6;
        if (r0 < 0) goto L6;
        return r0;
    L6:
        return r0 + r6;
    }
}
