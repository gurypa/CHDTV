package kotlin.jvm.internal;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVariance;

/* JADX INFO: compiled from: TypeParameterReference.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB'\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0013\u0010\u0018\u001a\u00020\t2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u001d2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\b\u0010\u001e\u001a\u00020\u0005H\u0016R\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000eR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\f8VX\u0096\u0004¢\u0006\f\u0012\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017¨\u0006 "}, m421d2 = {"Lkotlin/jvm/internal/TypeParameterReference;", "Lkotlin/reflect/KTypeParameter;", TtmlNode.RUBY_CONTAINER, "", "name", "", "variance", "Lkotlin/reflect/KVariance;", "isReified", "", "(Ljava/lang/Object;Ljava/lang/String;Lkotlin/reflect/KVariance;Z)V", "bounds", "", "Lkotlin/reflect/KType;", "()Z", "getName", "()Ljava/lang/String;", "upperBounds", "getUpperBounds$annotations", "()V", "getUpperBounds", "()Ljava/util/List;", "getVariance", "()Lkotlin/reflect/KVariance;", "equals", "other", "hashCode", "", "setUpperBounds", "", "toString", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class TypeParameterReference implements KTypeParameter {
    public static final Companion Companion = null;
    private volatile List<? extends KType> bounds;
    private final Object container;
    private final boolean isReified;
    private final String name;
    private final KVariance variance;

    /* JADX INFO: compiled from: TypeParameterReference.kt */
    @Metadata(m420d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/jvm/internal/TypeParameterReference$Companion;", "", "()V", "toString", "", "typeParameter", "Lkotlin/reflect/KTypeParameter;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {

        /* JADX INFO: compiled from: TypeParameterReference.kt */
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

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final String toString(KTypeParameter r6) {
            Intrinsics.checkNotNullParameter(r6, "typeParameter");
            StringBuilder r0 = new StringBuilder();
            KVariance r3 = r6.getVariance();
            switch(WhenMappings.$EnumSwitchMapping$0[r3.ordinal()]) {
                case 1: goto L7;
                case 2: goto L6;
                case 3: goto L5;
                default: goto L7;
            };
        L5:
            r0.append("out ");
            goto L7
        L6:
            r0.append("in ");
        L7:
            r0.append(r6.getName());
            String r1 = r0.toString();
            Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
            return r1;
        }
    }

    static {
        Companion = new Companion(null);
    }

    public TypeParameterReference(Object r2, String r3, KVariance r4, boolean r5) {
        Intrinsics.checkNotNullParameter(r3, "name");
        Intrinsics.checkNotNullParameter(r4, "variance");
        this.container = r2;
        this.name = r3;
        this.variance = r4;
        this.isReified = r5;
    }

    public static /* synthetic */ void getUpperBounds$annotations() {
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof TypeParameterReference) == true) goto L5;
    L9:
        return false;
    L5:
        if (Intrinsics.areEqual(this.container, ((TypeParameterReference) r3).container) == false) goto L9;
        if (Intrinsics.areEqual(getName(), ((TypeParameterReference) r3).getName()) == false) goto L9;
        return true;
    }

    @Override // kotlin.reflect.KTypeParameter
    public String getName() {
        return this.name;
    }

    @Override // kotlin.reflect.KTypeParameter
    public List<KType> getUpperBounds() {
        List r0 = this.bounds;
        if (r0 != null) goto L6;
        List<KType> r1 = CollectionsKt.listOf(Reflection.nullableTypeOf(Object.class));
        this.bounds = r1;
        return r1;
    L6:
        return r0;
    }

    @Override // kotlin.reflect.KTypeParameter
    public KVariance getVariance() {
        return this.variance;
    }

    public int hashCode() {
        Object r0 = this.container;
        if (r0 == null) goto L5;
        int r1 = r0.hashCode();
    L7:
        return (r1 * 31) + getName().hashCode();
    L5:
        r1 = 0;
        goto L7
    }

    @Override // kotlin.reflect.KTypeParameter
    public boolean isReified() {
        return this.isReified;
    }

    public final void setUpperBounds(List<? extends KType> r4) {
        Intrinsics.checkNotNullParameter(r4, "upperBounds");
        if (this.bounds != null) goto L7;
        this.bounds = r4;
        return;
    L7:
        throw new IllegalStateException(("Upper bounds of type parameter '" + this + "' have already been initialized.").toString());
    }

    public String toString() {
        return Companion.toString(this);
    }
}
