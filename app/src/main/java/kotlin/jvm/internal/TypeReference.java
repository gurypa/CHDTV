package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;

/* JADX INFO: compiled from: TypeReference.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B%\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tB/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\bH\u0002J\u0013\u0010$\u001a\u00020\b2\b\u0010%\u001a\u0004\u0018\u00010&H\u0096\u0002J\b\u0010'\u001a\u00020\fH\u0016J\b\u0010(\u001a\u00020\u001eH\u0016J\f\u0010\"\u001a\u00020\u001e*\u00020\u0006H\u0002R\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001c\u0010\u000b\u001a\u00020\f8\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0019R\u001e\u0010\n\u001a\u0004\u0018\u00010\u00018\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u001a\u0010\u0016\u001a\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u00020\u001e*\u0006\u0012\u0002\b\u00030\u001f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b \u0010!¨\u0006*"}, m421d2 = {"Lkotlin/jvm/internal/TypeReference;", "Lkotlin/reflect/KType;", "classifier", "Lkotlin/reflect/KClassifier;", "arguments", "", "Lkotlin/reflect/KTypeProjection;", "isMarkedNullable", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Z)V", "platformTypeUpperBound", "flags", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Lkotlin/reflect/KType;I)V", "annotations", "", "getAnnotations", "()Ljava/util/List;", "getArguments", "getClassifier", "()Lkotlin/reflect/KClassifier;", "getFlags$kotlin_stdlib$annotations", "()V", "getFlags$kotlin_stdlib", "()I", "()Z", "getPlatformTypeUpperBound$kotlin_stdlib$annotations", "getPlatformTypeUpperBound$kotlin_stdlib", "()Lkotlin/reflect/KType;", "arrayClassName", "", "Ljava/lang/Class;", "getArrayClassName", "(Ljava/lang/Class;)Ljava/lang/String;", "asString", "convertPrimitiveToWrapper", "equals", "other", "", "hashCode", "toString", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class TypeReference implements KType {
    public static final Companion Companion = null;
    public static final int IS_MARKED_NULLABLE = 1;
    public static final int IS_MUTABLE_COLLECTION_TYPE = 2;
    public static final int IS_NOTHING_TYPE = 4;
    private final List<KTypeProjection> arguments;
    private final KClassifier classifier;
    private final int flags;
    private final KType platformTypeUpperBound;

    /* JADX INFO: compiled from: TypeReference.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0080T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, m421d2 = {"Lkotlin/jvm/internal/TypeReference$Companion;", "", "()V", "IS_MARKED_NULLABLE", "", "IS_MUTABLE_COLLECTION_TYPE", "IS_NOTHING_TYPE", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    /* JADX INFO: compiled from: TypeReference.kt */
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
    }

    public TypeReference(KClassifier r2, List<KTypeProjection> r3, KType r4, int r5) {
        Intrinsics.checkNotNullParameter(r2, "classifier");
        Intrinsics.checkNotNullParameter(r3, "arguments");
        this.classifier = r2;
        this.arguments = r3;
        this.platformTypeUpperBound = r4;
        this.flags = r5;
    }

    public TypeReference(KClassifier r2, List<KTypeProjection> r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r2, "classifier");
        Intrinsics.checkNotNullParameter(r3, "arguments");
        this(r2, r3, null, r4 ? 1 : 0);
    }

    public static final /* synthetic */ String access$asString(TypeReference r1, KTypeProjection r2) {
        return r1.asString(r2);
    }

    private final String asString(KTypeProjection r4) {
        if (r4.getVariance() != null) goto L6;
        return "*";
    L6:
        KType r0 = r4.getType();
        if ((r0 instanceof TypeReference) == false) goto L9;
        TypeReference r1 = (TypeReference) r0;
    L10:
        if (r1 == null) goto L13;
        String r2 = r1.asString(true);
        if (r2 == null) goto L13;
    L15:
        switch(WhenMappings.$EnumSwitchMapping$0[r4.getVariance().ordinal()]) {
            case 1: goto L21;
            case 2: goto L23;
            case 3: goto L22;
            default: goto L17;
        };
    L17:
        throw new NoWhenBranchMatchedException();
    L22:
        return "out " + r2;
    L23:
        return "in " + r2;
    L21:
        return r2;
    L13:
        r2 = String.valueOf(r4.getType());
        goto L15
    L9:
        r1 = null;
        goto L10
    }

    private final String asString(boolean r14) {
        KClassifier r0 = getClassifier();
        Class<?> r2 = null;
        if ((r0 instanceof KClass) == false) goto L5;
        KClass r1 = (KClass) r0;
    L6:
        if (r1 == null) goto L9;
        r2 = JvmClassMappingKt.getJavaClass(r1);
    L9:
        if (r2 != null) goto L12;
        String r3 = getClassifier().toString();
    L22:
        String r4 = "";
        if (getArguments().isEmpty() == false) goto L25;
        String r5 = "";
    L27:
        if (isMarkedNullable() == false) goto L29;
        r4 = "?";
    L29:
        String r6 = r3 + r5 + r4;
        KType r7 = this.platformTypeUpperBound;
        if ((r7 instanceof TypeReference) == false) goto L40;
        String r8 = ((TypeReference) r7).asString(true);
        if (Intrinsics.areEqual(r8, r6) == true) goto L40;
        if (Intrinsics.areEqual(r8, r6 + '?') == false) goto L42;
        return r6 + '!';
    L42:
        return '(' + r6 + ".." + r8 + ')';
    L40:
        return r6;
    L25:
        r5 = CollectionsKt.joinToString$default(getArguments(), ", ", "<", ">", 0, null, new TypeReference$asString$args$1(this), 24, null);
        goto L27
    L12:
        if ((this.flags & 4) == 0) goto L15;
        r3 = "kotlin.Nothing";
        goto L22
    L15:
        if (r2.isArray() == false) goto L17;
        r3 = getArrayClassName(r2);
        goto L22
    L17:
        if (r14 == true) goto L19;
    L21:
        r3 = r2.getName();
        goto L22
    L19:
        if (r2.isPrimitive() == false) goto L21;
        KClassifier r9 = getClassifier();
        Intrinsics.checkNotNull(r9, "null cannot be cast to non-null type kotlin.reflect.KClass<*>");
        r3 = JvmClassMappingKt.getJavaObjectType((KClass) r9).getName();
        goto L22
    L5:
        r1 = null;
        goto L6
    }

    private final String getArrayClassName(Class<?> r2) {
        if (Intrinsics.areEqual(r2, boolean[].class) == false) goto L6;
        return "kotlin.BooleanArray";
    L6:
        if (Intrinsics.areEqual(r2, char[].class) == false) goto L9;
        return "kotlin.CharArray";
    L9:
        if (Intrinsics.areEqual(r2, byte[].class) == false) goto L12;
        return "kotlin.ByteArray";
    L12:
        if (Intrinsics.areEqual(r2, short[].class) == false) goto L15;
        return "kotlin.ShortArray";
    L15:
        if (Intrinsics.areEqual(r2, int[].class) == false) goto L18;
        return "kotlin.IntArray";
    L18:
        if (Intrinsics.areEqual(r2, float[].class) == false) goto L21;
        return "kotlin.FloatArray";
    L21:
        if (Intrinsics.areEqual(r2, long[].class) == false) goto L24;
        return "kotlin.LongArray";
    L24:
        if (Intrinsics.areEqual(r2, double[].class) == false) goto L26;
        return "kotlin.DoubleArray";
    L26:
        return "kotlin.Array";
    }

    public static /* synthetic */ void getFlags$kotlin_stdlib$annotations() {
    }

    public static /* synthetic */ void getPlatformTypeUpperBound$kotlin_stdlib$annotations() {
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof TypeReference) == true) goto L5;
    L13:
        return false;
    L5:
        if (Intrinsics.areEqual(getClassifier(), ((TypeReference) r3).getClassifier()) == false) goto L13;
        if (Intrinsics.areEqual(getArguments(), ((TypeReference) r3).getArguments()) == false) goto L13;
        if (Intrinsics.areEqual(this.platformTypeUpperBound, ((TypeReference) r3).platformTypeUpperBound) == false) goto L13;
        if (this.flags != ((TypeReference) r3).flags) goto L13;
        return true;
    }

    @Override // kotlin.reflect.KAnnotatedElement
    public List<Annotation> getAnnotations() {
        return CollectionsKt.emptyList();
    }

    @Override // kotlin.reflect.KType
    public List<KTypeProjection> getArguments() {
        return this.arguments;
    }

    @Override // kotlin.reflect.KType
    public KClassifier getClassifier() {
        return this.classifier;
    }

    public final int getFlags$kotlin_stdlib() {
        return this.flags;
    }

    public final KType getPlatformTypeUpperBound$kotlin_stdlib() {
        return this.platformTypeUpperBound;
    }

    public int hashCode() {
        return (((getClassifier().hashCode() * 31) + getArguments().hashCode()) * 31) + this.flags;
    }

    @Override // kotlin.reflect.KType
    public boolean isMarkedNullable() {
        if ((this.flags & 1) != 0) goto L7;
        return false;
    L7:
        return true;
    }

    public String toString() {
        return asString(false) + " (Kotlin reflection is not available)";
    }
}
