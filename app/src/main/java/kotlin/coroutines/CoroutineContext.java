package kotlin.coroutines;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CoroutineContext.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001:\u0002\u0011\u0012J5\u0010\u0002\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u00032\u0018\u0010\u0005\u001a\u0014\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u0002H\u00030\u0006H&¢\u0006\u0002\u0010\bJ(\u0010\t\u001a\u0004\u0018\u0001H\n\"\b\b\u0000\u0010\n*\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\n0\fH¦\u0002¢\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u00020\u00002\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\fH&J\u0011\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0000H\u0096\u0002¨\u0006\u0013"}, m421d2 = {"Lkotlin/coroutines/CoroutineContext;", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", ExifInterface.LONGITUDE_EAST, "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "plus", "context", "Element", "Key", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public interface CoroutineContext {

    /* JADX INFO: compiled from: CoroutineContext.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class DefaultImpls {
        public static CoroutineContext plus(CoroutineContext r1, CoroutineContext r2) {
            Intrinsics.checkNotNullParameter(r2, "context");
            if (r2 != EmptyCoroutineContext.INSTANCE) goto L6;
            return r1;
        L6:
            return (CoroutineContext) r2.fold(r1, C10841.INSTANCE);
        }
    }

    /* JADX INFO: compiled from: CoroutineContext.kt */
    @Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J5\u0010\u0006\u001a\u0002H\u0007\"\u0004\b\u0000\u0010\u00072\u0006\u0010\b\u001a\u0002H\u00072\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u0000\u0012\u0004\u0012\u0002H\u00070\nH\u0016¢\u0006\u0002\u0010\u000bJ(\u0010\f\u001a\u0004\u0018\u0001H\r\"\b\b\u0000\u0010\r*\u00020\u00002\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\r0\u0003H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u00020\u00012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0016R\u0016\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0010"}, m421d2 = {"Lkotlin/coroutines/CoroutineContext$Element;", "Lkotlin/coroutines/CoroutineContext;", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", ExifInterface.LONGITUDE_EAST, "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public interface Element extends CoroutineContext {

        /* JADX INFO: compiled from: CoroutineContext.kt */
        @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class DefaultImpls {
            public static <R> R fold(Element r1, R r2, Function2<? super R, ? super Element, ? extends R> r3) {
                Intrinsics.checkNotNullParameter(r3, "operation");
                return r3.invoke(r2, r1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public static <E extends Element> E get(Element r1, Key<E> r2) {
                Intrinsics.checkNotNullParameter(r2, "key");
                if (Intrinsics.areEqual(r1.getKey(), r2) == false) goto L5;
                Intrinsics.checkNotNull(r1, "null cannot be cast to non-null type E of kotlin.coroutines.CoroutineContext.Element.get");
                return r1;
            L5:
                return null;
            }

            public static CoroutineContext minusKey(Element r1, Key<?> r2) {
                Intrinsics.checkNotNullParameter(r2, "key");
                if (Intrinsics.areEqual(r1.getKey(), r2) == false) goto L6;
                return EmptyCoroutineContext.INSTANCE;
            L6:
                return r1;
            }

            public static CoroutineContext plus(Element r1, CoroutineContext r2) {
                Intrinsics.checkNotNullParameter(r2, "context");
                return DefaultImpls.plus(r1, r2);
            }
        }

        @Override // kotlin.coroutines.CoroutineContext
        <R> R fold(R r1, Function2<? super R, ? super Element, ? extends R> r2);

        @Override // kotlin.coroutines.CoroutineContext
        <E extends Element> E get(Key<E> r1);

        Key<?> getKey();

        @Override // kotlin.coroutines.CoroutineContext
        CoroutineContext minusKey(Key<?> r1);
    }

    /* JADX INFO: compiled from: CoroutineContext.kt */
    @Metadata(m420d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\bf\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003¨\u0006\u0004"}, m421d2 = {"Lkotlin/coroutines/CoroutineContext$Key;", ExifInterface.LONGITUDE_EAST, "Lkotlin/coroutines/CoroutineContext$Element;", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public interface Key<E extends Element> {
    }

    <R> R fold(R r1, Function2<? super R, ? super Element, ? extends R> r2);

    <E extends Element> E get(Key<E> r1);

    CoroutineContext minusKey(Key<?> r1);

    CoroutineContext plus(CoroutineContext r1);
}
