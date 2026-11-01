package androidx.media3.common.util;

import android.os.Bundle;
import android.util.SparseArray;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class BundleCollectionUtil {
    private BundleCollectionUtil() {
    }

    public static HashMap<String, String> bundleToStringHashMap(Bundle r4) {
        HashMap<String, String> r0 = new HashMap();
        if (r4 != Bundle.EMPTY) goto L5;
        return r0;
    L5:
        Iterator<String> r1 = r4.keySet().iterator();
    L7:
        if (r1.hasNext() == false) goto L12;
        String r2 = r1.next();
        String r3 = r4.getString(r2);
        if (r3 == null) goto L7;
        r0.put(r2, r3);
        goto L7
    L12:
        return r0;
    }

    public static ImmutableMap<String, String> bundleToStringImmutableMap(Bundle r2) {
        if (r2 != Bundle.EMPTY) goto L7;
        return ImmutableMap.m231of();
    L7:
        return ImmutableMap.copyOf(bundleToStringHashMap(r2));
    }

    public static void ensureClassLoader(Bundle r1) {
        if (r1 == null) goto L5;
        r1.setClassLoader((ClassLoader) Util.castNonNull(BundleCollectionUtil.class.getClassLoader()));
        return;
    }

    public static <T> ImmutableList<T> fromBundleList(Function<Bundle, T> r4, List<Bundle> r5) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        int r1 = 0;
    L4:
        if (r1 >= r5.size()) goto L7;
        r0.add(r4.apply((Bundle) Assertions.checkNotNull(r5.get(r1))));
        r1 = r1 + 1;
        goto L4
    L7:
        return r0.build();
    }

    public static <T> SparseArray<T> fromBundleSparseArray(Function<Bundle, T> r4, SparseArray<Bundle> r5) {
        SparseArray<T> r0 = new SparseArray(r5.size());
        int r1 = 0;
    L4:
        if (r1 >= r5.size()) goto L6;
        r0.put(r5.keyAt(r1), r4.apply(r5.valueAt(r1)));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    public static Bundle getBundleWithDefault(Bundle r2, String r3, Bundle r4) {
        Bundle r0 = r2.getBundle(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        return r4;
    }

    public static ArrayList<Integer> getIntegerArrayListWithDefault(Bundle r2, String r3, ArrayList<Integer> r4) {
        ArrayList<Integer> r0 = r2.getIntegerArrayList(r3);
        if (r0 == null) goto L6;
        return r0;
    L6:
        return r4;
    }

    public static Bundle stringMapToBundle(Map<String, String> r5) {
        Bundle r0 = new Bundle();
        Iterator<Map.Entry<String, String>> r1 = r5.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        Map.Entry<String, String> r2 = r1.next();
        r0.putString(r2.getKey(), r2.getValue());
        goto L4
    L6:
        return r0;
    }

    public static <T> ArrayList<Bundle> toBundleArrayList(Collection<T> r4, Function<T, Bundle> r5) {
        ArrayList<Bundle> r0 = new ArrayList(r4.size());
        Iterator<T> r1 = r4.iterator();
    L4:
        if (r1.hasNext() == false) goto L6;
        r0.add(r5.apply(r1.next()));
        goto L4
    L6:
        return r0;
    }

    public static <T> ImmutableList<Bundle> toBundleList(List<T> r4, Function<T, Bundle> r5) {
        ImmutableList.Builder r0 = ImmutableList.builder();
        int r1 = 0;
    L4:
        if (r1 >= r4.size()) goto L7;
        r0.add(r5.apply(r4.get(r1)));
        r1 = r1 + 1;
        goto L4
    L7:
        return r0.build();
    }

    public static <T> SparseArray<Bundle> toBundleSparseArray(SparseArray<T> r4, Function<T, Bundle> r5) {
        SparseArray<Bundle> r0 = new SparseArray(r4.size());
        int r1 = 0;
    L4:
        if (r1 >= r4.size()) goto L6;
        r0.put(r4.keyAt(r1), r5.apply(r4.valueAt(r1)));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }
}
