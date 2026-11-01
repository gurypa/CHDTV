package kotlin.reflect;

import java.lang.reflect.Type;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
/* synthetic */ class ParameterizedTypeImpl$getTypeName$1$1 extends FunctionReferenceImpl implements Function1<Type, String> {
    public static final ParameterizedTypeImpl$getTypeName$1$1 INSTANCE = null;

    static {
        INSTANCE = new ParameterizedTypeImpl$getTypeName$1$1();
    }

    ParameterizedTypeImpl$getTypeName$1$1() {
        super(1, TypesJVMKt.class, "typeToString", "typeToString(Ljava/lang/reflect/Type;)Ljava/lang/String;", 1);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ String invoke(Type r2) {
        return invoke2(r2);
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final String invoke2(Type r2) {
        Intrinsics.checkNotNullParameter(r2, "p0");
        return TypesJVMKt.access$typeToString(r2);
    }
}
