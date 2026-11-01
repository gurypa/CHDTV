package kotlin.p005io.path;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: OnErrorResult.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0087\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, m421d2 = {"Lkotlin/io/path/OnErrorResult;", "", "(Ljava/lang/String;I)V", "SKIP_SUBTREE", "TERMINATE", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum OnErrorResult extends Enum<OnErrorResult> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ OnErrorResult[] $VALUES = null;
    public static final OnErrorResult SKIP_SUBTREE = null;
    public static final OnErrorResult TERMINATE = null;

    private static final /* synthetic */ OnErrorResult[] $values() {
        return new OnErrorResult[]{SKIP_SUBTREE, TERMINATE};
    }

    static {
        SKIP_SUBTREE = new OnErrorResult("SKIP_SUBTREE", 0);
        TERMINATE = new OnErrorResult("TERMINATE", 1);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private OnErrorResult(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<OnErrorResult> getEntries() {
        return $ENTRIES;
    }

    public static OnErrorResult valueOf(String r1) {
        return (OnErrorResult) Enum.valueOf(OnErrorResult.class, r1);
    }

    public static OnErrorResult[] values() {
        return (OnErrorResult[]) $VALUES.clone();
    }
}
