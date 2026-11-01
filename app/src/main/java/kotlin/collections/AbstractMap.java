package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: AbstractMap.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\b'\u0018\u0000 )*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003:\u0001)B\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u001f\u0010\u0013\u001a\u00020\u00142\u0010\u0010\u0015\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u0016H\u0000¢\u0006\u0002\b\u0017J\u0015\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001aJ\u0015\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u001aJ\u0013\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0096\u0002J\u0018\u0010 \u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0019\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010!J\b\u0010\"\u001a\u00020\rH\u0016J#\u0010#\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u00162\u0006\u0010\u0019\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010$J\b\u0010%\u001a\u00020\u0014H\u0016J\b\u0010&\u001a\u00020'H\u0016J\u0012\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u001fH\u0002J\u001c\u0010&\u001a\u00020'2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0016H\bR\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\bX\u0088\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00010\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006*"}, m421d2 = {"Lkotlin/collections/AbstractMap;", "K", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "", "()V", "_keys", "", "_values", "", "keys", "getKeys", "()Ljava/util/Set;", MediaInformation.KEY_SIZE, "", "getSize", "()I", "values", "getValues", "()Ljava/util/Collection;", "containsEntry", "", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "key", "(Ljava/lang/Object;)Z", "containsValue", "value", "equals", "other", "", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "hashCode", "implFindEntry", "(Ljava/lang/Object;)Ljava/util/Map$Entry;", "isEmpty", "toString", "", "o", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public abstract class AbstractMap<K, V> implements Map<K, V>, KMappedMarker {
    public static final Companion Companion = null;
    private volatile Set<? extends K> _keys;
    private volatile Collection<? extends V> _values;

    /* JADX INFO: compiled from: AbstractMap.kt */
    @Metadata(m420d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J'\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\b\bJ\u001d\u0010\t\u001a\u00020\n2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000bJ\u001d\u0010\f\u001a\u00020\r2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000e¨\u0006\u000f"}, m421d2 = {"Lkotlin/collections/AbstractMap$Companion;", "", "()V", "entryEquals", "", "e", "", "other", "entryEquals$kotlin_stdlib", "entryHashCode", "", "entryHashCode$kotlin_stdlib", "entryToString", "", "entryToString$kotlin_stdlib", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final boolean entryEquals$kotlin_stdlib(Map.Entry<?, ?> r4, Object r5) {
            Intrinsics.checkNotNullParameter(r4, "e");
            if ((r5 instanceof Map.Entry) == true) goto L6;
            return false;
        L6:
            if (Intrinsics.areEqual(r4.getKey(), ((Map.Entry) r5).getKey()) == true) goto L8;
            return false;
        L8:
            if (Intrinsics.areEqual(r4.getValue(), ((Map.Entry) r5).getValue()) == false) goto L12;
            return true;
        L12:
            return false;
        }

        public final int entryHashCode$kotlin_stdlib(Map.Entry<?, ?> r6) {
            Intrinsics.checkNotNullParameter(r6, "e");
            Object r2 = r6.getKey();
            int r3 = 0;
            if (r2 == null) goto L5;
            int r4 = r2.hashCode();
        L6:
            Object r5 = r6.getValue();
            if (r5 == null) goto L10;
            r3 = r5.hashCode();
        L10:
            return r4 ^ r3;
        L5:
            r4 = 0;
            goto L6
        }

        public final String entryToString$kotlin_stdlib(Map.Entry<?, ?> r5) {
            Intrinsics.checkNotNullParameter(r5, "e");
            return new StringBuilder().append(r5.getKey()).append('=').append(r5.getValue()).toString();
        }
    }

    static {
        Companion = new Companion(null);
    }

    protected AbstractMap() {
    }

    public static final /* synthetic */ String access$toString(AbstractMap r1, Map.Entry r2) {
        return r1.toString(r2);
    }

    private final Map.Entry<K, V> implFindEntry(K r8) {
        Iterator<T> r2 = entrySet().iterator();
    L4:
        if (r2.hasNext() == false) goto L8;
        Object r3 = r2.next();
        if (Intrinsics.areEqual(((Map.Entry) r3).getKey(), r8) == false) goto L4;
    L10:
        return (Map.Entry) r3;
    L8:
        r3 = null;
        goto L10
    }

    private final String toString(Object r2) {
        if (r2 != this) goto L5;
        return "(this Map)";
    L5:
        return String.valueOf(r2);
    }

    private final String toString(Map.Entry<? extends K, ? extends V> r3) {
        return toString(r3.getKey()) + '=' + toString(r3.getValue());
    }

    @Override // java.util.Map
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final boolean containsEntry$kotlin_stdlib(Map.Entry<?, ?> r7) {
        if (r7 != null) goto L5;
        return false;
    L5:
        Object r1 = r7.getKey();
        Object r2 = r7.getValue();
        AbstractMap<K, V> r3 = this;
        Intrinsics.checkNotNull(r3, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.get, V of kotlin.collections.MapsKt__MapsKt.get>");
        V r4 = r3.get(r1);
        if (Intrinsics.areEqual(r2, r4) == true) goto L8;
        return false;
    L8:
        if (r4 != null) goto L12;
        AbstractMap<K, V> r5 = this;
        Intrinsics.checkNotNull(r5, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.containsKey, *>");
        if (r5.containsKey(r1) == true) goto L14;
        return false;
    L14:
        return true;
    L12:
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsKey(Object r2) {
        if (implFindEntry(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Map
    public boolean containsValue(Object r9) {
        Set<Map.Entry<K, V>> r0 = entrySet();
        if ((r0 instanceof Collection) == true) goto L5;
    L7:
        Iterator<T> r2 = r0.iterator();
    L9:
        if (r2.hasNext() == false) goto L19;
        if (Intrinsics.areEqual(((Map.Entry) r2.next()).getValue(), r9) == false) goto L9;
        return true;
    L19:
        return false;
    L5:
        if (r0.isEmpty() == false) goto L7;
        return false;
    }

    @Override // java.util.Map
    public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
        return getEntries();
    }

    @Override // java.util.Map
    public boolean equals(Object r9) {
        if (r9 != this) goto L6;
        return true;
    L6:
        if ((r9 instanceof Map) == true) goto L9;
        return false;
    L9:
        if (size() == ((Map) r9).size()) goto L11;
        return false;
    L11:
        Set<Map.Entry<K, V>> r1 = ((Map) r9).entrySet();
        if ((r1 instanceof Collection) == true) goto L14;
    L16:
        Iterator<T> r4 = r1.iterator();
    L18:
        if (r4.hasNext() == false) goto L28;
        if (containsEntry$kotlin_stdlib((Map.Entry) r4.next()) == true) goto L18;
        return false;
    L28:
        return true;
    L14:
        if (r1.isEmpty() == false) goto L16;
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V get(Object r2) {
        Map.Entry<K, V> r0 = implFindEntry(r2);
        if (r0 != null) goto L7;
        return null;
    L7:
        return r0.getValue();
    }

    public abstract Set getEntries();

    public Set<K> getKeys() {
        if (this._keys != null) goto L5;
        this._keys = new AbstractMap$keys$1(this);
    L5:
        Set<? extends K> r0 = this._keys;
        Intrinsics.checkNotNull(r0);
        return r0;
    }

    public int getSize() {
        return entrySet().size();
    }

    public Collection<V> getValues() {
        if (this._values != null) goto L5;
        this._values = new C10421(this);
    L5:
        Collection<? extends V> r0 = this._values;
        Intrinsics.checkNotNull(r0);
        return r0;
    }

    @Override // java.util.Map
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        if (size() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    @Override // java.util.Map
    public final /* bridge */ Set<K> keySet() {
        return getKeys();
    }

    @Override // java.util.Map
    public V put(K r3, V r4) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public V remove(Object r3) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    public String toString() {
        return CollectionsKt.joinToString$default(entrySet(), ", ", "{", "}", 0, null, new C10411(this), 24, null);
    }

    @Override // java.util.Map
    public final /* bridge */ Collection<V> values() {
        return getValues();
    }
}
