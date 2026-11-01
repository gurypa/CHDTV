package com.google.common.cache;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract enum RemovalCause extends Enum<RemovalCause> {
    private static final /* synthetic */ RemovalCause[] $VALUES = null;
    public static final RemovalCause COLLECTED = null;
    public static final RemovalCause EXPIRED = null;
    public static final RemovalCause EXPLICIT = null;
    public static final RemovalCause REPLACED = null;
    public static final RemovalCause SIZE = null;

    private static /* synthetic */ RemovalCause[] $values() {
        return new RemovalCause[]{EXPLICIT, REPLACED, COLLECTED, EXPIRED, SIZE};
    }

    static {
        final String r1 = "EXPLICIT";
        final int r2 = 0;
        EXPLICIT = new C05841(r1, r2);
        final String r3 = "REPLACED";
        final int r4 = 1;
        REPLACED = new C05852(r3, r4);
        final String r5 = "COLLECTED";
        final int r6 = 2;
        COLLECTED = new C05863(r5, r6);
        final String r7 = "EXPIRED";
        final int r8 = 3;
        EXPIRED = new C05874(r7, r8);
        final String r9 = "SIZE";
        final int r10 = 4;
        SIZE = new C05885(r9, r10);
        $VALUES = $values();
    }

    private RemovalCause(String r1, int r2) {
        super(r1, r2);
    }

    /* synthetic */ RemovalCause(String r1, int r2, C05841 r3) {
        this(r1, r2);
    }

    public static RemovalCause valueOf(String r1) {
        return (RemovalCause) Enum.valueOf(RemovalCause.class, r1);
    }

    public static RemovalCause[] values() {
        return (RemovalCause[]) $VALUES.clone();
    }

    abstract boolean wasEvicted();
}
