package kotlin.coroutines.intrinsics;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Intrinsics.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0081\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/coroutines/intrinsics/CoroutineSingletons;", "", "(Ljava/lang/String;I)V", "COROUTINE_SUSPENDED", "UNDECIDED", "RESUMED", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum CoroutineSingletons extends Enum<CoroutineSingletons> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ CoroutineSingletons[] $VALUES = null;
    public static final CoroutineSingletons COROUTINE_SUSPENDED = null;
    public static final CoroutineSingletons RESUMED = null;
    public static final CoroutineSingletons UNDECIDED = null;

    private static final /* synthetic */ CoroutineSingletons[] $values() {
        return new CoroutineSingletons[]{COROUTINE_SUSPENDED, UNDECIDED, RESUMED};
    }

    static {
        COROUTINE_SUSPENDED = new CoroutineSingletons("COROUTINE_SUSPENDED", 0);
        UNDECIDED = new CoroutineSingletons("UNDECIDED", 1);
        RESUMED = new CoroutineSingletons("RESUMED", 2);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private CoroutineSingletons(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<CoroutineSingletons> getEntries() {
        return $ENTRIES;
    }

    public static CoroutineSingletons valueOf(String r1) {
        return (CoroutineSingletons) Enum.valueOf(CoroutineSingletons.class, r1);
    }

    public static CoroutineSingletons[] values() {
        return (CoroutineSingletons[]) $VALUES.clone();
    }
}
