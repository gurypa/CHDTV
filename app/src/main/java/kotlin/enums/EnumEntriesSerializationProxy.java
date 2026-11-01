package kotlin.enums;

import androidx.exifinterface.media.ExifInterface;
import java.io.Serializable;
import java.lang.Enum;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: EnumEntriesSerializationProxy.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0000\u0018\u0000 \f*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004:\u0001\fB\u0013\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, m421d2 = {"Lkotlin/enums/EnumEntriesSerializationProxy;", ExifInterface.LONGITUDE_EAST, "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "entries", "", "([Ljava/lang/Enum;)V", "c", "Ljava/lang/Class;", "readResolve", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class EnumEntriesSerializationProxy<E extends Enum<E>> implements Serializable {
    private static final Companion Companion = null;
    private static final long serialVersionUID = 0;

    /* JADX INFO: renamed from: c */
    private final Class<E> f192c;

    /* JADX INFO: compiled from: EnumEntriesSerializationProxy.kt */
    @Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, m421d2 = {"Lkotlin/enums/EnumEntriesSerializationProxy$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    static {
        Companion = new Companion(null);
    }

    public EnumEntriesSerializationProxy(E[] r2) {
        Intrinsics.checkNotNullParameter(r2, "entries");
        Class<E> r0 = (Class<E>) r2.getClass().getComponentType();
        Intrinsics.checkNotNull(r0);
        this.f192c = r0;
    }

    private final Object readResolve() {
        E[] r0 = this.f192c.getEnumConstants();
        Intrinsics.checkNotNullExpressionValue(r0, "getEnumConstants(...)");
        return EnumEntriesKt.enumEntries(r0);
    }
}
