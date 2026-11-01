package androidx.core.util;

import android.util.LruCache;
import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LruCache.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\u001aû\u0001\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0004\"\b\b\u0001\u0010\u0003*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000628\b\u0006\u0010\u0007\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u00060\b2%\b\u0006\u0010\r\u001a\u001f\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0006\u0012\u0004\u0018\u0001H\u00030\u000e2d\b\u0006\u0010\u000f\u001a^\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u0012\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u0013\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u00150\u0010H\u0086\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0016"}, m421d2 = {"lruCache", "Landroid/util/LruCache;", "K", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "", "maxSize", "", "sizeOf", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "key", "value", "create", "Lkotlin/Function1;", "onEntryRemoved", "Lkotlin/Function4;", "", "evicted", "oldValue", "newValue", "", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class LruCacheKt {

    /* JADX INFO: Add missing generic type declarations: [V, K] */
    /* JADX INFO: renamed from: androidx.core.util.LruCacheKt$lruCache$4 */
    /* JADX INFO: compiled from: LruCache.kt */
    @Metadata(m420d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0001J\u0017\u0010\u0002\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0003\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u0004J/\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00028\u00002\u0006\u0010\t\u001a\u00028\u00012\b\u0010\n\u001a\u0004\u0018\u00018\u0001H\u0014¢\u0006\u0002\u0010\u000bJ\u001d\u0010\f\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00028\u00002\u0006\u0010\u000e\u001a\u00028\u0001H\u0014¢\u0006\u0002\u0010\u000f¨\u0006\u0010"}, m421d2 = {"androidx/core/util/LruCacheKt$lruCache$4", "Landroid/util/LruCache;", "create", "key", "(Ljava/lang/Object;)Ljava/lang/Object;", "entryRemoved", "", "evicted", "", "oldValue", "newValue", "(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "sizeOf", "", "value", "(Ljava/lang/Object;Ljava/lang/Object;)I", "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 176)
    public static final class C01024<K, V> extends LruCache<K, V> {
        final /* synthetic */ Function1<K, V> $create;
        final /* synthetic */ Function4<Boolean, K, V, V, Unit> $onEntryRemoved;
        final /* synthetic */ Function2<K, V, Integer> $sizeOf;

        /* JADX WARN: Multi-variable type inference failed */
        public C01024(int r1, Function2<? super K, ? super V, Integer> r2, Function1<? super K, ? extends V> r3, Function4<? super Boolean, ? super K, ? super V, ? super V, Unit> r4) {
            this.$sizeOf = r2;
            this.$create = r3;
            this.$onEntryRemoved = r4;
            super(r1);
        }

        @Override // android.util.LruCache
        protected V create(K r2) {
            Intrinsics.checkNotNullParameter(r2, "key");
            return this.$create.invoke(r2);
        }

        @Override // android.util.LruCache
        protected void entryRemoved(boolean r3, K r4, V r5, V r6) {
            Intrinsics.checkNotNullParameter(r4, "key");
            Intrinsics.checkNotNullParameter(r5, "oldValue");
            this.$onEntryRemoved.invoke(Boolean.valueOf(r3), r4, r5, r6);
        }

        @Override // android.util.LruCache
        protected int sizeOf(K r2, V r3) {
            Intrinsics.checkNotNullParameter(r2, "key");
            Intrinsics.checkNotNullParameter(r3, "value");
            return this.$sizeOf.invoke(r2, r3).intValue();
        }
    }

    public static final <K, V> LruCache<K, V> lruCache(int r2, Function2<? super K, ? super V, Integer> r3, Function1<? super K, ? extends V> r4, Function4<? super Boolean, ? super K, ? super V, ? super V, Unit> r5) {
        Intrinsics.checkNotNullParameter(r3, "sizeOf");
        Intrinsics.checkNotNullParameter(r4, "create");
        Intrinsics.checkNotNullParameter(r5, "onEntryRemoved");
        return new C01024(r2, r3, r4, r5);
    }

    public static /* synthetic */ LruCache lruCache$default(int r0, C00991 r1, C01002 r2, C01013 r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r1 = C00991.INSTANCE;
    L6:
        if ((r4 & 4) == 0) goto L9;
        r2 = C01002.INSTANCE;
    L9:
        if ((r4 & 8) == 0) goto L11;
        r3 = C01013.INSTANCE;
    L11:
        Intrinsics.checkNotNullParameter(r1, "sizeOf");
        Intrinsics.checkNotNullParameter(r2, "create");
        Intrinsics.checkNotNullParameter(r3, "onEntryRemoved");
        return new C01024(r0, r1, r2, r3);
    }
}
