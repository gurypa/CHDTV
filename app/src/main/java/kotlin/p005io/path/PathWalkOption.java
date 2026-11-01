package kotlin.p005io.path;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: PathWalkOption.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0087\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, m421d2 = {"Lkotlin/io/path/PathWalkOption;", "", "(Ljava/lang/String;I)V", "INCLUDE_DIRECTORIES", "BREADTH_FIRST", "FOLLOW_LINKS", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum PathWalkOption extends Enum<PathWalkOption> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ PathWalkOption[] $VALUES = null;
    public static final PathWalkOption BREADTH_FIRST = null;
    public static final PathWalkOption FOLLOW_LINKS = null;
    public static final PathWalkOption INCLUDE_DIRECTORIES = null;

    private static final /* synthetic */ PathWalkOption[] $values() {
        return new PathWalkOption[]{INCLUDE_DIRECTORIES, BREADTH_FIRST, FOLLOW_LINKS};
    }

    static {
        INCLUDE_DIRECTORIES = new PathWalkOption("INCLUDE_DIRECTORIES", 0);
        BREADTH_FIRST = new PathWalkOption("BREADTH_FIRST", 1);
        FOLLOW_LINKS = new PathWalkOption("FOLLOW_LINKS", 2);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private PathWalkOption(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<PathWalkOption> getEntries() {
        return $ENTRIES;
    }

    public static PathWalkOption valueOf(String r1) {
        return (PathWalkOption) Enum.valueOf(PathWalkOption.class, r1);
    }

    public static PathWalkOption[] values() {
        return (PathWalkOption[]) $VALUES.clone();
    }
}
