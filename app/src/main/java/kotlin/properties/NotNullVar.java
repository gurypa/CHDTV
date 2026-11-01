package kotlin.properties;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

/* JADX INFO: compiled from: Delegates.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u0002H\u00010\u0003B\u0005¢\u0006\u0002\u0010\u0004J$\u0010\u0007\u001a\u00028\u00002\b\u0010\b\u001a\u0004\u0018\u00010\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\nH\u0096\u0002¢\u0006\u0002\u0010\u000bJ,\u0010\f\u001a\u00020\r2\b\u0010\b\u001a\u0004\u0018\u00010\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\u0006\u0010\u0005\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u0012\u0010\u0005\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0006¨\u0006\u0011"}, m421d2 = {"Lkotlin/properties/NotNullVar;", ExifInterface.GPS_DIRECTION_TRUE, "", "Lkotlin/properties/ReadWriteProperty;", "()V", "value", "Ljava/lang/Object;", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "toString", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class NotNullVar<T> implements ReadWriteProperty<Object, T> {
    private T value;

    public NotNullVar() {
    }

    @Override // kotlin.properties.ReadWriteProperty, kotlin.properties.ReadOnlyProperty
    public T getValue(Object r4, KProperty<?> r5) {
        Intrinsics.checkNotNullParameter(r5, "property");
        T r0 = this.value;
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalStateException("Property " + r5.getName() + " should be initialized before get.");
    }

    @Override // kotlin.properties.ReadWriteProperty
    public void setValue(Object r2, KProperty<?> r3, T r4) {
        Intrinsics.checkNotNullParameter(r3, "property");
        Intrinsics.checkNotNullParameter(r4, "value");
        this.value = r4;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder().append("NotNullProperty(");
        if (this.value == null) goto L5;
        String r1 = "value=" + this.value;
    L7:
        return r0.append(r1).append(')').toString();
    L5:
        r1 = "value not initialized yet";
        goto L7
    }
}
