package kotlin.internal;

import androidx.exifinterface.media.ExifInterface;
import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.internal.jdk8.JDK8PlatformImplementations;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PlatformImplementations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b¢\u0006\u0002\u0010\f\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, m421d2 = {"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", ExifInterface.GPS_DIRECTION_TRUE, "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class PlatformImplementationsKt {
    public static final PlatformImplementations IMPLEMENTATIONS = null;

    static {
        JDK8PlatformImplementations r0 = new JDK8PlatformImplementations();
        IMPLEMENTATIONS = r0;
        return;
    L6:
        e = move-exception;
        ClassLoader r1 = r0.getClass().getClassLoader();
        ClassLoader r2 = PlatformImplementations.class.getClassLoader();
        if (Intrinsics.areEqual(r1, r2) == false) goto L10;
        throw e;
    L10:
        throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + r1 + ", base type classloader: " + r2, e);
    }

    public static final boolean apiVersionIsAtLeast(int r1, int r2, int r3) {
        return KotlinVersion.CURRENT.isAtLeast(r1, r2, r3);
    }

    private static final /* synthetic */ <T> T castToBaseType(Object r6) {
        Intrinsics.reifiedOperationMarker(1, ExifInterface.GPS_DIRECTION_TRUE);     // Catch: ClassCastException -> L5
        return (T) r6;
    L5:
        e = move-exception;
        ClassLoader r2 = r6.getClass().getClassLoader();
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
        ClassLoader r0 = Object.class.getClassLoader();
        if (Intrinsics.areEqual(r2, r0) == false) goto L9;
        throw e;
    L9:
        throw new ClassNotFoundException("Instance class was loaded from a different classloader: " + r2 + ", base type classloader: " + r0, e);
    }
}
