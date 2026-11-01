package kotlin.p005io;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: FileTreeWalk.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, m421d2 = {"Lkotlin/io/FileWalkDirection;", "", "(Ljava/lang/String;I)V", "TOP_DOWN", "BOTTOM_UP", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final enum FileWalkDirection extends Enum<FileWalkDirection> {
    private static final /* synthetic */ EnumEntries $ENTRIES = null;
    private static final /* synthetic */ FileWalkDirection[] $VALUES = null;
    public static final FileWalkDirection BOTTOM_UP = null;
    public static final FileWalkDirection TOP_DOWN = null;

    private static final /* synthetic */ FileWalkDirection[] $values() {
        return new FileWalkDirection[]{TOP_DOWN, BOTTOM_UP};
    }

    static {
        TOP_DOWN = new FileWalkDirection("TOP_DOWN", 0);
        BOTTOM_UP = new FileWalkDirection("BOTTOM_UP", 1);
        $VALUES = $values();
        $ENTRIES = EnumEntriesKt.enumEntries($VALUES);
    }

    private FileWalkDirection(String r1, int r2) {
        super(r1, r2);
    }

    public static EnumEntries<FileWalkDirection> getEntries() {
        return $ENTRIES;
    }

    public static FileWalkDirection valueOf(String r1) {
        return (FileWalkDirection) Enum.valueOf(FileWalkDirection.class, r1);
    }

    public static FileWalkDirection[] values() {
        return (FileWalkDirection[]) $VALUES.clone();
    }
}
