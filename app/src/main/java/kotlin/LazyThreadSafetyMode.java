package kotlin;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Lazy.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/LazyThreadSafetyMode;", "", "(Ljava/lang/String;I)V", "SYNCHRONIZED", "PUBLICATION", "NONE", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum LazyThreadSafetyMode extends Enum<LazyThreadSafetyMode> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ LazyThreadSafetyMode[] $VALUES = null;
    public static final LazyThreadSafetyMode NONE = null;
    public static final LazyThreadSafetyMode PUBLICATION = null;
    public static final LazyThreadSafetyMode SYNCHRONIZED = null;

    private static final /* synthetic */ LazyThreadSafetyMode[] $values() {
        return new LazyThreadSafetyMode[]{SYNCHRONIZED, PUBLICATION, NONE};
    }

    static {
        SYNCHRONIZED = new LazyThreadSafetyMode("SYNCHRONIZED", 0);
        PUBLICATION = new LazyThreadSafetyMode("PUBLICATION", 1);
        NONE = new LazyThreadSafetyMode("NONE", 2);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private LazyThreadSafetyMode(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<LazyThreadSafetyMode> getEntries() {
        return $ENTRIES;
    }

    public static LazyThreadSafetyMode valueOf(String r1) {
        return (LazyThreadSafetyMode) Enum.valueOf(LazyThreadSafetyMode.class, r1);
    }

    public static LazyThreadSafetyMode[] values() {
        return (LazyThreadSafetyMode[]) $VALUES.clone();
    }
}
