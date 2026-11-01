package kotlin.collections;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: AbstractIterator.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/collections/State;", "", "(Ljava/lang/String;I)V", "Ready", "NotReady", "Done", "Failed", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final enum State extends Enum<State> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ State[] $VALUES = null;
    public static final State Done = null;
    public static final State Failed = null;
    public static final State NotReady = null;
    public static final State Ready = null;

    private static final /* synthetic */ State[] $values() {
        return new State[]{Ready, NotReady, Done, Failed};
    }

    static {
        Ready = new State("Ready", 0);
        NotReady = new State("NotReady", 1);
        Done = new State("Done", 2);
        Failed = new State("Failed", 3);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private State(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<State> getEntries() {
        return $ENTRIES;
    }

    public static State valueOf(String r1) {
        return (State) Enum.valueOf(State.class, r1);
    }

    public static State[] values() {
        return (State[]) $VALUES.clone();
    }
}
