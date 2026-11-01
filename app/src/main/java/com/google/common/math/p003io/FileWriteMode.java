package com.google.common.p003io;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final enum FileWriteMode extends Enum<FileWriteMode> {
    private static final /* synthetic */ FileWriteMode[] $VALUES = null;
    public static final FileWriteMode APPEND = null;

    private static /* synthetic */ FileWriteMode[] $values() {
        return new FileWriteMode[]{APPEND};
    }

    static {
        APPEND = new FileWriteMode("APPEND", 0);
        $VALUES = $values();
    }

    private FileWriteMode(String r1, int r2) {
        super(r1, r2);
    }

    public static FileWriteMode valueOf(String r1) {
        return (FileWriteMode) Enum.valueOf(FileWriteMode.class, r1);
    }

    public static FileWriteMode[] values() {
        return (FileWriteMode[]) $VALUES.clone();
    }
}
