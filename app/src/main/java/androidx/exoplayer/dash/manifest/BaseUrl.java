package androidx.media3.exoplayer.dash.manifest;

import com.google.common.base.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class BaseUrl {
    public static final int DEFAULT_DVB_PRIORITY = 1;
    public static final int DEFAULT_WEIGHT = 1;
    public static final int PRIORITY_UNSET = Integer.MIN_VALUE;
    public final int priority;
    public final String serviceLocation;
    public final String url;
    public final int weight;

    public BaseUrl(String r3) {
        this(r3, r3, Integer.MIN_VALUE, 1);
    }

    public BaseUrl(String r1, String r2, int r3, int r4) {
        this.url = r1;
        this.serviceLocation = r2;
        this.priority = r3;
        this.weight = r4;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof BaseUrl) == true) goto L8;
        return false;
    L8:
        BaseUrl r1 = (BaseUrl) r6;
        if (this.priority != r1.priority) goto L18;
        if (this.weight != r1.weight) goto L18;
        if (Objects.equal(this.url, r1.url) == false) goto L18;
        if (Objects.equal(this.serviceLocation, r1.serviceLocation) == false) goto L18;
        return true;
    L18:
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.url, this.serviceLocation, Integer.valueOf(this.priority), Integer.valueOf(this.weight)});
    }
}
