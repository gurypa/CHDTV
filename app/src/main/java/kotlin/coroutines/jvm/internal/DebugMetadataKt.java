package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DebugMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0002\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\bH\u0002\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0002\u001a\u0019\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b*\u00020\bH\u0001¢\u0006\u0002\u0010\r\u001a\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\bH\u0001¢\u0006\u0002\b\u0010\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, m421d2 = {"COROUTINES_DEBUG_METADATA_VERSION", "", "checkDebugMetadataVersion", "", "expected", "actual", "getDebugMetadataAnnotation", "Lkotlin/coroutines/jvm/internal/DebugMetadata;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getLabel", "getSpilledVariableFieldMapping", "", "", "(Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;)[Ljava/lang/String;", "getStackTraceElementImpl", "Ljava/lang/StackTraceElement;", "getStackTraceElement", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class DebugMetadataKt {
    private static final int COROUTINES_DEBUG_METADATA_VERSION = 1;

    private static final void checkDebugMetadataVersion(int r3, int r4) {
        if (r4 > r3) goto L5;
        return;
    L5:
        throw new IllegalStateException(("Debug metadata version mismatch. Expected: " + r3 + ", got " + r4 + ". Please update the Kotlin standard library.").toString());
    }

    private static final DebugMetadata getDebugMetadataAnnotation(BaseContinuationImpl r2) {
        return (DebugMetadata) r2.getClass().getAnnotation(DebugMetadata.class);
    }

    private static final int getLabel(BaseContinuationImpl r4) {
        Field r0 = r4.getClass().getDeclaredField("label");     // Catch: Exception -> L11
        r0.setAccessible(true);     // Catch: Exception -> L11
        Object r2 = r0.get(r4);     // Catch: Exception -> L11
        if ((r2 instanceof Integer) == false) goto L5;
        Integer r3 = (Integer) r2;     // Catch: Exception -> L11
    L6:
        if (r3 == null) goto L9;
        int r5 = r3.intValue();     // Catch: Exception -> L11
    L16:
        return r5 - 1;
    L9:
        r5 = 0;
        goto L16
    L5:
        r3 = null;
    L12:
        return -1;
    }

    public static final String[] getSpilledVariableFieldMapping(BaseContinuationImpl r10) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        DebugMetadata r0 = getDebugMetadataAnnotation(r10);
        if (r0 != null) goto L6;
        return null;
    L6:
        checkDebugMetadataVersion(1, r0.m443v());
        ArrayList r1 = new ArrayList();
        int r2 = getLabel(r10);
        int[] r3 = r0.m438i();
        int r4 = r3.length;
        int r6 = 0;
    L7:
        if (r6 >= r4) goto L13;
        int r7 = r6;
        if (r3[r6] != r2) goto L11;
        r1.add(r0.m442s()[r7]);
        r1.add(r0.m441n()[r7]);
    L11:
        r6 = r6 + 1;
        goto L7
    L13:
        return (String[]) r1.toArray(new String[0]);
    }

    public static final StackTraceElement getStackTraceElement(BaseContinuationImpl r8) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        DebugMetadata r0 = getDebugMetadataAnnotation(r8);
        if (r0 != null) goto L6;
        return null;
    L6:
        checkDebugMetadataVersion(1, r0.m443v());
        int r1 = getLabel(r8);
        if (r1 >= 0) goto L9;
        int r2 = -1;
    L10:
        String r3 = ModuleNameRetriever.INSTANCE.getModuleName(r8);
        if (r3 != null) goto L13;
        String r4 = r0.m436c();
    L15:
        return new StackTraceElement(r4, r0.m440m(), r0.m437f(), r2);
    L13:
        r4 = r3 + '/' + r0.m436c();
        goto L15
    L9:
        r2 = r0.m439l()[r1];
        goto L10
    }
}
