package com.arthenica.ffmpegkit;

/* JADX INFO: loaded from: classes.dex */
public class ReturnCode {
    public static int CANCEL;
    public static int SUCCESS;
    private final int value;

    static {
        SUCCESS = 0;
        CANCEL = 255;
    }

    public ReturnCode(int r1) {
        this.value = r1;
    }

    public static boolean isCancel(ReturnCode r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (r2.getValue() != CANCEL) goto L6;
        return true;
    }

    public static boolean isSuccess(ReturnCode r2) {
        if (r2 != null) goto L4;
    L6:
        return false;
    L4:
        if (r2.getValue() != SUCCESS) goto L6;
        return true;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isValueCancel() {
        if (this.value != CANCEL) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean isValueError() {
        if (this.value != SUCCESS) goto L5;
    L7:
        return false;
    L5:
        if (this.value == CANCEL) goto L7;
        return true;
    }

    public boolean isValueSuccess() {
        if (this.value != SUCCESS) goto L5;
        return true;
    L5:
        return false;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
