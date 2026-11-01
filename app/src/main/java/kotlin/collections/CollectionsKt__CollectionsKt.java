package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.random.Random;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: Collections.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0086\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aC\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\bø\u0001\u0000\u001aC\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\bø\u0001\u0000\u001a\u001f\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a5\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007¢\u0006\u0002\u0010\u0019\u001aN\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u001b0\b\"\u0004\b\u0000\u0010\u001b2\u0006\u0010\u001c\u001a\u00020\u00062\u001f\b\u0001\u0010\u001d\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0013\u0012\u0004\u0012\u00020\u001e0\u000e¢\u0006\u0002\b\u001fH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001aF\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u001b0\b\"\u0004\b\u0000\u0010\u001b2\u001f\b\u0001\u0010\u001d\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0013\u0012\u0004\u0012\u00020\u001e0\u000e¢\u0006\u0002\b\u001fH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a!\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010!0\u00182\n\u0010\"\u001a\u0006\u0012\u0002\b\u00030\u0002H\u0000¢\u0006\u0002\u0010#\u001a3\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00070\u0018\"\u0004\b\u0000\u0010\u00072\n\u0010\"\u001a\u0006\u0012\u0002\b\u00030\u00022\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0018H\u0000¢\u0006\u0002\u0010%\u001a\u0012\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007\u001a\u0015\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007¢\u0006\u0002\u0010(\u001a%\u0010)\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020!2\b\u0010*\u001a\u0004\u0018\u0001H\u0007¢\u0006\u0002\u0010+\u001a3\u0010)\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020!2\u0016\u0010\u0017\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00070\u0018\"\u0004\u0018\u0001H\u0007¢\u0006\u0002\u0010(\u001a\u0015\u0010,\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010,\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007¢\u0006\u0002\u0010(\u001a%\u0010-\u001a\u00020\u001e2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010.\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u0006H\u0002¢\u0006\u0002\b0\u001a\b\u00101\u001a\u00020\u001eH\u0001\u001a\b\u00102\u001a\u00020\u001eH\u0001\u001a%\u00103\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018H\u0000¢\u0006\u0002\u00104\u001aS\u00105\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\u0006\u0010*\u001a\u0002H\u00072\u001a\u00106\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000707j\n\u0012\u0006\b\u0000\u0012\u0002H\u0007`82\b\b\u0002\u0010.\u001a\u00020\u00062\b\b\u0002\u0010/\u001a\u00020\u0006¢\u0006\u0002\u00109\u001a>\u00105\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\b\u0002\u0010.\u001a\u00020\u00062\b\b\u0002\u0010/\u001a\u00020\u00062\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u00060\u000e\u001aE\u00105\u001a\u00020\u0006\"\u000e\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070;*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00070\b2\b\u0010*\u001a\u0004\u0018\u0001H\u00072\b\b\u0002\u0010.\u001a\u00020\u00062\b\b\u0002\u0010/\u001a\u00020\u0006¢\u0006\u0002\u0010<\u001ag\u0010=\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007\"\u000e\b\u0001\u0010>*\b\u0012\u0004\u0012\u0002H>0;*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\u0010?\u001a\u0004\u0018\u0001H>2\b\b\u0002\u0010.\u001a\u00020\u00062\b\b\u0002\u0010/\u001a\u00020\u00062\u0016\b\u0004\u0010@\u001a\u0010\u0012\u0004\u0012\u0002H\u0007\u0012\u0006\u0012\u0004\u0018\u0001H>0\u000eH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010A\u001a,\u0010B\u001a\u00020C\"\t\b\u0000\u0010\u0007¢\u0006\u0002\bD*\b\u0012\u0004\u0012\u0002H\u00070\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a;\u0010E\u001a\u0002HF\"\u0010\b\u0000\u0010G*\u0006\u0012\u0002\b\u00030\u0002*\u0002HF\"\u0004\b\u0001\u0010F*\u0002HG2\f\u0010H\u001a\b\u0012\u0004\u0012\u0002HF0IH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010J\u001a\u0019\u0010K\u001a\u00020C\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a,\u0010L\u001a\u00020C\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\u001e\u0010M\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\bH\u0000\u001a!\u0010N\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u001a!\u0010N\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\bH\u0087\b\u001a&\u0010O\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070P2\u0006\u0010Q\u001a\u00020RH\u0007\"\u0019\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"!\u0010\u0005\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006S"}, m421d2 = {"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/util/Collection;)Lkotlin/ranges/IntRange;", "lastIndex", "", ExifInterface.GPS_DIRECTION_TRUE, "", "getLastIndex", "(Ljava/util/List;)I", "List", MediaInformation.KEY_SIZE, "init", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", StreamInformation.KEY_INDEX, "MutableList", "", "arrayListOf", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "elements", "", "([Ljava/lang/Object;)Ljava/util/ArrayList;", "buildList", ExifInterface.LONGITUDE_EAST, "capacity", "builderAction", "", "Lkotlin/ExtensionFunctionType;", "collectionToArrayCommonImpl", "", "collection", "(Ljava/util/Collection;)[Ljava/lang/Object;", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "emptyList", "listOf", "([Ljava/lang/Object;)Ljava/util/List;", "listOfNotNull", "element", "(Ljava/lang/Object;)Ljava/util/List;", "mutableListOf", "rangeCheck", "fromIndex", "toIndex", "rangeCheck$CollectionsKt__CollectionsKt", "throwCountOverflow", "throwIndexOverflow", "asCollection", "([Ljava/lang/Object;)Ljava/util/Collection;", "binarySearch", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/List;Ljava/lang/Object;Ljava/util/Comparator;II)I", "comparison", "", "(Ljava/util/List;Ljava/lang/Comparable;II)I", "binarySearchBy", "K", "key", "selector", "(Ljava/util/List;Ljava/lang/Comparable;IILkotlin/jvm/functions/Function1;)I", "containsAll", "", "Lkotlin/internal/OnlyInputTypes;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNotEmpty", "isNullOrEmpty", "optimizeReadOnlyList", "orEmpty", "shuffled", "", "random", "Lkotlin/random/Random;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt {

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlin.collections.CollectionsKt__CollectionsKt$binarySearchBy$1 */
    /* JADX INFO: compiled from: Collections.kt */
    @Metadata(m420d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0005\u001a\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "K", "", "it", "invoke", "(Ljava/lang/Object;)Ljava/lang/Integer;"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 176)
    public static final class C10611<T> extends Lambda implements Function1<T, Integer> {

        /* JADX INFO: Incorrect field signature: TK; */
        final /* synthetic */ Comparable $key;
        final /* synthetic */ Function1<T, K> $selector;

        /* JADX WARN: Incorrect types in method signature: (Lkotlin/jvm/functions/Function1<-TT;+TK;>;TK;)V */
        public C10611(Function1 r2, Comparable r3) {
            this.$selector = r2;
            this.$key = r3;
            super(1);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function1
        public final Integer invoke(T r3) {
            return Integer.valueOf(ComparisonsKt.compareValues((Comparable) this.$selector.invoke(r3), this.$key));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Integer invoke(Object r2) {
            return invoke(r2);
        }
    }

    public CollectionsKt__CollectionsKt() {
    }

    private static final <T> List<T> List(int r3, Function1<? super Integer, ? extends T> r4) {
        Intrinsics.checkNotNullParameter(r4, "init");
        ArrayList r0 = new ArrayList(r3);
        int r1 = 0;
    L3:
        if (r1 >= r3) goto L6;
        r0.add(r4.invoke(Integer.valueOf(r1)));
        r1 = r1 + 1;
        goto L3
    L6:
        return r0;
    }

    private static final <T> List<T> MutableList(int r5, Function1<? super Integer, ? extends T> r6) {
        Intrinsics.checkNotNullParameter(r6, "init");
        ArrayList r0 = new ArrayList(r5);
        int r1 = 0;
    L3:
        if (r1 >= r5) goto L6;
        r0.add(r6.invoke(Integer.valueOf(r1)));
        r1 = r1 + 1;
        goto L3
    L6:
        return r0;
    }

    private static final <T> ArrayList<T> arrayListOf() {
        return new ArrayList();
    }

    public static final <T> ArrayList<T> arrayListOf(T... r3) {
        Intrinsics.checkNotNullParameter(r3, "elements");
        if (r3.length != 0) goto L6;
        return new ArrayList();
    L6:
        return new ArrayList(new ArrayAsCollection(r3, true));
    }

    public static final <T> Collection<T> asCollection(T[] r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new ArrayAsCollection(r2, false);
    }

    public static final <T> int binarySearch(List<? extends T> r5, int r6, int r7, Function1<? super T, Integer> r8) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r8, "comparison");
        rangeCheck$CollectionsKt__CollectionsKt(r5.size(), r6, r7);
        int r0 = r6;
        int r1 = r7 - 1;
    L3:
        if (r0 > r1) goto L11;
        int r2 = (r0 + r1) >>> 1;
        int r4 = r8.invoke(r5.get(r2)).intValue();
        if (r4 < 0) goto L6;
        if (r4 <= 0) goto L9;
        r1 = r2 - 1;
        goto L3
    L9:
        return r2;
    L6:
        r0 = r2 + 1;
        goto L3
    L11:
        return -(r0 + 1);
    }

    public static final <T extends Comparable<? super T>> int binarySearch(List<? extends T> r5, T r6, int r7, int r8) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        rangeCheck$CollectionsKt__CollectionsKt(r5.size(), r7, r8);
        int r0 = r7;
        int r1 = r8 - 1;
    L3:
        if (r0 > r1) goto L11;
        int r2 = (r0 + r1) >>> 1;
        int r4 = ComparisonsKt.compareValues(r5.get(r2), r6);
        if (r4 < 0) goto L6;
        if (r4 <= 0) goto L9;
        r1 = r2 - 1;
        goto L3
    L9:
        return r2;
    L6:
        r0 = r2 + 1;
        goto L3
    L11:
        return -(r0 + 1);
    }

    public static final <T> int binarySearch(List<? extends T> r5, T r6, Comparator<? super T> r7, int r8, int r9) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r7, "comparator");
        rangeCheck$CollectionsKt__CollectionsKt(r5.size(), r8, r9);
        int r0 = r8;
        int r1 = r9 - 1;
    L3:
        if (r0 > r1) goto L11;
        int r2 = (r0 + r1) >>> 1;
        int r4 = r7.compare(r5.get(r2), r6);
        if (r4 < 0) goto L6;
        if (r4 <= 0) goto L9;
        r1 = r2 - 1;
        goto L3
    L9:
        return r2;
    L6:
        r0 = r2 + 1;
        goto L3
    L11:
        return -(r0 + 1);
    }

    public static /* synthetic */ int binarySearch$default(List r0, int r1, int r2, Function1 r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = 0;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.size();
    L9:
        return CollectionsKt.binarySearch(r0, r1, r2, r3);
    }

    public static /* synthetic */ int binarySearch$default(List r0, Comparable r1, int r2, int r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = r0.size();
    L9:
        return CollectionsKt.binarySearch(r0, r1, r2, r3);
    }

    public static /* synthetic */ int binarySearch$default(List r0, Object r1, Comparator r2, int r3, int r4, int r5, Object r6) {
        if ((r5 & 4) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r5 & 8) == 0) goto L9;
        r4 = r0.size();
    L9:
        return CollectionsKt.binarySearch(r0, r1, r2, r3, r4);
    }

    public static final <T, K extends Comparable<? super K>> int binarySearchBy(List<? extends T> r2, K r3, int r4, int r5, Function1<? super T, ? extends K> r6) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r6, "selector");
        return CollectionsKt.binarySearch(r2, r4, r5, new C10611(r6, r3));
    }

    public static /* synthetic */ int binarySearchBy$default(List r0, Comparable r1, int r2, int r3, Function1 r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        if ((r5 & 4) == 0) goto L8;
        r3 = r0.size();
    L8:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r4, "selector");
        return CollectionsKt.binarySearch(r0, r2, r3, new C10611(r4, r1));
    }

    private static final <E> List<E> buildList(int r1, Function1<? super List<E>, Unit> r2) {
        Intrinsics.checkNotNullParameter(r2, "builderAction");
        List r0 = CollectionsKt.createListBuilder(r1);
        r2.invoke(r0);
        return CollectionsKt.build(r0);
    }

    private static final <E> List<E> buildList(Function1<? super List<E>, Unit> r1) {
        Intrinsics.checkNotNullParameter(r1, "builderAction");
        List r0 = CollectionsKt.createListBuilder();
        r1.invoke(r0);
        return CollectionsKt.build(r0);
    }

    public static final Object[] collectionToArrayCommonImpl(Collection<?> r5) {
        Intrinsics.checkNotNullParameter(r5, "collection");
        if (r5.isEmpty() == true) goto L5;
        Object[] r0 = new Object[r5.size()];
        Iterator<?> r1 = r5.iterator();
        int r2 = 0;
    L8:
        if (r1.hasNext() == false) goto L10;
        r0[r2] = r1.next();
        r2 = r2 + 1;
        goto L8
    L10:
        return r0;
    L5:
        return new Object[0];
    }

    public static final <T> T[] collectionToArrayCommonImpl(Collection<?> r5, T[] r6) {
        Intrinsics.checkNotNullParameter(r5, "collection");
        Intrinsics.checkNotNullParameter(r6, "array");
        if (r5.isEmpty() == false) goto L7;
        return (T[]) CollectionsKt.terminateCollectionToArray(0, r6);
    L7:
        if (r6.length >= r5.size()) goto L9;
        Object[] r0 = ArraysKt.arrayOfNulls(r6, r5.size());
    L10:
        Iterator<?> r1 = r5.iterator();
        int r2 = 0;
    L12:
        if (r1.hasNext() == false) goto L15;
        r0[r2] = r1.next();
        r2 = r2 + 1;
        goto L12
    L15:
        return (T[]) CollectionsKt.terminateCollectionToArray(r5.size(), r0);
    L9:
        r0 = r6;
        goto L10
    }

    private static final <T> boolean containsAll(Collection<? extends T> r1, Collection<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        return r1.containsAll(r2);
    }

    public static final <T> List<T> emptyList() {
        return EmptyList.INSTANCE;
    }

    public static final IntRange getIndices(Collection<?> r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        return new IntRange(0, r3.size() - 1);
    }

    public static final <T> int getLastIndex(List<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.size() - 1;
    }

    /* JADX WARN: Incorrect types in method signature: <C::Ljava/util/Collection<*>;:TR;R:Ljava/lang/Object;>(TC;Lkotlin/jvm/functions/Function0<+TR;>;)TR; */
    private static final Object ifEmpty(Collection r1, Function0 r2) {
        Intrinsics.checkNotNullParameter(r2, "defaultValue");
        if (r1.isEmpty() == false) goto L6;
        return r2.invoke();
    L6:
        return r1;
    }

    private static final <T> boolean isNotEmpty(Collection<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return !r1.isEmpty();
    }

    private static final <T> boolean isNullOrEmpty(Collection<? extends T> r1) {
        if (r1 != null) goto L4;
    L7:
        return true;
    L4:
        if (r1.isEmpty() == true) goto L7;
        return false;
    }

    private static final <T> List<T> listOf() {
        return CollectionsKt.emptyList();
    }

    public static final <T> List<T> listOf(T... r1) {
        Intrinsics.checkNotNullParameter(r1, "elements");
        if (r1.length <= 0) goto L6;
        return ArraysKt.asList(r1);
    L6:
        return CollectionsKt.emptyList();
    }

    public static final <T> List<T> listOfNotNull(T r1) {
        if (r1 == null) goto L5;
        return CollectionsKt.listOf(r1);
    L5:
        return CollectionsKt.emptyList();
    }

    public static final <T> List<T> listOfNotNull(T... r1) {
        Intrinsics.checkNotNullParameter(r1, "elements");
        return ArraysKt.filterNotNull(r1);
    }

    private static final <T> List<T> mutableListOf() {
        return new ArrayList();
    }

    public static final <T> List<T> mutableListOf(T... r3) {
        Intrinsics.checkNotNullParameter(r3, "elements");
        if (r3.length != 0) goto L5;
        ArrayList r0 = new ArrayList();
    L7:
        return r0;
    L5:
        r0 = new ArrayList(new ArrayAsCollection(r3, true));
        goto L7
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> List<T> optimizeReadOnlyList(List<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        switch(r1.size()) {
            case 0: goto L7;
            case 1: goto L9;
            default: goto L8;
        };
    L8:
        return r1;
    L9:
        return CollectionsKt.listOf(r1.get(0));
    L7:
        return CollectionsKt.emptyList();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> Collection<T> orEmpty(Collection<? extends T> r1) {
        if (r1 != 0) goto L5;
        return CollectionsKt.emptyList();
    L5:
        return r1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> List<T> orEmpty(List<? extends T> r1) {
        if (r1 != 0) goto L5;
        return CollectionsKt.emptyList();
    L5:
        return r1;
    }

    private static final void rangeCheck$CollectionsKt__CollectionsKt(int r4, int r5, int r6) {
        if (r5 > r6) goto L12;
        if (r5 < 0) goto L10;
        if (r6 > r4) goto L8;
        return;
    L8:
        throw new IndexOutOfBoundsException("toIndex (" + r6 + ") is greater than size (" + r4 + ").");
    L10:
        throw new IndexOutOfBoundsException("fromIndex (" + r5 + ") is less than zero.");
    L12:
        throw new IllegalArgumentException("fromIndex (" + r5 + ") is greater than toIndex (" + r6 + ").");
    }

    public static final <T> List<T> shuffled(Iterable<? extends T> r3, Random r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "random");
        List<T> r0 = CollectionsKt.toMutableList(r3);
        CollectionsKt.shuffle(r0, r4);
        return r0;
    }

    public static final void throwCountOverflow() {
        throw new ArithmeticException("Count overflow has happened.");
    }

    public static final void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}
