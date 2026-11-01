package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.reflect.KCallable;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;
import kotlin.text.StringsKt;
import kotlin.text.Typography;

/* JADX INFO: compiled from: ClassReference.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 O2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001OB\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010F\u001a\u00020\u00122\b\u0010G\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010H\u001a\u00020IH\u0002J\b\u0010J\u001a\u00020KH\u0016J\u0012\u0010L\u001a\u00020\u00122\b\u0010M\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010N\u001a\u000201H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u001a\u0010\"\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b#\u0010\u0014\u001a\u0004\b\"\u0010\u0015R\u001a\u0010$\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b%\u0010\u0014\u001a\u0004\b$\u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001e\u0010(\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030)0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b*\u0010\u0010R\u001e\u0010+\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b,\u0010\u0010R\u0016\u0010-\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/R\u0016\u00100\u001a\u0004\u0018\u0001018VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b2\u00103R(\u00104\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u00010\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b5\u0010\u0014\u001a\u0004\b6\u0010\u000bR\u0016\u00107\u001a\u0004\u0018\u0001018VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b8\u00103R \u00109\u001a\b\u0012\u0004\u0012\u00020:0\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b;\u0010\u0014\u001a\u0004\b<\u0010\u000bR \u0010=\u001a\b\u0012\u0004\u0012\u00020>0\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b?\u0010\u0014\u001a\u0004\b@\u0010\u000bR\u001c\u0010A\u001a\u0004\u0018\u00010B8VX\u0097\u0004¢\u0006\f\u0012\u0004\bC\u0010\u0014\u001a\u0004\bD\u0010E¨\u0006P"}, m421d2 = {"Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", "isFinal", "isFinal$annotations", "isFun", "isFun$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "isValue", "isValue$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "sealedSubclasses", "getSealedSubclasses$annotations", "getSealedSubclasses", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "getSupertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "getTypeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "getVisibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", "hashCode", "", "isInstance", "value", "toString", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class ClassReference implements KClass<Object>, ClassBasedDeclarationContainer {
    public static final Companion Companion = null;
    private static final Map<Class<? extends Function<?>>, Integer> FUNCTION_CLASSES = null;
    private static final HashMap<String, String> classFqNames = null;
    private static final HashMap<String, String> primitiveFqNames = null;
    private static final HashMap<String, String> primitiveWrapperFqNames = null;
    private static final Map<String, String> simpleNames = null;
    private final Class<?> jClass;

    /* JADX INFO: compiled from: ClassReference.kt */
    @Metadata(m420d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u001c\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005R&\u0010\u0003\u001a\u001a\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\r\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, m421d2 = {"Lkotlin/jvm/internal/ClassReference$Companion;", "", "()V", "FUNCTION_CLASSES", "", "Ljava/lang/Class;", "Lkotlin/Function;", "", "classFqNames", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "primitiveFqNames", "primitiveWrapperFqNames", "simpleNames", "getClassQualifiedName", "jClass", "getClassSimpleName", "isInstance", "", "value", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final String getClassQualifiedName(Class<?> r5) {
            Intrinsics.checkNotNullParameter(r5, "jClass");
            String r1 = null;
            if (r5.isAnonymousClass() == false) goto L6;
            return null;
        L6:
            if (r5.isLocalClass() == false) goto L9;
            return null;
        L9:
            if (r5.isArray() == false) goto L18;
            Class<?> r0 = r5.getComponentType();
            if (r0.isPrimitive() == false) goto L16;
            String r2 = (String) ClassReference.access$getClassFqNames$cp().get(r0.getName());
            if (r2 == null) goto L16;
            r1 = r2 + "Array";
        L16:
            if (r1 != null) goto L22;
            return "kotlin.Array";
        L22:
            return r1;
        L18:
            String r3 = (String) ClassReference.access$getClassFqNames$cp().get(r5.getName());
            if (r3 == null) goto L21;
            return r3;
        L21:
            return r5.getCanonicalName();
        }

        public final String getClassSimpleName(Class<?> r9) {
            Intrinsics.checkNotNullParameter(r9, "jClass");
            String r1 = null;
            if (r9.isAnonymousClass() == false) goto L6;
            return null;
        L6:
            if (r9.isLocalClass() == false) goto L18;
            String r0 = r9.getSimpleName();
            Method r2 = r9.getEnclosingMethod();
            if (r2 == null) goto L13;
            Intrinsics.checkNotNull(r0);
            String r3 = StringsKt.substringAfter$default(r0, r2.getName() + Typography.dollar, null, 2, null);
            if (r3 == null) goto L13;
            return r3;
        L13:
            Constructor<?> r4 = r9.getEnclosingConstructor();
            if (r4 == null) goto L16;
            Intrinsics.checkNotNull(r0);
            return StringsKt.substringAfter$default(r0, r4.getName() + Typography.dollar, null, 2, null);
        L16:
            Intrinsics.checkNotNull(r0);
            return StringsKt.substringAfter$default(r0, Typography.dollar, null, 2, null);
        L18:
            if (r9.isArray() == false) goto L27;
            Class<?> r5 = r9.getComponentType();
            if (r5.isPrimitive() == false) goto L25;
            String r6 = (String) ClassReference.access$getSimpleNames$cp().get(r5.getName());
            if (r6 == null) goto L25;
            r1 = r6 + "Array";
        L25:
            if (r1 == null) goto L35;
            return r1;
        L35:
            return "Array";
        L27:
            String r7 = (String) ClassReference.access$getSimpleNames$cp().get(r9.getName());
            if (r7 == null) goto L30;
            return r7;
        L30:
            return r9.getSimpleName();
        }

        public final boolean isInstance(Object r4, Class<?> r5) {
            Intrinsics.checkNotNullParameter(r5, "jClass");
            Map r0 = ClassReference.access$getFUNCTION_CLASSES$cp();
            Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.get, V of kotlin.collections.MapsKt__MapsKt.get>");
            Integer r1 = (Integer) r0.get(r5);
            if (r1 == null) goto L7;
            return TypeIntrinsics.isFunctionOfArity(r4, r1.intValue());
        L7:
            if (r5.isPrimitive() == false) goto L9;
            Class<?> r2 = JvmClassMappingKt.getJavaObjectType(JvmClassMappingKt.getKotlinClass(r5));
        L11:
            return r2.isInstance(r4);
        L9:
            r2 = r5;
            goto L11
        }
    }

    static {
        Companion = new Companion(null);
        List r0 = CollectionsKt.listOf(new Class[]{Function0.class, Function1.class, Function2.class, Function3.class, Function4.class, Function5.class, Function6.class, Function7.class, Function8.class, Function9.class, Function10.class, Function11.class, Function12.class, Function13.class, Function14.class, Function15.class, Function16.class, Function17.class, Function18.class, Function19.class, Function20.class, Function21.class, Function22.class});
        ArrayList r4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r0, 10));
        int r7 = 0;
        Iterator r8 = r0.iterator();
    L4:
        if (r8.hasNext() == false) goto L9;
        Object r9 = r8.next();
        int r10 = r7 + 1;
        if (r7 >= 0) goto L8;
        CollectionsKt.throwIndexOverflow();
    L8:
        r4.add(TuplesKt.m428to((Class) r9, Integer.valueOf(r7)));
        r7 = r10;
        goto L4
    L9:
        FUNCTION_CLASSES = MapsKt.toMap(r4);
        HashMap<String, String> r1 = new HashMap();
        r1.put("boolean", "kotlin.Boolean");
        r1.put("char", "kotlin.Char");
        r1.put("byte", "kotlin.Byte");
        r1.put("short", "kotlin.Short");
        r1.put("int", "kotlin.Int");
        r1.put("float", "kotlin.Float");
        r1.put("long", "kotlin.Long");
        r1.put("double", "kotlin.Double");
        primitiveFqNames = r1;
        HashMap<String, String> r2 = new HashMap();
        r2.put("java.lang.Boolean", "kotlin.Boolean");
        r2.put("java.lang.Character", "kotlin.Char");
        r2.put("java.lang.Byte", "kotlin.Byte");
        r2.put("java.lang.Short", "kotlin.Short");
        r2.put("java.lang.Integer", "kotlin.Int");
        r2.put("java.lang.Float", "kotlin.Float");
        r2.put("java.lang.Long", "kotlin.Long");
        r2.put("java.lang.Double", "kotlin.Double");
        primitiveWrapperFqNames = r2;
        HashMap<String, String> r3 = new HashMap();
        r3.put("java.lang.Object", "kotlin.Any");
        r3.put("java.lang.String", "kotlin.String");
        r3.put("java.lang.CharSequence", "kotlin.CharSequence");
        r3.put("java.lang.Throwable", "kotlin.Throwable");
        r3.put("java.lang.Cloneable", "kotlin.Cloneable");
        r3.put("java.lang.Number", "kotlin.Number");
        r3.put("java.lang.Comparable", "kotlin.Comparable");
        r3.put("java.lang.Enum", "kotlin.Enum");
        r3.put("java.lang.annotation.Annotation", "kotlin.Annotation");
        r3.put("java.lang.Iterable", "kotlin.collections.Iterable");
        r3.put("java.util.Iterator", "kotlin.collections.Iterator");
        r3.put("java.util.Collection", "kotlin.collections.Collection");
        r3.put("java.util.List", "kotlin.collections.List");
        r3.put("java.util.Set", "kotlin.collections.Set");
        r3.put("java.util.ListIterator", "kotlin.collections.ListIterator");
        r3.put("java.util.Map", "kotlin.collections.Map");
        r3.put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        r3.put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        r3.put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        r3.putAll(primitiveFqNames);
        r3.putAll(primitiveWrapperFqNames);
        Collection<String> r5 = primitiveFqNames.values();
        Intrinsics.checkNotNullExpressionValue(r5, "<get-values>(...)");
        Iterator<T> r11 = r5.iterator();
    L11:
        if (r11.hasNext() == false) goto L13;
        String r12 = (String) r11.next();
        StringBuilder r13 = new StringBuilder().append("kotlin.jvm.internal.");
        Intrinsics.checkNotNull(r12);
        Pair r14 = TuplesKt.m428to(r13.append(StringsKt.substringAfterLast$default(r12, '.', null, 2, null)).append("CompanionObject").toString(), r12 + ".Companion");
        r3.put(r14.getFirst(), r14.getSecond());
        goto L11
    L13:
        Iterator<Map.Entry<Class<? extends Function<?>>, Integer>> r6 = FUNCTION_CLASSES.entrySet().iterator();
    L15:
        if (r6.hasNext() == false) goto L17;
        Map.Entry<Class<? extends Function<?>>, Integer> r15 = r6.next();
        r3.put(r15.getKey().getName(), "kotlin.Function" + r15.getValue().intValue());
        goto L15
    L17:
        classFqNames = r3;
        HashMap<String, String> r16 = classFqNames;
        LinkedHashMap r17 = new LinkedHashMap(MapsKt.mapCapacity(r16.size()));
        Iterator<T> r18 = r16.entrySet().iterator();
    L19:
        if (r18.hasNext() == false) goto L21;
        Object r19 = r18.next();
        r17.put(((Map.Entry) r19).getKey(), StringsKt.substringAfterLast$default((String) ((Map.Entry) r19).getValue(), '.', null, 2, null));
        goto L19
    L21:
        simpleNames = r17;
    }

    public ClassReference(Class<?> r2) {
        Intrinsics.checkNotNullParameter(r2, "jClass");
        this.jClass = r2;
    }

    public static final /* synthetic */ HashMap access$getClassFqNames$cp() {
        return classFqNames;
    }

    public static final /* synthetic */ Map access$getFUNCTION_CLASSES$cp() {
        return FUNCTION_CLASSES;
    }

    public static final /* synthetic */ Map access$getSimpleNames$cp() {
        return simpleNames;
    }

    private final Void error() {
        throw new KotlinReflectionNotSupportedError();
    }

    public static /* synthetic */ void getSealedSubclasses$annotations() {
    }

    public static /* synthetic */ void getSupertypes$annotations() {
    }

    public static /* synthetic */ void getTypeParameters$annotations() {
    }

    public static /* synthetic */ void getVisibility$annotations() {
    }

    public static /* synthetic */ void isAbstract$annotations() {
    }

    public static /* synthetic */ void isCompanion$annotations() {
    }

    public static /* synthetic */ void isData$annotations() {
    }

    public static /* synthetic */ void isFinal$annotations() {
    }

    public static /* synthetic */ void isFun$annotations() {
    }

    public static /* synthetic */ void isInner$annotations() {
    }

    public static /* synthetic */ void isOpen$annotations() {
    }

    public static /* synthetic */ void isSealed$annotations() {
    }

    public static /* synthetic */ void isValue$annotations() {
    }

    @Override // kotlin.reflect.KClass
    public boolean equals(Object r3) {
        if ((r3 instanceof ClassReference) == true) goto L5;
    L7:
        return false;
    L5:
        if (Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass) r3)) == false) goto L7;
        return true;
    }

    @Override // kotlin.reflect.KAnnotatedElement
    public List<Annotation> getAnnotations() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public Collection<KFunction<Object>> getConstructors() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.jvm.internal.ClassBasedDeclarationContainer
    public Class<?> getJClass() {
        return this.jClass;
    }

    @Override // kotlin.reflect.KClass, kotlin.reflect.KDeclarationContainer
    public Collection<KCallable<?>> getMembers() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public Collection<KClass<?>> getNestedClasses() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public Object getObjectInstance() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public String getQualifiedName() {
        return Companion.getClassQualifiedName(getJClass());
    }

    @Override // kotlin.reflect.KClass
    public List<KClass<? extends Object>> getSealedSubclasses() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public String getSimpleName() {
        return Companion.getClassSimpleName(getJClass());
    }

    @Override // kotlin.reflect.KClass
    public List<KType> getSupertypes() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public List<KTypeParameter> getTypeParameters() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public KVisibility getVisibility() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public int hashCode() {
        return JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    @Override // kotlin.reflect.KClass
    public boolean isAbstract() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isCompanion() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isData() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isFinal() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isFun() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isInner() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isInstance(Object r3) {
        return Companion.isInstance(r3, getJClass());
    }

    @Override // kotlin.reflect.KClass
    public boolean isOpen() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isSealed() {
        error();
        throw new KotlinNothingValueException();
    }

    @Override // kotlin.reflect.KClass
    public boolean isValue() {
        error();
        throw new KotlinNothingValueException();
    }

    public String toString() {
        return getJClass().toString() + " (Kotlin reflection is not available)";
    }
}
