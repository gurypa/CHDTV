package kotlin.reflect;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: KVisibility.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0087\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/reflect/KVisibility;", "", "(Ljava/lang/String;I)V", "PUBLIC", "PROTECTED", "INTERNAL", "PRIVATE", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum KVisibility extends Enum<KVisibility> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ KVisibility[] $VALUES = null;
    public static final KVisibility INTERNAL = null;
    public static final KVisibility PRIVATE = null;
    public static final KVisibility PROTECTED = null;
    public static final KVisibility PUBLIC = null;

    private static final /* synthetic */ KVisibility[] $values() {
        return new KVisibility[]{PUBLIC, PROTECTED, INTERNAL, PRIVATE};
    }

    static {
        PUBLIC = new KVisibility("PUBLIC", 0);
        PROTECTED = new KVisibility("PROTECTED", 1);
        INTERNAL = new KVisibility("INTERNAL", 2);
        PRIVATE = new KVisibility("PRIVATE", 3);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private KVisibility(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<KVisibility> getEntries() {
        return $ENTRIES;
    }

    public static KVisibility valueOf(String r1) {
        return (KVisibility) Enum.valueOf(KVisibility.class, r1);
    }

    public static KVisibility[] values() {
        return (KVisibility[]) $VALUES.clone();
    }
}
