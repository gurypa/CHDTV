package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Arrays.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a5\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u00032\u0010\u0010\u0004\u001a\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001¢\u0006\u0004\b\u0005\u0010\u0006\u001a#\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001¢\u0006\u0004\b\t\u0010\n\u001a?\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00030\u0011H\u0002¢\u0006\u0004\b\u0012\u0010\u0013\u001a+\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003¢\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0002H\u0018\"\u0010\b\u0000\u0010\u0019*\u0006\u0012\u0002\b\u00030\u0003*\u0002H\u0018\"\u0004\b\u0001\u0010\u0018*\u0002H\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a)\u0010\u001d\u001a\u00020\u0001*\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000¢\u0006\u0002\u0010\u001e\u001aG\u0010\u001f\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u00150 \"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0018*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00180 0\u0003¢\u0006\u0002\u0010!\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\""}, m421d2 = {"contentDeepEqualsImpl", "", ExifInterface.GPS_DIRECTION_TRUE, "", "other", "contentDeepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepToStringImpl", "", "contentDeepToString", "([Ljava/lang/Object;)Ljava/lang/String;", "contentDeepToStringInternal", "", "result", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "processed", "", "contentDeepToStringInternal$ArraysKt__ArraysKt", "([Ljava/lang/Object;Ljava/lang/StringBuilder;Ljava/util/List;)V", "flatten", "", "([[Ljava/lang/Object;)Ljava/util/List;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNullOrEmpty", "([Ljava/lang/Object;)Z", "unzip", "Lkotlin/Pair;", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/collections/ArraysKt")
class ArraysKt__ArraysKt extends ArraysKt__ArraysJVMKt {
    public ArraysKt__ArraysKt() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> boolean contentDeepEquals(T[] r8, T[] r9) {
        if (r8 != r9) goto L6;
        return true;
    L6:
        if (r8 == 0) goto L116;
        if (r9 == 0) goto L116;
        if (r8.length != r9.length) goto L116;
        int r2 = 0;
        int r3 = r8.length;
    L12:
        if (r2 >= r3) goto L115;
        Object[] r4 = r8[r2];
        Object[] r5 = r9[r2];
        if (r4 == r5) goto L113;
        if (r4 == 0) goto L114;
        if (r5 == 0) goto L114;
        if ((r4 instanceof Object[]) == false) goto L27;
        if ((r5 instanceof Object[]) == false) goto L27;
        if (ArraysKt.contentDeepEquals(r4, r5) == true) goto L113;
        return false;
    L27:
        if ((r4 instanceof byte[]) == false) goto L34;
        if ((r5 instanceof byte[]) == false) goto L34;
        if (Arrays.equals((byte[]) r4, (byte[]) r5) == true) goto L113;
        return false;
    L34:
        if ((r4 instanceof short[]) == false) goto L41;
        if ((r5 instanceof short[]) == false) goto L41;
        if (Arrays.equals((short[]) r4, (short[]) r5) == true) goto L113;
        return false;
    L41:
        if ((r4 instanceof int[]) == false) goto L48;
        if ((r5 instanceof int[]) == false) goto L48;
        if (Arrays.equals((int[]) r4, (int[]) r5) == true) goto L113;
        return false;
    L48:
        if ((r4 instanceof long[]) == false) goto L55;
        if ((r5 instanceof long[]) == false) goto L55;
        if (Arrays.equals((long[]) r4, (long[]) r5) == true) goto L113;
        return false;
    L55:
        if ((r4 instanceof float[]) == false) goto L62;
        if ((r5 instanceof float[]) == false) goto L62;
        if (Arrays.equals((float[]) r4, (float[]) r5) == true) goto L113;
        return false;
    L62:
        if ((r4 instanceof double[]) == false) goto L69;
        if ((r5 instanceof double[]) == false) goto L69;
        if (Arrays.equals((double[]) r4, (double[]) r5) == true) goto L113;
        return false;
    L69:
        if ((r4 instanceof char[]) == false) goto L76;
        if ((r5 instanceof char[]) == false) goto L76;
        if (Arrays.equals((char[]) r4, (char[]) r5) == true) goto L113;
        return false;
    L76:
        if ((r4 instanceof boolean[]) == false) goto L83;
        if ((r5 instanceof boolean[]) == false) goto L83;
        if (Arrays.equals((boolean[]) r4, (boolean[]) r5) == true) goto L113;
        return false;
    L83:
        if ((r4 instanceof UByteArray) == false) goto L90;
        if ((r5 instanceof UByteArray) == false) goto L90;
        if (UArraysKt.m1389contentEqualskV0jMPg(((UByteArray) r4).m904unboximpl(), ((UByteArray) r5).m904unboximpl()) == true) goto L113;
        return false;
    L90:
        if ((r4 instanceof UShortArray) == false) goto L97;
        if ((r5 instanceof UShortArray) == false) goto L97;
        if (UArraysKt.m1387contentEqualsFGO6Aew(((UShortArray) r4).m1167unboximpl(), ((UShortArray) r5).m1167unboximpl()) == true) goto L113;
        return false;
    L97:
        if ((r4 instanceof UIntArray) == false) goto L104;
        if ((r5 instanceof UIntArray) == false) goto L104;
        if (UArraysKt.m1388contentEqualsKJPZfPQ(((UIntArray) r4).m983unboximpl(), ((UIntArray) r5).m983unboximpl()) == true) goto L113;
        return false;
    L104:
        if ((r4 instanceof ULongArray) == false) goto L111;
        if ((r5 instanceof ULongArray) == false) goto L111;
        if (UArraysKt.m1390contentEqualslec5QzE(((ULongArray) r4).m1062unboximpl(), ((ULongArray) r5).m1062unboximpl()) == true) goto L113;
        return false;
    L111:
        if (Intrinsics.areEqual(r4, r5) == true) goto L113;
        return false;
    L114:
        return false;
    L113:
        r2 = r2 + 1;
        goto L12
    L115:
        return true;
    L116:
        return false;
    }

