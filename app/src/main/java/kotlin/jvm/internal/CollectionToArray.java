package kotlin.jvm.internal;

import androidx.media3.exoplayer.upstream.CmcdData;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/* JADX INFO: compiled from: CollectionToArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, m421d2 = {"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class CollectionToArray {
    private static final Object[] EMPTY = null;
    private static final int MAX_SIZE = 2147483645;

    static {
        EMPTY = new Object[0];
    }

    @Deprecated(message = "This function will be made internal in a future release")
    @DeprecatedSinceKotlin(warningSince = "1.9")
    public static final Object[] toArray(Collection<?> r10) {
        Intrinsics.checkNotNullParameter(r10, "collection");
        int r1 = r10.size();
        if (r1 == 0) goto L35;
        Iterator<?> r2 = r10.iterator();
        if (r2.hasNext() == false) goto L36;
        Object[] r3 = new Object[r1];
        int r4 = 0;
    L9:
        r4 = r4 + 1;
        r3[r4] = r2.next();
        if (r4 >= r3.length) goto L12;
        if (r2.hasNext() == true) goto L9;
        Object[] r9 = Arrays.copyOf(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r9, "copyOf(...)");
        return r9;
    L12:
        if (r2.hasNext() == false) goto L37;
        int r5 = ((r4 * 3) + 1) >>> 1;
        if (r5 <= r4) goto L17;
    L21:
        Object[] r7 = Arrays.copyOf(r3, r5);
        Intrinsics.checkNotNullExpressionValue(r7, "copyOf(...)");
        r3 = r7;
        goto L9
    L17:
        if (r4 >= MAX_SIZE) goto L20;
        r5 = MAX_SIZE;
        goto L21
    L20:
        throw new OutOfMemoryError();
    L37:
        return r3;
    L36:
        return EMPTY;
    L35:
        return EMPTY;
    }

    @Deprecated(message = "This function will be made internal in a future release")
    @DeprecatedSinceKotlin(warningSince = "1.9")
    public static final Object[] toArray(Collection<?> r10, Object[] r11) {
        Intrinsics.checkNotNullParameter(r10, "collection");
        if (r11 == null) goto L45;
        int r1 = r10.size();
        if (r1 == 0) goto L7;
        Iterator<?> r4 = r10.iterator();
        if (r4.hasNext() == true) goto L18;
        if (r11.length <= 0) goto L54;
        r11[0] = null;
    L54:
        return r11;
    L18:
        if (r1 > r11.length) goto L20;
        Object[] r6 = r11;
    L21:
        int r2 = 0;
    L22:
        r2 = r2 + 1;
        r6[r2] = r4.next();
        if (r2 >= r6.length) goto L25;
        if (r4.hasNext() == true) goto L22;
        Object[] r3 = r6;
        if (r3 != r11) goto L40;
        r11[r2] = null;
        Object[] r5 = r11;
    L42:
        return r5;
    L40:
        r5 = Arrays.copyOf(r3, r2);
        Intrinsics.checkNotNullExpressionValue(r5, "copyOf(...)");
        goto L42
    L25:
        if (r4.hasNext() == false) goto L55;
        int r7 = ((r2 * 3) + 1) >>> 1;
        if (r7 <= r2) goto L30;
    L34:
        Object[] r8 = Arrays.copyOf(r6, r7);
        Intrinsics.checkNotNullExpressionValue(r8, "copyOf(...)");
        r6 = r8;
        goto L22
    L30:
        if (r2 >= MAX_SIZE) goto L33;
        r7 = MAX_SIZE;
        goto L34
    L33:
        throw new OutOfMemoryError();
    L55:
        return r6;
    L20:
        Object r9 = Array.newInstance(r11.getClass().getComponentType(), r1);
        Intrinsics.checkNotNull(r9, "null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        r6 = (Object[]) r9;
        goto L21
    L7:
        if (r11.length <= 0) goto L54;
        r11[0] = null;
        goto L54
    L45:
        throw new NullPointerException();
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.Object, java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    private static final Object[] toArrayImpl(Collection<?> r8, Function0<Object[]> r9, Function1<? super Integer, Object[]> r10, Function2<? super Object[], ? super Integer, Object[]> r11) {
        int r1 = r8.size();
        if (r1 == 0) goto L5;
        Iterator<?> r2 = r8.iterator();
        if (r2.hasNext() == false) goto L9;
        Object[] r3 = r10.invoke(Integer.valueOf(r1));
        int r4 = 0;
    L11:
        r4 = r4 + 1;
        r3[r4] = r2.next();
        if (r4 >= r3.length) goto L14;
        if (r2.hasNext() == true) goto L11;
        return r11.invoke(r3, Integer.valueOf(r4));
    L14:
        if (r2.hasNext() == false) goto L15;
        int r5 = ((r4 * 3) + 1) >>> 1;
        if (r5 <= r4) goto L19;
    L23:
        Object[] r6 = Arrays.copyOf(r3, r5);
        Intrinsics.checkNotNullExpressionValue(r6, "copyOf(...)");
        r3 = r6;
        goto L11
    L19:
        if (r4 >= MAX_SIZE) goto L22;
        r5 = MAX_SIZE;
        goto L23
    L22:
        throw new OutOfMemoryError();
    L15:
        return r3;
    L9:
        return r9.invoke();
    L5:
        return r9.invoke();
    }
}
