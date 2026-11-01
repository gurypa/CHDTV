package kotlin.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.KTypeBase;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\"\u0010\n\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0003\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00078BX\u0083\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\b\u001a\u0004\b\u0005\u0010\t¨\u0006\u0015"}, m421d2 = {"javaType", "Ljava/lang/reflect/Type;", "Lkotlin/reflect/KType;", "getJavaType$annotations", "(Lkotlin/reflect/KType;)V", "getJavaType", "(Lkotlin/reflect/KType;)Ljava/lang/reflect/Type;", "Lkotlin/reflect/KTypeProjection;", "(Lkotlin/reflect/KTypeProjection;)V", "(Lkotlin/reflect/KTypeProjection;)Ljava/lang/reflect/Type;", "createPossiblyInnerType", "jClass", "Ljava/lang/Class;", "arguments", "", "typeToString", "", "type", "computeJavaType", "forceWrapper", "", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class TypesJVMKt {

    /* JADX INFO: compiled from: TypesJVM.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            int[] r0 = new int[KVariance.values().length];
            r0[KVariance.f201IN.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L14:
            r0[KVariance.INVARIANT.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L16:
            r0[KVariance.OUT.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L12:
            $EnumSwitchMapping$0 = r0;
        }
    }

    public static final /* synthetic */ Type access$computeJavaType(KType r1, boolean r2) {
        return computeJavaType(r1, r2);
    }

    public static final /* synthetic */ String access$typeToString(Type r1) {
        return typeToString(r1);
    }

    private static final Type computeJavaType(KType r8, boolean r9) {
        KClassifier r0 = r8.getClassifier();
        if ((r0 instanceof KTypeParameter) == false) goto L7;
        return new TypeVariableImpl((KTypeParameter) r0);
    L7:
        if ((r0 instanceof KClass) == false) goto L42;
        KClass r1 = (KClass) r0;
        if (r9 == false) goto L11;
        Class r2 = JvmClassMappingKt.getJavaObjectType(r1);
    L12:
        List<KTypeProjection> r3 = r8.getArguments();
        if (r3.isEmpty() == false) goto L17;
        return r2;
    L17:
        if (r2.isArray() == false) goto L40;
        if (r2.getComponentType().isPrimitive() == true) goto L21;
        KTypeProjection r4 = (KTypeProjection) CollectionsKt.singleOrNull(r3);
        if (r4 == null) goto L38;
        KVariance r5 = r4.component1();
        KType r6 = r4.component2();
        if (r5 != null) goto L27;
        int r7 = -1;
    L28:
        switch(r7) {
            case -1: goto L36;
            case 0: goto L30;
            case 1: goto L36;
            case 2: goto L31;
            case 3: goto L31;
            default: goto L30;
        };
    L31:
        Intrinsics.checkNotNull(r6);
        Type r10 = computeJavaType$default(r6, false, 1, null);
        if ((r10 instanceof Class) == false) goto L44;
        return r2;
    L44:
        return new GenericArrayTypeImpl(r10);
    L30:
        throw new NoWhenBranchMatchedException();
    L36:
        return r2;
    L27:
        r7 = WhenMappings.$EnumSwitchMapping$0[r5.ordinal()];
        goto L28
    L38:
        throw new IllegalArgumentException("kotlin.Array must have exactly one type argument: " + r8);
    L21:
        return r2;
    L40:
        return createPossiblyInnerType(r2, r3);
    L11:
        r2 = JvmClassMappingKt.getJavaClass(r1);
        goto L12
    L42:
        throw new UnsupportedOperationException("Unsupported type classifier: " + r8);
    }

    static /* synthetic */ Type computeJavaType$default(KType r0, boolean r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = false;
    L6:
        return computeJavaType(r0, r1);
    }

    private static final Type createPossiblyInnerType(Class<?> r12, List<KTypeProjection> r13) {
        Class<?> r0 = r12.getDeclaringClass();
        if (r0 != null) goto L11;
        List<KTypeProjection> r1 = r13;
        ArrayList r2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r1, 10));
        Iterator<T> r5 = r1.iterator();
    L6:
        if (r5.hasNext() == false) goto L9;
        r2.add(getJavaType((KTypeProjection) r5.next()));
        goto L6
    L9:
        return new ParameterizedTypeImpl(r12, null, r2);
    L11:
        if (Modifier.isStatic(r12.getModifiers()) == false) goto L18;
        Class<?> r3 = r0;
        List<KTypeProjection> r4 = r13;
        ArrayList r6 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r4, 10));
        Iterator<T> r7 = r4.iterator();
    L14:
        if (r7.hasNext() == false) goto L17;
        r6.add(getJavaType((KTypeProjection) r7.next()));
        goto L14
    L17:
        return new ParameterizedTypeImpl(r12, r3, r6);
    L18:
        int r8 = r12.getTypeParameters().length;
        Type r9 = createPossiblyInnerType(r0, r13.subList(r8, r13.size()));
        List<KTypeProjection> r10 = r13.subList(0, r8);
        ArrayList r11 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r10, 10));
        Iterator<T> r14 = r10.iterator();
    L20:
        if (r14.hasNext() == false) goto L23;
        r11.add(getJavaType((KTypeProjection) r14.next()));
        goto L20
    L23:
        return new ParameterizedTypeImpl(r12, r9, r11);
    }

    public static final Type getJavaType(KType r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if ((r3 instanceof KTypeBase) == false) goto L9;
        Type r0 = ((KTypeBase) r3).getJavaType();
        if (r0 == null) goto L9;
        return r0;
    L9:
        return computeJavaType$default(r3, false, 1, null);
    }

    private static final Type getJavaType(KTypeProjection r5) {
        KVariance r0 = r5.getVariance();
        if (r0 == null) goto L5;
        KType r1 = r5.getType();
        Intrinsics.checkNotNull(r1);
        switch(WhenMappings.$EnumSwitchMapping$0[r0.ordinal()]) {
            case 1: goto L13;
            case 2: goto L15;
            case 3: goto L14;
            default: goto L9;
        };
    L9:
        throw new NoWhenBranchMatchedException();
    L14:
        return new WildcardTypeImpl(computeJavaType(r1, true), null);
    L15:
        return computeJavaType(r1, true);
    L13:
        return new WildcardTypeImpl(null, computeJavaType(r1, true));
    L5:
        return WildcardTypeImpl.Companion.getSTAR();
    }

    public static /* synthetic */ void getJavaType$annotations(KType r0) {
    }

    private static /* synthetic */ void getJavaType$annotations(KTypeProjection r0) {
    }

    private static final String typeToString(Type r4) {
        if ((r4 instanceof Class) == false) goto L10;
        if (((Class) r4).isArray() == false) goto L7;
        Sequence r0 = SequencesKt.generateSequence(r4, TypesJVMKt$typeToString$unwrap$1.INSTANCE);
        String r1 = ((Class) SequencesKt.last(r0)).getName() + StringsKt.repeat("[]", SequencesKt.count(r0));
    L8:
        Intrinsics.checkNotNull(r1);
        return r1;
    L7:
        r1 = ((Class) r4).getName();
        goto L8
    L10:
        return r4.toString();
    }
}
