package kotlin.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0018\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a+\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0007¢\u0006\u0002\u0010\u0005\u001a\u0018\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0007¨\u0006\b"}, m421d2 = {"getPolymorphicElement", ExifInterface.LONGITUDE_EAST, "Lkotlin/coroutines/CoroutineContext$Element;", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Element;Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusPolymorphicKey", "Lkotlin/coroutines/CoroutineContext;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class CoroutineContextImplKt {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <E extends CoroutineContext.Element> E getPolymorphicElement(CoroutineContext.Element r3, CoroutineContext.Key<E> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "key");
        if ((r4 instanceof AbstractCoroutineContextKey) == false) goto L11;
        if (((AbstractCoroutineContextKey) r4).isSubKey$kotlin_stdlib(r3.getKey()) == false) goto L14;
        E r0 = (E) ((AbstractCoroutineContextKey) r4).tryCast$kotlin_stdlib(r3);
        if ((r0 instanceof CoroutineContext.Element) == true) goto L9;
        return null;
    L9:
        return r0;
    L14:
        return null;
    L11:
        if (r3.getKey() == r4) goto L13;
        return null;
    L13:
        return r3;
    }

    public static final CoroutineContext minusPolymorphicKey(CoroutineContext.Element r2, CoroutineContext.Key<?> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "key");
        if ((r3 instanceof AbstractCoroutineContextKey) == false) goto L12;
        if (((AbstractCoroutineContextKey) r3).isSubKey$kotlin_stdlib(r2.getKey()) == false) goto L10;
        if (((AbstractCoroutineContextKey) r3).tryCast$kotlin_stdlib(r2) == null) goto L10;
        return EmptyCoroutineContext.INSTANCE;
    L10:
        return r2;
    L12:
        if (r2.getKey() != r3) goto L15;
        return EmptyCoroutineContext.INSTANCE;
    L15:
        return r2;
    }
}
