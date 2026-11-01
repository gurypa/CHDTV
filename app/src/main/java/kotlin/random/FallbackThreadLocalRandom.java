package kotlin.random;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PlatformRandom.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0003*\u0001\b\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\t¨\u0006\n"}, m421d2 = {"Lkotlin/random/FallbackThreadLocalRandom;", "Lkotlin/random/AbstractPlatformRandom;", "()V", "impl", "Ljava/util/Random;", "getImpl", "()Ljava/util/Random;", "implStorage", "kotlin/random/FallbackThreadLocalRandom$implStorage$1", "Lkotlin/random/FallbackThreadLocalRandom$implStorage$1;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class FallbackThreadLocalRandom extends AbstractPlatformRandom {
    private final FallbackThreadLocalRandom$implStorage$1 implStorage;

    /* JADX WARN: Type inference failed for: r0v0, types: [kotlin.random.FallbackThreadLocalRandom$implStorage$1] */
    public FallbackThreadLocalRandom() {
        this.implStorage = new FallbackThreadLocalRandom$implStorage$1();
    }

    @Override // kotlin.random.AbstractPlatformRandom
    public java.util.Random getImpl() {
        java.util.Random r0 = get();
        Intrinsics.checkNotNullExpressionValue(r0, "get(...)");
        return r0;
    }
}
