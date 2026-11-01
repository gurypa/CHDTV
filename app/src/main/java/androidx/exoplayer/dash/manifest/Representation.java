package androidx.media3.exoplayer.dash.manifest;

import android.net.Uri;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.exoplayer.dash.DashSegmentIndex;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class Representation {
    public static final long REVISION_ID_DEFAULT = -1;
    public final ImmutableList<BaseUrl> baseUrls;
    public final List<Descriptor> essentialProperties;
    public final Format format;
    public final List<Descriptor> inbandEventStreams;
    private final RangedUri initializationUri;
    public final long presentationTimeOffsetUs;
    public final long revisionId;
    public final List<Descriptor> supplementalProperties;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.dash.manifest.Representation$1 */
    static /* synthetic */ class C02761 {
    }

    public static class MultiSegmentRepresentation extends Representation implements DashSegmentIndex {
        final SegmentBase.MultiSegmentBase segmentBase;

        public MultiSegmentRepresentation(long r11, Format r13, List<BaseUrl> r14, SegmentBase.MultiSegmentBase r15, List<Descriptor> r16, List<Descriptor> r17, List<Descriptor> r18) {
            super(r11, r13, r14, r15, r16, r17, r18, null);
            this.segmentBase = r15;
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getAvailableSegmentCount(long r3, long r5) {
            return this.segmentBase.getAvailableSegmentCount(r3, r5);
        }

        @Override // androidx.media3.exoplayer.dash.manifest.Representation
        public String getCacheKey() {
            return null;
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getDurationUs(long r3, long r5) {
            return this.segmentBase.getSegmentDurationUs(r3, r5);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getFirstAvailableSegmentNum(long r3, long r5) {
            return this.segmentBase.getFirstAvailableSegmentNum(r3, r5);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getFirstSegmentNum() {
            return this.segmentBase.getFirstSegmentNum();
        }

        @Override // androidx.media3.exoplayer.dash.manifest.Representation
        public DashSegmentIndex getIndex() {
            return this;
        }

        @Override // androidx.media3.exoplayer.dash.manifest.Representation
        public RangedUri getIndexUri() {
            return null;
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getNextSegmentAvailableTimeUs(long r3, long r5) {
            return this.segmentBase.getNextSegmentAvailableTimeUs(r3, r5);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getSegmentCount(long r3) {
            return this.segmentBase.getSegmentCount(r3);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getSegmentNum(long r3, long r5) {
            return this.segmentBase.getSegmentNum(r3, r5);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public RangedUri getSegmentUrl(long r2) {
            return this.segmentBase.getSegmentUrl(this, r2);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public long getTimeUs(long r3) {
            return this.segmentBase.getSegmentTimeUs(r3);
        }

        @Override // androidx.media3.exoplayer.dash.DashSegmentIndex
        public boolean isExplicit() {
            return this.segmentBase.isExplicit();
        }
    }

    public static class SingleSegmentRepresentation extends Representation {
        private final String cacheKey;
        public final long contentLength;
        private final RangedUri indexUri;
        private final SingleSegmentIndex segmentIndex;
        public final Uri uri;

        public SingleSegmentRepresentation(long r12, Format r14, List<BaseUrl> r15, SegmentBase.SingleSegmentBase r16, List<Descriptor> r17, List<Descriptor> r18, List<Descriptor> r19, String r20, long r21) {
            super(r12, r14, r15, r16, r17, r18, r19, null);
            this.uri = Uri.parse(r15.get(0).url);
            this.indexUri = r16.getIndex();
            this.cacheKey = r20;
            this.contentLength = r21;
            if (this.indexUri == null) goto L5;
            SingleSegmentIndex r2 = null;
        L6:
            this.segmentIndex = r2;
            return;
        L5:
            r2 = new SingleSegmentIndex(new RangedUri(null, 0, r21));
            goto L6
        }

        public static SingleSegmentRepresentation newInstance(long r22, Format r24, String r25, long r26, long r28, long r30, long r32, List<Descriptor> r34, String r35, long r36) {
            SegmentBase.SingleSegmentBase r8 = new SegmentBase.SingleSegmentBase(new RangedUri(null, r26, (r28 - r26) + 1), 1, 0, r30, (r32 - r30) + 1);
            return new SingleSegmentRepresentation(r22, r24, ImmutableList.m213of(new BaseUrl(r25)), r8, r34, ImmutableList.m212of(), ImmutableList.m212of(), r35, r36);
        }

        @Override // androidx.media3.exoplayer.dash.manifest.Representation
        public String getCacheKey() {
            return this.cacheKey;
        }

        @Override // androidx.media3.exoplayer.dash.manifest.Representation
        public DashSegmentIndex getIndex() {
            return this.segmentIndex;
        }

        @Override // androidx.media3.exoplayer.dash.manifest.Representation
        public RangedUri getIndexUri() {
            return this.indexUri;
        }
    }

    private Representation(long r3, Format r5, List<BaseUrl> r6, SegmentBase r7, List<Descriptor> r8, List<Descriptor> r9, List<Descriptor> r10) {
        Assertions.checkArgument(!r6.isEmpty());
        this.revisionId = r3;
        this.format = r5;
        this.baseUrls = ImmutableList.copyOf(r6);
        if (r8 != null) goto L5;
        List<Descriptor> r0 = Collections.emptyList();
    L6:
        this.inbandEventStreams = r0;
        this.essentialProperties = r9;
        this.supplementalProperties = r10;
        this.initializationUri = r7.getInitialization(this);
        this.presentationTimeOffsetUs = r7.getPresentationTimeOffsetUs();
        return;
    L5:
        r0 = Collections.unmodifiableList(r8);
        goto L6
    }

    /* synthetic */ Representation(long r1, Format r3, List r4, SegmentBase r5, List r6, List r7, List r8, C02761 r9) {
        this(r1, r3, r4, r5, r6, r7, r8);
    }

    public static Representation newInstance(long r9, Format r11, List<BaseUrl> r12, SegmentBase r13) {
        return newInstance(r9, r11, r12, r13, null, ImmutableList.m212of(), ImmutableList.m212of(), null);
    }

    public static Representation newInstance(long r23, Format r25, List<BaseUrl> r26, SegmentBase r27, List<Descriptor> r28, List<Descriptor> r29, List<Descriptor> r30, String r31) {
        if ((r27 instanceof SegmentBase.SingleSegmentBase) == false) goto L7;
        return new SingleSegmentRepresentation(r23, r25, r26, (SegmentBase.SingleSegmentBase) r27, r28, r29, r30, r31, -1);
    L7:
        if ((r27 instanceof SegmentBase.MultiSegmentBase) == false) goto L11;
        return new MultiSegmentRepresentation(r23, r25, r26, (SegmentBase.MultiSegmentBase) r27, r28, r29, r30);
    L11:
        throw new IllegalArgumentException("segmentBase must be of type SingleSegmentBase or MultiSegmentBase");
    }

    public abstract String getCacheKey();

    public abstract DashSegmentIndex getIndex();

    public abstract RangedUri getIndexUri();

    public RangedUri getInitializationUri() {
        return this.initializationUri;
    }
}
