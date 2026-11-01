package kotlin.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B)\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b¢\u0006\u0002\u0010\tJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\u0013\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\nH\u0016¢\u0006\u0002\u0010\u0011J\n\u0010\u0012\u001a\u0004\u0018\u00010\u0006H\u0016J\b\u0010\u0013\u001a\u00020\u0006H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0015H\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\nX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000b¨\u0006\u0019"}, m421d2 = {"Lkotlin/reflect/ParameterizedTypeImpl;", "Ljava/lang/reflect/ParameterizedType;", "Lkotlin/reflect/TypeImpl;", "rawType", "Ljava/lang/Class;", "ownerType", "Ljava/lang/reflect/Type;", "typeArguments", "", "(Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/List;)V", "", "[Ljava/lang/reflect/Type;", "equals", "", "other", "", "getActualTypeArguments", "()[Ljava/lang/reflect/Type;", "getOwnerType", "getRawType", "getTypeName", "", "hashCode", "", "toString", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class ParameterizedTypeImpl implements ParameterizedType, TypeImpl {
    private final Type ownerType;
    private final Class<?> rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(Class<?> r5, Type r6, List<? extends Type> r7) {
        Intrinsics.checkNotNullParameter(r5, "rawType");
        Intrinsics.checkNotNullParameter(r7, "typeArguments");
        this.rawType = r5;
        this.ownerType = r6;
        this.typeArguments = (Type[]) r7.toArray(new Type[0]);
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof ParameterizedType) == true) goto L5;
    L11:
        return false;
    L5:
        if (Intrinsics.areEqual(this.rawType, ((ParameterizedType) r3).getRawType()) == false) goto L11;
        if (Intrinsics.areEqual(this.ownerType, ((ParameterizedType) r3).getOwnerType()) == false) goto L11;
        if (Arrays.equals(getActualTypeArguments(), ((ParameterizedType) r3).getActualTypeArguments()) == false) goto L11;
        return true;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return this.typeArguments;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.ownerType;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getRawType() {
        return this.rawType;
    }

    @Override // java.lang.reflect.Type, kotlin.reflect.TypeImpl
    public String getTypeName() {
        StringBuilder r0 = new StringBuilder();
        if (this.ownerType == null) goto L5;
        r0.append(TypesJVMKt.access$typeToString(this.ownerType));
        r0.append("$");
        r0.append(this.rawType.getSimpleName());
    L7:
        if (this.typeArguments.length != 0) goto L9;
        boolean r3 = true;
    L10:
        if (r3 == true) goto L12;
        ArraysKt.joinTo$default(this.typeArguments, r0, null, "<", ">", 0, null, ParameterizedTypeImpl$getTypeName$1$1.INSTANCE, 50, null);
    L12:
        String r1 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
        return r1;
    L9:
        r3 = false;
        goto L10
    L5:
        r0.append(TypesJVMKt.access$typeToString(this.rawType));
        goto L7
    }

    public int hashCode() {
        int r0 = this.rawType.hashCode();
        Type r1 = this.ownerType;
        if (r1 == null) goto L5;
        int r2 = r1.hashCode();
    L7:
        return (r0 ^ r2) ^ Arrays.hashCode(getActualTypeArguments());
    L5:
        r2 = 0;
        goto L7
    }

    public String toString() {
        return getTypeName();
    }
}
