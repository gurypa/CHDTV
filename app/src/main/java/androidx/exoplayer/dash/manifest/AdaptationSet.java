package androidx.media3.exoplayer.dash.manifest;

import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AdaptationSet {
    public static final long ID_UNSET = -1;
    public final List<Descriptor> accessibilityDescriptors;
    public final List<Descriptor> essentialProperties;

    /* JADX INFO: renamed from: id */
    public final long f38id;
    public final List<Representation> representations;
    public final List<Descriptor> supplementalProperties;
    public final int type;

    public AdaptationSet(long r2, int r4, List<Representation> r5, List<Descriptor> r6, List<Descriptor> r7, List<Descriptor> r8) {
        this.f38id = r2;
        this.type = r4;
        this.representations = Collections.unmodifiableList(r5);
        this.accessibilityDescriptors = Collections.unmodifiableList(r6);
        this.essentialProperties = Collections.unmodifiableList(r7);
        this.supplementalProperties = Collections.unmodifiableList(r8);
    }
}
