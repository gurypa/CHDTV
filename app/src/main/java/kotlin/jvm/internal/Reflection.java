package kotlin.jvm.internal;

import java.util.Arrays;
import java.util.Collections;
import kotlin.collections.ArraysKt;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KFunction;
import kotlin.reflect.KMutableProperty0;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KMutableProperty2;
import kotlin.reflect.KProperty0;
import kotlin.reflect.KProperty1;
import kotlin.reflect.KProperty2;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;

/* JADX INFO: loaded from: classes.dex */
public class Reflection {
    private static final KClass[] EMPTY_K_CLASS_ARRAY = null;
    static final String REFLECTION_NOT_AVAILABLE = " (Kotlin reflection is not available)";
    private static final ReflectionFactory factory = null;

    static {
        ReflectionFactory r1 = (ReflectionFactory) Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();     // Catch: IllegalAccessException -> L4 InstantiationException -> L6 ClassNotFoundException -> L8 ClassCastException -> L10
    L13:
        if (r1 == null) goto L15;
        ReflectionFactory r0 = r1;
    L16:
        factory = r0;
        EMPTY_K_CLASS_ARRAY = new KClass[0];
        return;
    L15:
        r0 = new ReflectionFactory();
        goto L16
    L11:
        r1 = null;
    L9:
        r1 = null;
    L5:
        r1 = null;
    L7:
        r1 = null;
        goto L13
    }

    public Reflection() {
    }

    public static KClass createKotlinClass(Class r1) {
        return factory.createKotlinClass(r1);
    }

    public static KClass createKotlinClass(Class r1, String r2) {
        return factory.createKotlinClass(r1, r2);
    }

    public static KFunction function(FunctionReference r1) {
        return factory.function(r1);
    }

    public static KClass getOrCreateKotlinClass(Class r1) {
        return factory.getOrCreateKotlinClass(r1);
    }

    public static KClass getOrCreateKotlinClass(Class r1, String r2) {
        return factory.getOrCreateKotlinClass(r1, r2);
    }

    public static KClass[] getOrCreateKotlinClasses(Class[] r4) {
        int r0 = r4.length;
        if (r0 == 0) goto L5;
        KClass[] r1 = new KClass[r0];
        int r2 = 0;
    L7:
        if (r2 >= r0) goto L9;
        r1[r2] = getOrCreateKotlinClass(r4[r2]);
        r2 = r2 + 1;
        goto L7
    L9:
        return r1;
    L5:
        return EMPTY_K_CLASS_ARRAY;
    }

    public static KDeclarationContainer getOrCreateKotlinPackage(Class r2) {
        return factory.getOrCreateKotlinPackage(r2, "");
    }

    public static KDeclarationContainer getOrCreateKotlinPackage(Class r1, String r2) {
        return factory.getOrCreateKotlinPackage(r1, r2);
    }

    public static KType mutableCollectionType(KType r1) {
        return factory.mutableCollectionType(r1);
    }

    public static KMutableProperty0 mutableProperty0(MutablePropertyReference0 r1) {
        return factory.mutableProperty0(r1);
    }

    public static KMutableProperty1 mutableProperty1(MutablePropertyReference1 r1) {
        return factory.mutableProperty1(r1);
    }

    public static KMutableProperty2 mutableProperty2(MutablePropertyReference2 r1) {
        return factory.mutableProperty2(r1);
    }

    public static KType nothingType(KType r1) {
        return factory.nothingType(r1);
    }

    public static KType nullableTypeOf(Class r4) {
        return factory.typeOf(getOrCreateKotlinClass(r4), Collections.emptyList(), true);
    }

    public static KType nullableTypeOf(Class r4, KTypeProjection r5) {
        return factory.typeOf(getOrCreateKotlinClass(r4), Collections.singletonList(r5), true);
    }

    public static KType nullableTypeOf(Class r4, KTypeProjection r5, KTypeProjection r6) {
        return factory.typeOf(getOrCreateKotlinClass(r4), Arrays.asList(new KTypeProjection[]{r5, r6}), true);
    }

    public static KType nullableTypeOf(Class r4, KTypeProjection... r5) {
        return factory.typeOf(getOrCreateKotlinClass(r4), ArraysKt.toList(r5), true);
    }

    public static KType nullableTypeOf(KClassifier r3) {
        return factory.typeOf(r3, Collections.emptyList(), true);
    }

    public static KType platformType(KType r1, KType r2) {
        return factory.platformType(r1, r2);
    }

    public static KProperty0 property0(PropertyReference0 r1) {
        return factory.property0(r1);
    }

    public static KProperty1 property1(PropertyReference1 r1) {
        return factory.property1(r1);
    }

    public static KProperty2 property2(PropertyReference2 r1) {
        return factory.property2(r1);
    }

    public static String renderLambdaToString(FunctionBase r1) {
        return factory.renderLambdaToString(r1);
    }

    public static String renderLambdaToString(Lambda r1) {
        return factory.renderLambdaToString(r1);
    }

    public static void setUpperBounds(KTypeParameter r2, KType r3) {
        factory.setUpperBounds(r2, Collections.singletonList(r3));
    }

    public static void setUpperBounds(KTypeParameter r2, KType... r3) {
        factory.setUpperBounds(r2, ArraysKt.toList(r3));
    }

    public static KType typeOf(Class r4) {
        return factory.typeOf(getOrCreateKotlinClass(r4), Collections.emptyList(), false);
    }

    public static KType typeOf(Class r4, KTypeProjection r5) {
        return factory.typeOf(getOrCreateKotlinClass(r4), Collections.singletonList(r5), false);
    }

    public static KType typeOf(Class r5, KTypeProjection r6, KTypeProjection r7) {
        return factory.typeOf(getOrCreateKotlinClass(r5), Arrays.asList(new KTypeProjection[]{r6, r7}), false);
    }

    public static KType typeOf(Class r4, KTypeProjection... r5) {
        return factory.typeOf(getOrCreateKotlinClass(r4), ArraysKt.toList(r5), false);
    }

    public static KType typeOf(KClassifier r3) {
        return factory.typeOf(r3, Collections.emptyList(), false);
    }

    public static KTypeParameter typeParameter(Object r1, String r2, KVariance r3, boolean r4) {
        return factory.typeParameter(r1, r2, r3, r4);
    }
}
