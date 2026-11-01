package kotlin.reflect;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Unit;

/* JADX INFO: compiled from: KProperty.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001\tR\u001e\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X¦\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\n"}, m421d2 = {"Lkotlin/reflect/KMutableProperty;", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "Lkotlin/reflect/KProperty;", "setter", "Lkotlin/reflect/KMutableProperty$Setter;", "getSetter$annotations", "()V", "getSetter", "()Lkotlin/reflect/KMutableProperty$Setter;", "Setter", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public interface KMutableProperty<V> extends KProperty<V> {

    /* JADX INFO: compiled from: KProperty.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void getSetter$annotations() {
        }
    }

    /* JADX INFO: compiled from: KProperty.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\bf\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u00020\u00040\u0003¨\u0006\u0005"}, m421d2 = {"Lkotlin/reflect/KMutableProperty$Setter;", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "Lkotlin/reflect/KProperty$Accessor;", "Lkotlin/reflect/KFunction;", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public interface Setter<V> extends KProperty.Accessor<V>, KFunction<Unit> {
    }

    Setter<V> getSetter();
}
