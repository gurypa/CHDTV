package kotlin.annotation;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Annotations.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/annotation/AnnotationRetention;", "", "(Ljava/lang/String;I)V", "SOURCE", "BINARY", "RUNTIME", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum AnnotationRetention extends Enum<AnnotationRetention> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ AnnotationRetention[] $VALUES = null;
    public static final AnnotationRetention BINARY = null;
    public static final AnnotationRetention RUNTIME = null;
    public static final AnnotationRetention SOURCE = null;

    private static final /* synthetic */ AnnotationRetention[] $values() {
        return new AnnotationRetention[]{SOURCE, BINARY, RUNTIME};
    }

    static {
        SOURCE = new AnnotationRetention("SOURCE", 0);
        BINARY = new AnnotationRetention("BINARY", 1);
        RUNTIME = new AnnotationRetention("RUNTIME", 2);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private AnnotationRetention(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<AnnotationRetention> getEntries() {
        return $ENTRIES;
    }

    public static AnnotationRetention valueOf(String r1) {
        return (AnnotationRetention) Enum.valueOf(AnnotationRetention.class, r1);
    }

    public static AnnotationRetention[] values() {
        return (AnnotationRetention[]) $VALUES.clone();
    }
}
