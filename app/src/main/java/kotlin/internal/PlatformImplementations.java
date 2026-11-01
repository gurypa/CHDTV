package kotlin.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.MatchResult;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import kotlin.text.MatchGroup;

/* JADX INFO: compiled from: PlatformImplementations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0012B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\u0013"}, m421d2 = {"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "getSuppressed", "", "ReflectThrowable", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class PlatformImplementations {

    /* JADX INFO: compiled from: PlatformImplementations.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, m421d2 = {"Lkotlin/internal/PlatformImplementations$ReflectThrowable;", "", "()V", "addSuppressed", "Ljava/lang/reflect/Method;", "getSuppressed", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class ReflectThrowable {
        public static final ReflectThrowable INSTANCE = null;
        public static final Method addSuppressed = null;
        public static final Method getSuppressed = null;

        static {
            INSTANCE = new ReflectThrowable();
            Method[] r1 = Throwable.class.getMethods();
            Intrinsics.checkNotNull(r1);
            int r2 = r1.length;
            int r3 = 0;
            int r4 = 0;
        L3:
            Method r5 = null;
            if (r4 >= r2) goto L14;
            Method r6 = r1[r4];
            if (Intrinsics.areEqual(r6.getName(), "addSuppressed") == false) goto L10;
            Class<?>[] r9 = r6.getParameterTypes();
            Intrinsics.checkNotNullExpressionValue(r9, "getParameterTypes(...)");
            if (Intrinsics.areEqual(ArraysKt.singleOrNull(r9), Throwable.class) == false) goto L10;
            boolean r10 = true;
        L11:
            if (r10 == true) goto L15;
            r4 = r4 + 1;
        L15:
            addSuppressed = r6;
            int r7 = r1.length;
        L16:
            if (r3 >= r7) goto L21;
            Method r8 = r1[r3];
            if (Intrinsics.areEqual(r8.getName(), "getSuppressed") == true) goto L19;
            r3 = r3 + 1;
            goto L16
        L19:
            r5 = r8;
        L21:
            getSuppressed = r5;
            return;
        L10:
            r10 = false;
            goto L11
        L14:
            r6 = null;
            goto L15
        }

        private ReflectThrowable() {
        }
    }

    public PlatformImplementations() {
    }

    public void addSuppressed(Throwable r4, Throwable r5) {
        Intrinsics.checkNotNullParameter(r4, "cause");
        Intrinsics.checkNotNullParameter(r5, "exception");
        Method r0 = ReflectThrowable.addSuppressed;
        if (r0 == null) goto L6;
        r0.invoke(r4, new Object[]{r5});
        return;
    }

    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }

    public MatchGroup getMatchResultNamedGroup(MatchResult r3, String r4) {
        Intrinsics.checkNotNullParameter(r3, "matchResult");
        Intrinsics.checkNotNullParameter(r4, "name");
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    public List<Throwable> getSuppressed(Throwable r4) {
        Intrinsics.checkNotNullParameter(r4, "exception");
        Method r0 = ReflectThrowable.getSuppressed;
        if (r0 == null) goto L9;
        Object r1 = r0.invoke(r4, new Object[0]);
        if (r1 == null) goto L9;
        List<Throwable> r2 = ArraysKt.asList((Throwable[]) r1);
        if (r2 == null) goto L9;
        return r2;
    L9:
        return CollectionsKt.emptyList();
    }
}
