package kotlin;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ULongArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u001c\u0010\b\u001a\u00020\u00012\n\u0010\t\u001a\u00020\u0001\"\u00020\u0006H\u0087\b¢\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\f"}, m421d2 = {"ULongArray", "Lkotlin/ULongArray;", MediaInformation.KEY_SIZE, "", "init", "Lkotlin/Function1;", "Lkotlin/ULong;", "(ILkotlin/jvm/functions/Function1;)[J", "ulongArrayOf", "elements", "ulongArrayOf-QwZRm1k", "([J)[J", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class ULongArrayKt {
    private static final long[] ULongArray(int r4, Function1<? super Integer, ULong> r5) {
        Intrinsics.checkNotNullParameter(r5, "init");
        long[] r0 = new long[r4];
        int r1 = 0;
    L3:
        if (r1 >= r4) goto L6;
        r0[r1] = r5.invoke(Integer.valueOf(r1)).m1045unboximpl();
        r1 = r1 + 1;
        goto L3
    L6:
        return ULongArray.m1048constructorimpl(r0);
    }

    /* JADX INFO: renamed from: ulongArrayOf-QwZRm1k, reason: not valid java name */
    private static final long[] m1064ulongArrayOfQwZRm1k(long... r1) {
        Intrinsics.checkNotNullParameter(r1, "elements");
        return r1;
    }
}
