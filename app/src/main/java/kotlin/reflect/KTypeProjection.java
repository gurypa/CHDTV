package kotlin.reflect;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: KTypeProjection.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\b\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J!\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0016"}, m421d2 = {"Lkotlin/reflect/KTypeProjection;", "", "variance", "Lkotlin/reflect/KVariance;", "type", "Lkotlin/reflect/KType;", "(Lkotlin/reflect/KVariance;Lkotlin/reflect/KType;)V", "getType", "()Lkotlin/reflect/KType;", "getVariance", "()Lkotlin/reflect/KVariance;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class KTypeProjection {
    public static final Companion Companion = null;
    public static final KTypeProjection star = null;
    private final KType type;
    private final KVariance variance;

    /* JADX INFO: compiled from: KTypeProjection.kt */
    @Metadata(m420d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0007\u001a\u00020\u00048\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\b\u0010\u0002¨\u0006\u000e"}, m421d2 = {"Lkotlin/reflect/KTypeProjection$Companion;", "", "()V", "STAR", "Lkotlin/reflect/KTypeProjection;", "getSTAR", "()Lkotlin/reflect/KTypeProjection;", "star", "getStar$annotations", "contravariant", "type", "Lkotlin/reflect/KType;", "covariant", "invariant", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public static /* synthetic */ void getStar$annotations() {
        }

        @JvmStatic
        public final KTypeProjection contravariant(KType r3) {
            Intrinsics.checkNotNullParameter(r3, "type");
            return new KTypeProjection(KVariance.f201IN, r3);
        }

        @JvmStatic
        public final KTypeProjection covariant(KType r3) {
            Intrinsics.checkNotNullParameter(r3, "type");
            return new KTypeProjection(KVariance.OUT, r3);
        }

        public final KTypeProjection getSTAR() {
            return KTypeProjection.star;
        }

        @JvmStatic
        public final KTypeProjection invariant(KType r3) {
            Intrinsics.checkNotNullParameter(r3, "type");
            return new KTypeProjection(KVariance.INVARIANT, r3);
        }
    }

    /* JADX INFO: compiled from: KTypeProjection.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            int[] r0 = new int[KVariance.values().length];
            r0[KVariance.INVARIANT.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L14:
            r0[KVariance.f201IN.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L16:
            r0[KVariance.OUT.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L12:
            $EnumSwitchMapping$0 = r0;
        }
    }

    static {
        Companion = new Companion(null);
        star = new KTypeProjection(null, null);
    }

    public KTypeProjection(KVariance r5, KType r6) {
        this.variance = r5;
        this.type = r6;
        boolean r1 = true;
        if (this.variance != null) goto L5;
        boolean r0 = true;
    L7:
        if (this.type != null) goto L9;
        boolean r3 = true;
    L10:
        if (r0 == r3) goto L13;
        r1 = false;
    L13:
        if (r1 == false) goto L15;
        return;
    L15:
        if (this.variance != null) goto L17;
        String r2 = "Star projection must have no type specified.";
    L19:
        throw new IllegalArgumentException(r2.toString());
    L17:
        r2 = "The projection variance " + this.variance + " requires type to be specified.";
        goto L19
    L9:
        r3 = false;
        goto L10
    L5:
        r0 = false;
        goto L7
    }

    @JvmStatic
    public static final KTypeProjection contravariant(KType r1) {
        return Companion.contravariant(r1);
    }

    public static /* synthetic */ KTypeProjection copy$default(KTypeProjection r0, KVariance r1, KType r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = r0.variance;
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = r0.type;
    L9:
        return r0.copy(r1, r2);
    }

    @JvmStatic
    public static final KTypeProjection covariant(KType r1) {
        return Companion.covariant(r1);
    }

    @JvmStatic
    public static final KTypeProjection invariant(KType r1) {
        return Companion.invariant(r1);
    }

    public final KVariance component1() {
        return this.variance;
    }

    public final KType component2() {
        return this.type;
    }

    public final KTypeProjection copy(KVariance r2, KType r3) {
        return new KTypeProjection(r2, r3);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof KTypeProjection) == true) goto L8;
        return false;
    L8:
        KTypeProjection r1 = (KTypeProjection) r6;
        if (this.variance == r1.variance) goto L12;
        return false;
    L12:
        if (Intrinsics.areEqual(this.type, r1.type) == true) goto L14;
        return false;
    L14:
        return true;
    }

    public final KType getType() {
        return this.type;
    }

    public final KVariance getVariance() {
        return this.variance;
    }

    public int hashCode() {
        int r1 = 0;
        if (this.variance != null) goto L5;
        int r0 = 0;
    L6:
        int r2 = r0 * 31;
        if (this.type == null) goto L11;
        r1 = this.type.hashCode();
    L11:
        return r2 + r1;
    L5:
        r0 = this.variance.hashCode();
        goto L6
    }

    public String toString() {
        KVariance r0 = this.variance;
        if (r0 != null) goto L5;
        int r1 = -1;
    L6:
        switch(r1) {
            case -1: goto L12;
            case 0: goto L8;
            case 1: goto L16;
            case 2: goto L15;
            case 3: goto L14;
            default: goto L8;
        };
    L12:
        return "*";
    L8:
        throw new NoWhenBranchMatchedException();
    L14:
        return "out " + this.type;
    L15:
        return "in " + this.type;
    L16:
        return String.valueOf(this.type);
    L5:
        r1 = WhenMappings.$EnumSwitchMapping$0[r0.ordinal()];
        goto L6
    }
}
