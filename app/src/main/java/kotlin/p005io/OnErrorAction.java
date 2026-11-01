package kotlin.p005io;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Utils.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, m421d2 = {"Lkotlin/io/OnErrorAction;", "", "(Ljava/lang/String;I)V", "SKIP", "TERMINATE", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum OnErrorAction extends Enum<OnErrorAction> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ OnErrorAction[] $VALUES = null;
    public static final OnErrorAction SKIP = null;
    public static final OnErrorAction TERMINATE = null;

    private static final /* synthetic */ OnErrorAction[] $values() {
        return new OnErrorAction[]{SKIP, TERMINATE};
    }

    static {
        SKIP = new OnErrorAction("SKIP", 0);
        TERMINATE = new OnErrorAction("TERMINATE", 1);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private OnErrorAction(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<OnErrorAction> getEntries() {
        return $ENTRIES;
    }

    public static OnErrorAction valueOf(String r1) {
        return (OnErrorAction) Enum.valueOf(OnErrorAction.class, r1);
    }

    public static OnErrorAction[] values() {
        return (OnErrorAction[]) $VALUES.clone();
    }
}
