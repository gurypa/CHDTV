package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, m421d2 = {"Lkotlin/text/MatchGroup;", "", "value", "", "range", "Lkotlin/ranges/IntRange;", "(Ljava/lang/String;Lkotlin/ranges/IntRange;)V", "getRange", "()Lkotlin/ranges/IntRange;", "getValue", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class MatchGroup {
    private final IntRange range;
    private final String value;

    public MatchGroup(String r2, IntRange r3) {
        Intrinsics.checkNotNullParameter(r2, "value");
        Intrinsics.checkNotNullParameter(r3, "range");
        this.value = r2;
        this.range = r3;
    }

    public static /* synthetic */ MatchGroup copy$default(MatchGroup r0, String r1, IntRange r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = r0.value;
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = r0.range;
    L9:
        return r0.copy(r1, r2);
    }

    public final String component1() {
        return this.value;
    }

    public final IntRange component2() {
        return this.range;
    }

    public final MatchGroup copy(String r2, IntRange r3) {
        Intrinsics.checkNotNullParameter(r2, "value");
        Intrinsics.checkNotNullParameter(r3, "range");
        return new MatchGroup(r2, r3);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof MatchGroup) == true) goto L8;
        return false;
    L8:
        MatchGroup r1 = (MatchGroup) r6;
        if (Intrinsics.areEqual(this.value, r1.value) == true) goto L12;
        return false;
    L12:
        if (Intrinsics.areEqual(this.range, r1.range) == true) goto L14;
        return false;
    L14:
        return true;
    }

    public final IntRange getRange() {
        return this.range;
    }

    public final String getValue() {
        return this.value;
    }

    public int hashCode() {
        return (this.value.hashCode() * 31) + this.range.hashCode();
    }

    public String toString() {
        return "MatchGroup(value=" + this.value + ", range=" + this.range + ')';
    }
}
