package com.arthenica.ffmpegkit;

import androidx.core.os.EnvironmentCompat;

/* JADX INFO: loaded from: classes.dex */
public final enum Abi extends Enum<Abi> {
    private static final /* synthetic */ Abi[] $VALUES = null;
    public static final Abi ABI_ARM = null;
    public static final Abi ABI_ARM64_V8A = null;
    public static final Abi ABI_ARMV7A = null;
    public static final Abi ABI_ARMV7A_NEON = null;
    public static final Abi ABI_UNKNOWN = null;
    public static final Abi ABI_X86 = null;
    public static final Abi ABI_X86_64 = null;
    private final String name;

    private static /* synthetic */ Abi[] $values() {
        return new Abi[]{ABI_ARMV7A_NEON, ABI_ARMV7A, ABI_ARM, ABI_X86, ABI_X86_64, ABI_ARM64_V8A, ABI_UNKNOWN};
    }

    static {
        ABI_ARMV7A_NEON = new Abi("ABI_ARMV7A_NEON", 0, "armeabi-v7a-neon");
        ABI_ARMV7A = new Abi("ABI_ARMV7A", 1, "armeabi-v7a");
        ABI_ARM = new Abi("ABI_ARM", 2, "armeabi");
        ABI_X86 = new Abi("ABI_X86", 3, "x86");
        ABI_X86_64 = new Abi("ABI_X86_64", 4, "x86_64");
        ABI_ARM64_V8A = new Abi("ABI_ARM64_V8A", 5, "arm64-v8a");
        ABI_UNKNOWN = new Abi("ABI_UNKNOWN", 6, EnvironmentCompat.MEDIA_UNKNOWN);
        $VALUES = $values();
    }

    private Abi(String r1, int r2, String r3) {
        super(r1, r2);
        this.name = r3;
    }

    public static Abi from(String r1) {
        if (r1 != null) goto L6;
        return ABI_UNKNOWN;
    L6:
        if (r1.equals(ABI_ARM.getName()) == false) goto L10;
        return ABI_ARM;
    L10:
        if (r1.equals(ABI_ARMV7A.getName()) == false) goto L14;
        return ABI_ARMV7A;
    L14:
        if (r1.equals(ABI_ARMV7A_NEON.getName()) == false) goto L18;
        return ABI_ARMV7A_NEON;
    L18:
        if (r1.equals(ABI_ARM64_V8A.getName()) == false) goto L22;
        return ABI_ARM64_V8A;
    L22:
        if (r1.equals(ABI_X86.getName()) == false) goto L26;
        return ABI_X86;
    L26:
        if (r1.equals(ABI_X86_64.getName()) == false) goto L30;
        return ABI_X86_64;
    L30:
        return ABI_UNKNOWN;
    }

    public static Abi valueOf(String r1) {
        return (Abi) Enum.valueOf(Abi.class, r1);
    }

    public static Abi[] values() {
        return (Abi[]) $VALUES.clone();
    }

    public String getName() {
        return this.name;
    }
}
