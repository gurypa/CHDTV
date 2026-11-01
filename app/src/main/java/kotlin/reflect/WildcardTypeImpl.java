package kotlin.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u0000 \u00142\u00020\u00012\u00020\u0002:\u0001\u0014B\u0019\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0006J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002J\u0013\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\fH\u0016¢\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0013\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\fH\u0016¢\u0006\u0002\u0010\rJ\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u000fH\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, m421d2 = {"Lkotlin/reflect/WildcardTypeImpl;", "Ljava/lang/reflect/WildcardType;", "Lkotlin/reflect/TypeImpl;", "upperBound", "Ljava/lang/reflect/Type;", "lowerBound", "(Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;)V", "equals", "", "other", "", "getLowerBounds", "", "()[Ljava/lang/reflect/Type;", "getTypeName", "", "getUpperBounds", "hashCode", "", "toString", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class WildcardTypeImpl implements WildcardType, TypeImpl {
    public static final Companion Companion = null;
    private static final WildcardTypeImpl STAR = null;
    private final Type lowerBound;
    private final Type upperBound;

    /* JADX INFO: compiled from: TypesJVM.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/reflect/WildcardTypeImpl$Companion;", "", "()V", "STAR", "Lkotlin/reflect/WildcardTypeImpl;", "getSTAR", "()Lkotlin/reflect/WildcardTypeImpl;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final WildcardTypeImpl getSTAR() {
            return WildcardTypeImpl.access$getSTAR$cp();
        }
    }

    static {
        Companion = new Companion(null);
        STAR = new WildcardTypeImpl(null, null);
    }

    public WildcardTypeImpl(Type r1, Type r2) {
        this.upperBound = r1;
        this.lowerBound = r2;
    }

    public static final /* synthetic */ WildcardTypeImpl access$getSTAR$cp() {
        return STAR;
    }

    public boolean equals(Object r3) {
        if ((r3 instanceof WildcardType) == true) goto L5;
    L9:
        return false;
    L5:
        if (Arrays.equals(getUpperBounds(), ((WildcardType) r3).getUpperBounds()) == false) goto L9;
        if (Arrays.equals(getLowerBounds(), ((WildcardType) r3).getLowerBounds()) == false) goto L9;
        return true;
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getLowerBounds() {
        if (this.lowerBound != null) goto L6;
        return new Type[0];
    L6:
        return new Type[]{this.lowerBound};
    }

    @Override // java.lang.reflect.Type, kotlin.reflect.TypeImpl
    public String getTypeName() {
        if (this.lowerBound == null) goto L6;
        return "? super " + TypesJVMKt.access$typeToString(this.lowerBound);
    L6:
        if (this.upperBound != null) goto L8;
    L10:
        return "?";
    L8:
        if (Intrinsics.areEqual(this.upperBound, Object.class) == true) goto L10;
        return "? extends " + TypesJVMKt.access$typeToString(this.upperBound);
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getUpperBounds() {
        Class r0 = this.upperBound;
        if (r0 != null) goto L6;
    L6:
        return new Type[]{r0};
    }

    public int hashCode() {
        return Arrays.hashCode(getUpperBounds()) ^ Arrays.hashCode(getLowerBounds());
    }

    public String toString() {
        return getTypeName();
    }
}
