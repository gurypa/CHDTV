package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public class AbiDetect {
    static final String ARM_V7A = "arm-v7a";
    static final String ARM_V7A_NEON = "arm-v7a-neon";
    private static boolean armV7aNeonLoaded;

    static {
        armV7aNeonLoaded = false;
        NativeLoader.loadFFmpegKitAbiDetect();
        FFmpegKit.class.getName();
        FFmpegKitConfig.class.getName();
        FFprobeKit.class.getName();
    }

    private AbiDetect() {
    }

    public static String getAbi() {
        if (armV7aNeonLoaded == false) goto L7;
        return ARM_V7A_NEON;
    L7:
        return getNativeAbi();
    }

    public static String getCpuAbi() {
        return getNativeCpuAbi();
    }

    static native String getNativeAbi();

    static native String getNativeBuildConf();

    static native String getNativeCpuAbi();

    static native boolean isNativeLTSBuild();

    static void setArmV7aNeonLoaded() {
        armV7aNeonLoaded = true;
    }
}
