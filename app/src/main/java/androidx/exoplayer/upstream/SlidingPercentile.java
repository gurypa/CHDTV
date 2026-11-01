package androidx.media3.exoplayer.upstream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class SlidingPercentile {
    private static final Comparator<Sample> INDEX_COMPARATOR = null;
    private static final int MAX_RECYCLED_SAMPLES = 5;
    private static final int SORT_ORDER_BY_INDEX = 1;
    private static final int SORT_ORDER_BY_VALUE = 0;
    private static final int SORT_ORDER_NONE = -1;
    private static final Comparator<Sample> VALUE_COMPARATOR = null;
    private int currentSortOrder;
    private final int maxWeight;
    private int nextSampleIndex;
    private int recycledSampleCount;
    private final Sample[] recycledSamples;
    private final ArrayList<Sample> samples;
    private int totalWeight;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.upstream.SlidingPercentile$1 */
    static /* synthetic */ class C03721 {
    }

    private static class Sample {
        public int index;
        public float value;
        public int weight;

        private Sample() {
        }

        /* synthetic */ Sample(C03721 r1) {
            this();
        }
    }

    static {
        INDEX_COMPARATOR = new SlidingPercentile$$ExternalSyntheticLambda0();
        VALUE_COMPARATOR = new SlidingPercentile$$ExternalSyntheticLambda1();
    }

    public SlidingPercentile(int r2) {
        this.maxWeight = r2;
        this.recycledSamples = new Sample[5];
        this.samples = new ArrayList();
        this.currentSortOrder = -1;
    }

    private void ensureSortedByIndex() {
        if (this.currentSortOrder == 1) goto L6;
        Collections.sort(this.samples, INDEX_COMPARATOR);
        this.currentSortOrder = 1;
        return;
    }

    private void ensureSortedByValue() {
        if (this.currentSortOrder == 0) goto L6;
        Collections.sort(this.samples, VALUE_COMPARATOR);
        this.currentSortOrder = 0;
        return;
    }

    static /* synthetic */ int lambda$static$0(Sample r2, Sample r3) {
        return r2.index - r3.index;
    }

    static /* synthetic */ int lambda$static$1(Sample r2, Sample r3) {
        return Float.compare(r2.value, r3.value);
    }

    public void addSample(int r7, float r8) {
        ensureSortedByIndex();
        if (this.recycledSampleCount <= 0) goto L5;
        Sample[] r0 = this.recycledSamples;
        int r1 = this.recycledSampleCount - 1;
        this.recycledSampleCount = r1;
        Sample r2 = r0[r1];
    L6:
        int r3 = this.nextSampleIndex;
        this.nextSampleIndex = r3 + 1;
        r2.index = r3;
        r2.weight = r7;
        r2.value = r8;
        this.samples.add(r2);
        this.totalWeight += r7;
    L8:
        if (this.totalWeight <= this.maxWeight) goto L16;
        int r4 = this.totalWeight - this.maxWeight;
        Sample r5 = this.samples.get(0);
        if (r5.weight <= r4) goto L11;
        r5.weight -= r4;
        this.totalWeight -= r4;
        goto L8
    L11:
        this.totalWeight -= r5.weight;
        this.samples.remove(0);
        if (this.recycledSampleCount >= 5) goto L8;
        Sample[] r6 = this.recycledSamples;
        int r9 = this.recycledSampleCount;
        this.recycledSampleCount = r9 + 1;
        r6[r9] = r5;
        goto L8
    L16:
        return;
    L5:
        r2 = new Sample(null);
        goto L6
    }

    public float getPercentile(float r6) {
        ensureSortedByValue();
        float r0 = this.totalWeight * r6;
        int r1 = 0;
        int r2 = 0;
    L4:
        if (r2 >= this.samples.size()) goto L11;
        Sample r3 = this.samples.get(r2);
        r1 = r1 + r3.weight;
        if (r1 >= r0) goto L8;
        r2 = r2 + 1;
        goto L4
    L8:
        return r3.value;
    L11:
        if (this.samples.isEmpty() == false) goto L14;
        return Float.NaN;
    L14:
        return this.samples.get(this.samples.size() - 1).value;
    }

    public void reset() {
        this.samples.clear();
        this.currentSortOrder = -1;
        this.nextSampleIndex = 0;
        this.totalWeight = 0;
    }
}
