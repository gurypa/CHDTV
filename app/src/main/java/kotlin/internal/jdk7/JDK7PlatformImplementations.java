package kotlin.internal.jdk7;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.internal.PlatformImplementations;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: JDK7PlatformImplementations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u000eB\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\t2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002¨\u0006\u000f"}, m421d2 = {"Lkotlin/internal/jdk7/JDK7PlatformImplementations;", "Lkotlin/internal/PlatformImplementations;", "()V", "addSuppressed", "", "cause", "", "exception", "getSuppressed", "", "sdkIsNullOrAtLeast", "", "version", "", "ReflectSdkVersion", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public class JDK7PlatformImplementations extends PlatformImplementations {

    /* JADX INFO: compiled from: JDK7PlatformImplementations.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/internal/jdk7/JDK7PlatformImplementations$ReflectSdkVersion;", "", "()V", "sdkVersion", "", "Ljava/lang/Integer;", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
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

    public JDK7PlatformImplementations() {
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
    public void addSuppressed(Throwable r2, Throwable r3) {
        Intrinsics.checkNotNullParameter(r2, "cause");
        Intrinsics.checkNotNullParameter(r3, "exception");
        if (sdkIsNullOrAtLeast(19) == false) goto L5;
        r2.addSuppressed(r3);
        return;
    L5:
        super.addSuppressed(r2, r3);
    }

    @Override // kotlin.internal.PlatformImplementations
    public List<Throwable> getSuppressed(Throwable r3) {
        Intrinsics.checkNotNullParameter(r3, "exception");
        if (sdkIsNullOrAtLeast(19) == false) goto L6;
        Throwable[] r0 = r3.getSuppressed();
        Intrinsics.checkNotNullExpressionValue(r0, "getSuppressed(...)");
        return ArraysKt.asList(r0);
    L6:
        return super.getSuppressed(r3);
    }
}