    public static final <T> String contentDeepToString(T[] r5) {
        if (r5 != null) goto L5;
        return "null";
    L5:
        StringBuilder r1 = new StringBuilder((RangesKt.coerceAtMost(r5.length, 429496729) * 5) + 2);
        contentDeepToStringInternal$ArraysKt__ArraysKt(r5, r1, new ArrayList());
        String r2 = r1.toString();
        Intrinsics.checkNotNullExpressionValue(r2, "toString(...)");
        return r2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> void contentDeepToStringInternal$ArraysKt__ArraysKt(T[] r5, StringBuilder r6, List<Object[]> r7) {
        if (r7.contains(r5) == false) goto L6;
        r6.append("[...]");
        return;
    L6:
        r7.add(r5);
        r6.append('[');
        int r0 = 0;
        int r1 = r5.length;
    L7:
        if (r0 >= r1) goto L66;
        if (r0 == 0) goto L10;
        r6.append(", ");
    L10:
        Object[] r2 = r5[r0];
        if (r2 != 0) goto L14;
        r6.append("null");
    L65:
        r0 = r0 + 1;
        goto L7
    L14:
        if ((r2 instanceof Object[]) == false) goto L17;
        contentDeepToStringInternal$ArraysKt__ArraysKt(r2, r6, r7);
        goto L65
    L17:
        if ((r2 instanceof byte[]) == false) goto L20;
        String r3 = Arrays.toString((byte[]) r2);
        Intrinsics.checkNotNullExpressionValue(r3, "toString(...)");
        r6.append(r3);
        goto L65
    L20:
        if ((r2 instanceof short[]) == false) goto L23;
        String r4 = Arrays.toString((short[]) r2);
        Intrinsics.checkNotNullExpressionValue(r4, "toString(...)");
        r6.append(r4);
        goto L65
    L23:
        if ((r2 instanceof int[]) == false) goto L26;
        String r8 = Arrays.toString((int[]) r2);
        Intrinsics.checkNotNullExpressionValue(r8, "toString(...)");
        r6.append(r8);
        goto L65
    L26:
        if ((r2 instanceof long[]) == false) goto L29;
        String r9 = Arrays.toString((long[]) r2);
        Intrinsics.checkNotNullExpressionValue(r9, "toString(...)");
        r6.append(r9);
        goto L65
    L29:
        if ((r2 instanceof float[]) == false) goto L32;
        String r10 = Arrays.toString((float[]) r2);
        Intrinsics.checkNotNullExpressionValue(r10, "toString(...)");
        r6.append(r10);
        goto L65
    L32:
        if ((r2 instanceof double[]) == false) goto L35;
        String r11 = Arrays.toString((double[]) r2);
        Intrinsics.checkNotNullExpressionValue(r11, "toString(...)");
        r6.append(r11);
        goto L65
    L35:
        if ((r2 instanceof char[]) == false) goto L38;
        String r12 = Arrays.toString((char[]) r2);
        Intrinsics.checkNotNullExpressionValue(r12, "toString(...)");
        r6.append(r12);
        goto L65
    L38:
        if ((r2 instanceof boolean[]) == false) goto L40;
        String r13 = Arrays.toString((boolean[]) r2);
        Intrinsics.checkNotNullExpressionValue(r13, "toString(...)");
        r6.append(r13);
        goto L65
    L40:
        long[] r14 = null;
        byte[] r15 = null;
        short[] r16 = null;
        int[] r17 = null;
        if ((r2 instanceof UByteArray) == false) goto L47;
        UByteArray r18 = (UByteArray) r2;
        if (r18 == null) goto L45;
        r15 = r18.m904unboximpl();
    L45:
        r6.append(UArraysKt.m1395contentToString2csIQuQ(r15));
        goto L65
    L47:
        if ((r2 instanceof UShortArray) == false) goto L53;
        UShortArray r19 = (UShortArray) r2;
        if (r19 == null) goto L51;
        r16 = r19.m1167unboximpl();
    L51:
        r6.append(UArraysKt.m1397contentToStringd6D3K8(r16));
        goto L65
    L53:
        if ((r2 instanceof UIntArray) == false) goto L59;
        UIntArray r20 = (UIntArray) r2;
        if (r20 == null) goto L57;
        r17 = r20.m983unboximpl();
    L57:
        r6.append(UArraysKt.m1396contentToStringXUkPCBk(r17));
        goto L65
    L59:
        if ((r2 instanceof ULongArray) == false) goto L64;
        ULongArray r21 = (ULongArray) r2;
        if (r21 == null) goto L63;
        r14 = r21.m1062unboximpl();
    L63:
        r6.append(UArraysKt.m1398contentToStringuLth9ew(r14));
        goto L65
    L64:
        r6.append(r2.toString());
        goto L65
    L66:
        r6.append(']');
        r7.remove(CollectionsKt.getLastIndex(r7));
    }

    public static final <T> List<T> flatten(T[][] r7) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        T[][] r0 = r7;
        int r1 = r0.length;
        int r2 = 0;
        int r3 = 0;
        int r4 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r4 = r4 + r0[r3].length;
        r3 = r3 + 1;
        goto L3
    L5:
        ArrayList r5 = new ArrayList(r4);
        int r6 = r7.length;
    L6:
        if (r2 >= r6) goto L9;
        CollectionsKt.addAll(r5, r7[r2]);
        r2 = r2 + 1;
        goto L6
    L9:
        return r5;
    }

