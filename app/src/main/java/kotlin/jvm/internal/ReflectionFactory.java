package kotlin.jvm.internal;

import java.util.List;
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
public class ReflectionFactory {
    private static final String KOTLIN_JVM_FUNCTIONS = "kotlin.jvm.functions.";

    public ReflectionFactory() {
    }

    public KClass createKotlinClass(Class r2) {
        return new ClassReference(r2);
    }

    public KClass createKotlinClass(Class r2, String r3) {
        return new ClassReference(r2);
    }

    public KFunction function(FunctionReference r1) {
        return r1;
    }

    public KClass getOrCreateKotlinClass(Class r2) {
        return new ClassReference(r2);
    }

    public KClass getOrCreateKotlinClass(Class r2, String r3) {
        return new ClassReference(r2);
    }

    public KDeclarationContainer getOrCreateKotlinPackage(Class r2, String r3) {
        return new PackageReference(r2, r3);
    }

    public KType mutableCollectionType(KType r7) {
        TypeReference r0 = (TypeReference) r7;
        return new TypeReference(r7.getClassifier(), r7.getArguments(), r0.getPlatformTypeUpperBound$kotlin_stdlib(), r0.getFlags$kotlin_stdlib() | 2);
    }

    public KMutableProperty0 mutableProperty0(MutablePropertyReference0 r1) {
        return r1;
    }

    public KMutableProperty1 mutableProperty1(MutablePropertyReference1 r1) {
        return r1;
    }

    public KMutableProperty2 mutableProperty2(MutablePropertyReference2 r1) {
        return r1;
    }

    public KType nothingType(KType r7) {
        TypeReference r0 = (TypeReference) r7;
        return new TypeReference(r7.getClassifier(), r7.getArguments(), r0.getPlatformTypeUpperBound$kotlin_stdlib(), r0.getFlags$kotlin_stdlib() | 4);
    }

    public KType platformType(KType r5, KType r6) {
        return new TypeReference(r5.getClassifier(), r5.getArguments(), r6, ((TypeReference) r5).getFlags$kotlin_stdlib());
    }

    public KProperty0 property0(PropertyReference0 r1) {
        return r1;
    }

    public KProperty1 property1(PropertyReference1 r1) {
        return r1;
    }

    public KProperty2 property2(PropertyReference2 r1) {
        return r1;
    }

    public String renderLambdaToString(FunctionBase r4) {
        String r0 = r4.getClass().getGenericInterfaces()[0].toString();
        if (r0.startsWith(KOTLIN_JVM_FUNCTIONS) == false) goto L6;
        return r0.substring(KOTLIN_JVM_FUNCTIONS.length());
    L6:
        return r0;
    }

    public String renderLambdaToString(Lambda r2) {
        return renderLambdaToString(r2);
    }

    public void setUpperBounds(KTypeParameter r2, List<KType> r3) {
        ((TypeParameterReference) r2).setUpperBounds(r3);
    }

    public KType typeOf(KClassifier r2, List<KTypeProjection> r3, boolean r4) {
        return new TypeReference(r2, r3, r4);
    }

    public KTypeParameter typeParameter(Object r2, String r3, KVariance r4, boolean r5) {
        return new TypeParameterReference(r2, r3, r4, r5);
    }
}
