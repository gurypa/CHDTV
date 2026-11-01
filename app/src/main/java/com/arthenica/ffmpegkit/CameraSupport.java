package com.arthenica.ffmpegkit;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class CameraSupport {
    CameraSupport() {
    }

    static List<String> extractSupportedCameraIds(Context r11) {
        ArrayList r1 = new ArrayList();
        if (Build.VERSION.SDK_INT >= 24) goto L20;
    L19:
        return r1;
    L20:
        CameraManager r2 = (CameraManager) r11.getSystemService("camera");     // Catch: CameraAccessException -> L17
        if (r2 == null) goto L19;
        String[] r3 = r2.getCameraIdList();     // Catch: CameraAccessException -> L17
        int r4 = r3.length;     // Catch: CameraAccessException -> L17
        int r5 = 0;
    L7:
        if (r5 >= r4) goto L19;
        String r6 = r3[r5];     // Catch: CameraAccessException -> L17
        Integer r8 = (Integer) r2.getCameraCharacteristics(r6).get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);     // Catch: CameraAccessException -> L17
        if (r8 != null) goto L11;
    L13:
        if (r8 == null) goto L15;
        r1.add(r6);     // Catch: CameraAccessException -> L17
    L15:
        r5 = r5 + 1;
        goto L7
    L11:
        if (r8.intValue() != 2) goto L13;
        android.util.Log.d("ffmpeg-kit", "Detected camera with id " + r6 + " has LEGACY hardware level which is not supported by Android Camera2 NDK API.");     // Catch: CameraAccessException -> L17
    L17:
        e = move-exception;
        android.util.Log.w("ffmpeg-kit", "Detecting camera ids failed.", e);
        goto L19
    }
}
