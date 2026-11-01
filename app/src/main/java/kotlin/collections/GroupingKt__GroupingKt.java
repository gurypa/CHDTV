package kotlin.collections;

import android.R;
import androidx.exifinterface.media.ExifInterface;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Grouping.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000@\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u001a\u009e\u0001\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\bø\u0001\u0000\u001a·\u0001\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aI\u0010\u0014\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0016\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00150\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u0010H\u0007¢\u0006\u0002\u0010\u0016\u001a¿\u0001\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u000526\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\bø\u0001\u0000\u001a\u007f\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001aØ\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u001026\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a\u0093\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001f\u001a\u008b\u0001\u0010 \u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0001\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\bø\u0001\u0000\u001a¤\u0001\u0010\"\u001a\u0002H\u0010\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010#\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006$"}, m421d2 = {"aggregate", "", "K", "R", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/collections/Grouping;", "operation", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "key", "accumulator", "element", "", "first", "aggregateTo", "M", "", "destination", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function4;)Ljava/util/Map;", "eachCountTo", "", "(Lkotlin/collections/Grouping;Ljava/util/Map;)Ljava/util/Map;", "fold", "initialValueSelector", "Lkotlin/Function2;", "Lkotlin/Function3;", "initialValue", "(Lkotlin/collections/Grouping;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "foldTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "(Lkotlin/collections/Grouping;Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "reduce", ExifInterface.LATITUDE_SOUTH, "reduceTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/GroupingKt")
class GroupingKt__GroupingKt extends GroupingKt__GroupingJVMKt {
    public GroupingKt__GroupingKt() {
    }

    /* JADX WARN: Type inference failed for: r5v1, types: [java.lang.Object] */
    public static final <T, K, R> Map<K, R> aggregate(Grouping<T, ? extends K> r9, Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> r10) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        Intrinsics.checkNotNullParameter(r10, "operation");
        LinkedHashMap r1 = new LinkedHashMap();
        Iterator<T> r4 = r9.sourceIterator();
    L4:
        if (r4.hasNext() == false) goto L13;
        ?? r5 = r4.next();
        Object r6 = r9.keyOf(r5);
        R.bool r7 = (Object) r1.get(r6);
        if (r7 == null) goto L8;
    L10:
        boolean r8 = false;
    L11:
        r1.put(r6, r10.invoke(r6, r7, r5, Boolean.valueOf(r8)));
        goto L4
    L8:
        if (r1.containsKey(r6) == true) goto L10;
        r8 = true;
        goto L11
    L13:
        return r1;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.Object] */
    public static final <T, K, R, M extends Map<? super K, R>> M aggregateTo(Grouping<T, ? extends K> r6, M r7, Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> r8) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "destination");
        Intrinsics.checkNotNullParameter(r8, "operation");
        Iterator<T> r1 = r6.sourceIterator();
    L4:
        if (r1.hasNext() == false) goto L12;
        ?? r2 = r1.next();
        Object r3 = r6.keyOf(r2);
        R.bool r4 = (Object) r7.get(r3);
        if (r4 == null) goto L8;
    L10:
        boolean r5 = false;
    L11:
        r7.put(r3, r8.invoke(r3, r4, r2, Boolean.valueOf(r5)));
        goto L4
    L8:
        if (r7.containsKey(r3) == true) goto L10;
        r5 = true;
        goto L11
    L12:
        return r7;
    }

    public static final <T, K, M extends Map<? super K, Integer>> M eachCountTo(Grouping<T, ? extends K> r19, M r20) {
        Intrinsics.checkNotNullParameter(r19, "<this>");
        Intrinsics.checkNotNullParameter(r20, "destination");
        Iterator<T> r8 = r19.sourceIterator();
    L4:
        if (r8.hasNext() == false) goto L17;
        K r10 = r19.keyOf(r8.next());
        Object r11 = r20.get(r10);
        if (r11 == null) goto L8;
    L10:
        boolean r13 = false;
    L12:
        if (r13 == false) goto L14;
        Object r17 = 0;
    L15:
        r20.put(r10, Integer.valueOf(((Number) r17).intValue() + 1));
        goto L4
    L14:
        r17 = r11;
        goto L15
    L8:
        if (r20.containsKey(r10) == true) goto L10;
        r13 = true;
        goto L12
    L17:
        return r20;
    }

    public static final <T, K, R> Map<K, R> fold(Grouping<T, ? extends K> r17, R r18, Function2<? super R, ? super T, ? extends R> r19) {
        Intrinsics.checkNotNullParameter(r17, "<this>");
        Intrinsics.checkNotNullParameter(r19, "operation");
        boolean r1 = false;
        LinkedHashMap r5 = new LinkedHashMap();
        Iterator<T> r8 = r17.sourceIterator();
    L4:
        if (r8.hasNext() == false) goto L17;
        T r9 = r8.next();
        K r10 = r17.keyOf(r9);
        Object r11 = r5.get(r10);
        if (r11 == null) goto L8;
    L10:
        boolean r12 = false;
    L11:
        boolean r16 = r1;
        if (r12 == false) goto L14;
        Object r2 = r18;
    L15:
        r5.put(r10, r19.invoke(r2, r9));
        r1 = r16;
        goto L4
    L14:
        r2 = r11;
        goto L15
    L8:
        if (r5.containsKey(r10) == true) goto L10;
        r12 = true;
        goto L11
    L17:
        return r5;
    }

    public static final <T, K, R> Map<K, R> fold(Grouping<T, ? extends K> r19, Function2<? super K, ? super T, ? extends R> r20, Function3<? super K, ? super R, ? super T, ? extends R> r21) {
        Function2<? super K, ? super T, ? extends R> r0 = r20;
        Intrinsics.checkNotNullParameter(r19, "<this>");
        Intrinsics.checkNotNullParameter(r0, "initialValueSelector");
        Intrinsics.checkNotNullParameter(r21, "operation");
        boolean r2 = false;
        LinkedHashMap r6 = new LinkedHashMap();
        Iterator<T> r9 = r19.sourceIterator();
    L4:
        if (r9.hasNext() == false) goto L17;
        T r10 = r9.next();
        K r11 = r19.keyOf(r10);
        Object r12 = r6.get(r11);
        if (r12 == null) goto L8;
    L10:
        boolean r13 = false;
    L12:
        if (r13 == false) goto L14;
        Object r3 = r11;
        Object r1 = r0.invoke(r3, r10);
    L15:
        r6.put(r11, r21.invoke(r3, r1, r10));
        r0 = r20;
        r2 = r2;
        goto L4
    L14:
        r3 = r11;
        r1 = r12;
        goto L15
    L8:
        if (r6.containsKey(r11) == true) goto L10;
        r13 = true;
        goto L12
    L17:
        return r6;
    }

    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(Grouping<T, ? extends K> r12, M r13, R r14, Function2<? super R, ? super T, ? extends R> r15) {
        Intrinsics.checkNotNullParameter(r12, "<this>");
        Intrinsics.checkNotNullParameter(r13, "destination");
        Intrinsics.checkNotNullParameter(r15, "operation");
        Iterator<T> r3 = r12.sourceIterator();
    L4:
        if (r3.hasNext() == false) goto L17;
        T r4 = r3.next();
        K r5 = r12.keyOf(r4);
        Object r6 = r13.get(r5);
        if (r6 == null) goto L8;
    L10:
        boolean r7 = false;
    L12:
        if (r7 == false) goto L14;
        Object r11 = r14;
    L15:
        r13.put(r5, r15.invoke(r11, r4));
        goto L4
    L14:
        r11 = r6;
        goto L15
    L8:
        if (r13.containsKey(r5) == true) goto L10;
        r7 = true;
        goto L12
    L17:
        return r13;
    }

    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(Grouping<T, ? extends K> r17, M r18, Function2<? super K, ? super T, ? extends R> r19, Function3<? super K, ? super R, ? super T, ? extends R> r20) {
        Function2<? super K, ? super T, ? extends R> r1 = r19;
        Intrinsics.checkNotNullParameter(r17, "<this>");
        Intrinsics.checkNotNullParameter(r18, "destination");
        Intrinsics.checkNotNullParameter(r1, "initialValueSelector");
        Intrinsics.checkNotNullParameter(r20, "operation");
        Iterator<T> r7 = r17.sourceIterator();
    L4:
        if (r7.hasNext() == false) goto L17;
        T r8 = r7.next();
        Object r9 = r17.keyOf(r8);
        Object r10 = r18.get(r9);
        if (r10 == null) goto L8;
    L10:
        boolean r11 = false;
    L12:
        if (r11 == false) goto L14;
        Object r2 = r1.invoke(r9, r8);
    L15:
        r18.put(r9, r20.invoke(r9, r2, r8));
        r1 = r19;
        goto L4
    L14:
        r2 = r10;
        goto L15
    L8:
        if (r18.containsKey(r9) == true) goto L10;
        r11 = true;
        goto L12
    L17:
        return r18;
    }

    public static final <S, T extends S, K> Map<K, S> reduce(Grouping<T, ? extends K> r18, Function3<? super K, ? super S, ? super T, ? extends S> r19) {
        Intrinsics.checkNotNullParameter(r18, "<this>");
        Intrinsics.checkNotNullParameter(r19, "operation");
        LinkedHashMap r5 = new LinkedHashMap();
        Iterator<T> r8 = r18.sourceIterator();
    L4:
        if (r8.hasNext() == false) goto L17;
        T r9 = r8.next();
        Object r10 = r18.keyOf(r9);
        R.bool r11 = (Object) r5.get(r10);
        if (r11 == null) goto L8;
    L10:
        boolean r12 = false;
    L11:
        Object r15 = (Object) r9;
        if (r12 == true) goto L15;
        r15 = (Object) r19.invoke(r10, r11, r15);
    L15:
        r5.put(r10, r15);
        goto L4
    L8:
        if (r5.containsKey(r10) == true) goto L10;
        r12 = true;
        goto L11
    L17:
        return r5;
    }

    public static final <S, T extends S, K, M extends Map<? super K, S>> M reduceTo(Grouping<T, ? extends K> r13, M r14, Function3<? super K, ? super S, ? super T, ? extends S> r15) {
        Intrinsics.checkNotNullParameter(r13, "<this>");
        Intrinsics.checkNotNullParameter(r14, "destination");
        Intrinsics.checkNotNullParameter(r15, "operation");
        Iterator<T> r3 = r13.sourceIterator();
    L4:
        if (r3.hasNext() == false) goto L17;
        T r4 = r3.next();
        Object r5 = r13.keyOf(r4);
        R.bool r6 = (Object) r14.get(r5);
        if (r6 == null) goto L8;
    L10:
        boolean r7 = false;
    L11:
        Object r10 = (Object) r4;
        if (r7 == true) goto L15;
        r10 = (Object) r15.invoke(r5, r6, r10);
    L15:
        r14.put(r5, r10);
        goto L4
    L8:
        if (r14.containsKey(r5) == true) goto L10;
        r7 = true;
        goto L11
    L17:
        return r14;
    }
}
