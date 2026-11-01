package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: MutableCollections.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000P\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0000\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0010\u001a9\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0010\u001a(\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0015\u001a.\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0016\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0015\u001a.\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0016\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0017\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0018\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0014\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u001a\u001a&\u0010\u0018\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cH\u0087\b¢\u0006\u0002\u0010\u001d\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u001d\u0010\u001f\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001f\u0010!\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001d\u0010\"\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a\u001f\u0010#\u001a\u0004\u0018\u0001H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0011H\u0007¢\u0006\u0002\u0010 \u001a-\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010$\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0019*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH\u0087\b\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a*\u0010$\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00112\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u000e\u001a\u0015\u0010%\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b&¨\u0006'"}, m421d2 = {"addAll", "", ExifInterface.GPS_DIRECTION_TRUE, "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "convertToListIfNotCollection", "", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", StreamInformation.KEY_INDEX, "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "removeFirst", "(Ljava/util/List;)Ljava/lang/Object;", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt {
    public CollectionsKt__MutableCollectionsKt() {
    }

    public static final <T> boolean addAll(Collection<? super T> r4, Iterable<? extends T> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "elements");
        if ((r5 instanceof Collection) == true) goto L5;
        boolean r0 = false;
        Iterator<? extends T> r1 = r5.iterator();
    L8:
        if (r1.hasNext() == false) goto L12;
        if (r4.add(r1.next()) == false) goto L8;
        r0 = true;
        goto L8
    L12:
        return r0;
    L5:
        return r4.addAll((Collection) r5);
    }

    public static final <T> boolean addAll(Collection<? super T> r4, Sequence<? extends T> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "elements");
        boolean r0 = false;
        Iterator<? extends T> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L8;
        if (r4.add(r1.next()) == false) goto L4;
        r0 = true;
        goto L4
    L8:
        return r0;
    }

    public static final <T> boolean addAll(Collection<? super T> r1, T[] r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        return r1.addAll(ArraysKt.asList(r2));
    }

    public static final <T> Collection<T> convertToListIfNotCollection(Iterable<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof Collection) == false) goto L6;
        return (Collection) r1;
    L6:
        return CollectionsKt.toList(r1);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(Iterable<? extends T> r4, Function1<? super T, Boolean> r5, boolean r6) {
        boolean r0 = false;
        Iterator<? extends T> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        if (r5.invoke(r1.next()).booleanValue() != r6) goto L4;
        r1.remove();
        r0 = true;
        goto L4
    L9:
        return r0;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(List<T> r6, Function1<? super T, Boolean> r7, boolean r8) {
        if ((r6 instanceof RandomAccess) == true) goto L6;
        Intrinsics.checkNotNull(r6, "null cannot be cast to non-null type kotlin.collections.MutableIterable<T of kotlin.collections.CollectionsKt__MutableCollectionsKt.filterInPlace>");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(r6), r7, r8);
    L6:
        int r0 = 0;
        ?? r1 = new IntRange(0, CollectionsKt.getLastIndex(r6)).iterator();
    L8:
        if (r1.hasNext() == false) goto L16;
        int r2 = r1.nextInt();
        T r4 = r6.get(r2);
        if (r7.invoke(r4).booleanValue() == r8) goto L8;
        if (r0 == r2) goto L14;
        r6.set(r0, r4);
    L14:
        r0 = r0 + 1;
        goto L8
    L16:
        if (r0 >= r6.size()) goto L24;
        int r3 = CollectionsKt.getLastIndex(r6);
        if (r0 > r3) goto L22;
    L19:
        r6.remove(r3);
        if (r3 == r0) goto L31;
        r3 = r3 - 1;
        goto L19
    L31:
        return true;
    L22:
        return true;
    L24:
        return false;
    }

    private static final <T> void minusAssign(Collection<? super T> r1, Iterable<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        CollectionsKt.removeAll(r1, r2);
    }

    private static final <T> void minusAssign(Collection<? super T> r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.remove(r2);
    }

    private static final <T> void minusAssign(Collection<? super T> r1, Sequence<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        CollectionsKt.removeAll(r1, r2);
    }

    private static final <T> void minusAssign(Collection<? super T> r1, T[] r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        CollectionsKt.removeAll(r1, r2);
    }

    private static final <T> void plusAssign(Collection<? super T> r1, Iterable<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        CollectionsKt.addAll(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> void plusAssign(Collection<? super T> r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        r1.add(r2);
    }

    private static final <T> void plusAssign(Collection<? super T> r1, Sequence<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        CollectionsKt.addAll(r1, r2);
    }

    private static final <T> void plusAssign(Collection<? super T> r1, T[] r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        CollectionsKt.addAll(r1, r2);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use removeAt(index) instead.", replaceWith = @ReplaceWith(expression = "removeAt(index)", imports = {}))
    private static final <T> T remove(List<T> r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.remove(r2);
    }

    private static final <T> boolean remove(Collection<? extends T> r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return TypeIntrinsics.asMutableCollection(r1).remove(r2);
    }

    public static final <T> boolean removeAll(Iterable<? extends T> r1, Function1<? super T, Boolean> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(r1, r2, true);
    }

    public static final <T> boolean removeAll(Collection<? super T> r1, Iterable<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        return r1.removeAll(CollectionsKt.convertToListIfNotCollection(r2));
    }

    private static final <T> boolean removeAll(Collection<? extends T> r1, Collection<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        return TypeIntrinsics.asMutableCollection(r1).removeAll(r2);
    }

    public static final <T> boolean removeAll(Collection<? super T> r2, Sequence<? extends T> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "elements");
        List r0 = SequencesKt.toList(r3);
        if (r0.isEmpty() == false) goto L5;
    L7:
        return false;
    L5:
        if (r2.removeAll(r0) == false) goto L7;
        return true;
    }

    public static final <T> boolean removeAll(Collection<? super T> r3, T[] r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "elements");
        if (r4.length != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == true) goto L11;
        if (r3.removeAll(ArraysKt.asList(r4)) == false) goto L11;
        return true;
    L11:
        return false;
    L5:
        r0 = false;
        goto L6
    }

    public static final <T> boolean removeAll(List<T> r1, Function1<? super T, Boolean> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(r1, r2, true);
    }

    public static final <T> T removeFirst(List<T> r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isEmpty() == true) goto L7;
        return r2.remove(0);
    L7:
        throw new NoSuchElementException("List is empty.");
    }

    public static final <T> T removeFirstOrNull(List<T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return r1.remove(0);
    }

    public static final <T> T removeLast(List<T> r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isEmpty() == true) goto L7;
        return r2.remove(CollectionsKt.getLastIndex(r2));
    L7:
        throw new NoSuchElementException("List is empty.");
    }

    public static final <T> T removeLastOrNull(List<T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return r1.remove(CollectionsKt.getLastIndex(r1));
    }

    public static final <T> boolean retainAll(Iterable<? extends T> r1, Function1<? super T, Boolean> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(r1, r2, false);
    }

    public static final <T> boolean retainAll(Collection<? super T> r1, Iterable<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        return r1.retainAll(CollectionsKt.convertToListIfNotCollection(r2));
    }

    private static final <T> boolean retainAll(Collection<? extends T> r1, Collection<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        return TypeIntrinsics.asMutableCollection(r1).retainAll(r2);
    }

    public static final <T> boolean retainAll(Collection<? super T> r2, Sequence<? extends T> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "elements");
        List r0 = SequencesKt.toList(r3);
        if (r0.isEmpty() == true) goto L7;
        return r2.retainAll(r0);
    L7:
        return retainNothing$CollectionsKt__MutableCollectionsKt(r2);
    }

    public static final <T> boolean retainAll(Collection<? super T> r1, T[] r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "elements");
        if (r2.length != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == true) goto L10;
        return r1.retainAll(ArraysKt.asList(r2));
    L10:
        return retainNothing$CollectionsKt__MutableCollectionsKt(r1);
    L5:
        r0 = false;
        goto L6
    }

    public static final <T> boolean retainAll(List<T> r1, Function1<? super T, Boolean> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(r1, r2, false);
    }

    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(Collection<?> r1) {
        boolean r0 = !r1.isEmpty();
        r1.clear();
        return r0;
    }
}