    /* JADX WARN: Incorrect types in method signature: <C:[Ljava/lang/Object;:TR;R:Ljava/lang/Object;>(TC;Lkotlin/jvm/functions/Function0<+TR;>;)TR; */
    private static final Object ifEmpty(Object[] r1, Function0 r2) {
        Intrinsics.checkNotNullParameter(r2, "defaultValue");
        if (r1.length != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return r2.invoke();
    L9:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    private static final boolean isNullOrEmpty(Object[] r3) {
        if (r3 != null) goto L5;
        return true;
    L5:
        if (r3.length != 0) goto L7;
        boolean r1 = true;
    L8:
        if (r1 == false) goto L11;
        return true;
    L11:
        return false;
    L7:
        r1 = false;
        goto L8
    }

    public static final <T, R> Pair<List<T>, List<R>> unzip(Pair<? extends T, ? extends R>[] r6) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        ArrayList r0 = new ArrayList(r6.length);
        ArrayList r1 = new ArrayList(r6.length);
        int r2 = r6.length;
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L6;
        Pair<? extends T, ? extends R> r4 = r6[r3];
        r0.add(r4.getFirst());
        r1.add(r4.getSecond());
        r3 = r3 + 1;
        goto L3
    L6:
        return TuplesKt.m428to(r0, r1);
    }
}
