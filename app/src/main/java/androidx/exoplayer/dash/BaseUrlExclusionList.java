package androidx.media3.exoplayer.dash;

import android.os.SystemClock;
import android.util.Pair;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.dash.manifest.BaseUrl;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public final class BaseUrlExclusionList {
    private final Map<Integer, Long> excludedPriorities;
    private final Map<String, Long> excludedServiceLocations;
    private final Random random;
    private final Map<List<Pair<String, Integer>>, BaseUrl> selectionsTaken;

    public static /* synthetic */ int $r8$lambda$B0M_VULuMho33wL69NUZWXpBNM8(BaseUrl r0, BaseUrl r1) {
        return compareBaseUrl(r0, r1);
    }

    public BaseUrlExclusionList() {
        this(new Random());
    }

    BaseUrlExclusionList(Random r2) {
        this.selectionsTaken = new HashMap();
        this.random = r2;
        this.excludedServiceLocations = new HashMap();
        this.excludedPriorities = new HashMap();
    }

    private static <T> void addExclusion(T r2, long r3, Map<T, Long> r5) {
        if (r5.containsKey(r2) == false) goto L5;
        r3 = Math.max(r3, ((Long) Util.castNonNull(r5.get(r2))).longValue());
    L5:
        r5.put(r2, Long.valueOf(r3));
    }

    private List<BaseUrl> applyExclusions(List<BaseUrl> r8) {
        long r0 = SystemClock.elapsedRealtime();
        removeExpiredExclusions(r0, this.excludedServiceLocations);
        removeExpiredExclusions(r0, this.excludedPriorities);
        ArrayList r2 = new ArrayList();
        int r3 = 0;
    L4:
        if (r3 >= r8.size()) goto L11;
        BaseUrl r4 = r8.get(r3);
        if (this.excludedServiceLocations.containsKey(r4.serviceLocation) == true) goto L10;
        if (this.excludedPriorities.containsKey(Integer.valueOf(r4.priority)) == true) goto L10;
        r2.add(r4);
    L10:
        r3 = r3 + 1;
        goto L4
    L11:
        return r2;
    }

    private static int compareBaseUrl(BaseUrl r3, BaseUrl r4) {
        int r0 = Integer.compare(r3.priority, r4.priority);
        if (r0 == 0) goto L6;
        return r0;
    L6:
        return r3.serviceLocation.compareTo(r4.serviceLocation);
    }

    public static int getPriorityCount(List<BaseUrl> r3) {
        HashSet r0 = new HashSet();
        int r1 = 0;
    L4:
        if (r1 >= r3.size()) goto L7;
        r0.add(Integer.valueOf(r3.get(r1).priority));
        r1 = r1 + 1;
        goto L4
    L7:
        return r0.size();
    }

    private static <T> void removeExpiredExclusions(long r5, Map<T, Long> r7) {
        ArrayList r0 = new ArrayList();
        Iterator<Map.Entry<T, Long>> r1 = r7.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        Map.Entry<T, Long> r2 = r1.next();
        if (r2.getValue().longValue() > r5) goto L4;
        r0.add(r2.getKey());
        goto L4
    L9:
        int r3 = 0;
    L11:
        if (r3 >= r0.size()) goto L13;
        r7.remove(r0.get(r3));
        r3 = r3 + 1;
        goto L11
    }

    private BaseUrl selectWeighted(List<BaseUrl> r6) {
        int r0 = 0;
        int r1 = 0;
    L4:
        if (r1 >= r6.size()) goto L6;
        r0 = r0 + r6.get(r1).weight;
        r1 = r1 + 1;
        goto L4
    L6:
        int r2 = this.random.nextInt(r0);
        int r3 = 0;
        int r4 = 0;
    L8:
        if (r4 >= r6.size()) goto L14;
        BaseUrl r5 = r6.get(r4);
        r3 = r3 + r5.weight;
        if (r2 < r3) goto L11;
        r4 = r4 + 1;
        goto L8
    L11:
        return r5;
    L14:
        return (BaseUrl) Iterables.getLast(r6);
    }

    public void exclude(BaseUrl r5, long r6) {
        long r0 = SystemClock.elapsedRealtime() + r6;
        addExclusion(r5.serviceLocation, r0, this.excludedServiceLocations);
        if (r5.priority == Integer.MIN_VALUE) goto L6;
        addExclusion(Integer.valueOf(r5.priority), r0, this.excludedPriorities);
        return;
    }

    public int getPriorityCountAfterExclusion(List<BaseUrl> r5) {
        HashSet r0 = new HashSet();
        List<BaseUrl> r1 = applyExclusions(r5);
        int r2 = 0;
    L4:
        if (r2 >= r1.size()) goto L7;
        r0.add(Integer.valueOf(r1.get(r2).priority));
        r2 = r2 + 1;
        goto L4
    L7:
        return r0.size();
    }

    public void reset() {
        this.excludedServiceLocations.clear();
        this.excludedPriorities.clear();
        this.selectionsTaken.clear();
    }

    public BaseUrl selectBaseUrl(List<BaseUrl> r10) {
        List<BaseUrl> r0 = applyExclusions(r10);
        if (r0.size() < 2) goto L5;
        Collections.sort(r0, new BaseUrlExclusionList$$ExternalSyntheticLambda0());
        ArrayList r1 = new ArrayList();
        int r3 = r0.get(0).priority;
        int r4 = 0;
    L8:
        if (r4 >= r0.size()) goto L16;
        BaseUrl r5 = r0.get(r4);
        if (r3 != r5.priority) goto L12;
        r1.add(new Pair(r5.serviceLocation, Integer.valueOf(r5.weight)));
        r4 = r4 + 1;
        goto L8
    L12:
        if (r1.size() != 1) goto L16;
        return r0.get(0);
    L16:
        BaseUrl r6 = this.selectionsTaken.get(r1);
        if (r6 != null) goto L22;
        BaseUrl r7 = selectWeighted(r0.subList(0, r1.size()));
        this.selectionsTaken.put(r1, r7);
        return r7;
    L22:
        return r6;
    L5:
        return (BaseUrl) Iterables.getFirst(r0, null);
    }
}
