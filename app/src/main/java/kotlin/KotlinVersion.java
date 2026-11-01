package kotlin;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: KotlinVersion.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0017B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0011\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u000e\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\u0016\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003J\u001e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0016J \u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, m421d2 = {"Lkotlin/KotlinVersion;", "", "major", "", "minor", "(II)V", "patch", "(III)V", "getMajor", "()I", "getMinor", "getPatch", "version", "compareTo", "other", "equals", "", "", "hashCode", "isAtLeast", "toString", "", "versionOf", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class KotlinVersion implements Comparable<KotlinVersion> {
    public static final KotlinVersion CURRENT = null;
    public static final Companion Companion = null;
    public static final int MAX_COMPONENT_VALUE = 255;
    private final int major;
    private final int minor;
    private final int patch;
    private final int version;

    /* JADX INFO: compiled from: KotlinVersion.kt */
    @Metadata(m420d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, m421d2 = {"Lkotlin/KotlinVersion$Companion;", "", "()V", "CURRENT", "Lkotlin/KotlinVersion;", "MAX_COMPONENT_VALUE", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    static {
        Companion = new Companion(null);
        CURRENT = KotlinVersionCurrentValue.get();
    }

    public KotlinVersion(int r2, int r3) {
        this(r2, r3, 0);
    }

    public KotlinVersion(int r4, int r5, int r6) {
        this.major = r4;
        this.minor = r5;
        this.patch = r6;
        this.version = versionOf(this.major, this.minor, this.patch);
    }

    private final int versionOf(int r4, int r5, int r6) {
        boolean r1 = false;
        if (new IntRange(0, 255).contains(r4) == true) goto L5;
    L9:
        if (r1 == false) goto L13;
        return ((r4 << 16) + (r5 << 8)) + r6;
    L13:
        throw new IllegalArgumentException(("Version components are out of range: " + r4 + '.' + r5 + '.' + r6).toString());
    L5:
        if (new IntRange(0, 255).contains(r5) == false) goto L9;
        if (new IntRange(0, 255).contains(r6) == false) goto L9;
        r1 = true;
        goto L9
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(KotlinVersion r2) {
        return compareTo2(r2);
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(KotlinVersion r3) {
        Intrinsics.checkNotNullParameter(r3, "other");
        return this.version - r3.version;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof KotlinVersion) == false) goto L8;
        KotlinVersion r1 = (KotlinVersion) r6;
    L10:
        if (r1 != null) goto L13;
        return false;
    L13:
        if (this.version != r1.version) goto L16;
        return true;
    L16:
        return false;
    L8:
        r1 = null;
        goto L10
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public final int getPatch() {
        return this.patch;
    }

    public int hashCode() {
        return this.version;
    }

    public final boolean isAtLeast(int r2, int r3) {
        if (this.major <= r2) goto L5;
    L10:
        return true;
    L5:
        if (this.major == r2) goto L7;
    L9:
        return false;
    L7:
        if (this.minor < r3) goto L9;
        goto L9
    }

    public final boolean isAtLeast(int r2, int r3, int r4) {
        if (this.major <= r2) goto L5;
    L14:
        return true;
    L5:
        if (this.major == r2) goto L7;
    L13:
        return false;
    L7:
        if (this.minor > r3) goto L14;
        if (this.minor != r3) goto L13;
        if (this.patch < r4) goto L13;
        goto L13
    }

    public String toString() {
        return new StringBuilder().append(this.major).append('.').append(this.minor).append('.').append(this.patch).toString();
    }
}
