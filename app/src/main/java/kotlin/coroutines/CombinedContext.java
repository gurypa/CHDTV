package kotlin.coroutines;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.MediaInformation;
import java.io.Serializable;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

/* JADX INFO: compiled from: CoroutineContextImpl.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0001\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001!B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0000H\u0002J\u0013\u0010\f\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J5\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u00102\u0006\u0010\u0011\u001a\u0002H\u00102\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u00100\u0013H\u0016¢\u0006\u0002\u0010\u0014J(\u0010\u0015\u001a\u0004\u0018\u0001H\u0016\"\b\b\u0000\u0010\u0016*\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018H\u0096\u0002¢\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u00012\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018H\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u000eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, m421d2 = {"Lkotlin/coroutines/CombinedContext;", "Lkotlin/coroutines/CoroutineContext;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", TtmlNode.LEFT, "element", "Lkotlin/coroutines/CoroutineContext$Element;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/CoroutineContext$Element;)V", "contains", "", "containsAll", "context", "equals", "other", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", ExifInterface.LONGITUDE_EAST, "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "hashCode", "", "minusKey", MediaInformation.KEY_SIZE, "toString", "", "writeReplace", "Serialized", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class CombinedContext implements CoroutineContext, Serializable {
    private final CoroutineContext.Element element;
    private final CoroutineContext left;

    /* JADX INFO: compiled from: CoroutineContextImpl.kt */
    @Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \f2\u00060\u0001j\u0002`\u0002:\u0001\fB\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0002\u0010\u0006J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b¨\u0006\r"}, m421d2 = {"Lkotlin/coroutines/CombinedContext$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "elements", "", "Lkotlin/coroutines/CoroutineContext;", "([Lkotlin/coroutines/CoroutineContext;)V", "getElements", "()[Lkotlin/coroutines/CoroutineContext;", "[Lkotlin/coroutines/CoroutineContext;", "readResolve", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Serialized implements Serializable {
        public static final Companion Companion = null;
        private static final long serialVersionUID = 0;
        private final CoroutineContext[] elements;

        /* JADX INFO: compiled from: CoroutineContextImpl.kt */
        @Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, m421d2 = {"Lkotlin/coroutines/CombinedContext$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker r1) {
                this();
            }
        }

        static {
            Companion = new Companion(null);
        }

        public Serialized(CoroutineContext[] r2) {
            Intrinsics.checkNotNullParameter(r2, "elements");
            this.elements = r2;
        }

        private final Object readResolve() {
            CoroutineContext[] r0 = this.elements;
            CoroutineContext r3 = EmptyCoroutineContext.INSTANCE;
            int r4 = r0.length;
            int r5 = 0;
        L3:
            if (r5 >= r4) goto L6;
            CoroutineContext r7 = r3;
            r3 = r7.plus(r0[r5]);
            r5 = r5 + 1;
            goto L3
        L6:
            return r3;
        }

        public final CoroutineContext[] getElements() {
            return this.elements;
        }
    }

    public CombinedContext(CoroutineContext r2, CoroutineContext.Element r3) {
        Intrinsics.checkNotNullParameter(r2, TtmlNode.LEFT);
        Intrinsics.checkNotNullParameter(r3, "element");
        this.left = r2;
        this.element = r3;
    }

    private final boolean contains(CoroutineContext.Element r2) {
        return Intrinsics.areEqual(get(r2.getKey()), r2);
    }

    private final boolean containsAll(CombinedContext r4) {
        CombinedContext r0 = r4;
    L4:
        if (contains(r0.element) == false) goto L5;
        CoroutineContext r1 = r0.left;
        if ((r1 instanceof CombinedContext) == false) goto L10;
        r0 = (CombinedContext) r1;
        goto L4
    L10:
        Intrinsics.checkNotNull(r1, "null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
        return contains((CoroutineContext.Element) r1);
    L5:
        return false;
    }

    private final int size() {
        CombinedContext r0 = this;
        int r1 = 2;
    L3:
        CoroutineContext r2 = r0.left;
        if ((r2 instanceof CombinedContext) == false) goto L6;
        CombinedContext r3 = (CombinedContext) r2;
    L7:
        if (r3 == null) goto L8;
        r0 = r3;
        r1 = r1 + 1;
        goto L3
    L8:
        return r1;
    L6:
        r3 = null;
        goto L7
    }

    private final Object writeReplace() {
        int r0 = size();
        final CoroutineContext[] r1 = new CoroutineContext[r0];
        final Ref.IntRef r2 = new Ref.IntRef();
        fold(Unit.INSTANCE, new C10821(r1, r2));
        if (r2.element != r0) goto L5;
        boolean r3 = true;
    L6:
        if (r3 == false) goto L10;
        return new Serialized(r1);
    L10:
        throw new IllegalStateException("Check failed.".toString());
    L5:
        r3 = false;
        goto L6
    }

    public boolean equals(Object r3) {
        if (this != r3) goto L4;
    L11:
        return true;
    L4:
        if ((r3 instanceof CombinedContext) == true) goto L6;
    L10:
        return false;
    L6:
        if (((CombinedContext) r3).size() != size()) goto L10;
        if (((CombinedContext) r3).containsAll(this) == false) goto L10;
        goto L10
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <R> R fold(R r3, Function2<? super R, ? super CoroutineContext.Element, ? extends R> r4) {
        Intrinsics.checkNotNullParameter(r4, "operation");
        return r4.invoke((Object) this.left.fold(r3, r4), this.element);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> r4) {
        Intrinsics.checkNotNullParameter(r4, "key");
        CombinedContext r0 = this;
    L3:
        E r1 = (E) r0.element.get(r4);
        if (r1 != null) goto L6;
        CoroutineContext r2 = r0.left;
        if ((r2 instanceof CombinedContext) == false) goto L11;
        r0 = (CombinedContext) r2;
        goto L3
    L11:
        return (E) r2.get(r4);
    L6:
        return r1;
    }

    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }

    @Override // kotlin.coroutines.CoroutineContext
    public CoroutineContext minusKey(CoroutineContext.Key<?> r4) {
        Intrinsics.checkNotNullParameter(r4, "key");
        if (this.element.get(r4) != null) goto L5;
        CoroutineContext r0 = this.left.minusKey(r4);
        if (r0 != this.left) goto L10;
        return this;
    L10:
        if (r0 != EmptyCoroutineContext.INSTANCE) goto L13;
        return this.element;
    L13:
        return new CombinedContext(r0, this.element);
    L5:
        return this.left;
    }

    @Override // kotlin.coroutines.CoroutineContext
    public CoroutineContext plus(CoroutineContext r2) {
        return CoroutineContext.DefaultImpls.plus(this, r2);
    }

    public String toString() {
        return '[' + ((String) fold("", C10811.INSTANCE)) + ']';
    }
}
