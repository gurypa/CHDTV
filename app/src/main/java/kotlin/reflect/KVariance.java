package kotlin.reflect;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: KVariance.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0087\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/reflect/KVariance;", "", "(Ljava/lang/String;I)V", "INVARIANT", "IN", "OUT", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum KVariance extends Enum<KVariance> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ KVariance[] $VALUES = null;

    /* JADX INFO: renamed from: IN */
    public static final KVariance f201IN = null;
    public static final KVariance INVARIANT = null;
    public static final KVariance OUT = null;

    private static final /* synthetic */ KVariance[] $values() {
        return new KVariance[]{INVARIANT, f201IN, OUT};
    }

    static {
        INVARIANT = new KVariance("INVARIANT", 0);
        f201IN = new KVariance("IN", 1);
        OUT = new KVariance("OUT", 2);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private KVariance(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<KVariance> getEntries() {
        return $ENTRIES;
    }

    public static KVariance valueOf(String r1) {
        return (KVariance) Enum.valueOf(KVariance.class, r1);
    }

    public static KVariance[] values() {
        return (KVariance[]) $VALUES.clone();
    }
}
