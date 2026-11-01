package com.arthenica.ffmpegkit;

import android.os.Build;
import com.arthenica.smartexception.java.Exceptions;
import es.antonio.m3u.RecordingNative;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class NativeLoader {
    static final String[] FFMPEG_LIBRARIES = null;
    static final String[] LIBRARIES_LINKED_WITH_CXX = null;

    static {
        FFMPEG_LIBRARIES = new String[]{"avutil", "swscale", "swresample", "avcodec", "avformat", "avfilter", "avdevice"};
        LIBRARIES_LINKED_WITH_CXX = new String[]{"chromaprint", "openh264", "rubberband", "snappy", "srt", "tesseract", "x265", "zimg", "libilbc"};
    }

    public NativeLoader() {
    }

    static void enableRedirection() {
        if (isTestModeDisabled() == false) goto L6;
        FFmpegKitConfig.enableRedirection();
        return;
    }

    static String getDeviceDebugInformation() {
        return "brand: " + Build.BRAND + ", model: " + Build.MODEL + ", device: " + Build.DEVICE + ", api level: " + Build.VERSION.SDK_INT + ", abis: " + FFmpegKitConfig.argumentsToString(Build.SUPPORTED_ABIS) + ", 32bit abis: " + FFmpegKitConfig.argumentsToString(Build.SUPPORTED_32_BIT_ABIS) + ", 64bit abis: " + FFmpegKitConfig.argumentsToString(Build.SUPPORTED_64_BIT_ABIS);
    }

    static boolean isTestModeDisabled() {
        if (System.getProperty("enable.ffmpeg.kit.test.mode") != null) goto L5;
        return true;
    L5:
        return false;
    }

    static String loadAbi() {
        if (isTestModeDisabled() == false) goto L7;
        return AbiDetect.getAbi();
    L7:
        return Abi.ABI_X86_64.getName();
    }

    static String loadBuildDate() {
        if (isTestModeDisabled() == false) goto L7;
        return FFmpegKitConfig.getBuildDate();
    L7:
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
    }

    private static List<String> loadExternalLibraries() {
        if (isTestModeDisabled() == false) goto L7;
        return Packages.getExternalLibraries();
    L7:
        return Collections.emptyList();
    }

    static boolean loadFFmpeg() {
        return false;
    }

    static void loadFFmpegKit(boolean r5) {
        boolean r0 = false;
        if (r5 == false) goto L5;
    L10:
        if (r0 == true) goto L15;
        loadLibrary("ffmpegkit");
        return;
    L15:
        return;
    L5:
        if ("arm-v7a".equals(loadNativeAbi()) == false) goto L10;
        loadLibrary("ffmpegkit_armv7a_neon");     // Catch: Error -> L8
        r0 = true;
        AbiDetect.setArmV7aNeonLoaded();     // Catch: Error -> L8
    L8:
        e = move-exception;
        android.util.Log.i("ffmpeg-kit", String.format("NEON supported armeabi-v7a ffmpegkit library not found. Loading default armeabi-v7a library.%s", new Object[]{Exceptions.getStackTraceString(e)}));
        goto L10
    }

    static void loadFFmpegKitAbiDetect() {
        loadLibrary("ffmpegkit_abidetect");
    }

    static boolean loadIsLTSBuild() {
        if (isTestModeDisabled() == true) goto L5;
        return true;
    L5:
        return AbiDetect.isNativeLTSBuild();
    }

    private static void loadLibrary(String r5) {
        if (isTestModeDisabled() == true) goto L10;
        return;
    L10:
        RecordingNative.loadLibrary(r5);     // Catch: UnsatisfiedLinkError -> L6
        return;
    L6:
        e = move-exception;
        throw new Error(String.format("FFmpegKit failed to start on %s.", new Object[]{getDeviceDebugInformation()}), e);
    }

    static int loadLogLevel() {
        if (isTestModeDisabled() == false) goto L7;
        return FFmpegKitConfig.getNativeLogLevel();
    L7:
        return Level.AV_LOG_DEBUG.getValue();
    }

    private static String loadNativeAbi() {
        if (isTestModeDisabled() == false) goto L7;
        return AbiDetect.getNativeAbi();
    L7:
        return Abi.ABI_X86_64.getName();
    }

    static String loadPackageName() {
        if (isTestModeDisabled() == true) goto L5;
        return "test";
    L5:
        return Packages.getPackageName();
    }

    static String loadVersion() {
        if (isTestModeDisabled() == false) goto L7;
        return FFmpegKitConfig.getVersion();
    L7:
        if (loadIsLTSBuild() == true) goto L9;
        return "6.0";
    L9:
        return String.format("%s-lts", new Object[]{"6.0"});
    }
}
