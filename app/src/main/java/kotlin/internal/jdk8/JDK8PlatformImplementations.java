package kotlin.internal.jdk8;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.internal.jdk7.JDK7PlatformImplementations;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.jdk8.PlatformThreadLocalRandom;
import kotlin.ranges.IntRange;
import kotlin.text.MatchGroup;

/* JADX INFO: compiled from: JDK8PlatformImplementations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u000fB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002¨\u0006\u0010"}, m421d2 = {"Lkotlin/internal/jdk8/JDK8PlatformImplementations;", "Lkotlin/internal/jdk7/JDK7PlatformImplementations;", "()V", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "sdkIsNullOrAtLeast", "", "version", "", "ReflectSdkVersion", "kotlin-stdlib-jdk8"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class JDK8PlatformImplementations extends JDK7PlatformImplementations {

    /* JADX INFO: compiled from: JDK8PlatformImplementations.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/internal/jdk8/JDK8PlatformImplementations$ReflectSdkVersion;", "", "()V", "sdkVersion", "", "Ljava/lang/Integer;", "kotlin-stdlib-jdk8"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class ReflectSdkVersion {
        public static final ReflectSdkVersion INSTANCE = null;
        public static final Integer sdkVersion = null;

        static {
            INSTANCE = new ReflectSdkVersion();
            Integer r0 = null;
            Object r1 = Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);     // Catch: Throwable -> L8
            if ((r1 instanceof Integer) == false) goto L7;
            Integer r2 = (Integer) r1;     // Catch: Throwable -> L8
        L11:
            if (r2 != null) goto L13;
        L18:
            sdkVersion = r0;
            return;
        L13:
            if (r2.intValue() <= 0) goto L15;
            boolean r4 = true;
        L16:
            if (r4 == false) goto L18;
            r0 = r2;
            goto L18
        L15:
            r4 = false;
            goto L16
        L7:
            r2 = null;
        L9:
            r2 = null;
            goto L11
        }

        private ReflectSdkVersion() {
        }
    }

    public JDK8PlatformImplementations() {
    }

    private final boolean sdkIsNullOrAtLeast(int r2) {
        if (ReflectSdkVersion.sdkVersion != null) goto L5;
    L8:
        return true;
    L5:
        if (ReflectSdkVersion.sdkVersion.intValue() >= r2) goto L8;
        return false;
    }

    @Override // kotlin.internal.PlatformImplementations
    public Random defaultPlatformRandom() {
        if (sdkIsNullOrAtLeast(34) == false) goto L6;
        return new PlatformThreadLocalRandom();
    L6:
        return super.defaultPlatformRandom();
    }

    @Override // kotlin.internal.PlatformImplementations
    public MatchGroup getMatchResultNamedGroup(MatchResult r6, String r7) {
        Intrinsics.checkNotNullParameter(r6, "matchResult");
        Intrinsics.checkNotNullParameter(r7, "name");
        if ((r6 instanceof Matcher) == false) goto L5;
        Matcher r0 = (Matcher) r6;
    L6:
        if (r0 == null) goto L13;
        IntRange r2 = new IntRange(r0.start(r7), r0.end(r7) - 1);
        if (r2.getStart().intValue() < 0) goto L14;
        String r3 = r0.group(r7);
        Intrinsics.checkNotNullExpressionValue(r3, "group(...)");
        return new MatchGroup(r3, r2);
    L14:
        return null;
    L13:
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    L5:
        r0 = null;
        goto L6
    }
}
